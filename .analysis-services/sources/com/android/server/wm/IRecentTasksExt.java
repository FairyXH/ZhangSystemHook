package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IRecentTasksExt {
    default boolean getRecentTasksImpl(android.content.Intent intent) {
        return false;
    }

    default boolean isInVisibleRange(com.android.server.wm.Task task) {
        return false;
    }

    default boolean hasCompatibleActivityTypeAndWindowingMode(int windowingMode, int otherWindowingMode) {
        return false;
    }

    default boolean shouldRemoveIndexForAddTask(com.android.server.wm.Task task, com.android.server.wm.Task recentTask) {
        return false;
    }

    default boolean skipPersistMultiSearchTask(com.android.server.wm.Task task) {
        return false;
    }

    default boolean skipMultiSearchTask(com.android.server.wm.Task task) {
        return false;
    }

    default boolean isLaunchedFromMultiSearch(com.android.server.wm.Task t1, com.android.server.wm.Task t2) {
        return false;
    }

    default boolean skipPreloadingTaskInRecents(com.android.server.wm.Task task) {
        return false;
    }

    default int adjustPreloadingTaskIndex(int intendIndex, int oldIndex, com.android.server.wm.Task task, int counts) {
        return intendIndex;
    }

    default boolean skipAddPreloadingFakeTask(com.android.server.wm.Task task) {
        return false;
    }

    default boolean skipMovePreloadingTask(com.android.server.wm.Task task) {
        return false;
    }

    default boolean skipResetFreezeTaskListReordering(android.view.MotionEvent event) {
        return false;
    }

    default void removeContainerTask(com.android.server.wm.Task task) {
    }

    default boolean isTrimmable(com.android.server.wm.Task task) {
        return false;
    }

    default boolean isExcludeFromRecentsForFlexible(com.android.server.wm.ActivityTaskManagerService service, com.android.server.wm.Task task, boolean origin) {
        return false;
    }

    default boolean skipShowRecentTask(com.android.server.wm.Task task, int callingUid) {
        return false;
    }

    default void removeForAddTask(com.android.server.wm.Task task, com.android.server.wm.Task removedTask) {
    }

    default boolean reCheckUserSetupComplete(com.android.server.wm.Task task) {
        return false;
    }

    default boolean skipMoveTask(com.android.server.wm.Task task) {
        return false;
    }

    default void addPsContainerToTop(java.util.ArrayList<com.android.server.wm.Task> tasks, com.android.server.wm.Task focusTask, int indexToAdd) {
    }

    default boolean skipRemoveRecentTask(android.content.Context context, java.lang.String className) {
        return false;
    }

    default void setFirstTaskAndFlags(com.android.server.wm.Task task, int flags) {
    }

    default boolean isSpecificSceneInRecentList(int index) {
        return false;
    }

    default boolean isLauncherInSpecificScene() {
        return false;
    }

    default boolean isLauncherIndexZero() {
        return false;
    }

    default void clearFilterBuffer() {
    }

    default java.lang.String getFilteredBuffer() {
        return "";
    }
}
