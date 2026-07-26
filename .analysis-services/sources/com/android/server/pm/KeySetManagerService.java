package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
public class KeySetManagerService {
    public static final int CURRENT_VERSION = 1;
    public static final int FIRST_VERSION = 1;
    public static final long KEYSET_NOT_FOUND = -1;
    protected static final long PUBLIC_KEY_NOT_FOUND = -1;
    static final java.lang.String TAG = "KeySetManagerService";
    private long lastIssuedKeyId;
    private long lastIssuedKeySetId;
    protected final android.util.LongSparseArray<android.util.ArraySet<java.lang.Long>> mKeySetMapping;
    private final android.util.LongSparseArray<com.android.server.pm.KeySetHandle> mKeySets;
    private final com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.PackageSetting> mPackages;
    private final android.util.LongSparseArray<com.android.server.pm.KeySetManagerService.PublicKeyHandle> mPublicKeys;

    class PublicKeyHandle {
        private final long mId;
        private final java.security.PublicKey mKey;
        private int mRefCount;

        public PublicKeyHandle(long id, java.security.PublicKey key) {
            this.mId = id;
            this.mRefCount = 1;
            this.mKey = key;
        }

        private PublicKeyHandle(long id, int refCount, java.security.PublicKey key) {
            this.mId = id;
            this.mRefCount = refCount;
            this.mKey = key;
        }

        public long getId() {
            return this.mId;
        }

        public java.security.PublicKey getKey() {
            return this.mKey;
        }

        public int getRefCountLPr() {
            return this.mRefCount;
        }

        public void incrRefCountLPw() {
            this.mRefCount++;
        }

        public long decrRefCountLPw() {
            this.mRefCount--;
            return this.mRefCount;
        }
    }

    public KeySetManagerService(com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.PackageSetting> packages) {
        this.lastIssuedKeySetId = 0L;
        this.lastIssuedKeyId = 0L;
        this.mKeySets = new android.util.LongSparseArray<>();
        this.mPublicKeys = new android.util.LongSparseArray<>();
        this.mKeySetMapping = new android.util.LongSparseArray<>();
        this.mPackages = packages;
    }

    public KeySetManagerService(com.android.server.pm.KeySetManagerService other, com.android.server.utils.WatchedArrayMap<java.lang.String, com.android.server.pm.PackageSetting> packages) {
        this.lastIssuedKeySetId = 0L;
        this.lastIssuedKeyId = 0L;
        this.mKeySets = other.mKeySets.clone();
        this.mPublicKeys = other.mPublicKeys.clone();
        this.mKeySetMapping = other.mKeySetMapping.clone();
        this.mPackages = packages;
    }

    public boolean packageIsSignedByLPr(java.lang.String packageName, com.android.server.pm.KeySetHandle ks) {
        com.android.server.pm.PackageSetting pkg = this.mPackages.get(packageName);
        if (pkg == null) {
            throw new java.lang.NullPointerException("Invalid package name");
        }
        if (pkg.getKeySetData() == null) {
            throw new java.lang.NullPointerException("Package has no KeySet data");
        }
        long id = getIdByKeySetLPr(ks);
        if (id == -1) {
            return false;
        }
        android.util.ArraySet<java.lang.Long> pkgKeys = this.mKeySetMapping.get(pkg.getKeySetData().getProperSigningKeySet());
        android.util.ArraySet<java.lang.Long> testKeys = this.mKeySetMapping.get(id);
        return pkgKeys.containsAll(testKeys);
    }

    public boolean packageIsSignedByExactlyLPr(java.lang.String packageName, com.android.server.pm.KeySetHandle ks) {
        com.android.server.pm.PackageSetting pkg = this.mPackages.get(packageName);
        if (pkg == null) {
            throw new java.lang.NullPointerException("Invalid package name");
        }
        if (pkg.getKeySetData() == null || pkg.getKeySetData().getProperSigningKeySet() == -1) {
            throw new java.lang.NullPointerException("Package has no KeySet data");
        }
        long id = getIdByKeySetLPr(ks);
        if (id == -1) {
            return false;
        }
        android.util.ArraySet<java.lang.Long> pkgKeys = this.mKeySetMapping.get(pkg.getKeySetData().getProperSigningKeySet());
        android.util.ArraySet<java.lang.Long> testKeys = this.mKeySetMapping.get(id);
        return pkgKeys.equals(testKeys);
    }

    public void assertScannedPackageValid(com.android.server.pm.pkg.AndroidPackage pkg) throws com.android.server.pm.PackageManagerException {
        if (pkg == null || pkg.getPackageName() == null) {
            throw new com.android.server.pm.PackageManagerException(-2, "Passed invalid package to keyset validation.");
        }
        android.util.ArraySet<java.security.PublicKey> signingKeys = pkg.getSigningDetails().getPublicKeys();
        if (signingKeys == null || signingKeys.size() <= 0 || signingKeys.contains(null)) {
            throw new com.android.server.pm.PackageManagerException(-2, "Package has invalid signing-key-set.");
        }
        java.util.Map<java.lang.String, android.util.ArraySet<java.security.PublicKey>> definedMapping = pkg.getKeySetMapping();
        if (definedMapping != null) {
            if (definedMapping.containsKey(null) || definedMapping.containsValue(null)) {
                throw new com.android.server.pm.PackageManagerException(-2, "Package has null defined key set.");
            }
            for (android.util.ArraySet<java.security.PublicKey> value : definedMapping.values()) {
                if (value.size() <= 0 || value.contains(null)) {
                    throw new com.android.server.pm.PackageManagerException(-2, "Package has null/no public keys for defined key-sets.");
                }
            }
        }
        java.util.Set<java.lang.String> upgradeAliases = pkg.getUpgradeKeySets();
        if (upgradeAliases != null) {
            if (definedMapping == null || !definedMapping.keySet().containsAll(upgradeAliases)) {
                throw new com.android.server.pm.PackageManagerException(-2, "Package has upgrade-key-sets without corresponding definitions.");
            }
        }
    }

    public void addScannedPackageLPw(com.android.server.pm.pkg.AndroidPackage pkg) {
        java.util.Objects.requireNonNull(pkg, "Attempted to add null pkg to ksms.");
        java.util.Objects.requireNonNull(pkg.getPackageName(), "Attempted to add null pkg to ksms.");
        com.android.server.pm.PackageSetting ps = this.mPackages.get(pkg.getPackageName());
        java.util.Objects.requireNonNull(ps, "pkg: " + pkg.getPackageName() + "does not have a corresponding entry in mPackages.");
        addSigningKeySetToPackageLPw(ps, pkg.getSigningDetails().getPublicKeys());
        if (pkg.getKeySetMapping() != null) {
            addDefinedKeySetsToPackageLPw(ps, pkg.getKeySetMapping());
            if (pkg.getUpgradeKeySets() != null) {
                addUpgradeKeySetsToPackageLPw(ps, pkg.getUpgradeKeySets());
            }
        }
    }

    void addSigningKeySetToPackageLPw(com.android.server.pm.PackageSetting pkg, android.util.ArraySet<java.security.PublicKey> signingKeys) {
        long signingKeySetId = pkg.getKeySetData().getProperSigningKeySet();
        if (signingKeySetId != -1) {
            android.util.ArraySet<java.security.PublicKey> existingKeys = getPublicKeysFromKeySetLPr(signingKeySetId);
            if (existingKeys != null && existingKeys.equals(signingKeys)) {
                return;
            } else {
                decrementKeySetLPw(signingKeySetId);
            }
        }
        com.android.server.pm.KeySetHandle ks = addKeySetLPw(signingKeys);
        long id = ks.getId();
        pkg.getKeySetData().setProperSigningKeySet(id);
    }

    private long getIdByKeySetLPr(com.android.server.pm.KeySetHandle ks) {
        for (int keySetIndex = 0; keySetIndex < this.mKeySets.size(); keySetIndex++) {
            com.android.server.pm.KeySetHandle value = this.mKeySets.valueAt(keySetIndex);
            if (ks.equals(value)) {
                return this.mKeySets.keyAt(keySetIndex);
            }
        }
        return -1L;
    }

    void addDefinedKeySetsToPackageLPw(com.android.server.pm.PackageSetting pkg, java.util.Map<java.lang.String, android.util.ArraySet<java.security.PublicKey>> definedMapping) {
        android.util.ArrayMap<java.lang.String, java.lang.Long> prevDefinedKeySets = pkg.getKeySetData().getAliases();
        java.util.Map<java.lang.String, java.lang.Long> newKeySetAliases = new android.util.ArrayMap<>();
        for (java.util.Map.Entry<java.lang.String, android.util.ArraySet<java.security.PublicKey>> entry : definedMapping.entrySet()) {
            java.lang.String alias = entry.getKey();
            android.util.ArraySet<java.security.PublicKey> pubKeys = entry.getValue();
            if (alias != null && pubKeys != null && pubKeys.size() > 0) {
                com.android.server.pm.KeySetHandle ks = addKeySetLPw(pubKeys);
                newKeySetAliases.put(alias, java.lang.Long.valueOf(ks.getId()));
            }
        }
        int prevDefSize = prevDefinedKeySets.size();
        for (int i = 0; i < prevDefSize; i++) {
            decrementKeySetLPw(prevDefinedKeySets.valueAt(i).longValue());
        }
        pkg.getKeySetData().removeAllUpgradeKeySets();
        pkg.getKeySetData().setAliases(newKeySetAliases);
    }

    void addUpgradeKeySetsToPackageLPw(com.android.server.pm.PackageSetting pkg, java.util.Set<java.lang.String> upgradeAliases) {
        for (java.lang.String upgradeAlias : upgradeAliases) {
            pkg.getKeySetData().addUpgradeKeySet(upgradeAlias);
        }
    }

    public com.android.server.pm.KeySetHandle getKeySetByAliasAndPackageNameLPr(java.lang.String packageName, java.lang.String alias) {
        com.android.server.pm.PackageSetting p = this.mPackages.get(packageName);
        if (p == null || p.getKeySetData() == null) {
            return null;
        }
        android.util.ArrayMap<java.lang.String, java.lang.Long> aliases = p.getKeySetData().getAliases();
        java.lang.Long keySetId = aliases.get(alias);
        if (keySetId == null) {
            throw new java.lang.IllegalArgumentException("Unknown KeySet alias: " + alias + ", aliases = " + aliases);
        }
        return this.mKeySets.get(keySetId.longValue());
    }

    public boolean isIdValidKeySetId(long id) {
        return this.mKeySets.get(id) != null;
    }

    public boolean shouldCheckUpgradeKeySetLocked(com.android.server.pm.pkg.PackageStateInternal oldPs, com.android.server.pm.pkg.SharedUserApi sharedUserSetting, int scanFlags) {
        if (oldPs == null || (scanFlags & 512) != 0 || sharedUserSetting != null || !oldPs.getKeySetData().isUsingUpgradeKeySets()) {
            return false;
        }
        long[] upgradeKeySets = oldPs.getKeySetData().getUpgradeKeySets();
        for (int i = 0; i < upgradeKeySets.length; i++) {
            if (!isIdValidKeySetId(upgradeKeySets[i])) {
                android.util.Slog.wtf(TAG, "Package " + (oldPs.getPackageName() != null ? oldPs.getPackageName() : "<null>") + " contains upgrade-key-set reference to unknown key-set: " + upgradeKeySets[i] + " reverting to signatures check.");
                return false;
            }
        }
        return true;
    }

    public boolean checkUpgradeKeySetLocked(com.android.server.pm.pkg.PackageStateInternal oldPS, com.android.server.pm.pkg.AndroidPackage pkg) {
        long[] upgradeKeySets = oldPS.getKeySetData().getUpgradeKeySets();
        for (long j : upgradeKeySets) {
            java.util.Set<java.security.PublicKey> upgradeSet = getPublicKeysFromKeySetLPr(j);
            if (upgradeSet != null && pkg.getSigningDetails().getPublicKeys().containsAll(upgradeSet)) {
                return true;
            }
        }
        return false;
    }

    public android.util.ArraySet<java.security.PublicKey> getPublicKeysFromKeySetLPr(long id) {
        android.util.ArraySet<java.lang.Long> pkIds = this.mKeySetMapping.get(id);
        if (pkIds == null) {
            return null;
        }
        android.util.ArraySet<java.security.PublicKey> mPubKeys = new android.util.ArraySet<>();
        int pkSize = pkIds.size();
        for (int i = 0; i < pkSize; i++) {
            mPubKeys.add(this.mPublicKeys.get(pkIds.valueAt(i).longValue()).getKey());
        }
        return mPubKeys;
    }

    public com.android.server.pm.KeySetHandle getSigningKeySetByPackageNameLPr(java.lang.String packageName) {
        com.android.server.pm.PackageSetting p = this.mPackages.get(packageName);
        if (p == null || p.getKeySetData() == null || p.getKeySetData().getProperSigningKeySet() == -1) {
            return null;
        }
        return this.mKeySets.get(p.getKeySetData().getProperSigningKeySet());
    }

    private com.android.server.pm.KeySetHandle addKeySetLPw(android.util.ArraySet<java.security.PublicKey> keys) {
        if (keys == null || keys.size() == 0) {
            throw new java.lang.IllegalArgumentException("Cannot add an empty set of keys!");
        }
        android.util.ArraySet<java.lang.Long> addedKeyIds = new android.util.ArraySet<>(keys.size());
        int kSize = keys.size();
        for (int i = 0; i < kSize; i++) {
            addedKeyIds.add(java.lang.Long.valueOf(addPublicKeyLPw(keys.valueAt(i))));
        }
        long existingKeySetId = getIdFromKeyIdsLPr(addedKeyIds);
        if (existingKeySetId != -1) {
            for (int i2 = 0; i2 < kSize; i2++) {
                decrementPublicKeyLPw(addedKeyIds.valueAt(i2).longValue());
            }
            com.android.server.pm.KeySetHandle ks = this.mKeySets.get(existingKeySetId);
            ks.incrRefCountLPw();
            return ks;
        }
        long id = getFreeKeySetIDLPw();
        com.android.server.pm.KeySetHandle ks2 = new com.android.server.pm.KeySetHandle(id);
        this.mKeySets.put(id, ks2);
        this.mKeySetMapping.put(id, addedKeyIds);
        return ks2;
    }

    private void decrementKeySetLPw(long id) {
        com.android.server.pm.KeySetHandle ks = this.mKeySets.get(id);
        if (ks != null && ks.decrRefCountLPw() <= 0) {
            android.util.ArraySet<java.lang.Long> pubKeys = this.mKeySetMapping.get(id);
            int pkSize = pubKeys.size();
            for (int i = 0; i < pkSize; i++) {
                decrementPublicKeyLPw(pubKeys.valueAt(i).longValue());
            }
            this.mKeySets.delete(id);
            this.mKeySetMapping.delete(id);
        }
    }

    private void decrementPublicKeyLPw(long id) {
        com.android.server.pm.KeySetManagerService.PublicKeyHandle pk = this.mPublicKeys.get(id);
        if (pk != null && pk.decrRefCountLPw() <= 0) {
            this.mPublicKeys.delete(id);
        }
    }

    private long addPublicKeyLPw(java.security.PublicKey key) {
        java.util.Objects.requireNonNull(key, "Cannot add null public key!");
        long id = getIdForPublicKeyLPr(key);
        if (id != -1) {
            this.mPublicKeys.get(id).incrRefCountLPw();
            return id;
        }
        long id2 = getFreePublicKeyIdLPw();
        this.mPublicKeys.put(id2, new com.android.server.pm.KeySetManagerService.PublicKeyHandle(id2, key));
        return id2;
    }

    private long getIdFromKeyIdsLPr(java.util.Set<java.lang.Long> publicKeyIds) {
        for (int keyMapIndex = 0; keyMapIndex < this.mKeySetMapping.size(); keyMapIndex++) {
            android.util.ArraySet<java.lang.Long> value = this.mKeySetMapping.valueAt(keyMapIndex);
            if (value.equals(publicKeyIds)) {
                return this.mKeySetMapping.keyAt(keyMapIndex);
            }
        }
        return -1L;
    }

    private long getIdForPublicKeyLPr(java.security.PublicKey k) {
        java.lang.String encodedPublicKey = new java.lang.String(k.getEncoded());
        for (int publicKeyIndex = 0; publicKeyIndex < this.mPublicKeys.size(); publicKeyIndex++) {
            java.security.PublicKey value = this.mPublicKeys.valueAt(publicKeyIndex).getKey();
            java.lang.String encodedExistingKey = new java.lang.String(value.getEncoded());
            if (encodedPublicKey.equals(encodedExistingKey)) {
                return this.mPublicKeys.keyAt(publicKeyIndex);
            }
        }
        return -1L;
    }

    private long getFreeKeySetIDLPw() {
        this.lastIssuedKeySetId++;
        return this.lastIssuedKeySetId;
    }

    private long getFreePublicKeyIdLPw() {
        this.lastIssuedKeyId++;
        return this.lastIssuedKeyId;
    }

    public void removeAppKeySetDataLPw(java.lang.String packageName) {
        com.android.server.pm.PackageSetting pkg = this.mPackages.get(packageName);
        java.util.Objects.requireNonNull(pkg, "pkg name: " + packageName + "does not have a corresponding entry in mPackages.");
        long signingKeySetId = pkg.getKeySetData().getProperSigningKeySet();
        decrementKeySetLPw(signingKeySetId);
        android.util.ArrayMap<java.lang.String, java.lang.Long> definedKeySets = pkg.getKeySetData().getAliases();
        for (int i = 0; i < definedKeySets.size(); i++) {
            decrementKeySetLPw(definedKeySets.valueAt(i).longValue());
        }
        clearPackageKeySetDataLPw(pkg);
    }

    private void clearPackageKeySetDataLPw(com.android.server.pm.PackageSetting pkg) {
        pkg.getKeySetData().setProperSigningKeySet(-1L);
        pkg.getKeySetData().removeAllDefinedKeySets();
        pkg.getKeySetData().removeAllUpgradeKeySets();
    }

    @java.lang.Deprecated
    public java.lang.String encodePublicKey(java.security.PublicKey k) throws java.io.IOException {
        return new java.lang.String(android.util.Base64.encode(k.getEncoded(), 2));
    }

    public void dumpLPr(java.io.PrintWriter pw, java.lang.String packageName, com.android.server.pm.DumpState dumpState) {
        java.lang.String str = packageName;
        boolean printedHeader = false;
        for (java.util.Map.Entry<java.lang.String, com.android.server.pm.PackageSetting> e : this.mPackages.entrySet()) {
            java.lang.String keySetPackage = e.getKey();
            if (str == null || str.equals(keySetPackage)) {
                if (!printedHeader) {
                    if (dumpState.onTitlePrinted()) {
                        pw.println();
                    }
                    pw.println("Key Set Manager:");
                    printedHeader = true;
                }
                com.android.server.pm.PackageSetting pkg = e.getValue();
                pw.print("  [");
                pw.print(keySetPackage);
                pw.println("]");
                if (pkg.getKeySetData() != null) {
                    boolean printedLabel = false;
                    for (java.util.Map.Entry<java.lang.String, java.lang.Long> entry : pkg.getKeySetData().getAliases().entrySet()) {
                        if (!printedLabel) {
                            pw.print("      KeySets Aliases: ");
                            printedLabel = true;
                        } else {
                            pw.print(", ");
                        }
                        pw.print(entry.getKey());
                        pw.print('=');
                        pw.print(java.lang.Long.toString(entry.getValue().longValue()));
                    }
                    if (printedLabel) {
                        pw.println("");
                    }
                    boolean printedLabel2 = false;
                    if (pkg.getKeySetData().isUsingDefinedKeySets()) {
                        android.util.ArrayMap<java.lang.String, java.lang.Long> definedKeySets = pkg.getKeySetData().getAliases();
                        int dksSize = definedKeySets.size();
                        for (int i = 0; i < dksSize; i++) {
                            if (!printedLabel2) {
                                pw.print("      Defined KeySets: ");
                                printedLabel2 = true;
                            } else {
                                pw.print(", ");
                            }
                            pw.print(java.lang.Long.toString(definedKeySets.valueAt(i).longValue()));
                        }
                    }
                    if (printedLabel2) {
                        pw.println("");
                    }
                    boolean printedLabel3 = false;
                    long signingKeySet = pkg.getKeySetData().getProperSigningKeySet();
                    pw.print("      Signing KeySets: ");
                    pw.print(java.lang.Long.toString(signingKeySet));
                    pw.println("");
                    if (pkg.getKeySetData().isUsingUpgradeKeySets()) {
                        for (long keySetId : pkg.getKeySetData().getUpgradeKeySets()) {
                            if (!printedLabel3) {
                                pw.print("      Upgrade KeySets: ");
                                printedLabel3 = true;
                            } else {
                                pw.print(", ");
                            }
                            pw.print(java.lang.Long.toString(keySetId));
                        }
                    }
                    if (printedLabel3) {
                        pw.println("");
                    }
                }
                str = packageName;
            }
        }
    }

    void writeKeySetManagerServiceLPr(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        serializer.startTag((java.lang.String) null, "keyset-settings");
        serializer.attributeInt((java.lang.String) null, "version", 1);
        writePublicKeysLPr(serializer);
        writeKeySetsLPr(serializer);
        serializer.startTag((java.lang.String) null, "lastIssuedKeyId");
        serializer.attributeLong((java.lang.String) null, "value", this.lastIssuedKeyId);
        serializer.endTag((java.lang.String) null, "lastIssuedKeyId");
        serializer.startTag((java.lang.String) null, "lastIssuedKeySetId");
        serializer.attributeLong((java.lang.String) null, "value", this.lastIssuedKeySetId);
        serializer.endTag((java.lang.String) null, "lastIssuedKeySetId");
        serializer.endTag((java.lang.String) null, "keyset-settings");
    }

    void writePublicKeysLPr(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        serializer.startTag((java.lang.String) null, "keys");
        for (int pKeyIndex = 0; pKeyIndex < this.mPublicKeys.size(); pKeyIndex++) {
            long id = this.mPublicKeys.keyAt(pKeyIndex);
            com.android.server.pm.KeySetManagerService.PublicKeyHandle pkh = this.mPublicKeys.valueAt(pKeyIndex);
            serializer.startTag((java.lang.String) null, "public-key");
            serializer.attributeLong((java.lang.String) null, "identifier", id);
            serializer.attributeBytesBase64((java.lang.String) null, "value", pkh.getKey().getEncoded());
            serializer.endTag((java.lang.String) null, "public-key");
        }
        serializer.endTag((java.lang.String) null, "keys");
    }

    void writeKeySetsLPr(com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        serializer.startTag((java.lang.String) null, "keysets");
        for (int keySetIndex = 0; keySetIndex < this.mKeySetMapping.size(); keySetIndex++) {
            long id = this.mKeySetMapping.keyAt(keySetIndex);
            android.util.ArraySet<java.lang.Long> keys = this.mKeySetMapping.valueAt(keySetIndex);
            serializer.startTag((java.lang.String) null, "keyset");
            serializer.attributeLong((java.lang.String) null, "identifier", id);
            java.util.Iterator<java.lang.Long> it = keys.iterator();
            while (it.hasNext()) {
                long keyId = it.next().longValue();
                serializer.startTag((java.lang.String) null, "key-id");
                serializer.attributeLong((java.lang.String) null, "identifier", keyId);
                serializer.endTag((java.lang.String) null, "key-id");
            }
            serializer.endTag((java.lang.String) null, "keyset");
        }
        serializer.endTag((java.lang.String) null, "keysets");
    }

    void readKeySetsLPw(com.android.modules.utils.TypedXmlPullParser parser, android.util.ArrayMap<java.lang.Long, java.lang.Integer> keySetRefCounts) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        java.lang.String recordedVersionStr = parser.getAttributeValue((java.lang.String) null, "version");
        if (recordedVersionStr == null) {
            while (true) {
                int type = parser.next();
                if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                    break;
                }
            }
            for (com.android.server.pm.PackageSetting p : this.mPackages.values()) {
                clearPackageKeySetDataLPw(p);
            }
            return;
        }
        while (true) {
            int type2 = parser.next();
            if (type2 == 1 || (type2 == 3 && parser.getDepth() <= outerDepth)) {
                break;
            }
            if (type2 != 3 && type2 != 4) {
                java.lang.String tagName = parser.getName();
                if (tagName.equals("keys")) {
                    readKeysLPw(parser);
                } else if (tagName.equals("keysets")) {
                    readKeySetListLPw(parser);
                } else if (tagName.equals("lastIssuedKeyId")) {
                    this.lastIssuedKeyId = parser.getAttributeLong((java.lang.String) null, "value");
                } else if (tagName.equals("lastIssuedKeySetId")) {
                    this.lastIssuedKeySetId = parser.getAttributeLong((java.lang.String) null, "value");
                }
            }
        }
        addRefCountsFromSavedPackagesLPw(keySetRefCounts);
    }

    void readKeysLPw(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals("public-key")) {
                            readPublicKeyLPw(parser);
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    void readKeySetListLPw(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        long currentKeySetId = 0;
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type != 3 || parser.getDepth() > outerDepth) {
                    if (type != 3 && type != 4) {
                        java.lang.String tagName = parser.getName();
                        if (tagName.equals("keyset")) {
                            currentKeySetId = parser.getAttributeLong((java.lang.String) null, "identifier");
                            this.mKeySets.put(currentKeySetId, new com.android.server.pm.KeySetHandle(currentKeySetId, 0));
                            this.mKeySetMapping.put(currentKeySetId, new android.util.ArraySet<>());
                        } else if (tagName.equals("key-id")) {
                            long id = parser.getAttributeLong((java.lang.String) null, "identifier");
                            this.mKeySetMapping.get(currentKeySetId).add(java.lang.Long.valueOf(id));
                        }
                    }
                } else {
                    return;
                }
            } else {
                return;
            }
        }
    }

    void readPublicKeyLPw(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException {
        long identifier = parser.getAttributeLong((java.lang.String) null, "identifier");
        int refCount = 0;
        byte[] publicKey = parser.getAttributeBytesBase64((java.lang.String) null, "value", (byte[]) null);
        java.security.PublicKey pub = android.content.pm.parsing.FrameworkParsingPackageUtils.parsePublicKey(publicKey);
        if (pub != null) {
            com.android.server.pm.KeySetManagerService.PublicKeyHandle pkh = new com.android.server.pm.KeySetManagerService.PublicKeyHandle(identifier, refCount, pub);
            this.mPublicKeys.put(identifier, pkh);
        }
    }

    private void addRefCountsFromSavedPackagesLPw(android.util.ArrayMap<java.lang.Long, java.lang.Integer> keySetRefCounts) {
        int numRefCounts = keySetRefCounts.size();
        for (int i = 0; i < numRefCounts; i++) {
            com.android.server.pm.KeySetHandle ks = this.mKeySets.get(keySetRefCounts.keyAt(i).longValue());
            if (ks == null) {
                android.util.Slog.wtf(TAG, "Encountered non-existent key-set reference when reading settings");
            } else {
                ks.setRefCountLPw(keySetRefCounts.valueAt(i).intValue());
            }
        }
        android.util.ArraySet<java.lang.Long> orphanedKeySets = new android.util.ArraySet<>();
        int numKeySets = this.mKeySets.size();
        for (int i2 = 0; i2 < numKeySets; i2++) {
            if (this.mKeySets.valueAt(i2).getRefCountLPr() == 0) {
                android.util.Slog.wtf(TAG, "Encountered key-set w/out package references when reading settings");
                orphanedKeySets.add(java.lang.Long.valueOf(this.mKeySets.keyAt(i2)));
            }
            android.util.ArraySet<java.lang.Long> pubKeys = this.mKeySetMapping.valueAt(i2);
            int pkSize = pubKeys.size();
            for (int j = 0; j < pkSize; j++) {
                this.mPublicKeys.get(pubKeys.valueAt(j).longValue()).incrRefCountLPw();
            }
        }
        int numOrphans = orphanedKeySets.size();
        for (int i3 = 0; i3 < numOrphans; i3++) {
            decrementKeySetLPw(orphanedKeySets.valueAt(i3).longValue());
        }
    }
}
