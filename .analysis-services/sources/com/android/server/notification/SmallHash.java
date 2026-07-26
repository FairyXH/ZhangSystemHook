package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class SmallHash {
    public static final int MAX_HASH = 8192;

    public static int hash(java.lang.String in) {
        return hash(java.util.Objects.hashCode(in));
    }

    public static int hash(int in) {
        return java.lang.Math.floorMod(in, 8192);
    }
}
