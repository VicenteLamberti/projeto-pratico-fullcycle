package com.fullcycle.vicente.infrastructure;

import com.fullcycle.vicente.application.UseCase;
import com.fullcycle.vicente.domain.category.Category;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryJPAEntity;
import com.fullcycle.vicente.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.vicente.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        System.out.println("Hello world!");
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME,"development");
        SpringApplication.run(WebServerConfig.class,args);

    }
}