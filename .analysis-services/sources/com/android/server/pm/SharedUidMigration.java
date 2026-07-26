package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class SharedUidMigration {
    public static final int BEST_EFFORT = 2;
    private static final int DEFAULT = 1;
    public static final int LIVE_TRANSITION = 4;
    public static final int NEW_INSTALL_ONLY = 1;
    public static final java.lang.String PROPERTY_KEY = "persist.debug.pm.shared_uid_migration_strategy";
    public static final int TRANSITION_AT_BOOT = 3;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Strategy {
    }

    public static boolean isDisabled() {
        return false;
    }

    public static int getCurrentStrategy() {
        int s;
        if (android.os.Build.IS_USERDEBUG && (s = android.os.SystemProperties.getInt(PROPERTY_KEY, 1)) <= 2 && s >= 1) {
            return s;
        }
        return 1;
    }

    public static boolean applyStrategy(int strategy) {
        return !isDisabled() && getCurrentStrategy() >= strategy;
    }

    private SharedUidMigration() {
    }
}
