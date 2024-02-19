package org.example.stockswiftservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StockSwiftServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockSwiftServiceApplication.class, args);
    }

}
