package io.kontak.apps.anomaly.storage;

import org.springframework.data.annotation.Id;

record Thermometer(@Id String id, String thermometerId, Integer anomaliesCount) {
}
