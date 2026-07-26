package kotlin.reflect;

/* JADX INFO: compiled from: TypesJVM.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0013\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0096\u0002J%\u0010\u000b\u001a\u0004\u0018\u0001H\f\"\b\b\u0000\u0010\f*\u00020\r2\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\f0\u000f¢\u0006\u0002\u0010\u0010J\u0011\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\u0012¢\u0006\u0002\u0010\u0013J\u0013\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00150\u0012H\u0016¢\u0006\u0002\u0010\u0016J\u0011\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\r0\u0012¢\u0006\u0002\u0010\u0013J\b\u0010\u0018\u001a\u00020\u0002H\u0016J\b\u0010\u0019\u001a\u00020\u001aH\u0016J\b\u0010\u001b\u001a\u00020\u001aH\u0016J\b\u0010\u001c\u001a\u00020\u001dH\u0016J\b\u0010\u001e\u001a\u00020\u001aH\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u001f"}, d2 = {"Lkotlin/reflect/TypeVariableImpl;", "Ljava/lang/reflect/TypeVariable;", "Ljava/lang/reflect/GenericDeclaration;", "Lkotlin/reflect/TypeImpl;", "typeParameter", "Lkotlin/reflect/KTypeParameter;", "(Lkotlin/reflect/KTypeParameter;)V", "equals", "", "other", "", "getAnnotation", "T", "", "annotationClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)Ljava/lang/annotation/Annotation;", "getAnnotations", "", "()[Ljava/lang/annotation/Annotation;", "getBounds", "Ljava/lang/reflect/Type;", "()[Ljava/lang/reflect/Type;", "getDeclaredAnnotations", "getGenericDeclaration", "getName", "", "getTypeName", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class TypeVariableImpl implements java.lang.reflect.TypeVariable<java.lang.reflect.GenericDeclaration>, kotlin.reflect.TypeImpl {
    private final kotlin.reflect.KTypeParameter typeParameter;

    public TypeVariableImpl(kotlin.reflect.KTypeParameter typeParameter) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(typeParameter, "typeParameter");
        this.typeParameter = typeParameter;
    }

    @Override // java.lang.reflect.TypeVariable
    public java.lang.String getName() {
        return this.typeParameter.getName();
    }

    @Override // java.lang.reflect.TypeVariable
    public java.lang.reflect.GenericDeclaration getGenericDeclaration() {
        throw new kotlin.NotImplementedError("An operation is not implemented: " + ("getGenericDeclaration() is not yet supported for type variables created from KType: " + this.typeParameter));
    }

    @Override // java.lang.reflect.TypeVariable
    public java.lang.reflect.Type[] getBounds() {
        java.lang.Iterable $this$map$iv = this.typeParameter.getUpperBounds();
        java.util.Collection destination$iv$iv = new java.util.ArrayList(kotlin.collections.CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        for (java.lang.Object item$iv$iv : $this$map$iv) {
            kotlin.reflect.KType it = (kotlin.reflect.KType) item$iv$iv;
            destination$iv$iv.add(kotlin.reflect.TypesJVMKt.computeJavaType(it, true));
        }
        java.util.Collection thisCollection$iv = (java.util.List) destination$iv$iv;
        return (java.lang.reflect.Type[]) thisCollection$iv.toArray(new java.lang.reflect.Type[0]);
    }

    @Override // java.lang.reflect.Type, kotlin.reflect.TypeImpl
    public java.lang.String getTypeName() {
        return getName();
    }

    public boolean equals(java.lang.Object other) {
        return (other instanceof java.lang.reflect.TypeVariable) && kotlin.jvm.internal.Intrinsics.areEqual(getName(), ((java.lang.reflect.TypeVariable) other).getName()) && kotlin.jvm.internal.Intrinsics.areEqual(getGenericDeclaration(), ((java.lang.reflect.TypeVariable) other).getGenericDeclaration());
    }

    public int hashCode() {
        return getName().hashCode() ^ getGenericDeclaration().hashCode();
    }

    public java.lang.String toString() {
        return getTypeName();
    }

    public final <T extends java.lang.annotation.Annotation> T getAnnotation(java.lang.Class<T> annotationClass) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(annotationClass, "annotationClass");
        return null;
    }

    public final java.lang.annotation.Annotation[] getAnnotations() {
        return new java.lang.annotation.Annotation[0];
    }

    public final java.lang.annotation.Annotation[] getDeclaredAnnotations() {
        return new java.lang.annotation.Annotation[0];
    }
}
