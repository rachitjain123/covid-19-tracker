package com.rjrks.coronavirustracking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoronavirusTrackingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoronavirusTrackingApplication.class, args);
    }

}
