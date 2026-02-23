package ru.vavtech.hw8.mongock.changelog;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

@ChangeUnit(id = "insertAuthors", order = "002", author = "nrr")
public class InsertAuthorsChangeUnit {

    private static final String COLLECTION = "authors";

    private final MongoTemplate mongoTemplate;

    public InsertAuthorsChangeUnit(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Execution
    public void execute() {
        Document author1 = new Document().append("_id", "1").append("fullName", "Author_1");
        Document author2 = new Document().append("_id", "2").append("fullName", "Author_2");
        Document author3 = new Document().append("_id", "3").append("fullName", "Author_3");
        mongoTemplate.insert(List.of(author1, author2, author3), COLLECTION);
    }

    @RollbackExecution
    public void rollback() {
        mongoTemplate.remove(new org.springframework.data.mongodb.core.query.Query(), COLLECTION);
    }
}
