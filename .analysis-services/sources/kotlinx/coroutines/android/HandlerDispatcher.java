package kotlinx.coroutines.android;

/* JADX INFO: compiled from: HandlerDispatcher.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u00012\u00020\u0002B\u0007\b\u0004¢\u0006\u0002\u0010\u0003R\u0012\u0010\u0004\u001a\u00020\u0000X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\u0082\u0001\u0001\u0007¨\u0006\b"}, d2 = {"Lkotlinx/coroutines/android/HandlerDispatcher;", "Lkotlinx/coroutines/MainCoroutineDispatcher;", "Lkotlinx/coroutines/Delay;", "()V", "immediate", "getImmediate", "()Lkotlinx/coroutines/android/HandlerDispatcher;", "Lkotlinx/coroutines/android/HandlerContext;", "external__kotlinx.coroutines__android_common__kotlinx_coroutines_android"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class HandlerDispatcher extends kotlinx.coroutines.MainCoroutineDispatcher implements kotlinx.coroutines.Delay {
    public /* synthetic */ HandlerDispatcher(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    @Override // kotlinx.coroutines.MainCoroutineDispatcher
    public abstract kotlinx.coroutines.android.HandlerDispatcher getImmediate();

    private HandlerDispatcher() {
    }

    @Override // kotlinx.coroutines.Delay
    @kotlin.Deprecated(level = kotlin.DeprecationLevel.ERROR, message = "Deprecated without replacement as an internal method never intended for public use")
    public java.lang.Object delay(long time, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        return kotlinx.coroutines.Delay.DefaultImpls.delay(this, time, continuation);
    }

    public kotlinx.coroutines.DisposableHandle invokeOnTimeout(long timeMillis, java.lang.Runnable block, kotlin.coroutines.CoroutineContext context) {
        return kotlinx.coroutines.Delay.DefaultImpls.invokeOnTimeout(this, timeMillis, block, context);
    }
}
