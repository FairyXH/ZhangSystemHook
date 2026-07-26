package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class ZeroJankProxy implements com.android.server.inputmethod.IInputMethodManagerImpl.Callback {
    private final java.util.concurrent.Executor mExecutor;
    private final com.android.server.inputmethod.ZeroJankProxy.Callback mInner;

    interface Callback extends com.android.server.inputmethod.IInputMethodManagerImpl.Callback {
        com.android.server.inputmethod.ClientState getClientStateLocked(com.android.internal.inputmethod.IInputMethodClient iInputMethodClient);

        boolean isInputShownLocked();
    }

    ZeroJankProxy(java.util.concurrent.Executor executor, com.android.server.inputmethod.ZeroJankProxy.Callback inner) {
        this.mInner = inner;
        this.mExecutor = executor;
    }

    private void offload(com.android.internal.util.FunctionalUtils.ThrowingRunnable r) {
        offloadInner(r);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void offload(java.lang.Runnable r) {
        offloadInner(r);
    }

    private void offloadInner(final java.lang.Runnable r) {
        final long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda13
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.inputmethod.ZeroJankProxy.lambda$offloadInner$0(identity, r);
                }
            });
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    static /* synthetic */ void lambda$offloadInner$0(long identity, java.lang.Runnable r) {
        long inner = android.os.Binder.clearCallingIdentity();
        android.os.Binder.restoreCallingIdentity(identity);
        try {
            try {
                r.run();
            } catch (java.lang.Exception e) {
                android.util.Slog.e("InputMethodManagerService", "Error in async IMMS call", e);
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(inner);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addClient$1(com.android.internal.inputmethod.IInputMethodClient client, com.android.internal.inputmethod.IRemoteInputConnection inputConnection, int selfReportedDisplayId) throws java.lang.Exception {
        this.mInner.addClient(client, inputConnection, selfReportedDisplayId);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void addClient(final com.android.internal.inputmethod.IInputMethodClient client, final com.android.internal.inputmethod.IRemoteInputConnection inputConnection, final int selfReportedDisplayId) {
        offload(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda12
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$addClient$1(client, inputConnection, selfReportedDisplayId);
            }
        });
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public android.view.inputmethod.InputMethodInfo getCurrentInputMethodInfoAsUser(int userId) {
        return this.mInner.getCurrentInputMethodInfoAsUser(userId);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public com.android.internal.inputmethod.InputMethodInfoSafeList getInputMethodList(int userId, int directBootAwareness) {
        return this.mInner.getInputMethodList(userId, directBootAwareness);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public com.android.internal.inputmethod.InputMethodInfoSafeList getEnabledInputMethodList(int userId) {
        return this.mInner.getEnabledInputMethodList(userId);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public java.util.List<android.view.inputmethod.InputMethodInfo> getInputMethodListLegacy(int userId, int directBootAwareness) {
        return this.mInner.getInputMethodListLegacy(userId, directBootAwareness);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public java.util.List<android.view.inputmethod.InputMethodInfo> getEnabledInputMethodListLegacy(int userId) {
        return this.mInner.getEnabledInputMethodListLegacy(userId);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public java.util.List<android.view.inputmethod.InputMethodSubtype> getEnabledInputMethodSubtypeList(java.lang.String imiId, boolean allowsImplicitlyEnabledSubtypes, int userId) {
        return this.mInner.getEnabledInputMethodSubtypeList(imiId, allowsImplicitlyEnabledSubtypes, userId);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public android.view.inputmethod.InputMethodSubtype getLastInputMethodSubtype(int userId) {
        return this.mInner.getLastInputMethodSubtype(userId);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public boolean showSoftInput(final com.android.internal.inputmethod.IInputMethodClient client, final android.os.IBinder windowToken, final android.view.inputmethod.ImeTracker.Token statsToken, final int flags, final int lastClickToolType, final android.os.ResultReceiver resultReceiver, final int reason, final boolean async) {
        if (async) {
            offload(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda10
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$showSoftInput$2(client, windowToken, statsToken, flags, lastClickToolType, resultReceiver, reason, async);
                }
            });
            return true;
        }
        java.util.concurrent.CompletableFuture<java.lang.Boolean> future = java.util.concurrent.CompletableFuture.supplyAsync(new java.util.function.Supplier() { // from class: com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda11
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$showSoftInput$3(client, windowToken, statsToken, flags, lastClickToolType, resultReceiver, reason, async);
            }
        }, new com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda4(this));
        return future.completeOnTimeout(false, 1L, java.util.concurrent.TimeUnit.SECONDS).join().booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showSoftInput$2(com.android.internal.inputmethod.IInputMethodClient client, android.os.IBinder windowToken, android.view.inputmethod.ImeTracker.Token statsToken, int flags, int lastClickToolType, android.os.ResultReceiver resultReceiver, int reason, boolean async) throws java.lang.Exception {
        if (!this.mInner.showSoftInput(client, windowToken, statsToken, flags, lastClickToolType, resultReceiver, reason, async)) {
            sendResultReceiverFailure(resultReceiver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$showSoftInput$3(com.android.internal.inputmethod.IInputMethodClient client, android.os.IBinder windowToken, android.view.inputmethod.ImeTracker.Token statsToken, int flags, int lastClickToolType, android.os.ResultReceiver resultReceiver, int reason, boolean async) {
        return java.lang.Boolean.valueOf(this.mInner.showSoftInput(client, windowToken, statsToken, flags, lastClickToolType, resultReceiver, reason, async));
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public boolean hideSoftInput(final com.android.internal.inputmethod.IInputMethodClient client, final android.os.IBinder windowToken, final android.view.inputmethod.ImeTracker.Token statsToken, final int flags, final android.os.ResultReceiver resultReceiver, final int reason, final boolean async) {
        if (async) {
            offload(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda2
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$hideSoftInput$4(client, windowToken, statsToken, flags, resultReceiver, reason, async);
                }
            });
            return true;
        }
        java.util.concurrent.CompletableFuture<java.lang.Boolean> future = java.util.concurrent.CompletableFuture.supplyAsync(new java.util.function.Supplier() { // from class: com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return this.f$0.lambda$hideSoftInput$5(client, windowToken, statsToken, flags, resultReceiver, reason, async);
            }
        }, new com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda4(this));
        return future.completeOnTimeout(false, 1L, java.util.concurrent.TimeUnit.SECONDS).join().booleanValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$hideSoftInput$4(com.android.internal.inputmethod.IInputMethodClient client, android.os.IBinder windowToken, android.view.inputmethod.ImeTracker.Token statsToken, int flags, android.os.ResultReceiver resultReceiver, int reason, boolean async) throws java.lang.Exception {
        if (!this.mInner.hideSoftInput(client, windowToken, statsToken, flags, resultReceiver, reason, async)) {
            sendResultReceiverFailure(resultReceiver);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$hideSoftInput$5(com.android.internal.inputmethod.IInputMethodClient client, android.os.IBinder windowToken, android.view.inputmethod.ImeTracker.Token statsToken, int flags, android.os.ResultReceiver resultReceiver, int reason, boolean async) {
        return java.lang.Boolean.valueOf(this.mInner.hideSoftInput(client, windowToken, statsToken, flags, resultReceiver, reason, async));
    }

    private void sendResultReceiverFailure(android.os.ResultReceiver resultReceiver) {
        boolean isInputShown;
        int i;
        if (resultReceiver == null) {
            return;
        }
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            isInputShown = this.mInner.isInputShownLocked();
        }
        if (isInputShown) {
            i = 0;
        } else {
            i = 1;
        }
        resultReceiver.send(i, null);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void hideSoftInputFromServerForTest() {
        this.mInner.hideSoftInputFromServerForTest();
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void startInputOrWindowGainedFocusAsync(final int startInputReason, final com.android.internal.inputmethod.IInputMethodClient client, final android.os.IBinder windowToken, final int startInputFlags, final int softInputMode, final int windowFlags, final android.view.inputmethod.EditorInfo editorInfo, final com.android.internal.inputmethod.IRemoteInputConnection inputConnection, final com.android.internal.inputmethod.IRemoteAccessibilityInputConnection remoteAccessibilityInputConnection, final int unverifiedTargetSdkVersion, final int userId, final android.window.ImeOnBackInvokedDispatcher imeDispatcher, final int startInputSeq, boolean useAsyncShowHideMethod) {
        offload(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda8
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$startInputOrWindowGainedFocusAsync$6(startInputReason, client, windowToken, startInputFlags, softInputMode, windowFlags, editorInfo, inputConnection, remoteAccessibilityInputConnection, unverifiedTargetSdkVersion, userId, imeDispatcher, startInputSeq);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startInputOrWindowGainedFocusAsync$6(int startInputReason, com.android.internal.inputmethod.IInputMethodClient client, android.os.IBinder windowToken, int startInputFlags, int softInputMode, int windowFlags, android.view.inputmethod.EditorInfo editorInfo, com.android.internal.inputmethod.IRemoteInputConnection inputConnection, com.android.internal.inputmethod.IRemoteAccessibilityInputConnection remoteAccessibilityInputConnection, int unverifiedTargetSdkVersion, int userId, android.window.ImeOnBackInvokedDispatcher imeDispatcher, int startInputSeq) throws java.lang.Exception {
        com.android.internal.inputmethod.InputBindResult result = this.mInner.startInputOrWindowGainedFocus(startInputReason, client, windowToken, startInputFlags, softInputMode, windowFlags, editorInfo, inputConnection, remoteAccessibilityInputConnection, unverifiedTargetSdkVersion, userId, imeDispatcher);
        sendOnStartInputResult(client, result, startInputSeq);
        if (result.result == 1) {
            com.android.server.inputmethod.InputMethodManagerService imms = (com.android.server.inputmethod.InputMethodManagerService) this.mInner;
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                com.android.server.inputmethod.ClientState cs = imms.getClientStateLocked(client);
                if (cs != null) {
                    imms.requestClientSessionLocked(cs);
                    imms.requestClientSessionForAccessibilityLocked(cs);
                }
            }
        }
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public com.android.internal.inputmethod.InputBindResult startInputOrWindowGainedFocus(int startInputReason, com.android.internal.inputmethod.IInputMethodClient client, android.os.IBinder windowToken, int startInputFlags, int softInputMode, int windowFlags, android.view.inputmethod.EditorInfo editorInfo, com.android.internal.inputmethod.IRemoteInputConnection inputConnection, com.android.internal.inputmethod.IRemoteAccessibilityInputConnection remoteAccessibilityInputConnection, int unverifiedTargetSdkVersion, int userId, android.window.ImeOnBackInvokedDispatcher imeDispatcher) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showInputMethodPickerFromClient$7(com.android.internal.inputmethod.IInputMethodClient client, int auxiliarySubtypeMode) throws java.lang.Exception {
        this.mInner.showInputMethodPickerFromClient(client, auxiliarySubtypeMode);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void showInputMethodPickerFromClient(final com.android.internal.inputmethod.IInputMethodClient client, final int auxiliarySubtypeMode) {
        offload(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda6
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$showInputMethodPickerFromClient$7(client, auxiliarySubtypeMode);
            }
        });
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void showInputMethodPickerFromSystem(int auxiliarySubtypeMode, int displayId) {
        this.mInner.showInputMethodPickerFromSystem(auxiliarySubtypeMode, displayId);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public boolean isInputMethodPickerShownForTest() {
        return this.mInner.isInputMethodPickerShownForTest();
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public android.view.inputmethod.InputMethodSubtype getCurrentInputMethodSubtype(int userId) {
        return this.mInner.getCurrentInputMethodSubtype(userId);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void setAdditionalInputMethodSubtypes(java.lang.String imiId, android.view.inputmethod.InputMethodSubtype[] subtypes, int userId) {
        this.mInner.setAdditionalInputMethodSubtypes(imiId, subtypes, userId);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void setExplicitlyEnabledInputMethodSubtypes(java.lang.String imeId, int[] subtypeHashCodes, int userId) {
        this.mInner.setExplicitlyEnabledInputMethodSubtypes(imeId, subtypeHashCodes, userId);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public int getInputMethodWindowVisibleHeight(com.android.internal.inputmethod.IInputMethodClient client) {
        return this.mInner.getInputMethodWindowVisibleHeight(client);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void reportPerceptibleAsync(android.os.IBinder windowToken, boolean perceptible) {
        this.mInner.reportPerceptibleAsync(windowToken, perceptible);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void removeImeSurface() {
        this.mInner.removeImeSurface();
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void removeImeSurfaceFromWindowAsync(android.os.IBinder windowToken) {
        this.mInner.removeImeSurfaceFromWindowAsync(windowToken);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void startProtoDump(byte[] bytes, int i, java.lang.String s) {
        this.mInner.startProtoDump(bytes, i, s);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public boolean isImeTraceEnabled() {
        return this.mInner.isImeTraceEnabled();
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void startImeTrace() {
        this.mInner.startImeTrace();
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void stopImeTrace() {
        this.mInner.stopImeTrace();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startStylusHandwriting$8(com.android.internal.inputmethod.IInputMethodClient client) throws java.lang.Exception {
        this.mInner.startStylusHandwriting(client);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void startStylusHandwriting(final com.android.internal.inputmethod.IInputMethodClient client) {
        offload(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda7
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$startStylusHandwriting$8(client);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startConnectionlessStylusHandwriting$9(com.android.internal.inputmethod.IInputMethodClient client, int userId, android.view.inputmethod.CursorAnchorInfo cursorAnchorInfo, java.lang.String delegatePackageName, java.lang.String delegatorPackageName, com.android.internal.inputmethod.IConnectionlessHandwritingCallback callback) throws java.lang.Exception {
        this.mInner.startConnectionlessStylusHandwriting(client, userId, cursorAnchorInfo, delegatePackageName, delegatorPackageName, callback);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void startConnectionlessStylusHandwriting(final com.android.internal.inputmethod.IInputMethodClient client, final int userId, final android.view.inputmethod.CursorAnchorInfo cursorAnchorInfo, final java.lang.String delegatePackageName, final java.lang.String delegatorPackageName, final com.android.internal.inputmethod.IConnectionlessHandwritingCallback callback) {
        offload(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda0
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$startConnectionlessStylusHandwriting$9(client, userId, cursorAnchorInfo, delegatePackageName, delegatorPackageName, callback);
            }
        });
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public boolean acceptStylusHandwritingDelegation(final com.android.internal.inputmethod.IInputMethodClient client, final int userId, final java.lang.String delegatePackageName, final java.lang.String delegatorPackageName, final int flags) {
        try {
            return ((java.lang.Boolean) java.util.concurrent.CompletableFuture.supplyAsync(new java.util.function.Supplier() { // from class: com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda9
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return this.f$0.lambda$acceptStylusHandwritingDelegation$10(client, userId, delegatePackageName, delegatorPackageName, flags);
                }
            }, new com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda4(this)).get()).booleanValue();
        } catch (java.lang.InterruptedException e) {
            throw new java.lang.RuntimeException(e);
        } catch (java.util.concurrent.ExecutionException e2) {
            throw new java.lang.RuntimeException(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$acceptStylusHandwritingDelegation$10(com.android.internal.inputmethod.IInputMethodClient client, int userId, java.lang.String delegatePackageName, java.lang.String delegatorPackageName, int flags) {
        return java.lang.Boolean.valueOf(this.mInner.acceptStylusHandwritingDelegation(client, userId, delegatePackageName, delegatorPackageName, flags));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$acceptStylusHandwritingDelegationAsync$11(com.android.internal.inputmethod.IInputMethodClient client, int userId, java.lang.String delegatePackageName, java.lang.String delegatorPackageName, int flags, com.android.internal.inputmethod.IBooleanListener callback) throws java.lang.Exception {
        this.mInner.acceptStylusHandwritingDelegationAsync(client, userId, delegatePackageName, delegatorPackageName, flags, callback);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void acceptStylusHandwritingDelegationAsync(final com.android.internal.inputmethod.IInputMethodClient client, final int userId, final java.lang.String delegatePackageName, final java.lang.String delegatorPackageName, final int flags, final com.android.internal.inputmethod.IBooleanListener callback) {
        offload(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda1
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$acceptStylusHandwritingDelegationAsync$11(client, userId, delegatePackageName, delegatorPackageName, flags, callback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$prepareStylusHandwritingDelegation$12(com.android.internal.inputmethod.IInputMethodClient client, int userId, java.lang.String delegatePackageName, java.lang.String delegatorPackageName) throws java.lang.Exception {
        this.mInner.prepareStylusHandwritingDelegation(client, userId, delegatePackageName, delegatorPackageName);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void prepareStylusHandwritingDelegation(final com.android.internal.inputmethod.IInputMethodClient client, final int userId, final java.lang.String delegatePackageName, final java.lang.String delegatorPackageName) {
        offload(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.inputmethod.ZeroJankProxy$$ExternalSyntheticLambda5
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$prepareStylusHandwritingDelegation$12(client, userId, delegatePackageName, delegatorPackageName);
            }
        });
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public boolean isStylusHandwritingAvailableAsUser(int userId, boolean connectionless) {
        return this.mInner.isStylusHandwritingAvailableAsUser(userId, connectionless);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void addVirtualStylusIdForTestSession(com.android.internal.inputmethod.IInputMethodClient client) {
        this.mInner.addVirtualStylusIdForTestSession(client);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void setStylusWindowIdleTimeoutForTest(com.android.internal.inputmethod.IInputMethodClient client, long timeout) {
        this.mInner.setStylusWindowIdleTimeoutForTest(client, timeout);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public com.android.internal.inputmethod.IImeTracker getImeTrackerService() {
        return this.mInner.getImeTrackerService();
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver, android.os.Binder self) {
        this.mInner.onShellCommand(in, out, err, args, callback, resultReceiver, self);
    }

    @Override // com.android.server.inputmethod.IInputMethodManagerImpl.Callback
    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter fout, java.lang.String[] args) {
        this.mInner.dump(fd, fout, args);
    }

    private void sendOnStartInputResult(com.android.internal.inputmethod.IInputMethodClient client, com.android.internal.inputmethod.InputBindResult res, int startInputSeq) {
        synchronized (com.android.server.inputmethod.ImfLock.class) {
            com.android.server.inputmethod.ClientState cs = this.mInner.getClientStateLocked(client);
            if (cs != null && cs.mClient != null) {
                cs.mClient.onStartInputResult(res, startInputSeq);
            } else {
                android.util.Slog.i("InputMethodManagerService", "Client that requested startInputOrWindowGainedFocus is no longer bound. InputBindResult: " + res + " for startInputSeq: " + startInputSeq);
            }
        }
    }
}
