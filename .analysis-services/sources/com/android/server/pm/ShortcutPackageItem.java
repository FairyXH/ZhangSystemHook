package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
abstract class ShortcutPackageItem {
    private static final java.lang.String KEY_NAME = "name";
    private static final java.lang.String TAG = "ShortcutService";
    private final com.android.server.pm.ShortcutPackageInfo mPackageInfo;
    private final java.lang.String mPackageName;
    private final int mPackageUserId;
    protected final com.android.server.pm.ShortcutBitmapSaver mShortcutBitmapSaver;
    protected com.android.server.pm.ShortcutUser mShortcutUser;
    protected final java.lang.Object mPackageItemLock = new java.lang.Object();
    private final java.lang.Runnable mSaveShortcutPackageRunner = new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutPackageItem$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            this.f$0.saveShortcutPackageItem();
        }
    };

    protected abstract boolean canRestoreAnyVersion();

    public abstract int getOwnerUserId();

    protected abstract java.io.File getShortcutPackageItemFile();

    protected abstract void onRestored(int i);

    public abstract void saveToXml(com.android.modules.utils.TypedXmlSerializer typedXmlSerializer, boolean z) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException;

    protected ShortcutPackageItem(com.android.server.pm.ShortcutUser shortcutUser, int packageUserId, java.lang.String packageName, com.android.server.pm.ShortcutPackageInfo packageInfo) {
        this.mShortcutUser = shortcutUser;
        this.mPackageUserId = packageUserId;
        this.mPackageName = (java.lang.String) com.android.internal.util.Preconditions.checkStringNotEmpty(packageName);
        this.mPackageInfo = (com.android.server.pm.ShortcutPackageInfo) java.util.Objects.requireNonNull(packageInfo);
        this.mShortcutBitmapSaver = new com.android.server.pm.ShortcutBitmapSaver(shortcutUser.mService);
    }

    public void replaceUser(com.android.server.pm.ShortcutUser user) {
        this.mShortcutUser = user;
    }

    public com.android.server.pm.ShortcutUser getUser() {
        return this.mShortcutUser;
    }

    public int getPackageUserId() {
        return this.mPackageUserId;
    }

    public java.lang.String getPackageName() {
        return this.mPackageName;
    }

    public com.android.server.pm.ShortcutPackageInfo getPackageInfo() {
        return this.mPackageInfo;
    }

    public void refreshPackageSignatureAndSave() {
        if (this.mPackageInfo.isShadow()) {
            return;
        }
        com.android.server.pm.ShortcutService s = this.mShortcutUser.mService;
        this.mPackageInfo.refreshSignature(s, this);
        scheduleSave();
    }

    public void attemptToRestoreIfNeededAndSave() {
        int restoreBlockReason;
        if (!this.mPackageInfo.isShadow()) {
            return;
        }
        com.android.server.pm.ShortcutService s = this.mShortcutUser.mService;
        if (!s.isPackageInstalled(this.mPackageName, this.mPackageUserId)) {
            if (com.android.server.pm.ShortcutService.DEBUG) {
                android.util.Slog.d(TAG, java.lang.String.format("Package still not installed: %s/u%d", this.mPackageName, java.lang.Integer.valueOf(this.mPackageUserId)));
                return;
            }
            return;
        }
        long currentVersionCode = -1;
        if (!this.mPackageInfo.hasSignatures()) {
            s.wtf("Attempted to restore package " + this.mPackageName + "/u" + this.mPackageUserId + " but signatures not found in the restore data.");
            restoreBlockReason = 102;
        } else {
            android.content.pm.PackageInfo pi = s.getPackageInfoWithSignatures(this.mPackageName, this.mPackageUserId);
            currentVersionCode = pi.getLongVersionCode();
            restoreBlockReason = this.mPackageInfo.canRestoreTo(s, pi, canRestoreAnyVersion());
        }
        if (com.android.server.pm.ShortcutService.DEBUG) {
            android.util.Slog.d(TAG, java.lang.String.format("Restoring package: %s/u%d (version=%d) %s for u%d", this.mPackageName, java.lang.Integer.valueOf(this.mPackageUserId), java.lang.Long.valueOf(currentVersionCode), android.content.pm.ShortcutInfo.getDisabledReasonDebugString(restoreBlockReason), java.lang.Integer.valueOf(getOwnerUserId())));
        }
        onRestored(restoreBlockReason);
        this.mPackageInfo.setShadow(false);
        scheduleSave();
    }

    public void saveToFileLocked(java.io.File path, boolean forBackup) {
        com.android.modules.utils.TypedXmlSerializer itemOut;
        com.android.server.pm.ResilientAtomicFile file = getResilientFile(path);
        java.io.FileOutputStream os = null;
        try {
            try {
                os = file.startWrite();
                if (forBackup) {
                    itemOut = android.util.Xml.newFastSerializer();
                    itemOut.setOutput(os, java.nio.charset.StandardCharsets.UTF_8.name());
                } else {
                    itemOut = android.util.Xml.resolveSerializer(os);
                }
                itemOut.startDocument((java.lang.String) null, true);
                saveToXml(itemOut, forBackup);
                itemOut.endDocument();
                os.flush();
                file.finishWrite(os);
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                android.util.Slog.e(TAG, "Failed to write to file " + file.getBaseFile(), e);
                file.failWrite(os);
            }
            if (file != null) {
                file.close();
            }
        } catch (java.lang.Throwable th) {
            if (file != null) {
                try {
                    file.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    void scheduleSaveToAppSearchLocked() {
    }

    public org.json.JSONObject dumpCheckin(boolean clear) throws org.json.JSONException {
        org.json.JSONObject result = new org.json.JSONObject();
        result.put("name", this.mPackageName);
        return result;
    }

    public void verifyStates() {
    }

    public void scheduleSave() {
        this.mShortcutUser.mService.injectPostToHandlerDebounced(this.mSaveShortcutPackageRunner, this.mSaveShortcutPackageRunner);
    }

    void saveShortcutPackageItem() {
        waitForBitmapSaves();
        java.io.File path = getShortcutPackageItemFile();
        boolean z = com.android.server.pm.ShortcutService.DEBUG;
        android.util.Slog.d(TAG, "Saving package item " + getPackageName() + " to " + path);
        synchronized (this.mPackageItemLock) {
            path.getParentFile().mkdirs();
            saveToFileLocked(path, false);
            scheduleSaveToAppSearchLocked();
        }
    }

    public boolean waitForBitmapSaves() {
        boolean zWaitForAllSavesLocked;
        synchronized (this.mPackageItemLock) {
            zWaitForAllSavesLocked = this.mShortcutBitmapSaver.waitForAllSavesLocked();
        }
        return zWaitForAllSavesLocked;
    }

    public void saveBitmap(android.content.pm.ShortcutInfo shortcut, int maxDimension, android.graphics.Bitmap.CompressFormat format, int quality) {
        synchronized (this.mPackageItemLock) {
            this.mShortcutBitmapSaver.saveBitmapLocked(shortcut, maxDimension, format, quality);
        }
    }

    public java.lang.String getBitmapPathMayWait(android.content.pm.ShortcutInfo shortcut) {
        java.lang.String bitmapPathMayWaitLocked;
        synchronized (this.mPackageItemLock) {
            bitmapPathMayWaitLocked = this.mShortcutBitmapSaver.getBitmapPathMayWaitLocked(shortcut);
        }
        return bitmapPathMayWaitLocked;
    }

    public void removeIcon(android.content.pm.ShortcutInfo shortcut) {
        synchronized (this.mPackageItemLock) {
            this.mShortcutBitmapSaver.removeIcon(shortcut);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeShortcutPackageItem() {
        synchronized (this.mPackageItemLock) {
            getResilientFile(getShortcutPackageItemFile()).delete();
        }
    }

    protected static com.android.server.pm.ResilientAtomicFile getResilientFile(java.io.File file) {
        java.lang.String path = file.getPath();
        java.io.File temporaryBackup = new java.io.File(path + ".backup");
        java.io.File reserveCopy = new java.io.File(path + ".reservecopy");
        return new com.android.server.pm.ResilientAtomicFile(file, temporaryBackup, reserveCopy, 505, "shortcut package item", null);
    }
}
