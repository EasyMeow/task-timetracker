package com.example.timetrack.enums;

import lombok.Getter;

public enum TaskStatus {
    NEW("Новая"),
    IN_PROGRES("В процессе"),
    ON_REVIEW("На рассмотрении"),
    DONE("Готова");

    @Getter
    private final String name;

    TaskStatus(String name) {
        this.name = name;
    }
}
