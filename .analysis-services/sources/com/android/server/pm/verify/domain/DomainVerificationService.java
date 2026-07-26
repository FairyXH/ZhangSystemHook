package com.android.server.pm.verify.domain;

/* JADX INFO: loaded from: classes2.dex */
public class DomainVerificationService extends com.android.server.SystemService implements com.android.server.pm.verify.domain.DomainVerificationManagerInternal, com.android.server.pm.verify.domain.DomainVerificationShell.Callback {
    public static final boolean DEBUG_APPROVAL = false;
    private static final long SETTINGS_API_V2 = 178111421;
    private static final java.lang.String TAG = "DomainVerificationService";
    private final com.android.server.pm.verify.domain.models.DomainVerificationStateMap<com.android.server.pm.verify.domain.models.DomainVerificationPkgState> mAttachedPkgStates;
    private boolean mCanSendBroadcasts;
    private final com.android.server.pm.verify.domain.DomainVerificationCollector mCollector;
    private com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection mConnection;
    private final com.android.server.pm.verify.domain.DomainVerificationDebug mDebug;
    private final com.android.server.pm.verify.domain.DomainVerificationEnforcer mEnforcer;
    private final com.android.server.pm.verify.domain.DomainVerificationLegacySettings mLegacySettings;
    private final java.lang.Object mLock;
    private final com.android.server.compat.PlatformCompat mPlatformCompat;
    private com.android.server.pm.verify.domain.proxy.DomainVerificationProxy mProxy;
    private final com.android.server.pm.verify.domain.DomainVerificationSettings mSettings;
    private final com.android.server.pm.verify.domain.DomainVerificationShell mShell;
    private final android.content.pm.verify.domain.IDomainVerificationManager.Stub mStub;
    private final com.android.server.SystemConfig mSystemConfig;

    public DomainVerificationService(android.content.Context context, com.android.server.SystemConfig systemConfig, com.android.server.compat.PlatformCompat platformCompat) {
        super(context);
        this.mAttachedPkgStates = new com.android.server.pm.verify.domain.models.DomainVerificationStateMap<>();
        this.mLock = new java.lang.Object();
        this.mStub = new com.android.server.pm.verify.domain.DomainVerificationManagerStub(this);
        this.mProxy = new com.android.server.pm.verify.domain.proxy.DomainVerificationProxyUnavailable();
        this.mSystemConfig = systemConfig;
        this.mPlatformCompat = platformCompat;
        this.mCollector = new com.android.server.pm.verify.domain.DomainVerificationCollector(platformCompat, systemConfig);
        this.mSettings = new com.android.server.pm.verify.domain.DomainVerificationSettings(this.mCollector);
        this.mEnforcer = new com.android.server.pm.verify.domain.DomainVerificationEnforcer(context);
        this.mDebug = new com.android.server.pm.verify.domain.DomainVerificationDebug(this.mCollector);
        this.mShell = new com.android.server.pm.verify.domain.DomainVerificationShell(this);
        this.mLegacySettings = new com.android.server.pm.verify.domain.DomainVerificationLegacySettings();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("domain_verification", this.mStub);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public void setConnection(com.android.server.pm.verify.domain.DomainVerificationManagerInternal.Connection connection) {
        this.mConnection = connection;
        this.mEnforcer.setCallback(this.mConnection);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public com.android.server.pm.verify.domain.proxy.DomainVerificationProxy getProxy() {
        return this.mProxy;
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        super.onBootPhase(phase);
        if (!hasRealVerifier()) {
        }
        switch (phase) {
            case 550:
                this.mCanSendBroadcasts = true;
                break;
            case 1000:
                verifyPackages(null, false);
                break;
        }
    }

    @Override // com.android.server.SystemService
    public void onUserUnlocked(com.android.server.SystemService.TargetUser user) {
        super.onUserUnlocked(user);
        verifyPackages(null, false);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public void setProxy(com.android.server.pm.verify.domain.proxy.DomainVerificationProxy proxy) {
        this.mProxy = proxy;
    }

    public void setUriRelativeFilterGroups(java.lang.String packageName, android.os.Bundle bundle) throws android.content.pm.PackageManager.NameNotFoundException {
        getContext().enforceCallingOrSelfPermission("android.permission.DOMAIN_VERIFICATION_AGENT", "Caller " + this.mConnection.getCallingUid() + " does not hold android.permission.DOMAIN_VERIFICATION_AGENT");
        if (bundle.isEmpty()) {
            return;
        }
        synchronized (this.mLock) {
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.get(packageName);
            if (pkgState == null) {
                throw com.android.server.pm.verify.domain.DomainVerificationUtils.throwPackageUnavailable(packageName);
            }
            java.util.Map<java.lang.String, java.util.List<android.content.UriRelativeFilterGroup>> domainToGroupsMap = pkgState.getUriRelativeFilterGroupMap();
            for (java.lang.String domain : bundle.keySet()) {
                if (com.android.server.pm.verify.domain.DomainVerificationUtils.isValidDomain(domain)) {
                    java.util.ArrayList<android.content.UriRelativeFilterGroupParcel> parcels = bundle.getParcelableArrayList(domain, android.content.UriRelativeFilterGroupParcel.class);
                    java.util.List<android.content.UriRelativeFilterGroup> groups = android.content.UriRelativeFilterGroup.parcelsToGroups(parcels);
                    if (groups == null || groups.isEmpty()) {
                        domainToGroupsMap.remove(domain);
                    } else {
                        domainToGroupsMap.put(domain, groups);
                    }
                }
            }
        }
    }

    public android.os.Bundle getUriRelativeFilterGroups(java.lang.String packageName, java.util.List<java.lang.String> domains) {
        android.os.Bundle bundle = new android.os.Bundle();
        synchronized (this.mLock) {
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.get(packageName);
            if (pkgState != null) {
                java.util.Map<java.lang.String, java.util.List<android.content.UriRelativeFilterGroup>> map = pkgState.getUriRelativeFilterGroupMap();
                for (int i = 0; i < domains.size(); i++) {
                    if (map.containsKey(domains.get(i))) {
                        java.util.List<android.content.UriRelativeFilterGroup> groups = map.get(domains.get(i));
                        bundle.putParcelableList(domains.get(i), android.content.UriRelativeFilterGroup.groupsToParcels(groups));
                    }
                }
            }
        }
        return bundle;
    }

    private java.util.List<android.content.UriRelativeFilterGroup> getUriRelativeFilterGroups(java.lang.String packageName, java.lang.String domain) {
        synchronized (this.mLock) {
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.get(packageName);
            if (pkgState != null) {
                java.util.Map<java.lang.String, java.util.List<android.content.UriRelativeFilterGroup>> groupMap = pkgState.getUriRelativeFilterGroupMap();
                java.util.List<android.content.UriRelativeFilterGroup> groups = groupMap.get(domain);
                if (groups != null) {
                    return groups;
                }
                int first = domain.indexOf(".");
                int second = domain.indexOf(46, first + 1);
                while (first > 0 && second > 0) {
                    java.util.List<android.content.UriRelativeFilterGroup> groups2 = groupMap.get(com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER + domain.substring(first));
                    if (groups2 != null) {
                        return groups2;
                    }
                    first = second;
                    second = domain.indexOf(46, second + 1);
                }
            }
            return java.util.Collections.emptyList();
        }
    }

    public java.util.List<java.lang.String> queryValidVerificationPackageNames() {
        this.mEnforcer.assertApprovedVerifier(this.mConnection.getCallingUid(), this.mProxy);
        java.util.List<java.lang.String> packageNames = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            int size = this.mAttachedPkgStates.size();
            for (int index = 0; index < size; index++) {
                com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.valueAt(index);
                if (pkgState.isHasAutoVerifyDomains()) {
                    packageNames.add(pkgState.getPackageName());
                }
            }
        }
        return packageNames;
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public java.util.UUID getDomainVerificationInfoId(java.lang.String packageName) {
        synchronized (this.mLock) {
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.get(packageName);
            if (pkgState == null) {
                return null;
            }
            return pkgState.getId();
        }
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public android.content.pm.verify.domain.DomainVerificationInfo getDomainVerificationInfo(java.lang.String packageName) throws android.content.pm.PackageManager.NameNotFoundException {
        this.mEnforcer.assertApprovedQuerent(this.mConnection.getCallingUid(), this.mProxy);
        com.android.server.pm.Computer snapshot = this.mConnection.snapshot();
        synchronized (this.mLock) {
            com.android.server.pm.pkg.PackageStateInternal pkgSetting = snapshot.getPackageStateInternal(packageName);
            com.android.server.pm.pkg.AndroidPackage pkg = pkgSetting == null ? null : pkgSetting.getPkg();
            if (pkg == null) {
                throw com.android.server.pm.verify.domain.DomainVerificationUtils.throwPackageUnavailable(packageName);
            }
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.get(packageName);
            if (pkgState == null) {
                return null;
            }
            android.util.ArrayMap<java.lang.String, java.lang.Integer> hostToStateMap = new android.util.ArrayMap<>(pkgState.getStateMap());
            android.util.ArraySet<java.lang.String> domains = this.mCollector.collectValidAutoVerifyDomains(pkg);
            if (domains.isEmpty()) {
                return null;
            }
            int size = domains.size();
            for (int index = 0; index < size; index++) {
                hostToStateMap.putIfAbsent(domains.valueAt(index), 0);
            }
            int mapSize = hostToStateMap.size();
            for (int index2 = 0; index2 < mapSize; index2++) {
                int internalValue = hostToStateMap.valueAt(index2).intValue();
                int publicValue = android.content.pm.verify.domain.DomainVerificationState.convertToInfoState(internalValue);
                hostToStateMap.setValueAt(index2, java.lang.Integer.valueOf(publicValue));
            }
            return new android.content.pm.verify.domain.DomainVerificationInfo(pkgState.getId(), packageName, hostToStateMap);
        }
    }

    public int setDomainVerificationStatus(java.util.UUID domainSetId, java.util.Set<java.lang.String> domains, int state) throws android.content.pm.PackageManager.NameNotFoundException {
        if (state < 1024 && state != 1) {
            throw new java.lang.IllegalArgumentException("Caller is not allowed to set state code " + state);
        }
        return setDomainVerificationStatusInternal(this.mConnection.getCallingUid(), domainSetId, domains, state);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public int setDomainVerificationStatusInternal(int callingUid, java.util.UUID domainSetId, java.util.Set<java.lang.String> domains, int state) throws android.content.pm.PackageManager.NameNotFoundException {
        this.mEnforcer.assertApprovedVerifier(callingUid, this.mProxy);
        com.android.server.pm.Computer snapshot = this.mConnection.snapshot();
        synchronized (this.mLock) {
            java.util.List<java.lang.String> newlyVerifiedDomains = new java.util.ArrayList<>();
            com.android.server.pm.verify.domain.DomainVerificationService.GetAttachedResult result = getAndValidateAttachedLocked(domainSetId, domains, true, callingUid, null, snapshot);
            if (result.isError()) {
                return result.getErrorCode();
            }
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = result.getPkgState();
            android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap = pkgState.getStateMap();
            for (java.lang.String domain : domains) {
                java.lang.Integer previousState = stateMap.get(domain);
                if (previousState == null || (previousState.intValue() != state && android.content.pm.verify.domain.DomainVerificationState.isModifiable(previousState.intValue()))) {
                    if (android.content.pm.verify.domain.DomainVerificationState.isVerified(state) && (previousState == null || !android.content.pm.verify.domain.DomainVerificationState.isVerified(previousState.intValue()))) {
                        newlyVerifiedDomains.add(domain);
                    }
                    stateMap.put(domain, java.lang.Integer.valueOf(state));
                }
            }
            int size = newlyVerifiedDomains.size();
            for (int index = 0; index < size; index++) {
                removeUserStatesForDomain(pkgState, newlyVerifiedDomains.get(index));
            }
            this.mConnection.scheduleWriteSettings();
            return 0;
        }
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationShell.Callback
    public void setDomainVerificationStatusInternal(java.lang.String packageName, int state, android.util.ArraySet<java.lang.String> domains) throws android.content.pm.PackageManager.NameNotFoundException {
        android.util.ArraySet<java.lang.String> validDomains;
        this.mEnforcer.assertInternal(this.mConnection.getCallingUid());
        switch (state) {
            case 0:
            case 1:
            case 2:
            case 3:
                if (packageName == null) {
                    com.android.server.pm.Computer snapshot = this.mConnection.snapshot();
                    synchronized (this.mLock) {
                        android.util.ArraySet<java.lang.String> validDomains2 = new android.util.ArraySet<>();
                        int size = this.mAttachedPkgStates.size();
                        for (int index = 0; index < size; index++) {
                            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.valueAt(index);
                            java.lang.String pkgName = pkgState.getPackageName();
                            com.android.server.pm.pkg.PackageStateInternal pkgSetting = snapshot.getPackageStateInternal(pkgName);
                            if (pkgSetting != null && pkgSetting.getPkg() != null) {
                                com.android.server.pm.pkg.AndroidPackage pkg = pkgSetting.getPkg();
                                validDomains2.clear();
                                android.util.ArraySet<java.lang.String> autoVerifyDomains = this.mCollector.collectValidAutoVerifyDomains(pkg);
                                if (domains == null) {
                                    validDomains2.addAll((android.util.ArraySet<? extends java.lang.String>) autoVerifyDomains);
                                } else {
                                    validDomains2.addAll((android.util.ArraySet<? extends java.lang.String>) domains);
                                    validDomains2.retainAll(autoVerifyDomains);
                                }
                                setDomainVerificationStatusInternal(pkgState, state, validDomains2);
                            }
                        }
                        break;
                    }
                } else {
                    com.android.server.pm.Computer snapshot2 = this.mConnection.snapshot();
                    synchronized (this.mLock) {
                        com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState2 = this.mAttachedPkgStates.get(packageName);
                        if (pkgState2 == null) {
                            throw com.android.server.pm.verify.domain.DomainVerificationUtils.throwPackageUnavailable(packageName);
                        }
                        com.android.server.pm.pkg.PackageStateInternal pkgSetting2 = snapshot2.getPackageStateInternal(packageName);
                        if (pkgSetting2 == null || pkgSetting2.getPkg() == null) {
                            throw com.android.server.pm.verify.domain.DomainVerificationUtils.throwPackageUnavailable(packageName);
                        }
                        com.android.server.pm.pkg.AndroidPackage pkg2 = pkgSetting2.getPkg();
                        if (domains == null) {
                            validDomains = this.mCollector.collectValidAutoVerifyDomains(pkg2);
                        } else {
                            validDomains = domains;
                            validDomains.retainAll(this.mCollector.collectValidAutoVerifyDomains(pkg2));
                        }
                        android.util.ArraySet<java.lang.String> newlyVerifiedDomains = null;
                        if (android.content.pm.verify.domain.DomainVerificationState.isVerified(state)) {
                            newlyVerifiedDomains = new android.util.ArraySet<>();
                            android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap = pkgState2.getStateMap();
                            int domainsSize = validDomains.size();
                            for (int domainIndex = 0; domainIndex < domainsSize; domainIndex++) {
                                java.lang.String domain = validDomains.valueAt(domainIndex);
                                java.lang.Integer oldState = stateMap.get(domain);
                                if (oldState == null || !android.content.pm.verify.domain.DomainVerificationState.isVerified(oldState.intValue())) {
                                    newlyVerifiedDomains.add(domain);
                                }
                            }
                        }
                        setDomainVerificationStatusInternal(pkgState2, state, validDomains);
                        if (newlyVerifiedDomains != null) {
                            int domainsSize2 = newlyVerifiedDomains.size();
                            for (int domainIndex2 = 0; domainIndex2 < domainsSize2; domainIndex2++) {
                                removeUserStatesForDomain(pkgState2, newlyVerifiedDomains.valueAt(domainIndex2));
                            }
                        }
                    }
                }
                this.mConnection.scheduleWriteSettings();
                return;
            default:
                throw new java.lang.IllegalArgumentException("State must be one of NO_RESPONSE, SUCCESS, APPROVED, or DENIED");
        }
    }

    private void setDomainVerificationStatusInternal(com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState, int state, android.util.ArraySet<java.lang.String> validDomains) {
        android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap = pkgState.getStateMap();
        int size = validDomains.size();
        for (int index = 0; index < size; index++) {
            stateMap.put(validDomains.valueAt(index), java.lang.Integer.valueOf(state));
        }
    }

    private void removeUserStatesForDomain(com.android.server.pm.verify.domain.models.DomainVerificationPkgState owningPkgState, java.lang.String domain) {
        android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> owningUserStates = owningPkgState.getUserStates();
        synchronized (this.mLock) {
            int size = this.mAttachedPkgStates.size();
            for (int index = 0; index < size; index++) {
                com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.valueAt(index);
                android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> array = pkgState.getUserStates();
                int arraySize = array.size();
                for (int arrayIndex = 0; arrayIndex < arraySize; arrayIndex++) {
                    int userId = array.keyAt(arrayIndex);
                    com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState owningUserState = owningUserStates.get(userId);
                    if (owningUserState == null || owningUserState.isLinkHandlingAllowed()) {
                        array.valueAt(arrayIndex).removeHost(domain);
                    }
                }
            }
        }
    }

    public void setDomainVerificationLinkHandlingAllowed(java.lang.String packageName, boolean allowed, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        if (!this.mEnforcer.assertApprovedUserSelector(this.mConnection.getCallingUid(), this.mConnection.getCallingUserId(), packageName, userId)) {
            throw com.android.server.pm.verify.domain.DomainVerificationUtils.throwPackageUnavailable(packageName);
        }
        synchronized (this.mLock) {
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.get(packageName);
            if (pkgState == null) {
                throw com.android.server.pm.verify.domain.DomainVerificationUtils.throwPackageUnavailable(packageName);
            }
            pkgState.getOrCreateUserState(userId).setLinkHandlingAllowed(allowed);
        }
        this.mConnection.scheduleWriteSettings();
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationShell.Callback
    public void setDomainVerificationLinkHandlingAllowedInternal(java.lang.String packageName, boolean allowed, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        this.mEnforcer.assertInternal(this.mConnection.getCallingUid());
        if (packageName == null) {
            synchronized (this.mLock) {
                int pkgStateSize = this.mAttachedPkgStates.size();
                for (int pkgStateIndex = 0; pkgStateIndex < pkgStateSize; pkgStateIndex++) {
                    com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.valueAt(pkgStateIndex);
                    if (userId == -1) {
                        for (int aUserId : this.mConnection.getAllUserIds()) {
                            pkgState.getOrCreateUserState(aUserId).setLinkHandlingAllowed(allowed);
                        }
                    } else {
                        pkgState.getOrCreateUserState(userId).setLinkHandlingAllowed(allowed);
                    }
                }
            }
        } else {
            synchronized (this.mLock) {
                com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState2 = this.mAttachedPkgStates.get(packageName);
                if (pkgState2 == null) {
                    throw com.android.server.pm.verify.domain.DomainVerificationUtils.throwPackageUnavailable(packageName);
                }
                if (userId == -1) {
                    for (int aUserId2 : this.mConnection.getAllUserIds()) {
                        pkgState2.getOrCreateUserState(aUserId2).setLinkHandlingAllowed(allowed);
                    }
                } else {
                    pkgState2.getOrCreateUserState(userId).setLinkHandlingAllowed(allowed);
                }
            }
        }
        this.mConnection.scheduleWriteSettings();
    }

    public int setDomainVerificationUserSelection(java.util.UUID domainSetId, java.util.Set<java.lang.String> domains, boolean enabled, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        int statusCode;
        int callingUid = this.mConnection.getCallingUid();
        if (!this.mEnforcer.assertApprovedUserSelector(callingUid, this.mConnection.getCallingUserId(), null, userId)) {
            return 1;
        }
        com.android.server.pm.Computer snapshot = this.mConnection.snapshot();
        synchronized (this.mLock) {
            com.android.server.pm.verify.domain.DomainVerificationService.GetAttachedResult result = getAndValidateAttachedLocked(domainSetId, domains, false, callingUid, java.lang.Integer.valueOf(userId), snapshot);
            if (result.isError()) {
                return result.getErrorCode();
            }
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = result.getPkgState();
            com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState userState = pkgState.getOrCreateUserState(userId);
            if (enabled && (statusCode = revokeOtherUserSelectionsLocked(userState, userId, domains, snapshot)) != 0) {
                return statusCode;
            }
            if (enabled) {
                userState.addHosts(domains);
            } else {
                userState.removeHosts(domains);
            }
            this.mConnection.scheduleWriteSettings();
            return 0;
        }
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationShell.Callback
    public void setDomainVerificationUserSelectionInternal(int userId, java.lang.String packageName, boolean enabled, android.util.ArraySet<java.lang.String> domains) throws android.content.pm.PackageManager.NameNotFoundException {
        this.mEnforcer.assertInternal(this.mConnection.getCallingUid());
        com.android.server.pm.Computer snapshot = this.mConnection.snapshot();
        synchronized (this.mLock) {
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.get(packageName);
            if (pkgState == null) {
                throw com.android.server.pm.verify.domain.DomainVerificationUtils.throwPackageUnavailable(packageName);
            }
            com.android.server.pm.pkg.PackageStateInternal pkgSetting = snapshot.getPackageStateInternal(packageName);
            com.android.server.pm.pkg.AndroidPackage pkg = pkgSetting == null ? null : pkgSetting.getPkg();
            if (pkg == null) {
                throw com.android.server.pm.verify.domain.DomainVerificationUtils.throwPackageUnavailable(packageName);
            }
            java.util.Set<java.lang.String> validDomains = domains == null ? this.mCollector.collectAllWebDomains(pkg) : domains;
            validDomains.retainAll(this.mCollector.collectAllWebDomains(pkg));
            if (userId == -1) {
                for (int aUserId : this.mConnection.getAllUserIds()) {
                    com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState userState = pkgState.getOrCreateUserState(aUserId);
                    revokeOtherUserSelectionsLocked(userState, aUserId, validDomains, snapshot);
                    if (enabled) {
                        userState.addHosts(validDomains);
                    } else {
                        userState.removeHosts(validDomains);
                    }
                }
            } else {
                com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState userState2 = pkgState.getOrCreateUserState(userId);
                revokeOtherUserSelectionsLocked(userState2, userId, validDomains, snapshot);
                if (enabled) {
                    userState2.addHosts(validDomains);
                } else {
                    userState2.removeHosts(validDomains);
                }
            }
        }
        this.mConnection.scheduleWriteSettings();
    }

    private int revokeOtherUserSelectionsLocked(com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState userState, int userId, java.util.Set<java.lang.String> domains, com.android.server.pm.Computer snapshot) {
        com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState approvedUserState;
        android.util.ArrayMap<java.lang.String, java.util.List<java.lang.String>> domainToApprovedPackages = new android.util.ArrayMap<>();
        for (java.lang.String domain : domains) {
            if (!userState.getEnabledHosts().contains(domain)) {
                android.util.Pair<java.util.List<java.lang.String>, java.lang.Integer> packagesToLevel = getApprovedPackagesLocked(domain, userId, 1, snapshot);
                int highestApproval = ((java.lang.Integer) packagesToLevel.second).intValue();
                if (highestApproval > 3) {
                    return 3;
                }
                domainToApprovedPackages.put(domain, (java.util.List) packagesToLevel.first);
            }
        }
        int mapSize = domainToApprovedPackages.size();
        for (int mapIndex = 0; mapIndex < mapSize; mapIndex++) {
            java.lang.String domain2 = domainToApprovedPackages.keyAt(mapIndex);
            java.util.List<java.lang.String> approvedPackages = domainToApprovedPackages.valueAt(mapIndex);
            int approvedSize = approvedPackages.size();
            for (int approvedIndex = 0; approvedIndex < approvedSize; approvedIndex++) {
                java.lang.String approvedPackage = approvedPackages.get(approvedIndex);
                com.android.server.pm.verify.domain.models.DomainVerificationPkgState approvedPkgState = this.mAttachedPkgStates.get(approvedPackage);
                if (approvedPkgState != null && (approvedUserState = approvedPkgState.getUserState(userId)) != null) {
                    approvedUserState.removeHost(domain2);
                }
            }
        }
        return 0;
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationShell.Callback
    public android.content.pm.verify.domain.DomainVerificationUserState getDomainVerificationUserState(java.lang.String packageName, int userId) throws android.content.pm.PackageManager.NameNotFoundException {
        int domainState;
        if (!this.mEnforcer.assertApprovedUserStateQuerent(this.mConnection.getCallingUid(), this.mConnection.getCallingUserId(), packageName, userId)) {
            throw com.android.server.pm.verify.domain.DomainVerificationUtils.throwPackageUnavailable(packageName);
        }
        com.android.server.pm.Computer snapshot = this.mConnection.snapshot();
        synchronized (this.mLock) {
            com.android.server.pm.pkg.PackageStateInternal pkgSetting = snapshot.getPackageStateInternal(packageName);
            com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = pkgSetting == null ? null : pkgSetting.getPkg();
            if (pkg == null) {
                throw com.android.server.pm.verify.domain.DomainVerificationUtils.throwPackageUnavailable(packageName);
            }
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.get(packageName);
            if (pkgState == null) {
                return null;
            }
            android.util.ArraySet<java.lang.String> webDomains = this.mCollector.collectAllWebDomains(pkg);
            int webDomainsSize = webDomains.size();
            java.util.Map<java.lang.String, java.lang.Integer> domains = new android.util.ArrayMap<>(webDomainsSize);
            android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap = pkgState.getStateMap();
            com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState userState = pkgState.getUserState(userId);
            java.util.Set<java.lang.String> enabledHosts = userState == null ? java.util.Collections.emptySet() : userState.getEnabledHosts();
            int index = 0;
            while (index < webDomainsSize) {
                java.lang.String host = webDomains.valueAt(index);
                java.lang.Integer state = stateMap.get(host);
                if (state != null && android.content.pm.verify.domain.DomainVerificationState.isVerified(state.intValue())) {
                    domainState = 2;
                } else if (enabledHosts.contains(host)) {
                    domainState = 1;
                } else {
                    domainState = 0;
                }
                domains.put(host, java.lang.Integer.valueOf(domainState));
                index++;
                pkgSetting = pkgSetting;
            }
            boolean linkHandlingAllowed = userState == null || userState.isLinkHandlingAllowed();
            return new android.content.pm.verify.domain.DomainVerificationUserState(pkgState.getId(), packageName, android.os.UserHandle.of(userId), linkHandlingAllowed, domains);
        }
    }

    public java.util.List<android.content.pm.verify.domain.DomainOwner> getOwnersForDomain(java.lang.String domain, int userId) throws java.lang.Throwable {
        java.util.Objects.requireNonNull(domain);
        this.mEnforcer.assertOwnerQuerent(this.mConnection.getCallingUid(), this.mConnection.getCallingUserId(), userId);
        com.android.server.pm.Computer snapshot = this.mConnection.snapshot();
        android.util.SparseArray<java.util.List<java.lang.String>> levelToPackages = getOwnersForDomainInternal(domain, false, userId, snapshot);
        if (levelToPackages.size() == 0) {
            return java.util.Collections.emptyList();
        }
        java.util.List<android.content.pm.verify.domain.DomainOwner> owners = new java.util.ArrayList<>();
        int size = levelToPackages.size();
        for (int index = 0; index < size; index++) {
            int level = levelToPackages.keyAt(index);
            boolean overrideable = level <= 3;
            java.util.List<java.lang.String> packages = levelToPackages.valueAt(index);
            int packagesSize = packages.size();
            for (int packageIndex = 0; packageIndex < packagesSize; packageIndex++) {
                owners.add(new android.content.pm.verify.domain.DomainOwner(packages.get(packageIndex), overrideable));
            }
        }
        return owners;
    }

    private android.util.SparseArray<java.util.List<java.lang.String>> getOwnersForDomainInternal(java.lang.String domain, boolean includeNegative, final int userId, final com.android.server.pm.Computer snapshot) throws java.lang.Throwable {
        android.util.SparseArray<java.util.List<java.lang.String>> levelToPackages = new android.util.SparseArray<>();
        synchronized (this.mLock) {
            try {
                int size = this.mAttachedPkgStates.size();
                for (int index = 0; index < size; index++) {
                    com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.valueAt(index);
                    java.lang.String packageName = pkgState.getPackageName();
                    com.android.server.pm.pkg.PackageStateInternal pkgSetting = snapshot.getPackageStateInternal(packageName);
                    if (pkgSetting != null) {
                        int level = approvalLevelForDomain(pkgSetting, domain, includeNegative, userId, false, domain);
                        if (includeNegative || level > 0) {
                            java.util.List<java.lang.String> list = levelToPackages.get(level);
                            if (list == null) {
                                list = new java.util.ArrayList();
                                levelToPackages.put(level, list);
                            }
                            list.add(packageName);
                        }
                    }
                }
            } catch (java.lang.Throwable th) {
                th = th;
                while (true) {
                    try {
                        throw th;
                    } catch (java.lang.Throwable th2) {
                        th = th2;
                    }
                }
            }
        }
        int size2 = levelToPackages.size();
        if (size2 == 0) {
            return levelToPackages;
        }
        for (int index2 = 0; index2 < size2; index2++) {
            levelToPackages.valueAt(index2).sort(new java.util.Comparator() { // from class: com.android.server.pm.verify.domain.DomainVerificationService$$ExternalSyntheticLambda1
                @Override // java.util.Comparator
                public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                    return com.android.server.pm.verify.domain.DomainVerificationService.lambda$getOwnersForDomainInternal$0(snapshot, userId, (java.lang.String) obj, (java.lang.String) obj2);
                }
            });
        }
        return levelToPackages;
    }

    static /* synthetic */ int lambda$getOwnersForDomainInternal$0(com.android.server.pm.Computer snapshot, int userId, java.lang.String first, java.lang.String second) {
        long firstInstallTime;
        com.android.server.pm.pkg.PackageStateInternal firstPkgSetting = snapshot.getPackageStateInternal(first);
        com.android.server.pm.pkg.PackageStateInternal secondPkgSetting = snapshot.getPackageStateInternal(second);
        long secondInstallTime = -1;
        if (firstPkgSetting == null) {
            firstInstallTime = -1;
        } else {
            firstInstallTime = firstPkgSetting.getUserStateOrDefault(userId).getFirstInstallTimeMillis();
        }
        if (secondPkgSetting != null) {
            secondInstallTime = secondPkgSetting.getUserStateOrDefault(userId).getFirstInstallTimeMillis();
        }
        if (firstInstallTime != secondInstallTime) {
            return (int) (firstInstallTime - secondInstallTime);
        }
        return first.compareToIgnoreCase(second);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public java.util.UUID generateNewId() {
        return java.util.UUID.randomUUID();
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public void migrateState(com.android.server.pm.pkg.PackageStateInternal oldPkgSetting, com.android.server.pm.pkg.PackageStateInternal newPkgSetting, android.content.pm.verify.domain.DomainSet preVerifiedDomains) throws java.lang.Throwable {
        com.android.server.pm.pkg.AndroidPackage newPkg;
        android.util.ArrayMap<java.lang.String, java.lang.Integer> oldStateMap;
        java.lang.String pkgName = newPkgSetting.getPackageName();
        synchronized (this.mLock) {
            try {
                try {
                    java.util.UUID oldDomainSetId = oldPkgSetting.getDomainSetId();
                    java.util.UUID newDomainSetId = newPkgSetting.getDomainSetId();
                    com.android.server.pm.verify.domain.models.DomainVerificationPkgState oldPkgState = this.mAttachedPkgStates.remove(oldDomainSetId);
                    com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = oldPkgSetting.getPkg();
                    com.android.server.pm.pkg.AndroidPackage newPkg2 = newPkgSetting.getPkg();
                    android.util.ArrayMap<java.lang.String, java.lang.Integer> newStateMap = new android.util.ArrayMap<>();
                    android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> newUserStates = new android.util.SparseArray<>();
                    if (oldPkgState == null || pkg == null || newPkg2 == null) {
                        android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> newUserStates2 = newUserStates;
                        android.util.ArrayMap<java.lang.String, java.lang.Integer> newStateMap2 = newStateMap;
                        com.android.internal.pm.parsing.pkg.AndroidPackageInternal androidPackageInternal = pkg;
                        com.android.server.pm.pkg.AndroidPackage oldPkg = newPkg2;
                        android.util.Slog.wtf(TAG, "Invalid state nullability old state = " + oldPkgState + ", old pkgSetting = " + oldPkgSetting + ", new pkgSetting = " + newPkgSetting + ", old pkg = " + androidPackageInternal + ", new pkg = " + oldPkg, new java.lang.Exception());
                        com.android.server.pm.verify.domain.models.DomainVerificationPkgState newPkgState = new com.android.server.pm.verify.domain.models.DomainVerificationPkgState(pkgName, newDomainSetId, true, newStateMap2, newUserStates2, null);
                        this.mAttachedPkgStates.put(pkgName, newDomainSetId, newPkgState);
                        return;
                    }
                    android.util.ArrayMap<java.lang.String, java.lang.Integer> oldStateMap2 = oldPkgState.getStateMap();
                    android.util.ArrayMap<java.lang.String, java.util.List<android.content.UriRelativeFilterGroup>> oldGroups = oldPkgState.getUriRelativeFilterGroupMap();
                    android.util.ArraySet<java.lang.String> newAutoVerifyDomains = this.mCollector.collectValidAutoVerifyDomains(newPkg2);
                    int newDomainsSize = newAutoVerifyDomains.size();
                    int newDomainsIndex = 0;
                    while (newDomainsIndex < newDomainsSize) {
                        java.lang.String domain = newAutoVerifyDomains.valueAt(newDomainsIndex);
                        java.util.UUID oldDomainSetId2 = oldDomainSetId;
                        java.lang.Integer oldStateInteger = oldStateMap2.get(domain);
                        if (oldStateInteger == null) {
                            oldStateMap = oldStateMap2;
                        } else {
                            int oldState = oldStateInteger.intValue();
                            if (!android.content.pm.verify.domain.DomainVerificationState.shouldMigrate(oldState)) {
                                oldStateMap = oldStateMap2;
                            } else {
                                oldStateMap = oldStateMap2;
                                newStateMap.put(domain, java.lang.Integer.valueOf(oldState));
                            }
                        }
                        newDomainsIndex++;
                        oldDomainSetId = oldDomainSetId2;
                        oldStateMap2 = oldStateMap;
                    }
                    android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> oldUserStates = oldPkgState.getUserStates();
                    int oldUserStatesSize = oldUserStates.size();
                    if (oldUserStatesSize > 0) {
                        android.util.ArraySet<java.lang.String> newWebDomains = this.mCollector.collectAllWebDomains(newPkg2);
                        newPkg = newPkg2;
                        int oldUserStatesIndex = 0;
                        while (oldUserStatesIndex < oldUserStatesSize) {
                            int userId = oldUserStates.keyAt(oldUserStatesIndex);
                            com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState oldUserState = oldUserStates.valueAt(oldUserStatesIndex);
                            android.util.ArraySet<java.lang.String> oldEnabledHosts = oldUserState.getEnabledHosts();
                            android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> oldUserStates2 = oldUserStates;
                            android.util.ArraySet<java.lang.String> newEnabledHosts = new android.util.ArraySet<>(oldEnabledHosts);
                            newEnabledHosts.retainAll(newWebDomains);
                            android.util.ArraySet<java.lang.String> newWebDomains2 = newWebDomains;
                            com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState newUserState = new com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState(userId, newEnabledHosts, oldUserState.isLinkHandlingAllowed());
                            newUserStates.put(userId, newUserState);
                            oldUserStatesIndex++;
                            oldUserStates = oldUserStates2;
                            newWebDomains = newWebDomains2;
                            oldUserStatesSize = oldUserStatesSize;
                            pkg = pkg;
                        }
                    } else {
                        newPkg = newPkg2;
                    }
                    boolean sendBroadcast = false;
                    boolean hasAutoVerifyDomains = newDomainsSize > 0;
                    boolean needsBroadcast = applyImmutableState(newPkgSetting, newStateMap, newAutoVerifyDomains);
                    if (hasAutoVerifyDomains && needsBroadcast) {
                        sendBroadcast = true;
                    }
                    applyPreVerifiedState(newStateMap, newAutoVerifyDomains, preVerifiedDomains);
                    this.mAttachedPkgStates.put(pkgName, newDomainSetId, new com.android.server.pm.verify.domain.models.DomainVerificationPkgState(pkgName, newDomainSetId, hasAutoVerifyDomains, newStateMap, newUserStates, null, oldGroups));
                    if (sendBroadcast) {
                        sendBroadcast(pkgName);
                    }
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public void addPackage(com.android.server.pm.pkg.PackageStateInternal newPkgSetting, android.content.pm.verify.domain.DomainSet preVerifiedDomains) {
        boolean sendBroadcast;
        com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState;
        com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState2;
        java.util.UUID domainSetId = newPkgSetting.getDomainSetId();
        java.lang.String pkgName = newPkgSetting.getPackageName();
        com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState3 = this.mSettings.removePendingState(pkgName);
        if (pkgState3 != null) {
            sendBroadcast = false;
            pkgState = pkgState3;
        } else {
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState4 = this.mSettings.removeRestoredState(pkgName);
            if (pkgState4 == null || java.util.Objects.equals(pkgState4.getBackupSignatureHash(), android.util.PackageUtils.computeSignaturesSha256Digest(newPkgSetting.getSigningDetails().getSignatures()))) {
                sendBroadcast = true;
                pkgState = pkgState4;
            } else {
                sendBroadcast = true;
                pkgState = null;
            }
        }
        com.android.server.pm.pkg.AndroidPackage pkg = newPkgSetting.getPkg();
        android.util.ArraySet<java.lang.String> autoVerifyDomains = this.mCollector.collectValidAutoVerifyDomains(pkg);
        boolean hasAutoVerifyDomains = !autoVerifyDomains.isEmpty();
        boolean isPendingOrRestored = pkgState != null;
        if (isPendingOrRestored) {
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState5 = new com.android.server.pm.verify.domain.models.DomainVerificationPkgState(pkgState, domainSetId, hasAutoVerifyDomains);
            pkgState5.getStateMap().retainAll(autoVerifyDomains);
            java.util.Set<java.lang.String> webDomains = this.mCollector.collectAllWebDomains(pkg);
            android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> userStates = pkgState5.getUserStates();
            int size = userStates.size();
            for (int index = 0; index < size; index++) {
                userStates.valueAt(index).retainHosts(webDomains);
            }
            pkgState2 = pkgState5;
        } else {
            pkgState2 = new com.android.server.pm.verify.domain.models.DomainVerificationPkgState(pkgName, domainSetId, hasAutoVerifyDomains);
        }
        boolean needsBroadcast = applyImmutableState(newPkgSetting, pkgState2.getStateMap(), autoVerifyDomains);
        if (needsBroadcast && !isPendingOrRestored) {
            android.util.ArraySet<java.lang.String> webDomains2 = null;
            android.util.SparseIntArray legacyUserStates = this.mLegacySettings.getUserStates(pkgName);
            int userStateSize = legacyUserStates != null ? legacyUserStates.size() : 0;
            int index2 = 0;
            while (index2 < userStateSize) {
                int userId = legacyUserStates.keyAt(index2);
                boolean isPendingOrRestored2 = isPendingOrRestored;
                int legacyStatus = legacyUserStates.valueAt(index2);
                int userStateSize2 = userStateSize;
                if (legacyStatus == 2) {
                    if (webDomains2 == null) {
                        webDomains2 = this.mCollector.collectAllWebDomains(pkg);
                    }
                    pkgState2.getOrCreateUserState(userId).addHosts(webDomains2);
                }
                index2++;
                isPendingOrRestored = isPendingOrRestored2;
                userStateSize = userStateSize2;
            }
            android.content.pm.IntentFilterVerificationInfo legacyInfo = this.mLegacySettings.remove(pkgName);
            if (legacyInfo != null && legacyInfo.getStatus() == 2) {
                android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap = pkgState2.getStateMap();
                int domainsSize = autoVerifyDomains.size();
                int index3 = 0;
                while (index3 < domainsSize) {
                    stateMap.put(autoVerifyDomains.valueAt(index3), 4);
                    index3++;
                    webDomains2 = webDomains2;
                    pkg = pkg;
                }
            }
            applyPreVerifiedState(pkgState2.getStateMap(), autoVerifyDomains, preVerifiedDomains);
        }
        synchronized (this.mLock) {
            this.mAttachedPkgStates.put(pkgName, domainSetId, pkgState2);
        }
        if (sendBroadcast && hasAutoVerifyDomains) {
            sendBroadcast(pkgName);
        }
    }

    private void applyPreVerifiedState(android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap, android.util.ArraySet<java.lang.String> autoVerifyDomains, android.content.pm.verify.domain.DomainSet preVerifiedDomains) {
        if (preVerifiedDomains != null && !autoVerifyDomains.isEmpty()) {
            for (java.lang.String preVerifiedDomain : preVerifiedDomains.getDomains()) {
                if (autoVerifyDomains.contains(preVerifiedDomain) && !stateMap.containsKey(preVerifiedDomain)) {
                    stateMap.put(preVerifiedDomain, 8);
                }
            }
        }
    }

    private boolean applyImmutableState(com.android.server.pm.pkg.PackageStateInternal pkgSetting, android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap, android.util.ArraySet<java.lang.String> autoVerifyDomains) {
        if (pkgSetting.isSystem() && this.mSystemConfig.getLinkedApps().contains(pkgSetting.getPackageName())) {
            int domainsSize = autoVerifyDomains.size();
            for (int index = 0; index < domainsSize; index++) {
                stateMap.put(autoVerifyDomains.valueAt(index), 7);
            }
            return false;
        }
        int size = stateMap.size();
        for (int index2 = size - 1; index2 >= 0; index2--) {
            java.lang.Integer state = stateMap.valueAt(index2);
            if (state.intValue() == 7) {
                stateMap.removeAt(index2);
            }
        }
        return true;
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public void writeSettings(final com.android.server.pm.Computer snapshot, com.android.modules.utils.TypedXmlSerializer serializer, boolean includeSignatures, int userId) throws java.io.IOException {
        synchronized (this.mLock) {
            java.util.function.Function<java.lang.String, java.lang.String> pkgNameToSignature = null;
            if (includeSignatures) {
                pkgNameToSignature = new java.util.function.Function() { // from class: com.android.server.pm.verify.domain.DomainVerificationService$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return com.android.server.pm.verify.domain.DomainVerificationService.lambda$writeSettings$1(snapshot, (java.lang.String) obj);
                    }
                };
                this.mSettings.writeSettings(serializer, this.mAttachedPkgStates, userId, pkgNameToSignature);
            } else {
                this.mSettings.writeSettings(serializer, this.mAttachedPkgStates, userId, pkgNameToSignature);
            }
        }
        this.mLegacySettings.writeSettings(serializer);
    }

    static /* synthetic */ java.lang.String lambda$writeSettings$1(com.android.server.pm.Computer snapshot, java.lang.String pkgName) {
        com.android.server.pm.pkg.PackageStateInternal pkgSetting = snapshot.getPackageStateInternal(pkgName);
        if (pkgSetting == null) {
            return null;
        }
        return android.util.PackageUtils.computeSignaturesSha256Digest(pkgSetting.getSigningDetails().getSignatures());
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public void readSettings(com.android.server.pm.Computer snapshot, com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        synchronized (this.mLock) {
            this.mSettings.readSettings(parser, this.mAttachedPkgStates, snapshot);
        }
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public void readLegacySettings(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        this.mLegacySettings.readSettings(parser);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public void restoreSettings(com.android.server.pm.Computer snapshot, com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        synchronized (this.mLock) {
            this.mSettings.restoreSettings(parser, this.mAttachedPkgStates, snapshot);
        }
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public void addLegacySetting(java.lang.String packageName, android.content.pm.IntentFilterVerificationInfo info) {
        this.mLegacySettings.add(packageName, info);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public boolean setLegacyUserState(java.lang.String packageName, int userId, int state) {
        if (!this.mEnforcer.callerIsLegacyUserSelector(this.mConnection.getCallingUid(), this.mConnection.getCallingUserId(), packageName, userId)) {
            return false;
        }
        this.mLegacySettings.add(packageName, userId, state);
        this.mConnection.scheduleWriteSettings();
        return true;
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public int getLegacyState(java.lang.String packageName, int userId) {
        if (!this.mEnforcer.callerIsLegacyUserQuerent(this.mConnection.getCallingUid(), this.mConnection.getCallingUserId(), packageName, userId)) {
            return 0;
        }
        return this.mLegacySettings.getUserState(packageName, userId);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public void clearPackage(java.lang.String packageName) {
        synchronized (this.mLock) {
            this.mAttachedPkgStates.remove(packageName);
            this.mSettings.removePackage(packageName);
        }
        this.mConnection.scheduleWriteSettings();
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public void clearPackageForUser(java.lang.String packageName, int userId) {
        synchronized (this.mLock) {
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.get(packageName);
            if (pkgState != null) {
                pkgState.removeUser(userId);
            }
            this.mSettings.removePackageForUser(packageName, userId);
        }
        this.mConnection.scheduleWriteSettings();
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public void clearUser(int userId) {
        synchronized (this.mLock) {
            int attachedSize = this.mAttachedPkgStates.size();
            for (int index = 0; index < attachedSize; index++) {
                this.mAttachedPkgStates.valueAt(index).removeUser(userId);
            }
            this.mSettings.removeUser(userId);
        }
        this.mConnection.scheduleWriteSettings();
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public boolean runMessage(int messageCode, java.lang.Object object) {
        return this.mProxy.runMessage(messageCode, object);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationShell.Callback
    public void printState(android.util.IndentingPrintWriter writer, java.lang.String packageName, java.lang.Integer userId) throws android.content.pm.PackageManager.NameNotFoundException {
        printState(this.mConnection.snapshot(), writer, packageName, userId);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public void printState(com.android.server.pm.Computer snapshot, android.util.IndentingPrintWriter writer, java.lang.String packageName, java.lang.Integer userId) throws android.content.pm.PackageManager.NameNotFoundException {
        this.mEnforcer.assertApprovedQuerent(this.mConnection.getCallingUid(), this.mProxy);
        synchronized (this.mLock) {
            this.mDebug.printState(writer, packageName, userId, snapshot, this.mAttachedPkgStates);
        }
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationShell.Callback
    public void printOwnersForPackage(android.util.IndentingPrintWriter writer, java.lang.String packageName, java.lang.Integer userId) throws android.content.pm.PackageManager.NameNotFoundException {
        this.mEnforcer.assertApprovedQuerent(this.mConnection.getCallingUid(), this.mProxy);
        com.android.server.pm.Computer snapshot = this.mConnection.snapshot();
        synchronized (this.mLock) {
            if (packageName == null) {
                int size = this.mAttachedPkgStates.size();
                for (int index = 0; index < size; index++) {
                    try {
                        printOwnersForPackage(writer, this.mAttachedPkgStates.valueAt(index).getPackageName(), userId, snapshot);
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    }
                }
            } else {
                printOwnersForPackage(writer, packageName, userId, snapshot);
            }
        }
    }

    private void printOwnersForPackage(android.util.IndentingPrintWriter writer, java.lang.String packageName, java.lang.Integer userId, com.android.server.pm.Computer snapshot) throws android.content.pm.PackageManager.NameNotFoundException {
        com.android.server.pm.pkg.PackageStateInternal pkgSetting = snapshot.getPackageStateInternal(packageName);
        com.android.server.pm.pkg.AndroidPackage pkg = pkgSetting == null ? null : pkgSetting.getPkg();
        if (pkg == null) {
            throw com.android.server.pm.verify.domain.DomainVerificationUtils.throwPackageUnavailable(packageName);
        }
        android.util.ArraySet<java.lang.String> domains = this.mCollector.collectAllWebDomains(pkg);
        int size = domains.size();
        if (size == 0) {
            return;
        }
        writer.println(packageName + ":");
        writer.increaseIndent();
        for (int index = 0; index < size; index++) {
            printOwnersForDomain(writer, domains.valueAt(index), userId, snapshot);
        }
        writer.decreaseIndent();
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationShell.Callback
    public void printOwnersForDomains(android.util.IndentingPrintWriter writer, java.util.List<java.lang.String> domains, java.lang.Integer userId) {
        this.mEnforcer.assertApprovedQuerent(this.mConnection.getCallingUid(), this.mProxy);
        com.android.server.pm.Computer snapshot = this.mConnection.snapshot();
        synchronized (this.mLock) {
            int size = domains.size();
            for (int index = 0; index < size; index++) {
                printOwnersForDomain(writer, domains.get(index), userId, snapshot);
            }
        }
    }

    private void printOwnersForDomain(android.util.IndentingPrintWriter writer, java.lang.String domain, java.lang.Integer userId, com.android.server.pm.Computer snapshot) {
        android.util.SparseArray<android.util.SparseArray<java.util.List<java.lang.String>>> userIdToApprovalLevelToOwners = new android.util.SparseArray<>();
        if (userId == null || userId.intValue() == -1) {
            for (int aUserId : this.mConnection.getAllUserIds()) {
                userIdToApprovalLevelToOwners.put(aUserId, getOwnersForDomainInternal(domain, true, aUserId, snapshot));
            }
        } else {
            userIdToApprovalLevelToOwners.put(userId.intValue(), getOwnersForDomainInternal(domain, true, userId.intValue(), snapshot));
        }
        this.mDebug.printOwners(writer, domain, userIdToApprovalLevelToOwners);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public com.android.server.pm.verify.domain.DomainVerificationShell getShell() {
        return this.mShell;
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public com.android.server.pm.verify.domain.DomainVerificationCollector getCollector() {
        return this.mCollector;
    }

    private void sendBroadcast(java.lang.String packageName) {
        sendBroadcast(java.util.Collections.singleton(packageName));
    }

    private void sendBroadcast(java.util.Set<java.lang.String> packageNames) {
        if (!this.mCanSendBroadcasts) {
            return;
        }
        this.mProxy.sendBroadcastForPackages(packageNames);
    }

    private boolean hasRealVerifier() {
        return !(this.mProxy instanceof com.android.server.pm.verify.domain.proxy.DomainVerificationProxyUnavailable);
    }

    private com.android.server.pm.verify.domain.DomainVerificationService.GetAttachedResult getAndValidateAttachedLocked(java.util.UUID domainSetId, java.util.Set<java.lang.String> domains, boolean forAutoVerify, int callingUid, java.lang.Integer userIdForFilter, com.android.server.pm.Computer snapshot) throws android.content.pm.PackageManager.NameNotFoundException {
        android.util.ArraySet<java.lang.String> declaredDomains;
        if (domainSetId == null) {
            throw new java.lang.IllegalArgumentException("domainSetId cannot be null");
        }
        com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.get(domainSetId);
        if (pkgState == null) {
            return com.android.server.pm.verify.domain.DomainVerificationService.GetAttachedResult.error(1);
        }
        java.lang.String pkgName = pkgState.getPackageName();
        if (userIdForFilter != null && this.mConnection.filterAppAccess(pkgName, callingUid, userIdForFilter.intValue())) {
            return com.android.server.pm.verify.domain.DomainVerificationService.GetAttachedResult.error(1);
        }
        com.android.server.pm.pkg.PackageStateInternal pkgSetting = snapshot.getPackageStateInternal(pkgName);
        if (pkgSetting == null || pkgSetting.getPkg() == null) {
            throw com.android.server.pm.verify.domain.DomainVerificationUtils.throwPackageUnavailable(pkgName);
        }
        if (com.android.internal.util.CollectionUtils.isEmpty(domains)) {
            throw new java.lang.IllegalArgumentException("Provided domain set cannot be empty");
        }
        com.android.server.pm.pkg.AndroidPackage pkg = pkgSetting.getPkg();
        if (forAutoVerify) {
            declaredDomains = this.mCollector.collectValidAutoVerifyDomains(pkg);
        } else {
            declaredDomains = this.mCollector.collectAllWebDomains(pkg);
        }
        if (domains.retainAll(declaredDomains)) {
            return com.android.server.pm.verify.domain.DomainVerificationService.GetAttachedResult.error(2);
        }
        return com.android.server.pm.verify.domain.DomainVerificationService.GetAttachedResult.success(pkgState);
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationShell.Callback
    public void verifyPackages(java.util.List<java.lang.String> packageNames, boolean reVerify) {
        this.mEnforcer.assertInternal(this.mConnection.getCallingUid());
        java.util.Set<java.lang.String> packagesToBroadcast = new android.util.ArraySet<>();
        if (packageNames == null) {
            synchronized (this.mLock) {
                int pkgStatesSize = this.mAttachedPkgStates.size();
                for (int pkgStateIndex = 0; pkgStateIndex < pkgStatesSize; pkgStateIndex++) {
                    addIfShouldBroadcastLocked(packagesToBroadcast, this.mAttachedPkgStates.valueAt(pkgStateIndex), reVerify);
                }
            }
        } else {
            synchronized (this.mLock) {
                int size = packageNames.size();
                for (int index = 0; index < size; index++) {
                    java.lang.String packageName = packageNames.get(index);
                    com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.get(packageName);
                    if (pkgState != null) {
                        addIfShouldBroadcastLocked(packagesToBroadcast, pkgState, reVerify);
                    }
                }
            }
        }
        if (!packagesToBroadcast.isEmpty()) {
            sendBroadcast(packagesToBroadcast);
        }
    }

    private void addIfShouldBroadcastLocked(java.util.Collection<java.lang.String> packageNames, com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState, boolean reVerify) {
        if ((reVerify && pkgState.isHasAutoVerifyDomains()) || shouldReBroadcastPackage(pkgState)) {
            packageNames.add(pkgState.getPackageName());
        }
    }

    private boolean shouldReBroadcastPackage(com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState) {
        if (!pkgState.isHasAutoVerifyDomains()) {
            return false;
        }
        android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap = pkgState.getStateMap();
        int statesSize = stateMap.size();
        for (int stateIndex = 0; stateIndex < statesSize; stateIndex++) {
            java.lang.Integer state = stateMap.valueAt(stateIndex);
            if (!android.content.pm.verify.domain.DomainVerificationState.isDefault(state.intValue())) {
                return false;
            }
        }
        return true;
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationShell.Callback
    public void clearDomainVerificationState(java.util.List<java.lang.String> packageNames) {
        this.mEnforcer.assertInternal(this.mConnection.getCallingUid());
        com.android.server.pm.Computer snapshot = this.mConnection.snapshot();
        synchronized (this.mLock) {
            if (packageNames == null) {
                int size = this.mAttachedPkgStates.size();
                for (int index = 0; index < size; index++) {
                    com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.valueAt(index);
                    com.android.server.pm.pkg.PackageStateInternal pkgSetting = snapshot.getPackageStateInternal(pkgState.getPackageName());
                    if (pkgSetting != null && pkgSetting.getPkg() != null) {
                        resetDomainState(pkgState.getStateMap(), pkgSetting);
                    }
                }
            } else {
                int size2 = packageNames.size();
                for (int index2 = 0; index2 < size2; index2++) {
                    java.lang.String pkgName = packageNames.get(index2);
                    com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState2 = this.mAttachedPkgStates.get(pkgName);
                    com.android.server.pm.pkg.PackageStateInternal pkgSetting2 = snapshot.getPackageStateInternal(pkgName);
                    if (pkgSetting2 != null && pkgSetting2.getPkg() != null) {
                        resetDomainState(pkgState2.getStateMap(), pkgSetting2);
                    }
                }
            }
        }
        this.mConnection.scheduleWriteSettings();
    }

    private void resetDomainState(android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap, com.android.server.pm.pkg.PackageStateInternal pkgSetting) {
        boolean reset;
        int size = stateMap.size();
        for (int index = size - 1; index >= 0; index--) {
            java.lang.Integer state = stateMap.valueAt(index);
            switch (state.intValue()) {
                case 1:
                case 5:
                    reset = true;
                    break;
                default:
                    reset = state.intValue() >= 1024;
                    break;
            }
            if (reset) {
                stateMap.removeAt(index);
            }
        }
        applyImmutableState(pkgSetting, stateMap, this.mCollector.collectValidAutoVerifyDomains(pkgSetting.getPkg()));
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationShell.Callback
    public void clearUserStates(java.util.List<java.lang.String> packageNames, int userId) {
        this.mEnforcer.assertInternal(this.mConnection.getCallingUid());
        synchronized (this.mLock) {
            if (packageNames == null) {
                int size = this.mAttachedPkgStates.size();
                for (int index = 0; index < size; index++) {
                    com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.valueAt(index);
                    if (userId == -1) {
                        pkgState.removeAllUsers();
                    } else {
                        pkgState.removeUser(userId);
                    }
                }
            } else {
                int size2 = packageNames.size();
                for (int index2 = 0; index2 < size2; index2++) {
                    java.lang.String pkgName = packageNames.get(index2);
                    com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState2 = this.mAttachedPkgStates.get(pkgName);
                    if (userId == -1) {
                        pkgState2.removeAllUsers();
                    } else {
                        pkgState2.removeUser(userId);
                    }
                }
            }
        }
        this.mConnection.scheduleWriteSettings();
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public android.util.Pair<java.util.List<android.content.pm.ResolveInfo>, java.lang.Integer> filterToApprovedApp(android.content.Intent intent, java.util.List<android.content.pm.ResolveInfo> infos, int userId, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> pkgSettingFunction) {
        android.util.ArrayMap<android.content.pm.ResolveInfo, java.lang.Integer> infoApprovals = new android.util.ArrayMap<>();
        int infosSize = infos.size();
        for (int index = 0; index < infosSize; index++) {
            android.content.pm.ResolveInfo info = infos.get(index);
            if (info.isAutoResolutionAllowed()) {
                infoApprovals.put(info, null);
            }
        }
        int highestApproval = fillMapWithApprovalLevels(infoApprovals, intent.getData(), userId, pkgSettingFunction);
        if (highestApproval <= 0) {
            return android.util.Pair.create(java.util.Collections.emptyList(), java.lang.Integer.valueOf(highestApproval));
        }
        for (int index2 = infoApprovals.size() - 1; index2 >= 0; index2--) {
            if (infoApprovals.valueAt(index2).intValue() != highestApproval) {
                infoApprovals.removeAt(index2);
            }
        }
        if (highestApproval != 1) {
            filterToLastFirstInstalled(infoApprovals, pkgSettingFunction);
        }
        int size = infoApprovals.size();
        java.util.List<android.content.pm.ResolveInfo> finalList = new java.util.ArrayList<>(size);
        for (int index3 = 0; index3 < size; index3++) {
            finalList.add(infoApprovals.keyAt(index3));
        }
        if (highestApproval != 1) {
            filterToLastDeclared(finalList, pkgSettingFunction);
        }
        return android.util.Pair.create(finalList, java.lang.Integer.valueOf(highestApproval));
    }

    private boolean matchUriRelativeFilterGroups(android.net.Uri uri, java.lang.String pkgName) {
        if (uri.getHost() == null) {
            return false;
        }
        java.util.List<android.content.UriRelativeFilterGroup> groups = getUriRelativeFilterGroups(pkgName, uri.getHost());
        if (groups.isEmpty()) {
            return true;
        }
        return android.content.UriRelativeFilterGroup.matchGroupsToUri(groups, uri);
    }

    private int fillMapWithApprovalLevels(android.util.ArrayMap<android.content.pm.ResolveInfo, java.lang.Integer> inputMap, android.net.Uri uri, int userId, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> pkgSettingFunction) {
        int size = inputMap.size();
        int highestApproval = 0;
        for (int index = 0; index < size; index++) {
            if (inputMap.valueAt(index) == null) {
                android.content.pm.ResolveInfo info = inputMap.keyAt(index);
                java.lang.String packageName = info.getComponentInfo().packageName;
                com.android.server.pm.pkg.PackageStateInternal pkgSetting = pkgSettingFunction.apply(packageName);
                if (pkgSetting == null || (com.android.internal.hidden_from_bootclasspath.android.content.pm.Flags.relativeReferenceIntentFilters() && !matchUriRelativeFilterGroups(uri, packageName))) {
                    fillInfoMapForSamePackage(inputMap, packageName, 0);
                } else {
                    int approval = approvalLevelForDomain(pkgSetting, uri.getHost(), false, userId, false, uri.getHost());
                    int highestApproval2 = java.lang.Math.max(highestApproval, approval);
                    fillInfoMapForSamePackage(inputMap, packageName, approval);
                    highestApproval = highestApproval2;
                }
            }
        }
        return highestApproval;
    }

    private void fillInfoMapForSamePackage(android.util.ArrayMap<android.content.pm.ResolveInfo, java.lang.Integer> inputMap, java.lang.String targetPackageName, int level) {
        int size = inputMap.size();
        for (int index = 0; index < size; index++) {
            java.lang.String packageName = inputMap.keyAt(index).getComponentInfo().packageName;
            if (java.util.Objects.equals(targetPackageName, packageName)) {
                inputMap.setValueAt(index, java.lang.Integer.valueOf(level));
            }
        }
    }

    private void filterToLastFirstInstalled(android.util.ArrayMap<android.content.pm.ResolveInfo, java.lang.Integer> inputMap, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> pkgSettingFunction) {
        java.lang.String targetPackageName = null;
        long latestInstall = Long.MIN_VALUE;
        int size = inputMap.size();
        for (int index = 0; index < size; index++) {
            android.content.pm.ResolveInfo info = inputMap.keyAt(index);
            java.lang.String packageName = info.getComponentInfo().packageName;
            com.android.server.pm.pkg.PackageStateInternal pkgSetting = pkgSettingFunction.apply(packageName);
            if (pkgSetting != null) {
                long installTime = com.android.server.pm.pkg.PackageStateUtils.getEarliestFirstInstallTime(pkgSetting.getUserStates());
                if (installTime > latestInstall) {
                    latestInstall = installTime;
                    targetPackageName = packageName;
                }
            }
        }
        int index2 = inputMap.size();
        for (int index3 = index2 - 1; index3 >= 0; index3--) {
            android.content.pm.ResolveInfo info2 = inputMap.keyAt(index3);
            if (!java.util.Objects.equals(targetPackageName, info2.getComponentInfo().packageName)) {
                inputMap.removeAt(index3);
            }
        }
    }

    private void filterToLastDeclared(java.util.List<android.content.pm.ResolveInfo> inputList, java.util.function.Function<java.lang.String, com.android.server.pm.pkg.PackageStateInternal> pkgSettingFunction) {
        for (int index = 0; index < inputList.size(); index++) {
            android.content.pm.ResolveInfo info = inputList.get(index);
            java.lang.String targetPackageName = info.getComponentInfo().packageName;
            com.android.server.pm.pkg.PackageStateInternal pkgSetting = pkgSettingFunction.apply(targetPackageName);
            com.android.server.pm.pkg.AndroidPackage pkg = pkgSetting == null ? null : pkgSetting.getPkg();
            if (pkg != null) {
                android.content.pm.ResolveInfo result = info;
                int highestIndex = indexOfIntentFilterEntry(pkg, result);
                int searchIndex = inputList.size();
                while (true) {
                    searchIndex--;
                    if (searchIndex < index + 1) {
                        break;
                    }
                    android.content.pm.ResolveInfo searchInfo = inputList.get(searchIndex);
                    if (java.util.Objects.equals(targetPackageName, searchInfo.getComponentInfo().packageName)) {
                        int entryIndex = indexOfIntentFilterEntry(pkg, searchInfo);
                        if (entryIndex > highestIndex) {
                            highestIndex = entryIndex;
                            result = searchInfo;
                        }
                        inputList.remove(searchIndex);
                    }
                }
                inputList.set(index, result);
            }
        }
    }

    private int indexOfIntentFilterEntry(com.android.server.pm.pkg.AndroidPackage pkg, android.content.pm.ResolveInfo target) {
        java.util.List<com.android.internal.pm.pkg.component.ParsedActivity> activities = pkg.getActivities();
        int activitiesSize = activities.size();
        for (int activityIndex = 0; activityIndex < activitiesSize; activityIndex++) {
            if (java.util.Objects.equals(activities.get(activityIndex).getComponentName(), target.getComponentInfo().getComponentName())) {
                return activityIndex;
            }
        }
        return -1;
    }

    @Override // com.android.server.pm.verify.domain.DomainVerificationManagerInternal
    public int approvalLevelForDomain(com.android.server.pm.pkg.PackageStateInternal pkgSetting, android.content.Intent intent, long resolveInfoFlags, int userId) {
        java.lang.String packageName = pkgSetting.getPackageName();
        boolean debug = (intent.getFlags() & 8) != 0;
        if (!com.android.server.pm.verify.domain.DomainVerificationUtils.isDomainVerificationIntent(intent, resolveInfoFlags)) {
            if (debug) {
                debugApproval(packageName, intent, userId, false, "not valid intent");
            }
            return 0;
        }
        int approvalLevel = approvalLevelForDomain(pkgSetting, intent.getData().getHost(), false, userId, debug, intent);
        if (debug) {
            android.util.Slog.d("DomainVerificationServiceApproval", "Final approval level for " + pkgSetting.getPackageName() + " for host " + intent.getData().getHost() + " is " + approvalLevel);
        }
        return approvalLevel;
    }

    private int approvalLevelForDomain(com.android.server.pm.pkg.PackageStateInternal pkgSetting, java.lang.String host, boolean includeNegative, int userId, boolean debug, java.lang.Object debugObject) {
        int approvalLevel = approvalLevelForDomainInternal(pkgSetting, host, includeNegative, userId, debug, debugObject);
        if (includeNegative && approvalLevel == 0) {
            com.android.server.pm.pkg.PackageUserStateInternal pkgUserState = pkgSetting.getUserStateOrDefault(userId);
            if (!pkgUserState.isInstalled()) {
                return -4;
            }
            com.android.internal.pm.parsing.pkg.AndroidPackageInternal pkg = pkgSetting.getPkg();
            if (pkg != null) {
                if (!com.android.server.pm.pkg.PackageUserStateUtils.isPackageEnabled(pkgUserState, pkg)) {
                    return -3;
                }
                if (this.mCollector.containsAutoVerifyDomain(pkgSetting.getPkg(), host)) {
                    return -1;
                }
            }
        }
        return approvalLevel;
    }

    private int approvalLevelForDomainInternal(com.android.server.pm.pkg.PackageStateInternal pkgSetting, java.lang.String host, boolean includeNegative, int userId, boolean debug, java.lang.Object debugObject) {
        android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap;
        int index;
        int stateMapSize;
        java.lang.String packageName = pkgSetting.getPackageName();
        com.android.server.pm.pkg.AndroidPackage pkg = pkgSetting.getPkg();
        if (pkg != null && includeNegative && !this.mCollector.containsWebDomain(pkg, host)) {
            if (debug) {
                debugApproval(packageName, debugObject, userId, false, "domain not declared");
                return -2;
            }
            return -2;
        }
        com.android.server.pm.pkg.PackageUserStateInternal pkgUserState = pkgSetting.getUserStates().get(userId);
        if (pkgUserState == null) {
            if (debug) {
                debugApproval(packageName, debugObject, userId, false, "PackageUserState unavailable");
            }
            return 0;
        }
        if (!pkgUserState.isInstalled()) {
            if (debug) {
                debugApproval(packageName, debugObject, userId, false, "package not installed for user");
            }
            return 0;
        }
        if (!com.android.server.pm.pkg.PackageUserStateUtils.isPackageEnabled(pkgUserState, pkg)) {
            if (debug) {
                debugApproval(packageName, debugObject, userId, false, "package not enabled for user");
            }
            return 0;
        }
        if (!pkgUserState.isSuspended()) {
            if (pkg != null && !com.android.server.pm.verify.domain.DomainVerificationUtils.isChangeEnabled(this.mPlatformCompat, pkg, SETTINGS_API_V2)) {
                switch (this.mLegacySettings.getUserState(packageName, userId)) {
                    case 1:
                    case 4:
                        return 1;
                    case 2:
                        return 2;
                    case 3:
                        return 0;
                }
            }
            synchronized (this.mLock) {
                com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.get(packageName);
                if (pkgState == null) {
                    if (debug) {
                        debugApproval(packageName, debugObject, userId, false, "pkgState unavailable");
                    }
                    return 0;
                }
                com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState userState = pkgState.getUserState(userId);
                if (userState != null && !userState.isLinkHandlingAllowed()) {
                    if (debug) {
                        debugApproval(packageName, debugObject, userId, false, "link handling not allowed");
                    }
                    return 0;
                }
                if (pkg != null && pkgSetting.getUserStateOrDefault(userId).isInstantApp() && this.mCollector.collectValidAutoVerifyDomains(pkg).contains(host)) {
                    return 5;
                }
                android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap2 = pkgState.getStateMap();
                java.lang.Integer state = stateMap2.get(host);
                if (state == null || !android.content.pm.verify.domain.DomainVerificationState.isVerified(state.intValue())) {
                    android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap3 = stateMap2;
                    int stateMapSize2 = stateMap3.size();
                    int index2 = 0;
                    while (index2 < stateMapSize2) {
                        android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap4 = stateMap3;
                        if (!android.content.pm.verify.domain.DomainVerificationState.isVerified(stateMap4.valueAt(index2).intValue())) {
                            stateMap = stateMap4;
                            index = index2;
                            stateMapSize = stateMapSize2;
                        } else {
                            java.lang.String domain = stateMap4.keyAt(index2);
                            if (domain.startsWith("*.") && host.endsWith(domain.substring(2))) {
                                if (debug) {
                                    debugApproval(packageName, debugObject, userId, true, "host verified by wildcard");
                                }
                                return 4;
                            }
                            stateMap = stateMap4;
                            index = index2;
                            stateMapSize = stateMapSize2;
                        }
                        index2 = index + 1;
                        stateMapSize2 = stateMapSize;
                        stateMap3 = stateMap;
                    }
                    if (userState == null) {
                        if (debug) {
                            debugApproval(packageName, debugObject, userId, false, "userState unavailable");
                        }
                        return 0;
                    }
                    android.util.ArraySet<java.lang.String> enabledHosts = userState.getEnabledHosts();
                    if (enabledHosts.contains(host)) {
                        if (debug) {
                            debugApproval(packageName, debugObject, userId, true, "host enabled by user exactly");
                        }
                        return 3;
                    }
                    android.util.ArraySet<java.lang.String> enabledHosts2 = enabledHosts;
                    int enabledHostsSize = enabledHosts2.size();
                    int index3 = 0;
                    while (index3 < enabledHostsSize) {
                        android.util.ArraySet<java.lang.String> enabledHosts3 = enabledHosts2;
                        java.lang.String domain2 = enabledHosts3.valueAt(index3);
                        if (!domain2.startsWith("*.") || !host.endsWith(domain2.substring(2))) {
                            index3++;
                            enabledHostsSize = enabledHostsSize;
                            enabledHosts2 = enabledHosts3;
                        } else {
                            if (debug) {
                                debugApproval(packageName, debugObject, userId, true, "host enabled by user through wildcard");
                            }
                            return 3;
                        }
                    }
                    if (debug) {
                        debugApproval(packageName, debugObject, userId, false, "not approved");
                    }
                    return 0;
                }
                if (debug) {
                    debugApproval(packageName, debugObject, userId, true, "host verified exactly");
                }
                return 4;
            }
        }
        if (debug) {
            debugApproval(packageName, debugObject, userId, false, "package suspended for user");
        }
        return 0;
    }

    private android.util.Pair<java.util.List<java.lang.String>, java.lang.Integer> getApprovedPackagesLocked(java.lang.String domain, int userId, int minimumApproval, com.android.server.pm.Computer snapshot) {
        int level;
        boolean includeNegative = minimumApproval < 0;
        java.util.List<java.lang.String> approvedPackages = java.util.Collections.emptyList();
        int size = this.mAttachedPkgStates.size();
        int highestApproval = minimumApproval;
        java.util.List<java.lang.String> approvedPackages2 = approvedPackages;
        for (int index = 0; index < size; index++) {
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = this.mAttachedPkgStates.valueAt(index);
            java.lang.String packageName = pkgState.getPackageName();
            com.android.server.pm.pkg.PackageStateInternal pkgSetting = snapshot.getPackageStateInternal(packageName);
            if (pkgSetting != null && (level = approvalLevelForDomain(pkgSetting, domain, includeNegative, userId, false, domain)) >= minimumApproval) {
                if (level > highestApproval) {
                    approvedPackages2.clear();
                    approvedPackages2 = com.android.internal.util.CollectionUtils.add(approvedPackages2, packageName);
                    highestApproval = level;
                } else if (level == highestApproval) {
                    approvedPackages2 = com.android.internal.util.CollectionUtils.add(approvedPackages2, packageName);
                }
            }
        }
        if (approvedPackages2.isEmpty()) {
            return android.util.Pair.create(approvedPackages2, 0);
        }
        java.util.List<java.lang.String> filteredPackages = new java.util.ArrayList<>();
        long latestInstall = Long.MIN_VALUE;
        int approvedSize = approvedPackages2.size();
        for (int index2 = 0; index2 < approvedSize; index2++) {
            java.lang.String packageName2 = approvedPackages2.get(index2);
            com.android.server.pm.pkg.PackageStateInternal pkgSetting2 = snapshot.getPackageStateInternal(packageName2);
            if (pkgSetting2 != null) {
                long installTime = pkgSetting2.getUserStateOrDefault(userId).getFirstInstallTimeMillis();
                if (installTime > latestInstall) {
                    latestInstall = installTime;
                    filteredPackages.clear();
                    filteredPackages.add(packageName2);
                } else if (installTime == latestInstall) {
                    filteredPackages.add(packageName2);
                }
            }
        }
        return android.util.Pair.create(filteredPackages, java.lang.Integer.valueOf(highestApproval));
    }

    private void debugApproval(java.lang.String packageName, java.lang.Object debugObject, int userId, boolean approved, java.lang.String reason) {
        java.lang.String approvalString = approved ? "approved" : "denied";
        android.util.Slog.d("DomainVerificationServiceApproval", packageName + " was " + approvalString + " for " + debugObject + " for user " + userId + ": " + reason);
    }

    private static class GetAttachedResult {
        private final int mErrorCode;
        private final com.android.server.pm.verify.domain.models.DomainVerificationPkgState mPkgState;

        GetAttachedResult(com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState, int errorCode) {
            this.mPkgState = pkgState;
            this.mErrorCode = errorCode;
        }

        static com.android.server.pm.verify.domain.DomainVerificationService.GetAttachedResult error(int errorCode) {
            return new com.android.server.pm.verify.domain.DomainVerificationService.GetAttachedResult(null, errorCode);
        }

        static com.android.server.pm.verify.domain.DomainVerificationService.GetAttachedResult success(com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState) {
            return new com.android.server.pm.verify.domain.DomainVerificationService.GetAttachedResult(pkgState, 0);
        }

        com.android.server.pm.verify.domain.models.DomainVerificationPkgState getPkgState() {
            return this.mPkgState;
        }

        boolean isError() {
            return this.mErrorCode != 0;
        }

        public int getErrorCode() {
            return this.mErrorCode;
        }
    }
}
