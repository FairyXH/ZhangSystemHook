package com.android.server.storage;

/* JADX INFO: loaded from: classes3.dex */
public class DeviceStorageMonitorService extends com.android.server.SystemService {
    private static final long DEFAULT_CHECK_INTERVAL = 30000;
    public static final java.lang.String EXTRA_SEQUENCE = "seq";
    private static final long HIGH_CHECK_INTERVAL = 36000000;
    private static final long LOW_CHECK_INTERVAL = 60000;
    private static final int MSG_CHECK_HIGH = 2;
    private static final int MSG_CHECK_LOW = 1;
    static final int OPTION_FORCE_UPDATE = 1;
    static final java.lang.String SERVICE = "devicestoragemonitor";
    private static final java.lang.String TAG = "DeviceStorageMonitorService";
    private static final java.lang.String TV_NOTIFICATION_CHANNEL_ID = "devicestoragemonitor.tv";
    private com.android.server.storage.DeviceStorageMonitorService.DeviceStorageMonitorServiceWrapper dsmsWrapper;
    private com.android.server.storage.DeviceStorageMonitorService.CacheFileDeletedObserver mCacheFileDeletedObserver;
    public com.android.server.storage.IDeviceStorageMonitorServiceExt mDSSext;
    private volatile int mForceLevel;
    private final android.os.Handler mHandler;
    private final android.os.HandlerThread mHandlerThread;
    private final com.android.server.storage.DeviceStorageMonitorInternal mLocalService;
    private android.app.NotificationManager mNotifManager;
    private final android.os.Binder mRemoteService;
    private final java.util.concurrent.atomic.AtomicInteger mSeq;
    private final android.util.ArrayMap<java.util.UUID, com.android.server.storage.DeviceStorageMonitorService.State> mStates;
    private static final long DEFAULT_LOG_DELTA_BYTES = android.util.DataUnit.MEBIBYTES.toBytes(64);
    private static final long BOOT_IMAGE_STORAGE_REQUIREMENT = android.util.DataUnit.MEBIBYTES.toBytes(250);

    private static class State {
        private static final int LEVEL_FULL = 2;
        private static final int LEVEL_LOW = 1;
        private static final int LEVEL_NORMAL = 0;
        private static final int LEVEL_UNKNOWN = -1;
        public long lastUsableBytes;
        public int level;

        private State() {
            this.level = 0;
            this.lastUsableBytes = Long.MAX_VALUE;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isEntering(int level, int oldLevel, int newLevel) {
            return newLevel >= level && (oldLevel < level || oldLevel == -1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean isLeaving(int level, int oldLevel, int newLevel) {
            return newLevel < level && (oldLevel >= level || oldLevel == -1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static java.lang.String levelToString(int level) {
            switch (level) {
                case -1:
                    return "UNKNOWN";
                case 0:
                    return com.android.server.utils.PriorityDump.PRIORITY_ARG_NORMAL;
                case 1:
                    return "LOW";
                case 2:
                    return "FULL";
                default:
                    return java.lang.Integer.toString(level);
            }
        }
    }

    private com.android.server.storage.DeviceStorageMonitorService.State findOrCreateState(java.util.UUID uuid) {
        com.android.server.storage.DeviceStorageMonitorService.State state = this.mStates.get(uuid);
        if (state == null) {
            com.android.server.storage.DeviceStorageMonitorService.State state2 = new com.android.server.storage.DeviceStorageMonitorService.State();
            this.mStates.put(uuid, state2);
            return state2;
        }
        return state;
    }

    private void checkLow() {
        int newLevel;
        int oldLevel;
        int oldLevel2;
        android.os.storage.StorageManager storage = (android.os.storage.StorageManager) getContext().getSystemService(android.os.storage.StorageManager.class);
        int seq = this.mSeq.get();
        for (android.os.storage.VolumeInfo vol : storage.getWritablePrivateVolumes()) {
            java.io.File file = vol.getPath();
            long fullBytes = storage.getStorageFullBytes(file);
            long lowBytes = storage.getStorageLowBytes(file);
            if (file.getUsableSpace() < (3 * lowBytes) / 2) {
                android.content.pm.PackageManagerInternal pm = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
                try {
                    pm.freeStorage(vol.getFsUuid(), lowBytes * 2, 0);
                } catch (java.io.IOException e) {
                    android.util.Slog.w(TAG, e);
                }
            }
            java.util.UUID uuid = android.os.storage.StorageManager.convert(vol.getFsUuid());
            com.android.server.storage.DeviceStorageMonitorService.State state = findOrCreateState(uuid);
            long totalBytes = file.getTotalSpace();
            long usableBytes = file.getUsableSpace();
            int oldLevel3 = state.level;
            android.os.storage.StorageManager storage2 = storage;
            if (this.mForceLevel != -1) {
                newLevel = this.mForceLevel;
                oldLevel = -1;
            } else if (usableBytes <= fullBytes) {
                newLevel = 2;
                oldLevel = oldLevel3;
            } else if (usableBytes <= lowBytes) {
                newLevel = 1;
                oldLevel = oldLevel3;
            } else if (android.os.storage.StorageManager.UUID_DEFAULT.equals(uuid) && usableBytes < BOOT_IMAGE_STORAGE_REQUIREMENT) {
                newLevel = 1;
                oldLevel = oldLevel3;
            } else {
                newLevel = 0;
                oldLevel = oldLevel3;
            }
            if (java.lang.Math.abs(state.lastUsableBytes - usableBytes) > DEFAULT_LOG_DELTA_BYTES || oldLevel != newLevel) {
                oldLevel2 = oldLevel;
                com.android.server.EventLogTags.writeStorageState(uuid.toString(), oldLevel, newLevel, usableBytes, totalBytes);
                state.lastUsableBytes = usableBytes;
            } else {
                oldLevel2 = oldLevel;
            }
            updateNotifications(vol, oldLevel2, newLevel);
            updateBroadcasts(vol, oldLevel2, newLevel, seq);
            state.level = newLevel;
            storage = storage2;
        }
        if (!this.mHandler.hasMessages(1)) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1), 60000L);
        }
        if (!this.mHandler.hasMessages(2)) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(2), HIGH_CHECK_INTERVAL);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void checkHigh() {
        android.os.storage.StorageManager storage = (android.os.storage.StorageManager) getContext().getSystemService(android.os.storage.StorageManager.class);
        int storageThresholdPercentHigh = android.provider.DeviceConfig.getInt("storage_native_boot", "storage_threshold_percent_high", 20);
        for (android.os.storage.VolumeInfo vol : storage.getWritablePrivateVolumes()) {
            java.io.File file = vol.getPath();
            if (file.getUsableSpace() < (file.getTotalSpace() * ((long) storageThresholdPercentHigh)) / 100) {
                android.content.pm.PackageManagerInternal pm = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
                try {
                    pm.freeAllAppCacheAboveQuota(vol.getFsUuid());
                } catch (java.io.IOException e) {
                    android.util.Slog.w(TAG, e);
                }
            }
        }
        if (!this.mHandler.hasMessages(2)) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(2), HIGH_CHECK_INTERVAL);
        }
    }

    public DeviceStorageMonitorService(android.content.Context context) {
        super(context);
        this.mSeq = new java.util.concurrent.atomic.AtomicInteger(1);
        this.mForceLevel = -1;
        this.mStates = new android.util.ArrayMap<>();
        this.mLocalService = new com.android.server.storage.DeviceStorageMonitorInternal() { // from class: com.android.server.storage.DeviceStorageMonitorService.2
            @Override // com.android.server.storage.DeviceStorageMonitorInternal
            public void checkMemory() {
                com.android.server.storage.DeviceStorageMonitorService.this.mHandler.removeMessages(1);
                com.android.server.storage.DeviceStorageMonitorService.this.mHandler.obtainMessage(1).sendToTarget();
            }

            @Override // com.android.server.storage.DeviceStorageMonitorInternal
            public boolean isMemoryLow() {
                return android.os.Environment.getDataDirectory().getUsableSpace() < getMemoryLowThreshold();
            }

            @Override // com.android.server.storage.DeviceStorageMonitorInternal
            public long getMemoryLowThreshold() {
                return com.android.server.storage.DeviceStorageMonitorService.this.dsmsWrapper.getExtImpl().getMemoryLowThresholdInternal();
            }
        };
        this.mRemoteService = new android.os.Binder() { // from class: com.android.server.storage.DeviceStorageMonitorService.3
            @Override // android.os.Binder
            protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
                if (com.android.internal.util.DumpUtils.checkDumpPermission(com.android.server.storage.DeviceStorageMonitorService.this.getContext(), com.android.server.storage.DeviceStorageMonitorService.TAG, pw)) {
                    com.android.server.storage.DeviceStorageMonitorService.this.dumpImpl(fd, pw, args);
                }
            }

            public void onShellCommand(java.io.FileDescriptor in, java.io.FileDescriptor out, java.io.FileDescriptor err, java.lang.String[] args, android.os.ShellCallback callback, android.os.ResultReceiver resultReceiver) {
                com.android.server.storage.DeviceStorageMonitorService.this.new Shell().exec(this, in, out, err, args, callback, resultReceiver);
            }
        };
        this.dsmsWrapper = new com.android.server.storage.DeviceStorageMonitorService.DeviceStorageMonitorServiceWrapper();
        this.mDSSext = (com.android.server.storage.IDeviceStorageMonitorServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.storage.IDeviceStorageMonitorServiceExt.class).base(this).create();
        this.mHandlerThread = new android.os.HandlerThread(TAG, 10);
        this.mHandlerThread.start();
        this.mHandler = new android.os.Handler(this.mHandlerThread.getLooper()) { // from class: com.android.server.storage.DeviceStorageMonitorService.1
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                        com.android.server.storage.DeviceStorageMonitorService.this.dsmsWrapper.getExtImpl().dataCheck(com.android.server.storage.DeviceStorageMonitorService.this.mHandler);
                        break;
                    case 2:
                        com.android.server.storage.DeviceStorageMonitorService.this.checkHigh();
                        break;
                }
            }
        };
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        android.content.Context context = getContext();
        this.mNotifManager = (android.app.NotificationManager) context.getSystemService(android.app.NotificationManager.class);
        this.mCacheFileDeletedObserver = new com.android.server.storage.DeviceStorageMonitorService.CacheFileDeletedObserver();
        this.mCacheFileDeletedObserver.startWatching();
        android.content.pm.PackageManager packageManager = context.getPackageManager();
        boolean isTv = packageManager.hasSystemFeature("android.software.leanback");
        if (isTv) {
            this.mNotifManager.createNotificationChannel(new android.app.NotificationChannel(TV_NOTIFICATION_CHANNEL_ID, context.getString(android.R.string.display_rotation_camera_compat_toast_after_rotation), 4));
        }
        publishBinderService(SERVICE, this.mRemoteService);
        publishLocalService(com.android.server.storage.DeviceStorageMonitorInternal.class, this.mLocalService);
        this.dsmsWrapper.getExtImpl().onStart(this.mHandler, getContext(), this);
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        super.onBootPhase(phase);
        if (phase == 1000) {
            this.dsmsWrapper.getExtImpl().onBootPhase();
        }
    }

    class Shell extends android.os.ShellCommand {
        Shell() {
        }

        public int onCommand(java.lang.String cmd) {
            return com.android.server.storage.DeviceStorageMonitorService.this.onShellCommand(this, cmd);
        }

        public void onHelp() {
            java.io.PrintWriter pw = getOutPrintWriter();
            com.android.server.storage.DeviceStorageMonitorService.dumpHelp(pw);
        }
    }

    int parseOptions(com.android.server.storage.DeviceStorageMonitorService.Shell shell) {
        int opts = 0;
        while (true) {
            java.lang.String opt = shell.getNextOption();
            if (opt != null) {
                if ("-f".equals(opt)) {
                    opts |= 1;
                }
            } else {
                return opts;
            }
        }
    }

    int onShellCommand(com.android.server.storage.DeviceStorageMonitorService.Shell shell, java.lang.String cmd) {
        byte b;
        if (cmd == null) {
            return shell.handleDefaultCommands(cmd);
        }
        java.io.PrintWriter pw = shell.getOutPrintWriter();
        switch (cmd.hashCode()) {
            case 88200241:
                b = !cmd.equals("force-full") ? (byte) -1 : (byte) 3;
                break;
            case 108404047:
                b = !cmd.equals("reset") ? (byte) -1 : (byte) 2;
                break;
            case 1526871410:
                b = !cmd.equals("force-low") ? (byte) -1 : (byte) 0;
                break;
            case 1692300408:
                b = !cmd.equals("force-not-low") ? (byte) -1 : (byte) 1;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                int opts = parseOptions(shell);
                getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                this.mForceLevel = 1;
                this.dsmsWrapper.getExtImpl().setCmdForceLevel(cmd);
                int seq = this.mSeq.incrementAndGet();
                if ((opts & 1) != 0) {
                    this.mHandler.removeMessages(1);
                    this.mHandler.obtainMessage(1).sendToTarget();
                    pw.println(seq);
                }
                return 0;
            case 1:
                int opts2 = parseOptions(shell);
                getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                this.mForceLevel = 0;
                this.dsmsWrapper.getExtImpl().setCmdForceLevel(cmd);
                int seq2 = this.mSeq.incrementAndGet();
                if ((opts2 & 1) != 0) {
                    this.mHandler.removeMessages(1);
                    this.mHandler.obtainMessage(1).sendToTarget();
                    pw.println(seq2);
                }
                return 0;
            case 2:
                int opts3 = parseOptions(shell);
                getContext().enforceCallingOrSelfPermission("android.permission.DEVICE_POWER", null);
                this.mForceLevel = -1;
                int seq3 = this.mSeq.incrementAndGet();
                if ((opts3 & 1) != 0) {
                    this.mHandler.removeMessages(1);
                    this.mHandler.obtainMessage(1).sendToTarget();
                    pw.println(seq3);
                }
                return 0;
            case 3:
                this.dsmsWrapper.getExtImpl().shellCmdForceFull(shell, getContext(), "android.permission.DEVICE_POWER", this.mHandler);
                return 0;
            default:
                return shell.handleDefaultCommands(cmd);
        }
    }

    static void dumpHelp(java.io.PrintWriter pw) {
        pw.println("Device storage monitor service (devicestoragemonitor) commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("  force-low [-f]");
        pw.println("    Force storage to be low, freezing storage state.");
        pw.println("    -f: force a storage change broadcast be sent, prints new sequence.");
        pw.println("  force-not-low [-f]");
        pw.println("    Force storage to not be low, freezing storage state.");
        pw.println("    -f: force a storage change broadcast be sent, prints new sequence.");
        pw.println("  reset [-f]");
        pw.println("    Unfreeze storage state, returning to current real values.");
        pw.println("    -f: force a storage change broadcast be sent, prints new sequence.");
    }

    void dumpImpl(java.io.FileDescriptor fd, java.io.PrintWriter _pw, java.lang.String[] args) {
        java.io.PrintWriter indentingPrintWriter = new com.android.internal.util.IndentingPrintWriter(_pw, "  ");
        if (this.dsmsWrapper.getExtImpl().simulationTest(args, indentingPrintWriter)) {
            return;
        }
        if (args == null || args.length == 0 || "-a".equals(args[0])) {
            android.os.storage.StorageManager storage = (android.os.storage.StorageManager) getContext().getSystemService(android.os.storage.StorageManager.class);
            indentingPrintWriter.println("Known volumes:");
            indentingPrintWriter.increaseIndent();
            for (int i = 0; i < this.mStates.size(); i++) {
                java.util.UUID uuid = this.mStates.keyAt(i);
                com.android.server.storage.DeviceStorageMonitorService.State state = this.mStates.valueAt(i);
                if (android.os.storage.StorageManager.UUID_DEFAULT.equals(uuid)) {
                    indentingPrintWriter.println("Default:");
                } else {
                    indentingPrintWriter.println(uuid + ":");
                }
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.printPair("level", com.android.server.storage.DeviceStorageMonitorService.State.levelToString(state.level));
                indentingPrintWriter.printPair("lastUsableBytes", java.lang.Long.valueOf(state.lastUsableBytes));
                indentingPrintWriter.println();
                java.util.Iterator it = storage.getWritablePrivateVolumes().iterator();
                while (true) {
                    if (it.hasNext()) {
                        android.os.storage.VolumeInfo vol = (android.os.storage.VolumeInfo) it.next();
                        java.io.File file = vol.getPath();
                        java.util.UUID innerUuid = android.os.storage.StorageManager.convert(vol.getFsUuid());
                        if (java.util.Objects.equals(uuid, innerUuid)) {
                            indentingPrintWriter.print("lowBytes=");
                            indentingPrintWriter.print(storage.getStorageLowBytes(file));
                            indentingPrintWriter.print(" fullBytes=");
                            indentingPrintWriter.println(storage.getStorageFullBytes(file));
                            indentingPrintWriter.print("path=");
                            indentingPrintWriter.println(file);
                            break;
                        }
                    }
                }
                indentingPrintWriter.decreaseIndent();
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println();
            indentingPrintWriter.printPair("mSeq", java.lang.Integer.valueOf(this.mSeq.get()));
            indentingPrintWriter.printPair("mForceState", com.android.server.storage.DeviceStorageMonitorService.State.levelToString(this.mForceLevel));
            indentingPrintWriter.println();
            indentingPrintWriter.println();
            this.dsmsWrapper.getExtImpl().dumpImpl(indentingPrintWriter);
            return;
        }
        com.android.server.storage.DeviceStorageMonitorService.Shell shell = new com.android.server.storage.DeviceStorageMonitorService.Shell();
        shell.exec(this.mRemoteService, null, fd, null, args, null, new android.os.ResultReceiver(null));
    }

    private void updateNotifications(android.os.storage.VolumeInfo vol, int oldLevel, int newLevel) {
        android.content.Context context = getContext();
        java.util.UUID uuid = android.os.storage.StorageManager.convert(vol.getFsUuid());
        if (com.android.server.storage.DeviceStorageMonitorService.State.isEntering(1, oldLevel, newLevel)) {
            android.content.Intent lowMemIntent = new android.content.Intent("android.os.storage.action.MANAGE_STORAGE");
            lowMemIntent.putExtra("android.os.storage.extra.UUID", uuid);
            lowMemIntent.addFlags(268435456);
            java.lang.CharSequence title = context.getText(android.R.string.lockscreen_pattern_wrong);
            java.lang.CharSequence details = context.getText(android.R.string.lockscreen_pattern_correct);
            android.app.PendingIntent intent = android.app.PendingIntent.getActivityAsUser(context, 0, lowMemIntent, 67108864, null, android.os.UserHandle.CURRENT);
            android.app.Notification notification = new android.app.Notification.Builder(context, com.android.internal.notification.SystemNotificationChannels.ALERTS).setSmallIcon(android.R.drawable.search_plate).setTicker(title).setColor(context.getColor(android.R.color.system_notification_accent_color)).setContentTitle(title).setContentText(details).setContentIntent(intent).setStyle(new android.app.Notification.BigTextStyle().bigText(details)).setVisibility(1).setCategory("sys").extend(new android.app.Notification.TvExtender().setChannelId(TV_NOTIFICATION_CHANNEL_ID)).build();
            notification.flags |= 32;
            this.mNotifManager.notifyAsUser(uuid.toString(), 23, notification, android.os.UserHandle.ALL);
            com.android.internal.util.FrameworkStatsLog.write(130, java.util.Objects.toString(vol.getDescription()), 2);
            return;
        }
        if (com.android.server.storage.DeviceStorageMonitorService.State.isLeaving(1, oldLevel, newLevel)) {
            this.mNotifManager.cancelAsUser(uuid.toString(), 23, android.os.UserHandle.ALL);
            com.android.internal.util.FrameworkStatsLog.write(130, java.util.Objects.toString(vol.getDescription()), 1);
        }
    }

    private void updateBroadcasts(android.os.storage.VolumeInfo vol, int oldLevel, int newLevel, int seq) {
        if (!java.util.Objects.equals(android.os.storage.StorageManager.UUID_PRIVATE_INTERNAL, vol.getFsUuid())) {
            return;
        }
        android.content.Intent lowIntent = new android.content.Intent("android.intent.action.DEVICE_STORAGE_LOW").addFlags(85983232).putExtra(EXTRA_SEQUENCE, seq);
        android.content.Intent notLowIntent = new android.content.Intent("android.intent.action.DEVICE_STORAGE_OK").addFlags(85983232).putExtra(EXTRA_SEQUENCE, seq);
        if (com.android.server.storage.DeviceStorageMonitorService.State.isEntering(1, oldLevel, newLevel)) {
            getContext().sendStickyBroadcastAsUser(lowIntent, android.os.UserHandle.ALL);
        } else if (com.android.server.storage.DeviceStorageMonitorService.State.isLeaving(1, oldLevel, newLevel)) {
            getContext().removeStickyBroadcastAsUser(lowIntent, android.os.UserHandle.ALL);
            getContext().sendBroadcastAsUser(notLowIntent, android.os.UserHandle.ALL);
        }
        android.content.Intent fullIntent = new android.content.Intent("android.intent.action.DEVICE_STORAGE_FULL").addFlags(67108864).putExtra(EXTRA_SEQUENCE, seq);
        android.content.Intent notFullIntent = new android.content.Intent("android.intent.action.DEVICE_STORAGE_NOT_FULL").addFlags(67108864).putExtra(EXTRA_SEQUENCE, seq);
        if (com.android.server.storage.DeviceStorageMonitorService.State.isEntering(2, oldLevel, newLevel)) {
            getContext().sendStickyBroadcastAsUser(fullIntent, android.os.UserHandle.ALL);
        } else if (com.android.server.storage.DeviceStorageMonitorService.State.isLeaving(2, oldLevel, newLevel)) {
            getContext().removeStickyBroadcastAsUser(fullIntent, android.os.UserHandle.ALL);
            getContext().sendBroadcastAsUser(notFullIntent, android.os.UserHandle.ALL);
        }
    }

    private static class CacheFileDeletedObserver extends android.os.FileObserver {
        public CacheFileDeletedObserver() {
            super(android.os.Environment.getDownloadCacheDirectory().getAbsolutePath(), 512);
        }

        @Override // android.os.FileObserver
        public void onEvent(int event, java.lang.String path) {
            com.android.server.EventLogTags.writeCacheFileDeleted(path);
        }
    }

    public com.android.server.storage.IDeviceStorageMonitorServiceWrapper getWrapper() {
        return this.dsmsWrapper;
    }

    private class DeviceStorageMonitorServiceWrapper implements com.android.server.storage.IDeviceStorageMonitorServiceWrapper {
        private DeviceStorageMonitorServiceWrapper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.storage.IDeviceStorageMonitorServiceExt getExtImpl() {
            return com.android.server.storage.DeviceStorageMonitorService.this.mDSSext;
        }

        @Override // com.android.server.storage.IDeviceStorageMonitorServiceWrapper
        public int msgCheckLow() {
            return 1;
        }

        @Override // com.android.server.storage.IDeviceStorageMonitorServiceWrapper
        public java.util.concurrent.atomic.AtomicInteger mSeq() {
            return com.android.server.storage.DeviceStorageMonitorService.this.mSeq;
        }

        @Override // com.android.server.storage.IDeviceStorageMonitorServiceWrapper
        public int msgChecHigh() {
            return 2;
        }

        @Override // com.android.server.storage.IDeviceStorageMonitorServiceWrapper
        public long highCheckInterVal() {
            return com.android.server.storage.DeviceStorageMonitorService.HIGH_CHECK_INTERVAL;
        }
    }
}
