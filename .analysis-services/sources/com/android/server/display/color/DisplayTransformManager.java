package com.android.server.display.color;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayTransformManager {
    private static final float COLOR_SATURATION_BOOSTED = 1.1f;
    private static final float COLOR_SATURATION_NATURAL = 1.0f;
    private static final int DISPLAY_COLOR_ENHANCED = 2;
    private static final int DISPLAY_COLOR_MANAGED = 0;
    private static final int DISPLAY_COLOR_UNMANAGED = 1;
    public static final int LEVEL_COLOR_MATRIX_DISPLAY_WHITE_BALANCE = 125;
    public static final int LEVEL_COLOR_MATRIX_GRAYSCALE = 200;
    public static final int LEVEL_COLOR_MATRIX_INVERT_COLOR = 300;
    public static final int LEVEL_COLOR_MATRIX_NIGHT_DISPLAY = 100;
    public static final int LEVEL_COLOR_MATRIX_REDUCE_BRIGHT_COLORS = 250;
    public static final int LEVEL_COLOR_MATRIX_SATURATION = 150;
    static final java.lang.String PERSISTENT_PROPERTY_COMPOSITION_COLOR_MODE = "persist.sys.sf.color_mode";
    static final java.lang.String PERSISTENT_PROPERTY_DISPLAY_COLOR = "persist.sys.sf.native_mode";
    static final java.lang.String PERSISTENT_PROPERTY_SATURATION = "persist.sys.sf.color_saturation";
    private static final int SURFACE_FLINGER_TRANSACTION_COLOR_MATRIX = 1015;
    private static final int SURFACE_FLINGER_TRANSACTION_DALTONIZER = 1014;
    private static final int SURFACE_FLINGER_TRANSACTION_DISPLAY_COLOR = 1023;
    private static final int SURFACE_FLINGER_TRANSACTION_ORIRGB = 21999;
    private static final int SURFACE_FLINGER_TRANSACTION_QUERY_COLOR_MANAGED = 1030;
    private static final int SURFACE_FLINGER_TRANSACTION_SATURATION = 1022;
    private static final java.lang.String TAG = "DisplayTransformManager";
    private static final java.lang.String SURFACE_FLINGER = "SurfaceFlinger";
    private static final android.os.IBinder sFlinger = android.os.ServiceManager.getService(SURFACE_FLINGER);
    private final android.util.SparseArray<float[]> mColorMatrix = new android.util.SparseArray<>(6);
    private final float[][] mTempColorMatrix = (float[][]) java.lang.reflect.Array.newInstance((java.lang.Class<?>) java.lang.Float.TYPE, 2, 16);
    final java.lang.Object mDaltonizerModeLock = new java.lang.Object();
    int mDaltonizerMode = -1;
    int mDaltonizerLevel = -1;
    public com.android.server.display.color.IDisplayTransformManagerWrapper Wrapper = new com.android.server.display.color.DisplayTransformManager.DisplayTransformManagerWrapper();

    DisplayTransformManager() {
    }

    public float[] getColorMatrix(int key) {
        float[] fArrCopyOf;
        synchronized (this.mColorMatrix) {
            float[] value = this.mColorMatrix.get(key);
            fArrCopyOf = value == null ? null : java.util.Arrays.copyOf(value, value.length);
        }
        return fArrCopyOf;
    }

    public void setColorMatrix(int level, float[] value) {
        if (value != null && value.length != 16) {
            throw new java.lang.IllegalArgumentException("Expected length: 16 (4x4 matrix), actual length: " + value.length);
        }
        synchronized (this.mColorMatrix) {
            float[] oldValue = this.mColorMatrix.get(level);
            if (!java.util.Arrays.equals(oldValue, value)) {
                if (value == null) {
                    this.mColorMatrix.remove(level);
                } else if (oldValue == null) {
                    this.mColorMatrix.put(level, java.util.Arrays.copyOf(value, value.length));
                } else {
                    java.lang.System.arraycopy(value, 0, oldValue, 0, value.length);
                }
                applyColorMatrix(computeColorMatrixLocked());
            }
        }
    }

    public void setDaltonizerMode(int mode, int level) {
        synchronized (this.mDaltonizerModeLock) {
            if (this.mDaltonizerMode != mode || this.mDaltonizerLevel != level) {
                this.mDaltonizerMode = mode;
                this.mDaltonizerLevel = level;
                applyDaltonizerMode(mode, level);
            }
        }
    }

    private float[] computeColorMatrixLocked() {
        int count = this.mColorMatrix.size();
        if (count == 0) {
            return null;
        }
        float[][] result = this.mTempColorMatrix;
        android.opengl.Matrix.setIdentityM(result[0], 0);
        for (int i = 0; i < count; i++) {
            float[] rhs = this.mColorMatrix.valueAt(i);
            if (i == count - 1 && this.mColorMatrix.keyAt(i) > 300) {
                android.opengl.Matrix.multiplyMM(result[(i + 1) % 2], 0, rhs, 0, result[i % 2], 0);
            } else {
                android.opengl.Matrix.multiplyMM(result[(i + 1) % 2], 0, result[i % 2], 0, rhs, 0);
            }
        }
        int i2 = count % 2;
        return result[i2];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void applyColorMatrix(float[] m) {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken("android.ui.ISurfaceComposer");
        if (m != null) {
            data.writeInt(1);
            for (int i = 0; i < 16; i++) {
                data.writeFloat(m[i]);
            }
        } else {
            data.writeInt(0);
        }
        try {
            try {
                sFlinger.transact(1015, data, null, 0);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(TAG, "Failed to set color transform", ex);
            }
        } finally {
            data.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void applyColorMatrix32(float[] matrix) {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken("android.ui.ISurfaceComposer");
        if (matrix != null) {
            data.writeInt(1);
            for (int i = 0; i < 16; i++) {
                data.writeFloat(matrix[i]);
            }
            data.writeInt(1);
            for (int i2 = 16; i2 < 32; i2++) {
                data.writeFloat(matrix[i2]);
            }
        } else {
            data.writeInt(0);
        }
        try {
            try {
                android.util.Slog.d(TAG, "Success to set color transform ");
                sFlinger.transact(1015, data, null, 0);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(TAG, "Failed to set color transform", ex);
            }
        } finally {
            data.recycle();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void applyOriRGB(float[] oriRGB) {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken("android.ui.ISurfaceComposer");
        if (oriRGB != null) {
            data.writeInt(1);
            for (int i = 0; i < 3; i++) {
                data.writeFloat(oriRGB[i]);
            }
        } else {
            data.writeInt(0);
        }
        try {
            try {
                sFlinger.transact(SURFACE_FLINGER_TRANSACTION_ORIRGB, data, null, 0);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(TAG, "Failed to set oriRGB", ex);
            }
        } finally {
            data.recycle();
        }
    }

    private static void applyDaltonizerMode(int mode, int level) {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken("android.ui.ISurfaceComposer");
        data.writeInt(mode);
        data.writeInt(level);
        try {
            try {
                sFlinger.transact(1014, data, null, 0);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(TAG, "Failed to set Daltonizer mode", ex);
            }
        } finally {
            data.recycle();
        }
    }

    public boolean needsLinearColorMatrix() {
        return android.os.SystemProperties.getInt(PERSISTENT_PROPERTY_DISPLAY_COLOR, 1) != 1;
    }

    public boolean needsLinearColorMatrix(int colorMode) {
        return colorMode != 2;
    }

    public boolean setColorMode(int colorMode, float[] nightDisplayMatrix, int compositionColorMode) {
        if (colorMode == 0) {
            applySaturation(1.0f);
            setDisplayColor(0, compositionColorMode);
        } else if (colorMode == 1) {
            applySaturation(COLOR_SATURATION_BOOSTED);
            setDisplayColor(0, compositionColorMode);
        } else if (colorMode == 2) {
            applySaturation(1.0f);
            setDisplayColor(1, compositionColorMode);
        } else if (colorMode == 3) {
            applySaturation(1.0f);
            setDisplayColor(2, compositionColorMode);
        } else if (colorMode >= 256 && colorMode <= 511) {
            applySaturation(1.0f);
            setDisplayColor(colorMode, compositionColorMode);
        }
        setColorMatrix(100, nightDisplayMatrix);
        updateConfiguration();
        return true;
    }

    public boolean isDeviceColorManaged() {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken("android.ui.ISurfaceComposer");
        try {
            sFlinger.transact(1030, data, reply, 0);
            return reply.readBoolean();
        } catch (android.os.RemoteException ex) {
            android.util.Slog.e(TAG, "Failed to query wide color support", ex);
            return false;
        } finally {
            data.recycle();
            reply.recycle();
        }
    }

    private void applySaturation(float saturation) {
        android.os.SystemProperties.set(PERSISTENT_PROPERTY_SATURATION, java.lang.Float.toString(saturation));
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken("android.ui.ISurfaceComposer");
        data.writeFloat(saturation);
        try {
            try {
                sFlinger.transact(SURFACE_FLINGER_TRANSACTION_SATURATION, data, null, 0);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(TAG, "Failed to set saturation", ex);
            }
        } finally {
            data.recycle();
        }
    }

    private void setDisplayColor(int color, int compositionColorMode) {
        android.os.SystemProperties.set(PERSISTENT_PROPERTY_DISPLAY_COLOR, java.lang.Integer.toString(color));
        if (compositionColorMode != -1) {
            android.os.SystemProperties.set(PERSISTENT_PROPERTY_COMPOSITION_COLOR_MODE, java.lang.Integer.toString(compositionColorMode));
        }
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken("android.ui.ISurfaceComposer");
        data.writeInt(color);
        if (compositionColorMode != -1) {
            data.writeInt(compositionColorMode);
        }
        try {
            try {
                sFlinger.transact(1023, data, null, 0);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.e(TAG, "Failed to set display color", ex);
            }
        } finally {
            data.recycle();
        }
    }

    private void updateConfiguration() {
        try {
            android.app.ActivityTaskManager.getService().updateConfiguration((android.content.res.Configuration) null);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Could not update configuration", e);
        }
    }

    public com.android.server.display.color.IDisplayTransformManagerWrapper getWrapper() {
        return this.Wrapper;
    }

    private class DisplayTransformManagerWrapper implements com.android.server.display.color.IDisplayTransformManagerWrapper {
        private DisplayTransformManagerWrapper() {
        }

        @Override // com.android.server.display.color.IDisplayTransformManagerWrapper
        public android.util.SparseArray<float[]> getColorMatrixs() {
            return com.android.server.display.color.DisplayTransformManager.this.mColorMatrix;
        }

        @Override // com.android.server.display.color.IDisplayTransformManagerWrapper
        public void applyColorMatrix(float[] matrix) {
            com.android.server.display.color.DisplayTransformManager.applyColorMatrix(matrix);
        }

        @Override // com.android.server.display.color.IDisplayTransformManagerWrapper
        public void applyColorMatrix32(float[] matrix) {
            com.android.server.display.color.DisplayTransformManager.applyColorMatrix32(matrix);
        }

        @Override // com.android.server.display.color.IDisplayTransformManagerWrapper
        public void applyOriRGB(float[] oriRGB) {
            com.android.server.display.color.DisplayTransformManager.applyOriRGB(oriRGB);
        }
    }
}
