package io.kontakt.apps.temperature.generator;

import io.kontak.apps.event.TemperatureReading;
import io.kontak.apps.temperature.generator.SimpleTemperatureGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleTemperatureGeneratorTest {
    private SimpleTemperatureGenerator generator;

    @BeforeEach
    void initUseCase() {
        generator = new SimpleTemperatureGenerator();
    }

    @Test
    void shouldGenerateReadingsWithConsecutiveTimestamps() throws InterruptedException {
        TemperatureReading generatedReading1 = generator.generate();
        Thread.sleep(100);
        TemperatureReading generatedReading2 = generator.generate();
        Thread.sleep(100);
        TemperatureReading generatedReading3 = generator.generate();
        Thread.sleep(100);
        TemperatureReading generatedReading4 = generator.generate();

        assertTrue(generatedReading1.timestamp().isBefore(generatedReading2.timestamp()));
        assertTrue(generatedReading2.timestamp().isBefore(generatedReading3.timestamp()));
        assertTrue(generatedReading3.timestamp().isBefore(generatedReading4.timestamp()));
    }

    @RepeatedTest(10)
    void shouldGenerateTemperaturesBetween10And30Degrees() {
        TemperatureReading generatedReading = generator.generate();
        assertTrue(generatedReading.temperature() >= 10 && generatedReading.temperature() < 30);
    }

    @RepeatedTest(10)
    void shouldGenerateTemperaturesWithNonNullRoomIdAndThermometerId() {
        TemperatureReading generatedReading = generator.generate();
        assertNotNull(generatedReading.roomId());
        assertNotNull(generatedReading.thermometerId());
    }
}