package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CountBadSessionsFunctionTest {

    private final CountBadSessionsFunction function = new CountBadSessionsFunction();

    @Test
    @DisplayName("Подсчет количества сессий со статусом BAD")
    void shouldCountBadSessionsCorrectly() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("01.10.25 23:00;02.10.25 07:00;GOOD"),
                SleepingSession.parse("02.10.25 23:00;03.10.25 07:00;BAD"),
                SleepingSession.parse("03.10.25 14:00;03.10.25 15:00;NORMAL"),
                SleepingSession.parse("03.10.25 23:30;04.10.25 06:20;BAD")
        );

        Optional<Long> result = function.analyze(sessions).result();

        assertTrue(result.isPresent());
        assertEquals(2L, result.get());
    }

    @Test
    @DisplayName("Возврат 0, если сессий со статусом BAD нет")
    void shouldReturnZeroWhenNoBadSessionsPresent() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("01.10.25 23:00;02.10.25 07:00;GOOD"),
                SleepingSession.parse("02.10.25 14:00;02.10.25 15:00;NORMAL")
        );

        Optional<Long> result = function.analyze(sessions).result();

        assertTrue(result.isPresent());
        assertEquals(0L, result.get());
    }
}
