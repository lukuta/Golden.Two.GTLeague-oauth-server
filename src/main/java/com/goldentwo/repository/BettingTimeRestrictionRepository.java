package com.goldentwo.repository;

import com.goldentwo.model.BettingTimeRestriction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BettingTimeRestrictionRepository extends JpaRepository<BettingTimeRestriction, Long> {
}
