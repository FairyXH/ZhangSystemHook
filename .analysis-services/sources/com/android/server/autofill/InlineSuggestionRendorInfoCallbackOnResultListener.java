package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
final class InlineSuggestionRendorInfoCallbackOnResultListener implements android.os.RemoteCallback.OnResultListener {
    private static final java.lang.String TAG = "InlineSuggestionRendorInfoCallbackOnResultListener";
    private final android.view.autofill.AutofillId mFocusedId;
    private final java.util.function.Consumer<android.view.inputmethod.InlineSuggestionsRequest> mInlineSuggestionsRequestConsumer;
    private final int mRequestIdCopy;
    private final java.lang.ref.WeakReference<com.android.server.autofill.Session> mSessionWeakReference;

    InlineSuggestionRendorInfoCallbackOnResultListener(java.lang.ref.WeakReference<com.android.server.autofill.Session> sessionWeakReference, int requestIdCopy, java.util.function.Consumer<android.view.inputmethod.InlineSuggestionsRequest> inlineSuggestionsRequestConsumer, android.view.autofill.AutofillId focusedId) {
        this.mRequestIdCopy = requestIdCopy;
        this.mInlineSuggestionsRequestConsumer = inlineSuggestionsRequestConsumer;
        this.mSessionWeakReference = sessionWeakReference;
        this.mFocusedId = focusedId;
    }

    public void onResult(android.os.Bundle result) {
        com.android.server.autofill.Session session = this.mSessionWeakReference.get();
        if (session == null) {
            android.util.Slog.wtf(TAG, "Session is null before trying to call onResult");
            return;
        }
        synchronized (session.mLock) {
            if (session.mDestroyed) {
                android.util.Slog.wtf(TAG, "Session is destroyed before trying to call onResult");
            } else {
                session.mInlineSessionController.onCreateInlineSuggestionsRequestLocked(this.mFocusedId, session.inlineSuggestionsRequestCacheDecorator(this.mInlineSuggestionsRequestConsumer, this.mRequestIdCopy), result);
            }
        }
    }
}
