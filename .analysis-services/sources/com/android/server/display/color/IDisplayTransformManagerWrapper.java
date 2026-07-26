package com.android.server.display.color;

/* JADX INFO: loaded from: classes2.dex */
public interface IDisplayTransformManagerWrapper {
    default android.util.SparseArray<float[]> getColorMatrixs() {
        return new android.util.SparseArray<>();
    }

    default void applyColorMatrix(float[] matrix) {
    }

    default void applyColorMatrix32(float[] matrix) {
    }

    default void applyOriRGB(float[] oriRGB) {
    }
}
