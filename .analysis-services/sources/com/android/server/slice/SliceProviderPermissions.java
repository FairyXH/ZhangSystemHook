package com.android.server.slice;

/* JADX INFO: loaded from: classes3.dex */
public class SliceProviderPermissions implements com.android.server.slice.DirtyTracker, com.android.server.slice.DirtyTracker.Persistable {
    private static final java.lang.String ATTR_AUTHORITY = "authority";
    private static final java.lang.String ATTR_PKG = "pkg";
    private static final java.lang.String NAMESPACE = null;
    private static final java.lang.String TAG = "SliceProviderPermissions";
    private static final java.lang.String TAG_AUTHORITY = "authority";
    private static final java.lang.String TAG_PKG = "pkg";
    static final java.lang.String TAG_PROVIDER = "provider";
    private final android.util.ArrayMap<java.lang.String, com.android.server.slice.SliceProviderPermissions.SliceAuthority> mAuths = new android.util.ArrayMap<>();
    private final com.android.server.slice.SlicePermissionManager.PkgUser mPkg;
    private final com.android.server.slice.DirtyTracker mTracker;

    public SliceProviderPermissions(com.android.server.slice.SlicePermissionManager.PkgUser pkg, com.android.server.slice.DirtyTracker tracker) {
        this.mPkg = pkg;
        this.mTracker = tracker;
    }

    public com.android.server.slice.SlicePermissionManager.PkgUser getPkg() {
        return this.mPkg;
    }

    public synchronized java.util.Collection<com.android.server.slice.SliceProviderPermissions.SliceAuthority> getAuthorities() {
        return new java.util.ArrayList(this.mAuths.values());
    }

    public synchronized com.android.server.slice.SliceProviderPermissions.SliceAuthority getOrCreateAuthority(java.lang.String authority) {
        com.android.server.slice.SliceProviderPermissions.SliceAuthority ret;
        ret = this.mAuths.get(authority);
        if (ret == null) {
            ret = new com.android.server.slice.SliceProviderPermissions.SliceAuthority(authority, this);
            this.mAuths.put(authority, ret);
            onPersistableDirty(ret);
        }
        return ret;
    }

    @Override // com.android.server.slice.DirtyTracker
    public void onPersistableDirty(com.android.server.slice.DirtyTracker.Persistable obj) {
        this.mTracker.onPersistableDirty(this);
    }

    @Override // com.android.server.slice.DirtyTracker.Persistable
    public java.lang.String getFileName() {
        return getFileName(this.mPkg);
    }

    @Override // com.android.server.slice.DirtyTracker.Persistable
    public synchronized void writeTo(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
        out.startTag(NAMESPACE, TAG_PROVIDER);
        out.attribute(NAMESPACE, "pkg", this.mPkg.toString());
        int N = this.mAuths.size();
        for (int i = 0; i < N; i++) {
            out.startTag(NAMESPACE, "authority");
            out.attribute(NAMESPACE, "authority", this.mAuths.valueAt(i).mAuthority);
            this.mAuths.valueAt(i).writeTo(out);
            out.endTag(NAMESPACE, "authority");
        }
        out.endTag(NAMESPACE, TAG_PROVIDER);
    }

    public static com.android.server.slice.SliceProviderPermissions createFrom(org.xmlpull.v1.XmlPullParser parser, com.android.server.slice.DirtyTracker tracker) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        while (true) {
            if (parser.getEventType() == 2 && TAG_PROVIDER.equals(parser.getName())) {
                break;
            }
            parser.next();
        }
        int depth = parser.getDepth();
        com.android.server.slice.SlicePermissionManager.PkgUser pkgUser = new com.android.server.slice.SlicePermissionManager.PkgUser(parser.getAttributeValue(NAMESPACE, "pkg"));
        com.android.server.slice.SliceProviderPermissions provider = new com.android.server.slice.SliceProviderPermissions(pkgUser, tracker);
        parser.next();
        while (parser.getDepth() > depth) {
            if (parser.getEventType() == 2 && "authority".equals(parser.getName())) {
                try {
                    com.android.server.slice.SliceProviderPermissions.SliceAuthority authority = new com.android.server.slice.SliceProviderPermissions.SliceAuthority(parser.getAttributeValue(NAMESPACE, "authority"), provider);
                    authority.readFrom(parser);
                    provider.mAuths.put(authority.getAuthority(), authority);
                } catch (java.lang.IllegalArgumentException e) {
                    android.util.Slog.e(TAG, "Couldn't read PkgUser", e);
                }
            }
            parser.next();
        }
        return provider;
    }

    public static java.lang.String getFileName(com.android.server.slice.SlicePermissionManager.PkgUser pkg) {
        return java.lang.String.format("provider_%s", pkg.toString());
    }

    public static class SliceAuthority implements com.android.server.slice.DirtyTracker.Persistable {
        private final java.lang.String mAuthority;
        private final android.util.ArraySet<com.android.server.slice.SlicePermissionManager.PkgUser> mPkgs = new android.util.ArraySet<>();
        private final com.android.server.slice.DirtyTracker mTracker;

        public SliceAuthority(java.lang.String authority, com.android.server.slice.DirtyTracker tracker) {
            this.mAuthority = authority;
            this.mTracker = tracker;
        }

        public java.lang.String getAuthority() {
            return this.mAuthority;
        }

        public synchronized void addPkg(com.android.server.slice.SlicePermissionManager.PkgUser pkg) {
            if (this.mPkgs.add(pkg)) {
                this.mTracker.onPersistableDirty(this);
            }
        }

        public synchronized void removePkg(com.android.server.slice.SlicePermissionManager.PkgUser pkg) {
            if (this.mPkgs.remove(pkg)) {
                this.mTracker.onPersistableDirty(this);
            }
        }

        public synchronized java.util.Collection<com.android.server.slice.SlicePermissionManager.PkgUser> getPkgs() {
            return new android.util.ArraySet((android.util.ArraySet) this.mPkgs);
        }

        @Override // com.android.server.slice.DirtyTracker.Persistable
        public java.lang.String getFileName() {
            return null;
        }

        @Override // com.android.server.slice.DirtyTracker.Persistable
        public synchronized void writeTo(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
            int N = this.mPkgs.size();
            for (int i = 0; i < N; i++) {
                out.startTag(com.android.server.slice.SliceProviderPermissions.NAMESPACE, "pkg");
                out.text(this.mPkgs.valueAt(i).toString());
                out.endTag(com.android.server.slice.SliceProviderPermissions.NAMESPACE, "pkg");
            }
        }

        public synchronized void readFrom(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            parser.next();
            int depth = parser.getDepth();
            while (parser.getDepth() >= depth) {
                if (parser.getEventType() == 2 && "pkg".equals(parser.getName())) {
                    this.mPkgs.add(new com.android.server.slice.SlicePermissionManager.PkgUser(parser.nextText()));
                }
                parser.next();
            }
        }

        public boolean equals(java.lang.Object obj) {
            if (!getClass().equals(obj != null ? obj.getClass() : null)) {
                return false;
            }
            com.android.server.slice.SliceProviderPermissions.SliceAuthority other = (com.android.server.slice.SliceProviderPermissions.SliceAuthority) obj;
            return java.util.Objects.equals(this.mAuthority, other.mAuthority) && java.util.Objects.equals(this.mPkgs, other.mPkgs);
        }

        public java.lang.String toString() {
            return java.lang.String.format("(%s: %s)", this.mAuthority, this.mPkgs.toString());
        }
    }
}
