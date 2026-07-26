package org.intellij.lang.annotations;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.LOCAL_VARIABLE, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
public @interface MagicConstant {
    long[] flags() default {};

    java.lang.Class flagsFromClass() default void.class;

    long[] intValues() default {};

    java.lang.String[] stringValues() default {};

    java.lang.Class valuesFromClass() default void.class;
}
