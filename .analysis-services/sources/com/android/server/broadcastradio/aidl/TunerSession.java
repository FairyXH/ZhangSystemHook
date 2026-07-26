package com.android.server.broadcastradio.aidl;

/* JADX INFO: loaded from: classes.dex */
final class TunerSession extends android.hardware.radio.ITuner.Stub {
    private static final java.lang.String TAG = "BcRadioAidlSrv.session";
    private static final int TUNER_EVENT_LOGGER_QUEUE_SIZE = 25;
    final android.hardware.radio.ITunerCallback mCallback;
    private boolean mIsClosed;
    private boolean mIsMuted;
    private final com.android.server.broadcastradio.aidl.RadioModule mModule;
    private android.hardware.radio.RadioManager.BandConfig mPlaceHolderConfig;
    private com.android.server.broadcastradio.aidl.ProgramInfoCache mProgramInfoCache;
    private final android.hardware.broadcastradio.IBroadcastRadio mService;
    private final java.lang.Object mLock = new java.lang.Object();
    final int mUserId = android.os.Binder.getCallingUserHandle().getIdentifier();
    private final int mUid = android.os.Binder.getCallingUid();
    private final com.android.server.broadcastradio.RadioEventLogger mLogger = new com.android.server.broadcastradio.RadioEventLogger(TAG, 25);

    TunerSession(com.android.server.broadcastradio.aidl.RadioModule radioModule, android.hardware.broadcastradio.IBroadcastRadio service, android.hardware.radio.ITunerCallback callback) {
        this.mModule = (com.android.server.broadcastradio.aidl.RadioModule) java.util.Objects.requireNonNull(radioModule, "radioModule cannot be null");
        this.mService = (android.hardware.broadcastradio.IBroadcastRadio) java.util.Objects.requireNonNull(service, "service cannot be null");
        this.mCallback = (android.hardware.radio.ITunerCallback) java.util.Objects.requireNonNull(callback, "callback cannot be null");
    }

    public void close() {
        this.mLogger.logRadioEvent("Close tuner", new java.lang.Object[0]);
        close(null);
    }

    public void close(java.lang.Integer error) {
        if (error == null) {
            this.mLogger.logRadioEvent("Close tuner session on error null", new java.lang.Object[0]);
        } else {
            this.mLogger.logRadioEvent("Close tuner session on error %d", error);
        }
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                return;
            }
            this.mIsClosed = true;
            if (error != null) {
                try {
                    this.mCallback.onError(error.intValue());
                } catch (android.os.RemoteException ex) {
                    com.android.server.utils.Slogf.w(TAG, ex, "mCallback.onError(%s) failed", error);
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
            com.android.server.utils.Slogf.w(TAG, "Cannot set configuration for AIDL HAL client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            this.mPlaceHolderConfig = (android.hardware.radio.RadioManager.BandConfig) java.util.Objects.requireNonNull(config, "config cannot be null");
        }
        com.android.server.utils.Slogf.i(TAG, "Ignoring setConfiguration - not applicable for broadcastradio HAL AIDL");
        this.mModule.fanoutAidlCallback(new com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.aidl.TunerSession$$ExternalSyntheticLambda1
            @Override // com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable
            public final void run(android.hardware.radio.ITunerCallback iTunerCallback, int i) {
                iTunerCallback.onConfigurationChanged(config);
            }
        });
    }

    public android.hardware.radio.RadioManager.BandConfig getConfiguration() {
        android.hardware.radio.RadioManager.BandConfig bandConfig;
        synchronized (this.mLock) {
            checkNotClosedLocked();
            bandConfig = this.mPlaceHolderConfig;
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
            com.android.server.utils.Slogf.w(TAG, "Mute %b via RadioService is not implemented - please handle it via app", java.lang.Boolean.valueOf(mute));
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
        this.mLogger.logRadioEvent("Step with direction %s, skipSubChannel?  %s", directionDown ? android.net.INetd.IF_STATE_DOWN : android.net.INetd.IF_STATE_UP, skipSubChannel ? com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_YES : com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_NO);
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot step on AIDL HAL client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            try {
                this.mService.step(!directionDown);
            } catch (java.lang.RuntimeException ex) {
                throw com.android.server.broadcastradio.aidl.ConversionUtils.throwOnError(ex, "step");
            }
        }
    }

    public void seek(boolean directionDown, boolean skipSubChannel) throws android.os.RemoteException {
        this.mLogger.logRadioEvent("Seek with direction %s, skipSubChannel? %s", directionDown ? android.net.INetd.IF_STATE_DOWN : android.net.INetd.IF_STATE_UP, skipSubChannel ? com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_YES : com.android.server.UiModeManagerService.Shell.NIGHT_MODE_STR_NO);
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot scan on AIDL HAL client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            try {
                this.mService.seek(!directionDown, skipSubChannel);
            } catch (java.lang.RuntimeException ex) {
                throw com.android.server.broadcastradio.aidl.ConversionUtils.throwOnError(ex, "seek");
            }
        }
    }

    public void tune(android.hardware.radio.ProgramSelector selector) throws android.os.RemoteException {
        this.mLogger.logRadioEvent("Tune with selector %s", selector);
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot tune on AIDL HAL client from non-current user");
            return;
        }
        android.hardware.broadcastradio.ProgramSelector hwSel = com.android.server.broadcastradio.aidl.ConversionUtils.programSelectorToHalProgramSelector(selector);
        if (hwSel == null) {
            throw new java.lang.IllegalArgumentException("tune: INVALID_ARGUMENTS for program selector");
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            try {
                this.mService.tune(hwSel);
            } catch (java.lang.RuntimeException ex) {
                throw com.android.server.broadcastradio.aidl.ConversionUtils.throwOnError(ex, "tune");
            }
        }
    }

    public void cancel() {
        com.android.server.utils.Slogf.i(TAG, "Cancel");
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot cancel on AIDL HAL client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            try {
                this.mService.cancel();
            } catch (android.os.RemoteException ex) {
                com.android.server.utils.Slogf.e(TAG, "Failed to cancel tuner session");
                throw ex.rethrowFromSystemServer();
            }
        }
    }

    public void cancelAnnouncement() {
        com.android.server.utils.Slogf.w(TAG, "Announcements control doesn't involve cancelling at the HAL level in AIDL");
    }

    public android.graphics.Bitmap getImage(int id) {
        this.mLogger.logRadioEvent("Get image for %d", java.lang.Integer.valueOf(id));
        return this.mModule.getImage(id);
    }

    public boolean startBackgroundScan() {
        com.android.server.utils.Slogf.w(TAG, "Explicit background scan trigger is not supported with HAL AIDL");
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot start background scan on AIDL HAL client from non-current user");
            return false;
        }
        this.mModule.fanoutAidlCallback(new com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.aidl.TunerSession$$ExternalSyntheticLambda0
            @Override // com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable
            public final void run(android.hardware.radio.ITunerCallback iTunerCallback, int i) {
                iTunerCallback.onBackgroundScanComplete();
            }
        });
        return true;
    }

    public void startProgramListUpdates(android.hardware.radio.ProgramList.Filter filter) throws android.os.RemoteException {
        this.mLogger.logRadioEvent("Start programList updates %s", filter);
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot start program list updates on AIDL HAL client from non-current user");
            return;
        }
        if (filter == null) {
            filter = new android.hardware.radio.ProgramList.Filter(new android.util.ArraySet(), new android.util.ArraySet(), true, false);
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            this.mProgramInfoCache = new com.android.server.broadcastradio.aidl.ProgramInfoCache(filter);
        }
        this.mModule.onTunerSessionProgramListFilterChanged(this);
    }

    int getUid() {
        return this.mUid;
    }

    android.hardware.radio.ProgramList.Filter getProgramListFilter() {
        android.hardware.radio.ProgramList.Filter filter;
        synchronized (this.mLock) {
            filter = this.mProgramInfoCache == null ? null : this.mProgramInfoCache.getFilter();
        }
        return filter;
    }

    void onMergedProgramListUpdateFromHal(android.hardware.broadcastradio.ProgramListChunk mergedChunk) {
        synchronized (this.mLock) {
            if (this.mProgramInfoCache == null) {
                return;
            }
            java.util.List<android.hardware.radio.ProgramList.Chunk> clientUpdateChunks = this.mProgramInfoCache.filterAndApplyChunk(mergedChunk);
            dispatchClientUpdateChunks(clientUpdateChunks);
        }
    }

    void updateProgramInfoFromHalCache(com.android.server.broadcastradio.aidl.ProgramInfoCache halCache) {
        synchronized (this.mLock) {
            if (this.mProgramInfoCache == null) {
                return;
            }
            java.util.List<android.hardware.radio.ProgramList.Chunk> clientUpdateChunks = this.mProgramInfoCache.filterAndUpdateFromInternal(halCache, true);
            dispatchClientUpdateChunks(clientUpdateChunks);
        }
    }

    private void dispatchClientUpdateChunks(java.util.List<android.hardware.radio.ProgramList.Chunk> chunks) {
        if (chunks == null) {
            return;
        }
        for (int i = 0; i < chunks.size(); i++) {
            try {
                if (!com.android.server.broadcastradio.aidl.ConversionUtils.isAtLeastU(getUid())) {
                    android.hardware.radio.ProgramList.Chunk downgradedChunk = com.android.server.broadcastradio.aidl.ConversionUtils.convertChunkToTargetSdkVersion(chunks.get(i), getUid());
                    this.mCallback.onProgramListUpdated(downgradedChunk);
                } else {
                    this.mCallback.onProgramListUpdated(chunks.get(i));
                }
            } catch (android.os.RemoteException ex) {
                com.android.server.utils.Slogf.w(TAG, ex, "mCallback.onProgramListUpdated() failed", new java.lang.Object[0]);
            }
        }
    }

    public void stopProgramListUpdates() throws android.os.RemoteException {
        this.mLogger.logRadioEvent("Stop programList updates", new java.lang.Object[0]);
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot stop program list updates on AIDL HAL client from non-current user");
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
        boolean zIsConfigFlagSet;
        this.mLogger.logRadioEvent("is ConfigFlag %s set? ", android.hardware.broadcastradio.ConfigFlag$$.toString(flag));
        synchronized (this.mLock) {
            checkNotClosedLocked();
            try {
                try {
                    zIsConfigFlagSet = this.mService.isConfigFlagSet(flag);
                } catch (android.os.RemoteException ex) {
                    throw new java.lang.RuntimeException("Failed to check flag " + android.hardware.broadcastradio.ConfigFlag$$.toString(flag), ex);
                }
            } catch (java.lang.RuntimeException ex2) {
                throw com.android.server.broadcastradio.aidl.ConversionUtils.throwOnError(ex2, "isConfigFlagSet");
            }
        }
        return zIsConfigFlagSet;
    }

    public void setConfigFlag(int flag, boolean value) throws android.os.RemoteException {
        this.mLogger.logRadioEvent("set ConfigFlag %s to %b ", android.hardware.broadcastradio.ConfigFlag$$.toString(flag), java.lang.Boolean.valueOf(value));
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot set config flag for AIDL HAL client from non-current user");
            return;
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            try {
                this.mService.setConfigFlag(flag, value);
            } catch (java.lang.RuntimeException ex) {
                throw com.android.server.broadcastradio.aidl.ConversionUtils.throwOnError(ex, "setConfigFlag");
            }
        }
    }

    public java.util.Map<java.lang.String, java.lang.String> setParameters(java.util.Map<java.lang.String, java.lang.String> parameters) {
        java.util.Map<java.lang.String, java.lang.String> mapVendorInfoFromHalVendorKeyValues;
        this.mLogger.logRadioEvent("Set parameters ", new java.lang.Object[0]);
        if (!com.android.server.broadcastradio.RadioServiceUserController.isCurrentOrSystemUser()) {
            com.android.server.utils.Slogf.w(TAG, "Cannot set parameters for AIDL HAL client from non-current user");
            return new android.util.ArrayMap();
        }
        synchronized (this.mLock) {
            checkNotClosedLocked();
            try {
                mapVendorInfoFromHalVendorKeyValues = com.android.server.broadcastradio.aidl.ConversionUtils.vendorInfoFromHalVendorKeyValues(this.mService.setParameters(com.android.server.broadcastradio.aidl.ConversionUtils.vendorInfoToHalVendorKeyValues(parameters)));
            } catch (android.os.RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
        return mapVendorInfoFromHalVendorKeyValues;
    }

    public java.util.Map<java.lang.String, java.lang.String> getParameters(java.util.List<java.lang.String> keys) {
        java.util.Map<java.lang.String, java.lang.String> mapVendorInfoFromHalVendorKeyValues;
        this.mLogger.logRadioEvent("Get parameters ", new java.lang.Object[0]);
        synchronized (this.mLock) {
            checkNotClosedLocked();
            try {
                mapVendorInfoFromHalVendorKeyValues = com.android.server.broadcastradio.aidl.ConversionUtils.vendorInfoFromHalVendorKeyValues(this.mService.getParameters((java.lang.String[]) keys.toArray(new java.lang.String[0])));
            } catch (android.os.RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
        return mapVendorInfoFromHalVendorKeyValues;
    }

    void dumpInfo(android.util.IndentingPrintWriter pw) {
        pw.printf("TunerSession\n", new java.lang.Object[0]);
        pw.increaseIndent();
        synchronized (this.mLock) {
            pw.printf("Is session closed? %s\n", new java.lang.Object[]{this.mIsClosed ? "Yes" : "No"});
            pw.printf("Is muted? %s\n", new java.lang.Object[]{this.mIsMuted ? "Yes" : "No"});
            pw.printf("ProgramInfoCache: %s\n", new java.lang.Object[]{this.mProgramInfoCache});
            pw.printf("Config: %s\n", new java.lang.Object[]{this.mPlaceHolderConfig});
        }
        pw.printf("Tuner session events:\n", new java.lang.Object[0]);
        pw.increaseIndent();
        this.mLogger.dump(pw);
        pw.decreaseIndent();
        pw.decreaseIndent();
    }
}
