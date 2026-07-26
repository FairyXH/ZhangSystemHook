package com.android.server.slice;

/* JADX INFO: loaded from: classes3.dex */
public class SliceClientPermissions implements com.android.server.slice.DirtyTracker, com.android.server.slice.DirtyTracker.Persistable {
    private static final java.lang.String ATTR_AUTHORITY = "authority";
    private static final java.lang.String ATTR_FULL_ACCESS = "fullAccess";
    private static final java.lang.String ATTR_PKG = "pkg";
    private static final java.lang.String NAMESPACE = null;
    private static final java.lang.String TAG = "SliceClientPermissions";
    private static final java.lang.String TAG_AUTHORITY = "authority";
    static final java.lang.String TAG_CLIENT = "client";
    private static final java.lang.String TAG_PATH = "path";
    private final android.util.ArrayMap<com.android.server.slice.SlicePermissionManager.PkgUser, com.android.server.slice.SliceClientPermissions.SliceAuthority> mAuths = new android.util.ArrayMap<>();
    private boolean mHasFullAccess;
    private final com.android.server.slice.SlicePermissionManager.PkgUser mPkg;
    private final com.android.server.slice.DirtyTracker mTracker;

    public SliceClientPermissions(com.android.server.slice.SlicePermissionManager.PkgUser pkg, com.android.server.slice.DirtyTracker tracker) {
        this.mPkg = pkg;
        this.mTracker = tracker;
    }

    public com.android.server.slice.SlicePermissionManager.PkgUser getPkg() {
        return this.mPkg;
    }

    public synchronized java.util.Collection<com.android.server.slice.SliceClientPermissions.SliceAuthority> getAuthorities() {
        return new java.util.ArrayList(this.mAuths.values());
    }

    public synchronized com.android.server.slice.SliceClientPermissions.SliceAuthority getOrCreateAuthority(com.android.server.slice.SlicePermissionManager.PkgUser authority, com.android.server.slice.SlicePermissionManager.PkgUser provider) {
        com.android.server.slice.SliceClientPermissions.SliceAuthority ret;
        ret = this.mAuths.get(authority);
        if (ret == null) {
            ret = new com.android.server.slice.SliceClientPermissions.SliceAuthority(authority.getPkg(), provider, this);
            this.mAuths.put(authority, ret);
            onPersistableDirty(ret);
        }
        return ret;
    }

    public synchronized com.android.server.slice.SliceClientPermissions.SliceAuthority getAuthority(com.android.server.slice.SlicePermissionManager.PkgUser authority) {
        return this.mAuths.get(authority);
    }

    public boolean hasFullAccess() {
        return this.mHasFullAccess;
    }

    public void setHasFullAccess(boolean hasFullAccess) {
        if (this.mHasFullAccess == hasFullAccess) {
            return;
        }
        this.mHasFullAccess = hasFullAccess;
        this.mTracker.onPersistableDirty(this);
    }

    public void removeAuthority(java.lang.String authority, int userId) {
        if (this.mAuths.remove(new com.android.server.slice.SlicePermissionManager.PkgUser(authority, userId)) != null) {
            this.mTracker.onPersistableDirty(this);
        }
    }

    public synchronized boolean hasPermission(android.net.Uri uri, int userId) {
        boolean z = false;
        if (!java.util.Objects.equals(com.android.server.wm.ActivityTaskManagerInternal.ASSIST_KEY_CONTENT, uri.getScheme())) {
            return false;
        }
        com.android.server.slice.SliceClientPermissions.SliceAuthority authority = getAuthority(new com.android.server.slice.SlicePermissionManager.PkgUser(uri.getAuthority(), userId));
        if (authority != null) {
            if (authority.hasPermission(uri.getPathSegments())) {
                z = true;
            }
        }
        return z;
    }

    public void grantUri(android.net.Uri uri, com.android.server.slice.SlicePermissionManager.PkgUser providerPkg) {
        com.android.server.slice.SliceClientPermissions.SliceAuthority authority = getOrCreateAuthority(new com.android.server.slice.SlicePermissionManager.PkgUser(uri.getAuthority(), providerPkg.getUserId()), providerPkg);
        authority.addPath(uri.getPathSegments());
    }

    public void revokeUri(android.net.Uri uri, com.android.server.slice.SlicePermissionManager.PkgUser providerPkg) {
        com.android.server.slice.SliceClientPermissions.SliceAuthority authority = getOrCreateAuthority(new com.android.server.slice.SlicePermissionManager.PkgUser(uri.getAuthority(), providerPkg.getUserId()), providerPkg);
        authority.removePath(uri.getPathSegments());
    }

    public void clear() {
        if (this.mHasFullAccess || !this.mAuths.isEmpty()) {
            this.mHasFullAccess = false;
            this.mAuths.clear();
            onPersistableDirty(this);
        }
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
        out.startTag(NAMESPACE, TAG_CLIENT);
        out.attribute(NAMESPACE, ATTR_PKG, this.mPkg.toString());
        out.attribute(NAMESPACE, ATTR_FULL_ACCESS, this.mHasFullAccess ? "1" : "0");
        int N = this.mAuths.size();
        for (int i = 0; i < N; i++) {
            out.startTag(NAMESPACE, "authority");
            out.attribute(NAMESPACE, "authority", this.mAuths.valueAt(i).mAuthority);
            out.attribute(NAMESPACE, ATTR_PKG, this.mAuths.valueAt(i).mPkg.toString());
            this.mAuths.valueAt(i).writeTo(out);
            out.endTag(NAMESPACE, "authority");
        }
        out.endTag(NAMESPACE, TAG_CLIENT);
    }

    public static com.android.server.slice.SliceClientPermissions createFrom(org.xmlpull.v1.XmlPullParser parser, com.android.server.slice.DirtyTracker tracker) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        while (true) {
            if (parser.getEventType() != 2 || !TAG_CLIENT.equals(parser.getName())) {
                int depth = parser.getEventType();
                if (depth == 1) {
                    throw new org.xmlpull.v1.XmlPullParserException("Can't find client tag in xml");
                }
                parser.next();
            } else {
                int depth2 = parser.getDepth();
                com.android.server.slice.SlicePermissionManager.PkgUser pkgUser = new com.android.server.slice.SlicePermissionManager.PkgUser(parser.getAttributeValue(NAMESPACE, ATTR_PKG));
                com.android.server.slice.SliceClientPermissions provider = new com.android.server.slice.SliceClientPermissions(pkgUser, tracker);
                java.lang.String fullAccess = parser.getAttributeValue(NAMESPACE, ATTR_FULL_ACCESS);
                if (fullAccess == null) {
                    fullAccess = "0";
                }
                provider.mHasFullAccess = java.lang.Integer.parseInt(fullAccess) != 0;
                parser.next();
                while (parser.getDepth() > depth2) {
                    if (parser.getEventType() == 1) {
                        return provider;
                    }
                    if (parser.getEventType() == 2 && "authority".equals(parser.getName())) {
                        try {
                            com.android.server.slice.SlicePermissionManager.PkgUser pkg = new com.android.server.slice.SlicePermissionManager.PkgUser(parser.getAttributeValue(NAMESPACE, ATTR_PKG));
                            com.android.server.slice.SliceClientPermissions.SliceAuthority authority = new com.android.server.slice.SliceClientPermissions.SliceAuthority(parser.getAttributeValue(NAMESPACE, "authority"), pkg, provider);
                            authority.readFrom(parser);
                            provider.mAuths.put(new com.android.server.slice.SlicePermissionManager.PkgUser(authority.getAuthority(), pkg.getUserId()), authority);
                        } catch (java.lang.IllegalArgumentException e) {
                            android.util.Slog.e(TAG, "Couldn't read PkgUser", e);
                        }
                    }
                    parser.next();
                }
                return provider;
            }
        }
    }

    public static java.lang.String getFileName(com.android.server.slice.SlicePermissionManager.PkgUser pkg) {
        return java.lang.String.format("client_%s", pkg.toString());
    }

    public static class SliceAuthority implements com.android.server.slice.DirtyTracker.Persistable {
        public static final java.lang.String DELIMITER = "/";
        private final java.lang.String mAuthority;
        private final android.util.ArraySet<java.lang.String[]> mPaths = new android.util.ArraySet<>();
        private final com.android.server.slice.SlicePermissionManager.PkgUser mPkg;
        private final com.android.server.slice.DirtyTracker mTracker;

        public SliceAuthority(java.lang.String authority, com.android.server.slice.SlicePermissionManager.PkgUser pkg, com.android.server.slice.DirtyTracker tracker) {
            this.mAuthority = authority;
            this.mPkg = pkg;
            this.mTracker = tracker;
        }

        public java.lang.String getAuthority() {
            return this.mAuthority;
        }

        public com.android.server.slice.SlicePermissionManager.PkgUser getPkg() {
            return this.mPkg;
        }

        void addPath(java.util.List<java.lang.String> path) {
            java.lang.String[] pathSegs = (java.lang.String[]) path.toArray(new java.lang.String[path.size()]);
            for (int i = this.mPaths.size() - 1; i >= 0; i--) {
                java.lang.String[] existing = this.mPaths.valueAt(i);
                if (isPathPrefixMatch(existing, pathSegs)) {
                    return;
                }
                if (isPathPrefixMatch(pathSegs, existing)) {
                    this.mPaths.removeAt(i);
                }
            }
            this.mPaths.add(pathSegs);
            this.mTracker.onPersistableDirty(this);
        }

        void removePath(java.util.List<java.lang.String> path) {
            boolean changed = false;
            java.lang.String[] pathSegs = (java.lang.String[]) path.toArray(new java.lang.String[path.size()]);
            for (int i = this.mPaths.size() - 1; i >= 0; i--) {
                java.lang.String[] existing = this.mPaths.valueAt(i);
                if (isPathPrefixMatch(pathSegs, existing)) {
                    changed = true;
                    this.mPaths.removeAt(i);
                }
            }
            if (changed) {
                this.mTracker.onPersistableDirty(this);
            }
        }

        public synchronized java.util.Collection<java.lang.String[]> getPaths() {
            return new android.util.ArraySet((android.util.ArraySet) this.mPaths);
        }

        public boolean hasPermission(java.util.List<java.lang.String> path) {
            for (java.lang.String[] p : this.mPaths) {
                if (isPathPrefixMatch(p, (java.lang.String[]) path.toArray(new java.lang.String[path.size()]))) {
                    return true;
                }
            }
            return false;
        }

        private boolean isPathPrefixMatch(java.lang.String[] prefix, java.lang.String[] path) {
            int prefixSize = prefix.length;
            if (path.length < prefixSize) {
                return false;
            }
            for (int i = 0; i < prefixSize; i++) {
                if (!java.util.Objects.equals(path[i], prefix[i])) {
                    return false;
                }
            }
            return true;
        }

        @Override // com.android.server.slice.DirtyTracker.Persistable
        public java.lang.String getFileName() {
            return null;
        }

        @Override // com.android.server.slice.DirtyTracker.Persistable
        public synchronized void writeTo(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
            int N = this.mPaths.size();
            for (int i = 0; i < N; i++) {
                java.lang.String[] segments = this.mPaths.valueAt(i);
                if (segments != null) {
                    out.startTag(com.android.server.slice.SliceClientPermissions.NAMESPACE, com.android.server.slice.SliceClientPermissions.TAG_PATH);
                    out.text(encodeSegments(segments));
                    out.endTag(com.android.server.slice.SliceClientPermissions.NAMESPACE, com.android.server.slice.SliceClientPermissions.TAG_PATH);
                }
            }
        }

        public synchronized void readFrom(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            parser.next();
            int depth = parser.getDepth();
            while (parser.getDepth() >= depth) {
                if (parser.getEventType() == 2 && com.android.server.slice.SliceClientPermissions.TAG_PATH.equals(parser.getName())) {
                    this.mPaths.add(decodeSegments(parser.nextText()));
                }
                parser.next();
            }
        }

        private java.lang.String encodeSegments(java.lang.String[] s) {
            java.lang.String[] out = new java.lang.String[s.length];
            for (int i = 0; i < s.length; i++) {
                out[i] = android.net.Uri.encode(s[i]);
            }
            return android.text.TextUtils.join(DELIMITER, out);
        }

        private java.lang.String[] decodeSegments(java.lang.String s) {
            java.lang.String[] sets = s.split(DELIMITER, -1);
            for (int i = 0; i < sets.length; i++) {
                sets[i] = android.net.Uri.decode(sets[i]);
            }
            return sets;
        }

        public boolean equals(java.lang.Object obj) {
            if (!getClass().equals(obj != null ? obj.getClass() : null)) {
                return false;
            }
            com.android.server.slice.SliceClientPermissions.SliceAuthority other = (com.android.server.slice.SliceClientPermissions.SliceAuthority) obj;
            if (this.mPaths.size() != other.mPaths.size()) {
                return false;
            }
            java.util.ArrayList<java.lang.String[]> p1 = new java.util.ArrayList<>(this.mPaths);
            java.util.ArrayList<java.lang.String[]> p2 = new java.util.ArrayList<>(other.mPaths);
            p1.sort(java.util.Comparator.comparing(new java.util.function.Function() { // from class: com.android.server.slice.SliceClientPermissions$SliceAuthority$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj2) {
                    return android.text.TextUtils.join(",", (java.lang.String[]) obj2);
                }
            }));
            p2.sort(java.util.Comparator.comparing(new java.util.function.Function() { // from class: com.android.server.slice.SliceClientPermissions$SliceAuthority$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj2) {
                    return android.text.TextUtils.join(",", (java.lang.String[]) obj2);
                }
            }));
            for (int i = 0; i < p1.size(); i++) {
                java.lang.String[] a1 = p1.get(i);
                java.lang.String[] a2 = p2.get(i);
                if (a1.length != a2.length) {
                    return false;
                }
                for (int j = 0; j < a1.length; j++) {
                    if (!java.util.Objects.equals(a1[j], a2[j])) {
                        return false;
                    }
                }
            }
            return java.util.Objects.equals(this.mAuthority, other.mAuthority) && java.util.Objects.equals(this.mPkg, other.mPkg);
        }

        public java.lang.String toString() {
            return java.lang.String.format("(%s, %s: %s)", this.mAuthority, this.mPkg.toString(), pathToString(this.mPaths));
        }

        private java.lang.String pathToString(android.util.ArraySet<java.lang.String[]> paths) {
            return android.text.TextUtils.join(", ", (java.lang.Iterable) paths.stream().map(new java.util.function.Function() { // from class: com.android.server.slice.SliceClientPermissions$SliceAuthority$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return android.text.TextUtils.join(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER, (java.lang.String[]) obj);
                }
            }).collect(java.util.stream.Collectors.toList()));
        }
    }
}
