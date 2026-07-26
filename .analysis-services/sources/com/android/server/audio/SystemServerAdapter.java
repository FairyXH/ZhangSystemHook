package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class SystemServerAdapter {
    protected final android.content.Context mContext;

    protected SystemServerAdapter(android.content.Context context) {
        this.mContext = context;
    }

    static final com.android.server.audio.SystemServerAdapter getDefaultAdapter(android.content.Context context) {
        java.util.Objects.requireNonNull(context);
        return new com.android.server.audio.SystemServerAdapter(context);
    }

    public boolean isPrivileged() {
        return true;
    }

    public void sendMicrophoneMuteChangedIntent() {
        this.mContext.sendBroadcastAsUser(new android.content.Intent("android.media.action.MICROPHONE_MUTE_CHANGED").setFlags(1073741824), android.os.UserHandle.ALL);
    }

    public void sendDeviceBecomingNoisyIntent() {
        if (this.mContext == null) {
            return;
        }
        android.content.Intent intent = new android.content.Intent("android.media.AUDIO_BECOMING_NOISY");
        intent.addFlags(67108864);
        intent.addFlags(268435456);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }

    public void broadcastStickyIntentToCurrentProfileGroup(android.content.Intent intent) {
        int[] profileIds = ((android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class)).getCurrentProfileIds();
        for (int userId : profileIds) {
            android.app.ActivityManager.broadcastStickyIntent(intent, userId);
        }
    }

    void registerUserStartedReceiver(android.content.Context context) {
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.USER_STARTED");
        context.registerReceiverAsUser(new android.content.BroadcastReceiver() { // from class: com.android.server.audio.SystemServerAdapter.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context2, android.content.Intent intent) {
                int userId;
                if (!"android.intent.action.USER_STARTED".equals(intent.getAction()) || (userId = intent.getIntExtra("android.intent.extra.user_handle", -10000)) == -10000) {
                    return;
                }
                android.os.UserManager userManager = (android.os.UserManager) context2.getSystemService(android.os.UserManager.class);
                android.content.pm.UserInfo profileParent = userManager.getProfileParent(userId);
                if (profileParent == null) {
                    return;
                }
                com.android.server.audio.SystemServerAdapter.this.broadcastProfileParentStickyIntent(context2, "android.media.action.HDMI_AUDIO_PLUG", userId, profileParent.id);
                com.android.server.audio.SystemServerAdapter.this.broadcastProfileParentStickyIntent(context2, "android.intent.action.HEADSET_PLUG", userId, profileParent.id);
            }
        }, android.os.UserHandle.ALL, filter, null, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastProfileParentStickyIntent(android.content.Context context, java.lang.String intentAction, int profileId, int parentId) {
        android.content.Intent intent = context.registerReceiverAsUser(null, android.os.UserHandle.of(parentId), new android.content.IntentFilter(intentAction), null, null);
        if (intent != null) {
            android.app.ActivityManager.broadcastStickyIntent(intent, profileId);
        }
    }

    void broadcastMasterMuteStatus(boolean muted) {
        android.content.Intent intent = new android.content.Intent("android.media.MASTER_MUTE_CHANGED_ACTION");
        intent.putExtra("android.media.EXTRA_MASTER_VOLUME_MUTED", muted);
        intent.addFlags(872415232);
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            this.mContext.sendStickyBroadcastAsUser(intent, android.os.UserHandle.ALL);
        } finally {
            android.os.Binder.restoreCallingIdentity(ident);
        }
    }
}
