package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public class ShutdownThread extends java.lang.Thread {
    private static final int ACTION_DONE_POLL_WAIT_MS = 500;
    private static final int ACTIVITY_MANAGER_STOP_PERCENT = 4;
    private static final int BROADCAST_STOP_PERCENT = 2;
    private static final java.lang.String CHECK_POINTS_FILE_BASENAME = "/data/system/shutdown-checkpoints/checkpoints";
    private static final boolean DEBUG = false;
    static final int DEFAULT_SHUTDOWN_VIBRATE_MS = 500;
    private static final int MAX_BROADCAST_TIME = 5000;
    private static final int MAX_CHECK_POINTS_DUMP_WAIT_TIME = 10000;
    private static final int MAX_RADIO_WAIT_TIME = 3000;
    private static final int MAX_UNCRYPT_WAIT_TIME = 900000;
    private static final java.lang.String METRICS_FILE_BASENAME = "/data/system/shutdown-metrics";
    private static final int MOUNT_SERVICE_STOP_PERCENT = 20;
    private static final int PACKAGE_MANAGER_STOP_PERCENT = 6;
    private static final int RADIOS_STATE_POLL_SLEEP_MS = 100;
    private static final int RADIO_STOP_PERCENT = 18;
    public static final java.lang.String REBOOT_SAFEMODE_PROPERTY = "persist.sys.safemode";
    public static final java.lang.String RO_SAFEMODE_PROPERTY = "ro.sys.safemode";
    public static final java.lang.String SHUTDOWN_ACTION_PROPERTY = "sys.shutdown.requested";
    private static final java.lang.String TAG = "ShutdownThread";
    protected static java.lang.String mReason;
    protected static boolean mReboot;
    protected static boolean mRebootHasProgressBar;
    protected static boolean mRebootSafeMode;
    private static android.app.AlertDialog sConfirmDialog;
    private boolean mActionDone;
    private final java.lang.Object mActionDoneSync;
    protected android.content.Context mContext;
    private android.os.PowerManager.WakeLock mCpuWakeLock;
    protected android.os.Handler mHandler;
    private final com.android.server.power.ShutdownThread.Injector mInjector;
    protected android.os.PowerManager mPowerManager;
    private android.app.ProgressDialog mProgressDialog;
    protected android.os.PowerManager.WakeLock mScreenWakeLock;
    private com.android.server.power.IShutdownThreadExt mShutdownThreadExt;
    private static final java.lang.Object sIsStartedGuard = new java.lang.Object();
    private static boolean sIsStarted = false;
    protected static final com.android.server.power.ShutdownThread sInstance = new com.android.server.power.ShutdownThread();
    private static final android.util.ArrayMap<java.lang.String, java.lang.Long> TRON_METRICS = new android.util.ArrayMap<>();
    private static java.lang.String METRIC_SYSTEM_SERVER = "shutdown_system_server";
    private static java.lang.String METRIC_SEND_BROADCAST = "shutdown_send_shutdown_broadcast";
    private static java.lang.String METRIC_AM = "shutdown_activity_manager";
    private static java.lang.String METRIC_PM = "shutdown_package_manager";
    private static java.lang.String METRIC_RADIOS = "shutdown_radios";
    private static java.lang.String METRIC_RADIO = "shutdown_radio";
    private static java.lang.String METRIC_SHUTDOWN_TIME_START = "begin_shutdown";
    private static com.android.server.power.IShutdownThreadExt.IStaticExt mShutdownThreadStaticExt = (com.android.server.power.IShutdownThreadExt.IStaticExt) system.ext.loader.core.ExtLoader.type(com.android.server.power.IShutdownThreadExt.IStaticExt.class).create();

    public ShutdownThread() {
        this(new com.android.server.power.ShutdownThread.Injector());
    }

    ShutdownThread(com.android.server.power.ShutdownThread.Injector injector) {
        this.mActionDoneSync = new java.lang.Object();
        this.mShutdownThreadExt = (com.android.server.power.IShutdownThreadExt) system.ext.loader.core.ExtLoader.type(com.android.server.power.IShutdownThreadExt.class).base(this).create();
        this.mInjector = injector;
    }

    public static void shutdown(android.content.Context context, java.lang.String reason, boolean confirm) {
        if (mShutdownThreadStaticExt.interceptShutdown(context, reason)) {
            return;
        }
        mReboot = false;
        mRebootSafeMode = false;
        mReason = reason;
        shutdownInner(context, confirm);
    }

    private static void shutdownInner(final android.content.Context context, boolean confirm) {
        int resourceId;
        int i;
        context.assertRuntimeOverlayThemable();
        synchronized (sIsStartedGuard) {
            if (sIsStarted) {
                return;
            }
            com.android.server.power.ShutdownCheckPoints.recordCheckPoint(null);
            int longPressBehavior = context.getResources().getInteger(android.R.integer.config_keyChordPowerVolumeUp);
            if (mRebootSafeMode) {
                resourceId = android.R.string.power_off;
            } else if (longPressBehavior == 2) {
                resourceId = android.R.string.screenshot_edit;
            } else {
                resourceId = android.R.string.screen_not_shared_sensitive_content;
            }
            if (confirm) {
                com.android.server.power.ShutdownThread.CloseDialogReceiver closer = new com.android.server.power.ShutdownThread.CloseDialogReceiver(context);
                if (sConfirmDialog != null) {
                    sConfirmDialog.dismiss();
                }
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                if (mRebootSafeMode) {
                    i = android.R.string.prefs_bugreport;
                } else {
                    i = android.R.string.pin_specific_target;
                }
                sConfirmDialog = builder.setTitle(i).setMessage(resourceId).setPositiveButton(android.R.string.yes, new android.content.DialogInterface.OnClickListener() { // from class: com.android.server.power.ShutdownThread.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(android.content.DialogInterface dialog, int which) {
                        com.android.server.power.ShutdownThread.beginShutdownSequence(context);
                    }
                }).setNegativeButton(android.R.string.no, (android.content.DialogInterface.OnClickListener) null).create();
                closer.dialog = sConfirmDialog;
                sConfirmDialog.setOnDismissListener(closer);
                sConfirmDialog.getWindow().setType(2009);
                sConfirmDialog.show();
                return;
            }
            beginShutdownSequence(context);
        }
    }

    private static class CloseDialogReceiver extends android.content.BroadcastReceiver implements android.content.DialogInterface.OnDismissListener {
        public android.app.Dialog dialog;
        private android.content.Context mContext;

        CloseDialogReceiver(android.content.Context context) {
            this.mContext = context;
            android.content.IntentFilter filter = new android.content.IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS");
            context.registerReceiver(this, filter, 2);
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            this.dialog.cancel();
        }

        @Override // android.content.DialogInterface.OnDismissListener
        public void onDismiss(android.content.DialogInterface unused) {
            this.mContext.unregisterReceiver(this);
        }
    }

    public static void reboot(android.content.Context context, java.lang.String reason, boolean confirm) {
        if (mShutdownThreadStaticExt.interceptReboot(context, reason)) {
            return;
        }
        mReboot = true;
        mRebootSafeMode = false;
        mRebootHasProgressBar = false;
        mReason = reason;
        shutdownInner(context, confirm);
    }

    public static void rebootSafeMode(android.content.Context context, boolean confirm) {
        if (mShutdownThreadStaticExt.interceptReboot(context, "")) {
            return;
        }
        android.os.UserManager um = (android.os.UserManager) context.getSystemService("user");
        if (um.hasUserRestriction("no_safe_boot")) {
            return;
        }
        mReboot = true;
        mRebootSafeMode = true;
        mRebootHasProgressBar = false;
        mReason = null;
        shutdownInner(context, confirm);
    }

    private static android.app.ProgressDialog showShutdownDialog(android.content.Context context) {
        android.app.ProgressDialog pd = new android.app.ProgressDialog(context);
        if (mReason != null && mReason.startsWith("recovery-update")) {
            mRebootHasProgressBar = android.os.RecoverySystem.UNCRYPT_PACKAGE_FILE.exists() && !android.os.RecoverySystem.BLOCK_MAP_FILE.exists();
            pd.setTitle(context.getText(android.R.string.print_service_installed_title));
            if (mRebootHasProgressBar) {
                pd.setMax(100);
                pd.setProgress(0);
                pd.setIndeterminate(false);
                boolean showPercent = context.getResources().getBoolean(android.R.bool.config_restart_radio_on_pdp_fail_regular_deactivation);
                if (!showPercent) {
                    pd.setProgressPercentFormat(null);
                }
                pd.setProgressNumberFormat(null);
                pd.setProgressStyle(1);
                pd.setMessage(context.getText(android.R.string.preposition_for_year));
            } else {
                if (showSysuiReboot()) {
                    return null;
                }
                pd.setIndeterminate(true);
                pd.setMessage(context.getText(android.R.string.print_service_installed_message));
            }
        } else if (mReason != null && mReason.equals("recovery")) {
            if (com.android.server.RescueParty.isRecoveryTriggeredReboot()) {
                pd.setTitle(context.getText(android.R.string.pin_specific_target));
                pd.setMessage(context.getText(android.R.string.search_hint));
                pd.setIndeterminate(true);
            } else {
                if (showSysuiReboot()) {
                    return null;
                }
                pd.setTitle(context.getText(android.R.string.preposition_for_date));
                pd.setMessage(context.getText(android.R.string.prepend_shortcut_label));
                pd.setIndeterminate(true);
            }
        } else {
            if (showSysuiReboot()) {
                return null;
            }
            pd.setTitle(context.getText(android.R.string.pin_specific_target));
            pd.setMessage(context.getText(android.R.string.search_hint));
            pd.setIndeterminate(true);
        }
        pd.setCancelable(false);
        pd.getWindow().setType(2009);
        return pd;
    }

    private static boolean showSysuiReboot() {
        if (mShutdownThreadStaticExt.hasFeatureOriginalShutdownAnimation()) {
            if (mReason != null && mReason.equals("silence")) {
                android.util.Log.d(TAG, "silence reboot case,SysUI is unavailable");
                return false;
            }
            try {
                com.android.server.statusbar.StatusBarManagerInternal service = (com.android.server.statusbar.StatusBarManagerInternal) com.android.server.LocalServices.getService(com.android.server.statusbar.StatusBarManagerInternal.class);
                if (service.showShutdownUi(mReboot, mReason)) {
                    return true;
                }
            } catch (java.lang.Exception e) {
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void beginShutdownSequence(android.content.Context context) {
        synchronized (sIsStartedGuard) {
            if (sIsStarted) {
                return;
            }
            sIsStarted = true;
            sInstance.mProgressDialog = showShutdownDialog(context);
            sInstance.mContext = context;
            sInstance.mPowerManager = (android.os.PowerManager) context.getSystemService("power");
            sInstance.mCpuWakeLock = null;
            try {
                sInstance.mCpuWakeLock = sInstance.mPowerManager.newWakeLock(1, "ShutdownThread-cpu");
                sInstance.mCpuWakeLock.setReferenceCounted(false);
                sInstance.mCpuWakeLock.acquire();
            } catch (java.lang.SecurityException e) {
                android.util.Log.w(TAG, "No permission to acquire wake lock", e);
                sInstance.mCpuWakeLock = null;
            }
            sInstance.mScreenWakeLock = null;
            if (sInstance.mPowerManager.isScreenOn()) {
                try {
                    sInstance.mScreenWakeLock = sInstance.mPowerManager.newWakeLock(26, "ShutdownThread-screen");
                    sInstance.mScreenWakeLock.setReferenceCounted(false);
                    sInstance.mScreenWakeLock.acquire();
                } catch (java.lang.SecurityException e2) {
                    android.util.Log.w(TAG, "No permission to acquire wake lock", e2);
                    sInstance.mScreenWakeLock = null;
                }
            }
            mShutdownThreadStaticExt.beginShutdownSequence(context);
            if (android.app.admin.SecurityLog.isLoggingEnabled()) {
                android.app.admin.SecurityLog.writeEvent(210010, new java.lang.Object[0]);
            }
            sInstance.mHandler = new android.os.Handler() { // from class: com.android.server.power.ShutdownThread.2
            };
            sInstance.start();
        }
    }

    void actionDone() {
        synchronized (this.mActionDoneSync) {
            this.mActionDone = true;
            this.mActionDoneSync.notifyAll();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x00eb, code lost:
    
        android.util.Log.w(com.android.server.power.ShutdownThread.TAG, "Shutdown broadcast timed out");
        com.android.server.power.ShutdownThread.mShutdownThreadStaticExt.doShutdownDetect("47");
     */
    @Override // java.lang.Thread, java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void run() throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 594
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.power.ShutdownThread.run():void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static android.util.TimingsTraceLog newTimingsLog() {
        return new android.util.TimingsTraceLog("ShutdownTiming", 524288L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void metricStarted(java.lang.String metricKey) {
        synchronized (TRON_METRICS) {
            TRON_METRICS.put(metricKey, java.lang.Long.valueOf(android.os.SystemClock.elapsedRealtime() * (-1)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void metricEnded(java.lang.String metricKey) {
        synchronized (TRON_METRICS) {
            TRON_METRICS.put(metricKey, java.lang.Long.valueOf(android.os.SystemClock.elapsedRealtime() + TRON_METRICS.get(metricKey).longValue()));
        }
    }

    private static void metricShutdownStart() {
        synchronized (TRON_METRICS) {
            TRON_METRICS.put(METRIC_SHUTDOWN_TIME_START, java.lang.Long.valueOf(java.lang.System.currentTimeMillis()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setRebootProgress(final int progress, final java.lang.CharSequence message) {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.power.ShutdownThread.4
            @Override // java.lang.Runnable
            public void run() {
                if (com.android.server.power.ShutdownThread.this.mProgressDialog != null) {
                    com.android.server.power.ShutdownThread.this.mProgressDialog.setProgress(progress);
                    if (message != null) {
                        com.android.server.power.ShutdownThread.this.mProgressDialog.setMessage(message);
                    }
                }
            }
        });
    }

    private void shutdownRadios(final int timeout) {
        final long endTime = android.os.SystemClock.elapsedRealtime() + ((long) timeout);
        final boolean[] done = new boolean[1];
        java.lang.Thread t = new java.lang.Thread() { // from class: com.android.server.power.ShutdownThread.5
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                android.util.TimingsTraceLog shutdownTimingsTraceLog = com.android.server.power.ShutdownThread.newTimingsLog();
                android.telephony.TelephonyManager telephonyManager = (android.telephony.TelephonyManager) com.android.server.power.ShutdownThread.this.mContext.getSystemService(android.telephony.TelephonyManager.class);
                boolean radioOff = telephonyManager == null || !telephonyManager.isAnyRadioPoweredOn();
                if (!radioOff) {
                    android.util.Log.w(com.android.server.power.ShutdownThread.TAG, "Turning off cellular radios...");
                    com.android.server.power.ShutdownThread.metricStarted(com.android.server.power.ShutdownThread.METRIC_RADIO);
                    telephonyManager.shutdownAllRadios();
                }
                android.util.Log.i(com.android.server.power.ShutdownThread.TAG, "Waiting for Radio...");
                long delay = endTime - android.os.SystemClock.elapsedRealtime();
                while (delay > 0) {
                    if (com.android.server.power.ShutdownThread.mRebootHasProgressBar) {
                        int status = (int) ((((((long) timeout) - delay) * 1.0d) * 12.0d) / ((double) timeout));
                        com.android.server.power.ShutdownThread.sInstance.setRebootProgress(status + 6, null);
                    }
                    if (!radioOff) {
                        try {
                            radioOff = !telephonyManager.isAnyRadioPoweredOn();
                        } catch (java.lang.Exception e) {
                            android.util.Log.i(com.android.server.power.ShutdownThread.TAG, "phone is dead.......");
                        }
                        if (radioOff) {
                            android.util.Log.i(com.android.server.power.ShutdownThread.TAG, "Radio turned off.");
                            com.android.server.power.ShutdownThread.metricEnded(com.android.server.power.ShutdownThread.METRIC_RADIO);
                            shutdownTimingsTraceLog.logDuration("ShutdownRadio", ((java.lang.Long) com.android.server.power.ShutdownThread.TRON_METRICS.get(com.android.server.power.ShutdownThread.METRIC_RADIO)).longValue());
                        }
                    }
                    if (radioOff) {
                        android.util.Log.i(com.android.server.power.ShutdownThread.TAG, "Radio shutdown complete.");
                        done[0] = true;
                        return;
                    } else {
                        android.os.SystemClock.sleep(100L);
                        delay = endTime - android.os.SystemClock.elapsedRealtime();
                    }
                }
            }
        };
        t.start();
        try {
            t.join(timeout);
        } catch (java.lang.InterruptedException e) {
        }
        if (!done[0]) {
            android.util.Log.w(TAG, "Timed out waiting for Radio shutdown.");
        }
    }

    public static void rebootOrShutdown(android.content.Context context, boolean reboot, java.lang.String reason) {
        com.android.server.display.util.OplusDisplayPanelFeatureHelper.setDisplayPanelFeatureValue(210, 1);
        if (!mShutdownThreadStaticExt.rebootOrShutdownSubsystem()) {
            mShutdownThreadStaticExt.doShutdownDetect("43");
        }
        if (reboot) {
            android.util.Log.i(TAG, "Rebooting, reason: " + reason);
            if (mShutdownThreadStaticExt.shouldDoLowLevelShutdown(context)) {
                com.android.server.power.PowerManagerService.lowLevelReboot(reason);
            }
            android.util.Log.e(TAG, "Reboot failed, will attempt shutdown instead");
            return;
        }
        if (context == null || !mShutdownThreadStaticExt.shouldDoLowLevelShutdown(context)) {
            android.util.Log.i(TAG, "Shutdown process timeout noneed do lowLevelShutdown and vibrate");
            return;
        }
        try {
            sInstance.playShutdownVibration(context);
        } catch (java.lang.Exception e) {
            android.util.Log.w(TAG, "Failed to vibrate during shutdown.", e);
        }
        android.util.Log.i(TAG, "Performing low-level shutdown...");
        com.android.server.power.PowerManagerService.lowLevelShutdown(reason);
    }

    void playShutdownVibration(android.content.Context context) {
        android.os.Vibrator vibrator = this.mInjector.getVibrator(context);
        if (!vibrator.hasVibrator()) {
            return;
        }
        android.os.VibrationEffect vibrationEffect = getValidShutdownVibration(context, vibrator);
        vibrator.vibrate(vibrationEffect, android.os.VibrationAttributes.createForUsage(18));
        long vibrationDuration = vibrationEffect.getDuration();
        this.mInjector.sleep(vibrationDuration < 0 ? 500L : vibrationDuration);
    }

    private static void saveMetrics(boolean reboot, java.lang.String reason) {
        java.lang.StringBuilder metricValue = new java.lang.StringBuilder();
        metricValue.append("reboot:");
        metricValue.append(reboot ? "y" : "n");
        metricValue.append(",").append("reason:").append(reason);
        int metricsSize = TRON_METRICS.size();
        for (int i = 0; i < metricsSize; i++) {
            java.lang.String name = TRON_METRICS.keyAt(i);
            long value = TRON_METRICS.valueAt(i).longValue();
            if (value < 0) {
                android.util.Log.e(TAG, "metricEnded wasn't called for " + name);
            } else {
                metricValue.append(',').append(name).append(':').append(value);
            }
        }
        java.io.File tmp = new java.io.File("/data/system/shutdown-metrics.tmp");
        boolean saved = false;
        try {
            java.io.FileOutputStream fos = new java.io.FileOutputStream(tmp);
            try {
                fos.write(metricValue.toString().getBytes(java.nio.charset.StandardCharsets.UTF_8));
                saved = true;
                fos.close();
            } finally {
            }
        } catch (java.io.IOException e) {
            android.util.Log.e(TAG, "Cannot save shutdown metrics", e);
        }
        if (saved) {
            tmp.renameTo(new java.io.File("/data/system/shutdown-metrics.txt"));
        }
    }

    private void uncrypt() {
        android.util.Log.i(TAG, "Calling uncrypt and monitoring the progress...");
        final android.os.RecoverySystem.ProgressListener progressListener = new android.os.RecoverySystem.ProgressListener() { // from class: com.android.server.power.ShutdownThread.6
            @Override // android.os.RecoverySystem.ProgressListener
            public void onProgress(int status) {
                if (status < 0 || status >= 100) {
                    if (status == 100) {
                        java.lang.CharSequence msg = com.android.server.power.ShutdownThread.this.mContext.getText(android.R.string.print_service_installed_message);
                        com.android.server.power.ShutdownThread.sInstance.setRebootProgress(status, msg);
                        return;
                    }
                    return;
                }
                java.lang.CharSequence msg2 = com.android.server.power.ShutdownThread.this.mContext.getText(android.R.string.preposition_for_time);
                com.android.server.power.ShutdownThread.sInstance.setRebootProgress(((int) ((((double) status) * 80.0d) / 100.0d)) + 20, msg2);
            }
        };
        final boolean[] done = {false};
        java.lang.Thread t = new java.lang.Thread() { // from class: com.android.server.power.ShutdownThread.7
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    java.lang.String filename = android.os.FileUtils.readTextFile(android.os.RecoverySystem.UNCRYPT_PACKAGE_FILE, 0, null);
                    android.os.RecoverySystem.processPackage(com.android.server.power.ShutdownThread.this.mContext, new java.io.File(filename), progressListener);
                } catch (java.io.IOException e) {
                    android.util.Log.e(com.android.server.power.ShutdownThread.TAG, "Error uncrypting file", e);
                }
                done[0] = true;
            }
        };
        t.start();
        try {
            t.join(900000L);
        } catch (java.lang.InterruptedException e) {
        }
        if (!done[0]) {
            android.util.Log.w(TAG, "Timed out waiting for uncrypt.");
            java.lang.String timeoutMessage = java.lang.String.format("uncrypt_time: %d\nuncrypt_error: %d\n", 900, 100);
            try {
                android.os.FileUtils.stringToFile(android.os.RecoverySystem.UNCRYPT_STATUS_FILE, timeoutMessage);
            } catch (java.io.IOException e2) {
                android.util.Log.e(TAG, "Failed to write timeout message to uncrypt status", e2);
            }
        }
    }

    private android.os.VibrationEffect getValidShutdownVibration(android.content.Context context, android.os.Vibrator vibrator) {
        android.os.VibrationEffect parsedEffect = parseVibrationEffectFromFile(this.mInjector.getDefaultShutdownVibrationEffectFilePath(context), vibrator);
        if (parsedEffect == null) {
            return createDefaultVibrationEffect();
        }
        long parsedEffectDuration = parsedEffect.getDuration();
        if (parsedEffectDuration == Long.MAX_VALUE) {
            android.util.Log.w(TAG, "The parsed shutdown vibration is indefinite.");
            return createDefaultVibrationEffect();
        }
        return parsedEffect;
    }

    private static android.os.VibrationEffect parseVibrationEffectFromFile(java.lang.String filePath, android.os.Vibrator vibrator) {
        if (!android.text.TextUtils.isEmpty(filePath)) {
            try {
                return android.os.vibrator.persistence.VibrationXmlParser.parseDocument(new java.io.FileReader(filePath)).resolve(vibrator);
            } catch (java.lang.Exception e) {
                android.util.Log.e(TAG, "Error parsing default shutdown vibration effect.", e);
                return null;
            }
        }
        return null;
    }

    private static android.os.VibrationEffect createDefaultVibrationEffect() {
        return android.os.VibrationEffect.createOneShot(500L, -1);
    }

    static class Injector {
        Injector() {
        }

        public android.os.Vibrator getVibrator(android.content.Context context) {
            return new android.os.SystemVibrator(context);
        }

        public void sleep(long durationMs) {
            try {
                java.lang.Thread.sleep(durationMs);
            } catch (java.lang.InterruptedException e) {
            }
        }

        public java.lang.String getDefaultShutdownVibrationEffectFilePath(android.content.Context context) {
            return context.getResources().getString(android.R.string.config_deviceConfiguratorPackageName);
        }
    }

    protected boolean mStartShutdownSeq(android.content.Context c, boolean IsReboot) {
        return true;
    }

    protected void mShutdownSeqFinish(android.content.Context c) {
    }

    protected void mLowLevelShutdownSeq(android.content.Context c) {
    }
}
