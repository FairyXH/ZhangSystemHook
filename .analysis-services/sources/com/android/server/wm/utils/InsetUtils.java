package com.android.server.wm.utils;

/* JADX INFO: loaded from: classes3.dex */
public class InsetUtils {
    private InsetUtils() {
    }

    public static void rotateInsets(android.graphics.Rect inOutInsets, int rotationDelta) {
        switch (rotationDelta) {
            case 0:
                return;
            case 1:
                inOutInsets.set(inOutInsets.top, inOutInsets.right, inOutInsets.bottom, inOutInsets.left);
                return;
            case 2:
                inOutInsets.set(inOutInsets.right, inOutInsets.bottom, inOutInsets.left, inOutInsets.top);
                return;
            case 3:
                inOutInsets.set(inOutInsets.bottom, inOutInsets.left, inOutInsets.top, inOutInsets.right);
                return;
            default:
                throw new java.lang.IllegalArgumentException("Unknown rotation: " + rotationDelta);
        }
    }

    public static void addInsets(android.graphics.Rect inOutInsets, android.graphics.Rect insetsToAdd) {
        inOutInsets.left += insetsToAdd.left;
        inOutInsets.top += insetsToAdd.top;
        inOutInsets.right += insetsToAdd.right;
        inOutInsets.bottom += insetsToAdd.bottom;
    }

    public static void insetsBetweenFrames(android.graphics.Rect outerFrame, android.graphics.Rect innerFrame, android.graphics.Rect outInsets) {
        if (innerFrame == null) {
            outInsets.setEmpty();
            return;
        }
        int w = outerFrame.width();
        int h = outerFrame.height();
        outInsets.set(java.lang.Math.min(w, java.lang.Math.max(0, innerFrame.left - outerFrame.left)), java.lang.Math.min(h, java.lang.Math.max(0, innerFrame.top - outerFrame.top)), java.lang.Math.min(w, java.lang.Math.max(0, outerFrame.right - innerFrame.right)), java.lang.Math.min(h, java.lang.Math.max(0, outerFrame.bottom - innerFrame.bottom)));
    }
}
