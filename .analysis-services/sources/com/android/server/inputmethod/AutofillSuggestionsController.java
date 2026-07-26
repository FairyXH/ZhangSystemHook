package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class AutofillSuggestionsController {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = com.android.server.inputmethod.AutofillSuggestionsController.class.getSimpleName();
    private final com.android.server.inputmethod.InputMethodBindingController mBindingController;
    private android.os.IBinder mCurHostInputToken;
    private com.android.internal.inputmethod.InlineSuggestionsRequestCallback mInlineSuggestionsRequestCallback;
    private com.android.server.inputmethod.AutofillSuggestionsController.CreateInlineSuggestionsRequest mPendingInlineSuggestionsRequest;

    private static final class CreateInlineSuggestionsRequest {
        final com.android.internal.inputmethod.InlineSuggestionsRequestCallback mCallback;
        final java.lang.String mPackageName;
        final com.android.internal.inputmethod.InlineSuggestionsRequestInfo mRequestInfo;

        CreateInlineSuggestionsRequest(com.android.internal.inputmethod.InlineSuggestionsRequestInfo requestInfo, com.android.internal.inputmethod.InlineSuggestionsRequestCallback callback, java.lang.String packageName) {
            this.mRequestInfo = requestInfo;
            this.mCallback = callback;
            this.mPackageName = packageName;
        }
    }

    AutofillSuggestionsController(com.android.server.inputmethod.InputMethodBindingController bindingController) {
        this.mBindingController = bindingController;
    }

    void onResetSystemUi() {
        this.mCurHostInputToken = null;
    }

    android.os.IBinder getCurHostInputToken() {
        return this.mCurHostInputToken;
    }

    void onCreateInlineSuggestionsRequest(com.android.internal.inputmethod.InlineSuggestionsRequestInfo requestInfo, com.android.internal.inputmethod.InlineSuggestionsRequestCallback callback, boolean touchExplorationEnabled) {
        clearPendingInlineSuggestionsRequest();
        this.mInlineSuggestionsRequestCallback = callback;
        java.lang.String imeId = this.mBindingController.getSelectedMethodId();
        android.view.inputmethod.InputMethodInfo imi = com.android.server.inputmethod.InputMethodSettingsRepository.get(this.mBindingController.mUserId).getMethodMap().get(imeId);
        if (imi == null || !isInlineSuggestionsEnabled(imi, touchExplorationEnabled)) {
            callback.onInlineSuggestionsUnsupported();
            return;
        }
        this.mPendingInlineSuggestionsRequest = new com.android.server.inputmethod.AutofillSuggestionsController.CreateInlineSuggestionsRequest(requestInfo, callback, imi.getPackageName());
        if (this.mBindingController.getCurMethod() != null) {
            performOnCreateInlineSuggestionsRequest();
        }
    }

    void performOnCreateInlineSuggestionsRequest() {
        if (this.mPendingInlineSuggestionsRequest == null) {
            return;
        }
        com.android.server.inputmethod.IInputMethodInvoker curMethod = this.mBindingController.getCurMethod();
        if (curMethod != null) {
            curMethod.onCreateInlineSuggestionsRequest(this.mPendingInlineSuggestionsRequest.mRequestInfo, new com.android.server.inputmethod.AutofillSuggestionsController.InlineSuggestionsRequestCallbackDecorator(this.mPendingInlineSuggestionsRequest.mCallback, this.mPendingInlineSuggestionsRequest.mPackageName, this.mBindingController.getCurTokenDisplayId(), this.mBindingController.getCurToken()));
        } else {
            android.util.Slog.w(TAG, "No IME connected! Abandoning inline suggestions creation request.");
        }
        clearPendingInlineSuggestionsRequest();
    }

    private void clearPendingInlineSuggestionsRequest() {
        this.mPendingInlineSuggestionsRequest = null;
    }

    private static boolean isInlineSuggestionsEnabled(android.view.inputmethod.InputMethodInfo imi, boolean touchExplorationEnabled) {
        return imi.isInlineSuggestionsEnabled() && (!touchExplorationEnabled || imi.supportsInlineSuggestionsWithTouchExploration());
    }

    void invalidateAutofillSession() {
        if (this.mInlineSuggestionsRequestCallback != null) {
            this.mInlineSuggestionsRequestCallback.onInlineSuggestionsSessionInvalidated();
        }
    }

    private final class InlineSuggestionsRequestCallbackDecorator extends com.android.internal.inputmethod.IInlineSuggestionsRequestCallback.Stub {
        private final com.android.internal.inputmethod.InlineSuggestionsRequestCallback mCallback;
        private final int mImeDisplayId;
        private final java.lang.String mImePackageName;
        private final android.os.IBinder mImeToken;

        InlineSuggestionsRequestCallbackDecorator(com.android.internal.inputmethod.InlineSuggestionsRequestCallback callback, java.lang.String imePackageName, int displayId, android.os.IBinder imeToken) {
            this.mCallback = callback;
            this.mImePackageName = imePackageName;
            this.mImeDisplayId = displayId;
            this.mImeToken = imeToken;
        }

        public void onInlineSuggestionsUnsupported() {
            this.mCallback.onInlineSuggestionsUnsupported();
        }

        public void onInlineSuggestionsRequest(android.view.inputmethod.InlineSuggestionsRequest request, com.android.internal.inputmethod.IInlineSuggestionsResponseCallback callback) throws android.os.RemoteException {
            if (!this.mImePackageName.equals(request.getHostPackageName())) {
                throw new java.lang.SecurityException("Host package name in the provide request=[" + request.getHostPackageName() + "] doesn't match the IME package name=[" + this.mImePackageName + "].");
            }
            request.setHostDisplayId(this.mImeDisplayId);
            synchronized (com.android.server.inputmethod.ImfLock.class) {
                android.os.IBinder curImeToken = com.android.server.inputmethod.AutofillSuggestionsController.this.mBindingController.getCurToken();
                if (this.mImeToken == curImeToken) {
                    com.android.server.inputmethod.AutofillSuggestionsController.this.mCurHostInputToken = request.getHostInputToken();
                }
            }
            this.mCallback.onInlineSuggestionsRequest(request, callback);
        }

        public void onInputMethodStartInput(android.view.autofill.AutofillId imeFieldId) {
            this.mCallback.onInputMethodStartInput(imeFieldId);
        }

        public void onInputMethodShowInputRequested(boolean requestResult) {
            this.mCallback.onInputMethodShowInputRequested(requestResult);
        }

        public void onInputMethodStartInputView() {
            this.mCallback.onInputMethodStartInputView();
        }

        public void onInputMethodFinishInputView() {
            this.mCallback.onInputMethodFinishInputView();
        }

        public void onInputMethodFinishInput() {
            this.mCallback.onInputMethodFinishInput();
        }

        public void onInlineSuggestionsSessionInvalidated() {
            this.mCallback.onInlineSuggestionsSessionInvalidated();
        }
    }
}
