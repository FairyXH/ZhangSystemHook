package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class RotationWatcherController {
    private volatile boolean mHasProposedRotationListeners;
    private final com.android.server.wm.WindowManagerService mService;
    private final java.util.ArrayList<com.android.server.wm.RotationWatcherController.DisplayRotationWatcher> mDisplayRotationWatchers = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.wm.RotationWatcherController.ProposedRotationListener> mProposedRotationListeners = new java.util.ArrayList<>();

    RotationWatcherController(com.android.server.wm.WindowManagerService wms) {
        this.mService = wms;
    }

    void registerDisplayRotationWatcher(android.view.IRotationWatcher watcher, int displayId) {
        android.os.IBinder watcherBinder = watcher.asBinder();
        for (int i = this.mDisplayRotationWatchers.size() - 1; i >= 0; i--) {
            if (watcherBinder == this.mDisplayRotationWatchers.get(i).mWatcher.asBinder()) {
                throw new java.lang.IllegalArgumentException("Registering existed rotation watcher");
            }
        }
        register(watcherBinder, new com.android.server.wm.RotationWatcherController.DisplayRotationWatcher(this.mService, watcher, displayId), this.mDisplayRotationWatchers);
    }

    void registerProposedRotationListener(android.view.IRotationWatcher listener, android.os.IBinder contextToken) {
        android.os.IBinder listenerBinder = listener.asBinder();
        for (int i = this.mProposedRotationListeners.size() - 1; i >= 0; i--) {
            com.android.server.wm.RotationWatcherController.ProposedRotationListener watcher = this.mProposedRotationListeners.get(i);
            if (contextToken == watcher.mToken || listenerBinder == watcher.mWatcher.asBinder()) {
                android.util.Slog.w("WindowManager", "Register rotation listener to a registered token, uid=" + android.os.Binder.getCallingUid());
                return;
            }
        }
        register(listenerBinder, new com.android.server.wm.RotationWatcherController.ProposedRotationListener(this.mService, listener, contextToken), this.mProposedRotationListeners);
        this.mHasProposedRotationListeners = !this.mProposedRotationListeners.isEmpty();
    }

    private static <T extends com.android.server.wm.RotationWatcherController.RotationWatcher> void register(android.os.IBinder watcherBinder, T watcher, java.util.ArrayList<T> watcherList) {
        try {
            watcherBinder.linkToDeath(watcher, 0);
            watcherList.add(watcher);
        } catch (android.os.RemoteException e) {
        }
    }

    private static <T extends com.android.server.wm.RotationWatcherController.RotationWatcher> boolean unregister(android.view.IRotationWatcher watcher, java.util.ArrayList<T> watcherList) {
        android.os.IBinder watcherBinder = watcher.asBinder();
        for (int i = watcherList.size() - 1; i >= 0; i--) {
            com.android.server.wm.RotationWatcherController.RotationWatcher rotationWatcher = watcherList.get(i);
            if (watcherBinder == rotationWatcher.mWatcher.asBinder()) {
                watcherList.remove(i);
                rotationWatcher.unlinkToDeath();
                return true;
            }
        }
        return false;
    }

    void removeRotationWatcher(android.view.IRotationWatcher watcher) {
        boolean removed = unregister(watcher, this.mProposedRotationListeners);
        if (removed) {
            this.mHasProposedRotationListeners = !this.mProposedRotationListeners.isEmpty();
        } else {
            unregister(watcher, this.mDisplayRotationWatchers);
        }
    }

    void dispatchDisplayRotationChange(int displayId, int rotation) {
        for (int i = this.mDisplayRotationWatchers.size() - 1; i >= 0; i--) {
            com.android.server.wm.RotationWatcherController.DisplayRotationWatcher rotationWatcher = this.mDisplayRotationWatchers.get(i);
            if (rotationWatcher.mDisplayId == displayId) {
                rotationWatcher.notifyRotation(rotation);
            }
        }
    }

    void dispatchProposedRotation(com.android.server.wm.DisplayContent dc, int rotation) {
        for (int i = this.mProposedRotationListeners.size() - 1; i >= 0; i--) {
            com.android.server.wm.RotationWatcherController.ProposedRotationListener listener = this.mProposedRotationListeners.get(i);
            com.android.server.wm.WindowContainer<?> wc = getAssociatedWindowContainer(listener.mToken);
            if (wc != null) {
                if (wc.mDisplayContent == dc) {
                    listener.notifyRotation(rotation);
                }
            } else {
                this.mProposedRotationListeners.remove(i);
                this.mHasProposedRotationListeners = !this.mProposedRotationListeners.isEmpty();
                listener.unlinkToDeath();
            }
        }
    }

    boolean hasProposedRotationListeners() {
        return this.mHasProposedRotationListeners;
    }

    com.android.server.wm.WindowContainer<?> getAssociatedWindowContainer(android.os.IBinder contextToken) {
        com.android.server.wm.WindowContainer<?> wc = com.android.server.wm.ActivityRecord.forTokenLocked(contextToken);
        if (wc != null) {
            return wc;
        }
        return this.mService.mWindowContextListenerController.getContainer(contextToken);
    }

    void dump(java.io.PrintWriter pw) {
        if (!this.mDisplayRotationWatchers.isEmpty()) {
            pw.print("  mDisplayRotationWatchers: [");
            for (int i = this.mDisplayRotationWatchers.size() - 1; i >= 0; i--) {
                pw.print(' ');
                com.android.server.wm.RotationWatcherController.DisplayRotationWatcher watcher = this.mDisplayRotationWatchers.get(i);
                pw.print(watcher.mOwnerUid);
                pw.print("->");
                pw.print(watcher.mDisplayId);
            }
            pw.println(']');
        }
        if (!this.mProposedRotationListeners.isEmpty()) {
            pw.print("  mProposedRotationListeners: [");
            for (int i2 = this.mProposedRotationListeners.size() - 1; i2 >= 0; i2--) {
                pw.print(' ');
                com.android.server.wm.RotationWatcherController.ProposedRotationListener listener = this.mProposedRotationListeners.get(i2);
                pw.print(listener.mOwnerUid);
                pw.print("->");
                pw.print(getAssociatedWindowContainer(listener.mToken));
            }
            pw.println(']');
        }
    }

    private static class DisplayRotationWatcher extends com.android.server.wm.RotationWatcherController.RotationWatcher {
        final int mDisplayId;

        DisplayRotationWatcher(com.android.server.wm.WindowManagerService wms, android.view.IRotationWatcher watcher, int displayId) {
            super(wms, watcher);
            this.mDisplayId = displayId;
        }
    }

    private static class ProposedRotationListener extends com.android.server.wm.RotationWatcherController.RotationWatcher {
        final android.os.IBinder mToken;

        ProposedRotationListener(com.android.server.wm.WindowManagerService wms, android.view.IRotationWatcher watcher, android.os.IBinder token) {
            super(wms, watcher);
            this.mToken = token;
        }
    }

    private static class RotationWatcher implements android.os.IBinder.DeathRecipient {
        final int mOwnerUid = android.os.Binder.getCallingUid();
        final android.view.IRotationWatcher mWatcher;
        final com.android.server.wm.WindowManagerService mWms;

        RotationWatcher(com.android.server.wm.WindowManagerService wms, android.view.IRotationWatcher watcher) {
            this.mWms = wms;
            this.mWatcher = watcher;
        }

        void notifyRotation(int rotation) {
            try {
                this.mWatcher.onRotationChanged(rotation);
            } catch (android.os.RemoteException e) {
            }
        }

        void unlinkToDeath() {
            this.mWatcher.asBinder().unlinkToDeath(this, 0);
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            this.mWms.removeRotationWatcher(this.mWatcher);
        }
    }
}
