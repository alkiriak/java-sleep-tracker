package ru.yandex.practicum.sleeptracker.function;

import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.util.List;
import java.util.function.Function;

/**
 * Analyzes sleep sessions and produces a typed result.
 */
public interface SleepAnalysisFunction<T> extends Function<List<SleepingSession>, SleepAnalysisResult<T>> {
}
