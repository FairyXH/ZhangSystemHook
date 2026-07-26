package com.android.server.policy.role;

/* JADX INFO: loaded from: classes3.dex */
public class RoleServicePlatformHelperImpl implements com.android.server.role.RoleServicePlatformHelper {
    private static final java.lang.String ATTRIBUTE_NAME = "name";
    private static final java.lang.String LOG_TAG = com.android.server.policy.role.RoleServicePlatformHelperImpl.class.getSimpleName();
    private static final java.lang.String ROLES_FILE_NAME = "roles.xml";
    private static final java.lang.String TAG_HOLDER = "holder";
    private static final java.lang.String TAG_ROLE = "role";
    private static final java.lang.String TAG_ROLES = "roles";
    private final android.content.Context mContext;

    public RoleServicePlatformHelperImpl(android.content.Context context) {
        this.mContext = context;
    }

    @Override // com.android.server.role.RoleServicePlatformHelper
    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getLegacyRoleState(int userId) {
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> roles = readFile(userId);
        if (roles == null) {
            return readFromLegacySettings(userId);
        }
        return roles;
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> readFile(int userId) {
        java.io.File file = getFile(userId);
        try {
            try {
                java.io.FileInputStream in = new android.util.AtomicFile(file).openRead();
                try {
                    org.xmlpull.v1.XmlPullParser parser = android.util.Xml.newPullParser();
                    parser.setInput(in, null);
                    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> roles = parseXml(parser);
                    android.util.Slog.i(LOG_TAG, "Read legacy roles.xml successfully");
                    if (in != null) {
                        in.close();
                    }
                    return roles;
                } catch (java.lang.Throwable th) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (java.lang.Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                android.util.Slog.wtf(LOG_TAG, "Failed to parse legacy roles.xml: " + file, e);
                return null;
            }
        } catch (java.io.FileNotFoundException e2) {
            android.util.Slog.i(LOG_TAG, "Legacy roles.xml not found");
            return null;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x0036, code lost:
    
        throw new java.io.IOException("Missing <roles> in roles.xml");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> parseXml(org.xmlpull.v1.XmlPullParser r7) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            r6 = this;
            int r0 = r7.getDepth()
            r1 = 1
            int r0 = r0 + r1
        L6:
            int r2 = r7.next()
            r3 = r2
            if (r2 == r1) goto L2f
            int r2 = r7.getDepth()
            r4 = r2
            if (r2 >= r0) goto L17
            r2 = 3
            if (r3 == r2) goto L2f
        L17:
            if (r4 > r0) goto L6
            r2 = 2
            if (r3 == r2) goto L1d
            goto L6
        L1d:
            java.lang.String r2 = r7.getName()
            java.lang.String r5 = "roles"
            boolean r2 = r2.equals(r5)
            if (r2 == 0) goto L6
            java.util.Map r1 = r6.parseRoles(r7)
            return r1
        L2f:
            java.io.IOException r1 = new java.io.IOException
            java.lang.String r2 = "Missing <roles> in roles.xml"
            r1.<init>(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.policy.role.RoleServicePlatformHelperImpl.parseXml(org.xmlpull.v1.XmlPullParser):java.util.Map");
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> parseRoles(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int depth;
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> roles = new android.util.ArrayMap<>();
        int innerDepth = parser.getDepth() + 1;
        while (true) {
            int type = parser.next();
            if (type == 1 || ((depth = parser.getDepth()) < innerDepth && type == 3)) {
                break;
            }
            if (depth <= innerDepth && type == 2 && parser.getName().equals(TAG_ROLE)) {
                java.lang.String roleName = parser.getAttributeValue(null, "name");
                java.util.Set<java.lang.String> roleHolders = parseRoleHoldersLocked(parser);
                roles.put(roleName, roleHolders);
            }
        }
        return roles;
    }

    private java.util.Set<java.lang.String> parseRoleHoldersLocked(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int depth;
        java.util.Set<java.lang.String> roleHolders = new android.util.ArraySet<>();
        int innerDepth = parser.getDepth() + 1;
        while (true) {
            int type = parser.next();
            if (type == 1 || ((depth = parser.getDepth()) < innerDepth && type == 3)) {
                break;
            }
            if (depth <= innerDepth && type == 2 && parser.getName().equals(TAG_HOLDER)) {
                java.lang.String roleHolder = parser.getAttributeValue(null, "name");
                roleHolders.add(roleHolder);
            }
        }
        return roleHolders;
    }

    private static java.io.File getFile(int userId) {
        return new java.io.File(android.os.Environment.getUserSystemDirectory(userId), ROLES_FILE_NAME);
    }

    private java.util.Map<java.lang.String, java.util.Set<java.lang.String>> readFromLegacySettings(int userId) {
        java.lang.String assistantPackageName;
        java.lang.String dialerPackageName;
        java.lang.String smsPackageName;
        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> roles = new android.util.ArrayMap<>();
        android.content.ContentResolver contentResolver = this.mContext.getContentResolver();
        java.lang.String assistantSetting = android.provider.Settings.Secure.getStringForUser(contentResolver, "assistant", userId);
        android.content.pm.PackageManager packageManager = this.mContext.getPackageManager();
        java.lang.String homePackageName = null;
        if (assistantSetting != null) {
            if (!assistantSetting.isEmpty()) {
                android.content.ComponentName componentName = android.content.ComponentName.unflattenFromString(assistantSetting);
                assistantPackageName = componentName != null ? componentName.getPackageName() : null;
            } else {
                assistantPackageName = null;
            }
        } else if (packageManager.isDeviceUpgrading()) {
            java.lang.String defaultAssistant = this.mContext.getString(android.R.string.config_defaultAssistant);
            assistantPackageName = !android.text.TextUtils.isEmpty(defaultAssistant) ? defaultAssistant : null;
        } else {
            assistantPackageName = null;
        }
        if (assistantPackageName != null) {
            roles.put("android.app.role.ASSISTANT", java.util.Collections.singleton(assistantPackageName));
        }
        android.content.pm.PackageManagerInternal packageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        java.lang.String browserPackageName = packageManagerInternal.removeLegacyDefaultBrowserPackageName(userId);
        if (browserPackageName != null) {
            roles.put("android.app.role.BROWSER", java.util.Collections.singleton(browserPackageName));
        }
        java.lang.String dialerSetting = android.provider.Settings.Secure.getStringForUser(contentResolver, "dialer_default_application", userId);
        if (!android.text.TextUtils.isEmpty(dialerSetting)) {
            dialerPackageName = dialerSetting;
        } else if (packageManager.isDeviceUpgrading()) {
            dialerPackageName = this.mContext.getString(android.R.string.config_defaultDialer);
        } else {
            dialerPackageName = null;
        }
        if (dialerPackageName != null) {
            roles.put("android.app.role.DIALER", java.util.Collections.singleton(dialerPackageName));
        }
        java.lang.String smsSetting = android.provider.Settings.Secure.getStringForUser(contentResolver, "sms_default_application", userId);
        if (!android.text.TextUtils.isEmpty(smsSetting)) {
            smsPackageName = smsSetting;
        } else if (this.mContext.getPackageManager().isDeviceUpgrading()) {
            smsPackageName = this.mContext.getString(android.R.string.config_defaultSms);
        } else {
            smsPackageName = null;
        }
        if (smsPackageName != null) {
            roles.put("android.app.role.SMS", java.util.Collections.singleton(smsPackageName));
        }
        if (packageManager.isDeviceUpgrading()) {
            android.content.pm.ResolveInfo resolveInfo = packageManager.resolveActivityAsUser(new android.content.Intent("android.intent.action.MAIN").addCategory("android.intent.category.HOME"), 851968, userId);
            if (resolveInfo != null && resolveInfo.activityInfo != null) {
                homePackageName = resolveInfo.activityInfo.packageName;
            }
            if (homePackageName != null && isSettingsApplication(homePackageName, userId)) {
                homePackageName = null;
            }
        } else {
            homePackageName = null;
        }
        if (homePackageName != null) {
            roles.put("android.app.role.HOME", java.util.Collections.singleton(homePackageName));
        }
        java.lang.String emergencyPackageName = android.provider.Settings.Secure.getStringForUser(contentResolver, "emergency_assistance_application", userId);
        if (emergencyPackageName != null) {
            roles.put("android.app.role.EMERGENCY", java.util.Collections.singleton(emergencyPackageName));
        }
        return roles;
    }

    private boolean isSettingsApplication(java.lang.String packageName, int userId) {
        android.content.pm.PackageManager packageManager = this.mContext.getPackageManager();
        android.content.pm.ResolveInfo resolveInfo = packageManager.resolveActivityAsUser(new android.content.Intent("android.settings.SETTINGS"), 851968, userId);
        if (resolveInfo == null || resolveInfo.activityInfo == null) {
            return false;
        }
        return java.util.Objects.equals(packageName, resolveInfo.activityInfo.packageName);
    }

    @Override // com.android.server.role.RoleServicePlatformHelper
    public java.lang.String computePackageStateHash(final int userId) {
        android.content.ComponentName deviceOwnerComponent;
        android.content.ComponentName profileOwnerComponent;
        final android.content.pm.PackageManagerInternal packageManagerInternal = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        android.app.admin.DevicePolicyManagerInternal devicePolicyManagerInternal = (android.app.admin.DevicePolicyManagerInternal) com.android.server.LocalServices.getService(android.app.admin.DevicePolicyManagerInternal.class);
        com.android.server.policy.role.RoleServicePlatformHelperImpl.MessageDigestOutputStream mdos = new com.android.server.policy.role.RoleServicePlatformHelperImpl.MessageDigestOutputStream();
        final java.io.DataOutputStream dataOutputStream = new java.io.DataOutputStream(new java.io.BufferedOutputStream(mdos));
        packageManagerInternal.forEachInstalledPackage(new java.util.function.Consumer() { // from class: com.android.server.policy.role.RoleServicePlatformHelperImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.policy.role.RoleServicePlatformHelperImpl.lambda$computePackageStateHash$0(dataOutputStream, packageManagerInternal, userId, (com.android.server.pm.pkg.AndroidPackage) obj);
            }
        }, userId);
        java.lang.String profileOwner = "";
        java.lang.String deviceOwner = "";
        if (devicePolicyManagerInternal != null) {
            try {
                if (devicePolicyManagerInternal.getDeviceOwnerUserId() == userId && (deviceOwnerComponent = devicePolicyManagerInternal.getDeviceOwnerComponent(false)) != null) {
                    deviceOwner = deviceOwnerComponent.getPackageName();
                }
            } catch (java.io.IOException e) {
                throw new java.lang.AssertionError(e);
            }
        }
        dataOutputStream.writeUTF(deviceOwner);
        if (devicePolicyManagerInternal != null && (profileOwnerComponent = devicePolicyManagerInternal.getProfileOwnerAsUser(userId)) != null) {
            profileOwner = profileOwnerComponent.getPackageName();
        }
        dataOutputStream.writeUTF(profileOwner);
        dataOutputStream.writeInt(android.provider.Settings.Global.getInt(this.mContext.getContentResolver(), "device_demo_mode", 0));
        dataOutputStream.writeBoolean(com.android.internal.hidden_from_bootclasspath.android.permission.flags.Flags.walletRoleEnabled());
        dataOutputStream.flush();
        return mdos.getDigestAsString();
    }

    static /* synthetic */ void lambda$computePackageStateHash$0(java.io.DataOutputStream dataOutputStream, android.content.pm.PackageManagerInternal packageManagerInternal, int userId, com.android.server.pm.pkg.AndroidPackage pkg) {
        try {
            dataOutputStream.writeUTF(pkg.getPackageName());
            dataOutputStream.writeLong(pkg.getLongVersionCode());
            dataOutputStream.writeInt(packageManagerInternal.getApplicationEnabledState(pkg.getPackageName(), userId));
            java.util.Set<java.lang.String> requestedPermissions = pkg.getRequestedPermissions();
            dataOutputStream.writeInt(requestedPermissions.size());
            for (java.lang.String permissionName : requestedPermissions) {
                dataOutputStream.writeUTF(permissionName);
            }
            android.util.ArraySet<java.lang.String> enabledComponents = packageManagerInternal.getEnabledComponents(pkg.getPackageName(), userId);
            int enabledComponentsSize = com.android.internal.util.CollectionUtils.size(enabledComponents);
            dataOutputStream.writeInt(enabledComponentsSize);
            for (int i = 0; i < enabledComponentsSize; i++) {
                dataOutputStream.writeUTF(enabledComponents.valueAt(i));
            }
            android.util.ArraySet<java.lang.String> disabledComponents = packageManagerInternal.getDisabledComponents(pkg.getPackageName(), userId);
            int disabledComponentsSize = com.android.internal.util.CollectionUtils.size(disabledComponents);
            for (int i2 = 0; i2 < disabledComponentsSize; i2++) {
                dataOutputStream.writeUTF(disabledComponents.valueAt(i2));
            }
            for (android.content.pm.Signature signature : pkg.getSigningDetails().getSignatures()) {
                dataOutputStream.write(signature.toByteArray());
            }
        } catch (java.io.IOException e) {
            throw new java.lang.AssertionError(e);
        }
    }

    private static class MessageDigestOutputStream extends java.io.OutputStream {
        private final java.security.MessageDigest mMessageDigest;

        MessageDigestOutputStream() {
            try {
                this.mMessageDigest = java.security.MessageDigest.getInstance("SHA256");
            } catch (java.security.NoSuchAlgorithmException e) {
                throw new java.lang.RuntimeException("Failed to create MessageDigest", e);
            }
        }

        java.lang.String getDigestAsString() {
            return libcore.util.HexEncoding.encodeToString(this.mMessageDigest.digest(), true);
        }

        @Override // java.io.OutputStream
        public void write(int b) throws java.io.IOException {
            this.mMessageDigest.update((byte) b);
        }

        @Override // java.io.OutputStream
        public void write(byte[] b) throws java.io.IOException {
            this.mMessageDigest.update(b);
        }

        @Override // java.io.OutputStream
        public void write(byte[] b, int off, int len) throws java.io.IOException {
            this.mMessageDigest.update(b, off, len);
        }
    }
}
