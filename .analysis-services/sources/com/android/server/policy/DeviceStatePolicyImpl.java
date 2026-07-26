package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public final class DeviceStatePolicyImpl extends com.android.server.devicestate.DeviceStatePolicy {
    private final com.android.server.devicestate.DeviceStateProvider mProvider;

    public DeviceStatePolicyImpl(android.content.Context context) {
        super(context);
        this.mProvider = com.android.server.policy.DeviceStateProviderImpl.create(this.mContext);
    }

    @Override // com.android.server.devicestate.DeviceStatePolicy
    public com.android.server.devicestate.DeviceStateProvider getDeviceStateProvider() {
        return this.mProvider;
    }

    @Override // com.android.server.devicestate.DeviceStatePolicy
    public void configureDeviceForState(int state, java.lang.Runnable onComplete) {
        onComplete.run();
    }

    @Override // android.util.Dumpable
    public void dump(java.io.PrintWriter writer, java.lang.String[] args) {
        this.mProvider.dump(writer, args);
    }
}
