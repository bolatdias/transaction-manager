package com.example.demo.service;


import com.example.demo.mapper.LimitMapper;
import com.example.demo.model.Limit;
import com.example.demo.model.LimitType;
import com.example.demo.payload.LimitSetRequest;
import com.example.demo.repository.LimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class LimitService {

    private final LimitRepository limitRepository;

    public void addLimit(LimitSetRequest request) {
        Limit limit = LimitMapper.INSTANCE.limitSetRequestToLimit(request);


        limitRepository.save(limit);
    }

    public Limit getLimit(LimitType type, OffsetDateTime time) {
        return limitRepository.findFirstByTimeAndType(time, type).orElse(null);
    }

    public void saveLimit(Limit limit) {
        limitRepository.save(limit);
    }


    public boolean validateByMonth(Limit limit, OffsetDateTime lastRefreshedTime, OffsetDateTime now) {
        long monthsBetween = lastRefreshedTime.until(now, ChronoUnit.MONTHS);

        return monthsBetween >= 1;

    }

//    public void refreshLimit(Limit limit) {
//        Limit lastLimit = getLastLimit(limit.getType());
//        lastLimit.setLimitValue(limit.getLimitValue());
//        limitRepository.save(lastLimit);
//    }
//
//    public boolean validateByMonth(Limit limit) {
//        Limit lastLimit = getLastLimit(limit.getType());
//    }
}









