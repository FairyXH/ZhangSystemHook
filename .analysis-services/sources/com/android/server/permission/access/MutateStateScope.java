package com.android.server.permission.access;

/* JADX INFO: compiled from: AccessState.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u000b"}, d2 = {"Lcom/android/server/permission/access/MutateStateScope;", "Lcom/android/server/permission/access/GetStateScope;", "oldState", "Lcom/android/server/permission/access/AccessState;", "newState", "Lcom/android/server/permission/access/MutableAccessState;", "(Lcom/android/server/permission/access/AccessState;Lcom/android/server/permission/access/MutableAccessState;)V", "getNewState", "()Lcom/android/server/permission/access/MutableAccessState;", "getOldState", "()Lcom/android/server/permission/access/AccessState;", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MutateStateScope extends com.android.server.permission.access.GetStateScope {
    private final com.android.server.permission.access.MutableAccessState newState;
    private final com.android.server.permission.access.AccessState oldState;

    public final com.android.server.permission.access.MutableAccessState getNewState() {
        return this.newState;
    }

    public final com.android.server.permission.access.AccessState getOldState() {
        return this.oldState;
    }

    public MutateStateScope(com.android.server.permission.access.AccessState oldState, com.android.server.permission.access.MutableAccessState newState) {
        super(newState);
        this.oldState = oldState;
        this.newState = newState;
    }
}
