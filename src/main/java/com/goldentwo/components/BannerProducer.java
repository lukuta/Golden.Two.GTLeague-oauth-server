package com.goldentwo.components;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
public class BannerProducer {

    public byte[] getGTLeagueBanner() {
        InputStream in = BannerProducer.class.getClassLoader().getResourceAsStream("static/banner.png");
        byte[] byteArray = null;
        try {
            byteArray = IOUtils.toByteArray(in);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return byteArray;
    }
}
