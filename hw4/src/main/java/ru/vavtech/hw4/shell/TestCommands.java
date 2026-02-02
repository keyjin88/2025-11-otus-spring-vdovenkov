package ru.vavtech.hw4.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.stereotype.Component;
import ru.vavtech.hw4.service.TestRunnerService;

@Component
@RequiredArgsConstructor
public class TestCommands {

    private final TestRunnerService testRunnerService;

    @Command(name = "start", alias = "s", description = "Запустить тестирование", group = "Тестирование")
    public void startTesting() {
        testRunnerService.run();
    }
}
