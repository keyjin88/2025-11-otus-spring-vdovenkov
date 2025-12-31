package ru.vavtech.hw4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vavtech.hw4.dao.QuestionDao;
import ru.vavtech.hw4.domain.Answer;
import ru.vavtech.hw4.domain.Student;
import ru.vavtech.hw4.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("empty.message");
        ioService.printFormattedLine("welcome.message");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (int i = 0; i < questions.size(); i++) {
            var question = questions.get(i);
            ioService.printFormattedLine("question", i, question.text());
            List<Answer> answers = question.answers();
            for (int j = 0; j < answers.size(); j++) {
                Answer answer = answers.get(j);
                ioService.printFormattedLine("answer", j, answer.text());
            }
            var answerIndex = ioService.readIntForRangeWithPrompt(
                    0,
                    question.answers().size(),
                    "enter.answer.number", "wrong.answer.number");
            testResult.applyAnswer(question, question.answers().get(answerIndex).isCorrect());
        }
        return testResult;
    }
}
