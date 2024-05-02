package com.example.demo;

import com.example.demo.model.Limit;
import com.example.demo.service.LimitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LimitServiceTest {
    @Mock
    private LimitService limitServiceMock;

    @InjectMocks
    private LimitService limitService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testIsMonth_Equal_True() {
        OffsetDateTime lastRefreshDate = OffsetDateTime.now().minus(1, ChronoUnit.MONTHS);
        Limit limit = new Limit();

        OffsetDateTime now = OffsetDateTime.now();



        boolean result = limitService.isMonthEqual(lastRefreshDate, now);

        assertEquals(false, result);
    }

    @Test
    public void testIsMonth_Equal_False() {
        OffsetDateTime lastRefreshDate = OffsetDateTime.now().minus(1, ChronoUnit.HOURS); // Less than a month ago
        OffsetDateTime now = OffsetDateTime.now();



        boolean result = limitService.isMonthEqual(lastRefreshDate,now);

        assertEquals(true, result);
    }

    @Test
    public void testValidateByYear_MonthEqual() {
        OffsetDateTime lastRefreshDate = OffsetDateTime.now().minus(12, ChronoUnit.MONTHS); // Less than a month ago
        OffsetDateTime now = OffsetDateTime.now();



        boolean result = limitService.isMonthEqual(lastRefreshDate,now);
        assertEquals(false, result);
    }

}
