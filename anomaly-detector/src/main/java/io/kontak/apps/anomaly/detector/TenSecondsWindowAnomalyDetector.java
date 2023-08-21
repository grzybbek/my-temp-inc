package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@ConditionalOnProperty(value = "spring.profile.active", havingValue = "tenSecondsWindowAnomaly")
public class TenSecondsWindowAnomalyDetector implements AnomalyDetector {
    private static final Logger log = LoggerFactory.getLogger(TenSecondsWindowAnomalyDetector.class);

    @Override
    public Optional<Anomaly> apply(List<TemperatureReading> temperatureReadings) {
        Instant timestampOfNewestRead = temperatureReadings.get(temperatureReadings.size() - 1).timestamp();
        Instant windowStart = timestampOfNewestRead.minus(10, ChronoUnit.SECONDS);
        List<TemperatureReading> readingsInWindow = new ArrayList<>(temperatureReadings.stream().filter(t -> t.timestamp().isAfter(windowStart)).toList());
        double averageOfWindow = readingsInWindow.stream().mapToDouble(TemperatureReading::temperature).average().orElse(Double.NaN);
        Collections.reverse(readingsInWindow);
        return readingsInWindow.stream().filter(t -> Math.round(t.temperature() - averageOfWindow) >= 5)
                .findFirst()
                .map(t -> {
                    Anomaly anomaly = new Anomaly(
                            t.temperature(),
                            t.roomId(),
                            t.thermometerId(),
                            t.timestamp()
                    );
                    log.info("Found an anomaly: %s".formatted(anomaly));
                    return anomaly;
                });
    }

}
