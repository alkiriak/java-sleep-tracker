package ru.yandex.practicum.sleeptracker.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public record SleepingSession(LocalDateTime start, LocalDateTime end, SleepingVerdict verdict) {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm", Locale.ENGLISH);
    public static final LocalTime NIGHT_START_TIME = LocalTime.MIDNIGHT;
    public static final LocalTime NIGHT_END_TIME = LocalTime.of(6, 0);

    public static SleepingSession parse(String rawLine) {
        String[] parts = rawLine.split(";");
        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "Invalid log line format. Expected 3 semicolon-separated fields: " + rawLine
            );
        }
        LocalDateTime start = LocalDateTime.parse(parts[0].trim(), FORMATTER);
        LocalDateTime end = LocalDateTime.parse(parts[1].trim(), FORMATTER);
        SleepingVerdict verdict = SleepingVerdict.valueOf(parts[2].trim());

        return new SleepingSession(start, end, verdict);
    }

    public long getDurationMinutes() {
        return ChronoUnit.MINUTES.between(start, end);
    }

    public boolean isBad() {
        return verdict == SleepingVerdict.BAD;
    }

    /**
     * Checks if the session overlaps with the 00:00 - 06:00 window of the given morning date.
     */
    public boolean intersectsNight(LocalDate morningDate) {
        LocalDateTime nightStart = morningDate.atTime(NIGHT_START_TIME);
        LocalDateTime nightEnd = morningDate.atTime(NIGHT_END_TIME);
        return start.isBefore(nightEnd) && end.isAfter(nightStart);
    }

    /**
     * Checks if the session counts as night sleep for its wake-up date.
     */
    public boolean isNightSession() {
        return intersectsNight(end.toLocalDate());
    }
}
