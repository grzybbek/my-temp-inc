package io.kontak.apps.temperature.generator;

import io.kontak.apps.event.TemperatureReading;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SimpleTemperatureGenerator implements TemperatureGenerator {
    private static final Logger log = LoggerFactory.getLogger(SimpleTemperatureGenerator.class);

    private final Random random = new Random();
    private final Map<String, List<String>> roomsWithThermometers = new HashMap<>();
    private final List<String> rooms;

    public SimpleTemperatureGenerator() {
        IntStream.range(0, random.nextInt(2, 5))
                .forEach(i -> roomsWithThermometers.put(
                        UUID.randomUUID().toString(),
                        IntStream.range(0, random.nextInt(1, 3))
                                .mapToObj(j -> UUID.randomUUID().toString()).collect(Collectors.toList())));

        rooms = new ArrayList<>(roomsWithThermometers.keySet());
    }

    @Override
    public TemperatureReading generate() {
        String roomId = rooms.get(random.nextInt(rooms.size()));
        List<String> thermometers = roomsWithThermometers.get(roomId);
        String thermometerId = thermometers.get(random.nextInt(thermometers.size()));

        TemperatureReading temperatureReading = new TemperatureReading(
                random.nextDouble(10d, 30d),
                roomId,
                thermometerId,
                Instant.now()
        );
        log.info("Generated temperature reading: %s".formatted(temperatureReading));
        return temperatureReading;
    }

}
