package kotlin.jvm;

/* JADX INFO: compiled from: JvmClassMapping.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0010\n\u0002\b\n\n\u0002\u0010\u0000\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\u0010\u0011\n\u0002\b\u0002\u001a\u001f\u0010\u001f\u001a\u00020 \"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\u0014*\u0006\u0012\u0002\b\u00030!¢\u0006\u0002\u0010\"\"'\u0010\u0000\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0003*\u0002H\u00028F¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\";\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u000e\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\t*\b\u0012\u0004\u0012\u0002H\b0\t8Æ\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\n\u0010\u000b\u001a\u0004\b\f\u0010\r\"-\u0010\u000e\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00018G¢\u0006\f\u0012\u0004\b\u000f\u0010\u0010\u001a\u0004\b\u0011\u0010\u0012\"&\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\b\b\u0000\u0010\u0002*\u00020\u0014*\u0002H\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0015\";\u0010\u0013\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0007\"\b\b\u0000\u0010\u0002*\u00020\u0014*\b\u0012\u0004\u0012\u0002H\u00020\u00018Ç\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0016\u0010\u0010\u001a\u0004\b\u0017\u0010\u0012\"+\u0010\u0018\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\b\b\u0000\u0010\u0002*\u00020\u0014*\b\u0012\u0004\u0012\u0002H\u00020\u00018F¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u0012\"-\u0010\u001a\u001a\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0007\"\b\b\u0000\u0010\u0002*\u00020\u0014*\b\u0012\u0004\u0012\u0002H\u00020\u00018F¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u0012\"+\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0014*\b\u0012\u0004\u0012\u0002H\u00020\u00078G¢\u0006\u0006\u001a\u0004\b\u001d\u0010\u001e¨\u0006#"}, d2 = {"annotationClass", "Lkotlin/reflect/KClass;", "T", "", "getAnnotationClass", "(Ljava/lang/annotation/Annotation;)Lkotlin/reflect/KClass;", "declaringJavaClass", "Ljava/lang/Class;", "E", "", "getDeclaringJavaClass$annotations", "(Ljava/lang/Enum;)V", "getDeclaringJavaClass", "(Ljava/lang/Enum;)Ljava/lang/Class;", "java", "getJavaClass$annotations", "(Lkotlin/reflect/KClass;)V", "getJavaClass", "(Lkotlin/reflect/KClass;)Ljava/lang/Class;", "javaClass", "", "(Ljava/lang/Object;)Ljava/lang/Class;", "getRuntimeClassOfKClassInstance$annotations", "getRuntimeClassOfKClassInstance", "javaObjectType", "getJavaObjectType", "javaPrimitiveType", "getJavaPrimitiveType", "kotlin", "getKotlinClass", "(Ljava/lang/Class;)Lkotlin/reflect/KClass;", "isArrayOf", "", "", "([Ljava/lang/Object;)Z", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class JvmClassMappingKt {
    public static /* synthetic */ void getDeclaringJavaClass$annotations(java.lang.Enum r0) {
    }

    public static /* synthetic */ void getJavaClass$annotations(kotlin.reflect.KClass kClass) {
    }

    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Use 'java' property to get Java class corresponding to this Kotlin class or cast this instance to Any if you really want to get the runtime Java class of this implementation of KClass.", replaceWith = @kotlin.ReplaceWith(expression = "(this as Any).javaClass", imports = {}))
    public static /* synthetic */ void getRuntimeClassOfKClassInstance$annotations(kotlin.reflect.KClass kClass) {
    }

    public static final <T> java.lang.Class<T> getJavaClass(kotlin.reflect.KClass<T> kClass) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(kClass, "<this>");
        java.lang.Class<T> cls = (java.lang.Class<T>) ((kotlin.jvm.internal.ClassBasedDeclarationContainer) kClass).getJClass();
        kotlin.jvm.internal.Intrinsics.checkNotNull(cls, "null cannot be cast to non-null type java.lang.Class<T of kotlin.jvm.JvmClassMappingKt.<get-java>>");
        return cls;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static final <T> java.lang.Class<T> getJavaPrimitiveType(kotlin.reflect.KClass<T> kClass) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(kClass, "<this>");
        java.lang.Class thisJClass = ((kotlin.jvm.internal.ClassBasedDeclarationContainer) kClass).getJClass();
        if (thisJClass.isPrimitive()) {
            kotlin.jvm.internal.Intrinsics.checkNotNull(thisJClass, "null cannot be cast to non-null type java.lang.Class<T of kotlin.jvm.JvmClassMappingKt.<get-javaPrimitiveType>>");
            return thisJClass;
        }
        java.lang.String name = thisJClass.getName();
        if (name != null) {
            switch (name.hashCode()) {
                case -2056817302:
                    if (name.equals("java.lang.Integer")) {
                        return java.lang.Integer.TYPE;
                    }
                    break;
                case -527879800:
                    if (name.equals("java.lang.Float")) {
                        return java.lang.Float.TYPE;
                    }
                    break;
                case -515992664:
                    if (name.equals("java.lang.Short")) {
                        return java.lang.Short.TYPE;
                    }
                    break;
                case 155276373:
                    if (name.equals("java.lang.Character")) {
                        return java.lang.Character.TYPE;
                    }
                    break;
                case 344809556:
                    if (name.equals("java.lang.Boolean")) {
                        return java.lang.Boolean.TYPE;
                    }
                    break;
                case 398507100:
                    if (name.equals("java.lang.Byte")) {
                        return java.lang.Byte.TYPE;
                    }
                    break;
                case 398795216:
                    if (name.equals("java.lang.Long")) {
                        return java.lang.Long.TYPE;
                    }
                    break;
                case 399092968:
                    if (name.equals("java.lang.Void")) {
                        return java.lang.Void.TYPE;
                    }
                    break;
                case 761287205:
                    if (name.equals("java.lang.Double")) {
                        return java.lang.Double.TYPE;
                    }
                    break;
            }
        }
        return null;
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    public static final <T> java.lang.Class<T> getJavaObjectType(kotlin.reflect.KClass<T> kClass) {
        java.lang.Class cls;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(kClass, "<this>");
        java.lang.Class thisJClass = ((kotlin.jvm.internal.ClassBasedDeclarationContainer) kClass).getJClass();
        if (!thisJClass.isPrimitive()) {
            kotlin.jvm.internal.Intrinsics.checkNotNull(thisJClass, "null cannot be cast to non-null type java.lang.Class<T of kotlin.jvm.JvmClassMappingKt.<get-javaObjectType>>");
            return thisJClass;
        }
        java.lang.String name = thisJClass.getName();
        if (name != null) {
            switch (name.hashCode()) {
                case -1325958191:
                    cls = !name.equals("double") ? thisJClass : java.lang.Double.class;
                    break;
                case 104431:
                    cls = !name.equals("int") ? thisJClass : java.lang.Integer.class;
                    break;
                case 3039496:
                    cls = !name.equals("byte") ? thisJClass : java.lang.Byte.class;
                    break;
                case 3052374:
                    cls = !name.equals("char") ? thisJClass : java.lang.Character.class;
                    break;
                case 3327612:
                    cls = !name.equals("long") ? thisJClass : java.lang.Long.class;
                    break;
                case 3625364:
                    cls = !name.equals("void") ? thisJClass : java.lang.Void.class;
                    break;
                case 64711720:
                    cls = !name.equals("boolean") ? thisJClass : java.lang.Boolean.class;
                    break;
                case 97526364:
                    cls = !name.equals("float") ? thisJClass : java.lang.Float.class;
                    break;
                case 109413500:
                    cls = !name.equals("short") ? thisJClass : java.lang.Short.class;
                    break;
                default:
                    cls = thisJClass;
                    break;
            }
        } else {
            cls = thisJClass;
        }
        kotlin.jvm.internal.Intrinsics.checkNotNull(cls, "null cannot be cast to non-null type java.lang.Class<T of kotlin.jvm.JvmClassMappingKt.<get-javaObjectType>>");
        return cls;
    }

    public static final <T> kotlin.reflect.KClass<T> getKotlinClass(java.lang.Class<T> cls) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cls, "<this>");
        return kotlin.jvm.internal.Reflection.getOrCreateKotlinClass(cls);
    }

    public static final <T> java.lang.Class<T> getJavaClass(T t) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "<this>");
        java.lang.Class<T> cls = (java.lang.Class<T>) t.getClass();
        kotlin.jvm.internal.Intrinsics.checkNotNull(cls, "null cannot be cast to non-null type java.lang.Class<T of kotlin.jvm.JvmClassMappingKt.<get-javaClass>>");
        return cls;
    }

    public static final <T> java.lang.Class<kotlin.reflect.KClass<T>> getRuntimeClassOfKClassInstance(kotlin.reflect.KClass<T> kClass) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(kClass, "<this>");
        java.lang.Class<kotlin.reflect.KClass<T>> cls = (java.lang.Class<kotlin.reflect.KClass<T>>) kClass.getClass();
        kotlin.jvm.internal.Intrinsics.checkNotNull(cls, "null cannot be cast to non-null type java.lang.Class<kotlin.reflect.KClass<T of kotlin.jvm.JvmClassMappingKt.<get-javaClass>>>");
        return cls;
    }

    public static final /* synthetic */ boolean isArrayOf(java.lang.Object[] $this$isArrayOf) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$isArrayOf, "<this>");
        kotlin.jvm.internal.Intrinsics.reifiedOperationMarker(4, "T");
        return java.lang.Object.class.isAssignableFrom($this$isArrayOf.getClass().getComponentType());
    }

    public static final <T extends java.lang.annotation.Annotation> kotlin.reflect.KClass<? extends T> getAnnotationClass(T t) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(t, "<this>");
        java.lang.Class<? extends java.lang.annotation.Annotation> clsAnnotationType = t.annotationType();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(clsAnnotationType, "annotationType(...)");
        kotlin.reflect.KClass<? extends T> kotlinClass = getKotlinClass(clsAnnotationType);
        kotlin.jvm.internal.Intrinsics.checkNotNull(kotlinClass, "null cannot be cast to non-null type kotlin.reflect.KClass<out T of kotlin.jvm.JvmClassMappingKt.<get-annotationClass>>");
        return kotlinClass;
    }

    private static final <E extends java.lang.Enum<E>> java.lang.Class<E> getDeclaringJavaClass(java.lang.Enum<E> r2) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, "<this>");
        java.lang.Class<E> declaringClass = r2.getDeclaringClass();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(declaringClass, "getDeclaringClass(...)");
        return declaringClass;
    }
}
