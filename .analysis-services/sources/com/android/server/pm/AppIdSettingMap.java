package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
final class AppIdSettingMap {
    private int mFirstAvailableAppId;
    private final com.android.server.utils.WatchedArrayList<com.android.server.pm.SettingBase> mNonSystemSettings;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedArrayList<com.android.server.pm.SettingBase>> mNonSystemSettingsSnapshot;
    private final com.android.server.utils.WatchedSparseArray<com.android.server.pm.SettingBase> mSystemSettings;
    private final com.android.server.utils.SnapshotCache<com.android.server.utils.WatchedSparseArray<com.android.server.pm.SettingBase>> mSystemSettingsSnapshot;

    AppIdSettingMap() {
        this.mFirstAvailableAppId = 10000;
        this.mNonSystemSettings = new com.android.server.utils.WatchedArrayList<>();
        this.mNonSystemSettingsSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mNonSystemSettings, this.mNonSystemSettings, "AppIdSettingMap.mNonSystemSettings");
        this.mSystemSettings = new com.android.server.utils.WatchedSparseArray<>();
        this.mSystemSettingsSnapshot = new com.android.server.utils.SnapshotCache.Auto(this.mSystemSettings, this.mSystemSettings, "AppIdSettingMap.mSystemSettings");
    }

    AppIdSettingMap(com.android.server.pm.AppIdSettingMap orig) {
        this.mFirstAvailableAppId = 10000;
        this.mNonSystemSettings = orig.mNonSystemSettingsSnapshot.snapshot();
        this.mNonSystemSettingsSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.mSystemSettings = orig.mSystemSettingsSnapshot.snapshot();
        this.mSystemSettingsSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
    }

    public boolean registerExistingAppId(int appId, com.android.server.pm.SettingBase setting, java.lang.Object name) {
        if (appId >= 10000) {
            int index = appId - 10000;
            for (int size = this.mNonSystemSettings.size(); index >= size; size++) {
                this.mNonSystemSettings.add(null);
            }
            if (this.mNonSystemSettings.get(index) != null) {
                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Adding duplicate app id: " + appId + " name=" + name);
                return false;
            }
            this.mNonSystemSettings.set(index, setting);
            return true;
        }
        if (this.mSystemSettings.get(appId) != null) {
            com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Adding duplicate shared id: " + appId + " name=" + name);
            return false;
        }
        this.mSystemSettings.put(appId, setting);
        return true;
    }

    public com.android.server.pm.SettingBase getSetting(int appId) {
        if (appId >= 10000) {
            int size = this.mNonSystemSettings.size();
            int index = appId - 10000;
            if (index < size) {
                return this.mNonSystemSettings.get(index);
            }
            return null;
        }
        return this.mSystemSettings.get(appId);
    }

    public void removeSetting(int appId) {
        if (appId >= 10000) {
            int size = this.mNonSystemSettings.size();
            int index = appId - 10000;
            if (index < size) {
                this.mNonSystemSettings.set(index, null);
            }
        } else {
            this.mSystemSettings.remove(appId);
        }
        setFirstAvailableAppId(appId + 1);
    }

    private void setFirstAvailableAppId(int uid) {
        if (uid > this.mFirstAvailableAppId) {
            this.mFirstAvailableAppId = uid;
        }
    }

    public void replaceSetting(int appId, com.android.server.pm.SettingBase setting) {
        if (appId >= 10000) {
            int size = this.mNonSystemSettings.size();
            int index = appId - 10000;
            if (index < size) {
                this.mNonSystemSettings.set(index, setting);
                return;
            } else {
                com.android.server.pm.PackageManagerService.reportSettingsProblem(5, "Error in package manager settings: calling replaceAppIdLpw to replace SettingBase at appId=" + appId + " but nothing is replaced.");
                return;
            }
        }
        this.mSystemSettings.put(appId, setting);
    }

    public int acquireAndRegisterNewAppId(com.android.server.pm.SettingBase obj) {
        int size = this.mNonSystemSettings.size();
        for (int i = this.mFirstAvailableAppId - 10000; i < size; i++) {
            if (this.mNonSystemSettings.get(i) == null) {
                this.mNonSystemSettings.set(i, obj);
                return i + 10000;
            }
        }
        if (size > 9999) {
            return -1;
        }
        this.mNonSystemSettings.add(obj);
        return size + 10000;
    }

    public com.android.server.pm.AppIdSettingMap snapshot() {
        return new com.android.server.pm.AppIdSettingMap(this);
    }

    public void registerObserver(com.android.server.utils.Watcher observer) {
        this.mNonSystemSettings.registerObserver(observer);
        this.mSystemSettings.registerObserver(observer);
    }
}
