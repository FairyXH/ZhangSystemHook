package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public enum ThermalStatus {
    none("none"),
    light("light"),
    moderate("moderate"),
    severe("severe"),
    critical("critical"),
    emergency("emergency"),
    shutdown("shutdown");

    private final java.lang.String rawName;

    ThermalStatus(java.lang.String rawName) {
        this.rawName = rawName;
    }

    public java.lang.String getRawName() {
        return this.rawName;
    }

    static com.android.server.display.config.ThermalStatus fromString(java.lang.String rawString) {
        for (com.android.server.display.config.ThermalStatus _f : values()) {
            if (_f.getRawName().equals(rawString)) {
                return _f;
            }
        }
        throw new java.lang.IllegalArgumentException(rawString);
    }
}
