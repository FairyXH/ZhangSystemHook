package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class UserSystemPackageInstaller {
    private static final boolean DEBUG = false;
    static final java.lang.String PACKAGE_WHITELIST_MODE_PROP = "persist.debug.user.package_whitelist_mode";
    private static final java.lang.String TAG = com.android.server.pm.UserSystemPackageInstaller.class.getSimpleName();
    static final int USER_TYPE_PACKAGE_WHITELIST_MODE_DEVICE_DEFAULT = -1;
    public static final int USER_TYPE_PACKAGE_WHITELIST_MODE_DISABLE = 0;
    public static final int USER_TYPE_PACKAGE_WHITELIST_MODE_ENFORCE = 1;
    public static final int USER_TYPE_PACKAGE_WHITELIST_MODE_IGNORE_OTA = 16;
    public static final int USER_TYPE_PACKAGE_WHITELIST_MODE_IMPLICIT_WHITELIST = 4;
    public static final int USER_TYPE_PACKAGE_WHITELIST_MODE_IMPLICIT_WHITELIST_SYSTEM = 8;
    public static final int USER_TYPE_PACKAGE_WHITELIST_MODE_LOG = 2;
    static final int USER_TYPE_PACKAGE_WHITELIST_MODE_NONE = -1000;
    private final int FLAG_MULTI_SYSTEM = 536870912;
    private final com.android.server.pm.UserManagerService mUm;
    private final java.lang.String[] mUserTypes;
    private final android.util.ArrayMap<java.lang.String, java.lang.Long> mWhitelistedPackagesForUserTypes;

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface PackageWhitelistMode {
    }

    UserSystemPackageInstaller(com.android.server.pm.UserManagerService um, android.util.ArrayMap<java.lang.String, com.android.server.pm.UserTypeDetails> userTypes) {
        this.mUm = um;
        this.mUserTypes = getAndSortKeysFromMap(userTypes);
        if (this.mUserTypes.length > 64) {
            throw new java.lang.IllegalArgumentException("Device contains " + userTypes.size() + " user types. However, UserSystemPackageInstaller does not work if there are more than 64 user types.");
        }
        this.mWhitelistedPackagesForUserTypes = determineWhitelistedPackagesForUserTypes(com.android.server.SystemConfig.getInstance());
    }

    UserSystemPackageInstaller(com.android.server.pm.UserManagerService ums, android.util.ArrayMap<java.lang.String, java.lang.Long> whitelist, java.lang.String[] sortedUserTypes) {
        this.mUm = ums;
        this.mUserTypes = sortedUserTypes;
        this.mWhitelistedPackagesForUserTypes = whitelist;
    }

    boolean installWhitelistedSystemPackages(final boolean isFirstBoot, boolean isUpgrade, final android.util.ArraySet<java.lang.String> preExistingPackages) {
        boolean install;
        int mode = getWhitelistMode();
        checkWhitelistedSystemPackages(mode);
        final boolean isConsideredUpgrade = isUpgrade && !isIgnoreOtaMode(mode);
        if (!isConsideredUpgrade && !isFirstBoot) {
            return false;
        }
        if (isFirstBoot && !isEnforceMode(mode)) {
            return false;
        }
        android.util.Slog.i(TAG, "Reviewing whitelisted packages due to " + (isFirstBoot ? "[firstBoot]" : "") + (isConsideredUpgrade ? "[upgrade]" : ""));
        android.content.pm.PackageManagerInternal pmInt = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        final android.util.SparseArrayMap<java.lang.String, java.lang.Boolean> changesToCommit = new android.util.SparseArrayMap<>();
        for (final int userId : this.mUm.getUserIds()) {
            if (userId == 999) {
                android.util.Slog.i(TAG, "skip install system packages for multi app user when OTA");
            } else if (this.mUm.getUserInfo(userId) != null && (this.mUm.getUserInfo(userId).flags & 536870912) > 0) {
                android.util.Slog.i(TAG, "skip install system packages for multi-system user when OTA");
            } else {
                java.util.Set<java.lang.String> userWhitelist = getInstallablePackagesForUserId(userId);
                if (userWhitelist == null) {
                    pmInt.forEachPackageState(new java.util.function.Consumer() { // from class: com.android.server.pm.UserSystemPackageInstaller$$ExternalSyntheticLambda1
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            com.android.server.pm.UserSystemPackageInstaller.lambda$installWhitelistedSystemPackages$0(userId, isFirstBoot, isConsideredUpgrade, preExistingPackages, changesToCommit, (com.android.server.pm.pkg.PackageStateInternal) obj);
                        }
                    });
                } else {
                    for (java.lang.String packageName : userWhitelist) {
                        com.android.server.pm.pkg.PackageStateInternal packageState = pmInt.getPackageStateInternal(packageName);
                        if (packageState.getPkg() != null && packageState.getUserStateOrDefault(userId).isInstalled() != (!packageState.getTransientState().isHiddenUntilInstalled()) && shouldChangeInstallationState(packageState, install, userId, isFirstBoot, isConsideredUpgrade, preExistingPackages)) {
                            changesToCommit.add(userId, packageState.getPackageName(), java.lang.Boolean.valueOf(install));
                        }
                    }
                }
            }
        }
        pmInt.commitPackageStateMutation(null, new java.util.function.Consumer() { // from class: com.android.server.pm.UserSystemPackageInstaller$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.UserSystemPackageInstaller.lambda$installWhitelistedSystemPackages$1(changesToCommit, (com.android.server.pm.pkg.mutate.PackageStateMutator) obj);
            }
        });
        return true;
    }

    static /* synthetic */ void lambda$installWhitelistedSystemPackages$0(int userId, boolean isFirstBoot, boolean isConsideredUpgrade, android.util.ArraySet preExistingPackages, android.util.SparseArrayMap changesToCommit, com.android.server.pm.pkg.PackageStateInternal packageState) {
        boolean install;
        if (packageState.getPkg() != null && packageState.getUserStateOrDefault(userId).isInstalled() != (!packageState.getTransientState().isHiddenUntilInstalled()) && shouldChangeInstallationState(packageState, install, userId, isFirstBoot, isConsideredUpgrade, preExistingPackages)) {
            changesToCommit.add(userId, packageState.getPackageName(), java.lang.Boolean.valueOf(install));
        }
    }

    static /* synthetic */ void lambda$installWhitelistedSystemPackages$1(android.util.SparseArrayMap changesToCommit, com.android.server.pm.pkg.mutate.PackageStateMutator packageStateMutator) {
        int i;
        for (int userIndex = 0; userIndex < changesToCommit.numMaps(); userIndex++) {
            int userId = changesToCommit.keyAt(userIndex);
            int packagesSize = changesToCommit.numElementsForKey(userId);
            for (int packageIndex = 0; packageIndex < packagesSize; packageIndex++) {
                java.lang.String packageName = (java.lang.String) changesToCommit.keyAt(userIndex, packageIndex);
                boolean installed = ((java.lang.Boolean) changesToCommit.valueAt(userIndex, packageIndex)).booleanValue();
                com.android.server.pm.pkg.mutate.PackageUserStateWrite installed2 = packageStateMutator.forPackage(packageName).userState(userId).setInstalled(installed);
                if (installed) {
                    i = 0;
                } else {
                    i = 1;
                }
                installed2.setUninstallReason(i);
                android.util.Slog.i(TAG + "CommitDebug", (installed ? "Installed " : "Uninstalled ") + packageName + " for user " + userId);
            }
        }
    }

    private static boolean shouldChangeInstallationState(com.android.server.pm.pkg.PackageStateInternal packageState, boolean install, int userId, boolean isFirstBoot, boolean isUpgrade, android.util.ArraySet<java.lang.String> preOtaPkgs) {
        return install ? packageState.getUserStateOrDefault(userId).getUninstallReason() == 1 : isFirstBoot || (isUpgrade && !preOtaPkgs.contains(packageState.getPackageName()));
    }

    private void checkWhitelistedSystemPackages(int mode) {
        if (!isLogMode(mode) && !isEnforceMode(mode)) {
            return;
        }
        android.util.Slog.v(TAG, "Checking that all system packages are whitelisted.");
        java.util.List<java.lang.String> warnings = getPackagesWhitelistWarnings();
        int numberWarnings = warnings.size();
        if (numberWarnings != 0) {
            android.util.Slog.w(TAG, "checkWhitelistedSystemPackages(mode=" + modeToString(mode) + ") has " + numberWarnings + " warnings:");
            for (int i = 0; i < numberWarnings; i++) {
                android.util.Slog.w(TAG, warnings.get(i));
            }
        } else {
            android.util.Slog.v(TAG, "checkWhitelistedSystemPackages(mode=" + modeToString(mode) + ") has no warnings");
        }
        if (isImplicitWhitelistMode(mode) && !isLogMode(mode)) {
            return;
        }
        java.util.List<java.lang.String> errors = getPackagesWhitelistErrors(mode);
        int numberErrors = errors.size();
        if (numberErrors != 0) {
            android.util.Slog.e(TAG, "checkWhitelistedSystemPackages(mode=" + modeToString(mode) + ") has " + numberErrors + " errors:");
            boolean doWtf = !isImplicitWhitelistMode(mode);
            for (int i2 = 0; i2 < numberErrors; i2++) {
                java.lang.String msg = errors.get(i2);
                if (doWtf) {
                    android.util.Slog.wtf(TAG, msg);
                } else {
                    android.util.Slog.e(TAG, msg);
                }
            }
            return;
        }
        android.util.Slog.v(TAG, "checkWhitelistedSystemPackages(mode=" + modeToString(mode) + ") has no errors");
    }

    private java.util.List<java.lang.String> getPackagesWhitelistWarnings() {
        java.util.Set<java.lang.String> allWhitelistedPackages = getWhitelistedSystemPackages();
        java.util.List<java.lang.String> warnings = new java.util.ArrayList<>();
        android.content.pm.PackageManagerInternal pmInt = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        for (java.lang.String pkgName : allWhitelistedPackages) {
            com.android.server.pm.pkg.PackageStateInternal packageState = pmInt.getPackageStateInternal(pkgName);
            com.android.server.pm.pkg.AndroidPackage pkg = packageState == null ? null : packageState.getAndroidPackage();
            if (pkg == null) {
                warnings.add(java.lang.String.format("%s is allowlisted but not present.", pkgName));
            } else if (!packageState.isSystem()) {
                warnings.add(java.lang.String.format("%s is allowlisted and present but not a system package.", pkgName));
            } else if (shouldUseOverlayTargetName(pkg)) {
                warnings.add(java.lang.String.format("%s is allowlisted unnecessarily since it's a static overlay.", pkgName));
            }
        }
        return warnings;
    }

    private java.util.List<java.lang.String> getPackagesWhitelistErrors(int mode) {
        if ((!isEnforceMode(mode) || isImplicitWhitelistMode(mode)) && !isLogMode(mode)) {
            return java.util.Collections.emptyList();
        }
        final java.util.List<java.lang.String> errors = new java.util.ArrayList<>();
        final java.util.Set<java.lang.String> allWhitelistedPackages = getWhitelistedSystemPackages();
        final android.content.pm.PackageManagerInternal pmInt = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        pmInt.forEachPackageState(new java.util.function.Consumer() { // from class: com.android.server.pm.UserSystemPackageInstaller$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.UserSystemPackageInstaller.lambda$getPackagesWhitelistErrors$2(allWhitelistedPackages, pmInt, errors, (com.android.server.pm.pkg.PackageStateInternal) obj);
            }
        });
        return errors;
    }

    static /* synthetic */ void lambda$getPackagesWhitelistErrors$2(java.util.Set allWhitelistedPackages, android.content.pm.PackageManagerInternal pmInt, java.util.List errors, com.android.server.pm.pkg.PackageStateInternal packageState) {
        com.android.server.pm.pkg.AndroidPackage pkg = packageState.getAndroidPackage();
        if (pkg == null || !packageState.isSystem() || pkg.isApex()) {
            return;
        }
        java.lang.String pkgName = pkg.getManifestPackageName();
        if (!allWhitelistedPackages.contains(pkgName) && !shouldUseOverlayTargetName(pmInt.getPackage(pkgName))) {
            errors.add(java.lang.String.format("System package %s is not whitelisted using 'install-in-user-type' in SystemConfig for any user types!", pkgName));
        }
    }

    boolean isEnforceMode() {
        return isEnforceMode(getWhitelistMode());
    }

    boolean isIgnoreOtaMode() {
        return isIgnoreOtaMode(getWhitelistMode());
    }

    boolean isLogMode() {
        return isLogMode(getWhitelistMode());
    }

    boolean isImplicitWhitelistMode() {
        return isImplicitWhitelistMode(getWhitelistMode());
    }

    boolean isImplicitWhitelistSystemMode() {
        return isImplicitWhitelistSystemMode(getWhitelistMode());
    }

    private static boolean shouldUseOverlayTargetName(com.android.server.pm.pkg.AndroidPackage pkg) {
        return pkg.isOverlayIsStatic();
    }

    private static boolean isEnforceMode(int whitelistMode) {
        return (whitelistMode & 1) != 0;
    }

    private static boolean isIgnoreOtaMode(int whitelistMode) {
        return (whitelistMode & 16) != 0;
    }

    private static boolean isLogMode(int whitelistMode) {
        return (whitelistMode & 2) != 0;
    }

    private static boolean isImplicitWhitelistMode(int whitelistMode) {
        return (whitelistMode & 4) != 0;
    }

    private static boolean isImplicitWhitelistSystemMode(int whitelistMode) {
        return (whitelistMode & 8) != 0;
    }

    private int getWhitelistMode() {
        int runtimeMode = android.os.SystemProperties.getInt(PACKAGE_WHITELIST_MODE_PROP, -1);
        if (runtimeMode != -1) {
            return runtimeMode;
        }
        return getDeviceDefaultWhitelistMode();
    }

    private int getDeviceDefaultWhitelistMode() {
        return android.content.res.Resources.getSystem().getInteger(android.R.integer.config_shortPressOnSleepBehavior);
    }

    static java.lang.String modeToString(int mode) {
        switch (mode) {
            case -1000:
                return "NONE";
            case -1:
                return "DEVICE_DEFAULT";
            default:
                return android.util.DebugUtils.flagsToString(com.android.server.pm.UserSystemPackageInstaller.class, "USER_TYPE_PACKAGE_WHITELIST_MODE_", mode);
        }
    }

    private java.util.Set<java.lang.String> getInstallablePackagesForUserId(int userId) {
        return getInstallablePackagesForUserType(this.mUm.getUserInfo(userId).userType);
    }

    java.util.Set<java.lang.String> getInstallablePackagesForUserType(java.lang.String userType) {
        int mode = getWhitelistMode();
        if (!isEnforceMode(mode)) {
            return null;
        }
        final boolean implicitlyWhitelist = isImplicitWhitelistMode(mode) || (isImplicitWhitelistSystemMode(mode) && this.mUm.isUserTypeSubtypeOfSystem(userType));
        final java.util.Set<java.lang.String> whitelistedPackages = getWhitelistedPackagesForUserType(userType);
        final java.util.Set<java.lang.String> installPackages = new android.util.ArraySet<>();
        android.content.pm.PackageManagerInternal pmInt = (android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class);
        pmInt.forEachPackageState(new java.util.function.Consumer() { // from class: com.android.server.pm.UserSystemPackageInstaller$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$getInstallablePackagesForUserType$3(whitelistedPackages, implicitlyWhitelist, installPackages, (com.android.server.pm.pkg.PackageStateInternal) obj);
            }
        });
        return installPackages;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getInstallablePackagesForUserType$3(java.util.Set whitelistedPackages, boolean implicitlyWhitelist, java.util.Set installPackages, com.android.server.pm.pkg.PackageStateInternal packageState) {
        com.android.server.pm.pkg.AndroidPackage pkg = packageState.getAndroidPackage();
        if (pkg != null && packageState.isSystem() && shouldInstallPackage(pkg, this.mWhitelistedPackagesForUserTypes, whitelistedPackages, implicitlyWhitelist)) {
            installPackages.add(pkg.getPackageName());
        }
    }

    static boolean shouldInstallPackage(com.android.server.pm.pkg.AndroidPackage sysPkg, android.util.ArrayMap<java.lang.String, java.lang.Long> userTypeWhitelist, java.util.Set<java.lang.String> userWhitelist, boolean implicitlyWhitelist) {
        java.lang.String pkgName = shouldUseOverlayTargetName(sysPkg) ? sysPkg.getOverlayTarget() : sysPkg.getManifestPackageName();
        return (implicitlyWhitelist && !userTypeWhitelist.containsKey(pkgName)) || userWhitelist.contains(pkgName) || sysPkg.isApex();
    }

    java.util.Set<java.lang.String> getWhitelistedPackagesForUserType(java.lang.String userType) {
        long userTypeMask = getUserTypeMask(userType);
        java.util.Set<java.lang.String> installablePkgs = new android.util.ArraySet<>(this.mWhitelistedPackagesForUserTypes.size());
        for (int i = 0; i < this.mWhitelistedPackagesForUserTypes.size(); i++) {
            java.lang.String pkgName = this.mWhitelistedPackagesForUserTypes.keyAt(i);
            long whitelistedUserTypes = this.mWhitelistedPackagesForUserTypes.valueAt(i).longValue();
            if ((userTypeMask & whitelistedUserTypes) != 0) {
                installablePkgs.add(pkgName);
            }
        }
        return installablePkgs;
    }

    private java.util.Set<java.lang.String> getWhitelistedSystemPackages() {
        return this.mWhitelistedPackagesForUserTypes.keySet();
    }

    android.util.ArrayMap<java.lang.String, java.lang.Long> determineWhitelistedPackagesForUserTypes(com.android.server.SystemConfig sysConfig) {
        java.util.Map<java.lang.String, java.lang.Long> baseTypeBitSets = getBaseTypeBitSets();
        android.util.ArrayMap<java.lang.String, java.util.Set<java.lang.String>> whitelist = sysConfig.getAndClearPackageToUserTypeWhitelist();
        android.util.ArrayMap<java.lang.String, java.lang.Long> result = new android.util.ArrayMap<>(whitelist.size() + 1);
        for (int i = 0; i < whitelist.size(); i++) {
            java.lang.String pkgName = whitelist.keyAt(i).intern();
            long typesBitSet = getTypesBitSet(whitelist.valueAt(i), baseTypeBitSets);
            if (typesBitSet != 0) {
                result.put(pkgName, java.lang.Long.valueOf(typesBitSet));
            }
        }
        android.util.ArrayMap<java.lang.String, java.util.Set<java.lang.String>> blacklist = sysConfig.getAndClearPackageToUserTypeBlacklist();
        for (int i2 = 0; i2 < blacklist.size(); i2++) {
            java.lang.String pkgName2 = blacklist.keyAt(i2).intern();
            long nonTypesBitSet = getTypesBitSet(blacklist.valueAt(i2), baseTypeBitSets);
            java.lang.Long typesBitSet2 = result.get(pkgName2);
            if (typesBitSet2 != null) {
                result.put(pkgName2, java.lang.Long.valueOf(typesBitSet2.longValue() & (~nonTypesBitSet)));
            } else if (nonTypesBitSet != 0) {
                result.put(pkgName2, 0L);
            }
        }
        result.put(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, -1L);
        return result;
    }

    long getUserTypeMask(java.lang.String userType) {
        int userTypeIndex = java.util.Arrays.binarySearch(this.mUserTypes, userType);
        if (userTypeIndex < 0) {
            return 0L;
        }
        long userTypeMask = 1 << userTypeIndex;
        return userTypeMask;
    }

    private java.util.Map<java.lang.String, java.lang.Long> getBaseTypeBitSets() {
        long typesBitSetFull = 0;
        long typesBitSetSystem = 0;
        long typesBitSetProfile = 0;
        for (int idx = 0; idx < this.mUserTypes.length; idx++) {
            if (this.mUm.isUserTypeSubtypeOfFull(this.mUserTypes[idx])) {
                typesBitSetFull |= (long) (1 << idx);
            }
            if (this.mUm.isUserTypeSubtypeOfSystem(this.mUserTypes[idx])) {
                typesBitSetSystem |= (long) (1 << idx);
            }
            if (this.mUm.isUserTypeSubtypeOfProfile(this.mUserTypes[idx])) {
                typesBitSetProfile |= (long) (1 << idx);
            }
        }
        java.util.Map<java.lang.String, java.lang.Long> result = new android.util.ArrayMap<>(3);
        result.put("FULL", java.lang.Long.valueOf(typesBitSetFull));
        result.put("SYSTEM", java.lang.Long.valueOf(typesBitSetSystem));
        result.put("PROFILE", java.lang.Long.valueOf(typesBitSetProfile));
        return result;
    }

    private long getTypesBitSet(java.lang.Iterable<java.lang.String> userTypes, java.util.Map<java.lang.String, java.lang.Long> baseTypeBitSets) {
        long resultBitSet = 0;
        for (java.lang.String type : userTypes) {
            java.lang.Long baseTypeBitSet = baseTypeBitSets.get(type);
            if (baseTypeBitSet != null) {
                resultBitSet |= baseTypeBitSet.longValue();
            } else {
                long userTypeBitSet = getUserTypeMask(type);
                if (userTypeBitSet != 0) {
                    resultBitSet |= userTypeBitSet;
                } else {
                    android.util.Slog.w(TAG, "SystemConfig contained an invalid user type: " + type);
                }
            }
        }
        return resultBitSet;
    }

    private static java.lang.String[] getAndSortKeysFromMap(android.util.ArrayMap<java.lang.String, ?> map) {
        java.lang.String[] userTypeList = new java.lang.String[map.size()];
        for (int i = 0; i < map.size(); i++) {
            userTypeList[i] = map.keyAt(i);
        }
        java.util.Arrays.sort(userTypeList);
        return userTypeList;
    }

    void dump(android.util.IndentingPrintWriter pw) {
        int mode = getWhitelistMode();
        pw.println("Whitelisted packages per user type");
        pw.increaseIndent();
        pw.print("Mode: ");
        pw.print(mode);
        pw.print(isEnforceMode(mode) ? " (enforced)" : "");
        pw.print(isLogMode(mode) ? " (logged)" : "");
        pw.print(isImplicitWhitelistMode(mode) ? " (implicit)" : "");
        pw.print(isIgnoreOtaMode(mode) ? " (ignore OTAs)" : "");
        pw.println();
        pw.decreaseIndent();
        pw.increaseIndent();
        pw.println("Legend");
        pw.increaseIndent();
        for (int idx = 0; idx < this.mUserTypes.length; idx++) {
            pw.println(idx + " -> " + this.mUserTypes[idx]);
        }
        pw.decreaseIndent();
        pw.decreaseIndent();
        pw.increaseIndent();
        int size = this.mWhitelistedPackagesForUserTypes.size();
        if (size == 0) {
            pw.println("No packages");
            pw.decreaseIndent();
            return;
        }
        pw.print(size);
        pw.println(" packages:");
        pw.increaseIndent();
        for (int pkgIdx = 0; pkgIdx < size; pkgIdx++) {
            java.lang.String pkgName = this.mWhitelistedPackagesForUserTypes.keyAt(pkgIdx);
            pw.print(pkgName);
            pw.print(": ");
            long userTypesBitSet = this.mWhitelistedPackagesForUserTypes.valueAt(pkgIdx).longValue();
            for (int idx2 = 0; idx2 < this.mUserTypes.length; idx2++) {
                if ((((long) (1 << idx2)) & userTypesBitSet) != 0) {
                    pw.print(idx2);
                    pw.print(" ");
                }
            }
            pw.println();
        }
        pw.decreaseIndent();
        pw.decreaseIndent();
        pw.increaseIndent();
        dumpPackageWhitelistProblems(pw, mode, true, false);
        pw.decreaseIndent();
    }

    void dumpPackageWhitelistProblems(android.util.IndentingPrintWriter pw, int mode, boolean verbose, boolean criticalOnly) {
        if (mode == -1000) {
            mode = getWhitelistMode();
        } else if (mode == -1) {
            mode = getDeviceDefaultWhitelistMode();
        }
        if (criticalOnly) {
            mode &= -3;
        }
        android.util.Slog.v(TAG, "dumpPackageWhitelistProblems(): using mode " + modeToString(mode));
        java.util.List<java.lang.String> errors = getPackagesWhitelistErrors(mode);
        showIssues(pw, verbose, errors, "errors");
        if (criticalOnly) {
            return;
        }
        java.util.List<java.lang.String> warnings = getPackagesWhitelistWarnings();
        showIssues(pw, verbose, warnings, "warnings");
    }

    private static void showIssues(android.util.IndentingPrintWriter pw, boolean verbose, java.util.List<java.lang.String> issues, java.lang.String issueType) {
        int size = issues.size();
        if (size == 0) {
            if (verbose) {
                pw.print("No ");
                pw.println(issueType);
                return;
            }
            return;
        }
        if (verbose) {
            pw.print(size);
            pw.print(' ');
            pw.println(issueType);
            pw.increaseIndent();
        }
        for (int i = 0; i < size; i++) {
            pw.println(issues.get(i));
        }
        if (verbose) {
            pw.decreaseIndent();
        }
    }
}
