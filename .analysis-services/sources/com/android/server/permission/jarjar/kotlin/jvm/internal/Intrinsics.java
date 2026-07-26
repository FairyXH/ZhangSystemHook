package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: loaded from: classes2.dex */
public class Intrinsics {
    private Intrinsics() {
    }

    public static java.lang.String stringPlus(java.lang.String self, java.lang.Object other) {
        return self + other;
    }

    public static void checkNotNull(java.lang.Object object) {
        if (object == null) {
            throwJavaNpe();
        }
    }

    public static void checkNotNull(java.lang.Object object, java.lang.String message) {
        if (object == null) {
            throwJavaNpe(message);
        }
    }

    public static void throwNpe() {
        throw ((com.android.server.permission.jarjar.kotlin.KotlinNullPointerException) sanitizeStackTrace(new com.android.server.permission.jarjar.kotlin.KotlinNullPointerException()));
    }

    public static void throwNpe(java.lang.String message) {
        throw ((com.android.server.permission.jarjar.kotlin.KotlinNullPointerException) sanitizeStackTrace(new com.android.server.permission.jarjar.kotlin.KotlinNullPointerException(message)));
    }

    public static void throwJavaNpe() {
        throw ((java.lang.NullPointerException) sanitizeStackTrace(new java.lang.NullPointerException()));
    }

    public static void throwJavaNpe(java.lang.String message) {
        throw ((java.lang.NullPointerException) sanitizeStackTrace(new java.lang.NullPointerException(message)));
    }

    public static void throwUninitializedProperty(java.lang.String message) {
        throw ((com.android.server.permission.jarjar.kotlin.UninitializedPropertyAccessException) sanitizeStackTrace(new com.android.server.permission.jarjar.kotlin.UninitializedPropertyAccessException(message)));
    }

    public static void throwUninitializedPropertyAccessException(java.lang.String propertyName) {
        throwUninitializedProperty("lateinit property " + propertyName + " has not been initialized");
    }

    public static void throwAssert() {
        throw ((java.lang.AssertionError) sanitizeStackTrace(new java.lang.AssertionError()));
    }

    public static void throwAssert(java.lang.String message) {
        throw ((java.lang.AssertionError) sanitizeStackTrace(new java.lang.AssertionError(message)));
    }

    public static void throwIllegalArgument() {
        throw ((java.lang.IllegalArgumentException) sanitizeStackTrace(new java.lang.IllegalArgumentException()));
    }

    public static void throwIllegalArgument(java.lang.String message) {
        throw ((java.lang.IllegalArgumentException) sanitizeStackTrace(new java.lang.IllegalArgumentException(message)));
    }

    public static void throwIllegalState() {
        throw ((java.lang.IllegalStateException) sanitizeStackTrace(new java.lang.IllegalStateException()));
    }

    public static void throwIllegalState(java.lang.String message) {
        throw ((java.lang.IllegalStateException) sanitizeStackTrace(new java.lang.IllegalStateException(message)));
    }

    public static void checkExpressionValueIsNotNull(java.lang.Object value, java.lang.String expression) {
        if (value == null) {
            throw ((java.lang.IllegalStateException) sanitizeStackTrace(new java.lang.IllegalStateException(expression + " must not be null")));
        }
    }

    public static void checkNotNullExpressionValue(java.lang.Object value, java.lang.String expression) {
        if (value == null) {
            throw ((java.lang.NullPointerException) sanitizeStackTrace(new java.lang.NullPointerException(expression + " must not be null")));
        }
    }

    public static void checkReturnedValueIsNotNull(java.lang.Object value, java.lang.String className, java.lang.String methodName) {
        if (value == null) {
            throw ((java.lang.IllegalStateException) sanitizeStackTrace(new java.lang.IllegalStateException("Method specified as non-null returned null: " + className + "." + methodName)));
        }
    }

    public static void checkReturnedValueIsNotNull(java.lang.Object value, java.lang.String message) {
        if (value == null) {
            throw ((java.lang.IllegalStateException) sanitizeStackTrace(new java.lang.IllegalStateException(message)));
        }
    }

    public static void checkFieldIsNotNull(java.lang.Object value, java.lang.String className, java.lang.String fieldName) {
        if (value == null) {
            throw ((java.lang.IllegalStateException) sanitizeStackTrace(new java.lang.IllegalStateException("Field specified as non-null is null: " + className + "." + fieldName)));
        }
    }

    public static void checkFieldIsNotNull(java.lang.Object value, java.lang.String message) {
        if (value == null) {
            throw ((java.lang.IllegalStateException) sanitizeStackTrace(new java.lang.IllegalStateException(message)));
        }
    }

    public static void checkParameterIsNotNull(java.lang.Object value, java.lang.String paramName) {
        if (value == null) {
            throwParameterIsNullIAE(paramName);
        }
    }

    public static void checkNotNullParameter(java.lang.Object value, java.lang.String paramName) {
        if (value == null) {
            throwParameterIsNullNPE(paramName);
        }
    }

    private static void throwParameterIsNullIAE(java.lang.String paramName) {
        throw ((java.lang.IllegalArgumentException) sanitizeStackTrace(new java.lang.IllegalArgumentException(createParameterIsNullExceptionMessage(paramName))));
    }

    private static void throwParameterIsNullNPE(java.lang.String paramName) {
        throw ((java.lang.NullPointerException) sanitizeStackTrace(new java.lang.NullPointerException(createParameterIsNullExceptionMessage(paramName))));
    }

    private static java.lang.String createParameterIsNullExceptionMessage(java.lang.String paramName) {
        java.lang.StackTraceElement[] stackTraceElements = java.lang.Thread.currentThread().getStackTrace();
        java.lang.String thisClassName = com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.class.getName();
        int i = 0;
        while (!stackTraceElements[i].getClassName().equals(thisClassName)) {
            i++;
        }
        while (stackTraceElements[i].getClassName().equals(thisClassName)) {
            i++;
        }
        java.lang.StackTraceElement caller = stackTraceElements[i];
        java.lang.String className = caller.getClassName();
        java.lang.String methodName = caller.getMethodName();
        return "Parameter specified as non-null is null: method " + className + "." + methodName + ", parameter " + paramName;
    }

    public static int compare(long thisVal, long anotherVal) {
        if (thisVal < anotherVal) {
            return -1;
        }
        return thisVal == anotherVal ? 0 : 1;
    }

    public static int compare(int thisVal, int anotherVal) {
        if (thisVal < anotherVal) {
            return -1;
        }
        return thisVal == anotherVal ? 0 : 1;
    }

    public static boolean areEqual(java.lang.Object first, java.lang.Object second) {
        return first == null ? second == null : first.equals(second);
    }

    public static boolean areEqual(java.lang.Double first, java.lang.Double second) {
        if (first == null) {
            if (second == null) {
                return true;
            }
        } else if (second != null && first.doubleValue() == second.doubleValue()) {
            return true;
        }
        return false;
    }

    public static boolean areEqual(java.lang.Double first, double second) {
        return first != null && first.doubleValue() == second;
    }

    public static boolean areEqual(double first, java.lang.Double second) {
        return second != null && first == second.doubleValue();
    }

    public static boolean areEqual(java.lang.Float first, java.lang.Float second) {
        if (first == null) {
            if (second == null) {
                return true;
            }
        } else if (second != null && first.floatValue() == second.floatValue()) {
            return true;
        }
        return false;
    }

    public static boolean areEqual(java.lang.Float first, float second) {
        return first != null && first.floatValue() == second;
    }

    public static boolean areEqual(float first, java.lang.Float second) {
        return second != null && first == second.floatValue();
    }

    public static void throwUndefinedForReified() {
        throwUndefinedForReified("This function has a reified type parameter and thus can only be inlined at compilation time, not called directly.");
    }

    public static void throwUndefinedForReified(java.lang.String message) {
        throw new java.lang.UnsupportedOperationException(message);
    }

    public static void reifiedOperationMarker(int id, java.lang.String typeParameterIdentifier) {
        throwUndefinedForReified();
    }

    public static void reifiedOperationMarker(int id, java.lang.String typeParameterIdentifier, java.lang.String message) {
        throwUndefinedForReified(message);
    }

    public static void needClassReification() {
        throwUndefinedForReified();
    }

    public static void needClassReification(java.lang.String message) {
        throwUndefinedForReified(message);
    }

    public static void checkHasClass(java.lang.String internalName) throws java.lang.ClassNotFoundException {
        java.lang.String fqName = internalName.replace('/', '.');
        try {
            java.lang.Class.forName(fqName);
        } catch (java.lang.ClassNotFoundException e) {
            throw ((java.lang.ClassNotFoundException) sanitizeStackTrace(new java.lang.ClassNotFoundException("Class " + fqName + " is not found. Please update the Kotlin runtime to the latest version", e)));
        }
    }

    public static void checkHasClass(java.lang.String internalName, java.lang.String requiredVersion) throws java.lang.ClassNotFoundException {
        java.lang.String fqName = internalName.replace('/', '.');
        try {
            java.lang.Class.forName(fqName);
        } catch (java.lang.ClassNotFoundException e) {
            throw ((java.lang.ClassNotFoundException) sanitizeStackTrace(new java.lang.ClassNotFoundException("Class " + fqName + " is not found: this code requires the Kotlin runtime of version at least " + requiredVersion, e)));
        }
    }

    private static <T extends java.lang.Throwable> T sanitizeStackTrace(T t) {
        return (T) sanitizeStackTrace(t, com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.class.getName());
    }

    static <T extends java.lang.Throwable> T sanitizeStackTrace(T throwable, java.lang.String classNameToDrop) {
        java.lang.StackTraceElement[] stackTrace = throwable.getStackTrace();
        int size = stackTrace.length;
        int lastIntrinsic = -1;
        for (int i = 0; i < size; i++) {
            if (classNameToDrop.equals(stackTrace[i].getClassName())) {
                lastIntrinsic = i;
            }
        }
        int i2 = lastIntrinsic + 1;
        java.lang.StackTraceElement[] newStackTrace = (java.lang.StackTraceElement[]) java.util.Arrays.copyOfRange(stackTrace, i2, size);
        throwable.setStackTrace(newStackTrace);
        return throwable;
    }

    public static class Kotlin {
        private Kotlin() {
        }
    }
}
