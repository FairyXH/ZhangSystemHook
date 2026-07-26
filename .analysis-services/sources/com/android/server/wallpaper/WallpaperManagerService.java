package com.android.server.wallpaper;

/* JADX INFO: loaded from: classes3.dex */
public class WallpaperManagerService extends android.app.IWallpaperManager.Stub implements com.android.server.wallpaper.IWallpaperManagerService {
    private static final int CRASH_RECONNECT_RETRIES = 5;
    private static final boolean DEBUG_LIVE = true;
    private static final double LMK_LOW_THRESHOLD_MEMORY_PERCENTAGE = 10.0d;
    private static final long LMK_RECONNECT_DELAY_MS = 5000;
    private static final int LMK_RECONNECT_REBIND_RETRIES = 6;
    private static final int MAX_WALLPAPER_COMPONENT_LOG_LENGTH = 128;
    private static final long MIN_WALLPAPER_CRASH_TIME = 30000;
    private static final java.lang.String TAG = "WallpaperManagerService";
    private final android.app.ActivityManager mActivityManager;
    private final android.app.AppOpsManager mAppOpsManager;
    private android.app.WallpaperColors mCacheDefaultImageWallpaperColors;
    private final android.util.SparseArray<android.util.SparseArray<android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback>>> mColorsChangedListeners;
    private final android.content.Context mContext;
    private android.content.ComponentName mDefaultWallpaperComponent;
    protected com.android.server.wallpaper.WallpaperData mFallbackWallpaper;
    private com.android.server.ServiceThread mHandlerThread;
    private boolean mHomeWallpaperWaitingForUnlock;
    private final android.content.pm.IPackageManager mIPackageManager;
    private final android.content.ComponentName mImageWallpaper;
    private boolean mInAmbientMode;
    protected com.android.server.wallpaper.WallpaperData mLastLockWallpaper;
    protected com.android.server.wallpaper.WallpaperData mLastWallpaper;
    private boolean mLockWallpaperWaitingForUnlock;
    private final com.android.server.wallpaper.WallpaperManagerService.MyPackageMonitor mMonitor;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    com.android.server.wallpaper.WallpaperManagerService.WallpaperDestinationChangeHandler mPendingMigrationViaStatic;
    private boolean mShuttingDown;
    final com.android.server.wallpaper.WallpaperCropper mWallpaperCropper;
    final com.android.server.wallpaper.WallpaperDataParser mWallpaperDataParser;
    final com.android.server.wallpaper.WallpaperDisplayHelper mWallpaperDisplayHelper;
    private final com.android.server.wm.WindowManagerInternal mWindowManagerInternal;
    private static final android.graphics.RectF LOCAL_COLOR_BOUNDS = new android.graphics.RectF(0.0f, 0.0f, 1.0f, 1.0f);
    private static final java.util.Map<java.lang.Integer, java.lang.String> sWallpaperType = java.util.Map.of(1, "decode_record", 2, "decode_lock_record");
    private final java.lang.Object mLock = new java.lang.Object();
    private boolean mInitialUserSwitch = true;
    private final android.hardware.display.DisplayManager.DisplayListener mDisplayListener = new android.hardware.display.DisplayManager.DisplayListener() { // from class: com.android.server.wallpaper.WallpaperManagerService.1
        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayAdded(int displayId) {
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayRemoved(int displayId) {
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperDisplayHelper.removeDisplayData(displayId);
                if (com.android.server.wallpaper.WallpaperManagerService.this.mLastWallpaper != null) {
                    com.android.server.wallpaper.WallpaperData targetWallpaper = null;
                    if (com.android.server.wallpaper.WallpaperManagerService.this.mLastWallpaper.connection.containsDisplay(displayId)) {
                        targetWallpaper = com.android.server.wallpaper.WallpaperManagerService.this.mLastWallpaper;
                    } else if (com.android.server.wallpaper.WallpaperManagerService.this.mFallbackWallpaper != null && com.android.server.wallpaper.WallpaperManagerService.this.mFallbackWallpaper.connection != null && com.android.server.wallpaper.WallpaperManagerService.this.mFallbackWallpaper.connection.containsDisplay(displayId)) {
                        targetWallpaper = com.android.server.wallpaper.WallpaperManagerService.this.mFallbackWallpaper;
                    }
                    if (targetWallpaper == null) {
                        return;
                    }
                    com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector = targetWallpaper.connection.getDisplayConnectorOrCreate(displayId);
                    if (connector == null) {
                        return;
                    }
                    connector.disconnectLocked(targetWallpaper.connection);
                    targetWallpaper.connection.removeDisplayConnector(displayId);
                    com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperDisplayHelper.removeDisplayData(displayId);
                }
                for (int i = com.android.server.wallpaper.WallpaperManagerService.this.mColorsChangedListeners.size() - 1; i >= 0; i--) {
                    android.util.SparseArray<android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback>> callbacks = (android.util.SparseArray) com.android.server.wallpaper.WallpaperManagerService.this.mColorsChangedListeners.valueAt(i);
                    callbacks.delete(displayId);
                }
            }
        }

        @Override // android.hardware.display.DisplayManager.DisplayListener
        public void onDisplayChanged(int displayId) {
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().removeDisplayData(displayId, com.android.server.wallpaper.WallpaperManagerService.this);
            }
        }
    };
    private final android.util.SparseArray<com.android.server.wallpaper.WallpaperData> mWallpaperMap = new android.util.SparseArray<>();
    private final android.util.SparseArray<com.android.server.wallpaper.WallpaperData> mLockWallpaperMap = new android.util.SparseArray<>();
    private final android.util.SparseBooleanArray mUserRestorecon = new android.util.SparseBooleanArray();
    private int mCurrentUserId = -10000;
    private com.android.server.wallpaper.LocalColorRepository mLocalColorRepo = new com.android.server.wallpaper.LocalColorRepository();
    private com.android.server.wallpaper.WallpaperManagerService.WallpaperManagerServiceWrapperImpl mWallpaperManagerServiceWrapper = new com.android.server.wallpaper.WallpaperManagerService.WallpaperManagerServiceWrapperImpl();
    private com.android.server.wallpaper.IWallpaperManagerServiceExt mWallpaperManagerServiceExt = (com.android.server.wallpaper.IWallpaperManagerServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.wallpaper.IWallpaperManagerServiceExt.class).base(this).create();
    private android.app.IWallpaperManagerExt mWallpaperManagerExt = (android.app.IWallpaperManagerExt) system.ext.loader.core.ExtLoader.type(android.app.IWallpaperManagerExt.class).create();

    public static class Lifecycle extends com.android.server.SystemService {
        private com.android.server.wallpaper.IWallpaperManagerService mService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            try {
                this.mService = (com.android.server.wallpaper.IWallpaperManagerService) java.lang.Class.forName(getContext().getResources().getString(android.R.string.config_wearServiceComponent)).getConstructor(android.content.Context.class).newInstance(getContext());
                publishBinderService(com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_LIVE_WALLPAPER, this.mService);
            } catch (java.lang.Exception exp) {
                android.util.Slog.wtf(com.android.server.wallpaper.WallpaperManagerService.TAG, "Failed to instantiate WallpaperManagerService", exp);
            }
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (this.mService != null) {
                this.mService.onBootPhase(phase);
            }
        }

        @Override // com.android.server.SystemService
        public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
            if (this.mService != null) {
                this.mService.onUnlockUser(user.getUserIdentifier());
            }
        }
    }

    class WallpaperObserver extends android.os.FileObserver {
        final int mUserId;
        final com.android.server.wallpaper.WallpaperData mWallpaper;
        final java.io.File mWallpaperDir;
        final java.io.File mWallpaperFile;
        final java.io.File mWallpaperLockFile;

        public WallpaperObserver(com.android.server.wallpaper.WallpaperData wallpaper) {
            super(com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceExt.getWallpaperDir(wallpaper.userId, wallpaper.mWallpaperDataExt.getPhysicalDisplayId(), com.android.server.wallpaper.WallpaperUtils.getWallpaperDir(wallpaper.userId)).getAbsolutePath(), 1672);
            this.mUserId = wallpaper.userId;
            this.mWallpaperDir = com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceExt.getWallpaperDir(wallpaper.userId, wallpaper.mWallpaperDataExt.getPhysicalDisplayId(), com.android.server.wallpaper.WallpaperUtils.getWallpaperDir(wallpaper.userId));
            this.mWallpaper = wallpaper;
            this.mWallpaperFile = new java.io.File(this.mWallpaperDir, "wallpaper_orig");
            this.mWallpaperLockFile = new java.io.File(this.mWallpaperDir, "wallpaper_lock_orig");
        }

        com.android.server.wallpaper.WallpaperData dataForEvent(boolean lockChanged) {
            com.android.server.wallpaper.WallpaperData wallpaper = null;
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                if (lockChanged) {
                    try {
                        wallpaper = com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceExt.getWallpaperMapLock(this.mWallpaper, com.android.server.wallpaper.WallpaperManagerService.this.mLockWallpaperMap).get(this.mUserId);
                    } catch (java.lang.Throwable th) {
                        throw th;
                    }
                }
                if (wallpaper == null) {
                    wallpaper = com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceExt.getWallpaperMapSys(this.mWallpaper, com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperMap).get(this.mUserId);
                }
            }
            return wallpaper != null ? wallpaper : this.mWallpaper;
        }

        private void updateWallpapers(int event, java.lang.String path) throws java.lang.Throwable {
            boolean isAppliedToLock;
            int i;
            java.io.File changedFile = new java.io.File(this.mWallpaperDir, path);
            boolean sysWallpaperChanged = this.mWallpaperFile.equals(changedFile);
            boolean lockWallpaperChanged = this.mWallpaperLockFile.equals(changedFile);
            final com.android.server.wallpaper.WallpaperData wallpaper = dataForEvent(lockWallpaperChanged);
            boolean moved = event == 128;
            boolean written = event == 8 || moved;
            boolean isMigration = moved && lockWallpaperChanged;
            boolean isRestore = moved && !isMigration;
            boolean isAppliedToLock2 = (wallpaper.mWhich & 2) != 0;
            boolean needsUpdate = wallpaper.wallpaperComponent == null || event != 8 || wallpaper.imageWallpaperPending;
            if (isMigration) {
                return;
            }
            if (sysWallpaperChanged || lockWallpaperChanged) {
                if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                    android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "Wallpaper file change: evt=" + event + " path=" + path + " sys=" + sysWallpaperChanged + " lock=" + lockWallpaperChanged + " imagePending=" + wallpaper.imageWallpaperPending + " mWhich=0x" + java.lang.Integer.toHexString(wallpaper.mWhich) + " userId=" + wallpaper.userId + " written=" + written + " isMigration=" + isMigration + " isRestore=" + isRestore + " isAppliedToLock=" + isAppliedToLock2 + " needsUpdate=" + needsUpdate);
                }
                synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                    try {
                        try {
                            if (com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().shouldNotifyCallbacks(wallpaper)) {
                                try {
                                    com.android.server.wallpaper.WallpaperManagerService.this.notifyCallbacksLocked(wallpaper);
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                }
                            }
                            if (!written || !needsUpdate) {
                                return;
                            }
                            android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "Setting new static wallpaper: which=" + wallpaper.mWhich);
                            int phyid = com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getPhysicalDisplayIdLocked(wallpaper.mWhich);
                            com.android.server.wallpaper.WallpaperManagerService.WallpaperDestinationChangeHandler localSync = com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getPendingMigrationViaStatic(com.android.server.wallpaper.WallpaperManagerService.this.mPendingMigrationViaStatic, phyid);
                            com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().removePendingMigrationViaStatic(phyid);
                            com.android.server.wallpaper.WallpaperManagerService.this.mPendingMigrationViaStatic = null;
                            android.os.SELinux.restorecon(changedFile);
                            com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().setIsMainDisplayWallpaperChangeLocked(wallpaper, wallpaper.mWhich);
                            if (isRestore) {
                                try {
                                    if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                                        android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "Wallpaper restore; reloading metadata");
                                    }
                                    try {
                                        com.android.server.wallpaper.WallpaperManagerService.this.loadSettingsLocked(wallpaper.userId, true, 3);
                                    } catch (java.lang.Throwable th2) {
                                        th = th2;
                                    }
                                } catch (java.lang.Throwable th3) {
                                    th = th3;
                                }
                            }
                            try {
                                if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                                    android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "Wallpaper written; generating crop");
                                }
                                com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperCropper.generateCrop(wallpaper);
                                if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                                    android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "Crop done; invoking completion callback");
                                }
                                wallpaper.imageWallpaperPending = false;
                                if (sysWallpaperChanged) {
                                    com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().restoreDefaultThemeIfNeeded(com.android.server.wallpaper.WallpaperManagerService.this.mContext, wallpaper.mWhich);
                                    if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                                        android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "Home screen wallpaper changed");
                                    }
                                    android.os.IRemoteCallback iRemoteCallback = new android.os.IRemoteCallback.Stub() { // from class: com.android.server.wallpaper.WallpaperManagerService.WallpaperObserver.1
                                        public void sendResult(android.os.Bundle data) throws android.os.RemoteException {
                                            android.util.Slog.i(com.android.server.wallpaper.WallpaperManagerService.TAG, "publish system wallpaper changed!");
                                            com.android.server.wallpaper.WallpaperManagerService.this.notifyWallpaperChanged(wallpaper);
                                        }
                                    };
                                    com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().updateWallpaperBitmap();
                                    wallpaper.mBindSource = com.android.server.wallpaper.WallpaperData.BindSource.SET_STATIC;
                                    com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceExt.copySysWallpaperToOtherPhysicalDisplaysLocked(wallpaper, 1);
                                    if (com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().bindWallpaperComponentOnImageChangedLocked(wallpaper, iRemoteCallback)) {
                                        isAppliedToLock = isAppliedToLock2;
                                        i = 2;
                                    } else {
                                        isAppliedToLock = isAppliedToLock2;
                                        i = 2;
                                        com.android.server.wallpaper.WallpaperManagerService.this.bindWallpaperComponentLocked(com.android.server.wallpaper.WallpaperManagerService.this.mImageWallpaper, true, false, wallpaper, iRemoteCallback);
                                    }
                                } else {
                                    isAppliedToLock = isAppliedToLock2;
                                    i = 2;
                                }
                                if (lockWallpaperChanged) {
                                    com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().restoreDefaultThemeIfNeeded(com.android.server.wallpaper.WallpaperManagerService.this.mContext, wallpaper.mWhich);
                                    if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                                        android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "Lock screen wallpaper changed");
                                    }
                                    android.os.IRemoteCallback iRemoteCallback2 = new android.os.IRemoteCallback.Stub() { // from class: com.android.server.wallpaper.WallpaperManagerService.WallpaperObserver.2
                                        public void sendResult(android.os.Bundle data) throws android.os.RemoteException {
                                            android.util.Slog.i(com.android.server.wallpaper.WallpaperManagerService.TAG, "publish lock wallpaper changed!");
                                            com.android.server.wallpaper.WallpaperManagerService.this.notifyWallpaperChanged(wallpaper);
                                        }
                                    };
                                    com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().notifyLockWallpaperChanged(com.android.server.wallpaper.WallpaperManagerService.this.mContext, com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId, com.android.server.wallpaper.WallpaperManagerService.this.mImageWallpaper, false);
                                    wallpaper.mBindSource = com.android.server.wallpaper.WallpaperData.BindSource.SET_STATIC;
                                    com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceExt.copySysWallpaperToOtherPhysicalDisplaysLocked(wallpaper, i);
                                    if (!com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().bindWallpaperComponentOnImageChangedLocked(wallpaper, iRemoteCallback2)) {
                                        com.android.server.wallpaper.WallpaperManagerService.this.bindWallpaperComponentLocked(com.android.server.wallpaper.WallpaperManagerService.this.mImageWallpaper, true, false, wallpaper, iRemoteCallback2);
                                    }
                                } else if (isAppliedToLock) {
                                    if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                                        android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "Lock screen wallpaper changed to same as home");
                                    }
                                    com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().notifyLockWallpaperChanged(com.android.server.wallpaper.WallpaperManagerService.this.mContext, com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId, com.android.server.wallpaper.WallpaperManagerService.this.mImageWallpaper, false);
                                    int physicalDisplayId = wallpaper.mWallpaperDataExt.getPhysicalDisplayId();
                                    com.android.server.wallpaper.WallpaperData lockedWallpaper = com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(i, physicalDisplayId, com.android.server.wallpaper.WallpaperManagerService.this.mLockWallpaperMap).get(wallpaper.userId);
                                    android.util.Slog.d(com.android.server.wallpaper.WallpaperManagerService.TAG, "changed to same as home: lockedWallpaper " + lockedWallpaper);
                                    if (lockedWallpaper != null) {
                                        com.android.server.wallpaper.WallpaperManagerService.this.detachWallpaperLocked(lockedWallpaper);
                                        com.android.server.wallpaper.WallpaperManagerService.this.clearWallpaperBitmaps(lockedWallpaper.userId, lockedWallpaper.mWhich);
                                        com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(i, physicalDisplayId, com.android.server.wallpaper.WallpaperManagerService.this.mLockWallpaperMap).remove(this.mWallpaper.userId);
                                    }
                                    com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceExt.deleteStaticLockWallpaper(wallpaper, i);
                                }
                                if (!com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().saveSettingsLockedForAffectedPhysicalDisplays(wallpaper.userId, wallpaper.mWhich)) {
                                    com.android.server.wallpaper.WallpaperManagerService.this.saveSettingsLocked(wallpaper.userId);
                                }
                                if (localSync != null) {
                                    localSync.complete();
                                }
                                if (com.android.window.flags.Flags.offloadColorExtraction()) {
                                    return;
                                }
                                com.android.server.wallpaper.WallpaperManagerService.this.notifyWallpaperColorsChanged(wallpaper);
                                return;
                            } catch (java.lang.Throwable th4) {
                                th = th4;
                            }
                        } catch (java.lang.Throwable th5) {
                            th = th5;
                        }
                    } catch (java.lang.Throwable th6) {
                        th = th6;
                    }
                    throw th;
                }
            }
        }

        @Override // android.os.FileObserver
        public void onEvent(int event, java.lang.String path) throws java.lang.Throwable {
            if (path != null) {
                updateWallpapers(event, path);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyWallpaperChanged(com.android.server.wallpaper.WallpaperData wallpaper) {
        if (wallpaper.setComplete != null) {
            try {
                wallpaper.setComplete.onWallpaperChanged();
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "onWallpaperChanged threw an exception", e);
            }
        }
    }

    void notifyWallpaperColorsChanged(com.android.server.wallpaper.WallpaperData wallpaper) {
        notifyWallpaperColorsChanged(wallpaper, wallpaper.mWhich);
    }

    private void notifyWallpaperColorsChanged(final com.android.server.wallpaper.WallpaperData wallpaper, final int which) {
        if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
            android.util.Slog.i(TAG, "Notifying wallpaper colors changed");
        }
        if (!this.mWallpaperManagerServiceWrapper.getExtImpl().notifyWallpaperColorsChanged(wallpaper, which) && wallpaper.connection != null) {
            wallpaper.connection.forEachDisplayConnector(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda20
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$notifyWallpaperColorsChanged$0(wallpaper, which, (com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyWallpaperColorsChanged$0(com.android.server.wallpaper.WallpaperData wallpaper, int which, com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector) {
        notifyWallpaperColorsChangedOnDisplay(wallpaper, connector.mDisplayId, which);
    }

    private android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback> getWallpaperCallbacks(int userId, int displayId) {
        android.util.SparseArray<android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback>> displayListeners = this.mColorsChangedListeners.get(userId);
        if (displayListeners == null) {
            return null;
        }
        android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback> listeners = displayListeners.get(displayId);
        return listeners;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyWallpaperColorsChangedOnDisplay(com.android.server.wallpaper.WallpaperData wallpaper, int displayId) {
        notifyWallpaperColorsChangedOnDisplay(wallpaper, displayId, wallpaper.mWhich);
    }

    private void notifyWallpaperColorsChangedOnDisplay(com.android.server.wallpaper.WallpaperData wallpaper, int displayId, int which) {
        synchronized (this.mLock) {
            android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback> currentUserColorListeners = getWallpaperCallbacks(wallpaper.userId, displayId);
            android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback> userAllColorListeners = getWallpaperCallbacks(-1, displayId);
            if (emptyCallbackList(currentUserColorListeners) && emptyCallbackList(userAllColorListeners)) {
                if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                    android.util.Slog.w(TAG, "notifyWallpaperColorsChangedOnDisplay, wallpaper: " + wallpaper + ", displayId: " + displayId + ",  emptyCallbackList, return");
                }
                return;
            }
            if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                android.util.Slog.v(TAG, "notifyWallpaperColorsChangedOnDisplay " + wallpaper.mWhich);
            }
            boolean needsExtraction = wallpaper.primaryColors == null || wallpaper.mIsColorExtractedFromDim;
            boolean notify = true;
            if (needsExtraction) {
                notify = extractColors(wallpaper);
            }
            if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                android.util.Slog.v(TAG, "notifyWallpaperColorsChangedOnDisplay, needsExtraction: " + needsExtraction + ", displayId: " + displayId + ",  needsExtraction: " + needsExtraction + ", notify: " + notify);
            }
            if (notify) {
                notifyColorListeners(getAdjustedWallpaperColorsOnDimming(wallpaper), which, wallpaper.userId, displayId);
            }
        }
    }

    private static <T extends android.os.IInterface> boolean emptyCallbackList(android.os.RemoteCallbackList<T> list) {
        return list == null || list.getRegisteredCallbackCount() == 0;
    }

    private void notifyColorListeners(android.app.WallpaperColors wallpaperColors, int which, int userId, int displayId) {
        java.util.ArrayList<android.app.IWallpaperManagerCallback> colorListeners = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                android.util.Slog.v(TAG, "notifyColorListeners, wallpaperColors: " + wallpaperColors + ", which: " + which + ",  userId: " + userId);
            }
            android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback> currentUserColorListeners = getWallpaperCallbacks(userId, displayId);
            android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback> userAllColorListeners = getWallpaperCallbacks(-1, displayId);
            if (currentUserColorListeners != null) {
                int count = currentUserColorListeners.beginBroadcast();
                for (int i = 0; i < count; i++) {
                    colorListeners.add(currentUserColorListeners.getBroadcastItem(i));
                }
                currentUserColorListeners.finishBroadcast();
            }
            if (userAllColorListeners != null) {
                int count2 = userAllColorListeners.beginBroadcast();
                for (int i2 = 0; i2 < count2; i2++) {
                    colorListeners.add(userAllColorListeners.getBroadcastItem(i2));
                }
                userAllColorListeners.finishBroadcast();
            }
        }
        int count3 = colorListeners.size();
        if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
            android.util.Slog.w(TAG, "notifyColorListeners, wallpaperColors: " + wallpaperColors + ", which: " + which + ",  userId: " + userId + ", count: " + count3);
        }
        for (int i3 = 0; i3 < count3; i3++) {
            try {
                colorListeners.get(i3).onWallpaperColorsChanged(wallpaperColors, which, userId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "onWallpaperColorsChanged() threw an exception", e);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:61:0x009d  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00a5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean extractColors(com.android.server.wallpaper.WallpaperData r11) {
        /*
            Method dump skipped, instruction units count: 219
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wallpaper.WallpaperManagerService.extractColors(com.android.server.wallpaper.WallpaperData):boolean");
    }

    private android.app.WallpaperColors extractDefaultImageWallpaperColors(com.android.server.wallpaper.WallpaperData wallpaper) {
        android.app.WallpaperColors colors;
        java.io.InputStream is;
        if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
            android.util.Slog.d(TAG, "Extract default image wallpaper colors");
        }
        synchronized (this.mLock) {
            if (this.mCacheDefaultImageWallpaperColors != null) {
                return this.mCacheDefaultImageWallpaperColors;
            }
            float dimAmount = wallpaper.mWallpaperDimAmount;
            android.app.WallpaperColors colors2 = null;
            try {
                is = android.app.WallpaperManager.openDefaultWallpaper(this.mContext, 1);
                try {
                } finally {
                }
            } catch (java.io.IOException e) {
                android.util.Slog.w(TAG, "Can't close default wallpaper stream", e);
                colors = null;
            } catch (java.lang.OutOfMemoryError e2) {
                android.util.Slog.w(TAG, "Can't decode default wallpaper stream", e2);
            }
            if (is == null) {
                android.util.Slog.w(TAG, "Can't open default wallpaper stream");
                if (is != null) {
                    is.close();
                }
                return null;
            }
            android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
            android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeStream(is, null, options);
            if (bitmap != null) {
                colors2 = android.app.WallpaperColors.fromBitmap(bitmap, dimAmount);
                bitmap.recycle();
            }
            if (is != null) {
                is.close();
            }
            colors = colors2;
            if (colors == null) {
                android.util.Slog.e(TAG, "Extract default image wallpaper colors failed");
            } else {
                synchronized (this.mLock) {
                    this.mCacheDefaultImageWallpaperColors = colors;
                }
            }
            return colors;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean supportsMultiDisplay(com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection connection) {
        if (connection != null) {
            return connection.mInfo == null || connection.mInfo.supportsMultipleDisplays();
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateFallbackConnection() {
        if (this.mLastWallpaper == null || this.mFallbackWallpaper == null) {
            return;
        }
        com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection systemConnection = this.mLastWallpaper.connection;
        final com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection fallbackConnection = this.mFallbackWallpaper.connection;
        if (fallbackConnection == null) {
            android.util.Slog.w(TAG, "Fallback wallpaper connection has not been created yet!!");
            return;
        }
        if (supportsMultiDisplay(systemConnection)) {
            if (fallbackConnection.mDisplayConnector.size() != 0) {
                fallbackConnection.forEachDisplayConnector(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda2
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.wallpaper.WallpaperManagerService.lambda$updateFallbackConnection$1(fallbackConnection, (com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) obj);
                    }
                });
                fallbackConnection.mDisplayConnector.clear();
                return;
            }
            return;
        }
        fallbackConnection.appendConnectorWithCondition(new java.util.function.Predicate() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$updateFallbackConnection$2(fallbackConnection, (android.view.Display) obj);
            }
        });
        fallbackConnection.forEachDisplayConnector(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) throws android.os.RemoteException {
                this.f$0.lambda$updateFallbackConnection$3(fallbackConnection, (com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) obj);
            }
        });
    }

    static /* synthetic */ void lambda$updateFallbackConnection$1(com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection fallbackConnection, com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector) {
        if (connector.mEngine != null) {
            connector.disconnectLocked(fallbackConnection);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$updateFallbackConnection$2(com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection fallbackConnection, android.view.Display display) {
        return this.mWallpaperDisplayHelper.isUsableDisplay(display, fallbackConnection.mClientUid) && display.getDisplayId() != 0 && this.mWallpaperManagerServiceWrapper.getExtImpl().isAvailableFallbackDisplay(display) && !fallbackConnection.containsDisplay(display.getDisplayId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateFallbackConnection$3(com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection fallbackConnection, com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector) throws android.os.RemoteException {
        if (connector.mEngine == null) {
            connector.connectLocked(fallbackConnection, this.mFallbackWallpaper);
        }
    }

    final class DisplayConnector {
        boolean mDimensionsChanged;
        final int mDisplayId;
        android.service.wallpaper.IWallpaperEngine mEngine;
        boolean mPaddingChanged;
        final android.os.Binder mToken = new android.os.Binder();

        DisplayConnector(int displayId) {
            this.mDisplayId = displayId;
            android.util.Slog.i(com.android.server.wallpaper.WallpaperManagerService.TAG, "DisplayConnector mToken " + this.mToken + " mDisplayId " + this.mDisplayId);
        }

        void ensureStatusHandled() {
            com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData wpdData = com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperDisplayHelper.getDisplayDataOrCreate(this.mDisplayId);
            if (this.mDimensionsChanged) {
                try {
                    this.mEngine.setDesiredSize(wpdData.mWidth, wpdData.mHeight);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "Failed to set wallpaper dimensions", e);
                }
                this.mDimensionsChanged = false;
            }
            if (this.mPaddingChanged) {
                try {
                    this.mEngine.setDisplayPadding(wpdData.mPadding);
                } catch (android.os.RemoteException e2) {
                    android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "Failed to set wallpaper padding", e2);
                }
                this.mPaddingChanged = false;
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r3v14 */
        /* JADX WARN: Type inference failed for: r3v15 */
        /* JADX WARN: Type inference failed for: r3v6 */
        /* JADX WARN: Type inference failed for: r3v7, types: [boolean, int] */
        public void connectLocked(com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection connection, com.android.server.wallpaper.WallpaperData wallpaper) throws android.os.RemoteException {
            ?? r3;
            android.content.ComponentName componentName;
            com.android.server.utils.TimingsTraceAndSlog t;
            if (connection.mService == null) {
                android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "WallpaperService is not connected yet");
                return;
            }
            com.android.server.utils.TimingsTraceAndSlog t2 = new com.android.server.utils.TimingsTraceAndSlog(com.android.server.wallpaper.WallpaperManagerService.TAG);
            t2.traceBegin("WPMS.connectLocked-" + wallpaper.wallpaperComponent);
            android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "Adding window token: " + this.mToken);
            com.android.server.wallpaper.WallpaperManagerService.this.mWindowManagerInternal.addWindowToken(this.mToken, 2013, this.mDisplayId, null);
            com.android.server.wallpaper.WallpaperManagerService.this.mWindowManagerInternal.setWallpaperShowWhenLocked(this.mToken, (wallpaper.mWhich & 2) != 0);
            if (!com.android.window.flags.Flags.multiCrop() || !com.android.server.wallpaper.WallpaperManagerService.this.mImageWallpaper.equals(wallpaper.wallpaperComponent)) {
                com.android.server.wallpaper.WallpaperManagerService.this.mWindowManagerInternal.setWallpaperCropHints(this.mToken, new android.util.SparseArray<>());
            } else {
                com.android.server.wallpaper.WallpaperManagerService.this.mWindowManagerInternal.setWallpaperCropHints(this.mToken, com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperCropper.getRelativeCropHints(wallpaper));
            }
            com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData wpdData = com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperDisplayHelper.getDisplayDataOrCreate(this.mDisplayId);
            try {
                t = t2;
                try {
                    connection.mService.attach(connection, this.mToken, 2013, false, wpdData.mWidth, wpdData.mHeight, wpdData.mPadding, this.mDisplayId, wallpaper.mWhich, connection.mInfo);
                    componentName = null;
                    r3 = 0;
                    try {
                        com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onServiceAttachedLocked(false, 0, null);
                    } catch (android.os.RemoteException e) {
                        e = e;
                        android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "Failed attaching wallpaper on display", e);
                        if (wallpaper != null && !wallpaper.wallpaperUpdating && connection.getConnectedEngineSize() == 0) {
                            boolean revertToImage = com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onServiceAttachedLocked(true, wallpaper.userId, wallpaper.wallpaperComponent);
                            if (revertToImage) {
                                componentName = com.android.server.wallpaper.WallpaperManagerService.this.mImageWallpaper;
                            }
                            android.content.ComponentName componentName2 = componentName;
                            com.android.server.wallpaper.WallpaperManagerService.this.bindWallpaperComponentLocked(componentName2, false, false, wallpaper, null);
                        } else {
                            com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onServiceAttachedLocked(r3, r3, componentName);
                        }
                    }
                } catch (android.os.RemoteException e2) {
                    e = e2;
                    componentName = null;
                    r3 = 0;
                }
            } catch (android.os.RemoteException e3) {
                e = e3;
                r3 = 0;
                componentName = null;
                t = t2;
            }
            t.traceEnd();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void disconnectLocked(com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection connection) {
            android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "Removing window token: " + this.mToken);
            com.android.server.wallpaper.WallpaperManagerService.this.mWindowManagerInternal.removeWindowToken(this.mToken, false, this.mDisplayId);
            try {
                if (connection.mService != null) {
                    connection.mService.detach(this.mToken);
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "connection.mService.destroy() threw a RemoteException", e);
            }
            this.mEngine = null;
        }
    }

    class WallpaperConnection extends android.service.wallpaper.IWallpaperConnection.Stub implements android.content.ServiceConnection {
        private static final long WALLPAPER_RECONNECT_TIMEOUT_MS = 20000;
        final int mClientUid;
        final android.app.WallpaperInfo mInfo;
        android.os.IRemoteCallback mReply;
        android.service.wallpaper.IWallpaperService mService;
        com.android.server.wallpaper.WallpaperData mWallpaper;
        private final android.util.SparseArray<com.android.server.wallpaper.WallpaperManagerService.DisplayConnector> mDisplayConnector = new android.util.SparseArray<>();
        private int mLmkLimitRebindRetries = 6;
        private int mCrashRebindRetries = 5;
        private boolean mNeedTryToRebind = false;
        private java.lang.Runnable mResetRunnable = new java.lang.Runnable() { // from class: com.android.server.wallpaper.WallpaperManagerService$WallpaperConnection$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        };
        private java.lang.Runnable mTryToRebindRunnable = new java.lang.Runnable() { // from class: com.android.server.wallpaper.WallpaperManagerService$WallpaperConnection$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.tryToRebind();
            }
        };
        private java.lang.Runnable mDisconnectRunnable = new java.lang.Runnable() { // from class: com.android.server.wallpaper.WallpaperManagerService$WallpaperConnection$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$5();
            }
        };

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$0() {
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                if (com.android.server.wallpaper.WallpaperManagerService.this.mShuttingDown) {
                    android.util.Slog.i(com.android.server.wallpaper.WallpaperManagerService.TAG, "Ignoring relaunch timeout during shutdown");
                    return;
                }
                if (!this.mWallpaper.wallpaperUpdating && this.mWallpaper.userId == com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId) {
                    boolean needTryToRebind = this.mNeedTryToRebind;
                    this.mNeedTryToRebind = false;
                    if (com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().rebindWallpaperIfReset(this.mWallpaper, needTryToRebind)) {
                        android.util.Slog.i(com.android.server.wallpaper.WallpaperManagerService.TAG, "Wallpaper reconnect timed out for " + this.mWallpaper.wallpaperComponent + ", try to rebind");
                        return;
                    }
                    android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "Wallpaper reconnect timed out for " + this.mWallpaper.wallpaperComponent + ", reverting to built-in wallpaper!");
                    com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onWallpaperClearEvent(this.mWallpaper.wallpaperComponent, this.mWallpaper.mWhich, this.mWallpaper.userId, "reconnect timed out");
                    if (!com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperLockedForComponent(1, this.mWallpaper.userId, false, null, this.mWallpaper.wallpaperComponent)) {
                        com.android.server.wallpaper.WallpaperManagerService.this.clearWallpaperLocked(this.mWallpaper.mWhich, this.mWallpaper.userId, false, null);
                    }
                }
            }
        }

        WallpaperConnection(android.app.WallpaperInfo info, com.android.server.wallpaper.WallpaperData wallpaper, int clientUid) {
            this.mInfo = info;
            this.mWallpaper = wallpaper;
            this.mClientUid = clientUid;
            initDisplayState();
            android.util.Slog.i(com.android.server.wallpaper.WallpaperManagerService.TAG, "new WallpaperConnection " + this + "\nInfo " + this.mInfo + "\n mWallpaper " + this.mWallpaper + "\n mDisplayConnector " + this.mDisplayConnector);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void initDisplayState() {
            if (!this.mWallpaper.equals(com.android.server.wallpaper.WallpaperManagerService.this.mFallbackWallpaper)) {
                if (com.android.server.wallpaper.WallpaperManagerService.this.supportsMultiDisplay(this)) {
                    appendConnectorWithCondition(new java.util.function.Predicate() { // from class: com.android.server.wallpaper.WallpaperManagerService$WallpaperConnection$$ExternalSyntheticLambda3
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return this.f$0.lambda$initDisplayState$1((android.view.Display) obj);
                        }
                    });
                } else {
                    this.mDisplayConnector.append(0, com.android.server.wallpaper.WallpaperManagerService.this.new DisplayConnector(0));
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ boolean lambda$initDisplayState$1(android.view.Display display) {
            return com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperDisplayHelper.isUsableDisplay(display, this.mClientUid);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void appendConnectorWithCondition(java.util.function.Predicate<android.view.Display> tester) {
            android.view.Display[] displays = com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperDisplayHelper.getDisplays(this.mWallpaper);
            for (android.view.Display display : displays) {
                if (tester.test(display)) {
                    int displayId = display.getDisplayId();
                    com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector = this.mDisplayConnector.get(displayId);
                    if (connector == null) {
                        this.mDisplayConnector.append(displayId, com.android.server.wallpaper.WallpaperManagerService.this.new DisplayConnector(displayId));
                    }
                }
            }
        }

        void forEachDisplayConnector(java.util.function.Consumer<com.android.server.wallpaper.WallpaperManagerService.DisplayConnector> action) {
            if (com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().forEachAvailableDisplayConnector(this.mDisplayConnector, this, action)) {
                return;
            }
            for (int i = this.mDisplayConnector.size() - 1; i >= 0; i--) {
                com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector = this.mDisplayConnector.valueAt(i);
                action.accept(connector);
            }
        }

        int getConnectedEngineSize() {
            int engineSize = 0;
            for (int i = this.mDisplayConnector.size() - 1; i >= 0; i--) {
                com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector = this.mDisplayConnector.valueAt(i);
                if (connector.mEngine != null) {
                    engineSize++;
                }
            }
            return engineSize;
        }

        com.android.server.wallpaper.WallpaperManagerService.DisplayConnector getDisplayConnectorOrCreate(int displayId) {
            com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector = this.mDisplayConnector.get(displayId);
            if (connector == null && com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperDisplayHelper.isUsableDisplay(displayId, this.mClientUid)) {
                com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector2 = com.android.server.wallpaper.WallpaperManagerService.this.new DisplayConnector(displayId);
                this.mDisplayConnector.append(displayId, connector2);
                return connector2;
            }
            return connector;
        }

        boolean containsDisplay(int displayId) {
            return this.mDisplayConnector.get(displayId) != null;
        }

        void removeDisplayConnector(int displayId) {
            com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector = this.mDisplayConnector.get(displayId);
            if (connector != null) {
                this.mDisplayConnector.remove(displayId);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog(com.android.server.wallpaper.WallpaperManagerService.TAG);
            t.traceBegin("WPMS.onServiceConnected-" + name);
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                if (this.mWallpaper.connection == this) {
                    this.mService = android.service.wallpaper.IWallpaperService.Stub.asInterface(service);
                    if (com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().needAttachService(this.mWallpaper)) {
                        if (this.mWallpaper.userId == com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId) {
                            com.android.server.wallpaper.WallpaperManagerService.this.attachServiceLocked(this, this.mWallpaper);
                        } else {
                            android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "shouldn't attach, wallpaper userId " + this.mWallpaper.userId + ", for mCurrentUserId " + com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId + ", mReply: " + this.mReply);
                            engineShown(null);
                        }
                    }
                    if (!this.mWallpaper.equals(com.android.server.wallpaper.WallpaperManagerService.this.mFallbackWallpaper) && !com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().saveSettingsLockedOnServiceConnected(this.mWallpaper.userId, this.mWallpaper.mWallpaperDataExt.getPhysicalDisplayId())) {
                        com.android.server.wallpaper.WallpaperManagerService.this.saveSettingsLocked(this.mWallpaper.userId);
                    }
                    com.android.server.FgThread.getHandler().removeCallbacks(this.mResetRunnable);
                    com.android.server.wallpaper.WallpaperManagerService.this.mContext.getMainThreadHandler().removeCallbacks(this.mTryToRebindRunnable);
                    com.android.server.wallpaper.WallpaperManagerService.this.mContext.getMainThreadHandler().removeCallbacks(this.mDisconnectRunnable);
                }
            }
            t.traceEnd();
        }

        public void onLocalWallpaperColorsChanged(final android.graphics.RectF area, final android.app.WallpaperColors colors, final int displayId) {
            forEachDisplayConnector(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$WallpaperConnection$$ExternalSyntheticLambda5
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$onLocalWallpaperColorsChanged$3(area, colors, displayId, (com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLocalWallpaperColorsChanged$3(final android.graphics.RectF area, final android.app.WallpaperColors colors, int displayId, com.android.server.wallpaper.WallpaperManagerService.DisplayConnector displayConnector) {
            java.util.function.Consumer<android.app.ILocalWallpaperColorConsumer> callback = new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$WallpaperConnection$$ExternalSyntheticLambda4
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection.lambda$onLocalWallpaperColorsChanged$2(area, colors, (android.app.ILocalWallpaperColorConsumer) obj);
                }
            };
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                com.android.server.wallpaper.WallpaperManagerService.this.mLocalColorRepo.forEachCallback(callback, area, displayId);
            }
        }

        static /* synthetic */ void lambda$onLocalWallpaperColorsChanged$2(android.graphics.RectF area, android.app.WallpaperColors colors, android.app.ILocalWallpaperColorConsumer cb) {
            try {
                cb.onColorsChanged(area, colors);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "Failed to notify local color callbacks", e);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "Wallpaper service gone: " + name);
                if (!java.util.Objects.equals(name, this.mWallpaper.wallpaperComponent)) {
                    android.util.Slog.e(com.android.server.wallpaper.WallpaperManagerService.TAG, "Does not match expected wallpaper component " + this.mWallpaper.wallpaperComponent);
                }
                this.mService = null;
                forEachDisplayConnector(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$WallpaperConnection$$ExternalSyntheticLambda6
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$onServiceDisconnected$4((com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) obj);
                    }
                });
                if (this.mWallpaper.connection == this && !this.mWallpaper.wallpaperUpdating) {
                    com.android.server.wallpaper.WallpaperManagerService.this.mContext.getMainThreadHandler().postDelayed(this.mDisconnectRunnable, 1000L);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onServiceDisconnected$4(com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector) {
            com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceExt.removeEngineIfDisconnected(connector);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void scheduleTimeoutLocked() {
            android.os.Handler fgHandler = com.android.server.FgThread.getHandler();
            fgHandler.removeCallbacks(this.mResetRunnable);
            fgHandler.postDelayed(this.mResetRunnable, WALLPAPER_RECONNECT_TIMEOUT_MS);
            android.util.Slog.i(com.android.server.wallpaper.WallpaperManagerService.TAG, "Started wallpaper reconnect timeout for " + this.mWallpaper.wallpaperComponent);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void tryToRebind() {
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                if (this.mWallpaper.wallpaperUpdating) {
                    return;
                }
                android.content.ComponentName wpService = this.mWallpaper.wallpaperComponent;
                this.mWallpaper.mBindSource = com.android.server.wallpaper.WallpaperData.BindSource.CONNECTION_TRY_TO_REBIND;
                if (com.android.server.wallpaper.WallpaperManagerService.this.bindWallpaperComponentLocked(wpService, true, false, this.mWallpaper, null)) {
                    this.mNeedTryToRebind = true;
                    if (this.mWallpaper.connection != null) {
                        this.mWallpaper.connection.scheduleTimeoutLocked();
                    }
                } else if (android.os.SystemClock.uptimeMillis() - this.mWallpaper.lastDiedTime < WALLPAPER_RECONNECT_TIMEOUT_MS) {
                    android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "Rebind fail! Try again later");
                    com.android.server.wallpaper.WallpaperManagerService.this.mContext.getMainThreadHandler().postDelayed(this.mTryToRebindRunnable, 1000L);
                } else {
                    android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "Reverting to built-in wallpaper!");
                    com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onWallpaperClearEvent(this.mWallpaper.wallpaperComponent, this.mWallpaper.mWhich, this.mWallpaper.userId, "bind fail timeout");
                    if (!com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperLockedForComponent(1, this.mWallpaper.userId, false, null, this.mWallpaper.wallpaperComponent)) {
                        com.android.server.wallpaper.WallpaperManagerService.this.clearWallpaperLocked(this.mWallpaper.mWhich, this.mWallpaper.userId, false, null);
                    }
                    java.lang.String flattened = wpService.flattenToString();
                    android.util.EventLog.writeEvent(com.android.server.EventLogTags.WP_WALLPAPER_CRASHED, flattened.substring(0, java.lang.Math.min(flattened.length(), 128)));
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$new$5() {
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                if (this == this.mWallpaper.connection) {
                    android.content.ComponentName wpService = this.mWallpaper.wallpaperComponent;
                    if (!this.mWallpaper.wallpaperUpdating && this.mWallpaper.userId == com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId && !java.util.Objects.equals(com.android.server.wallpaper.WallpaperManagerService.this.mImageWallpaper, wpService)) {
                        java.util.List<android.app.ApplicationExitInfo> reasonList = com.android.server.wallpaper.WallpaperManagerService.this.mActivityManager.getHistoricalProcessExitReasons(wpService.getPackageName(), 0, 1);
                        int exitReason = 0;
                        if (reasonList != null && !reasonList.isEmpty()) {
                            android.app.ApplicationExitInfo info = reasonList.get(0);
                            exitReason = info.getReason();
                        }
                        android.util.Slog.d(com.android.server.wallpaper.WallpaperManagerService.TAG, "exitReason: " + exitReason);
                        if (exitReason == 3) {
                            if (isRunningOnLowMemory()) {
                                android.util.Slog.i(com.android.server.wallpaper.WallpaperManagerService.TAG, "Rebind is delayed due to lmk");
                                com.android.server.wallpaper.WallpaperManagerService.this.mContext.getMainThreadHandler().postDelayed(this.mTryToRebindRunnable, com.android.server.wallpaper.WallpaperManagerService.LMK_RECONNECT_DELAY_MS);
                                this.mLmkLimitRebindRetries = 6;
                            } else {
                                if (this.mLmkLimitRebindRetries <= 0) {
                                    android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "Reverting to built-in wallpaper due to lmk!");
                                    com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onWallpaperClearEvent(this.mWallpaper.wallpaperComponent, this.mWallpaper.mWhich, this.mWallpaper.userId, "lmk");
                                    com.android.server.wallpaper.WallpaperManagerService.this.clearWallpaperLocked(this.mWallpaper.mWhich, this.mWallpaper.userId, false, null);
                                    this.mLmkLimitRebindRetries = 6;
                                    return;
                                }
                                this.mLmkLimitRebindRetries--;
                                com.android.server.wallpaper.WallpaperManagerService.this.mContext.getMainThreadHandler().postDelayed(this.mTryToRebindRunnable, com.android.server.wallpaper.WallpaperManagerService.LMK_RECONNECT_DELAY_MS);
                            }
                        } else if (this.mWallpaper.lastDiedTime != 0 && this.mWallpaper.lastDiedTime + 30000 > android.os.SystemClock.uptimeMillis()) {
                            if (com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().delayRebindOnCrashTimeout(this.mWallpaper, this)) {
                                return;
                            }
                            if (this.mCrashRebindRetries > 0) {
                                this.mCrashRebindRetries--;
                                com.android.server.wallpaper.WallpaperManagerService.this.mContext.getMainThreadHandler().post(this.mTryToRebindRunnable);
                            } else {
                                this.mCrashRebindRetries = 5;
                                android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "Reverting to built-in wallpaper!");
                                if (!com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperLockedForComponent(1, this.mWallpaper.userId, false, null, this.mWallpaper.wallpaperComponent)) {
                                    com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onWallpaperClearEvent(this.mWallpaper.wallpaperComponent, 1, this.mWallpaper.userId, exitReason + ", shut down twice");
                                    com.android.server.wallpaper.WallpaperManagerService.this.clearWallpaperLocked(1, this.mWallpaper.userId, false, null);
                                }
                            }
                        } else {
                            this.mWallpaper.lastDiedTime = android.os.SystemClock.uptimeMillis();
                            this.mCrashRebindRetries = 5;
                            com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().updateWallpaperBeforeTryingToRebind(this.mWallpaper);
                            tryToRebind();
                        }
                    }
                } else {
                    android.util.Slog.i(com.android.server.wallpaper.WallpaperManagerService.TAG, "Wallpaper changed during disconnect tracking; ignoring");
                }
            }
        }

        private boolean isRunningOnLowMemory() {
            android.app.ActivityManager.MemoryInfo memoryInfo = new android.app.ActivityManager.MemoryInfo();
            com.android.server.wallpaper.WallpaperManagerService.this.mActivityManager.getMemoryInfo(memoryInfo);
            double availableMBsInPercentage = (memoryInfo.availMem / memoryInfo.totalMem) * 100.0d;
            return availableMBsInPercentage < com.android.server.wallpaper.WallpaperManagerService.LMK_LOW_THRESHOLD_MEMORY_PERCENTAGE;
        }

        public void onWallpaperColorsChanged(android.app.WallpaperColors primaryColors, int displayId) {
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                    android.util.Slog.d(com.android.server.wallpaper.WallpaperManagerService.TAG, "onWallpaperColorsChanged primaryColors: " + primaryColors + ", displayId: " + displayId + ", wallpaper: " + this.mWallpaper);
                }
                boolean isImageWallpaper = com.android.server.wallpaper.WallpaperManagerService.this.mImageWallpaper.equals(this.mWallpaper.wallpaperComponent);
                if (isImageWallpaper && (!com.android.window.flags.Flags.offloadColorExtraction() || primaryColors == null)) {
                    if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                        android.util.Slog.d(com.android.server.wallpaper.WallpaperManagerService.TAG, "onWallpaperColorsChanged, return");
                    }
                    return;
                }
                this.mWallpaper.primaryColors = primaryColors;
                com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().copyWallpaperColorsToOtherPhysicalDisplaysLocked(this.mWallpaper);
                if (com.android.window.flags.Flags.offloadColorExtraction() && isImageWallpaper) {
                    com.android.server.wallpaper.WallpaperManagerService.this.saveSettingsLocked(this.mWallpaper.userId);
                }
                com.android.server.wallpaper.WallpaperManagerService.this.notifyWallpaperColorsChangedOnDisplay(this.mWallpaper, displayId);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x0042 A[Catch: RemoteException -> 0x0048, all -> 0x0095, TRY_LEAVE, TryCatch #4 {RemoteException -> 0x0048, blocks: (B:15:0x0036, B:17:0x003c, B:19:0x0042), top: B:52:0x0036, outer: #1 }] */
        /* JADX WARN: Removed duplicated region for block: B:25:0x005c A[Catch: all -> 0x0095, TRY_LEAVE, TryCatch #1 {, blocks: (B:4:0x0007, B:6:0x000d, B:8:0x0016, B:11:0x0020, B:14:0x002f, B:15:0x0036, B:17:0x003c, B:23:0x0050, B:25:0x005c, B:27:0x0062, B:30:0x0069, B:31:0x0070, B:34:0x0079, B:38:0x008b, B:37:0x0084, B:19:0x0042, B:22:0x0049, B:40:0x008d, B:41:0x0094), top: B:47:0x0007, inners: #0, #2, #3, #4 }] */
        /* JADX WARN: Removed duplicated region for block: B:31:0x0070 A[Catch: all -> 0x0095, TRY_LEAVE, TryCatch #1 {, blocks: (B:4:0x0007, B:6:0x000d, B:8:0x0016, B:11:0x0020, B:14:0x002f, B:15:0x0036, B:17:0x003c, B:23:0x0050, B:25:0x005c, B:27:0x0062, B:30:0x0069, B:31:0x0070, B:34:0x0079, B:38:0x008b, B:37:0x0084, B:19:0x0042, B:22:0x0049, B:40:0x008d, B:41:0x0094), top: B:47:0x0007, inners: #0, #2, #3, #4 }] */
        /* JADX WARN: Removed duplicated region for block: B:45:0x0079 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void attachEngine(android.service.wallpaper.IWallpaperEngine r7, int r8) {
            /*
                r6 = this;
                com.android.server.wallpaper.WallpaperManagerService r0 = com.android.server.wallpaper.WallpaperManagerService.this
                java.lang.Object r0 = com.android.server.wallpaper.WallpaperManagerService.m10295$$Nest$fgetmLock(r0)
                monitor-enter(r0)
                com.android.server.wallpaper.WallpaperManagerService$DisplayConnector r1 = r6.getDisplayConnectorOrCreate(r8)     // Catch: java.lang.Throwable -> L95
                if (r1 == 0) goto L8d
                r1.mEngine = r7     // Catch: java.lang.Throwable -> L95
                r1.ensureStatusHandled()     // Catch: java.lang.Throwable -> L95
                android.app.WallpaperInfo r2 = r6.mInfo     // Catch: java.lang.Throwable -> L95
                if (r2 == 0) goto L36
                android.app.WallpaperInfo r2 = r6.mInfo     // Catch: java.lang.Throwable -> L95
                boolean r2 = r2.supportsAmbientMode()     // Catch: java.lang.Throwable -> L95
                if (r2 == 0) goto L36
                if (r8 != 0) goto L36
                android.service.wallpaper.IWallpaperEngine r2 = r1.mEngine     // Catch: android.os.RemoteException -> L2e java.lang.Throwable -> L95
                com.android.server.wallpaper.WallpaperManagerService r3 = com.android.server.wallpaper.WallpaperManagerService.this     // Catch: android.os.RemoteException -> L2e java.lang.Throwable -> L95
                boolean r3 = com.android.server.wallpaper.WallpaperManagerService.m10293$$Nest$fgetmInAmbientMode(r3)     // Catch: android.os.RemoteException -> L2e java.lang.Throwable -> L95
                r4 = 0
                r2.setInAmbientMode(r3, r4)     // Catch: android.os.RemoteException -> L2e java.lang.Throwable -> L95
                goto L36
            L2e:
                r2 = move-exception
                java.lang.String r3 = "WallpaperManagerService"
                java.lang.String r4 = "Failed to set ambient mode state"
                android.util.Slog.w(r3, r4, r2)     // Catch: java.lang.Throwable -> L95
            L36:
                boolean r2 = com.android.window.flags.Flags.offloadColorExtraction()     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L95
                if (r2 == 0) goto L42
                com.android.server.wallpaper.WallpaperData r2 = r6.mWallpaper     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L95
                android.app.WallpaperColors r2 = r2.primaryColors     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L95
                if (r2 != 0) goto L47
            L42:
                android.service.wallpaper.IWallpaperEngine r2 = r1.mEngine     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L95
                r2.requestWallpaperColors()     // Catch: android.os.RemoteException -> L48 java.lang.Throwable -> L95
            L47:
                goto L50
            L48:
                r2 = move-exception
                java.lang.String r3 = "WallpaperManagerService"
                java.lang.String r4 = "Failed to request wallpaper colors"
                android.util.Slog.w(r3, r4, r2)     // Catch: java.lang.Throwable -> L95
            L50:
                com.android.server.wallpaper.WallpaperManagerService r2 = com.android.server.wallpaper.WallpaperManagerService.this     // Catch: java.lang.Throwable -> L95
                com.android.server.wallpaper.LocalColorRepository r2 = com.android.server.wallpaper.WallpaperManagerService.m10294$$Nest$fgetmLocalColorRepo(r2)     // Catch: java.lang.Throwable -> L95
                java.util.List r2 = r2.getAreasByDisplayId(r8)     // Catch: java.lang.Throwable -> L95
                if (r2 == 0) goto L70
                int r3 = r2.size()     // Catch: java.lang.Throwable -> L95
                if (r3 == 0) goto L70
                android.service.wallpaper.IWallpaperEngine r3 = r1.mEngine     // Catch: android.os.RemoteException -> L68 java.lang.Throwable -> L95
                r3.addLocalColorsAreas(r2)     // Catch: android.os.RemoteException -> L68 java.lang.Throwable -> L95
                goto L70
            L68:
                r3 = move-exception
                java.lang.String r4 = "WallpaperManagerService"
                java.lang.String r5 = "Failed to register local colors areas"
                android.util.Slog.w(r4, r5, r3)     // Catch: java.lang.Throwable -> L95
            L70:
                com.android.server.wallpaper.WallpaperData r3 = r6.mWallpaper     // Catch: java.lang.Throwable -> L95
                float r3 = r3.mWallpaperDimAmount     // Catch: java.lang.Throwable -> L95
                r4 = 0
                int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
                if (r3 == 0) goto L8b
                android.service.wallpaper.IWallpaperEngine r3 = r1.mEngine     // Catch: android.os.RemoteException -> L83 java.lang.Throwable -> L95
                com.android.server.wallpaper.WallpaperData r4 = r6.mWallpaper     // Catch: android.os.RemoteException -> L83 java.lang.Throwable -> L95
                float r4 = r4.mWallpaperDimAmount     // Catch: android.os.RemoteException -> L83 java.lang.Throwable -> L95
                r3.applyDimming(r4)     // Catch: android.os.RemoteException -> L83 java.lang.Throwable -> L95
                goto L8b
            L83:
                r3 = move-exception
                java.lang.String r4 = "WallpaperManagerService"
                java.lang.String r5 = "Failed to dim wallpaper"
                android.util.Slog.w(r4, r5, r3)     // Catch: java.lang.Throwable -> L95
            L8b:
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L95
                return
            L8d:
                java.lang.IllegalStateException r2 = new java.lang.IllegalStateException     // Catch: java.lang.Throwable -> L95
                java.lang.String r3 = "Connector has already been destroyed"
                r2.<init>(r3)     // Catch: java.lang.Throwable -> L95
                throw r2     // Catch: java.lang.Throwable -> L95
            L95:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L95
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection.attachEngine(android.service.wallpaper.IWallpaperEngine, int):void");
        }

        public void engineShown(android.service.wallpaper.IWallpaperEngine engine) {
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                if (this.mReply != null) {
                    com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog(com.android.server.wallpaper.WallpaperManagerService.TAG);
                    t.traceBegin("WPMS.mReply.sendResult");
                    long ident = android.os.Binder.clearCallingIdentity();
                    try {
                        try {
                            this.mReply.sendResult((android.os.Bundle) null);
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.d(com.android.server.wallpaper.WallpaperManagerService.TAG, "Failed to send callback!", e);
                        }
                        t.traceEnd();
                        this.mReply = null;
                    } finally {
                        android.os.Binder.restoreCallingIdentity(ident);
                    }
                }
            }
        }

        public android.os.ParcelFileDescriptor setWallpaper(java.lang.String name) {
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                if (this.mWallpaper.connection != this) {
                    return null;
                }
                return com.android.server.wallpaper.WallpaperManagerService.this.updateWallpaperBitmapLocked(name, this.mWallpaper, null);
            }
        }
    }

    class WallpaperDestinationChangeHandler {
        final com.android.server.wallpaper.WallpaperData mNewWallpaper;
        final com.android.server.wallpaper.WallpaperData mOriginalSystem;

        WallpaperDestinationChangeHandler(com.android.server.wallpaper.WallpaperData newWallpaper) {
            this.mNewWallpaper = newWallpaper;
            com.android.server.wallpaper.WallpaperData sysWp = com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(newWallpaper.mWhich, newWallpaper.mWallpaperDataExt.getPhysicalDisplayId(), com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperMap).get(newWallpaper.userId);
            this.mOriginalSystem = new com.android.server.wallpaper.WallpaperData(sysWp);
            android.util.Slog.d(com.android.server.wallpaper.WallpaperManagerService.TAG, "WallpaperDestinationChangeHandler:  \n mNewWallpaper: " + this.mNewWallpaper + "\n mOriginalSystem: " + this.mOriginalSystem);
        }

        void complete() {
            int phyid = this.mNewWallpaper.mWallpaperDataExt.getPhysicalDisplayId();
            android.util.Slog.d(com.android.server.wallpaper.WallpaperManagerService.TAG, "complete:  \n mNewWallpaper: " + this.mNewWallpaper + "\n mOriginalSystem: " + this.mOriginalSystem);
            if (this.mNewWallpaper.mSystemWasBoth) {
                if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                    android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "Handling change from system+lock wallpaper");
                }
                if (com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperType(this.mNewWallpaper.mWhich) == 1) {
                    boolean originalIsStatic = com.android.server.wallpaper.WallpaperManagerService.this.mImageWallpaper.equals(this.mOriginalSystem.wallpaperComponent);
                    if (originalIsStatic) {
                        com.android.server.wallpaper.WallpaperData lockWp = com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, phyid, com.android.server.wallpaper.WallpaperManagerService.this.mLockWallpaperMap).get(this.mNewWallpaper.userId);
                        android.util.Slog.d(com.android.server.wallpaper.WallpaperManagerService.TAG, "originalIsStatic  lockWp" + lockWp);
                        if (lockWp != null && this.mOriginalSystem.connection != null) {
                            android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "static system+lock to system success");
                            lockWp.wallpaperComponent = this.mOriginalSystem.wallpaperComponent;
                            lockWp.connection = this.mOriginalSystem.connection;
                            lockWp.connection.mWallpaper = lockWp;
                            this.mOriginalSystem.mWhich = 2;
                            com.android.server.wallpaper.WallpaperManagerService.this.updateEngineFlags(this.mOriginalSystem);
                            com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onStaticSystemAndLockToSys(this.mNewWallpaper);
                        } else {
                            android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "static system+lock to system failure");
                            com.android.server.wallpaper.WallpaperData currentSystem = com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(1, phyid, com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperMap).get(this.mNewWallpaper.userId);
                            currentSystem.mWhich = 3;
                            com.android.server.wallpaper.WallpaperManagerService.this.updateEngineFlags(currentSystem);
                            com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, phyid, com.android.server.wallpaper.WallpaperManagerService.this.mLockWallpaperMap).remove(this.mNewWallpaper.userId);
                        }
                    } else {
                        android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "live system+lock to system success");
                        this.mOriginalSystem.mWhich = 2;
                        if (!com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().isSharedConnectionWallpaperWithSameFlag(this.mOriginalSystem)) {
                            com.android.server.wallpaper.WallpaperManagerService.this.updateEngineFlags(this.mOriginalSystem);
                        }
                        com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, phyid, com.android.server.wallpaper.WallpaperManagerService.this.mLockWallpaperMap).put(this.mNewWallpaper.userId, this.mOriginalSystem);
                        com.android.server.wallpaper.WallpaperManagerService.this.mLastLockWallpaper = this.mOriginalSystem;
                        com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onLiveSystemAndLockToSys(this.mNewWallpaper);
                    }
                } else if (com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperType(this.mNewWallpaper.mWhich) == 2) {
                    android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "system+lock to lock");
                    com.android.server.wallpaper.WallpaperData currentSystem2 = com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(1, phyid, com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperMap).get(this.mNewWallpaper.userId);
                    if (currentSystem2.wallpaperId == this.mOriginalSystem.wallpaperId) {
                        currentSystem2.mWhich = 1;
                        if (!com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().isSharedConnectionWallpaperWithSameFlag(currentSystem2)) {
                            com.android.server.wallpaper.WallpaperManagerService.this.updateEngineFlags(currentSystem2);
                        }
                        com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onSystemAndLockToLock(this.mNewWallpaper);
                    }
                }
            }
            int phyId = com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getPhysicalDisplayIdLocked(this.mNewWallpaper.mWhich);
            if (!com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().saveSettingsLocked(this.mNewWallpaper.userId, phyId)) {
                com.android.server.wallpaper.WallpaperManagerService.this.saveSettingsLocked(this.mNewWallpaper.userId);
            }
            android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "--- wallpaper changed --");
            android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "new sysWp: " + com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperMap.get(com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId));
            android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "new lockWp: " + com.android.server.wallpaper.WallpaperManagerService.this.mLockWallpaperMap.get(com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId));
            android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "new lastWp: " + com.android.server.wallpaper.WallpaperManagerService.this.mLastWallpaper);
            android.util.Slog.v(com.android.server.wallpaper.WallpaperManagerService.TAG, "new lastLockWp: " + com.android.server.wallpaper.WallpaperManagerService.this.mLastLockWallpaper);
            com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onWallpaperChange();
        }
    }

    class MyPackageMonitor extends com.android.internal.content.PackageMonitor {
        private MyPackageMonitor() {
            super(true);
        }

        public void onPackageUpdateFinished(java.lang.String packageName, int uid) throws java.lang.Throwable {
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                try {
                    try {
                        if (com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId != getChangingUserId()) {
                            return;
                        }
                        for (com.android.server.wallpaper.WallpaperData wallpaper : com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperForAllPhysicalDisplay(com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId, 1, com.android.server.wallpaper.WallpaperManagerService.this.getWallpapers())) {
                            android.content.ComponentName wpService = wallpaper.wallpaperComponent;
                            if (wpService != null && wpService.getPackageName().equals(packageName)) {
                                android.util.Slog.i(com.android.server.wallpaper.WallpaperManagerService.TAG, "Wallpaper " + wpService + " update has finished");
                                wallpaper.wallpaperUpdating = false;
                                if (!com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperComponentLockedOnPackageUpdated(wallpaper)) {
                                    com.android.server.wallpaper.WallpaperManagerService.this.lambda$clearWallpaperLocked$9(wallpaper);
                                }
                                wallpaper.mBindSource = com.android.server.wallpaper.WallpaperData.BindSource.PACKAGE_UPDATE_FINISHED;
                                if (!com.android.server.wallpaper.WallpaperManagerService.this.bindWallpaperComponentLocked(wpService, false, false, wallpaper, null)) {
                                    android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "Wallpaper " + wpService + " no longer available; reverting to default");
                                    com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onWallpaperClearEvent(wpService, wallpaper.mWhich, wallpaper.userId, "package update finished, no longer available");
                                    if (!com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperLocked(wallpaper.mWhich, wallpaper.userId, false, null, wallpaper.mWallpaperDataExt.getPhysicalDisplayId())) {
                                        com.android.server.wallpaper.WallpaperManagerService.this.clearWallpaperLocked(wallpaper.mWhich, wallpaper.userId, false, null);
                                    }
                                }
                            }
                        }
                        com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().resetOnPackageUpdatedLocked();
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }

        public void onPackageModified(java.lang.String packageName) {
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                if (com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId != getChangingUserId()) {
                    return;
                }
                for (com.android.server.wallpaper.WallpaperData wallpaper : com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperForAllPhysicalDisplay(com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId, 1, new com.android.server.wallpaper.WallpaperData[]{(com.android.server.wallpaper.WallpaperData) com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperMap.get(com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId)})) {
                    if (wallpaper.wallpaperComponent != null && wallpaper.wallpaperComponent.getPackageName().equals(packageName)) {
                        doPackagesChangedLocked(true, wallpaper);
                    }
                }
            }
        }

        public void onPackageUpdateStarted(java.lang.String packageName, int uid) {
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                if (com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId != getChangingUserId()) {
                    return;
                }
                for (com.android.server.wallpaper.WallpaperData wallpaper : com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperForAllPhysicalDisplay(com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId, 1, new com.android.server.wallpaper.WallpaperData[]{(com.android.server.wallpaper.WallpaperData) com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperMap.get(com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId)})) {
                    if (wallpaper.wallpaperComponent != null && wallpaper.wallpaperComponent.getPackageName().equals(packageName)) {
                        android.util.Slog.i(com.android.server.wallpaper.WallpaperManagerService.TAG, "Wallpaper service " + wallpaper.wallpaperComponent + " is updating");
                        wallpaper.wallpaperUpdating = true;
                        if (wallpaper.connection != null) {
                            com.android.server.FgThread.getHandler().removeCallbacks(wallpaper.connection.mResetRunnable);
                        }
                    }
                }
            }
        }

        public boolean onHandleForceStop(android.content.Intent intent, java.lang.String[] packages, int uid, boolean doit) {
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                boolean changed = false;
                if (com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId != getChangingUserId()) {
                    return false;
                }
                for (com.android.server.wallpaper.WallpaperData wallpaper : com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperForAllPhysicalDisplay(com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId, 1, new com.android.server.wallpaper.WallpaperData[]{(com.android.server.wallpaper.WallpaperData) com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperMap.get(com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId)})) {
                    boolean res = doPackagesChangedLocked(doit, wallpaper);
                    changed |= res;
                }
                return changed;
            }
        }

        public void onSomePackagesChanged() {
            synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                if (com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId != getChangingUserId()) {
                    return;
                }
                for (com.android.server.wallpaper.WallpaperData wallpaper : com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperForAllPhysicalDisplay(com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId, 1, new com.android.server.wallpaper.WallpaperData[]{(com.android.server.wallpaper.WallpaperData) com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperMap.get(com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId)})) {
                    doPackagesChangedLocked(true, wallpaper);
                }
            }
        }

        boolean doPackagesChangedLocked(boolean doit, com.android.server.wallpaper.WallpaperData wallpaper) throws android.os.RemoteException {
            int change;
            int change2;
            boolean changed = false;
            if (wallpaper.wallpaperComponent != null && ((change2 = isPackageDisappearing(wallpaper.wallpaperComponent.getPackageName())) == 3 || change2 == 2)) {
                changed = true;
                if (doit) {
                    if (com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().checkServiceAvailable(com.android.server.wallpaper.WallpaperManagerService.this.mContext, com.android.server.wallpaper.WallpaperManagerService.this.mIPackageManager, wallpaper.wallpaperComponent, wallpaper.userId) && com.android.server.wallpaper.WallpaperManagerService.this.bindWallpaperComponentLocked(wallpaper.wallpaperComponent, true, false, wallpaper, null)) {
                        android.util.Slog.i(com.android.server.wallpaper.WallpaperManagerService.TAG, "doPackagesChangedLocked bind successful");
                    } else {
                        android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "Wallpaper uninstalled, removing: " + wallpaper.wallpaperComponent);
                        com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onWallpaperClearEvent(wallpaper.wallpaperComponent, wallpaper.mWhich, wallpaper.userId, "wallpaper uninstalled");
                        if (!com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperLocked(wallpaper.mWhich, wallpaper.userId, false, null, wallpaper.mWallpaperDataExt.getPhysicalDisplayId())) {
                            com.android.server.wallpaper.WallpaperManagerService.this.clearWallpaperLocked(wallpaper.mWhich, wallpaper.userId, false, null);
                        }
                    }
                }
            }
            boolean changed2 = changed;
            if (wallpaper.nextWallpaperComponent != null && ((change = isPackageDisappearing(wallpaper.nextWallpaperComponent.getPackageName())) == 3 || change == 2)) {
                wallpaper.nextWallpaperComponent = null;
            }
            if (wallpaper.wallpaperComponent != null && isPackageModified(wallpaper.wallpaperComponent.getPackageName())) {
                try {
                    if (com.android.server.wallpaper.WallpaperManagerService.this.mIPackageManager != null) {
                        android.content.pm.ServiceInfo si = com.android.server.wallpaper.WallpaperManagerService.this.mIPackageManager.getServiceInfo(wallpaper.wallpaperComponent, 786432L, wallpaper.userId);
                        if (si == null) {
                            android.util.Slog.w(com.android.server.wallpaper.WallpaperManagerService.TAG, "Wallpaper component gone, removing: " + wallpaper.wallpaperComponent);
                            if (!com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperLocked(wallpaper.mWhich, wallpaper.userId, false, null, wallpaper.mWallpaperDataExt.getPhysicalDisplayId())) {
                                com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onWallpaperClearEvent(wallpaper.wallpaperComponent, wallpaper.mWhich, wallpaper.userId, "package changed, wallpaper component gone");
                                com.android.server.wallpaper.WallpaperManagerService.this.clearWallpaperLocked(wallpaper.mWhich, wallpaper.userId, false, null);
                            }
                        }
                    }
                } catch (android.os.RemoteException e) {
                }
            }
            if (wallpaper.nextWallpaperComponent != null && isPackageModified(wallpaper.nextWallpaperComponent.getPackageName())) {
                try {
                    if (com.android.server.wallpaper.WallpaperManagerService.this.mIPackageManager != null) {
                        android.content.pm.ServiceInfo si2 = com.android.server.wallpaper.WallpaperManagerService.this.mIPackageManager.getServiceInfo(wallpaper.nextWallpaperComponent, 786432L, wallpaper.userId);
                        if (si2 == null) {
                            wallpaper.nextWallpaperComponent = null;
                        }
                    }
                } catch (android.os.RemoteException e2) {
                }
            }
            return changed2;
        }
    }

    com.android.server.wallpaper.WallpaperData getCurrentWallpaperData(int which, int userId) {
        com.android.server.wallpaper.WallpaperData wallpaperData;
        synchronized (this.mLock) {
            int wallpaperType = this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(which);
            android.util.SparseArray<com.android.server.wallpaper.WallpaperData> wallpaperDataMap = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(wallpaperType, wallpaperType == 1 ? this.mWallpaperMap : this.mLockWallpaperMap);
            wallpaperData = wallpaperDataMap.get(userId);
        }
        return wallpaperData;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public WallpaperManagerService(android.content.Context context) {
        boolean z;
        java.lang.Object[] objArr = 0;
        java.lang.Object[] objArr2 = 0;
        if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
            android.util.Slog.v(TAG, "WallpaperService startup");
        }
        this.mContext = context;
        this.mShuttingDown = false;
        this.mImageWallpaper = android.content.ComponentName.unflattenFromString(context.getResources().getString(android.R.string.imProtocolAim));
        this.mWindowManagerInternal = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
        this.mPackageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        this.mIPackageManager = android.app.AppGlobals.getPackageManager();
        this.mAppOpsManager = (android.app.AppOpsManager) this.mContext.getSystemService("appops");
        android.hardware.display.DisplayManager displayManager = (android.hardware.display.DisplayManager) this.mContext.getSystemService(android.hardware.display.DisplayManager.class);
        displayManager.registerDisplayListener(this.mDisplayListener, null);
        android.view.WindowManager windowManager = (android.view.WindowManager) this.mContext.getSystemService(android.view.WindowManager.class);
        if (this.mContext.getResources().getIntArray(android.R.array.config_face_acquire_vendor_keyguard_ignorelist).length > 0) {
            z = true;
        } else {
            z = false;
        }
        this.mWallpaperDisplayHelper = new com.android.server.wallpaper.WallpaperDisplayHelper(displayManager, windowManager, this.mWindowManagerInternal, z, this.mWallpaperManagerServiceExt);
        this.mWallpaperCropper = new com.android.server.wallpaper.WallpaperCropper(this.mWallpaperDisplayHelper, this.mWallpaperManagerServiceExt);
        com.android.server.wm.WindowManagerInternal windowManagerInternal = this.mWindowManagerInternal;
        final com.android.server.wallpaper.WallpaperCropper wallpaperCropper = this.mWallpaperCropper;
        java.util.Objects.requireNonNull(wallpaperCropper);
        windowManagerInternal.setWallpaperCropUtils(new com.android.server.wallpaper.WallpaperCropper.WallpaperCropUtils() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda8
            @Override // com.android.server.wallpaper.WallpaperCropper.WallpaperCropUtils
            public final android.graphics.Rect getCrop(android.graphics.Point point, android.graphics.Point point2, android.util.SparseArray sparseArray, boolean z2) {
                return wallpaperCropper.getCrop(point, point2, sparseArray, z2);
            }
        });
        this.mActivityManager = (android.app.ActivityManager) this.mContext.getSystemService(android.app.ActivityManager.class);
        if (this.mContext.getResources().getBoolean(android.R.bool.config_mms_content_disposition_support)) {
            java.lang.String[] stringArray = this.mContext.getResources().getStringArray(android.R.array.fingerprint_acquired_vendor);
            android.util.IntArray intArray = new android.util.IntArray();
            for (java.lang.String str : stringArray) {
                try {
                    intArray.add(this.mContext.getPackageManager().getApplicationInfo(str, 0).uid);
                } catch (java.lang.Exception e) {
                    android.util.Slog.e(TAG, e.toString());
                }
            }
            if (intArray.size() > 0) {
                try {
                    android.app.ActivityManager.getService().registerUidObserverForUids(new android.app.UidObserver() { // from class: com.android.server.wallpaper.WallpaperManagerService.2
                        public void onUidStateChanged(int uid, int procState, long procStateSeq, int capability) {
                            com.android.server.wallpaper.WallpaperManagerService.this.pauseOrResumeRenderingImmediately(procState == 2);
                        }
                    }, 1, 2, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, intArray.toArray());
                } catch (android.os.RemoteException e2) {
                    android.util.Slog.e(TAG, e2.toString());
                }
            }
        }
        this.mMonitor = new com.android.server.wallpaper.WallpaperManagerService.MyPackageMonitor();
        this.mColorsChangedListeners = new android.util.SparseArray<>();
        this.mWallpaperDataParser = new com.android.server.wallpaper.WallpaperDataParser(this.mContext, this.mWallpaperDisplayHelper, this.mWallpaperCropper, this.mWallpaperManagerServiceExt);
        com.android.server.LocalServices.addService(com.android.server.wallpaper.WallpaperManagerInternal.class, new com.android.server.wallpaper.WallpaperManagerService.LocalService());
        this.mWallpaperManagerServiceWrapper.getExtImpl().initExt();
    }

    private final class LocalService extends com.android.server.wallpaper.WallpaperManagerInternal {
        private LocalService() {
        }

        @Override // com.android.server.wallpaper.WallpaperManagerInternal
        public void onDisplayReady(int displayId) {
            com.android.server.wallpaper.WallpaperManagerService.this.onDisplayReadyInternal(displayId);
        }

        @Override // com.android.server.wallpaper.WallpaperManagerInternal
        public void onScreenTurnedOn(int displayId) {
            com.android.server.wallpaper.WallpaperManagerService.this.notifyScreenTurnedOn(displayId);
        }

        @Override // com.android.server.wallpaper.WallpaperManagerInternal
        public void onScreenTurningOn(int displayId) {
            com.android.server.wallpaper.WallpaperManagerService.this.notifyScreenTurningOn(displayId);
        }

        @Override // com.android.server.wallpaper.WallpaperManagerInternal
        public void onKeyguardGoingAway() {
            com.android.server.wallpaper.WallpaperManagerService.this.notifyKeyguardGoingAway();
        }
    }

    void initialize() throws android.os.RemoteException {
        android.content.ComponentName defaultComponent = android.app.WallpaperManager.getDefaultWallpaperComponent(this.mContext);
        this.mDefaultWallpaperComponent = defaultComponent == null ? this.mImageWallpaper : defaultComponent;
        this.mMonitor.register(this.mContext, null, android.os.UserHandle.ALL, true);
        com.android.server.wallpaper.WallpaperUtils.getWallpaperDir(0).mkdirs();
        int curPhysicalDisplayId = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentPhysicalDisplayIdLocked();
        if (!this.mWallpaperManagerServiceWrapper.getExtImpl().loadSettingsLocked(0, false, curPhysicalDisplayId, 3)) {
            loadSettingsLocked(0, false, 3);
        }
        this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperSafeLocked(0, 1, curPhysicalDisplayId);
        this.mWallpaperManagerServiceWrapper.getExtImpl().initSeparateWallpaperForMultiDisplay(this.mContext);
        this.mWallpaperManagerServiceExt.registerLogSwitchObserver(this.mContext);
    }

    protected void finalize() throws java.lang.Throwable {
        super.finalize();
        for (int i = 0; i < this.mWallpaperMap.size(); i++) {
            com.android.server.wallpaper.WallpaperData wallpaper = this.mWallpaperMap.valueAt(i);
            wallpaper.wallpaperObserver.stopWatching();
        }
        this.mWallpaperManagerServiceWrapper.getExtImpl().finalizeSubDisplay();
    }

    void systemReady() throws java.lang.Throwable {
        if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
            android.util.Slog.v(TAG, "systemReady");
        }
        initialize();
        java.util.List<com.android.server.wallpaper.WallpaperData> sysWallpapers = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperForAllPhysicalDisplay(0, 1, new com.android.server.wallpaper.WallpaperData[]{this.mWallpaperMap.get(0)});
        for (com.android.server.wallpaper.WallpaperData wallpaper : sysWallpapers) {
            if (this.mImageWallpaper.equals(wallpaper.nextWallpaperComponent)) {
                if (!wallpaper.cropExists()) {
                    if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                        android.util.Slog.i(TAG, "No crop; regenerating from source");
                    }
                    this.mWallpaperCropper.generateCrop(wallpaper);
                }
                if (!wallpaper.cropExists()) {
                    if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                        android.util.Slog.i(TAG, "Unable to regenerate crop; resetting");
                    }
                    if (!this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperLocked(wallpaper.mWhich, 0, false, null, wallpaper.mWallpaperDataExt.getPhysicalDisplayId())) {
                        clearWallpaperLocked(wallpaper.mWhich, 0, false, null);
                    }
                }
            } else if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                android.util.Slog.i(TAG, "Nondefault wallpaper component; gracefully ignoring");
            }
        }
        android.content.IntentFilter userFilter = new android.content.IntentFilter();
        userFilter.addAction("android.intent.action.USER_REMOVED");
        this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.wallpaper.WallpaperManagerService.3
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                java.lang.String action = intent.getAction();
                if ("android.intent.action.USER_REMOVED".equals(action)) {
                    com.android.server.wallpaper.WallpaperManagerService.this.onRemoveUser(intent.getIntExtra("android.intent.extra.user_handle", -10000));
                }
            }
        }, userFilter);
        android.content.IntentFilter shutdownFilter = new android.content.IntentFilter("android.intent.action.ACTION_SHUTDOWN");
        this.mContext.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.wallpaper.WallpaperManagerService.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                if ("android.intent.action.ACTION_SHUTDOWN".equals(intent.getAction())) {
                    if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                        android.util.Slog.i(com.android.server.wallpaper.WallpaperManagerService.TAG, "Shutting down");
                    }
                    synchronized (com.android.server.wallpaper.WallpaperManagerService.this.mLock) {
                        com.android.server.wallpaper.WallpaperManagerService.this.mShuttingDown = true;
                    }
                }
            }
        }, shutdownFilter);
        try {
            android.app.ActivityManager.getService().registerUserSwitchObserver(new android.app.UserSwitchObserver() { // from class: com.android.server.wallpaper.WallpaperManagerService.5
                public void onUserSwitching(int newUserId, android.os.IRemoteCallback reply) {
                    try {
                        android.os.Trace.traceBegin(8L, "WallpaperManagerService.switchUser");
                        com.android.server.wallpaper.WallpaperManagerService.this.errorCheck(newUserId);
                        com.android.server.wallpaper.WallpaperManagerService.this.switchUser(newUserId, reply);
                    } finally {
                        android.os.Trace.traceEnd(8L);
                    }
                }

                public void onUserSwitchComplete(int newUserId) throws android.os.RemoteException {
                    com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceWrapper.getExtImpl().onUserSwitchComplete(newUserId);
                }
            }, TAG);
        } catch (android.os.RemoteException e) {
            e.rethrowAsRuntimeException();
        }
    }

    public java.lang.String getName() {
        java.lang.String str;
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.RuntimeException("getName() can only be called from the system process");
        }
        synchronized (this.mLock) {
            str = this.mWallpaperMap.get(0).name;
        }
        return str;
    }

    void stopObserver(com.android.server.wallpaper.WallpaperData wallpaper) {
        if (wallpaper != null && wallpaper.wallpaperObserver != null) {
            wallpaper.wallpaperObserver.stopWatching();
            wallpaper.wallpaperObserver = null;
        }
    }

    void stopObserversLocked(int userId) {
        stopObserver(this.mWallpaperMap.get(userId));
        stopObserver(this.mLockWallpaperMap.get(userId));
        this.mWallpaperMap.remove(userId);
        this.mLockWallpaperMap.remove(userId);
        this.mWallpaperManagerServiceWrapper.getExtImpl().stopSubDisplayObserversLocked(userId);
    }

    @Override // com.android.server.wallpaper.IWallpaperManagerService
    public void onBootPhase(int phase) throws java.lang.Throwable {
        errorCheck(0);
        if (phase == 550) {
            systemReady();
        } else if (phase == 600) {
            switchUser(0, null);
            this.mWallpaperManagerServiceWrapper.getExtImpl().initCustomizeWallpaper(this.mContext);
            this.mWallpaperManagerServiceWrapper.getExtImpl().initWallpaperBitmap();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void errorCheck(final int userID) {
        sWallpaperType.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda18
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$errorCheck$4(userID, (java.lang.Integer) obj, (java.lang.String) obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$errorCheck$4(int userID, java.lang.Integer type, java.lang.String filename) {
        java.io.File record = new java.io.File(com.android.server.wallpaper.WallpaperUtils.getWallpaperDir(userID), filename);
        if (record.exists()) {
            android.util.Slog.w(TAG, "User:" + userID + ", wallpaper type = " + type + ", wallpaper fail detect!! reset to default wallpaper");
            clearWallpaperBitmaps(userID, type.intValue());
            record.delete();
        }
        this.mWallpaperManagerServiceWrapper.getExtImpl().subDisplayErrorCheck(userID, type.intValue(), filename);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearWallpaperBitmaps(int userID, int wallpaperType) {
        com.android.server.wallpaper.WallpaperData wallpaper = new com.android.server.wallpaper.WallpaperData(userID, wallpaperType);
        clearWallpaperBitmaps(wallpaper);
    }

    private boolean clearWallpaperBitmaps(com.android.server.wallpaper.WallpaperData wallpaper) {
        boolean sourceExists = wallpaper.sourceExists();
        boolean cropExists = wallpaper.cropExists();
        if (sourceExists) {
            wallpaper.getWallpaperFile().delete();
        }
        if (cropExists) {
            wallpaper.getCropFile().delete();
        }
        android.util.Slog.d(TAG, "clearWallpaperBitmaps: " + wallpaper + " " + android.os.Debug.getCallers(5));
        return sourceExists || cropExists;
    }

    @Override // com.android.server.wallpaper.IWallpaperManagerService
    public void onUnlockUser(final int userId) {
        synchronized (this.mLock) {
            if (this.mCurrentUserId == userId) {
                if (this.mHomeWallpaperWaitingForUnlock) {
                    com.android.server.wallpaper.WallpaperData systemWallpaper = getWallpaperSafeLocked(userId, 1);
                    systemWallpaper.mBindSource = com.android.server.wallpaper.WallpaperData.BindSource.SWITCH_WALLPAPER_UNLOCK_USER;
                    switchWallpaper(systemWallpaper, null);
                    notifyCallbacksLocked(systemWallpaper);
                }
                if (this.mLockWallpaperWaitingForUnlock) {
                    com.android.server.wallpaper.WallpaperData lockWallpaper = getWallpaperSafeLocked(userId, 2);
                    lockWallpaper.mBindSource = com.android.server.wallpaper.WallpaperData.BindSource.SWITCH_WALLPAPER_UNLOCK_USER;
                    com.android.server.wallpaper.WallpaperData curSystemWallpaper = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperSafeLocked(userId, 1, this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentPhysicalDisplayIdLocked());
                    if (curSystemWallpaper == null) {
                        getWallpaperSafeLocked(userId, 1);
                    }
                    switchWallpaper(lockWallpaper, null);
                    notifyCallbacksLocked(lockWallpaper);
                }
                this.mWallpaperManagerServiceWrapper.getExtImpl().switchWallpaperForOtherPhysicalDisplay(userId, true);
                if (!this.mUserRestorecon.get(userId)) {
                    this.mUserRestorecon.put(userId, true);
                    java.lang.Runnable relabeler = new java.lang.Runnable() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda9
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onUnlockUser$5(userId);
                        }
                    };
                    com.android.internal.os.BackgroundThread.getHandler().post(relabeler);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onUnlockUser$5(int userId) {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog(TAG);
        t.traceBegin("Wallpaper_selinux_restorecon-" + userId);
        try {
            for (java.io.File file : com.android.server.wallpaper.WallpaperUtils.getWallpaperFiles(userId)) {
                if (file.exists()) {
                    android.os.SELinux.restorecon(file);
                }
            }
            this.mWallpaperManagerServiceWrapper.getExtImpl().restoreconSubDisplayFiles(userId);
        } finally {
            t.traceEnd();
        }
    }

    void onRemoveUser(int userId) {
        if (userId < 1) {
            return;
        }
        synchronized (this.mLock) {
            stopObserversLocked(userId);
            com.android.server.wallpaper.WallpaperUtils.getWallpaperFiles(userId).forEach(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda15
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((java.io.File) obj).delete();
                }
            });
            this.mUserRestorecon.delete(userId);
            this.mWallpaperManagerServiceWrapper.getExtImpl().deleteSubDisplayFiles(userId);
        }
    }

    void switchUser(int userId, android.os.IRemoteCallback reply) {
        final com.android.server.wallpaper.WallpaperData systemWallpaper;
        final com.android.server.wallpaper.WallpaperData lockWallpaper;
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog(TAG);
        t.traceBegin("Wallpaper_switch-user-" + userId);
        try {
            synchronized (this.mLock) {
                if (this.mCurrentUserId == userId) {
                    return;
                }
                int lastUserID = this.mCurrentUserId;
                this.mCurrentUserId = userId;
                this.mWallpaperManagerServiceWrapper.getExtImpl().initOnUserSwitch(userId);
                int curPhysicalDisplayId = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentPhysicalDisplayIdLocked();
                com.android.server.wallpaper.WallpaperData curSystemWallpaper = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperSafeLocked(userId, 1, curPhysicalDisplayId);
                if (curSystemWallpaper == null) {
                    systemWallpaper = getWallpaperSafeLocked(userId, 1);
                } else {
                    systemWallpaper = curSystemWallpaper;
                }
                if (systemWallpaper.mWhich == 3) {
                    lockWallpaper = systemWallpaper;
                } else {
                    lockWallpaper = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperSafeLocked(userId, 2, curPhysicalDisplayId);
                }
                if (systemWallpaper.wallpaperObserver == null) {
                    systemWallpaper.wallpaperObserver = new com.android.server.wallpaper.WallpaperManagerService.WallpaperObserver(systemWallpaper);
                    systemWallpaper.wallpaperObserver.startWatching();
                }
                this.mWallpaperManagerServiceWrapper.getExtImpl().detachOtherPhysicalDisplaysWallpaper(userId, systemWallpaper, lastUserID);
                if (android.multiuser.Flags.reorderWallpaperDuringUserSwitch()) {
                    detachWallpaperLocked(this.mLastLockWallpaper);
                    detachWallpaperLocked(this.mLastWallpaper);
                    if (lockWallpaper == systemWallpaper) {
                        switchWallpaper(systemWallpaper, reply);
                    } else {
                        switchWallpaper(0 != 0 ? systemWallpaper : lockWallpaper, null);
                        switchWallpaper(0 != 0 ? lockWallpaper : systemWallpaper, reply);
                    }
                } else {
                    if (lockWallpaper != systemWallpaper) {
                        switchWallpaper(lockWallpaper, null);
                    }
                    switchWallpaper(systemWallpaper, reply);
                }
                this.mInitialUserSwitch = false;
                this.mWallpaperManagerServiceWrapper.getExtImpl().switchWallpaperForOtherPhysicalDisplay(userId, false);
                com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda11
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$switchUser$6(systemWallpaper, lockWallpaper);
                    }
                });
            }
        } finally {
            t.traceEnd();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$switchUser$6(com.android.server.wallpaper.WallpaperData systemWallpaper, com.android.server.wallpaper.WallpaperData lockWallpaper) {
        if (com.android.window.flags.Flags.offloadColorExtraction()) {
            return;
        }
        notifyWallpaperColorsChanged(systemWallpaper);
        if (lockWallpaper != systemWallpaper) {
            notifyWallpaperColorsChanged(lockWallpaper);
        }
        notifyWallpaperColorsChanged(this.mFallbackWallpaper);
    }

    void switchWallpaper(com.android.server.wallpaper.WallpaperData wallpaper, android.os.IRemoteCallback reply) {
        android.util.Slog.d(TAG, "switchWallpaper: " + wallpaper + android.os.Debug.getCallers(5));
        synchronized (this.mLock) {
            if ((wallpaper.mWhich & 1) != 0) {
                this.mHomeWallpaperWaitingForUnlock = false;
            }
            if ((wallpaper.mWhich & 2) != 0) {
                this.mLockWallpaperWaitingForUnlock = false;
            }
            android.content.ComponentName cname = wallpaper.wallpaperComponent != null ? wallpaper.wallpaperComponent : wallpaper.nextWallpaperComponent;
            if (!bindWallpaperComponentLocked(cname, true, false, wallpaper, reply)) {
                android.content.pm.ServiceInfo si = null;
                try {
                    si = this.mIPackageManager.getServiceInfo(cname, 262144L, wallpaper.userId);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Failure starting previous wallpaper; clearing", e);
                }
                onSwitchWallpaperFailLocked(wallpaper, reply, si);
            }
        }
    }

    private void onSwitchWallpaperFailLocked(final com.android.server.wallpaper.WallpaperData wallpaper, android.os.IRemoteCallback reply, android.content.pm.ServiceInfo serviceInfo) throws android.os.RemoteException {
        if (serviceInfo == null) {
            clearWallpaperLocked(wallpaper.mWhich, wallpaper.userId, false, reply);
            return;
        }
        android.util.Slog.w(TAG, "Wallpaper isn't direct boot aware; using fallback until unlocked");
        wallpaper.wallpaperComponent = wallpaper.nextWallpaperComponent;
        com.android.server.wallpaper.WallpaperData fallback = this.mWallpaperManagerServiceWrapper.getExtImpl().newDirectBootAwareFallbackWallpaper(wallpaper, new java.util.function.Supplier() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda7
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return com.android.server.wallpaper.WallpaperManagerService.lambda$onSwitchWallpaperFailLocked$7(wallpaper);
            }
        });
        clearWallpaperBitmaps(wallpaper);
        fallback.mBindSource = com.android.server.wallpaper.WallpaperData.BindSource.SWITCH_WALLPAPER_FAILURE;
        bindWallpaperComponentLocked(this.mImageWallpaper, true, false, fallback, reply);
        if ((wallpaper.mWhich & 1) != 0) {
            this.mHomeWallpaperWaitingForUnlock = true;
        }
        if ((wallpaper.mWhich & 2) != 0) {
            this.mLockWallpaperWaitingForUnlock = true;
        }
    }

    static /* synthetic */ com.android.server.wallpaper.WallpaperData lambda$onSwitchWallpaperFailLocked$7(com.android.server.wallpaper.WallpaperData wallpaper) {
        return new com.android.server.wallpaper.WallpaperData(wallpaper.userId, wallpaper.mWhich);
    }

    public void clearWallpaper(java.lang.String callingPackage, int which, int userId) {
        android.util.Slog.v(TAG, "clearWallpaper callPackage=" + callingPackage + ", which: " + which + ", userId: " + userId);
        checkPermission("android.permission.SET_WALLPAPER");
        if (!isWallpaperSupported(callingPackage) || !isSetWallpaperAllowed(callingPackage)) {
            return;
        }
        int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, "clearWallpaper", null);
        com.android.server.wallpaper.WallpaperData data = null;
        synchronized (this.mLock) {
            boolean fromForeground = isFromForegroundApp(callingPackage);
            int wallpaperType = this.mWallpaperManagerExt.getWallpaperType(which);
            if (wallpaperType == 2) {
                data = this.mLockWallpaperMap.get(userId2);
            }
            if (wallpaperType == 1 || data == null) {
                data = this.mWallpaperMap.get(userId2);
            }
            this.mWallpaperManagerServiceWrapper.getExtImpl().onWallpaperClearEvent(data.wallpaperComponent, which, userId2, callingPackage);
            int formatWhich = this.mWallpaperManagerServiceWrapper.getExtImpl().formatWhichClear(which);
            if (!this.mWallpaperManagerServiceWrapper.getExtImpl().clearWallpaperLockedForMultiPhysicalDisplays(formatWhich, userId2, true, null)) {
                clearWallpaperLocked(which, userId2, fromForeground, null);
            }
            if (this.mWallpaperManagerServiceWrapper.getExtImpl().isCurrentPhysicalDisplayWallpaperChangedLocked(formatWhich)) {
                if (this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(which) == 2) {
                    data = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(2, this.mLockWallpaperMap).get(userId2);
                }
                if (this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(which) == 2 || data == null) {
                    this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(userId2);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:60:0x0178  */
    /* JADX WARN: Removed duplicated region for block: B:79:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void clearWallpaperLocked(int r22, final int r23, final boolean r24, final android.os.IRemoteCallback r25) throws android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 390
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wallpaper.WallpaperManagerService.clearWallpaperLocked(int, int, boolean, android.os.IRemoteCallback):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$clearWallpaperLocked$8(android.content.ComponentName component, int finalWhich, int phyDisplayId, int userId, boolean force, boolean fromForeground, android.os.IRemoteCallback reply) throws java.lang.Exception {
        return java.lang.Boolean.valueOf(setWallpaperComponentInternal(component, this.mWallpaperManagerServiceWrapper.getExtImpl().getWhichValue(finalWhich, phyDisplayId), userId, force, fromForeground, reply));
    }

    private boolean hasCrossUserPermission() {
        int interactPermission = this.mContext.checkCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL");
        return interactPermission == 0;
    }

    public boolean hasNamedWallpaper(java.lang.String name) {
        int callingUser = android.os.UserHandle.getCallingUserId();
        boolean allowCrossUser = hasCrossUserPermission();
        if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
            android.util.Slog.d(TAG, "hasNamedWallpaper() caller " + android.os.Binder.getCallingUid() + " cross-user?: " + allowCrossUser);
        }
        synchronized (this.mLock) {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                java.util.List<android.content.pm.UserInfo> users = ((android.os.UserManager) this.mContext.getSystemService("user")).getUsers();
                android.os.Binder.restoreCallingIdentity(ident);
                for (android.content.pm.UserInfo user : users) {
                    if (allowCrossUser || callingUser == user.id) {
                        if (!user.isProfile()) {
                            com.android.server.wallpaper.WallpaperData wd = this.mWallpaperMap.get(user.id);
                            if (wd == null) {
                                loadSettingsLocked(user.id, false, 3);
                                wd = this.mWallpaperMap.get(user.id);
                            }
                            if (wd != null && name.equals(wd.name)) {
                                return true;
                            }
                            if (this.mWallpaperManagerServiceWrapper.getExtImpl().hasNamedSubWallpaperForUser(user.id, name)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(ident);
                throw th;
            }
        }
    }

    public void setDimensionHints(int width, int height, java.lang.String callingPackage, int displayId) throws android.os.RemoteException {
        checkPermission("android.permission.SET_WALLPAPER_HINTS");
        if (!isWallpaperSupported(callingPackage)) {
            return;
        }
        int width2 = java.lang.Math.min(width, com.android.server.wallpaper.GLHelper.getMaxTextureSize());
        int height2 = java.lang.Math.min(height, com.android.server.wallpaper.GLHelper.getMaxTextureSize());
        synchronized (this.mLock) {
            int userId = android.os.UserHandle.getCallingUserId();
            int phyDisplayId = this.mWallpaperManagerServiceWrapper.getExtImpl().getPhysicalDisplayIdFromDisplayIdLocked(displayId);
            com.android.server.wallpaper.WallpaperData wallpaper = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperSafeLocked(userId, 1, phyDisplayId);
            if (width2 <= 0 || height2 <= 0) {
                throw new java.lang.IllegalArgumentException("width and height must be > 0");
            }
            if (!this.mWallpaperDisplayHelper.isValidDisplay(displayId)) {
                throw new java.lang.IllegalArgumentException("Cannot find display with id=" + displayId);
            }
            com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData wpdData = this.mWallpaperDisplayHelper.getDisplayDataOrCreate(displayId);
            if (width2 != wpdData.mWidth || height2 != wpdData.mHeight) {
                wpdData.mWidth = width2;
                wpdData.mHeight = height2;
                if (displayId == 0 && !this.mWallpaperManagerServiceWrapper.getExtImpl().saveSettingsLocked(userId, phyDisplayId)) {
                    saveSettingsLocked(userId);
                }
                if (this.mCurrentUserId != userId) {
                    return;
                }
                if (wallpaper.connection != null) {
                    com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector = wallpaper.connection.getDisplayConnectorOrCreate(displayId);
                    android.service.wallpaper.IWallpaperEngine engine = connector != null ? connector.mEngine : null;
                    if (engine != null) {
                        try {
                            engine.setDesiredSize(width2, height2);
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.w(TAG, "Failed to set desired size", e);
                        }
                        notifyCallbacksLocked(wallpaper);
                    } else if (wallpaper.connection.mService != null && connector != null) {
                        connector.mDimensionsChanged = true;
                    }
                }
            }
        }
    }

    public int getWidthHint(int displayId) throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (!this.mWallpaperDisplayHelper.isValidDisplay(displayId)) {
                throw new java.lang.IllegalArgumentException("Cannot find display with id=" + displayId);
            }
            com.android.server.wallpaper.WallpaperData wallpaper = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(android.os.UserHandle.getCallingUserId());
            if (wallpaper == null) {
                return 0;
            }
            com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData wpdData = this.mWallpaperDisplayHelper.getDisplayDataOrCreate(displayId);
            return wpdData.mWidth;
        }
    }

    public int getHeightHint(int displayId) throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (!this.mWallpaperDisplayHelper.isValidDisplay(displayId)) {
                throw new java.lang.IllegalArgumentException("Cannot find display with id=" + displayId);
            }
            com.android.server.wallpaper.WallpaperData wallpaper = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(android.os.UserHandle.getCallingUserId());
            if (wallpaper == null) {
                return 0;
            }
            com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData wpdData = this.mWallpaperDisplayHelper.getDisplayDataOrCreate(displayId);
            return wpdData.mHeight;
        }
    }

    public void setDisplayPadding(android.graphics.Rect padding, java.lang.String callingPackage, int displayId) {
        checkPermission("android.permission.SET_WALLPAPER_HINTS");
        if (!isWallpaperSupported(callingPackage)) {
            return;
        }
        synchronized (this.mLock) {
            if (!this.mWallpaperDisplayHelper.isValidDisplay(displayId)) {
                throw new java.lang.IllegalArgumentException("Cannot find display with id=" + displayId);
            }
            int userId = android.os.UserHandle.getCallingUserId();
            com.android.server.wallpaper.WallpaperData wallpaper = getWallpaperSafeLocked(userId, 1);
            if (padding.left < 0 || padding.top < 0 || padding.right < 0 || padding.bottom < 0) {
                throw new java.lang.IllegalArgumentException("padding must be positive: " + padding);
            }
            int maxSize = this.mWallpaperDisplayHelper.getMaximumSizeDimension(displayId);
            int paddingWidth = padding.left + padding.right;
            int paddingHeight = padding.top + padding.bottom;
            if (paddingWidth > maxSize) {
                throw new java.lang.IllegalArgumentException("padding width " + paddingWidth + " exceeds max width " + maxSize);
            }
            if (paddingHeight > maxSize) {
                throw new java.lang.IllegalArgumentException("padding height " + paddingHeight + " exceeds max height " + maxSize);
            }
            com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData wpdData = this.mWallpaperDisplayHelper.getDisplayDataOrCreate(displayId);
            if (!padding.equals(wpdData.mPadding)) {
                wpdData.mPadding.set(padding);
                if (displayId == 0) {
                    saveSettingsLocked(userId);
                }
                if (this.mCurrentUserId != userId) {
                    return;
                }
                if (wallpaper.connection != null) {
                    com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector = wallpaper.connection.getDisplayConnectorOrCreate(displayId);
                    android.service.wallpaper.IWallpaperEngine engine = connector != null ? connector.mEngine : null;
                    if (engine != null) {
                        try {
                            engine.setDisplayPadding(padding);
                        } catch (android.os.RemoteException e) {
                            android.util.Slog.w(TAG, "Failed to set display padding", e);
                        }
                        notifyCallbacksLocked(wallpaper);
                    } else if (wallpaper.connection.mService != null && connector != null) {
                        connector.mPaddingChanged = true;
                    }
                }
            }
        }
    }

    @java.lang.Deprecated
    public android.os.ParcelFileDescriptor getWallpaper(java.lang.String callingPkg, android.app.IWallpaperManagerCallback cb, int which, android.os.Bundle outParams, int wallpaperUserId) {
        return getWallpaperWithFeature(callingPkg, null, cb, which, outParams, wallpaperUserId, true);
    }

    public android.os.ParcelFileDescriptor getWallpaperWithFeature(java.lang.String callingPkg, java.lang.String callingFeatureId, android.app.IWallpaperManagerCallback cb, int which, android.os.Bundle outParams, int wallpaperUserId, boolean getCropped) {
        android.util.SparseArray<com.android.server.wallpaper.WallpaperData> sparseArray;
        boolean hasPrivilege = hasPermission("android.permission.READ_WALLPAPER_INTERNAL") || hasPermission("android.permission.MANAGE_EXTERNAL_STORAGE");
        if (!hasPrivilege) {
            ((android.os.storage.StorageManager) this.mContext.getSystemService(android.os.storage.StorageManager.class)).checkPermissionReadImages(true, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), callingPkg, callingFeatureId);
        }
        int wallpaperUserId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), wallpaperUserId, false, true, "getWallpaper", null);
        if (this.mWallpaperManagerServiceWrapper.getExtImpl().isNotAvailableWhichWithSinglePhysicalDisplayFlag(which)) {
            throw new java.lang.IllegalArgumentException("Must specify exactly one kind of wallpaper to read");
        }
        synchronized (this.mLock) {
            com.android.server.wallpaper.IWallpaperManagerServiceExt extImpl = this.mWallpaperManagerServiceWrapper.getExtImpl();
            if (this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(which) != 2) {
                sparseArray = this.mWallpaperMap;
            } else {
                sparseArray = this.mLockWallpaperMap;
            }
            android.util.SparseArray<com.android.server.wallpaper.WallpaperData> whichSet = extImpl.getWallpaperMapByWhich(which, sparseArray);
            com.android.server.wallpaper.WallpaperData wallpaper = whichSet.get(wallpaperUserId2);
            if (wallpaper == null) {
                android.util.Slog.d(TAG, "no established wallpaper: " + which + ", userid: " + wallpaperUserId2 + ", callingPkg: " + callingPkg);
                return null;
            }
            com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData wpdData = this.mWallpaperDisplayHelper.getDisplayDataOrCreate(0);
            if (outParams != null) {
                try {
                    outParams.putInt("width", wpdData.mWidth);
                    outParams.putInt("height", wpdData.mHeight);
                } catch (java.io.FileNotFoundException e) {
                    android.util.Slog.w(TAG, "Error getting wallpaper", e);
                    return null;
                }
            }
            if (cb != null) {
                wallpaper.callbacks.register(cb);
                this.mWallpaperManagerServiceWrapper.getExtImpl().registerWallpaperCallbacksToOtherPhysicalDisplays(which, cb, wallpaper);
            }
            java.io.File result = getCropped ? wallpaper.getCropFile() : wallpaper.getWallpaperFile();
            if (!result.exists()) {
                android.util.Slog.d(TAG, "wallpaper file does not exist: " + result.getAbsolutePath() + ", userid: " + wallpaperUserId2 + ", callingPkg: " + callingPkg);
                return null;
            }
            if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                android.util.Slog.d(TAG, "wallpaper file exists: " + result.getAbsolutePath() + ", which: 0x" + java.lang.Integer.toHexString(which) + ", userid: " + wallpaperUserId2 + ", callingPkg: " + callingPkg + ", wallpaper: " + wallpaper);
            }
            return android.os.ParcelFileDescriptor.open(result, 268435456);
        }
    }

    public java.util.List<android.graphics.Rect> getBitmapCrops(java.util.List<android.graphics.Point> displaySizes, int which, boolean originalBitmap, int userId) {
        int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, "getBitmapCrop", null);
        synchronized (this.mLock) {
            checkPermission("android.permission.READ_WALLPAPER_INTERNAL");
            com.android.server.wallpaper.WallpaperData wallpaper = which == 2 ? this.mLockWallpaperMap.get(userId2) : this.mWallpaperMap.get(userId2);
            if (wallpaper != null && this.mImageWallpaper.equals(wallpaper.wallpaperComponent)) {
                android.util.SparseArray<android.graphics.Rect> relativeSuggestedCrops = this.mWallpaperCropper.getRelativeCropHints(wallpaper);
                android.graphics.Point croppedBitmapSize = new android.graphics.Point((int) ((wallpaper.cropHint.width() / wallpaper.mSampleSize) + 0.5f), (int) ((wallpaper.cropHint.height() / wallpaper.mSampleSize) + 0.5f));
                if (croppedBitmapSize.equals(0, 0)) {
                    return null;
                }
                android.util.SparseArray<android.graphics.Rect> relativeDefaultCrops = this.mWallpaperCropper.getDefaultCrops(relativeSuggestedCrops, croppedBitmapSize);
                android.util.SparseArray<android.graphics.Rect> adjustedRelativeSuggestedCrops = new android.util.SparseArray<>();
                for (int i = 0; i < relativeDefaultCrops.size(); i++) {
                    int key = relativeDefaultCrops.keyAt(i);
                    if (relativeSuggestedCrops.contains(key)) {
                        adjustedRelativeSuggestedCrops.put(key, relativeDefaultCrops.get(key));
                    }
                }
                java.util.List<android.graphics.Rect> result = new java.util.ArrayList<>();
                boolean rtl = android.text.TextUtils.getLayoutDirectionFromLocale(java.util.Locale.getDefault()) == 1;
                for (android.graphics.Point displaySize : displaySizes) {
                    result.add(this.mWallpaperCropper.getCrop(displaySize, croppedBitmapSize, adjustedRelativeSuggestedCrops, rtl));
                }
                if (originalBitmap) {
                    result = com.android.server.wallpaper.WallpaperCropper.getOriginalCropHints(wallpaper, result);
                }
                return result;
            }
            return null;
        }
    }

    public java.util.List<android.graphics.Rect> getFutureBitmapCrops(android.graphics.Point bitmapSize, java.util.List<android.graphics.Point> displaySizes, int[] screenOrientations, java.util.List<android.graphics.Rect> crops) {
        android.util.SparseArray<android.graphics.Rect> cropMap = getCropMap(screenOrientations, crops);
        android.util.SparseArray<android.graphics.Rect> defaultCrops = this.mWallpaperCropper.getDefaultCrops(cropMap, bitmapSize);
        java.util.List<android.graphics.Rect> result = new java.util.ArrayList<>();
        boolean rtl = android.text.TextUtils.getLayoutDirectionFromLocale(java.util.Locale.getDefault()) == 1;
        for (android.graphics.Point displaySize : displaySizes) {
            result.add(this.mWallpaperCropper.getCrop(displaySize, bitmapSize, defaultCrops, rtl));
        }
        return result;
    }

    public android.graphics.Rect getBitmapCrop(android.graphics.Point bitmapSize, int[] screenOrientations, java.util.List<android.graphics.Rect> crops) {
        if (!com.android.window.flags.Flags.multiCrop()) {
            throw new java.lang.UnsupportedOperationException("This method should only be called with the multi crop flag enabled");
        }
        android.util.SparseArray<android.graphics.Rect> cropMap = getCropMap(screenOrientations, crops);
        android.util.SparseArray<android.graphics.Rect> defaultCrops = this.mWallpaperCropper.getDefaultCrops(cropMap, bitmapSize);
        return com.android.server.wallpaper.WallpaperCropper.getTotalCrop(defaultCrops);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasPermission(java.lang.String permission) {
        return this.mContext.checkCallingOrSelfPermission(permission) == 0;
    }

    public android.app.WallpaperInfo getWallpaperInfo(int userId) {
        return getWallpaperInfoWithFlags(1, userId);
    }

    public android.app.WallpaperInfo getWallpaperInfoWithFlags(int which, int userId) {
        com.android.server.wallpaper.WallpaperData wallpaper;
        int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, "getWallpaperInfo", null);
        synchronized (this.mLock) {
            if (which == 2) {
                wallpaper = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(2, this.mLockWallpaperMap).get(userId2);
            } else {
                wallpaper = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(userId2);
            }
            if (wallpaper != null && wallpaper.connection != null && wallpaper.connection.mInfo != null) {
                android.app.WallpaperInfo info = wallpaper.connection.mInfo;
                if (!hasPermission("android.permission.READ_WALLPAPER_INTERNAL") && !this.mPackageManagerInternal.canQueryPackage(android.os.Binder.getCallingUid(), info.getComponent().getPackageName())) {
                    return null;
                }
                return info;
            }
            return null;
        }
    }

    public android.os.ParcelFileDescriptor getWallpaperInfoFile(int userId) {
        synchronized (this.mLock) {
            try {
                try {
                    java.io.File file = new java.io.File(com.android.server.wallpaper.WallpaperUtils.getWallpaperDir(userId), "wallpaper_info.xml");
                    if (!file.exists()) {
                        return null;
                    }
                    return android.os.ParcelFileDescriptor.open(file, 268435456);
                } catch (java.io.FileNotFoundException e) {
                    android.util.Slog.w(TAG, "Error getting wallpaper info file", e);
                    return null;
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    public int getWallpaperIdForUser(int which, int userId) {
        int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, "getWallpaperIdForUser", null);
        if (this.mWallpaperManagerServiceWrapper.getExtImpl().isNotAvailableWhichWithSinglePhysicalDisplayFlag(which)) {
            throw new java.lang.IllegalArgumentException("Must specify exactly one kind of wallpaper");
        }
        synchronized (this.mLock) {
            android.util.SparseArray<com.android.server.wallpaper.WallpaperData> map = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMapByWhich(which, this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(which) == 2 ? this.mLockWallpaperMap : this.mWallpaperMap);
            com.android.server.wallpaper.WallpaperData wallpaper = map.get(userId2);
            if (wallpaper != null) {
                return wallpaper.wallpaperId;
            }
            return -1;
        }
    }

    public void registerWallpaperColorsCallback(android.app.IWallpaperManagerCallback cb, int userId, int displayId) {
        int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, true, true, "registerWallpaperColorsCallback", null);
        synchronized (this.mLock) {
            android.util.SparseArray<android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback>> userDisplayColorsChangedListeners = this.mColorsChangedListeners.get(userId2);
            if (userDisplayColorsChangedListeners == null) {
                userDisplayColorsChangedListeners = new android.util.SparseArray<>();
                this.mColorsChangedListeners.put(userId2, userDisplayColorsChangedListeners);
            }
            android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback> displayChangedListeners = userDisplayColorsChangedListeners.get(displayId);
            if (displayChangedListeners == null) {
                displayChangedListeners = new android.os.RemoteCallbackList<>();
                userDisplayColorsChangedListeners.put(displayId, displayChangedListeners);
            }
            displayChangedListeners.register(cb);
        }
    }

    public void unregisterWallpaperColorsCallback(android.app.IWallpaperManagerCallback cb, int userId, int displayId) {
        android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback> displayChangedListeners;
        int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, true, true, "unregisterWallpaperColorsCallback", null);
        synchronized (this.mLock) {
            android.util.SparseArray<android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback>> userDisplayColorsChangedListeners = this.mColorsChangedListeners.get(userId2);
            if (userDisplayColorsChangedListeners != null && (displayChangedListeners = userDisplayColorsChangedListeners.get(displayId)) != null) {
                displayChangedListeners.unregister(cb);
            }
        }
    }

    public void setInAmbientMode(boolean inAmbientMode, long animationDuration) {
        android.service.wallpaper.IWallpaperEngine engine;
        java.util.List<android.service.wallpaper.IWallpaperEngine> engines = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            this.mInAmbientMode = inAmbientMode;
            for (com.android.server.wallpaper.WallpaperData data : getActiveWallpapers()) {
                if ((data.connection.mInfo == null || data.connection.mInfo.supportsAmbientMode()) && (engine = data.connection.getDisplayConnectorOrCreate(0).mEngine) != null) {
                    engines.add(engine);
                }
            }
        }
        java.util.Iterator<android.service.wallpaper.IWallpaperEngine> it = engines.iterator();
        while (it.hasNext()) {
            try {
                it.next().setInAmbientMode(inAmbientMode, animationDuration);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to set ambient mode", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pauseOrResumeRenderingImmediately(final boolean pause) {
        synchronized (this.mLock) {
            for (com.android.server.wallpaper.WallpaperData data : getActiveWallpapers()) {
                if (data.connection.mInfo != null && ((pause || ((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class)).isUidForeground(data.connection.mInfo.getServiceInfo().applicationInfo.uid)) && data.connection.containsDisplay(this.mWindowManagerInternal.getTopFocusedDisplayId()))) {
                    data.connection.forEachDisplayConnector(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            com.android.server.wallpaper.WallpaperManagerService.lambda$pauseOrResumeRenderingImmediately$10(pause, (com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) obj);
                        }
                    });
                }
            }
        }
    }

    static /* synthetic */ void lambda$pauseOrResumeRenderingImmediately$10(boolean pause, com.android.server.wallpaper.WallpaperManagerService.DisplayConnector displayConnector) {
        if (displayConnector.mEngine != null) {
            try {
                displayConnector.mEngine.setVisibility(!pause);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to set visibility", e);
            }
        }
    }

    public void notifyWakingUp(final int x, final int y, final android.os.Bundle extras) {
        checkCallerIsSystemOrSystemUi();
        synchronized (this.mLock) {
            for (com.android.server.wallpaper.WallpaperData data : getActiveWallpapers()) {
                data.connection.forEachDisplayConnector(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda13
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.wallpaper.WallpaperManagerService.lambda$notifyWakingUp$11(x, y, extras, (com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) obj);
                    }
                });
            }
        }
    }

    static /* synthetic */ void lambda$notifyWakingUp$11(int x, int y, android.os.Bundle extras, com.android.server.wallpaper.WallpaperManagerService.DisplayConnector displayConnector) {
        if (displayConnector.mEngine != null) {
            try {
                displayConnector.mEngine.dispatchWallpaperCommand("android.wallpaper.wakingup", x, y, -1, extras);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to dispatch COMMAND_WAKING_UP", e);
            }
        }
    }

    public void notifyGoingToSleep(final int x, final int y, final android.os.Bundle extras) {
        checkCallerIsSystemOrSystemUi();
        synchronized (this.mLock) {
            for (com.android.server.wallpaper.WallpaperData data : getActiveWallpapers()) {
                data.connection.forEachDisplayConnector(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda23
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.wallpaper.WallpaperManagerService.lambda$notifyGoingToSleep$12(x, y, extras, (com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) obj);
                    }
                });
            }
        }
    }

    static /* synthetic */ void lambda$notifyGoingToSleep$12(int x, int y, android.os.Bundle extras, com.android.server.wallpaper.WallpaperManagerService.DisplayConnector displayConnector) {
        if (displayConnector.mEngine != null) {
            try {
                displayConnector.mEngine.dispatchWallpaperCommand("android.wallpaper.goingtosleep", x, y, -1, extras);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to dispatch COMMAND_GOING_TO_SLEEP", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyScreenTurnedOn(int displayId) {
        android.service.wallpaper.IWallpaperEngine engine;
        synchronized (this.mLock) {
            for (com.android.server.wallpaper.WallpaperData data : getActiveWallpapers()) {
                if (data.connection.containsDisplay(displayId) && (engine = data.connection.getDisplayConnectorOrCreate(displayId).mEngine) != null) {
                    try {
                        engine.onScreenTurnedOn();
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w(TAG, "Failed to notify that the screen turned on", e);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyScreenTurningOn(int displayId) {
        android.service.wallpaper.IWallpaperEngine engine;
        synchronized (this.mLock) {
            for (com.android.server.wallpaper.WallpaperData data : getActiveWallpapers()) {
                if (data.connection.containsDisplay(displayId) && (engine = data.connection.getDisplayConnectorOrCreate(displayId).mEngine) != null) {
                    try {
                        engine.onScreenTurningOn();
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w(TAG, "Failed to notify that the screen is turning on", e);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyKeyguardGoingAway() {
        synchronized (this.mLock) {
            for (com.android.server.wallpaper.WallpaperData data : getActiveWallpapers()) {
                data.connection.forEachDisplayConnector(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda12
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.wallpaper.WallpaperManagerService.lambda$notifyKeyguardGoingAway$13((com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) obj);
                    }
                });
            }
        }
    }

    static /* synthetic */ void lambda$notifyKeyguardGoingAway$13(com.android.server.wallpaper.WallpaperManagerService.DisplayConnector displayConnector) {
        if (displayConnector.mEngine != null) {
            try {
                displayConnector.mEngine.dispatchWallpaperCommand("android.wallpaper.keyguardgoingaway", -1, -1, -1, new android.os.Bundle());
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to notify that the keyguard is going away", e);
            }
        }
    }

    private com.android.server.wallpaper.WallpaperData[] getActiveWallpapers() {
        com.android.server.wallpaper.WallpaperData systemWallpaper = this.mWallpaperMap.get(this.mCurrentUserId);
        com.android.server.wallpaper.WallpaperData lockWallpaper = this.mLockWallpaperMap.get(this.mCurrentUserId);
        boolean systemValid = (systemWallpaper == null || systemWallpaper.connection == null) ? false : true;
        boolean lockValid = (lockWallpaper == null || lockWallpaper.connection == null) ? false : true;
        if (systemValid && lockValid) {
            return new com.android.server.wallpaper.WallpaperData[]{systemWallpaper, lockWallpaper};
        }
        if (systemValid) {
            return new com.android.server.wallpaper.WallpaperData[]{systemWallpaper};
        }
        if (lockValid) {
            return new com.android.server.wallpaper.WallpaperData[]{lockWallpaper};
        }
        return new com.android.server.wallpaper.WallpaperData[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.wallpaper.WallpaperData[] getWallpapers() {
        com.android.server.wallpaper.WallpaperData systemWallpaper = this.mWallpaperMap.get(this.mCurrentUserId);
        com.android.server.wallpaper.WallpaperData lockWallpaper = this.mLockWallpaperMap.get(this.mCurrentUserId);
        boolean systemValid = systemWallpaper != null;
        boolean lockValid = lockWallpaper != null;
        if (systemValid && lockValid) {
            return new com.android.server.wallpaper.WallpaperData[]{systemWallpaper, lockWallpaper};
        }
        if (systemValid) {
            return new com.android.server.wallpaper.WallpaperData[]{systemWallpaper};
        }
        if (lockValid) {
            return new com.android.server.wallpaper.WallpaperData[]{lockWallpaper};
        }
        return new com.android.server.wallpaper.WallpaperData[0];
    }

    private android.service.wallpaper.IWallpaperEngine getEngine(int which, int userId, int displayId) {
        com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection connection;
        com.android.server.wallpaper.WallpaperData wallpaperData = findWallpaperAtDisplay(userId, displayId);
        if (wallpaperData == null || (connection = wallpaperData.connection) == null) {
            return null;
        }
        android.service.wallpaper.IWallpaperEngine engine = null;
        synchronized (this.mLock) {
            for (int i = 0; i < connection.mDisplayConnector.size(); i++) {
                int id = ((com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) connection.mDisplayConnector.get(i)).mDisplayId;
                int currentWhich = ((com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) connection.mDisplayConnector.get(i)).mDisplayId;
                if (id == displayId || currentWhich == which) {
                    engine = ((com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) connection.mDisplayConnector.get(i)).mEngine;
                    break;
                }
            }
        }
        return engine;
    }

    public void addOnLocalColorsChangedListener(android.app.ILocalWallpaperColorConsumer callback, java.util.List<android.graphics.RectF> regions, int which, int userId, int displayId) throws android.os.RemoteException {
        if (which != 2 && which != 1) {
            throw new java.lang.IllegalArgumentException("which should be either FLAG_LOCK or FLAG_SYSTEM");
        }
        android.service.wallpaper.IWallpaperEngine engine = getEngine(which, userId, displayId);
        if (engine == null) {
            return;
        }
        synchronized (this.mLock) {
            this.mLocalColorRepo.addAreas(callback, regions, displayId);
        }
        engine.addLocalColorsAreas(regions);
    }

    public void removeOnLocalColorsChangedListener(android.app.ILocalWallpaperColorConsumer callback, java.util.List<android.graphics.RectF> removeAreas, int which, int userId, int displayId) throws android.os.RemoteException {
        if (which != 2 && which != 1) {
            throw new java.lang.IllegalArgumentException("which should be either FLAG_LOCK or FLAG_SYSTEM");
        }
        android.os.UserHandle callingUser = android.os.Binder.getCallingUserHandle();
        if (callingUser.getIdentifier() != userId) {
            throw new java.lang.SecurityException("calling user id does not match");
        }
        long identity = android.os.Binder.clearCallingIdentity();
        java.util.List<android.graphics.RectF> purgeAreas = null;
        try {
            synchronized (this.mLock) {
                purgeAreas = this.mLocalColorRepo.removeAreas(callback, removeAreas, displayId);
            }
        } catch (java.lang.Exception e) {
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
        android.os.Binder.restoreCallingIdentity(identity);
        android.service.wallpaper.IWallpaperEngine engine = getEngine(which, userId, displayId);
        if (engine == null || purgeAreas == null || purgeAreas.size() <= 0) {
            return;
        }
        engine.removeLocalColorsAreas(purgeAreas);
    }

    public boolean lockScreenWallpaperExists() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(2, this.mLockWallpaperMap).get(this.mCurrentUserId) != null;
        }
        return z;
    }

    public boolean isStaticWallpaper(int which) {
        synchronized (this.mLock) {
            com.android.server.wallpaper.WallpaperData wallpaperData = (which == 2 ? this.mLockWallpaperMap : this.mWallpaperMap).get(this.mCurrentUserId);
            if (wallpaperData == null) {
                return false;
            }
            return this.mImageWallpaper.equals(wallpaperData.wallpaperComponent);
        }
    }

    public void setWallpaperDimAmount(float dimAmount) throws android.os.RemoteException {
        setWallpaperDimAmountForUid(android.os.Binder.getCallingUid(), dimAmount);
    }

    public void setWallpaperDimAmountForUid(int uid, float dimAmount) {
        int userId;
        checkPermission("android.permission.SET_WALLPAPER_DIM_AMOUNT");
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            java.util.List<com.android.server.wallpaper.WallpaperData> pendingColorExtraction = new java.util.ArrayList<>();
            synchronized (this.mLock) {
                int i = 0;
                int userId2 = this.mCurrentUserId != -10000 ? this.mCurrentUserId : 0;
                com.android.server.wallpaper.WallpaperData wallpaper = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(userId2);
                com.android.server.wallpaper.WallpaperData lockWallpaper = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(2, this.mLockWallpaperMap).get(userId2);
                if (dimAmount == 0.0f) {
                    wallpaper.mUidToDimAmount.remove(uid);
                } else {
                    wallpaper.mUidToDimAmount.put(uid, java.lang.Float.valueOf(dimAmount));
                }
                final float maxDimAmount = getHighestDimAmountFromMap(wallpaper.mUidToDimAmount);
                if (wallpaper.mWallpaperDimAmount == maxDimAmount) {
                    return;
                }
                wallpaper.mWallpaperDimAmount = maxDimAmount;
                if (lockWallpaper != null) {
                    lockWallpaper.mWallpaperDimAmount = maxDimAmount;
                }
                boolean changed = false;
                com.android.server.wallpaper.WallpaperData[] activeWallpapers = getActiveWallpapers();
                int length = activeWallpapers.length;
                while (i < length) {
                    com.android.server.wallpaper.WallpaperData wp = activeWallpapers[i];
                    if (wp == null || wp.connection == null) {
                        userId = userId2;
                    } else {
                        userId = userId2;
                        wp.connection.forEachDisplayConnector(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda5
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                com.android.server.wallpaper.WallpaperManagerService.lambda$setWallpaperDimAmountForUid$14(maxDimAmount, (com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) obj);
                            }
                        });
                        if (!com.android.window.flags.Flags.offloadColorExtraction()) {
                            wp.mIsColorExtractedFromDim = true;
                            pendingColorExtraction.add(wp);
                        }
                        changed = true;
                    }
                    i++;
                    userId2 = userId;
                }
                if (changed) {
                    this.mWallpaperManagerServiceWrapper.getExtImpl().saveSettingsLockedForAffectedPhysicalDisplays(this.mCurrentUserId, this.mWallpaperManagerServiceWrapper.getExtImpl().getWhichValue(1, com.android.server.wallpaper.IWallpaperManagerServiceExt.ALL_PHYSICAL_DISPLAY_IDS));
                }
                for (com.android.server.wallpaper.WallpaperData wp2 : pendingColorExtraction) {
                    if (!com.android.window.flags.Flags.offloadColorExtraction()) {
                        notifyWallpaperColorsChanged(wp2);
                    }
                }
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    static /* synthetic */ void lambda$setWallpaperDimAmountForUid$14(float maxDimAmount, com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector) {
        if (connector.mEngine != null) {
            try {
                connector.mEngine.applyDimming(maxDimAmount);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Can't apply dimming on wallpaper display connector", e);
            }
        }
    }

    public float getWallpaperDimAmount() {
        checkPermission("android.permission.SET_WALLPAPER_DIM_AMOUNT");
        synchronized (this.mLock) {
            com.android.server.wallpaper.WallpaperData data = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(this.mCurrentUserId);
            if (data == null && (data = this.mWallpaperManagerServiceWrapper.getExtImpl().getCurrentWallpaperMap(1, this.mWallpaperMap).get(0)) == null) {
                android.util.Slog.e(TAG, "getWallpaperDimAmount: wallpaperData is null");
                return 0.0f;
            }
            return data.mWallpaperDimAmount;
        }
    }

    private float getHighestDimAmountFromMap(android.util.SparseArray<java.lang.Float> uidToDimAmountMap) {
        float maxDimAmount = 0.0f;
        for (int i = 0; i < uidToDimAmountMap.size(); i++) {
            maxDimAmount = java.lang.Math.max(maxDimAmount, uidToDimAmountMap.valueAt(i).floatValue());
        }
        return maxDimAmount;
    }

    public android.app.WallpaperColors getWallpaperColors(int which, int userId, int displayId) throws android.os.RemoteException {
        if (this.mWallpaperManagerServiceWrapper.getExtImpl().isNotAvailableWhichWithSinglePhysicalDisplayFlag(which)) {
            throw new java.lang.IllegalArgumentException("which should be either FLAG_LOCK or FLAG_SYSTEM");
        }
        int userId2 = android.app.ActivityManager.handleIncomingUser(android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), userId, false, true, "getWallpaperColors", null);
        synchronized (this.mLock) {
            com.android.server.wallpaper.WallpaperData wallpaperData = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperForWallpaperColors(which, userId2, displayId);
            if (wallpaperData == null) {
                if (which == 2) {
                    wallpaperData = this.mLockWallpaperMap.get(userId2);
                }
                if (wallpaperData == null) {
                    wallpaperData = findWallpaperAtDisplay(userId2, displayId);
                }
            }
            if (wallpaperData == null) {
                return null;
            }
            boolean shouldExtract = wallpaperData.primaryColors == null || wallpaperData.mIsColorExtractedFromDim;
            if (shouldExtract) {
                extractColors(wallpaperData);
            }
            return getAdjustedWallpaperColorsOnDimming(wallpaperData);
        }
    }

    android.app.WallpaperColors getAdjustedWallpaperColorsOnDimming(com.android.server.wallpaper.WallpaperData wallpaperData) {
        synchronized (this.mLock) {
            android.app.WallpaperColors wallpaperColors = wallpaperData.primaryColors;
            if (wallpaperColors == null || (wallpaperColors.getColorHints() & 4) != 0 || wallpaperData.mWallpaperDimAmount == 0.0f) {
                return wallpaperColors;
            }
            int adjustedColorHints = wallpaperColors.getColorHints() & (-2) & (-3);
            return new android.app.WallpaperColors(wallpaperColors.getPrimaryColor(), wallpaperColors.getSecondaryColor(), wallpaperColors.getTertiaryColor(), adjustedColorHints);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.wallpaper.WallpaperData findWallpaperAtDisplay(int userId, int displayId) {
        if (this.mFallbackWallpaper != null && this.mFallbackWallpaper.connection != null && this.mFallbackWallpaper.connection.containsDisplay(displayId)) {
            return this.mFallbackWallpaper;
        }
        return this.mWallpaperManagerServiceWrapper.getExtImpl().findWallpaperAtDisplay(userId, displayId, 1, this.mWallpaperMap.get(userId));
    }

    public android.os.ParcelFileDescriptor setWallpaper(java.lang.String name, java.lang.String callingPackage, int[] screenOrientations, java.util.List<android.graphics.Rect> crops, boolean allowBackup, android.os.Bundle extras, int which, android.app.IWallpaperManagerCallback completion, int userId) throws java.lang.Throwable {
        android.graphics.Rect cropHint;
        boolean fromForegroundApp;
        android.util.Slog.i(TAG, "setWallpaper: name = " + name + ", callingPackage = " + callingPackage + ", screenOrientations = " + (screenOrientations == null ? null : java.util.Arrays.stream(screenOrientations).boxed().toList()) + ", crops = " + crops + ", which = " + java.lang.Integer.toHexString(which) + ", userId = " + userId + ", allowBackup = " + allowBackup);
        this.mWallpaperManagerServiceWrapper.getExtImpl().setWallpapersCallingPackage(callingPackage);
        int userId2 = android.app.ActivityManager.handleIncomingUser(getCallingPid(), getCallingUid(), userId, false, true, "changing wallpaper", null);
        checkPermission("android.permission.SET_WALLPAPER");
        if ((which & 3) == 0) {
            android.util.Slog.e(TAG, "Must specify a valid wallpaper category to set");
            throw new java.lang.IllegalArgumentException("Must specify a valid wallpaper category to set");
        }
        if (isWallpaperSupported(callingPackage) && isSetWallpaperAllowed(callingPackage)) {
            android.util.SparseArray<android.graphics.Rect> cropMap = !com.android.window.flags.Flags.multiCrop() ? null : getCropMap(screenOrientations, crops);
            android.graphics.Rect cropHint2 = (com.android.window.flags.Flags.multiCrop() || crops == null || crops.isEmpty()) ? new android.graphics.Rect() : crops.get(0);
            boolean fromForegroundApp2 = !com.android.window.flags.Flags.multiCrop() ? false : isFromForegroundApp(callingPackage);
            if (cropHint2 == null && !com.android.window.flags.Flags.multiCrop()) {
                cropHint = new android.graphics.Rect(0, 0, 0, 0);
            } else {
                if (!com.android.window.flags.Flags.multiCrop() && (cropHint2.width() < 0 || cropHint2.height() < 0 || cropHint2.left < 0 || cropHint2.top < 0)) {
                    throw new java.lang.IllegalArgumentException("Invalid crop rect supplied: " + cropHint2);
                }
                cropHint = cropHint2;
            }
            synchronized (this.mLock) {
                try {
                    try {
                        int which2 = this.mWallpaperManagerServiceWrapper.getExtImpl().doRectifyWhich(which);
                        try {
                            if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                                try {
                                    android.util.Slog.v(TAG, "setWallpaper which=0x" + java.lang.Integer.toHexString(which2));
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                    throw th;
                                }
                            }
                            int phyid = this.mWallpaperManagerServiceWrapper.getExtImpl().getPhysicalDisplayIdLocked(which2);
                            com.android.server.wallpaper.WallpaperData originalSystemWallpaper = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(1, phyid, this.mWallpaperMap).get(userId2);
                            boolean systemIsStatic = originalSystemWallpaper != null && this.mImageWallpaper.equals(originalSystemWallpaper.wallpaperComponent);
                            boolean systemIsBoth = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, phyid, this.mLockWallpaperMap).get(userId2) == null;
                            if (this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperType(which2) == 1 && systemIsStatic && systemIsBoth) {
                                android.util.Slog.i(TAG, "Migrating current wallpaper to be lock-only before updating system wallpaper");
                                if (!this.mWallpaperManagerServiceExt.migrateStaticSystemToLockWallpaperLocked(userId2, which2)) {
                                    migrateStaticSystemToLockWallpaperLocked(userId2);
                                }
                            }
                            com.android.server.wallpaper.WallpaperData wallpaper = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperSafeLocked(userId2, which2);
                            if (this.mWallpaperManagerServiceExt.getPendingMigrationViaStatic(this.mPendingMigrationViaStatic, phyid) == null) {
                                fromForegroundApp = fromForegroundApp2;
                            } else {
                                try {
                                    android.util.Slog.w(TAG, "Starting new static wp migration before previous migration finished");
                                    fromForegroundApp = fromForegroundApp2;
                                } catch (java.lang.Throwable th2) {
                                    th = th2;
                                    throw th;
                                }
                                try {
                                    this.mWallpaperManagerServiceExt.updateSystemWallpaperParameter(this.mWallpaperManagerServiceExt.getPendingMigrationViaStatic(this.mPendingMigrationViaStatic, phyid).mNewWallpaper, which2);
                                } catch (java.lang.Throwable th3) {
                                    th = th3;
                                    throw th;
                                }
                            }
                            this.mPendingMigrationViaStatic = new com.android.server.wallpaper.WallpaperManagerService.WallpaperDestinationChangeHandler(wallpaper);
                            this.mWallpaperManagerServiceExt.putPendingMigrationViaStatic(this.mPendingMigrationViaStatic, phyid);
                            long ident = android.os.Binder.clearCallingIdentity();
                            try {
                                android.os.ParcelFileDescriptor pfd = updateWallpaperBitmapLocked(name, wallpaper, extras);
                                if (pfd != null) {
                                    wallpaper.imageWallpaperPending = true;
                                    wallpaper.mSystemWasBoth = systemIsBoth;
                                    wallpaper.mWhich = this.mWallpaperManagerServiceWrapper.getExtImpl().formatWhichPending(which2);
                                    try {
                                        wallpaper.setComplete = completion;
                                        wallpaper.fromForegroundApp = com.android.window.flags.Flags.multiCrop() ? fromForegroundApp : isFromForegroundApp(callingPackage);
                                        wallpaper.cropHint.set(cropHint);
                                        if (com.android.window.flags.Flags.multiCrop()) {
                                            wallpaper.mCropHints = cropMap;
                                            wallpaper.mSampleSize = 1.0f;
                                            wallpaper.mOrientationWhenSet = this.mWallpaperDisplayHelper.getDefaultDisplayCurrentOrientation();
                                        }
                                        wallpaper.allowBackup = allowBackup;
                                        wallpaper.mWallpaperDimAmount = getWallpaperDimAmount();
                                        this.mWallpaperManagerServiceExt.clearFlipClockTextColorIfNeed(this.mContext, wallpaper.mWhich, userId2);
                                        if (com.android.window.flags.Flags.offloadColorExtraction()) {
                                            wallpaper.primaryColors = null;
                                        }
                                    } catch (java.lang.Throwable th4) {
                                        th = th4;
                                        android.os.Binder.restoreCallingIdentity(ident);
                                        throw th;
                                    }
                                }
                                android.os.Binder.restoreCallingIdentity(ident);
                                return pfd;
                            } catch (java.lang.Throwable th5) {
                                th = th5;
                            }
                        } catch (java.lang.Throwable th6) {
                            th = th6;
                        }
                    } catch (java.lang.Throwable th7) {
                        th = th7;
                    }
                } catch (java.lang.Throwable th8) {
                    th = th8;
                }
            }
        }
        return null;
    }

    private android.util.SparseArray<android.graphics.Rect> getCropMap(int[] screenOrientations, java.util.List<android.graphics.Rect> crops) {
        if (((screenOrientations == null) ^ (crops == null)) || (crops != null && crops.size() != screenOrientations.length)) {
            throw new java.lang.IllegalArgumentException("Illegal crops/orientations lists: must both be null, or both the same size");
        }
        android.util.SparseArray<android.graphics.Rect> cropMap = new android.util.SparseArray<>();
        if (crops != null && !crops.isEmpty()) {
            for (int i = 0; i < crops.size(); i++) {
                android.graphics.Rect crop = crops.get(i);
                int width = crop.width();
                int height = crop.height();
                if (width < 0 || height < 0 || crop.left < 0 || crop.top < 0) {
                    throw new java.lang.IllegalArgumentException("Invalid crop rect supplied: " + crop);
                }
                int orientation = screenOrientations[i];
                if (orientation == -1 && cropMap.size() > 1) {
                    throw new java.lang.IllegalArgumentException("Invalid crops supplied: the UNKNOWNscreen orientation should only be used in a singleton map");
                }
                cropMap.put(orientation, crop);
            }
        }
        return cropMap;
    }

    private void migrateStaticSystemToLockWallpaperLocked(int userId) {
        migrateStaticSystemToLockWallpaperLocked(userId, 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void migrateStaticSystemToLockWallpaperLocked(int userId, int phyDisplayId) {
        com.android.server.wallpaper.WallpaperData sysWP = this.mWallpaperManagerServiceExt.getWallpaperMap(1, phyDisplayId, this.mWallpaperMap).get(userId);
        if (sysWP == null) {
            if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                android.util.Slog.i(TAG, "No system wallpaper? Not tracking for lock-only");
                return;
            }
            return;
        }
        com.android.server.wallpaper.WallpaperData lockWP = new com.android.server.wallpaper.WallpaperData(userId, this.mWallpaperManagerServiceExt.getWhichValue(2, phyDisplayId));
        lockWP.mWallpaperDataExt = sysWP.mWallpaperDataExt;
        lockWP.wallpaperId = sysWP.wallpaperId;
        lockWP.cropHint.set(sysWP.cropHint);
        if (sysWP.mCropHints != null) {
            lockWP.mCropHints = sysWP.mCropHints.clone();
        }
        lockWP.allowBackup = sysWP.allowBackup;
        lockWP.primaryColors = sysWP.primaryColors;
        lockWP.mWallpaperDimAmount = sysWP.mWallpaperDimAmount;
        lockWP.mWhich = 2;
        try {
            if (sysWP.getWallpaperFile().exists()) {
                android.system.Os.rename(sysWP.getWallpaperFile().getAbsolutePath(), lockWP.getWallpaperFile().getAbsolutePath());
            }
            if (sysWP.getCropFile().exists()) {
                android.system.Os.rename(sysWP.getCropFile().getAbsolutePath(), lockWP.getCropFile().getAbsolutePath());
            }
            android.util.SparseArray<com.android.server.wallpaper.WallpaperData> lockWallpaperMap = this.mWallpaperManagerServiceExt.getWallpaperMap(lockWP.mWhich, phyDisplayId, this.mLockWallpaperMap);
            lockWallpaperMap.put(userId, lockWP);
            android.util.Slog.d(TAG, "migrateStaticSystemToLockWallpaperLocked:  " + lockWP);
            android.os.SELinux.restorecon(lockWP.getWallpaperFile());
            this.mLastLockWallpaper = lockWP;
        } catch (android.system.ErrnoException e) {
            android.util.Slog.w(TAG, "Couldn't migrate system wallpaper: " + e.getMessage());
            clearWallpaperBitmaps(lockWP);
        }
    }

    android.os.ParcelFileDescriptor updateWallpaperBitmapLocked(java.lang.String name, com.android.server.wallpaper.WallpaperData wallpaper, android.os.Bundle extras) {
        if (name == null) {
            name = "";
        }
        try {
            java.io.File dir = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperDir(wallpaper.userId, wallpaper.mWallpaperDataExt.getPhysicalDisplayId(), com.android.server.wallpaper.WallpaperUtils.getWallpaperDir(wallpaper.userId));
            if (!dir.exists()) {
                dir.mkdir();
                android.os.FileUtils.setPermissions(dir.getPath(), 505, -1, -1);
            }
            android.os.ParcelFileDescriptor fd = android.os.ParcelFileDescriptor.open(wallpaper.getWallpaperFile(), 1006632960);
            if (!android.os.SELinux.restorecon(wallpaper.getWallpaperFile())) {
                android.util.Slog.w(TAG, "restorecon failed for wallpaper file: " + wallpaper.getWallpaperFile().getPath());
                return null;
            }
            wallpaper.name = name;
            wallpaper.wallpaperId = com.android.server.wallpaper.WallpaperUtils.makeWallpaperIdLocked();
            if (extras != null) {
                extras.putInt("android.service.wallpaper.extra.ID", wallpaper.wallpaperId);
            }
            wallpaper.primaryColors = null;
            android.util.Slog.v(TAG, "updateWallpaperBitmapLocked() : id=" + wallpaper.wallpaperId + " name=" + name + " file=" + wallpaper.getWallpaperFile().getAbsolutePath());
            return fd;
        } catch (java.io.FileNotFoundException e) {
            android.util.Slog.w(TAG, "Error setting wallpaper", e);
            return null;
        }
    }

    public void setWallpaperComponentChecked(android.content.ComponentName name, java.lang.String callingPackage, int which, int userId) {
        if (isWallpaperSupported(callingPackage) && isSetWallpaperAllowed(callingPackage)) {
            android.util.Slog.i(TAG, "setWallpaperComponentChecked, , which: " + which + " name: " + name + ", callingPackage: " + callingPackage + ", userId: " + userId);
            int which2 = this.mWallpaperManagerServiceWrapper.getExtImpl().doRectifyWhich(which);
            if (!this.mWallpaperManagerServiceWrapper.getExtImpl().setWallpaperComponent(name, callingPackage, which2, userId)) {
                setWallpaperComponent(name, callingPackage, which2, userId);
            }
        }
    }

    public void setWallpaperComponent(android.content.ComponentName name) {
        setWallpaperComponent(name, "", android.os.UserHandle.getCallingUserId(), 1);
    }

    boolean setWallpaperComponent(android.content.ComponentName name, java.lang.String callingPackage, int which, int userId) {
        boolean fromForeground = isFromForegroundApp(callingPackage);
        return setWallpaperComponentInternal(name, which, userId, false, fromForeground, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:103:0x02a5  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x02b0  */
    /* JADX WARN: Removed duplicated region for block: B:109:0x02b5  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x0168 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0134 A[Catch: all -> 0x0137, TRY_LEAVE, TryCatch #3 {all -> 0x0137, blocks: (B:26:0x011f, B:33:0x0134), top: B:133:0x011f }] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x013f A[Catch: all -> 0x02c0, TRY_ENTER, TryCatch #0 {all -> 0x02c0, blocks: (B:22:0x0106, B:38:0x0141, B:37:0x013f), top: B:128:0x0106 }] */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0196 A[Catch: all -> 0x02a1, TryCatch #1 {all -> 0x02a1, blocks: (B:52:0x0188, B:53:0x018b, B:55:0x0196, B:60:0x01aa, B:62:0x01bc, B:63:0x01c2, B:69:0x01d1, B:74:0x01e2, B:76:0x01ee, B:78:0x01f2, B:79:0x01f9, B:81:0x0237, B:83:0x023c, B:85:0x0241, B:86:0x0248, B:87:0x025a, B:89:0x0261, B:90:0x026a, B:92:0x0274, B:93:0x0283, B:95:0x0287, B:99:0x029b, B:48:0x0177, B:50:0x017b), top: B:130:0x0168 }] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x01bc A[Catch: all -> 0x02a1, TryCatch #1 {all -> 0x02a1, blocks: (B:52:0x0188, B:53:0x018b, B:55:0x0196, B:60:0x01aa, B:62:0x01bc, B:63:0x01c2, B:69:0x01d1, B:74:0x01e2, B:76:0x01ee, B:78:0x01f2, B:79:0x01f9, B:81:0x0237, B:83:0x023c, B:85:0x0241, B:86:0x0248, B:87:0x025a, B:89:0x0261, B:90:0x026a, B:92:0x0274, B:93:0x0283, B:95:0x0287, B:99:0x029b, B:48:0x0177, B:50:0x017b), top: B:130:0x0168 }] */
    /* JADX WARN: Removed duplicated region for block: B:65:0x01c9  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01ee A[Catch: all -> 0x02a1, TryCatch #1 {all -> 0x02a1, blocks: (B:52:0x0188, B:53:0x018b, B:55:0x0196, B:60:0x01aa, B:62:0x01bc, B:63:0x01c2, B:69:0x01d1, B:74:0x01e2, B:76:0x01ee, B:78:0x01f2, B:79:0x01f9, B:81:0x0237, B:83:0x023c, B:85:0x0241, B:86:0x0248, B:87:0x025a, B:89:0x0261, B:90:0x026a, B:92:0x0274, B:93:0x0283, B:95:0x0287, B:99:0x029b, B:48:0x0177, B:50:0x017b), top: B:130:0x0168 }] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0261 A[Catch: all -> 0x02a1, TryCatch #1 {all -> 0x02a1, blocks: (B:52:0x0188, B:53:0x018b, B:55:0x0196, B:60:0x01aa, B:62:0x01bc, B:63:0x01c2, B:69:0x01d1, B:74:0x01e2, B:76:0x01ee, B:78:0x01f2, B:79:0x01f9, B:81:0x0237, B:83:0x023c, B:85:0x0241, B:86:0x0248, B:87:0x025a, B:89:0x0261, B:90:0x026a, B:92:0x0274, B:93:0x0283, B:95:0x0287, B:99:0x029b, B:48:0x0177, B:50:0x017b), top: B:130:0x0168 }] */
    /* JADX WARN: Removed duplicated region for block: B:92:0x0274 A[Catch: all -> 0x02a1, TryCatch #1 {all -> 0x02a1, blocks: (B:52:0x0188, B:53:0x018b, B:55:0x0196, B:60:0x01aa, B:62:0x01bc, B:63:0x01c2, B:69:0x01d1, B:74:0x01e2, B:76:0x01ee, B:78:0x01f2, B:79:0x01f9, B:81:0x0237, B:83:0x023c, B:85:0x0241, B:86:0x0248, B:87:0x025a, B:89:0x0261, B:90:0x026a, B:92:0x0274, B:93:0x0283, B:95:0x0287, B:99:0x029b, B:48:0x0177, B:50:0x017b), top: B:130:0x0168 }] */
    /* JADX WARN: Removed duplicated region for block: B:95:0x0287 A[Catch: all -> 0x02a1, TryCatch #1 {all -> 0x02a1, blocks: (B:52:0x0188, B:53:0x018b, B:55:0x0196, B:60:0x01aa, B:62:0x01bc, B:63:0x01c2, B:69:0x01d1, B:74:0x01e2, B:76:0x01ee, B:78:0x01f2, B:79:0x01f9, B:81:0x0237, B:83:0x023c, B:85:0x0241, B:86:0x0248, B:87:0x025a, B:89:0x0261, B:90:0x026a, B:92:0x0274, B:93:0x0283, B:95:0x0287, B:99:0x029b, B:48:0x0177, B:50:0x017b), top: B:130:0x0168 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean setWallpaperComponentInternal(android.content.ComponentName r28, int r29, int r30, boolean r31, boolean r32, android.os.IRemoteCallback r33) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 757
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wallpaper.WallpaperManagerService.setWallpaperComponentInternal(android.content.ComponentName, int, int, boolean, boolean, android.os.IRemoteCallback):boolean");
    }

    static /* synthetic */ void lambda$setWallpaperComponentInternal$15(com.android.server.wallpaper.WallpaperManagerService.DisplayConnector displayConnector) {
        try {
            if (displayConnector.mEngine != null) {
                displayConnector.mEngine.dispatchWallpaperCommand("android.wallpaper.reapply", 0, 0, 0, (android.os.Bundle) null);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Error sending apply message to wallpaper", e);
        }
    }

    private boolean isDefaultComponent(android.content.ComponentName name) {
        return name == null || name.equals(this.mDefaultWallpaperComponent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean changingToSame(android.content.ComponentName componentName, com.android.server.wallpaper.WallpaperData wallpaper) {
        if (wallpaper.connection != null) {
            android.content.ComponentName wallpaperName = wallpaper.wallpaperComponent;
            if (isDefaultComponent(componentName) && isDefaultComponent(wallpaperName)) {
                if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                    android.util.Slog.v(TAG, "changingToSame: still using default");
                }
                return true;
            }
            if (wallpaperName != null && wallpaperName.equals(componentName)) {
                if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                    android.util.Slog.v(TAG, "same wallpaper");
                }
                return true;
            }
            return false;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:153:0x03b0 A[Catch: all -> 0x03bf, TRY_LEAVE, TryCatch #9 {all -> 0x03bf, blocks: (B:47:0x00ca, B:51:0x00d5, B:53:0x00ee, B:56:0x0110, B:58:0x011a, B:60:0x012f, B:63:0x0138, B:64:0x013d, B:65:0x013e, B:67:0x014a, B:69:0x0152, B:70:0x0173, B:72:0x0179, B:74:0x018c, B:76:0x0196, B:80:0x01a9, B:83:0x01b2, B:84:0x01b7, B:87:0x01bb, B:90:0x01c4, B:91:0x01c9, B:92:0x01ca, B:94:0x01cf, B:96:0x01e4, B:99:0x01ed, B:100:0x01f2, B:104:0x01fe, B:106:0x0204, B:108:0x0212, B:110:0x021d, B:112:0x0232, B:115:0x023b, B:116:0x0240, B:117:0x0241, B:121:0x0253, B:123:0x02da, B:124:0x02df, B:126:0x02fe, B:127:0x0301, B:129:0x0326, B:131:0x033b, B:134:0x0344, B:135:0x0349, B:136:0x034a, B:138:0x035c, B:139:0x035f, B:141:0x037e, B:142:0x0381, B:151:0x0391, B:153:0x03b0, B:156:0x03b9, B:157:0x03be), top: B:164:0x006b }] */
    /* JADX WARN: Removed duplicated region for block: B:156:0x03b9 A[Catch: all -> 0x03bf, TRY_ENTER, TryCatch #9 {all -> 0x03bf, blocks: (B:47:0x00ca, B:51:0x00d5, B:53:0x00ee, B:56:0x0110, B:58:0x011a, B:60:0x012f, B:63:0x0138, B:64:0x013d, B:65:0x013e, B:67:0x014a, B:69:0x0152, B:70:0x0173, B:72:0x0179, B:74:0x018c, B:76:0x0196, B:80:0x01a9, B:83:0x01b2, B:84:0x01b7, B:87:0x01bb, B:90:0x01c4, B:91:0x01c9, B:92:0x01ca, B:94:0x01cf, B:96:0x01e4, B:99:0x01ed, B:100:0x01f2, B:104:0x01fe, B:106:0x0204, B:108:0x0212, B:110:0x021d, B:112:0x0232, B:115:0x023b, B:116:0x0240, B:117:0x0241, B:121:0x0253, B:123:0x02da, B:124:0x02df, B:126:0x02fe, B:127:0x0301, B:129:0x0326, B:131:0x033b, B:134:0x0344, B:135:0x0349, B:136:0x034a, B:138:0x035c, B:139:0x035f, B:141:0x037e, B:142:0x0381, B:151:0x0391, B:153:0x03b0, B:156:0x03b9, B:157:0x03be), top: B:164:0x006b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    boolean bindWallpaperComponentLocked(android.content.ComponentName r25, boolean r26, boolean r27, com.android.server.wallpaper.WallpaperData r28, android.os.IRemoteCallback r29) throws android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 964
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wallpaper.WallpaperManagerService.bindWallpaperComponentLocked(android.content.ComponentName, boolean, boolean, com.android.server.wallpaper.WallpaperData, android.os.IRemoteCallback):boolean");
    }

    private android.os.Handler getHandlerForBindingWallpaperLocked() {
        if (!android.multiuser.Flags.bindWallpaperServiceOnItsOwnThreadDuringAUserSwitch()) {
            return this.mContext.getMainThreadHandler();
        }
        if (this.mInitialUserSwitch) {
            return this.mContext.getMainThreadHandler();
        }
        if (this.mHandlerThread == null) {
            this.mHandlerThread = new com.android.server.ServiceThread(TAG, -2, true);
            this.mHandlerThread.start();
        }
        return this.mHandlerThread.getThreadHandler();
    }

    private void updateCurrentWallpapers(com.android.server.wallpaper.WallpaperData newWallpaper) {
        if (newWallpaper.userId != this.mCurrentUserId || newWallpaper.equals(this.mFallbackWallpaper)) {
            return;
        }
        if (newWallpaper.mWhich == 3) {
            this.mLastWallpaper = newWallpaper;
        } else if (newWallpaper.mWhich == 1) {
            this.mLastWallpaper = newWallpaper;
        } else if (newWallpaper.mWhich == 2) {
            this.mLastLockWallpaper = newWallpaper;
        }
    }

    private void maybeDetachLastWallpapers(com.android.server.wallpaper.WallpaperData newWallpaper) {
        if (newWallpaper.userId != this.mCurrentUserId || newWallpaper.equals(this.mFallbackWallpaper)) {
            android.util.Slog.w(TAG, "maybeDetachLastWallpapers, newWallpaper.userId:" + newWallpaper.userId + " " + this.mCurrentUserId + ", " + newWallpaper + "  " + this.mFallbackWallpaper + ", return");
            return;
        }
        boolean homeUpdated = (newWallpaper.mWhich & 1) != 0;
        boolean lockUpdated = (newWallpaper.mWhich & 2) != 0;
        boolean systemWillBecomeLock = newWallpaper.mSystemWasBoth && !lockUpdated;
        if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
            android.util.Slog.d(TAG, "maybeDetachLastWallpapers homeUpdated: " + homeUpdated + ", lockUpdated: " + lockUpdated + ", systemWillBecomeLock: " + systemWillBecomeLock);
        }
        if (homeUpdated && !systemWillBecomeLock) {
            detachWallpaperLocked(this.mLastWallpaper);
        }
        if (lockUpdated) {
            detachWallpaperLocked(this.mLastLockWallpaper);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void detachWallpaperLocked(com.android.server.wallpaper.WallpaperData wallpaper) {
        if (this.mWallpaperManagerServiceWrapper.getExtImpl().detachSharedWallpaperLocked(wallpaper)) {
            android.util.Log.d(TAG, "do not detachWallpaperLocked:" + wallpaper);
        } else {
            detachWallpaperLockedInner(wallpaper);
        }
    }

    private void detachWallpaperLockedInner(final com.android.server.wallpaper.WallpaperData wallpaper) {
        if (wallpaper != null && wallpaper.connection != null) {
            android.util.Slog.v(TAG, "Detaching wallpaper: " + wallpaper);
            if (wallpaper.connection.mReply != null) {
                try {
                    wallpaper.connection.mReply.sendResult((android.os.Bundle) null);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Error sending reply to wallpaper before disconnect", e);
                }
                wallpaper.connection.mReply = null;
            }
            wallpaper.connection.forEachDisplayConnector(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda6
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) obj).disconnectLocked(wallpaper.connection);
                }
            });
            wallpaper.connection.mService = null;
            wallpaper.connection.mDisplayConnector.clear();
            com.android.server.FgThread.getHandler().removeCallbacks(wallpaper.connection.mResetRunnable);
            this.mContext.getMainThreadHandler().removeCallbacks(wallpaper.connection.mDisconnectRunnable);
            this.mContext.getMainThreadHandler().removeCallbacks(wallpaper.connection.mTryToRebindRunnable);
            try {
                if (!this.mWallpaperManagerServiceExt.unBindServiceForSeparateWallpaper(this.mContext, wallpaper)) {
                    android.util.Slog.d(TAG, "unbindService: " + wallpaper.connection);
                    this.mContext.unbindService(wallpaper.connection);
                }
            } catch (java.lang.IllegalArgumentException e2) {
                android.util.Slog.w(TAG, "Error unbinding wallpaper when detaching", e2);
            }
            wallpaper.connection = null;
            if (wallpaper == this.mLastWallpaper) {
                this.mLastWallpaper = null;
            }
            if (wallpaper == this.mLastLockWallpaper) {
                this.mLastLockWallpaper = null;
            }
            this.mWallpaperManagerServiceWrapper.getExtImpl().removeLastWallpaperLocked(wallpaper);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEngineFlags(final com.android.server.wallpaper.WallpaperData wallpaper) {
        if (wallpaper.connection == null) {
            return;
        }
        wallpaper.connection.forEachDisplayConnector(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda10
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$updateEngineFlags$17(wallpaper, (com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateEngineFlags$17(com.android.server.wallpaper.WallpaperData wallpaper, com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector) {
        try {
            if (connector.mEngine != null) {
                if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                    android.util.Slog.v(TAG, "updateEngineFlags wallpaper: " + wallpaper);
                }
                connector.mEngine.setWallpaperFlags(wallpaper.mWhich);
                this.mWindowManagerInternal.setWallpaperShowWhenLocked(connector.mToken, (wallpaper.mWhich & 2) != 0);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to update wallpaper engine flags", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: clearWallpaperComponentLocked, reason: merged with bridge method [inline-methods] */
    public void lambda$clearWallpaperLocked$9(com.android.server.wallpaper.WallpaperData wallpaper) {
        wallpaper.wallpaperComponent = null;
        detachWallpaperLocked(wallpaper);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void attachServiceLocked(final com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection conn, final com.android.server.wallpaper.WallpaperData wallpaper) {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog(TAG);
        t.traceBegin("WPMS.attachServiceLocked");
        conn.forEachDisplayConnector(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) throws android.os.RemoteException {
                ((com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) obj).connectLocked(conn, wallpaper);
            }
        });
        t.traceEnd();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyCallbacksLocked(com.android.server.wallpaper.WallpaperData wallpaper) {
        int n = wallpaper.callbacks.beginBroadcast();
        for (int i = 0; i < n; i++) {
            try {
                wallpaper.callbacks.getBroadcastItem(i).onWallpaperChanged();
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed to notify callbacks about wallpaper changes", e);
            }
        }
        wallpaper.callbacks.finishBroadcast();
        android.content.Intent intent = new android.content.Intent("android.intent.action.WALLPAPER_CHANGED");
        intent.putExtra("android.service.wallpaper.extra.FROM_FOREGROUND_APP", wallpaper.fromForegroundApp);
        this.mContext.sendBroadcastAsUser(intent, new android.os.UserHandle(this.mCurrentUserId));
    }

    private void checkPermission(java.lang.String permission) {
        if (!hasPermission(permission)) {
            throw new java.lang.SecurityException("Access denied to process: " + android.os.Binder.getCallingPid() + ", must have permission " + permission);
        }
    }

    private boolean packageBelongsToUid(java.lang.String packageName, int uid) {
        int userId = android.os.UserHandle.getUserId(uid);
        try {
            int packageUid = this.mContext.getPackageManager().getPackageUidAsUser(packageName, userId);
            return packageUid == uid;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void enforcePackageBelongsToUid(java.lang.String packageName, int uid) {
        if (!packageBelongsToUid(packageName, uid)) {
            throw new java.lang.IllegalArgumentException("Invalid package or package does not belong to uid:" + uid);
        }
    }

    private boolean isFromForegroundApp(final java.lang.String callingPackage) {
        return ((java.lang.Boolean) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda19
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$isFromForegroundApp$19(callingPackage);
            }
        })).booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$isFromForegroundApp$19(java.lang.String callingPackage) throws java.lang.Exception {
        return java.lang.Boolean.valueOf(this.mActivityManager.getPackageImportance(callingPackage) == 100);
    }

    private void checkCallerIsSystemOrSystemUi() {
        if (android.os.Binder.getCallingUid() != android.os.Process.myUid() && this.mContext.checkCallingPermission("android.permission.STATUS_BAR_SERVICE") != 0) {
            throw new java.lang.SecurityException("Access denied: only system processes can call this");
        }
    }

    public boolean isWallpaperSupported(java.lang.String callingPackage) {
        int callingUid = android.os.Binder.getCallingUid();
        enforcePackageBelongsToUid(callingPackage, callingUid);
        return this.mAppOpsManager.checkOpNoThrow(48, callingUid, callingPackage) == 0;
    }

    public boolean isSetWallpaperAllowed(java.lang.String callingPackage) {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        java.lang.String[] uidPackages = pm.getPackagesForUid(android.os.Binder.getCallingUid());
        boolean uidMatchPackage = java.util.Arrays.asList(uidPackages).contains(callingPackage);
        if (!uidMatchPackage) {
            return false;
        }
        android.app.admin.DevicePolicyManagerInternal dpmi = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        if (dpmi != null && dpmi.isDeviceOrProfileOwnerInCallingUser(callingPackage)) {
            return true;
        }
        if (!this.mWallpaperManagerServiceWrapper.getExtImpl().isSetWallpaperAllowed(callingPackage, this.mContext)) {
            return false;
        }
        int callingUserId = android.os.UserHandle.getCallingUserId();
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.pm.UserManagerInternal umi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            return true ^ umi.hasUserRestriction("no_set_wallpaper", callingUserId);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public boolean isWallpaperBackupEligible(int which, int userId) {
        com.android.server.wallpaper.WallpaperData wallpaper;
        if (which == 2) {
            wallpaper = this.mLockWallpaperMap.get(userId);
        } else {
            wallpaper = this.mWallpaperMap.get(userId);
        }
        if (wallpaper != null) {
            return wallpaper.allowBackup;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDisplayReadyInternal(int displayId) {
        synchronized (this.mLock) {
            if (this.mLastWallpaper == null) {
                return;
            }
            if (supportsMultiDisplay(this.mLastWallpaper.connection)) {
                com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector = this.mLastWallpaper.connection.getDisplayConnectorOrCreate(displayId);
                if (connector == null) {
                    return;
                }
                connector.connectLocked(this.mLastWallpaper.connection, this.mLastWallpaper);
                return;
            }
            if (this.mFallbackWallpaper != null && this.mFallbackWallpaper.connection != null) {
                com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector2 = this.mFallbackWallpaper.connection.getDisplayConnectorOrCreate(displayId);
                if (connector2 == null) {
                } else {
                    connector2.connectLocked(this.mFallbackWallpaper.connection, this.mFallbackWallpaper);
                }
            } else {
                android.util.Slog.w(TAG, "No wallpaper can be added to the new display");
            }
        }
    }

    void saveSettingsLocked(int userId) {
        com.android.server.utils.TimingsTraceAndSlog t = new com.android.server.utils.TimingsTraceAndSlog(TAG);
        t.traceBegin("WPMS.saveSettingsLocked-" + userId);
        int phyDisplayId = this.mWallpaperManagerServiceWrapper.getExtImpl().getCachePhysicalDisplayId();
        android.util.Slog.d(TAG, "saveSettingsLocked:  phyDisplayId " + phyDisplayId + " system " + this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(1, phyDisplayId, this.mWallpaperMap).get(userId) + " lock " + this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, phyDisplayId, this.mLockWallpaperMap).get(userId));
        this.mWallpaperDataParser.saveSettingsLocked(userId, this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(1, phyDisplayId, this.mWallpaperMap).get(userId), this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, phyDisplayId, this.mLockWallpaperMap).get(userId));
        t.traceEnd();
    }

    com.android.server.wallpaper.WallpaperData getWallpaperSafeLocked(int userId, int which) throws android.os.RemoteException {
        android.util.SparseArray<com.android.server.wallpaper.WallpaperData> whichSet;
        int phyDisplayId = this.mWallpaperManagerServiceWrapper.getExtImpl().getCachePhysicalDisplayId();
        int which2 = this.mWallpaperManagerServiceWrapper.getManagerExtImpl().getWallpaperType(which);
        if (which2 == 2) {
            whichSet = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, phyDisplayId, this.mLockWallpaperMap);
        } else {
            whichSet = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(1, phyDisplayId, this.mWallpaperMap);
        }
        com.android.server.wallpaper.WallpaperData wallpaper = whichSet.get(userId);
        if (wallpaper == null) {
            int whichLoad = which2 == 2 ? 2 : 3;
            loadSettingsLocked(userId, false, whichLoad);
            com.android.server.wallpaper.WallpaperData wallpaper2 = whichSet.get(userId);
            if (wallpaper2 == null) {
                if (which2 == 2) {
                    com.android.server.wallpaper.WallpaperData wallpaper3 = new com.android.server.wallpaper.WallpaperData(userId, this.mWallpaperManagerServiceWrapper.getExtImpl().getWhichValue(2, phyDisplayId));
                    wallpaper3.mWallpaperDataExt.setPhysicalDisplayId(phyDisplayId);
                    whichSet.put(userId, wallpaper3);
                    return wallpaper3;
                }
                android.util.Slog.wtf(TAG, "Didn't find wallpaper in non-lock case!");
                com.android.server.wallpaper.WallpaperData wallpaper4 = new com.android.server.wallpaper.WallpaperData(userId, this.mWallpaperManagerServiceWrapper.getExtImpl().getWhichValue(1, phyDisplayId));
                wallpaper4.mWallpaperDataExt.setPhysicalDisplayId(phyDisplayId);
                whichSet.put(userId, wallpaper4);
                return wallpaper4;
            }
            return wallpaper2;
        }
        return wallpaper;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loadSettingsLocked(int userId, boolean keepDimensionHints, int which) throws android.os.RemoteException {
        initializeFallbackWallpaper();
        boolean restoreFromOld = !this.mWallpaperMap.contains(userId);
        com.android.server.wallpaper.WallpaperDataParser.WallpaperLoadingResult result = this.mWallpaperDataParser.loadSettingsLocked(userId, keepDimensionHints, restoreFromOld, which);
        boolean updateSystem = (which & 1) != 0;
        boolean updateLock = (which & 2) != 0;
        int phyDisplayId = this.mWallpaperManagerServiceWrapper.getExtImpl().getCachePhysicalDisplayId();
        android.util.Slog.d(TAG, "loadSettingsLocked: " + updateSystem + " " + updateLock + "  " + phyDisplayId + " result.getSystemWallpaperData() " + result.getSystemWallpaperData() + " result.getLockWallpaperData() " + result.getLockWallpaperData());
        if (updateSystem) {
            this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(1, phyDisplayId, this.mWallpaperMap).put(userId, result.getSystemWallpaperData());
        }
        if (updateLock) {
            if (result.success()) {
                this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, phyDisplayId, this.mLockWallpaperMap).put(userId, result.getLockWallpaperData());
            } else {
                this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpaperMap(2, phyDisplayId, this.mLockWallpaperMap).remove(userId);
            }
        }
        this.mWallpaperManagerServiceWrapper.getExtImpl().onLoadSettingsEnd(result.getSystemWallpaperData());
    }

    private void initializeFallbackWallpaper() throws android.os.RemoteException {
        if (this.mFallbackWallpaper == null) {
            if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                android.util.Slog.d(TAG, "Initialize fallback wallpaper");
            }
            this.mFallbackWallpaper = new com.android.server.wallpaper.WallpaperData(0, 1);
            this.mFallbackWallpaper.allowBackup = false;
            this.mFallbackWallpaper.wallpaperId = com.android.server.wallpaper.WallpaperUtils.makeWallpaperIdLocked();
            this.mFallbackWallpaper.mBindSource = com.android.server.wallpaper.WallpaperData.BindSource.INITIALIZE_FALLBACK;
            bindWallpaperComponentLocked(this.mDefaultWallpaperComponent, true, false, this.mFallbackWallpaper, null);
        }
    }

    public void settingsRestored() {
        com.android.server.wallpaper.WallpaperData wallpaper;
        boolean success;
        if (android.os.Binder.getCallingUid() != 1000) {
            throw new java.lang.RuntimeException("settingsRestored() can only be called from the system process");
        }
        if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
            android.util.Slog.v(TAG, "settingsRestored");
        }
        synchronized (this.mLock) {
            loadSettingsLocked(0, false, 3);
            wallpaper = this.mWallpaperMap.get(0);
            wallpaper.wallpaperId = com.android.server.wallpaper.WallpaperUtils.makeWallpaperIdLocked();
            wallpaper.allowBackup = true;
            if (wallpaper.nextWallpaperComponent != null && !wallpaper.nextWallpaperComponent.equals(this.mImageWallpaper)) {
                wallpaper.mBindSource = com.android.server.wallpaper.WallpaperData.BindSource.RESTORE_SETTINGS_LIVE_SUCCESS;
                if (!bindWallpaperComponentLocked(wallpaper.nextWallpaperComponent, false, false, wallpaper, null)) {
                    wallpaper.mBindSource = com.android.server.wallpaper.WallpaperData.BindSource.RESTORE_SETTINGS_LIVE_FAILURE;
                    bindWallpaperComponentLocked(null, false, false, wallpaper, null);
                }
                success = true;
            } else {
                if ("".equals(wallpaper.name)) {
                    if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                        android.util.Slog.v(TAG, "settingsRestored: name is empty");
                    }
                    success = true;
                } else {
                    if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                        android.util.Slog.v(TAG, "settingsRestored: attempting to restore named resource");
                    }
                    success = this.mWallpaperDataParser.restoreNamedResourceLocked(wallpaper);
                }
                if (com.android.server.wallpaper.WallpaperUtils.DEBUG) {
                    android.util.Slog.v(TAG, "settingsRestored: success=" + success + " id=" + wallpaper.wallpaperId);
                }
                if (success) {
                    this.mWallpaperCropper.generateCrop(wallpaper);
                    wallpaper.mBindSource = com.android.server.wallpaper.WallpaperData.BindSource.RESTORE_SETTINGS_STATIC;
                    bindWallpaperComponentLocked(wallpaper.nextWallpaperComponent, true, false, wallpaper, null);
                }
            }
        }
        if (!success) {
            android.util.Slog.e(TAG, "Failed to restore wallpaper: '" + wallpaper.name + "'");
            wallpaper.name = "";
            com.android.server.wallpaper.WallpaperUtils.getWallpaperDir(0).delete();
        }
        synchronized (this.mLock) {
            saveSettingsLocked(0);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        new com.android.server.wallpaper.WallpaperManagerShellCommand(this).exec(this, in, out, err, args, callback, resultReceiver);
    }

    private void dumpWallpaper(com.android.server.wallpaper.WallpaperData wallpaper, final java.io.PrintWriter pw) {
        if (wallpaper == null) {
            pw.println(" (null entry)");
            return;
        }
        pw.print(" User ");
        pw.print(wallpaper.userId);
        pw.print(": id=");
        pw.print(wallpaper.wallpaperId);
        pw.print(": mWhich=");
        pw.print(wallpaper.mWhich);
        pw.print(": mSystemWasBoth=");
        pw.print(wallpaper.mSystemWasBoth);
        pw.print(": mBindSource=");
        pw.println(wallpaper.mBindSource.name());
        pw.println(" Display state:");
        pw.println(" setting display:" + wallpaper.mWallpaperDataExt.getPhysicalDisplayId());
        this.mWallpaperDisplayHelper.forEachDisplayData(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda21
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.wallpaper.WallpaperManagerService.lambda$dumpWallpaper$20(pw, (com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData) obj);
            }
        });
        pw.print("  mCropHint=");
        pw.println(wallpaper.cropHint);
        pw.print("  mName=");
        pw.println(wallpaper.name);
        pw.print("  mAllowBackup=");
        pw.println(wallpaper.allowBackup);
        pw.print("  mWallpaperComponent=");
        pw.println(wallpaper.wallpaperComponent);
        pw.print("  mWallpaperDimAmount=");
        pw.println(wallpaper.mWallpaperDimAmount);
        pw.print("  isColorExtracted=");
        pw.println(wallpaper.mIsColorExtractedFromDim);
        pw.println("  mUidToDimAmount:");
        for (int j = 0; j < wallpaper.mUidToDimAmount.size(); j++) {
            pw.print("    UID=");
            pw.print(wallpaper.mUidToDimAmount.keyAt(j));
            pw.print(" dimAmount=");
            pw.println(wallpaper.mUidToDimAmount.valueAt(j));
        }
        if (wallpaper.connection != null) {
            com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection conn = wallpaper.connection;
            pw.print("  Wallpaper connection ");
            pw.print(conn);
            pw.println(":");
            if (conn.mInfo != null) {
                pw.print("    mInfo.component=");
                pw.println(conn.mInfo.getComponent());
            }
            conn.forEachDisplayConnector(new java.util.function.Consumer() { // from class: com.android.server.wallpaper.WallpaperManagerService$$ExternalSyntheticLambda22
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.wallpaper.WallpaperManagerService.lambda$dumpWallpaper$21(pw, (com.android.server.wallpaper.WallpaperManagerService.DisplayConnector) obj);
                }
            });
            pw.print("    mService=");
            pw.println(conn.mService);
            pw.print("    mLastDiedTime=");
            pw.println(wallpaper.lastDiedTime - android.os.SystemClock.uptimeMillis());
        }
        pw.print(" cropFile=");
        pw.println(wallpaper.getCropFile().exists() ? wallpaper.getCropFile().getAbsolutePath() : "");
        pw.print(" wallpaperFile=");
        pw.println(wallpaper.getWallpaperFile().exists() ? wallpaper.getWallpaperFile().getAbsolutePath() : "");
    }

    static /* synthetic */ void lambda$dumpWallpaper$20(java.io.PrintWriter pw, com.android.server.wallpaper.WallpaperDisplayHelper.DisplayData wpSize) {
        pw.print("  displayId=");
        pw.println(wpSize.mDisplayId);
        pw.print("  mWidth=");
        pw.print(wpSize.mWidth);
        pw.print("  mHeight=");
        pw.println(wpSize.mHeight);
        pw.print("  mPadding=");
        pw.println(wpSize.mPadding);
    }

    static /* synthetic */ void lambda$dumpWallpaper$21(java.io.PrintWriter pw, com.android.server.wallpaper.WallpaperManagerService.DisplayConnector connector) {
        pw.print("     mDisplayId=");
        pw.println(connector.mDisplayId);
        pw.print("     mToken=");
        pw.println(connector.mToken);
        pw.print("     mEngine=");
        pw.println(connector.mEngine);
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, pw)) {
            pw.print("mDefaultWallpaperComponent=");
            pw.println(this.mDefaultWallpaperComponent);
            pw.print("mImageWallpaper=");
            pw.println(this.mImageWallpaper);
            synchronized (this.mLock) {
                pw.println("System wallpaper state:");
                java.util.List<com.android.server.wallpaper.WallpaperData> sysWallpapers = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpapersLocked(1, this.mWallpaperMap);
                for (com.android.server.wallpaper.WallpaperData wallpaper : sysWallpapers) {
                    dumpWallpaper(wallpaper, pw);
                }
                pw.println("Lock wallpaper state:");
                java.util.List<com.android.server.wallpaper.WallpaperData> lockWallpapers = this.mWallpaperManagerServiceWrapper.getExtImpl().getWallpapersLocked(2, this.mLockWallpaperMap);
                for (com.android.server.wallpaper.WallpaperData wallpaper2 : lockWallpapers) {
                    dumpWallpaper(wallpaper2, pw);
                }
                pw.println("Fallback wallpaper state:");
                if (this.mFallbackWallpaper != null) {
                    dumpWallpaper(this.mFallbackWallpaper, pw);
                }
                pw.println("Last wallpaper state:");
                pw.println("mLastWallpaper：" + this.mLastWallpaper);
                pw.println("mLastLockWallpaper：" + this.mLastLockWallpaper);
                this.mWallpaperManagerServiceExt.dump(fd, pw, args);
            }
        }
    }

    public com.android.server.wallpaper.IWallpaperManagerServiceWrapper getWrapper() {
        return this.mWallpaperManagerServiceWrapper;
    }

    private class WallpaperManagerServiceWrapperImpl implements com.android.server.wallpaper.IWallpaperManagerServiceWrapper {
        private WallpaperManagerServiceWrapperImpl() {
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public com.android.server.wallpaper.IWallpaperManagerServiceExt getExtImpl() {
            return com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerServiceExt;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.app.IWallpaperManagerExt getManagerExtImpl() {
            return com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperManagerExt;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void removeDisplayData(int displayId) {
            com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperDisplayHelper.removeDisplayData(displayId);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public android.content.Context getContext() {
            return com.android.server.wallpaper.WallpaperManagerService.this.mContext;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public java.lang.Object getLock() {
            return com.android.server.wallpaper.WallpaperManagerService.this.mLock;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public android.content.ComponentName getImageWallpaper() {
            return com.android.server.wallpaper.WallpaperManagerService.this.mImageWallpaper;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void notifyCallbacksLocked(com.android.server.wallpaper.WallpaperData wpData) {
            com.android.server.wallpaper.WallpaperManagerService.this.notifyCallbacksLocked(wpData);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public int getCurrentUserId() {
            return com.android.server.wallpaper.WallpaperManagerService.this.mCurrentUserId;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public android.util.SparseArray<com.android.server.wallpaper.WallpaperData> getWallpaperMap() {
            return com.android.server.wallpaper.WallpaperManagerService.this.mWallpaperMap;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public android.util.SparseArray<com.android.server.wallpaper.WallpaperData> getLockWallpaperMap() {
            return com.android.server.wallpaper.WallpaperManagerService.this.mLockWallpaperMap;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void loadSettingsLocked(int userId, boolean keepDimensionHints, int which) throws android.os.RemoteException {
            com.android.server.wallpaper.WallpaperManagerService.this.loadSettingsLocked(userId, keepDimensionHints, which);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public com.android.server.wallpaper.WallpaperData getFallbackWallpaper() {
            return com.android.server.wallpaper.WallpaperManagerService.this.mFallbackWallpaper;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void notifyWallpaperColorsChangedOnDisplay(com.android.server.wallpaper.WallpaperData wallpaper, int which, int displayId) {
            com.android.server.wallpaper.WallpaperManagerService.this.notifyWallpaperColorsChangedOnDisplay(wallpaper, displayId);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public java.lang.String[] getPerUserFiles() {
            return new java.lang.String[]{"wallpaper_orig", com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_LIVE_WALLPAPER, "wallpaper_lock_orig", "wallpaper_lock", "wallpaper_info.xml"};
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void clearWallpaperComponentLocked(com.android.server.wallpaper.WallpaperData wallpaperData) {
            com.android.server.wallpaper.WallpaperManagerService.this.lambda$clearWallpaperLocked$9(wallpaperData);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void detachWallpaperLocked(com.android.server.wallpaper.WallpaperData wallpaper) {
            com.android.server.wallpaper.WallpaperManagerService.this.detachWallpaperLocked(wallpaper);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void updateFallbackConnection() {
            com.android.server.wallpaper.WallpaperManagerService.this.updateFallbackConnection();
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public android.os.RemoteCallbackList<android.app.IWallpaperManagerCallback> getWallpaperCallbacks(com.android.server.wallpaper.WallpaperData wallpaper) {
            return wallpaper.callbacks;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void setWallpaperComponent(android.content.ComponentName name, int userId, int which) {
            com.android.server.wallpaper.WallpaperManagerService.this.setWallpaperComponent(name, "", which, userId);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public android.content.ComponentName getDefaultWallpaperComponent() {
            return com.android.server.wallpaper.WallpaperManagerService.this.mDefaultWallpaperComponent;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public boolean hasPermission(java.lang.String permission) {
            return com.android.server.wallpaper.WallpaperManagerService.this.hasPermission(permission);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public com.android.server.wallpaper.WallpaperData findWallpaperAtDisplay(int userId, int displayId) {
            return com.android.server.wallpaper.WallpaperManagerService.this.findWallpaperAtDisplay(userId, displayId);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public boolean changingToSame(android.content.ComponentName componentName, com.android.server.wallpaper.WallpaperData wallpaper) {
            return com.android.server.wallpaper.WallpaperManagerService.this.changingToSame(componentName, wallpaper);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public android.util.SparseArray<com.android.server.wallpaper.WallpaperManagerService.DisplayConnector> getDisplayConnectors(com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection connection) {
            return connection.mDisplayConnector;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void initDisplayState(com.android.server.wallpaper.WallpaperManagerService.WallpaperConnection connection) {
            connection.initDisplayState();
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void removeCallback(com.android.server.wallpaper.WallpaperData wallpaperData) {
            com.android.server.FgThread.getHandler().removeCallbacks(wallpaperData.connection.mResetRunnable);
            com.android.server.wallpaper.WallpaperManagerService.this.mContext.getMainThreadHandler().removeCallbacks(wallpaperData.connection.mDisconnectRunnable);
            com.android.server.wallpaper.WallpaperManagerService.this.mContext.getMainThreadHandler().removeCallbacks(wallpaperData.connection.mTryToRebindRunnable);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public android.app.ActivityManager getActivityManager() {
            return com.android.server.wallpaper.WallpaperManagerService.this.mActivityManager;
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public android.hardware.display.DisplayManager getDisplayManager() {
            return (android.hardware.display.DisplayManager) com.android.server.wallpaper.WallpaperManagerService.this.mContext.getSystemService(android.hardware.display.DisplayManager.class);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void clearWallpaperLocked(int which, int userId, boolean fromForeground, android.os.IRemoteCallback reply) throws android.os.RemoteException {
            com.android.server.wallpaper.WallpaperManagerService.this.clearWallpaperLocked(which, userId, fromForeground, reply);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void scheduleTimeoutLocked(com.android.server.wallpaper.WallpaperData wallpaper) {
            if (wallpaper != null && wallpaper.connection != null) {
                wallpaper.connection.scheduleTimeoutLocked();
            }
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void clearWallpaperBitmaps(int userID, int wallpaperType) {
            com.android.server.wallpaper.WallpaperManagerService.this.clearWallpaperBitmaps(userID, wallpaperType);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void migrateStaticSystemToLockWallpaperLocked(int userId, int phyDisplayId) {
            com.android.server.wallpaper.WallpaperManagerService.this.migrateStaticSystemToLockWallpaperLocked(userId, phyDisplayId);
        }

        @Override // com.android.server.wallpaper.IWallpaperManagerServiceWrapper
        public void updateEngineFlags(com.android.server.wallpaper.WallpaperData wallpaperData) {
            com.android.server.wallpaper.WallpaperManagerService.this.updateEngineFlags(wallpaperData);
        }
    }
}
