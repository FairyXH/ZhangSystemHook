package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class ModuleInfoProvider {
    private static final java.lang.String MODULE_METADATA_KEY = "android.content.pm.MODULE_METADATA";
    private static final java.lang.String TAG = "PackageManager.ModuleInfoProvider";
    private final com.android.server.pm.ApexManager mApexManager;
    private final android.content.Context mContext;
    private volatile boolean mMetadataLoaded;
    private final java.util.Map<java.lang.String, android.content.pm.ModuleInfo> mModuleInfo;
    private android.content.pm.IPackageManager mPackageManager;
    private volatile java.lang.String mPackageName;

    ModuleInfoProvider(android.content.Context context) {
        this.mContext = context;
        this.mApexManager = com.android.server.pm.ApexManager.getInstance();
        this.mModuleInfo = new android.util.ArrayMap();
    }

    public ModuleInfoProvider(android.content.res.XmlResourceParser metadata, android.content.res.Resources resources, com.android.server.pm.ApexManager apexManager) {
        this.mContext = null;
        this.mApexManager = apexManager;
        this.mModuleInfo = new android.util.ArrayMap();
        loadModuleMetadata(metadata, resources);
    }

    private android.content.pm.IPackageManager getPackageManager() {
        if (this.mPackageManager == null) {
            this.mPackageManager = android.content.pm.IPackageManager.Stub.asInterface(android.os.ServiceManager.getService("package"));
        }
        return this.mPackageManager;
    }

    public void systemReady() {
        this.mPackageName = this.mContext.getResources().getString(android.R.string.config_defaultQrCodeComponent);
        if (android.text.TextUtils.isEmpty(this.mPackageName)) {
            android.util.Slog.w(TAG, "No configured module metadata provider.");
            return;
        }
        try {
            android.content.pm.PackageInfo pi = getPackageManager().getPackageInfo(this.mPackageName, 128L, 0);
            android.content.Context packageContext = this.mContext.createPackageContext(this.mPackageName, 0);
            android.content.res.Resources packageResources = packageContext.getResources();
            android.content.res.XmlResourceParser parser = packageResources.getXml(pi.applicationInfo.metaData.getInt(MODULE_METADATA_KEY));
            loadModuleMetadata(parser, packageResources);
        } catch (android.content.pm.PackageManager.NameNotFoundException | android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Unable to discover metadata package: " + this.mPackageName, e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x0020, code lost:
    
        android.util.Slog.w(com.android.server.pm.ModuleInfoProvider.TAG, "Unexpected metadata element: " + r8.getName());
        r7.mModuleInfo.clear();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void loadModuleMetadata(android.content.res.XmlResourceParser r8, android.content.res.Resources r9) {
        /*
            r7 = this;
            java.lang.String r0 = "PackageManager.ModuleInfoProvider"
            r1 = 1
            java.lang.String r2 = "module-metadata"
            com.android.internal.util.XmlUtils.beginDocument(r8, r2)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98
        L9:
            com.android.internal.util.XmlUtils.nextElement(r8)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98
            int r2 = r8.getEventType()     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98
            if (r2 != r1) goto L13
            goto L40
        L13:
            java.lang.String r2 = "module"
            java.lang.String r3 = r8.getName()     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98
            boolean r2 = r2.equals(r3)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98
            if (r2 != 0) goto L46
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98
            r2.<init>()     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98
            java.lang.String r3 = "Unexpected metadata element: "
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98
            java.lang.String r3 = r8.getName()     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98
            java.lang.String r2 = r2.toString()     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98
            android.util.Slog.w(r0, r2)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98
            java.util.Map<java.lang.String, android.content.pm.ModuleInfo> r2 = r7.mModuleInfo     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98
            r2.clear()     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98
        L40:
            r8.close()
            r7.mMetadataLoaded = r1
            goto La4
        L46:
            java.lang.String r2 = "name"
            r3 = 0
            java.lang.String r2 = r8.getAttributeValue(r3, r2)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            java.lang.String r2 = r2.substring(r1)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            int r2 = java.lang.Integer.parseInt(r2)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            java.lang.CharSequence r2 = r9.getText(r2)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            java.lang.String r3 = "packageName"
            java.lang.String r3 = com.android.internal.util.XmlUtils.readStringAttribute(r8, r3)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            java.lang.String r4 = "isHidden"
            boolean r4 = com.android.internal.util.XmlUtils.readBooleanAttribute(r8, r4)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            android.content.pm.ModuleInfo r5 = new android.content.pm.ModuleInfo     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            r5.<init>()     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            r5.setHidden(r4)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            r5.setPackageName(r3)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            r5.setName(r2)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            com.android.server.pm.ApexManager r6 = r7.mApexManager     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            java.lang.String r6 = r6.getApexModuleNameForPackageName(r3)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            r5.setApexModuleName(r6)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            boolean r6 = com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.provideInfoOfApkInApex()     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            if (r6 == 0) goto L8e
            com.android.server.pm.ApexManager r6 = r7.mApexManager     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            java.util.List r6 = r6.getApksInApex(r3)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            r5.setApkInApexPackageNames(r6)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
        L8e:
            java.util.Map<java.lang.String, android.content.pm.ModuleInfo> r6 = r7.mModuleInfo     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            r6.put(r3, r5)     // Catch: java.lang.Throwable -> L96 java.lang.Throwable -> L98 java.lang.Throwable -> L98
            goto L9
        L96:
            r0 = move-exception
            goto La5
        L98:
            r2 = move-exception
            java.lang.String r3 = "Error parsing module metadata"
            android.util.Slog.w(r0, r3, r2)     // Catch: java.lang.Throwable -> L96
            java.util.Map<java.lang.String, android.content.pm.ModuleInfo> r0 = r7.mModuleInfo     // Catch: java.lang.Throwable -> L96
            r0.clear()     // Catch: java.lang.Throwable -> L96
            goto L40
        La4:
            return
        La5:
            r8.close()
            r7.mMetadataLoaded = r1
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ModuleInfoProvider.loadModuleMetadata(android.content.res.XmlResourceParser, android.content.res.Resources):void");
    }

    java.util.List<android.content.pm.ModuleInfo> getInstalledModules(int flags) {
        if (!this.mMetadataLoaded) {
            throw new java.lang.IllegalStateException("Call to getInstalledModules before metadata loaded");
        }
        if ((131072 & flags) != 0) {
            return new java.util.ArrayList(this.mModuleInfo.values());
        }
        try {
            java.util.List<android.content.pm.PackageInfo> allPackages = getPackageManager().getInstalledPackages(1073741824 | flags, android.os.UserHandle.getCallingUserId()).getList();
            java.util.ArrayList<android.content.pm.ModuleInfo> installedModules = new java.util.ArrayList<>(allPackages.size());
            for (android.content.pm.PackageInfo p : allPackages) {
                android.content.pm.ModuleInfo m = this.mModuleInfo.get(p.packageName);
                if (m != null) {
                    installedModules.add(m);
                }
            }
            return installedModules;
        } catch (android.os.RemoteException e) {
            android.util.Slog.w(TAG, "Unable to retrieve all package names", e);
            return java.util.Collections.emptyList();
        }
    }

    android.content.pm.ModuleInfo getModuleInfo(java.lang.String name, int flags) {
        if (!this.mMetadataLoaded) {
            throw new java.lang.IllegalStateException("Call to getModuleInfo before metadata loaded");
        }
        if ((flags & 1) != 0) {
            for (android.content.pm.ModuleInfo moduleInfo : this.mModuleInfo.values()) {
                if (name.equals(moduleInfo.getApexModuleName())) {
                    return moduleInfo;
                }
            }
            return null;
        }
        return this.mModuleInfo.get(name);
    }

    java.lang.String getPackageName() {
        if (!this.mMetadataLoaded) {
            throw new java.lang.IllegalStateException("Call to getVersion before metadata loaded");
        }
        return this.mPackageName;
    }
}
