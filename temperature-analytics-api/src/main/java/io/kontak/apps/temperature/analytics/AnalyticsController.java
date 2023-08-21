package io.kontak.apps.temperature.analytics;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.Thermometer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/anomalies")
    public List<Anomaly> getAnomalies(
            @RequestParam(name = "thermometerId", required = false) String thermometerId,
            @RequestParam(name = "roomId", required = false) String roomId) {
        if (Optional.ofNullable(thermometerId).isPresent()) {
            return analyticsService.getAnomaliesPerThermometerId(thermometerId);
        } else if (Optional.ofNullable(roomId).isPresent()) {
            return analyticsService.getAnomaliesPerRoomId(roomId);
        }
        throw new UnsupportedOperationException("RequestParam must be set: thermometerId / roomId");
    }

    @GetMapping("/thermometers")
    public List<Thermometer> getThermometersWithAnomaliesMoreThan(@RequestParam(name = "threshold") String threshold) {
        return analyticsService.getThermometersWithAnomaliesMoreThan(threshold);
    }
}
