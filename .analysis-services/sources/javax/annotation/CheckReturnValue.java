package javax.annotation;

/* JADX INFO: loaded from: classes3.dex */
@java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.PACKAGE})
@java.lang.annotation.Documented
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface CheckReturnValue {
    javax.annotation.meta.When when() default javax.annotation.meta.When.ALWAYS;
}
