package io.kontak.apps.temperature.analytics;

import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.Thermometer;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AnalyticsController.class)
class AnalyticsControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AnalyticsService service;

    @Test
    void shouldReturnAnomaliesPerThermometerId() throws Exception {
        String thermometerId = "thermometerId";
        List<Anomaly> anomalies = List.of(
                new Anomaly(23.5, "roomId1", thermometerId, Instant.parse("2023-08-21T15:24:10.690Z")),
                new Anomaly(18.3, "roomId1", thermometerId, Instant.parse("2023-08-21T15:24:18.690Z"))
        );
        when(service.getAnomaliesPerThermometerId(thermometerId)).thenReturn(anomalies);

        mvc.perform(get("/analytics/anomalies?thermometerId=%s".formatted(thermometerId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("""
                        [
                            {
                                "temperature": 23.5,
                                "roomId": "roomId1",
                                "thermometerId": "thermometerId",
                                "timestamp": "2023-08-21T15:24:10.690Z"
                            },
                            {
                                "temperature": 18.3,
                                "roomId": "roomId1",
                                "thermometerId": "thermometerId",
                                "timestamp": "2023-08-21T15:24:18.690Z"
                            }
                        ]""".replaceAll("\\s+", "")));
    }

    @Test
    void shouldReturnAnomaliesPerRoomId() throws Exception {
        String roomId = "roomId1";
        List<Anomaly> anomalies = List.of(
                new Anomaly(23.5, roomId, "thermometerId", Instant.parse("2023-08-21T15:24:10.690Z")),
                new Anomaly(18.3, roomId, "thermometerId", Instant.parse("2023-08-21T15:24:18.690Z"))
        );
        when(service.getAnomaliesPerRoomId(roomId)).thenReturn(anomalies);

        mvc.perform(get("/analytics/anomalies?roomId=%s".formatted(roomId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("""
                        [
                            {
                                "temperature": 23.5,
                                "roomId": "roomId1",
                                "thermometerId": "thermometerId",
                                "timestamp": "2023-08-21T15:24:10.690Z"
                            },
                            {
                                "temperature": 18.3,
                                "roomId": "roomId1",
                                "thermometerId": "thermometerId",
                                "timestamp": "2023-08-21T15:24:18.690Z"
                            }
                        ]""".replaceAll("\\s+", "")));
    }

    @Test
    void getThermometersWithAnomaliesMoreThan() throws Exception {
        Integer threshold = 14;
        List<Thermometer> thermometers = List.of(
                new Thermometer("thermometer1", 15),
                new Thermometer("thermometer2", 23)
        );
        when(service.getThermometersWithAnomaliesMoreThan(threshold.toString())).thenReturn(thermometers);
        mvc.perform(get("/analytics/thermometers?threshold=%s".formatted(threshold))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("""
                        [
                            {
                                "thermometerId": "thermometer1",
                                "anomaliesCount": 15
                            },
                            {
                                "thermometerId": "thermometer2",
                                "anomaliesCount": 23
                            }
                        ]""".replaceAll("\\s+", "")));
    }
}