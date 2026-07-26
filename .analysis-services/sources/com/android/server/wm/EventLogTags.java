package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class EventLogTags {
    public static final int FOLDSCREENSWITCHING_KEYGUARD_ONDRAWN = 38507;
    public static final int FOLDSCREENSWITCHING_PENDING_STATE = 38508;
    public static final int FOLDSCREENSWITCHING_POWEROFF_END = 38502;
    public static final int FOLDSCREENSWITCHING_POWEROFF_START = 38501;
    public static final int FOLDSCREENSWITCHING_POWERON_END = 38504;
    public static final int FOLDSCREENSWITCHING_POWERON_START = 38503;
    public static final int FOLDSCREENSWITCHING_SCREENTURNINGON_END = 38506;
    public static final int FOLDSCREENSWITCHING_SCREENTURNINGON_START = 38505;
    public static final int FOLDSCREENSWITCHING_SENSOR = 38500;
    public static final int IMF_REMOVE_IME_SCREENSHOT = 32005;
    public static final int IMF_SHOW_IME_SCREENSHOT = 32004;
    public static final int IMF_UPDATE_IME_PARENT = 32003;
    public static final int WM_ACTIVITY_LAUNCH_TIME = 30009;
    public static final int WM_ADD_TO_STOPPING = 30066;
    public static final int WM_BACK_NAVI_CANCELED = 31100;
    public static final int WM_BOOT_ANIMATION_DONE = 31007;
    public static final int WM_CREATE_ACTIVITY = 30005;
    public static final int WM_CREATE_TASK = 30004;
    public static final int WM_DESTROY_ACTIVITY = 30018;
    public static final int WM_ENTER_PIP = 38000;
    public static final int WM_FAILED_TO_PAUSE = 30012;
    public static final int WM_FINISH_ACTIVITY = 30001;
    public static final int WM_FOCUSED_ROOT_TASK = 30044;
    public static final int WM_NEW_INTENT = 30003;
    public static final int WM_NO_SURFACE_MEMORY = 31000;
    public static final int WM_PAUSE_ACTIVITY = 30013;
    public static final int WM_RELAUNCH_ACTIVITY = 30020;
    public static final int WM_RELAUNCH_RESUME_ACTIVITY = 30019;
    public static final int WM_RESTART_ACTIVITY = 30006;
    public static final int WM_RESUME_ACTIVITY = 30007;
    public static final int WM_SET_KEYGUARD_OCCLUDED = 31008;
    public static final int WM_SET_KEYGUARD_SHOWN = 30067;
    public static final int WM_SET_REQUESTED_ORIENTATION = 31006;
    public static final int WM_SET_RESUMED_ACTIVITY = 30043;
    public static final int WM_STOP_ACTIVITY = 30048;
    public static final int WM_TASK_CREATED = 31001;
    public static final int WM_TASK_MOVED = 31002;
    public static final int WM_TASK_REMOVED = 31003;
    public static final int WM_TASK_TO_FRONT = 30002;
    public static final int WM_TASK_WINDOWING_MODE_CHANGED = 38509;
    public static final int WM_TF_CREATED = 31004;
    public static final int WM_TF_REMOVED = 31005;
    public static final int WM_WALLPAPER_SURFACE = 33001;

    private EventLogTags() {
    }

    public static void writeWmFinishActivity(int user, int token, int taskId, java.lang.String componentName, java.lang.String reason) {
        android.util.EventLog.writeEvent(WM_FINISH_ACTIVITY, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(token), java.lang.Integer.valueOf(taskId), componentName, reason);
    }

    public static void writeWmTaskToFront(int user, int task, int displayId) {
        android.util.EventLog.writeEvent(WM_TASK_TO_FRONT, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(task), java.lang.Integer.valueOf(displayId));
    }

    public static void writeWmNewIntent(int user, int token, int taskId, java.lang.String componentName, java.lang.String action, java.lang.String mimeType, java.lang.String uri, int flags) {
        android.util.EventLog.writeEvent(WM_NEW_INTENT, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(token), java.lang.Integer.valueOf(taskId), componentName, action, mimeType, uri, java.lang.Integer.valueOf(flags));
    }

    public static void writeWmCreateTask(int user, int taskId, int rootTaskId, int displayId) {
        android.util.EventLog.writeEvent(WM_CREATE_TASK, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(taskId), java.lang.Integer.valueOf(rootTaskId), java.lang.Integer.valueOf(displayId));
    }

    public static void writeWmCreateActivity(int user, int token, int taskId, java.lang.String componentName, java.lang.String action, java.lang.String mimeType, java.lang.String uri, int flags) {
        android.util.EventLog.writeEvent(WM_CREATE_ACTIVITY, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(token), java.lang.Integer.valueOf(taskId), componentName, action, mimeType, uri, java.lang.Integer.valueOf(flags));
    }

    public static void writeWmRestartActivity(int user, int token, int taskId, java.lang.String componentName) {
        android.util.EventLog.writeEvent(WM_RESTART_ACTIVITY, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(token), java.lang.Integer.valueOf(taskId), componentName);
    }

    public static void writeWmResumeActivity(int user, int token, int taskId, java.lang.String componentName) {
        android.util.EventLog.writeEvent(WM_RESUME_ACTIVITY, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(token), java.lang.Integer.valueOf(taskId), componentName);
    }

    public static void writeWmActivityLaunchTime(int user, int token, java.lang.String componentName, long time) {
        android.util.EventLog.writeEvent(WM_ACTIVITY_LAUNCH_TIME, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(token), componentName, java.lang.Long.valueOf(time));
    }

    public static void writeWmFailedToPause(int user, int token, java.lang.String wantingToPause, java.lang.String currentlyPausing) {
        android.util.EventLog.writeEvent(WM_FAILED_TO_PAUSE, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(token), wantingToPause, currentlyPausing);
    }

    public static void writeWmPauseActivity(int user, int token, java.lang.String componentName, java.lang.String userLeaving, java.lang.String reason) {
        android.util.EventLog.writeEvent(WM_PAUSE_ACTIVITY, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(token), componentName, userLeaving, reason);
    }

    public static void writeWmDestroyActivity(int user, int token, int taskId, java.lang.String componentName, java.lang.String reason) {
        android.util.EventLog.writeEvent(WM_DESTROY_ACTIVITY, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(token), java.lang.Integer.valueOf(taskId), componentName, reason);
    }

    public static void writeWmRelaunchResumeActivity(int user, int token, int taskId, java.lang.String componentName, java.lang.String configMask) {
        android.util.EventLog.writeEvent(WM_RELAUNCH_RESUME_ACTIVITY, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(token), java.lang.Integer.valueOf(taskId), componentName, configMask);
    }

    public static void writeWmRelaunchActivity(int user, int token, int taskId, java.lang.String componentName, java.lang.String configMask) {
        android.util.EventLog.writeEvent(WM_RELAUNCH_ACTIVITY, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(token), java.lang.Integer.valueOf(taskId), componentName, configMask);
    }

    public static void writeWmSetResumedActivity(int user, java.lang.String componentName, java.lang.String reason) {
        android.util.EventLog.writeEvent(WM_SET_RESUMED_ACTIVITY, java.lang.Integer.valueOf(user), componentName, reason);
    }

    public static void writeWmFocusedRootTask(int user, int displayId, int focusedRootTaskId, int lastFocusedRootTaskId, java.lang.String reason) {
        android.util.EventLog.writeEvent(WM_FOCUSED_ROOT_TASK, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(focusedRootTaskId), java.lang.Integer.valueOf(lastFocusedRootTaskId), reason);
    }

    public static void writeWmStopActivity(int user, int token, java.lang.String componentName) {
        android.util.EventLog.writeEvent(WM_STOP_ACTIVITY, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(token), componentName);
    }

    public static void writeWmAddToStopping(int user, int token, java.lang.String componentName, java.lang.String reason) {
        android.util.EventLog.writeEvent(WM_ADD_TO_STOPPING, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(token), componentName, reason);
    }

    public static void writeWmSetKeyguardShown(int displayId, int keyguardshowing, int aodshowing, int keyguardgoingaway, int occluded, java.lang.String reason) {
        android.util.EventLog.writeEvent(WM_SET_KEYGUARD_SHOWN, java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(keyguardshowing), java.lang.Integer.valueOf(aodshowing), java.lang.Integer.valueOf(keyguardgoingaway), java.lang.Integer.valueOf(occluded), reason);
    }

    public static void writeWmNoSurfaceMemory(java.lang.String window, int pid, java.lang.String operation) {
        android.util.EventLog.writeEvent(WM_NO_SURFACE_MEMORY, window, java.lang.Integer.valueOf(pid), operation);
    }

    public static void writeWmTaskCreated(int taskid) {
        android.util.EventLog.writeEvent(WM_TASK_CREATED, taskid);
    }

    public static void writeWmTaskMoved(int taskid, int rootTaskId, int displayId, int totop, int index) {
        android.util.EventLog.writeEvent(WM_TASK_MOVED, java.lang.Integer.valueOf(taskid), java.lang.Integer.valueOf(rootTaskId), java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(totop), java.lang.Integer.valueOf(index));
    }

    public static void writeWmTaskRemoved(int taskid, int rootTaskId, int displayId, java.lang.String reason) {
        android.util.EventLog.writeEvent(WM_TASK_REMOVED, java.lang.Integer.valueOf(taskid), java.lang.Integer.valueOf(rootTaskId), java.lang.Integer.valueOf(displayId), reason);
    }

    public static void writeWmTfCreated(int token, int taskid) {
        android.util.EventLog.writeEvent(WM_TF_CREATED, java.lang.Integer.valueOf(token), java.lang.Integer.valueOf(taskid));
    }

    public static void writeWmTfRemoved(int token, int taskid) {
        android.util.EventLog.writeEvent(WM_TF_REMOVED, java.lang.Integer.valueOf(token), java.lang.Integer.valueOf(taskid));
    }

    public static void writeWmSetRequestedOrientation(int orientation, java.lang.String componentName) {
        android.util.EventLog.writeEvent(WM_SET_REQUESTED_ORIENTATION, java.lang.Integer.valueOf(orientation), componentName);
    }

    public static void writeWmBootAnimationDone(long time) {
        android.util.EventLog.writeEvent(WM_BOOT_ANIMATION_DONE, time);
    }

    public static void writeWmSetKeyguardOccluded(int occluded, int animate, int transit, java.lang.String channel) {
        android.util.EventLog.writeEvent(WM_SET_KEYGUARD_OCCLUDED, java.lang.Integer.valueOf(occluded), java.lang.Integer.valueOf(animate), java.lang.Integer.valueOf(transit), channel);
    }

    public static void writeWmBackNaviCanceled(java.lang.String reason) {
        android.util.EventLog.writeEvent(WM_BACK_NAVI_CANCELED, reason);
    }

    public static void writeImfUpdateImeParent(java.lang.String surfaceName) {
        android.util.EventLog.writeEvent(IMF_UPDATE_IME_PARENT, surfaceName);
    }

    public static void writeImfShowImeScreenshot(java.lang.String targetWindow, int transition, java.lang.String surfacePosition) {
        android.util.EventLog.writeEvent(IMF_SHOW_IME_SCREENSHOT, targetWindow, java.lang.Integer.valueOf(transition), surfacePosition);
    }

    public static void writeImfRemoveImeScreenshot(java.lang.String targetWindow) {
        android.util.EventLog.writeEvent(IMF_REMOVE_IME_SCREENSHOT, targetWindow);
    }

    public static void writeWmWallpaperSurface(int displayId, int visible, java.lang.String target) {
        android.util.EventLog.writeEvent(WM_WALLPAPER_SURFACE, java.lang.Integer.valueOf(displayId), java.lang.Integer.valueOf(visible), target);
    }

    public static void writeWmEnterPip(int user, int token, java.lang.String componentName, java.lang.String isAutoEnter) {
        android.util.EventLog.writeEvent(WM_ENTER_PIP, java.lang.Integer.valueOf(user), java.lang.Integer.valueOf(token), componentName, isAutoEnter);
    }

    public static void writeFoldscreenswitchingSensor(long time) {
        android.util.EventLog.writeEvent(FOLDSCREENSWITCHING_SENSOR, time);
    }

    public static void writeFoldscreenswitchingPoweroffStart(long time) {
        android.util.EventLog.writeEvent(FOLDSCREENSWITCHING_POWEROFF_START, time);
    }

    public static void writeFoldscreenswitchingPoweroffEnd(long time) {
        android.util.EventLog.writeEvent(FOLDSCREENSWITCHING_POWEROFF_END, time);
    }

    public static void writeFoldscreenswitchingPoweronStart(long time) {
        android.util.EventLog.writeEvent(FOLDSCREENSWITCHING_POWERON_START, time);
    }

    public static void writeFoldscreenswitchingPoweronEnd(long time) {
        android.util.EventLog.writeEvent(FOLDSCREENSWITCHING_POWERON_END, time);
    }

    public static void writeFoldscreenswitchingScreenturningonStart(long time) {
        android.util.EventLog.writeEvent(FOLDSCREENSWITCHING_SCREENTURNINGON_START, time);
    }

    public static void writeFoldscreenswitchingScreenturningonEnd(long time) {
        android.util.EventLog.writeEvent(FOLDSCREENSWITCHING_SCREENTURNINGON_END, time);
    }

    public static void writeFoldscreenswitchingKeyguardOndrawn(long time) {
        android.util.EventLog.writeEvent(FOLDSCREENSWITCHING_KEYGUARD_ONDRAWN, time);
    }

    public static void writeFoldscreenswitchingPendingState(int state) {
        android.util.EventLog.writeEvent(FOLDSCREENSWITCHING_PENDING_STATE, state);
    }

    public static void writeWmTaskWindowingModeChanged(int taskid, int roottaskid, int newwindowingmode, int prevwindowingmode) {
        android.util.EventLog.writeEvent(WM_TASK_WINDOWING_MODE_CHANGED, java.lang.Integer.valueOf(taskid), java.lang.Integer.valueOf(roottaskid), java.lang.Integer.valueOf(newwindowingmode), java.lang.Integer.valueOf(prevwindowingmode));
    }
}
