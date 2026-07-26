package com.android.server.timedetector;

/* JADX INFO: loaded from: classes3.dex */
final class ServiceConfigAccessorImpl implements com.android.server.timedetector.ServiceConfigAccessor {
    private static final int[] DEFAULT_AUTOMATIC_TIME_ORIGIN_PRIORITIES = {1, 3};
    private static final java.util.Set<java.lang.String> SERVER_FLAGS_KEYS_TO_WATCH = java.util.Set.of(com.android.server.timedetector.ServerFlags.KEY_TIME_DETECTOR_LOWER_BOUND_MILLIS_OVERRIDE, com.android.server.timedetector.ServerFlags.KEY_TIME_DETECTOR_ORIGIN_PRIORITIES_OVERRIDE);
    private static final java.lang.Object SLOCK = new java.lang.Object();
    private static final int SYSTEM_CLOCK_CONFIRMATION_THRESHOLD_MILLIS = 1000;
    private static com.android.server.timedetector.ServiceConfigAccessor sInstance;
    private final com.android.server.timedetector.ServiceConfigAccessorImpl.ConfigOriginPrioritiesSupplier mConfigOriginPrioritiesSupplier;
    private final java.util.List<com.android.server.timezonedetector.StateChangeListener> mConfigurationInternalListeners = new java.util.ArrayList();
    private final android.content.Context mContext;
    private final android.content.ContentResolver mCr;
    private final com.android.server.timedetector.ServerFlags mServerFlags;
    private final com.android.server.timedetector.ServiceConfigAccessorImpl.ServerFlagsOriginPrioritiesSupplier mServerFlagsOriginPrioritiesSupplier;
    private final int mSystemClockUpdateThresholdMillis;
    private final android.os.UserManager mUserManager;

    /* JADX WARN: Multi-variable type inference failed */
    private ServiceConfigAccessorImpl(android.content.Context context) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mCr = context.getContentResolver();
        this.mUserManager = (android.os.UserManager) context.getSystemService(android.os.UserManager.class);
        this.mServerFlags = com.android.server.timedetector.ServerFlags.getInstance(this.mContext);
        this.mConfigOriginPrioritiesSupplier = new com.android.server.timedetector.ServiceConfigAccessorImpl.ConfigOriginPrioritiesSupplier(context);
        this.mServerFlagsOriginPrioritiesSupplier = new com.android.server.timedetector.ServiceConfigAccessorImpl.ServerFlagsOriginPrioritiesSupplier(this.mServerFlags);
        this.mSystemClockUpdateThresholdMillis = context.getResources().getInteger(android.R.integer.config_screen_rotation_fade_out);
        android.content.IntentFilter intentFilter = new android.content.IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        this.mContext.registerReceiverForAllUsers(new android.content.BroadcastReceiver() { // from class: com.android.server.timedetector.ServiceConfigAccessorImpl.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                com.android.server.timedetector.ServiceConfigAccessorImpl.this.handleConfigurationInternalChangeOnMainThread();
            }
        }, intentFilter, null, null);
        android.os.Handler mainThreadHandler = this.mContext.getMainThreadHandler();
        this.mContext.getContentResolver().registerContentObserver(android.provider.Settings.Global.getUriFor("auto_time"), true, new android.database.ContentObserver(mainThreadHandler) { // from class: com.android.server.timedetector.ServiceConfigAccessorImpl.2
            @Override // android.database.ContentObserver
            public void onChange(boolean selfChange) {
                com.android.server.timedetector.ServiceConfigAccessorImpl.this.handleConfigurationInternalChangeOnMainThread();
            }
        });
        this.mServerFlags.addListener(new com.android.server.timezonedetector.StateChangeListener() { // from class: com.android.server.timedetector.ServiceConfigAccessorImpl$$ExternalSyntheticLambda0
            @Override // com.android.server.timezonedetector.StateChangeListener
            public final void onChange() {
                this.f$0.handleConfigurationInternalChangeOnMainThread();
            }
        }, SERVER_FLAGS_KEYS_TO_WATCH);
        this.mUserManager.addUserRestrictionsListener(new com.android.server.timedetector.ServiceConfigAccessorImpl.AnonymousClass3(mainThreadHandler));
    }

    /* JADX INFO: renamed from: com.android.server.timedetector.ServiceConfigAccessorImpl$3, reason: invalid class name */
    class AnonymousClass3 extends android.os.IUserRestrictionsListener.Stub {
        final /* synthetic */ android.os.Handler val$mainThreadHandler;

        AnonymousClass3(android.os.Handler handler) {
            this.val$mainThreadHandler = handler;
        }

        public void onUserRestrictionsChanged(final int userId, final android.os.Bundle newRestrictions, final android.os.Bundle prevRestrictions) {
            this.val$mainThreadHandler.post(new java.lang.Runnable() { // from class: com.android.server.timedetector.ServiceConfigAccessorImpl$3$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUserRestrictionsChanged$0(userId, newRestrictions, prevRestrictions);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserRestrictionsChanged$0(int userId, android.os.Bundle newRestrictions, android.os.Bundle prevRestrictions) {
            com.android.server.timedetector.ServiceConfigAccessorImpl.this.handleUserRestrictionsChangeOnMainThread(userId, newRestrictions, prevRestrictions);
        }
    }

    static com.android.server.timedetector.ServiceConfigAccessor getInstance(android.content.Context context) {
        com.android.server.timedetector.ServiceConfigAccessor serviceConfigAccessor;
        synchronized (SLOCK) {
            if (sInstance == null) {
                sInstance = new com.android.server.timedetector.ServiceConfigAccessorImpl(context);
            }
            serviceConfigAccessor = sInstance;
        }
        return serviceConfigAccessor;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleConfigurationInternalChangeOnMainThread() {
        java.util.List<com.android.server.timezonedetector.StateChangeListener> configurationInternalListeners;
        synchronized (this) {
            configurationInternalListeners = new java.util.ArrayList<>(this.mConfigurationInternalListeners);
        }
        for (com.android.server.timezonedetector.StateChangeListener changeListener : configurationInternalListeners) {
            changeListener.onChange();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUserRestrictionsChangeOnMainThread(int userId, android.os.Bundle newRestrictions, android.os.Bundle prevRestrictions) {
        handleConfigurationInternalChangeOnMainThread();
    }

    @Override // com.android.server.timedetector.ServiceConfigAccessor
    public synchronized void addConfigurationInternalChangeListener(com.android.server.timezonedetector.StateChangeListener listener) {
        this.mConfigurationInternalListeners.add((com.android.server.timezonedetector.StateChangeListener) java.util.Objects.requireNonNull(listener));
    }

    @Override // com.android.server.timedetector.ServiceConfigAccessor
    public synchronized void removeConfigurationInternalChangeListener(com.android.server.timezonedetector.StateChangeListener listener) {
        this.mConfigurationInternalListeners.remove(java.util.Objects.requireNonNull(listener));
    }

    @Override // com.android.server.timedetector.ServiceConfigAccessor
    public synchronized com.android.server.timedetector.ConfigurationInternal getCurrentUserConfigurationInternal() {
        int currentUserId;
        currentUserId = ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).getCurrentUserId();
        return getConfigurationInternal(currentUserId);
    }

    @Override // com.android.server.timedetector.ServiceConfigAccessor
    public synchronized boolean updateConfiguration(int userId, android.app.time.TimeConfiguration requestedConfiguration, boolean bypassUserPolicyChecks) {
        java.util.Objects.requireNonNull(requestedConfiguration);
        android.app.time.TimeCapabilitiesAndConfig capabilitiesAndConfig = getConfigurationInternal(userId).createCapabilitiesAndConfig(bypassUserPolicyChecks);
        android.app.time.TimeCapabilities capabilities = capabilitiesAndConfig.getCapabilities();
        android.app.time.TimeConfiguration oldConfiguration = capabilitiesAndConfig.getConfiguration();
        android.app.time.TimeConfiguration newConfiguration = capabilities.tryApplyConfigChanges(oldConfiguration, requestedConfiguration);
        if (newConfiguration == null) {
            return false;
        }
        storeConfiguration(userId, newConfiguration);
        return true;
    }

    private void storeConfiguration(int userId, android.app.time.TimeConfiguration configuration) {
        java.util.Objects.requireNonNull(configuration);
        if (isAutoDetectionSupported()) {
            boolean autoDetectionEnabled = configuration.isAutoDetectionEnabled();
            setAutoDetectionEnabledIfRequired(autoDetectionEnabled);
        }
    }

    @Override // com.android.server.timedetector.ServiceConfigAccessor
    public synchronized com.android.server.timedetector.ConfigurationInternal getConfigurationInternal(int userId) {
        android.app.timedetector.TimeDetectorHelper timeDetectorHelper;
        timeDetectorHelper = android.app.timedetector.TimeDetectorHelper.INSTANCE;
        return new com.android.server.timedetector.ConfigurationInternal.Builder(userId).setUserConfigAllowed(isUserConfigAllowed(userId)).setAutoDetectionSupported(isAutoDetectionSupported()).setAutoDetectionEnabledSetting(getAutoDetectionEnabledSetting()).setSystemClockUpdateThresholdMillis(getSystemClockUpdateThresholdMillis()).setSystemClockConfidenceThresholdMillis(getSystemClockConfidenceUpgradeThresholdMillis()).setAutoSuggestionLowerBound(getAutoSuggestionLowerBound()).setManualSuggestionLowerBound(timeDetectorHelper.getManualSuggestionLowerBound()).setSuggestionUpperBound(timeDetectorHelper.getSuggestionUpperBound()).setOriginPriorities(getOriginPriorities()).build();
    }

    private void setAutoDetectionEnabledIfRequired(boolean z) {
        if (getAutoDetectionEnabledSetting() != z) {
            android.provider.Settings.Global.putInt(this.mCr, "auto_time", z ? 1 : 0);
        }
    }

    private boolean isUserConfigAllowed(int userId) {
        android.os.UserHandle userHandle = android.os.UserHandle.of(userId);
        return !this.mUserManager.hasUserRestriction("no_config_date_time", userHandle);
    }

    private boolean getAutoDetectionEnabledSetting() {
        return android.provider.Settings.Global.getInt(this.mCr, "auto_time", 1) > 0;
    }

    private boolean isAutoDetectionSupported() {
        int[] originsSupported = getOriginPriorities();
        for (int originSupported : originsSupported) {
            if (originSupported == 3 || originSupported == 5 || originSupported == 4) {
                return true;
            }
            if (originSupported == 1) {
                boolean deviceHasTelephony = this.mContext.getPackageManager().hasSystemFeature("android.hardware.telephony");
                if (deviceHasTelephony) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getSystemClockUpdateThresholdMillis() {
        return this.mSystemClockUpdateThresholdMillis;
    }

    private int getSystemClockConfidenceUpgradeThresholdMillis() {
        return 1000;
    }

    private java.time.Instant getAutoSuggestionLowerBound() {
        return this.mServerFlags.getOptionalInstant(com.android.server.timedetector.ServerFlags.KEY_TIME_DETECTOR_LOWER_BOUND_MILLIS_OVERRIDE).orElse(android.app.timedetector.TimeDetectorHelper.INSTANCE.getAutoSuggestionLowerBoundDefault());
    }

    private int[] getOriginPriorities() {
        int[] serverFlagsValue = this.mServerFlagsOriginPrioritiesSupplier.get();
        if (serverFlagsValue != null) {
            return serverFlagsValue;
        }
        int[] configValue = this.mConfigOriginPrioritiesSupplier.get();
        if (configValue != null) {
            return configValue;
        }
        return DEFAULT_AUTOMATIC_TIME_ORIGIN_PRIORITIES;
    }

    private static abstract class BaseOriginPrioritiesSupplier implements java.util.function.Supplier<int[]> {
        private int[] mLastPriorityInts;
        private java.lang.String[] mLastPriorityStrings;

        protected abstract java.lang.String[] lookupPriorityStrings();

        private BaseOriginPrioritiesSupplier() {
        }

        @Override // java.util.function.Supplier
        public int[] get() {
            java.lang.String[] priorityStrings = lookupPriorityStrings();
            synchronized (this) {
                if (java.util.Arrays.equals(this.mLastPriorityStrings, priorityStrings)) {
                    return this.mLastPriorityInts;
                }
                int[] priorityInts = null;
                if (priorityStrings != null) {
                    priorityInts = new int[priorityStrings.length];
                    for (int i = 0; i < priorityInts.length; i++) {
                        try {
                            java.lang.String priorityString = priorityStrings[i];
                            com.android.internal.util.Preconditions.checkArgument(priorityString != null);
                            priorityInts[i] = com.android.server.timedetector.TimeDetectorStrategy.stringToOrigin(priorityString.trim());
                        } catch (java.lang.IllegalArgumentException e) {
                            priorityInts = null;
                        }
                    }
                }
                this.mLastPriorityStrings = priorityStrings;
                this.mLastPriorityInts = priorityInts;
                return priorityInts;
            }
        }
    }

    private static class ConfigOriginPrioritiesSupplier extends com.android.server.timedetector.ServiceConfigAccessorImpl.BaseOriginPrioritiesSupplier {
        private final android.content.Context mContext;

        private ConfigOriginPrioritiesSupplier(android.content.Context context) {
            super();
            this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        }

        @Override // com.android.server.timedetector.ServiceConfigAccessorImpl.BaseOriginPrioritiesSupplier
        protected java.lang.String[] lookupPriorityStrings() {
            return this.mContext.getResources().getStringArray(android.R.array.config_autoRotationTiltTolerance);
        }
    }

    private static class ServerFlagsOriginPrioritiesSupplier extends com.android.server.timedetector.ServiceConfigAccessorImpl.BaseOriginPrioritiesSupplier {
        private final com.android.server.timedetector.ServerFlags mServerFlags;

        private ServerFlagsOriginPrioritiesSupplier(com.android.server.timedetector.ServerFlags serverFlags) {
            super();
            this.mServerFlags = (com.android.server.timedetector.ServerFlags) java.util.Objects.requireNonNull(serverFlags);
        }

        @Override // com.android.server.timedetector.ServiceConfigAccessorImpl.BaseOriginPrioritiesSupplier
        protected java.lang.String[] lookupPriorityStrings() {
            java.util.Optional<java.lang.String[]> priorityStrings = this.mServerFlags.getOptionalStringArray(com.android.server.timedetector.ServerFlags.KEY_TIME_DETECTOR_ORIGIN_PRIORITIES_OVERRIDE);
            return priorityStrings.orElse(null);
        }
    }
}
