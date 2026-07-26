package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class InputMethodBindingController {
    static final int IME_CONNECTION_BIND_FLAGS = 1082654725;
    static final int IME_VISIBLE_BIND_FLAGS = 738201601;
    private final com.android.server.inputmethod.AutofillSuggestionsController mAutofillController;
    private final android.content.Context mContext;
    private java.lang.String mCurId;
    private android.content.Intent mCurIntent;
    private com.android.server.inputmethod.IInputMethodInvoker mCurMethod;
    private int mCurMethodUid;
    private int mCurSeq;
    private android.os.IBinder mCurToken;
    private int mCurTokenDisplayId;
    private android.view.inputmethod.InputMethodSubtype mCurrentSubtype;
    private int mDeviceIdToShowIme;
    private int mDisplayIdToShowIme;
    private boolean mHasMainConnection;
    private final int mImeConnectionBindFlags;
    private long mLastBindTime;
    private java.util.concurrent.CountDownLatch mLatchForTesting;
    private final android.content.ServiceConnection mMainConnection;
    private final android.content.pm.PackageManagerInternal mPackageManagerInternal;
    private java.lang.String mSelectedMethodId;
    private final com.android.server.inputmethod.InputMethodManagerService mService;
    private boolean mSupportsConnectionlessStylusHw;
    private boolean mSupportsStylusHw;
    final int mUserId;
    private boolean mVisibleBound;
    private final android.content.ServiceConnection mVisibleConnection;
    private final com.android.server.wm.WindowManagerInternal mWindowManagerInternal;
    static boolean DEBUG = false;
    private static final java.lang.String TAG = com.android.server.inputmethod.InputMethodBindingController.class.getSimpleName();
    static long TIME_TO_RECONNECT = 3000;

    InputMethodBindingController(int userId, com.android.server.inputmethod.InputMethodManagerService service) {
        this(userId, service, IME_CONNECTION_BIND_FLAGS, null);
    }

    InputMethodBindingController(int userId, com.android.server.inputmethod.InputMethodManagerService service, int imeConnectionBindFlags, java.util.concurrent.CountDownLatch latchForTesting) {
        this.mCurMethodUid = -1;
        this.mCurTokenDisplayId = -1;
        this.mDisplayIdToShowIme = -1;
        this.mDeviceIdToShowIme = 0;
        this.mVisibleConnection = new android.content.ServiceConnection() { // from class: com.android.server.inputmethod.InputMethodBindingController.1
            @Override // android.content.ServiceConnection
            public void onBindingDied(android.content.ComponentName name) {
                synchronized (com.android.server.inputmethod.ImfLock.class) {
                    com.android.server.inputmethod.InputMethodBindingController.this.mAutofillController.invalidateAutofillSession();
                    if (com.android.server.inputmethod.InputMethodBindingController.this.isVisibleBound()) {
                        com.android.server.inputmethod.InputMethodBindingController.this.unbindVisibleConnection();
                    }
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service2) {
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(android.content.ComponentName name) {
                synchronized (com.android.server.inputmethod.ImfLock.class) {
                    com.android.server.inputmethod.InputMethodBindingController.this.mAutofillController.invalidateAutofillSession();
                }
            }
        };
        this.mMainConnection = new android.content.ServiceConnection() { // from class: com.android.server.inputmethod.InputMethodBindingController.2
            @Override // android.content.ServiceConnection
            public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service2) {
                android.os.Trace.traceBegin(32L, "IMMS.onServiceConnected");
                synchronized (com.android.server.inputmethod.ImfLock.class) {
                    if (com.android.server.inputmethod.InputMethodBindingController.this.mCurIntent != null && name.equals(com.android.server.inputmethod.InputMethodBindingController.this.mCurIntent.getComponent())) {
                        com.android.server.inputmethod.InputMethodBindingController.this.mCurMethod = com.android.server.inputmethod.IInputMethodInvoker.create(com.android.internal.inputmethod.IInputMethod.Stub.asInterface(service2));
                        updateCurrentMethodUid();
                        if (com.android.server.inputmethod.InputMethodBindingController.this.mCurToken == null) {
                            android.util.Slog.w(com.android.server.inputmethod.InputMethodBindingController.TAG, "Service connected without a token!");
                            com.android.server.inputmethod.InputMethodBindingController.this.unbindCurrentMethod();
                            android.os.Trace.traceEnd(32L);
                            return;
                        }
                        if (com.android.server.inputmethod.InputMethodBindingController.DEBUG) {
                            android.util.Slog.v(com.android.server.inputmethod.InputMethodBindingController.TAG, "Initiating attach with token: " + com.android.server.inputmethod.InputMethodBindingController.this.mCurToken);
                        }
                        android.view.inputmethod.InputMethodInfo info = com.android.server.inputmethod.InputMethodSettingsRepository.get(com.android.server.inputmethod.InputMethodBindingController.this.mUserId).getMethodMap().get(com.android.server.inputmethod.InputMethodBindingController.this.mSelectedMethodId);
                        boolean z = true;
                        boolean supportsStylusHwChanged = com.android.server.inputmethod.InputMethodBindingController.this.mSupportsStylusHw != info.supportsStylusHandwriting();
                        com.android.server.inputmethod.InputMethodBindingController.this.mSupportsStylusHw = info.supportsStylusHandwriting();
                        if (supportsStylusHwChanged) {
                            android.view.inputmethod.InputMethodManager.invalidateLocalStylusHandwritingAvailabilityCaches();
                        }
                        if (com.android.server.inputmethod.InputMethodBindingController.this.mSupportsConnectionlessStylusHw == info.supportsConnectionlessStylusHandwriting()) {
                            z = false;
                        }
                        boolean supportsConnectionlessStylusHwChanged = z;
                        if (supportsConnectionlessStylusHwChanged) {
                            com.android.server.inputmethod.InputMethodBindingController.this.mSupportsConnectionlessStylusHw = info.supportsConnectionlessStylusHandwriting();
                            android.view.inputmethod.InputMethodManager.invalidateLocalConnectionlessStylusHandwritingAvailabilityCaches();
                        }
                        com.android.server.inputmethod.InputMethodBindingController.this.mService.initializeImeLocked(com.android.server.inputmethod.InputMethodBindingController.this.mCurMethod, com.android.server.inputmethod.InputMethodBindingController.this.mCurToken);
                        com.android.server.inputmethod.InputMethodBindingController.this.mService.scheduleNotifyImeUidToAudioService(com.android.server.inputmethod.InputMethodBindingController.this.mCurMethodUid);
                        com.android.server.inputmethod.InputMethodBindingController.this.mService.reRequestCurrentClientSessionLocked();
                        com.android.server.inputmethod.InputMethodBindingController.this.mAutofillController.performOnCreateInlineSuggestionsRequest();
                    }
                    com.android.server.inputmethod.InputMethodBindingController.this.mService.scheduleResetStylusHandwriting();
                    android.os.Trace.traceEnd(32L);
                    if (com.android.server.inputmethod.InputMethodBindingController.this.mLatchForTesting != null) {
                        com.android.server.inputmethod.InputMethodBindingController.this.mLatchForTesting.countDown();
                    }
                }
            }

            private void updateCurrentMethodUid() {
                java.lang.String curMethodPackage = com.android.server.inputmethod.InputMethodBindingController.this.mCurIntent.getComponent().getPackageName();
                int curMethodUid = com.android.server.inputmethod.InputMethodBindingController.this.mPackageManagerInternal.getPackageUid(curMethodPackage, 0L, com.android.server.inputmethod.InputMethodBindingController.this.mUserId);
                if (curMethodUid < 0) {
                    android.util.Slog.e(com.android.server.inputmethod.InputMethodBindingController.TAG, "Failed to get UID for package=" + curMethodPackage);
                    com.android.server.inputmethod.InputMethodBindingController.this.mCurMethodUid = -1;
                } else {
                    com.android.server.inputmethod.InputMethodBindingController.this.mCurMethodUid = curMethodUid;
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(android.content.ComponentName name) {
                synchronized (com.android.server.inputmethod.ImfLock.class) {
                    if (com.android.server.inputmethod.InputMethodBindingController.DEBUG) {
                        android.util.Slog.v(com.android.server.inputmethod.InputMethodBindingController.TAG, "Service disconnected: " + name + " mCurIntent=" + com.android.server.inputmethod.InputMethodBindingController.this.mCurIntent);
                    }
                    if (com.android.server.inputmethod.InputMethodBindingController.this.mCurMethod != null && com.android.server.inputmethod.InputMethodBindingController.this.mCurIntent != null && name.equals(com.android.server.inputmethod.InputMethodBindingController.this.mCurIntent.getComponent())) {
                        com.android.server.inputmethod.InputMethodBindingController.this.mLastBindTime = android.os.SystemClock.uptimeMillis();
                        com.android.server.inputmethod.InputMethodBindingController.this.clearCurMethodAndSessions();
                        com.android.server.inputmethod.InputMethodBindingController.this.mService.clearInputShownLocked();
                        com.android.server.inputmethod.InputMethodBindingController.this.mService.unbindCurrentClientLocked(3);
                    }
                }
            }
        };
        this.mUserId = userId;
        this.mService = service;
        this.mContext = this.mService.mContext;
        this.mAutofillController = new com.android.server.inputmethod.AutofillSuggestionsController(this);
        this.mPackageManagerInternal = this.mService.mPackageManagerInternal;
        this.mWindowManagerInternal = this.mService.mWindowManagerInternal;
        this.mImeConnectionBindFlags = imeConnectionBindFlags;
        this.mLatchForTesting = latchForTesting;
    }

    long getLastBindTime() {
        return this.mLastBindTime;
    }

    boolean hasMainConnection() {
        return this.mHasMainConnection;
    }

    java.lang.String getCurId() {
        return this.mCurId;
    }

    java.lang.String getSelectedMethodId() {
        return this.mSelectedMethodId;
    }

    void setSelectedMethodId(java.lang.String selectedMethodId) {
        this.mSelectedMethodId = this.mService.getWrapper().getExtImpl().onSetSelectedMethodId(selectedMethodId);
    }

    android.view.inputmethod.InputMethodInfo getSelectedMethod() {
        return com.android.server.inputmethod.InputMethodSettingsRepository.get(this.mUserId).getMethodMap().get(this.mSelectedMethodId);
    }

    android.os.IBinder getCurToken() {
        return this.mCurToken;
    }

    android.view.inputmethod.InputMethodSubtype getCurrentSubtype() {
        return this.mCurrentSubtype;
    }

    void setCurrentSubtype(android.view.inputmethod.InputMethodSubtype currentSubtype) {
        this.mCurrentSubtype = currentSubtype;
    }

    int getCurTokenDisplayId() {
        return this.mCurTokenDisplayId;
    }

    android.content.Intent getCurIntent() {
        return this.mCurIntent;
    }

    int getSequenceNumber() {
        return this.mCurSeq;
    }

    void advanceSequenceNumber() {
        this.mCurSeq++;
        if (this.mCurSeq <= 0) {
            this.mCurSeq = 1;
        }
    }

    com.android.server.inputmethod.IInputMethodInvoker getCurMethod() {
        return this.mCurMethod;
    }

    int getCurMethodUid() {
        return this.mCurMethodUid;
    }

    boolean isVisibleBound() {
        return this.mVisibleBound;
    }

    boolean supportsStylusHandwriting() {
        return this.mSupportsStylusHw;
    }

    boolean supportsConnectionlessStylusHandwriting() {
        return this.mSupportsConnectionlessStylusHw;
    }

    void invalidateAutofillSession() {
        this.mAutofillController.invalidateAutofillSession();
    }

    void onCreateInlineSuggestionsRequest(com.android.internal.inputmethod.InlineSuggestionsRequestInfo requestInfo, com.android.internal.inputmethod.InlineSuggestionsRequestCallback callback, boolean touchExplorationEnabled) {
        this.mAutofillController.onCreateInlineSuggestionsRequest(requestInfo, callback, touchExplorationEnabled);
    }

    android.os.IBinder getCurHostInputToken() {
        return this.mAutofillController.getCurHostInputToken();
    }

    void unbindCurrentMethod() {
        if (isVisibleBound()) {
            unbindVisibleConnection();
        }
        if (hasMainConnection()) {
            unbindMainConnection();
        }
        if (getCurToken() != null) {
            removeCurrentToken();
            this.mService.resetSystemUiLocked();
            this.mAutofillController.onResetSystemUi();
        }
        this.mCurId = null;
        clearCurMethodAndSessions();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearCurMethodAndSessions() throws java.lang.Throwable {
        this.mService.clearClientSessionsLocked();
        this.mCurMethod = null;
        this.mCurMethodUid = -1;
    }

    private void removeCurrentToken() {
        if (DEBUG) {
            android.util.Slog.v(TAG, "Removing window token: " + this.mCurToken + " for display: " + this.mCurTokenDisplayId);
        }
        this.mWindowManagerInternal.removeWindowToken(this.mCurToken, true, false, this.mCurTokenDisplayId);
        this.mCurToken = null;
        this.mCurTokenDisplayId = -1;
    }

    com.android.internal.inputmethod.InputBindResult bindCurrentMethod() {
        if (this.mSelectedMethodId == null) {
            android.util.Slog.e(TAG, "mSelectedMethodId is null!");
            return com.android.internal.inputmethod.InputBindResult.NO_IME;
        }
        android.view.inputmethod.InputMethodInfo info = com.android.server.inputmethod.InputMethodSettingsRepository.get(this.mUserId).getMethodMap().get(this.mSelectedMethodId);
        if (info == null) {
            throw new java.lang.IllegalArgumentException("Unknown id: " + this.mSelectedMethodId);
        }
        this.mCurIntent = createImeBindingIntent(info.getComponent());
        if (bindCurrentInputMethodServiceMainConnection()) {
            this.mCurId = info.getId();
            this.mLastBindTime = android.os.SystemClock.uptimeMillis();
            this.mCurToken = new android.os.Binder();
            this.mCurTokenDisplayId = this.mDisplayIdToShowIme;
            if (DEBUG) {
                android.util.Slog.v(TAG, "Adding window token: " + this.mCurToken + " for display: " + this.mDisplayIdToShowIme);
            }
            this.mWindowManagerInternal.addWindowToken(this.mCurToken, 2011, this.mDisplayIdToShowIme, null);
            return new com.android.internal.inputmethod.InputBindResult(2, (com.android.internal.inputmethod.IInputMethodSession) null, (android.util.SparseArray) null, (android.view.InputChannel) null, this.mCurId, this.mCurSeq, false);
        }
        android.util.Slog.w("InputMethodManagerService", "Failure connecting to input method service: " + this.mCurIntent);
        this.mCurIntent = null;
        return com.android.internal.inputmethod.InputBindResult.IME_NOT_CONNECTED;
    }

    private android.content.Intent createImeBindingIntent(android.content.ComponentName component) {
        android.content.Intent intent = new android.content.Intent("android.view.InputMethod");
        intent.setComponent(component);
        intent.putExtra("android.intent.extra.client_label", android.R.string.indeterminate_progress_49);
        android.app.ActivityOptions options = android.app.ActivityOptions.makeBasic().setPendingIntentCreatorBackgroundActivityStartMode(2);
        intent.putExtra("android.intent.extra.client_intent", android.app.PendingIntent.getActivity(this.mContext, 0, new android.content.Intent("android.settings.INPUT_METHOD_SETTINGS"), 67108864, options.toBundle()));
        return intent;
    }

    private void unbindMainConnection() {
        this.mContext.unbindService(this.mMainConnection);
        this.mHasMainConnection = false;
    }

    void unbindVisibleConnection() {
        this.mContext.unbindService(this.mVisibleConnection);
        this.mVisibleBound = false;
    }

    private boolean bindCurrentInputMethodService(android.content.ServiceConnection conn, int flags) {
        if (this.mCurIntent == null || conn == null) {
            android.util.Slog.e(TAG, "--- bind failed: service = " + this.mCurIntent + ", conn = " + conn);
            return false;
        }
        return this.mContext.bindServiceAsUser(this.mCurIntent, conn, flags, new android.os.UserHandle(this.mUserId));
    }

    private boolean bindCurrentInputMethodServiceMainConnection() {
        this.mHasMainConnection = bindCurrentInputMethodService(this.mMainConnection, this.mImeConnectionBindFlags);
        if (this.mHasMainConnection) {
            this.mLastBindTime = android.os.SystemClock.uptimeMillis();
        }
        return this.mHasMainConnection;
    }

    void setCurrentMethodVisible() {
        if (this.mCurMethod != null) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "setCurrentMethodVisible: mCurToken=" + this.mCurToken);
            }
            if (hasMainConnection() && !isVisibleBound()) {
                this.mVisibleBound = bindCurrentInputMethodService(this.mVisibleConnection, IME_VISIBLE_BIND_FLAGS);
                return;
            }
            return;
        }
        if (!hasMainConnection()) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Cannot show input: no IME bound. Rebinding.");
            }
            bindCurrentMethod();
            return;
        }
        long bindingDuration = android.os.SystemClock.uptimeMillis() - this.mLastBindTime;
        if (bindingDuration >= TIME_TO_RECONNECT) {
            android.util.EventLog.writeEvent(com.android.server.EventLogTags.IMF_FORCE_RECONNECT_IME, getSelectedMethodId(), java.lang.Long.valueOf(bindingDuration), 1);
            android.util.Slog.w(TAG, "Force disconnect/connect to the IME in setCurrentMethodVisible()");
            unbindMainConnection();
            bindCurrentInputMethodServiceMainConnection();
            return;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Can't show input: connection = " + this.mHasMainConnection + ", time = " + (TIME_TO_RECONNECT - bindingDuration));
        }
    }

    void setCurrentMethodNotVisible() {
        if (isVisibleBound()) {
            unbindVisibleConnection();
        }
    }

    void setDisplayIdToShowIme(int displayId) {
        this.mDisplayIdToShowIme = displayId;
    }

    int getDisplayIdToShowIme() {
        return this.mDisplayIdToShowIme;
    }

    void setDeviceIdToShowIme(int deviceId) {
        this.mDeviceIdToShowIme = deviceId;
    }

    int getDeviceIdToShowIme() {
        return this.mDeviceIdToShowIme;
    }
}
