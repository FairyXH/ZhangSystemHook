package com.android.server.permission.jarjar.kotlin.annotation;

/* JADX INFO: compiled from: Annotations.kt */
/* JADX INFO: loaded from: classes2.dex */
@java.lang.annotation.Target({java.lang.annotation.ElementType.ANNOTATION_TYPE})
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\u0002\u0018\u00002\u00020\u0001B\n\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003R\u000f\u0010\u0002\u001a\u00020\u0003¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0004¨\u0006\u0005"}, d2 = {"Lkotlin/annotation/Retention;", "", "value", "Lkotlin/annotation/AnnotationRetention;", "()Lkotlin/annotation/AnnotationRetention;", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@com.android.server.permission.jarjar.kotlin.annotation.Target(allowedTargets = {com.android.server.permission.jarjar.kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS})
public @interface Retention {
    com.android.server.permission.jarjar.kotlin.annotation.AnnotationRetention value() default com.android.server.permission.jarjar.kotlin.annotation.AnnotationRetention.RUNTIME;
}
