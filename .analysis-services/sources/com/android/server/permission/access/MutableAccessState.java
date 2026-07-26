package com.android.server.permission.access;

/* JADX INFO: compiled from: AccessState.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0007\b\u0016¢\u0006\u0002\u0010\u0002B\u000f\b\u0010\u0012\u0006\u0010\u0003\u001a\u00020\u0001¢\u0006\u0002\u0010\u0004Bo\b\u0002\u0012\u0016\u0010\u0005\u001a\u0012\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006j\u0002`\t\u0012\u0016\u0010\n\u001a\u0012\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\f0\u0006j\u0002`\r\u00126\u0010\u000e\u001a2\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00110\u000fj\u0002`\u0012\u0012\u0014\u0012\u0012\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00110\u0013j\u0002`\u00140\u0006j\u0002`\u0015¢\u0006\u0002\u0010\u0016J\u0006\u0010\u0017\u001a\u00020\bJ\u0010\u0010\u0018\u001a\u00020\f2\b\b\u0002\u0010\u0019\u001a\u00020\u001aJ\u001a\u0010\u001b\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u001c\u001a\u00020\u001a2\b\b\u0002\u0010\u0019\u001a\u00020\u001aJ\u0018\u0010\u001d\u001a\u00020\u00112\u0006\u0010\u001e\u001a\u00020\u001a2\b\b\u0002\u0010\u0019\u001a\u00020\u001aJ\u0016\u0010\u001f\u001a\u0012\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00110\u0013j\u0002`\u0014¨\u0006 "}, d2 = {"Lcom/android/server/permission/access/MutableAccessState;", "Lcom/android/server/permission/access/AccessState;", "()V", "accessState", "(Lcom/android/server/permission/access/AccessState;)V", "externalStateReference", "Lcom/android/server/permission/access/immutable/MutableReference;", "Lcom/android/server/permission/access/ExternalState;", "Lcom/android/server/permission/access/MutableExternalState;", "Lcom/android/server/permission/access/ExternalStateReference;", "systemStateReference", "Lcom/android/server/permission/access/SystemState;", "Lcom/android/server/permission/access/MutableSystemState;", "Lcom/android/server/permission/access/SystemStateReference;", "userStatesReference", "Lcom/android/server/permission/access/immutable/IntReferenceMap;", "Lcom/android/server/permission/access/UserState;", "Lcom/android/server/permission/access/MutableUserState;", "Lcom/android/server/permission/access/UserStates;", "Lcom/android/server/permission/access/immutable/MutableIntReferenceMap;", "Lcom/android/server/permission/access/MutableUserStates;", "Lcom/android/server/permission/access/UserStatesReference;", "(Lcom/android/server/permission/access/immutable/MutableReference;Lcom/android/server/permission/access/immutable/MutableReference;Lcom/android/server/permission/access/immutable/MutableReference;)V", "mutateExternalState", "mutateSystemState", "writeMode", "", "mutateUserState", "userId", "mutateUserStateAt", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "mutateUserStatesNoWrite", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MutableAccessState extends com.android.server.permission.access.AccessState {
    private MutableAccessState(com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.ExternalState, com.android.server.permission.access.MutableExternalState> mutableReference, com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.SystemState, com.android.server.permission.access.MutableSystemState> mutableReference2, com.android.server.permission.access.immutable.MutableReference<com.android.server.permission.access.immutable.IntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState>, com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState>> mutableReference3) {
        super(mutableReference, mutableReference2, mutableReference3, null);
    }

    public MutableAccessState() {
        this(new com.android.server.permission.access.immutable.MutableReference(new com.android.server.permission.access.MutableExternalState()), new com.android.server.permission.access.immutable.MutableReference(new com.android.server.permission.access.MutableSystemState()), new com.android.server.permission.access.immutable.MutableReference(new com.android.server.permission.access.immutable.MutableIntReferenceMap(null, 1, null)));
    }

    public MutableAccessState(com.android.server.permission.access.AccessState accessState) {
        this(accessState.getExternalStateReference$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().toImmutable(), accessState.getSystemStateReference$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().toImmutable(), accessState.getUserStatesReference$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().toImmutable());
    }

    public final com.android.server.permission.access.MutableExternalState mutateExternalState() {
        return (com.android.server.permission.access.MutableExternalState) getExternalStateReference$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().mutate();
    }

    public static /* synthetic */ com.android.server.permission.access.MutableSystemState mutateSystemState$default(com.android.server.permission.access.MutableAccessState mutableAccessState, int i, int i2, java.lang.Object obj) {
        if ((i2 & 1) != 0) {
            i = 1;
        }
        return mutableAccessState.mutateSystemState(i);
    }

    public final com.android.server.permission.access.MutableSystemState mutateSystemState(int writeMode) {
        com.android.server.permission.access.immutable.Immutable immutableMutate = getSystemStateReference$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().mutate();
        com.android.server.permission.access.MutableSystemState $this$mutateSystemState_u24lambda_u240 = (com.android.server.permission.access.MutableSystemState) immutableMutate;
        $this$mutateSystemState_u24lambda_u240.requestWriteMode(writeMode);
        return (com.android.server.permission.access.MutableSystemState) immutableMutate;
    }

    public final com.android.server.permission.access.immutable.MutableIntReferenceMap<com.android.server.permission.access.UserState, com.android.server.permission.access.MutableUserState> mutateUserStatesNoWrite() {
        return (com.android.server.permission.access.immutable.MutableIntReferenceMap) getUserStatesReference$frameworks__base__services__permission__android_common__services_permission_pre_jarjar().mutate();
    }

    public static /* synthetic */ com.android.server.permission.access.MutableUserState mutateUserState$default(com.android.server.permission.access.MutableAccessState mutableAccessState, int i, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 1;
        }
        return mutableAccessState.mutateUserState(i, i2);
    }

    public final com.android.server.permission.access.MutableUserState mutateUserState(int userId, int writeMode) {
        com.android.server.permission.access.MutableUserState $this$mutateUserState_u24lambda_u241 = (com.android.server.permission.access.MutableUserState) mutateUserStatesNoWrite().mutate(userId);
        if ($this$mutateUserState_u24lambda_u241 == null) {
            return null;
        }
        $this$mutateUserState_u24lambda_u241.requestWriteMode(writeMode);
        return $this$mutateUserState_u24lambda_u241;
    }

    public static /* synthetic */ com.android.server.permission.access.MutableUserState mutateUserStateAt$default(com.android.server.permission.access.MutableAccessState mutableAccessState, int i, int i2, int i3, java.lang.Object obj) {
        if ((i3 & 2) != 0) {
            i2 = 1;
        }
        return mutableAccessState.mutateUserStateAt(i, i2);
    }

    public final com.android.server.permission.access.MutableUserState mutateUserStateAt(int index, int writeMode) {
        com.android.server.permission.access.immutable.Immutable immutableMutateAt = mutateUserStatesNoWrite().mutateAt(index);
        com.android.server.permission.access.MutableUserState $this$mutateUserStateAt_u24lambda_u242 = (com.android.server.permission.access.MutableUserState) immutableMutateAt;
        $this$mutateUserStateAt_u24lambda_u242.requestWriteMode(writeMode);
        return (com.android.server.permission.access.MutableUserState) immutableMutateAt;
    }
}
