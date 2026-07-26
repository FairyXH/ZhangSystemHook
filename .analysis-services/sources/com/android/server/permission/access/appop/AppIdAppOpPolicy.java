package com.android.server.permission.access.appop;

/* JADX INFO: compiled from: AppIdAppOpPolicy.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 *2\u00020\u0001:\u0002*+B\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0007J\u0018\u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0017H\u0016J\u000e\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0007J\"\u0010\u0019\u001a\u00020\u0017*\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u000bJ(\u0010\u001d\u001a\u0010\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\u0017\u0018\u00010\u001e*\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u0016\u001a\u00020\u0017J\u0014\u0010\u001f\u001a\u00020\u0011*\u00020 2\u0006\u0010\u001b\u001a\u00020\u0017H\u0016J\f\u0010!\u001a\u00020\u0011*\u00020\u001aH\u0016J\u001a\u0010\"\u001a\u00020#*\u00020 2\u0006\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u0016\u001a\u00020\u0017J*\u0010$\u001a\u00020#*\u00020 2\u0006\u0010\u001b\u001a\u00020\u00172\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u001c\u001a\u00020\u000b2\u0006\u0010%\u001a\u00020\u0017J$\u0010&\u001a\u00020\u0011*\u00020 2\u0006\u0010'\u001a\u00020(2\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010)\u001a\u00020\u0017H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u000b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006,"}, d2 = {"Lcom/android/server/permission/access/appop/AppIdAppOpPolicy;", "Lcom/android/server/permission/access/appop/BaseAppOpPolicy;", "()V", "migration", "Lcom/android/server/permission/access/appop/AppIdAppOpMigration;", "onAppOpModeChangedListeners", "Lcom/android/server/permission/access/immutable/IndexedListSet;", "Lcom/android/server/permission/access/appop/AppIdAppOpPolicy$OnAppOpModeChangedListener;", "onAppOpModeChangedListenersLock", "", "subjectScheme", "", "getSubjectScheme", "()Ljava/lang/String;", "upgrade", "Lcom/android/server/permission/access/appop/AppIdAppOpUpgrade;", "addOnAppOpModeChangedListener", "", "listener", "migrateUserState", "state", "Lcom/android/server/permission/access/MutableAccessState;", "userId", "", "removeOnAppOpModeChangedListener", "getAppOpMode", "Lcom/android/server/permission/access/GetStateScope;", "appId", "appOpName", "getAppOpModes", "Lcom/android/server/permission/access/immutable/IndexedMap;", "onAppIdRemoved", "Lcom/android/server/permission/access/MutateStateScope;", "onStateMutated", "removeAppOpModes", "", "setAppOpMode", com.android.server.app.GameManagerService.GamePackageConfiguration.GameModeConfiguration.MODE_KEY, "upgradePackageState", "packageState", "Lcom/android/server/pm/pkg/PackageState;", "version", "Companion", "OnAppOpModeChangedListener", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AppIdAppOpPolicy extends com.android.server.permission.access.appop.BaseAppOpPolicy {
    public static final com.android.server.permission.access.appop.AppIdAppOpPolicy.Companion Companion = new com.android.server.permission.access.appop.AppIdAppOpPolicy.Companion(null);
    private static final java.lang.String LOG_TAG = com.android.server.permission.access.appop.AppIdAppOpPolicy.class.getSimpleName();
    private final com.android.server.permission.access.appop.AppIdAppOpMigration migration;
    private volatile com.android.server.permission.access.immutable.IndexedListSet<com.android.server.permission.access.appop.AppIdAppOpPolicy.OnAppOpModeChangedListener> onAppOpModeChangedListeners;
    private final java.lang.Object onAppOpModeChangedListenersLock;
    private final com.android.server.permission.access.appop.AppIdAppOpUpgrade upgrade;

    /* JADX INFO: compiled from: AppIdAppOpPolicy.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\b&\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J0\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0006H&J\b\u0010\f\u001a\u00020\u0004H&¨\u0006\r"}, d2 = {"Lcom/android/server/permission/access/appop/AppIdAppOpPolicy$OnAppOpModeChangedListener;", "", "()V", "onAppOpModeChanged", "", "appId", "", "userId", "appOpName", "", "oldMode", "newMode", "onStateMutated", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static abstract class OnAppOpModeChangedListener {
        public abstract void onAppOpModeChanged(int i, int i2, java.lang.String str, int i3, int i4);

        public abstract void onStateMutated();
    }

    public AppIdAppOpPolicy() {
        super(new com.android.server.permission.access.appop.AppIdAppOpPersistence());
        this.migration = new com.android.server.permission.access.appop.AppIdAppOpMigration();
        this.upgrade = new com.android.server.permission.access.appop.AppIdAppOpUpgrade(this);
        this.onAppOpModeChangedListeners = new com.android.server.permission.access.immutable.MutableIndexedListSet(null, 1, null);
        this.onAppOpModeChangedListenersLock = new java.lang.Object();
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public java.lang.String getSubjectScheme() {
        return "uid";
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onStateMutated(com.android.server.permission.access.GetStateScope $this$onStateMutated) {
        com.android.server.permission.access.immutable.IndexedListSet<com.android.server.permission.access.appop.AppIdAppOpPolicy.OnAppOpModeChangedListener> indexedListSet = this.onAppOpModeChangedListeners;
        int size = indexedListSet.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            com.android.server.permission.access.appop.AppIdAppOpPolicy.OnAppOpModeChangedListener it = indexedListSet.elementAt(index$iv);
            it.onStateMutated();
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void onAppIdRemoved(com.android.server.permission.access.MutateStateScope $this$onAppIdRemoved, int appId) {
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> userStates = $this$onAppIdRemoved.getNewState().getUserStates();
        int size = userStates.getSize();
        for (int index$iv = 0; index$iv < size; index$iv++) {
            userStates.keyAt(index$iv);
            com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) userStates.valueAt(index$iv);
            int userStateIndex = index$iv;
            int appIdIndex = userState.getAppIdAppOpModes().indexOfKey(appId);
            if (appIdIndex >= 0) {
                com.android.server.permission.access.MutableAccessState.mutateUserStateAt$default($this$onAppIdRemoved.getNewState(), userStateIndex, 0, 2, null).mutateAppIdAppOpModes().removeAt(appIdIndex);
            }
        }
    }

    public final com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer> getAppOpModes(com.android.server.permission.access.GetStateScope $this$getAppOpModes, int appId, int userId) {
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> appIdAppOpModes;
        com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) $this$getAppOpModes.getState().getUserStates().get(userId);
        if (userState == null || (appIdAppOpModes = userState.getAppIdAppOpModes()) == null) {
            return null;
        }
        return (com.android.server.permission.access.immutable.IndexedMap) appIdAppOpModes.get(appId);
    }

    public final boolean removeAppOpModes(com.android.server.permission.access.MutateStateScope $this$removeAppOpModes, int appId, int userId) {
        int appIdIndex;
        int userStateIndex = $this$removeAppOpModes.getNewState().getUserStates().indexOfKey(userId);
        if (userStateIndex >= 0 && (appIdIndex = ((com.android.server.permission.access.UserState) $this$removeAppOpModes.getNewState().getUserStates().valueAt(userStateIndex)).getAppIdAppOpModes().indexOfKey(appId)) >= 0) {
            com.android.server.permission.access.MutableAccessState.mutateUserStateAt$default($this$removeAppOpModes.getNewState(), userStateIndex, 0, 2, null).mutateAppIdAppOpModes().removeAt(appIdIndex);
            return true;
        }
        return false;
    }

    public final int getAppOpMode(com.android.server.permission.access.GetStateScope $this$getAppOpMode, int appId, int userId, java.lang.String appOpName) {
        com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> appIdAppOpModes;
        com.android.server.permission.access.UserState userState = (com.android.server.permission.access.UserState) $this$getAppOpMode.getState().getUserStates().get(userId);
        return ((java.lang.Number) com.android.server.permission.access.immutable.IndexedMapExtensionsKt.getWithDefault((userState == null || (appIdAppOpModes = userState.getAppIdAppOpModes()) == null) ? null : (com.android.server.permission.access.immutable.IndexedMap) appIdAppOpModes.get(appId), appOpName, java.lang.Integer.valueOf(android.app.AppOpsManager.opToDefaultMode(appOpName)))).intValue();
    }

    public final boolean setAppOpMode(com.android.server.permission.access.MutateStateScope $this$setAppOpMode, int appId, int userId, java.lang.String appOpName, int mode) {
        if (!$this$setAppOpMode.getNewState().getUserStates().contains(userId)) {
            android.util.Slog.e(LOG_TAG, "Unable to set app op mode for missing user " + userId);
            return false;
        }
        int defaultMode = android.app.AppOpsManager.opToDefaultMode(appOpName);
        com.android.server.permission.access.immutable.Immutable immutable = $this$setAppOpMode.getNewState().getUserStates().get(userId);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(immutable);
        int oldMode = ((java.lang.Number) com.android.server.permission.access.immutable.IndexedMapExtensionsKt.getWithDefault((com.android.server.permission.access.immutable.IndexedMap) ((com.android.server.permission.access.UserState) immutable).getAppIdAppOpModes().get(appId), appOpName, java.lang.Integer.valueOf(defaultMode))).intValue();
        if (oldMode == mode) {
            return false;
        }
        com.android.server.permission.access.MutableUserState mutableUserStateMutateUserState$default = com.android.server.permission.access.MutableAccessState.mutateUserState$default($this$setAppOpMode.getNewState(), userId, 0, 2, null);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(mutableUserStateMutateUserState$default);
        com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.immutable.IndexedMap<java.lang.String, java.lang.Integer>, com.android.server.permission.access.immutable.MutableIndexedMap<java.lang.String, java.lang.Integer>> mutableIntReferenceMapMutateAppIdAppOpModes = mutableUserStateMutateUserState$default.mutateAppIdAppOpModes();
        com.android.server.permission.access.immutable.Immutable immutableMutate = mutableIntReferenceMapMutateAppIdAppOpModes.mutate(appId);
        if (immutableMutate == null) {
            com.android.server.permission.access.immutable.Immutable mutableIndexedMap = new com.android.server.permission.access.immutable.MutableIndexedMap(null, 1, null);
            com.android.server.permission.access.immutable.Immutable it$iv = mutableIndexedMap;
            mutableIntReferenceMapMutateAppIdAppOpModes.put(appId, it$iv);
            immutableMutate = mutableIndexedMap;
        }
        com.android.server.permission.access.immutable.MutableIndexedMap appOpModes = (com.android.server.permission.access.immutable.MutableIndexedMap) immutableMutate;
        com.android.server.permission.access.immutable.IndexedMapExtensionsKt.putWithDefault(appOpModes, appOpName, java.lang.Integer.valueOf(mode), java.lang.Integer.valueOf(defaultMode));
        if (appOpModes.isEmpty()) {
            com.android.server.permission.access.immutable.IntReferenceMapExtensionsKt.minusAssign(mutableIntReferenceMapMutateAppIdAppOpModes, appId);
        }
        com.android.server.permission.access.immutable.IndexedListSet<com.android.server.permission.access.appop.AppIdAppOpPolicy.OnAppOpModeChangedListener> indexedListSet = this.onAppOpModeChangedListeners;
        int size = indexedListSet.getSize();
        int index$iv = 0;
        while (index$iv < size) {
            com.android.server.permission.access.appop.AppIdAppOpPolicy.OnAppOpModeChangedListener it = indexedListSet.elementAt(index$iv);
            it.onAppOpModeChanged(appId, userId, appOpName, oldMode, mode);
            index$iv++;
            size = size;
            indexedListSet = indexedListSet;
        }
        return true;
    }

    public final void addOnAppOpModeChangedListener(com.android.server.permission.access.appop.AppIdAppOpPolicy.OnAppOpModeChangedListener listener) {
        synchronized (this.onAppOpModeChangedListenersLock) {
            this.onAppOpModeChangedListeners = com.android.server.permission.access.immutable.IndexedListSetExtensionsKt.plus(this.onAppOpModeChangedListeners, listener);
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    public final void removeOnAppOpModeChangedListener(com.android.server.permission.access.appop.AppIdAppOpPolicy.OnAppOpModeChangedListener listener) {
        synchronized (this.onAppOpModeChangedListenersLock) {
            this.onAppOpModeChangedListeners = com.android.server.permission.access.immutable.IndexedListSetExtensionsKt.minus(this.onAppOpModeChangedListeners, listener);
            com.android.server.permission.jarjar.kotlin.Unit unit = com.android.server.permission.jarjar.kotlin.Unit.INSTANCE;
        }
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void migrateUserState(com.android.server.permission.access.MutableAccessState state, int userId) {
        com.android.server.permission.access.appop.AppIdAppOpMigration $this$migrateUserState_u24lambda_u246 = this.migration;
        $this$migrateUserState_u24lambda_u246.migrateUserState(state, userId);
    }

    @Override // com.android.server.permission.access.SchemePolicy
    public void upgradePackageState(com.android.server.permission.access.MutateStateScope $this$upgradePackageState, com.android.server.pm.pkg.PackageState packageState, int userId, int version) {
        com.android.server.permission.access.appop.AppIdAppOpUpgrade $this$upgradePackageState_u24lambda_u247 = this.upgrade;
        $this$upgradePackageState_u24lambda_u247.upgradePackageState($this$upgradePackageState, packageState, userId, version);
    }

    /* JADX INFO: compiled from: AppIdAppOpPolicy.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\n \u0005*\u0004\u0018\u00010\u00040\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lcom/android/server/permission/access/appop/AppIdAppOpPolicy$Companion;", "", "()V", "LOG_TAG", "", "com.android.server.permission.jarjar.kotlin.jvm.PlatformType", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
