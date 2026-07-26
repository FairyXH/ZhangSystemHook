package com.android.server.pm.verify.domain.models;

/* JADX INFO: loaded from: classes2.dex */
public class DomainVerificationStateMap<ValueType> {
    private static final java.lang.String TAG = "DomainVerificationStateMap";
    private final android.util.ArrayMap<java.lang.String, ValueType> mPackageNameMap = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.util.UUID, ValueType> mDomainSetIdMap = new android.util.ArrayMap<>();

    public int size() {
        return this.mPackageNameMap.size();
    }

    public ValueType valueAt(int index) {
        return this.mPackageNameMap.valueAt(index);
    }

    public ValueType get(java.lang.String packageName) {
        return this.mPackageNameMap.get(packageName);
    }

    public ValueType get(java.util.UUID domainSetId) {
        return this.mDomainSetIdMap.get(domainSetId);
    }

    public void put(java.lang.String packageName, java.util.UUID domainSetId, ValueType valueType) {
        if (this.mPackageNameMap.containsKey(packageName)) {
            remove(packageName);
        }
        this.mPackageNameMap.put(packageName, valueType);
        this.mDomainSetIdMap.put(domainSetId, valueType);
    }

    public ValueType remove(java.lang.String packageName) {
        int index;
        ValueType valueRemoved = this.mPackageNameMap.remove(packageName);
        if (valueRemoved != null && (index = this.mDomainSetIdMap.indexOfValue(valueRemoved)) >= 0) {
            this.mDomainSetIdMap.removeAt(index);
        }
        return valueRemoved;
    }

    public ValueType remove(java.util.UUID domainSetId) {
        int index;
        ValueType valueRemoved = this.mDomainSetIdMap.remove(domainSetId);
        if (valueRemoved != null && (index = this.mPackageNameMap.indexOfValue(valueRemoved)) >= 0) {
            this.mPackageNameMap.removeAt(index);
        }
        return valueRemoved;
    }

    public java.util.List<java.lang.String> getPackageNames() {
        return new java.util.ArrayList(this.mPackageNameMap.keySet());
    }

    public java.util.Collection<ValueType> values() {
        return new java.util.ArrayList(this.mPackageNameMap.values());
    }

    public java.lang.String toString() {
        return "DomainVerificationStateMap{packageNameMap=" + this.mPackageNameMap + ", domainSetIdMap=" + this.mDomainSetIdMap + '}';
    }
}
