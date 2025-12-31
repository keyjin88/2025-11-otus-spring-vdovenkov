package ru.vavtech.hw4.domain;

import java.util.List;

public record Question(String text, List<Answer> answers) {
}
