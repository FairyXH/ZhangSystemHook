package com.android.server.display.color;

/* JADX INFO: loaded from: classes2.dex */
class AppSaturationController {
    static final float[] TRANSLATION_VECTOR = {0.0f, 0.0f, 0.0f};
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.Map<java.lang.String, android.util.SparseArray<com.android.server.display.color.AppSaturationController.SaturationController>> mAppsMap = new java.util.HashMap();

    AppSaturationController() {
    }

    boolean addColorTransformController(java.lang.String packageName, int userId, java.lang.ref.WeakReference<com.android.server.display.color.ColorDisplayService.ColorTransformController> controller) {
        boolean zAddColorTransformController;
        synchronized (this.mLock) {
            zAddColorTransformController = getSaturationControllerLocked(packageName, userId).addColorTransformController(controller);
        }
        return zAddColorTransformController;
    }

    public boolean setSaturationLevel(java.lang.String callingPackageName, java.lang.String affectedPackageName, int userId, int saturationLevel) {
        boolean saturationLevel2;
        synchronized (this.mLock) {
            saturationLevel2 = getSaturationControllerLocked(affectedPackageName, userId).setSaturationLevel(callingPackageName, saturationLevel);
        }
        return saturationLevel2;
    }

    public void dump(java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            pw.println("App Saturation: ");
            if (this.mAppsMap.size() == 0) {
                pw.println("    No packages");
                return;
            }
            java.util.List<java.lang.String> packageNames = new java.util.ArrayList<>(this.mAppsMap.keySet());
            java.util.Collections.sort(packageNames);
            for (java.lang.String packageName : packageNames) {
                pw.println("    " + packageName + ":");
                android.util.SparseArray<com.android.server.display.color.AppSaturationController.SaturationController> appUserIdMap = this.mAppsMap.get(packageName);
                for (int i = 0; i < appUserIdMap.size(); i++) {
                    pw.println("        " + appUserIdMap.keyAt(i) + ":");
                    appUserIdMap.valueAt(i).dump(pw);
                }
            }
        }
    }

    private com.android.server.display.color.AppSaturationController.SaturationController getSaturationControllerLocked(java.lang.String packageName, int userId) {
        return getOrCreateSaturationControllerLocked(getOrCreateUserIdMapLocked(packageName), userId);
    }

    private android.util.SparseArray<com.android.server.display.color.AppSaturationController.SaturationController> getOrCreateUserIdMapLocked(java.lang.String packageName) {
        if (this.mAppsMap.get(packageName) != null) {
            return this.mAppsMap.get(packageName);
        }
        android.util.SparseArray<com.android.server.display.color.AppSaturationController.SaturationController> appUserIdMap = new android.util.SparseArray<>();
        this.mAppsMap.put(packageName, appUserIdMap);
        return appUserIdMap;
    }

    private com.android.server.display.color.AppSaturationController.SaturationController getOrCreateSaturationControllerLocked(android.util.SparseArray<com.android.server.display.color.AppSaturationController.SaturationController> appUserIdMap, int userId) {
        if (appUserIdMap.get(userId) != null) {
            return appUserIdMap.get(userId);
        }
        com.android.server.display.color.AppSaturationController.SaturationController saturationController = new com.android.server.display.color.AppSaturationController.SaturationController();
        appUserIdMap.put(userId, saturationController);
        return saturationController;
    }

    static void computeGrayscaleTransformMatrix(float saturation, float[] matrix) {
        float desaturation = 1.0f - saturation;
        float[] luminance = {0.231f * desaturation, 0.715f * desaturation, 0.072f * desaturation};
        matrix[0] = luminance[0] + saturation;
        matrix[1] = luminance[0];
        matrix[2] = luminance[0];
        matrix[3] = luminance[1];
        matrix[4] = luminance[1] + saturation;
        matrix[5] = luminance[1];
        matrix[6] = luminance[2];
        matrix[7] = luminance[2];
        matrix[8] = luminance[2] + saturation;
    }

    private static class SaturationController {
        private static final int FULL_SATURATION = 100;
        private final java.util.List<java.lang.ref.WeakReference<com.android.server.display.color.ColorDisplayService.ColorTransformController>> mControllerRefs;
        private final android.util.ArrayMap<java.lang.String, java.lang.Integer> mSaturationLevels;
        private float[] mTransformMatrix;

        private SaturationController() {
            this.mControllerRefs = new java.util.ArrayList();
            this.mSaturationLevels = new android.util.ArrayMap<>();
            this.mTransformMatrix = new float[9];
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean setSaturationLevel(java.lang.String callingPackageName, int saturationLevel) {
            if (saturationLevel == 100) {
                this.mSaturationLevels.remove(callingPackageName);
            } else {
                this.mSaturationLevels.put(callingPackageName, java.lang.Integer.valueOf(saturationLevel));
            }
            if (!this.mControllerRefs.isEmpty()) {
                return updateState();
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean addColorTransformController(java.lang.ref.WeakReference<com.android.server.display.color.ColorDisplayService.ColorTransformController> controller) {
            clearExpiredReferences();
            this.mControllerRefs.add(controller);
            if (!this.mSaturationLevels.isEmpty()) {
                return updateState();
            }
            return false;
        }

        private int calculateSaturationLevel() {
            int saturationLevel = 100;
            for (int i = 0; i < this.mSaturationLevels.size(); i++) {
                int level = this.mSaturationLevels.valueAt(i).intValue();
                if (level < saturationLevel) {
                    saturationLevel = level;
                }
            }
            return saturationLevel;
        }

        private boolean updateState() {
            com.android.server.display.color.AppSaturationController.computeGrayscaleTransformMatrix(calculateSaturationLevel() / 100.0f, this.mTransformMatrix);
            boolean updated = false;
            java.util.Iterator<java.lang.ref.WeakReference<com.android.server.display.color.ColorDisplayService.ColorTransformController>> iterator = this.mControllerRefs.iterator();
            while (iterator.hasNext()) {
                java.lang.ref.WeakReference<com.android.server.display.color.ColorDisplayService.ColorTransformController> controllerRef = iterator.next();
                com.android.server.display.color.ColorDisplayService.ColorTransformController controller = controllerRef.get();
                if (controller != null) {
                    controller.applyAppSaturation(this.mTransformMatrix, com.android.server.display.color.AppSaturationController.TRANSLATION_VECTOR);
                    updated = true;
                } else {
                    iterator.remove();
                }
            }
            return updated;
        }

        private void clearExpiredReferences() {
            java.util.Iterator<java.lang.ref.WeakReference<com.android.server.display.color.ColorDisplayService.ColorTransformController>> iterator = this.mControllerRefs.iterator();
            while (iterator.hasNext()) {
                java.lang.ref.WeakReference<com.android.server.display.color.ColorDisplayService.ColorTransformController> controllerRef = iterator.next();
                com.android.server.display.color.ColorDisplayService.ColorTransformController controller = controllerRef.get();
                if (controller == null) {
                    iterator.remove();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dump(java.io.PrintWriter pw) {
            pw.println("            mSaturationLevels: " + this.mSaturationLevels);
            pw.println("            mControllerRefs count: " + this.mControllerRefs.size());
        }
    }
}
