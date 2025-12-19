package ru.vavtech.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.vavtech.hw.dao.QuestionDao;
import ru.vavtech.hw.service.IOService;
import ru.vavtech.hw.service.ResultService;
import ru.vavtech.hw.service.ResultServiceImpl;
import ru.vavtech.hw.service.StreamsIOService;
import ru.vavtech.hw.service.StudentService;
import ru.vavtech.hw.service.StudentServiceImpl;
import ru.vavtech.hw.service.TestRunnerService;
import ru.vavtech.hw.service.TestRunnerServiceImpl;
import ru.vavtech.hw.service.TestService;
import ru.vavtech.hw.service.TestServiceImpl;

@Configuration
public class ServicesConfig {

    @Bean
    public IOService ioService() {
        return new StreamsIOService(java.lang.System.out, java.lang.System.in);
    }

    @Bean
    public TestService testService(IOService ioService, QuestionDao questionDao) {
        return new TestServiceImpl(ioService, questionDao);
    }

    @Bean
    public StudentService studentService(IOService ioService) {
        return new StudentServiceImpl(ioService);
    }

    @Bean
    public ResultService resultService(AppProperties appProperties, IOService ioService) {
        return new ResultServiceImpl(appProperties, ioService);
    }

    @Bean
    public TestRunnerService personService(TestService testService,
                                           StudentService studentService,
                                           ResultService resultService) {
        return new TestRunnerServiceImpl(testService, studentService, resultService);
    }
}
