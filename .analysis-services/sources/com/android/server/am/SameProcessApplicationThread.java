package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class SameProcessApplicationThread extends android.app.IApplicationThread.Default {
    private final android.os.Handler mHandler;
    private final android.app.IApplicationThread mWrapped;

    public SameProcessApplicationThread(android.app.IApplicationThread wrapped, android.os.Handler handler) {
        this.mWrapped = (android.app.IApplicationThread) java.util.Objects.requireNonNull(wrapped);
        this.mHandler = (android.os.Handler) java.util.Objects.requireNonNull(handler);
    }

    public void scheduleReceiver(final android.content.Intent intent, final android.content.pm.ActivityInfo info, final android.content.res.CompatibilityInfo compatInfo, final int resultCode, final java.lang.String data, final android.os.Bundle extras, final boolean ordered, final boolean assumeDelivered, final int sendingUser, final int processState, final int sendingUid, final java.lang.String sendingPackage) {
        com.android.server.OplusFgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.am.SameProcessApplicationThread$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleReceiver$0(intent, info, compatInfo, resultCode, data, extras, ordered, assumeDelivered, sendingUser, processState, sendingUid, sendingPackage);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleReceiver$0(android.content.Intent intent, android.content.pm.ActivityInfo info, android.content.res.CompatibilityInfo compatInfo, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean assumeDelivered, int sendingUser, int processState, int sendingUid, java.lang.String sendingPackage) {
        try {
            this.mWrapped.scheduleReceiver(intent, info, compatInfo, resultCode, data, extras, ordered, assumeDelivered, sendingUser, processState, sendingUid, sendingPackage);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public void scheduleRegisteredReceiver(final android.content.IIntentReceiver receiver, final android.content.Intent intent, final int resultCode, final java.lang.String data, final android.os.Bundle extras, final boolean ordered, final boolean sticky, final boolean assumeDelivered, final int sendingUser, final int processState, final int sendingUid, final java.lang.String sendingPackage) {
        com.android.server.OplusFgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.am.SameProcessApplicationThread$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleRegisteredReceiver$1(receiver, intent, resultCode, data, extras, ordered, sticky, assumeDelivered, sendingUser, processState, sendingUid, sendingPackage);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleRegisteredReceiver$1(android.content.IIntentReceiver receiver, android.content.Intent intent, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, boolean assumeDelivered, int sendingUser, int processState, int sendingUid, java.lang.String sendingPackage) {
        try {
            this.mWrapped.scheduleRegisteredReceiver(receiver, intent, resultCode, data, extras, ordered, sticky, assumeDelivered, sendingUser, processState, sendingUid, sendingPackage);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public void scheduleReceiverList(final java.util.List<android.app.ReceiverInfo> info) {
        com.android.server.OplusFgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.am.SameProcessApplicationThread$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleReceiverList$2(info);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleReceiverList$2(java.util.List info) {
        try {
            this.mWrapped.scheduleReceiverList(info);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException(e);
        }
    }

    public void schedulePing(final android.os.RemoteCallback pong) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.am.SameProcessApplicationThread$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$schedulePing$3(pong);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$schedulePing$3(android.os.RemoteCallback pong) {
        try {
            this.mWrapped.schedulePing(pong);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException(e);
        }
    }
}
