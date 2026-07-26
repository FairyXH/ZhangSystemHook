package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
public class ZoneInfoDbTimeZoneProviderEventPreProcessor implements com.android.server.timezonedetector.location.TimeZoneProviderEventPreProcessor {
    @Override // com.android.server.timezonedetector.location.TimeZoneProviderEventPreProcessor
    public android.service.timezone.TimeZoneProviderEvent preProcess(android.service.timezone.TimeZoneProviderEvent event) {
        if (event.getSuggestion() != null && !event.getSuggestion().getTimeZoneIds().isEmpty() && hasInvalidZones(event)) {
            android.service.timezone.TimeZoneProviderStatus providerStatus = new android.service.timezone.TimeZoneProviderStatus.Builder(event.getTimeZoneProviderStatus()).setTimeZoneResolutionOperationStatus(3).build();
            return android.service.timezone.TimeZoneProviderEvent.createUncertainEvent(event.getCreationElapsedMillis(), providerStatus);
        }
        return event;
    }

    private static boolean hasInvalidZones(android.service.timezone.TimeZoneProviderEvent event) {
        for (java.lang.String timeZone : event.getSuggestion().getTimeZoneIds()) {
            if (!com.android.i18n.timezone.ZoneInfoDb.getInstance().hasTimeZone(timeZone)) {
                com.android.server.timezonedetector.location.LocationTimeZoneManagerService.infoLog("event=" + event + " has unsupported zone(" + timeZone + ")");
                return true;
            }
        }
        return false;
    }
}
