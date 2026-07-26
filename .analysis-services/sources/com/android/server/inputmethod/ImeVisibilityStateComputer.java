package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
public final class ImeVisibilityStateComputer {
    private static boolean DEBUG = com.android.server.inputmethod.InputMethodManagerService.DEBUG;
    public static final int STATE_HIDE_IME = 0;
    public static final int STATE_HIDE_IME_EXPLICIT = 5;
    public static final int STATE_HIDE_IME_NOT_ALWAYS = 6;
    public static final int STATE_INVALID = -1;
    public static final int STATE_REMOVE_IME_SNAPSHOT = 8;
    public static final int STATE_SHOW_IME = 1;
    public static final int STATE_SHOW_IME_ABOVE_OVERLAY = 2;
    public static final int STATE_SHOW_IME_BEHIND_OVERLAY = 3;
    public static final int STATE_SHOW_IME_IMPLICIT = 7;
    public static final int STATE_SHOW_IME_SNAPSHOT = 4;
    private static final java.lang.String TAG = "ImeVisibilityStateComputer";
    private android.os.IBinder mCurVisibleImeInputTarget;
    private android.os.IBinder mCurVisibleImeLayeringOverlay;
    final com.android.server.inputmethod.InputMethodManagerService.ImeDisplayValidator mImeDisplayValidator;
    private boolean mInputShown;
    private final com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityPolicy mPolicy;
    private final java.util.WeakHashMap<android.os.IBinder, com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState> mRequestWindowStateMap;
    private boolean mRequestedImeScreenshot;
    boolean mRequestedShowExplicitly;
    private final com.android.server.inputmethod.InputMethodManagerService mService;
    boolean mShowForced;
    private final com.android.server.wm.WindowManagerInternal mWindowManagerInternal;

    @interface VisibilityState {
    }

    /* JADX WARN: Illegal instructions before constructor call */
    public ImeVisibilityStateComputer(com.android.server.inputmethod.InputMethodManagerService service) {
        com.android.server.wm.WindowManagerInternal windowManagerInternal = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
        final com.android.server.wm.WindowManagerInternal windowManagerInternal2 = (com.android.server.wm.WindowManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.WindowManagerInternal.class);
        java.util.Objects.requireNonNull(windowManagerInternal2);
        this(service, windowManagerInternal, new com.android.server.inputmethod.InputMethodManagerService.ImeDisplayValidator() { // from class: com.android.server.inputmethod.ImeVisibilityStateComputer$$ExternalSyntheticLambda0
            @Override // com.android.server.inputmethod.InputMethodManagerService.ImeDisplayValidator
            public final int getDisplayImePolicy(int i) {
                return windowManagerInternal2.getDisplayImePolicy(i);
            }
        }, new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityPolicy());
    }

    public ImeVisibilityStateComputer(com.android.server.inputmethod.InputMethodManagerService service, com.android.server.inputmethod.ImeVisibilityStateComputer.Injector injector) {
        this(service, injector.getWmService(), injector.getImeValidator(), new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityPolicy());
    }

    interface Injector {
        default com.android.server.wm.WindowManagerInternal getWmService() {
            return null;
        }

        default com.android.server.inputmethod.InputMethodManagerService.ImeDisplayValidator getImeValidator() {
            return null;
        }
    }

    private ImeVisibilityStateComputer(com.android.server.inputmethod.InputMethodManagerService service, com.android.server.wm.WindowManagerInternal wmService, com.android.server.inputmethod.InputMethodManagerService.ImeDisplayValidator imeDisplayValidator, com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityPolicy imePolicy) {
        this.mRequestWindowStateMap = new java.util.WeakHashMap<>();
        this.mService = service;
        this.mWindowManagerInternal = wmService;
        this.mImeDisplayValidator = imeDisplayValidator;
        this.mPolicy = imePolicy;
        this.mWindowManagerInternal.setInputMethodTargetChangeListener(new com.android.server.wm.ImeTargetChangeListener() { // from class: com.android.server.inputmethod.ImeVisibilityStateComputer.1
            @Override // com.android.server.wm.ImeTargetChangeListener
            public void onImeTargetOverlayVisibilityChanged(android.os.IBinder overlayWindowToken, int windowType, boolean visible, boolean removed) {
                com.android.server.inputmethod.ImeVisibilityStateComputer.this.mCurVisibleImeLayeringOverlay = (!visible || removed || windowType == 3) ? null : overlayWindowToken;
            }

            @Override // com.android.server.wm.ImeTargetChangeListener
            public void onImeInputTargetVisibilityChanged(android.os.IBinder imeInputTarget, boolean visibleRequested, boolean removed) {
                if (com.android.server.inputmethod.ImeVisibilityStateComputer.this.mCurVisibleImeInputTarget == imeInputTarget && ((!visibleRequested || removed) && com.android.server.inputmethod.ImeVisibilityStateComputer.this.mCurVisibleImeLayeringOverlay != null)) {
                    android.view.inputmethod.ImeTracker.Token statsToken = android.view.inputmethod.ImeTracker.forLogging().onStart(2, 6, 37, false);
                    com.android.server.inputmethod.ImeVisibilityStateComputer.this.mService.onApplyImeVisibilityFromComputer(imeInputTarget, statsToken, new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult(5, 37));
                }
                com.android.server.inputmethod.ImeVisibilityStateComputer.this.mCurVisibleImeInputTarget = (!visibleRequested || removed) ? null : imeInputTarget;
            }
        });
    }

    boolean onImeShowFlags(android.view.inputmethod.ImeTracker.Token statsToken, int showFlags) {
        if (this.mPolicy.mA11yRequestingNoSoftKeyboard || this.mPolicy.mImeHiddenByDisplayPolicy) {
            android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 4);
            return false;
        }
        android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 4);
        if ((showFlags & 2) != 0) {
            this.mRequestedShowExplicitly = true;
            this.mShowForced = true;
        } else if ((showFlags & 1) == 0) {
            this.mRequestedShowExplicitly = true;
        }
        return true;
    }

    boolean canHideIme(android.view.inputmethod.ImeTracker.Token statsToken, int hideFlags) {
        if ((hideFlags & 1) != 0 && (this.mRequestedShowExplicitly || this.mShowForced)) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "Not hiding: explicit show not cancelled by non-explicit hide");
            }
            android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 6);
            return false;
        }
        if (this.mShowForced && (hideFlags & 2) != 0) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "Not hiding: forced show not cancelled by not-always hide");
            }
            android.view.inputmethod.ImeTracker.forLogging().onFailed(statsToken, 7);
            return false;
        }
        android.view.inputmethod.ImeTracker.forLogging().onProgress(statsToken, 7);
        return true;
    }

    int getShowFlagsForInputMethodServiceOnly() {
        if (this.mShowForced) {
            int flags = 0 | 3;
            return flags;
        }
        if (!this.mRequestedShowExplicitly) {
            return 0;
        }
        int flags2 = 0 | 1;
        return flags2;
    }

    int getShowFlags() {
        if (this.mShowForced) {
            int flags = 0 | 2;
            return flags;
        }
        if (this.mRequestedShowExplicitly) {
            return 0;
        }
        int flags2 = 0 | 1;
        return flags2;
    }

    void clearImeShowFlags() {
        this.mRequestedShowExplicitly = false;
        this.mShowForced = false;
        this.mInputShown = false;
    }

    int computeImeDisplayId(com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState state, int displayId) {
        int displayToShowIme = com.android.server.inputmethod.InputMethodManagerService.computeImeDisplayIdForTarget(displayId, this.mImeDisplayValidator);
        state.setImeDisplayId(displayToShowIme);
        boolean imeHiddenByPolicy = displayToShowIme == -1;
        this.mPolicy.setImeHiddenByDisplayPolicy(imeHiddenByPolicy);
        return displayToShowIme;
    }

    void requestImeVisibility(android.os.IBinder windowToken, boolean showIme) {
        com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState state = getOrCreateWindowState(windowToken);
        if (!this.mPolicy.mPendingA11yRequestingHideKeyboard) {
            state.setRequestedImeVisible(showIme);
        } else {
            this.mPolicy.mPendingA11yRequestingHideKeyboard = false;
        }
        state.setRequestImeToken(new android.os.Binder());
        setWindowStateInner(windowToken, state);
    }

    com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState getOrCreateWindowState(android.os.IBinder windowToken) {
        com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState state = this.mRequestWindowStateMap.get(windowToken);
        if (state == null) {
            return new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState(0, 0, false, false, false);
        }
        return state;
    }

    com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState getWindowStateOrNull(android.os.IBinder windowToken) {
        com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState state = this.mRequestWindowStateMap.get(windowToken);
        return state;
    }

    void setWindowState(android.os.IBinder windowToken, com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState newState) {
        com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState state = this.mRequestWindowStateMap.get(windowToken);
        if (state != null && newState.hasEditorFocused() && newState.mToolType != 2) {
            newState.setRequestedImeVisible(state.mRequestedImeVisible);
        }
        setWindowStateInner(windowToken, newState);
    }

    private void setWindowStateInner(android.os.IBinder windowToken, com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState newState) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "setWindowStateInner, windowToken=" + windowToken + ", state=" + newState);
        }
        this.mRequestWindowStateMap.put(windowToken, newState);
    }

    static class ImeVisibilityResult {
        private final int mReason;
        private final int mState;

        ImeVisibilityResult(int state, int reason) {
            this.mState = state;
            this.mReason = reason;
        }

        int getState() {
            return this.mState;
        }

        int getReason() {
            return this.mReason;
        }
    }

    com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult computeState(com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState state, boolean allowVisible) {
        int softInputVisibility = state.mSoftInputModeState & 15;
        boolean doAutoShow = (state.mSoftInputModeState & com.android.internal.util.FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED) == 16 || this.mService.mRes.getConfiguration().isLayoutSizeAtLeast(3);
        boolean isForwardNavigation = (state.mSoftInputModeState & 256) != 0;
        if (state.hasEditorFocused() && shouldRestoreImeVisibility(state)) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "Will show input to restore visibility");
            }
            state.setRequestedImeVisible(true);
            setWindowStateInner(getWindowTokenFrom(state), state);
            return new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult(7, 23);
        }
        switch (softInputVisibility) {
            case 0:
                if (state.hasImeFocusChanged() && (!state.hasEditorFocused() || (!doAutoShow && !android.view.inputmethod.Flags.refactorInsetsController()))) {
                    if (android.view.WindowManager.LayoutParams.mayUseInputMethod(state.getWindowFlags())) {
                        if (DEBUG) {
                            android.util.Slog.v(TAG, "Unspecified window will hide input");
                        }
                        return new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult(6, 12);
                    }
                } else if (state.hasEditorFocused() && doAutoShow && isForwardNavigation) {
                    if (DEBUG) {
                        android.util.Slog.v(TAG, "Unspecified window will show input");
                    }
                    return new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult(7, 6);
                }
            case 1:
                com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState lastState = getWindowStateOrNull(this.mService.mLastImeTargetWindow);
                if (lastState != null) {
                    state.setRequestedImeVisible(lastState.mRequestedImeVisible);
                }
                break;
            case 2:
                if (!android.view.inputmethod.Flags.refactorInsetsController() && isForwardNavigation) {
                    if (DEBUG) {
                        android.util.Slog.v(TAG, "Window asks to hide input going forward");
                    }
                    return new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult(5, 13);
                }
                break;
            case 3:
                if (!android.view.inputmethod.Flags.refactorInsetsController() && state.hasImeFocusChanged()) {
                    if (DEBUG) {
                        android.util.Slog.v(TAG, "Window asks to hide input");
                    }
                    return new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult(5, 14);
                }
                break;
            case 4:
                if (isForwardNavigation) {
                    if (allowVisible) {
                        if (DEBUG) {
                            android.util.Slog.v(TAG, "Window asks to show input going forward");
                        }
                        return new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult(7, 7);
                    }
                    android.util.Slog.e(TAG, "SOFT_INPUT_STATE_VISIBLE is ignored because there is no focused view that also returns true from View#onCheckIsTextEditor()");
                }
                break;
            case 5:
                if (DEBUG) {
                    android.util.Slog.v(TAG, "Window asks to always show input");
                }
                if (allowVisible) {
                    if (state.hasImeFocusChanged()) {
                        return new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult(7, 8);
                    }
                } else {
                    android.util.Slog.e(TAG, "SOFT_INPUT_STATE_ALWAYS_VISIBLE is ignored because there is no focused view that also returns true from View#onCheckIsTextEditor()");
                }
                break;
        }
        if (!state.hasImeFocusChanged() && state.isStartInputByGainFocus()) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "Same window without editor will hide input");
            }
            return new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult(5, 21);
        }
        if (!state.hasEditorFocused() && this.mInputShown && state.isStartInputByGainFocus() && this.mService.mInputMethodDeviceConfigs.shouldHideImeWhenNoEditorFocus()) {
            if (DEBUG) {
                android.util.Slog.v(TAG, "Window without editor will hide input");
            }
            if (android.view.inputmethod.Flags.refactorInsetsController()) {
                state.setRequestedImeVisible(false);
            }
            return new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult(5, 33);
        }
        return null;
    }

    com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult onInteractiveChanged(android.os.IBinder windowToken, boolean interactive) {
        com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState state = getWindowStateOrNull(windowToken);
        if (state != null && state.isRequestedImeVisible() && this.mInputShown && !interactive) {
            this.mRequestedImeScreenshot = true;
            return new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult(4, 34);
        }
        if (interactive && this.mRequestedImeScreenshot) {
            this.mRequestedImeScreenshot = false;
            return new com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityResult(8, 35);
        }
        return null;
    }

    android.os.IBinder getWindowTokenFrom(android.os.IBinder requestImeToken) {
        for (android.os.IBinder windowToken : this.mRequestWindowStateMap.keySet()) {
            com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState state = this.mRequestWindowStateMap.get(windowToken);
            if (state.getRequestImeToken() == requestImeToken) {
                return windowToken;
            }
        }
        return this.mService.mImeBindingState.mFocusedWindow;
    }

    android.os.IBinder getWindowTokenFrom(com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState windowState) {
        for (android.os.IBinder windowToken : this.mRequestWindowStateMap.keySet()) {
            com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState state = this.mRequestWindowStateMap.get(windowToken);
            if (state == windowState) {
                return windowToken;
            }
        }
        return null;
    }

    boolean shouldRestoreImeVisibility(com.android.server.inputmethod.ImeVisibilityStateComputer.ImeTargetWindowState state) {
        int softInputMode = state.getSoftInputModeState();
        switch (softInputMode & 15) {
            case 2:
                if ((softInputMode & 256) != 0) {
                    return false;
                }
                break;
            case 3:
                return false;
        }
        return this.mWindowManagerInternal.shouldRestoreImeVisibility(getWindowTokenFrom(state));
    }

    boolean isInputShown() {
        return this.mInputShown;
    }

    void setInputShown(boolean inputShown) {
        this.mInputShown = inputShown;
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        proto.write(1133871366154L, this.mRequestedShowExplicitly);
        proto.write(1133871366155L, this.mShowForced);
        proto.write(1133871366168L, this.mPolicy.isA11yRequestNoSoftKeyboard());
        proto.write(1133871366156L, this.mInputShown);
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        android.util.Printer p = new android.util.PrintWriterPrinter(pw);
        p.println(prefix + "mRequestedShowExplicitly=" + this.mRequestedShowExplicitly + " mShowForced=" + this.mShowForced);
        p.println(prefix + "mImeHiddenByDisplayPolicy=" + this.mPolicy.isImeHiddenByDisplayPolicy());
        p.println(prefix + "mInputShown=" + this.mInputShown);
    }

    static class ImeVisibilityPolicy {
        private boolean mA11yRequestingNoSoftKeyboard;
        private boolean mImeHiddenByDisplayPolicy;
        private boolean mPendingA11yRequestingHideKeyboard;

        ImeVisibilityPolicy() {
        }

        void setImeHiddenByDisplayPolicy(boolean hideIme) {
            this.mImeHiddenByDisplayPolicy = hideIme;
        }

        boolean isImeHiddenByDisplayPolicy() {
            return this.mImeHiddenByDisplayPolicy;
        }

        void setA11yRequestNoSoftKeyboard(int keyboardShowMode) {
            this.mA11yRequestingNoSoftKeyboard = (keyboardShowMode & 3) == 1;
            if (this.mA11yRequestingNoSoftKeyboard) {
                this.mPendingA11yRequestingHideKeyboard = true;
            }
        }

        boolean isA11yRequestNoSoftKeyboard() {
            return this.mA11yRequestingNoSoftKeyboard;
        }
    }

    com.android.server.inputmethod.ImeVisibilityStateComputer.ImeVisibilityPolicy getImePolicy() {
        return this.mPolicy;
    }

    static class ImeTargetWindowState {
        private final boolean mHasFocusedEditor;
        private int mImeDisplayId;
        private final boolean mImeFocusChanged;
        private final boolean mIsStartInputByGainFocus;
        private android.os.IBinder mRequestImeToken;
        private boolean mRequestedImeVisible;
        private final int mSoftInputModeState;
        private final int mToolType;
        private final int mWindowFlags;

        ImeTargetWindowState(int softInputModeState, int windowFlags, boolean imeFocusChanged, boolean hasFocusedEditor, boolean isStartInputByGainFocus) {
            this(softInputModeState, windowFlags, imeFocusChanged, hasFocusedEditor, isStartInputByGainFocus, 0);
        }

        ImeTargetWindowState(int softInputModeState, int windowFlags, boolean imeFocusChanged, boolean hasFocusedEditor, boolean isStartInputByGainFocus, int toolType) {
            this.mImeDisplayId = 0;
            this.mSoftInputModeState = softInputModeState;
            this.mWindowFlags = windowFlags;
            this.mImeFocusChanged = imeFocusChanged;
            this.mHasFocusedEditor = hasFocusedEditor;
            this.mIsStartInputByGainFocus = isStartInputByGainFocus;
            this.mToolType = toolType;
        }

        boolean hasImeFocusChanged() {
            return this.mImeFocusChanged;
        }

        boolean hasEditorFocused() {
            return this.mHasFocusedEditor;
        }

        boolean isStartInputByGainFocus() {
            return this.mIsStartInputByGainFocus;
        }

        int getSoftInputModeState() {
            return this.mSoftInputModeState;
        }

        int getWindowFlags() {
            return this.mWindowFlags;
        }

        int getToolType() {
            return this.mToolType;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setImeDisplayId(int imeDisplayId) {
            this.mImeDisplayId = imeDisplayId;
        }

        int getImeDisplayId() {
            return this.mImeDisplayId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setRequestedImeVisible(boolean requestedImeVisible) {
            this.mRequestedImeVisible = requestedImeVisible;
        }

        boolean isRequestedImeVisible() {
            return this.mRequestedImeVisible;
        }

        void setRequestImeToken(android.os.IBinder token) {
            this.mRequestImeToken = token;
        }

        android.os.IBinder getRequestImeToken() {
            return this.mRequestImeToken;
        }

        public java.lang.String toString() {
            return "ImeTargetWindowState{ imeToken " + this.mRequestImeToken + " imeFocusChanged " + this.mImeFocusChanged + " hasEditorFocused " + this.mHasFocusedEditor + " requestedImeVisible " + this.mRequestedImeVisible + " imeDisplayId " + this.mImeDisplayId + " softInputModeState " + com.android.internal.inputmethod.InputMethodDebug.softInputModeToString(this.mSoftInputModeState) + " isStartInputByGainFocus " + this.mIsStartInputByGainFocus + "}";
        }
    }
}
