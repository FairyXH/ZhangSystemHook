package com.android.server.contentcapture;

/* JADX INFO: loaded from: classes.dex */
public class ContentCaptureManagerService extends com.android.server.infra.AbstractMasterSystemService<com.android.server.contentcapture.ContentCaptureManagerService, com.android.server.contentcapture.ContentCapturePerUserService> {
    private static final java.lang.String CONTENT_PROTECTION_GROUP_CONFIG_SEPARATOR_GROUP = ";";
    private static final java.lang.String CONTENT_PROTECTION_GROUP_CONFIG_SEPARATOR_VALUE = ",";
    private static final int DATA_SHARE_BYTE_BUFFER_LENGTH = 1024;
    private static final int EVENT__DATA_SHARE_ERROR_CONCURRENT_REQUEST = 14;
    private static final int EVENT__DATA_SHARE_ERROR_TIMEOUT_INTERRUPTED = 15;
    private static final int EVENT__DATA_SHARE_WRITE_FINISHED = 9;
    private static final int MAX_CONCURRENT_FILE_SHARING_REQUESTS = 10;
    private static final int MAX_DATA_SHARE_FILE_DESCRIPTORS_TTL_MS = 300000;
    private static final int MAX_TEMP_SERVICE_DURATION_MS = 120000;
    static final java.lang.String RECEIVER_BUNDLE_EXTRA_SESSIONS = "sessions";
    private static final java.lang.String TAG = com.android.server.contentcapture.ContentCaptureManagerService.class.getSimpleName();
    private boolean activityStartAssistDataEnabled;
    private android.app.ActivityManagerInternal mAm;
    private final android.os.RemoteCallbackList<android.view.contentcapture.IContentCaptureOptionsCallback> mCallbacks;
    private final com.android.server.contentcapture.ContentCaptureManagerService.ContentCaptureManagerServiceStub mContentCaptureManagerServiceStub;
    private com.android.server.contentprotection.ContentProtectionAllowlistManager mContentProtectionAllowlistManager;
    private com.android.server.contentprotection.ContentProtectionConsentManager mContentProtectionConsentManager;
    private android.content.ComponentName mContentProtectionServiceComponentName;
    private final java.util.concurrent.Executor mDataShareExecutor;
    long mDevCfgContentProtectionAllowlistDelayMs;
    long mDevCfgContentProtectionAllowlistTimeoutMs;
    long mDevCfgContentProtectionAutoDisconnectTimeoutMs;
    int mDevCfgContentProtectionBufferSize;
    java.util.List<java.util.List<java.lang.String>> mDevCfgContentProtectionOptionalGroups;
    int mDevCfgContentProtectionOptionalGroupsThreshold;
    java.util.List<java.util.List<java.lang.String>> mDevCfgContentProtectionRequiredGroups;
    boolean mDevCfgDisableFlushForViewTreeAppearing;
    boolean mDevCfgEnableContentProtectionReceiver;
    int mDevCfgIdleFlushingFrequencyMs;
    int mDevCfgIdleUnbindTimeoutMs;
    int mDevCfgLogHistorySize;
    int mDevCfgLoggingLevel;
    int mDevCfgMaxBufferSize;
    int mDevCfgTextChangeFlushingFrequencyMs;
    private boolean mDisabledByDeviceConfig;
    private android.util.SparseBooleanArray mDisabledBySettings;
    final com.android.server.contentcapture.ContentCaptureManagerService.GlobalContentCaptureOptions mGlobalContentCaptureOptions;
    private final android.os.Handler mHandler;
    private final com.android.server.contentcapture.ContentCaptureManagerService.LocalService mLocalService;
    private final java.util.Set<java.lang.String> mPackagesWithShareRequests;
    final android.util.LocalLog mRequestsHistory;

    public ContentCaptureManagerService(android.content.Context context) {
        super(context, new com.android.server.infra.FrameworkResourcesServiceNameResolver(context, android.R.string.config_defaultDndAccessPackages), "no_content_capture", 1);
        this.mLocalService = new com.android.server.contentcapture.ContentCaptureManagerService.LocalService();
        this.mContentCaptureManagerServiceStub = new com.android.server.contentcapture.ContentCaptureManagerService.ContentCaptureManagerServiceStub();
        this.mDataShareExecutor = java.util.concurrent.Executors.newCachedThreadPool();
        this.mHandler = new android.os.Handler(android.os.Looper.getMainLooper());
        this.mPackagesWithShareRequests = new java.util.HashSet();
        this.mCallbacks = new android.os.RemoteCallbackList<>();
        this.mGlobalContentCaptureOptions = new com.android.server.contentcapture.ContentCaptureManagerService.GlobalContentCaptureOptions();
        this.mDevCfgContentProtectionRequiredGroups = android.view.contentcapture.ContentCaptureManager.DEFAULT_CONTENT_PROTECTION_REQUIRED_GROUPS;
        this.mDevCfgContentProtectionOptionalGroups = android.view.contentcapture.ContentCaptureManager.DEFAULT_CONTENT_PROTECTION_OPTIONAL_GROUPS;
        android.provider.DeviceConfig.addOnPropertiesChangedListener("content_capture", android.app.ActivityThread.currentApplication().getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$$ExternalSyntheticLambda1
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.lambda$new$0(properties);
            }
        });
        setDeviceConfigProperties();
        if (this.mDevCfgLogHistorySize > 0) {
            if (this.debug) {
                android.util.Slog.d(TAG, "log history size: " + this.mDevCfgLogHistorySize);
            }
            this.mRequestsHistory = new android.util.LocalLog(this.mDevCfgLogHistorySize);
        } else {
            if (this.debug) {
                android.util.Slog.d(TAG, "disabled log history because size is " + this.mDevCfgLogHistorySize);
            }
            this.mRequestsHistory = null;
        }
        java.util.List<android.content.pm.UserInfo> users = getSupportedUsers();
        for (int i = 0; i < users.size(); i++) {
            int userId = users.get(i).id;
            boolean disabled = !isEnabledBySettings(userId);
            if (disabled) {
                android.util.Slog.i(TAG, "user " + userId + " disabled by settings");
                if (this.mDisabledBySettings == null) {
                    this.mDisabledBySettings = new android.util.SparseBooleanArray(1);
                }
                this.mDisabledBySettings.put(userId, true);
            }
            this.mGlobalContentCaptureOptions.setServiceInfo(userId, this.mServiceNameResolver.getServiceName(userId), this.mServiceNameResolver.isTemporary(userId));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public com.android.server.contentcapture.ContentCapturePerUserService newServiceLocked(int resolvedUserId, boolean disabled) {
        return new com.android.server.contentcapture.ContentCapturePerUserService(this, this.mLock, disabled, resolvedUserId);
    }

    @Override // com.android.server.SystemService
    public boolean isUserSupported(com.android.server.SystemService.TargetUser user) {
        return user.isFull() || user.isProfile();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("content_capture", this.mContentCaptureManagerServiceStub);
        publishLocalService(com.android.server.contentcapture.ContentCaptureManagerInternal.class, this.mLocalService);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public void onServiceRemoved(com.android.server.contentcapture.ContentCapturePerUserService service, int userId) {
        service.destroyLocked();
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageUpdatingLocked(int userId) {
        com.android.server.contentcapture.ContentCapturePerUserService service = getServiceForUserLocked(userId);
        if (service != null) {
            service.onPackageUpdatingLocked();
        }
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServicePackageUpdatedLocked(int userId) {
        com.android.server.contentcapture.ContentCapturePerUserService service = getServiceForUserLocked(userId);
        if (service != null) {
            service.onPackageUpdatedLocked();
        }
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onServiceNameChanged(int userId, java.lang.String serviceName, boolean isTemporary) {
        this.mGlobalContentCaptureOptions.setServiceInfo(userId, serviceName, isTemporary);
        super.onServiceNameChanged(userId, serviceName, isTemporary);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void enforceCallingPermissionForManagement() {
        getContext().enforceCallingPermission("android.permission.MANAGE_CONTENT_CAPTURE", TAG);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected int getMaximumTemporaryServiceDurationMs() {
        return 120000;
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void registerForExtraSettingsChanges(android.content.ContentResolver resolver, android.database.ContentObserver observer) {
        resolver.registerContentObserver(android.provider.Settings.Secure.getUriFor("content_capture_enabled"), false, observer, -1);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void onSettingsChanged(int userId, java.lang.String property) {
        byte b;
        switch (property.hashCode()) {
            case -322385022:
                if (property.equals("content_capture_enabled")) {
                    b = 0;
                    break;
                }
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                setContentCaptureFeatureEnabledBySettingsForUser(userId, isEnabledBySettings(userId));
                break;
            default:
                android.util.Slog.w(TAG, "Unexpected property (" + property + "); updating cache instead");
                break;
        }
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected boolean isDisabledLocked(int userId) {
        return this.mDisabledByDeviceConfig || isDisabledBySettingsLocked(userId) || super.isDisabledLocked(userId);
    }

    @Override // com.android.server.infra.AbstractMasterSystemService
    protected void assertCalledByPackageOwner(java.lang.String packageName) {
        try {
            super.assertCalledByPackageOwner(packageName);
        } catch (java.lang.SecurityException e) {
            int callingUid = android.os.Binder.getCallingUid();
            android.service.voice.VoiceInteractionManagerInternal.HotwordDetectionServiceIdentity hotwordDetectionServiceIdentity = ((android.service.voice.VoiceInteractionManagerInternal) com.android.server.LocalServices.getService(android.service.voice.VoiceInteractionManagerInternal.class)).getHotwordDetectionServiceIdentity();
            if (callingUid != hotwordDetectionServiceIdentity.getIsolatedUid()) {
                super.assertCalledByPackageOwner(packageName);
                return;
            }
            java.lang.String[] packages = getContext().getPackageManager().getPackagesForUid(hotwordDetectionServiceIdentity.getOwnerUid());
            if (packages != null) {
                for (java.lang.String candidate : packages) {
                    if (packageName.equals(candidate)) {
                        return;
                    }
                }
            }
            throw e;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDisabledBySettingsLocked(int userId) {
        return this.mDisabledBySettings != null && this.mDisabledBySettings.get(userId);
    }

    private boolean isEnabledBySettings(int userId) {
        boolean enabled = android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "content_capture_enabled", 1, userId) == 1;
        return enabled;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00de  */
    /* JADX INFO: renamed from: onDeviceConfigChange, reason: merged with bridge method [inline-methods] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void lambda$new$0(android.provider.DeviceConfig.Properties r6) {
        /*
            Method dump skipped, instruction units count: 384
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.contentcapture.ContentCaptureManagerService.lambda$new$0(android.provider.DeviceConfig$Properties):void");
    }

    protected void setFineTuneParamsFromDeviceConfig() {
        boolean enableContentProtectionReceiverOld;
        boolean enableContentProtectionReceiverNew;
        java.lang.String contentProtectionRequiredGroupsConfig;
        java.lang.String contentProtectionOptionalGroupsConfig;
        int contentProtectionOptionalGroupsThreshold;
        long contentProtectionAllowlistDelayMs;
        long contentProtectionAllowlistTimeoutMs;
        com.android.server.contentprotection.ContentProtectionAllowlistManager contentProtectionAllowlistManagerOld;
        com.android.server.contentprotection.ContentProtectionConsentManager contentProtectionConsentManagerNew;
        android.content.ComponentName contentProtectionServiceComponentNameNew;
        synchronized (this.mLock) {
            this.mDevCfgMaxBufferSize = android.provider.DeviceConfig.getInt("content_capture", "max_buffer_size", 500);
            this.mDevCfgIdleFlushingFrequencyMs = android.provider.DeviceConfig.getInt("content_capture", "idle_flush_frequency", 5000);
            this.mDevCfgTextChangeFlushingFrequencyMs = android.provider.DeviceConfig.getInt("content_capture", "text_change_flush_frequency", 1000);
            this.mDevCfgLogHistorySize = android.provider.DeviceConfig.getInt("content_capture", "log_history_size", 20);
            this.mDevCfgIdleUnbindTimeoutMs = android.provider.DeviceConfig.getInt("content_capture", "idle_unbind_timeout", 0);
            this.mDevCfgDisableFlushForViewTreeAppearing = android.provider.DeviceConfig.getBoolean("content_capture", "disable_flush_for_view_tree_appearing", false);
            enableContentProtectionReceiverOld = this.mDevCfgEnableContentProtectionReceiver;
            enableContentProtectionReceiverNew = getDeviceConfigEnableContentProtectionReceiver();
            this.mDevCfgContentProtectionBufferSize = android.provider.DeviceConfig.getInt("content_capture", "content_protection_buffer_size", 150);
            contentProtectionRequiredGroupsConfig = android.provider.DeviceConfig.getString("content_capture", "content_protection_required_groups_config", "");
            contentProtectionOptionalGroupsConfig = android.provider.DeviceConfig.getString("content_capture", "content_protection_optional_groups_config", "");
            contentProtectionOptionalGroupsThreshold = android.provider.DeviceConfig.getInt("content_capture", "content_protection_optional_groups_threshold", 0);
            contentProtectionAllowlistDelayMs = android.provider.DeviceConfig.getLong("content_capture", "content_protection_allowlist_delay_ms", 30000L);
            contentProtectionAllowlistTimeoutMs = android.provider.DeviceConfig.getLong("content_capture", "content_protection_allowlist_timeout_ms", 250L);
            this.mDevCfgContentProtectionAutoDisconnectTimeoutMs = android.provider.DeviceConfig.getLong("content_capture", "content_protection_auto_disconnect_timeout_ms", 3000L);
            contentProtectionAllowlistManagerOld = this.mContentProtectionAllowlistManager;
            if (this.verbose) {
                android.util.Slog.v(TAG, "setFineTuneParamsFromDeviceConfig(): bufferSize=" + this.mDevCfgMaxBufferSize + ", idleFlush=" + this.mDevCfgIdleFlushingFrequencyMs + ", textFlush=" + this.mDevCfgTextChangeFlushingFrequencyMs + ", logHistory=" + this.mDevCfgLogHistorySize + ", idleUnbindTimeoutMs=" + this.mDevCfgIdleUnbindTimeoutMs + ", disableFlushForViewTreeAppearing=" + this.mDevCfgDisableFlushForViewTreeAppearing + ", enableContentProtectionReceiver=" + enableContentProtectionReceiverNew + ", contentProtectionBufferSize=" + this.mDevCfgContentProtectionBufferSize + ", contentProtectionRequiredGroupsConfig=" + contentProtectionRequiredGroupsConfig + ", contentProtectionOptionalGroupsConfig=" + contentProtectionOptionalGroupsConfig + ", contentProtectionOptionalGroupsThreshold=" + contentProtectionOptionalGroupsThreshold + ", contentProtectionAllowlistDelayMs=" + contentProtectionAllowlistDelayMs + ", contentProtectionAllowlistTimeoutMs=" + contentProtectionAllowlistTimeoutMs + ", contentProtectionAutoDisconnectTimeoutMs=" + this.mDevCfgContentProtectionAutoDisconnectTimeoutMs);
            }
        }
        java.util.List<java.util.List<java.lang.String>> contentProtectionRequiredGroups = parseContentProtectionGroupsConfig(contentProtectionRequiredGroupsConfig);
        java.util.List<java.util.List<java.lang.String>> contentProtectionOptionalGroups = parseContentProtectionGroupsConfig(contentProtectionOptionalGroupsConfig);
        com.android.server.contentprotection.ContentProtectionAllowlistManager contentProtectionAllowlistManagerNew = null;
        if (contentProtectionAllowlistManagerOld != null && !enableContentProtectionReceiverNew) {
            contentProtectionAllowlistManagerOld.stop();
        }
        if (enableContentProtectionReceiverOld || !enableContentProtectionReceiverNew) {
            contentProtectionConsentManagerNew = null;
            contentProtectionServiceComponentNameNew = null;
        } else {
            android.content.ComponentName contentProtectionServiceComponentNameNew2 = getContentProtectionServiceComponentName();
            if (contentProtectionServiceComponentNameNew2 == null) {
                contentProtectionConsentManagerNew = null;
                contentProtectionServiceComponentNameNew = contentProtectionServiceComponentNameNew2;
            } else {
                contentProtectionAllowlistManagerNew = createContentProtectionAllowlistManager(contentProtectionAllowlistTimeoutMs);
                contentProtectionAllowlistManagerNew.start(contentProtectionAllowlistDelayMs);
                com.android.server.contentprotection.ContentProtectionConsentManager contentProtectionConsentManagerNew2 = createContentProtectionConsentManager();
                contentProtectionConsentManagerNew = contentProtectionConsentManagerNew2;
                contentProtectionServiceComponentNameNew = contentProtectionServiceComponentNameNew2;
            }
        }
        synchronized (this.mLock) {
            this.mDevCfgEnableContentProtectionReceiver = enableContentProtectionReceiverNew;
            this.mDevCfgContentProtectionRequiredGroups = contentProtectionRequiredGroups;
            this.mDevCfgContentProtectionOptionalGroups = contentProtectionOptionalGroups;
            this.mDevCfgContentProtectionOptionalGroupsThreshold = contentProtectionOptionalGroupsThreshold;
            this.mDevCfgContentProtectionAllowlistDelayMs = contentProtectionAllowlistDelayMs;
            if (enableContentProtectionReceiverOld ^ enableContentProtectionReceiverNew) {
                this.mContentProtectionServiceComponentName = contentProtectionServiceComponentNameNew;
                this.mContentProtectionAllowlistManager = contentProtectionAllowlistManagerNew;
                this.mContentProtectionConsentManager = contentProtectionConsentManagerNew;
            }
        }
    }

    private void setLoggingLevelFromDeviceConfig() {
        this.mDevCfgLoggingLevel = android.provider.DeviceConfig.getInt("content_capture", "logging_level", android.view.contentcapture.ContentCaptureHelper.getDefaultLoggingLevel());
        android.view.contentcapture.ContentCaptureHelper.setLoggingLevel(this.mDevCfgLoggingLevel);
        this.verbose = android.view.contentcapture.ContentCaptureHelper.sVerbose;
        this.debug = android.view.contentcapture.ContentCaptureHelper.sDebug;
        if (this.verbose) {
            android.util.Slog.v(TAG, "setLoggingLevelFromDeviceConfig(): level=" + this.mDevCfgLoggingLevel + ", debug=" + this.debug + ", verbose=" + this.verbose);
        }
    }

    private void setDeviceConfigProperties() {
        setLoggingLevelFromDeviceConfig();
        setFineTuneParamsFromDeviceConfig();
        java.lang.String enabled = android.provider.DeviceConfig.getProperty("content_capture", "service_explicitly_enabled");
        setDisabledByDeviceConfig(enabled);
        setActivityStartAssistDataEnabled();
    }

    private void setActivityStartAssistDataEnabled() {
        synchronized (this.mLock) {
            this.activityStartAssistDataEnabled = android.provider.DeviceConfig.getBoolean("content_capture", "enable_activity_start_assist_content", false);
        }
    }

    private void setDisabledByDeviceConfig(java.lang.String explicitlyEnabled) {
        boolean newDisabledValue;
        if (this.verbose) {
            android.util.Slog.v(TAG, "setDisabledByDeviceConfig(): explicitlyEnabled=" + explicitlyEnabled);
        }
        java.util.List<android.content.pm.UserInfo> users = getSupportedUsers();
        if (explicitlyEnabled != null && explicitlyEnabled.equalsIgnoreCase("false")) {
            newDisabledValue = true;
        } else {
            newDisabledValue = false;
        }
        synchronized (this.mLock) {
            if (this.mDisabledByDeviceConfig == newDisabledValue) {
                if (this.verbose) {
                    android.util.Slog.v(TAG, "setDisabledByDeviceConfig(): already " + newDisabledValue);
                }
                return;
            }
            this.mDisabledByDeviceConfig = newDisabledValue;
            android.util.Slog.i(TAG, "setDisabledByDeviceConfig(): set to " + this.mDisabledByDeviceConfig);
            for (int i = 0; i < users.size(); i++) {
                int userId = users.get(i).id;
                boolean disabled = this.mDisabledByDeviceConfig || isDisabledBySettingsLocked(userId);
                android.util.Slog.i(TAG, "setDisabledByDeviceConfig(): updating service for user " + userId + " to " + (disabled ? "'disabled'" : "'enabled'"));
                updateCachedServiceLocked(userId, disabled);
            }
        }
    }

    private void setContentCaptureFeatureEnabledBySettingsForUser(int userId, boolean enabled) {
        synchronized (this.mLock) {
            if (this.mDisabledBySettings == null) {
                this.mDisabledBySettings = new android.util.SparseBooleanArray();
            }
            boolean disabled = true;
            boolean alreadyEnabled = !this.mDisabledBySettings.get(userId);
            if (!(enabled ^ alreadyEnabled)) {
                if (this.debug) {
                    android.util.Slog.d(TAG, "setContentCaptureFeatureEnabledForUser(): already " + enabled);
                }
                return;
            }
            if (enabled) {
                android.util.Slog.i(TAG, "setContentCaptureFeatureEnabled(): enabling service for user " + userId);
                this.mDisabledBySettings.delete(userId);
            } else {
                android.util.Slog.i(TAG, "setContentCaptureFeatureEnabled(): disabling service for user " + userId);
                this.mDisabledBySettings.put(userId, true);
            }
            if (enabled && !this.mDisabledByDeviceConfig) {
                disabled = false;
            }
            updateCachedServiceLocked(userId, disabled);
        }
    }

    void destroySessions(int userId, com.android.internal.os.IResultReceiver receiver) {
        android.util.Slog.i(TAG, "destroySessions() for userId " + userId);
        enforceCallingPermissionForManagement();
        synchronized (this.mLock) {
            if (userId != -1) {
                com.android.server.contentcapture.ContentCapturePerUserService service = peekServiceForUserLocked(userId);
                if (service != null) {
                    service.destroySessionsLocked();
                }
            } else {
                visitServicesLocked(new com.android.server.infra.AbstractMasterSystemService.Visitor() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$$ExternalSyntheticLambda2
                    @Override // com.android.server.infra.AbstractMasterSystemService.Visitor
                    public final void visit(java.lang.Object obj) {
                        ((com.android.server.contentcapture.ContentCapturePerUserService) obj).destroySessionsLocked();
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
                com.android.server.contentcapture.ContentCapturePerUserService service = peekServiceForUserLocked(userId);
                if (service != null) {
                    service.listSessionsLocked(sessions);
                }
            } else {
                visitServicesLocked(new com.android.server.infra.AbstractMasterSystemService.Visitor() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$$ExternalSyntheticLambda0
                    @Override // com.android.server.infra.AbstractMasterSystemService.Visitor
                    public final void visit(java.lang.Object obj) {
                        ((com.android.server.contentcapture.ContentCapturePerUserService) obj).listSessionsLocked(sessions);
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

    void updateOptions(final java.lang.String packageName, final android.content.ContentCaptureOptions options) {
        this.mCallbacks.broadcast(new java.util.function.BiConsumer() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                com.android.server.contentcapture.ContentCaptureManagerService.lambda$updateOptions$3(packageName, options, (android.view.contentcapture.IContentCaptureOptionsCallback) obj, obj2);
            }
        });
    }

    static /* synthetic */ void lambda$updateOptions$3(java.lang.String packageName, android.content.ContentCaptureOptions options, android.view.contentcapture.IContentCaptureOptionsCallback callback, java.lang.Object pkg) {
        if (pkg.equals(packageName)) {
            try {
                callback.setContentCaptureOptions(options);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Unable to send setContentCaptureOptions(): " + e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.app.ActivityManagerInternal getAmInternal() {
        synchronized (this.mLock) {
            if (this.mAm == null) {
                this.mAm = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
            }
        }
        return this.mAm;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void assertCalledByServiceLocked(java.lang.String methodName) {
        if (!isCalledByServiceLocked(methodName)) {
            throw new java.lang.SecurityException("caller is not user's ContentCapture service");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isCalledByServiceLocked(java.lang.String methodName) {
        int userId = android.os.UserHandle.getCallingUserId();
        int callingUid = android.os.Binder.getCallingUid();
        java.lang.String serviceName = this.mServiceNameResolver.getServiceName(userId);
        if (serviceName == null) {
            android.util.Slog.e(TAG, methodName + ": called by UID " + callingUid + ", but there's no service set for user " + userId);
            return false;
        }
        android.content.ComponentName serviceComponent = android.content.ComponentName.unflattenFromString(serviceName);
        if (serviceComponent == null) {
            android.util.Slog.w(TAG, methodName + ": invalid service name: " + serviceName);
            return false;
        }
        java.lang.String servicePackageName = serviceComponent.getPackageName();
        android.content.pm.PackageManager pm = getContext().getPackageManager();
        try {
            int serviceUid = pm.getPackageUidAsUser(servicePackageName, android.os.UserHandle.getCallingUserId());
            if (callingUid != serviceUid) {
                android.util.Slog.e(TAG, methodName + ": called by UID " + callingUid + ", but service UID is " + serviceUid);
                return false;
            }
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.w(TAG, methodName + ": could not verify UID for " + serviceName);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean throwsSecurityException(com.android.internal.os.IResultReceiver result, java.lang.Runnable runable) {
        try {
            runable.run();
            return false;
        } catch (java.lang.SecurityException e) {
            try {
                result.send(-1, com.android.internal.util.SyncResultReceiver.bundleFor(e.getMessage()));
                return true;
            } catch (android.os.RemoteException e2) {
                android.util.Slog.w(TAG, "Unable to send security exception (" + e + "): ", e2);
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDefaultServiceLocked(int userId) {
        java.lang.String defaultServiceName = this.mServiceNameResolver.getDefaultServiceName(userId);
        if (defaultServiceName == null) {
            return false;
        }
        java.lang.String currentServiceName = this.mServiceNameResolver.getServiceName(userId);
        return defaultServiceName.equals(currentServiceName);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.server.infra.AbstractMasterSystemService
    public void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
        super.dumpLocked(prefix, pw);
        java.lang.String prefix2 = prefix + "  ";
        pw.print(prefix);
        pw.print("Users disabled by Settings: ");
        pw.println(this.mDisabledBySettings);
        pw.print(prefix);
        pw.println("DeviceConfig Settings: ");
        pw.print(prefix2);
        pw.print("disabled: ");
        pw.println(this.mDisabledByDeviceConfig);
        pw.print(prefix2);
        pw.print("loggingLevel: ");
        pw.println(this.mDevCfgLoggingLevel);
        pw.print(prefix2);
        pw.print("maxBufferSize: ");
        pw.println(this.mDevCfgMaxBufferSize);
        pw.print(prefix2);
        pw.print("idleFlushingFrequencyMs: ");
        pw.println(this.mDevCfgIdleFlushingFrequencyMs);
        pw.print(prefix2);
        pw.print("textChangeFlushingFrequencyMs: ");
        pw.println(this.mDevCfgTextChangeFlushingFrequencyMs);
        pw.print(prefix2);
        pw.print("logHistorySize: ");
        pw.println(this.mDevCfgLogHistorySize);
        pw.print(prefix2);
        pw.print("idleUnbindTimeoutMs: ");
        pw.println(this.mDevCfgIdleUnbindTimeoutMs);
        pw.print(prefix2);
        pw.print("disableFlushForViewTreeAppearing: ");
        pw.println(this.mDevCfgDisableFlushForViewTreeAppearing);
        pw.print(prefix2);
        pw.print("enableContentProtectionReceiver: ");
        pw.println(this.mDevCfgEnableContentProtectionReceiver);
        pw.print(prefix2);
        pw.print("contentProtectionBufferSize: ");
        pw.println(this.mDevCfgContentProtectionBufferSize);
        pw.print(prefix2);
        pw.print("contentProtectionRequiredGroupsSize: ");
        pw.println(this.mDevCfgContentProtectionRequiredGroups.size());
        pw.print(prefix2);
        pw.print("contentProtectionOptionalGroupsSize: ");
        pw.println(this.mDevCfgContentProtectionOptionalGroups.size());
        pw.print(prefix2);
        pw.print("contentProtectionOptionalGroupsThreshold: ");
        pw.println(this.mDevCfgContentProtectionOptionalGroupsThreshold);
        pw.print(prefix2);
        pw.print("contentProtectionAllowlistDelayMs: ");
        pw.println(this.mDevCfgContentProtectionAllowlistDelayMs);
        pw.print(prefix2);
        pw.print("contentProtectionAllowlistTimeoutMs: ");
        pw.println(this.mDevCfgContentProtectionAllowlistTimeoutMs);
        pw.print(prefix2);
        pw.print("contentProtectionAutoDisconnectTimeoutMs: ");
        pw.println(this.mDevCfgContentProtectionAutoDisconnectTimeoutMs);
        pw.print(prefix2);
        pw.print("activityStartAssistDataEnabled: ");
        pw.println(this.activityStartAssistDataEnabled);
        pw.print(prefix);
        pw.println("Global Options:");
        this.mGlobalContentCaptureOptions.dump(prefix2, pw);
    }

    protected boolean getDeviceConfigEnableContentProtectionReceiver() {
        return android.provider.DeviceConfig.getBoolean("content_capture", "enable_content_protection_receiver", false);
    }

    protected com.android.server.contentprotection.ContentProtectionAllowlistManager createContentProtectionAllowlistManager(long timeoutMs) {
        return new com.android.server.contentprotection.ContentProtectionAllowlistManager(this, com.android.internal.os.BackgroundThread.getHandler(), timeoutMs);
    }

    protected com.android.server.contentprotection.ContentProtectionConsentManager createContentProtectionConsentManager() {
        return new com.android.server.contentprotection.ContentProtectionConsentManager(com.android.internal.os.BackgroundThread.getHandler(), getContext().getContentResolver(), android.app.admin.DevicePolicyCache.getInstance());
    }

    private android.content.ComponentName getContentProtectionServiceComponentName() {
        java.lang.String flatComponentName = getContentProtectionServiceFlatComponentName();
        if (flatComponentName == null) {
            return null;
        }
        return android.content.ComponentName.unflattenFromString(flatComponentName);
    }

    protected java.lang.String getContentProtectionServiceFlatComponentName() {
        return getContext().getString(android.R.string.config_defaultDndDeniedPackages);
    }

    protected android.service.contentcapture.ContentCaptureServiceInfo createContentProtectionServiceInfo(android.content.ComponentName componentName) throws android.content.pm.PackageManager.NameNotFoundException {
        return new android.service.contentcapture.ContentCaptureServiceInfo(getContext(), componentName, false, android.os.UserHandle.getCallingUserId());
    }

    public com.android.server.contentprotection.RemoteContentProtectionService createRemoteContentProtectionService() {
        synchronized (this.mLock) {
            if (this.mDevCfgEnableContentProtectionReceiver && this.mContentProtectionServiceComponentName != null) {
                android.content.ComponentName componentName = this.mContentProtectionServiceComponentName;
                long autoDisconnectTimeoutMs = this.mDevCfgContentProtectionAutoDisconnectTimeoutMs;
                try {
                    createContentProtectionServiceInfo(componentName);
                    return createRemoteContentProtectionService(componentName, autoDisconnectTimeoutMs);
                } catch (java.lang.Exception e) {
                    return null;
                }
            }
            return null;
        }
    }

    protected com.android.server.contentprotection.RemoteContentProtectionService createRemoteContentProtectionService(android.content.ComponentName componentName, long autoDisconnectTimeoutMs) {
        return new com.android.server.contentprotection.RemoteContentProtectionService(getContext(), componentName, android.os.UserHandle.getCallingUserId(), isBindInstantServiceAllowed(), autoDisconnectTimeoutMs);
    }

    protected com.android.server.contentcapture.ContentCaptureManagerService.ContentCaptureManagerServiceStub getContentCaptureManagerServiceStub() {
        return this.mContentCaptureManagerServiceStub;
    }

    protected java.util.List<java.util.List<java.lang.String>> parseContentProtectionGroupsConfig(java.lang.String config) {
        if (this.verbose) {
            android.util.Slog.v(TAG, "parseContentProtectionGroupsConfig: " + config);
        }
        if (config == null) {
            return java.util.Collections.emptyList();
        }
        return java.util.Arrays.stream(config.split(CONTENT_PROTECTION_GROUP_CONFIG_SEPARATOR_GROUP)).map(new java.util.function.Function() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$$ExternalSyntheticLambda4
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.parseContentProtectionGroupConfigValues((java.lang.String) obj);
            }
        }).filter(new java.util.function.Predicate() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.contentcapture.ContentCaptureManagerService.lambda$parseContentProtectionGroupsConfig$4((java.util.List) obj);
            }
        }).toList();
    }

    static /* synthetic */ boolean lambda$parseContentProtectionGroupsConfig$4(java.util.List group) {
        return !group.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<java.lang.String> parseContentProtectionGroupConfigValues(java.lang.String group) {
        return java.util.Arrays.stream(group.split(CONTENT_PROTECTION_GROUP_CONFIG_SEPARATOR_VALUE)).filter(new java.util.function.Predicate() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.contentcapture.ContentCaptureManagerService.lambda$parseContentProtectionGroupConfigValues$5((java.lang.String) obj);
            }
        }).toList();
    }

    static /* synthetic */ boolean lambda$parseContentProtectionGroupConfigValues$5(java.lang.String value) {
        return !value.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isContentProtectionEnabledLocked() {
        return (!this.mDevCfgEnableContentProtectionReceiver || this.mContentProtectionServiceComponentName == null || this.mContentProtectionAllowlistManager == null || this.mContentProtectionConsentManager == null || (this.mDevCfgContentProtectionRequiredGroups.isEmpty() && this.mDevCfgContentProtectionOptionalGroups.isEmpty())) ? false : true;
    }

    final class ContentCaptureManagerServiceStub extends android.view.contentcapture.IContentCaptureManager.Stub {
        ContentCaptureManagerServiceStub() {
        }

        public void startSession(android.os.IBinder activityToken, android.os.IBinder shareableActivityToken, android.content.ComponentName componentName, int sessionId, int flags, com.android.internal.os.IResultReceiver result) throws java.lang.Throwable {
            java.util.Objects.requireNonNull(activityToken);
            java.util.Objects.requireNonNull(shareableActivityToken);
            int userId = android.os.UserHandle.getCallingUserId();
            android.content.pm.ActivityPresentationInfo activityPresentationInfo = com.android.server.contentcapture.ContentCaptureManagerService.this.getAmInternal().getActivityPresentationInfo(activityToken);
            synchronized (com.android.server.contentcapture.ContentCaptureManagerService.this.mLock) {
                try {
                    try {
                        com.android.server.contentcapture.ContentCapturePerUserService service = (com.android.server.contentcapture.ContentCapturePerUserService) com.android.server.contentcapture.ContentCaptureManagerService.this.getServiceForUserLocked(userId);
                        if (!com.android.server.contentcapture.ContentCaptureManagerService.this.isDefaultServiceLocked(userId)) {
                            if (!com.android.server.contentcapture.ContentCaptureManagerService.this.isCalledByServiceLocked("startSession()")) {
                                android.service.contentcapture.ContentCaptureService.setClientState(result, 4, (android.os.IBinder) null);
                                return;
                            }
                        }
                        service.startSessionLocked(activityToken, shareableActivityToken, activityPresentationInfo, sessionId, android.os.Binder.getCallingUid(), flags, result);
                    } catch (java.lang.Throwable th) {
                        th = th;
                        throw th;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }

        public void finishSession(int sessionId) {
            int userId = android.os.UserHandle.getCallingUserId();
            synchronized (com.android.server.contentcapture.ContentCaptureManagerService.this.mLock) {
                com.android.server.contentcapture.ContentCapturePerUserService service = (com.android.server.contentcapture.ContentCapturePerUserService) com.android.server.contentcapture.ContentCaptureManagerService.this.getServiceForUserLocked(userId);
                service.finishSessionLocked(sessionId);
            }
        }

        public void getServiceComponentName(com.android.internal.os.IResultReceiver result) {
            android.content.ComponentName connectedServiceComponentName;
            int userId = android.os.UserHandle.getCallingUserId();
            synchronized (com.android.server.contentcapture.ContentCaptureManagerService.this.mLock) {
                com.android.server.contentcapture.ContentCapturePerUserService service = (com.android.server.contentcapture.ContentCapturePerUserService) com.android.server.contentcapture.ContentCaptureManagerService.this.getServiceForUserLocked(userId);
                connectedServiceComponentName = service.getServiceComponentName();
            }
            try {
                result.send(0, com.android.internal.util.SyncResultReceiver.bundleFor(connectedServiceComponentName));
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Unable to send service component name: " + e);
            }
        }

        public void removeData(android.view.contentcapture.DataRemovalRequest request) {
            java.util.Objects.requireNonNull(request);
            com.android.server.contentcapture.ContentCaptureManagerService.this.assertCalledByPackageOwner(request.getPackageName());
            int userId = android.os.UserHandle.getCallingUserId();
            synchronized (com.android.server.contentcapture.ContentCaptureManagerService.this.mLock) {
                com.android.server.contentcapture.ContentCapturePerUserService service = (com.android.server.contentcapture.ContentCapturePerUserService) com.android.server.contentcapture.ContentCaptureManagerService.this.getServiceForUserLocked(userId);
                service.removeDataLocked(request);
            }
        }

        public void shareData(android.view.contentcapture.DataShareRequest request, android.view.contentcapture.IDataShareWriteAdapter clientAdapter) {
            java.util.Objects.requireNonNull(request);
            java.util.Objects.requireNonNull(clientAdapter);
            com.android.server.contentcapture.ContentCaptureManagerService.this.assertCalledByPackageOwner(request.getPackageName());
            int userId = android.os.UserHandle.getCallingUserId();
            synchronized (com.android.server.contentcapture.ContentCaptureManagerService.this.mLock) {
                com.android.server.contentcapture.ContentCapturePerUserService service = (com.android.server.contentcapture.ContentCapturePerUserService) com.android.server.contentcapture.ContentCaptureManagerService.this.getServiceForUserLocked(userId);
                if (com.android.server.contentcapture.ContentCaptureManagerService.this.mPackagesWithShareRequests.size() < 10 && !com.android.server.contentcapture.ContentCaptureManagerService.this.mPackagesWithShareRequests.contains(request.getPackageName())) {
                    service.onDataSharedLocked(request, new com.android.server.contentcapture.ContentCaptureManagerService.DataShareCallbackDelegate(request, clientAdapter, com.android.server.contentcapture.ContentCaptureManagerService.this));
                    return;
                }
                try {
                    java.lang.String serviceName = com.android.server.contentcapture.ContentCaptureManagerService.this.mServiceNameResolver.getServiceName(userId);
                    com.android.server.contentcapture.ContentCaptureMetricsLogger.writeServiceEvent(14, serviceName);
                    clientAdapter.error(2);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to send error message to client");
                }
            }
        }

        public void isContentCaptureFeatureEnabled(com.android.internal.os.IResultReceiver result) {
            synchronized (com.android.server.contentcapture.ContentCaptureManagerService.this.mLock) {
                if (com.android.server.contentcapture.ContentCaptureManagerService.this.throwsSecurityException(result, new java.lang.Runnable() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$ContentCaptureManagerServiceStub$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$isContentCaptureFeatureEnabled$0();
                    }
                })) {
                    return;
                }
                int userId = android.os.UserHandle.getCallingUserId();
                int userId2 = (com.android.server.contentcapture.ContentCaptureManagerService.this.mDisabledByDeviceConfig || com.android.server.contentcapture.ContentCaptureManagerService.this.isDisabledBySettingsLocked(userId)) ? 0 : 1;
                try {
                    result.send(userId2 == 0 ? 2 : 1, (android.os.Bundle) null);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Unable to send isContentCaptureFeatureEnabled(): " + e);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$isContentCaptureFeatureEnabled$0() {
            com.android.server.contentcapture.ContentCaptureManagerService.this.assertCalledByServiceLocked("isContentCaptureFeatureEnabled()");
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getServiceSettingsActivity$1() {
            com.android.server.contentcapture.ContentCaptureManagerService.this.enforceCallingPermissionForManagement();
        }

        public void getServiceSettingsActivity(com.android.internal.os.IResultReceiver result) {
            if (com.android.server.contentcapture.ContentCaptureManagerService.this.throwsSecurityException(result, new java.lang.Runnable() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$ContentCaptureManagerServiceStub$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$getServiceSettingsActivity$1();
                }
            })) {
                return;
            }
            int userId = android.os.UserHandle.getCallingUserId();
            synchronized (com.android.server.contentcapture.ContentCaptureManagerService.this.mLock) {
                com.android.server.contentcapture.ContentCapturePerUserService service = (com.android.server.contentcapture.ContentCapturePerUserService) com.android.server.contentcapture.ContentCaptureManagerService.this.getServiceForUserLocked(userId);
                if (service == null) {
                    return;
                }
                android.content.ComponentName componentName = service.getServiceSettingsActivityLocked();
                try {
                    result.send(0, com.android.internal.util.SyncResultReceiver.bundleFor(componentName));
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Unable to send getServiceSettingsIntent(): " + e);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getContentCaptureConditions$2(java.lang.String packageName) {
            com.android.server.contentcapture.ContentCaptureManagerService.this.assertCalledByPackageOwner(packageName);
        }

        public void getContentCaptureConditions(final java.lang.String packageName, com.android.internal.os.IResultReceiver result) {
            java.util.ArrayList<android.view.contentcapture.ContentCaptureCondition> conditions;
            if (com.android.server.contentcapture.ContentCaptureManagerService.this.throwsSecurityException(result, new java.lang.Runnable() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$ContentCaptureManagerServiceStub$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$getContentCaptureConditions$2(packageName);
                }
            })) {
                return;
            }
            int userId = android.os.UserHandle.getCallingUserId();
            synchronized (com.android.server.contentcapture.ContentCaptureManagerService.this.mLock) {
                com.android.server.contentcapture.ContentCapturePerUserService service = (com.android.server.contentcapture.ContentCapturePerUserService) com.android.server.contentcapture.ContentCaptureManagerService.this.getServiceForUserLocked(userId);
                conditions = service == null ? null : android.view.contentcapture.ContentCaptureHelper.toList(service.getContentCaptureConditionsLocked(packageName));
            }
            try {
                result.send(0, com.android.internal.util.SyncResultReceiver.bundleFor(conditions));
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Unable to send getServiceComponentName(): " + e);
            }
        }

        public void registerContentCaptureOptionsCallback(java.lang.String packageName, android.view.contentcapture.IContentCaptureOptionsCallback callback) throws java.lang.Throwable {
            com.android.server.contentcapture.ContentCaptureManagerService.this.assertCalledByPackageOwner(packageName);
            com.android.server.contentcapture.ContentCaptureManagerService.this.mCallbacks.register(callback, packageName);
            int userId = android.os.UserHandle.getCallingUserId();
            android.content.ContentCaptureOptions options = com.android.server.contentcapture.ContentCaptureManagerService.this.mGlobalContentCaptureOptions.getOptions(userId, packageName);
            if (options != null) {
                try {
                    callback.setContentCaptureOptions(options);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Unable to send setContentCaptureOptions(): " + e);
                }
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:18:0x0037  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void dump(java.io.FileDescriptor r9, java.io.PrintWriter r10, java.lang.String[] r11) {
            /*
                r8 = this;
                com.android.server.contentcapture.ContentCaptureManagerService r0 = com.android.server.contentcapture.ContentCaptureManagerService.this
                android.content.Context r0 = r0.getContext()
                java.lang.String r1 = com.android.server.contentcapture.ContentCaptureManagerService.m3027$$Nest$sfgetTAG()
                boolean r0 = com.android.internal.util.DumpUtils.checkDumpPermission(r0, r1, r10)
                if (r0 != 0) goto L11
                return
            L11:
                r0 = 1
                if (r11 == 0) goto L61
                int r1 = r11.length
                r2 = 0
                r3 = r2
            L17:
                if (r3 >= r1) goto L61
                r4 = r11[r3]
                int r5 = r4.hashCode()
                switch(r5) {
                    case 1098711592: goto L2d;
                    case 1333069025: goto L23;
                    default: goto L22;
                }
            L22:
                goto L37
            L23:
                java.lang.String r5 = "--help"
                boolean r5 = r4.equals(r5)
                if (r5 == 0) goto L22
                r5 = 1
                goto L38
            L2d:
                java.lang.String r5 = "--no-history"
                boolean r5 = r4.equals(r5)
                if (r5 == 0) goto L22
                r5 = r2
                goto L38
            L37:
                r5 = -1
            L38:
                switch(r5) {
                    case 0: goto L5c;
                    case 1: goto L56;
                    default: goto L3b;
                }
            L3b:
                java.lang.String r5 = com.android.server.contentcapture.ContentCaptureManagerService.m3027$$Nest$sfgetTAG()
                java.lang.StringBuilder r6 = new java.lang.StringBuilder
                r6.<init>()
                java.lang.String r7 = "Ignoring invalid dump arg: "
                java.lang.StringBuilder r6 = r6.append(r7)
                java.lang.StringBuilder r6 = r6.append(r4)
                java.lang.String r6 = r6.toString()
                android.util.Slog.w(r5, r6)
                goto L5e
            L56:
                java.lang.String r1 = "Usage: dumpsys content_capture [--no-history]"
                r10.println(r1)
                return
            L5c:
                r0 = 0
            L5e:
                int r3 = r3 + 1
                goto L17
            L61:
                com.android.server.contentcapture.ContentCaptureManagerService r1 = com.android.server.contentcapture.ContentCaptureManagerService.this
                java.lang.Object r1 = com.android.server.contentcapture.ContentCaptureManagerService.access$1600(r1)
                monitor-enter(r1)
                com.android.server.contentcapture.ContentCaptureManagerService r2 = com.android.server.contentcapture.ContentCaptureManagerService.this     // Catch: java.lang.Throwable -> L95
                java.lang.String r3 = ""
                r2.dumpLocked(r3, r10)     // Catch: java.lang.Throwable -> L95
                monitor-exit(r1)     // Catch: java.lang.Throwable -> L95
                java.lang.String r1 = "Requests history: "
                r10.print(r1)
                com.android.server.contentcapture.ContentCaptureManagerService r1 = com.android.server.contentcapture.ContentCaptureManagerService.this
                android.util.LocalLog r1 = r1.mRequestsHistory
                if (r1 != 0) goto L81
                java.lang.String r1 = "disabled by device config"
                r10.println(r1)
                goto L94
            L81:
                if (r0 == 0) goto L91
                r10.println()
                com.android.server.contentcapture.ContentCaptureManagerService r1 = com.android.server.contentcapture.ContentCaptureManagerService.this
                android.util.LocalLog r1 = r1.mRequestsHistory
                r1.reverseDump(r9, r10, r11)
                r10.println()
                goto L94
            L91:
                r10.println()
            L94:
                return
            L95:
                r2 = move-exception
                monitor-exit(r1)     // Catch: java.lang.Throwable -> L95
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.contentcapture.ContentCaptureManagerService.ContentCaptureManagerServiceStub.dump(java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
        }

        /* JADX WARN: Multi-variable type inference failed */
        public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) throws android.os.RemoteException {
            new com.android.server.contentcapture.ContentCaptureManagerServiceShellCommand(com.android.server.contentcapture.ContentCaptureManagerService.this).exec(this, in, out, err, args, callback, resultReceiver);
        }

        public void resetTemporaryService(int userId) {
            com.android.server.contentcapture.ContentCaptureManagerService.this.resetTemporaryService(userId);
        }

        public void setTemporaryService(int userId, java.lang.String serviceName, int duration) {
            com.android.server.contentcapture.ContentCaptureManagerService.this.setTemporaryService(userId, serviceName, duration);
        }

        public void setDefaultServiceEnabled(int userId, boolean enabled) {
            com.android.server.contentcapture.ContentCaptureManagerService.this.setDefaultServiceEnabled(userId, enabled);
        }

        public void onLoginDetected(final android.content.pm.ParceledListSlice<android.view.contentcapture.ContentCaptureEvent> events) {
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$ContentCaptureManagerServiceStub$$ExternalSyntheticLambda0
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$onLoginDetected$3(events);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLoginDetected$3(android.content.pm.ParceledListSlice events) throws java.lang.Exception {
            com.android.server.contentprotection.RemoteContentProtectionService service = com.android.server.contentcapture.ContentCaptureManagerService.this.createRemoteContentProtectionService();
            if (service == null) {
                return;
            }
            try {
                service.onLoginDetected(events);
            } catch (java.lang.Exception ex) {
                android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to call remote service", ex);
            }
        }
    }

    private final class LocalService extends com.android.server.contentcapture.ContentCaptureManagerInternal {
        private LocalService() {
        }

        @Override // com.android.server.contentcapture.ContentCaptureManagerInternal
        public boolean isContentCaptureServiceForUser(int uid, int userId) {
            synchronized (com.android.server.contentcapture.ContentCaptureManagerService.this.mLock) {
                com.android.server.contentcapture.ContentCapturePerUserService service = (com.android.server.contentcapture.ContentCapturePerUserService) com.android.server.contentcapture.ContentCaptureManagerService.this.peekServiceForUserLocked(userId);
                if (service != null) {
                    return service.isContentCaptureServiceForUserLocked(uid);
                }
                return false;
            }
        }

        @Override // com.android.server.contentcapture.ContentCaptureManagerInternal
        public boolean sendActivityStartAssistData(int userId, android.os.IBinder activityToken, android.content.Intent intentData) {
            synchronized (com.android.server.contentcapture.ContentCaptureManagerService.this.mLock) {
                if (!com.android.server.contentcapture.ContentCaptureManagerService.this.activityStartAssistDataEnabled) {
                    return false;
                }
                android.content.Intent intent = new android.content.Intent(intentData);
                intent.setFlags(intent.getFlags() & (-67));
                android.os.Bundle assistContentExtras = new android.os.Bundle();
                assistContentExtras.putBoolean("activity_start_assist_content", true);
                android.app.assist.AssistContent assistContent = new android.app.assist.AssistContent(assistContentExtras);
                assistContent.setDefaultIntent(intent);
                android.os.Bundle activityAssistData = new android.os.Bundle();
                activityAssistData.putParcelable(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT, assistContent);
                com.android.server.contentcapture.ContentCapturePerUserService service = (com.android.server.contentcapture.ContentCapturePerUserService) com.android.server.contentcapture.ContentCaptureManagerService.this.peekServiceForUserLocked(userId);
                if (service == null) {
                    return false;
                }
                return service.sendActivityAssistDataLocked(activityToken, activityAssistData);
            }
        }

        @Override // com.android.server.contentcapture.ContentCaptureManagerInternal
        public boolean sendActivityAssistData(int userId, android.os.IBinder activityToken, android.os.Bundle data) {
            synchronized (com.android.server.contentcapture.ContentCaptureManagerService.this.mLock) {
                com.android.server.contentcapture.ContentCapturePerUserService service = (com.android.server.contentcapture.ContentCapturePerUserService) com.android.server.contentcapture.ContentCaptureManagerService.this.peekServiceForUserLocked(userId);
                if (service != null) {
                    return service.sendActivityAssistDataLocked(activityToken, data);
                }
                return false;
            }
        }

        @Override // com.android.server.contentcapture.ContentCaptureManagerInternal
        public android.content.ContentCaptureOptions getOptionsForPackage(int userId, java.lang.String packageName) {
            return com.android.server.contentcapture.ContentCaptureManagerService.this.mGlobalContentCaptureOptions.getOptions(userId, packageName);
        }

        @Override // com.android.server.contentcapture.ContentCaptureManagerInternal
        public void notifyActivityEvent(int userId, android.content.ComponentName activityComponent, int eventType, android.app.assist.ActivityId activityId) {
            synchronized (com.android.server.contentcapture.ContentCaptureManagerService.this.mLock) {
                com.android.server.contentcapture.ContentCapturePerUserService service = (com.android.server.contentcapture.ContentCapturePerUserService) com.android.server.contentcapture.ContentCaptureManagerService.this.peekServiceForUserLocked(userId);
                if (service != null) {
                    service.onActivityEventLocked(activityId, activityComponent, eventType);
                }
            }
        }
    }

    final class GlobalContentCaptureOptions extends com.android.internal.infra.GlobalWhitelistState {
        private final android.util.SparseArray<java.lang.String> mServicePackages = new android.util.SparseArray<>();
        private final android.util.SparseBooleanArray mTemporaryServices = new android.util.SparseBooleanArray();

        GlobalContentCaptureOptions() {
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
                        android.util.Slog.w(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "setServiceInfo(): invalid name: " + serviceName);
                        this.mServicePackages.remove(userId);
                    } else {
                        this.mServicePackages.put(userId, componentName.getPackageName());
                    }
                } else {
                    this.mServicePackages.remove(userId);
                }
            }
        }

        public android.content.ContentCaptureOptions getOptions(int userId, java.lang.String packageName) throws java.lang.Throwable {
            boolean isContentCaptureReceiverEnabled;
            android.util.ArraySet<android.content.ComponentName> whitelistedComponents;
            android.content.ContentCaptureOptions options;
            boolean isContentProtectionReceiverEnabled = isContentProtectionReceiverEnabled(userId, packageName);
            synchronized (this.mGlobalWhitelistStateLock) {
                try {
                    isContentCaptureReceiverEnabled = isContentCaptureReceiverEnabled(userId, packageName);
                    if (isContentCaptureReceiverEnabled) {
                        whitelistedComponents = null;
                    } else {
                        android.util.ArraySet<android.content.ComponentName> whitelistedComponents2 = getWhitelistedComponents(userId, packageName);
                        if (!isContentProtectionReceiverEnabled && whitelistedComponents2 == null && packageName.equals(this.mServicePackages.get(userId))) {
                            if (com.android.server.contentcapture.ContentCaptureManagerService.this.verbose) {
                                android.util.Slog.v(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "getOptionsForPackage() lite for " + packageName);
                            }
                            return new android.content.ContentCaptureOptions(com.android.server.contentcapture.ContentCaptureManagerService.this.mDevCfgLoggingLevel);
                        }
                        whitelistedComponents = whitelistedComponents2;
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                }
                try {
                    if (android.os.Build.IS_USER && com.android.server.contentcapture.ContentCaptureManagerService.this.mServiceNameResolver.isTemporary(userId) && !packageName.equals(this.mServicePackages.get(userId))) {
                        android.util.Slog.w(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Ignoring package " + packageName + " while using temporary service " + this.mServicePackages.get(userId));
                        return null;
                    }
                    if (isContentCaptureReceiverEnabled || isContentProtectionReceiverEnabled || whitelistedComponents != null) {
                        synchronized (com.android.server.contentcapture.ContentCaptureManagerService.this.mLock) {
                            options = new android.content.ContentCaptureOptions(com.android.server.contentcapture.ContentCaptureManagerService.this.mDevCfgLoggingLevel, com.android.server.contentcapture.ContentCaptureManagerService.this.mDevCfgMaxBufferSize, com.android.server.contentcapture.ContentCaptureManagerService.this.mDevCfgIdleFlushingFrequencyMs, com.android.server.contentcapture.ContentCaptureManagerService.this.mDevCfgTextChangeFlushingFrequencyMs, com.android.server.contentcapture.ContentCaptureManagerService.this.mDevCfgLogHistorySize, com.android.server.contentcapture.ContentCaptureManagerService.this.mDevCfgDisableFlushForViewTreeAppearing, isContentCaptureReceiverEnabled || whitelistedComponents != null, new android.content.ContentCaptureOptions.ContentProtectionOptions(isContentProtectionReceiverEnabled, com.android.server.contentcapture.ContentCaptureManagerService.this.mDevCfgContentProtectionBufferSize, com.android.server.contentcapture.ContentCaptureManagerService.this.mDevCfgContentProtectionRequiredGroups, com.android.server.contentcapture.ContentCaptureManagerService.this.mDevCfgContentProtectionOptionalGroups, com.android.server.contentcapture.ContentCaptureManagerService.this.mDevCfgContentProtectionOptionalGroupsThreshold), whitelistedComponents);
                            if (com.android.server.contentcapture.ContentCaptureManagerService.this.verbose) {
                                android.util.Slog.v(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "getOptionsForPackage(" + packageName + "): " + options);
                            }
                        }
                        return options;
                    }
                    if (com.android.server.contentcapture.ContentCaptureManagerService.this.verbose) {
                        android.util.Slog.v(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "getOptionsForPackage(" + packageName + "): not whitelisted");
                    }
                    return null;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    throw th;
                }
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

        public boolean isWhitelisted(int userId, java.lang.String packageName) {
            return isContentCaptureReceiverEnabled(userId, packageName) || isContentProtectionReceiverEnabled(userId, packageName);
        }

        public boolean isWhitelisted(int userId, android.content.ComponentName componentName) {
            return super.isWhitelisted(userId, componentName) || isContentProtectionReceiverEnabled(userId, componentName.getPackageName());
        }

        private boolean isContentCaptureReceiverEnabled(int userId, java.lang.String packageName) {
            return super.isWhitelisted(userId, packageName);
        }

        private boolean isContentProtectionReceiverEnabled(int userId, java.lang.String packageName) {
            synchronized (com.android.server.contentcapture.ContentCaptureManagerService.this.mLock) {
                if (!com.android.server.contentcapture.ContentCaptureManagerService.this.isContentProtectionEnabledLocked()) {
                    return false;
                }
                com.android.server.contentprotection.ContentProtectionConsentManager consentManager = com.android.server.contentcapture.ContentCaptureManagerService.this.mContentProtectionConsentManager;
                com.android.server.contentprotection.ContentProtectionAllowlistManager allowlistManager = com.android.server.contentcapture.ContentCaptureManagerService.this.mContentProtectionAllowlistManager;
                return consentManager.isConsentGranted(userId) && allowlistManager.isAllowed(packageName);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class DataShareCallbackDelegate extends android.service.contentcapture.IDataShareCallback.Stub {
        private final android.view.contentcapture.IDataShareWriteAdapter mClientAdapter;
        private final android.view.contentcapture.DataShareRequest mDataShareRequest;
        private final java.util.concurrent.atomic.AtomicBoolean mLoggedWriteFinish = new java.util.concurrent.atomic.AtomicBoolean(false);
        private final com.android.server.contentcapture.ContentCaptureManagerService mParentService;

        DataShareCallbackDelegate(android.view.contentcapture.DataShareRequest dataShareRequest, android.view.contentcapture.IDataShareWriteAdapter clientAdapter, com.android.server.contentcapture.ContentCaptureManagerService parentService) {
            this.mDataShareRequest = dataShareRequest;
            this.mClientAdapter = clientAdapter;
            this.mParentService = parentService;
        }

        public void accept(final android.service.contentcapture.IDataShareReadAdapter serviceAdapter) {
            android.util.Slog.i(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Data share request accepted by Content Capture service");
            logServiceEvent(7);
            android.util.Pair<android.os.ParcelFileDescriptor, android.os.ParcelFileDescriptor> clientPipe = createPipe();
            if (clientPipe == null) {
                logServiceEvent(12);
                sendErrorSignal(this.mClientAdapter, serviceAdapter, 1);
                return;
            }
            final android.os.ParcelFileDescriptor sourceIn = (android.os.ParcelFileDescriptor) clientPipe.second;
            final android.os.ParcelFileDescriptor sinkIn = (android.os.ParcelFileDescriptor) clientPipe.first;
            android.util.Pair<android.os.ParcelFileDescriptor, android.os.ParcelFileDescriptor> servicePipe = createPipe();
            if (servicePipe == null) {
                logServiceEvent(13);
                bestEffortCloseFileDescriptors(sourceIn, sinkIn);
                sendErrorSignal(this.mClientAdapter, serviceAdapter, 1);
                return;
            }
            final android.os.ParcelFileDescriptor sourceOut = (android.os.ParcelFileDescriptor) servicePipe.second;
            final android.os.ParcelFileDescriptor sinkOut = (android.os.ParcelFileDescriptor) servicePipe.first;
            synchronized (this.mParentService.mLock) {
                this.mParentService.mPackagesWithShareRequests.add(this.mDataShareRequest.getPackageName());
            }
            if (setUpSharingPipeline(this.mClientAdapter, serviceAdapter, sourceIn, sinkOut)) {
                bestEffortCloseFileDescriptors(sourceIn, sinkOut);
                this.mParentService.mDataShareExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$DataShareCallbackDelegate$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$accept$0(sinkIn, sourceOut, serviceAdapter);
                    }
                });
                this.mParentService.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.contentcapture.ContentCaptureManagerService$DataShareCallbackDelegate$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.lambda$accept$1(sourceIn, sinkIn, sourceOut, sinkOut, serviceAdapter);
                    }
                }, 300000L);
            } else {
                sendErrorSignal(this.mClientAdapter, serviceAdapter, 1);
                bestEffortCloseFileDescriptors(sourceIn, sinkIn, sourceOut, sinkOut);
                synchronized (this.mParentService.mLock) {
                    this.mParentService.mPackagesWithShareRequests.remove(this.mDataShareRequest.getPackageName());
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$accept$0(android.os.ParcelFileDescriptor sinkIn, android.os.ParcelFileDescriptor sourceOut, android.service.contentcapture.IDataShareReadAdapter serviceAdapter) {
            java.io.InputStream fis;
            boolean receivedData = false;
            try {
                try {
                    try {
                        fis = new android.os.ParcelFileDescriptor.AutoCloseInputStream(sinkIn);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to call finish() the service operation", e);
                        return;
                    }
                } catch (java.io.IOException e2) {
                    android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to pipe client and service streams", e2);
                    logServiceEvent(10);
                    sendErrorSignal(this.mClientAdapter, serviceAdapter, 1);
                    synchronized (this.mParentService.mLock) {
                        this.mParentService.mPackagesWithShareRequests.remove(this.mDataShareRequest.getPackageName());
                        if (0 != 0) {
                            if (!this.mLoggedWriteFinish.get()) {
                                logServiceEvent(9);
                                this.mLoggedWriteFinish.set(true);
                            }
                            try {
                                this.mClientAdapter.finish();
                            } catch (android.os.RemoteException e3) {
                                android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to call finish() the client operation", e3);
                            }
                            serviceAdapter.finish();
                            return;
                        }
                    }
                }
                try {
                    java.io.OutputStream fos = new android.os.ParcelFileDescriptor.AutoCloseOutputStream(sourceOut);
                    try {
                        byte[] byteBuffer = new byte[1024];
                        while (true) {
                            int readBytes = fis.read(byteBuffer);
                            if (readBytes == -1) {
                                break;
                            }
                            fos.write(byteBuffer, 0, readBytes);
                            receivedData = true;
                        }
                        fos.close();
                        fis.close();
                        synchronized (this.mParentService.mLock) {
                            this.mParentService.mPackagesWithShareRequests.remove(this.mDataShareRequest.getPackageName());
                        }
                        if (receivedData) {
                            if (!this.mLoggedWriteFinish.get()) {
                                logServiceEvent(9);
                                this.mLoggedWriteFinish.set(true);
                            }
                            try {
                                this.mClientAdapter.finish();
                            } catch (android.os.RemoteException e4) {
                                android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to call finish() the client operation", e4);
                            }
                            serviceAdapter.finish();
                            return;
                        }
                        logServiceEvent(11);
                        sendErrorSignal(this.mClientAdapter, serviceAdapter, 1);
                    } finally {
                    }
                } catch (java.lang.Throwable th) {
                    try {
                        fis.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (java.lang.Throwable th3) {
                synchronized (this.mParentService.mLock) {
                    this.mParentService.mPackagesWithShareRequests.remove(this.mDataShareRequest.getPackageName());
                    if (0 != 0) {
                        if (!this.mLoggedWriteFinish.get()) {
                            logServiceEvent(9);
                            this.mLoggedWriteFinish.set(true);
                        }
                        try {
                            this.mClientAdapter.finish();
                        } catch (android.os.RemoteException e5) {
                            android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to call finish() the client operation", e5);
                        }
                        try {
                            serviceAdapter.finish();
                        } catch (android.os.RemoteException e6) {
                            android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to call finish() the service operation", e6);
                        }
                    } else {
                        logServiceEvent(11);
                        sendErrorSignal(this.mClientAdapter, serviceAdapter, 1);
                    }
                    throw th3;
                }
            }
        }

        public void reject() {
            android.util.Slog.i(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Data share request rejected by Content Capture service");
            logServiceEvent(8);
            try {
                this.mClientAdapter.rejected();
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to call rejected() the client operation", e);
                try {
                    this.mClientAdapter.error(1);
                } catch (android.os.RemoteException e2) {
                    android.util.Slog.w(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to call error() the client operation", e2);
                }
            }
        }

        private boolean setUpSharingPipeline(android.view.contentcapture.IDataShareWriteAdapter clientAdapter, android.service.contentcapture.IDataShareReadAdapter serviceAdapter, android.os.ParcelFileDescriptor sourceIn, android.os.ParcelFileDescriptor sinkOut) {
            try {
                clientAdapter.write(sourceIn);
                try {
                    serviceAdapter.start(sinkOut);
                    return true;
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to call start() the service operation", e);
                    logServiceEvent(13);
                    return false;
                }
            } catch (android.os.RemoteException e2) {
                android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to call write() the client operation", e2);
                logServiceEvent(12);
                return false;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* JADX INFO: renamed from: enforceDataSharingTtl, reason: merged with bridge method [inline-methods] */
        public void lambda$accept$1(android.os.ParcelFileDescriptor sourceIn, android.os.ParcelFileDescriptor sinkIn, android.os.ParcelFileDescriptor sourceOut, android.os.ParcelFileDescriptor sinkOut, android.service.contentcapture.IDataShareReadAdapter serviceAdapter) {
            synchronized (this.mParentService.mLock) {
                this.mParentService.mPackagesWithShareRequests.remove(this.mDataShareRequest.getPackageName());
                boolean finishedSuccessfully = (sinkIn.getFileDescriptor().valid() || sourceOut.getFileDescriptor().valid()) ? false : true;
                if (finishedSuccessfully) {
                    if (!this.mLoggedWriteFinish.get()) {
                        logServiceEvent(9);
                        this.mLoggedWriteFinish.set(true);
                    }
                    android.util.Slog.i(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Content capture data sharing session terminated successfully for package '" + this.mDataShareRequest.getPackageName() + "'");
                } else {
                    logServiceEvent(15);
                    android.util.Slog.i(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Reached the timeout of Content Capture data sharing session for package '" + this.mDataShareRequest.getPackageName() + "', terminating the pipe.");
                }
                bestEffortCloseFileDescriptors(sourceIn, sinkIn, sourceOut, sinkOut);
                if (!finishedSuccessfully) {
                    sendErrorSignal(this.mClientAdapter, serviceAdapter, 3);
                }
            }
        }

        private android.util.Pair<android.os.ParcelFileDescriptor, android.os.ParcelFileDescriptor> createPipe() {
            try {
                android.os.ParcelFileDescriptor[] fileDescriptors = android.os.ParcelFileDescriptor.createPipe();
                if (fileDescriptors.length != 2) {
                    android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to create a content capture data-sharing pipe, unexpected number of file descriptors");
                    return null;
                }
                if (!fileDescriptors[0].getFileDescriptor().valid() || !fileDescriptors[1].getFileDescriptor().valid()) {
                    android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to create a content capture data-sharing pipe, didn't receive a pair of valid file descriptors.");
                    return null;
                }
                return android.util.Pair.create(fileDescriptors[0], fileDescriptors[1]);
            } catch (java.io.IOException e) {
                android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to create a content capture data-sharing pipe", e);
                return null;
            }
        }

        private void bestEffortCloseFileDescriptor(android.os.ParcelFileDescriptor fd) {
            try {
                fd.close();
            } catch (java.io.IOException e) {
                android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to close a file descriptor", e);
            }
        }

        private void bestEffortCloseFileDescriptors(android.os.ParcelFileDescriptor... fds) {
            for (android.os.ParcelFileDescriptor fd : fds) {
                bestEffortCloseFileDescriptor(fd);
            }
        }

        private static void sendErrorSignal(android.view.contentcapture.IDataShareWriteAdapter clientAdapter, android.service.contentcapture.IDataShareReadAdapter serviceAdapter, int errorCode) {
            try {
                clientAdapter.error(errorCode);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to call error() the client operation", e);
            }
            try {
                serviceAdapter.error(errorCode);
            } catch (android.os.RemoteException e2) {
                android.util.Slog.e(com.android.server.contentcapture.ContentCaptureManagerService.TAG, "Failed to call error() the service operation", e2);
            }
        }

        private void logServiceEvent(int eventType) {
            int userId = android.os.UserHandle.getCallingUserId();
            java.lang.String serviceName = this.mParentService.mServiceNameResolver.getServiceName(userId);
            com.android.server.contentcapture.ContentCaptureMetricsLogger.writeServiceEvent(eventType, serviceName);
        }
    }
}
