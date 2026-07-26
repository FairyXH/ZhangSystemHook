package com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal;

/* JADX INFO: compiled from: DebugProbes.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\u001a\"\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001H\u0001\u001a\u0014\u0010\u0004\u001a\u00020\u00052\n\u0010\u0006\u001a\u0006\u0012\u0002\b\u00030\u0001H\u0001\u001a\u0014\u0010\u0007\u001a\u00020\u00052\n\u0010\u0006\u001a\u0006\u0012\u0002\b\u00030\u0001H\u0001¨\u0006\b"}, d2 = {"probeCoroutineCreated", "Lkotlin/coroutines/Continuation;", "T", "completion", "probeCoroutineResumed", "", "frame", "probeCoroutineSuspended", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class DebugProbesKt {
    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> com.android.server.permission.jarjar.kotlin.coroutines.Continuation<T> probeCoroutineCreated(com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super T> continuation) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "completion");
        return continuation;
    }

    public static final void probeCoroutineResumed(com.android.server.permission.jarjar.kotlin.coroutines.Continuation<?> continuation) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "frame");
    }

    public static final void probeCoroutineSuspended(com.android.server.permission.jarjar.kotlin.coroutines.Continuation<?> continuation) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "frame");
    }
}
