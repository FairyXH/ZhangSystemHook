package com.android.server.twilight;

/* JADX INFO: loaded from: classes3.dex */
public final class TwilightState {
    private final long mSunriseTimeMillis;
    private final long mSunsetTimeMillis;

    public TwilightState(long sunriseTimeMillis, long sunsetTimeMillis) {
        this.mSunriseTimeMillis = sunriseTimeMillis;
        this.mSunsetTimeMillis = sunsetTimeMillis;
    }

    public long sunriseTimeMillis() {
        return this.mSunriseTimeMillis;
    }

    public java.time.LocalDateTime sunrise() {
        java.time.ZoneId zoneId = java.util.TimeZone.getDefault().toZoneId();
        return java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(this.mSunriseTimeMillis), zoneId);
    }

    public long sunsetTimeMillis() {
        return this.mSunsetTimeMillis;
    }

    public java.time.LocalDateTime sunset() {
        java.time.ZoneId zoneId = java.util.TimeZone.getDefault().toZoneId();
        return java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(this.mSunsetTimeMillis), zoneId);
    }

    public boolean isNight() {
        long now = java.lang.System.currentTimeMillis();
        return now >= this.mSunsetTimeMillis && now < this.mSunriseTimeMillis;
    }

    public boolean equals(java.lang.Object o) {
        return (o instanceof com.android.server.twilight.TwilightState) && equals((com.android.server.twilight.TwilightState) o);
    }

    public boolean equals(com.android.server.twilight.TwilightState other) {
        return other != null && this.mSunriseTimeMillis == other.mSunriseTimeMillis && this.mSunsetTimeMillis == other.mSunsetTimeMillis;
    }

    public int hashCode() {
        return java.lang.Long.hashCode(this.mSunriseTimeMillis) ^ java.lang.Long.hashCode(this.mSunsetTimeMillis);
    }

    public java.lang.String toString() {
        return "TwilightState { sunrise=" + ((java.lang.Object) android.text.format.DateFormat.format("MM-dd HH:mm", this.mSunriseTimeMillis)) + " sunset=" + ((java.lang.Object) android.text.format.DateFormat.format("MM-dd HH:mm", this.mSunsetTimeMillis)) + " }";
    }
}
