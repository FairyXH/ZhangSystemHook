package com.android.server.pm.verify.domain;

/* JADX INFO: loaded from: classes2.dex */
public class DomainVerificationDebug {
    public static final boolean DEBUG_ALL = false;
    public static final boolean DEBUG_ANY = false;
    public static final boolean DEBUG_APPROVAL = false;
    public static final boolean DEBUG_BROADCASTS = false;
    public static final boolean DEBUG_PROXIES = false;
    private final com.android.server.pm.verify.domain.DomainVerificationCollector mCollector;

    DomainVerificationDebug(com.android.server.pm.verify.domain.DomainVerificationCollector collector) {
        this.mCollector = collector;
    }

    public void printState(android.util.IndentingPrintWriter writer, java.lang.String packageName, java.lang.Integer userId, com.android.server.pm.Computer snapshot, com.android.server.pm.verify.domain.models.DomainVerificationStateMap<com.android.server.pm.verify.domain.models.DomainVerificationPkgState> stateMap) throws android.content.pm.PackageManager.NameNotFoundException {
        int index;
        int size;
        android.util.ArrayMap<java.lang.String, java.lang.Integer> reusedMap = new android.util.ArrayMap<>();
        android.util.ArraySet<java.lang.String> reusedSet = new android.util.ArraySet<>();
        if (packageName == null) {
            int size2 = stateMap.size();
            int index2 = 0;
            while (index2 < size2) {
                com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = stateMap.valueAt(index2);
                java.lang.String pkgName = pkgState.getPackageName();
                com.android.server.pm.pkg.PackageStateInternal pkgSetting = snapshot.getPackageStateInternal(pkgName);
                if (pkgSetting == null) {
                    index = index2;
                    size = size2;
                } else if (pkgSetting.getPkg() == null) {
                    index = index2;
                    size = size2;
                } else {
                    boolean wasHeaderPrinted = printState(writer, pkgState, (com.android.server.pm.pkg.AndroidPackage) pkgSetting.getPkg(), reusedMap, false);
                    index = index2;
                    size = size2;
                    printState(writer, pkgState, pkgSetting.getPkg(), userId, reusedSet, wasHeaderPrinted);
                }
                index2 = index + 1;
                size2 = size;
            }
            return;
        }
        com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState2 = stateMap.get(packageName);
        if (pkgState2 == null) {
            throw com.android.server.pm.verify.domain.DomainVerificationUtils.throwPackageUnavailable(packageName);
        }
        com.android.server.pm.pkg.PackageStateInternal pkgSetting2 = snapshot.getPackageStateInternal(packageName);
        if (pkgSetting2 == null || pkgSetting2.getPkg() == null) {
            throw com.android.server.pm.verify.domain.DomainVerificationUtils.throwPackageUnavailable(packageName);
        }
        com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = pkgSetting2.getPkg();
        printState(writer, pkgState2, (com.android.server.pm.pkg.AndroidPackage) pkg, reusedMap, false);
        printState(writer, pkgState2, pkg, userId, reusedSet, true);
    }

    public void printOwners(android.util.IndentingPrintWriter writer, java.lang.String domain, android.util.SparseArray<android.util.SparseArray<java.util.List<java.lang.String>>> userIdToApprovalLevelToOwners) {
        android.util.SparseArray<android.util.SparseArray<java.util.List<java.lang.String>>> sparseArray = userIdToApprovalLevelToOwners;
        writer.println(domain + ":");
        writer.increaseIndent();
        if (userIdToApprovalLevelToOwners.size() == 0) {
            writer.println("none");
            writer.decreaseIndent();
            return;
        }
        int usersSize = userIdToApprovalLevelToOwners.size();
        int userIndex = 0;
        while (userIndex < usersSize) {
            int userId = sparseArray.keyAt(userIndex);
            android.util.SparseArray<java.util.List<java.lang.String>> approvalLevelToOwners = sparseArray.valueAt(userIndex);
            if (approvalLevelToOwners.size() != 0) {
                boolean printedUserHeader = false;
                int approvalsSize = approvalLevelToOwners.size();
                for (int approvalIndex = 0; approvalIndex < approvalsSize; approvalIndex++) {
                    int approvalLevel = approvalLevelToOwners.keyAt(approvalIndex);
                    if (approvalLevel >= -1) {
                        if (!printedUserHeader) {
                            writer.println("User " + userId + ":");
                            writer.increaseIndent();
                            printedUserHeader = true;
                        }
                        java.lang.String approvalString = com.android.server.pm.verify.domain.DomainVerificationManagerInternal.approvalLevelToDebugString(approvalLevel);
                        java.util.List<java.lang.String> owners = approvalLevelToOwners.valueAt(approvalIndex);
                        writer.println(approvalString + "[" + approvalLevel + "]:");
                        writer.increaseIndent();
                        if (owners.size() == 0) {
                            writer.println("none");
                            writer.decreaseIndent();
                        } else {
                            int ownersIndex = 0;
                            for (int ownersSize = owners.size(); ownersIndex < ownersSize; ownersSize = ownersSize) {
                                writer.println(owners.get(ownersIndex));
                                ownersIndex++;
                            }
                            writer.decreaseIndent();
                        }
                    }
                }
                if (printedUserHeader) {
                    writer.decreaseIndent();
                }
            }
            userIndex++;
            sparseArray = userIdToApprovalLevelToOwners;
        }
        writer.decreaseIndent();
    }

    boolean printState(android.util.IndentingPrintWriter writer, com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState, com.android.server.pm.pkg.AndroidPackage pkg, android.util.ArrayMap<java.lang.String, java.lang.Integer> reusedMap, boolean wasHeaderPrinted) {
        reusedMap.clear();
        reusedMap.putAll((android.util.ArrayMap<? extends java.lang.String, ? extends java.lang.Integer>) pkgState.getStateMap());
        android.util.ArraySet<java.lang.String> declaredDomains = this.mCollector.collectValidAutoVerifyDomains(pkg);
        int declaredSize = declaredDomains.size();
        for (int declaredIndex = 0; declaredIndex < declaredSize; declaredIndex++) {
            java.lang.String domain = declaredDomains.valueAt(declaredIndex);
            reusedMap.putIfAbsent(domain, 0);
        }
        boolean printedHeader = false;
        if (!reusedMap.isEmpty()) {
            if (!wasHeaderPrinted) {
                android.content.pm.Signature[] signatures = pkg.getSigningDetails().getSignatures();
                java.lang.String signaturesDigest = signatures == null ? null : java.util.Arrays.toString(android.util.PackageUtils.computeSignaturesSha256Digests(pkg.getSigningDetails().getSignatures(), ":"));
                writer.println(pkgState.getPackageName() + ":");
                writer.increaseIndent();
                writer.println("ID: " + pkgState.getId());
                writer.println("Signatures: " + signaturesDigest);
                writer.decreaseIndent();
                printedHeader = true;
            }
            writer.increaseIndent();
            android.util.ArraySet<java.lang.String> invalidDomains = this.mCollector.collectInvalidAutoVerifyDomains(pkg);
            if (!invalidDomains.isEmpty()) {
                writer.println("Invalid autoVerify domains:");
                writer.increaseIndent();
                int size = invalidDomains.size();
                for (int index = 0; index < size; index++) {
                    writer.println(invalidDomains.valueAt(index));
                }
                writer.decreaseIndent();
            }
            writer.println("Domain verification state:");
            writer.increaseIndent();
            int stateSize = reusedMap.size();
            for (int stateIndex = 0; stateIndex < stateSize; stateIndex++) {
                java.lang.String domain2 = reusedMap.keyAt(stateIndex);
                java.lang.Integer state = reusedMap.valueAt(stateIndex);
                writer.print(domain2);
                writer.print(": ");
                writer.println(android.content.pm.verify.domain.DomainVerificationState.stateToDebugString(state.intValue()));
            }
            writer.decreaseIndent();
            writer.decreaseIndent();
        }
        return printedHeader;
    }

    void printState(android.util.IndentingPrintWriter writer, com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState, com.android.server.pm.pkg.AndroidPackage pkg, java.lang.Integer userId, android.util.ArraySet<java.lang.String> reusedSet, boolean wasHeaderPrinted) {
        if (userId == null) {
            return;
        }
        android.util.ArraySet<java.lang.String> allWebDomains = this.mCollector.collectAllWebDomains(pkg);
        android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> userStates = pkgState.getUserStates();
        if (userId.intValue() == -1) {
            int size = userStates.size();
            if (size == 0) {
                printState(writer, pkgState, userId.intValue(), null, reusedSet, allWebDomains, wasHeaderPrinted);
                return;
            }
            for (int index = 0; index < size; index++) {
                com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState userState = userStates.valueAt(index);
                printState(writer, pkgState, userState.getUserId(), userState, reusedSet, allWebDomains, wasHeaderPrinted);
            }
            return;
        }
        printState(writer, pkgState, userId.intValue(), userStates.get(userId.intValue()), reusedSet, allWebDomains, wasHeaderPrinted);
    }

    boolean printState(android.util.IndentingPrintWriter writer, com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState, int userId, com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState userState, android.util.ArraySet<java.lang.String> reusedSet, android.util.ArraySet<java.lang.String> allWebDomains, boolean wasHeaderPrinted) {
        reusedSet.clear();
        reusedSet.addAll((android.util.ArraySet<? extends java.lang.String>) allWebDomains);
        if (userState != null) {
            reusedSet.removeAll((android.util.ArraySet<? extends java.lang.String>) userState.getEnabledHosts());
        }
        boolean printedHeader = false;
        android.util.ArraySet<java.lang.String> enabledHosts = userState == null ? null : userState.getEnabledHosts();
        int enabledSize = com.android.internal.util.CollectionUtils.size(enabledHosts);
        int disabledSize = reusedSet.size();
        if (enabledSize > 0 || disabledSize > 0) {
            if (!wasHeaderPrinted) {
                writer.println(pkgState.getPackageName() + " " + pkgState.getId() + ":");
                printedHeader = true;
            }
            boolean isLinkHandlingAllowed = userState == null || userState.isLinkHandlingAllowed();
            writer.increaseIndent();
            writer.print("User ");
            writer.print(userId == -1 ? "all" : java.lang.Integer.valueOf(userId));
            writer.println(":");
            writer.increaseIndent();
            writer.print("Verification link handling allowed: ");
            writer.println(isLinkHandlingAllowed);
            writer.println("Selection state:");
            writer.increaseIndent();
            if (enabledSize > 0) {
                writer.println("Enabled:");
                writer.increaseIndent();
                for (int enabledIndex = 0; enabledIndex < enabledSize; enabledIndex++) {
                    writer.println(enabledHosts.valueAt(enabledIndex));
                }
                writer.decreaseIndent();
            }
            if (disabledSize > 0) {
                writer.println("Disabled:");
                writer.increaseIndent();
                for (int disabledIndex = 0; disabledIndex < disabledSize; disabledIndex++) {
                    writer.println(reusedSet.valueAt(disabledIndex));
                }
                writer.decreaseIndent();
            }
            writer.decreaseIndent();
            writer.decreaseIndent();
            writer.decreaseIndent();
        }
        return printedHeader;
    }
}
