package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class FactoryResetter {
    private static final java.util.concurrent.atomic.AtomicBoolean sFactoryResetting = new java.util.concurrent.atomic.AtomicBoolean(false);

    public static boolean isFactoryResetting() {
        return sFactoryResetting.get();
    }

    @java.lang.Deprecated
    public static void setFactoryResetting(android.content.Context context) {
        com.android.internal.util.Preconditions.checkCallAuthorization(context.checkCallingOrSelfPermission("android.permission.MASTER_CLEAR") == 0);
        sFactoryResetting.set(true);
    }

    private FactoryResetter() {
        throw new java.lang.UnsupportedOperationException("Provides only static methods");
    }
}
