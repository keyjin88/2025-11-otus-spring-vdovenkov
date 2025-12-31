package ru.vavtech.hw4.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.vavtech.hw4.dao.QuestionDao;
import ru.vavtech.hw4.domain.Answer;
import ru.vavtech.hw4.domain.Question;
import ru.vavtech.hw4.domain.Student;
import ru.vavtech.hw4.domain.TestResult;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class TestServiceImplTest {


    @MockitoBean
    private IOService ioService;
    @MockitoBean
    private QuestionDao questionDao;
    @Autowired
    private TestServiceImpl testService;

    @Test
    void executeTestFor() {
        Student student = new Student("John", "Doe");

        Question question1 = new Question("What is 2 + 2?", Arrays.asList(new Answer("3", false), new Answer("4", true)));
        Question question2 = new Question("What is the capital of Grate Britain?", Arrays.asList(new Answer("Berlin", false), new Answer("London", true)));

        when(questionDao.findAll()).thenReturn(Arrays.asList(question1, question2));
        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString())).thenReturn(1, 0);

        TestResult result = testService.executeTestFor(student);

        verify(ioService, times(6)).printFormattedLine(anyString(), anyInt(), anyString());
        verify(ioService, times(6)).printFormattedLine(anyString(), anyInt(), anyString());
        verify(ioService, times(2)).readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString());

        assertThat(result.getRightAnswersCount()).isEqualTo(1);
    }

}