package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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

        SleepAnalysisResult<Long> result = function.apply(sessions);

        assertEquals(570L, result.result());
    }

    @Test
    @DisplayName("Возврат null при поиске максимума в пустом списке")
    void shouldReturnNullOnEmptyList() {
        SleepAnalysisResult<Long> result = function.apply(List.of());
        assertNull(result.result());
    }
}
