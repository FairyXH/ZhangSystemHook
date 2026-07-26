package com.android.server.pm.verify.domain.models;

/* JADX INFO: loaded from: classes2.dex */
public class DomainVerificationPkgState {
    private final java.lang.String mBackupSignatureHash;
    private final boolean mHasAutoVerifyDomains;
    private final java.util.UUID mId;
    private final java.lang.String mPackageName;
    private final android.util.ArrayMap<java.lang.String, java.lang.Integer> mStateMap;
    private final android.util.ArrayMap<java.lang.String, java.util.List<android.content.UriRelativeFilterGroup>> mUriRelativeFilterGroupMap;
    private final android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> mUserStates;

    public DomainVerificationPkgState(java.lang.String packageName, java.util.UUID id, boolean hasAutoVerifyDomains) {
        this(packageName, id, hasAutoVerifyDomains, new android.util.ArrayMap(0), new android.util.SparseArray(0), null, new android.util.ArrayMap());
    }

    public DomainVerificationPkgState(com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState, java.util.UUID id, boolean hasAutoVerifyDomains) {
        this(pkgState.getPackageName(), id, hasAutoVerifyDomains, pkgState.getStateMap(), pkgState.getUserStates(), null, new android.util.ArrayMap());
    }

    public DomainVerificationPkgState(java.lang.String packageName, java.util.UUID id, boolean hasAutoVerifyDomains, android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap, android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> userStates, java.lang.String backupSignatureHash) {
        this(packageName, id, hasAutoVerifyDomains, stateMap, userStates, backupSignatureHash, new android.util.ArrayMap());
    }

    public com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState getUserState(int userId) {
        return this.mUserStates.get(userId);
    }

    public com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState getOrCreateUserState(int userId) {
        com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState userState = this.mUserStates.get(userId);
        if (userState == null) {
            com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState userState2 = new com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState(userId);
            this.mUserStates.put(userId, userState2);
            return userState2;
        }
        return userState;
    }

    public void removeUser(int userId) {
        this.mUserStates.remove(userId);
    }

    public void removeAllUsers() {
        this.mUserStates.clear();
    }

    private int userStatesHashCode() {
        return this.mUserStates.contentHashCode();
    }

    private boolean userStatesEquals(android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> other) {
        return this.mUserStates.contentEquals(other);
    }

    public DomainVerificationPkgState(java.lang.String packageName, java.util.UUID id, boolean hasAutoVerifyDomains, android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap, android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> userStates, java.lang.String backupSignatureHash, android.util.ArrayMap<java.lang.String, java.util.List<android.content.UriRelativeFilterGroup>> uriRelativeFilterGroupMap) {
        this.mPackageName = packageName;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, (android.annotation.NonNull) null, this.mPackageName);
        this.mId = id;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, (android.annotation.NonNull) null, this.mId);
        this.mHasAutoVerifyDomains = hasAutoVerifyDomains;
        this.mStateMap = stateMap;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, (android.annotation.NonNull) null, this.mStateMap);
        this.mUserStates = userStates;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, (android.annotation.NonNull) null, this.mUserStates);
        this.mBackupSignatureHash = backupSignatureHash;
        this.mUriRelativeFilterGroupMap = uriRelativeFilterGroupMap;
        com.android.internal.util.AnnotationValidations.validate(android.annotation.NonNull.class, (android.annotation.NonNull) null, this.mUriRelativeFilterGroupMap);
    }

    public java.lang.String getPackageName() {
        return this.mPackageName;
    }

    public java.util.UUID getId() {
        return this.mId;
    }

    public boolean isHasAutoVerifyDomains() {
        return this.mHasAutoVerifyDomains;
    }

    public android.util.ArrayMap<java.lang.String, java.lang.Integer> getStateMap() {
        return this.mStateMap;
    }

    public android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> getUserStates() {
        return this.mUserStates;
    }

    public java.lang.String getBackupSignatureHash() {
        return this.mBackupSignatureHash;
    }

    public android.util.ArrayMap<java.lang.String, java.util.List<android.content.UriRelativeFilterGroup>> getUriRelativeFilterGroupMap() {
        return this.mUriRelativeFilterGroupMap;
    }

    public java.lang.String toString() {
        return "DomainVerificationPkgState { packageName = " + this.mPackageName + ", id = " + this.mId + ", hasAutoVerifyDomains = " + this.mHasAutoVerifyDomains + ", stateMap = " + this.mStateMap + ", userStates = " + this.mUserStates + ", backupSignatureHash = " + this.mBackupSignatureHash + ", uriRelativeFilterGroupMap = " + this.mUriRelativeFilterGroupMap + " }";
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        com.android.server.pm.verify.domain.models.DomainVerificationPkgState that = (com.android.server.pm.verify.domain.models.DomainVerificationPkgState) o;
        if (java.util.Objects.equals(this.mPackageName, that.mPackageName) && java.util.Objects.equals(this.mId, that.mId) && this.mHasAutoVerifyDomains == that.mHasAutoVerifyDomains && java.util.Objects.equals(this.mStateMap, that.mStateMap) && userStatesEquals(that.mUserStates) && java.util.Objects.equals(this.mBackupSignatureHash, that.mBackupSignatureHash) && java.util.Objects.equals(this.mUriRelativeFilterGroupMap, that.mUriRelativeFilterGroupMap)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int _hash = (1 * 31) + java.util.Objects.hashCode(this.mPackageName);
        return (((((((((((_hash * 31) + java.util.Objects.hashCode(this.mId)) * 31) + java.lang.Boolean.hashCode(this.mHasAutoVerifyDomains)) * 31) + java.util.Objects.hashCode(this.mStateMap)) * 31) + userStatesHashCode()) * 31) + java.util.Objects.hashCode(this.mBackupSignatureHash)) * 31) + java.util.Objects.hashCode(this.mUriRelativeFilterGroupMap);
    }

    @java.lang.Deprecated
    private void __metadata() {
    }
}
