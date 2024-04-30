package com.example.demo.mapper;

import com.example.demo.model.Transaction;
import com.example.demo.payload.TransactionRequestDTO;
import com.example.demo.payload.TransactionResponseDTO;
import com.example.demo.utils.AppConst;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Component
public class TransactionMapper {

    public Transaction convertDTOtoModel(TransactionRequestDTO requestDTO) {
        Transaction transaction = new Transaction();

        transaction.setAccountFrom(requestDTO.getAccountFrom());
        transaction.setAccountTo(requestDTO.getAccountTo());
        transaction.setSum(requestDTO.getSum());
        transaction.setExpenseCategory(requestDTO.getType());
        transaction.setDatetime(requestDTO.getDatetime());
        return transaction;
    }

    public TransactionResponseDTO convertModelToDTO(Transaction transaction) {
        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setAccountFrom(transaction.getAccountFrom());
        responseDTO.setAccountTo(transaction.getAccountTo());
        responseDTO.setCurrencyShortname(transaction.getCurrency().getSymbol());
        responseDTO.setSum(transaction.getSum());
        responseDTO.setExpenseCategory(transaction.getExpenseCategory());
        responseDTO.setDatetime(transaction.getDatetime());

        responseDTO.setLimitSum(transaction.getLimit().getLimitValue());
        responseDTO.setLimitDatetime(transaction.getDatetime());
        responseDTO.setLimitCurrencyShortname(AppConst.BASE_CURRENCY);
        return responseDTO;
    }
}
