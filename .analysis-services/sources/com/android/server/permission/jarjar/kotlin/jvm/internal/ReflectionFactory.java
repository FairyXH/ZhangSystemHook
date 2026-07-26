package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: loaded from: classes2.dex */
public class ReflectionFactory {
    private static final java.lang.String KOTLIN_JVM_FUNCTIONS = "com.android.server.permission.jarjar.kotlin.jvm.functions.";

    public com.android.server.permission.jarjar.kotlin.reflect.KClass createKotlinClass(java.lang.Class javaClass) {
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.ClassReference(javaClass);
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KClass createKotlinClass(java.lang.Class javaClass, java.lang.String internalName) {
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.ClassReference(javaClass);
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KDeclarationContainer getOrCreateKotlinPackage(java.lang.Class javaClass, java.lang.String moduleName) {
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.PackageReference(javaClass, moduleName);
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KClass getOrCreateKotlinClass(java.lang.Class javaClass) {
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.ClassReference(javaClass);
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KClass getOrCreateKotlinClass(java.lang.Class javaClass, java.lang.String internalName) {
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.ClassReference(javaClass);
    }

    public java.lang.String renderLambdaToString(com.android.server.permission.jarjar.kotlin.jvm.internal.Lambda lambda) {
        return renderLambdaToString((com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionBase) lambda);
    }

    public java.lang.String renderLambdaToString(com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionBase lambda) {
        java.lang.String result = lambda.getClass().getGenericInterfaces()[0].toString();
        return result.startsWith(KOTLIN_JVM_FUNCTIONS) ? result.substring(KOTLIN_JVM_FUNCTIONS.length()) : result;
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KFunction function(com.android.server.permission.jarjar.kotlin.jvm.internal.FunctionReference f) {
        return f;
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KProperty0 property0(com.android.server.permission.jarjar.kotlin.jvm.internal.PropertyReference0 p) {
        return p;
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KMutableProperty0 mutableProperty0(com.android.server.permission.jarjar.kotlin.jvm.internal.MutablePropertyReference0 p) {
        return p;
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KProperty1 property1(com.android.server.permission.jarjar.kotlin.jvm.internal.PropertyReference1 p) {
        return p;
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KMutableProperty1 mutableProperty1(com.android.server.permission.jarjar.kotlin.jvm.internal.MutablePropertyReference1 p) {
        return p;
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KProperty2 property2(com.android.server.permission.jarjar.kotlin.jvm.internal.PropertyReference2 p) {
        return p;
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KMutableProperty2 mutableProperty2(com.android.server.permission.jarjar.kotlin.jvm.internal.MutablePropertyReference2 p) {
        return p;
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KType typeOf(com.android.server.permission.jarjar.kotlin.reflect.KClassifier klass, java.util.List<com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection> arguments, boolean isMarkedNullable) {
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.TypeReference(klass, arguments, isMarkedNullable);
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KTypeParameter typeParameter(java.lang.Object container, java.lang.String name, com.android.server.permission.jarjar.kotlin.reflect.KVariance variance, boolean isReified) {
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.TypeParameterReference(container, name, variance, isReified);
    }

    public void setUpperBounds(com.android.server.permission.jarjar.kotlin.reflect.KTypeParameter typeParameter, java.util.List<com.android.server.permission.jarjar.kotlin.reflect.KType> bounds) {
        ((com.android.server.permission.jarjar.kotlin.jvm.internal.TypeParameterReference) typeParameter).setUpperBounds(bounds);
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KType platformType(com.android.server.permission.jarjar.kotlin.reflect.KType lowerBound, com.android.server.permission.jarjar.kotlin.reflect.KType upperBound) {
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.TypeReference(lowerBound.getClassifier(), lowerBound.getArguments(), upperBound, ((com.android.server.permission.jarjar.kotlin.jvm.internal.TypeReference) lowerBound).getFlags$kotlin_stdlib());
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KType mutableCollectionType(com.android.server.permission.jarjar.kotlin.reflect.KType type) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.TypeReference typeRef = (com.android.server.permission.jarjar.kotlin.jvm.internal.TypeReference) type;
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.TypeReference(type.getClassifier(), type.getArguments(), typeRef.getPlatformTypeUpperBound$kotlin_stdlib(), typeRef.getFlags$kotlin_stdlib() | 2);
    }

    public com.android.server.permission.jarjar.kotlin.reflect.KType nothingType(com.android.server.permission.jarjar.kotlin.reflect.KType type) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.TypeReference typeRef = (com.android.server.permission.jarjar.kotlin.jvm.internal.TypeReference) type;
        return new com.android.server.permission.jarjar.kotlin.jvm.internal.TypeReference(type.getClassifier(), type.getArguments(), typeRef.getPlatformTypeUpperBound$kotlin_stdlib(), typeRef.getFlags$kotlin_stdlib() | 4);
    }
}
