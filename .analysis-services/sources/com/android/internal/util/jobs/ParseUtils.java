package com.android.internal.util.jobs;

/* JADX INFO: loaded from: classes.dex */
public final class ParseUtils {
    private ParseUtils() {
    }

    public static int parseInt(java.lang.String value, int defValue) {
        return parseIntWithBase(value, 10, defValue);
    }

    public static int parseIntWithBase(java.lang.String value, int base, int defValue) {
        if (value == null) {
            return defValue;
        }
        try {
            return java.lang.Integer.parseInt(value, base);
        } catch (java.lang.NumberFormatException e) {
            return defValue;
        }
    }

    public static long parseLong(java.lang.String value, long defValue) {
        return parseLongWithBase(value, 10, defValue);
    }

    public static long parseLongWithBase(java.lang.String value, int base, long defValue) {
        if (value == null) {
            return defValue;
        }
        try {
            return java.lang.Long.parseLong(value, base);
        } catch (java.lang.NumberFormatException e) {
            return defValue;
        }
    }

    public static float parseFloat(java.lang.String value, float defValue) {
        if (value == null) {
            return defValue;
        }
        try {
            return java.lang.Float.parseFloat(value);
        } catch (java.lang.NumberFormatException e) {
            return defValue;
        }
    }

    public static double parseDouble(java.lang.String value, double defValue) {
        if (value == null) {
            return defValue;
        }
        try {
            return java.lang.Double.parseDouble(value);
        } catch (java.lang.NumberFormatException e) {
            return defValue;
        }
    }

    public static boolean parseBoolean(java.lang.String str, boolean z) {
        if ("true".equals(str)) {
            return true;
        }
        return ("false".equals(str) || parseInt(str, z ? 1 : 0) == 0) ? false : true;
    }
}
