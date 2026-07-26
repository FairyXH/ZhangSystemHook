package com.android.server.locksettings.recoverablekeystore;

/* JADX INFO: loaded from: classes2.dex */
public class RecoverySnapshotListenersStorage {
    private static final java.lang.String TAG = "RecoverySnapshotLstnrs";
    private android.util.SparseArray<android.app.PendingIntent> mAgentIntents = new android.util.SparseArray<>();
    private android.util.ArraySet<java.lang.Integer> mAgentsWithPendingSnapshots = new android.util.ArraySet<>();

    public synchronized void setSnapshotListener(int recoveryAgentUid, android.app.PendingIntent intent) {
        android.util.Log.i(TAG, "Registered listener for agent with uid " + recoveryAgentUid);
        this.mAgentIntents.put(recoveryAgentUid, intent);
        if (this.mAgentsWithPendingSnapshots.contains(java.lang.Integer.valueOf(recoveryAgentUid))) {
            android.util.Log.i(TAG, "Snapshot already created for agent. Immediately triggering intent.");
            tryToSendIntent(recoveryAgentUid, intent);
        }
    }

    public synchronized boolean hasListener(int recoveryAgentUid) {
        return this.mAgentIntents.get(recoveryAgentUid) != null;
    }

    public synchronized void recoverySnapshotAvailable(int recoveryAgentUid) {
        android.app.PendingIntent intent = this.mAgentIntents.get(recoveryAgentUid);
        if (intent == null) {
            android.util.Log.i(TAG, "Snapshot available for agent " + recoveryAgentUid + " but agent has not yet initialized. Will notify agent when it does.");
            this.mAgentsWithPendingSnapshots.add(java.lang.Integer.valueOf(recoveryAgentUid));
        } else {
            tryToSendIntent(recoveryAgentUid, intent);
        }
    }

    private synchronized void tryToSendIntent(int recoveryAgentUid, android.app.PendingIntent intent) {
        try {
            intent.send();
            this.mAgentsWithPendingSnapshots.remove(java.lang.Integer.valueOf(recoveryAgentUid));
            android.util.Log.d(TAG, "Successfully notified listener.");
        } catch (android.app.PendingIntent.CanceledException e) {
            android.util.Log.e(TAG, "Failed to trigger PendingIntent for " + recoveryAgentUid, e);
            this.mAgentsWithPendingSnapshots.add(java.lang.Integer.valueOf(recoveryAgentUid));
        }
    }
}
