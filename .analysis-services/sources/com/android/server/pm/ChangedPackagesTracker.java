package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class ChangedPackagesTracker {
    private int mChangedPackagesSequenceNumber;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<android.util.SparseArray<java.lang.String>> mUserIdToSequenceToPackage = new android.util.SparseArray<>();
    private final android.util.SparseArray<java.util.Map<java.lang.String, java.lang.Integer>> mChangedPackagesSequenceNumbers = new android.util.SparseArray<>();

    ChangedPackagesTracker() {
    }

    public android.content.pm.ChangedPackages getChangedPackages(int sequenceNumber, int userId) {
        synchronized (this.mLock) {
            if (sequenceNumber >= this.mChangedPackagesSequenceNumber) {
                return null;
            }
            android.util.SparseArray<java.lang.String> changedPackages = this.mUserIdToSequenceToPackage.get(userId);
            if (changedPackages == null) {
                return null;
            }
            java.util.List<java.lang.String> packageNames = new java.util.ArrayList<>(this.mChangedPackagesSequenceNumber - sequenceNumber);
            for (int i = sequenceNumber; i < this.mChangedPackagesSequenceNumber; i++) {
                java.lang.String packageName = changedPackages.get(i);
                if (packageName != null) {
                    packageNames.add(packageName);
                }
            }
            return packageNames.isEmpty() ? null : new android.content.pm.ChangedPackages(this.mChangedPackagesSequenceNumber, packageNames);
        }
    }

    int getSequenceNumber() {
        return this.mChangedPackagesSequenceNumber;
    }

    void iterateAll(java.util.function.BiConsumer<java.lang.Integer, android.util.SparseArray<android.util.SparseArray<java.lang.String>>> sequenceNumberAndValues) {
        synchronized (this.mLock) {
            sequenceNumberAndValues.accept(java.lang.Integer.valueOf(this.mChangedPackagesSequenceNumber), this.mUserIdToSequenceToPackage);
        }
    }

    void updateSequenceNumber(java.lang.String packageName, int[] userList) {
        synchronized (this.mLock) {
            for (int i = userList.length - 1; i >= 0; i--) {
                int userId = userList[i];
                android.util.SparseArray<java.lang.String> changedPackages = this.mUserIdToSequenceToPackage.get(userId);
                if (changedPackages == null) {
                    changedPackages = new android.util.SparseArray<>();
                    this.mUserIdToSequenceToPackage.put(userId, changedPackages);
                }
                java.util.Map<java.lang.String, java.lang.Integer> sequenceNumbers = this.mChangedPackagesSequenceNumbers.get(userId);
                if (sequenceNumbers == null) {
                    sequenceNumbers = new java.util.HashMap();
                    this.mChangedPackagesSequenceNumbers.put(userId, sequenceNumbers);
                }
                java.lang.Integer sequenceNumber = sequenceNumbers.get(packageName);
                if (sequenceNumber != null) {
                    changedPackages.remove(sequenceNumber.intValue());
                }
                changedPackages.put(this.mChangedPackagesSequenceNumber, packageName);
                sequenceNumbers.put(packageName, java.lang.Integer.valueOf(this.mChangedPackagesSequenceNumber));
            }
            int i2 = this.mChangedPackagesSequenceNumber;
            this.mChangedPackagesSequenceNumber = i2 + 1;
        }
    }
}
