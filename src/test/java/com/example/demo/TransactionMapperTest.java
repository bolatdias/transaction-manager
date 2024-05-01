package com.example.demo;

import com.example.demo.mapper.TransactionMapper;
import com.example.demo.model.Currency;
import com.example.demo.model.Limit;
import com.example.demo.model.LimitType;
import com.example.demo.model.Transaction;
import com.example.demo.payload.TransactionRequestDTO;
import com.example.demo.payload.TransactionResponseDTO;
import com.example.demo.utils.AppConst;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionMapperTest {

    private final TransactionMapper mapper = Mappers.getMapper(TransactionMapper.class);

    @Test
    public void testConvertDTOtoModel() {
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setAccountFrom(123456L);
        requestDTO.setAccountTo(654321L);
        requestDTO.setSum(BigDecimal.valueOf(100.0));
        requestDTO.setType(LimitType.SERVICE);
        requestDTO.setDatetime(OffsetDateTime.now());

        Transaction transaction = mapper.convertDTOtoModel(requestDTO);

        assertEquals(requestDTO.getAccountFrom(), transaction.getAccountFrom());
        assertEquals(requestDTO.getAccountTo(), transaction.getAccountTo());
        assertEquals(requestDTO.getSum(), transaction.getSum());
        assertEquals(requestDTO.getType(), transaction.getExpenseCategory());
        assertEquals(requestDTO.getDatetime(), transaction.getDatetime());
    }

    @Test
    public void testConvertModelToDTO() {
        Currency currency=new Currency();
        currency.setSymbol("KZT");

        Limit limit=new Limit();
        limit.setLimitValue(AppConst.LIMIT_VALUE);

        Transaction transaction = new Transaction();
        transaction.setAccountFrom(123456L);
        transaction.setAccountTo(654321L);
        transaction.setSum(BigDecimal.valueOf(100.0));
        transaction.setExpenseCategory(LimitType.SERVICE);
        transaction.setCurrency(currency);
        transaction.setDatetime(OffsetDateTime.now());
        transaction.setLimit(limit);

        TransactionResponseDTO responseDTO = mapper.convertModelToDTO(transaction);

        assertEquals(transaction.getAccountFrom(), responseDTO.getAccountFrom());
        assertEquals(transaction.getAccountTo(), responseDTO.getAccountTo());
        assertEquals(transaction.getCurrency().getSymbol(), responseDTO.getCurrencyShortname());
        assertEquals(transaction.getSum(), responseDTO.getSum());
        assertEquals(transaction.getExpenseCategory(), responseDTO.getExpenseCategory());
        assertEquals(transaction.getDatetime(), responseDTO.getDatetime());
        assertEquals(transaction.getLimit().getLimitValue(), responseDTO.getLimitSum());
        assertEquals(transaction.getDatetime(), responseDTO.getLimitDatetime());
        assertEquals(AppConst.BASE_CURRENCY, responseDTO.getLimitCurrencyShortname());
    }



}
