package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class AppSnapshotLoader {
    private static final java.lang.String TAG = "WindowManager";
    private final com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider mPersistInfoProvider;

    AppSnapshotLoader(com.android.server.wm.BaseAppSnapshotPersister.PersistInfoProvider persistInfoProvider) {
        this.mPersistInfoProvider = persistInfoProvider;
    }

    static class PreRLegacySnapshotConfig {
        final boolean mForceLoadReducedJpeg;
        final float mScale;

        PreRLegacySnapshotConfig(float scale, boolean forceLoadReducedJpeg) {
            this.mScale = scale;
            this.mForceLoadReducedJpeg = forceLoadReducedJpeg;
        }
    }

    com.android.server.wm.AppSnapshotLoader.PreRLegacySnapshotConfig getLegacySnapshotConfig(int taskWidth, float legacyScale, boolean highResFileExists, boolean loadLowResolutionBitmap) {
        float preRLegacyScale = 0.0f;
        boolean forceLoadReducedJpeg = false;
        boolean isPreRLegacySnapshot = taskWidth == 0;
        if (!isPreRLegacySnapshot) {
            return null;
        }
        boolean isPreQLegacyProto = isPreRLegacySnapshot && java.lang.Float.compare(legacyScale, 0.0f) == 0;
        if (isPreQLegacyProto) {
            if (android.app.ActivityManager.isLowRamDeviceStatic() && !highResFileExists) {
                preRLegacyScale = 0.6f;
                forceLoadReducedJpeg = true;
            } else {
                preRLegacyScale = loadLowResolutionBitmap ? 0.5f : 1.0f;
            }
        } else if (isPreRLegacySnapshot) {
            if (android.app.ActivityManager.isLowRamDeviceStatic()) {
                preRLegacyScale = legacyScale;
                forceLoadReducedJpeg = true;
            } else {
                preRLegacyScale = loadLowResolutionBitmap ? 0.5f * legacyScale : legacyScale;
            }
        }
        return new com.android.server.wm.AppSnapshotLoader.PreRLegacySnapshotConfig(preRLegacyScale, forceLoadReducedJpeg);
    }

    android.window.TaskSnapshot loadTask(int id, int userId, boolean loadLowResolutionBitmap) {
        java.lang.String str;
        java.io.File lowResolutionBitmapFile;
        android.graphics.Point taskSize;
        java.io.File protoFile = this.mPersistInfoProvider.getProtoFile(id, userId);
        if (!protoFile.exists()) {
            return null;
        }
        try {
            byte[] bytes = java.nio.file.Files.readAllBytes(protoFile.toPath());
            com.android.server.wm.nano.WindowManagerProtos.TaskSnapshotProto proto = com.android.server.wm.nano.WindowManagerProtos.TaskSnapshotProto.parseFrom(bytes);
            java.io.File highResBitmap = this.mPersistInfoProvider.getHighResolutionBitmapFile(id, userId);
            com.android.server.wm.AppSnapshotLoader.PreRLegacySnapshotConfig legacyConfig = getLegacySnapshotConfig(proto.taskWidth, proto.legacyScale, highResBitmap.exists(), loadLowResolutionBitmap);
            boolean forceLoadReducedJpeg = (legacyConfig != null && legacyConfig.mForceLoadReducedJpeg) || this.mPersistInfoProvider.getForceReduceSnapshot();
            if (loadLowResolutionBitmap || forceLoadReducedJpeg) {
                lowResolutionBitmapFile = this.mPersistInfoProvider.getLowResolutionBitmapFile(id, userId);
            } else {
                lowResolutionBitmapFile = highResBitmap;
            }
            java.io.File bitmapFile = lowResolutionBitmapFile;
            if (!bitmapFile.exists()) {
                return null;
            }
            android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
            options.inPreferredConfig = (!this.mPersistInfoProvider.use16BitFormat() || proto.isTranslucent) ? android.graphics.Bitmap.Config.ARGB_8888 : android.graphics.Bitmap.Config.RGB_565;
            android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(bitmapFile.getPath(), options);
            if (bitmap != null) {
                android.graphics.Bitmap hwBitmap = bitmap.copy(android.graphics.Bitmap.Config.HARDWARE, false);
                bitmap.recycle();
                if (hwBitmap == null) {
                    android.util.Slog.w(TAG, "Failed to create hardware bitmap: " + bitmapFile.getPath());
                    return null;
                }
                android.hardware.HardwareBuffer buffer = hwBitmap.getHardwareBuffer();
                if (buffer == null) {
                    android.util.Slog.w(TAG, "Failed to retrieve gralloc buffer for bitmap: " + bitmapFile.getPath());
                    return null;
                }
                android.content.ComponentName topActivityComponent = android.content.ComponentName.unflattenFromString(proto.topActivityComponent);
                if (legacyConfig != null) {
                    int taskWidth = (int) (hwBitmap.getWidth() / legacyConfig.mScale);
                    int taskHeight = (int) (hwBitmap.getHeight() / legacyConfig.mScale);
                    android.graphics.Point taskSize2 = new android.graphics.Point(taskWidth, taskHeight);
                    taskSize = taskSize2;
                } else {
                    taskSize = new android.graphics.Point(proto.taskWidth, proto.taskHeight);
                }
                long j = proto.id;
                long jElapsedRealtimeNanos = android.os.SystemClock.elapsedRealtimeNanos();
                android.graphics.ColorSpace colorSpace = hwBitmap.getColorSpace();
                int i = proto.orientation;
                int i2 = proto.rotation;
                android.graphics.Rect rect = new android.graphics.Rect(proto.insetLeft, proto.insetTop, proto.insetRight, proto.insetBottom);
                android.graphics.Rect rect2 = new android.graphics.Rect(proto.letterboxInsetLeft, proto.letterboxInsetTop, proto.letterboxInsetRight, proto.letterboxInsetBottom);
                boolean z = proto.isRealSnapshot;
                int i3 = proto.windowingMode;
                int i4 = proto.appearance;
                boolean z2 = proto.isTranslucent;
                int i5 = proto.uiMode;
                str = TAG;
                try {
                    return new android.window.TaskSnapshot(j, jElapsedRealtimeNanos, topActivityComponent, buffer, colorSpace, i, i2, taskSize, rect, rect2, loadLowResolutionBitmap, z, i3, i4, z2, false, i5);
                } catch (java.io.IOException e) {
                    android.util.Slog.w(str, "Unable to load task snapshot data for Id=" + id);
                    return null;
                }
            }
            android.util.Slog.w(TAG, "Failed to load bitmap: " + bitmapFile.getPath());
            return null;
        } catch (java.io.IOException e2) {
            str = TAG;
        }
    }
}
