package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class BundleUtils {
    private BundleUtils() {
    }

    public static boolean isEmpty(android.os.Bundle in) {
        return in == null || in.size() == 0;
    }

    public static android.os.Bundle clone(android.os.Bundle in) {
        return in != null ? new android.os.Bundle(in) : new android.os.Bundle();
    }
}
