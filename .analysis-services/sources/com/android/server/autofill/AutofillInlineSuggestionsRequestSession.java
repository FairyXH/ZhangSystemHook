package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
final class AutofillInlineSuggestionsRequestSession {
    private static final java.lang.String TAG = com.android.server.autofill.AutofillInlineSuggestionsRequestSession.class.getSimpleName();
    private android.view.autofill.AutofillId mAutofillId;
    private final android.content.ComponentName mComponentName;
    private final android.os.Handler mHandler;
    private android.view.autofill.AutofillId mImeCurrentFieldId;
    private boolean mImeInputStarted;
    private boolean mImeInputViewStarted;
    private android.view.inputmethod.InlineSuggestionsRequest mImeRequest;
    private java.util.function.Consumer<android.view.inputmethod.InlineSuggestionsRequest> mImeRequestConsumer;
    private boolean mImeRequestReceived;
    private com.android.server.autofill.ui.InlineFillUi mInlineFillUi;
    private final com.android.server.inputmethod.InputMethodManagerInternal mInputMethodManagerInternal;
    private final java.lang.Object mLock;
    private boolean mPreviousHasNonPinSuggestionShow;
    private com.android.internal.inputmethod.IInlineSuggestionsResponseCallback mResponseCallback;
    private final com.android.server.autofill.ui.InlineFillUi.InlineUiEventCallback mUiCallback;
    private final android.os.Bundle mUiExtras;
    private final int mUserId;
    private java.lang.Boolean mPreviousResponseIsNotEmpty = null;
    private boolean mDestroyed = false;
    private boolean mImeSessionInvalidated = false;
    private boolean mImeShowing = false;

    AutofillInlineSuggestionsRequestSession(com.android.server.inputmethod.InputMethodManagerInternal inputMethodManagerInternal, int userId, android.content.ComponentName componentName, android.os.Handler handler, java.lang.Object lock, android.view.autofill.AutofillId autofillId, java.util.function.Consumer<android.view.inputmethod.InlineSuggestionsRequest> requestConsumer, android.os.Bundle uiExtras, com.android.server.autofill.ui.InlineFillUi.InlineUiEventCallback callback) {
        this.mInputMethodManagerInternal = inputMethodManagerInternal;
        this.mUserId = userId;
        this.mComponentName = componentName;
        this.mHandler = handler;
        this.mLock = lock;
        this.mUiExtras = uiExtras;
        this.mUiCallback = callback;
        this.mAutofillId = autofillId;
        this.mImeRequestConsumer = requestConsumer;
    }

    android.view.autofill.AutofillId getAutofillIdLocked() {
        return this.mAutofillId;
    }

    java.util.Optional<android.view.inputmethod.InlineSuggestionsRequest> getInlineSuggestionsRequestLocked() {
        if (this.mDestroyed) {
            return java.util.Optional.empty();
        }
        return java.util.Optional.ofNullable(this.mImeRequest);
    }

    boolean onInlineSuggestionsResponseLocked(com.android.server.autofill.ui.InlineFillUi inlineFillUi) {
        if (this.mDestroyed) {
            return false;
        }
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "onInlineSuggestionsResponseLocked called for:" + inlineFillUi.getAutofillId());
        }
        if (this.mImeRequest == null || this.mResponseCallback == null || this.mImeSessionInvalidated) {
            return false;
        }
        this.mAutofillId = inlineFillUi.getAutofillId();
        this.mInlineFillUi = inlineFillUi;
        maybeUpdateResponseToImeLocked();
        return true;
    }

    void destroySessionLocked() {
        this.mDestroyed = true;
        if (!this.mImeRequestReceived) {
            android.util.Slog.w(TAG, "Never received an InlineSuggestionsRequest from the IME for " + this.mAutofillId);
        }
    }

    void onCreateInlineSuggestionsRequestLocked() {
        if (this.mDestroyed) {
            return;
        }
        this.mImeSessionInvalidated = false;
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "onCreateInlineSuggestionsRequestLocked called: " + this.mAutofillId);
        }
        this.mInputMethodManagerInternal.onCreateInlineSuggestionsRequest(this.mUserId, new com.android.internal.inputmethod.InlineSuggestionsRequestInfo(this.mComponentName, this.mAutofillId, this.mUiExtras), new com.android.server.autofill.AutofillInlineSuggestionsRequestSession.InlineSuggestionsRequestCallbackImpl());
    }

    void resetInlineFillUiLocked() {
        this.mInlineFillUi = null;
    }

    private void maybeUpdateResponseToImeLocked() {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "maybeUpdateResponseToImeLocked called");
        }
        if (!this.mDestroyed && this.mResponseCallback != null && this.mImeInputViewStarted && this.mInlineFillUi != null && match(this.mAutofillId, this.mImeCurrentFieldId)) {
            android.view.inputmethod.InlineSuggestionsResponse response = this.mInlineFillUi.getInlineSuggestionsResponse();
            boolean isEmptyResponse = response.getInlineSuggestions().isEmpty();
            if (isEmptyResponse && java.lang.Boolean.FALSE.equals(this.mPreviousResponseIsNotEmpty)) {
                return;
            }
            maybeNotifyFillUiEventLocked(response.getInlineSuggestions());
            updateResponseToImeUncheckLocked(response);
            this.mPreviousResponseIsNotEmpty = java.lang.Boolean.valueOf(!isEmptyResponse);
        }
    }

    private void updateResponseToImeUncheckLocked(android.view.inputmethod.InlineSuggestionsResponse response) {
        if (this.mDestroyed) {
            return;
        }
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "Send inline response: " + response.getInlineSuggestions().size());
        }
        try {
            this.mResponseCallback.onInlineSuggestionsResponse(this.mAutofillId, response);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "RemoteException sending InlineSuggestionsResponse to IME");
        }
    }

    private void maybeNotifyFillUiEventLocked(java.util.List<android.view.inputmethod.InlineSuggestion> suggestions) {
        if (this.mDestroyed) {
            return;
        }
        boolean hasSuggestionToShow = false;
        int i = 0;
        while (true) {
            if (i >= suggestions.size()) {
                break;
            }
            android.view.inputmethod.InlineSuggestion suggestion = suggestions.get(i);
            if (suggestion.getInfo().isPinned()) {
                i++;
            } else {
                hasSuggestionToShow = true;
                break;
            }
        }
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "maybeNotifyFillUiEventLoked(): hasSuggestionToShow=" + hasSuggestionToShow + ", mPreviousHasNonPinSuggestionShow=" + this.mPreviousHasNonPinSuggestionShow);
        }
        if (hasSuggestionToShow && !this.mPreviousHasNonPinSuggestionShow) {
            this.mUiCallback.notifyInlineUiShown(this.mAutofillId);
        } else if (!hasSuggestionToShow && this.mPreviousHasNonPinSuggestionShow) {
            this.mUiCallback.notifyInlineUiHidden(this.mAutofillId);
        }
        this.mPreviousHasNonPinSuggestionShow = hasSuggestionToShow;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnReceiveImeRequest(android.view.inputmethod.InlineSuggestionsRequest request, com.android.internal.inputmethod.IInlineSuggestionsResponseCallback callback) {
        synchronized (this.mLock) {
            if (!this.mDestroyed && !this.mImeRequestReceived) {
                this.mImeRequestReceived = true;
                this.mImeSessionInvalidated = false;
                if (request != null && callback != null) {
                    this.mImeRequest = request;
                    this.mResponseCallback = callback;
                    handleOnReceiveImeStatusUpdated(this.mAutofillId, true, false);
                }
                if (this.mImeRequestConsumer != null) {
                    this.mImeRequestConsumer.accept(this.mImeRequest);
                    this.mImeRequestConsumer = null;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnReceiveImeStatusUpdated(boolean imeInputStarted, boolean imeInputViewStarted) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return;
            }
            this.mImeShowing = imeInputViewStarted;
            if (this.mImeCurrentFieldId != null) {
                boolean imeInputViewStartedChanged = true;
                boolean imeInputStartedChanged = this.mImeInputStarted != imeInputStarted;
                if (this.mImeInputViewStarted == imeInputViewStarted) {
                    imeInputViewStartedChanged = false;
                }
                this.mImeInputStarted = imeInputStarted;
                this.mImeInputViewStarted = imeInputViewStarted;
                if (imeInputStartedChanged || imeInputViewStartedChanged) {
                    maybeUpdateResponseToImeLocked();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnReceiveImeStatusUpdated(android.view.autofill.AutofillId imeFieldId, boolean imeInputStarted, boolean imeInputViewStarted) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return;
            }
            if (imeFieldId != null) {
                this.mImeCurrentFieldId = imeFieldId;
            }
            handleOnReceiveImeStatusUpdated(imeInputStarted, imeInputViewStarted);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnReceiveImeSessionInvalidated() {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return;
            }
            this.mImeSessionInvalidated = true;
        }
    }

    boolean isImeShowing() {
        boolean z;
        synchronized (this.mLock) {
            z = !this.mDestroyed && this.mImeShowing;
        }
        return z;
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class InlineSuggestionsRequestCallbackImpl implements com.android.internal.inputmethod.InlineSuggestionsRequestCallback {
        private final java.lang.ref.WeakReference<com.android.server.autofill.AutofillInlineSuggestionsRequestSession> mSession;

        private InlineSuggestionsRequestCallbackImpl(com.android.server.autofill.AutofillInlineSuggestionsRequestSession session) {
            this.mSession = new java.lang.ref.WeakReference<>(session);
        }

        public void onInlineSuggestionsUnsupported() {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(com.android.server.autofill.AutofillInlineSuggestionsRequestSession.TAG, "onInlineSuggestionsUnsupported() called.");
            }
            com.android.server.autofill.AutofillInlineSuggestionsRequestSession session = this.mSession.get();
            if (session != null) {
                session.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.autofill.AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda1(), session, (java.lang.Object) null, (java.lang.Object) null));
            }
        }

        public void onInlineSuggestionsRequest(android.view.inputmethod.InlineSuggestionsRequest request, com.android.internal.inputmethod.IInlineSuggestionsResponseCallback callback) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(com.android.server.autofill.AutofillInlineSuggestionsRequestSession.TAG, "onInlineSuggestionsRequest() received: " + request);
            }
            com.android.server.autofill.AutofillInlineSuggestionsRequestSession session = this.mSession.get();
            if (session != null) {
                session.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.autofill.AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda1(), session, request, callback));
            }
        }

        public void onInputMethodStartInput(android.view.autofill.AutofillId imeFieldId) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(com.android.server.autofill.AutofillInlineSuggestionsRequestSession.TAG, "onInputMethodStartInput() received on " + imeFieldId);
            }
            com.android.server.autofill.AutofillInlineSuggestionsRequestSession session = this.mSession.get();
            if (session != null) {
                session.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuadConsumer() { // from class: com.android.server.autofill.AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda2
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4) {
                        ((com.android.server.autofill.AutofillInlineSuggestionsRequestSession) obj).handleOnReceiveImeStatusUpdated((android.view.autofill.AutofillId) obj2, ((java.lang.Boolean) obj3).booleanValue(), ((java.lang.Boolean) obj4).booleanValue());
                    }
                }, session, imeFieldId, true, false));
            }
        }

        public void onInputMethodShowInputRequested(boolean requestResult) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(com.android.server.autofill.AutofillInlineSuggestionsRequestSession.TAG, "onInputMethodShowInputRequested() received: " + requestResult);
            }
        }

        public void onInputMethodStartInputView() {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(com.android.server.autofill.AutofillInlineSuggestionsRequestSession.TAG, "onInputMethodStartInputView() received");
            }
            com.android.server.autofill.AutofillInlineSuggestionsRequestSession session = this.mSession.get();
            if (session != null) {
                session.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.autofill.AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda3(), session, true, true));
            }
        }

        public void onInputMethodFinishInputView() {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(com.android.server.autofill.AutofillInlineSuggestionsRequestSession.TAG, "onInputMethodFinishInputView() received");
            }
            com.android.server.autofill.AutofillInlineSuggestionsRequestSession session = this.mSession.get();
            if (session != null) {
                session.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.autofill.AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda3(), session, true, false));
            }
        }

        public void onInputMethodFinishInput() {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(com.android.server.autofill.AutofillInlineSuggestionsRequestSession.TAG, "onInputMethodFinishInput() received");
            }
            com.android.server.autofill.AutofillInlineSuggestionsRequestSession session = this.mSession.get();
            if (session != null) {
                session.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.autofill.AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda3(), session, false, false));
            }
        }

        public void onInlineSuggestionsSessionInvalidated() {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(com.android.server.autofill.AutofillInlineSuggestionsRequestSession.TAG, "onInlineSuggestionsSessionInvalidated() called.");
            }
            com.android.server.autofill.AutofillInlineSuggestionsRequestSession session = this.mSession.get();
            if (session != null) {
                session.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.autofill.AutofillInlineSuggestionsRequestSession$InlineSuggestionsRequestCallbackImpl$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.autofill.AutofillInlineSuggestionsRequestSession) obj).handleOnReceiveImeSessionInvalidated();
                    }
                }, session));
            }
        }
    }

    private static boolean match(android.view.autofill.AutofillId autofillId, android.view.autofill.AutofillId imeClientFieldId) {
        return (autofillId == null || imeClientFieldId == null || autofillId.getViewId() != imeClientFieldId.getViewId()) ? false : true;
    }
}
