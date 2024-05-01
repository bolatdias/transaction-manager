package com.example.demo.service;


import com.example.demo.mapper.TransactionMapper;
import com.example.demo.model.Currency;
import com.example.demo.model.Limit;
import com.example.demo.model.LimitType;
import com.example.demo.model.Transaction;
import com.example.demo.payload.TransactionRequestDTO;
import com.example.demo.payload.TransactionResponseDTO;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final static Logger logger = Logger.getLogger(TransactionService.class.getName());
    private final TransactionRepository transactionRepository;
    private final LimitService limitService;

    @Transactional
    public void addTransaction(TransactionRequestDTO requestDTO, Currency currency) {
        Transaction transaction = TransactionMapper.INSTANCE.convertDTOtoModel(requestDTO);
        transaction.setCurrency(currency);

        Limit limit = limitService.getLimit(requestDTO.getType(), requestDTO.getDatetime());

        transaction.setLimit(limit);
        transactionRepository.save(transaction);
    }


    public List<TransactionResponseDTO> getExceededTransactions() {
        List<TransactionResponseDTO> responseDTOS = new ArrayList<>();
        for (LimitType limitType : LimitType.values()) {
            responseDTOS.addAll(getExceededTransactionsByType(limitType));
        }
        return responseDTOS;
    }

    public List<TransactionResponseDTO> getExceededTransactionsByType(LimitType limitType) {
        List<TransactionResponseDTO> responseDTOs = new ArrayList<>();

        List<Transaction> list = transactionRepository.findAllByLimitType(limitType, Sort.by("datetime"));
        if(list.isEmpty()) {
            return responseDTOs;
        }
        OffsetDateTime now = OffsetDateTime.now();

        HashMap<Limit, OffsetDateTime> limitsLastRefreshTime = new HashMap<>();
        HashMap<Currency, BigDecimal> currenciesWithRate = new HashMap<>();


        BigDecimal value = list.get(0).getLimit().getLimitValue();
        BigDecimal remainValue = new BigDecimal(String.valueOf(value));


        for (Transaction transaction : list) {
            Limit limit = transaction.getLimit();

            limitsLastRefreshTime.put(limit, limit.getCreatedDate());

            Currency currency = transaction.getCurrency();

            if (!currenciesWithRate.containsKey(currency)) {
                BigDecimal rate = getExchangeRate(now, currency);
                currenciesWithRate.put(currency, rate);
            }


            BigDecimal rate = currenciesWithRate.get(currency);

            BigDecimal currValue = limit.getLimitValue();
            if (value.compareTo(currValue) != 0) {

                remainValue = remainValue.add(currValue.subtract(value));
                value = currValue;
            }

            OffsetDateTime lastRefreshedTime = limitsLastRefreshTime.get(limit);
            if (limitService.validateByMonth(limit, lastRefreshedTime, now)) {
                limitsLastRefreshTime.put(limit, now);
                remainValue = remainValue.add(currValue);
            }

            remainValue = remainValue.subtract(transaction.getSum().divide(rate, 2, BigDecimal.ROUND_HALF_UP));


            if (remainValue.compareTo(BigDecimal.ZERO) < 0) {
                responseDTOs.add(TransactionMapper.INSTANCE.convertModelToDTO(transaction));
            }

            logger.info(value + " " + currValue + " " + remainValue + " " + rate);

        }

        return responseDTOs;
    }

    private BigDecimal getExchangeRate(OffsetDateTime now, Currency currency) {
        if (now.getDayOfYear() == currency.getExchangeDate().getDayOfYear()) {
            return currency.getCloseExchange();
        } else {
            return currency.getPreviousCloseExchange();
        }
    }


}
