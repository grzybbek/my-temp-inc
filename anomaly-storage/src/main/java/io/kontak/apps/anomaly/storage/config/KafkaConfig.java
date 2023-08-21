package io.kontak.apps.anomaly.storage.config;

import io.kontak.apps.anomaly.storage.AnomalyDocument;
import io.kontak.apps.anomaly.storage.AnomalyService;
import io.kontak.apps.anomaly.storage.TemperatureAnomaliesListener;
import io.kontak.apps.event.Anomaly;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class KafkaConfig {

    @Bean
    public Function<KStream<String, Anomaly>, KStream<String, AnomalyDocument>> anomalyStorageProcessor(AnomalyService service) {
        return new TemperatureAnomaliesListener(service);
    }

}
