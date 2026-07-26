package com.android.server.updates;

/* JADX INFO: loaded from: classes3.dex */
public class LangIdInstallReceiver extends com.android.server.updates.ConfigUpdateInstallReceiver {
    public LangIdInstallReceiver() {
        super("/data/misc/textclassifier/", "lang_id.model", "metadata/lang_id", "version");
    }

    @Override // com.android.server.updates.ConfigUpdateInstallReceiver
    protected boolean verifyVersion(int current, int alternative) {
        return true;
    }
}
