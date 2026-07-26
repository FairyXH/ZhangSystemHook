package com.android.server.signedconfig;

/* JADX INFO: loaded from: classes3.dex */
public class SignedConfigEvent {
    public int type = 0;
    public int status = 0;
    public int version = 0;
    public java.lang.String fromPackage = null;
    public int verifiedWith = 0;

    public void send() {
        com.android.internal.util.FrameworkStatsLog.write(123, this.type, this.status, this.version, this.fromPackage, this.verifiedWith);
    }
}
