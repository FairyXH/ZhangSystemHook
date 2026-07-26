package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: ThreadContext.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u001a\u001a\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u0004H\u0000\u001a\u0010\u0010\u000f\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\rH\u0000\u001a\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u00042\u0006\u0010\f\u001a\u00020\r2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0004H\u0000\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u0002\n\u0000\"$\u0010\u0002\u001a\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0012\u0004\u0012\u00020\u0005\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\",\u0010\u0006\u001a \u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u0007\u0012\u0004\u0012\u00020\u0005\u0012\n\u0012\b\u0012\u0002\b\u0003\u0018\u00010\u00070\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\" \u0010\b\u001a\u0014\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, d2 = {"NO_THREAD_ELEMENTS", "Lkotlinx/coroutines/internal/Symbol;", "countAll", "Lkotlin/Function2;", "", "Lkotlin/coroutines/CoroutineContext$Element;", "findOne", "Lkotlinx/coroutines/ThreadContextElement;", "updateState", "Lkotlinx/coroutines/internal/ThreadState;", "restoreThreadContext", "", "context", "Lkotlin/coroutines/CoroutineContext;", "oldState", "threadContextElements", "updateThreadContext", "countOrElement", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class ThreadContextKt {
    public static final kotlinx.coroutines.internal.Symbol NO_THREAD_ELEMENTS = new kotlinx.coroutines.internal.Symbol("NO_THREAD_ELEMENTS");
    private static final kotlin.jvm.functions.Function2<java.lang.Object, kotlin.coroutines.CoroutineContext.Element, java.lang.Object> countAll = new kotlin.jvm.functions.Function2<java.lang.Object, kotlin.coroutines.CoroutineContext.Element, java.lang.Object>() { // from class: kotlinx.coroutines.internal.ThreadContextKt$countAll$1
        @Override // kotlin.jvm.functions.Function2
        public final java.lang.Object invoke(java.lang.Object countOrElement, kotlin.coroutines.CoroutineContext.Element element) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "element");
            if (element instanceof kotlinx.coroutines.ThreadContextElement) {
                java.lang.Integer num = countOrElement instanceof java.lang.Integer ? (java.lang.Integer) countOrElement : null;
                int inCount = num != null ? num.intValue() : 1;
                return inCount == 0 ? element : java.lang.Integer.valueOf(inCount + 1);
            }
            return countOrElement;
        }
    };
    private static final kotlin.jvm.functions.Function2<kotlinx.coroutines.ThreadContextElement<?>, kotlin.coroutines.CoroutineContext.Element, kotlinx.coroutines.ThreadContextElement<?>> findOne = new kotlin.jvm.functions.Function2<kotlinx.coroutines.ThreadContextElement<?>, kotlin.coroutines.CoroutineContext.Element, kotlinx.coroutines.ThreadContextElement<?>>() { // from class: kotlinx.coroutines.internal.ThreadContextKt$findOne$1
        @Override // kotlin.jvm.functions.Function2
        public final kotlinx.coroutines.ThreadContextElement<?> invoke(kotlinx.coroutines.ThreadContextElement<?> threadContextElement, kotlin.coroutines.CoroutineContext.Element element) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "element");
            if (threadContextElement != null) {
                return threadContextElement;
            }
            if (element instanceof kotlinx.coroutines.ThreadContextElement) {
                return (kotlinx.coroutines.ThreadContextElement) element;
            }
            return null;
        }
    };
    private static final kotlin.jvm.functions.Function2<kotlinx.coroutines.internal.ThreadState, kotlin.coroutines.CoroutineContext.Element, kotlinx.coroutines.internal.ThreadState> updateState = new kotlin.jvm.functions.Function2<kotlinx.coroutines.internal.ThreadState, kotlin.coroutines.CoroutineContext.Element, kotlinx.coroutines.internal.ThreadState>() { // from class: kotlinx.coroutines.internal.ThreadContextKt$updateState$1
        @Override // kotlin.jvm.functions.Function2
        public final kotlinx.coroutines.internal.ThreadState invoke(kotlinx.coroutines.internal.ThreadState state, kotlin.coroutines.CoroutineContext.Element element) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(state, "state");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "element");
            if (element instanceof kotlinx.coroutines.ThreadContextElement) {
                state.append((kotlinx.coroutines.ThreadContextElement) element, ((kotlinx.coroutines.ThreadContextElement) element).updateThreadContext(state.context));
            }
            return state;
        }
    };

    public static final java.lang.Object threadContextElements(kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        java.lang.Object objFold = context.fold(0, countAll);
        kotlin.jvm.internal.Intrinsics.checkNotNull(objFold);
        return objFold;
    }

    public static final java.lang.Object updateThreadContext(kotlin.coroutines.CoroutineContext context, java.lang.Object countOrElement) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        java.lang.Object countOrElement2 = countOrElement == null ? threadContextElements(context) : countOrElement;
        if (countOrElement2 == 0) {
            return NO_THREAD_ELEMENTS;
        }
        if (countOrElement2 instanceof java.lang.Integer) {
            return context.fold(new kotlinx.coroutines.internal.ThreadState(context, ((java.lang.Number) countOrElement2).intValue()), updateState);
        }
        kotlin.jvm.internal.Intrinsics.checkNotNull(countOrElement2, "null cannot be cast to non-null type kotlinx.coroutines.ThreadContextElement<kotlin.Any?>");
        kotlinx.coroutines.ThreadContextElement element = (kotlinx.coroutines.ThreadContextElement) countOrElement2;
        return element.updateThreadContext(context);
    }

    public static final void restoreThreadContext(kotlin.coroutines.CoroutineContext context, java.lang.Object oldState) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        if (oldState == NO_THREAD_ELEMENTS) {
            return;
        }
        if (oldState instanceof kotlinx.coroutines.internal.ThreadState) {
            ((kotlinx.coroutines.internal.ThreadState) oldState).restore(context);
            return;
        }
        java.lang.Object objFold = context.fold(null, findOne);
        kotlin.jvm.internal.Intrinsics.checkNotNull(objFold, "null cannot be cast to non-null type kotlinx.coroutines.ThreadContextElement<kotlin.Any?>");
        kotlinx.coroutines.ThreadContextElement element = (kotlinx.coroutines.ThreadContextElement) objFold;
        element.restoreThreadContext(context, oldState);
    }
}
