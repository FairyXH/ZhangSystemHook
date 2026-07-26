package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class IInputMethodManagerImpl extends com.android.internal.view.IInputMethodManager.Stub {
    private final com.android.server.inputmethod.IInputMethodManagerImpl.Callback mCallback;

    interface Callback {
        boolean acceptStylusHandwritingDelegation(com.android.internal.inputmethod.IInputMethodClient iInputMethodClient, int i, java.lang.String str, java.lang.String str2, int i2);

        void acceptStylusHandwritingDelegationAsync(com.android.internal.inputmethod.IInputMethodClient iInputMethodClient, int i, java.lang.String str, java.lang.String str2, int i2, com.android.internal.inputmethod.IBooleanListener iBooleanListener);

        void addClient(com.android.internal.inputmethod.IInputMethodClient iInputMethodClient, com.android.internal.inputmethod.IRemoteInputConnection iRemoteInputConnection, int i);

        void addVirtualStylusIdForTestSession(com.android.internal.inputmethod.IInputMethodClient iInputMethodClient);

        void dump(java.io.FileDescriptor fileDescriptor, java.io.PrintWriter printWriter, java.lang.String[] strArr);

        android.view.inputmethod.InputMethodInfo getCurrentInputMethodInfoAsUser(int i);

        android.view.inputmethod.InputMethodSubtype getCurrentInputMethodSubtype(int i);

        com.android.internal.inputmethod.InputMethodInfoSafeList getEnabledInputMethodList(int i);

        java.util.List<android.view.inputmethod.InputMethodInfo> getEnabledInputMethodListLegacy(int i);

        java.util.List<android.view.inputmethod.InputMethodSubtype> getEnabledInputMethodSubtypeList(java.lang.String str, boolean z, int i);

        com.android.internal.inputmethod.IImeTracker getImeTrackerService();

        com.android.internal.inputmethod.InputMethodInfoSafeList getInputMethodList(int i, int i2);

        java.util.List<android.view.inputmethod.InputMethodInfo> getInputMethodListLegacy(int i, int i2);

        int getInputMethodWindowVisibleHeight(com.android.internal.inputmethod.IInputMethodClient iInputMethodClient);

        android.view.inputmethod.InputMethodSubtype getLastInputMethodSubtype(int i);

        boolean hideSoftInput(com.android.internal.inputmethod.IInputMethodClient iInputMethodClient, android.os.IBinder iBinder, android.view.inputmethod.ImeTracker.Token token, int i, android.os.ResultReceiver resultReceiver, int i2, boolean z);

        void hideSoftInputFromServerForTest();

        boolean isImeTraceEnabled();

        boolean isInputMethodPickerShownForTest();

        boolean isStylusHandwritingAvailableAsUser(int i, boolean z);

        void onShellCommand(java.io.FileDescriptor fileDescriptor, java.io.FileDescriptor fileDescriptor2, java.io.FileDescriptor fileDescriptor3, java.lang.String[] strArr, android.os.ShellCallback shellCallback, android.os.ResultReceiver resultReceiver, android.os.Binder binder);

        void prepareStylusHandwritingDelegation(com.android.internal.inputmethod.IInputMethodClient iInputMethodClient, int i, java.lang.String str, java.lang.String str2);

        void removeImeSurface();

        void removeImeSurfaceFromWindowAsync(android.os.IBinder iBinder);

        void reportPerceptibleAsync(android.os.IBinder iBinder, boolean z);

        void setAdditionalInputMethodSubtypes(java.lang.String str, android.view.inputmethod.InputMethodSubtype[] inputMethodSubtypeArr, int i);

        void setExplicitlyEnabledInputMethodSubtypes(java.lang.String str, int[] iArr, int i);

        void setStylusWindowIdleTimeoutForTest(com.android.internal.inputmethod.IInputMethodClient iInputMethodClient, long j);

        void showInputMethodPickerFromClient(com.android.internal.inputmethod.IInputMethodClient iInputMethodClient, int i);

        void showInputMethodPickerFromSystem(int i, int i2);

        boolean showSoftInput(com.android.internal.inputmethod.IInputMethodClient iInputMethodClient, android.os.IBinder iBinder, android.view.inputmethod.ImeTracker.Token token, int i, int i2, android.os.ResultReceiver resultReceiver, int i3, boolean z);

        void startConnectionlessStylusHandwriting(com.android.internal.inputmethod.IInputMethodClient iInputMethodClient, int i, android.view.inputmethod.CursorAnchorInfo cursorAnchorInfo, java.lang.String str, java.lang.String str2, com.android.internal.inputmethod.IConnectionlessHandwritingCallback iConnectionlessHandwritingCallback);

        void startImeTrace();

        com.android.internal.inputmethod.InputBindResult startInputOrWindowGainedFocus(int i, com.android.internal.inputmethod.IInputMethodClient iInputMethodClient, android.os.IBinder iBinder, int i2, int i3, int i4, android.view.inputmethod.EditorInfo editorInfo, com.android.internal.inputmethod.IRemoteInputConnection iRemoteInputConnection, com.android.internal.inputmethod.IRemoteAccessibilityInputConnection iRemoteAccessibilityInputConnection, int i5, int i6, android.window.ImeOnBackInvokedDispatcher imeOnBackInvokedDispatcher);

        void startInputOrWindowGainedFocusAsync(int i, com.android.internal.inputmethod.IInputMethodClient iInputMethodClient, android.os.IBinder iBinder, int i2, int i3, int i4, android.view.inputmethod.EditorInfo editorInfo, com.android.internal.inputmethod.IRemoteInputConnection iRemoteInputConnection, com.android.internal.inputmethod.IRemoteAccessibilityInputConnection iRemoteAccessibilityInputConnection, int i5, int i6, android.window.ImeOnBackInvokedDispatcher imeOnBackInvokedDispatcher, int i7, boolean z);

        void startProtoDump(byte[] bArr, int i, java.lang.String str);

        void startStylusHandwriting(com.android.internal.inputmethod.IInputMethodClient iInputMethodClient);

        void stopImeTrace();
    }

    @java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD})
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface PermissionVerified {
        java.lang.String value() default "";
    }

    private IInputMethodManagerImpl(com.android.server.inputmethod.IInputMethodManagerImpl.Callback callback) {
        this.mCallback = callback;
    }

    static com.android.server.inputmethod.IInputMethodManagerImpl create(com.android.server.inputmethod.IInputMethodManagerImpl.Callback callback) {
        return new com.android.server.inputmethod.IInputMethodManagerImpl(callback);
    }

    public void addClient(com.android.internal.inputmethod.IInputMethodClient client, com.android.internal.inputmethod.IRemoteInputConnection inputmethod, int untrustedDisplayId) {
        this.mCallback.addClient(client, inputmethod, untrustedDisplayId);
    }

    public android.view.inputmethod.InputMethodInfo getCurrentInputMethodInfoAsUser(int userId) {
        return this.mCallback.getCurrentInputMethodInfoAsUser(userId);
    }

    public com.android.internal.inputmethod.InputMethodInfoSafeList getInputMethodList(int userId, int directBootAwareness) {
        return this.mCallback.getInputMethodList(userId, directBootAwareness);
    }

    public com.android.internal.inputmethod.InputMethodInfoSafeList getEnabledInputMethodList(int userId) {
        return this.mCallback.getEnabledInputMethodList(userId);
    }

    public java.util.List<android.view.inputmethod.InputMethodInfo> getInputMethodListLegacy(int userId, int directBootAwareness) {
        return this.mCallback.getInputMethodListLegacy(userId, directBootAwareness);
    }

    public java.util.List<android.view.inputmethod.InputMethodInfo> getEnabledInputMethodListLegacy(int userId) {
        return this.mCallback.getEnabledInputMethodListLegacy(userId);
    }

    public java.util.List<android.view.inputmethod.InputMethodSubtype> getEnabledInputMethodSubtypeList(java.lang.String imiId, boolean allowsImplicitlyEnabledSubtypes, int userId) {
        return this.mCallback.getEnabledInputMethodSubtypeList(imiId, allowsImplicitlyEnabledSubtypes, userId);
    }

    public android.view.inputmethod.InputMethodSubtype getLastInputMethodSubtype(int userId) {
        return this.mCallback.getLastInputMethodSubtype(userId);
    }

    public boolean showSoftInput(com.android.internal.inputmethod.IInputMethodClient client, android.os.IBinder windowToken, android.view.inputmethod.ImeTracker.Token statsToken, int flags, int lastClickToolType, android.os.ResultReceiver resultReceiver, int reason, boolean async) {
        return this.mCallback.showSoftInput(client, windowToken, statsToken, flags, lastClickToolType, resultReceiver, reason, async);
    }

    public boolean hideSoftInput(com.android.internal.inputmethod.IInputMethodClient client, android.os.IBinder windowToken, android.view.inputmethod.ImeTracker.Token statsToken, int flags, android.os.ResultReceiver resultReceiver, int reason, boolean async) {
        return this.mCallback.hideSoftInput(client, windowToken, statsToken, flags, resultReceiver, reason, async);
    }

    public void hideSoftInputFromServerForTest() {
        super.hideSoftInputFromServerForTest_enforcePermission();
        this.mCallback.hideSoftInputFromServerForTest();
    }

    public com.android.internal.inputmethod.InputBindResult startInputOrWindowGainedFocus(int startInputReason, com.android.internal.inputmethod.IInputMethodClient client, android.os.IBinder windowToken, int startInputFlags, int softInputMode, int windowFlags, android.view.inputmethod.EditorInfo editorInfo, com.android.internal.inputmethod.IRemoteInputConnection inputConnection, com.android.internal.inputmethod.IRemoteAccessibilityInputConnection remoteAccessibilityInputConnection, int unverifiedTargetSdkVersion, int userId, android.window.ImeOnBackInvokedDispatcher imeDispatcher) {
        return this.mCallback.startInputOrWindowGainedFocus(startInputReason, client, windowToken, startInputFlags, softInputMode, windowFlags, editorInfo, inputConnection, remoteAccessibilityInputConnection, unverifiedTargetSdkVersion, userId, imeDispatcher);
    }

    public void startInputOrWindowGainedFocusAsync(int startInputReason, com.android.internal.inputmethod.IInputMethodClient client, android.os.IBinder windowToken, int startInputFlags, int softInputMode, int windowFlags, android.view.inputmethod.EditorInfo editorInfo, com.android.internal.inputmethod.IRemoteInputConnection inputConnection, com.android.internal.inputmethod.IRemoteAccessibilityInputConnection remoteAccessibilityInputConnection, int unverifiedTargetSdkVersion, int userId, android.window.ImeOnBackInvokedDispatcher imeDispatcher, int startInputSeq, boolean useAsyncShowHideMethod) {
        this.mCallback.startInputOrWindowGainedFocusAsync(startInputReason, client, windowToken, startInputFlags, softInputMode, windowFlags, editorInfo, inputConnection, remoteAccessibilityInputConnection, unverifiedTargetSdkVersion, userId, imeDispatcher, startInputSeq, useAsyncShowHideMethod);
    }

    public void showInputMethodPickerFromClient(com.android.internal.inputmethod.IInputMethodClient client, int auxiliarySubtypeMode) {
        this.mCallback.showInputMethodPickerFromClient(client, auxiliarySubtypeMode);
    }

    public void showInputMethodPickerFromSystem(int auxiliarySubtypeMode, int displayId) {
        super.showInputMethodPickerFromSystem_enforcePermission();
        this.mCallback.showInputMethodPickerFromSystem(auxiliarySubtypeMode, displayId);
    }

    public boolean isInputMethodPickerShownForTest() {
        super.isInputMethodPickerShownForTest_enforcePermission();
        return this.mCallback.isInputMethodPickerShownForTest();
    }

    public android.view.inputmethod.InputMethodSubtype getCurrentInputMethodSubtype(int userId) {
        return this.mCallback.getCurrentInputMethodSubtype(userId);
    }

    public void setAdditionalInputMethodSubtypes(java.lang.String id, android.view.inputmethod.InputMethodSubtype[] subtypes, int userId) {
        this.mCallback.setAdditionalInputMethodSubtypes(id, subtypes, userId);
    }

    public void setExplicitlyEnabledInputMethodSubtypes(java.lang.String imeId, int[] subtypeHashCodes, int userId) {
        this.mCallback.setExplicitlyEnabledInputMethodSubtypes(imeId, subtypeHashCodes, userId);
    }

    public int getInputMethodWindowVisibleHeight(com.android.internal.inputmethod.IInputMethodClient client) {
        return this.mCallback.getInputMethodWindowVisibleHeight(client);
    }

    public void reportPerceptibleAsync(android.os.IBinder windowToken, boolean perceptible) {
        this.mCallback.reportPerceptibleAsync(windowToken, perceptible);
    }

    public void removeImeSurface() {
        super.removeImeSurface_enforcePermission();
        this.mCallback.removeImeSurface();
    }

    public void removeImeSurfaceFromWindowAsync(android.os.IBinder windowToken) {
        this.mCallback.removeImeSurfaceFromWindowAsync(windowToken);
    }

    public void startProtoDump(byte[] protoDump, int source, java.lang.String where) {
        this.mCallback.startProtoDump(protoDump, source, where);
    }

    public boolean isImeTraceEnabled() {
        return this.mCallback.isImeTraceEnabled();
    }

    public void startImeTrace() {
        super.startImeTrace_enforcePermission();
        this.mCallback.startImeTrace();
    }

    public void stopImeTrace() {
        super.stopImeTrace_enforcePermission();
        this.mCallback.stopImeTrace();
    }

    public void startStylusHandwriting(com.android.internal.inputmethod.IInputMethodClient client) {
        this.mCallback.startStylusHandwriting(client);
    }

    public void startConnectionlessStylusHandwriting(com.android.internal.inputmethod.IInputMethodClient client, int userId, android.view.inputmethod.CursorAnchorInfo cursorAnchorInfo, java.lang.String delegatePackageName, java.lang.String delegatorPackageName, com.android.internal.inputmethod.IConnectionlessHandwritingCallback callback) {
        this.mCallback.startConnectionlessStylusHandwriting(client, userId, cursorAnchorInfo, delegatePackageName, delegatorPackageName, callback);
    }

    public void prepareStylusHandwritingDelegation(com.android.internal.inputmethod.IInputMethodClient client, int userId, java.lang.String delegatePackageName, java.lang.String delegatorPackageName) {
        this.mCallback.prepareStylusHandwritingDelegation(client, userId, delegatePackageName, delegatorPackageName);
    }

    public boolean acceptStylusHandwritingDelegation(com.android.internal.inputmethod.IInputMethodClient client, int userId, java.lang.String delegatePackageName, java.lang.String delegatorPackageName, int flags) {
        return this.mCallback.acceptStylusHandwritingDelegation(client, userId, delegatePackageName, delegatorPackageName, flags);
    }

    public void acceptStylusHandwritingDelegationAsync(com.android.internal.inputmethod.IInputMethodClient client, int userId, java.lang.String delegatePackageName, java.lang.String delegatorPackageName, int flags, com.android.internal.inputmethod.IBooleanListener callback) {
        this.mCallback.acceptStylusHandwritingDelegationAsync(client, userId, delegatePackageName, delegatorPackageName, flags, callback);
    }

    public boolean isStylusHandwritingAvailableAsUser(int userId, boolean connectionless) {
        return this.mCallback.isStylusHandwritingAvailableAsUser(userId, connectionless);
    }

    public void addVirtualStylusIdForTestSession(com.android.internal.inputmethod.IInputMethodClient client) {
        super.addVirtualStylusIdForTestSession_enforcePermission();
        this.mCallback.addVirtualStylusIdForTestSession(client);
    }

    public void setStylusWindowIdleTimeoutForTest(com.android.internal.inputmethod.IInputMethodClient client, long timeout) {
        super.setStylusWindowIdleTimeoutForTest_enforcePermission();
        this.mCallback.setStylusWindowIdleTimeoutForTest(client, timeout);
    }

    public com.android.internal.inputmethod.IImeTracker getImeTrackerService() {
        return this.mCallback.getImeTrackerService();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
        this.mCallback.onShellCommand(in, out, err, args, callback, resultReceiver, this);
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        this.mCallback.dump(fd, pw, args);
    }
}
