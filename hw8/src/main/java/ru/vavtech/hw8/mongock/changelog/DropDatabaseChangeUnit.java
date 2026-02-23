package ru.vavtech.hw8.mongock.changelog;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.MongoDatabaseFactory;

@ChangeUnit(id = "dropDB", order = "000", author = "nrr", runAlways = true)
public class DropDatabaseChangeUnit {

    private final MongoDatabaseFactory mongoDatabaseFactory;

    public DropDatabaseChangeUnit(MongoDatabaseFactory mongoDatabaseFactory) {
        this.mongoDatabaseFactory = mongoDatabaseFactory;
    }

    @Execution
    public void execute() {
        mongoDatabaseFactory.getMongoDatabase().drop();
    }

    @RollbackExecution
    public void rollback() {
        // Откат удаления БД не требуется
    }
}
