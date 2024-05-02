package com.example.demo;

import com.example.demo.mapper.TransactionMapper;
import com.example.demo.model.Currency;
import com.example.demo.model.Limit;
import com.example.demo.model.LimitType;
import com.example.demo.model.Transaction;
import com.example.demo.payload.TransactionRequestDTO;
import com.example.demo.payload.TransactionResponseDTO;
import com.example.demo.repository.LimitRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.LimitService;
import com.example.demo.service.TransactionService;
import com.example.demo.utils.AppConst;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private LimitService limitService;
    @Mock
    private LimitRepository limitRepository;
    @InjectMocks
    private TransactionService transactionService;

    private Transaction createTransaction(Currency currency, Limit limit, String datetimeStr) {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCurrency(currency);
        transaction.setAccountFrom(12345L);
        transaction.setAccountTo(54321L);
        transaction.setSum(new BigDecimal("1500"));
        transaction.setDatetime(OffsetDateTime.parse(datetimeStr));
        transaction.setLimit(limit);
        return transaction;
    }
    private Transaction createTransaction(Long id,Currency currency, Limit limit, String datetimeStr, BigDecimal sum) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setCurrency(currency);
        transaction.setAccountFrom(12345L);
        transaction.setAccountTo(54321L);
        transaction.setSum(sum);
        transaction.setDatetime(OffsetDateTime.parse(datetimeStr));
        transaction.setLimit(limit);
        return transaction;
    }

    private Limit createLimit(BigDecimal limitValue, String datetimeStr) {
        Limit limit = new Limit();
        limit.setId(1L);
        limit.setLimitValue(limitValue);
        limit.setType(LimitType.SERVICE);
        limit.setCreatedDate(OffsetDateTime.parse(datetimeStr));
        return limit;
    }
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddTransactionWithExistingLimit() {
        // Mock data
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setAccountFrom(123L);
        requestDTO.setAccountTo(9999999999L);
        requestDTO.setCurrencyShortname("KZT");
        requestDTO.setSum(BigDecimal.valueOf(10000.45));
        requestDTO.setDatetime(OffsetDateTime.now());
        requestDTO.setType(LimitType.SERVICE);


        Currency currency = new Currency();
        currency.setId(1L);
        currency.setCloseExchange(BigDecimal.valueOf(442.10));
        currency.setPreviousCloseExchange(BigDecimal.valueOf(430));
        currency.setSymbol("KZT");
        currency.setExchangeDate(OffsetDateTime.now());

        Limit limit = new Limit();
        limit.setId(1L);
        limit.setLimitValue(AppConst.LIMIT_VALUE);
        limit.setCreatedDate(OffsetDateTime.now());


        Transaction transaction = TransactionMapper.INSTANCE.convertDTOtoModel(requestDTO);
        transaction.setCurrency(currency);
        transaction.setLimit(limit);

        when(transactionMapper.convertDTOtoModel(requestDTO)).thenReturn(transaction);
        when(limitService.getLimit(LimitType.SERVICE, requestDTO.getDatetime())).thenReturn(limit);
        when(transactionRepository.save(transaction)).thenReturn(transaction);


        transactionService.addTransaction(requestDTO, currency);

        verify(limitService).getLimit(LimitType.SERVICE, requestDTO.getDatetime());
        verify(transactionRepository).save(transaction);
    }

    @Test
    public void testGetExceeded() {
        // Mock data for Currency
        Currency currency = new Currency();
        currency.setId(1L);
        currency.setSymbol("USD");
        currency.setCloseExchange(new BigDecimal("1.0"));
        currency.setPreviousCloseExchange(new BigDecimal("0.9"));
        currency.setExchangeDate(OffsetDateTime.now());

        // Mock data for Limit
        Limit limit = new Limit();
        limit.setId(1L);
        limit.setLimitValue(new BigDecimal("1000"));
        limit.setType(LimitType.SERVICE);
        limit.setCreatedDate(OffsetDateTime.now());

        // Mock data for Transaction
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);
        transaction1.setCurrency(currency);
        transaction1.setAccountFrom(12345L);
        transaction1.setAccountTo(54321L);
        transaction1.setSum(new BigDecimal("1500"));
        transaction1.setDatetime(OffsetDateTime.now());
        transaction1.setLimit(limit);


        when(transactionRepository.findAllByLimitType(any(), any(Sort.class))).thenReturn(Collections.singletonList(transaction1));
        List<TransactionResponseDTO> result = transactionService.getExceededByType(LimitType.SERVICE);






        assertEquals(1, result.size());
    }

    @Test
    public void testGetExceeded_withDateTimes() {

        Currency currency = new Currency();
        currency.setId(1L);
        currency.setSymbol("USD");
        currency.setCloseExchange(new BigDecimal("1.0"));
        currency.setPreviousCloseExchange(new BigDecimal("0.9"));
        currency.setExchangeDate(OffsetDateTime.now());

        Limit limit = new Limit();
        limit.setId(1L);
        limit.setLimitValue(new BigDecimal("1000"));
        limit.setType(LimitType.SERVICE);
        limit.setCreatedDate(OffsetDateTime.now());


        Transaction transaction1 = createTransaction(currency, limit, "2024-05-01T10:00:00Z");
        Transaction transaction2 = createTransaction(currency, limit, "2024-05-01T11:00:00Z");
        Transaction transaction3 = createTransaction(currency, limit, "2024-05-01T12:00:00Z");


        List<Transaction> sorted = Arrays.asList(transaction1, transaction2, transaction3);
        when(transactionRepository.findAllByLimitType(any(), any(Sort.class))).thenReturn(sorted);

        List<TransactionResponseDTO> result = transactionService.getExceededByType(LimitType.SERVICE);




        assertEquals(3, result.size()); // Assuming all transactions exceed the limit
    }


    @Test
    public void testGetExceeded_withCase1() {
        // Mock data for Currency
        Currency currency = new Currency();
        currency.setId(1L);
        currency.setSymbol("USD");
        currency.setCloseExchange(new BigDecimal("1.0"));
        currency.setPreviousCloseExchange(new BigDecimal("0.9"));
        currency.setExchangeDate(OffsetDateTime.now());

        // Mock data for Limits
        Limit limit1 = createLimit(new BigDecimal("1000"), "2022-01-01T00:00:00Z");
        Limit limit2 = createLimit(new BigDecimal("2000"), "2022-01-10T00:00:00Z");

        // Mock data for Transactions
        Transaction transaction1 = createTransaction(1L,currency, limit1, "2022-01-02T00:00:00Z", new BigDecimal("500"));
        Transaction transaction2 = createTransaction(2L,currency, limit1, "2022-01-03T00:00:00Z", new BigDecimal("600"));
        Transaction transaction3 = createTransaction(3L,currency, limit2, "2022-01-11T00:00:00Z", new BigDecimal("100"));
        Transaction transaction4 = createTransaction(4L,currency, limit2, "2022-01-12T00:00:00Z", new BigDecimal("700"));
        Transaction transaction5 = createTransaction(5L,currency, limit2, "2022-01-13T00:00:00Z", new BigDecimal("100"));
        Transaction transaction6 = createTransaction(6L,currency, limit2, "2022-01-13T01:00:00Z", new BigDecimal("100"));


        List<Transaction> sortedList = Arrays.asList(
                transaction1, transaction2, transaction3, transaction4, transaction5, transaction6);

        when(transactionRepository.findAllByLimitType(any(), any(Sort.class))).thenReturn(sortedList);



        List<TransactionResponseDTO> result = transactionService.getExceededByType(LimitType.SERVICE);








        // Assertions
        assertEquals(2, result.size()); // Assuming two transactions exceed the limit
        assertEquals("2022-01-03T00:00Z", result.get(0).getDatetime().toString()); // First transaction should exceed the limit
        assertEquals("2022-01-13T01:00Z", result.get(1).getDatetime().toString()); // Second transaction should exceed the limit
    }

    @Test
    public void testGetExceeded_withCase2() {
        Currency currency = new Currency();
        currency.setId(1L);
        currency.setSymbol("USD");
        currency.setCloseExchange(new BigDecimal("1.0"));
        currency.setPreviousCloseExchange(new BigDecimal("0.9"));
        currency.setExchangeDate(OffsetDateTime.now());

        Limit limit1 = createLimit(new BigDecimal("1000"), "2022-02-01T00:00:00Z");
        Limit limit2 = createLimit(new BigDecimal("400"), "2022-02-10T00:00:00Z");

        Transaction transaction1 = createTransaction(1L, currency, limit1, "2022-02-02T00:00:00Z", new BigDecimal("500"));
        Transaction transaction2 = createTransaction(2L, currency, limit1, "2022-02-03T00:00:00Z", new BigDecimal("100"));
        Transaction transaction3 = createTransaction(3L, currency, limit2, "2022-02-11T00:00:00Z", new BigDecimal("100"));
        Transaction transaction4 = createTransaction(4L, currency, limit2, "2022-02-12T00:00:00Z", new BigDecimal("100"));

        List<Transaction> sortedList = Arrays.asList(
                transaction1, transaction2, transaction3, transaction4);

        when(transactionRepository.findAllByLimitType(any(), any(Sort.class))).thenReturn(sortedList);

        List<TransactionResponseDTO> result = transactionService.getExceededByType(LimitType.SERVICE);
        assertEquals(2, result.size());
        assertEquals("2022-02-11T00:00Z", result.get(0).getDatetime().toString());
        assertEquals("2022-02-12T00:00Z", result.get(1).getDatetime().toString());
    }

    @Test
    public void testGetExceeded_withCase1KZT() {
        // Mock data for Currency
        Currency currency = new Currency();
        currency.setId(1L);
        currency.setSymbol("KZT");
        currency.setCloseExchange(new BigDecimal("425.50"));
        currency.setPreviousCloseExchange(new BigDecimal("420.75"));
        currency.setExchangeDate(OffsetDateTime.now());

        // Mock data for Limits
        Limit limit1 = createLimit(new BigDecimal("1000"), "2022-01-01T00:00:00Z");
        Limit limit2 = createLimit(new BigDecimal("2000"), "2022-01-10T00:00:00Z");

        // Mock data for Transactions
        Transaction transaction1 = createTransaction(1L, currency, limit1, "2022-01-02T00:00:00Z", new BigDecimal("212750"));
        Transaction transaction2 = createTransaction(2L, currency, limit1, "2022-01-03T00:00:00Z", new BigDecimal("255300"));
        Transaction transaction3 = createTransaction(3L, currency, limit2, "2022-01-11T00:00:00Z", new BigDecimal("42500"));
        Transaction transaction4 = createTransaction(4L, currency, limit2, "2022-01-12T00:00:00Z", new BigDecimal("297850"));
        Transaction transaction5 = createTransaction(5L, currency, limit2, "2022-01-13T00:00:00Z", new BigDecimal("42500"));
        Transaction transaction6 = createTransaction(6L, currency, limit2, "2022-01-13T01:00:00Z", new BigDecimal("42500"));

        List<Transaction> sortedList = Arrays.asList(
                transaction1, transaction2, transaction3, transaction4, transaction5, transaction6);

        when(transactionRepository.findAllByLimitType(any(), any(Sort.class))).thenReturn(sortedList);

        List<TransactionResponseDTO> result = transactionService.getExceededByType(LimitType.SERVICE);

        // Assertions
        assertEquals(2, result.size()); // Assuming two transactions exceed the limit
        assertEquals("2022-01-03T00:00Z", result.get(0).getDatetime().toString()); // First transaction should exceed the limit
        assertEquals("2022-01-13T01:00Z", result.get(1).getDatetime().toString()); // Second transaction should exceed the limit
    }

    @Test
    public void testGetExceeded_withMounths() {
        Currency currency = new Currency();
        currency.setId(1L);
        currency.setSymbol("USD");
        currency.setCloseExchange(new BigDecimal("1.0"));
        currency.setPreviousCloseExchange(new BigDecimal("0.9"));
        currency.setExchangeDate(OffsetDateTime.now());

        Limit limit1 = createLimit(new BigDecimal("1000"), "2022-01-01T00:00:00Z");

        // Mock data for Transactions
        Transaction transaction1 = createTransaction(1L,currency, limit1, "2022-01-02T00:00:00Z", new BigDecimal("500"));
        Transaction transaction2 = createTransaction(2L,currency, limit1, "2022-01-03T00:00:00Z", new BigDecimal("600"));
        Transaction transaction3 = createTransaction(3L,currency, limit1, "2022-02-11T00:00:00Z", new BigDecimal("400"));
        Transaction transaction4 = createTransaction(4L,currency, limit1, "2022-03-12T00:00:00Z", new BigDecimal("700"));


        List<Transaction> sortedList = Arrays.asList(
                transaction1, transaction2, transaction3, transaction4);

        when(transactionRepository.findAllByLimitType(any(), any(Sort.class))).thenReturn(sortedList);
        List<TransactionResponseDTO> result = transactionService.getExceededByType(LimitType.SERVICE);


        // Assertions
        assertEquals(1, result.size()); // Assuming two transactions exceed the limit
        assertEquals("2022-01-03T00:00Z", result.get(0).getDatetime().toString()); // First transaction should exceed the limit// Second transaction should exceed the limit
    }

    @Test
    public void testGetExceeded_withMounths2() {
        Currency currency = new Currency();
        currency.setId(1L);
        currency.setSymbol("USD");
        currency.setCloseExchange(new BigDecimal("1.0"));
        currency.setPreviousCloseExchange(new BigDecimal("0.9"));
        currency.setExchangeDate(OffsetDateTime.now());

        Limit limit1 = createLimit(new BigDecimal("1000"), "2022-01-01T00:00:00Z");

        // Mock data for Transactions
        Transaction transaction1 = createTransaction(1L,currency, limit1, "2022-01-02T00:00:00Z", new BigDecimal("500"));
        Transaction transaction2 = createTransaction(2L,currency, limit1, "2022-01-03T00:00:00Z", new BigDecimal("600"));
        Transaction transaction3 = createTransaction(3L,currency, limit1, "2022-02-11T00:00:00Z", new BigDecimal("400"));
        Transaction transaction4 = createTransaction(4L,currency, limit1, "2023-02-12T00:00:00Z", new BigDecimal("700"));


        List<Transaction> sortedList = Arrays.asList(
                transaction1, transaction2, transaction3, transaction4);

        when(transactionRepository.findAllByLimitType(any(), any(Sort.class))).thenReturn(sortedList);
        List<TransactionResponseDTO> result = transactionService.getExceededByType(LimitType.SERVICE);


        // Assertions
        assertEquals(1, result.size()); // Assuming two transactions exceed the limit
        assertEquals("2022-01-03T00:00Z", result.get(0).getDatetime().toString()); // First transaction should exceed the limit// Second transaction should exceed the limit
    }
}
