package com.android.server.broadcastradio.hal2;

/* JADX INFO: loaded from: classes.dex */
final class RadioModule {
    private static final int RADIO_EVENT_LOGGER_QUEUE_SIZE = 25;
    private static final java.lang.String TAG = "BcRadio2Srv.module";
    private android.hardware.broadcastradio.V2_0.ITunerSession mHalTunerSession;
    private final android.hardware.radio.RadioManager.ModuleProperties mProperties;
    private final android.hardware.broadcastradio.V2_0.IBroadcastRadio mService;
    private final java.lang.Object mLock = new java.lang.Object();
    private java.lang.Boolean mAntennaConnected = null;
    private android.hardware.radio.RadioManager.ProgramInfo mCurrentProgramInfo = null;
    private final com.android.server.broadcastradio.hal2.ProgramInfoCache mProgramInfoCache = new com.android.server.broadcastradio.hal2.ProgramInfoCache(null);
    private android.hardware.radio.ProgramList.Filter mUnionOfAidlProgramFilters = null;
    private final android.hardware.broadcastradio.V2_0.ITunerCallback mHalTunerCallback = new com.android.server.broadcastradio.hal2.RadioModule.AnonymousClass1();
    private final java.util.Set<com.android.server.broadcastradio.hal2.TunerSession> mAidlTunerSessions = new android.util.ArraySet();
    private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final com.android.server.broadcastradio.RadioEventLogger mEventLogger = new com.android.server.broadcastradio.RadioEventLogger(TAG, 25);

    interface AidlCallbackRunnable {
        void run(android.hardware.radio.ITunerCallback iTunerCallback) throws android.os.RemoteException;
    }

    /* JADX INFO: renamed from: com.android.server.broadcastradio.hal2.RadioModule$1, reason: invalid class name */
    class AnonymousClass1 extends android.hardware.broadcastradio.V2_0.ITunerCallback.Stub {
        AnonymousClass1() {
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerCallback
        public void onTuneFailed(final int result, final android.hardware.broadcastradio.V2_0.ProgramSelector programSelector) {
            com.android.server.broadcastradio.hal2.RadioModule.this.fireLater(new java.lang.Runnable() { // from class: com.android.server.broadcastradio.hal2.RadioModule$1$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onTuneFailed$1(programSelector, result);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTuneFailed$1(android.hardware.broadcastradio.V2_0.ProgramSelector programSelector, int result) {
            final android.hardware.radio.ProgramSelector csel = com.android.server.broadcastradio.hal2.Convert.programSelectorFromHal(programSelector);
            final int tunerResult = com.android.server.broadcastradio.hal2.Convert.halResultToTunerResult(result);
            synchronized (com.android.server.broadcastradio.hal2.RadioModule.this.mLock) {
                com.android.server.broadcastradio.hal2.RadioModule.this.fanoutAidlCallbackLocked(new com.android.server.broadcastradio.hal2.RadioModule.AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.hal2.RadioModule$1$$ExternalSyntheticLambda0
                    @Override // com.android.server.broadcastradio.hal2.RadioModule.AidlCallbackRunnable
                    public final void run(android.hardware.radio.ITunerCallback iTunerCallback) {
                        iTunerCallback.onTuneFailed(tunerResult, csel);
                    }
                });
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerCallback
        public void onCurrentProgramInfoChanged(final android.hardware.broadcastradio.V2_0.ProgramInfo halProgramInfo) {
            com.android.server.broadcastradio.hal2.RadioModule.this.fireLater(new java.lang.Runnable() { // from class: com.android.server.broadcastradio.hal2.RadioModule$1$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCurrentProgramInfoChanged$3(halProgramInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCurrentProgramInfoChanged$3(android.hardware.broadcastradio.V2_0.ProgramInfo halProgramInfo) {
            synchronized (com.android.server.broadcastradio.hal2.RadioModule.this.mLock) {
                com.android.server.broadcastradio.hal2.RadioModule.this.mCurrentProgramInfo = com.android.server.broadcastradio.hal2.Convert.programInfoFromHal(halProgramInfo);
                final android.hardware.radio.RadioManager.ProgramInfo currentProgramInfo = com.android.server.broadcastradio.hal2.RadioModule.this.mCurrentProgramInfo;
                com.android.server.broadcastradio.hal2.RadioModule.this.fanoutAidlCallbackLocked(new com.android.server.broadcastradio.hal2.RadioModule.AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.hal2.RadioModule$1$$ExternalSyntheticLambda7
                    @Override // com.android.server.broadcastradio.hal2.RadioModule.AidlCallbackRunnable
                    public final void run(android.hardware.radio.ITunerCallback iTunerCallback) {
                        iTunerCallback.onCurrentProgramInfoChanged(currentProgramInfo);
                    }
                });
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerCallback
        public void onProgramListUpdated(final android.hardware.broadcastradio.V2_0.ProgramListChunk programListChunk) {
            com.android.server.broadcastradio.hal2.RadioModule.this.fireLater(new java.lang.Runnable() { // from class: com.android.server.broadcastradio.hal2.RadioModule$1$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onProgramListUpdated$4(programListChunk);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onProgramListUpdated$4(android.hardware.broadcastradio.V2_0.ProgramListChunk programListChunk) {
            synchronized (com.android.server.broadcastradio.hal2.RadioModule.this.mLock) {
                com.android.server.broadcastradio.hal2.RadioModule.this.mProgramInfoCache.filterAndApplyChunk(programListChunk);
                for (com.android.server.broadcastradio.hal2.TunerSession tunerSession : com.android.server.broadcastradio.hal2.RadioModule.this.mAidlTunerSessions) {
                    tunerSession.onMergedProgramListUpdateFromHal(programListChunk);
                }
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerCallback
        public void onAntennaStateChange(final boolean connected) {
            com.android.server.broadcastradio.hal2.RadioModule.this.fireLater(new java.lang.Runnable() { // from class: com.android.server.broadcastradio.hal2.RadioModule$1$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onAntennaStateChange$6(connected);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAntennaStateChange$6(final boolean connected) {
            synchronized (com.android.server.broadcastradio.hal2.RadioModule.this.mLock) {
                com.android.server.broadcastradio.hal2.RadioModule.this.mAntennaConnected = java.lang.Boolean.valueOf(connected);
                com.android.server.broadcastradio.hal2.RadioModule.this.fanoutAidlCallbackLocked(new com.android.server.broadcastradio.hal2.RadioModule.AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.hal2.RadioModule$1$$ExternalSyntheticLambda5
                    @Override // com.android.server.broadcastradio.hal2.RadioModule.AidlCallbackRunnable
                    public final void run(android.hardware.radio.ITunerCallback iTunerCallback) {
                        iTunerCallback.onAntennaState(connected);
                    }
                });
            }
        }

        @Override // android.hardware.broadcastradio.V2_0.ITunerCallback
        public void onParametersUpdated(final java.util.ArrayList<android.hardware.broadcastradio.V2_0.VendorKeyValue> parameters) {
            com.android.server.broadcastradio.hal2.RadioModule.this.fireLater(new java.lang.Runnable() { // from class: com.android.server.broadcastradio.hal2.RadioModule$1$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onParametersUpdated$8(parameters);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onParametersUpdated$8(java.util.ArrayList parameters) {
            final java.util.Map<java.lang.String, java.lang.String> cparam = com.android.server.broadcastradio.hal2.Convert.vendorInfoFromHal(parameters);
            synchronized (com.android.server.broadcastradio.hal2.RadioModule.this.mLock) {
                com.android.server.broadcastradio.hal2.RadioModule.this.fanoutAidlCallbackLocked(new com.android.server.broadcastradio.hal2.RadioModule.AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.hal2.RadioModule$1$$ExternalSyntheticLambda1
                    @Override // com.android.server.broadcastradio.hal2.RadioModule.AidlCallbackRunnable
                    public final void run(android.hardware.radio.ITunerCallback iTunerCallback) {
                        iTunerCallback.onParametersUpdated(cparam);
                    }
                });
            }
        }
    }

    RadioModule(android.hardware.broadcastradio.V2_0.IBroadcastRadio service, android.hardware.radio.RadioManager.ModuleProperties properties) {
        this.mProperties = (android.hardware.radio.RadioManager.ModuleProperties) java.util.Objects.requireNonNull(properties);
        this.mService = (android.hardware.broadcastradio.V2_0.IBroadcastRadio) java.util.Objects.requireNonNull(service);
    }

    static com.android.server.broadcastradio.hal2.RadioModule tryLoadingModule(int idx, java.lang.String fqName) {
        try {
            com.android.server.utils.Slogf.i(TAG, "Try loading module for idx " + idx + ", fqName " + fqName);
            android.hardware.broadcastradio.V2_0.IBroadcastRadio service = android.hardware.broadcastradio.V2_0.IBroadcastRadio.getService(fqName);
            if (service == null) {
                com.android.server.utils.Slogf.w(TAG, "No service found for fqName " + fqName);
                return null;
            }
            final com.android.server.broadcastradio.hal2.Mutable<android.hardware.broadcastradio.V2_0.AmFmRegionConfig> amfmConfig = new com.android.server.broadcastradio.hal2.Mutable<>();
            service.getAmFmRegionConfig(false, new android.hardware.broadcastradio.V2_0.IBroadcastRadio.getAmFmRegionConfigCallback() { // from class: com.android.server.broadcastradio.hal2.RadioModule$$ExternalSyntheticLambda4
                @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio.getAmFmRegionConfigCallback
                public final void onValues(int i, android.hardware.broadcastradio.V2_0.AmFmRegionConfig amFmRegionConfig) {
                    com.android.server.broadcastradio.hal2.RadioModule.lambda$tryLoadingModule$0(amfmConfig, i, amFmRegionConfig);
                }
            });
            final com.android.server.broadcastradio.hal2.Mutable<java.util.List<android.hardware.broadcastradio.V2_0.DabTableEntry>> dabConfig = new com.android.server.broadcastradio.hal2.Mutable<>();
            service.getDabRegionConfig(new android.hardware.broadcastradio.V2_0.IBroadcastRadio.getDabRegionConfigCallback() { // from class: com.android.server.broadcastradio.hal2.RadioModule$$ExternalSyntheticLambda5
                @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio.getDabRegionConfigCallback
                public final void onValues(int i, java.util.ArrayList arrayList) {
                    com.android.server.broadcastradio.hal2.RadioModule.lambda$tryLoadingModule$1(dabConfig, i, arrayList);
                }
            });
            android.hardware.radio.RadioManager.ModuleProperties prop = com.android.server.broadcastradio.hal2.Convert.propertiesFromHal(idx, fqName, service.getProperties(), amfmConfig.value, dabConfig.value);
            return new com.android.server.broadcastradio.hal2.RadioModule(service, prop);
        } catch (android.os.RemoteException ex) {
            com.android.server.utils.Slogf.e(TAG, "Failed to load module " + fqName, ex);
            return null;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ void lambda$tryLoadingModule$0(com.android.server.broadcastradio.hal2.Mutable amfmConfig, int result, android.hardware.broadcastradio.V2_0.AmFmRegionConfig amFmRegionConfig) {
        if (result == 0) {
            amfmConfig.value = amFmRegionConfig;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ void lambda$tryLoadingModule$1(com.android.server.broadcastradio.hal2.Mutable dabConfig, int result, java.util.ArrayList arrayList) {
        if (result == 0) {
            dabConfig.value = arrayList;
        }
    }

    android.hardware.broadcastradio.V2_0.IBroadcastRadio getService() {
        return this.mService;
    }

    public android.hardware.radio.RadioManager.ModuleProperties getProperties() {
        return this.mProperties;
    }

    com.android.server.broadcastradio.hal2.TunerSession openSession(android.hardware.radio.ITunerCallback userCb) throws android.os.RemoteException {
        com.android.server.broadcastradio.hal2.TunerSession tunerSession;
        this.mEventLogger.logRadioEvent("Open TunerSession", new java.lang.Object[0]);
        synchronized (this.mLock) {
            if (this.mHalTunerSession == null) {
                final com.android.server.broadcastradio.hal2.Mutable<android.hardware.broadcastradio.V2_0.ITunerSession> hwSession = new com.android.server.broadcastradio.hal2.Mutable<>();
                this.mService.openSession(this.mHalTunerCallback, new android.hardware.broadcastradio.V2_0.IBroadcastRadio.openSessionCallback() { // from class: com.android.server.broadcastradio.hal2.RadioModule$$ExternalSyntheticLambda6
                    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
                    @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio.openSessionCallback
                    public final void onValues(int i, android.hardware.broadcastradio.V2_0.ITunerSession iTunerSession) throws android.os.ParcelableException {
                        this.f$0.lambda$openSession$2(hwSession, i, iTunerSession);
                    }
                });
                this.mHalTunerSession = (android.hardware.broadcastradio.V2_0.ITunerSession) java.util.Objects.requireNonNull(hwSession.value);
            }
            tunerSession = new com.android.server.broadcastradio.hal2.TunerSession(this, this.mHalTunerSession, userCb);
            this.mAidlTunerSessions.add(tunerSession);
            if (this.mAntennaConnected != null) {
                userCb.onAntennaState(this.mAntennaConnected.booleanValue());
            }
            if (this.mCurrentProgramInfo != null) {
                userCb.onCurrentProgramInfoChanged(this.mCurrentProgramInfo);
            }
        }
        return tunerSession;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$openSession$2(com.android.server.broadcastradio.hal2.Mutable hwSession, int result, android.hardware.broadcastradio.V2_0.ITunerSession iTunerSession) throws android.os.ParcelableException {
        com.android.server.broadcastradio.hal2.Convert.throwOnError("openSession", result);
        hwSession.value = iTunerSession;
        this.mEventLogger.logRadioEvent("New HIDL 2.0 tuner session is opened", new java.lang.Object[0]);
    }

    void closeSessions(java.lang.Integer error) {
        com.android.server.broadcastradio.hal2.TunerSession[] tunerSessions;
        this.mEventLogger.logRadioEvent("Close TunerSessions", new java.lang.Object[0]);
        synchronized (this.mLock) {
            tunerSessions = new com.android.server.broadcastradio.hal2.TunerSession[this.mAidlTunerSessions.size()];
            this.mAidlTunerSessions.toArray(tunerSessions);
            this.mAidlTunerSessions.clear();
        }
        for (com.android.server.broadcastradio.hal2.TunerSession tunerSession : tunerSessions) {
            tunerSession.close(error);
        }
    }

    private android.hardware.radio.ProgramList.Filter buildUnionOfTunerSessionFiltersLocked() {
        java.util.Set<java.lang.Integer> idTypes = null;
        java.util.Set<android.hardware.radio.ProgramSelector.Identifier> ids = null;
        boolean includeCategories = false;
        boolean excludeModifications = true;
        for (com.android.server.broadcastradio.hal2.TunerSession tunerSession : this.mAidlTunerSessions) {
            android.hardware.radio.ProgramList.Filter filter = tunerSession.getProgramListFilter();
            if (filter != null) {
                if (idTypes == null) {
                    idTypes = new android.util.ArraySet<>(filter.getIdentifierTypes());
                    ids = new android.util.ArraySet<>(filter.getIdentifiers());
                    includeCategories = filter.areCategoriesIncluded();
                    excludeModifications = filter.areModificationsExcluded();
                } else {
                    if (!idTypes.isEmpty()) {
                        if (filter.getIdentifierTypes().isEmpty()) {
                            idTypes.clear();
                        } else {
                            idTypes.addAll(filter.getIdentifierTypes());
                        }
                    }
                    if (!ids.isEmpty()) {
                        if (filter.getIdentifiers().isEmpty()) {
                            ids.clear();
                        } else {
                            ids.addAll(filter.getIdentifiers());
                        }
                    }
                    includeCategories |= filter.areCategoriesIncluded();
                    excludeModifications &= filter.areModificationsExcluded();
                }
            }
        }
        if (idTypes == null) {
            return null;
        }
        return new android.hardware.radio.ProgramList.Filter(idTypes, ids, includeCategories, excludeModifications);
    }

    void onTunerSessionProgramListFilterChanged(com.android.server.broadcastradio.hal2.TunerSession session) {
        synchronized (this.mLock) {
            onTunerSessionProgramListFilterChangedLocked(session);
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    private void onTunerSessionProgramListFilterChangedLocked(com.android.server.broadcastradio.hal2.TunerSession session) throws android.os.ParcelableException {
        android.hardware.radio.ProgramList.Filter newFilter = buildUnionOfTunerSessionFiltersLocked();
        if (newFilter == null) {
            if (this.mUnionOfAidlProgramFilters == null) {
                return;
            }
            this.mUnionOfAidlProgramFilters = null;
            try {
                this.mHalTunerSession.stopProgramListUpdates();
                return;
            } catch (android.os.RemoteException ex) {
                com.android.server.utils.Slogf.e(TAG, "mHalTunerSession.stopProgramListUpdates() failed: ", ex);
                return;
            }
        }
        if (newFilter.equals(this.mUnionOfAidlProgramFilters)) {
            if (session != null) {
                session.updateProgramInfoFromHalCache(this.mProgramInfoCache);
            }
        } else {
            this.mUnionOfAidlProgramFilters = newFilter;
            try {
                int halResult = this.mHalTunerSession.startProgramListUpdates(com.android.server.broadcastradio.hal2.Convert.programFilterToHal(newFilter));
                com.android.server.broadcastradio.hal2.Convert.throwOnError("startProgramListUpdates", halResult);
            } catch (android.os.RemoteException ex2) {
                com.android.server.utils.Slogf.e(TAG, "mHalTunerSession.startProgramListUpdates() failed: ", ex2);
            }
        }
    }

    void onTunerSessionClosed(com.android.server.broadcastradio.hal2.TunerSession tunerSession) {
        synchronized (this.mLock) {
            onTunerSessionsClosedLocked(tunerSession);
        }
    }

    private void onTunerSessionsClosedLocked(com.android.server.broadcastradio.hal2.TunerSession... tunerSessions) {
        for (com.android.server.broadcastradio.hal2.TunerSession tunerSession : tunerSessions) {
            this.mAidlTunerSessions.remove(tunerSession);
        }
        onTunerSessionProgramListFilterChanged(null);
        if (this.mAidlTunerSessions.isEmpty() && this.mHalTunerSession != null) {
            this.mEventLogger.logRadioEvent("Closing HAL tuner session", new java.lang.Object[0]);
            try {
                this.mHalTunerSession.close();
            } catch (android.os.RemoteException ex) {
                com.android.server.utils.Slogf.e(TAG, "mHalTunerSession.close() failed: ", ex);
            }
            this.mHalTunerSession = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireLater(final java.lang.Runnable r) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.broadcastradio.hal2.RadioModule$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                r.run();
            }
        });
    }

    void fanoutAidlCallback(final com.android.server.broadcastradio.hal2.RadioModule.AidlCallbackRunnable runnable) {
        fireLater(new java.lang.Runnable() { // from class: com.android.server.broadcastradio.hal2.RadioModule$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$fanoutAidlCallback$4(runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fanoutAidlCallback$4(com.android.server.broadcastradio.hal2.RadioModule.AidlCallbackRunnable runnable) {
        synchronized (this.mLock) {
            fanoutAidlCallbackLocked(runnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fanoutAidlCallbackLocked(com.android.server.broadcastradio.hal2.RadioModule.AidlCallbackRunnable runnable) {
        int currentUserId = com.android.server.broadcastradio.RadioServiceUserController.getCurrentUser();
        java.util.List<com.android.server.broadcastradio.hal2.TunerSession> deadSessions = null;
        for (com.android.server.broadcastradio.hal2.TunerSession tunerSession : this.mAidlTunerSessions) {
            if (tunerSession.mUserId == currentUserId || tunerSession.mUserId == 0) {
                try {
                    runnable.run(tunerSession.mCallback);
                } catch (android.os.DeadObjectException e) {
                    com.android.server.utils.Slogf.e(TAG, "Removing dead TunerSession");
                    if (deadSessions == null) {
                        deadSessions = new java.util.ArrayList<>();
                    }
                    deadSessions.add(tunerSession);
                } catch (android.os.RemoteException ex) {
                    com.android.server.utils.Slogf.e(TAG, "Failed to invoke ITunerCallback: ", ex);
                }
            }
        }
        if (deadSessions != null) {
            onTunerSessionsClosedLocked((com.android.server.broadcastradio.hal2.TunerSession[]) deadSessions.toArray(new com.android.server.broadcastradio.hal2.TunerSession[0]));
        }
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: android.os.ParcelableException */
    android.hardware.radio.ICloseHandle addAnnouncementListener(int[] enabledTypes, android.hardware.radio.IAnnouncementListener listener) throws android.os.RemoteException, android.os.ParcelableException {
        this.mEventLogger.logRadioEvent("Add AnnouncementListener", new java.lang.Object[0]);
        java.util.ArrayList<java.lang.Byte> enabledList = new java.util.ArrayList<>();
        for (int type : enabledTypes) {
            enabledList.add(java.lang.Byte.valueOf((byte) type));
        }
        final android.util.MutableInt halResult = new android.util.MutableInt(1);
        final com.android.server.broadcastradio.hal2.Mutable<android.hardware.broadcastradio.V2_0.ICloseHandle> hwCloseHandle = new com.android.server.broadcastradio.hal2.Mutable<>();
        android.hardware.broadcastradio.V2_0.IAnnouncementListener hwListener = new com.android.server.broadcastradio.hal2.RadioModule.AnonymousClass2(listener);
        this.mService.registerAnnouncementListener(enabledList, hwListener, new android.hardware.broadcastradio.V2_0.IBroadcastRadio.registerAnnouncementListenerCallback() { // from class: com.android.server.broadcastradio.hal2.RadioModule$$ExternalSyntheticLambda3
            @Override // android.hardware.broadcastradio.V2_0.IBroadcastRadio.registerAnnouncementListenerCallback
            public final void onValues(int i, android.hardware.broadcastradio.V2_0.ICloseHandle iCloseHandle) {
                com.android.server.broadcastradio.hal2.RadioModule.lambda$addAnnouncementListener$5(halResult, hwCloseHandle, i, iCloseHandle);
            }
        });
        com.android.server.broadcastradio.hal2.Convert.throwOnError("addAnnouncementListener", halResult.value);
        return new android.hardware.radio.ICloseHandle.Stub() { // from class: com.android.server.broadcastradio.hal2.RadioModule.3
            public void close() {
                try {
                    ((android.hardware.broadcastradio.V2_0.ICloseHandle) hwCloseHandle.value).close();
                } catch (android.os.RemoteException ex) {
                    com.android.server.utils.Slogf.e(com.android.server.broadcastradio.hal2.RadioModule.TAG, "Failed closing announcement listener", ex);
                }
                hwCloseHandle.value = null;
            }
        };
    }

    /* JADX INFO: renamed from: com.android.server.broadcastradio.hal2.RadioModule$2, reason: invalid class name */
    class AnonymousClass2 extends android.hardware.broadcastradio.V2_0.IAnnouncementListener.Stub {
        final /* synthetic */ android.hardware.radio.IAnnouncementListener val$listener;

        AnonymousClass2(android.hardware.radio.IAnnouncementListener iAnnouncementListener) {
            this.val$listener = iAnnouncementListener;
        }

        @Override // android.hardware.broadcastradio.V2_0.IAnnouncementListener
        public void onListUpdated(java.util.ArrayList<android.hardware.broadcastradio.V2_0.Announcement> hwAnnouncements) throws android.os.RemoteException {
            this.val$listener.onListUpdated((java.util.List) hwAnnouncements.stream().map(new java.util.function.Function() { // from class: com.android.server.broadcastradio.hal2.RadioModule$2$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.broadcastradio.hal2.Convert.announcementFromHal((android.hardware.broadcastradio.V2_0.Announcement) obj);
                }
            }).collect(java.util.stream.Collectors.toList()));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    static /* synthetic */ void lambda$addAnnouncementListener$5(android.util.MutableInt halResult, com.android.server.broadcastradio.hal2.Mutable hwCloseHandle, int result, android.hardware.broadcastradio.V2_0.ICloseHandle iCloseHandle) {
        halResult.value = result;
        hwCloseHandle.value = iCloseHandle;
    }

    android.graphics.Bitmap getImage(final int id) {
        this.mEventLogger.logRadioEvent("Get image for id %d", java.lang.Integer.valueOf(id));
        if (id == 0) {
            throw new java.lang.IllegalArgumentException("Image ID is missing");
        }
        java.util.List<java.lang.Byte> rawList = (java.util.List) com.android.server.broadcastradio.hal2.Utils.maybeRethrow(new com.android.server.broadcastradio.hal2.Utils.FuncThrowingRemoteException() { // from class: com.android.server.broadcastradio.hal2.RadioModule$$ExternalSyntheticLambda0
            @Override // com.android.server.broadcastradio.hal2.Utils.FuncThrowingRemoteException
            public final java.lang.Object exec() {
                return this.f$0.lambda$getImage$6(id);
            }
        });
        byte[] rawImage = new byte[rawList.size()];
        for (int i = 0; i < rawList.size(); i++) {
            rawImage[i] = rawList.get(i).byteValue();
        }
        int i2 = rawImage.length;
        if (i2 == 0) {
            return null;
        }
        return android.graphics.BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.util.ArrayList lambda$getImage$6(int id) throws android.os.RemoteException {
        return this.mService.getImage(id);
    }

    void dumpInfo(android.util.IndentingPrintWriter pw) {
        pw.printf("RadioModule\n", new java.lang.Object[0]);
        pw.increaseIndent();
        pw.printf("BroadcastRadioService: %s\n", new java.lang.Object[]{this.mService});
        pw.printf("Properties: %s\n", new java.lang.Object[]{this.mProperties});
        synchronized (this.mLock) {
            pw.printf("HIDL 2.0 HAL TunerSession: %s\n", new java.lang.Object[]{this.mHalTunerSession});
            pw.printf("Is antenna connected? ", new java.lang.Object[0]);
            if (this.mAntennaConnected == null) {
                pw.printf("null\n", new java.lang.Object[0]);
            } else {
                pw.printf("%s\n", new java.lang.Object[]{this.mAntennaConnected.booleanValue() ? "Yes" : "No"});
            }
            pw.printf("Current ProgramInfo: %s\n", new java.lang.Object[]{this.mCurrentProgramInfo});
            pw.printf("ProgramInfoCache: %s\n", new java.lang.Object[]{this.mProgramInfoCache});
            pw.printf("Union of AIDL ProgramFilters: %s\n", new java.lang.Object[]{this.mUnionOfAidlProgramFilters});
            pw.printf("AIDL TunerSessions:\n", new java.lang.Object[0]);
            pw.increaseIndent();
            for (com.android.server.broadcastradio.hal2.TunerSession aidlTunerSession : this.mAidlTunerSessions) {
                aidlTunerSession.dumpInfo(pw);
            }
            pw.decreaseIndent();
        }
        pw.printf("Radio module events:\n", new java.lang.Object[0]);
        pw.increaseIndent();
        this.mEventLogger.dump(pw);
        pw.decreaseIndent();
        pw.decreaseIndent();
    }
}
