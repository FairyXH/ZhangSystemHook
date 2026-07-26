package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class Letterbox {
    static final android.graphics.Rect EMPTY_RECT = new android.graphics.Rect();
    private static final android.graphics.Point ZERO_POINT = new android.graphics.Point(0, 0);
    private final java.util.function.BooleanSupplier mAreCornersRounded;
    private final java.util.function.IntSupplier mBlurRadiusSupplier;
    private final java.util.function.Supplier<android.graphics.Color> mColorSupplier;
    private final java.util.function.DoubleSupplier mDarkScrimAlphaSupplier;
    private final java.util.function.IntConsumer mDoubleTapCallbackX;
    private final java.util.function.IntConsumer mDoubleTapCallbackY;
    private final java.util.function.BooleanSupplier mHasWallpaperBackgroundSupplier;
    private final java.util.function.Supplier<android.view.SurfaceControl> mParentSurfaceSupplier;
    private final java.util.function.Supplier<android.view.SurfaceControl.Builder> mSurfaceControlFactory;
    private final java.util.function.Supplier<android.view.SurfaceControl.Transaction> mTransactionFactory;
    private final android.graphics.Rect mOuter = new android.graphics.Rect();
    private final android.graphics.Rect mInner = new android.graphics.Rect();
    private final com.android.server.wm.Letterbox.LetterboxSurface mTop = new com.android.server.wm.Letterbox.LetterboxSurface("top");
    private final com.android.server.wm.Letterbox.LetterboxSurface mLeft = new com.android.server.wm.Letterbox.LetterboxSurface("left");
    private final com.android.server.wm.Letterbox.LetterboxSurface mBottom = new com.android.server.wm.Letterbox.LetterboxSurface("bottom");
    private final com.android.server.wm.Letterbox.LetterboxSurface mRight = new com.android.server.wm.Letterbox.LetterboxSurface("right");
    private final com.android.server.wm.Letterbox.LetterboxSurface mFullWindowSurface = new com.android.server.wm.Letterbox.LetterboxSurface("fullWindow");
    private final com.android.server.wm.Letterbox.LetterboxSurface[] mSurfaces = {this.mLeft, this.mTop, this.mRight, this.mBottom};

    public Letterbox(java.util.function.Supplier<android.view.SurfaceControl.Builder> surfaceControlFactory, java.util.function.Supplier<android.view.SurfaceControl.Transaction> transactionFactory, java.util.function.BooleanSupplier areCornersRounded, java.util.function.Supplier<android.graphics.Color> colorSupplier, java.util.function.BooleanSupplier hasWallpaperBackgroundSupplier, java.util.function.IntSupplier blurRadiusSupplier, java.util.function.DoubleSupplier darkScrimAlphaSupplier, java.util.function.IntConsumer doubleTapCallbackX, java.util.function.IntConsumer doubleTapCallbackY, java.util.function.Supplier<android.view.SurfaceControl> parentSurface) {
        this.mSurfaceControlFactory = surfaceControlFactory;
        this.mTransactionFactory = transactionFactory;
        this.mAreCornersRounded = areCornersRounded;
        this.mColorSupplier = colorSupplier;
        this.mHasWallpaperBackgroundSupplier = hasWallpaperBackgroundSupplier;
        this.mBlurRadiusSupplier = blurRadiusSupplier;
        this.mDarkScrimAlphaSupplier = darkScrimAlphaSupplier;
        this.mDoubleTapCallbackX = doubleTapCallbackX;
        this.mDoubleTapCallbackY = doubleTapCallbackY;
        this.mParentSurfaceSupplier = parentSurface;
    }

    public void layout(android.graphics.Rect outer, android.graphics.Rect inner, android.graphics.Point surfaceOrigin) {
        this.mOuter.set(outer);
        this.mInner.set(inner);
        this.mTop.layout(outer.left, outer.top, outer.right, inner.top, surfaceOrigin);
        this.mLeft.layout(outer.left, outer.top, inner.left, outer.bottom, surfaceOrigin);
        this.mBottom.layout(outer.left, inner.bottom, outer.right, outer.bottom, surfaceOrigin);
        this.mRight.layout(inner.right, outer.top, outer.right, outer.bottom, surfaceOrigin);
        this.mFullWindowSurface.layout(outer.left, outer.top, outer.right, outer.bottom, surfaceOrigin);
    }

    public android.graphics.Rect getInsets() {
        return new android.graphics.Rect(this.mLeft.getWidth(), this.mTop.getHeight(), this.mRight.getWidth(), this.mBottom.getHeight());
    }

    android.graphics.Rect getInnerFrame() {
        return this.mInner;
    }

    android.graphics.Rect getOuterFrame() {
        return this.mOuter;
    }

    boolean notIntersectsOrFullyContains(android.graphics.Rect rect) {
        int emptyCount = 0;
        int noOverlappingCount = 0;
        for (com.android.server.wm.Letterbox.LetterboxSurface surface : this.mSurfaces) {
            android.graphics.Rect surfaceRect = surface.mLayoutFrameGlobal;
            if (surfaceRect.isEmpty()) {
                emptyCount++;
            } else if (!android.graphics.Rect.intersects(surfaceRect, rect)) {
                noOverlappingCount++;
            } else if (surfaceRect.contains(rect)) {
                return true;
            }
        }
        return emptyCount + noOverlappingCount == this.mSurfaces.length;
    }

    public void hide() {
        layout(EMPTY_RECT, EMPTY_RECT, ZERO_POINT);
    }

    public void destroy() {
        this.mOuter.setEmpty();
        this.mInner.setEmpty();
        for (com.android.server.wm.Letterbox.LetterboxSurface surface : this.mSurfaces) {
            surface.remove();
        }
        this.mFullWindowSurface.remove();
    }

    public boolean needsApplySurfaceChanges() {
        if (useFullWindowSurface()) {
            return this.mFullWindowSurface.needsApplySurfaceChanges();
        }
        for (com.android.server.wm.Letterbox.LetterboxSurface surface : this.mSurfaces) {
            if (surface.needsApplySurfaceChanges()) {
                return true;
            }
        }
        return false;
    }

    public void applySurfaceChanges(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl.Transaction inputT) {
        int i = 0;
        if (useFullWindowSurface()) {
            this.mFullWindowSurface.applySurfaceChanges(t, inputT);
            com.android.server.wm.Letterbox.LetterboxSurface[] letterboxSurfaceArr = this.mSurfaces;
            int length = letterboxSurfaceArr.length;
            while (i < length) {
                com.android.server.wm.Letterbox.LetterboxSurface surface = letterboxSurfaceArr[i];
                surface.remove();
                i++;
            }
            return;
        }
        com.android.server.wm.Letterbox.LetterboxSurface[] letterboxSurfaceArr2 = this.mSurfaces;
        int length2 = letterboxSurfaceArr2.length;
        while (i < length2) {
            com.android.server.wm.Letterbox.LetterboxSurface surface2 = letterboxSurfaceArr2[i];
            surface2.applySurfaceChanges(t, inputT);
            i++;
        }
        this.mFullWindowSurface.remove();
    }

    void attachInput(com.android.server.wm.WindowState win) {
        if (useFullWindowSurface()) {
            this.mFullWindowSurface.attachInput(win);
            return;
        }
        for (com.android.server.wm.Letterbox.LetterboxSurface surface : this.mSurfaces) {
            surface.attachInput(win);
        }
    }

    void onMovedToDisplay(int displayId) {
        for (com.android.server.wm.Letterbox.LetterboxSurface surface : this.mSurfaces) {
            if (surface.mInputInterceptor != null) {
                surface.mInputInterceptor.mWindowHandle.displayId = displayId;
            }
        }
        if (this.mFullWindowSurface.mInputInterceptor != null) {
            this.mFullWindowSurface.mInputInterceptor.mWindowHandle.displayId = displayId;
        }
    }

    private boolean useFullWindowSurface() {
        return this.mAreCornersRounded.getAsBoolean() || this.mHasWallpaperBackgroundSupplier.getAsBoolean();
    }

    private final class TapEventReceiver extends android.view.InputEventReceiver {
        private final android.view.GestureDetector mDoubleTapDetector;
        private final com.android.server.wm.Letterbox.DoubleTapListener mDoubleTapListener;

        TapEventReceiver(android.view.InputChannel inputChannel, com.android.server.wm.WindowManagerService wmService, android.os.Handler uiHandler) {
            super(inputChannel, uiHandler.getLooper());
            this.mDoubleTapListener = new com.android.server.wm.Letterbox.DoubleTapListener(wmService);
            this.mDoubleTapDetector = new android.view.GestureDetector(wmService.mContext, this.mDoubleTapListener, uiHandler);
        }

        public void onInputEvent(android.view.InputEvent event) {
            android.view.MotionEvent motionEvent = (android.view.MotionEvent) event;
            finishInputEvent(event, this.mDoubleTapDetector.onTouchEvent(motionEvent));
        }
    }

    private class DoubleTapListener extends android.view.GestureDetector.SimpleOnGestureListener {
        private final com.android.server.wm.WindowManagerService mWmService;

        private DoubleTapListener(com.android.server.wm.WindowManagerService wmService) {
            this.mWmService = wmService;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTapEvent(android.view.MotionEvent e) {
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWmService.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    if (com.android.server.wm.Letterbox.this.mOuter.isEmpty() || e.getAction() != 1) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return false;
                    }
                    com.android.server.wm.Letterbox.this.mDoubleTapCallbackX.accept((int) e.getRawX());
                    com.android.server.wm.Letterbox.this.mDoubleTapCallbackY.accept((int) e.getRawY());
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    return true;
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
        }
    }

    private final class InputInterceptor implements java.lang.Runnable {
        private final android.view.InputChannel mClientChannel;
        private final android.os.Handler mHandler = com.android.server.UiThread.getHandler();
        private final android.view.InputEventReceiver mInputEventReceiver;
        private final android.os.IBinder mToken;
        private final android.view.InputWindowHandle mWindowHandle;
        private final com.android.server.wm.WindowManagerService mWmService;

        InputInterceptor(java.lang.String namePrefix, com.android.server.wm.WindowState win) {
            this.mWmService = win.mWmService;
            java.lang.String name = namePrefix + (win.mActivityRecord != null ? win.mActivityRecord : win);
            this.mClientChannel = this.mWmService.mInputManager.createInputChannel(name);
            this.mInputEventReceiver = com.android.server.wm.Letterbox.this.new TapEventReceiver(this.mClientChannel, this.mWmService, this.mHandler);
            this.mToken = this.mClientChannel.getToken();
            this.mWindowHandle = new android.view.InputWindowHandle((android.view.InputApplicationHandle) null, win.getDisplayId());
            this.mWindowHandle.name = name;
            this.mWindowHandle.token = this.mToken;
            this.mWindowHandle.layoutParamsType = 2022;
            this.mWindowHandle.dispatchingTimeoutMillis = android.os.InputConstants.DEFAULT_DISPATCHING_TIMEOUT_MILLIS;
            this.mWindowHandle.ownerPid = com.android.server.wm.WindowManagerService.MY_PID;
            this.mWindowHandle.ownerUid = com.android.server.wm.WindowManagerService.MY_UID;
            this.mWindowHandle.scaleFactor = 1.0f;
            this.mWindowHandle.inputConfig = 1028;
        }

        void updateTouchableRegion(android.graphics.Rect frame) {
            if (frame.isEmpty()) {
                this.mWindowHandle.token = null;
                return;
            }
            this.mWindowHandle.token = this.mToken;
            this.mWindowHandle.touchableRegion.set(frame);
            this.mWindowHandle.touchableRegion.translate(-frame.left, -frame.top);
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mInputEventReceiver.dispose();
            this.mClientChannel.dispose();
        }

        void dispose() {
            this.mWmService.mInputManager.removeInputChannel(this.mToken);
            this.mHandler.post(this);
        }
    }

    private class LetterboxSurface {
        private android.graphics.Color mColor;
        private boolean mHasWallpaperBackground;
        private com.android.server.wm.Letterbox.InputInterceptor mInputInterceptor;
        private android.view.SurfaceControl mParentSurface;
        private android.view.SurfaceControl mSurface;
        private final java.lang.String mType;
        private final android.graphics.Rect mSurfaceFrameRelative = new android.graphics.Rect();
        private final android.graphics.Rect mLayoutFrameGlobal = new android.graphics.Rect();
        private final android.graphics.Rect mLayoutFrameRelative = new android.graphics.Rect();

        public LetterboxSurface(java.lang.String type) {
            this.mType = type;
        }

        public void layout(int left, int top, int right, int bottom, android.graphics.Point surfaceOrigin) {
            this.mLayoutFrameGlobal.set(left, top, right, bottom);
            this.mLayoutFrameRelative.set(this.mLayoutFrameGlobal);
            this.mLayoutFrameRelative.offset(-surfaceOrigin.x, -surfaceOrigin.y);
        }

        private void createSurface(android.view.SurfaceControl.Transaction t) {
            this.mSurface = ((android.view.SurfaceControl.Builder) com.android.server.wm.Letterbox.this.mSurfaceControlFactory.get()).setName("Letterbox - " + this.mType).setFlags(4).setColorLayer().setCallsite("LetterboxSurface.createSurface").build();
            t.setLayer(this.mSurface, -20000).setColorSpaceAgnostic(this.mSurface, true);
        }

        void attachInput(com.android.server.wm.WindowState win) {
            if (this.mInputInterceptor != null) {
                this.mInputInterceptor.dispose();
            }
            this.mInputInterceptor = com.android.server.wm.Letterbox.this.new InputInterceptor("Letterbox_" + this.mType + "_", win);
        }

        boolean isRemoved() {
            return (this.mSurface == null && this.mInputInterceptor == null) ? false : true;
        }

        public void remove() {
            if (this.mSurface != null) {
                ((android.view.SurfaceControl.Transaction) com.android.server.wm.Letterbox.this.mTransactionFactory.get()).remove(this.mSurface).apply();
                this.mSurface = null;
            }
            if (this.mInputInterceptor != null) {
                this.mInputInterceptor.dispose();
                this.mInputInterceptor = null;
            }
        }

        public int getWidth() {
            return java.lang.Math.max(0, this.mLayoutFrameGlobal.width());
        }

        public int getHeight() {
            return java.lang.Math.max(0, this.mLayoutFrameGlobal.height());
        }

        public void applySurfaceChanges(android.view.SurfaceControl.Transaction t, android.view.SurfaceControl.Transaction inputT) {
            if (!needsApplySurfaceChanges()) {
                return;
            }
            this.mSurfaceFrameRelative.set(this.mLayoutFrameRelative);
            if (!this.mSurfaceFrameRelative.isEmpty()) {
                if (this.mSurface == null) {
                    createSurface(t);
                }
                this.mColor = (android.graphics.Color) com.android.server.wm.Letterbox.this.mColorSupplier.get();
                this.mParentSurface = (android.view.SurfaceControl) com.android.server.wm.Letterbox.this.mParentSurfaceSupplier.get();
                t.setColor(this.mSurface, getRgbColorArray());
                t.setPosition(this.mSurface, this.mSurfaceFrameRelative.left, this.mSurfaceFrameRelative.top);
                t.setWindowCrop(this.mSurface, this.mSurfaceFrameRelative.width(), this.mSurfaceFrameRelative.height());
                t.reparent(this.mSurface, this.mParentSurface);
                this.mHasWallpaperBackground = com.android.server.wm.Letterbox.this.mHasWallpaperBackgroundSupplier.getAsBoolean();
                updateAlphaAndBlur(t);
                t.show(this.mSurface);
            } else if (this.mSurface != null) {
                t.hide(this.mSurface);
            }
            if (this.mSurface != null && this.mInputInterceptor != null) {
                this.mInputInterceptor.updateTouchableRegion(this.mSurfaceFrameRelative);
                inputT.setInputWindowInfo(this.mSurface, this.mInputInterceptor.mWindowHandle);
            }
        }

        private void updateAlphaAndBlur(android.view.SurfaceControl.Transaction t) {
            if (!this.mHasWallpaperBackground) {
                t.setAlpha(this.mSurface, 1.0f);
                t.setBackgroundBlurRadius(this.mSurface, 0);
                return;
            }
            float alpha = (float) com.android.server.wm.Letterbox.this.mDarkScrimAlphaSupplier.getAsDouble();
            t.setAlpha(this.mSurface, alpha);
            if (com.android.server.wm.Letterbox.this.mBlurRadiusSupplier.getAsInt() <= 0) {
                t.setBackgroundBlurRadius(this.mSurface, 0);
            } else {
                t.setBackgroundBlurRadius(this.mSurface, com.android.server.wm.Letterbox.this.mBlurRadiusSupplier.getAsInt());
            }
        }

        private float[] getRgbColorArray() {
            float[] rgbTmpFloat = {this.mColor.red(), this.mColor.green(), this.mColor.blue()};
            return rgbTmpFloat;
        }

        public boolean needsApplySurfaceChanges() {
            return (this.mSurfaceFrameRelative.equals(this.mLayoutFrameRelative) && (this.mSurfaceFrameRelative.isEmpty() || (com.android.server.wm.Letterbox.this.mHasWallpaperBackgroundSupplier.getAsBoolean() == this.mHasWallpaperBackground && ((android.graphics.Color) com.android.server.wm.Letterbox.this.mColorSupplier.get()).equals(this.mColor) && com.android.server.wm.Letterbox.this.mParentSurfaceSupplier.get() == this.mParentSurface))) ? false : true;
        }
    }
}
