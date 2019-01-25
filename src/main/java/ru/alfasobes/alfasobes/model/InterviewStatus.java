package ru.alfasobes.alfasobes.model;

import lombok.Getter;

public enum InterviewStatus {

    CREATED("создано"), UNFINISHED("начато"), FINISHED("завершено");

    @Getter
    private final String description;

    InterviewStatus(String s) {
        this.description = s;
    }


}
