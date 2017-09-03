package com.goldentwo;

import com.goldentwo.model.BettingTimeRestriction;
import com.goldentwo.repository.BettingTimeRestrictionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalTime;

@SpringBootApplication
public class GtleagueOauthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GtleagueOauthServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner initTimeRestriction(BettingTimeRestrictionRepository bettingTimeRestrictionRepository) {
        return args -> {
            BettingTimeRestriction bettingTimeRestriction = BettingTimeRestriction.builder()
                    .id(1L)
                    .timeFrom(LocalTime.of(6, 0))
                    .timeTo(LocalTime.of(23, 0))
                    .build();

            bettingTimeRestrictionRepository.saveAndFlush(bettingTimeRestriction);
        };
    }
}
