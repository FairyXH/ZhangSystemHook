package com.android.server.permission.jarjar.kotlin.reflect;

/* JADX INFO: compiled from: TypesJVM.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u00012\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0013\u0010\u0006\u001a\u00020\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0096\u0002J\b\u0010\n\u001a\u00020\u0004H\u0016J\b\u0010\u000b\u001a\u00020\fH\u0016J\b\u0010\r\u001a\u00020\u000eH\u0016J\b\u0010\u000f\u001a\u00020\fH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lkotlin/reflect/GenericArrayTypeImpl;", "Ljava/lang/reflect/GenericArrayType;", "Lkotlin/reflect/TypeImpl;", "elementType", "Ljava/lang/reflect/Type;", "(Ljava/lang/reflect/Type;)V", "equals", "", "other", "", "getGenericComponentType", "getTypeName", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class GenericArrayTypeImpl implements java.lang.reflect.GenericArrayType, com.android.server.permission.jarjar.kotlin.reflect.TypeImpl {
    private final java.lang.reflect.Type elementType;

    public GenericArrayTypeImpl(java.lang.reflect.Type elementType) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elementType, "elementType");
        this.elementType = elementType;
    }

    @Override // java.lang.reflect.GenericArrayType
    public java.lang.reflect.Type getGenericComponentType() {
        return this.elementType;
    }

    @Override // java.lang.reflect.Type, com.android.server.permission.jarjar.kotlin.reflect.TypeImpl
    public java.lang.String getTypeName() {
        return com.android.server.permission.jarjar.kotlin.reflect.TypesJVMKt.typeToString(this.elementType) + "[]";
    }

    public boolean equals(java.lang.Object other) {
        return (other instanceof java.lang.reflect.GenericArrayType) && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(getGenericComponentType(), ((java.lang.reflect.GenericArrayType) other).getGenericComponentType());
    }

    public int hashCode() {
        return getGenericComponentType().hashCode();
    }

    public java.lang.String toString() {
        return getTypeName();
    }
}
