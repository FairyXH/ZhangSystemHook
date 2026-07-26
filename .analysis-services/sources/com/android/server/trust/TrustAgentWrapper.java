package com.android.server.trust;

/* JADX INFO: loaded from: classes3.dex */
public class TrustAgentWrapper {
    private static final java.lang.String DATA_DURATION = "duration";
    private static final java.lang.String DATA_ESCROW_TOKEN = "escrow_token";
    private static final java.lang.String DATA_HANDLE = "handle";
    private static final java.lang.String DATA_MESSAGE = "message";
    private static final java.lang.String DATA_USER_ID = "user_id";
    private static final boolean DEBUG = com.android.server.trust.TrustManagerService.DEBUG;
    private static final java.lang.String EXTRA_COMPONENT_NAME = "componentName";
    private static final int MSG_ADD_ESCROW_TOKEN = 7;
    private static final int MSG_ESCROW_TOKEN_STATE = 9;
    private static final int MSG_GRANT_TRUST = 1;
    private static final int MSG_LOCK_USER = 12;
    private static final int MSG_MANAGING_TRUST = 6;
    private static final int MSG_REMOVE_ESCROW_TOKEN = 8;
    private static final int MSG_RESTART_TIMEOUT = 4;
    private static final int MSG_REVOKE_TRUST = 2;
    private static final int MSG_SET_TRUST_AGENT_FEATURES_COMPLETED = 5;
    private static final int MSG_SHOW_KEYGUARD_ERROR_MESSAGE = 11;
    private static final int MSG_TRUST_TIMEOUT = 3;
    private static final int MSG_UNLOCK_USER = 10;
    private static final java.lang.String PERMISSION = "android.permission.PROVIDE_TRUST_AGENT";
    private static final long RESTART_TIMEOUT_MILLIS = 300000;
    private static final java.lang.String TAG = "TrustAgentWrapper";
    private static final java.lang.String TRUST_EXPIRED_ACTION = "android.server.trust.TRUST_EXPIRED_ACTION";
    private final android.content.Intent mAlarmIntent;
    private android.app.AlarmManager mAlarmManager;
    private android.app.PendingIntent mAlarmPendingIntent;
    private boolean mBound;
    private final android.content.Context mContext;
    private boolean mDisplayTrustGrantedMessage;
    private boolean mManagingTrust;
    private long mMaximumTimeToLock;
    private java.lang.CharSequence mMessage;
    private final android.content.ComponentName mName;
    private long mScheduledRestartUptimeMillis;
    private android.os.IBinder mSetTrustAgentFeaturesToken;
    private android.service.trust.ITrustAgentService mTrustAgentService;
    private boolean mTrustDisabledByDpm;
    private final com.android.server.trust.TrustManagerService mTrustManagerService;
    private boolean mTrustable;
    private boolean mTrusted;
    private final int mUserId;
    private boolean mPendingSuccessfulUnlock = false;
    private boolean mWaitingForTrustableDowngrade = false;
    private boolean mWithinSecurityLockdownWindow = false;
    private final android.content.BroadcastReceiver mTrustableDowngradeReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.trust.TrustAgentWrapper.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if ("android.intent.action.SCREEN_OFF".equals(intent.getAction())) {
                com.android.server.trust.TrustAgentWrapper.this.downgradeToTrustable();
            }
        }
    };
    private final android.content.BroadcastReceiver mBroadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.trust.TrustAgentWrapper.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            android.content.ComponentName component = (android.content.ComponentName) intent.getParcelableExtra(com.android.server.trust.TrustAgentWrapper.EXTRA_COMPONENT_NAME, android.content.ComponentName.class);
            if (com.android.server.trust.TrustAgentWrapper.TRUST_EXPIRED_ACTION.equals(intent.getAction()) && com.android.server.trust.TrustAgentWrapper.this.mName.equals(component)) {
                com.android.server.trust.TrustAgentWrapper.this.mHandler.removeMessages(3);
                com.android.server.trust.TrustAgentWrapper.this.mHandler.sendEmptyMessage(3);
            }
        }
    };
    private final android.os.Handler mHandler = new com.android.server.trust.TrustAgentWrapper.AnonymousClass3();
    private android.service.trust.ITrustAgentServiceCallback mCallback = new android.service.trust.ITrustAgentServiceCallback.Stub() { // from class: com.android.server.trust.TrustAgentWrapper.4
        public void grantTrust(java.lang.CharSequence message, long durationMs, int flags, com.android.internal.infra.AndroidFuture resultCallback) {
            if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                com.android.server.utils.Slogf.d(com.android.server.trust.TrustAgentWrapper.TAG, "grantTrust(message=\"%s\", durationMs=%d, flags=0x%x)", message, java.lang.Long.valueOf(durationMs), java.lang.Integer.valueOf(flags));
            }
            android.os.Message msg = com.android.server.trust.TrustAgentWrapper.this.mHandler.obtainMessage(1, flags, 0, android.util.Pair.create(message, resultCallback));
            msg.getData().putLong(com.android.server.trust.TrustAgentWrapper.DATA_DURATION, durationMs);
            msg.sendToTarget();
        }

        public void revokeTrust() {
            if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                android.util.Slog.d(com.android.server.trust.TrustAgentWrapper.TAG, "revokeTrust()");
            }
            com.android.server.trust.TrustAgentWrapper.this.mHandler.sendEmptyMessage(2);
        }

        public void lockUser() {
            if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                android.util.Slog.d(com.android.server.trust.TrustAgentWrapper.TAG, "lockUser()");
            }
            com.android.server.trust.TrustAgentWrapper.this.mHandler.sendEmptyMessage(12);
        }

        public void setManagingTrust(boolean z) {
            if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                com.android.server.utils.Slogf.d(com.android.server.trust.TrustAgentWrapper.TAG, "setManagingTrust(%s)", java.lang.Boolean.valueOf(z));
            }
            com.android.server.trust.TrustAgentWrapper.this.mHandler.obtainMessage(6, z ? 1 : 0, 0).sendToTarget();
        }

        public void onConfigureCompleted(boolean z, android.os.IBinder iBinder) {
            if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                com.android.server.utils.Slogf.d(com.android.server.trust.TrustAgentWrapper.TAG, "onConfigureCompleted(result=%s)", java.lang.Boolean.valueOf(z));
            }
            com.android.server.trust.TrustAgentWrapper.this.mHandler.obtainMessage(5, z ? 1 : 0, 0, iBinder).sendToTarget();
        }

        public void addEscrowToken(byte[] token, int userId) {
            if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                com.android.server.utils.Slogf.d(com.android.server.trust.TrustAgentWrapper.TAG, "addEscrowToken(userId=%d)", java.lang.Integer.valueOf(userId));
            }
            if (com.android.server.trust.TrustAgentWrapper.this.mContext.getResources().getBoolean(android.R.bool.config_allowDisablingAssistDisclosure)) {
                throw new java.lang.SecurityException("Escrow token API is not allowed.");
            }
            android.os.Message msg = com.android.server.trust.TrustAgentWrapper.this.mHandler.obtainMessage(7);
            msg.getData().putByteArray(com.android.server.trust.TrustAgentWrapper.DATA_ESCROW_TOKEN, token);
            msg.getData().putInt(com.android.server.trust.TrustAgentWrapper.DATA_USER_ID, userId);
            msg.sendToTarget();
        }

        public void isEscrowTokenActive(long handle, int userId) {
            if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                com.android.server.utils.Slogf.d(com.android.server.trust.TrustAgentWrapper.TAG, "isEscrowTokenActive(handle=%016x, userId=%d)", java.lang.Long.valueOf(handle), java.lang.Integer.valueOf(userId));
            }
            if (com.android.server.trust.TrustAgentWrapper.this.mContext.getResources().getBoolean(android.R.bool.config_allowDisablingAssistDisclosure)) {
                throw new java.lang.SecurityException("Escrow token API is not allowed.");
            }
            android.os.Message msg = com.android.server.trust.TrustAgentWrapper.this.mHandler.obtainMessage(9);
            msg.getData().putLong(com.android.server.trust.TrustAgentWrapper.DATA_HANDLE, handle);
            msg.getData().putInt(com.android.server.trust.TrustAgentWrapper.DATA_USER_ID, userId);
            msg.sendToTarget();
        }

        public void removeEscrowToken(long handle, int userId) {
            if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                com.android.server.utils.Slogf.d(com.android.server.trust.TrustAgentWrapper.TAG, "removeEscrowToken(handle=%016x, userId=%d)", java.lang.Long.valueOf(handle), java.lang.Integer.valueOf(userId));
            }
            if (com.android.server.trust.TrustAgentWrapper.this.mContext.getResources().getBoolean(android.R.bool.config_allowDisablingAssistDisclosure)) {
                throw new java.lang.SecurityException("Escrow token API is not allowed.");
            }
            android.os.Message msg = com.android.server.trust.TrustAgentWrapper.this.mHandler.obtainMessage(8);
            msg.getData().putLong(com.android.server.trust.TrustAgentWrapper.DATA_HANDLE, handle);
            msg.getData().putInt(com.android.server.trust.TrustAgentWrapper.DATA_USER_ID, userId);
            msg.sendToTarget();
        }

        public void unlockUserWithToken(long handle, byte[] token, int userId) {
            if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                com.android.server.utils.Slogf.d(com.android.server.trust.TrustAgentWrapper.TAG, "unlockUserWithToken(handle=%016x, userId=%d)", java.lang.Long.valueOf(handle), java.lang.Integer.valueOf(userId));
            }
            if (com.android.server.trust.TrustAgentWrapper.this.mContext.getResources().getBoolean(android.R.bool.config_allowDisablingAssistDisclosure)) {
                throw new java.lang.SecurityException("Escrow token API is not allowed.");
            }
            android.os.Message msg = com.android.server.trust.TrustAgentWrapper.this.mHandler.obtainMessage(10);
            msg.getData().putInt(com.android.server.trust.TrustAgentWrapper.DATA_USER_ID, userId);
            msg.getData().putLong(com.android.server.trust.TrustAgentWrapper.DATA_HANDLE, handle);
            msg.getData().putByteArray(com.android.server.trust.TrustAgentWrapper.DATA_ESCROW_TOKEN, token);
            msg.sendToTarget();
        }

        public void showKeyguardErrorMessage(java.lang.CharSequence message) {
            if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                com.android.server.utils.Slogf.d(com.android.server.trust.TrustAgentWrapper.TAG, "showKeyguardErrorMessage(\"%s\")", message);
            }
            android.os.Message msg = com.android.server.trust.TrustAgentWrapper.this.mHandler.obtainMessage(11);
            msg.getData().putCharSequence(com.android.server.trust.TrustAgentWrapper.DATA_MESSAGE, message);
            msg.sendToTarget();
        }
    };
    private final android.content.ServiceConnection mConnection = new android.content.ServiceConnection() { // from class: com.android.server.trust.TrustAgentWrapper.5
        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                android.util.Slog.d(com.android.server.trust.TrustAgentWrapper.TAG, "TrustAgent started : " + name.flattenToString());
            }
            com.android.server.trust.TrustAgentWrapper.this.mHandler.removeMessages(4);
            com.android.server.trust.TrustAgentWrapper.this.mTrustAgentService = android.service.trust.ITrustAgentService.Stub.asInterface(service);
            com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.mArchive.logAgentConnected(com.android.server.trust.TrustAgentWrapper.this.mUserId, name);
            com.android.server.trust.TrustAgentWrapper.this.setCallback(com.android.server.trust.TrustAgentWrapper.this.mCallback);
            com.android.server.trust.TrustAgentWrapper.this.updateDevicePolicyFeatures();
            if (com.android.server.trust.TrustAgentWrapper.this.mPendingSuccessfulUnlock) {
                com.android.server.trust.TrustAgentWrapper.this.onUnlockAttempt(true);
                com.android.server.trust.TrustAgentWrapper.this.mPendingSuccessfulUnlock = false;
            }
            if (com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.isDeviceLockedInner(com.android.server.trust.TrustAgentWrapper.this.mUserId)) {
                com.android.server.trust.TrustAgentWrapper.this.onDeviceLocked();
            } else {
                com.android.server.trust.TrustAgentWrapper.this.onDeviceUnlocked();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                android.util.Slog.d(com.android.server.trust.TrustAgentWrapper.TAG, "TrustAgent disconnected : " + name.flattenToShortString());
            }
            com.android.server.trust.TrustAgentWrapper.this.mTrustAgentService = null;
            com.android.server.trust.TrustAgentWrapper.this.mManagingTrust = false;
            com.android.server.trust.TrustAgentWrapper.this.mSetTrustAgentFeaturesToken = null;
            com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.mArchive.logAgentDied(com.android.server.trust.TrustAgentWrapper.this.mUserId, name);
            com.android.server.trust.TrustAgentWrapper.this.mHandler.sendEmptyMessage(2);
            if (com.android.server.trust.TrustAgentWrapper.this.mBound) {
                com.android.server.trust.TrustAgentWrapper.this.scheduleRestart();
            }
            if (com.android.server.trust.TrustAgentWrapper.this.mWithinSecurityLockdownWindow) {
                com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.lockUser(com.android.server.trust.TrustAgentWrapper.this.mUserId);
            }
        }
    };

    /* JADX INFO: renamed from: com.android.server.trust.TrustAgentWrapper$3, reason: invalid class name */
    class AnonymousClass3 extends android.os.Handler {
        AnonymousClass3() {
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message message) {
            long jMin;
            int i = 1;
            switch (message.what) {
                case 1:
                    if (!com.android.server.trust.TrustAgentWrapper.this.isConnected()) {
                        android.util.Log.w(com.android.server.trust.TrustAgentWrapper.TAG, "Agent is not connected, cannot grant trust: " + com.android.server.trust.TrustAgentWrapper.this.mName.flattenToShortString());
                        return;
                    }
                    com.android.server.trust.TrustAgentWrapper.this.mTrusted = true;
                    com.android.server.trust.TrustAgentWrapper.this.mTrustable = false;
                    android.util.Pair pair = (android.util.Pair) message.obj;
                    com.android.server.trust.TrustAgentWrapper.this.mMessage = (java.lang.CharSequence) pair.first;
                    com.android.internal.infra.AndroidFuture<android.service.trust.GrantTrustResult> androidFuture = (com.android.internal.infra.AndroidFuture) pair.second;
                    int i2 = message.arg1;
                    com.android.server.trust.TrustAgentWrapper.this.mDisplayTrustGrantedMessage = (i2 & 8) != 0;
                    if ((i2 & 4) != 0) {
                        com.android.server.trust.TrustAgentWrapper.this.mWaitingForTrustableDowngrade = true;
                        androidFuture.thenAccept(new java.util.function.Consumer() { // from class: com.android.server.trust.TrustAgentWrapper$3$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                this.f$0.lambda$handleMessage$0((android.service.trust.GrantTrustResult) obj);
                            }
                        });
                    } else {
                        com.android.server.trust.TrustAgentWrapper.this.mWaitingForTrustableDowngrade = false;
                    }
                    long j = message.getData().getLong(com.android.server.trust.TrustAgentWrapper.DATA_DURATION);
                    if (j > 0) {
                        if (com.android.server.trust.TrustAgentWrapper.this.mMaximumTimeToLock != 0) {
                            jMin = java.lang.Math.min(j, com.android.server.trust.TrustAgentWrapper.this.mMaximumTimeToLock);
                            if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                                android.util.Slog.d(com.android.server.trust.TrustAgentWrapper.TAG, "DPM lock timeout in effect. Timeout adjusted from " + j + " to " + jMin);
                            }
                        } else {
                            jMin = j;
                        }
                        long jElapsedRealtime = android.os.SystemClock.elapsedRealtime() + jMin;
                        com.android.server.trust.TrustAgentWrapper.this.mAlarmPendingIntent = android.app.PendingIntent.getBroadcast(com.android.server.trust.TrustAgentWrapper.this.mContext, 0, com.android.server.trust.TrustAgentWrapper.this.mAlarmIntent, android.hardware.audio.common.V2_0.AudioFormat.EVRCWB);
                        com.android.server.trust.TrustAgentWrapper.this.mAlarmManager.set(2, jElapsedRealtime, com.android.server.trust.TrustAgentWrapper.this.mAlarmPendingIntent);
                    }
                    com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.mArchive.logGrantTrust(com.android.server.trust.TrustAgentWrapper.this.mUserId, com.android.server.trust.TrustAgentWrapper.this.mName, com.android.server.trust.TrustAgentWrapper.this.mMessage != null ? com.android.server.trust.TrustAgentWrapper.this.mMessage.toString() : null, j, i2);
                    com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.updateTrust(com.android.server.trust.TrustAgentWrapper.this.mUserId, i2, androidFuture);
                    return;
                case 2:
                    break;
                case 3:
                    if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                        android.util.Slog.d(com.android.server.trust.TrustAgentWrapper.TAG, "Trust timed out : " + com.android.server.trust.TrustAgentWrapper.this.mName.flattenToShortString());
                    }
                    com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.mArchive.logTrustTimeout(com.android.server.trust.TrustAgentWrapper.this.mUserId, com.android.server.trust.TrustAgentWrapper.this.mName);
                    com.android.server.trust.TrustAgentWrapper.this.onTrustTimeout();
                    break;
                case 4:
                    android.util.Slog.w(com.android.server.trust.TrustAgentWrapper.TAG, "Connection attempt to agent " + com.android.server.trust.TrustAgentWrapper.this.mName.flattenToShortString() + " timed out, rebinding");
                    com.android.server.trust.TrustAgentWrapper.this.destroy();
                    com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.resetAgent(com.android.server.trust.TrustAgentWrapper.this.mName, com.android.server.trust.TrustAgentWrapper.this.mUserId);
                    return;
                case 5:
                    android.os.IBinder iBinder = (android.os.IBinder) message.obj;
                    char c = message.arg1 == 0 ? (char) 0 : (char) 1;
                    if (com.android.server.trust.TrustAgentWrapper.this.mSetTrustAgentFeaturesToken == iBinder) {
                        com.android.server.trust.TrustAgentWrapper.this.mSetTrustAgentFeaturesToken = null;
                        if (com.android.server.trust.TrustAgentWrapper.this.mTrustDisabledByDpm && c != 0) {
                            if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                                android.util.Slog.d(com.android.server.trust.TrustAgentWrapper.TAG, "Re-enabling agent because it acknowledged enabled features: " + com.android.server.trust.TrustAgentWrapper.this.mName.flattenToShortString());
                            }
                            com.android.server.trust.TrustAgentWrapper.this.mTrustDisabledByDpm = false;
                            com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.updateTrust(com.android.server.trust.TrustAgentWrapper.this.mUserId, 0);
                            return;
                        }
                        return;
                    }
                    if (com.android.server.trust.TrustAgentWrapper.DEBUG) {
                        android.util.Slog.w(com.android.server.trust.TrustAgentWrapper.TAG, "Ignoring MSG_SET_TRUST_AGENT_FEATURES_COMPLETED with obsolete token: " + com.android.server.trust.TrustAgentWrapper.this.mName.flattenToShortString());
                        return;
                    }
                    return;
                case 6:
                    com.android.server.trust.TrustAgentWrapper.this.mManagingTrust = message.arg1 != 0;
                    if (!com.android.server.trust.TrustAgentWrapper.this.mManagingTrust) {
                        com.android.server.trust.TrustAgentWrapper.this.mTrusted = false;
                        com.android.server.trust.TrustAgentWrapper.this.mDisplayTrustGrantedMessage = false;
                        com.android.server.trust.TrustAgentWrapper.this.mMessage = null;
                    }
                    com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.mArchive.logManagingTrust(com.android.server.trust.TrustAgentWrapper.this.mUserId, com.android.server.trust.TrustAgentWrapper.this.mName, com.android.server.trust.TrustAgentWrapper.this.mManagingTrust);
                    com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.updateTrust(com.android.server.trust.TrustAgentWrapper.this.mUserId, 0);
                    return;
                case 7:
                    byte[] byteArray = message.getData().getByteArray(com.android.server.trust.TrustAgentWrapper.DATA_ESCROW_TOKEN);
                    int i3 = message.getData().getInt(com.android.server.trust.TrustAgentWrapper.DATA_USER_ID);
                    long jAddEscrowToken = com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.addEscrowToken(byteArray, i3);
                    boolean z = false;
                    try {
                        if (com.android.server.trust.TrustAgentWrapper.this.mTrustAgentService != null) {
                            com.android.server.trust.TrustAgentWrapper.this.mTrustAgentService.onEscrowTokenAdded(byteArray, jAddEscrowToken, android.os.UserHandle.of(i3));
                            z = true;
                        }
                    } catch (android.os.RemoteException e) {
                        com.android.server.trust.TrustAgentWrapper.this.onError(e);
                    }
                    if (!z) {
                        com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.removeEscrowToken(jAddEscrowToken, i3);
                        return;
                    }
                    return;
                case 8:
                    long j2 = message.getData().getLong(com.android.server.trust.TrustAgentWrapper.DATA_HANDLE);
                    boolean zRemoveEscrowToken = com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.removeEscrowToken(j2, message.getData().getInt(com.android.server.trust.TrustAgentWrapper.DATA_USER_ID));
                    try {
                        if (com.android.server.trust.TrustAgentWrapper.this.mTrustAgentService != null) {
                            com.android.server.trust.TrustAgentWrapper.this.mTrustAgentService.onEscrowTokenRemoved(j2, zRemoveEscrowToken);
                            return;
                        }
                        return;
                    } catch (android.os.RemoteException e2) {
                        com.android.server.trust.TrustAgentWrapper.this.onError(e2);
                        return;
                    }
                case 9:
                    long j3 = message.getData().getLong(com.android.server.trust.TrustAgentWrapper.DATA_HANDLE);
                    boolean zIsEscrowTokenActive = com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.isEscrowTokenActive(j3, message.getData().getInt(com.android.server.trust.TrustAgentWrapper.DATA_USER_ID));
                    try {
                        if (com.android.server.trust.TrustAgentWrapper.this.mTrustAgentService != null) {
                            android.service.trust.ITrustAgentService iTrustAgentService = com.android.server.trust.TrustAgentWrapper.this.mTrustAgentService;
                            if (!zIsEscrowTokenActive) {
                                i = 0;
                            }
                            iTrustAgentService.onTokenStateReceived(j3, i);
                            return;
                        }
                        return;
                    } catch (android.os.RemoteException e3) {
                        com.android.server.trust.TrustAgentWrapper.this.onError(e3);
                        return;
                    }
                case 10:
                    com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.unlockUserWithToken(message.getData().getLong(com.android.server.trust.TrustAgentWrapper.DATA_HANDLE), message.getData().getByteArray(com.android.server.trust.TrustAgentWrapper.DATA_ESCROW_TOKEN), message.getData().getInt(com.android.server.trust.TrustAgentWrapper.DATA_USER_ID));
                    return;
                case 11:
                    com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.showKeyguardErrorMessage(message.getData().getCharSequence(com.android.server.trust.TrustAgentWrapper.DATA_MESSAGE));
                    return;
                case 12:
                    com.android.server.trust.TrustAgentWrapper.this.mTrusted = false;
                    com.android.server.trust.TrustAgentWrapper.this.mTrustable = false;
                    com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.updateTrust(com.android.server.trust.TrustAgentWrapper.this.mUserId, 0);
                    com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.lockUser(com.android.server.trust.TrustAgentWrapper.this.mUserId);
                    return;
                default:
                    return;
            }
            com.android.server.trust.TrustAgentWrapper.this.mTrusted = false;
            com.android.server.trust.TrustAgentWrapper.this.mTrustable = false;
            com.android.server.trust.TrustAgentWrapper.this.mWaitingForTrustableDowngrade = false;
            com.android.server.trust.TrustAgentWrapper.this.mDisplayTrustGrantedMessage = false;
            com.android.server.trust.TrustAgentWrapper.this.mMessage = null;
            com.android.server.trust.TrustAgentWrapper.this.mHandler.removeMessages(3);
            if (message.what == 2) {
                com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.mArchive.logRevokeTrust(com.android.server.trust.TrustAgentWrapper.this.mUserId, com.android.server.trust.TrustAgentWrapper.this.mName);
            }
            com.android.server.trust.TrustAgentWrapper.this.mTrustManagerService.updateTrust(com.android.server.trust.TrustAgentWrapper.this.mUserId, 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleMessage$0(android.service.trust.GrantTrustResult result) {
            if (result.getStatus() == 1) {
                com.android.server.trust.TrustAgentWrapper.this.setSecurityWindowTimer();
            }
        }
    }

    public TrustAgentWrapper(android.content.Context context, com.android.server.trust.TrustManagerService trustManagerService, android.content.Intent intent, android.os.UserHandle user) {
        this.mContext = context;
        this.mTrustManagerService = trustManagerService;
        this.mAlarmManager = (android.app.AlarmManager) this.mContext.getSystemService(com.android.server.am.HostingRecord.TRIGGER_TYPE_ALARM);
        this.mUserId = user.getIdentifier();
        this.mName = intent.getComponent();
        this.mAlarmIntent = new android.content.Intent(TRUST_EXPIRED_ACTION).putExtra(EXTRA_COMPONENT_NAME, this.mName);
        this.mAlarmIntent.setData(android.net.Uri.parse(this.mAlarmIntent.toUri(1)));
        this.mAlarmIntent.setPackage(context.getPackageName());
        android.content.IntentFilter alarmFilter = new android.content.IntentFilter(TRUST_EXPIRED_ACTION);
        alarmFilter.addDataScheme(this.mAlarmIntent.getScheme());
        java.lang.String pathUri = this.mAlarmIntent.toUri(1);
        alarmFilter.addDataPath(pathUri, 0);
        android.content.IntentFilter trustableFilter = new android.content.IntentFilter("android.intent.action.SCREEN_OFF");
        scheduleRestart();
        this.mBound = context.bindServiceAsUser(intent, this.mConnection, android.hardware.audio.common.V2_0.AudioFormat.AAC_MAIN, user);
        if (this.mBound) {
            this.mContext.registerReceiver(this.mBroadcastReceiver, alarmFilter, PERMISSION, null, 2);
            this.mContext.registerReceiver(this.mTrustableDowngradeReceiver, trustableFilter);
        } else {
            android.util.Log.e(TAG, "Can't bind to TrustAgent " + this.mName.flattenToShortString());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onError(java.lang.Exception e) {
        android.util.Slog.w(TAG, "Exception ", e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onTrustTimeout() {
        try {
            if (this.mTrustAgentService != null) {
                this.mTrustAgentService.onTrustTimeout();
            }
        } catch (android.os.RemoteException e) {
            onError(e);
        }
    }

    public void onUnlockAttempt(boolean successful) {
        try {
            if (this.mTrustAgentService != null) {
                this.mTrustAgentService.onUnlockAttempt(successful);
            } else {
                this.mPendingSuccessfulUnlock = successful;
            }
        } catch (android.os.RemoteException e) {
            onError(e);
        }
    }

    public void onUserRequestedUnlock(boolean dismissKeyguard) {
        try {
            if (this.mTrustAgentService != null) {
                this.mTrustAgentService.onUserRequestedUnlock(dismissKeyguard);
            }
        } catch (android.os.RemoteException e) {
            onError(e);
        }
    }

    public void onUserMayRequestUnlock() {
        try {
            if (this.mTrustAgentService != null) {
                this.mTrustAgentService.onUserMayRequestUnlock();
            }
        } catch (android.os.RemoteException e) {
            onError(e);
        }
    }

    public void onUnlockLockout(int timeoutMs) {
        try {
            if (this.mTrustAgentService != null) {
                this.mTrustAgentService.onUnlockLockout(timeoutMs);
            }
        } catch (android.os.RemoteException e) {
            onError(e);
        }
    }

    public void onDeviceLocked() {
        this.mWithinSecurityLockdownWindow = false;
        try {
            if (this.mTrustAgentService != null) {
                this.mTrustAgentService.onDeviceLocked();
            }
        } catch (android.os.RemoteException e) {
            onError(e);
        }
    }

    public void onDeviceUnlocked() {
        try {
            if (this.mTrustAgentService != null) {
                this.mTrustAgentService.onDeviceUnlocked();
            }
        } catch (android.os.RemoteException e) {
            onError(e);
        }
    }

    public void onEscrowTokenActivated(long handle, int userId) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "onEscrowTokenActivated: " + handle + " user: " + userId);
        }
        if (this.mTrustAgentService != null) {
            try {
                this.mTrustAgentService.onTokenStateReceived(handle, 1);
            } catch (android.os.RemoteException e) {
                onError(e);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setCallback(android.service.trust.ITrustAgentServiceCallback callback) {
        try {
            if (this.mTrustAgentService != null) {
                this.mTrustAgentService.setCallback(callback);
            }
        } catch (android.os.RemoteException e) {
            onError(e);
        }
    }

    boolean updateDevicePolicyFeatures() {
        boolean trustDisabled = false;
        if (DEBUG) {
            android.util.Slog.d(TAG, "updateDevicePolicyFeatures(" + this.mName + ")");
        }
        try {
            if (this.mTrustAgentService != null) {
                android.app.admin.DevicePolicyManager dpm = (android.app.admin.DevicePolicyManager) this.mContext.getSystemService("device_policy");
                if ((dpm.getKeyguardDisabledFeatures(null, this.mUserId) & 16) != 0) {
                    java.util.List<android.os.PersistableBundle> config = dpm.getTrustAgentConfiguration(null, this.mName, this.mUserId);
                    trustDisabled = true;
                    if (DEBUG) {
                        android.util.Slog.d(TAG, "Detected trust agents disabled. Config = " + config);
                    }
                    if (config != null && config.size() > 0) {
                        if (DEBUG) {
                            android.util.Slog.d(TAG, "TrustAgent " + this.mName.flattenToShortString() + " disabled until it acknowledges " + config);
                        }
                        this.mSetTrustAgentFeaturesToken = new android.os.Binder();
                        this.mTrustAgentService.onConfigure(config, this.mSetTrustAgentFeaturesToken);
                    }
                } else {
                    this.mTrustAgentService.onConfigure(java.util.Collections.EMPTY_LIST, (android.os.IBinder) null);
                }
                long maxTimeToLock = dpm.getMaximumTimeToLock(null, this.mUserId);
                if (maxTimeToLock != this.mMaximumTimeToLock) {
                    this.mMaximumTimeToLock = maxTimeToLock;
                    if (this.mAlarmPendingIntent != null) {
                        this.mAlarmManager.cancel(this.mAlarmPendingIntent);
                        this.mAlarmPendingIntent = null;
                        this.mHandler.sendEmptyMessage(3);
                    }
                }
            }
        } catch (android.os.RemoteException e) {
            onError(e);
        }
        if (this.mTrustDisabledByDpm != trustDisabled) {
            this.mTrustDisabledByDpm = trustDisabled;
            this.mTrustManagerService.updateTrust(this.mUserId, 0);
        }
        return trustDisabled;
    }

    public boolean isTrusted() {
        return this.mTrusted && this.mManagingTrust && !this.mTrustDisabledByDpm;
    }

    public boolean isTrustable() {
        return this.mTrustable && this.mManagingTrust && !this.mTrustDisabledByDpm;
    }

    public boolean isTrustableOrWaitingForDowngrade() {
        return this.mWaitingForTrustableDowngrade || isTrustable();
    }

    public void setUntrustable() {
        this.mTrustable = false;
    }

    public void downgradeToTrustable() {
        if (this.mWaitingForTrustableDowngrade) {
            this.mWaitingForTrustableDowngrade = false;
            this.mTrusted = false;
            this.mTrustable = true;
            this.mTrustManagerService.updateTrust(this.mUserId, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSecurityWindowTimer() {
        this.mWithinSecurityLockdownWindow = true;
        long expiration = android.os.SystemClock.elapsedRealtime() + 15000;
        this.mAlarmManager.setExact(2, expiration, TAG, new android.app.AlarmManager.OnAlarmListener() { // from class: com.android.server.trust.TrustAgentWrapper.6
            @Override // android.app.AlarmManager.OnAlarmListener
            public void onAlarm() {
                com.android.server.trust.TrustAgentWrapper.this.mWithinSecurityLockdownWindow = false;
            }
        }, android.os.Handler.getMain());
    }

    public boolean isManagingTrust() {
        return this.mManagingTrust && !this.mTrustDisabledByDpm;
    }

    public java.lang.CharSequence getMessage() {
        return this.mMessage;
    }

    public boolean shouldDisplayTrustGrantedMessage() {
        return this.mDisplayTrustGrantedMessage;
    }

    public void destroy() {
        this.mHandler.removeMessages(4);
        if (!this.mBound) {
            return;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "TrustAgent unbound : " + this.mName.flattenToShortString());
        }
        this.mTrustManagerService.mArchive.logAgentStopped(this.mUserId, this.mName);
        this.mContext.unbindService(this.mConnection);
        this.mBound = false;
        this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        this.mContext.unregisterReceiver(this.mTrustableDowngradeReceiver);
        this.mTrustAgentService = null;
        this.mSetTrustAgentFeaturesToken = null;
        this.mHandler.sendEmptyMessage(2);
    }

    public boolean isConnected() {
        return this.mTrustAgentService != null;
    }

    public boolean isBound() {
        return this.mBound;
    }

    public long getScheduledRestartUptimeMillis() {
        return this.mScheduledRestartUptimeMillis;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleRestart() {
        this.mHandler.removeMessages(4);
        this.mScheduledRestartUptimeMillis = android.os.SystemClock.uptimeMillis() + 300000;
        this.mHandler.sendEmptyMessageAtTime(4, this.mScheduledRestartUptimeMillis);
    }
}
