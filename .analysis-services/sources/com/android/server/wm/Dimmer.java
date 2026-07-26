package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public abstract class Dimmer {
    static final boolean DIMMER_REFACTOR = com.android.window.flags.Flags.introduceSmootherDimmer();
    protected final com.android.server.wm.WindowContainer mHost;

    protected abstract void adjustAppearance(com.android.server.wm.WindowContainer windowContainer, float f, int i);

    protected abstract void adjustRelativeLayer(com.android.server.wm.WindowContainer windowContainer, int i);

    abstract void dontAnimateExit();

    abstract android.graphics.Rect getDimBounds();

    abstract android.view.SurfaceControl getDimLayer();

    abstract void resetDimStates();

    abstract boolean updateDims(android.view.SurfaceControl.Transaction transaction);

    protected Dimmer(com.android.server.wm.WindowContainer host) {
        this.mHost = host;
    }

    static com.android.server.wm.Dimmer create(com.android.server.wm.WindowContainer host) {
        return DIMMER_REFACTOR ? new com.android.server.wm.SmoothDimmer(host) : new com.android.server.wm.LegacyDimmer(host);
    }

    com.android.server.wm.WindowContainer<?> getHost() {
        return this.mHost;
    }
}
