package com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal;

/* JADX INFO: compiled from: RunSuspend.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\u001a+\u0010\u0000\u001a\u00020\u00012\u001c\u0010\u0002\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00010\u0004\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0003H\u0001¢\u0006\u0002\u0010\u0006¨\u0006\u0007"}, d2 = {"runSuspend", "", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "", "(Lkotlin/jvm/functions/Function1;)V", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class RunSuspendKt {
    public static final void runSuspend(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit>, ? extends java.lang.Object> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "block");
        com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.RunSuspend run = new com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.RunSuspend();
        com.android.server.permission.jarjar.kotlin.coroutines.ContinuationKt.startCoroutine(function1, run);
        run.await();
    }
}
