package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
class PanningScalingHandler extends android.view.GestureDetector.SimpleOnGestureListener implements android.view.ScaleGestureDetector.OnScaleGestureListener {
    private final boolean mBlockScroll;
    private final int mDisplayId;
    private boolean mEnable;
    private float mInitialScaleFactor = -1.0f;
    private final com.android.server.accessibility.magnification.PanningScalingHandler.MagnificationDelegate mMagnificationDelegate;
    private final float mMaxScale;
    private final float mMinScale;
    private final android.view.ScaleGestureDetector mScaleGestureDetector;
    private boolean mScaling;
    private final float mScalingThreshold;
    private final android.view.GestureDetector mScrollGestureDetector;
    private static final java.lang.String TAG = "PanningScalingHandler";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    interface MagnificationDelegate {
        float getScale(int i);

        boolean processScroll(int i, float f, float f2);

        void setScale(int i, float f);
    }

    PanningScalingHandler(android.content.Context context, float maxScale, float minScale, boolean blockScroll, com.android.server.accessibility.magnification.PanningScalingHandler.MagnificationDelegate magnificationDelegate) {
        this.mDisplayId = context.getDisplayId();
        this.mMaxScale = maxScale;
        this.mMinScale = minScale;
        this.mBlockScroll = blockScroll;
        if (com.android.server.accessibility.Flags.pinchZoomZeroMinSpan()) {
            this.mScaleGestureDetector = new android.view.ScaleGestureDetector(context, android.view.ViewConfiguration.get(context).getScaledTouchSlop() * 2, 0, android.os.Handler.getMain(), this);
        } else {
            this.mScaleGestureDetector = new android.view.ScaleGestureDetector(context, this, android.os.Handler.getMain());
        }
        this.mScrollGestureDetector = new android.view.GestureDetector(context, this, android.os.Handler.getMain());
        this.mScaleGestureDetector.setQuickScaleEnabled(false);
        this.mMagnificationDelegate = magnificationDelegate;
        android.util.TypedValue scaleValue = new android.util.TypedValue();
        context.getResources().getValue(android.R.dimen.config_minPercentageMultiWindowSupportHeight, scaleValue, false);
        this.mScalingThreshold = scaleValue.getFloat();
    }

    void setEnabled(boolean enable) {
        clear();
        this.mEnable = enable;
    }

    void onTouchEvent(android.view.MotionEvent motionEvent) {
        this.mScaleGestureDetector.onTouchEvent(motionEvent);
        this.mScrollGestureDetector.onTouchEvent(motionEvent);
    }

    @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
    public boolean onScroll(android.view.MotionEvent e1, android.view.MotionEvent e2, float distanceX, float distanceY) {
        if (!this.mEnable) {
            return true;
        }
        if (this.mBlockScroll && this.mScaling) {
            return true;
        }
        return this.mMagnificationDelegate.processScroll(this.mDisplayId, distanceX, distanceY);
    }

    @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
    public boolean onScale(android.view.ScaleGestureDetector detector) {
        float scale;
        if (DEBUG) {
            android.util.Slog.i(TAG, "onScale: triggered ");
        }
        if (!this.mScaling) {
            if (this.mInitialScaleFactor < 0.0f) {
                this.mInitialScaleFactor = detector.getScaleFactor();
                return false;
            }
            float deltaScale = detector.getScaleFactor() - this.mInitialScaleFactor;
            this.mScaling = java.lang.Math.abs(deltaScale) > this.mScalingThreshold;
            return this.mScaling;
        }
        float initialScale = this.mMagnificationDelegate.getScale(this.mDisplayId);
        float targetScale = detector.getScaleFactor() * initialScale;
        if (targetScale > this.mMaxScale && targetScale > initialScale) {
            scale = this.mMaxScale;
        } else {
            float scale2 = this.mMinScale;
            if (targetScale < scale2 && targetScale < initialScale) {
                scale = this.mMinScale;
            } else {
                scale = targetScale;
            }
        }
        if (DEBUG) {
            android.util.Slog.i(TAG, "Scaled content to: " + scale + "x");
        }
        this.mMagnificationDelegate.setScale(this.mDisplayId, scale);
        return true;
    }

    @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
    public boolean onScaleBegin(android.view.ScaleGestureDetector detector) {
        return this.mEnable;
    }

    @Override // android.view.ScaleGestureDetector.OnScaleGestureListener
    public void onScaleEnd(android.view.ScaleGestureDetector detector) {
        clear();
    }

    void clear() {
        this.mInitialScaleFactor = -1.0f;
        this.mScaling = false;
    }

    public java.lang.String toString() {
        return "PanningScalingHandler{mInitialScaleFactor=" + this.mInitialScaleFactor + ", mScaling=" + this.mScaling + '}';
    }
}
