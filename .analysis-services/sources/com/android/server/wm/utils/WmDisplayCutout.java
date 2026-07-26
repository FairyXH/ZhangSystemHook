package com.android.server.wm.utils;

/* JADX INFO: loaded from: classes3.dex */
public class WmDisplayCutout {
    public static final com.android.server.wm.utils.WmDisplayCutout NO_CUTOUT = new com.android.server.wm.utils.WmDisplayCutout(android.view.DisplayCutout.NO_CUTOUT, null);
    private final android.util.Size mFrameSize;
    private final android.view.DisplayCutout mInner;

    public WmDisplayCutout(android.view.DisplayCutout inner, android.util.Size frameSize) {
        this.mInner = inner;
        this.mFrameSize = frameSize;
    }

    public static com.android.server.wm.utils.WmDisplayCutout computeSafeInsets(android.view.DisplayCutout inner, int displayWidth, int displayHeight) {
        if (inner == android.view.DisplayCutout.NO_CUTOUT) {
            return NO_CUTOUT;
        }
        android.util.Size displaySize = new android.util.Size(displayWidth, displayHeight);
        android.graphics.Rect safeInsets = android.view.DisplayCutout.computeSafeInsets(displayWidth, displayHeight, inner);
        return new com.android.server.wm.utils.WmDisplayCutout(inner.replaceSafeInsets(safeInsets), displaySize);
    }

    public com.android.server.wm.utils.WmDisplayCutout computeSafeInsets(int width, int height) {
        return computeSafeInsets(this.mInner, width, height);
    }

    public android.view.DisplayCutout getDisplayCutout() {
        return this.mInner;
    }

    public boolean equals(java.lang.Object o) {
        if (!(o instanceof com.android.server.wm.utils.WmDisplayCutout)) {
            return false;
        }
        com.android.server.wm.utils.WmDisplayCutout that = (com.android.server.wm.utils.WmDisplayCutout) o;
        return java.util.Objects.equals(this.mInner, that.mInner) && java.util.Objects.equals(this.mFrameSize, that.mFrameSize);
    }

    public int hashCode() {
        return java.util.Objects.hash(this.mInner, this.mFrameSize);
    }

    public java.lang.String toString() {
        return "WmDisplayCutout{" + this.mInner + ", mFrameSize=" + this.mFrameSize + '}';
    }
}
