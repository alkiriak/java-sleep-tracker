package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.function.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.Optional;

public class MinSessionFunction implements SleepAnalysisFunction<Long> {

    public static final String DESCRIPTION = "Минимальная продолжительность сессии (мин)";

    @Override
    public SleepAnalysisResult<Long> analyze(List<SleepingSession> sleepingSessions) {
        Optional<Long> min = sleepingSessions.stream()
                .map(SleepingSession::getDurationMinutes)
                .min(Long::compareTo);
        return new SleepAnalysisResult<>(DESCRIPTION, min);
    }
}
