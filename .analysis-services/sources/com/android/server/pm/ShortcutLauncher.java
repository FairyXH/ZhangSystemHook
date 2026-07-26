package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class ShortcutLauncher extends com.android.server.pm.ShortcutPackageItem {
    private static final java.lang.String ATTR_LAUNCHER_USER_ID = "launcher-user";
    private static final java.lang.String ATTR_PACKAGE_NAME = "package-name";
    private static final java.lang.String ATTR_PACKAGE_USER_ID = "package-user";
    private static final java.lang.String ATTR_VALUE = "value";
    private static final java.lang.String TAG = "ShortcutService";
    private static final java.lang.String TAG_PACKAGE = "package";
    private static final java.lang.String TAG_PIN = "pin";
    static final java.lang.String TAG_ROOT = "launcher-pins";
    private final int mOwnerUserId;
    private final android.util.ArrayMap<android.content.pm.UserPackage, android.util.ArraySet<java.lang.String>> mPinnedShortcuts;

    private ShortcutLauncher(com.android.server.pm.ShortcutUser shortcutUser, int ownerUserId, java.lang.String packageName, int launcherUserId, com.android.server.pm.ShortcutPackageInfo spi) {
        super(shortcutUser, launcherUserId, packageName, spi != null ? spi : com.android.server.pm.ShortcutPackageInfo.newEmpty());
        this.mPinnedShortcuts = new android.util.ArrayMap<>();
        this.mOwnerUserId = ownerUserId;
    }

    public ShortcutLauncher(com.android.server.pm.ShortcutUser shortcutUser, int ownerUserId, java.lang.String packageName, int launcherUserId) {
        this(shortcutUser, ownerUserId, packageName, launcherUserId, null);
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    public int getOwnerUserId() {
        return this.mOwnerUserId;
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    protected boolean canRestoreAnyVersion() {
        return true;
    }

    private void onRestoreBlocked() {
        java.util.ArrayList<android.content.pm.UserPackage> pinnedPackages;
        synchronized (this.mPackageItemLock) {
            pinnedPackages = new java.util.ArrayList<>(this.mPinnedShortcuts.keySet());
            this.mPinnedShortcuts.clear();
        }
        for (int i = pinnedPackages.size() - 1; i >= 0; i--) {
            android.content.pm.UserPackage up = pinnedPackages.get(i);
            com.android.server.pm.ShortcutPackage p = this.mShortcutUser.getPackageShortcutsIfExists(up.packageName);
            if (p != null) {
                p.refreshPinnedFlags();
            }
        }
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    protected void onRestored(int restoreBlockReason) {
        if (restoreBlockReason != 0) {
            onRestoreBlocked();
        }
    }

    public void pinShortcuts(int packageUserId, java.lang.String packageName, java.util.List<java.lang.String> ids, boolean forPinRequest) {
        com.android.server.pm.ShortcutPackage packageShortcuts = this.mShortcutUser.getPackageShortcutsIfExists(packageName);
        if (packageShortcuts == null) {
            return;
        }
        android.content.pm.UserPackage up = android.content.pm.UserPackage.of(packageUserId, packageName);
        int idSize = ids.size();
        if (idSize == 0) {
            synchronized (this.mPackageItemLock) {
                this.mPinnedShortcuts.remove(up);
            }
        } else {
            android.util.ArraySet<java.lang.String> floatingSet = new android.util.ArraySet<>();
            android.util.ArraySet<java.lang.String> newSet = new android.util.ArraySet<>();
            for (int i = 0; i < idSize; i++) {
                java.lang.String id = ids.get(i);
                android.content.pm.ShortcutInfo si = packageShortcuts.findShortcutById(id);
                if (si != null) {
                    if (si.isDynamic() || si.isLongLived() || si.isManifestShortcut() || forPinRequest) {
                        newSet.add(id);
                    } else {
                        floatingSet.add(id);
                    }
                }
            }
            synchronized (this.mPackageItemLock) {
                android.util.ArraySet<java.lang.String> prevSet = this.mPinnedShortcuts.get(up);
                if (prevSet != null) {
                    for (java.lang.String id2 : floatingSet) {
                        if (prevSet.contains(id2)) {
                            newSet.add(id2);
                        }
                    }
                }
                this.mPinnedShortcuts.put(up, newSet);
            }
        }
        packageShortcuts.refreshPinnedFlags();
    }

    public android.util.ArraySet<java.lang.String> getPinnedShortcutIds(java.lang.String packageName, int packageUserId) {
        android.util.ArraySet<java.lang.String> arraySet;
        synchronized (this.mPackageItemLock) {
            android.util.ArraySet<java.lang.String> pinnedShortcuts = this.mPinnedShortcuts.get(android.content.pm.UserPackage.of(packageUserId, packageName));
            arraySet = pinnedShortcuts == null ? null : new android.util.ArraySet<>((android.util.ArraySet) pinnedShortcuts);
        }
        return arraySet;
    }

    public boolean hasPinned(android.content.pm.ShortcutInfo shortcut) {
        boolean z;
        synchronized (this.mPackageItemLock) {
            android.util.ArraySet<java.lang.String> pinned = this.mPinnedShortcuts.get(android.content.pm.UserPackage.of(shortcut.getUserId(), shortcut.getPackage()));
            z = pinned != null && pinned.contains(shortcut.getId());
        }
        return z;
    }

    public void addPinnedShortcut(java.lang.String packageName, int packageUserId, java.lang.String id, boolean forPinRequest) {
        java.util.ArrayList<java.lang.String> pinnedList;
        synchronized (this.mPackageItemLock) {
            android.util.ArraySet<java.lang.String> pinnedSet = this.mPinnedShortcuts.get(android.content.pm.UserPackage.of(packageUserId, packageName));
            if (pinnedSet != null) {
                pinnedList = new java.util.ArrayList<>(pinnedSet.size() + 1);
                pinnedList.addAll(pinnedSet);
            } else {
                pinnedList = new java.util.ArrayList<>(1);
            }
        }
        pinnedList.add(id);
        pinShortcuts(packageUserId, packageName, pinnedList, forPinRequest);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean cleanUpPackage(java.lang.String packageName, int packageUserId) {
        boolean z;
        synchronized (this.mPackageItemLock) {
            z = this.mPinnedShortcuts.remove(android.content.pm.UserPackage.of(packageUserId, packageName)) != null;
        }
        return z;
    }

    public void ensurePackageInfo() {
        android.content.pm.PackageInfo pi = this.mShortcutUser.mService.getPackageInfoWithSignatures(getPackageName(), getPackageUserId());
        if (pi == null) {
            android.util.Slog.w(TAG, "Package not found: " + getPackageName());
        } else {
            getPackageInfo().updateFromPackageInfo(pi);
        }
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    public void saveToXml(com.android.modules.utils.TypedXmlSerializer out, boolean forBackup) throws java.io.IOException {
        android.util.ArrayMap<android.content.pm.UserPackage, android.util.ArraySet<java.lang.String>> pinnedShortcuts;
        if (forBackup && !getPackageInfo().isBackupAllowed()) {
            return;
        }
        synchronized (this.mPackageItemLock) {
            pinnedShortcuts = new android.util.ArrayMap<>(this.mPinnedShortcuts);
        }
        int size = pinnedShortcuts.size();
        if (size == 0) {
            return;
        }
        out.startTag((java.lang.String) null, TAG_ROOT);
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_PACKAGE_NAME, getPackageName());
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_LAUNCHER_USER_ID, getPackageUserId());
        getPackageInfo().saveToXml(this.mShortcutUser.mService, out, forBackup);
        for (int i = 0; i < size; i++) {
            android.content.pm.UserPackage up = pinnedShortcuts.keyAt(i);
            if (up != null && (!forBackup || up.userId == getOwnerUserId())) {
                out.startTag((java.lang.String) null, "package");
                com.android.server.pm.ShortcutService.writeAttr(out, ATTR_PACKAGE_NAME, up.packageName);
                com.android.server.pm.ShortcutService.writeAttr(out, ATTR_PACKAGE_USER_ID, up.userId);
                android.util.ArraySet<java.lang.String> ids = pinnedShortcuts.valueAt(i);
                int idSize = ids.size();
                for (int j = 0; j < idSize; j++) {
                    com.android.server.pm.ShortcutService.writeTagValue(out, TAG_PIN, ids.valueAt(j));
                }
                out.endTag((java.lang.String) null, "package");
            }
        }
        out.endTag((java.lang.String) null, TAG_ROOT);
    }

    public static com.android.server.pm.ShortcutLauncher loadFromFile(java.io.File path, com.android.server.pm.ShortcutUser shortcutUser, int ownerUserId, boolean fromBackup) {
        com.android.server.pm.ResilientAtomicFile file = getResilientFile(path);
        try {
            try {
                java.io.FileInputStream in = file.openRead();
                if (in == null) {
                    android.util.Slog.d(TAG, "Not found " + path);
                    if (file != null) {
                        file.close();
                        return null;
                    }
                    return null;
                }
                com.android.server.pm.ShortcutLauncher ret = null;
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
                while (true) {
                    int type = parser.next();
                    if (type == 1) {
                        break;
                    }
                    if (type == 2) {
                        int depth = parser.getDepth();
                        java.lang.String tag = parser.getName();
                        if (depth == 1 && TAG_ROOT.equals(tag)) {
                            ret = loadFromXml(parser, shortcutUser, ownerUserId, fromBackup);
                        } else {
                            com.android.server.pm.ShortcutService.throwForInvalidTag(depth, tag);
                        }
                    }
                }
                if (file != null) {
                    file.close();
                }
                return ret;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Failed to read file " + file.getBaseFile(), e);
                file.failRead(null, e);
                com.android.server.pm.ShortcutLauncher shortcutLauncherLoadFromFile = loadFromFile(path, shortcutUser, ownerUserId, fromBackup);
                if (file != null) {
                    file.close();
                }
                return shortcutLauncherLoadFromFile;
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

    /* JADX WARN: Removed duplicated region for block: B:57:0x00df  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.android.server.pm.ShortcutLauncher loadFromXml(com.android.modules.utils.TypedXmlPullParser r16, com.android.server.pm.ShortcutUser r17, int r18, boolean r19) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instruction units count: 262
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ShortcutLauncher.loadFromXml(com.android.modules.utils.TypedXmlPullParser, com.android.server.pm.ShortcutUser, int, boolean):com.android.server.pm.ShortcutLauncher");
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix, com.android.server.pm.ShortcutService.DumpFilter filter) {
        android.util.ArrayMap<android.content.pm.UserPackage, android.util.ArraySet<java.lang.String>> pinnedShortcuts;
        pw.println();
        pw.print(prefix);
        pw.print("Launcher: ");
        pw.print(getPackageName());
        pw.print("  Package user: ");
        pw.print(getPackageUserId());
        pw.print("  Owner user: ");
        pw.print(getOwnerUserId());
        pw.println();
        getPackageInfo().dump(pw, prefix + "  ");
        pw.println();
        synchronized (this.mPackageItemLock) {
            pinnedShortcuts = new android.util.ArrayMap<>(this.mPinnedShortcuts);
        }
        int size = pinnedShortcuts.size();
        for (int i = 0; i < size; i++) {
            pw.println();
            android.content.pm.UserPackage up = pinnedShortcuts.keyAt(i);
            pw.print(prefix);
            pw.print("  ");
            pw.print("Package: ");
            pw.print(up.packageName);
            pw.print("  User: ");
            pw.println(up.userId);
            android.util.ArraySet<java.lang.String> ids = pinnedShortcuts.valueAt(i);
            int idSize = ids.size();
            for (int j = 0; j < idSize; j++) {
                pw.print(prefix);
                pw.print("    Pinned: ");
                pw.print(ids.valueAt(j));
                pw.println();
            }
        }
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    public org.json.JSONObject dumpCheckin(boolean clear) throws org.json.JSONException {
        org.json.JSONObject result = super.dumpCheckin(clear);
        return result;
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    protected java.io.File getShortcutPackageItemFile() {
        java.io.File path = new java.io.File(this.mShortcutUser.mService.injectUserDataPath(this.mShortcutUser.getUserId()), "launchers");
        java.lang.String fileName = getPackageName() + getPackageUserId() + ".xml";
        return new java.io.File(path, fileName);
    }
}
