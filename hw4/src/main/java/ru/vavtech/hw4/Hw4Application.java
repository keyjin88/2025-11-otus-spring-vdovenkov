package ru.vavtech.hw4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.vavtech.hw4.config.AppProperties;


@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class Hw4Application {

    public static void main(String[] args) {
        SpringApplication.run(Hw4Application.class, args);
    }

}
