package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: OnUndeliveredElement.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000(\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u001aI\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001\"\u0004\b\u0000\u0010\u0004*\u0018\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u00020\u00030\u0001j\b\u0012\u0004\u0012\u0002H\u0004`\u00052\u0006\u0010\u0006\u001a\u0002H\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0000¢\u0006\u0002\u0010\t\u001a=\u0010\n\u001a\u00020\u0003\"\u0004\b\u0000\u0010\u0004*\u0018\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u00020\u00030\u0001j\b\u0012\u0004\u0012\u0002H\u0004`\u00052\u0006\u0010\u0006\u001a\u0002H\u00042\u0006\u0010\u0007\u001a\u00020\bH\u0000¢\u0006\u0002\u0010\u000b\u001aC\u0010\f\u001a\u0004\u0018\u00010\r\"\u0004\b\u0000\u0010\u0004*\u0018\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u00020\u00030\u0001j\b\u0012\u0004\u0012\u0002H\u0004`\u00052\u0006\u0010\u0006\u001a\u0002H\u00042\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\rH\u0000¢\u0006\u0002\u0010\u000f**\b\u0000\u0010\u0010\u001a\u0004\b\u0000\u0010\u0004\"\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u00020\u00030\u00012\u000e\u0012\u0004\u0012\u0002H\u0004\u0012\u0004\u0012\u00020\u00030\u0001¨\u0006\u0011"}, d2 = {"bindCancellationFun", "Lkotlin/Function1;", "", "", "E", "Lkotlinx/coroutines/internal/OnUndeliveredElement;", "element", "context", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlin/jvm/functions/Function1;Ljava/lang/Object;Lkotlin/coroutines/CoroutineContext;)Lkotlin/jvm/functions/Function1;", "callUndeliveredElement", "(Lkotlin/jvm/functions/Function1;Ljava/lang/Object;Lkotlin/coroutines/CoroutineContext;)V", "callUndeliveredElementCatchingException", "Lkotlinx/coroutines/internal/UndeliveredElementException;", "undeliveredElementException", "(Lkotlin/jvm/functions/Function1;Ljava/lang/Object;Lkotlinx/coroutines/internal/UndeliveredElementException;)Lkotlinx/coroutines/internal/UndeliveredElementException;", "OnUndeliveredElement", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class OnUndeliveredElementKt {
    public static /* synthetic */ kotlinx.coroutines.internal.UndeliveredElementException callUndeliveredElementCatchingException$default(kotlin.jvm.functions.Function1 function1, java.lang.Object obj, kotlinx.coroutines.internal.UndeliveredElementException undeliveredElementException, int i, java.lang.Object obj2) {
        if ((i & 2) != 0) {
            undeliveredElementException = null;
        }
        return callUndeliveredElementCatchingException(function1, obj, undeliveredElementException);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final <E> kotlinx.coroutines.internal.UndeliveredElementException callUndeliveredElementCatchingException(kotlin.jvm.functions.Function1<? super E, kotlin.Unit> function1, E e, kotlinx.coroutines.internal.UndeliveredElementException undeliveredElementException) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "<this>");
        try {
            function1.invoke(e);
        } catch (java.lang.Throwable ex) {
            if (undeliveredElementException != null && undeliveredElementException.getCause() != ex) {
                kotlinx.coroutines.internal.UndeliveredElementException $this$addSuppressedThrowable$iv = undeliveredElementException;
                kotlin.ExceptionsKt.addSuppressed($this$addSuppressedThrowable$iv, ex);
            } else {
                return new kotlinx.coroutines.internal.UndeliveredElementException("Exception in undelivered element handler for " + e, ex);
            }
        }
        return undeliveredElementException;
    }

    public static final <E> void callUndeliveredElement(kotlin.jvm.functions.Function1<? super E, kotlin.Unit> function1, E e, kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlinx.coroutines.internal.UndeliveredElementException ex = callUndeliveredElementCatchingException(function1, e, null);
        if (ex != null) {
            kotlinx.coroutines.CoroutineExceptionHandlerKt.handleCoroutineException(context, ex);
        }
    }

    public static final <E> kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> bindCancellationFun(final kotlin.jvm.functions.Function1<? super E, kotlin.Unit> function1, final E e, final kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function1, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        return new kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit>() { // from class: kotlinx.coroutines.internal.OnUndeliveredElementKt.bindCancellationFun.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Throwable th) {
                invoke2(th);
                return kotlin.Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(java.lang.Throwable th) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(th, "<anonymous parameter 0>");
                kotlinx.coroutines.internal.OnUndeliveredElementKt.callUndeliveredElement(function1, e, context);
            }
        };
    }
}
