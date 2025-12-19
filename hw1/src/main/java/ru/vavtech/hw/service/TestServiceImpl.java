package ru.vavtech.hw.service;

import lombok.RequiredArgsConstructor;
import ru.vavtech.hw.dao.QuestionDao;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        questions.forEach(question -> {
            ioService.printLine("Question: " + question.text());
            question.answers().forEach(answer -> ioService.printLine("Answer: " + answer.text()));
        });
    }
}
