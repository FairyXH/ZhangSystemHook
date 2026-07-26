package com.android.server.vcn.util;

/* JADX INFO: loaded from: classes3.dex */
public class LogUtils {
    public static java.lang.String getHashedSubscriptionGroup(android.os.ParcelUuid uuid) {
        if (uuid == null) {
            return null;
        }
        return com.android.internal.util.HexDump.toHexString(uuid.hashCode());
    }
}
