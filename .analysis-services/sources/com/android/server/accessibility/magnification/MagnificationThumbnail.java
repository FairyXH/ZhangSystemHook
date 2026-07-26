package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
public class MagnificationThumbnail {
    private static final float ASPECT_RATIO = 14.0f;
    private static final float BG_ASPECT_RATIO = 7.0f;
    private static final boolean DEBUG = false;
    private static final int FADE_IN_ANIMATION_DURATION_MS = 200;
    private static final int FADE_OUT_ANIMATION_DURATION_MS = 1000;
    private static final int LINGER_DURATION_MS = 500;
    private static final java.lang.String LOG_TAG = "MagnificationThumbnail";
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private boolean mIsFadingIn;
    private android.animation.ObjectAnimator mThumbnailAnimator;
    public android.widget.FrameLayout mThumbnailLayout;
    private android.view.View mThumbnailView;
    private android.graphics.Rect mWindowBounds;
    private final android.view.WindowManager mWindowManager;
    private boolean mVisible = false;
    private final android.view.WindowManager.LayoutParams mBackgroundParams = createLayoutParams();
    private int mThumbnailWidth = 0;
    private int mThumbnailHeight = 0;

    public MagnificationThumbnail(android.content.Context context, android.view.WindowManager windowManager, android.os.Handler handler) {
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mHandler = handler;
        this.mWindowBounds = this.mWindowManager.getCurrentWindowMetrics().getBounds();
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationThumbnail$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.createThumbnailLayout();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createThumbnailLayout() {
        this.mThumbnailLayout = (android.widget.FrameLayout) android.view.LayoutInflater.from(this.mContext).inflate(android.R.layout.select_dialog_singlechoice_holo, (android.view.ViewGroup) null);
        this.mThumbnailView = this.mThumbnailLayout.findViewById(android.R.id.KEYCODE_ZOOM_OUT);
    }

    public void setThumbnailBounds(final android.graphics.Rect currentBounds, final float scale, final float centerX, final float centerY) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationThumbnail$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setThumbnailBounds$0(currentBounds, scale, centerX, centerY);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setThumbnailBounds$0(android.graphics.Rect currentBounds, float scale, float centerX, float centerY) {
        refreshBackgroundBounds(currentBounds);
        if (this.mVisible) {
            lambda$updateThumbnail$1(scale, centerX, centerY);
        }
    }

    private void refreshBackgroundBounds(android.graphics.Rect currentBounds) {
        this.mWindowBounds = currentBounds;
        android.graphics.Point magnificationBoundary = getMagnificationThumbnailPadding(this.mContext);
        this.mThumbnailWidth = (int) (this.mWindowBounds.width() / BG_ASPECT_RATIO);
        this.mThumbnailHeight = (int) (this.mWindowBounds.height() / BG_ASPECT_RATIO);
        int initX = magnificationBoundary.x;
        int initY = magnificationBoundary.y;
        this.mBackgroundParams.width = this.mThumbnailWidth;
        this.mBackgroundParams.height = this.mThumbnailHeight;
        this.mBackgroundParams.x = initX;
        this.mBackgroundParams.y = initY;
        if (this.mVisible) {
            this.mWindowManager.updateViewLayout(this.mThumbnailLayout, this.mBackgroundParams);
        }
    }

    private void showThumbnail() {
        animateThumbnail(true);
    }

    public void hideThumbnail() {
        this.mHandler.post(new com.android.server.accessibility.magnification.MagnificationThumbnail$$ExternalSyntheticLambda0(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hideThumbnailMainThread() {
        if (this.mVisible) {
            animateThumbnail(false);
        }
    }

    private void animateThumbnail(final boolean fadeIn) {
        this.mHandler.removeCallbacks(new com.android.server.accessibility.magnification.MagnificationThumbnail$$ExternalSyntheticLambda0(this));
        if (fadeIn) {
            this.mHandler.postDelayed(new com.android.server.accessibility.magnification.MagnificationThumbnail$$ExternalSyntheticLambda0(this), 500L);
        }
        if (fadeIn == this.mIsFadingIn) {
            return;
        }
        this.mIsFadingIn = fadeIn;
        if (fadeIn && !this.mVisible) {
            this.mWindowManager.addView(this.mThumbnailLayout, this.mBackgroundParams);
            this.mVisible = true;
        }
        if (this.mThumbnailAnimator != null) {
            this.mThumbnailAnimator.cancel();
        }
        this.mThumbnailAnimator = android.animation.ObjectAnimator.ofFloat(this.mThumbnailLayout, "alpha", fadeIn ? 1.0f : 0.0f);
        this.mThumbnailAnimator.setDuration(fadeIn ? 200L : 1000L);
        this.mThumbnailAnimator.addListener(new android.animation.Animator.AnimatorListener() { // from class: com.android.server.accessibility.magnification.MagnificationThumbnail.1
            private boolean mIsCancelled;

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(android.animation.Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(android.animation.Animator animation) {
                if (!this.mIsCancelled && !fadeIn && com.android.server.accessibility.magnification.MagnificationThumbnail.this.mVisible) {
                    com.android.server.accessibility.magnification.MagnificationThumbnail.this.mWindowManager.removeView(com.android.server.accessibility.magnification.MagnificationThumbnail.this.mThumbnailLayout);
                    com.android.server.accessibility.magnification.MagnificationThumbnail.this.mVisible = false;
                }
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(android.animation.Animator animation) {
                this.mIsCancelled = true;
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(android.animation.Animator animation) {
            }
        });
        this.mThumbnailAnimator.start();
    }

    public void updateThumbnail(final float scale, final float centerX, final float centerY) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationThumbnail$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateThumbnail$1(scale, centerX, centerY);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: updateThumbnailMainThread, reason: merged with bridge method [inline-methods] */
    public void lambda$updateThumbnail$1(float scale, float centerX, float centerY) {
        showThumbnail();
        float scaleDown = java.lang.Float.isNaN(scale) ? this.mThumbnailView.getScaleX() : 1.0f / scale;
        if (!java.lang.Float.isNaN(scale)) {
            this.mThumbnailView.setScaleX(scaleDown);
            this.mThumbnailView.setScaleY(scaleDown);
        }
        if (!java.lang.Float.isNaN(centerX) && !java.lang.Float.isNaN(centerY) && this.mThumbnailWidth > 0 && this.mThumbnailHeight > 0) {
            int padding = this.mThumbnailView.getPaddingTop();
            float centerXScaled = (centerX * 0.14285715f) - ((this.mThumbnailWidth / 2.0f) + padding);
            float centerYScaled = (centerY * 0.14285715f) - ((this.mThumbnailHeight / 2.0f) + padding);
            this.mThumbnailView.setTranslationX(centerXScaled);
            this.mThumbnailView.setTranslationY(centerYScaled);
        }
    }

    private android.view.WindowManager.LayoutParams createLayoutParams() {
        android.view.WindowManager.LayoutParams params = new android.view.WindowManager.LayoutParams(-2, -2, 2027, 24, -2);
        params.inputFeatures = 1;
        params.gravity = 83;
        params.setFitInsetsTypes(android.view.WindowInsets.Type.ime() | android.view.WindowInsets.Type.navigationBars());
        return params;
    }

    private android.graphics.Point getMagnificationThumbnailPadding(android.content.Context context) {
        android.graphics.Point thumbnailPaddings = new android.graphics.Point(0, 0);
        int defaultPadding = this.mContext.getResources().getDimensionPixelSize(android.R.dimen.accessibility_focus_highlight_stroke_width);
        thumbnailPaddings.x = defaultPadding;
        thumbnailPaddings.y = defaultPadding;
        return thumbnailPaddings;
    }
}
