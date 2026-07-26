package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
public final class GeolocationTimeZoneSuggestion {
    private final long mEffectiveFromElapsedMillis;
    private final java.util.List<java.lang.String> mZoneIds;

    private GeolocationTimeZoneSuggestion(long effectiveFromElapsedMillis, java.util.List<java.lang.String> zoneIds) {
        this.mEffectiveFromElapsedMillis = effectiveFromElapsedMillis;
        if (zoneIds == null) {
            this.mZoneIds = null;
        } else {
            this.mZoneIds = java.util.Collections.unmodifiableList(new java.util.ArrayList(zoneIds));
        }
    }

    public static com.android.server.timezonedetector.GeolocationTimeZoneSuggestion createUncertainSuggestion(long effectiveFromElapsedMillis) {
        return new com.android.server.timezonedetector.GeolocationTimeZoneSuggestion(effectiveFromElapsedMillis, null);
    }

    public static com.android.server.timezonedetector.GeolocationTimeZoneSuggestion createCertainSuggestion(long effectiveFromElapsedMillis, java.util.List<java.lang.String> zoneIds) {
        return new com.android.server.timezonedetector.GeolocationTimeZoneSuggestion(effectiveFromElapsedMillis, zoneIds);
    }

    public long getEffectiveFromElapsedMillis() {
        return this.mEffectiveFromElapsedMillis;
    }

    public java.util.List<java.lang.String> getZoneIds() {
        return this.mZoneIds;
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        com.android.server.timezonedetector.GeolocationTimeZoneSuggestion that = (com.android.server.timezonedetector.GeolocationTimeZoneSuggestion) o;
        if (this.mEffectiveFromElapsedMillis == that.mEffectiveFromElapsedMillis && java.util.Objects.equals(this.mZoneIds, that.mZoneIds)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Long.valueOf(this.mEffectiveFromElapsedMillis), this.mZoneIds);
    }

    public java.lang.String toString() {
        return "GeolocationTimeZoneSuggestion{mEffectiveFromElapsedMillis=" + this.mEffectiveFromElapsedMillis + ", mZoneIds=" + this.mZoneIds + '}';
    }
}
