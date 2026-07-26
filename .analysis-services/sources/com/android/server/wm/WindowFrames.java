package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class WindowFrames {
    private static final java.lang.StringBuilder sTmpSB = new java.lang.StringBuilder();
    private boolean mContentChanged;
    private boolean mInsetsChanged;
    private boolean mParentFrameWasClippedByDisplayCutout;
    public final android.graphics.Rect mParentFrame = new android.graphics.Rect();
    public final android.graphics.Rect mDisplayFrame = new android.graphics.Rect();
    final android.graphics.Rect mFrame = new android.graphics.Rect();
    final android.graphics.Rect mLastFrame = new android.graphics.Rect();
    final android.graphics.Rect mRelFrame = new android.graphics.Rect();
    final android.graphics.Rect mLastRelFrame = new android.graphics.Rect();
    private boolean mFrameSizeChanged = false;
    final android.graphics.Rect mCompatFrame = new android.graphics.Rect();
    boolean mLastForceReportingResized = false;
    boolean mForceReportingResized = false;

    public void setFrames(android.graphics.Rect parentFrame, android.graphics.Rect displayFrame) {
        this.mParentFrame.set(parentFrame);
        this.mDisplayFrame.set(displayFrame);
    }

    public void setParentFrameWasClippedByDisplayCutout(boolean parentFrameWasClippedByDisplayCutout) {
        this.mParentFrameWasClippedByDisplayCutout = parentFrameWasClippedByDisplayCutout;
    }

    boolean parentFrameWasClippedByDisplayCutout() {
        return this.mParentFrameWasClippedByDisplayCutout;
    }

    boolean didFrameSizeChange() {
        return (this.mLastFrame.width() == this.mFrame.width() && this.mLastFrame.height() == this.mFrame.height()) ? false : true;
    }

    boolean setReportResizeHints() {
        this.mLastForceReportingResized |= this.mForceReportingResized;
        this.mFrameSizeChanged |= didFrameSizeChange();
        return this.mLastForceReportingResized || this.mFrameSizeChanged;
    }

    boolean isFrameSizeChangeReported() {
        return this.mFrameSizeChanged || didFrameSizeChange();
    }

    void clearReportResizeHints() {
        this.mLastForceReportingResized = false;
        this.mFrameSizeChanged = false;
    }

    void onResizeHandled() {
        this.mForceReportingResized = false;
    }

    void forceReportingResized() {
        this.mForceReportingResized = true;
    }

    public void setContentChanged(boolean contentChanged) {
        this.mContentChanged = contentChanged;
    }

    boolean hasContentChanged() {
        return this.mContentChanged;
    }

    void setInsetsChanged(boolean insetsChanged) {
        this.mInsetsChanged = insetsChanged;
    }

    boolean hasInsetsChanged() {
        return this.mInsetsChanged;
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        this.mParentFrame.dumpDebug(proto, 1146756268040L);
        this.mDisplayFrame.dumpDebug(proto, 1146756268036L);
        this.mFrame.dumpDebug(proto, 1146756268037L);
        this.mCompatFrame.dumpDebug(proto, 1146756268048L);
        proto.end(token);
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + "Frames: parent=" + this.mParentFrame.toShortString(sTmpSB) + " display=" + this.mDisplayFrame.toShortString(sTmpSB) + " frame=" + this.mFrame.toShortString(sTmpSB) + " last=" + this.mLastFrame.toShortString(sTmpSB) + " insetsChanged=" + this.mInsetsChanged);
    }

    java.lang.String getInsetsChangedInfo() {
        return "forceReportingResized=" + this.mLastForceReportingResized + " insetsChanged=" + this.mInsetsChanged;
    }
}
