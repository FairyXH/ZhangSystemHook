package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class Session extends android.view.IWindowSession.Stub implements android.os.IBinder.DeathRecipient {
    private static final float DEFAULT_REFRESHRATE = 9999.0f;
    private static final java.lang.String MSYNCTAG = "MSyncRefreshRate";
    private final java.util.ArrayList<com.android.server.wm.WindowState> mAddedWindows;
    private com.android.server.wm.AlertWindowNotification mAlertWindowNotification;
    private final android.util.ArraySet<com.android.server.wm.WindowSurfaceController> mAlertWindowSurfaces;
    final android.view.IWindowSessionCallback mCallback;
    final boolean mCanAddInternalSystemWindow;
    final boolean mCanAlwaysUpdateWallpaper;
    final boolean mCanCreateSystemApplicationOverlay;
    boolean mCanForceShowingInsets;
    final boolean mCanHideNonSystemOverlayWindows;
    final boolean mCanSetUnrestrictedGestureExclusion;
    private final boolean mCanStartTasksFromRecents;
    private boolean mClientDead;
    private final com.android.server.wm.DragDropController mDragDropController;
    private final android.view.InsetsSourceControl.Array mDummyControls;
    private float mLastReportedAnimatorScale;
    protected java.lang.String mPackageName;
    final int mPid;
    final com.android.server.wm.WindowProcessController mProcess;
    private java.lang.String mRelayoutTag;
    final com.android.server.wm.WindowManagerService mService;
    private com.android.server.wm.ISessionExt mSessionExt;
    private com.android.server.wm.Session.SessionWrapper mSessionWrapper;
    final boolean mSetsUnrestrictedKeepClearAreas;
    private boolean mShowingAlertWindowNotificationAllowed;
    private final java.lang.String mStringName;
    android.view.SurfaceSession mSurfaceSession;
    final int mUid;

    public Session(com.android.server.wm.WindowManagerService service, android.view.IWindowSessionCallback callback) {
        this(service, callback, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
    }

    Session(com.android.server.wm.WindowManagerService service, android.view.IWindowSessionCallback callback, int callingPid, int callingUid) {
        this.mAddedWindows = new java.util.ArrayList<>();
        this.mAlertWindowSurfaces = new android.util.ArraySet<>();
        this.mClientDead = false;
        this.mDummyControls = new android.view.InsetsSourceControl.Array();
        this.mSessionWrapper = new com.android.server.wm.Session.SessionWrapper();
        this.mSessionExt = (com.android.server.wm.ISessionExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.ISessionExt.class).base(this).create();
        this.mService = service;
        this.mCallback = callback;
        this.mPid = callingPid;
        this.mUid = callingUid;
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = service.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mLastReportedAnimatorScale = service.getCurrentAnimatorScale();
                this.mProcess = service.mAtmService.mProcessMap.getProcess(this.mPid);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        if (this.mProcess == null) {
            throw new java.lang.IllegalStateException("Unknown pid=" + this.mPid + " uid=" + this.mUid);
        }
        this.mCanAddInternalSystemWindow = service.mContext.checkCallingOrSelfPermission("android.permission.INTERNAL_SYSTEM_WINDOW") == 0;
        this.mCanForceShowingInsets = service.mAtmService.isCallerRecents(this.mUid) || service.mContext.checkCallingOrSelfPermission("android.permission.STATUS_BAR_SERVICE") == 0;
        this.mCanHideNonSystemOverlayWindows = service.mContext.checkCallingOrSelfPermission("android.permission.HIDE_NON_SYSTEM_OVERLAY_WINDOWS") == 0 || service.mContext.checkCallingOrSelfPermission("android.permission.HIDE_OVERLAY_WINDOWS") == 0;
        this.mCanCreateSystemApplicationOverlay = service.mContext.checkCallingOrSelfPermission("android.permission.SYSTEM_APPLICATION_OVERLAY") == 0;
        this.mCanStartTasksFromRecents = service.mContext.checkCallingOrSelfPermission("android.permission.START_TASKS_FROM_RECENTS") == 0;
        this.mSetsUnrestrictedKeepClearAreas = service.mContext.checkCallingOrSelfPermission("android.permission.SET_UNRESTRICTED_KEEP_CLEAR_AREAS") == 0;
        this.mCanSetUnrestrictedGestureExclusion = service.mContext.checkCallingOrSelfPermission("android.permission.SET_UNRESTRICTED_GESTURE_EXCLUSION") == 0;
        this.mCanAlwaysUpdateWallpaper = com.android.window.flags.Flags.alwaysUpdateWallpaperPermission() && service.mContext.checkCallingOrSelfPermission("android.permission.ALWAYS_UPDATE_WALLPAPER") == 0;
        this.mShowingAlertWindowNotificationAllowed = this.mService.mShowAlertWindowNotifications;
        this.mDragDropController = this.mService.mDragDropController;
        this.mSessionExt.setOplusSafeWindowPermission(service);
        this.mSessionExt.setOplusWallpaperUpdatePermission(service);
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("Session{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(" ");
        sb.append(this.mPid);
        if (this.mUid < 10000) {
            sb.append(":");
            sb.append(this.mUid);
        } else {
            sb.append(":u");
            sb.append(android.os.UserHandle.getUserId(this.mUid));
            sb.append('a');
            sb.append(android.os.UserHandle.getAppId(this.mUid));
        }
        sb.append("}");
        this.mStringName = sb.toString();
        try {
            this.mCallback.asBinder().linkToDeath(this, 0);
        } catch (android.os.RemoteException e) {
            this.mClientDead = true;
        }
    }

    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        try {
            return super.onTransact(code, data, reply, flags);
        } catch (java.lang.RuntimeException e) {
            if (!(e instanceof java.lang.SecurityException)) {
                android.util.Slog.wtf("WindowManager", "Window Session Crash", e);
            }
            throw e;
        }
    }

    boolean isClientDead() {
        return this.mClientDead;
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mCallback.asBinder().unlinkToDeath(this, 0);
                this.mClientDead = true;
                try {
                    for (int i = this.mAddedWindows.size() - 1; i >= 0; i--) {
                        com.android.server.wm.WindowState w = this.mAddedWindows.get(i);
                        android.util.Slog.i("WindowManager", "WIN DEATH: " + w);
                        if (w.mActivityRecord != null && w.mActivityRecord.findMainWindow() == w) {
                            this.mService.mSnapshotController.onAppDied(w.mActivityRecord);
                        }
                        w.removeIfPossible();
                    }
                } finally {
                    killSessionLocked();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public int addToDisplay(android.view.IWindow window, android.view.WindowManager.LayoutParams attrs, int viewVisibility, int displayId, int requestedVisibleTypes, android.view.InputChannel outInputChannel, android.view.InsetsState outInsetsState, android.view.InsetsSourceControl.Array outActiveControls, android.graphics.Rect outAttachedFrame, float[] outSizeCompatScale) {
        return this.mService.addWindow(this, window, attrs, viewVisibility, displayId, android.os.UserHandle.getUserId(this.mUid), requestedVisibleTypes, outInputChannel, outInsetsState, outActiveControls, outAttachedFrame, outSizeCompatScale);
    }

    public int addToDisplayAsUser(android.view.IWindow window, android.view.WindowManager.LayoutParams attrs, int viewVisibility, int displayId, int userId, int requestedVisibleTypes, android.view.InputChannel outInputChannel, android.view.InsetsState outInsetsState, android.view.InsetsSourceControl.Array outActiveControls, android.graphics.Rect outAttachedFrame, float[] outSizeCompatScale) {
        return this.mService.addWindow(this, window, attrs, viewVisibility, displayId, userId, requestedVisibleTypes, outInputChannel, outInsetsState, outActiveControls, outAttachedFrame, outSizeCompatScale);
    }

    public int addToDisplayWithoutInputChannel(android.view.IWindow window, android.view.WindowManager.LayoutParams attrs, int viewVisibility, int displayId, android.view.InsetsState outInsetsState, android.graphics.Rect outAttachedFrame, float[] outSizeCompatScale) {
        return this.mService.addWindow(this, window, attrs, viewVisibility, displayId, android.os.UserHandle.getUserId(this.mUid), android.view.WindowInsets.Type.defaultVisible(), null, outInsetsState, this.mDummyControls, outAttachedFrame, outSizeCompatScale);
    }

    public void remove(android.os.IBinder clientToken) {
        this.mService.removeClientToken(this, clientToken);
    }

    public boolean cancelDraw(android.view.IWindow window) {
        return this.mService.cancelDraw(this, window);
    }

    public int relayout(android.view.IWindow window, android.view.WindowManager.LayoutParams attrs, int requestedWidth, int requestedHeight, int viewFlags, int flags, int seq, int lastSyncSeqId, android.view.WindowRelayoutResult outRelayoutResult) {
        android.os.Trace.traceBegin(32L, this.mRelayoutTag);
        int res = this.mService.relayoutWindow(this, window, attrs, requestedWidth, requestedHeight, viewFlags, flags, seq, lastSyncSeqId, outRelayoutResult);
        android.os.Trace.traceEnd(32L);
        this.mSessionExt.hookrelayout(outRelayoutResult != null ? outRelayoutResult.mergedConfiguration : null, this.mPackageName);
        return res;
    }

    @java.lang.Deprecated
    public int relayoutLegacy(android.view.IWindow window, android.view.WindowManager.LayoutParams attrs, int requestedWidth, int requestedHeight, int viewFlags, int flags, int seq, int lastSyncSeqId, android.window.ClientWindowFrames outFrames, android.util.MergedConfiguration mergedConfiguration, android.view.SurfaceControl outSurfaceControl, android.view.InsetsState outInsetsState, android.view.InsetsSourceControl.Array outActiveControls, android.os.Bundle outBundle) {
        this.mRelayoutTag = this.mRelayoutTag == null ? "relayoutWindow:" : this.mRelayoutTag;
        android.os.Trace.traceBegin(32L, this.mRelayoutTag);
        int res = this.mService.relayoutWindow(this, window, attrs, requestedWidth, requestedHeight, viewFlags, flags, seq, lastSyncSeqId, outFrames, mergedConfiguration, outSurfaceControl, outInsetsState, outActiveControls, outBundle);
        android.os.Trace.traceEnd(32L);
        this.mSessionExt.hookrelayout(mergedConfiguration, this.mPackageName);
        return res;
    }

    public void relayoutAsync(android.view.IWindow window, android.view.WindowManager.LayoutParams attrs, int requestedWidth, int requestedHeight, int viewFlags, int flags, int seq, int lastSyncSeqId) {
        if (com.android.window.flags.Flags.windowSessionRelayoutInfo()) {
            relayout(window, attrs, requestedWidth, requestedHeight, viewFlags, flags, seq, lastSyncSeqId, null);
        } else {
            relayoutLegacy(window, attrs, requestedWidth, requestedHeight, viewFlags, flags, seq, lastSyncSeqId, null, null, null, null, null, null);
        }
    }

    public boolean outOfMemory(android.view.IWindow window) {
        return this.mService.outOfMemoryWindow(this, window);
    }

    public void setInsets(android.view.IWindow window, int touchableInsets, android.graphics.Rect contentInsets, android.graphics.Rect visibleInsets, android.graphics.Region touchableArea) {
        this.mService.setInsetsWindow(this, window, touchableInsets, contentInsets, visibleInsets, touchableArea);
    }

    public void clearTouchableRegion(android.view.IWindow window) {
        this.mService.clearTouchableRegion(this, window);
    }

    public void finishDrawing(android.view.IWindow window, android.view.SurfaceControl.Transaction postDrawTransaction, int seqId) throws java.lang.Throwable {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
            android.util.Slog.v("WindowManager", "IWindow finishDrawing called for " + window);
        }
        if (android.os.Trace.isTagEnabled(32L)) {
            android.os.Trace.traceBegin(32L, "finishDrawing: " + this.mPackageName);
        }
        this.mService.finishDrawingWindow(this, window, postDrawTransaction, seqId);
        android.os.Trace.traceEnd(32L);
    }

    public boolean performHapticFeedback(int effectId, boolean always, boolean fromIme) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mService.mPolicy.performHapticFeedback(this.mUid, this.mPackageName, effectId, always, null, fromIme);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void performHapticFeedbackAsync(int effectId, boolean always, boolean fromIme) {
        performHapticFeedback(effectId, always, fromIme);
    }

    public android.os.IBinder performDrag(android.view.IWindow window, int flags, android.view.SurfaceControl surface, int touchSource, int touchDeviceId, int touchPointerId, float touchX, float touchY, float thumbCenterX, float thumbCenterY, android.content.ClipData data) throws java.lang.Throwable {
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        validateAndResolveDragMimeTypeExtras(data, callingUid, callingPid, this.mPackageName);
        validateDragFlags(flags);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mDragDropController.performDrag(this.mPid, this.mUid, window, flags, surface, touchSource, touchDeviceId, touchPointerId, touchX, touchY, thumbCenterX, thumbCenterY, data);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean dropForAccessibility(android.view.IWindow window, int x, int y) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mDragDropController.dropForAccessibility(window, x, y);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    void validateDragFlags(int flags) {
        if ((flags & 2048) != 0 && !this.mCanStartTasksFromRecents) {
            throw new java.lang.SecurityException("Requires START_TASKS_FROM_RECENTS permission");
        }
    }

    void validateAndResolveDragMimeTypeExtras(android.content.ClipData clipData, int i, int i2, java.lang.String str) throws java.lang.Throwable {
        android.content.ClipDescription description = clipData != null ? clipData.getDescription() : null;
        if (description == null) {
            return;
        }
        boolean zHasMimeType = description.hasMimeType("application/vnd.android.activity");
        boolean zHasMimeType2 = description.hasMimeType("application/vnd.android.shortcut");
        boolean zHasMimeType3 = description.hasMimeType("application/vnd.android.task");
        int i3 = (zHasMimeType ? 1 : 0) + (zHasMimeType2 ? 1 : 0) + (zHasMimeType3 ? 1 : 0);
        if (i3 == 0) {
            return;
        }
        if (i3 > 1) {
            throw new java.lang.IllegalArgumentException("Can not specify more than one of activity, shortcut, or task mime types");
        }
        if (clipData.getItemCount() == 0) {
            throw new java.lang.IllegalArgumentException("Unexpected number of items (none)");
        }
        for (int i4 = 0; i4 < clipData.getItemCount(); i4++) {
            if (clipData.getItemAt(i4).getIntent() == null) {
                throw new java.lang.IllegalArgumentException("Unexpected item, expected an intent");
            }
        }
        if (zHasMimeType) {
            long jClearCallingIdentity = android.os.Binder.clearCallingIdentity();
            int i5 = 0;
            while (i5 < clipData.getItemCount()) {
                try {
                    android.content.ClipData.Item itemAt = clipData.getItemAt(i5);
                    android.content.Intent intent = itemAt.getIntent();
                    android.app.PendingIntent pendingIntent = (android.app.PendingIntent) intent.getParcelableExtra("android.intent.extra.PENDING_INTENT");
                    android.os.UserHandle userHandle = (android.os.UserHandle) intent.getParcelableExtra("android.intent.extra.USER");
                    if (pendingIntent == null || userHandle == null) {
                        throw new java.lang.IllegalArgumentException("Clip data must include the pending intent to launch and its associated user to launch for.");
                    }
                    android.content.ClipDescription clipDescription = description;
                    try {
                        itemAt.setActivityInfo(this.mService.mAtmService.resolveActivityInfoForIntent(this.mService.mAmInternal.getIntentForIntentSender(pendingIntent.getIntentSender().getTarget()), null, userHandle.getIdentifier(), i, i2));
                        i5++;
                        description = clipDescription;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        android.os.Binder.restoreCallingIdentity(jClearCallingIdentity);
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
            android.os.Binder.restoreCallingIdentity(jClearCallingIdentity);
            return;
        }
        if (zHasMimeType2) {
            if (!this.mCanStartTasksFromRecents) {
                throw new java.lang.SecurityException("Requires START_TASKS_FROM_RECENTS permission");
            }
            for (int i6 = 0; i6 < clipData.getItemCount(); i6++) {
                android.content.ClipData.Item itemAt2 = clipData.getItemAt(i6);
                android.content.Intent intent2 = itemAt2.getIntent();
                java.lang.String stringExtra = intent2.getStringExtra("android.intent.extra.shortcut.ID");
                java.lang.String stringExtra2 = intent2.getStringExtra("android.intent.extra.PACKAGE_NAME");
                android.os.UserHandle userHandle2 = (android.os.UserHandle) intent2.getParcelableExtra("android.intent.extra.USER");
                if (android.text.TextUtils.isEmpty(stringExtra) || android.text.TextUtils.isEmpty(stringExtra2) || userHandle2 == null) {
                    throw new java.lang.IllegalArgumentException("Clip item must include the package name, shortcut id, and the user to launch for.");
                }
                android.content.Intent[] intentArrCreateShortcutIntents = ((android.content.pm.ShortcutServiceInternal) com.android.server.LocalServices.getService(android.content.pm.ShortcutServiceInternal.class)).createShortcutIntents(android.os.UserHandle.getUserId(i), str, stringExtra2, stringExtra, userHandle2.getIdentifier(), i2, i);
                if (intentArrCreateShortcutIntents == null || intentArrCreateShortcutIntents.length == 0) {
                    throw new java.lang.IllegalArgumentException("Invalid shortcut id");
                }
                itemAt2.setActivityInfo(this.mService.mAtmService.resolveActivityInfoForIntent(intentArrCreateShortcutIntents[0], null, userHandle2.getIdentifier(), i, i2));
            }
            return;
        }
        if (zHasMimeType3) {
            if (!this.mCanStartTasksFromRecents) {
                throw new java.lang.SecurityException("Requires START_TASKS_FROM_RECENTS permission");
            }
            for (int i7 = 0; i7 < clipData.getItemCount(); i7++) {
                android.content.ClipData.Item itemAt3 = clipData.getItemAt(i7);
                int intExtra = itemAt3.getIntent().getIntExtra("android.intent.extra.TASK_ID", -1);
                if (intExtra == -1) {
                    throw new java.lang.IllegalArgumentException("Clip item must include the task id.");
                }
                com.android.server.wm.Task taskAnyTaskForId = this.mService.mRoot.anyTaskForId(intExtra);
                if (taskAnyTaskForId == null) {
                    throw new java.lang.IllegalArgumentException("Invalid task id.");
                }
                if (taskAnyTaskForId.getRootActivity() != null) {
                    itemAt3.setActivityInfo(taskAnyTaskForId.getRootActivity().info);
                } else {
                    itemAt3.setActivityInfo(this.mService.mAtmService.resolveActivityInfoForIntent(taskAnyTaskForId.intent, null, taskAnyTaskForId.mUserId, i, i2));
                }
            }
        }
    }

    public void reportDropResult(android.view.IWindow window, boolean consumed) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mDragDropController.reportDropResult(window, consumed);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void cancelDragAndDrop(android.os.IBinder dragToken, boolean skipAnimation) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mDragDropController.cancelDragAndDrop(dragToken, skipAnimation);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void dragRecipientEntered(android.view.IWindow window) {
        this.mDragDropController.dragRecipientEntered(window);
    }

    public void dragRecipientExited(android.view.IWindow window) {
        this.mDragDropController.dragRecipientExited(window);
    }

    public boolean startMovingTask(android.view.IWindow window, float startX, float startY) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            android.util.Slog.d("WindowManager", "startMovingTask: {" + startX + "," + startY + "}");
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            return this.mService.mTaskPositioningController.startMovingTask(window, startX, startY);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void finishMovingTask(android.view.IWindow window) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG_TASK_POSITIONING) {
            android.util.Slog.d("WindowManager", "finishMovingTask");
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mService.mTaskPositioningController.finishTaskPositioning(window);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void reportSystemGestureExclusionChanged(android.view.IWindow window, java.util.List<android.graphics.Rect> exclusionRects) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mService.reportSystemGestureExclusionChanged(this, window, exclusionRects);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void reportDecorViewGestureInterceptionChanged(android.view.IWindow window, boolean intercepted) {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mService.reportDecorViewGestureChanged(this, window, intercepted);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void reportKeepClearAreasChanged(android.view.IWindow window, java.util.List<android.graphics.Rect> restricted, java.util.List<android.graphics.Rect> unrestricted) {
        if (!this.mSetsUnrestrictedKeepClearAreas && !unrestricted.isEmpty()) {
            unrestricted = java.util.Collections.emptyList();
        }
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mService.reportKeepClearAreasChanged(this, window, restricted, unrestricted);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    private void actionOnWallpaper(android.os.IBinder window, java.util.function.BiConsumer<com.android.server.wm.WallpaperController, com.android.server.wm.WindowState> action) {
        com.android.server.wm.WindowState windowState = this.mService.windowForClientLocked(this, window, true);
        action.accept(windowState.getDisplayContent().mWallpaperController, windowState);
    }

    public void setWallpaperPosition(android.os.IBinder window, final float x, final float y, final float xStep, final float yStep) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    actionOnWallpaper(window, new java.util.function.BiConsumer() { // from class: com.android.server.wm.Session$$ExternalSyntheticLambda5
                        @Override // java.util.function.BiConsumer
                        public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                            ((com.android.server.wm.WallpaperController) obj).setWindowWallpaperPosition((com.android.server.wm.WindowState) obj2, x, y, xStep, yStep);
                        }
                    });
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void setWallpaperZoomOut(android.os.IBinder window, final float zoom) {
        if (java.lang.Float.compare(0.0f, zoom) > 0 || java.lang.Float.compare(1.0f, zoom) < 0 || java.lang.Float.isNaN(zoom)) {
            throw new java.lang.IllegalArgumentException("Zoom must be a valid float between 0 and 1: " + zoom);
        }
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    actionOnWallpaper(window, new java.util.function.BiConsumer() { // from class: com.android.server.wm.Session$$ExternalSyntheticLambda1
                        @Override // java.util.function.BiConsumer
                        public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                            ((com.android.server.wm.WallpaperController) obj).setWallpaperZoomOut((com.android.server.wm.WindowState) obj2, zoom);
                        }
                    });
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void setShouldZoomOutWallpaper(android.os.IBinder window, final boolean shouldZoom) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                actionOnWallpaper(window, new java.util.function.BiConsumer() { // from class: com.android.server.wm.Session$$ExternalSyntheticLambda3
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        ((com.android.server.wm.WallpaperController) obj).setShouldZoomOutWallpaper((com.android.server.wm.WindowState) obj2, shouldZoom);
                    }
                });
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void wallpaperOffsetsComplete(final android.os.IBinder window) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                actionOnWallpaper(window, new java.util.function.BiConsumer() { // from class: com.android.server.wm.Session$$ExternalSyntheticLambda2
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        ((com.android.server.wm.WallpaperController) obj).wallpaperOffsetsComplete(window);
                    }
                });
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void setWallpaperDisplayOffset(android.os.IBinder window, final int x, final int y) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long ident = android.os.Binder.clearCallingIdentity();
                try {
                    actionOnWallpaper(window, new java.util.function.BiConsumer() { // from class: com.android.server.wm.Session$$ExternalSyntheticLambda0
                        @Override // java.util.function.BiConsumer
                        public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                            ((com.android.server.wm.WallpaperController) obj).setWindowWallpaperDisplayOffset((com.android.server.wm.WindowState) obj2, x, y);
                        }
                    });
                } finally {
                    android.os.Binder.restoreCallingIdentity(ident);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void sendWallpaperCommand(android.os.IBinder window, java.lang.String action, int x, int y, int z, android.os.Bundle extras, boolean sync) throws java.lang.Throwable {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                try {
                    long ident = android.os.Binder.clearCallingIdentity();
                    try {
                    } catch (java.lang.Throwable th) {
                        th = th;
                    }
                    try {
                        com.android.server.wm.WindowState windowState = this.mService.windowForClientLocked(this, window, true);
                        com.android.server.wm.WallpaperController wallpaperController = windowState.getDisplayContent().mWallpaperController;
                        if (this.mCanAlwaysUpdateWallpaper || this.mSessionExt.hasOplusWallpaperUpdatePermission() || windowState == wallpaperController.getWallpaperTarget() || windowState == wallpaperController.getPrevWallpaperTarget()) {
                            wallpaperController.sendWindowWallpaperCommandUnchecked(windowState, action, x, y, z, extras, sync);
                        }
                        android.os.Binder.restoreCallingIdentity(ident);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                        android.os.Binder.restoreCallingIdentity(ident);
                        throw th;
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
    }

    public void wallpaperCommandComplete(final android.os.IBinder window, android.os.Bundle result) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                actionOnWallpaper(window, new java.util.function.BiConsumer() { // from class: com.android.server.wm.Session$$ExternalSyntheticLambda4
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        ((com.android.server.wm.WallpaperController) obj).wallpaperCommandComplete(window);
                    }
                });
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public void onRectangleOnScreenRequested(android.os.IBinder token, android.graphics.Rect rectangle) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    this.mService.onRectangleOnScreenRequested(token, rectangle);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public android.view.IWindowId getWindowId(android.os.IBinder window) {
        return this.mService.getWindowId(window);
    }

    public void pokeDrawLock(android.os.IBinder window) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mService.pokeDrawLock(this, window);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void updateTapExcludeRegion(android.view.IWindow window, android.graphics.Region region) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mService.updateTapExcludeRegion(window, region);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void updateRequestedVisibleTypes(android.view.IWindow window, int requestedVisibleTypes) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowState win = this.mService.windowForClientLocked(this, window, false);
                if (win != null) {
                    win.setRequestedVisibleTypes(requestedVisibleTypes);
                    win.getDisplayContent().getInsetsPolicy().onRequestedVisibleTypesChanged(win);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void onWindowAdded(com.android.server.wm.WindowState w) {
        w.getWrapper().getExtImpl().attach(w);
        if (this.mPackageName == null) {
            this.mPackageName = this.mProcess.mInfo.packageName;
            this.mRelayoutTag = "relayoutWindow: " + this.mPackageName;
        }
        if (this.mSurfaceSession == null) {
            if (com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
                android.util.Slog.v("WindowManager", "First window added to " + this + ", creating SurfaceSession");
            }
            this.mSurfaceSession = new android.view.SurfaceSession();
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_TRANSACTIONS_enabled[2]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mSurfaceSession);
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, -1594708154257031561L, 0, null, protoLogParam0);
            }
            this.mService.mSessions.add(this);
            if (this.mLastReportedAnimatorScale != this.mService.getCurrentAnimatorScale()) {
                this.mService.dispatchNewAnimatorScaleLocked(this);
            }
            this.mProcess.mWindowSession = this;
        }
        this.mAddedWindows.add(w);
    }

    void onWindowRemoved(com.android.server.wm.WindowState w) {
        this.mAddedWindows.remove(w);
        if (this.mAddedWindows.isEmpty()) {
            killSessionLocked();
        }
    }

    boolean hasWindow() {
        return !this.mAddedWindows.isEmpty();
    }

    void onWindowSurfaceVisibilityChanged(com.android.server.wm.WindowSurfaceController surfaceController, boolean visible, int type) {
        boolean changed;
        if (!android.view.WindowManager.LayoutParams.isSystemAlertWindowType(type)) {
            return;
        }
        boolean noSystemOverlayPermission = (this.mCanAddInternalSystemWindow || this.mCanCreateSystemApplicationOverlay) ? false : true;
        if (visible) {
            changed = this.mAlertWindowSurfaces.add(surfaceController);
            if (type == 2038) {
                com.android.internal.os.logging.MetricsLoggerWrapper.logAppOverlayEnter(this.mUid, this.mPackageName, changed, type, false);
            } else if (noSystemOverlayPermission) {
                com.android.internal.os.logging.MetricsLoggerWrapper.logAppOverlayEnter(this.mUid, this.mPackageName, changed, type, true);
            }
        } else {
            changed = this.mAlertWindowSurfaces.remove(surfaceController);
            if (type == 2038) {
                com.android.internal.os.logging.MetricsLoggerWrapper.logAppOverlayExit(this.mUid, this.mPackageName, changed, type, false);
            } else if (noSystemOverlayPermission) {
                com.android.internal.os.logging.MetricsLoggerWrapper.logAppOverlayExit(this.mUid, this.mPackageName, changed, type, true);
            }
        }
        if (changed && noSystemOverlayPermission) {
            if (this.mAlertWindowSurfaces.isEmpty()) {
                cancelAlertWindowNotification();
            } else if (this.mAlertWindowNotification == null && !isSatellitePointingUiPackage()) {
                this.mAlertWindowNotification = new com.android.server.wm.AlertWindowNotification(this.mService, this.mPackageName);
                if (this.mShowingAlertWindowNotificationAllowed) {
                    this.mAlertWindowNotification.post();
                }
            }
        }
        if (changed && this.mPid != com.android.server.wm.WindowManagerService.MY_PID) {
            setHasOverlayUi(!this.mAlertWindowSurfaces.isEmpty());
        }
    }

    private boolean isSatellitePointingUiPackage() {
        return this.mPackageName != null && this.mPackageName.equals(this.mService.mContext.getString(android.R.string.config_radio_access_family)) && com.android.server.wm.ActivityTaskManagerService.checkPermission("android.permission.SATELLITE_COMMUNICATION", this.mPid, this.mUid) == 0;
    }

    void setShowingAlertWindowNotificationAllowed(boolean allowed) {
        this.mShowingAlertWindowNotificationAllowed = allowed;
        if (this.mAlertWindowNotification != null) {
            if (allowed) {
                this.mAlertWindowNotification.post();
            } else {
                this.mAlertWindowNotification.cancel(false);
            }
        }
    }

    private void killSessionLocked() {
        if (!this.mClientDead) {
            return;
        }
        this.mService.mSessions.remove(this);
        if (this.mSurfaceSession == null) {
            return;
        }
        if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_TRANSACTIONS_enabled[2]) {
            java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mSurfaceSession);
            com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_TRANSACTIONS, 2638961674625826260L, 0, null, protoLogParam0);
        }
        try {
            this.mSurfaceSession.kill();
        } catch (java.lang.Exception e) {
            android.util.Slog.w("WindowManager", "Exception thrown when killing surface session " + this.mSurfaceSession + " in session " + this + ": " + e.toString());
        }
        this.mSurfaceSession = null;
        this.mAddedWindows.clear();
        this.mAlertWindowSurfaces.clear();
        setHasOverlayUi(false);
        cancelAlertWindowNotification();
    }

    void setHasOverlayUi(boolean z) {
        this.mService.mH.obtainMessage(58, this.mPid, z ? 1 : 0).sendToTarget();
    }

    private void cancelAlertWindowNotification() {
        if (this.mAlertWindowNotification == null) {
            return;
        }
        this.mAlertWindowNotification.cancel(true);
        this.mAlertWindowNotification = null;
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("numWindow=");
        pw.print(this.mAddedWindows.size());
        pw.print(" mCanAddInternalSystemWindow=");
        pw.print(this.mCanAddInternalSystemWindow);
        pw.print(" mAlertWindowSurfaces=");
        pw.print(this.mAlertWindowSurfaces);
        pw.print(" mClientDead=");
        pw.print(this.mClientDead);
        pw.print(" mSurfaceSession=");
        pw.println(this.mSurfaceSession);
        pw.print(prefix);
        pw.print("mPackageName=");
        pw.println(this.mPackageName);
        if (isSatellitePointingUiPackage()) {
            pw.print(prefix);
            pw.println("mIsSatellitePointingUiPackage=true");
        }
    }

    public java.lang.String toString() {
        return this.mStringName;
    }

    boolean hasAlertWindowSurfaces(com.android.server.wm.DisplayContent displayContent) {
        for (int i = this.mAlertWindowSurfaces.size() - 1; i >= 0; i--) {
            com.android.server.wm.WindowSurfaceController surfaceController = this.mAlertWindowSurfaces.valueAt(i);
            if (surfaceController.mAnimator.mWin.getDisplayContent() == displayContent) {
                return true;
            }
        }
        return false;
    }

    public void grantInputChannel(int displayId, android.view.SurfaceControl surface, android.os.IBinder clientToken, android.window.InputTransferToken hostInputTransferToken, int flags, int privateFlags, int inputFeatures, int type, android.os.IBinder windowToken, android.window.InputTransferToken inputTransferToken, java.lang.String inputHandleName, android.view.InputChannel outInputChannel) {
        if (hostInputTransferToken == null && !this.mCanAddInternalSystemWindow) {
            throw new java.lang.SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mService.grantInputChannel(this, this.mUid, this.mPid, displayId, surface, clientToken, hostInputTransferToken, flags, this.mCanAddInternalSystemWindow ? privateFlags : 0, inputFeatures, type, windowToken, inputTransferToken, inputHandleName, outInputChannel);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void updateInputChannel(android.os.IBinder channelToken, int displayId, android.view.SurfaceControl surface, int flags, int privateFlags, int inputFeatures, android.graphics.Region region) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mService.updateInputChannel(channelToken, displayId, surface, flags, this.mCanAddInternalSystemWindow ? privateFlags : 0, inputFeatures, region);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void grantEmbeddedWindowFocus(android.view.IWindow callingWindow, android.window.InputTransferToken targetInputToken, boolean grantFocus) {
        if (com.android.server.wm.WindowManagerDebugConfig.DEBUG) {
            android.util.Slog.d("WindowManager", "NFW_grantEmbeddedWindowFocus: calleruid: " + android.os.Binder.getCallingUid() + " callerpid: " + android.os.Binder.getCallingPid());
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            if (callingWindow == null) {
                if (!this.mCanAddInternalSystemWindow) {
                    throw new java.lang.SecurityException("Requires INTERNAL_SYSTEM_WINDOW permission");
                }
                this.mService.grantEmbeddedWindowFocus(this, targetInputToken, grantFocus);
            } else {
                this.mService.grantEmbeddedWindowFocus(this, callingWindow, targetInputToken, grantFocus);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public boolean moveFocusToAdjacentWindow(android.view.IWindow fromWindow, int direction) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    com.android.server.wm.WindowState win = this.mService.windowForClientLocked(this, fromWindow, false);
                    if (win != null) {
                        boolean zMoveFocusToAdjacentWindow = this.mService.moveFocusToAdjacentWindow(win, direction);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return zMoveFocusToAdjacentWindow;
                    }
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return false;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    public void generateDisplayHash(android.view.IWindow window, android.graphics.Rect boundsInWindow, java.lang.String hashAlgorithm, android.os.RemoteCallback callback) {
        long origId = android.os.Binder.clearCallingIdentity();
        try {
            this.mService.generateDisplayHash(this, window, boundsInWindow, hashAlgorithm, callback);
        } finally {
            android.os.Binder.restoreCallingIdentity(origId);
        }
    }

    public void setOnBackInvokedCallbackInfo(android.view.IWindow window, android.window.OnBackInvokedCallbackInfo callbackInfo) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                com.android.server.wm.WindowState windowState = this.mService.windowForClientLocked(this, window, false);
                if (windowState == null) {
                    android.util.Slog.i("WindowManager", "setOnBackInvokedCallback(): No window state for package:" + this.mPackageName);
                } else {
                    windowState.setOnBackInvokedCallbackInfo(callbackInfo);
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    public com.android.server.wm.ISessionWrapper getWrapper() {
        return this.mSessionWrapper;
    }

    private class SessionWrapper implements com.android.server.wm.ISessionWrapper {
        private SessionWrapper() {
        }

        @Override // com.android.server.wm.ISessionWrapper
        public com.android.server.wm.ISessionExt getExtImpl() {
            return com.android.server.wm.Session.this.mSessionExt;
        }
    }
}
