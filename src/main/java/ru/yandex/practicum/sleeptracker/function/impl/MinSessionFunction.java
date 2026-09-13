package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.function.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;

public class MinSessionFunction implements SleepAnalysisFunction<Long> {

    private static final String DESCRIPTION = "Минимальная продолжительность сессии (мин)";

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sleepingSessions) {
        Long min = sleepingSessions.stream()
                .map(SleepingSession::getDurationMinutes)
                .min(Long::compareTo)
                .orElse(null);
        return new SleepAnalysisResult<>(DESCRIPTION, min);
    }
}
