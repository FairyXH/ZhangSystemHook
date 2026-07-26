package com.android.server.location.eventlog;

/* JADX INFO: loaded from: classes2.dex */
public interface ILocationEventLogWrapper {
    default void addExtLog(java.lang.Object logEvent) {
    }

    default void updateEventsLocationSize(int newSize) {
    }

    default void addLogToProviderEvent(java.lang.String provider, android.location.util.identity.CallerIdentity identity, java.lang.Object logEvent, long intervalMillis, boolean register) {
    }
}
