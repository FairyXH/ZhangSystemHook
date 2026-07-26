package com.android.server.broadcastradio.hal2;

/* JADX INFO: loaded from: classes.dex */
final class TunerSession extends android.hardware.radio.ITuner.Stub {
    private static final java.lang.String TAG = "BcRadio2Srv.session";
    private static final int TUNER_EVENT_LOGGER_QUEUE_SIZE = 25;
    final android.hardware.radio.ITunerCallback mCallback;
    private final android.hardware.broadcastradio.V2_0.ITunerSession mHwSession;
    private final com.android.server.broadcastradio.hal2.RadioModule mModule;
    private final java.lang.Object mLock = new java.lang.Object();
    private boolean mIsClosed = false;
    private boolean mIsMuted = false;
    private com.android.server.broadcastradio.hal2.ProgramInfoCache mProgramInfoCache = null;
    private android.hardware.radio.RadioManager.BandConfig mDummyConfig = null;
    final int mUserId = android.os.Binder.getCallingUserHandle().getIdentifier();
    private final com.android.server.broadcastradio.RadioEventLogger mEventLogger = new com.android.server.broadcastradio.RadioEventLogger(TAG, 25);

    TunerSession(com.android.server.broadcastradio.hal2.RadioModule module, android.hardware.broadcastradio.V2_0.ITunerSession hwSession, android.hardware.radio.ITunerCallback callback) {
        this.mModule = (com.android.server.broadcastradio.hal2.RadioModule) java.util.Objects.requireNonNull(module);
        this.mHwSession = (android.hardware.broadcastradio.V2_0.ITunerSession) java.util.Objects.requireNonNull(hwSession);
        this.mCallback = (android.hardware.radio.ITunerCallback) java.util.Objects.requireNonNull(callback);
    }

    public void close() {
        this.mEventLogger.logRadioEvent("Close", new java.lang.Object[0]);
        close(null);
    }

    public void close(java.lang.Integer error) {
        this.mEventLogger.logRadioEvent("Close on error %d", error);
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                return;
            }
            this.mIsClosed = true;
            if (error != null) {
                try {
                    this.mCallback.onError(error.intValue());
                } catch (android.os.RemoteException ex) {
                    com.android.server.utils.Slogf.w(TAG, "mCallback.onError() failed: ", ex);
                }
            }
            this.mModule.onTunerSessionClosed(this);
        }
    }

    public boolean isClosed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mIsClosed;
        }
        return z;
    }

    private void checkNotClosedLocked() {
        if (this.mIsClosed) {
            throw new java.lang.IllegalStateException("Tuner is closed, no further operations are allowed");
        }
    }

    public void setConfiguration(final android.hardware.radio.RadioManager.BandConfig config) {
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot set configuration for HAL 2.0 client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            this.mDummyConfig = (android.hardware.radio.RadioManager.BandConfig) java.util.Objects.requireNonNull(config);
        }
        com.android.server.utils.Slogf.i(TAG, "Ignoring setConfiguration - not applicable for broadcastradio HAL 2.0");
        this.mModule.fanoutAidlCallback(new com.android.server.broadcastradio.hal2.RadioModule.AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.hal2.TunerSession$$ExternalSyntheticLambda4
            @Override // com.android.server.broadcastradio.hal2.RadioModule.AidlCallbackRunnable
            public final void run(android.hardware.radio.ITunerCallback iTunerCallback) {
                iTunerCallback.onConfigurationChanged(config);
            }
        });
    }

    public android.hardware.radio.RadioManager.BandConfig getConfiguration() {
        android.hardware.radio.RadioManager.BandConfig bandConfig;
        synchronized (this.mLock) {
            checkNotClosedLocked();
            bandConfig = this.mDummyConfig;
        }
        return bandConfig;
    }

    public void setMuted(boolean mute) {
        synchronized (this.mLock) {
            checkNotClosedLocked();
            if (this.mIsMuted == mute) {
                return;
            }
            this.mIsMuted = mute;
            com.android.server.utils.Slogf.w(TAG, "Mute via RadioService is not implemented - please handle it via app");
        }
    }

    public boolean isMuted() {
        boolean z;
        synchronized (this.mLock) {
            checkNotClosedLocked();
            z = this.mIsMuted;
        }
        return z;
    }

    public void step(boolean directionDown, boolean skipSubChannel) throws android.os.RemoteException {
        this.mEventLogger.logRadioEvent("Step with direction %s, skipSubChannel?  %s", directionDown ? android.net.INetd.IF_STATE_DOWN : android.net.INetd.IF_STATE_UP, skipSubChannel ? com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_YES : com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_NO);
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot step on HAL 2.0 client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            int halResult = this.mHwSession.step(!directionDown);
            com.android.server.broadcastradio.hal2.Convert.throwOnError("step", halResult);
        }
    }

    public void seek(boolean directionDown, boolean skipSubChannel) throws android.os.RemoteException {
        this.mEventLogger.logRadioEvent("Seek with direction %s, skipSubChannel? %s", directionDown ? android.net.INetd.IF_STATE_DOWN : android.net.INetd.IF_STATE_UP, skipSubChannel ? com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_YES : com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_NO);
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot scan on HAL 2.0 client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            int halResult = this.mHwSession.scan(!directionDown, skipSubChannel);
            com.android.server.broadcastradio.hal2.Convert.throwOnError("step", halResult);
        }
    }

    public void tune(android.hardware.radio.ProgramSelector selector) throws android.os.RemoteException {
        this.mEventLogger.logRadioEvent("Tune with selector %s", selector);
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot tune on HAL 2.0 client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            int halResult = this.mHwSession.tune(com.android.server.broadcastradio.hal2.Convert.programSelectorToHal(selector));
            com.android.server.broadcastradio.hal2.Convert.throwOnError("tune", halResult);
        }
    }

    public void cancel() {
        com.android.server.utils.Slogf.i(TAG, "Cancel");
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot cancel on HAL 2.0 client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            final android.hardware.broadcastradio.V2_0.ITunerSession iTunerSession = this.mHwSession;
            java.util.Objects.requireNonNull(iTunerSession);
            com.android.server.broadcastradio.hal2.Utils.maybeRethrow(new com.android.server.broadcastradio.hal2.Utils.VoidFuncThrowingRemoteException() { // from class: com.android.server.broadcastradio.hal2.TunerSession$$ExternalSyntheticLambda5
                @Override // com.android.server.broadcastradio.hal2.Utils.VoidFuncThrowingRemoteException
                public final void exec() throws android.os.RemoteException {
                    iTunerSession.cancel();
                }
            });
        }
    }

    public void cancelAnnouncement() {
        com.android.server.utils.Slogf.w(TAG, "Announcements control doesn't involve cancelling at the HAL level in HAL 2.0");
    }

    public android.graphics.Bitmap getImage(int id) {
        this.mEventLogger.logRadioEvent("Get image for %d", java.lang.Integer.valueOf(id));
        return this.mModule.getImage(id);
    }

    public boolean startBackgroundScan() {
        com.android.server.utils.Slogf.w(TAG, "Explicit background scan trigger is not supported with HAL 2.0");
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot start background scan on HAL 2.0 client from non-current user");
            return false;
        }
        this.mModule.fanoutAidlCallback(new com.android.server.broadcastradio.hal2.RadioModule.AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.hal2.TunerSession$$ExternalSyntheticLambda1
            @Override // com.android.server.broadcastradio.hal2.RadioModule.AidlCallbackRunnable
            public final void run(android.hardware.radio.ITunerCallback iTunerCallback) {
                iTunerCallback.onBackgroundScanComplete();
            }
        });
        return true;
    }

    public void startProgramListUpdates(android.hardware.radio.ProgramList.Filter filter) {
        this.mEventLogger.logRadioEvent("start programList updates %s", filter);
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot start program list updates on HAL 2.0 client from non-current user");
            return;
        }
        if (filter == null) {
            filter = new android.hardware.radio.ProgramList.Filter(new android.util.ArraySet(), new android.util.ArraySet(), true, false);
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            this.mProgramInfoCache = new com.android.server.broadcastradio.hal2.ProgramInfoCache(filter);
        }
        this.mModule.onTunerSessionProgramListFilterChanged(this);
    }

    android.hardware.radio.ProgramList.Filter getProgramListFilter() {
        android.hardware.radio.ProgramList.Filter filter;
        synchronized (this.mLock) {
            filter = this.mProgramInfoCache == null ? null : this.mProgramInfoCache.getFilter();
        }
        return filter;
    }

    void onMergedProgramListUpdateFromHal(android.hardware.broadcastradio.V2_0.ProgramListChunk mergedChunk) {
        synchronized (this.mLock) {
            if (this.mProgramInfoCache == null) {
                return;
            }
            java.util.List<android.hardware.radio.ProgramList.Chunk> clientUpdateChunks = this.mProgramInfoCache.filterAndApplyChunk(mergedChunk);
            dispatchClientUpdateChunks(clientUpdateChunks);
        }
    }

    void updateProgramInfoFromHalCache(com.android.server.broadcastradio.hal2.ProgramInfoCache halCache) {
        synchronized (this.mLock) {
            if (this.mProgramInfoCache == null) {
                return;
            }
            java.util.List<android.hardware.radio.ProgramList.Chunk> clientUpdateChunks = this.mProgramInfoCache.filterAndUpdateFrom(halCache, true);
            dispatchClientUpdateChunks(clientUpdateChunks);
        }
    }

    private void dispatchClientUpdateChunks(java.util.List<android.hardware.radio.ProgramList.Chunk> chunks) {
        if (chunks == null) {
            return;
        }
        for (android.hardware.radio.ProgramList.Chunk chunk : chunks) {
            try {
                this.mCallback.onProgramListUpdated(chunk);
            } catch (android.os.RemoteException ex) {
                com.android.server.utils.Slogf.w(TAG, "mCallback.onProgramListUpdated() failed: ", ex);
            }
        }
    }

    public void stopProgramListUpdates() throws android.os.RemoteException {
        this.mEventLogger.logRadioEvent("Stop programList updates", new java.lang.Object[0]);
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot stop program list updates on HAL 2.0 client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            this.mProgramInfoCache = null;
        }
        this.mModule.onTunerSessionProgramListFilterChanged(this);
    }

    public boolean isConfigFlagSupported(int flag) {
        try {
            isConfigFlagSet(flag);
            return true;
        } catch (java.lang.IllegalStateException | java.lang.UnsupportedOperationException e) {
            return false;
        }
    }

    public boolean isConfigFlagSet(int flag) {
        boolean z;
        this.mEventLogger.logRadioEvent("Is ConfigFlagSet for %s", android.hardware.broadcastradio.V2_0.ConfigFlag.toString(flag));
        synchronized (this.mLock) {
            checkNotClosedLocked();
            final android.util.MutableInt halResult = new android.util.MutableInt(1);
            final android.util.MutableBoolean flagState = new android.util.MutableBoolean(false);
            try {
                this.mHwSession.isConfigFlagSet(flag, new android.hardware.broadcastradio.V2_0.ITunerSession.isConfigFlagSetCallback() { // from class: com.android.server.broadcastradio.hal2.TunerSession$$ExternalSyntheticLambda0
                    @Override // android.hardware.broadcastradio.V2_0.ITunerSession.isConfigFlagSetCallback
                    public final void onValues(int i, boolean z2) {
                        com.android.server.broadcastradio.hal2.TunerSession.lambda$isConfigFlagSet$2(halResult, flagState, i, z2);
                    }
                });
                com.android.server.broadcastradio.hal2.Convert.throwOnError("isConfigFlagSet", halResult.value);
                z = flagState.value;
            } catch (android.os.RemoteException ex) {
                throw new java.lang.RuntimeException("Failed to check flag " + android.hardware.broadcastradio.V2_0.ConfigFlag.toString(flag), ex);
            }
        }
        return z;
    }

    static /* synthetic */ void lambda$isConfigFlagSet$2(android.util.MutableInt halResult, android.util.MutableBoolean flagState, int result, boolean value) {
        halResult.value = result;
        flagState.value = value;
    }

    public void setConfigFlag(int flag, boolean value) throws android.os.RemoteException {
        this.mEventLogger.logRadioEvent("Set ConfigFlag  %s = %b", android.hardware.broadcastradio.V2_0.ConfigFlag.toString(flag), java.lang.Boolean.valueOf(value));
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot set config flag for HAL 2.0 client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            int halResult = this.mHwSession.setConfigFlag(flag, value);
            com.android.server.broadcastradio.hal2.Convert.throwOnError("setConfigFlag", halResult);
        }
    }

    public java.util.Map<java.lang.String, java.lang.String> setParameters(final java.util.Map<java.lang.String, java.lang.String> parameters) {
        java.util.Map<java.lang.String, java.lang.String> mapVendorInfoFromHal;
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot set parameters for HAL 2.0 client from non-current user");
            return new android.util.ArrayMap();
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            mapVendorInfoFromHal = com.android.server.broadcastradio.hal2.Convert.vendorInfoFromHal((java.util.List) com.android.server.broadcastradio.hal2.Utils.maybeRethrow(new com.android.server.broadcastradio.hal2.Utils.FuncThrowingRemoteException() { // from class: com.android.server.broadcastradio.hal2.TunerSession$$ExternalSyntheticLambda2
                @Override // com.android.server.broadcastradio.hal2.Utils.FuncThrowingRemoteException
                public final java.lang.Object exec() {
                    return this.f$0.lambda$setParameters$3(parameters);
                }
            }));
        }
        return mapVendorInfoFromHal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.util.ArrayList lambda$setParameters$3(java.util.Map parameters) throws android.os.RemoteException {
        return this.mHwSession.setParameters(com.android.server.broadcastradio.hal2.Convert.vendorInfoToHal(parameters));
    }

    public java.util.Map<java.lang.String, java.lang.String> getParameters(final java.util.List<java.lang.String> keys) {
        java.util.Map<java.lang.String, java.lang.String> mapVendorInfoFromHal;
        synchronized (this.mLock) {
            checkNotClosedLocked();
            mapVendorInfoFromHal = com.android.server.broadcastradio.hal2.Convert.vendorInfoFromHal((java.util.List) com.android.server.broadcastradio.hal2.Utils.maybeRethrow(new com.android.server.broadcastradio.hal2.Utils.FuncThrowingRemoteException() { // from class: com.android.server.broadcastradio.hal2.TunerSession$$ExternalSyntheticLambda3
                @Override // com.android.server.broadcastradio.hal2.Utils.FuncThrowingRemoteException
                public final java.lang.Object exec() {
                    return this.f$0.lambda$getParameters$4(keys);
                }
            }));
        }
        return mapVendorInfoFromHal;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.util.ArrayList lambda$getParameters$4(java.util.List keys) throws android.os.RemoteException {
        return this.mHwSession.getParameters(com.android.server.broadcastradio.hal2.Convert.listToArrayList(keys));
    }

    void dumpInfo(android.util.IndentingPrintWriter pw) {
        pw.printf("TunerSession\n", new java.lang.Object[0]);
        pw.increaseIndent();
        pw.printf("HIDL HAL Session: %s\n", new java.lang.Object[]{this.mHwSession});
        synchronized (this.mLock) {
            pw.printf("Is session closed? %s\n", new java.lang.Object[]{this.mIsClosed ? "Yes" : "No"});
            pw.printf("Is muted? %s\n", new java.lang.Object[]{this.mIsMuted ? "Yes" : "No"});
            pw.printf("ProgramInfoCache: %s\n", new java.lang.Object[]{this.mProgramInfoCache});
            pw.printf("Config: %s\n", new java.lang.Object[]{this.mDummyConfig});
        }
        pw.printf("Tuner session events:\n", new java.lang.Object[0]);
        pw.increaseIndent();
        this.mEventLogger.dump(pw);
        pw.decreaseIndent();
        pw.decreaseIndent();
    }
}
