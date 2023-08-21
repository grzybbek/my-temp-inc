package io.kontak.apps.temperature.generator;

import io.kontak.apps.event.TemperatureReading;

public interface TemperatureGenerator {

    TemperatureReading generate();
}
