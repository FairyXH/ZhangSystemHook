package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class PackageMonitorCallbackHelper {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = "PackageMonitorCallbackHelper";
    android.app.IActivityManager mActivityManager;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.os.RemoteCallbackList<android.os.IRemoteCallback> mCallbacks = new android.os.RemoteCallbackList<>();

    PackageMonitorCallbackHelper() {
    }

    public void registerPackageMonitorCallback(android.os.IRemoteCallback callback, int userId, int uid) {
        synchronized (this.mLock) {
            this.mCallbacks.register(callback, new com.android.server.pm.PackageMonitorCallbackHelper.RegisterUser(userId, uid));
        }
    }

    public void unregisterPackageMonitorCallback(android.os.IRemoteCallback callback) {
        synchronized (this.mLock) {
            this.mCallbacks.unregister(callback);
        }
    }

    public void onUserRemoved(int userId) {
        java.util.ArrayList<android.os.IRemoteCallback> targetUnRegisteredCallbacks = null;
        synchronized (this.mLock) {
            int registerCount = this.mCallbacks.getRegisteredCallbackCount();
            for (int i = 0; i < registerCount; i++) {
                com.android.server.pm.PackageMonitorCallbackHelper.RegisterUser registerUser = (com.android.server.pm.PackageMonitorCallbackHelper.RegisterUser) this.mCallbacks.getRegisteredCallbackCookie(i);
                if (registerUser.getUserId() == userId) {
                    android.os.IRemoteCallback callback = this.mCallbacks.getRegisteredCallbackItem(i);
                    if (targetUnRegisteredCallbacks == null) {
                        targetUnRegisteredCallbacks = new java.util.ArrayList<>();
                    }
                    targetUnRegisteredCallbacks.add(callback);
                }
            }
        }
        if (targetUnRegisteredCallbacks != null && targetUnRegisteredCallbacks.size() > 0) {
            int count = targetUnRegisteredCallbacks.size();
            for (int i2 = 0; i2 < count; i2++) {
                unregisterPackageMonitorCallback(targetUnRegisteredCallbacks.get(i2));
            }
        }
    }

    public void notifyPackageAddedForNewUsers(java.lang.String packageName, int appId, int[] userIds, int[] instantUserIds, boolean isArchived, int dataLoaderType, android.util.SparseArray<int[]> broadcastAllowList, android.os.Handler handler) throws java.lang.Throwable {
        android.os.Bundle extras = new android.os.Bundle(2);
        int uid = android.os.UserHandle.getUid(com.android.internal.util.ArrayUtils.isEmpty(userIds) ? instantUserIds[0] : userIds[0], appId);
        extras.putInt("android.intent.extra.UID", uid);
        if (isArchived) {
            extras.putBoolean("android.intent.extra.ARCHIVAL", true);
        }
        extras.putInt("android.content.pm.extra.DATA_LOADER_TYPE", dataLoaderType);
        notifyPackageMonitor("android.intent.action.PACKAGE_ADDED", packageName, extras, userIds, instantUserIds, broadcastAllowList, handler, null);
    }

    public void notifyResourcesChanged(boolean mediaStatus, boolean replacing, java.lang.String[] pkgNames, int[] uids, android.os.Handler handler) throws java.lang.Throwable {
        android.os.Bundle extras = new android.os.Bundle();
        extras.putStringArray("android.intent.extra.changed_package_list", pkgNames);
        extras.putIntArray("android.intent.extra.changed_uid_list", uids);
        if (replacing) {
            extras.putBoolean("android.intent.extra.REPLACING", replacing);
        }
        java.lang.String action = mediaStatus ? "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE" : "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE";
        notifyPackageMonitor(action, null, extras, null, null, null, handler, null);
    }

    public void notifyPackageChanged(java.lang.String packageName, boolean dontKillApp, java.util.ArrayList<java.lang.String> componentNames, int packageUid, java.lang.String reason, int[] userIds, int[] instantUserIds, android.util.SparseArray<int[]> broadcastAllowList, android.os.Handler handler) throws java.lang.Throwable {
        android.os.Bundle extras = new android.os.Bundle(4);
        extras.putString("android.intent.extra.changed_component_name", componentNames.get(0));
        java.lang.String[] nameList = new java.lang.String[componentNames.size()];
        componentNames.toArray(nameList);
        extras.putStringArray("android.intent.extra.changed_component_name_list", nameList);
        extras.putBoolean("android.intent.extra.DONT_KILL_APP", dontKillApp);
        extras.putInt("android.intent.extra.UID", packageUid);
        if (reason != null) {
            extras.putString("android.intent.extra.REASON", reason);
        }
        notifyPackageMonitor("android.intent.action.PACKAGE_CHANGED", packageName, extras, userIds, instantUserIds, broadcastAllowList, handler, null);
    }

    public void notifyPackageMonitor(java.lang.String action, java.lang.String pkg, android.os.Bundle extras, int[] userIds, int[] instantUserIds, android.util.SparseArray<int[]> broadcastAllowList, android.os.Handler handler, java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> filterExtras) throws java.lang.Throwable {
        int[] resolvedUserIds;
        if (!isAllowedCallbackAction(action)) {
            return;
        }
        if (userIds == null) {
            try {
                if (this.mActivityManager == null) {
                    this.mActivityManager = android.app.ActivityManager.getService();
                }
                if (this.mActivityManager == null) {
                    return;
                } else {
                    resolvedUserIds = this.mActivityManager.getRunningUserIds();
                }
            } catch (android.os.RemoteException e) {
                return;
            }
        } else {
            resolvedUserIds = userIds;
        }
        if (com.android.internal.util.ArrayUtils.isEmpty(instantUserIds)) {
            doNotifyCallbacksByAction(action, pkg, extras, resolvedUserIds, broadcastAllowList, handler, filterExtras);
        } else {
            doNotifyCallbacksByAction(action, pkg, extras, instantUserIds, broadcastAllowList, handler, filterExtras);
        }
    }

    void notifyPackageMonitorWithIntent(android.content.Intent intent, int userId, int[] broadcastAllowList, android.os.Handler handler) {
        if (!isAllowedCallbackAction(intent.getAction())) {
            return;
        }
        doNotifyCallbacksByIntent(intent, userId, broadcastAllowList, handler);
    }

    private static boolean isAllowedCallbackAction(java.lang.String action) {
        return android.text.TextUtils.equals(action, "android.intent.action.PACKAGE_ADDED") || android.text.TextUtils.equals(action, "android.intent.action.PACKAGE_REMOVED") || android.text.TextUtils.equals(action, "android.intent.action.PACKAGE_CHANGED") || android.text.TextUtils.equals(action, "android.intent.action.UID_REMOVED") || android.text.TextUtils.equals(action, "android.intent.action.PACKAGES_SUSPENDED") || android.text.TextUtils.equals(action, "android.intent.action.PACKAGES_UNSUSPENDED") || android.text.TextUtils.equals(action, "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE") || android.text.TextUtils.equals(action, "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE") || android.text.TextUtils.equals(action, "android.intent.action.PACKAGE_DATA_CLEARED") || android.text.TextUtils.equals(action, "android.intent.action.PACKAGE_RESTARTED") || android.text.TextUtils.equals(action, "android.intent.action.PACKAGE_UNSTOPPED");
    }

    private void doNotifyCallbacksByIntent(android.content.Intent intent, int userId, int[] broadcastAllowList, android.os.Handler handler) {
        android.os.RemoteCallbackList<android.os.IRemoteCallback> callbacks;
        synchronized (this.mLock) {
            callbacks = this.mCallbacks;
        }
        doNotifyCallbacks(callbacks, intent, userId, broadcastAllowList, handler, null);
    }

    private void doNotifyCallbacksByAction(java.lang.String action, java.lang.String pkg, android.os.Bundle extras, int[] userIds, android.util.SparseArray<int[]> broadcastAllowList, android.os.Handler handler, java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> filterExtrasFunction) throws java.lang.Throwable {
        android.os.RemoteCallbackList<android.os.IRemoteCallback> callbacks;
        synchronized (this.mLock) {
            try {
                callbacks = this.mCallbacks;
            } catch (java.lang.Throwable th) {
                th = th;
                while (true) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        for (int userId : userIds) {
            android.content.Intent intent = new android.content.Intent(action, pkg != null ? android.net.Uri.fromParts("package", pkg, null) : null);
            if (extras != null) {
                intent.putExtras(extras);
            }
            int uid = intent.getIntExtra("android.intent.extra.UID", -1);
            if (uid >= 0 && android.os.UserHandle.getUserId(uid) != userId) {
                intent.putExtra("android.intent.extra.UID", android.os.UserHandle.getUid(userId, android.os.UserHandle.getAppId(uid)));
            }
            intent.putExtra("android.intent.extra.user_handle", userId);
            int[] allowUids = broadcastAllowList != null ? broadcastAllowList.get(userId) : null;
            doNotifyCallbacks(callbacks, intent, userId, allowUids, handler, filterExtrasFunction);
        }
    }

    private void doNotifyCallbacks(final android.os.RemoteCallbackList<android.os.IRemoteCallback> callbacks, final android.content.Intent intent, final int userId, final int[] allowUids, android.os.Handler handler, final java.util.function.BiFunction<java.lang.Integer, android.os.Bundle, android.os.Bundle> filterExtrasFunction) {
        handler.post(new java.lang.Runnable() { // from class: com.android.server.pm.PackageMonitorCallbackHelper$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$doNotifyCallbacks$1(callbacks, userId, allowUids, intent, filterExtrasFunction);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doNotifyCallbacks$1(android.os.RemoteCallbackList callbacks, final int userId, final int[] allowUids, final android.content.Intent intent, final java.util.function.BiFunction filterExtrasFunction) {
        callbacks.broadcast(new java.util.function.BiConsumer() { // from class: com.android.server.pm.PackageMonitorCallbackHelper$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                this.f$0.lambda$doNotifyCallbacks$0(userId, allowUids, intent, filterExtrasFunction, (android.os.IRemoteCallback) obj, obj2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doNotifyCallbacks$0(int userId, int[] allowUids, android.content.Intent intent, java.util.function.BiFunction filterExtrasFunction, android.os.IRemoteCallback callback, java.lang.Object user) {
        android.os.Bundle extras;
        com.android.server.pm.PackageMonitorCallbackHelper.RegisterUser registerUser = (com.android.server.pm.PackageMonitorCallbackHelper.RegisterUser) user;
        if (registerUser.getUserId() != -1 && registerUser.getUserId() != userId) {
            return;
        }
        int registerUid = registerUser.getUid();
        if (allowUids != null && registerUid != 1000 && !com.android.internal.util.ArrayUtils.contains(allowUids, registerUid)) {
            return;
        }
        android.content.Intent newIntent = intent;
        if (filterExtrasFunction != null && (extras = intent.getExtras()) != null) {
            android.os.Bundle filteredExtras = (android.os.Bundle) filterExtrasFunction.apply(java.lang.Integer.valueOf(registerUid), extras);
            if (filteredExtras == null) {
                return;
            }
            newIntent = new android.content.Intent(newIntent);
            newIntent.replaceExtras(filteredExtras);
        }
        invokeCallback(callback, newIntent);
    }

    private void invokeCallback(android.os.IRemoteCallback callback, android.content.Intent intent) {
        try {
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putParcelable("android.content.pm.extra.EXTRA_PACKAGE_MONITOR_CALLBACK_RESULT", intent);
            callback.sendResult(bundle);
        } catch (android.os.RemoteException e) {
        }
    }

    private final class RegisterUser {
        int mUid;
        int mUserId;

        RegisterUser(int userId, int uid) {
            this.mUid = uid;
            this.mUserId = userId;
        }

        public int getUid() {
            return this.mUid;
        }

        public int getUserId() {
            return this.mUserId;
        }
    }
}
