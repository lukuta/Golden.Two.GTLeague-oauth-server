package com.goldentwo.controller;

import com.goldentwo.components.BannerProducer;
import com.goldentwo.model.BettingTimeRestriction;
import com.goldentwo.service.GTLeageConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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

        response.put("timeFrom", bettingTimeRestriction.getTimeFrom());
        response.put("timeTo", bettingTimeRestriction.getTimeTo());

        return response;
    }

    @PostMapping("/betting-times")
    public BettingTimeRestriction createBettingTimeRestriction(@RequestBody BettingTimeRestriction bettingTimeRestriction) {
        return gtLeageConfigService.create(bettingTimeRestriction);
    }

    @PutMapping("/betting-times")
    public void changeBettingTimes(@RequestParam(value = "fromHours") int fromHours,
                                   @RequestParam(value = "fromMinutes") int fromMinutes,
                                   @RequestParam(value = "toHours") int toHours,
                                   @RequestParam(value = "toMinutes") int toMinutes) {

        LocalTime from = LocalTime.of(fromHours, fromMinutes);
        LocalTime to = LocalTime.of(toHours, toMinutes);

        gtLeageConfigService.updateBettingTimeRestrictions(from, to);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.OK)
    public boolean handleEntityNotFoundException() {
        log.debug("Found no restriction in database. So betting is permitted");
        return true;
    }
}
