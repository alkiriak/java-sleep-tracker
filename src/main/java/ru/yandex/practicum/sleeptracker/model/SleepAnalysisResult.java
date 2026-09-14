package ru.yandex.practicum.sleeptracker.model;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public record SleepAnalysisResult<T>(String description, Optional<T> result) {

    public SleepAnalysisResult {
        Objects.requireNonNull(description, "description must not be null");
        Objects.requireNonNull(result, "result optional must not be null");
    }

    public SleepAnalysisResult(String description, T value) {
        this(description, Optional.ofNullable(value));
    }

    /**
     * Formats the description and result for user display.
     */
    public String toDisplayString() {
        String formattedResult = result
                .map(this::formatValue)
                .orElse("N/A");

        return description + ": " + formattedResult;
    }

    private String formatValue(T val) {
        return switch (val) {
            case Double d -> String.format(Locale.ROOT, "%.1f", d);
            default -> String.valueOf(val);
        };
    }
}
