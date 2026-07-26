package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageManagerServiceCompilerMapping {
    static int REASON_SHARED_INDEX;
    public static java.lang.String[] REASON_STRINGS;
    private static com.android.server.pm.IPackageManagerServiceCompilerMappingExt mPmsCMExt = (com.android.server.pm.IPackageManagerServiceCompilerMappingExt) system.ext.loader.core.ExtLoader.type(com.android.server.pm.IPackageManagerServiceCompilerMappingExt.class).create();

    static {
        REASON_STRINGS = new java.lang.String[]{"first-boot", "boot-after-ota", "post-boot", "install", "install-fast", "install-bulk", "install-bulk-secondary", "install-bulk-downgraded", "install-bulk-secondary-downgraded", "bg-dexopt", "ab-ota", "inactive", "cmdline", "boot-after-mainline-update", "shared"};
        REASON_SHARED_INDEX = REASON_STRINGS.length - 1;
        REASON_STRINGS = mPmsCMExt.modifyReasonList(REASON_STRINGS);
        REASON_SHARED_INDEX = REASON_STRINGS.length - 1;
        if (com.android.server.pm.PackageManagerService.REASON_LAST + 1 != REASON_STRINGS.length) {
            throw new java.lang.IllegalStateException("REASON_STRINGS not correct");
        }
        if (!"shared".equals(REASON_STRINGS[REASON_SHARED_INDEX])) {
            throw new java.lang.IllegalStateException("REASON_STRINGS not correct because of shared index");
        }
    }

    private static java.lang.String getSystemPropertyName(int reason) {
        if (reason < 0 || reason >= REASON_STRINGS.length) {
            throw new java.lang.IllegalArgumentException("reason " + reason + " invalid");
        }
        return "pm.dexopt." + REASON_STRINGS[reason];
    }

    private static java.lang.String getAndCheckValidity(int reason) {
        if (mPmsCMExt.getAndCheckValidityForOplus(reason)) {
            return "speed-profile";
        }
        java.lang.String sysPropValue = android.os.SystemProperties.get(getSystemPropertyName(reason));
        if (sysPropValue == null || sysPropValue.isEmpty() || (!sysPropValue.equals("skip") && !dalvik.system.DexFile.isValidCompilerFilter(sysPropValue))) {
            throw new java.lang.IllegalStateException("Value \"" + sysPropValue + "\" not valid (reason " + REASON_STRINGS[reason] + ")");
        }
        if (!isFilterAllowedForReason(reason, sysPropValue)) {
            throw new java.lang.IllegalStateException("Value \"" + sysPropValue + "\" not allowed (reason " + REASON_STRINGS[reason] + ")");
        }
        return sysPropValue;
    }

    private static boolean isFilterAllowedForReason(int reason, java.lang.String filter) {
        return (reason == REASON_SHARED_INDEX && dalvik.system.DexFile.isProfileGuidedCompilerFilter(filter)) ? false : true;
    }

    static void checkProperties() {
        java.lang.RuntimeException toThrow = null;
        for (int reason = 0; reason <= com.android.server.pm.PackageManagerService.REASON_LAST; reason++) {
            if (!mPmsCMExt.checkPropertiesForOplus(reason)) {
                try {
                    java.lang.String sysPropName = getSystemPropertyName(reason);
                    if (sysPropName == null || sysPropName.isEmpty()) {
                        throw new java.lang.IllegalStateException("Reason system property name \"" + sysPropName + "\" for reason " + REASON_STRINGS[reason]);
                    }
                    getAndCheckValidity(reason);
                } catch (java.lang.Exception exc) {
                    if (toThrow == null) {
                        toThrow = new java.lang.IllegalStateException("PMS compiler filter settings are bad.");
                    }
                    toThrow.addSuppressed(exc);
                }
            }
        }
        if (toThrow != null) {
            throw toThrow;
        }
    }

    public static java.lang.String getCompilerFilterForReason(int reason) {
        return getAndCheckValidity(reason);
    }

    public static java.lang.String getDefaultCompilerFilter() {
        java.lang.String value = android.os.SystemProperties.get("dalvik.vm.dex2oat-filter");
        if (value == null || value.isEmpty() || !dalvik.system.DexFile.isValidCompilerFilter(value) || dalvik.system.DexFile.isProfileGuidedCompilerFilter(value)) {
            return "speed";
        }
        return value;
    }

    public static java.lang.String getReasonName(int reason) {
        if (reason < 0 || reason >= REASON_STRINGS.length) {
            throw new java.lang.IllegalArgumentException("reason " + reason + " invalid");
        }
        return REASON_STRINGS[reason];
    }
}
