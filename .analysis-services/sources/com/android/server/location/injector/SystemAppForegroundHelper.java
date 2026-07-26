package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public class SystemAppForegroundHelper extends com.android.server.location.injector.AppForegroundHelper {
    private android.app.ActivityManager mActivityManager;
    private final android.content.Context mContext;

    public SystemAppForegroundHelper(android.content.Context context) {
        this.mContext = context;
    }

    public void onSystemReady() {
        if (this.mActivityManager != null) {
            return;
        }
        this.mActivityManager = (android.app.ActivityManager) java.util.Objects.requireNonNull((android.app.ActivityManager) this.mContext.getSystemService(android.app.ActivityManager.class));
        this.mActivityManager.addOnUidImportanceListener(new android.app.ActivityManager.OnUidImportanceListener() { // from class: com.android.server.location.injector.SystemAppForegroundHelper$$ExternalSyntheticLambda0
            public final void onUidImportance(int i, int i2) {
                this.f$0.onAppForegroundChanged(i, i2);
            }
        }, 125);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAppForegroundChanged(final int uid, int importance) {
        final boolean foreground = isForeground(importance);
        com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.location.injector.SystemAppForegroundHelper$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$onAppForegroundChanged$0(uid, foreground);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onAppForegroundChanged$0(int uid, boolean foreground) {
        notifyAppForeground(uid, foreground);
    }

    @Override // com.android.server.location.injector.AppForegroundHelper
    public boolean isAppForeground(int uid) {
        com.android.internal.util.Preconditions.checkState(this.mActivityManager != null);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return isForeground(this.mActivityManager.getUidImportance(uid));
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }
}
