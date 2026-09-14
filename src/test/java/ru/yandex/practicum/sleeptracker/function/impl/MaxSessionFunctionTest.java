package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaxSessionFunctionTest {

    private final MaxSessionFunction function = new MaxSessionFunction();

    @Test
    @DisplayName("Поиск максимальной продолжительности среди нескольких сессий")
    void shouldReturnMaximumDurationInMinutes() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("01.10.25 23:00;02.10.25 07:00;GOOD"),   // 480 мин
                SleepingSession.parse("02.10.25 14:00;02.10.25 14:30;NORMAL"), // 30 мин
                SleepingSession.parse("02.10.25 22:00;03.10.25 07:30;BAD")     // 570 мин
        );

        Optional<Long> result = function.analyze(sessions).result();

        assertTrue(result.isPresent());
        assertEquals(570L, result.get());
    }

    @Test
    @DisplayName("Возврат null при поиске максимума в пустом списке")
    void shouldReturnNullOnEmptyList() {
        Optional<Long> result = function.analyze(List.of()).result();
        assertTrue(result.isEmpty());
    }
}
