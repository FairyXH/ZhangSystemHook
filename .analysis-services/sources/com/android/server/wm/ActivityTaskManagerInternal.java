package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ActivityTaskManagerInternal {
    public static final int APP_TRANSITION_RECENTS_ANIM = 5;
    public static final int APP_TRANSITION_SNAPSHOT = 4;
    public static final int APP_TRANSITION_SPLASH_SCREEN = 1;
    public static final int APP_TRANSITION_TIMEOUT = 3;
    public static final int APP_TRANSITION_WINDOWS_DRAWN = 2;
    public static final java.lang.String ASSIST_ACTIVITY_ID = "activityId";
    public static final java.lang.String ASSIST_KEY_CONTENT = "content";
    public static final java.lang.String ASSIST_KEY_DATA = "data";
    public static final java.lang.String ASSIST_KEY_RECEIVER_EXTRAS = "receiverExtras";
    public static final java.lang.String ASSIST_KEY_STRUCTURE = "structure";
    public static final java.lang.String ASSIST_TASK_ID = "taskId";

    public interface PackageConfigurationUpdater {
        boolean commit();

        com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater setGrammaticalGender(int i);

        com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater setLocales(android.os.LocaleList localeList);

        com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater setNightMode(int i);
    }

    public interface ScreenObserver {
        void onAwakeStateChanged(boolean z);

        void onKeyguardStateChanged(boolean z);
    }

    public abstract boolean attachApplication(com.android.server.wm.WindowProcessController windowProcessController) throws android.os.RemoteException;

    public abstract boolean canCloseSystemDialogs(int i, int i2);

    public abstract boolean canGcNow();

    public abstract boolean canShowErrorDialogs();

    public abstract boolean checkCanCloseSystemDialogs(int i, int i2, java.lang.String str);

    public abstract void cleanupDisabledPackageComponents(java.lang.String str, java.util.Set<java.lang.String> set, int i, boolean z);

    public abstract void cleanupRecentTasksForUser(int i);

    public abstract void clearHeavyWeightProcessIfEquals(com.android.server.wm.WindowProcessController windowProcessController);

    public abstract void clearLockedTasks(java.lang.String str);

    public abstract void clearPendingResultForActivity(android.os.IBinder iBinder, java.lang.ref.WeakReference<com.android.server.am.PendingIntentRecord> weakReference);

    public abstract void closeSystemDialogs(java.lang.String str);

    public abstract android.content.res.CompatibilityInfo compatibilityInfoForPackage(android.content.pm.ApplicationInfo applicationInfo);

    public abstract com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater createPackageConfigurationUpdater();

    public abstract com.android.server.wm.ActivityTaskManagerInternal.PackageConfigurationUpdater createPackageConfigurationUpdater(java.lang.String str, int i);

    public abstract void dump(java.lang.String str, java.io.FileDescriptor fileDescriptor, java.io.PrintWriter printWriter, java.lang.String[] strArr, int i, boolean z, boolean z2, java.lang.String str2, int i2);

    public abstract boolean dumpActivity(java.io.FileDescriptor fileDescriptor, java.io.PrintWriter printWriter, java.lang.String str, java.lang.String[] strArr, int i, boolean z, boolean z2, boolean z3, int i2, int i3);

    public abstract void dumpForOom(java.io.PrintWriter printWriter);

    public abstract boolean dumpForProcesses(java.io.FileDescriptor fileDescriptor, java.io.PrintWriter printWriter, boolean z, java.lang.String str, int i, boolean z2, boolean z3, int i2);

    public abstract void enableScreenAfterBoot(boolean z);

    public abstract void finishHeavyWeightApp();

    public abstract int finishTopCrashedActivities(com.android.server.wm.WindowProcessController windowProcessController, java.lang.String str);

    public abstract void flushRecentTasks();

    public abstract android.content.ComponentName getActivityName(android.os.IBinder iBinder);

    public abstract java.util.List<android.app.ActivityManager.AppTask> getAppTasks(java.lang.String str, int i);

    public abstract com.android.server.wm.ActivityTaskManagerInternal.PackageConfig getApplicationConfig(java.lang.String str, int i);

    public abstract com.android.server.wm.ActivityTaskManagerInternal.ActivityTokens getAttachedNonFinishingActivityForTask(int i, android.os.IBinder iBinder);

    public abstract int getDisplayId(android.os.IBinder iBinder);

    public abstract android.content.ComponentName getHomeActivityForUser(int i);

    public abstract android.content.Intent getHomeIntent();

    public abstract android.content.IIntentSender getIntentSender(int i, java.lang.String str, java.lang.String str2, int i2, int i3, android.os.IBinder iBinder, java.lang.String str3, int i4, android.content.Intent[] intentArr, java.lang.String[] strArr, int i5, android.os.Bundle bundle);

    public abstract com.android.server.wm.ActivityMetricsLaunchObserverRegistry getLaunchObserverRegistry();

    public abstract android.app.ActivityManager.RecentTaskInfo getMostRecentTaskFromBackground();

    public abstract com.android.server.wm.ActivityServiceConnectionsHolder getServiceConnectionsHolder(android.os.IBinder iBinder);

    public abstract android.window.TaskSnapshot getTaskSnapshotBlocking(int i, boolean z);

    public abstract int getTaskToShowPermissionDialogOn(java.lang.String str, int i);

    public abstract com.android.server.wm.WindowProcessController getTopApp();

    public abstract int getTopProcessState();

    public abstract java.util.List<com.android.server.wm.ActivityAssistInfo> getTopVisibleActivities();

    public abstract android.os.IBinder getUriPermissionOwnerForActivity(android.os.IBinder iBinder);

    public abstract boolean handleAppCrashInActivityController(java.lang.String str, int i, java.lang.String str2, java.lang.String str3, long j, java.lang.String str4, java.lang.Runnable runnable);

    public abstract void handleAppDied(com.android.server.wm.WindowProcessController windowProcessController, boolean z, java.lang.Runnable runnable);

    public abstract boolean hasResumedActivity(int i);

    public abstract boolean hasSystemAlertWindowPermission(int i, int i2, java.lang.String str);

    public abstract boolean isAssistDataAllowed();

    public abstract boolean isBaseOfLockedTask(java.lang.String str);

    public abstract boolean isCallerRecents(int i);

    public abstract boolean isGetTasksAllowed(java.lang.String str, int i, int i2);

    public abstract boolean isRecentsComponentHomeActivity(int i);

    public abstract boolean isShuttingDown();

    public abstract boolean isSleeping();

    public abstract boolean isUidForeground(int i);

    public abstract void loadRecentTasksForUser(int i);

    public abstract void notifyActiveDreamChanged(android.content.ComponentName componentName);

    public abstract void notifyActiveVoiceInteractionServiceChanged(android.content.ComponentName componentName);

    public abstract void notifyLockedProfile(int i);

    public abstract void onCleanUpApplicationRecord(com.android.server.wm.WindowProcessController windowProcessController);

    public abstract boolean onForceStopPackage(java.lang.String str, boolean z, boolean z2, int i);

    public abstract void onHandleAppCrash(com.android.server.wm.WindowProcessController windowProcessController);

    public abstract void onLocalVoiceInteractionStarted(android.os.IBinder iBinder, android.service.voice.IVoiceInteractionSession iVoiceInteractionSession, com.android.internal.app.IVoiceInteractor iVoiceInteractor);

    public abstract void onPackageAdded(java.lang.String str, boolean z);

    public abstract void onPackageDataCleared(java.lang.String str, int i);

    public abstract void onPackageReplaced(android.content.pm.ApplicationInfo applicationInfo);

    public abstract void onPackageUninstalled(java.lang.String str, int i);

    public abstract void onPackagesSuspendedChanged(java.lang.String[] strArr, boolean z, int i);

    public abstract void onProcessAdded(com.android.server.wm.WindowProcessController windowProcessController);

    public abstract void onProcessMapped(int i, com.android.server.wm.WindowProcessController windowProcessController);

    public abstract void onProcessRemoved(java.lang.String str, int i);

    public abstract void onProcessUnMapped(int i);

    public abstract void onUidActive(int i, int i2);

    public abstract void onUidInactive(int i);

    public abstract void onUidProcStateChanged(int i, int i2);

    public abstract void onUserStopped(int i);

    public abstract void preBindApplication(com.android.server.wm.WindowProcessController windowProcessController);

    public abstract void preBindApplication(com.android.server.wm.WindowProcessController windowProcessController, android.content.res.Configuration configuration, android.os.Bundle bundle);

    public abstract void registerActivityStartInterceptor(int i, com.android.server.wm.ActivityInterceptorCallback activityInterceptorCallback);

    public abstract void registerCompatScaleProvider(int i, com.android.server.wm.CompatScaleProvider compatScaleProvider);

    public abstract void registerScreenObserver(com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver screenObserver);

    public abstract void registerTaskStackListener(android.app.ITaskStackListener iTaskStackListener);

    public abstract void removeRecentTasksByPackageName(java.lang.String str, int i);

    public abstract void removeUser(int i);

    public abstract void restartTaskActivityProcessIfVisible(int i, java.lang.String str);

    public abstract void resumeTopActivities(boolean z);

    public abstract void scheduleDestroyAllActivities(java.lang.String str);

    public abstract void sendActivityResult(int i, android.os.IBinder iBinder, java.lang.String str, int i2, int i3, android.content.Intent intent);

    public abstract void setAccessibilityServiceUids(android.util.IntArray intArray);

    public abstract void setAllowAppSwitches(java.lang.String str, int i, int i2);

    public abstract void setBackgroundActivityStartCallback(com.android.server.wm.BackgroundActivityStartCallback backgroundActivityStartCallback);

    public abstract void setCompanionAppUids(int i, java.util.Set<java.lang.Integer> set);

    public abstract void setDeviceOwnerUid(int i);

    public abstract void setProfileApp(java.lang.String str);

    public abstract void setProfileOwnerUids(java.util.Set<java.lang.Integer> set);

    public abstract void setProfileProc(com.android.server.wm.WindowProcessController windowProcessController);

    public abstract void setProfilerInfo(android.app.ProfilerInfo profilerInfo);

    public abstract void setVr2dDisplayId(int i);

    public abstract boolean showStrictModeViolationDialog();

    public abstract void showSystemReadyErrorDialogsIfNeeded();

    public abstract boolean shuttingDown(boolean z, int i);

    public abstract int startActivitiesAsPackage(java.lang.String str, java.lang.String str2, int i, android.content.Intent[] intentArr, android.os.Bundle bundle);

    public abstract int startActivitiesInPackage(int i, int i2, int i3, java.lang.String str, java.lang.String str2, android.content.Intent[] intentArr, java.lang.String[] strArr, android.os.IBinder iBinder, com.android.server.wm.SafeActivityOptions safeActivityOptions, int i4, boolean z, com.android.server.am.PendingIntentRecord pendingIntentRecord, android.app.BackgroundStartPrivileges backgroundStartPrivileges);

    public abstract int startActivityAsUser(android.app.IApplicationThread iApplicationThread, java.lang.String str, java.lang.String str2, android.content.Intent intent, android.os.IBinder iBinder, int i, android.os.Bundle bundle, int i2);

    public abstract int startActivityInPackage(int i, int i2, int i3, java.lang.String str, java.lang.String str2, android.content.Intent intent, java.lang.String str3, android.os.IBinder iBinder, java.lang.String str4, int i4, int i5, com.android.server.wm.SafeActivityOptions safeActivityOptions, int i6, com.android.server.wm.Task task, java.lang.String str5, boolean z, com.android.server.am.PendingIntentRecord pendingIntentRecord, android.app.BackgroundStartPrivileges backgroundStartPrivileges);

    public abstract int startActivityWithScreenshot(android.content.Intent intent, java.lang.String str, int i, int i2, android.os.IBinder iBinder, android.os.Bundle bundle, int i3);

    public abstract void startConfirmDeviceCredentialIntent(android.content.Intent intent, android.os.Bundle bundle);

    public abstract android.app.IAppTask startDreamActivity(android.content.Intent intent, int i, int i2);

    public abstract boolean startHomeActivity(int i, java.lang.String str);

    public abstract boolean startHomeOnAllDisplays(int i, java.lang.String str);

    public abstract boolean startHomeOnDisplay(int i, java.lang.String str, int i2, boolean z, boolean z2);

    public abstract boolean switchUser(int i, com.android.server.am.UserState userState);

    public abstract void unregisterActivityStartInterceptor(int i);

    public abstract void unregisterCompatScaleProvider(int i);

    public abstract void unregisterScreenObserver(com.android.server.wm.ActivityTaskManagerInternal.ScreenObserver screenObserver);

    public abstract void unregisterTaskStackListener(android.app.ITaskStackListener iTaskStackListener);

    public abstract void updateTopComponentForFactoryTest();

    public abstract void updateUserConfiguration();

    public abstract boolean useTopSchedGroupForTopProcess();

    public abstract void writeActivitiesToProto(android.util.proto.ProtoOutputStream protoOutputStream);

    public abstract void writeProcessesToProto(android.util.proto.ProtoOutputStream protoOutputStream, java.lang.String str, int i, boolean z);

    public final class ActivityTokens {
        private final android.os.IBinder mActivityToken;
        private final android.app.IApplicationThread mAppThread;
        private final android.os.IBinder mAssistToken;
        private final android.os.IBinder mShareableActivityToken;
        private final int mUid;

        public ActivityTokens(android.os.IBinder activityToken, android.os.IBinder assistToken, android.app.IApplicationThread appThread, android.os.IBinder shareableActivityToken, int uid) {
            this.mActivityToken = activityToken;
            this.mAssistToken = assistToken;
            this.mAppThread = appThread;
            this.mShareableActivityToken = shareableActivityToken;
            this.mUid = uid;
        }

        public android.os.IBinder getActivityToken() {
            return this.mActivityToken;
        }

        public android.os.IBinder getAssistToken() {
            return this.mAssistToken;
        }

        public android.os.IBinder getShareableActivityToken() {
            return this.mShareableActivityToken;
        }

        public android.app.IApplicationThread getApplicationThread() {
            return this.mAppThread;
        }

        public int getUid() {
            return this.mUid;
        }
    }

    public static class PackageConfig {
        public final java.lang.Integer mGrammaticalGender;
        public final android.os.LocaleList mLocales;
        public final java.lang.Integer mNightMode;

        public PackageConfig(java.lang.Integer nightMode, android.os.LocaleList locales, java.lang.Integer grammaticalGender) {
            this.mNightMode = nightMode;
            this.mLocales = locales;
            this.mGrammaticalGender = grammaticalGender;
        }

        public java.lang.String toString() {
            return "PackageConfig: nightMode " + this.mNightMode + " locales " + this.mLocales;
        }
    }
}
