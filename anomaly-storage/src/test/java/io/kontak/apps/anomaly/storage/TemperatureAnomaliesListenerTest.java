package io.kontak.apps.anomaly.storage;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.Anomaly;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;
import java.time.Instant;

public class TemperatureAnomaliesListenerTest extends AbstractIntegrationTest {

    @Value("${spring.cloud.stream.bindings.anomalyStorageProcessor-in-0.destination}")
    private String inputTopic;

    @Value("${spring.cloud.stream.bindings.anomalyStorageProcessor-out-0.destination}")
    private String outputTopic;

    @Test
    void testInOutFlow() {
        try (TestKafkaConsumer<AnomalyDocument> consumer = new TestKafkaConsumer<>(
                kafkaContainer.getBootstrapServers(),
                outputTopic,
                AnomalyDocument.class
        );
             TestKafkaProducer<Anomaly> producer = new TestKafkaProducer<>(
                     kafkaContainer.getBootstrapServers(),
                     inputTopic
             )) {
            String thermometerId = "thermometer";
            for (int i = 0; i < 25; i++) {
                Anomaly anomaly = new Anomaly(i, "room", thermometerId, Instant.now());
                producer.produce(anomaly.thermometerId(), anomaly);
            }
            consumer.drain(
                    consumerRecords -> consumerRecords.stream().anyMatch(r -> r.value().thermometerId().equals(thermometerId)),
                    Duration.ofSeconds(5)
            );
        }
    }
}
