package org.mybop.weatherapplication;

import android.content.Context;

import java.util.Locale;

public enum UnitSystem {
    METRIC(0, "metric", "°C", "m/s"),
    IMPERIAL(1, "imperial", "°F", "mph");

    private final int index;

    private final String param;

    private final String temperatureUnit;

    private final String speedUnit;

    UnitSystem(int index, String param, String temperatureUnit, String speedUnit) {
        this.index = index;
        this.param = param;
        this.temperatureUnit = temperatureUnit;
        this.speedUnit = speedUnit;
    }

    public int getIndex() {
        return index;
    }

    public String getParam() {
        return param;
    }

    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public String getSpeedUnit() {
        return speedUnit;
    }

    public String getString(Context context) {
        return context.getResources()
                .getStringArray(R.array.measurement)[index];
    }

    public static UnitSystem valueOf(int index) {
        for (UnitSystem system : values()) {
            if (system.index == index) {
                return system;
            }
        }
        throw new IllegalArgumentException(String.format(Locale.ENGLISH, "given index %d doesn't exist", index));
    }
}
