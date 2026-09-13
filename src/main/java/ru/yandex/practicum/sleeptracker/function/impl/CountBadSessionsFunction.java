package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.function.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;

public class CountBadSessionsFunction implements SleepAnalysisFunction<Long> {

    private static final String DESCRIPTION = "Количество сессий с плохим качеством сна";

    @Override
    public SleepAnalysisResult<Long> analyze(List<SleepingSession> sleepingSessions) {
        long countBad = sleepingSessions.stream()
                .filter(SleepingSession::isBad)
                .count();
        return new SleepAnalysisResult<>(DESCRIPTION, countBad);
    }
}
