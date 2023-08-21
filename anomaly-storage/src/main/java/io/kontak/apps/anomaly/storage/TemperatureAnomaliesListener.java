package io.kontak.apps.anomaly.storage;

import io.kontak.apps.event.Anomaly;
import org.apache.kafka.streams.kstream.KStream;

import java.util.function.Function;

public class TemperatureAnomaliesListener implements Function<KStream<String, Anomaly>, KStream<String, AnomalyDocument>> {

    private final AnomalyService anomalyService;

    public TemperatureAnomaliesListener(AnomalyService anomalyService) {
        this.anomalyService = anomalyService;
    }

    @Override
    public KStream<String, AnomalyDocument> apply(KStream<String, Anomaly> events) {
        return events.mapValues(anomalyService::getSavedAnomalyDocument);
    }
}
