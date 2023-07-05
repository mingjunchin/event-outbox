package com.example.eventoutbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class EventOutboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventOutboxApplication.class, args);
    }

}
