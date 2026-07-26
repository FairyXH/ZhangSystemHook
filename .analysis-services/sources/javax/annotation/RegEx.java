package javax.annotation;

/* JADX INFO: loaded from: classes3.dex */
@javax.annotation.Syntax("RegEx")
@java.lang.annotation.Documented
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface RegEx {
    javax.annotation.meta.When when() default javax.annotation.meta.When.ALWAYS;

    public static class Checker implements javax.annotation.meta.TypeQualifierValidator<javax.annotation.RegEx> {
        @Override // javax.annotation.meta.TypeQualifierValidator
        public javax.annotation.meta.When forConstantValue(javax.annotation.RegEx annotation, java.lang.Object value) {
            if (!(value instanceof java.lang.String)) {
                return javax.annotation.meta.When.NEVER;
            }
            try {
                java.util.regex.Pattern.compile((java.lang.String) value);
                return javax.annotation.meta.When.ALWAYS;
            } catch (java.util.regex.PatternSyntaxException e) {
                return javax.annotation.meta.When.NEVER;
            }
        }
    }
}
