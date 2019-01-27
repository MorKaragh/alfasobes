package ru.alfasobes.alfasobes.model;

import javax.persistence.Entity;

public enum InterviewAnswer {
    GOOD("хороший ответ"), BAD("плохой ответ"), MODERATE("нормальный ответ");

    private final String description;

    InterviewAnswer(String s) {
        this.description = s;
    }

    public String getDescription() {
        return description;
    }
}
