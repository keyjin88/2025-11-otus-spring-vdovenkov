package ru.vavtech.hw.service;

import ru.vavtech.hw.domain.Student;
import ru.vavtech.hw.domain.TestResult;

public interface TestService {

    TestResult executeTestFor(Student student);
}
