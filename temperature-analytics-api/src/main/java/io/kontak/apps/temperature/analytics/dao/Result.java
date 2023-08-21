package io.kontak.apps.temperature.analytics.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Result(
        EmbeddedProperty _embedded
) {
}