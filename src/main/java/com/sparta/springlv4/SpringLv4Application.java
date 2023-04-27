package com.sparta.springlv4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringLv4Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringLv4Application.class, args);
    }

}
