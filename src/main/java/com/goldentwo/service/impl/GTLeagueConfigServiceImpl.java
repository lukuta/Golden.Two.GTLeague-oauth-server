package com.goldentwo.service.impl;

import com.goldentwo.model.BettingTimeRestriction;
import com.goldentwo.repository.BettingTimeRestrictionRepository;
import com.goldentwo.service.GTLeageConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class GTLeagueConfigServiceImpl implements GTLeageConfigService {

    private final BettingTimeRestrictionRepository bettingTimeRestrictionRepository;

    @Autowired
    public GTLeagueConfigServiceImpl(BettingTimeRestrictionRepository bettingTimeRestrictionRepository) {
        this.bettingTimeRestrictionRepository = bettingTimeRestrictionRepository;
    }

    @Override
    public BettingTimeRestriction getBettingTimeRestriction() {
        List<BettingTimeRestriction> bettingTimeRestrictions = Optional.ofNullable(bettingTimeRestrictionRepository.findAll())
                .orElseThrow(EntityNotFoundException::new);

        if (bettingTimeRestrictions.size() > 1) {
            bettingTimeRestrictions.sort(Comparator.comparing(BettingTimeRestriction::getId).reversed());
            return bettingTimeRestrictions.get(0);
        }

        return bettingTimeRestrictions.get(0);
    }

    @Override
    public void updateBettingTimeRestrictions(LocalTime from, LocalTime to) {

    }

    @Override
    public boolean isBettingAllowed() {
        return false;
    }
}
