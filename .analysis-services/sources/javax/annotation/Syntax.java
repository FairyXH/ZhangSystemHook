package javax.annotation;

/* JADX INFO: loaded from: classes3.dex */
@javax.annotation.meta.TypeQualifier(applicableTo = java.lang.String.class)
@java.lang.annotation.Documented
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Syntax {
    java.lang.String value();

    javax.annotation.meta.When when() default javax.annotation.meta.When.ALWAYS;
}
