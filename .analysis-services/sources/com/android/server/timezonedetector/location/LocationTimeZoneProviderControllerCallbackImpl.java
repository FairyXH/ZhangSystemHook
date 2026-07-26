package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
class LocationTimeZoneProviderControllerCallbackImpl extends com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Callback {
    LocationTimeZoneProviderControllerCallbackImpl(com.android.server.timezonedetector.location.ThreadingDomain threadingDomain) {
        super(threadingDomain);
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderController.Callback
    void sendEvent(com.android.server.timezonedetector.LocationAlgorithmEvent event) {
        this.mThreadingDomain.assertCurrentThread();
        com.android.server.timezonedetector.TimeZoneDetectorInternal timeZoneDetector = (com.android.server.timezonedetector.TimeZoneDetectorInternal) com.android.server.LocalServices.getService(com.android.server.timezonedetector.TimeZoneDetectorInternal.class);
        timeZoneDetector.handleLocationAlgorithmEvent(event);
    }
}
