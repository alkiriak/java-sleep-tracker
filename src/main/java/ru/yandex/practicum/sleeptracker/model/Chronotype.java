package ru.yandex.practicum.sleeptracker.model;

public enum Chronotype {

    OWL("Сова"),
    LARK("Жаворонок"),
    DOVE("Голубь");

    private final String description;

    Chronotype(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
