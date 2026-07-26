package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class ActivityResult extends android.app.ResultInfo {
    final com.android.server.wm.ActivityRecord mFrom;

    public ActivityResult(com.android.server.wm.ActivityRecord from, java.lang.String resultWho, int requestCode, int resultCode, android.content.Intent data, android.os.IBinder callerToken) {
        super(resultWho, requestCode, resultCode, data, callerToken);
        this.mFrom = from;
    }
}
