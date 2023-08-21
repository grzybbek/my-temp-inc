package io.kontak.apps.anomaly.storage;

import org.junit.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AnomalyRepositoryTest {

    @Autowired
    private AnomalyRepository anomalyRepository;

    @Test
    public void shouldFindByThermometerId() {
        anomalyRepository.save(new AnomalyDocument("1", 20.4, "roomId1", "thermometerId1", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("2", 25.6, "roomId1", "thermometerId1", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("3", 18.9, "roomId1", "thermometerId2", Instant.now()));

        List<AnomalyDocument> foundByThermometer = anomalyRepository.findByThermometerId("thermometerId1");

        assertEquals(2, foundByThermometer.size());

        assertEquals("1", foundByThermometer.get(0).id());
        assertEquals("2", foundByThermometer.get(1).id());
    }

    @Test
    public void shouldFindByRoomId() {
        anomalyRepository.save(new AnomalyDocument("4", 20.3, "roomId2", "thermometerId3", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("5", 21.5, "roomId2", "thermometerId4", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("6", 19.7, "roomId3", "thermometerId5", Instant.now()));

        List<AnomalyDocument> foundByRoomId = anomalyRepository.findByRoomId("roomId2");

        assertEquals(2, foundByRoomId.size());

        assertEquals("4", foundByRoomId.get(0).id());
        assertEquals("5", foundByRoomId.get(1).id());
    }

    @Test
    public void shouldFindThermometersWithMoreAnomaliesThanThreshold() {
        anomalyRepository.save(new AnomalyDocument("7", 20.4, "roomId4", "thermometerId6", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("8", 25.6, "roomId4", "thermometerId6", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("9", 18.9, "roomId4", "thermometerId6", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("10", 19.7, "roomId4", "thermometerId6", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("11", 20.3, "roomId4", "thermometerId7", Instant.now()));
        anomalyRepository.save(new AnomalyDocument("12", 21.5, "roomId4", "thermometerId7", Instant.now()));

        List<Thermometer> thermometers = anomalyRepository.findThermometersWithMoreAnomaliesThanThreshold(3);

        assertEquals(1, thermometers.size());
    }

    @Test
    public void shouldFindByTemperatureAndRoomIdAndThermometerIdAndTimestamp() {
        AnomalyDocument anomalyDocument = new AnomalyDocument("13", 27.8, "roomId5", "thermometerId8", Instant.ofEpochMilli(1684945006));
        anomalyRepository.save(anomalyDocument);

        Optional<AnomalyDocument> anomaly = anomalyRepository.findByTemperatureAndRoomIdAndThermometerIdAndTimestamp(27.8, "roomId5", "thermometerId8", Instant.ofEpochMilli(1684945006));

        assertTrue(anomaly.isPresent());
        assertEquals(anomalyDocument, anomaly.get());
    }
}