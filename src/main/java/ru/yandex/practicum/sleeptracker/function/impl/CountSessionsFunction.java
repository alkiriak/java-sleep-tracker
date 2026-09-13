package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.function.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;

public class CountSessionsFunction implements SleepAnalysisFunction<Long> {

    private static final String DESCRIPTION = "Всего сессий сна";

    @Override
    public SleepAnalysisResult<Long> apply(List<SleepingSession> sleepingSessions) {
        long size = sleepingSessions.size();
        return new SleepAnalysisResult<>(DESCRIPTION, size);
    }
}
