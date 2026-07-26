package com.android.server.om;

/* JADX INFO: loaded from: classes2.dex */
public class OverlayReferenceMapper {
    private static final java.lang.String TAG = "OverlayReferenceMapper";
    private boolean mDeferRebuild;
    private final com.android.server.om.OverlayReferenceMapper.Provider mProvider;
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.ArrayMap<java.lang.String, android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>>> mActorToTargetToOverlays = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, java.util.Set<java.lang.String>> mActorPkgToPkgs = new android.util.ArrayMap<>();

    public interface Provider {
        java.lang.String getActorPkg(java.lang.String str);

        java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getTargetToOverlayables(com.android.server.pm.pkg.AndroidPackage androidPackage);
    }

    public OverlayReferenceMapper(boolean deferRebuild, com.android.server.om.OverlayReferenceMapper.Provider provider) {
        this.mDeferRebuild = deferRebuild;
        this.mProvider = provider != null ? provider : new com.android.server.om.OverlayReferenceMapper.Provider() { // from class: com.android.server.om.OverlayReferenceMapper.1
            @Override // com.android.server.om.OverlayReferenceMapper.Provider
            public java.lang.String getActorPkg(java.lang.String actor) {
                java.util.Map<java.lang.String, java.util.Map<java.lang.String, java.lang.String>> namedActors = com.android.server.SystemConfig.getInstance().getNamedActors();
                android.util.Pair<java.lang.String, com.android.server.om.OverlayActorEnforcer.ActorState> actorPair = com.android.server.om.OverlayActorEnforcer.getPackageNameForActor(actor, namedActors);
                return (java.lang.String) actorPair.first;
            }

            @Override // com.android.server.om.OverlayReferenceMapper.Provider
            public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getTargetToOverlayables(com.android.server.pm.pkg.AndroidPackage pkg) {
                java.lang.String target = pkg.getOverlayTarget();
                if (android.text.TextUtils.isEmpty(target)) {
                    return java.util.Collections.emptyMap();
                }
                java.lang.String overlayable = pkg.getOverlayTargetOverlayableName();
                java.util.Map<java.lang.String, java.util.Set<java.lang.String>> targetToOverlayables = new java.util.HashMap<>();
                java.util.Set<java.lang.String> overlayables = new java.util.HashSet<>();
                overlayables.add(overlayable);
                targetToOverlayables.put(target, overlayables);
                return targetToOverlayables;
            }
        };
    }

    public java.util.Map<java.lang.String, java.util.Set<java.lang.String>> getActorPkgToPkgs() {
        return this.mActorPkgToPkgs;
    }

    public boolean isValidActor(java.lang.String targetName, java.lang.String actorPackageName) {
        boolean z;
        synchronized (this.mLock) {
            ensureMapBuilt();
            java.util.Set<java.lang.String> validSet = this.mActorPkgToPkgs.get(actorPackageName);
            z = validSet != null && validSet.contains(targetName);
        }
        return z;
    }

    public android.util.ArraySet<java.lang.String> addPkg(com.android.server.pm.pkg.AndroidPackage pkg, java.util.Map<java.lang.String, com.android.server.pm.pkg.AndroidPackage> otherPkgs) {
        android.util.ArraySet<java.lang.String> changed;
        synchronized (this.mLock) {
            changed = new android.util.ArraySet<>();
            if (!pkg.getOverlayables().isEmpty()) {
                addTarget(pkg, otherPkgs, changed);
            }
            if (!this.mProvider.getTargetToOverlayables(pkg).isEmpty()) {
                addOverlay(pkg, otherPkgs, changed);
            }
            if (!this.mDeferRebuild) {
                rebuild();
            }
        }
        return changed;
    }

    public android.util.ArraySet<java.lang.String> removePkg(java.lang.String pkgName) {
        android.util.ArraySet<java.lang.String> changedPackages;
        synchronized (this.mLock) {
            changedPackages = new android.util.ArraySet<>();
            removeTarget(pkgName, changedPackages);
            removeOverlay(pkgName, changedPackages);
            if (!this.mDeferRebuild) {
                rebuild();
            }
        }
        return changedPackages;
    }

    private void removeTarget(java.lang.String target, java.util.Collection<java.lang.String> changedPackages) {
        synchronized (this.mLock) {
            int size = this.mActorToTargetToOverlays.size();
            for (int index = size - 1; index >= 0; index--) {
                android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> targetToOverlays = this.mActorToTargetToOverlays.valueAt(index);
                if (targetToOverlays.containsKey(target)) {
                    targetToOverlays.remove(target);
                    java.lang.String actor = this.mActorToTargetToOverlays.keyAt(index);
                    changedPackages.add(this.mProvider.getActorPkg(actor));
                    if (targetToOverlays.isEmpty()) {
                        this.mActorToTargetToOverlays.removeAt(index);
                    }
                }
            }
        }
    }

    private void addTarget(com.android.server.pm.pkg.AndroidPackage targetPkg, java.util.Map<java.lang.String, com.android.server.pm.pkg.AndroidPackage> otherPkgs, java.util.Collection<java.lang.String> changedPackages) {
        synchronized (this.mLock) {
            java.lang.String target = targetPkg.getPackageName();
            removeTarget(target, changedPackages);
            java.util.Map<java.lang.String, java.lang.String> overlayablesToActors = targetPkg.getOverlayables();
            for (java.lang.String overlayable : overlayablesToActors.keySet()) {
                java.lang.String actor = overlayablesToActors.get(overlayable);
                addTargetToMap(actor, target, changedPackages);
                for (com.android.server.pm.pkg.AndroidPackage overlayPkg : otherPkgs.values()) {
                    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> targetToOverlayables = this.mProvider.getTargetToOverlayables(overlayPkg);
                    java.util.Set<java.lang.String> overlayables = targetToOverlayables.get(target);
                    if (!com.android.internal.util.CollectionUtils.isEmpty(overlayables)) {
                        if (overlayables.contains(overlayable)) {
                            java.lang.String overlay = overlayPkg.getPackageName();
                            addOverlayToMap(actor, target, overlay, changedPackages);
                        }
                    }
                }
            }
        }
    }

    private void removeOverlay(java.lang.String overlay, java.util.Collection<java.lang.String> changedPackages) {
        synchronized (this.mLock) {
            int actorsSize = this.mActorToTargetToOverlays.size();
            for (int actorIndex = actorsSize - 1; actorIndex >= 0; actorIndex--) {
                android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> targetToOverlays = this.mActorToTargetToOverlays.valueAt(actorIndex);
                int targetsSize = targetToOverlays.size();
                for (int targetIndex = targetsSize - 1; targetIndex >= 0; targetIndex--) {
                    java.util.Set<java.lang.String> overlays = targetToOverlays.valueAt(targetIndex);
                    if (overlays.remove(overlay)) {
                        java.lang.String actor = this.mActorToTargetToOverlays.keyAt(actorIndex);
                        changedPackages.add(this.mProvider.getActorPkg(actor));
                    }
                }
                if (targetToOverlays.isEmpty()) {
                    this.mActorToTargetToOverlays.removeAt(actorIndex);
                }
            }
        }
    }

    private void addOverlay(com.android.server.pm.pkg.AndroidPackage overlayPkg, java.util.Map<java.lang.String, com.android.server.pm.pkg.AndroidPackage> otherPkgs, java.util.Collection<java.lang.String> changedPackages) throws java.lang.Throwable {
        java.lang.String overlay;
        synchronized (this.mLock) {
            try {
                try {
                    overlay = overlayPkg.getPackageName();
                    removeOverlay(overlay, changedPackages);
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
                try {
                    java.util.Map<java.lang.String, java.util.Set<java.lang.String>> targetToOverlayables = this.mProvider.getTargetToOverlayables(overlayPkg);
                    for (java.util.Map.Entry<java.lang.String, java.util.Set<java.lang.String>> entry : targetToOverlayables.entrySet()) {
                        java.lang.String target = entry.getKey();
                        java.util.Set<java.lang.String> overlayables = entry.getValue();
                        com.android.server.pm.pkg.AndroidPackage targetPkg = otherPkgs.get(target);
                        if (targetPkg != null) {
                            java.lang.String targetPkgName = targetPkg.getPackageName();
                            java.util.Map<java.lang.String, java.lang.String> overlayableToActor = targetPkg.getOverlayables();
                            for (java.lang.String overlayable : overlayables) {
                                java.lang.String actor = overlayableToActor.get(overlayable);
                                if (!android.text.TextUtils.isEmpty(actor)) {
                                    addOverlayToMap(actor, targetPkgName, overlay, changedPackages);
                                    targetToOverlayables = targetToOverlayables;
                                }
                            }
                        }
                    }
                } catch (java.lang.Throwable th2) {
                    th = th2;
                    throw th;
                }
            } catch (java.lang.Throwable th3) {
                th = th3;
            }
        }
    }

    public void rebuildIfDeferred() {
        synchronized (this.mLock) {
            if (this.mDeferRebuild) {
                rebuild();
                this.mDeferRebuild = false;
            }
        }
    }

    private void ensureMapBuilt() {
        if (this.mDeferRebuild) {
            rebuildIfDeferred();
            android.util.Slog.w(TAG, "The actor map was queried before the system was ready, which mayresult in decreased performance.");
        }
    }

    private void rebuild() {
        synchronized (this.mLock) {
            this.mActorPkgToPkgs.clear();
            for (java.lang.String actor : this.mActorToTargetToOverlays.keySet()) {
                java.lang.String actorPkg = this.mProvider.getActorPkg(actor);
                if (!android.text.TextUtils.isEmpty(actorPkg)) {
                    android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> targetToOverlays = this.mActorToTargetToOverlays.get(actor);
                    java.util.Set<java.lang.String> pkgs = new java.util.HashSet<>();
                    for (java.lang.String target : targetToOverlays.keySet()) {
                        java.util.Set<java.lang.String> overlays = targetToOverlays.get(target);
                        pkgs.add(target);
                        pkgs.addAll(overlays);
                    }
                    this.mActorPkgToPkgs.put(actorPkg, pkgs);
                }
            }
        }
    }

    private void addTargetToMap(java.lang.String actor, java.lang.String target, java.util.Collection<java.lang.String> changedPackages) {
        android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> targetToOverlays = this.mActorToTargetToOverlays.get(actor);
        if (targetToOverlays == null) {
            targetToOverlays = new android.util.ArrayMap<>();
            this.mActorToTargetToOverlays.put(actor, targetToOverlays);
        }
        android.util.ArraySet<java.lang.String> overlays = targetToOverlays.get(target);
        if (overlays == null) {
            android.util.ArraySet<java.lang.String> overlays2 = new android.util.ArraySet<>();
            targetToOverlays.put(target, overlays2);
        }
        changedPackages.add(this.mProvider.getActorPkg(actor));
    }

    private void addOverlayToMap(java.lang.String actor, java.lang.String target, java.lang.String overlay, java.util.Collection<java.lang.String> changedPackages) {
        synchronized (this.mLock) {
            android.util.ArrayMap<java.lang.String, android.util.ArraySet<java.lang.String>> targetToOverlays = this.mActorToTargetToOverlays.get(actor);
            if (targetToOverlays == null) {
                targetToOverlays = new android.util.ArrayMap<>();
                this.mActorToTargetToOverlays.put(actor, targetToOverlays);
            }
            android.util.ArraySet<java.lang.String> overlays = targetToOverlays.get(target);
            if (overlays == null) {
                overlays = new android.util.ArraySet<>();
                targetToOverlays.put(target, overlays);
            }
            overlays.add(overlay);
        }
        changedPackages.add(this.mProvider.getActorPkg(actor));
    }
}
