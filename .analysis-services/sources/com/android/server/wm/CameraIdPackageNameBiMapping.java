package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class CameraIdPackageNameBiMapping {
    private final java.util.Map<java.lang.String, java.lang.String> mPackageToCameraIdMap = new android.util.ArrayMap();
    private final java.util.Map<java.lang.String, java.lang.String> mCameraIdToPackageMap = new android.util.ArrayMap();

    CameraIdPackageNameBiMapping() {
    }

    boolean isEmpty() {
        return this.mCameraIdToPackageMap.isEmpty();
    }

    void put(java.lang.String packageName, java.lang.String cameraId) {
        removePackageName(packageName);
        removeCameraId(cameraId);
        this.mPackageToCameraIdMap.put(packageName, cameraId);
        this.mCameraIdToPackageMap.put(cameraId, packageName);
    }

    boolean containsPackageName(java.lang.String packageName) {
        return this.mPackageToCameraIdMap.containsKey(packageName);
    }

    java.lang.String getCameraId(java.lang.String packageName) {
        return this.mPackageToCameraIdMap.get(packageName);
    }

    void removeCameraId(java.lang.String cameraId) {
        java.lang.String packageName = this.mCameraIdToPackageMap.get(cameraId);
        if (packageName == null) {
            return;
        }
        this.mPackageToCameraIdMap.remove(packageName, cameraId);
        this.mCameraIdToPackageMap.remove(cameraId, packageName);
    }

    java.lang.String getSummaryForDisplayRotationHistoryRecord() {
        return "{ mPackageToCameraIdMap=" + this.mPackageToCameraIdMap + " }";
    }

    private void removePackageName(java.lang.String packageName) {
        java.lang.String cameraId = this.mPackageToCameraIdMap.get(packageName);
        if (cameraId == null) {
            return;
        }
        this.mPackageToCameraIdMap.remove(packageName, cameraId);
        this.mCameraIdToPackageMap.remove(cameraId, packageName);
    }
}
