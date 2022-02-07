package com.adamsoft.moviereview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MoviereviewApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoviereviewApplication.class, args);
    }

}
