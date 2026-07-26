package com.android.server.input.debug;

/* JADX INFO: loaded from: classes2.dex */
public class FocusEventDebugView extends android.widget.RelativeLayout {
    private static final int KEY_FADEOUT_DURATION_MILLIS = 1000;
    private static final int KEY_SEPARATION_MARGIN_DP = 16;
    private static final int KEY_TRANSITION_DURATION_MILLIS = 100;
    private static final int KEY_VIEW_MIN_WIDTH_DP = 32;
    private static final int KEY_VIEW_SIDE_PADDING_DP = 16;
    private static final int KEY_VIEW_TEXT_SIZE_SP = 12;
    private static final int KEY_VIEW_VERTICAL_PADDING_DP = 8;
    private static final int OUTER_PADDING_DP = 16;
    private static final double ROTATY_GRAPH_HEIGHT_FRACTION = 0.5d;
    private static final java.lang.String TAG = com.android.server.input.debug.FocusEventDebugView.class.getSimpleName();
    private final android.util.DisplayMetrics mDm;
    private com.android.server.input.debug.FocusEventDebugGlobalMonitor mFocusEventDebugGlobalMonitor;
    private final int mOuterPadding;
    private com.android.server.input.debug.FocusEventDebugView.PressedKeyContainer mPressedKeyContainer;
    private final java.util.Map<android.util.Pair<java.lang.Integer, java.lang.Integer>, com.android.server.input.debug.FocusEventDebugView.PressedKeyView> mPressedKeys;
    private com.android.server.input.debug.FocusEventDebugView.PressedKeyContainer mPressedModifierContainer;
    private com.android.server.input.debug.RotaryInputGraphView mRotaryInputGraphView;
    private final java.util.function.Supplier<com.android.server.input.debug.RotaryInputGraphView> mRotaryInputGraphViewFactory;
    private com.android.server.input.debug.RotaryInputValueView mRotaryInputValueView;
    private final java.util.function.Supplier<com.android.server.input.debug.RotaryInputValueView> mRotaryInputValueViewFactory;
    private final com.android.server.input.InputManagerService mService;

    FocusEventDebugView(android.content.Context c, com.android.server.input.InputManagerService service, java.util.function.Supplier<com.android.server.input.debug.RotaryInputValueView> rotaryInputValueViewFactory, java.util.function.Supplier<com.android.server.input.debug.RotaryInputGraphView> rotaryInputGraphViewFactory) {
        super(c);
        this.mPressedKeys = new java.util.HashMap();
        setFocusableInTouchMode(true);
        this.mService = service;
        this.mRotaryInputValueViewFactory = rotaryInputValueViewFactory;
        this.mRotaryInputGraphViewFactory = rotaryInputGraphViewFactory;
        this.mDm = this.mContext.getResources().getDisplayMetrics();
        this.mOuterPadding = (int) android.util.TypedValue.applyDimension(1, 16.0f, this.mDm);
    }

    public FocusEventDebugView(final android.content.Context c, com.android.server.input.InputManagerService service) {
        this(c, service, new java.util.function.Supplier() { // from class: com.android.server.input.debug.FocusEventDebugView$$ExternalSyntheticLambda5
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return com.android.server.input.debug.FocusEventDebugView.lambda$new$0(c);
            }
        }, new java.util.function.Supplier() { // from class: com.android.server.input.debug.FocusEventDebugView$$ExternalSyntheticLambda6
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return com.android.server.input.debug.FocusEventDebugView.lambda$new$1(c);
            }
        });
    }

    static /* synthetic */ com.android.server.input.debug.RotaryInputValueView lambda$new$0(android.content.Context c) {
        return new com.android.server.input.debug.RotaryInputValueView(c);
    }

    static /* synthetic */ com.android.server.input.debug.RotaryInputGraphView lambda$new$1(android.content.Context c) {
        return new com.android.server.input.debug.RotaryInputGraphView(c);
    }

    @Override // android.view.View
    public android.view.WindowInsets onApplyWindowInsets(android.view.WindowInsets insets) {
        int paddingBottom = 0;
        android.view.RoundedCorner bottomLeft = insets.getRoundedCorner(3);
        if (bottomLeft != null && !insets.isRound()) {
            paddingBottom = bottomLeft.getRadius();
        }
        android.view.RoundedCorner bottomRight = insets.getRoundedCorner(2);
        if (bottomRight != null && !insets.isRound()) {
            paddingBottom = java.lang.Math.max(paddingBottom, bottomRight.getRadius());
        }
        if (insets.getDisplayCutout() != null) {
            paddingBottom = java.lang.Math.max(paddingBottom, insets.getDisplayCutout().getSafeInsetBottom());
        }
        setPadding(this.mOuterPadding, this.mOuterPadding, this.mOuterPadding, this.mOuterPadding + paddingBottom);
        setClipToPadding(false);
        invalidate();
        return super.onApplyWindowInsets(insets);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(android.view.KeyEvent event) {
        lambda$reportKeyEvent$5(event);
        return super.dispatchKeyEvent(event);
    }

    public void updateShowKeyPresses(final boolean enabled) {
        post(new java.lang.Runnable() { // from class: com.android.server.input.debug.FocusEventDebugView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateShowKeyPresses$2(enabled);
            }
        });
    }

    public void updateShowRotaryInput(final boolean enabled) {
        post(new java.lang.Runnable() { // from class: com.android.server.input.debug.FocusEventDebugView$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$updateShowRotaryInput$3(enabled);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleUpdateShowKeyPresses, reason: merged with bridge method [inline-methods] */
    public void lambda$updateShowKeyPresses$2(boolean enabled) {
        if (enabled == showKeyPresses()) {
            return;
        }
        if (!enabled) {
            removeView(this.mPressedKeyContainer);
            this.mPressedKeyContainer = null;
            removeView(this.mPressedModifierContainer);
            this.mPressedModifierContainer = null;
            return;
        }
        this.mPressedKeyContainer = new com.android.server.input.debug.FocusEventDebugView.PressedKeyContainer(this.mContext);
        this.mPressedKeyContainer.setOrientation(0);
        this.mPressedKeyContainer.setGravity(85);
        this.mPressedKeyContainer.setLayoutDirection(0);
        final android.widget.HorizontalScrollView scroller = new android.widget.HorizontalScrollView(this.mContext);
        scroller.addView(this.mPressedKeyContainer);
        scroller.setHorizontalScrollBarEnabled(false);
        scroller.addOnLayoutChangeListener(new android.view.View.OnLayoutChangeListener() { // from class: com.android.server.input.debug.FocusEventDebugView$$ExternalSyntheticLambda1
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(android.view.View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                scroller.fullScroll(66);
            }
        });
        scroller.setHorizontalFadingEdgeEnabled(true);
        android.widget.RelativeLayout.LayoutParams scrollerLayoutParams = new android.widget.RelativeLayout.LayoutParams(-2, -2);
        scrollerLayoutParams.addRule(12);
        scrollerLayoutParams.addRule(11);
        addView(scroller, scrollerLayoutParams);
        this.mPressedModifierContainer = new com.android.server.input.debug.FocusEventDebugView.PressedKeyContainer(this.mContext);
        this.mPressedModifierContainer.setOrientation(1);
        this.mPressedModifierContainer.setGravity(83);
        android.widget.RelativeLayout.LayoutParams modifierLayoutParams = new android.widget.RelativeLayout.LayoutParams(-2, -2);
        modifierLayoutParams.addRule(12);
        modifierLayoutParams.addRule(9);
        modifierLayoutParams.addRule(0, scroller.getId());
        addView(this.mPressedModifierContainer, modifierLayoutParams);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: handleUpdateShowRotaryInput, reason: merged with bridge method [inline-methods] */
    public void lambda$updateShowRotaryInput$3(boolean enabled) {
        if (enabled == showRotaryInput()) {
            return;
        }
        if (!enabled) {
            this.mFocusEventDebugGlobalMonitor.dispose();
            this.mFocusEventDebugGlobalMonitor = null;
            removeView(this.mRotaryInputValueView);
            this.mRotaryInputValueView = null;
            removeView(this.mRotaryInputGraphView);
            this.mRotaryInputGraphView = null;
            return;
        }
        this.mFocusEventDebugGlobalMonitor = new com.android.server.input.debug.FocusEventDebugGlobalMonitor(this, this.mService);
        this.mRotaryInputValueView = this.mRotaryInputValueViewFactory.get();
        android.widget.RelativeLayout.LayoutParams valueLayoutParams = new android.widget.RelativeLayout.LayoutParams(-2, -2);
        valueLayoutParams.addRule(14);
        valueLayoutParams.addRule(12);
        addView(this.mRotaryInputValueView, valueLayoutParams);
        this.mRotaryInputGraphView = this.mRotaryInputGraphViewFactory.get();
        android.widget.RelativeLayout.LayoutParams graphLayoutParams = new android.widget.RelativeLayout.LayoutParams(-1, (int) (((double) this.mDm.heightPixels) * ROTATY_GRAPH_HEIGHT_FRACTION));
        graphLayoutParams.addRule(13);
        addView(this.mRotaryInputGraphView, graphLayoutParams);
    }

    public void reportKeyEvent(android.view.KeyEvent event) {
        final android.view.KeyEvent keyEvent = android.view.KeyEvent.obtain(event);
        post(new java.lang.Runnable() { // from class: com.android.server.input.debug.FocusEventDebugView$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$reportKeyEvent$5(keyEvent);
            }
        });
    }

    public void reportMotionEvent(android.view.MotionEvent event) {
        if (event.getSource() != 4194304) {
            return;
        }
        final android.view.MotionEvent motionEvent = android.view.MotionEvent.obtain(event);
        post(new java.lang.Runnable() { // from class: com.android.server.input.debug.FocusEventDebugView$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$reportMotionEvent$6(motionEvent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleKeyEvent, reason: merged with bridge method [inline-methods] */
    public void lambda$reportKeyEvent$5(android.view.KeyEvent keyEvent) {
        com.android.server.input.debug.FocusEventDebugView.PressedKeyContainer container;
        if (!showKeyPresses()) {
            return;
        }
        android.util.Pair<java.lang.Integer, java.lang.Integer> identifier = new android.util.Pair<>(java.lang.Integer.valueOf(keyEvent.getDeviceId()), java.lang.Integer.valueOf(keyEvent.getScanCode()));
        if (android.view.KeyEvent.isModifierKey(keyEvent.getKeyCode())) {
            container = this.mPressedModifierContainer;
        } else {
            container = this.mPressedKeyContainer;
        }
        com.android.server.input.debug.FocusEventDebugView.PressedKeyView pressedKeyView = this.mPressedKeys.get(identifier);
        switch (keyEvent.getAction()) {
            case 0:
                if (pressedKeyView != null) {
                    if (keyEvent.getRepeatCount() == 0) {
                        android.util.Slog.w(TAG, "Got key down for " + android.view.KeyEvent.keyCodeToString(keyEvent.getKeyCode()) + " that was already tracked as being down.");
                    } else {
                        container.handleKeyRepeat(pressedKeyView);
                    }
                } else {
                    com.android.server.input.debug.FocusEventDebugView.PressedKeyView pressedKeyView2 = new com.android.server.input.debug.FocusEventDebugView.PressedKeyView(this.mContext, getLabel(keyEvent));
                    this.mPressedKeys.put(identifier, pressedKeyView2);
                    container.handleKeyPressed(pressedKeyView2);
                }
                break;
            case 1:
                if (pressedKeyView == null) {
                    android.util.Slog.w(TAG, "Got key up for " + android.view.KeyEvent.keyCodeToString(keyEvent.getKeyCode()) + " that was not tracked as being down.");
                } else {
                    this.mPressedKeys.remove(identifier);
                    container.handleKeyRelease(pressedKeyView);
                }
                break;
        }
        keyEvent.recycle();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: handleRotaryInput, reason: merged with bridge method [inline-methods] */
    public void lambda$reportMotionEvent$6(android.view.MotionEvent motionEvent) {
        if (!showRotaryInput()) {
            return;
        }
        float scrollAxisValue = motionEvent.getAxisValue(26);
        this.mRotaryInputValueView.updateValue(scrollAxisValue);
        this.mRotaryInputGraphView.addValue(scrollAxisValue, motionEvent.getEventTime());
        motionEvent.recycle();
    }

    private static java.lang.String getLabel(android.view.KeyEvent event) {
        switch (event.getKeyCode()) {
            case 3:
                return "◯";
            case 4:
                return "◁";
            case 19:
                return "↑";
            case 20:
                return "↓";
            case 21:
                return "←";
            case 22:
                return "→";
            case 61:
                return "⇥";
            case 62:
                return "␣";
            case 66:
            case 160:
                return "⏎";
            case 67:
                return "⌫";
            case 85:
                return "⏯";
            case 111:
                return "esc";
            case 112:
                return "⌦";
            case 268:
                return "↖";
            case 269:
                return "↙";
            case 270:
                return "↗";
            case 271:
                return "↘";
            case com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_LOCATION_PROVIDER /* 312 */:
                return "□";
            default:
                int unicodeChar = event.getUnicodeChar();
                if (unicodeChar != 0) {
                    if ((Integer.MIN_VALUE & unicodeChar) != 0) {
                        int combiningChar = android.view.KeyCharacterMap.getCombiningChar(Integer.MAX_VALUE & unicodeChar);
                        return "◌" + java.lang.String.valueOf((char) combiningChar);
                    }
                    return java.lang.String.valueOf((char) unicodeChar);
                }
                java.lang.String label = android.view.KeyEvent.keyCodeToString(event.getKeyCode());
                if (label.startsWith("KEYCODE_")) {
                    return label.substring(8);
                }
                return label;
        }
    }

    private boolean showKeyPresses() {
        return this.mPressedKeyContainer != null;
    }

    private boolean showRotaryInput() {
        return this.mRotaryInputValueView != null;
    }

    private static class PressedKeyView extends android.widget.TextView {
        private static final android.graphics.ColorFilter sInvertColors = new android.graphics.ColorMatrixColorFilter(new float[]{-1.0f, 0.0f, 0.0f, 0.0f, 255.0f, 0.0f, -1.0f, 0.0f, 0.0f, 255.0f, 0.0f, 0.0f, -1.0f, 0.0f, 255.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});

        PressedKeyView(android.content.Context c, java.lang.String label) {
            super(c);
            android.util.DisplayMetrics dm = c.getResources().getDisplayMetrics();
            int keyViewSidePadding = (int) android.util.TypedValue.applyDimension(1, 16.0f, dm);
            int keyViewVerticalPadding = (int) android.util.TypedValue.applyDimension(1, 8.0f, dm);
            int keyViewMinWidth = (int) android.util.TypedValue.applyDimension(1, 32.0f, dm);
            int textSize = (int) android.util.TypedValue.applyDimension(2, 12.0f, dm);
            setText(label);
            setGravity(17);
            setMinimumWidth(keyViewMinWidth);
            setTextSize(textSize);
            setTypeface(android.graphics.Typeface.SANS_SERIF);
            setBackgroundResource(android.R.drawable.editbox_background_focus_yellow);
            setPaddingRelative(keyViewSidePadding, keyViewVerticalPadding, keyViewSidePadding, keyViewVerticalPadding);
            setHighlighted(true);
        }

        void setHighlighted(boolean isHighlighted) {
            if (isHighlighted) {
                setTextColor(android.hardware.audio.common.V2_0.AudioFormat.MAIN_MASK);
                getBackground().setColorFilter(sInvertColors);
            } else {
                setTextColor(-1);
                getBackground().clearColorFilter();
            }
            invalidate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static class PressedKeyContainer extends android.widget.LinearLayout {
        private final android.view.ViewGroup.MarginLayoutParams mPressedKeyLayoutParams;

        PressedKeyContainer(android.content.Context c) {
            super(c);
            android.util.DisplayMetrics dm = c.getResources().getDisplayMetrics();
            int keySeparationMargin = (int) android.util.TypedValue.applyDimension(1, 16.0f, dm);
            android.animation.LayoutTransition transition = new android.animation.LayoutTransition();
            transition.disableTransitionType(2);
            transition.disableTransitionType(3);
            transition.disableTransitionType(1);
            transition.setDuration(100L);
            setLayoutTransition(transition);
            this.mPressedKeyLayoutParams = new android.view.ViewGroup.MarginLayoutParams(-2, -2);
            if (getOrientation() == 1) {
                this.mPressedKeyLayoutParams.setMargins(0, keySeparationMargin, 0, 0);
            } else {
                this.mPressedKeyLayoutParams.setMargins(keySeparationMargin, 0, 0, 0);
            }
        }

        public void handleKeyPressed(com.android.server.input.debug.FocusEventDebugView.PressedKeyView pressedKeyView) {
            addView(pressedKeyView, getChildCount(), this.mPressedKeyLayoutParams);
            invalidate();
        }

        public void handleKeyRepeat(com.android.server.input.debug.FocusEventDebugView.PressedKeyView repeatedKeyView) {
        }

        public void handleKeyRelease(com.android.server.input.debug.FocusEventDebugView.PressedKeyView releasedKeyView) {
            releasedKeyView.setHighlighted(false);
            releasedKeyView.clearAnimation();
            releasedKeyView.animate().alpha(0.0f).setDuration(1000L).setInterpolator(new android.view.animation.AccelerateInterpolator()).withEndAction(new java.lang.Runnable() { // from class: com.android.server.input.debug.FocusEventDebugView$PressedKeyContainer$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.cleanUpPressedKeyViews();
                }
            }).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cleanUpPressedKeyViews() {
            int numChildrenToRemove = 0;
            for (int i = 0; i < getChildCount(); i++) {
                android.view.View child = getChildAt(i);
                if (child.getAlpha() != 0.0f) {
                    break;
                }
                child.setVisibility(8);
                child.clearAnimation();
                numChildrenToRemove++;
            }
            removeViews(0, numChildrenToRemove);
            invalidate();
        }
    }
}
