package com.android.server.permission.access;

/* JADX INFO: compiled from: AccessState.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\b6\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001Bo\b\u0004\u0012\u0016\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004j\u0002`\u0007\u0012\u0016\u0010\b\u001a\u0012\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u0004j\u0002`\u000b\u00126\u0010\f\u001a2\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f0\rj\u0002`\u0010\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f0\u0011j\u0002`\u00120\u0004j\u0002`\u0013¢\u0006\u0002\u0010\u0014J\b\u0010\"\u001a\u00020\u0002H\u0016R\u0011\u0010\u0015\u001a\u00020\u00058F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R$\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\u00060\u0004j\u0002`\u0007X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u001a\u001a\u00020\t8F¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u001cR$\u0010\b\u001a\u0012\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\n0\u0004j\u0002`\u000bX\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0019R!\u0010\u001e\u001a\u0012\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f0\rj\u0002`\u00108F¢\u0006\u0006\u001a\u0004\b\u001f\u0010 RD\u0010\f\u001a2\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f0\rj\u0002`\u0010\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u000f0\u0011j\u0002`\u00120\u0004j\u0002`\u0013X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0019\u0082\u0001\u0001\u0002¨\u0006#"}, d2 = {"Lcom/android/server/permission/access/AccessState;", "Lcom/android/server/permission/access/immutable/Immutable;", "Lcom/android/server/permission/access/MutableAccessState;", "externalStateReference", "Lcom/android/server/permission/access/immutable/MutableReference;", "Lcom/android/server/permission/access/ExternalState;", "Lcom/android/server/permission/access/MutableExternalState;", "Lcom/android/server/permission/access/ExternalStateReference;", "systemStateReference", "Lcom/android/server/permission/access/SystemState;", "Lcom/android/server/permission/access/MutableSystemState;", "Lcom/android/server/permission/access/SystemStateReference;", "userStatesReference", "Lcom/android/server/permission/access/immutable/IntReferenceMap;", "Lcom/android/server/permission/access/UserState;", "Lcom/android/server/permission/access/MutableUserState;", "Lcom/android/server/permission/access/UserStates;", "Lcom/android/server/permission/access/immutable/MutableIntReferenceMap;", "Lcom/android/server/permission/access/MutableUserStates;", "Lcom/android/server/permission/access/UserStatesReference;", "(Lcom/android/server/permission/access/immutable/MutableReference;Lcom/android/server/permission/access/immutable/MutableReference;Lcom/android/server/permission/access/immutable/MutableReference;)V", "externalState", "getExternalState", "()Lcom/android/server/permission/access/ExternalState;", "getExternalStateReference$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "()Lcom/android/server/permission/access/immutable/MutableReference;", "systemState", "getSystemState", "()Lcom/android/server/permission/access/SystemState;", "getSystemStateReference$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "userStates", "getUserStates", "()Lcom/android/server/permission/access/immutable/IntReferenceMap;", "getUserStatesReference$frameworks__base__services__permission__android_common__services_permission_pre_jarjar", "toMutable", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class AccessState implements com.android.server.permission.access.immutable.Immutable<com.android.server.permission.access.MutableAccessState> {
    private final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.ExternalState, com.android.server.permission.access.MutableExternalState> externalStateReference;
    private final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.SystemState, com.android.server.permission.access.MutableSystemState> systemStateReference;
    private final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState>, com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState>> userStatesReference;

    public /* synthetic */ AccessState(com.android.server.permission.access.immutable.MutableReference mutableReference, com.android.server.permission.access.immutable.MutableReference mutableReference2, com.android.server.permission.access.immutable.MutableReference mutableReference3, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(mutableReference, mutableReference2, mutableReference3);
    }

    private AccessState(com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.ExternalState, com.android.server.permission.access.MutableExternalState> mutableReference, com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.SystemState, com.android.server.permission.access.MutableSystemState> mutableReference2, com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState>, com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState>> mutableReference3) {
        this.externalStateReference = mutableReference;
        this.systemStateReference = mutableReference2;
        this.userStatesReference = mutableReference3;
    }

    public final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.ExternalState, com.android.server.permission.access.MutableExternalState> getExternalStateReference$frameworks__base__services__permission__android_common__services_permission_pre_jarjar() {
        return this.externalStateReference;
    }

    public final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.SystemState, com.android.server.permission.access.MutableSystemState> getSystemStateReference$frameworks__base__services__permission__android_common__services_permission_pre_jarjar() {
        return this.systemStateReference;
    }

    public final com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState>, com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState>> getUserStatesReference$frameworks__base__services__permission__android_common__services_permission_pre_jarjar() {
        return this.userStatesReference;
    }

    public final com.android.server.permission.access.ExternalState getExternalState() {
        return (com.android.server.permission.access.ExternalState) this.externalStateReference.get();
    }

    public final com.android.server.permission.access.SystemState getSystemState() {
        return (com.android.server.permission.access.SystemState) this.systemStateReference.get();
    }

    public final com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> getUserStates() {
        return (com.android.server.permission.access.immutable.IntReferenceMap) this.userStatesReference.get();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.server.permission.access.immutable.Immutable
    public com.android.server.permission.access.MutableAccessState toMutable() {
        return new com.android.server.permission.access.MutableAccessState(this);
    }
}
