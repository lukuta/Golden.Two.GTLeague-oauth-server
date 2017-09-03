package com.goldentwo.controller;

import com.goldentwo.components.BannerProducer;
import com.goldentwo.model.BettingTimeRestriction;
import com.goldentwo.service.GTLeageConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final BannerProducer bannerService;
    private final GTLeageConfigService gtLeageConfigService;

    @Autowired
    public UserController(BannerProducer bannerService, GTLeageConfigService gtLeageConfigService) {
        this.bannerService = bannerService;
        this.gtLeageConfigService = gtLeageConfigService;
    }

    @GetMapping("/me")
    public Principal getUserPrincipal(Principal principal) {
        return principal;
    }

    @GetMapping(value = "/banner", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getBanner() {
        return bannerService.getGTLeagueBanner();
    }

    @GetMapping("/is-betting-allowed")
    public boolean isBettingAllowed() {
        return gtLeageConfigService.isBettingAllowed();
    }

    @GetMapping("/betting-times")
    public Map<String, LocalTime> getBettingTimes() {
        BettingTimeRestriction bettingTimeRestriction = gtLeageConfigService.getBettingTimeRestriction();
        Map<String, LocalTime> response = new HashMap<>();

        response.put("Time from:", bettingTimeRestriction.getTimeFrom());
        response.put("Time to:", bettingTimeRestriction.getTimeTo());

        return response;
    }

    @PutMapping("/betting-times")
    public void changeBettingTimes(@RequestParam(value = "from" ,required = false) LocalTime from,
                                   @RequestParam(value = "to", required = false) LocalTime to) {
        gtLeageConfigService.updateBettingTimeRestrictions(from, to);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public boolean handleEntityNotFoundException() {
        return true;
    }
}
