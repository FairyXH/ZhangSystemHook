package com.android.server.autofill;

/* JADX INFO: loaded from: classes.dex */
final class Session implements com.android.server.autofill.RemoteFillService.FillServiceCallbacks, com.android.server.autofill.ViewState.Listener, com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback, android.service.autofill.ValueFinder, com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks {
    private static final java.lang.String ACTION_DELAYED_FILL = "android.service.autofill.action.DELAYED_FILL";
    static final int AUGMENTED_AUTOFILL_REQUEST_ID = 1;
    private static final boolean DBG = false;
    private static final int DEFAULT__FIELD_CLASSIFICATION_REQUEST_ID_SNAPSHOT = -2;
    private static final int DEFAULT__FILL_REQUEST_ID_SNAPSHOT = -2;
    public static final java.lang.String EXTRA_KEY_DETECTIONS = "detections";
    private static final java.lang.String EXTRA_REQUEST_ID = "android.service.autofill.extra.REQUEST_ID";
    private static final java.lang.String PCC_HINTS_DELIMITER = ",";
    static final java.lang.String REQUEST_ID_KEY = "autofill_request_id";
    static final java.lang.String SESSION_ID_KEY = "autofill_session_id";
    public static final int STATE_ACTIVE = 1;
    public static final int STATE_FINISHED = 2;
    public static final int STATE_REMOVED = 3;
    public static final int STATE_UNKNOWN = 0;
    private static final java.lang.String TAG = "AutofillSession";
    private static com.android.server.autofill.RequestId mRequestId = new com.android.server.autofill.RequestId();
    private static java.util.concurrent.atomic.AtomicInteger sIdCounterForPcc = new java.util.concurrent.atomic.AtomicInteger(2);
    public final int id;
    private android.os.IBinder mActivityToken;
    private java.lang.Runnable mAugmentedAutofillDestroyer;
    private java.util.ArrayList<android.view.autofill.AutofillId> mAugmentedAutofillableIds;
    private java.util.ArrayList<android.metrics.LogMaker> mAugmentedRequestsLogs;
    private android.view.autofill.IAutoFillManagerClient mClient;
    private android.os.Bundle mClientState;
    private android.os.IBinder.DeathRecipient mClientVulture;
    private final boolean mCompatMode;
    private final android.content.ComponentName mComponentName;
    private final android.content.Context mContext;
    private java.util.ArrayList<android.service.autofill.FillContext> mContexts;
    private final android.content.ComponentName mCredentialAutofillService;
    private android.view.autofill.AutofillId mCurrentViewId;
    private boolean mDelayedFillBroadcastReceiverRegistered;
    private android.app.PendingIntent mDelayedFillPendingIntent;
    boolean mDestroyed;
    private com.android.server.autofill.FillRequestEventLogger mFillRequestEventLogger;
    private com.android.server.autofill.FillResponseEventLogger mFillResponseEventLogger;
    public final int mFlags;
    private final android.os.Handler mHandler;
    private boolean mHasCallback;
    private boolean mIgnoreViewStateResetToEmpty;
    final com.android.server.autofill.AutofillInlineSessionController mInlineSessionController;
    private boolean mIsPrimaryCredential;
    private android.view.autofill.AutofillId[] mLastFillDialogTriggerIds;
    private android.util.Pair<java.lang.Integer, android.view.inputmethod.InlineSuggestionsRequest> mLastInlineSuggestionsRequest;
    private long mLatencyBaseTime;
    final java.lang.Object mLock;
    private boolean mLogViewEntered;
    private boolean mLoggedInlineDatasetShown;
    private com.android.server.autofill.ui.PendingUi mPendingSaveUi;
    private com.android.server.autofill.PresentationStatsEventLogger mPresentationStatsEventLogger;
    private android.view.autofill.AutofillId mPreviousNonNullEnteredViewId;
    private boolean mPreviouslyFillDialogPotentiallyStarted;
    private final com.android.server.autofill.RemoteFillService mRemoteFillService;
    private int mRequestCount;
    private android.util.SparseArray<android.service.autofill.FillResponse> mResponses;
    private com.android.server.autofill.SaveEventLogger mSaveEventLogger;
    private boolean mSaveOnAllViewsInvisible;
    private final com.android.server.autofill.SecondaryProviderHandler mSecondaryProviderHandler;
    private android.util.SparseArray<android.service.autofill.FillResponse> mSecondaryResponses;
    private java.util.ArrayList<java.lang.String> mSelectedDatasetIds;
    private final com.android.server.autofill.AutofillManagerServiceImpl mService;
    private com.android.server.autofill.SessionCommittedEventLogger mSessionCommittedEventLogger;
    private final com.android.server.autofill.Session.SessionFlags mSessionFlags;
    private int mSessionState;
    private final long mStartTime;
    private final com.android.server.autofill.ui.AutoFillUI mUi;
    private final android.util.LocalLog mUiLatencyHistory;
    private long mUiShownTime;
    private android.app.assist.AssistStructure.ViewNode mUrlBar;
    private final android.util.LocalLog mWtfHistory;
    public final int taskId;
    public final int uid;
    public final int userId;
    private com.android.server.autofill.ISessionExt mSessionExt = (com.android.server.autofill.ISessionExt) system.ext.loader.core.ExtLoader.type(com.android.server.autofill.ISessionExt.class).base(this).create();
    private com.android.server.autofill.ISessionWrapper mSessionWrapper = new com.android.server.autofill.Session.SessionWrapper();
    private final com.android.internal.logging.MetricsLogger mMetricsLogger = new com.android.internal.logging.MetricsLogger();
    private final android.util.ArrayMap<android.view.autofill.AutofillId, com.android.server.autofill.ViewState> mViewStates = new android.util.ArrayMap<>();
    private int mFillRequestIdSnapshot = -2;
    private int mFieldClassificationIdSnapshot = -2;
    private final android.util.SparseArray<android.metrics.LogMaker> mRequestLogs = new android.util.SparseArray<>(1);
    private final com.android.server.autofill.Session.AssistDataReceiverImpl mAssistReceiver = new com.android.server.autofill.Session.AssistDataReceiverImpl();
    private final com.android.server.autofill.Session.PccAssistDataReceiverImpl mPccAssistReceiver = new com.android.server.autofill.Session.PccAssistDataReceiverImpl();
    private final com.android.server.autofill.Session.ClassificationState mClassificationState = new com.android.server.autofill.Session.ClassificationState();
    private final android.content.BroadcastReceiver mDelayedFillBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.autofill.Session.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (!intent.getAction().equals(com.android.server.autofill.Session.ACTION_DELAYED_FILL)) {
                android.util.Slog.wtf(com.android.server.autofill.Session.TAG, "Unexpected action is received.");
                return;
            }
            if (!intent.hasExtra(com.android.server.autofill.Session.EXTRA_REQUEST_ID)) {
                android.util.Slog.e(com.android.server.autofill.Session.TAG, "Delay fill action is missing request id extra.");
                return;
            }
            android.util.Slog.v(com.android.server.autofill.Session.TAG, "mDelayedFillBroadcastReceiver delayed fill action received");
            synchronized (com.android.server.autofill.Session.this.mLock) {
                int requestId = intent.getIntExtra(com.android.server.autofill.Session.EXTRA_REQUEST_ID, 0);
                android.service.autofill.FillResponse response = (android.service.autofill.FillResponse) intent.getParcelableExtra("android.service.autofill.extra.FILL_RESPONSE", android.service.autofill.FillResponse.class);
                com.android.server.autofill.Session.this.mFillRequestEventLogger.maybeSetRequestTriggerReason(2);
                com.android.server.autofill.Session.this.mAssistReceiver.processDelayedFillLocked(requestId, response);
            }
        }
    };

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface SessionState {
    }

    public com.android.server.autofill.ISessionWrapper getWrapper() {
        return this.mSessionWrapper;
    }

    private class SessionWrapper implements com.android.server.autofill.ISessionWrapper {
        private SessionWrapper() {
        }

        @Override // com.android.server.autofill.ISessionWrapper
        public void save() {
            com.android.server.autofill.Session.this.save();
        }

        @Override // com.android.server.autofill.ISessionWrapper
        public void autofill(int requestId, int datasetIndex, android.service.autofill.Dataset dataset, boolean generateEvent, int uiType) {
            com.android.server.autofill.Session.this.autoFill(requestId, datasetIndex, dataset, generateEvent, uiType);
        }

        @Override // com.android.server.autofill.ISessionWrapper
        public com.android.server.autofill.ISessionExt getSessionExt() {
            return com.android.server.autofill.Session.this.mSessionExt;
        }
    }

    void onSwitchInputMethodLocked() {
        if (!this.mSessionFlags.mExpiredResponse && shouldResetSessionStateOnInputMethodSwitch()) {
            this.mSessionFlags.mExpiredResponse = true;
            this.mAugmentedAutofillableIds = null;
            if (this.mSessionFlags.mAugmentedAutofillOnly) {
                this.mCurrentViewId = null;
            }
        }
    }

    private boolean shouldResetSessionStateOnInputMethodSwitch() {
        if (this.mService.getRemoteInlineSuggestionRenderServiceLocked() == null) {
            return false;
        }
        if (this.mSessionFlags.mInlineSupportedByService) {
            return true;
        }
        com.android.server.autofill.ViewState state = this.mViewStates.get(this.mCurrentViewId);
        return (state == null || (state.getState() & 4096) == 0) ? false : true;
    }

    private final class SessionFlags {
        private boolean mAugmentedAutofillOnly;
        private boolean mAutofillDisabled;
        private boolean mExpiredResponse;
        private boolean mFillDialogDisabled;
        private boolean mInlineSupportedByService;
        private boolean mScreenHasCredmanField;
        private boolean mShowingSaveUi;

        private SessionFlags() {
        }
    }

    final class AssistDataReceiverImpl extends android.app.IAssistDataReceiver.Stub {
        private android.service.autofill.FillRequest mLastFillRequest;
        private android.service.autofill.FillRequest mPendingFillRequest;
        private android.view.inputmethod.InlineSuggestionsRequest mPendingInlineSuggestionsRequest;
        private boolean mWaitForInlineRequest;

        AssistDataReceiverImpl() {
        }

        java.util.function.Consumer<android.view.inputmethod.InlineSuggestionsRequest> newAutofillRequestLocked(com.android.server.autofill.ViewState viewState, boolean isInlineRequest) {
            this.mPendingFillRequest = null;
            this.mWaitForInlineRequest = isInlineRequest;
            this.mPendingInlineSuggestionsRequest = null;
            if (!isInlineRequest) {
                return null;
            }
            java.lang.ref.WeakReference<com.android.server.autofill.Session.AssistDataReceiverImpl> assistDataReceiverWeakReference = new java.lang.ref.WeakReference<>(this);
            java.lang.ref.WeakReference<com.android.server.autofill.ViewState> viewStateWeakReference = new java.lang.ref.WeakReference<>(viewState);
            return new com.android.server.autofill.InlineSuggestionRequestConsumer(assistDataReceiverWeakReference, viewStateWeakReference);
        }

        void handleInlineSuggestionRequest(android.view.inputmethod.InlineSuggestionsRequest inlineSuggestionsRequest, com.android.server.autofill.ViewState viewState) {
            synchronized (com.android.server.autofill.Session.this.mLock) {
                if (this.mWaitForInlineRequest && this.mPendingInlineSuggestionsRequest == null) {
                    this.mWaitForInlineRequest = inlineSuggestionsRequest != null;
                    this.mPendingInlineSuggestionsRequest = inlineSuggestionsRequest;
                    maybeRequestFillLocked();
                    viewState.resetState(65536);
                }
            }
        }

        void maybeRequestFillLocked() {
            if (this.mPendingFillRequest == null) {
                return;
            }
            com.android.server.autofill.Session.this.mFieldClassificationIdSnapshot = com.android.server.autofill.Session.sIdCounterForPcc.get();
            if (this.mWaitForInlineRequest) {
                if (this.mPendingInlineSuggestionsRequest == null) {
                    return;
                }
                android.os.Bundle newClientState = com.android.server.autofill.Session.this.mSessionExt.hookOnFillRequestClientState(this.mPendingFillRequest.getClientState());
                this.mPendingFillRequest = new android.service.autofill.FillRequest(this.mPendingFillRequest.getId(), this.mPendingFillRequest.getFillContexts(), this.mPendingFillRequest.getHints(), newClientState, this.mPendingFillRequest.getFlags(), this.mPendingInlineSuggestionsRequest, this.mPendingFillRequest.getDelayedFillIntentSender());
            }
            this.mLastFillRequest = this.mPendingFillRequest;
            if (com.android.server.autofill.Session.this.shouldRequestSecondaryProvider(this.mPendingFillRequest.getFlags()) && com.android.server.autofill.Session.this.mSecondaryProviderHandler != null) {
                android.util.Slog.v(com.android.server.autofill.Session.TAG, "Requesting fill response to secondary provider.");
                if (!com.android.server.autofill.Session.this.mIsPrimaryCredential) {
                    this.mPendingFillRequest = com.android.server.autofill.Session.this.addCredentialManagerDataToClientState(this.mPendingFillRequest, this.mPendingInlineSuggestionsRequest, com.android.server.autofill.Session.this.id);
                }
                com.android.server.autofill.Session.this.mSecondaryProviderHandler.onFillRequest(this.mPendingFillRequest, this.mPendingFillRequest.getFlags(), com.android.server.autofill.Session.this.mClient.asBinder());
            } else if (com.android.server.autofill.Session.this.mRemoteFillService != null) {
                if (com.android.server.autofill.Session.this.mIsPrimaryCredential) {
                    this.mPendingFillRequest = com.android.server.autofill.Session.this.addCredentialManagerDataToClientState(this.mPendingFillRequest, this.mPendingInlineSuggestionsRequest, com.android.server.autofill.Session.this.id);
                    com.android.server.autofill.Session.this.mRemoteFillService.onFillCredentialRequest(this.mPendingFillRequest, com.android.server.autofill.Session.this.mClient.asBinder());
                } else {
                    com.android.server.autofill.Session.this.mRemoteFillService.onFillRequest(this.mPendingFillRequest);
                }
            }
            this.mPendingInlineSuggestionsRequest = null;
            this.mWaitForInlineRequest = false;
            this.mPendingFillRequest = null;
            long fillRequestSentRelativeTimestamp = android.os.SystemClock.elapsedRealtime() - com.android.server.autofill.Session.this.mLatencyBaseTime;
            com.android.server.autofill.Session.this.mPresentationStatsEventLogger.maybeSetFillRequestSentTimestampMs((int) fillRequestSentRelativeTimestamp);
            com.android.server.autofill.Session.this.mFillRequestEventLogger.maybeSetLatencyFillRequestSentMillis((int) fillRequestSentRelativeTimestamp);
            com.android.server.autofill.Session.this.mFillRequestEventLogger.logAndEndEvent();
        }

        public void onHandleAssistData(android.os.Bundle resultData) throws android.os.RemoteException {
            int flags;
            android.content.IntentSender intentSender;
            if (com.android.server.autofill.Session.this.mRemoteFillService == null) {
                com.android.server.autofill.Session.this.wtf(null, "onHandleAssistData() called without a remote service. mForAugmentedAutofillOnly: %s", java.lang.Boolean.valueOf(com.android.server.autofill.Session.this.mSessionFlags.mAugmentedAutofillOnly));
                return;
            }
            android.view.autofill.AutofillId currentViewId = com.android.server.autofill.Session.this.mCurrentViewId;
            if (currentViewId == null) {
                android.util.Slog.w(com.android.server.autofill.Session.TAG, "No current view id - session might have finished");
                return;
            }
            android.app.assist.AssistStructure structure = (android.app.assist.AssistStructure) resultData.getParcelable(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_STRUCTURE, android.app.assist.AssistStructure.class);
            if (structure == null) {
                android.util.Slog.e(com.android.server.autofill.Session.TAG, "No assist structure - app might have crashed providing it");
                return;
            }
            android.os.Bundle receiverExtras = resultData.getBundle(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_RECEIVER_EXTRAS);
            if (receiverExtras == null) {
                android.util.Slog.e(com.android.server.autofill.Session.TAG, "No receiver extras - app might have crashed providing it");
                return;
            }
            int requestId = receiverExtras.getInt(com.android.server.autofill.Session.EXTRA_REQUEST_ID);
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(com.android.server.autofill.Session.TAG, "New structure for requestId " + requestId + ": " + structure);
            }
            synchronized (com.android.server.autofill.Session.this.mLock) {
                try {
                    try {
                        try {
                            structure.ensureDataForAutofill();
                            java.util.ArrayList<android.view.autofill.AutofillId> ids = com.android.server.autofill.Helper.getAutofillIds(structure, false);
                            for (int i = 0; i < ids.size(); i++) {
                                ids.get(i).setSessionId(com.android.server.autofill.Session.this.id);
                            }
                            int flags2 = structure.getFlags();
                            if (!com.android.server.autofill.Session.this.mCompatMode) {
                                flags = flags2;
                            } else {
                                java.lang.String[] urlBarIds = com.android.server.autofill.Session.this.mService.getUrlBarResourceIdsForCompatMode(com.android.server.autofill.Session.this.mComponentName.getPackageName());
                                if (com.android.server.autofill.Helper.sDebug) {
                                    android.util.Slog.d(com.android.server.autofill.Session.TAG, "url_bars in compat mode: " + java.util.Arrays.toString(urlBarIds));
                                }
                                if (urlBarIds != null) {
                                    com.android.server.autofill.Session.this.mUrlBar = com.android.server.autofill.Helper.sanitizeUrlBar(structure, urlBarIds);
                                    if (com.android.server.autofill.Session.this.mUrlBar != null) {
                                        android.view.autofill.AutofillId urlBarId = com.android.server.autofill.Session.this.mUrlBar.getAutofillId();
                                        if (com.android.server.autofill.Helper.sDebug) {
                                            android.util.Slog.d(com.android.server.autofill.Session.TAG, "Setting urlBar as id=" + urlBarId + " and domain " + com.android.server.autofill.Session.this.mUrlBar.getWebDomain());
                                        }
                                        com.android.server.autofill.ViewState viewState = new com.android.server.autofill.ViewState(urlBarId, com.android.server.autofill.Session.this, 512, com.android.server.autofill.Session.this.mIsPrimaryCredential);
                                        com.android.server.autofill.Session.this.mViewStates.put(urlBarId, viewState);
                                    }
                                }
                                flags = flags2 | 2;
                            }
                            com.android.server.autofill.Session.this.mSessionExt.hookSanitizeForParceling(structure);
                            if (com.android.server.autofill.Session.this.mContexts == null) {
                                com.android.server.autofill.Session.this.mContexts = new java.util.ArrayList(1);
                            }
                            com.android.server.autofill.Session.this.mContexts.add(new android.service.autofill.FillContext(requestId, structure, currentViewId));
                            com.android.server.autofill.Session.this.cancelCurrentRequestLocked();
                            int numContexts = com.android.server.autofill.Session.this.mContexts.size();
                            for (int i2 = 0; i2 < numContexts; i2++) {
                                com.android.server.autofill.Session.this.fillContextWithAllowedValuesLocked((android.service.autofill.FillContext) com.android.server.autofill.Session.this.mContexts.get(i2), flags);
                            }
                            java.util.ArrayList<android.service.autofill.FillContext> contexts = com.android.server.autofill.Session.this.mergePreviousSessionLocked(false);
                            java.util.List<java.lang.String> hints = com.android.server.autofill.Session.this.getTypeHintsForProvider();
                            com.android.server.autofill.Session.this.mDelayedFillPendingIntent = com.android.server.autofill.Session.this.createPendingIntent(requestId);
                            com.android.server.autofill.Session.this.mClientState = com.android.server.autofill.Session.this.mSessionExt.hookOnFillRequestClientState(com.android.server.autofill.Session.this.mClientState);
                            android.os.Bundle bundle = com.android.server.autofill.Session.this.mClientState;
                            if (com.android.server.autofill.Session.this.mDelayedFillPendingIntent == null) {
                                intentSender = null;
                            } else {
                                intentSender = com.android.server.autofill.Session.this.mDelayedFillPendingIntent.getIntentSender();
                            }
                            int numContexts2 = flags;
                            android.service.autofill.FillRequest request = new android.service.autofill.FillRequest(requestId, contexts, hints, bundle, numContexts2, null, intentSender);
                            this.mPendingFillRequest = request;
                            maybeRequestFillLocked();
                            if (com.android.server.autofill.Session.this.mActivityToken != null) {
                                com.android.server.autofill.Session.this.mService.sendActivityAssistDataToContentCapture(com.android.server.autofill.Session.this.mActivityToken, resultData);
                            }
                        } catch (java.lang.RuntimeException e) {
                            com.android.server.autofill.Session.this.wtf(e, "Exception lazy loading assist structure for %s: %s", structure.getActivityComponent(), e);
                        }
                    } catch (java.lang.Throwable th) {
                        e = th;
                        throw e;
                    }
                } catch (java.lang.Throwable th2) {
                    e = th2;
                    throw e;
                }
            }
        }

        public void onHandleAssistScreenshot(android.graphics.Bitmap screenshot) {
        }

        void processDelayedFillLocked(int requestId, android.service.autofill.FillResponse response) {
            if (this.mLastFillRequest != null && requestId == this.mLastFillRequest.getId()) {
                android.util.Slog.v(com.android.server.autofill.Session.TAG, "processDelayedFillLocked: calling onFillRequestSuccess with new response");
                com.android.server.autofill.Session.this.onFillRequestSuccess(requestId, response, com.android.server.autofill.Session.this.mService.getServicePackageName(), this.mLastFillRequest.getFlags());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.service.autofill.FillRequest addCredentialManagerDataToClientState(android.service.autofill.FillRequest pendingFillRequest, android.view.inputmethod.InlineSuggestionsRequest pendingInlineSuggestionsRequest, int sessionId) {
        if (pendingFillRequest.getClientState() == null) {
            pendingFillRequest = new android.service.autofill.FillRequest(pendingFillRequest.getId(), pendingFillRequest.getFillContexts(), pendingFillRequest.getHints(), new android.os.Bundle(), pendingFillRequest.getFlags(), pendingInlineSuggestionsRequest, pendingFillRequest.getDelayedFillIntentSender());
        }
        pendingFillRequest.getClientState().putInt(SESSION_ID_KEY, sessionId);
        pendingFillRequest.getClientState().putInt(REQUEST_ID_KEY, pendingFillRequest.getId());
        android.os.ResultReceiver resultReceiver = constructCredentialManagerCallback(pendingFillRequest.getId());
        pendingFillRequest.getClientState().putParcelable("android.credentials.AUTOFILL_RESULT_RECEIVER", resultReceiver);
        return pendingFillRequest;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<java.lang.String> getTypeHintsForProvider() {
        if (!this.mService.isPccClassificationEnabled()) {
            return java.util.Collections.EMPTY_LIST;
        }
        java.lang.String typeHints = this.mService.getMaster().getPccProviderHints();
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "TypeHints flag:" + typeHints);
        }
        if (android.text.TextUtils.isEmpty(typeHints)) {
            return new java.util.ArrayList();
        }
        return java.util.List.of((java.lang.Object[]) typeHints.split(PCC_HINTS_DELIMITER));
    }

    private final class PccAssistDataReceiverImpl extends android.app.IAssistDataReceiver.Stub {
        private PccAssistDataReceiverImpl() {
        }

        void maybeRequestFieldClassificationFromServiceLocked() {
            if (com.android.server.autofill.Session.this.mClassificationState.mPendingFieldClassificationRequest == null) {
                android.util.Slog.w(com.android.server.autofill.Session.TAG, "Received AssistData without pending classification request");
                return;
            }
            com.android.server.autofill.RemoteFieldClassificationService remoteFieldClassificationService = com.android.server.autofill.Session.this.mService.getRemoteFieldClassificationServiceLocked();
            if (remoteFieldClassificationService != null) {
                java.lang.ref.WeakReference<com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks> fieldClassificationServiceCallbacksWeakRef = new java.lang.ref.WeakReference<>(com.android.server.autofill.Session.this);
                remoteFieldClassificationService.onFieldClassificationRequest(com.android.server.autofill.Session.this.mClassificationState.mPendingFieldClassificationRequest, fieldClassificationServiceCallbacksWeakRef);
            }
            com.android.server.autofill.Session.this.mClassificationState.onFieldClassificationRequestSent();
        }

        public void onHandleAssistData(android.os.Bundle resultData) throws android.os.RemoteException {
            android.app.assist.AssistStructure structure = (android.app.assist.AssistStructure) resultData.getParcelable(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_STRUCTURE, android.app.assist.AssistStructure.class);
            if (structure == null) {
                android.util.Slog.e(com.android.server.autofill.Session.TAG, "No assist structure for pcc detection - app might have crashed providing it");
                return;
            }
            android.os.Bundle receiverExtras = resultData.getBundle(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_RECEIVER_EXTRAS);
            if (receiverExtras == null) {
                android.util.Slog.e(com.android.server.autofill.Session.TAG, "No receiver extras for pcc detection - app might have crashed providing it");
                return;
            }
            int requestId = receiverExtras.getInt(com.android.server.autofill.Session.EXTRA_REQUEST_ID);
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(com.android.server.autofill.Session.TAG, "New structure for PCC Detection: requestId " + requestId + ": " + structure);
            }
            synchronized (com.android.server.autofill.Session.this.mLock) {
                try {
                    try {
                        structure.ensureDataForAutofill();
                        java.util.ArrayList<android.view.autofill.AutofillId> ids = com.android.server.autofill.Helper.getAutofillIds(structure, false);
                        for (int i = 0; i < ids.size(); i++) {
                            ids.get(i).setSessionId(com.android.server.autofill.Session.this.id);
                        }
                        com.android.server.autofill.Session.this.mClassificationState.onAssistStructureReceived(structure);
                        maybeRequestFieldClassificationFromServiceLocked();
                    } catch (java.lang.RuntimeException e) {
                        com.android.server.autofill.Session.this.wtf(e, "Exception lazy loading assist structure for %s: %s", structure.getActivityComponent(), e);
                    }
                } catch (java.lang.Throwable th) {
                    throw th;
                }
            }
        }

        public void onHandleAssistScreenshot(android.graphics.Bitmap screenshot) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.app.PendingIntent createPendingIntent(int requestId) {
        android.util.Slog.d(TAG, "createPendingIntent for request " + requestId);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.content.Intent intent = new android.content.Intent(ACTION_DELAYED_FILL).setPackage(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME).putExtra(EXTRA_REQUEST_ID, requestId);
            android.app.PendingIntent pendingIntent = android.app.PendingIntent.getBroadcast(this.mContext, this.id, intent, 1375731712);
            return pendingIntent;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private void clearPendingIntentLocked() {
        android.util.Slog.d(TAG, "clearPendingIntentLocked");
        if (this.mDelayedFillPendingIntent == null) {
            return;
        }
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mDelayedFillPendingIntent.cancel();
            this.mDelayedFillPendingIntent = null;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private void registerDelayedFillBroadcastLocked() {
        if (!this.mDelayedFillBroadcastReceiverRegistered) {
            android.util.Slog.v(TAG, "registerDelayedFillBroadcastLocked()");
            android.content.IntentFilter intentFilter = new android.content.IntentFilter(ACTION_DELAYED_FILL);
            this.mContext.registerReceiver(this.mDelayedFillBroadcastReceiver, intentFilter);
            this.mDelayedFillBroadcastReceiverRegistered = true;
        }
    }

    private void unregisterDelayedFillBroadcastLocked() {
        if (this.mDelayedFillBroadcastReceiverRegistered) {
            android.util.Slog.v(TAG, "unregisterDelayedFillBroadcastLocked()");
            this.mContext.unregisterReceiver(this.mDelayedFillBroadcastReceiver);
            this.mDelayedFillBroadcastReceiverRegistered = false;
        }
    }

    private android.view.autofill.AutofillId[] getIdsOfAllViewStatesLocked() {
        int numViewState = this.mViewStates.size();
        android.view.autofill.AutofillId[] ids = new android.view.autofill.AutofillId[numViewState];
        for (int i = 0; i < numViewState; i++) {
            ids[i] = this.mViewStates.valueAt(i).id;
        }
        return ids;
    }

    public java.lang.String findByAutofillId(android.view.autofill.AutofillId id) {
        synchronized (this.mLock) {
            android.view.autofill.AutofillValue value = findValueLocked(id);
            if (value != null) {
                if (value.isText()) {
                    return value.getTextValue().toString();
                }
                if (value.isList()) {
                    java.lang.CharSequence[] options = getAutofillOptionsFromContextsLocked(id);
                    if (options != null) {
                        int index = value.getListValue();
                        java.lang.CharSequence option = options[index];
                        return option != null ? option.toString() : null;
                    }
                    android.util.Slog.w(TAG, "findByAutofillId(): no autofill options for id " + id);
                }
            }
            return null;
        }
    }

    public android.view.autofill.AutofillValue findRawValueByAutofillId(android.view.autofill.AutofillId id) {
        android.view.autofill.AutofillValue autofillValueFindValueLocked;
        synchronized (this.mLock) {
            autofillValueFindValueLocked = findValueLocked(id);
        }
        return autofillValueFindValueLocked;
    }

    private android.view.autofill.AutofillValue findValueLocked(android.view.autofill.AutofillId autofillId) {
        android.view.autofill.AutofillValue value = findValueFromThisSessionOnlyLocked(autofillId);
        if (value != null) {
            return getSanitizedValue(com.android.server.autofill.Helper.createSanitizers(getSaveInfoLocked()), autofillId, value);
        }
        java.util.ArrayList<com.android.server.autofill.Session> previousSessions = this.mService.getPreviousSessionsLocked(this);
        if (previousSessions != null) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "findValueLocked(): looking on " + previousSessions.size() + " previous sessions for autofillId " + autofillId);
            }
            for (int i = 0; i < previousSessions.size(); i++) {
                com.android.server.autofill.Session previousSession = previousSessions.get(i);
                android.view.autofill.AutofillValue previousValue = previousSession.findValueFromThisSessionOnlyLocked(autofillId);
                if (previousValue != null) {
                    return getSanitizedValue(com.android.server.autofill.Helper.createSanitizers(previousSession.getSaveInfoLocked()), autofillId, previousValue);
                }
            }
            return null;
        }
        return null;
    }

    private android.view.autofill.AutofillValue findValueFromThisSessionOnlyLocked(android.view.autofill.AutofillId autofillId) {
        android.view.autofill.AutofillValue candidateSaveValue;
        com.android.server.autofill.ViewState state = this.mViewStates.get(autofillId);
        if (state == null) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "findValueLocked(): no view state for " + autofillId);
                return null;
            }
            return null;
        }
        android.view.autofill.AutofillValue value = state.getCurrentValue();
        if ((value == null || value.isEmpty()) && (candidateSaveValue = state.getCandidateSaveValue()) != null && !candidateSaveValue.isEmpty()) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "findValueLocked(): current value for " + autofillId + " is empty, using candidateSaveValue instead.");
            }
            return candidateSaveValue;
        }
        if (value == null && com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "findValueLocked(): no current value for " + autofillId + ", checking value from previous fill contexts");
            return getValueFromContextsLocked(autofillId);
        }
        return value;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fillContextWithAllowedValuesLocked(android.service.autofill.FillContext fillContext, int flags) {
        android.app.assist.AssistStructure.ViewNode[] nodes = fillContext.findViewNodesByAutofillIds(getIdsOfAllViewStatesLocked());
        int numViewState = this.mViewStates.size();
        for (int i = 0; i < numViewState; i++) {
            com.android.server.autofill.ViewState viewState = this.mViewStates.valueAt(i);
            android.app.assist.AssistStructure.ViewNode node = nodes[i];
            if (node == null) {
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "fillContextWithAllowedValuesLocked(): no node for " + viewState.id);
                }
            } else {
                android.view.autofill.AutofillValue currentValue = viewState.getCurrentValue();
                android.view.autofill.AutofillValue filledValue = viewState.getAutofilledValue();
                android.app.assist.AssistStructure.AutofillOverlay overlay = new android.app.assist.AssistStructure.AutofillOverlay();
                if (filledValue != null && filledValue.equals(currentValue)) {
                    overlay.value = currentValue;
                }
                if (this.mCurrentViewId != null) {
                    overlay.focused = this.mCurrentViewId.equals(viewState.id);
                    if (overlay.focused && (flags & 1) != 0) {
                        overlay.value = currentValue;
                    }
                }
                node.setAutofillOverlay(overlay);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelCurrentRequestLocked() {
        if (this.mRemoteFillService == null) {
            wtf(null, "cancelCurrentRequestLocked() called without a remote service. mForAugmentedAutofillOnly: %s", java.lang.Boolean.valueOf(this.mSessionFlags.mAugmentedAutofillOnly));
            return;
        }
        int canceledRequest = this.mRemoteFillService.cancelCurrentRequest();
        if (canceledRequest != Integer.MIN_VALUE && this.mContexts != null) {
            int numContexts = this.mContexts.size();
            for (int i = numContexts - 1; i >= 0; i--) {
                if (this.mContexts.get(i).getRequestId() == canceledRequest) {
                    if (com.android.server.autofill.Helper.sDebug) {
                        android.util.Slog.d(TAG, "cancelCurrentRequest(): id = " + canceledRequest);
                    }
                    this.mContexts.remove(i);
                    return;
                }
            }
        }
    }

    private boolean isViewFocusedLocked(int flags) {
        return (flags & 16) == 0;
    }

    private void requestNewFillResponseLocked(com.android.server.autofill.ViewState viewState, int newState, int flags) {
        boolean isSecondary = shouldRequestSecondaryProvider(flags);
        android.service.autofill.FillResponse existingResponse = isSecondary ? viewState.getSecondaryResponse() : viewState.getResponse();
        this.mFillRequestEventLogger.startLogForNewRequest();
        this.mRequestCount++;
        this.mFillRequestEventLogger.maybeSetAppPackageUid(this.uid);
        this.mFillRequestEventLogger.maybeSetFlags(this.mFlags);
        if (this.mPreviouslyFillDialogPotentiallyStarted) {
            this.mFillRequestEventLogger.maybeSetRequestTriggerReason(3);
        } else if ((flags & 1) != 0) {
            this.mFillRequestEventLogger.maybeSetRequestTriggerReason(1);
        } else {
            this.mFillRequestEventLogger.maybeSetRequestTriggerReason(4);
        }
        if (existingResponse != null) {
            setViewStatesLocked(existingResponse, 1, true, true);
            this.mFillRequestEventLogger.maybeSetRequestTriggerReason(5);
        }
        this.mSessionFlags.mExpiredResponse = false;
        this.mSessionState = 1;
        if (this.mSessionFlags.mAugmentedAutofillOnly || this.mRemoteFillService == null) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "requestNewFillResponse(): triggering augmented autofill instead (mForAugmentedAutofillOnly=" + this.mSessionFlags.mAugmentedAutofillOnly + ", flags=" + flags + ")");
            }
            this.mSessionFlags.mAugmentedAutofillOnly = true;
            this.mFillRequestEventLogger.maybeSetRequestId(1);
            this.mFillRequestEventLogger.maybeSetIsAugmented(true);
            this.mFillRequestEventLogger.logAndEndEvent();
            triggerAugmentedAutofillLocked(flags);
            return;
        }
        viewState.setState(newState);
        int requestId = mRequestId.nextId(isSecondary);
        int ordinal = this.mRequestLogs.size() + 1;
        android.metrics.LogMaker log = newLogMaker(907).addTaggedData(1454, java.lang.Integer.valueOf(ordinal));
        if (flags != 0) {
            log.addTaggedData(1452, java.lang.Integer.valueOf(flags));
        }
        this.mRequestLogs.put(requestId, log);
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Requesting structure for request #" + ordinal + " ,requestId=" + requestId + ", flags=" + flags);
        }
        boolean isCredmanRequested = (flags & 2048) != 0;
        this.mPresentationStatsEventLogger.maybeSetRequestId(requestId);
        this.mPresentationStatsEventLogger.maybeSetIsCredentialRequest(isCredmanRequested);
        this.mPresentationStatsEventLogger.maybeSetFieldClassificationRequestId(this.mFieldClassificationIdSnapshot);
        this.mPresentationStatsEventLogger.maybeSetAutofillServiceUid(getAutofillServiceUid());
        this.mFillRequestEventLogger.maybeSetRequestId(requestId);
        this.mFillRequestEventLogger.maybeSetAutofillServiceUid(getAutofillServiceUid());
        this.mSaveEventLogger.maybeSetAutofillServiceUid(getAutofillServiceUid());
        this.mSessionCommittedEventLogger.maybeSetAutofillServiceUid(getAutofillServiceUid());
        if (this.mSessionFlags.mInlineSupportedByService) {
            this.mFillRequestEventLogger.maybeSetInlineSuggestionHostUid(this.mContext, this.userId);
        }
        this.mFillRequestEventLogger.maybeSetIsFillDialogEligible(!this.mSessionFlags.mFillDialogDisabled);
        cancelCurrentRequestLocked();
        if (this.mService.isPccClassificationEnabled() && this.mClassificationState.mHintsToAutofillIdMap == null) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "triggering field classification");
            }
            requestAssistStructureForPccLocked(flags | 512);
        }
        com.android.server.autofill.RemoteInlineSuggestionRenderService remoteRenderService = this.mService.getRemoteInlineSuggestionRenderServiceLocked();
        if (this.mSessionFlags.mInlineSupportedByService && remoteRenderService != null) {
            if (isViewFocusedLocked(flags) || isRequestSupportFillDialog(flags)) {
                java.util.function.Consumer<android.view.inputmethod.InlineSuggestionsRequest> inlineSuggestionsRequestConsumer = this.mAssistReceiver.newAutofillRequestLocked(viewState, true);
                if (inlineSuggestionsRequestConsumer != null) {
                    android.view.autofill.AutofillId focusedId = this.mCurrentViewId;
                    java.lang.ref.WeakReference sessionWeakReference = new java.lang.ref.WeakReference(this);
                    com.android.server.autofill.InlineSuggestionRendorInfoCallbackOnResultListener inlineSuggestionRendorInfoCallbackOnResultListener = new com.android.server.autofill.InlineSuggestionRendorInfoCallbackOnResultListener(sessionWeakReference, requestId, inlineSuggestionsRequestConsumer, focusedId);
                    android.os.RemoteCallback inlineSuggestionRendorInfoCallback = new android.os.RemoteCallback(inlineSuggestionRendorInfoCallbackOnResultListener, this.mHandler);
                    remoteRenderService.getInlineSuggestionsRendererInfo(inlineSuggestionRendorInfoCallback);
                    viewState.setState(65536);
                }
                requestAssistStructureLocked(requestId, flags);
            }
        }
        this.mAssistReceiver.newAutofillRequestLocked(viewState, false);
        requestAssistStructureLocked(requestId, flags);
    }

    private boolean isRequestSupportFillDialog(int flags) {
        return (flags & 64) != 0;
    }

    private void requestAssistStructureForPccLocked(int flags) {
        int requestId;
        if (this.mClassificationState.shouldTriggerRequest()) {
            this.mFillRequestIdSnapshot = sIdCounterForPcc.get();
            this.mClassificationState.updatePendingRequest();
            do {
                requestId = sIdCounterForPcc.getAndIncrement();
            } while (requestId == Integer.MIN_VALUE);
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "request id is " + requestId + ", requesting assist structure for pcc");
            }
            try {
                android.os.Bundle receiverExtras = new android.os.Bundle();
                receiverExtras.putInt(EXTRA_REQUEST_ID, requestId);
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    if (!android.app.ActivityTaskManager.getService().requestAutofillData(this.mPccAssistReceiver, receiverExtras, this.mActivityToken, flags)) {
                        android.util.Slog.w(TAG, "failed to request autofill data for " + this.mActivityToken);
                    }
                    android.os.Binder.restoreCallingIdentity(identity);
                } catch (java.lang.Throwable th) {
                    android.os.Binder.restoreCallingIdentity(identity);
                    throw th;
                }
            } catch (android.os.RemoteException e) {
            }
        }
    }

    private void requestAssistStructureLocked(int requestId, int flags) {
        try {
            android.os.Bundle receiverExtras = new android.os.Bundle();
            receiverExtras.putInt(EXTRA_REQUEST_ID, requestId);
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                if (!android.app.ActivityTaskManager.getService().requestAutofillData(this.mAssistReceiver, receiverExtras, this.mActivityToken, flags)) {
                    android.util.Slog.w(TAG, "failed to request autofill data for " + this.mActivityToken);
                }
                android.os.Binder.restoreCallingIdentity(identity);
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(identity);
                throw th;
            }
        } catch (android.os.RemoteException e) {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    Session(com.android.server.autofill.AutofillManagerServiceImpl autofillManagerServiceImpl, com.android.server.autofill.ui.AutoFillUI autoFillUI, android.content.Context context, android.os.Handler handler, int i, java.lang.Object obj, int i2, int i3, int i4, android.os.IBinder iBinder, android.os.IBinder iBinder2, boolean z, android.util.LocalLog localLog, android.util.LocalLog localLog2, android.content.ComponentName componentName, android.content.ComponentName componentName2, boolean z2, boolean z3, boolean z4, int i5, com.android.server.inputmethod.InputMethodManagerInternal inputMethodManagerInternal, boolean z5) throws java.lang.Throwable {
        android.content.ComponentName componentName3;
        android.content.ComponentName componentName4;
        int i6;
        com.android.server.autofill.RemoteFillService remoteFillService;
        com.android.server.autofill.SecondaryProviderHandler secondaryProviderHandler;
        android.content.ComponentName componentName5;
        this.mSessionState = 0;
        if (i2 < 0) {
            wtf(null, "Non-positive sessionId: %s", java.lang.Integer.valueOf(i2));
        }
        this.id = i2;
        this.mFlags = i5;
        this.userId = i;
        this.taskId = i3;
        this.uid = i4;
        this.mService = autofillManagerServiceImpl;
        this.mLock = obj;
        this.mUi = autoFillUI;
        this.mHandler = handler;
        this.mCredentialAutofillService = getCredentialAutofillService(context);
        if (z5) {
            android.content.ComponentName componentName6 = this.mCredentialAutofillService;
            if (componentName == null) {
                componentName5 = componentName6;
            } else {
                componentName5 = componentName6;
                if (!componentName.equals(this.mCredentialAutofillService)) {
                    componentName3 = componentName;
                    componentName4 = componentName5;
                }
            }
            componentName3 = null;
            componentName4 = componentName5;
        } else {
            componentName3 = this.mCredentialAutofillService;
            componentName4 = componentName;
        }
        android.util.Slog.v(TAG, "Primary service component name: " + componentName4 + ", secondary service component name: " + componentName3);
        if (componentName4 == null) {
            i6 = 1;
            remoteFillService = null;
        } else {
            i6 = 1;
            remoteFillService = new com.android.server.autofill.RemoteFillService(context, componentName4, i, this, z3, this.mCredentialAutofillService);
        }
        this.mRemoteFillService = remoteFillService;
        if (componentName3 == null) {
            secondaryProviderHandler = null;
        } else {
            secondaryProviderHandler = new com.android.server.autofill.SecondaryProviderHandler(context, i, z3, new com.android.server.autofill.SecondaryProviderHandler.SecondaryProviderCallback() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda1
                @Override // com.android.server.autofill.SecondaryProviderHandler.SecondaryProviderCallback
                public final void onSecondaryFillResponse(android.service.autofill.FillResponse fillResponse, int i7) {
                    this.f$0.onSecondaryFillResponse(fillResponse, i7);
                }
            }, componentName3, this.mCredentialAutofillService);
        }
        this.mSecondaryProviderHandler = secondaryProviderHandler;
        this.mActivityToken = iBinder;
        this.mHasCallback = z;
        this.mUiLatencyHistory = localLog;
        this.mWtfHistory = localLog2;
        this.mContext = com.android.server.autofill.Helper.getDisplayContext(context, ((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class)).getDisplayId(iBinder));
        this.mComponentName = componentName2;
        this.mCompatMode = z2;
        this.mSessionState = i6;
        this.mStartTime = android.os.SystemClock.elapsedRealtime();
        this.mLatencyBaseTime = this.mStartTime;
        this.mRequestCount = 0;
        this.mPresentationStatsEventLogger = com.android.server.autofill.PresentationStatsEventLogger.createPresentationLog(i2, i4, this.mLatencyBaseTime);
        this.mFillRequestEventLogger = com.android.server.autofill.FillRequestEventLogger.forSessionId(i2);
        this.mFillResponseEventLogger = com.android.server.autofill.FillResponseEventLogger.forSessionId(i2);
        this.mSessionCommittedEventLogger = com.android.server.autofill.SessionCommittedEventLogger.forSessionId(i2);
        this.mSessionCommittedEventLogger.maybeSetComponentPackageUid(i4);
        this.mSaveEventLogger = com.android.server.autofill.SaveEventLogger.forSessionId(i2, this.mLatencyBaseTime);
        this.mIsPrimaryCredential = z5;
        this.mSessionExt.setRemoteServiceComponentName(componentName);
        this.mIgnoreViewStateResetToEmpty = android.view.autofill.AutofillFeatureFlags.shouldIgnoreViewStateResetToEmpty();
        synchronized (this.mLock) {
            try {
                try {
                    this.mSessionFlags = new com.android.server.autofill.Session.SessionFlags();
                    this.mSessionFlags.mAugmentedAutofillOnly = z4;
                    this.mSessionFlags.mInlineSupportedByService = this.mService.isInlineSuggestionsEnabledLocked();
                    setClientLocked(iBinder2);
                    this.mInlineSessionController = new com.android.server.autofill.AutofillInlineSessionController(inputMethodManagerInternal, i, componentName2, handler, this.mLock, new com.android.server.autofill.ui.InlineFillUi.InlineUiEventCallback() { // from class: com.android.server.autofill.Session.2
                        @Override // com.android.server.autofill.ui.InlineFillUi.InlineUiEventCallback
                        public void notifyInlineUiShown(android.view.autofill.AutofillId autofillId) {
                            com.android.server.autofill.Session.this.notifyFillUiShown(autofillId);
                        }

                        @Override // com.android.server.autofill.ui.InlineFillUi.InlineUiEventCallback
                        public void notifyInlineUiHidden(android.view.autofill.AutofillId autofillId) {
                            com.android.server.autofill.Session.this.notifyFillUiHidden(autofillId);
                        }
                    });
                    this.mMetricsLogger.write(newLogMaker(906).addTaggedData(1452, java.lang.Integer.valueOf(i5)));
                    this.mLogViewEntered = false;
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    private android.content.ComponentName getCredentialAutofillService(android.content.Context context) {
        android.content.ComponentName componentName = null;
        java.lang.String credentialManagerAutofillCompName = context.getResources().getString(android.R.string.config_defaultModuleMetadataProvider);
        if (credentialManagerAutofillCompName != null && !credentialManagerAutofillCompName.isEmpty()) {
            componentName = android.content.ComponentName.unflattenFromString(credentialManagerAutofillCompName);
        }
        if (componentName == null) {
            android.util.Slog.w(TAG, "Invalid CredentialAutofillService");
        }
        return componentName;
    }

    android.os.IBinder getActivityTokenLocked() {
        return this.mActivityToken;
    }

    void switchActivity(android.os.IBinder newActivity, android.os.IBinder newClient) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#switchActivity() rejected - session: " + this.id + " destroyed");
                return;
            }
            this.mActivityToken = newActivity;
            setClientLocked(newClient);
            updateTrackedIdsLocked();
        }
    }

    private void setClientLocked(android.os.IBinder client) {
        unlinkClientVultureLocked();
        this.mClient = android.view.autofill.IAutoFillManagerClient.Stub.asInterface(client);
        this.mClientVulture = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda9
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                this.f$0.lambda$setClientLocked$0();
            }
        };
        try {
            this.mClient.asBinder().linkToDeath(this.mClientVulture, 0);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "could not set binder death listener on autofill client: " + e);
            this.mClientVulture = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setClientLocked$0() {
        synchronized (this.mLock) {
            android.util.Slog.d(TAG, "handling death of " + this.mActivityToken + " when saving=" + this.mSessionFlags.mShowingSaveUi);
            if (this.mSessionFlags.mShowingSaveUi) {
                this.mUi.hideFillUi(this);
            } else {
                this.mUi.destroyAll(this.mPendingSaveUi, this, false);
            }
            if (this.mSessionExt.isOplusAutofillService()) {
                forceRemoveFromServiceLocked();
            }
        }
    }

    private void unlinkClientVultureLocked() {
        if (this.mClient != null && this.mClientVulture != null) {
            boolean unlinked = this.mClient.asBinder().unlinkToDeath(this.mClientVulture, 0);
            if (!unlinked) {
                android.util.Slog.w(TAG, "unlinking vulture from death failed for " + this.mActivityToken);
            }
            this.mClientVulture = null;
        }
    }

    @Override // com.android.server.autofill.RemoteFillService.FillServiceCallbacks
    public void onFillRequestSuccess(int requestId, android.service.autofill.FillResponse response, java.lang.String servicePackageName, int requestFlags) {
        synchronized (this.mLock) {
            this.mFillResponseEventLogger.startLogForNewResponse();
            this.mFillResponseEventLogger.maybeSetRequestId(requestId);
            this.mFillResponseEventLogger.maybeSetAppPackageUid(this.uid);
            this.mFillResponseEventLogger.maybeSetResponseStatus(2);
            this.mFillResponseEventLogger.startResponseProcessingTime();
            long fillRequestReceivedRelativeTimestamp = android.os.SystemClock.elapsedRealtime() - this.mLatencyBaseTime;
            this.mPresentationStatsEventLogger.maybeSetFillResponseReceivedTimestampMs((int) fillRequestReceivedRelativeTimestamp);
            this.mFillResponseEventLogger.maybeSetLatencyFillResponseReceivedMillis((int) fillRequestReceivedRelativeTimestamp);
            this.mFillResponseEventLogger.maybeSetDetectionPreference(getDetectionPreferenceForLogging());
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#onFillRequestSuccess() rejected - session: " + this.id + " destroyed");
                this.mFillResponseEventLogger.maybeSetResponseStatus(5);
                this.mFillResponseEventLogger.logAndEndEvent();
                return;
            }
            if (this.mSessionFlags.mShowingSaveUi) {
                android.util.Slog.w(TAG, "Call to Session#onFillRequestSuccess() rejected - session: " + this.id + " is showing saveUi");
                this.mFillResponseEventLogger.maybeSetResponseStatus(5);
                this.mFillResponseEventLogger.logAndEndEvent();
                return;
            }
            android.metrics.LogMaker requestLog = this.mRequestLogs.get(requestId);
            if (requestLog != null) {
                requestLog.setType(10);
            } else {
                android.util.Slog.w(TAG, "onFillRequestSuccess(): no request log for id " + requestId);
            }
            if (response == null) {
                this.mFillResponseEventLogger.maybeSetTotalDatasetsProvided(0);
                if (requestLog != null) {
                    requestLog.addTaggedData(909, -1);
                }
                processNullResponseLocked(requestId, requestFlags);
                return;
            }
            android.view.autofill.AutofillId[] fieldClassificationIds = response.getFieldClassificationIds();
            if (fieldClassificationIds == null || this.mService.isFieldClassificationEnabledLocked()) {
                this.mLastFillDialogTriggerIds = response.getFillDialogTriggerIds();
                if ((response.getFlags() & 4) != 0) {
                    android.util.Slog.v(TAG, "Service requested to wait for delayed fill response.");
                    registerDelayedFillBroadcastLocked();
                }
                this.mService.setLastResponseLocked(this.id, response);
                if (this.mLogViewEntered) {
                    this.mLogViewEntered = false;
                    this.mService.logViewEntered(this.id, null);
                }
                long disableDuration = response.getDisableDuration();
                boolean autofillDisabled = disableDuration > 0;
                if (autofillDisabled) {
                    int flags = response.getFlags();
                    boolean disableActivityOnly = (flags & 2) != 0;
                    notifyDisableAutofillToClient(disableDuration, disableActivityOnly ? this.mComponentName : null);
                    if (disableActivityOnly) {
                        this.mService.disableAutofillForActivity(this.mComponentName, disableDuration, this.id, this.mCompatMode);
                    } else {
                        this.mService.disableAutofillForApp(this.mComponentName.getPackageName(), disableDuration, this.id, this.mCompatMode);
                    }
                    synchronized (this.mLock) {
                        this.mSessionFlags.mAutofillDisabled = true;
                        if (triggerAugmentedAutofillLocked(requestFlags) != null) {
                            this.mSessionFlags.mAugmentedAutofillOnly = true;
                            if (com.android.server.autofill.Helper.sDebug) {
                                android.util.Slog.d(TAG, "Service disabled autofill for " + this.mComponentName + ", but session is kept for augmented autofill only");
                            }
                            return;
                        } else if (com.android.server.autofill.Helper.sDebug) {
                            java.lang.StringBuilder message = new java.lang.StringBuilder("Service disabled autofill for ").append(this.mComponentName).append(": flags=").append(flags).append(", duration=");
                            android.util.TimeUtils.formatDuration(disableDuration, message);
                            android.util.Slog.d(TAG, message.toString());
                        }
                    }
                }
                java.util.List<android.service.autofill.Dataset> datasetList = response.getDatasets();
                if (((datasetList == null || datasetList.isEmpty()) && response.getAuthentication() == null) || autofillDisabled) {
                    notifyUnavailableToClient(autofillDisabled ? 4 : 0, null);
                    synchronized (this.mLock) {
                        this.mInlineSessionController.setInlineFillUiLocked(com.android.server.autofill.ui.InlineFillUi.emptyUi(this.mCurrentViewId));
                    }
                }
                if (requestLog != null) {
                    requestLog.addTaggedData(909, java.lang.Integer.valueOf(response.getDatasets() == null ? 0 : response.getDatasets().size()));
                    if (fieldClassificationIds != null) {
                        requestLog.addTaggedData(1271, java.lang.Integer.valueOf(fieldClassificationIds.length));
                    }
                }
                int datasetCount = datasetList == null ? 0 : datasetList.size();
                synchronized (this.mLock) {
                    this.mFillResponseEventLogger.maybeSetTotalDatasetsProvided(datasetCount);
                    this.mFillResponseEventLogger.maybeSetAvailableCount(datasetCount);
                    processResponseLockedForPcc(response, response.getClientState(), requestFlags);
                    this.mFillResponseEventLogger.maybeSetLatencyResponseProcessingMillis();
                    this.mFillResponseEventLogger.logAndEndEvent();
                }
                return;
            }
            android.util.Slog.w(TAG, "Ignoring " + response + " because field detection is disabled");
            processNullResponseLocked(requestId, requestFlags);
        }
    }

    private void processResponseLockedForPcc(android.service.autofill.FillResponse response, android.os.Bundle newClientState, int flags) {
        synchronized (this.mLock) {
            android.service.autofill.FillResponse response2 = getEffectiveFillResponse(response);
            if (isEmptyResponse(response2)) {
                processNullResponseLocked(response2 != null ? response2.getRequestId() : 0, flags);
            } else {
                processResponseLocked(response2, newClientState, flags);
            }
        }
    }

    private boolean isEmptyResponse(android.service.autofill.FillResponse response) {
        boolean z = true;
        if (response == null) {
            return true;
        }
        android.service.autofill.SaveInfo saveInfo = response.getSaveInfo();
        synchronized (this.mLock) {
            if ((response.getDatasets() != null && !response.getDatasets().isEmpty()) || response.getAuthentication() != null || ((saveInfo != null && (!com.android.internal.util.ArrayUtils.isEmpty(saveInfo.getOptionalIds()) || !com.android.internal.util.ArrayUtils.isEmpty(saveInfo.getRequiredIds()) || (saveInfo.getFlags() & 4) != 0)) || !com.android.internal.util.ArrayUtils.isEmpty(response.getFieldClassificationIds()))) {
                z = false;
            }
        }
        return z;
    }

    private android.service.autofill.FillResponse getEffectiveFillResponse(android.service.autofill.FillResponse response) throws java.lang.Throwable {
        com.android.server.autofill.Session.DatasetComputationContainer resultContainer;
        com.android.server.autofill.Session.DatasetComputationContainer autofillProviderContainer = new com.android.server.autofill.Session.DatasetComputationContainer();
        computeDatasetsForProviderAndUpdateContainer(response, autofillProviderContainer);
        if (!this.mService.isPccClassificationEnabled()) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "PCC classification is disabled");
            }
            return createShallowCopy(response, autofillProviderContainer);
        }
        synchronized (this.mLock) {
            if (this.mClassificationState.mState == 4 && this.mClassificationState.mLastFieldClassificationResponse != null) {
                if (!this.mClassificationState.processResponse()) {
                    return response;
                }
                boolean preferAutofillProvider = this.mService.getMaster().preferProviderOverPcc();
                boolean shouldUseFallback = this.mService.getMaster().shouldUsePccFallback();
                if (preferAutofillProvider && !shouldUseFallback) {
                    if (com.android.server.autofill.Helper.sVerbose) {
                        android.util.Slog.v(TAG, "preferAutofillProvider but no fallback");
                    }
                    return createShallowCopy(response, autofillProviderContainer);
                }
                com.android.server.autofill.Session.DatasetComputationContainer detectionPccContainer = new com.android.server.autofill.Session.DatasetComputationContainer();
                computeDatasetsForPccAndUpdateContainer(response, detectionPccContainer);
                if (preferAutofillProvider) {
                    resultContainer = autofillProviderContainer;
                    if (shouldUseFallback) {
                        addFallbackDatasets(autofillProviderContainer, detectionPccContainer);
                    }
                } else {
                    resultContainer = detectionPccContainer;
                    if (shouldUseFallback) {
                        addFallbackDatasets(detectionPccContainer, autofillProviderContainer);
                    }
                }
                return createShallowCopy(response, resultContainer);
            }
            if (com.android.server.autofill.Helper.sVerbose) {
                boolean z = true;
                java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("PCC classification no last response:").append(this.mClassificationState.mLastFieldClassificationResponse == null).append(" ,ineligible state=");
                if (this.mClassificationState.mState == 4) {
                    z = false;
                }
                android.util.Slog.v(TAG, sbAppend.append(z).toString());
            }
            return createShallowCopy(response, autofillProviderContainer);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSecondaryFillResponse(android.service.autofill.FillResponse fillResponse, int flags) {
        if (fillResponse == null) {
            return;
        }
        synchronized (this.mLock) {
            this.mFillResponseEventLogger.startLogForNewResponse();
            this.mFillResponseEventLogger.maybeSetRequestId(fillResponse.getRequestId());
            this.mFillResponseEventLogger.maybeSetAppPackageUid(this.uid);
            this.mFillResponseEventLogger.maybeSetResponseStatus(2);
            this.mFillResponseEventLogger.startResponseProcessingTime();
            long fillRequestReceivedRelativeTimestamp = android.os.SystemClock.elapsedRealtime() - this.mLatencyBaseTime;
            this.mPresentationStatsEventLogger.maybeSetFillResponseReceivedTimestampMs((int) fillRequestReceivedRelativeTimestamp);
            this.mFillResponseEventLogger.maybeSetLatencyFillResponseReceivedMillis((int) fillRequestReceivedRelativeTimestamp);
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#onSecondaryFillResponse() rejected - session: " + this.id + " destroyed");
                this.mFillResponseEventLogger.maybeSetResponseStatus(5);
                this.mFillResponseEventLogger.logAndEndEvent();
                return;
            }
            java.util.List<android.service.autofill.Dataset> datasetList = fillResponse.getDatasets();
            int datasetCount = datasetList == null ? 0 : datasetList.size();
            this.mFillResponseEventLogger.maybeSetTotalDatasetsProvided(datasetCount);
            this.mFillResponseEventLogger.maybeSetAvailableCount(datasetCount);
            if (this.mSecondaryResponses == null) {
                this.mSecondaryResponses = new android.util.SparseArray<>(2);
            }
            this.mSecondaryResponses.put(fillResponse.getRequestId(), fillResponse);
            setViewStatesLocked(fillResponse, 2, false, false);
            com.android.server.autofill.ViewState currentView = this.mViewStates.get(this.mCurrentViewId);
            if (currentView != null) {
                currentView.maybeCallOnFillReady(flags);
            }
            this.mFillResponseEventLogger.maybeSetLatencyResponseProcessingMillis();
            this.mFillResponseEventLogger.logAndEndEvent();
        }
    }

    private android.service.autofill.FillResponse createShallowCopy(android.service.autofill.FillResponse response, com.android.server.autofill.Session.DatasetComputationContainer container) {
        return android.service.autofill.FillResponse.shallowCopy(response, new java.util.ArrayList(container.mDatasets), getEligibleSaveInfo(response));
    }

    private android.service.autofill.SaveInfo getEligibleSaveInfo(android.service.autofill.FillResponse response) {
        android.service.autofill.SaveInfo saveInfo = response.getSaveInfo();
        if (saveInfo == null || !com.android.internal.util.ArrayUtils.isEmpty(saveInfo.getOptionalIds()) || !com.android.internal.util.ArrayUtils.isEmpty(saveInfo.getRequiredIds()) || (saveInfo.getFlags() & 4) != 0) {
            return saveInfo;
        }
        synchronized (this.mLock) {
            android.util.ArrayMap<java.lang.String, java.util.Set<android.view.autofill.AutofillId>> hintsToAutofillIdMap = this.mClassificationState.mHintsToAutofillIdMap;
            if (hintsToAutofillIdMap != null && !hintsToAutofillIdMap.isEmpty()) {
                android.util.ArraySet<android.view.autofill.AutofillId> ids = new android.util.ArraySet<>();
                int saveType = saveInfo.getType();
                if (saveType == 0) {
                    java.util.Iterator<java.util.Set<android.view.autofill.AutofillId>> it = hintsToAutofillIdMap.values().iterator();
                    while (it.hasNext()) {
                        ids.addAll(it.next());
                    }
                } else {
                    java.util.Set<java.lang.String> hints = com.android.server.autofill.HintsHelper.getHintsForSaveType(saveType);
                    for (java.util.Map.Entry<java.lang.String, java.util.Set<android.view.autofill.AutofillId>> entry : hintsToAutofillIdMap.entrySet()) {
                        java.lang.String hint = entry.getKey();
                        if (hints.contains(hint)) {
                            ids.addAll(entry.getValue());
                        }
                    }
                }
                if (ids.isEmpty()) {
                    return saveInfo;
                }
                android.view.autofill.AutofillId[] autofillIds = new android.view.autofill.AutofillId[ids.size()];
                this.mSaveEventLogger.maybeSetIsFrameworkCreatedSaveInfo(true);
                ids.toArray(autofillIds);
                return android.service.autofill.SaveInfo.copy(saveInfo, autofillIds);
            }
            return saveInfo;
        }
    }

    private static class DatasetComputationContainer {
        java.util.Map<android.view.autofill.AutofillId, java.util.Set<android.service.autofill.Dataset>> mAutofillIdToDatasetMap;
        java.util.Set<android.view.autofill.AutofillId> mAutofillIds;
        java.util.Set<android.service.autofill.Dataset> mDatasets;

        private DatasetComputationContainer() {
            this.mAutofillIds = new java.util.LinkedHashSet();
            this.mDatasets = new java.util.LinkedHashSet();
            this.mAutofillIdToDatasetMap = new java.util.LinkedHashMap();
        }

        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder("DatasetComputationContainer[");
            if (this.mAutofillIds != null) {
                builder.append(", autofillIds=").append(this.mAutofillIds);
            }
            if (this.mDatasets != null) {
                builder.append(", mDatasets=").append(this.mDatasets);
            }
            if (this.mAutofillIdToDatasetMap != null) {
                builder.append(", mAutofillIdToDatasetMap=").append(this.mAutofillIdToDatasetMap);
            }
            return builder.append(']').toString();
        }
    }

    private void addFallbackDatasets(com.android.server.autofill.Session.DatasetComputationContainer c1, com.android.server.autofill.Session.DatasetComputationContainer c2) {
        for (android.view.autofill.AutofillId id : c2.mAutofillIds) {
            if (!c1.mAutofillIds.contains(id)) {
                if (c2.mAutofillIdToDatasetMap.get(id).isEmpty()) {
                    return;
                }
                java.util.Set<android.service.autofill.Dataset> datasets = c2.mAutofillIdToDatasetMap.get(id);
                java.util.Set<android.service.autofill.Dataset> copyDatasets = new java.util.LinkedHashSet<>(datasets);
                c1.mAutofillIds.add(id);
                c1.mAutofillIdToDatasetMap.put(id, copyDatasets);
                c1.mDatasets.addAll(copyDatasets);
                for (android.service.autofill.Dataset dataset : datasets) {
                    for (android.view.autofill.AutofillId currentId : dataset.getFieldIds()) {
                        if (!currentId.equals(id)) {
                            c2.mAutofillIdToDatasetMap.get(currentId).remove(dataset);
                        }
                    }
                }
            }
        }
    }

    private void computeDatasetsForProviderAndUpdateContainer(android.service.autofill.FillResponse response, com.android.server.autofill.Session.DatasetComputationContainer container) {
        int globalPickReason;
        int globalPickReason2;
        boolean isPccEnabled;
        java.util.List<android.service.autofill.Dataset> datasets;
        java.util.Iterator it;
        int pickReason;
        boolean isPccEnabled2 = this.mService.isPccClassificationEnabled();
        if (isPccEnabled2) {
            globalPickReason = 2;
        } else {
            globalPickReason = 1;
        }
        java.util.List<android.service.autofill.Dataset> datasets2 = response.getDatasets();
        if (datasets2 == null) {
            return;
        }
        java.util.Map<android.view.autofill.AutofillId, java.util.Set<android.service.autofill.Dataset>> autofillIdToDatasetMap = new java.util.LinkedHashMap<>();
        java.util.Set<android.service.autofill.Dataset> eligibleDatasets = new java.util.LinkedHashSet<>();
        java.util.Set<android.view.autofill.AutofillId> eligibleAutofillIds = new java.util.LinkedHashSet<>();
        java.util.Iterator it2 = response.getDatasets().iterator();
        while (it2.hasNext()) {
            android.service.autofill.Dataset dataset = (android.service.autofill.Dataset) it2.next();
            if (dataset.getFieldIds() != null && !dataset.getFieldIds().isEmpty()) {
                int pickReason2 = globalPickReason;
                if (dataset.getAutofillDatatypes() == null) {
                    globalPickReason2 = globalPickReason;
                    isPccEnabled = isPccEnabled2;
                    datasets = datasets2;
                    it = it2;
                } else if (dataset.getAutofillDatatypes().isEmpty()) {
                    globalPickReason2 = globalPickReason;
                    isPccEnabled = isPccEnabled2;
                    datasets = datasets2;
                    it = it2;
                } else {
                    boolean conversionRequired = false;
                    int newSize = dataset.getFieldIds().size();
                    java.util.Iterator it3 = dataset.getFieldIds().iterator();
                    while (it3.hasNext()) {
                        if (((android.view.autofill.AutofillId) it3.next()) == null) {
                            conversionRequired = true;
                            newSize--;
                        }
                    }
                    if (newSize != 0) {
                        if (!conversionRequired) {
                            globalPickReason2 = globalPickReason;
                            isPccEnabled = isPccEnabled2;
                            datasets = datasets2;
                            it = it2;
                        } else {
                            int pickReason3 = 3;
                            java.util.ArrayList<android.view.autofill.AutofillId> fieldIds = new java.util.ArrayList<>(newSize);
                            java.util.ArrayList<android.view.autofill.AutofillValue> fieldValues = new java.util.ArrayList<>(newSize);
                            java.util.ArrayList<android.widget.RemoteViews> fieldPresentations = new java.util.ArrayList<>(newSize);
                            globalPickReason2 = globalPickReason;
                            java.util.ArrayList<android.widget.RemoteViews> fieldDialogPresentations = new java.util.ArrayList<>(newSize);
                            java.util.ArrayList<android.service.autofill.InlinePresentation> fieldInlinePresentations = new java.util.ArrayList<>(newSize);
                            isPccEnabled = isPccEnabled2;
                            java.util.ArrayList<android.service.autofill.InlinePresentation> fieldInlineTooltipPresentations = new java.util.ArrayList<>(newSize);
                            datasets = datasets2;
                            java.util.ArrayList<android.service.autofill.Dataset.DatasetFieldFilter> fieldFilters = new java.util.ArrayList<>(newSize);
                            it = it2;
                            int i = 0;
                            while (true) {
                                pickReason = pickReason3;
                                if (i >= dataset.getFieldIds().size()) {
                                    break;
                                }
                                android.view.autofill.AutofillId id = (android.view.autofill.AutofillId) dataset.getFieldIds().get(i);
                                if (id != null) {
                                    fieldIds.add(id);
                                    fieldValues.add((android.view.autofill.AutofillValue) dataset.getFieldValues().get(i));
                                    fieldPresentations.add(dataset.getFieldPresentation(i));
                                    fieldDialogPresentations.add(dataset.getFieldDialogPresentation(i));
                                    fieldInlinePresentations.add(dataset.getFieldInlinePresentation(i));
                                    fieldInlineTooltipPresentations.add(dataset.getFieldInlineTooltipPresentation(i));
                                    fieldFilters.add(dataset.getFilter(i));
                                }
                                i++;
                                pickReason3 = pickReason;
                            }
                            dataset = new android.service.autofill.Dataset(fieldIds, fieldValues, fieldPresentations, fieldDialogPresentations, fieldInlinePresentations, fieldInlineTooltipPresentations, fieldFilters, new java.util.ArrayList(), dataset.getFieldContent(), null, null, null, null, dataset.getId(), dataset.getAuthentication());
                            pickReason2 = pickReason;
                        }
                    }
                }
                dataset.setEligibleReasonReason(pickReason2);
                eligibleDatasets.add(dataset);
                for (android.view.autofill.AutofillId id2 : dataset.getFieldIds()) {
                    eligibleAutofillIds.add(id2);
                    java.util.Set<android.service.autofill.Dataset> datasetForIds = autofillIdToDatasetMap.get(id2);
                    if (datasetForIds == null) {
                        datasetForIds = new java.util.LinkedHashSet<>();
                    }
                    datasetForIds.add(dataset);
                    autofillIdToDatasetMap.put(id2, datasetForIds);
                }
                globalPickReason = globalPickReason2;
                isPccEnabled2 = isPccEnabled;
                datasets2 = datasets;
                it2 = it;
            }
        }
        container.mAutofillIdToDatasetMap = autofillIdToDatasetMap;
        container.mDatasets = eligibleDatasets;
        container.mAutofillIds = eligibleAutofillIds;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v0, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r12v1 */
    /* JADX WARN: Type inference failed for: r12v10 */
    /* JADX WARN: Type inference failed for: r12v11 */
    /* JADX WARN: Type inference failed for: r12v2 */
    /* JADX WARN: Type inference failed for: r12v3 */
    /* JADX WARN: Type inference failed for: r12v4 */
    /* JADX WARN: Type inference failed for: r12v6 */
    /* JADX WARN: Type inference failed for: r12v7 */
    /* JADX WARN: Type inference failed for: r12v9 */
    /* JADX WARN: Type inference failed for: r25v0 */
    /* JADX WARN: Type inference failed for: r25v1 */
    /* JADX WARN: Type inference failed for: r25v10 */
    /* JADX WARN: Type inference failed for: r25v11 */
    /* JADX WARN: Type inference failed for: r25v12 */
    /* JADX WARN: Type inference failed for: r25v13 */
    /* JADX WARN: Type inference failed for: r25v14 */
    /* JADX WARN: Type inference failed for: r25v15 */
    /* JADX WARN: Type inference failed for: r25v16 */
    /* JADX WARN: Type inference failed for: r25v17 */
    /* JADX WARN: Type inference failed for: r25v18 */
    /* JADX WARN: Type inference failed for: r25v19 */
    /* JADX WARN: Type inference failed for: r25v2 */
    /* JADX WARN: Type inference failed for: r25v20 */
    /* JADX WARN: Type inference failed for: r25v21 */
    /* JADX WARN: Type inference failed for: r25v22 */
    /* JADX WARN: Type inference failed for: r25v3 */
    /* JADX WARN: Type inference failed for: r25v4 */
    /* JADX WARN: Type inference failed for: r25v5 */
    /* JADX WARN: Type inference failed for: r25v6 */
    /* JADX WARN: Type inference failed for: r25v7 */
    /* JADX WARN: Type inference failed for: r25v8 */
    /* JADX WARN: Type inference failed for: r25v9 */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v14 */
    /* JADX WARN: Type inference failed for: r3v19 */
    /* JADX WARN: Type inference failed for: r3v21 */
    /* JADX WARN: Type inference failed for: r3v22, types: [java.util.Set] */
    /* JADX WARN: Type inference failed for: r3v24 */
    /* JADX WARN: Type inference failed for: r3v5, types: [java.util.Set] */
    private void computeDatasetsForPccAndUpdateContainer(android.service.autofill.FillResponse fillResponse, com.android.server.autofill.Session.DatasetComputationContainer datasetComputationContainer) throws java.lang.Throwable {
        ?? r25;
        android.util.ArrayMap arrayMap;
        java.util.LinkedHashSet linkedHashSet;
        java.util.LinkedHashMap linkedHashMap;
        int i;
        ?? r252;
        java.util.List list;
        java.util.LinkedHashSet linkedHashSet2;
        java.util.LinkedHashSet linkedHashSet3;
        android.service.autofill.Dataset dataset;
        java.util.Set linkedHashSet4;
        java.util.LinkedHashMap linkedHashMap2;
        java.util.LinkedHashSet linkedHashSet5;
        java.util.LinkedHashSet linkedHashSet6;
        java.util.LinkedHashSet linkedHashSet7;
        java.util.LinkedHashSet linkedHashSet8;
        android.util.ArrayMap arrayMap2;
        java.util.List list2;
        int i2;
        java.util.LinkedHashSet linkedHashSet9;
        int i3;
        java.util.LinkedHashSet linkedHashSet10;
        ?? r3;
        com.android.server.autofill.Session session = this;
        com.android.server.autofill.Session.DatasetComputationContainer datasetComputationContainer2 = datasetComputationContainer;
        java.util.List datasets = fillResponse.getDatasets();
        if (datasets == null) {
            return;
        }
        ?? r12 = session.mLock;
        synchronized (r12) {
            try {
                try {
                    android.util.ArrayMap arrayMap3 = session.mClassificationState.mHintsToAutofillIdMap;
                    android.util.ArrayMap arrayMap4 = session.mClassificationState.mGroupHintsToAutofillIdMap;
                    java.util.LinkedHashMap linkedHashMap3 = new java.util.LinkedHashMap();
                    java.util.LinkedHashSet linkedHashSet11 = new java.util.LinkedHashSet();
                    java.util.LinkedHashSet linkedHashSet12 = new java.util.LinkedHashSet();
                    int i4 = 0;
                    r12 = r12;
                    while (i4 < datasets.size()) {
                        try {
                            android.service.autofill.Dataset dataset2 = (android.service.autofill.Dataset) datasets.get(i4);
                            if (dataset2.getAutofillDatatypes() == null) {
                                arrayMap = arrayMap3;
                                linkedHashSet = linkedHashSet11;
                                linkedHashMap = linkedHashMap3;
                                i = i4;
                                r252 = r12;
                                list = datasets;
                                linkedHashSet2 = linkedHashSet12;
                            } else if (dataset2.getAutofillDatatypes().isEmpty()) {
                                arrayMap = arrayMap3;
                                linkedHashSet = linkedHashSet11;
                                linkedHashMap = linkedHashMap3;
                                i = i4;
                                r252 = r12;
                                list = datasets;
                                linkedHashSet2 = linkedHashSet12;
                            } else {
                                java.util.ArrayList<android.view.autofill.AutofillId> arrayList = new java.util.ArrayList<>();
                                java.util.ArrayList<android.view.autofill.AutofillValue> arrayList2 = new java.util.ArrayList<>();
                                java.util.ArrayList<android.widget.RemoteViews> arrayList3 = new java.util.ArrayList<>();
                                java.util.ArrayList<android.widget.RemoteViews> arrayList4 = new java.util.ArrayList<>();
                                java.util.ArrayList<android.service.autofill.InlinePresentation> arrayList5 = new java.util.ArrayList<>();
                                java.util.ArrayList<android.service.autofill.InlinePresentation> arrayList6 = new java.util.ArrayList<>();
                                java.util.ArrayList<android.service.autofill.Dataset.DatasetFieldFilter> arrayList7 = new java.util.ArrayList<>();
                                i = i4;
                                ?? linkedHashSet13 = new java.util.LinkedHashSet();
                                boolean z = false;
                                java.util.LinkedHashSet linkedHashSet14 = new java.util.LinkedHashSet();
                                java.util.LinkedHashSet linkedHashSet15 = new java.util.LinkedHashSet();
                                java.util.LinkedHashSet linkedHashSet16 = linkedHashSet12;
                                int i5 = 4;
                                int i6 = 0;
                                while (true) {
                                    linkedHashSet3 = linkedHashSet11;
                                    if (i6 >= dataset2.getAutofillDatatypes().size()) {
                                        break;
                                    }
                                    if (dataset2.getAutofillDatatypes().get(i6) == null) {
                                        try {
                                            int i7 = (dataset2.getFieldIds() == null || dataset2.getFieldIds().get(i6) == null) ? i5 : 5;
                                            android.view.autofill.AutofillId autofillId = (android.view.autofill.AutofillId) dataset2.getFieldIds().get(i6);
                                            if (session.mClassificationState.mClassificationCombinedHintsMap.containsKey(autofillId)) {
                                                linkedHashMap2 = linkedHashMap3;
                                                linkedHashSet5 = linkedHashSet3;
                                                linkedHashSet6 = linkedHashSet16;
                                                linkedHashSet7 = linkedHashSet14;
                                                linkedHashSet8 = linkedHashSet15;
                                            } else {
                                                java.util.LinkedHashSet linkedHashSet17 = linkedHashSet15;
                                                linkedHashSet17.add(autofillId);
                                                java.util.LinkedHashSet linkedHashSet18 = linkedHashSet14;
                                                linkedHashSet18.add(autofillId);
                                                linkedHashSet8 = linkedHashSet17;
                                                linkedHashSet7 = linkedHashSet18;
                                                linkedHashSet6 = linkedHashSet16;
                                                linkedHashSet5 = linkedHashSet3;
                                                linkedHashMap2 = linkedHashMap3;
                                                copyFieldsFromDataset(dataset2, i6, autofillId, arrayList, arrayList2, arrayList3, arrayList4, arrayList5, arrayList6, arrayList7);
                                            }
                                            arrayMap2 = arrayMap3;
                                            list2 = datasets;
                                            i2 = i6;
                                            linkedHashSet9 = linkedHashSet6;
                                            i3 = i7;
                                            linkedHashSet10 = linkedHashSet7;
                                            r3 = linkedHashSet13;
                                            linkedHashSet13 = r12;
                                        } catch (java.lang.Throwable th) {
                                            th = th;
                                            r25 = r12;
                                            throw th;
                                        }
                                    } else {
                                        linkedHashMap2 = linkedHashMap3;
                                        linkedHashSet5 = linkedHashSet3;
                                        java.util.LinkedHashSet linkedHashSet19 = linkedHashSet16;
                                        java.util.LinkedHashSet linkedHashSet20 = linkedHashSet14;
                                        linkedHashSet8 = linkedHashSet15;
                                        java.lang.String str = (java.lang.String) dataset2.getAutofillDatatypes().get(i6);
                                        if (arrayMap3.containsKey(str)) {
                                            arrayMap2 = arrayMap3;
                                            java.util.ArrayList<android.view.autofill.AutofillId> arrayList8 = new java.util.ArrayList((java.util.Collection) arrayMap3.get(str));
                                            if (arrayList8.isEmpty()) {
                                                i3 = i5;
                                                list2 = datasets;
                                                i2 = i6;
                                                linkedHashSet9 = linkedHashSet19;
                                                linkedHashSet10 = linkedHashSet20;
                                                r3 = linkedHashSet13;
                                                linkedHashSet13 = r12;
                                            } else {
                                                boolean z2 = true;
                                                ?? r122 = r12;
                                                ?? r253 = linkedHashSet13;
                                                for (android.view.autofill.AutofillId autofillId2 : arrayList8) {
                                                    java.util.ArrayList arrayList9 = arrayList8;
                                                    linkedHashSet19.add(autofillId2);
                                                    java.lang.String str2 = str;
                                                    ?? r32 = r253;
                                                    r32.add(autofillId2);
                                                    ?? r254 = r122;
                                                    java.util.List list3 = datasets;
                                                    boolean z3 = z2;
                                                    int i8 = i6;
                                                    int i9 = i5;
                                                    java.util.LinkedHashSet linkedHashSet21 = linkedHashSet19;
                                                    java.util.LinkedHashSet linkedHashSet22 = linkedHashSet20;
                                                    copyFieldsFromDataset(dataset2, i6, autofillId2, arrayList, arrayList2, arrayList3, arrayList4, arrayList5, arrayList6, arrayList7);
                                                    linkedHashSet20 = linkedHashSet22;
                                                    linkedHashSet19 = linkedHashSet21;
                                                    r122 = r254;
                                                    arrayList8 = arrayList9;
                                                    datasets = list3;
                                                    z2 = z3;
                                                    i6 = i8;
                                                    i5 = i9;
                                                    r253 = r32;
                                                    str = str2;
                                                }
                                                i3 = i5;
                                                boolean z4 = z2;
                                                list2 = datasets;
                                                i2 = i6;
                                                linkedHashSet9 = linkedHashSet19;
                                                linkedHashSet10 = linkedHashSet20;
                                                r3 = r253;
                                                linkedHashSet13 = r122;
                                                z = z4;
                                            }
                                        } else {
                                            arrayMap2 = arrayMap3;
                                            i3 = i5;
                                            list2 = datasets;
                                            i2 = i6;
                                            linkedHashSet9 = linkedHashSet19;
                                            linkedHashSet10 = linkedHashSet20;
                                            r3 = linkedHashSet13;
                                            linkedHashSet13 = r12;
                                        }
                                    }
                                    try {
                                        i6 = i2 + 1;
                                        session = this;
                                        linkedHashSet16 = linkedHashSet9;
                                        r12 = linkedHashSet13;
                                        arrayMap3 = arrayMap2;
                                        linkedHashSet15 = linkedHashSet8;
                                        linkedHashMap3 = linkedHashMap2;
                                        datasets = list2;
                                        linkedHashSet14 = linkedHashSet10;
                                        linkedHashSet13 = r3;
                                        linkedHashSet11 = linkedHashSet5;
                                        i5 = i3;
                                    } catch (java.lang.Throwable th2) {
                                        th = th2;
                                        r25 = linkedHashSet13;
                                        throw th;
                                    }
                                }
                                int i10 = i5;
                                java.util.LinkedHashMap linkedHashMap4 = linkedHashMap3;
                                list = datasets;
                                linkedHashSet2 = linkedHashSet16;
                                ?? r33 = linkedHashSet13;
                                java.util.LinkedHashSet linkedHashSet23 = linkedHashSet14;
                                java.util.LinkedHashSet linkedHashSet24 = linkedHashSet15;
                                arrayMap = arrayMap3;
                                r252 = r12;
                                if (z) {
                                    r33.addAll(linkedHashSet23);
                                    java.util.LinkedHashSet linkedHashSet25 = linkedHashSet24;
                                    linkedHashSet2.addAll(linkedHashSet25);
                                    android.service.autofill.Dataset dataset3 = new android.service.autofill.Dataset(arrayList, arrayList2, arrayList3, arrayList4, arrayList5, arrayList6, arrayList7, new java.util.ArrayList(), dataset2.getFieldContent(), null, null, null, null, dataset2.getId(), dataset2.getAuthentication());
                                    dataset3.setEligibleReasonReason(i10);
                                    linkedHashSet = linkedHashSet3;
                                    linkedHashSet.add(dataset3);
                                    for (android.view.autofill.AutofillId autofillId3 : r33) {
                                        java.util.LinkedHashSet linkedHashSet26 = linkedHashSet25;
                                        java.util.LinkedHashSet linkedHashSet27 = linkedHashSet23;
                                        java.util.LinkedHashMap linkedHashMap5 = linkedHashMap4;
                                        if (linkedHashMap5.containsKey(autofillId3)) {
                                            dataset = dataset2;
                                            linkedHashSet4 = (java.util.Set) linkedHashMap5.get(autofillId3);
                                        } else {
                                            dataset = dataset2;
                                            linkedHashSet4 = new java.util.LinkedHashSet();
                                        }
                                        linkedHashSet4.add(dataset3);
                                        linkedHashMap5.put(autofillId3, linkedHashSet4);
                                        linkedHashMap4 = linkedHashMap5;
                                        dataset2 = dataset;
                                        linkedHashSet23 = linkedHashSet27;
                                        linkedHashSet25 = linkedHashSet26;
                                    }
                                    linkedHashMap = linkedHashMap4;
                                } else {
                                    linkedHashSet = linkedHashSet3;
                                    linkedHashMap = linkedHashMap4;
                                }
                            }
                            i4 = i + 1;
                            session = this;
                            linkedHashSet12 = linkedHashSet2;
                            linkedHashSet11 = linkedHashSet;
                            r12 = r252;
                            datasets = list;
                            datasetComputationContainer2 = datasetComputationContainer;
                            linkedHashMap3 = linkedHashMap;
                            arrayMap3 = arrayMap;
                        } catch (java.lang.Throwable th3) {
                            th = th3;
                            r25 = r12;
                        }
                    }
                    java.util.LinkedHashSet linkedHashSet28 = linkedHashSet11;
                    java.util.LinkedHashMap linkedHashMap6 = linkedHashMap3;
                    ?? r255 = r12;
                    datasetComputationContainer.mAutofillIds = linkedHashSet12;
                    datasetComputationContainer.mDatasets = linkedHashSet28;
                    datasetComputationContainer.mAutofillIdToDatasetMap = linkedHashMap6;
                } catch (java.lang.Throwable th4) {
                    th = th4;
                    r25 = r12;
                }
            } catch (java.lang.Throwable th5) {
                th = th5;
            }
        }
    }

    private void copyFieldsFromDataset(android.service.autofill.Dataset dataset, int index, android.view.autofill.AutofillId autofillId, java.util.ArrayList<android.view.autofill.AutofillId> fieldIds, java.util.ArrayList<android.view.autofill.AutofillValue> fieldValues, java.util.ArrayList<android.widget.RemoteViews> fieldPresentations, java.util.ArrayList<android.widget.RemoteViews> fieldDialogPresentations, java.util.ArrayList<android.service.autofill.InlinePresentation> fieldInlinePresentations, java.util.ArrayList<android.service.autofill.InlinePresentation> fieldInlineTooltipPresentations, java.util.ArrayList<android.service.autofill.Dataset.DatasetFieldFilter> fieldFilters) {
        fieldIds.add(autofillId);
        fieldValues.add((android.view.autofill.AutofillValue) dataset.getFieldValues().get(index));
        fieldPresentations.add(dataset.getFieldPresentation(index));
        fieldDialogPresentations.add(dataset.getFieldDialogPresentation(index));
        fieldInlinePresentations.add(dataset.getFieldInlinePresentation(index));
        fieldInlineTooltipPresentations.add(dataset.getFieldInlineTooltipPresentation(index));
        fieldFilters.add(dataset.getFilter(index));
    }

    @Override // com.android.server.autofill.RemoteFillService.FillServiceCallbacks
    public void onFillRequestFailure(int requestId, java.lang.Throwable t) {
        java.lang.CharSequence message = t.getMessage();
        boolean timedOut = t instanceof java.util.concurrent.TimeoutException;
        boolean showMessage = !android.text.TextUtils.isEmpty(message);
        synchronized (this.mLock) {
            this.mFillResponseEventLogger.startLogForNewResponse();
            this.mFillResponseEventLogger.maybeSetRequestId(requestId);
            this.mFillResponseEventLogger.maybeSetAppPackageUid(this.uid);
            this.mFillResponseEventLogger.maybeSetAvailableCount(-1);
            this.mFillResponseEventLogger.maybeSetTotalDatasetsProvided(-1);
            this.mFillResponseEventLogger.maybeSetDetectionPreference(getDetectionPreferenceForLogging());
            long fillRequestReceivedRelativeTimestamp = android.os.SystemClock.elapsedRealtime() - this.mLatencyBaseTime;
            this.mFillResponseEventLogger.maybeSetLatencyFillResponseReceivedMillis((int) fillRequestReceivedRelativeTimestamp);
            unregisterDelayedFillBroadcastLocked();
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#onFillRequestFailureOrTimeout(req=" + requestId + ") rejected - session: " + this.id + " destroyed");
                this.mFillResponseEventLogger.maybeSetResponseStatus(5);
                this.mFillResponseEventLogger.logAndEndEvent();
                return;
            }
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "finishing session due to service " + (timedOut ? "timeout" : "failure"));
            }
            this.mService.resetLastResponse();
            this.mLastFillDialogTriggerIds = null;
            android.metrics.LogMaker requestLog = this.mRequestLogs.get(requestId);
            if (requestLog == null) {
                android.util.Slog.w(TAG, "onFillRequestFailureOrTimeout(): no log for id " + requestId);
            } else {
                requestLog.setType(timedOut ? 2 : 11);
            }
            if (showMessage) {
                int targetSdk = this.mService.getTargedSdkLocked();
                if (targetSdk >= 29) {
                    showMessage = false;
                    android.util.Slog.w(TAG, "onFillRequestFailureOrTimeout(): not showing '" + ((java.lang.Object) message) + "' because service's targetting API " + targetSdk);
                }
                if (message != null) {
                    requestLog.addTaggedData(1572, java.lang.Integer.valueOf(message.length()));
                }
            }
            if (t instanceof java.util.concurrent.TimeoutException) {
                this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReason(5);
                this.mFillResponseEventLogger.maybeSetResponseStatus(4);
            } else if (t instanceof android.os.TransactionTooLargeException) {
                this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReason(7);
                this.mFillResponseEventLogger.maybeSetResponseStatus(6);
            } else {
                this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReason(7);
                this.mFillResponseEventLogger.maybeSetResponseStatus(1);
            }
            this.mPresentationStatsEventLogger.logAndEndEvent();
            this.mFillResponseEventLogger.maybeSetLatencyResponseProcessingMillis();
            this.mFillResponseEventLogger.logAndEndEvent();
            notifyUnavailableToClient(6, null);
            if (showMessage) {
                getUiForShowing().showError(message, this);
            }
            removeFromService();
        }
    }

    @Override // com.android.server.autofill.RemoteFillService.FillServiceCallbacks
    public void onSaveRequestSuccess(java.lang.String servicePackageName, android.content.IntentSender intentSender) {
        synchronized (this.mLock) {
            this.mSessionFlags.mShowingSaveUi = false;
            this.mSaveEventLogger.maybeSetIsSaved(true);
            this.mSaveEventLogger.maybeSetLatencySaveFinishMillis();
            this.mSaveEventLogger.logAndEndEvent();
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#onSaveRequestSuccess() rejected - session: " + this.id + " destroyed");
                return;
            }
            android.metrics.LogMaker log = newLogMaker(918, servicePackageName).setType(intentSender == null ? 10 : 1);
            this.mMetricsLogger.write(log);
            if (intentSender != null) {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.d(TAG, "Starting intent sender on save()");
                }
                startIntentSenderAndFinishSession(intentSender);
            }
            removeFromService();
        }
    }

    @Override // com.android.server.autofill.RemoteFillService.FillServiceCallbacks
    public void onSaveRequestFailure(java.lang.CharSequence message, java.lang.String servicePackageName) {
        int targetSdk;
        boolean showMessage = !android.text.TextUtils.isEmpty(message);
        synchronized (this.mLock) {
            this.mSessionFlags.mShowingSaveUi = false;
            this.mSaveEventLogger.maybeSetLatencySaveFinishMillis();
            this.mSaveEventLogger.logAndEndEvent();
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#onSaveRequestFailure() rejected - session: " + this.id + " destroyed");
                return;
            }
            if (showMessage && (targetSdk = this.mService.getTargedSdkLocked()) >= 29) {
                showMessage = false;
                android.util.Slog.w(TAG, "onSaveRequestFailure(): not showing '" + ((java.lang.Object) message) + "' because service's targetting API " + targetSdk);
            }
            android.metrics.LogMaker log = newLogMaker(918, servicePackageName).setType(11);
            if (message != null) {
                log.addTaggedData(1572, java.lang.Integer.valueOf(message.length()));
            }
            this.mMetricsLogger.write(log);
            if (showMessage) {
                getUiForShowing().showError(message, this);
            }
            removeFromService();
        }
    }

    @Override // com.android.server.autofill.RemoteFillService.FillServiceCallbacks
    public void onConvertCredentialRequestSuccess(android.service.autofill.ConvertCredentialResponse convertCredentialResponse) {
        android.service.autofill.Dataset dataset = convertCredentialResponse.getDataset();
        android.os.Bundle clientState = convertCredentialResponse.getClientState();
        if (dataset == null) {
            android.util.Slog.e(TAG, "onConvertCredentialRequestSuccess(): dataset inside response is null");
            return;
        }
        int requestId = -1;
        if (clientState == null) {
            android.util.Slog.e(TAG, "onConvertCredentialRequestSuccess(): client state is null, this would cause loss in logging.");
        } else {
            requestId = clientState.getInt("android.view.autofill.extra.AUTOFILL_REQUEST_ID");
        }
        fill(requestId, -1, dataset, 4);
    }

    private android.service.autofill.FillContext getFillContextByRequestIdLocked(int requestId) {
        if (this.mContexts == null) {
            return null;
        }
        int numContexts = this.mContexts.size();
        for (int i = 0; i < numContexts; i++) {
            android.service.autofill.FillContext context = this.mContexts.get(i);
            if (context.getRequestId() == requestId) {
                return context;
            }
        }
        return null;
    }

    public void onServiceDied(com.android.server.autofill.RemoteFillService service) {
        android.util.Slog.w(TAG, "removing session because service died");
        synchronized (this.mLock) {
            forceRemoveFromServiceLocked();
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void authenticate(int requestId, int datasetIndex, android.content.IntentSender intent, android.os.Bundle extras, int uiType) {
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "authenticate(): requestId=" + requestId + "; datasetIdx=" + datasetIndex + "; intentSender=" + intent);
        }
        synchronized (this.mLock) {
            this.mPresentationStatsEventLogger.maybeSetAuthenticationType(2);
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#authenticate() rejected - session: " + this.id + " destroyed");
                return;
            }
            android.content.Intent fillInIntent = createAuthFillInIntentLocked(requestId, extras);
            if (fillInIntent == null) {
                forceRemoveFromServiceLocked();
                return;
            }
            this.mService.setAuthenticationSelected(this.id, this.mClientState, uiType);
            int authenticationId = android.view.autofill.AutofillManager.makeAuthenticationId(requestId, datasetIndex);
            this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.QuintConsumer() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda10
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5) {
                    ((com.android.server.autofill.Session) obj).startAuthentication(((java.lang.Integer) obj2).intValue(), (android.content.IntentSender) obj3, (android.content.Intent) obj4, ((java.lang.Boolean) obj5).booleanValue());
                }
            }, this, java.lang.Integer.valueOf(authenticationId), intent, fillInIntent, java.lang.Boolean.valueOf(uiType == 2)));
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void fill(int requestId, int datasetIndex, android.service.autofill.Dataset dataset, int uiType) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#fill() rejected - session: " + this.id + " destroyed");
            } else {
                this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.HexConsumer() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda6
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6) {
                        ((com.android.server.autofill.Session) obj).autoFill(((java.lang.Integer) obj2).intValue(), ((java.lang.Integer) obj3).intValue(), (android.service.autofill.Dataset) obj4, ((java.lang.Boolean) obj5).booleanValue(), ((java.lang.Integer) obj6).intValue());
                    }
                }, this, java.lang.Integer.valueOf(requestId), java.lang.Integer.valueOf(datasetIndex), dataset, true, java.lang.Integer.valueOf(uiType)));
            }
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void save() {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#save() rejected - session: " + this.id + " destroyed");
            } else {
                this.mSaveEventLogger.maybeSetLatencySaveRequestMillis();
                this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.BiConsumer() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda2
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        ((com.android.server.autofill.AutofillManagerServiceImpl) obj).handleSessionSave((com.android.server.autofill.Session) obj2);
                    }
                }, this.mService, this));
            }
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void cancelSave() {
        synchronized (this.mLock) {
            this.mSessionFlags.mShowingSaveUi = false;
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#cancelSave() rejected - session: " + this.id + " destroyed");
            } else {
                this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda7
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.autofill.Session) obj).removeFromService();
                    }
                }, this));
            }
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void onShown(int uiType, int numDatasetsShown) {
        synchronized (this.mLock) {
            this.mPresentationStatsEventLogger.maybeSetDisplayPresentationType(uiType);
            if (uiType == 2) {
                this.mPresentationStatsEventLogger.maybeIncrementCountShown();
                if (!this.mLoggedInlineDatasetShown) {
                    this.mService.logDatasetShown(this.id, this.mClientState, uiType);
                    android.util.Slog.d(TAG, "onShown(): " + uiType + ", " + numDatasetsShown);
                }
                this.mLoggedInlineDatasetShown = true;
            } else {
                this.mPresentationStatsEventLogger.maybeSetCountShown(numDatasetsShown);
                this.mPresentationStatsEventLogger.maybeSetSuggestionPresentedTimestampMs();
                this.mService.logDatasetShown(this.id, this.mClientState, uiType);
                android.util.Slog.d(TAG, "onShown(): " + uiType + ", " + numDatasetsShown);
            }
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void requestShowFillUi(android.view.autofill.AutofillId id, int width, int height, android.view.autofill.IAutofillWindowPresenter presenter) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#requestShowFillUi() rejected - session: " + id + " destroyed");
                return;
            }
            if (id.equals(this.mCurrentViewId)) {
                try {
                    com.android.server.autofill.ViewState view = this.mViewStates.get(id);
                    this.mClient.requestShowFillUi(this.id, id, width, height, view.getVirtualBounds(), presenter);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Error requesting to show fill UI", e);
                }
            } else if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "Do not show full UI on " + id + " as it is not the current view (" + this.mCurrentViewId + ") anymore");
            }
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void dispatchUnhandledKey(android.view.autofill.AutofillId id, android.view.KeyEvent keyEvent) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#dispatchUnhandledKey() rejected - session: " + id + " destroyed");
                return;
            }
            if (id.equals(this.mCurrentViewId)) {
                try {
                    this.mClient.dispatchUnhandledKey(this.id, id, keyEvent);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Error requesting to dispatch unhandled key", e);
                }
            } else {
                android.util.Slog.w(TAG, "Do not dispatch unhandled key on " + id + " as it is not the current view (" + this.mCurrentViewId + ") anymore");
            }
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void requestHideFillUi(android.view.autofill.AutofillId id) {
        synchronized (this.mLock) {
            try {
                this.mClient.requestHideFillUi(this.id, id);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Error requesting to hide fill UI", e);
            }
            this.mInlineSessionController.hideInlineSuggestionsUiLocked(id);
            this.mPresentationStatsEventLogger.markShownCountAsResettable();
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void requestHideFillUiWhenDestroyed(android.view.autofill.AutofillId id) {
        synchronized (this.mLock) {
            try {
                this.mClient.requestHideFillUiWhenDestroyed(this.id, id);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Error requesting to hide fill UI", e);
            }
            this.mInlineSessionController.hideInlineSuggestionsUiLocked(id);
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void cancelSession() {
        synchronized (this.mLock) {
            removeFromServiceLocked();
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void startIntentSenderAndFinishSession(android.content.IntentSender intentSender) {
        startIntentSender(intentSender, null);
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void startIntentSender(android.content.IntentSender intentSender, android.content.Intent intent) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#startIntentSender() rejected - session: " + this.id + " destroyed");
                return;
            }
            if (intent == null) {
                removeFromServiceLocked();
            }
            this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda5
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                    ((com.android.server.autofill.Session) obj).doStartIntentSender((android.content.IntentSender) obj2, (android.content.Intent) obj3);
                }
            }, this, intentSender, intent));
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void requestShowSoftInput(android.view.autofill.AutofillId id) {
        android.view.autofill.IAutoFillManagerClient client = getClient();
        if (client != null) {
            try {
                client.requestShowSoftInput(id);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Error sending input show up notification", e);
            }
        }
    }

    @Override // com.android.server.autofill.ui.AutoFillUI.AutoFillUiCallback
    public void requestFallbackFromFillDialog() {
        setFillDialogDisabled();
        synchronized (this.mLock) {
            if (this.mCurrentViewId == null) {
                return;
            }
            com.android.server.autofill.ViewState currentView = this.mViewStates.get(this.mCurrentViewId);
            currentView.maybeCallOnFillReady(this.mFlags);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyFillUiHidden(android.view.autofill.AutofillId autofillId) {
        synchronized (this.mLock) {
            try {
                this.mClient.notifyFillUiHidden(this.id, autofillId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Error sending fill UI hidden notification", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyFillUiShown(android.view.autofill.AutofillId autofillId) {
        synchronized (this.mLock) {
            try {
                this.mClient.notifyFillUiShown(this.id, autofillId);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Error sending fill UI shown notification", e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doStartIntentSender(android.content.IntentSender intentSender, android.content.Intent intent) {
        try {
            synchronized (this.mLock) {
                this.mClient.startIntentSender(intentSender, intent);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Error launching auth intent", e);
        }
    }

    void setAuthenticationResultLocked(android.os.Bundle data, int authenticationId) throws java.lang.Throwable {
        android.service.autofill.FillResponse fillResponse;
        android.service.autofill.Dataset dataset;
        android.service.autofill.Dataset datasetFromCredentialResponse;
        if (this.mDestroyed) {
            android.util.Slog.w(TAG, "Call to Session#setAuthenticationResultLocked() rejected - session: " + this.id + " destroyed");
            return;
        }
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "setAuthenticationResultLocked(): id= " + authenticationId + ", data=" + data);
        }
        int requestId = android.view.autofill.AutofillManager.getRequestIdFromAuthenticationId(authenticationId);
        if (requestId == 1) {
            setAuthenticationResultForAugmentedAutofillLocked(data, authenticationId);
            this.mPresentationStatsEventLogger.logAndEndEvent();
            return;
        }
        if (this.mResponses == null) {
            android.util.Slog.w(TAG, "setAuthenticationResultLocked(" + authenticationId + "): no responses");
            this.mPresentationStatsEventLogger.maybeSetAuthenticationResult(2);
            this.mPresentationStatsEventLogger.logAndEndEvent();
            removeFromService();
            return;
        }
        if (com.android.server.autofill.RequestId.isSecondaryProvider(requestId)) {
            fillResponse = this.mSecondaryResponses.get(requestId);
        } else {
            fillResponse = this.mResponses.get(requestId);
        }
        android.service.autofill.FillResponse authenticatedResponse = fillResponse;
        if (authenticatedResponse == null || data == null) {
            android.util.Slog.w(TAG, "no authenticated response");
            this.mPresentationStatsEventLogger.maybeSetAuthenticationResult(2);
            this.mPresentationStatsEventLogger.logAndEndEvent();
            removeFromService();
            return;
        }
        int datasetIdx = android.view.autofill.AutofillManager.getDatasetIdFromAuthenticationId(authenticationId);
        if (datasetIdx == 65535) {
            dataset = null;
        } else {
            android.service.autofill.Dataset dataset2 = (android.service.autofill.Dataset) authenticatedResponse.getDatasets().get(datasetIdx);
            if (dataset2 != null) {
                dataset = dataset2;
            } else {
                android.util.Slog.w(TAG, "no dataset with index " + datasetIdx + " on fill response");
                this.mPresentationStatsEventLogger.maybeSetAuthenticationResult(2);
                this.mPresentationStatsEventLogger.logAndEndEvent();
                removeFromService();
                return;
            }
        }
        this.mSessionFlags.mExpiredResponse = false;
        android.os.Parcelable result = data.getParcelable("android.view.autofill.extra.AUTHENTICATION_RESULT");
        android.credentials.GetCredentialException exception = (android.credentials.GetCredentialException) data.getSerializable("android.service.credentials.extra.GET_CREDENTIAL_EXCEPTION", android.credentials.GetCredentialException.class);
        android.os.Bundle newClientState = data.getBundle("android.view.autofill.extra.CLIENT_STATE");
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "setAuthenticationResultLocked(): result=" + result + ", clientState=" + newClientState + ", authenticationId=" + authenticationId);
        }
        if (android.service.autofill.Flags.autofillCredmanDevIntegration() && exception != null && !exception.getType().equals("android.credentials.GetCredentialException.TYPE_USER_CANCELED")) {
            if (dataset != null && dataset.getFieldIds().size() == 1) {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.d(TAG, "setAuthenticationResultLocked(): result returns withCredential Manager Exception");
                }
                sendCredentialManagerResponseToApp(null, exception, (android.view.autofill.AutofillId) dataset.getFieldIds().get(0));
                return;
            }
            return;
        }
        if (result instanceof android.service.autofill.FillResponse) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "setAuthenticationResultLocked(): received FillResponse from authentication flow");
            }
            logAuthenticationStatusLocked(requestId, com.android.internal.util.FrameworkStatsLog.MEDIA_CODEC_RENDERED__RESOLUTION__RESOLUTION_720P_HD_ALMOST);
            this.mPresentationStatsEventLogger.maybeSetAuthenticationResult(1);
            replaceResponseLocked(authenticatedResponse, (android.service.autofill.FillResponse) result, newClientState);
            return;
        }
        if (result instanceof android.credentials.GetCredentialResponse) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "Received GetCredentialResponse from authentication flow");
            }
            if (android.service.autofill.Flags.autofillCredmanDevIntegration()) {
                android.credentials.GetCredentialResponse response = (android.credentials.GetCredentialResponse) result;
                if (dataset != null && dataset.getFieldIds().size() == 1) {
                    android.view.autofill.AutofillId autofillId = (android.view.autofill.AutofillId) dataset.getFieldIds().get(0);
                    if (com.android.server.autofill.Helper.sDebug) {
                        android.util.Slog.d(TAG, "Received GetCredentialResponse from authentication flow,for autofillId: " + autofillId);
                    }
                    sendCredentialManagerResponseToApp(response, null, autofillId);
                    return;
                }
                return;
            }
            if (android.service.autofill.Flags.autofillCredmanIntegration() && (datasetFromCredentialResponse = getDatasetFromCredentialResponse((android.credentials.GetCredentialResponse) result)) != null) {
                autoFill(requestId, datasetIdx, datasetFromCredentialResponse, false, 0);
                return;
            }
            return;
        }
        if (result instanceof android.service.autofill.Dataset) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "setAuthenticationResultLocked(): received Dataset from authentication flow");
            }
            if (datasetIdx != 65535) {
                logAuthenticationStatusLocked(requestId, 1126);
                this.mPresentationStatsEventLogger.maybeSetAuthenticationResult(1);
                if (newClientState != null) {
                    if (com.android.server.autofill.Helper.sDebug) {
                        android.util.Slog.d(TAG, "Updating client state from auth dataset");
                    }
                    this.mClientState = newClientState;
                }
                android.service.autofill.Dataset datasetFromResult = getEffectiveDatasetForAuthentication((android.service.autofill.Dataset) result);
                android.service.autofill.Dataset oldDataset = (android.service.autofill.Dataset) authenticatedResponse.getDatasets().get(datasetIdx);
                if (!isAuthResultDatasetEphemeral(oldDataset, data)) {
                    authenticatedResponse.getDatasets().set(datasetIdx, datasetFromResult);
                }
                autoFill(requestId, datasetIdx, datasetFromResult, false, 0);
                return;
            }
            android.util.Slog.w(TAG, "invalid index (" + datasetIdx + ") for authentication id " + authenticationId);
            logAuthenticationStatusLocked(requestId, 1127);
            this.mPresentationStatsEventLogger.maybeSetAuthenticationResult(2);
            return;
        }
        if (result != null) {
            android.util.Slog.w(TAG, "service returned invalid auth type: " + result);
        }
        logAuthenticationStatusLocked(requestId, 1128);
        this.mPresentationStatsEventLogger.maybeSetAuthenticationResult(2);
        processNullResponseLocked(requestId, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.service.autofill.Dataset getDatasetFromCredentialResponse(android.credentials.GetCredentialResponse result) {
        android.os.Bundle bundle;
        if (result == null || (bundle = result.getCredential().getData()) == null) {
            return null;
        }
        return (android.service.autofill.Dataset) bundle.getParcelable("android.view.autofill.extra.AUTHENTICATION_RESULT", android.service.autofill.Dataset.class);
    }

    android.service.autofill.Dataset getEffectiveDatasetForAuthentication(android.service.autofill.Dataset authenticatedDataset) throws java.lang.Throwable {
        android.service.autofill.FillResponse response = getEffectiveFillResponse(new android.service.autofill.FillResponse.Builder().addDataset(authenticatedDataset).build());
        if (response == null || response.getDatasets().size() == 0) {
            android.util.Log.wtf(TAG, "No datasets in fill response on authentication. response = " + (response == null ? "null" : response.toString()));
            return authenticatedDataset;
        }
        java.util.List<android.service.autofill.Dataset> datasets = response.getDatasets();
        android.service.autofill.Dataset result = (android.service.autofill.Dataset) response.getDatasets().get(0);
        if (datasets.size() > 1) {
            android.service.autofill.Dataset.Builder builder = new android.service.autofill.Dataset.Builder();
            for (android.service.autofill.Dataset dataset : datasets) {
                if (!dataset.getFieldIds().isEmpty()) {
                    for (int i = 0; i < dataset.getFieldIds().size(); i++) {
                        builder.setField((android.view.autofill.AutofillId) dataset.getFieldIds().get(i), new android.service.autofill.Field.Builder().setValue((android.view.autofill.AutofillValue) dataset.getFieldValues().get(i)).build());
                    }
                }
            }
            android.service.autofill.Dataset result2 = builder.setId(authenticatedDataset.getId()).build();
            return result2;
        }
        return result;
    }

    private static boolean isAuthResultDatasetEphemeral(android.service.autofill.Dataset oldDataset, android.os.Bundle authResultData) {
        if (authResultData.containsKey("android.view.autofill.extra.AUTHENTICATION_RESULT_EPHEMERAL_DATASET")) {
            return authResultData.getBoolean("android.view.autofill.extra.AUTHENTICATION_RESULT_EPHEMERAL_DATASET");
        }
        return isPinnedDataset(oldDataset);
    }

    private static boolean isPinnedDataset(android.service.autofill.Dataset dataset) {
        if (dataset != null && dataset.getFieldIds() != null) {
            int numOfFields = dataset.getFieldIds().size();
            for (int i = 0; i < numOfFields; i++) {
                android.service.autofill.InlinePresentation inlinePresentation = dataset.getFieldInlinePresentation(i);
                if (inlinePresentation != null && inlinePresentation.isPinned()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    void setAuthenticationResultForAugmentedAutofillLocked(android.os.Bundle data, int authId) {
        android.service.autofill.Dataset dataset = data == null ? null : (android.service.autofill.Dataset) data.getParcelable("android.view.autofill.extra.AUTHENTICATION_RESULT", android.service.autofill.Dataset.class);
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "Auth result for augmented autofill: sessionId=" + this.id + ", authId=" + authId + ", dataset=" + dataset);
        }
        android.view.autofill.AutofillId fieldId = (dataset == null || dataset.getFieldIds().size() != 1) ? null : (android.view.autofill.AutofillId) dataset.getFieldIds().get(0);
        android.view.autofill.AutofillValue value = (dataset == null || dataset.getFieldValues().size() != 1) ? null : (android.view.autofill.AutofillValue) dataset.getFieldValues().get(0);
        android.content.ClipData content = dataset != null ? dataset.getFieldContent() : null;
        if (fieldId == null || (value == null && content == null)) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "Rejecting empty/invalid auth result");
            }
            this.mService.resetLastAugmentedAutofillResponse();
            removeFromServiceLocked();
            return;
        }
        com.android.server.autofill.RemoteAugmentedAutofillService remoteAugmentedAutofillService = this.mService.getRemoteAugmentedAutofillServiceIfCreatedLocked();
        if (remoteAugmentedAutofillService == null) {
            android.util.Slog.e(TAG, "Can't fill after auth: RemoteAugmentedAutofillService is null");
            this.mService.resetLastAugmentedAutofillResponse();
            removeFromServiceLocked();
            return;
        }
        fieldId.setSessionId(this.id);
        this.mCurrentViewId = fieldId;
        android.os.Bundle clientState = data.getBundle("android.view.autofill.extra.CLIENT_STATE");
        this.mService.logAugmentedAutofillSelected(this.id, dataset.getId(), clientState);
        if (content != null) {
            com.android.server.autofill.AutofillUriGrantsManager autofillUgm = remoteAugmentedAutofillService.getAutofillUriGrantsManager();
            autofillUgm.grantUriPermissions(this.mComponentName, this.mActivityToken, this.userId, content);
        }
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "Filling after auth: fieldId=" + fieldId + ", value=" + value + ", content=" + content);
        }
        try {
            if (content != null) {
                this.mClient.autofillContent(this.id, fieldId, content);
            } else {
                this.mClient.autofill(this.id, dataset.getFieldIds(), dataset.getFieldValues(), true);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Error filling after auth: fieldId=" + fieldId + ", value=" + value + ", content=" + content, e);
        }
        this.mInlineSessionController.setInlineFillUiLocked(com.android.server.autofill.ui.InlineFillUi.emptyUi(fieldId));
    }

    void setHasCallbackLocked(boolean hasIt) {
        if (this.mDestroyed) {
            android.util.Slog.w(TAG, "Call to Session#setHasCallbackLocked() rejected - session: " + this.id + " destroyed");
        } else {
            this.mHasCallback = hasIt;
        }
    }

    private android.service.autofill.FillResponse getLastResponseLocked(java.lang.String logPrefixFmt) {
        java.lang.String logPrefix;
        if (com.android.server.autofill.Helper.sDebug && logPrefixFmt != null) {
            logPrefix = java.lang.String.format(logPrefixFmt, java.lang.Integer.valueOf(this.id));
        } else {
            logPrefix = null;
        }
        if (this.mContexts == null) {
            if (logPrefix != null) {
                android.util.Slog.d(TAG, logPrefix + ": no contexts");
            }
            return null;
        }
        if (this.mResponses == null) {
            if (com.android.server.autofill.Helper.sVerbose && logPrefix != null) {
                android.util.Slog.v(TAG, logPrefix + ": no responses on session");
            }
            return null;
        }
        int lastResponseIdx = getLastResponseIndexLocked();
        if (lastResponseIdx < 0) {
            if (logPrefix != null) {
                android.util.Slog.w(TAG, logPrefix + ": did not get last response. mResponses=" + this.mResponses + ", mViewStates=" + this.mViewStates);
            }
            return null;
        }
        android.service.autofill.FillResponse response = this.mResponses.valueAt(lastResponseIdx);
        if (com.android.server.autofill.Helper.sVerbose && logPrefix != null) {
            android.util.Slog.v(TAG, logPrefix + ": mResponses=" + this.mResponses + ", mContexts=" + this.mContexts + ", mViewStates=" + this.mViewStates);
        }
        return response;
    }

    private android.service.autofill.SaveInfo getSaveInfoLocked() {
        android.service.autofill.FillResponse response = getLastResponseLocked(null);
        if (response == null) {
            return null;
        }
        return response.getSaveInfo();
    }

    int getSaveInfoFlagsLocked() {
        android.service.autofill.SaveInfo saveInfo = getSaveInfoLocked();
        if (saveInfo == null) {
            return 0;
        }
        return saveInfo.getFlags();
    }

    static class SaveInfoStats {
        public int saveDataTypeCount;
        public int saveInfoCount;

        SaveInfoStats() {
        }
    }

    private com.android.server.autofill.Session.SaveInfoStats getSaveInfoStatsLocked() {
        com.android.server.autofill.Session.SaveInfoStats retSaveInfoStats = new com.android.server.autofill.Session.SaveInfoStats();
        retSaveInfoStats.saveInfoCount = -1;
        retSaveInfoStats.saveDataTypeCount = -1;
        if (this.mContexts == null) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "getSaveInfoStatsLocked(): mContexts is null");
            }
        } else {
            if (this.mResponses == null) {
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "getSaveInfoStatsLocked(): mResponses is null");
                }
                return retSaveInfoStats;
            }
            int numSaveInfos = 0;
            int numSaveDataTypes = 0;
            android.util.ArraySet<java.lang.Integer> saveDataTypeSeen = new android.util.ArraySet<>();
            int numResponses = this.mResponses.size();
            for (int responseNum = 0; responseNum < numResponses; responseNum++) {
                android.service.autofill.FillResponse response = this.mResponses.valueAt(responseNum);
                if (response != null && response.getSaveInfo() != null) {
                    numSaveInfos++;
                    int saveDataType = response.getSaveInfo().getType();
                    if (!saveDataTypeSeen.contains(java.lang.Integer.valueOf(saveDataType))) {
                        saveDataTypeSeen.add(java.lang.Integer.valueOf(saveDataType));
                        numSaveDataTypes++;
                    }
                }
            }
            retSaveInfoStats.saveInfoCount = numSaveInfos;
            retSaveInfoStats.saveDataTypeCount = numSaveDataTypes;
        }
        return retSaveInfoStats;
    }

    public void logContextCommitted() {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "logContextCommitted (" + this.id + "): commit_reason:0 no_save_reason:0");
        }
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.autofill.Session$$ExternalSyntheticLambda4(), this, 0, 0));
        synchronized (this.mLock) {
            logAllEventsLocked(0);
        }
    }

    public void logContextCommittedLocked(int saveDialogNotShowReason, int commitReason) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "logContextCommittedLocked (" + this.id + "): commit_reason:" + commitReason + " no_save_reason:" + saveDialogNotShowReason);
        }
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.autofill.Session$$ExternalSyntheticLambda4(), this, java.lang.Integer.valueOf(saveDialogNotShowReason), java.lang.Integer.valueOf(commitReason)));
        this.mSessionCommittedEventLogger.maybeSetCommitReason(commitReason);
        this.mSessionCommittedEventLogger.maybeSetRequestCount(this.mRequestCount);
        com.android.server.autofill.Session.SaveInfoStats saveInfoStats = getSaveInfoStatsLocked();
        this.mSessionCommittedEventLogger.maybeSetSaveInfoCount(saveInfoStats.saveInfoCount);
        this.mSessionCommittedEventLogger.maybeSetSaveDataTypeCount(saveInfoStats.saveDataTypeCount);
        this.mSessionCommittedEventLogger.maybeSetLastFillResponseHasSaveInfo(getSaveInfoLocked() != null);
        this.mSaveEventLogger.maybeSetSaveUiNotShownReason(1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleLogContextCommitted(int saveDialogNotShowReason, int commitReason) throws java.lang.Throwable {
        android.service.autofill.FillResponse lastResponse;
        android.service.autofill.FieldClassificationUserData userData;
        synchronized (this.mLock) {
            lastResponse = getLastResponseLocked("logContextCommited(%s)");
        }
        if (lastResponse == null) {
            android.util.Slog.w(TAG, "handleLogContextCommitted(): last response is null");
            return;
        }
        android.service.autofill.UserData genericUserData = this.mService.getUserData();
        android.service.autofill.FieldClassificationUserData userData2 = lastResponse.getUserData();
        if (userData2 == null && genericUserData == null) {
            userData = null;
        } else if (userData2 != null && genericUserData != null) {
            userData = new android.service.autofill.CompositeUserData(genericUserData, userData2);
        } else if (userData2 != null) {
            userData = userData2;
        } else {
            userData = this.mService.getUserData();
        }
        com.android.server.autofill.FieldClassificationStrategy fcStrategy = this.mService.getFieldClassificationStrategy();
        if (userData != null && fcStrategy != null) {
            logFieldClassificationScore(fcStrategy, userData, saveDialogNotShowReason, commitReason);
        } else {
            logContextCommitted(null, null, saveDialogNotShowReason, commitReason);
        }
    }

    private void logContextCommitted(java.util.ArrayList<android.view.autofill.AutofillId> detectedFieldIds, java.util.ArrayList<android.service.autofill.FieldClassification> detectedFieldClassifications, int saveDialogNotShowReason, int commitReason) {
        synchronized (this.mLock) {
            logContextCommittedLocked(detectedFieldIds, detectedFieldClassifications, saveDialogNotShowReason, commitReason);
        }
    }

    private void logContextCommittedLocked(java.util.ArrayList<android.view.autofill.AutofillId> detectedFieldIds, java.util.ArrayList<android.service.autofill.FieldClassification> detectedFieldClassifications, int saveDialogNotShowReason, int commitReason) {
        java.lang.String str;
        java.util.ArrayList<android.view.autofill.AutofillId> manuallyFilledFieldIds;
        java.util.ArrayList<java.util.ArrayList<java.lang.String>> manuallyFilledDatasetIds;
        android.view.autofill.AutofillValue currentValue;
        java.lang.String str2;
        int responseCount;
        android.util.ArrayMap<android.view.autofill.AutofillId, android.util.ArraySet<java.lang.String>> manuallyFilledIds;
        int responseCount2;
        java.lang.String str3;
        android.view.autofill.AutofillValue currentValue2;
        android.util.ArraySet<java.lang.String> ignoredDatasets;
        android.view.autofill.AutofillValue currentValue3;
        java.util.ArrayList<android.view.autofill.AutofillValue> values;
        android.util.ArrayMap<android.view.autofill.AutofillId, android.util.ArraySet<java.lang.String>> manuallyFilledIds2;
        java.util.ArrayList<android.view.autofill.AutofillId> changedFieldIds;
        java.util.ArrayList<java.lang.String> changedDatasetIds;
        java.util.ArrayList<android.view.autofill.AutofillId> changedFieldIds2;
        java.util.ArrayList<java.lang.String> changedDatasetIds2;
        android.util.ArrayMap<android.view.autofill.AutofillId, android.util.ArraySet<java.lang.String>> manuallyFilledIds3;
        android.util.ArrayMap<android.view.autofill.AutofillId, android.util.ArraySet<java.lang.String>> manuallyFilledIds4;
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "logContextCommittedLocked (" + this.id + "): commit_reason:" + commitReason + " no_save_reason:" + saveDialogNotShowReason);
        }
        android.service.autofill.FillResponse lastResponse = getLastResponseLocked("logContextCommited(%s)");
        if (lastResponse == null) {
            return;
        }
        this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReason(com.android.server.autofill.PresentationStatsEventLogger.getNoPresentationEventReason(commitReason));
        this.mPresentationStatsEventLogger.logAndEndEvent();
        int flags = lastResponse.getFlags();
        if ((flags & 1) == 0) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "logContextCommittedLocked(): ignored by flags " + flags);
                return;
            }
            return;
        }
        android.util.ArraySet<java.lang.String> ignoredDatasets2 = null;
        java.util.ArrayList<android.view.autofill.AutofillId> changedFieldIds3 = null;
        java.util.ArrayList<java.lang.String> changedDatasetIds3 = null;
        android.util.ArrayMap<android.view.autofill.AutofillId, android.util.ArraySet<java.lang.String>> manuallyFilledIds5 = null;
        int responseCount3 = this.mResponses.size();
        int i = 0;
        boolean hasAtLeastOneDataset = false;
        while (true) {
            str = "logContextCommitted() skipping idless dataset ";
            if (i >= responseCount3) {
                break;
            }
            android.service.autofill.FillResponse response = this.mResponses.valueAt(i);
            java.util.List<android.service.autofill.Dataset> datasets = response.getDatasets();
            if (datasets == null || datasets.isEmpty()) {
                changedFieldIds2 = changedFieldIds3;
                changedDatasetIds2 = changedDatasetIds3;
                manuallyFilledIds3 = manuallyFilledIds5;
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "logContextCommitted() no datasets at " + i);
                }
                i++;
                changedFieldIds3 = changedFieldIds2;
                changedDatasetIds3 = changedDatasetIds2;
                manuallyFilledIds5 = manuallyFilledIds3;
            } else {
                int j = 0;
                while (true) {
                    changedFieldIds2 = changedFieldIds3;
                    if (j >= datasets.size()) {
                        break;
                    }
                    android.service.autofill.Dataset dataset = datasets.get(j);
                    java.util.ArrayList<java.lang.String> changedDatasetIds4 = changedDatasetIds3;
                    java.lang.String datasetId = dataset.getId();
                    if (datasetId == null) {
                        if (!com.android.server.autofill.Helper.sVerbose) {
                            manuallyFilledIds4 = manuallyFilledIds5;
                        } else {
                            manuallyFilledIds4 = manuallyFilledIds5;
                            android.util.Slog.v(TAG, "logContextCommitted() skipping idless dataset " + dataset);
                        }
                    } else {
                        manuallyFilledIds4 = manuallyFilledIds5;
                        if (this.mSelectedDatasetIds == null || !this.mSelectedDatasetIds.contains(datasetId)) {
                            if (com.android.server.autofill.Helper.sVerbose) {
                                android.util.Slog.v(TAG, "adding ignored dataset " + datasetId);
                            }
                            if (ignoredDatasets2 == null) {
                                ignoredDatasets2 = new android.util.ArraySet<>();
                            }
                            ignoredDatasets2.add(datasetId);
                            hasAtLeastOneDataset = true;
                        } else {
                            hasAtLeastOneDataset = true;
                        }
                    }
                    j++;
                    changedFieldIds3 = changedFieldIds2;
                    changedDatasetIds3 = changedDatasetIds4;
                    manuallyFilledIds5 = manuallyFilledIds4;
                }
                changedDatasetIds2 = changedDatasetIds3;
                manuallyFilledIds3 = manuallyFilledIds5;
                i++;
                changedFieldIds3 = changedFieldIds2;
                changedDatasetIds3 = changedDatasetIds2;
                manuallyFilledIds5 = manuallyFilledIds3;
            }
        }
        java.util.ArrayList<android.view.autofill.AutofillId> changedFieldIds4 = changedFieldIds3;
        int i2 = 0;
        android.util.ArrayMap<android.view.autofill.AutofillId, android.util.ArraySet<java.lang.String>> manuallyFilledIds6 = manuallyFilledIds5;
        java.util.ArrayList<java.lang.String> changedDatasetIds5 = changedDatasetIds3;
        java.util.ArrayList<android.view.autofill.AutofillId> changedFieldIds5 = changedFieldIds4;
        android.util.ArraySet<java.lang.String> ignoredDatasets3 = ignoredDatasets2;
        while (i2 < this.mViewStates.size()) {
            com.android.server.autofill.ViewState viewState = this.mViewStates.valueAt(i2);
            int state = viewState.getState();
            if ((state & 8) != 0) {
                if ((state & 2048) != 0) {
                    java.lang.String datasetId2 = viewState.getDatasetId();
                    if (datasetId2 == null) {
                        android.util.Slog.w(TAG, "logContextCommitted(): no dataset id on " + viewState);
                    } else {
                        android.view.autofill.AutofillValue autofilledValue = viewState.getAutofilledValue();
                        android.view.autofill.AutofillValue currentValue4 = viewState.getCurrentValue();
                        if (autofilledValue != null && autofilledValue.equals(currentValue4)) {
                            if (com.android.server.autofill.Helper.sDebug) {
                                android.util.Slog.d(TAG, "logContextCommitted(): ignoring changed " + viewState + " because it has same value that was autofilled");
                            }
                        } else {
                            if (com.android.server.autofill.Helper.sDebug) {
                                android.util.Slog.d(TAG, "logContextCommitted() found changed state: " + viewState);
                            }
                            if (changedFieldIds5 != null) {
                                changedFieldIds = changedFieldIds5;
                                changedDatasetIds = changedDatasetIds5;
                            } else {
                                java.util.ArrayList<android.view.autofill.AutofillId> changedFieldIds6 = new java.util.ArrayList<>();
                                java.util.ArrayList<java.lang.String> changedDatasetIds6 = new java.util.ArrayList<>();
                                changedFieldIds = changedFieldIds6;
                                changedDatasetIds = changedDatasetIds6;
                            }
                            changedFieldIds.add(viewState.id);
                            changedDatasetIds.add(datasetId2);
                            changedFieldIds5 = changedFieldIds;
                            changedDatasetIds5 = changedDatasetIds;
                        }
                    }
                } else {
                    android.view.autofill.AutofillValue currentValue5 = viewState.getCurrentValue();
                    if (currentValue5 == null) {
                        if (com.android.server.autofill.Helper.sDebug) {
                            android.util.Slog.d(TAG, "logContextCommitted(): skipping view without current value ( " + viewState + ")");
                        }
                    } else if (hasAtLeastOneDataset) {
                        int j2 = 0;
                        while (j2 < responseCount3) {
                            android.service.autofill.FillResponse response2 = this.mResponses.valueAt(j2);
                            java.util.List<android.service.autofill.Dataset> datasets2 = response2.getDatasets();
                            if (datasets2 == null || datasets2.isEmpty()) {
                                currentValue = currentValue5;
                                str2 = str;
                                responseCount = responseCount3;
                                if (com.android.server.autofill.Helper.sVerbose) {
                                    android.util.Slog.v(TAG, "logContextCommitted() no datasets at " + j2);
                                }
                                j2++;
                                responseCount3 = responseCount;
                                str = str2;
                                currentValue5 = currentValue;
                            } else {
                                int k = 0;
                                while (true) {
                                    manuallyFilledIds = manuallyFilledIds6;
                                    if (k >= datasets2.size()) {
                                        break;
                                    }
                                    android.service.autofill.Dataset dataset2 = datasets2.get(k);
                                    java.util.List<android.service.autofill.Dataset> datasets3 = datasets2;
                                    java.lang.String datasetId3 = dataset2.getId();
                                    if (datasetId3 == null) {
                                        if (!com.android.server.autofill.Helper.sVerbose) {
                                            responseCount2 = responseCount3;
                                        } else {
                                            responseCount2 = responseCount3;
                                            android.util.Slog.v(TAG, str + dataset2);
                                        }
                                        currentValue2 = currentValue5;
                                        str3 = str;
                                    } else {
                                        responseCount2 = responseCount3;
                                        java.util.ArrayList<android.view.autofill.AutofillValue> values2 = dataset2.getFieldValues();
                                        str3 = str;
                                        int l = 0;
                                        while (true) {
                                            android.service.autofill.Dataset dataset3 = dataset2;
                                            if (l >= values2.size()) {
                                                break;
                                            }
                                            android.view.autofill.AutofillValue candidate = values2.get(l);
                                            if (!currentValue5.equals(candidate)) {
                                                currentValue3 = currentValue5;
                                                values = values2;
                                            } else {
                                                if (!com.android.server.autofill.Helper.sDebug) {
                                                    currentValue3 = currentValue5;
                                                } else {
                                                    currentValue3 = currentValue5;
                                                    android.util.Slog.d(TAG, "field " + viewState.id + " was manually filled with value set by dataset " + datasetId3);
                                                }
                                                if (manuallyFilledIds != null) {
                                                    manuallyFilledIds2 = manuallyFilledIds;
                                                } else {
                                                    manuallyFilledIds2 = new android.util.ArrayMap<>();
                                                }
                                                android.util.ArraySet<java.lang.String> datasetIds = manuallyFilledIds2.get(viewState.id);
                                                if (datasetIds == null) {
                                                    values = values2;
                                                    datasetIds = new android.util.ArraySet<>(1);
                                                    manuallyFilledIds2.put(viewState.id, datasetIds);
                                                } else {
                                                    values = values2;
                                                }
                                                datasetIds.add(datasetId3);
                                                manuallyFilledIds = manuallyFilledIds2;
                                            }
                                            l++;
                                            dataset2 = dataset3;
                                            currentValue5 = currentValue3;
                                            values2 = values;
                                        }
                                        currentValue2 = currentValue5;
                                        if (this.mSelectedDatasetIds == null || !this.mSelectedDatasetIds.contains(datasetId3)) {
                                            if (com.android.server.autofill.Helper.sVerbose) {
                                                android.util.Slog.v(TAG, "adding ignored dataset " + datasetId3);
                                            }
                                            if (ignoredDatasets3 != null) {
                                                ignoredDatasets = ignoredDatasets3;
                                            } else {
                                                ignoredDatasets = new android.util.ArraySet<>();
                                            }
                                            ignoredDatasets.add(datasetId3);
                                            ignoredDatasets3 = ignoredDatasets;
                                        }
                                    }
                                    manuallyFilledIds6 = manuallyFilledIds;
                                    k++;
                                    datasets2 = datasets3;
                                    responseCount3 = responseCount2;
                                    str = str3;
                                    currentValue5 = currentValue2;
                                }
                                currentValue = currentValue5;
                                str2 = str;
                                responseCount = responseCount3;
                                manuallyFilledIds6 = manuallyFilledIds;
                                j2++;
                                responseCount3 = responseCount;
                                str = str2;
                                currentValue5 = currentValue;
                            }
                        }
                    }
                }
            }
            i2++;
            responseCount3 = responseCount3;
            str = str;
        }
        if (manuallyFilledIds6 == null) {
            manuallyFilledFieldIds = null;
            manuallyFilledDatasetIds = null;
        } else {
            int size = manuallyFilledIds6.size();
            java.util.ArrayList<android.view.autofill.AutofillId> manuallyFilledFieldIds2 = new java.util.ArrayList<>(size);
            java.util.ArrayList<java.util.ArrayList<java.lang.String>> manuallyFilledDatasetIds2 = new java.util.ArrayList<>(size);
            for (int i3 = 0; i3 < size; i3++) {
                android.view.autofill.AutofillId fieldId = manuallyFilledIds6.keyAt(i3);
                android.util.ArraySet<java.lang.String> datasetIds2 = manuallyFilledIds6.valueAt(i3);
                manuallyFilledFieldIds2.add(fieldId);
                manuallyFilledDatasetIds2.add(new java.util.ArrayList<>(datasetIds2));
            }
            manuallyFilledFieldIds = manuallyFilledFieldIds2;
            manuallyFilledDatasetIds = manuallyFilledDatasetIds2;
        }
        this.mService.logContextCommittedLocked(this.id, this.mClientState, this.mSelectedDatasetIds, ignoredDatasets3, changedFieldIds5, changedDatasetIds5, manuallyFilledFieldIds, manuallyFilledDatasetIds, detectedFieldIds, detectedFieldClassifications, this.mComponentName, this.mCompatMode, saveDialogNotShowReason);
        this.mSessionCommittedEventLogger.maybeSetCommitReason(commitReason);
        this.mSessionCommittedEventLogger.maybeSetRequestCount(this.mRequestCount);
        this.mSaveEventLogger.maybeSetSaveUiNotShownReason(saveDialogNotShowReason);
    }

    private void logFieldClassificationScore(com.android.server.autofill.FieldClassificationStrategy fcStrategy, android.service.autofill.FieldClassificationUserData userData, int saveDialogNotShowReason, int commitReason) throws java.lang.Throwable {
        java.util.Collection<com.android.server.autofill.ViewState> viewStates;
        java.lang.String[] userValues = userData.getValues();
        java.lang.String[] categoryIds = userData.getCategoryIds();
        java.lang.String defaultAlgorithm = userData.getFieldClassificationAlgorithm();
        android.os.Bundle defaultArgs = userData.getDefaultFieldClassificationArgs();
        android.util.ArrayMap<java.lang.String, java.lang.String> algorithms = userData.getFieldClassificationAlgorithms();
        android.util.ArrayMap<java.lang.String, android.os.Bundle> args = userData.getFieldClassificationArgs();
        if (userValues == null || categoryIds == null || userValues.length != categoryIds.length) {
            java.lang.String[] categoryIds2 = categoryIds;
            java.lang.String[] userValues2 = userValues;
            int valuesLength = userValues2 == null ? -1 : userValues2.length;
            int idsLength = categoryIds2 != null ? categoryIds2.length : -1;
            android.util.Slog.w(TAG, "setScores(): user data mismatch: values.length = " + valuesLength + ", ids.length = " + idsLength);
            return;
        }
        int maxFieldsSize = android.service.autofill.UserData.getMaxFieldClassificationIdsSize();
        java.util.ArrayList<android.view.autofill.AutofillId> detectedFieldIds = new java.util.ArrayList<>(maxFieldsSize);
        java.util.ArrayList<android.service.autofill.FieldClassification> detectedFieldClassifications = new java.util.ArrayList<>(maxFieldsSize);
        synchronized (this.mLock) {
            try {
                viewStates = this.mViewStates.values();
            } catch (java.lang.Throwable th) {
                th = th;
                while (true) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        int viewsSize = viewStates.size();
        android.view.autofill.AutofillId[] autofillIds = new android.view.autofill.AutofillId[viewsSize];
        java.util.ArrayList<android.view.autofill.AutofillValue> currentValues = new java.util.ArrayList<>(viewsSize);
        int k = 0;
        for (com.android.server.autofill.ViewState viewState : viewStates) {
            currentValues.add(viewState.getCurrentValue());
            autofillIds[k] = viewState.id;
            k++;
        }
        android.os.RemoteCallback callback = new android.os.RemoteCallback(new com.android.server.autofill.LogFieldClassificationScoreOnResultListener(this, saveDialogNotShowReason, commitReason, viewsSize, autofillIds, userValues, categoryIds, detectedFieldIds, detectedFieldClassifications));
        fcStrategy.calculateScores(callback, currentValues, userValues, categoryIds, defaultAlgorithm, defaultArgs, algorithms, args);
    }

    /* JADX WARN: Incorrect condition in loop: B:46:0x0136 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void handleLogFieldClassificationScore(android.os.Bundle r18, int r19, int r20, int r21, android.view.autofill.AutofillId[] r22, java.lang.String[] r23, java.lang.String[] r24, java.util.ArrayList<android.view.autofill.AutofillId> r25, java.util.ArrayList<android.service.autofill.FieldClassification> r26) {
        /*
            Method dump skipped, instruction units count: 385
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.autofill.Session.handleLogFieldClassificationScore(android.os.Bundle, int, int, int, android.view.autofill.AutofillId[], java.lang.String[], java.lang.String[], java.util.ArrayList, java.util.ArrayList):void");
    }

    public void logSaveUiShown() {
        this.mHandler.sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.autofill.Session) obj).logSaveShown();
            }
        }, this));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v4 */
    /* JADX WARN: Type inference failed for: r1v5, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v12 */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public com.android.server.autofill.Session.SaveResult showSaveLocked() {
        boolean allRequiredAreNotEmpty;
        boolean atLeastOneChanged;
        boolean allRequiredAreNotEmpty2;
        android.graphics.drawable.Drawable serviceIcon;
        java.lang.CharSequence serviceLabel;
        boolean z;
        com.android.server.autofill.Session session;
        java.util.List<android.service.autofill.Dataset> datasets;
        android.service.autofill.InternalValidator validator;
        android.view.autofill.AutofillId[] requiredIds;
        android.util.ArrayMap<android.view.autofill.AutofillId, android.view.autofill.AutofillValue> datasetValues;
        int i;
        int saveDialogNotShowReason;
        boolean atLeastOneChanged2;
        android.view.autofill.AutofillValue candidateSaveValue;
        boolean allRequiredAreNotEmpty3;
        boolean allRequiredAreNotEmpty4;
        boolean atLeastOneChanged3;
        if (!this.mDestroyed) {
            this.mSessionState = 2;
            android.service.autofill.FillResponse response = getLastResponseLocked("showSaveLocked(%s)");
            android.service.autofill.SaveInfo saveInfo = response == null ? null : response.getSaveInfo();
            if (this.mSessionExt.skipSaveUiAndNativeProcess()) {
                save();
                return new com.android.server.autofill.Session.SaveResult(true, false, 0);
            }
            if (this.mSessionFlags.mScreenHasCredmanField) {
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "Call to Session#showSaveLocked() rejected - there is credman field in screen");
                }
                this.mSaveEventLogger.maybeSetSaveUiNotShownReason(10);
                this.mSaveEventLogger.logAndEndEvent();
                return new com.android.server.autofill.Session.SaveResult(false, true, 0);
            }
            if (saveInfo == null) {
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "showSaveLocked(" + this.id + "): no saveInfo from service");
                }
                this.mSaveEventLogger.maybeSetSaveUiNotShownReason(2);
                this.mSaveEventLogger.logAndEndEvent();
                return new com.android.server.autofill.Session.SaveResult(false, true, 1);
            }
            if ((saveInfo.getFlags() & 4) != 0) {
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.v(TAG, "showSaveLocked(" + this.id + "): service asked to delay save");
                }
                this.mSaveEventLogger.maybeSetSaveUiNotShownReason(3);
                this.mSaveEventLogger.logAndEndEvent();
                return new com.android.server.autofill.Session.SaveResult(false, false, 2);
            }
            android.util.ArrayMap<android.view.autofill.AutofillId, android.service.autofill.InternalSanitizer> sanitizers = com.android.server.autofill.Helper.createSanitizers(saveInfo);
            android.util.ArrayMap<android.view.autofill.AutofillId, android.view.autofill.AutofillValue> currentValues = new android.util.ArrayMap<>();
            android.util.ArraySet<android.view.autofill.AutofillId> savableIds = new android.util.ArraySet<>();
            android.view.autofill.AutofillId[] requiredIds2 = saveInfo.getRequiredIds();
            boolean allRequiredAreNotEmpty5 = true;
            boolean atLeastOneChanged4 = false;
            boolean allRequiredAreNotEmpty6 = false;
            if (requiredIds2 != null) {
                int i2 = 0;
                while (true) {
                    if (i2 >= requiredIds2.length) {
                        allRequiredAreNotEmpty5 = allRequiredAreNotEmpty3;
                        break;
                    }
                    android.view.autofill.AutofillId id = requiredIds2[i2];
                    if (id == null) {
                        android.util.Slog.w(TAG, "null autofill id on " + java.util.Arrays.toString(requiredIds2));
                        allRequiredAreNotEmpty4 = allRequiredAreNotEmpty3;
                        atLeastOneChanged3 = atLeastOneChanged4;
                    } else {
                        savableIds.add(id);
                        com.android.server.autofill.ViewState viewState = this.mViewStates.get(id);
                        if (viewState == null) {
                            android.util.Slog.w(TAG, "showSaveLocked(): no ViewState for required " + id);
                            allRequiredAreNotEmpty5 = false;
                            break;
                        }
                        android.view.autofill.AutofillValue value = viewState.getCurrentValue();
                        if (value == null || value.isEmpty()) {
                            android.view.autofill.AutofillValue candidateSaveValue2 = viewState.getCandidateSaveValue();
                            if (candidateSaveValue2 != null && !candidateSaveValue2.isEmpty()) {
                                if (com.android.server.autofill.Helper.sVerbose) {
                                    android.util.Slog.v(TAG, "current value is empty, using cached last non-empty value instead");
                                }
                                value = candidateSaveValue2;
                                allRequiredAreNotEmpty4 = allRequiredAreNotEmpty3;
                                atLeastOneChanged3 = atLeastOneChanged4;
                            } else {
                                android.view.autofill.AutofillValue initialValue = getValueFromContextsLocked(id);
                                if (initialValue != null) {
                                    if (!com.android.server.autofill.Helper.sDebug) {
                                        allRequiredAreNotEmpty4 = allRequiredAreNotEmpty3;
                                        atLeastOneChanged3 = atLeastOneChanged4;
                                    } else {
                                        allRequiredAreNotEmpty4 = allRequiredAreNotEmpty3;
                                        atLeastOneChanged3 = atLeastOneChanged4;
                                        android.util.Slog.d(TAG, "Value of required field " + id + " didn't change; using initial value (" + initialValue + ") instead");
                                    }
                                    value = initialValue;
                                } else {
                                    boolean atLeastOneChanged5 = atLeastOneChanged4;
                                    if (com.android.server.autofill.Helper.sDebug) {
                                        android.util.Slog.d(TAG, "empty value for required " + id);
                                    }
                                    allRequiredAreNotEmpty5 = false;
                                    atLeastOneChanged4 = atLeastOneChanged5;
                                }
                            }
                        } else {
                            allRequiredAreNotEmpty4 = allRequiredAreNotEmpty3;
                            atLeastOneChanged3 = atLeastOneChanged4;
                        }
                        android.view.autofill.AutofillValue value2 = getSanitizedValue(sanitizers, id, value);
                        if (value2 == null) {
                            if (com.android.server.autofill.Helper.sDebug) {
                                android.util.Slog.d(TAG, "value of required field " + id + " failed sanitization");
                            }
                            allRequiredAreNotEmpty5 = false;
                            atLeastOneChanged4 = atLeastOneChanged3;
                        } else {
                            viewState.setSanitizedValue(value2);
                            currentValues.put(id, value2);
                            android.view.autofill.AutofillValue filledValue = viewState.getAutofilledValue();
                            if (!value2.equals(filledValue)) {
                                boolean changed = true;
                                if (filledValue == null) {
                                    android.view.autofill.AutofillValue initialValue2 = getValueFromContextsLocked(id);
                                    if (initialValue2 != null && initialValue2.equals(value2)) {
                                        if (com.android.server.autofill.Helper.sDebug) {
                                            android.util.Slog.d(TAG, "id " + id + " is part of dataset but initial value didn't change: " + value2);
                                        }
                                        changed = false;
                                    } else {
                                        this.mSaveEventLogger.maybeSetIsNewField(true);
                                    }
                                } else {
                                    allRequiredAreNotEmpty6 = true;
                                }
                                if (!changed) {
                                    atLeastOneChanged4 = atLeastOneChanged3;
                                } else {
                                    if (com.android.server.autofill.Helper.sDebug) {
                                        android.util.Slog.d(TAG, "found a change on required " + id + ": " + filledValue + " => " + value2);
                                    }
                                    atLeastOneChanged4 = true;
                                }
                                i2++;
                                allRequiredAreNotEmpty3 = allRequiredAreNotEmpty4;
                            }
                        }
                    }
                    atLeastOneChanged4 = atLeastOneChanged3;
                    i2++;
                    allRequiredAreNotEmpty3 = allRequiredAreNotEmpty4;
                }
            }
            android.view.autofill.AutofillId[] optionalIds = saveInfo.getOptionalIds();
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "allRequiredAreNotEmpty: " + allRequiredAreNotEmpty5 + " hasOptional: " + (optionalIds != null));
            }
            if (!allRequiredAreNotEmpty5) {
                saveDialogNotShowReason = 3;
                this.mSaveEventLogger.maybeSetSaveUiNotShownReason(4);
                this.mSaveEventLogger.logAndEndEvent();
            } else {
                if (optionalIds != null && (!atLeastOneChanged4 || !allRequiredAreNotEmpty5)) {
                    for (android.view.autofill.AutofillId id2 : optionalIds) {
                        savableIds.add(id2);
                        com.android.server.autofill.ViewState viewState2 = this.mViewStates.get(id2);
                        if (viewState2 == null) {
                            android.util.Slog.w(TAG, "no ViewState for optional " + id2);
                            atLeastOneChanged2 = atLeastOneChanged4;
                        } else if ((viewState2.getState() & 8) != 0) {
                            android.view.autofill.AutofillValue currentValue = viewState2.getCurrentValue();
                            if ((currentValue != null && !currentValue.isEmpty()) || (candidateSaveValue = viewState2.getCandidateSaveValue()) == null || candidateSaveValue.isEmpty()) {
                                atLeastOneChanged2 = atLeastOneChanged4;
                            } else {
                                if (!com.android.server.autofill.Helper.sVerbose) {
                                    atLeastOneChanged2 = atLeastOneChanged4;
                                } else {
                                    atLeastOneChanged2 = atLeastOneChanged4;
                                    android.util.Slog.v(TAG, "current value is empty, using cached last non-empty value instead");
                                }
                                currentValue = candidateSaveValue;
                            }
                            android.view.autofill.AutofillValue value3 = getSanitizedValue(sanitizers, id2, currentValue);
                            if (value3 == null) {
                                if (com.android.server.autofill.Helper.sDebug) {
                                    android.util.Slog.d(TAG, "value of opt. field " + id2 + " failed sanitization");
                                }
                            } else {
                                currentValues.put(id2, value3);
                                android.view.autofill.AutofillValue filledValue2 = viewState2.getAutofilledValue();
                                if (value3 != null && !value3.equals(filledValue2)) {
                                    if (com.android.server.autofill.Helper.sDebug) {
                                        android.util.Slog.d(TAG, "found a change on optional " + id2 + ": " + filledValue2 + " => " + value3);
                                    }
                                    if (filledValue2 != null) {
                                        allRequiredAreNotEmpty6 = true;
                                    } else {
                                        this.mSaveEventLogger.maybeSetIsNewField(true);
                                    }
                                    atLeastOneChanged4 = true;
                                } else {
                                    atLeastOneChanged4 = atLeastOneChanged2;
                                }
                            }
                        } else {
                            atLeastOneChanged2 = atLeastOneChanged4;
                            android.view.autofill.AutofillValue initialValue3 = getValueFromContextsLocked(id2);
                            if (com.android.server.autofill.Helper.sDebug) {
                                android.util.Slog.d(TAG, "no current value for " + id2 + "; initial value is " + initialValue3);
                            }
                            if (initialValue3 != null) {
                                currentValues.put(id2, initialValue3);
                            }
                        }
                        atLeastOneChanged4 = atLeastOneChanged2;
                    }
                    atLeastOneChanged = atLeastOneChanged4;
                    allRequiredAreNotEmpty2 = allRequiredAreNotEmpty6;
                } else {
                    atLeastOneChanged = atLeastOneChanged4;
                }
                if (!atLeastOneChanged) {
                    saveDialogNotShowReason = 4;
                    this.mSaveEventLogger.maybeSetSaveUiNotShownReason(5);
                    this.mSaveEventLogger.logAndEndEvent();
                    atLeastOneChanged4 = atLeastOneChanged;
                } else {
                    if (com.android.server.autofill.Helper.sDebug) {
                        android.util.Slog.d(TAG, "at least one field changed, validate fields for save UI");
                    }
                    android.service.autofill.InternalValidator validator2 = saveInfo.getValidator();
                    if (validator2 != null) {
                        android.metrics.LogMaker log = newLogMaker(1133);
                        try {
                            boolean isValid = validator2.isValid(this);
                            if (com.android.server.autofill.Helper.sDebug) {
                                android.util.Slog.d(TAG, validator2 + " returned " + isValid);
                            }
                            if (isValid) {
                                i = 10;
                            } else {
                                i = 5;
                            }
                            log.setType(i);
                            this.mMetricsLogger.write(log);
                            if (!isValid) {
                                android.util.Slog.i(TAG, "not showing save UI because fields failed validation");
                                this.mSaveEventLogger.maybeSetSaveUiNotShownReason(6);
                                this.mSaveEventLogger.logAndEndEvent();
                                return new com.android.server.autofill.Session.SaveResult(false, true, 5);
                            }
                        } catch (java.lang.Exception e) {
                            android.util.Slog.e(TAG, "Not showing save UI because validation failed:", e);
                            log.setType(11);
                            this.mMetricsLogger.write(log);
                            this.mSaveEventLogger.maybeSetSaveUiNotShownReason(6);
                            this.mSaveEventLogger.logAndEndEvent();
                            return new com.android.server.autofill.Session.SaveResult(false, true, 5);
                        }
                    }
                    java.util.List<android.service.autofill.Dataset> datasets2 = response.getDatasets();
                    if (datasets2 != null) {
                        int i3 = 0;
                        while (i3 < datasets2.size()) {
                            android.service.autofill.Dataset dataset = datasets2.get(i3);
                            android.util.ArrayMap<android.view.autofill.AutofillId, android.view.autofill.AutofillValue> datasetValues2 = com.android.server.autofill.Helper.getFields(dataset);
                            if (com.android.server.autofill.Helper.sVerbose) {
                                android.util.Slog.v(TAG, "Checking if saved fields match contents of dataset #" + i3 + ": " + dataset + "; savableIds=" + savableIds);
                            }
                            int j = 0;
                            while (j < savableIds.size()) {
                                android.view.autofill.AutofillId id3 = savableIds.valueAt(j);
                                android.view.autofill.AutofillValue currentValue2 = currentValues.get(id3);
                                if (currentValue2 == null) {
                                    if (!com.android.server.autofill.Helper.sDebug) {
                                        datasets = datasets2;
                                        validator = validator2;
                                        requiredIds = requiredIds2;
                                        datasetValues = datasetValues2;
                                    } else {
                                        datasets = datasets2;
                                        validator = validator2;
                                        requiredIds = requiredIds2;
                                        android.util.Slog.d(TAG, "dataset has value for field that is null: " + id3);
                                        datasetValues = datasetValues2;
                                    }
                                } else {
                                    datasets = datasets2;
                                    validator = validator2;
                                    requiredIds = requiredIds2;
                                    android.view.autofill.AutofillValue datasetValue = datasetValues2.get(id3);
                                    if (!currentValue2.equals(datasetValue)) {
                                        if (com.android.server.autofill.Helper.sDebug) {
                                            android.util.Slog.d(TAG, "found a dataset change on id " + id3 + ": from " + datasetValue + " to " + currentValue2);
                                        }
                                        i3++;
                                        datasets2 = datasets;
                                        validator2 = validator;
                                        requiredIds2 = requiredIds;
                                    } else {
                                        datasetValues = datasetValues2;
                                        if (com.android.server.autofill.Helper.sVerbose) {
                                            android.util.Slog.v(TAG, "no dataset changes for id " + id3);
                                        }
                                    }
                                }
                                j++;
                                datasets2 = datasets;
                                validator2 = validator;
                                requiredIds2 = requiredIds;
                                datasetValues2 = datasetValues;
                            }
                            if (com.android.server.autofill.Helper.sDebug) {
                                android.util.Slog.d(TAG, "ignoring Save UI because all fields match contents of dataset #" + i3 + ": " + dataset);
                            }
                            this.mSaveEventLogger.maybeSetSaveUiNotShownReason(7);
                            this.mSaveEventLogger.logAndEndEvent();
                            return new com.android.server.autofill.Session.SaveResult(false, true, 6);
                        }
                    }
                    android.view.autofill.IAutoFillManagerClient client = getClient();
                    this.mPendingSaveUi = new com.android.server.autofill.ui.PendingUi(new android.os.Binder(), this.id, client);
                    synchronized (this.mLock) {
                        try {
                            serviceIcon = getServiceIcon(response);
                            serviceLabel = getServiceLabel(response);
                        } finally {
                            th = th;
                            while (true) {
                                try {
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                }
                            }
                        }
                    }
                    if (serviceLabel == null || serviceIcon == null) {
                        com.android.server.autofill.Session session2 = this;
                        ?? r1 = 1;
                        int i4 = 0;
                        session2.wtf(null, "showSaveLocked(): no service label or icon", new java.lang.Object[i4]);
                        session2.mSaveEventLogger.maybeSetSaveUiNotShownReason(r1);
                        session2.mSaveEventLogger.logAndEndEvent();
                        return new com.android.server.autofill.Session.SaveResult(i4, r1, i4);
                    }
                    if (this.mSessionExt.skipSaveUi()) {
                        save();
                        return new com.android.server.autofill.Session.SaveResult(true, false, 0);
                    }
                    getUiForShowing().showSaveUi(serviceLabel, serviceIcon, this.mService.getServicePackageName(), saveInfo, this, this.mComponentName, this, this.mContext, this.mPendingSaveUi, allRequiredAreNotEmpty2, this.mCompatMode, response.getShowSaveDialogIcon(), this.mSaveEventLogger);
                    if (client == null) {
                        z = true;
                        session = this;
                    } else {
                        session = this;
                        try {
                            z = true;
                            try {
                                client.setSaveUiState(session.id, true);
                            } catch (android.os.RemoteException e2) {
                                e = e2;
                                android.util.Slog.e(TAG, "Error notifying client to set save UI state to shown: " + e);
                            }
                        } catch (android.os.RemoteException e3) {
                            e = e3;
                            z = true;
                        }
                    }
                    session.mSessionFlags.mShowingSaveUi = z;
                    if (com.android.server.autofill.Helper.sDebug) {
                        android.util.Slog.d(TAG, "Good news, everyone! All checks passed, show save UI for " + session.id + "!");
                    }
                    return new com.android.server.autofill.Session.SaveResult(z, false, 0);
                }
            }
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "showSaveLocked(" + this.id + "): with no changes, comes no responsibilities.allRequiredAreNotNull=" + allRequiredAreNotEmpty5 + ", atLeastOneChanged=" + atLeastOneChanged4);
            }
            return new com.android.server.autofill.Session.SaveResult(false, true, saveDialogNotShowReason);
        }
        android.util.Slog.w(TAG, "Call to Session#showSaveLocked() rejected - session: " + this.id + " destroyed");
        this.mSaveEventLogger.maybeSetSaveUiNotShownReason(9);
        this.mSaveEventLogger.logAndEndEvent();
        return new com.android.server.autofill.Session.SaveResult(false, false, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void logSaveShown() {
        this.mService.logSaveShown(this.id, this.mClientState);
    }

    private android.view.autofill.AutofillValue getSanitizedValue(android.util.ArrayMap<android.view.autofill.AutofillId, android.service.autofill.InternalSanitizer> sanitizers, android.view.autofill.AutofillId id, android.view.autofill.AutofillValue value) {
        if (sanitizers == null || value == null) {
            return value;
        }
        com.android.server.autofill.ViewState state = this.mViewStates.get(id);
        android.view.autofill.AutofillValue sanitized = state == null ? null : state.getSanitizedValue();
        if (sanitized == null) {
            android.service.autofill.InternalSanitizer sanitizer = sanitizers.get(id);
            if (sanitizer == null) {
                return value;
            }
            sanitized = sanitizer.sanitize(value);
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "Value for " + id + "(" + value + ") sanitized to " + sanitized);
            }
            if (state != null) {
                state.setSanitizedValue(sanitized);
            }
        }
        return sanitized;
    }

    boolean isSaveUiShowingLocked() {
        return this.mSessionFlags.mShowingSaveUi;
    }

    private android.app.assist.AssistStructure.ViewNode getViewNodeFromContextsLocked(android.view.autofill.AutofillId autofillId) {
        int numContexts = this.mContexts.size();
        for (int i = numContexts - 1; i >= 0; i--) {
            android.service.autofill.FillContext context = this.mContexts.get(i);
            android.app.assist.AssistStructure.ViewNode node = com.android.server.autofill.Helper.findViewNodeByAutofillId(context.getStructure(), autofillId);
            if (node != null) {
                return node;
            }
        }
        return null;
    }

    private android.view.autofill.AutofillValue getValueFromContextsLocked(android.view.autofill.AutofillId autofillId) {
        int numContexts = this.mContexts.size();
        for (int i = numContexts - 1; i >= 0; i--) {
            android.service.autofill.FillContext context = this.mContexts.get(i);
            android.app.assist.AssistStructure.ViewNode node = com.android.server.autofill.Helper.findViewNodeByAutofillId(context.getStructure(), autofillId);
            if (node != null) {
                android.view.autofill.AutofillValue value = node.getAutofillValue();
                if (com.android.server.autofill.Helper.sDebug) {
                    android.util.Slog.d(TAG, "getValueFromContexts(" + this.id + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + autofillId + ") at " + i + ": " + value);
                }
                if (value != null && !value.isEmpty()) {
                    return value;
                }
            }
        }
        return null;
    }

    private java.lang.CharSequence[] getAutofillOptionsFromContextsLocked(android.view.autofill.AutofillId autofillId) {
        int numContexts = this.mContexts.size();
        for (int i = numContexts - 1; i >= 0; i--) {
            android.service.autofill.FillContext context = this.mContexts.get(i);
            android.app.assist.AssistStructure.ViewNode node = com.android.server.autofill.Helper.findViewNodeByAutofillId(context.getStructure(), autofillId);
            if (node != null && node.getAutofillOptions() != null) {
                return node.getAutofillOptions();
            }
        }
        return null;
    }

    private void updateValuesForSaveLocked() {
        android.util.ArrayMap<android.view.autofill.AutofillId, android.service.autofill.InternalSanitizer> sanitizers = com.android.server.autofill.Helper.createSanitizers(getSaveInfoLocked());
        int numContexts = this.mContexts.size();
        for (int contextNum = 0; contextNum < numContexts; contextNum++) {
            android.service.autofill.FillContext context = this.mContexts.get(contextNum);
            android.app.assist.AssistStructure.ViewNode[] nodes = context.findViewNodesByAutofillIds(getIdsOfAllViewStatesLocked());
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "updateValuesForSaveLocked(): updating " + context);
            }
            for (int viewStateNum = 0; viewStateNum < this.mViewStates.size(); viewStateNum++) {
                com.android.server.autofill.ViewState viewState = this.mViewStates.valueAt(viewStateNum);
                android.view.autofill.AutofillId id = viewState.id;
                android.view.autofill.AutofillValue value = viewState.getCurrentValue();
                if (value == null) {
                    if (com.android.server.autofill.Helper.sVerbose) {
                        android.util.Slog.v(TAG, "updateValuesForSaveLocked(): skipping " + id);
                    }
                } else {
                    android.app.assist.AssistStructure.ViewNode node = nodes[viewStateNum];
                    if (node == null) {
                        android.util.Slog.w(TAG, "callSaveLocked(): did not find node with id " + id);
                    } else {
                        if (com.android.server.autofill.Helper.sVerbose) {
                            android.util.Slog.v(TAG, "updateValuesForSaveLocked(): updating " + id + " to " + value);
                        }
                        android.view.autofill.AutofillValue sanitizedValue = viewState.getSanitizedValue();
                        if (sanitizedValue == null) {
                            sanitizedValue = getSanitizedValue(sanitizers, id, value);
                        }
                        if (sanitizedValue != null) {
                            node.updateAutofillValue(sanitizedValue);
                        } else if (com.android.server.autofill.Helper.sDebug) {
                            android.util.Slog.d(TAG, "updateValuesForSaveLocked(): not updating field " + id + " because it failed sanitization");
                        }
                    }
                }
            }
            context.getStructure().sanitizeForParceling(false);
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "updateValuesForSaveLocked(): dumping structure of " + context + " before calling service.save()");
                context.getStructure().dump(false);
            }
        }
    }

    void callSaveLocked() {
        if (this.mDestroyed) {
            android.util.Slog.w(TAG, "Call to Session#callSaveLocked() rejected - session: " + this.id + " destroyed");
            this.mSaveEventLogger.maybeSetIsSaved(false);
            this.mSaveEventLogger.logAndEndEvent();
            return;
        }
        if (this.mRemoteFillService == null) {
            wtf(null, "callSaveLocked() called without a remote service. mForAugmentedAutofillOnly: %s", java.lang.Boolean.valueOf(this.mSessionFlags.mAugmentedAutofillOnly));
            this.mSaveEventLogger.maybeSetIsSaved(false);
            this.mSaveEventLogger.logAndEndEvent();
            return;
        }
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "callSaveLocked(" + this.id + "): mViewStates=" + this.mViewStates);
        }
        if (this.mContexts == null) {
            android.util.Slog.w(TAG, "callSaveLocked(): no contexts");
            this.mSaveEventLogger.maybeSetIsSaved(false);
            this.mSaveEventLogger.logAndEndEvent();
            return;
        }
        updateValuesForSaveLocked();
        cancelCurrentRequestLocked();
        java.util.ArrayList<android.service.autofill.FillContext> contexts = mergePreviousSessionLocked(true);
        this.mClientState = this.mSessionExt.hookOnSaveRequestClientState(this.mClientState);
        android.service.assist.classification.FieldClassificationResponse fieldClassificationResponse = this.mClassificationState.mLastFieldClassificationResponse;
        if (this.mService.isPccClassificationEnabled() && fieldClassificationResponse != null && !fieldClassificationResponse.getClassifications().isEmpty()) {
            if (this.mClientState == null) {
                this.mClientState = new android.os.Bundle();
            }
            this.mClientState.putParcelableArrayList(EXTRA_KEY_DETECTIONS, new java.util.ArrayList<>(fieldClassificationResponse.getClassifications()));
        }
        android.service.autofill.SaveRequest saveRequest = new android.service.autofill.SaveRequest(contexts, this.mClientState, this.mSelectedDatasetIds);
        this.mRemoteFillService.onSaveRequest(saveRequest);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.ArrayList<android.service.autofill.FillContext> mergePreviousSessionLocked(boolean forSave) {
        java.util.ArrayList<com.android.server.autofill.Session> previousSessions = this.mService.getPreviousSessionsLocked(this);
        if (previousSessions != null) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "mergeSessions(" + this.id + "): Merging the content of " + previousSessions.size() + " sessions for task " + this.taskId);
            }
            java.util.ArrayList<android.service.autofill.FillContext> contexts = new java.util.ArrayList<>();
            for (int i = 0; i < previousSessions.size(); i++) {
                com.android.server.autofill.Session previousSession = previousSessions.get(i);
                java.util.ArrayList<android.service.autofill.FillContext> previousContexts = previousSession.mContexts;
                if (previousContexts == null) {
                    android.util.Slog.w(TAG, "mergeSessions(" + this.id + "): Not merging null contexts from " + previousSession.id);
                } else {
                    if (forSave) {
                        previousSession.updateValuesForSaveLocked();
                    }
                    if (com.android.server.autofill.Helper.sDebug) {
                        android.util.Slog.d(TAG, "mergeSessions(" + this.id + "): adding " + previousContexts.size() + " context from previous session #" + previousSession.id);
                    }
                    contexts.addAll(previousContexts);
                    if (this.mClientState == null && previousSession.mClientState != null) {
                        if (com.android.server.autofill.Helper.sDebug) {
                            android.util.Slog.d(TAG, "mergeSessions(" + this.id + "): setting client state from previous session" + previousSession.id);
                        }
                        this.mClientState = previousSession.mClientState;
                    }
                }
            }
            contexts.addAll(this.mContexts);
            return contexts;
        }
        return new java.util.ArrayList<>(this.mContexts);
    }

    private boolean requestNewFillResponseOnViewEnteredIfNecessaryLocked(android.view.autofill.AutofillId id, com.android.server.autofill.ViewState viewState, int flags) {
        if ((flags & 1) != 0 || this.mSessionExt.hookShouldRequestNewFillResponse()) {
            this.mSessionExt.hookSetOnFillRequestReason(1);
            this.mSessionFlags.mAugmentedAutofillOnly = false;
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "Re-starting session on view " + id + " and flags " + flags);
            }
            requestNewFillResponseLocked(viewState, 256, flags);
            return true;
        }
        if (shouldStartNewPartitionLocked(id, flags)) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "Starting partition or augmented request for view id " + id + ": " + viewState.getStateAsString());
            }
            this.mSessionFlags.mAugmentedAutofillOnly = false;
            requestNewFillResponseLocked(viewState, 32, flags);
            return true;
        }
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "Not starting new partition for view " + id + ": " + viewState.getStateAsString());
        }
        return false;
    }

    private boolean shouldStartNewPartitionLocked(android.view.autofill.AutofillId id, int flags) {
        com.android.server.autofill.ViewState currentView = this.mViewStates.get(id);
        android.util.SparseArray<android.service.autofill.FillResponse> responses = shouldRequestSecondaryProvider(flags) ? this.mSecondaryResponses : this.mResponses;
        if (responses == null) {
            return currentView != null && (currentView.getState() & 65536) == 0;
        }
        if (this.mSessionFlags.mExpiredResponse) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "Starting a new partition because the response has expired.");
            }
            return true;
        }
        int numResponses = responses.size();
        if (numResponses >= com.android.server.autofill.AutofillManagerService.getPartitionMaxCount()) {
            android.util.Slog.e(TAG, "Not starting a new partition on " + id + " because session " + this.id + " reached maximum of " + com.android.server.autofill.AutofillManagerService.getPartitionMaxCount());
            return false;
        }
        for (int responseNum = 0; responseNum < numResponses; responseNum++) {
            android.service.autofill.FillResponse response = responses.valueAt(responseNum);
            if (com.android.internal.util.ArrayUtils.contains(response.getIgnoredIds(), id)) {
                return false;
            }
            android.service.autofill.SaveInfo saveInfo = response.getSaveInfo();
            if (saveInfo != null && (com.android.internal.util.ArrayUtils.contains(saveInfo.getOptionalIds(), id) || com.android.internal.util.ArrayUtils.contains(saveInfo.getRequiredIds(), id))) {
                return false;
            }
            java.util.List<android.service.autofill.Dataset> datasets = response.getDatasets();
            if (datasets != null) {
                int numDatasets = datasets.size();
                for (int dataSetNum = 0; dataSetNum < numDatasets; dataSetNum++) {
                    java.util.ArrayList<android.view.autofill.AutofillId> fields = datasets.get(dataSetNum).getFieldIds();
                    if (fields != null && fields.contains(id)) {
                        return false;
                    }
                }
            }
            if (com.android.internal.util.ArrayUtils.contains(response.getAuthenticationIds(), id)) {
                return false;
            }
        }
        return true;
    }

    boolean shouldRequestSecondaryProvider(int flags) {
        if (!this.mService.isAutofillCredmanIntegrationEnabled() || this.mSecondaryProviderHandler == null) {
            return false;
        }
        return this.mIsPrimaryCredential ? (flags & 2048) == 0 : (flags & 2048) != 0;
    }

    void updateLocked(android.view.autofill.AutofillId id, android.graphics.Rect virtualBounds, android.view.autofill.AutofillValue value, int action, int flags) {
        java.lang.String currentUrl;
        if (this.mDestroyed) {
            android.util.Slog.w(TAG, "Call to Session#updateLocked() rejected - session: " + id + " destroyed");
            return;
        }
        if (action == 5) {
            this.mSessionFlags.mExpiredResponse = true;
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "Set the response has expired.");
            }
            this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReasonIfNoReasonExists(3);
            this.mPresentationStatsEventLogger.logAndEndEvent();
            return;
        }
        id.setSessionId(this.id);
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "updateLocked(" + this.id + "): id=" + id + ", action=" + actionAsString(action) + ", flags=" + flags);
        }
        com.android.server.autofill.ViewState viewState = this.mViewStates.get(id);
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "updateLocked(" + this.id + "): mCurrentViewId=" + this.mCurrentViewId + ", mExpiredResponse=" + this.mSessionFlags.mExpiredResponse + ", viewState=" + viewState);
        }
        if (viewState == null) {
            if (action == 1 || action == 4 || action == 2) {
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "Creating viewState for " + id);
                }
                boolean isIgnored = isIgnoredLocked(id);
                viewState = new com.android.server.autofill.ViewState(id, this, isIgnored ? 128 : 1, this.mIsPrimaryCredential);
                this.mViewStates.put(id, viewState);
                if (isIgnored) {
                    if (com.android.server.autofill.Helper.sDebug) {
                        android.util.Slog.d(TAG, "updateLocked(): ignoring view " + viewState);
                        return;
                    }
                    return;
                }
            } else {
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "Ignoring specific action when viewState=null");
                    return;
                }
                return;
            }
        }
        if ((flags & 256) != 0) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Log.d(TAG, "force to reset fill dialog state");
            }
            this.mSessionFlags.mFillDialogDisabled = false;
        }
        if ((flags & 512) != 0) {
            requestAssistStructureForPccLocked(flags);
            return;
        }
        if ((flags & 1024) != 0) {
            this.mSessionFlags.mScreenHasCredmanField = true;
        }
        switch (action) {
            case 1:
                this.mCurrentViewId = viewState.id;
                this.mPreviousNonNullEnteredViewId = viewState.id;
                viewState.update(value, virtualBounds, flags);
                startNewEventForPresentationStatsEventLogger();
                this.mPresentationStatsEventLogger.maybeSetIsNewRequest(true);
                if (!isRequestSupportFillDialog(flags)) {
                    this.mSessionFlags.mFillDialogDisabled = true;
                    this.mPreviouslyFillDialogPotentiallyStarted = false;
                } else {
                    this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReason(8);
                    this.mPreviouslyFillDialogPotentiallyStarted = true;
                }
                requestNewFillResponseLocked(viewState, 16, flags);
                return;
            case 2:
                this.mLatencyBaseTime = android.os.SystemClock.elapsedRealtime();
                boolean wasPreviouslyFillDialog = this.mPreviouslyFillDialogPotentiallyStarted;
                this.mPreviouslyFillDialogPotentiallyStarted = false;
                if (com.android.server.autofill.Helper.sVerbose && virtualBounds != null) {
                    android.util.Slog.v(TAG, "entered on virtual child " + id + ": " + virtualBounds);
                }
                boolean isSameViewEntered = java.util.Objects.equals(this.mCurrentViewId, viewState.id);
                this.mCurrentViewId = viewState.id;
                if (value != null) {
                    viewState.setCurrentValue(value);
                }
                boolean isSameViewAgain = isSameViewEntered || java.util.Objects.equals(this.mCurrentViewId, this.mPreviousNonNullEnteredViewId);
                if (this.mCurrentViewId != null) {
                    this.mPreviousNonNullEnteredViewId = this.mCurrentViewId;
                }
                boolean isCredmanRequested = (flags & 2048) != 0;
                if (shouldRequestSecondaryProvider(flags)) {
                    if (requestNewFillResponseOnViewEnteredIfNecessaryLocked(id, viewState, flags)) {
                        android.util.Slog.v(TAG, "Started a new fill request for secondary provider.");
                        return;
                    }
                    android.service.autofill.FillResponse response = viewState.getSecondaryResponse();
                    if (response != null) {
                        logPresentationStatsOnViewEnteredLocked(response, isCredmanRequested);
                    }
                    viewState.update(value, virtualBounds, flags);
                    return;
                }
                if (this.mCompatMode && (viewState.getState() & 512) != 0) {
                    if (com.android.server.autofill.Helper.sDebug) {
                        android.util.Slog.d(TAG, "Ignoring VIEW_ENTERED on URL BAR (id=" + id + ")");
                        return;
                    }
                    return;
                }
                synchronized (this.mLock) {
                    if (!this.mLogViewEntered) {
                        this.mService.logViewEntered(this.id, null);
                    }
                    this.mLogViewEntered = true;
                    break;
                }
                if (!wasPreviouslyFillDialog && !isSameViewAgain) {
                    this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReason(2);
                    this.mPresentationStatsEventLogger.logAndEndEvent();
                }
                if ((flags & 1) == 0) {
                    if (this.mAugmentedAutofillableIds != null && this.mAugmentedAutofillableIds.contains(id)) {
                        if (!isSameViewEntered) {
                            if (com.android.server.autofill.Helper.sDebug) {
                                android.util.Slog.d(TAG, "trigger augmented autofill.");
                            }
                            triggerAugmentedAutofillLocked(flags);
                            return;
                        } else {
                            if (com.android.server.autofill.Helper.sDebug) {
                                android.util.Slog.d(TAG, "skip augmented autofill for same view: same view entered");
                                return;
                            }
                            return;
                        }
                    }
                    if (this.mSessionFlags.mAugmentedAutofillOnly && isSameViewEntered) {
                        if (com.android.server.autofill.Helper.sDebug) {
                            android.util.Slog.d(TAG, "skip augmented autofill for same view: standard autofill disabled.");
                            return;
                        }
                        return;
                    }
                }
                if (!wasPreviouslyFillDialog) {
                    startNewEventForPresentationStatsEventLogger();
                }
                if (requestNewFillResponseOnViewEnteredIfNecessaryLocked(id, viewState, flags)) {
                    if (wasPreviouslyFillDialog) {
                        this.mPresentationStatsEventLogger.logAndEndEvent();
                        startNewEventForPresentationStatsEventLogger();
                        return;
                    }
                    return;
                }
                android.service.autofill.FillResponse response2 = viewState.getResponse();
                if (response2 != null) {
                    logPresentationStatsOnViewEnteredLocked(response2, isCredmanRequested);
                }
                if (isSameViewEntered) {
                    setFillDialogDisabledAndStartInput();
                    return;
                } else {
                    viewState.update(value, virtualBounds, flags);
                    return;
                }
            case 3:
                if (java.util.Objects.equals(this.mCurrentViewId, viewState.id)) {
                    if (com.android.server.autofill.Helper.sVerbose) {
                        android.util.Slog.v(TAG, "Exiting view " + id);
                    }
                    this.mUi.hideFillUi(this);
                    this.mUi.hideFillDialog(this);
                    hideAugmentedAutofillLocked(viewState);
                    this.mInlineSessionController.resetInlineFillUiLocked();
                    if ((viewState.getState() & 65536) == 0) {
                        this.mCurrentViewId = null;
                    }
                    this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReason(2);
                    return;
                }
                return;
            case 4:
                if (this.mCompatMode && (viewState.getState() & 512) != 0) {
                    if (this.mUrlBar == null) {
                        currentUrl = null;
                    } else {
                        currentUrl = this.mUrlBar.getText().toString().trim();
                    }
                    if (currentUrl == null) {
                        wtf(null, "URL bar value changed, but current value is null", new java.lang.Object[0]);
                        return;
                    }
                    if (value == null || !value.isText()) {
                        wtf(null, "URL bar value changed to null or non-text: %s", value);
                        return;
                    }
                    java.lang.String newUrl = value.getTextValue().toString();
                    if (newUrl.equals(currentUrl)) {
                        if (com.android.server.autofill.Helper.sDebug) {
                            android.util.Slog.d(TAG, "Ignoring change on URL bar as it's the same");
                            return;
                        }
                        return;
                    } else if (this.mSaveOnAllViewsInvisible) {
                        if (com.android.server.autofill.Helper.sDebug) {
                            android.util.Slog.d(TAG, "Ignoring change on URL because session will finish when views are gone");
                            return;
                        }
                        return;
                    } else {
                        if (com.android.server.autofill.Helper.sDebug) {
                            android.util.Slog.d(TAG, "Finishing session because URL bar changed");
                        }
                        forceRemoveFromServiceLocked(5);
                        return;
                    }
                }
                if (!java.util.Objects.equals(value, viewState.getCurrentValue())) {
                    logIfViewClearedLocked(id, value, viewState);
                    updateViewStateAndUiOnValueChangedLocked(id, value, viewState, flags);
                    return;
                }
                return;
            default:
                android.util.Slog.w(TAG, "updateLocked(): unknown action: " + action);
                return;
        }
    }

    private void logPresentationStatsOnViewEnteredLocked(android.service.autofill.FillResponse response, boolean isCredmanRequested) {
        this.mPresentationStatsEventLogger.maybeSetRequestId(response.getRequestId());
        this.mPresentationStatsEventLogger.maybeSetIsCredentialRequest(isCredmanRequested);
        this.mPresentationStatsEventLogger.maybeSetFieldClassificationRequestId(this.mFieldClassificationIdSnapshot);
        this.mPresentationStatsEventLogger.maybeSetAvailableCount(response.getDatasets(), this.mCurrentViewId);
        this.mPresentationStatsEventLogger.maybeSetFocusedId(this.mCurrentViewId);
    }

    private void hideAugmentedAutofillLocked(com.android.server.autofill.ViewState viewState) {
        if ((viewState.getState() & 4096) != 0) {
            viewState.resetState(4096);
            cancelAugmentedAutofillLocked();
        }
    }

    private boolean isIgnoredLocked(android.view.autofill.AutofillId id) {
        android.service.autofill.FillResponse response = getLastResponseLocked(null);
        if (response == null) {
            return false;
        }
        return com.android.internal.util.ArrayUtils.contains(response.getIgnoredIds(), id);
    }

    private void logIfViewClearedLocked(android.view.autofill.AutofillId id, android.view.autofill.AutofillValue value, com.android.server.autofill.ViewState viewState) {
        if ((value == null || value.isEmpty()) && viewState.getCurrentValue() != null && viewState.getCurrentValue().isText() && viewState.getCurrentValue().getTextValue() != null && getSaveInfoLocked() != null) {
            int length = viewState.getCurrentValue().getTextValue().length();
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "updateLocked(" + id + "): resetting value that was " + length + " chars long");
            }
            android.metrics.LogMaker log = newLogMaker(1124).addTaggedData(1125, java.lang.Integer.valueOf(length));
            this.mMetricsLogger.write(log);
        }
    }

    private void updateViewStateAndUiOnValueChangedLocked(android.view.autofill.AutofillId id, android.view.autofill.AutofillValue value, com.android.server.autofill.ViewState viewState, int flags) {
        java.lang.String textValue = null;
        if (this.mIgnoreViewStateResetToEmpty && ((value == null || value.isEmpty()) && viewState.getCurrentValue() != null && viewState.getCurrentValue().isText() && viewState.getCurrentValue().getTextValue() != null && viewState.getCurrentValue().getTextValue().length() > 1)) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "value is resetting to empty, caching the last non-empty value");
            }
            viewState.setCandidateSaveValue(viewState.getCurrentValue());
        } else {
            viewState.setCandidateSaveValue(null);
        }
        if (value == null || !value.isText()) {
            textValue = null;
        } else {
            java.lang.CharSequence text = value.getTextValue();
            if (text != null) {
                textValue = text.toString();
            }
        }
        updateFilteringStateOnValueChangedLocked(textValue, viewState);
        viewState.setCurrentValue(value);
        java.lang.String filterText = textValue;
        android.view.autofill.AutofillValue filledValue = viewState.getAutofilledValue();
        if (filledValue != null) {
            if (filledValue.equals(value)) {
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "ignoring autofilled change on id " + id);
                }
                this.mInlineSessionController.hideInlineSuggestionsUiLocked(viewState.id);
                viewState.resetState(8);
                return;
            }
            if (viewState.id.equals(this.mCurrentViewId) && (viewState.getState() & 4) != 0) {
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "field changed after autofill on id " + id);
                }
                viewState.resetState(4);
                com.android.server.autofill.ViewState currentView = this.mViewStates.get(this.mCurrentViewId);
                currentView.maybeCallOnFillReady(flags);
            }
        }
        if (textValue != null) {
            this.mPresentationStatsEventLogger.onFieldTextUpdated(viewState, textValue.length());
        }
        if (viewState.id.equals(this.mCurrentViewId) && (viewState.getState() & 8192) != 0) {
            if ((viewState.getState() & 32768) != 0) {
                this.mInlineSessionController.disableFilterMatching(viewState.id);
            }
            this.mInlineSessionController.filterInlineFillUiLocked(this.mCurrentViewId, filterText);
        } else if (viewState.id.equals(this.mCurrentViewId) && (viewState.getState() & 4096) != 0 && !android.text.TextUtils.isEmpty(filterText)) {
            this.mInlineSessionController.hideInlineSuggestionsUiLocked(this.mCurrentViewId);
        }
        viewState.setState(8);
        getUiForShowing().filterFillUi(filterText, this);
    }

    private void updateFilteringStateOnValueChangedLocked(java.lang.String newTextValue, com.android.server.autofill.ViewState viewState) {
        java.lang.String currentTextValue;
        if (newTextValue == null) {
            newTextValue = "";
        }
        android.view.autofill.AutofillValue currentValue = viewState.getCurrentValue();
        if (currentValue == null || !currentValue.isText()) {
            currentTextValue = "";
        } else {
            currentTextValue = currentValue.getTextValue().toString();
        }
        if ((viewState.getState() & 16384) == 0) {
            if (!com.android.server.autofill.Helper.containsCharsInOrder(newTextValue, currentTextValue)) {
                viewState.setState(16384);
            }
        } else if (!com.android.server.autofill.Helper.containsCharsInOrder(currentTextValue, newTextValue)) {
            viewState.setState(32768);
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // com.android.server.autofill.ViewState.Listener
    public void onFillReady(android.service.autofill.FillResponse response, android.view.autofill.AutofillId filledId, android.view.autofill.AutofillValue value, int flags) throws java.lang.Throwable {
        java.lang.String filterText;
        java.lang.CharSequence serviceLabel;
        android.graphics.drawable.Drawable serviceIcon;
        synchronized (this.mLock) {
            this.mPresentationStatsEventLogger.maybeSetFieldClassificationRequestId(this.mFieldClassificationIdSnapshot);
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#onFillReady() rejected - session: " + this.id + " destroyed");
                this.mSaveEventLogger.maybeSetSaveUiNotShownReason(9);
                this.mSaveEventLogger.logAndEndEvent();
                this.mPresentationStatsEventLogger.maybeSetNoPresentationEventReason(6);
                this.mPresentationStatsEventLogger.logAndEndEvent();
                return;
            }
            if (value != null && value.isText()) {
                java.lang.String filterText2 = value.getTextValue().toString();
                filterText = filterText2;
            } else {
                filterText = null;
            }
            synchronized (this.mService.mLock) {
                try {
                    serviceLabel = this.mService.getServiceLabelLocked();
                    serviceIcon = this.mService.getServiceIconLocked();
                } catch (java.lang.Throwable th) {
                    th = th;
                    while (true) {
                        try {
                            throw th;
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                    }
                }
            }
            if (serviceLabel == null || serviceIcon == null) {
                wtf(null, "onFillReady(): no service label or icon", new java.lang.Object[0]);
                return;
            }
            synchronized (this.mLock) {
                try {
                    this.mPresentationStatsEventLogger.maybeSetSuggestionSentTimestampMs();
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    while (true) {
                        try {
                            throw th;
                        } catch (java.lang.Throwable th4) {
                            th = th4;
                        }
                    }
                }
            }
            android.view.autofill.AutofillId[] ids = response.getFillDialogTriggerIds();
            if (ids != null && com.android.internal.util.ArrayUtils.contains(ids, filledId)) {
                if (requestShowFillDialog(response, filledId, filterText, flags)) {
                    synchronized (this.mLock) {
                        com.android.server.autofill.ViewState currentView = this.mViewStates.get(this.mCurrentViewId);
                        currentView.setState(131072);
                        this.mPresentationStatsEventLogger.maybeSetDisplayPresentationType(3);
                    }
                    setFillDialogDisabled();
                    return;
                }
                setFillDialogDisabled();
            }
            if (response.supportsInlineSuggestions()) {
                synchronized (this.mLock) {
                    if (requestShowInlineSuggestionsLocked(response, filterText)) {
                        com.android.server.autofill.ViewState currentView2 = this.mViewStates.get(this.mCurrentViewId);
                        currentView2.setState(8192);
                        this.mPresentationStatsEventLogger.maybeSetInlinePresentationAndSuggestionHostUid(this.mContext, this.userId);
                        return;
                    }
                }
            }
            getUiForShowing().showFillUi(filledId, response, filterText, this.mService.getServicePackageName(), this.mComponentName, serviceLabel, serviceIcon, this, this.mContext, this.id, this.mCompatMode, this.mService.getMaster().getMaxInputLengthForAutofill());
            synchronized (this.mLock) {
                if (this.mUiShownTime == 0) {
                    this.mUiShownTime = android.os.SystemClock.elapsedRealtime();
                    long duration = this.mUiShownTime - this.mStartTime;
                    if (com.android.server.autofill.Helper.sDebug) {
                        java.lang.StringBuilder msg = new java.lang.StringBuilder("1st UI for ").append(this.mActivityToken).append(" shown in ");
                        android.util.TimeUtils.formatDuration(duration, msg);
                        android.util.Slog.d(TAG, msg.toString());
                    }
                    java.lang.StringBuilder historyLog = new java.lang.StringBuilder("id=").append(this.id).append(" app=").append(this.mActivityToken).append(" svc=").append(this.mService.getServicePackageName()).append(" latency=");
                    android.util.TimeUtils.formatDuration(duration, historyLog);
                    this.mUiLatencyHistory.log(historyLog.toString());
                    addTaggedDataToRequestLogLocked(response.getRequestId(), 1145, java.lang.Long.valueOf(duration));
                }
            }
        }
    }

    private boolean isCredmanIntegrationActive(android.service.autofill.FillResponse response) {
        return android.service.autofill.Flags.autofillCredmanIntegration() && (response.getFlags() & 8) != 0;
    }

    private void updateFillDialogTriggerIdsLocked() {
        android.service.autofill.FillResponse response = getLastResponseLocked(null);
        if (response == null) {
            return;
        }
        android.view.autofill.AutofillId[] ids = response.getFillDialogTriggerIds();
        notifyClientFillDialogTriggerIds(ids != null ? java.util.Arrays.asList(ids) : null);
    }

    private void notifyClientFillDialogTriggerIds(java.util.List<android.view.autofill.AutofillId> fieldIds) {
        try {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "notifyFillDialogTriggerIds(): " + fieldIds);
            }
            getClient().notifyFillDialogTriggerIds(fieldIds);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Cannot set trigger ids for fill dialog", e);
        }
    }

    private boolean isFillDialogUiEnabled() {
        boolean z;
        synchronized (this.mLock) {
            z = (this.mSessionFlags.mFillDialogDisabled || this.mSessionFlags.mScreenHasCredmanField) ? false : true;
        }
        return z;
    }

    private void setFillDialogDisabled() {
        synchronized (this.mLock) {
            this.mSessionFlags.mFillDialogDisabled = true;
        }
        notifyClientFillDialogTriggerIds(null);
    }

    private void setFillDialogDisabledAndStartInput() {
        android.view.autofill.AutofillId id;
        if (getUiForShowing().isFillDialogShowing()) {
            setFillDialogDisabled();
            synchronized (this.mLock) {
                id = this.mCurrentViewId;
            }
            requestShowSoftInput(id);
        }
    }

    private boolean requestShowFillDialog(android.service.autofill.FillResponse response, android.view.autofill.AutofillId filledId, java.lang.String filterText, int flags) throws java.lang.Throwable {
        android.graphics.drawable.Drawable serviceIcon;
        if (!isFillDialogUiEnabled()) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Log.w(TAG, "requestShowFillDialog: fill dialog is disabled");
            }
            return false;
        }
        if ((flags & 128) != 0) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Log.w(TAG, "requestShowFillDialog: IME is showing");
            }
            return false;
        }
        if (this.mInlineSessionController.isImeShowing()) {
            return false;
        }
        synchronized (this.mLock) {
            try {
                try {
                    if (this.mLastFillDialogTriggerIds != null) {
                        if (com.android.internal.util.ArrayUtils.contains(this.mLastFillDialogTriggerIds, filledId)) {
                            synchronized (this.mLock) {
                                try {
                                    serviceIcon = getServiceIcon(response);
                                } catch (java.lang.Throwable th) {
                                    th = th;
                                }
                                try {
                                    getUiForShowing().showFillDialog(filledId, response, filterText, this.mService.getServicePackageName(), this.mComponentName, serviceIcon, this, this.id, this.mCompatMode, this.mPresentationStatsEventLogger, this.mLock);
                                    return true;
                                } catch (java.lang.Throwable th2) {
                                    th = th2;
                                    throw th;
                                }
                            }
                        }
                    }
                    if (com.android.server.autofill.Helper.sDebug) {
                        android.util.Log.w(TAG, "Last fill dialog triggered ids are changed.");
                    }
                    return false;
                } catch (java.lang.Throwable th3) {
                    th = th3;
                    throw th;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
            }
        }
    }

    private android.graphics.drawable.Drawable getServiceIcon(android.service.autofill.FillResponse response) {
        android.graphics.drawable.Drawable serviceIcon = null;
        int iconResourceId = response.getIconResourceId();
        if (iconResourceId != 0) {
            serviceIcon = this.mService.getMaster().getContext().getPackageManager().getDrawable(this.mService.getServicePackageName(), iconResourceId, null);
        }
        if (serviceIcon == null) {
            android.graphics.drawable.Drawable serviceIcon2 = this.mService.getServiceIconLocked();
            return serviceIcon2;
        }
        return serviceIcon;
    }

    private java.lang.CharSequence getServiceLabel(android.service.autofill.FillResponse response) {
        java.lang.CharSequence serviceLabel = null;
        int customServiceNameId = response.getServiceDisplayNameResourceId();
        if (customServiceNameId != 0) {
            serviceLabel = this.mService.getMaster().getContext().getPackageManager().getText(this.mService.getServicePackageName(), customServiceNameId, null);
        }
        if (serviceLabel == null) {
            java.lang.CharSequence serviceLabel2 = this.mService.getServiceLabelLocked();
            return serviceLabel2;
        }
        return serviceLabel;
    }

    private boolean requestShowInlineSuggestionsLocked(final android.service.autofill.FillResponse response, java.lang.String filterText) {
        if (this.mCurrentViewId == null) {
            android.util.Log.w(TAG, "requestShowInlineSuggestionsLocked(): no view currently focused");
            return false;
        }
        final android.view.autofill.AutofillId focusedId = this.mCurrentViewId;
        java.util.Optional<android.view.inputmethod.InlineSuggestionsRequest> inlineSuggestionsRequest = this.mInlineSessionController.getInlineSuggestionsRequestLocked();
        if (!inlineSuggestionsRequest.isPresent()) {
            android.util.Log.w(TAG, "InlineSuggestionsRequest unavailable");
            return false;
        }
        com.android.server.autofill.RemoteInlineSuggestionRenderService remoteRenderService = this.mService.getRemoteInlineSuggestionRenderServiceLocked();
        if (remoteRenderService == null) {
            android.util.Log.w(TAG, "RemoteInlineSuggestionRenderService not found");
            return false;
        }
        synchronized (this.mLock) {
            this.mLoggedInlineDatasetShown = false;
        }
        com.android.server.autofill.ui.InlineFillUi.InlineFillUiInfo inlineFillUiInfo = new com.android.server.autofill.ui.InlineFillUi.InlineFillUiInfo(inlineSuggestionsRequest.get(), focusedId, filterText, remoteRenderService, this.userId, this.id);
        com.android.server.autofill.ui.InlineFillUi inlineFillUi = com.android.server.autofill.ui.InlineFillUi.forAutofill(inlineFillUiInfo, response, new com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback() { // from class: com.android.server.autofill.Session.3
            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void autofill(android.service.autofill.Dataset dataset, int datasetIndex) {
                com.android.server.autofill.Session.this.fill(response.getRequestId(), datasetIndex, dataset, 2);
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void authenticate(int requestId, int datasetIndex) {
                com.android.server.autofill.Session.this.authenticate(response.getRequestId(), datasetIndex, response.getAuthentication(), response.getClientState(), 2);
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void startIntentSender(android.content.IntentSender intentSender) {
                com.android.server.autofill.Session.this.startIntentSender(intentSender, new android.content.Intent());
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void onError() {
                synchronized (com.android.server.autofill.Session.this.mLock) {
                    com.android.server.autofill.Session.this.mInlineSessionController.setInlineFillUiLocked(com.android.server.autofill.ui.InlineFillUi.emptyUi(focusedId));
                }
            }

            @Override // com.android.server.autofill.ui.InlineFillUi.InlineSuggestionUiCallback
            public void onInflate() {
                com.android.server.autofill.Session.this.onShown(2, 1);
            }
        }, this.mService.getMaster().getMaxInputLengthForAutofill());
        return this.mInlineSessionController.setInlineFillUiLocked(inlineFillUi);
    }

    private android.os.ResultReceiver constructCredentialManagerCallback(final int requestId) {
        android.os.ResultReceiver resultReceiver = new android.os.ResultReceiver(this.mHandler) { // from class: com.android.server.autofill.Session.4
            final android.view.autofill.AutofillId mAutofillId;

            {
                this.mAutofillId = com.android.server.autofill.Session.this.mCurrentViewId;
            }

            @Override // android.os.ResultReceiver
            protected void onReceiveResult(int resultCode, android.os.Bundle resultData) {
                if (resultCode == 0) {
                    android.util.Slog.d(com.android.server.autofill.Session.TAG, "onReceiveResult from Credential Manager bottom sheet with mCurrentViewId: " + this.mAutofillId);
                    android.credentials.GetCredentialResponse getCredentialResponse = (android.credentials.GetCredentialResponse) resultData.getParcelable("android.service.credentials.extra.GET_CREDENTIAL_RESPONSE", android.credentials.GetCredentialResponse.class);
                    if (android.service.autofill.Flags.autofillCredmanDevIntegration()) {
                        com.android.server.autofill.Session.this.sendCredentialManagerResponseToApp(getCredentialResponse, null, this.mAutofillId);
                        return;
                    }
                    android.service.autofill.Dataset datasetFromCredential = com.android.server.autofill.Session.this.getDatasetFromCredentialResponse(getCredentialResponse);
                    if (datasetFromCredential != null) {
                        com.android.server.autofill.Session.this.autoFill(requestId, -1, datasetFromCredential, false, 4);
                        return;
                    }
                    return;
                }
                if (resultCode == -1) {
                    java.lang.String[] exception = resultData.getStringArray("android.service.credentials.extra.GET_CREDENTIAL_EXCEPTION");
                    if (exception != null && exception.length >= 2) {
                        java.lang.String errType = exception[0];
                        java.lang.String errMsg = exception[1];
                        android.util.Slog.w(com.android.server.autofill.Session.TAG, "Credman bottom sheet from pinned entry failed with: + " + errType + " , " + errMsg);
                        com.android.server.autofill.Session.this.sendCredentialManagerResponseToApp(null, new android.credentials.GetCredentialException(errType, errMsg), this.mAutofillId);
                        return;
                    }
                    return;
                }
                android.util.Slog.d(com.android.server.autofill.Session.TAG, "Unknown resultCode from credential manager bottom sheet: " + resultCode);
            }
        };
        android.os.ResultReceiver ipcFriendlyResultReceiver = toIpcFriendlyResultReceiver(resultReceiver);
        return ipcFriendlyResultReceiver;
    }

    private android.os.ResultReceiver toIpcFriendlyResultReceiver(android.os.ResultReceiver resultReceiver) {
        android.os.Parcel parcel = android.os.Parcel.obtain();
        resultReceiver.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        android.os.ResultReceiver ipcFriendly = (android.os.ResultReceiver) android.os.ResultReceiver.CREATOR.createFromParcel(parcel);
        parcel.recycle();
        return ipcFriendly;
    }

    boolean isDestroyed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mDestroyed;
        }
        return z;
    }

    android.view.autofill.IAutoFillManagerClient getClient() {
        android.view.autofill.IAutoFillManagerClient iAutoFillManagerClient;
        synchronized (this.mLock) {
            iAutoFillManagerClient = this.mClient;
        }
        return iAutoFillManagerClient;
    }

    private void notifyUnavailableToClient(int sessionFinishedState, java.util.ArrayList<android.view.autofill.AutofillId> autofillableIds) {
        synchronized (this.mLock) {
            if (this.mCurrentViewId == null) {
                return;
            }
            try {
                if (this.mHasCallback) {
                    this.mClient.notifyNoFillUi(this.id, this.mCurrentViewId, sessionFinishedState);
                } else if (sessionFinishedState != 0) {
                    this.mClient.setSessionFinished(sessionFinishedState, autofillableIds);
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Error notifying client no fill UI: id=" + this.mCurrentViewId, e);
            }
        }
    }

    private void notifyDisableAutofillToClient(long disableDuration, android.content.ComponentName componentName) {
        synchronized (this.mLock) {
            if (this.mCurrentViewId == null) {
                return;
            }
            try {
                this.mClient.notifyDisableAutofill(disableDuration, componentName);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Error notifying client disable autofill: id=" + this.mCurrentViewId, e);
            }
        }
    }

    private void updateTrackedIdsLocked() {
        boolean saveOnFinish;
        android.view.autofill.AutofillId saveTriggerId;
        int flags;
        android.util.ArraySet<android.view.autofill.AutofillId> trackedViews;
        android.util.ArraySet<android.view.autofill.AutofillId> fillableIds;
        android.service.autofill.FillResponse response = getLastResponseLocked(null);
        if (response == null) {
            return;
        }
        android.util.ArraySet<android.view.autofill.AutofillId> trackedViews2 = null;
        this.mSaveOnAllViewsInvisible = false;
        android.service.autofill.SaveInfo saveInfo = response.getSaveInfo();
        boolean z = true;
        if (saveInfo == null) {
            this.mSaveEventLogger.maybeSetSaveUiNotShownReason(2);
            saveOnFinish = true;
            saveTriggerId = null;
            flags = 0;
            trackedViews = null;
        } else {
            android.view.autofill.AutofillId saveTriggerId2 = saveInfo.getTriggerId();
            if (saveTriggerId2 != null) {
                writeLog(1228);
                this.mSaveEventLogger.maybeSetSaveUiShownReason(3);
            }
            int flags2 = saveInfo.getFlags();
            this.mSaveOnAllViewsInvisible = (flags2 & 1) != 0;
            this.mFillResponseEventLogger.maybeSetSaveUiTriggerIds(1);
            this.mSaveEventLogger.maybeSetRequestId(response.getRequestId());
            this.mSaveEventLogger.maybeSetAppPackageUid(this.uid);
            this.mSaveEventLogger.maybeSetSaveUiTriggerIds(1);
            this.mSaveEventLogger.maybeSetFlag(flags2);
            if (this.mSaveOnAllViewsInvisible) {
                if (0 == 0) {
                    trackedViews2 = new android.util.ArraySet<>();
                }
                if (saveInfo.getRequiredIds() != null) {
                    java.util.Collections.addAll(trackedViews2, saveInfo.getRequiredIds());
                    this.mSaveEventLogger.maybeSetSaveUiShownReason(1);
                }
                if (saveInfo.getOptionalIds() != null) {
                    java.util.Collections.addAll(trackedViews2, saveInfo.getOptionalIds());
                    this.mSaveEventLogger.maybeSetSaveUiShownReason(2);
                }
            }
            if ((flags2 & 2) != 0) {
                this.mSaveEventLogger.maybeSetSaveUiShownReason(0);
                this.mSaveEventLogger.maybeSetSaveUiNotShownReason(8);
                saveOnFinish = false;
                saveTriggerId = saveTriggerId2;
                flags = flags2;
                trackedViews = trackedViews2;
            } else {
                saveOnFinish = true;
                saveTriggerId = saveTriggerId2;
                flags = flags2;
                trackedViews = trackedViews2;
            }
        }
        java.util.List<android.service.autofill.Dataset> datasets = response.getDatasets();
        android.util.ArraySet<android.view.autofill.AutofillId> fillableIds2 = null;
        if (datasets == null) {
            fillableIds = null;
        } else {
            for (int i = 0; i < datasets.size(); i++) {
                android.service.autofill.Dataset dataset = datasets.get(i);
                java.util.ArrayList<android.view.autofill.AutofillId> fieldIds = dataset.getFieldIds();
                if (fieldIds != null) {
                    for (int j = 0; j < fieldIds.size(); j++) {
                        android.view.autofill.AutofillId id = fieldIds.get(j);
                        if (id != null && (trackedViews == null || !trackedViews.contains(id))) {
                            fillableIds2 = com.android.internal.util.ArrayUtils.add(fillableIds2, id);
                        }
                    }
                }
            }
            fillableIds = fillableIds2;
        }
        try {
            if (com.android.server.autofill.Helper.sVerbose) {
                try {
                    java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("updateTrackedIdsLocked(): trackedViews: ").append(trackedViews).append(" fillableIds: ").append(fillableIds).append(" triggerId: ").append(saveTriggerId).append(" saveOnFinish:").append(saveOnFinish).append(" flags: ").append(flags).append(" hasSaveInfo: ");
                    if (saveInfo == null) {
                        z = false;
                    }
                    android.util.Slog.v(TAG, sbAppend.append(z).toString());
                    try {
                        this.mClient.setTrackedViews(this.id, com.android.server.autofill.Helper.toArray(trackedViews), this.mSaveOnAllViewsInvisible, saveOnFinish, com.android.server.autofill.Helper.toArray(fillableIds), saveTriggerId);
                        return;
                    } catch (android.os.RemoteException e) {
                        e = e;
                    }
                } catch (android.os.RemoteException e2) {
                    e = e2;
                }
            } else {
                this.mClient.setTrackedViews(this.id, com.android.server.autofill.Helper.toArray(trackedViews), this.mSaveOnAllViewsInvisible, saveOnFinish, com.android.server.autofill.Helper.toArray(fillableIds), saveTriggerId);
                return;
            }
        } catch (android.os.RemoteException e3) {
            e = e3;
        }
        android.util.Slog.w(TAG, "Cannot set tracked ids", e);
    }

    void setAutofillFailureLocked(java.util.List<android.view.autofill.AutofillId> ids) {
        if (com.android.server.autofill.Helper.sVerbose && !ids.isEmpty()) {
            android.util.Slog.v(TAG, "Total views that failed to populate: " + ids.size());
        }
        for (int i = 0; i < ids.size(); i++) {
            android.view.autofill.AutofillId id = ids.get(i);
            com.android.server.autofill.ViewState viewState = this.mViewStates.get(id);
            if (viewState == null) {
                android.util.Slog.w(TAG, "setAutofillFailure(): no view for id " + id);
            } else {
                viewState.resetState(4);
                int state = viewState.getState();
                viewState.setState(state | 1024);
                if (com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "Changed state of " + id + " to " + viewState.getStateAsString());
                }
            }
        }
        this.mPresentationStatsEventLogger.maybeSetViewFillFailureCounts(ids.size());
    }

    void setViewAutofilledLocked(android.view.autofill.AutofillId id) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "View autofilled: " + id);
        }
        if (id.getSessionId() == 0) {
            id.setSessionId(this.id);
        }
        this.mPresentationStatsEventLogger.maybeAddSuccessId(id);
    }

    private void replaceResponseLocked(android.service.autofill.FillResponse oldResponse, android.service.autofill.FillResponse newResponse, android.os.Bundle newClientState) {
        setViewStatesLocked(oldResponse, 1, true, true);
        newResponse.setRequestId(oldResponse.getRequestId());
        processResponseLockedForPcc(newResponse, newClientState, 0);
    }

    private void processNullResponseLocked(int requestId, int flags) {
        java.util.ArrayList<android.view.autofill.AutofillId> autofillableIds;
        unregisterDelayedFillBroadcastLocked();
        if ((flags & 1) != 0) {
            getUiForShowing().showError(android.R.string.auto_data_switch_content, this);
        }
        android.service.autofill.FillContext context = getFillContextByRequestIdLocked(requestId);
        if (context == null) {
            android.util.Slog.w(TAG, "processNullResponseLocked(): no context for req " + requestId);
            autofillableIds = null;
        } else {
            android.app.assist.AssistStructure structure = context.getStructure();
            autofillableIds = com.android.server.autofill.Helper.getAutofillIds(structure, true);
        }
        this.mFillResponseEventLogger.maybeSetAvailableCount(0);
        this.mFillResponseEventLogger.maybeSetLatencyResponseProcessingMillis();
        this.mFillResponseEventLogger.logAndEndEvent();
        this.mService.resetLastResponse();
        this.mAugmentedAutofillDestroyer = triggerAugmentedAutofillLocked(flags);
        if (this.mAugmentedAutofillDestroyer == null && (flags & 4) == 0) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "canceling session " + this.id + " when service returned null and it cannot be augmented. AutofillableIds: " + autofillableIds);
            }
            notifyUnavailableToClient(2, autofillableIds);
            removeFromService();
            return;
        }
        if ((flags & 4) != 0) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "keeping session " + this.id + " when service returned null and augmented service is disabled for password fields. AutofillableIds: " + autofillableIds);
            }
            this.mInlineSessionController.hideInlineSuggestionsUiLocked(this.mCurrentViewId);
        } else if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "keeping session " + this.id + " when service returned null but it can be augmented. AutofillableIds: " + autofillableIds);
        }
        this.mAugmentedAutofillableIds = autofillableIds;
        try {
            this.mClient.setState(32);
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Error setting client to autofill-only", e);
        }
    }

    private java.lang.Runnable triggerAugmentedAutofillLocked(int flags) {
        if ((flags & 4) != 0) {
            return null;
        }
        int supportedModes = this.mService.getSupportedSmartSuggestionModesLocked();
        if (supportedModes == 0) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "triggerAugmentedAutofillLocked(): no supported modes");
            }
            return null;
        }
        final com.android.server.autofill.RemoteAugmentedAutofillService remoteService = this.mService.getRemoteAugmentedAutofillServiceLocked();
        if (remoteService == null) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "triggerAugmentedAutofillLocked(): no service for user");
            }
            return null;
        }
        if ((supportedModes & 1) == 0) {
            android.util.Slog.w(TAG, "Unsupported Smart Suggestion mode: " + supportedModes);
            return null;
        }
        if (this.mCurrentViewId == null) {
            android.util.Slog.w(TAG, "triggerAugmentedAutofillLocked(): no view currently focused");
            return null;
        }
        boolean isAllowlisted = this.mService.isWhitelistedForAugmentedAutofillLocked(this.mComponentName);
        if (!isAllowlisted) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "triggerAugmentedAutofillLocked(): " + android.content.ComponentName.flattenToShortString(this.mComponentName) + " not whitelisted ");
            }
            logAugmentedAutofillRequestLocked(1, remoteService.getComponentName(), this.mCurrentViewId, isAllowlisted, null);
            return null;
        }
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "calling Augmented Autofill Service (" + android.content.ComponentName.flattenToShortString(remoteService.getComponentName()) + ") on view " + this.mCurrentViewId + " using suggestion mode " + android.view.autofill.AutofillManager.getSmartSuggestionModeToString(1) + " when server returned null for session " + this.id);
        }
        this.mFillRequestEventLogger.startLogForNewRequest();
        this.mRequestCount++;
        this.mFillRequestEventLogger.maybeSetAppPackageUid(this.uid);
        this.mFillRequestEventLogger.maybeSetFlags(this.mFlags);
        this.mFillRequestEventLogger.maybeSetRequestId(1);
        this.mFillRequestEventLogger.maybeSetIsAugmented(true);
        this.mFillRequestEventLogger.logAndEndEvent();
        com.android.server.autofill.ViewState viewState = this.mViewStates.get(this.mCurrentViewId);
        viewState.setState(4096);
        android.view.autofill.AutofillValue currentValue = viewState.getCurrentValue();
        if (this.mAugmentedRequestsLogs == null) {
            this.mAugmentedRequestsLogs = new java.util.ArrayList<>();
        }
        android.metrics.LogMaker log = newLogMaker(1630, remoteService.getComponentName().getPackageName());
        this.mAugmentedRequestsLogs.add(log);
        android.view.autofill.AutofillId focusedId = this.mCurrentViewId;
        java.util.function.Consumer<android.view.inputmethod.InlineSuggestionsRequest> requestAugmentedAutofill = new com.android.server.autofill.Session.AugmentedAutofillInlineSuggestionRequestConsumer(this, focusedId, isAllowlisted, 1, currentValue);
        com.android.server.autofill.RemoteInlineSuggestionRenderService remoteRenderService = this.mService.getRemoteInlineSuggestionRenderServiceLocked();
        if (remoteRenderService != null && ((this.mSessionFlags.mAugmentedAutofillOnly || !this.mSessionFlags.mInlineSupportedByService || this.mSessionFlags.mExpiredResponse) && (isViewFocusedLocked(flags) || isRequestSupportFillDialog(flags)))) {
            if (com.android.server.autofill.Helper.sDebug) {
                android.util.Slog.d(TAG, "Create inline request for augmented autofill");
            }
            remoteRenderService.getInlineSuggestionsRendererInfo(new android.os.RemoteCallback(new com.android.server.autofill.Session.AugmentedAutofillInlineSuggestionRendererOnResultListener(this, focusedId, requestAugmentedAutofill), this.mHandler));
        } else {
            requestAugmentedAutofill.accept(this.mInlineSessionController.getInlineSuggestionsRequestLocked().orElse(null));
        }
        if (this.mAugmentedAutofillDestroyer == null) {
            java.util.Objects.requireNonNull(remoteService);
            this.mAugmentedAutofillDestroyer = new java.lang.Runnable() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    remoteService.onDestroyAutofillWindowsRequest();
                }
            };
        }
        return this.mAugmentedAutofillDestroyer;
    }

    private static class AugmentedAutofillInlineSuggestionRendererOnResultListener implements android.os.RemoteCallback.OnResultListener {
        final android.view.autofill.AutofillId mFocusedId;
        java.util.function.Consumer<android.view.inputmethod.InlineSuggestionsRequest> mRequestAugmentedAutofill;
        java.lang.ref.WeakReference<com.android.server.autofill.Session> mSessionWeakRef;

        AugmentedAutofillInlineSuggestionRendererOnResultListener(com.android.server.autofill.Session session, android.view.autofill.AutofillId focussedId, java.util.function.Consumer<android.view.inputmethod.InlineSuggestionsRequest> requestAugmentedAutofill) {
            this.mSessionWeakRef = new java.lang.ref.WeakReference<>(session);
            this.mFocusedId = focussedId;
            this.mRequestAugmentedAutofill = requestAugmentedAutofill;
        }

        public void onResult(android.os.Bundle result) {
            com.android.server.autofill.Session session = this.mSessionWeakRef.get();
            if (com.android.server.autofill.Session.logIfSessionNull(session, "AugmentedAutofillInlineSuggestionRendererOnResultListener:")) {
                return;
            }
            synchronized (session.mLock) {
                session.mInlineSessionController.onCreateInlineSuggestionsRequestLocked(this.mFocusedId, this.mRequestAugmentedAutofill, result);
            }
        }
    }

    private static class AugmentedAutofillInlineSuggestionRequestConsumer implements java.util.function.Consumer<android.view.inputmethod.InlineSuggestionsRequest> {
        final android.view.autofill.AutofillValue mCurrentValue;
        final android.view.autofill.AutofillId mFocusedId;
        final boolean mIsAllowlisted;
        final int mMode;
        java.lang.ref.WeakReference<com.android.server.autofill.Session> mSessionWeakRef;

        AugmentedAutofillInlineSuggestionRequestConsumer(com.android.server.autofill.Session session, android.view.autofill.AutofillId focussedId, boolean isAllowlisted, int mode, android.view.autofill.AutofillValue currentValue) {
            this.mSessionWeakRef = new java.lang.ref.WeakReference<>(session);
            this.mFocusedId = focussedId;
            this.mIsAllowlisted = isAllowlisted;
            this.mMode = mode;
            this.mCurrentValue = currentValue;
        }

        @Override // java.util.function.Consumer
        public void accept(android.view.inputmethod.InlineSuggestionsRequest inlineSuggestionsRequest) {
            com.android.server.autofill.Session session = this.mSessionWeakRef.get();
            if (com.android.server.autofill.Session.logIfSessionNull(session, "AugmentedAutofillInlineSuggestionRequestConsumer:")) {
                return;
            }
            session.onAugmentedAutofillInlineSuggestionAccept(inlineSuggestionsRequest, this.mFocusedId, this.mIsAllowlisted, this.mMode, this.mCurrentValue);
        }
    }

    private static class AugmentedAutofillInlineSuggestionsResponseCallback implements java.util.function.Function<com.android.server.autofill.ui.InlineFillUi, java.lang.Boolean> {
        java.lang.ref.WeakReference<com.android.server.autofill.Session> mSessionWeakRef;

        AugmentedAutofillInlineSuggestionsResponseCallback(com.android.server.autofill.Session session) {
            this.mSessionWeakRef = new java.lang.ref.WeakReference<>(session);
        }

        @Override // java.util.function.Function
        public java.lang.Boolean apply(com.android.server.autofill.ui.InlineFillUi inlineFillUi) {
            java.lang.Boolean boolValueOf;
            com.android.server.autofill.Session session = this.mSessionWeakRef.get();
            if (com.android.server.autofill.Session.logIfSessionNull(session, "AugmentedAutofillInlineSuggestionsResponseCallback:")) {
                return false;
            }
            synchronized (session.mLock) {
                boolValueOf = java.lang.Boolean.valueOf(session.mInlineSessionController.setInlineFillUiLocked(inlineFillUi));
            }
            return boolValueOf;
        }
    }

    private static class AugmentedAutofillErrorCallback implements java.lang.Runnable {
        java.lang.ref.WeakReference<com.android.server.autofill.Session> mSessionWeakRef;

        AugmentedAutofillErrorCallback(com.android.server.autofill.Session session) {
            this.mSessionWeakRef = new java.lang.ref.WeakReference<>(session);
        }

        @Override // java.lang.Runnable
        public void run() {
            com.android.server.autofill.Session session = this.mSessionWeakRef.get();
            if (com.android.server.autofill.Session.logIfSessionNull(session, "AugmentedAutofillErrorCallback:")) {
                return;
            }
            session.onAugmentedAutofillErrorCallback();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean logIfSessionNull(com.android.server.autofill.Session session, java.lang.String logPrefix) {
        if (session == null) {
            android.util.Slog.wtf(TAG, logPrefix + " Session null");
            return true;
        }
        if (session.mDestroyed) {
            android.util.Slog.w(TAG, logPrefix + " Session destroyed, but following through");
            return false;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAugmentedAutofillInlineSuggestionAccept(android.view.inputmethod.InlineSuggestionsRequest inlineSuggestionsRequest, android.view.autofill.AutofillId focussedId, boolean isAllowlisted, int mode, android.view.autofill.AutofillValue currentValue) {
        synchronized (this.mLock) {
            com.android.server.autofill.RemoteAugmentedAutofillService remoteService = this.mService.getRemoteAugmentedAutofillServiceLocked();
            logAugmentedAutofillRequestLocked(mode, remoteService.getComponentName(), focussedId, isAllowlisted, java.lang.Boolean.valueOf(inlineSuggestionsRequest != null));
            remoteService.onRequestAutofillLocked(this.id, this.mClient, this.taskId, this.mComponentName, this.mActivityToken, android.view.autofill.AutofillId.withoutSession(focussedId), currentValue, inlineSuggestionsRequest, new com.android.server.autofill.Session.AugmentedAutofillInlineSuggestionsResponseCallback(this), new com.android.server.autofill.Session.AugmentedAutofillErrorCallback(this), this.mService.getRemoteInlineSuggestionRenderServiceLocked(), this.userId);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAugmentedAutofillErrorCallback() {
        synchronized (this.mLock) {
            cancelAugmentedAutofillLocked();
            this.mInlineSessionController.setInlineFillUiLocked(com.android.server.autofill.ui.InlineFillUi.emptyUi(this.mCurrentViewId));
        }
    }

    private void cancelAugmentedAutofillLocked() {
        com.android.server.autofill.RemoteAugmentedAutofillService remoteService = this.mService.getRemoteAugmentedAutofillServiceLocked();
        if (remoteService == null) {
            android.util.Slog.w(TAG, "cancelAugmentedAutofillLocked(): no service for user");
            return;
        }
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "cancelAugmentedAutofillLocked() on " + this.mCurrentViewId);
        }
        remoteService.onDestroyAutofillWindowsRequest();
    }

    private void processResponseLocked(android.service.autofill.FillResponse newResponse, android.os.Bundle newClientState, int flags) {
        this.mUi.hideAll(this);
        if ((newResponse.getFlags() & 4) == 0) {
            android.util.Slog.d(TAG, "Service did not request to wait for delayed fill response.");
            unregisterDelayedFillBroadcastLocked();
        }
        int requestId = newResponse.getRequestId();
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "processResponseLocked(): mCurrentViewId=" + this.mCurrentViewId + ",flags=" + flags + ", reqId=" + requestId + ", resp=" + newResponse + ",newClientState=" + newClientState);
        }
        if (this.mResponses == null) {
            this.mResponses = new android.util.SparseArray<>(2);
        }
        this.mResponses.put(requestId, newResponse);
        this.mClientState = newClientState != null ? newClientState : newResponse.getClientState();
        boolean webviewRequestedCredman = newClientState != null && newClientState.getBoolean("webview_requested_credential", false);
        java.util.List<android.service.autofill.Dataset> datasetList = newResponse.getDatasets();
        this.mPresentationStatsEventLogger.maybeSetWebviewRequestedCredential(webviewRequestedCredman);
        this.mPresentationStatsEventLogger.maybeSetFieldClassificationRequestId(sIdCounterForPcc.get());
        this.mPresentationStatsEventLogger.maybeSetAvailableCount(datasetList, this.mCurrentViewId);
        this.mFillResponseEventLogger.maybeSetDatasetsCountAfterPotentialPccFiltering(datasetList);
        setViewStatesLocked(newResponse, 2, false, true);
        updateFillDialogTriggerIdsLocked();
        updateTrackedIdsLocked();
        if (this.mSessionExt.useOplusAutofillService(this.mClientState, newResponse)) {
            java.util.List<android.service.autofill.Dataset> datasets = newResponse.getDatasets();
            if (datasets != null) {
                android.service.autofill.Dataset dataset = datasets.get(0);
                autoFill(newResponse.getRequestId(), 0, dataset, false, 0);
                return;
            }
            return;
        }
        if (this.mCurrentViewId == null) {
            return;
        }
        com.android.server.autofill.ViewState currentView = this.mViewStates.get(this.mCurrentViewId);
        currentView.maybeCallOnFillReady(flags);
    }

    private void setViewStatesLocked(android.service.autofill.FillResponse response, int state, boolean clearResponse, boolean isPrimary) {
        java.util.List<android.service.autofill.Dataset> datasets = response.getDatasets();
        if (datasets != null && !datasets.isEmpty()) {
            for (int i = 0; i < datasets.size(); i++) {
                android.service.autofill.Dataset dataset = datasets.get(i);
                if (dataset == null) {
                    android.util.Slog.w(TAG, "Ignoring null dataset on " + datasets);
                } else {
                    setViewStatesLocked(response, dataset, state, clearResponse, isPrimary);
                }
            }
        } else if (response.getAuthentication() != null) {
            for (android.view.autofill.AutofillId autofillId : response.getAuthenticationIds()) {
                com.android.server.autofill.ViewState viewState = createOrUpdateViewStateLocked(autofillId, state, null);
                if (!clearResponse) {
                    viewState.setResponse(response, isPrimary);
                } else {
                    viewState.setResponse(null, isPrimary);
                }
            }
        }
        android.service.autofill.SaveInfo saveInfo = response.getSaveInfo();
        if (saveInfo != null) {
            android.view.autofill.AutofillId[] requiredIds = saveInfo.getRequiredIds();
            if (requiredIds != null) {
                for (android.view.autofill.AutofillId id : requiredIds) {
                    createOrUpdateViewStateLocked(id, state, null);
                }
            }
            android.view.autofill.AutofillId[] optionalIds = saveInfo.getOptionalIds();
            if (optionalIds != null) {
                for (android.view.autofill.AutofillId id2 : optionalIds) {
                    createOrUpdateViewStateLocked(id2, state, null);
                }
            }
        }
        android.view.autofill.AutofillId[] authIds = response.getAuthenticationIds();
        if (authIds != null) {
            for (android.view.autofill.AutofillId id3 : authIds) {
                createOrUpdateViewStateLocked(id3, state, null);
            }
        }
    }

    private void setViewStatesLocked(android.service.autofill.FillResponse response, android.service.autofill.Dataset dataset, int state, boolean clearResponse, boolean isPrimary) {
        java.util.ArrayList<android.view.autofill.AutofillId> ids = dataset.getFieldIds();
        java.util.ArrayList<android.view.autofill.AutofillValue> values = dataset.getFieldValues();
        for (int j = 0; j < ids.size(); j++) {
            android.view.autofill.AutofillId id = ids.get(j);
            android.view.autofill.AutofillValue value = values.get(j);
            com.android.server.autofill.ViewState viewState = createOrUpdateViewStateLocked(id, state, value);
            java.lang.String datasetId = dataset.getId();
            if (datasetId != null) {
                viewState.setDatasetId(datasetId);
            }
            if (clearResponse) {
                viewState.setResponse(null, isPrimary);
            } else if (response != null) {
                viewState.setResponse(response, isPrimary);
            }
        }
    }

    private com.android.server.autofill.ViewState createOrUpdateViewStateLocked(android.view.autofill.AutofillId id, int state, android.view.autofill.AutofillValue value) {
        com.android.server.autofill.ViewState viewState = this.mViewStates.get(id);
        if (viewState != null) {
            viewState.setState(state);
        } else {
            viewState = new com.android.server.autofill.ViewState(id, this, state, this.mIsPrimaryCredential);
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "Adding autofillable view with id " + id + " and state " + state);
            }
            viewState.setCurrentValue(findValueLocked(id));
            this.mViewStates.put(id, viewState);
        }
        if ((state & 4) != 0) {
            viewState.setAutofilledValue(value);
        }
        return viewState;
    }

    void autoFill(int requestId, int datasetIndex, android.service.autofill.Dataset dataset, boolean generateEvent, int uiType) {
        android.content.Intent fillInIntent;
        if (com.android.server.autofill.Helper.sDebug) {
            android.util.Slog.d(TAG, "autoFill(): requestId=" + requestId + "; datasetIdx=" + datasetIndex + "; dataset=" + dataset);
        }
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#autoFill() rejected - session: " + this.id + " destroyed");
                return;
            }
            this.mPresentationStatsEventLogger.maybeSetSelectedDatasetId(datasetIndex);
            this.mPresentationStatsEventLogger.maybeSetSelectedDatasetPickReason(dataset.getEligibleReason());
            if (dataset.getAuthentication() == null) {
                if (generateEvent) {
                    this.mService.logDatasetSelected(dataset.getId(), this.id, this.mClientState, uiType);
                }
                if (this.mCurrentViewId != null) {
                    this.mInlineSessionController.hideInlineSuggestionsUiLocked(this.mCurrentViewId);
                }
                autoFillApp(dataset);
                return;
            }
            this.mService.logDatasetAuthenticationSelected(dataset.getId(), this.id, this.mClientState, uiType);
            this.mPresentationStatsEventLogger.maybeSetAuthenticationType(1);
            setViewStatesLocked(null, dataset, 64, false, true);
            if (dataset.getCredentialFillInIntent() != null && android.service.autofill.Flags.autofillCredmanIntegration()) {
                android.util.Slog.d(TAG, "Setting credential fill intent");
                fillInIntent = dataset.getCredentialFillInIntent();
            } else {
                fillInIntent = createAuthFillInIntentLocked(requestId, this.mClientState);
            }
            if (fillInIntent == null) {
                forceRemoveFromServiceLocked();
            } else {
                int authenticationId = android.view.autofill.AutofillManager.makeAuthenticationId(requestId, datasetIndex);
                startAuthentication(authenticationId, dataset.getAuthentication(), fillInIntent, false);
            }
        }
    }

    private android.content.Intent createAuthFillInIntentLocked(int requestId, android.os.Bundle extras) {
        android.content.Intent fillInIntent = new android.content.Intent();
        android.service.autofill.FillContext context = getFillContextByRequestIdLocked(requestId);
        if (context == null) {
            wtf(null, "createAuthFillInIntentLocked(): no FillContext. requestId=%d; mContexts=%s", java.lang.Integer.valueOf(requestId), this.mContexts);
            return null;
        }
        if (this.mLastInlineSuggestionsRequest != null && ((java.lang.Integer) this.mLastInlineSuggestionsRequest.first).intValue() == requestId) {
            fillInIntent.putExtra("android.view.autofill.extra.INLINE_SUGGESTIONS_REQUEST", (android.os.Parcelable) this.mLastInlineSuggestionsRequest.second);
        }
        fillInIntent.putExtra("android.view.autofill.extra.ASSIST_STRUCTURE", context.getStructure());
        fillInIntent.putExtra("android.view.autofill.extra.CLIENT_STATE", extras);
        return fillInIntent;
    }

    java.util.function.Consumer<android.view.inputmethod.InlineSuggestionsRequest> inlineSuggestionsRequestCacheDecorator(final java.util.function.Consumer<android.view.inputmethod.InlineSuggestionsRequest> consumer, final int requestId) {
        return new java.util.function.Consumer() { // from class: com.android.server.autofill.Session$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$inlineSuggestionsRequestCacheDecorator$1(consumer, requestId, (android.view.inputmethod.InlineSuggestionsRequest) obj);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$inlineSuggestionsRequestCacheDecorator$1(java.util.function.Consumer consumer, int requestId, android.view.inputmethod.InlineSuggestionsRequest inlineSuggestionsRequest) {
        consumer.accept(inlineSuggestionsRequest);
        synchronized (this.mLock) {
            this.mLastInlineSuggestionsRequest = android.util.Pair.create(java.lang.Integer.valueOf(requestId), inlineSuggestionsRequest);
        }
    }

    private int getDetectionPreferenceForLogging() {
        if (this.mService.isPccClassificationEnabled()) {
            if (this.mService.getMaster().preferProviderOverPcc()) {
                return 1;
            }
            return 2;
        }
        return 0;
    }

    private void startNewEventForPresentationStatsEventLogger() {
        synchronized (this.mLock) {
            this.mPresentationStatsEventLogger.startNewEvent();
            this.mPresentationStatsEventLogger.maybeSetDetectionPreference(getDetectionPreferenceForLogging());
            this.mPresentationStatsEventLogger.maybeSetAutofillServiceUid(getAutofillServiceUid());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startAuthentication(int authenticationId, android.content.IntentSender intent, android.content.Intent fillInIntent, boolean authenticateInline) {
        try {
            synchronized (this.mLock) {
                this.mClient.authenticate(this.id, authenticationId, intent, fillInIntent, authenticateInline);
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.e(TAG, "Error launching auth intent", e);
        }
    }

    static final class SaveResult {
        private boolean mLogSaveShown;
        private boolean mRemoveSession;
        private int mSaveDialogNotShowReason;

        SaveResult(boolean logSaveShown, boolean removeSession, int saveDialogNotShowReason) {
            this.mLogSaveShown = logSaveShown;
            this.mRemoveSession = removeSession;
            this.mSaveDialogNotShowReason = saveDialogNotShowReason;
        }

        public boolean isLogSaveShown() {
            return this.mLogSaveShown;
        }

        public void setLogSaveShown(boolean logSaveShown) {
            this.mLogSaveShown = logSaveShown;
        }

        public boolean isRemoveSession() {
            return this.mRemoveSession;
        }

        public void setRemoveSession(boolean removeSession) {
            this.mRemoveSession = removeSession;
        }

        public int getNoSaveUiReason() {
            return this.mSaveDialogNotShowReason;
        }

        public void setSaveDialogNotShowReason(int saveDialogNotShowReason) {
            this.mSaveDialogNotShowReason = saveDialogNotShowReason;
        }

        public java.lang.String toString() {
            return "SaveResult: [logSaveShown=" + this.mLogSaveShown + ", removeSession=" + this.mRemoveSession + ", saveDialogNotShowReason=" + this.mSaveDialogNotShowReason + "]";
        }
    }

    private static final class ClassificationState {
        private static final int STATE_INITIAL = 1;
        private static final int STATE_INVALIDATED = 5;
        private static final int STATE_PENDING_ASSIST_REQUEST = 2;
        private static final int STATE_PENDING_REQUEST = 3;
        private static final int STATE_RESPONSE = 4;
        private android.util.ArrayMap<android.view.autofill.AutofillId, java.util.Set<java.lang.String>> mClassificationCombinedHintsMap;
        private android.util.ArrayMap<android.view.autofill.AutofillId, java.util.Set<java.lang.String>> mClassificationGroupHintsMap;
        private android.util.ArrayMap<android.view.autofill.AutofillId, java.util.Set<java.lang.String>> mClassificationHintsMap;
        private android.util.ArrayMap<java.lang.String, java.util.Set<android.view.autofill.AutofillId>> mGroupHintsToAutofillIdMap;
        private android.util.ArrayMap<java.lang.String, java.util.Set<android.view.autofill.AutofillId>> mHintsToAutofillIdMap;
        private android.service.assist.classification.FieldClassificationResponse mLastFieldClassificationResponse;
        private android.service.assist.classification.FieldClassificationRequest mPendingFieldClassificationRequest;
        private int mState;

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @interface ClassificationRequestState {
        }

        private ClassificationState() {
            this.mState = 1;
        }

        private java.lang.String stateToString() {
            switch (this.mState) {
                case 1:
                    return "STATE_INITIAL";
                case 2:
                    return "STATE_PENDING_ASSIST_REQUEST";
                case 3:
                    return "STATE_PENDING_REQUEST";
                case 4:
                    return "STATE_RESPONSE";
                case 5:
                    return "STATE_INVALIDATED";
                default:
                    return "UNKNOWN_CLASSIFICATION_STATE_" + this.mState;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean processResponse() {
            if (this.mClassificationHintsMap != null && !this.mClassificationHintsMap.isEmpty()) {
                return true;
            }
            android.service.assist.classification.FieldClassificationResponse response = this.mLastFieldClassificationResponse;
            if (response == null) {
                return false;
            }
            this.mClassificationHintsMap = new android.util.ArrayMap<>();
            this.mClassificationGroupHintsMap = new android.util.ArrayMap<>();
            this.mHintsToAutofillIdMap = new android.util.ArrayMap<>();
            this.mGroupHintsToAutofillIdMap = new android.util.ArrayMap<>();
            this.mClassificationCombinedHintsMap = new android.util.ArrayMap<>();
            java.util.Set<android.service.assist.classification.FieldClassification> classifications = response.getClassifications();
            for (android.service.assist.classification.FieldClassification classification : classifications) {
                android.view.autofill.AutofillId id = classification.getAutofillId();
                java.util.Set<java.lang.String> hintDetections = classification.getHints();
                java.util.Set<java.lang.String> groupHintsDetections = classification.getGroupHints();
                android.util.ArraySet<java.lang.String> combinedHints = new android.util.ArraySet<>(hintDetections);
                this.mClassificationHintsMap.put(id, hintDetections);
                if (groupHintsDetections != null) {
                    this.mClassificationGroupHintsMap.put(id, groupHintsDetections);
                    combinedHints.addAll(groupHintsDetections);
                }
                this.mClassificationCombinedHintsMap.put(id, combinedHints);
                processDetections(hintDetections, id, this.mHintsToAutofillIdMap);
                processDetections(groupHintsDetections, id, this.mGroupHintsToAutofillIdMap);
            }
            return true;
        }

        private static void processDetections(java.util.Set<java.lang.String> detections, android.view.autofill.AutofillId id, android.util.ArrayMap<java.lang.String, java.util.Set<android.view.autofill.AutofillId>> currentMap) {
            java.util.Set<android.view.autofill.AutofillId> autofillIds;
            for (java.lang.String detection : detections) {
                if (currentMap.containsKey(detection)) {
                    autofillIds = currentMap.get(detection);
                } else {
                    autofillIds = new android.util.ArraySet<>();
                }
                autofillIds.add(id);
                currentMap.put(detection, autofillIds);
            }
        }

        private void invalidateState() {
            this.mState = 5;
        }

        private void updatePendingAssistData() {
            this.mState = 2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updatePendingRequest() {
            this.mState = 3;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void updateResponseReceived(android.service.assist.classification.FieldClassificationResponse response) {
            this.mState = 4;
            this.mLastFieldClassificationResponse = response;
            this.mPendingFieldClassificationRequest = null;
            processResponse();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onAssistStructureReceived(android.app.assist.AssistStructure structure) {
            this.mState = 3;
            this.mPendingFieldClassificationRequest = new android.service.assist.classification.FieldClassificationRequest(structure);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onFieldClassificationRequestSent() {
            this.mState = 3;
            this.mPendingFieldClassificationRequest = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean shouldTriggerRequest() {
            return this.mState == 1 || this.mState == 5;
        }

        public java.lang.String toString() {
            return "ClassificationState: [state=" + stateToString() + ", mPendingFieldClassificationRequest=" + this.mPendingFieldClassificationRequest + ", mLastFieldClassificationResponse=" + this.mLastFieldClassificationResponse + ", mClassificationHintsMap=" + this.mClassificationHintsMap + ", mClassificationGroupHintsMap=" + this.mClassificationGroupHintsMap + ", mHintsToAutofillIdMap=" + this.mHintsToAutofillIdMap + ", mGroupHintsToAutofillIdMap=" + this.mGroupHintsToAutofillIdMap + "]";
        }
    }

    public java.lang.String toString() {
        return "Session: [id=" + this.id + ", component=" + this.mComponentName + ", state=" + sessionStateAsString(this.mSessionState) + "]";
    }

    void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
        java.lang.String prefix2 = prefix + "  ";
        pw.print(prefix);
        pw.print("id: ");
        pw.println(this.id);
        pw.print(prefix);
        pw.print("uid: ");
        pw.println(this.uid);
        pw.print(prefix);
        pw.print("taskId: ");
        pw.println(this.taskId);
        pw.print(prefix);
        pw.print("flags: ");
        pw.println(this.mFlags);
        pw.print(prefix);
        pw.print("displayId: ");
        pw.println(this.mContext.getDisplayId());
        pw.print(prefix);
        pw.print("state: ");
        pw.println(sessionStateAsString(this.mSessionState));
        pw.print(prefix);
        pw.print("mComponentName: ");
        pw.println(this.mComponentName);
        pw.print(prefix);
        pw.print("mActivityToken: ");
        pw.println(this.mActivityToken);
        pw.print(prefix);
        pw.print("mStartTime: ");
        pw.println(this.mStartTime);
        pw.print(prefix);
        pw.print("Time to show UI: ");
        if (this.mUiShownTime == 0) {
            pw.println("N/A");
        } else {
            android.util.TimeUtils.formatDuration(this.mUiShownTime - this.mStartTime, pw);
            pw.println();
        }
        int requestLogsSizes = this.mRequestLogs.size();
        pw.print(prefix);
        pw.print("mSessionLogs: ");
        pw.println(requestLogsSizes);
        for (int i = 0; i < requestLogsSizes; i++) {
            int requestId = this.mRequestLogs.keyAt(i);
            android.metrics.LogMaker log = this.mRequestLogs.valueAt(i);
            pw.print(prefix2);
            pw.print('#');
            pw.print(i);
            pw.print(": req=");
            pw.print(requestId);
            pw.print(", log=");
            dumpRequestLog(pw, log);
            pw.println();
        }
        pw.print(prefix);
        pw.print("mResponses: ");
        if (this.mResponses == null) {
            pw.println("null");
        } else {
            pw.println(this.mResponses.size());
            for (int i2 = 0; i2 < this.mResponses.size(); i2++) {
                pw.print(prefix2);
                pw.print('#');
                pw.print(i2);
                pw.print(' ');
                pw.println(this.mResponses.valueAt(i2));
            }
        }
        pw.print(prefix);
        pw.print("mCurrentViewId: ");
        pw.println(this.mCurrentViewId);
        pw.print(prefix);
        pw.print("mDestroyed: ");
        pw.println(this.mDestroyed);
        pw.print(prefix);
        pw.print("mShowingSaveUi: ");
        pw.println(this.mSessionFlags.mShowingSaveUi);
        pw.print(prefix);
        pw.print("mPendingSaveUi: ");
        pw.println(this.mPendingSaveUi);
        int numberViews = this.mViewStates.size();
        pw.print(prefix);
        pw.print("mViewStates size: ");
        pw.println(this.mViewStates.size());
        for (int i3 = 0; i3 < numberViews; i3++) {
            pw.print(prefix);
            pw.print("ViewState at #");
            pw.println(i3);
            this.mViewStates.valueAt(i3).dump(prefix2, pw);
        }
        pw.print(prefix);
        pw.print("mContexts: ");
        if (this.mContexts != null) {
            int numContexts = this.mContexts.size();
            for (int i4 = 0; i4 < numContexts; i4++) {
                android.service.autofill.FillContext context = this.mContexts.get(i4);
                pw.print(prefix2);
                pw.print(context);
                if (com.android.server.autofill.Helper.sVerbose) {
                    pw.println("AssistStructure dumped at logcat)");
                    context.getStructure().dump(false);
                }
            }
        } else {
            pw.println("null");
        }
        pw.print(prefix);
        pw.print("mHasCallback: ");
        pw.println(this.mHasCallback);
        if (this.mClientState != null) {
            pw.print(prefix);
            pw.print("mClientState: ");
            pw.print(this.mClientState.getSize());
            pw.println(" bytes");
        }
        pw.print(prefix);
        pw.print("mCompatMode: ");
        pw.println(this.mCompatMode);
        pw.print(prefix);
        pw.print("mUrlBar: ");
        if (this.mUrlBar == null) {
            pw.println("N/A");
        } else {
            pw.print("id=");
            pw.print(this.mUrlBar.getAutofillId());
            pw.print(" domain=");
            pw.print(this.mUrlBar.getWebDomain());
            pw.print(" text=");
            com.android.server.autofill.Helper.printlnRedactedText(pw, this.mUrlBar.getText());
        }
        pw.print(prefix);
        pw.print("mSaveOnAllViewsInvisible: ");
        pw.println(this.mSaveOnAllViewsInvisible);
        pw.print(prefix);
        pw.print("mSelectedDatasetIds: ");
        pw.println(this.mSelectedDatasetIds);
        if (this.mSessionFlags.mAugmentedAutofillOnly) {
            pw.print(prefix);
            pw.println("For Augmented Autofill Only");
        }
        if (this.mSessionFlags.mFillDialogDisabled) {
            pw.print(prefix);
            pw.println("Fill Dialog disabled");
        }
        if (this.mLastFillDialogTriggerIds != null) {
            pw.print(prefix);
            pw.println("Last Fill Dialog trigger ids: ");
            pw.println(this.mSelectedDatasetIds);
        }
        if (this.mAugmentedAutofillDestroyer != null) {
            pw.print(prefix);
            pw.println("has mAugmentedAutofillDestroyer");
        }
        if (this.mAugmentedRequestsLogs != null) {
            pw.print(prefix);
            pw.print("number augmented requests: ");
            pw.println(this.mAugmentedRequestsLogs.size());
        }
        if (this.mAugmentedAutofillableIds != null) {
            pw.print(prefix);
            pw.print("mAugmentedAutofillableIds: ");
            pw.println(this.mAugmentedAutofillableIds);
        }
        if (this.mRemoteFillService != null) {
            this.mRemoteFillService.dump(prefix, pw);
        }
    }

    private static void dumpRequestLog(java.io.PrintWriter pw, android.metrics.LogMaker log) {
        pw.print("CAT=");
        pw.print(log.getCategory());
        pw.print(", TYPE=");
        int type = log.getType();
        switch (type) {
            case 2:
                pw.print("CLOSE");
                break;
            case 10:
                pw.print("SUCCESS");
                break;
            case 11:
                pw.print("FAILURE");
                break;
            default:
                pw.print("UNSUPPORTED");
                break;
        }
        pw.print('(');
        pw.print(type);
        pw.print(')');
        pw.print(", PKG=");
        pw.print(log.getPackageName());
        pw.print(", SERVICE=");
        pw.print(log.getTaggedData(908));
        pw.print(", ORDINAL=");
        pw.print(log.getTaggedData(1454));
        dumpNumericValue(pw, log, "FLAGS", 1452);
        dumpNumericValue(pw, log, "NUM_DATASETS", 909);
        dumpNumericValue(pw, log, "UI_LATENCY", 1145);
        int authStatus = com.android.server.autofill.Helper.getNumericValue(log, 1453);
        if (authStatus != 0) {
            pw.print(", AUTH_STATUS=");
            switch (authStatus) {
                case com.android.internal.util.FrameworkStatsLog.MEDIA_CODEC_RENDERED__RESOLUTION__RESOLUTION_720P_HD_ALMOST /* 912 */:
                    pw.print("AUTHENTICATED");
                    break;
                case 1126:
                    pw.print("DATASET_AUTHENTICATED");
                    break;
                case 1127:
                    pw.print("INVALID_DATASET_AUTHENTICATION");
                    break;
                case 1128:
                    pw.print("INVALID_AUTHENTICATION");
                    break;
                default:
                    pw.print("UNSUPPORTED");
                    break;
            }
            pw.print('(');
            pw.print(authStatus);
            pw.print(')');
        }
        dumpNumericValue(pw, log, "FC_IDS", 1271);
        dumpNumericValue(pw, log, "COMPAT_MODE", 1414);
    }

    private static void dumpNumericValue(java.io.PrintWriter pw, android.metrics.LogMaker log, java.lang.String field, int tag) {
        int value = com.android.server.autofill.Helper.getNumericValue(log, tag);
        if (value != 0) {
            pw.print(", ");
            pw.print(field);
            pw.print('=');
            pw.print(value);
        }
    }

    void sendCredentialManagerResponseToApp(android.credentials.GetCredentialResponse response, android.credentials.GetCredentialException exception, android.view.autofill.AutofillId viewId) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#sendCredentialManagerResponseToApp() rejected - session: " + this.id + " destroyed");
                return;
            }
            try {
                com.android.server.autofill.ViewState viewState = this.mViewStates.get(viewId);
                if (this.mService.getMaster().getIsFillFieldsFromCurrentSessionOnly() && viewState != null && viewState.id.getSessionId() != this.id && com.android.server.autofill.Helper.sVerbose) {
                    android.util.Slog.v(TAG, "Skipping sending credential response to view: " + viewId + " as it isn't part of the current session: " + this.id);
                }
                if (exception != null) {
                    if (viewId.isVirtualInt()) {
                        sendResponseToViewNode(viewId, null, exception);
                    } else {
                        this.mClient.onGetCredentialException(this.id, viewId, exception.getType(), exception.getMessage());
                    }
                } else if (response != null) {
                    if (viewId.isVirtualInt()) {
                        sendResponseToViewNode(viewId, response, null);
                    } else {
                        this.mClient.onGetCredentialResponse(this.id, viewId, response);
                    }
                } else {
                    android.util.Slog.w(TAG, "sendCredentialManagerResponseToApp called with null responseand exception");
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Error sending credential response to activity: " + e);
            }
        }
    }

    private void sendResponseToViewNode(android.view.autofill.AutofillId viewId, android.credentials.GetCredentialResponse response, android.credentials.GetCredentialException exception) {
        android.app.assist.AssistStructure.ViewNode viewNode = getViewNodeFromContextsLocked(viewId);
        if (viewNode != null && viewNode.getPendingCredentialCallback() != null) {
            android.os.Bundle resultData = new android.os.Bundle();
            if (response != null) {
                resultData.putParcelable("android.service.credentials.extra.GET_CREDENTIAL_RESPONSE", response);
                viewNode.getPendingCredentialCallback().send(0, resultData);
                return;
            } else {
                if (exception != null) {
                    resultData.putStringArray("android.service.credentials.extra.GET_CREDENTIAL_EXCEPTION", new java.lang.String[]{exception.getType(), exception.getMessage()});
                    viewNode.getPendingCredentialCallback().send(-1, resultData);
                    return;
                }
                return;
            }
        }
        android.util.Slog.w(TAG, "View node not found after GetCredentialResponse");
    }

    void autoFillApp(android.service.autofill.Dataset dataset) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                android.util.Slog.w(TAG, "Call to Session#autoFillApp() rejected - session: " + this.id + " destroyed");
                return;
            }
            try {
                int entryCount = dataset.getFieldIds().size();
                java.util.List<android.view.autofill.AutofillId> ids = new java.util.ArrayList<>(entryCount);
                java.util.List<android.view.autofill.AutofillValue> values = new java.util.ArrayList<>(entryCount);
                boolean waitingDatasetAuth = false;
                boolean hideHighlight = false;
                if (entryCount == 1 && ((android.view.autofill.AutofillId) dataset.getFieldIds().get(0)).equals(this.mCurrentViewId)) {
                    hideHighlight = true;
                }
                for (int i = 0; i < entryCount; i++) {
                    if (dataset.getFieldValues().get(i) != null) {
                        android.view.autofill.AutofillId viewId = (android.view.autofill.AutofillId) dataset.getFieldIds().get(i);
                        com.android.server.autofill.ViewState viewState = this.mViewStates.get(viewId);
                        if (this.mService.getMaster().getIsFillFieldsFromCurrentSessionOnly() && viewState != null && viewState.id.getSessionId() != this.id) {
                            if (com.android.server.autofill.Helper.sVerbose) {
                                android.util.Slog.v(TAG, "Skipping filling view: " + viewId + " as it isn't part of the current session: " + this.id);
                            }
                        } else {
                            ids.add(viewId);
                            values.add((android.view.autofill.AutofillValue) dataset.getFieldValues().get(i));
                            if (viewState != null && (viewState.getState() & 64) != 0) {
                                if (com.android.server.autofill.Helper.sVerbose) {
                                    android.util.Slog.v(TAG, "autofillApp(): view " + viewId + " waiting auth");
                                }
                                waitingDatasetAuth = true;
                                viewState.resetState(64);
                            }
                        }
                    }
                }
                if (!ids.isEmpty()) {
                    if (waitingDatasetAuth) {
                        this.mUi.hideFillUi(this);
                    }
                    if (com.android.server.autofill.Helper.sVerbose) {
                        android.util.Slog.v(TAG, "Total views to be autofilled: " + ids.size());
                    }
                    this.mPresentationStatsEventLogger.maybeSetViewFillablesAndCount(ids);
                    if (com.android.server.autofill.Helper.sDebug) {
                        android.util.Slog.d(TAG, "autoFillApp(): the buck is on the app: " + dataset);
                    }
                    this.mClient.autofill(this.id, ids, values, hideHighlight);
                    if (dataset.getId() != null) {
                        if (this.mSelectedDatasetIds == null) {
                            this.mSelectedDatasetIds = new java.util.ArrayList<>();
                        }
                        this.mSelectedDatasetIds.add(dataset.getId());
                    }
                    setViewStatesLocked(null, dataset, 4, false, true);
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Error autofilling activity: " + e);
            }
        }
    }

    private com.android.server.autofill.ui.AutoFillUI getUiForShowing() {
        com.android.server.autofill.ui.AutoFillUI autoFillUI;
        synchronized (this.mLock) {
            this.mUi.setCallback(this);
            autoFillUI = this.mUi;
        }
        return autoFillUI;
    }

    private void logAllEventsLocked(int val) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "logAllEvents(" + this.id + "): commitReason: " + val);
        }
        this.mSessionCommittedEventLogger.maybeSetCommitReason(val);
        this.mSessionCommittedEventLogger.maybeSetRequestCount(this.mRequestCount);
        this.mSessionCommittedEventLogger.maybeSetSessionDurationMillis(android.os.SystemClock.elapsedRealtime() - this.mStartTime);
        this.mFillRequestEventLogger.logAndEndEvent();
        this.mFillResponseEventLogger.logAndEndEvent();
        this.mPresentationStatsEventLogger.logAndEndEvent();
        this.mSaveEventLogger.logAndEndEvent();
        this.mSessionCommittedEventLogger.logAndEndEvent();
    }

    com.android.server.autofill.RemoteFillService destroyLocked() {
        com.android.server.autofill.IRemoteFillServiceExt remoteFillServiceExt;
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "destroyLocked for session: " + this.id);
        }
        logAllEventsLocked(5);
        if (this.mDestroyed) {
            return null;
        }
        clearPendingIntentLocked();
        unregisterDelayedFillBroadcastLocked();
        unlinkClientVultureLocked();
        this.mUi.destroyAll(this.mPendingSaveUi, this, true);
        this.mUi.clearCallback(this);
        if (this.mCurrentViewId != null) {
            this.mInlineSessionController.destroyLocked(this.mCurrentViewId);
        }
        com.android.server.autofill.RemoteInlineSuggestionRenderService remoteRenderService = this.mService.getRemoteInlineSuggestionRenderServiceLocked();
        if (remoteRenderService != null) {
            remoteRenderService.destroySuggestionViews(this.userId, this.id);
        }
        this.mDestroyed = true;
        if (this.mRemoteFillService != null && (remoteFillServiceExt = this.mRemoteFillService.getWrapper().getRemoteFillServiceExt()) != null) {
            remoteFillServiceExt.setSessionDestroyedLocked(this.mDestroyed);
        }
        int totalRequests = this.mRequestLogs.size();
        if (totalRequests > 0) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "destroyLocked(): logging " + totalRequests + " requests");
            }
            for (int i = 0; i < totalRequests; i++) {
                this.mMetricsLogger.write(this.mRequestLogs.valueAt(i));
            }
        }
        int totalAugmentedRequests = this.mAugmentedRequestsLogs == null ? 0 : this.mAugmentedRequestsLogs.size();
        if (totalAugmentedRequests > 0) {
            if (com.android.server.autofill.Helper.sVerbose) {
                android.util.Slog.v(TAG, "destroyLocked(): logging " + totalRequests + " augmented requests");
            }
            for (int i2 = 0; i2 < totalAugmentedRequests; i2++) {
                this.mMetricsLogger.write(this.mAugmentedRequestsLogs.get(i2));
            }
        }
        android.metrics.LogMaker log = newLogMaker(919).addTaggedData(1455, java.lang.Integer.valueOf(totalRequests));
        if (totalAugmentedRequests > 0) {
            log.addTaggedData(1631, java.lang.Integer.valueOf(totalAugmentedRequests));
        }
        if (this.mSessionFlags.mAugmentedAutofillOnly) {
            log.addTaggedData(1720, 1);
        }
        this.mMetricsLogger.write(log);
        return this.mRemoteFillService;
    }

    void forceRemoveFromServiceLocked() {
        forceRemoveFromServiceLocked(0);
    }

    void forceRemoveFromServiceIfForAugmentedOnlyLocked() {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "forceRemoveFromServiceIfForAugmentedOnlyLocked(" + this.id + "): " + this.mSessionFlags.mAugmentedAutofillOnly);
        }
        if (this.mSessionFlags.mAugmentedAutofillOnly) {
            forceRemoveFromServiceLocked();
        }
    }

    void forceRemoveFromServiceLocked(int clientState) {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "forceRemoveFromServiceLocked(): " + this.mPendingSaveUi);
        }
        boolean isPendingSaveUi = isSaveUiPendingLocked();
        this.mPendingSaveUi = null;
        removeFromServiceLocked();
        this.mUi.destroyAll(this.mPendingSaveUi, this, false);
        if (!isPendingSaveUi) {
            try {
                this.mClient.setSessionFinished(clientState, (java.util.List) null);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "Error notifying client to finish session", e);
            }
        }
        destroyAugmentedAutofillWindowsLocked();
    }

    void destroyAugmentedAutofillWindowsLocked() {
        if (this.mAugmentedAutofillDestroyer != null) {
            this.mAugmentedAutofillDestroyer.run();
            this.mAugmentedAutofillDestroyer = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFromService() {
        synchronized (this.mLock) {
            removeFromServiceLocked();
        }
    }

    void removeFromServiceLocked() {
        if (com.android.server.autofill.Helper.sVerbose) {
            android.util.Slog.v(TAG, "removeFromServiceLocked(" + this.id + "): " + this.mPendingSaveUi);
        }
        if (this.mDestroyed) {
            android.util.Slog.w(TAG, "Call to Session#removeFromServiceLocked() rejected - session: " + this.id + " destroyed");
            return;
        }
        if (isSaveUiPendingLocked()) {
            android.util.Slog.i(TAG, "removeFromServiceLocked() ignored, waiting for pending save ui");
            return;
        }
        com.android.server.autofill.RemoteFillService remoteFillService = destroyLocked();
        this.mService.removeSessionLocked(this.id);
        if (remoteFillService != null) {
            remoteFillService.destroy();
        }
        if (this.mSecondaryProviderHandler != null) {
            this.mSecondaryProviderHandler.destroy();
        }
        this.mSessionState = 3;
    }

    void onPendingSaveUi(int operation, android.os.IBinder token) {
        getUiForShowing().onPendingSaveUi(operation, token);
    }

    boolean isSaveUiPendingForTokenLocked(android.os.IBinder token) {
        return isSaveUiPendingLocked() && token.equals(this.mPendingSaveUi.getToken());
    }

    private boolean isSaveUiPendingLocked() {
        return this.mPendingSaveUi != null && this.mPendingSaveUi.getState() == 2;
    }

    private int getLastResponseIndexLocked() {
        if (this.mResponses == null || this.mResponses.size() == 0) {
            return -1;
        }
        java.util.List<java.lang.Integer> requestIdList = new java.util.ArrayList<>();
        int responseCount = this.mResponses.size();
        for (int i = 0; i < responseCount; i++) {
            requestIdList.add(java.lang.Integer.valueOf(this.mResponses.keyAt(i)));
        }
        int i2 = com.android.server.autofill.RequestId.getLastRequestIdIndex(requestIdList);
        return i2;
    }

    private android.metrics.LogMaker newLogMaker(int category) {
        return newLogMaker(category, this.mService.getServicePackageName());
    }

    private android.metrics.LogMaker newLogMaker(int category, java.lang.String servicePackageName) {
        return com.android.server.autofill.Helper.newLogMaker(category, this.mComponentName, servicePackageName, this.id, this.mCompatMode);
    }

    private void writeLog(int category) {
        this.mMetricsLogger.write(newLogMaker(category));
    }

    private void logAuthenticationStatusLocked(int requestId, int status) {
        addTaggedDataToRequestLogLocked(requestId, 1453, java.lang.Integer.valueOf(status));
    }

    private void addTaggedDataToRequestLogLocked(int requestId, int tag, java.lang.Object value) {
        android.metrics.LogMaker requestLog = this.mRequestLogs.get(requestId);
        if (requestLog == null) {
            android.util.Slog.w(TAG, "addTaggedDataToRequestLogLocked(tag=" + tag + "): no log for id " + requestId);
        } else {
            requestLog.addTaggedData(tag, value);
        }
    }

    private void logAugmentedAutofillRequestLocked(int mode, android.content.ComponentName augmentedRemoteServiceName, android.view.autofill.AutofillId focusedId, boolean isWhitelisted, java.lang.Boolean isInline) {
        java.lang.String historyItem = "aug:id=" + this.id + " u=" + this.uid + " m=" + mode + " a=" + android.content.ComponentName.flattenToShortString(this.mComponentName) + " f=" + focusedId + " s=" + augmentedRemoteServiceName + " w=" + isWhitelisted + " i=" + isInline;
        this.mService.getMaster().logRequestLocked(historyItem);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wtf(java.lang.Exception e, java.lang.String fmt, java.lang.Object... args) {
        java.lang.String message = java.lang.String.format(fmt, args);
        synchronized (this.mLock) {
            this.mWtfHistory.log(message);
        }
        if (e != null) {
            android.util.Slog.wtf(TAG, message, e);
        } else {
            android.util.Slog.wtf(TAG, message);
        }
    }

    private static java.lang.String actionAsString(int action) {
        switch (action) {
            case 1:
                return "START_SESSION";
            case 2:
                return "VIEW_ENTERED";
            case 3:
                return "VIEW_EXITED";
            case 4:
                return "VALUE_CHANGED";
            case 5:
                return "RESPONSE_EXPIRED";
            default:
                return "UNKNOWN_" + action;
        }
    }

    private static java.lang.String sessionStateAsString(int sessionState) {
        switch (sessionState) {
            case 0:
                return "STATE_UNKNOWN";
            case 1:
                return "STATE_ACTIVE";
            case 2:
                return "STATE_FINISHED";
            case 3:
                return "STATE_REMOVED";
            default:
                return "UNKNOWN_SESSION_STATE_" + sessionState;
        }
    }

    private int getAutofillServiceUid() {
        android.content.pm.ServiceInfo serviceInfo = this.mService.getServiceInfo();
        if (serviceInfo == null) {
            return -1;
        }
        return serviceInfo.applicationInfo.uid;
    }

    @Override // com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks
    public void onClassificationRequestSuccess(android.service.assist.classification.FieldClassificationResponse response) {
        this.mClassificationState.updateResponseReceived(response);
    }

    @Override // com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks
    public void onClassificationRequestFailure(int requestId, java.lang.CharSequence message) {
    }

    @Override // com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks
    public void onClassificationRequestTimeout(int requestId) {
    }

    @Override // com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks
    public void onServiceDied(com.android.server.autofill.RemoteFieldClassificationService service) {
        android.util.Slog.w(TAG, "removing session because service died");
        synchronized (this.mLock) {
        }
    }

    @Override // com.android.server.autofill.RemoteFieldClassificationService.FieldClassificationServiceCallbacks
    public void logFieldClassificationEvent(long startTime, android.service.assist.classification.FieldClassificationResponse response, int status) {
        com.android.server.autofill.FieldClassificationEventLogger logger = com.android.server.autofill.FieldClassificationEventLogger.createLogger();
        logger.startNewLogForRequest();
        logger.maybeSetLatencyMillis(android.os.SystemClock.elapsedRealtime() - startTime);
        logger.maybeSetAppPackageUid(this.uid);
        logger.maybeSetNextFillRequestId(this.mFillRequestIdSnapshot + 1);
        logger.maybeSetRequestId(sIdCounterForPcc.get());
        logger.maybeSetSessionId(this.id);
        int count = -1;
        if (response != null) {
            count = response.getClassifications().size();
        }
        logger.maybeSetRequestStatus(status);
        logger.maybeSetCountClassifications(count);
        logger.logAndEndEvent();
        this.mFillRequestIdSnapshot = -2;
    }
}
