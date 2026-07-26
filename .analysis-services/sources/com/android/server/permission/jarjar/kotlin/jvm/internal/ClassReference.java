package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: compiled from: ClassReference.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000p\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\u001b\n\u0002\b\u0003\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u0000 O2\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003:\u0001OB\u0011\u0012\n\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005¢\u0006\u0002\u0010\u0006J\u0013\u0010F\u001a\u00020\u00122\b\u0010G\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\b\u0010H\u001a\u00020IH\u0002J\b\u0010J\u001a\u00020KH\u0016J\u0012\u0010L\u001a\u00020\u00122\b\u0010M\u001a\u0004\u0018\u00010\u0002H\u0017J\b\u0010N\u001a\u000201H\u0016R\u001a\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\n\u0010\u000bR \u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00020\u000e0\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u0013\u0010\u0014\u001a\u0004\b\u0011\u0010\u0015R\u001a\u0010\u0016\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u0017\u0010\u0014\u001a\u0004\b\u0016\u0010\u0015R\u001a\u0010\u0018\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u0019\u0010\u0014\u001a\u0004\b\u0018\u0010\u0015R\u001a\u0010\u001a\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u001b\u0010\u0014\u001a\u0004\b\u001a\u0010\u0015R\u001a\u0010\u001c\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u001d\u0010\u0014\u001a\u0004\b\u001c\u0010\u0015R\u001a\u0010\u001e\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b\u001f\u0010\u0014\u001a\u0004\b\u001e\u0010\u0015R\u001a\u0010 \u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b!\u0010\u0014\u001a\u0004\b \u0010\u0015R\u001a\u0010\"\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b#\u0010\u0014\u001a\u0004\b\"\u0010\u0015R\u001a\u0010$\u001a\u00020\u00128VX\u0097\u0004¢\u0006\f\u0012\u0004\b%\u0010\u0014\u001a\u0004\b$\u0010\u0015R\u0018\u0010\u0004\u001a\u0006\u0012\u0002\b\u00030\u0005X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b&\u0010'R\u001e\u0010(\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030)0\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b*\u0010\u0010R\u001e\u0010+\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00010\r8VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b,\u0010\u0010R\u0016\u0010-\u001a\u0004\u0018\u00010\u00028VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b.\u0010/R\u0016\u00100\u001a\u0004\u0018\u0001018VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b2\u00103R(\u00104\u001a\u0010\u0012\f\u0012\n\u0012\u0006\b\u0001\u0012\u00020\u00020\u00010\b8VX\u0097\u0004¢\u0006\f\u0012\u0004\b5\u0010\u0014\u001a\u0004\b6\u0010\u000bR\u0016\u00107\u001a\u0004\u0018\u0001018VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b8\u00103R \u00109\u001a\b\u0012\u0004\u0012\u00020:0\b8VX\u0097\u0004¢\u0006\f\u0012\u0004\b;\u0010\u0014\u001a\u0004\b<\u0010\u000bR \u0010=\u001a\b\u0012\u0004\u0012\u00020>0\b8VX\u0097\u0004¢\u0006\f\u0012\u0004\b?\u0010\u0014\u001a\u0004\b@\u0010\u000bR\u001c\u0010A\u001a\u0004\u0018\u00010B8VX\u0097\u0004¢\u0006\f\u0012\u0004\bC\u0010\u0014\u001a\u0004\bD\u0010E¨\u0006P"}, d2 = {"Lkotlin/jvm/internal/ClassReference;", "Lkotlin/reflect/KClass;", "", "Lkotlin/jvm/internal/ClassBasedDeclarationContainer;", "jClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)V", "annotations", "", "", "getAnnotations", "()Ljava/util/List;", "constructors", "", "Lkotlin/reflect/KFunction;", "getConstructors", "()Ljava/util/Collection;", "isAbstract", "", "isAbstract$annotations", "()V", "()Z", "isCompanion", "isCompanion$annotations", "isData", "isData$annotations", "isFinal", "isFinal$annotations", "isFun", "isFun$annotations", "isInner", "isInner$annotations", "isOpen", "isOpen$annotations", "isSealed", "isSealed$annotations", "isValue", "isValue$annotations", "getJClass", "()Ljava/lang/Class;", "members", "Lkotlin/reflect/KCallable;", "getMembers", "nestedClasses", "getNestedClasses", "objectInstance", "getObjectInstance", "()Ljava/lang/Object;", "qualifiedName", "", "getQualifiedName", "()Ljava/lang/String;", "sealedSubclasses", "getSealedSubclasses$annotations", "getSealedSubclasses", "simpleName", "getSimpleName", "supertypes", "Lkotlin/reflect/KType;", "getSupertypes$annotations", "getSupertypes", "typeParameters", "Lkotlin/reflect/KTypeParameter;", "getTypeParameters$annotations", "getTypeParameters", "visibility", "Lkotlin/reflect/KVisibility;", "getVisibility$annotations", "getVisibility", "()Lkotlin/reflect/KVisibility;", "equals", "other", "error", "", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "isInstance", "value", "toString", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class ClassReference implements com.android.server.permission.jarjar.kotlin.reflect.KClass<java.lang.Object>, com.android.server.permission.jarjar.kotlin.jvm.internal.ClassBasedDeclarationContainer {
    public static final com.android.server.permission.jarjar.kotlin.jvm.internal.ClassReference.Companion Companion = new com.android.server.permission.jarjar.kotlin.jvm.internal.ClassReference.Companion(null);
    private static final java.util.Map<java.lang.Class<? extends com.android.server.permission.jarjar.kotlin.Function<?>>, java.lang.Integer> FUNCTION_CLASSES;
    private static final java.util.HashMap<java.lang.String, java.lang.String> classFqNames;
    private static final java.util.HashMap<java.lang.String, java.lang.String> primitiveFqNames;
    private static final java.util.HashMap<java.lang.String, java.lang.String> primitiveWrapperFqNames;
    private static final java.util.Map<java.lang.String, java.lang.String> simpleNames;
    private final java.lang.Class<?> jClass;

    public static /* synthetic */ void getSealedSubclasses$annotations() {
    }

    public static /* synthetic */ void getSupertypes$annotations() {
    }

    public static /* synthetic */ void getTypeParameters$annotations() {
    }

    public static /* synthetic */ void getVisibility$annotations() {
    }

    public static /* synthetic */ void isAbstract$annotations() {
    }

    public static /* synthetic */ void isCompanion$annotations() {
    }

    public static /* synthetic */ void isData$annotations() {
    }

    public static /* synthetic */ void isFinal$annotations() {
    }

    public static /* synthetic */ void isFun$annotations() {
    }

    public static /* synthetic */ void isInner$annotations() {
    }

    public static /* synthetic */ void isOpen$annotations() {
    }

    public static /* synthetic */ void isSealed$annotations() {
    }

    public static /* synthetic */ void isValue$annotations() {
    }

    public ClassReference(java.lang.Class<?> cls) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cls, "jClass");
        this.jClass = cls;
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.internal.ClassBasedDeclarationContainer
    public java.lang.Class<?> getJClass() {
        return this.jClass;
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public java.lang.String getSimpleName() {
        return Companion.getClassSimpleName(getJClass());
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public java.lang.String getQualifiedName() {
        return Companion.getClassQualifiedName(getJClass());
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass, com.android.server.permission.jarjar.kotlin.reflect.KDeclarationContainer
    public java.util.Collection<com.android.server.permission.jarjar.kotlin.reflect.KCallable<?>> getMembers() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public java.util.Collection<com.android.server.permission.jarjar.kotlin.reflect.KFunction<java.lang.Object>> getConstructors() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public java.util.Collection<com.android.server.permission.jarjar.kotlin.reflect.KClass<?>> getNestedClasses() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KAnnotatedElement
    public java.util.List<java.lang.annotation.Annotation> getAnnotations() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public java.lang.Object getObjectInstance() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public boolean isInstance(java.lang.Object value) {
        return Companion.isInstance(value, getJClass());
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public java.util.List<com.android.server.permission.jarjar.kotlin.reflect.KTypeParameter> getTypeParameters() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public java.util.List<com.android.server.permission.jarjar.kotlin.reflect.KType> getSupertypes() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public java.util.List<com.android.server.permission.jarjar.kotlin.reflect.KClass<? extends java.lang.Object>> getSealedSubclasses() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public com.android.server.permission.jarjar.kotlin.reflect.KVisibility getVisibility() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public boolean isFinal() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public boolean isOpen() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public boolean isAbstract() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public boolean isSealed() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public boolean isData() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public boolean isInner() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public boolean isCompanion() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public boolean isFun() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public boolean isValue() {
        error();
        throw new com.android.server.permission.jarjar.kotlin.KotlinNothingValueException();
    }

    private final java.lang.Void error() {
        throw new com.android.server.permission.jarjar.kotlin.jvm.KotlinReflectionNotSupportedError();
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public boolean equals(java.lang.Object other) {
        return (other instanceof com.android.server.permission.jarjar.kotlin.jvm.internal.ClassReference) && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(com.android.server.permission.jarjar.kotlin.jvm.JvmClassMappingKt.getJavaObjectType(this), com.android.server.permission.jarjar.kotlin.jvm.JvmClassMappingKt.getJavaObjectType((com.android.server.permission.jarjar.kotlin.reflect.KClass) other));
    }

    @Override // com.android.server.permission.jarjar.kotlin.reflect.KClass
    public int hashCode() {
        return com.android.server.permission.jarjar.kotlin.jvm.JvmClassMappingKt.getJavaObjectType(this).hashCode();
    }

    public java.lang.String toString() {
        return getJClass().toString() + " (Kotlin reflection is not available)";
    }

    /* JADX INFO: compiled from: ClassReference.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0014\u0010\u000f\u001a\u0004\u0018\u00010\n2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005J\u0014\u0010\u0011\u001a\u0004\u0018\u00010\n2\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005J\u001c\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u00012\n\u0010\u0010\u001a\u0006\u0012\u0002\b\u00030\u0005R&\u0010\u0003\u001a\u001a\u0012\u0010\u0012\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\u00060\u0005\u0012\u0004\u0012\u00020\u00070\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R*\u0010\b\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R*\u0010\f\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R*\u0010\r\u001a\u001e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\tj\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n`\u000bX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020\n0\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lkotlin/jvm/internal/ClassReference$Companion;", "", "()V", "FUNCTION_CLASSES", "", "Ljava/lang/Class;", "Lkotlin/Function;", "", "classFqNames", "Ljava/util/HashMap;", "", "Lkotlin/collections/HashMap;", "primitiveFqNames", "primitiveWrapperFqNames", "simpleNames", "getClassQualifiedName", "jClass", "getClassSimpleName", "isInstance", "", "value", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final java.lang.String getClassSimpleName(java.lang.Class<?> cls) {
            java.lang.String str;
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cls, "jClass");
            java.lang.String str2 = null;
            if (cls.isAnonymousClass()) {
                return null;
            }
            if (cls.isLocalClass()) {
                java.lang.String name = cls.getSimpleName();
                java.lang.reflect.Method method = cls.getEnclosingMethod();
                if (method != null) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(name);
                    java.lang.String strSubstringAfter$default = com.android.server.permission.jarjar.kotlin.text.StringsKt.substringAfter$default(name, method.getName() + '$', (java.lang.String) null, 2, (java.lang.Object) null);
                    if (strSubstringAfter$default != null) {
                        return strSubstringAfter$default;
                    }
                }
                java.lang.reflect.Constructor<?> enclosingConstructor = cls.getEnclosingConstructor();
                if (enclosingConstructor != null) {
                    com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(name);
                    return com.android.server.permission.jarjar.kotlin.text.StringsKt.substringAfter$default(name, enclosingConstructor.getName() + '$', (java.lang.String) null, 2, (java.lang.Object) null);
                }
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(name);
                return com.android.server.permission.jarjar.kotlin.text.StringsKt.substringAfter$default(name, '$', (java.lang.String) null, 2, (java.lang.Object) null);
            }
            if (!cls.isArray()) {
                java.lang.String str3 = (java.lang.String) com.android.server.permission.jarjar.kotlin.jvm.internal.ClassReference.simpleNames.get(cls.getName());
                return str3 == null ? cls.getSimpleName() : str3;
            }
            java.lang.Class<?> componentType = cls.getComponentType();
            if (componentType.isPrimitive() && (str = (java.lang.String) com.android.server.permission.jarjar.kotlin.jvm.internal.ClassReference.simpleNames.get(componentType.getName())) != null) {
                str2 = str + "Array";
            }
            return str2 == null ? "Array" : str2;
        }

        public final java.lang.String getClassQualifiedName(java.lang.Class<?> cls) {
            java.lang.String str;
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cls, "jClass");
            java.lang.String str2 = null;
            if (cls.isAnonymousClass() || cls.isLocalClass()) {
                return null;
            }
            if (!cls.isArray()) {
                java.lang.String str3 = (java.lang.String) com.android.server.permission.jarjar.kotlin.jvm.internal.ClassReference.classFqNames.get(cls.getName());
                return str3 == null ? cls.getCanonicalName() : str3;
            }
            java.lang.Class<?> componentType = cls.getComponentType();
            if (componentType.isPrimitive() && (str = (java.lang.String) com.android.server.permission.jarjar.kotlin.jvm.internal.ClassReference.classFqNames.get(componentType.getName())) != null) {
                str2 = str + "Array";
            }
            return str2 == null ? "com.android.server.permission.jarjar.kotlin.Array" : str2;
        }

        public final boolean isInstance(java.lang.Object value, java.lang.Class<?> cls) {
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cls, "jClass");
            java.util.Map map = com.android.server.permission.jarjar.kotlin.jvm.internal.ClassReference.FUNCTION_CLASSES;
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(map, "null cannot be cast to non-null type kotlin.collections.Map<K of kotlin.collections.MapsKt__MapsKt.get, V of kotlin.collections.MapsKt__MapsKt.get>");
            java.lang.Integer num = (java.lang.Integer) map.get(cls);
            if (num != null) {
                int arity = num.intValue();
                return com.android.server.permission.jarjar.kotlin.jvm.internal.TypeIntrinsics.isFunctionOfArity(value, arity);
            }
            return (cls.isPrimitive() ? com.android.server.permission.jarjar.kotlin.jvm.JvmClassMappingKt.getJavaObjectType(com.android.server.permission.jarjar.kotlin.jvm.JvmClassMappingKt.getKotlinClass(cls)) : cls).isInstance(value);
        }
    }

    static {
        java.lang.Iterable $this$mapIndexed$iv = com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.listOf((java.lang.Object[]) new java.lang.Class[]{com.android.server.permission.jarjar.kotlin.jvm.functions.Function0.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function2.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function3.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function4.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function5.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function6.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function7.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function8.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function9.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function10.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function11.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function12.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function13.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function14.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function15.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function16.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function17.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function18.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function19.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function20.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function21.class, com.android.server.permission.jarjar.kotlin.jvm.functions.Function22.class});
        java.util.Collection destination$iv$iv = new java.util.ArrayList(com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.collectionSizeOrDefault($this$mapIndexed$iv, 10));
        int index$iv$iv = 0;
        for (java.lang.Object item$iv$iv : $this$mapIndexed$iv) {
            int index$iv$iv2 = index$iv$iv + 1;
            if (index$iv$iv < 0) {
                com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.throwIndexOverflow();
            }
            java.lang.Class clazz = (java.lang.Class) item$iv$iv;
            destination$iv$iv.add(com.android.server.permission.jarjar.kotlin.TuplesKt.to(clazz, java.lang.Integer.valueOf(index$iv$iv)));
            index$iv$iv = index$iv$iv2;
        }
        FUNCTION_CLASSES = com.android.server.permission.jarjar.kotlin.collections.MapsKt.toMap((java.util.List) destination$iv$iv);
        java.util.HashMap<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("boolean", "com.android.server.permission.jarjar.kotlin.Boolean");
        map.put("char", "com.android.server.permission.jarjar.kotlin.Char");
        map.put("byte", "com.android.server.permission.jarjar.kotlin.Byte");
        map.put("short", "com.android.server.permission.jarjar.kotlin.Short");
        map.put("int", "com.android.server.permission.jarjar.kotlin.Int");
        map.put("float", "com.android.server.permission.jarjar.kotlin.Float");
        map.put("long", "com.android.server.permission.jarjar.kotlin.Long");
        map.put("double", "com.android.server.permission.jarjar.kotlin.Double");
        primitiveFqNames = map;
        java.util.HashMap<java.lang.String, java.lang.String> map2 = new java.util.HashMap<>();
        map2.put("java.lang.Boolean", "com.android.server.permission.jarjar.kotlin.Boolean");
        map2.put("java.lang.Character", "com.android.server.permission.jarjar.kotlin.Char");
        map2.put("java.lang.Byte", "com.android.server.permission.jarjar.kotlin.Byte");
        map2.put("java.lang.Short", "com.android.server.permission.jarjar.kotlin.Short");
        map2.put("java.lang.Integer", "com.android.server.permission.jarjar.kotlin.Int");
        map2.put("java.lang.Float", "com.android.server.permission.jarjar.kotlin.Float");
        map2.put("java.lang.Long", "com.android.server.permission.jarjar.kotlin.Long");
        map2.put("java.lang.Double", "com.android.server.permission.jarjar.kotlin.Double");
        primitiveWrapperFqNames = map2;
        java.util.HashMap<java.lang.String, java.lang.String> map3 = new java.util.HashMap<>();
        map3.put("java.lang.Object", "com.android.server.permission.jarjar.kotlin.Any");
        map3.put("java.lang.String", "com.android.server.permission.jarjar.kotlin.String");
        map3.put("java.lang.CharSequence", "com.android.server.permission.jarjar.kotlin.CharSequence");
        map3.put("java.lang.Throwable", "com.android.server.permission.jarjar.kotlin.Throwable");
        map3.put("java.lang.Cloneable", "com.android.server.permission.jarjar.kotlin.Cloneable");
        map3.put("java.lang.Number", "com.android.server.permission.jarjar.kotlin.Number");
        map3.put("java.lang.Comparable", "com.android.server.permission.jarjar.kotlin.Comparable");
        map3.put("java.lang.Enum", "com.android.server.permission.jarjar.kotlin.Enum");
        map3.put("java.lang.annotation.Annotation", "com.android.server.permission.jarjar.kotlin.Annotation");
        map3.put("java.lang.Iterable", "com.android.server.permission.jarjar.kotlin.collections.Iterable");
        map3.put("java.util.Iterator", "com.android.server.permission.jarjar.kotlin.collections.Iterator");
        map3.put("java.util.Collection", "com.android.server.permission.jarjar.kotlin.collections.Collection");
        map3.put("java.util.List", "com.android.server.permission.jarjar.kotlin.collections.List");
        map3.put("java.util.Set", "com.android.server.permission.jarjar.kotlin.collections.Set");
        map3.put("java.util.ListIterator", "com.android.server.permission.jarjar.kotlin.collections.ListIterator");
        map3.put("java.util.Map", "com.android.server.permission.jarjar.kotlin.collections.Map");
        map3.put("java.util.Map$Entry", "com.android.server.permission.jarjar.kotlin.collections.Map.Entry");
        map3.put("com.android.server.permission.jarjar.kotlin.jvm.internal.StringCompanionObject", "com.android.server.permission.jarjar.kotlin.String.Companion");
        map3.put("com.android.server.permission.jarjar.kotlin.jvm.internal.EnumCompanionObject", "com.android.server.permission.jarjar.kotlin.Enum.Companion");
        map3.putAll(primitiveFqNames);
        map3.putAll(primitiveWrapperFqNames);
        java.lang.Iterable iterableValues = primitiveFqNames.values();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(iterableValues, "<get-values>(...)");
        java.lang.Iterable $this$associateTo$iv = iterableValues;
        for (java.lang.Object element$iv : $this$associateTo$iv) {
            java.lang.String kotlinName = (java.lang.String) element$iv;
            java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("com.android.server.permission.jarjar.kotlin.jvm.internal.");
            com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(kotlinName);
            com.android.server.permission.jarjar.kotlin.Pair pair = com.android.server.permission.jarjar.kotlin.TuplesKt.to(sbAppend.append(com.android.server.permission.jarjar.kotlin.text.StringsKt.substringAfterLast$default(kotlinName, '.', (java.lang.String) null, 2, (java.lang.Object) null)).append("CompanionObject").toString(), kotlinName + ".Companion");
            map3.put(pair.getFirst(), pair.getSecond());
        }
        for (java.util.Map.Entry<java.lang.Class<? extends com.android.server.permission.jarjar.kotlin.Function<?>>, java.lang.Integer> entry : FUNCTION_CLASSES.entrySet()) {
            java.lang.Class<? extends com.android.server.permission.jarjar.kotlin.Function<?>> key = entry.getKey();
            int arity = entry.getValue().intValue();
            map3.put(key.getName(), "com.android.server.permission.jarjar.kotlin.Function" + arity);
        }
        classFqNames = map3;
        java.util.Map $this$mapValues$iv = classFqNames;
        java.util.Map destination$iv$iv2 = new java.util.LinkedHashMap(com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapCapacity($this$mapValues$iv.size()));
        java.lang.Iterable $this$associateByTo$iv$iv$iv = $this$mapValues$iv.entrySet();
        for (java.lang.Object element$iv$iv$iv : $this$associateByTo$iv$iv$iv) {
            java.util.Map.Entry it$iv$iv = (java.util.Map.Entry) element$iv$iv$iv;
            java.lang.Object key2 = it$iv$iv.getKey();
            java.lang.String fqName = (java.lang.String) ((java.util.Map.Entry) element$iv$iv$iv).getValue();
            destination$iv$iv2.put(key2, com.android.server.permission.jarjar.kotlin.text.StringsKt.substringAfterLast$default(fqName, '.', (java.lang.String) null, 2, (java.lang.Object) null));
        }
        simpleNames = destination$iv$iv2;
    }
}
