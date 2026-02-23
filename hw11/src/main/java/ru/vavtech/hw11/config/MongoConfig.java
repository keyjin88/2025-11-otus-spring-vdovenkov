package ru.vavtech.hw11.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.boot.mongodb.autoconfigure.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClient reactiveMongoClient(MongoProperties properties) {
        String uri = properties.determineUri();
        return MongoClients.create(uri);
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient, MongoProperties properties) {
        String database = properties.getDatabase() != null ? properties.getDatabase() : "library";
        return new ReactiveMongoTemplate(
            new SimpleReactiveMongoDatabaseFactory(mongoClient, database)
        );
    }
} 