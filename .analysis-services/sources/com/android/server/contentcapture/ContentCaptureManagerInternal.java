package com.android.server.contentcapture;

/* JADX INFO: loaded from: classes.dex */
public abstract class ContentCaptureManagerInternal {
    public abstract android.content.ContentCaptureOptions getOptionsForPackage(int i, java.lang.String str);

    public abstract boolean isContentCaptureServiceForUser(int i, int i2);

    public abstract void notifyActivityEvent(int i, android.content.ComponentName componentName, int i2, android.app.assist.ActivityId activityId);

    public abstract boolean sendActivityAssistData(int i, android.os.IBinder iBinder, android.os.Bundle bundle);

    public abstract boolean sendActivityStartAssistData(int i, android.os.IBinder iBinder, android.content.Intent intent);
}
