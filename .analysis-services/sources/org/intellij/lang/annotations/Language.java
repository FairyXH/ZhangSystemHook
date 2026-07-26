package org.intellij.lang.annotations;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.LOCAL_VARIABLE, java.lang.annotation.ElementType.ANNOTATION_TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.CLASS)
public @interface Language {
    java.lang.String prefix() default "";

    java.lang.String suffix() default "";

    java.lang.String value();
}
