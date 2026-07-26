package com.android.server.broadcastradio.aidl;

/* JADX INFO: loaded from: classes.dex */
final class RadioModule {
    private static final int RADIO_EVENT_LOGGER_QUEUE_SIZE = 25;
    private static final java.lang.String TAG = "BcRadioAidlSrv.module";
    private java.lang.Boolean mAntennaConnected;
    private android.hardware.radio.RadioManager.ProgramInfo mCurrentProgramInfo;
    private final android.hardware.radio.RadioManager.ModuleProperties mProperties;
    private final android.hardware.broadcastradio.IBroadcastRadio mService;
    private android.hardware.radio.ProgramList.Filter mUnionOfAidlProgramFilters;
    private final java.lang.Object mLock = new java.lang.Object();
    private final com.android.server.broadcastradio.aidl.ProgramInfoCache mProgramInfoCache = new com.android.server.broadcastradio.aidl.ProgramInfoCache(null);
    private final android.util.ArraySet<com.android.server.broadcastradio.aidl.TunerSession> mAidlTunerSessions = new android.util.ArraySet<>();
    private final android.hardware.broadcastradio.ITunerCallback mHalTunerCallback = new com.android.server.broadcastradio.aidl.RadioModule.AnonymousClass1();
    private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private final com.android.server.broadcastradio.RadioEventLogger mLogger = new com.android.server.broadcastradio.RadioEventLogger(TAG, 25);

    interface AidlCallbackRunnable {
        void run(android.hardware.radio.ITunerCallback iTunerCallback, int i) throws android.os.RemoteException;
    }

    /* JADX INFO: renamed from: com.android.server.broadcastradio.aidl.RadioModule$1, reason: invalid class name */
    class AnonymousClass1 extends android.hardware.broadcastradio.ITunerCallback.Stub {
        AnonymousClass1() {
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public int getInterfaceVersion() {
            return 2;
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public java.lang.String getInterfaceHash() {
            return "bff68a8bc8b7cc191ab62bee10f7df8e79494467";
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onTuneFailed(final int result, final android.hardware.broadcastradio.ProgramSelector programSelector) {
            com.android.server.broadcastradio.aidl.RadioModule.this.fireLater(new java.lang.Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onTuneFailed$1(programSelector, result);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTuneFailed$1(android.hardware.broadcastradio.ProgramSelector programSelector, int result) {
            final android.hardware.radio.ProgramSelector csel = com.android.server.broadcastradio.aidl.ConversionUtils.programSelectorFromHalProgramSelector(programSelector);
            final int tunerResult = com.android.server.broadcastradio.aidl.ConversionUtils.halResultToTunerResult(result);
            synchronized (com.android.server.broadcastradio.aidl.RadioModule.this.mLock) {
                com.android.server.broadcastradio.aidl.RadioModule.this.fanoutAidlCallbackLocked(new com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda8
                    @Override // com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable
                    public final void run(android.hardware.radio.ITunerCallback iTunerCallback, int i) throws android.os.RemoteException {
                        com.android.server.broadcastradio.aidl.RadioModule.AnonymousClass1.lambda$onTuneFailed$0(csel, tunerResult, iTunerCallback, i);
                    }
                });
            }
        }

        static /* synthetic */ void lambda$onTuneFailed$0(android.hardware.radio.ProgramSelector csel, int tunerResult, android.hardware.radio.ITunerCallback cb, int uid) throws android.os.RemoteException {
            if (csel != null && !com.android.server.broadcastradio.aidl.ConversionUtils.programSelectorMeetsSdkVersionRequirement(csel, uid)) {
                com.android.server.utils.Slogf.e(com.android.server.broadcastradio.aidl.RadioModule.TAG, "onTuneFailed: cannot send program selector requiring higher target SDK version");
            } else {
                cb.onTuneFailed(tunerResult, csel);
            }
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onCurrentProgramInfoChanged(final android.hardware.broadcastradio.ProgramInfo halProgramInfo) {
            com.android.server.broadcastradio.aidl.RadioModule.this.fireLater(new java.lang.Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onCurrentProgramInfoChanged$3(halProgramInfo);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCurrentProgramInfoChanged$3(android.hardware.broadcastradio.ProgramInfo halProgramInfo) {
            final android.hardware.radio.RadioManager.ProgramInfo currentProgramInfo = com.android.server.broadcastradio.aidl.ConversionUtils.programInfoFromHalProgramInfo(halProgramInfo);
            java.util.Objects.requireNonNull(currentProgramInfo, "Program info from AIDL HAL is invalid");
            synchronized (com.android.server.broadcastradio.aidl.RadioModule.this.mLock) {
                com.android.server.broadcastradio.aidl.RadioModule.this.mCurrentProgramInfo = currentProgramInfo;
                com.android.server.broadcastradio.aidl.RadioModule.this.fanoutAidlCallbackLocked(new com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda1
                    @Override // com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable
                    public final void run(android.hardware.radio.ITunerCallback iTunerCallback, int i) throws android.os.RemoteException {
                        com.android.server.broadcastradio.aidl.RadioModule.AnonymousClass1.lambda$onCurrentProgramInfoChanged$2(currentProgramInfo, iTunerCallback, i);
                    }
                });
            }
        }

        static /* synthetic */ void lambda$onCurrentProgramInfoChanged$2(android.hardware.radio.RadioManager.ProgramInfo currentProgramInfo, android.hardware.radio.ITunerCallback cb, int uid) throws android.os.RemoteException {
            if (!com.android.server.broadcastradio.aidl.ConversionUtils.programInfoMeetsSdkVersionRequirement(currentProgramInfo, uid)) {
                com.android.server.utils.Slogf.e(com.android.server.broadcastradio.aidl.RadioModule.TAG, "onCurrentProgramInfoChanged: cannot send program info requiring higher target SDK version");
            } else {
                cb.onCurrentProgramInfoChanged(currentProgramInfo);
            }
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onProgramListUpdated(final android.hardware.broadcastradio.ProgramListChunk programListChunk) {
            com.android.server.broadcastradio.aidl.RadioModule.this.fireLater(new java.lang.Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onProgramListUpdated$4(programListChunk);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onProgramListUpdated$4(android.hardware.broadcastradio.ProgramListChunk programListChunk) {
            synchronized (com.android.server.broadcastradio.aidl.RadioModule.this.mLock) {
                com.android.server.broadcastradio.aidl.RadioModule.this.mProgramInfoCache.filterAndApplyChunk(programListChunk);
                for (int i = 0; i < com.android.server.broadcastradio.aidl.RadioModule.this.mAidlTunerSessions.size(); i++) {
                    ((com.android.server.broadcastradio.aidl.TunerSession) com.android.server.broadcastradio.aidl.RadioModule.this.mAidlTunerSessions.valueAt(i)).onMergedProgramListUpdateFromHal(programListChunk);
                }
            }
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onAntennaStateChange(final boolean connected) {
            com.android.server.broadcastradio.aidl.RadioModule.this.fireLater(new java.lang.Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onAntennaStateChange$6(connected);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAntennaStateChange$6(final boolean connected) {
            synchronized (com.android.server.broadcastradio.aidl.RadioModule.this.mLock) {
                com.android.server.broadcastradio.aidl.RadioModule.this.mAntennaConnected = java.lang.Boolean.valueOf(connected);
                com.android.server.broadcastradio.aidl.RadioModule.this.fanoutAidlCallbackLocked(new com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda0
                    @Override // com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable
                    public final void run(android.hardware.radio.ITunerCallback iTunerCallback, int i) {
                        iTunerCallback.onAntennaState(connected);
                    }
                });
            }
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onConfigFlagUpdated(final int flag, final boolean value) {
            com.android.server.broadcastradio.aidl.RadioModule.this.fireLater(new java.lang.Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onConfigFlagUpdated$8(flag, value);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onConfigFlagUpdated$8(final int flag, final boolean value) {
            synchronized (com.android.server.broadcastradio.aidl.RadioModule.this.mLock) {
                com.android.server.broadcastradio.aidl.RadioModule.this.fanoutAidlCallbackLocked(new com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda2
                    @Override // com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable
                    public final void run(android.hardware.radio.ITunerCallback iTunerCallback, int i) throws android.os.RemoteException {
                        com.android.server.broadcastradio.aidl.RadioModule.AnonymousClass1.lambda$onConfigFlagUpdated$7(flag, value, iTunerCallback, i);
                    }
                });
            }
        }

        static /* synthetic */ void lambda$onConfigFlagUpdated$7(int flag, boolean value, android.hardware.radio.ITunerCallback cb, int uid) throws android.os.RemoteException {
            if (!com.android.server.broadcastradio.aidl.ConversionUtils.configFlagMeetsSdkVersionRequirement(flag, uid)) {
                com.android.server.utils.Slogf.e(com.android.server.broadcastradio.aidl.RadioModule.TAG, "onConfigFlagUpdated: cannot send program info requiring higher target SDK version");
            } else {
                cb.onConfigFlagUpdated(flag, value);
            }
        }

        @Override // android.hardware.broadcastradio.ITunerCallback
        public void onParametersUpdated(final android.hardware.broadcastradio.VendorKeyValue[] parameters) {
            com.android.server.broadcastradio.aidl.RadioModule.this.fireLater(new java.lang.Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onParametersUpdated$10(parameters);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onParametersUpdated$10(android.hardware.broadcastradio.VendorKeyValue[] parameters) {
            synchronized (com.android.server.broadcastradio.aidl.RadioModule.this.mLock) {
                final java.util.Map<java.lang.String, java.lang.String> cparam = com.android.server.broadcastradio.aidl.ConversionUtils.vendorInfoFromHalVendorKeyValues(parameters);
                com.android.server.broadcastradio.aidl.RadioModule.this.fanoutAidlCallbackLocked(new com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$1$$ExternalSyntheticLambda6
                    @Override // com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable
                    public final void run(android.hardware.radio.ITunerCallback iTunerCallback, int i) {
                        iTunerCallback.onParametersUpdated(cparam);
                    }
                });
            }
        }
    }

    RadioModule(android.hardware.broadcastradio.IBroadcastRadio service, android.hardware.radio.RadioManager.ModuleProperties properties) {
        this.mProperties = (android.hardware.radio.RadioManager.ModuleProperties) java.util.Objects.requireNonNull(properties, "properties cannot be null");
        this.mService = (android.hardware.broadcastradio.IBroadcastRadio) java.util.Objects.requireNonNull(service, "service cannot be null");
    }

    static com.android.server.broadcastradio.aidl.RadioModule tryLoadingModule(int moduleId, java.lang.String moduleName, android.os.IBinder serviceBinder) {
        android.hardware.broadcastradio.AmFmRegionConfig amfmConfig;
        android.hardware.broadcastradio.DabTableEntry[] dabConfig;
        try {
            com.android.server.utils.Slogf.i(TAG, "Try loading module for module id = %d, module name = %s", java.lang.Integer.valueOf(moduleId), moduleName);
            android.hardware.broadcastradio.IBroadcastRadio service = android.hardware.broadcastradio.IBroadcastRadio.Stub.asInterface(serviceBinder);
            if (service == null) {
                com.android.server.utils.Slogf.w(TAG, "Module %s is null", moduleName);
                return null;
            }
            try {
                amfmConfig = service.getAmFmRegionConfig(false);
            } catch (java.lang.RuntimeException e) {
                com.android.server.utils.Slogf.i(TAG, "Module %s does not has AMFM config", moduleName);
                amfmConfig = null;
            }
            try {
                dabConfig = service.getDabRegionConfig();
            } catch (java.lang.RuntimeException e2) {
                com.android.server.utils.Slogf.i(TAG, "Module %s does not has DAB config", moduleName);
                dabConfig = null;
            }
            android.hardware.radio.RadioManager.ModuleProperties prop = com.android.server.broadcastradio.aidl.ConversionUtils.propertiesFromHalProperties(moduleId, moduleName, service.getProperties(), amfmConfig, dabConfig);
            return new com.android.server.broadcastradio.aidl.RadioModule(service, prop);
        } catch (android.os.RemoteException ex) {
            com.android.server.utils.Slogf.e(TAG, ex, "Failed to load module %s", moduleName);
            return null;
        }
    }

    android.hardware.broadcastradio.IBroadcastRadio getService() {
        return this.mService;
    }

    android.hardware.radio.RadioManager.ModuleProperties getProperties() {
        return this.mProperties;
    }

    com.android.server.broadcastradio.aidl.TunerSession openSession(android.hardware.radio.ITunerCallback userCb) throws android.os.RemoteException {
        com.android.server.broadcastradio.aidl.TunerSession tunerSession;
        java.lang.Boolean antennaConnected;
        android.hardware.radio.RadioManager.ProgramInfo currentProgramInfo;
        this.mLogger.logRadioEvent("Open TunerSession", new java.lang.Object[0]);
        synchronized (this.mLock) {
            boolean isFirstTunerSession = this.mAidlTunerSessions.isEmpty();
            tunerSession = new com.android.server.broadcastradio.aidl.TunerSession(this, this.mService, userCb);
            this.mAidlTunerSessions.add(tunerSession);
            antennaConnected = this.mAntennaConnected;
            currentProgramInfo = this.mCurrentProgramInfo;
            if (isFirstTunerSession) {
                this.mService.setTunerCallback(this.mHalTunerCallback);
            }
        }
        if (antennaConnected != null) {
            userCb.onAntennaState(antennaConnected.booleanValue());
        }
        if (currentProgramInfo != null) {
            userCb.onCurrentProgramInfoChanged(currentProgramInfo);
        }
        return tunerSession;
    }

    void closeSessions(int error) {
        com.android.server.broadcastradio.aidl.TunerSession[] tunerSessions;
        this.mLogger.logRadioEvent("Close TunerSessions %d", java.lang.Integer.valueOf(error));
        synchronized (this.mLock) {
            tunerSessions = new com.android.server.broadcastradio.aidl.TunerSession[this.mAidlTunerSessions.size()];
            this.mAidlTunerSessions.toArray(tunerSessions);
        }
        for (com.android.server.broadcastradio.aidl.TunerSession tunerSession : tunerSessions) {
            try {
                tunerSession.close(java.lang.Integer.valueOf(error));
            } catch (java.lang.Exception e) {
                com.android.server.utils.Slogf.e(TAG, "Failed to close TunerSession %s: %s", tunerSession, e);
            }
        }
    }

    private android.hardware.radio.ProgramList.Filter buildUnionOfTunerSessionFiltersLocked() {
        java.util.Set<java.lang.Integer> idTypes = null;
        java.util.Set<android.hardware.radio.ProgramSelector.Identifier> ids = null;
        boolean includeCategories = false;
        boolean excludeModifications = true;
        for (int i = 0; i < this.mAidlTunerSessions.size(); i++) {
            android.hardware.radio.ProgramList.Filter filter = this.mAidlTunerSessions.valueAt(i).getProgramListFilter();
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

    void onTunerSessionProgramListFilterChanged(com.android.server.broadcastradio.aidl.TunerSession session) {
        synchronized (this.mLock) {
            onTunerSessionProgramListFilterChangedLocked(session);
        }
    }

    private void onTunerSessionProgramListFilterChangedLocked(com.android.server.broadcastradio.aidl.TunerSession session) {
        android.hardware.radio.ProgramList.Filter newFilter = buildUnionOfTunerSessionFiltersLocked();
        if (newFilter == null) {
            if (this.mUnionOfAidlProgramFilters == null) {
                return;
            }
            this.mUnionOfAidlProgramFilters = null;
            try {
                this.mService.stopProgramListUpdates();
                return;
            } catch (android.os.RemoteException ex) {
                com.android.server.utils.Slogf.e(TAG, ex, "mHalTunerSession.stopProgramListUpdates() failed", new java.lang.Object[0]);
                return;
            }
        }
        synchronized (this.mLock) {
            if (newFilter.equals(this.mUnionOfAidlProgramFilters)) {
                if (session != null) {
                    session.updateProgramInfoFromHalCache(this.mProgramInfoCache);
                }
                return;
            }
            this.mUnionOfAidlProgramFilters = newFilter;
            try {
                this.mService.startProgramListUpdates(com.android.server.broadcastradio.aidl.ConversionUtils.filterToHalProgramFilter(newFilter));
            } catch (android.os.RemoteException ex2) {
                com.android.server.utils.Slogf.e(TAG, ex2, "mHalTunerSession.startProgramListUpdates() failed", new java.lang.Object[0]);
            } catch (java.lang.RuntimeException ex3) {
                throw com.android.server.broadcastradio.aidl.ConversionUtils.throwOnError(ex3, "Start Program ListUpdates");
            }
        }
    }

    void onTunerSessionClosed(com.android.server.broadcastradio.aidl.TunerSession tunerSession) {
        synchronized (this.mLock) {
            onTunerSessionsClosedLocked(tunerSession);
        }
    }

    private void onTunerSessionsClosedLocked(com.android.server.broadcastradio.aidl.TunerSession... tunerSessions) {
        for (com.android.server.broadcastradio.aidl.TunerSession tunerSession : tunerSessions) {
            this.mAidlTunerSessions.remove(tunerSession);
        }
        onTunerSessionProgramListFilterChanged(null);
        if (this.mAidlTunerSessions.isEmpty()) {
            try {
                this.mService.unsetTunerCallback();
            } catch (android.os.RemoteException ex) {
                com.android.server.utils.Slogf.wtf(TAG, ex, "Failed to unregister HAL callback for module %d", java.lang.Integer.valueOf(this.mProperties.getId()));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireLater(final java.lang.Runnable r) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                r.run();
            }
        });
    }

    void fanoutAidlCallback(final com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable runnable) {
        fireLater(new java.lang.Runnable() { // from class: com.android.server.broadcastradio.aidl.RadioModule$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$fanoutAidlCallback$1(runnable);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$fanoutAidlCallback$1(com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable runnable) {
        synchronized (this.mLock) {
            fanoutAidlCallbackLocked(runnable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fanoutAidlCallbackLocked(com.android.server.broadcastradio.aidl.RadioModule.AidlCallbackRunnable runnable) {
        int currentUserId = com.android.server.broadcastradio.RadioServiceUserController.getCurrentUser();
        java.util.List<com.android.server.broadcastradio.aidl.TunerSession> deadSessions = null;
        for (int i = 0; i < this.mAidlTunerSessions.size(); i++) {
            if (this.mAidlTunerSessions.valueAt(i).mUserId == currentUserId || this.mAidlTunerSessions.valueAt(i).mUserId == 0) {
                try {
                    runnable.run(this.mAidlTunerSessions.valueAt(i).mCallback, this.mAidlTunerSessions.valueAt(i).getUid());
                } catch (android.os.DeadObjectException e) {
                    com.android.server.utils.Slogf.e(TAG, "Removing dead TunerSession");
                    if (deadSessions == null) {
                        deadSessions = new java.util.ArrayList<>();
                    }
                    deadSessions.add(this.mAidlTunerSessions.valueAt(i));
                } catch (android.os.RemoteException ex) {
                    com.android.server.utils.Slogf.e(TAG, ex, "Failed to invoke ITunerCallback", new java.lang.Object[0]);
                }
            }
        }
        if (deadSessions != null) {
            onTunerSessionsClosedLocked((com.android.server.broadcastradio.aidl.TunerSession[]) deadSessions.toArray(new com.android.server.broadcastradio.aidl.TunerSession[deadSessions.size()]));
        }
    }

    android.hardware.radio.ICloseHandle addAnnouncementListener(final android.hardware.radio.IAnnouncementListener listener, int[] enabledTypes) throws android.os.RemoteException {
        this.mLogger.logRadioEvent("Add AnnouncementListener", new java.lang.Object[0]);
        byte[] enabledList = new byte[enabledTypes.length];
        for (int index = 0; index < enabledList.length; index++) {
            enabledList[index] = (byte) enabledTypes[index];
        }
        final android.hardware.broadcastradio.ICloseHandle[] hwCloseHandle = {null};
        android.hardware.broadcastradio.IAnnouncementListener hwListener = new android.hardware.broadcastradio.IAnnouncementListener.Stub() { // from class: com.android.server.broadcastradio.aidl.RadioModule.2
            @Override // android.hardware.broadcastradio.IAnnouncementListener
            public int getInterfaceVersion() {
                return 2;
            }

            @Override // android.hardware.broadcastradio.IAnnouncementListener
            public java.lang.String getInterfaceHash() {
                return "bff68a8bc8b7cc191ab62bee10f7df8e79494467";
            }

            @Override // android.hardware.broadcastradio.IAnnouncementListener
            public void onListUpdated(android.hardware.broadcastradio.Announcement[] hwAnnouncements) throws android.os.RemoteException {
                java.util.List<android.hardware.radio.Announcement> announcements = new java.util.ArrayList<>(hwAnnouncements.length);
                for (android.hardware.broadcastradio.Announcement announcement : hwAnnouncements) {
                    announcements.add(com.android.server.broadcastradio.aidl.ConversionUtils.announcementFromHalAnnouncement(announcement));
                }
                listener.onListUpdated(announcements);
            }
        };
        try {
            hwCloseHandle[0] = this.mService.registerAnnouncementListener(hwListener, enabledList);
            return new android.hardware.radio.ICloseHandle.Stub() { // from class: com.android.server.broadcastradio.aidl.RadioModule.3
                public void close() {
                    try {
                        hwCloseHandle[0].close();
                    } catch (android.os.RemoteException ex) {
                        com.android.server.utils.Slogf.e(com.android.server.broadcastradio.aidl.RadioModule.TAG, ex, "Failed closing announcement listener", new java.lang.Object[0]);
                    }
                    hwCloseHandle[0] = null;
                }
            };
        } catch (java.lang.RuntimeException ex) {
            throw com.android.server.broadcastradio.aidl.ConversionUtils.throwOnError(ex, "AnnouncementListener");
        }
    }

    android.graphics.Bitmap getImage(int id) {
        this.mLogger.logRadioEvent("Get image for id = %d", java.lang.Integer.valueOf(id));
        if (id == 0) {
            throw new java.lang.IllegalArgumentException("Image ID is missing");
        }
        try {
            byte[] rawImage = this.mService.getImage(id);
            if (rawImage == null || rawImage.length == 0) {
                return null;
            }
            return android.graphics.BitmapFactory.decodeByteArray(rawImage, 0, rawImage.length);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    void dumpInfo(android.util.IndentingPrintWriter pw) {
        pw.printf("RadioModule\n", new java.lang.Object[0]);
        pw.increaseIndent();
        synchronized (this.mLock) {
            pw.printf("BroadcastRadioServiceImpl: %s\n", new java.lang.Object[]{this.mService});
            pw.printf("Properties: %s\n", new java.lang.Object[]{this.mProperties});
            pw.printf("Antenna state: ", new java.lang.Object[0]);
            if (this.mAntennaConnected == null) {
                pw.printf("undetermined\n", new java.lang.Object[0]);
            } else {
                pw.printf("%s\n", new java.lang.Object[]{this.mAntennaConnected.booleanValue() ? "connected" : "not connected"});
            }
            pw.printf("current ProgramInfo: %s\n", new java.lang.Object[]{this.mCurrentProgramInfo});
            pw.printf("ProgramInfoCache: %s\n", new java.lang.Object[]{this.mProgramInfoCache});
            pw.printf("Union of AIDL ProgramFilters: %s\n", new java.lang.Object[]{this.mUnionOfAidlProgramFilters});
            pw.printf("AIDL TunerSessions [%d]:\n", new java.lang.Object[]{java.lang.Integer.valueOf(this.mAidlTunerSessions.size())});
            pw.increaseIndent();
            for (int i = 0; i < this.mAidlTunerSessions.size(); i++) {
                this.mAidlTunerSessions.valueAt(i).dumpInfo(pw);
            }
            pw.decreaseIndent();
        }
        pw.printf("Radio module events:\n", new java.lang.Object[0]);
        pw.increaseIndent();
        this.mLogger.dump(pw);
        pw.decreaseIndent();
        pw.decreaseIndent();
    }
}
