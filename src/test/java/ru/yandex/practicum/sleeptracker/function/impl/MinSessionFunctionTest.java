package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MinSessionFunctionTest {

    private final MinSessionFunction function = new MinSessionFunction();

    @Test
    @DisplayName("Поиск минимальной продолжительности среди нескольких сессий")
    void shouldReturnMinimumDurationInMinutes() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("01.10.25 23:00;02.10.25 07:00;GOOD"),   // 480 мин
                SleepingSession.parse("02.10.25 14:00;02.10.25 14:40;NORMAL"), // 40 мин
                SleepingSession.parse("02.10.25 23:30;03.10.25 06:00;BAD")     // 390 мин
        );

        SleepAnalysisResult<Long> result = function.analyze(sessions);

        assertEquals(40L, result.result());
    }

    @Test
    @DisplayName("Возврат null при поиске минимума в пустом списке")
    void shouldReturnNullOnEmptyList() {
        SleepAnalysisResult<Long> result = function.analyze(List.of());
        assertNull(result.result());
    }
}
