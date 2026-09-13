package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.function.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;

public class MaxSessionFunction implements SleepAnalysisFunction<Long> {

    private static final String DESCRIPTION = "Максимальная продолжительность сессии (мин)";

    @Override
    public SleepAnalysisResult<Long> analyze(List<SleepingSession> sleepingSessions) {
        Long max = sleepingSessions.stream()
                .map(SleepingSession::getDurationMinutes)
                .max(Long::compare)
                .orElse(null);
        return new SleepAnalysisResult<>(DESCRIPTION, max);
    }
}
