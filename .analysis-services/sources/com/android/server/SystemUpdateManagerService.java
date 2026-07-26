package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class SystemUpdateManagerService extends android.os.ISystemUpdateManager.Stub {
    private static final java.lang.String INFO_FILE = "system-update-info.xml";
    private static final int INFO_FILE_VERSION = 0;
    private static final java.lang.String KEY_BOOT_COUNT = "boot-count";
    private static final java.lang.String KEY_INFO_BUNDLE = "info-bundle";
    private static final java.lang.String KEY_UID = "uid";
    private static final java.lang.String KEY_VERSION = "version";
    private static final java.lang.String TAG = "SystemUpdateManagerService";
    private static final java.lang.String TAG_INFO = "info";
    private static final int UID_UNKNOWN = -1;
    private final android.content.Context mContext;
    private final java.lang.Object mLock = new java.lang.Object();
    private int mLastUid = -1;
    private int mLastStatus = 0;
    private final android.util.AtomicFile mFile = new android.util.AtomicFile(new java.io.File(android.os.Environment.getDataSystemDirectory(), INFO_FILE));

    public SystemUpdateManagerService(android.content.Context context) {
        this.mContext = context;
        synchronized (this.mLock) {
            loadSystemUpdateInfoLocked();
        }
    }

    public void updateSystemUpdateInfo(android.os.PersistableBundle infoBundle) {
        updateSystemUpdateInfo_enforcePermission();
        int status = infoBundle.getInt("status", 0);
        if (status == 0) {
            android.util.Slog.w(TAG, "Invalid status info. Ignored");
            return;
        }
        int uid = android.os.Binder.getCallingUid();
        if (this.mLastUid == -1 || this.mLastUid == uid || status != 1) {
            synchronized (this.mLock) {
                saveSystemUpdateInfoLocked(infoBundle, uid);
            }
            return;
        }
        android.util.Slog.i(TAG, "Inactive updater reporting IDLE status. Ignored");
    }

    public android.os.Bundle retrieveSystemUpdateInfo() {
        android.os.Bundle bundleLoadSystemUpdateInfoLocked;
        if (this.mContext.checkCallingOrSelfPermission("android.permission.READ_SYSTEM_UPDATE_INFO") == -1 && this.mContext.checkCallingOrSelfPermission("android.permission.RECOVERY") == -1) {
            throw new java.lang.SecurityException("Can't read system update info. Requiring READ_SYSTEM_UPDATE_INFO or RECOVERY permission.");
        }
        synchronized (this.mLock) {
            bundleLoadSystemUpdateInfoLocked = loadSystemUpdateInfoLocked();
        }
        return bundleLoadSystemUpdateInfoLocked;
    }

    private android.os.Bundle loadSystemUpdateInfoLocked() throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        android.os.PersistableBundle loadedBundle = null;
        try {
            java.io.FileInputStream fis = this.mFile.openRead();
            try {
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(fis);
                loadedBundle = readInfoFileLocked(parser);
                if (fis != null) {
                    fis.close();
                }
            } finally {
            }
        } catch (java.io.FileNotFoundException e) {
            android.util.Slog.i(TAG, "No existing info file " + this.mFile.getBaseFile());
        } catch (java.io.IOException e2) {
            android.util.Slog.e(TAG, "Failed to read the info file:", e2);
        } catch (org.xmlpull.v1.XmlPullParserException e3) {
            android.util.Slog.e(TAG, "Failed to parse the info file:", e3);
        }
        if (loadedBundle == null) {
            return removeInfoFileAndGetDefaultInfoBundleLocked();
        }
        int version = loadedBundle.getInt(KEY_VERSION, -1);
        if (version == -1) {
            android.util.Slog.w(TAG, "Invalid info file (invalid version). Ignored");
            return removeInfoFileAndGetDefaultInfoBundleLocked();
        }
        int lastUid = loadedBundle.getInt("uid", -1);
        if (lastUid == -1) {
            android.util.Slog.w(TAG, "Invalid info file (invalid UID). Ignored");
            return removeInfoFileAndGetDefaultInfoBundleLocked();
        }
        int lastBootCount = loadedBundle.getInt(KEY_BOOT_COUNT, -1);
        if (lastBootCount == -1 || lastBootCount != getBootCount()) {
            android.util.Slog.w(TAG, "Outdated info file. Ignored");
            return removeInfoFileAndGetDefaultInfoBundleLocked();
        }
        android.os.PersistableBundle infoBundle = loadedBundle.getPersistableBundle(KEY_INFO_BUNDLE);
        if (infoBundle == null) {
            android.util.Slog.w(TAG, "Invalid info file (missing info). Ignored");
            return removeInfoFileAndGetDefaultInfoBundleLocked();
        }
        int lastStatus = infoBundle.getInt("status", 0);
        if (lastStatus == 0) {
            android.util.Slog.w(TAG, "Invalid info file (invalid status). Ignored");
            return removeInfoFileAndGetDefaultInfoBundleLocked();
        }
        this.mLastStatus = lastStatus;
        this.mLastUid = lastUid;
        return new android.os.Bundle(infoBundle);
    }

    private void saveSystemUpdateInfoLocked(android.os.PersistableBundle infoBundle, int uid) {
        android.os.PersistableBundle outBundle = new android.os.PersistableBundle();
        outBundle.putPersistableBundle(KEY_INFO_BUNDLE, infoBundle);
        outBundle.putInt(KEY_VERSION, 0);
        outBundle.putInt("uid", uid);
        outBundle.putInt(KEY_BOOT_COUNT, getBootCount());
        if (writeInfoFileLocked(outBundle)) {
            this.mLastUid = uid;
            this.mLastStatus = infoBundle.getInt("status");
        }
    }

    private android.os.PersistableBundle readInfoFileLocked(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type == 2 && TAG_INFO.equals(parser.getName())) {
                    return android.os.PersistableBundle.restoreFromXml(parser);
                }
            } else {
                return null;
            }
        }
    }

    private boolean writeInfoFileLocked(android.os.PersistableBundle outBundle) {
        java.io.FileOutputStream fos = null;
        try {
            fos = this.mFile.startWrite();
            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(fos);
            out.startDocument((java.lang.String) null, true);
            out.startTag((java.lang.String) null, TAG_INFO);
            outBundle.saveToXml(out);
            out.endTag((java.lang.String) null, TAG_INFO);
            out.endDocument();
            this.mFile.finishWrite(fos);
            return true;
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Slog.e(TAG, "Failed to save the info file:", e);
            if (fos != null) {
                this.mFile.failWrite(fos);
                return false;
            }
            return false;
        }
    }

    private android.os.Bundle removeInfoFileAndGetDefaultInfoBundleLocked() {
        if (this.mFile.exists()) {
            android.util.Slog.i(TAG, "Removing info file");
            this.mFile.delete();
        }
        this.mLastStatus = 0;
        this.mLastUid = -1;
        android.os.Bundle infoBundle = new android.os.Bundle();
        infoBundle.putInt("status", 0);
        return infoBundle;
    }

    private int getBootCount() {
        return android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "boot_count", 0);
    }
}
