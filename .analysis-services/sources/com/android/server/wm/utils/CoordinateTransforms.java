package com.android.server.wm.utils;

/* JADX INFO: loaded from: classes3.dex */
public class CoordinateTransforms {
    private CoordinateTransforms() {
    }

    public static void transformPhysicalToLogicalCoordinates(int rotation, int physicalWidth, int physicalHeight, android.graphics.Matrix out) {
        switch (rotation) {
            case 0:
                out.reset();
                return;
            case 1:
                out.setRotate(270.0f);
                out.postTranslate(0.0f, physicalWidth);
                return;
            case 2:
                out.setRotate(180.0f);
                out.postTranslate(physicalWidth, physicalHeight);
                return;
            case 3:
                out.setRotate(90.0f);
                out.postTranslate(physicalHeight, 0.0f);
                return;
            default:
                throw new java.lang.IllegalArgumentException("Unknown rotation: " + rotation);
        }
    }

    public static void transformLogicalToPhysicalCoordinates(int rotation, int physicalWidth, int physicalHeight, android.graphics.Matrix out) {
        switch (rotation) {
            case 0:
                out.reset();
                return;
            case 1:
                out.setRotate(90.0f);
                out.preTranslate(0.0f, -physicalWidth);
                return;
            case 2:
                out.setRotate(180.0f);
                out.preTranslate(-physicalWidth, -physicalHeight);
                return;
            case 3:
                out.setRotate(270.0f);
                out.preTranslate(-physicalHeight, 0.0f);
                return;
            default:
                throw new java.lang.IllegalArgumentException("Unknown rotation: " + rotation);
        }
    }

    public static void transformToRotation(int oldRotation, int newRotation, android.view.DisplayInfo info, android.graphics.Matrix out) {
        boolean z = true;
        if (info.rotation != 1 && info.rotation != 3) {
            z = false;
        }
        boolean flipped = z;
        int h = flipped ? info.logicalWidth : info.logicalHeight;
        int w = flipped ? info.logicalHeight : info.logicalWidth;
        android.graphics.Matrix tmp = new android.graphics.Matrix();
        transformLogicalToPhysicalCoordinates(oldRotation, w, h, out);
        transformPhysicalToLogicalCoordinates(newRotation, w, h, tmp);
        out.postConcat(tmp);
    }

    public static void transformToRotation(int oldRotation, int newRotation, int newWidth, int newHeight, android.graphics.Matrix out) {
        boolean flipped = true;
        if (newRotation != 1 && newRotation != 3) {
            flipped = false;
        }
        int h = flipped ? newWidth : newHeight;
        int w = flipped ? newHeight : newWidth;
        android.graphics.Matrix tmp = new android.graphics.Matrix();
        transformLogicalToPhysicalCoordinates(oldRotation, w, h, out);
        transformPhysicalToLogicalCoordinates(newRotation, w, h, tmp);
        out.postConcat(tmp);
    }

    public static void computeRotationMatrix(int rotationDelta, int w, int h, android.graphics.Matrix outMatrix) {
        switch (rotationDelta) {
            case 0:
                outMatrix.reset();
                break;
            case 1:
                outMatrix.setRotate(90.0f);
                outMatrix.postTranslate(h, 0.0f);
                break;
            case 2:
                outMatrix.setRotate(180.0f);
                outMatrix.postTranslate(w, h);
                break;
            case 3:
                outMatrix.setRotate(270.0f);
                outMatrix.postTranslate(0.0f, w);
                break;
        }
    }
}
