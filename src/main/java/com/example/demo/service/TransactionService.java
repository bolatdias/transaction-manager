package com.example.demo.service;


import com.example.demo.mapper.TransactionMapper;
import com.example.demo.model.Currency;
import com.example.demo.model.Limit;
import com.example.demo.model.Transaction;
import com.example.demo.payload.PagedResponse;
import com.example.demo.payload.TransactionRequestDTO;
import com.example.demo.payload.TransactionResponseDTO;
import com.example.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final static Logger logger = Logger.getLogger(TransactionService.class.getName());
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final LimitService limitService;

    @Transactional
    public void addTransaction(TransactionRequestDTO requestDTO, Currency currency) {
        Transaction transaction = transactionMapper.convertDTOtoModel(requestDTO);
        transaction.setCurrency(currency);


        BigDecimal rate;
        if (currency.getExchangeDate().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()) {
            rate = currency.getCloseExchange();
        } else {
            rate = currency.getPreviousCloseExchange();
        }

        Limit limit = limitService.getLastLimit(requestDTO.getType());

        BigDecimal sum = requestDTO.getSum().divide(rate, 2);
        BigDecimal remainValue = limit.getRemainValue().subtract(sum);
        limit.setRemainValue(remainValue);

        transaction.setLimitExceeded(remainValue.compareTo(BigDecimal.ZERO) < 0);

        limitService.saveLimit(limit);
        transaction.setLimit(limit);
        transactionRepository.save(transaction);

    }


    public PagedResponse<TransactionResponseDTO> getTransactions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactions = transactionRepository.findAll(pageable);

        List<TransactionResponseDTO> list = new ArrayList<>();

        for (Transaction transaction : transactions.getContent()) {
            TransactionResponseDTO dto = transactionMapper.convertModelToDTO(transaction);
            list.add(dto);
        }

        PagedResponse<TransactionResponseDTO> pagedResponse = new PagedResponse<>();
        pagedResponse.setTotalPages(transactions.getTotalPages());
        pagedResponse.setTotalElements(transactions.getTotalElements());
        pagedResponse.setPage(page);
        pagedResponse.setSize(size);
        pagedResponse.setLast(transactions.isLast());
        pagedResponse.setContent(list);
        return pagedResponse;
    }

}
