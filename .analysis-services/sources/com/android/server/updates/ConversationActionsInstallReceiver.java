package com.android.server.updates;

/* JADX INFO: loaded from: classes3.dex */
public class ConversationActionsInstallReceiver extends com.android.server.updates.ConfigUpdateInstallReceiver {
    public ConversationActionsInstallReceiver() {
        super("/data/misc/textclassifier/", "actions_suggestions.model", "metadata/actions_suggestions", "version");
    }

    @Override // com.android.server.updates.ConfigUpdateInstallReceiver
    protected boolean verifyVersion(int current, int alternative) {
        return true;
    }
}
