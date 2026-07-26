package com.android.server.updates;

/* JADX INFO: loaded from: classes3.dex */
public class CertPinInstallReceiver extends com.android.server.updates.ConfigUpdateInstallReceiver {
    public CertPinInstallReceiver() {
        super("/data/misc/keychain/", "pins", "metadata/", "version");
    }
}
