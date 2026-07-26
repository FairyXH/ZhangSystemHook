package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class DisplayWindowListenerController {
    android.os.RemoteCallbackList<android.view.IDisplayWindowListener> mDisplayListeners = new android.os.RemoteCallbackList<>();
    private final com.android.server.wm.WindowManagerService mService;

    DisplayWindowListenerController(com.android.server.wm.WindowManagerService service) {
        this.mService = service;
    }

    int[] registerListener(android.view.IDisplayWindowListener listener) {
        int[] array;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mDisplayListeners.register(listener);
                final android.util.IntArray displayIds = new android.util.IntArray();
                this.mService.mAtmService.mRootWindowContainer.forAllDisplays(new java.util.function.Consumer() { // from class: com.android.server.wm.DisplayWindowListenerController$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        displayIds.add(((com.android.server.wm.DisplayContent) obj).mDisplayId);
                    }
                });
                array = displayIds.toArray();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return array;
    }

    void unregisterListener(android.view.IDisplayWindowListener listener) {
        this.mDisplayListeners.unregister(listener);
    }

    void dispatchDisplayAdded(com.android.server.wm.DisplayContent display) {
        int count = this.mDisplayListeners.beginBroadcast();
        for (int i = 0; i < count; i++) {
            try {
                this.mDisplayListeners.getBroadcastItem(i).onDisplayAdded(display.mDisplayId);
            } catch (android.os.RemoteException e) {
            }
        }
        this.mDisplayListeners.finishBroadcast();
    }

    void dispatchDisplayChanged(com.android.server.wm.DisplayContent display, android.content.res.Configuration newConfig) {
        boolean isInHierarchy = false;
        for (int i = 0; i < display.getParent().getChildCount(); i++) {
            if (display.getParent().getChildAt(i) == display) {
                isInHierarchy = true;
            }
        }
        if (!isInHierarchy) {
            return;
        }
        int count = this.mDisplayListeners.beginBroadcast();
        for (int i2 = 0; i2 < count; i2++) {
            try {
                this.mDisplayListeners.getBroadcastItem(i2).onDisplayConfigurationChanged(display.getDisplayId(), newConfig);
            } catch (android.os.RemoteException e) {
            }
        }
        this.mDisplayListeners.finishBroadcast();
    }

    void dispatchDisplayRemoved(com.android.server.wm.DisplayContent display) {
        int count = this.mDisplayListeners.beginBroadcast();
        for (int i = 0; i < count; i++) {
            try {
                this.mDisplayListeners.getBroadcastItem(i).onDisplayRemoved(display.mDisplayId);
            } catch (android.os.RemoteException e) {
            }
        }
        this.mDisplayListeners.finishBroadcast();
    }

    void dispatchFixedRotationStarted(com.android.server.wm.DisplayContent display, int newRotation) {
        int count = this.mDisplayListeners.beginBroadcast();
        for (int i = 0; i < count; i++) {
            try {
                this.mDisplayListeners.getBroadcastItem(i).onFixedRotationStarted(display.mDisplayId, newRotation);
            } catch (android.os.RemoteException e) {
            }
        }
        this.mDisplayListeners.finishBroadcast();
    }

    void dispatchFixedRotationFinished(com.android.server.wm.DisplayContent display) {
        int count = this.mDisplayListeners.beginBroadcast();
        for (int i = 0; i < count; i++) {
            try {
                this.mDisplayListeners.getBroadcastItem(i).onFixedRotationFinished(display.mDisplayId);
            } catch (android.os.RemoteException e) {
            }
        }
        this.mDisplayListeners.finishBroadcast();
    }

    void dispatchKeepClearAreasChanged(com.android.server.wm.DisplayContent display, java.util.Set<android.graphics.Rect> restricted, java.util.Set<android.graphics.Rect> unrestricted) {
        int count = this.mDisplayListeners.beginBroadcast();
        for (int i = 0; i < count; i++) {
            try {
                this.mDisplayListeners.getBroadcastItem(i).onKeepClearAreasChanged(display.mDisplayId, new java.util.ArrayList(restricted), new java.util.ArrayList(unrestricted));
            } catch (android.os.RemoteException e) {
            }
        }
        this.mDisplayListeners.finishBroadcast();
    }
}
