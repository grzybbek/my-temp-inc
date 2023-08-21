package io.kontak.apps.anomaly.detector;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.TemperatureReading;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class TemperatureMeasurementsListener implements Function<KStream<String, TemperatureReading>, KStream<String, Anomaly>> {

    private static final Logger log = LoggerFactory.getLogger(TemperatureMeasurementsListener.class);

    private final AnomalyDetector anomalyDetector;
    private final List<TemperatureReading> temperatureReadings = new ArrayList<>();

    public TemperatureMeasurementsListener(AnomalyDetector anomalyDetector) {
        this.anomalyDetector = anomalyDetector;
    }

    @Override
    public KStream<String, Anomaly> apply(KStream<String, TemperatureReading> events) {
        return events
                .mapValues((temperatureReading) -> {
                    log.info("Received temperature reading: %s".formatted(temperatureReading));
                    temperatureReadings.add(temperatureReading);
                    return anomalyDetector.apply(temperatureReadings);
                })
                .filter((s, anomaly) -> anomaly.isPresent())
                .mapValues((s, anomaly) -> anomaly.get())
                .selectKey((s, anomaly) -> anomaly.thermometerId());
    }

    @Scheduled(fixedRateString = "${temperatures-detector.rate.minutes}", timeUnit = TimeUnit.MINUTES)
    public void removeOldTemperatureReadings() {
        Instant oneMinuteAgo = Instant.now().minus(1, ChronoUnit.MINUTES);
        boolean removed = temperatureReadings.removeIf(temperatureReading -> temperatureReading.timestamp().isBefore(oneMinuteAgo));
        log.info("Removed old temperature readings: %s".formatted(removed));
    }
}
