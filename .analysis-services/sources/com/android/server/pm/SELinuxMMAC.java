package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public final class SELinuxMMAC {
    private static final boolean DEBUG_POLICY = false;
    private static final boolean DEBUG_POLICY_INSTALL = false;
    private static final boolean DEBUG_POLICY_ORDER = false;
    private static final java.lang.String DEFAULT_SEINFO = "default";
    private static final java.lang.String PARTITION_STR = ":partition=";
    private static final java.lang.String PRIVILEGED_APP_STR = ":privapp";
    static final long SELINUX_LATEST_CHANGES = 143539591;
    static final long SELINUX_R_CHANGES = 168782947;
    static final java.lang.String TAG = "SELinuxMMAC";
    private static final java.lang.String TARGETSDKVERSION_STR = ":targetSdkVersion=";
    private static boolean sPolicyRead;
    private static final java.util.List<com.android.server.pm.Policy> sPolicies = new java.util.ArrayList();
    private static final java.util.List<java.io.File> sMacPermissions = new java.util.ArrayList();

    static {
        sMacPermissions.add(new java.io.File(android.os.Environment.getRootDirectory(), "/etc/selinux/plat_mac_permissions.xml"));
        java.io.File systemExtMacPermission = new java.io.File(android.os.Environment.getSystemExtDirectory(), "/etc/selinux/system_ext_mac_permissions.xml");
        if (systemExtMacPermission.exists()) {
            sMacPermissions.add(systemExtMacPermission);
        }
        java.io.File productMacPermission = new java.io.File(android.os.Environment.getProductDirectory(), "/etc/selinux/product_mac_permissions.xml");
        if (productMacPermission.exists()) {
            sMacPermissions.add(productMacPermission);
        }
        java.io.File vendorMacPermission = new java.io.File(android.os.Environment.getVendorDirectory(), "/etc/selinux/vendor_mac_permissions.xml");
        if (vendorMacPermission.exists()) {
            sMacPermissions.add(vendorMacPermission);
        }
        java.io.File odmMacPermission = new java.io.File(android.os.Environment.getOdmDirectory(), "/etc/selinux/odm_mac_permissions.xml");
        if (odmMacPermission.exists()) {
            sMacPermissions.add(odmMacPermission);
        }
    }

    public static boolean readInstallPolicy() {
        byte b;
        synchronized (sPolicies) {
            if (sPolicyRead) {
                return true;
            }
            java.util.List<com.android.server.pm.Policy> policies = new java.util.ArrayList<>();
            org.xmlpull.v1.XmlPullParser parser = android.util.Xml.newPullParser();
            int count = sMacPermissions.size();
            java.io.FileReader policyFile = null;
            for (int i = 0; i < count; i++) {
                java.io.File macPermission = sMacPermissions.get(i);
                try {
                    try {
                        try {
                            policyFile = new java.io.FileReader(macPermission);
                            android.util.Slog.d(TAG, "Using policy file " + macPermission);
                            parser.setInput(policyFile);
                            parser.nextTag();
                            parser.require(2, null, "policy");
                            while (parser.next() != 3) {
                                if (parser.getEventType() == 2) {
                                    java.lang.String name = parser.getName();
                                    switch (name.hashCode()) {
                                        case -902467798:
                                            if (name.equals("signer")) {
                                                b = 0;
                                                break;
                                            }
                                        default:
                                            b = -1;
                                            break;
                                    }
                                    switch (b) {
                                        case 0:
                                            policies.add(readSignerOrThrow(parser));
                                            break;
                                        default:
                                            skip(parser);
                                            break;
                                    }
                                }
                            }
                            libcore.io.IoUtils.closeQuietly(policyFile);
                        } catch (java.io.IOException ioe) {
                            android.util.Slog.w(TAG, "Exception parsing " + macPermission, ioe);
                            libcore.io.IoUtils.closeQuietly(policyFile);
                            return false;
                        }
                    } catch (java.lang.IllegalArgumentException | java.lang.IllegalStateException | org.xmlpull.v1.XmlPullParserException ex) {
                        android.util.Slog.w(TAG, "Exception @" + parser.getPositionDescription() + " while parsing " + macPermission + ":" + ex);
                        libcore.io.IoUtils.closeQuietly(policyFile);
                        return false;
                    }
                } catch (java.lang.Throwable th) {
                    libcore.io.IoUtils.closeQuietly(policyFile);
                    throw th;
                }
            }
            com.android.server.pm.PolicyComparator policySort = new com.android.server.pm.PolicyComparator();
            java.util.Collections.sort(policies, policySort);
            if (policySort.foundDuplicate()) {
                android.util.Slog.w(TAG, "ERROR! Duplicate entries found parsing mac_permissions.xml files");
                return false;
            }
            synchronized (sPolicies) {
                sPolicies.clear();
                sPolicies.addAll(policies);
                sPolicyRead = true;
            }
            return true;
        }
    }

    private static com.android.server.pm.Policy readSignerOrThrow(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        parser.require(2, null, "signer");
        com.android.server.pm.Policy.PolicyBuilder pb = new com.android.server.pm.Policy.PolicyBuilder();
        java.lang.String cert = parser.getAttributeValue(null, "signature");
        if (cert != null) {
            pb.addSignature(cert);
        }
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                java.lang.String tagName = parser.getName();
                if ("seinfo".equals(tagName)) {
                    java.lang.String seinfo = parser.getAttributeValue(null, "value");
                    pb.setGlobalSeinfoOrThrow(seinfo);
                    readSeinfo(parser);
                } else if ("package".equals(tagName)) {
                    readPackageOrThrow(parser, pb);
                } else if ("cert".equals(tagName)) {
                    java.lang.String sig = parser.getAttributeValue(null, "signature");
                    pb.addSignature(sig);
                    readCert(parser);
                } else {
                    skip(parser);
                }
            }
        }
        return pb.build();
    }

    private static void readPackageOrThrow(org.xmlpull.v1.XmlPullParser parser, com.android.server.pm.Policy.PolicyBuilder pb) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        parser.require(2, null, "package");
        java.lang.String pkgName = parser.getAttributeValue(null, "name");
        while (parser.next() != 3) {
            if (parser.getEventType() == 2) {
                java.lang.String tagName = parser.getName();
                if ("seinfo".equals(tagName)) {
                    java.lang.String seinfo = parser.getAttributeValue(null, "value");
                    pb.addInnerPackageMapOrThrow(pkgName, seinfo);
                    readSeinfo(parser);
                } else {
                    skip(parser);
                }
            }
        }
    }

    private static void readCert(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        parser.require(2, null, "cert");
        parser.nextTag();
    }

    private static void readSeinfo(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        parser.require(2, null, "seinfo");
        parser.nextTag();
    }

    private static void skip(org.xmlpull.v1.XmlPullParser p) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (p.getEventType() != 2) {
            throw new java.lang.IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (p.next()) {
                case 2:
                    depth++;
                    break;
                case 3:
                    depth--;
                    break;
            }
        }
    }

    private static int getTargetSdkVersionForSeInfo(com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.SharedUserApi sharedUser, com.android.server.compat.PlatformCompat compatibility) {
        if (sharedUser != null && sharedUser.getPackages().size() != 0) {
            return sharedUser.getSeInfoTargetSdkVersion();
        }
        android.content.pm.ApplicationInfo appInfo = com.android.server.pm.parsing.pkg.AndroidPackageUtils.generateAppInfoWithoutState(pkg);
        if (compatibility.isChangeEnabledInternal(SELINUX_LATEST_CHANGES, appInfo)) {
            return java.lang.Math.max(10000, pkg.getTargetSdkVersion());
        }
        if (compatibility.isChangeEnabledInternal(SELINUX_R_CHANGES, appInfo)) {
            return java.lang.Math.max(30, pkg.getTargetSdkVersion());
        }
        return pkg.getTargetSdkVersion();
    }

    private static java.lang.String getPartition(com.android.server.pm.pkg.PackageState state) {
        if (state.isSystemExt()) {
            return "system_ext";
        }
        if (state.isProduct()) {
            return "product";
        }
        if (state.isVendor()) {
            return "vendor";
        }
        if (state.isOem()) {
            return "oem";
        }
        if (state.isOdm()) {
            return "odm";
        }
        if (state.isSystem()) {
            return "system";
        }
        return "";
    }

    public static java.lang.String getSeInfo(com.android.server.pm.pkg.PackageState packageState, com.android.server.pm.pkg.AndroidPackage pkg, com.android.server.pm.pkg.SharedUserApi sharedUser, com.android.server.compat.PlatformCompat compatibility) {
        int targetSdkVersion = getTargetSdkVersionForSeInfo(pkg, sharedUser, compatibility);
        boolean isPrivileged = sharedUser != null ? sharedUser.isPrivileged() | packageState.isPrivileged() : packageState.isPrivileged();
        return getSeInfo(packageState, pkg, isPrivileged, targetSdkVersion);
    }

    public static java.lang.String getSeInfo(com.android.server.pm.pkg.PackageState packageState, com.android.server.pm.pkg.AndroidPackage pkg, boolean isPrivileged, int targetSdkVersion) {
        java.lang.String seInfo = null;
        synchronized (sPolicies) {
            if (sPolicyRead) {
                for (com.android.server.pm.Policy policy : sPolicies) {
                    seInfo = policy.getMatchedSeInfo(pkg);
                    if (seInfo != null) {
                        break;
                    }
                }
            }
        }
        if (seInfo == null) {
            seInfo = "default";
        }
        if (isPrivileged) {
            seInfo = seInfo + PRIVILEGED_APP_STR;
        }
        java.lang.String seInfo2 = seInfo + TARGETSDKVERSION_STR + targetSdkVersion;
        java.lang.String partition = getPartition(packageState);
        if (!partition.isEmpty()) {
            return seInfo2 + PARTITION_STR + partition;
        }
        return seInfo2;
    }
}
