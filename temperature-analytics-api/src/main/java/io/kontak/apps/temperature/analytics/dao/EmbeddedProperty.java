package io.kontak.apps.temperature.analytics.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.kontak.apps.event.Anomaly;
import io.kontak.apps.event.Thermometer;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmbeddedProperty(
        List<Anomaly> anomalies,
        List<Thermometer> thermometers
) {

}