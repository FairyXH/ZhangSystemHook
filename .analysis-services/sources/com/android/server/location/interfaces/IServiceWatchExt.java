package com.android.server.location.interfaces;

/* JADX INFO: loaded from: classes2.dex */
public interface IServiceWatchExt extends com.android.server.location.common.IOplusCommonFeature {
    public static final com.android.server.location.interfaces.IServiceWatchExt DEFAULT = new com.android.server.location.interfaces.IServiceWatchExt() { // from class: com.android.server.location.interfaces.IServiceWatchExt.1
    };
    public static final java.lang.String Name = "IServiceWatchExt";

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.OplusLbsFeatureList.OplusIndex index() {
        return com.android.server.location.common.OplusLbsFeatureList.OplusIndex.IServiceWatchExt;
    }

    @Override // com.android.server.location.common.IOplusCommonFeature
    default com.android.server.location.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void storeConnectionWrapper(com.android.server.servicewatcher.IServiceWatcherExt wrapper, android.content.ComponentName component, android.os.IBinder binder, android.os.Looper looper) {
    }

    default void signalConnectionWrapper() {
    }
}
