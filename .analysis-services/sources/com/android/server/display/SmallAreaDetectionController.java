package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
final class SmallAreaDetectionController {
    private static final java.lang.String KEY_SMALL_AREA_DETECTION_ALLOWLIST = "small_area_detection_allowlist";
    private final android.content.Context mContext;
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.Map<java.lang.String, java.lang.Float> mAllowPkgMap = new android.util.ArrayMap();
    private final android.content.pm.PackageManagerInternal mPackageManager = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);

    private static native void nativeSetSmallAreaDetectionThreshold(int i, float f);

    private static native void nativeUpdateSmallAreaDetection(int[] iArr, float[] fArr);

    static com.android.server.display.SmallAreaDetectionController create(android.content.Context context) {
        com.android.server.display.SmallAreaDetectionController controller = new com.android.server.display.SmallAreaDetectionController(context, android.provider.DeviceConfigInterface.REAL);
        java.lang.String property = android.provider.DeviceConfigInterface.REAL.getProperty("display_manager", KEY_SMALL_AREA_DETECTION_ALLOWLIST);
        controller.updateAllowlist(property);
        return controller;
    }

    /* JADX WARN: Multi-variable type inference failed */
    SmallAreaDetectionController(android.content.Context context, android.provider.DeviceConfigInterface deviceConfigInterface) {
        this.mContext = context;
        deviceConfigInterface.addOnPropertiesChangedListener("display_manager", com.android.internal.os.BackgroundThread.getExecutor(), new com.android.server.display.SmallAreaDetectionController.OnPropertiesChangedListener());
        this.mPackageManager.getPackageList(new com.android.server.display.SmallAreaDetectionController.PackageReceiver());
    }

    void updateAllowlist(java.lang.String property) {
        java.util.Map<java.lang.String, java.lang.Float> allowPkgMap = new android.util.ArrayMap<>();
        synchronized (this.mLock) {
            this.mAllowPkgMap.clear();
            int i = 0;
            if (property != null) {
                java.lang.String[] mapStrings = property.split(",");
                int length = mapStrings.length;
                while (i < length) {
                    java.lang.String mapString = mapStrings[i];
                    putToAllowlist(mapString);
                    i++;
                }
            } else {
                java.lang.String[] defaultMapStrings = this.mContext.getResources().getStringArray(android.R.array.config_secondaryBuiltInDisplayWaterfallCutout);
                int length2 = defaultMapStrings.length;
                while (i < length2) {
                    java.lang.String defaultMapString = defaultMapStrings[i];
                    putToAllowlist(defaultMapString);
                    i++;
                }
            }
            if (this.mAllowPkgMap.isEmpty()) {
                return;
            }
            allowPkgMap.putAll(this.mAllowPkgMap);
            updateSmallAreaDetection(allowPkgMap);
        }
    }

    private void putToAllowlist(java.lang.String rowData) {
        java.lang.String[] items = rowData.split(":");
        if (items.length == 2) {
            try {
                java.lang.String pkg = items[0];
                float threshold = java.lang.Float.valueOf(items[1]).floatValue();
                this.mAllowPkgMap.put(pkg, java.lang.Float.valueOf(threshold));
            } catch (java.lang.Exception e) {
            }
        }
    }

    private void updateSmallAreaDetection(java.util.Map<java.lang.String, java.lang.Float> allowPkgMap) {
        android.util.SparseArray<java.lang.Float> appIdThresholdList = new android.util.SparseArray<>(allowPkgMap.size());
        for (java.lang.String pkg : allowPkgMap.keySet()) {
            float threshold = allowPkgMap.get(pkg).floatValue();
            com.android.server.pm.pkg.PackageStateInternal stage = this.mPackageManager.getPackageStateInternal(pkg);
            if (stage != null) {
                appIdThresholdList.put(stage.getAppId(), java.lang.Float.valueOf(threshold));
            }
        }
        int[] appIds = new int[appIdThresholdList.size()];
        float[] thresholds = new float[appIdThresholdList.size()];
        for (int i = 0; i < appIdThresholdList.size(); i++) {
            appIds[i] = appIdThresholdList.keyAt(i);
            thresholds[i] = appIdThresholdList.valueAt(i).floatValue();
        }
        updateSmallAreaDetection(appIds, thresholds);
    }

    void updateSmallAreaDetection(int[] appIds, float[] thresholds) {
        nativeUpdateSmallAreaDetection(appIds, thresholds);
    }

    void setSmallAreaDetectionThreshold(int appId, float threshold) {
        nativeSetSmallAreaDetectionThreshold(appId, threshold);
    }

    void dump(java.io.PrintWriter pw) {
        pw.println("Small area detection allowlist");
        pw.println("  Packages:");
        synchronized (this.mLock) {
            for (java.lang.String pkg : this.mAllowPkgMap.keySet()) {
                pw.println("    " + pkg + " threshold = " + this.mAllowPkgMap.get(pkg));
            }
        }
    }

    private class OnPropertiesChangedListener implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        private OnPropertiesChangedListener() {
        }

        public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            if (properties.getKeyset().contains(com.android.server.display.SmallAreaDetectionController.KEY_SMALL_AREA_DETECTION_ALLOWLIST)) {
                com.android.server.display.SmallAreaDetectionController.this.updateAllowlist(properties.getString(com.android.server.display.SmallAreaDetectionController.KEY_SMALL_AREA_DETECTION_ALLOWLIST, (java.lang.String) null));
            }
        }
    }

    private final class PackageReceiver implements android.content.pm.PackageManagerInternal.PackageListObserver {
        private PackageReceiver() {
        }

        @Override // android.content.pm.PackageManagerInternal.PackageListObserver
        public void onPackageAdded(java.lang.String packageName, int uid) {
            float threshold = 0.0f;
            synchronized (com.android.server.display.SmallAreaDetectionController.this.mLock) {
                if (com.android.server.display.SmallAreaDetectionController.this.mAllowPkgMap.containsKey(packageName)) {
                    threshold = ((java.lang.Float) com.android.server.display.SmallAreaDetectionController.this.mAllowPkgMap.get(packageName)).floatValue();
                }
            }
            if (threshold > 0.0f) {
                com.android.server.display.SmallAreaDetectionController.this.setSmallAreaDetectionThreshold(android.os.UserHandle.getAppId(uid), threshold);
            }
        }
    }
}
