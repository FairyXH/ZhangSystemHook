package javax.annotation;

/* JADX INFO: loaded from: classes3.dex */
@javax.annotation.meta.TypeQualifier
@java.lang.annotation.Documented
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Nonnull {
    javax.annotation.meta.When when() default javax.annotation.meta.When.ALWAYS;

    public static class Checker implements javax.annotation.meta.TypeQualifierValidator<javax.annotation.Nonnull> {
        @Override // javax.annotation.meta.TypeQualifierValidator
        public javax.annotation.meta.When forConstantValue(javax.annotation.Nonnull qualifierqualifierArgument, java.lang.Object value) {
            if (value == null) {
                return javax.annotation.meta.When.NEVER;
            }
            return javax.annotation.meta.When.ALWAYS;
        }
    }
}
