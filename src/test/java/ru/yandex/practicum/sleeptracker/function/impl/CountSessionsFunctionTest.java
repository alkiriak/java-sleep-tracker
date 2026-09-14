package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CountSessionsFunctionTest {

    private final CountSessionsFunction function = new CountSessionsFunction();

    @Test
    @DisplayName("Корректный подсчет количества сессий в списке")
    void shouldReturnCorrectSessionCount() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("01.10.25 23:00;02.10.25 07:00;GOOD"),
                SleepingSession.parse("02.10.25 14:00;02.10.25 14:45;NORMAL"),
                SleepingSession.parse("02.10.25 23:30;03.10.25 07:30;BAD")
        );

        Optional<Long> result = function.analyze(sessions).result();

        assertTrue(result.isPresent());
        assertEquals(3L, result.get());
    }

    @Test
    @DisplayName("Возврат 0 для пустого списка сессий")
    void shouldReturnZeroOnEmptyList() {
        Optional<Long> result = function.analyze(List.of()).result();
        assertTrue(result.isPresent());
        assertEquals(0L, result.get());
    }
}
