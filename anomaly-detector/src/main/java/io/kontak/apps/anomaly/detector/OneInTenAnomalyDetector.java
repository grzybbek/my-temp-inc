package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(value = "spring.profile.active", havingValue = "oneInTenAnomaly")
public class OneInTenAnomalyDetector implements AnomalyDetector {
    private static final Logger log = LoggerFactory.getLogger(OneInTenAnomalyDetector.class);

    @Override
    public Optional<Anomaly> apply(List<TemperatureReading> temperatureReadings) {
        if (temperatureReadings.size() < 10) {
            return Optional.empty();
        }
        List<TemperatureReading> lastTenElements = new ArrayList<>(getLastTenElements(temperatureReadings).toList());
        TemperatureReading lastReading = lastTenElements.remove(0);
        double averageOfPreviousReads = getLastTenElements(temperatureReadings)
                .mapToDouble(TemperatureReading::temperature).average().orElse(Double.NaN);
        if (lastReading.temperature() - averageOfPreviousReads < 5) {
            return Optional.empty();
        }
        Anomaly anomaly = new Anomaly(
                lastReading.temperature(),
                lastReading.roomId(),
                lastReading.thermometerId(),
                lastReading.timestamp()
        );
        log.info("Found an anomaly: %s".formatted(anomaly));
        return Optional.of(anomaly);
    }

    private Stream<TemperatureReading> getLastTenElements(List<TemperatureReading> temperatureReadings) {
        int count = 10;
        return IntStream
                .iterate(temperatureReadings.size() - 1, i -> i >= temperatureReadings.size() - count, i -> i - 1)
                .mapToObj(temperatureReadings::get);
    }
}
