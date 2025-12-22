package ru.vavtech.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vavtech.hw.domain.Student;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final IOService ioService;

    @Override
    public Student determineCurrentStudent() {
        var firstName = ioService.readStringWithPrompt("enter.student.name");
        var lastName = ioService.readStringWithPrompt("enter.student.lastName");
        return new Student(firstName, lastName);
    }
}
