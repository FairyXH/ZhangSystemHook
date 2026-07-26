package com.android.server.permission.jarjar.kotlin.coroutines;

/* JADX INFO: compiled from: Continuation.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000>\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0003\n\u0002\b\u0004\u001a<\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007\"\u0004\b\u0000\u0010\b2\u0006\u0010\t\u001a\u00020\u00012\u001a\b\u0004\u0010\n\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\f\u0012\u0004\u0012\u00020\r0\u000bH\u0087\bø\u0001\u0000\u001a=\u0010\u000e\u001a\u0002H\b\"\u0004\b\u0000\u0010\b2\u001a\b\u0004\u0010\u000f\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u0007\u0012\u0004\u0012\u00020\r0\u000bH\u0087H\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\u0010\u001aA\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\u0007\"\u0004\b\u0000\u0010\b*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u000b2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007H\u0007¢\u0006\u0002\u0010\u0014\u001aZ\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\r0\u0007\"\u0004\b\u0000\u0010\u0015\"\u0004\b\u0001\u0010\b*#\b\u0001\u0012\u0004\u0012\u0002H\u0015\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0016¢\u0006\u0002\b\u00172\u0006\u0010\u0018\u001a\u0002H\u00152\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007H\u0007¢\u0006\u0002\u0010\u0019\u001a&\u0010\u001a\u001a\u00020\r\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u00072\u0006\u0010\u001b\u001a\u0002H\bH\u0087\b¢\u0006\u0002\u0010\u001c\u001a!\u0010\u001d\u001a\u00020\r\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u00072\u0006\u0010\u001e\u001a\u00020\u001fH\u0087\b\u001a;\u0010 \u001a\u00020\r\"\u0004\b\u0000\u0010\b*\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u000b2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007H\u0007¢\u0006\u0002\u0010!\u001aT\u0010 \u001a\u00020\r\"\u0004\b\u0000\u0010\u0015\"\u0004\b\u0001\u0010\b*#\b\u0001\u0012\u0004\u0012\u0002H\u0015\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\b0\u0007\u0012\u0006\u0012\u0004\u0018\u00010\u00120\u0016¢\u0006\u0002\b\u00172\u0006\u0010\u0018\u001a\u0002H\u00152\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\b0\u0007H\u0007¢\u0006\u0002\u0010\"\"\u001b\u0010\u0000\u001a\u00020\u00018Æ\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u0002\u0010\u0003\u001a\u0004\b\u0004\u0010\u0005\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006#"}, d2 = {"coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "getCoroutineContext$annotations", "()V", "getCoroutineContext", "()Lkotlin/coroutines/CoroutineContext;", "Continuation", "Lkotlin/coroutines/Continuation;", "T", "context", "resumeWith", "Lkotlin/Function1;", "Lkotlin/Result;", "", "suspendCoroutine", "block", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "createCoroutine", "", "completion", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "R", "Lkotlin/Function2;", "Lkotlin/ExtensionFunctionType;", "receiver", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Lkotlin/coroutines/Continuation;", "resume", "value", "(Lkotlin/coroutines/Continuation;Ljava/lang/Object;)V", "resumeWithException", "exception", "", "startCoroutine", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)V", "(Lkotlin/jvm/functions/Function2;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)V", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ContinuationKt {
    public static /* synthetic */ void getCoroutineContext$annotations() {
    }

    private static final <T> void resume(com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super T> continuation, T t) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "<this>");
        com.android.server.permission.jarjar.kotlin.Result.Companion companion = com.android.server.permission.jarjar.kotlin.Result.Companion;
        continuation.resumeWith(com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(t));
    }

    private static final <T> void resumeWithException(com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super T> continuation, java.lang.Throwable exception) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        com.android.server.permission.jarjar.kotlin.Result.Companion companion = com.android.server.permission.jarjar.kotlin.Result.Companion;
        continuation.resumeWith(com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(com.android.server.permission.jarjar.kotlin.ResultKt.createFailure(exception)));
    }

    private static final <T> com.android.server.permission.jarjar.kotlin.coroutines.Continuation<T> Continuation(final com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext context, final com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.Result<? extends T>, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "resumeWith");
        return new com.android.server.permission.jarjar.kotlin.coroutines.Continuation<T>() { // from class: com.android.server.permission.jarjar.kotlin.coroutines.ContinuationKt.Continuation.1
            @Override // com.android.server.permission.jarjar.kotlin.coroutines.Continuation
            public com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext getContext() {
                return context;
            }

            @Override // com.android.server.permission.jarjar.kotlin.coroutines.Continuation
            public void resumeWith(java.lang.Object result) {
                function1.invoke(com.android.server.permission.jarjar.kotlin.Result.m6088boximpl(result));
            }
        };
    }

    public static final <T> com.android.server.permission.jarjar.kotlin.coroutines.Continuation<com.android.server.permission.jarjar.kotlin.Unit> createCoroutine(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function1, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super T> continuation) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "completion");
        return new com.android.server.permission.jarjar.kotlin.coroutines.SafeContinuation(com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.createCoroutineUnintercepted(function1, continuation)), com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED());
    }

    public static final <R, T> com.android.server.permission.jarjar.kotlin.coroutines.Continuation<com.android.server.permission.jarjar.kotlin.Unit> createCoroutine(com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super R, ? super com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, R r, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super T> continuation) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "completion");
        return new com.android.server.permission.jarjar.kotlin.coroutines.SafeContinuation(com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.createCoroutineUnintercepted(function2, r, continuation)), com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED());
    }

    public static final <T> void startCoroutine(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function1, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super T> continuation) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "completion");
        com.android.server.permission.jarjar.kotlin.coroutines.Continuation continuationIntercepted = com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.createCoroutineUnintercepted(function1, continuation));
        com.android.server.permission.jarjar.kotlin.Result.Companion companion = com.android.server.permission.jarjar.kotlin.Result.Companion;
        continuationIntercepted.resumeWith(com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(com.android.server.permission.jarjar.kotlin.Unit.INSTANCE));
    }

    public static final <R, T> void startCoroutine(com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super R, ? super com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, R r, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super T> continuation) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "completion");
        com.android.server.permission.jarjar.kotlin.coroutines.Continuation continuationIntercepted = com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.createCoroutineUnintercepted(function2, r, continuation));
        com.android.server.permission.jarjar.kotlin.Result.Companion companion = com.android.server.permission.jarjar.kotlin.Result.Companion;
        continuationIntercepted.resumeWith(com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(com.android.server.permission.jarjar.kotlin.Unit.INSTANCE));
    }

    private static final <T> java.lang.Object suspendCoroutine(com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super T>, com.android.server.permission.jarjar.kotlin.Unit> function1, com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super T> continuation) throws java.lang.Throwable {
        com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.mark(0);
        com.android.server.permission.jarjar.kotlin.coroutines.SafeContinuation safe = new com.android.server.permission.jarjar.kotlin.coroutines.SafeContinuation(com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.intercepted(continuation));
        function1.invoke(safe);
        java.lang.Object orThrow = safe.getOrThrow();
        if (orThrow == com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            com.android.server.permission.jarjar.kotlin.coroutines.jvm.internal.DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        com.android.server.permission.jarjar.kotlin.jvm.internal.InlineMarker.mark(1);
        return orThrow;
    }

    private static final com.android.server.permission.jarjar.kotlin.coroutines.CoroutineContext getCoroutineContext() {
        throw new com.android.server.permission.jarjar.kotlin.NotImplementedError("Implemented as intrinsic");
    }
}
