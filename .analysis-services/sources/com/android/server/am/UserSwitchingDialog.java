package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
class UserSwitchingDialog extends android.app.AlertDialog {
    private static final int ANIMATION_TIMEOUT_MS = 1000;
    protected static final boolean DEBUG = true;
    private static final long DIALOG_SHOW_HIDE_ANIMATION_DURATION_MS = 300;
    private static final java.lang.String TAG = "UserSwitchingDialog";
    private static final long TRACE_TAG = 64;
    protected final android.content.Context mContext;
    private final boolean mDisableAnimations;
    private final android.os.Handler mHandler;
    private final boolean mNeedToFreezeScreen;
    protected final android.content.pm.UserInfo mNewUser;
    protected final android.content.pm.UserInfo mOldUser;
    private final java.lang.String mSwitchingFromSystemUserMessage;
    private final java.lang.String mSwitchingToSystemUserMessage;
    private final int mTraceCookie;
    public com.android.server.am.IUserSwitchingDialogExt mUserSwitchingDialogExt;
    private final com.android.server.wm.WindowManagerService mWindowManager;

    UserSwitchingDialog(android.content.Context context, android.content.pm.UserInfo oldUser, android.content.pm.UserInfo newUser, java.lang.String switchingFromSystemUserMessage, java.lang.String switchingToSystemUserMessage, com.android.server.wm.WindowManagerService windowManager) {
        super(context);
        this.mHandler = new android.os.Handler(android.os.Looper.myLooper());
        this.mUserSwitchingDialogExt = (com.android.server.am.IUserSwitchingDialogExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IUserSwitchingDialogExt.class).create();
        this.mContext = context;
        this.mOldUser = oldUser;
        this.mNewUser = newUser;
        this.mSwitchingFromSystemUserMessage = switchingFromSystemUserMessage;
        this.mSwitchingToSystemUserMessage = switchingToSystemUserMessage;
        this.mDisableAnimations = android.os.SystemProperties.getBoolean("debug.usercontroller.disable_user_switching_dialog_animations", false);
        this.mWindowManager = windowManager;
        this.mNeedToFreezeScreen = !this.mDisableAnimations;
        this.mTraceCookie = (oldUser.id * 21473) + newUser.id;
        inflateContent();
        configureWindow();
    }

    private void configureWindow() {
        android.view.Window window = getWindow();
        android.view.WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.privateFlags = 272;
        if (this.mNewUser.id == 888) {
            attrs.privateFlags |= 524288;
        }
        attrs.layoutInDisplayCutoutMode = 3;
        window.setAttributes(attrs);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setType(2010);
        window.setDecorFitsSystemWindows(false);
        window.getInsetsController().hide(android.view.WindowInsets.Type.navigationBars());
    }

    void inflateContent() {
        setCancelable(false);
        android.widget.TextView textView = (android.widget.TextView) android.view.LayoutInflater.from(getContext()).inflate(201917506, (android.view.ViewGroup) null);
        if (textView != null) {
            java.lang.String message = getTextMessage();
            textView.setAccessibilityPaneTitle(message);
            textView.setText(message);
            if (!android.os.UserManager.isDeviceInDemoMode(this.mContext)) {
                textView.setCompoundDrawablesWithIntrinsicBounds((android.graphics.drawable.Drawable) null, getContext().getDrawable(android.R.drawable.ic_popup_sync_2), (android.graphics.drawable.Drawable) null, (android.graphics.drawable.Drawable) null);
            }
            setView(textView);
        }
        android.widget.ImageView imageView = (android.widget.ImageView) findViewById(android.R.id.icon);
        if (imageView != null) {
            imageView.setImageBitmap(getUserIconRounded());
        }
        android.widget.ImageView progressCircular = (android.widget.ImageView) findViewById(android.R.id.perm_money_icon);
        if (progressCircular != null) {
            if (this.mDisableAnimations) {
                progressCircular.setVisibility(8);
                return;
            }
            android.util.TypedValue value = new android.util.TypedValue();
            getContext().getTheme().resolveAttribute(android.R.^attr-private.colorAccentPrimaryVariant, value, true);
            progressCircular.setColorFilter(value.data);
        }
    }

    private android.graphics.Bitmap getUserIconRounded() {
        android.graphics.Bitmap bmp = (android.graphics.Bitmap) com.android.internal.util.ObjectUtils.getOrElse(android.graphics.BitmapFactory.decodeFile(this.mNewUser.iconPath), defaultUserIcon(this.mNewUser.id));
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        android.graphics.Bitmap bmpRounded = android.graphics.Bitmap.createBitmap(w, h, bmp.getConfig());
        android.graphics.Paint paint = new android.graphics.Paint(1);
        paint.setShader(new android.graphics.BitmapShader(bmp, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP));
        new android.graphics.Canvas(bmpRounded).drawRoundRect(new android.graphics.RectF(0.0f, 0.0f, w, h), w / 2.0f, h / 2.0f, paint);
        return bmpRounded;
    }

    private android.graphics.Bitmap defaultUserIcon(int userId) {
        android.content.res.Resources res = getContext().getResources();
        android.graphics.drawable.Drawable icon = com.android.internal.util.UserIcons.getDefaultUserIcon(res, userId, false);
        return com.android.internal.util.UserIcons.convertToBitmapAtUserIconSize(res, icon);
    }

    private java.lang.String getTextMessage() {
        java.lang.String message;
        int i;
        android.content.res.Resources res = getContext().getResources();
        if (android.os.UserManager.isDeviceInDemoMode(this.mContext)) {
            if (this.mOldUser.isDemo()) {
                i = android.R.string.device_state_notification_settings_button;
            } else {
                i = android.R.string.device_state_notification_turn_off_button;
            }
            return res.getString(i);
        }
        if (this.mOldUser.id == 0) {
            message = this.mSwitchingFromSystemUserMessage;
        } else {
            message = this.mNewUser.id == 0 ? this.mSwitchingToSystemUserMessage : null;
        }
        return message != null ? message : this.mUserSwitchingDialogExt.fixSwitchingMessage(android.R.string.time_picker_increment_set_pm_button, this.mNewUser.name, 201588903, res);
    }

    private boolean isUserSetupComplete(android.content.pm.UserInfo user) {
        return android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "user_setup_complete", 0, user.id) == 1;
    }

    @Override // android.app.Dialog
    public void show() {
        asyncTraceBegin("dialog", 0);
        super.show();
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        super.dismiss();
        stopFreezingScreen();
        asyncTraceEnd("dialog", 0);
    }

    public void show(final java.lang.Runnable onShown) {
        android.util.Slog.d(TAG, "show called");
        show();
        startShowAnimation(new java.lang.Runnable() { // from class: com.android.server.am.UserSwitchingDialog$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$show$0(onShown);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$show$0(java.lang.Runnable onShown) {
        startFreezingScreen();
        onShown.run();
    }

    public void dismiss(final java.lang.Runnable onDismissed) {
        android.util.Slog.d(TAG, "dismiss called");
        if (onDismissed == null) {
            dismiss();
        } else {
            startDismissAnimation(new java.lang.Runnable() { // from class: com.android.server.am.UserSwitchingDialog$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$dismiss$1(onDismissed);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dismiss$1(java.lang.Runnable onDismissed) {
        dismiss();
        onDismissed.run();
    }

    private void startFreezingScreen() {
        if (!this.mNeedToFreezeScreen) {
            return;
        }
        long freezingStart = android.os.SystemClock.elapsedRealtime();
        traceBegin("startFreezingScreen");
        this.mWindowManager.startFreezingScreen(0, 0);
        traceEnd("startFreezingScreen");
        long freezingCost = android.os.SystemClock.elapsedRealtime() - freezingStart;
        this.mUserSwitchingDialogExt.startFreezingScreenInStartUser(this.mOldUser.id, this.mNewUser.id);
        this.mUserSwitchingDialogExt.startUserInternalEnter(true, this.mOldUser.id, this.mNewUser.id, -1L, freezingStart, freezingCost, true);
    }

    private void stopFreezingScreen() {
        if (!this.mNeedToFreezeScreen) {
            return;
        }
        traceBegin("stopFreezingScreen");
        this.mWindowManager.stopFreezingScreen();
        traceEnd("stopFreezingScreen");
    }

    private void startShowAnimation(final java.lang.Runnable onAnimationEnd) {
        if (this.mDisableAnimations) {
            onAnimationEnd.run();
        } else {
            asyncTraceBegin("showAnimation", 1);
            startDialogAnimation("show", new android.view.animation.AlphaAnimation(0.0f, 1.0f), new java.lang.Runnable() { // from class: com.android.server.am.UserSwitchingDialog$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startShowAnimation$3(onAnimationEnd);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startShowAnimation$3(final java.lang.Runnable onAnimationEnd) {
        asyncTraceEnd("showAnimation", 1);
        asyncTraceBegin("spinnerAnimation", 2);
        startProgressAnimation(new java.lang.Runnable() { // from class: com.android.server.am.UserSwitchingDialog$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startShowAnimation$2(onAnimationEnd);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startShowAnimation$2(java.lang.Runnable onAnimationEnd) {
        asyncTraceEnd("spinnerAnimation", 2);
        onAnimationEnd.run();
    }

    private void startDismissAnimation(final java.lang.Runnable onAnimationEnd) {
        if (this.mDisableAnimations || this.mNeedToFreezeScreen) {
            onAnimationEnd.run();
        } else {
            asyncTraceBegin("dismissAnimation", 3);
            startDialogAnimation("dismiss", new android.view.animation.AlphaAnimation(1.0f, 0.0f), new java.lang.Runnable() { // from class: com.android.server.am.UserSwitchingDialog$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$startDismissAnimation$4(onAnimationEnd);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startDismissAnimation$4(java.lang.Runnable onAnimationEnd) {
        asyncTraceEnd("dismissAnimation", 3);
        onAnimationEnd.run();
    }

    private void startProgressAnimation(java.lang.Runnable onAnimationEnd) {
        android.graphics.drawable.AnimatedVectorDrawable avd = getSpinnerAVD();
        if (this.mDisableAnimations || avd == null) {
            onAnimationEnd.run();
            return;
        }
        final java.lang.Runnable onAnimationEndWithTimeout = animationWithTimeout("spinner", onAnimationEnd);
        avd.registerAnimationCallback(new android.graphics.drawable.Animatable2.AnimationCallback() { // from class: com.android.server.am.UserSwitchingDialog.1
            @Override // android.graphics.drawable.Animatable2.AnimationCallback
            public void onAnimationEnd(android.graphics.drawable.Drawable drawable) {
                onAnimationEndWithTimeout.run();
            }
        });
        avd.start();
    }

    private android.graphics.drawable.AnimatedVectorDrawable getSpinnerAVD() {
        android.widget.ImageView view = (android.widget.ImageView) findViewById(android.R.id.perm_money_icon);
        if (view != null) {
            android.graphics.drawable.Drawable drawable = view.getDrawable();
            if (drawable instanceof android.graphics.drawable.AnimatedVectorDrawable) {
                return (android.graphics.drawable.AnimatedVectorDrawable) drawable;
            }
            return null;
        }
        return null;
    }

    private void startDialogAnimation(java.lang.String name, android.view.animation.Animation animation, java.lang.Runnable onAnimationEnd) {
        android.view.View view = findViewById(android.R.id.content);
        if (this.mDisableAnimations || view == null) {
            onAnimationEnd.run();
            return;
        }
        final java.lang.Runnable onAnimationEndWithTimeout = animationWithTimeout(name, onAnimationEnd);
        animation.setDuration(DIALOG_SHOW_HIDE_ANIMATION_DURATION_MS);
        animation.setAnimationListener(new android.view.animation.Animation.AnimationListener() { // from class: com.android.server.am.UserSwitchingDialog.2
            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationStart(android.view.animation.Animation animation2) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationEnd(android.view.animation.Animation animation2) {
                onAnimationEndWithTimeout.run();
            }

            @Override // android.view.animation.Animation.AnimationListener
            public void onAnimationRepeat(android.view.animation.Animation animation2) {
            }
        });
        view.startAnimation(animation);
    }

    private java.lang.Runnable animationWithTimeout(final java.lang.String name, final java.lang.Runnable onAnimationEnd) {
        final java.util.concurrent.atomic.AtomicBoolean isFirst = new java.util.concurrent.atomic.AtomicBoolean(true);
        final java.lang.Runnable onAnimationEndOrTimeout = new java.lang.Runnable() { // from class: com.android.server.am.UserSwitchingDialog$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$animationWithTimeout$5(isFirst, onAnimationEnd);
            }
        };
        this.mHandler.postDelayed(new java.lang.Runnable() { // from class: com.android.server.am.UserSwitchingDialog$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.am.UserSwitchingDialog.lambda$animationWithTimeout$6(name, onAnimationEndOrTimeout);
            }
        }, 1000L);
        return onAnimationEndOrTimeout;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animationWithTimeout$5(java.util.concurrent.atomic.AtomicBoolean isFirst, java.lang.Runnable onAnimationEnd) {
        if (isFirst.getAndSet(false)) {
            this.mHandler.removeCallbacksAndMessages(null);
            onAnimationEnd.run();
        }
    }

    static /* synthetic */ void lambda$animationWithTimeout$6(java.lang.String name, java.lang.Runnable onAnimationEndOrTimeout) {
        android.util.Slog.w(TAG, name + " animation not completed in 1000 ms");
        onAnimationEndOrTimeout.run();
    }

    private void asyncTraceBegin(java.lang.String subTag, int subCookie) {
        android.util.Slog.d(TAG, "asyncTraceBegin-" + subTag);
        android.os.Trace.asyncTraceBegin(TRACE_TAG, TAG + subTag, this.mTraceCookie + subCookie);
    }

    private void asyncTraceEnd(java.lang.String subTag, int subCookie) {
        android.os.Trace.asyncTraceEnd(TRACE_TAG, TAG + subTag, this.mTraceCookie + subCookie);
        android.util.Slog.d(TAG, "asyncTraceEnd-" + subTag);
    }

    private void traceBegin(java.lang.String msg) {
        android.util.Slog.d(TAG, "traceBegin-" + msg);
        android.os.Trace.traceBegin(TRACE_TAG, msg);
    }

    private void traceEnd(java.lang.String msg) {
        android.os.Trace.traceEnd(TRACE_TAG);
        android.util.Slog.d(TAG, "traceEnd-" + msg);
    }
}
