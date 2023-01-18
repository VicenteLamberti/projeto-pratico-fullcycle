package com.fullcycle.vicente.infrastructure;

import com.fullcycle.vicente.application.UseCase;
import com.fullcycle.vicente.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        SpringApplication.run(WebServerConfig.class,args);
    }
}