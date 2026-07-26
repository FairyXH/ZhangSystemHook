package org.intellij.lang.annotations;

/* JADX INFO: loaded from: classes4.dex */
@java.lang.annotation.Target({java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.METHOD})
@java.lang.annotation.Documented
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.CLASS)
public @interface Flow {
    public static final java.lang.String DEFAULT_SOURCE = "The method argument (if parameter was annotated) or this container (if instance method was annotated)";
    public static final java.lang.String DEFAULT_TARGET = "This container (if the parameter was annotated) or the return value (if instance method was annotated)";
    public static final java.lang.String RETURN_METHOD_TARGET = "The return value of this method";
    public static final java.lang.String THIS_SOURCE = "this";
    public static final java.lang.String THIS_TARGET = "this";

    java.lang.String source() default "The method argument (if parameter was annotated) or this container (if instance method was annotated)";

    boolean sourceIsContainer() default false;

    java.lang.String target() default "This container (if the parameter was annotated) or the return value (if instance method was annotated)";

    boolean targetIsContainer() default false;
}
