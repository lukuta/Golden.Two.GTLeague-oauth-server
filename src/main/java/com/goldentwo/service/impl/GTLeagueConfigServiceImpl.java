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

@Service
public class GTLeagueConfigServiceImpl implements GTLeageConfigService {

    private final BettingTimeRestrictionRepository bettingTimeRestrictionRepository;

    @Autowired
    public GTLeagueConfigServiceImpl(BettingTimeRestrictionRepository bettingTimeRestrictionRepository) {
        this.bettingTimeRestrictionRepository = bettingTimeRestrictionRepository;
    }

    @Override
    public BettingTimeRestriction getBettingTimeRestriction() {
        List<BettingTimeRestriction> bettingTimeRestrictions = bettingTimeRestrictionRepository.findAll();

        if (bettingTimeRestrictions.isEmpty()) {
            throw new EntityNotFoundException();
        }

        if (bettingTimeRestrictions.size() > 1) {
            bettingTimeRestrictions.sort(Comparator.comparing(BettingTimeRestriction::getId).reversed());
            return bettingTimeRestrictions.get(0);
        }

        return bettingTimeRestrictions.get(0);
    }

    @Override
    public void updateBettingTimeRestrictions(LocalTime from, LocalTime to) {
        BettingTimeRestriction bettingTimeRestriction = getBettingTimeRestriction();
        bettingTimeRestriction.setTimeFrom(from);
        bettingTimeRestriction.setTimeTo(to);

        bettingTimeRestrictionRepository.save(bettingTimeRestriction);

    }

    @Override
    public boolean isBettingAllowed() {
        BettingTimeRestriction bettingTimeRestriction = getBettingTimeRestriction();
        LocalTime nowTime = LocalTime.now();

        return nowTime.isAfter(bettingTimeRestriction.getTimeFrom())
                && nowTime.isBefore(bettingTimeRestriction.getTimeTo());

    }

    @Override
    public BettingTimeRestriction create(BettingTimeRestriction bettingTimeRestriction) {
        bettingTimeRestrictionRepository.deleteAll();
        return bettingTimeRestrictionRepository.saveAndFlush(bettingTimeRestriction);
    }
}
