package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
class ExternalDisplayPolicy {
    static final java.lang.String ENABLE_ON_CONNECT = "persist.sys.display.enable_on_connect.external";
    private final com.android.server.display.notifications.DisplayNotificationManager mDisplayNotificationManager;
    private final com.android.server.display.ExternalDisplayStatsService mExternalDisplayStatsService;
    private final com.android.server.display.feature.DisplayManagerFlags mFlags;
    private final android.os.Handler mHandler;
    private final com.android.server.display.ExternalDisplayPolicy.Injector mInjector;
    private boolean mIsBootCompleted;
    private final com.android.server.display.LogicalDisplayMapper mLogicalDisplayMapper;
    private final com.android.server.display.DisplayManagerService.SyncRoot mSyncRoot;
    private static final java.lang.String TAG = "ExternalDisplayPolicy";
    private static final boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);
    private volatile int mStatus = 0;
    private final java.util.Set<java.lang.Integer> mDisplayIdsWaitingForBootCompletion = new java.util.HashSet();

    interface Injector {
        com.android.server.display.notifications.DisplayNotificationManager getDisplayNotificationManager();

        com.android.server.display.ExternalDisplayStatsService getExternalDisplayStatsService();

        com.android.server.display.feature.DisplayManagerFlags getFlags();

        android.os.Handler getHandler();

        com.android.server.display.LogicalDisplayMapper getLogicalDisplayMapper();

        com.android.server.display.DisplayManagerService.SyncRoot getSyncRoot();

        android.os.IThermalService getThermalService();

        void sendExternalDisplayEventLocked(com.android.server.display.LogicalDisplay logicalDisplay, int i);
    }

    static boolean isExternalDisplayLocked(com.android.server.display.LogicalDisplay logicalDisplay) {
        return logicalDisplay.getDisplayInfoLocked().type == 2;
    }

    ExternalDisplayPolicy(com.android.server.display.ExternalDisplayPolicy.Injector injector) {
        this.mInjector = injector;
        this.mLogicalDisplayMapper = this.mInjector.getLogicalDisplayMapper();
        this.mSyncRoot = this.mInjector.getSyncRoot();
        this.mFlags = this.mInjector.getFlags();
        this.mDisplayNotificationManager = this.mInjector.getDisplayNotificationManager();
        this.mHandler = this.mInjector.getHandler();
        this.mExternalDisplayStatsService = this.mInjector.getExternalDisplayStatsService();
    }

    void onBootCompleted() {
        synchronized (this.mSyncRoot) {
            this.mIsBootCompleted = true;
            for (java.lang.Integer displayId : this.mDisplayIdsWaitingForBootCompletion) {
                com.android.server.display.LogicalDisplay logicalDisplay = this.mLogicalDisplayMapper.getDisplayLocked(displayId.intValue());
                if (logicalDisplay != null) {
                    handleExternalDisplayConnectedLocked(logicalDisplay);
                }
            }
            if (!this.mDisplayIdsWaitingForBootCompletion.isEmpty()) {
                this.mLogicalDisplayMapper.updateLogicalDisplaysLocked();
            }
            this.mDisplayIdsWaitingForBootCompletion.clear();
        }
        if (!this.mFlags.isConnectedDisplayManagementEnabled()) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "External display management is not enabled on your device: cannot register thermal listener.");
            }
        } else if (!this.mFlags.isConnectedDisplayErrorHandlingEnabled()) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "ConnectedDisplayErrorHandlingEnabled is not enabled on your device: cannot register thermal listener.");
            }
        } else if (!registerThermalServiceListener(new com.android.server.display.ExternalDisplayPolicy.SkinThermalStatusObserver())) {
            android.util.Slog.e(TAG, "Failed to register thermal listener");
        }
    }

    void setExternalDisplayEnabledLocked(com.android.server.display.LogicalDisplay logicalDisplay, boolean enabled) {
        if (!isExternalDisplayLocked(logicalDisplay)) {
            android.util.Slog.e(TAG, "setExternalDisplayEnabledLocked called for non external display");
            return;
        }
        if (!this.mFlags.isConnectedDisplayManagementEnabled()) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "setExternalDisplayEnabledLocked: External display management is not enabled on your device, cannot enable/disable display.");
            }
        } else {
            if (enabled && !isExternalDisplayAllowed()) {
                android.util.Slog.w(TAG, "setExternalDisplayEnabledLocked: External display can not be enabled because it is currently not allowed.");
                android.os.Handler handler = this.mHandler;
                final com.android.server.display.notifications.DisplayNotificationManager displayNotificationManager = this.mDisplayNotificationManager;
                java.util.Objects.requireNonNull(displayNotificationManager);
                handler.post(new java.lang.Runnable() { // from class: com.android.server.display.ExternalDisplayPolicy$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        displayNotificationManager.onHighTemperatureExternalDisplayNotAllowed();
                    }
                });
                return;
            }
            this.mLogicalDisplayMapper.setDisplayEnabledLocked(logicalDisplay, enabled);
        }
    }

    void handleExternalDisplayConnectedLocked(com.android.server.display.LogicalDisplay logicalDisplay) {
        if (!isExternalDisplayLocked(logicalDisplay)) {
            android.util.Slog.e(TAG, "handleExternalDisplayConnectedLocked called for non-external display");
            return;
        }
        if (!this.mFlags.isConnectedDisplayManagementEnabled()) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "handleExternalDisplayConnectedLocked connected display management flag is off");
                return;
            }
            return;
        }
        if (!this.mIsBootCompleted) {
            this.mDisplayIdsWaitingForBootCompletion.add(java.lang.Integer.valueOf(logicalDisplay.getDisplayIdLocked()));
            return;
        }
        this.mExternalDisplayStatsService.onDisplayConnected(logicalDisplay);
        if ((android.os.Build.IS_ENG || android.os.Build.IS_USERDEBUG) && android.os.SystemProperties.getBoolean(ENABLE_ON_CONNECT, false)) {
            android.util.Slog.w(TAG, "External display is enabled by default, bypassing user consent.");
            this.mInjector.sendExternalDisplayEventLocked(logicalDisplay, 6);
            return;
        }
        this.mLogicalDisplayMapper.setEnabledLocked(logicalDisplay, false);
        if (!isExternalDisplayAllowed()) {
            android.util.Slog.w(TAG, "handleExternalDisplayConnectedLocked: External display can not be used because it is currently not allowed.");
            this.mDisplayNotificationManager.onHighTemperatureExternalDisplayNotAllowed();
        } else {
            this.mInjector.sendExternalDisplayEventLocked(logicalDisplay, 6);
            if (DEBUG) {
                android.util.Slog.d(TAG, "handleExternalDisplayConnectedLocked complete displayId=" + logicalDisplay.getDisplayIdLocked());
            }
        }
    }

    void handleLogicalDisplayDisconnectedLocked(com.android.server.display.LogicalDisplay logicalDisplay) {
        if (!this.mFlags.isConnectedDisplayManagementEnabled()) {
            return;
        }
        int displayId = logicalDisplay.getDisplayIdLocked();
        if (this.mDisplayIdsWaitingForBootCompletion.remove(java.lang.Integer.valueOf(displayId))) {
            return;
        }
        this.mExternalDisplayStatsService.onDisplayDisconnected(displayId);
    }

    void handleLogicalDisplayAddedLocked(com.android.server.display.LogicalDisplay logicalDisplay) {
        if (!isExternalDisplayLocked(logicalDisplay) || !this.mFlags.isConnectedDisplayManagementEnabled()) {
            return;
        }
        this.mExternalDisplayStatsService.onDisplayAdded(logicalDisplay.getDisplayIdLocked());
    }

    void onPresentation(int displayId, boolean isShown) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.LogicalDisplay logicalDisplay = this.mLogicalDisplayMapper.getDisplayLocked(displayId);
            if (logicalDisplay != null && isExternalDisplayLocked(logicalDisplay)) {
                if (!this.mFlags.isConnectedDisplayManagementEnabled()) {
                    return;
                }
                if (isShown) {
                    this.mExternalDisplayStatsService.onPresentationWindowAdded(displayId);
                } else {
                    this.mExternalDisplayStatsService.onPresentationWindowRemoved(displayId);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableExternalDisplayLocked(com.android.server.display.LogicalDisplay logicalDisplay) {
        if (!isExternalDisplayLocked(logicalDisplay)) {
            return;
        }
        if (!this.mFlags.isConnectedDisplayManagementEnabled()) {
            android.util.Slog.e(TAG, "disableExternalDisplayLocked shouldn't be called when the connected display management flag is off");
            return;
        }
        if (!this.mFlags.isConnectedDisplayErrorHandlingEnabled()) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "disableExternalDisplayLocked shouldn't be called when the error handling flag is off");
            }
        } else {
            if (!logicalDisplay.isEnabledLocked()) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "disableExternalDisplayLocked is not allowed: displayId=" + logicalDisplay.getDisplayIdLocked() + " isEnabledLocked=false");
                    return;
                }
                return;
            }
            if (!isExternalDisplayAllowed()) {
                android.util.Slog.w(TAG, "External display is currently not allowed and is getting disabled.");
                this.mDisplayNotificationManager.onHighTemperatureExternalDisplayNotAllowed();
            }
            this.mLogicalDisplayMapper.setDisplayEnabledLocked(logicalDisplay, false);
            this.mExternalDisplayStatsService.onDisplayDisabled(logicalDisplay.getDisplayIdLocked());
            if (DEBUG) {
                android.util.Slog.d(TAG, "disableExternalDisplayLocked complete displayId=" + logicalDisplay.getDisplayIdLocked());
            }
        }
    }

    boolean isExternalDisplayAllowed() {
        return this.mStatus < 4;
    }

    private boolean registerThermalServiceListener(android.os.IThermalEventListener.Stub listener) {
        android.os.IThermalService thermalService = this.mInjector.getThermalService();
        if (thermalService == null) {
            android.util.Slog.w(TAG, "Could not observe thermal status. Service not available");
            return false;
        }
        try {
            thermalService.registerThermalEventListenerWithType(listener, 3);
            if (DEBUG) {
                android.util.Slog.d(TAG, "registerThermalServiceListener complete.");
                return true;
            }
            return true;
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Failed to register thermal status listener", e);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void disableExternalDisplays() {
        synchronized (this.mSyncRoot) {
            this.mLogicalDisplayMapper.forEachLocked(new java.util.function.Consumer() { // from class: com.android.server.display.ExternalDisplayPolicy$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.disableExternalDisplayLocked((com.android.server.display.LogicalDisplay) obj);
                }
            });
        }
    }

    private final class SkinThermalStatusObserver extends android.os.IThermalEventListener.Stub {
        private SkinThermalStatusObserver() {
        }

        public void notifyThrottling(android.os.Temperature temp) {
            int newStatus = temp.getStatus();
            int previousStatus = com.android.server.display.ExternalDisplayPolicy.this.mStatus;
            com.android.server.display.ExternalDisplayPolicy.this.mStatus = newStatus;
            if (4 > previousStatus && 4 <= newStatus) {
                com.android.server.display.ExternalDisplayPolicy.this.disableExternalDisplays();
            }
        }
    }
}
