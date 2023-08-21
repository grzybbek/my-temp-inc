package io.kontak.apps.temperature.analytics;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.Thermometer;
import io.kontak.apps.temperature.analytics.dao.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class AnalyticsService {
    private final WebClient webClient;

    public AnalyticsService(WebClient webClient) {
        this.webClient = webClient;
    }

    public List<Anomaly> getAnomaliesPerThermometerId(String thermometerId) {
        return webClient.get()
                .uri("/anomalies/search/findByThermometerId?thermometerId=%s".formatted(thermometerId))
                .retrieve().bodyToFlux(Result.class).flatMapIterable(r -> r._embedded().anomalies()).collectList().block();
    }

    public List<Anomaly> getAnomaliesPerRoomId(String roomId) {
        return webClient.get()
                .uri("/anomalies/search/findByRoomId?roomId=%s".formatted(roomId))
                .retrieve().bodyToFlux(Result.class).flatMapIterable(r -> r._embedded().anomalies()).collectList().block();
    }

    public List<Thermometer> getThermometersWithAnomaliesMoreThan(String threshold) {
        return webClient.get()
                .uri("/anomalies/search/findThermometersWithMoreAnomaliesThanThreshold?threshold=%s".formatted(threshold))
                .retrieve().bodyToFlux(Result.class).flatMapIterable(r -> r._embedded().thermometers()).collectList().block();
    }
}
