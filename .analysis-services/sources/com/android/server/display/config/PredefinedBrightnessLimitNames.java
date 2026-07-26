package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public enum PredefinedBrightnessLimitNames {
    _default("default"),
    adaptive("adaptive");

    private final java.lang.String rawName;

    PredefinedBrightnessLimitNames(java.lang.String rawName) {
        this.rawName = rawName;
    }

    public java.lang.String getRawName() {
        return this.rawName;
    }

    static com.android.server.display.config.PredefinedBrightnessLimitNames fromString(java.lang.String rawString) {
        for (com.android.server.display.config.PredefinedBrightnessLimitNames _f : values()) {
            if (_f.getRawName().equals(rawString)) {
                return _f;
            }
        }
        throw new java.lang.IllegalArgumentException(rawString);
    }
}
