package com.itsm.itsmsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ItsmSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItsmSystemApplication.class, args);
    }

}
