package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
public final class KeyboardLedController implements android.hardware.input.InputManager.InputDeviceListener {
    private static final int MSG_UPDATE_EXISTING_DEVICES = 1;
    private static final int MSG_UPDATE_MIC_MUTE_LED_STATE = 2;
    private static final java.lang.String TAG = com.android.server.input.KeyboardLedController.class.getSimpleName();
    private android.media.AudioManager mAudioManager;
    private final android.content.Context mContext;
    private final android.os.Handler mHandler;
    private android.hardware.input.InputManager mInputManager;
    private final android.util.SparseArray<android.view.InputDevice> mKeyboardsWithMicMuteLed = new android.util.SparseArray<>();
    private android.content.BroadcastReceiver mMicrophoneMuteChangedIntentReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.input.KeyboardLedController.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            android.os.Message msg = android.os.Message.obtain(com.android.server.input.KeyboardLedController.this.mHandler, 2);
            com.android.server.input.KeyboardLedController.this.mHandler.sendMessage(msg);
        }
    };
    private final com.android.server.input.NativeInputManagerService mNative;
    private android.hardware.SensorPrivacyManager mSensorPrivacyManager;

    KeyboardLedController(android.content.Context context, android.os.Looper looper, com.android.server.input.NativeInputManagerService nativeService) {
        this.mContext = context;
        this.mNative = nativeService;
        this.mHandler = new android.os.Handler(looper, new android.os.Handler.Callback() { // from class: com.android.server.input.KeyboardLedController$$ExternalSyntheticLambda0
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(android.os.Message message) {
                return this.f$0.handleMessage(message);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case 1:
                for (int deviceId : (int[]) msg.obj) {
                    onInputDeviceAdded(deviceId);
                }
                return true;
            case 2:
                updateMicMuteLedState();
                return true;
            default:
                return false;
        }
    }

    private void updateMicMuteLedState() {
        boolean z = true;
        if (!this.mAudioManager.isMicrophoneMute() && !this.mSensorPrivacyManager.areAnySensorPrivacyTogglesEnabled(1)) {
            z = false;
        }
        boolean isMicrophoneMute = z;
        int color = isMicrophoneMute ? -1 : 0;
        for (int i = 0; i < this.mKeyboardsWithMicMuteLed.size(); i++) {
            android.view.InputDevice device = this.mKeyboardsWithMicMuteLed.valueAt(i);
            if (device != null) {
                int deviceId = device.getId();
                android.hardware.lights.Light light = getKeyboardMicMuteLight(device);
                if (light != null) {
                    this.mNative.setLightColor(deviceId, light.getId(), color);
                }
            }
        }
    }

    private android.hardware.lights.Light getKeyboardMicMuteLight(android.view.InputDevice device) {
        for (android.hardware.lights.Light light : device.getLightsManager().getLights()) {
            if (light.getType() == 10004 && light.hasBrightnessControl()) {
                return light;
            }
        }
        return null;
    }

    public void systemRunning() {
        this.mSensorPrivacyManager = (android.hardware.SensorPrivacyManager) java.util.Objects.requireNonNull((android.hardware.SensorPrivacyManager) this.mContext.getSystemService(android.hardware.SensorPrivacyManager.class));
        this.mInputManager = (android.hardware.input.InputManager) java.util.Objects.requireNonNull((android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class));
        this.mAudioManager = (android.media.AudioManager) java.util.Objects.requireNonNull((android.media.AudioManager) this.mContext.getSystemService(android.media.AudioManager.class));
        this.mInputManager.registerInputDeviceListener(this, this.mHandler);
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 1, this.mInputManager.getInputDeviceIds());
        this.mHandler.sendMessage(msg);
        this.mContext.registerReceiverAsUser(this.mMicrophoneMuteChangedIntentReceiver, android.os.UserHandle.ALL, new android.content.IntentFilter("android.media.action.MICROPHONE_MUTE_CHANGED"), null, this.mHandler);
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceAdded(int deviceId) {
        onInputDeviceChanged(deviceId);
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceRemoved(int deviceId) {
        this.mKeyboardsWithMicMuteLed.remove(deviceId);
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceChanged(int deviceId) {
        android.view.InputDevice inputDevice = this.mInputManager.getInputDevice(deviceId);
        if (inputDevice != null && getKeyboardMicMuteLight(inputDevice) != null) {
            this.mKeyboardsWithMicMuteLed.put(deviceId, inputDevice);
            android.os.Message msg = android.os.Message.obtain(this.mHandler, 2);
            this.mHandler.sendMessage(msg);
        }
    }

    public void dump(java.io.PrintWriter pw) {
        android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw);
        ipw.println(TAG + ": " + this.mKeyboardsWithMicMuteLed.size() + " keyboard mic mute lights");
        ipw.increaseIndent();
        for (int i = 0; i < this.mKeyboardsWithMicMuteLed.size(); i++) {
            android.view.InputDevice inputDevice = this.mKeyboardsWithMicMuteLed.valueAt(i);
            ipw.println(i + " " + inputDevice.getName() + ": " + getKeyboardMicMuteLight(inputDevice).toString());
        }
        ipw.decreaseIndent();
    }
}
