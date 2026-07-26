package javax.annotation;

/* JADX INFO: loaded from: classes3.dex */
@javax.annotation.meta.TypeQualifier(applicableTo = java.lang.Number.class)
@java.lang.annotation.Documented
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Nonnegative {
    javax.annotation.meta.When when() default javax.annotation.meta.When.ALWAYS;

    public static class Checker implements javax.annotation.meta.TypeQualifierValidator<javax.annotation.Nonnegative> {
        @Override // javax.annotation.meta.TypeQualifierValidator
        public javax.annotation.meta.When forConstantValue(javax.annotation.Nonnegative annotation, java.lang.Object v) {
            boolean isNegative;
            if (!(v instanceof java.lang.Number)) {
                return javax.annotation.meta.When.NEVER;
            }
            java.lang.Number value = (java.lang.Number) v;
            if (value instanceof java.lang.Long) {
                isNegative = value.longValue() < 0;
            } else {
                boolean isNegative2 = value instanceof java.lang.Double;
                if (isNegative2) {
                    isNegative = value.doubleValue() < 0.0d;
                } else {
                    boolean isNegative3 = value instanceof java.lang.Float;
                    if (isNegative3) {
                        isNegative = value.floatValue() < 0.0f;
                    } else {
                        isNegative = value.intValue() < 0;
                    }
                }
            }
            if (isNegative) {
                return javax.annotation.meta.When.NEVER;
            }
            return javax.annotation.meta.When.ALWAYS;
        }
    }
}
