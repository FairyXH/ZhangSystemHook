package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ShellRoot {
    private static final java.lang.String TAG = "ShellRoot";
    private android.view.IWindow mAccessibilityWindow;
    private android.os.IBinder.DeathRecipient mAccessibilityWindowDeath;
    private android.view.IWindow mClient;
    private final android.os.IBinder.DeathRecipient mDeathRecipient;
    private final com.android.server.wm.DisplayContent mDisplayContent;
    private final int mShellRootLayer;
    private android.view.SurfaceControl mSurfaceControl;
    private com.android.server.wm.WindowToken mToken;
    private int mWindowType;

    ShellRoot(android.view.IWindow client, com.android.server.wm.DisplayContent dc, final int shellRootLayer) {
        this.mSurfaceControl = null;
        this.mDisplayContent = dc;
        this.mShellRootLayer = shellRootLayer;
        this.mDeathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.wm.ShellRoot$$ExternalSyntheticLambda0
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                this.f$0.lambda$new$0(shellRootLayer);
            }
        };
        try {
            client.asBinder().linkToDeath(this.mDeathRecipient, 0);
            this.mClient = client;
            switch (shellRootLayer) {
                case 0:
                    this.mWindowType = 2034;
                    break;
                case 1:
                    this.mWindowType = 2038;
                    break;
                default:
                    throw new java.lang.IllegalArgumentException(shellRootLayer + " is not an acceptable shell root layer.");
            }
            this.mToken = new com.android.server.wm.WindowToken.Builder(dc.mWmService, client.asBinder(), this.mWindowType).setDisplayContent(dc).setPersistOnEmpty(true).setOwnerCanManageAppTokens(true).build();
            this.mSurfaceControl = this.mToken.makeChildSurface(null).setContainerLayer().setName("Shell Root Leash " + dc.getDisplayId()).setCallsite(TAG).build();
            this.mToken.getPendingTransaction().show(this.mSurfaceControl);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Unable to add shell root layer " + shellRootLayer + " on display " + dc.getDisplayId(), e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int shellRootLayer) {
        this.mDisplayContent.removeShellRoot(shellRootLayer);
    }

    int getWindowType() {
        return this.mWindowType;
    }

    void clear() {
        if (this.mClient != null) {
            this.mClient.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
            this.mClient = null;
        }
        if (this.mToken != null) {
            this.mToken.removeImmediately();
            this.mToken = null;
        }
    }

    android.view.SurfaceControl getSurfaceControl() {
        return this.mSurfaceControl;
    }

    android.view.IWindow getClient() {
        return this.mClient;
    }

    void startAnimation(android.view.animation.Animation anim) {
        if (this.mToken.windowType != 2034) {
            return;
        }
        android.view.DisplayInfo displayInfo = this.mToken.getFixedRotationTransformDisplayInfo();
        if (displayInfo == null) {
            displayInfo = this.mDisplayContent.getDisplayInfo();
        }
        anim.initialize(displayInfo.logicalWidth, displayInfo.logicalHeight, displayInfo.appWidth, displayInfo.appHeight);
        anim.restrictDuration(10000L);
        anim.scaleCurrentDuration(this.mDisplayContent.mWmService.getWindowAnimationScaleLocked());
        com.android.server.wm.AnimationAdapter adapter = new com.android.server.wm.LocalAnimationAdapter(new com.android.server.wm.WindowAnimationSpec(anim, new android.graphics.Point(0, 0), false, 0.0f), this.mDisplayContent.mWmService.mSurfaceAnimationRunner);
        this.mToken.startAnimation(this.mToken.getPendingTransaction(), adapter, false, 16);
    }

    android.os.IBinder getAccessibilityWindowToken() {
        if (this.mAccessibilityWindow != null) {
            return this.mAccessibilityWindow.asBinder();
        }
        return null;
    }

    void setAccessibilityWindow(android.view.IWindow window) {
        if (this.mAccessibilityWindow != null) {
            this.mAccessibilityWindow.asBinder().unlinkToDeath(this.mAccessibilityWindowDeath, 0);
        }
        this.mAccessibilityWindow = window;
        if (this.mAccessibilityWindow != null) {
            try {
                this.mAccessibilityWindowDeath = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.wm.ShellRoot$$ExternalSyntheticLambda1
                    @Override // android.os.IBinder.DeathRecipient
                    public final void binderDied() {
                        this.f$0.lambda$setAccessibilityWindow$1();
                    }
                };
                this.mAccessibilityWindow.asBinder().linkToDeath(this.mAccessibilityWindowDeath, 0);
            } catch (android.os.RemoteException e) {
                this.mAccessibilityWindow = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setAccessibilityWindow$1() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mDisplayContent.mWmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mAccessibilityWindow = null;
                setAccessibilityWindow(null);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }
}
