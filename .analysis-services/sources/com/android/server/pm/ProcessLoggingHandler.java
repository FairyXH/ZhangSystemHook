package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class ProcessLoggingHandler extends android.os.Handler {
    private static final int CHECKSUM_TYPE = 8;
    private static final java.lang.String TAG = "ProcessLoggingHandler";
    private final java.util.concurrent.Executor mExecutor;
    private final android.util.ArrayMap<java.lang.String, com.android.server.pm.ProcessLoggingHandler.LoggingInfo> mLoggingInfo;

    static class LoggingInfo {
        public java.lang.String apkHash = null;
        public java.util.List<android.os.Bundle> pendingLogEntries = new java.util.ArrayList();

        LoggingInfo() {
        }
    }

    ProcessLoggingHandler() {
        super(com.android.internal.os.BackgroundThread.getHandler().getLooper());
        this.mExecutor = new android.os.HandlerExecutor(this);
        this.mLoggingInfo = new android.util.ArrayMap<>();
    }

    void logAppProcessStart(android.content.Context context, android.content.pm.PackageManagerInternal pmi, java.lang.String apkFile, java.lang.String packageName, java.lang.String processName, int uid, java.lang.String seinfo, int pid) {
        boolean requestChecksums;
        final com.android.server.pm.ProcessLoggingHandler.LoggingInfo loggingInfo;
        com.android.server.pm.ProcessLoggingHandler.LoggingInfo loggingInfo2;
        com.android.server.pm.ProcessLoggingHandler.LoggingInfo loggingInfo3;
        android.os.Bundle data = new android.os.Bundle();
        data.putLong("startTimestamp", java.lang.System.currentTimeMillis());
        data.putString("processName", processName);
        data.putInt("uid", uid);
        data.putString("seinfo", seinfo);
        data.putInt(com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_PID, pid);
        if (apkFile == null) {
            enqueueSecurityLogEvent(data, "No APK");
            return;
        }
        synchronized (this.mLoggingInfo) {
            com.android.server.pm.ProcessLoggingHandler.LoggingInfo cached = this.mLoggingInfo.get(apkFile);
            requestChecksums = cached == null;
            if (requestChecksums) {
                cached = new com.android.server.pm.ProcessLoggingHandler.LoggingInfo();
                this.mLoggingInfo.put(apkFile, cached);
            }
            loggingInfo = cached;
        }
        synchronized (loggingInfo) {
            try {
                if (!android.text.TextUtils.isEmpty(loggingInfo.apkHash)) {
                    try {
                        enqueueSecurityLogEvent(data, loggingInfo.apkHash);
                        return;
                    } catch (java.lang.Throwable th) {
                        th = th;
                        loggingInfo2 = loggingInfo;
                    }
                } else {
                    loggingInfo.pendingLogEntries.add(data);
                    if (!requestChecksums) {
                        return;
                    }
                    try {
                        loggingInfo3 = loggingInfo;
                    } catch (java.lang.Throwable th2) {
                        t = th2;
                        loggingInfo3 = loggingInfo;
                    }
                    try {
                        pmi.requestChecksums(packageName, false, 0, 8, null, new android.content.pm.IOnChecksumsReadyListener.Stub() { // from class: com.android.server.pm.ProcessLoggingHandler.1
                            public void onChecksumsReady(java.util.List<android.content.pm.ApkChecksum> checksums) throws android.os.RemoteException {
                                com.android.server.pm.ProcessLoggingHandler.this.processChecksums(loggingInfo, checksums);
                            }
                        }, context.getUserId(), this.mExecutor, this);
                        return;
                    } catch (java.lang.Throwable th3) {
                        t = th3;
                        android.util.Slog.e(TAG, "requestChecksums() failed", t);
                        enqueueProcessChecksum(loggingInfo3, null);
                        return;
                    }
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
                loggingInfo2 = loggingInfo;
            }
            while (true) {
                try {
                    throw th;
                } catch (java.lang.Throwable th5) {
                    th = th5;
                }
            }
        }
    }

    void processChecksums(com.android.server.pm.ProcessLoggingHandler.LoggingInfo loggingInfo, java.util.List<android.content.pm.ApkChecksum> checksums) {
        int size = checksums.size();
        for (int i = 0; i < size; i++) {
            android.content.pm.ApkChecksum checksum = checksums.get(i);
            if (checksum.getType() == 8) {
                processChecksum(loggingInfo, checksum.getValue());
                return;
            }
        }
        android.util.Slog.e(TAG, "requestChecksums() failed to return SHA256, see logs for details.");
        processChecksum(loggingInfo, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enqueueProcessChecksum$0(com.android.server.pm.ProcessLoggingHandler.LoggingInfo loggingInfo) {
        processChecksum(loggingInfo, null);
    }

    void enqueueProcessChecksum(final com.android.server.pm.ProcessLoggingHandler.LoggingInfo loggingInfo, byte[] hash) {
        post(new java.lang.Runnable() { // from class: com.android.server.pm.ProcessLoggingHandler$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$enqueueProcessChecksum$0(loggingInfo);
            }
        });
    }

    void processChecksum(com.android.server.pm.ProcessLoggingHandler.LoggingInfo loggingInfo, byte[] hash) {
        java.lang.String apkHash;
        if (hash != null) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            for (byte b : hash) {
                sb.append(java.lang.String.format("%02x", java.lang.Byte.valueOf(b)));
            }
            apkHash = sb.toString();
        } else {
            apkHash = "Failed to count APK hash";
        }
        synchronized (loggingInfo) {
            if (android.text.TextUtils.isEmpty(loggingInfo.apkHash)) {
                loggingInfo.apkHash = apkHash;
                java.util.List<android.os.Bundle> pendingLogEntries = loggingInfo.pendingLogEntries;
                loggingInfo.pendingLogEntries = null;
                if (pendingLogEntries != null) {
                    for (android.os.Bundle data : pendingLogEntries) {
                        lambda$enqueueSecurityLogEvent$1(data, apkHash);
                    }
                }
            }
        }
    }

    void invalidateBaseApkHash(java.lang.String apkFile) {
        synchronized (this.mLoggingInfo) {
            this.mLoggingInfo.remove(apkFile);
        }
    }

    void enqueueSecurityLogEvent(final android.os.Bundle data, final java.lang.String apkHash) {
        post(new java.lang.Runnable() { // from class: com.android.server.pm.ProcessLoggingHandler$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$enqueueSecurityLogEvent$1(data, apkHash);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: renamed from: logSecurityLogEvent, reason: merged with bridge method [inline-methods] */
    public void lambda$enqueueSecurityLogEvent$1(android.os.Bundle bundle, java.lang.String apkHash) {
        long startTimestamp = bundle.getLong("startTimestamp");
        java.lang.String processName = bundle.getString("processName");
        int uid = bundle.getInt("uid");
        java.lang.String seinfo = bundle.getString("seinfo");
        int pid = bundle.getInt(com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_PID);
        android.app.admin.SecurityLog.writeEvent(210005, new java.lang.Object[]{processName, java.lang.Long.valueOf(startTimestamp), java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(pid), seinfo, apkHash});
    }
}
