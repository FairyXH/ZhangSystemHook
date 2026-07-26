package com.android.server.om;

/* JADX INFO: loaded from: classes2.dex */
final class OverlayManagerServiceImpl {
    private static final int FLAG_OVERLAY_IS_BEING_REPLACED = 2;
    private static final int FLAG_SYSTEM_UPDATE_UNINSTALL = 4;

    @java.lang.Deprecated
    private static final int FLAG_TARGET_IS_BEING_REPLACED = 1;
    private final java.lang.String[] mDefaultOverlays;
    private final com.android.server.om.IdmapManager mIdmapManager;
    private final com.android.internal.content.om.OverlayConfig mOverlayConfig;
    private final com.android.server.om.PackageManagerHelper mPackageManager;
    private final com.android.server.om.OverlayManagerSettings mSettings;

    private boolean mustReinitializeOverlay(com.android.server.pm.pkg.AndroidPackage theTruth, android.content.om.OverlayInfo oldSettings) {
        boolean isMutable;
        if (oldSettings == null || !java.util.Objects.equals(theTruth.getOverlayTarget(), oldSettings.targetPackageName) || !java.util.Objects.equals(theTruth.getOverlayTargetOverlayableName(), oldSettings.targetOverlayableName) || oldSettings.isFabricated || (isMutable = isPackageConfiguredMutable(theTruth)) != oldSettings.isMutable) {
            return true;
        }
        if (!isMutable && isPackageConfiguredEnabled(theTruth) != oldSettings.isEnabled()) {
            return true;
        }
        return false;
    }

    private boolean mustReinitializeOverlay(android.os.FabricatedOverlayInfo theTruth, android.content.om.OverlayInfo oldSettings) {
        if (oldSettings == null || !java.util.Objects.equals(theTruth.targetPackageName, oldSettings.targetPackageName) || !java.util.Objects.equals(theTruth.targetOverlayable, oldSettings.targetOverlayableName)) {
            return true;
        }
        return false;
    }

    OverlayManagerServiceImpl(com.android.server.om.PackageManagerHelper packageManager, com.android.server.om.IdmapManager idmapManager, com.android.server.om.OverlayManagerSettings settings, com.android.internal.content.om.OverlayConfig overlayConfig, java.lang.String[] defaultOverlays) {
        this.mPackageManager = packageManager;
        this.mIdmapManager = idmapManager;
        this.mSettings = settings;
        this.mOverlayConfig = overlayConfig;
        this.mDefaultOverlays = defaultOverlays;
    }

    android.util.ArraySet<android.content.pm.UserPackage> updateOverlaysForUser(int newUserId) {
        int i;
        android.util.ArrayMap<java.lang.String, com.android.server.pm.pkg.PackageState> userPackages;
        android.util.ArraySet<java.lang.String> overlaidByOthers;
        if (com.android.server.om.OverlayManagerService.DEBUG) {
            android.util.Slog.d("OverlayManager", "updateOverlaysForUser newUserId=" + newUserId);
        }
        android.util.ArraySet<android.content.pm.UserPackage> updatedTargets = new android.util.ArraySet<>();
        final android.util.ArrayMap<java.lang.String, com.android.server.pm.pkg.PackageState> userPackages2 = this.mPackageManager.initializeForUser(newUserId);
        com.android.internal.util.CollectionUtils.addAll(updatedTargets, removeOverlaysForUser(new java.util.function.Predicate() { // from class: com.android.server.om.OverlayManagerServiceImpl$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.om.OverlayManagerServiceImpl.lambda$updateOverlaysForUser$0(userPackages2, (android.content.om.OverlayInfo) obj);
            }
        }, newUserId));
        android.util.ArraySet<java.lang.String> overlaidByOthers2 = new android.util.ArraySet<>();
        java.util.Iterator<com.android.server.pm.pkg.PackageState> it = userPackages2.values().iterator();
        while (it.hasNext()) {
            com.android.server.pm.pkg.AndroidPackage pkg = it.next().getAndroidPackage();
            java.lang.String overlayTarget = pkg == null ? null : pkg.getOverlayTarget();
            if (!android.text.TextUtils.isEmpty(overlayTarget)) {
                overlaidByOthers2.add(overlayTarget);
            }
        }
        int n = userPackages2.size();
        int i2 = 0;
        while (true) {
            i = 0;
            if (i2 >= n) {
                break;
            }
            com.android.server.pm.pkg.PackageState packageState = userPackages2.valueAt(i2);
            com.android.server.pm.pkg.AndroidPackage pkg2 = packageState.getAndroidPackage();
            if (pkg2 != null) {
                java.lang.String packageName = packageState.getPackageName();
                try {
                    com.android.internal.util.CollectionUtils.addAll(updatedTargets, updatePackageOverlays(pkg2, newUserId, 0));
                    if (overlaidByOthers2.contains(packageName)) {
                        updatedTargets.add(android.content.pm.UserPackage.of(newUserId, packageName));
                    }
                } catch (com.android.server.om.OverlayManagerServiceImpl.OperationFailedException e) {
                    android.util.Slog.e("OverlayManager", "failed to initialize overlays of '" + packageName + "' for user " + newUserId + "", e);
                }
            }
            i2++;
        }
        for (android.os.FabricatedOverlayInfo info : getFabricatedOverlayInfos()) {
            try {
                com.android.internal.util.CollectionUtils.addAll(updatedTargets, registerFabricatedOverlay(info, newUserId));
            } catch (com.android.server.om.OverlayManagerServiceImpl.OperationFailedException e2) {
                android.util.Slog.e("OverlayManager", "failed to initialize fabricated overlay of '" + info.path + "' for user " + newUserId + "", e2);
            }
        }
        android.util.ArraySet<java.lang.String> enabledCategories = new android.util.ArraySet<>();
        android.util.ArrayMap<java.lang.String, java.util.List<android.content.om.OverlayInfo>> userOverlays = this.mSettings.getOverlaysForUser(newUserId);
        int userOverlayTargetCount = userOverlays.size();
        int i3 = 0;
        while (i3 < userOverlayTargetCount) {
            java.util.List<android.content.om.OverlayInfo> overlayList = userOverlays.valueAt(i3);
            int overlayCount = overlayList != null ? overlayList.size() : i;
            for (int j = 0; j < overlayCount; j++) {
                android.content.om.OverlayInfo oi = overlayList.get(j);
                if (oi.isEnabled()) {
                    enabledCategories.add(oi.category);
                }
            }
            i3++;
            i = 0;
        }
        java.lang.String[] strArr = this.mDefaultOverlays;
        int length = strArr.length;
        int i4 = 0;
        while (i4 < length) {
            java.lang.String defaultOverlay = strArr[i4];
            try {
                android.content.om.OverlayIdentifier overlay = new android.content.om.OverlayIdentifier(defaultOverlay);
                android.content.om.OverlayInfo oi2 = this.mSettings.getOverlayInfo(overlay, newUserId);
                userPackages = userPackages2;
                try {
                    if (enabledCategories.contains(oi2.category)) {
                        overlaidByOthers = overlaidByOthers2;
                    } else {
                        overlaidByOthers = overlaidByOthers2;
                        try {
                            android.util.Slog.w("OverlayManager", "Enabling default overlay '" + defaultOverlay + "' for target '" + oi2.targetPackageName + "' in category '" + oi2.category + "' for user " + newUserId);
                            this.mSettings.setEnabled(overlay, newUserId, true);
                            if (updateState(oi2, newUserId, 0)) {
                                com.android.internal.util.CollectionUtils.add(updatedTargets, android.content.pm.UserPackage.of(oi2.userId, oi2.targetPackageName));
                            }
                        } catch (com.android.server.om.OverlayManagerSettings.BadKeyException e3) {
                            e = e3;
                            android.util.Slog.e("OverlayManager", "Failed to set default overlay '" + defaultOverlay + "' for user " + newUserId, e);
                        }
                    }
                } catch (com.android.server.om.OverlayManagerSettings.BadKeyException e4) {
                    e = e4;
                    overlaidByOthers = overlaidByOthers2;
                }
            } catch (com.android.server.om.OverlayManagerSettings.BadKeyException e5) {
                e = e5;
                userPackages = userPackages2;
                overlaidByOthers = overlaidByOthers2;
            }
            i4++;
            userPackages2 = userPackages;
            overlaidByOthers2 = overlaidByOthers;
        }
        cleanStaleResourceCache();
        return updatedTargets;
    }

    static /* synthetic */ boolean lambda$updateOverlaysForUser$0(android.util.ArrayMap userPackages, android.content.om.OverlayInfo info) {
        return !userPackages.containsKey(info.packageName);
    }

    void onUserRemoved(int userId) {
        if (com.android.server.om.OverlayManagerService.DEBUG) {
            android.util.Slog.d("OverlayManager", "onUserRemoved userId=" + userId);
        }
        this.mSettings.removeUser(userId);
    }

    java.util.Set<android.content.pm.UserPackage> onPackageAdded(java.lang.String pkgName, int userId) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
        java.util.Set<android.content.pm.UserPackage> updatedTargets = new android.util.ArraySet<>();
        updatedTargets.add(android.content.pm.UserPackage.of(userId, pkgName));
        updatedTargets.addAll(reconcileSettingsForPackage(pkgName, userId, 0));
        return updatedTargets;
    }

    java.util.Set<android.content.pm.UserPackage> onPackageChanged(java.lang.String pkgName, int userId) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
        return reconcileSettingsForPackage(pkgName, userId, 0);
    }

    java.util.Set<android.content.pm.UserPackage> onPackageReplacing(java.lang.String pkgName, boolean systemUpdateUninstall, int userId) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
        int flags = 2;
        if (systemUpdateUninstall) {
            flags = 2 | 4;
        }
        return reconcileSettingsForPackage(pkgName, userId, flags);
    }

    java.util.Set<android.content.pm.UserPackage> onPackageReplaced(java.lang.String pkgName, int userId) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
        return reconcileSettingsForPackage(pkgName, userId, 0);
    }

    java.util.Set<android.content.pm.UserPackage> onPackageRemoved(final java.lang.String pkgName, int userId) {
        if (com.android.server.om.OverlayManagerService.DEBUG) {
            android.util.Slog.d("OverlayManager", "onPackageRemoved pkgName=" + pkgName + " userId=" + userId);
        }
        java.util.Set<android.content.pm.UserPackage> targets = updateOverlaysForTarget(pkgName, userId, 0);
        return com.android.internal.util.CollectionUtils.addAll(targets, removeOverlaysForUser(new java.util.function.Predicate() { // from class: com.android.server.om.OverlayManagerServiceImpl$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return pkgName.equals(((android.content.om.OverlayInfo) obj).packageName);
            }
        }, userId));
    }

    private java.util.Set<android.content.pm.UserPackage> removeOverlaysForUser(final java.util.function.Predicate<android.content.om.OverlayInfo> condition, final int userId) {
        java.util.List<android.content.om.OverlayInfo> overlays = this.mSettings.removeIf(new java.util.function.Predicate() { // from class: com.android.server.om.OverlayManagerServiceImpl$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.om.OverlayManagerServiceImpl.lambda$removeOverlaysForUser$2(userId, condition, (android.content.om.OverlayInfo) obj);
            }
        });
        java.util.Set<android.content.pm.UserPackage> targets = java.util.Collections.emptySet();
        int n = overlays.size();
        for (int i = 0; i < n; i++) {
            android.content.om.OverlayInfo info = overlays.get(i);
            targets = com.android.internal.util.CollectionUtils.add(targets, android.content.pm.UserPackage.of(userId, info.targetPackageName));
            removeIdmapIfPossible(info);
        }
        return targets;
    }

    static /* synthetic */ boolean lambda$removeOverlaysForUser$2(int userId, java.util.function.Predicate condition, android.content.om.OverlayInfo io) {
        return userId == io.userId && condition.test(io);
    }

    private java.util.Set<android.content.pm.UserPackage> updateOverlaysForTarget(java.lang.String targetPackage, int userId, int flags) {
        boolean modified = false;
        java.util.List<android.content.om.OverlayInfo> overlays = this.mSettings.getOverlaysForTarget(targetPackage, userId);
        int n = overlays.size();
        for (int i = 0; i < n; i++) {
            android.content.om.OverlayInfo oi = overlays.get(i);
            try {
                modified |= updateState(oi, userId, flags);
            } catch (com.android.server.om.OverlayManagerSettings.BadKeyException e) {
                android.util.Slog.e("OverlayManager", "failed to update settings", e);
                modified |= this.mSettings.remove(oi.getOverlayIdentifier(), userId);
            }
        }
        if (!modified) {
            return java.util.Collections.emptySet();
        }
        return java.util.Set.of(android.content.pm.UserPackage.of(userId, targetPackage));
    }

    private java.util.Set<android.content.pm.UserPackage> updatePackageOverlays(com.android.server.pm.pkg.AndroidPackage pkg, int userId, int flags) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
        java.util.Set<android.content.pm.UserPackage> updatedTargets;
        if (pkg.getOverlayTarget() == null) {
            return java.util.Collections.emptySet();
        }
        java.util.Set<android.content.pm.UserPackage> updatedTargets2 = java.util.Collections.emptySet();
        android.content.om.OverlayIdentifier overlay = new android.content.om.OverlayIdentifier(pkg.getPackageName());
        int priority = getPackageConfiguredPriority(pkg);
        try {
            android.content.om.CriticalOverlayInfo nullableOverlayInfo = this.mSettings.getNullableOverlayInfo(overlay, userId);
            if (mustReinitializeOverlay(pkg, (android.content.om.OverlayInfo) nullableOverlayInfo)) {
                if (nullableOverlayInfo == null) {
                    updatedTargets = updatedTargets2;
                } else {
                    updatedTargets = com.android.internal.util.CollectionUtils.add(updatedTargets2, android.content.pm.UserPackage.of(userId, ((android.content.om.OverlayInfo) nullableOverlayInfo).targetPackageName));
                }
                try {
                    nullableOverlayInfo = this.mSettings.init(overlay, userId, pkg.getOverlayTarget(), pkg.getOverlayTargetOverlayableName(), ((com.android.server.pm.pkg.AndroidPackageSplit) pkg.getSplits().get(0)).getPath(), isPackageConfiguredMutable(pkg), isPackageConfiguredEnabled(pkg), getPackageConfiguredPriority(pkg), pkg.getOverlayCategory(), false);
                    updatedTargets2 = updatedTargets;
                } catch (com.android.server.om.OverlayManagerSettings.BadKeyException e) {
                    e = e;
                    throw new com.android.server.om.OverlayManagerServiceImpl.OperationFailedException("failed to update settings", e);
                }
            } else if (priority != ((android.content.om.OverlayInfo) nullableOverlayInfo).priority) {
                this.mSettings.setPriority(overlay, userId, priority);
                updatedTargets2 = com.android.internal.util.CollectionUtils.add(updatedTargets2, android.content.pm.UserPackage.of(userId, ((android.content.om.OverlayInfo) nullableOverlayInfo).targetPackageName));
            }
            try {
                if (updateState(nullableOverlayInfo, userId, flags)) {
                    return com.android.internal.util.CollectionUtils.add(updatedTargets2, android.content.pm.UserPackage.of(userId, ((android.content.om.OverlayInfo) nullableOverlayInfo).targetPackageName));
                }
                return updatedTargets2;
            } catch (com.android.server.om.OverlayManagerSettings.BadKeyException e2) {
                e = e2;
                throw new com.android.server.om.OverlayManagerServiceImpl.OperationFailedException("failed to update settings", e);
            }
        } catch (com.android.server.om.OverlayManagerSettings.BadKeyException e3) {
            e = e3;
        }
    }

    private java.util.Set<android.content.pm.UserPackage> reconcileSettingsForPackage(java.lang.String pkgName, int userId, int flags) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
        if (com.android.server.om.OverlayManagerService.DEBUG) {
            android.util.Slog.d("OverlayManager", "reconcileSettingsForPackage pkgName=" + pkgName + " userId=" + userId);
        }
        java.util.Set<android.content.pm.UserPackage> updatedTargets = java.util.Collections.emptySet();
        java.util.Set<android.content.pm.UserPackage> updatedTargets2 = com.android.internal.util.CollectionUtils.addAll(updatedTargets, updateOverlaysForTarget(pkgName, userId, flags));
        com.android.server.pm.pkg.PackageState packageState = this.mPackageManager.getPackageStateForUser(pkgName, userId);
        com.android.server.pm.pkg.AndroidPackage pkg = packageState == null ? null : packageState.getAndroidPackage();
        if (pkg == null) {
            return onPackageRemoved(pkgName, userId);
        }
        return com.android.internal.util.CollectionUtils.addAll(updatedTargets2, updatePackageOverlays(pkg, userId, flags));
    }

    android.content.om.OverlayInfo getOverlayInfo(android.content.om.OverlayIdentifier packageName, int userId) {
        try {
            return this.mSettings.getOverlayInfo(packageName, userId);
        } catch (com.android.server.om.OverlayManagerSettings.BadKeyException e) {
            return null;
        }
    }

    java.util.List<android.content.om.OverlayInfo> getOverlayInfosForTarget(java.lang.String targetPackageName, int userId) {
        return this.mSettings.getOverlaysForTarget(targetPackageName, userId);
    }

    java.util.Map<java.lang.String, java.util.List<android.content.om.OverlayInfo>> getOverlaysForUser(int userId) {
        return this.mSettings.getOverlaysForUser(userId);
    }

    java.util.Set<android.content.pm.UserPackage> setEnabled(android.content.om.OverlayIdentifier overlay, boolean enable, int userId) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
        if (com.android.server.om.OverlayManagerService.DEBUG) {
            android.util.Slog.d("OverlayManager", java.lang.String.format("setEnabled overlay=%s enable=%s userId=%d", overlay, java.lang.Boolean.valueOf(enable), java.lang.Integer.valueOf(userId)));
        }
        try {
            android.content.om.OverlayInfo oi = this.mSettings.getOverlayInfo(overlay, userId);
            if (!oi.isMutable) {
                throw new com.android.server.om.OverlayManagerServiceImpl.OperationFailedException("cannot enable immutable overlay packages in runtime");
            }
            boolean modified = this.mSettings.setEnabled(overlay, userId, enable);
            if (modified | updateState(oi, userId, 0)) {
                return java.util.Set.of(android.content.pm.UserPackage.of(userId, oi.targetPackageName));
            }
            return java.util.Set.of();
        } catch (com.android.server.om.OverlayManagerSettings.BadKeyException e) {
            throw new com.android.server.om.OverlayManagerServiceImpl.OperationFailedException("failed to update settings", e);
        }
    }

    java.util.Optional<android.content.pm.UserPackage> setEnabledExclusive(android.content.om.OverlayIdentifier overlay, boolean withinCategory, int userId) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
        if (com.android.server.om.OverlayManagerService.DEBUG) {
            android.util.Slog.d("OverlayManager", java.lang.String.format("setEnabledExclusive overlay=%s withinCategory=%s userId=%d", overlay, java.lang.Boolean.valueOf(withinCategory), java.lang.Integer.valueOf(userId)));
        }
        try {
            android.content.om.OverlayInfo enabledInfo = this.mSettings.getOverlayInfo(overlay, userId);
            if (!enabledInfo.isMutable) {
                throw new com.android.server.om.OverlayManagerServiceImpl.OperationFailedException("cannot enable immutable overlay packages in runtime");
            }
            java.util.List<android.content.om.OverlayInfo> allOverlays = getOverlayInfosForTarget(enabledInfo.targetPackageName, userId);
            allOverlays.remove(enabledInfo);
            boolean modified = false;
            for (int i = 0; i < allOverlays.size(); i++) {
                android.content.om.OverlayInfo disabledInfo = allOverlays.get(i);
                android.content.om.OverlayIdentifier disabledOverlay = disabledInfo.getOverlayIdentifier();
                if (disabledInfo.isMutable && (!withinCategory || java.util.Objects.equals(disabledInfo.category, enabledInfo.category))) {
                    modified = modified | this.mSettings.setEnabled(disabledOverlay, userId, false) | updateState(disabledInfo, userId, 0);
                }
            }
            if (modified | this.mSettings.setEnabled(overlay, userId, true) | updateState(enabledInfo, userId, 0)) {
                return java.util.Optional.of(android.content.pm.UserPackage.of(userId, enabledInfo.targetPackageName));
            }
            return java.util.Optional.empty();
        } catch (com.android.server.om.OverlayManagerSettings.BadKeyException e) {
            throw new com.android.server.om.OverlayManagerServiceImpl.OperationFailedException("failed to update settings", e);
        }
    }

    java.util.Set<android.content.pm.UserPackage> registerFabricatedOverlay(android.os.FabricatedOverlayInternal overlay) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
        if (android.content.pm.parsing.FrameworkParsingPackageUtils.validateName(overlay.overlayName, false, true) != null) {
            throw new com.android.server.om.OverlayManagerServiceImpl.OperationFailedException("overlay name can only consist of alphanumeric characters, '_', and '.'");
        }
        android.os.FabricatedOverlayInfo info = this.mIdmapManager.createFabricatedOverlay(overlay);
        if (info == null) {
            throw new com.android.server.om.OverlayManagerServiceImpl.OperationFailedException("failed to create fabricated overlay");
        }
        java.util.Set<android.content.pm.UserPackage> updatedTargets = new android.util.ArraySet<>();
        for (int userId : this.mSettings.getUsers()) {
            updatedTargets.addAll(registerFabricatedOverlay(info, userId));
        }
        return updatedTargets;
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x00a9 A[Catch: BadKeyException -> 0x00bb, TRY_LEAVE, TryCatch #4 {BadKeyException -> 0x00bb, blocks: (B:28:0x00a3, B:30:0x00a9), top: B:55:0x00a3 }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00b7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.Set<android.content.pm.UserPackage> registerFabricatedOverlay(android.os.FabricatedOverlayInfo r20, int r21) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
        /*
            Method dump skipped, instruction units count: 209
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.om.OverlayManagerServiceImpl.registerFabricatedOverlay(android.os.FabricatedOverlayInfo, int):java.util.Set");
    }

    java.util.Set<android.content.pm.UserPackage> unregisterFabricatedOverlay(android.content.om.OverlayIdentifier overlay) {
        java.util.Set<android.content.pm.UserPackage> updatedTargets = new android.util.ArraySet<>();
        for (int userId : this.mSettings.getUsers()) {
            updatedTargets.addAll(unregisterFabricatedOverlay(overlay, userId));
        }
        return updatedTargets;
    }

    private java.util.Set<android.content.pm.UserPackage> unregisterFabricatedOverlay(android.content.om.OverlayIdentifier overlay, int userId) {
        android.content.om.OverlayInfo oi = this.mSettings.getNullableOverlayInfo(overlay, userId);
        if (oi != null) {
            this.mSettings.remove(overlay, userId);
            if (oi.isEnabled()) {
                return java.util.Set.of(android.content.pm.UserPackage.of(userId, oi.targetPackageName));
            }
        }
        return java.util.Set.of();
    }

    private void cleanStaleResourceCache() {
        java.util.Set<java.lang.String> fabricatedPaths = this.mSettings.getAllBaseCodePaths();
        for (android.os.FabricatedOverlayInfo info : this.mIdmapManager.getFabricatedOverlayInfos()) {
            if (!fabricatedPaths.contains(info.path)) {
                this.mIdmapManager.deleteFabricatedOverlay(info.path);
            }
        }
    }

    private java.util.List<android.os.FabricatedOverlayInfo> getFabricatedOverlayInfos() {
        final java.util.Set<java.lang.String> fabricatedPaths = this.mSettings.getAllBaseCodePaths();
        java.util.ArrayList<android.os.FabricatedOverlayInfo> infos = new java.util.ArrayList<>(this.mIdmapManager.getFabricatedOverlayInfos());
        infos.removeIf(new java.util.function.Predicate() { // from class: com.android.server.om.OverlayManagerServiceImpl$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.om.OverlayManagerServiceImpl.lambda$getFabricatedOverlayInfos$3(fabricatedPaths, (android.os.FabricatedOverlayInfo) obj);
            }
        });
        return infos;
    }

    static /* synthetic */ boolean lambda$getFabricatedOverlayInfos$3(java.util.Set fabricatedPaths, android.os.FabricatedOverlayInfo info) {
        return !fabricatedPaths.contains(info.path);
    }

    private boolean isPackageConfiguredMutable(com.android.server.pm.pkg.AndroidPackage overlay) {
        return this.mOverlayConfig.isMutable(overlay.getPackageName());
    }

    private int getPackageConfiguredPriority(com.android.server.pm.pkg.AndroidPackage overlay) {
        return this.mOverlayConfig.getPriority(overlay.getPackageName());
    }

    private boolean isPackageConfiguredEnabled(com.android.server.pm.pkg.AndroidPackage overlay) {
        return this.mOverlayConfig.isEnabled(overlay.getPackageName());
    }

    java.util.Optional<android.content.pm.UserPackage> setPriority(android.content.om.OverlayIdentifier overlay, android.content.om.OverlayIdentifier newParentOverlay, int userId) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
        try {
            if (com.android.server.om.OverlayManagerService.DEBUG) {
                android.util.Slog.d("OverlayManager", "setPriority overlay=" + overlay + " newParentOverlay=" + newParentOverlay + " userId=" + userId);
            }
            android.content.om.OverlayInfo overlayInfo = this.mSettings.getOverlayInfo(overlay, userId);
            if (!overlayInfo.isMutable) {
                throw new com.android.server.om.OverlayManagerServiceImpl.OperationFailedException("cannot change priority of an immutable overlay package at runtime");
            }
            if (this.mSettings.setPriority(overlay, newParentOverlay, userId)) {
                return java.util.Optional.of(android.content.pm.UserPackage.of(userId, overlayInfo.targetPackageName));
            }
            return java.util.Optional.empty();
        } catch (com.android.server.om.OverlayManagerSettings.BadKeyException e) {
            throw new com.android.server.om.OverlayManagerServiceImpl.OperationFailedException("failed to update settings", e);
        }
    }

    java.util.Set<android.content.pm.UserPackage> setHighestPriority(android.content.om.OverlayIdentifier overlay, int userId) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
        try {
            if (com.android.server.om.OverlayManagerService.DEBUG) {
                android.util.Slog.d("OverlayManager", "setHighestPriority overlay=" + overlay + " userId=" + userId);
            }
            android.content.om.OverlayInfo overlayInfo = this.mSettings.getOverlayInfo(overlay, userId);
            if (!overlayInfo.isMutable) {
                throw new com.android.server.om.OverlayManagerServiceImpl.OperationFailedException("cannot change priority of an immutable overlay package at runtime");
            }
            if (this.mSettings.setHighestPriority(overlay, userId)) {
                return java.util.Set.of(android.content.pm.UserPackage.of(userId, overlayInfo.targetPackageName));
            }
            return java.util.Set.of();
        } catch (com.android.server.om.OverlayManagerSettings.BadKeyException e) {
            throw new com.android.server.om.OverlayManagerServiceImpl.OperationFailedException("failed to update settings", e);
        }
    }

    java.util.Optional<android.content.pm.UserPackage> setLowestPriority(android.content.om.OverlayIdentifier overlay, int userId) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
        try {
            if (com.android.server.om.OverlayManagerService.DEBUG) {
                android.util.Slog.d("OverlayManager", "setLowestPriority packageName=" + overlay + " userId=" + userId);
            }
            android.content.om.OverlayInfo overlayInfo = this.mSettings.getOverlayInfo(overlay, userId);
            if (!overlayInfo.isMutable) {
                throw new com.android.server.om.OverlayManagerServiceImpl.OperationFailedException("cannot change priority of an immutable overlay package at runtime");
            }
            if (this.mSettings.setLowestPriority(overlay, userId)) {
                return java.util.Optional.of(android.content.pm.UserPackage.of(userId, overlayInfo.targetPackageName));
            }
            return java.util.Optional.empty();
        } catch (com.android.server.om.OverlayManagerSettings.BadKeyException e) {
            throw new com.android.server.om.OverlayManagerServiceImpl.OperationFailedException("failed to update settings", e);
        }
    }

    void dump(java.io.PrintWriter pw, com.android.server.om.DumpState dumpState) {
        android.content.om.OverlayIdentifier id;
        android.content.om.OverlayInfo oi;
        android.util.Pair<android.content.om.OverlayIdentifier, java.lang.String> overlayIdmap = null;
        if (dumpState.getPackageName() != null && (oi = this.mSettings.getNullableOverlayInfo((id = new android.content.om.OverlayIdentifier(dumpState.getPackageName(), dumpState.getOverlayName())), 0)) != null) {
            overlayIdmap = new android.util.Pair<>(id, oi.baseCodePath);
        }
        this.mSettings.dump(pw, dumpState);
        if (dumpState.getField() == null) {
            java.util.Set<android.util.Pair<android.content.om.OverlayIdentifier, java.lang.String>> allIdmaps = overlayIdmap != null ? java.util.Set.of(overlayIdmap) : this.mSettings.getAllIdentifiersAndBaseCodePaths();
            for (android.util.Pair<android.content.om.OverlayIdentifier, java.lang.String> pair : allIdmaps) {
                pw.println("IDMAP OF " + pair.first);
                java.lang.String dump = this.mIdmapManager.dumpIdmap((java.lang.String) pair.second);
                if (dump != null) {
                    pw.println(dump);
                } else {
                    android.content.om.OverlayInfo oi2 = this.mSettings.getNullableOverlayInfo((android.content.om.OverlayIdentifier) pair.first, 0);
                    pw.println((oi2 == null || this.mIdmapManager.idmapExists(oi2)) ? "<internal error>" : "<missing idmap>");
                }
            }
        }
        if (overlayIdmap == null) {
            pw.println("Default overlays: " + android.text.TextUtils.join(";", this.mDefaultOverlays));
        }
        if (dumpState.getPackageName() == null) {
            this.mOverlayConfig.dump(pw);
        }
    }

    java.lang.String[] getDefaultOverlayPackages() {
        return this.mDefaultOverlays;
    }

    void removeIdmapForOverlay(android.content.om.OverlayIdentifier overlay, int userId) throws com.android.server.om.OverlayManagerServiceImpl.OperationFailedException {
        try {
            android.content.om.OverlayInfo oi = this.mSettings.getOverlayInfo(overlay, userId);
            removeIdmapIfPossible(oi);
        } catch (com.android.server.om.OverlayManagerSettings.BadKeyException e) {
            throw new com.android.server.om.OverlayManagerServiceImpl.OperationFailedException("failed to update settings", e);
        }
    }

    android.content.pm.overlay.OverlayPaths getEnabledOverlayPaths(java.lang.String targetPackageName, int userId, final boolean includeImmutableOverlays) {
        final android.content.pm.overlay.OverlayPaths.Builder paths = new android.content.pm.overlay.OverlayPaths.Builder();
        this.mSettings.forEachMatching(userId, null, targetPackageName, new java.util.function.Consumer() { // from class: com.android.server.om.OverlayManagerServiceImpl$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.om.OverlayManagerServiceImpl.lambda$getEnabledOverlayPaths$4(includeImmutableOverlays, paths, (android.content.om.OverlayInfo) obj);
            }
        });
        return paths.build();
    }

    static /* synthetic */ void lambda$getEnabledOverlayPaths$4(boolean includeImmutableOverlays, android.content.pm.overlay.OverlayPaths.Builder paths, android.content.om.OverlayInfo oi) {
        if (!oi.isEnabled()) {
            return;
        }
        if (!includeImmutableOverlays && !oi.isMutable) {
            return;
        }
        if (oi.isFabricated()) {
            paths.addNonApkPath(oi.baseCodePath);
        } else {
            paths.addApkPath(oi.baseCodePath);
        }
    }

    private boolean updateState(android.content.om.CriticalOverlayInfo info, int userId, int flags) throws com.android.server.om.OverlayManagerSettings.BadKeyException {
        boolean modified;
        int idmapStatus;
        android.content.om.OverlayIdentifier overlay = info.getOverlayIdentifier();
        com.android.server.pm.pkg.PackageState targetPackageState = this.mPackageManager.getPackageStateForUser(info.getTargetPackageName(), userId);
        com.android.server.pm.pkg.AndroidPackage targetPackage = targetPackageState == null ? null : targetPackageState.getAndroidPackage();
        com.android.server.pm.pkg.PackageState overlayPackageState = this.mPackageManager.getPackageStateForUser(info.getPackageName(), userId);
        com.android.server.pm.pkg.AndroidPackage overlayPackage = overlayPackageState != null ? overlayPackageState.getAndroidPackage() : null;
        if (overlayPackage == null) {
            removeIdmapIfPossible(this.mSettings.getOverlayInfo(overlay, userId));
            return this.mSettings.remove(overlay, userId);
        }
        boolean modified2 = false | this.mSettings.setCategory(overlay, userId, overlayPackage.getOverlayCategory());
        if (!info.isFabricated()) {
            modified2 |= this.mSettings.setBaseCodePath(overlay, userId, ((com.android.server.pm.pkg.AndroidPackageSplit) overlayPackage.getSplits().get(0)).getPath());
        }
        android.content.om.OverlayInfo updatedOverlayInfo = this.mSettings.getOverlayInfo(overlay, userId);
        if (targetPackage != null && ((!com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(info.getTargetPackageName()) && !"oplus".equals(info.getTargetPackageName())) || isPackageConfiguredMutable(overlayPackage))) {
            int idmapStatus2 = this.mIdmapManager.createIdmap(targetPackage, overlayPackageState, overlayPackage, updatedOverlayInfo.baseCodePath, overlay.getOverlayName(), userId);
            modified = modified2 | ((idmapStatus2 & 2) != 0);
            idmapStatus = idmapStatus2;
        } else {
            modified = modified2;
            idmapStatus = 0;
        }
        int currentState = this.mSettings.getState(overlay, userId);
        int newState = calculateNewState(updatedOverlayInfo, targetPackage, userId, flags, idmapStatus);
        if (currentState != newState) {
            if (com.android.server.om.OverlayManagerService.DEBUG) {
                android.util.Slog.d("OverlayManager", java.lang.String.format("%s:%d: %s -> %s", overlay, java.lang.Integer.valueOf(userId), android.content.om.OverlayInfo.stateToString(currentState), android.content.om.OverlayInfo.stateToString(newState)));
            }
            return modified | this.mSettings.setState(overlay, userId, newState);
        }
        if (com.android.server.om.OverlayManagerService.DEBUG) {
            android.util.Slog.d("OverlayManager", java.lang.String.format("%s:%d: %s", overlay, java.lang.Integer.valueOf(userId), android.content.om.OverlayInfo.stateToString(currentState)));
            return modified;
        }
        return modified;
    }

    private int calculateNewState(android.content.om.OverlayInfo info, com.android.server.pm.pkg.AndroidPackage targetPackage, int userId, int flags, int idmapStatus) throws com.android.server.om.OverlayManagerSettings.BadKeyException {
        if ((flags & 1) != 0) {
            return 4;
        }
        if ((flags & 2) != 0) {
            return 5;
        }
        if ((flags & 4) != 0) {
            return 7;
        }
        if (targetPackage == null) {
            return 0;
        }
        if ((idmapStatus & 1) == 0 && !this.mIdmapManager.idmapExists(info)) {
            return 1;
        }
        boolean enabled = this.mSettings.getEnabled(info.getOverlayIdentifier(), userId);
        return enabled ? 3 : 2;
    }

    private void removeIdmapIfPossible(android.content.om.OverlayInfo oi) {
        android.content.om.OverlayInfo tmp;
        if (!this.mIdmapManager.idmapExists(oi)) {
            return;
        }
        int[] userIds = this.mSettings.getUsers();
        for (int userId : userIds) {
            try {
                tmp = this.mSettings.getOverlayInfo(oi.getOverlayIdentifier(), userId);
            } catch (com.android.server.om.OverlayManagerSettings.BadKeyException e) {
            }
            if (tmp != null && tmp.isEnabled()) {
                return;
            }
        }
        this.mIdmapManager.removeIdmap(oi, oi.userId);
    }

    static final class OperationFailedException extends java.lang.Exception {
        OperationFailedException(java.lang.String message) {
            super(message);
        }

        OperationFailedException(java.lang.String message, java.lang.Throwable cause) {
            super(message, cause);
        }
    }

    com.android.internal.content.om.OverlayConfig getOverlayConfig() {
        return this.mOverlayConfig;
    }
}
