package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
public class ConversationStatusExpirationBroadcastReceiver extends android.content.BroadcastReceiver {
    static final java.lang.String ACTION = "ConversationStatusExpiration";
    static final java.lang.String EXTRA_USER_ID = "userId";
    static final int REQUEST_CODE = 10;
    static final java.lang.String SCHEME = "expStatus";

    void scheduleExpiration(android.content.Context context, int userId, java.lang.String pkg, java.lang.String conversationId, android.app.people.ConversationStatus status) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            android.app.PendingIntent pi = android.app.PendingIntent.getBroadcast(context, 10, new android.content.Intent(ACTION).setPackage(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME).setData(new android.net.Uri.Builder().scheme(SCHEME).appendPath(getKey(userId, pkg, conversationId, status)).build()).addFlags(268435456).putExtra("userId", userId), android.hardware.audio.common.V2_0.AudioFormat.DTS_HD);
            ((android.app.AlarmManager) context.getSystemService(android.app.AlarmManager.class)).setExactAndAllowWhileIdle(0, status.getEndTimeMillis(), pi);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    private static java.lang.String getKey(int userId, java.lang.String pkg, java.lang.String conversationId, android.app.people.ConversationStatus status) {
        return userId + pkg + conversationId + status.getId();
    }

    static android.content.IntentFilter getFilter() {
        android.content.IntentFilter conversationStatusFilter = new android.content.IntentFilter(ACTION);
        conversationStatusFilter.addDataScheme(SCHEME);
        return conversationStatusFilter;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(android.content.Context context, final android.content.Intent intent) {
        java.lang.String action = intent.getAction();
        if (action != null && ACTION.equals(action)) {
            new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.people.data.ConversationStatusExpirationBroadcastReceiver$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    com.android.server.people.data.ConversationStatusExpirationBroadcastReceiver.lambda$onReceive$0(intent);
                }
            }).start();
        }
    }

    static /* synthetic */ void lambda$onReceive$0(android.content.Intent intent) {
        com.android.server.people.PeopleServiceInternal peopleServiceInternal = (com.android.server.people.PeopleServiceInternal) com.android.server.LocalServices.getService(com.android.server.people.PeopleServiceInternal.class);
        peopleServiceInternal.pruneDataForUser(intent.getIntExtra("userId", android.app.ActivityManager.getCurrentUser()), new android.os.CancellationSignal());
    }
}
