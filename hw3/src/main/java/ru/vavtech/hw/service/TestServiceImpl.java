package ru.vavtech.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vavtech.hw.dao.QuestionDao;
import ru.vavtech.hw.domain.Answer;
import ru.vavtech.hw.domain.Student;
import ru.vavtech.hw.domain.TestResult;

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
            ioService.printFormattedLine("question", i + 1, question.text());
            List<Answer> answers = question.answers();
            for (int j = 0; j < answers.size(); j++) {
                Answer answer = answers.get(j);
                ioService.printFormattedLine("answer", j + 1, answer.text());
            }
            var answerIndex = ioService.readIntForRangeWithPrompt(
                    1,
                    question.answers().size(),
                    "enter.answer.number", "wrong.answer.number");
            testResult.applyAnswer(question, question.answers().get(answerIndex - 1).isCorrect());
        }
        return testResult;
    }
}
