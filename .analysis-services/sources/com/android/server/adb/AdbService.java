package com.android.server.adb;

/* JADX INFO: loaded from: classes.dex */
public class AdbService extends android.debug.IAdbManager.Stub {
    static final java.lang.String ADBD = "adbd";
    static final java.lang.String CTL_START = "ctl.start";
    static final java.lang.String CTL_STOP = "ctl.stop";
    private static final boolean DEBUG = android.os.SystemProperties.getBoolean("persist.sys.assert.panic", false);
    private static final java.lang.String TAG = "AdbService";
    private static final java.lang.String USB_PERSISTENT_CONFIG_PROPERTY = "persist.sys.usb.config";
    private static final java.lang.String WIFI_PERSISTENT_CONFIG_PROPERTY = "persist.adb.tls_server.enable";
    private final android.os.RemoteCallbackList<android.debug.IAdbCallback> mCallbacks;
    java.util.concurrent.atomic.AtomicInteger mConnectionPort;
    private com.android.server.adb.AdbDebuggingManager.AdbConnectionPortPoller mConnectionPortPoller;
    private final android.content.ContentResolver mContentResolver;
    private final android.content.Context mContext;
    private com.android.server.adb.AdbDebuggingManager mDebuggingManager;
    private boolean mIsAdbUsbEnabled;
    private boolean mIsAdbWifiEnabled;
    private android.database.ContentObserver mObserver;
    private final com.android.server.adb.AdbService.AdbConnectionPortListener mPortListener;
    private final android.util.ArrayMap<android.os.IBinder, android.debug.IAdbTransport> mTransports;

    public static class Lifecycle extends com.android.server.SystemService {
        private com.android.server.adb.AdbService mAdbService;

        public Lifecycle(android.content.Context context) {
            super(context);
        }

        @Override // com.android.server.SystemService
        public void onStart() {
            this.mAdbService = new com.android.server.adb.AdbService(getContext());
            publishBinderService(com.android.server.integrity.AppIntegrityManagerServiceImpl.ADB_INSTALLER, this.mAdbService);
        }

        @Override // com.android.server.SystemService
        public void onBootPhase(int phase) {
            if (phase == 550) {
                this.mAdbService.systemReady();
            } else if (phase == 1000) {
                com.android.server.FgThread.getHandler().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new java.util.function.Consumer() { // from class: com.android.server.adb.AdbService$Lifecycle$$ExternalSyntheticLambda0
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        ((com.android.server.adb.AdbService) obj).bootCompleted();
                    }
                }, this.mAdbService));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    class AdbManagerInternalImpl extends android.debug.AdbManagerInternal {
        private AdbManagerInternalImpl() {
        }

        public void registerTransport(android.debug.IAdbTransport transport) {
            com.android.server.adb.AdbService.this.mTransports.put(transport.asBinder(), transport);
        }

        public void unregisterTransport(android.debug.IAdbTransport transport) {
            com.android.server.adb.AdbService.this.mTransports.remove(transport.asBinder());
        }

        public boolean isAdbEnabled(byte transportType) {
            if (transportType == 0) {
                return com.android.server.adb.AdbService.this.mIsAdbUsbEnabled;
            }
            if (transportType == 1) {
                return com.android.server.adb.AdbService.this.mIsAdbWifiEnabled;
            }
            throw new java.lang.IllegalArgumentException("isAdbEnabled called with unimplemented transport type=" + ((int) transportType));
        }

        public java.io.File getAdbKeysFile() {
            if (com.android.server.adb.AdbService.this.mDebuggingManager == null) {
                return null;
            }
            return com.android.server.adb.AdbService.this.mDebuggingManager.getUserKeyFile();
        }

        public java.io.File getAdbTempKeysFile() {
            if (com.android.server.adb.AdbService.this.mDebuggingManager == null) {
                return null;
            }
            return com.android.server.adb.AdbService.this.mDebuggingManager.getAdbTempKeysFile();
        }

        public void notifyKeyFilesUpdated() {
            if (com.android.server.adb.AdbService.this.mDebuggingManager == null) {
                return;
            }
            com.android.server.adb.AdbService.this.mDebuggingManager.notifyKeyFilesUpdated();
        }

        public void startAdbdForTransport(byte transportType) {
            com.android.server.FgThread.getHandler().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.adb.AdbService$AdbManagerInternalImpl$$ExternalSyntheticLambda0(), com.android.server.adb.AdbService.this, true, java.lang.Byte.valueOf(transportType)));
        }

        public void stopAdbdForTransport(byte transportType) {
            com.android.server.FgThread.getHandler().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.server.adb.AdbService$AdbManagerInternalImpl$$ExternalSyntheticLambda0(), com.android.server.adb.AdbService.this, false, java.lang.Byte.valueOf(transportType)));
        }
    }

    private void registerContentObservers() {
        try {
            this.mObserver = new com.android.server.adb.AdbService.AdbSettingsObserver();
            if (!"1".equals(android.os.SystemProperties.get("SPECIAL_OPLUS_CONFIG"))) {
                this.mContentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("adb_enabled"), false, this.mObserver);
                this.mContentResolver.registerContentObserver(android.provider.Settings.Global.getUriFor("adb_wifi_enabled"), false, this.mObserver);
            }
        } catch (java.lang.Exception e) {
            android.util.Slog.e(TAG, "Error in registerContentObservers", e);
        }
    }

    private static boolean containsFunction(java.lang.String functions, java.lang.String function) {
        int index = functions.indexOf(function);
        if (index < 0) {
            return false;
        }
        if (index > 0 && functions.charAt(index - 1) != ',') {
            return false;
        }
        int charAfter = function.length() + index;
        if (charAfter < functions.length() && functions.charAt(charAfter) != ',') {
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class AdbSettingsObserver extends android.database.ContentObserver {
        private final android.net.Uri mAdbUsbUri;
        private final android.net.Uri mAdbWifiUri;

        AdbSettingsObserver() {
            super(null);
            this.mAdbUsbUri = android.provider.Settings.Global.getUriFor("adb_enabled");
            this.mAdbWifiUri = android.provider.Settings.Global.getUriFor("adb_wifi_enabled");
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri, int userId) {
            if (this.mAdbUsbUri.equals(uri)) {
                boolean shouldEnable = android.provider.Settings.Global.getInt(com.android.server.adb.AdbService.this.mContentResolver, "adb_enabled", 0) > 0;
                com.android.server.FgThread.getHandler().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.adb.AdbService$AdbSettingsObserver$$ExternalSyntheticLambda0
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                        ((com.android.server.adb.AdbService) obj).setAdbEnabled(((java.lang.Boolean) obj2).booleanValue(), ((java.lang.Byte) obj3).byteValue());
                    }
                }, com.android.server.adb.AdbService.this, java.lang.Boolean.valueOf(shouldEnable), (byte) 0));
            } else if (this.mAdbWifiUri.equals(uri)) {
                boolean shouldEnable2 = android.provider.Settings.Global.getInt(com.android.server.adb.AdbService.this.mContentResolver, "adb_wifi_enabled", 0) > 0;
                com.android.server.FgThread.getHandler().sendMessage(com.android.internal.util.function.pooled.PooledLambda.obtainMessage(new com.android.internal.util.function.TriConsumer() { // from class: com.android.server.adb.AdbService$AdbSettingsObserver$$ExternalSyntheticLambda0
                    public final void accept(java.lang.Object obj, java.lang.Object obj2, java.lang.Object obj3) {
                        ((com.android.server.adb.AdbService) obj).setAdbEnabled(((java.lang.Boolean) obj2).booleanValue(), ((java.lang.Byte) obj3).byteValue());
                    }
                }, com.android.server.adb.AdbService.this, java.lang.Boolean.valueOf(shouldEnable2), (byte) 1));
            }
        }
    }

    private AdbService(android.content.Context context) {
        this.mConnectionPort = new java.util.concurrent.atomic.AtomicInteger(-1);
        this.mPortListener = new com.android.server.adb.AdbService.AdbConnectionPortListener();
        this.mCallbacks = new android.os.RemoteCallbackList<>();
        this.mTransports = new android.util.ArrayMap<>();
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mDebuggingManager = new com.android.server.adb.AdbDebuggingManager(context);
        registerContentObservers();
        com.android.server.LocalServices.addService(android.debug.AdbManagerInternal.class, new com.android.server.adb.AdbService.AdbManagerInternalImpl());
    }

    public void systemReady() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "systemReady");
        }
        int i = 1;
        this.mIsAdbUsbEnabled = android.os.SystemProperties.getBoolean("ro.debuggable", false) || android.os.SystemProperties.getBoolean("ro.oplus.adb.secure", false) || containsFunction(android.os.SystemProperties.get(USB_PERSISTENT_CONFIG_PROPERTY, ""), com.android.server.integrity.AppIntegrityManagerServiceImpl.ADB_INSTALLER) || android.provider.Settings.Global.getInt(this.mContentResolver, "adb_enabled", 0) > 0 || com.android.server.bluetooth.IOplusBluetoothManagerServiceExt.FLAG_ENABLE.equals(android.os.SystemProperties.get("ro.boot.atm", ""));
        android.util.Slog.i(TAG, "mIsAdbUsbEnabled=" + this.mIsAdbUsbEnabled);
        boolean shouldEnableAdbUsb = this.mIsAdbUsbEnabled || android.os.SystemProperties.getBoolean(com.android.server.testharness.TestHarnessModeService.TEST_HARNESS_MODE_PROPERTY, false);
        this.mIsAdbWifiEnabled = "1".equals(android.os.SystemProperties.get(WIFI_PERSISTENT_CONFIG_PROPERTY, "0"));
        try {
            android.provider.Settings.Global.putInt(this.mContentResolver, "adb_enabled", shouldEnableAdbUsb ? 1 : 0);
            android.content.ContentResolver contentResolver = this.mContentResolver;
            if (!this.mIsAdbWifiEnabled) {
                i = 0;
            }
            android.provider.Settings.Global.putInt(contentResolver, "adb_wifi_enabled", i);
        } catch (java.lang.SecurityException e) {
            android.util.Slog.d(TAG, "ADB_ENABLED is restricted.");
        }
        int usbAdbCurrentStatus = android.provider.Settings.Global.getInt(this.mContentResolver, "adb_enabled", -1);
        com.android.server.usb.IOplusUsbDeviceFeature oplusUsbDeviceFeature = null;
        if (0 == 0) {
            oplusUsbDeviceFeature = (com.android.server.usb.IOplusUsbDeviceFeature) android.common.OplusFeatureCache.getOrCreate(com.android.server.usb.IOplusUsbDeviceFeature.DEFAULT, new java.lang.Object[0]);
        }
        oplusUsbDeviceFeature.usbAdbFeatureStatusRecord(this.mContext, "usbAdbCurrentStatus: " + usbAdbCurrentStatus + ", shouldEnableAdbUsb: " + (shouldEnableAdbUsb ? "1" : "0"), "usb adb status");
    }

    public void bootCompleted() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "boot completed");
        }
        if (this.mDebuggingManager != null) {
            this.mDebuggingManager.setAdbEnabled(this.mIsAdbUsbEnabled, (byte) 0);
            this.mDebuggingManager.setAdbEnabled(this.mIsAdbWifiEnabled, (byte) 1);
        }
    }

    public void allowDebugging(boolean alwaysAllow, java.lang.String publicKey) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_DEBUGGING", null);
        com.android.internal.util.Preconditions.checkStringNotEmpty(publicKey);
        if (this.mDebuggingManager != null) {
            this.mDebuggingManager.allowDebugging(alwaysAllow, publicKey);
        }
    }

    public void denyDebugging() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_DEBUGGING", null);
        if (this.mDebuggingManager != null) {
            this.mDebuggingManager.denyDebugging();
        }
    }

    public void clearDebuggingKeys() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_DEBUGGING", null);
        if (this.mDebuggingManager != null) {
            this.mDebuggingManager.clearDebuggingKeys();
            return;
        }
        throw new java.lang.RuntimeException("Cannot clear ADB debugging keys, AdbDebuggingManager not enabled");
    }

    public boolean isAdbWifiSupported() {
        this.mContext.enforceCallingPermission("android.permission.MANAGE_DEBUGGING", TAG);
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.wifi") || this.mContext.getPackageManager().hasSystemFeature("android.hardware.ethernet");
    }

    public boolean isAdbWifiQrSupported() {
        this.mContext.enforceCallingPermission("android.permission.MANAGE_DEBUGGING", TAG);
        return isAdbWifiSupported() && this.mContext.getPackageManager().hasSystemFeature("android.hardware.camera.any");
    }

    public void allowWirelessDebugging(boolean alwaysAllow, java.lang.String bssid) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_DEBUGGING", null);
        com.android.internal.util.Preconditions.checkStringNotEmpty(bssid);
        if (this.mDebuggingManager != null) {
            this.mDebuggingManager.allowWirelessDebugging(alwaysAllow, bssid);
        }
    }

    public void denyWirelessDebugging() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_DEBUGGING", null);
        if (this.mDebuggingManager != null) {
            this.mDebuggingManager.denyWirelessDebugging();
        }
    }

    public android.debug.FingerprintAndPairDevice[] getPairedDevices() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_DEBUGGING", null);
        if (this.mDebuggingManager == null) {
            return null;
        }
        java.util.Map<java.lang.String, android.debug.PairDevice> map = this.mDebuggingManager.getPairedDevices();
        android.debug.FingerprintAndPairDevice[] ret = new android.debug.FingerprintAndPairDevice[map.size()];
        int i = 0;
        for (java.util.Map.Entry<java.lang.String, android.debug.PairDevice> entry : map.entrySet()) {
            ret[i] = new android.debug.FingerprintAndPairDevice();
            ret[i].keyFingerprint = entry.getKey();
            ret[i].device = entry.getValue();
            i++;
        }
        return ret;
    }

    public void unpairDevice(java.lang.String fingerprint) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_DEBUGGING", null);
        com.android.internal.util.Preconditions.checkStringNotEmpty(fingerprint);
        if (this.mDebuggingManager != null) {
            this.mDebuggingManager.unpairDevice(fingerprint);
        }
    }

    public void enablePairingByPairingCode() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_DEBUGGING", null);
        if (this.mDebuggingManager != null) {
            this.mDebuggingManager.enablePairingByPairingCode();
        }
    }

    public void enablePairingByQrCode(java.lang.String serviceName, java.lang.String password) {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_DEBUGGING", null);
        com.android.internal.util.Preconditions.checkStringNotEmpty(serviceName);
        com.android.internal.util.Preconditions.checkStringNotEmpty(password);
        if (this.mDebuggingManager != null) {
            this.mDebuggingManager.enablePairingByQrCode(serviceName, password);
        }
    }

    public void disablePairing() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_DEBUGGING", null);
        if (this.mDebuggingManager != null) {
            this.mDebuggingManager.disablePairing();
        }
    }

    public int getAdbWirelessPort() {
        this.mContext.enforceCallingOrSelfPermission("android.permission.MANAGE_DEBUGGING", null);
        if (this.mDebuggingManager != null) {
            return this.mDebuggingManager.getAdbWirelessPort();
        }
        return this.mConnectionPort.get();
    }

    public void registerCallback(android.debug.IAdbCallback callback) throws android.os.RemoteException {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Registering callback " + callback);
        }
        this.mCallbacks.register(callback);
    }

    public void unregisterCallback(android.debug.IAdbCallback callback) throws android.os.RemoteException {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Unregistering callback " + callback);
        }
        this.mCallbacks.unregister(callback);
    }

    class AdbConnectionPortListener implements com.android.server.adb.AdbDebuggingManager.AdbConnectionPortListener {
        AdbConnectionPortListener() {
        }

        @Override // com.android.server.adb.AdbDebuggingManager.AdbConnectionPortListener
        public void onPortReceived(int port) {
            if (port > 0 && port <= 65535) {
                com.android.server.adb.AdbService.this.mConnectionPort.set(port);
            } else {
                com.android.server.adb.AdbService.this.mConnectionPort.set(-1);
                try {
                    android.provider.Settings.Global.putInt(com.android.server.adb.AdbService.this.mContentResolver, "adb_wifi_enabled", 0);
                } catch (java.lang.SecurityException e) {
                    android.util.Slog.d(com.android.server.adb.AdbService.TAG, "ADB_ENABLED is restricted.");
                }
            }
            com.android.server.adb.AdbService.this.broadcastPortInfo(com.android.server.adb.AdbService.this.mConnectionPort.get());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void broadcastPortInfo(int port) {
        int i;
        android.content.Intent intent = new android.content.Intent("com.android.server.adb.WIRELESS_DEBUG_STATUS");
        if (port >= 0) {
            i = 4;
        } else {
            i = 5;
        }
        intent.putExtra("status", i);
        intent.putExtra("adb_port", port);
        com.android.server.adb.AdbDebuggingManager.sendBroadcastWithDebugPermission(this.mContext, intent, android.os.UserHandle.ALL);
        android.util.Slog.i(TAG, "sent port broadcast port=" + port);
    }

    private void startAdbd() {
        android.os.SystemProperties.set(CTL_START, ADBD);
    }

    private void stopAdbd() {
        if (!this.mIsAdbUsbEnabled && !this.mIsAdbWifiEnabled) {
            android.os.SystemProperties.set(CTL_STOP, ADBD);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAdbdEnabledForTransport(boolean enable, byte transportType) {
        if (transportType == 0) {
            this.mIsAdbUsbEnabled = enable;
        } else if (transportType == 1) {
            this.mIsAdbWifiEnabled = enable;
        }
        if (enable) {
            startAdbd();
        } else {
            stopAdbd();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAdbEnabled(final boolean enable, final byte transportType) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "setAdbEnabled(" + enable + "), mIsAdbUsbEnabled=" + this.mIsAdbUsbEnabled + ", mIsAdbWifiEnabled=" + this.mIsAdbWifiEnabled + ", transportType=" + ((int) transportType));
        }
        if (transportType == 0 && enable != this.mIsAdbUsbEnabled) {
            this.mIsAdbUsbEnabled = enable;
        } else if (transportType == 1 && enable != this.mIsAdbWifiEnabled) {
            this.mIsAdbWifiEnabled = enable;
            if (this.mIsAdbWifiEnabled) {
                if (!((java.lang.Boolean) android.sysprop.AdbProperties.secure().orElse(false)).booleanValue() && this.mDebuggingManager == null) {
                    android.os.SystemProperties.set(WIFI_PERSISTENT_CONFIG_PROPERTY, "1");
                    this.mConnectionPortPoller = new com.android.server.adb.AdbDebuggingManager.AdbConnectionPortPoller(this.mPortListener);
                    this.mConnectionPortPoller.start();
                }
            } else {
                android.os.SystemProperties.set(WIFI_PERSISTENT_CONFIG_PROPERTY, "0");
                if (this.mConnectionPortPoller != null) {
                    this.mConnectionPortPoller.cancelAndWait();
                    this.mConnectionPortPoller = null;
                }
            }
        } else {
            return;
        }
        if (enable) {
            startAdbd();
        } else {
            stopAdbd();
        }
        for (android.debug.IAdbTransport transport : this.mTransports.values()) {
            try {
                transport.onAdbEnabled(enable, transportType);
            } catch (android.os.RemoteException e) {
                android.util.Slog.w(TAG, "Unable to send onAdbEnabled to transport " + transport.toString());
            }
        }
        if (this.mDebuggingManager != null) {
            this.mDebuggingManager.setAdbEnabled(enable, transportType);
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Broadcasting enable = " + enable + ", type = " + ((int) transportType));
        }
        this.mCallbacks.broadcast(new java.util.function.Consumer() { // from class: com.android.server.adb.AdbService$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.adb.AdbService.lambda$setAdbEnabled$0(enable, transportType, (android.debug.IAdbCallback) obj);
            }
        });
    }

    static /* synthetic */ void lambda$setAdbEnabled$0(boolean enable, byte transportType, android.debug.IAdbCallback callback) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Sending enable = " + enable + ", type = " + ((int) transportType) + " to " + callback);
        }
        try {
            callback.onDebuggingChanged(enable, transportType);
        } catch (android.os.RemoteException ex) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Unable to send onDebuggingChanged:", ex);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public int handleShellCommand(android.os.ParcelFileDescriptor in, android.os.ParcelFileDescriptor out, android.os.ParcelFileDescriptor err, java.lang.String[] args) {
        return new com.android.server.adb.AdbShellCommand(this).exec(this, in.getFileDescriptor(), out.getFileDescriptor(), err.getFileDescriptor(), args);
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        com.android.internal.util.dump.DualDumpOutputStream dump;
        if (com.android.internal.util.DumpUtils.checkDumpPermission(this.mContext, TAG, writer)) {
            com.android.internal.util.IndentingPrintWriter pw = new com.android.internal.util.IndentingPrintWriter(writer, "  ");
            long ident = android.os.Binder.clearCallingIdentity();
            try {
                android.util.ArraySet<java.lang.String> argsSet = new android.util.ArraySet<>();
                java.util.Collections.addAll(argsSet, args);
                boolean dumpAsProto = false;
                if (argsSet.contains("--proto")) {
                    dumpAsProto = true;
                }
                if (argsSet.size() == 0 || argsSet.contains("-a") || dumpAsProto) {
                    if (dumpAsProto) {
                        dump = new com.android.internal.util.dump.DualDumpOutputStream(new android.util.proto.ProtoOutputStream(fd));
                    } else {
                        pw.println("ADB MANAGER STATE (dumpsys adb):");
                        dump = new com.android.internal.util.dump.DualDumpOutputStream(new com.android.internal.util.IndentingPrintWriter(pw, "  "));
                    }
                    if (this.mDebuggingManager != null) {
                        this.mDebuggingManager.dump(dump, "debugging_manager", 1146756268033L);
                    }
                    dump.flush();
                } else {
                    pw.println("Dump current ADB state");
                    pw.println("  No commands available");
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(ident);
            }
        }
    }
}
