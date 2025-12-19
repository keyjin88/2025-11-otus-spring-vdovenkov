package ru.vavtech.hw.dao;

import ru.vavtech.hw.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
