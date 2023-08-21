package io.kontak.apps.anomaly.storage;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "anomalies")
public record AnomalyDocument(
        @Id String id,
        double temperature,
        @Indexed String roomId,
        @Indexed String thermometerId,
        Instant timestamp) {

}
