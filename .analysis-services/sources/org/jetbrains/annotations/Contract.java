package org.jetbrains.annotations;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD})
@java.lang.annotation.Documented
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.CLASS)
public @interface Contract {
    boolean pure() default false;

    java.lang.String value() default "";
}
