package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public interface ISystemGesturesPointerEventListenerExt {
    default void init(android.content.Context context) {
    }

    default void updateDefaultSwipeDistance() {
    }

    default boolean checkSwipeFromBottom(float x, float y, int screenHeight) {
        return true;
    }

    default boolean hookSwipeFromTop(float x, float y) {
        return false;
    }

    default boolean inSplitHandleRegion(android.view.MotionEvent event) {
        return false;
    }

    default boolean isOnePuttHandleRegion(android.view.MotionEvent event) {
        return false;
    }

    default void hookOnGlobalFlingGesture(int duration) {
    }

    default void notifyFlingGestureStatus(int duration) {
    }

    default void notifyScrollGestureStatus() {
    }

    default void notifyMotionUpOrCancel() {
    }

    default void notifyMotionDown() {
    }

    default void setSwipeStartThreshold(android.graphics.Rect swipeStartThreshold) {
    }

    default boolean checkSwipeForGameMode(int leftOrRight, int screenWidth, float fromX) {
        return false;
    }

    default android.os.Handler getOplusUiHandler(android.os.Handler handler) {
        return handler;
    }

    default void setInterceptSwipeEvent() {
    }

    default void resetInterceptSwipeEvent() {
    }

    default boolean getInterceptSwipeEvent() {
        return false;
    }
}
