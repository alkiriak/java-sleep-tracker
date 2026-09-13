package ru.yandex.practicum.sleeptracker.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SleepAnalysisResultTest {

    @Test
    @DisplayName("toDisplayString: корректное форматирование строк для различных типов данных")
    void shouldFormatDisplayStringForDifferentTypes() {
        SleepAnalysisResult<Double> doubleResult = new SleepAnalysisResult<>("Avg", 412.365);
        SleepAnalysisResult<Long> longResult = new SleepAnalysisResult<>("Max", 520L);
        SleepAnalysisResult<Chronotype> enumResult = new SleepAnalysisResult<>("Type", Chronotype.OWL);
        SleepAnalysisResult<Long> nullResult = new SleepAnalysisResult<>("Min", null);

        assertAll(
                () -> assertEquals("Avg: 412.4", doubleResult.toDisplayString()),
                () -> assertEquals("Max: 520", longResult.toDisplayString()),
                () -> assertEquals("Type: Сова", enumResult.toDisplayString()),
                () -> assertEquals("Min: N/A", nullResult.toDisplayString())
        );
    }
}
