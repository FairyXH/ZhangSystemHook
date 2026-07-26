package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class DeviceStateController {
    private final int mConcurrentDisplayDeviceState;
    private int mCurrentState;
    private final int[] mFoldedDeviceStates;
    private final int[] mHalfFoldedDeviceStates;
    private final boolean mMatchBuiltInDisplayOrientationToDefaultDisplay;
    private final int[] mOpenDeviceStates;
    private final int[] mRearDisplayDeviceStates;
    private final int[] mReverseRotationAroundZAxisStates;
    private final com.android.server.wm.WindowManagerGlobalLock mWmLock;
    final java.util.Map<java.util.function.Consumer<com.android.server.wm.DeviceStateController.DeviceState>, java.util.concurrent.Executor> mDeviceStateCallbacks = new android.util.ArrayMap();
    private com.android.server.wm.DeviceStateController.DeviceState mCurrentDeviceState = com.android.server.wm.DeviceStateController.DeviceState.UNKNOWN;

    public enum DeviceState {
        UNKNOWN,
        OPEN,
        FOLDED,
        HALF_FOLDED,
        REAR,
        CONCURRENT
    }

    DeviceStateController(android.content.Context context, com.android.server.wm.WindowManagerGlobalLock wmLock) {
        this.mWmLock = wmLock;
        this.mOpenDeviceStates = context.getResources().getIntArray(android.R.array.config_notificationSignalExtractors);
        this.mHalfFoldedDeviceStates = context.getResources().getIntArray(android.R.array.config_globalActionsList);
        this.mFoldedDeviceStates = context.getResources().getIntArray(android.R.array.config_face_acquire_vendor_keyguard_ignorelist);
        this.mRearDisplayDeviceStates = context.getResources().getIntArray(android.R.array.config_perDeviceStateRotationLockDefaults);
        this.mConcurrentDisplayDeviceState = context.getResources().getInteger(android.R.integer.config_defaultUiModeType);
        this.mReverseRotationAroundZAxisStates = context.getResources().getIntArray(android.R.array.config_deviceSpecificSystemServices);
        this.mMatchBuiltInDisplayOrientationToDefaultDisplay = context.getResources().getBoolean(android.R.bool.config_letterboxIsDisplayRotationImmersiveAppCompatPolicyEnabled);
    }

    void registerDeviceStateCallback(java.util.function.Consumer<com.android.server.wm.DeviceStateController.DeviceState> callback, java.util.concurrent.Executor executor) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWmLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mDeviceStateCallbacks.put(callback, executor);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    void unregisterDeviceStateCallback(java.util.function.Consumer<com.android.server.wm.DeviceStateController.DeviceState> callback) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWmLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mDeviceStateCallbacks.remove(callback);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    boolean shouldReverseRotationDirectionAroundZAxis(com.android.server.wm.DisplayContent displayContent) {
        if (!displayContent.isDefaultDisplay) {
            return false;
        }
        return com.android.internal.util.ArrayUtils.contains(this.mReverseRotationAroundZAxisStates, this.mCurrentState);
    }

    boolean shouldMatchBuiltInDisplayOrientationToReverseDefaultDisplay() {
        return this.mMatchBuiltInDisplayOrientationToDefaultDisplay;
    }

    public void onDeviceStateReceivedByDisplayManager(int state) {
        final com.android.server.wm.DeviceStateController.DeviceState deviceState;
        this.mCurrentState = state;
        if (com.android.internal.util.ArrayUtils.contains(this.mHalfFoldedDeviceStates, state)) {
            deviceState = com.android.server.wm.DeviceStateController.DeviceState.HALF_FOLDED;
        } else if (com.android.internal.util.ArrayUtils.contains(this.mFoldedDeviceStates, state)) {
            deviceState = com.android.server.wm.DeviceStateController.DeviceState.FOLDED;
        } else if (com.android.internal.util.ArrayUtils.contains(this.mRearDisplayDeviceStates, state)) {
            deviceState = com.android.server.wm.DeviceStateController.DeviceState.REAR;
        } else if (com.android.internal.util.ArrayUtils.contains(this.mOpenDeviceStates, state)) {
            deviceState = com.android.server.wm.DeviceStateController.DeviceState.OPEN;
        } else if (state == this.mConcurrentDisplayDeviceState) {
            deviceState = com.android.server.wm.DeviceStateController.DeviceState.CONCURRENT;
        } else {
            deviceState = com.android.server.wm.DeviceStateController.DeviceState.UNKNOWN;
        }
        if (this.mCurrentDeviceState == null || !this.mCurrentDeviceState.equals(deviceState)) {
            this.mCurrentDeviceState = deviceState;
            java.util.List<android.util.Pair<java.util.function.Consumer<com.android.server.wm.DeviceStateController.DeviceState>, java.util.concurrent.Executor>> entries = copyDeviceStateCallbacks();
            for (int i = 0; i < entries.size(); i++) {
                final android.util.Pair<java.util.function.Consumer<com.android.server.wm.DeviceStateController.DeviceState>, java.util.concurrent.Executor> entry = entries.get(i);
                ((java.util.concurrent.Executor) entry.second).execute(new java.lang.Runnable() { // from class: com.android.server.wm.DeviceStateController$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        ((java.util.function.Consumer) entry.first).accept(deviceState);
                    }
                });
            }
        }
    }

    java.util.List<android.util.Pair<java.util.function.Consumer<com.android.server.wm.DeviceStateController.DeviceState>, java.util.concurrent.Executor>> copyDeviceStateCallbacks() {
        final java.util.List<android.util.Pair<java.util.function.Consumer<com.android.server.wm.DeviceStateController.DeviceState>, java.util.concurrent.Executor>> entries = new java.util.ArrayList<>();
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mWmLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mDeviceStateCallbacks.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.wm.DeviceStateController$$ExternalSyntheticLambda0
                    @Override // java.util.function.BiConsumer
                    public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                        entries.add(new android.util.Pair((java.util.function.Consumer) obj, (java.util.concurrent.Executor) obj2));
                    }
                });
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return entries;
    }
}
