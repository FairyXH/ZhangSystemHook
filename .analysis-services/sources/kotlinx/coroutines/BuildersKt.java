package kotlinx.coroutines;

/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"kotlinx/coroutines/BuildersKt__BuildersKt", "kotlinx/coroutines/BuildersKt__Builders_commonKt"}, k = 4, mv = {1, 9, 0}, xi = 48)
public final class BuildersKt {
    public static final <T> kotlinx.coroutines.Deferred<T> async(kotlinx.coroutines.CoroutineScope $this$async, kotlin.coroutines.CoroutineContext context, kotlinx.coroutines.CoroutineStart start, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.BuildersKt__Builders_commonKt.async($this$async, context, start, function2);
    }

    public static final <T> java.lang.Object invoke(kotlinx.coroutines.CoroutineDispatcher $this$invoke, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super T> continuation) {
        return kotlinx.coroutines.BuildersKt__Builders_commonKt.invoke($this$invoke, function2, continuation);
    }

    public static final kotlinx.coroutines.Job launch(kotlinx.coroutines.CoroutineScope $this$launch, kotlin.coroutines.CoroutineContext context, kotlinx.coroutines.CoroutineStart start, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super kotlin.Unit>, ? extends java.lang.Object> function2) {
        return kotlinx.coroutines.BuildersKt__Builders_commonKt.launch($this$launch, context, start, function2);
    }

    public static final <T> T runBlocking(kotlin.coroutines.CoroutineContext coroutineContext, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2) throws java.lang.InterruptedException {
        return (T) kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking(coroutineContext, function2);
    }

    public static final <T> java.lang.Object withContext(kotlin.coroutines.CoroutineContext context, kotlin.jvm.functions.Function2<? super kotlinx.coroutines.CoroutineScope, ? super kotlin.coroutines.Continuation<? super T>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super T> continuation) throws java.lang.Throwable {
        return kotlinx.coroutines.BuildersKt__Builders_commonKt.withContext(context, function2, continuation);
    }
}
