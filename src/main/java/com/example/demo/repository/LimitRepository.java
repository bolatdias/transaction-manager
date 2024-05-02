package com.example.demo.repository;

import com.example.demo.model.Limit;
import com.example.demo.model.LimitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;


@Repository
public interface LimitRepository extends JpaRepository<Limit, Long> {

    @Query("SELECT l FROM Limit l WHERE l.createdDate < :time AND l.type = :type ORDER BY l.createdDate DESC limit 1")
    Optional<Limit> findFirstByTimeAndType(OffsetDateTime time, LimitType type);
}
