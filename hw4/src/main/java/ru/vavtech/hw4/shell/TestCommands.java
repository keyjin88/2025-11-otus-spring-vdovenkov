package ru.vavtech.hw4.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.vavtech.hw4.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class TestCommands {

    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Start testing command", key = {"start", "s"})
    public void startTesting() {
        testRunnerService.run();
    }
}
