package javax.annotation;

/* JADX INFO: loaded from: classes3.dex */
@javax.annotation.meta.TypeQualifier(applicableTo = java.lang.String.class)
@java.lang.annotation.Documented
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface MatchesPattern {
    int flags() default 0;

    @javax.annotation.RegEx
    java.lang.String value();

    public static class Checker implements javax.annotation.meta.TypeQualifierValidator<javax.annotation.MatchesPattern> {
        @Override // javax.annotation.meta.TypeQualifierValidator
        public javax.annotation.meta.When forConstantValue(javax.annotation.MatchesPattern annotation, java.lang.Object value) {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(annotation.value(), annotation.flags());
            if (p.matcher((java.lang.String) value).matches()) {
                return javax.annotation.meta.When.ALWAYS;
            }
            return javax.annotation.meta.When.NEVER;
        }
    }
}
