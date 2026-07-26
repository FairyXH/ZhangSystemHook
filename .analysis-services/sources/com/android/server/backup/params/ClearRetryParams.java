package com.android.server.backup.params;

/* JADX INFO: loaded from: classes.dex */
public class ClearRetryParams {
    public java.lang.String packageName;
    public java.lang.String transportName;

    public ClearRetryParams(java.lang.String transportName, java.lang.String packageName) {
        this.transportName = transportName;
        this.packageName = packageName;
    }
}
