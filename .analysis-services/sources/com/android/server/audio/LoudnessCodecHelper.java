package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class LoudnessCodecHelper {
    private static final boolean DEBUG = false;
    static final int SPL_RANGE_LARGE = 3;
    static final int SPL_RANGE_MEDIUM = 2;
    static final int SPL_RANGE_SMALL = 1;
    static final int SPL_RANGE_UNKNOWN = 0;
    private static final java.lang.String SYSTEM_PROPERTY_SPEAKER_SPL_RANGE_SIZE = "audio.loudness.builtin-speaker-spl-range-size";
    private static final java.lang.String TAG = "AS.LoudnessCodecHelper";
    private static final com.android.server.utils.EventLogger sLogger = new com.android.server.utils.EventLogger(30, "Loudness updates");
    private final com.android.server.audio.AudioService mAudioService;
    private final com.android.server.audio.LoudnessCodecHelper.LoudnessRemoteCallbackList mLoudnessUpdateDispatchers = new com.android.server.audio.LoudnessCodecHelper.LoudnessRemoteCallbackList(this);
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.HashMap<com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId, java.util.Set<java.lang.Integer>> mStartedConfigPiids = new java.util.HashMap<>();
    private final java.util.HashMap<com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId, java.util.Set<android.media.LoudnessCodecInfo>> mStartedConfigInfo = new java.util.HashMap<>();
    private final android.util.SparseIntArray mPiidToDeviceIdCache = new android.util.SparseIntArray();
    private final android.util.SparseIntArray mPiidToPidCache = new android.util.SparseIntArray();
    private final java.util.HashMap<com.android.server.audio.LoudnessCodecHelper.LoudnessCodecInputProperties, android.os.PersistableBundle> mCachedProperties = new java.util.HashMap<>();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface DeviceSplRange {
    }

    private static final class LoudnessRemoteCallbackList extends android.os.RemoteCallbackList<android.media.ILoudnessCodecUpdatesDispatcher> {
        private final com.android.server.audio.LoudnessCodecHelper mLoudnessCodecHelper;

        LoudnessRemoteCallbackList(com.android.server.audio.LoudnessCodecHelper loudnessCodecHelper) {
            this.mLoudnessCodecHelper = loudnessCodecHelper;
        }

        @Override // android.os.RemoteCallbackList
        public void onCallbackDied(android.media.ILoudnessCodecUpdatesDispatcher callback, java.lang.Object cookie) {
            java.lang.Integer pid = null;
            if (cookie instanceof java.lang.Integer) {
                pid = (java.lang.Integer) cookie;
            }
            if (pid != null) {
                com.android.server.audio.LoudnessCodecHelper.sLogger.enqueue(com.android.server.audio.AudioServiceEvents.LoudnessEvent.getClientDied(pid.intValue()));
                this.mLoudnessCodecHelper.onClientPidDied(pid.intValue());
            }
            super.onCallbackDied(callback, cookie);
        }
    }

    static final class LoudnessCodecInputProperties {
        private final int mDeviceSplRange;
        private final boolean mIsDownmixing;
        private final int mMetadataType;

        static final class Builder {
            private int mDeviceSplRange;
            private boolean mIsDownmixing;
            private int mMetadataType;

            Builder() {
            }

            com.android.server.audio.LoudnessCodecHelper.LoudnessCodecInputProperties.Builder setMetadataType(int metadataType) {
                this.mMetadataType = metadataType;
                return this;
            }

            com.android.server.audio.LoudnessCodecHelper.LoudnessCodecInputProperties.Builder setIsDownmixing(boolean isDownmixing) {
                this.mIsDownmixing = isDownmixing;
                return this;
            }

            com.android.server.audio.LoudnessCodecHelper.LoudnessCodecInputProperties.Builder setDeviceSplRange(int deviceSplRange) {
                this.mDeviceSplRange = deviceSplRange;
                return this;
            }

            com.android.server.audio.LoudnessCodecHelper.LoudnessCodecInputProperties build() {
                return new com.android.server.audio.LoudnessCodecHelper.LoudnessCodecInputProperties(this.mMetadataType, this.mIsDownmixing, this.mDeviceSplRange);
            }
        }

        private LoudnessCodecInputProperties(int metadataType, boolean isDownmixing, int deviceSplRange) {
            this.mMetadataType = metadataType;
            this.mIsDownmixing = isDownmixing;
            this.mDeviceSplRange = deviceSplRange;
        }

        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            com.android.server.audio.LoudnessCodecHelper.LoudnessCodecInputProperties lcip = (com.android.server.audio.LoudnessCodecHelper.LoudnessCodecInputProperties) obj;
            if (this.mMetadataType == lcip.mMetadataType && this.mIsDownmixing == lcip.mIsDownmixing && this.mDeviceSplRange == lcip.mDeviceSplRange) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.mMetadataType), java.lang.Boolean.valueOf(this.mIsDownmixing), java.lang.Integer.valueOf(this.mDeviceSplRange));
        }

        public java.lang.String toString() {
            return "Loudness properties: device SPL range: " + com.android.server.audio.LoudnessCodecHelper.splRangeToString(this.mDeviceSplRange) + " down-mixing: " + this.mIsDownmixing + " metadata type: " + this.mMetadataType;
        }

        android.os.PersistableBundle createLoudnessParameters() {
            android.os.PersistableBundle persistableBundle = new android.os.PersistableBundle();
            switch (this.mDeviceSplRange) {
                case 1:
                    persistableBundle.putInt("aac-target-ref-level", 64);
                    if (this.mMetadataType == 1) {
                        persistableBundle.putInt("aac-drc-heavy-compression", 1);
                    } else if (this.mMetadataType == 2) {
                        persistableBundle.putInt("aac-drc-effect-type", 3);
                    }
                    return persistableBundle;
                case 2:
                    persistableBundle.putInt("aac-target-ref-level", 96);
                    if (this.mMetadataType == 1) {
                        persistableBundle.putInt("aac-drc-heavy-compression", this.mIsDownmixing ? 1 : 0);
                    } else if (this.mMetadataType == 2) {
                        persistableBundle.putInt("aac-drc-effect-type", 6);
                    }
                    return persistableBundle;
                case 3:
                    persistableBundle.putInt("aac-target-ref-level", 124);
                    if (this.mMetadataType == 1) {
                        persistableBundle.putInt("aac-drc-heavy-compression", 0);
                    } else if (this.mMetadataType == 2) {
                        persistableBundle.putInt("aac-drc-effect-type", 6);
                    }
                    return persistableBundle;
                default:
                    persistableBundle.putInt("aac-target-ref-level", 96);
                    if (this.mMetadataType == 1) {
                        persistableBundle.putInt("aac-drc-heavy-compression", this.mIsDownmixing ? 1 : 0);
                    } else if (this.mMetadataType == 2) {
                        persistableBundle.putInt("aac-drc-effect-type", 6);
                    }
                    return persistableBundle;
            }
        }
    }

    static final class LoudnessTrackId {
        private final int mPid;
        private final int mSessionId;

        private LoudnessTrackId(int sessionId, int pid) {
            this.mSessionId = sessionId;
            this.mPid = pid;
        }

        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId lti = (com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId) obj;
            if (this.mSessionId == lti.mSessionId && this.mPid == lti.mPid) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.mSessionId), java.lang.Integer.valueOf(this.mPid));
        }

        public java.lang.String toString() {
            return "Loudness track id: session ID: " + this.mSessionId + " pid: " + this.mPid;
        }
    }

    LoudnessCodecHelper(com.android.server.audio.AudioService audioService) {
        this.mAudioService = (com.android.server.audio.AudioService) java.util.Objects.requireNonNull(audioService);
    }

    void registerLoudnessCodecUpdatesDispatcher(android.media.ILoudnessCodecUpdatesDispatcher dispatcher) {
        this.mLoudnessUpdateDispatchers.register(dispatcher, java.lang.Integer.valueOf(android.os.Binder.getCallingPid()));
    }

    void unregisterLoudnessCodecUpdatesDispatcher(android.media.ILoudnessCodecUpdatesDispatcher dispatcher) {
        this.mLoudnessUpdateDispatchers.unregister(dispatcher);
    }

    void startLoudnessCodecUpdates(final int sessionId) {
        final int pid = android.os.Binder.getCallingPid();
        com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId newConfig = new com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId(sessionId, pid);
        synchronized (this.mLock) {
            if (this.mStartedConfigInfo.containsKey(newConfig)) {
                android.util.Log.w(TAG, "Already started loudness updates for config: " + newConfig);
                return;
            }
            this.mStartedConfigInfo.put(newConfig, new java.util.HashSet());
            final java.util.HashSet<java.lang.Integer> newPiids = new java.util.HashSet<>();
            this.mStartedConfigPiids.put(newConfig, newPiids);
            android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
            try {
                this.mAudioService.getActivePlaybackConfigurations().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.audio.LoudnessCodecHelper$$ExternalSyntheticLambda2
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.audio.LoudnessCodecHelper.lambda$startLoudnessCodecUpdates$0(sessionId, pid, (android.media.AudioPlaybackConfiguration) obj);
                    }
                }).forEach(new java.util.function.Consumer() { // from class: com.android.server.audio.LoudnessCodecHelper$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$startLoudnessCodecUpdates$1(newPiids, pid, (android.media.AudioPlaybackConfiguration) obj);
                    }
                });
                if (ignored != null) {
                    ignored.close();
                }
            } catch (java.lang.Throwable th) {
                if (ignored != null) {
                    try {
                        ignored.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
    }

    static /* synthetic */ boolean lambda$startLoudnessCodecUpdates$0(int sessionId, int pid, android.media.AudioPlaybackConfiguration conf) {
        return conf.getSessionId() == sessionId && conf.getClientPid() == pid;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startLoudnessCodecUpdates$1(java.util.HashSet newPiids, int pid, android.media.AudioPlaybackConfiguration apc) {
        int piid = apc.getPlayerInterfaceId();
        synchronized (this.mLock) {
            newPiids.add(java.lang.Integer.valueOf(piid));
            this.mPiidToPidCache.put(piid, pid);
            sLogger.enqueue(com.android.server.audio.AudioServiceEvents.LoudnessEvent.getStartPiid(piid, pid));
        }
    }

    void stopLoudnessCodecUpdates(int sessionId) {
        int pid = android.os.Binder.getCallingPid();
        com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId config = new com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId(sessionId, pid);
        synchronized (this.mLock) {
            if (!this.mStartedConfigInfo.containsKey(config)) {
                android.util.Log.w(TAG, "Loudness updates are already stopped config: " + config);
                return;
            }
            java.util.Set<java.lang.Integer> startedPiidSet = this.mStartedConfigPiids.get(config);
            if (startedPiidSet == null) {
                android.util.Log.e(TAG, "Loudness updates are already stopped config: " + config);
                return;
            }
            for (java.lang.Integer piid : startedPiidSet) {
                sLogger.enqueue(com.android.server.audio.AudioServiceEvents.LoudnessEvent.getStopPiid(piid.intValue(), this.mPiidToPidCache.get(piid.intValue(), -1)));
                this.mPiidToDeviceIdCache.delete(piid.intValue());
                this.mPiidToPidCache.delete(piid.intValue());
            }
            this.mStartedConfigPiids.remove(config);
            this.mStartedConfigInfo.remove(config);
        }
    }

    void addLoudnessCodecInfo(final int sessionId, int mediaCodecHash, android.media.LoudnessCodecInfo info) {
        final int pid = android.os.Binder.getCallingPid();
        com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId config = new com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId(sessionId, pid);
        synchronized (this.mLock) {
            if (this.mStartedConfigInfo.containsKey(config) && this.mStartedConfigPiids.containsKey(config)) {
                java.util.Set<java.lang.Integer> piids = this.mStartedConfigPiids.get(config);
                java.util.Set<android.media.LoudnessCodecInfo> infoSet = this.mStartedConfigInfo.get(config);
                infoSet.add(info);
                android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
                try {
                    android.os.PersistableBundle updateBundle = new android.os.PersistableBundle();
                    java.util.Optional<android.media.AudioPlaybackConfiguration> apc = this.mAudioService.getActivePlaybackConfigurations().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.audio.LoudnessCodecHelper$$ExternalSyntheticLambda6
                        @Override // java.util.function.Predicate
                        public final boolean test(java.lang.Object obj) {
                            return com.android.server.audio.LoudnessCodecHelper.lambda$addLoudnessCodecInfo$2(sessionId, pid, (android.media.AudioPlaybackConfiguration) obj);
                        }
                    }).findFirst();
                    if (apc.isEmpty()) {
                        updateBundle.putPersistableBundle(java.lang.Integer.toString(mediaCodecHash), getLoudnessParams(info));
                    } else {
                        android.media.AudioDeviceInfo deviceInfo = apc.get().getAudioDeviceInfo();
                        if (deviceInfo != null) {
                            synchronized (this.mLock) {
                                piids.add(java.lang.Integer.valueOf(apc.get().getPlayerInterfaceId()));
                                updateBundle.putPersistableBundle(java.lang.Integer.toString(mediaCodecHash), getCodecBundle_l(deviceInfo.getInternalType(), deviceInfo.getAddress(), info));
                            }
                        }
                    }
                    if (!updateBundle.isDefinitelyEmpty()) {
                        dispatchNewLoudnessParameters(sessionId, updateBundle);
                    }
                    if (ignored != null) {
                        ignored.close();
                        return;
                    }
                    return;
                } catch (java.lang.Throwable th) {
                    if (ignored != null) {
                        try {
                            ignored.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
            android.util.Log.w(TAG, "Cannot add new loudness info for stopped config " + config);
        }
    }

    static /* synthetic */ boolean lambda$addLoudnessCodecInfo$2(int sessionId, int pid, android.media.AudioPlaybackConfiguration conf) {
        return conf.getSessionId() == sessionId && conf.getClientPid() == pid;
    }

    void removeLoudnessCodecInfo(int sessionId, android.media.LoudnessCodecInfo codecInfo) {
        int pid = android.os.Binder.getCallingPid();
        com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId config = new com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId(sessionId, pid);
        synchronized (this.mLock) {
            if (this.mStartedConfigInfo.containsKey(config) && this.mStartedConfigPiids.containsKey(config)) {
                java.util.Set<android.media.LoudnessCodecInfo> codecInfos = this.mStartedConfigInfo.get(config);
                if (!codecInfos.remove(codecInfo)) {
                    android.util.Log.w(TAG, "Could not find to remove codecInfo " + codecInfo);
                }
                return;
            }
            android.util.Log.w(TAG, "Cannot remove loudness info for stopped config " + config);
        }
    }

    android.os.PersistableBundle getLoudnessParams(android.media.LoudnessCodecInfo codecInfo) {
        android.os.PersistableBundle codecBundle_l;
        java.util.ArrayList<android.media.AudioDeviceAttributes> devicesForAttributes = this.mAudioService.getDevicesForAttributesInt(new android.media.AudioAttributes.Builder().setUsage(1).setContentType(2).build(), false);
        if (!devicesForAttributes.isEmpty()) {
            android.media.AudioDeviceAttributes audioDeviceAttribute = devicesForAttributes.get(0);
            synchronized (this.mLock) {
                codecBundle_l = getCodecBundle_l(audioDeviceAttribute.getInternalType(), audioDeviceAttribute.getAddress(), codecInfo);
            }
            return codecBundle_l;
        }
        return new android.os.PersistableBundle();
    }

    void updateCodecParameters(java.util.List<android.media.AudioPlaybackConfiguration> configs) {
        java.util.List<android.media.AudioPlaybackConfiguration> updateApcList = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            for (android.media.AudioPlaybackConfiguration apc : configs) {
                int piid = apc.getPlayerInterfaceId();
                int cachedDeviceId = this.mPiidToDeviceIdCache.get(piid, 0);
                android.media.AudioDeviceInfo deviceInfo = apc.getAudioDeviceInfo();
                if (deviceInfo == null) {
                    if (cachedDeviceId != 0) {
                        this.mPiidToDeviceIdCache.delete(piid);
                    }
                } else if (cachedDeviceId != deviceInfo.getId()) {
                    this.mPiidToDeviceIdCache.put(piid, deviceInfo.getId());
                    com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId config = new com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId(apc.getSessionId(), apc.getClientPid());
                    if (this.mStartedConfigInfo.containsKey(config) && this.mStartedConfigPiids.containsKey(config)) {
                        updateApcList.add(apc);
                        this.mStartedConfigPiids.get(config).add(java.lang.Integer.valueOf(piid));
                    }
                }
            }
        }
        updateApcList.forEach(new java.util.function.Consumer() { // from class: com.android.server.audio.LoudnessCodecHelper$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.updateCodecParametersForConfiguration((android.media.AudioPlaybackConfiguration) obj);
            }
        });
    }

    void dump(java.io.PrintWriter pw) {
        pw.println("\nRegistered clients:\n");
        synchronized (this.mLock) {
            for (java.util.Map.Entry<com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId, java.util.Set<java.lang.Integer>> entry : this.mStartedConfigPiids.entrySet()) {
                for (java.lang.Integer piid : entry.getValue()) {
                    int pid = this.mPiidToPidCache.get(piid.intValue(), -1);
                    java.util.Set<android.media.LoudnessCodecInfo> codecInfos = this.mStartedConfigInfo.get(entry.getKey());
                    if (codecInfos != null) {
                        pw.println(java.lang.String.format("Player piid %d pid %d active codec types %s\n", piid, java.lang.Integer.valueOf(pid), codecInfos.stream().map(new java.util.function.Function() { // from class: com.android.server.audio.LoudnessCodecHelper$$ExternalSyntheticLambda0
                            @Override // java.util.function.Function
                            public final java.lang.Object apply(java.lang.Object obj) {
                                return ((android.media.LoudnessCodecInfo) obj).toString();
                            }
                        }).collect(java.util.stream.Collectors.joining(", "))));
                    }
                }
            }
            pw.println();
        }
        sLogger.dump(pw);
        pw.println();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onClientPidDied(final int pid) {
        synchronized (this.mLock) {
            for (int i = 0; i < this.mPiidToPidCache.size(); i++) {
                int piid = this.mPiidToPidCache.keyAt(i);
                if (this.mPiidToPidCache.get(piid) == pid) {
                    this.mPiidToDeviceIdCache.delete(piid);
                }
            }
            this.mStartedConfigPiids.entrySet().removeIf(new java.util.function.Predicate() { // from class: com.android.server.audio.LoudnessCodecHelper$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.audio.LoudnessCodecHelper.lambda$onClientPidDied$3(pid, (java.util.Map.Entry) obj);
                }
            });
            this.mStartedConfigInfo.entrySet().removeIf(new java.util.function.Predicate() { // from class: com.android.server.audio.LoudnessCodecHelper$$ExternalSyntheticLambda5
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.audio.LoudnessCodecHelper.lambda$onClientPidDied$4(pid, (java.util.Map.Entry) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$onClientPidDied$3(int pid, java.util.Map.Entry entry) {
        return ((com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId) entry.getKey()).mPid == pid;
    }

    static /* synthetic */ boolean lambda$onClientPidDied$4(int pid, java.util.Map.Entry entry) {
        return ((com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId) entry.getKey()).mPid == pid;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCodecParametersForConfiguration(android.media.AudioPlaybackConfiguration apc) {
        android.os.PersistableBundle allBundles = new android.os.PersistableBundle();
        synchronized (this.mLock) {
            com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId config = new com.android.server.audio.LoudnessCodecHelper.LoudnessTrackId(apc.getSessionId(), apc.getClientPid());
            java.util.Set<android.media.LoudnessCodecInfo> codecInfos = this.mStartedConfigInfo.get(config);
            android.media.AudioDeviceInfo deviceInfo = apc.getAudioDeviceInfo();
            if (codecInfos != null && deviceInfo != null) {
                for (android.media.LoudnessCodecInfo info : codecInfos) {
                    if (info != null) {
                        allBundles.putPersistableBundle(java.lang.Integer.toString(info.hashCode()), getCodecBundle_l(deviceInfo.getInternalType(), deviceInfo.getAddress(), info));
                    }
                }
            }
        }
        if (!allBundles.isDefinitelyEmpty()) {
            dispatchNewLoudnessParameters(apc.getSessionId(), allBundles);
        }
    }

    private void dispatchNewLoudnessParameters(int sessionId, android.os.PersistableBundle bundle) {
        int nbDispatchers = this.mLoudnessUpdateDispatchers.beginBroadcast();
        for (int i = 0; i < nbDispatchers; i++) {
            try {
                this.mLoudnessUpdateDispatchers.getBroadcastItem(i).dispatchLoudnessCodecParameterChange(sessionId, bundle);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Error dispatching for sessionId " + sessionId + " bundle: " + bundle, e);
            }
        }
        this.mLoudnessUpdateDispatchers.finishBroadcast();
    }

    private android.os.PersistableBundle getCodecBundle_l(int internalDeviceType, java.lang.String address, android.media.LoudnessCodecInfo codecInfo) {
        com.android.server.audio.LoudnessCodecHelper.LoudnessCodecInputProperties.Builder builder = new com.android.server.audio.LoudnessCodecHelper.LoudnessCodecInputProperties.Builder();
        com.android.server.audio.LoudnessCodecHelper.LoudnessCodecInputProperties prop = builder.setDeviceSplRange(getDeviceSplRange(internalDeviceType, address)).setIsDownmixing(codecInfo.isDownmixing).setMetadataType(codecInfo.metadataType).build();
        if (this.mCachedProperties.containsKey(prop)) {
            return this.mCachedProperties.get(prop);
        }
        android.os.PersistableBundle codecBundle = prop.createLoudnessParameters();
        this.mCachedProperties.put(prop, codecBundle);
        return codecBundle;
    }

    private int getDeviceSplRange(int internalDeviceType, java.lang.String address) {
        int deviceCategory;
        android.media.permission.SafeCloseable ignored = android.media.permission.ClearCallingIdentityContext.create();
        try {
            if (android.media.audio.Flags.automaticBtDeviceType()) {
                deviceCategory = this.mAudioService.getBluetoothAudioDeviceCategory(address);
            } else {
                deviceCategory = this.mAudioService.getBluetoothAudioDeviceCategory_legacy(address, android.media.AudioSystem.isBluetoothLeDevice(internalDeviceType));
            }
            if (ignored != null) {
                ignored.close();
            }
            if (internalDeviceType == 2) {
                java.lang.String splRange = android.os.SystemProperties.get(SYSTEM_PROPERTY_SPEAKER_SPL_RANGE_SIZE, "unknown");
                if (!splRange.equals("unknown")) {
                    return stringToSplRange(splRange);
                }
                if (!this.mAudioService.isPlatformAutomotive() && !this.mAudioService.isPlatformTelevision()) {
                    return 1;
                }
                return 2;
            }
            if (internalDeviceType == 67108864 || internalDeviceType == 8 || internalDeviceType == 4 || (android.media.AudioSystem.isBluetoothDevice(internalDeviceType) && deviceCategory == 3)) {
                return 3;
            }
            if (android.media.AudioSystem.isBluetoothDevice(internalDeviceType)) {
                if (deviceCategory == 4) {
                    return 2;
                }
                return (deviceCategory == 5 || deviceCategory == 6) ? 1 : 0;
            }
            return 0;
        } catch (java.lang.Throwable th) {
            if (ignored != null) {
                try {
                    ignored.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String splRangeToString(int splRange) {
        switch (splRange) {
            case 1:
                return "small";
            case 2:
                return "medium";
            case 3:
                return "large";
            default:
                return "unknown";
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x002c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static int stringToSplRange(java.lang.String r4) {
        /*
            int r0 = r4.hashCode()
            r1 = 0
            r2 = 1
            r3 = 2
            switch(r0) {
                case -1078030475: goto L21;
                case 102742843: goto L16;
                case 109548807: goto Lb;
                default: goto La;
            }
        La:
            goto L2c
        Lb:
            java.lang.String r0 = "small"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto La
            r0 = r3
            goto L2d
        L16:
            java.lang.String r0 = "large"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto La
            r0 = r1
            goto L2d
        L21:
            java.lang.String r0 = "medium"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto La
            r0 = r2
            goto L2d
        L2c:
            r0 = -1
        L2d:
            switch(r0) {
                case 0: goto L33;
                case 1: goto L32;
                case 2: goto L31;
                default: goto L30;
            }
        L30:
            return r1
        L31:
            return r2
        L32:
            return r3
        L33:
            r0 = 3
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.LoudnessCodecHelper.stringToSplRange(java.lang.String):int");
    }
}
