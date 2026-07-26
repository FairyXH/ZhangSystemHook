package com.android.server.pm.verify.domain;

/* JADX INFO: loaded from: classes2.dex */
public final class DomainVerificationUtils {
    public static final int MAX_DOMAIN_LABEL_LENGTH = 63;
    public static final int MAX_DOMAIN_LENGTH = 254;
    private static final java.lang.ThreadLocal<java.util.regex.Matcher> sCachedMatcher = java.lang.ThreadLocal.withInitial(new java.util.function.Supplier() { // from class: com.android.server.pm.verify.domain.DomainVerificationUtils$$ExternalSyntheticLambda0
        @Override // java.util.function.Supplier
        public final java.lang.Object get() {
            return android.util.Patterns.DOMAIN_NAME.matcher("");
        }
    });

    static android.content.pm.PackageManager.NameNotFoundException throwPackageUnavailable(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new android.content.pm.PackageManager.NameNotFoundException("Package " + packageName + " unavailable");
    }

    public static boolean isDomainVerificationIntent(android.content.Intent intent, long resolveInfoFlags) {
        if (!intent.isWebIntent()) {
            return false;
        }
        java.lang.String host = intent.getData().getHost();
        if (android.text.TextUtils.isEmpty(host) || !sCachedMatcher.get().reset(host).matches()) {
            return false;
        }
        java.util.Set<java.lang.String> categories = intent.getCategories();
        int categoriesSize = com.android.internal.util.CollectionUtils.size(categories);
        if (categoriesSize > 2) {
            return false;
        }
        if (categoriesSize == 2) {
            return intent.hasCategory("android.intent.category.DEFAULT") && intent.hasCategory("android.intent.category.BROWSABLE");
        }
        boolean matchDefaultByFlags = (65536 & resolveInfoFlags) != 0;
        if (categoriesSize == 0 || intent.hasCategory("android.intent.category.BROWSABLE")) {
            return matchDefaultByFlags;
        }
        return intent.hasCategory("android.intent.category.DEFAULT");
    }

    static boolean isChangeEnabled(com.android.server.compat.PlatformCompat platformCompat, com.android.server.pm.pkg.AndroidPackage pkg, long changeId) {
        return platformCompat.isChangeEnabledInternalNoLogging(changeId, buildMockAppInfo(pkg));
    }

    private static android.content.pm.ApplicationInfo buildMockAppInfo(com.android.server.pm.pkg.AndroidPackage pkg) {
        android.content.pm.ApplicationInfo appInfo = new android.content.pm.ApplicationInfo();
        appInfo.packageName = pkg.getPackageName();
        appInfo.targetSdkVersion = pkg.getTargetSdkVersion();
        return appInfo;
    }

    static boolean isValidDomain(java.lang.String domain) {
        if (domain.length() > 254 || domain.equals(com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER)) {
            return false;
        }
        if (domain.charAt(0) == '*') {
            if (domain.charAt(1) != '.') {
                return false;
            }
            domain = domain.substring(2);
        }
        int labels = 1;
        int labelStart = -1;
        for (int i = 0; i < domain.length(); i++) {
            char c = domain.charAt(i);
            if (c == '.') {
                int labelLength = (i - labelStart) - 1;
                if (labelLength == 0 || labelLength > 63) {
                    return false;
                }
                labelStart = i;
                labels++;
            } else if (!isValidDomainChar(c)) {
                return false;
            }
        }
        int lastLabelLength = (domain.length() - labelStart) - 1;
        return lastLabelLength != 0 && lastLabelLength <= 63 && labels > 1;
    }

    private static boolean isValidDomainChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || ((c >= '0' && c <= '9') || c == '-');
    }
}
