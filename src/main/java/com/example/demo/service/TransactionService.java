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
        Limit limit = limitService.getLimit(requestDTO.getType(), requestDTO.getDatetime());
        Transaction transaction = TransactionMapper.INSTANCE.convertDTOtoModel(requestDTO);
        transaction.setCurrency(currency);

        transaction.setLimit(limit);
        transactionRepository.save(transaction);
    }


    public List<TransactionResponseDTO> getExceeded() {
        List<TransactionResponseDTO> responseDTOS = new ArrayList<>();
        for (LimitType limitType : LimitType.values()) {
            responseDTOS.addAll(getExceededByType(limitType));
        }
        return responseDTOS;
    }


    public List<TransactionResponseDTO> getExceededByType(LimitType limitType) {
        List<TransactionResponseDTO> responseDTOs = new ArrayList<>();
        List<Transaction> list = transactionRepository.findAllByLimitType(limitType, Sort.by("datetime"));

        if (list.isEmpty()) {
            return responseDTOs;
        }

        BigDecimal value = list.get(0).getLimit().getLimitValue();
        BigDecimal remainValue = new BigDecimal(String.valueOf(value));
        OffsetDateTime lastDate = list.get(0).getDatetime();

        for (Transaction transaction : list) {
            Limit limit = transaction.getLimit();
            Currency currency = transaction.getCurrency();


            BigDecimal rate = getExchangeRate(OffsetDateTime.now(), currency);
            BigDecimal currValue = limit.getLimitValue();

            if (value.compareTo(currValue) != 0) {
                remainValue = remainValue.add(currValue.subtract(value));
                value = currValue;
            }


            OffsetDateTime datetime = transaction.getDatetime();
            if (refreshRemain(datetime, lastDate)) {
                lastDate = datetime;
                remainValue = value;
            }



            remainValue = remainValue.subtract(transaction.getSum().divide(rate, 2, BigDecimal.ROUND_HALF_DOWN));

            logger.info("Date: " + datetime + "remain value: " + remainValue);
            if (remainValue.compareTo(BigDecimal.ZERO) < 0) {
                responseDTOs.add(TransactionMapper.INSTANCE.convertModelToDTO(transaction));
            }


        }

        return responseDTOs;
    }

    private boolean refreshRemain(OffsetDateTime datetime, OffsetDateTime lastDate) {
        return datetime.getMonth() != lastDate.getMonth() || datetime.getYear() != lastDate.getYear();
    }


    private BigDecimal getExchangeRate(OffsetDateTime now, Currency currency) {
        if (now.getDayOfYear() == currency.getExchangeDate().getDayOfYear()) {
            return currency.getCloseExchange();
        } else {
            return currency.getPreviousCloseExchange();
        }
    }


}
