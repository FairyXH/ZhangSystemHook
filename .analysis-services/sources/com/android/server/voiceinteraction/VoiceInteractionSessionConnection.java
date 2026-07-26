package com.android.server.voiceinteraction;

/* JADX INFO: loaded from: classes3.dex */
final class VoiceInteractionSessionConnection implements android.content.ServiceConnection, com.android.server.am.AssistDataRequester.AssistDataRequesterCallbacks {
    static final int BOOST_TIMEOUT_MS = 300;
    static final boolean DEBUG = false;
    private static final int LOW_POWER_STANDBY_ALLOWLIST_TIMEOUT_MS = 120000;
    static final int MAX_POWER_BOOST_TIMEOUT = 10000;
    static final int POWER_BOOST_TIMEOUT_MS = java.lang.Integer.parseInt(java.lang.System.getProperty("vendor.powerhal.interaction.max", "200"));
    static final java.lang.String TAG = "VoiceInteractionServiceManager";
    final android.app.AppOpsManager mAppOps;
    com.android.server.am.AssistDataRequester mAssistDataRequester;
    final android.content.Intent mBindIntent;
    boolean mBound;
    final com.android.server.voiceinteraction.VoiceInteractionSessionConnection.Callback mCallback;
    final int mCallingUid;
    boolean mCanceled;
    final android.content.Context mContext;
    boolean mFullyBound;
    final android.os.Handler mHandler;
    com.android.internal.app.IVoiceInteractor mInteractor;
    private boolean mListeningVisibleActivity;
    final java.lang.Object mLock;
    private boolean mLowPowerStandbyAllowlisted;
    final android.os.IBinder mPermissionOwner;
    android.service.voice.IVoiceInteractionSessionService mService;
    android.service.voice.IVoiceInteractionSession mSession;
    final android.content.ComponentName mSessionComponentName;
    private com.android.server.voiceinteraction.VoiceInteractionSessionConnection.PowerBoostSetter mSetPowerBoostRunnable;
    android.os.Bundle mShowArgs;
    int mShowFlags;
    boolean mShown;
    final int mUser;
    final android.os.IBinder mToken = new android.os.Binder();
    java.util.ArrayList<com.android.internal.app.IVoiceInteractionSessionShowCallback> mPendingShowCallbacks = new java.util.ArrayList<>();
    private java.util.List<com.android.server.wm.ActivityAssistInfo> mPendingHandleAssistWithoutData = new java.util.ArrayList();
    private final java.util.concurrent.ScheduledExecutorService mScheduledExecutorService = java.util.concurrent.Executors.newSingleThreadScheduledExecutor();
    private final android.util.ArrayMap<android.os.IBinder, android.service.voice.VisibleActivityInfo> mVisibleActivityInfoForToken = new android.util.ArrayMap<>();
    private final java.lang.Runnable mRemoveFromLowPowerStandbyAllowlistRunnable = new java.lang.Runnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionSessionConnection$$ExternalSyntheticLambda1
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.removeFromLowPowerStandbyAllowlist();
        }
    };
    com.android.internal.app.IVoiceInteractionSessionShowCallback mShowCallback = new com.android.internal.app.IVoiceInteractionSessionShowCallback.Stub() { // from class: com.android.server.voiceinteraction.VoiceInteractionSessionConnection.1
        public void onFailed() throws android.os.RemoteException {
            synchronized (com.android.server.voiceinteraction.VoiceInteractionSessionConnection.this.mLock) {
                com.android.server.voiceinteraction.VoiceInteractionSessionConnection.this.notifyPendingShowCallbacksFailedLocked();
            }
        }

        public void onShown() throws android.os.RemoteException {
            synchronized (com.android.server.voiceinteraction.VoiceInteractionSessionConnection.this.mLock) {
                com.android.server.voiceinteraction.VoiceInteractionSessionConnection.this.notifyPendingShowCallbacksShownLocked();
            }
        }
    };
    final android.content.ServiceConnection mFullConnection = new android.content.ServiceConnection() { // from class: com.android.server.voiceinteraction.VoiceInteractionSessionConnection.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
        }
    };
    private java.lang.Runnable mShowAssistDisclosureRunnable = new java.lang.Runnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionSessionConnection.3
        @Override // java.lang.Runnable
        public void run() {
            com.android.server.statusbar.StatusBarManagerInternal statusBarInternal = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
            if (statusBarInternal != null) {
                statusBarInternal.showAssistDisclosure();
            }
        }
    };
    final android.app.IActivityTaskManager mActivityTaskManager = android.app.ActivityTaskManager.getService();
    final android.app.IActivityManager mAm = android.app.ActivityManager.getService();
    final com.android.server.uri.UriGrantsManagerInternal mUgmInternal = (com.android.server.uri.UriGrantsManagerInternal) com.android.server.LocalServices.getService(com.android.server.uri.UriGrantsManagerInternal.class);
    final android.view.IWindowManager mIWindowManager = android.view.IWindowManager.Stub.asInterface(android.os.ServiceManager.getService("window"));
    private final android.os.PowerManagerInternal mPowerManagerInternal = (android.os.PowerManagerInternal) com.android.server.LocalServices.getService(android.os.PowerManagerInternal.class);
    private final com.android.server.power.LowPowerStandbyControllerInternal mLowPowerStandbyControllerInternal = (com.android.server.power.LowPowerStandbyControllerInternal) com.android.server.LocalServices.getService(com.android.server.power.LowPowerStandbyControllerInternal.class);
    private final android.os.Handler mFgHandler = com.android.server.FgThread.getHandler();

    public interface Callback {
        void onSessionHidden(com.android.server.voiceinteraction.VoiceInteractionSessionConnection voiceInteractionSessionConnection);

        void onSessionShown(com.android.server.voiceinteraction.VoiceInteractionSessionConnection voiceInteractionSessionConnection);

        void sessionConnectionGone(com.android.server.voiceinteraction.VoiceInteractionSessionConnection voiceInteractionSessionConnection);
    }

    class PowerBoostSetter implements java.lang.Runnable {
        private boolean mCanceled;
        private final java.time.Instant mExpiryTime;

        PowerBoostSetter(java.time.Instant expiryTime) {
            this.mExpiryTime = expiryTime;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (com.android.server.voiceinteraction.VoiceInteractionSessionConnection.this.mLock) {
                if (this.mCanceled) {
                    return;
                }
                if (java.time.Instant.now().isBefore(this.mExpiryTime)) {
                    com.android.server.voiceinteraction.VoiceInteractionSessionConnection.this.mPowerManagerInternal.setPowerBoost(0, 300);
                    if (com.android.server.voiceinteraction.VoiceInteractionSessionConnection.this.mSetPowerBoostRunnable != null) {
                        com.android.server.voiceinteraction.VoiceInteractionSessionConnection.this.mFgHandler.postDelayed(com.android.server.voiceinteraction.VoiceInteractionSessionConnection.this.mSetPowerBoostRunnable, com.android.server.voiceinteraction.VoiceInteractionSessionConnection.POWER_BOOST_TIMEOUT_MS);
                    }
                } else {
                    android.util.Slog.w(com.android.server.voiceinteraction.VoiceInteractionSessionConnection.TAG, "Reset power boost INTERACTION because reaching max timeout.");
                    com.android.server.voiceinteraction.VoiceInteractionSessionConnection.this.mPowerManagerInternal.setPowerBoost(0, -1);
                }
            }
        }

        void cancel() {
            synchronized (com.android.server.voiceinteraction.VoiceInteractionSessionConnection.this.mLock) {
                this.mCanceled = true;
            }
        }
    }

    public VoiceInteractionSessionConnection(java.lang.Object lock, android.content.ComponentName component, int user, android.content.Context context, com.android.server.voiceinteraction.VoiceInteractionSessionConnection.Callback callback, int callingUid, android.os.Handler handler) {
        this.mLock = lock;
        this.mSessionComponentName = component;
        this.mUser = user;
        this.mContext = context;
        this.mCallback = callback;
        this.mCallingUid = callingUid;
        this.mHandler = handler;
        this.mAppOps = (android.app.AppOpsManager) context.getSystemService(android.app.AppOpsManager.class);
        this.mAssistDataRequester = new com.android.server.am.AssistDataRequester(this.mContext, this.mIWindowManager, (android.app.AppOpsManager) this.mContext.getSystemService("appops"), this, this.mLock, 49, 50);
        android.os.IBinder permOwner = this.mUgmInternal.newUriPermissionOwner("voicesession:" + component.flattenToShortString());
        this.mPermissionOwner = permOwner;
        this.mBindIntent = new android.content.Intent("android.service.voice.VoiceInteractionService");
        this.mBindIntent.setComponent(this.mSessionComponentName);
        this.mBound = this.mContext.bindServiceAsUser(this.mBindIntent, this, 1048625, new android.os.UserHandle(this.mUser));
        if (this.mBound) {
            try {
                this.mIWindowManager.addWindowToken(this.mToken, 2031, 0, (android.os.Bundle) null);
                return;
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Failed adding window token", e);
                return;
            }
        }
        android.util.Slog.w(TAG, "Failed binding to voice interaction session service " + this.mSessionComponentName);
    }

    public int getUserDisabledShowContextLocked() {
        int flags = 0;
        if (android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "assist_structure_enabled", 1, this.mUser) == 0) {
            flags = 0 | 1;
        }
        if (android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "assist_screenshot_enabled", 1, this.mUser) == 0) {
            return flags | 2;
        }
        return flags;
    }

    public boolean showLocked(android.os.Bundle args, int flags, java.lang.String attributionTag, int disabledContext, com.android.internal.app.IVoiceInteractionSessionShowCallback showCallback, java.util.List<com.android.server.wm.ActivityAssistInfo> topActivities) {
        boolean isAssistDataAllowed;
        if (!this.mBound) {
            if (showCallback != null) {
                try {
                    showCallback.onFailed();
                } catch (android.os.RemoteException e) {
                }
            }
            return false;
        }
        if (!this.mFullyBound) {
            this.mFullyBound = this.mContext.bindServiceAsUser(this.mBindIntent, this.mFullConnection, 404226049, new android.os.UserHandle(this.mUser));
        }
        this.mShown = true;
        this.mShowArgs = args;
        this.mShowFlags = flags;
        int disabledContext2 = disabledContext | getUserDisabledShowContextLocked();
        boolean fetchData = (flags & 1) != 0;
        boolean fetchScreenshot = (flags & 2) != 0;
        boolean assistDataRequestNeeded = fetchData || fetchScreenshot;
        if (assistDataRequestNeeded) {
            int topActivitiesCount = topActivities.size();
            java.util.ArrayList<android.os.IBinder> topActivitiesToken = new java.util.ArrayList<>(topActivitiesCount);
            for (int i = 0; i < topActivitiesCount; i++) {
                topActivitiesToken.add(topActivities.get(i).getActivityToken());
            }
            int i2 = disabledContext2 & 1;
            boolean fetchDataAllowed = i2 == 0;
            try {
                isAssistDataAllowed = this.mActivityTaskManager.isAssistDataAllowed();
            } catch (android.os.RemoteException e2) {
                isAssistDataAllowed = false;
            }
            if (fetchDataAllowed && isAssistDataAllowed) {
                java.util.ArrayList<android.content.ComponentName> topComponents = new java.util.ArrayList<>(topActivitiesCount);
                for (int i3 = 0; i3 < topActivitiesCount; i3++) {
                    topComponents.add(topActivities.get(i3).getComponentName());
                }
                this.mShowArgs.putParcelableArrayList("android.service.voice.FOREGROUND_ACTIVITIES", topComponents);
            }
            this.mAssistDataRequester.requestAssistData(topActivitiesToken, fetchData, fetchScreenshot, fetchDataAllowed, (disabledContext2 & 2) == 0, this.mCallingUid, this.mSessionComponentName.getPackageName(), attributionTag);
            boolean needDisclosure = this.mAssistDataRequester.getPendingDataCount() > 0 || this.mAssistDataRequester.getPendingScreenshotCount() > 0;
            if (needDisclosure && com.android.internal.app.AssistUtils.shouldDisclose(this.mContext, this.mSessionComponentName)) {
                this.mHandler.post(this.mShowAssistDisclosureRunnable);
            }
        }
        if (this.mSession != null) {
            try {
                this.mSession.show(this.mShowArgs, this.mShowFlags, showCallback);
                this.mShowArgs = null;
                this.mShowFlags = 0;
            } catch (android.os.RemoteException e3) {
            }
            if (assistDataRequestNeeded) {
                this.mAssistDataRequester.processPendingAssistData();
            } else {
                doHandleAssistWithoutData(topActivities);
            }
        } else {
            if (showCallback != null) {
                this.mPendingShowCallbacks.add(showCallback);
            }
            if (!assistDataRequestNeeded) {
                this.mPendingHandleAssistWithoutData = topActivities;
            }
        }
        if (this.mSetPowerBoostRunnable != null) {
            this.mSetPowerBoostRunnable.cancel();
        }
        this.mSetPowerBoostRunnable = new com.android.server.voiceinteraction.VoiceInteractionSessionConnection.PowerBoostSetter(java.time.Instant.now().plusMillis(10000L));
        this.mFgHandler.post(this.mSetPowerBoostRunnable);
        if (this.mLowPowerStandbyControllerInternal != null) {
            this.mLowPowerStandbyControllerInternal.addToAllowlist(this.mCallingUid, 1);
            this.mLowPowerStandbyAllowlisted = true;
            this.mFgHandler.removeCallbacks(this.mRemoveFromLowPowerStandbyAllowlistRunnable);
            this.mFgHandler.postDelayed(this.mRemoveFromLowPowerStandbyAllowlistRunnable, 120000L);
        }
        this.mCallback.onSessionShown(this);
        return true;
    }

    private void doHandleAssistWithoutData(java.util.List<com.android.server.wm.ActivityAssistInfo> topActivities) {
        int activityCount = topActivities.size();
        for (int i = 0; i < activityCount; i++) {
            com.android.server.wm.ActivityAssistInfo topActivity = topActivities.get(i);
            android.os.IBinder assistToken = topActivity.getAssistToken();
            int taskId = topActivity.getTaskId();
            int activityIndex = i;
            try {
                this.mSession.handleAssist(taskId, assistToken, (android.os.Bundle) null, (android.app.assist.AssistStructure) null, (android.app.assist.AssistContent) null, activityIndex, activityCount);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    @Override // com.android.server.am.AssistDataRequester.AssistDataRequesterCallbacks
    public boolean canHandleReceivedAssistDataLocked() {
        return this.mSession != null;
    }

    @Override // com.android.server.am.AssistDataRequester.AssistDataRequesterCallbacks
    public void onAssistDataReceivedLocked(android.os.Bundle data, int activityIndex, int activityCount) throws java.lang.Throwable {
        int uid;
        android.content.ClipData clipData;
        if (this.mSession == null) {
            return;
        }
        if (data == null) {
            try {
                this.mSession.handleAssist(-1, (android.os.IBinder) null, (android.os.Bundle) null, (android.app.assist.AssistStructure) null, (android.app.assist.AssistContent) null, 0, 0);
                return;
            } catch (android.os.RemoteException e) {
                return;
            }
        }
        int taskId = data.getInt(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_TASK_ID);
        android.os.IBinder activityId = data.getBinder(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_ACTIVITY_ID);
        android.os.Bundle assistData = data.getBundle("data");
        android.app.assist.AssistStructure structure = (android.app.assist.AssistStructure) data.getParcelable(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_STRUCTURE, android.app.assist.AssistStructure.class);
        android.app.assist.AssistContent content = (android.app.assist.AssistContent) data.getParcelable(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT, android.app.assist.AssistContent.class);
        if (assistData == null) {
            uid = -1;
        } else {
            int uid2 = assistData.getInt("android.intent.extra.ASSIST_UID", -1);
            uid = uid2;
        }
        if (uid >= 0 && content != null) {
            android.content.Intent intent = content.getIntent();
            if (intent != null && (clipData = intent.getClipData()) != null && android.content.Intent.isAccessUriMode(intent.getFlags())) {
                grantClipDataPermissions(clipData, intent.getFlags(), uid, this.mCallingUid, this.mSessionComponentName.getPackageName());
            }
            android.content.ClipData clipData2 = content.getClipData();
            if (clipData2 != null) {
                grantClipDataPermissions(clipData2, 1, uid, this.mCallingUid, this.mSessionComponentName.getPackageName());
            }
        }
        try {
            try {
                this.mSession.handleAssist(taskId, activityId, assistData, structure, content, activityIndex, activityCount);
            } catch (android.os.RemoteException e2) {
            }
        } catch (android.os.RemoteException e3) {
        }
    }

    @Override // com.android.server.am.AssistDataRequester.AssistDataRequesterCallbacks
    public void onAssistScreenshotReceivedLocked(android.graphics.Bitmap screenshot) {
        if (this.mSession == null) {
            return;
        }
        try {
            this.mSession.handleScreenshot(screenshot);
        } catch (android.os.RemoteException e) {
        }
    }

    void grantUriPermission(android.net.Uri uri, int mode, int srcUid, int destUid, java.lang.String destPkg) throws java.lang.Throwable {
        int sourceUserId;
        if (com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(uri.getScheme())) {
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                this.mUgmInternal.checkGrantUriPermission(srcUid, null, android.content.ContentProvider.getUriWithoutUserId(uri), mode, android.content.ContentProvider.getUserIdFromUri(uri, android.os.UserHandle.getUserId(srcUid)));
                sourceUserId = android.content.ContentProvider.getUserIdFromUri(uri, this.mUser);
            } catch (android.os.RemoteException e) {
            } catch (java.lang.SecurityException e2) {
                e = e2;
            } catch (java.lang.Throwable th) {
                th = th;
            }
            try {
                android.app.UriGrantsManager.getService().grantUriPermissionFromOwner(this.mPermissionOwner, srcUid, destPkg, android.content.ContentProvider.getUriWithoutUserId(uri), 1, sourceUserId, this.mUser);
                android.os.Binder.restoreCallingIdentity(ident);
            } catch (android.os.RemoteException e3) {
                android.os.Binder.restoreCallingIdentity(ident);
            } catch (java.lang.SecurityException e4) {
                e = e4;
                try {
                    android.util.Slog.w(TAG, "Can't propagate permission", e);
                    android.os.Binder.restoreCallingIdentity(ident);
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    android.os.Binder.restoreCallingIdentity(ident);
                    throw th;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
                android.os.Binder.restoreCallingIdentity(ident);
                throw th;
            }
        }
    }

    void grantClipDataItemPermission(android.content.ClipData.Item item, int mode, int srcUid, int destUid, java.lang.String destPkg) throws java.lang.Throwable {
        if (item.getUri() != null) {
            grantUriPermission(item.getUri(), mode, srcUid, destUid, destPkg);
        }
        android.content.Intent intent = item.getIntent();
        if (intent != null && intent.getData() != null) {
            grantUriPermission(intent.getData(), mode, srcUid, destUid, destPkg);
        }
    }

    void grantClipDataPermissions(android.content.ClipData data, int mode, int srcUid, int destUid, java.lang.String destPkg) throws java.lang.Throwable {
        int N = data.getItemCount();
        for (int i = 0; i < N; i++) {
            grantClipDataItemPermission(data.getItemAt(i), mode, srcUid, destUid, destPkg);
        }
    }

    public boolean hideLocked() {
        if (!this.mBound) {
            return false;
        }
        if (this.mShown) {
            this.mShown = false;
            this.mShowArgs = null;
            this.mShowFlags = 0;
            this.mAssistDataRequester.cancel();
            this.mPendingShowCallbacks.clear();
            if (this.mSession != null) {
                try {
                    this.mSession.hide();
                } catch (android.os.RemoteException e) {
                }
            }
            this.mUgmInternal.revokeUriPermissionFromOwner(this.mPermissionOwner, null, 3, this.mUser);
            if (this.mSession != null) {
                try {
                    android.app.ActivityTaskManager.getService().finishVoiceTask(this.mSession);
                } catch (android.os.RemoteException e2) {
                }
            }
            if (this.mSetPowerBoostRunnable != null) {
                this.mSetPowerBoostRunnable.cancel();
                this.mSetPowerBoostRunnable = null;
            }
            this.mPowerManagerInternal.setPowerBoost(0, -1);
            if (this.mLowPowerStandbyControllerInternal != null) {
                removeFromLowPowerStandbyAllowlist();
            }
            this.mCallback.onSessionHidden(this);
        }
        if (this.mFullyBound) {
            this.mContext.unbindService(this.mFullConnection);
            this.mFullyBound = false;
            return true;
        }
        return true;
    }

    public void cancelLocked(boolean finishTask) {
        this.mListeningVisibleActivity = false;
        this.mVisibleActivityInfoForToken.clear();
        hideLocked();
        this.mCanceled = true;
        if (this.mBound) {
            if (this.mSession != null) {
                try {
                    this.mSession.destroy();
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Voice interation session already dead");
                }
            }
            if (finishTask && this.mSession != null) {
                try {
                    android.app.ActivityTaskManager.getService().finishVoiceTask(this.mSession);
                } catch (android.os.RemoteException e2) {
                }
            }
            this.mContext.unbindService(this);
            try {
                this.mIWindowManager.removeWindowToken(this.mToken, 0);
            } catch (android.os.RemoteException e3) {
                android.util.Slog.w(TAG, "Failed removing window token", e3);
            }
            this.mBound = false;
            this.mService = null;
            this.mSession = null;
            this.mInteractor = null;
        }
        if (this.mFullyBound) {
            this.mContext.unbindService(this.mFullConnection);
            this.mFullyBound = false;
        }
    }

    public boolean deliverNewSessionLocked(android.service.voice.IVoiceInteractionSession session, com.android.internal.app.IVoiceInteractor interactor) {
        this.mSession = session;
        this.mInteractor = interactor;
        if (this.mShown) {
            try {
                session.show(this.mShowArgs, this.mShowFlags, this.mShowCallback);
                this.mShowArgs = null;
                this.mShowFlags = 0;
            } catch (android.os.RemoteException e) {
            }
            this.mAssistDataRequester.processPendingAssistData();
            if (!this.mPendingHandleAssistWithoutData.isEmpty()) {
                doHandleAssistWithoutData(this.mPendingHandleAssistWithoutData);
                this.mPendingHandleAssistWithoutData.clear();
                return true;
            }
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyPendingShowCallbacksShownLocked() {
        for (int i = 0; i < this.mPendingShowCallbacks.size(); i++) {
            try {
                this.mPendingShowCallbacks.get(i).onShown();
            } catch (android.os.RemoteException e) {
            }
        }
        this.mPendingShowCallbacks.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyPendingShowCallbacksFailedLocked() {
        for (int i = 0; i < this.mPendingShowCallbacks.size(); i++) {
            try {
                this.mPendingShowCallbacks.get(i).onFailed();
            } catch (android.os.RemoteException e) {
            }
        }
        this.mPendingShowCallbacks.clear();
    }

    void startListeningVisibleActivityChangedLocked() {
        if (!this.mShown || this.mCanceled || this.mSession == null) {
            return;
        }
        this.mListeningVisibleActivity = true;
        this.mVisibleActivityInfoForToken.clear();
        android.util.ArrayMap<android.os.IBinder, android.service.voice.VisibleActivityInfo> newVisibleActivityInfos = getTopVisibleActivityInfosLocked();
        if (newVisibleActivityInfos == null || newVisibleActivityInfos.isEmpty()) {
            return;
        }
        notifyVisibleActivitiesChangedLocked(newVisibleActivityInfos, 1);
        this.mVisibleActivityInfoForToken.putAll((android.util.ArrayMap<? extends android.os.IBinder, ? extends android.service.voice.VisibleActivityInfo>) newVisibleActivityInfos);
    }

    void stopListeningVisibleActivityChangedLocked() {
        this.mListeningVisibleActivity = false;
        this.mVisibleActivityInfoForToken.clear();
    }

    void notifyActivityEventChangedLocked(final android.os.IBinder activityToken, final int type) {
        if (!this.mListeningVisibleActivity) {
            return;
        }
        this.mScheduledExecutorService.execute(new java.lang.Runnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionSessionConnection$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyActivityEventChangedLocked$0(activityToken, type);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyActivityEventChangedLocked$0(android.os.IBinder activityToken, int type) {
        synchronized (this.mLock) {
            handleVisibleActivitiesLocked(activityToken, type);
        }
    }

    private android.util.ArrayMap<android.os.IBinder, android.service.voice.VisibleActivityInfo> getTopVisibleActivityInfosLocked() {
        java.util.List<com.android.server.wm.ActivityAssistInfo> allVisibleActivities = ((com.android.server.wm.ActivityTaskManagerInternal) com.android.server.LocalServices.getService(com.android.server.wm.ActivityTaskManagerInternal.class)).getTopVisibleActivities();
        if (allVisibleActivities.isEmpty()) {
            android.util.Slog.w(TAG, "no visible activity");
            return null;
        }
        int count = allVisibleActivities.size();
        android.util.ArrayMap<android.os.IBinder, android.service.voice.VisibleActivityInfo> visibleActivityInfoArrayMap = new android.util.ArrayMap<>(count);
        for (int i = 0; i < count; i++) {
            com.android.server.wm.ActivityAssistInfo info = allVisibleActivities.get(i);
            visibleActivityInfoArrayMap.put(info.getActivityToken(), new android.service.voice.VisibleActivityInfo(info.getTaskId(), info.getAssistToken()));
        }
        return visibleActivityInfoArrayMap;
    }

    private void handleVisibleActivitiesLocked(android.os.IBinder activityToken, int type) {
        android.service.voice.VisibleActivityInfo notifyVisibleActivityInfo;
        if (!this.mListeningVisibleActivity || !this.mShown || this.mCanceled || this.mSession == null) {
            return;
        }
        boolean notifyOnVisible = false;
        if (type == 1 || type == 2) {
            if (this.mVisibleActivityInfoForToken.containsKey(activityToken) || (notifyVisibleActivityInfo = getVisibleActivityInfoFromTopVisibleActivity(activityToken)) == null) {
                return;
            } else {
                notifyOnVisible = true;
            }
        } else if (type == 3) {
            if (getVisibleActivityInfoFromTopVisibleActivity(activityToken) != null || (notifyVisibleActivityInfo = this.mVisibleActivityInfoForToken.get(activityToken)) == null) {
                return;
            }
        } else if (type == 4) {
            notifyVisibleActivityInfo = this.mVisibleActivityInfoForToken.get(activityToken);
            if (notifyVisibleActivityInfo == null) {
                return;
            }
        } else {
            android.util.Slog.w(TAG, "notifyActivityEventChangedLocked unexpected type=" + type);
            return;
        }
        try {
            this.mSession.notifyVisibleActivityInfoChanged(notifyVisibleActivityInfo, notifyOnVisible ? 1 : 2);
        } catch (android.os.RemoteException e) {
        }
        if (notifyOnVisible) {
            this.mVisibleActivityInfoForToken.put(activityToken, notifyVisibleActivityInfo);
        } else {
            this.mVisibleActivityInfoForToken.remove(activityToken);
        }
    }

    private void notifyVisibleActivitiesChangedLocked(android.util.ArrayMap<android.os.IBinder, android.service.voice.VisibleActivityInfo> visibleActivityInfos, int type) {
        if (visibleActivityInfos == null || visibleActivityInfos.isEmpty() || this.mSession == null) {
            return;
        }
        for (int i = 0; i < visibleActivityInfos.size(); i++) {
            try {
                this.mSession.notifyVisibleActivityInfoChanged(visibleActivityInfos.valueAt(i), type);
            } catch (android.os.RemoteException e) {
                return;
            }
        }
    }

    private android.service.voice.VisibleActivityInfo getVisibleActivityInfoFromTopVisibleActivity(android.os.IBinder activityToken) {
        android.util.ArrayMap<android.os.IBinder, android.service.voice.VisibleActivityInfo> visibleActivityInfos = getTopVisibleActivityInfosLocked();
        if (visibleActivityInfos == null) {
            return null;
        }
        return visibleActivityInfos.get(activityToken);
    }

    void notifyActivityDestroyedLocked(final android.os.IBinder activityToken) {
        if (!this.mListeningVisibleActivity) {
            return;
        }
        this.mScheduledExecutorService.execute(new java.lang.Runnable() { // from class: com.android.server.voiceinteraction.VoiceInteractionSessionConnection$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$notifyActivityDestroyedLocked$1(activityToken);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyActivityDestroyedLocked$1(android.os.IBinder activityToken) {
        synchronized (this.mLock) {
            if (this.mListeningVisibleActivity) {
                if (this.mShown && !this.mCanceled && this.mSession != null) {
                    android.service.voice.VisibleActivityInfo visibleActivityInfo = this.mVisibleActivityInfoForToken.remove(activityToken);
                    if (visibleActivityInfo != null) {
                        try {
                            this.mSession.notifyVisibleActivityInfoChanged(visibleActivityInfo, 2);
                        } catch (android.os.RemoteException e) {
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFromLowPowerStandbyAllowlist() {
        synchronized (this.mLock) {
            if (this.mLowPowerStandbyAllowlisted) {
                this.mFgHandler.removeCallbacks(this.mRemoveFromLowPowerStandbyAllowlistRunnable);
                this.mLowPowerStandbyControllerInternal.removeFromAllowlist(this.mCallingUid, 1);
                this.mLowPowerStandbyAllowlisted = false;
            }
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
        synchronized (this.mLock) {
            this.mService = android.service.voice.IVoiceInteractionSessionService.Stub.asInterface(service);
            if (!this.mCanceled) {
                try {
                    this.mService.newSession(this.mToken, this.mShowArgs, this.mShowFlags);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.w(TAG, "Failed adding window token", e);
                }
            }
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(android.content.ComponentName name) {
        this.mCallback.sessionConnectionGone(this);
        synchronized (this.mLock) {
            this.mService = null;
        }
    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.print("mToken=");
        pw.println(this.mToken);
        pw.print(prefix);
        pw.print("mShown=");
        pw.println(this.mShown);
        pw.print(prefix);
        pw.print("mShowArgs=");
        pw.println(this.mShowArgs);
        pw.print(prefix);
        pw.print("mShowFlags=0x");
        pw.println(java.lang.Integer.toHexString(this.mShowFlags));
        pw.print(prefix);
        pw.print("mBound=");
        pw.println(this.mBound);
        if (this.mBound) {
            pw.print(prefix);
            pw.print("mService=");
            pw.println(this.mService);
            pw.print(prefix);
            pw.print("mSession=");
            pw.println(this.mSession);
            pw.print(prefix);
            pw.print("mInteractor=");
            pw.println(this.mInteractor);
        }
        this.mAssistDataRequester.dump(prefix, pw);
    }
}
