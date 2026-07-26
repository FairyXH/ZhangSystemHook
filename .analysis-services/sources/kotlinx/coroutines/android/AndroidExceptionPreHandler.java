package kotlinx.coroutines.android;

/* JADX INFO: compiled from: AndroidExceptionPreHandler.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\u0018\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\n\u0010\f\u001a\u0004\u0018\u00010\rH\u0002R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/android/AndroidExceptionPreHandler;", "Lkotlin/coroutines/AbstractCoroutineContextElement;", "Lkotlinx/coroutines/CoroutineExceptionHandler;", "()V", "_preHandler", "", "handleException", "", "context", "Lkotlin/coroutines/CoroutineContext;", "exception", "", "preHandler", "Ljava/lang/reflect/Method;", "external__kotlinx.coroutines__android_common__kotlinx_coroutines_android"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AndroidExceptionPreHandler extends kotlin.coroutines.AbstractCoroutineContextElement implements kotlinx.coroutines.CoroutineExceptionHandler {
    private volatile java.lang.Object _preHandler;

    public AndroidExceptionPreHandler() {
        super(kotlinx.coroutines.CoroutineExceptionHandler.INSTANCE);
        this._preHandler = this;
    }

    private final java.lang.reflect.Method preHandler() {
        java.lang.Object current = this._preHandler;
        if (current != this) {
            return (java.lang.reflect.Method) current;
        }
        java.lang.reflect.Method declared = null;
        try {
            boolean z = false;
            java.lang.reflect.Method it = java.lang.Thread.class.getDeclaredMethod("getUncaughtExceptionPreHandler", new java.lang.Class[0]);
            if (java.lang.reflect.Modifier.isPublic(it.getModifiers())) {
                if (java.lang.reflect.Modifier.isStatic(it.getModifiers())) {
                    z = true;
                }
            }
            if (z) {
                declared = it;
            }
        } catch (java.lang.Throwable th) {
        }
        this._preHandler = declared;
        return declared;
    }

    @Override // kotlinx.coroutines.CoroutineExceptionHandler
    public void handleException(kotlin.coroutines.CoroutineContext context, java.lang.Throwable exception) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
    }
}
