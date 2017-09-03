package com.goldentwo.service;

import com.goldentwo.model.BettingTimeRestriction;

import java.time.LocalTime;

public interface GTLeageConfigService {
    boolean isBettingAllowed();

    BettingTimeRestriction getBettingTimeRestriction();

    void updateBettingTimeRestrictions(LocalTime from, LocalTime to);
}
