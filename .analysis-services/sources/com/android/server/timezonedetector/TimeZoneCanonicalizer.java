package com.android.server.timezonedetector;

/* JADX INFO: loaded from: classes3.dex */
final class TimeZoneCanonicalizer implements java.util.function.Function<java.lang.String, java.lang.String> {
    TimeZoneCanonicalizer() {
    }

    @Override // java.util.function.Function
    public java.lang.String apply(java.lang.String timeZoneId) {
        java.lang.String canonicialZoneId = com.android.i18n.timezone.TimeZoneFinder.getInstance().getCountryZonesFinder().findCanonicalTimeZoneId(timeZoneId);
        return canonicialZoneId == null ? timeZoneId : canonicialZoneId;
    }
}
