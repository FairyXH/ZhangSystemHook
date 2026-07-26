package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
abstract class LocationTimeZoneProviderProxy implements com.android.server.timezonedetector.Dumpable {
    protected final android.content.Context mContext;
    protected com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy.Listener mListener;
    protected final java.lang.Object mSharedLock;
    protected final com.android.server.timezonedetector.location.ThreadingDomain mThreadingDomain;

    interface Listener {
        void onProviderBound();

        void onProviderUnbound();

        void onReportTimeZoneProviderEvent(android.service.timezone.TimeZoneProviderEvent timeZoneProviderEvent);
    }

    abstract void onDestroy();

    abstract void onInitialize();

    abstract void setRequest(com.android.server.timezonedetector.location.TimeZoneProviderRequest timeZoneProviderRequest);

    LocationTimeZoneProviderProxy(android.content.Context context, com.android.server.timezonedetector.location.ThreadingDomain threadingDomain) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mThreadingDomain = (com.android.server.timezonedetector.location.ThreadingDomain) java.util.Objects.requireNonNull(threadingDomain);
        this.mSharedLock = threadingDomain.getLockObject();
    }

    void initialize(com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy.Listener listener) {
        java.util.Objects.requireNonNull(listener);
        synchronized (this.mSharedLock) {
            if (this.mListener != null) {
                throw new java.lang.IllegalStateException("listener already set");
            }
            this.mListener = listener;
            onInitialize();
        }
    }

    void destroy() {
        synchronized (this.mSharedLock) {
            onDestroy();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleTimeZoneProviderEvent$0(android.service.timezone.TimeZoneProviderEvent timeZoneProviderEvent) {
        this.mListener.onReportTimeZoneProviderEvent(timeZoneProviderEvent);
    }

    final void handleTimeZoneProviderEvent(final android.service.timezone.TimeZoneProviderEvent timeZoneProviderEvent) {
        this.mThreadingDomain.post(new java.lang.Runnable() { // from class: com.android.server.timezonedetector.location.LocationTimeZoneProviderProxy$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$handleTimeZoneProviderEvent$0(timeZoneProviderEvent);
            }
        });
    }
}
