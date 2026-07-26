package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class ShortcutPackage extends com.android.server.pm.ShortcutPackageItem {
    private static final java.lang.String ATTR_ACTIVITY = "activity";
    private static final java.lang.String ATTR_BITMAP_PATH = "bitmap-path";
    private static final java.lang.String ATTR_CALL_COUNT = "call-count";
    private static final java.lang.String ATTR_DISABLED_MESSAGE = "dmessage";
    private static final java.lang.String ATTR_DISABLED_MESSAGE_RES_ID = "dmessageid";
    private static final java.lang.String ATTR_DISABLED_MESSAGE_RES_NAME = "dmessagename";
    private static final java.lang.String ATTR_DISABLED_REASON = "disabled-reason";
    private static final java.lang.String ATTR_FLAGS = "flags";
    private static final java.lang.String ATTR_ICON_RES_ID = "icon-res";
    private static final java.lang.String ATTR_ICON_RES_NAME = "icon-resname";
    private static final java.lang.String ATTR_ICON_URI = "icon-uri";
    private static final java.lang.String ATTR_ID = "id";
    private static final java.lang.String ATTR_INTENT_LEGACY = "intent";
    private static final java.lang.String ATTR_INTENT_NO_EXTRA = "intent-base";
    private static final java.lang.String ATTR_LAST_RESET = "last-reset";
    private static final java.lang.String ATTR_LOCUS_ID = "locus-id";
    private static final java.lang.String ATTR_NAME = "name";
    private static final java.lang.String ATTR_NAME_XMLUTILS = "name";
    private static final java.lang.String ATTR_PERSON_IS_BOT = "is-bot";
    private static final java.lang.String ATTR_PERSON_IS_IMPORTANT = "is-important";
    private static final java.lang.String ATTR_PERSON_KEY = "key";
    private static final java.lang.String ATTR_PERSON_NAME = "name";
    private static final java.lang.String ATTR_PERSON_URI = "uri";
    private static final java.lang.String ATTR_RANK = "rank";
    private static final java.lang.String ATTR_SCHEMA_VERSON = "schema-version";
    private static final java.lang.String ATTR_SPLASH_SCREEN_THEME_NAME = "splash-screen-theme-name";
    private static final java.lang.String ATTR_TEXT = "text";
    private static final java.lang.String ATTR_TEXT_RES_ID = "textid";
    private static final java.lang.String ATTR_TEXT_RES_NAME = "textname";
    private static final java.lang.String ATTR_TIMESTAMP = "timestamp";
    private static final java.lang.String ATTR_TITLE = "title";
    private static final java.lang.String ATTR_TITLE_RES_ID = "titleid";
    private static final java.lang.String ATTR_TITLE_RES_NAME = "titlename";
    private static final java.lang.String KEY_BITMAPS = "bitmaps";
    private static final java.lang.String KEY_BITMAP_BYTES = "bitmapBytes";
    private static final java.lang.String KEY_DYNAMIC = "dynamic";
    private static final java.lang.String KEY_MANIFEST = "manifest";
    private static final java.lang.String KEY_PINNED = "pinned";
    private static final java.lang.String NAME_CAPABILITY = "capability";
    private static final java.lang.String NAME_CATEGORIES = "categories";
    private static final java.lang.String TAG = "ShortcutService";
    private static final java.lang.String TAG_CATEGORIES = "categories";
    private static final java.lang.String TAG_EXTRAS = "extras";
    private static final java.lang.String TAG_INTENT = "intent";
    private static final java.lang.String TAG_INTENT_EXTRAS_LEGACY = "intent-extras";
    private static final java.lang.String TAG_MAP_XMLUTILS = "map";
    private static final java.lang.String TAG_PERSON = "person";
    static final java.lang.String TAG_ROOT = "package";
    private static final java.lang.String TAG_SHARE_TARGET = "share-target";
    private static final java.lang.String TAG_SHORTCUT = "shortcut";
    private static final java.lang.String TAG_STRING_ARRAY_XMLUTILS = "string-array";
    private static final java.lang.String TAG_VERIFY = "ShortcutService.verify";
    private int mApiCallCount;
    private final java.util.concurrent.Executor mExecutor;
    private boolean mIsAppSearchSchemaUpToDate;
    private long mLastKnownForegroundElapsedTime;
    private long mLastReportedTime;
    private long mLastResetTime;
    private final int mPackageUid;
    private final java.util.ArrayList<com.android.server.pm.ShareTargetInfo> mShareTargets;
    final java.util.Comparator<android.content.pm.ShortcutInfo> mShortcutRankComparator;
    final java.util.Comparator<android.content.pm.ShortcutInfo> mShortcutTypeAndRankComparator;
    final java.util.Comparator<android.content.pm.ShortcutInfo> mShortcutTypeRankAndTimeComparator;
    private final android.util.ArrayMap<java.lang.String, android.content.pm.ShortcutInfo> mShortcuts;
    private final android.util.ArrayMap<java.lang.String, android.content.pm.ShortcutInfo> mTransientShortcuts;

    private ShortcutPackage(com.android.server.pm.ShortcutUser shortcutUser, int packageUserId, java.lang.String packageName, com.android.server.pm.ShortcutPackageInfo spi) {
        super(shortcutUser, packageUserId, packageName, spi != null ? spi : com.android.server.pm.ShortcutPackageInfo.newEmpty());
        this.mShortcuts = new android.util.ArrayMap<>();
        this.mTransientShortcuts = new android.util.ArrayMap<>(0);
        this.mShareTargets = new java.util.ArrayList<>(0);
        this.mShortcutTypeAndRankComparator = new java.util.Comparator() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda37
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.pm.ShortcutPackage.lambda$new$19((android.content.pm.ShortcutInfo) obj, (android.content.pm.ShortcutInfo) obj2);
            }
        };
        this.mShortcutTypeRankAndTimeComparator = new java.util.Comparator() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda38
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.pm.ShortcutPackage.lambda$new$20((android.content.pm.ShortcutInfo) obj, (android.content.pm.ShortcutInfo) obj2);
            }
        };
        this.mShortcutRankComparator = new java.util.Comparator() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda39
            @Override // java.util.Comparator
            public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                return com.android.server.pm.ShortcutPackage.lambda$new$25((android.content.pm.ShortcutInfo) obj, (android.content.pm.ShortcutInfo) obj2);
            }
        };
        this.mPackageUid = shortcutUser.mService.injectGetPackageUid(packageName, packageUserId);
        this.mExecutor = com.android.internal.os.BackgroundThread.getExecutor();
    }

    public ShortcutPackage(com.android.server.pm.ShortcutUser shortcutUser, int packageUserId, java.lang.String packageName) {
        this(shortcutUser, packageUserId, packageName, null);
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    public int getOwnerUserId() {
        return getPackageUserId();
    }

    public int getPackageUid() {
        return this.mPackageUid;
    }

    public android.content.res.Resources getPackageResources() {
        return this.mShortcutUser.mService.injectGetResourcesForApplicationAsUser(getPackageName(), getPackageUserId());
    }

    private boolean isAppSearchEnabled() {
        return this.mShortcutUser.mService.isAppSearchEnabled();
    }

    public int getShortcutCount() {
        int size;
        synchronized (this.mPackageItemLock) {
            size = this.mShortcuts.size();
        }
        return size;
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    protected boolean canRestoreAnyVersion() {
        return true;
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    protected void onRestored(final int restoreBlockReason) {
        java.lang.String.format("%s:-%s AND %s:%s", ATTR_FLAGS, 4096, "disabledReason", java.lang.Integer.valueOf(restoreBlockReason));
        forEachShortcutMutate(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda42
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.ShortcutPackage.lambda$onRestored$0(restoreBlockReason, (android.content.pm.ShortcutInfo) obj);
            }
        });
        refreshPinnedFlags();
    }

    static /* synthetic */ void lambda$onRestored$0(int restoreBlockReason, android.content.pm.ShortcutInfo si) {
        if (restoreBlockReason == 0 && !si.hasFlags(4096) && si.getDisabledReason() == restoreBlockReason) {
            return;
        }
        si.clearFlags(4096);
        si.setDisabledReason(restoreBlockReason);
        if (restoreBlockReason != 0) {
            si.addFlags(64);
        }
    }

    public android.content.pm.ShortcutInfo findShortcutById(java.lang.String id) {
        android.content.pm.ShortcutInfo shortcutInfo;
        if (id == null) {
            return null;
        }
        synchronized (this.mPackageItemLock) {
            shortcutInfo = this.mShortcuts.get(id);
        }
        return shortcutInfo;
    }

    public boolean isShortcutExistsAndInvisibleToPublisher(java.lang.String id) {
        android.content.pm.ShortcutInfo si = findShortcutById(id);
        return (si == null || si.isVisibleToPublisher()) ? false : true;
    }

    public boolean isShortcutExistsAndVisibleToPublisher(java.lang.String id) {
        android.content.pm.ShortcutInfo si = findShortcutById(id);
        return si != null && si.isVisibleToPublisher();
    }

    private void ensureNotImmutable(android.content.pm.ShortcutInfo shortcut, boolean ignoreInvisible) {
        if (shortcut != null && shortcut.isImmutable()) {
            if (!ignoreInvisible || shortcut.isVisibleToPublisher()) {
                throw new java.lang.IllegalArgumentException("Manifest shortcut ID=" + shortcut.getId() + " may not be manipulated via APIs");
            }
        }
    }

    public void ensureNotImmutable(java.lang.String id, boolean ignoreInvisible) {
        ensureNotImmutable(findShortcutById(id), ignoreInvisible);
    }

    public void ensureImmutableShortcutsNotIncludedWithIds(java.util.List<java.lang.String> shortcutIds, boolean ignoreInvisible) {
        for (int i = shortcutIds.size() - 1; i >= 0; i--) {
            ensureNotImmutable(shortcutIds.get(i), ignoreInvisible);
        }
    }

    public void ensureImmutableShortcutsNotIncluded(java.util.List<android.content.pm.ShortcutInfo> shortcuts, boolean ignoreInvisible) {
        for (int i = shortcuts.size() - 1; i >= 0; i--) {
            ensureNotImmutable(shortcuts.get(i).getId(), ignoreInvisible);
        }
    }

    public void ensureNoBitmapIconIfShortcutIsLongLived(java.util.List<android.content.pm.ShortcutInfo> shortcuts) {
        android.graphics.drawable.Icon icon;
        for (int i = shortcuts.size() - 1; i >= 0; i--) {
            android.content.pm.ShortcutInfo si = shortcuts.get(i);
            if (si.isLongLived() && (((icon = si.getIcon()) == null || icon.getType() == 1 || icon.getType() == 5) && (icon != null || si.hasIconFile()))) {
                android.util.Slog.e(TAG, "Invalid icon type in shortcut " + si.getId() + ". Bitmaps are not allowed in long-lived shortcuts. Use Resource icons, or Uri-based icons instead.");
                return;
            }
        }
    }

    public void ensureAllShortcutsVisibleToLauncher(java.util.List<android.content.pm.ShortcutInfo> shortcuts) {
        for (android.content.pm.ShortcutInfo shortcut : shortcuts) {
            if (shortcut.isExcludedFromSurfaces(1)) {
                throw new java.lang.IllegalArgumentException("Shortcut ID=" + shortcut.getId() + " is hidden from launcher and may not be manipulated via APIs");
            }
        }
    }

    private android.content.pm.ShortcutInfo forceDeleteShortcutInner(java.lang.String id) {
        android.content.pm.ShortcutInfo shortcut;
        synchronized (this.mPackageItemLock) {
            shortcut = this.mShortcuts.remove(id);
            if (shortcut != null) {
                removeIcon(shortcut);
                shortcut.clearFlags(1610629155);
            }
        }
        return shortcut;
    }

    private void forceReplaceShortcutInner(android.content.pm.ShortcutInfo newShortcut) {
        com.android.server.pm.ShortcutService s = this.mShortcutUser.mService;
        forceDeleteShortcutInner(newShortcut.getId());
        s.saveIconAndFixUpShortcutLocked(this, newShortcut);
        s.fixUpShortcutResourceNamesAndValues(newShortcut);
        ensureShortcutCountBeforePush();
        saveShortcut(newShortcut);
    }

    public boolean addOrReplaceDynamicShortcut(android.content.pm.ShortcutInfo newShortcut) {
        com.android.internal.util.Preconditions.checkArgument(newShortcut.isEnabled(), "add/setDynamicShortcuts() cannot publish disabled shortcuts");
        newShortcut.addFlags(1);
        android.content.pm.ShortcutInfo oldShortcut = findShortcutById(newShortcut.getId());
        if (oldShortcut != null) {
            oldShortcut.ensureUpdatableWith(newShortcut, false);
            newShortcut.addFlags(oldShortcut.getFlags() & 1610629122);
        }
        if (newShortcut.isExcludedFromSurfaces(1)) {
            if (isAppSearchEnabled()) {
                synchronized (this.mPackageItemLock) {
                    this.mTransientShortcuts.put(newShortcut.getId(), newShortcut);
                }
            }
        } else {
            forceReplaceShortcutInner(newShortcut);
        }
        if (oldShortcut != null) {
            return true;
        }
        return false;
    }

    public boolean pushDynamicShortcut(final android.content.pm.ShortcutInfo newShortcut, java.util.List<android.content.pm.ShortcutInfo> changedShortcuts) {
        boolean z;
        com.android.internal.util.Preconditions.checkArgument(newShortcut.isEnabled(), "pushDynamicShortcuts() cannot publish disabled shortcuts");
        newShortcut.addFlags(1);
        changedShortcuts.clear();
        android.content.pm.ShortcutInfo oldShortcut = findShortcutById(newShortcut.getId());
        boolean deleted = false;
        if (oldShortcut == null || !oldShortcut.isDynamic()) {
            com.android.server.pm.ShortcutService service = this.mShortcutUser.mService;
            int maxShortcuts = service.getMaxActivityShortcuts();
            android.util.ArrayMap<android.content.ComponentName, java.util.ArrayList<android.content.pm.ShortcutInfo>> all = sortShortcutsToActivities();
            java.util.ArrayList<android.content.pm.ShortcutInfo> activityShortcuts = all.get(newShortcut.getActivity());
            if (activityShortcuts != null && activityShortcuts.size() > maxShortcuts) {
                service.wtf("Error pushing shortcut. There are already " + activityShortcuts.size() + " shortcuts.");
            }
            if (activityShortcuts != null && activityShortcuts.size() >= maxShortcuts) {
                java.util.Collections.sort(activityShortcuts, this.mShortcutTypeAndRankComparator);
                android.content.pm.ShortcutInfo shortcut = activityShortcuts.get(maxShortcuts - 1);
                if (shortcut.isManifestShortcut()) {
                    android.util.Slog.e(TAG, "Failed to remove manifest shortcut while pushing dynamic shortcut " + newShortcut.getId());
                    return true;
                }
                changedShortcuts.add(shortcut);
                if (deleteDynamicWithId(shortcut.getId(), true, true) != null) {
                    z = true;
                } else {
                    z = false;
                }
                deleted = z;
            }
        }
        if (oldShortcut != null) {
            oldShortcut.ensureUpdatableWith(newShortcut, false);
            newShortcut.addFlags(oldShortcut.getFlags() & 1610629122);
        }
        if (newShortcut.isExcludedFromSurfaces(1)) {
            if (isAppSearchEnabled()) {
                synchronized (this.mPackageItemLock) {
                    this.mTransientShortcuts.put(newShortcut.getId(), newShortcut);
                }
            }
        } else {
            forceReplaceShortcutInner(newShortcut);
        }
        if (isAppSearchEnabled()) {
            runAsSystem(new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda48
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$pushDynamicShortcut$3(newShortcut);
                }
            });
        }
        return deleted;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pushDynamicShortcut$3(final android.content.pm.ShortcutInfo newShortcut) {
        fromAppSearch().thenAccept(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda11
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$pushDynamicShortcut$2(newShortcut, (android.app.appsearch.AppSearchSession) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$pushDynamicShortcut$2(android.content.pm.ShortcutInfo newShortcut, android.app.appsearch.AppSearchSession session) {
        session.reportUsage(new android.app.appsearch.ReportUsageRequest.Builder(getPackageName(), newShortcut.getId()).build(), this.mExecutor, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda16
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.ShortcutPackage.lambda$pushDynamicShortcut$1((android.app.appsearch.AppSearchResult) obj);
            }
        });
    }

    static /* synthetic */ void lambda$pushDynamicShortcut$1(android.app.appsearch.AppSearchResult result) {
        if (!result.isSuccess()) {
            if (com.android.server.pm.ShortcutService.DEBUG) {
                android.util.Slog.e(TAG, "Failed to report usage via AppSearch. " + result.getErrorMessage());
            } else {
                android.util.Slog.e(TAG, "Failed to report usage via AppSearch. ");
            }
        }
    }

    private void ensureShortcutCountBeforePush() {
        com.android.server.pm.ShortcutService service = this.mShortcutUser.mService;
        int maxShortcutPerApp = service.getMaxAppShortcuts();
        synchronized (this.mPackageItemLock) {
            java.util.List<android.content.pm.ShortcutInfo> appShortcuts = (java.util.List) this.mShortcuts.values().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda49
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.pm.ShortcutPackage.lambda$ensureShortcutCountBeforePush$4((android.content.pm.ShortcutInfo) obj);
                }
            }).collect(java.util.stream.Collectors.toList());
            if (appShortcuts.size() >= maxShortcutPerApp) {
                java.util.Collections.sort(appShortcuts, this.mShortcutTypeRankAndTimeComparator);
                while (appShortcuts.size() >= maxShortcutPerApp) {
                    android.content.pm.ShortcutInfo shortcut = appShortcuts.remove(appShortcuts.size() - 1);
                    if (shortcut.isDeclaredInManifest()) {
                        throw new java.lang.IllegalArgumentException(getPackageName() + " has published " + appShortcuts.size() + " manifest shortcuts across different activities.");
                    }
                    forceDeleteShortcutInner(shortcut.getId());
                }
            }
        }
    }

    static /* synthetic */ boolean lambda$ensureShortcutCountBeforePush$4(android.content.pm.ShortcutInfo si) {
        return !si.isPinned();
    }

    private java.util.List<android.content.pm.ShortcutInfo> removeOrphans() {
        final java.util.List<android.content.pm.ShortcutInfo> removeList = new java.util.ArrayList<>(1);
        forEachShortcut(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda14
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.ShortcutPackage.lambda$removeOrphans$5(removeList, (android.content.pm.ShortcutInfo) obj);
            }
        });
        if (!removeList.isEmpty()) {
            for (int i = removeList.size() - 1; i >= 0; i--) {
                forceDeleteShortcutInner(removeList.get(i).getId());
            }
        }
        return removeList;
    }

    static /* synthetic */ void lambda$removeOrphans$5(java.util.List removeList, android.content.pm.ShortcutInfo si) {
        if (si.isAlive()) {
            return;
        }
        removeList.add(si);
    }

    public java.util.List<android.content.pm.ShortcutInfo> deleteAllDynamicShortcuts() {
        long now = this.mShortcutUser.mService.injectCurrentTimeMillis();
        boolean changed = false;
        synchronized (this.mPackageItemLock) {
            for (int i = this.mShortcuts.size() - 1; i >= 0; i--) {
                android.content.pm.ShortcutInfo si = this.mShortcuts.valueAt(i);
                if (si.isDynamic() && si.isVisibleToPublisher()) {
                    changed = true;
                    si.setTimestamp(now);
                    si.clearFlags(1);
                    si.setRank(0);
                }
            }
        }
        removeAllShortcutsAsync();
        if (changed) {
            return removeOrphans();
        }
        return null;
    }

    public android.content.pm.ShortcutInfo deleteDynamicWithId(java.lang.String shortcutId, boolean ignoreInvisible, boolean ignorePersistedShortcuts) {
        return deleteOrDisableWithId(shortcutId, false, false, ignoreInvisible, 0, ignorePersistedShortcuts);
    }

    private android.content.pm.ShortcutInfo disableDynamicWithId(java.lang.String shortcutId, boolean ignoreInvisible, int disabledReason, boolean ignorePersistedShortcuts) {
        return deleteOrDisableWithId(shortcutId, true, false, ignoreInvisible, disabledReason, ignorePersistedShortcuts);
    }

    public android.content.pm.ShortcutInfo deleteLongLivedWithId(java.lang.String shortcutId, boolean ignoreInvisible) {
        android.content.pm.ShortcutInfo shortcut = findShortcutById(shortcutId);
        if (shortcut != null) {
            mutateShortcut(shortcutId, null, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda47
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    ((android.content.pm.ShortcutInfo) obj).clearFlags(1610629120);
                }
            });
        }
        return deleteOrDisableWithId(shortcutId, false, false, ignoreInvisible, 0, false);
    }

    public android.content.pm.ShortcutInfo disableWithId(java.lang.String shortcutId, final java.lang.String disabledMessage, final int disabledMessageResId, boolean overrideImmutable, boolean ignoreInvisible, int disabledReason) {
        android.content.pm.ShortcutInfo deleted = deleteOrDisableWithId(shortcutId, true, overrideImmutable, ignoreInvisible, disabledReason, false);
        mutateShortcut(shortcutId, null, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda32
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$disableWithId$7(disabledMessage, disabledMessageResId, (android.content.pm.ShortcutInfo) obj);
            }
        });
        return deleted;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$disableWithId$7(java.lang.String disabledMessage, int disabledMessageResId, android.content.pm.ShortcutInfo disabled) {
        if (disabled != null) {
            if (disabledMessage != null) {
                disabled.setDisabledMessage(disabledMessage);
            } else if (disabledMessageResId != 0) {
                disabled.setDisabledMessageResId(disabledMessageResId);
                this.mShortcutUser.mService.fixUpShortcutResourceNamesAndValues(disabled);
            }
        }
    }

    private android.content.pm.ShortcutInfo deleteOrDisableWithId(java.lang.String shortcutId, final boolean disable, boolean overrideImmutable, boolean ignoreInvisible, final int disabledReason, boolean ignorePersistedShortcuts) {
        com.android.internal.util.Preconditions.checkState(disable == (disabledReason != 0), "disable and disabledReason disagree: " + disable + " vs " + disabledReason);
        android.content.pm.ShortcutInfo oldShortcut = findShortcutById(shortcutId);
        if (oldShortcut == null || (!oldShortcut.isEnabled() && ignoreInvisible && !oldShortcut.isVisibleToPublisher())) {
            return null;
        }
        if (!overrideImmutable) {
            ensureNotImmutable(oldShortcut, true);
        }
        if (!ignorePersistedShortcuts) {
            removeShortcutAsync(shortcutId);
        }
        if (oldShortcut.isPinned() || oldShortcut.isCached()) {
            mutateShortcut(oldShortcut.getId(), oldShortcut, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda59
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$deleteOrDisableWithId$8(disable, disabledReason, (android.content.pm.ShortcutInfo) obj);
                }
            });
            return null;
        }
        forceDeleteShortcutInner(shortcutId);
        return oldShortcut;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$deleteOrDisableWithId$8(boolean disable, int disabledReason, android.content.pm.ShortcutInfo si) {
        si.setRank(0);
        si.clearFlags(33);
        if (disable) {
            si.addFlags(64);
            if (si.getDisabledReason() == 0) {
                si.setDisabledReason(disabledReason);
            }
        }
        si.setTimestamp(this.mShortcutUser.mService.injectCurrentTimeMillis());
        if (this.mShortcutUser.mService.isDummyMainActivity(si.getActivity())) {
            si.setActivity(null);
        }
    }

    public void enableWithId(java.lang.String shortcutId) {
        mutateShortcut(shortcutId, null, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda40
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$enableWithId$9((android.content.pm.ShortcutInfo) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enableWithId$9(android.content.pm.ShortcutInfo si) {
        ensureNotImmutable(si, true);
        si.clearFlags(64);
        si.setDisabledReason(0);
    }

    public void updateInvisibleShortcutForPinRequestWith(android.content.pm.ShortcutInfo shortcut) {
        android.content.pm.ShortcutInfo source = findShortcutById(shortcut.getId());
        java.util.Objects.requireNonNull(source);
        this.mShortcutUser.mService.validateShortcutForPinRequest(shortcut);
        shortcut.addFlags(2);
        forceReplaceShortcutInner(shortcut);
        adjustRanks();
    }

    public void refreshPinnedFlags() {
        final java.util.Set<java.lang.String> pinnedShortcuts = new android.util.ArraySet<>();
        this.mShortcutUser.forAllLaunchers(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda21
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$refreshPinnedFlags$10(pinnedShortcuts, (com.android.server.pm.ShortcutLauncher) obj);
            }
        });
        java.util.List<android.content.pm.ShortcutInfo> pinned = findAll(pinnedShortcuts);
        if (pinned != null) {
            pinned.forEach(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda22
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.pm.ShortcutPackage.lambda$refreshPinnedFlags$11((android.content.pm.ShortcutInfo) obj);
                }
            });
        }
        forEachShortcutMutate(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda23
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.ShortcutPackage.lambda$refreshPinnedFlags$12(pinnedShortcuts, (android.content.pm.ShortcutInfo) obj);
            }
        });
        this.mShortcutUser.forAllLaunchers(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda24
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((com.android.server.pm.ShortcutLauncher) obj).scheduleSave();
            }
        });
        removeOrphans();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshPinnedFlags$10(java.util.Set pinnedShortcuts, com.android.server.pm.ShortcutLauncher launcherShortcuts) {
        android.util.ArraySet<java.lang.String> pinned = launcherShortcuts.getPinnedShortcutIds(getPackageName(), getPackageUserId());
        if (pinned == null || pinned.size() == 0) {
            return;
        }
        pinnedShortcuts.addAll(pinned);
    }

    static /* synthetic */ void lambda$refreshPinnedFlags$11(android.content.pm.ShortcutInfo si) {
        if (!si.isPinned()) {
            si.addFlags(2);
        }
    }

    static /* synthetic */ void lambda$refreshPinnedFlags$12(java.util.Set pinnedShortcuts, android.content.pm.ShortcutInfo si) {
        if (!pinnedShortcuts.contains(si.getId()) && si.isPinned()) {
            si.clearFlags(2);
        }
    }

    public int getApiCallCount(boolean unlimited) {
        com.android.server.pm.ShortcutService s = this.mShortcutUser.mService;
        if (s.isUidForegroundLocked(this.mPackageUid) || this.mLastKnownForegroundElapsedTime < s.getUidLastForegroundElapsedTimeLocked(this.mPackageUid) || unlimited) {
            this.mLastKnownForegroundElapsedTime = s.injectElapsedRealtime();
            resetRateLimiting();
        }
        long last = s.getLastResetTimeLocked();
        long now = s.injectCurrentTimeMillis();
        if (com.android.server.pm.ShortcutService.isClockValid(now) && this.mLastResetTime > now) {
            android.util.Slog.w(TAG, "Clock rewound");
            this.mLastResetTime = now;
            this.mApiCallCount = 0;
            return this.mApiCallCount;
        }
        if (this.mLastResetTime < last) {
            boolean z = com.android.server.pm.ShortcutService.DEBUG;
            android.util.Slog.d(TAG, java.lang.String.format("%s: last reset=%d, now=%d, last=%d: resetting", getPackageName(), java.lang.Long.valueOf(this.mLastResetTime), java.lang.Long.valueOf(now), java.lang.Long.valueOf(last)));
            this.mApiCallCount = 0;
            this.mLastResetTime = last;
        }
        return this.mApiCallCount;
    }

    public boolean tryApiCall(boolean unlimited) {
        com.android.server.pm.ShortcutService s = this.mShortcutUser.mService;
        if (getApiCallCount(unlimited) >= s.mMaxUpdatesPerInterval) {
            return false;
        }
        this.mApiCallCount++;
        scheduleSave();
        return true;
    }

    public void resetRateLimiting() {
        if (com.android.server.pm.ShortcutService.DEBUG) {
            android.util.Slog.d(TAG, "resetRateLimiting: " + getPackageName());
        }
        if (this.mApiCallCount > 0) {
            this.mApiCallCount = 0;
            scheduleSave();
        }
    }

    public void resetRateLimitingForCommandLineNoSaving() {
        this.mApiCallCount = 0;
        this.mLastResetTime = 0L;
    }

    public void findAll(java.util.List<android.content.pm.ShortcutInfo> result, java.util.function.Predicate<android.content.pm.ShortcutInfo> filter, int cloneFlag) {
        findAll(result, filter, cloneFlag, null, 0, false);
    }

    public void findAll(final java.util.List<android.content.pm.ShortcutInfo> result, final java.util.function.Predicate<android.content.pm.ShortcutInfo> filter, final int cloneFlag, final java.lang.String callingLauncher, int launcherUserId, final boolean getPinnedByAnyLauncher) {
        if (getPackageInfo().isShadow()) {
            android.util.Log.d(TAG, "findAll() returned empty results because " + getPackageName() + " isn't installed yet");
            return;
        }
        com.android.server.pm.ShortcutService s = this.mShortcutUser.mService;
        final android.util.ArraySet<java.lang.String> pinnedByCallerSet = callingLauncher == null ? null : s.getLauncherShortcutsLocked(callingLauncher, getPackageUserId(), launcherUserId).getPinnedShortcutIds(getPackageName(), getPackageUserId());
        forEachShortcut(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda20
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$findAll$13(result, filter, cloneFlag, callingLauncher, pinnedByCallerSet, getPinnedByAnyLauncher, (android.content.pm.ShortcutInfo) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX INFO: renamed from: filter, reason: merged with bridge method [inline-methods] */
    public void lambda$findAll$13(java.util.List<android.content.pm.ShortcutInfo> result, java.util.function.Predicate<android.content.pm.ShortcutInfo> query, int cloneFlag, java.lang.String callingLauncher, android.util.ArraySet<java.lang.String> pinnedByCallerSet, boolean getPinnedByAnyLauncher, android.content.pm.ShortcutInfo si) {
        boolean isPinnedByCaller = callingLauncher == null || (pinnedByCallerSet != null && pinnedByCallerSet.contains(si.getId()));
        if (!getPinnedByAnyLauncher && si.isFloating() && !si.isCached() && !isPinnedByCaller) {
            android.util.Log.d(TAG, si.getId() + " ignored because it isn't pinned by " + callingLauncher);
            return;
        }
        android.content.pm.ShortcutInfo clone = si.clone(cloneFlag);
        if (!getPinnedByAnyLauncher && !isPinnedByCaller) {
            clone.clearFlags(2);
        }
        if (query == null || query.test(clone)) {
            if (!isPinnedByCaller) {
                clone.clearFlags(2);
            }
            result.add(clone);
        }
    }

    public void resetThrottling() {
        this.mApiCallCount = 0;
    }

    public java.util.List<android.content.pm.ShortcutManager.ShareShortcutInfo> getMatchingShareTargets(android.content.IntentFilter filter) {
        return getMatchingShareTargets(filter, null);
    }

    java.util.List<android.content.pm.ShortcutManager.ShareShortcutInfo> getMatchingShareTargets(android.content.IntentFilter filter, java.lang.String pkgName) throws java.lang.Throwable {
        synchronized (this.mPackageItemLock) {
            try {
                java.util.List<com.android.server.pm.ShareTargetInfo> matchedTargets = new java.util.ArrayList<>();
                for (int i = 0; i < this.mShareTargets.size(); i++) {
                    com.android.server.pm.ShareTargetInfo target = this.mShareTargets.get(i);
                    com.android.server.pm.ShareTargetInfo.TargetData[] targetDataArr = target.mTargetData;
                    int length = targetDataArr.length;
                    int i2 = 0;
                    while (true) {
                        if (i2 < length) {
                            com.android.server.pm.ShareTargetInfo.TargetData data = targetDataArr[i2];
                            try {
                                if (!filter.hasDataType(data.mMimeType)) {
                                    i2++;
                                } else {
                                    matchedTargets.add(target);
                                    break;
                                }
                            } catch (java.lang.Throwable th) {
                                th = th;
                                throw th;
                            }
                        }
                    }
                }
                if (matchedTargets.isEmpty()) {
                    return new java.util.ArrayList();
                }
                java.util.ArrayList<android.content.pm.ShortcutInfo> shortcuts = new java.util.ArrayList<>();
                findAll(shortcuts, new com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda18(), 9, pkgName, 0, false);
                java.util.List<android.content.pm.ShortcutManager.ShareShortcutInfo> result = new java.util.ArrayList<>();
                for (int i3 = 0; i3 < shortcuts.size(); i3++) {
                    java.util.Set<java.lang.String> categories = shortcuts.get(i3).getCategories();
                    if (categories != null && !categories.isEmpty()) {
                        int j = 0;
                        while (true) {
                            if (j < matchedTargets.size()) {
                                boolean hasAllCategories = true;
                                com.android.server.pm.ShareTargetInfo target2 = matchedTargets.get(j);
                                int q = 0;
                                while (true) {
                                    if (q >= target2.mCategories.length) {
                                        break;
                                    }
                                    if (categories.contains(target2.mCategories[q])) {
                                        q++;
                                    } else {
                                        hasAllCategories = false;
                                        break;
                                    }
                                }
                                if (!hasAllCategories) {
                                    j++;
                                } else {
                                    result.add(new android.content.pm.ShortcutManager.ShareShortcutInfo(shortcuts.get(i3), new android.content.ComponentName(getPackageName(), target2.mTargetClass)));
                                    break;
                                }
                            }
                        }
                    }
                }
                return result;
            } catch (java.lang.Throwable th2) {
                th = th2;
            }
        }
    }

    public boolean hasShareTargets() {
        boolean z;
        synchronized (this.mPackageItemLock) {
            z = !this.mShareTargets.isEmpty();
        }
        return z;
    }

    int getSharingShortcutCount() {
        synchronized (this.mPackageItemLock) {
            if (this.mShareTargets.isEmpty()) {
                return 0;
            }
            java.util.ArrayList<android.content.pm.ShortcutInfo> shortcuts = new java.util.ArrayList<>();
            findAll(shortcuts, new com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda18(), 27);
            int sharingShortcutCount = 0;
            for (int i = 0; i < shortcuts.size(); i++) {
                java.util.Set<java.lang.String> categories = shortcuts.get(i).getCategories();
                if (categories != null && !categories.isEmpty()) {
                    int j = 0;
                    while (true) {
                        if (j < this.mShareTargets.size()) {
                            boolean hasAllCategories = true;
                            com.android.server.pm.ShareTargetInfo target = this.mShareTargets.get(j);
                            int q = 0;
                            while (true) {
                                if (q >= target.mCategories.length) {
                                    break;
                                }
                                if (categories.contains(target.mCategories[q])) {
                                    q++;
                                } else {
                                    hasAllCategories = false;
                                    break;
                                }
                            }
                            if (!hasAllCategories) {
                                j++;
                            } else {
                                sharingShortcutCount++;
                                break;
                            }
                        }
                    }
                }
            }
            return sharingShortcutCount;
        }
    }

    private android.util.ArraySet<java.lang.String> getUsedBitmapFilesLocked() {
        final android.util.ArraySet<java.lang.String> usedFiles = new android.util.ArraySet<>(1);
        forEachShortcut(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda58
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.ShortcutPackage.lambda$getUsedBitmapFilesLocked$14(usedFiles, (android.content.pm.ShortcutInfo) obj);
            }
        });
        return usedFiles;
    }

    static /* synthetic */ void lambda$getUsedBitmapFilesLocked$14(android.util.ArraySet usedFiles, android.content.pm.ShortcutInfo si) {
        if (si.getBitmapPath() != null) {
            usedFiles.add(getFileName(si.getBitmapPath()));
        }
    }

    public void cleanupDanglingBitmapFiles(java.io.File path) {
        synchronized (this.mPackageItemLock) {
            this.mShortcutBitmapSaver.waitForAllSavesLocked();
            android.util.ArraySet<java.lang.String> usedFiles = getUsedBitmapFilesLocked();
            for (java.io.File child : path.listFiles()) {
                if (child.isFile()) {
                    java.lang.String name = child.getName();
                    if (!usedFiles.contains(name)) {
                        if (com.android.server.pm.ShortcutService.DEBUG) {
                            android.util.Slog.d(TAG, "Removing dangling bitmap file: " + child.getAbsolutePath());
                        }
                        child.delete();
                    }
                }
            }
        }
    }

    private static java.lang.String getFileName(java.lang.String path) {
        int sep = path.lastIndexOf(java.io.File.separatorChar);
        if (sep == -1) {
            return path;
        }
        return path.substring(sep + 1);
    }

    private boolean areAllActivitiesStillEnabled() {
        final com.android.server.pm.ShortcutService s = this.mShortcutUser.mService;
        final java.util.ArrayList<android.content.ComponentName> checked = new java.util.ArrayList<>(4);
        final boolean[] reject = new boolean[1];
        forEachShortcutStopWhen(new java.util.function.Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda19
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return this.f$0.lambda$areAllActivitiesStillEnabled$15(checked, s, reject, (android.content.pm.ShortcutInfo) obj);
            }
        });
        return true ^ reject[0];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ java.lang.Boolean lambda$areAllActivitiesStillEnabled$15(java.util.ArrayList checked, com.android.server.pm.ShortcutService s, boolean[] reject, android.content.pm.ShortcutInfo si) {
        android.content.ComponentName activity = si.getActivity();
        if (checked.contains(activity)) {
            return false;
        }
        checked.add(activity);
        if (activity == null || s.injectIsActivityEnabledAndExported(activity, getOwnerUserId())) {
            return false;
        }
        reject[0] = true;
        return true;
    }

    public boolean rescanPackageIfNeeded(boolean isNewApp, boolean forceRescan) {
        final com.android.server.pm.ShortcutService s = this.mShortcutUser.mService;
        long start = s.getStatStartTime();
        try {
            android.content.pm.PackageInfo pi = this.mShortcutUser.mService.getPackageInfo(getPackageName(), getPackageUserId());
            if (pi == null) {
                return false;
            }
            if (!isNewApp && !forceRescan && getPackageInfo().getVersionCode() == pi.getLongVersionCode() && getPackageInfo().getLastUpdateTime() == pi.lastUpdateTime) {
                if (areAllActivitiesStillEnabled()) {
                    return false;
                }
            }
            s.logDurationStat(14, start);
            java.util.List<android.content.pm.ShortcutInfo> newManifestShortcutList = null;
            int shareTargetSize = 0;
            synchronized (this.mPackageItemLock) {
                try {
                    shareTargetSize = this.mShareTargets.size();
                    java.util.List<android.content.pm.ShortcutInfo> newManifestShortcutList2 = com.android.server.pm.ShortcutParser.parseShortcuts(this.mShortcutUser.mService, getPackageName(), getPackageUserId(), this.mShareTargets);
                    newManifestShortcutList = newManifestShortcutList2;
                } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                    android.util.Slog.e(TAG, "Failed to load shortcuts from AndroidManifest.xml.", e);
                }
            }
            int manifestShortcutSize = newManifestShortcutList == null ? 0 : newManifestShortcutList.size();
            boolean z = com.android.server.pm.ShortcutService.DEBUG;
            android.util.Slog.d(TAG, java.lang.String.format("Package %s has %d manifest shortcut(s), and %d share target(s)", getPackageName(), java.lang.Integer.valueOf(manifestShortcutSize), java.lang.Integer.valueOf(shareTargetSize)));
            if (isNewApp && manifestShortcutSize == 0) {
                return false;
            }
            boolean z2 = com.android.server.pm.ShortcutService.DEBUG;
            android.util.Slog.d(TAG, java.lang.String.format("Package %s %s, version %d -> %d", getPackageName(), isNewApp ? "added" : "updated", java.lang.Long.valueOf(getPackageInfo().getVersionCode()), java.lang.Long.valueOf(pi.getLongVersionCode())));
            getPackageInfo().updateFromPackageInfo(pi);
            final long newVersionCode = getPackageInfo().getVersionCode();
            forEachShortcutMutate(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda9
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$rescanPackageIfNeeded$16(newVersionCode, (android.content.pm.ShortcutInfo) obj);
                }
            });
            if (!isNewApp) {
                final android.content.res.Resources publisherRes = getPackageResources();
                forEachShortcutMutate(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda10
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$rescanPackageIfNeeded$17(s, publisherRes, (android.content.pm.ShortcutInfo) obj);
                    }
                });
            }
            publishManifestShortcuts(newManifestShortcutList);
            if (newManifestShortcutList != null) {
                pushOutExcessShortcuts();
            }
            s.verifyStates();
            s.packageShortcutsChanged(this, null, null);
            return true;
        } finally {
            s.logDurationStat(14, start);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rescanPackageIfNeeded$16(long newVersionCode, android.content.pm.ShortcutInfo si) {
        if (si.getDisabledReason() != 100) {
            return;
        }
        if (getPackageInfo().getBackupSourceVersionCode() > newVersionCode) {
            if (com.android.server.pm.ShortcutService.DEBUG) {
                android.util.Slog.d(TAG, java.lang.String.format("Shortcut %s require version %s, still not restored.", si.getId(), java.lang.Long.valueOf(getPackageInfo().getBackupSourceVersionCode())));
            }
        } else {
            android.util.Slog.i(TAG, java.lang.String.format("Restoring shortcut: %s", si.getId()));
            si.clearFlags(64);
            si.setDisabledReason(0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$rescanPackageIfNeeded$17(com.android.server.pm.ShortcutService s, android.content.res.Resources publisherRes, android.content.pm.ShortcutInfo si) {
        if (si.isDynamic()) {
            if (si.getActivity() == null) {
                s.wtf("null activity detected.");
            } else if (!s.injectIsMainActivity(si.getActivity(), getPackageUserId())) {
                android.util.Slog.w(TAG, java.lang.String.format("%s is no longer main activity. Disabling shorcut %s.", getPackageName(), si.getId()));
                if (disableDynamicWithId(si.getId(), false, 2, false) != null) {
                    return;
                }
            }
        }
        if (!si.hasAnyResources() || publisherRes == null) {
            return;
        }
        if (!si.isOriginallyFromManifest()) {
            si.lookupAndFillInResourceIds(publisherRes);
        }
        si.setTimestamp(s.injectCurrentTimeMillis());
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x007c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private boolean publishManifestShortcuts(java.util.List<android.content.pm.ShortcutInfo> r14) {
        /*
            Method dump skipped, instruction units count: 211
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ShortcutPackage.publishManifestShortcuts(java.util.List):boolean");
    }

    static /* synthetic */ void lambda$publishManifestShortcuts$18(android.util.ArraySet toDisableList, android.content.pm.ShortcutInfo si) {
        if (si.isManifestShortcut()) {
            toDisableList.add(si.getId());
        }
    }

    private boolean pushOutExcessShortcuts() {
        com.android.server.pm.ShortcutService service = this.mShortcutUser.mService;
        int maxShortcuts = service.getMaxActivityShortcuts();
        android.util.ArrayMap<android.content.ComponentName, java.util.ArrayList<android.content.pm.ShortcutInfo>> all = sortShortcutsToActivities();
        for (int outer = all.size() - 1; outer >= 0; outer--) {
            java.util.ArrayList<android.content.pm.ShortcutInfo> list = all.valueAt(outer);
            if (list.size() > maxShortcuts) {
                java.util.Collections.sort(list, this.mShortcutTypeAndRankComparator);
                for (int inner = list.size() - 1; inner >= maxShortcuts; inner--) {
                    android.content.pm.ShortcutInfo shortcut = list.get(inner);
                    if (shortcut.isManifestShortcut()) {
                        service.wtf("Found manifest shortcuts in excess list.");
                    } else {
                        deleteDynamicWithId(shortcut.getId(), true, true);
                    }
                }
            }
        }
        return false;
    }

    static /* synthetic */ int lambda$new$19(android.content.pm.ShortcutInfo a, android.content.pm.ShortcutInfo b) {
        if (a.isManifestShortcut() && !b.isManifestShortcut()) {
            return -1;
        }
        if (!a.isManifestShortcut() && b.isManifestShortcut()) {
            return 1;
        }
        return java.lang.Integer.compare(a.getRank(), b.getRank());
    }

    static /* synthetic */ int lambda$new$20(android.content.pm.ShortcutInfo a, android.content.pm.ShortcutInfo b) {
        if (a.isDeclaredInManifest() && !b.isDeclaredInManifest()) {
            return -1;
        }
        if (!a.isDeclaredInManifest() && b.isDeclaredInManifest()) {
            return 1;
        }
        if (a.isDynamic() && b.isDynamic()) {
            return java.lang.Integer.compare(a.getRank(), b.getRank());
        }
        if (a.isDynamic()) {
            return -1;
        }
        if (b.isDynamic()) {
            return 1;
        }
        if (a.isCached() && b.isCached()) {
            if (a.hasFlags(536870912) && !b.hasFlags(536870912)) {
                return -1;
            }
            if (!a.hasFlags(536870912) && b.hasFlags(536870912)) {
                return 1;
            }
            if (a.hasFlags(1073741824) && !b.hasFlags(1073741824)) {
                return -1;
            }
            if (!a.hasFlags(1073741824) && b.hasFlags(1073741824)) {
                return 1;
            }
        }
        if (a.isCached()) {
            return -1;
        }
        if (b.isCached()) {
            return 1;
        }
        return java.lang.Long.compare(b.getLastChangedTimestamp(), a.getLastChangedTimestamp());
    }

    private android.util.ArrayMap<android.content.ComponentName, java.util.ArrayList<android.content.pm.ShortcutInfo>> sortShortcutsToActivities() {
        final android.util.ArrayMap<android.content.ComponentName, java.util.ArrayList<android.content.pm.ShortcutInfo>> activitiesToShortcuts = new android.util.ArrayMap<>();
        forEachShortcut(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda2
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$sortShortcutsToActivities$22(activitiesToShortcuts, (android.content.pm.ShortcutInfo) obj);
            }
        });
        return activitiesToShortcuts;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sortShortcutsToActivities$22(android.util.ArrayMap activitiesToShortcuts, android.content.pm.ShortcutInfo si) {
        if (si.isFloating()) {
            return;
        }
        android.content.ComponentName activity = si.getActivity();
        if (activity == null) {
            this.mShortcutUser.mService.wtf("null activity detected.");
        } else {
            java.util.ArrayList<android.content.pm.ShortcutInfo> list = (java.util.ArrayList) activitiesToShortcuts.computeIfAbsent(activity, new java.util.function.Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda46
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.pm.ShortcutPackage.lambda$sortShortcutsToActivities$21((android.content.ComponentName) obj);
                }
            });
            list.add(si);
        }
    }

    static /* synthetic */ java.util.ArrayList lambda$sortShortcutsToActivities$21(android.content.ComponentName k) {
        return new java.util.ArrayList();
    }

    private void incrementCountForActivity(android.util.ArrayMap<android.content.ComponentName, java.lang.Integer> counts, android.content.ComponentName cn, int increment) {
        java.lang.Integer oldValue = counts.get(cn);
        if (oldValue == null) {
            oldValue = 0;
        }
        counts.put(cn, java.lang.Integer.valueOf(oldValue.intValue() + increment));
    }

    public void enforceShortcutCountsBeforeOperation(java.util.List<android.content.pm.ShortcutInfo> newList, final int operation) {
        com.android.server.pm.ShortcutService service = this.mShortcutUser.mService;
        final android.util.ArrayMap<android.content.ComponentName, java.lang.Integer> counts = new android.util.ArrayMap<>(4);
        forEachShortcut(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda33
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$enforceShortcutCountsBeforeOperation$23(counts, operation, (android.content.pm.ShortcutInfo) obj);
            }
        });
        for (int i = newList.size() - 1; i >= 0; i--) {
            android.content.pm.ShortcutInfo newShortcut = newList.get(i);
            android.content.ComponentName newActivity = newShortcut.getActivity();
            if (newActivity == null) {
                if (operation != 2) {
                    service.wtf("Activity must not be null at this point");
                }
            } else {
                android.content.pm.ShortcutInfo original = findShortcutById(newShortcut.getId());
                if (original == null) {
                    if (operation != 2) {
                        incrementCountForActivity(counts, newActivity, 1);
                    }
                } else if (!original.isFloating() || operation != 2) {
                    if (operation != 0) {
                        android.content.ComponentName oldActivity = original.getActivity();
                        if (!original.isFloating()) {
                            incrementCountForActivity(counts, oldActivity, -1);
                        }
                    }
                    incrementCountForActivity(counts, newActivity, 1);
                }
            }
        }
        int i2 = counts.size();
        for (int i3 = i2 - 1; i3 >= 0; i3--) {
            service.enforceMaxActivityShortcuts(counts.valueAt(i3).intValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$enforceShortcutCountsBeforeOperation$23(android.util.ArrayMap counts, int operation, android.content.pm.ShortcutInfo shortcut) {
        if (shortcut.isManifestShortcut()) {
            incrementCountForActivity(counts, shortcut.getActivity(), 1);
        } else if (shortcut.isDynamic() && operation != 0) {
            incrementCountForActivity(counts, shortcut.getActivity(), 1);
        }
    }

    public void resolveResourceStrings() {
        final com.android.server.pm.ShortcutService s = this.mShortcutUser.mService;
        final android.content.res.Resources publisherRes = getPackageResources();
        final java.util.List<android.content.pm.ShortcutInfo> changedShortcuts = new java.util.ArrayList<>(1);
        if (publisherRes != null) {
            forEachShortcutMutate(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda36
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    com.android.server.pm.ShortcutPackage.lambda$resolveResourceStrings$24(publisherRes, s, changedShortcuts, (android.content.pm.ShortcutInfo) obj);
                }
            });
        }
        if (!com.android.internal.util.CollectionUtils.isEmpty(changedShortcuts)) {
            s.packageShortcutsChanged(this, changedShortcuts, null);
        }
    }

    static /* synthetic */ void lambda$resolveResourceStrings$24(android.content.res.Resources publisherRes, com.android.server.pm.ShortcutService s, java.util.List changedShortcuts, android.content.pm.ShortcutInfo si) {
        if (si.hasStringResources()) {
            si.resolveResourceStrings(publisherRes);
            si.setTimestamp(s.injectCurrentTimeMillis());
            changedShortcuts.add(si);
        }
    }

    public void clearAllImplicitRanks() {
        forEachShortcutMutate(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda61
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                ((android.content.pm.ShortcutInfo) obj).clearImplicitRankAndRankChangedFlag();
            }
        });
    }

    static /* synthetic */ int lambda$new$25(android.content.pm.ShortcutInfo a, android.content.pm.ShortcutInfo b) {
        int ret = java.lang.Integer.compare(a.getRank(), b.getRank());
        if (ret != 0) {
            return ret;
        }
        if (a.isRankChanged() != b.isRankChanged()) {
            return a.isRankChanged() ? -1 : 1;
        }
        int ret2 = java.lang.Integer.compare(a.getImplicitRank(), b.getImplicitRank());
        if (ret2 != 0) {
            return ret2;
        }
        return a.getId().compareTo(b.getId());
    }

    public void adjustRanks() {
        com.android.server.pm.ShortcutService s = this.mShortcutUser.mService;
        final long now = s.injectCurrentTimeMillis();
        forEachShortcutMutate(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda55
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.ShortcutPackage.lambda$adjustRanks$26(now, (android.content.pm.ShortcutInfo) obj);
            }
        });
        android.util.ArrayMap<android.content.ComponentName, java.util.ArrayList<android.content.pm.ShortcutInfo>> all = sortShortcutsToActivities();
        for (int outer = all.size() - 1; outer >= 0; outer--) {
            java.util.ArrayList<android.content.pm.ShortcutInfo> list = all.valueAt(outer);
            java.util.Collections.sort(list, this.mShortcutRankComparator);
            final int thisRank = 0;
            int size = list.size();
            for (int i = 0; i < size; i++) {
                android.content.pm.ShortcutInfo si = list.get(i);
                if (!si.isManifestShortcut()) {
                    if (!si.isDynamic()) {
                        s.wtf("Non-dynamic shortcut found. " + si.toInsecureString());
                    } else {
                        int rank = thisRank + 1;
                        if (si.getRank() != thisRank) {
                            mutateShortcut(si.getId(), si, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda56
                                @Override // java.util.function.Consumer
                                public final void accept(java.lang.Object obj) {
                                    com.android.server.pm.ShortcutPackage.lambda$adjustRanks$27(now, thisRank, (android.content.pm.ShortcutInfo) obj);
                                }
                            });
                        }
                        thisRank = rank;
                    }
                }
            }
        }
    }

    static /* synthetic */ void lambda$adjustRanks$26(long now, android.content.pm.ShortcutInfo si) {
        if (si.isFloating() && si.getRank() != 0) {
            si.setTimestamp(now);
            si.setRank(0);
        }
    }

    static /* synthetic */ void lambda$adjustRanks$27(long now, int thisRank, android.content.pm.ShortcutInfo shortcut) {
        shortcut.setTimestamp(now);
        shortcut.setRank(thisRank);
    }

    public boolean hasNonManifestShortcuts() {
        final boolean[] condition = new boolean[1];
        forEachShortcutStopWhen(new java.util.function.Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda1
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.pm.ShortcutPackage.lambda$hasNonManifestShortcuts$28(condition, (android.content.pm.ShortcutInfo) obj);
            }
        });
        return condition[0];
    }

    static /* synthetic */ java.lang.Boolean lambda$hasNonManifestShortcuts$28(boolean[] condition, android.content.pm.ShortcutInfo si) {
        if (!si.isDeclaredInManifest()) {
            condition[0] = true;
            return true;
        }
        return false;
    }

    void reportShortcutUsed(android.app.usage.UsageStatsManagerInternal usageStatsManagerInternal, java.lang.String shortcutId) {
        synchronized (this.mPackageItemLock) {
            long currentTS = android.os.SystemClock.elapsedRealtime();
            com.android.server.pm.ShortcutService s = this.mShortcutUser.mService;
            if (currentTS - this.mLastReportedTime > s.mSaveDelayMillis) {
                this.mLastReportedTime = currentTS;
                long token = s.injectClearCallingIdentity();
                try {
                    usageStatsManagerInternal.reportShortcutUsage(getPackageName(), shortcutId, getPackageUserId());
                } finally {
                    s.injectRestoreCallingIdentity(token);
                }
            }
        }
    }

    public void dump(final java.io.PrintWriter pw, final java.lang.String prefix, com.android.server.pm.ShortcutService.DumpFilter filter) {
        pw.println();
        pw.print(prefix);
        pw.print("Package: ");
        pw.print(getPackageName());
        pw.print("  UID: ");
        pw.print(this.mPackageUid);
        pw.println();
        pw.print(prefix);
        pw.print("  ");
        pw.print("Calls: ");
        pw.print(getApiCallCount(false));
        pw.println();
        pw.print(prefix);
        pw.print("  ");
        pw.print("Last known FG: ");
        pw.print(this.mLastKnownForegroundElapsedTime);
        pw.println();
        pw.print(prefix);
        pw.print("  ");
        pw.print("Last reset: [");
        pw.print(this.mLastResetTime);
        pw.print("] ");
        pw.print(com.android.server.pm.ShortcutService.formatTime(this.mLastResetTime));
        pw.println();
        getPackageInfo().dump(pw, prefix + "  ");
        pw.println();
        pw.print(prefix);
        pw.println("  Shortcuts:");
        final long[] totalBitmapSize = new long[1];
        forEachShortcut(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda43
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.ShortcutPackage.lambda$dump$29(pw, prefix, totalBitmapSize, (android.content.pm.ShortcutInfo) obj);
            }
        });
        pw.print(prefix);
        pw.print("  ");
        pw.print("Total bitmap size: ");
        pw.print(totalBitmapSize[0]);
        pw.print(" (");
        pw.print(android.text.format.Formatter.formatFileSize(this.mShortcutUser.mService.mContext, totalBitmapSize[0]));
        pw.println(")");
        pw.println();
        synchronized (this.mPackageItemLock) {
            this.mShortcutBitmapSaver.dumpLocked(pw, "  ");
        }
    }

    static /* synthetic */ void lambda$dump$29(java.io.PrintWriter pw, java.lang.String prefix, long[] totalBitmapSize, android.content.pm.ShortcutInfo si) {
        pw.println(si.toDumpString(prefix + "    "));
        if (si.getBitmapPath() != null) {
            long len = new java.io.File(si.getBitmapPath()).length();
            pw.print(prefix);
            pw.print("      ");
            pw.print("bitmap size=");
            pw.println(len);
            totalBitmapSize[0] = totalBitmapSize[0] + len;
        }
    }

    public void dumpShortcuts(final java.io.PrintWriter pw, int matchFlags) {
        boolean matchDynamic = (matchFlags & 2) != 0;
        boolean matchPinned = (matchFlags & 4) != 0;
        boolean matchManifest = (matchFlags & 1) != 0;
        boolean matchCached = (matchFlags & 8) != 0;
        final int shortcutFlags = (matchDynamic ? 1 : 0) | (matchPinned ? 2 : 0) | (matchManifest ? 32 : 0) | (matchCached ? 1610629120 : 0);
        forEachShortcut(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda50
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.ShortcutPackage.lambda$dumpShortcuts$30(shortcutFlags, pw, (android.content.pm.ShortcutInfo) obj);
            }
        });
    }

    static /* synthetic */ void lambda$dumpShortcuts$30(int shortcutFlags, java.io.PrintWriter pw, android.content.pm.ShortcutInfo si) {
        if ((si.getFlags() & shortcutFlags) != 0) {
            pw.println(si.toDumpString(""));
        }
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    public org.json.JSONObject dumpCheckin(boolean clear) throws org.json.JSONException {
        org.json.JSONObject result = super.dumpCheckin(clear);
        final int[] numDynamic = new int[1];
        final int[] numPinned = new int[1];
        final int[] numManifest = new int[1];
        final int[] numBitmaps = new int[1];
        final long[] totalBitmapSize = new long[1];
        forEachShortcut(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda57
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.ShortcutPackage.lambda$dumpCheckin$31(numDynamic, numManifest, numPinned, numBitmaps, totalBitmapSize, (android.content.pm.ShortcutInfo) obj);
            }
        });
        result.put(KEY_DYNAMIC, numDynamic[0]);
        result.put(KEY_MANIFEST, numManifest[0]);
        result.put(KEY_PINNED, numPinned[0]);
        result.put(KEY_BITMAPS, numBitmaps[0]);
        result.put(KEY_BITMAP_BYTES, totalBitmapSize[0]);
        return result;
    }

    static /* synthetic */ void lambda$dumpCheckin$31(int[] numDynamic, int[] numManifest, int[] numPinned, int[] numBitmaps, long[] totalBitmapSize, android.content.pm.ShortcutInfo si) {
        if (si.isDynamic()) {
            numDynamic[0] = numDynamic[0] + 1;
        }
        if (si.isDeclaredInManifest()) {
            numManifest[0] = numManifest[0] + 1;
        }
        if (si.isPinned()) {
            numPinned[0] = numPinned[0] + 1;
        }
        if (si.getBitmapPath() != null) {
            numBitmaps[0] = numBitmaps[0] + 1;
            totalBitmapSize[0] = totalBitmapSize[0] + new java.io.File(si.getBitmapPath()).length();
        }
    }

    private boolean hasNoShortcut() {
        if (!isAppSearchEnabled()) {
            return getShortcutCount() == 0;
        }
        final boolean[] hasAnyShortcut = new boolean[1];
        forEachShortcutStopWhen(new java.util.function.Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda13
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.pm.ShortcutPackage.lambda$hasNoShortcut$32(hasAnyShortcut, (android.content.pm.ShortcutInfo) obj);
            }
        });
        return !hasAnyShortcut[0];
    }

    static /* synthetic */ java.lang.Boolean lambda$hasNoShortcut$32(boolean[] hasAnyShortcut, android.content.pm.ShortcutInfo si) {
        hasAnyShortcut[0] = true;
        return true;
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    public void saveToXml(com.android.modules.utils.TypedXmlSerializer out, boolean forBackup) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        synchronized (this.mPackageItemLock) {
            int size = this.mShortcuts.size();
            int shareTargetSize = this.mShareTargets.size();
            if (hasNoShortcut() && shareTargetSize == 0 && this.mApiCallCount == 0) {
                return;
            }
            out.startTag((java.lang.String) null, "package");
            com.android.server.pm.ShortcutService.writeAttr(out, "name", getPackageName());
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_CALL_COUNT, this.mApiCallCount);
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_LAST_RESET, this.mLastResetTime);
            if (!forBackup) {
                com.android.server.pm.ShortcutService.writeAttr(out, ATTR_SCHEMA_VERSON, this.mIsAppSearchSchemaUpToDate ? 3L : 0L);
            }
            getPackageInfo().saveToXml(this.mShortcutUser.mService, out, forBackup);
            for (int j = 0; j < size; j++) {
                saveShortcut(out, this.mShortcuts.valueAt(j), forBackup, getPackageInfo().isBackupAllowed());
            }
            if (!forBackup) {
                for (int j2 = 0; j2 < shareTargetSize; j2++) {
                    this.mShareTargets.get(j2).saveToXml(out);
                }
            }
            out.endTag((java.lang.String) null, "package");
        }
    }

    private void saveShortcut(com.android.modules.utils.TypedXmlSerializer out, android.content.pm.ShortcutInfo si, boolean forBackup, boolean appSupportsBackup) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.server.pm.ShortcutService s = this.mShortcutUser.mService;
        if (forBackup && (!si.isPinned() || !si.isEnabled())) {
            return;
        }
        boolean shouldBackupDetails = !forBackup || appSupportsBackup;
        if (si.isIconPendingSave()) {
            removeIcon(si);
        }
        out.startTag((java.lang.String) null, TAG_SHORTCUT);
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_ID, si.getId());
        com.android.server.pm.ShortcutService.writeAttr(out, "activity", si.getActivity());
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_TITLE, si.getTitle());
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_TITLE_RES_ID, si.getTitleResId());
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_TITLE_RES_NAME, si.getTitleResName());
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_SPLASH_SCREEN_THEME_NAME, si.getStartingThemeResName());
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_TEXT, si.getText());
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_TEXT_RES_ID, si.getTextResId());
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_TEXT_RES_NAME, si.getTextResName());
        if (shouldBackupDetails) {
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_DISABLED_MESSAGE, si.getDisabledMessage());
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_DISABLED_MESSAGE_RES_ID, si.getDisabledMessageResourceId());
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_DISABLED_MESSAGE_RES_NAME, si.getDisabledMessageResName());
        }
        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_DISABLED_REASON, si.getDisabledReason());
        com.android.server.pm.ShortcutService.writeAttr(out, "timestamp", si.getLastChangedTimestamp());
        android.content.LocusId locusId = si.getLocusId();
        if (locusId != null) {
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_LOCUS_ID, si.getLocusId().getId());
        }
        if (forBackup) {
            int flags = si.getFlags() & (-35342);
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_FLAGS, flags);
            long packageVersionCode = getPackageInfo().getVersionCode();
            if (packageVersionCode == 0) {
                s.wtf("Package version code should be available at this point.");
            }
        } else {
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_RANK, si.getRank());
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_FLAGS, si.getFlags());
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_ICON_RES_ID, si.getIconResourceId());
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_ICON_RES_NAME, si.getIconResName());
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_BITMAP_PATH, si.getBitmapPath());
            com.android.server.pm.ShortcutService.writeAttr(out, ATTR_ICON_URI, si.getIconUri());
        }
        if (shouldBackupDetails) {
            java.util.Set<java.lang.String> cat = si.getCategories();
            if (cat != null && cat.size() > 0) {
                out.startTag((java.lang.String) null, "categories");
                com.android.internal.util.XmlUtils.writeStringArrayXml((java.lang.String[]) cat.toArray(new java.lang.String[cat.size()]), "categories", com.android.internal.util.XmlUtils.makeTyped(out));
                out.endTag((java.lang.String) null, "categories");
            }
            if (!forBackup) {
                android.app.Person[] persons = si.getPersons();
                if (!com.android.internal.util.ArrayUtils.isEmpty(persons)) {
                    for (android.app.Person p : persons) {
                        out.startTag((java.lang.String) null, TAG_PERSON);
                        com.android.server.pm.ShortcutService.writeAttr(out, "name", p.getName());
                        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_PERSON_URI, p.getUri());
                        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_PERSON_KEY, p.getKey());
                        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_PERSON_IS_BOT, p.isBot());
                        com.android.server.pm.ShortcutService.writeAttr(out, ATTR_PERSON_IS_IMPORTANT, p.isImportant());
                        out.endTag((java.lang.String) null, TAG_PERSON);
                    }
                }
            }
            android.content.Intent[] intentsNoExtras = si.getIntentsNoExtras();
            android.os.PersistableBundle[] intentsExtras = si.getIntentPersistableExtrases();
            if (intentsNoExtras != null && intentsExtras != null) {
                int numIntents = intentsNoExtras.length;
                for (int i = 0; i < numIntents; i++) {
                    out.startTag((java.lang.String) null, "intent");
                    com.android.server.pm.ShortcutService.writeAttr(out, ATTR_INTENT_NO_EXTRA, intentsNoExtras[i]);
                    com.android.server.pm.ShortcutService.writeTagExtra(out, TAG_EXTRAS, intentsExtras[i]);
                    out.endTag((java.lang.String) null, "intent");
                }
            }
            com.android.server.pm.ShortcutService.writeTagExtra(out, TAG_EXTRAS, si.getExtras());
            java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.util.List<java.lang.String>>> capabilityBindings = si.getCapabilityBindingsInternal();
            if (capabilityBindings != null && !capabilityBindings.isEmpty()) {
                com.android.internal.util.XmlUtils.writeMapXml(capabilityBindings, NAME_CAPABILITY, out);
            }
        }
        out.endTag((java.lang.String) null, TAG_SHORTCUT);
    }

    public static com.android.server.pm.ShortcutPackage loadFromFile(com.android.server.pm.ShortcutService s, com.android.server.pm.ShortcutUser shortcutUser, java.io.File path, boolean fromBackup) {
        com.android.server.pm.ResilientAtomicFile file = getResilientFile(path);
        try {
            try {
                java.io.FileInputStream in = file.openRead();
                if (in == null) {
                    android.util.Slog.d(TAG, "Not found " + path);
                    if (file != null) {
                        file.close();
                        return null;
                    }
                    return null;
                }
                com.android.server.pm.ShortcutPackage ret = null;
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
                while (true) {
                    int type = parser.next();
                    if (type == 1) {
                        break;
                    }
                    if (type == 2) {
                        int depth = parser.getDepth();
                        java.lang.String tag = parser.getName();
                        android.util.Slog.d(TAG, java.lang.String.format("depth=%d type=%d name=%s", java.lang.Integer.valueOf(depth), java.lang.Integer.valueOf(type), tag));
                        if (depth == 1 && "package".equals(tag)) {
                            ret = loadFromXml(s, shortcutUser, parser, fromBackup);
                        } else {
                            com.android.server.pm.ShortcutService.throwForInvalidTag(depth, tag);
                        }
                    }
                }
                if (file != null) {
                    file.close();
                }
                return ret;
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Failed to read file " + file.getBaseFile(), e);
                file.failRead(null, e);
                com.android.server.pm.ShortcutPackage shortcutPackageLoadFromFile = loadFromFile(s, shortcutUser, path, fromBackup);
                if (file != null) {
                    file.close();
                }
                return shortcutPackageLoadFromFile;
            }
        } catch (java.lang.Throwable th) {
            if (file != null) {
                try {
                    file.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:51:0x0098 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:58:0x008c A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00b7 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x008d A[SYNTHETIC] */
    /*  JADX ERROR: UnsupportedOperationException in pass: RegionMakerVisitor
        java.lang.UnsupportedOperationException
        	at java.base/java.util.Collections$UnmodifiableCollection.add(Unknown Source)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker$1.leaveRegion(SwitchRegionMaker.java:390)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:70)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:23)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.insertBreaksForCase(SwitchRegionMaker.java:370)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.insertBreaks(SwitchRegionMaker.java:85)
        	at jadx.core.dex.visitors.regions.PostProcessRegions.leaveRegion(PostProcessRegions.java:33)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:70)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:19)
        	at jadx.core.dex.visitors.regions.PostProcessRegions.process(PostProcessRegions.java:23)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:31)
        */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.android.server.pm.ShortcutPackage loadFromXml(com.android.server.pm.ShortcutService r16, com.android.server.pm.ShortcutUser r17, com.android.modules.utils.TypedXmlPullParser r18, boolean r19) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instruction units count: 226
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ShortcutPackage.loadFromXml(com.android.server.pm.ShortcutService, com.android.server.pm.ShortcutUser, com.android.modules.utils.TypedXmlPullParser, boolean):com.android.server.pm.ShortcutPackage");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0202, code lost:
    
        if (r15 == null) goto L62;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0204, code lost:
    
        android.content.pm.ShortcutInfo.setIntentExtras(r15, r1);
        r2.clear();
        r2.add(r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x020d, code lost:
    
        if (r8 != 0) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0211, code lost:
    
        if ((r10 & 64) == 0) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0213, code lost:
    
        r3 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x0216, code lost:
    
        r3 = r8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x0217, code lost:
    
        if (r67 == false) goto L69;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x0219, code lost:
    
        r57 = r10 | 4096;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x021e, code lost:
    
        r57 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x0220, code lost:
    
        if (r11 != null) goto L72;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x0223, code lost:
    
        r6 = new android.content.LocusId(r11);
     */
    /* JADX WARN: Code restructure failed: missing block: B:73:0x0228, code lost:
    
        r36 = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x028a, code lost:
    
        return new android.content.pm.ShortcutInfo(r66, r7, r65, r39, null, r40, r41, r42, r44, r45, r46, r47, r48, r49, r4, (android.content.Intent[]) r2.toArray(new android.content.Intent[r2.size()]), r17, r56, r50, r57, r16, r52, r53, r54, r3, (android.app.Person[]) r5.toArray(new android.app.Person[r5.size()]), r36, r43, r55);
     */
    /* JADX WARN: Removed duplicated region for block: B:38:0x0153  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static android.content.pm.ShortcutInfo parseShortcut(com.android.modules.utils.TypedXmlPullParser r64, java.lang.String r65, int r66, boolean r67) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            Method dump skipped, instruction units count: 700
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ShortcutPackage.parseShortcut(com.android.modules.utils.TypedXmlPullParser, java.lang.String, int, boolean):android.content.pm.ShortcutInfo");
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x0064, code lost:
    
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static android.content.Intent parseIntent(com.android.modules.utils.TypedXmlPullParser r7) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
            java.lang.String r0 = "intent-base"
            android.content.Intent r0 = com.android.server.pm.ShortcutService.parseIntentAttribute(r7, r0)
            int r1 = r7.getDepth()
        Lb:
            int r2 = r7.next()
            r3 = r2
            r4 = 1
            if (r2 == r4) goto L64
            r2 = 3
            if (r3 != r2) goto L1c
            int r2 = r7.getDepth()
            if (r2 <= r1) goto L64
        L1c:
            r2 = 2
            if (r3 == r2) goto L20
            goto Lb
        L20:
            int r2 = r7.getDepth()
            java.lang.String r4 = r7.getName()
            java.lang.Integer r5 = java.lang.Integer.valueOf(r2)
            java.lang.Integer r6 = java.lang.Integer.valueOf(r3)
            java.lang.Object[] r5 = new java.lang.Object[]{r5, r6, r4}
            java.lang.String r6 = "  depth=%d type=%d name=%s"
            java.lang.String r5 = java.lang.String.format(r6, r5)
            java.lang.String r6 = "ShortcutService"
            android.util.Slog.d(r6, r5)
            int r5 = r4.hashCode()
            switch(r5) {
                case -1289032093: goto L48;
                default: goto L47;
            }
        L47:
            goto L52
        L48:
            java.lang.String r5 = "extras"
            boolean r5 = r4.equals(r5)
            if (r5 == 0) goto L47
            r5 = 0
            goto L53
        L52:
            r5 = -1
        L53:
            switch(r5) {
                case 0: goto L5b;
                default: goto L56;
            }
        L56:
            java.io.IOException r5 = com.android.server.pm.ShortcutService.throwForInvalidTag(r2, r4)
            throw r5
        L5b:
            android.os.PersistableBundle r5 = android.os.PersistableBundle.restoreFromXml(r7)
            android.content.pm.ShortcutInfo.setIntentExtras(r0, r5)
            goto Lb
        L64:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.pm.ShortcutPackage.parseIntent(com.android.modules.utils.TypedXmlPullParser):android.content.Intent");
    }

    private static android.app.Person parsePerson(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.CharSequence name = com.android.server.pm.ShortcutService.parseStringAttribute(parser, "name");
        java.lang.String uri = com.android.server.pm.ShortcutService.parseStringAttribute(parser, ATTR_PERSON_URI);
        java.lang.String key = com.android.server.pm.ShortcutService.parseStringAttribute(parser, ATTR_PERSON_KEY);
        boolean isBot = com.android.server.pm.ShortcutService.parseBooleanAttribute(parser, ATTR_PERSON_IS_BOT);
        boolean isImportant = com.android.server.pm.ShortcutService.parseBooleanAttribute(parser, ATTR_PERSON_IS_IMPORTANT);
        android.app.Person.Builder builder = new android.app.Person.Builder();
        builder.setName(name).setUri(uri).setKey(key).setBot(isBot).setImportant(isImportant);
        return builder.build();
    }

    java.util.List<android.content.pm.ShortcutInfo> getAllShortcutsForTest() {
        java.util.List<android.content.pm.ShortcutInfo> ret = new java.util.ArrayList<>(1);
        java.util.Objects.requireNonNull(ret);
        forEachShortcut(new com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda44(ret));
        return ret;
    }

    java.util.List<com.android.server.pm.ShareTargetInfo> getAllShareTargetsForTest() {
        java.util.ArrayList arrayList;
        synchronized (this.mPackageItemLock) {
            arrayList = new java.util.ArrayList(this.mShareTargets);
        }
        return arrayList;
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    public void verifyStates() {
        super.verifyStates();
        final boolean[] failed = new boolean[1];
        final com.android.server.pm.ShortcutService s = this.mShortcutUser.mService;
        android.util.ArrayMap<android.content.ComponentName, java.util.ArrayList<android.content.pm.ShortcutInfo>> all = sortShortcutsToActivities();
        for (int outer = all.size() - 1; outer >= 0; outer--) {
            java.util.ArrayList<android.content.pm.ShortcutInfo> list = all.valueAt(outer);
            if (list.size() > this.mShortcutUser.mService.getMaxActivityShortcuts()) {
                failed[0] = true;
                android.util.Log.e(TAG_VERIFY, "Package " + getPackageName() + ": activity " + all.keyAt(outer) + " has " + all.valueAt(outer).size() + " shortcuts.");
            }
            java.util.Collections.sort(list, new java.util.Comparator() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda25
                @Override // java.util.Comparator
                public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                    return java.lang.Integer.compare(((android.content.pm.ShortcutInfo) obj).getRank(), ((android.content.pm.ShortcutInfo) obj2).getRank());
                }
            });
            java.util.ArrayList<android.content.pm.ShortcutInfo> dynamicList = new java.util.ArrayList<>(list);
            dynamicList.removeIf(new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda26
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.pm.ShortcutPackage.lambda$verifyStates$34((android.content.pm.ShortcutInfo) obj);
                }
            });
            java.util.ArrayList<android.content.pm.ShortcutInfo> manifestList = new java.util.ArrayList<>(list);
            manifestList.removeIf(new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda27
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.pm.ShortcutPackage.lambda$verifyStates$35((android.content.pm.ShortcutInfo) obj);
                }
            });
            verifyRanksSequential(dynamicList);
            verifyRanksSequential(manifestList);
        }
        forEachShortcut(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda28
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$verifyStates$36(failed, s, (android.content.pm.ShortcutInfo) obj);
            }
        });
        if (failed[0]) {
            throw new java.lang.IllegalStateException("See logcat for errors");
        }
    }

    static /* synthetic */ boolean lambda$verifyStates$34(android.content.pm.ShortcutInfo si) {
        return !si.isDynamic();
    }

    static /* synthetic */ boolean lambda$verifyStates$35(android.content.pm.ShortcutInfo si) {
        return !si.isManifestShortcut();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$verifyStates$36(boolean[] failed, com.android.server.pm.ShortcutService s, android.content.pm.ShortcutInfo si) {
        if (!si.isDeclaredInManifest() && !si.isDynamic() && !si.isPinned() && !si.isCached()) {
            failed[0] = true;
            android.util.Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + si.getId() + " is not manifest, dynamic or pinned.");
        }
        if (si.isDeclaredInManifest() && si.isDynamic()) {
            failed[0] = true;
            android.util.Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + si.getId() + " is both dynamic and manifest at the same time.");
        }
        if (si.getActivity() == null && !si.isFloating()) {
            failed[0] = true;
            android.util.Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + si.getId() + " has null activity, but not floating.");
        }
        if ((si.isDynamic() || si.isManifestShortcut()) && !si.isEnabled()) {
            failed[0] = true;
            android.util.Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + si.getId() + " is not floating, but is disabled.");
        }
        if (si.isFloating() && si.getRank() != 0) {
            failed[0] = true;
            android.util.Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + si.getId() + " is floating, but has rank=" + si.getRank());
        }
        if (si.getIcon() != null) {
            failed[0] = true;
            android.util.Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + si.getId() + " still has an icon");
        }
        if (si.hasAdaptiveBitmap() && !si.hasIconFile() && !si.hasIconUri()) {
            failed[0] = true;
            android.util.Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + si.getId() + " has adaptive bitmap but was not saved to a file nor has icon uri.");
        }
        if (si.hasIconFile() && si.hasIconResource()) {
            failed[0] = true;
            android.util.Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + si.getId() + " has both resource and bitmap icons");
        }
        if (si.hasIconFile() && si.hasIconUri()) {
            failed[0] = true;
            android.util.Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + si.getId() + " has both url and bitmap icons");
        }
        if (si.hasIconUri() && si.hasIconResource()) {
            failed[0] = true;
            android.util.Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + si.getId() + " has both url and resource icons");
        }
        if (si.isEnabled() != (si.getDisabledReason() == 0)) {
            failed[0] = true;
            android.util.Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + si.getId() + " isEnabled() and getDisabledReason() disagree: " + si.isEnabled() + " vs " + si.getDisabledReason());
        }
        if (si.getDisabledReason() == 100 && getPackageInfo().getBackupSourceVersionCode() == -1) {
            failed[0] = true;
            android.util.Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + si.getId() + " RESTORED_VERSION_LOWER with no backup source version code.");
        }
        if (s.isDummyMainActivity(si.getActivity())) {
            failed[0] = true;
            android.util.Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + si.getId() + " has a dummy target activity");
        }
    }

    void mutateShortcut(java.lang.String id, android.content.pm.ShortcutInfo shortcut, java.util.function.Consumer<android.content.pm.ShortcutInfo> transform) {
        java.util.Objects.requireNonNull(id);
        java.util.Objects.requireNonNull(transform);
        synchronized (this.mPackageItemLock) {
            if (shortcut != null) {
                transform.accept(shortcut);
            }
            android.content.pm.ShortcutInfo si = findShortcutById(id);
            if (si == null) {
                return;
            }
            transform.accept(si);
            saveShortcut(si);
        }
    }

    private void saveShortcut(android.content.pm.ShortcutInfo... shortcuts) {
        java.util.Objects.requireNonNull(shortcuts);
        saveShortcut(java.util.Arrays.asList(shortcuts));
    }

    private void saveShortcut(java.util.Collection<android.content.pm.ShortcutInfo> shortcuts) {
        java.util.Objects.requireNonNull(shortcuts);
        synchronized (this.mPackageItemLock) {
            for (android.content.pm.ShortcutInfo si : shortcuts) {
                this.mShortcuts.put(si.getId(), si);
            }
        }
    }

    java.util.List<android.content.pm.ShortcutInfo> findAll(java.util.Collection<java.lang.String> ids) {
        java.util.List<android.content.pm.ShortcutInfo> list;
        synchronized (this.mPackageItemLock) {
            java.util.stream.Stream<java.lang.String> stream = ids.stream();
            final android.util.ArrayMap<java.lang.String, android.content.pm.ShortcutInfo> arrayMap = this.mShortcuts;
            java.util.Objects.requireNonNull(arrayMap);
            list = (java.util.List) stream.map(new java.util.function.Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda4
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return (android.content.pm.ShortcutInfo) arrayMap.get((java.lang.String) obj);
                }
            }).filter(new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda5
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return java.util.Objects.nonNull((android.content.pm.ShortcutInfo) obj);
                }
            }).collect(java.util.stream.Collectors.toList());
        }
        return list;
    }

    private void forEachShortcut(final java.util.function.Consumer<android.content.pm.ShortcutInfo> cb) {
        forEachShortcutStopWhen(new java.util.function.Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda54
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return com.android.server.pm.ShortcutPackage.lambda$forEachShortcut$37(cb, (android.content.pm.ShortcutInfo) obj);
            }
        });
    }

    static /* synthetic */ java.lang.Boolean lambda$forEachShortcut$37(java.util.function.Consumer cb, android.content.pm.ShortcutInfo si) {
        cb.accept(si);
        return false;
    }

    private void forEachShortcutMutate(java.util.function.Consumer<android.content.pm.ShortcutInfo> cb) {
        for (int i = this.mShortcuts.size() - 1; i >= 0; i--) {
            android.content.pm.ShortcutInfo si = this.mShortcuts.valueAt(i);
            cb.accept(si);
        }
    }

    private void forEachShortcutStopWhen(java.util.function.Function<android.content.pm.ShortcutInfo, java.lang.Boolean> cb) {
        synchronized (this.mPackageItemLock) {
            for (int i = this.mShortcuts.size() - 1; i >= 0; i--) {
                android.content.pm.ShortcutInfo si = this.mShortcuts.valueAt(i);
                if (cb.apply(si).booleanValue()) {
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public com.android.internal.infra.AndroidFuture<android.app.appsearch.AppSearchSession> setupSchema(final android.app.appsearch.AppSearchSession session) {
        android.util.Slog.d(TAG, "Setup Schema for user=" + this.mShortcutUser.getUserId() + " pkg=" + getPackageName());
        android.app.appsearch.SetSchemaRequest.Builder schemaBuilder = new android.app.appsearch.SetSchemaRequest.Builder().addSchemas(android.content.pm.AppSearchShortcutPerson.SCHEMA, android.content.pm.AppSearchShortcutInfo.SCHEMA).setForceOverride(true).addRequiredPermissionsForSchemaTypeVisibility("Shortcut", java.util.Collections.singleton(5)).addRequiredPermissionsForSchemaTypeVisibility("Shortcut", java.util.Collections.singleton(6)).addRequiredPermissionsForSchemaTypeVisibility("ShortcutPerson", java.util.Collections.singleton(5)).addRequiredPermissionsForSchemaTypeVisibility("ShortcutPerson", java.util.Collections.singleton(6));
        final com.android.internal.infra.AndroidFuture<android.app.appsearch.AppSearchSession> future = new com.android.internal.infra.AndroidFuture<>();
        session.setSchema(schemaBuilder.build(), this.mExecutor, this.mShortcutUser.mExecutor, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda45
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.ShortcutPackage.lambda$setupSchema$38(future, session, (android.app.appsearch.AppSearchResult) obj);
            }
        });
        return future;
    }

    static /* synthetic */ void lambda$setupSchema$38(com.android.internal.infra.AndroidFuture future, android.app.appsearch.AppSearchSession session, android.app.appsearch.AppSearchResult result) {
        if (!result.isSuccess()) {
            future.completeExceptionally(new java.lang.IllegalArgumentException(result.getErrorMessage()));
        } else {
            future.complete(session);
        }
    }

    private android.app.appsearch.SearchSpec getSearchSpec() {
        return new android.app.appsearch.SearchSpec.Builder().addFilterSchemas("Shortcut").addFilterNamespaces(getPackageName()).setTermMatch(1).setResultCountPerPage(this.mShortcutUser.mService.getMaxActivityShortcuts()).build();
    }

    private boolean verifyRanksSequential(java.util.List<android.content.pm.ShortcutInfo> list) {
        boolean failed = false;
        for (int i = 0; i < list.size(); i++) {
            android.content.pm.ShortcutInfo si = list.get(i);
            if (si.getRank() != i) {
                failed = true;
                android.util.Log.e(TAG_VERIFY, "Package " + getPackageName() + ": shortcut " + si.getId() + " rank=" + si.getRank() + " but expected to be " + i);
            }
        }
        return failed;
    }

    void removeAllShortcutsAsync() {
        if (!isAppSearchEnabled()) {
            return;
        }
        runAsSystem(new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda12
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$removeAllShortcutsAsync$41();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeAllShortcutsAsync$41() {
        fromAppSearch().thenAccept(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda3
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$removeAllShortcutsAsync$40((android.app.appsearch.AppSearchSession) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeAllShortcutsAsync$40(android.app.appsearch.AppSearchSession session) {
        session.remove("", getSearchSpec(), this.mShortcutUser.mExecutor, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda53
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.pm.ShortcutPackage.lambda$removeAllShortcutsAsync$39((android.app.appsearch.AppSearchResult) obj);
            }
        });
    }

    static /* synthetic */ void lambda$removeAllShortcutsAsync$39(android.app.appsearch.AppSearchResult result) {
        if (!result.isSuccess()) {
            android.util.Slog.e(TAG, "Failed to remove shortcuts from AppSearch. " + result.getErrorMessage());
        }
    }

    void getShortcutByIdsAsync(final java.util.Set<java.lang.String> ids, final java.util.function.Consumer<java.util.List<android.content.pm.ShortcutInfo>> cb) {
        if (!isAppSearchEnabled()) {
            cb.accept(java.util.Collections.emptyList());
        } else {
            runAsSystem(new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda29
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$getShortcutByIdsAsync$43(ids, cb);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getShortcutByIdsAsync$43(final java.util.Set ids, final java.util.function.Consumer cb) {
        fromAppSearch().thenAccept(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda17
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$getShortcutByIdsAsync$42(ids, cb, (android.app.appsearch.AppSearchSession) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getShortcutByIdsAsync$42(java.util.Set ids, java.util.function.Consumer cb, android.app.appsearch.AppSearchSession session) {
        session.getByDocumentId(new android.app.appsearch.GetByDocumentIdRequest.Builder(getPackageName()).addIds(ids).build(), this.mShortcutUser.mExecutor, new com.android.server.pm.ShortcutPackage.AnonymousClass1(cb));
    }

    /* JADX INFO: renamed from: com.android.server.pm.ShortcutPackage$1, reason: invalid class name */
    class AnonymousClass1 implements android.app.appsearch.BatchResultCallback<java.lang.String, android.app.appsearch.GenericDocument> {
        final /* synthetic */ java.util.function.Consumer val$cb;

        AnonymousClass1(java.util.function.Consumer consumer) {
            this.val$cb = consumer;
        }

        @Override // android.app.appsearch.BatchResultCallback
        public void onResult(android.app.appsearch.AppSearchBatchResult<java.lang.String, android.app.appsearch.GenericDocument> result) {
            java.util.List<android.content.pm.ShortcutInfo> ret = (java.util.List) result.getSuccesses().values().stream().map(new java.util.function.Function() { // from class: com.android.server.pm.ShortcutPackage$1$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return this.f$0.lambda$onResult$0((android.app.appsearch.GenericDocument) obj);
                }
            }).collect(java.util.stream.Collectors.toList());
            this.val$cb.accept(ret);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ android.content.pm.ShortcutInfo lambda$onResult$0(android.app.appsearch.GenericDocument doc) {
            return android.content.pm.ShortcutInfo.createFromGenericDocument(com.android.server.pm.ShortcutPackage.this.mShortcutUser.getUserId(), doc);
        }

        @Override // android.app.appsearch.BatchResultCallback
        public void onSystemError(java.lang.Throwable throwable) {
            android.util.Slog.d(com.android.server.pm.ShortcutPackage.TAG, "Error retrieving shortcuts", throwable);
        }
    }

    private void removeShortcutAsync(java.lang.String... id) {
        java.util.Objects.requireNonNull(id);
        removeShortcutAsync(java.util.Arrays.asList(id));
    }

    private void removeShortcutAsync(final java.util.Collection<java.lang.String> ids) {
        if (!isAppSearchEnabled()) {
            return;
        }
        runAsSystem(new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda41
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$removeShortcutAsync$45(ids);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeShortcutAsync$45(final java.util.Collection ids) {
        fromAppSearch().thenAccept(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda31
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$removeShortcutAsync$44(ids, (android.app.appsearch.AppSearchSession) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$removeShortcutAsync$44(java.util.Collection ids, android.app.appsearch.AppSearchSession session) {
        session.remove(new android.app.appsearch.RemoveByDocumentIdRequest.Builder(getPackageName()).addIds((java.util.Collection<java.lang.String>) ids).build(), this.mShortcutUser.mExecutor, new android.app.appsearch.BatchResultCallback<java.lang.String, java.lang.Void>() { // from class: com.android.server.pm.ShortcutPackage.2
            @Override // android.app.appsearch.BatchResultCallback
            public void onResult(android.app.appsearch.AppSearchBatchResult<java.lang.String, java.lang.Void> result) {
                if (!result.isSuccess()) {
                    java.util.Map<java.lang.String, android.app.appsearch.AppSearchResult<java.lang.Void>> failures = result.getFailures();
                    for (java.lang.String key : failures.keySet()) {
                        android.util.Slog.e(com.android.server.pm.ShortcutPackage.TAG, "Failed deleting " + key + ", error message:" + failures.get(key).getErrorMessage());
                    }
                }
            }

            @Override // android.app.appsearch.BatchResultCallback
            public void onSystemError(java.lang.Throwable throwable) {
                android.util.Slog.e(com.android.server.pm.ShortcutPackage.TAG, "Error removing shortcuts", throwable);
            }
        });
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    void scheduleSaveToAppSearchLocked() {
        java.util.Map<java.lang.String, android.content.pm.ShortcutInfo> copy = new android.util.ArrayMap<>(this.mShortcuts);
        if (!this.mTransientShortcuts.isEmpty()) {
            copy.putAll(this.mTransientShortcuts);
            this.mTransientShortcuts.clear();
        }
        saveShortcutsAsync((java.util.Collection) copy.values().stream().filter(new java.util.function.Predicate() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((android.content.pm.ShortcutInfo) obj).usesQuota();
            }
        }).collect(java.util.stream.Collectors.toList()));
    }

    private void saveShortcutsAsync(final java.util.Collection<android.content.pm.ShortcutInfo> shortcuts) {
        java.util.Objects.requireNonNull(shortcuts);
        if (!isAppSearchEnabled() || shortcuts.isEmpty()) {
            return;
        }
        android.util.Slog.d(TAG, "Saving shortcuts async for user=" + this.mShortcutUser.getUserId() + " pkg=" + getPackageName() + " ids=" + ((java.lang.String) shortcuts.stream().map(new com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda34()).collect(java.util.stream.Collectors.joining(",", "[", "]"))));
        runAsSystem(new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda35
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$saveShortcutsAsync$47(shortcuts);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveShortcutsAsync$47(final java.util.Collection shortcuts) {
        fromAppSearch().thenAccept(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda15
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$saveShortcutsAsync$46(shortcuts, (android.app.appsearch.AppSearchSession) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveShortcutsAsync$46(java.util.Collection shortcuts, android.app.appsearch.AppSearchSession session) {
        if (shortcuts.isEmpty()) {
            return;
        }
        session.put(new android.app.appsearch.PutDocumentsRequest.Builder().addGenericDocuments(android.content.pm.AppSearchShortcutInfo.toGenericDocuments(shortcuts)).build(), this.mShortcutUser.mExecutor, new android.app.appsearch.BatchResultCallback<java.lang.String, java.lang.Void>() { // from class: com.android.server.pm.ShortcutPackage.3
            @Override // android.app.appsearch.BatchResultCallback
            public void onResult(android.app.appsearch.AppSearchBatchResult<java.lang.String, java.lang.Void> result) {
                if (!result.isSuccess()) {
                    for (android.app.appsearch.AppSearchResult<java.lang.Void> k : result.getFailures().values()) {
                        android.util.Slog.e(com.android.server.pm.ShortcutPackage.TAG, k.getErrorMessage());
                    }
                }
            }

            @Override // android.app.appsearch.BatchResultCallback
            public void onSystemError(java.lang.Throwable throwable) {
                android.util.Slog.d(com.android.server.pm.ShortcutPackage.TAG, "Error persisting shortcuts", throwable);
            }
        });
    }

    void getTopShortcutsFromPersistence(final com.android.internal.infra.AndroidFuture<java.util.List<android.content.pm.ShortcutInfo>> cb) {
        if (!isAppSearchEnabled()) {
            cb.complete((java.lang.Object) null);
        }
        runAsSystem(new java.lang.Runnable() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$getTopShortcutsFromPersistence$51(cb);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getTopShortcutsFromPersistence$51(final com.android.internal.infra.AndroidFuture cb) {
        fromAppSearch().thenAccept(new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda60
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$getTopShortcutsFromPersistence$50(cb, (android.app.appsearch.AppSearchSession) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getTopShortcutsFromPersistence$50(final com.android.internal.infra.AndroidFuture cb, android.app.appsearch.AppSearchSession session) {
        android.app.appsearch.SearchResults res = session.search("", getSearchSpec());
        res.getNextPage(this.mShortcutUser.mExecutor, new java.util.function.Consumer() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda30
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                this.f$0.lambda$getTopShortcutsFromPersistence$49(cb, (android.app.appsearch.AppSearchResult) obj);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getTopShortcutsFromPersistence$49(com.android.internal.infra.AndroidFuture cb, android.app.appsearch.AppSearchResult results) {
        if (!results.isSuccess()) {
            cb.completeExceptionally(new java.lang.IllegalStateException(results.getErrorMessage()));
        } else {
            cb.complete((java.util.List) ((java.util.List) results.getResultValue()).stream().map(new java.util.function.Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda51
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((android.app.appsearch.SearchResult) obj).getGenericDocument();
                }
            }).map(new java.util.function.Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda52
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return this.f$0.lambda$getTopShortcutsFromPersistence$48((android.app.appsearch.GenericDocument) obj);
                }
            }).collect(java.util.stream.Collectors.toList()));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.content.pm.ShortcutInfo lambda$getTopShortcutsFromPersistence$48(android.app.appsearch.GenericDocument doc) {
        return android.content.pm.ShortcutInfo.createFromGenericDocument(this.mShortcutUser.getUserId(), doc);
    }

    private com.android.internal.infra.AndroidFuture<android.app.appsearch.AppSearchSession> fromAppSearch() {
        android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.getThreadPolicy();
        android.app.appsearch.AppSearchManager.SearchContext searchContext = new android.app.appsearch.AppSearchManager.SearchContext.Builder(getPackageName()).build();
        com.android.internal.infra.AndroidFuture<android.app.appsearch.AppSearchSession> future = null;
        try {
            try {
                android.os.StrictMode.setThreadPolicy(new android.os.StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
                future = this.mShortcutUser.getAppSearch(searchContext);
                synchronized (this.mPackageItemLock) {
                    if (!this.mIsAppSearchSchemaUpToDate) {
                        future = future.thenCompose(new java.util.function.Function() { // from class: com.android.server.pm.ShortcutPackage$$ExternalSyntheticLambda6
                            @Override // java.util.function.Function
                            public final java.lang.Object apply(java.lang.Object obj) {
                                return this.f$0.setupSchema((android.app.appsearch.AppSearchSession) obj);
                            }
                        });
                    }
                    this.mIsAppSearchSchemaUpToDate = true;
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.e(TAG, "Failed to create app search session. pkg=" + getPackageName() + " user=" + this.mShortcutUser.getUserId(), e);
                ((com.android.internal.infra.AndroidFuture) java.util.Objects.requireNonNull(future)).completeExceptionally(e);
            }
            return (com.android.internal.infra.AndroidFuture) java.util.Objects.requireNonNull(future);
        } finally {
            android.os.StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void runAsSystem(java.lang.Runnable fn) {
        long callingIdentity = android.os.Binder.clearCallingIdentity();
        try {
            fn.run();
        } finally {
            android.os.Binder.restoreCallingIdentity(callingIdentity);
        }
    }

    @Override // com.android.server.pm.ShortcutPackageItem
    protected java.io.File getShortcutPackageItemFile() {
        java.io.File path = new java.io.File(this.mShortcutUser.mService.injectUserDataPath(this.mShortcutUser.getUserId()), "packages");
        java.lang.String fileName = getPackageName() + ".xml";
        return new java.io.File(path, fileName);
    }
}
