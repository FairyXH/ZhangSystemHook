package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
final class AutofillInlineSessionController {
    private final android.content.ComponentName mComponentName;
    private final android.os.Handler mHandler;
    private com.android.server.autofill.ui.InlineFillUi mInlineFillUi;
    private final com.android.server.inputmethod.InputMethodManagerInternal mInputMethodManagerInternal;
    private final java.lang.Object mLock;
    private com.android.server.autofill.AutofillInlineSuggestionsRequestSession mSession;
    private final com.android.server.autofill.ui.InlineFillUi.InlineUiEventCallback mUiCallback;
    private final int mUserId;

    AutofillInlineSessionController(com.android.server.inputmethod.InputMethodManagerInternal inputMethodManagerInternal, int userId, android.content.ComponentName componentName, android.os.Handler handler, java.lang.Object lock, com.android.server.autofill.ui.InlineFillUi.InlineUiEventCallback callback) {
        this.mInputMethodManagerInternal = inputMethodManagerInternal;
        this.mUserId = userId;
        this.mComponentName = componentName;
        this.mHandler = handler;
        this.mLock = lock;
        this.mUiCallback = callback;
    }

    void onCreateInlineSuggestionsRequestLocked(android.view.autofill.AutofillId autofillId, java.util.function.Consumer<android.view.inputmethod.InlineSuggestionsRequest> requestConsumer, android.os.Bundle uiExtras) {
        if (this.mSession != null) {
            this.mSession.destroySessionLocked();
        }
        this.mInlineFillUi = null;
        this.mSession = new com.android.server.autofill.AutofillInlineSuggestionsRequestSession(this.mInputMethodManagerInternal, this.mUserId, this.mComponentName, this.mHandler, this.mLock, autofillId, requestConsumer, uiExtras, this.mUiCallback);
        this.mSession.onCreateInlineSuggestionsRequestLocked();
    }

    void destroyLocked(android.view.autofill.AutofillId autofillId) {
        if (this.mSession != null) {
            this.mSession.onInlineSuggestionsResponseLocked(com.android.server.autofill.ui.InlineFillUi.emptyUi(autofillId));
            this.mSession.destroySessionLocked();
            this.mSession = null;
        }
        this.mInlineFillUi = null;
    }

    java.util.Optional<android.view.inputmethod.InlineSuggestionsRequest> getInlineSuggestionsRequestLocked() {
        if (this.mSession != null) {
            return this.mSession.getInlineSuggestionsRequestLocked();
        }
        return java.util.Optional.empty();
    }

    boolean hideInlineSuggestionsUiLocked(android.view.autofill.AutofillId autofillId) {
        if (this.mSession != null) {
            return this.mSession.onInlineSuggestionsResponseLocked(com.android.server.autofill.ui.InlineFillUi.emptyUi(autofillId));
        }
        return false;
    }

    void disableFilterMatching(android.view.autofill.AutofillId autofillId) {
        if (this.mInlineFillUi != null && this.mInlineFillUi.getAutofillId().equals(autofillId)) {
            this.mInlineFillUi.disableFilterMatching();
        }
    }

    void resetInlineFillUiLocked() {
        this.mInlineFillUi = null;
        if (this.mSession != null) {
            this.mSession.resetInlineFillUiLocked();
        }
    }

    boolean filterInlineFillUiLocked(android.view.autofill.AutofillId autofillId, java.lang.String filterText) {
        if (this.mInlineFillUi != null && this.mInlineFillUi.getAutofillId().equals(autofillId)) {
            this.mInlineFillUi.setFilterText(filterText);
            return requestImeToShowInlineSuggestionsLocked();
        }
        return false;
    }

    boolean setInlineFillUiLocked(com.android.server.autofill.ui.InlineFillUi inlineFillUi) {
        this.mInlineFillUi = inlineFillUi;
        return requestImeToShowInlineSuggestionsLocked();
    }

    private boolean requestImeToShowInlineSuggestionsLocked() {
        if (this.mSession != null && this.mInlineFillUi != null) {
            return this.mSession.onInlineSuggestionsResponseLocked(this.mInlineFillUi);
        }
        return false;
    }

    boolean isImeShowing() {
        if (this.mSession != null) {
            return this.mSession.isImeShowing();
        }
        return false;
    }
}
