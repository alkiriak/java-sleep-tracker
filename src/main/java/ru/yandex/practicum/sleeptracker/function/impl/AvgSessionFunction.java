package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.function.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.Optional;

public class AvgSessionFunction implements SleepAnalysisFunction<Double> {

    public static final String DESCRIPTION = "Средняя продолжительность сессии (мин)";

    @Override
    public SleepAnalysisResult<Double> analyze(List<SleepingSession> sleepingSessions) {
        Optional<Double> avg = sleepingSessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .average()
                .stream()
                .boxed()
                .findFirst();
        return new SleepAnalysisResult<>(DESCRIPTION, avg);
    }
}
