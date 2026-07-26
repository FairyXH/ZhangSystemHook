package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
final class KeyboardBacklightController implements com.android.server.input.InputManagerService.KeyboardBacklightControllerInterface, android.hardware.input.InputManager.InputDeviceListener {
    private static final int DEFAULT_NUM_BRIGHTNESS_CHANGE_STEPS = 10;
    private static final int MAX_BRIGHTNESS = 255;
    static final int MAX_BRIGHTNESS_CHANGE_STEPS = 10;
    private static final int MSG_DECREMENT_KEYBOARD_BACKLIGHT = 3;
    private static final int MSG_INCREMENT_KEYBOARD_BACKLIGHT = 2;
    private static final int MSG_INTERACTIVE_STATE_CHANGED = 6;
    private static final int MSG_NOTIFY_USER_ACTIVITY = 4;
    private static final int MSG_NOTIFY_USER_INACTIVITY = 5;
    private static final int MSG_UPDATE_EXISTING_DEVICES = 1;
    private static final java.lang.String UEVENT_KEYBOARD_BACKLIGHT_TAG = "kbd_backlight";
    private int mAmbientBacklightValue;
    private final com.android.server.input.AmbientKeyboardBacklightController mAmbientController;
    private com.android.server.input.AmbientKeyboardBacklightController.AmbientKeyboardBacklightListener mAmbientListener;
    private final com.android.server.input.KeyboardBacklightController.AnimatorFactory mAnimatorFactory;
    private final android.content.Context mContext;
    private final com.android.server.input.PersistentDataStore mDataStore;
    private final android.os.Handler mHandler;
    private boolean mIsBacklightOn;
    private boolean mIsInteractive;
    private final android.util.SparseArray<com.android.server.input.KeyboardBacklightController.KeyboardBacklightListenerRecord> mKeyboardBacklightListenerRecords;
    private final android.util.SparseArray<com.android.server.input.KeyboardBacklightController.KeyboardBacklightState> mKeyboardBacklights;
    private final com.android.server.input.NativeInputManagerService mNative;
    private final com.android.server.input.UEventManager mUEventManager;
    private static final java.lang.String TAG = "KbdBacklightController";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private static final long TRANSITION_ANIMATION_DURATION_MILLIS = java.time.Duration.ofSeconds(1).toMillis();
    static final long USER_INACTIVITY_THRESHOLD_MILLIS = java.time.Duration.ofSeconds(30).toMillis();
    static final int[] DEFAULT_BRIGHTNESS_VALUE_FOR_LEVEL = new int[11];

    interface AnimatorFactory {
        android.animation.ValueAnimator makeIntAnimator(int i, int i2);
    }

    private enum Direction {
        DIRECTION_UP,
        DIRECTION_DOWN
    }

    static {
        for (int i = 0; i <= 10; i++) {
            DEFAULT_BRIGHTNESS_VALUE_FOR_LEVEL[i] = (int) java.lang.Math.floor((i * 255.0f) / 10.0f);
        }
    }

    KeyboardBacklightController(android.content.Context context, com.android.server.input.NativeInputManagerService nativeService, com.android.server.input.PersistentDataStore dataStore, android.os.Looper looper, com.android.server.input.UEventManager uEventManager) {
        this(context, nativeService, dataStore, looper, new com.android.server.input.KeyboardBacklightController.AnimatorFactory() { // from class: com.android.server.input.KeyboardBacklightController$$ExternalSyntheticLambda0
            @Override // com.android.server.input.KeyboardBacklightController.AnimatorFactory
            public final android.animation.ValueAnimator makeIntAnimator(int i, int i2) {
                return android.animation.ValueAnimator.ofInt(i, i2);
            }
        }, uEventManager);
    }

    KeyboardBacklightController(android.content.Context context, com.android.server.input.NativeInputManagerService nativeService, com.android.server.input.PersistentDataStore dataStore, android.os.Looper looper, com.android.server.input.KeyboardBacklightController.AnimatorFactory animatorFactory, com.android.server.input.UEventManager uEventManager) {
        this.mKeyboardBacklights = new android.util.SparseArray<>(1);
        this.mIsBacklightOn = false;
        this.mIsInteractive = true;
        this.mKeyboardBacklightListenerRecords = new android.util.SparseArray<>();
        this.mAmbientBacklightValue = 0;
        this.mContext = context;
        this.mNative = nativeService;
        this.mDataStore = dataStore;
        this.mHandler = new android.os.Handler(looper, new android.os.Handler.Callback() { // from class: com.android.server.input.KeyboardBacklightController$$ExternalSyntheticLambda2
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(android.os.Message message) {
                return this.f$0.handleMessage(message);
            }
        });
        this.mAnimatorFactory = animatorFactory;
        this.mAmbientController = new com.android.server.input.AmbientKeyboardBacklightController(context, looper);
        this.mUEventManager = uEventManager;
    }

    @Override // com.android.server.input.InputManagerService.KeyboardBacklightControllerInterface
    public void systemRunning() {
        android.hardware.input.InputManager inputManager = (android.hardware.input.InputManager) java.util.Objects.requireNonNull((android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class));
        inputManager.registerInputDeviceListener(this, this.mHandler);
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 1, inputManager.getInputDeviceIds());
        this.mHandler.sendMessage(msg);
        this.mUEventManager.addListener(new com.android.server.input.UEventManager.UEventListener() { // from class: com.android.server.input.KeyboardBacklightController.1
            @Override // com.android.server.input.UEventManager.UEventListener
            public void onUEvent(android.os.UEventObserver.UEvent event) {
                com.android.server.input.KeyboardBacklightController.this.onKeyboardBacklightUEvent(event);
            }
        }, UEVENT_KEYBOARD_BACKLIGHT_TAG);
        if (com.android.server.input.InputFeatureFlagProvider.isAmbientKeyboardBacklightControlEnabled()) {
            this.mAmbientController.systemRunning();
        }
    }

    @Override // com.android.server.input.InputManagerService.KeyboardBacklightControllerInterface
    public void incrementKeyboardBacklight(int deviceId) {
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 2, java.lang.Integer.valueOf(deviceId));
        this.mHandler.sendMessage(msg);
    }

    @Override // com.android.server.input.InputManagerService.KeyboardBacklightControllerInterface
    public void decrementKeyboardBacklight(int deviceId) {
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 3, java.lang.Integer.valueOf(deviceId));
        this.mHandler.sendMessage(msg);
    }

    @Override // com.android.server.input.InputManagerService.KeyboardBacklightControllerInterface
    public void notifyUserActivity() {
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 4);
        this.mHandler.sendMessage(msg);
    }

    @Override // com.android.server.input.InputManagerService.KeyboardBacklightControllerInterface
    public void onInteractiveChanged(boolean isInteractive) {
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 6, java.lang.Boolean.valueOf(isInteractive));
        this.mHandler.sendMessage(msg);
    }

    private void updateKeyboardBacklight(int deviceId, com.android.server.input.KeyboardBacklightController.Direction direction) {
        int lowerBound;
        int newBrightnessLevel;
        android.view.InputDevice inputDevice = getInputDevice(deviceId);
        com.android.server.input.KeyboardBacklightController.KeyboardBacklightState state = this.mKeyboardBacklights.get(deviceId);
        if (inputDevice == null || state == null) {
            return;
        }
        if (state.mUseAmbientController) {
            int index = java.util.Arrays.binarySearch(state.mBrightnessValueForLevel, this.mAmbientBacklightValue);
            if (index < 0) {
                int lowerBound2 = java.lang.Math.max(0, (-(index + 1)) - 1);
                lowerBound = direction == com.android.server.input.KeyboardBacklightController.Direction.DIRECTION_UP ? lowerBound2 : lowerBound2 + 1;
            } else {
                lowerBound = index;
            }
        } else {
            lowerBound = state.mBrightnessLevel;
        }
        if (direction == com.android.server.input.KeyboardBacklightController.Direction.DIRECTION_UP) {
            newBrightnessLevel = java.lang.Math.min(lowerBound + 1, state.getNumBrightnessChangeSteps());
        } else {
            int newBrightnessLevel2 = lowerBound - 1;
            newBrightnessLevel = java.lang.Math.max(newBrightnessLevel2, 0);
        }
        state.setBrightnessLevel(newBrightnessLevel);
        updateAmbientLightListener();
        maybeBackupBacklightBrightness(inputDevice, state.mLight, state.mBrightnessValueForLevel[newBrightnessLevel]);
        if (DEBUG) {
            android.util.Slog.d(TAG, "Changing state from " + state.mBrightnessLevel + " to " + newBrightnessLevel);
        }
        synchronized (this.mKeyboardBacklightListenerRecords) {
            for (int i = 0; i < this.mKeyboardBacklightListenerRecords.size(); i++) {
                android.hardware.input.IKeyboardBacklightState callbackState = new android.hardware.input.IKeyboardBacklightState();
                callbackState.brightnessLevel = newBrightnessLevel;
                callbackState.maxBrightnessLevel = state.getNumBrightnessChangeSteps();
                this.mKeyboardBacklightListenerRecords.valueAt(i).notifyKeyboardBacklightChanged(deviceId, callbackState, true);
            }
        }
    }

    private void maybeBackupBacklightBrightness(android.view.InputDevice inputDevice, android.hardware.lights.Light keyboardBacklight, int brightnessValue) {
        if (com.android.server.input.InputFeatureFlagProvider.isAmbientKeyboardBacklightControlEnabled()) {
            return;
        }
        synchronized (this.mDataStore) {
            try {
                this.mDataStore.setKeyboardBacklightBrightness(inputDevice.getDescriptor(), keyboardBacklight.getId(), brightnessValue);
            } finally {
                this.mDataStore.saveIfNeeded();
            }
        }
    }

    private void maybeRestoreBacklightBrightness(android.view.InputDevice inputDevice, android.hardware.lights.Light keyboardBacklight) {
        java.util.OptionalInt brightness;
        if (com.android.server.input.InputFeatureFlagProvider.isAmbientKeyboardBacklightControlEnabled()) {
            return;
        }
        com.android.server.input.KeyboardBacklightController.KeyboardBacklightState state = this.mKeyboardBacklights.get(inputDevice.getId());
        synchronized (this.mDataStore) {
            brightness = this.mDataStore.getKeyboardBacklightBrightness(inputDevice.getDescriptor(), keyboardBacklight.getId());
        }
        if (state != null && brightness.isPresent()) {
            int brightnessValue = java.lang.Math.max(0, java.lang.Math.min(255, brightness.getAsInt()));
            int newLevel = java.util.Arrays.binarySearch(state.mBrightnessValueForLevel, brightnessValue);
            if (newLevel < 0) {
                newLevel = java.lang.Math.min(state.getNumBrightnessChangeSteps(), -(newLevel + 1));
            }
            state.setBrightnessLevel(newLevel);
            if (DEBUG) {
                android.util.Slog.d(TAG, "Restoring brightness level " + brightness.getAsInt());
            }
        }
    }

    private void handleUserActivity() {
        if (!this.mIsInteractive) {
            return;
        }
        this.mIsBacklightOn = true;
        for (int i = 0; i < this.mKeyboardBacklights.size(); i++) {
            com.android.server.input.KeyboardBacklightController.KeyboardBacklightState state = this.mKeyboardBacklights.valueAt(i);
            state.onBacklightStateChanged();
        }
        this.mHandler.removeMessages(5);
        this.mHandler.sendEmptyMessageAtTime(5, android.os.SystemClock.uptimeMillis() + USER_INACTIVITY_THRESHOLD_MILLIS);
    }

    private void handleUserInactivity() {
        this.mIsBacklightOn = false;
        for (int i = 0; i < this.mKeyboardBacklights.size(); i++) {
            com.android.server.input.KeyboardBacklightController.KeyboardBacklightState state = this.mKeyboardBacklights.valueAt(i);
            state.onBacklightStateChanged();
        }
    }

    public void handleInteractiveStateChange(boolean isInteractive) {
        this.mIsInteractive = isInteractive;
        if (isInteractive) {
            handleUserActivity();
        } else {
            handleUserInactivity();
        }
        updateAmbientLightListener();
    }

    public void handleAmbientLightValueChanged(int brightnessValue) {
        this.mAmbientBacklightValue = brightnessValue;
        for (int i = 0; i < this.mKeyboardBacklights.size(); i++) {
            com.android.server.input.KeyboardBacklightController.KeyboardBacklightState state = this.mKeyboardBacklights.valueAt(i);
            state.onAmbientBacklightValueChanged();
        }
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
                updateKeyboardBacklight(((java.lang.Integer) msg.obj).intValue(), com.android.server.input.KeyboardBacklightController.Direction.DIRECTION_UP);
                return true;
            case 3:
                updateKeyboardBacklight(((java.lang.Integer) msg.obj).intValue(), com.android.server.input.KeyboardBacklightController.Direction.DIRECTION_DOWN);
                return true;
            case 4:
                handleUserActivity();
                return true;
            case 5:
                handleUserInactivity();
                return true;
            case 6:
                handleInteractiveStateChange(((java.lang.Boolean) msg.obj).booleanValue());
                return true;
            default:
                return false;
        }
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceAdded(int deviceId) {
        onInputDeviceChanged(deviceId);
        updateAmbientLightListener();
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceRemoved(int deviceId) {
        this.mKeyboardBacklights.remove(deviceId);
        updateAmbientLightListener();
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceChanged(int deviceId) {
        android.view.InputDevice inputDevice = getInputDevice(deviceId);
        if (inputDevice == null) {
            return;
        }
        android.hardware.lights.Light keyboardBacklight = getKeyboardBacklight(inputDevice);
        if (keyboardBacklight == null) {
            this.mKeyboardBacklights.remove(deviceId);
            return;
        }
        com.android.server.input.KeyboardBacklightController.KeyboardBacklightState state = this.mKeyboardBacklights.get(deviceId);
        if (state != null && state.mLight.getId() == keyboardBacklight.getId()) {
            return;
        }
        this.mKeyboardBacklights.put(deviceId, new com.android.server.input.KeyboardBacklightController.KeyboardBacklightState(deviceId, keyboardBacklight));
        maybeRestoreBacklightBrightness(inputDevice, keyboardBacklight);
    }

    private android.view.InputDevice getInputDevice(int deviceId) {
        android.hardware.input.InputManager inputManager = (android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class);
        if (inputManager != null) {
            return inputManager.getInputDevice(deviceId);
        }
        return null;
    }

    private android.hardware.lights.Light getKeyboardBacklight(android.view.InputDevice inputDevice) {
        for (android.hardware.lights.Light light : inputDevice.getLightsManager().getLights()) {
            if (light.getType() == 10003 && light.hasBrightnessControl()) {
                return light;
            }
        }
        return null;
    }

    @Override // com.android.server.input.InputManagerService.KeyboardBacklightControllerInterface
    public void registerKeyboardBacklightListener(android.hardware.input.IKeyboardBacklightListener listener, int pid) {
        synchronized (this.mKeyboardBacklightListenerRecords) {
            if (this.mKeyboardBacklightListenerRecords.get(pid) != null) {
                throw new java.lang.IllegalStateException("The calling process has already registered a KeyboardBacklightListener.");
            }
            com.android.server.input.KeyboardBacklightController.KeyboardBacklightListenerRecord record = new com.android.server.input.KeyboardBacklightController.KeyboardBacklightListenerRecord(pid, listener);
            try {
                listener.asBinder().linkToDeath(record, 0);
                this.mKeyboardBacklightListenerRecords.put(pid, record);
            } catch (android.os.RemoteException ex) {
                throw new java.lang.RuntimeException(ex);
            }
        }
    }

    @Override // com.android.server.input.InputManagerService.KeyboardBacklightControllerInterface
    public void unregisterKeyboardBacklightListener(android.hardware.input.IKeyboardBacklightListener listener, int pid) {
        synchronized (this.mKeyboardBacklightListenerRecords) {
            com.android.server.input.KeyboardBacklightController.KeyboardBacklightListenerRecord record = this.mKeyboardBacklightListenerRecords.get(pid);
            if (record == null) {
                throw new java.lang.IllegalStateException("The calling process has no registered KeyboardBacklightListener.");
            }
            if (record.mListener.asBinder() != listener.asBinder()) {
                throw new java.lang.IllegalStateException("The calling process has a different registered KeyboardBacklightListener.");
            }
            record.mListener.asBinder().unlinkToDeath(record, 0);
            this.mKeyboardBacklightListenerRecords.remove(pid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onKeyboardBacklightListenerDied(int pid) {
        synchronized (this.mKeyboardBacklightListenerRecords) {
            this.mKeyboardBacklightListenerRecords.remove(pid);
        }
    }

    public void onKeyboardBacklightUEvent(android.os.UEventObserver.UEvent event) {
        if ("ADD".equalsIgnoreCase(event.get("ACTION")) && "LEDS".equalsIgnoreCase(event.get("SUBSYSTEM"))) {
            java.lang.String devPath = event.get("DEVPATH");
            if (isValidBacklightNodePath(devPath)) {
                this.mNative.sysfsNodeChanged("/sys" + devPath);
            }
        }
    }

    private void updateAmbientLightListener() {
        if (!com.android.server.input.InputFeatureFlagProvider.isAmbientKeyboardBacklightControlEnabled()) {
            return;
        }
        boolean needToListenAmbientLightSensor = false;
        for (int i = 0; i < this.mKeyboardBacklights.size(); i++) {
            needToListenAmbientLightSensor |= this.mKeyboardBacklights.valueAt(i).mUseAmbientController;
        }
        boolean needToListenAmbientLightSensor2 = needToListenAmbientLightSensor & this.mIsInteractive;
        if (needToListenAmbientLightSensor2 && this.mAmbientListener == null) {
            this.mAmbientListener = new com.android.server.input.AmbientKeyboardBacklightController.AmbientKeyboardBacklightListener() { // from class: com.android.server.input.KeyboardBacklightController$$ExternalSyntheticLambda1
                @Override // com.android.server.input.AmbientKeyboardBacklightController.AmbientKeyboardBacklightListener
                public final void onKeyboardBacklightValueChanged(int i2) {
                    this.f$0.handleAmbientLightValueChanged(i2);
                }
            };
            this.mAmbientController.registerAmbientBacklightListener(this.mAmbientListener);
        }
        if (!needToListenAmbientLightSensor2 && this.mAmbientListener != null) {
            this.mAmbientController.unregisterAmbientBacklightListener(this.mAmbientListener);
            this.mAmbientListener = null;
        }
    }

    private static boolean isValidBacklightNodePath(java.lang.String devPath) {
        int index;
        if (android.text.TextUtils.isEmpty(devPath) || (index = devPath.lastIndexOf(47)) < 0) {
            return false;
        }
        java.lang.String backlightNode = devPath.substring(index + 1);
        java.lang.String devPath2 = devPath.substring(0, index);
        return devPath2.endsWith("leds") && backlightNode.contains(UEVENT_KEYBOARD_BACKLIGHT_TAG) && devPath2.lastIndexOf(47) >= 0;
    }

    @Override // com.android.server.input.InputManagerService.KeyboardBacklightControllerInterface
    public void dump(java.io.PrintWriter pw) {
        android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw);
        ipw.println("KbdBacklightController: " + this.mKeyboardBacklights.size() + " keyboard backlights");
        ipw.increaseIndent();
        for (int i = 0; i < this.mKeyboardBacklights.size(); i++) {
            com.android.server.input.KeyboardBacklightController.KeyboardBacklightState state = this.mKeyboardBacklights.valueAt(i);
            ipw.println(i + ": " + state.toString());
        }
        ipw.decreaseIndent();
    }

    private class KeyboardBacklightListenerRecord implements android.os.IBinder.DeathRecipient {
        public final android.hardware.input.IKeyboardBacklightListener mListener;
        public final int mPid;

        KeyboardBacklightListenerRecord(int pid, android.hardware.input.IKeyboardBacklightListener listener) {
            this.mPid = pid;
            this.mListener = listener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            if (com.android.server.input.KeyboardBacklightController.DEBUG) {
                android.util.Slog.d(com.android.server.input.KeyboardBacklightController.TAG, "Keyboard backlight listener for pid " + this.mPid + " died.");
            }
            com.android.server.input.KeyboardBacklightController.this.onKeyboardBacklightListenerDied(this.mPid);
        }

        public void notifyKeyboardBacklightChanged(int deviceId, android.hardware.input.IKeyboardBacklightState state, boolean isTriggeredByKeyPress) {
            try {
                this.mListener.onBrightnessChanged(deviceId, state, isTriggeredByKeyPress);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.input.KeyboardBacklightController.TAG, "Failed to notify process " + this.mPid + " that keyboard backlight changed, assuming it died.", ex);
                binderDied();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class KeyboardBacklightState {
        private android.animation.ValueAnimator mAnimator;
        private int mBrightnessLevel;
        private final int mDeviceId;
        private final android.hardware.lights.Light mLight;
        private boolean mUseAmbientController = com.android.server.input.InputFeatureFlagProvider.isAmbientKeyboardBacklightControlEnabled();
        private final int[] mBrightnessValueForLevel = setupBrightnessLevels();

        KeyboardBacklightState(int deviceId, android.hardware.lights.Light light) {
            this.mDeviceId = deviceId;
            this.mLight = light;
        }

        private int[] setupBrightnessLevels() {
            if (!com.android.server.input.InputFeatureFlagProvider.isKeyboardBacklightCustomLevelsEnabled()) {
                return com.android.server.input.KeyboardBacklightController.DEFAULT_BRIGHTNESS_VALUE_FOR_LEVEL;
            }
            int[] customLevels = this.mLight.getPreferredBrightnessLevels();
            if (customLevels == null || customLevels.length == 0) {
                return com.android.server.input.KeyboardBacklightController.DEFAULT_BRIGHTNESS_VALUE_FOR_LEVEL;
            }
            java.util.TreeSet<java.lang.Integer> brightnessLevels = new java.util.TreeSet<>();
            brightnessLevels.add(0);
            for (int level : customLevels) {
                if (level > 0 && level < 255) {
                    brightnessLevels.add(java.lang.Integer.valueOf(level));
                }
            }
            brightnessLevels.add(255);
            int brightnessChangeSteps = brightnessLevels.size() - 1;
            if (brightnessChangeSteps > 10) {
                return com.android.server.input.KeyboardBacklightController.DEFAULT_BRIGHTNESS_VALUE_FOR_LEVEL;
            }
            int[] result = new int[brightnessLevels.size()];
            int index = 0;
            java.util.Iterator<java.lang.Integer> it = brightnessLevels.iterator();
            while (it.hasNext()) {
                int val = it.next().intValue();
                result[index] = val;
                index++;
            }
            return result;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getNumBrightnessChangeSteps() {
            return this.mBrightnessValueForLevel.length - 1;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onBacklightStateChanged() {
            int toValue = this.mUseAmbientController ? com.android.server.input.KeyboardBacklightController.this.mAmbientBacklightValue : this.mBrightnessValueForLevel[this.mBrightnessLevel];
            setBacklightValue(com.android.server.input.KeyboardBacklightController.this.mIsBacklightOn ? toValue : 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setBrightnessLevel(int brightnessLevel) {
            this.mUseAmbientController = false;
            if (com.android.server.input.KeyboardBacklightController.this.mIsBacklightOn) {
                setBacklightValue(this.mBrightnessValueForLevel[brightnessLevel]);
            }
            this.mBrightnessLevel = brightnessLevel;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void onAmbientBacklightValueChanged() {
            if (com.android.server.input.KeyboardBacklightController.this.mIsBacklightOn && this.mUseAmbientController) {
                setBacklightValue(com.android.server.input.KeyboardBacklightController.this.mAmbientBacklightValue);
            }
        }

        private void cancelAnimation() {
            if (this.mAnimator != null && this.mAnimator.isRunning()) {
                this.mAnimator.cancel();
            }
        }

        private void setBacklightValue(int toValue) {
            int fromValue = android.graphics.Color.alpha(com.android.server.input.KeyboardBacklightController.this.mNative.getLightColor(this.mDeviceId, this.mLight.getId()));
            if (fromValue == toValue) {
                return;
            }
            if (com.android.server.input.InputFeatureFlagProvider.isKeyboardBacklightAnimationEnabled()) {
                startAnimation(fromValue, toValue);
            } else {
                com.android.server.input.KeyboardBacklightController.this.mNative.setLightColor(this.mDeviceId, this.mLight.getId(), android.graphics.Color.argb(toValue, 0, 0, 0));
            }
        }

        private void startAnimation(int fromValue, int toValue) {
            cancelAnimation();
            this.mAnimator = com.android.server.input.KeyboardBacklightController.this.mAnimatorFactory.makeIntAnimator(fromValue, toValue);
            this.mAnimator.addUpdateListener(new android.animation.ValueAnimator.AnimatorUpdateListener() { // from class: com.android.server.input.KeyboardBacklightController$KeyboardBacklightState$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(android.animation.ValueAnimator valueAnimator) {
                    this.f$0.lambda$startAnimation$0(valueAnimator);
                }
            });
            this.mAnimator.setDuration(com.android.server.input.KeyboardBacklightController.TRANSITION_ANIMATION_DURATION_MILLIS).start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$startAnimation$0(android.animation.ValueAnimator animation) {
            com.android.server.input.KeyboardBacklightController.this.mNative.setLightColor(this.mDeviceId, this.mLight.getId(), android.graphics.Color.argb(((java.lang.Integer) animation.getAnimatedValue()).intValue(), 0, 0, 0));
        }

        public java.lang.String toString() {
            return "KeyboardBacklightState{Light=" + this.mLight.getId() + ", BrightnessLevel=" + this.mBrightnessLevel + "}";
        }
    }
}
