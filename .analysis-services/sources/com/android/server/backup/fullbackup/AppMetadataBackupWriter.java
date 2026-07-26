package com.android.server.backup.fullbackup;

/* JADX INFO: loaded from: classes.dex */
public class AppMetadataBackupWriter {
    private final android.app.backup.FullBackupDataOutput mOutput;
    private final android.content.pm.PackageManager mPackageManager;

    public AppMetadataBackupWriter(android.app.backup.FullBackupDataOutput output, android.content.pm.PackageManager packageManager) {
        this.mOutput = output;
        this.mPackageManager = packageManager;
    }

    public void backupManifest(android.content.pm.PackageInfo packageInfo, java.io.File manifestFile, java.io.File filesDir, boolean withApk) throws java.io.IOException {
        backupManifest(packageInfo, manifestFile, filesDir, null, null, withApk);
    }

    public void backupManifest(android.content.pm.PackageInfo packageInfo, java.io.File manifestFile, java.io.File filesDir, java.lang.String domain, java.lang.String linkDomain, boolean withApk) throws java.io.IOException {
        byte[] manifestBytes = getManifestBytes(packageInfo, withApk);
        java.io.FileOutputStream outputStream = new java.io.FileOutputStream(manifestFile);
        outputStream.write(manifestBytes);
        outputStream.close();
        manifestFile.setLastModified(0L);
        android.app.backup.FullBackup.backupToTar(packageInfo.packageName, domain, linkDomain, filesDir.getAbsolutePath(), manifestFile.getAbsolutePath(), this.mOutput);
    }

    private byte[] getManifestBytes(android.content.pm.PackageInfo packageInfo, boolean withApk) {
        java.lang.String packageName = packageInfo.packageName;
        java.lang.StringBuilder builder = new java.lang.StringBuilder(4096);
        android.util.StringBuilderPrinter printer = new android.util.StringBuilderPrinter(builder);
        printer.println(java.lang.Integer.toString(1));
        printer.println(packageName);
        printer.println(java.lang.Long.toString(packageInfo.getLongVersionCode()));
        printer.println(java.lang.Integer.toString(android.os.Build.VERSION.SDK_INT));
        java.lang.String installerName = this.mPackageManager.getInstallerPackageName(packageName);
        printer.println(installerName != null ? installerName : "");
        printer.println(withApk ? "1" : "0");
        android.content.pm.SigningInfo signingInfo = packageInfo.signingInfo;
        if (signingInfo == null) {
            printer.println("0");
        } else {
            android.content.pm.Signature[] signatures = signingInfo.getApkContentsSigners();
            printer.println(java.lang.Integer.toString(signatures.length));
            for (android.content.pm.Signature sig : signatures) {
                printer.println(sig.toCharsString());
            }
        }
        return builder.toString().getBytes();
    }

    public void backupWidget(android.content.pm.PackageInfo packageInfo, java.io.File metadataFile, java.io.File filesDir, byte[] widgetData) throws java.io.IOException {
        com.android.internal.util.Preconditions.checkArgument(widgetData.length > 0, "Can't backup widget with no data.");
        java.lang.String packageName = packageInfo.packageName;
        java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream(metadataFile);
        java.io.BufferedOutputStream bufferedOutputStream = new java.io.BufferedOutputStream(fileOutputStream);
        java.io.DataOutputStream dataOutputStream = new java.io.DataOutputStream(bufferedOutputStream);
        byte[] metadata = getMetadataBytes(packageName);
        bufferedOutputStream.write(metadata);
        writeWidgetData(dataOutputStream, widgetData);
        bufferedOutputStream.flush();
        dataOutputStream.close();
        metadataFile.setLastModified(0L);
        android.app.backup.FullBackup.backupToTar(packageName, (java.lang.String) null, (java.lang.String) null, filesDir.getAbsolutePath(), metadataFile.getAbsolutePath(), this.mOutput);
    }

    private byte[] getMetadataBytes(java.lang.String packageName) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder(512);
        android.util.StringBuilderPrinter printer = new android.util.StringBuilderPrinter(builder);
        printer.println(java.lang.Integer.toString(1));
        printer.println(packageName);
        return builder.toString().getBytes();
    }

    private void writeWidgetData(java.io.DataOutputStream out, byte[] widgetData) throws java.io.IOException {
        out.writeInt(com.android.server.backup.UserBackupManagerService.BACKUP_WIDGET_METADATA_TOKEN);
        out.writeInt(widgetData.length);
        out.write(widgetData);
    }

    public void backupApk(android.content.pm.PackageInfo packageInfo) {
        java.lang.String appSourceDir = packageInfo.applicationInfo.getBaseCodePath();
        java.lang.String apkDir = new java.io.File(appSourceDir).getParent();
        android.app.backup.FullBackup.backupToTar(packageInfo.packageName, com.android.server.wm.ActivityTaskManagerService.DUMP_ACTIVITIES_SHORT_CMD, (java.lang.String) null, apkDir, appSourceDir, this.mOutput);
    }

    public void backupObb(int userId, android.content.pm.PackageInfo packageInfo) {
        java.io.File[] obbFiles;
        android.os.Environment.UserEnvironment userEnv = new android.os.Environment.UserEnvironment(userId);
        java.io.File obbDir = userEnv.buildExternalStorageAppObbDirs(packageInfo.packageName)[0];
        if (obbDir != null && (obbFiles = obbDir.listFiles()) != null) {
            java.lang.String obbDirName = obbDir.getAbsolutePath();
            for (java.io.File obb : obbFiles) {
                android.app.backup.FullBackup.backupToTar(packageInfo.packageName, "obb", (java.lang.String) null, obbDirName, obb.getAbsolutePath(), this.mOutput);
            }
        }
    }
}
