package com.example.demo;

import com.example.demo.mapper.LimitMapper;
import com.example.demo.model.Limit;
import com.example.demo.payload.LimitSetRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LimitMapperTest {

    @Test
    public void testLimitSetRequestToLimit() {

        LimitSetRequest request = new LimitSetRequest();
        request.setLimit(BigDecimal.valueOf(100));


        Limit limit = LimitMapper.INSTANCE.limitSetRequestToLimit(request);


        assertEquals(request.getLimit(), limit.getLimitValue());
    }
}
