package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class IInputMethodClientInvoker {
    private static boolean DEBUG = com.android.server.inputmethod.InputMethodManagerService.DEBUG;
    private static final java.lang.String TAG = "InputMethodManagerService";
    private final android.os.Handler mHandler;
    private final boolean mIsProxy;
    private final com.android.internal.inputmethod.IInputMethodClient mTarget;

    static com.android.server.inputmethod.IInputMethodClientInvoker create(com.android.internal.inputmethod.IInputMethodClient inputMethodClient, android.os.Handler handler) {
        if (inputMethodClient == null) {
            return null;
        }
        boolean isProxy = android.os.Binder.isProxy(inputMethodClient);
        return new com.android.server.inputmethod.IInputMethodClientInvoker(inputMethodClient, isProxy, isProxy ? null : handler);
    }

    static com.android.server.inputmethod.IInputMethodClientInvoker create$ravenwood(com.android.internal.inputmethod.IInputMethodClient inputMethodClient, android.os.Handler handler) {
        if (inputMethodClient == null) {
            return null;
        }
        return new com.android.server.inputmethod.IInputMethodClientInvoker(inputMethodClient, true, null);
    }

    private IInputMethodClientInvoker(com.android.internal.inputmethod.IInputMethodClient target, boolean isProxy, android.os.Handler handler) {
        this.mTarget = target;
        this.mIsProxy = isProxy;
        this.mHandler = handler;
    }

    private static java.lang.String getCallerMethodName() {
        java.lang.StackTraceElement[] callStack = java.lang.Thread.currentThread().getStackTrace();
        if (callStack.length <= 4) {
            return "<bottom of call stack>";
        }
        return callStack[4].getMethodName();
    }

    private static void logRemoteException(android.os.RemoteException e) {
        if (DEBUG || !(e instanceof android.os.DeadObjectException)) {
            android.util.Slog.w(TAG, "IPC failed at IInputMethodClientInvoker#" + getCallerMethodName(), e);
        }
    }

    void onBindMethod(final com.android.internal.inputmethod.InputBindResult res) {
        if (this.mIsProxy) {
            lambda$onBindMethod$0(res);
        } else {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onBindMethod$0(res);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onBindMethodInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$onBindMethod$0(com.android.internal.inputmethod.InputBindResult res) {
        try {
            try {
                this.mTarget.onBindMethod(res);
            } catch (android.os.RemoteException e) {
                logRemoteException(e);
                if (res.channel != null && this.mIsProxy) {
                }
            }
            if (res.channel != null && this.mIsProxy) {
                res.channel.dispose();
            }
        } catch (java.lang.Throwable th) {
            if (res.channel != null && this.mIsProxy) {
                res.channel.dispose();
            }
            throw th;
        }
    }

    void onStartInputResult(final com.android.internal.inputmethod.InputBindResult res, final int startInputSeq) {
        if (this.mIsProxy) {
            lambda$onStartInputResult$1(res, startInputSeq);
        } else {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onStartInputResult$1(res, startInputSeq);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onStartInputResultInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$onStartInputResult$1(com.android.internal.inputmethod.InputBindResult res, int startInputSeq) {
        try {
            try {
                this.mTarget.onStartInputResult(res, startInputSeq);
            } catch (android.os.RemoteException e) {
                logRemoteException(e);
                if (res.channel != null && this.mIsProxy) {
                }
            }
            if (res.channel != null && this.mIsProxy) {
                res.channel.dispose();
            }
        } catch (java.lang.Throwable th) {
            if (res.channel != null && this.mIsProxy) {
                res.channel.dispose();
            }
            throw th;
        }
    }

    void onBindAccessibilityService(final com.android.internal.inputmethod.InputBindResult res, final int id) {
        if (this.mIsProxy) {
            lambda$onBindAccessibilityService$2(res, id);
        } else {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onBindAccessibilityService$2(res, id);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onBindAccessibilityServiceInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$onBindAccessibilityService$2(com.android.internal.inputmethod.InputBindResult res, int id) {
        try {
            try {
                this.mTarget.onBindAccessibilityService(res, id);
            } catch (android.os.RemoteException e) {
                logRemoteException(e);
                if (res.channel != null && this.mIsProxy) {
                }
            }
            if (res.channel != null && this.mIsProxy) {
                res.channel.dispose();
            }
        } catch (java.lang.Throwable th) {
            if (res.channel != null && this.mIsProxy) {
                res.channel.dispose();
            }
            throw th;
        }
    }

    void onUnbindMethod(final int sequence, final int unbindReason) {
        if (this.mIsProxy) {
            lambda$onUnbindMethod$3(sequence, unbindReason);
        } else {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUnbindMethod$3(sequence, unbindReason);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onUnbindMethodInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$onUnbindMethod$3(int sequence, int unbindReason) {
        try {
            this.mTarget.onUnbindMethod(sequence, unbindReason);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void onUnbindAccessibilityService(final int sequence, final int id) {
        if (this.mIsProxy) {
            lambda$onUnbindAccessibilityService$4(sequence, id);
        } else {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onUnbindAccessibilityService$4(sequence, id);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: onUnbindAccessibilityServiceInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$onUnbindAccessibilityService$4(int sequence, int id) {
        try {
            this.mTarget.onUnbindAccessibilityService(sequence, id);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void setActive(final boolean active, final boolean fullscreen) {
        if (this.mIsProxy) {
            lambda$setActive$5(active, fullscreen);
        } else {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda8
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setActive$5(active, fullscreen);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: setActiveInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$setActive$5(boolean active, boolean fullscreen) {
        try {
            this.mTarget.setActive(active, fullscreen);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void setInteractive(final boolean interactive, final boolean fullscreen) {
        if (this.mIsProxy) {
            lambda$setInteractive$6(interactive, fullscreen);
        } else {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setInteractive$6(interactive, fullscreen);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: setInteractiveInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$setInteractive$6(boolean interactive, boolean fullscreen) {
        try {
            this.mTarget.setInteractive(interactive, fullscreen);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void setImeVisibility(final boolean visible) {
        if (this.mIsProxy) {
            lambda$setImeVisibility$7(visible);
        } else {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setImeVisibility$7(visible);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: setImeVisibilityInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$setImeVisibility$7(boolean visible) {
        try {
            this.mTarget.setImeVisibility(visible);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void scheduleStartInputIfNecessary(final boolean fullscreen) {
        if (this.mIsProxy) {
            lambda$scheduleStartInputIfNecessary$8(fullscreen);
        } else {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda10
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$scheduleStartInputIfNecessary$8(fullscreen);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: scheduleStartInputIfNecessaryInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$scheduleStartInputIfNecessary$8(boolean fullscreen) {
        try {
            this.mTarget.scheduleStartInputIfNecessary(fullscreen);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void reportFullscreenMode(final boolean fullscreen) {
        if (this.mIsProxy) {
            lambda$reportFullscreenMode$9(fullscreen);
        } else {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$reportFullscreenMode$9(fullscreen);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: reportFullscreenModeInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$reportFullscreenMode$9(boolean fullscreen) {
        try {
            this.mTarget.reportFullscreenMode(fullscreen);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setImeTraceEnabled(final boolean enabled) {
        if (this.mIsProxy) {
            lambda$setImeTraceEnabled$10(enabled);
        } else {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda11
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setImeTraceEnabled$10(enabled);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: setImeTraceEnabledInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$setImeTraceEnabled$10(boolean enabled) {
        try {
            this.mTarget.setImeTraceEnabled(enabled);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void throwExceptionFromSystem(final java.lang.String message) {
        if (this.mIsProxy) {
            lambda$throwExceptionFromSystem$11(message);
        } else {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.inputmethod.IInputMethodClientInvoker$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$throwExceptionFromSystem$11(message);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: throwExceptionFromSystemInternal, reason: merged with bridge method [inline-methods] */
    public void lambda$throwExceptionFromSystem$11(java.lang.String message) {
        try {
            this.mTarget.throwExceptionFromSystem(message);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    android.os.IBinder asBinder() {
        return this.mTarget.asBinder();
    }
}
