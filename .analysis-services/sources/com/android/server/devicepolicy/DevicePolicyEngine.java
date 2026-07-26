package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class DevicePolicyEngine {
    private static final java.lang.String CELLULAR_2G_USER_RESTRICTION_ID = android.app.admin.DevicePolicyIdentifiers.getIdentifierForUserRestriction("no_cellular_2g");
    static final int DEFAULT_POLICY_SIZE_LIMIT = -1;
    static final java.lang.String DEVICE_LOCK_CONTROLLER_ROLE = "android.app.role.SYSTEM_FINANCED_DEVICE_CONTROLLER";
    static final java.lang.String TAG = "DevicePolicyEngine";
    private final android.content.Context mContext;
    private final com.android.server.devicepolicy.DeviceAdminServiceController mDeviceAdminServiceController;
    private final java.lang.Object mLock;
    private final android.os.UserManager mUserManager;
    private int mPolicySizeLimit = -1;
    private final android.util.SparseArray<java.util.Map<android.app.admin.PolicyKey, com.android.server.devicepolicy.PolicyState<?>>> mLocalPolicies = new android.util.SparseArray<>();
    private final java.util.Map<android.app.admin.PolicyKey, com.android.server.devicepolicy.PolicyState<?>> mGlobalPolicies = new java.util.HashMap();
    private final android.util.SparseArray<java.util.Set<com.android.server.devicepolicy.EnforcingAdmin>> mEnforcingAdmins = new android.util.SparseArray<>();
    private final android.util.SparseArray<java.util.HashMap<com.android.server.devicepolicy.EnforcingAdmin, java.lang.Integer>> mAdminPolicySize = new android.util.SparseArray<>();

    DevicePolicyEngine(android.content.Context context, com.android.server.devicepolicy.DeviceAdminServiceController deviceAdminServiceController, java.lang.Object lock) {
        this.mContext = (android.content.Context) java.util.Objects.requireNonNull(context);
        this.mDeviceAdminServiceController = (com.android.server.devicepolicy.DeviceAdminServiceController) java.util.Objects.requireNonNull(deviceAdminServiceController);
        this.mLock = java.util.Objects.requireNonNull(lock);
        this.mUserManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
    }

    private void maybeForceEnforcementRefreshLocked(com.android.server.devicepolicy.PolicyDefinition<?> policyDefinition) {
        try {
            if (shouldForceEnforcementRefresh(policyDefinition)) {
                forceEnforcementRefreshLocked(policyDefinition);
            }
        } catch (java.lang.Throwable e) {
            android.util.Log.e(TAG, "Exception throw during maybeForceEnforcementRefreshLocked", e);
        }
    }

    private boolean shouldForceEnforcementRefresh(com.android.server.devicepolicy.PolicyDefinition<?> policyDefinition) {
        android.app.admin.PolicyKey policyKey;
        if (policyDefinition == null || (policyKey = policyDefinition.getPolicyKey()) == null || !(policyKey instanceof android.app.admin.UserRestrictionPolicyKey)) {
            return false;
        }
        return true;
    }

    private void forceEnforcementRefreshLocked(final com.android.server.devicepolicy.PolicyDefinition<java.lang.Boolean> policyDefinition) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda5
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$forceEnforcementRefreshLocked$0(policyDefinition);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$forceEnforcementRefreshLocked$0(com.android.server.devicepolicy.PolicyDefinition policyDefinition) throws java.lang.Exception {
        android.app.admin.PolicyValue<java.lang.Boolean> globalValue = new android.app.admin.BooleanPolicyValue<>(false);
        try {
            com.android.server.devicepolicy.PolicyState<java.lang.Boolean> policyState = getGlobalPolicyStateLocked(policyDefinition);
            globalValue = policyState.getCurrentResolvedPolicy();
        } catch (java.lang.IllegalArgumentException e) {
        }
        enforcePolicy(policyDefinition, globalValue, -1);
        for (android.content.pm.UserInfo user : this.mUserManager.getUsers()) {
            android.app.admin.PolicyValue<java.lang.Boolean> localValue = new android.app.admin.BooleanPolicyValue<>(false);
            try {
                com.android.server.devicepolicy.PolicyState<java.lang.Boolean> localPolicyState = getLocalPolicyStateLocked(policyDefinition, user.id);
                localValue = localPolicyState.getCurrentResolvedPolicy();
            } catch (java.lang.IllegalArgumentException e2) {
            }
            enforcePolicy(policyDefinition, localValue, user.id);
        }
    }

    <V> void setLocalPolicy(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin, android.app.admin.PolicyValue<V> value, int userId, boolean skipEnforcePolicy) {
        boolean policyChanged;
        java.util.Objects.requireNonNull(policyDefinition);
        java.util.Objects.requireNonNull(enforcingAdmin);
        synchronized (this.mLock) {
            com.android.server.devicepolicy.PolicyState<V> localPolicyState = getLocalPolicyStateLocked(policyDefinition, userId);
            if (!android.app.admin.flags.Flags.devicePolicySizeTrackingInternalBugFixEnabled() || handleAdminPolicySizeLimit(localPolicyState, enforcingAdmin, value, policyDefinition, userId)) {
                if (policyDefinition.isNonCoexistablePolicy()) {
                    setNonCoexistableLocalPolicyLocked(policyDefinition, localPolicyState, enforcingAdmin, value, userId, skipEnforcePolicy);
                    return;
                }
                boolean hasGlobalPolicies = hasGlobalPolicyLocked(policyDefinition);
                if (hasGlobalPolicies) {
                    com.android.server.devicepolicy.PolicyState<V> globalPolicyState = getGlobalPolicyStateLocked(policyDefinition);
                    policyChanged = localPolicyState.addPolicy(enforcingAdmin, value, globalPolicyState.getPoliciesSetByAdmins());
                } else {
                    policyChanged = localPolicyState.addPolicy(enforcingAdmin, value);
                }
                if (!skipEnforcePolicy) {
                    maybeForceEnforcementRefreshLocked(policyDefinition);
                    if (policyChanged) {
                        onLocalPolicyChangedLocked(policyDefinition, enforcingAdmin, userId);
                    }
                    boolean policyEnforced = java.util.Objects.equals(localPolicyState.getCurrentResolvedPolicy(), value);
                    int i = 0;
                    if (!policyEnforced && shouldApplyPackageSetUnionPolicyHack(policyDefinition)) {
                        android.app.admin.PolicyValue<V> currentResolvedPolicy = localPolicyState.getCurrentResolvedPolicy();
                        policyEnforced = (currentResolvedPolicy == null || value == null || !((java.util.Set) currentResolvedPolicy.getValue()).containsAll((java.util.Collection) value.getValue())) ? false : true;
                    }
                    if (!policyEnforced) {
                        i = 1;
                    }
                    sendPolicyResultToAdmin(enforcingAdmin, policyDefinition, i, userId);
                }
                updateDeviceAdminServiceOnPolicyAddLocked(enforcingAdmin);
                write();
                applyToInheritableProfiles(policyDefinition, enforcingAdmin, value, userId);
            }
        }
    }

    private <V> void setNonCoexistableLocalPolicyLocked(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, com.android.server.devicepolicy.PolicyState<V> localPolicyState, com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin, android.app.admin.PolicyValue<V> value, int userId, boolean skipEnforcePolicy) {
        if (value == null) {
            localPolicyState.removePolicy(enforcingAdmin);
        } else {
            localPolicyState.addPolicy(enforcingAdmin, value);
        }
        if (!skipEnforcePolicy) {
            enforcePolicy(policyDefinition, value, userId);
        }
        if (localPolicyState.getPoliciesSetByAdmins().isEmpty()) {
            removeLocalPolicyStateLocked(policyDefinition, userId);
        }
        updateDeviceAdminServiceOnPolicyAddLocked(enforcingAdmin);
        write();
        applyToInheritableProfiles(policyDefinition, enforcingAdmin, value, userId);
    }

    <V> void setLocalPolicy(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin, android.app.admin.PolicyValue<V> value, int userId) {
        setLocalPolicy(policyDefinition, enforcingAdmin, value, userId, false);
    }

    <V> void removeLocalPolicy(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin, int userId) {
        boolean policyChanged;
        java.util.Objects.requireNonNull(policyDefinition);
        java.util.Objects.requireNonNull(enforcingAdmin);
        synchronized (this.mLock) {
            maybeForceEnforcementRefreshLocked(policyDefinition);
            if (hasLocalPolicyLocked(policyDefinition, userId)) {
                com.android.server.devicepolicy.PolicyState<V> localPolicyState = getLocalPolicyStateLocked(policyDefinition, userId);
                if (android.app.admin.flags.Flags.devicePolicySizeTrackingInternalBugFixEnabled()) {
                    decreasePolicySizeForAdmin(localPolicyState, enforcingAdmin);
                }
                if (policyDefinition.isNonCoexistablePolicy()) {
                    setNonCoexistableLocalPolicyLocked(policyDefinition, localPolicyState, enforcingAdmin, null, userId, false);
                    return;
                }
                if (hasGlobalPolicyLocked(policyDefinition)) {
                    com.android.server.devicepolicy.PolicyState<V> globalPolicyState = getGlobalPolicyStateLocked(policyDefinition);
                    policyChanged = localPolicyState.removePolicy(enforcingAdmin, globalPolicyState.getPoliciesSetByAdmins());
                } else {
                    policyChanged = localPolicyState.removePolicy(enforcingAdmin);
                }
                if (policyChanged) {
                    onLocalPolicyChangedLocked(policyDefinition, enforcingAdmin, userId);
                }
                sendPolicyResultToAdmin(enforcingAdmin, policyDefinition, 2, userId);
                if (localPolicyState.getPoliciesSetByAdmins().isEmpty()) {
                    removeLocalPolicyStateLocked(policyDefinition, userId);
                }
                updateDeviceAdminServiceOnPolicyRemoveLocked(enforcingAdmin);
                write();
                applyToInheritableProfiles(policyDefinition, enforcingAdmin, null, userId);
            }
        }
    }

    private <V> void applyToInheritableProfiles(final com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, final com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin, final android.app.admin.PolicyValue<V> value, final int userId) {
        if (policyDefinition.isInheritable()) {
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda3
                public final void runOrThrow() throws java.lang.Exception {
                    this.f$0.lambda$applyToInheritableProfiles$1(userId, value, policyDefinition, enforcingAdmin);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$applyToInheritableProfiles$1(int userId, android.app.admin.PolicyValue value, com.android.server.devicepolicy.PolicyDefinition policyDefinition, com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin) throws java.lang.Exception {
        java.util.List<android.content.pm.UserInfo> userInfos = this.mUserManager.getProfiles(userId);
        for (android.content.pm.UserInfo childUserInfo : userInfos) {
            int childUserId = childUserInfo.getUserHandle().getIdentifier();
            if (isProfileOfUser(childUserId, userId) && isInheritDevicePolicyFromParent(childUserInfo)) {
                if (value != null) {
                    setLocalPolicy(policyDefinition, enforcingAdmin, value, childUserId);
                } else {
                    removeLocalPolicy(policyDefinition, enforcingAdmin, childUserId);
                }
            }
        }
    }

    private boolean isProfileOfUser(int childUserId, int parentUserId) {
        android.content.pm.UserInfo parentInfo = this.mUserManager.getProfileParent(childUserId);
        return (childUserId == parentUserId || parentInfo == null || parentInfo.getUserHandle().getIdentifier() != parentUserId) ? false : true;
    }

    private boolean isInheritDevicePolicyFromParent(android.content.pm.UserInfo userInfo) {
        android.content.pm.UserProperties userProperties = this.mUserManager.getUserProperties(userInfo.getUserHandle());
        return userProperties != null && this.mUserManager.getUserProperties(userInfo.getUserHandle()).getInheritDevicePolicy() == 1;
    }

    private <V> void onLocalPolicyChangedLocked(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin, int userId) {
        com.android.server.devicepolicy.PolicyState<V> localPolicyState = getLocalPolicyStateLocked(policyDefinition, userId);
        enforcePolicy(policyDefinition, localPolicyState.getCurrentResolvedPolicy(), userId);
        sendPolicyChangedToAdminsLocked(localPolicyState, enforcingAdmin, policyDefinition, userId);
        if (hasGlobalPolicyLocked(policyDefinition)) {
            com.android.server.devicepolicy.PolicyState<V> globalPolicyState = getGlobalPolicyStateLocked(policyDefinition);
            sendPolicyChangedToAdminsLocked(globalPolicyState, enforcingAdmin, policyDefinition, userId);
        }
        sendDevicePolicyChangedToSystem(userId);
    }

    <V> void setGlobalPolicy(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin, android.app.admin.PolicyValue<V> value) {
        setGlobalPolicy(policyDefinition, enforcingAdmin, value, false);
    }

    <V> void setGlobalPolicy(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin, android.app.admin.PolicyValue<V> value, boolean skipEnforcePolicy) {
        java.util.Objects.requireNonNull(policyDefinition);
        java.util.Objects.requireNonNull(enforcingAdmin);
        java.util.Objects.requireNonNull(value);
        synchronized (this.mLock) {
            com.android.server.devicepolicy.PolicyState<V> globalPolicyState = getGlobalPolicyStateLocked(policyDefinition);
            if (!android.app.admin.flags.Flags.devicePolicySizeTrackingInternalBugFixEnabled() || handleAdminPolicySizeLimit(globalPolicyState, enforcingAdmin, value, policyDefinition, -1)) {
                if (checkFor2gFailure(policyDefinition, enforcingAdmin)) {
                    android.util.Log.i(TAG, "Device does not support capabilities required to disable 2g. Not setting global policy state.");
                    return;
                }
                boolean policyChanged = globalPolicyState.addPolicy(enforcingAdmin, value);
                boolean policyAppliedOnAllUsers = applyGlobalPolicyOnUsersWithLocalPoliciesLocked(policyDefinition, enforcingAdmin, value, skipEnforcePolicy);
                if (!skipEnforcePolicy) {
                    maybeForceEnforcementRefreshLocked(policyDefinition);
                    if (policyChanged) {
                        onGlobalPolicyChangedLocked(policyDefinition, enforcingAdmin);
                    }
                    boolean policyAppliedGlobally = java.util.Objects.equals(globalPolicyState.getCurrentResolvedPolicy(), value);
                    if (!policyAppliedGlobally && shouldApplyPackageSetUnionPolicyHack(policyDefinition)) {
                        android.app.admin.PolicyValue<V> currentResolvedPolicy = globalPolicyState.getCurrentResolvedPolicy();
                        policyAppliedGlobally = (currentResolvedPolicy == null || value == null || !((java.util.Set) currentResolvedPolicy.getValue()).containsAll((java.util.Collection) value.getValue())) ? false : true;
                    }
                    boolean policyApplied = policyAppliedGlobally && policyAppliedOnAllUsers;
                    sendPolicyResultToAdmin(enforcingAdmin, policyDefinition, policyApplied ? 0 : 1, -1);
                }
                updateDeviceAdminServiceOnPolicyAddLocked(enforcingAdmin);
                write();
            }
        }
    }

    <V> void removeGlobalPolicy(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin) {
        java.util.Objects.requireNonNull(policyDefinition);
        java.util.Objects.requireNonNull(enforcingAdmin);
        synchronized (this.mLock) {
            com.android.server.devicepolicy.PolicyState<V> policyState = getGlobalPolicyStateLocked(policyDefinition);
            if (android.app.admin.flags.Flags.devicePolicySizeTrackingInternalBugFixEnabled()) {
                decreasePolicySizeForAdmin(policyState, enforcingAdmin);
            }
            boolean policyChanged = policyState.removePolicy(enforcingAdmin);
            maybeForceEnforcementRefreshLocked(policyDefinition);
            if (policyChanged) {
                onGlobalPolicyChangedLocked(policyDefinition, enforcingAdmin);
            }
            applyGlobalPolicyOnUsersWithLocalPoliciesLocked(policyDefinition, enforcingAdmin, null, false);
            sendPolicyResultToAdmin(enforcingAdmin, policyDefinition, 2, -1);
            if (policyState.getPoliciesSetByAdmins().isEmpty()) {
                removeGlobalPolicyStateLocked(policyDefinition);
            }
            updateDeviceAdminServiceOnPolicyRemoveLocked(enforcingAdmin);
            write();
        }
    }

    private <V> void onGlobalPolicyChangedLocked(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin) {
        com.android.server.devicepolicy.PolicyState<V> policyState = getGlobalPolicyStateLocked(policyDefinition);
        enforcePolicy(policyDefinition, policyState.getCurrentResolvedPolicy(), -1);
        sendPolicyChangedToAdminsLocked(policyState, enforcingAdmin, policyDefinition, -1);
        sendDevicePolicyChangedToSystem(-1);
    }

    private <V> boolean applyGlobalPolicyOnUsersWithLocalPoliciesLocked(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin, android.app.admin.PolicyValue<V> value, boolean skipEnforcePolicy) {
        if (policyDefinition.isGlobalOnlyPolicy()) {
            return true;
        }
        boolean isAdminPolicyApplied = true;
        for (int i = 0; i < this.mLocalPolicies.size(); i++) {
            int userId = this.mLocalPolicies.keyAt(i);
            if (hasLocalPolicyLocked(policyDefinition, userId)) {
                com.android.server.devicepolicy.PolicyState<V> localPolicyState = getLocalPolicyStateLocked(policyDefinition, userId);
                com.android.server.devicepolicy.PolicyState<V> globalPolicyState = getGlobalPolicyStateLocked(policyDefinition);
                boolean policyChanged = localPolicyState.resolvePolicy(globalPolicyState.getPoliciesSetByAdmins());
                if (policyChanged && !skipEnforcePolicy) {
                    enforcePolicy(policyDefinition, localPolicyState.getCurrentResolvedPolicy(), userId);
                    sendPolicyChangedToAdminsLocked(localPolicyState, enforcingAdmin, policyDefinition, userId);
                }
                if (shouldApplyPackageSetUnionPolicyHack(policyDefinition)) {
                    if (!java.util.Objects.equals(value, localPolicyState.getCurrentResolvedPolicy())) {
                        android.app.admin.PolicyValue<V> currentResolvedPolicy = localPolicyState.getCurrentResolvedPolicy();
                        isAdminPolicyApplied &= (currentResolvedPolicy == null || value == null || !((java.util.Set) currentResolvedPolicy.getValue()).containsAll((java.util.Collection) value.getValue())) ? false : true;
                    }
                } else {
                    isAdminPolicyApplied &= java.util.Objects.equals(value, localPolicyState.getCurrentResolvedPolicy());
                }
            }
        }
        return isAdminPolicyApplied;
    }

    <V> V getResolvedPolicy(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, int i) {
        android.app.admin.PolicyValue<V> resolvedPolicyValue = getResolvedPolicyValue(policyDefinition, i);
        if (resolvedPolicyValue == null) {
            return null;
        }
        return (V) resolvedPolicyValue.getValue();
    }

    private <V> android.app.admin.PolicyValue<V> getResolvedPolicyValue(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, int userId) {
        android.app.admin.PolicyValue<V> resolvedValue;
        java.util.Objects.requireNonNull(policyDefinition);
        synchronized (this.mLock) {
            resolvedValue = null;
            if (hasLocalPolicyLocked(policyDefinition, userId)) {
                resolvedValue = getLocalPolicyStateLocked(policyDefinition, userId).getCurrentResolvedPolicy();
            } else if (hasGlobalPolicyLocked(policyDefinition)) {
                resolvedValue = getGlobalPolicyStateLocked(policyDefinition).getCurrentResolvedPolicy();
            }
        }
        return resolvedValue;
    }

    <V> V getResolvedPolicyAcrossUsers(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, java.util.List<java.lang.Integer> list) {
        java.util.Objects.requireNonNull(policyDefinition);
        java.util.ArrayList arrayList = new java.util.ArrayList();
        synchronized (this.mLock) {
            java.util.Iterator<java.lang.Integer> it = list.iterator();
            while (it.hasNext()) {
                android.app.admin.PolicyValue<V> resolvedPolicyValue = getResolvedPolicyValue(policyDefinition, it.next().intValue());
                if (resolvedPolicyValue != null) {
                    arrayList.add(resolvedPolicyValue);
                }
            }
        }
        android.app.admin.PolicyValue<V> policyValueResolve = policyDefinition.getResolutionMechanism().resolve(arrayList);
        if (policyValueResolve == null) {
            return null;
        }
        return (V) policyValueResolve.getValue();
    }

    <V> V getLocalPolicySetByAdmin(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin, int i) {
        java.util.Objects.requireNonNull(policyDefinition);
        java.util.Objects.requireNonNull(enforcingAdmin);
        synchronized (this.mLock) {
            V v = null;
            if (!hasLocalPolicyLocked(policyDefinition, i)) {
                return null;
            }
            android.app.admin.PolicyValue<V> policyValue = getLocalPolicyStateLocked(policyDefinition, i).getPoliciesSetByAdmins().get(enforcingAdmin);
            if (policyValue != null) {
                v = (V) policyValue.getValue();
            }
            return v;
        }
    }

    <V> V getGlobalPolicySetByAdmin(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin) {
        java.util.Objects.requireNonNull(policyDefinition);
        java.util.Objects.requireNonNull(enforcingAdmin);
        synchronized (this.mLock) {
            V v = null;
            if (!hasGlobalPolicyLocked(policyDefinition)) {
                return null;
            }
            android.app.admin.PolicyValue<V> policyValue = getGlobalPolicyStateLocked(policyDefinition).getPoliciesSetByAdmins().get(enforcingAdmin);
            if (policyValue != null) {
                v = (V) policyValue.getValue();
            }
            return v;
        }
    }

    <V> java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> getLocalPoliciesSetByAdmins(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, int userId) {
        java.util.Objects.requireNonNull(policyDefinition);
        synchronized (this.mLock) {
            if (!hasLocalPolicyLocked(policyDefinition, userId)) {
                return new java.util.LinkedHashMap<>();
            }
            return getLocalPolicyStateLocked(policyDefinition, userId).getPoliciesSetByAdmins();
        }
    }

    <V> java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> getGlobalPoliciesSetByAdmins(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition) {
        java.util.Objects.requireNonNull(policyDefinition);
        synchronized (this.mLock) {
            if (!hasGlobalPolicyLocked(policyDefinition)) {
                return new java.util.LinkedHashMap<>();
            }
            return getGlobalPolicyStateLocked(policyDefinition).getPoliciesSetByAdmins();
        }
    }

    <V> java.util.Set<android.app.admin.PolicyKey> getLocalPolicyKeysSetByAdmin(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin, int userId) {
        java.util.Objects.requireNonNull(policyDefinition);
        java.util.Objects.requireNonNull(enforcingAdmin);
        synchronized (this.mLock) {
            if (!policyDefinition.isGlobalOnlyPolicy() && this.mLocalPolicies.contains(userId)) {
                java.util.Set<android.app.admin.PolicyKey> keys = new java.util.HashSet<>();
                for (android.app.admin.PolicyKey key : this.mLocalPolicies.get(userId).keySet()) {
                    if (key.hasSameIdentifierAs(policyDefinition.getPolicyKey()) && this.mLocalPolicies.get(userId).get(key).getPoliciesSetByAdmins().containsKey(enforcingAdmin)) {
                        keys.add(key);
                    }
                }
                return keys;
            }
            return java.util.Set.of();
        }
    }

    <V> java.util.Set<android.app.admin.PolicyKey> getLocalPolicyKeysSetByAllAdmins(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, int userId) {
        java.util.Objects.requireNonNull(policyDefinition);
        synchronized (this.mLock) {
            if (!policyDefinition.isGlobalOnlyPolicy() && this.mLocalPolicies.contains(userId)) {
                java.util.Set<android.app.admin.PolicyKey> keys = new java.util.HashSet<>();
                for (android.app.admin.PolicyKey key : this.mLocalPolicies.get(userId).keySet()) {
                    if (key.hasSameIdentifierAs(policyDefinition.getPolicyKey())) {
                        keys.add(key);
                    }
                }
                return keys;
            }
            return java.util.Set.of();
        }
    }

    java.util.Set<android.app.admin.UserRestrictionPolicyKey> getUserRestrictionPolicyKeysForAdmin(com.android.server.devicepolicy.EnforcingAdmin admin, int userId) {
        java.util.Objects.requireNonNull(admin);
        synchronized (this.mLock) {
            if (userId == -1) {
                return getUserRestrictionPolicyKeysForAdminLocked(this.mGlobalPolicies, admin);
            }
            if (!this.mLocalPolicies.contains(userId)) {
                return java.util.Set.of();
            }
            return getUserRestrictionPolicyKeysForAdminLocked(this.mLocalPolicies.get(userId), admin);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    <V> void transferPolicies(com.android.server.devicepolicy.EnforcingAdmin oldAdmin, com.android.server.devicepolicy.EnforcingAdmin newAdmin) {
        synchronized (this.mLock) {
            java.util.Set<android.app.admin.PolicyKey> globalPolicies = new java.util.HashSet<>(this.mGlobalPolicies.keySet());
            for (android.app.admin.PolicyKey policy : globalPolicies) {
                com.android.server.devicepolicy.PolicyState<?> policyState = this.mGlobalPolicies.get(policy);
                if (policyState.getPoliciesSetByAdmins().containsKey(oldAdmin)) {
                    com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition = policyState.getPolicyDefinition();
                    android.app.admin.PolicyValue<?> policyValue = policyState.getPoliciesSetByAdmins().get(oldAdmin);
                    setGlobalPolicy(policyDefinition, newAdmin, policyValue);
                }
            }
            for (int i = 0; i < this.mLocalPolicies.size(); i++) {
                int userId = this.mLocalPolicies.keyAt(i);
                java.util.Set<android.app.admin.PolicyKey> localPolicies = new java.util.HashSet<>(this.mLocalPolicies.get(userId).keySet());
                for (android.app.admin.PolicyKey policy2 : localPolicies) {
                    com.android.server.devicepolicy.PolicyState<?> policyState2 = this.mLocalPolicies.get(userId).get(policy2);
                    if (policyState2.getPoliciesSetByAdmins().containsKey(oldAdmin)) {
                        com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition2 = policyState2.getPolicyDefinition();
                        android.app.admin.PolicyValue<?> policyValue2 = policyState2.getPoliciesSetByAdmins().get(oldAdmin);
                        setLocalPolicy(policyDefinition2, newAdmin, policyValue2, userId);
                    }
                }
            }
        }
        removePoliciesForAdmin(oldAdmin);
    }

    private java.util.Set<android.app.admin.UserRestrictionPolicyKey> getUserRestrictionPolicyKeysForAdminLocked(java.util.Map<android.app.admin.PolicyKey, com.android.server.devicepolicy.PolicyState<?>> policies, com.android.server.devicepolicy.EnforcingAdmin admin) {
        android.app.admin.PolicyValue<?> policyValue;
        java.util.Set<android.app.admin.UserRestrictionPolicyKey> keys = new java.util.HashSet<>();
        java.util.Iterator<android.app.admin.PolicyKey> it = policies.keySet().iterator();
        while (it.hasNext()) {
            android.app.admin.UserRestrictionPolicyKey userRestrictionPolicyKey = (android.app.admin.PolicyKey) it.next();
            if (policies.get(userRestrictionPolicyKey).getPolicyDefinition().isUserRestrictionPolicy() && (policyValue = policies.get(userRestrictionPolicyKey).getPoliciesSetByAdmins().get(admin)) != null && ((java.lang.Boolean) policyValue.getValue()).booleanValue()) {
                keys.add(userRestrictionPolicyKey);
            }
        }
        return keys;
    }

    private <V> boolean hasLocalPolicyLocked(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, int userId) {
        if (!policyDefinition.isGlobalOnlyPolicy() && this.mLocalPolicies.contains(userId) && this.mLocalPolicies.get(userId).containsKey(policyDefinition.getPolicyKey())) {
            return !this.mLocalPolicies.get(userId).get(policyDefinition.getPolicyKey()).getPoliciesSetByAdmins().isEmpty();
        }
        return false;
    }

    private <V> boolean hasGlobalPolicyLocked(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition) {
        if (!policyDefinition.isLocalOnlyPolicy() && this.mGlobalPolicies.containsKey(policyDefinition.getPolicyKey())) {
            return !this.mGlobalPolicies.get(policyDefinition.getPolicyKey()).getPoliciesSetByAdmins().isEmpty();
        }
        return false;
    }

    private <V> com.android.server.devicepolicy.PolicyState<V> getLocalPolicyStateLocked(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, int userId) {
        if (policyDefinition.isGlobalOnlyPolicy()) {
            throw new java.lang.IllegalArgumentException(policyDefinition.getPolicyKey() + " is a global only policy.");
        }
        if (!this.mLocalPolicies.contains(userId)) {
            this.mLocalPolicies.put(userId, new java.util.HashMap());
        }
        if (!this.mLocalPolicies.get(userId).containsKey(policyDefinition.getPolicyKey())) {
            this.mLocalPolicies.get(userId).put(policyDefinition.getPolicyKey(), new com.android.server.devicepolicy.PolicyState<>(policyDefinition));
        }
        return getPolicyStateLocked(this.mLocalPolicies.get(userId), policyDefinition);
    }

    private <V> void removeLocalPolicyStateLocked(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, int userId) {
        if (!this.mLocalPolicies.contains(userId)) {
            return;
        }
        this.mLocalPolicies.get(userId).remove(policyDefinition.getPolicyKey());
    }

    private <V> com.android.server.devicepolicy.PolicyState<V> getGlobalPolicyStateLocked(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition) {
        if (policyDefinition.isLocalOnlyPolicy()) {
            throw new java.lang.IllegalArgumentException(policyDefinition.getPolicyKey() + " is a local only policy.");
        }
        if (!this.mGlobalPolicies.containsKey(policyDefinition.getPolicyKey())) {
            this.mGlobalPolicies.put(policyDefinition.getPolicyKey(), new com.android.server.devicepolicy.PolicyState<>(policyDefinition));
        }
        return getPolicyStateLocked(this.mGlobalPolicies, policyDefinition);
    }

    private <V> void removeGlobalPolicyStateLocked(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition) {
        this.mGlobalPolicies.remove(policyDefinition.getPolicyKey());
    }

    private static <V> com.android.server.devicepolicy.PolicyState<V> getPolicyStateLocked(java.util.Map<android.app.admin.PolicyKey, com.android.server.devicepolicy.PolicyState<?>> policies, com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition) {
        try {
            com.android.server.devicepolicy.PolicyState<V> policyState = (com.android.server.devicepolicy.PolicyState) policies.get(policyDefinition.getPolicyKey());
            return policyState;
        } catch (java.lang.ClassCastException e) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <V> void enforcePolicy(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, android.app.admin.PolicyValue<V> policyValue, int userId) {
        policyDefinition.enforcePolicy(policyValue == null ? null : policyValue.getValue(), this.mContext, userId);
    }

    private void sendDevicePolicyChangedToSystem(final int userId) {
        final android.content.Intent intent = new android.content.Intent("android.app.action.DEVICE_POLICY_MANAGER_STATE_CHANGED");
        intent.setFlags(1073741824);
        final android.os.Bundle options = new android.app.BroadcastOptions().setDeliveryGroupPolicy(1).setDeferralPolicy(2).toBundle();
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda0
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$sendDevicePolicyChangedToSystem$2(intent, userId, options);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendDevicePolicyChangedToSystem$2(android.content.Intent intent, int userId, android.os.Bundle options) throws java.lang.Exception {
        this.mContext.sendBroadcastAsUser(intent, new android.os.UserHandle(userId), null, options);
    }

    private <V> void sendPolicyResultToAdmin(final com.android.server.devicepolicy.EnforcingAdmin admin, final com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, final int result, final int userId) {
        final android.content.Intent intent = new android.content.Intent("android.app.admin.action.DEVICE_POLICY_SET_RESULT");
        intent.setPackage(admin.getPackageName());
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda1
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$sendPolicyResultToAdmin$3(intent, admin, policyDefinition, userId, result);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendPolicyResultToAdmin$3(android.content.Intent intent, com.android.server.devicepolicy.EnforcingAdmin admin, com.android.server.devicepolicy.PolicyDefinition policyDefinition, int userId, int result) throws java.lang.Exception {
        java.util.List receivers = this.mContext.getPackageManager().queryBroadcastReceiversAsUser(intent, android.content.pm.PackageManager.ResolveInfoFlags.of(2L), admin.getUserId());
        if (receivers.isEmpty()) {
            android.util.Log.i(TAG, "Couldn't find any receivers that handle ACTION_DEVICE_POLICY_SET_RESULT in package " + admin.getPackageName());
            return;
        }
        android.os.Bundle extras = new android.os.Bundle();
        policyDefinition.getPolicyKey().writeToBundle(extras);
        extras.putInt("android.app.admin.extra.POLICY_TARGET_USER_ID", getTargetUser(admin.getUserId(), userId));
        extras.putInt("android.app.admin.extra.POLICY_UPDATE_RESULT_KEY", result);
        intent.putExtras(extras);
        maybeSendIntentToAdminReceivers(intent, android.os.UserHandle.of(admin.getUserId()), receivers);
    }

    private <V> void sendPolicyChangedToAdminsLocked(com.android.server.devicepolicy.PolicyState<V> policyState, com.android.server.devicepolicy.EnforcingAdmin callingAdmin, com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, int userId) {
        for (com.android.server.devicepolicy.EnforcingAdmin admin : policyState.getPoliciesSetByAdmins().keySet()) {
            if (!admin.equals(callingAdmin)) {
                int result = java.util.Objects.equals(policyState.getPoliciesSetByAdmins().get(admin), policyState.getCurrentResolvedPolicy()) ? 0 : 1;
                maybeSendOnPolicyChanged(admin, policyDefinition, result, userId);
            }
        }
    }

    private <V> void maybeSendOnPolicyChanged(final com.android.server.devicepolicy.EnforcingAdmin admin, final com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, final int reason, final int userId) {
        final android.content.Intent intent = new android.content.Intent("android.app.admin.action.DEVICE_POLICY_CHANGED");
        intent.setPackage(admin.getPackageName());
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda6
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$maybeSendOnPolicyChanged$4(intent, admin, policyDefinition, userId, reason);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$maybeSendOnPolicyChanged$4(android.content.Intent intent, com.android.server.devicepolicy.EnforcingAdmin admin, com.android.server.devicepolicy.PolicyDefinition policyDefinition, int userId, int reason) throws java.lang.Exception {
        java.util.List receivers = this.mContext.getPackageManager().queryBroadcastReceiversAsUser(intent, android.content.pm.PackageManager.ResolveInfoFlags.of(2L), admin.getUserId());
        if (receivers.isEmpty()) {
            android.util.Log.i(TAG, "Couldn't find any receivers that handle ACTION_DEVICE_POLICY_CHANGED in package " + admin.getPackageName());
            return;
        }
        android.os.Bundle extras = new android.os.Bundle();
        policyDefinition.getPolicyKey().writeToBundle(extras);
        extras.putInt("android.app.admin.extra.POLICY_TARGET_USER_ID", getTargetUser(admin.getUserId(), userId));
        extras.putInt("android.app.admin.extra.POLICY_UPDATE_RESULT_KEY", reason);
        intent.putExtras(extras);
        intent.addFlags(268435456);
        maybeSendIntentToAdminReceivers(intent, android.os.UserHandle.of(admin.getUserId()), receivers);
    }

    private void maybeSendIntentToAdminReceivers(android.content.Intent intent, android.os.UserHandle userHandle, java.util.List<android.content.pm.ResolveInfo> receivers) {
        for (android.content.pm.ResolveInfo resolveInfo : receivers) {
            if (!"android.permission.BIND_DEVICE_ADMIN".equals(resolveInfo.activityInfo.permission)) {
                android.util.Log.w(TAG, "Receiver " + resolveInfo.activityInfo + " is not protected by BIND_DEVICE_ADMIN permission!");
            } else {
                this.mContext.sendBroadcastAsUser(intent, userHandle);
            }
        }
    }

    private int getTargetUser(int adminUserId, int targetUserId) {
        if (targetUserId == -1) {
            return -3;
        }
        if (adminUserId == targetUserId) {
            return -1;
        }
        if (getProfileParentId(adminUserId) != targetUserId) {
            return -3;
        }
        return -2;
    }

    private int getProfileParentId(final int userId) {
        return ((java.lang.Integer) android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingSupplier() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda2
            public final java.lang.Object getOrThrow() {
                return this.f$0.lambda$getProfileParentId$5(userId);
            }
        })).intValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Integer lambda$getProfileParentId$5(int userId) throws java.lang.Exception {
        android.content.pm.UserInfo parentUser = this.mUserManager.getProfileParent(userId);
        return java.lang.Integer.valueOf(parentUser != null ? parentUser.id : userId);
    }

    private void updateDeviceAdminsServicesForUser(int userId, boolean enable, java.lang.String actionForLog) {
        if (!enable) {
            this.mDeviceAdminServiceController.stopServicesForUser(userId, actionForLog);
            return;
        }
        for (com.android.server.devicepolicy.EnforcingAdmin admin : getEnforcingAdminsOnUser(userId)) {
            if (!admin.hasAuthority("enterprise")) {
                this.mDeviceAdminServiceController.startServiceForAdmin(admin.getPackageName(), userId, actionForLog);
            }
        }
    }

    void handleStartUser(int userId) {
        updateDeviceAdminsServicesForUser(userId, true, "start-user");
    }

    void handleUnlockUser(int userId) {
        updateDeviceAdminsServicesForUser(userId, true, "unlock-user");
    }

    void handleStopUser(int userId) {
        updateDeviceAdminsServicesForUser(userId, false, "stop-user");
    }

    void handlePackageChanged(final java.lang.String updatedPackage, final int userId, final java.lang.String removedDpcPackage) {
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda7
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$handlePackageChanged$6(userId, removedDpcPackage, updatedPackage);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$handlePackageChanged$6(int userId, java.lang.String removedDpcPackage, java.lang.String updatedPackage) throws java.lang.Exception {
        java.util.Set<com.android.server.devicepolicy.EnforcingAdmin> admins = getEnforcingAdminsOnUser(userId);
        if (removedDpcPackage != null) {
            for (com.android.server.devicepolicy.EnforcingAdmin admin : admins) {
                if (removedDpcPackage.equals(admin.getPackageName())) {
                    removePoliciesForAdmin(admin);
                    return;
                }
            }
        }
        for (com.android.server.devicepolicy.EnforcingAdmin admin2 : admins) {
            if (updatedPackage == null || updatedPackage.equals(admin2.getPackageName())) {
                if (!isPackageInstalled(admin2.getPackageName(), userId)) {
                    com.android.server.utils.Slogf.i(TAG, java.lang.String.format("Admin package %s not found for user %d, removing admin policies", admin2.getPackageName(), java.lang.Integer.valueOf(userId)));
                    removePoliciesForAdmin(admin2);
                    return;
                }
            }
        }
        if (updatedPackage != null) {
            updateDeviceAdminServiceOnPackageChanged(updatedPackage, userId);
            removePersistentPreferredActivityPoliciesForPackage(updatedPackage, userId);
        }
    }

    private void removePersistentPreferredActivityPoliciesForPackage(java.lang.String packageName, int userId) {
        java.util.Set<android.app.admin.PolicyKey> policyKeys;
        java.util.Iterator<android.app.admin.PolicyKey> it;
        java.util.Set<android.app.admin.PolicyKey> policyKeys2 = getLocalPolicyKeysSetByAllAdmins(com.android.server.devicepolicy.PolicyDefinition.GENERIC_PERSISTENT_PREFERRED_ACTIVITY, userId);
        java.util.Iterator<android.app.admin.PolicyKey> it2 = policyKeys2.iterator();
        while (it2.hasNext()) {
            android.app.admin.IntentFilterPolicyKey intentFilterPolicyKey = (android.app.admin.PolicyKey) it2.next();
            if (!(intentFilterPolicyKey instanceof android.app.admin.IntentFilterPolicyKey)) {
                throw new java.lang.IllegalStateException("PolicyKey for PERSISTENT_PREFERRED_ACTIVITY is not of type IntentFilterPolicyKey");
            }
            android.app.admin.IntentFilterPolicyKey parsedKey = intentFilterPolicyKey;
            android.content.IntentFilter intentFilter = (android.content.IntentFilter) java.util.Objects.requireNonNull(parsedKey.getIntentFilter());
            com.android.server.devicepolicy.PolicyDefinition<android.content.ComponentName> policyDefinition = com.android.server.devicepolicy.PolicyDefinition.PERSISTENT_PREFERRED_ACTIVITY(intentFilter);
            java.util.LinkedHashMap<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<android.content.ComponentName>> policies = getLocalPoliciesSetByAdmins(policyDefinition, userId);
            android.content.pm.IPackageManager packageManager = android.app.AppGlobals.getPackageManager();
            for (com.android.server.devicepolicy.EnforcingAdmin admin : policies.keySet()) {
                if (policies.get(admin).getValue() == null) {
                    policyKeys = policyKeys2;
                    it = it2;
                } else if (!((android.content.ComponentName) policies.get(admin).getValue()).getPackageName().equals(packageName)) {
                    policyKeys = policyKeys2;
                    it = it2;
                } else {
                    policyKeys = policyKeys2;
                    it = it2;
                    try {
                        if (packageManager.getPackageInfo(packageName, 0L, userId) == null || packageManager.getActivityInfo((android.content.ComponentName) policies.get(admin).getValue(), 0L, userId) == null) {
                            com.android.server.utils.Slogf.e(TAG, java.lang.String.format("Persistent preferred activity in package %s not found for user %d, removing policy for admin", packageName, java.lang.Integer.valueOf(userId)));
                            removeLocalPolicy(policyDefinition, admin, userId);
                        }
                    } catch (android.os.RemoteException re) {
                        com.android.server.utils.Slogf.wtf(TAG, "Error handling package changes", re);
                    }
                }
                policyKeys2 = policyKeys;
                it2 = it;
            }
        }
    }

    private boolean isPackageInstalled(java.lang.String packageName, int userId) {
        try {
            return android.app.AppGlobals.getPackageManager().getPackageInfo(packageName, 0L, userId) != null;
        } catch (android.os.RemoteException re) {
            com.android.server.utils.Slogf.wtf(TAG, "Error handling package changes", re);
            return true;
        }
    }

    void handleUserRemoved(int userId) {
        removeLocalPoliciesForUser(userId);
        removePoliciesForAdminsOnUser(userId);
    }

    void handleUserCreated(android.content.pm.UserInfo user) {
        enforcePoliciesOnInheritableProfilesIfApplicable(user);
    }

    void handleRoleChanged(java.lang.String roleName, int userId) {
        if (!DEVICE_LOCK_CONTROLLER_ROLE.equals(roleName)) {
            return;
        }
        java.lang.String roleAuthority = com.android.server.devicepolicy.EnforcingAdmin.getRoleAuthorityOf(roleName);
        java.util.Set<com.android.server.devicepolicy.EnforcingAdmin> admins = getEnforcingAdminsOnUser(userId);
        for (com.android.server.devicepolicy.EnforcingAdmin admin : admins) {
            if (admin.hasAuthority(roleAuthority)) {
                admin.reloadRoleAuthorities();
                if (!admin.hasAuthority(roleAuthority)) {
                    removePoliciesForAdmin(admin);
                }
            }
        }
    }

    private void enforcePoliciesOnInheritableProfilesIfApplicable(final android.content.pm.UserInfo user) {
        if (!user.isProfile()) {
            return;
        }
        android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.devicepolicy.DevicePolicyEngine$$ExternalSyntheticLambda4
            public final void runOrThrow() throws java.lang.Exception {
                this.f$0.lambda$enforcePoliciesOnInheritableProfilesIfApplicable$7(user);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enforcePoliciesOnInheritableProfilesIfApplicable$7(android.content.pm.UserInfo user) throws java.lang.Exception {
        int userId;
        android.content.pm.UserInfo parentInfo;
        android.content.pm.UserProperties userProperties = this.mUserManager.getUserProperties(user.getUserHandle());
        if (userProperties == null || userProperties.getInheritDevicePolicy() != 1 || (parentInfo = this.mUserManager.getProfileParent((userId = user.id))) == null || parentInfo.getUserHandle().getIdentifier() == userId) {
            return;
        }
        synchronized (this.mLock) {
            if (this.mLocalPolicies.contains(parentInfo.getUserHandle().getIdentifier())) {
                for (java.util.Map.Entry<android.app.admin.PolicyKey, com.android.server.devicepolicy.PolicyState<?>> entry : this.mLocalPolicies.get(parentInfo.getUserHandle().getIdentifier()).entrySet()) {
                    enforcePolicyOnUserLocked(userId, entry.getValue());
                }
            }
        }
    }

    private <V> void enforcePolicyOnUserLocked(int userId, com.android.server.devicepolicy.PolicyState<V> policyState) {
        if (!policyState.getPolicyDefinition().isInheritable()) {
            return;
        }
        for (java.util.Map.Entry<com.android.server.devicepolicy.EnforcingAdmin, android.app.admin.PolicyValue<V>> enforcingAdminEntry : policyState.getPoliciesSetByAdmins().entrySet()) {
            setLocalPolicy(policyState.getPolicyDefinition(), enforcingAdminEntry.getKey(), enforcingAdminEntry.getValue(), userId);
        }
    }

    android.app.admin.DevicePolicyState getDevicePolicyState() {
        android.app.admin.DevicePolicyState devicePolicyState;
        synchronized (this.mLock) {
            java.util.Map<android.os.UserHandle, java.util.Map<android.app.admin.PolicyKey, android.app.admin.PolicyState<?>>> policies = new java.util.HashMap<>();
            for (int i = 0; i < this.mLocalPolicies.size(); i++) {
                android.os.UserHandle user = android.os.UserHandle.of(this.mLocalPolicies.keyAt(i));
                policies.put(user, new java.util.HashMap<>());
                for (android.app.admin.PolicyKey policyKey : this.mLocalPolicies.valueAt(i).keySet()) {
                    policies.get(user).put(policyKey, this.mLocalPolicies.valueAt(i).get(policyKey).getParcelablePolicyState());
                }
            }
            if (!this.mGlobalPolicies.isEmpty()) {
                policies.put(android.os.UserHandle.ALL, new java.util.HashMap<>());
                for (android.app.admin.PolicyKey policyKey2 : this.mGlobalPolicies.keySet()) {
                    policies.get(android.os.UserHandle.ALL).put(policyKey2, this.mGlobalPolicies.get(policyKey2).getParcelablePolicyState());
                }
            }
            devicePolicyState = new android.app.admin.DevicePolicyState(policies);
        }
        return devicePolicyState;
    }

    void removePoliciesForAdmin(com.android.server.devicepolicy.EnforcingAdmin admin) {
        synchronized (this.mLock) {
            java.util.Set<android.app.admin.PolicyKey> globalPolicies = new java.util.HashSet<>(this.mGlobalPolicies.keySet());
            for (android.app.admin.PolicyKey policy : globalPolicies) {
                com.android.server.devicepolicy.PolicyState<?> policyState = this.mGlobalPolicies.get(policy);
                if (policyState.getPoliciesSetByAdmins().containsKey(admin)) {
                    removeGlobalPolicy(policyState.getPolicyDefinition(), admin);
                }
            }
            for (int i = 0; i < this.mLocalPolicies.size(); i++) {
                java.util.Set<android.app.admin.PolicyKey> localPolicies = new java.util.HashSet<>(this.mLocalPolicies.get(this.mLocalPolicies.keyAt(i)).keySet());
                for (android.app.admin.PolicyKey policy2 : localPolicies) {
                    com.android.server.devicepolicy.PolicyState<?> policyState2 = this.mLocalPolicies.get(this.mLocalPolicies.keyAt(i)).get(policy2);
                    if (policyState2.getPoliciesSetByAdmins().containsKey(admin)) {
                        removeLocalPolicy(policyState2.getPolicyDefinition(), admin, this.mLocalPolicies.keyAt(i));
                    }
                }
            }
        }
    }

    private void removeLocalPoliciesForUser(int userId) {
        synchronized (this.mLock) {
            if (this.mLocalPolicies.contains(userId)) {
                java.util.Set<android.app.admin.PolicyKey> localPolicies = new java.util.HashSet<>(this.mLocalPolicies.get(userId).keySet());
                for (android.app.admin.PolicyKey policy : localPolicies) {
                    com.android.server.devicepolicy.PolicyState<?> policyState = this.mLocalPolicies.get(userId).get(policy);
                    java.util.Set<com.android.server.devicepolicy.EnforcingAdmin> admins = new java.util.HashSet<>(policyState.getPoliciesSetByAdmins().keySet());
                    for (com.android.server.devicepolicy.EnforcingAdmin admin : admins) {
                        removeLocalPolicy(policyState.getPolicyDefinition(), admin, userId);
                    }
                }
                this.mLocalPolicies.remove(userId);
            }
        }
    }

    private void removePoliciesForAdminsOnUser(int userId) {
        java.util.Set<com.android.server.devicepolicy.EnforcingAdmin> admins = getEnforcingAdminsOnUser(userId);
        for (com.android.server.devicepolicy.EnforcingAdmin admin : admins) {
            removePoliciesForAdmin(admin);
        }
    }

    private void updateDeviceAdminServiceOnPackageChanged(java.lang.String updatedPackage, int userId) {
        for (com.android.server.devicepolicy.EnforcingAdmin admin : getEnforcingAdminsOnUser(userId)) {
            if (!admin.hasAuthority("enterprise") && updatedPackage.equals(admin.getPackageName())) {
                this.mDeviceAdminServiceController.startServiceForAdmin(updatedPackage, userId, "package-broadcast");
            }
        }
    }

    private void updateDeviceAdminServiceOnPolicyAddLocked(com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin) {
        int userId = enforcingAdmin.getUserId();
        if (this.mEnforcingAdmins.contains(userId) && this.mEnforcingAdmins.get(userId).contains(enforcingAdmin)) {
            return;
        }
        if (!this.mEnforcingAdmins.contains(enforcingAdmin.getUserId())) {
            this.mEnforcingAdmins.put(enforcingAdmin.getUserId(), new java.util.HashSet());
        }
        this.mEnforcingAdmins.get(enforcingAdmin.getUserId()).add(enforcingAdmin);
        if (enforcingAdmin.hasAuthority("enterprise")) {
            return;
        }
        this.mDeviceAdminServiceController.startServiceForAdmin(enforcingAdmin.getPackageName(), userId, "policy-added");
    }

    private void updateDeviceAdminServiceOnPolicyRemoveLocked(com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin) {
        if (doesAdminHavePoliciesLocked(enforcingAdmin)) {
            return;
        }
        int userId = enforcingAdmin.getUserId();
        if (this.mEnforcingAdmins.contains(userId)) {
            this.mEnforcingAdmins.get(userId).remove(enforcingAdmin);
            if (this.mEnforcingAdmins.get(userId).isEmpty()) {
                this.mEnforcingAdmins.remove(enforcingAdmin.getUserId());
            }
        }
        if (enforcingAdmin.hasAuthority("enterprise")) {
            return;
        }
        this.mDeviceAdminServiceController.stopServiceForAdmin(enforcingAdmin.getPackageName(), userId, "policy-removed");
    }

    private boolean doesAdminHavePoliciesLocked(com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin) {
        for (android.app.admin.PolicyKey policy : this.mGlobalPolicies.keySet()) {
            com.android.server.devicepolicy.PolicyState<?> policyState = this.mGlobalPolicies.get(policy);
            if (policyState.getPoliciesSetByAdmins().containsKey(enforcingAdmin)) {
                return true;
            }
        }
        for (int i = 0; i < this.mLocalPolicies.size(); i++) {
            for (android.app.admin.PolicyKey policy2 : this.mLocalPolicies.get(this.mLocalPolicies.keyAt(i)).keySet()) {
                com.android.server.devicepolicy.PolicyState<?> policyState2 = this.mLocalPolicies.get(this.mLocalPolicies.keyAt(i)).get(policy2);
                if (policyState2.getPoliciesSetByAdmins().containsKey(enforcingAdmin)) {
                    return true;
                }
            }
        }
        return false;
    }

    private java.util.Set<com.android.server.devicepolicy.EnforcingAdmin> getEnforcingAdminsOnUser(int userId) {
        java.util.Set<com.android.server.devicepolicy.EnforcingAdmin> hashSet;
        synchronized (this.mLock) {
            hashSet = this.mEnforcingAdmins.contains(userId) ? new java.util.HashSet<>(this.mEnforcingAdmins.get(userId)) : java.util.Collections.emptySet();
        }
        return hashSet;
    }

    private static <V> int sizeOf(android.app.admin.PolicyValue<V> value) {
        try {
            android.os.Parcel parcel = android.os.Parcel.obtain();
            parcel.writeParcelable(value, 0);
            parcel.setDataPosition(0);
            byte[] bytes = parcel.marshall();
            return bytes.length;
        } catch (java.lang.Exception e) {
            android.util.Log.e(TAG, "Error calculating size of policy: " + e);
            return 0;
        }
    }

    private <V> boolean handleAdminPolicySizeLimit(com.android.server.devicepolicy.PolicyState<V> policyState, com.android.server.devicepolicy.EnforcingAdmin admin, android.app.admin.PolicyValue<V> value, com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, int userId) {
        int currentAdminPoliciesSize = 0;
        int existingPolicySize = 0;
        if (this.mAdminPolicySize.contains(admin.getUserId()) && this.mAdminPolicySize.get(admin.getUserId()).containsKey(admin)) {
            currentAdminPoliciesSize = this.mAdminPolicySize.get(admin.getUserId()).get(admin).intValue();
        }
        if (policyState.getPoliciesSetByAdmins().containsKey(admin)) {
            existingPolicySize = sizeOf(policyState.getPoliciesSetByAdmins().get(admin));
        }
        int policySize = sizeOf(value);
        if (this.mPolicySizeLimit == -1 || (currentAdminPoliciesSize + policySize) - existingPolicySize < this.mPolicySizeLimit) {
            increasePolicySizeForAdmin(admin, policySize - existingPolicySize);
            return true;
        }
        android.util.Log.w(TAG, "Admin " + admin + "reached max allowed storage limit.");
        sendPolicyResultToAdmin(admin, policyDefinition, 3, userId);
        return false;
    }

    private <V> void increasePolicySizeForAdmin(com.android.server.devicepolicy.EnforcingAdmin admin, int policySizeDiff) {
        if (!this.mAdminPolicySize.contains(admin.getUserId())) {
            this.mAdminPolicySize.put(admin.getUserId(), new java.util.HashMap<>());
        }
        if (!this.mAdminPolicySize.get(admin.getUserId()).containsKey(admin)) {
            this.mAdminPolicySize.get(admin.getUserId()).put(admin, 0);
        }
        this.mAdminPolicySize.get(admin.getUserId()).put(admin, java.lang.Integer.valueOf(this.mAdminPolicySize.get(admin.getUserId()).get(admin).intValue() + policySizeDiff));
    }

    private <V> void decreasePolicySizeForAdmin(com.android.server.devicepolicy.PolicyState<V> policyState, com.android.server.devicepolicy.EnforcingAdmin admin) {
        if (!policyState.getPoliciesSetByAdmins().containsKey(admin) || !this.mAdminPolicySize.contains(admin.getUserId()) || !this.mAdminPolicySize.get(admin.getUserId()).containsKey(admin)) {
            return;
        }
        this.mAdminPolicySize.get(admin.getUserId()).put(admin, java.lang.Integer.valueOf(this.mAdminPolicySize.get(admin.getUserId()).get(admin).intValue() - sizeOf(policyState.getPoliciesSetByAdmins().get(admin))));
        if (this.mAdminPolicySize.get(admin.getUserId()).get(admin).intValue() <= 0) {
            this.mAdminPolicySize.get(admin.getUserId()).remove(admin);
        }
        if (this.mAdminPolicySize.get(admin.getUserId()).isEmpty()) {
            this.mAdminPolicySize.remove(admin.getUserId());
        }
    }

    void setMaxPolicyStorageLimit(int storageLimit) {
        this.mPolicySizeLimit = storageLimit;
    }

    int getMaxPolicyStorageLimit() {
        return this.mPolicySizeLimit;
    }

    int getPolicySizeForAdmin(com.android.server.devicepolicy.EnforcingAdmin admin) {
        if (this.mAdminPolicySize.contains(admin.getUserId()) && this.mAdminPolicySize.get(admin.getUserId()).containsKey(admin)) {
            return this.mAdminPolicySize.get(admin.getUserId()).get(admin).intValue();
        }
        return 0;
    }

    public void dump(android.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            pw.println("Local Policies: ");
            pw.increaseIndent();
            for (int i = 0; i < this.mLocalPolicies.size(); i++) {
                int userId = this.mLocalPolicies.keyAt(i);
                pw.printf("User %d:\n", new java.lang.Object[]{java.lang.Integer.valueOf(userId)});
                pw.increaseIndent();
                for (android.app.admin.PolicyKey policy : this.mLocalPolicies.get(userId).keySet()) {
                    com.android.server.devicepolicy.PolicyState<?> policyState = this.mLocalPolicies.get(userId).get(policy);
                    policyState.dump(pw);
                    pw.println();
                }
                pw.decreaseIndent();
            }
            pw.decreaseIndent();
            pw.println();
            pw.println("Global Policies: ");
            pw.increaseIndent();
            for (android.app.admin.PolicyKey policy2 : this.mGlobalPolicies.keySet()) {
                com.android.server.devicepolicy.PolicyState<?> policyState2 = this.mGlobalPolicies.get(policy2);
                policyState2.dump(pw);
                pw.println();
            }
            pw.decreaseIndent();
            if (android.app.admin.flags.Flags.devicePolicySizeTrackingInternalBugFixEnabled()) {
                pw.println();
                pw.println("Default admin policy size limit: -1");
                pw.println("Current admin policy size limit: " + this.mPolicySizeLimit);
                pw.println("Admin Policies size: ");
                for (int i2 = 0; i2 < this.mAdminPolicySize.size(); i2++) {
                    int userId2 = this.mAdminPolicySize.keyAt(i2);
                    pw.printf("User %d:\n", new java.lang.Object[]{java.lang.Integer.valueOf(userId2)});
                    pw.increaseIndent();
                    for (com.android.server.devicepolicy.EnforcingAdmin admin : this.mAdminPolicySize.get(userId2).keySet()) {
                        pw.printf("Admin : " + admin + " : " + this.mAdminPolicySize.get(userId2).get(admin), new java.lang.Object[0]);
                        pw.println();
                    }
                    pw.decreaseIndent();
                }
                pw.decreaseIndent();
            }
        }
    }

    private void write() {
        synchronized (this.mLock) {
            android.util.Log.d(TAG, "Writing device policies to file.");
            new com.android.server.devicepolicy.DevicePolicyEngine.DevicePoliciesReaderWriter().writeToFileLocked();
        }
    }

    void load() {
        android.util.Log.d(TAG, "Reading device policies from file.");
        synchronized (this.mLock) {
            clear();
            new com.android.server.devicepolicy.DevicePolicyEngine.DevicePoliciesReaderWriter().readFromFileLocked();
        }
    }

    void createBackup(java.lang.String backupId) {
        synchronized (this.mLock) {
            com.android.server.devicepolicy.DevicePolicyEngine.DevicePoliciesReaderWriter.createBackup(backupId);
        }
    }

    <V> void reapplyAllPoliciesOnBootLocked() {
        for (android.app.admin.PolicyKey policy : this.mGlobalPolicies.keySet()) {
            com.android.server.devicepolicy.PolicyState<?> policyState = this.mGlobalPolicies.get(policy);
            com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition = policyState.getPolicyDefinition();
            if (!policyDefinition.shouldSkipEnforcementIfNotChanged()) {
                android.app.admin.PolicyValue<V> policyValue = policyState.getCurrentResolvedPolicy();
                enforcePolicy(policyDefinition, policyValue, -1);
            }
        }
        for (int i = 0; i < this.mLocalPolicies.size(); i++) {
            int userId = this.mLocalPolicies.keyAt(i);
            for (android.app.admin.PolicyKey policy2 : this.mLocalPolicies.get(userId).keySet()) {
                com.android.server.devicepolicy.PolicyState<?> policyState2 = this.mLocalPolicies.get(userId).get(policy2);
                com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition2 = policyState2.getPolicyDefinition();
                if (!policyDefinition2.shouldSkipEnforcementIfNotChanged()) {
                    android.app.admin.PolicyValue<V> policyValue2 = policyState2.getCurrentResolvedPolicy();
                    enforcePolicy(policyDefinition2, policyValue2, userId);
                }
            }
        }
    }

    void clearAllPolicies() {
        clear();
        write();
    }

    private void clear() {
        synchronized (this.mLock) {
            this.mGlobalPolicies.clear();
            this.mLocalPolicies.clear();
            this.mEnforcingAdmins.clear();
            this.mAdminPolicySize.clear();
        }
    }

    private <V> boolean checkFor2gFailure(com.android.server.devicepolicy.PolicyDefinition<V> policyDefinition, com.android.server.devicepolicy.EnforcingAdmin enforcingAdmin) {
        boolean isCapabilitySupported;
        if (!policyDefinition.getPolicyKey().getIdentifier().equals(CELLULAR_2G_USER_RESTRICTION_ID)) {
            return false;
        }
        try {
            isCapabilitySupported = ((android.telephony.TelephonyManager) this.mContext.getSystemService(android.telephony.TelephonyManager.class)).isRadioInterfaceCapabilitySupported("CAPABILITY_USES_ALLOWED_NETWORK_TYPES_BITMASK");
        } catch (java.lang.IllegalStateException e) {
            isCapabilitySupported = false;
        }
        if (isCapabilitySupported) {
            return false;
        }
        sendPolicyResultToAdmin(enforcingAdmin, policyDefinition, 4, -1);
        return true;
    }

    private <V> boolean shouldApplyPackageSetUnionPolicyHack(com.android.server.devicepolicy.PolicyDefinition<V> policy) {
        java.lang.String policyKey = policy.getPolicyKey().getIdentifier();
        return policyKey.equals("userControlDisabledPackages") || policyKey.equals("packagesSuspended");
    }

    private class DevicePoliciesReaderWriter {
        private static final java.lang.String ATTR_POLICY_SUM_SIZE = "size";
        private static final java.lang.String ATTR_USER_ID = "user-id";
        private static final java.lang.String BACKUP_DIRECTORY = "device_policy_backups";
        private static final java.lang.String BACKUP_FILENAME = "device_policy_state.%s.xml";
        private static final java.lang.String DEVICE_POLICIES_XML = "device_policy_state.xml";
        private static final java.lang.String TAG_ENFORCING_ADMIN = "enforcing-admin";
        private static final java.lang.String TAG_ENFORCING_ADMINS_ENTRY = "enforcing-admins-entry";
        private static final java.lang.String TAG_ENFORCING_ADMIN_AND_SIZE = "enforcing-admin-and-size";
        private static final java.lang.String TAG_GLOBAL_POLICY_ENTRY = "global-policy-entry";
        private static final java.lang.String TAG_LOCAL_POLICY_ENTRY = "local-policy-entry";
        private static final java.lang.String TAG_MAX_POLICY_SIZE_LIMIT = "max-policy-size-limit";
        private static final java.lang.String TAG_POLICY_KEY_ENTRY = "policy-key-entry";
        private static final java.lang.String TAG_POLICY_STATE_ENTRY = "policy-state-entry";
        private static final java.lang.String TAG_POLICY_SUM_SIZE = "policy-sum-size";
        private final java.io.File mFile;

        private static java.io.File getFileName() {
            return new java.io.File(android.os.Environment.getDataSystemDirectory(), DEVICE_POLICIES_XML);
        }

        private DevicePoliciesReaderWriter() {
            this.mFile = getFileName();
        }

        public static void createBackup(java.lang.String backupId) {
            try {
                java.io.File backupDirectory = new java.io.File(android.os.Environment.getDataSystemDirectory(), BACKUP_DIRECTORY);
                backupDirectory.mkdir();
                java.nio.file.Path backupPath = java.nio.file.Path.of(backupDirectory.getPath(), BACKUP_FILENAME.formatted(backupId));
                if (backupPath.toFile().exists()) {
                    android.util.Log.w(com.android.server.devicepolicy.DevicePolicyEngine.TAG, "Backup already exist: " + backupPath);
                } else {
                    java.nio.file.Files.copy(getFileName().toPath(), backupPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    android.util.Log.i(com.android.server.devicepolicy.DevicePolicyEngine.TAG, "Backup created at " + backupPath);
                }
            } catch (java.lang.Exception e) {
                android.util.Log.e(com.android.server.devicepolicy.DevicePolicyEngine.TAG, "Cannot create backup " + backupId, e);
            }
        }

        void writeToFileLocked() {
            android.util.Log.d(com.android.server.devicepolicy.DevicePolicyEngine.TAG, "Writing to " + this.mFile);
            android.util.AtomicFile f = new android.util.AtomicFile(this.mFile);
            java.io.FileOutputStream outputStream = null;
            try {
                outputStream = f.startWrite();
                com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(outputStream);
                out.startDocument((java.lang.String) null, true);
                writeInner(out);
                out.endDocument();
                out.flush();
                f.finishWrite(outputStream);
            } catch (java.io.IOException e) {
                android.util.Log.e(com.android.server.devicepolicy.DevicePolicyEngine.TAG, "Exception when writing", e);
                if (outputStream != null) {
                    f.failWrite(outputStream);
                }
            }
        }

        void writeInner(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            writeLocalPoliciesInner(serializer);
            writeGlobalPoliciesInner(serializer);
            writeEnforcingAdminsInner(serializer);
            writeEnforcingAdminSizeInner(serializer);
            writeMaxPolicySizeInner(serializer);
        }

        private void writeLocalPoliciesInner(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            if (com.android.server.devicepolicy.DevicePolicyEngine.this.mLocalPolicies != null) {
                for (int i = 0; i < com.android.server.devicepolicy.DevicePolicyEngine.this.mLocalPolicies.size(); i++) {
                    int userId = com.android.server.devicepolicy.DevicePolicyEngine.this.mLocalPolicies.keyAt(i);
                    for (java.util.Map.Entry<android.app.admin.PolicyKey, com.android.server.devicepolicy.PolicyState<?>> policy : ((java.util.Map) com.android.server.devicepolicy.DevicePolicyEngine.this.mLocalPolicies.get(userId)).entrySet()) {
                        serializer.startTag((java.lang.String) null, TAG_LOCAL_POLICY_ENTRY);
                        serializer.attributeInt((java.lang.String) null, ATTR_USER_ID, userId);
                        serializer.startTag((java.lang.String) null, TAG_POLICY_KEY_ENTRY);
                        policy.getKey().saveToXml(serializer);
                        serializer.endTag((java.lang.String) null, TAG_POLICY_KEY_ENTRY);
                        serializer.startTag((java.lang.String) null, TAG_POLICY_STATE_ENTRY);
                        policy.getValue().saveToXml(serializer);
                        serializer.endTag((java.lang.String) null, TAG_POLICY_STATE_ENTRY);
                        serializer.endTag((java.lang.String) null, TAG_LOCAL_POLICY_ENTRY);
                    }
                }
            }
        }

        private void writeGlobalPoliciesInner(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            if (com.android.server.devicepolicy.DevicePolicyEngine.this.mGlobalPolicies != null) {
                for (java.util.Map.Entry<android.app.admin.PolicyKey, com.android.server.devicepolicy.PolicyState<?>> policy : com.android.server.devicepolicy.DevicePolicyEngine.this.mGlobalPolicies.entrySet()) {
                    serializer.startTag((java.lang.String) null, TAG_GLOBAL_POLICY_ENTRY);
                    serializer.startTag((java.lang.String) null, TAG_POLICY_KEY_ENTRY);
                    policy.getKey().saveToXml(serializer);
                    serializer.endTag((java.lang.String) null, TAG_POLICY_KEY_ENTRY);
                    serializer.startTag((java.lang.String) null, TAG_POLICY_STATE_ENTRY);
                    policy.getValue().saveToXml(serializer);
                    serializer.endTag((java.lang.String) null, TAG_POLICY_STATE_ENTRY);
                    serializer.endTag((java.lang.String) null, TAG_GLOBAL_POLICY_ENTRY);
                }
            }
        }

        private void writeEnforcingAdminsInner(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            if (com.android.server.devicepolicy.DevicePolicyEngine.this.mEnforcingAdmins != null) {
                for (int i = 0; i < com.android.server.devicepolicy.DevicePolicyEngine.this.mEnforcingAdmins.size(); i++) {
                    int userId = com.android.server.devicepolicy.DevicePolicyEngine.this.mEnforcingAdmins.keyAt(i);
                    for (com.android.server.devicepolicy.EnforcingAdmin admin : (java.util.Set) com.android.server.devicepolicy.DevicePolicyEngine.this.mEnforcingAdmins.get(userId)) {
                        serializer.startTag((java.lang.String) null, TAG_ENFORCING_ADMINS_ENTRY);
                        admin.saveToXml(serializer);
                        serializer.endTag((java.lang.String) null, TAG_ENFORCING_ADMINS_ENTRY);
                    }
                }
            }
        }

        private void writeEnforcingAdminSizeInner(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            if (android.app.admin.flags.Flags.devicePolicySizeTrackingInternalBugFixEnabled() && com.android.server.devicepolicy.DevicePolicyEngine.this.mAdminPolicySize != null) {
                for (int i = 0; i < com.android.server.devicepolicy.DevicePolicyEngine.this.mAdminPolicySize.size(); i++) {
                    int userId = com.android.server.devicepolicy.DevicePolicyEngine.this.mAdminPolicySize.keyAt(i);
                    for (com.android.server.devicepolicy.EnforcingAdmin admin : ((java.util.HashMap) com.android.server.devicepolicy.DevicePolicyEngine.this.mAdminPolicySize.get(userId)).keySet()) {
                        serializer.startTag((java.lang.String) null, TAG_ENFORCING_ADMIN_AND_SIZE);
                        serializer.startTag((java.lang.String) null, TAG_ENFORCING_ADMIN);
                        admin.saveToXml(serializer);
                        serializer.endTag((java.lang.String) null, TAG_ENFORCING_ADMIN);
                        serializer.startTag((java.lang.String) null, TAG_POLICY_SUM_SIZE);
                        serializer.attributeInt((java.lang.String) null, ATTR_POLICY_SUM_SIZE, ((java.lang.Integer) ((java.util.HashMap) com.android.server.devicepolicy.DevicePolicyEngine.this.mAdminPolicySize.get(userId)).get(admin)).intValue());
                        serializer.endTag((java.lang.String) null, TAG_POLICY_SUM_SIZE);
                        serializer.endTag((java.lang.String) null, TAG_ENFORCING_ADMIN_AND_SIZE);
                    }
                }
            }
        }

        private void writeMaxPolicySizeInner(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
            if (!android.app.admin.flags.Flags.devicePolicySizeTrackingInternalBugFixEnabled()) {
                return;
            }
            serializer.startTag((java.lang.String) null, TAG_MAX_POLICY_SIZE_LIMIT);
            serializer.attributeInt((java.lang.String) null, ATTR_POLICY_SUM_SIZE, com.android.server.devicepolicy.DevicePolicyEngine.this.mPolicySizeLimit);
            serializer.endTag((java.lang.String) null, TAG_MAX_POLICY_SIZE_LIMIT);
        }

        void readFromFileLocked() {
            if (!this.mFile.exists()) {
                android.util.Log.d(com.android.server.devicepolicy.DevicePolicyEngine.TAG, "" + this.mFile + " doesn't exist");
                return;
            }
            android.util.Log.d(com.android.server.devicepolicy.DevicePolicyEngine.TAG, "Reading from " + this.mFile);
            android.util.AtomicFile f = new android.util.AtomicFile(this.mFile);
            java.io.InputStream input = null;
            try {
                try {
                    input = f.openRead();
                    com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(input);
                    readInner(parser);
                } catch (java.io.IOException | java.lang.ClassNotFoundException | org.xmlpull.v1.XmlPullParserException e) {
                    com.android.server.utils.Slogf.wtf(com.android.server.devicepolicy.DevicePolicyEngine.TAG, "Error parsing resources file", e);
                }
            } finally {
                libcore.io.IoUtils.closeQuietly(input);
            }
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:23:0x004b  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void readInner(com.android.modules.utils.TypedXmlPullParser r5) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException, java.lang.ClassNotFoundException {
            /*
                r4 = this;
                int r0 = r5.getDepth()
            L4:
                boolean r1 = com.android.internal.util.XmlUtils.nextElementWithin(r5, r0)
                if (r1 == 0) goto L7d
                java.lang.String r1 = r5.getName()
                int r2 = r1.hashCode()
                switch(r2) {
                    case -1900677631: goto L40;
                    case -1329955015: goto L35;
                    case -949666205: goto L2b;
                    case 134595137: goto L20;
                    case 1016501079: goto L16;
                    default: goto L15;
                }
            L15:
                goto L4b
            L16:
                java.lang.String r2 = "enforcing-admins-entry"
                boolean r2 = r1.equals(r2)
                if (r2 == 0) goto L15
                r2 = 2
                goto L4c
            L20:
                java.lang.String r2 = "max-policy-size-limit"
                boolean r2 = r1.equals(r2)
                if (r2 == 0) goto L15
                r2 = 4
                goto L4c
            L2b:
                java.lang.String r2 = "enforcing-admin-and-size"
                boolean r2 = r1.equals(r2)
                if (r2 == 0) goto L15
                r2 = 3
                goto L4c
            L35:
                java.lang.String r2 = "local-policy-entry"
                boolean r2 = r1.equals(r2)
                if (r2 == 0) goto L15
                r2 = 0
                goto L4c
            L40:
                java.lang.String r2 = "global-policy-entry"
                boolean r2 = r1.equals(r2)
                if (r2 == 0) goto L15
                r2 = 1
                goto L4c
            L4b:
                r2 = -1
            L4c:
                switch(r2) {
                    case 0: goto L78;
                    case 1: goto L74;
                    case 2: goto L70;
                    case 3: goto L6c;
                    case 4: goto L68;
                    default: goto L4f;
                }
            L4f:
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "Unknown tag "
                java.lang.StringBuilder r2 = r2.append(r3)
                java.lang.StringBuilder r2 = r2.append(r1)
                java.lang.String r2 = r2.toString()
                java.lang.String r3 = "DevicePolicyEngine"
                com.android.server.utils.Slogf.wtf(r3, r2)
                goto L7c
            L68:
                r4.readMaxPolicySizeInner(r5)
                goto L7c
            L6c:
                r4.readEnforcingAdminAndSizeInner(r5)
                goto L7c
            L70:
                r4.readEnforcingAdminsInner(r5)
                goto L7c
            L74:
                r4.readGlobalPoliciesInner(r5)
                goto L7c
            L78:
                r4.readLocalPoliciesInner(r5)
            L7c:
                goto L4
            L7d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicepolicy.DevicePolicyEngine.DevicePoliciesReaderWriter.readInner(com.android.modules.utils.TypedXmlPullParser):void");
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0038  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void readLocalPoliciesInner(com.android.modules.utils.TypedXmlPullParser r9) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            /*
                Method dump skipped, instruction units count: 210
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicepolicy.DevicePolicyEngine.DevicePoliciesReaderWriter.readLocalPoliciesInner(com.android.modules.utils.TypedXmlPullParser):void");
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:14:0x0030  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void readGlobalPoliciesInner(com.android.modules.utils.TypedXmlPullParser r8) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            /*
                r7 = this;
                r0 = 0
                r1 = 0
                int r2 = r8.getDepth()
            L6:
                boolean r3 = com.android.internal.util.XmlUtils.nextElementWithin(r8, r2)
                java.lang.String r4 = "DevicePolicyEngine"
                if (r3 == 0) goto L56
                java.lang.String r3 = r8.getName()
                int r5 = r3.hashCode()
                switch(r5) {
                    case 1439131817: goto L25;
                    case 1917578267: goto L1a;
                    default: goto L19;
                }
            L19:
                goto L30
            L1a:
                java.lang.String r5 = "policy-state-entry"
                boolean r5 = r3.equals(r5)
                if (r5 == 0) goto L19
                r5 = 1
                goto L31
            L25:
                java.lang.String r5 = "policy-key-entry"
                boolean r5 = r3.equals(r5)
                if (r5 == 0) goto L19
                r5 = 0
                goto L31
            L30:
                r5 = -1
            L31:
                switch(r5) {
                    case 0: goto L50;
                    case 1: goto L4b;
                    default: goto L34;
                }
            L34:
                java.lang.StringBuilder r5 = new java.lang.StringBuilder
                r5.<init>()
                java.lang.String r6 = "Unknown tag for local policy entry"
                java.lang.StringBuilder r5 = r5.append(r6)
                java.lang.StringBuilder r5 = r5.append(r3)
                java.lang.String r5 = r5.toString()
                com.android.server.utils.Slogf.wtf(r4, r5)
                goto L55
            L4b:
                com.android.server.devicepolicy.PolicyState r1 = com.android.server.devicepolicy.PolicyState.readFromXml(r8)
                goto L55
            L50:
                android.app.admin.PolicyKey r0 = com.android.server.devicepolicy.PolicyDefinition.readPolicyKeyFromXml(r8)
            L55:
                goto L6
            L56:
                if (r0 == 0) goto L64
                if (r1 == 0) goto L64
                com.android.server.devicepolicy.DevicePolicyEngine r3 = com.android.server.devicepolicy.DevicePolicyEngine.this
                java.util.Map r3 = com.android.server.devicepolicy.DevicePolicyEngine.m3124$$Nest$fgetmGlobalPolicies(r3)
                r3.put(r0, r1)
                goto L96
            L64:
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r5 = "Error parsing global policy, policyKey is "
                java.lang.StringBuilder r3 = r3.append(r5)
                java.lang.String r5 = "null"
                if (r0 != 0) goto L76
                r6 = r5
                goto L77
            L76:
                r6 = r0
            L77:
                java.lang.StringBuilder r3 = r3.append(r6)
                java.lang.String r6 = ", and policyState is "
                java.lang.StringBuilder r3 = r3.append(r6)
                if (r1 != 0) goto L84
                goto L85
            L84:
                r5 = r1
            L85:
                java.lang.StringBuilder r3 = r3.append(r5)
                java.lang.String r5 = "."
                java.lang.StringBuilder r3 = r3.append(r5)
                java.lang.String r3 = r3.toString()
                com.android.server.utils.Slogf.wtf(r4, r3)
            L96:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicepolicy.DevicePolicyEngine.DevicePoliciesReaderWriter.readGlobalPoliciesInner(com.android.modules.utils.TypedXmlPullParser):void");
        }

        private void readEnforcingAdminsInner(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException {
            com.android.server.devicepolicy.EnforcingAdmin admin = com.android.server.devicepolicy.EnforcingAdmin.readFromXml(parser);
            if (admin == null) {
                com.android.server.utils.Slogf.wtf(com.android.server.devicepolicy.DevicePolicyEngine.TAG, "Error parsing enforcingAdmins, EnforcingAdmin is null.");
                return;
            }
            if (!com.android.server.devicepolicy.DevicePolicyEngine.this.mEnforcingAdmins.contains(admin.getUserId())) {
                com.android.server.devicepolicy.DevicePolicyEngine.this.mEnforcingAdmins.put(admin.getUserId(), new java.util.HashSet());
            }
            ((java.util.Set) com.android.server.devicepolicy.DevicePolicyEngine.this.mEnforcingAdmins.get(admin.getUserId())).add(admin);
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:14:0x002f  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private void readEnforcingAdminAndSizeInner(com.android.modules.utils.TypedXmlPullParser r8) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            /*
                r7 = this;
                int r0 = r8.getDepth()
                r1 = 0
                r2 = 0
            L6:
                boolean r3 = com.android.internal.util.XmlUtils.nextElementWithin(r8, r0)
                java.lang.String r4 = "DevicePolicyEngine"
                if (r3 == 0) goto L59
                java.lang.String r3 = r8.getName()
                int r5 = r3.hashCode()
                switch(r5) {
                    case -1290014687: goto L25;
                    case -1249111938: goto L1a;
                    default: goto L19;
                }
            L19:
                goto L2f
            L1a:
                java.lang.String r5 = "policy-sum-size"
                boolean r5 = r3.equals(r5)
                if (r5 == 0) goto L19
                r5 = 1
                goto L30
            L25:
                java.lang.String r5 = "enforcing-admin"
                boolean r5 = r3.equals(r5)
                if (r5 == 0) goto L19
                r5 = 0
                goto L30
            L2f:
                r5 = -1
            L30:
                switch(r5) {
                    case 0: goto L53;
                    case 1: goto L4a;
                    default: goto L33;
                }
            L33:
                java.lang.StringBuilder r5 = new java.lang.StringBuilder
                r5.<init>()
                java.lang.String r6 = "Unknown tag "
                java.lang.StringBuilder r5 = r5.append(r6)
                java.lang.StringBuilder r5 = r5.append(r3)
                java.lang.String r5 = r5.toString()
                com.android.server.utils.Slogf.wtf(r4, r5)
                goto L58
            L4a:
                r4 = 0
                java.lang.String r5 = "size"
                int r2 = r8.getAttributeInt(r4, r5)
                goto L58
            L53:
                com.android.server.devicepolicy.EnforcingAdmin r1 = com.android.server.devicepolicy.EnforcingAdmin.readFromXml(r8)
            L58:
                goto L6
            L59:
                if (r1 != 0) goto L61
                java.lang.String r3 = "Error parsing enforcingAdmins, EnforcingAdmin is null."
                com.android.server.utils.Slogf.wtf(r4, r3)
                return
            L61:
                if (r2 > 0) goto L7a
                java.lang.StringBuilder r3 = new java.lang.StringBuilder
                r3.<init>()
                java.lang.String r5 = "Error parsing policy size, size is "
                java.lang.StringBuilder r3 = r3.append(r5)
                java.lang.StringBuilder r3 = r3.append(r2)
                java.lang.String r3 = r3.toString()
                com.android.server.utils.Slogf.wtf(r4, r3)
                return
            L7a:
                com.android.server.devicepolicy.DevicePolicyEngine r3 = com.android.server.devicepolicy.DevicePolicyEngine.this
                android.util.SparseArray r3 = com.android.server.devicepolicy.DevicePolicyEngine.m3122$$Nest$fgetmAdminPolicySize(r3)
                int r4 = r1.getUserId()
                boolean r3 = r3.contains(r4)
                if (r3 != 0) goto L9c
                com.android.server.devicepolicy.DevicePolicyEngine r3 = com.android.server.devicepolicy.DevicePolicyEngine.this
                android.util.SparseArray r3 = com.android.server.devicepolicy.DevicePolicyEngine.m3122$$Nest$fgetmAdminPolicySize(r3)
                int r4 = r1.getUserId()
                java.util.HashMap r5 = new java.util.HashMap
                r5.<init>()
                r3.put(r4, r5)
            L9c:
                com.android.server.devicepolicy.DevicePolicyEngine r3 = com.android.server.devicepolicy.DevicePolicyEngine.this
                android.util.SparseArray r3 = com.android.server.devicepolicy.DevicePolicyEngine.m3122$$Nest$fgetmAdminPolicySize(r3)
                int r4 = r1.getUserId()
                java.lang.Object r3 = r3.get(r4)
                java.util.HashMap r3 = (java.util.HashMap) r3
                java.lang.Integer r4 = java.lang.Integer.valueOf(r2)
                r3.put(r1, r4)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.devicepolicy.DevicePolicyEngine.DevicePoliciesReaderWriter.readEnforcingAdminAndSizeInner(com.android.modules.utils.TypedXmlPullParser):void");
        }

        private void readMaxPolicySizeInner(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            if (!android.app.admin.flags.Flags.devicePolicySizeTrackingInternalBugFixEnabled()) {
                return;
            }
            com.android.server.devicepolicy.DevicePolicyEngine.this.mPolicySizeLimit = parser.getAttributeInt((java.lang.String) null, ATTR_POLICY_SUM_SIZE);
        }
    }
}
