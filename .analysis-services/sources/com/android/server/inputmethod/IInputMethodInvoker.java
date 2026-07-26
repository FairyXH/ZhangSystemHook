package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class IInputMethodInvoker {
    private static boolean DEBUG = com.android.server.inputmethod.InputMethodManagerService.DEBUG;
    private static final java.lang.String TAG = "InputMethodManagerService";
    private final com.android.internal.inputmethod.IInputMethod mTarget;

    static com.android.server.inputmethod.IInputMethodInvoker create(com.android.internal.inputmethod.IInputMethod inputMethod) {
        if (inputMethod == null) {
            return null;
        }
        if (!android.os.Binder.isProxy(inputMethod)) {
            throw new java.lang.UnsupportedOperationException(inputMethod + " must have been a BinderProxy.");
        }
        return new com.android.server.inputmethod.IInputMethodInvoker(inputMethod);
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
            android.util.Slog.w(TAG, "IPC failed at IInputMethodInvoker#" + getCallerMethodName(), e);
        }
    }

    static int getBinderIdentityHashCode(com.android.server.inputmethod.IInputMethodInvoker obj) {
        if (obj == null) {
            return 0;
        }
        return java.lang.System.identityHashCode(obj.mTarget);
    }

    private IInputMethodInvoker(com.android.internal.inputmethod.IInputMethod target) {
        this.mTarget = target;
    }

    android.os.IBinder asBinder() {
        return this.mTarget.asBinder();
    }

    void initializeInternal(android.os.IBinder token, com.android.internal.inputmethod.IInputMethodPrivilegedOperations privilegedOperations, int navigationBarFlags) {
        com.android.internal.inputmethod.IInputMethod.InitParams params = new com.android.internal.inputmethod.IInputMethod.InitParams();
        params.token = token;
        params.privilegedOperations = privilegedOperations;
        params.navigationBarFlags = navigationBarFlags;
        try {
            this.mTarget.initializeInternal(params);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void onCreateInlineSuggestionsRequest(com.android.internal.inputmethod.InlineSuggestionsRequestInfo requestInfo, com.android.internal.inputmethod.IInlineSuggestionsRequestCallback cb) {
        try {
            this.mTarget.onCreateInlineSuggestionsRequest(requestInfo, cb);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void bindInput(android.view.inputmethod.InputBinding binding) {
        try {
            this.mTarget.bindInput(binding);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void unbindInput() {
        try {
            this.mTarget.unbindInput();
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void startInput(android.os.IBinder startInputToken, com.android.internal.inputmethod.IRemoteInputConnection remoteInputConnection, android.view.inputmethod.EditorInfo editorInfo, boolean restarting, int navButtonFlags, android.window.ImeOnBackInvokedDispatcher imeDispatcher) {
        com.android.internal.inputmethod.IInputMethod.StartInputParams params = new com.android.internal.inputmethod.IInputMethod.StartInputParams();
        params.startInputToken = startInputToken;
        params.remoteInputConnection = remoteInputConnection;
        params.editorInfo = editorInfo;
        params.restarting = restarting;
        params.navigationBarFlags = navButtonFlags;
        params.imeDispatcher = imeDispatcher;
        try {
            this.mTarget.startInput(params);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void onNavButtonFlagsChanged(int navButtonFlags) {
        try {
            this.mTarget.onNavButtonFlagsChanged(navButtonFlags);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void createSession(android.view.InputChannel channel, com.android.internal.inputmethod.IInputMethodSessionCallback callback) {
        try {
            this.mTarget.createSession(channel, callback);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void setSessionEnabled(com.android.internal.inputmethod.IInputMethodSession session, boolean enabled) {
        try {
            this.mTarget.setSessionEnabled(session, enabled);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    boolean showSoftInput(android.os.IBinder showInputToken, android.view.inputmethod.ImeTracker.Token statsToken, int flags, android.os.ResultReceiver resultReceiver) {
        try {
            this.mTarget.showSoftInput(showInputToken, statsToken, flags, resultReceiver);
            return true;
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
            return false;
        }
    }

    boolean hideSoftInput(android.os.IBinder hideInputToken, android.view.inputmethod.ImeTracker.Token statsToken, int flags, android.os.ResultReceiver resultReceiver) {
        try {
            this.mTarget.hideSoftInput(hideInputToken, statsToken, flags, resultReceiver);
            return true;
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
            return false;
        }
    }

    void updateEditorToolType(int toolType) {
        try {
            this.mTarget.updateEditorToolType(toolType);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void changeInputMethodSubtype(android.view.inputmethod.InputMethodSubtype subtype) {
        try {
            this.mTarget.changeInputMethodSubtype(subtype);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void canStartStylusHandwriting(int requestId, com.android.internal.inputmethod.IConnectionlessHandwritingCallback connectionlessCallback, android.view.inputmethod.CursorAnchorInfo cursorAnchorInfo, boolean isConnectionlessForDelegation) {
        try {
            this.mTarget.canStartStylusHandwriting(requestId, connectionlessCallback, cursorAnchorInfo, isConnectionlessForDelegation);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    boolean startStylusHandwriting(int requestId, android.view.InputChannel channel, java.util.List<android.view.MotionEvent> events) {
        try {
            this.mTarget.startStylusHandwriting(requestId, channel, events);
            return true;
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
            return false;
        }
    }

    void commitHandwritingDelegationTextIfAvailable() {
        try {
            this.mTarget.commitHandwritingDelegationTextIfAvailable();
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void discardHandwritingDelegationText() {
        try {
            this.mTarget.discardHandwritingDelegationText();
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void initInkWindow() {
        try {
            this.mTarget.initInkWindow();
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void finishStylusHandwriting() {
        try {
            this.mTarget.finishStylusHandwriting();
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void removeStylusHandwritingWindow() {
        try {
            this.mTarget.removeStylusHandwritingWindow();
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }

    void setStylusWindowIdleTimeoutForTest(long timeout) {
        try {
            this.mTarget.setStylusWindowIdleTimeoutForTest(timeout);
        } catch (android.os.RemoteException e) {
            logRemoteException(e);
        }
    }
}
