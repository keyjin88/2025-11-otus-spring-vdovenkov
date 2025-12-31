package ru.vavtech.hw4.dao;


import ru.vavtech.hw4.domain.Question;

import java.util.List;

public interface QuestionDao {
    List<Question> findAll();
}
