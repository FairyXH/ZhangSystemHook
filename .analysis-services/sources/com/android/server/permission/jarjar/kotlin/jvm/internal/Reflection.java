package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: loaded from: classes2.dex */
public class Reflection {
    private static final com.android.server.permission.jarjar.kotlin.reflect.KClass[] EMPTY_K_CLASS_ARRAY;
    static final java.lang.String REFLECTION_NOT_AVAILABLE = " (Kotlin reflection is not available)";
    private static final com.android.server.permission.jarjar.kotlin.jvm.internal.ReflectionFactory factory;

    static {
        com.android.server.permission.jarjar.kotlin.jvm.internal.ReflectionFactory impl;
        try {
            java.lang.Class<?> implClass = java.lang.Class.forName("com.android.server.permission.jarjar.kotlin.reflect.jvm.internal.ReflectionFactoryImpl");
            impl = (com.android.server.permission.jarjar.kotlin.jvm.internal.ReflectionFactory) implClass.newInstance();
        } catch (java.lang.ClassCastException e) {
            impl = null;
        } catch (java.lang.ClassNotFoundException e2) {
            impl = null;
        } catch (java.lang.IllegalAccessException e3) {
            impl = null;
        } catch (java.lang.InstantiationException e4) {
            impl = null;
        }
        factory = impl != null ? impl : new com.android.server.permission.jarjar.kotlin.jvm.internal.ReflectionFactory();
        EMPTY_K_CLASS_ARRAY = new com.android.server.permission.jarjar.kotlin.reflect.KClass[0];
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KClass createKotlinClass(java.lang.Class javaClass) {
        return factory.createKotlinClass(javaClass);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KClass createKotlinClass(java.lang.Class javaClass, java.lang.String internalName) {
        return factory.createKotlinClass(javaClass, internalName);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KDeclarationContainer getOrCreateKotlinPackage(java.lang.Class javaClass) {
        return factory.getOrCreateKotlinPackage(javaClass, "");
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KDeclarationContainer getOrCreateKotlinPackage(java.lang.Class javaClass, java.lang.String moduleName) {
        return factory.getOrCreateKotlinPackage(javaClass, moduleName);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KClass getOrCreateKotlinClass(java.lang.Class javaClass) {
        return factory.getOrCreateKotlinClass(javaClass);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KClass getOrCreateKotlinClass(java.lang.Class javaClass, java.lang.String internalName) {
        return factory.getOrCreateKotlinClass(javaClass, internalName);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KClass[] getOrCreateKotlinClasses(java.lang.Class[] javaClasses) {
        int size = javaClasses.length;
        if (size == 0) {
            return EMPTY_K_CLASS_ARRAY;
        }
        com.android.server.permission.jarjar.kotlin.reflect.KClass[] kClasses = new com.android.server.permission.jarjar.kotlin.reflect.KClass[size];
        for (int i = 0; i < size; i++) {
            kClasses[i] = getOrCreateKotlinClass(javaClasses[i]);
        }
        return kClasses;
    }

    public static java.lang.String renderLambdaToString(com.android.server.permission.jarjar.kotlin.jvm.internal.Lambda lambda) {
        return factory.renderLambdaToString(lambda);
    }

    public static java.lang.String renderLambdaToString(com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionBase lambda) {
        return factory.renderLambdaToString(lambda);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KFunction function(com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionReference f) {
        return factory.function(f);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KProperty0 property0(com.android.server.permission.jarjar.kotlin.jvm.internal.PropertyReference0 p) {
        return factory.property0(p);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KMutableProperty0 mutableProperty0(com.android.server.permission.jarjar.kotlin.jvm.internal.MutablePropertyReference0 p) {
        return factory.mutableProperty0(p);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KProperty1 property1(com.android.server.permission.jarjar.kotlin.jvm.internal.PropertyReference1 p) {
        return factory.property1(p);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KMutableProperty1 mutableProperty1(com.android.server.permission.jarjar.kotlin.jvm.internal.MutablePropertyReference1 p) {
        return factory.mutableProperty1(p);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KProperty2 property2(com.android.server.permission.jarjar.kotlin.jvm.internal.PropertyReference2 p) {
        return factory.property2(p);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KMutableProperty2 mutableProperty2(com.android.server.permission.jarjar.kotlin.jvm.internal.MutablePropertyReference2 p) {
        return factory.mutableProperty2(p);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KType typeOf(com.android.server.permission.jarjar.kotlin.reflect.KClassifier classifier) {
        return factory.typeOf(classifier, java.util.Collections.emptyList(), false);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KType typeOf(java.lang.Class klass) {
        return factory.typeOf(getOrCreateKotlinClass(klass), java.util.Collections.emptyList(), false);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KType typeOf(java.lang.Class klass, com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection arg1) {
        return factory.typeOf(getOrCreateKotlinClass(klass), java.util.Collections.singletonList(arg1), false);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KType typeOf(java.lang.Class klass, com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection arg1, com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection arg2) {
        return factory.typeOf(getOrCreateKotlinClass(klass), java.util.Arrays.asList(arg1, arg2), false);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KType typeOf(java.lang.Class klass, com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection... arguments) {
        return factory.typeOf(getOrCreateKotlinClass(klass), com.android.server.permission.jarjar.kotlin.collections.ArraysKt.toList(arguments), false);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KType nullableTypeOf(com.android.server.permission.jarjar.kotlin.reflect.KClassifier classifier) {
        return factory.typeOf(classifier, java.util.Collections.emptyList(), true);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KType nullableTypeOf(java.lang.Class klass) {
        return factory.typeOf(getOrCreateKotlinClass(klass), java.util.Collections.emptyList(), true);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KType nullableTypeOf(java.lang.Class klass, com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection arg1) {
        return factory.typeOf(getOrCreateKotlinClass(klass), java.util.Collections.singletonList(arg1), true);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KType nullableTypeOf(java.lang.Class klass, com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection arg1, com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection arg2) {
        return factory.typeOf(getOrCreateKotlinClass(klass), java.util.Arrays.asList(arg1, arg2), true);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KType nullableTypeOf(java.lang.Class klass, com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection... arguments) {
        return factory.typeOf(getOrCreateKotlinClass(klass), com.android.server.permission.jarjar.kotlin.collections.ArraysKt.toList(arguments), true);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KTypeParameter typeParameter(java.lang.Object container, java.lang.String name, com.android.server.permission.jarjar.kotlin.reflect.KVariance variance, boolean isReified) {
        return factory.typeParameter(container, name, variance, isReified);
    }

    public static void setUpperBounds(com.android.server.permission.jarjar.kotlin.reflect.KTypeParameter typeParameter, com.android.server.permission.jarjar.kotlin.reflect.KType bound) {
        factory.setUpperBounds(typeParameter, java.util.Collections.singletonList(bound));
    }

    public static void setUpperBounds(com.android.server.permission.jarjar.kotlin.reflect.KTypeParameter typeParameter, com.android.server.permission.jarjar.kotlin.reflect.KType... bounds) {
        factory.setUpperBounds(typeParameter, com.android.server.permission.jarjar.kotlin.collections.ArraysKt.toList(bounds));
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KType platformType(com.android.server.permission.jarjar.kotlin.reflect.KType lowerBound, com.android.server.permission.jarjar.kotlin.reflect.KType upperBound) {
        return factory.platformType(lowerBound, upperBound);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KType mutableCollectionType(com.android.server.permission.jarjar.kotlin.reflect.KType type) {
        return factory.mutableCollectionType(type);
    }

    public static com.android.server.permission.jarjar.kotlin.reflect.KType nothingType(com.android.server.permission.jarjar.kotlin.reflect.KType type) {
        return factory.nothingType(type);
    }
}
