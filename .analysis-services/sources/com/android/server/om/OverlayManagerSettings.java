package com.android.server.om;

/* JADX INFO: loaded from: classes2.dex */
final class OverlayManagerSettings {
    private final java.util.ArrayList<com.android.server.om.OverlayManagerSettings.SettingsItem> mItems = new java.util.ArrayList<>();

    OverlayManagerSettings() {
    }

    android.content.om.OverlayInfo init(android.content.om.OverlayIdentifier overlay, int userId, java.lang.String targetPackageName, java.lang.String targetOverlayableName, java.lang.String baseCodePath, boolean isMutable, boolean isEnabled, int priority, java.lang.String overlayCategory, boolean isFabricated) {
        remove(overlay, userId);
        com.android.server.om.OverlayManagerSettings.SettingsItem item = new com.android.server.om.OverlayManagerSettings.SettingsItem(overlay, userId, targetPackageName, targetOverlayableName, baseCodePath, -1, isEnabled, isMutable, priority, overlayCategory, isFabricated);
        insert(item);
        return item.getOverlayInfo();
    }

    boolean remove(android.content.om.OverlayIdentifier overlay, int userId) {
        int idx = select(overlay, userId);
        if (idx < 0) {
            return false;
        }
        this.mItems.remove(idx);
        return true;
    }

    android.content.om.OverlayInfo getOverlayInfo(android.content.om.OverlayIdentifier overlay, int userId) throws com.android.server.om.OverlayManagerSettings.BadKeyException {
        int idx = select(overlay, userId);
        if (idx < 0) {
            throw new com.android.server.om.OverlayManagerSettings.BadKeyException(overlay, userId);
        }
        return this.mItems.get(idx).getOverlayInfo();
    }

    android.content.om.OverlayInfo getNullableOverlayInfo(android.content.om.OverlayIdentifier overlay, int userId) {
        int idx = select(overlay, userId);
        if (idx < 0) {
            return null;
        }
        return this.mItems.get(idx).getOverlayInfo();
    }

    boolean setBaseCodePath(android.content.om.OverlayIdentifier overlay, int userId, java.lang.String path) throws com.android.server.om.OverlayManagerSettings.BadKeyException {
        int idx = select(overlay, userId);
        if (idx < 0) {
            throw new com.android.server.om.OverlayManagerSettings.BadKeyException(overlay, userId);
        }
        return this.mItems.get(idx).setBaseCodePath(path);
    }

    boolean setCategory(android.content.om.OverlayIdentifier overlay, int userId, java.lang.String category) throws com.android.server.om.OverlayManagerSettings.BadKeyException {
        int idx = select(overlay, userId);
        if (idx < 0) {
            throw new com.android.server.om.OverlayManagerSettings.BadKeyException(overlay, userId);
        }
        return this.mItems.get(idx).setCategory(category);
    }

    boolean getEnabled(android.content.om.OverlayIdentifier overlay, int userId) throws com.android.server.om.OverlayManagerSettings.BadKeyException {
        int idx = select(overlay, userId);
        if (idx < 0) {
            throw new com.android.server.om.OverlayManagerSettings.BadKeyException(overlay, userId);
        }
        return this.mItems.get(idx).isEnabled();
    }

    boolean setEnabled(android.content.om.OverlayIdentifier overlay, int userId, boolean enable) throws com.android.server.om.OverlayManagerSettings.BadKeyException {
        int idx = select(overlay, userId);
        if (idx < 0) {
            throw new com.android.server.om.OverlayManagerSettings.BadKeyException(overlay, userId);
        }
        return this.mItems.get(idx).setEnabled(enable);
    }

    int getState(android.content.om.OverlayIdentifier overlay, int userId) throws com.android.server.om.OverlayManagerSettings.BadKeyException {
        int idx = select(overlay, userId);
        if (idx < 0) {
            throw new com.android.server.om.OverlayManagerSettings.BadKeyException(overlay, userId);
        }
        return this.mItems.get(idx).getState();
    }

    boolean setState(android.content.om.OverlayIdentifier overlay, int userId, int state) throws com.android.server.om.OverlayManagerSettings.BadKeyException {
        int idx = select(overlay, userId);
        if (idx < 0) {
            throw new com.android.server.om.OverlayManagerSettings.BadKeyException(overlay, userId);
        }
        return this.mItems.get(idx).setState(state);
    }

    java.util.List<android.content.om.OverlayInfo> getOverlaysForTarget(java.lang.String targetPackageName, int userId) {
        java.util.List<com.android.server.om.OverlayManagerSettings.SettingsItem> items = selectWhereTarget(targetPackageName, userId);
        return com.android.internal.util.CollectionUtils.map(items, new java.util.function.Function() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda10
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((com.android.server.om.OverlayManagerSettings.SettingsItem) obj).getOverlayInfo();
            }
        });
    }

    void forEachMatching(int userId, java.lang.String overlayName, java.lang.String targetPackageName, java.util.function.Consumer<android.content.om.OverlayInfo> consumer) {
        int n = this.mItems.size();
        for (int i = 0; i < n; i++) {
            com.android.server.om.OverlayManagerSettings.SettingsItem item = this.mItems.get(i);
            if (item.getUserId() == userId && ((overlayName == null || item.mOverlay.getPackageName().equals(overlayName)) && (targetPackageName == null || item.mTargetPackageName.equals(targetPackageName)))) {
                consumer.accept(item.getOverlayInfo());
            }
        }
    }

    android.util.ArrayMap<java.lang.String, java.util.List<android.content.om.OverlayInfo>> getOverlaysForUser(int userId) {
        java.util.List<com.android.server.om.OverlayManagerSettings.SettingsItem> items = selectWhereUser(userId);
        android.util.ArrayMap<java.lang.String, java.util.List<android.content.om.OverlayInfo>> targetInfos = new android.util.ArrayMap<>();
        int n = items.size();
        for (int i = 0; i < n; i++) {
            com.android.server.om.OverlayManagerSettings.SettingsItem item = items.get(i);
            targetInfos.computeIfAbsent(item.mTargetPackageName, new java.util.function.Function() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda13
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.om.OverlayManagerSettings.lambda$getOverlaysForUser$0((java.lang.String) obj);
                }
            }).add(item.getOverlayInfo());
        }
        return targetInfos;
    }

    static /* synthetic */ java.util.List lambda$getOverlaysForUser$0(java.lang.String String) {
        return new java.util.ArrayList();
    }

    java.util.Set<java.lang.String> getAllBaseCodePaths() {
        final java.util.Set<java.lang.String> paths = new android.util.ArraySet<>();
        this.mItems.forEach(new java.util.function.Consumer() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                paths.add(((com.android.server.om.OverlayManagerSettings.SettingsItem) obj).mBaseCodePath);
            }
        });
        return paths;
    }

    java.util.Set<android.util.Pair<android.content.om.OverlayIdentifier, java.lang.String>> getAllIdentifiersAndBaseCodePaths() {
        final java.util.Set<android.util.Pair<android.content.om.OverlayIdentifier, java.lang.String>> set = new android.util.ArraySet<>();
        this.mItems.forEach(new java.util.function.Consumer() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.om.OverlayManagerSettings.SettingsItem settingsItem = (com.android.server.om.OverlayManagerSettings.SettingsItem) obj;
                set.add(new android.util.Pair(settingsItem.mOverlay, settingsItem.mBaseCodePath));
            }
        });
        return set;
    }

    static /* synthetic */ boolean lambda$removeIf$3(java.util.function.Predicate predicate, int userId, android.content.om.OverlayInfo info) {
        return predicate.test(info) && info.userId == userId;
    }

    java.util.List<android.content.om.OverlayInfo> removeIf(final java.util.function.Predicate<android.content.om.OverlayInfo> predicate, final int userId) {
        return removeIf(new java.util.function.Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.om.OverlayManagerSettings.lambda$removeIf$3(predicate, userId, (android.content.om.OverlayInfo) obj);
            }
        });
    }

    java.util.List<android.content.om.OverlayInfo> removeIf(java.util.function.Predicate<android.content.om.OverlayInfo> predicate) {
        java.util.List<android.content.om.OverlayInfo> removed = null;
        for (int i = this.mItems.size() - 1; i >= 0; i--) {
            android.content.om.OverlayInfo info = this.mItems.get(i).getOverlayInfo();
            if (predicate.test(info)) {
                this.mItems.remove(i);
                removed = com.android.internal.util.CollectionUtils.add(removed, info);
            }
        }
        return com.android.internal.util.CollectionUtils.emptyIfNull(removed);
    }

    int[] getUsers() {
        return this.mItems.stream().mapToInt(new java.util.function.ToIntFunction() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda11
            @Override // java.util.function.ToIntFunction
            public final int applyAsInt(java.lang.Object obj) {
                return ((com.android.server.om.OverlayManagerSettings.SettingsItem) obj).getUserId();
            }
        }).distinct().toArray();
    }

    boolean removeUser(final int userId) {
        return this.mItems.removeIf(new java.util.function.Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.om.OverlayManagerSettings.lambda$removeUser$4(userId, (com.android.server.om.OverlayManagerSettings.SettingsItem) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$removeUser$4(int userId, com.android.server.om.OverlayManagerSettings.SettingsItem item) {
        if (item.getUserId() == userId) {
            if (com.android.server.om.OverlayManagerService.DEBUG) {
                android.util.Slog.d("OverlayManager", "Removing overlay " + item.mOverlay + " for user " + userId + " from settings because user was removed");
                return true;
            }
            return true;
        }
        return false;
    }

    void setPriority(android.content.om.OverlayIdentifier overlay, int userId, int priority) throws com.android.server.om.OverlayManagerSettings.BadKeyException {
        int moveIdx = select(overlay, userId);
        if (moveIdx < 0) {
            throw new com.android.server.om.OverlayManagerSettings.BadKeyException(overlay, userId);
        }
        com.android.server.om.OverlayManagerSettings.SettingsItem itemToMove = this.mItems.get(moveIdx);
        this.mItems.remove(moveIdx);
        itemToMove.setPriority(priority);
        insert(itemToMove);
    }

    boolean setPriority(android.content.om.OverlayIdentifier overlay, android.content.om.OverlayIdentifier newOverlay, int userId) {
        int moveIdx;
        int parentIdx;
        if (overlay.equals(newOverlay) || (moveIdx = select(overlay, userId)) < 0 || (parentIdx = select(newOverlay, userId)) < 0) {
            return false;
        }
        com.android.server.om.OverlayManagerSettings.SettingsItem itemToMove = this.mItems.get(moveIdx);
        if (!itemToMove.getTargetPackageName().equals(this.mItems.get(parentIdx).getTargetPackageName())) {
            return false;
        }
        this.mItems.remove(moveIdx);
        int newParentIdx = select(newOverlay, userId) + 1;
        this.mItems.add(newParentIdx, itemToMove);
        return moveIdx != newParentIdx;
    }

    boolean setLowestPriority(android.content.om.OverlayIdentifier overlay, int userId) {
        int idx = select(overlay, userId);
        if (idx <= 0) {
            return false;
        }
        com.android.server.om.OverlayManagerSettings.SettingsItem item = this.mItems.get(idx);
        this.mItems.remove(item);
        this.mItems.add(0, item);
        return true;
    }

    boolean setHighestPriority(android.content.om.OverlayIdentifier overlay, int userId) {
        int idx = select(overlay, userId);
        if (idx < 0 || idx == this.mItems.size() - 1) {
            return false;
        }
        com.android.server.om.OverlayManagerSettings.SettingsItem item = this.mItems.get(idx);
        this.mItems.remove(idx);
        this.mItems.add(item);
        return true;
    }

    private void insert(com.android.server.om.OverlayManagerSettings.SettingsItem item) {
        int i = this.mItems.size() - 1;
        while (i >= 0) {
            com.android.server.om.OverlayManagerSettings.SettingsItem parentItem = this.mItems.get(i);
            if (parentItem.mPriority <= item.getPriority()) {
                break;
            } else {
                i--;
            }
        }
        this.mItems.add(i + 1, item);
    }

    void dump(java.io.PrintWriter p, final com.android.server.om.DumpState dumpState) {
        java.util.stream.Stream<com.android.server.om.OverlayManagerSettings.SettingsItem> items = this.mItems.stream();
        if (dumpState.getUserId() != -1) {
            items = items.filter(new java.util.function.Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.om.OverlayManagerSettings.lambda$dump$5(dumpState, (com.android.server.om.OverlayManagerSettings.SettingsItem) obj);
                }
            });
        }
        if (dumpState.getPackageName() != null) {
            items = items.filter(new java.util.function.Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((com.android.server.om.OverlayManagerSettings.SettingsItem) obj).mOverlay.getPackageName().equals(dumpState.getPackageName());
                }
            });
        }
        if (dumpState.getOverlayName() != null) {
            items = items.filter(new java.util.function.Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda5
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return ((com.android.server.om.OverlayManagerSettings.SettingsItem) obj).mOverlay.getOverlayName().equals(dumpState.getOverlayName());
                }
            });
        }
        final com.android.internal.util.IndentingPrintWriter pw = new com.android.internal.util.IndentingPrintWriter(p, "  ");
        if (dumpState.getField() != null) {
            items.forEach(new java.util.function.Consumer() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda6
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$dump$8(pw, dumpState, (com.android.server.om.OverlayManagerSettings.SettingsItem) obj);
                }
            });
        } else {
            items.forEach(new java.util.function.Consumer() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda7
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$dump$9(pw, (com.android.server.om.OverlayManagerSettings.SettingsItem) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$dump$5(com.android.server.om.DumpState dumpState, com.android.server.om.OverlayManagerSettings.SettingsItem item) {
        return item.mUserId == dumpState.getUserId();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dump$8(com.android.internal.util.IndentingPrintWriter pw, com.android.server.om.DumpState dumpState, com.android.server.om.OverlayManagerSettings.SettingsItem item) {
        dumpSettingsItemField(pw, item, dumpState.getField());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: dumpSettingsItem, reason: merged with bridge method [inline-methods] */
    public void lambda$dump$9(com.android.internal.util.IndentingPrintWriter pw, com.android.server.om.OverlayManagerSettings.SettingsItem item) {
        pw.println(item.mOverlay + ":" + item.getUserId() + " {");
        pw.increaseIndent();
        pw.println("mPackageName...........: " + item.mOverlay.getPackageName());
        pw.println("mOverlayName...........: " + item.mOverlay.getOverlayName());
        pw.println("mUserId................: " + item.getUserId());
        pw.println("mTargetPackageName.....: " + item.getTargetPackageName());
        pw.println("mTargetOverlayableName.: " + item.getTargetOverlayableName());
        pw.println("mBaseCodePath..........: " + item.getBaseCodePath());
        pw.println("mState.................: " + android.content.om.OverlayInfo.stateToString(item.getState()));
        pw.println("mIsEnabled.............: " + item.isEnabled());
        pw.println("mIsMutable.............: " + item.isMutable());
        pw.println("mPriority..............: " + item.mPriority);
        pw.println("mCategory..............: " + item.mCategory);
        pw.println("mIsFabricated..........: " + item.mIsFabricated);
        pw.decreaseIndent();
        pw.println("}");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0084  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void dumpSettingsItemField(com.android.internal.util.IndentingPrintWriter r2, com.android.server.om.OverlayManagerSettings.SettingsItem r3, java.lang.String r4) {
        /*
            Method dump skipped, instruction units count: 310
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.om.OverlayManagerSettings.dumpSettingsItemField(com.android.internal.util.IndentingPrintWriter, com.android.server.om.OverlayManagerSettings$SettingsItem, java.lang.String):void");
    }

    void restore(java.io.InputStream is) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.server.om.OverlayManagerSettings.Serializer.restore(this.mItems, is);
    }

    void persist(java.io.OutputStream os) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.server.om.OverlayManagerSettings.Serializer.persist(this.mItems, os);
    }

    static final class Serializer {
        private static final java.lang.String ATTR_BASE_CODE_PATH = "baseCodePath";
        private static final java.lang.String ATTR_CATEGORY = "category";
        private static final java.lang.String ATTR_IS_ENABLED = "isEnabled";
        private static final java.lang.String ATTR_IS_FABRICATED = "fabricated";
        private static final java.lang.String ATTR_IS_STATIC = "isStatic";
        private static final java.lang.String ATTR_OVERLAY_NAME = "overlayName";
        private static final java.lang.String ATTR_PACKAGE_NAME = "packageName";
        private static final java.lang.String ATTR_PRIORITY = "priority";
        private static final java.lang.String ATTR_STATE = "state";
        private static final java.lang.String ATTR_TARGET_OVERLAYABLE_NAME = "targetOverlayableName";
        private static final java.lang.String ATTR_TARGET_PACKAGE_NAME = "targetPackageName";
        private static final java.lang.String ATTR_USER_ID = "userId";
        private static final java.lang.String ATTR_VERSION = "version";
        static final int CURRENT_VERSION = 4;
        private static final java.lang.String TAG_ITEM = "item";
        private static final java.lang.String TAG_OVERLAYS = "overlays";

        Serializer() {
        }

        public static void restore(java.util.ArrayList<com.android.server.om.OverlayManagerSettings.SettingsItem> table, java.io.InputStream is) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            table.clear();
            com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(is);
            com.android.internal.util.XmlUtils.beginDocument(parser, TAG_OVERLAYS);
            int version = parser.getAttributeInt((java.lang.String) null, ATTR_VERSION);
            if (version != 4) {
                upgrade(version);
            }
            int depth = parser.getDepth();
            while (com.android.internal.util.XmlUtils.nextElementWithin(parser, depth)) {
                if ("item".equals(parser.getName())) {
                    com.android.server.om.OverlayManagerSettings.SettingsItem item = restoreRow(parser, depth + 1);
                    table.add(item);
                }
            }
        }

        private static void upgrade(int oldVersion) throws org.xmlpull.v1.XmlPullParserException {
            switch (oldVersion) {
                case 0:
                case 1:
                case 2:
                    throw new org.xmlpull.v1.XmlPullParserException("old version " + oldVersion + "; ignoring");
                case 3:
                    return;
                default:
                    throw new org.xmlpull.v1.XmlPullParserException("unrecognized version " + oldVersion);
            }
        }

        private static com.android.server.om.OverlayManagerSettings.SettingsItem restoreRow(com.android.modules.utils.TypedXmlPullParser parser, int depth) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            android.content.om.OverlayIdentifier overlay = new android.content.om.OverlayIdentifier(com.android.internal.util.XmlUtils.readStringAttribute(parser, "packageName"), com.android.internal.util.XmlUtils.readStringAttribute(parser, ATTR_OVERLAY_NAME));
            int userId = parser.getAttributeInt((java.lang.String) null, "userId");
            java.lang.String targetPackageName = com.android.internal.util.XmlUtils.readStringAttribute(parser, ATTR_TARGET_PACKAGE_NAME);
            java.lang.String targetOverlayableName = com.android.internal.util.XmlUtils.readStringAttribute(parser, ATTR_TARGET_OVERLAYABLE_NAME);
            java.lang.String baseCodePath = com.android.internal.util.XmlUtils.readStringAttribute(parser, ATTR_BASE_CODE_PATH);
            int state = parser.getAttributeInt((java.lang.String) null, "state");
            boolean isEnabled = parser.getAttributeBoolean((java.lang.String) null, ATTR_IS_ENABLED, false);
            boolean isStatic = parser.getAttributeBoolean((java.lang.String) null, ATTR_IS_STATIC, false);
            int priority = parser.getAttributeInt((java.lang.String) null, ATTR_PRIORITY);
            java.lang.String category = com.android.internal.util.XmlUtils.readStringAttribute(parser, ATTR_CATEGORY);
            boolean isFabricated = parser.getAttributeBoolean((java.lang.String) null, ATTR_IS_FABRICATED, false);
            return new com.android.server.om.OverlayManagerSettings.SettingsItem(overlay, userId, targetPackageName, targetOverlayableName, baseCodePath, state, isEnabled, !isStatic, priority, category, isFabricated);
        }

        public static void persist(java.util.ArrayList<com.android.server.om.OverlayManagerSettings.SettingsItem> table, java.io.OutputStream os) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            com.android.modules.utils.TypedXmlSerializer xml = android.util.Xml.resolveSerializer(os);
            xml.startDocument((java.lang.String) null, true);
            xml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            xml.startTag((java.lang.String) null, TAG_OVERLAYS);
            xml.attributeInt((java.lang.String) null, ATTR_VERSION, 4);
            int n = table.size();
            for (int i = 0; i < n; i++) {
                com.android.server.om.OverlayManagerSettings.SettingsItem item = table.get(i);
                persistRow(xml, item);
            }
            xml.endTag((java.lang.String) null, TAG_OVERLAYS);
            xml.endDocument();
        }

        private static void persistRow(com.android.modules.utils.TypedXmlSerializer xml, com.android.server.om.OverlayManagerSettings.SettingsItem item) throws java.io.IOException {
            xml.startTag((java.lang.String) null, "item");
            com.android.internal.util.XmlUtils.writeStringAttribute(xml, "packageName", item.mOverlay.getPackageName());
            com.android.internal.util.XmlUtils.writeStringAttribute(xml, ATTR_OVERLAY_NAME, item.mOverlay.getOverlayName());
            xml.attributeInt((java.lang.String) null, "userId", item.mUserId);
            com.android.internal.util.XmlUtils.writeStringAttribute(xml, ATTR_TARGET_PACKAGE_NAME, item.mTargetPackageName);
            com.android.internal.util.XmlUtils.writeStringAttribute(xml, ATTR_TARGET_OVERLAYABLE_NAME, item.mTargetOverlayableName);
            com.android.internal.util.XmlUtils.writeStringAttribute(xml, ATTR_BASE_CODE_PATH, item.mBaseCodePath);
            xml.attributeInt((java.lang.String) null, "state", item.mState);
            com.android.internal.util.XmlUtils.writeBooleanAttribute(xml, ATTR_IS_ENABLED, item.mIsEnabled);
            com.android.internal.util.XmlUtils.writeBooleanAttribute(xml, ATTR_IS_STATIC, !item.mIsMutable);
            xml.attributeInt((java.lang.String) null, ATTR_PRIORITY, item.mPriority);
            com.android.internal.util.XmlUtils.writeStringAttribute(xml, ATTR_CATEGORY, item.mCategory);
            com.android.internal.util.XmlUtils.writeBooleanAttribute(xml, ATTR_IS_FABRICATED, item.mIsFabricated);
            xml.endTag((java.lang.String) null, "item");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    static final class SettingsItem {
        private java.lang.String mBaseCodePath;
        private android.content.om.OverlayInfo mCache = null;
        private java.lang.String mCategory;
        private boolean mIsEnabled;
        private boolean mIsFabricated;
        private boolean mIsMutable;
        private final android.content.om.OverlayIdentifier mOverlay;
        private int mPriority;
        private int mState;
        private final java.lang.String mTargetOverlayableName;
        private final java.lang.String mTargetPackageName;
        private final int mUserId;

        SettingsItem(android.content.om.OverlayIdentifier overlay, int userId, java.lang.String targetPackageName, java.lang.String targetOverlayableName, java.lang.String baseCodePath, int state, boolean isEnabled, boolean isMutable, int priority, java.lang.String category, boolean isFabricated) {
            this.mOverlay = overlay;
            this.mUserId = userId;
            this.mTargetPackageName = targetPackageName;
            this.mTargetOverlayableName = targetOverlayableName;
            this.mBaseCodePath = baseCodePath;
            this.mState = state;
            this.mIsEnabled = isEnabled;
            this.mCategory = category;
            this.mIsMutable = isMutable;
            this.mPriority = priority;
            this.mIsFabricated = isFabricated;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.lang.String getTargetPackageName() {
            return this.mTargetPackageName;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.lang.String getTargetOverlayableName() {
            return this.mTargetOverlayableName;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getUserId() {
            return this.mUserId;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public java.lang.String getBaseCodePath() {
            return this.mBaseCodePath;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean setBaseCodePath(java.lang.String path) {
            if (!this.mBaseCodePath.equals(path)) {
                this.mBaseCodePath = path;
                invalidateCache();
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getState() {
            return this.mState;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean setState(int state) {
            if (this.mState != state) {
                this.mState = state;
                invalidateCache();
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isEnabled() {
            return this.mIsEnabled;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean setEnabled(boolean enable) {
            if (!this.mIsMutable || this.mIsEnabled == enable) {
                return false;
            }
            this.mIsEnabled = enable;
            invalidateCache();
            return true;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean setCategory(java.lang.String category) {
            if (!java.util.Objects.equals(this.mCategory, category)) {
                this.mCategory = category == null ? null : category.intern();
                invalidateCache();
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public android.content.om.OverlayInfo getOverlayInfo() {
            if (this.mCache == null) {
                this.mCache = new android.content.om.OverlayInfo(this.mOverlay.getPackageName(), this.mOverlay.getOverlayName(), this.mTargetPackageName, this.mTargetOverlayableName, this.mCategory, this.mBaseCodePath, this.mState, this.mUserId, this.mPriority, this.mIsMutable, this.mIsFabricated);
            }
            return this.mCache;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setPriority(int priority) {
            this.mPriority = priority;
            invalidateCache();
        }

        private void invalidateCache() {
            this.mCache = null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean isMutable() {
            return this.mIsMutable;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int getPriority() {
            return this.mPriority;
        }
    }

    private int select(android.content.om.OverlayIdentifier overlay, int userId) {
        int n = this.mItems.size();
        for (int i = 0; i < n; i++) {
            com.android.server.om.OverlayManagerSettings.SettingsItem item = this.mItems.get(i);
            if (item.mUserId == userId && item.mOverlay.equals(overlay)) {
                return i;
            }
        }
        return -1;
    }

    private java.util.List<com.android.server.om.OverlayManagerSettings.SettingsItem> selectWhereUser(final int userId) {
        java.util.List<com.android.server.om.OverlayManagerSettings.SettingsItem> selectedItems = new java.util.ArrayList<>();
        com.android.internal.util.CollectionUtils.addIf(this.mItems, selectedItems, new java.util.function.Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda14
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.om.OverlayManagerSettings.lambda$selectWhereUser$10(userId, (com.android.server.om.OverlayManagerSettings.SettingsItem) obj);
            }
        });
        return selectedItems;
    }

    static /* synthetic */ boolean lambda$selectWhereUser$10(int userId, com.android.server.om.OverlayManagerSettings.SettingsItem i) {
        return i.mUserId == userId;
    }

    private java.util.List<com.android.server.om.OverlayManagerSettings.SettingsItem> selectWhereOverlay(final java.lang.String packageName, int userId) {
        java.util.List<com.android.server.om.OverlayManagerSettings.SettingsItem> items = selectWhereUser(userId);
        items.removeIf(new java.util.function.Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.om.OverlayManagerSettings.lambda$selectWhereOverlay$11(packageName, (com.android.server.om.OverlayManagerSettings.SettingsItem) obj);
            }
        });
        return items;
    }

    static /* synthetic */ boolean lambda$selectWhereOverlay$11(java.lang.String packageName, com.android.server.om.OverlayManagerSettings.SettingsItem i) {
        return !i.mOverlay.getPackageName().equals(packageName);
    }

    private java.util.List<com.android.server.om.OverlayManagerSettings.SettingsItem> selectWhereTarget(final java.lang.String targetPackageName, int userId) {
        java.util.List<com.android.server.om.OverlayManagerSettings.SettingsItem> items = selectWhereUser(userId);
        items.removeIf(new java.util.function.Predicate() { // from class: com.android.server.om.OverlayManagerSettings$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.om.OverlayManagerSettings.lambda$selectWhereTarget$12(targetPackageName, (com.android.server.om.OverlayManagerSettings.SettingsItem) obj);
            }
        });
        return items;
    }

    static /* synthetic */ boolean lambda$selectWhereTarget$12(java.lang.String targetPackageName, com.android.server.om.OverlayManagerSettings.SettingsItem i) {
        return !i.getTargetPackageName().equals(targetPackageName);
    }

    static final class BadKeyException extends java.lang.Exception {
        BadKeyException(android.content.om.OverlayIdentifier overlay, int userId) {
            super("Bad key '" + overlay + "' for user " + userId);
        }
    }
}
