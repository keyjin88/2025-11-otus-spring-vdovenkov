package ru.vavtech.hw4.service;


import ru.vavtech.hw4.domain.Student;
import ru.vavtech.hw4.domain.TestResult;

public interface TestService {

    TestResult executeTestFor(Student student);
}
