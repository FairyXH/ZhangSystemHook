package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public enum AutoBrightnessModeName {
    _default("default"),
    idle("idle"),
    doze("doze");

    private final java.lang.String rawName;

    AutoBrightnessModeName(java.lang.String rawName) {
        this.rawName = rawName;
    }

    public java.lang.String getRawName() {
        return this.rawName;
    }

    static com.android.server.display.config.AutoBrightnessModeName fromString(java.lang.String rawString) {
        for (com.android.server.display.config.AutoBrightnessModeName _f : values()) {
            if (_f.getRawName().equals(rawString)) {
                return _f;
            }
        }
        throw new java.lang.IllegalArgumentException(rawString);
    }
}
