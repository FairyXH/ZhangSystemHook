package com.android.server.bluetooth;

/* JADX INFO: loaded from: classes.dex */
class BluetoothServiceBinder extends android.bluetooth.IBluetoothManager.Stub {
    private static final java.lang.String TAG = com.android.server.bluetooth.BluetoothServiceBinder.class.getSimpleName();
    private boolean DBG;
    private final android.app.AppOpsManager mAppOpsManager;
    private final com.android.server.bluetooth.BluetoothManagerService mBluetoothManagerService;
    private final android.content.Context mContext;
    private final android.permission.PermissionManager mPermissionManager;
    private final com.android.server.bluetooth.BtPermissionUtils mPermissionUtils;
    private final android.os.UserManager mUserManager;
    private final android.os.Looper unusedmLooper;

    BluetoothServiceBinder(com.android.server.bluetooth.BluetoothManagerService bms, android.os.Looper looper, android.content.Context ctx, android.os.UserManager userManager) {
        this.DBG = !android.os.SystemProperties.getBoolean("ro.build.release_type", false) || android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
        this.mBluetoothManagerService = bms;
        this.unusedmLooper = looper;
        this.mContext = ctx;
        this.mUserManager = userManager;
        this.mAppOpsManager = (android.app.AppOpsManager) java.util.Objects.requireNonNull((android.app.AppOpsManager) ctx.getSystemService(android.app.AppOpsManager.class), "AppOpsManager system service cannot be null");
        this.mPermissionManager = (android.permission.PermissionManager) java.util.Objects.requireNonNull((android.permission.PermissionManager) ctx.getSystemService(android.permission.PermissionManager.class), "PermissionManager system service cannot be null");
        this.mPermissionUtils = new com.android.server.bluetooth.BtPermissionUtils(ctx);
    }

    public android.os.IBinder registerAdapter(android.bluetooth.IBluetoothManagerCallback callback) {
        java.util.Objects.requireNonNull(callback, "Callback cannot be null in registerAdapter");
        android.bluetooth.IBluetooth bluetooth = this.mBluetoothManagerService.registerAdapter(callback);
        if (bluetooth == null) {
            return null;
        }
        return bluetooth.asBinder();
    }

    public void unregisterAdapter(android.bluetooth.IBluetoothManagerCallback callback) {
        java.util.Objects.requireNonNull(callback, "Callback cannot be null in unregisterAdapter");
        this.mBluetoothManagerService.unregisterAdapter(callback);
    }

    public boolean enable(android.content.AttributionSource source) {
        java.util.Objects.requireNonNull(source, "AttributionSource cannot be null in enable");
        java.lang.String errorMsg = this.mPermissionUtils.callerCanToggle(this.mContext, source, this.mUserManager, this.mAppOpsManager, this.mPermissionManager, com.android.server.bluetooth.IOplusBluetoothManagerServiceExt.FLAG_ENABLE, true);
        if (!errorMsg.isEmpty()) {
            com.android.server.bluetooth.Log.d(TAG, "enable(): FAILED: " + errorMsg);
            return false;
        }
        return this.mBluetoothManagerService.enable(source.getPackageName());
    }

    public boolean enableNoAutoConnect(android.content.AttributionSource source) {
        java.util.Objects.requireNonNull(source, "AttributionSource cannot be null in enableNoAutoConnect");
        java.lang.String errorMsg = this.mPermissionUtils.callerCanToggle(this.mContext, source, this.mUserManager, this.mAppOpsManager, this.mPermissionManager, "enableNoAutoConnect", false);
        if (!errorMsg.isEmpty()) {
            com.android.server.bluetooth.Log.d(TAG, "enableNoAutoConnect(): FAILED: " + errorMsg);
            return false;
        }
        if (!com.android.server.bluetooth.BtPermissionUtils.isCallerNfc(com.android.server.bluetooth.BtPermissionUtils.getCallingAppId())) {
            throw new java.lang.SecurityException("No permission to enable Bluetooth quietly");
        }
        return this.mBluetoothManagerService.enableNoAutoConnect(source.getPackageName());
    }

    public boolean disable(android.content.AttributionSource source, boolean persist) {
        java.util.Objects.requireNonNull(source, "AttributionSource cannot be null in disable");
        if (!persist) {
            com.android.server.bluetooth.BtPermissionUtils.enforcePrivileged(this.mContext);
        }
        java.lang.String errorMsg = this.mPermissionUtils.callerCanToggle(this.mContext, source, this.mUserManager, this.mAppOpsManager, this.mPermissionManager, "disable", true);
        if (!errorMsg.isEmpty()) {
            com.android.server.bluetooth.Log.d(TAG, "disable(): FAILED: " + errorMsg);
            return false;
        }
        return this.mBluetoothManagerService.disable(source.getPackageName(), persist);
    }

    public int getState() {
        if (!com.android.server.bluetooth.BtPermissionUtils.isCallerSystem(com.android.server.bluetooth.BtPermissionUtils.getCallingAppId()) && !this.mPermissionUtils.checkIfCallerIsForegroundUser(this.mUserManager)) {
            com.android.server.bluetooth.Log.w(TAG, "getState(): UNAUTHORIZED. Report OFF for non-active and non system user");
            return 10;
        }
        return this.mBluetoothManagerService.getState();
    }

    public java.lang.String getAddress(android.content.AttributionSource source) {
        java.util.Objects.requireNonNull(source, "AttributionSource cannot be null in getAddress");
        if (!com.android.server.bluetooth.BtPermissionUtils.checkConnectPermissionForDataDelivery(this.mContext, this.mPermissionManager, source, "getAddress")) {
            return null;
        }
        if (!com.android.server.bluetooth.BtPermissionUtils.isCallerSystem(com.android.server.bluetooth.BtPermissionUtils.getCallingAppId()) && !this.mPermissionUtils.checkIfCallerIsForegroundUser(this.mUserManager)) {
            com.android.server.bluetooth.Log.w(TAG, "getAddress(): Not allowed for non-active and non system user");
            return null;
        }
        if (this.mContext.checkCallingOrSelfPermission("android.permission.LOCAL_MAC_ADDRESS") != 0) {
            com.android.server.bluetooth.Log.w(TAG, "getAddress(): Client does not have LOCAL_MAC_ADDRESS permission");
            return "02:00:00:00:00:00";
        }
        return this.mBluetoothManagerService.getAddress();
    }

    public java.lang.String getName(android.content.AttributionSource source) {
        java.util.Objects.requireNonNull(source, "AttributionSource cannot be null in getName");
        if (!com.android.server.bluetooth.BtPermissionUtils.checkConnectPermissionForDataDelivery(this.mContext, this.mPermissionManager, source, "getName")) {
            return null;
        }
        if (!com.android.server.bluetooth.BtPermissionUtils.isCallerSystem(com.android.server.bluetooth.BtPermissionUtils.getCallingAppId()) && !this.mPermissionUtils.checkIfCallerIsForegroundUser(this.mUserManager)) {
            com.android.server.bluetooth.Log.w(TAG, "getName(): not allowed for non-active and non system user");
            return null;
        }
        return this.mBluetoothManagerService.getName();
    }

    public boolean onFactoryReset(android.content.AttributionSource source) {
        java.util.Objects.requireNonNull(source, "AttributionSource cannot be null in onFactoryReset");
        com.android.server.bluetooth.BtPermissionUtils.enforcePrivileged(this.mContext);
        if (!com.android.server.bluetooth.BtPermissionUtils.checkConnectPermissionForDataDelivery(this.mContext, this.mPermissionManager, source, "onFactoryReset")) {
            return false;
        }
        return this.mBluetoothManagerService.onFactoryReset();
    }

    public boolean isBleScanAvailable() {
        return this.mBluetoothManagerService.isBleScanAvailable();
    }

    public boolean enableBle(android.content.AttributionSource source, android.os.IBinder token) {
        java.util.Objects.requireNonNull(source, "AttributionSource cannot be null in enableBle");
        java.util.Objects.requireNonNull(token, "IBinder cannot be null in enableBle");
        java.lang.String errorMsg = this.mPermissionUtils.callerCanToggle(this.mContext, source, this.mUserManager, this.mAppOpsManager, this.mPermissionManager, "enableBle", false);
        if (!errorMsg.isEmpty()) {
            com.android.server.bluetooth.Log.d(TAG, "enableBle(): FAILED: " + errorMsg);
            return false;
        }
        return this.mBluetoothManagerService.enableBle(source.getPackageName(), token);
    }

    public boolean disableBle(android.content.AttributionSource source, android.os.IBinder token) {
        java.util.Objects.requireNonNull(source, "AttributionSource cannot be null in disableBle");
        java.util.Objects.requireNonNull(token, "IBinder cannot be null in disableBle");
        java.lang.String errorMsg = this.mPermissionUtils.callerCanToggle(this.mContext, source, this.mUserManager, this.mAppOpsManager, this.mPermissionManager, "disableBle", false);
        if (!errorMsg.isEmpty()) {
            com.android.server.bluetooth.Log.d(TAG, "disableBle(): FAILED: " + errorMsg);
            return false;
        }
        return this.mBluetoothManagerService.disableBle(source.getPackageName(), token);
    }

    public boolean isHearingAidProfileSupported() {
        return this.mBluetoothManagerService.isHearingAidProfileSupported();
    }

    public int setBtHciSnoopLogMode(int mode) {
        com.android.server.bluetooth.BtPermissionUtils.enforcePrivileged(this.mContext);
        return this.mBluetoothManagerService.setBtHciSnoopLogMode(mode);
    }

    public int getBtHciSnoopLogMode() {
        com.android.server.bluetooth.BtPermissionUtils.enforcePrivileged(this.mContext);
        return this.mBluetoothManagerService.getBtHciSnoopLogMode();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int handleShellCommand(android.os.ParcelFileDescriptor in, android.os.ParcelFileDescriptor out, android.os.ParcelFileDescriptor err, java.lang.String[] args) {
        return new com.android.server.bluetooth.BluetoothShellCommand(this.mBluetoothManagerService).exec(this, in.getFileDescriptor(), out.getFileDescriptor(), err.getFileDescriptor(), args);
    }

    public boolean isAutoOnSupported() {
        com.android.server.bluetooth.BtPermissionUtils.enforcePrivileged(this.mContext);
        return this.mBluetoothManagerService.isAutoOnSupported();
    }

    public boolean isAutoOnEnabled() {
        com.android.server.bluetooth.BtPermissionUtils.enforcePrivileged(this.mContext);
        return this.mBluetoothManagerService.isAutoOnEnabled();
    }

    public void setAutoOnEnabled(boolean status) {
        com.android.server.bluetooth.BtPermissionUtils.enforcePrivileged(this.mContext);
        this.mBluetoothManagerService.setAutoOnEnabled(status);
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        if (this.mContext.checkCallingOrSelfPermission("android.permission.DUMP") != 0) {
            com.android.server.bluetooth.Log.w(TAG, "dump(): Client does not have DUMP permission");
        } else {
            this.mBluetoothManagerService.dump(fd, writer, args);
        }
    }

    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        if (super.onTransact(code, data, reply, flags)) {
            return true;
        }
        if (this.DBG) {
            com.android.server.bluetooth.Log.d(TAG, "onTransact will goto OplusBluetoothManagerServiceExtImpl");
        }
        return this.mBluetoothManagerService.mOplusBms.oplusOnTransact(code, data, reply, flags);
    }
}
