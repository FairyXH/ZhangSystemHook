package com.android.server.updates;

/* JADX INFO: loaded from: classes3.dex */
public class SmartSelectionInstallReceiver extends com.android.server.updates.ConfigUpdateInstallReceiver {
    public SmartSelectionInstallReceiver() {
        super("/data/misc/textclassifier/", "textclassifier.model", "metadata/classification", "version");
    }

    @Override // com.android.server.updates.ConfigUpdateInstallReceiver
    protected boolean verifyVersion(int current, int alternative) {
        return true;
    }
}
