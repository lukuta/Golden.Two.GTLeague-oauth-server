package com.goldentwo.controller;

import com.goldentwo.components.BannerProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    private final BannerProducer bannerService;

    @Autowired
    public UserController(BannerProducer bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping("/me")
    public Principal getUserPrincipal(Principal principal) {
        return principal;
    }

    @GetMapping(value = "/banner", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getBanner() {
        return bannerService.getGTLeagueBanner();
    }

}
