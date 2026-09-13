package ru.yandex.practicum.sleeptracker.model;

import java.util.Locale;

public record SleepAnalysisResult<T>(String description, T result) {

    /**
     * Formats the description and result for user display.
     */
    public String toDisplayString() {
        String formattedResult = switch (result) {
            case Double d -> String.format(Locale.ROOT, "%.1f", d);
            case null -> "N/A";
            default -> String.valueOf(result);
        };

        return description + ": " + formattedResult;
    }
}
