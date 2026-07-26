package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public interface IOplusGoogleDozeRestrict extends android.common.IOplusCommonFeature {
    public static final com.android.server.IOplusGoogleDozeRestrict DEFAULT = new com.android.server.IOplusGoogleDozeRestrict() { // from class: com.android.server.IOplusGoogleDozeRestrict.1
    };
    public static final java.lang.String NAME = "IOplusGoogleDozeRestrict";

    default android.common.OplusFeatureList.OplusIndex index() {
        return android.common.OplusFeatureList.OplusIndex.IOplusGoogleDozeRestrict;
    }

    default android.common.IOplusCommonFeature getDefault() {
        return DEFAULT;
    }

    default void initArgs(android.content.Context context, android.os.Handler handler, com.android.server.DeviceIdleController deviceIdleInternal) {
    }

    default void updateWhitelistApps(android.util.ArrayMap<java.lang.String, java.lang.Integer> whitelistApps, boolean isSystem, boolean isExceptIdle) {
    }

    default void reportWhitelistForAms(android.app.ActivityManagerInternal localAms, android.util.SparseBooleanArray allAppIds, android.util.SparseBooleanArray allExceptIdleAppIds) {
    }

    default void restoreConfigFile(android.util.ArrayMap<java.lang.String, java.lang.Integer> whitelistApps, org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
    }

    default boolean interceptWhitelistOperation(android.content.pm.ApplicationInfo ai, java.lang.String name, boolean isSystem, boolean isExceptIdle, boolean add) {
        return false;
    }

    default void interceptWhitelistReset(boolean isExceptIdle, android.util.ArraySet<java.lang.String> list) {
    }

    default void updateWhitelist() {
    }
}
