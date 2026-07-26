package kotlin.jvm.internal;

/* JADX INFO: compiled from: TypeReference.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001b\n\u0002\b\u000e\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0000\n\u0002\b\u0004\b\u0007\u0018\u0000 )2\u00020\u0001:\u0001)B%\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tB/\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005\u0012\b\u0010\n\u001a\u0004\u0018\u00010\u0001\u0012\u0006\u0010\u000b\u001a\u00020\f¢\u0006\u0002\u0010\rJ\u0010\u0010\"\u001a\u00020\u001e2\u0006\u0010#\u001a\u00020\bH\u0002J\u0013\u0010$\u001a\u00020\b2\b\u0010%\u001a\u0004\u0018\u00010&H\u0096\u0002J\b\u0010'\u001a\u00020\fH\u0016J\b\u0010(\u001a\u00020\u001eH\u0016J\f\u0010\"\u001a\u00020\u001e*\u00020\u0006H\u0002R\u001a\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\u00058VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u001a\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u001c\u0010\u000b\u001a\u00020\f8\u0000X\u0081\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u0015\u0010\u0016\u001a\u0004\b\u0017\u0010\u0018R\u0014\u0010\u0007\u001a\u00020\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\u0019R\u001e\u0010\n\u001a\u0004\u0018\u00010\u00018\u0000X\u0081\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u001a\u0010\u0016\u001a\u0004\b\u001b\u0010\u001cR\u001c\u0010\u001d\u001a\u00020\u001e*\u0006\u0012\u0002\b\u00030\u001f8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b \u0010!¨\u0006*"}, d2 = {"Lkotlin/jvm/internal/TypeReference;", "Lkotlin/reflect/KType;", "classifier", "Lkotlin/reflect/KClassifier;", "arguments", "", "Lkotlin/reflect/KTypeProjection;", "isMarkedNullable", "", "(Lkotlin/reflect/KClassifier;Ljava/util/List;Z)V", "platformTypeUpperBound", "flags", "", "(Lkotlin/reflect/KClassifier;Ljava/util/List;Lkotlin/reflect/KType;I)V", "annotations", "", "getAnnotations", "()Ljava/util/List;", "getArguments", "getClassifier", "()Lkotlin/reflect/KClassifier;", "getFlags$kotlin_stdlib$annotations", "()V", "getFlags$kotlin_stdlib", "()I", "()Z", "getPlatformTypeUpperBound$kotlin_stdlib$annotations", "getPlatformTypeUpperBound$kotlin_stdlib", "()Lkotlin/reflect/KType;", "arrayClassName", "", "Ljava/lang/Class;", "getArrayClassName", "(Ljava/lang/Class;)Ljava/lang/String;", "asString", "convertPrimitiveToWrapper", "equals", "other", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "toString", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class TypeReference implements kotlin.reflect.KType {
    public static final int IS_MARKED_NULLABLE = 1;
    public static final int IS_MUTABLE_COLLECTION_TYPE = 2;
    public static final int IS_NOTHING_TYPE = 4;
    private final java.util.List<kotlin.reflect.KTypeProjection> arguments;
    private final kotlin.reflect.KClassifier classifier;
    private final int flags;
    private final kotlin.reflect.KType platformTypeUpperBound;

    /* JADX INFO: compiled from: TypeReference.kt */
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

    public static /* synthetic */ void getFlags$kotlin_stdlib$annotations() {
    }

    public static /* synthetic */ void getPlatformTypeUpperBound$kotlin_stdlib$annotations() {
    }

    public TypeReference(kotlin.reflect.KClassifier classifier, java.util.List<kotlin.reflect.KTypeProjection> arguments, kotlin.reflect.KType platformTypeUpperBound, int flags) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(classifier, "classifier");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(arguments, "arguments");
        this.classifier = classifier;
        this.arguments = arguments;
        this.platformTypeUpperBound = platformTypeUpperBound;
        this.flags = flags;
    }

    @Override // kotlin.reflect.KType
    public kotlin.reflect.KClassifier getClassifier() {
        return this.classifier;
    }

    @Override // kotlin.reflect.KType
    public java.util.List<kotlin.reflect.KTypeProjection> getArguments() {
        return this.arguments;
    }

    /* JADX INFO: renamed from: getPlatformTypeUpperBound$kotlin_stdlib, reason: from getter */
    public final kotlin.reflect.KType getPlatformTypeUpperBound() {
        return this.platformTypeUpperBound;
    }

    /* JADX INFO: renamed from: getFlags$kotlin_stdlib, reason: from getter */
    public final int getFlags() {
        return this.flags;
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public TypeReference(kotlin.reflect.KClassifier classifier, java.util.List<kotlin.reflect.KTypeProjection> arguments, boolean z) {
        this(classifier, arguments, null, z ? 1 : 0);
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(classifier, "classifier");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(arguments, "arguments");
    }

    @Override // kotlin.reflect.KAnnotatedElement
    public java.util.List<java.lang.annotation.Annotation> getAnnotations() {
        return kotlin.collections.CollectionsKt.emptyList();
    }

    @Override // kotlin.reflect.KType
    public boolean isMarkedNullable() {
        return (this.flags & 1) != 0;
    }

    public boolean equals(java.lang.Object other) {
        return (other instanceof kotlin.jvm.internal.TypeReference) && kotlin.jvm.internal.Intrinsics.areEqual(getClassifier(), ((kotlin.jvm.internal.TypeReference) other).getClassifier()) && kotlin.jvm.internal.Intrinsics.areEqual(getArguments(), ((kotlin.jvm.internal.TypeReference) other).getArguments()) && kotlin.jvm.internal.Intrinsics.areEqual(this.platformTypeUpperBound, ((kotlin.jvm.internal.TypeReference) other).platformTypeUpperBound) && this.flags == ((kotlin.jvm.internal.TypeReference) other).flags;
    }

    public int hashCode() {
        return (((getClassifier().hashCode() * 31) + getArguments().hashCode()) * 31) + java.lang.Integer.hashCode(this.flags);
    }

    public java.lang.String toString() {
        return asString(false) + " (Kotlin reflection is not available)";
    }

    private final java.lang.String asString(boolean convertPrimitiveToWrapper) {
        java.lang.String klass;
        kotlin.reflect.KClassifier classifier = getClassifier();
        kotlin.reflect.KClass kClass = classifier instanceof kotlin.reflect.KClass ? (kotlin.reflect.KClass) classifier : null;
        java.lang.Class<?> javaClass = kClass != null ? kotlin.jvm.JvmClassMappingKt.getJavaClass(kClass) : null;
        if (javaClass == null) {
            klass = getClassifier().toString();
        } else if ((this.flags & 4) != 0) {
            klass = "kotlin.Nothing";
        } else if (javaClass.isArray()) {
            klass = getArrayClassName(javaClass);
        } else if (convertPrimitiveToWrapper && javaClass.isPrimitive()) {
            kotlin.reflect.KClassifier classifier2 = getClassifier();
            kotlin.jvm.internal.Intrinsics.checkNotNull(classifier2, "null cannot be cast to non-null type kotlin.reflect.KClass<*>");
            klass = kotlin.jvm.JvmClassMappingKt.getJavaObjectType((kotlin.reflect.KClass) classifier2).getName();
        } else {
            klass = javaClass.getName();
        }
        java.lang.String args = getArguments().isEmpty() ? "" : kotlin.collections.CollectionsKt.joinToString$default(getArguments(), ", ", "<", ">", 0, null, new kotlin.jvm.functions.Function1<kotlin.reflect.KTypeProjection, java.lang.CharSequence>() { // from class: kotlin.jvm.internal.TypeReference$asString$args$1
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public final java.lang.CharSequence invoke(kotlin.reflect.KTypeProjection it) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
                return this.this$0.asString(it);
            }
        }, 24, null);
        java.lang.String nullable = isMarkedNullable() ? "?" : "";
        java.lang.String result = klass + args + nullable;
        kotlin.reflect.KType upper = this.platformTypeUpperBound;
        if (upper instanceof kotlin.jvm.internal.TypeReference) {
            java.lang.String renderedUpper = ((kotlin.jvm.internal.TypeReference) upper).asString(true);
            if (!kotlin.jvm.internal.Intrinsics.areEqual(renderedUpper, result)) {
                return kotlin.jvm.internal.Intrinsics.areEqual(renderedUpper, new java.lang.StringBuilder().append(result).append('?').toString()) ? result + '!' : '(' + result + ".." + renderedUpper + ')';
            }
        }
        return result;
    }

    private final java.lang.String getArrayClassName(java.lang.Class<?> cls) {
        return kotlin.jvm.internal.Intrinsics.areEqual(cls, boolean[].class) ? "kotlin.BooleanArray" : kotlin.jvm.internal.Intrinsics.areEqual(cls, char[].class) ? "kotlin.CharArray" : kotlin.jvm.internal.Intrinsics.areEqual(cls, byte[].class) ? "kotlin.ByteArray" : kotlin.jvm.internal.Intrinsics.areEqual(cls, short[].class) ? "kotlin.ShortArray" : kotlin.jvm.internal.Intrinsics.areEqual(cls, int[].class) ? "kotlin.IntArray" : kotlin.jvm.internal.Intrinsics.areEqual(cls, float[].class) ? "kotlin.FloatArray" : kotlin.jvm.internal.Intrinsics.areEqual(cls, long[].class) ? "kotlin.LongArray" : kotlin.jvm.internal.Intrinsics.areEqual(cls, double[].class) ? "kotlin.DoubleArray" : "kotlin.Array";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final java.lang.String asString(kotlin.reflect.KTypeProjection $this$asString) {
        java.lang.String typeString;
        if ($this$asString.getVariance() == null) {
            return com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER;
        }
        kotlin.reflect.KType type = $this$asString.getType();
        kotlin.jvm.internal.TypeReference typeReference = type instanceof kotlin.jvm.internal.TypeReference ? (kotlin.jvm.internal.TypeReference) type : null;
        if (typeReference == null || (typeString = typeReference.asString(true)) == null) {
            typeString = java.lang.String.valueOf($this$asString.getType());
        }
        switch (kotlin.jvm.internal.TypeReference.WhenMappings.$EnumSwitchMapping$0[$this$asString.getVariance().ordinal()]) {
            case 1:
                return typeString;
            case 2:
                return "in " + typeString;
            case 3:
                return "out " + typeString;
            default:
                throw new kotlin.NoWhenBranchMatchedException();
        }
    }
}
