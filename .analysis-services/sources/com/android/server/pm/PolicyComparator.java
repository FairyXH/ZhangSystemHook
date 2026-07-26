package com.android.server.pm;

/* JADX INFO: compiled from: SELinuxMMAC.java */
/* JADX INFO: loaded from: classes2.dex */
final class PolicyComparator implements java.util.Comparator<com.android.server.pm.Policy> {
    private boolean duplicateFound = false;

    PolicyComparator() {
    }

    public boolean foundDuplicate() {
        return this.duplicateFound;
    }

    @Override // java.util.Comparator
    public int compare(com.android.server.pm.Policy p1, com.android.server.pm.Policy p2) {
        if (p1.hasInnerPackages() != p2.hasInnerPackages()) {
            return p1.hasInnerPackages() ? -1 : 1;
        }
        if (p1.getSignatures().equals(p2.getSignatures())) {
            if (p1.hasGlobalSeinfo()) {
                this.duplicateFound = true;
                android.util.Slog.e("SELinuxMMAC", "Duplicate policy entry: " + p1.toString());
            }
            java.util.Map<java.lang.String, java.lang.String> p1Packages = p1.getInnerPackages();
            java.util.Map<java.lang.String, java.lang.String> p2Packages = p2.getInnerPackages();
            if (!java.util.Collections.disjoint(p1Packages.keySet(), p2Packages.keySet())) {
                this.duplicateFound = true;
                android.util.Slog.e("SELinuxMMAC", "Duplicate policy entry: " + p1.toString());
                return 0;
            }
            return 0;
        }
        return 0;
    }
}
