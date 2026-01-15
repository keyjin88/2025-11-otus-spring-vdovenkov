package ru.vavtech.hw4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.vavtech.hw4.config.AppProperties;
import ru.vavtech.hw4.domain.Student;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final MessageSource messageSource;

    private final AppProperties props;

    private final IOService ioService;

    @Override
    public Student determineCurrentStudent() {
        var firstName = ioService.readStringWithPrompt("enter.student.name");
        var lastName = ioService.readStringWithPrompt("enter.student.lastName");
        return new Student(firstName, lastName);
    }
}
