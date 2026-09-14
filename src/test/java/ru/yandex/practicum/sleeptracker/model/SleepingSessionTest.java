package ru.yandex.practicum.sleeptracker.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SleepingSessionTest {

    @Test
    @DisplayName("Корректный парсинг валидной строки лога")
    void shouldParseValidLogLine() {
        String line = "01.10.25 23:15;02.10.25 07:30;GOOD";
        SleepingSession session = SleepingSession.parse(line);

        assertEquals(LocalDateTime.of(2025, 10, 1, 23, 15), session.start());
        assertEquals(LocalDateTime.of(2025, 10, 2, 7, 30), session.end());
        assertEquals(SleepingVerdict.GOOD, session.verdict());
    }

    @Test
    @DisplayName("Выброс IllegalArgumentException при неверном количестве полей после сплита")
    void shouldThrowExceptionWhenFieldsCountIsNotThree() {
        String emptyLine = "";
        String missingFieldsLine = "01.10.25 23:15;02.10.25 07:30";
        String extraFieldsLine = "01.10.25 23:15;02.10.25 07:30;GOOD;EXTRA";

        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> SleepingSession.parse(emptyLine)),
                () -> assertThrows(IllegalArgumentException.class, () -> SleepingSession.parse(missingFieldsLine)),
                () -> assertThrows(IllegalArgumentException.class, () -> SleepingSession.parse(extraFieldsLine))
        );
    }

    @Test
    @DisplayName("Выброс исключения при некорректном формате даты")
    void shouldThrowExceptionOnInvalidDateFormat() {
        String invalidDateLine = "2025-10-01 23:15;02.10.25 07:30;GOOD";
        assertThrows(DateTimeParseException.class, () -> SleepingSession.parse(invalidDateLine));
    }

    @Test
    @DisplayName("Выброс исключения при неизвестном вердикте")
    void shouldThrowExceptionOnInvalidVerdict() {
        String invalidVerdictLine = "01.10.25 23:15;02.10.25 07:30;EXCELLENT";
        assertThrows(IllegalArgumentException.class, () -> SleepingSession.parse(invalidVerdictLine));
    }

    @Test
    @DisplayName("getDurationMinutes: вычисление длительности дневного сна внутри одного дня")
    void getDurationMinutes_shouldCalculateMinutesWithinSameDay() {
        SleepingSession session = SleepingSession.parse("03.10.25 14:10;03.10.25 15:00;NORMAL");
        assertEquals(50L, session.getDurationMinutes());
    }

    @Test
    @DisplayName("getDurationMinutes: вычисление длительности ночного сна через полночь")
    void getDurationMinutes_shouldCalculateMinutesAcrossMidnight() {
        SleepingSession session = SleepingSession.parse("01.10.25 23:15;02.10.25 07:30;GOOD");
        assertEquals(495L, session.getDurationMinutes());
    }

    @Test
    @DisplayName("isBad: должен возвращать true, если вердикт BAD")
    void isBad_shouldReturnTrueWhenVerdictIsBad() {
        SleepingSession session = SleepingSession.parse("03.10.25 23:30;04.10.25 06:20;BAD");
        assertTrue(session.isBad());
    }

    @Test
    @DisplayName("isBad: должен возвращать false для статусов GOOD и NORMAL")
    void isBad_shouldReturnFalseWhenVerdictIsNotBad() {
        SleepingSession goodSession = SleepingSession.parse("01.10.25 23:15;02.10.25 07:30;GOOD");
        SleepingSession normalSession = SleepingSession.parse("02.10.25 23:50;03.10.25 06:40;NORMAL");

        assertFalse(goodSession.isBad());
        assertFalse(normalSession.isBad());
    }

    @Test
    @DisplayName("intersectsNight: пересечение ночи при сне через полночь (23:00 - 07:00)")
    void intersectsNight_shouldReturnTrueForOvernightSession() {
        SleepingSession session = SleepingSession.parse("01.10.25 23:00;02.10.25 07:00;GOOD");
        LocalDate morningDate = LocalDate.of(2025, 10, 2);

        assertTrue(session.intersectsNight(morningDate));
    }

    @Test
    @DisplayName("intersectsNight: пересечение ночи при сне только внутри окна (02:00 - 05:00)")
    void intersectsNight_shouldReturnTrueWhenFullyInsideNightWindow() {
        SleepingSession session = SleepingSession.parse("02.10.25 02:00;02.10.25 05:00;GOOD");
        LocalDate morningDate = LocalDate.of(2025, 10, 2);

        assertTrue(session.intersectsNight(morningDate));
    }

    @Test
    @DisplayName("intersectsNight: дневной сон не пересекает окно 00:00 - 06:00")
    void intersectsNight_shouldReturnFalseForDaySession() {
        SleepingSession session = SleepingSession.parse("02.10.25 14:00;02.10.25 15:30;GOOD");
        LocalDate morningDate = LocalDate.of(2025, 10, 2);

        assertFalse(session.intersectsNight(morningDate));
    }

    @Test
    @DisplayName("intersectsNight: сон заканчивается ровно в 00:00 или начинается ровно в 06:00 не пересекает ночь")
    void intersectsNight_shouldReturnFalseOnExactBoundaries() {
        SleepingSession beforeMidnight = SleepingSession.parse("01.10.25 22:00;02.10.25 00:00;GOOD");
        SleepingSession afterMorning = SleepingSession.parse("02.10.25 06:00;02.10.25 10:00;GOOD");
        LocalDate morningDate = LocalDate.of(2025, 10, 2);

        assertFalse(beforeMidnight.intersectsNight(morningDate));
        assertFalse(afterMorning.intersectsNight(morningDate));
    }

    @Test
    @DisplayName("isNightSession: возвращает true для обычной ночной сессии")
    void isNightSession_shouldReturnTrueForNightSession() {
        SleepingSession session = SleepingSession.parse("01.10.25 23:30;02.10.25 07:30;GOOD");
        assertTrue(session.isNightSession());
    }

    @Test
    @DisplayName("isNightSession: возвращает false для дневной сессии сна")
    void isNightSession_shouldReturnFalseForDaytimeSession() {
        SleepingSession session = SleepingSession.parse("02.10.25 13:00;02.10.25 14:30;NORMAL");
        assertFalse(session.isNightSession());
    }
}
