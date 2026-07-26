package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public final class ExternalDisplayStatsService {
    private static final int AUDIO_SINK_CHANGED = 10;
    private static final int CONNECTED_STATE = 2;
    private static final int DISABLED_STATE = 3;
    private static final int DISCONNECTED_STATE = 1;
    private static final int ERROR_CABLE_NOT_CAPABLE_DISPLAYPORT = 13;
    private static final int ERROR_DISPLAYPORT_LINK_FAILED = 12;
    private static final int ERROR_HOTPLUG_CONNECTION = 11;
    private static final int EXTENDED_STATE = 6;
    private static final int INVALID_DISPLAYS_COUNT = -1;
    private static final int KEYGUARD = 4;
    private static final int MIRRORING_STATE = 5;
    private static final int PRESENTATION_ENDED = 9;
    private static final int PRESENTATION_WHILE_EXTENDED = 8;
    private static final int PRESENTATION_WHILE_MIRRORING = 7;
    private final android.media.AudioManager.AudioPlaybackCallback mAudioPlaybackCallback;
    private final android.util.SparseIntArray mExternalDisplayStates;
    private final com.android.server.display.ExternalDisplayStatsService.Injector mInjector;
    private int mInteractiveExternalDisplays;
    private final android.content.BroadcastReceiver mInteractivityReceiver;
    private boolean mIsExternalDisplayUsedForAudio;
    private boolean mIsInitialized;
    private static final java.lang.String TAG = "ExternalDisplayStatsService";
    private static final boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);

    /* JADX INFO: renamed from: com.android.server.display.ExternalDisplayStatsService$1, reason: invalid class name */
    class AnonymousClass1 extends android.media.AudioManager.AudioPlaybackCallback {
        private final java.lang.Runnable mLogStateAfterAudioSinkEnabled = new java.lang.Runnable() { // from class: com.android.server.display.ExternalDisplayStatsService$1$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$$0();
            }
        };
        private final java.lang.Runnable mLogStateAfterAudioSinkDisabled = new java.lang.Runnable() { // from class: com.android.server.display.ExternalDisplayStatsService$1$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$$1();
            }
        };

        AnonymousClass1() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$$0() {
            com.android.server.display.ExternalDisplayStatsService.this.logStateAfterAudioSinkChanged(true);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$$1() {
            com.android.server.display.ExternalDisplayStatsService.this.logStateAfterAudioSinkChanged(false);
        }

        @Override // android.media.AudioManager.AudioPlaybackCallback
        public void onPlaybackConfigChanged(java.util.List<android.media.AudioPlaybackConfiguration> configs) {
            super.onPlaybackConfigChanged(configs);
            scheduleAudioSinkChange(isExternalDisplayUsedForAudio(configs));
        }

        private boolean isExternalDisplayUsedForAudio(java.util.List<android.media.AudioPlaybackConfiguration> configs) {
            for (android.media.AudioPlaybackConfiguration config : configs) {
                android.media.AudioDeviceInfo info = config.getAudioDeviceInfo();
                if (config.isActive() && info != null && info.isSink() && (info.getType() == 9 || info.getType() == 10 || info.getType() == 11)) {
                    if (com.android.server.display.ExternalDisplayStatsService.DEBUG) {
                        android.util.Slog.d(com.android.server.display.ExternalDisplayStatsService.TAG, "isExternalDisplayUsedForAudio: use " + ((java.lang.Object) info.getProductName()) + " isActive=" + config.isActive() + " isSink=" + info.isSink() + " type=" + info.getType());
                        return true;
                    }
                    return true;
                }
                if (com.android.server.display.ExternalDisplayStatsService.DEBUG && info != null) {
                    android.util.Slog.d(com.android.server.display.ExternalDisplayStatsService.TAG, "isExternalDisplayUsedForAudio: drop " + ((java.lang.Object) info.getProductName()) + " isActive=" + config.isActive() + " isSink=" + info.isSink() + " type=" + info.getType());
                }
            }
            return false;
        }

        private void scheduleAudioSinkChange(boolean isAudioOnExternalDisplay) {
            if (com.android.server.display.ExternalDisplayStatsService.DEBUG) {
                android.util.Slog.d(com.android.server.display.ExternalDisplayStatsService.TAG, "scheduleAudioSinkChange: mIsExternalDisplayUsedForAudio=" + com.android.server.display.ExternalDisplayStatsService.this.mIsExternalDisplayUsedForAudio + " isAudioOnExternalDisplay=" + isAudioOnExternalDisplay);
            }
            com.android.server.display.ExternalDisplayStatsService.this.mInjector.getHandler().removeCallbacks(this.mLogStateAfterAudioSinkEnabled);
            com.android.server.display.ExternalDisplayStatsService.this.mInjector.getHandler().removeCallbacks(this.mLogStateAfterAudioSinkDisabled);
            java.lang.Runnable callback = isAudioOnExternalDisplay ? this.mLogStateAfterAudioSinkEnabled : this.mLogStateAfterAudioSinkDisabled;
            if (isAudioOnExternalDisplay) {
                com.android.server.display.ExternalDisplayStatsService.this.mInjector.getHandler().postDelayed(callback, 10000L);
            } else {
                com.android.server.display.ExternalDisplayStatsService.this.mInjector.getHandler().post(callback);
            }
        }
    }

    ExternalDisplayStatsService(android.content.Context context, android.os.Handler handler) {
        this(new com.android.server.display.ExternalDisplayStatsService.Injector(context, handler));
    }

    ExternalDisplayStatsService(com.android.server.display.ExternalDisplayStatsService.Injector injector) {
        this.mExternalDisplayStates = new android.util.SparseIntArray();
        this.mAudioPlaybackCallback = new com.android.server.display.ExternalDisplayStatsService.AnonymousClass1();
        this.mInteractivityReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.display.ExternalDisplayStatsService.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                int interactiveDisplaysCount = 0;
                synchronized (com.android.server.display.ExternalDisplayStatsService.this.mExternalDisplayStates) {
                    if (com.android.server.display.ExternalDisplayStatsService.this.mExternalDisplayStates.size() == 0) {
                        return;
                    }
                    for (int i = 0; i < com.android.server.display.ExternalDisplayStatsService.this.mExternalDisplayStates.size(); i++) {
                        if (com.android.server.display.ExternalDisplayStatsService.this.mInjector.isInteractive(com.android.server.display.ExternalDisplayStatsService.this.mExternalDisplayStates.keyAt(i))) {
                            interactiveDisplaysCount++;
                        }
                    }
                    if (com.android.server.display.ExternalDisplayStatsService.this.mInteractiveExternalDisplays == interactiveDisplaysCount) {
                        return;
                    }
                    if (interactiveDisplaysCount == 0) {
                        com.android.server.display.ExternalDisplayStatsService.this.logExternalDisplayIdleStarted();
                    } else if (-1 != com.android.server.display.ExternalDisplayStatsService.this.mInteractiveExternalDisplays) {
                        com.android.server.display.ExternalDisplayStatsService.this.logExternalDisplayIdleEnded();
                    }
                    com.android.server.display.ExternalDisplayStatsService.this.mInteractiveExternalDisplays = interactiveDisplaysCount;
                }
            }
        };
        this.mInjector = injector;
    }

    public void onHotplugConnectionError() {
        logExternalDisplayError(11);
    }

    public void onDisplayPortLinkTrainingFailure() {
        logExternalDisplayError(12);
    }

    public void onCableNotCapableDisplayPort() {
        logExternalDisplayError(13);
    }

    void onDisplayConnected(com.android.server.display.LogicalDisplay display) {
        android.view.DisplayInfo displayInfo = display.getDisplayInfoLocked();
        if (displayInfo == null || displayInfo.type != 2) {
            return;
        }
        logStateConnected(display.getDisplayIdLocked());
    }

    void onDisplayAdded(int displayId) {
        if (this.mInjector.isExtendedDisplayEnabled()) {
            logStateExtended(displayId);
        } else {
            logStateMirroring(displayId);
        }
    }

    void onDisplayDisabled(int displayId) {
        logStateDisabled(displayId);
    }

    void onDisplayDisconnected(int displayId) {
        logStateDisconnected(displayId);
    }

    void onPresentationWindowAdded(int displayId) {
        logExternalDisplayPresentationStarted(displayId);
    }

    void onPresentationWindowRemoved(int displayId) {
        logExternalDisplayPresentationEnded(displayId);
    }

    boolean isInteractiveExternalDisplays() {
        return this.mInteractiveExternalDisplays != 0;
    }

    boolean isExternalDisplayUsedForAudio() {
        return this.mIsExternalDisplayUsedForAudio;
    }

    private void logExternalDisplayError(int errorType) {
        int countOfExternalDisplays;
        synchronized (this.mExternalDisplayStates) {
            countOfExternalDisplays = this.mExternalDisplayStates.size();
        }
        this.mInjector.writeLog(com.android.internal.util.FrameworkStatsLog.EXTERNAL_DISPLAY_STATE_CHANGED, errorType, countOfExternalDisplays, this.mIsExternalDisplayUsedForAudio);
        if (DEBUG) {
            android.util.Slog.d(TAG, "logExternalDisplayError countOfExternalDisplays=" + countOfExternalDisplays + " errorType=" + errorType + " mIsExternalDisplayUsedForAudio=" + this.mIsExternalDisplayUsedForAudio);
        }
    }

    private void scheduleInit() {
        this.mInjector.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.display.ExternalDisplayStatsService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleInit$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleInit$0() {
        if (this.mIsInitialized) {
            android.util.Slog.e(TAG, "scheduleInit is called but already initialized");
            return;
        }
        this.mIsInitialized = true;
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        filter.addAction("android.intent.action.SCREEN_ON");
        this.mInteractiveExternalDisplays = -1;
        this.mIsExternalDisplayUsedForAudio = false;
        this.mInjector.registerInteractivityReceiver(this.mInteractivityReceiver, filter);
        this.mInjector.registerAudioPlaybackCallback(this.mAudioPlaybackCallback);
    }

    private void scheduleDeinit() {
        this.mInjector.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.display.ExternalDisplayStatsService$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleDeinit$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleDeinit$1() {
        if (!this.mIsInitialized) {
            android.util.Slog.e(TAG, "scheduleDeinit is called but never initialized");
            return;
        }
        this.mIsInitialized = false;
        this.mInjector.unregisterInteractivityReceiver(this.mInteractivityReceiver);
        this.mInjector.unregisterAudioPlaybackCallback(this.mAudioPlaybackCallback);
    }

    private void logStateConnected(int displayId) {
        synchronized (this.mExternalDisplayStates) {
            int state = this.mExternalDisplayStates.get(displayId, 1);
            if (state != 1) {
                return;
            }
            this.mExternalDisplayStates.put(displayId, 2);
            int countOfExternalDisplays = this.mExternalDisplayStates.size();
            if (countOfExternalDisplays == 1) {
                scheduleInit();
            }
            this.mInjector.writeLog(com.android.internal.util.FrameworkStatsLog.EXTERNAL_DISPLAY_STATE_CHANGED, 2, countOfExternalDisplays, this.mIsExternalDisplayUsedForAudio);
            if (DEBUG) {
                android.util.Slog.d(TAG, "logStateConnected displayId=" + displayId + " countOfExternalDisplays=" + countOfExternalDisplays + " currentState=" + state + " state=2 mIsExternalDisplayUsedForAudio=" + this.mIsExternalDisplayUsedForAudio);
            }
        }
    }

    private void logStateDisconnected(int displayId) {
        synchronized (this.mExternalDisplayStates) {
            int state = this.mExternalDisplayStates.get(displayId, 1);
            if (state == 1) {
                if (DEBUG) {
                    android.util.Slog.d(TAG, "logStateDisconnected displayId=" + displayId + " already disconnected");
                }
                return;
            }
            int countOfExternalDisplays = this.mExternalDisplayStates.size();
            this.mExternalDisplayStates.delete(displayId);
            this.mInjector.writeLog(com.android.internal.util.FrameworkStatsLog.EXTERNAL_DISPLAY_STATE_CHANGED, 1, countOfExternalDisplays, this.mIsExternalDisplayUsedForAudio);
            if (DEBUG) {
                android.util.Slog.d(TAG, "logStateDisconnected displayId=" + displayId + " countOfExternalDisplays=" + countOfExternalDisplays + " currentState=" + state + " state=1 mIsExternalDisplayUsedForAudio=" + this.mIsExternalDisplayUsedForAudio);
            }
            if (countOfExternalDisplays == 1) {
                scheduleDeinit();
            }
        }
    }

    private void logStateMirroring(int displayId) {
        synchronized (this.mExternalDisplayStates) {
            int state = this.mExternalDisplayStates.get(displayId, 1);
            if (state != 1 && state != 5) {
                for (int i = 0; i < this.mExternalDisplayStates.size(); i++) {
                    if (this.mExternalDisplayStates.keyAt(i) == displayId) {
                        this.mExternalDisplayStates.put(displayId, 5);
                        this.mInjector.writeLog(com.android.internal.util.FrameworkStatsLog.EXTERNAL_DISPLAY_STATE_CHANGED, 5, i + 1, this.mIsExternalDisplayUsedForAudio);
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "logStateMirroring displayId=" + displayId + " countOfExternalDisplays=" + (i + 1) + " currentState=" + state + " state=5 mIsExternalDisplayUsedForAudio=" + this.mIsExternalDisplayUsedForAudio);
                        }
                    }
                }
            }
        }
    }

    private void logStateExtended(int displayId) {
        synchronized (this.mExternalDisplayStates) {
            int state = this.mExternalDisplayStates.get(displayId, 1);
            if (state != 1 && state != 6) {
                for (int i = 0; i < this.mExternalDisplayStates.size(); i++) {
                    if (this.mExternalDisplayStates.keyAt(i) == displayId) {
                        this.mExternalDisplayStates.put(displayId, 6);
                        this.mInjector.writeLog(com.android.internal.util.FrameworkStatsLog.EXTERNAL_DISPLAY_STATE_CHANGED, 6, i + 1, this.mIsExternalDisplayUsedForAudio);
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "logStateExtended displayId=" + displayId + " countOfExternalDisplays=" + (i + 1) + " currentState=" + state + " state=6 mIsExternalDisplayUsedForAudio=" + this.mIsExternalDisplayUsedForAudio);
                        }
                    }
                }
            }
        }
    }

    private void logStateDisabled(int displayId) {
        synchronized (this.mExternalDisplayStates) {
            int state = this.mExternalDisplayStates.get(displayId, 1);
            if (state != 1 && state != 3) {
                for (int i = 0; i < this.mExternalDisplayStates.size(); i++) {
                    if (this.mExternalDisplayStates.keyAt(i) == displayId) {
                        this.mExternalDisplayStates.put(displayId, 3);
                        this.mInjector.writeLog(com.android.internal.util.FrameworkStatsLog.EXTERNAL_DISPLAY_STATE_CHANGED, 3, i + 1, this.mIsExternalDisplayUsedForAudio);
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "logStateDisabled displayId=" + displayId + " countOfExternalDisplays=" + (i + 1) + " currentState=" + state + " state=3 mIsExternalDisplayUsedForAudio=" + this.mIsExternalDisplayUsedForAudio);
                        }
                    }
                }
            }
        }
    }

    private void logExternalDisplayPresentationStarted(int displayId) {
        synchronized (this.mExternalDisplayStates) {
            int state = this.mExternalDisplayStates.get(displayId, 1);
            if (state == 1) {
                return;
            }
            int countOfExternalDisplays = this.mExternalDisplayStates.size();
            int newState = this.mInjector.isExtendedDisplayEnabled() ? 8 : 7;
            this.mInjector.writeLog(com.android.internal.util.FrameworkStatsLog.EXTERNAL_DISPLAY_STATE_CHANGED, newState, countOfExternalDisplays, this.mIsExternalDisplayUsedForAudio);
            if (DEBUG) {
                android.util.Slog.d(TAG, "logExternalDisplayPresentationStarted state=" + state + " newState=" + newState + " mIsExternalDisplayUsedForAudio=" + this.mIsExternalDisplayUsedForAudio);
            }
        }
    }

    private void logExternalDisplayPresentationEnded(int displayId) {
        synchronized (this.mExternalDisplayStates) {
            int state = this.mExternalDisplayStates.get(displayId, 1);
            if (state == 1) {
                return;
            }
            int countOfExternalDisplays = this.mExternalDisplayStates.size();
            this.mInjector.writeLog(com.android.internal.util.FrameworkStatsLog.EXTERNAL_DISPLAY_STATE_CHANGED, 9, countOfExternalDisplays, this.mIsExternalDisplayUsedForAudio);
            if (DEBUG) {
                android.util.Slog.d(TAG, "logExternalDisplayPresentationEnded state=" + state + " countOfExternalDisplays=" + countOfExternalDisplays + " mIsExternalDisplayUsedForAudio=" + this.mIsExternalDisplayUsedForAudio);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logExternalDisplayIdleStarted() {
        synchronized (this.mExternalDisplayStates) {
            for (int i = 0; i < this.mExternalDisplayStates.size(); i++) {
                this.mInjector.writeLog(com.android.internal.util.FrameworkStatsLog.EXTERNAL_DISPLAY_STATE_CHANGED, 4, i + 1, this.mIsExternalDisplayUsedForAudio);
                if (DEBUG) {
                    int displayId = this.mExternalDisplayStates.keyAt(i);
                    int state = this.mExternalDisplayStates.get(displayId, 1);
                    android.util.Slog.d(TAG, "logExternalDisplayIdleStarted displayId=" + displayId + " currentState=" + state + " countOfExternalDisplays=" + (i + 1) + " state=4 mIsExternalDisplayUsedForAudio=" + this.mIsExternalDisplayUsedForAudio);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logExternalDisplayIdleEnded() {
        synchronized (this.mExternalDisplayStates) {
            for (int i = 0; i < this.mExternalDisplayStates.size(); i++) {
                int displayId = this.mExternalDisplayStates.keyAt(i);
                int state = this.mExternalDisplayStates.get(displayId, 1);
                if (state == 1) {
                    return;
                }
                this.mInjector.writeLog(com.android.internal.util.FrameworkStatsLog.EXTERNAL_DISPLAY_STATE_CHANGED, state, i + 1, this.mIsExternalDisplayUsedForAudio);
                if (DEBUG) {
                    android.util.Slog.d(TAG, "logExternalDisplayIdleEnded displayId=" + displayId + " state=" + state + " countOfExternalDisplays=" + (i + 1) + " mIsExternalDisplayUsedForAudio=" + this.mIsExternalDisplayUsedForAudio);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logStateAfterAudioSinkChanged(boolean enabled) {
        int countOfExternalDisplays;
        if (this.mIsExternalDisplayUsedForAudio == enabled) {
            return;
        }
        this.mIsExternalDisplayUsedForAudio = enabled;
        synchronized (this.mExternalDisplayStates) {
            countOfExternalDisplays = this.mExternalDisplayStates.size();
        }
        this.mInjector.writeLog(com.android.internal.util.FrameworkStatsLog.EXTERNAL_DISPLAY_STATE_CHANGED, 10, countOfExternalDisplays, this.mIsExternalDisplayUsedForAudio);
        if (DEBUG) {
            android.util.Slog.d(TAG, "logStateAfterAudioSinkChanged countOfExternalDisplays)=" + countOfExternalDisplays + " mIsExternalDisplayUsedForAudio=" + this.mIsExternalDisplayUsedForAudio);
        }
    }

    static class Injector {
        private android.media.AudioManager mAudioManager;
        private final android.content.Context mContext;
        private final android.os.Handler mHandler;
        private android.os.PowerManager mPowerManager;

        Injector(android.content.Context context, android.os.Handler handler) {
            this.mContext = context;
            this.mHandler = handler;
        }

        boolean isExtendedDisplayEnabled() {
            try {
                return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "force_desktop_mode_on_external_displays", 0) != 0;
            } catch (java.lang.Throwable th) {
                return false;
            }
        }

        void registerInteractivityReceiver(android.content.BroadcastReceiver interactivityReceiver, android.content.IntentFilter filter) {
            this.mContext.registerReceiver(interactivityReceiver, filter, null, this.mHandler, 4);
        }

        void unregisterInteractivityReceiver(android.content.BroadcastReceiver interactivityReceiver) {
            this.mContext.unregisterReceiver(interactivityReceiver);
        }

        void registerAudioPlaybackCallback(android.media.AudioManager.AudioPlaybackCallback audioPlaybackCallback) {
            if (this.mAudioManager == null) {
                this.mAudioManager = (android.media.AudioManager) this.mContext.getSystemService(android.media.AudioManager.class);
            }
            if (this.mAudioManager != null) {
                this.mAudioManager.registerAudioPlaybackCallback(audioPlaybackCallback, this.mHandler);
            }
        }

        void unregisterAudioPlaybackCallback(android.media.AudioManager.AudioPlaybackCallback audioPlaybackCallback) {
            if (this.mAudioManager == null) {
                this.mAudioManager = (android.media.AudioManager) this.mContext.getSystemService(android.media.AudioManager.class);
            }
            if (this.mAudioManager != null) {
                this.mAudioManager.unregisterAudioPlaybackCallback(audioPlaybackCallback);
            }
        }

        boolean isInteractive(int displayId) {
            if (this.mPowerManager == null) {
                this.mPowerManager = (android.os.PowerManager) this.mContext.getSystemService(android.os.PowerManager.class);
            }
            return this.mPowerManager == null || this.mPowerManager.isInteractive(displayId);
        }

        android.os.Handler getHandler() {
            return this.mHandler;
        }

        void writeLog(int externalDisplayStateChanged, int event, int numberOfDisplays, boolean isExternalDisplayUsedForAudio) {
            com.android.internal.util.FrameworkStatsLog.write(externalDisplayStateChanged, event, numberOfDisplays, isExternalDisplayUsedForAudio);
        }
    }
}
