package com.android.server.logcat;

/* JADX INFO: loaded from: classes2.dex */
public final class LogcatManagerService extends com.android.server.SystemService {
    private static final boolean DEBUG = false;
    public static final java.lang.String EXTRA_CALLBACK = "EXTRA_CALLBACK";
    private static final int MSG_APPROVE_LOG_ACCESS = 1;
    private static final int MSG_DECLINE_LOG_ACCESS = 2;
    private static final int MSG_LOG_ACCESS_FINISHED = 3;
    private static final int MSG_LOG_ACCESS_REQUESTED = 0;
    private static final int MSG_LOG_ACCESS_STATUS_EXPIRED = 5;
    private static final int MSG_PENDING_TIMEOUT = 4;
    static final int PENDING_CONFIRMATION_TIMEOUT_MILLIS;
    private static final int STATUS_APPROVED = 2;
    private static final int STATUS_DECLINED = 3;
    static final int STATUS_EXPIRATION_TIMEOUT_MILLIS = 60000;
    private static final int STATUS_NEW_REQUEST = 0;
    private static final int STATUS_PENDING = 1;
    private static final java.lang.String TAG = "LogcatManagerService";
    private static final java.lang.String TARGET_ACTIVITY_NAME = "com.android.systemui.logcat.LogAccessDialogActivity";
    private static final java.lang.String TARGET_PACKAGE_NAME = "com.android.systemui";
    private final java.util.Map<com.android.server.logcat.LogcatManagerService.LogAccessClient, java.lang.Integer> mActiveLogAccessCount;
    private android.app.ActivityManagerInternal mActivityManagerInternal;
    private final com.android.server.logcat.LogcatManagerService.BinderService mBinderService;
    private final java.util.function.Supplier<java.lang.Long> mClock;
    private final android.content.Context mContext;
    private final com.android.server.logcat.LogcatManagerService.LogAccessDialogCallback mDialogCallback;
    private final android.os.Handler mHandler;
    private final com.android.server.logcat.LogcatManagerService.Injector mInjector;
    private final java.util.Map<com.android.server.logcat.LogcatManagerService.LogAccessClient, com.android.server.logcat.LogcatManagerService.LogAccessStatus> mLogAccessStatus;
    private android.os.ILogd mLogdService;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface LogAccessRequestStatus {
    }

    static {
        PENDING_CONFIRMATION_TIMEOUT_MILLIS = android.os.Build.IS_DEBUGGABLE ? com.android.server.policy.EventLogTags.SCREEN_TOGGLED : 400000;
    }

    private static final class LogAccessClient {
        final java.lang.String mPackageName;
        final int mUid;

        LogAccessClient(int uid, java.lang.String packageName) {
            this.mUid = uid;
            this.mPackageName = packageName;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.logcat.LogcatManagerService.LogAccessClient)) {
                return false;
            }
            com.android.server.logcat.LogcatManagerService.LogAccessClient that = (com.android.server.logcat.LogcatManagerService.LogAccessClient) o;
            return this.mUid == that.mUid && java.util.Objects.equals(this.mPackageName, that.mPackageName);
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.mUid), this.mPackageName);
        }

        public java.lang.String toString() {
            return "LogAccessClient{mUid=" + this.mUid + ", mPackageName=" + this.mPackageName + '}';
        }
    }

    private static final class LogAccessRequest {
        final int mFd;
        final int mGid;
        final int mPid;
        final int mUid;

        private LogAccessRequest(int uid, int gid, int pid, int fd) {
            this.mUid = uid;
            this.mGid = gid;
            this.mPid = pid;
            this.mFd = fd;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.logcat.LogcatManagerService.LogAccessRequest)) {
                return false;
            }
            com.android.server.logcat.LogcatManagerService.LogAccessRequest that = (com.android.server.logcat.LogcatManagerService.LogAccessRequest) o;
            return this.mUid == that.mUid && this.mGid == that.mGid && this.mPid == that.mPid && this.mFd == that.mFd;
        }

        public int hashCode() {
            return java.util.Objects.hash(java.lang.Integer.valueOf(this.mUid), java.lang.Integer.valueOf(this.mGid), java.lang.Integer.valueOf(this.mPid), java.lang.Integer.valueOf(this.mFd));
        }

        public java.lang.String toString() {
            return "LogAccessRequest{mUid=" + this.mUid + ", mGid=" + this.mGid + ", mPid=" + this.mPid + ", mFd=" + this.mFd + '}';
        }
    }

    private static final class LogAccessStatus {
        final java.util.List<com.android.server.logcat.LogcatManagerService.LogAccessRequest> mPendingRequests;
        int mStatus;

        private LogAccessStatus() {
            this.mStatus = 0;
            this.mPendingRequests = new java.util.ArrayList();
        }
    }

    private final class BinderService extends android.os.logcat.ILogcatManagerService.Stub {
        private BinderService() {
        }

        public void startThread(int uid, int gid, int pid, int fd) {
            com.android.server.logcat.LogcatManagerService.LogAccessRequest logAccessRequest = new com.android.server.logcat.LogcatManagerService.LogAccessRequest(uid, gid, pid, fd);
            android.os.Message msg = com.android.server.logcat.LogcatManagerService.this.mHandler.obtainMessage(0, logAccessRequest);
            com.android.server.logcat.LogcatManagerService.this.mHandler.sendMessageAtTime(msg, ((java.lang.Long) com.android.server.logcat.LogcatManagerService.this.mClock.get()).longValue());
        }

        public void finishThread(int uid, int gid, int pid, int fd) {
            com.android.server.logcat.LogcatManagerService.LogAccessRequest logAccessRequest = new com.android.server.logcat.LogcatManagerService.LogAccessRequest(uid, gid, pid, fd);
            android.os.Message msg = com.android.server.logcat.LogcatManagerService.this.mHandler.obtainMessage(3, logAccessRequest);
            com.android.server.logcat.LogcatManagerService.this.mHandler.sendMessageAtTime(msg, ((java.lang.Long) com.android.server.logcat.LogcatManagerService.this.mClock.get()).longValue());
        }
    }

    final class LogAccessDialogCallback extends com.android.internal.app.ILogAccessDialogCallback.Stub {
        LogAccessDialogCallback() {
        }

        public void approveAccessForClient(int uid, java.lang.String packageName) {
            com.android.server.logcat.LogcatManagerService.LogAccessClient client = new com.android.server.logcat.LogcatManagerService.LogAccessClient(uid, packageName);
            android.os.Message msg = com.android.server.logcat.LogcatManagerService.this.mHandler.obtainMessage(1, client);
            com.android.server.logcat.LogcatManagerService.this.mHandler.sendMessageAtTime(msg, ((java.lang.Long) com.android.server.logcat.LogcatManagerService.this.mClock.get()).longValue());
        }

        public void declineAccessForClient(int uid, java.lang.String packageName) {
            com.android.server.logcat.LogcatManagerService.LogAccessClient client = new com.android.server.logcat.LogcatManagerService.LogAccessClient(uid, packageName);
            android.os.Message msg = com.android.server.logcat.LogcatManagerService.this.mHandler.obtainMessage(2, client);
            com.android.server.logcat.LogcatManagerService.this.mHandler.sendMessageAtTime(msg, ((java.lang.Long) com.android.server.logcat.LogcatManagerService.this.mClock.get()).longValue());
        }
    }

    private android.os.ILogd getLogdService() {
        if (this.mLogdService == null) {
            this.mLogdService = this.mInjector.getLogdService();
        }
        return this.mLogdService;
    }

    private static class LogAccessRequestHandler extends android.os.Handler {
        private final com.android.server.logcat.LogcatManagerService mService;

        LogAccessRequestHandler(android.os.Looper looper, com.android.server.logcat.LogcatManagerService service) {
            super(looper);
            this.mService = service;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    com.android.server.logcat.LogcatManagerService.LogAccessRequest request = (com.android.server.logcat.LogcatManagerService.LogAccessRequest) msg.obj;
                    this.mService.onLogAccessRequested(request);
                    break;
                case 1:
                    com.android.server.logcat.LogcatManagerService.LogAccessClient client = (com.android.server.logcat.LogcatManagerService.LogAccessClient) msg.obj;
                    this.mService.onAccessApprovedForClient(client);
                    break;
                case 2:
                    com.android.server.logcat.LogcatManagerService.LogAccessClient client2 = (com.android.server.logcat.LogcatManagerService.LogAccessClient) msg.obj;
                    this.mService.onAccessDeclinedForClient(client2);
                    break;
                case 3:
                    com.android.server.logcat.LogcatManagerService.LogAccessRequest request2 = (com.android.server.logcat.LogcatManagerService.LogAccessRequest) msg.obj;
                    this.mService.onLogAccessFinished(request2);
                    break;
                case 4:
                    com.android.server.logcat.LogcatManagerService.LogAccessClient client3 = (com.android.server.logcat.LogcatManagerService.LogAccessClient) msg.obj;
                    this.mService.onPendingTimeoutExpired(client3);
                    break;
                case 5:
                    com.android.server.logcat.LogcatManagerService.LogAccessClient client4 = (com.android.server.logcat.LogcatManagerService.LogAccessClient) msg.obj;
                    this.mService.onAccessStatusExpired(client4);
                    break;
            }
        }
    }

    static class Injector {
        Injector() {
        }

        protected java.util.function.Supplier<java.lang.Long> createClock() {
            return new java.util.function.Supplier() { // from class: com.android.server.logcat.LogcatManagerService$Injector$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final java.lang.Object get() {
                    return java.lang.Long.valueOf(android.os.SystemClock.uptimeMillis());
                }
            };
        }

        protected android.os.Looper getLooper() {
            return android.os.Looper.getMainLooper();
        }

        protected android.os.ILogd getLogdService() {
            return android.os.ILogd.Stub.asInterface(android.os.ServiceManager.getService("logd"));
        }
    }

    public LogcatManagerService(android.content.Context context) {
        this(context, new com.android.server.logcat.LogcatManagerService.Injector());
    }

    public LogcatManagerService(android.content.Context context, com.android.server.logcat.LogcatManagerService.Injector injector) {
        super(context);
        this.mLogAccessStatus = new android.util.ArrayMap();
        this.mActiveLogAccessCount = new android.util.ArrayMap();
        this.mContext = context;
        this.mInjector = injector;
        this.mClock = injector.createClock();
        this.mBinderService = new com.android.server.logcat.LogcatManagerService.BinderService();
        this.mDialogCallback = new com.android.server.logcat.LogcatManagerService.LogAccessDialogCallback();
        this.mHandler = new com.android.server.logcat.LogcatManagerService.LogAccessRequestHandler(injector.getLooper(), this);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        try {
            this.mActivityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
            publishBinderService("logcat", this.mBinderService);
        } catch (java.lang.Throwable t) {
            android.util.Slog.e(TAG, "Could not start the LogcatManagerService.", t);
        }
    }

    com.android.server.logcat.LogcatManagerService.LogAccessDialogCallback getDialogCallback() {
        return this.mDialogCallback;
    }

    android.os.logcat.ILogcatManagerService getBinderService() {
        return this.mBinderService;
    }

    private com.android.server.logcat.LogcatManagerService.LogAccessClient getClientForRequest(com.android.server.logcat.LogcatManagerService.LogAccessRequest request) {
        java.lang.String packageName = getPackageName(request);
        if (packageName == null) {
            return null;
        }
        return new com.android.server.logcat.LogcatManagerService.LogAccessClient(request.mUid, packageName);
    }

    private java.lang.String getPackageName(com.android.server.logcat.LogcatManagerService.LogAccessRequest request) {
        android.content.pm.PackageManager pm = this.mContext.getPackageManager();
        if (pm == null) {
            android.util.Slog.e(TAG, "PackageManager is null, declining the logd access");
            return null;
        }
        java.lang.String[] packageNames = pm.getPackagesForUid(request.mUid);
        if (com.android.internal.util.ArrayUtils.isEmpty(packageNames)) {
            android.util.Slog.e(TAG, "Unknown calling package name, declining the logd access");
            return null;
        }
        if (this.mActivityManagerInternal != null) {
            int pid = request.mPid;
            java.lang.String packageName = this.mActivityManagerInternal.getPackageNameByPid(pid);
            while (true) {
                if ((packageName != null && com.android.internal.util.ArrayUtils.contains(packageNames, packageName)) || pid == -1) {
                    break;
                }
                pid = android.os.Process.getParentPid(pid);
                packageName = this.mActivityManagerInternal.getPackageNameByPid(pid);
            }
            if (packageName != null && com.android.internal.util.ArrayUtils.contains(packageNames, packageName)) {
                return packageName;
            }
        }
        java.util.Arrays.sort(packageNames);
        java.lang.String firstPackageName = packageNames[0];
        if (firstPackageName == null || firstPackageName.isEmpty()) {
            android.util.Slog.e(TAG, "Unknown calling package name, declining the logd access");
            return null;
        }
        return firstPackageName;
    }

    void onLogAccessRequested(com.android.server.logcat.LogcatManagerService.LogAccessRequest request) {
        com.android.server.logcat.LogcatManagerService.LogAccessClient client = getClientForRequest(request);
        if (client == null) {
            declineRequest(request);
        }
        com.android.server.logcat.LogcatManagerService.LogAccessStatus logAccessStatus = this.mLogAccessStatus.get(client);
        if (logAccessStatus == null) {
            logAccessStatus = new com.android.server.logcat.LogcatManagerService.LogAccessStatus();
            this.mLogAccessStatus.put(client, logAccessStatus);
        }
        switch (logAccessStatus.mStatus) {
            case 0:
                logAccessStatus.mPendingRequests.add(request);
                processNewLogAccessRequest(client);
                break;
            case 1:
                logAccessStatus.mPendingRequests.add(request);
                break;
            case 2:
                approveRequest(client, request);
                break;
            case 3:
                declineRequest(request);
                break;
        }
    }

    private boolean shouldShowConfirmationDialog(com.android.server.logcat.LogcatManagerService.LogAccessClient client) {
        int procState = this.mActivityManagerInternal.getUidProcessState(client.mUid);
        return procState == 2;
    }

    private void processNewLogAccessRequest(com.android.server.logcat.LogcatManagerService.LogAccessClient client) {
        boolean isInstrumented = this.mActivityManagerInternal.getInstrumentationSourceUid(client.mUid) != -1;
        if (isInstrumented) {
            onAccessApprovedForClient(client);
            return;
        }
        if (!shouldShowConfirmationDialog(client)) {
            onAccessDeclinedForClient(client);
            return;
        }
        com.android.server.logcat.LogcatManagerService.LogAccessStatus logAccessStatus = this.mLogAccessStatus.get(client);
        logAccessStatus.mStatus = 1;
        this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(4, client), this.mClock.get().longValue() + ((long) PENDING_CONFIRMATION_TIMEOUT_MILLIS));
        android.content.Intent mIntent = createIntent(client);
        mIntent.setFlags(268435456);
        mIntent.setComponent(new android.content.ComponentName(TARGET_PACKAGE_NAME, TARGET_ACTIVITY_NAME));
        this.mContext.startActivityAsUser(mIntent, android.os.UserHandle.SYSTEM);
    }

    void onAccessApprovedForClient(com.android.server.logcat.LogcatManagerService.LogAccessClient client) {
        scheduleStatusExpiry(client);
        com.android.server.logcat.LogcatManagerService.LogAccessStatus logAccessStatus = this.mLogAccessStatus.get(client);
        if (logAccessStatus != null) {
            for (com.android.server.logcat.LogcatManagerService.LogAccessRequest request : logAccessStatus.mPendingRequests) {
                approveRequest(client, request);
            }
            logAccessStatus.mStatus = 2;
            logAccessStatus.mPendingRequests.clear();
        }
    }

    void onAccessDeclinedForClient(com.android.server.logcat.LogcatManagerService.LogAccessClient client) {
        scheduleStatusExpiry(client);
        com.android.server.logcat.LogcatManagerService.LogAccessStatus logAccessStatus = this.mLogAccessStatus.get(client);
        if (logAccessStatus != null) {
            for (com.android.server.logcat.LogcatManagerService.LogAccessRequest request : logAccessStatus.mPendingRequests) {
                declineRequest(request);
            }
            logAccessStatus.mStatus = 3;
            logAccessStatus.mPendingRequests.clear();
        }
    }

    private void scheduleStatusExpiry(com.android.server.logcat.LogcatManagerService.LogAccessClient client) {
        this.mHandler.removeMessages(4, client);
        this.mHandler.removeMessages(5, client);
        this.mHandler.sendMessageAtTime(this.mHandler.obtainMessage(5, client), this.mClock.get().longValue() + 60000);
    }

    void onPendingTimeoutExpired(com.android.server.logcat.LogcatManagerService.LogAccessClient client) {
        com.android.server.logcat.LogcatManagerService.LogAccessStatus logAccessStatus = this.mLogAccessStatus.get(client);
        if (logAccessStatus != null && logAccessStatus.mStatus == 1) {
            onAccessDeclinedForClient(client);
        }
    }

    void onAccessStatusExpired(com.android.server.logcat.LogcatManagerService.LogAccessClient client) {
        this.mLogAccessStatus.remove(client);
    }

    void onLogAccessFinished(com.android.server.logcat.LogcatManagerService.LogAccessRequest request) {
        com.android.server.logcat.LogcatManagerService.LogAccessClient client = getClientForRequest(request);
        int activeCount = this.mActiveLogAccessCount.getOrDefault(client, 1).intValue() - 1;
        if (activeCount == 0) {
            this.mActiveLogAccessCount.remove(client);
        } else {
            this.mActiveLogAccessCount.put(client, java.lang.Integer.valueOf(activeCount));
        }
    }

    private void approveRequest(com.android.server.logcat.LogcatManagerService.LogAccessClient client, com.android.server.logcat.LogcatManagerService.LogAccessRequest request) {
        try {
            try {
                getLogdService().approve(request.mUid, request.mGid, request.mPid, request.mFd);
            } catch (android.os.DeadObjectException e) {
                android.util.Slog.w(TAG, "Logd connection no longer valid while approving, trying once more.");
                this.mLogdService = null;
                getLogdService().approve(request.mUid, request.mGid, request.mPid, request.mFd);
            }
            java.lang.Integer activeCount = this.mActiveLogAccessCount.getOrDefault(client, 0);
            this.mActiveLogAccessCount.put(client, java.lang.Integer.valueOf(activeCount.intValue() + 1));
        } catch (android.os.RemoteException e2) {
            android.util.Slog.e(TAG, "Fails to call remote functions", e2);
        }
    }

    private void declineRequest(com.android.server.logcat.LogcatManagerService.LogAccessRequest request) {
        try {
            try {
                getLogdService().decline(request.mUid, request.mGid, request.mPid, request.mFd);
            } catch (android.os.DeadObjectException e) {
                android.util.Slog.w(TAG, "Logd connection no longer valid while declining, trying once more.");
                this.mLogdService = null;
                getLogdService().decline(request.mUid, request.mGid, request.mPid, request.mFd);
            }
        } catch (android.os.RemoteException e2) {
            android.util.Slog.e(TAG, "Fails to call remote functions", e2);
        }
    }

    public android.content.Intent createIntent(com.android.server.logcat.LogcatManagerService.LogAccessClient client) {
        android.content.Intent intent = new android.content.Intent();
        intent.setFlags(268468224);
        intent.putExtra("android.intent.extra.PACKAGE_NAME", client.mPackageName);
        intent.putExtra("android.intent.extra.UID", client.mUid);
        intent.putExtra(EXTRA_CALLBACK, this.mDialogCallback.asBinder());
        return intent;
    }
}
