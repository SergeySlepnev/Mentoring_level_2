package com.spdev.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ActiveProfiles;

@TestConfiguration
@ActiveProfiles("test")
public class TestApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(TestApplicationRunner.class);
    }
}