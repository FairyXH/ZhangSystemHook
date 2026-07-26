package com.android.server.companion.virtual.audio;

/* JADX INFO: loaded from: classes.dex */
public final class VirtualAudioController implements com.android.server.companion.virtual.audio.AudioPlaybackDetector.AudioPlaybackCallback, com.android.server.companion.virtual.audio.AudioRecordingDetector.AudioRecordingCallback, com.android.server.companion.virtual.GenericWindowPolicyController.RunningAppsChangedListener {
    private static final java.lang.String TAG = "VirtualAudioController";
    private static final int UPDATE_REROUTING_APPS_DELAY_MS = 2000;
    private final com.android.server.companion.virtual.audio.AudioPlaybackDetector mAudioPlaybackDetector;
    private final com.android.server.companion.virtual.audio.AudioRecordingDetector mAudioRecordingDetector;
    private android.companion.virtual.audio.IAudioConfigChangedCallback mConfigChangedCallback;
    private final android.content.Context mContext;
    private com.android.server.companion.virtual.GenericWindowPolicyController mGenericWindowPolicyController;
    private android.companion.virtual.audio.IAudioRoutingCallback mRoutingCallback;
    private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final java.lang.Runnable mUpdateAudioRoutingRunnable = new java.lang.Runnable() { // from class: com.android.server.companion.virtual.audio.VirtualAudioController$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.notifyAppsNeedingAudioRoutingChanged();
        }
    };
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.ArraySet<java.lang.Integer> mRunningAppUids = new android.util.ArraySet<>();
    private android.util.ArraySet<java.lang.Integer> mPlayingAppUids = new android.util.ArraySet<>();
    private final java.lang.Object mCallbackLock = new java.lang.Object();

    public VirtualAudioController(android.content.Context context, android.content.AttributionSource attributionSource) {
        this.mContext = context;
        this.mAudioPlaybackDetector = new com.android.server.companion.virtual.audio.AudioPlaybackDetector(context);
        this.mAudioRecordingDetector = new com.android.server.companion.virtual.audio.AudioRecordingDetector(context);
        if (android.companion.virtualdevice.flags.Flags.metricsCollection()) {
            com.android.modules.expresslog.Counter.logIncrementWithUid("virtual_devices.value_virtual_audio_created_count", attributionSource.getUid());
        }
    }

    public void startListening(com.android.server.companion.virtual.GenericWindowPolicyController genericWindowPolicyController, android.companion.virtual.audio.IAudioRoutingCallback routingCallback, android.companion.virtual.audio.IAudioConfigChangedCallback configChangedCallback) {
        this.mGenericWindowPolicyController = genericWindowPolicyController;
        this.mGenericWindowPolicyController.registerRunningAppsChangedListener(this);
        synchronized (this.mCallbackLock) {
            this.mRoutingCallback = routingCallback;
            this.mConfigChangedCallback = configChangedCallback;
        }
        synchronized (this.mLock) {
            this.mRunningAppUids.clear();
            this.mPlayingAppUids.clear();
        }
        if (configChangedCallback != null) {
            this.mAudioPlaybackDetector.register(this);
            this.mAudioRecordingDetector.register(this);
        }
    }

    public void stopListening() {
        if (this.mHandler.hasCallbacks(this.mUpdateAudioRoutingRunnable)) {
            this.mHandler.removeCallbacks(this.mUpdateAudioRoutingRunnable);
        }
        this.mAudioPlaybackDetector.unregister();
        this.mAudioRecordingDetector.unregister();
        if (this.mGenericWindowPolicyController != null) {
            this.mGenericWindowPolicyController.unregisterRunningAppsChangedListener(this);
            this.mGenericWindowPolicyController = null;
        }
        synchronized (this.mCallbackLock) {
            this.mRoutingCallback = null;
            this.mConfigChangedCallback = null;
        }
    }

    @Override // com.android.server.companion.virtual.GenericWindowPolicyController.RunningAppsChangedListener
    public void onRunningAppsChanged(android.util.ArraySet<java.lang.Integer> runningUids) {
        synchronized (this.mLock) {
            if (this.mRunningAppUids.equals(runningUids)) {
                return;
            }
            this.mRunningAppUids.clear();
            this.mRunningAppUids.addAll((android.util.ArraySet<? extends java.lang.Integer>) runningUids);
            android.util.ArraySet<java.lang.Integer> oldPlayingAppUids = this.mPlayingAppUids;
            android.media.AudioManager audioManager = (android.media.AudioManager) this.mContext.getSystemService(android.media.AudioManager.class);
            java.util.List<android.media.AudioPlaybackConfiguration> configs = audioManager.getActivePlaybackConfigurations();
            this.mPlayingAppUids = findPlayingAppUids(configs, this.mRunningAppUids);
            if (!this.mPlayingAppUids.isEmpty()) {
                android.util.Slog.i(TAG, "Audio is playing, do not change rerouted apps");
                return;
            }
            if (!oldPlayingAppUids.isEmpty()) {
                android.util.Slog.i(TAG, "The last playing app removed, delay change rerouted apps");
                if (this.mHandler.hasCallbacks(this.mUpdateAudioRoutingRunnable)) {
                    this.mHandler.removeCallbacks(this.mUpdateAudioRoutingRunnable);
                }
                this.mHandler.postDelayed(this.mUpdateAudioRoutingRunnable, 2000L);
                return;
            }
            notifyAppsNeedingAudioRoutingChanged();
        }
    }

    @Override // com.android.server.companion.virtual.audio.AudioPlaybackDetector.AudioPlaybackCallback
    public void onPlaybackConfigChanged(java.util.List<android.media.AudioPlaybackConfiguration> configs) {
        java.util.List<android.media.AudioPlaybackConfiguration> audioPlaybackConfigurations;
        updatePlayingApplications(configs);
        synchronized (this.mLock) {
            audioPlaybackConfigurations = findPlaybackConfigurations(configs, this.mRunningAppUids);
        }
        synchronized (this.mCallbackLock) {
            if (this.mConfigChangedCallback != null) {
                try {
                    this.mConfigChangedCallback.onPlaybackConfigChanged(audioPlaybackConfigurations);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "RemoteException when calling onPlaybackConfigChanged", e);
                }
            }
        }
    }

    @Override // com.android.server.companion.virtual.audio.AudioRecordingDetector.AudioRecordingCallback
    public void onRecordingConfigChanged(java.util.List<android.media.AudioRecordingConfiguration> configs) {
        java.util.List<android.media.AudioRecordingConfiguration> audioRecordingConfigurations;
        synchronized (this.mLock) {
            audioRecordingConfigurations = findRecordingConfigurations(configs, this.mRunningAppUids);
        }
        synchronized (this.mCallbackLock) {
            if (this.mConfigChangedCallback != null) {
                try {
                    this.mConfigChangedCallback.onRecordingConfigChanged(audioRecordingConfigurations);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "RemoteException when calling onRecordingConfigChanged", e);
                }
            }
        }
    }

    private void updatePlayingApplications(java.util.List<android.media.AudioPlaybackConfiguration> configs) {
        synchronized (this.mLock) {
            android.util.ArraySet<java.lang.Integer> playingAppUids = findPlayingAppUids(configs, this.mRunningAppUids);
            if (this.mPlayingAppUids.equals(playingAppUids)) {
                return;
            }
            this.mPlayingAppUids = playingAppUids;
            notifyAppsNeedingAudioRoutingChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyAppsNeedingAudioRoutingChanged() {
        int[] runningUids;
        if (this.mHandler.hasCallbacks(this.mUpdateAudioRoutingRunnable)) {
            this.mHandler.removeCallbacks(this.mUpdateAudioRoutingRunnable);
        }
        synchronized (this.mLock) {
            runningUids = new int[this.mRunningAppUids.size()];
            for (int i = 0; i < this.mRunningAppUids.size(); i++) {
                runningUids[i] = this.mRunningAppUids.valueAt(i).intValue();
            }
        }
        synchronized (this.mCallbackLock) {
            if (this.mRoutingCallback != null) {
                try {
                    this.mRoutingCallback.onAppsNeedingAudioRoutingChanged(runningUids);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "RemoteException when calling updateReroutingApps", e);
                }
            }
        }
    }

    private static android.util.ArraySet<java.lang.Integer> findPlayingAppUids(java.util.List<android.media.AudioPlaybackConfiguration> configs, android.util.ArraySet<java.lang.Integer> runningAppUids) {
        android.util.ArraySet<java.lang.Integer> playingAppUids = new android.util.ArraySet<>();
        for (android.media.AudioPlaybackConfiguration config : configs) {
            if (runningAppUids.contains(java.lang.Integer.valueOf(config.getClientUid())) && config.getPlayerState() == 2) {
                playingAppUids.add(java.lang.Integer.valueOf(config.getClientUid()));
            }
        }
        return playingAppUids;
    }

    private static java.util.List<android.media.AudioPlaybackConfiguration> findPlaybackConfigurations(java.util.List<android.media.AudioPlaybackConfiguration> configs, android.util.ArraySet<java.lang.Integer> runningAppUids) {
        java.util.List<android.media.AudioPlaybackConfiguration> runningConfigs = new java.util.ArrayList<>();
        for (android.media.AudioPlaybackConfiguration config : configs) {
            if (runningAppUids.contains(java.lang.Integer.valueOf(config.getClientUid()))) {
                runningConfigs.add(config);
            }
        }
        return runningConfigs;
    }

    private static java.util.List<android.media.AudioRecordingConfiguration> findRecordingConfigurations(java.util.List<android.media.AudioRecordingConfiguration> configs, android.util.ArraySet<java.lang.Integer> runningAppUids) {
        java.util.List<android.media.AudioRecordingConfiguration> runningConfigs = new java.util.ArrayList<>();
        for (android.media.AudioRecordingConfiguration config : configs) {
            if (runningAppUids.contains(java.lang.Integer.valueOf(config.getClientUid()))) {
                runningConfigs.add(config);
            }
        }
        return runningConfigs;
    }

    boolean hasPendingRunnable() {
        return this.mHandler.hasCallbacks(this.mUpdateAudioRoutingRunnable);
    }

    void addPlayingAppsForTesting(int appUid) {
        synchronized (this.mLock) {
            this.mPlayingAppUids.add(java.lang.Integer.valueOf(appUid));
        }
    }
}
