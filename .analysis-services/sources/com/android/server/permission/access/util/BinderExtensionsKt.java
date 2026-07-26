package com.android.server.permission.access.util;

/* JADX INFO: compiled from: BinderExtensions.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0016\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a,\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u00020\u00030\u00022\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0005H\u0086\b¢\u0006\u0002\u0010\u0006¨\u0006\u0007"}, d2 = {"withClearedCallingIdentity", "R", "Lkotlin/reflect/KClass;", "Landroid/os/Binder;", "action", "Lkotlin/Function0;", "(Lkotlin/reflect/KClass;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "frameworks__base__services__permission__android_common__services.permission-pre-jarjar"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class BinderExtensionsKt {
    public static final <R> R withClearedCallingIdentity(com.android.server.permission.jarjar.kotlin.reflect.KClass<android.os.Binder> kClass, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends R> function0) {
        long token = android.os.Binder.clearCallingIdentity();
        try {
            return function0.invoke();
        } finally {
            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
            android.os.Binder.restoreCallingIdentity(token);
            com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
        }
    }
}
