package javax.annotation;

/* JADX INFO: loaded from: classes3.dex */
@javax.annotation.meta.TypeQualifier
@java.lang.annotation.Documented
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Untainted {
    javax.annotation.meta.When when() default javax.annotation.meta.When.ALWAYS;
}
