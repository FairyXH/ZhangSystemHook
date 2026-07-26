package kotlin.reflect;

/* JADX INFO: compiled from: TypesJVM.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\u001a\"\u0010\n\u001a\u00020\u00012\n\u0010\u000b\u001a\u0006\u0012\u0002\b\u00030\f2\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00070\u000eH\u0003\u001a\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0001H\u0002\u001a\u0016\u0010\u0012\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0003\"\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00028FX\u0087\u0004¢\u0006\f\u0012\u0004\b\u0003\u0010\u0004\u001a\u0004\b\u0005\u0010\u0006\"\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00078BX\u0083\u0004¢\u0006\f\u0012\u0004\b\u0003\u0010\b\u001a\u0004\b\u0005\u0010\t¨\u0006\u0015"}, d2 = {"javaType", "Ljava/lang/reflect/Type;", "Lkotlin/reflect/KType;", "getJavaType$annotations", "(Lkotlin/reflect/KType;)V", "getJavaType", "(Lkotlin/reflect/KType;)Ljava/lang/reflect/Type;", "Lkotlin/reflect/KTypeProjection;", "(Lkotlin/reflect/KTypeProjection;)V", "(Lkotlin/reflect/KTypeProjection;)Ljava/lang/reflect/Type;", "createPossiblyInnerType", "jClass", "Ljava/lang/Class;", "arguments", "", "typeToString", "", "type", "computeJavaType", "forceWrapper", "", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class TypesJVMKt {

    /* JADX INFO: compiled from: TypesJVM.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[kotlin.reflect.KVariance.values().length];
            try {
                iArr[kotlin.reflect.KVariance.IN.ordinal()] = 1;
            } catch (java.lang.NoSuchFieldError e) {
            }
            try {
                iArr[kotlin.reflect.KVariance.INVARIANT.ordinal()] = 2;
            } catch (java.lang.NoSuchFieldError e2) {
            }
            try {
                iArr[kotlin.reflect.KVariance.OUT.ordinal()] = 3;
            } catch (java.lang.NoSuchFieldError e3) {
            }
            $EnumSwitchMapping$0 = iArr;
        }
    }

    public static /* synthetic */ void getJavaType$annotations(kotlin.reflect.KType kType) {
    }

    private static /* synthetic */ void getJavaType$annotations(kotlin.reflect.KTypeProjection kTypeProjection) {
    }

    public static final java.lang.reflect.Type getJavaType(kotlin.reflect.KType $this$javaType) {
        java.lang.reflect.Type it;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$javaType, "<this>");
        if (($this$javaType instanceof kotlin.jvm.internal.KTypeBase) && (it = ((kotlin.jvm.internal.KTypeBase) $this$javaType).getJavaType()) != null) {
            return it;
        }
        return computeJavaType$default($this$javaType, false, 1, null);
    }

    static /* synthetic */ java.lang.reflect.Type computeJavaType$default(kotlin.reflect.KType kType, boolean z, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        return computeJavaType(kType, z);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final java.lang.reflect.Type computeJavaType(kotlin.reflect.KType $this$computeJavaType, boolean forceWrapper) {
        kotlin.reflect.KClassifier classifier = $this$computeJavaType.getClassifier();
        if (classifier instanceof kotlin.reflect.KTypeParameter) {
            return new kotlin.reflect.TypeVariableImpl((kotlin.reflect.KTypeParameter) classifier);
        }
        if (classifier instanceof kotlin.reflect.KClass) {
            kotlin.reflect.KClass kClass = (kotlin.reflect.KClass) classifier;
            java.lang.Class jClass = forceWrapper ? kotlin.jvm.JvmClassMappingKt.getJavaObjectType(kClass) : kotlin.jvm.JvmClassMappingKt.getJavaClass(kClass);
            java.util.List<kotlin.reflect.KTypeProjection> arguments = $this$computeJavaType.getArguments();
            if (arguments.isEmpty()) {
                return jClass;
            }
            if (jClass.isArray()) {
                if (jClass.getComponentType().isPrimitive()) {
                    return jClass;
                }
                kotlin.reflect.KTypeProjection kTypeProjection = (kotlin.reflect.KTypeProjection) kotlin.collections.CollectionsKt.singleOrNull((java.util.List) arguments);
                if (kTypeProjection == null) {
                    throw new java.lang.IllegalArgumentException("kotlin.Array must have exactly one type argument: " + $this$computeJavaType);
                }
                kotlin.reflect.KVariance variance = kTypeProjection.getVariance();
                kotlin.reflect.KType elementType = kTypeProjection.getType();
                switch (variance == null ? -1 : kotlin.reflect.TypesJVMKt.WhenMappings.$EnumSwitchMapping$0[variance.ordinal()]) {
                    case -1:
                    case 1:
                        return jClass;
                    case 0:
                    default:
                        throw new kotlin.NoWhenBranchMatchedException();
                    case 2:
                    case 3:
                        kotlin.jvm.internal.Intrinsics.checkNotNull(elementType);
                        java.lang.reflect.Type javaElementType = computeJavaType$default(elementType, false, 1, null);
                        return javaElementType instanceof java.lang.Class ? jClass : new kotlin.reflect.GenericArrayTypeImpl(javaElementType);
                }
            }
            return createPossiblyInnerType(jClass, arguments);
        }
        throw new java.lang.UnsupportedOperationException("Unsupported type classifier: " + $this$computeJavaType);
    }

    private static final java.lang.reflect.Type createPossiblyInnerType(java.lang.Class<?> cls, java.util.List<kotlin.reflect.KTypeProjection> list) {
        java.lang.Class<?> declaringClass = cls.getDeclaringClass();
        if (declaringClass == null) {
            java.util.List<kotlin.reflect.KTypeProjection> $this$map$iv = list;
            java.util.Collection destination$iv$iv = new java.util.ArrayList(kotlin.collections.CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
            for (java.lang.Object item$iv$iv : $this$map$iv) {
                kotlin.reflect.KTypeProjection p0 = (kotlin.reflect.KTypeProjection) item$iv$iv;
                destination$iv$iv.add(getJavaType(p0));
            }
            return new kotlin.reflect.ParameterizedTypeImpl(cls, null, (java.util.List) destination$iv$iv);
        }
        if (java.lang.reflect.Modifier.isStatic(cls.getModifiers())) {
            java.lang.Class<?> cls2 = declaringClass;
            java.util.List<kotlin.reflect.KTypeProjection> $this$map$iv2 = list;
            java.util.Collection destination$iv$iv2 = new java.util.ArrayList(kotlin.collections.CollectionsKt.collectionSizeOrDefault($this$map$iv2, 10));
            for (java.lang.Object item$iv$iv2 : $this$map$iv2) {
                kotlin.reflect.KTypeProjection p02 = (kotlin.reflect.KTypeProjection) item$iv$iv2;
                destination$iv$iv2.add(getJavaType(p02));
            }
            return new kotlin.reflect.ParameterizedTypeImpl(cls, cls2, (java.util.List) destination$iv$iv2);
        }
        int n = cls.getTypeParameters().length;
        java.lang.reflect.Type typeCreatePossiblyInnerType = createPossiblyInnerType(declaringClass, list.subList(n, list.size()));
        java.lang.Iterable $this$map$iv3 = list.subList(0, n);
        java.util.Collection destination$iv$iv3 = new java.util.ArrayList(kotlin.collections.CollectionsKt.collectionSizeOrDefault($this$map$iv3, 10));
        for (java.lang.Object item$iv$iv3 : $this$map$iv3) {
            kotlin.reflect.KTypeProjection p03 = (kotlin.reflect.KTypeProjection) item$iv$iv3;
            destination$iv$iv3.add(getJavaType(p03));
        }
        return new kotlin.reflect.ParameterizedTypeImpl(cls, typeCreatePossiblyInnerType, (java.util.List) destination$iv$iv3);
    }

    private static final java.lang.reflect.Type getJavaType(kotlin.reflect.KTypeProjection $this$javaType) {
        kotlin.reflect.KVariance variance = $this$javaType.getVariance();
        if (variance == null) {
            return kotlin.reflect.WildcardTypeImpl.INSTANCE.getSTAR();
        }
        kotlin.reflect.KType type = $this$javaType.getType();
        kotlin.jvm.internal.Intrinsics.checkNotNull(type);
        switch (kotlin.reflect.TypesJVMKt.WhenMappings.$EnumSwitchMapping$0[variance.ordinal()]) {
            case 1:
                return new kotlin.reflect.WildcardTypeImpl(null, computeJavaType(type, true));
            case 2:
                return computeJavaType(type, true);
            case 3:
                return new kotlin.reflect.WildcardTypeImpl(computeJavaType(type, true), null);
            default:
                throw new kotlin.NoWhenBranchMatchedException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final java.lang.String typeToString(java.lang.reflect.Type type) {
        java.lang.String name;
        if (type instanceof java.lang.Class) {
            if (((java.lang.Class) type).isArray()) {
                kotlin.sequences.Sequence unwrap = kotlin.sequences.SequencesKt.generateSequence(type, kotlin.reflect.TypesJVMKt$typeToString$unwrap$1.INSTANCE);
                name = ((java.lang.Class) kotlin.sequences.SequencesKt.last(unwrap)).getName() + kotlin.text.StringsKt.repeat("[]", kotlin.sequences.SequencesKt.count(unwrap));
            } else {
                name = ((java.lang.Class) type).getName();
            }
            kotlin.jvm.internal.Intrinsics.checkNotNull(name);
            return name;
        }
        return type.toString();
    }
}
