package com.android.server.app;

/* JADX INFO: loaded from: classes.dex */
final class GameTaskInfoProvider {
    private static final java.lang.String TAG = "GameTaskInfoProvider";
    private static final int TASK_INFO_CACHE_MAX_SIZE = 50;
    private final android.app.IActivityTaskManager mActivityTaskManager;
    private final com.android.server.app.GameClassifier mGameClassifier;
    private final android.os.UserHandle mUserHandle;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.LruCache<java.lang.Integer, com.android.server.app.GameTaskInfo> mGameTaskInfoCache = new android.util.LruCache<>(50);

    GameTaskInfoProvider(android.os.UserHandle userHandle, android.app.IActivityTaskManager activityTaskManager, com.android.server.app.GameClassifier gameClassifier) {
        this.mUserHandle = userHandle;
        this.mActivityTaskManager = activityTaskManager;
        this.mGameClassifier = gameClassifier;
    }

    com.android.server.app.GameTaskInfo get(int taskId) {
        synchronized (this.mLock) {
            com.android.server.app.GameTaskInfo cachedTaskInfo = this.mGameTaskInfoCache.get(java.lang.Integer.valueOf(taskId));
            if (cachedTaskInfo != null) {
                return cachedTaskInfo;
            }
            android.app.ActivityManager.RunningTaskInfo runningTaskInfo = getRunningTaskInfo(taskId);
            if (runningTaskInfo == null || runningTaskInfo.baseActivity == null) {
                return null;
            }
            return generateGameInfo(taskId, runningTaskInfo.baseActivity);
        }
    }

    com.android.server.app.GameTaskInfo get(int taskId, android.content.ComponentName componentName) {
        synchronized (this.mLock) {
            com.android.server.app.GameTaskInfo cachedTaskInfo = this.mGameTaskInfoCache.get(java.lang.Integer.valueOf(taskId));
            if (cachedTaskInfo != null) {
                if (!cachedTaskInfo.mComponentName.equals(componentName)) {
                    return cachedTaskInfo;
                }
                android.util.Slog.w(TAG, "Found cached task info for taskId " + taskId + " but cached component name " + cachedTaskInfo.mComponentName + " does not match " + componentName);
            }
            return generateGameInfo(taskId, componentName);
        }
    }

    android.app.ActivityManager.RunningTaskInfo getRunningTaskInfo(int taskId) {
        try {
            java.util.List<android.app.ActivityManager.RunningTaskInfo> runningTaskInfos = this.mActivityTaskManager.getTasks(Integer.MAX_VALUE, false, false, -1);
            for (android.app.ActivityManager.RunningTaskInfo taskInfo : runningTaskInfos) {
                if (taskInfo.taskId == taskId) {
                    return taskInfo;
                }
            }
            return null;
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Failed to fetch running tasks");
            return null;
        }
    }

    private com.android.server.app.GameTaskInfo generateGameInfo(int taskId, android.content.ComponentName componentName) {
        com.android.server.app.GameTaskInfo gameTaskInfo = new com.android.server.app.GameTaskInfo(taskId, this.mGameClassifier.isGame(componentName.getPackageName(), this.mUserHandle), componentName);
        synchronized (this.mLock) {
            this.mGameTaskInfoCache.put(java.lang.Integer.valueOf(taskId), gameTaskInfo);
        }
        return gameTaskInfo;
    }
}
