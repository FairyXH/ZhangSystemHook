package com.android.server.os;

/* JADX INFO: loaded from: classes2.dex */
public class SchedulingPolicyService extends android.os.ISchedulingPolicyService.Stub {
    private static final java.lang.String[] MEDIA_PROCESS_NAMES = {"media.swcodec"};
    private static final int PRIORITY_MAX = 3;
    private static final int PRIORITY_MIN = 1;
    private static final java.lang.String TAG = "SchedulingPolicyService";
    private android.os.IBinder mClient;
    private final android.os.IBinder.DeathRecipient mDeathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.os.SchedulingPolicyService.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            com.android.server.os.SchedulingPolicyService.this.requestCpusetBoost(false, null);
        }
    };
    private int mBoostedPid = -1;

    public SchedulingPolicyService() {
        com.android.server.SystemServerInitThreadPool.submit(new java.lang.Runnable() { // from class: com.android.server.os.SchedulingPolicyService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0();
            }
        }, "SchedulingPolicyService.<init>");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        int[] nativePids;
        synchronized (this.mDeathRecipient) {
            if (this.mBoostedPid == -1 && (nativePids = android.os.Process.getPidsForCommands(MEDIA_PROCESS_NAMES)) != null && nativePids.length == 1) {
                this.mBoostedPid = nativePids[0];
                disableCpusetBoost(nativePids[0]);
            }
        }
    }

    public int requestPriority(int pid, int tid, int prio, boolean isForApp) {
        if (!isPermitted() || prio < 1 || prio > 3 || android.os.Process.getThreadGroupLeader(tid) != pid) {
            return -1;
        }
        if (android.os.Binder.getCallingUid() == 1041 && !isForApp && android.os.Process.getUidForPid(tid) != 1041) {
            return -1;
        }
        if (android.os.Binder.getCallingUid() != 1002) {
            try {
                android.os.Process.setThreadGroup(tid, !isForApp ? 4 : 6);
            } catch (java.lang.RuntimeException e) {
                android.util.Log.e(TAG, "Failed setThreadGroup: " + e);
                return -1;
            }
        }
        try {
            android.os.Process.setThreadScheduler(tid, com.android.server.policy.WindowManagerPolicy.COLOR_FADE_LAYER, prio);
            return 0;
        } catch (java.lang.RuntimeException e2) {
            android.util.Log.e(TAG, "Failed setThreadScheduler: " + e2);
            return -1;
        }
    }

    public int requestCpusetBoost(boolean enable, android.os.IBinder client) {
        if (android.os.Binder.getCallingPid() != android.os.Process.myPid() && android.os.Binder.getCallingUid() != 1013) {
            return -1;
        }
        int[] nativePids = android.os.Process.getPidsForCommands(MEDIA_PROCESS_NAMES);
        if (nativePids == null || nativePids.length != 1) {
            android.util.Log.e(TAG, "requestCpusetBoost: can't find media.codec process");
            return -1;
        }
        synchronized (this.mDeathRecipient) {
            if (enable) {
                return enableCpusetBoost(nativePids[0], client);
            }
            return disableCpusetBoost(nativePids[0]);
        }
    }

    private int enableCpusetBoost(int pid, android.os.IBinder client) {
        if (this.mBoostedPid == pid) {
            return 0;
        }
        this.mBoostedPid = -1;
        if (this.mClient != null) {
            try {
                this.mClient.unlinkToDeath(this.mDeathRecipient, 0);
            } catch (java.lang.Exception e) {
            } catch (java.lang.Throwable th) {
                this.mClient = null;
                throw th;
            }
            this.mClient = null;
        }
        try {
            client.linkToDeath(this.mDeathRecipient, 0);
            android.util.Log.i(TAG, "Moving " + pid + " to group 5");
            android.os.Process.setProcessGroup(pid, 5);
            this.mBoostedPid = pid;
            this.mClient = client;
            return 0;
        } catch (java.lang.Exception e2) {
            android.util.Log.e(TAG, "Failed enableCpusetBoost: " + e2);
            try {
                client.unlinkToDeath(this.mDeathRecipient, 0);
            } catch (java.lang.Exception e3) {
            }
            return -1;
        }
    }

    private int disableCpusetBoost(int pid) {
        int boostedPid = this.mBoostedPid;
        this.mBoostedPid = -1;
        if (this.mClient != null) {
            try {
                this.mClient.unlinkToDeath(this.mDeathRecipient, 0);
            } catch (java.lang.Exception e) {
            } catch (java.lang.Throwable th) {
                this.mClient = null;
                throw th;
            }
            this.mClient = null;
        }
        if (boostedPid == pid) {
            try {
                android.util.Log.i(TAG, "Moving " + pid + " back to group default");
                android.os.Process.setProcessGroup(pid, -1);
            } catch (java.lang.Exception e2) {
                android.util.Log.w(TAG, "Couldn't move pid " + pid + " back to group default");
            }
        }
        return 0;
    }

    private boolean isPermitted() {
        if (android.os.Binder.getCallingPid() == android.os.Process.myPid()) {
            return true;
        }
        switch (android.os.Binder.getCallingUid()) {
            case 1001:
            case 1002:
            case 1041:
            case 1047:
                break;
        }
        return true;
    }
}
