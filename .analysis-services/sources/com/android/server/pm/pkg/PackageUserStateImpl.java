package com.android.server.pm.pkg;

/* JADX INFO: loaded from: classes2.dex */
public class PackageUserStateImpl extends com.android.server.utils.WatchableImpl implements com.android.server.pm.pkg.PackageUserStateInternal, com.android.server.utils.Snappable {
    private com.android.server.pm.pkg.ArchiveState mArchiveState;
    private int mBooleans;
    private long mCeDataInode;
    private com.android.server.utils.WatchedArrayMap<android.content.ComponentName, android.util.Pair<java.lang.String, java.lang.Integer>> mComponentLabelIconOverrideMap;
    private long mDeDataInode;
    protected com.android.server.utils.WatchedArraySet<java.lang.String> mDisabledComponentsWatched;
    private int mDistractionFlags;
    protected com.android.server.utils.WatchedArraySet<java.lang.String> mEnabledComponentsWatched;
    private int mEnabledState;
    private long mFirstInstallTimeMillis;
    private java.lang.String mHarmfulAppWarning;
    private int mInstallReason;
    private java.lang.String mLastDisableAppCaller;
    private int mMinAspectRatio;
    private android.content.pm.overlay.OverlayPaths mOverlayPaths;
    public android.content.pm.IPackageUserStateExt mPackageUserStateExt;
    protected com.android.server.utils.WatchedArrayMap<java.lang.String, android.content.pm.overlay.OverlayPaths> mSharedLibraryOverlayPaths;
    final com.android.server.utils.SnapshotCache<com.android.server.pm.pkg.PackageUserStateImpl> mSnapshot;
    private java.lang.String mSplashScreenTheme;
    private com.android.server.utils.WatchedArrayMap<android.content.pm.UserPackage, com.android.server.pm.pkg.SuspendParams> mSuspendParams;
    private int mUninstallReason;
    private com.android.server.utils.Watchable mWatchable;

    private static class Booleans {
        private static final int HIDDEN = 8;
        private static final int INSTALLED = 1;
        private static final int INSTANT_APP = 16;
        private static final int NOT_LAUNCHED = 4;
        private static final int STOPPED = 2;
        private static final int VIRTUAL_PRELOADED = 32;

        public @interface Flags {
        }

        private Booleans() {
        }
    }

    private void setBoolean(int flag, boolean value) {
        if (value) {
            this.mBooleans |= flag;
        } else {
            this.mBooleans &= ~flag;
        }
    }

    private boolean getBoolean(int flag) {
        return (this.mBooleans & flag) != 0;
    }

    private com.android.server.utils.SnapshotCache<com.android.server.pm.pkg.PackageUserStateImpl> makeCache() {
        return new com.android.server.utils.SnapshotCache<com.android.server.pm.pkg.PackageUserStateImpl>(this, this) { // from class: com.android.server.pm.pkg.PackageUserStateImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.android.server.utils.SnapshotCache
            public com.android.server.pm.pkg.PackageUserStateImpl createSnapshot() {
                return new com.android.server.pm.pkg.PackageUserStateImpl(com.android.server.pm.pkg.PackageUserStateImpl.this.mWatchable, (com.android.server.pm.pkg.PackageUserStateImpl) this.mSource);
            }
        };
    }

    public PackageUserStateImpl() {
        this.mEnabledState = 0;
        this.mInstallReason = 0;
        this.mUninstallReason = 0;
        this.mMinAspectRatio = 0;
        this.mPackageUserStateExt = (android.content.pm.IPackageUserStateExt) system.ext.loader.core.ExtLoader.type(android.content.pm.IPackageUserStateExt.class).base(this).create();
        this.mWatchable = null;
        this.mSnapshot = makeCache();
        setBoolean(1, true);
    }

    public PackageUserStateImpl(com.android.server.utils.Watchable watchable) {
        this.mEnabledState = 0;
        this.mInstallReason = 0;
        this.mUninstallReason = 0;
        this.mMinAspectRatio = 0;
        this.mPackageUserStateExt = (android.content.pm.IPackageUserStateExt) system.ext.loader.core.ExtLoader.type(android.content.pm.IPackageUserStateExt.class).base(this).create();
        this.mWatchable = watchable;
        this.mSnapshot = makeCache();
        setBoolean(1, true);
    }

    public PackageUserStateImpl(com.android.server.utils.Watchable watchable, com.android.server.pm.pkg.PackageUserStateImpl other) {
        this.mEnabledState = 0;
        this.mInstallReason = 0;
        this.mUninstallReason = 0;
        this.mMinAspectRatio = 0;
        this.mPackageUserStateExt = (android.content.pm.IPackageUserStateExt) system.ext.loader.core.ExtLoader.type(android.content.pm.IPackageUserStateExt.class).base(this).create();
        this.mWatchable = watchable;
        this.mBooleans = other.mBooleans;
        this.mDisabledComponentsWatched = other.mDisabledComponentsWatched == null ? null : other.mDisabledComponentsWatched.snapshot();
        this.mEnabledComponentsWatched = other.mEnabledComponentsWatched == null ? null : other.mEnabledComponentsWatched.snapshot();
        this.mOverlayPaths = other.mOverlayPaths;
        this.mSharedLibraryOverlayPaths = other.mSharedLibraryOverlayPaths == null ? null : other.mSharedLibraryOverlayPaths.snapshot();
        this.mCeDataInode = other.mCeDataInode;
        this.mDeDataInode = other.mDeDataInode;
        this.mDistractionFlags = other.mDistractionFlags;
        this.mEnabledState = other.mEnabledState;
        this.mInstallReason = other.mInstallReason;
        this.mUninstallReason = other.mUninstallReason;
        this.mHarmfulAppWarning = other.mHarmfulAppWarning;
        this.mLastDisableAppCaller = other.mLastDisableAppCaller;
        this.mSplashScreenTheme = other.mSplashScreenTheme;
        this.mMinAspectRatio = other.mMinAspectRatio;
        this.mSuspendParams = other.mSuspendParams == null ? null : other.mSuspendParams.snapshot();
        this.mComponentLabelIconOverrideMap = other.mComponentLabelIconOverrideMap != null ? other.mComponentLabelIconOverrideMap.snapshot() : null;
        this.mFirstInstallTimeMillis = other.mFirstInstallTimeMillis;
        this.mArchiveState = other.mArchiveState;
        this.mSnapshot = new com.android.server.utils.SnapshotCache.Sealed();
        this.mPackageUserStateExt.setExtraData(other.mPackageUserStateExt);
    }

    private void onChanged() {
        if (this.mWatchable != null) {
            this.mWatchable.dispatchChange(this.mWatchable);
        }
        dispatchChange(this);
    }

    @Override // com.android.server.utils.Snappable
    public com.android.server.pm.pkg.PackageUserStateImpl snapshot() {
        return this.mSnapshot.snapshot();
    }

    public boolean setOverlayPaths(android.content.pm.overlay.OverlayPaths paths) {
        if (java.util.Objects.equals(paths, this.mOverlayPaths)) {
            return false;
        }
        if ((this.mOverlayPaths == null && paths.isEmpty()) || (paths == null && this.mOverlayPaths.isEmpty())) {
            return false;
        }
        this.mOverlayPaths = paths;
        onChanged();
        return true;
    }

    public boolean setSharedLibraryOverlayPaths(java.lang.String library, android.content.pm.overlay.OverlayPaths paths) {
        if (this.mSharedLibraryOverlayPaths == null) {
            this.mSharedLibraryOverlayPaths = new com.android.server.utils.WatchedArrayMap<>();
            this.mSharedLibraryOverlayPaths.registerObserver(this.mSnapshot);
        }
        android.content.pm.overlay.OverlayPaths currentPaths = this.mSharedLibraryOverlayPaths.get(library);
        if (java.util.Objects.equals(paths, currentPaths)) {
            return false;
        }
        if (paths == null || paths.isEmpty()) {
            boolean returnValue = this.mSharedLibraryOverlayPaths.remove(library) != null;
            onChanged();
            return returnValue;
        }
        this.mSharedLibraryOverlayPaths.put(library, paths);
        onChanged();
        return true;
    }

    @Override // com.android.server.pm.pkg.PackageUserStateInternal
    public com.android.server.utils.WatchedArraySet<java.lang.String> getDisabledComponentsNoCopy() {
        return this.mDisabledComponentsWatched;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean ignorePackageDisabledInIsEnabled(int enabled, long flags) {
        return this.mPackageUserStateExt.ignorePackageDisabledInIsEnabled(enabled, flags);
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public int getOplusFreezeState() {
        return this.mPackageUserStateExt.getFreezeState();
    }

    @Override // com.android.server.pm.pkg.PackageUserStateInternal
    public com.android.server.utils.WatchedArraySet<java.lang.String> getEnabledComponentsNoCopy() {
        return this.mEnabledComponentsWatched;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    /* JADX INFO: renamed from: getDisabledComponents, reason: merged with bridge method [inline-methods] */
    public android.util.ArraySet<java.lang.String> m8024getDisabledComponents() {
        return this.mDisabledComponentsWatched == null ? new android.util.ArraySet<>() : this.mDisabledComponentsWatched.untrackedStorage();
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    /* JADX INFO: renamed from: getEnabledComponents, reason: merged with bridge method [inline-methods] */
    public android.util.ArraySet<java.lang.String> m8025getEnabledComponents() {
        return this.mEnabledComponentsWatched == null ? new android.util.ArraySet<>() : this.mEnabledComponentsWatched.untrackedStorage();
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isComponentEnabled(java.lang.String componentName) {
        return this.mEnabledComponentsWatched != null && this.mEnabledComponentsWatched.contains(componentName);
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isComponentDisabled(java.lang.String componentName) {
        return this.mDisabledComponentsWatched != null && this.mDisabledComponentsWatched.contains(componentName);
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public android.content.pm.overlay.OverlayPaths getAllOverlayPaths() {
        if (this.mOverlayPaths == null && this.mSharedLibraryOverlayPaths == null) {
            return null;
        }
        android.content.pm.overlay.OverlayPaths.Builder newPaths = new android.content.pm.overlay.OverlayPaths.Builder();
        newPaths.addAll(this.mOverlayPaths);
        if (this.mSharedLibraryOverlayPaths != null) {
            for (android.content.pm.overlay.OverlayPaths libOverlayPaths : this.mSharedLibraryOverlayPaths.values()) {
                newPaths.addAll(libOverlayPaths);
            }
        }
        return newPaths.build();
    }

    public boolean overrideLabelAndIcon(android.content.ComponentName component, java.lang.String nonLocalizedLabel, java.lang.Integer icon) {
        android.util.Pair<java.lang.String, java.lang.Integer> pair;
        java.lang.String existingLabel = null;
        java.lang.Integer existingIcon = null;
        if (this.mComponentLabelIconOverrideMap != null && (pair = this.mComponentLabelIconOverrideMap.get(component)) != null) {
            existingLabel = (java.lang.String) pair.first;
            existingIcon = (java.lang.Integer) pair.second;
        }
        boolean changed = (android.text.TextUtils.equals(existingLabel, nonLocalizedLabel) && java.util.Objects.equals(existingIcon, icon)) ? false : true;
        if (changed) {
            if (nonLocalizedLabel == null && icon == null) {
                this.mComponentLabelIconOverrideMap.remove(component);
                if (this.mComponentLabelIconOverrideMap.isEmpty()) {
                    this.mComponentLabelIconOverrideMap = null;
                }
            } else {
                if (this.mComponentLabelIconOverrideMap == null) {
                    this.mComponentLabelIconOverrideMap = new com.android.server.utils.WatchedArrayMap<>(1);
                    this.mComponentLabelIconOverrideMap.registerObserver(this.mSnapshot);
                }
                this.mComponentLabelIconOverrideMap.put(component, android.util.Pair.create(nonLocalizedLabel, icon));
            }
            onChanged();
        }
        return changed;
    }

    public void resetOverrideComponentLabelIcon() {
        this.mComponentLabelIconOverrideMap = null;
    }

    @Override // com.android.server.pm.pkg.PackageUserStateInternal
    public android.util.Pair<java.lang.String, java.lang.Integer> getOverrideLabelIconForComponent(android.content.ComponentName componentName) {
        if (com.android.internal.util.ArrayUtils.isEmpty(this.mComponentLabelIconOverrideMap)) {
            return null;
        }
        return this.mComponentLabelIconOverrideMap.get(componentName);
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isSuspended() {
        return !com.android.internal.util.CollectionUtils.isEmpty(this.mSuspendParams);
    }

    public com.android.server.pm.pkg.PackageUserStateImpl putSuspendParams(android.content.pm.UserPackage suspendingPackage, com.android.server.pm.pkg.SuspendParams suspendParams) {
        if (this.mSuspendParams == null) {
            this.mSuspendParams = new com.android.server.utils.WatchedArrayMap<>();
            this.mSuspendParams.registerObserver(this.mSnapshot);
        }
        if (!this.mSuspendParams.containsKey(suspendingPackage) || !java.util.Objects.equals(this.mSuspendParams.get(suspendingPackage), suspendParams)) {
            this.mSuspendParams.put(suspendingPackage, suspendParams);
            onChanged();
        }
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl removeSuspension(android.content.pm.UserPackage suspendingPackage) {
        if (this.mSuspendParams != null) {
            this.mSuspendParams.remove(suspendingPackage);
            onChanged();
        }
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setDisabledComponents(android.util.ArraySet<java.lang.String> value) {
        if (this.mDisabledComponentsWatched == null) {
            this.mDisabledComponentsWatched = new com.android.server.utils.WatchedArraySet<>();
            this.mDisabledComponentsWatched.registerObserver(this.mSnapshot);
        }
        this.mDisabledComponentsWatched.clear();
        if (value != null) {
            this.mDisabledComponentsWatched.addAll(value);
        }
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setEnabledComponents(android.util.ArraySet<java.lang.String> value) {
        if (this.mEnabledComponentsWatched == null) {
            this.mEnabledComponentsWatched = new com.android.server.utils.WatchedArraySet<>();
            this.mEnabledComponentsWatched.registerObserver(this.mSnapshot);
        }
        this.mEnabledComponentsWatched.clear();
        if (value != null) {
            this.mEnabledComponentsWatched.addAll(value);
        }
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setEnabledComponents(com.android.server.utils.WatchedArraySet<java.lang.String> value) {
        this.mEnabledComponentsWatched = value;
        if (this.mEnabledComponentsWatched != null) {
            this.mEnabledComponentsWatched.registerObserver(this.mSnapshot);
        }
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setDisabledComponents(com.android.server.utils.WatchedArraySet<java.lang.String> value) {
        this.mDisabledComponentsWatched = value;
        if (this.mDisabledComponentsWatched != null) {
            this.mDisabledComponentsWatched.registerObserver(this.mSnapshot);
        }
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setCeDataInode(long value) {
        this.mCeDataInode = value;
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setDeDataInode(long value) {
        this.mDeDataInode = value;
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setInstalled(boolean value) {
        setBoolean(1, value);
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setStopped(boolean value) {
        setBoolean(2, value);
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setNotLaunched(boolean value) {
        setBoolean(4, value);
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setHidden(boolean value) {
        setBoolean(8, value);
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setDistractionFlags(int value) {
        this.mDistractionFlags = value;
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setInstantApp(boolean value) {
        setBoolean(16, value);
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setVirtualPreload(boolean value) {
        setBoolean(32, value);
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setEnabledState(int value) {
        this.mEnabledState = value;
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setInstallReason(int value) {
        this.mInstallReason = value;
        com.android.internal.util.AnnotationValidations.validate(android.content.pm.PackageManager.InstallReason.class, (java.lang.annotation.Annotation) null, this.mInstallReason);
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setUninstallReason(int value) {
        this.mUninstallReason = value;
        com.android.internal.util.AnnotationValidations.validate(android.content.pm.PackageManager.UninstallReason.class, (java.lang.annotation.Annotation) null, this.mUninstallReason);
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setHarmfulAppWarning(java.lang.String value) {
        this.mHarmfulAppWarning = value;
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setLastDisableAppCaller(java.lang.String value) {
        this.mLastDisableAppCaller = value;
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setSharedLibraryOverlayPaths(android.util.ArrayMap<java.lang.String, android.content.pm.overlay.OverlayPaths> value) {
        if (value == null) {
            return this;
        }
        if (this.mSharedLibraryOverlayPaths == null) {
            this.mSharedLibraryOverlayPaths = new com.android.server.utils.WatchedArrayMap<>();
            registerObserver(this.mSnapshot);
        }
        this.mSharedLibraryOverlayPaths.clear();
        this.mSharedLibraryOverlayPaths.putAll(value);
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setSplashScreenTheme(java.lang.String value) {
        this.mSplashScreenTheme = value;
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setMinAspectRatio(int value) {
        this.mMinAspectRatio = value;
        com.android.internal.util.AnnotationValidations.validate(android.content.pm.PackageManager.UserMinAspectRatio.class, (java.lang.annotation.Annotation) null, this.mMinAspectRatio);
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setSuspendParams(android.util.ArrayMap<android.content.pm.UserPackage, com.android.server.pm.pkg.SuspendParams> value) {
        if (value == null) {
            return this;
        }
        if (this.mSuspendParams == null) {
            this.mSuspendParams = new com.android.server.utils.WatchedArrayMap<>();
            registerObserver(this.mSnapshot);
        }
        this.mSuspendParams.clear();
        this.mSuspendParams.putAll(value);
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setComponentLabelIconOverrideMap(android.util.ArrayMap<android.content.ComponentName, android.util.Pair<java.lang.String, java.lang.Integer>> value) {
        if (value == null) {
            return this;
        }
        if (this.mComponentLabelIconOverrideMap == null) {
            this.mComponentLabelIconOverrideMap = new com.android.server.utils.WatchedArrayMap<>();
            registerObserver(this.mSnapshot);
        }
        this.mComponentLabelIconOverrideMap.clear();
        this.mComponentLabelIconOverrideMap.putAll(value);
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setFirstInstallTimeMillis(long value) {
        this.mFirstInstallTimeMillis = value;
        onChanged();
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setArchiveState(com.android.server.pm.pkg.ArchiveState archiveState) {
        this.mArchiveState = archiveState;
        onChanged();
        return this;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public java.util.Map<java.lang.String, android.content.pm.overlay.OverlayPaths> getSharedLibraryOverlayPaths() {
        return this.mSharedLibraryOverlayPaths == null ? java.util.Collections.emptyMap() : this.mSharedLibraryOverlayPaths;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setWatchable(com.android.server.utils.Watchable watchable) {
        this.mWatchable = watchable;
        return this;
    }

    private boolean watchableEquals(com.android.server.utils.Watchable other) {
        return true;
    }

    private int watchableHashCode() {
        return 0;
    }

    private boolean snapshotEquals(com.android.server.utils.SnapshotCache<com.android.server.pm.pkg.PackageUserStateImpl> other) {
        return true;
    }

    private int snapshotHashCode() {
        return 0;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isInstalled() {
        return getBoolean(1);
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isStopped() {
        return getBoolean(2);
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isNotLaunched() {
        return getBoolean(4);
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isHidden() {
        return getBoolean(8);
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isInstantApp() {
        return getBoolean(16);
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isVirtualPreload() {
        return getBoolean(32);
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean isQuarantined() {
        if (!isSuspended()) {
            return false;
        }
        com.android.server.utils.WatchedArrayMap<android.content.pm.UserPackage, com.android.server.pm.pkg.SuspendParams> suspendParams = this.mSuspendParams;
        int size = suspendParams.size();
        for (int i = 0; i < size; i++) {
            com.android.server.pm.pkg.SuspendParams params = suspendParams.valueAt(i);
            if (params.isQuarantined()) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public boolean dataExists() {
        return getCeDataInode() > 0 || getDeDataInode() > 0;
    }

    public com.android.server.utils.WatchedArraySet<java.lang.String> getDisabledComponentsWatched() {
        return this.mDisabledComponentsWatched;
    }

    public com.android.server.utils.WatchedArraySet<java.lang.String> getEnabledComponentsWatched() {
        return this.mEnabledComponentsWatched;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public long getCeDataInode() {
        return this.mCeDataInode;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public long getDeDataInode() {
        return this.mDeDataInode;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public int getDistractionFlags() {
        return this.mDistractionFlags;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public int getEnabledState() {
        return this.mEnabledState;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public int getInstallReason() {
        return this.mInstallReason;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public int getUninstallReason() {
        return this.mUninstallReason;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public java.lang.String getHarmfulAppWarning() {
        return this.mHarmfulAppWarning;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public java.lang.String getLastDisableAppCaller() {
        return this.mLastDisableAppCaller;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public android.content.pm.overlay.OverlayPaths getOverlayPaths() {
        return this.mOverlayPaths;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public java.lang.String getSplashScreenTheme() {
        return this.mSplashScreenTheme;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public int getMinAspectRatio() {
        return this.mMinAspectRatio;
    }

    @Override // com.android.server.pm.pkg.PackageUserStateInternal
    public com.android.server.utils.WatchedArrayMap<android.content.pm.UserPackage, com.android.server.pm.pkg.SuspendParams> getSuspendParams() {
        return this.mSuspendParams;
    }

    public com.android.server.utils.WatchedArrayMap<android.content.ComponentName, android.util.Pair<java.lang.String, java.lang.Integer>> getComponentLabelIconOverrideMap() {
        return this.mComponentLabelIconOverrideMap;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public long getFirstInstallTimeMillis() {
        return this.mFirstInstallTimeMillis;
    }

    @Override // com.android.server.pm.pkg.PackageUserState
    public com.android.server.pm.pkg.ArchiveState getArchiveState() {
        return this.mArchiveState;
    }

    public com.android.server.utils.SnapshotCache<com.android.server.pm.pkg.PackageUserStateImpl> getSnapshot() {
        return this.mSnapshot;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setBooleans(int value) {
        this.mBooleans = value;
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setDisabledComponentsWatched(com.android.server.utils.WatchedArraySet<java.lang.String> value) {
        this.mDisabledComponentsWatched = value;
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setEnabledComponentsWatched(com.android.server.utils.WatchedArraySet<java.lang.String> value) {
        this.mEnabledComponentsWatched = value;
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setSharedLibraryOverlayPaths(com.android.server.utils.WatchedArrayMap<java.lang.String, android.content.pm.overlay.OverlayPaths> value) {
        this.mSharedLibraryOverlayPaths = value;
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setSuspendParams(com.android.server.utils.WatchedArrayMap<android.content.pm.UserPackage, com.android.server.pm.pkg.SuspendParams> value) {
        this.mSuspendParams = value;
        return this;
    }

    public com.android.server.pm.pkg.PackageUserStateImpl setComponentLabelIconOverrideMap(com.android.server.utils.WatchedArrayMap<android.content.ComponentName, android.util.Pair<java.lang.String, java.lang.Integer>> value) {
        this.mComponentLabelIconOverrideMap = value;
        return this;
    }

    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        com.android.server.pm.pkg.PackageUserStateImpl that = (com.android.server.pm.pkg.PackageUserStateImpl) o;
        if (this.mBooleans == that.mBooleans && java.util.Objects.equals(this.mDisabledComponentsWatched, that.mDisabledComponentsWatched) && java.util.Objects.equals(this.mEnabledComponentsWatched, that.mEnabledComponentsWatched) && this.mCeDataInode == that.mCeDataInode && this.mDeDataInode == that.mDeDataInode && this.mDistractionFlags == that.mDistractionFlags && this.mEnabledState == that.mEnabledState && this.mInstallReason == that.mInstallReason && this.mUninstallReason == that.mUninstallReason && java.util.Objects.equals(this.mHarmfulAppWarning, that.mHarmfulAppWarning) && java.util.Objects.equals(this.mLastDisableAppCaller, that.mLastDisableAppCaller) && java.util.Objects.equals(this.mOverlayPaths, that.mOverlayPaths) && java.util.Objects.equals(this.mSharedLibraryOverlayPaths, that.mSharedLibraryOverlayPaths) && java.util.Objects.equals(this.mSplashScreenTheme, that.mSplashScreenTheme) && this.mMinAspectRatio == that.mMinAspectRatio && java.util.Objects.equals(this.mSuspendParams, that.mSuspendParams) && java.util.Objects.equals(this.mComponentLabelIconOverrideMap, that.mComponentLabelIconOverrideMap) && this.mFirstInstallTimeMillis == that.mFirstInstallTimeMillis && watchableEquals(that.mWatchable) && java.util.Objects.equals(this.mArchiveState, that.mArchiveState) && snapshotEquals(that.mSnapshot)) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        int _hash = (1 * 31) + this.mBooleans;
        return (((((((((((((((((((((((((((((((((((((((_hash * 31) + java.util.Objects.hashCode(this.mDisabledComponentsWatched)) * 31) + java.util.Objects.hashCode(this.mEnabledComponentsWatched)) * 31) + java.lang.Long.hashCode(this.mCeDataInode)) * 31) + java.lang.Long.hashCode(this.mDeDataInode)) * 31) + this.mDistractionFlags) * 31) + this.mEnabledState) * 31) + this.mInstallReason) * 31) + this.mUninstallReason) * 31) + java.util.Objects.hashCode(this.mHarmfulAppWarning)) * 31) + java.util.Objects.hashCode(this.mLastDisableAppCaller)) * 31) + java.util.Objects.hashCode(this.mOverlayPaths)) * 31) + java.util.Objects.hashCode(this.mSharedLibraryOverlayPaths)) * 31) + java.util.Objects.hashCode(this.mSplashScreenTheme)) * 31) + this.mMinAspectRatio) * 31) + java.util.Objects.hashCode(this.mSuspendParams)) * 31) + java.util.Objects.hashCode(this.mComponentLabelIconOverrideMap)) * 31) + java.lang.Long.hashCode(this.mFirstInstallTimeMillis)) * 31) + watchableHashCode()) * 31) + java.util.Objects.hashCode(this.mArchiveState)) * 31) + snapshotHashCode();
    }

    @java.lang.Deprecated
    private void __metadata() {
    }
}
