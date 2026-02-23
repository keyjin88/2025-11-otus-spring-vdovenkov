package ru.vavtech.hw10;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class Application {
    private final Environment environment;

    public Application(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventListener(ApplicationStartedEvent.class)
    public void printStartupInfo() {
        String port = environment.getProperty("server.port", "8080");
        System.out.printf("Чтобы перейти на страницу сайта открывай: %n%s%n", "http://localhost:" + port);
    }
}
