package com.android.server.vr;

/* JADX INFO: loaded from: classes3.dex */
public class EnabledComponentsObserver implements com.android.server.vr.SettingsObserver.SettingChangeListener {
    public static final int DISABLED = -1;
    private static final java.lang.String ENABLED_SERVICES_SEPARATOR = ":";
    public static final int NOT_INSTALLED = -2;
    public static final int NO_ERROR = 0;
    private static final java.lang.String TAG = com.android.server.vr.EnabledComponentsObserver.class.getSimpleName();
    private final android.content.Context mContext;
    private final java.lang.Object mLock;
    private final java.lang.String mServiceName;
    private final java.lang.String mServicePermission;
    private final java.lang.String mSettingName;
    private final android.util.SparseArray<android.util.ArraySet<android.content.ComponentName>> mInstalledSet = new android.util.SparseArray<>();
    private final android.util.SparseArray<android.util.ArraySet<android.content.ComponentName>> mEnabledSet = new android.util.SparseArray<>();
    private final java.util.Set<com.android.server.vr.EnabledComponentsObserver.EnabledComponentChangeListener> mEnabledComponentListeners = new android.util.ArraySet();

    public interface EnabledComponentChangeListener {
        void onEnabledComponentChanged();
    }

    private EnabledComponentsObserver(android.content.Context context, java.lang.String settingName, java.lang.String servicePermission, java.lang.String serviceName, java.lang.Object lock, java.util.Collection<com.android.server.vr.EnabledComponentsObserver.EnabledComponentChangeListener> listeners) {
        this.mLock = lock;
        this.mContext = context;
        this.mSettingName = settingName;
        this.mServiceName = serviceName;
        this.mServicePermission = servicePermission;
        this.mEnabledComponentListeners.addAll(listeners);
    }

    public static com.android.server.vr.EnabledComponentsObserver build(android.content.Context context, android.os.Handler handler, java.lang.String settingName, android.os.Looper looper, java.lang.String servicePermission, java.lang.String serviceName, java.lang.Object lock, java.util.Collection<com.android.server.vr.EnabledComponentsObserver.EnabledComponentChangeListener> listeners) {
        com.android.server.vr.SettingsObserver s = com.android.server.vr.SettingsObserver.build(context, handler, settingName);
        final com.android.server.vr.EnabledComponentsObserver o = new com.android.server.vr.EnabledComponentsObserver(context, settingName, servicePermission, serviceName, lock, listeners);
        com.android.internal.content.PackageMonitor packageMonitor = new com.android.internal.content.PackageMonitor(true) { // from class: com.android.server.vr.EnabledComponentsObserver.1
            public void onSomePackagesChanged() {
                o.onPackagesChanged();
            }

            public void onPackageDisappeared(java.lang.String packageName, int reason) {
                o.onPackagesChanged();
            }

            public void onPackageModified(java.lang.String packageName) {
                o.onPackagesChanged();
            }

            public boolean onHandleForceStop(android.content.Intent intent, java.lang.String[] packages, int uid, boolean doit) {
                o.onPackagesChanged();
                return super.onHandleForceStop(intent, packages, uid, doit);
            }
        };
        packageMonitor.register(context, looper, android.os.UserHandle.ALL, true);
        s.addListener(o);
        return o;
    }

    public void onPackagesChanged() {
        rebuildAll();
    }

    @Override // com.android.server.vr.SettingsObserver.SettingChangeListener
    public void onSettingChanged() {
        rebuildAll();
    }

    @Override // com.android.server.vr.SettingsObserver.SettingChangeListener
    public void onSettingRestored(java.lang.String prevValue, java.lang.String newValue, int userId) {
        rebuildAll();
    }

    public void onUsersChanged() {
        rebuildAll();
    }

    public void rebuildAll() {
        synchronized (this.mLock) {
            this.mInstalledSet.clear();
            this.mEnabledSet.clear();
            int[] userIds = getCurrentProfileIds();
            for (int i : userIds) {
                android.util.ArraySet<android.content.ComponentName> implementingPackages = loadComponentNamesForUser(i);
                android.util.ArraySet<android.content.ComponentName> packagesFromSettings = loadComponentNamesFromSetting(this.mSettingName, i);
                packagesFromSettings.retainAll(implementingPackages);
                this.mInstalledSet.put(i, implementingPackages);
                this.mEnabledSet.put(i, packagesFromSettings);
            }
        }
        sendSettingChanged();
    }

    public int isValid(android.content.ComponentName component, int userId) {
        synchronized (this.mLock) {
            android.util.ArraySet<android.content.ComponentName> installedComponents = this.mInstalledSet.get(userId);
            if (installedComponents != null && installedComponents.contains(component)) {
                android.util.ArraySet<android.content.ComponentName> validComponents = this.mEnabledSet.get(userId);
                if (validComponents != null && validComponents.contains(component)) {
                    return 0;
                }
                return -1;
            }
            return -2;
        }
    }

    public android.util.ArraySet<android.content.ComponentName> getInstalled(int userId) {
        synchronized (this.mLock) {
            android.util.ArraySet<android.content.ComponentName> ret = this.mInstalledSet.get(userId);
            if (ret != null) {
                return ret;
            }
            return new android.util.ArraySet<>();
        }
    }

    public android.util.ArraySet<android.content.ComponentName> getEnabled(int userId) {
        synchronized (this.mLock) {
            android.util.ArraySet<android.content.ComponentName> ret = this.mEnabledSet.get(userId);
            if (ret != null) {
                return ret;
            }
            return new android.util.ArraySet<>();
        }
    }

    private int[] getCurrentProfileIds() {
        android.os.UserManager userManager = (android.os.UserManager) this.mContext.getSystemService("user");
        if (userManager == null) {
            return null;
        }
        return userManager.getEnabledProfileIds(android.app.ActivityManager.getCurrentUser());
    }

    public static android.util.ArraySet<android.content.ComponentName> loadComponentNames(android.content.pm.PackageManager pm, int userId, java.lang.String serviceName, java.lang.String permissionName) {
        android.util.ArraySet<android.content.ComponentName> installed = new android.util.ArraySet<>();
        android.content.Intent queryIntent = new android.content.Intent(serviceName);
        java.util.List<android.content.pm.ResolveInfo> installedServices = pm.queryIntentServicesAsUser(queryIntent, 786564, userId);
        if (installedServices != null) {
            int count = installedServices.size();
            for (int i = 0; i < count; i++) {
                android.content.pm.ResolveInfo resolveInfo = installedServices.get(i);
                android.content.pm.ServiceInfo info = resolveInfo.serviceInfo;
                android.content.ComponentName component = new android.content.ComponentName(info.packageName, info.name);
                if (!permissionName.equals(info.permission)) {
                    android.util.Slog.w(TAG, "Skipping service " + info.packageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + info.name + ": it does not require the permission " + permissionName);
                } else {
                    installed.add(component);
                }
            }
        }
        return installed;
    }

    private android.util.ArraySet<android.content.ComponentName> loadComponentNamesForUser(int userId) {
        return loadComponentNames(this.mContext.getPackageManager(), userId, this.mServiceName, this.mServicePermission);
    }

    private android.util.ArraySet<android.content.ComponentName> loadComponentNamesFromSetting(java.lang.String settingName, int userId) {
        android.content.ContentResolver cr = this.mContext.getContentResolver();
        java.lang.String settingValue = android.provider.Settings.Secure.getStringForUser(cr, settingName, userId);
        if (android.text.TextUtils.isEmpty(settingValue)) {
            return new android.util.ArraySet<>();
        }
        java.lang.String[] restored = settingValue.split(ENABLED_SERVICES_SEPARATOR);
        android.util.ArraySet<android.content.ComponentName> result = new android.util.ArraySet<>(restored.length);
        for (java.lang.String str : restored) {
            android.content.ComponentName value = android.content.ComponentName.unflattenFromString(str);
            if (value != null) {
                result.add(value);
            }
        }
        return result;
    }

    private void sendSettingChanged() {
        for (com.android.server.vr.EnabledComponentsObserver.EnabledComponentChangeListener l : this.mEnabledComponentListeners) {
            l.onEnabledComponentChanged();
        }
    }
}
