package kotlin.jvm.internal;

/* JADX INFO: compiled from: TypeParameterReference.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\b\u0007\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB'\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0013\u0010\u0018\u001a\u00020\t2\b\u0010\u0019\u001a\u0004\u0018\u00010\u0003H\u0096\u0002J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u0014\u0010\u001c\u001a\u00020\u001d2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\fJ\b\u0010\u001e\u001a\u00020\u0005H\u0016R\u0016\u0010\u000b\u001a\n\u0012\u0004\u0012\u00020\r\u0018\u00010\fX\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\tX\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u000eR\u0014\u0010\u0004\u001a\u00020\u0005X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R \u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\f8VX\u0096\u0004¢\u0006\f\u0012\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0006\u001a\u00020\u0007X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017¨\u0006 "}, d2 = {"Lkotlin/jvm/internal/TypeParameterReference;", "Lkotlin/reflect/KTypeParameter;", "container", "", "name", "", "variance", "Lkotlin/reflect/KVariance;", "isReified", "", "(Ljava/lang/Object;Ljava/lang/String;Lkotlin/reflect/KVariance;Z)V", "bounds", "", "Lkotlin/reflect/KType;", "()Z", "getName", "()Ljava/lang/String;", "upperBounds", "getUpperBounds$annotations", "()V", "getUpperBounds", "()Ljava/util/List;", "getVariance", "()Lkotlin/reflect/KVariance;", "equals", "other", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "setUpperBounds", "", "toString", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class TypeParameterReference implements kotlin.reflect.KTypeParameter {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final kotlin.jvm.internal.TypeParameterReference.Companion INSTANCE = new kotlin.jvm.internal.TypeParameterReference.Companion(null);
    private volatile java.util.List<? extends kotlin.reflect.KType> bounds;
    private final java.lang.Object container;
    private final boolean isReified;
    private final java.lang.String name;
    private final kotlin.reflect.KVariance variance;

    public static /* synthetic */ void getUpperBounds$annotations() {
    }

    public TypeParameterReference(java.lang.Object container, java.lang.String name, kotlin.reflect.KVariance variance, boolean isReified) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(variance, "variance");
        this.container = container;
        this.name = name;
        this.variance = variance;
        this.isReified = isReified;
    }

    @Override // kotlin.reflect.KTypeParameter
    public java.lang.String getName() {
        return this.name;
    }

    @Override // kotlin.reflect.KTypeParameter
    public kotlin.reflect.KVariance getVariance() {
        return this.variance;
    }

    @Override // kotlin.reflect.KTypeParameter
    /* JADX INFO: renamed from: isReified, reason: from getter */
    public boolean getIsReified() {
        return this.isReified;
    }

    @Override // kotlin.reflect.KTypeParameter
    public java.util.List<kotlin.reflect.KType> getUpperBounds() {
        java.util.List list = this.bounds;
        if (list != null) {
            return list;
        }
        java.util.List<kotlin.reflect.KType> listListOf = kotlin.collections.CollectionsKt.listOf(kotlin.jvm.internal.Reflection.nullableTypeOf(java.lang.Object.class));
        this.bounds = listListOf;
        return listListOf;
    }

    public final void setUpperBounds(java.util.List<? extends kotlin.reflect.KType> upperBounds) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(upperBounds, "upperBounds");
        if (this.bounds != null) {
            throw new java.lang.IllegalStateException(("Upper bounds of type parameter '" + this + "' have already been initialized.").toString());
        }
        this.bounds = upperBounds;
    }

    public boolean equals(java.lang.Object other) {
        return (other instanceof kotlin.jvm.internal.TypeParameterReference) && kotlin.jvm.internal.Intrinsics.areEqual(this.container, ((kotlin.jvm.internal.TypeParameterReference) other).container) && kotlin.jvm.internal.Intrinsics.areEqual(getName(), ((kotlin.jvm.internal.TypeParameterReference) other).getName());
    }

    public int hashCode() {
        java.lang.Object obj = this.container;
        return ((obj != null ? obj.hashCode() : 0) * 31) + getName().hashCode();
    }

    public java.lang.String toString() {
        return INSTANCE.toString(this);
    }

    /* JADX INFO: compiled from: TypeParameterReference.kt */
    @kotlin.Metadata(d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006¨\u0006\u0007"}, d2 = {"Lkotlin/jvm/internal/TypeParameterReference$Companion;", "", "()V", "toString", "", "typeParameter", "Lkotlin/reflect/KTypeParameter;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {

        /* JADX INFO: compiled from: TypeParameterReference.kt */
        @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
        public /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[kotlin.reflect.KVariance.values().length];
                try {
                    iArr[kotlin.reflect.KVariance.INVARIANT.ordinal()] = 1;
                } catch (java.lang.NoSuchFieldError e) {
                }
                try {
                    iArr[kotlin.reflect.KVariance.IN.ordinal()] = 2;
                } catch (java.lang.NoSuchFieldError e2) {
                }
                try {
                    iArr[kotlin.reflect.KVariance.OUT.ordinal()] = 3;
                } catch (java.lang.NoSuchFieldError e3) {
                }
                $EnumSwitchMapping$0 = iArr;
            }
        }

        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final java.lang.String toString(kotlin.reflect.KTypeParameter typeParameter) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(typeParameter, "typeParameter");
            java.lang.StringBuilder $this$toString_u24lambda_u240 = new java.lang.StringBuilder();
            switch (kotlin.jvm.internal.TypeParameterReference.Companion.WhenMappings.$EnumSwitchMapping$0[typeParameter.getVariance().ordinal()]) {
                case 2:
                    $this$toString_u24lambda_u240.append("in ");
                    break;
                case 3:
                    $this$toString_u24lambda_u240.append("out ");
                    break;
            }
            $this$toString_u24lambda_u240.append(typeParameter.getName());
            java.lang.String string = $this$toString_u24lambda_u240.toString();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            return string;
        }
    }
}
