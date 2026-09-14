package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CountSleeplessNightsFunctionTest {

    private final CountSleeplessNightsFunction function = new CountSleeplessNightsFunction();

    @Test
    @DisplayName("Пустой список сессий дает 0 бессонных ночей")
    void shouldReturnZeroOnEmptyList() {
        Optional<Long> result = function.analyze(List.of()).result();
        assertTrue(result.isPresent());
        assertEquals(0L, result.get());
    }

    @Test
    @DisplayName("Сон до полудня первого дня: первая ночь считается предыдущей")
    void shouldCountPreviousNightWhenFirstSessionBeforeNoon() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("01.10.25 07:00;01.10.25 11:00;GOOD"),
                SleepingSession.parse("01.10.25 23:00;02.10.25 07:00;GOOD")
        );
        Optional<Long> result = function.analyze(sessions).result();
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    @DisplayName("Сессии только днем приводят ко всем бессонным ночам")
    void shouldCountAllNightsAsSleeplessIfOnlyDaySessions() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("01.10.25 14:00;01.10.25 16:00;GOOD"),
                SleepingSession.parse("02.10.25 17:00;02.10.25 23:00;GOOD"),
                SleepingSession.parse("03.10.25 07:00;03.10.25 15:00;BAD")
        );
        Optional<Long> result = function.analyze(sessions).result();
        assertTrue(result.isPresent());
        assertEquals(2L, result.get());
    }

    @Test
    @DisplayName("Сон только в промежутке 02:00 - 05:00 спасает от статуса бессонной ночи")
    void shouldNotBeSleeplessIfSleptPartiallyInWindow() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("02.10.25 02:00;02.10.25 05:00;NORMAL")
        );
        Optional<Long> result = function.analyze(sessions).result();
        assertTrue(result.isPresent());
        assertEquals(0L, result.get());
    }

    @Test
    @DisplayName("Сон, заканчивающийся в 00:00 или начинающийся в 06:00, не спасает бессонную ночь")
    void shouldNotCountSleepOnExactNightBoundaries() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("01.10.25 23:00;02.10.25 00:00;GOOD"),
                SleepingSession.parse("02.10.25 06:00;02.10.25 10:00;GOOD")
        );
        Optional<Long> result = function.analyze(sessions).result();
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    @DisplayName("Корректный расчет через границу месяцев")
    void shouldHandleMonthTransitionCorrectly() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("30.10.25 23:00;31.10.25 07:00;GOOD"),
                SleepingSession.parse("01.11.25 23:00;02.11.25 07:00;GOOD")
        );
        Optional<Long> result = function.analyze(sessions).result();
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }

    @Test
    @DisplayName("Корректный расчет бессонных ночей при переходе через границу года")
    void shouldHandleYearTransitionCorrectly() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("30.12.25 23:00;31.12.25 07:00;GOOD"),
                SleepingSession.parse("01.01.26 23:30;02.01.26 07:00;GOOD")
        );
        Optional<Long> result = function.analyze(sessions).result();
        assertTrue(result.isPresent());
        assertEquals(1L, result.get());
    }
}
