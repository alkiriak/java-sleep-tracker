package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.function.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;

public class CountSleeplessNightsFunction implements SleepAnalysisFunction<Long> {

    public static final String DESCRIPTION = "Количество бессонных ночей";

    @Override
    public SleepAnalysisResult<Long> analyze(List<SleepingSession> sessions) {
        if (sessions.isEmpty()) {
            return new SleepAnalysisResult<>(DESCRIPTION, 0L);
        }

        LocalDateTime firstSessionStart = sessions.getFirst().start();
        LocalDateTime lastSessionEnd = sessions.getLast().end();

        LocalDate firstNightMorning = firstSessionStart.toLocalTime().isBefore(LocalTime.NOON)
                ? firstSessionStart.toLocalDate()
                : firstSessionStart.toLocalDate().plusDays(1);
        LocalDate lastNightMorning = lastSessionEnd.toLocalDate();

        if (firstNightMorning.isAfter(lastNightMorning)) {
            return new SleepAnalysisResult<>(DESCRIPTION, 0L);
        }

        long totalNights = ChronoUnit.DAYS.between(firstNightMorning, lastNightMorning) + 1;
        long totalNightsWithSleep = sessions.stream()
                .flatMap(this::getIntersectedNights)
                .filter(date -> !date.isBefore(firstNightMorning) && !date.isAfter(lastNightMorning))
                .distinct()
                .count();

        long sleeplessCount = totalNights - totalNightsWithSleep;

        return new SleepAnalysisResult<>(DESCRIPTION, sleeplessCount);
    }

    /**
     * Returns night morning dates intersected by this sleep session.
     */
    private Stream<LocalDate> getIntersectedNights(SleepingSession session) {
        LocalDate startCandidate = session.start().toLocalDate();
        LocalDate endCandidate = session.end().toLocalDate();

        long daysSpan = ChronoUnit.DAYS.between(startCandidate, endCandidate) + 1;

        return Stream.iterate(startCandidate, date -> date.plusDays(1))
                .limit(daysSpan)
                .filter(session::intersectsNight);
    }
}
