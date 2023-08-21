package io.kontak.apps.anomaly.storage;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel = "anomalies", path = "anomalies")
public interface AnomalyRepository extends MongoRepository<AnomalyDocument, String> {

    Optional<AnomalyDocument> findByTemperatureAndRoomIdAndThermometerIdAndTimestamp(
            double temperature, String roomId, String thermometerId, Instant timestamp);

    List<AnomalyDocument> findByThermometerId(String thermometerId);

    List<AnomalyDocument> findByRoomId(String roomId);

    @Aggregation(pipeline = {
            """
                    {
                        $group: {
                          _id: "$thermometerId",
                          thermometerId: {
                            $first: "$thermometerId",
                          },
                          anomaliesCount: {
                            $sum: 1,
                          },
                        },
                      }""",
            """
                    {
                        $match: {
                            anomaliesCount: {
                              $gt: ?0,
                            },
                          },
                      }""",
    })
    List<Thermometer> findThermometersWithMoreAnomaliesThanThreshold(Integer threshold);
}
