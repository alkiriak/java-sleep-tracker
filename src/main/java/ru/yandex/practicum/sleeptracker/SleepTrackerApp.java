package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.function.impl.AvgSessionFunction;
import ru.yandex.practicum.sleeptracker.function.impl.CountBadSessionsFunction;
import ru.yandex.practicum.sleeptracker.function.impl.CountSessionsFunction;
import ru.yandex.practicum.sleeptracker.function.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.function.impl.CountSleeplessNightsFunction;
import ru.yandex.practicum.sleeptracker.function.impl.DetectChronotypeFunction;
import ru.yandex.practicum.sleeptracker.function.impl.MaxSessionFunction;
import ru.yandex.practicum.sleeptracker.function.impl.MinSessionFunction;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class SleepTrackerApp {

    private static final List<SleepAnalysisFunction<?>> ANALYSIS_FUNCTIONS = List.of(
            new CountSessionsFunction(),
            new MinSessionFunction(),
            new MaxSessionFunction(),
            new AvgSessionFunction(),
            new CountBadSessionsFunction(),
            new CountSleeplessNightsFunction(),
            new DetectChronotypeFunction()
    );

    public static void main(String[] args) {
        if (args == null || args.length != 1) {
            System.err.println("Required one program argument - path to sleep log file");
            return;
        }

        List<SleepingSession> sessions;
        try (Stream<String> lines = Files.lines(Path.of(args[0]))) {
            sessions = lines
                    .filter(line -> !line.isBlank())
                    .map(SleepingSession::parse)
                    .toList();
        } catch (IOException e) {
            System.err.println("Exception occurred while reading log file: " + e.getMessage());
            return;
        }

        ANALYSIS_FUNCTIONS.stream()
                .map(f -> f.analyze(sessions))
                .map(SleepAnalysisResult::toDisplayString)
                .forEach(System.out::println);
    }
}
