package io.kontak.apps.anomaly.storage;

import io.kontak.apps.event.Anomaly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AnomalyService {
    private static final Logger log = LoggerFactory.getLogger(AnomalyService.class);
    private final AnomalyRepository anomalyRepository;
    private final AnomalyMapper mapper;

    public AnomalyService(AnomalyRepository anomalyRepository, AnomalyMapper mapper) {
        this.anomalyRepository = anomalyRepository;
        this.mapper = mapper;
    }

    public AnomalyDocument getSavedAnomalyDocument(Anomaly anomaly) {
        Optional<AnomalyDocument> anomalyDocument = findIfAlreadyExists(anomaly);
        if (anomalyDocument.isPresent()) {
            return anomalyDocument.get();
        }
        log.info("Saving new anomaly: %s".formatted(anomaly));
        return anomalyRepository.save(mapper.mapAnomalyToDocument(anomaly));
    }

    private Optional<AnomalyDocument> findIfAlreadyExists(Anomaly anomaly) {
        return anomalyRepository.findByTemperatureAndRoomIdAndThermometerIdAndTimestamp(
                anomaly.temperature(), anomaly.roomId(), anomaly.thermometerId(), anomaly.timestamp());
    }
}
