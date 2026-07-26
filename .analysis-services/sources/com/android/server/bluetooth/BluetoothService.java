package com.android.server.bluetooth;

/* JADX INFO: loaded from: classes.dex */
public class BluetoothService extends com.android.server.SystemService {
    private com.android.server.bluetooth.BluetoothManagerService mBluetoothManagerService;
    private android.os.HandlerThread mHandlerThread;
    private boolean mInitialized;

    public BluetoothService(android.content.Context context) {
        super(context);
        this.mInitialized = false;
        this.mHandlerThread = new android.os.HandlerThread("BluetoothManagerService");
        this.mHandlerThread.start();
        this.mBluetoothManagerService = new com.android.server.bluetooth.BluetoothManagerService(context, this.mHandlerThread.getLooper());
    }

    private void initialize(com.android.server.SystemService.TargetUser user) {
        if (!this.mInitialized) {
            this.mBluetoothManagerService.handleOnBootPhase(user.getUserHandle());
            this.mInitialized = true;
        }
    }

    @Override // com.android.server.SystemService
    public void onStart() {
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 500) {
            publishBinderService("bluetooth_manager", this.mBluetoothManagerService.getBinder());
        }
    }

    @Override // com.android.server.SystemService
    public void onUserStarting(com.android.server.SystemService.TargetUser user) {
        if (!android.os.UserManager.isHeadlessSystemUserMode()) {
            initialize(user);
        }
    }

    @Override // com.android.server.SystemService
    public void onUserSwitching(com.android.server.SystemService.TargetUser from, com.android.server.SystemService.TargetUser to) {
        if (!this.mInitialized) {
            initialize(to);
        } else {
            this.mBluetoothManagerService.onSwitchUser(to.getUserHandle());
        }
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocking(com.android.server.SystemService.TargetUser user) {
        this.mBluetoothManagerService.handleOnUnlockUser(user.getUserHandle());
    }
}
