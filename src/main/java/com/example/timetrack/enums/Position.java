package com.example.timetrack.enums;

import lombok.Getter;

public enum Position {
    TEAM_LEAD("Тимлид"),
    DEVELOPER("Разработчик"),
    PROJECT_MANAGER("Менеджер проекта");

    @Getter
    private final String name;

    Position(String name) {
        this.name = name;
    }
}
