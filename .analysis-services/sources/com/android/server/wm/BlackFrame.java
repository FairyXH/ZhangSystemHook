package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class BlackFrame {
    private final com.android.server.wm.BlackFrame.BlackSurface[] mBlackSurfaces = new com.android.server.wm.BlackFrame.BlackSurface[4];
    private final android.graphics.Rect mInnerRect;
    private final android.graphics.Rect mOuterRect;
    private final java.util.function.Supplier<android.view.SurfaceControl.Transaction> mTransactionFactory;

    static class BlackSurface {
        final int layer;
        final int left;
        final android.view.SurfaceControl surface;
        final int top;

        BlackSurface(android.view.SurfaceControl.Transaction transaction, int layer, int l, int t, int r, int b, com.android.server.wm.DisplayContent dc, android.view.SurfaceControl surfaceControl) throws android.view.Surface.OutOfResourcesException {
            this.left = l;
            this.top = t;
            this.layer = layer;
            int w = r - l;
            int h = b - t;
            this.surface = dc.makeOverlay().setName("BlackSurface").setColorLayer().setParent(surfaceControl).setCallsite("BlackSurface").build();
            transaction.setWindowCrop(this.surface, w, h);
            transaction.setAlpha(this.surface, 1.0f);
            transaction.setLayer(this.surface, layer);
            transaction.setPosition(this.surface, this.left, this.top);
            transaction.show(this.surface);
            if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_SURFACE_ALLOC_enabled[2]) {
                java.lang.String protoLogParam0 = java.lang.String.valueOf(this.surface);
                long protoLogParam1 = layer;
                com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, -2963535976860666511L, 4, null, protoLogParam0, java.lang.Long.valueOf(protoLogParam1));
            }
        }
    }

    public void printTo(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.print("Outer: ");
        this.mOuterRect.printShortString(pw);
        pw.print(" / Inner: ");
        this.mInnerRect.printShortString(pw);
        pw.println();
        for (int i = 0; i < this.mBlackSurfaces.length; i++) {
            com.android.server.wm.BlackFrame.BlackSurface bs = this.mBlackSurfaces[i];
            pw.print(prefix);
            pw.print("#");
            pw.print(i);
            pw.print(": ");
            pw.print(bs.surface);
            pw.print(" left=");
            pw.print(bs.left);
            pw.print(" top=");
            pw.println(bs.top);
        }
    }

    public BlackFrame(java.util.function.Supplier<android.view.SurfaceControl.Transaction> factory, android.view.SurfaceControl.Transaction t, android.graphics.Rect outer, android.graphics.Rect inner, int layer, com.android.server.wm.DisplayContent dc, boolean forceDefaultOrientation, android.view.SurfaceControl surfaceControl) throws android.view.Surface.OutOfResourcesException {
        boolean success = false;
        this.mTransactionFactory = factory;
        this.mOuterRect = new android.graphics.Rect(outer);
        this.mInnerRect = new android.graphics.Rect(inner);
        try {
            if (outer.top < inner.top) {
                this.mBlackSurfaces[0] = new com.android.server.wm.BlackFrame.BlackSurface(t, layer, outer.left, outer.top, inner.right, inner.top, dc, surfaceControl);
            }
            if (outer.left < inner.left) {
                this.mBlackSurfaces[1] = new com.android.server.wm.BlackFrame.BlackSurface(t, layer, outer.left, inner.top, inner.left, outer.bottom, dc, surfaceControl);
            }
            if (outer.bottom > inner.bottom) {
                this.mBlackSurfaces[2] = new com.android.server.wm.BlackFrame.BlackSurface(t, layer, inner.left, inner.bottom, outer.right, outer.bottom, dc, surfaceControl);
            }
            if (outer.right > inner.right) {
                this.mBlackSurfaces[3] = new com.android.server.wm.BlackFrame.BlackSurface(t, layer, inner.right, outer.top, outer.right, inner.bottom, dc, surfaceControl);
            }
            success = true;
        } finally {
            if (!success) {
                kill();
            }
        }
    }

    public void kill() {
        android.view.SurfaceControl.Transaction t = this.mTransactionFactory.get();
        for (int i = 0; i < this.mBlackSurfaces.length; i++) {
            if (this.mBlackSurfaces[i] != null) {
                if (com.android.internal.protolog.ProtoLogImpl_209941506.Cache.WM_SHOW_SURFACE_ALLOC_enabled[2]) {
                    java.lang.String protoLogParam0 = java.lang.String.valueOf(this.mBlackSurfaces[i].surface);
                    com.android.internal.protolog.ProtoLogImpl_209941506.i(com.android.internal.protolog.ProtoLogGroup.WM_SHOW_SURFACE_ALLOC, -5633771912572750947L, 0, null, protoLogParam0);
                }
                t.remove(this.mBlackSurfaces[i].surface);
                this.mBlackSurfaces[i] = null;
            }
        }
        t.apply();
    }
}
