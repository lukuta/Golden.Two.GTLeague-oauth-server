package com.goldentwo.service.impl;

import com.goldentwo.service.GTLeageConfigService;
import org.springframework.stereotype.Service;

@Service
public class GTLeagueConfigServiceImpl implements GTLeageConfigService{
    @Override
    public boolean isBettingAllowed() {
        return false;
    }
}
