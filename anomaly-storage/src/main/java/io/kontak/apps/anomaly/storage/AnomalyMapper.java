package io.kontak.apps.anomaly.storage;

import io.kontak.apps.anomaly.storage.AnomalyDocument;
import io.kontak.apps.event.Anomaly;
import org.springframework.stereotype.Service;

@Service
public class AnomalyMapper {
    public AnomalyDocument mapAnomalyToDocument(Anomaly anomaly) {
        return new AnomalyDocument(null, anomaly.temperature(), anomaly.roomId(), anomaly.thermometerId(), anomaly.timestamp());
    }
}
