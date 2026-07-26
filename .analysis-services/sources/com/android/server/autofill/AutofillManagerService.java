package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
public final class AutofillManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.autofill.AutofillManagerService, com.android.server.autofill.AutofillManagerServiceImpl> {
    private static final char COMPAT_PACKAGE_DELIMITER = ':';
    private static final char COMPAT_PACKAGE_URL_IDS_BLOCK_BEGIN = '[';
    private static final char COMPAT_PACKAGE_URL_IDS_BLOCK_END = ']';
    private static final char COMPAT_PACKAGE_URL_IDS_DELIMITER = ',';
    private static final int DEFAULT_AUGMENTED_AUTOFILL_REQUEST_TIMEOUT_MILLIS = 5000;
    private static final java.lang.String DEFAULT_PCC_FEATURE_PROVIDER_HINTS = "";
    private static final boolean DEFAULT_PCC_USE_FALLBACK = true;
    private static final boolean DEFAULT_PREFER_PROVIDER_OVER_PCC = true;
    static final java.lang.String RECEIVER_BUNDLE_EXTRA_SESSIONS = "sessions";
    private static final java.lang.String TAG = "AutofillManagerService";
    private static final java.lang.Object sLock = com.android.server.autofill.AutofillManagerService.class;
    private static int sPartitionMaxCount = 10;
    private static int sVisibleDatasetsMaxCount = 0;
    private final android.app.ActivityManagerInternal mAm;
    final com.android.server.infra.FrameworkResourcesServiceNameResolver mAugmentedAutofillResolver;
    final com.android.server.autofill.AutofillManagerService.AugmentedAutofillState mAugmentedAutofillState;
    int mAugmentedServiceIdleUnbindTimeoutMs;
    int mAugmentedServiceRequestTimeoutMs;
    private final com.android.server.autofill.AutofillManagerService.AutofillCompatState mAutofillCompatState;
    private boolean mAutofillCredmanIntegrationEnabled;
    private final android.content.BroadcastReceiver mBroadcastReceiver;
    final android.content.ComponentName mCredentialAutofillService;
    private final com.android.server.autofill.AutofillManagerService.DisabledInfoCache mDisabledInfoCache;
    final com.android.server.infra.FrameworkResourcesServiceNameResolver mFieldClassificationResolver;
    private final java.lang.Object mFlagLock;
    private boolean mIsFillFieldsFromCurrentSessionOnly;
    private final com.android.server.autofill.AutofillManagerService.LocalService mLocalService;
    private int mMaxInputLengthForAutofill;
    private boolean mPccClassificationEnabled;
    private boolean mPccPreferProviderOverPcc;
    private java.lang.String mPccProviderHints;
    private boolean mPccUseFallbackDetection;
    private final android.util.LocalLog mRequestsHistory;
    private int mSupportedSmartSuggestionModes;
    private final com.android.server.autofill.ui.AutoFillUI mUi;
    private final android.util.LocalLog mUiLatencyHistory;
    private final android.util.LocalLog mWtfHistory;

    /* JADX INFO: renamed from: com.android.server.autofill.AutofillManagerService$1, reason: invalid class name */
    class AnonymousClass1 extends android.content.BroadcastReceiver {
        AnonymousClass1() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.d(com.android.server.autofill.AutofillManagerService.TAG, "Close system dialogs");
                }
                synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                    com.android.server.autofill.AutofillManagerService.this.visitServicesLocked(new com.android.server.infra.AbstractMasterSystemService.Visitor() { // from class: com.android.server.autofill.AutofillManagerService$1$$ExternalSyntheticLambda0
                        @Override // com.android.server.infra.AbstractMasterSystemService.Visitor
                        public final void visit(java.lang.Object obj) {
                            ((com.android.server.autofill.AutofillManagerServiceImpl) obj).forceRemoveFinishedSessionsLocked();
                        }
                    });
                }
                com.android.server.autofill.AutofillManagerService.this.mUi.hideAll(null);
            }
        }
    }

    public AutofillManagerService(android.content.Context context) {
        super(context, new com.android.server.infra.SecureSettingsServiceNameResolver(context, "autofill_service"), "no_autofill", 4);
        this.mRequestsHistory = new android.util.LocalLog(20);
        this.mUiLatencyHistory = new android.util.LocalLog(20);
        this.mWtfHistory = new android.util.LocalLog(50);
        this.mAutofillCompatState = new com.android.server.autofill.AutofillManagerService.AutofillCompatState();
        this.mDisabledInfoCache = new com.android.server.autofill.AutofillManagerService.DisabledInfoCache();
        this.mLocalService = new com.android.server.autofill.AutofillManagerService.LocalService();
        this.mBroadcastReceiver = new com.android.server.autofill.AutofillManagerService.AnonymousClass1();
        this.mAugmentedAutofillState = new com.android.server.autofill.AutofillManagerService.AugmentedAutofillState();
        this.mFlagLock = new java.lang.Object();
        this.mUi = new com.android.server.autofill.ui.AutoFillUI(android.app.ActivityThread.currentActivityThread().getSystemUiContext());
        this.mAm = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
        android.provider.DeviceConfig.addOnPropertiesChangedListener("autofill", android.app.ActivityThread.currentApplication().getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.autofill.AutofillManagerService$$ExternalSyntheticLambda1
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.lambda$new$0(properties);
            }
        });
        setLogLevelFromSettings();
        setMaxPartitionsFromSettings();
        setMaxVisibleDatasetsFromSettings();
        setDeviceConfigProperties();
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
        context.registerReceiver(this.mBroadcastReceiver, filter, null, com.android.server.FgThread.getHandler(), 2);
        this.mAugmentedAutofillResolver = new com.android.server.infra.FrameworkResourcesServiceNameResolver(getContext(), android.R.string.config_defaultContextualSearchPackageName);
        this.mAugmentedAutofillResolver.setOnTemporaryServiceNameChangedCallback(new com.android.server.infra.ServiceNameResolver.NameResolverListener() { // from class: com.android.server.autofill.AutofillManagerService$$ExternalSyntheticLambda2
            @Override // com.android.server.infra.ServiceNameResolver.NameResolverListener
            public final void onNameResolved(int i, java.lang.String str, boolean z) {
                this.f$0.lambda$new$1(i, str, z);
            }
        });
        this.mFieldClassificationResolver = new com.android.server.infra.FrameworkResourcesServiceNameResolver(getContext(), android.R.string.config_defaultNetworkScorerPackageName);
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Resolving FieldClassificationService to serviceName: " + this.mFieldClassificationResolver.readServiceName(0));
        }
        this.mFieldClassificationResolver.setOnTemporaryServiceNameChangedCallback(new com.android.server.infra.ServiceNameResolver.NameResolverListener() { // from class: com.android.server.autofill.AutofillManagerService$$ExternalSyntheticLambda3
            @Override // com.android.server.infra.ServiceNameResolver.NameResolverListener
            public final void onNameResolved(int i, java.lang.String str, boolean z) {
                this.f$0.lambda$new$2(i, str, z);
            }
        });
        if (this.mSupportedSmartSuggestionModes != 0) {
            java.util.List<android.content.pm.UserInfo> users = getSupportedUsers();
            for (int i = 0; i < users.size(); i++) {
                int userId = users.get(i).id;
                getServiceForUserLocked(userId);
                this.mAugmentedAutofillState.setServiceInfo(userId, this.mAugmentedAutofillResolver.getServiceName(userId), this.mAugmentedAutofillResolver.isTemporary(userId));
            }
        }
        java.lang.String credentialManagerAutofillCompName = context.getResources().getString(android.R.string.config_defaultModuleMetadataProvider);
        if (credentialManagerAutofillCompName != null && !credentialManagerAutofillCompName.isEmpty()) {
            this.mCredentialAutofillService = android.content.ComponentName.unflattenFromString(credentialManagerAutofillCompName);
        } else {
            this.mCredentialAutofillService = null;
            android.util.Slog.w(TAG, "Invalid CredentialAutofillService");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.provider.DeviceConfig.Properties properties) {
        onDeviceConfigChange(properties.getKeyset());
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected java.lang.String getServiceSettingsProperty() {
        return "autofill_service";
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void registerForExtraSettingsChanges(android.content.ContentResolver resolver, android.database.ContentObserver observer) {
        resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("autofill_logging_level"), false, observer, -1);
        resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("autofill_max_partitions_size"), false, observer, -1);
        resolver.registerContentObserver(android.provider.Settings.Global.getUriFor("autofill_max_visible_datasets"), false, observer, -1);
        resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("selected_input_method_subtype"), false, observer, -1);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0031  */
    @Override // com.android.server.infra.AbstractMasterSystemService
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onSettingsChanged(int r4, java.lang.String r5) {
        /*
            r3 = this;
            int r0 = r5.hashCode()
            switch(r0) {
                case -1848997872: goto L27;
                case -1299292969: goto L1d;
                case -1048937777: goto L13;
                case 1194058837: goto L8;
                default: goto L7;
            }
        L7:
            goto L31
        L8:
            java.lang.String r0 = "selected_input_method_subtype"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L7
            r0 = 3
            goto L32
        L13:
            java.lang.String r0 = "autofill_max_partitions_size"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L7
            r0 = 1
            goto L32
        L1d:
            java.lang.String r0 = "autofill_logging_level"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L7
            r0 = 0
            goto L32
        L27:
            java.lang.String r0 = "autofill_max_visible_datasets"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L7
            r0 = 2
            goto L32
        L31:
            r0 = -1
        L32:
            switch(r0) {
                case 0: goto L63;
                case 1: goto L5f;
                case 2: goto L5b;
                case 3: goto L57;
                default: goto L35;
            }
        L35:
            java.lang.String r0 = "AutofillManagerService"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unexpected property ("
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r5)
            java.lang.String r2 = "); updating cache instead"
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Slog.w(r0, r1)
            java.lang.Object r0 = r3.mLock
            monitor-enter(r0)
            goto L67
        L57:
            r3.handleInputMethodSwitch(r4)
            goto L6b
        L5b:
            r3.setMaxVisibleDatasetsFromSettings()
            goto L6b
        L5f:
            r3.setMaxPartitionsFromSettings()
            goto L6b
        L63:
            r3.setLogLevelFromSettings()
            goto L6b
        L67:
            r3.updateCachedServiceLocked(r4)     // Catch: java.lang.Throwable -> L6c
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L6c
        L6b:
            return
        L6c:
            r1 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> L6c
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.autofill.AutofillManagerService.onSettingsChanged(int, java.lang.String):void");
    }

    private void handleInputMethodSwitch(int userId) {
        synchronized (this.mLock) {
            com.android.server.autofill.AutofillManagerServiceImpl service = peekServiceForUserWithLocalBinderIdentityLocked(userId);
            if (service != null) {
                service.onSwitchInputMethod();
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0084  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void onDeviceConfigChange(java.util.Set<java.lang.String> r6) {
        /*
            Method dump skipped, instruction units count: 238
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.autofill.AutofillManagerService.onDeviceConfigChange(java.util.Set):void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onAugmentedServiceNameChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$new$1(int userId, java.lang.String serviceName, boolean isTemporary) {
        this.mAugmentedAutofillState.setServiceInfo(userId, serviceName, isTemporary);
        synchronized (this.mLock) {
            com.android.server.autofill.AutofillManagerServiceImpl service = peekServiceForUserWithLocalBinderIdentityLocked(userId);
            if (service == null) {
                getServiceForUserWithLocalBinderIdentityLocked(userId);
            } else {
                service.updateRemoteAugmentedAutofillService();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onFieldClassificationServiceNameChanged, reason: merged with bridge method [inline-methods] */
    public void lambda$new$2(int userId, java.lang.String serviceName, boolean isTemporary) {
        synchronized (this.mLock) {
            com.android.server.autofill.AutofillManagerServiceImpl service = peekServiceForUserWithLocalBinderIdentityLocked(userId);
            if (service == null) {
                getServiceForUserWithLocalBinderIdentityLocked(userId);
            } else {
                service.updateRemoteFieldClassificationService();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.autofill.AutofillManagerServiceImpl getServiceForUserWithLocalBinderIdentityLocked(int userId) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.autofill.AutofillManagerServiceImpl managerService = getServiceForUserLocked(userId);
            return managerService;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.autofill.AutofillManagerServiceImpl peekServiceForUserWithLocalBinderIdentityLocked(int userId) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            com.android.server.autofill.AutofillManagerServiceImpl managerService = peekServiceForUserLocked(userId);
            return managerService;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.autofill.AutofillManagerServiceImpl newServiceLocked(int resolvedUserId, boolean disabled) {
        return new com.android.server.autofill.AutofillManagerServiceImpl(this, this.mLock, this.mUiLatencyHistory, this.mWtfHistory, resolvedUserId, this.mUi, this.mAutofillCompatState, disabled, this.mDisabledInfoCache);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public void onServiceRemoved(com.android.server.autofill.AutofillManagerServiceImpl service, int userId) {
        service.destroyLocked();
        this.mDisabledInfoCache.remove(userId);
        this.mAutofillCompatState.removeCompatibilityModeRequests(userId);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public void onServiceEnabledLocked(com.android.server.autofill.AutofillManagerServiceImpl service, int userId) {
        addCompatibilityModeRequestsLocked(service, userId);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.MANAGE_AUTO_FILL", TAG);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("autofill", new com.android.server.autofill.AutofillManagerService.AutoFillManagerServiceStub());
        publishLocalService(android.view.autofill.AutofillManagerInternal.class, this.mLocalService);
    }

    @Override // com.android.server.SystemService
    public boolean isUserSupported(com.android.server.SystemService.TargetUser user) {
        return user.isFull() || user.isProfile();
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "Hiding UI when user switched");
        }
        this.mUi.hideAll(null);
    }

    int getSupportedSmartSuggestionModesLocked() {
        return this.mSupportedSmartSuggestionModes;
    }

    void logRequestLocked(java.lang.String historyItem) {
        this.mRequestsHistory.log(historyItem);
    }

    boolean isInstantServiceAllowed() {
        return this.mAllowInstantService;
    }

    void removeAllSessions(int userId, com.android.internal.os.IResultReceiver receiver) {
        android.util.Slog.i(TAG, "removeAllSessions() for userId " + userId);
        enforceCallingPermissionForManagement();
        synchronized (this.mLock) {
            if (userId != -1) {
                com.android.server.autofill.AutofillManagerServiceImpl service = peekServiceForUserLocked(userId);
                if (service != null) {
                    service.forceRemoveAllSessionsLocked();
                }
            } else {
                visitServicesLocked(new com.android.server.infra.AbstractMasterSystemService.Visitor() { // from class: com.android.server.autofill.AutofillManagerService$$ExternalSyntheticLambda4
                    @Override // com.android.server.infra.AbstractMasterSystemService.Visitor
                    public final void visit(java.lang.Object obj) {
                        ((com.android.server.autofill.AutofillManagerServiceImpl) obj).forceRemoveAllSessionsLocked();
                    }
                });
            }
        }
        try {
            receiver.send(0, new android.os.Bundle());
        } catch (android.os.RemoteException e) {
        }
    }

    void listSessions(int userId, com.android.internal.os.IResultReceiver receiver) {
        android.util.Slog.i(TAG, "listSessions() for userId " + userId);
        enforceCallingPermissionForManagement();
        android.os.Bundle resultData = new android.os.Bundle();
        final java.util.ArrayList<java.lang.String> sessions = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            if (userId != -1) {
                com.android.server.autofill.AutofillManagerServiceImpl service = peekServiceForUserLocked(userId);
                if (service != null) {
                    service.listSessionsLocked(sessions);
                }
            } else {
                visitServicesLocked(new com.android.server.infra.AbstractMasterSystemService.Visitor() { // from class: com.android.server.autofill.AutofillManagerService$$ExternalSyntheticLambda0
                    @Override // com.android.server.infra.AbstractMasterSystemService.Visitor
                    public final void visit(java.lang.Object obj) {
                        ((com.android.server.autofill.AutofillManagerServiceImpl) obj).listSessionsLocked(sessions);
                    }
                });
            }
        }
        resultData.putStringArrayList(RECEIVER_BUNDLE_EXTRA_SESSIONS, sessions);
        try {
            receiver.send(0, resultData);
        } catch (android.os.RemoteException e) {
        }
    }

    void reset() {
        android.util.Slog.i(TAG, "reset()");
        enforceCallingPermissionForManagement();
        synchronized (this.mLock) {
            visitServicesLocked(new com.android.server.infra.AbstractMasterSystemService.Visitor() { // from class: com.android.server.autofill.AutofillManagerService$$ExternalSyntheticLambda5
                @Override // com.android.server.infra.AbstractMasterSystemService.Visitor
                public final void visit(java.lang.Object obj) {
                    ((com.android.server.autofill.AutofillManagerServiceImpl) obj).destroyLocked();
                }
            });
            clearCacheLocked();
        }
    }

    void setLogLevel(int level) {
        android.util.Slog.i(TAG, "setLogLevel(): " + level);
        enforceCallingPermissionForManagement();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.provider.Settings.Global.putInt(getContext().getContentResolver(), "autofill_logging_level", level);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void setLogLevelFromSettings() {
        int level = android.provider.Settings.Global.getInt(getContext().getContentResolver(), "autofill_logging_level", 4);
        boolean debug = false;
        boolean verbose = false;
        if (level != 0) {
            if (level == 4) {
                verbose = true;
                debug = true;
            } else if (level == 2) {
                debug = true;
            } else {
                android.util.Slog.w(TAG, "setLogLevelFromSettings(): invalid level: " + level);
            }
        }
        if (debug || com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "setLogLevelFromSettings(): level=" + level + ", debug=" + debug + ", verbose=" + verbose);
        }
        synchronized (this.mLock) {
            setLoggingLevelsLocked(debug, verbose);
        }
    }

    int getLogLevel() {
        enforceCallingPermissionForManagement();
        synchronized (this.mLock) {
            if (com.android.server.autofill.Helper.sVerbose) {
                return 4;
            }
            return com.android.server.autofill.Helper.sDebug ? 2 : 0;
        }
    }

    int getMaxPartitions() {
        int i;
        synchronized (this.mLock) {
            i = sPartitionMaxCount;
        }
        return i;
    }

    void setMaxPartitions(int max) {
        android.util.Slog.i(TAG, "setMaxPartitions(): " + max);
        enforceCallingPermissionForManagement();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.provider.Settings.Global.putInt(getContext().getContentResolver(), "autofill_max_partitions_size", max);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void setMaxPartitionsFromSettings() {
        int max = android.provider.Settings.Global.getInt(getContext().getContentResolver(), "autofill_max_partitions_size", 10);
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "setMaxPartitionsFromSettings(): " + max);
        }
        synchronized (sLock) {
            sPartitionMaxCount = max;
        }
    }

    int getMaxVisibleDatasets() {
        int i;
        synchronized (sLock) {
            i = sVisibleDatasetsMaxCount;
        }
        return i;
    }

    void setMaxVisibleDatasets(int max) {
        android.util.Slog.i(TAG, "setMaxVisibleDatasets(): " + max);
        enforceCallingPermissionForManagement();
        long token = android.os.Binder.clearCallingIdentity();
        try {
            android.provider.Settings.Global.putInt(getContext().getContentResolver(), "autofill_max_visible_datasets", max);
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    private void setMaxVisibleDatasetsFromSettings() {
        int max = android.provider.Settings.Global.getInt(getContext().getContentResolver(), "autofill_max_visible_datasets", 0);
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "setMaxVisibleDatasetsFromSettings(): " + max);
        }
        synchronized (sLock) {
            sVisibleDatasetsMaxCount = max;
        }
    }

    private void setDeviceConfigProperties() {
        synchronized (this.mLock) {
            this.mAugmentedServiceIdleUnbindTimeoutMs = android.provider.DeviceConfig.getInt("autofill", "augmented_service_idle_unbind_timeout", 0);
            this.mAugmentedServiceRequestTimeoutMs = android.provider.DeviceConfig.getInt("autofill", "augmented_service_request_timeout", 5000);
            this.mSupportedSmartSuggestionModes = android.provider.DeviceConfig.getInt("autofill", "smart_suggestion_supported_modes", 1);
            if (this.verbose) {
                android.util.Slog.v(this.mTag, "setDeviceConfigProperties() for AugmentedAutofill: augmentedIdleTimeout=" + this.mAugmentedServiceIdleUnbindTimeoutMs + ", augmentedRequestTimeout=" + this.mAugmentedServiceRequestTimeoutMs + ", smartSuggestionMode=" + android.view.autofill.AutofillManager.getSmartSuggestionModeToString(this.mSupportedSmartSuggestionModes));
            }
        }
        synchronized (this.mFlagLock) {
            this.mPccClassificationEnabled = android.provider.DeviceConfig.getBoolean("autofill", "pcc_classification_enabled", false);
            this.mPccPreferProviderOverPcc = android.provider.DeviceConfig.getBoolean("autofill", "prefer_provider_over_pcc", true);
            this.mPccUseFallbackDetection = android.provider.DeviceConfig.getBoolean("autofill", "pcc_use_fallback", true);
            this.mPccProviderHints = android.provider.DeviceConfig.getString("autofill", "pcc_classification_hints", "");
            this.mMaxInputLengthForAutofill = android.provider.DeviceConfig.getInt("autofill", "max_input_length_for_autofill", 3);
            this.mAutofillCredmanIntegrationEnabled = android.service.autofill.Flags.autofillCredmanIntegration();
            this.mIsFillFieldsFromCurrentSessionOnly = android.view.autofill.AutofillFeatureFlags.shouldFillFieldsFromCurrentSessionOnly();
            if (this.verbose) {
                android.util.Slog.v(this.mTag, "setDeviceConfigProperties() for PCC: mPccClassificationEnabled=" + this.mPccClassificationEnabled + ", mPccPreferProviderOverPcc=" + this.mPccPreferProviderOverPcc + ", mPccUseFallbackDetection=" + this.mPccUseFallbackDetection + ", mPccProviderHints=" + this.mPccProviderHints + ", mAutofillCredmanIntegrationEnabled=" + this.mAutofillCredmanIntegrationEnabled + ", mIsFillFieldsFromCurrentSessionOnly=" + this.mIsFillFieldsFromCurrentSessionOnly);
            }
        }
    }

    private void updateCachedServices() {
        java.util.List<android.content.pm.UserInfo> supportedUsers = getSupportedUsers();
        for (android.content.pm.UserInfo userInfo : supportedUsers) {
            synchronized (this.mLock) {
                updateCachedServiceLocked(userInfo.id);
            }
        }
    }

    void calculateScore(java.lang.String algorithmName, java.lang.String value1, java.lang.String value2, android.os.RemoteCallback callback) {
        enforceCallingPermissionForManagement();
        com.android.server.autofill.FieldClassificationStrategy strategy = new com.android.server.autofill.FieldClassificationStrategy(getContext(), -2);
        strategy.calculateScores(callback, java.util.Arrays.asList(android.view.autofill.AutofillValue.forText(value1)), new java.lang.String[]{value2}, new java.lang.String[]{null}, algorithmName, null, null, null);
    }

    java.lang.Boolean getFullScreenMode() {
        enforceCallingPermissionForManagement();
        return com.android.server.autofill.Helper.sFullScreenMode;
    }

    void setFullScreenMode(java.lang.Boolean mode) {
        enforceCallingPermissionForManagement();
        com.android.server.autofill.Helper.sFullScreenMode = mode;
    }

    void setTemporaryAugmentedAutofillService(int userId, java.lang.String serviceName, int durationMs) {
        android.util.Slog.i(this.mTag, "setTemporaryAugmentedAutofillService(" + userId + ") to " + serviceName + " for " + durationMs + "ms");
        enforceCallingPermissionForManagement();
        java.util.Objects.requireNonNull(serviceName);
        if (durationMs > 120000) {
            throw new java.lang.IllegalArgumentException("Max duration is 120000 (called with " + durationMs + ")");
        }
        this.mAugmentedAutofillResolver.setTemporaryService(userId, serviceName, durationMs);
    }

    void resetTemporaryAugmentedAutofillService(int userId) {
        enforceCallingPermissionForManagement();
        this.mAugmentedAutofillResolver.resetTemporaryService(userId);
    }

    boolean isDefaultAugmentedServiceEnabled(int userId) {
        enforceCallingPermissionForManagement();
        return this.mAugmentedAutofillResolver.isDefaultServiceEnabled(userId);
    }

    boolean setDefaultAugmentedServiceEnabled(int userId, boolean enabled) {
        android.util.Slog.i(this.mTag, "setDefaultAugmentedServiceEnabled() for userId " + userId + ": " + enabled);
        enforceCallingPermissionForManagement();
        synchronized (this.mLock) {
            com.android.server.autofill.AutofillManagerServiceImpl service = getServiceForUserLocked(userId);
            if (service != null) {
                boolean changed = this.mAugmentedAutofillResolver.setDefaultServiceEnabled(userId, enabled);
                if (changed) {
                    service.updateRemoteAugmentedAutofillService();
                    return true;
                }
                if (this.debug) {
                    android.util.Slog.d(TAG, "setDefaultAugmentedServiceEnabled(): already " + enabled);
                }
            }
            return false;
        }
    }

    boolean isFieldDetectionServiceEnabledForUser(int userId) {
        enforceCallingPermissionForManagement();
        synchronized (this.mLock) {
            com.android.server.autofill.AutofillManagerServiceImpl service = getServiceForUserLocked(userId);
            if (service != null) {
                return service.isPccClassificationEnabled();
            }
            return false;
        }
    }

    java.lang.String getFieldDetectionServiceName(int userId) {
        enforceCallingPermissionForManagement();
        return this.mFieldClassificationResolver.readServiceName(userId);
    }

    boolean setTemporaryDetectionService(int userId, java.lang.String serviceName, int durationMs) {
        android.util.Slog.i(this.mTag, "setTemporaryDetectionService(" + userId + ") to " + serviceName + " for " + durationMs + "ms");
        enforceCallingPermissionForManagement();
        java.util.Objects.requireNonNull(serviceName);
        this.mFieldClassificationResolver.setTemporaryService(userId, serviceName, durationMs);
        return false;
    }

    void resetTemporaryDetectionService(int userId) {
        enforceCallingPermissionForManagement();
        this.mFieldClassificationResolver.resetTemporaryService(userId);
    }

    boolean requestSavedPasswordCount(int userId, com.android.internal.os.IResultReceiver receiver) {
        enforceCallingPermissionForManagement();
        synchronized (this.mLock) {
            com.android.server.autofill.AutofillManagerServiceImpl service = peekServiceForUserLocked(userId);
            if (service != null) {
                service.requestSavedPasswordCount(receiver);
                return true;
            }
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "requestSavedPasswordCount(): no service for " + userId);
            }
            return false;
        }
    }

    private void setLoggingLevelsLocked(boolean debug, boolean verbose) {
        com.android.server.autofill.Helper.sDebug = debug;
        android.view.autofill.Helper.sDebug = debug;
        this.debug = debug;
        com.android.server.autofill.Helper.sVerbose = verbose;
        android.view.autofill.Helper.sVerbose = verbose;
        this.verbose = verbose;
    }

    private void addCompatibilityModeRequestsLocked(com.android.server.autofill.AutofillManagerServiceImpl service, int userId) {
        this.mAutofillCompatState.reset(userId);
        android.util.ArrayMap<java.lang.String, java.lang.Long> compatPackages = service.getCompatibilityPackagesLocked();
        if (compatPackages == null || compatPackages.isEmpty()) {
            return;
        }
        java.util.Map<java.lang.String, java.lang.String[]> allowedPackages = getAllowedCompatModePackages();
        int compatPackageCount = compatPackages.size();
        for (int i = 0; i < compatPackageCount; i++) {
            java.lang.String packageName = compatPackages.keyAt(i);
            if (allowedPackages == null || !allowedPackages.containsKey(packageName)) {
                android.util.Slog.w(TAG, "Ignoring not allowed compat package " + packageName);
            } else {
                java.lang.Long maxVersionCode = compatPackages.valueAt(i);
                if (maxVersionCode != null) {
                    this.mAutofillCompatState.addCompatibilityModeRequest(packageName, maxVersionCode.longValue(), allowedPackages.get(packageName), userId);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.lang.String getAllowedCompatModePackagesFromDeviceConfig() {
        java.lang.String config = android.provider.DeviceConfig.getString("autofill", "compat_mode_allowed_packages", (java.lang.String) null);
        if (!android.text.TextUtils.isEmpty(config)) {
            return config;
        }
        return getAllowedCompatModePackagesFromSettings();
    }

    private java.lang.String getAllowedCompatModePackagesFromSettings() {
        return android.provider.Settings.Global.getString(getContext().getContentResolver(), "autofill_compat_mode_allowed_packages");
    }

    private java.util.Map<java.lang.String, java.lang.String[]> getAllowedCompatModePackages() {
        return getAllowedCompatModePackages(getAllowedCompatModePackagesFromDeviceConfig());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void send(com.android.internal.os.IResultReceiver receiver, int value) {
        try {
            receiver.send(value, (android.os.Bundle) null);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Error async reporting result to client: " + e);
        }
    }

    private void send(com.android.internal.os.IResultReceiver receiver, android.os.Bundle value) {
        try {
            receiver.send(0, value);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Error async reporting result to client: " + e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void send(com.android.internal.os.IResultReceiver receiver, java.lang.String value) {
        send(receiver, com.android.internal.util.SyncResultReceiver.bundleFor(value));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void send(com.android.internal.os.IResultReceiver receiver, java.lang.String[] value) {
        send(receiver, com.android.internal.util.SyncResultReceiver.bundleFor(value));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void send(com.android.internal.os.IResultReceiver receiver, android.os.Parcelable value) {
        send(receiver, com.android.internal.util.SyncResultReceiver.bundleFor(value));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void send(com.android.internal.os.IResultReceiver iResultReceiver, boolean z) {
        send(iResultReceiver, z ? 1 : 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void send(com.android.internal.os.IResultReceiver receiver, int value1, int value2) {
        try {
            receiver.send(value1, com.android.internal.util.SyncResultReceiver.bundleFor(value2));
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Error async reporting result to client: " + e);
        }
    }

    public boolean isPccClassificationFlagEnabled() {
        boolean z;
        synchronized (this.mFlagLock) {
            z = this.mPccClassificationEnabled;
        }
        return z;
    }

    public boolean isAutofillCredmanIntegrationEnabled() {
        boolean z;
        synchronized (this.mFlagLock) {
            z = this.mAutofillCredmanIntegrationEnabled;
        }
        return z;
    }

    public boolean preferProviderOverPcc() {
        boolean z;
        synchronized (this.mFlagLock) {
            z = this.mPccPreferProviderOverPcc;
        }
        return z;
    }

    public boolean shouldUsePccFallback() {
        boolean z;
        synchronized (this.mFlagLock) {
            z = this.mPccUseFallbackDetection;
        }
        return z;
    }

    public java.lang.String getPccProviderHints() {
        java.lang.String str;
        synchronized (this.mFlagLock) {
            str = this.mPccProviderHints;
        }
        return str;
    }

    public int getMaxInputLengthForAutofill() {
        int i;
        synchronized (this.mFlagLock) {
            i = this.mMaxInputLengthForAutofill;
        }
        return i;
    }

    public boolean getIsFillFieldsFromCurrentSessionOnly() {
        boolean z;
        synchronized (this.mFlagLock) {
            z = this.mIsFillFieldsFromCurrentSessionOnly;
        }
        return z;
    }

    static java.util.Map<java.lang.String, java.lang.String[]> getAllowedCompatModePackages(java.lang.String setting) {
        java.lang.String packageName;
        java.util.List<java.lang.String> urlBarIds;
        if (android.text.TextUtils.isEmpty(setting)) {
            return null;
        }
        android.util.ArrayMap<java.lang.String, java.lang.String[]> compatPackages = new android.util.ArrayMap<>();
        android.text.TextUtils.SimpleStringSplitter splitter = new android.text.TextUtils.SimpleStringSplitter(COMPAT_PACKAGE_DELIMITER);
        splitter.setString(setting);
        while (splitter.hasNext()) {
            java.lang.String packageBlock = splitter.next();
            int urlBlockIndex = packageBlock.indexOf(91);
            if (urlBlockIndex == -1) {
                packageName = packageBlock;
                urlBarIds = null;
            } else if (packageBlock.charAt(packageBlock.length() - 1) != ']') {
                android.util.Slog.w(TAG, "Ignoring entry '" + packageBlock + "' on '" + setting + "'because it does not end on '" + COMPAT_PACKAGE_URL_IDS_BLOCK_END + "'");
            } else {
                packageName = packageBlock.substring(0, urlBlockIndex);
                java.util.List<java.lang.String> urlBarIds2 = new java.util.ArrayList<>();
                java.lang.String urlBarIdsBlock = packageBlock.substring(urlBlockIndex + 1, packageBlock.length() - 1);
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "pkg:" + packageName + ": block:" + packageBlock + ": urls:" + urlBarIds2 + ": block:" + urlBarIdsBlock + ":");
                }
                android.text.TextUtils.SimpleStringSplitter splitter2 = new android.text.TextUtils.SimpleStringSplitter(COMPAT_PACKAGE_URL_IDS_DELIMITER);
                splitter2.setString(urlBarIdsBlock);
                while (splitter2.hasNext()) {
                    java.lang.String urlBarId = splitter2.next();
                    urlBarIds2.add(urlBarId);
                }
                urlBarIds = urlBarIds2;
            }
            if (urlBarIds == null) {
                compatPackages.put(packageName, null);
            } else {
                java.lang.String[] urlBarIdsArray = new java.lang.String[urlBarIds.size()];
                urlBarIds.toArray(urlBarIdsArray);
                compatPackages.put(packageName, urlBarIdsArray);
            }
        }
        return compatPackages;
    }

    public static int getPartitionMaxCount() {
        int i;
        synchronized (sLock) {
            i = sPartitionMaxCount;
        }
        return i;
    }

    public static int getVisibleDatasetsMaxCount() {
        int i;
        synchronized (sLock) {
            i = sVisibleDatasetsMaxCount;
        }
        return i;
    }

    private final class LocalService extends android.view.autofill.AutofillManagerInternal {
        private LocalService() {
        }

        public void onBackKeyPressed() {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(com.android.server.autofill.AutofillManagerService.TAG, "onBackKeyPressed()");
            }
            com.android.server.autofill.AutofillManagerService.this.mUi.hideAll(null);
            synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.getServiceForUserWithLocalBinderIdentityLocked(android.os.UserHandle.getCallingUserId());
                service.onBackKeyPressed();
            }
        }

        public android.content.AutofillOptions getAutofillOptions(java.lang.String packageName, long versionCode, int userId) {
            int loggingLevel;
            if (com.android.server.autofill.AutofillManagerService.this.verbose) {
                loggingLevel = 6;
            } else if (com.android.server.autofill.AutofillManagerService.this.debug) {
                loggingLevel = 2;
            } else {
                loggingLevel = 0;
            }
            boolean compatModeEnabled = com.android.server.autofill.AutofillManagerService.this.mAutofillCompatState.isCompatibilityModeRequested(packageName, versionCode, userId);
            android.content.AutofillOptions options = new android.content.AutofillOptions(loggingLevel, compatModeEnabled);
            com.android.server.autofill.AutofillManagerService.this.mAugmentedAutofillState.injectAugmentedAutofillInfo(options, userId, packageName);
            injectDisableAppInfo(options, userId, packageName);
            return options;
        }

        public boolean isAugmentedAutofillServiceForUser(int callingUid, int userId) {
            synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                com.android.server.autofill.AutofillManagerServiceImpl service = (com.android.server.autofill.AutofillManagerServiceImpl) com.android.server.autofill.AutofillManagerService.this.peekServiceForUserLocked(userId);
                if (service != null) {
                    return service.isAugmentedAutofillServiceForUserLocked(callingUid);
                }
                return false;
            }
        }

        private void injectDisableAppInfo(android.content.AutofillOptions options, int userId, java.lang.String packageName) {
            options.appDisabledExpiration = com.android.server.autofill.AutofillManagerService.this.mDisabledInfoCache.getAppDisabledExpiration(userId, packageName);
            options.disabledActivities = com.android.server.autofill.AutofillManagerService.this.mDisabledInfoCache.getAppDisabledActivities(userId, packageName);
        }
    }

    static final class PackageCompatState {
        private final long maxVersionCode;
        private final java.lang.String[] urlBarResourceIds;

        PackageCompatState(long maxVersionCode, java.lang.String[] urlBarResourceIds) {
            this.maxVersionCode = maxVersionCode;
            this.urlBarResourceIds = urlBarResourceIds;
        }

        public java.lang.String toString() {
            return "maxVersionCode=" + this.maxVersionCode + ", urlBarResourceIds=" + java.util.Arrays.toString(this.urlBarResourceIds);
        }
    }

    static final class DisabledInfoCache {
        private final java.lang.Object mLock = new java.lang.Object();
        private final android.util.SparseArray<com.android.server.autofill.AutofillManagerService.AutofillDisabledInfo> mCache = new android.util.SparseArray<>();

        DisabledInfoCache() {
        }

        void remove(int userId) {
            synchronized (this.mLock) {
                this.mCache.remove(userId);
            }
        }

        void addDisabledAppLocked(int userId, java.lang.String packageName, long expiration) {
            java.util.Objects.requireNonNull(packageName);
            synchronized (this.mLock) {
                com.android.server.autofill.AutofillManagerService.AutofillDisabledInfo info = getOrCreateAutofillDisabledInfoByUserIdLocked(userId);
                info.putDisableAppsLocked(packageName, expiration);
            }
        }

        void addDisabledActivityLocked(int userId, android.content.ComponentName componentName, long expiration) {
            java.util.Objects.requireNonNull(componentName);
            synchronized (this.mLock) {
                com.android.server.autofill.AutofillManagerService.AutofillDisabledInfo info = getOrCreateAutofillDisabledInfoByUserIdLocked(userId);
                info.putDisableActivityLocked(componentName, expiration);
            }
        }

        boolean isAutofillDisabledLocked(int userId, android.content.ComponentName componentName) {
            boolean disabled;
            java.util.Objects.requireNonNull(componentName);
            synchronized (this.mLock) {
                com.android.server.autofill.AutofillManagerService.AutofillDisabledInfo info = this.mCache.get(userId);
                disabled = info != null ? info.isAutofillDisabledLocked(componentName) : false;
            }
            return disabled;
        }

        long getAppDisabledExpiration(int userId, java.lang.String packageName) {
            java.lang.Long expiration;
            java.util.Objects.requireNonNull(packageName);
            synchronized (this.mLock) {
                com.android.server.autofill.AutofillManagerService.AutofillDisabledInfo info = this.mCache.get(userId);
                expiration = java.lang.Long.valueOf(info != null ? info.getAppDisabledExpirationLocked(packageName) : 0L);
            }
            return expiration.longValue();
        }

        android.util.ArrayMap<java.lang.String, java.lang.Long> getAppDisabledActivities(int userId, java.lang.String packageName) {
            android.util.ArrayMap<java.lang.String, java.lang.Long> disabledList;
            java.util.Objects.requireNonNull(packageName);
            synchronized (this.mLock) {
                com.android.server.autofill.AutofillManagerService.AutofillDisabledInfo info = this.mCache.get(userId);
                disabledList = info != null ? info.getAppDisabledActivitiesLocked(packageName) : null;
            }
            return disabledList;
        }

        void dump(int userId, java.lang.String prefix, java.io.PrintWriter pw) {
            synchronized (this.mLock) {
                com.android.server.autofill.AutofillManagerService.AutofillDisabledInfo info = this.mCache.get(userId);
                if (info != null) {
                    info.dumpLocked(prefix, pw);
                }
            }
        }

        private com.android.server.autofill.AutofillManagerService.AutofillDisabledInfo getOrCreateAutofillDisabledInfoByUserIdLocked(int userId) {
            com.android.server.autofill.AutofillManagerService.AutofillDisabledInfo info = this.mCache.get(userId);
            if (info == null) {
                com.android.server.autofill.AutofillManagerService.AutofillDisabledInfo info2 = new com.android.server.autofill.AutofillManagerService.AutofillDisabledInfo();
                this.mCache.put(userId, info2);
                return info2;
            }
            return info;
        }
    }

    private static final class AutofillDisabledInfo {
        private android.util.ArrayMap<android.content.ComponentName, java.lang.Long> mDisabledActivities;
        private android.util.ArrayMap<java.lang.String, java.lang.Long> mDisabledApps;

        private AutofillDisabledInfo() {
        }

        void putDisableAppsLocked(java.lang.String packageName, long expiration) {
            if (this.mDisabledApps == null) {
                this.mDisabledApps = new android.util.ArrayMap<>(1);
            }
            this.mDisabledApps.put(packageName, java.lang.Long.valueOf(expiration));
        }

        void putDisableActivityLocked(android.content.ComponentName componentName, long expiration) {
            if (this.mDisabledActivities == null) {
                this.mDisabledActivities = new android.util.ArrayMap<>(1);
            }
            this.mDisabledActivities.put(componentName, java.lang.Long.valueOf(expiration));
        }

        long getAppDisabledExpirationLocked(java.lang.String packageName) {
            java.lang.Long expiration;
            if (this.mDisabledApps == null || (expiration = this.mDisabledApps.get(packageName)) == null) {
                return 0L;
            }
            return expiration.longValue();
        }

        android.util.ArrayMap<java.lang.String, java.lang.Long> getAppDisabledActivitiesLocked(java.lang.String packageName) {
            if (this.mDisabledActivities != null) {
                int size = this.mDisabledActivities.size();
                android.util.ArrayMap<java.lang.String, java.lang.Long> disabledList = null;
                for (int i = 0; i < size; i++) {
                    android.content.ComponentName component = this.mDisabledActivities.keyAt(i);
                    if (packageName.equals(component.getPackageName())) {
                        if (disabledList == null) {
                            disabledList = new android.util.ArrayMap<>();
                        }
                        long expiration = this.mDisabledActivities.valueAt(i).longValue();
                        disabledList.put(component.flattenToShortString(), java.lang.Long.valueOf(expiration));
                    }
                }
                return disabledList;
            }
            return null;
        }

        boolean isAutofillDisabledLocked(android.content.ComponentName componentName) {
            java.lang.Long expiration;
            long elapsedTime = 0;
            if (this.mDisabledActivities != null) {
                elapsedTime = android.os.SystemClock.elapsedRealtime();
                java.lang.Long expiration2 = this.mDisabledActivities.get(componentName);
                if (expiration2 != null) {
                    if (expiration2.longValue() >= elapsedTime) {
                        return true;
                    }
                    if (com.android.server.autofill.Helper.sVerbose) {
                        android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "Removing " + componentName.toShortString() + " from disabled list");
                    }
                    this.mDisabledActivities.remove(componentName);
                }
            }
            java.lang.String packageName = componentName.getPackageName();
            if (this.mDisabledApps == null || (expiration = this.mDisabledApps.get(packageName)) == null) {
                return false;
            }
            if (elapsedTime == 0) {
                elapsedTime = android.os.SystemClock.elapsedRealtime();
            }
            if (expiration.longValue() >= elapsedTime) {
                return true;
            }
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "Removing " + packageName + " from disabled list");
            }
            this.mDisabledApps.remove(packageName);
            return false;
        }

        void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
            java.lang.String str;
            com.android.server.autofill.AutofillManagerService.AutofillDisabledInfo autofillDisabledInfo = this;
            java.lang.String str2 = prefix;
            pw.print(str2);
            pw.print("Disabled apps: ");
            java.lang.String str3 = ": ";
            if (autofillDisabledInfo.mDisabledApps == null) {
                pw.println("N/A");
                str = ": ";
            } else {
                int size = autofillDisabledInfo.mDisabledApps.size();
                pw.println(size);
                java.lang.StringBuilder builder = new java.lang.StringBuilder();
                long now = android.os.SystemClock.elapsedRealtime();
                int i = 0;
                while (i < size) {
                    java.lang.String packageName = autofillDisabledInfo.mDisabledApps.keyAt(i);
                    long expiration = autofillDisabledInfo.mDisabledApps.valueAt(i).longValue();
                    builder.append(str2).append(str2).append(i).append(". ").append(packageName).append(str3);
                    android.util.TimeUtils.formatDuration(expiration - now, builder);
                    builder.append('\n');
                    i++;
                    str3 = str3;
                }
                str = str3;
                pw.println(builder);
            }
            pw.print(str2);
            pw.print("Disabled activities: ");
            if (autofillDisabledInfo.mDisabledActivities == null) {
                pw.println("N/A");
                return;
            }
            int size2 = autofillDisabledInfo.mDisabledActivities.size();
            pw.println(size2);
            java.lang.StringBuilder builder2 = new java.lang.StringBuilder();
            long now2 = android.os.SystemClock.elapsedRealtime();
            int i2 = 0;
            while (i2 < size2) {
                android.content.ComponentName component = autofillDisabledInfo.mDisabledActivities.keyAt(i2);
                long expiration2 = autofillDisabledInfo.mDisabledActivities.valueAt(i2).longValue();
                builder2.append(str2).append(str2).append(i2).append(". ").append(component).append(str);
                android.util.TimeUtils.formatDuration(expiration2 - now2, builder2);
                builder2.append('\n');
                i2++;
                autofillDisabledInfo = this;
                str2 = prefix;
            }
            pw.println(builder2);
        }
    }

    static final class AutofillCompatState {
        private final java.lang.Object mLock = new java.lang.Object();
        private android.util.SparseArray<android.util.ArrayMap<java.lang.String, com.android.server.autofill.AutofillManagerService.PackageCompatState>> mUserSpecs;

        AutofillCompatState() {
        }

        boolean isCompatibilityModeRequested(java.lang.String packageName, long versionCode, int userId) {
            synchronized (this.mLock) {
                if (this.mUserSpecs == null) {
                    return false;
                }
                android.util.ArrayMap<java.lang.String, com.android.server.autofill.AutofillManagerService.PackageCompatState> userSpec = this.mUserSpecs.get(userId);
                if (userSpec == null) {
                    return false;
                }
                com.android.server.autofill.AutofillManagerService.PackageCompatState metadata = userSpec.get(packageName);
                if (metadata == null) {
                    return false;
                }
                return versionCode <= metadata.maxVersionCode;
            }
        }

        java.lang.String[] getUrlBarResourceIds(java.lang.String packageName, int userId) {
            synchronized (this.mLock) {
                if (this.mUserSpecs == null) {
                    return null;
                }
                android.util.ArrayMap<java.lang.String, com.android.server.autofill.AutofillManagerService.PackageCompatState> userSpec = this.mUserSpecs.get(userId);
                if (userSpec == null) {
                    return null;
                }
                com.android.server.autofill.AutofillManagerService.PackageCompatState metadata = userSpec.get(packageName);
                if (metadata == null) {
                    return null;
                }
                return metadata.urlBarResourceIds;
            }
        }

        void addCompatibilityModeRequest(java.lang.String packageName, long versionCode, java.lang.String[] urlBarResourceIds, int userId) {
            synchronized (this.mLock) {
                if (this.mUserSpecs == null) {
                    this.mUserSpecs = new android.util.SparseArray<>();
                }
                android.util.ArrayMap<java.lang.String, com.android.server.autofill.AutofillManagerService.PackageCompatState> userSpec = this.mUserSpecs.get(userId);
                if (userSpec == null) {
                    userSpec = new android.util.ArrayMap<>();
                    this.mUserSpecs.put(userId, userSpec);
                }
                userSpec.put(packageName, new com.android.server.autofill.AutofillManagerService.PackageCompatState(versionCode, urlBarResourceIds));
            }
        }

        void removeCompatibilityModeRequests(int userId) {
            synchronized (this.mLock) {
                if (this.mUserSpecs != null) {
                    this.mUserSpecs.remove(userId);
                    if (this.mUserSpecs.size() <= 0) {
                        this.mUserSpecs = null;
                    }
                }
            }
        }

        void reset(int userId) {
            synchronized (this.mLock) {
                if (this.mUserSpecs != null) {
                    this.mUserSpecs.delete(userId);
                    int newSize = this.mUserSpecs.size();
                    if (newSize == 0) {
                        if (com.android.server.autofill.Helper.sVerbose) {
                            android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "reseting mUserSpecs");
                        }
                        this.mUserSpecs = null;
                    } else if (com.android.server.autofill.Helper.sVerbose) {
                        android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "mUserSpecs down to " + newSize);
                    }
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
            synchronized (this.mLock) {
                if (this.mUserSpecs == null) {
                    pw.println("N/A");
                    return;
                }
                pw.println();
                java.lang.String prefix2 = prefix + "  ";
                for (int i = 0; i < this.mUserSpecs.size(); i++) {
                    int user = this.mUserSpecs.keyAt(i);
                    pw.print(prefix);
                    pw.print("User: ");
                    pw.println(user);
                    android.util.ArrayMap<java.lang.String, com.android.server.autofill.AutofillManagerService.PackageCompatState> perUser = this.mUserSpecs.valueAt(i);
                    for (int j = 0; j < perUser.size(); j++) {
                        java.lang.String packageName = perUser.keyAt(j);
                        com.android.server.autofill.AutofillManagerService.PackageCompatState state = perUser.valueAt(j);
                        pw.print(prefix2);
                        pw.print(packageName);
                        pw.print(": ");
                        pw.println(state);
                    }
                }
            }
        }
    }

    static final class AugmentedAutofillState extends com.android.internal.infra.GlobalWhitelistState {
        private final android.util.SparseArray<java.lang.String> mServicePackages = new android.util.SparseArray<>();
        private final android.util.SparseBooleanArray mTemporaryServices = new android.util.SparseBooleanArray();

        AugmentedAutofillState() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setServiceInfo(int userId, java.lang.String serviceName, boolean isTemporary) {
            synchronized (this.mGlobalWhitelistStateLock) {
                if (isTemporary) {
                    this.mTemporaryServices.put(userId, true);
                } else {
                    this.mTemporaryServices.delete(userId);
                }
                if (serviceName != null) {
                    android.content.ComponentName componentName = android.content.ComponentName.unflattenFromString(serviceName);
                    if (componentName == null) {
                        android.util.Slog.w(com.android.server.autofill.AutofillManagerService.TAG, "setServiceInfo(): invalid name: " + serviceName);
                        this.mServicePackages.remove(userId);
                    } else {
                        this.mServicePackages.put(userId, componentName.getPackageName());
                    }
                } else {
                    this.mServicePackages.remove(userId);
                }
            }
        }

        public void injectAugmentedAutofillInfo(android.content.AutofillOptions options, int userId, java.lang.String packageName) {
            synchronized (this.mGlobalWhitelistStateLock) {
                if (this.mWhitelisterHelpers == null) {
                    return;
                }
                com.android.internal.infra.WhitelistHelper helper = (com.android.internal.infra.WhitelistHelper) this.mWhitelisterHelpers.get(userId);
                if (helper != null) {
                    options.augmentedAutofillEnabled = helper.isWhitelisted(packageName);
                    options.whitelistedActivitiesForAugmentedAutofill = helper.getWhitelistedComponents(packageName);
                }
            }
        }

        public boolean isWhitelisted(int userId, android.content.ComponentName componentName) {
            synchronized (this.mGlobalWhitelistStateLock) {
                if (!super.isWhitelisted(userId, componentName)) {
                    return false;
                }
                if (android.os.Build.IS_USER && this.mTemporaryServices.get(userId)) {
                    java.lang.String packageName = componentName.getPackageName();
                    if (!packageName.equals(this.mServicePackages.get(userId))) {
                        android.util.Slog.w(com.android.server.autofill.AutofillManagerService.TAG, "Ignoring package " + packageName + " for augmented autofill while using temporary service " + this.mServicePackages.get(userId));
                        return false;
                    }
                }
                return true;
            }
        }

        public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
            super.dump(prefix, pw);
            synchronized (this.mGlobalWhitelistStateLock) {
                if (this.mServicePackages.size() > 0) {
                    pw.print(prefix);
                    pw.print("Service packages: ");
                    pw.println(this.mServicePackages);
                }
                if (this.mTemporaryServices.size() > 0) {
                    pw.print(prefix);
                    pw.print("Temp services: ");
                    pw.println(this.mTemporaryServices);
                }
            }
        }
    }

    final class AutoFillManagerServiceStub extends android.view.autofill.IAutoFillManager.Stub {
        AutoFillManagerServiceStub() {
        }

        public void addClient(android.view.autofill.IAutoFillManagerClient client, android.content.ComponentName componentName, int userId, com.android.internal.os.IResultReceiver receiver, boolean credmanRequested) {
            try {
                try {
                    synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                        int enabledFlags = com.android.server.autofill.AutofillManagerService.this.getServiceForUserWithLocalBinderIdentityLocked(userId).addClientLocked(client, componentName, credmanRequested);
                        flags = enabledFlags != 0 ? 0 | enabledFlags : 0;
                        if (com.android.server.autofill.Helper.sDebug) {
                            flags |= 2;
                        }
                        if (com.android.server.autofill.Helper.sVerbose) {
                            flags |= 4;
                        }
                    }
                } catch (java.lang.Exception ex) {
                    android.util.Log.wtf(com.android.server.autofill.AutofillManagerService.TAG, "addClient(): failed " + ex.toString(), ex);
                }
            } finally {
                com.android.server.autofill.AutofillManagerService.this.send(receiver, flags);
            }
        }

        public void removeClient(android.view.autofill.IAutoFillManagerClient client, int userId) {
            synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                com.android.server.autofill.AutofillManagerServiceImpl service = (com.android.server.autofill.AutofillManagerServiceImpl) com.android.server.autofill.AutofillManagerService.this.peekServiceForUserLocked(userId);
                if (service != null) {
                    service.removeClientLocked(client);
                } else if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "removeClient(): no service for " + userId);
                }
            }
        }

        public void setAuthenticationResult(android.os.Bundle data, int sessionId, int authenticationId, int userId) {
            synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.getServiceForUserWithLocalBinderIdentityLocked(userId);
                service.setAuthenticationResultLocked(data, sessionId, authenticationId, getCallingUid());
            }
        }

        public void setHasCallback(int sessionId, int userId, boolean hasIt) {
            synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.getServiceForUserWithLocalBinderIdentityLocked(userId);
                service.setHasCallback(sessionId, getCallingUid(), hasIt);
            }
        }

        public void startSession(android.os.IBinder activityToken, android.os.IBinder clientCallback, android.view.autofill.AutofillId autofillId, android.graphics.Rect bounds, android.view.autofill.AutofillValue value, int userId, boolean hasCallback, int flags, android.content.ComponentName clientActivity, boolean compatMode, com.android.internal.os.IResultReceiver receiver) throws java.lang.Throwable {
            java.util.Objects.requireNonNull(activityToken, "activityToken");
            java.util.Objects.requireNonNull(clientCallback, "clientCallback");
            java.util.Objects.requireNonNull(autofillId, "autofillId");
            java.util.Objects.requireNonNull(clientActivity, "clientActivity");
            java.lang.String packageName = (java.lang.String) java.util.Objects.requireNonNull(clientActivity.getPackageName());
            com.android.internal.util.Preconditions.checkArgument(userId == android.os.UserHandle.getUserId(getCallingUid()), "userId");
            try {
                com.android.server.autofill.AutofillManagerService.this.getContext().getPackageManager().getPackageInfoAsUser(packageName, 0, userId);
                int taskId = com.android.server.autofill.AutofillManagerService.this.mAm.getTaskIdForActivity(activityToken, false);
                synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                    try {
                        try {
                            com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.getServiceForUserWithLocalBinderIdentityLocked(userId);
                            long result = service.startSessionLocked(activityToken, taskId, getCallingUid(), clientCallback, autofillId, bounds, value, hasCallback, clientActivity, compatMode, com.android.server.autofill.AutofillManagerService.this.mAllowInstantService, flags);
                            int sessionId = (int) result;
                            int resultFlags = (int) (result >> 32);
                            if (resultFlags != 0) {
                                com.android.server.autofill.AutofillManagerService.this.send(receiver, sessionId, resultFlags);
                            } else {
                                com.android.server.autofill.AutofillManagerService.this.send(receiver, sessionId);
                            }
                        } catch (java.lang.Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                throw new java.lang.IllegalArgumentException(packageName + " is not a valid package", e);
            }
        }

        public void getFillEventHistory(com.android.internal.os.IResultReceiver receiver) throws android.os.RemoteException {
            android.service.autofill.FillEventHistory fillEventHistory = null;
            int userId = android.os.UserHandle.getCallingUserId();
            try {
                try {
                    synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                        com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                        if (service != null) {
                            fillEventHistory = service.getFillEventHistory(getCallingUid());
                        } else if (com.android.server.autofill.Helper.sVerbose) {
                            android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "getFillEventHistory(): no service for " + userId);
                        }
                    }
                } catch (java.lang.Exception ex) {
                    android.util.Log.wtf(com.android.server.autofill.AutofillManagerService.TAG, "getFillEventHistory(): failed " + ex.toString());
                }
            } finally {
                com.android.server.autofill.AutofillManagerService.this.send(receiver, fillEventHistory);
            }
        }

        public void getUserData(com.android.internal.os.IResultReceiver receiver) throws android.os.RemoteException {
            android.service.autofill.UserData userData = null;
            int userId = android.os.UserHandle.getCallingUserId();
            try {
                try {
                    synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                        com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                        if (service != null) {
                            userData = service.getUserData(getCallingUid());
                        } else if (com.android.server.autofill.Helper.sVerbose) {
                            android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "getUserData(): no service for " + userId);
                        }
                    }
                } catch (java.lang.Exception ex) {
                    android.util.Log.wtf(com.android.server.autofill.AutofillManagerService.TAG, "getUserData(): failed " + ex.toString());
                }
            } finally {
                com.android.server.autofill.AutofillManagerService.this.send(receiver, userData);
            }
        }

        public void getUserDataId(com.android.internal.os.IResultReceiver receiver) throws android.os.RemoteException {
            android.service.autofill.UserData userData = null;
            int userId = android.os.UserHandle.getCallingUserId();
            try {
                try {
                    synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                        com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                        if (service != null) {
                            userData = service.getUserData(getCallingUid());
                        } else if (com.android.server.autofill.Helper.sVerbose) {
                            android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "getUserDataId(): no service for " + userId);
                        }
                    }
                } catch (java.lang.Exception ex) {
                    android.util.Log.wtf(com.android.server.autofill.AutofillManagerService.TAG, "getUserDataId(): failed " + ex.toString());
                    if (userData != null) {
                    }
                }
                if (userData != null) {
                    userDataId = userData.getId();
                }
                com.android.server.autofill.AutofillManagerService.this.send(receiver, userDataId);
            } catch (java.lang.Throwable th) {
                userDataId = userData != null ? userData.getId() : null;
                com.android.server.autofill.AutofillManagerService.this.send(receiver, userDataId);
                throw th;
            }
        }

        public void setUserData(android.service.autofill.UserData userData) throws android.os.RemoteException {
            int userId = android.os.UserHandle.getCallingUserId();
            synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                if (service != null) {
                    service.setUserData(getCallingUid(), userData);
                } else if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "setUserData(): no service for " + userId);
                }
            }
        }

        public void isFieldClassificationEnabled(com.android.internal.os.IResultReceiver receiver) throws android.os.RemoteException {
            boolean enabled = false;
            int userId = android.os.UserHandle.getCallingUserId();
            try {
                try {
                    synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                        com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                        if (service != null) {
                            enabled = service.isFieldClassificationEnabled(getCallingUid());
                        } else if (com.android.server.autofill.Helper.sVerbose) {
                            android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "isFieldClassificationEnabled(): no service for " + userId);
                        }
                    }
                } catch (java.lang.Exception ex) {
                    android.util.Log.wtf(com.android.server.autofill.AutofillManagerService.TAG, "isFieldClassificationEnabled(): failed " + ex.toString());
                }
            } finally {
                com.android.server.autofill.AutofillManagerService.this.send(receiver, enabled);
            }
        }

        public void getDefaultFieldClassificationAlgorithm(com.android.internal.os.IResultReceiver receiver) throws android.os.RemoteException {
            java.lang.String algorithm = null;
            int userId = android.os.UserHandle.getCallingUserId();
            try {
                try {
                    synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                        com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                        if (service != null) {
                            algorithm = service.getDefaultFieldClassificationAlgorithm(getCallingUid());
                        } else if (com.android.server.autofill.Helper.sVerbose) {
                            android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "getDefaultFcAlgorithm(): no service for " + userId);
                        }
                    }
                } catch (java.lang.Exception ex) {
                    android.util.Log.wtf(com.android.server.autofill.AutofillManagerService.TAG, "getDefaultFieldClassificationAlgorithm(): failed " + ex.toString());
                }
            } finally {
                com.android.server.autofill.AutofillManagerService.this.send(receiver, algorithm);
            }
        }

        public void setAugmentedAutofillWhitelist(java.util.List<java.lang.String> packages, java.util.List<android.content.ComponentName> activities, com.android.internal.os.IResultReceiver receiver) throws android.os.RemoteException {
            com.android.server.autofill.AutofillManagerService autofillManagerService;
            boolean ok = false;
            int userId = android.os.UserHandle.getCallingUserId();
            try {
                try {
                    synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                        com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                        if (service != null) {
                            ok = service.setAugmentedAutofillWhitelistLocked(packages, activities, getCallingUid());
                        } else if (com.android.server.autofill.Helper.sVerbose) {
                            android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "setAugmentedAutofillWhitelist(): no service for " + userId);
                        }
                    }
                    autofillManagerService = com.android.server.autofill.AutofillManagerService.this;
                } catch (java.lang.Exception ex) {
                    android.util.Log.wtf(com.android.server.autofill.AutofillManagerService.TAG, "setAugmentedAutofillWhitelist(): failed " + ex.toString());
                    autofillManagerService = com.android.server.autofill.AutofillManagerService.this;
                    if (!ok) {
                    }
                }
                if (!ok) {
                    i = -1;
                }
                autofillManagerService.send(receiver, i);
            } catch (java.lang.Throwable th) {
                com.android.server.autofill.AutofillManagerService.this.send(receiver, ok ? 0 : -1);
                throw th;
            }
        }

        public void getAvailableFieldClassificationAlgorithms(com.android.internal.os.IResultReceiver receiver) throws android.os.RemoteException {
            java.lang.String[] algorithms = null;
            int userId = android.os.UserHandle.getCallingUserId();
            try {
                synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                    com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                    if (service != null) {
                        algorithms = service.getAvailableFieldClassificationAlgorithms(getCallingUid());
                    } else if (com.android.server.autofill.Helper.sVerbose) {
                        android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "getAvailableFcAlgorithms(): no service for " + userId);
                    }
                }
            } catch (java.lang.Exception ex) {
                android.util.Log.wtf(com.android.server.autofill.AutofillManagerService.TAG, "getAvailableFieldClassificationAlgorithms(): failed " + ex.toString());
            } finally {
                com.android.server.autofill.AutofillManagerService.this.send(receiver, algorithms);
            }
        }

        public void getAutofillServiceComponentName(com.android.internal.os.IResultReceiver receiver) throws android.os.RemoteException {
            android.content.ComponentName componentName = null;
            int userId = android.os.UserHandle.getCallingUserId();
            try {
                try {
                    synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                        com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                        if (service != null) {
                            componentName = service.getServiceComponentName();
                        } else if (com.android.server.autofill.Helper.sVerbose) {
                            android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "getAutofillServiceComponentName(): no service for " + userId);
                        }
                    }
                } catch (java.lang.Exception ex) {
                    android.util.Log.wtf(com.android.server.autofill.AutofillManagerService.TAG, "getAutofillServiceComponentName(): failed " + ex.toString());
                }
            } finally {
                com.android.server.autofill.AutofillManagerService.this.send(receiver, componentName);
            }
        }

        public void restoreSession(int sessionId, android.os.IBinder activityToken, android.os.IBinder appCallback, com.android.internal.os.IResultReceiver receiver) throws android.os.RemoteException {
            boolean restored = false;
            int userId = android.os.UserHandle.getCallingUserId();
            try {
                try {
                    java.util.Objects.requireNonNull(activityToken, "activityToken");
                    java.util.Objects.requireNonNull(appCallback, "appCallback");
                    synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                        com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                        if (service != null) {
                            restored = service.restoreSession(sessionId, getCallingUid(), activityToken, appCallback);
                        } else if (com.android.server.autofill.Helper.sVerbose) {
                            android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "restoreSession(): no service for " + userId);
                        }
                    }
                } catch (java.lang.Exception ex) {
                    android.util.Log.wtf(com.android.server.autofill.AutofillManagerService.TAG, "restoreSession(): failed " + ex.toString());
                }
            } finally {
                com.android.server.autofill.AutofillManagerService.this.send(receiver, restored);
            }
        }

        public void updateSession(int sessionId, android.view.autofill.AutofillId autoFillId, android.graphics.Rect bounds, android.view.autofill.AutofillValue value, int action, int flags, int userId) {
            synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                if (service != null) {
                    service.updateSessionLocked(sessionId, getCallingUid(), autoFillId, bounds, value, action, flags);
                } else if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "updateSession(): no service for " + userId);
                }
            }
        }

        public void setAutofillFailure(int sessionId, java.util.List<android.view.autofill.AutofillId> ids, int userId) {
            synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                if (service != null) {
                    service.setAutofillFailureLocked(sessionId, getCallingUid(), ids);
                } else if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "setAutofillFailure(): no service for " + userId);
                }
            }
        }

        public void setViewAutofilled(int sessionId, android.view.autofill.AutofillId id, int userId) {
            synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                if (service != null) {
                    service.setViewAutofilledLocked(sessionId, getCallingUid(), id);
                } else if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "setAutofillFailure(): no service for " + userId);
                }
            }
        }

        public void finishSession(int sessionId, int userId, int commitReason) {
            synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                if (service != null) {
                    service.finishSessionLocked(sessionId, getCallingUid(), commitReason);
                } else if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "finishSession(): no service for " + userId);
                }
            }
        }

        public void cancelSession(int sessionId, int userId) {
            synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                if (service != null) {
                    service.cancelSessionLocked(sessionId, getCallingUid());
                } else if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "cancelSession(): no service for " + userId);
                }
            }
        }

        public void disableOwnedAutofillServices(int userId) {
            synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                if (service != null) {
                    service.disableOwnedAutofillServicesLocked(android.os.Binder.getCallingUid());
                } else if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(com.android.server.autofill.AutofillManagerService.TAG, "cancelSession(): no service for " + userId);
                }
            }
        }

        public void isServiceSupported(int userId, com.android.internal.os.IResultReceiver receiver) {
            boolean supported = false;
            try {
                try {
                    synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                        supported = !com.android.server.autofill.AutofillManagerService.this.isDisabledLocked(userId);
                    }
                } catch (java.lang.Exception ex) {
                    android.util.Log.wtf(com.android.server.autofill.AutofillManagerService.TAG, "isServiceSupported(): failed " + ex.toString());
                }
            } finally {
                com.android.server.autofill.AutofillManagerService.this.send(receiver, supported);
            }
        }

        public void isServiceEnabled(int userId, java.lang.String packageName, com.android.internal.os.IResultReceiver receiver) {
            boolean enabled = false;
            try {
                try {
                    synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                        com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(userId);
                        enabled = java.util.Objects.equals(packageName, service.getServicePackageName());
                    }
                } catch (java.lang.Exception ex) {
                    android.util.Log.wtf(com.android.server.autofill.AutofillManagerService.TAG, "isServiceEnabled(): failed " + ex.toString());
                }
            } finally {
                com.android.server.autofill.AutofillManagerService.this.send(receiver, enabled);
            }
        }

        public void onPendingSaveUi(int operation, android.os.IBinder token) {
            java.util.Objects.requireNonNull(token, "token");
            boolean z = true;
            if (operation != 1 && operation != 2) {
                z = false;
            }
            com.android.internal.util.Preconditions.checkArgument(z, "invalid operation: %d", new java.lang.Object[]{java.lang.Integer.valueOf(operation)});
            synchronized (com.android.server.autofill.AutofillManagerService.this.mLock) {
                com.android.server.autofill.AutofillManagerServiceImpl service = com.android.server.autofill.AutofillManagerService.this.peekServiceForUserWithLocalBinderIdentityLocked(android.os.UserHandle.getCallingUserId());
                if (service != null) {
                    service.onPendingSaveUi(operation, token);
                }
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:21:0x0041  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void dump(java.io.FileDescriptor r11, java.io.PrintWriter r12, java.lang.String[] r13) {
            /*
                Method dump skipped, instruction units count: 536
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.autofill.AutofillManagerService.AutoFillManagerServiceStub.dump(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
            new com.android.server.autofill.AutofillManagerServiceShellCommand(com.android.server.autofill.AutofillManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }
    }
}
