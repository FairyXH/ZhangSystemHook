package com.android.server.permission.jarjar.kotlin;

/* JADX INFO: compiled from: Annotations.kt */
/* JADX INFO: loaded from: classes2.dex */
@java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.ANNOTATION_TYPE})
@com.android.server.permission.jarjar.kotlin.annotation.MustBeDocumented
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0087\u0002\u0018\u00002\u00020\u0001B\u001c\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007R\u000f\u0010\u0006\u001a\u00020\u0007¢\u0006\u0006\u001a\u0004\b\u0006\u0010\bR\u000f\u0010\u0002\u001a\u00020\u0003¢\u0006\u0006\u001a\u0004\b\u0002\u0010\tR\u000f\u0010\u0004\u001a\u00020\u0005¢\u0006\u0006\u001a\u0004\b\u0004\u0010\n¨\u0006\u000b"}, d2 = {"Lkotlin/Deprecated;", "", "message", "", "replaceWith", "Lkotlin/ReplaceWith;", "level", "Lkotlin/DeprecationLevel;", "()Lkotlin/DeprecationLevel;", "()Ljava/lang/String;", "()Lkotlin/ReplaceWith;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
@java.lang.annotation.Documented
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@com.android.server.permission.jarjar.kotlin.annotation.Target(allowedTargets = {com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.CLASS, com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.FUNCTION, com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.PROPERTY, com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS, com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.CONSTRUCTOR, com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.PROPERTY_SETTER, com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.PROPERTY_GETTER, com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.TYPEALIAS})
public @interface Deprecated {
    com.android.server.permission.jarjar.kotlin.DeprecationLevel level() default com.android.server.permission.jarjar.kotlin.DeprecationLevel.WARNING;

    java.lang.String message();

    com.android.server.permission.jarjar.kotlin.ReplaceWith replaceWith() default @com.android.server.permission.jarjar.kotlin.ReplaceWith(expression = "", imports = {});
}
