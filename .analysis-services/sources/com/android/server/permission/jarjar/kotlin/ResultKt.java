package com.android.server.permission.jarjar.kotlin;

/* JADX INFO: compiled from: Result.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0001\u001a+\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0005\"\u0004\b\u0000\u0010\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00060\bH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\t\u001a\u0084\u0001\u0010\n\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u0006\"\u0004\b\u0001\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\f\u001a\u001d\u0012\u0013\u0012\u0011H\u000b¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u0002H\u00060\r2!\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0002\u0012\u0004\u0012\u0002H\u00060\rH\u0087\bø\u0001\u0000\u0082\u0002\u0014\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000\n\b\b\u0001\u0012\u0002\u0010\u0002 \u0000¢\u0006\u0002\u0010\u0012\u001a0\u0010\u0013\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u0006\"\b\b\u0001\u0010\u000b*\u0002H\u0006*\b\u0012\u0004\u0012\u0002H\u000b0\u00052\u0006\u0010\u0014\u001a\u0002H\u0006H\u0087\b¢\u0006\u0002\u0010\u0015\u001a[\u0010\u0016\u001a\u0002H\u0006\"\u0004\b\u0000\u0010\u0006\"\b\b\u0001\u0010\u000b*\u0002H\u0006*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\u0011\u001a\u001d\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0002\u0012\u0004\u0012\u0002H\u00060\rH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0002\u0010\u0017\u001a\u001e\u0010\u0018\u001a\u0002H\u000b\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u0005H\u0087\b¢\u0006\u0002\u0010\u0019\u001a]\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0005\"\u0004\b\u0000\u0010\u0006\"\u0004\b\u0001\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\u001b\u001a\u001d\u0012\u0013\u0012\u0011H\u000b¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u0002H\u00060\rH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0002\u0010\u0017\u001aP\u0010\u001c\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0005\"\u0004\b\u0000\u0010\u0006\"\u0004\b\u0001\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\u001b\u001a\u001d\u0012\u0013\u0012\u0011H\u000b¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u0002H\u00060\rH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0017\u001aW\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0005\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\u001d\u001a\u001d\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0002\u0012\u0004\u0012\u00020\u001e0\rH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0002\u0010\u0017\u001aW\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0005\"\u0004\b\u0000\u0010\u000b*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\u001d\u001a\u001d\u0012\u0013\u0012\u0011H\u000b¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0010\u0012\u0004\u0012\u00020\u001e0\rH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0002\u0010\u0017\u001aa\u0010\u001f\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0005\"\u0004\b\u0000\u0010\u0006\"\b\b\u0001\u0010\u000b*\u0002H\u0006*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\u001b\u001a\u001d\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0002\u0012\u0004\u0012\u0002H\u00060\rH\u0087\bø\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0000¢\u0006\u0002\u0010\u0017\u001aT\u0010 \u001a\b\u0012\u0004\u0012\u0002H\u00060\u0005\"\u0004\b\u0000\u0010\u0006\"\b\b\u0001\u0010\u000b*\u0002H\u0006*\b\u0012\u0004\u0012\u0002H\u000b0\u00052!\u0010\u001b\u001a\u001d\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u000e\u0012\b\b\u000f\u0012\u0004\b\b(\u0002\u0012\u0004\u0012\u0002H\u00060\rH\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0017\u001a@\u0010\u0004\u001a\b\u0012\u0004\u0012\u0002H\u00060\u0005\"\u0004\b\u0000\u0010\u000b\"\u0004\b\u0001\u0010\u0006*\u0002H\u000b2\u0017\u0010\u0007\u001a\u0013\u0012\u0004\u0012\u0002H\u000b\u0012\u0004\u0012\u0002H\u00060\r¢\u0006\u0002\b!H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0017\u001a\u0015\u0010\"\u001a\u00020\u001e*\u0006\u0012\u0002\b\u00030\u0005H\u0001¢\u0006\u0002\u0010#\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006$"}, d2 = {"createFailure", "", "exception", "", "runCatching", "Lkotlin/Result;", "R", "block", "Lkotlin/Function0;", "(Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "fold", "T", "onSuccess", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "value", "onFailure", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "getOrDefault", "defaultValue", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "getOrElse", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "getOrThrow", "(Ljava/lang/Object;)Ljava/lang/Object;", "map", "transform", "mapCatching", "action", "", "recover", "recoverCatching", "Lkotlin/ExtensionFunctionType;", "throwOnFailure", "(Ljava/lang/Object;)V", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ResultKt {
    public static final java.lang.Object createFailure(java.lang.Throwable exception) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        return new com.android.server.permission.jarjar.kotlin.Result.Failure(exception);
    }

    public static final void throwOnFailure(java.lang.Object $this$throwOnFailure) throws java.lang.Throwable {
        if ($this$throwOnFailure instanceof com.android.server.permission.jarjar.kotlin.Result.Failure) {
            throw ((com.android.server.permission.jarjar.kotlin.Result.Failure) $this$throwOnFailure).exception;
        }
    }

    private static final <R> java.lang.Object runCatching(com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<? extends R> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "block");
        try {
            com.android.server.permission.jarjar.kotlin.Result.Companion companion = com.android.server.permission.jarjar.kotlin.Result.Companion;
            return com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(function0.invoke());
        } catch (java.lang.Throwable e) {
            com.android.server.permission.jarjar.kotlin.Result.Companion companion2 = com.android.server.permission.jarjar.kotlin.Result.Companion;
            return com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(createFailure(e));
        }
    }

    private static final <T, R> java.lang.Object runCatching(T t, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "block");
        try {
            com.android.server.permission.jarjar.kotlin.Result.Companion companion = com.android.server.permission.jarjar.kotlin.Result.Companion;
            return com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(function1.invoke(t));
        } catch (java.lang.Throwable e) {
            com.android.server.permission.jarjar.kotlin.Result.Companion companion2 = com.android.server.permission.jarjar.kotlin.Result.Companion;
            return com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(createFailure(e));
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <T> T getOrThrow(java.lang.Object obj) throws java.lang.Throwable {
        throwOnFailure(obj);
        return obj;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <R, T extends R> R getOrElse(java.lang.Object obj, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.lang.Throwable, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "onFailure");
        java.lang.Throwable exception = com.android.server.permission.jarjar.kotlin.Result.m6092exceptionOrNullimpl(obj);
        return exception == null ? obj : function1.invoke(exception);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static final <R, T extends R> R getOrDefault(java.lang.Object obj, R r) {
        return com.android.server.permission.jarjar.kotlin.Result.m6095isFailureimpl(obj) ? r : obj;
    }

    private static final <R, T> R fold(java.lang.Object $this$fold, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends R> function1, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.lang.Throwable, ? extends R> function12) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "onSuccess");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function12, "onFailure");
        java.lang.Throwable exception = com.android.server.permission.jarjar.kotlin.Result.m6092exceptionOrNullimpl($this$fold);
        return exception == null ? function1.invoke($this$fold) : function12.invoke(exception);
    }

    private static final <R, T> java.lang.Object map(java.lang.Object $this$map, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "transform");
        if (!com.android.server.permission.jarjar.kotlin.Result.m6096isSuccessimpl($this$map)) {
            return com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl($this$map);
        }
        com.android.server.permission.jarjar.kotlin.Result.Companion companion = com.android.server.permission.jarjar.kotlin.Result.Companion;
        return com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(function1.invoke($this$map));
    }

    private static final <R, T> java.lang.Object mapCatching(java.lang.Object $this$mapCatching, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "transform");
        if (com.android.server.permission.jarjar.kotlin.Result.m6096isSuccessimpl($this$mapCatching)) {
            try {
                com.android.server.permission.jarjar.kotlin.Result.Companion companion = com.android.server.permission.jarjar.kotlin.Result.Companion;
                java.lang.Object $this$mapCatching_u24lambda_u243 = com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(function1.invoke($this$mapCatching));
                return $this$mapCatching_u24lambda_u243;
            } catch (java.lang.Throwable th) {
                com.android.server.permission.jarjar.kotlin.Result.Companion companion2 = com.android.server.permission.jarjar.kotlin.Result.Companion;
                return com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(createFailure(th));
            }
        }
        return com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl($this$mapCatching);
    }

    private static final <R, T extends R> java.lang.Object recover(java.lang.Object $this$recover, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.lang.Throwable, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "transform");
        java.lang.Throwable exception = com.android.server.permission.jarjar.kotlin.Result.m6092exceptionOrNullimpl($this$recover);
        if (exception == null) {
            return $this$recover;
        }
        com.android.server.permission.jarjar.kotlin.Result.Companion companion = com.android.server.permission.jarjar.kotlin.Result.Companion;
        return com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(function1.invoke(exception));
    }

    private static final <R, T extends R> java.lang.Object recoverCatching(java.lang.Object $this$recoverCatching, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.lang.Throwable, ? extends R> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "transform");
        java.lang.Throwable exception = com.android.server.permission.jarjar.kotlin.Result.m6092exceptionOrNullimpl($this$recoverCatching);
        if (exception == null) {
            return $this$recoverCatching;
        }
        try {
            com.android.server.permission.jarjar.kotlin.Result.Companion companion = com.android.server.permission.jarjar.kotlin.Result.Companion;
            java.lang.Object $this$recoverCatching_u24lambda_u245 = com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(function1.invoke(exception));
            return $this$recoverCatching_u24lambda_u245;
        } catch (java.lang.Throwable th) {
            com.android.server.permission.jarjar.kotlin.Result.Companion companion2 = com.android.server.permission.jarjar.kotlin.Result.Companion;
            return com.android.server.permission.jarjar.kotlin.Result.m6089constructorimpl(createFailure(th));
        }
    }

    private static final <T> java.lang.Object onFailure(java.lang.Object $this$onFailure, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super java.lang.Throwable, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "action");
        java.lang.Throwable it = com.android.server.permission.jarjar.kotlin.Result.m6092exceptionOrNullimpl($this$onFailure);
        if (it != null) {
            function1.invoke(it);
        }
        return $this$onFailure;
    }

    private static final <T> java.lang.Object onSuccess(java.lang.Object $this$onSuccess, com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<? super T, com.android.server.permission.jarjar.kotlin.Unit> function1) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "action");
        if (com.android.server.permission.jarjar.kotlin.Result.m6096isSuccessimpl($this$onSuccess)) {
            function1.invoke($this$onSuccess);
        }
        return $this$onSuccess;
    }
}
