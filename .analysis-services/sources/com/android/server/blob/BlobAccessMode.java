package com.android.server.blob;

/* JADX INFO: loaded from: classes.dex */
class BlobAccessMode {
    public static final int ACCESS_TYPE_ALLOWLIST = 8;
    public static final int ACCESS_TYPE_PRIVATE = 1;
    public static final int ACCESS_TYPE_PUBLIC = 2;
    public static final int ACCESS_TYPE_SAME_SIGNATURE = 4;
    private int mAccessType = 1;
    private final android.util.ArraySet<com.android.server.blob.BlobAccessMode.PackageIdentifier> mAllowedPackages = new android.util.ArraySet<>();

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @interface AccessType {
    }

    BlobAccessMode() {
    }

    void allow(com.android.server.blob.BlobAccessMode other) {
        if ((other.mAccessType & 8) != 0) {
            this.mAllowedPackages.addAll((android.util.ArraySet<? extends com.android.server.blob.BlobAccessMode.PackageIdentifier>) other.mAllowedPackages);
        }
        this.mAccessType |= other.mAccessType;
    }

    void allowPublicAccess() {
        this.mAccessType |= 2;
    }

    void allowSameSignatureAccess() {
        this.mAccessType |= 4;
    }

    void allowPackageAccess(java.lang.String packageName, byte[] certificate) {
        this.mAccessType |= 8;
        this.mAllowedPackages.add(com.android.server.blob.BlobAccessMode.PackageIdentifier.create(packageName, certificate));
    }

    boolean isPublicAccessAllowed() {
        return (this.mAccessType & 2) != 0;
    }

    boolean isSameSignatureAccessAllowed() {
        return (this.mAccessType & 4) != 0;
    }

    boolean isPackageAccessAllowed(java.lang.String packageName, byte[] certificate) {
        if ((this.mAccessType & 8) == 0) {
            return false;
        }
        return this.mAllowedPackages.contains(com.android.server.blob.BlobAccessMode.PackageIdentifier.create(packageName, certificate));
    }

    boolean isAccessAllowedForCaller(android.content.Context context, java.lang.String callingPackage, int callingUid, int committerUid) {
        if ((this.mAccessType & 2) != 0) {
            return true;
        }
        if ((this.mAccessType & 4) != 0 && checkSignatures(callingUid, committerUid)) {
            return true;
        }
        if ((this.mAccessType & 8) != 0) {
            android.os.UserHandle callingUser = android.os.UserHandle.of(android.os.UserHandle.getUserId(callingUid));
            android.content.pm.PackageManager pm = context.createContextAsUser(callingUser, 0).getPackageManager();
            for (int i = 0; i < this.mAllowedPackages.size(); i++) {
                com.android.server.blob.BlobAccessMode.PackageIdentifier packageIdentifier = this.mAllowedPackages.valueAt(i);
                if (packageIdentifier.packageName.equals(callingPackage) && pm.hasSigningCertificate(callingPackage, packageIdentifier.certificate, 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkSignatures(int uid1, int uid2) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return ((android.content.pm.PackageManagerInternal) com.android.server.LocalServices.getService(android.content.pm.PackageManagerInternal.class)).checkUidSignaturesForAllUsers(uid1, uid2) == 0;
        } finally {
            android.os.Binder.restoreCallingIdentity(token);
        }
    }

    int getAccessType() {
        return this.mAccessType;
    }

    int getAllowedPackagesCount() {
        return this.mAllowedPackages.size();
    }

    void dump(android.util.IndentingPrintWriter fout) {
        fout.println("accessType: " + android.util.DebugUtils.flagsToString(com.android.server.blob.BlobAccessMode.class, "ACCESS_TYPE_", this.mAccessType));
        fout.print("Explicitly allowed pkgs:");
        if (this.mAllowedPackages.isEmpty()) {
            fout.println(" (Empty)");
            return;
        }
        fout.increaseIndent();
        int count = this.mAllowedPackages.size();
        for (int i = 0; i < count; i++) {
            fout.println(this.mAllowedPackages.valueAt(i).toString());
        }
        fout.decreaseIndent();
    }

    void writeToXml(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
        com.android.internal.util.XmlUtils.writeIntAttribute(out, "t", this.mAccessType);
        int count = this.mAllowedPackages.size();
        for (int i = 0; i < count; i++) {
            out.startTag(null, "wl");
            com.android.server.blob.BlobAccessMode.PackageIdentifier packageIdentifier = this.mAllowedPackages.valueAt(i);
            com.android.internal.util.XmlUtils.writeStringAttribute(out, "p", packageIdentifier.packageName);
            com.android.internal.util.XmlUtils.writeByteArrayAttribute(out, "ct", packageIdentifier.certificate);
            out.endTag(null, "wl");
        }
    }

    static com.android.server.blob.BlobAccessMode createFromXml(org.xmlpull.v1.XmlPullParser in) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.server.blob.BlobAccessMode blobAccessMode = new com.android.server.blob.BlobAccessMode();
        int accessType = com.android.internal.util.XmlUtils.readIntAttribute(in, "t");
        blobAccessMode.mAccessType = accessType;
        int depth = in.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(in, depth)) {
            if ("wl".equals(in.getName())) {
                java.lang.String packageName = com.android.internal.util.XmlUtils.readStringAttribute(in, "p");
                byte[] certificate = com.android.internal.util.XmlUtils.readByteArrayAttribute(in, "ct");
                blobAccessMode.allowPackageAccess(packageName, certificate);
            }
        }
        return blobAccessMode;
    }

    private static final class PackageIdentifier {
        public final byte[] certificate;
        public final java.lang.String packageName;

        private PackageIdentifier(java.lang.String packageName, byte[] certificate) {
            this.packageName = packageName;
            this.certificate = certificate;
        }

        public static com.android.server.blob.BlobAccessMode.PackageIdentifier create(java.lang.String packageName, byte[] certificate) {
            return new com.android.server.blob.BlobAccessMode.PackageIdentifier(packageName, certificate);
        }

        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || !(obj instanceof com.android.server.blob.BlobAccessMode.PackageIdentifier)) {
                return false;
            }
            com.android.server.blob.BlobAccessMode.PackageIdentifier other = (com.android.server.blob.BlobAccessMode.PackageIdentifier) obj;
            if (this.packageName.equals(other.packageName) && java.util.Arrays.equals(this.certificate, other.certificate)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return java.util.Objects.hash(this.packageName, java.lang.Integer.valueOf(java.util.Arrays.hashCode(this.certificate)));
        }

        public java.lang.String toString() {
            return "[" + this.packageName + ", " + android.util.Base64.encodeToString(this.certificate, 2) + "]";
        }
    }
}
