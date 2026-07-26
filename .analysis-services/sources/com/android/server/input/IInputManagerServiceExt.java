package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
public interface IInputManagerServiceExt {

    public interface InputManagerNativeCallback {
        java.lang.String requestAdjustNativeDump();

        default void onNativeDynamicallyConfigLog(boolean isOpenLibinputflinger, boolean isOpenLibinput) {
        }

        default void onNativeDynamicallyConfigLog(int config) {
        }
    }

    default void init(android.content.Context context, android.os.Handler mHandler, long ptr) {
    }

    default void onSystemRunning() {
    }

    default boolean updateInvalidRegion(java.lang.String regionKey, java.util.List<android.graphics.RectF> region, boolean disposable, boolean isDelete, android.os.Bundle extras) {
        return false;
    }

    default boolean setJoyStickConfig(int configType, java.lang.String config) {
        return false;
    }

    default boolean setJoyStickStatus(int configStatus) {
        return false;
    }

    default boolean setJoyStickSwitch(int switchStatus) {
        return false;
    }

    default boolean dynamicallyAdjustDump(java.io.PrintWriter pw, java.lang.String[] args) {
        return false;
    }

    default void start() {
    }

    default void updateUntrustedTouchConfig(java.lang.String data, boolean isRus) {
    }

    default void showTipsDialog(java.lang.String packageName, android.content.Context context) {
    }

    default void removePackageFromUntrustedRecord(java.lang.String action, java.lang.String pkgName) {
    }

    default java.util.HashSet<java.lang.String> getTrustedPackages() {
        return new java.util.HashSet<>();
    }

    default java.util.ArrayList<java.lang.Integer> getTrustedWindowType() {
        return new java.util.ArrayList<>();
    }

    default boolean isOplusTrustedApp(java.lang.String packageName, int uid, java.lang.String extras) {
        return false;
    }

    default void setAccessibilityStatus(boolean enabled) {
    }

    default boolean isNeedIntermittentIntercept(android.view.InputEvent event) {
        return false;
    }

    default void notifyInputDispatcherThread(int tid) {
    }

    default void notifyInputReaderThread(int tid) {
    }

    default boolean notifyGestureMonitorUnresponsive(int pid, java.lang.String reason) {
        return false;
    }

    default void setDisplayViewportsInternal(java.util.List<android.hardware.display.DisplayViewport> viewports) {
    }

    default int hasTouchedWindow(int uid, java.lang.String pkg) {
        return 0;
    }

    default void inputCancelFromNative(int uid) {
    }

    default boolean addProxyBinder(android.os.IBinder bpBinder, int uid, int pid) {
        return false;
    }

    default boolean removeProxyBinder(android.os.IBinder bpBinder, int uid) {
        return false;
    }

    default void notifyInputJitter(java.lang.String info) {
    }

    default void showTouchPadNotification(android.view.InputDevice[] mInputDevices) {
    }

    default boolean isOfficialKeyboard(android.view.InputDevice inputDevice) {
        return false;
    }

    default void notifyRefreshRateChanged(int displayId, float refreshrate) {
    }

    default void notifyImeWindowStateChanged(boolean hasShow) {
    }

    default void debugInputKeyInject(int pid, android.view.InputEvent event, java.lang.String from) {
    }

    default void uploadCollectData(java.lang.String type, java.lang.String reason) {
    }

    default void sendLaserDelta(float deltaX, float deltaY) {
    }

    default void interceptNotifyMotion(int action, java.lang.String toolType) {
    }
}
