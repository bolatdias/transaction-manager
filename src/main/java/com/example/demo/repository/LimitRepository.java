package com.example.demo.repository;

import com.example.demo.model.Limit;
import com.example.demo.model.LimitType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {

    Optional<Limit> findFirstByType(LimitType type, Sort sort);
}
