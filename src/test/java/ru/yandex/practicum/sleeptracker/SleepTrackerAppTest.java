package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SleepTrackerAppTest {

    private final PrintStream standardOut = System.out;
    private final PrintStream standardErr = System.err;
    private final ByteArrayOutputStream outputCaptor = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errCaptor = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputCaptor));
        System.setErr(new PrintStream(errCaptor));
    }

    @AfterEach
    void tearDown() {
        System.setOut(standardOut);
        System.setErr(standardErr);
    }

    @Test
    @DisplayName("Проверка обработки некорректных аргументов командной строки")
    void shouldShowErrorWhenInvalidArgumentsProvided() {
        SleepTrackerApp.main(new String[]{});
        assertTrue(errCaptor.toString().contains("Required one program argument"));

        errCaptor.reset();

        SleepTrackerApp.main(new String[]{"file1.log", "file2.log"});
        assertTrue(errCaptor.toString().contains("Required one program argument"));
    }

    @Test
    @DisplayName("Проверка обработки несуществующего файла лога")
    void shouldHandleMissingFileError() {
        SleepTrackerApp.main(new String[]{"non_existent_file_123.log"});
        assertTrue(errCaptor.toString().contains("Exception occurred while reading log file"));
    }

    @Test
    @DisplayName("Запуск main с валидным файлом и проверка всех метрик")
    void shouldProcessFileAndPrintCorrectMetrics(@TempDir Path tempDir) throws IOException {
        String logContent = """
                01.10.25 23:00;02.10.25 07:00;GOOD
                02.10.25 14:00;02.10.25 15:00;NORMAL
                03.10.25 15:00;03.10.25 17:00;NORMAL
                03.10.25 23:00;04.10.25 08:00;BAD
                """;

        Path logFile = tempDir.resolve("sleep.log");
        Files.writeString(logFile, logContent);

        SleepTrackerApp.main(new String[]{logFile.toString()});

        String output = outputCaptor.toString();
        assertTrue(output.contains("Всего сессий сна: 4"));
        assertTrue(output.contains("Минимальная продолжительность сессии (мин): 60"));
        assertTrue(output.contains("Максимальная продолжительность сессии (мин): 540"));
        assertTrue(output.contains("Средняя продолжительность сессии (мин): 300.0"));
        assertTrue(output.contains("Количество сессий с плохим качеством сна: 1"));
        assertTrue(output.contains("Количество бессонных ночей: 1"));
        assertTrue(output.contains("Хронотип пользователя: Голубь"));
    }
}
