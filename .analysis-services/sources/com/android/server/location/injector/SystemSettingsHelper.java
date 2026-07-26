package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public class SystemSettingsHelper extends com.android.server.location.injector.SettingsHelper {
    private static final long DEFAULT_BACKGROUND_THROTTLE_INTERVAL_MS = 1800000;
    private static final long DEFAULT_BACKGROUND_THROTTLE_PROXIMITY_ALERT_INTERVAL_MS = 1800000;
    private static final float DEFAULT_COARSE_LOCATION_ACCURACY_M = 2000.0f;
    private static final java.lang.String LOCATION_PACKAGE_ALLOWLIST = "locationPackagePrefixWhitelist";
    private static final java.lang.String LOCATION_PACKAGE_DENYLIST = "locationPackagePrefixBlacklist";
    private final com.android.server.location.injector.SystemSettingsHelper.LongGlobalSetting mBackgroundThrottleIntervalMs;
    private final com.android.server.location.injector.SystemSettingsHelper.StringSetCachedGlobalSetting mBackgroundThrottlePackageWhitelist;
    private final android.content.Context mContext;
    private final com.android.server.location.injector.SystemSettingsHelper.BooleanGlobalSetting mGnssMeasurementFullTracking;
    private final com.android.server.location.injector.SystemSettingsHelper.IntegerSecureSetting mLocationMode;
    private final com.android.server.location.injector.SystemSettingsHelper.StringListCachedSecureSetting mLocationPackageBlacklist;
    private final com.android.server.location.injector.SystemSettingsHelper.StringListCachedSecureSetting mLocationPackageWhitelist;
    private final com.android.server.location.injector.SystemSettingsHelper.PackageTagsListSetting mAdasPackageAllowlist = new com.android.server.location.injector.SystemSettingsHelper.PackageTagsListSetting("adas_settings_allowlist", new java.util.function.Supplier() { // from class: com.android.server.location.injector.SystemSettingsHelper$$ExternalSyntheticLambda1
        @Override // java.util.function.Supplier
        public final java.lang.Object get() {
            return com.android.server.SystemConfig.getInstance().getAllowAdasLocationSettings();
        }
    });
    private final com.android.server.location.injector.SystemSettingsHelper.PackageTagsListSetting mIgnoreSettingsPackageAllowlist = new com.android.server.location.injector.SystemSettingsHelper.PackageTagsListSetting("ignore_settings_allowlist", new java.util.function.Supplier() { // from class: com.android.server.location.injector.SystemSettingsHelper$$ExternalSyntheticLambda2
        @Override // java.util.function.Supplier
        public final java.lang.Object get() {
            return com.android.server.SystemConfig.getInstance().getAllowIgnoreLocationSettings();
        }
    });

    public SystemSettingsHelper(android.content.Context context) {
        this.mContext = context;
        this.mLocationMode = new com.android.server.location.injector.SystemSettingsHelper.IntegerSecureSetting(context, "location_mode", com.android.server.FgThread.getHandler());
        this.mBackgroundThrottleIntervalMs = new com.android.server.location.injector.SystemSettingsHelper.LongGlobalSetting(context, "location_background_throttle_interval_ms", com.android.server.FgThread.getHandler());
        this.mGnssMeasurementFullTracking = new com.android.server.location.injector.SystemSettingsHelper.BooleanGlobalSetting(context, "enable_gnss_raw_meas_full_tracking", com.android.server.FgThread.getHandler());
        this.mLocationPackageBlacklist = new com.android.server.location.injector.SystemSettingsHelper.StringListCachedSecureSetting(context, LOCATION_PACKAGE_DENYLIST, com.android.server.FgThread.getHandler());
        this.mLocationPackageWhitelist = new com.android.server.location.injector.SystemSettingsHelper.StringListCachedSecureSetting(context, LOCATION_PACKAGE_ALLOWLIST, com.android.server.FgThread.getHandler());
        this.mBackgroundThrottlePackageWhitelist = new com.android.server.location.injector.SystemSettingsHelper.StringSetCachedGlobalSetting(context, "location_background_throttle_package_whitelist", new java.util.function.Supplier() { // from class: com.android.server.location.injector.SystemSettingsHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return com.android.server.SystemConfig.getInstance().getAllowUnthrottledLocation();
            }
        }, com.android.server.FgThread.getHandler());
    }

    public void onSystemReady() {
        this.mLocationMode.register();
        this.mBackgroundThrottleIntervalMs.register();
        this.mLocationPackageBlacklist.register();
        this.mLocationPackageWhitelist.register();
        this.mBackgroundThrottlePackageWhitelist.register();
        this.mIgnoreSettingsPackageAllowlist.register();
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public boolean isLocationEnabled(int userId) {
        return this.mLocationMode.getValueForUser(0, userId) != 0;
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void setLocationEnabled(boolean enabled, int userId) {
        int i;
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.content.ContentResolver contentResolver = this.mContext.getContentResolver();
            if (enabled) {
                i = 3;
            } else {
                i = 0;
            }
            android.provider.Settings.Secure.putIntForUser(contentResolver, "location_mode", i, userId);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void addOnLocationEnabledChangedListener(com.android.server.location.injector.SettingsHelper.UserSettingChangedListener listener) {
        this.mLocationMode.addListener(listener);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void removeOnLocationEnabledChangedListener(com.android.server.location.injector.SettingsHelper.UserSettingChangedListener listener) {
        this.mLocationMode.removeListener(listener);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public long getBackgroundThrottleIntervalMs() {
        return this.mBackgroundThrottleIntervalMs.getValue(1800000L);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void addOnBackgroundThrottleIntervalChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener listener) {
        this.mBackgroundThrottleIntervalMs.addListener(listener);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void removeOnBackgroundThrottleIntervalChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener listener) {
        this.mBackgroundThrottleIntervalMs.removeListener(listener);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public boolean isLocationPackageBlacklisted(int userId, java.lang.String packageName) {
        java.util.List<java.lang.String> locationPackageBlacklist = this.mLocationPackageBlacklist.getValueForUser(userId);
        if (locationPackageBlacklist.isEmpty()) {
            return false;
        }
        java.util.List<java.lang.String> locationPackageWhitelist = this.mLocationPackageWhitelist.getValueForUser(userId);
        for (java.lang.String locationWhitelistPackage : locationPackageWhitelist) {
            if (packageName.startsWith(locationWhitelistPackage)) {
                return false;
            }
        }
        for (java.lang.String locationBlacklistPackage : locationPackageBlacklist) {
            if (packageName.startsWith(locationBlacklistPackage)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void addOnLocationPackageBlacklistChangedListener(com.android.server.location.injector.SettingsHelper.UserSettingChangedListener listener) {
        this.mLocationPackageBlacklist.addListener(listener);
        this.mLocationPackageWhitelist.addListener(listener);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void removeOnLocationPackageBlacklistChangedListener(com.android.server.location.injector.SettingsHelper.UserSettingChangedListener listener) {
        this.mLocationPackageBlacklist.removeListener(listener);
        this.mLocationPackageWhitelist.removeListener(listener);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public java.util.Set<java.lang.String> getBackgroundThrottlePackageWhitelist() {
        return this.mBackgroundThrottlePackageWhitelist.getValue();
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void addOnBackgroundThrottlePackageWhitelistChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener listener) {
        this.mBackgroundThrottlePackageWhitelist.addListener(listener);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void removeOnBackgroundThrottlePackageWhitelistChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener listener) {
        this.mBackgroundThrottlePackageWhitelist.removeListener(listener);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public boolean isGnssMeasurementsFullTrackingEnabled() {
        return this.mGnssMeasurementFullTracking.getValue(false);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void addOnGnssMeasurementsFullTrackingEnabledChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener listener) {
        this.mGnssMeasurementFullTracking.addListener(listener);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void removeOnGnssMeasurementsFullTrackingEnabledChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener listener) {
        this.mGnssMeasurementFullTracking.removeListener(listener);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public android.os.PackageTagsList getAdasAllowlist() {
        return this.mAdasPackageAllowlist.getValue();
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void addAdasAllowlistChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener listener) {
        this.mAdasPackageAllowlist.addListener(listener);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void removeAdasAllowlistChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener listener) {
        this.mAdasPackageAllowlist.removeListener(listener);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public android.os.PackageTagsList getIgnoreSettingsAllowlist() {
        return this.mIgnoreSettingsPackageAllowlist.getValue();
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void addIgnoreSettingsAllowlistChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener listener) {
        this.mIgnoreSettingsPackageAllowlist.addListener(listener);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void removeIgnoreSettingsAllowlistChangedListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener listener) {
        this.mIgnoreSettingsPackageAllowlist.removeListener(listener);
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public long getBackgroundThrottleProximityAlertIntervalMs() {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return android.provider.Settings.Global.getLong(this.mContext.getContentResolver(), "location_background_throttle_proximity_alert_interval_ms", 1800000L);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public float getCoarseLocationAccuracyM() {
        long identity = android.os.Binder.clearCallingIdentity();
        android.content.ContentResolver cr = this.mContext.getContentResolver();
        try {
            return android.provider.Settings.Secure.getFloatForUser(cr, "locationCoarseAccuracy", DEFAULT_COARSE_LOCATION_ACCURACY_M, cr.getUserId());
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @Override // com.android.server.location.injector.SettingsHelper
    public void dump(java.io.FileDescriptor fd, android.util.IndentingPrintWriter ipw, java.lang.String[] args) {
        try {
            int[] userIds = android.app.ActivityManager.getService().getRunningUserIds();
            ipw.print("Location Setting: ");
            ipw.increaseIndent();
            if (userIds.length > 1) {
                ipw.println();
                for (int userId : userIds) {
                    ipw.print("[u");
                    ipw.print(userId);
                    ipw.print("] ");
                    ipw.println(isLocationEnabled(userId));
                }
            } else {
                ipw.println(isLocationEnabled(userIds[0]));
            }
            ipw.decreaseIndent();
            ipw.println("Location Allow/Deny Packages:");
            ipw.increaseIndent();
            if (userIds.length > 1) {
                for (int userId2 : userIds) {
                    java.util.List<java.lang.String> locationPackageBlacklist = this.mLocationPackageBlacklist.getValueForUser(userId2);
                    if (!locationPackageBlacklist.isEmpty()) {
                        ipw.print("user ");
                        ipw.print(userId2);
                        ipw.println(":");
                        ipw.increaseIndent();
                        for (java.lang.String packageName : locationPackageBlacklist) {
                            ipw.print("[deny] ");
                            ipw.println(packageName);
                        }
                        java.util.List<java.lang.String> locationPackageWhitelist = this.mLocationPackageWhitelist.getValueForUser(userId2);
                        for (java.lang.String packageName2 : locationPackageWhitelist) {
                            ipw.print("[allow] ");
                            ipw.println(packageName2);
                        }
                        ipw.decreaseIndent();
                    }
                }
            } else {
                for (java.lang.String packageName3 : this.mLocationPackageBlacklist.getValueForUser(userIds[0])) {
                    ipw.print("[deny] ");
                    ipw.println(packageName3);
                }
                java.util.List<java.lang.String> locationPackageWhitelist2 = this.mLocationPackageWhitelist.getValueForUser(userIds[0]);
                for (java.lang.String packageName4 : locationPackageWhitelist2) {
                    ipw.print("[allow] ");
                    ipw.println(packageName4);
                }
            }
            ipw.decreaseIndent();
            java.util.Set<java.lang.String> backgroundThrottlePackageWhitelist = this.mBackgroundThrottlePackageWhitelist.getValue();
            if (!backgroundThrottlePackageWhitelist.isEmpty()) {
                ipw.println("Throttling Allow Packages:");
                ipw.increaseIndent();
                for (java.lang.String packageName5 : backgroundThrottlePackageWhitelist) {
                    ipw.println(packageName5);
                }
                ipw.decreaseIndent();
            }
            android.os.PackageTagsList ignoreSettingsAllowlist = this.mIgnoreSettingsPackageAllowlist.getValue();
            if (!ignoreSettingsAllowlist.isEmpty()) {
                ipw.println("Emergency Bypass Allow Packages:");
                ipw.increaseIndent();
                ignoreSettingsAllowlist.dump(ipw);
                ipw.decreaseIndent();
            }
            android.os.PackageTagsList adasPackageAllowlist = this.mAdasPackageAllowlist.getValue();
            if (!adasPackageAllowlist.isEmpty()) {
                ipw.println("ADAS Bypass Allow Packages:");
                ipw.increaseIndent();
                adasPackageAllowlist.dump(ipw);
                ipw.decreaseIndent();
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static abstract class ObservingSetting extends android.database.ContentObserver {
        private final java.util.concurrent.CopyOnWriteArrayList<com.android.server.location.injector.SettingsHelper.UserSettingChangedListener> mListeners;
        private boolean mRegistered;

        ObservingSetting(android.os.Handler handler) {
            super(handler);
            this.mListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
        }

        protected synchronized boolean isRegistered() {
            return this.mRegistered;
        }

        protected synchronized void register(android.content.Context context, android.net.Uri uri) {
            if (this.mRegistered) {
                return;
            }
            context.getContentResolver().registerContentObserver(uri, false, this, -1);
            this.mRegistered = true;
        }

        public void addListener(com.android.server.location.injector.SettingsHelper.UserSettingChangedListener listener) {
            this.mListeners.add(listener);
        }

        public void removeListener(com.android.server.location.injector.SettingsHelper.UserSettingChangedListener listener) {
            this.mListeners.remove(listener);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
            if (com.android.server.location.LocationManagerService.D) {
                android.util.Log.d(com.android.server.location.LocationManagerService.TAG, "location setting changed [u" + userId + "]: " + uri);
            }
            for (com.android.server.location.injector.SettingsHelper.UserSettingChangedListener listener : this.mListeners) {
                listener.onSettingChanged(userId);
            }
        }
    }

    private static class IntegerSecureSetting extends com.android.server.location.injector.SystemSettingsHelper.ObservingSetting {
        private final android.content.Context mContext;
        private final java.lang.String mSettingName;

        IntegerSecureSetting(android.content.Context context, java.lang.String settingName, android.os.Handler handler) {
            super(handler);
            this.mContext = context;
            this.mSettingName = settingName;
        }

        void register() {
            register(this.mContext, android.provider.Settings.Secure.getUriFor(this.mSettingName));
        }

        public int getValueForUser(int defaultValue, int userId) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), this.mSettingName, defaultValue, userId);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    private static class StringListCachedSecureSetting extends com.android.server.location.injector.SystemSettingsHelper.ObservingSetting {
        private int mCachedUserId;
        private java.util.List<java.lang.String> mCachedValue;
        private final android.content.Context mContext;
        private final java.lang.String mSettingName;

        StringListCachedSecureSetting(android.content.Context context, java.lang.String settingName, android.os.Handler handler) {
            super(handler);
            this.mContext = context;
            this.mSettingName = settingName;
            this.mCachedUserId = -10000;
        }

        public void register() {
            register(this.mContext, android.provider.Settings.Secure.getUriFor(this.mSettingName));
        }

        public synchronized java.util.List<java.lang.String> getValueForUser(int userId) {
            java.util.List<java.lang.String> value;
            com.android.internal.util.Preconditions.checkArgument(userId != -10000);
            value = this.mCachedValue;
            if (userId != this.mCachedUserId) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    java.lang.String setting = android.provider.Settings.Secure.getStringForUser(this.mContext.getContentResolver(), this.mSettingName, userId);
                    if (android.text.TextUtils.isEmpty(setting)) {
                        try {
                            value = java.util.Collections.emptyList();
                        } catch (java.lang.Throwable th) {
                            th = th;
                            android.os.Binder.restoreCallingIdentity(identity);
                            throw th;
                        }
                    } else {
                        value = java.util.Arrays.asList(setting.split(","));
                    }
                    android.os.Binder.restoreCallingIdentity(identity);
                    if (isRegistered()) {
                        this.mCachedUserId = userId;
                        this.mCachedValue = value;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
            return value;
        }

        public synchronized void invalidateForUser(int userId) {
            if (this.mCachedUserId == userId) {
                this.mCachedUserId = -10000;
                this.mCachedValue = null;
            }
        }

        @Override // com.android.server.location.injector.SystemSettingsHelper.ObservingSetting, android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
            invalidateForUser(userId);
            super.onChange(selfChange, uri, userId);
        }
    }

    private static class BooleanGlobalSetting extends com.android.server.location.injector.SystemSettingsHelper.ObservingSetting {
        private final android.content.Context mContext;
        private final java.lang.String mSettingName;

        BooleanGlobalSetting(android.content.Context context, java.lang.String settingName, android.os.Handler handler) {
            super(handler);
            this.mContext = context;
            this.mSettingName = settingName;
        }

        public void register() {
            register(this.mContext, android.provider.Settings.Global.getUriFor(this.mSettingName));
        }

        public boolean getValue(boolean defaultValue) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), this.mSettingName, defaultValue ? 1 : 0) != 0;
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    private static class LongGlobalSetting extends com.android.server.location.injector.SystemSettingsHelper.ObservingSetting {
        private final android.content.Context mContext;
        private final java.lang.String mSettingName;

        LongGlobalSetting(android.content.Context context, java.lang.String settingName, android.os.Handler handler) {
            super(handler);
            this.mContext = context;
            this.mSettingName = settingName;
        }

        public void register() {
            register(this.mContext, android.provider.Settings.Global.getUriFor(this.mSettingName));
        }

        public long getValue(long defaultValue) {
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                return android.provider.Settings.Global.getLong(this.mContext.getContentResolver(), this.mSettingName, defaultValue);
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }
    }

    private static class StringSetCachedGlobalSetting extends com.android.server.location.injector.SystemSettingsHelper.ObservingSetting {
        private final java.util.function.Supplier<android.util.ArraySet<java.lang.String>> mBaseValuesSupplier;
        private android.util.ArraySet<java.lang.String> mCachedValue;
        private final android.content.Context mContext;
        private final java.lang.String mSettingName;
        private boolean mValid;

        StringSetCachedGlobalSetting(android.content.Context context, java.lang.String settingName, java.util.function.Supplier<android.util.ArraySet<java.lang.String>> baseValuesSupplier, android.os.Handler handler) {
            super(handler);
            this.mContext = context;
            this.mSettingName = settingName;
            this.mBaseValuesSupplier = baseValuesSupplier;
            this.mValid = false;
        }

        public void register() {
            register(this.mContext, android.provider.Settings.Global.getUriFor(this.mSettingName));
        }

        public synchronized java.util.Set<java.lang.String> getValue() {
            android.util.ArraySet<java.lang.String> value;
            value = this.mCachedValue;
            if (!this.mValid) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    value = new android.util.ArraySet<>(this.mBaseValuesSupplier.get());
                    java.lang.String setting = android.provider.Settings.Global.getString(this.mContext.getContentResolver(), this.mSettingName);
                    if (!android.text.TextUtils.isEmpty(setting)) {
                        try {
                            value.addAll(java.util.Arrays.asList(setting.split(",")));
                        } catch (java.lang.Throwable th) {
                            th = th;
                            android.os.Binder.restoreCallingIdentity(identity);
                            throw th;
                        }
                    }
                    android.os.Binder.restoreCallingIdentity(identity);
                    if (isRegistered()) {
                        this.mValid = true;
                        this.mCachedValue = value;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
            return value;
        }

        public synchronized void invalidate() {
            this.mValid = false;
            this.mCachedValue = null;
        }

        @Override // com.android.server.location.injector.SystemSettingsHelper.ObservingSetting, android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
            invalidate();
            super.onChange(selfChange, uri, userId);
        }
    }

    private static class DeviceConfigSetting implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        private final java.util.concurrent.CopyOnWriteArrayList<com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener> mListeners = new java.util.concurrent.CopyOnWriteArrayList<>();
        protected final java.lang.String mName;
        private boolean mRegistered;

        DeviceConfigSetting(java.lang.String name) {
            this.mName = name;
        }

        protected synchronized boolean isRegistered() {
            return this.mRegistered;
        }

        protected synchronized void register() {
            if (this.mRegistered) {
                return;
            }
            android.provider.DeviceConfig.addOnPropertiesChangedListener("location", com.android.server.FgThread.getExecutor(), this);
            this.mRegistered = true;
        }

        public void addListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener listener) {
            this.mListeners.add(listener);
        }

        public void removeListener(com.android.server.location.injector.SettingsHelper.GlobalSettingChangedListener listener) {
            this.mListeners.remove(listener);
        }

        public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            if (!properties.getKeyset().contains(this.mName)) {
                return;
            }
            onPropertiesChanged();
        }

        public void onPropertiesChanged() {
            if (com.android.server.location.LocationManagerService.D) {
                android.util.Log.d(com.android.server.location.LocationManagerService.TAG, "location device config setting changed: " + this.mName);
            }
            for (com.android.server.location.injector.SettingsHelper.UserSettingChangedListener listener : this.mListeners) {
                listener.onSettingChanged(-1);
            }
        }
    }

    private static class PackageTagsListSetting extends com.android.server.location.injector.SystemSettingsHelper.DeviceConfigSetting {
        private final java.util.function.Supplier<android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>>> mBaseValuesSupplier;
        private android.os.PackageTagsList mCachedValue;
        private boolean mValid;

        PackageTagsListSetting(java.lang.String name, java.util.function.Supplier<android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>>> baseValuesSupplier) {
            super(name);
            this.mBaseValuesSupplier = baseValuesSupplier;
        }

        public synchronized android.os.PackageTagsList getValue() {
            android.os.PackageTagsList value;
            value = this.mCachedValue;
            if (!this.mValid) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    android.os.PackageTagsList.Builder builder = new android.os.PackageTagsList.Builder().add(this.mBaseValuesSupplier.get());
                    java.lang.String setting = android.provider.DeviceConfig.getProperty("location", this.mName);
                    if (!android.text.TextUtils.isEmpty(setting)) {
                        try {
                            java.lang.String[] strArrSplit = setting.split(",");
                            int length = strArrSplit.length;
                            char c = 0;
                            int i = 0;
                            while (i < length) {
                                java.lang.String packageAndTags = strArrSplit[i];
                                if (!android.text.TextUtils.isEmpty(packageAndTags)) {
                                    java.lang.String[] packageThenTags = packageAndTags.split(";");
                                    java.lang.String packageName = packageThenTags[c];
                                    if (packageThenTags.length == 1) {
                                        builder.add(packageName);
                                    } else {
                                        for (int i2 = 1; i2 < packageThenTags.length; i2++) {
                                            java.lang.String attributionTag = packageThenTags[i2];
                                            if ("null".equals(attributionTag)) {
                                                attributionTag = null;
                                            }
                                            if (com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER.equals(attributionTag)) {
                                                builder.add(packageName);
                                            } else {
                                                builder.add(packageName, attributionTag);
                                            }
                                        }
                                    }
                                }
                                i++;
                                c = 0;
                            }
                        } catch (java.lang.Throwable th) {
                            th = th;
                            android.os.Binder.restoreCallingIdentity(identity);
                            throw th;
                        }
                    }
                    value = builder.build();
                    android.os.Binder.restoreCallingIdentity(identity);
                    if (isRegistered()) {
                        this.mValid = true;
                        this.mCachedValue = value;
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
            return value;
        }

        public synchronized void invalidate() {
            this.mValid = false;
            this.mCachedValue = null;
        }

        @Override // com.android.server.location.injector.SystemSettingsHelper.DeviceConfigSetting
        public void onPropertiesChanged() {
            invalidate();
            super.onPropertiesChanged();
        }
    }
}
