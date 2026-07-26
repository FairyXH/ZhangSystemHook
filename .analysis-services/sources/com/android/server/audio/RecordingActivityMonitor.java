package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public final class RecordingActivityMonitor implements android.media.AudioSystem.AudioRecordingCallback {
    private static final boolean DEBUG;
    public static final java.lang.String TAG = "AudioService.RecordingActivityMonitor";
    private static final com.android.server.utils.EventLogger sEventLogger;
    private com.android.server.audio.AudioService mAudioService;
    private final android.content.pm.PackageManager mPackMan;
    com.android.server.audio.IRecordingActivityMonitorExt mRecordingActivityMonitorExt;
    private java.util.ArrayList<com.android.server.audio.RecordingActivityMonitor.RecMonitorClient> mClients = new java.util.ArrayList<>();
    private boolean mHasPublicClients = false;
    private java.util.concurrent.atomic.AtomicInteger mLegacyRemoteSubmixRiid = new java.util.concurrent.atomic.AtomicInteger(-1);
    private java.util.concurrent.atomic.AtomicBoolean mLegacyRemoteSubmixActive = new java.util.concurrent.atomic.AtomicBoolean(false);
    private java.util.List<com.android.server.audio.RecordingActivityMonitor.RecordingState> mRecordStates = new java.util.ArrayList();

    static {
        DEBUG = "eng".equals(android.os.Build.TYPE) || "userdebug".equals(android.os.Build.TYPE);
        sEventLogger = new com.android.server.utils.EventLogger(50, "recording activity received by AudioService");
    }

    static final class RecordingState {
        private android.media.AudioRecordingConfiguration mConfig;
        private final com.android.server.audio.RecordingActivityMonitor.RecorderDeathHandler mDeathHandler;
        private boolean mIsActive;
        private com.android.server.audio.RecordingActivityMonitor.RecordingState.RecordingStateWrapper mRecordingStateWrapper;
        private final int mRiid;

        RecordingState(int riid, com.android.server.audio.RecordingActivityMonitor.RecorderDeathHandler handler) {
            this.mRecordingStateWrapper = new com.android.server.audio.RecordingActivityMonitor.RecordingState.RecordingStateWrapper();
            this.mRiid = riid;
            this.mDeathHandler = handler;
        }

        RecordingState(android.media.AudioRecordingConfiguration config) {
            this.mRecordingStateWrapper = new com.android.server.audio.RecordingActivityMonitor.RecordingState.RecordingStateWrapper();
            this.mRiid = -1;
            this.mDeathHandler = null;
            this.mConfig = config;
        }

        int getRiid() {
            return this.mRiid;
        }

        int getPortId() {
            if (this.mConfig != null) {
                return this.mConfig.getClientPortId();
            }
            return -1;
        }

        android.media.AudioRecordingConfiguration getConfig() {
            return this.mConfig;
        }

        boolean hasDeathHandler() {
            return this.mDeathHandler != null;
        }

        boolean isActiveConfiguration() {
            return this.mIsActive && this.mConfig != null;
        }

        void release() {
            if (this.mDeathHandler != null) {
                this.mDeathHandler.release();
            }
        }

        boolean setActive(boolean active) {
            if (this.mIsActive == active) {
                return false;
            }
            this.mIsActive = active;
            return this.mConfig != null;
        }

        boolean setConfig(android.media.AudioRecordingConfiguration config) {
            if (config.equals(this.mConfig)) {
                return false;
            }
            this.mConfig = config;
            return this.mIsActive;
        }

        void dump(java.io.PrintWriter pw) {
            pw.println("riid " + this.mRiid + "; active? " + this.mIsActive);
            if (this.mConfig != null) {
                this.mConfig.dump(pw);
            } else {
                pw.println("  no config");
            }
        }

        public com.android.server.audio.IRecordingStateWrapper getWrapper() {
            return this.mRecordingStateWrapper;
        }

        private class RecordingStateWrapper implements com.android.server.audio.IRecordingStateWrapper {
            private RecordingStateWrapper() {
            }

            @Override // com.android.server.audio.IRecordingStateWrapper
            public boolean getIsActive() {
                return com.android.server.audio.RecordingActivityMonitor.RecordingState.this.mIsActive;
            }
        }
    }

    RecordingActivityMonitor(android.content.Context ctxt, com.android.server.audio.AudioService audioservice) {
        com.android.server.audio.RecordingActivityMonitor.RecMonitorClient.sMonitor = this;
        com.android.server.audio.RecordingActivityMonitor.RecorderDeathHandler.sMonitor = this;
        this.mPackMan = ctxt.getPackageManager();
        this.mAudioService = audioservice;
        this.mRecordingActivityMonitorExt = (com.android.server.audio.IRecordingActivityMonitorExt) system.ext.loader.core.ExtLoader.type(com.android.server.audio.IRecordingActivityMonitorExt.class).base(this).create();
    }

    public void onRecordingConfigurationChanged(int event, int riid, int uid, int session, int source, int portId, boolean silenced, int[] recordingInfo, android.media.audiofx.AudioEffect.Descriptor[] clientEffects, android.media.audiofx.AudioEffect.Descriptor[] effects, int activeSource, java.lang.String packName) {
        boolean z;
        android.media.AudioDeviceInfo device;
        android.media.AudioRecordingConfiguration config = createRecordingConfiguration(uid, session, source, recordingInfo, portId, silenced, activeSource, clientEffects, effects);
        if (DEBUG) {
            android.util.Log.i(TAG, "onRecordingConfigurationChanged(uid=" + uid + " event=" + event + " session=" + session + " source=" + source + " packName=" + packName + " riid=" + riid + " activeSource=" + activeSource + " packName=" + packName + " clientSampleRate=" + recordingInfo[2] + " deviceSampleRate=" + recordingInfo[5]);
        }
        if (!android.os.Build.isMtkPlatform()) {
            z = true;
        } else {
            z = true;
            this.mAudioService.handleRecordingConfigurationChanged(event, recordingInfo[2], uid, source, config.getAudioDevice());
            if (riid != -1 && event == 1) {
                return;
            }
        }
        if (source == 8 && ((event == 0 || event == 2) && (device = config.getAudioDevice()) != null && "0".equals(device.getAddress()))) {
            this.mLegacyRemoteSubmixRiid.set(riid);
            this.mLegacyRemoteSubmixActive.set(z);
        }
        if (!android.media.MediaRecorder.isSystemOnlyAudioSource(source)) {
            dispatchCallbacks(updateSnapshot(event, riid, config));
        } else {
            sEventLogger.enqueue(new com.android.server.audio.RecordingActivityMonitor.RecordingEvent(event, riid, config).printLog(TAG));
        }
    }

    public int trackRecorder(android.os.IBinder recorder) {
        if (recorder == null) {
            android.util.Log.e(TAG, "trackRecorder called with null token");
            return -1;
        }
        int newRiid = android.media.AudioSystem.newAudioRecorderId();
        com.android.server.audio.RecordingActivityMonitor.RecorderDeathHandler handler = new com.android.server.audio.RecordingActivityMonitor.RecorderDeathHandler(newRiid, recorder);
        if (!handler.init()) {
            return -1;
        }
        synchronized (this.mRecordStates) {
            this.mRecordStates.add(new com.android.server.audio.RecordingActivityMonitor.RecordingState(newRiid, handler));
        }
        return newRiid;
    }

    public void recorderEvent(int riid, int event) {
        android.util.Log.d(TAG, "recorderEvent, event = " + event);
        int configEvent = 0;
        if (this.mLegacyRemoteSubmixRiid.get() == riid) {
            this.mLegacyRemoteSubmixActive.set(event == 0);
        }
        if (event != 0) {
            if (event != 1) {
                configEvent = -1;
            } else {
                configEvent = 1;
            }
        }
        if (riid == -1 || configEvent == -1) {
            sEventLogger.enqueue(new com.android.server.audio.RecordingActivityMonitor.RecordingEvent(event, riid, null).printLog(TAG));
        } else {
            dispatchCallbacks(updateSnapshot(configEvent, riid, null));
        }
    }

    public void releaseRecorder(int riid) {
        dispatchCallbacks(updateSnapshot(3, riid, null));
    }

    public boolean isRecordingActiveForUid(int uid) {
        synchronized (this.mRecordStates) {
            for (com.android.server.audio.RecordingActivityMonitor.RecordingState state : this.mRecordStates) {
                if (state.isActiveConfiguration() && state.getConfig().getClientUid() == uid && !state.getConfig().isClientSilenced()) {
                    return true;
                }
            }
            return false;
        }
    }

    private void dispatchCallbacks(java.util.List<android.media.AudioRecordingConfiguration> configs) {
        java.util.List<android.media.AudioRecordingConfiguration> configsPublic;
        if (configs == null) {
            return;
        }
        synchronized (this.mClients) {
            if (this.mHasPublicClients) {
                configsPublic = anonymizeForPublicConsumption(configs);
            } else {
                configsPublic = new java.util.ArrayList<>();
            }
            for (com.android.server.audio.RecordingActivityMonitor.RecMonitorClient rmc : this.mClients) {
                try {
                    if (rmc.mIsPrivileged) {
                        rmc.mDispatcherCb.dispatchRecordingConfigChange(configs);
                    } else {
                        rmc.mDispatcherCb.dispatchRecordingConfigChange(configsPublic);
                    }
                } catch (android.os.RemoteException e) {
                    android.util.Log.w(TAG, "Could not call dispatchRecordingConfigChange() on client", e);
                }
            }
        }
    }

    protected void dump(java.io.PrintWriter pw) {
        pw.println("\nRecordActivityMonitor dump time: " + java.text.DateFormat.getTimeInstance().format(new java.util.Date()));
        synchronized (this.mRecordStates) {
            for (com.android.server.audio.RecordingActivityMonitor.RecordingState state : this.mRecordStates) {
                state.dump(pw);
            }
        }
        pw.println("\n");
        sEventLogger.dump(pw);
    }

    private static java.util.ArrayList<android.media.AudioRecordingConfiguration> anonymizeForPublicConsumption(java.util.List<android.media.AudioRecordingConfiguration> sysConfigs) {
        java.util.ArrayList<android.media.AudioRecordingConfiguration> publicConfigs = new java.util.ArrayList<>();
        for (android.media.AudioRecordingConfiguration config : sysConfigs) {
            publicConfigs.add(android.media.AudioRecordingConfiguration.anonymizedCopy(config));
        }
        return publicConfigs;
    }

    void initMonitor() {
        android.media.AudioSystem.setRecordingCallback(this);
    }

    void onAudioServerDied() {
        java.util.List<android.media.AudioRecordingConfiguration> configs = null;
        synchronized (this.mRecordStates) {
            boolean configChanged = false;
            java.util.Iterator<com.android.server.audio.RecordingActivityMonitor.RecordingState> it = this.mRecordStates.iterator();
            while (it.hasNext()) {
                com.android.server.audio.RecordingActivityMonitor.RecordingState state = it.next();
                if (!state.hasDeathHandler()) {
                    if (state.isActiveConfiguration()) {
                        configChanged = true;
                        sEventLogger.enqueue(new com.android.server.audio.RecordingActivityMonitor.RecordingEvent(3, state.getRiid(), state.getConfig()));
                    }
                    it.remove();
                }
            }
            if (configChanged) {
                configs = getActiveRecordingConfigurations(true);
            }
        }
        dispatchCallbacks(configs);
    }

    void registerRecordingCallback(android.media.IRecordingConfigDispatcher rcdb, boolean isPrivileged) {
        if (rcdb == null) {
            return;
        }
        synchronized (this.mClients) {
            com.android.server.audio.RecordingActivityMonitor.RecMonitorClient rmc = new com.android.server.audio.RecordingActivityMonitor.RecMonitorClient(rcdb, isPrivileged);
            if (rmc.init()) {
                if (!isPrivileged) {
                    this.mHasPublicClients = true;
                }
                this.mClients.add(rmc);
            }
        }
    }

    void unregisterRecordingCallback(android.media.IRecordingConfigDispatcher rcdb) {
        if (rcdb == null) {
            return;
        }
        synchronized (this.mClients) {
            java.util.Iterator<com.android.server.audio.RecordingActivityMonitor.RecMonitorClient> clientIterator = this.mClients.iterator();
            boolean hasPublicClients = false;
            while (clientIterator.hasNext()) {
                com.android.server.audio.RecordingActivityMonitor.RecMonitorClient rmc = clientIterator.next();
                if (rcdb.asBinder().equals(rmc.mDispatcherCb.asBinder())) {
                    rmc.release();
                    clientIterator.remove();
                } else if (!rmc.mIsPrivileged) {
                    hasPublicClients = true;
                }
            }
            this.mHasPublicClients = hasPublicClients;
        }
    }

    java.util.List<android.media.AudioRecordingConfiguration> getActiveRecordingConfigurations(boolean isPrivileged) {
        java.util.List<android.media.AudioRecordingConfiguration> configs = new java.util.ArrayList<>();
        synchronized (this.mRecordStates) {
            for (com.android.server.audio.RecordingActivityMonitor.RecordingState state : this.mRecordStates) {
                if (state.isActiveConfiguration()) {
                    configs.add(state.getConfig());
                }
            }
        }
        if (!isPrivileged) {
            return anonymizeForPublicConsumption(configs);
        }
        return configs;
    }

    boolean isLegacyRemoteSubmixActive() {
        return this.mLegacyRemoteSubmixActive.get();
    }

    private android.media.AudioRecordingConfiguration createRecordingConfiguration(int uid, int session, int source, int[] recordingInfo, int portId, boolean silenced, int activeSource, android.media.audiofx.AudioEffect.Descriptor[] clientEffects, android.media.audiofx.AudioEffect.Descriptor[] effects) {
        java.lang.String packageName;
        android.media.AudioFormat clientFormat = new android.media.AudioFormat.Builder().setEncoding(recordingInfo[0]).setChannelMask(recordingInfo[1]).setSampleRate(recordingInfo[2]).build();
        android.media.AudioFormat deviceFormat = new android.media.AudioFormat.Builder().setEncoding(recordingInfo[3]).setChannelMask(recordingInfo[4]).setSampleRate(recordingInfo[5]).build();
        int patchHandle = recordingInfo[6];
        java.lang.String[] packages = this.mPackMan.getPackagesForUid(uid);
        if (packages != null && packages.length > 0) {
            packageName = packages[0];
        } else {
            packageName = "";
        }
        return new android.media.AudioRecordingConfiguration(uid, session, source, clientFormat, deviceFormat, patchHandle, packageName, portId, silenced, activeSource, clientEffects, effects);
    }

    private java.util.List<android.media.AudioRecordingConfiguration> updateSnapshot(int event, int riid, android.media.AudioRecordingConfiguration config) {
        java.util.List<android.media.AudioRecordingConfiguration> configs = null;
        synchronized (this.mRecordStates) {
            int stateIndex = -1;
            try {
                if (riid != -1) {
                    stateIndex = findStateByRiid(riid);
                } else if (config != null) {
                    stateIndex = findStateByPortId(config.getClientPortId());
                }
                if (stateIndex == -1) {
                    if (event == 0 && config != null) {
                        this.mRecordStates.add(new com.android.server.audio.RecordingActivityMonitor.RecordingState(config));
                        stateIndex = this.mRecordStates.size() - 1;
                    } else {
                        if (config == null) {
                            android.util.Log.e(TAG, java.lang.String.format("Unexpected event %d for riid %d", java.lang.Integer.valueOf(event), java.lang.Integer.valueOf(riid)));
                        }
                        return null;
                    }
                }
                com.android.server.audio.RecordingActivityMonitor.RecordingState state = this.mRecordStates.get(stateIndex);
                boolean configChanged = false;
                switch (event) {
                    case 0:
                        boolean configChanged2 = state.setActive(true);
                        if (config == null) {
                            configChanged = configChanged2;
                        } else if (state.setConfig(config) || configChanged2) {
                            configChanged = true;
                        }
                        break;
                    case 1:
                        configChanged = state.setActive(false);
                        if (!state.hasDeathHandler()) {
                            this.mRecordStates.remove(stateIndex);
                        }
                        break;
                    case 2:
                        configChanged = state.setConfig(config);
                        break;
                    case 3:
                        configChanged = state.isActiveConfiguration();
                        state.release();
                        this.mRecordStates.remove(stateIndex);
                        break;
                    default:
                        android.util.Log.e(TAG, java.lang.String.format("Unknown event %d for riid %d / portid %d", java.lang.Integer.valueOf(event), java.lang.Integer.valueOf(riid), java.lang.Integer.valueOf(state.getPortId())));
                        configChanged = false;
                        break;
                }
                if (configChanged) {
                    sEventLogger.enqueue(new com.android.server.audio.RecordingActivityMonitor.RecordingEvent(event, riid, state.getConfig()));
                    configs = getActiveRecordingConfigurations(true);
                }
                this.mRecordingActivityMonitorExt.endHookUpdateSnapshot(state);
                return configs;
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    private int findStateByRiid(int riid) {
        synchronized (this.mRecordStates) {
            for (int i = 0; i < this.mRecordStates.size(); i++) {
                if (this.mRecordStates.get(i).getRiid() == riid) {
                    return i;
                }
            }
            return -1;
        }
    }

    private int findStateByPortId(int portId) {
        synchronized (this.mRecordStates) {
            for (int i = 0; i < this.mRecordStates.size(); i++) {
                if (!this.mRecordStates.get(i).hasDeathHandler() && this.mRecordStates.get(i).getPortId() == portId) {
                    return i;
                }
            }
            return -1;
        }
    }

    private static final class RecMonitorClient implements android.os.IBinder.DeathRecipient {
        static com.android.server.audio.RecordingActivityMonitor sMonitor;
        final android.media.IRecordingConfigDispatcher mDispatcherCb;
        final boolean mIsPrivileged;

        RecMonitorClient(android.media.IRecordingConfigDispatcher rcdb, boolean isPrivileged) {
            this.mDispatcherCb = rcdb;
            this.mIsPrivileged = isPrivileged;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Log.w(com.android.server.audio.RecordingActivityMonitor.TAG, "client died");
            sMonitor.unregisterRecordingCallback(this.mDispatcherCb);
        }

        boolean init() {
            try {
                this.mDispatcherCb.asBinder().linkToDeath(this, 0);
                return true;
            } catch (android.os.RemoteException e) {
                android.util.Log.w(com.android.server.audio.RecordingActivityMonitor.TAG, "Could not link to client death", e);
                return false;
            }
        }

        void release() {
            this.mDispatcherCb.asBinder().unlinkToDeath(this, 0);
        }
    }

    private static final class RecorderDeathHandler implements android.os.IBinder.DeathRecipient {
        static com.android.server.audio.RecordingActivityMonitor sMonitor;
        private final android.os.IBinder mRecorderToken;
        final int mRiid;

        RecorderDeathHandler(int riid, android.os.IBinder recorderToken) {
            this.mRiid = riid;
            this.mRecorderToken = recorderToken;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            sMonitor.releaseRecorder(this.mRiid);
        }

        boolean init() {
            try {
                this.mRecorderToken.linkToDeath(this, 0);
                return true;
            } catch (android.os.RemoteException e) {
                android.util.Log.w(com.android.server.audio.RecordingActivityMonitor.TAG, "Could not link to recorder death", e);
                return false;
            }
        }

        void release() {
            this.mRecorderToken.unlinkToDeath(this, 0);
        }
    }

    private static final class RecordingEvent extends com.android.server.utils.EventLogger.Event {
        private final int mClientUid;
        private final java.lang.String mPackName;
        private final int mRIId;
        private final int mRecEvent;
        private final int mSession;
        private final boolean mSilenced;
        private final int mSource;

        RecordingEvent(int event, int riid, android.media.AudioRecordingConfiguration config) {
            this.mRecEvent = event;
            this.mRIId = riid;
            if (config != null) {
                this.mClientUid = config.getClientUid();
                this.mSession = config.getClientAudioSessionId();
                this.mSource = config.getClientAudioSource();
                this.mPackName = config.getClientPackageName();
                this.mSilenced = config.isClientSilenced();
                return;
            }
            this.mClientUid = -1;
            this.mSession = -1;
            this.mSource = -1;
            this.mPackName = null;
            this.mSilenced = false;
        }

        private static java.lang.String recordEventToString(int recEvent) {
            switch (recEvent) {
                case 0:
                    return "start";
                case 1:
                    return "stop";
                case 2:
                    return "update";
                case 3:
                    return "release";
                default:
                    return "unknown (" + recEvent + ")";
            }
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            return "rec " + recordEventToString(this.mRecEvent) + " riid:" + this.mRIId + " uid:" + this.mClientUid + " session:" + this.mSession + " src:" + android.media.MediaRecorder.toLogFriendlyAudioSource(this.mSource) + (this.mSilenced ? " silenced" : " not silenced") + (this.mPackName == null ? "" : " pack:" + this.mPackName);
        }
    }
}
