package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
class InlineSuggestionRequestConsumer implements java.util.function.Consumer<android.view.inputmethod.InlineSuggestionsRequest> {
    static final java.lang.String TAG = "InlineSuggestionRequestConsumer";
    private final java.lang.ref.WeakReference<com.android.server.autofill.Session.AssistDataReceiverImpl> mAssistDataReceiverWeakReference;
    private final java.lang.ref.WeakReference<com.android.server.autofill.ViewState> mViewStateWeakReference;

    InlineSuggestionRequestConsumer(java.lang.ref.WeakReference<com.android.server.autofill.Session.AssistDataReceiverImpl> assistDataReceiverWeakReference, java.lang.ref.WeakReference<com.android.server.autofill.ViewState> viewStateWeakReference) {
        this.mAssistDataReceiverWeakReference = assistDataReceiverWeakReference;
        this.mViewStateWeakReference = viewStateWeakReference;
    }

    @Override // java.util.function.Consumer
    public void accept(android.view.inputmethod.InlineSuggestionsRequest inlineSuggestionsRequest) {
        com.android.server.autofill.Session.AssistDataReceiverImpl assistDataReceiver = this.mAssistDataReceiverWeakReference.get();
        com.android.server.autofill.ViewState viewState = this.mViewStateWeakReference.get();
        if (assistDataReceiver == null) {
            android.util.Slog.wtf(TAG, "assistDataReceiver is null when accepting new inline suggestionrequests");
        } else if (viewState == null) {
            android.util.Slog.wtf(TAG, "view state is null when accepting new inline suggestion requests");
        } else {
            assistDataReceiver.handleInlineSuggestionRequest(inlineSuggestionsRequest, viewState);
        }
    }
}
