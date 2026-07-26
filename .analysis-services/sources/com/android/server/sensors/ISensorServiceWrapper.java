package com.android.server.sensors;

/* JADX INFO: loaded from: classes3.dex */
public interface ISensorServiceWrapper {
    default com.android.server.sensors.ISensorServiceExt getExtImpl() {
        return new com.android.server.sensors.ISensorServiceExt() { // from class: com.android.server.sensors.ISensorServiceWrapper.1
        };
    }

    default java.lang.String[] getProximityOwnerInternal() {
        return null;
    }

    default long[] getProximityEventsInternal() {
        return null;
    }

    default void cleanUpProxEventsInternal() {
    }

    default java.lang.String[] getUltrasonicProximityUsage() {
        return null;
    }
}
