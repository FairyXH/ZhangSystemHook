package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class ImmersiveModeConfirmation {
    private static final java.lang.String CONFIRMED = "confirmed";
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_SHOW_EVERY_TIME = false;
    private static final int IMMERSIVE_MODE_CONFIRMATION_WINDOW_TYPE = 2017;
    private static final java.lang.String TAG = "ImmersiveModeConfirmation";
    private static boolean sConfirmed;
    private boolean mCanSystemBarsBeShownByUser;
    private com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView mClingWindow;
    private final android.content.Context mContext;
    private final com.android.server.wm.ImmersiveModeConfirmation.H mHandler;
    private final long mPanicThresholdMs;
    private long mPanicTime;
    private final long mShowDelayMs;
    private boolean mVrModeEnabled;
    private android.content.Context mWindowContext;
    private android.view.WindowManager mWindowManager;
    private final android.os.IBinder mWindowToken = new android.os.Binder();
    private int mWindowContextRootDisplayAreaId = -1;
    private int mLockTaskState = 0;
    private final java.lang.Runnable mConfirm = new java.lang.Runnable() { // from class: com.android.server.wm.ImmersiveModeConfirmation.1
        @Override // java.lang.Runnable
        public void run() {
            if (!com.android.server.wm.ImmersiveModeConfirmation.sConfirmed) {
                com.android.server.wm.ImmersiveModeConfirmation.sConfirmed = true;
                com.android.server.wm.ImmersiveModeConfirmation.saveSetting(com.android.server.wm.ImmersiveModeConfirmation.this.mContext);
            }
            com.android.server.wm.ImmersiveModeConfirmation.this.handleHide();
        }
    };

    ImmersiveModeConfirmation(android.content.Context context, android.os.Looper looper, boolean vrModeEnabled, boolean canSystemBarsBeShownByUser) {
        android.view.Display display = context.getDisplay();
        android.content.Context uiContext = android.app.ActivityThread.currentActivityThread().getSystemUiContext();
        this.mContext = display.getDisplayId() == 0 ? uiContext : uiContext.createDisplayContext(display);
        this.mHandler = new com.android.server.wm.ImmersiveModeConfirmation.H(looper);
        this.mShowDelayMs = ((long) context.getResources().getInteger(android.R.integer.device_idle_light_idle_factor)) * 3;
        this.mPanicThresholdMs = context.getResources().getInteger(android.R.integer.config_externalDisplayPeakHeight);
        this.mVrModeEnabled = vrModeEnabled;
        this.mCanSystemBarsBeShownByUser = canSystemBarsBeShownByUser;
    }

    static boolean loadSetting(int currentUserId, android.content.Context context) {
        boolean wasConfirmed = sConfirmed;
        sConfirmed = false;
        java.lang.String value = null;
        try {
            value = android.provider.Settings.Secure.getStringForUser(context.getContentResolver(), "immersive_mode_confirmations", -2);
            sConfirmed = CONFIRMED.equals(value);
        } catch (java.lang.Throwable t) {
            android.util.Slog.w(TAG, "Error loading confirmations, value=" + value, t);
        }
        return sConfirmed != wasConfirmed;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void saveSetting(android.content.Context context) {
        try {
            java.lang.String value = sConfirmed ? CONFIRMED : null;
            android.provider.Settings.Secure.putStringForUser(context.getContentResolver(), "immersive_mode_confirmations", value, -2);
        } catch (java.lang.Throwable t) {
            android.util.Slog.w(TAG, "Error saving confirmations, sConfirmed=" + sConfirmed, t);
        }
    }

    void release() {
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
    }

    boolean onSettingChanged(int currentUserId) {
        boolean changed = loadSetting(currentUserId, this.mContext);
        if (changed && sConfirmed) {
            this.mHandler.sendEmptyMessage(2);
        }
        return changed;
    }

    void immersiveModeChangedLw(int rootDisplayAreaId, boolean isImmersiveMode, boolean userSetupComplete, boolean navBarEmpty) {
        this.mHandler.removeMessages(1);
        if (isImmersiveMode) {
            if (!sConfirmed && userSetupComplete && !this.mVrModeEnabled && this.mCanSystemBarsBeShownByUser && !navBarEmpty && !android.os.UserManager.isDeviceInDemoMode(this.mContext) && this.mLockTaskState != 1) {
                android.os.Message msg = this.mHandler.obtainMessage(1);
                msg.arg1 = rootDisplayAreaId;
                this.mHandler.sendMessageDelayed(msg, this.mShowDelayMs);
                return;
            }
            return;
        }
        this.mHandler.sendEmptyMessage(2);
    }

    boolean onPowerKeyDown(boolean isScreenOn, long time, boolean inImmersiveMode, boolean navBarEmpty) {
        if (!isScreenOn && time - this.mPanicTime < this.mPanicThresholdMs) {
            return this.mClingWindow == null;
        }
        if (isScreenOn && inImmersiveMode && !navBarEmpty) {
            this.mPanicTime = time;
        } else {
            this.mPanicTime = 0L;
        }
        return false;
    }

    void confirmCurrentPrompt() {
        if (this.mClingWindow != null) {
            this.mHandler.post(this.mConfirm);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleHide() {
        if (this.mClingWindow != null) {
            if (this.mWindowManager != null) {
                try {
                    this.mWindowManager.removeView(this.mClingWindow);
                } catch (android.view.WindowManager.InvalidDisplayException e) {
                    android.util.Slog.w(TAG, "Fail to hide the immersive confirmation window because of " + e);
                }
                this.mWindowManager = null;
                this.mWindowContext = null;
            }
            this.mClingWindow = null;
        }
    }

    private android.view.WindowManager.LayoutParams getClingWindowLayoutParams() {
        android.view.WindowManager.LayoutParams lp = new android.view.WindowManager.LayoutParams(-1, -1, IMMERSIVE_MODE_CONFIRMATION_WINDOW_TYPE, 16777504, -3);
        lp.setFitInsetsTypes(lp.getFitInsetsTypes() & (~android.view.WindowInsets.Type.statusBars()));
        lp.layoutInDisplayCutoutMode = 3;
        lp.privateFlags |= 537002000;
        lp.setTitle(TAG);
        lp.windowAnimations = android.R.style.Animation.DropDownDown;
        lp.token = getWindowToken();
        return lp;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.widget.FrameLayout.LayoutParams getBubbleLayoutParams() {
        return new android.widget.FrameLayout.LayoutParams(getClingWindowWidth(), -2, 49);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getClingWindowWidth() {
        return this.mContext.getResources().getDimensionPixelSize(android.R.dimen.floating_toolbar_minimum_overflow_height);
    }

    android.os.IBinder getWindowToken() {
        return this.mWindowToken;
    }

    private class ClingWindowView extends android.widget.FrameLayout {
        private static final int ANIMATION_DURATION = 250;
        private static final int BGCOLOR = Integer.MIN_VALUE;
        private static final int OFFSET_DP = 96;
        private android.view.ViewGroup mClingLayout;
        private final android.graphics.drawable.ColorDrawable mColor;
        private android.animation.ValueAnimator mColorAnim;
        private final java.lang.Runnable mConfirm;
        private android.view.ViewTreeObserver.OnComputeInternalInsetsListener mInsetsListener;
        private final android.view.animation.Interpolator mInterpolator;
        private android.content.BroadcastReceiver mReceiver;
        private java.lang.Runnable mUpdateLayoutRunnable;

        ClingWindowView(android.content.Context context, java.lang.Runnable confirm) {
            super(context);
            this.mColor = new android.graphics.drawable.ColorDrawable(0);
            this.mUpdateLayoutRunnable = new java.lang.Runnable() { // from class: com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.1
                @Override // java.lang.Runnable
                public void run() {
                    if (com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mClingLayout != null && com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mClingLayout.getParent() != null) {
                        com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mClingLayout.setLayoutParams(com.android.server.wm.ImmersiveModeConfirmation.this.getBubbleLayoutParams());
                    }
                }
            };
            this.mInsetsListener = new android.view.ViewTreeObserver.OnComputeInternalInsetsListener() { // from class: com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.2
                private final int[] mTmpInt2 = new int[2];

                public void onComputeInternalInsets(android.view.ViewTreeObserver.InternalInsetsInfo inoutInfo) {
                    com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mClingLayout.getLocationInWindow(this.mTmpInt2);
                    inoutInfo.setTouchableInsets(3);
                    inoutInfo.touchableRegion.set(this.mTmpInt2[0], this.mTmpInt2[1], this.mTmpInt2[0] + com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mClingLayout.getWidth(), this.mTmpInt2[1] + com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mClingLayout.getHeight());
                }
            };
            this.mReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.3
                @Override // android.content.BroadcastReceiver
                public void onReceive(android.content.Context context2, android.content.Intent intent) {
                    if (intent.getAction().equals("android.intent.action.CONFIGURATION_CHANGED")) {
                        com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.post(com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mUpdateLayoutRunnable);
                    }
                }
            };
            this.mConfirm = confirm;
            setBackground(this.mColor);
            setImportantForAccessibility(2);
            this.mInterpolator = android.view.animation.AnimationUtils.loadInterpolator(this.mContext, android.R.interpolator.linear_out_slow_in);
        }

        @Override // android.view.ViewGroup, android.view.View
        public void onAttachedToWindow() {
            super.onAttachedToWindow();
            android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
            this.mContext.getDisplay().getMetrics(metrics);
            float density = metrics.density;
            getViewTreeObserver().addOnComputeInternalInsetsListener(this.mInsetsListener);
            this.mClingLayout = (android.view.ViewGroup) android.view.View.inflate(getContext(), android.R.layout.heavy_weight_switcher, null);
            android.widget.Button ok = (android.widget.Button) this.mClingLayout.findViewById(android.R.id.nine);
            ok.setOnClickListener(new android.view.View.OnClickListener() { // from class: com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.4
                @Override // android.view.View.OnClickListener
                public void onClick(android.view.View v) {
                    com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mConfirm.run();
                }
            });
            addView(this.mClingLayout, com.android.server.wm.ImmersiveModeConfirmation.this.getBubbleLayoutParams());
            if (android.app.ActivityManager.isHighEndGfx()) {
                final android.view.View cling = this.mClingLayout;
                cling.setAlpha(0.0f);
                cling.setTranslationY((-96.0f) * density);
                postOnAnimation(new java.lang.Runnable() { // from class: com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.5
                    @Override // java.lang.Runnable
                    public void run() {
                        cling.animate().alpha(1.0f).translationY(0.0f).setDuration(250L).setInterpolator(com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mInterpolator).withLayer().start();
                        com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mColorAnim = android.animation.ValueAnimator.ofObject(new android.animation.ArgbEvaluator(), 0, Integer.MIN_VALUE);
                        com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mColorAnim.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() { // from class: com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.5.1
                            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                            public void onAnimationUpdate(android.animation.ValueAnimator animation) {
                                int c = ((java.lang.Integer) animation.getAnimatedValue()).intValue();
                                com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mColor.setColor(c);
                            }
                        });
                        com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mColorAnim.setDuration(250L);
                        com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mColorAnim.setInterpolator(com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mInterpolator);
                        com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView.this.mColorAnim.start();
                    }
                });
            } else {
                this.mColor.setColor(Integer.MIN_VALUE);
            }
            this.mContext.registerReceiver(this.mReceiver, new android.content.IntentFilter("android.intent.action.CONFIGURATION_CHANGED"));
        }

        @Override // android.view.ViewGroup, android.view.View
        public void onDetachedFromWindow() {
            this.mContext.unregisterReceiver(this.mReceiver);
        }

        @Override // android.view.View
        public boolean onTouchEvent(android.view.MotionEvent motion) {
            return true;
        }

        @Override // android.view.View
        public android.view.WindowInsets onApplyWindowInsets(android.view.WindowInsets insets) {
            android.graphics.Rect topDisplayCutout;
            int width = getWidth();
            int windowWidth = com.android.server.wm.ImmersiveModeConfirmation.this.getClingWindowWidth();
            if (insets.getDisplayCutout() != null) {
                topDisplayCutout = insets.getDisplayCutout().getBoundingRectTop();
            } else {
                topDisplayCutout = new android.graphics.Rect();
            }
            boolean intersectsTopCutout = topDisplayCutout.intersects(width - (windowWidth / 2), 0, (windowWidth / 2) + width, topDisplayCutout.bottom);
            if (com.android.server.wm.ImmersiveModeConfirmation.this.mClingWindow != null && (windowWidth < 0 || (width > 0 && intersectsTopCutout))) {
                android.view.View iconView = com.android.server.wm.ImmersiveModeConfirmation.this.mClingWindow.findViewById(android.R.id.grant_credentials_permission_message_header);
                android.widget.RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) iconView.getLayoutParams();
                lp.topMargin = topDisplayCutout.bottom;
                iconView.setLayoutParams(lp);
            }
            return new android.view.WindowInsets.Builder(insets).setInsets(android.view.WindowInsets.Type.systemBars(), android.graphics.Insets.NONE).build();
        }
    }

    private android.view.WindowManager createWindowManager(int rootDisplayAreaId) {
        if (this.mWindowManager != null) {
            throw new java.lang.IllegalStateException("Must not create a new WindowManager while there is an existing one");
        }
        android.os.Bundle options = getOptionsForWindowContext(rootDisplayAreaId);
        this.mWindowContextRootDisplayAreaId = rootDisplayAreaId;
        this.mWindowContext = this.mContext.createWindowContext(IMMERSIVE_MODE_CONFIRMATION_WINDOW_TYPE, options);
        this.mWindowManager = (android.view.WindowManager) this.mWindowContext.getSystemService(android.view.WindowManager.class);
        return this.mWindowManager;
    }

    private android.os.Bundle getOptionsForWindowContext(int rootDisplayAreaId) {
        if (rootDisplayAreaId == -1) {
            return null;
        }
        android.os.Bundle options = new android.os.Bundle();
        options.putInt("root_display_area_id", rootDisplayAreaId);
        return options;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleShow(int rootDisplayAreaId) {
        if (this.mClingWindow != null) {
            if (rootDisplayAreaId == this.mWindowContextRootDisplayAreaId) {
                return;
            } else {
                handleHide();
            }
        }
        if (this.mClingWindow != null) {
            android.util.Slog.d(TAG, "mClingWindow was not null ,we should not show it again,just return");
            return;
        }
        this.mClingWindow = new com.android.server.wm.ImmersiveModeConfirmation.ClingWindowView(this.mContext, this.mConfirm);
        android.view.WindowManager.LayoutParams lp = getClingWindowLayoutParams();
        try {
            createWindowManager(rootDisplayAreaId).addView(this.mClingWindow, lp);
        } catch (android.view.WindowManager.InvalidDisplayException e) {
            android.util.Slog.w(TAG, "Fail to show the immersive confirmation window because of " + e);
        }
    }

    private final class H extends android.os.Handler {
        private static final int HIDE = 2;
        private static final int SHOW = 1;

        H(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            if (android.view.ViewRootImpl.CLIENT_TRANSIENT) {
            }
            switch (msg.what) {
                case 1:
                    com.android.server.wm.ImmersiveModeConfirmation.this.handleShow(msg.arg1);
                    break;
                case 2:
                    com.android.server.wm.ImmersiveModeConfirmation.this.handleHide();
                    break;
            }
        }
    }

    void onVrStateChangedLw(boolean enabled) {
        this.mVrModeEnabled = enabled;
        if (this.mVrModeEnabled) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessage(2);
        }
    }

    void onLockTaskModeChangedLw(int lockTaskState) {
        this.mLockTaskState = lockTaskState;
    }
}
