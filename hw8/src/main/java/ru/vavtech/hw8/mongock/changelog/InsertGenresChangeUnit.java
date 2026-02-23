package ru.vavtech.hw8.mongock.changelog;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@ChangeUnit(id = "insertGenres", order = "001", author = "nrr")
public class InsertGenresChangeUnit {

    private static final String COLLECTION = "genres";

    private final MongoTemplate mongoTemplate;

    public InsertGenresChangeUnit(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Execution
    public void execute() {
        Document genre1 = new Document().append("_id", "1").append("name", "Genre_1");
        Document genre2 = new Document().append("_id", "2").append("name", "Genre_2");
        Document genre3 = new Document().append("_id", "3").append("name", "Genre_3");
        mongoTemplate.insert(List.of(genre1, genre2, genre3), COLLECTION);
    }

    @RollbackExecution
    public void rollback() {
        mongoTemplate.remove(new org.springframework.data.mongodb.core.query.Query(), COLLECTION);
    }
}
