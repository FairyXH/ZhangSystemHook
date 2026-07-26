package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface IWindowOrganizerControllerExt {
    default void setTransitionRecentsFromRemote(android.os.IBinder transitionToken) {
    }

    default void notifyTransitionRemoveTask(android.os.IBinder playing, android.os.IBinder merged, int removeTaskId) {
    }

    default void notifyTransitionMerged(android.os.IBinder playing, android.os.IBinder merged) {
    }

    default void notifyRemoteInterrupt(android.os.IBinder playing, android.os.IBinder merged) {
    }

    default void notifyStartCompleted(android.window.WindowContainerToken openingTaskToken, boolean abort) {
    }

    default void reorderTask(com.android.server.wm.Task reorderTask, com.android.server.wm.Transition finish) {
    }

    default void recordFinishTransitionState(android.window.WindowContainerTransaction t, com.android.server.wm.Transition transition) {
    }

    default boolean skipReorderHomeTaskIfNeed(com.android.server.wm.Task task, com.android.server.wm.Transition finishTransition) {
        return false;
    }

    default boolean shouldSkipStartTransition(com.android.server.wm.Transition transition) {
        return false;
    }

    default void shouldDeferLayoutInRecentsInterruptFinish(com.android.server.wm.Transition finishTransition, int effects) {
    }

    default void fillExtendInfo(com.android.server.wm.Transition transition, android.window.OplusWCTExtendInfo extendInfo) {
    }

    default boolean disableRecentsTransitionForFlexibleWindow(android.window.WindowContainerTransaction t, int type) {
        return false;
    }
}
