package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ScreenRecordingCallbackController {
    private com.android.server.wm.WindowContainer<com.android.server.wm.WindowContainer> mRecordedWC;
    private final com.android.server.wm.WindowManagerService mWms;
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.wm.ScreenRecordingCallbackController.Callback> mCallbacks = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.Integer, java.lang.Boolean> mLastInvokedStateByUid = new android.util.ArrayMap<>();
    private boolean mWatcherCallbackRegistered = false;

    private final class Callback implements android.os.IBinder.DeathRecipient {
        android.window.IScreenRecordingCallback mCallback;
        int mUid;

        Callback(android.window.IScreenRecordingCallback callback, int uid) {
            this.mCallback = callback;
            this.mUid = uid;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.wm.ScreenRecordingCallbackController.this.unregister(this.mCallback);
        }
    }

    private final class MediaProjectionWatcherCallback extends android.media.projection.IMediaProjectionWatcherCallback.Stub {
        private MediaProjectionWatcherCallback() {
        }

        public void onStart(android.media.projection.MediaProjectionInfo mediaProjectionInfo) {
            com.android.server.wm.ScreenRecordingCallbackController.this.onScreenRecordingStart(mediaProjectionInfo);
        }

        public void onStop(android.media.projection.MediaProjectionInfo mediaProjectionInfo) {
            com.android.server.wm.ScreenRecordingCallbackController.this.onScreenRecordingStop();
        }

        public void onRecordingSessionSet(android.media.projection.MediaProjectionInfo mediaProjectionInfo, android.view.ContentRecordingSession contentRecordingSession) {
        }
    }

    ScreenRecordingCallbackController(com.android.server.wm.WindowManagerService wms) {
        this.mWms = wms;
    }

    private void setRecordedWindowContainer(final android.media.projection.MediaProjectionInfo mediaProjectionInfo) {
        if (mediaProjectionInfo.getLaunchCookie() == null) {
            this.mRecordedWC = this.mWms.mRoot.getDefaultDisplay();
        } else {
            this.mRecordedWC = this.mWms.mRoot.getActivity(new java.util.function.Predicate() { // from class: com.android.server.wm.ScreenRecordingCallbackController$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.wm.ScreenRecordingCallbackController.lambda$setRecordedWindowContainer$0(mediaProjectionInfo, (com.android.server.wm.ActivityRecord) obj);
                }
            }).getTask();
        }
    }

    static /* synthetic */ boolean lambda$setRecordedWindowContainer$0(android.media.projection.MediaProjectionInfo mediaProjectionInfo, com.android.server.wm.ActivityRecord activity) {
        return activity.mLaunchCookie == mediaProjectionInfo.getLaunchCookie().binder;
    }

    private void ensureMediaProjectionWatcherCallbackRegistered() {
        if (this.mWatcherCallbackRegistered) {
            return;
        }
        android.os.IBinder binder = android.os.ServiceManager.getService("media_projection");
        android.media.projection.IMediaProjectionManager mediaProjectionManager = android.media.projection.IMediaProjectionManager.Stub.asInterface(binder);
        long identityToken = android.os.Binder.clearCallingIdentity();
        android.media.projection.MediaProjectionInfo mediaProjectionInfo = null;
        try {
            try {
                mediaProjectionInfo = mediaProjectionManager.addCallback(new com.android.server.wm.ScreenRecordingCallbackController.MediaProjectionWatcherCallback());
                this.mWatcherCallbackRegistered = true;
            } catch (android.os.RemoteException e) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_ERROR_enabled[4]) {
                    com.android.internal.protolog.ProtoLogImpl_209941506.e(com.android.internal.protolog.ProtoLogGroup.WM_ERROR, 4666728330189027178L, 0, "Failed to register MediaProjectionWatcherCallback", null);
                }
            }
            if (mediaProjectionInfo != null) {
                setRecordedWindowContainer(mediaProjectionInfo);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identityToken);
        }
    }

    boolean register(android.window.IScreenRecordingCallback callback) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWms.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                ensureMediaProjectionWatcherCallbackRegistered();
                android.os.IBinder binder = callback.asBinder();
                int uid = android.os.Binder.getCallingUid();
                if (this.mCallbacks.containsKey(binder)) {
                    boolean zBooleanValue = this.mLastInvokedStateByUid.get(java.lang.Integer.valueOf(uid)).booleanValue();
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return zBooleanValue;
                }
                com.android.server.wm.ScreenRecordingCallbackController.Callback callbackInfo = new com.android.server.wm.ScreenRecordingCallbackController.Callback(callback, uid);
                try {
                    binder.linkToDeath(callbackInfo, 0);
                    boolean uidInRecording = uidHasRecordedActivity(callbackInfo.mUid);
                    this.mLastInvokedStateByUid.put(java.lang.Integer.valueOf(callbackInfo.mUid), java.lang.Boolean.valueOf(uidInRecording));
                    this.mCallbacks.put(binder, callbackInfo);
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return uidInRecording;
                } catch (android.os.RemoteException e) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    void unregister(android.window.IScreenRecordingCallback callback) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWms.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                android.os.IBinder binder = callback.asBinder();
                com.android.server.wm.ScreenRecordingCallbackController.Callback callbackInfo = this.mCallbacks.remove(binder);
                if (callbackInfo == null) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return;
                }
                binder.unlinkToDeath(callbackInfo, 0);
                boolean uidHasCallback = false;
                int i = 0;
                while (true) {
                    if (i >= this.mCallbacks.size()) {
                        break;
                    }
                    if (this.mCallbacks.valueAt(i).mUid != callbackInfo.mUid) {
                        i++;
                    } else {
                        uidHasCallback = true;
                        break;
                    }
                }
                if (!uidHasCallback) {
                    this.mLastInvokedStateByUid.remove(java.lang.Integer.valueOf(callbackInfo.mUid));
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onScreenRecordingStart(android.media.projection.MediaProjectionInfo mediaProjectionInfo) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWms.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                setRecordedWindowContainer(mediaProjectionInfo);
                dispatchCallbacks(getRecordedUids(), true);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onScreenRecordingStop() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWms.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                dispatchCallbacks(getRecordedUids(), false);
                this.mRecordedWC = null;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void onProcessActivityVisibilityChanged(int uid, boolean processVisible) {
        if (this.mRecordedWC == null || !this.mLastInvokedStateByUid.containsKey(java.lang.Integer.valueOf(uid)) || processVisible == this.mLastInvokedStateByUid.get(java.lang.Integer.valueOf(uid)).booleanValue()) {
            return;
        }
        boolean uidInRecording = uidHasRecordedActivity(uid);
        if (!processVisible || uidInRecording) {
            if (!processVisible && uidInRecording) {
                return;
            }
            android.util.ArraySet<java.lang.Integer> uidSet = new android.util.ArraySet<>();
            uidSet.add(java.lang.Integer.valueOf(uid));
            dispatchCallbacks(uidSet, processVisible);
        }
    }

    private boolean uidHasRecordedActivity(final int uid) {
        if (this.mRecordedWC == null) {
            return false;
        }
        final boolean[] hasRecordedActivity = {false};
        this.mRecordedWC.forAllActivities(new java.util.function.Predicate() { // from class: com.android.server.wm.ScreenRecordingCallbackController$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.wm.ScreenRecordingCallbackController.lambda$uidHasRecordedActivity$1(uid, hasRecordedActivity, (com.android.server.wm.ActivityRecord) obj);
            }
        }, true);
        return hasRecordedActivity[0];
    }

    static /* synthetic */ boolean lambda$uidHasRecordedActivity$1(int uid, boolean[] hasRecordedActivity, com.android.server.wm.ActivityRecord activityRecord) {
        if (activityRecord.getUid() != uid || !activityRecord.isVisibleRequested()) {
            return false;
        }
        hasRecordedActivity[0] = true;
        return true;
    }

    private android.util.ArraySet<java.lang.Integer> getRecordedUids() {
        final android.util.ArraySet<java.lang.Integer> result = new android.util.ArraySet<>();
        if (this.mRecordedWC == null) {
            return result;
        }
        this.mRecordedWC.forAllActivities(new java.util.function.Consumer() { // from class: com.android.server.wm.ScreenRecordingCallbackController$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$getRecordedUids$2(result, (com.android.server.wm.ActivityRecord) obj);
            }
        }, true);
        return result;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getRecordedUids$2(android.util.ArraySet result, com.android.server.wm.ActivityRecord activityRecord) {
        if (activityRecord.isVisibleRequested() && this.mLastInvokedStateByUid.containsKey(java.lang.Integer.valueOf(activityRecord.getUid()))) {
            result.add(java.lang.Integer.valueOf(activityRecord.getUid()));
        }
    }

    private void dispatchCallbacks(android.util.ArraySet<java.lang.Integer> uids, final boolean visibleInScreenRecording) {
        if (uids.isEmpty()) {
            return;
        }
        for (int i = 0; i < uids.size(); i++) {
            this.mLastInvokedStateByUid.put(uids.valueAt(i), java.lang.Boolean.valueOf(visibleInScreenRecording));
        }
        final java.util.ArrayList<android.window.IScreenRecordingCallback> callbacks = new java.util.ArrayList<>();
        for (int i2 = 0; i2 < this.mCallbacks.size(); i2++) {
            if (uids.contains(java.lang.Integer.valueOf(this.mCallbacks.valueAt(i2).mUid))) {
                callbacks.add(this.mCallbacks.valueAt(i2).mCallback);
            }
        }
        this.mWms.mH.post(new java.lang.Runnable() { // from class: com.android.server.wm.ScreenRecordingCallbackController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.wm.ScreenRecordingCallbackController.lambda$dispatchCallbacks$3(callbacks, visibleInScreenRecording);
            }
        });
    }

    static /* synthetic */ void lambda$dispatchCallbacks$3(java.util.ArrayList callbacks, boolean visibleInScreenRecording) {
        for (int i = 0; i < callbacks.size(); i++) {
            try {
                ((android.window.IScreenRecordingCallback) callbacks.get(i)).onScreenRecordingStateChanged(visibleInScreenRecording);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    void dump(java.io.PrintWriter pw) {
        pw.format("ScreenRecordingCallbackController:\n", new java.lang.Object[0]);
        pw.format("  Registered callbacks:\n", new java.lang.Object[0]);
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            pw.format("    callback=%s uid=%s\n", this.mCallbacks.keyAt(i), java.lang.Integer.valueOf(this.mCallbacks.valueAt(i).mUid));
        }
        pw.format("  Last invoked states:\n", new java.lang.Object[0]);
        for (int i2 = 0; i2 < this.mLastInvokedStateByUid.size(); i2++) {
            pw.format("    uid=%s isVisibleInScreenRecording=%s\n", this.mLastInvokedStateByUid.keyAt(i2), this.mLastInvokedStateByUid.valueAt(i2));
        }
    }
}
