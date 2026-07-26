package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class HbtUtilSocExtImpl implements com.android.server.pm.IHbtUtilSocExt {
    static final java.lang.String TAG = "HbtUtilSocExtImpl";
    private static volatile com.android.server.pm.HbtUtilSocExtImpl sInstance;

    private HbtUtilSocExtImpl(java.lang.Object base) {
    }

    public static com.android.server.pm.HbtUtilSocExtImpl getInstance(java.lang.Object base) {
        if (sInstance == null) {
            synchronized (com.android.server.pm.HbtUtilSocExtImpl.class) {
                if (sInstance == null) {
                    sInstance = new com.android.server.pm.HbtUtilSocExtImpl(base);
                }
            }
        }
        return sInstance;
    }
}
