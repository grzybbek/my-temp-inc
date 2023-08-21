package io.kontakt.apps.anomaly.detector;

import io.kontak.apps.anomaly.detector.OneInTenAnomalyDetector;
import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OneInTenAnomalyDetectorTest {

    @Test
    void shouldFindAnomalyInTenLastReadings() {
        OneInTenAnomalyDetector detector = new OneInTenAnomalyDetector();
        Optional<Anomaly> anomaly = detector.apply(createTemperatureReadings());
        assertTrue(anomaly.isPresent());
        assertEquals(27.1, anomaly.get().temperature());
    }

    @NotNull
    private static List<TemperatureReading> createTemperatureReadings() {
        return List.of(
                new TemperatureReading(20.1, "roomId", "thermometerId", Instant.now()),
                new TemperatureReading(21.2, "roomId", "thermometerId", Instant.now()),
                new TemperatureReading(20.3, "roomId", "thermometerId", Instant.now()),
                new TemperatureReading(19.1, "roomId", "thermometerId", Instant.now()),
                new TemperatureReading(20.1, "roomId", "thermometerId", Instant.now()),
                new TemperatureReading(19.2, "roomId", "thermometerId", Instant.now()),
                new TemperatureReading(20.1, "roomId", "thermometerId", Instant.now()),
                new TemperatureReading(18.1, "roomId", "thermometerId", Instant.now()),
                new TemperatureReading(19.4, "roomId", "thermometerId", Instant.now()),
                new TemperatureReading(20.1, "roomId", "thermometerId", Instant.now()),
                new TemperatureReading(27.1, "roomId", "thermometerId", Instant.now())
        );
    }
}