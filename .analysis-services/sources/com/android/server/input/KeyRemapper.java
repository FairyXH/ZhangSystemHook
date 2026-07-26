package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
final class KeyRemapper implements android.hardware.input.InputManager.InputDeviceListener {
    private static final int MSG_CLEAR_ALL_REMAPPING = 3;
    private static final int MSG_REMAP_KEY = 2;
    private static final int MSG_UPDATE_EXISTING_DEVICES = 1;
    private final android.content.Context mContext;
    private final com.android.server.input.PersistentDataStore mDataStore;
    private final android.os.Handler mHandler;
    private final com.android.server.input.NativeInputManagerService mNative;

    KeyRemapper(android.content.Context context, com.android.server.input.NativeInputManagerService nativeService, com.android.server.input.PersistentDataStore dataStore, android.os.Looper looper) {
        this.mContext = context;
        this.mNative = nativeService;
        this.mDataStore = dataStore;
        this.mHandler = new android.os.Handler(looper, new android.os.Handler.Callback() { // from class: com.android.server.input.KeyRemapper$$ExternalSyntheticLambda0
            @Override // android.os.Handler.Callback
            public final boolean handleMessage(android.os.Message message) {
                return this.f$0.handleMessage(message);
            }
        });
    }

    public void systemRunning() {
        android.hardware.input.InputManager inputManager = (android.hardware.input.InputManager) java.util.Objects.requireNonNull((android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class));
        inputManager.registerInputDeviceListener(this, this.mHandler);
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 1, inputManager.getInputDeviceIds());
        this.mHandler.sendMessage(msg);
    }

    public void remapKey(int fromKey, int toKey) {
        if (!supportRemapping()) {
            return;
        }
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 2, fromKey, toKey);
        this.mHandler.sendMessage(msg);
    }

    public void clearAllKeyRemappings() {
        if (!supportRemapping()) {
            return;
        }
        android.os.Message msg = android.os.Message.obtain(this.mHandler, 3);
        this.mHandler.sendMessage(msg);
    }

    public java.util.Map<java.lang.Integer, java.lang.Integer> getKeyRemapping() {
        java.util.Map<java.lang.Integer, java.lang.Integer> keyRemapping;
        if (!supportRemapping()) {
            return new android.util.ArrayMap();
        }
        synchronized (this.mDataStore) {
            keyRemapping = this.mDataStore.getKeyRemapping();
        }
        return keyRemapping;
    }

    private void addKeyRemapping(int fromKey, int toKey) {
        android.hardware.input.InputManager inputManager = (android.hardware.input.InputManager) java.util.Objects.requireNonNull((android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class));
        for (int deviceId : inputManager.getInputDeviceIds()) {
            android.view.InputDevice inputDevice = inputManager.getInputDevice(deviceId);
            if (inputDevice != null && !inputDevice.isVirtual() && inputDevice.isFullKeyboard()) {
                this.mNative.addKeyRemapping(deviceId, fromKey, toKey);
            }
        }
    }

    private void remapKeyInternal(int fromKey, int toKey) {
        addKeyRemapping(fromKey, toKey);
        synchronized (this.mDataStore) {
            try {
                if (fromKey == toKey) {
                    this.mDataStore.clearMappedKey(fromKey);
                } else {
                    this.mDataStore.remapKey(fromKey, toKey);
                }
            } finally {
                this.mDataStore.saveIfNeeded();
            }
        }
    }

    private void clearAllRemappingsInternal() {
        synchronized (this.mDataStore) {
            try {
                java.util.Map<java.lang.Integer, java.lang.Integer> keyRemapping = this.mDataStore.getKeyRemapping();
                java.util.Iterator<java.lang.Integer> it = keyRemapping.keySet().iterator();
                while (it.hasNext()) {
                    int fromKey = it.next().intValue();
                    this.mDataStore.clearMappedKey(fromKey);
                    addKeyRemapping(fromKey, fromKey);
                }
            } finally {
                this.mDataStore.saveIfNeeded();
            }
        }
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceAdded(final int deviceId) {
        if (!supportRemapping()) {
            return;
        }
        android.hardware.input.InputManager inputManager = (android.hardware.input.InputManager) java.util.Objects.requireNonNull((android.hardware.input.InputManager) this.mContext.getSystemService(android.hardware.input.InputManager.class));
        android.view.InputDevice inputDevice = inputManager.getInputDevice(deviceId);
        if (inputDevice != null && !inputDevice.isVirtual() && inputDevice.isFullKeyboard()) {
            java.util.Map<java.lang.Integer, java.lang.Integer> remapping = getKeyRemapping();
            remapping.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.input.KeyRemapper$$ExternalSyntheticLambda1
                @Override // java.util.function.BiConsumer
                public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                    this.f$0.lambda$onInputDeviceAdded$0(deviceId, (java.lang.Integer) obj, (java.lang.Integer) obj2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onInputDeviceAdded$0(int deviceId, java.lang.Integer fromKey, java.lang.Integer toKey) {
        this.mNative.addKeyRemapping(deviceId, fromKey.intValue(), toKey.intValue());
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceRemoved(int deviceId) {
    }

    @Override // android.hardware.input.InputManager.InputDeviceListener
    public void onInputDeviceChanged(int deviceId) {
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
                remapKeyInternal(msg.arg1, msg.arg2);
                return true;
            case 3:
                clearAllRemappingsInternal();
                return true;
            default:
                return false;
        }
    }

    private boolean supportRemapping() {
        return android.util.FeatureFlagUtils.isEnabled(this.mContext, "settings_new_keyboard_modifier_key");
    }
}
