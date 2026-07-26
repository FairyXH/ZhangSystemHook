package kotlin.jvm.internal;

/* JADX INFO: loaded from: classes3.dex */
public class ReflectionFactory {
    private static final java.lang.String KOTLIN_JVM_FUNCTIONS = "kotlin.jvm.functions.";

    public kotlin.reflect.KClass createKotlinClass(java.lang.Class javaClass) {
        return new kotlin.jvm.internal.ClassReference(javaClass);
    }

    public kotlin.reflect.KClass createKotlinClass(java.lang.Class javaClass, java.lang.String internalName) {
        return new kotlin.jvm.internal.ClassReference(javaClass);
    }

    public kotlin.reflect.KDeclarationContainer getOrCreateKotlinPackage(java.lang.Class javaClass, java.lang.String moduleName) {
        return new kotlin.jvm.internal.PackageReference(javaClass, moduleName);
    }

    public kotlin.reflect.KClass getOrCreateKotlinClass(java.lang.Class javaClass) {
        return new kotlin.jvm.internal.ClassReference(javaClass);
    }

    public kotlin.reflect.KClass getOrCreateKotlinClass(java.lang.Class javaClass, java.lang.String internalName) {
        return new kotlin.jvm.internal.ClassReference(javaClass);
    }

    public java.lang.String renderLambdaToString(kotlin.jvm.internal.Lambda lambda) {
        return renderLambdaToString((kotlin.jvm.internal.FunctionBase) lambda);
    }

    public java.lang.String renderLambdaToString(kotlin.jvm.internal.FunctionBase lambda) {
        java.lang.String result = lambda.getClass().getGenericInterfaces()[0].toString();
        return result.startsWith(KOTLIN_JVM_FUNCTIONS) ? result.substring(KOTLIN_JVM_FUNCTIONS.length()) : result;
    }

    public kotlin.reflect.KFunction function(kotlin.jvm.internal.FunctionReference f) {
        return f;
    }

    public kotlin.reflect.KProperty0 property0(kotlin.jvm.internal.PropertyReference0 p) {
        return p;
    }

    public kotlin.reflect.KMutableProperty0 mutableProperty0(kotlin.jvm.internal.MutablePropertyReference0 p) {
        return p;
    }

    public kotlin.reflect.KProperty1 property1(kotlin.jvm.internal.PropertyReference1 p) {
        return p;
    }

    public kotlin.reflect.KMutableProperty1 mutableProperty1(kotlin.jvm.internal.MutablePropertyReference1 p) {
        return p;
    }

    public kotlin.reflect.KProperty2 property2(kotlin.jvm.internal.PropertyReference2 p) {
        return p;
    }

    public kotlin.reflect.KMutableProperty2 mutableProperty2(kotlin.jvm.internal.MutablePropertyReference2 p) {
        return p;
    }

    public kotlin.reflect.KType typeOf(kotlin.reflect.KClassifier klass, java.util.List<kotlin.reflect.KTypeProjection> arguments, boolean isMarkedNullable) {
        return new kotlin.jvm.internal.TypeReference(klass, arguments, isMarkedNullable);
    }

    public kotlin.reflect.KTypeParameter typeParameter(java.lang.Object container, java.lang.String name, kotlin.reflect.KVariance variance, boolean isReified) {
        return new kotlin.jvm.internal.TypeParameterReference(container, name, variance, isReified);
    }

    public void setUpperBounds(kotlin.reflect.KTypeParameter typeParameter, java.util.List<kotlin.reflect.KType> bounds) {
        ((kotlin.jvm.internal.TypeParameterReference) typeParameter).setUpperBounds(bounds);
    }

    public kotlin.reflect.KType platformType(kotlin.reflect.KType lowerBound, kotlin.reflect.KType upperBound) {
        return new kotlin.jvm.internal.TypeReference(lowerBound.getClassifier(), lowerBound.getArguments(), upperBound, ((kotlin.jvm.internal.TypeReference) lowerBound).getFlags());
    }

    public kotlin.reflect.KType mutableCollectionType(kotlin.reflect.KType type) {
        kotlin.jvm.internal.TypeReference typeRef = (kotlin.jvm.internal.TypeReference) type;
        return new kotlin.jvm.internal.TypeReference(type.getClassifier(), type.getArguments(), typeRef.getPlatformTypeUpperBound(), typeRef.getFlags() | 2);
    }

    public kotlin.reflect.KType nothingType(kotlin.reflect.KType type) {
        kotlin.jvm.internal.TypeReference typeRef = (kotlin.jvm.internal.TypeReference) type;
        return new kotlin.jvm.internal.TypeReference(type.getClassifier(), type.getArguments(), typeRef.getPlatformTypeUpperBound(), typeRef.getFlags() | 4);
    }
}
