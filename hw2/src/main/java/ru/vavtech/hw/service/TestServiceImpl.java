package ru.vavtech.hw.service;

import lombok.RequiredArgsConstructor;
import ru.vavtech.hw.dao.QuestionDao;
import ru.vavtech.hw.domain.Answer;
import ru.vavtech.hw.domain.Student;
import ru.vavtech.hw.domain.TestResult;

import java.util.List;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (int i = 0; i < questions.size(); i++) {
            var question = questions.get(i);
            ioService.printFormattedLine("Question %d: %s", i, question.text());
            List<Answer> answers = question.answers();
            for (int j = 0; j < answers.size(); j++) {
                Answer answer = answers.get(j);
                ioService.printFormattedLine("%d) %s", j, answer.text());
            }
            var answerIndex = ioService.readIntForRangeWithPrompt(
                    0,
                    question.answers().size(),
                    "Enter answer number: ",
                    "Answer must be from 0 to %d".formatted(question.answers().size() - 1));
            testResult.applyAnswer(question, question.answers().get(answerIndex).isCorrect());
        }
        return testResult;
    }
}
