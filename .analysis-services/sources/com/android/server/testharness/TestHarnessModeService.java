package com.android.server.testharness;

/* JADX INFO: loaded from: classes3.dex */
public class TestHarnessModeService extends com.android.server.SystemService {
    private static final java.lang.String TAG = com.android.server.testharness.TestHarnessModeService.class.getSimpleName();
    public static final java.lang.String TEST_HARNESS_MODE_PROPERTY = "persist.sys.test_harness";
    private boolean mEnableKeepMemtagMode;
    private com.android.server.pdb.PersistentDataBlockManagerInternal mPersistentDataBlockManagerInternal;
    private final android.os.IBinder mService;

    public TestHarnessModeService(android.content.Context context) {
        super(context);
        this.mEnableKeepMemtagMode = false;
        this.mService = new android.os.Binder() { // from class: com.android.server.testharness.TestHarnessModeService.1
            public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
                new com.android.server.testharness.TestHarnessModeService.TestHarnessModeShellCommand().exec(this, in, out, err, args, callback, resultReceiver);
            }
        };
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("testharness", this.mService);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        switch (phase) {
            case 500:
                setUpTestHarnessMode();
                break;
            case 1000:
                completeTestHarnessModeSetup();
                showNotificationIfEnabled();
                break;
        }
        super.onBootPhase(phase);
    }

    private void setUpTestHarnessMode() {
        android.util.Slog.d(TAG, "Setting up test harness mode");
        byte[] testHarnessModeData = getTestHarnessModeData();
        if (testHarnessModeData == null) {
            return;
        }
        setDeviceProvisioned();
        disableLockScreen();
        android.os.SystemProperties.set(TEST_HARNESS_MODE_PROPERTY, "1");
    }

    private void disableLockScreen() {
        int mainUserId = getMainUserId();
        com.android.internal.widget.LockPatternUtils utils = new com.android.internal.widget.LockPatternUtils(getContext());
        utils.setLockScreenDisabled(true, mainUserId);
    }

    private void completeTestHarnessModeSetup() {
        android.util.Slog.d(TAG, "Completing Test Harness Mode setup.");
        byte[] testHarnessModeData = getTestHarnessModeData();
        if (testHarnessModeData == null) {
            return;
        }
        try {
            try {
                setUpAdbFiles(com.android.server.testharness.TestHarnessModeService.PersistentData.fromBytes(testHarnessModeData));
                configureSettings();
                configureUser();
            } catch (com.android.server.testharness.TestHarnessModeService.SetUpTestHarnessModeException e) {
                android.util.Slog.e(TAG, "Failed to set up Test Harness Mode. Bad data.", e);
            }
        } finally {
            getPersistentDataBlock().clearTestHarnessModeData();
        }
    }

    private byte[] getTestHarnessModeData() {
        com.android.server.pdb.PersistentDataBlockManagerInternal blockManager = getPersistentDataBlock();
        if (blockManager == null) {
            android.util.Slog.e(TAG, "Failed to start Test Harness Mode; no implementation of PersistentDataBlockManagerInternal was bound!");
            return null;
        }
        byte[] testHarnessModeData = blockManager.getTestHarnessModeData();
        if (testHarnessModeData == null || testHarnessModeData.length == 0) {
            return null;
        }
        return testHarnessModeData;
    }

    private void configureSettings() {
        android.content.ContentResolver cr = getContext().getContentResolver();
        if (android.provider.Settings.Global.getInt(cr, "adb_enabled", 0) == 1) {
            android.os.SystemProperties.set("ctl.restart", "adbd");
            android.util.Slog.d(TAG, "Restarted adbd");
        }
        android.provider.Settings.Global.putLong(cr, "adb_allowed_connection_time", 0L);
        android.provider.Settings.Global.putInt(cr, "development_settings_enabled", 1);
        android.provider.Settings.Global.putInt(cr, "verifier_verify_adb_installs", 0);
        android.provider.Settings.Global.putInt(cr, "stay_on_while_plugged_in", 15);
        android.provider.Settings.Global.putInt(cr, "ota_disable_automatic_update", 1);
    }

    private void setUpAdbFiles(com.android.server.testharness.TestHarnessModeService.PersistentData persistentData) {
        android.debug.AdbManagerInternal adbManager = (android.debug.AdbManagerInternal) com.android.server.LocalServices.getService(android.debug.AdbManagerInternal.class);
        if (adbManager.getAdbKeysFile() != null) {
            writeBytesToFile(persistentData.mAdbKeys, adbManager.getAdbKeysFile().toPath());
        }
        if (adbManager.getAdbTempKeysFile() != null) {
            writeBytesToFile(persistentData.mAdbTempKeys, adbManager.getAdbTempKeysFile().toPath());
        }
        adbManager.notifyKeyFilesUpdated();
    }

    private void configureUser() {
        int mainUserId = getMainUserId();
        android.content.ContentResolver.setMasterSyncAutomaticallyAsUser(false, mainUserId);
        android.location.LocationManager locationManager = (android.location.LocationManager) getContext().getSystemService(android.location.LocationManager.class);
        locationManager.setLocationEnabledForUser(true, android.os.UserHandle.of(mainUserId));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getMainUserId() {
        com.android.server.pm.UserManagerInternal umi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        int mainUserId = umi.getMainUserId();
        if (mainUserId >= 0) {
            return mainUserId;
        }
        android.util.Slog.w(TAG, "No MainUser exists; using user 0 instead");
        return 0;
    }

    private void writeBytesToFile(byte[] keys, java.nio.file.Path adbKeys) {
        try {
            java.io.OutputStream fileOutputStream = java.nio.file.Files.newOutputStream(adbKeys, new java.nio.file.OpenOption[0]);
            fileOutputStream.write(keys);
            fileOutputStream.close();
            java.util.Set<java.nio.file.attribute.PosixFilePermission> permissions = java.nio.file.Files.getPosixFilePermissions(adbKeys, new java.nio.file.LinkOption[0]);
            permissions.add(java.nio.file.attribute.PosixFilePermission.GROUP_READ);
            java.nio.file.Files.setPosixFilePermissions(adbKeys, permissions);
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, "Failed to set up adb keys", e);
        }
    }

    private void setDeviceProvisioned() {
        android.content.ContentResolver cr = getContext().getContentResolver();
        android.provider.Settings.Global.putInt(cr, "device_provisioned", 1);
        android.provider.Settings.Secure.putIntForUser(cr, "user_setup_complete", 1, -2);
    }

    private void showNotificationIfEnabled() {
        if (!android.os.SystemProperties.getBoolean(TEST_HARNESS_MODE_PROPERTY, false)) {
            return;
        }
        java.lang.String title = getContext().getString(android.R.string.status_bar_cdma_eri);
        java.lang.String message = getContext().getString(android.R.string.status_bar_cast);
        android.app.Notification notification = new android.app.Notification.Builder(getContext(), com.android.internal.notification.SystemNotificationChannels.DEVELOPER).setSmallIcon(android.R.drawable.seekbar_thumb_pressed_to_unpressed_animation).setWhen(0L).setOngoing(true).setTicker(title).setDefaults(0).setColor(getContext().getColor(android.R.color.system_notification_accent_color)).setContentTitle(title).setContentText(message).setVisibility(1).build();
        android.app.NotificationManager notificationManager = (android.app.NotificationManager) getContext().getSystemService(android.app.NotificationManager.class);
        notificationManager.notifyAsUser(null, 54, notification, android.os.UserHandle.ALL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.server.pdb.PersistentDataBlockManagerInternal getPersistentDataBlock() {
        if (this.mPersistentDataBlockManagerInternal == null) {
            android.util.Slog.d(TAG, "Getting PersistentDataBlockManagerInternal from LocalServices");
            this.mPersistentDataBlockManagerInternal = (com.android.server.pdb.PersistentDataBlockManagerInternal) com.android.server.LocalServices.getService(com.android.server.pdb.PersistentDataBlockManagerInternal.class);
        }
        return this.mPersistentDataBlockManagerInternal;
    }

    private class TestHarnessModeShellCommand extends android.os.ShellCommand {
        private TestHarnessModeShellCommand() {
        }

        public int onCommand(java.lang.String cmd) {
            byte b;
            byte b2;
            if (cmd == null) {
                return handleDefaultCommands(cmd);
            }
            switch (cmd.hashCode()) {
                case -1298848381:
                    b = !cmd.equals(com.android.server.bluetooth.IOplusBluetoothManagerServiceExt.FLAG_ENABLE) ? (byte) -1 : (byte) 0;
                    break;
                case 1097519758:
                    b = !cmd.equals("restore") ? (byte) -1 : (byte) 1;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                case 1:
                    break;
                default:
                    return handleDefaultCommands(cmd);
            }
            while (true) {
                java.lang.String opt = getNextOption();
                if (opt != null) {
                    switch (opt.hashCode()) {
                        case -510577843:
                            if (opt.equals("--keep-memtag")) {
                                b2 = 0;
                                break;
                            }
                        default:
                            b2 = -1;
                            break;
                    }
                    switch (b2) {
                        case 0:
                            com.android.server.testharness.TestHarnessModeService.this.mEnableKeepMemtagMode = true;
                            break;
                        default:
                            getErrPrintWriter().println("Invalid option: " + opt);
                            return 1;
                    }
                } else {
                    checkPermissions();
                    long originalId = android.os.Binder.clearCallingIdentity();
                    try {
                        if (isDeviceSecure()) {
                            getErrPrintWriter().println("Test Harness Mode cannot be enabled if there is a lock screen");
                            android.os.Binder.restoreCallingIdentity(originalId);
                            return 2;
                        }
                        return handleEnable();
                    } finally {
                        android.os.Binder.restoreCallingIdentity(originalId);
                    }
                }
            }
        }

        private void checkPermissions() {
            com.android.server.testharness.TestHarnessModeService.this.getContext().enforceCallingPermission("android.permission.ENABLE_TEST_HARNESS_MODE", "You must hold android.permission.ENABLE_TEST_HARNESS_MODE to enable Test Harness Mode");
        }

        private boolean isDeviceSecure() {
            android.app.KeyguardManager keyguardManager = (android.app.KeyguardManager) com.android.server.testharness.TestHarnessModeService.this.getContext().getSystemService(android.app.KeyguardManager.class);
            return keyguardManager.isDeviceSecure(com.android.server.testharness.TestHarnessModeService.this.getMainUserId());
        }

        private int handleEnable() {
            android.debug.AdbManagerInternal adbManager = (android.debug.AdbManagerInternal) com.android.server.LocalServices.getService(android.debug.AdbManagerInternal.class);
            java.io.File adbKeys = adbManager.getAdbKeysFile();
            java.io.File adbTempKeys = adbManager.getAdbTempKeysFile();
            try {
                byte[] adbKeysBytes = getBytesFromFile(adbKeys);
                byte[] adbTempKeysBytes = getBytesFromFile(adbTempKeys);
                com.android.server.testharness.TestHarnessModeService.PersistentData persistentData = new com.android.server.testharness.TestHarnessModeService.PersistentData(adbKeysBytes, adbTempKeysBytes);
                com.android.server.pdb.PersistentDataBlockManagerInternal blockManager = com.android.server.testharness.TestHarnessModeService.this.getPersistentDataBlock();
                if (blockManager == null) {
                    android.util.Slog.e("ShellCommand", "Failed to enable Test Harness Mode. No implementation of PersistentDataBlockManagerInternal was bound.");
                    getErrPrintWriter().println("Failed to enable Test Harness Mode");
                    return 1;
                }
                blockManager.setTestHarnessModeData(persistentData.toBytes());
                android.content.Intent i = new android.content.Intent("android.intent.action.FACTORY_RESET");
                i.setPackage(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
                i.addFlags(268435456);
                i.putExtra("android.intent.extra.REASON", "ShellCommand");
                i.putExtra("android.intent.extra.WIPE_EXTERNAL_STORAGE", true);
                i.putExtra("keep_memtag_mode", com.android.server.testharness.TestHarnessModeService.this.mEnableKeepMemtagMode);
                com.android.server.testharness.TestHarnessModeService.this.getContext().sendBroadcastAsUser(i, android.os.UserHandle.SYSTEM);
                return 0;
            } catch (java.io.IOException e) {
                android.util.Slog.e("ShellCommand", "Failed to store ADB keys.", e);
                getErrPrintWriter().println("Failed to enable Test Harness Mode");
                return 1;
            }
        }

        private byte[] getBytesFromFile(java.io.File file) throws java.io.IOException {
            if (file == null || !file.exists()) {
                return new byte[0];
            }
            java.nio.file.Path path = file.toPath();
            java.io.InputStream inputStream = java.nio.file.Files.newInputStream(path, new java.nio.file.OpenOption[0]);
            try {
                int size = (int) java.nio.file.Files.size(path);
                byte[] bytes = new byte[size];
                int numBytes = inputStream.read(bytes);
                if (numBytes != size) {
                    throw new java.io.IOException("Failed to read the whole file");
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                return bytes;
            } catch (java.lang.Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            pw.println("About:");
            pw.println("  Test Harness Mode is a mode that the device can be placed in to prepare");
            pw.println("  the device for running UI tests. The device is placed into this mode by");
            pw.println("  first wiping all data from the device, preserving ADB keys.");
            pw.println();
            pw.println("  By default, the following settings are configured:");
            pw.println("    * Package Verifier is disabled");
            pw.println("    * Stay Awake While Charging is enabled");
            pw.println("    * OTA Updates are disabled");
            pw.println("    * Auto-Sync for accounts is disabled");
            pw.println();
            pw.println("  Other apps may configure themselves differently in Test Harness Mode by");
            pw.println("  checking ActivityManager.isRunningInUserTestHarness()");
            pw.println();
            pw.println("Test Harness Mode commands:");
            pw.println("  help");
            pw.println("    Print this help text.");
            pw.println();
            pw.println("  enable|restore");
            pw.println("    Erase all data from this device and enable Test Harness Mode,");
            pw.println("    preserving the stored ADB keys currently on the device and toggling");
            pw.println("    settings in a way that are conducive to Instrumentation testing.");
        }
    }

    public static class PersistentData {
        static final byte VERSION_1 = 1;
        static final byte VERSION_2 = 2;
        final byte[] mAdbKeys;
        final byte[] mAdbTempKeys;
        final int mVersion;

        PersistentData(byte[] adbKeys, byte[] adbTempKeys) {
            this(2, adbKeys, adbTempKeys);
        }

        PersistentData(int version, byte[] adbKeys, byte[] adbTempKeys) {
            this.mVersion = version;
            this.mAdbKeys = adbKeys;
            this.mAdbTempKeys = adbTempKeys;
        }

        static com.android.server.testharness.TestHarnessModeService.PersistentData fromBytes(byte[] bytes) throws com.android.server.testharness.TestHarnessModeService.SetUpTestHarnessModeException {
            try {
                java.io.DataInputStream is = new java.io.DataInputStream(new java.io.ByteArrayInputStream(bytes));
                int version = is.readInt();
                if (version == 1) {
                    is.readBoolean();
                }
                int adbKeysLength = is.readInt();
                byte[] adbKeys = new byte[adbKeysLength];
                is.readFully(adbKeys);
                int adbTempKeysLength = is.readInt();
                byte[] adbTempKeys = new byte[adbTempKeysLength];
                is.readFully(adbTempKeys);
                return new com.android.server.testharness.TestHarnessModeService.PersistentData(version, adbKeys, adbTempKeys);
            } catch (java.io.IOException e) {
                throw new com.android.server.testharness.TestHarnessModeService.SetUpTestHarnessModeException(e);
            }
        }

        byte[] toBytes() {
            try {
                java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
                java.io.DataOutputStream dos = new java.io.DataOutputStream(os);
                dos.writeInt(2);
                dos.writeInt(this.mAdbKeys.length);
                dos.write(this.mAdbKeys);
                dos.writeInt(this.mAdbTempKeys.length);
                dos.write(this.mAdbTempKeys);
                dos.close();
                return os.toByteArray();
            } catch (java.io.IOException e) {
                throw new java.lang.RuntimeException(e);
            }
        }
    }

    private static class SetUpTestHarnessModeException extends java.lang.Exception {
        SetUpTestHarnessModeException(java.lang.Exception e) {
            super(e);
        }
    }
}
