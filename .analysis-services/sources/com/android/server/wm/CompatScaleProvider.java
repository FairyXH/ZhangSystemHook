package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface CompatScaleProvider {
    public static final int COMPAT_SCALE_MODE_GAME = 1;
    public static final int COMPAT_SCALE_MODE_PRODUCT = 2;
    public static final int COMPAT_SCALE_MODE_SYSTEM_FIRST = 0;
    public static final int COMPAT_SCALE_MODE_SYSTEM_LAST = 2;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface CompatScaleModeOrderId {
    }

    android.content.res.CompatibilityInfo.CompatScale getCompatScale(java.lang.String str, int i);

    static boolean isValidOrderId(int id) {
        return id >= 0 && id <= 2;
    }
}
