package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;

/**
 * Analyzes sleep sessions and produces a typed result.
 */
@FunctionalInterface
public interface SleepAnalysisFunction<T> {
    SleepAnalysisResult<T> analyze(List<SleepingSession> sessions);
}
