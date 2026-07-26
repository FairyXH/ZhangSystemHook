package com.android.server.broadcastradio.hal1;

/* JADX INFO: loaded from: classes.dex */
final class Convert {
    private static final java.lang.String TAG = "BcRadio1Srv.Convert";

    Convert() {
    }

    static java.lang.String[][] stringMapToNative(java.util.Map<java.lang.String, java.lang.String> map) {
        if (map == null) {
            com.android.server.utils.Slogf.v(TAG, "map is null, returning zero-elements array");
            return (java.lang.String[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.String.class, 0, 0);
        }
        java.util.Set<java.util.Map.Entry<java.lang.String, java.lang.String>> entries = map.entrySet();
        int len = entries.size();
        java.lang.String[][] arr = (java.lang.String[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.String.class, len, 2);
        int i = 0;
        for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : entries) {
            arr[i][0] = entry.getKey();
            arr[i][1] = entry.getValue();
            i++;
        }
        com.android.server.utils.Slogf.v(TAG, "converted " + i + " element(s)");
        return arr;
    }
}
