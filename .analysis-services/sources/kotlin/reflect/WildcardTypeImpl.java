package kotlin.reflect;

/* JADX INFO: compiled from: TypesJVM.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\u0003\u0018\u0000 \u00142\u00020\u00012\u00020\u0002:\u0001\u0014B\u0019\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004¢\u0006\u0002\u0010\u0006J\u0013\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\nH\u0096\u0002J\u0013\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\fH\u0016¢\u0006\u0002\u0010\rJ\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0013\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00040\fH\u0016¢\u0006\u0002\u0010\rJ\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\u000fH\u0016R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lkotlin/reflect/WildcardTypeImpl;", "Ljava/lang/reflect/WildcardType;", "Lkotlin/reflect/TypeImpl;", "upperBound", "Ljava/lang/reflect/Type;", "lowerBound", "(Ljava/lang/reflect/Type;Ljava/lang/reflect/Type;)V", "equals", "", "other", "", "getLowerBounds", "", "()[Ljava/lang/reflect/Type;", "getTypeName", "", "getUpperBounds", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class WildcardTypeImpl implements java.lang.reflect.WildcardType, kotlin.reflect.TypeImpl {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final kotlin.reflect.WildcardTypeImpl.Companion INSTANCE = new kotlin.reflect.WildcardTypeImpl.Companion(null);
    private static final kotlin.reflect.WildcardTypeImpl STAR = new kotlin.reflect.WildcardTypeImpl(null, null);
    private final java.lang.reflect.Type lowerBound;
    private final java.lang.reflect.Type upperBound;

    public WildcardTypeImpl(java.lang.reflect.Type upperBound, java.lang.reflect.Type lowerBound) {
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
    }

    @Override // java.lang.reflect.WildcardType
    public java.lang.reflect.Type[] getUpperBounds() {
        java.lang.reflect.Type[] typeArr = new java.lang.reflect.Type[1];
        java.lang.Class cls = this.upperBound;
        if (cls == null) {
        }
        typeArr[0] = cls;
        return typeArr;
    }

    @Override // java.lang.reflect.WildcardType
    public java.lang.reflect.Type[] getLowerBounds() {
        if (this.lowerBound != null) {
            return new java.lang.reflect.Type[]{this.lowerBound};
        }
        return new java.lang.reflect.Type[0];
    }

    @Override // java.lang.reflect.Type, kotlin.reflect.TypeImpl
    public java.lang.String getTypeName() {
        if (this.lowerBound != null) {
            return "? super " + kotlin.reflect.TypesJVMKt.typeToString(this.lowerBound);
        }
        if (this.upperBound != null && !kotlin.jvm.internal.Intrinsics.areEqual(this.upperBound, java.lang.Object.class)) {
            return "? extends " + kotlin.reflect.TypesJVMKt.typeToString(this.upperBound);
        }
        return "?";
    }

    public boolean equals(java.lang.Object other) {
        return (other instanceof java.lang.reflect.WildcardType) && java.util.Arrays.equals(getUpperBounds(), ((java.lang.reflect.WildcardType) other).getUpperBounds()) && java.util.Arrays.equals(getLowerBounds(), ((java.lang.reflect.WildcardType) other).getLowerBounds());
    }

    public int hashCode() {
        return java.util.Arrays.hashCode(getUpperBounds()) ^ java.util.Arrays.hashCode(getLowerBounds());
    }

    public java.lang.String toString() {
        return getTypeName();
    }

    /* JADX INFO: compiled from: TypesJVM.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, d2 = {"Lkotlin/reflect/WildcardTypeImpl$Companion;", "", "()V", "STAR", "Lkotlin/reflect/WildcardTypeImpl;", "getSTAR", "()Lkotlin/reflect/WildcardTypeImpl;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final kotlin.reflect.WildcardTypeImpl getSTAR() {
            return kotlin.reflect.WildcardTypeImpl.STAR;
        }
    }
}
