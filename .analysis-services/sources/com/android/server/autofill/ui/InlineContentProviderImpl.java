package com.android.server.autofill.ui;

/* JADX INFO: loaded from: classes.dex */
final class InlineContentProviderImpl extends com.android.internal.view.inline.IInlineContentProvider.Stub {
    private static final java.lang.String TAG = com.android.server.autofill.ui.InlineContentProviderImpl.class.getSimpleName();
    private final android.os.Handler mHandler = com.android.server.FgThread.getHandler();
    private boolean mProvideContentCalled = false;
    private com.android.server.autofill.ui.RemoteInlineSuggestionUi mRemoteInlineSuggestionUi;
    private final com.android.server.autofill.ui.RemoteInlineSuggestionViewConnector mRemoteInlineSuggestionViewConnector;

    InlineContentProviderImpl(com.android.server.autofill.ui.RemoteInlineSuggestionViewConnector remoteInlineSuggestionViewConnector, com.android.server.autofill.ui.RemoteInlineSuggestionUi remoteInlineSuggestionUi) {
        this.mRemoteInlineSuggestionViewConnector = remoteInlineSuggestionViewConnector;
        this.mRemoteInlineSuggestionUi = remoteInlineSuggestionUi;
    }

    public com.android.server.autofill.ui.InlineContentProviderImpl copy() {
        return new com.android.server.autofill.ui.InlineContentProviderImpl(this.mRemoteInlineSuggestionViewConnector, this.mRemoteInlineSuggestionUi);
    }

    public void provideContent(final int width, final int height, final com.android.internal.view.inline.IInlineContentCallback callback) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.InlineContentProviderImpl$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$provideContent$0(width, height, callback);
            }
        });
    }

    public void requestSurfacePackage() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.InlineContentProviderImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.handleGetSurfacePackage();
            }
        });
    }

    public void onSurfacePackageReleased() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.InlineContentProviderImpl$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.handleOnSurfacePackageReleased();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: handleProvideContent, reason: merged with bridge method [inline-methods] */
    public void lambda$provideContent$0(int width, int height, com.android.internal.view.inline.IInlineContentCallback callback) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "handleProvideContent");
        }
        if (this.mProvideContentCalled) {
            return;
        }
        this.mProvideContentCalled = true;
        if (this.mRemoteInlineSuggestionUi == null || !this.mRemoteInlineSuggestionUi.match(width, height)) {
            this.mRemoteInlineSuggestionUi = new com.android.server.autofill.ui.RemoteInlineSuggestionUi(this.mRemoteInlineSuggestionViewConnector, width, height, this.mHandler);
        }
        this.mRemoteInlineSuggestionUi.setInlineContentCallback(callback);
        this.mRemoteInlineSuggestionUi.requestSurfacePackage();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleGetSurfacePackage() {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "handleGetSurfacePackage");
        }
        if (!this.mProvideContentCalled || this.mRemoteInlineSuggestionUi == null) {
            return;
        }
        this.mRemoteInlineSuggestionUi.requestSurfacePackage();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnSurfacePackageReleased() {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "handleOnSurfacePackageReleased");
        }
        if (!this.mProvideContentCalled || this.mRemoteInlineSuggestionUi == null) {
            return;
        }
        this.mRemoteInlineSuggestionUi.surfacePackageReleased();
    }
}
