package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: compiled from: PackageReference.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0019\u0012\n\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0096\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0005H\u0016R\u0018\u0010\u0002\u001a\u0006\u0012\u0002\b\u00030\u0003X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001e\u0010\t\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u000b0\n8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lkotlin/jvm/internal/PackageReference;", "Lkotlin/jvm/internal/ClassBasedDeclarationContainer;", "jClass", "Ljava/lang/Class;", "moduleName", "", "(Ljava/lang/Class;Ljava/lang/String;)V", "getJClass", "()Ljava/lang/Class;", "members", "", "Lkotlin/reflect/KCallable;", "getMembers", "()Ljava/util/Collection;", "equals", "", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class PackageReference implements com.android.server.permission.jarjar.kotlin.jvm.internal.ClassBasedDeclarationContainer {
    private final java.lang.Class<?> jClass;
    private final java.lang.String moduleName;

    public PackageReference(java.lang.Class<?> cls, java.lang.String moduleName) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cls, "jClass");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(moduleName, "moduleName");
        this.jClass = cls;
        this.moduleName = moduleName;
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.ClassBasedDeclarationContainer
    public java.lang.Class<?> getJClass() {
        return this.jClass;
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KDeclarationContainer
    public java.util.Collection<com.android.server.permission.jarjar.kotlin.reflect.KCallable<?>> getMembers() {
        throw new com.android.server.permission.jarjar.kotlin.jvm.KotlinReflectionNotSupportedError();
    }

    public boolean equals(java.lang.Object other) {
        return (other instanceof com.android.server.permission.jarjar.kotlin.jvm.internal.PackageReference) && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(getJClass(), ((com.android.server.permission.jarjar.kotlin.jvm.internal.PackageReference) other).getJClass());
    }

    public int hashCode() {
        return getJClass().hashCode();
    }

    public java.lang.String toString() {
        return getJClass().toString() + " (Kotlin reflection is not available)";
    }
}
