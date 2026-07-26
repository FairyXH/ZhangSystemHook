package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
public class MediaSessionRecord extends com.android.server.media.MediaSessionRecordImpl implements android.os.IBinder.DeathRecipient {
    private static final int OPTIMISTIC_VOLUME_TIMEOUT = 1000;
    private static final int TEMP_USER_ENGAGED_TIMEOUT_MS = 600000;
    static final long THROW_FOR_ACTIVITY_MEDIA_BUTTON_RECEIVER = 272737196;
    static final long THROW_FOR_INVALID_BROADCAST_RECEIVER = 270049379;
    private static final int USER_DISENGAGED = 2;
    private static final int USER_PERMANENTLY_ENGAGED = 0;
    private static final int USER_TEMPORARY_ENGAGED = 1;
    private android.media.AudioManager mAudioManager;
    private final android.content.Context mContext;
    private android.os.Bundle mExtras;
    private long mFlags;
    private final com.android.server.media.MediaSessionRecord.MessageHandler mHandler;
    private android.app.PendingIntent mLaunchIntent;
    private com.android.server.media.MediaButtonReceiverHolder mMediaButtonReceiverHolder;
    private android.media.MediaMetadata mMetadata;
    private java.lang.String mMetadataDescription;
    private final int mOwnerPid;
    private final int mOwnerUid;
    private final java.lang.String mPackageName;
    private android.media.session.PlaybackState mPlaybackState;
    private int mPolicies;
    private java.util.List<android.media.session.MediaSession.QueueItem> mQueue;
    private java.lang.CharSequence mQueueTitle;
    private int mRatingType;
    private final com.android.server.media.MediaSessionService mService;
    private final com.android.server.media.MediaSessionRecord.SessionCb mSessionCb;
    private final android.os.Bundle mSessionInfo;
    private final android.media.session.MediaSession.Token mSessionToken;
    private final java.lang.String mTag;
    private final int mUserId;
    private final boolean mVolumeAdjustmentForRemoteGroupSessions;
    private java.lang.String mVolumeControlId;
    private static final java.lang.String[] ART_URIS = {"android.media.metadata.ALBUM_ART_URI", "android.media.metadata.ART_URI", "android.media.metadata.DISPLAY_ICON_URI"};
    private static final java.lang.String TAG = "MediaSessionRecord";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final java.util.List<java.lang.Integer> ALWAYS_PRIORITY_STATES = java.util.Arrays.asList(4, 5, 9, 10);
    private static final java.util.List<java.lang.Integer> TRANSITION_PRIORITY_STATES = java.util.Arrays.asList(6, 8, 3);
    private static final android.media.AudioAttributes DEFAULT_ATTRIBUTES = new android.media.AudioAttributes.Builder().setUsage(1).build();
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.concurrent.CopyOnWriteArrayList<com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder> mControllerCallbackHolders = new java.util.concurrent.CopyOnWriteArrayList<>();
    private int mVolumeType = 1;
    private int mVolumeControlType = 2;
    private int mMaxVolume = 0;
    private int mCurrentVolume = 0;
    private int mOptimisticVolume = -1;
    private boolean mIsActive = false;
    private boolean mDestroyed = false;
    private long mDuration = -1;
    private final java.lang.Runnable mUserEngagementTimeoutExpirationRunnable = new java.lang.Runnable() { // from class: com.android.server.media.MediaSessionRecord$$ExternalSyntheticLambda3
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$0();
        }
    };
    private int mUserEngagementState = 2;
    private final java.lang.Runnable mClearOptimisticVolumeRunnable = new java.lang.Runnable() { // from class: com.android.server.media.MediaSessionRecord$$ExternalSyntheticLambda4
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.lambda$new$11();
        }
    };
    private final com.android.server.media.MediaSessionRecord.ControllerStub mController = new com.android.server.media.MediaSessionRecord.ControllerStub();
    private final com.android.server.media.MediaSessionRecord.SessionStub mSession = new com.android.server.media.MediaSessionRecord.SessionStub();
    private android.media.AudioAttributes mAudioAttrs = DEFAULT_ATTRIBUTES;
    private final com.android.server.uri.UriGrantsManagerInternal mUgmInternal = (com.android.server.uri.UriGrantsManagerInternal) com.android.server.LocalServices.getService(com.android.server.uri.UriGrantsManagerInternal.class);
    private final android.app.ForegroundServiceDelegationOptions mForegroundServiceDelegationOptions = createForegroundServiceDelegationOptions();

    /* JADX INFO: Access modifiers changed from: private */
    interface ControllerCallbackCall {
        void performOn(com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder iSessionControllerCallbackHolder) throws android.os.RemoteException;
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface UserEngagementState {
    }

    private static int getVolumeStream(android.media.AudioAttributes attr) {
        if (attr == null) {
            return DEFAULT_ATTRIBUTES.getVolumeControlStream();
        }
        int stream = attr.getVolumeControlStream();
        if (stream == Integer.MIN_VALUE) {
            return DEFAULT_ATTRIBUTES.getVolumeControlStream();
        }
        return stream;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        synchronized (this.mLock) {
            updateUserEngagedStateIfNeededLocked(true);
        }
    }

    public MediaSessionRecord(int ownerPid, int ownerUid, int userId, java.lang.String ownerPackageName, android.media.session.ISessionCallback cb, java.lang.String tag, android.os.Bundle sessionInfo, com.android.server.media.MediaSessionService service, android.os.Looper handlerLooper, int policies) throws android.os.RemoteException {
        this.mOwnerPid = ownerPid;
        this.mOwnerUid = ownerUid;
        this.mUserId = userId;
        this.mPackageName = ownerPackageName;
        this.mTag = tag;
        this.mSessionInfo = sessionInfo;
        this.mSessionToken = new android.media.session.MediaSession.Token(ownerUid, this.mController);
        this.mSessionCb = new com.android.server.media.MediaSessionRecord.SessionCb(cb);
        this.mService = service;
        this.mContext = this.mService.getContext();
        this.mHandler = new com.android.server.media.MediaSessionRecord.MessageHandler(handlerLooper);
        this.mAudioManager = (android.media.AudioManager) this.mContext.getSystemService("audio");
        this.mPolicies = policies;
        this.mVolumeAdjustmentForRemoteGroupSessions = this.mContext.getResources().getBoolean(android.R.bool.config_ui_enableFadingMarquee);
        this.mSessionCb.mCb.asBinder().linkToDeath(this, 0);
    }

    private android.app.ForegroundServiceDelegationOptions createForegroundServiceDelegationOptions() {
        return new android.app.ForegroundServiceDelegationOptions.Builder().setClientPid(this.mOwnerPid).setClientUid(getUid()).setClientPackageName(getPackageName()).setClientAppThread((android.app.IApplicationThread) null).setSticky(false).setClientInstanceName("MediaSessionFgsDelegate_" + getUid() + "_" + this.mOwnerPid + "_" + getPackageName()).setForegroundServiceTypes(0).setDelegationService(2).build();
    }

    public android.media.session.ISession getSessionBinder() {
        return this.mSession;
    }

    public android.media.session.MediaSession.Token getSessionToken() {
        return this.mSessionToken;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public java.lang.String getPackageName() {
        return this.mPackageName;
    }

    public com.android.server.media.MediaButtonReceiverHolder getMediaButtonReceiver() {
        return this.mMediaButtonReceiverHolder;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public int getUid() {
        return this.mOwnerUid;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public int getUserId() {
        return this.mUserId;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean isSystemPriority() {
        return (this.mFlags & 65536) != 0;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public void adjustVolume(java.lang.String packageName, java.lang.String opPackageName, int pid, int uid, boolean asSystemService, int direction, int flags, boolean useSuggested) {
        int previousFlagPlaySound = flags & 4;
        int flags2 = (checkPlaybackActiveState(true) || isSystemPriority()) ? flags & (-5) : flags;
        if (this.mVolumeType == 1) {
            int stream = getVolumeStream(this.mAudioAttrs);
            postAdjustLocalVolume(stream, direction, flags2, opPackageName, pid, uid, asSystemService, useSuggested, previousFlagPlaySound);
            return;
        }
        if (this.mVolumeControlType == 0) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Session does not support volume adjustment");
            }
        } else if (direction == 101 || direction == -100 || direction == 100) {
            android.util.Slog.w(TAG, "Muting remote playback is not supported");
        } else {
            if (DEBUG) {
                android.util.Slog.w(TAG, "adjusting volume, pkg=" + packageName + ", asSystemService=" + asSystemService + ", dir=" + direction);
            }
            this.mSessionCb.adjustVolume(packageName, pid, uid, asSystemService, direction);
            int volumeBefore = this.mOptimisticVolume < 0 ? this.mCurrentVolume : this.mOptimisticVolume;
            this.mOptimisticVolume = volumeBefore + direction;
            this.mOptimisticVolume = java.lang.Math.max(0, java.lang.Math.min(this.mOptimisticVolume, this.mMaxVolume));
            this.mHandler.removeCallbacks(this.mClearOptimisticVolumeRunnable);
            this.mHandler.postDelayed(this.mClearOptimisticVolumeRunnable, 1000L);
            if (volumeBefore != this.mOptimisticVolume) {
                pushVolumeUpdate();
            }
            if (DEBUG) {
                android.util.Slog.d(TAG, "Adjusted optimistic volume to " + this.mOptimisticVolume + " max is " + this.mMaxVolume);
            }
        }
        this.mService.notifyRemoteVolumeChanged(flags2, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVolumeTo(java.lang.String packageName, final java.lang.String opPackageName, final int pid, final int uid, final int value, final int flags) {
        if (this.mVolumeType == 1) {
            final int stream = getVolumeStream(this.mAudioAttrs);
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.MediaSessionRecord$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$setVolumeTo$1(opPackageName, pid, uid, flags, stream, value);
                }
            });
            return;
        }
        if (this.mVolumeControlType != 2) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Session does not support setting volume");
            }
        } else {
            int value2 = java.lang.Math.max(0, java.lang.Math.min(value, this.mMaxVolume));
            this.mSessionCb.setVolumeTo(packageName, pid, uid, value2);
            int volumeBefore = this.mOptimisticVolume < 0 ? this.mCurrentVolume : this.mOptimisticVolume;
            this.mOptimisticVolume = java.lang.Math.max(0, java.lang.Math.min(value2, this.mMaxVolume));
            this.mHandler.removeCallbacks(this.mClearOptimisticVolumeRunnable);
            this.mHandler.postDelayed(this.mClearOptimisticVolumeRunnable, 1000L);
            if (volumeBefore != this.mOptimisticVolume) {
                pushVolumeUpdate();
            }
            if (DEBUG) {
                android.util.Slog.d(TAG, "Set optimistic volume to " + this.mOptimisticVolume + " max is " + this.mMaxVolume);
            }
        }
        this.mService.notifyRemoteVolumeChanged(flags, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: setStreamVolumeForUid, reason: merged with bridge method [inline-methods] */
    public void lambda$setVolumeTo$1(java.lang.String opPackageName, int pid, int uid, int flags, int stream, int volumeValue) {
        try {
            this.mAudioManager.setStreamVolumeForUid(stream, volumeValue, flags, opPackageName, uid, pid, this.mContext.getApplicationInfo().targetSdkVersion);
        } catch (java.lang.IllegalArgumentException | java.lang.SecurityException e) {
            android.util.Slog.e(TAG, "Cannot set volume: stream=" + stream + ", value=" + volumeValue + ", flags=" + flags, e);
        }
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean isActive() {
        return this.mIsActive && !this.mDestroyed;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean checkPlaybackActiveState(boolean expected) {
        return this.mPlaybackState != null && this.mPlaybackState.isActive() == expected;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean isPlaybackTypeLocal() {
        return this.mVolumeType == 1;
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        this.mService.onSessionDied(this);
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public void close() {
        int callingUid = android.os.Binder.getCallingUid();
        int callingPid = android.os.Binder.getCallingPid();
        ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).logFgsApiEnd(4, callingUid, callingPid);
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return;
            }
            this.mSessionCb.mCb.asBinder().unlinkToDeath(this, 0);
            this.mDestroyed = true;
            this.mPlaybackState = null;
            updateUserEngagedStateIfNeededLocked(true);
            this.mHandler.post(9);
        }
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean isClosed() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mDestroyed;
        }
        return z;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public void expireTempEngaged() {
        this.mHandler.post(this.mUserEngagementTimeoutExpirationRunnable);
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean sendMediaButton(java.lang.String packageName, int pid, int uid, boolean asSystemService, android.view.KeyEvent ke, int sequenceId, android.os.ResultReceiver cb) {
        return this.mSessionCb.sendMediaButton(packageName, pid, uid, asSystemService, ke, sequenceId, cb);
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public boolean canHandleVolumeKey() {
        if (isPlaybackTypeLocal()) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Local MediaSessionRecord can handle volume key");
            }
            return true;
        }
        if (this.mVolumeControlType == 0) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Local MediaSessionRecord with FIXED volume control can't handle volume key");
            }
            return false;
        }
        if (this.mVolumeAdjustmentForRemoteGroupSessions) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Volume adjustment for remote group sessions allowed so MediaSessionRecord can handle volume key");
            }
            return true;
        }
        android.media.MediaRouter2Manager mRouter2Manager = android.media.MediaRouter2Manager.getInstance(this.mContext);
        java.util.List<android.media.RoutingSessionInfo> sessions = mRouter2Manager.getRoutingSessions(this.mPackageName);
        boolean foundNonSystemSession = false;
        boolean remoteSessionAllowVolumeAdjustment = true;
        if (DEBUG) {
            android.util.Slog.d(TAG, "Found " + sessions.size() + " routing sessions for package name " + this.mPackageName);
        }
        for (android.media.RoutingSessionInfo session : sessions) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Found routingSessionInfo: " + session);
            }
            if (!session.isSystemSession()) {
                foundNonSystemSession = true;
                if (session.getVolumeHandling() == 0) {
                    remoteSessionAllowVolumeAdjustment = false;
                }
            }
        }
        if (!foundNonSystemSession && DEBUG) {
            android.util.Slog.d(TAG, "Package " + this.mPackageName + " has a remote media session but no associated routing session");
        }
        return foundNonSystemSession && remoteSessionAllowVolumeAdjustment;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    boolean isLinkedToNotification(android.app.Notification notification) {
        return notification.isMediaNotification() && java.util.Objects.equals(notification.extras.getParcelable("android.mediaSession", android.media.session.MediaSession.Token.class), this.mSessionToken);
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public int getSessionPolicies() {
        int i;
        synchronized (this.mLock) {
            i = this.mPolicies;
        }
        return i;
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public void setSessionPolicies(int policies) {
        synchronized (this.mLock) {
            this.mPolicies = policies;
        }
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + this.mTag + " " + this);
        java.lang.String indent = prefix + "  ";
        pw.println(indent + "ownerPid=" + this.mOwnerPid + ", ownerUid=" + this.mOwnerUid + ", userId=" + this.mUserId);
        pw.println(indent + "package=" + this.mPackageName);
        pw.println(indent + "launchIntent=" + this.mLaunchIntent);
        pw.println(indent + "mediaButtonReceiver=" + this.mMediaButtonReceiverHolder);
        pw.println(indent + "active=" + this.mIsActive);
        pw.println(indent + "flags=" + this.mFlags);
        pw.println(indent + "rating type=" + this.mRatingType);
        pw.println(indent + "controllers: " + this.mControllerCallbackHolders.size());
        pw.println(indent + "state=" + (this.mPlaybackState == null ? null : this.mPlaybackState.toString()));
        pw.println(indent + "audioAttrs=" + this.mAudioAttrs);
        pw.append((java.lang.CharSequence) indent).append((java.lang.CharSequence) "volumeType=").append((java.lang.CharSequence) toVolumeTypeString(this.mVolumeType)).append((java.lang.CharSequence) ", controlType=").append((java.lang.CharSequence) toVolumeControlTypeString(this.mVolumeControlType)).append((java.lang.CharSequence) ", max=").append((java.lang.CharSequence) java.lang.Integer.toString(this.mMaxVolume)).append((java.lang.CharSequence) ", current=").append((java.lang.CharSequence) java.lang.Integer.toString(this.mCurrentVolume)).append((java.lang.CharSequence) ", volumeControlId=").append((java.lang.CharSequence) this.mVolumeControlId).println();
        pw.println(indent + "metadata: " + this.mMetadataDescription);
        pw.println(indent + "queueTitle=" + ((java.lang.Object) this.mQueueTitle) + ", size=" + (this.mQueue == null ? 0 : this.mQueue.size()));
    }

    private static java.lang.String toVolumeControlTypeString(int volumeControlType) {
        switch (volumeControlType) {
            case 0:
                return "FIXED";
            case 1:
                return "RELATIVE";
            case 2:
                return "ABSOLUTE";
            default:
                return android.text.TextUtils.formatSimple("unknown(%d)", new java.lang.Object[]{java.lang.Integer.valueOf(volumeControlType)});
        }
    }

    private static java.lang.String toVolumeTypeString(int volumeType) {
        switch (volumeType) {
            case 1:
                return "LOCAL";
            case 2:
                return "REMOTE";
            default:
                return android.text.TextUtils.formatSimple("unknown(%d)", new java.lang.Object[]{java.lang.Integer.valueOf(volumeType)});
        }
    }

    public java.lang.String toString() {
        return this.mPackageName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.mTag + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + getUniqueId() + " (userId=" + this.mUserId + ")";
    }

    @Override // com.android.server.media.MediaSessionRecordImpl
    public android.app.ForegroundServiceDelegationOptions getForegroundServiceDelegationOptions() {
        return this.mForegroundServiceDelegationOptions;
    }

    private void postAdjustLocalVolume(final int stream, final int direction, final int flags, java.lang.String callingOpPackageName, int callingPid, int callingUid, boolean asSystemService, final boolean useSuggested, final int previousFlagPlaySound) {
        java.lang.String opPackageName;
        int uid;
        int pid;
        if (DEBUG) {
            android.util.Slog.w(TAG, "adjusting local volume, stream=" + stream + ", dir=" + direction + ", asSystemService=" + asSystemService + ", useSuggested=" + useSuggested);
        }
        if (asSystemService) {
            java.lang.String opPackageName2 = this.mContext.getOpPackageName();
            opPackageName = opPackageName2;
            uid = 1000;
            pid = android.os.Process.myPid();
        } else {
            opPackageName = callingOpPackageName;
            uid = callingUid;
            pid = callingPid;
        }
        final java.lang.String str = opPackageName;
        final int i = uid;
        final int i2 = pid;
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.MediaSessionRecord$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$postAdjustLocalVolume$2(stream, direction, flags, useSuggested, previousFlagPlaySound, str, i, i2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: adjustSuggestedStreamVolumeForUid, reason: merged with bridge method [inline-methods] */
    public void lambda$postAdjustLocalVolume$2(int stream, int direction, int flags, boolean useSuggested, int previousFlagPlaySound, java.lang.String opPackageName, int uid, int pid) {
        try {
            if (useSuggested) {
                if (android.media.AudioSystem.isStreamActive(stream, 0)) {
                    this.mAudioManager.adjustSuggestedStreamVolumeForUid(stream, direction, flags, opPackageName, uid, pid, this.mContext.getApplicationInfo().targetSdkVersion);
                } else {
                    this.mAudioManager.adjustSuggestedStreamVolumeForUid(Integer.MIN_VALUE, direction, flags | previousFlagPlaySound, opPackageName, uid, pid, this.mContext.getApplicationInfo().targetSdkVersion);
                }
            } else {
                this.mAudioManager.adjustStreamVolumeForUid(stream, direction, flags, opPackageName, uid, pid, this.mContext.getApplicationInfo().targetSdkVersion);
            }
        } catch (java.lang.IllegalArgumentException | java.lang.SecurityException e) {
            android.util.Slog.e(TAG, "Cannot adjust volume: direction=" + direction + ", stream=" + stream + ", flags=" + flags + ", opPackageName=" + opPackageName + ", uid=" + uid + ", useSuggested=" + useSuggested + ", previousFlagPlaySound=" + previousFlagPlaySound, e);
        }
    }

    private void logCallbackException(java.lang.String msg, com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder holder, java.lang.Exception e) {
        android.util.Slog.v(TAG, msg + ", this=" + this + ", callback package=" + holder.mPackageName + ", exception=" + e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pushPlaybackStateUpdate() {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return;
            }
            final android.media.session.PlaybackState playbackState = this.mPlaybackState;
            performOnCallbackHolders("pushPlaybackStateUpdate", new com.android.server.media.MediaSessionRecord.ControllerCallbackCall() { // from class: com.android.server.media.MediaSessionRecord$$ExternalSyntheticLambda5
                @Override // com.android.server.media.MediaSessionRecord.ControllerCallbackCall
                public final void performOn(com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder iSessionControllerCallbackHolder) {
                    iSessionControllerCallbackHolder.mCallback.onPlaybackStateChanged(playbackState);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pushMetadataUpdate() {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return;
            }
            final android.media.MediaMetadata metadata = this.mMetadata;
            performOnCallbackHolders("pushMetadataUpdate", new com.android.server.media.MediaSessionRecord.ControllerCallbackCall() { // from class: com.android.server.media.MediaSessionRecord$$ExternalSyntheticLambda9
                @Override // com.android.server.media.MediaSessionRecord.ControllerCallbackCall
                public final void performOn(com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder iSessionControllerCallbackHolder) {
                    iSessionControllerCallbackHolder.mCallback.onMetadataChanged(metadata);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pushQueueUpdate() {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return;
            }
            final java.util.ArrayList<android.media.session.MediaSession.QueueItem> toSend = this.mQueue == null ? null : new java.util.ArrayList<>(this.mQueue);
            performOnCallbackHolders("pushQueueUpdate", new com.android.server.media.MediaSessionRecord.ControllerCallbackCall() { // from class: com.android.server.media.MediaSessionRecord$$ExternalSyntheticLambda11
                @Override // com.android.server.media.MediaSessionRecord.ControllerCallbackCall
                public final void performOn(com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder iSessionControllerCallbackHolder) throws android.os.RemoteException {
                    com.android.server.media.MediaSessionRecord.lambda$pushQueueUpdate$5(toSend, iSessionControllerCallbackHolder);
                }
            });
        }
    }

    static /* synthetic */ void lambda$pushQueueUpdate$5(java.util.ArrayList toSend, com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder holder) throws android.os.RemoteException {
        android.content.pm.ParceledListSlice<android.media.session.MediaSession.QueueItem> parcelableQueue = null;
        if (toSend != null) {
            parcelableQueue = new android.content.pm.ParceledListSlice<>(toSend);
            parcelableQueue.setInlineCountLimit(1);
        }
        holder.mCallback.onQueueChanged(parcelableQueue);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pushQueueTitleUpdate() {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return;
            }
            final java.lang.CharSequence queueTitle = this.mQueueTitle;
            performOnCallbackHolders("pushQueueTitleUpdate", new com.android.server.media.MediaSessionRecord.ControllerCallbackCall() { // from class: com.android.server.media.MediaSessionRecord$$ExternalSyntheticLambda8
                @Override // com.android.server.media.MediaSessionRecord.ControllerCallbackCall
                public final void performOn(com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder iSessionControllerCallbackHolder) {
                    iSessionControllerCallbackHolder.mCallback.onQueueTitleChanged(queueTitle);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pushExtrasUpdate() {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return;
            }
            final android.os.Bundle extras = this.mExtras;
            performOnCallbackHolders("pushExtrasUpdate", new com.android.server.media.MediaSessionRecord.ControllerCallbackCall() { // from class: com.android.server.media.MediaSessionRecord$$ExternalSyntheticLambda10
                @Override // com.android.server.media.MediaSessionRecord.ControllerCallbackCall
                public final void performOn(com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder iSessionControllerCallbackHolder) {
                    iSessionControllerCallbackHolder.mCallback.onExtrasChanged(extras);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pushVolumeUpdate() {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return;
            }
            final android.media.session.MediaController.PlaybackInfo info = getVolumeAttributes();
            performOnCallbackHolders("pushVolumeUpdate", new com.android.server.media.MediaSessionRecord.ControllerCallbackCall() { // from class: com.android.server.media.MediaSessionRecord$$ExternalSyntheticLambda7
                @Override // com.android.server.media.MediaSessionRecord.ControllerCallbackCall
                public final void performOn(com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder iSessionControllerCallbackHolder) {
                    iSessionControllerCallbackHolder.mCallback.onVolumeInfoChanged(info);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pushEvent(final java.lang.String event, final android.os.Bundle data) {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return;
            }
            performOnCallbackHolders("pushEvent", new com.android.server.media.MediaSessionRecord.ControllerCallbackCall() { // from class: com.android.server.media.MediaSessionRecord$$ExternalSyntheticLambda1
                @Override // com.android.server.media.MediaSessionRecord.ControllerCallbackCall
                public final void performOn(com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder iSessionControllerCallbackHolder) {
                    iSessionControllerCallbackHolder.mCallback.onEvent(event, data);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void pushSessionDestroyed() {
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                performOnCallbackHolders("pushSessionDestroyed", new com.android.server.media.MediaSessionRecord.ControllerCallbackCall() { // from class: com.android.server.media.MediaSessionRecord$$ExternalSyntheticLambda2
                    @Override // com.android.server.media.MediaSessionRecord.ControllerCallbackCall
                    public final void performOn(com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder iSessionControllerCallbackHolder) throws android.os.RemoteException {
                        com.android.server.media.MediaSessionRecord.lambda$pushSessionDestroyed$10(iSessionControllerCallbackHolder);
                    }
                });
                synchronized (this.mLock) {
                    this.mControllerCallbackHolders.clear();
                }
            }
        }
    }

    static /* synthetic */ void lambda$pushSessionDestroyed$10(com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder holder) throws android.os.RemoteException {
        holder.mCallback.asBinder().unlinkToDeath(holder.mDeathMonitor, 0);
        holder.mCallback.onSessionDestroyed();
    }

    private void performOnCallbackHolders(java.lang.String operationName, com.android.server.media.MediaSessionRecord.ControllerCallbackCall call) {
        java.util.ArrayList<com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder> deadCallbackHolders = new java.util.ArrayList<>();
        for (com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder holder : this.mControllerCallbackHolders) {
            try {
                call.performOn(holder);
            } catch (android.os.RemoteException | java.util.NoSuchElementException exception) {
                deadCallbackHolders.add(holder);
                logCallbackException("Exception while executing: " + operationName, holder, exception);
            }
        }
        synchronized (this.mLock) {
            this.mControllerCallbackHolders.removeAll(deadCallbackHolders);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.media.session.PlaybackState getStateWithUpdatedPosition() {
        long position;
        synchronized (this.mLock) {
            if (this.mDestroyed) {
                return null;
            }
            android.media.session.PlaybackState state = this.mPlaybackState;
            long duration = this.mDuration;
            android.media.session.PlaybackState result = null;
            if (state != null && (state.getState() == 3 || state.getState() == 4 || state.getState() == 5)) {
                long updateTime = state.getLastPositionUpdateTime();
                long currentTime = android.os.SystemClock.elapsedRealtime();
                if (updateTime > 0) {
                    long position2 = ((long) (state.getPlaybackSpeed() * (currentTime - updateTime))) + state.getPosition();
                    if (duration >= 0 && position2 > duration) {
                        position = duration;
                    } else if (position2 >= 0) {
                        position = position2;
                    } else {
                        position = 0;
                    }
                    android.media.session.PlaybackState.Builder builder = new android.media.session.PlaybackState.Builder(state);
                    builder.setState(state.getState(), position, state.getPlaybackSpeed(), currentTime);
                    result = builder.build();
                }
            }
            return result == null ? state : result;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getControllerHolderIndexForCb(android.media.session.ISessionControllerCallback cb) {
        android.os.IBinder binder = cb.asBinder();
        for (int i = this.mControllerCallbackHolders.size() - 1; i >= 0; i--) {
            if (binder.equals(this.mControllerCallbackHolders.get(i).mCallback.asBinder())) {
                return i;
            }
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public android.media.session.MediaController.PlaybackInfo getVolumeAttributes() {
        synchronized (this.mLock) {
            if (this.mVolumeType == 2) {
                int current = this.mOptimisticVolume != -1 ? this.mOptimisticVolume : this.mCurrentVolume;
                return new android.media.session.MediaController.PlaybackInfo(this.mVolumeType, this.mVolumeControlType, this.mMaxVolume, current, this.mAudioAttrs, this.mVolumeControlId);
            }
            int volumeType = this.mVolumeType;
            android.media.AudioAttributes attributes = this.mAudioAttrs;
            int stream = getVolumeStream(attributes);
            int max = this.mAudioManager.getStreamMaxVolume(stream);
            int current2 = this.mAudioManager.getStreamVolume(stream);
            return new android.media.session.MediaController.PlaybackInfo(volumeType, 2, max, current2, attributes, null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$11() {
        boolean needUpdate = this.mOptimisticVolume != this.mCurrentVolume;
        this.mOptimisticVolume = -1;
        if (needUpdate) {
            pushVolumeUpdate();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean componentNameExists(android.content.ComponentName componentName, android.content.Context context, int userId) {
        android.content.Intent mediaButtonIntent = new android.content.Intent("android.intent.action.MEDIA_BUTTON");
        mediaButtonIntent.addFlags(268435456);
        mediaButtonIntent.setComponent(componentName);
        android.os.UserHandle userHandle = android.os.UserHandle.of(userId);
        android.content.pm.PackageManager pm = context.getPackageManager();
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = pm.queryBroadcastReceiversAsUser(mediaButtonIntent, android.content.pm.PackageManager.ResolveInfoFlags.of(0L), userHandle);
        return !resolveInfos.isEmpty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateUserEngagedStateIfNeededLocked(boolean isTimeoutExpired) {
        int newUserEngagedState;
        if (!com.android.media.flags.Flags.enableNotifyingActivityManagerWithMediaSessionStatusChange()) {
            return;
        }
        int oldUserEngagedState = this.mUserEngagementState;
        if (!isActive() || this.mPlaybackState == null || this.mDestroyed) {
            newUserEngagedState = 2;
        } else if (isActive() && this.mPlaybackState.isActive()) {
            newUserEngagedState = 0;
        } else if (this.mPlaybackState.getState() == 2) {
            if (oldUserEngagedState == 0 || !isTimeoutExpired) {
                newUserEngagedState = 1;
            } else {
                newUserEngagedState = 2;
            }
        } else {
            newUserEngagedState = 2;
        }
        if (oldUserEngagedState == newUserEngagedState) {
            return;
        }
        this.mUserEngagementState = newUserEngagedState;
        if (newUserEngagedState == 1) {
            this.mHandler.postDelayed(this.mUserEngagementTimeoutExpirationRunnable, 600000L);
        } else {
            this.mHandler.removeCallbacks(this.mUserEngagementTimeoutExpirationRunnable);
        }
        boolean wasUserEngaged = oldUserEngagedState != 2;
        final boolean isNowUserEngaged = newUserEngagedState != 2;
        if (wasUserEngaged != isNowUserEngaged) {
            this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.media.MediaSessionRecord$$ExternalSyntheticLambda6
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$updateUserEngagedStateIfNeededLocked$12(isNowUserEngaged);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateUserEngagedStateIfNeededLocked$12(boolean isNowUserEngaged) {
        this.mService.onSessionUserEngagementStateChange(this, isNowUserEngaged);
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class SessionStub extends android.media.session.ISession.Stub {
        private SessionStub() {
        }

        public void destroySession() throws android.os.RemoteException {
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.MediaSessionRecord.this.mService.onSessionDied(com.android.server.media.MediaSessionRecord.this);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void sendEvent(java.lang.String event, android.os.Bundle data) throws android.os.RemoteException {
            com.android.server.media.MediaSessionRecord.this.mHandler.post(6, event, data == null ? null : new android.os.Bundle(data));
        }

        public android.media.session.ISessionController getController() throws android.os.RemoteException {
            return com.android.server.media.MediaSessionRecord.this.mController;
        }

        public void setActive(boolean active) throws android.os.RemoteException {
            int callingUid = android.os.Binder.getCallingUid();
            int callingPid = android.os.Binder.getCallingPid();
            if (active) {
                ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).logFgsApiBegin(4, callingUid, callingPid);
            } else {
                ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).logFgsApiEnd(4, callingUid, callingPid);
            }
            synchronized (com.android.server.media.MediaSessionRecord.this.mLock) {
                com.android.server.media.MediaSessionRecord.this.mIsActive = active;
                com.android.server.media.MediaSessionRecord.this.updateUserEngagedStateIfNeededLocked(false);
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.MediaSessionRecord.this.mService.onSessionActiveStateChanged(com.android.server.media.MediaSessionRecord.this, com.android.server.media.MediaSessionRecord.this.mPlaybackState);
                android.os.Binder.restoreCallingIdentity(token);
                com.android.server.media.MediaSessionRecord.this.mHandler.post(7);
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(token);
                throw th;
            }
        }

        public void setFlags(int flags) throws android.os.RemoteException {
            if ((flags & 65536) != 0) {
                int pid = android.os.Binder.getCallingPid();
                int uid = android.os.Binder.getCallingUid();
                com.android.server.media.MediaSessionRecord.this.mService.enforcePhoneStatePermission(pid, uid);
            }
            com.android.server.media.MediaSessionRecord.this.mFlags = flags;
            if ((65536 & flags) != 0) {
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.media.MediaSessionRecord.this.mService.setGlobalPrioritySession(com.android.server.media.MediaSessionRecord.this);
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
            com.android.server.media.MediaSessionRecord.this.mHandler.post(7);
        }

        public void setMediaButtonReceiver(android.app.PendingIntent pi) throws android.os.RemoteException {
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                if ((com.android.server.media.MediaSessionRecord.this.mPolicies & 1) != 0) {
                    return;
                }
                if (pi == null || !pi.isActivity()) {
                    com.android.server.media.MediaSessionRecord.this.mMediaButtonReceiverHolder = com.android.server.media.MediaButtonReceiverHolder.create(com.android.server.media.MediaSessionRecord.this.mUserId, pi, com.android.server.media.MediaSessionRecord.this.mPackageName);
                    com.android.server.media.MediaSessionRecord.this.mService.onMediaButtonReceiverChanged(com.android.server.media.MediaSessionRecord.this);
                } else {
                    if (android.app.compat.CompatChanges.isChangeEnabled(com.android.server.media.MediaSessionRecord.THROW_FOR_ACTIVITY_MEDIA_BUTTON_RECEIVER, uid)) {
                        throw new java.lang.IllegalArgumentException("The media button receiver cannot be set to an activity.");
                    }
                    android.util.Slog.w(com.android.server.media.MediaSessionRecord.TAG, "Ignoring invalid media button receiver targeting an activity.");
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setMediaButtonBroadcastReceiver(android.content.ComponentName receiver) throws android.os.RemoteException {
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            if (receiver != null) {
                try {
                    if (!android.text.TextUtils.equals(com.android.server.media.MediaSessionRecord.this.mPackageName, receiver.getPackageName())) {
                        android.util.EventLog.writeEvent(1397638484, "238177121", -1, "");
                        throw new java.lang.IllegalArgumentException("receiver does not belong to package name provided to MediaSessionRecord. Pkg = " + com.android.server.media.MediaSessionRecord.this.mPackageName + ", Receiver Pkg = " + receiver.getPackageName());
                    }
                } finally {
                    android.os.Binder.restoreCallingIdentity(token);
                }
            }
            if ((com.android.server.media.MediaSessionRecord.this.mPolicies & 1) != 0) {
                return;
            }
            if (com.android.server.media.MediaSessionRecord.componentNameExists(receiver, com.android.server.media.MediaSessionRecord.this.mContext, com.android.server.media.MediaSessionRecord.this.mUserId)) {
                com.android.server.media.MediaSessionRecord.this.mMediaButtonReceiverHolder = com.android.server.media.MediaButtonReceiverHolder.create(com.android.server.media.MediaSessionRecord.this.mUserId, receiver);
                com.android.server.media.MediaSessionRecord.this.mService.onMediaButtonReceiverChanged(com.android.server.media.MediaSessionRecord.this);
            } else {
                if (android.app.compat.CompatChanges.isChangeEnabled(com.android.server.media.MediaSessionRecord.THROW_FOR_INVALID_BROADCAST_RECEIVER, uid)) {
                    throw new java.lang.IllegalArgumentException("Invalid component name: " + receiver);
                }
                android.util.Slog.w(com.android.server.media.MediaSessionRecord.TAG, "setMediaButtonBroadcastReceiver(): Ignoring invalid component name=" + receiver);
            }
        }

        public void setLaunchPendingIntent(android.app.PendingIntent pi) throws android.os.RemoteException {
            com.android.server.media.MediaSessionRecord.this.mLaunchIntent = pi;
        }

        public void setMetadata(android.media.MediaMetadata metadata, long duration, java.lang.String metadataDescription) throws android.os.RemoteException {
            synchronized (com.android.server.media.MediaSessionRecord.this.mLock) {
                com.android.server.media.MediaSessionRecord.this.mDuration = duration;
                com.android.server.media.MediaSessionRecord.this.mMetadataDescription = metadataDescription;
                com.android.server.media.MediaSessionRecord.this.mMetadata = sanitizeMediaMetadata(metadata);
            }
            com.android.server.media.MediaSessionRecord.this.mHandler.post(1);
        }

        private android.media.MediaMetadata sanitizeMediaMetadata(android.media.MediaMetadata metadata) {
            if (metadata == null) {
                return null;
            }
            android.media.MediaMetadata.Builder metadataBuilder = new android.media.MediaMetadata.Builder(metadata);
            for (java.lang.String key : com.android.server.media.MediaSessionRecord.ART_URIS) {
                java.lang.String uriString = metadata.getString(key);
                if (!android.text.TextUtils.isEmpty(uriString)) {
                    android.net.Uri uri = android.net.Uri.parse(uriString);
                    if (com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT.equals(uri.getScheme())) {
                        try {
                            com.android.server.media.MediaSessionRecord.this.mUgmInternal.checkGrantUriPermission(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), android.content.ContentProvider.getUriWithoutUserId(uri), 1, android.content.ContentProvider.getUserIdFromUri(uri, com.android.server.media.MediaSessionRecord.this.getUserId()));
                        } catch (java.lang.SecurityException e) {
                            metadataBuilder.putString(key, null);
                        }
                    }
                }
            }
            android.media.MediaMetadata sanitizedMetadata = metadataBuilder.build();
            sanitizedMetadata.size();
            return sanitizedMetadata;
        }

        public void setPlaybackState(android.media.session.PlaybackState state) throws android.os.RemoteException {
            int oldState = com.android.server.media.MediaSessionRecord.this.mPlaybackState == null ? 0 : com.android.server.media.MediaSessionRecord.this.mPlaybackState.getState();
            int newState = state == null ? 0 : state.getState();
            boolean shouldUpdatePriority = com.android.server.media.MediaSessionRecord.ALWAYS_PRIORITY_STATES.contains(java.lang.Integer.valueOf(newState)) || (!com.android.server.media.MediaSessionRecord.TRANSITION_PRIORITY_STATES.contains(java.lang.Integer.valueOf(oldState)) && com.android.server.media.MediaSessionRecord.TRANSITION_PRIORITY_STATES.contains(java.lang.Integer.valueOf(newState)));
            synchronized (com.android.server.media.MediaSessionRecord.this.mLock) {
                com.android.server.media.MediaSessionRecord.this.mPlaybackState = state;
                com.android.server.media.MediaSessionRecord.this.updateUserEngagedStateIfNeededLocked(false);
            }
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.MediaSessionRecord.this.mService.onSessionPlaybackStateChanged(com.android.server.media.MediaSessionRecord.this, shouldUpdatePriority, com.android.server.media.MediaSessionRecord.this.mPlaybackState);
                android.os.Binder.restoreCallingIdentity(token);
                com.android.server.media.MediaSessionRecord.this.mHandler.post(2);
            } catch (java.lang.Throwable th) {
                android.os.Binder.restoreCallingIdentity(token);
                throw th;
            }
        }

        public void resetQueue() throws android.os.RemoteException {
            synchronized (com.android.server.media.MediaSessionRecord.this.mLock) {
                com.android.server.media.MediaSessionRecord.this.mQueue = null;
            }
            com.android.server.media.MediaSessionRecord.this.mHandler.post(3);
        }

        public android.os.IBinder getBinderForSetQueue() throws android.os.RemoteException {
            return new android.media.session.ParcelableListBinder(android.media.session.MediaSession.QueueItem.class, new java.util.function.Consumer() { // from class: com.android.server.media.MediaSessionRecord$SessionStub$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$getBinderForSetQueue$0((java.util.List) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$getBinderForSetQueue$0(java.util.List list) {
            synchronized (com.android.server.media.MediaSessionRecord.this.mLock) {
                com.android.server.media.MediaSessionRecord.this.mQueue = list;
            }
            com.android.server.media.MediaSessionRecord.this.mHandler.post(3);
        }

        public void setQueueTitle(java.lang.CharSequence title) throws android.os.RemoteException {
            com.android.server.media.MediaSessionRecord.this.mQueueTitle = title;
            com.android.server.media.MediaSessionRecord.this.mHandler.post(4);
        }

        public void setExtras(android.os.Bundle extras) throws android.os.RemoteException {
            synchronized (com.android.server.media.MediaSessionRecord.this.mLock) {
                com.android.server.media.MediaSessionRecord.this.mExtras = extras == null ? null : new android.os.Bundle(extras);
            }
            com.android.server.media.MediaSessionRecord.this.mHandler.post(5);
        }

        public void setRatingType(int type) throws android.os.RemoteException {
            com.android.server.media.MediaSessionRecord.this.mRatingType = type;
        }

        public void setCurrentVolume(int volume) throws android.os.RemoteException {
            com.android.server.media.MediaSessionRecord.this.mCurrentVolume = volume;
            com.android.server.media.MediaSessionRecord.this.mHandler.post(8);
        }

        public void setPlaybackToLocal(android.media.AudioAttributes attributes) throws android.os.RemoteException {
            boolean typeChanged;
            synchronized (com.android.server.media.MediaSessionRecord.this.mLock) {
                typeChanged = com.android.server.media.MediaSessionRecord.this.mVolumeType == 2;
                com.android.server.media.MediaSessionRecord.this.mVolumeType = 1;
                com.android.server.media.MediaSessionRecord.this.mVolumeControlId = null;
                if (attributes != null) {
                    com.android.server.media.MediaSessionRecord.this.mAudioAttrs = attributes;
                } else {
                    android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Received null audio attributes, using existing attributes");
                }
            }
            if (typeChanged) {
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.media.MediaSessionRecord.this.mService.onSessionPlaybackTypeChanged(com.android.server.media.MediaSessionRecord.this);
                    android.os.Binder.restoreCallingIdentity(token);
                    com.android.server.media.MediaSessionRecord.this.mHandler.post(8);
                } catch (java.lang.Throwable th) {
                    android.os.Binder.restoreCallingIdentity(token);
                    throw th;
                }
            }
        }

        public void setPlaybackToRemote(int control, int max, java.lang.String controlId) throws android.os.RemoteException {
            boolean typeChanged;
            synchronized (com.android.server.media.MediaSessionRecord.this.mLock) {
                boolean z = true;
                if (com.android.server.media.MediaSessionRecord.this.mVolumeType != 1) {
                    z = false;
                }
                typeChanged = z;
                com.android.server.media.MediaSessionRecord.this.mVolumeType = 2;
                com.android.server.media.MediaSessionRecord.this.mVolumeControlType = control;
                com.android.server.media.MediaSessionRecord.this.mMaxVolume = max;
                com.android.server.media.MediaSessionRecord.this.mVolumeControlId = controlId;
            }
            if (typeChanged) {
                long token = android.os.Binder.clearCallingIdentity();
                try {
                    com.android.server.media.MediaSessionRecord.this.mService.onSessionPlaybackTypeChanged(com.android.server.media.MediaSessionRecord.this);
                    android.os.Binder.restoreCallingIdentity(token);
                    com.android.server.media.MediaSessionRecord.this.mHandler.post(8);
                } catch (java.lang.Throwable th) {
                    android.os.Binder.restoreCallingIdentity(token);
                    throw th;
                }
            }
        }
    }

    class SessionCb {
        private final android.media.session.ISessionCallback mCb;

        SessionCb(android.media.session.ISessionCallback cb) {
            this.mCb = cb;
        }

        public boolean sendMediaButton(java.lang.String packageName, int pid, int uid, boolean asSystemService, android.view.KeyEvent keyEvent, int sequenceId, android.os.ResultReceiver cb) {
            try {
                if (android.view.KeyEvent.isMediaSessionKey(keyEvent.getKeyCode())) {
                    java.lang.String reason = "action=" + android.view.KeyEvent.actionToString(keyEvent.getAction()) + ";code=" + android.view.KeyEvent.keyCodeToString(keyEvent.getKeyCode());
                    com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, reason);
                }
                if (asSystemService) {
                    this.mCb.onMediaButton(com.android.server.media.MediaSessionRecord.this.mContext.getPackageName(), android.os.Process.myPid(), 1000, createMediaButtonIntent(keyEvent), sequenceId, cb);
                    return true;
                }
                this.mCb.onMediaButton(packageName, pid, uid, createMediaButtonIntent(keyEvent), sequenceId, cb);
                return true;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in sendMediaRequest.", e);
                return false;
            }
        }

        public boolean sendMediaButton(java.lang.String packageName, int pid, int uid, boolean asSystemService, android.view.KeyEvent keyEvent) {
            try {
                if (android.view.KeyEvent.isMediaSessionKey(keyEvent.getKeyCode())) {
                    java.lang.String reason = "action=" + android.view.KeyEvent.actionToString(keyEvent.getAction()) + ";code=" + android.view.KeyEvent.keyCodeToString(keyEvent.getKeyCode());
                    com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, reason);
                }
                if (asSystemService) {
                    this.mCb.onMediaButton(com.android.server.media.MediaSessionRecord.this.mContext.getPackageName(), android.os.Process.myPid(), 1000, createMediaButtonIntent(keyEvent), 0, (android.os.ResultReceiver) null);
                    return true;
                }
                this.mCb.onMediaButtonFromController(packageName, pid, uid, createMediaButtonIntent(keyEvent));
                return true;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in sendMediaRequest.", e);
                return false;
            }
        }

        public void sendCommand(java.lang.String packageName, int pid, int uid, java.lang.String command, android.os.Bundle args, android.os.ResultReceiver cb) {
            try {
                try {
                    java.lang.String reason = "MediaSessionRecord:" + command;
                    com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, reason);
                    this.mCb.onCommand(packageName, pid, uid, command, args, cb);
                } catch (android.os.RemoteException e) {
                    e = e;
                    android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in sendCommand.", e);
                }
            } catch (android.os.RemoteException e2) {
                e = e2;
            }
        }

        public void sendCustomAction(java.lang.String packageName, int pid, int uid, java.lang.String action, android.os.Bundle args) {
            try {
                try {
                    java.lang.String reason = "MediaSessionRecord:custom-" + action;
                    com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, reason);
                    this.mCb.onCustomAction(packageName, pid, uid, action, args);
                } catch (android.os.RemoteException e) {
                    e = e;
                    android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in sendCustomAction.", e);
                }
            } catch (android.os.RemoteException e2) {
                e = e2;
            }
        }

        public void prepare(java.lang.String packageName, int pid, int uid) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:prepare");
                this.mCb.onPrepare(packageName, pid, uid);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in prepare.", e);
            }
        }

        public void prepareFromMediaId(java.lang.String packageName, int pid, int uid, java.lang.String mediaId, android.os.Bundle extras) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:prepareFromMediaId");
                this.mCb.onPrepareFromMediaId(packageName, pid, uid, mediaId, extras);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in prepareFromMediaId.", e);
            }
        }

        public void prepareFromSearch(java.lang.String packageName, int pid, int uid, java.lang.String query, android.os.Bundle extras) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:prepareFromSearch");
                this.mCb.onPrepareFromSearch(packageName, pid, uid, query, extras);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in prepareFromSearch.", e);
            }
        }

        public void prepareFromUri(java.lang.String packageName, int pid, int uid, android.net.Uri uri, android.os.Bundle extras) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:prepareFromUri");
                this.mCb.onPrepareFromUri(packageName, pid, uid, uri, extras);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in prepareFromUri.", e);
            }
        }

        public void play(java.lang.String packageName, int pid, int uid) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:play");
                this.mCb.onPlay(packageName, pid, uid);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in play.", e);
            }
        }

        public void playFromMediaId(java.lang.String packageName, int pid, int uid, java.lang.String mediaId, android.os.Bundle extras) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:playFromMediaId");
                this.mCb.onPlayFromMediaId(packageName, pid, uid, mediaId, extras);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in playFromMediaId.", e);
            }
        }

        public void playFromSearch(java.lang.String packageName, int pid, int uid, java.lang.String query, android.os.Bundle extras) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:playFromSearch");
                this.mCb.onPlayFromSearch(packageName, pid, uid, query, extras);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in playFromSearch.", e);
            }
        }

        public void playFromUri(java.lang.String packageName, int pid, int uid, android.net.Uri uri, android.os.Bundle extras) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:playFromUri");
                this.mCb.onPlayFromUri(packageName, pid, uid, uri, extras);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in playFromUri.", e);
            }
        }

        public void skipToTrack(java.lang.String packageName, int pid, int uid, long id) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:skipToTrack");
                this.mCb.onSkipToTrack(packageName, pid, uid, id);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in skipToTrack", e);
            }
        }

        public void pause(java.lang.String packageName, int pid, int uid) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:pause");
                this.mCb.onPause(packageName, pid, uid);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in pause.", e);
            }
        }

        public void stop(java.lang.String packageName, int pid, int uid) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:stop");
                this.mCb.onStop(packageName, pid, uid);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in stop.", e);
            }
        }

        public void next(java.lang.String packageName, int pid, int uid) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:next");
                this.mCb.onNext(packageName, pid, uid);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in next.", e);
            }
        }

        public void previous(java.lang.String packageName, int pid, int uid) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:previous");
                this.mCb.onPrevious(packageName, pid, uid);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in previous.", e);
            }
        }

        public void fastForward(java.lang.String packageName, int pid, int uid) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:fastForward");
                this.mCb.onFastForward(packageName, pid, uid);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in fastForward.", e);
            }
        }

        public void rewind(java.lang.String packageName, int pid, int uid) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:rewind");
                this.mCb.onRewind(packageName, pid, uid);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in rewind.", e);
            }
        }

        public void seekTo(java.lang.String packageName, int pid, int uid, long pos) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:seekTo");
                this.mCb.onSeekTo(packageName, pid, uid, pos);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in seekTo.", e);
            }
        }

        public void rate(java.lang.String packageName, int pid, int uid, android.media.Rating rating) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:rate");
                this.mCb.onRate(packageName, pid, uid, rating);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in rate.", e);
            }
        }

        public void setPlaybackSpeed(java.lang.String packageName, int pid, int uid, float speed) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:setPlaybackSpeed");
                this.mCb.onSetPlaybackSpeed(packageName, pid, uid, speed);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in setPlaybackSpeed.", e);
            }
        }

        public void adjustVolume(java.lang.String packageName, int pid, int uid, boolean asSystemService, int direction) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:adjustVolume");
                if (asSystemService) {
                    this.mCb.onAdjustVolume(com.android.server.media.MediaSessionRecord.this.mContext.getPackageName(), android.os.Process.myPid(), 1000, direction);
                } else {
                    this.mCb.onAdjustVolume(packageName, pid, uid, direction);
                }
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in adjustVolume.", e);
            }
        }

        public void setVolumeTo(java.lang.String packageName, int pid, int uid, int value) {
            try {
                com.android.server.media.MediaSessionRecord.this.mService.tempAllowlistTargetPkgIfPossible(com.android.server.media.MediaSessionRecord.this.getUid(), com.android.server.media.MediaSessionRecord.this.getPackageName(), pid, uid, packageName, "MediaSessionRecord:setVolumeTo");
                this.mCb.onSetVolumeTo(packageName, pid, uid, value);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.media.MediaSessionRecord.TAG, "Remote failure in setVolumeTo.", e);
            }
        }

        private android.content.Intent createMediaButtonIntent(android.view.KeyEvent keyEvent) {
            android.content.Intent mediaButtonIntent = new android.content.Intent("android.intent.action.MEDIA_BUTTON");
            mediaButtonIntent.putExtra("android.intent.extra.KEY_EVENT", keyEvent);
            return mediaButtonIntent;
        }
    }

    class ControllerStub extends android.media.session.ISessionController.Stub {
        ControllerStub() {
        }

        public void sendCommand(java.lang.String packageName, java.lang.String command, android.os.Bundle args, android.os.ResultReceiver cb) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.sendCommand(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), command, args, cb);
        }

        public boolean sendMediaButton(java.lang.String packageName, android.view.KeyEvent keyEvent) {
            return com.android.server.media.MediaSessionRecord.this.mSessionCb.sendMediaButton(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), false, keyEvent);
        }

        public void registerCallback(java.lang.String packageName, final android.media.session.ISessionControllerCallback cb) {
            synchronized (com.android.server.media.MediaSessionRecord.this.mLock) {
                if (com.android.server.media.MediaSessionRecord.this.mDestroyed) {
                    try {
                        cb.onSessionDestroyed();
                    } catch (java.lang.Exception e) {
                    }
                    return;
                }
                if (com.android.server.media.MediaSessionRecord.this.getControllerHolderIndexForCb(cb) < 0) {
                    com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder holder = com.android.server.media.MediaSessionRecord.this.new ISessionControllerCallbackHolder(cb, packageName, android.os.Binder.getCallingUid(), new android.os.IBinder.DeathRecipient() { // from class: com.android.server.media.MediaSessionRecord$ControllerStub$$ExternalSyntheticLambda0
                        @Override // android.os.IBinder.DeathRecipient
                        public final void binderDied() {
                            this.f$0.lambda$registerCallback$0(cb);
                        }
                    });
                    com.android.server.media.MediaSessionRecord.this.mControllerCallbackHolders.add(holder);
                    if (com.android.server.media.MediaSessionRecord.DEBUG) {
                        android.util.Slog.d(com.android.server.media.MediaSessionRecord.TAG, "registering controller callback " + cb + " from controller" + packageName);
                    }
                    try {
                        cb.asBinder().linkToDeath(holder.mDeathMonitor, 0);
                    } catch (android.os.RemoteException e2) {
                        lambda$registerCallback$0(cb);
                        android.util.Slog.w(com.android.server.media.MediaSessionRecord.TAG, "registerCallback failed to linkToDeath", e2);
                    }
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:14:0x0040 A[Catch: all -> 0x005f, TryCatch #1 {, blocks: (B:4:0x0007, B:7:0x0010, B:11:0x0031, B:12:0x003a, B:14:0x0040, B:15:0x005d, B:10:0x002a), top: B:22:0x0007, inners: #0 }] */
        /* JADX INFO: renamed from: unregisterCallback, reason: merged with bridge method [inline-methods] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void lambda$registerCallback$0(android.media.session.ISessionControllerCallback r6) {
            /*
                r5 = this;
                com.android.server.media.MediaSessionRecord r0 = com.android.server.media.MediaSessionRecord.this
                java.lang.Object r0 = com.android.server.media.MediaSessionRecord.m5272$$Nest$fgetmLock(r0)
                monitor-enter(r0)
                com.android.server.media.MediaSessionRecord r1 = com.android.server.media.MediaSessionRecord.this     // Catch: java.lang.Throwable -> L5f
                int r1 = com.android.server.media.MediaSessionRecord.m5305$$Nest$mgetControllerHolderIndexForCb(r1, r6)     // Catch: java.lang.Throwable -> L5f
                r2 = -1
                if (r1 == r2) goto L3a
                android.os.IBinder r2 = r6.asBinder()     // Catch: java.util.NoSuchElementException -> L29 java.lang.Throwable -> L5f
                com.android.server.media.MediaSessionRecord r3 = com.android.server.media.MediaSessionRecord.this     // Catch: java.util.NoSuchElementException -> L29 java.lang.Throwable -> L5f
                java.util.concurrent.CopyOnWriteArrayList r3 = com.android.server.media.MediaSessionRecord.m5266$$Nest$fgetmControllerCallbackHolders(r3)     // Catch: java.util.NoSuchElementException -> L29 java.lang.Throwable -> L5f
                java.lang.Object r3 = r3.get(r1)     // Catch: java.util.NoSuchElementException -> L29 java.lang.Throwable -> L5f
                com.android.server.media.MediaSessionRecord$ISessionControllerCallbackHolder r3 = (com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder) r3     // Catch: java.util.NoSuchElementException -> L29 java.lang.Throwable -> L5f
                android.os.IBinder$DeathRecipient r3 = com.android.server.media.MediaSessionRecord.ISessionControllerCallbackHolder.m5324$$Nest$fgetmDeathMonitor(r3)     // Catch: java.util.NoSuchElementException -> L29 java.lang.Throwable -> L5f
                r4 = 0
                r2.unlinkToDeath(r3, r4)     // Catch: java.util.NoSuchElementException -> L29 java.lang.Throwable -> L5f
                goto L31
            L29:
                r2 = move-exception
                java.lang.String r3 = "MediaSessionRecord"
                java.lang.String r4 = "error unlinking to binder death"
                android.util.Slog.w(r3, r4, r2)     // Catch: java.lang.Throwable -> L5f
            L31:
                com.android.server.media.MediaSessionRecord r2 = com.android.server.media.MediaSessionRecord.this     // Catch: java.lang.Throwable -> L5f
                java.util.concurrent.CopyOnWriteArrayList r2 = com.android.server.media.MediaSessionRecord.m5266$$Nest$fgetmControllerCallbackHolders(r2)     // Catch: java.lang.Throwable -> L5f
                r2.remove(r1)     // Catch: java.lang.Throwable -> L5f
            L3a:
                boolean r2 = com.android.server.media.MediaSessionRecord.m5320$$Nest$sfgetDEBUG()     // Catch: java.lang.Throwable -> L5f
                if (r2 == 0) goto L5d
                java.lang.String r2 = "MediaSessionRecord"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L5f
                r3.<init>()     // Catch: java.lang.Throwable -> L5f
                java.lang.String r4 = "unregistering callback "
                java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L5f
                android.os.IBinder r4 = r6.asBinder()     // Catch: java.lang.Throwable -> L5f
                java.lang.StringBuilder r3 = r3.append(r4)     // Catch: java.lang.Throwable -> L5f
                java.lang.String r3 = r3.toString()     // Catch: java.lang.Throwable -> L5f
                android.util.Slog.d(r2, r3)     // Catch: java.lang.Throwable -> L5f
            L5d:
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L5f
                return
            L5f:
                r1 = move-exception
                monitor-exit(r0)     // Catch: java.lang.Throwable -> L5f
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.media.MediaSessionRecord.ControllerStub.lambda$registerCallback$0(android.media.session.ISessionControllerCallback):void");
        }

        public java.lang.String getPackageName() {
            return com.android.server.media.MediaSessionRecord.this.mPackageName;
        }

        public java.lang.String getTag() {
            return com.android.server.media.MediaSessionRecord.this.mTag;
        }

        public android.os.Bundle getSessionInfo() {
            return com.android.server.media.MediaSessionRecord.this.mSessionInfo;
        }

        public android.app.PendingIntent getLaunchPendingIntent() {
            return com.android.server.media.MediaSessionRecord.this.mLaunchIntent;
        }

        public long getFlags() {
            return com.android.server.media.MediaSessionRecord.this.mFlags;
        }

        public android.media.session.MediaController.PlaybackInfo getVolumeAttributes() {
            return com.android.server.media.MediaSessionRecord.this.getVolumeAttributes();
        }

        public void adjustVolume(java.lang.String packageName, java.lang.String opPackageName, int direction, int flags) {
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.MediaSessionRecord.this.adjustVolume(packageName, opPackageName, pid, uid, false, direction, flags, false);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void setVolumeTo(java.lang.String packageName, java.lang.String opPackageName, int value, int flags) {
            int pid = android.os.Binder.getCallingPid();
            int uid = android.os.Binder.getCallingUid();
            long token = android.os.Binder.clearCallingIdentity();
            try {
                com.android.server.media.MediaSessionRecord.this.setVolumeTo(packageName, opPackageName, pid, uid, value, flags);
            } finally {
                android.os.Binder.restoreCallingIdentity(token);
            }
        }

        public void prepare(java.lang.String packageName) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.prepare(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
        }

        public void prepareFromMediaId(java.lang.String packageName, java.lang.String mediaId, android.os.Bundle extras) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.prepareFromMediaId(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), mediaId, extras);
        }

        public void prepareFromSearch(java.lang.String packageName, java.lang.String query, android.os.Bundle extras) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.prepareFromSearch(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), query, extras);
        }

        public void prepareFromUri(java.lang.String packageName, android.net.Uri uri, android.os.Bundle extras) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.prepareFromUri(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), uri, extras);
        }

        public void play(java.lang.String packageName) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.play(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
        }

        public void playFromMediaId(java.lang.String packageName, java.lang.String mediaId, android.os.Bundle extras) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.playFromMediaId(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), mediaId, extras);
        }

        public void playFromSearch(java.lang.String packageName, java.lang.String query, android.os.Bundle extras) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.playFromSearch(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), query, extras);
        }

        public void playFromUri(java.lang.String packageName, android.net.Uri uri, android.os.Bundle extras) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.playFromUri(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), uri, extras);
        }

        public void skipToQueueItem(java.lang.String packageName, long id) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.skipToTrack(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), id);
        }

        public void pause(java.lang.String packageName) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.pause(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
        }

        public void stop(java.lang.String packageName) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.stop(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
        }

        public void next(java.lang.String packageName) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.next(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
        }

        public void previous(java.lang.String packageName) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.previous(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
        }

        public void fastForward(java.lang.String packageName) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.fastForward(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
        }

        public void rewind(java.lang.String packageName) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.rewind(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid());
        }

        public void seekTo(java.lang.String packageName, long pos) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.seekTo(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), pos);
        }

        public void rate(java.lang.String packageName, android.media.Rating rating) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.rate(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), rating);
        }

        public void setPlaybackSpeed(java.lang.String packageName, float speed) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.setPlaybackSpeed(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), speed);
        }

        public void sendCustomAction(java.lang.String packageName, java.lang.String action, android.os.Bundle args) {
            com.android.server.media.MediaSessionRecord.this.mSessionCb.sendCustomAction(packageName, android.os.Binder.getCallingPid(), android.os.Binder.getCallingUid(), action, args);
        }

        public android.media.MediaMetadata getMetadata() {
            android.media.MediaMetadata mediaMetadata;
            synchronized (com.android.server.media.MediaSessionRecord.this.mLock) {
                mediaMetadata = com.android.server.media.MediaSessionRecord.this.mMetadata;
            }
            return mediaMetadata;
        }

        public android.media.session.PlaybackState getPlaybackState() {
            return com.android.server.media.MediaSessionRecord.this.getStateWithUpdatedPosition();
        }

        public android.content.pm.ParceledListSlice getQueue() {
            android.content.pm.ParceledListSlice parceledListSlice;
            synchronized (com.android.server.media.MediaSessionRecord.this.mLock) {
                parceledListSlice = com.android.server.media.MediaSessionRecord.this.mQueue == null ? null : new android.content.pm.ParceledListSlice(com.android.server.media.MediaSessionRecord.this.mQueue);
            }
            return parceledListSlice;
        }

        public java.lang.CharSequence getQueueTitle() {
            return com.android.server.media.MediaSessionRecord.this.mQueueTitle;
        }

        public android.os.Bundle getExtras() {
            android.os.Bundle bundle;
            synchronized (com.android.server.media.MediaSessionRecord.this.mLock) {
                bundle = com.android.server.media.MediaSessionRecord.this.mExtras;
            }
            return bundle;
        }

        public int getRatingType() {
            return com.android.server.media.MediaSessionRecord.this.mRatingType;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ISessionControllerCallbackHolder {
        private final android.media.session.ISessionControllerCallback mCallback;
        private final android.os.IBinder.DeathRecipient mDeathMonitor;
        private final java.lang.String mPackageName;
        private final int mUid;

        ISessionControllerCallbackHolder(android.media.session.ISessionControllerCallback callback, java.lang.String packageName, int uid, android.os.IBinder.DeathRecipient deathMonitor) {
            this.mCallback = callback;
            this.mPackageName = packageName;
            this.mUid = uid;
            this.mDeathMonitor = deathMonitor;
        }
    }

    private class MessageHandler extends android.os.Handler {
        private static final int MSG_DESTROYED = 9;
        private static final int MSG_SEND_EVENT = 6;
        private static final int MSG_UPDATE_EXTRAS = 5;
        private static final int MSG_UPDATE_METADATA = 1;
        private static final int MSG_UPDATE_PLAYBACK_STATE = 2;
        private static final int MSG_UPDATE_QUEUE = 3;
        private static final int MSG_UPDATE_QUEUE_TITLE = 4;
        private static final int MSG_UPDATE_SESSION_STATE = 7;
        private static final int MSG_UPDATE_VOLUME = 8;

        public MessageHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.media.MediaSessionRecord.this.pushMetadataUpdate();
                    break;
                case 2:
                    com.android.server.media.MediaSessionRecord.this.pushPlaybackStateUpdate();
                    break;
                case 3:
                    com.android.server.media.MediaSessionRecord.this.pushQueueUpdate();
                    break;
                case 4:
                    com.android.server.media.MediaSessionRecord.this.pushQueueTitleUpdate();
                    break;
                case 5:
                    com.android.server.media.MediaSessionRecord.this.pushExtrasUpdate();
                    break;
                case 6:
                    com.android.server.media.MediaSessionRecord.this.pushEvent((java.lang.String) msg.obj, msg.getData());
                    break;
                case 8:
                    com.android.server.media.MediaSessionRecord.this.pushVolumeUpdate();
                    break;
                case 9:
                    com.android.server.media.MediaSessionRecord.this.pushSessionDestroyed();
                    break;
            }
        }

        public void post(int what) {
            post(what, null);
        }

        public void post(int what, java.lang.Object obj) {
            obtainMessage(what, obj).sendToTarget();
        }

        public void post(int what, java.lang.Object obj, android.os.Bundle data) {
            android.os.Message msg = obtainMessage(what, obj);
            msg.setData(data);
            msg.sendToTarget();
        }
    }
}
