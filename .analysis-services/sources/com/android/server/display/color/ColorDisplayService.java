package com.android.server.display.color;

/* JADX INFO: loaded from: classes2.dex */
public final class ColorDisplayService extends com.android.server.SystemService {
    private static final com.android.server.display.color.ColorDisplayService.ColorMatrixEvaluator COLOR_MATRIX_EVALUATOR;
    private static final float[] MATRIX_GRAYSCALE;
    static final float[] MATRIX_IDENTITY = new float[16];
    private static final float[] MATRIX_INVERT_COLOR;
    private static final int MSG_APPLY_DISPLAY_WHITE_BALANCE = 5;
    private static final int MSG_APPLY_GLOBAL_SATURATION = 4;
    private static final int MSG_APPLY_NIGHT_DISPLAY_ANIMATED = 3;
    private static final int MSG_APPLY_NIGHT_DISPLAY_IMMEDIATE = 2;
    private static final int MSG_APPLY_REDUCE_BRIGHT_COLORS = 6;
    private static final int MSG_SET_UP = 1;
    private static final int MSG_USER_CHANGED = 0;
    private static final int NOT_SET = -1;
    static final java.lang.String TAG = "ColorDisplayService";
    private final com.android.server.display.color.AppSaturationController mAppSaturationController;
    private boolean mBootCompleted;
    private final java.lang.Object mCctTintApplierLock;
    private com.android.server.display.color.IColorDisplayServiceExt mCdsExt;
    private android.util.SparseIntArray mColorModeCompositionColorSpaces;
    private android.database.ContentObserver mContentObserver;
    private int mCurrentUser;
    private final com.android.server.display.feature.DisplayManagerFlags mDisplayManagerFlags;
    private com.android.server.display.color.ColorDisplayService.DisplayWhiteBalanceListener mDisplayWhiteBalanceListener;
    final com.android.server.display.color.DisplayWhiteBalanceTintController mDisplayWhiteBalanceTintController;
    private final com.android.server.display.color.TintController mGlobalSaturationTintController;
    final android.os.Handler mHandler;
    private com.android.server.display.color.ColorDisplayService.NightDisplayAutoMode mNightDisplayAutoMode;
    private final com.android.server.display.color.ColorDisplayService.NightDisplayTintController mNightDisplayTintController;
    private com.android.server.display.color.ColorDisplayService.ReduceBrightColorsListener mReduceBrightColorsListener;
    private final com.android.server.display.color.ReduceBrightColorsTintController mReduceBrightColorsTintController;
    private int mTearDownUser;
    private android.database.ContentObserver mUserSetupObserver;

    public interface ColorTransformController {
        void applyAppSaturation(float[] fArr, float[] fArr2);
    }

    public interface DisplayWhiteBalanceListener {
        void onDisplayWhiteBalanceStatusChanged(boolean z);
    }

    public interface ReduceBrightColorsListener {
        void onReduceBrightColorsActivationChanged(boolean z, boolean z2);

        void onReduceBrightColorsStrengthChanged(int i);
    }

    static {
        android.opengl.Matrix.setIdentityM(MATRIX_IDENTITY, 0);
        COLOR_MATRIX_EVALUATOR = new com.android.server.display.color.ColorDisplayService.ColorMatrixEvaluator();
        MATRIX_GRAYSCALE = new float[]{0.2126f, 0.2126f, 0.2126f, 0.0f, 0.7152f, 0.7152f, 0.7152f, 0.0f, 0.0722f, 0.0722f, 0.0722f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
        MATRIX_INVERT_COLOR = new float[]{0.402f, -0.598f, -0.599f, 0.0f, -1.174f, -0.174f, -1.175f, 0.0f, -0.228f, -0.228f, 0.772f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ColorDisplayService(android.content.Context context) {
        super(context);
        this.mDisplayManagerFlags = new com.android.server.display.feature.DisplayManagerFlags();
        this.mDisplayWhiteBalanceTintController = new com.android.server.display.color.DisplayWhiteBalanceTintController((android.hardware.display.DisplayManagerInternal) com.android.server.LocalServices.getService(android.hardware.display.DisplayManagerInternal.class), this.mDisplayManagerFlags);
        this.mNightDisplayTintController = new com.android.server.display.color.ColorDisplayService.NightDisplayTintController();
        this.mGlobalSaturationTintController = new com.android.server.display.color.GlobalSaturationTintController();
        this.mReduceBrightColorsTintController = new com.android.server.display.color.ReduceBrightColorsTintController();
        this.mAppSaturationController = new com.android.server.display.color.AppSaturationController();
        this.mCurrentUser = -10000;
        this.mTearDownUser = this.mCurrentUser;
        this.mCdsExt = (com.android.server.display.color.IColorDisplayServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.color.IColorDisplayServiceExt.class).base(this).create();
        this.mColorModeCompositionColorSpaces = null;
        this.mCctTintApplierLock = new java.lang.Object();
        this.mHandler = new com.android.server.display.color.ColorDisplayService.TintHandler(com.android.server.DisplayThread.get().getLooper());
        this.mCdsExt.init(context);
    }

    public void setColorModeExt(int mode) {
        android.util.Slog.d(TAG, "setColorModeExt mode=" + mode);
        setColorModeInternal(mode);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("color_display", new com.android.server.display.color.ColorDisplayService.BinderService());
        publishLocalService(com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal.class, new com.android.server.display.color.ColorDisplayService.ColorDisplayServiceInternal());
        publishLocalService(com.android.server.display.color.DisplayTransformManager.class, new com.android.server.display.color.DisplayTransformManager());
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase >= 1000) {
            this.mBootCompleted = true;
            if (this.mCurrentUser != -10000 && this.mUserSetupObserver == null) {
                this.mHandler.sendEmptyMessage(1);
            }
            this.mCdsExt.onBootComplete();
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStarting(com.android.server.SystemService.TargetUser user) {
        if (this.mCurrentUser == -10000) {
            android.os.Message message = this.mHandler.obtainMessage(0);
            message.arg1 = user.getUserIdentifier();
            this.mTearDownUser = this.mCurrentUser;
            this.mHandler.sendMessage(message);
        }
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        android.os.Message message = this.mHandler.obtainMessage(0);
        message.arg1 = to.getUserIdentifier();
        this.mTearDownUser = this.mCurrentUser;
        this.mHandler.sendMessage(message);
    }

    @Override // com.android.server.SystemService
    public void onUserStopping(com.android.server.SystemService.TargetUser user) {
        if (this.mCurrentUser == user.getUserIdentifier()) {
            android.os.Message message = this.mHandler.obtainMessage(0);
            message.arg1 = -10000;
            this.mTearDownUser = this.mCurrentUser;
            this.mHandler.sendMessage(message);
        }
    }

    void onUserChanged(int userHandle) {
        if (userHandle == -10000 && this.mCurrentUser != this.mTearDownUser) {
            android.util.Slog.e(TAG, "Stopping failed! Current user is " + this.mCurrentUser + ", not " + this.mTearDownUser);
            return;
        }
        final android.content.ContentResolver cr = getContext().getContentResolver();
        if (this.mCurrentUser != -10000) {
            if (this.mUserSetupObserver != null) {
                cr.unregisterContentObserver(this.mUserSetupObserver);
                this.mUserSetupObserver = null;
            } else if (this.mBootCompleted) {
                tearDown();
            }
        }
        this.mCurrentUser = userHandle;
        if (this.mCurrentUser != -10000) {
            if (!isUserSetupCompleted(cr, this.mCurrentUser)) {
                this.mUserSetupObserver = new android.database.ContentObserver(this.mHandler) { // from class: com.android.server.display.color.ColorDisplayService.1
                    @Override // android.database.ContentObserver
                    public void onChange(boolean selfChange, android.net.Uri uri) {
                        if (com.android.server.display.color.ColorDisplayService.isUserSetupCompleted(cr, com.android.server.display.color.ColorDisplayService.this.mCurrentUser)) {
                            cr.unregisterContentObserver(this);
                            com.android.server.display.color.ColorDisplayService.this.mUserSetupObserver = null;
                            if (com.android.server.display.color.ColorDisplayService.this.mBootCompleted) {
                                com.android.server.display.color.ColorDisplayService.this.setUp();
                            }
                        }
                    }
                };
                cr.registerContentObserver(android.provider.Settings.Secure.getUriFor("user_setup_complete"), false, this.mUserSetupObserver, this.mCurrentUser);
            } else if (this.mBootCompleted) {
                setUp();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isUserSetupCompleted(android.content.ContentResolver cr, int userHandle) {
        return android.provider.Settings.Secure.getIntForUser(cr, "user_setup_complete", 0, userHandle) == 1;
    }

    private void setUpDisplayCompositionColorSpaces(android.content.res.Resources res) {
        int[] compSpaces;
        this.mColorModeCompositionColorSpaces = null;
        int[] colorModes = res.getIntArray(android.R.array.config_disabledDreamComponents);
        if (colorModes == null || (compSpaces = res.getIntArray(android.R.array.config_disabledUntilUsedPreinstalledImes)) == null) {
            return;
        }
        if (colorModes.length != compSpaces.length) {
            android.util.Slog.e(TAG, "Number of composition color spaces doesn't match specified color modes");
            return;
        }
        this.mColorModeCompositionColorSpaces = new android.util.SparseIntArray(colorModes.length);
        for (int i = 0; i < colorModes.length; i++) {
            this.mColorModeCompositionColorSpaces.put(colorModes[i], compSpaces[i]);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setUp() {
        android.util.Slog.d(TAG, "setUp: currentUser=" + this.mCurrentUser);
        if (this.mContentObserver == null) {
            this.mContentObserver = new android.database.ContentObserver(this.mHandler) { // from class: com.android.server.display.color.ColorDisplayService.2
                /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
                /* JADX WARN: Removed duplicated region for block: B:49:0x00aa  */
                @Override // android.database.ContentObserver
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public void onChange(boolean r5, android.net.Uri r6) {
                    /*
                        Method dump skipped, instruction units count: 476
                        To view this dump add '--comments-level debug' option
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.color.ColorDisplayService.AnonymousClass2.onChange(boolean, android.net.Uri):void");
                }
            };
        }
        android.content.ContentResolver cr = getContext().getContentResolver();
        cr.registerContentObserver(android.provider.Settings.Secure.getUriFor("night_display_activated"), false, this.mContentObserver, this.mCurrentUser);
        cr.registerContentObserver(android.provider.Settings.Secure.getUriFor("night_display_color_temperature"), false, this.mContentObserver, this.mCurrentUser);
        cr.registerContentObserver(android.provider.Settings.Secure.getUriFor("night_display_auto_mode"), false, this.mContentObserver, this.mCurrentUser);
        cr.registerContentObserver(android.provider.Settings.Secure.getUriFor("night_display_custom_start_time"), false, this.mContentObserver, this.mCurrentUser);
        cr.registerContentObserver(android.provider.Settings.Secure.getUriFor("night_display_custom_end_time"), false, this.mContentObserver, this.mCurrentUser);
        cr.registerContentObserver(android.provider.Settings.System.getUriFor("display_color_mode"), false, this.mContentObserver, this.mCurrentUser);
        cr.registerContentObserver(android.provider.Settings.Secure.getUriFor("accessibility_display_inversion_enabled"), false, this.mContentObserver, this.mCurrentUser);
        cr.registerContentObserver(android.provider.Settings.Secure.getUriFor("accessibility_display_daltonizer_enabled"), false, this.mContentObserver, this.mCurrentUser);
        cr.registerContentObserver(android.provider.Settings.Secure.getUriFor("accessibility_display_daltonizer"), false, this.mContentObserver, this.mCurrentUser);
        cr.registerContentObserver(android.provider.Settings.Secure.getUriFor("display_white_balance_enabled"), false, this.mContentObserver, this.mCurrentUser);
        cr.registerContentObserver(android.provider.Settings.Secure.getUriFor("reduce_bright_colors_activated"), false, this.mContentObserver, this.mCurrentUser);
        cr.registerContentObserver(android.provider.Settings.Secure.getUriFor("reduce_bright_colors_level"), false, this.mContentObserver, this.mCurrentUser);
        if (com.android.server.accessibility.Flags.enableColorCorrectionSaturation()) {
            cr.registerContentObserver(android.provider.Settings.Secure.getUriFor("accessibility_display_daltonizer_saturation_level"), false, this.mContentObserver, this.mCurrentUser);
        }
        onAccessibilityInversionChanged();
        onAccessibilityDaltonizerChanged();
        setUpDisplayCompositionColorSpaces(getContext().getResources());
        onDisplayColorModeChanged(getColorModeInternal());
        com.android.server.display.color.DisplayTransformManager dtm = (com.android.server.display.color.DisplayTransformManager) getLocalService(com.android.server.display.color.DisplayTransformManager.class);
        if (this.mNightDisplayTintController.isAvailable(getContext())) {
            this.mNightDisplayTintController.setActivated(null);
            this.mNightDisplayTintController.setUp(getContext(), dtm.needsLinearColorMatrix());
            this.mNightDisplayTintController.setMatrix(this.mNightDisplayTintController.getColorTemperatureSetting());
            onNightDisplayAutoModeChanged(getNightDisplayAutoModeInternal());
            if (this.mNightDisplayTintController.isActivatedStateNotSet()) {
                this.mNightDisplayTintController.setActivated(java.lang.Boolean.valueOf(this.mNightDisplayTintController.isActivatedSetting()));
            }
        }
        if (this.mDisplayWhiteBalanceTintController.isAvailable(getContext())) {
            this.mDisplayWhiteBalanceTintController.setUp(getContext(), true);
            updateDisplayWhiteBalanceStatus();
        }
        if (this.mReduceBrightColorsTintController.isAvailable(getContext())) {
            this.mReduceBrightColorsTintController.setUp(getContext(), dtm.needsLinearColorMatrix());
            onReduceBrightColorsStrengthLevelChanged();
            boolean reset = resetReduceBrightColors();
            if (!reset) {
                onReduceBrightColorsActivationChanged(false);
                this.mHandler.sendEmptyMessage(6);
            }
        }
        this.mCdsExt.onSetUp(this.mCurrentUser);
    }

    private void tearDown() {
        android.util.Slog.d(TAG, "tearDown: currentUser=" + this.mCurrentUser);
        if (this.mContentObserver != null) {
            getContext().getContentResolver().unregisterContentObserver(this.mContentObserver);
        }
        if (this.mNightDisplayTintController.isAvailable(getContext())) {
            if (this.mNightDisplayAutoMode != null) {
                this.mNightDisplayAutoMode.onStop();
                this.mNightDisplayAutoMode = null;
            }
            this.mNightDisplayTintController.endAnimator();
        }
        if (this.mDisplayWhiteBalanceTintController.isAvailable(getContext())) {
            this.mDisplayWhiteBalanceTintController.endAnimator();
        }
        if (this.mGlobalSaturationTintController.isAvailable(getContext())) {
            this.mGlobalSaturationTintController.setActivated(null);
        }
        if (this.mReduceBrightColorsTintController.isAvailable(getContext())) {
            this.mReduceBrightColorsTintController.setActivated(null);
        }
        this.mCdsExt.onTearDown();
    }

    void cancelAllAnimators() {
        this.mNightDisplayTintController.cancelAnimator();
        this.mGlobalSaturationTintController.cancelAnimator();
        this.mReduceBrightColorsTintController.cancelAnimator();
        this.mDisplayWhiteBalanceTintController.cancelAnimator();
    }

    private boolean resetReduceBrightColors() {
        if (this.mCurrentUser == -10000) {
            return false;
        }
        boolean isSettingActivated = android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "reduce_bright_colors_activated", 0, this.mCurrentUser) == 1;
        boolean shouldResetOnReboot = android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "reduce_bright_colors_persist_across_reboots", 0, this.mCurrentUser) == 0;
        if (isSettingActivated && this.mReduceBrightColorsTintController.isActivatedStateNotSet() && shouldResetOnReboot) {
            return android.provider.Settings.Secure.putIntForUser(getContext().getContentResolver(), "reduce_bright_colors_activated", 0, this.mCurrentUser);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNightDisplayAutoModeChanged(int autoMode) {
        android.util.Slog.d(TAG, "onNightDisplayAutoModeChanged: autoMode=" + autoMode);
        if (this.mNightDisplayAutoMode != null) {
            this.mNightDisplayAutoMode.onStop();
            this.mNightDisplayAutoMode = null;
        }
        if (autoMode == 1) {
            this.mNightDisplayAutoMode = new com.android.server.display.color.ColorDisplayService.CustomNightDisplayAutoMode();
        } else if (autoMode == 2) {
            this.mNightDisplayAutoMode = new com.android.server.display.color.ColorDisplayService.TwilightNightDisplayAutoMode();
        }
        if (this.mNightDisplayAutoMode != null) {
            this.mNightDisplayAutoMode.onStart();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNightDisplayCustomStartTimeChanged(java.time.LocalTime startTime) {
        android.util.Slog.d(TAG, "onNightDisplayCustomStartTimeChanged: startTime=" + startTime);
        if (this.mNightDisplayAutoMode != null) {
            this.mNightDisplayAutoMode.onCustomStartTimeChanged(startTime);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onNightDisplayCustomEndTimeChanged(java.time.LocalTime endTime) {
        android.util.Slog.d(TAG, "onNightDisplayCustomEndTimeChanged: endTime=" + endTime);
        if (this.mNightDisplayAutoMode != null) {
            this.mNightDisplayAutoMode.onCustomEndTimeChanged(endTime);
        }
    }

    private int getCompositionColorSpace(int mode) {
        if (this.mColorModeCompositionColorSpaces == null) {
            return -1;
        }
        return this.mColorModeCompositionColorSpaces.get(mode, -1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDisplayColorModeChanged(int mode) {
        if (mode == -1) {
            return;
        }
        this.mNightDisplayTintController.cancelAnimator();
        this.mDisplayWhiteBalanceTintController.cancelAnimator();
        if (this.mNightDisplayTintController.isAvailable(getContext())) {
            com.android.server.display.color.DisplayTransformManager dtm = (com.android.server.display.color.DisplayTransformManager) getLocalService(com.android.server.display.color.DisplayTransformManager.class);
            this.mNightDisplayTintController.setUp(getContext(), dtm.needsLinearColorMatrix(mode));
            this.mNightDisplayTintController.setMatrix(this.mNightDisplayTintController.getColorTemperatureSetting());
        }
        com.android.server.display.color.DisplayTransformManager dtm2 = (com.android.server.display.color.DisplayTransformManager) getLocalService(com.android.server.display.color.DisplayTransformManager.class);
        dtm2.setColorMode(mode, this.mNightDisplayTintController.getMatrix(), getCompositionColorSpace(mode));
        if (this.mDisplayWhiteBalanceTintController.isAvailable(getContext())) {
            updateDisplayWhiteBalanceStatus();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAccessibilityActivated() {
        onDisplayColorModeChanged(getColorModeInternal());
    }

    private boolean isAccessiblityDaltonizerEnabled() {
        return android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "accessibility_display_daltonizer_enabled", 0, this.mCurrentUser) != 0;
    }

    private boolean isAccessiblityInversionEnabled() {
        return android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "accessibility_display_inversion_enabled", 0, this.mCurrentUser) != 0;
    }

    private boolean isAccessibilityEnabled() {
        return isAccessiblityDaltonizerEnabled() || isAccessiblityInversionEnabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAccessibilityDaltonizerChanged() {
        int daltonizerMode;
        if (this.mCurrentUser == -10000) {
            return;
        }
        android.content.ContentResolver contentResolver = getContext().getContentResolver();
        if (isAccessiblityDaltonizerEnabled()) {
            daltonizerMode = android.provider.Settings.Secure.getIntForUser(contentResolver, "accessibility_display_daltonizer", 12, this.mCurrentUser);
        } else {
            daltonizerMode = -1;
        }
        int saturation = -1;
        if (com.android.server.accessibility.Flags.enableColorCorrectionSaturation()) {
            saturation = android.provider.Settings.Secure.getIntForUser(contentResolver, "accessibility_display_daltonizer_saturation_level", -1, this.mCurrentUser);
        }
        com.android.server.display.color.DisplayTransformManager dtm = (com.android.server.display.color.DisplayTransformManager) getLocalService(com.android.server.display.color.DisplayTransformManager.class);
        if (daltonizerMode == 0) {
            dtm.setColorMatrix(200, MATRIX_GRAYSCALE);
            dtm.setDaltonizerMode(-1, saturation);
        } else {
            dtm.setColorMatrix(200, null);
            dtm.setDaltonizerMode(daltonizerMode, saturation);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAccessibilityInversionChanged() {
        if (this.mCurrentUser == -10000) {
            return;
        }
        com.android.server.display.color.DisplayTransformManager dtm = (com.android.server.display.color.DisplayTransformManager) getLocalService(com.android.server.display.color.DisplayTransformManager.class);
        dtm.setColorMatrix(300, isAccessiblityInversionEnabled() ? MATRIX_INVERT_COLOR : null);
        this.mCdsExt.applyAccessiblityInversionState(isAccessiblityInversionEnabled());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReduceBrightColorsActivationChanged(boolean userInitiated) {
        if (this.mCurrentUser == -10000) {
            return;
        }
        boolean activated = android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "reduce_bright_colors_activated", 0, this.mCurrentUser) == 1;
        this.mReduceBrightColorsTintController.setActivated(java.lang.Boolean.valueOf(activated));
        if (this.mReduceBrightColorsListener != null) {
            this.mReduceBrightColorsListener.onReduceBrightColorsActivationChanged(activated, userInitiated);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onReduceBrightColorsStrengthLevelChanged() {
        if (this.mCurrentUser == -10000) {
            return;
        }
        int strength = android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "reduce_bright_colors_level", -1, this.mCurrentUser);
        if (strength == -1) {
            strength = getContext().getResources().getInteger(android.R.integer.config_num_physical_slots);
        }
        this.mReduceBrightColorsTintController.setMatrix(strength);
        if (this.mReduceBrightColorsListener != null) {
            this.mReduceBrightColorsListener.onReduceBrightColorsStrengthChanged(strength);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyTint(final com.android.server.display.color.TintController tintController, boolean immediate) {
        tintController.cancelAnimator();
        final com.android.server.display.color.DisplayTransformManager dtm = (com.android.server.display.color.DisplayTransformManager) getLocalService(com.android.server.display.color.DisplayTransformManager.class);
        float[] from = dtm.getColorMatrix(tintController.getLevel());
        final float[] to = tintController.getMatrix();
        if (immediate) {
            dtm.setColorMatrix(tintController.getLevel(), to);
            return;
        }
        com.android.server.display.color.ColorDisplayService.TintValueAnimator valueAnimator = com.android.server.display.color.ColorDisplayService.TintValueAnimator.ofMatrix(COLOR_MATRIX_EVALUATOR, from == null ? MATRIX_IDENTITY : from, to);
        tintController.setAnimator(valueAnimator);
        valueAnimator.setDuration(tintController.getTransitionDurationMilliseconds());
        valueAnimator.setInterpolator(android.view.animation.AnimationUtils.loadInterpolator(getContext(), android.R.interpolator.fast_out_slow_in));
        valueAnimator.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() { // from class: com.android.server.display.color.ColorDisplayService$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(android.animation.ValueAnimator valueAnimator2) {
                this.f$0.lambda$applyTint$0(tintController, dtm, valueAnimator2);
            }
        });
        valueAnimator.addListener(new android.animation.AnimatorListenerAdapter() { // from class: com.android.server.display.color.ColorDisplayService.3
            private boolean mIsCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(android.animation.Animator animator) {
                this.mIsCancelled = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(android.animation.Animator animator) {
                com.android.server.display.color.ColorDisplayService.TintValueAnimator t = (com.android.server.display.color.ColorDisplayService.TintValueAnimator) animator;
                android.util.Slog.d(com.android.server.display.color.ColorDisplayService.TAG, tintController.getClass().getSimpleName() + " Animation cancelled: " + this.mIsCancelled + " to matrix: " + com.android.server.display.color.TintController.matrixToString(to, 16) + " min matrix coefficients: " + com.android.server.display.color.TintController.matrixToString(t.getMin(), 16) + " max matrix coefficients: " + com.android.server.display.color.TintController.matrixToString(t.getMax(), 16));
                if (!this.mIsCancelled) {
                    com.android.server.display.color.ColorDisplayService.this.mCdsExt.setColorMatrix(tintController.getLevel(), to, dtm);
                }
                tintController.setAnimator(null);
            }
        });
        valueAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyTint$0(com.android.server.display.color.TintController tintController, com.android.server.display.color.DisplayTransformManager dtm, android.animation.ValueAnimator animator) {
        float[] value = (float[]) animator.getAnimatedValue();
        this.mCdsExt.setColorMatrix(tintController.getLevel(), value, dtm);
        ((com.android.server.display.color.ColorDisplayService.TintValueAnimator) animator).updateMinMaxComponents();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void applyTintByCct(final com.android.server.display.color.ColorTemperatureTintController tintController, boolean immediate) {
        synchronized (this.mCctTintApplierLock) {
            tintController.cancelAnimator();
            final com.android.server.display.color.DisplayTransformManager dtm = (com.android.server.display.color.DisplayTransformManager) getLocalService(com.android.server.display.color.DisplayTransformManager.class);
            final int from = tintController.getAppliedCct();
            final int to = tintController.isActivated() ? tintController.getTargetCct() : tintController.getDisabledCct();
            if (immediate) {
                android.util.Slog.d(TAG, tintController.getClass().getSimpleName() + " applied immediately: toCct=" + to + " fromCct=" + from);
                dtm.setColorMatrix(tintController.getLevel(), tintController.computeMatrixForCct(to));
                tintController.setAppliedCct(to);
            } else {
                long duration = tintController.getTransitionDurationMilliseconds(to > from);
                android.util.Slog.d(TAG, tintController.getClass().getSimpleName() + " animation started: toCct=" + to + " fromCct=" + from + " with duration=" + duration);
                android.animation.ValueAnimator valueAnimator = android.animation.ValueAnimator.ofInt(from, to);
                tintController.setAnimator(valueAnimator);
                com.android.server.display.color.CctEvaluator evaluator = tintController.getEvaluator();
                if (evaluator != null) {
                    valueAnimator.setEvaluator(evaluator);
                }
                valueAnimator.setDuration(duration);
                valueAnimator.setInterpolator(android.view.animation.AnimationUtils.loadInterpolator(getContext(), android.R.interpolator.linear));
                valueAnimator.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() { // from class: com.android.server.display.color.ColorDisplayService$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(android.animation.ValueAnimator valueAnimator2) {
                        this.f$0.lambda$applyTintByCct$1(tintController, dtm, valueAnimator2);
                    }
                });
                valueAnimator.addListener(new android.animation.AnimatorListenerAdapter() { // from class: com.android.server.display.color.ColorDisplayService.4
                    private boolean mIsCancelled;

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(android.animation.Animator animator) {
                        android.util.Slog.d(com.android.server.display.color.ColorDisplayService.TAG, tintController.getClass().getSimpleName() + " animation cancelled");
                        this.mIsCancelled = true;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(android.animation.Animator animator) {
                        synchronized (com.android.server.display.color.ColorDisplayService.this.mCctTintApplierLock) {
                            android.util.Slog.d(com.android.server.display.color.ColorDisplayService.TAG, tintController.getClass().getSimpleName() + " animation ended: wasCancelled=" + this.mIsCancelled + " toCct=" + to + " fromCct=" + from);
                            if (!this.mIsCancelled) {
                                dtm.setColorMatrix(tintController.getLevel(), tintController.computeMatrixForCct(to));
                                tintController.setAppliedCct(to);
                            }
                            tintController.setAnimator(null);
                        }
                    }
                });
                valueAnimator.start();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyTintByCct$1(com.android.server.display.color.ColorTemperatureTintController tintController, com.android.server.display.color.DisplayTransformManager dtm, android.animation.ValueAnimator animator) {
        synchronized (this.mCctTintApplierLock) {
            int value = ((java.lang.Integer) animator.getAnimatedValue()).intValue();
            if (value != tintController.getAppliedCct()) {
                dtm.setColorMatrix(tintController.getLevel(), tintController.computeMatrixForCct(value));
                tintController.setAppliedCct(value);
            }
        }
    }

    static java.time.LocalDateTime getDateTimeBefore(java.time.LocalTime localTime, java.time.LocalDateTime compareTime) {
        java.time.LocalDateTime ldt = java.time.LocalDateTime.of(compareTime.getYear(), compareTime.getMonth(), compareTime.getDayOfMonth(), localTime.getHour(), localTime.getMinute());
        return ldt.isAfter(compareTime) ? ldt.minusDays(1L) : ldt;
    }

    static java.time.LocalDateTime getDateTimeAfter(java.time.LocalTime localTime, java.time.LocalDateTime compareTime) {
        java.time.LocalDateTime ldt = java.time.LocalDateTime.of(compareTime.getYear(), compareTime.getMonth(), compareTime.getDayOfMonth(), localTime.getHour(), localTime.getMinute());
        return ldt.isBefore(compareTime) ? ldt.plusDays(1L) : ldt;
    }

    void updateDisplayWhiteBalanceStatus() {
        boolean oldActivated = this.mDisplayWhiteBalanceTintController.isActivated();
        com.android.server.display.color.DisplayTransformManager dtm = (com.android.server.display.color.DisplayTransformManager) getLocalService(com.android.server.display.color.DisplayTransformManager.class);
        this.mDisplayWhiteBalanceTintController.setActivated(java.lang.Boolean.valueOf(isDisplayWhiteBalanceSettingEnabled() && !this.mNightDisplayTintController.isActivated() && !isAccessibilityEnabled() && dtm.needsLinearColorMatrix() && this.mDisplayWhiteBalanceTintController.isAllowed()));
        boolean activated = this.mDisplayWhiteBalanceTintController.isActivated();
        if (this.mDisplayWhiteBalanceListener != null && oldActivated != activated) {
            this.mDisplayWhiteBalanceListener.onDisplayWhiteBalanceStatusChanged(activated);
        }
        if (oldActivated && !activated) {
            this.mHandler.sendEmptyMessage(5);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setDisplayWhiteBalanceSettingEnabled(boolean z) {
        if (this.mCurrentUser == -10000) {
            return false;
        }
        return android.provider.Settings.Secure.putIntForUser(getContext().getContentResolver(), "display_white_balance_enabled", z ? 1 : 0, this.mCurrentUser);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDisplayWhiteBalanceSettingEnabled() {
        if (this.mCurrentUser == -10000) {
            return false;
        }
        return android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "display_white_balance_enabled", getContext().getResources().getBoolean(android.R.bool.config_displayColorFadeDisabled) ? 1 : 0, this.mCurrentUser) == 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setReduceBrightColorsActivatedInternal(boolean z) {
        if (this.mCurrentUser == -10000) {
            return false;
        }
        return android.provider.Settings.Secure.putIntForUser(getContext().getContentResolver(), "reduce_bright_colors_activated", z ? 1 : 0, this.mCurrentUser);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setReduceBrightColorsStrengthInternal(int strength) {
        if (this.mCurrentUser == -10000) {
            return false;
        }
        return android.provider.Settings.Secure.putIntForUser(getContext().getContentResolver(), "reduce_bright_colors_level", strength, this.mCurrentUser);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDeviceColorManagedInternal() {
        com.android.server.display.color.DisplayTransformManager dtm = (com.android.server.display.color.DisplayTransformManager) getLocalService(com.android.server.display.color.DisplayTransformManager.class);
        return dtm.isDeviceColorManaged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getTransformCapabilitiesInternal() {
        int availabilityFlags = 0;
        if (android.view.SurfaceControl.getProtectedContentSupport()) {
            availabilityFlags = 0 | 1;
        }
        android.content.res.Resources res = getContext().getResources();
        if (res.getBoolean(android.R.bool.config_profcollectReportUploaderEnabled)) {
            availabilityFlags |= 2;
        }
        if (res.getBoolean(android.R.bool.config_pulseOnNotificationsAvailable)) {
            return availabilityFlags | 4;
        }
        return availabilityFlags;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setNightDisplayAutoModeInternal(int autoMode) {
        if (getNightDisplayAutoModeInternal() != autoMode) {
            android.provider.Settings.Secure.putStringForUser(getContext().getContentResolver(), "night_display_last_activated_time", null, this.mCurrentUser);
        }
        return android.provider.Settings.Secure.putIntForUser(getContext().getContentResolver(), "night_display_auto_mode", autoMode, this.mCurrentUser);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNightDisplayAutoModeInternal() {
        int autoMode = getNightDisplayAutoModeRawInternal();
        if (autoMode == -1) {
            autoMode = getContext().getResources().getInteger(android.R.integer.config_defaultBinderHeavyHitterWatcherBatchSize);
        }
        if (autoMode != 0 && autoMode != 1 && autoMode != 2) {
            android.util.Slog.e(TAG, "Invalid autoMode: " + autoMode);
            return 0;
        }
        return autoMode;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNightDisplayAutoModeRawInternal() {
        if (this.mCurrentUser == -10000) {
            return -1;
        }
        return android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "night_display_auto_mode", -1, this.mCurrentUser);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.hardware.display.Time getNightDisplayCustomStartTimeInternal() {
        int startTimeValue = android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "night_display_custom_start_time", -1, this.mCurrentUser);
        if (startTimeValue == -1) {
            startTimeValue = getContext().getResources().getInteger(android.R.integer.config_defaultHapticFeedbackIntensity);
        }
        return new android.hardware.display.Time(java.time.LocalTime.ofSecondOfDay(startTimeValue / 1000));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setNightDisplayCustomStartTimeInternal(android.hardware.display.Time startTime) {
        return android.provider.Settings.Secure.putIntForUser(getContext().getContentResolver(), "night_display_custom_start_time", startTime.getLocalTime().toSecondOfDay() * 1000, this.mCurrentUser);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.hardware.display.Time getNightDisplayCustomEndTimeInternal() {
        int endTimeValue = android.provider.Settings.Secure.getIntForUser(getContext().getContentResolver(), "night_display_custom_end_time", -1, this.mCurrentUser);
        if (endTimeValue == -1) {
            endTimeValue = getContext().getResources().getInteger(android.R.integer.config_defaultDisplayDefaultColorMode);
        }
        return new android.hardware.display.Time(java.time.LocalTime.ofSecondOfDay(endTimeValue / 1000));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean setNightDisplayCustomEndTimeInternal(android.hardware.display.Time endTime) {
        return android.provider.Settings.Secure.putIntForUser(getContext().getContentResolver(), "night_display_custom_end_time", endTime.getLocalTime().toSecondOfDay() * 1000, this.mCurrentUser);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.time.LocalDateTime getNightDisplayLastActivatedTimeSetting() {
        android.content.ContentResolver cr = getContext().getContentResolver();
        java.lang.String lastActivatedTime = android.provider.Settings.Secure.getStringForUser(cr, "night_display_last_activated_time", getContext().getUserId());
        if (lastActivatedTime != null) {
            try {
                return java.time.LocalDateTime.parse(lastActivatedTime);
            } catch (java.time.format.DateTimeParseException e) {
                try {
                    return java.time.LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(java.lang.Long.parseLong(lastActivatedTime)), java.time.ZoneId.systemDefault());
                } catch (java.lang.NumberFormatException | java.time.DateTimeException e2) {
                }
            }
        }
        return java.time.LocalDateTime.MIN;
    }

    void setSaturationLevelInternal(int saturationLevel) {
        android.os.Message message = this.mHandler.obtainMessage(4);
        message.arg1 = saturationLevel;
        this.mHandler.sendMessage(message);
    }

    boolean setAppSaturationLevelInternal(java.lang.String callingPackageName, java.lang.String affectedPackageName, int saturationLevel) {
        return this.mAppSaturationController.setSaturationLevel(callingPackageName, affectedPackageName, this.mCurrentUser, saturationLevel);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setColorModeInternal(int colorMode) {
        if (!isColorModeAvailable(colorMode)) {
            throw new java.lang.IllegalArgumentException("Invalid colorMode: " + colorMode);
        }
        try {
            android.provider.Settings.System.putIntForUser(getContext().getContentResolver(), "display_color_mode", colorMode, android.app.ActivityManager.getCurrentUser());
        } catch (java.lang.Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getColorModeInternal() {
        int a11yColorMode;
        android.content.ContentResolver cr = getContext().getContentResolver();
        if (isAccessibilityEnabled() && (a11yColorMode = getContext().getResources().getInteger(android.R.integer.bugreport_state_unknown)) >= 0) {
            return a11yColorMode;
        }
        int colorMode = android.provider.Settings.System.getIntForUser(cr, "display_color_mode", -1, this.mCurrentUser);
        if (colorMode == -1) {
            colorMode = getCurrentColorModeFromSystemProperties();
        }
        if (!isColorModeAvailable(colorMode)) {
            int[] mappedColorModes = getContext().getResources().getIntArray(android.R.array.config_lteDbmThresholds);
            if (colorMode != -1 && mappedColorModes.length > colorMode && isColorModeAvailable(mappedColorModes[colorMode])) {
                return mappedColorModes[colorMode];
            }
            int[] availableColorModes = getContext().getResources().getIntArray(android.R.array.config_autoTimeSourcesPriority);
            if (availableColorModes.length > 0) {
                return availableColorModes[0];
            }
            return -1;
        }
        return colorMode;
    }

    private int getCurrentColorModeFromSystemProperties() {
        int displayColorSetting = android.os.SystemProperties.getInt("persist.sys.sf.native_mode", 0);
        if (displayColorSetting == 0) {
            return "1.0".equals(android.os.SystemProperties.get("persist.sys.sf.color_saturation", "1.0")) ? 0 : 1;
        }
        if (displayColorSetting == 1) {
            return 2;
        }
        if (displayColorSetting == 2) {
            return 3;
        }
        if (displayColorSetting >= 256 && displayColorSetting <= 511) {
            return displayColorSetting;
        }
        return -1;
    }

    private boolean isColorModeAvailable(int colorMode) {
        int[] availableColorModes = getContext().getResources().getIntArray(android.R.array.config_autoTimeSourcesPriority);
        if (availableColorModes != null) {
            for (int mode : availableColorModes) {
                if (mode == colorMode) {
                    return true;
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dumpInternal(java.io.PrintWriter pw) {
        pw.println("COLOR DISPLAY MANAGER dumpsys (color_display)");
        pw.println("Night display:");
        if (this.mNightDisplayTintController.isAvailable(getContext())) {
            pw.println("    Activated: " + this.mNightDisplayTintController.isActivated());
            pw.println("    Color temp: " + this.mNightDisplayTintController.getColorTemperature());
        } else {
            pw.println("    Not available");
        }
        pw.println("Global saturation:");
        if (this.mGlobalSaturationTintController.isAvailable(getContext())) {
            pw.println("    Activated: " + this.mGlobalSaturationTintController.isActivated());
        } else {
            pw.println("    Not available");
        }
        this.mAppSaturationController.dump(pw);
        pw.println("Display white balance:");
        if (this.mDisplayWhiteBalanceTintController.isAvailable(getContext())) {
            pw.println("    Activated: " + this.mDisplayWhiteBalanceTintController.isActivated());
            this.mDisplayWhiteBalanceTintController.dump(pw);
        } else {
            pw.println("    Not available");
        }
        pw.println("Reduce bright colors:");
        if (this.mReduceBrightColorsTintController.isAvailable(getContext())) {
            pw.println("    Activated: " + this.mReduceBrightColorsTintController.isActivated());
            this.mReduceBrightColorsTintController.dump(pw);
        } else {
            pw.println("    Not available");
        }
        pw.println("Color mode: " + getColorModeInternal());
    }

    private abstract class NightDisplayAutoMode {
        public abstract void onActivated(boolean z);

        public abstract void onStart();

        public abstract void onStop();

        private NightDisplayAutoMode() {
        }

        public void onCustomStartTimeChanged(java.time.LocalTime startTime) {
        }

        public void onCustomEndTimeChanged(java.time.LocalTime endTime) {
        }
    }

    private final class CustomNightDisplayAutoMode extends com.android.server.display.color.ColorDisplayService.NightDisplayAutoMode implements android.app.AlarmManager.OnAlarmListener {
        private final android.app.AlarmManager mAlarmManager;
        private java.time.LocalTime mEndTime;
        private java.time.LocalDateTime mLastActivatedTime;
        private java.time.LocalTime mStartTime;
        private final android.content.BroadcastReceiver mTimeChangedReceiver;

        CustomNightDisplayAutoMode() {
            super();
            this.mAlarmManager = (android.app.AlarmManager) com.android.server.display.color.ColorDisplayService.this.getContext().getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
            this.mTimeChangedReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.display.color.ColorDisplayService.CustomNightDisplayAutoMode.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(android.content.Context context, android.content.Intent intent) {
                    com.android.server.display.color.ColorDisplayService.CustomNightDisplayAutoMode.this.updateActivated();
                }
            };
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateActivated() {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            java.time.LocalDateTime start = com.android.server.display.color.ColorDisplayService.getDateTimeBefore(this.mStartTime, now);
            java.time.LocalDateTime end = com.android.server.display.color.ColorDisplayService.getDateTimeAfter(this.mEndTime, start);
            boolean activate = now.isBefore(end);
            if (this.mLastActivatedTime != null && this.mLastActivatedTime.isBefore(now) && this.mLastActivatedTime.isAfter(start) && (this.mLastActivatedTime.isAfter(end) || now.isBefore(end))) {
                activate = com.android.server.display.color.ColorDisplayService.this.mNightDisplayTintController.isActivatedSetting();
            }
            if (com.android.server.display.color.ColorDisplayService.this.mNightDisplayTintController.isActivatedStateNotSet() || com.android.server.display.color.ColorDisplayService.this.mNightDisplayTintController.isActivated() != activate) {
                com.android.server.display.color.ColorDisplayService.this.mNightDisplayTintController.setActivated(java.lang.Boolean.valueOf(activate), activate ? start : end);
            }
            updateNextAlarm(java.lang.Boolean.valueOf(com.android.server.display.color.ColorDisplayService.this.mNightDisplayTintController.isActivated()), now);
        }

        /* JADX WARN: Type inference failed for: r1v1, types: [java.time.ZonedDateTime] */
        private void updateNextAlarm(java.lang.Boolean activated, java.time.LocalDateTime now) {
            if (activated != null) {
                java.time.LocalDateTime next = activated.booleanValue() ? com.android.server.display.color.ColorDisplayService.getDateTimeAfter(this.mEndTime, now) : com.android.server.display.color.ColorDisplayService.getDateTimeAfter(this.mStartTime, now);
                long millis = next.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
                this.mAlarmManager.setExact(1, millis, com.android.server.display.color.ColorDisplayService.TAG, this, null);
            }
        }

        @Override // com.android.server.display.color.ColorDisplayService.NightDisplayAutoMode
        public void onStart() {
            android.content.IntentFilter intentFilter = new android.content.IntentFilter("android.intent.action.TIME_SET");
            intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
            com.android.server.display.color.ColorDisplayService.this.getContext().registerReceiver(this.mTimeChangedReceiver, intentFilter);
            this.mStartTime = com.android.server.display.color.ColorDisplayService.this.getNightDisplayCustomStartTimeInternal().getLocalTime();
            this.mEndTime = com.android.server.display.color.ColorDisplayService.this.getNightDisplayCustomEndTimeInternal().getLocalTime();
            this.mLastActivatedTime = com.android.server.display.color.ColorDisplayService.this.getNightDisplayLastActivatedTimeSetting();
            updateActivated();
        }

        @Override // com.android.server.display.color.ColorDisplayService.NightDisplayAutoMode
        public void onStop() {
            com.android.server.display.color.ColorDisplayService.this.getContext().unregisterReceiver(this.mTimeChangedReceiver);
            this.mAlarmManager.cancel(this);
            this.mLastActivatedTime = null;
        }

        @Override // com.android.server.display.color.ColorDisplayService.NightDisplayAutoMode
        public void onActivated(boolean activated) {
            this.mLastActivatedTime = com.android.server.display.color.ColorDisplayService.this.getNightDisplayLastActivatedTimeSetting();
            updateNextAlarm(java.lang.Boolean.valueOf(activated), java.time.LocalDateTime.now());
        }

        @Override // com.android.server.display.color.ColorDisplayService.NightDisplayAutoMode
        public void onCustomStartTimeChanged(java.time.LocalTime startTime) {
            this.mStartTime = startTime;
            this.mLastActivatedTime = null;
            updateActivated();
        }

        @Override // com.android.server.display.color.ColorDisplayService.NightDisplayAutoMode
        public void onCustomEndTimeChanged(java.time.LocalTime endTime) {
            this.mEndTime = endTime;
            this.mLastActivatedTime = null;
            updateActivated();
        }

        @Override // android.app.AlarmManager.OnAlarmListener
        public void onAlarm() {
            android.util.Slog.d(com.android.server.display.color.ColorDisplayService.TAG, "onAlarm");
            updateActivated();
        }
    }

    private final class TwilightNightDisplayAutoMode extends com.android.server.display.color.ColorDisplayService.NightDisplayAutoMode implements com.android.server.twilight.TwilightListener {
        private java.time.LocalDateTime mLastActivatedTime;
        private final com.android.server.twilight.TwilightManager mTwilightManager;

        TwilightNightDisplayAutoMode() {
            super();
            this.mTwilightManager = (com.android.server.twilight.TwilightManager) com.android.server.display.color.ColorDisplayService.this.getLocalService(com.android.server.twilight.TwilightManager.class);
        }

        private void updateActivated(com.android.server.twilight.TwilightState state) {
            if (state == null) {
                return;
            }
            boolean activate = state.isNight();
            if (this.mLastActivatedTime != null) {
                java.time.LocalDateTime now = java.time.LocalDateTime.now();
                java.time.LocalDateTime sunrise = state.sunrise();
                java.time.LocalDateTime sunset = state.sunset();
                if (this.mLastActivatedTime.isBefore(now) && (this.mLastActivatedTime.isBefore(sunrise) ^ this.mLastActivatedTime.isBefore(sunset))) {
                    activate = com.android.server.display.color.ColorDisplayService.this.mNightDisplayTintController.isActivatedSetting();
                }
            }
            if (com.android.server.display.color.ColorDisplayService.this.mNightDisplayTintController.isActivatedStateNotSet() || com.android.server.display.color.ColorDisplayService.this.mNightDisplayTintController.isActivated() != activate) {
                com.android.server.display.color.ColorDisplayService.this.mNightDisplayTintController.setActivated(java.lang.Boolean.valueOf(activate));
            }
        }

        @Override // com.android.server.display.color.ColorDisplayService.NightDisplayAutoMode
        public void onActivated(boolean activated) {
            this.mLastActivatedTime = com.android.server.display.color.ColorDisplayService.this.getNightDisplayLastActivatedTimeSetting();
        }

        @Override // com.android.server.display.color.ColorDisplayService.NightDisplayAutoMode
        public void onStart() {
            this.mTwilightManager.registerListener(this, com.android.server.display.color.ColorDisplayService.this.mHandler);
            this.mLastActivatedTime = com.android.server.display.color.ColorDisplayService.this.getNightDisplayLastActivatedTimeSetting();
            updateActivated(this.mTwilightManager.getLastTwilightState());
        }

        @Override // com.android.server.display.color.ColorDisplayService.NightDisplayAutoMode
        public void onStop() {
            this.mTwilightManager.unregisterListener(this);
            this.mLastActivatedTime = null;
        }

        @Override // com.android.server.twilight.TwilightListener
        public void onTwilightStateChanged(com.android.server.twilight.TwilightState state) {
            android.util.Slog.d(com.android.server.display.color.ColorDisplayService.TAG, "onTwilightStateChanged: isNight=" + (state == null ? null : java.lang.Boolean.valueOf(state.isNight())));
            updateActivated(state);
        }
    }

    static class TintValueAnimator extends android.animation.ValueAnimator {
        private float[] max;
        private float[] min;

        TintValueAnimator() {
        }

        public static com.android.server.display.color.ColorDisplayService.TintValueAnimator ofMatrix(com.android.server.display.color.ColorDisplayService.ColorMatrixEvaluator evaluator, java.lang.Object... values) {
            com.android.server.display.color.ColorDisplayService.TintValueAnimator anim = new com.android.server.display.color.ColorDisplayService.TintValueAnimator();
            anim.setObjectValues(values);
            anim.setEvaluator(evaluator);
            if (values == null || values.length == 0) {
                return null;
            }
            float[] m = (float[]) values[0];
            anim.min = new float[m.length];
            anim.max = new float[m.length];
            for (int i = 0; i < m.length; i++) {
                anim.min[i] = Float.MAX_VALUE;
                anim.max[i] = Float.MIN_VALUE;
            }
            return anim;
        }

        public void updateMinMaxComponents() {
            float[] value = (float[]) getAnimatedValue();
            if (value == null) {
                return;
            }
            for (int i = 0; i < value.length; i++) {
                this.min[i] = java.lang.Math.min(this.min[i], value[i]);
                this.max[i] = java.lang.Math.max(this.max[i], value[i]);
            }
        }

        public float[] getMin() {
            return this.min;
        }

        public float[] getMax() {
            return this.max;
        }
    }

    private static class ColorMatrixEvaluator implements android.animation.TypeEvaluator<float[]> {
        private final float[] mResultMatrix;

        private ColorMatrixEvaluator() {
            this.mResultMatrix = new float[16];
        }

        @Override // android.animation.TypeEvaluator
        public float[] evaluate(float fraction, float[] startValue, float[] endValue) {
            for (int i = 0; i < this.mResultMatrix.length; i++) {
                this.mResultMatrix[i] = android.util.MathUtils.lerp(startValue[i], endValue[i], fraction);
            }
            return this.mResultMatrix;
        }
    }

    private final class NightDisplayTintController extends com.android.server.display.color.TintController {
        private java.lang.Integer mColorTemp;
        private final float[] mColorTempCoefficients;
        private java.lang.Boolean mIsAvailable;
        private final float[] mMatrix;

        private NightDisplayTintController() {
            this.mMatrix = new float[16];
            this.mColorTempCoefficients = new float[9];
        }

        @Override // com.android.server.display.color.TintController
        public void setUp(android.content.Context context, boolean needsLinear) {
            int i;
            android.content.res.Resources resources = context.getResources();
            if (needsLinear) {
                i = android.R.array.config_networkNotifySwitches;
            } else {
                i = android.R.array.config_networkSupportedKeepaliveCount;
            }
            java.lang.String[] coefficients = resources.getStringArray(i);
            for (int i2 = 0; i2 < 9 && i2 < coefficients.length; i2++) {
                this.mColorTempCoefficients[i2] = java.lang.Float.parseFloat(coefficients[i2]);
            }
        }

        @Override // com.android.server.display.color.TintController
        public void setMatrix(int cct) {
            if (this.mMatrix.length != 16) {
                android.util.Slog.d(com.android.server.display.color.ColorDisplayService.TAG, "The display transformation matrix must be 4x4");
                return;
            }
            android.opengl.Matrix.setIdentityM(this.mMatrix, 0);
            float squareTemperature = cct * cct;
            float red = (this.mColorTempCoefficients[0] * squareTemperature) + (cct * this.mColorTempCoefficients[1]) + this.mColorTempCoefficients[2];
            float green = (this.mColorTempCoefficients[3] * squareTemperature) + (cct * this.mColorTempCoefficients[4]) + this.mColorTempCoefficients[5];
            float blue = (this.mColorTempCoefficients[6] * squareTemperature) + (cct * this.mColorTempCoefficients[7]) + this.mColorTempCoefficients[8];
            this.mMatrix[0] = red;
            this.mMatrix[5] = green;
            this.mMatrix[10] = blue;
        }

        @Override // com.android.server.display.color.TintController
        public float[] getMatrix() {
            return isActivated() ? this.mMatrix : com.android.server.display.color.ColorDisplayService.MATRIX_IDENTITY;
        }

        @Override // com.android.server.display.color.TintController
        public void setActivated(java.lang.Boolean activated) {
            setActivated(activated, java.time.LocalDateTime.now());
        }

        public void setActivated(java.lang.Boolean bool, java.time.LocalDateTime localDateTime) {
            if (bool == null) {
                super.setActivated(null);
                return;
            }
            boolean z = bool.booleanValue() != isActivated();
            if (!isActivatedStateNotSet() && z) {
                android.provider.Settings.Secure.putStringForUser(com.android.server.display.color.ColorDisplayService.this.getContext().getContentResolver(), "night_display_last_activated_time", localDateTime.toString(), com.android.server.display.color.ColorDisplayService.this.mCurrentUser);
            }
            if (isActivatedStateNotSet() || z) {
                super.setActivated(bool);
                if (isActivatedSetting() != bool.booleanValue()) {
                    android.provider.Settings.Secure.putIntForUser(com.android.server.display.color.ColorDisplayService.this.getContext().getContentResolver(), "night_display_activated", bool.booleanValue() ? 1 : 0, com.android.server.display.color.ColorDisplayService.this.mCurrentUser);
                }
                onActivated(bool.booleanValue());
            }
        }

        @Override // com.android.server.display.color.TintController
        public int getLevel() {
            return 100;
        }

        @Override // com.android.server.display.color.TintController
        public boolean isAvailable(android.content.Context context) {
            if (this.mIsAvailable == null) {
                this.mIsAvailable = java.lang.Boolean.valueOf(android.hardware.display.ColorDisplayManager.isNightDisplayAvailable(context));
            }
            return this.mIsAvailable.booleanValue();
        }

        private void onActivated(boolean activated) {
            android.util.Slog.i(com.android.server.display.color.ColorDisplayService.TAG, activated ? "Turning on night display" : "Turning off night display");
            if (com.android.server.display.color.ColorDisplayService.this.mNightDisplayAutoMode != null) {
                com.android.server.display.color.ColorDisplayService.this.mNightDisplayAutoMode.onActivated(activated);
            }
            if (com.android.server.display.color.ColorDisplayService.this.mDisplayWhiteBalanceTintController.isAvailable(com.android.server.display.color.ColorDisplayService.this.getContext())) {
                com.android.server.display.color.ColorDisplayService.this.updateDisplayWhiteBalanceStatus();
            }
            com.android.server.display.color.ColorDisplayService.this.mHandler.sendEmptyMessage(3);
        }

        int getColorTemperature() {
            return this.mColorTemp != null ? clampNightDisplayColorTemperature(this.mColorTemp.intValue()) : getColorTemperatureSetting();
        }

        boolean setColorTemperature(int temperature) {
            this.mColorTemp = java.lang.Integer.valueOf(temperature);
            boolean success = android.provider.Settings.Secure.putIntForUser(com.android.server.display.color.ColorDisplayService.this.getContext().getContentResolver(), "night_display_color_temperature", temperature, com.android.server.display.color.ColorDisplayService.this.mCurrentUser);
            onColorTemperatureChanged(temperature);
            return success;
        }

        void onColorTemperatureChanged(int temperature) {
            setMatrix(temperature);
            com.android.server.display.color.ColorDisplayService.this.mHandler.sendEmptyMessage(2);
        }

        boolean isActivatedSetting() {
            return com.android.server.display.color.ColorDisplayService.this.mCurrentUser != -10000 && android.provider.Settings.Secure.getIntForUser(com.android.server.display.color.ColorDisplayService.this.getContext().getContentResolver(), "night_display_activated", 0, com.android.server.display.color.ColorDisplayService.this.mCurrentUser) == 1;
        }

        int getColorTemperatureSetting() {
            if (com.android.server.display.color.ColorDisplayService.this.mCurrentUser == -10000) {
                return -1;
            }
            return clampNightDisplayColorTemperature(android.provider.Settings.Secure.getIntForUser(com.android.server.display.color.ColorDisplayService.this.getContext().getContentResolver(), "night_display_color_temperature", -1, com.android.server.display.color.ColorDisplayService.this.mCurrentUser));
        }

        private int clampNightDisplayColorTemperature(int colorTemperature) {
            if (colorTemperature == -1) {
                colorTemperature = com.android.server.display.color.ColorDisplayService.this.getContext().getResources().getInteger(android.R.integer.config_minNumVisibleRecentTasks_lowRam);
            }
            int minimumTemperature = android.hardware.display.ColorDisplayManager.getMinimumColorTemperature(com.android.server.display.color.ColorDisplayService.this.getContext());
            int maximumTemperature = android.hardware.display.ColorDisplayManager.getMaximumColorTemperature(com.android.server.display.color.ColorDisplayService.this.getContext());
            if (colorTemperature < minimumTemperature) {
                return minimumTemperature;
            }
            if (colorTemperature > maximumTemperature) {
                return maximumTemperature;
            }
            return colorTemperature;
        }
    }

    public class ColorDisplayServiceInternal {
        public ColorDisplayServiceInternal() {
        }

        public void setDisplayWhiteBalanceAllowed(boolean allowed) {
            com.android.server.display.color.ColorDisplayService.this.mDisplayWhiteBalanceTintController.setAllowed(allowed);
            com.android.server.display.color.ColorDisplayService.this.updateDisplayWhiteBalanceStatus();
        }

        public boolean setDisplayWhiteBalanceColorTemperature(int cct) {
            com.android.server.display.color.ColorDisplayService.this.mDisplayWhiteBalanceTintController.setTargetCct(cct);
            if (com.android.server.display.color.ColorDisplayService.this.mDisplayWhiteBalanceTintController.isActivated()) {
                com.android.server.display.color.ColorDisplayService.this.mHandler.sendEmptyMessage(5);
                return true;
            }
            return false;
        }

        public float getDisplayWhiteBalanceLuminance() {
            return com.android.server.display.color.ColorDisplayService.this.mDisplayWhiteBalanceTintController.getLuminance();
        }

        public boolean resetDisplayWhiteBalanceColorTemperature() {
            int temperatureDefault = com.android.server.display.color.ColorDisplayService.this.getContext().getResources().getInteger(android.R.integer.config_delay_for_ims_dereg_millis);
            android.util.Slog.d(com.android.server.display.color.ColorDisplayService.TAG, "resetDisplayWhiteBalanceColorTemperature: " + temperatureDefault);
            return setDisplayWhiteBalanceColorTemperature(temperatureDefault);
        }

        public boolean setDisplayWhiteBalanceListener(com.android.server.display.color.ColorDisplayService.DisplayWhiteBalanceListener listener) {
            com.android.server.display.color.ColorDisplayService.this.mDisplayWhiteBalanceListener = listener;
            return com.android.server.display.color.ColorDisplayService.this.mDisplayWhiteBalanceTintController.isActivated();
        }

        public boolean isDisplayWhiteBalanceEnabled() {
            return com.android.server.display.color.ColorDisplayService.this.isDisplayWhiteBalanceSettingEnabled();
        }

        public boolean setReduceBrightColorsListener(com.android.server.display.color.ColorDisplayService.ReduceBrightColorsListener listener) {
            com.android.server.display.color.ColorDisplayService.this.mReduceBrightColorsListener = listener;
            return com.android.server.display.color.ColorDisplayService.this.mReduceBrightColorsTintController.isActivated();
        }

        public boolean isReduceBrightColorsActivated() {
            return com.android.server.display.color.ColorDisplayService.this.mReduceBrightColorsTintController.isActivated();
        }

        public int getReduceBrightColorsStrength() {
            return com.android.server.display.color.ColorDisplayService.this.mReduceBrightColorsTintController.getStrength();
        }

        public float getReduceBrightColorsAdjustedBrightnessNits(float nits) {
            return com.android.server.display.color.ColorDisplayService.this.mReduceBrightColorsTintController.getAdjustedBrightness(nits);
        }

        public boolean attachColorTransformController(java.lang.String packageName, int userId, java.lang.ref.WeakReference<com.android.server.display.color.ColorDisplayService.ColorTransformController> controller) {
            return com.android.server.display.color.ColorDisplayService.this.mAppSaturationController.addColorTransformController(packageName, userId, controller);
        }

        public com.android.server.display.color.IColorDisplayServiceExt getColorDisplayServiceExt() {
            return com.android.server.display.color.ColorDisplayService.this.mCdsExt;
        }

        public void applyEvenDimmerColorChanges(boolean enabled, int strength) {
            com.android.server.display.color.ColorDisplayService.this.mReduceBrightColorsTintController.setActivated(java.lang.Boolean.valueOf(enabled));
            com.android.server.display.color.ColorDisplayService.this.mReduceBrightColorsTintController.setMatrix(strength);
            com.android.server.display.color.ColorDisplayService.this.mHandler.sendEmptyMessage(6);
        }
    }

    private final class TintHandler extends android.os.Handler {
        private TintHandler(android.os.Looper looper) {
            super(looper, null, true);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    com.android.server.display.color.ColorDisplayService.this.onUserChanged(msg.arg1);
                    break;
                case 1:
                    com.android.server.display.color.ColorDisplayService.this.setUp();
                    break;
                case 2:
                    com.android.server.display.color.ColorDisplayService.this.applyTint(com.android.server.display.color.ColorDisplayService.this.mNightDisplayTintController, true);
                    break;
                case 3:
                    com.android.server.display.color.ColorDisplayService.this.applyTint(com.android.server.display.color.ColorDisplayService.this.mNightDisplayTintController, false);
                    break;
                case 4:
                    com.android.server.display.color.ColorDisplayService.this.mGlobalSaturationTintController.setMatrix(msg.arg1);
                    com.android.server.display.color.ColorDisplayService.this.applyTint(com.android.server.display.color.ColorDisplayService.this.mGlobalSaturationTintController, false);
                    break;
                case 5:
                    com.android.server.display.color.ColorDisplayService.this.applyTintByCct(com.android.server.display.color.ColorDisplayService.this.mDisplayWhiteBalanceTintController, false);
                    break;
                case 6:
                    com.android.server.display.color.ColorDisplayService.this.applyTint(com.android.server.display.color.ColorDisplayService.this.mReduceBrightColorsTintController, true);
                    break;
            }
        }
    }

    final class BinderService extends android.hardware.display.IColorDisplayManager.Stub {
        BinderService() {
        }

        public void setColorMode(int colorMode) {
            setColorMode_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.color.ColorDisplayService.this.setColorModeInternal(colorMode);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public int getColorMode() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.getColorModeInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean isDeviceColorManaged() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.isDeviceColorManagedInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean setSaturationLevel(int level) {
            boolean hasTransformsPermission = com.android.server.display.color.ColorDisplayService.this.getContext().checkCallingOrSelfPermission("android.permission.CONTROL_DISPLAY_COLOR_TRANSFORMS") == 0;
            boolean hasLegacyPermission = com.android.server.display.color.ColorDisplayService.this.getContext().checkCallingPermission("android.permission.CONTROL_DISPLAY_SATURATION") == 0;
            if (!hasTransformsPermission && !hasLegacyPermission) {
                throw new java.lang.SecurityException("Permission required to set display saturation level");
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.color.ColorDisplayService.this.setSaturationLevelInternal(level);
                return true;
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:8:0x0021  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean isSaturationActivated() {
            /*
                r3 = this;
                super.isSaturationActivated_enforcePermission()
                long r0 = android.os.Binder.clearCallingIdentity()
                com.android.server.display.color.ColorDisplayService r2 = com.android.server.display.color.ColorDisplayService.this     // Catch: java.lang.Throwable -> L26
                com.android.server.display.color.TintController r2 = com.android.server.display.color.ColorDisplayService.m3810$$Nest$fgetmGlobalSaturationTintController(r2)     // Catch: java.lang.Throwable -> L26
                boolean r2 = r2.isActivatedStateNotSet()     // Catch: java.lang.Throwable -> L26
                if (r2 != 0) goto L21
                com.android.server.display.color.ColorDisplayService r2 = com.android.server.display.color.ColorDisplayService.this     // Catch: java.lang.Throwable -> L26
                com.android.server.display.color.TintController r2 = com.android.server.display.color.ColorDisplayService.m3810$$Nest$fgetmGlobalSaturationTintController(r2)     // Catch: java.lang.Throwable -> L26
                boolean r2 = r2.isActivated()     // Catch: java.lang.Throwable -> L26
                if (r2 == 0) goto L21
                r2 = 1
                goto L22
            L21:
                r2 = 0
            L22:
                android.os.Binder.restoreCallingIdentity(r0)
                return r2
            L26:
                r2 = move-exception
                android.os.Binder.restoreCallingIdentity(r0)
                throw r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.color.ColorDisplayService.BinderService.isSaturationActivated():boolean");
        }

        public boolean setAppSaturationLevel(java.lang.String packageName, int level) {
            super.setAppSaturationLevel_enforcePermission();
            java.lang.String callingPackageName = ((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).getNameForUid(android.os.Binder.getCallingUid());
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.setAppSaturationLevelInternal(callingPackageName, packageName, level);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public int getTransformCapabilities() {
            super.getTransformCapabilities_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.getTransformCapabilitiesInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean setNightDisplayActivated(boolean activated) {
            setNightDisplayActivated_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.color.ColorDisplayService.this.mNightDisplayTintController.setActivated(java.lang.Boolean.valueOf(activated));
                android.os.Binder.restoreCallingIdentity(token);
                return true;
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(token);
                throw th;
            }
        }

        public boolean isNightDisplayActivated() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.mNightDisplayTintController.isActivated();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean setNightDisplayColorTemperature(int temperature) {
            setNightDisplayColorTemperature_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.mNightDisplayTintController.setColorTemperature(temperature);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public int getNightDisplayColorTemperature() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.mNightDisplayTintController.getColorTemperature();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean setNightDisplayAutoMode(int autoMode) {
            setNightDisplayAutoMode_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.setNightDisplayAutoModeInternal(autoMode);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public int getNightDisplayAutoMode() {
            getNightDisplayAutoMode_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.getNightDisplayAutoModeInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public int getNightDisplayAutoModeRaw() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.getNightDisplayAutoModeRawInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean setNightDisplayCustomStartTime(android.hardware.display.Time startTime) {
            setNightDisplayCustomStartTime_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.setNightDisplayCustomStartTimeInternal(startTime);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.hardware.display.Time getNightDisplayCustomStartTime() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.getNightDisplayCustomStartTimeInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean setNightDisplayCustomEndTime(android.hardware.display.Time endTime) {
            setNightDisplayCustomEndTime_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.setNightDisplayCustomEndTimeInternal(endTime);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public android.hardware.display.Time getNightDisplayCustomEndTime() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.getNightDisplayCustomEndTimeInternal();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean setDisplayWhiteBalanceEnabled(boolean enabled) {
            setDisplayWhiteBalanceEnabled_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.setDisplayWhiteBalanceSettingEnabled(enabled);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean isDisplayWhiteBalanceEnabled() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.isDisplayWhiteBalanceSettingEnabled();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean isReduceBrightColorsActivated() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.mReduceBrightColorsTintController.isActivated();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean setReduceBrightColorsActivated(boolean activated) {
            setReduceBrightColorsActivated_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.setReduceBrightColorsActivatedInternal(activated);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public int getReduceBrightColorsStrength() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.mReduceBrightColorsTintController.getStrength();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public float getReduceBrightColorsOffsetFactor() {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.mReduceBrightColorsTintController.getOffsetFactor();
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public boolean setReduceBrightColorsStrength(int strength) {
            setReduceBrightColorsStrength_enforcePermission();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return com.android.server.display.color.ColorDisplayService.this.setReduceBrightColorsStrengthInternal(strength);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
            if (!com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.display.color.ColorDisplayService.this.getContext(), com.android.server.display.color.ColorDisplayService.TAG, pw)) {
                return;
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.display.color.ColorDisplayService.this.dumpInternal(pw);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public int handleShellCommand(android.os.ParcelFileDescriptor in, android.os.ParcelFileDescriptor out, android.os.ParcelFileDescriptor err, java.lang.String[] args) {
            com.android.server.display.color.ColorDisplayService.this.getContext().enforceCallingOrSelfPermission("android.permission.CONTROL_DISPLAY_COLOR_TRANSFORMS", "Permission required to use ADB color transform commands");
            long token = android.os.Binder.clearCallingIdentity();
            try {
                return new com.android.server.display.color.ColorDisplayShellCommand(com.android.server.display.color.ColorDisplayService.this).exec(this, in.getFileDescriptor(), out.getFileDescriptor(), err.getFileDescriptor(), args);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }
    }
}
