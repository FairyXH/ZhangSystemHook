package com.android.server.utils;

/* JADX INFO: loaded from: classes3.dex */
@java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface Watched {
    boolean manual() default false;
}
