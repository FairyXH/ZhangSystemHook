package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class InputDeviceDelegate implements android.hardware.input.InputManager.InputDeviceListener {
    private static final java.lang.String TAG = "InputDeviceDelegate";
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private android.hardware.input.InputManager mInputManager;
    private boolean mShouldVibrateInputDevices;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<android.os.VibratorManager> mInputDeviceVibrators = new android.util.SparseArray<>();

    InputDeviceDelegate(android.content.Context context, android.os.Handler handler) {
        this.mHandler = handler;
        this.mContext = context;
    }

    public void onSystemReady() {
        synchronized (this.mLock) {
            this.mInputManager = (android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class);
        }
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceAdded(int deviceId) {
        updateInputDevice(deviceId);
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceChanged(int deviceId) {
        updateInputDevice(deviceId);
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceRemoved(int deviceId) {
        synchronized (this.mLock) {
            this.mInputDeviceVibrators.remove(deviceId);
        }
    }

    public boolean isAvailable() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mInputDeviceVibrators.size() > 0;
        }
        return z;
    }

    public boolean vibrateIfAvailable(com.android.server.vibrator.Vibration.CallerInfo callerInfo, android.os.CombinedVibration effect) {
        boolean z;
        synchronized (this.mLock) {
            for (int i = 0; i < this.mInputDeviceVibrators.size(); i++) {
                this.mInputDeviceVibrators.valueAt(i).vibrate(callerInfo.uid, callerInfo.opPkg, effect, callerInfo.reason, callerInfo.attrs);
            }
            z = this.mInputDeviceVibrators.size() > 0;
        }
        return z;
    }

    public boolean cancelVibrateIfAvailable() {
        boolean z;
        synchronized (this.mLock) {
            for (int i = 0; i < this.mInputDeviceVibrators.size(); i++) {
                this.mInputDeviceVibrators.valueAt(i).cancel();
            }
            z = this.mInputDeviceVibrators.size() > 0;
        }
        return z;
    }

    public boolean updateInputDeviceVibrators(boolean vibrateInputDevices) {
        synchronized (this.mLock) {
            if (this.mInputManager == null) {
                return false;
            }
            if (vibrateInputDevices == this.mShouldVibrateInputDevices) {
                return false;
            }
            this.mShouldVibrateInputDevices = vibrateInputDevices;
            this.mInputDeviceVibrators.clear();
            if (vibrateInputDevices) {
                this.mInputManager.registerInputDeviceListener(this, this.mHandler);
                for (int deviceId : this.mInputManager.getInputDeviceIds()) {
                    android.view.InputDevice device = this.mInputManager.getInputDevice(deviceId);
                    if (device != null) {
                        android.os.VibratorManager vibratorManager = device.getVibratorManager();
                        if (vibratorManager.getVibratorIds().length > 0) {
                            this.mInputDeviceVibrators.put(device.getId(), vibratorManager);
                        }
                    }
                }
            } else {
                this.mInputManager.unregisterInputDeviceListener(this);
            }
            return true;
        }
    }

    private void updateInputDevice(int deviceId) {
        synchronized (this.mLock) {
            if (this.mInputManager == null) {
                return;
            }
            if (this.mShouldVibrateInputDevices) {
                android.view.InputDevice device = this.mInputManager.getInputDevice(deviceId);
                if (device == null) {
                    this.mInputDeviceVibrators.remove(deviceId);
                    return;
                }
                android.os.VibratorManager vibratorManager = device.getVibratorManager();
                if (vibratorManager.getVibratorIds().length > 0) {
                    this.mInputDeviceVibrators.put(device.getId(), vibratorManager);
                } else {
                    this.mInputDeviceVibrators.remove(deviceId);
                }
            }
        }
    }
}
