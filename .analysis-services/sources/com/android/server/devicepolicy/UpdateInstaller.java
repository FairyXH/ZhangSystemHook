package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
abstract class UpdateInstaller {
    static final java.lang.String TAG = "UpdateInstaller";
    private android.app.admin.StartInstallingUpdateCallback mCallback;
    private com.android.server.devicepolicy.DevicePolicyConstants mConstants;
    protected android.content.Context mContext;
    protected java.io.File mCopiedUpdateFile;
    private com.android.server.devicepolicy.DevicePolicyManagerService.Injector mInjector;
    private android.os.ParcelFileDescriptor mUpdateFileDescriptor;

    public abstract void installUpdateInThread();

    protected UpdateInstaller(android.content.Context context, android.os.ParcelFileDescriptor updateFileDescriptor, android.app.admin.StartInstallingUpdateCallback callback, com.android.server.devicepolicy.DevicePolicyManagerService.Injector injector, com.android.server.devicepolicy.DevicePolicyConstants constants) {
        this.mContext = context;
        this.mCallback = callback;
        this.mUpdateFileDescriptor = updateFileDescriptor;
        this.mInjector = injector;
        this.mConstants = constants;
    }

    public void startInstallUpdate() {
        this.mCopiedUpdateFile = null;
        if (!isBatteryLevelSufficient()) {
            notifyCallbackOnError(5, "The battery level must be above " + this.mConstants.BATTERY_THRESHOLD_NOT_CHARGING + " while not charging or above " + this.mConstants.BATTERY_THRESHOLD_CHARGING + " while charging");
            return;
        }
        java.lang.Thread thread = new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.devicepolicy.UpdateInstaller$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$startInstallUpdate$0();
            }
        });
        thread.setPriority(10);
        thread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$startInstallUpdate$0() {
        this.mCopiedUpdateFile = copyUpdateFileToDataOtaPackageDir();
        if (this.mCopiedUpdateFile == null) {
            notifyCallbackOnError(1, "Error while copying file.");
        } else {
            installUpdateInThread();
        }
    }

    private boolean isBatteryLevelSufficient() {
        android.content.Intent batteryStatus = this.mContext.registerReceiver(null, new android.content.IntentFilter("android.intent.action.BATTERY_CHANGED"));
        float batteryPercentage = calculateBatteryPercentage(batteryStatus);
        boolean isBatteryPluggedIn = batteryStatus.getIntExtra("plugged", -1) > 0;
        return isBatteryPluggedIn ? batteryPercentage >= ((float) this.mConstants.BATTERY_THRESHOLD_CHARGING) : batteryPercentage >= ((float) this.mConstants.BATTERY_THRESHOLD_NOT_CHARGING);
    }

    private float calculateBatteryPercentage(android.content.Intent batteryStatus) {
        int level = batteryStatus.getIntExtra("level", -1);
        int scale = batteryStatus.getIntExtra("scale", -1);
        return (level * 100) / scale;
    }

    private java.io.File copyUpdateFileToDataOtaPackageDir() {
        try {
            java.io.File destination = createNewFileWithPermissions();
            copyToFile(destination);
            return destination;
        } catch (java.io.IOException e) {
            android.util.Log.w(TAG, "Failed to copy update file to OTA directory", e);
            notifyCallbackOnError(1, android.util.Log.getStackTraceString(e));
            return null;
        }
    }

    private java.io.File createNewFileWithPermissions() throws java.io.IOException {
        java.io.File destination = java.io.File.createTempFile("update", ".zip", new java.io.File(android.os.Environment.getDataDirectory() + "/ota_package"));
        android.os.FileUtils.setPermissions(destination, 484, -1, -1);
        return destination;
    }

    private void copyToFile(java.io.File destination) throws java.io.IOException {
        java.io.OutputStream out = new java.io.FileOutputStream(destination);
        try {
            java.io.InputStream in = new android.os.ParcelFileDescriptor.AutoCloseInputStream(this.mUpdateFileDescriptor);
            try {
                android.os.FileUtils.copy(in, out);
                in.close();
                out.close();
            } finally {
            }
        } catch (java.lang.Throwable th) {
            try {
                out.close();
            } catch (java.lang.Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    void cleanupUpdateFile() {
        if (this.mCopiedUpdateFile != null && this.mCopiedUpdateFile.exists()) {
            this.mCopiedUpdateFile.delete();
        }
    }

    protected void notifyCallbackOnError(int errorCode, java.lang.String errorMessage) {
        cleanupUpdateFile();
        android.app.admin.DevicePolicyEventLogger.createEvent(74).setInt(errorCode).write();
        try {
            this.mCallback.onStartInstallingUpdateError(errorCode, errorMessage);
        } catch (android.os.RemoteException e) {
            android.util.Log.d(TAG, "Error while calling callback", e);
        }
    }

    protected void notifyCallbackOnSuccess() {
        cleanupUpdateFile();
        this.mInjector.powerManagerReboot("deviceowner");
    }
}
