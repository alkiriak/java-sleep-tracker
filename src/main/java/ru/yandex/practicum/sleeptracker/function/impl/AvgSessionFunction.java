package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.function.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.OptionalDouble;

public class AvgSessionFunction implements SleepAnalysisFunction<Double> {

    private static final String DESCRIPTION = "Средняя продолжительность сессии (мин)";

    @Override
    public SleepAnalysisResult<Double> apply(List<SleepingSession> sleepingSessions) {
        OptionalDouble avg = sleepingSessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .average();
        Double result = avg.isPresent() ? avg.getAsDouble() : null;
        return new SleepAnalysisResult<>(DESCRIPTION, result);
    }
}
