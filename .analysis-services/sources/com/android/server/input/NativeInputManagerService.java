package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
interface NativeInputManagerService {
    void addKeyRemapping(int i, int i2, int i3);

    boolean canDispatchToDisplay(int i, int i2);

    void cancelCurrentTouch();

    void cancelVibrate(int i, int i2);

    void changeKeyboardLayoutAssociation();

    void changeTypeAssociation();

    void changeUniqueIdAssociation();

    android.view.InputChannel createInputChannel(java.lang.String str);

    android.view.InputChannel createInputMonitor(int i, java.lang.String str, int i2);

    void disableInputDevice(int i);

    void disableSensor(int i, int i2);

    void displayRemoved(int i);

    java.lang.String dump();

    void dynamicallyConfigLog(int i);

    void enableInputDevice(int i);

    boolean enableSensor(int i, int i2, int i3, int i4);

    void fadePointer(int i);

    boolean flushSensor(int i, int i2);

    int getBatteryCapacity(int i);

    java.lang.String getBatteryDevicePath(int i);

    int getBatteryStatus(int i);

    java.lang.String getBluetoothAddress(int i);

    int getKeyCodeForKeyLocation(int i, int i2);

    int getKeyCodeState(int i, int i2, int i3);

    int getLastUsedInputDeviceId();

    int getLightColor(int i, int i2);

    int getLightPlayerId(int i, int i2);

    java.util.List<android.hardware.lights.Light> getLights(int i);

    float[] getMouseCursorPosition(int i);

    int getMousePointerSpeed();

    int getScanCodeState(int i, int i2, int i3);

    android.hardware.input.InputSensorInfo[] getSensorList(int i);

    int getSwitchState(int i, int i2, int i3);

    int[] getVibratorIds(int i);

    boolean hasKeys(int i, int i2, int[] iArr, boolean[] zArr);

    int hasTouchedWindow(int i, java.lang.String str);

    int injectInputEvent(android.view.InputEvent inputEvent, boolean z, int i, int i2, int i3, int i4);

    boolean isVibrating(int i);

    void monitor();

    void notifyPortAssociationsChanged();

    void pilferPointers(android.os.IBinder iBinder);

    void reloadCalibration();

    void reloadDeviceAliases();

    void reloadKeyboardLayouts();

    void reloadPointerIcons();

    void removeInputChannel(android.os.IBinder iBinder);

    void requestPointerCapture(android.os.IBinder iBinder, boolean z);

    void setAccessibilityBounceKeysThreshold(int i);

    void setAccessibilitySlowKeysThreshold(int i);

    void setAccessibilityStickyKeysEnabled(boolean z);

    void setDisplayEligibilityForPointerCapture(int i, boolean z);

    void setDisplayViewports(android.hardware.display.DisplayViewport[] displayViewportArr);

    void setFocusedApplication(int i, android.view.InputApplicationHandle inputApplicationHandle);

    void setFocusedDisplay(int i);

    boolean setInTouchMode(boolean z, int i, int i2, boolean z2, int i3);

    void setInputDispatchMode(boolean z, boolean z2);

    void setInputFilterEnabled(boolean z);

    void setInputMethodConnectionIsActive(boolean z);

    void setInteractive(boolean z);

    void setKeyRepeatConfiguration(int i, int i2);

    void setLaserEnabled(boolean z);

    void setLightColor(int i, int i2, int i3);

    void setLightPlayerId(int i, int i2, int i3);

    void setMaximumObscuringOpacityForTouch(float f);

    void setMinTimeBetweenUserActivityPokes(long j);

    void setMotionClassifierEnabled(boolean z);

    void setMousePointerAccelerationEnabled(int i, boolean z);

    void setNotifyEnabled(boolean z);

    void setPointerDisplayId(int i);

    void setPointerGestureReverseSwipe(boolean z);

    boolean setPointerIcon(android.view.PointerIcon pointerIcon, int i, int i2, int i3, android.os.IBinder iBinder);

    void setPointerIconVisibility(int i, boolean z);

    void setPointerSpeed(int i);

    void setRotateVolumeKey(boolean z);

    void setShowTouches(boolean z);

    void setStylusButtonMotionEventsEnabled(boolean z);

    void setStylusPointerIconEnabled(boolean z);

    void setSystemUiLightsOut(boolean z);

    void setTapToPressEnabled(boolean z);

    void setTouchpadNaturalScrollingEnabled(boolean z);

    void setTouchpadPointerSpeed(int i);

    void setTouchpadRightClickZoneEnabled(boolean z);

    void setTouchpadTapDraggingEnabled(boolean z);

    void setTouchpadTapToClickEnabled(boolean z);

    void start();

    void sysfsNodeChanged(java.lang.String str);

    void toggleCapsLock(int i);

    @java.lang.Deprecated
    boolean transferTouch(android.os.IBinder iBinder, int i);

    boolean transferTouchGesture(android.os.IBinder iBinder, android.os.IBinder iBinder2, boolean z);

    android.view.VerifiedInputEvent verifyInputEvent(android.view.InputEvent inputEvent);

    void vibrate(int i, long[] jArr, int[] iArr, int i2, int i3);

    void vibrateCombined(int i, long[] jArr, android.util.SparseArray<int[]> sparseArray, int i2, int i3);

    public static class NativeImpl implements com.android.server.input.NativeInputManagerService {
        public long mPtr;

        private native long init(com.android.server.input.InputManagerService inputManagerService, android.os.MessageQueue messageQueue);

        @Override // com.android.server.input.NativeInputManagerService
        public native void addKeyRemapping(int i, int i2, int i3);

        @Override // com.android.server.input.NativeInputManagerService
        public native boolean canDispatchToDisplay(int i, int i2);

        @Override // com.android.server.input.NativeInputManagerService
        public native void cancelCurrentTouch();

        @Override // com.android.server.input.NativeInputManagerService
        public native void cancelVibrate(int i, int i2);

        @Override // com.android.server.input.NativeInputManagerService
        public native void changeKeyboardLayoutAssociation();

        @Override // com.android.server.input.NativeInputManagerService
        public native void changeTypeAssociation();

        @Override // com.android.server.input.NativeInputManagerService
        public native void changeUniqueIdAssociation();

        @Override // com.android.server.input.NativeInputManagerService
        public native android.view.InputChannel createInputChannel(java.lang.String str);

        @Override // com.android.server.input.NativeInputManagerService
        public native android.view.InputChannel createInputMonitor(int i, java.lang.String str, int i2);

        @Override // com.android.server.input.NativeInputManagerService
        public native void disableInputDevice(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native void disableSensor(int i, int i2);

        @Override // com.android.server.input.NativeInputManagerService
        public native void displayRemoved(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native java.lang.String dump();

        @Override // com.android.server.input.NativeInputManagerService
        public native void dynamicallyConfigLog(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native void enableInputDevice(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native boolean enableSensor(int i, int i2, int i3, int i4);

        @Override // com.android.server.input.NativeInputManagerService
        public native void fadePointer(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native boolean flushSensor(int i, int i2);

        @Override // com.android.server.input.NativeInputManagerService
        public native int getBatteryCapacity(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native java.lang.String getBatteryDevicePath(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native int getBatteryStatus(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native java.lang.String getBluetoothAddress(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native int getKeyCodeForKeyLocation(int i, int i2);

        @Override // com.android.server.input.NativeInputManagerService
        public native int getKeyCodeState(int i, int i2, int i3);

        @Override // com.android.server.input.NativeInputManagerService
        public native int getLastUsedInputDeviceId();

        @Override // com.android.server.input.NativeInputManagerService
        public native int getLightColor(int i, int i2);

        @Override // com.android.server.input.NativeInputManagerService
        public native int getLightPlayerId(int i, int i2);

        @Override // com.android.server.input.NativeInputManagerService
        public native java.util.List<android.hardware.lights.Light> getLights(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native float[] getMouseCursorPosition(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native int getMousePointerSpeed();

        @Override // com.android.server.input.NativeInputManagerService
        public native int getScanCodeState(int i, int i2, int i3);

        @Override // com.android.server.input.NativeInputManagerService
        public native android.hardware.input.InputSensorInfo[] getSensorList(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native int getSwitchState(int i, int i2, int i3);

        @Override // com.android.server.input.NativeInputManagerService
        public native int[] getVibratorIds(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native boolean hasKeys(int i, int i2, int[] iArr, boolean[] zArr);

        @Override // com.android.server.input.NativeInputManagerService
        public native int hasTouchedWindow(int i, java.lang.String str);

        @Override // com.android.server.input.NativeInputManagerService
        public native int injectInputEvent(android.view.InputEvent inputEvent, boolean z, int i, int i2, int i3, int i4);

        @Override // com.android.server.input.NativeInputManagerService
        public native boolean isVibrating(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native void monitor();

        @Override // com.android.server.input.NativeInputManagerService
        public native void notifyPortAssociationsChanged();

        @Override // com.android.server.input.NativeInputManagerService
        public native void pilferPointers(android.os.IBinder iBinder);

        @Override // com.android.server.input.NativeInputManagerService
        public native void reloadCalibration();

        @Override // com.android.server.input.NativeInputManagerService
        public native void reloadDeviceAliases();

        @Override // com.android.server.input.NativeInputManagerService
        public native void reloadKeyboardLayouts();

        @Override // com.android.server.input.NativeInputManagerService
        public native void reloadPointerIcons();

        @Override // com.android.server.input.NativeInputManagerService
        public native void removeInputChannel(android.os.IBinder iBinder);

        @Override // com.android.server.input.NativeInputManagerService
        public native void requestPointerCapture(android.os.IBinder iBinder, boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setAccessibilityBounceKeysThreshold(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setAccessibilitySlowKeysThreshold(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setAccessibilityStickyKeysEnabled(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setDisplayEligibilityForPointerCapture(int i, boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setDisplayViewports(android.hardware.display.DisplayViewport[] displayViewportArr);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setFocusedApplication(int i, android.view.InputApplicationHandle inputApplicationHandle);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setFocusedDisplay(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native boolean setInTouchMode(boolean z, int i, int i2, boolean z2, int i3);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setInputDispatchMode(boolean z, boolean z2);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setInputFilterEnabled(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setInputMethodConnectionIsActive(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setInteractive(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setKeyRepeatConfiguration(int i, int i2);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setLaserEnabled(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setLightColor(int i, int i2, int i3);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setLightPlayerId(int i, int i2, int i3);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setMaximumObscuringOpacityForTouch(float f);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setMinTimeBetweenUserActivityPokes(long j);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setMotionClassifierEnabled(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setMousePointerAccelerationEnabled(int i, boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setNotifyEnabled(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setPointerDisplayId(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setPointerGestureReverseSwipe(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native boolean setPointerIcon(android.view.PointerIcon pointerIcon, int i, int i2, int i3, android.os.IBinder iBinder);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setPointerIconVisibility(int i, boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setPointerSpeed(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setRotateVolumeKey(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setShowTouches(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setStylusButtonMotionEventsEnabled(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setStylusPointerIconEnabled(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setSystemUiLightsOut(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setTapToPressEnabled(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setTouchpadNaturalScrollingEnabled(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setTouchpadPointerSpeed(int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setTouchpadRightClickZoneEnabled(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setTouchpadTapDraggingEnabled(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void setTouchpadTapToClickEnabled(boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native void start();

        @Override // com.android.server.input.NativeInputManagerService
        public native void sysfsNodeChanged(java.lang.String str);

        @Override // com.android.server.input.NativeInputManagerService
        public native void toggleCapsLock(int i);

        @Override // com.android.server.input.NativeInputManagerService
        @java.lang.Deprecated
        public native boolean transferTouch(android.os.IBinder iBinder, int i);

        @Override // com.android.server.input.NativeInputManagerService
        public native boolean transferTouchGesture(android.os.IBinder iBinder, android.os.IBinder iBinder2, boolean z);

        @Override // com.android.server.input.NativeInputManagerService
        public native android.view.VerifiedInputEvent verifyInputEvent(android.view.InputEvent inputEvent);

        @Override // com.android.server.input.NativeInputManagerService
        public native void vibrate(int i, long[] jArr, int[] iArr, int i2, int i3);

        @Override // com.android.server.input.NativeInputManagerService
        public native void vibrateCombined(int i, long[] jArr, android.util.SparseArray<int[]> sparseArray, int i2, int i3);

        NativeImpl(com.android.server.input.InputManagerService service, android.os.MessageQueue messageQueue) {
            this.mPtr = init(service, messageQueue);
        }
    }
}
