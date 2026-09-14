package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DetectChronotypeFunctionTest {

    private final DetectChronotypeFunction function = new DetectChronotypeFunction();

    @Test
    @DisplayName("Определение типа 'Сова'")
    void shouldDetectOwl() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("01.10.25 23:30;02.10.25 09:30;GOOD"),
                SleepingSession.parse("03.10.25 01:15;03.10.25 10:00;NORMAL")
        );
        Optional<Chronotype> result = function.analyze(sessions).result();
        assertTrue(result.isPresent());
        assertEquals(Chronotype.OWL, result.get());
    }

    @Test
    @DisplayName("Определение типа 'Жаворонок'")
    void shouldDetectLark() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("01.10.25 21:00;02.10.25 06:00;GOOD"),
                SleepingSession.parse("02.10.25 21:30;03.10.25 06:30;GOOD")
        );
        Optional<Chronotype> result = function.analyze(sessions).result();
        assertTrue(result.isPresent());
        assertEquals(Chronotype.LARK, result.get());
    }

    @Test
    @DisplayName("Определение типа 'Голубь' при преобладании промежуточных сессий")
    void shouldDetectDoveByDefault() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("01.10.25 22:30;02.10.25 07:30;GOOD"),
                SleepingSession.parse("02.10.25 23:15;03.10.25 08:00;NORMAL")
        );
        Optional<Chronotype> result = function.analyze(sessions).result();
        assertTrue(result.isPresent());
        assertEquals(Chronotype.DOVE, result.get());
    }

    @Test
    @DisplayName("Выбор преобладающего типа при наличии смешанных сессий")
    void shouldSelectPredominantChronotype() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("01.10.25 23:30;02.10.25 09:30;GOOD"),   // Сова
                SleepingSession.parse("03.10.25 01:30;03.10.25 10:00;NORMAL"), // Сова
                SleepingSession.parse("03.10.25 21:00;04.10.25 06:30;GOOD"),   // Жаворонок
                SleepingSession.parse("04.10.25 22:30;05.10.25 07:30;GOOD")    // Голубь
        );
        Optional<Chronotype> result = function.analyze(sessions).result();
        assertTrue(result.isPresent());
        assertEquals(Chronotype.OWL, result.get());
    }

    @Test
    @DisplayName("При равенстве голосов выбирается 'Голубь'")
    void shouldDefaultToDoveOnTie() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("01.10.25 23:30;02.10.25 09:30;GOOD"), // Сова
                SleepingSession.parse("02.10.25 21:00;03.10.25 06:30;GOOD")  // Жаворонок
        );
        Optional<Chronotype> result = function.analyze(sessions).result();
        assertTrue(result.isPresent());
        assertEquals(Chronotype.DOVE, result.get());
    }

    @Test
    @DisplayName("Игнорирование дневных сессий при определении хронотипа")
    void shouldIgnoreDaySessions() {
        List<SleepingSession> sessions = List.of(
                SleepingSession.parse("01.10.25 14:00;01.10.25 15:00;NORMAL"),
                SleepingSession.parse("02.10.25 13:00;02.10.25 14:00;NORMAL"),
                SleepingSession.parse("02.10.25 21:30;03.10.25 06:00;GOOD") // 1 жаворонок
        );
        Optional<Chronotype> result = function.analyze(sessions).result();
        assertTrue(result.isPresent());
        assertEquals(Chronotype.LARK, result.get());
    }

    @Test
    @DisplayName("Определение хронотипа для пустого списка сессий возвращает 'Голубь'")
    void shouldReturnDoveForEmptySessionsList() {
        Optional<Chronotype> result = function.analyze(List.of()).result();
        assertTrue(result.isPresent());
        assertEquals(Chronotype.DOVE, result.get());
    }
}
