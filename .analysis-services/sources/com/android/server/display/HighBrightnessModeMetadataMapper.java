package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
class HighBrightnessModeMetadataMapper {
    private static final java.lang.String TAG = "HighBrightnessModeMetadataMapper";
    private final android.util.ArrayMap<java.lang.String, com.android.server.display.HighBrightnessModeMetadata> mHighBrightnessModeMetadataMap = new android.util.ArrayMap<>();

    HighBrightnessModeMetadataMapper() {
    }

    com.android.server.display.HighBrightnessModeMetadata getHighBrightnessModeMetadataLocked(com.android.server.display.LogicalDisplay display) {
        com.android.server.display.DisplayDevice device = display.getPrimaryDisplayDeviceLocked();
        if (device == null) {
            android.util.Slog.wtf(TAG, "Display Device is null in DisplayPowerController for display: " + display.getDisplayIdLocked());
            return null;
        }
        java.lang.String uniqueId = device.getUniqueId();
        if (this.mHighBrightnessModeMetadataMap.containsKey(uniqueId)) {
            return this.mHighBrightnessModeMetadataMap.get(uniqueId);
        }
        com.android.server.display.HighBrightnessModeMetadata hbmInfo = new com.android.server.display.HighBrightnessModeMetadata();
        this.mHighBrightnessModeMetadataMap.put(uniqueId, hbmInfo);
        return hbmInfo;
    }
}
