package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class PackageKeySetData {
    static final long KEYSET_UNASSIGNED = -1;
    private final android.util.ArrayMap<java.lang.String, java.lang.Long> mKeySetAliases;
    private long mProperSigningKeySet;
    private long[] mUpgradeKeySets;

    PackageKeySetData() {
        this.mKeySetAliases = new android.util.ArrayMap<>();
        this.mProperSigningKeySet = -1L;
    }

    PackageKeySetData(com.android.server.pm.PackageKeySetData original) {
        this.mKeySetAliases = new android.util.ArrayMap<>();
        this.mProperSigningKeySet = original.mProperSigningKeySet;
        this.mUpgradeKeySets = com.android.internal.util.ArrayUtils.cloneOrNull(original.mUpgradeKeySets);
        this.mKeySetAliases.putAll((android.util.ArrayMap<? extends java.lang.String, ? extends java.lang.Long>) original.mKeySetAliases);
    }

    protected void setProperSigningKeySet(long ks) {
        this.mProperSigningKeySet = ks;
    }

    protected long getProperSigningKeySet() {
        return this.mProperSigningKeySet;
    }

    protected void addUpgradeKeySet(java.lang.String alias) {
        if (alias == null) {
            return;
        }
        java.lang.Long ks = this.mKeySetAliases.get(alias);
        if (ks != null) {
            this.mUpgradeKeySets = com.android.internal.util.ArrayUtils.appendLong(this.mUpgradeKeySets, ks.longValue());
            return;
        }
        throw new java.lang.IllegalArgumentException("Upgrade keyset alias " + alias + "does not refer to a defined keyset alias!");
    }

    protected void addUpgradeKeySetById(long ks) {
        this.mUpgradeKeySets = com.android.internal.util.ArrayUtils.appendLong(this.mUpgradeKeySets, ks);
    }

    protected void removeAllUpgradeKeySets() {
        this.mUpgradeKeySets = null;
    }

    protected long[] getUpgradeKeySets() {
        return this.mUpgradeKeySets;
    }

    protected android.util.ArrayMap<java.lang.String, java.lang.Long> getAliases() {
        return this.mKeySetAliases;
    }

    protected void setAliases(java.util.Map<java.lang.String, java.lang.Long> newAliases) {
        removeAllDefinedKeySets();
        this.mKeySetAliases.putAll(newAliases);
    }

    protected void addDefinedKeySet(long ks, java.lang.String alias) {
        this.mKeySetAliases.put(alias, java.lang.Long.valueOf(ks));
    }

    protected void removeAllDefinedKeySets() {
        this.mKeySetAliases.erase();
    }

    protected boolean isUsingDefinedKeySets() {
        return this.mKeySetAliases.size() > 0;
    }

    protected boolean isUsingUpgradeKeySets() {
        return this.mUpgradeKeySets != null && this.mUpgradeKeySets.length > 0;
    }
}
