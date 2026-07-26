package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public interface ISessionWrapper {
    default void save() {
    }

    default void autofill(int requestId, int datasetIndex, android.service.autofill.Dataset dataset, boolean generateEvent, int uiType) {
    }

    default com.android.server.autofill.ISessionExt getSessionExt() {
        return null;
    }
}
