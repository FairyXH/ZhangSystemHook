package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public class SystemScreenInteractiveHelper extends com.android.server.location.injector.ScreenInteractiveHelper {
    private final android.content.Context mContext;
    private volatile boolean mIsInteractive;
    private boolean mReady;

    public SystemScreenInteractiveHelper(android.content.Context context) {
        this.mContext = context;
    }

    public void onSystemReady() {
        if (this.mReady) {
            return;
        }
        android.content.IntentFilter screenIntentFilter = new android.content.IntentFilter();
        screenIntentFilter.addAction("android.intent.action.SCREEN_OFF");
        screenIntentFilter.addAction("android.intent.action.SCREEN_ON");
        this.mContext.registerReceiverAsUser(new android.content.BroadcastReceiver() { // from class: com.android.server.location.injector.SystemScreenInteractiveHelper.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                boolean interactive;
                if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
                    interactive = true;
                } else if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                    interactive = false;
                } else {
                    return;
                }
                com.android.server.location.injector.SystemScreenInteractiveHelper.this.onScreenInteractiveChanged(interactive);
            }
        }, android.os.UserHandle.ALL, screenIntentFilter, null, ((com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, this.mContext)).getHandler(0));
        this.mReady = true;
    }

    void onScreenInteractiveChanged(boolean interactive) {
        if (interactive == this.mIsInteractive) {
            return;
        }
        this.mIsInteractive = interactive;
        notifyScreenInteractiveChanged(interactive);
    }

    @Override // com.android.server.location.injector.ScreenInteractiveHelper
    public boolean isInteractive() {
        com.android.internal.util.Preconditions.checkState(this.mReady);
        return this.mIsInteractive;
    }
}
