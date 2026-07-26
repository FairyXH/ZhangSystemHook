package com.android.server.power.batterysaver;

/* JADX INFO: loaded from: classes3.dex */
public interface IBatterySaverPolicyExt {
    default void init(android.content.Context context) {
    }

    default void onGetGpsMode(int policyLevel, com.android.server.power.batterysaver.BatterySaverPolicy.Policy policy, boolean isAutomotiveProjectionActive) {
    }

    default void onGetBatterySaverPolicy(int type, int policyLevel, com.android.server.power.batterysaver.BatterySaverPolicy.Policy policy, boolean isAutomotiveProjectionActive) {
    }

    default void onIsLaunchBoostDisabled(int policyLevel, com.android.server.power.batterysaver.BatterySaverPolicy.Policy policy) {
    }
}
