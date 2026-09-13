package ru.yandex.practicum.sleeptracker.function.impl;

import ru.yandex.practicum.sleeptracker.function.SleepAnalysisFunction;
import ru.yandex.practicum.sleeptracker.model.Chronotype;
import ru.yandex.practicum.sleeptracker.model.SleepAnalysisResult;
import ru.yandex.practicum.sleeptracker.model.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DetectChronotypeFunction implements SleepAnalysisFunction<Chronotype> {

    private static final String DESCRIPTION = "Хронотип пользователя";

    private static final LocalTime OWL_BED_TIME = LocalTime.of(23, 0);
    private static final LocalTime OWL_WAKE_TIME = LocalTime.of(9, 0);
    private static final LocalTime LARK_BED_TIME = LocalTime.of(22, 0);
    private static final LocalTime LARK_WAKE_TIME = LocalTime.of(7, 0);

    @Override
    public SleepAnalysisResult<Chronotype> apply(List<SleepingSession> sessions) {
        Map<Chronotype, Long> counts = sessions.stream()
                .filter(SleepingSession::isNightSession)
                .map(this::classifySession)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        long owls = counts.getOrDefault(Chronotype.OWL, 0L);
        long larks = counts.getOrDefault(Chronotype.LARK, 0L);
        long doves = counts.getOrDefault(Chronotype.DOVE, 0L);

        Chronotype finalType;
        if (owls > larks && owls > doves) {
            finalType = Chronotype.OWL;
        } else if (larks > owls && larks > doves) {
            finalType = Chronotype.LARK;
        } else {
            finalType = Chronotype.DOVE;
        }

        return new SleepAnalysisResult<>(DESCRIPTION, finalType);
    }

    private Chronotype classifySession(SleepingSession s) {
        LocalDate morningDate = s.end().toLocalDate();

        LocalDateTime owlBedThreshold = morningDate.minusDays(1).atTime(OWL_BED_TIME);
        LocalDateTime larkBedThreshold = morningDate.minusDays(1).atTime(LARK_BED_TIME);

        LocalDateTime owlWakeThreshold = morningDate.atTime(OWL_WAKE_TIME);
        LocalDateTime larkWakeThreshold = morningDate.atTime(LARK_WAKE_TIME);

        boolean isOwl = s.start().isAfter(owlBedThreshold) && s.end().isAfter(owlWakeThreshold);
        if (isOwl) {
            return Chronotype.OWL;
        }

        boolean isLark = s.start().isBefore(larkBedThreshold) && s.end().isBefore(larkWakeThreshold);
        if (isLark) {
            return Chronotype.LARK;
        }

        return Chronotype.DOVE;
    }
}
