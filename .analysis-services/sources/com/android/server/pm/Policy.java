package com.android.server.pm;

/* JADX INFO: compiled from: SELinuxMMAC.java */
/* JADX INFO: loaded from: classes2.dex */
final class Policy {
    private final java.util.Set<android.content.pm.Signature> mCerts;
    private final java.util.Map<java.lang.String, java.lang.String> mPkgMap;
    private final java.lang.String mSeinfo;

    private Policy(com.android.server.pm.Policy.PolicyBuilder builder) {
        this.mSeinfo = builder.mSeinfo;
        this.mCerts = java.util.Collections.unmodifiableSet(builder.mCerts);
        this.mPkgMap = java.util.Collections.unmodifiableMap(builder.mPkgMap);
    }

    public java.util.Set<android.content.pm.Signature> getSignatures() {
        return this.mCerts;
    }

    public boolean hasInnerPackages() {
        return !this.mPkgMap.isEmpty();
    }

    public java.util.Map<java.lang.String, java.lang.String> getInnerPackages() {
        return this.mPkgMap;
    }

    public boolean hasGlobalSeinfo() {
        return this.mSeinfo != null;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (android.content.pm.Signature cert : this.mCerts) {
            sb.append("cert=" + cert.toCharsString().substring(0, 11) + "... ");
        }
        if (this.mSeinfo != null) {
            sb.append("seinfo=" + this.mSeinfo);
        }
        for (java.lang.String name : this.mPkgMap.keySet()) {
            sb.append(" " + name + "=" + this.mPkgMap.get(name));
        }
        return sb.toString();
    }

    public java.lang.String getMatchedSeInfo(com.android.server.pm.pkg.AndroidPackage pkg) {
        android.content.pm.Signature[] certs = (android.content.pm.Signature[]) this.mCerts.toArray(new android.content.pm.Signature[0]);
        if (pkg.getSigningDetails() != android.content.pm.SigningDetails.UNKNOWN && !android.content.pm.Signature.areExactMatch(pkg.getSigningDetails(), certs) && (certs.length > 1 || !pkg.getSigningDetails().hasCertificate(certs[0]))) {
            return null;
        }
        java.lang.String seinfoValue = this.mPkgMap.get(pkg.getPackageName());
        if (seinfoValue != null) {
            return seinfoValue;
        }
        return this.mSeinfo;
    }

    /* JADX INFO: compiled from: SELinuxMMAC.java */
    public static final class PolicyBuilder {
        private final java.util.Set<android.content.pm.Signature> mCerts = new java.util.HashSet(2);
        private final java.util.Map<java.lang.String, java.lang.String> mPkgMap = new java.util.HashMap(2);
        private java.lang.String mSeinfo;

        public com.android.server.pm.Policy.PolicyBuilder addSignature(java.lang.String cert) {
            if (cert == null) {
                java.lang.String err = "Invalid signature value " + cert;
                throw new java.lang.IllegalArgumentException(err);
            }
            this.mCerts.add(new android.content.pm.Signature(cert));
            return this;
        }

        public com.android.server.pm.Policy.PolicyBuilder setGlobalSeinfoOrThrow(java.lang.String seinfo) {
            if (!validateValue(seinfo)) {
                java.lang.String err = "Invalid seinfo value " + seinfo;
                throw new java.lang.IllegalArgumentException(err);
            }
            if (this.mSeinfo != null && !this.mSeinfo.equals(seinfo)) {
                throw new java.lang.IllegalStateException("Duplicate seinfo tag found");
            }
            this.mSeinfo = seinfo;
            return this;
        }

        public com.android.server.pm.Policy.PolicyBuilder addInnerPackageMapOrThrow(java.lang.String pkgName, java.lang.String seinfo) {
            if (!validateValue(pkgName)) {
                java.lang.String err = "Invalid package name " + pkgName;
                throw new java.lang.IllegalArgumentException(err);
            }
            if (!validateValue(seinfo)) {
                java.lang.String err2 = "Invalid seinfo value " + seinfo;
                throw new java.lang.IllegalArgumentException(err2);
            }
            java.lang.String pkgValue = this.mPkgMap.get(pkgName);
            if (pkgValue != null && !pkgValue.equals(seinfo)) {
                throw new java.lang.IllegalStateException("Conflicting seinfo value found");
            }
            this.mPkgMap.put(pkgName, seinfo);
            return this;
        }

        private boolean validateValue(java.lang.String name) {
            if (name == null || !name.matches("\\A[\\.\\w]+\\z")) {
                return false;
            }
            return true;
        }

        public com.android.server.pm.Policy build() {
            com.android.server.pm.Policy p = new com.android.server.pm.Policy(this);
            if (p.mCerts.isEmpty()) {
                throw new java.lang.IllegalStateException("Missing certs with signer tag. Expecting at least one.");
            }
            if (!((p.mSeinfo == null) ^ p.mPkgMap.isEmpty())) {
                throw new java.lang.IllegalStateException("Only seinfo tag XOR package tags are allowed within a signer stanza.");
            }
            return p;
        }
    }
}
