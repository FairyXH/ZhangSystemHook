package com.android.server.pm.verify.domain;

/* JADX INFO: loaded from: classes2.dex */
public class DomainVerificationPersistence {
    public static final java.lang.String ATTR_ACTION = "action";
    public static final java.lang.String ATTR_ALLOW_LINK_HANDLING = "allowLinkHandling";
    public static final java.lang.String ATTR_FILTER = "filter";
    private static final java.lang.String ATTR_HAS_AUTO_VERIFY_DOMAINS = "hasAutoVerifyDomains";
    private static final java.lang.String ATTR_ID = "id";
    public static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_PACKAGE_NAME = "packageName";
    public static final java.lang.String ATTR_PATTERN_TYPE = "pattern-type";
    private static final java.lang.String ATTR_SIGNATURE = "signature";
    public static final java.lang.String ATTR_STATE = "state";
    public static final java.lang.String ATTR_URI_PART = "uri-part";
    public static final java.lang.String ATTR_USER_ID = "userId";
    private static final java.lang.String TAG = "DomainVerificationPersistence";
    public static final java.lang.String TAG_ACTIVE = "active";
    public static final java.lang.String TAG_DOMAIN = "domain";
    public static final java.lang.String TAG_DOMAIN_VERIFICATIONS = "domain-verifications";
    public static final java.lang.String TAG_ENABLED_HOSTS = "enabled-hosts";
    public static final java.lang.String TAG_HOST = "host";
    public static final java.lang.String TAG_PACKAGE_STATE = "package-state";
    public static final java.lang.String TAG_RESTORED = "restored";
    private static final java.lang.String TAG_STATE = "state";
    public static final java.lang.String TAG_URI_RELATIVE_FILTER = "uri-relative-filter";
    public static final java.lang.String TAG_URI_RELATIVE_FILTER_GROUP = "uri-relative-filter-group";
    public static final java.lang.String TAG_URI_RELATIVE_FILTER_GROUPS = "uri-relative-filter-groups";
    public static final java.lang.String TAG_USER_STATE = "user-state";
    private static final java.lang.String TAG_USER_STATES = "user-states";

    public static void writeToXml(com.android.modules.utils.TypedXmlSerializer xmlSerializer, com.android.server.pm.verify.domain.models.DomainVerificationStateMap<com.android.server.pm.verify.domain.models.DomainVerificationPkgState> attached, android.util.ArrayMap<java.lang.String, com.android.server.pm.verify.domain.models.DomainVerificationPkgState> pending, android.util.ArrayMap<java.lang.String, com.android.server.pm.verify.domain.models.DomainVerificationPkgState> restored, int userId, java.util.function.Function<java.lang.String, java.lang.String> pkgNameToSignature) throws java.io.IOException {
        com.android.server.pm.SettingsXml.Serializer serializer = com.android.server.pm.SettingsXml.serializer(xmlSerializer);
        try {
            com.android.server.pm.SettingsXml.WriteSection ignored = serializer.startSection(TAG_DOMAIN_VERIFICATIONS);
            try {
                android.util.ArraySet<com.android.server.pm.verify.domain.models.DomainVerificationPkgState> active = new android.util.ArraySet<>();
                int attachedSize = attached.size();
                for (int attachedIndex = 0; attachedIndex < attachedSize; attachedIndex++) {
                    active.add(attached.valueAt(attachedIndex));
                }
                int pendingSize = pending.size();
                for (int pendingIndex = 0; pendingIndex < pendingSize; pendingIndex++) {
                    active.add(pending.valueAt(pendingIndex));
                }
                com.android.server.pm.SettingsXml.WriteSection restoredSection = serializer.startSection(TAG_ACTIVE);
                try {
                    writePackageStates(restoredSection, active, userId, pkgNameToSignature);
                    if (restoredSection != null) {
                        restoredSection.close();
                    }
                    restoredSection = serializer.startSection(TAG_RESTORED);
                    try {
                        writePackageStates(restoredSection, restored.values(), userId, pkgNameToSignature);
                        if (restoredSection != null) {
                            restoredSection.close();
                        }
                        if (ignored != null) {
                            ignored.close();
                        }
                        if (serializer != null) {
                            serializer.close();
                        }
                    } finally {
                    }
                } finally {
                }
            } finally {
            }
        } catch (java.lang.Throwable th) {
            if (serializer != null) {
                try {
                    serializer.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static void writePackageStates(com.android.server.pm.SettingsXml.WriteSection section, java.util.Collection<com.android.server.pm.verify.domain.models.DomainVerificationPkgState> states, int userId, java.util.function.Function<java.lang.String, java.lang.String> pkgNameToSignature) throws java.io.IOException {
        if (states.isEmpty()) {
            return;
        }
        for (com.android.server.pm.verify.domain.models.DomainVerificationPkgState state : states) {
            writePkgStateToXml(section, state, userId, pkgNameToSignature);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0039  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.android.server.pm.verify.domain.DomainVerificationPersistence.ReadResult readFromXml(com.android.modules.utils.TypedXmlPullParser r5) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            android.util.ArrayMap r0 = new android.util.ArrayMap
            r0.<init>()
            android.util.ArrayMap r1 = new android.util.ArrayMap
            r1.<init>()
            com.android.server.pm.SettingsXml$ReadSection r2 = com.android.server.pm.SettingsXml.parser(r5)
            com.android.server.pm.SettingsXml$ChildSection r2 = r2.children()
        L12:
            boolean r3 = r2.moveToNext()
            if (r3 == 0) goto L47
            java.lang.String r3 = r2.getName()
            int r4 = r3.hashCode()
            switch(r4) {
                case -1422950650: goto L2f;
                case -336625770: goto L24;
                default: goto L23;
            }
        L23:
            goto L39
        L24:
            java.lang.String r4 = "restored"
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L23
            r3 = 1
            goto L3a
        L2f:
            java.lang.String r4 = "active"
            boolean r3 = r3.equals(r4)
            if (r3 == 0) goto L23
            r3 = 0
            goto L3a
        L39:
            r3 = -1
        L3a:
            switch(r3) {
                case 0: goto L42;
                case 1: goto L3e;
                default: goto L3d;
            }
        L3d:
            goto L46
        L3e:
            readPackageStates(r2, r1)
            goto L46
        L42:
            readPackageStates(r2, r0)
        L46:
            goto L12
        L47:
            com.android.server.pm.verify.domain.DomainVerificationPersistence$ReadResult r3 = new com.android.server.pm.verify.domain.DomainVerificationPersistence$ReadResult
            r3.<init>(r0, r1)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.verify.domain.DomainVerificationPersistence.readFromXml(com.android.modules.utils.TypedXmlPullParser):com.android.server.pm.verify.domain.DomainVerificationPersistence$ReadResult");
    }

    private static void readPackageStates(com.android.server.pm.SettingsXml.ReadSection section, android.util.ArrayMap<java.lang.String, com.android.server.pm.verify.domain.models.DomainVerificationPkgState> map) {
        com.android.server.pm.SettingsXml.ChildSection child = section.children();
        while (child.moveToNext(TAG_PACKAGE_STATE)) {
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState = createPkgStateFromXml(child);
            if (pkgState != null) {
                map.put(pkgState.getPackageName(), pkgState);
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0079  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static com.android.server.pm.verify.domain.models.DomainVerificationPkgState createPkgStateFromXml(com.android.server.pm.SettingsXml.ReadSection r19) {
        /*
            r0 = r19
            java.lang.String r1 = "packageName"
            java.lang.String r1 = r0.getString(r1)
            java.lang.String r2 = "id"
            java.lang.String r10 = r0.getString(r2)
            java.lang.String r2 = "hasAutoVerifyDomains"
            boolean r11 = r0.getBoolean(r2)
            java.lang.String r2 = "signature"
            java.lang.String r12 = r0.getString(r2)
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 != 0) goto L9d
            boolean r2 = android.text.TextUtils.isEmpty(r10)
            if (r2 == 0) goto L2c
            goto L9d
        L2c:
            java.util.UUID r13 = java.util.UUID.fromString(r10)
            android.util.ArrayMap r2 = new android.util.ArrayMap
            r2.<init>()
            r14 = r2
            android.util.SparseArray r2 = new android.util.SparseArray
            r2.<init>()
            r15 = r2
            android.util.ArrayMap r2 = new android.util.ArrayMap
            r2.<init>()
            r9 = r2
            com.android.server.pm.SettingsXml$ChildSection r8 = r19.children()
        L46:
            boolean r2 = r8.moveToNext()
            if (r2 == 0) goto L8b
            java.lang.String r2 = r8.getName()
            int r3 = r2.hashCode()
            switch(r3) {
                case -1576041916: goto L6e;
                case 109757585: goto L63;
                case 1632406025: goto L58;
                default: goto L57;
            }
        L57:
            goto L79
        L58:
            java.lang.String r3 = "uri-relative-filter-groups"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L57
            r2 = 2
            goto L7a
        L63:
            java.lang.String r3 = "state"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L57
            r2 = 0
            goto L7a
        L6e:
            java.lang.String r3 = "user-states"
            boolean r2 = r2.equals(r3)
            if (r2 == 0) goto L57
            r2 = 1
            goto L7a
        L79:
            r2 = -1
        L7a:
            switch(r2) {
                case 0: goto L86;
                case 1: goto L82;
                case 2: goto L7e;
                default: goto L7d;
            }
        L7d:
            goto L8a
        L7e:
            readUriRelativeFilterGroups(r8, r9)
            goto L8a
        L82:
            readUserStates(r8, r15)
            goto L8a
        L86:
            readDomainStates(r8, r14)
        L8a:
            goto L46
        L8b:
            com.android.server.pm.verify.domain.models.DomainVerificationPkgState r16 = new com.android.server.pm.verify.domain.models.DomainVerificationPkgState
            r2 = r16
            r3 = r1
            r4 = r13
            r5 = r11
            r6 = r14
            r7 = r15
            r17 = r8
            r8 = r12
            r18 = r9
            r2.<init>(r3, r4, r5, r6, r7, r8, r9)
            return r16
        L9d:
            r2 = 0
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.verify.domain.DomainVerificationPersistence.createPkgStateFromXml(com.android.server.pm.SettingsXml$ReadSection):com.android.server.pm.verify.domain.models.DomainVerificationPkgState");
    }

    private static void readUriRelativeFilterGroups(com.android.server.pm.SettingsXml.ReadSection section, android.util.ArrayMap<java.lang.String, java.util.List<android.content.UriRelativeFilterGroup>> groupMap) {
        com.android.server.pm.SettingsXml.ChildSection child = section.children();
        while (child.moveToNext(TAG_DOMAIN)) {
            java.lang.String domain = child.getString("name");
            groupMap.put(domain, createUriRelativeFilterGroupsFromXml(child));
        }
    }

    private static java.util.ArrayList<android.content.UriRelativeFilterGroup> createUriRelativeFilterGroupsFromXml(com.android.server.pm.SettingsXml.ReadSection section) {
        com.android.server.pm.SettingsXml.ChildSection child = section.children();
        java.util.ArrayList<android.content.UriRelativeFilterGroup> groups = new java.util.ArrayList<>();
        while (child.moveToNext(TAG_URI_RELATIVE_FILTER_GROUP)) {
            android.content.UriRelativeFilterGroup group = new android.content.UriRelativeFilterGroup(section.getInt("action"));
            readUriRelativeFiltersFromXml(child, group);
            groups.add(group);
        }
        return groups;
    }

    private static void readUriRelativeFiltersFromXml(com.android.server.pm.SettingsXml.ReadSection section, android.content.UriRelativeFilterGroup group) {
        com.android.server.pm.SettingsXml.ChildSection child = section.children();
        while (child.moveToNext(TAG_URI_RELATIVE_FILTER)) {
            java.lang.String filter = child.getString(ATTR_FILTER);
            if (filter != null) {
                group.addUriRelativeFilter(new android.content.UriRelativeFilter(child.getInt(ATTR_URI_PART), child.getInt(ATTR_PATTERN_TYPE), filter));
            }
        }
    }

    private static void readUserStates(com.android.server.pm.SettingsXml.ReadSection section, android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> userStates) {
        com.android.server.pm.SettingsXml.ChildSection child = section.children();
        while (child.moveToNext("user-state")) {
            com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState userState = createUserStateFromXml(child);
            if (userState != null) {
                userStates.put(userState.getUserId(), userState);
            }
        }
    }

    private static void readDomainStates(com.android.server.pm.SettingsXml.ReadSection stateSection, android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap) {
        com.android.server.pm.SettingsXml.ChildSection child = stateSection.children();
        while (child.moveToNext(TAG_DOMAIN)) {
            java.lang.String name = child.getString("name");
            int state = child.getInt("state", 0);
            stateMap.put(name, java.lang.Integer.valueOf(state));
        }
    }

    private static void writePkgStateToXml(com.android.server.pm.SettingsXml.WriteSection parentSection, com.android.server.pm.verify.domain.models.DomainVerificationPkgState pkgState, int userId, java.util.function.Function<java.lang.String, java.lang.String> pkgNameToSignature) throws java.io.IOException {
        java.lang.String packageName = pkgState.getPackageName();
        java.lang.String signature = pkgNameToSignature == null ? null : pkgNameToSignature.apply(packageName);
        if (signature == null) {
            signature = pkgState.getBackupSignatureHash();
        }
        com.android.server.pm.SettingsXml.WriteSection ignored = parentSection.startSection(TAG_PACKAGE_STATE).attribute("packageName", packageName).attribute(ATTR_ID, pkgState.getId().toString()).attribute(ATTR_HAS_AUTO_VERIFY_DOMAINS, pkgState.isHasAutoVerifyDomains()).attribute(ATTR_SIGNATURE, signature);
        try {
            writeStateMap(parentSection, pkgState.getStateMap());
            writeUserStates(parentSection, userId, pkgState.getUserStates());
            writeUriRelativeFilterGroupMap(parentSection, pkgState.getUriRelativeFilterGroupMap());
            if (ignored != null) {
                ignored.close();
            }
        } catch (java.lang.Throwable th) {
            if (ignored != null) {
                try {
                    ignored.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static void writeUserStates(com.android.server.pm.SettingsXml.WriteSection parentSection, int userId, android.util.SparseArray<com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState> states) throws java.io.IOException {
        int size = states.size();
        if (size == 0) {
            return;
        }
        com.android.server.pm.SettingsXml.WriteSection section = parentSection.startSection("user-states");
        try {
            if (userId == -1) {
                for (int index = 0; index < size; index++) {
                    writeUserStateToXml(section, states.valueAt(index));
                }
            } else {
                com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState userState = states.get(userId);
                if (userState != null) {
                    writeUserStateToXml(section, userState);
                }
            }
            if (section != null) {
                section.close();
            }
        } catch (java.lang.Throwable th) {
            if (section != null) {
                try {
                    section.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static void writeStateMap(com.android.server.pm.SettingsXml.WriteSection parentSection, android.util.ArrayMap<java.lang.String, java.lang.Integer> stateMap) throws java.io.IOException {
        if (stateMap.isEmpty()) {
            return;
        }
        com.android.server.pm.SettingsXml.WriteSection stateSection = parentSection.startSection("state");
        try {
            int size = stateMap.size();
            for (int index = 0; index < size; index++) {
                stateSection.startSection(TAG_DOMAIN).attribute("name", stateMap.keyAt(index)).attribute("state", stateMap.valueAt(index).intValue()).finish();
            }
            if (stateSection != null) {
                stateSection.close();
            }
        } catch (java.lang.Throwable th) {
            if (stateSection != null) {
                try {
                    stateSection.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState createUserStateFromXml(com.android.server.pm.SettingsXml.ReadSection section) {
        int userId = section.getInt("userId");
        if (userId == -1) {
            return null;
        }
        boolean allowLinkHandling = section.getBoolean(ATTR_ALLOW_LINK_HANDLING, false);
        android.util.ArraySet<java.lang.String> enabledHosts = new android.util.ArraySet<>();
        com.android.server.pm.SettingsXml.ChildSection child = section.children();
        while (child.moveToNext(TAG_ENABLED_HOSTS)) {
            readEnabledHosts(child, enabledHosts);
        }
        return new com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState(userId, enabledHosts, allowLinkHandling);
    }

    private static void readEnabledHosts(com.android.server.pm.SettingsXml.ReadSection section, android.util.ArraySet<java.lang.String> enabledHosts) {
        com.android.server.pm.SettingsXml.ChildSection child = section.children();
        while (child.moveToNext("host")) {
            java.lang.String hostName = child.getString("name");
            if (!android.text.TextUtils.isEmpty(hostName)) {
                enabledHosts.add(hostName);
            }
        }
    }

    private static void writeUserStateToXml(com.android.server.pm.SettingsXml.WriteSection parentSection, com.android.server.pm.verify.domain.models.DomainVerificationInternalUserState userState) throws java.io.IOException {
        com.android.server.pm.SettingsXml.WriteSection section = parentSection.startSection("user-state").attribute("userId", userState.getUserId()).attribute(ATTR_ALLOW_LINK_HANDLING, userState.isLinkHandlingAllowed());
        try {
            android.util.ArraySet<java.lang.String> enabledHosts = userState.getEnabledHosts();
            if (!enabledHosts.isEmpty()) {
                com.android.server.pm.SettingsXml.WriteSection enabledHostsSection = section.startSection(TAG_ENABLED_HOSTS);
                try {
                    int size = enabledHosts.size();
                    for (int index = 0; index < size; index++) {
                        enabledHostsSection.startSection("host").attribute("name", enabledHosts.valueAt(index)).finish();
                    }
                    if (enabledHostsSection != null) {
                        enabledHostsSection.close();
                    }
                } finally {
                }
            }
            if (section != null) {
                section.close();
            }
        } catch (java.lang.Throwable th) {
            if (section != null) {
                try {
                    section.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private static void writeUriRelativeFilterGroupMap(com.android.server.pm.SettingsXml.WriteSection parentSection, android.util.ArrayMap<java.lang.String, java.util.List<android.content.UriRelativeFilterGroup>> groupMap) throws java.io.IOException {
        if (groupMap.isEmpty()) {
            return;
        }
        com.android.server.pm.SettingsXml.WriteSection section = parentSection.startSection(TAG_URI_RELATIVE_FILTER_GROUPS);
        for (int i = 0; i < groupMap.size(); i++) {
            try {
                writeUriRelativeFilterGroups(section, groupMap.keyAt(i), groupMap.valueAt(i));
            } catch (java.lang.Throwable th) {
                if (section != null) {
                    try {
                        section.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (section != null) {
            section.close();
        }
    }

    private static void writeUriRelativeFilterGroups(com.android.server.pm.SettingsXml.WriteSection parentSection, java.lang.String domain, java.util.List<android.content.UriRelativeFilterGroup> groups) throws java.io.IOException {
        if (groups.isEmpty()) {
            return;
        }
        com.android.server.pm.SettingsXml.WriteSection section = parentSection.startSection(TAG_DOMAIN).attribute("name", domain);
        for (int i = 0; i < groups.size(); i++) {
            try {
                writeUriRelativeFilterGroup(section, groups.get(i));
            } catch (java.lang.Throwable th) {
                if (section != null) {
                    try {
                        section.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (section != null) {
            section.close();
        }
    }

    private static void writeUriRelativeFilterGroup(com.android.server.pm.SettingsXml.WriteSection parentSection, android.content.UriRelativeFilterGroup group) throws java.io.IOException {
        com.android.server.pm.SettingsXml.WriteSection section = parentSection.startSection(TAG_URI_RELATIVE_FILTER_GROUP).attribute("action", group.getAction());
        try {
            for (android.content.UriRelativeFilter filter : group.getUriRelativeFilters()) {
                section.startSection(TAG_URI_RELATIVE_FILTER).attribute(ATTR_URI_PART, filter.getUriPart()).attribute(ATTR_PATTERN_TYPE, filter.getPatternType()).attribute(ATTR_FILTER, filter.getFilter()).finish();
            }
            if (section != null) {
                section.close();
            }
        } catch (java.lang.Throwable th) {
            if (section != null) {
                try {
                    section.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public static class ReadResult {
        public final android.util.ArrayMap<java.lang.String, com.android.server.pm.verify.domain.models.DomainVerificationPkgState> active;
        public final android.util.ArrayMap<java.lang.String, com.android.server.pm.verify.domain.models.DomainVerificationPkgState> restored;

        public ReadResult(android.util.ArrayMap<java.lang.String, com.android.server.pm.verify.domain.models.DomainVerificationPkgState> active, android.util.ArrayMap<java.lang.String, com.android.server.pm.verify.domain.models.DomainVerificationPkgState> restored) {
            this.active = active;
            this.restored = restored;
        }
    }
}
