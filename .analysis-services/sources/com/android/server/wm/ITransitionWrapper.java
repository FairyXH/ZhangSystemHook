package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ITransitionWrapper {
    default com.android.server.wm.ITransitionExt getExtImpl() {
        return new com.android.server.wm.ITransitionExt() { // from class: com.android.server.wm.ITransitionWrapper.1
        };
    }

    default boolean isWcTranslucent(com.android.server.wm.WindowContainer wc) {
        return false;
    }

    default android.util.ArrayMap<com.android.server.wm.ActivityRecord, com.android.server.wm.Task> getTransientLaunches() {
        return null;
    }

    default void initTransientLaunches(android.util.ArrayMap<com.android.server.wm.ActivityRecord, com.android.server.wm.Task> transientLaunches) {
    }

    default java.util.ArrayList<com.android.server.wm.Task> getTransientHideTasks() {
        return null;
    }

    default void initTransientHideTasks(java.util.ArrayList<com.android.server.wm.Task> transientHideTasks) {
    }

    default java.util.ArrayList<com.android.server.wm.DisplayContent> getTargetDisplays() {
        return new java.util.ArrayList<>();
    }

    default android.util.ArraySet<com.android.server.wm.WindowToken> getVisibleAtTransitionEndTokens() {
        return new android.util.ArraySet<>();
    }

    default int getRecentsDisplayId() {
        return -1;
    }

    default boolean getIsSeamlessRotation() {
        return false;
    }

    default com.android.server.wm.TransitionController getTransitionController() {
        return null;
    }

    default android.view.SurfaceControl.Transaction getTmpTransaction() {
        return null;
    }

    default android.view.SurfaceControl.Transaction getCleanupTransaction() {
        return null;
    }

    default android.view.SurfaceControl.Transaction getInputSinkTransaction() {
        return null;
    }

    default void setInputSinkTransaction(android.view.SurfaceControl.Transaction t) {
    }

    default void closeStartTransition() {
    }

    default void closeFinishTransition() {
    }
}
