package com.example.demo.service;


import com.example.demo.model.Limit;
import com.example.demo.model.LimitType;
import com.example.demo.payload.LimitSetRequest;
import com.example.demo.repository.LimitRepository;
import com.example.demo.utils.AppConst;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LimitService {

    private final LimitRepository limitRepository;

    public void addLimit(LimitSetRequest request) {
        Limit limit = new Limit();
        limit.setLimitValue(request.getLimit());

        Limit lastLimit = getLastLimit(request.getType());


        BigDecimal difference = request.getLimit().subtract(lastLimit.getLimitValue());
        limit.setRemainValue(lastLimit.getRemainValue().add(difference));
        limit.setType(request.getType());

        limitRepository.save(limit);
    }

    public Limit getLastLimit(LimitType type) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createDate");
        Limit limit = limitRepository.findFirstByType(type, sort).orElse(null);


        if (limit == null) {
            limit = new Limit();
            limit.setLimitValue(AppConst.LIMIT_VALUE);
            limit.setRemainValue(AppConst.LIMIT_VALUE);
            limit.setType(type);
        }


        return limit;
    }

    public void saveLimit(Limit limit) {
        limitRepository.save(limit);
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









