package ru.wv3rine.abspringwebapp.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("ru.wv3rine.abspringwebapp")
@EnableJpaRepositories(basePackages = "ru.wv3rine.abspringwebapp.dao")
@EntityScan("ru.wv3rine.abspringwebapp.models")
public class AbSpringWebAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbSpringWebAppApplication.class, args);
    }

}
