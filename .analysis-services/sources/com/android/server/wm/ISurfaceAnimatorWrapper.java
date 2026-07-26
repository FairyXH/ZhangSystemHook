package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ISurfaceAnimatorWrapper {
    public static final int ANIMATION_TYPE_TRANSWINDOW = 128;

    default com.android.server.wm.ISurfaceAnimatorExt getExtImpl() {
        return new com.android.server.wm.ISurfaceAnimatorExt() { // from class: com.android.server.wm.ISurfaceAnimatorWrapper.1
        };
    }

    default void reset(android.view.SurfaceControl.Transaction t, boolean destroyLeash) {
    }
}
