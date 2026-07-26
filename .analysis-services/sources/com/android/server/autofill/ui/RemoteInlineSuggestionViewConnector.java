package com.android.server.autofill.ui;

/* JADX INFO: loaded from: classes.dex */
final class RemoteInlineSuggestionViewConnector {
    private static final java.lang.String TAG = com.android.server.autofill.ui.RemoteInlineSuggestionViewConnector.class.getSimpleName();
    private final int mDisplayId;
    private final android.os.IBinder mHostInputToken;
    private final android.service.autofill.InlinePresentation mInlinePresentation;
    private final java.lang.Runnable mOnAutofillCallback;
    private final java.lang.Runnable mOnErrorCallback;
    private final java.lang.Runnable mOnInflateCallback;
    private final com.android.server.autofill.RemoteInlineSuggestionRenderService mRemoteRenderService;
    private final int mSessionId;
    private final java.util.function.Consumer<android.content.IntentSender> mStartIntentSenderFromClientApp;
    private final int mUserId;

    RemoteInlineSuggestionViewConnector(com.android.server.autofill.ui.InlineFillUi.InlineFillUiInfo inlineFillUiInfo, android.service.autofill.InlinePresentation inlinePresentation, java.lang.Runnable onAutofillCallback, final com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback uiCallback) {
        this.mRemoteRenderService = inlineFillUiInfo.mRemoteRenderService;
        this.mInlinePresentation = inlinePresentation;
        this.mHostInputToken = inlineFillUiInfo.mInlineRequest.getHostInputToken();
        this.mDisplayId = inlineFillUiInfo.mInlineRequest.getHostDisplayId();
        this.mUserId = inlineFillUiInfo.mUserId;
        this.mSessionId = inlineFillUiInfo.mSessionId;
        this.mOnAutofillCallback = onAutofillCallback;
        java.util.Objects.requireNonNull(uiCallback);
        this.mOnErrorCallback = new java.lang.Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionViewConnector$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                uiCallback.onError();
            }
        };
        java.util.Objects.requireNonNull(uiCallback);
        this.mOnInflateCallback = new java.lang.Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionViewConnector$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                uiCallback.onInflate();
            }
        };
        java.util.Objects.requireNonNull(uiCallback);
        this.mStartIntentSenderFromClientApp = new java.util.function.Consumer() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionViewConnector$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                uiCallback.startIntentSender((android.content.IntentSender) obj);
            }
        };
    }

    public boolean renderSuggestion(int width, int height, android.service.autofill.IInlineSuggestionUiCallback callback) {
        if (com.android.server.autofill.Helper.sanitizeSlice(this.mInlinePresentation.getSlice()) == null) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "Skipped rendering inline suggestion.");
            }
            return false;
        }
        if (this.mRemoteRenderService == null) {
            return false;
        }
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "Request to recreate the UI");
        }
        this.mRemoteRenderService.renderSuggestion(callback, this.mInlinePresentation, width, height, this.mHostInputToken, this.mDisplayId, this.mUserId, this.mSessionId);
        return true;
    }

    public void onClick() {
        this.mOnAutofillCallback.run();
    }

    public void onError() {
        this.mOnErrorCallback.run();
    }

    public void onRender() {
        this.mOnInflateCallback.run();
    }

    public void onTransferTouchFocusToImeWindow(android.os.IBinder sourceInputToken, int displayId) {
        com.android.server.inputmethod.InputMethodManagerInternal inputMethodManagerInternal = (com.android.server.inputmethod.InputMethodManagerInternal) com.android.server.LocalServices.getService(com.android.server.inputmethod.InputMethodManagerInternal.class);
        if (!inputMethodManagerInternal.transferTouchFocusToImeWindow(sourceInputToken, displayId, this.mUserId)) {
            android.util.Slog.e(TAG, "Cannot transfer touch focus from suggestion to IME");
            this.mOnErrorCallback.run();
        }
    }

    public void onStartIntentSender(android.content.IntentSender intentSender) {
        this.mStartIntentSenderFromClientApp.accept(intentSender);
    }
}
