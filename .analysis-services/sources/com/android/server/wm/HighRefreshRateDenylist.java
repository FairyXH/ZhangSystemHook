package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class HighRefreshRateDenylist {
    private final java.lang.String[] mDefaultDenylist;
    private final android.util.ArraySet<java.lang.String> mDenylistedPackages = new android.util.ArraySet<>();
    private final java.lang.Object mLock = new java.lang.Object();

    static com.android.server.wm.HighRefreshRateDenylist create(android.content.res.Resources r) {
        return new com.android.server.wm.HighRefreshRateDenylist(r, android.provider.DeviceConfigInterface.REAL);
    }

    HighRefreshRateDenylist(android.content.res.Resources r, android.provider.DeviceConfigInterface deviceConfig) {
        this.mDefaultDenylist = r.getStringArray(android.R.array.config_highAmbientBrightnessThresholdsOfFixedRefreshRate);
        deviceConfig.addOnPropertiesChangedListener("display_manager", com.android.internal.os.BackgroundThread.getExecutor(), new com.android.server.wm.HighRefreshRateDenylist.OnPropertiesChangedListener());
        java.lang.String property = deviceConfig.getProperty("display_manager", "high_refresh_rate_blacklist");
        updateDenylist(property);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDenylist(java.lang.String property) {
        synchronized (this.mLock) {
            this.mDenylistedPackages.clear();
            int i = 0;
            if (property != null) {
                java.lang.String[] packages = property.split(",");
                int length = packages.length;
                while (i < length) {
                    java.lang.String pkg = packages[i];
                    java.lang.String pkgName = pkg.trim();
                    if (!pkgName.isEmpty()) {
                        this.mDenylistedPackages.add(pkgName);
                    }
                    i++;
                }
            } else {
                java.lang.String[] strArr = this.mDefaultDenylist;
                int length2 = strArr.length;
                while (i < length2) {
                    java.lang.String pkg2 = strArr[i];
                    this.mDenylistedPackages.add(pkg2);
                    i++;
                }
            }
        }
    }

    boolean isDenylisted(java.lang.String packageName) {
        boolean zContains;
        synchronized (this.mLock) {
            zContains = this.mDenylistedPackages.contains(packageName);
        }
        return zContains;
    }

    void dump(java.io.PrintWriter pw) {
        pw.println("High Refresh Rate Denylist");
        pw.println("  Packages:");
        synchronized (this.mLock) {
            for (java.lang.String pkg : this.mDenylistedPackages) {
                pw.println("    " + pkg);
            }
        }
    }

    private class OnPropertiesChangedListener implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        private OnPropertiesChangedListener() {
        }

        public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            if (properties.getKeyset().contains("high_refresh_rate_blacklist")) {
                com.android.server.wm.HighRefreshRateDenylist.this.updateDenylist(properties.getString("high_refresh_rate_blacklist", (java.lang.String) null));
            }
        }
    }
}
