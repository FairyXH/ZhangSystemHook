package kotlin;

/* JADX INFO: compiled from: Result.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0001\u001a+\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0005\"\u0004\b\u0000\u0010\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00060\bH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\t\u001a\u0084\u0001\u0010\n\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u0006\"\u0004\b\u0001\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\f\u001a\u001d\u0012\u0013\u0012\u0011H\u000b¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u0002H\u00060\r2!\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0002\u0012\u0004\u0012\u0002H\u00060\rH\u0087\bø\u0001\u0000\u0082\u0002\u0014\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0000¢\u0006\u0002\u0010\u0012\u001a0\u0010\u0013\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u0006\"\b\b\u0001\u0010\u000b*\u0002H\u0006*\b\u0012\u0004\u0012\u0002H\u000b0\u00052\u0006\u0010\u0014\u001a\u0002H\u0006H\u0087\b¢\u0006\u0002\u0010\u0015\u001a[\u0010\u0016\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u0006\"\b\b\u0001\u0010\u000b*\u0002H\u0006*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0002\u0012\u0004\u0012\u0002H\u00060\rH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0002\u0010\u0017\u001a\u001e\u0010\u0018\u001a\u0002H\u000b\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u0005H\u0087\b¢\u0006\u0002\u0010\u0019\u001a]\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0005\"\u0004\b\u0000\u0010\u0006\"\u0004\b\u0001\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\u001b\u001a\u001d\u0012\u0013\u0012\u0011H\u000b¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u0002H\u00060\rH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0002\u0010\u0017\u001aP\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0005\"\u0004\b\u0000\u0010\u0006\"\u0004\b\u0001\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\u001b\u001a\u001d\u0012\u0013\u0012\u0011H\u000b¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u0002H\u00060\rH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0017\u001aW\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0005\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\u001d\u001a\u001d\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0002\u0012\u0004\u0012\u00020\u001e0\rH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0002\u0010\u0017\u001aW\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0005\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\u001d\u001a\u001d\u0012\u0013\u0012\u0011H\u000b¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u001e0\rH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0002\u0010\u0017\u001aa\u0010\u001f\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0005\"\u0004\b\u0000\u0010\u0006\"\b\b\u0001\u0010\u000b*\u0002H\u0006*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\u001b\u001a\u001d\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0002\u0012\u0004\u0012\u0002H\u00060\rH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0002\u0010\u0017\u001aT\u0010 \u001a\b\u0012\u0004\u0012\u0002H\u00060\u0005\"\u0004\b\u0000\u0010\u0006\"\b\b\u0001\u0010\u000b*\u0002H\u0006*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\u001b\u001a\u001d\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0002\u0012\u0004\u0012\u0002H\u00060\rH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0017\u001a@\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0005\"\u0004\b\u0000\u0010\u000b\"\u0004\b\u0001\u0010\u0006*\u0002H\u000b2\u0017\u0010\u0007\u001a\u0013\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u0002H\u00060\r¢\u0006\u0002\b!H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0017\u001a\u0015\u0010\"\u001a\u00020\u001e*\u0006\u0012\u0002\b\u00030\u0005H\u0001¢\u0006\u0002\u0010#\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006$"}, d2 = {"createFailure", "", "exception", "", "runCatching", "Lkotlin/Result;", "R", "block", "Lkotlin/Function0;", "(Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "fold", "T", "onSuccess", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "value", "onFailure", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "getOrDefault", "defaultValue", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "getOrElse", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "getOrThrow", "(Ljava/lang/Object;)Ljava/lang/Object;", "map", "transform", "mapCatching", "action", "", "recover", "recoverCatching", "Lkotlin/ExtensionFunctionType;", "throwOnFailure", "(Ljava/lang/Object;)V", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ResultKt {
    public static final java.lang.Object createFailure(java.lang.Throwable exception) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        return new kotlin.Result.Failure(exception);
    }

    public static final void throwOnFailure(java.lang.Object $this$throwOnFailure) throws java.lang.Throwable {
        if ($this$throwOnFailure instanceof kotlin.Result.Failure) {
            throw ((kotlin.Result.Failure) $this$throwOnFailure).exception;
        }
    }

    private static final <R> java.lang.Object runCatching(kotlin.jvm.functions.Function0<? extends R> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        try {
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            return kotlin.Result.m11307constructorimpl(block.invoke());
        } catch (java.lang.Throwable e) {
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            return kotlin.Result.m11307constructorimpl(createFailure(e));
        }
    }

    private static final <T, R> java.lang.Object runCatching(T t, kotlin.jvm.functions.Function1<? super T, ? extends R> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        try {
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            return kotlin.Result.m11307constructorimpl(block.invoke(t));
        } catch (java.lang.Throwable e) {
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            return kotlin.Result.m11307constructorimpl(createFailure(e));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <T> T getOrThrow(java.lang.Object obj) throws java.lang.Throwable {
        throwOnFailure(obj);
        return obj;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <R, T extends R> R getOrElse(java.lang.Object obj, kotlin.jvm.functions.Function1<? super java.lang.Throwable, ? extends R> onFailure) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onFailure, "onFailure");
        java.lang.Throwable exception = kotlin.Result.m11310exceptionOrNullimpl(obj);
        return exception == null ? obj : onFailure.invoke(exception);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <R, T extends R> R getOrDefault(java.lang.Object obj, R r) {
        return kotlin.Result.m11313isFailureimpl(obj) ? r : obj;
    }

    private static final <R, T> R fold(java.lang.Object $this$fold, kotlin.jvm.functions.Function1<? super T, ? extends R> onSuccess, kotlin.jvm.functions.Function1<? super java.lang.Throwable, ? extends R> onFailure) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onSuccess, "onSuccess");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onFailure, "onFailure");
        java.lang.Throwable exception = kotlin.Result.m11310exceptionOrNullimpl($this$fold);
        return exception == null ? onSuccess.invoke($this$fold) : onFailure.invoke(exception);
    }

    private static final <R, T> java.lang.Object map(java.lang.Object $this$map, kotlin.jvm.functions.Function1<? super T, ? extends R> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        if (!kotlin.Result.m11314isSuccessimpl($this$map)) {
            return kotlin.Result.m11307constructorimpl($this$map);
        }
        kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
        return kotlin.Result.m11307constructorimpl(transform.invoke($this$map));
    }

    private static final <R, T> java.lang.Object mapCatching(java.lang.Object $this$mapCatching, kotlin.jvm.functions.Function1<? super T, ? extends R> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        if (kotlin.Result.m11314isSuccessimpl($this$mapCatching)) {
            try {
                kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
                java.lang.Object $this$mapCatching_u24lambda_u243 = kotlin.Result.m11307constructorimpl(transform.invoke($this$mapCatching));
                return $this$mapCatching_u24lambda_u243;
            } catch (java.lang.Throwable th) {
                kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
                return kotlin.Result.m11307constructorimpl(createFailure(th));
            }
        }
        return kotlin.Result.m11307constructorimpl($this$mapCatching);
    }

    private static final <R, T extends R> java.lang.Object recover(java.lang.Object $this$recover, kotlin.jvm.functions.Function1<? super java.lang.Throwable, ? extends R> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        java.lang.Throwable exception = kotlin.Result.m11310exceptionOrNullimpl($this$recover);
        if (exception == null) {
            return $this$recover;
        }
        kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
        return kotlin.Result.m11307constructorimpl(transform.invoke(exception));
    }

    private static final <R, T extends R> java.lang.Object recoverCatching(java.lang.Object $this$recoverCatching, kotlin.jvm.functions.Function1<? super java.lang.Throwable, ? extends R> transform) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(transform, "transform");
        java.lang.Throwable exception = kotlin.Result.m11310exceptionOrNullimpl($this$recoverCatching);
        if (exception == null) {
            return $this$recoverCatching;
        }
        try {
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            java.lang.Object $this$recoverCatching_u24lambda_u245 = kotlin.Result.m11307constructorimpl(transform.invoke(exception));
            return $this$recoverCatching_u24lambda_u245;
        } catch (java.lang.Throwable th) {
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            return kotlin.Result.m11307constructorimpl(createFailure(th));
        }
    }

    private static final <T> java.lang.Object onFailure(java.lang.Object $this$onFailure, kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        java.lang.Throwable it = kotlin.Result.m11310exceptionOrNullimpl($this$onFailure);
        if (it != null) {
            action.invoke(it);
        }
        return $this$onFailure;
    }

    private static final <T> java.lang.Object onSuccess(java.lang.Object $this$onSuccess, kotlin.jvm.functions.Function1<? super T, kotlin.Unit> action) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(action, "action");
        if (kotlin.Result.m11314isSuccessimpl($this$onSuccess)) {
            action.invoke($this$onSuccess);
        }
        return $this$onSuccess;
    }
}
