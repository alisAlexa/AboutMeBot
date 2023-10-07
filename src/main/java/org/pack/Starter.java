package org.pack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.pack")
public class Starter {


    public static void main(String[] args) {
        SpringApplication.run(Starter.class);
    }
}