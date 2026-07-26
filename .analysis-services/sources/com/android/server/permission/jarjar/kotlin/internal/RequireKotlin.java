package com.android.server.permission.jarjar.kotlin.internal;

/* JADX INFO: compiled from: Annotations.kt */
/* JADX INFO: loaded from: classes2.dex */
@java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR})
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\b\u0081\u0002\u0018\u00002\u00020\u0001B0\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0002\u0010\u0007\u001a\u00020\b\u0012\b\b\u0002\u0010\t\u001a\u00020\nR\u000f\u0010\t\u001a\u00020\n¢\u0006\u0006\u001a\u0004\b\t\u0010\u000bR\u000f\u0010\u0005\u001a\u00020\u0006¢\u0006\u0006\u001a\u0004\b\u0005\u0010\fR\u000f\u0010\u0004\u001a\u00020\u0003¢\u0006\u0006\u001a\u0004\b\u0004\u0010\rR\u000f\u0010\u0002\u001a\u00020\u0003¢\u0006\u0006\u001a\u0004\b\u0002\u0010\rR\u000f\u0010\u0007\u001a\u00020\b¢\u0006\u0006\u001a\u0004\b\u0007\u0010\u000e¨\u0006\u000f"}, d2 = {"Lkotlin/internal/RequireKotlin;", "", "version", "", "message", "level", "Lkotlin/DeprecationLevel;", "versionKind", "Lkotlin/internal/RequireKotlinVersionKind;", "errorCode", "", "()I", "()Lkotlin/DeprecationLevel;", "()Ljava/lang/String;", "()Lkotlin/internal/RequireKotlinVersionKind;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
@com.android.server.permission.jarjar.kotlin.annotation.Repeatable
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
@com.android.server.permission.jarjar.kotlin.annotation.Target(allowedTargets = {com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.CLASS, com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.FUNCTION, com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.PROPERTY, com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.CONSTRUCTOR, com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.TYPEALIAS})
@com.android.server.permission.jarjar.kotlin.annotation.Retention(com.android.server.permission.jarjar.kotlin.annotation.AnnotationRetention.SOURCE)
@java.lang.annotation.Repeatable(com.android.server.permission.jarjar.kotlin.internal.RequireKotlin.Container.class)
public @interface RequireKotlin {

    /* JADX INFO: compiled from: Annotations.kt */
    @java.lang.annotation.Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR})
    @com.android.server.permission.jarjar.kotlin.jvm.internal.RepeatableContainer
    @com.android.server.permission.jarjar.kotlin.annotation.Retention(com.android.server.permission.jarjar.kotlin.annotation.AnnotationRetention.SOURCE)
    @com.android.server.permission.jarjar.kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @com.android.server.permission.jarjar.kotlin.annotation.Target(allowedTargets = {com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.CLASS, com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.FUNCTION, com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.PROPERTY, com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.CONSTRUCTOR, com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.TYPEALIAS})
    public @interface Container {
        com.android.server.permission.jarjar.kotlin.internal.RequireKotlin[] value();
    }

    int errorCode() default -1;

    com.android.server.permission.jarjar.kotlin.DeprecationLevel level() default com.android.server.permission.jarjar.kotlin.DeprecationLevel.ERROR;

    java.lang.String message() default "";

    java.lang.String version();

    com.android.server.permission.jarjar.kotlin.internal.RequireKotlinVersionKind versionKind() default com.android.server.permission.jarjar.kotlin.internal.RequireKotlinVersionKind.LANGUAGE_VERSION;
}
