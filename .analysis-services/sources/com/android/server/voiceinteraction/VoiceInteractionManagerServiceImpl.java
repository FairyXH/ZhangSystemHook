package com.android.server.voiceinteraction;

/* JADX INFO: loaded from: classes3.dex */
class VoiceInteractionManagerServiceImpl implements com.android.server.voiceinteraction.VoiceInteractionSessionConnection.Callback {
    static final java.lang.String CLOSE_REASON_VOICE_INTERACTION = "voiceinteraction";
    static final boolean DEBUG = false;
    private static final long REQUEST_DIRECT_ACTIONS_RETRY_TIME_MS = 200;
    private static final boolean SYSPROP_VISUAL_QUERY_SERVICE_ENABLED = android.os.SystemProperties.getBoolean("ro.hotword.visual_query_service_enabled", false);
    static final java.lang.String TAG = "VoiceInteractionServiceManager";
    com.android.server.voiceinteraction.VoiceInteractionSessionConnection mActiveSession;
    final android.content.ComponentName mComponent;
    final android.content.Context mContext;
    int mDisabledShowContext;
    final android.os.Handler mHandler;
    final android.content.ComponentName mHotwordDetectionComponentName;
    volatile com.android.server.voiceinteraction.HotwordDetectionConnection mHotwordDetectionConnection;
    final android.view.IWindowManager mIWindowManager;
    final android.service.voice.VoiceInteractionServiceInfo mInfo;
    android.service.voice.IVoiceInteractionService mService;
    final com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub mServiceStub;
    final android.content.ComponentName mSessionComponentName;
    final int mUser;
    final boolean mValid;
    final android.content.ComponentName mVisualQueryDetectionComponentName;
    boolean mBound = false;
    final android.content.BroadcastReceiver mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                java.lang.String reason = intent.getStringExtra(com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_KEY);
                if (!com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.CLOSE_REASON_VOICE_INTERACTION.equals(reason) && !android.text.TextUtils.equals("dream", reason) && !com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                    synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mServiceStub) {
                        if (com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mActiveSession != null && com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mActiveSession.mSession != null) {
                            try {
                                if (!com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mActiveSession.mShown && com.android.server.policy.PhoneWindowManager.SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                                    android.util.Slog.e(com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.TAG, "no active session show when closing system dialogs");
                                    return;
                                }
                                com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mActiveSession.mSession.closeSystemDialogs();
                            } catch (android.os.RemoteException e) {
                            }
                        }
                    }
                }
            }
        }
    };
    final android.content.ServiceConnection mConnection = new android.content.ServiceConnection() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mServiceStub) {
                com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mService = android.service.voice.IVoiceInteractionService.Stub.asInterface(service);
                try {
                    com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mService.ready();
                } catch (android.os.RemoteException e) {
                }
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            synchronized (com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mServiceStub) {
                com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mService = null;
                com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.resetHotwordDetectionConnectionLocked();
            }
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(android.content.ComponentName name) {
            android.util.Slog.d(com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.TAG, "onBindingDied to " + name);
            java.lang.String packageName = name.getPackageName();
            android.content.pm.ParceledListSlice<android.app.ApplicationExitInfo> plistSlice = null;
            try {
                plistSlice = com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mAm.getHistoricalProcessExitReasons(packageName, 0, 1, com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mUser);
            } catch (android.os.RemoteException e) {
            }
            if (plistSlice == null) {
                return;
            }
            java.util.List<android.app.ApplicationExitInfo> list = plistSlice.getList();
            if (list.isEmpty()) {
                return;
            }
            android.app.ApplicationExitInfo info = list.get(0);
            if (info.getReason() == 10 && info.getSubReason() == 23) {
                com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mServiceStub.handleUserStop(packageName, com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mUser);
            }
        }
    };
    final java.util.ArrayList<com.android.internal.app.IVoiceInteractionAccessibilitySettingsListener> mAccessibilitySettingsListeners = new java.util.ArrayList<>();
    final android.os.Handler mDirectActionsHandler = new android.os.Handler(true);
    final android.app.IActivityManager mAm = android.app.ActivityManager.getService();
    final android.app.IActivityTaskManager mAtm = android.app.ActivityTaskManager.getService();
    final android.content.pm.PackageManagerInternal mPackageManagerInternal = (android.content.pm.PackageManagerInternal) java.util.Objects.requireNonNull((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class));

    interface DetectorRemoteExceptionListener {
        void onDetectorRemoteException(android.os.IBinder iBinder, int i);
    }

    VoiceInteractionManagerServiceImpl(android.content.Context context, android.os.Handler handler, com.android.server.voiceinteraction.VoiceInteractionManagerService.VoiceInteractionManagerServiceStub stub, int userHandle, android.content.ComponentName service) {
        android.content.ComponentName componentName;
        this.mContext = context;
        this.mHandler = handler;
        this.mServiceStub = stub;
        this.mUser = userHandle;
        this.mComponent = service;
        try {
            android.service.voice.VoiceInteractionServiceInfo info = new android.service.voice.VoiceInteractionServiceInfo(context.getPackageManager(), service, this.mUser);
            this.mInfo = info;
            if (this.mInfo.getParseError() != null) {
                android.util.Slog.w(TAG, "Bad voice interaction service: " + this.mInfo.getParseError());
                this.mSessionComponentName = null;
                this.mHotwordDetectionComponentName = null;
                this.mVisualQueryDetectionComponentName = null;
                this.mIWindowManager = null;
                this.mValid = false;
                return;
            }
            this.mValid = true;
            this.mSessionComponentName = new android.content.ComponentName(service.getPackageName(), this.mInfo.getSessionService());
            java.lang.String hotwordDetectionServiceName = this.mInfo.getHotwordDetectionService();
            if (hotwordDetectionServiceName == null) {
                componentName = null;
            } else {
                componentName = new android.content.ComponentName(service.getPackageName(), hotwordDetectionServiceName);
            }
            this.mHotwordDetectionComponentName = componentName;
            java.lang.String visualQueryDetectionServiceName = this.mInfo.getVisualQueryDetectionService();
            this.mVisualQueryDetectionComponentName = visualQueryDetectionServiceName != null ? new android.content.ComponentName(service.getPackageName(), visualQueryDetectionServiceName) : null;
            this.mIWindowManager = android.view.IWindowManager.Stub.asInterface(android.os.ServiceManager.getService("window"));
            android.content.IntentFilter filter = new android.content.IntentFilter();
            filter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
            this.mContext.registerReceiver(this.mBroadcastReceiver, filter, null, handler, 2);
            new com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.AccessibilitySettingsContentObserver().register(this.mContext.getContentResolver());
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.w(TAG, "Voice interaction service not found: " + service, e);
            this.mInfo = null;
            this.mSessionComponentName = null;
            this.mHotwordDetectionComponentName = null;
            this.mVisualQueryDetectionComponentName = null;
            this.mIWindowManager = null;
            this.mValid = false;
        }
    }

    public void grantImplicitAccessLocked(int grantRecipientUid, android.content.Intent intent) {
        int grantRecipientAppId = android.os.UserHandle.getAppId(grantRecipientUid);
        int grantRecipientUserId = android.os.UserHandle.getUserId(grantRecipientUid);
        int voiceInteractionUid = this.mInfo.getServiceInfo().applicationInfo.uid;
        this.mPackageManagerInternal.grantImplicitAccess(grantRecipientUserId, intent, grantRecipientAppId, voiceInteractionUid, true);
    }

    public boolean showSessionLocked(android.os.Bundle args, int flags, java.lang.String attributionTag, com.android.internal.app.IVoiceInteractionSessionShowCallback showCallback, android.os.IBinder activityToken) {
        java.util.List<com.android.server.wm.ActivityAssistInfo> visibleActivities;
        int sessionId = this.mServiceStub.getNextShowSessionId();
        android.os.Bundle newArgs = args == null ? new android.os.Bundle() : args;
        newArgs.putInt("android.service.voice.SHOW_SESSION_ID", sessionId);
        try {
            if (this.mService != null) {
                try {
                    this.mService.prepareToShowSession(newArgs, flags);
                } catch (android.os.RemoteException e) {
                    e = e;
                    android.util.Slog.w(TAG, "RemoteException while calling prepareToShowSession", e);
                }
            }
        } catch (android.os.RemoteException e2) {
            e = e2;
        }
        if (this.mActiveSession == null) {
            this.mActiveSession = new com.android.server.voiceinteraction.VoiceInteractionSessionConnection(this.mServiceStub, this.mSessionComponentName, this.mUser, this.mContext, this, this.mInfo.getServiceInfo().applicationInfo.uid, this.mHandler);
        }
        if (!this.mActiveSession.mBound) {
            try {
                if (this.mService != null) {
                    android.os.Bundle failedArgs = new android.os.Bundle();
                    failedArgs.putInt("android.service.voice.SHOW_SESSION_ID", sessionId);
                    this.mService.showSessionFailed(failedArgs);
                }
            } catch (android.os.RemoteException e3) {
                android.util.Slog.w(TAG, "RemoteException while calling showSessionFailed", e3);
            }
        }
        java.util.List<com.android.server.wm.ActivityAssistInfo> allVisibleActivities = ((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class)).getTopVisibleActivities();
        if (activityToken != null) {
            java.util.List<com.android.server.wm.ActivityAssistInfo> visibleActivities2 = new java.util.ArrayList<>();
            int activitiesCount = allVisibleActivities.size();
            int i = 0;
            while (true) {
                if (i >= activitiesCount) {
                    break;
                }
                com.android.server.wm.ActivityAssistInfo info = allVisibleActivities.get(i);
                if (info.getActivityToken() != activityToken) {
                    i++;
                } else {
                    visibleActivities2.add(info);
                    break;
                }
            }
            visibleActivities = visibleActivities2;
        } else {
            visibleActivities = allVisibleActivities;
        }
        return this.mActiveSession.showLocked(newArgs, flags, attributionTag, this.mDisabledShowContext, showCallback, visibleActivities);
    }

    public void getActiveServiceSupportedActions(java.util.List<java.lang.String> commands, com.android.internal.app.IVoiceActionCheckCallback callback) {
        if (this.mService == null) {
            android.util.Slog.w(TAG, "Not bound to voice interaction service " + this.mComponent);
            try {
                callback.onComplete((java.util.List) null);
            } catch (android.os.RemoteException e) {
            }
        } else {
            try {
                this.mService.getActiveServiceSupportedActions(commands, callback);
            } catch (android.os.RemoteException e2) {
                android.util.Slog.w(TAG, "RemoteException while calling getActiveServiceSupportedActions", e2);
            }
        }
    }

    public boolean hideSessionLocked() {
        if (this.mActiveSession != null) {
            return this.mActiveSession.hideLocked();
        }
        return false;
    }

    public boolean deliverNewSessionLocked(android.os.IBinder token, android.service.voice.IVoiceInteractionSession session, com.android.internal.app.IVoiceInteractor interactor) {
        if (this.mActiveSession == null || token != this.mActiveSession.mToken) {
            android.util.Slog.w(TAG, "deliverNewSession does not match active session");
            return false;
        }
        this.mActiveSession.deliverNewSessionLocked(session, interactor);
        return true;
    }

    public int startVoiceActivityLocked(java.lang.String callingFeatureId, int callingPid, int callingUid, android.os.IBinder token, android.content.Intent intent, java.lang.String resolvedType) {
        try {
        } catch (android.os.RemoteException e) {
            e = e;
        }
        try {
        } catch (android.os.RemoteException e2) {
            e = e2;
        }
        if (this.mActiveSession != null) {
            if (token == this.mActiveSession.mToken) {
                try {
                    if (!this.mActiveSession.mShown) {
                        try {
                            android.util.Slog.w(TAG, "startVoiceActivity not allowed on hidden session");
                            return -100;
                        } catch (android.os.RemoteException e3) {
                            e = e3;
                        }
                    } else {
                        android.content.Intent intent2 = new android.content.Intent(intent);
                        try {
                            intent2.addCategory("android.intent.category.VOICE");
                            intent2.addFlags(android.hardware.audio.common.V2_0.AudioFormat.MP2);
                            return this.mAtm.startVoiceActivity(this.mComponent.getPackageName(), callingFeatureId, callingPid, callingUid, intent2, resolvedType, this.mActiveSession.mSession, this.mActiveSession.mInteractor, 0, (android.app.ProfilerInfo) null, (android.os.Bundle) null, this.mUser);
                        } catch (android.os.RemoteException e4) {
                            e = e4;
                        }
                    }
                } catch (android.os.RemoteException e5) {
                    e = e5;
                }
                throw new java.lang.IllegalStateException("Unexpected remote error", e);
            }
        }
        android.util.Slog.w(TAG, "startVoiceActivity does not match active session");
        return -99;
    }

    public int startAssistantActivityLocked(java.lang.String callingFeatureId, int callingPid, int callingUid, android.os.IBinder token, android.content.Intent intent, java.lang.String resolvedType, android.os.Bundle bundle) {
        try {
            if (this.mActiveSession != null) {
                if (token == this.mActiveSession.mToken) {
                    try {
                        if (!this.mActiveSession.mShown) {
                            android.util.Slog.w(TAG, "startAssistantActivity not allowed on hidden session");
                            return -90;
                        }
                        try {
                            android.content.Intent intent2 = new android.content.Intent(intent);
                            try {
                                intent2.addFlags(268435456);
                                try {
                                    bundle.putInt("android.activity.activityType", 4);
                                    return this.mAtm.startAssistantActivity(this.mComponent.getPackageName(), callingFeatureId, callingPid, callingUid, intent2, resolvedType, bundle, this.mUser);
                                } catch (android.os.RemoteException e) {
                                    e = e;
                                    throw new java.lang.IllegalStateException("Unexpected remote error", e);
                                }
                            } catch (android.os.RemoteException e2) {
                                e = e2;
                            }
                        } catch (android.os.RemoteException e3) {
                            e = e3;
                            throw new java.lang.IllegalStateException("Unexpected remote error", e);
                        }
                    } catch (android.os.RemoteException e4) {
                        e = e4;
                        throw new java.lang.IllegalStateException("Unexpected remote error", e);
                    }
                }
            }
        } catch (android.os.RemoteException e5) {
            e = e5;
        }
        try {
            android.util.Slog.w(TAG, "startAssistantActivity does not match active session");
            return -89;
        } catch (android.os.RemoteException e6) {
            e = e6;
            throw new java.lang.IllegalStateException("Unexpected remote error", e);
        }
    }

    public void requestDirectActionsLocked(android.os.IBinder token, int taskId, android.os.IBinder assistToken, android.os.RemoteCallback cancellationCallback, android.os.RemoteCallback callback) {
        if (this.mActiveSession == null || token != this.mActiveSession.mToken) {
            android.util.Slog.w(TAG, "requestDirectActionsLocked does not match active session");
            callback.sendResult((android.os.Bundle) null);
            return;
        }
        com.android.server.wm.ActivityTaskManagerInternal.ActivityTokens tokens = ((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class)).getAttachedNonFinishingActivityForTask(taskId, null);
        if (tokens == null || tokens.getAssistToken() != assistToken) {
            android.util.Slog.w(TAG, "Unknown activity to query for direct actions");
            this.mDirectActionsHandler.sendMessageDelayed(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.HexConsumer() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl$$ExternalSyntheticLambda0
                public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3, java.lang.Object obj4, java.lang.Object obj5, java.lang.Object obj6) {
                    ((com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl) obj).retryRequestDirectActions((android.os.IBinder) obj2, ((java.lang.Integer) obj3).intValue(), (android.os.IBinder) obj4, (android.os.RemoteCallback) obj5, (android.os.RemoteCallback) obj6);
                }
            }, this, token, java.lang.Integer.valueOf(taskId), assistToken, cancellationCallback, callback), REQUEST_DIRECT_ACTIONS_RETRY_TIME_MS);
            return;
        }
        grantImplicitAccessLocked(tokens.getUid(), null);
        try {
            tokens.getApplicationThread().requestDirectActions(tokens.getActivityToken(), this.mActiveSession.mInteractor, cancellationCallback, callback);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w("Unexpected remote error", e);
            callback.sendResult((android.os.Bundle) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void retryRequestDirectActions(android.os.IBinder token, int taskId, android.os.IBinder assistToken, android.os.RemoteCallback cancellationCallback, android.os.RemoteCallback callback) {
        synchronized (this.mServiceStub) {
            if (this.mActiveSession != null && token == this.mActiveSession.mToken) {
                com.android.server.wm.ActivityTaskManagerInternal.ActivityTokens tokens = ((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class)).getAttachedNonFinishingActivityForTask(taskId, null);
                if (tokens == null || tokens.getAssistToken() != assistToken) {
                    android.util.Slog.w(TAG, "Unknown activity to query for direct actions during retrying");
                    callback.sendResult((android.os.Bundle) null);
                } else {
                    try {
                        tokens.getApplicationThread().requestDirectActions(tokens.getActivityToken(), this.mActiveSession.mInteractor, cancellationCallback, callback);
                    } catch (android.os.RemoteException e) {
                        android.util.Slog.w("Unexpected remote error", e);
                        callback.sendResult((android.os.Bundle) null);
                    }
                }
                return;
            }
            android.util.Slog.w(TAG, "retryRequestDirectActions does not match active session");
            callback.sendResult((android.os.Bundle) null);
        }
    }

    void performDirectActionLocked(android.os.IBinder token, java.lang.String actionId, android.os.Bundle arguments, int taskId, android.os.IBinder assistToken, android.os.RemoteCallback cancellationCallback, android.os.RemoteCallback resultCallback) {
        if (this.mActiveSession == null || token != this.mActiveSession.mToken) {
            android.util.Slog.w(TAG, "performDirectActionLocked does not match active session");
            resultCallback.sendResult((android.os.Bundle) null);
            return;
        }
        com.android.server.wm.ActivityTaskManagerInternal.ActivityTokens tokens = ((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class)).getAttachedNonFinishingActivityForTask(taskId, null);
        if (tokens != null && tokens.getAssistToken() == assistToken) {
            try {
                tokens.getApplicationThread().performDirectAction(tokens.getActivityToken(), actionId, arguments, cancellationCallback, resultCallback);
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w("Unexpected remote error", e);
                resultCallback.sendResult((android.os.Bundle) null);
                return;
            }
        }
        android.util.Slog.w(TAG, "Unknown activity to perform a direct action");
        resultCallback.sendResult((android.os.Bundle) null);
    }

    public void setKeepAwakeLocked(android.os.IBinder token, boolean keepAwake) {
        try {
            if (this.mActiveSession != null && token == this.mActiveSession.mToken) {
                this.mAtm.setVoiceKeepAwake(this.mActiveSession.mSession, keepAwake);
                return;
            }
            android.util.Slog.w(TAG, "setKeepAwake does not match active session");
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException("Unexpected remote error", e);
        }
    }

    public void closeSystemDialogsLocked(android.os.IBinder token) {
        try {
            if (this.mActiveSession != null && token == this.mActiveSession.mToken) {
                this.mAm.closeSystemDialogs(CLOSE_REASON_VOICE_INTERACTION);
                return;
            }
            android.util.Slog.w(TAG, "closeSystemDialogs does not match active session");
        } catch (android.os.RemoteException e) {
            throw new java.lang.IllegalStateException("Unexpected remote error", e);
        }
    }

    public void finishLocked(android.os.IBinder token, boolean finishTask) {
        if (this.mActiveSession == null || (!finishTask && token != this.mActiveSession.mToken)) {
            android.util.Slog.w(TAG, "finish does not match active session");
        } else {
            this.mActiveSession.cancelLocked(finishTask);
            this.mActiveSession = null;
        }
    }

    public void setDisabledShowContextLocked(int callingUid, int flags) {
        int activeUid = this.mInfo.getServiceInfo().applicationInfo.uid;
        if (callingUid != activeUid) {
            throw new java.lang.SecurityException("Calling uid " + callingUid + " does not match active uid " + activeUid);
        }
        this.mDisabledShowContext = flags;
    }

    public int getDisabledShowContextLocked(int callingUid) {
        int activeUid = this.mInfo.getServiceInfo().applicationInfo.uid;
        if (callingUid != activeUid) {
            throw new java.lang.SecurityException("Calling uid " + callingUid + " does not match active uid " + activeUid);
        }
        return this.mDisabledShowContext;
    }

    public int getUserDisabledShowContextLocked(int callingUid) {
        int activeUid = this.mInfo.getServiceInfo().applicationInfo.uid;
        if (callingUid != activeUid) {
            throw new java.lang.SecurityException("Calling uid " + callingUid + " does not match active uid " + activeUid);
        }
        if (this.mActiveSession != null) {
            return this.mActiveSession.getUserDisabledShowContextLocked();
        }
        return 0;
    }

    public boolean supportsLocalVoiceInteraction() {
        return this.mInfo.getSupportsLocalInteraction();
    }

    public android.content.pm.ApplicationInfo getApplicationInfo() {
        return this.mInfo.getServiceInfo().applicationInfo;
    }

    public void startListeningVisibleActivityChangedLocked(android.os.IBinder token) {
        if (this.mActiveSession == null || token != this.mActiveSession.mToken) {
            android.util.Slog.w(TAG, "startListeningVisibleActivityChangedLocked does not match active session");
        } else {
            this.mActiveSession.startListeningVisibleActivityChangedLocked();
        }
    }

    public void stopListeningVisibleActivityChangedLocked(android.os.IBinder token) {
        if (this.mActiveSession == null || token != this.mActiveSession.mToken) {
            android.util.Slog.w(TAG, "stopListeningVisibleActivityChangedLocked does not match active session");
        } else {
            this.mActiveSession.stopListeningVisibleActivityChangedLocked();
        }
    }

    public void notifyActivityDestroyedLocked(android.os.IBinder activityToken) {
        if (this.mActiveSession == null || !this.mActiveSession.mShown) {
            return;
        }
        this.mActiveSession.notifyActivityDestroyedLocked(activityToken);
    }

    public void notifyActivityEventChangedLocked(android.os.IBinder activityToken, int type) {
        if (this.mActiveSession == null || !this.mActiveSession.mShown) {
            return;
        }
        this.mActiveSession.notifyActivityEventChangedLocked(activityToken, type);
    }

    public void updateStateLocked(android.os.PersistableBundle options, android.os.SharedMemory sharedMemory, android.os.IBinder token) {
        android.util.Slog.v(TAG, "updateStateLocked");
        if (sharedMemory != null && !sharedMemory.setProtect(android.system.OsConstants.PROT_READ)) {
            android.util.Slog.w(TAG, "Can't set sharedMemory to be read-only");
            throw new java.lang.IllegalStateException("Can't set sharedMemory to be read-only");
        }
        if (this.mHotwordDetectionConnection == null) {
            android.util.Slog.w(TAG, "update State, but no hotword detection connection");
            throw new java.lang.IllegalStateException("Hotword detection connection not found");
        }
        synchronized (this.mHotwordDetectionConnection.mLock) {
            this.mHotwordDetectionConnection.updateStateLocked(options, sharedMemory, token);
        }
    }

    private void verifyDetectorForHotwordDetectionLocked(android.os.SharedMemory sharedMemory, com.android.internal.app.IHotwordRecognitionStatusCallback callback, int detectorType) {
        android.util.Slog.v(TAG, "verifyDetectorForHotwordDetectionLocked");
        int voiceInteractionServiceUid = this.mInfo.getServiceInfo().applicationInfo.uid;
        if (this.mHotwordDetectionComponentName == null) {
            android.util.Slog.w(TAG, "Hotword detection service name not found");
            logDetectorCreateEventIfNeeded(callback, detectorType, false, voiceInteractionServiceUid);
            throw new java.lang.IllegalStateException("Hotword detection service name not found");
        }
        android.content.pm.ServiceInfo hotwordDetectionServiceInfo = getServiceInfoLocked(this.mHotwordDetectionComponentName, this.mUser);
        if (hotwordDetectionServiceInfo == null) {
            android.util.Slog.w(TAG, "Hotword detection service info not found");
            logDetectorCreateEventIfNeeded(callback, detectorType, false, voiceInteractionServiceUid);
            throw new java.lang.IllegalStateException("Hotword detection service info not found");
        }
        if (!isIsolatedProcessLocked(hotwordDetectionServiceInfo)) {
            android.util.Slog.w(TAG, "Hotword detection service not in isolated process");
            logDetectorCreateEventIfNeeded(callback, detectorType, false, voiceInteractionServiceUid);
            throw new java.lang.IllegalStateException("Hotword detection service not in isolated process");
        }
        if (!"android.permission.BIND_HOTWORD_DETECTION_SERVICE".equals(hotwordDetectionServiceInfo.permission)) {
            android.util.Slog.w(TAG, "Hotword detection service does not require permission android.permission.BIND_HOTWORD_DETECTION_SERVICE");
            logDetectorCreateEventIfNeeded(callback, detectorType, false, voiceInteractionServiceUid);
            throw new java.lang.SecurityException("Hotword detection service does not require permission android.permission.BIND_HOTWORD_DETECTION_SERVICE");
        }
        if (this.mContext.getPackageManager().checkPermission("android.permission.BIND_HOTWORD_DETECTION_SERVICE", this.mInfo.getServiceInfo().packageName) == 0) {
            android.util.Slog.w(TAG, "Voice interaction service should not hold permission android.permission.BIND_HOTWORD_DETECTION_SERVICE");
            logDetectorCreateEventIfNeeded(callback, detectorType, false, voiceInteractionServiceUid);
            throw new java.lang.SecurityException("Voice interaction service should not hold permission android.permission.BIND_HOTWORD_DETECTION_SERVICE");
        }
        if (sharedMemory != null && !sharedMemory.setProtect(android.system.OsConstants.PROT_READ)) {
            android.util.Slog.w(TAG, "Can't set sharedMemory to be read-only");
            logDetectorCreateEventIfNeeded(callback, detectorType, false, voiceInteractionServiceUid);
            throw new java.lang.IllegalStateException("Can't set sharedMemory to be read-only");
        }
        logDetectorCreateEventIfNeeded(callback, detectorType, true, voiceInteractionServiceUid);
    }

    private void verifyDetectorForVisualQueryDetectionLocked(android.os.SharedMemory sharedMemory) {
        android.util.Slog.v(TAG, "verifyDetectorForVisualQueryDetectionLocked");
        if (this.mVisualQueryDetectionComponentName == null) {
            android.util.Slog.w(TAG, "Visual query detection service name not found");
            throw new java.lang.IllegalStateException("Visual query detection service name not found");
        }
        android.content.pm.ServiceInfo visualQueryDetectionServiceInfo = getServiceInfoLocked(this.mVisualQueryDetectionComponentName, this.mUser);
        if (visualQueryDetectionServiceInfo == null) {
            android.util.Slog.w(TAG, "Visual query detection service info not found");
            throw new java.lang.IllegalStateException("Visual query detection service name not found");
        }
        if (!isIsolatedProcessLocked(visualQueryDetectionServiceInfo)) {
            android.util.Slog.w(TAG, "Visual query detection service not in isolated process");
            throw new java.lang.IllegalStateException("Visual query detection not in isolated process");
        }
        if (!"android.permission.BIND_VISUAL_QUERY_DETECTION_SERVICE".equals(visualQueryDetectionServiceInfo.permission)) {
            android.util.Slog.w(TAG, "Visual query detection does not require permission android.permission.BIND_VISUAL_QUERY_DETECTION_SERVICE");
            throw new java.lang.SecurityException("Visual query detection does not require permission android.permission.BIND_VISUAL_QUERY_DETECTION_SERVICE");
        }
        if (this.mContext.getPackageManager().checkPermission("android.permission.BIND_VISUAL_QUERY_DETECTION_SERVICE", this.mInfo.getServiceInfo().packageName) == 0) {
            android.util.Slog.w(TAG, "Voice interaction service should not hold permission android.permission.BIND_VISUAL_QUERY_DETECTION_SERVICE");
            throw new java.lang.SecurityException("Voice interaction service should not hold permission android.permission.BIND_VISUAL_QUERY_DETECTION_SERVICE");
        }
        if (sharedMemory != null && !sharedMemory.setProtect(android.system.OsConstants.PROT_READ)) {
            android.util.Slog.w(TAG, "Can't set sharedMemory to be read-only");
            throw new java.lang.IllegalStateException("Can't set sharedMemory to be read-only");
        }
    }

    public void initAndVerifyDetectorLocked(android.media.permission.Identity voiceInteractorIdentity, android.os.PersistableBundle options, android.os.SharedMemory sharedMemory, android.os.IBinder token, com.android.internal.app.IHotwordRecognitionStatusCallback callback, int detectorType) {
        if (detectorType != 3) {
            verifyDetectorForHotwordDetectionLocked(sharedMemory, callback, detectorType);
        } else {
            verifyDetectorForVisualQueryDetectionLocked(sharedMemory);
        }
        if (SYSPROP_VISUAL_QUERY_SERVICE_ENABLED && !verifyProcessSharingLocked()) {
            android.util.Slog.w(TAG, "Sandboxed detection service not in shared isolated process");
            throw new java.lang.IllegalStateException("VisualQueryDetectionService or HotworDetectionService not in a shared isolated process. Please make sure to set android:allowSharedIsolatedProcess and android:isolatedProcess to be true and android:externalService to be false in the manifest file");
        }
        if (this.mHotwordDetectionConnection == null) {
            this.mHotwordDetectionConnection = new com.android.server.voiceinteraction.HotwordDetectionConnection(this.mServiceStub, this.mContext, this.mInfo.getServiceInfo().applicationInfo.uid, voiceInteractorIdentity, this.mHotwordDetectionComponentName, this.mVisualQueryDetectionComponentName, this.mUser, false, detectorType, new com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.DetectorRemoteExceptionListener() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl$$ExternalSyntheticLambda1
                @Override // com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.DetectorRemoteExceptionListener
                public final void onDetectorRemoteException(android.os.IBinder iBinder, int i) {
                    this.f$0.lambda$initAndVerifyDetectorLocked$0(iBinder, i);
                }
            });
            registerAccessibilityDetectionSettingsListenerLocked(this.mHotwordDetectionConnection.mAccessibilitySettingsListener);
        } else if (detectorType != 3) {
            this.mHotwordDetectionConnection.setDetectorType(detectorType);
        }
        this.mHotwordDetectionConnection.createDetectorLocked(options, sharedMemory, token, callback, detectorType);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$initAndVerifyDetectorLocked$0(android.os.IBinder token1, int detectorType1) {
        try {
            this.mService.detectorRemoteExceptionOccurred(token1, detectorType1);
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Fail to notify client detector remote exception occurred.");
        }
    }

    public void destroyDetectorLocked(android.os.IBinder token) {
        android.util.Slog.v(TAG, "destroyDetectorLocked");
        if (this.mHotwordDetectionConnection == null) {
            android.util.Slog.w(TAG, "destroy detector callback, but no hotword detection connection");
        } else {
            this.mHotwordDetectionConnection.destroyDetectorLocked(token);
        }
    }

    private void logDetectorCreateEventIfNeeded(com.android.internal.app.IHotwordRecognitionStatusCallback callback, int detectorType, boolean isCreated, int voiceInteractionServiceUid) {
        if (callback != null) {
            com.android.server.voiceinteraction.HotwordMetricsLogger.writeDetectorCreateEvent(detectorType, isCreated, voiceInteractionServiceUid);
        }
    }

    public void shutdownHotwordDetectionServiceLocked() {
        if (this.mHotwordDetectionConnection == null) {
            android.util.Slog.w(TAG, "shutdown, but no hotword detection connection");
            return;
        }
        this.mHotwordDetectionConnection.cancelLocked();
        unregisterAccessibilityDetectionSettingsListenerLocked(this.mHotwordDetectionConnection.mAccessibilitySettingsListener);
        this.mHotwordDetectionConnection = null;
    }

    public void setVisualQueryDetectionAttentionListenerLocked(com.android.internal.app.IVisualQueryDetectionAttentionListener listener) {
        if (this.mHotwordDetectionConnection == null) {
            return;
        }
        this.mHotwordDetectionConnection.setVisualQueryDetectionAttentionListenerLocked(listener);
    }

    public boolean startPerceivingLocked(android.service.voice.IVisualQueryDetectionVoiceInteractionCallback callback) {
        if (this.mHotwordDetectionConnection == null) {
            return false;
        }
        return this.mHotwordDetectionConnection.startPerceivingLocked(callback);
    }

    public boolean stopPerceivingLocked() {
        if (this.mHotwordDetectionConnection == null) {
            android.util.Slog.w(TAG, "stopPerceivingLocked() called but connection isn't established");
            return false;
        }
        return this.mHotwordDetectionConnection.stopPerceivingLocked();
    }

    public void startListeningFromMicLocked(android.media.AudioFormat audioFormat, android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback callback) {
        if (this.mHotwordDetectionConnection == null) {
            return;
        }
        this.mHotwordDetectionConnection.startListeningFromMicLocked(audioFormat, callback);
    }

    public void startListeningFromExternalSourceLocked(android.os.ParcelFileDescriptor audioStream, android.media.AudioFormat audioFormat, android.os.PersistableBundle options, android.os.IBinder token, android.service.voice.IMicrophoneHotwordDetectionVoiceInteractionCallback callback) {
        if (this.mHotwordDetectionConnection == null) {
            return;
        }
        if (audioStream == null) {
            android.util.Slog.w(TAG, "External source is null for hotword detector");
            throw new java.lang.IllegalStateException("External source is null for hotword detector");
        }
        this.mHotwordDetectionConnection.startListeningFromExternalSourceLocked(audioStream, audioFormat, options, token, callback);
    }

    public void startListeningFromWearableLocked(android.os.ParcelFileDescriptor audioStream, android.media.AudioFormat audioFormat, android.os.PersistableBundle options, android.service.voice.VoiceInteractionManagerInternal.WearableHotwordDetectionCallback callback) {
        if (this.mHotwordDetectionConnection == null) {
            callback.onError("Unable to start listening from wearable because the hotword detection connection is null.");
        } else {
            this.mHotwordDetectionConnection.startListeningFromWearableLocked(audioStream, audioFormat, options, callback);
        }
    }

    public void stopListeningFromMicLocked() {
        if (this.mHotwordDetectionConnection == null) {
            android.util.Slog.w(TAG, "stopListeningFromMicLocked() called but connection isn't established");
        } else {
            this.mHotwordDetectionConnection.stopListeningFromMicLocked();
        }
    }

    public void triggerHardwareRecognitionEventForTestLocked(android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent event, com.android.internal.app.IHotwordRecognitionStatusCallback callback) {
        if (this.mHotwordDetectionConnection == null) {
            android.util.Slog.w(TAG, "triggerHardwareRecognitionEventForTestLocked() called but connection isn't established");
        } else {
            this.mHotwordDetectionConnection.triggerHardwareRecognitionEventForTestLocked(event, callback);
        }
    }

    public android.hardware.soundtrigger.IRecognitionStatusCallback createSoundTriggerCallbackLocked(android.content.Context context, com.android.internal.app.IHotwordRecognitionStatusCallback callback, android.media.permission.Identity voiceInteractorIdentity) {
        return new com.android.server.voiceinteraction.HotwordDetectionConnection.SoundTriggerCallback(context, callback, this.mHotwordDetectionConnection, voiceInteractorIdentity);
    }

    private static android.content.pm.ServiceInfo getServiceInfoLocked(android.content.ComponentName componentName, int userHandle) {
        try {
            return android.app.AppGlobals.getPackageManager().getServiceInfo(componentName, 786560L, userHandle);
        } catch (android.os.RemoteException e) {
            return null;
        }
    }

    boolean isIsolatedProcessLocked(android.content.pm.ServiceInfo serviceInfo) {
        return (serviceInfo.flags & 2) != 0 && (serviceInfo.flags & 4) == 0;
    }

    boolean verifyProcessSharingLocked() {
        android.content.pm.ServiceInfo hotwordInfo = getServiceInfoLocked(this.mHotwordDetectionComponentName, this.mUser);
        android.content.pm.ServiceInfo visualQueryInfo = getServiceInfoLocked(this.mVisualQueryDetectionComponentName, this.mUser);
        if (hotwordInfo == null || visualQueryInfo == null) {
            return true;
        }
        return ((hotwordInfo.flags & 16) == 0 || (visualQueryInfo.flags & 16) == 0) ? false : true;
    }

    void forceRestartHotwordDetector() {
        if (this.mHotwordDetectionConnection == null) {
            android.util.Slog.w(TAG, "Failed to force-restart hotword detection: no hotword detection active");
        } else {
            this.mHotwordDetectionConnection.forceRestart();
        }
    }

    void setDebugHotwordLoggingLocked(boolean logging) {
        if (this.mHotwordDetectionConnection == null) {
            android.util.Slog.w(TAG, "Failed to set temporary debug logging: no hotword detection active");
        } else {
            this.mHotwordDetectionConnection.setDebugHotwordLoggingLocked(logging);
        }
    }

    void resetHotwordDetectionConnectionLocked() {
        if (this.mHotwordDetectionConnection == null) {
            return;
        }
        this.mHotwordDetectionConnection.cancelLocked();
        unregisterAccessibilityDetectionSettingsListenerLocked(this.mHotwordDetectionConnection.mAccessibilitySettingsListener);
        this.mHotwordDetectionConnection = null;
    }

    public void dumpLocked(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
        if (!this.mValid) {
            pw.print("  NOT VALID: ");
            if (this.mInfo == null) {
                pw.println("no info");
                return;
            } else {
                pw.println(this.mInfo.getParseError());
                return;
            }
        }
        pw.print("  mUser=");
        pw.println(this.mUser);
        pw.print("  mComponent=");
        pw.println(this.mComponent.flattenToShortString());
        pw.print("  Session service=");
        pw.println(this.mInfo.getSessionService());
        pw.println("  Service info:");
        this.mInfo.getServiceInfo().dump(new android.util.PrintWriterPrinter(pw), "    ");
        pw.print("  Recognition service=");
        pw.println(this.mInfo.getRecognitionService());
        pw.print("  Hotword detection service=");
        pw.println(this.mInfo.getHotwordDetectionService());
        pw.print("  Settings activity=");
        pw.println(this.mInfo.getSettingsActivity());
        pw.print("  Supports assist=");
        pw.println(this.mInfo.getSupportsAssist());
        pw.print("  Supports launch from keyguard=");
        pw.println(this.mInfo.getSupportsLaunchFromKeyguard());
        if (this.mDisabledShowContext != 0) {
            pw.print("  mDisabledShowContext=");
            pw.println(java.lang.Integer.toHexString(this.mDisabledShowContext));
        }
        pw.print("  mBound=");
        pw.print(this.mBound);
        pw.print(" mService=");
        pw.println(this.mService);
        if (this.mHotwordDetectionConnection != null) {
            pw.println("  Hotword detection connection:");
            this.mHotwordDetectionConnection.dump("    ", pw);
        } else {
            pw.println("  No Hotword detection connection");
        }
        if (this.mActiveSession != null) {
            pw.println("  Active session:");
            this.mActiveSession.dump("    ", pw);
        }
    }

    boolean getAccessibilityDetectionEnabled() {
        return android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "visual_query_accessibility_detection_enabled", 0, this.mUser) == 1;
    }

    void registerAccessibilityDetectionSettingsListenerLocked(com.android.internal.app.IVoiceInteractionAccessibilitySettingsListener listener) {
        this.mAccessibilitySettingsListeners.add(listener);
    }

    void unregisterAccessibilityDetectionSettingsListenerLocked(com.android.internal.app.IVoiceInteractionAccessibilitySettingsListener listener) {
        this.mAccessibilitySettingsListeners.remove(listener);
    }

    void startLocked() {
        android.content.Intent intent = new android.content.Intent("android.service.voice.VoiceInteractionService");
        intent.setComponent(this.mComponent);
        this.mBound = this.mContext.bindServiceAsUser(intent, this.mConnection, 68161537, new android.os.UserHandle(this.mUser));
        if (!this.mBound) {
            android.util.Slog.w(TAG, "Failed binding to voice interaction service " + this.mComponent);
        }
    }

    public void launchVoiceAssistFromKeyguard() {
        if (this.mService == null) {
            android.util.Slog.w(TAG, "Not bound to voice interaction service " + this.mComponent);
            return;
        }
        try {
            this.mService.launchVoiceAssistFromKeyguard();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "RemoteException while calling launchVoiceAssistFromKeyguard", e);
        }
    }

    void shutdownLocked() {
        if (this.mActiveSession != null) {
            this.mActiveSession.cancelLocked(false);
            this.mActiveSession = null;
        }
        try {
            if (this.mService != null) {
                this.mService.shutdown();
            }
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "RemoteException in shutdown", e);
        }
        if (this.mHotwordDetectionConnection != null) {
            this.mHotwordDetectionConnection.cancelLocked();
            unregisterAccessibilityDetectionSettingsListenerLocked(this.mHotwordDetectionConnection.mAccessibilitySettingsListener);
            this.mHotwordDetectionConnection = null;
        }
        if (this.mBound) {
            this.mContext.unbindService(this.mConnection);
            this.mBound = false;
        }
        if (this.mValid) {
            this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        }
    }

    void notifySoundModelsChangedLocked() {
        if (this.mService == null) {
            android.util.Slog.w(TAG, "Not bound to voice interaction service " + this.mComponent);
            return;
        }
        try {
            this.mService.soundModelsChanged();
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "RemoteException while calling soundModelsChanged", e);
        }
    }

    @Override // com.android.server.voiceinteraction.VoiceInteractionSessionConnection.Callback
    public void sessionConnectionGone(com.android.server.voiceinteraction.VoiceInteractionSessionConnection connection) {
        synchronized (this.mServiceStub) {
            finishLocked(connection.mToken, false);
        }
    }

    @Override // com.android.server.voiceinteraction.VoiceInteractionSessionConnection.Callback
    public void onSessionShown(com.android.server.voiceinteraction.VoiceInteractionSessionConnection connection) {
        this.mServiceStub.onSessionShown();
    }

    @Override // com.android.server.voiceinteraction.VoiceInteractionSessionConnection.Callback
    public void onSessionHidden(com.android.server.voiceinteraction.VoiceInteractionSessionConnection connection) {
        this.mServiceStub.onSessionHidden();
        this.mServiceStub.setSessionWindowVisible(connection.mToken, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class AccessibilitySettingsContentObserver extends android.database.ContentObserver {
        private android.net.Uri mAccessibilitySettingsEnabledUri;

        AccessibilitySettingsContentObserver() {
            super(null);
            this.mAccessibilitySettingsEnabledUri = android.provider.Settings.Secure.getUriFor("visual_query_accessibility_detection_enabled");
        }

        public void register(android.content.ContentResolver contentResolver) {
            contentResolver.registerContentObserver(this.mAccessibilitySettingsEnabledUri, false, this, -1);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            android.util.Slog.i(com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.TAG, "OnChange called with uri:" + uri);
            if (this.mAccessibilitySettingsEnabledUri.equals(uri)) {
                final boolean enable = android.provider.Settings.Secure.getIntForUser(com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mContext.getContentResolver(), "visual_query_accessibility_detection_enabled", 0, com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mUser) == 1;
                android.util.Slog.i(com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.TAG, "Notifying listeners with Accessibility setting set to " + enable);
                com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.this.mAccessibilitySettingsListeners.forEach(new java.util.function.Consumer() { // from class: com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl$AccessibilitySettingsContentObserver$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        com.android.server.voiceinteraction.VoiceInteractionManagerServiceImpl.AccessibilitySettingsContentObserver.lambda$onChange$0(enable, (com.android.internal.app.IVoiceInteractionAccessibilitySettingsListener) obj);
                    }
                });
            }
        }

        static /* synthetic */ void lambda$onChange$0(boolean enable, com.android.internal.app.IVoiceInteractionAccessibilitySettingsListener listener) {
            try {
                listener.onAccessibilityDetectionChanged(enable);
            } catch (android.os.RemoteException e) {
                e.rethrowFromSystemServer();
            }
        }
    }
}
