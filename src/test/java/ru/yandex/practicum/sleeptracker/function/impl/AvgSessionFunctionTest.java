package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

        Optional<Double> result = function.analyze(sessions).result();

        assertTrue(result.isPresent());
        assertEquals(300.0, result.get());
    }

    @Test
    @DisplayName("Возврат null при вычислении среднего в пустом списке")
    void shouldReturnNullOnEmptyList() {
        Optional<Double> result = function.analyze(List.of()).result();
        assertTrue(result.isEmpty());
    }
}
