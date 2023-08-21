package io.kontakt.apps.anomaly.detector;

import io.kontak.apps.anomaly.detector.TenSecondsWindowAnomalyDetector;
import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TenSecondsWindowAnomalyDetectorTest {

    @ParameterizedTest
    @MethodSource("provideForWindow")
    void shouldFindAnomalyInTenSecondsWindow(List<TemperatureReading> temperatureReadings, double expectedAnomalyTemperature) {
        TenSecondsWindowAnomalyDetector detector = new TenSecondsWindowAnomalyDetector();
        Optional<Anomaly> anomaly = detector.apply(temperatureReadings);
        assertTrue(anomaly.isPresent());
        assertEquals(expectedAnomalyTemperature, anomaly.get().temperature());
    }

    private static Stream<Arguments> provideForWindow() {
        return Stream.of(
                Arguments.of(createTemperatureReadings1(), 25.1),
                Arguments.of(createTemperatureReadings2(), 25.4)
        );
    }

    @NotNull
    private static List<TemperatureReading> createTemperatureReadings1() {
        return List.of(
                new TemperatureReading(19.1, "roomId", "thermometerId", Instant.ofEpochMilli(1684945005)),
                new TemperatureReading(19.2, "roomId", "thermometerId", Instant.ofEpochMilli(1684945006)),
                new TemperatureReading(19.5, "roomId", "thermometerId", Instant.ofEpochMilli(1684945007)),
                new TemperatureReading(19.7, "roomId", "thermometerId", Instant.ofEpochMilli(1684945008)),
                new TemperatureReading(19.3, "roomId", "thermometerId", Instant.ofEpochMilli(1684945009)),
                new TemperatureReading(25.1, "roomId", "thermometerId", Instant.ofEpochMilli(1684945010))
        );
    }

    @NotNull
    private static List<TemperatureReading> createTemperatureReadings2() {
        return List.of(
                new TemperatureReading(19.1, "roomId", "thermometerId", Instant.ofEpochMilli(1684945005)),
                new TemperatureReading(19.2, "roomId", "thermometerId", Instant.ofEpochMilli(1684945006)),
                new TemperatureReading(19.5, "roomId", "thermometerId", Instant.ofEpochMilli(1684945007)),
                new TemperatureReading(19.7, "roomId", "thermometerId", Instant.ofEpochMilli(1684945008)),
                new TemperatureReading(19.3, "roomId", "thermometerId", Instant.ofEpochMilli(1684945009)),
                new TemperatureReading(25.1, "roomId", "thermometerId", Instant.ofEpochMilli(1684945010)),
                new TemperatureReading(18.2, "roomId", "thermometerId", Instant.ofEpochMilli(1684945011)),
                new TemperatureReading(19.1, "roomId", "thermometerId", Instant.ofEpochMilli(1684945012)),
                new TemperatureReading(19.2, "roomId", "thermometerId", Instant.ofEpochMilli(1684945013)),
                new TemperatureReading(25.4, "roomId", "thermometerId", Instant.ofEpochMilli(1684945015))
        );
    }
}