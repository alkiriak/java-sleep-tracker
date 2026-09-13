package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AvgSessionFunctionTest {

    private final AvgSessionFunction function = new AvgSessionFunction();

    @Test
    @DisplayName("Вычисление средней продолжительности сессий сна")
    void shouldReturnAverageDurationInMinutes() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("01.10.25 23:00;02.10.25 07:00;GOOD"),   // 480 мин
                SleepingSession.parse("02.10.25 14:00;02.10.25 14:30;NORMAL"), // 30 мин
                SleepingSession.parse("02.10.25 23:30;03.10.25 06:00;BAD")     // 390 мин
        );
        SleepAnalysisResult<Double> result = function.apply(sessions);

        assertEquals(300.0, result.result());
    }

    @Test
    @DisplayName("Возврат null при вычислении среднего в пустом списке")
    void shouldReturnNullOnEmptyList() {
        SleepAnalysisResult<Double> result = function.apply(List.of());
        assertNull(result.result());
    }
}
