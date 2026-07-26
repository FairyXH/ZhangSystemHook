package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public interface INotifierExt {
    default void notifyScreenOnOff(boolean on) {
    }

    default void notifyOnWakefulnessChangeStartedEnter(boolean interactive, int reason) {
    }

    default boolean isNeedActiveInput() {
        return true;
    }

    default void notifyOnWakefulnessChangeFinishedEnter(com.android.server.input.InputManagerInternal mInputManagerInternal, boolean interactive, boolean interactiveChanging) {
    }

    default void handleLateInteractiveChangeInActive() {
    }

    default boolean handleLateInteractiveChangeUnActive() {
        return false;
    }

    default boolean handleEarlyInteractiveChangeInActive() {
        return false;
    }

    default void onWakefulnessChanged(int wakefulness) {
    }

    default void noteSysStateChanged(int state, int type) {
    }

    default boolean playChargingStartedFeedback() {
        return false;
    }

    default void updatePendingBroadcastLocked() {
    }

    default void finishPendingBroadcastLocked() {
    }

    default boolean isSkipGotoSleepBroadcast() {
        return false;
    }

    default boolean isSkipWakeupBroadcast() {
        return false;
    }
}
