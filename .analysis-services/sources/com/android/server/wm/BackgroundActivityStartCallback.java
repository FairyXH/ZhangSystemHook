package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface BackgroundActivityStartCallback {
    boolean canCloseSystemDialogs(java.util.Collection<android.os.IBinder> collection, int i);

    boolean isActivityStartAllowed(java.util.Collection<android.os.IBinder> collection, int i, java.lang.String str);
}
