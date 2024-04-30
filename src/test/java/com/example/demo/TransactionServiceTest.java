package com.example.demo;

import com.example.demo.mapper.TransactionMapper;
import com.example.demo.model.Currency;
import com.example.demo.model.Limit;
import com.example.demo.model.LimitType;
import com.example.demo.model.Transaction;
import com.example.demo.payload.TransactionRequestDTO;
import com.example.demo.repository.LimitRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.service.TransactionService;
import com.example.demo.utils.AppConst;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private LimitRepository limitRepository;

    @InjectMocks
    private TransactionService transactionService;


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
        currency.setExchangeDate(LocalDateTime.now());

        Limit limit = new Limit();
        limit.setType(LimitType.SERVICE); // Adjusted limit type to match the requestDTO type
        limit.setLimitValue(AppConst.LIMIT_VALUE);
        limit.setRemainValue(AppConst.LIMIT_VALUE);
        limit.setCreateDate(LocalDateTime.now());

        when(limitRepository.findFirstByType(LimitType.SERVICE)).thenReturn(Optional.of(limit));
        // Invoke the method
        transactionService.addTransaction(requestDTO, currency);

        // Verify that the limit's remainValue is updated correctly to 950
        assertEquals(BigDecimal.valueOf(950), limit.getRemainValue());

        verify(limitRepository).findFirstByType(LimitType.SERVICE);
        verify(limitRepository).save(limit);
    }

    @Test
    public void addTransaction_LimitRemainValueUpdated() {

        // Mock data
        TransactionRequestDTO requestDTO = new TransactionRequestDTO();
        requestDTO.setType(LimitType.SERVICE);
        requestDTO.setSum(BigDecimal.valueOf(21000));

        Currency currency = new Currency();
        currency.setExchangeDate(LocalDateTime.now());
        currency.setCloseExchange(BigDecimal.valueOf(420)); // Assuming close exchange rate

        Limit limit = new Limit();
        limit.setType(LimitType.SERVICE);
        limit.setCreateDate(LocalDateTime.now()); // Assuming limit set this month
        limit.setLimitValue(BigDecimal.valueOf(1000)); // Assuming limit value

        given(transactionMapper.convertDTOtoModel(requestDTO)).willReturn(new Transaction());
        given(limitRepository.findFirstByType(LimitType.SERVICE)).willReturn(Optional.of(limit));


        transactionService.addTransaction(requestDTO, currency);

        // Verify that transaction and limit are saved
        verify(limitRepository).save(any(Limit.class));

        // Check the updated limit remain value
        assertEquals(BigDecimal.valueOf(500), limit.getRemainValue()); // Assuming limit is not exceeded
    }

}
