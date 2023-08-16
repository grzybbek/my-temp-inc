package io.kontak.apps.temperature.analytics;

import io.kontak.apps.event.Anomaly;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("analytics")
public class AnalyticsController {

    @GetMapping("/anomalies")
    public List<Anomaly> getAnomaliesPerThermometerId(@RequestParam(name = "thermometerId") String thermometerId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/anomalies")
    public List<Anomaly> getAnomaliesPerRoomId(@RequestParam(name = "roomId") String roomId) {
        throw new UnsupportedOperationException();
    }

    @GetMapping("/thermometers")
    public List<String> getThermometersWithAnomaliesMoreThan(@RequestParam(name = "threshold") String threshold) {
        throw new UnsupportedOperationException();
    }
}
