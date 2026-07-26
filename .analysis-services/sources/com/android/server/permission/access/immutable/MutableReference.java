package com.android.server.permission.access.immutable;

/* JADX INFO: compiled from: MutableReference.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00030\u0002*\b\b\u0001\u0010\u0003*\u0002H\u00012\u00020\u0004B\u000f\b\u0016\u0012\u0006\u0010\u0005\u001a\u00028\u0001¢\u0006\u0002\u0010\u0006B\u0019\b\u0002\u0012\u0006\u0010\u0007\u001a\u00028\u0000\u0012\b\u0010\u0005\u001a\u0004\u0018\u00018\u0001¢\u0006\u0002\u0010\bJ\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u0004H\u0096\u0002J\u000b\u0010\r\u001a\u00028\u0000¢\u0006\u0002\u0010\u000eJ\b\u0010\u000f\u001a\u00020\u0010H\u0016J\u000b\u0010\u0011\u001a\u00028\u0001¢\u0006\u0002\u0010\u000eJ\u0012\u0010\u0012\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\u0000R\u0010\u0010\u0007\u001a\u00028\u0000X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\tR\u0012\u0010\u0005\u001a\u0004\u0018\u00018\u0001X\u0082\u000e¢\u0006\u0004\n\u0002\u0010\t¨\u0006\u0013"}, d2 = {"Lcom/android/server/permission/access/immutable/MutableReference;", "I", "Lcom/android/server/permission/access/immutable/Immutable;", "M", "", "mutable", "(Lcom/android/server/permission/access/immutable/Immutable;)V", "immutable", "(Lcom/android/server/permission/access/immutable/Immutable;Lcom/android/server/permission/access/immutable/Immutable;)V", "Lcom/android/server/permission/access/immutable/Immutable;", "equals", "", "other", "get", "()Lcom/android/server/permission/access/immutable/Immutable;", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "mutate", "toImmutable", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MutableReference<I extends com.android.server.permission.access.immutable.Immutable<M>, M extends I> {
    private I immutable;

    /* JADX INFO: Incorrect field signature: TM; */
    private com.android.server.permission.access.immutable.Immutable mutable;

    /* JADX WARN: Incorrect types in method signature: (TI;TM;)V */
    /* JADX WARN: Multi-variable type inference failed */
    private MutableReference(com.android.server.permission.access.immutable.Immutable immutable, com.android.server.permission.access.immutable.Immutable mutable) {
        this.immutable = immutable;
        this.mutable = mutable;
    }

    /* JADX WARN: Incorrect types in method signature: (TM;)V */
    public MutableReference(com.android.server.permission.access.immutable.Immutable mutable) {
        this(mutable, mutable);
    }

    public final I get() {
        return this.immutable;
    }

    /* JADX WARN: Incorrect return type in method signature: ()TM; */
    public final com.android.server.permission.access.immutable.Immutable mutate() {
        com.android.server.permission.access.immutable.Immutable it = this.mutable;
        if (it != null) {
            return it;
        }
        java.lang.Object mutable = this.immutable.toMutable();
        I i = (I) mutable;
        this.immutable = i;
        this.mutable = i;
        return (com.android.server.permission.access.immutable.Immutable) mutable;
    }

    public final com.android.server.permission.access.immutable.MutableReference<I, M> toImmutable() {
        return new com.android.server.permission.access.immutable.MutableReference<>(this.immutable, null);
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(getClass(), other != null ? other.getClass() : null)) {
            return false;
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(other, "null cannot be cast to non-null type com.android.server.permission.access.immutable.MutableReference<*, *>");
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.immutable, ((com.android.server.permission.access.immutable.MutableReference) other).immutable);
    }

    public int hashCode() {
        return this.immutable.hashCode();
    }
}
