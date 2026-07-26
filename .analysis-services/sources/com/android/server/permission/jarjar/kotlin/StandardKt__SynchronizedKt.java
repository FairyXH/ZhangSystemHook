package com.android.server.permission.jarjar.kotlin;

/* JADX INFO: compiled from: Synchronized.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a:\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0005H\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0001¢\u0006\u0002\u0010\u0006\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0007"}, d2 = {"synchronized", "R", "lock", "", "block", "Lkotlin/Function0;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/StandardKt")
class StandardKt__SynchronizedKt extends com.android.server.permission.jarjar.kotlin.StandardKt__StandardKt {
    /* JADX INFO: renamed from: synchronized, reason: not valid java name */
    private static final <R> R m6099synchronized(java.lang.Object lock, com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends R> function0) {
        R rInvoke;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(lock, "lock");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "block");
        synchronized (lock) {
            try {
                rInvoke = function0.invoke();
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
            } catch (java.lang.Throwable th) {
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyStart(1);
                com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
                throw th;
            }
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.finallyEnd(1);
        return rInvoke;
    }
}
