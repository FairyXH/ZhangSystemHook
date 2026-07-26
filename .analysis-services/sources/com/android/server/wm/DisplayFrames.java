package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class DisplayFrames {
    public final android.graphics.Rect mDisplayCutoutSafe;
    private final com.android.server.wm.IDisplayFramesExt mDisplayFramesExt;
    public int mHeight;
    public final android.view.InsetsState mInsetsState;
    public int mRotation;
    public final android.graphics.Rect mUnrestricted;
    public int mWidth;
    private final com.android.server.wm.IDisplayFramesWrapper mWrapper;
    private static final int ID_DISPLAY_CUTOUT_LEFT = android.view.InsetsSource.createId((java.lang.Object) null, 0, android.view.WindowInsets.Type.displayCutout());
    private static final int ID_DISPLAY_CUTOUT_TOP = android.view.InsetsSource.createId((java.lang.Object) null, 1, android.view.WindowInsets.Type.displayCutout());
    private static final int ID_DISPLAY_CUTOUT_RIGHT = android.view.InsetsSource.createId((java.lang.Object) null, 2, android.view.WindowInsets.Type.displayCutout());
    private static final int ID_DISPLAY_CUTOUT_BOTTOM = android.view.InsetsSource.createId((java.lang.Object) null, 3, android.view.WindowInsets.Type.displayCutout());
    private static final com.android.server.wm.IDisplayFramesStaticWrapper STATIC_WRAPPER = new com.android.server.wm.DisplayFrames.DisplayFramesStaticWrapper();

    public DisplayFrames(android.view.InsetsState insetsState, android.view.DisplayInfo info, android.view.DisplayCutout cutout, android.view.RoundedCorners roundedCorners, android.view.PrivacyIndicatorBounds indicatorBounds, android.view.DisplayShape displayShape) {
        this.mUnrestricted = new android.graphics.Rect();
        this.mDisplayCutoutSafe = new android.graphics.Rect();
        this.mWrapper = new com.android.server.wm.DisplayFrames.DisplayFramesWrapper();
        this.mDisplayFramesExt = (com.android.server.wm.IDisplayFramesExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IDisplayFramesExt.class).base(this).create();
        this.mInsetsState = insetsState;
        this.mDisplayFramesExt.setDisplayId(info.displayId);
        update(info.rotation, info.logicalWidth, info.logicalHeight, cutout, roundedCorners, indicatorBounds, displayShape);
    }

    DisplayFrames() {
        this.mUnrestricted = new android.graphics.Rect();
        this.mDisplayCutoutSafe = new android.graphics.Rect();
        this.mWrapper = new com.android.server.wm.DisplayFrames.DisplayFramesWrapper();
        this.mDisplayFramesExt = (com.android.server.wm.IDisplayFramesExt) system.ext.loader.core.ExtLoader.type(com.android.server.wm.IDisplayFramesExt.class).base(this).create();
        this.mInsetsState = new android.view.InsetsState();
    }

    public boolean update(int rotation, int w, int h, android.view.DisplayCutout displayCutout, android.view.RoundedCorners roundedCorners, android.view.PrivacyIndicatorBounds indicatorBounds, android.view.DisplayShape displayShape) {
        android.view.InsetsState state = this.mInsetsState;
        android.graphics.Rect safe = this.mDisplayCutoutSafe;
        if (this.mRotation == rotation && this.mWidth == w && this.mHeight == h && this.mInsetsState.getDisplayCutout().equals(displayCutout) && state.getRoundedCorners().equals(roundedCorners) && state.getPrivacyIndicatorBounds().equals(indicatorBounds)) {
            return false;
        }
        this.mRotation = rotation;
        this.mWidth = w;
        this.mHeight = h;
        android.graphics.Rect u = this.mUnrestricted;
        u.set(0, 0, w, h);
        state.setDisplayFrame(u);
        state.setDisplayCutout(displayCutout);
        state.setRoundedCorners(roundedCorners);
        state.setPrivacyIndicatorBounds(indicatorBounds);
        state.setDisplayShape(displayShape);
        state.getDisplayCutoutSafe(safe);
        if (safe.left > u.left) {
            state.getOrCreateSource(ID_DISPLAY_CUTOUT_LEFT, android.view.WindowInsets.Type.displayCutout()).setFrame(u.left, u.top, safe.left, u.bottom).updateSideHint(u);
        } else {
            state.removeSource(ID_DISPLAY_CUTOUT_LEFT);
        }
        if (safe.top > u.top) {
            state.getOrCreateSource(ID_DISPLAY_CUTOUT_TOP, android.view.WindowInsets.Type.displayCutout()).setFrame(u.left, u.top, u.right, safe.top).updateSideHint(u);
        } else {
            state.removeSource(ID_DISPLAY_CUTOUT_TOP);
        }
        if (safe.right < u.right) {
            state.getOrCreateSource(ID_DISPLAY_CUTOUT_RIGHT, android.view.WindowInsets.Type.displayCutout()).setFrame(safe.right, u.top, u.right, u.bottom).updateSideHint(u);
        } else {
            state.removeSource(ID_DISPLAY_CUTOUT_RIGHT);
        }
        if (safe.bottom < u.bottom) {
            state.getOrCreateSource(ID_DISPLAY_CUTOUT_BOTTOM, android.view.WindowInsets.Type.displayCutout()).setFrame(u.left, safe.bottom, u.right, u.bottom).updateSideHint(u);
            this.mDisplayFramesExt.removeSecondaryDisplaySource(state, w, h);
            return true;
        }
        state.removeSource(ID_DISPLAY_CUTOUT_BOTTOM);
        return true;
    }

    public void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.end(token);
    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.println(prefix + "DisplayFrames w=" + this.mWidth + " h=" + this.mHeight + " r=" + this.mRotation);
    }

    public static com.android.server.wm.IDisplayFramesStaticWrapper getStaticWrapper() {
        return STATIC_WRAPPER;
    }

    public com.android.server.wm.IDisplayFramesWrapper getWrapper() {
        return this.mWrapper;
    }

    private class DisplayFramesWrapper extends com.android.server.wm.DisplayFrames.DisplayFramesStaticWrapper implements com.android.server.wm.IDisplayFramesWrapper {
        private DisplayFramesWrapper() {
            super();
        }

        @Override // com.android.server.wm.IDisplayFramesWrapper
        public com.android.server.wm.IDisplayFramesExt getExtImpl() {
            return com.android.server.wm.DisplayFrames.this.mDisplayFramesExt;
        }
    }

    private static class DisplayFramesStaticWrapper implements com.android.server.wm.IDisplayFramesStaticWrapper {
        private DisplayFramesStaticWrapper() {
        }

        @Override // com.android.server.wm.IDisplayFramesStaticWrapper
        public final int getTopDisplayCutoutId() {
            return com.android.server.wm.DisplayFrames.ID_DISPLAY_CUTOUT_TOP;
        }

        @Override // com.android.server.wm.IDisplayFramesStaticWrapper
        public final int getBottomDisplayCutoutId() {
            return com.android.server.wm.DisplayFrames.ID_DISPLAY_CUTOUT_BOTTOM;
        }

        @Override // com.android.server.wm.IDisplayFramesStaticWrapper
        public final int getLeftDisplayCutoutId() {
            return com.android.server.wm.DisplayFrames.ID_DISPLAY_CUTOUT_LEFT;
        }

        @Override // com.android.server.wm.IDisplayFramesStaticWrapper
        public final int getRightDisplayCutoutId() {
            return com.android.server.wm.DisplayFrames.ID_DISPLAY_CUTOUT_RIGHT;
        }
    }
}
