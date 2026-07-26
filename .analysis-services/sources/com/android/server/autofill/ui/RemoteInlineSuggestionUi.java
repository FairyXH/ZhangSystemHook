package com.android.server.autofill.ui;

/* JADX INFO: loaded from: classes.dex */
final class RemoteInlineSuggestionUi {
    private static final long RELEASE_REMOTE_VIEW_HOST_DELAY_MS = 200;
    private static final java.lang.String TAG = com.android.server.autofill.ui.RemoteInlineSuggestionUi.class.getSimpleName();
    private int mActualHeight;
    private int mActualWidth;
    private java.lang.Runnable mDelayedReleaseViewRunnable;
    private final android.os.Handler mHandler;
    private final int mHeight;
    private com.android.internal.view.inline.IInlineContentCallback mInlineContentCallback;
    private android.service.autofill.IInlineSuggestionUi mInlineSuggestionUi;
    private final com.android.server.autofill.ui.RemoteInlineSuggestionViewConnector mRemoteInlineSuggestionViewConnector;
    private final int mWidth;
    private int mRefCount = 0;
    private boolean mWaitingForUiCreation = false;
    private final com.android.server.autofill.ui.RemoteInlineSuggestionUi.InlineSuggestionUiCallbackImpl mInlineSuggestionUiCallback = new com.android.server.autofill.ui.RemoteInlineSuggestionUi.InlineSuggestionUiCallbackImpl();

    RemoteInlineSuggestionUi(com.android.server.autofill.ui.RemoteInlineSuggestionViewConnector remoteInlineSuggestionViewConnector, int width, int height, android.os.Handler handler) {
        this.mHandler = handler;
        this.mRemoteInlineSuggestionViewConnector = remoteInlineSuggestionViewConnector;
        this.mWidth = width;
        this.mHeight = height;
    }

    void setInlineContentCallback(final com.android.internal.view.inline.IInlineContentCallback inlineContentCallback) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setInlineContentCallback$0(inlineContentCallback);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setInlineContentCallback$0(com.android.internal.view.inline.IInlineContentCallback inlineContentCallback) {
        this.mInlineContentCallback = inlineContentCallback;
    }

    void requestSurfacePackage() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.handleRequestSurfacePackage();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$surfacePackageReleased$1() {
        handleUpdateRefCount(-1);
    }

    void surfacePackageReleased() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$surfacePackageReleased$1();
            }
        });
    }

    boolean match(int width, int height) {
        return this.mWidth == width && this.mHeight == height;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRequestSurfacePackage() {
        cancelPendingReleaseViewRequest();
        if (this.mInlineSuggestionUi == null) {
            if (this.mWaitingForUiCreation) {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.d(TAG, "Inline suggestion ui is not ready");
                    return;
                }
                return;
            } else {
                this.mRemoteInlineSuggestionViewConnector.renderSuggestion(this.mWidth, this.mHeight, this.mInlineSuggestionUiCallback);
                this.mWaitingForUiCreation = true;
                return;
            }
        }
        try {
            this.mInlineSuggestionUi.getSurfacePackage(new com.android.server.autofill.ui.RemoteInlineSuggestionUi.AnonymousClass1());
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "RemoteException calling getSurfacePackage.");
        }
    }

    /* JADX INFO: renamed from: com.android.server.autofill.ui.RemoteInlineSuggestionUi$1, reason: invalid class name */
    class AnonymousClass1 extends android.service.autofill.ISurfacePackageResultCallback.Stub {
        AnonymousClass1() {
        }

        public void onResult(final android.view.SurfaceControlViewHost.SurfacePackage result) {
            com.android.server.autofill.ui.RemoteInlineSuggestionUi.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onResult$0(result);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onResult$0(android.view.SurfaceControlViewHost.SurfacePackage result) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(com.android.server.autofill.ui.RemoteInlineSuggestionUi.TAG, "Sending refreshed SurfacePackage to IME");
            }
            try {
                com.android.server.autofill.ui.RemoteInlineSuggestionUi.this.mInlineContentCallback.onContent(result, com.android.server.autofill.ui.RemoteInlineSuggestionUi.this.mActualWidth, com.android.server.autofill.ui.RemoteInlineSuggestionUi.this.mActualHeight);
                com.android.server.autofill.ui.RemoteInlineSuggestionUi.this.handleUpdateRefCount(1);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(com.android.server.autofill.ui.RemoteInlineSuggestionUi.TAG, "RemoteException calling onContent");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleUpdateRefCount(int delta) {
        cancelPendingReleaseViewRequest();
        this.mRefCount += delta;
        if (this.mRefCount <= 0) {
            this.mDelayedReleaseViewRunnable = new java.lang.Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$handleUpdateRefCount$2();
                }
            };
            this.mHandler.postDelayed(this.mDelayedReleaseViewRunnable, RELEASE_REMOTE_VIEW_HOST_DELAY_MS);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handleUpdateRefCount$2() {
        if (this.mInlineSuggestionUi != null) {
            try {
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "releasing the host");
                }
                this.mInlineSuggestionUi.releaseSurfaceControlViewHost();
                this.mInlineSuggestionUi = null;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "RemoteException calling releaseSurfaceControlViewHost");
            }
        }
        this.mDelayedReleaseViewRunnable = null;
    }

    private void cancelPendingReleaseViewRequest() {
        if (this.mDelayedReleaseViewRunnable != null) {
            this.mHandler.removeCallbacks(this.mDelayedReleaseViewRunnable);
            this.mDelayedReleaseViewRunnable = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleInlineSuggestionUiReady(android.service.autofill.IInlineSuggestionUi content, android.view.SurfaceControlViewHost.SurfacePackage surfacePackage, int width, int height) {
        this.mInlineSuggestionUi = content;
        this.mRefCount = 0;
        this.mWaitingForUiCreation = false;
        this.mActualWidth = width;
        this.mActualHeight = height;
        if (this.mInlineContentCallback != null) {
            try {
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "Sending new UI content to IME");
                }
                handleUpdateRefCount(1);
                this.mInlineContentCallback.onContent(surfacePackage, this.mActualWidth, this.mActualHeight);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "RemoteException calling onContent");
            }
        }
        if (surfacePackage != null) {
            surfacePackage.release();
        }
        this.mRemoteInlineSuggestionViewConnector.onRender();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnClick() {
        this.mRemoteInlineSuggestionViewConnector.onClick();
        if (this.mInlineContentCallback != null) {
            try {
                this.mInlineContentCallback.onClick();
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "RemoteException calling onClick");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnLongClick() {
        if (this.mInlineContentCallback != null) {
            try {
                this.mInlineContentCallback.onLongClick();
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "RemoteException calling onLongClick");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnError() {
        this.mRemoteInlineSuggestionViewConnector.onError();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnTransferTouchFocusToImeWindow(android.os.IBinder sourceInputToken, int displayId) {
        this.mRemoteInlineSuggestionViewConnector.onTransferTouchFocusToImeWindow(sourceInputToken, displayId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleOnStartIntentSender(android.content.IntentSender intentSender) {
        this.mRemoteInlineSuggestionViewConnector.onStartIntentSender(intentSender);
    }

    /* JADX INFO: Access modifiers changed from: private */
    class InlineSuggestionUiCallbackImpl extends android.service.autofill.IInlineSuggestionUiCallback.Stub {
        private InlineSuggestionUiCallbackImpl() {
        }

        public void onClick() {
            android.os.Handler handler = com.android.server.autofill.ui.RemoteInlineSuggestionUi.this.mHandler;
            final com.android.server.autofill.ui.RemoteInlineSuggestionUi remoteInlineSuggestionUi = com.android.server.autofill.ui.RemoteInlineSuggestionUi.this;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$InlineSuggestionUiCallbackImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    remoteInlineSuggestionUi.handleOnClick();
                }
            });
        }

        public void onLongClick() {
            android.os.Handler handler = com.android.server.autofill.ui.RemoteInlineSuggestionUi.this.mHandler;
            final com.android.server.autofill.ui.RemoteInlineSuggestionUi remoteInlineSuggestionUi = com.android.server.autofill.ui.RemoteInlineSuggestionUi.this;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$InlineSuggestionUiCallbackImpl$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    remoteInlineSuggestionUi.handleOnLongClick();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onContent$0(android.service.autofill.IInlineSuggestionUi content, android.view.SurfaceControlViewHost.SurfacePackage surface, int width, int height) {
            com.android.server.autofill.ui.RemoteInlineSuggestionUi.this.handleInlineSuggestionUiReady(content, surface, width, height);
        }

        public void onContent(final android.service.autofill.IInlineSuggestionUi content, final android.view.SurfaceControlViewHost.SurfacePackage surface, final int width, final int height) {
            com.android.server.autofill.ui.RemoteInlineSuggestionUi.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$InlineSuggestionUiCallbackImpl$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onContent$0(content, surface, width, height);
                }
            });
        }

        public void onError() {
            android.os.Handler handler = com.android.server.autofill.ui.RemoteInlineSuggestionUi.this.mHandler;
            final com.android.server.autofill.ui.RemoteInlineSuggestionUi remoteInlineSuggestionUi = com.android.server.autofill.ui.RemoteInlineSuggestionUi.this;
            handler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$InlineSuggestionUiCallbackImpl$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    remoteInlineSuggestionUi.handleOnError();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onTransferTouchFocusToImeWindow$1(android.os.IBinder sourceInputToken, int displayId) {
            com.android.server.autofill.ui.RemoteInlineSuggestionUi.this.handleOnTransferTouchFocusToImeWindow(sourceInputToken, displayId);
        }

        public void onTransferTouchFocusToImeWindow(final android.os.IBinder sourceInputToken, final int displayId) {
            com.android.server.autofill.ui.RemoteInlineSuggestionUi.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$InlineSuggestionUiCallbackImpl$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onTransferTouchFocusToImeWindow$1(sourceInputToken, displayId);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStartIntentSender$2(android.content.IntentSender intentSender) {
            com.android.server.autofill.ui.RemoteInlineSuggestionUi.this.handleOnStartIntentSender(intentSender);
        }

        public void onStartIntentSender(final android.content.IntentSender intentSender) {
            com.android.server.autofill.ui.RemoteInlineSuggestionUi.this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.autofill.ui.RemoteInlineSuggestionUi$InlineSuggestionUiCallbackImpl$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onStartIntentSender$2(intentSender);
                }
            });
        }
    }
}
