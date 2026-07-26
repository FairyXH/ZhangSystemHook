package com.android.server.locales;

/* JADX INFO: loaded from: classes2.dex */
public class SystemAppUpdateTracker {
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String PACKAGE_XML_TAG = "package";
    private static final java.lang.String SYSTEM_APPS_XML_TAG = "system_apps";
    private static final java.lang.String TAG = "SystemAppUpdateTracker";
    private final android.content.Context mContext;
    private final java.lang.Object mFileLock;
    private final com.android.server.locales.LocaleManagerService mLocaleManagerService;
    private final java.util.Set<java.lang.String> mUpdatedApps;
    private final android.util.AtomicFile mUpdatedAppsFile;

    SystemAppUpdateTracker(com.android.server.locales.LocaleManagerService localeManagerService) {
        this(localeManagerService.mContext, localeManagerService, new android.util.AtomicFile(new java.io.File(android.os.Environment.getDataSystemDirectory(), "locale_manager_service_updated_system_apps.xml")));
    }

    SystemAppUpdateTracker(android.content.Context context, com.android.server.locales.LocaleManagerService localeManagerService, android.util.AtomicFile file) {
        this.mFileLock = new java.lang.Object();
        this.mUpdatedApps = new java.util.HashSet();
        this.mContext = context;
        this.mLocaleManagerService = localeManagerService;
        this.mUpdatedAppsFile = file;
    }

    void init() {
        loadUpdatedSystemApps();
    }

    private void loadUpdatedSystemApps() {
        if (this.mUpdatedAppsFile.getBaseFile().exists()) {
            java.io.InputStream updatedAppNamesInputStream = null;
            try {
                try {
                    updatedAppNamesInputStream = this.mUpdatedAppsFile.openRead();
                    readFromXml(updatedAppNamesInputStream);
                } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                    android.util.Slog.e(TAG, "loadUpdatedSystemApps: Could not parse storage file ", e);
                }
            } finally {
                libcore.io.IoUtils.closeQuietly(updatedAppNamesInputStream);
            }
        }
    }

    private void readFromXml(java.io.InputStream updateInfoInputStream) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.newFastPullParser();
        parser.setInput(updateInfoInputStream, java.nio.charset.StandardCharsets.UTF_8.name());
        com.android.internal.util.XmlUtils.beginDocument(parser, SYSTEM_APPS_XML_TAG);
        int depth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
            if (parser.getName().equals("package")) {
                java.lang.String packageName = parser.getAttributeValue((java.lang.String) null, "name");
                if (!android.text.TextUtils.isEmpty(packageName)) {
                    this.mUpdatedApps.add(packageName);
                }
            }
        }
    }

    void onPackageUpdateFinished(java.lang.String packageName, int uid) {
        try {
            if (!this.mUpdatedApps.contains(packageName) && isUpdatedSystemApp(packageName)) {
                int userId = android.os.UserHandle.getUserId(uid);
                java.lang.String installingPackageName = this.mLocaleManagerService.getInstallingPackageName(packageName, userId);
                if (installingPackageName == null) {
                    return;
                }
                try {
                    android.os.LocaleList appLocales = this.mLocaleManagerService.getApplicationLocales(packageName, userId);
                    if (!appLocales.isEmpty()) {
                        this.mLocaleManagerService.notifyInstallerOfAppWhoseLocaleChanged(packageName, userId, appLocales);
                    }
                } catch (android.os.RemoteException e) {
                }
                updateBroadcastedAppsList(packageName);
            }
        } catch (java.lang.Exception e2) {
            android.util.Slog.e(TAG, "Exception in onPackageUpdateFinished.", e2);
        }
    }

    private void updateBroadcastedAppsList(java.lang.String packageName) {
        synchronized (this.mFileLock) {
            this.mUpdatedApps.add(packageName);
            writeUpdatedAppsFileLocked();
        }
    }

    private void writeUpdatedAppsFileLocked() {
        java.io.FileOutputStream stream = null;
        try {
            stream = this.mUpdatedAppsFile.startWrite();
            writeToXmlLocked(stream);
            this.mUpdatedAppsFile.finishWrite(stream);
        } catch (java.io.IOException e) {
            this.mUpdatedAppsFile.failWrite(stream);
            android.util.Slog.e(TAG, "Failed to persist the updated apps list", e);
        }
    }

    private void writeToXmlLocked(java.io.OutputStream stream) throws java.io.IOException {
        com.android.modules.utils.TypedXmlSerializer xml = android.util.Xml.newFastSerializer();
        xml.setOutput(stream, java.nio.charset.StandardCharsets.UTF_8.name());
        xml.startDocument((java.lang.String) null, true);
        xml.startTag((java.lang.String) null, SYSTEM_APPS_XML_TAG);
        for (java.lang.String packageName : this.mUpdatedApps) {
            xml.startTag((java.lang.String) null, "package");
            xml.attribute((java.lang.String) null, "name", packageName);
            xml.endTag((java.lang.String) null, "package");
        }
        xml.endTag((java.lang.String) null, SYSTEM_APPS_XML_TAG);
        xml.endDocument();
    }

    private boolean isUpdatedSystemApp(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        android.content.pm.ApplicationInfo appInfo = null;
        try {
            appInfo = this.mContext.getPackageManager().getApplicationInfo(packageName, android.content.pm.PackageManager.ApplicationInfoFlags.of(1048576L));
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        }
        return (appInfo == null || (appInfo.flags & 128) == 0) ? false : true;
    }

    java.util.Set<java.lang.String> getUpdatedApps() {
        return this.mUpdatedApps;
    }
}
