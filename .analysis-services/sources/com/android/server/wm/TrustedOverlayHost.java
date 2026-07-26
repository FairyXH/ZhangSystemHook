package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class TrustedOverlayHost {
    final java.util.ArrayList<android.view.SurfaceControlViewHost.SurfacePackage> mOverlays = new java.util.ArrayList<>();
    android.view.SurfaceControl mSurfaceControl;
    final com.android.server.wm.WindowManagerService mWmService;

    TrustedOverlayHost(com.android.server.wm.WindowManagerService wms) {
        this.mWmService = wms;
    }

    void requireOverlaySurfaceControl() {
        if (this.mSurfaceControl == null) {
            android.view.SurfaceControl.Builder b = this.mWmService.makeSurfaceBuilder(null).setContainerLayer().setHidden(true).setCallsite("TrustedOverlayHost.requireOverlaySurfaceControl").setName("Overlay Host Leash");
            this.mSurfaceControl = b.build();
            android.view.SurfaceControl.Transaction t = this.mWmService.mTransactionFactory.get();
            t.setTrustedOverlay(this.mSurfaceControl, true).apply();
        }
    }

    void setParent(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl newParent) {
        if (this.mSurfaceControl == null) {
            return;
        }
        t.reparent(this.mSurfaceControl, newParent);
        if (newParent != null) {
            t.show(this.mSurfaceControl);
        } else {
            t.hide(this.mSurfaceControl);
        }
    }

    void setLayer(android.view.SurfaceControl.Transaction t, int layer) {
        if (this.mSurfaceControl != null) {
            t.setLayer(this.mSurfaceControl, layer);
        }
    }

    void setVisibility(android.view.SurfaceControl.Transaction t, boolean visible) {
        if (this.mSurfaceControl != null) {
            t.setVisibility(this.mSurfaceControl, visible);
        }
    }

    void addOverlay(android.view.SurfaceControlViewHost.SurfacePackage p, android.view.SurfaceControl currentParent) {
        requireOverlaySurfaceControl();
        boolean hasExistingOverlay = false;
        for (int i = this.mOverlays.size() - 1; i >= 0; i--) {
            android.view.SurfaceControlViewHost.SurfacePackage l = this.mOverlays.get(i);
            if (l.getSurfaceControl().isSameSurface(p.getSurfaceControl())) {
                hasExistingOverlay = true;
            }
        }
        if (!hasExistingOverlay) {
            this.mOverlays.add(p);
        }
        android.view.SurfaceControl.Transaction t = this.mWmService.mTransactionFactory.get();
        t.reparent(p.getSurfaceControl(), this.mSurfaceControl).show(p.getSurfaceControl());
        setParent(t, currentParent);
        t.apply();
    }

    boolean removeOverlay(android.view.SurfaceControlViewHost.SurfacePackage p) {
        android.view.SurfaceControl.Transaction t = this.mWmService.mTransactionFactory.get();
        for (int i = this.mOverlays.size() - 1; i >= 0; i--) {
            android.view.SurfaceControlViewHost.SurfacePackage l = this.mOverlays.get(i);
            if (l.getSurfaceControl().isSameSurface(p.getSurfaceControl())) {
                this.mOverlays.remove(i);
                t.reparent(l.getSurfaceControl(), null);
                l.release();
            }
        }
        t.apply();
        return this.mOverlays.size() > 0;
    }

    void dispatchConfigurationChanged(android.content.res.Configuration c) {
        for (int i = this.mOverlays.size() - 1; i >= 0; i--) {
            android.view.SurfaceControlViewHost.SurfacePackage l = this.mOverlays.get(i);
            try {
                l.getRemoteInterface().onConfigurationChanged(c);
            } catch (java.lang.Exception e) {
                removeOverlay(l);
            }
        }
    }

    private void dispatchDetachedFromWindow() {
        for (int i = this.mOverlays.size() - 1; i >= 0; i--) {
            android.view.SurfaceControlViewHost.SurfacePackage l = this.mOverlays.get(i);
            try {
                l.getRemoteInterface().onDispatchDetachedFromWindow();
            } catch (java.lang.Exception e) {
            }
            l.release();
        }
    }

    void dispatchInsetsChanged(android.view.InsetsState s, android.graphics.Rect insetFrame) {
        for (int i = this.mOverlays.size() - 1; i >= 0; i--) {
            android.view.SurfaceControlViewHost.SurfacePackage l = this.mOverlays.get(i);
            try {
                l.getRemoteInterface().onInsetsChanged(s, insetFrame);
            } catch (java.lang.Exception e) {
            }
        }
    }

    void release() {
        dispatchDetachedFromWindow();
        this.mOverlays.clear();
        android.view.SurfaceControl.Transaction t = this.mWmService.mTransactionFactory.get();
        t.remove(this.mSurfaceControl).apply();
        this.mSurfaceControl = null;
    }
}
