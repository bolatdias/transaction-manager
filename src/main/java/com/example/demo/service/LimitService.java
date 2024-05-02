package com.example.demo.service;


import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.LimitMapper;
import com.example.demo.model.Limit;
import com.example.demo.model.LimitType;
import com.example.demo.payload.LimitSetRequestDTO;
import com.example.demo.repository.LimitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class LimitService {

    private final LimitRepository limitRepository;

    public void addLimit(LimitSetRequestDTO request) {
        Limit limit = LimitMapper.INSTANCE.limitSetRequestToLimit(request);


        limitRepository.save(limit);
    }

    public Limit getLimit(LimitType type, OffsetDateTime time) {
        return limitRepository.findFirstByTimeAndType(time, type).orElseThrow(
                () -> new ResourceNotFoundException("LimitService", "type", type)
        );
    }


    public boolean isMonthEqual(OffsetDateTime time, OffsetDateTime time2) {
        return time.getMonth() == time2.getMonth() && time.getYear() == time2.getYear();
    }

}









