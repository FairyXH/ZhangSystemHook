package com.android.server.notification;

/* JADX INFO: loaded from: classes2.dex */
public class PropConfig {
    private static final java.lang.String UNSET = "UNSET";

    public static int getInt(android.content.Context context, java.lang.String propName, int resId) {
        return android.os.SystemProperties.getInt(propName, context.getResources().getInteger(resId));
    }

    public static java.lang.String[] getStringArray(android.content.Context context, java.lang.String propName, int resId) {
        java.lang.String prop = android.os.SystemProperties.get(propName, UNSET);
        return !UNSET.equals(prop) ? prop.split(",") : context.getResources().getStringArray(resId);
    }
}
