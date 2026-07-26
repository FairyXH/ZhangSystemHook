package kotlinx.coroutines;

/* JADX INFO: compiled from: CoroutineContext.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000>\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a \u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\nH\u0002\u001a;\u0010\u000b\u001a\u0002H\f\"\u0004\b\u0000\u0010\f2\n\u0010\r\u001a\u0006\u0012\u0002\b\u00030\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\f0\u0012H\u0080\bø\u0001\u0000¢\u0006\u0002\u0010\u0013\u001a7\u0010\u0014\u001a\u0002H\f\"\u0004\b\u0000\u0010\f2\u0006\u0010\u0015\u001a\u00020\u00032\b\u0010\u000f\u001a\u0004\u0018\u00010\u00102\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\f0\u0012H\u0080\bø\u0001\u0000¢\u0006\u0002\u0010\u0016\u001a\f\u0010\u0017\u001a\u00020\n*\u00020\u0003H\u0002\u001a\u0014\u0010\u0018\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0019\u001a\u00020\u0003H\u0007\u001a\u0014\u0010\u0018\u001a\u00020\u0003*\u00020\u001a2\u0006\u0010\u0015\u001a\u00020\u0003H\u0007\u001a\u0013\u0010\u001b\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u001c*\u00020\u001dH\u0080\u0010\u001a(\u0010\u001e\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u001c*\u0006\u0012\u0002\b\u00030\u000e2\u0006\u0010\u0015\u001a\u00020\u00032\b\u0010\u001f\u001a\u0004\u0018\u00010\u0010H\u0000\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082T¢\u0006\u0002\n\u0000\"\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u0001*\u00020\u00038@X\u0080\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006 "}, d2 = {"DEBUG_THREAD_NAME_SEPARATOR", "", "coroutineName", "Lkotlin/coroutines/CoroutineContext;", "getCoroutineName", "(Lkotlin/coroutines/CoroutineContext;)Ljava/lang/String;", "foldCopies", "originalContext", "appendContext", "isNewCoroutine", "", "withContinuationContext", "T", "continuation", "Lkotlin/coroutines/Continuation;", "countOrElement", "", "block", "Lkotlin/Function0;", "(Lkotlin/coroutines/Continuation;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "withCoroutineContext", "context", "(Lkotlin/coroutines/CoroutineContext;Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "hasCopyableElements", "newCoroutineContext", "addedContext", "Lkotlinx/coroutines/CoroutineScope;", "undispatchedCompletion", "Lkotlinx/coroutines/UndispatchedCoroutine;", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "updateUndispatchedCompletion", "oldValue", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class CoroutineContextKt {
    private static final java.lang.String DEBUG_THREAD_NAME_SEPARATOR = " @";

    public static final kotlin.coroutines.CoroutineContext newCoroutineContext(kotlinx.coroutines.CoroutineScope $this$newCoroutineContext, kotlin.coroutines.CoroutineContext context) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$newCoroutineContext, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.coroutines.CoroutineContext combined = foldCopies($this$newCoroutineContext.getCoroutineContext(), context, true);
        kotlin.coroutines.CoroutineContext debug = kotlinx.coroutines.DebugKt.getDEBUG() ? combined.plus(new kotlinx.coroutines.CoroutineId(kotlinx.coroutines.DebugKt.getCOROUTINE_ID().incrementAndGet())) : combined;
        return (combined == kotlinx.coroutines.Dispatchers.getDefault() || combined.get(kotlin.coroutines.ContinuationInterceptor.INSTANCE) != null) ? debug : debug.plus(kotlinx.coroutines.Dispatchers.getDefault());
    }

    public static final kotlin.coroutines.CoroutineContext newCoroutineContext(kotlin.coroutines.CoroutineContext $this$newCoroutineContext, kotlin.coroutines.CoroutineContext addedContext) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$newCoroutineContext, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(addedContext, "addedContext");
        return !hasCopyableElements(addedContext) ? $this$newCoroutineContext.plus(addedContext) : foldCopies($this$newCoroutineContext, addedContext, false);
    }

    private static final boolean hasCopyableElements(kotlin.coroutines.CoroutineContext $this$hasCopyableElements) {
        return ((java.lang.Boolean) $this$hasCopyableElements.fold(false, new kotlin.jvm.functions.Function2<java.lang.Boolean, kotlin.coroutines.CoroutineContext.Element, java.lang.Boolean>() { // from class: kotlinx.coroutines.CoroutineContextKt.hasCopyableElements.1
            public final java.lang.Boolean invoke(boolean result, kotlin.coroutines.CoroutineContext.Element it) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
                return java.lang.Boolean.valueOf(result || (it instanceof kotlinx.coroutines.CopyableThreadContextElement));
            }

            @Override // kotlin.jvm.functions.Function2
            public /* bridge */ /* synthetic */ java.lang.Boolean invoke(java.lang.Boolean bool, kotlin.coroutines.CoroutineContext.Element element) {
                return invoke(bool.booleanValue(), element);
            }
        })).booleanValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v7, types: [T, java.lang.Object] */
    private static final kotlin.coroutines.CoroutineContext foldCopies(kotlin.coroutines.CoroutineContext originalContext, kotlin.coroutines.CoroutineContext coroutineContext, final boolean isNewCoroutine) {
        boolean hasElementsLeft = hasCopyableElements(originalContext);
        boolean hasElementsRight = hasCopyableElements(coroutineContext);
        if (!hasElementsLeft && !hasElementsRight) {
            return originalContext.plus(coroutineContext);
        }
        final kotlin.jvm.internal.Ref.ObjectRef leftoverContext = new kotlin.jvm.internal.Ref.ObjectRef();
        leftoverContext.element = coroutineContext;
        kotlin.coroutines.CoroutineContext folded = (kotlin.coroutines.CoroutineContext) originalContext.fold(kotlin.coroutines.EmptyCoroutineContext.INSTANCE, new kotlin.jvm.functions.Function2<kotlin.coroutines.CoroutineContext, kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext>() { // from class: kotlinx.coroutines.CoroutineContextKt$foldCopies$folded$1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            /* JADX WARN: Type inference failed for: r2v3, types: [T, kotlin.coroutines.CoroutineContext] */
            @Override // kotlin.jvm.functions.Function2
            public final kotlin.coroutines.CoroutineContext invoke(kotlin.coroutines.CoroutineContext result, kotlin.coroutines.CoroutineContext.Element element) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(result, "result");
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "element");
                if (!(element instanceof kotlinx.coroutines.CopyableThreadContextElement)) {
                    return result.plus(element);
                }
                kotlin.coroutines.CoroutineContext.Element newElement = leftoverContext.element.get(element.getKey());
                if (newElement == null) {
                    return result.plus(isNewCoroutine ? ((kotlinx.coroutines.CopyableThreadContextElement) element).copyForChild() : (kotlinx.coroutines.CopyableThreadContextElement) element);
                }
                leftoverContext.element = leftoverContext.element.minusKey(element.getKey());
                return result.plus(((kotlinx.coroutines.CopyableThreadContextElement) element).mergeForChild(newElement));
            }
        });
        if (hasElementsRight) {
            leftoverContext.element = ((kotlin.coroutines.CoroutineContext) leftoverContext.element).fold(kotlin.coroutines.EmptyCoroutineContext.INSTANCE, new kotlin.jvm.functions.Function2<kotlin.coroutines.CoroutineContext, kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext>() { // from class: kotlinx.coroutines.CoroutineContextKt.foldCopies.1
                @Override // kotlin.jvm.functions.Function2
                public final kotlin.coroutines.CoroutineContext invoke(kotlin.coroutines.CoroutineContext result, kotlin.coroutines.CoroutineContext.Element element) {
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(result, "result");
                    kotlin.jvm.internal.Intrinsics.checkNotNullParameter(element, "element");
                    if (element instanceof kotlinx.coroutines.CopyableThreadContextElement) {
                        return result.plus(((kotlinx.coroutines.CopyableThreadContextElement) element).copyForChild());
                    }
                    return result.plus(element);
                }
            });
        }
        return folded.plus((kotlin.coroutines.CoroutineContext) leftoverContext.element);
    }

    public static final <T> T withCoroutineContext(kotlin.coroutines.CoroutineContext context, java.lang.Object countOrElement, kotlin.jvm.functions.Function0<? extends T> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        java.lang.Object oldValue = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(context, countOrElement);
        try {
            return block.invoke();
        } finally {
            kotlin.jvm.internal.InlineMarker.finallyStart(1);
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(context, oldValue);
            kotlin.jvm.internal.InlineMarker.finallyEnd(1);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x002f A[DONT_GENERATE] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final <T> T withContinuationContext(kotlin.coroutines.Continuation<?> r7, java.lang.Object r8, kotlin.jvm.functions.Function0<? extends T> r9) {
        /*
            java.lang.String r0 = "continuation"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r7, r0)
            java.lang.String r0 = "block"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r9, r0)
            r0 = 0
            kotlin.coroutines.CoroutineContext r1 = r7.getContext()
            java.lang.Object r2 = kotlinx.coroutines.internal.ThreadContextKt.updateThreadContext(r1, r8)
            kotlinx.coroutines.internal.Symbol r3 = kotlinx.coroutines.internal.ThreadContextKt.NO_THREAD_ELEMENTS
            if (r2 == r3) goto L1c
            kotlinx.coroutines.UndispatchedCoroutine r3 = updateUndispatchedCompletion(r7, r1, r2)
            goto L1d
        L1c:
            r3 = 0
        L1d:
            r4 = 1
            java.lang.Object r5 = r9.invoke()     // Catch: java.lang.Throwable -> L36
            kotlin.jvm.internal.InlineMarker.finallyStart(r4)
            if (r3 == 0) goto L2f
            boolean r6 = r3.clearThreadContext()
            if (r6 == 0) goto L32
        L2f:
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(r1, r2)
        L32:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r4)
            return r5
        L36:
            r5 = move-exception
            kotlin.jvm.internal.InlineMarker.finallyStart(r4)
            if (r3 == 0) goto L42
            boolean r6 = r3.clearThreadContext()
            if (r6 == 0) goto L45
        L42:
            kotlinx.coroutines.internal.ThreadContextKt.restoreThreadContext(r1, r2)
        L45:
            kotlin.jvm.internal.InlineMarker.finallyEnd(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.CoroutineContextKt.withContinuationContext(kotlin.coroutines.Continuation, java.lang.Object, kotlin.jvm.functions.Function0):java.lang.Object");
    }

    public static final kotlinx.coroutines.UndispatchedCoroutine<?> updateUndispatchedCompletion(kotlin.coroutines.Continuation<?> continuation, kotlin.coroutines.CoroutineContext context, java.lang.Object oldValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(context, "context");
        if (!(continuation instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
            return null;
        }
        boolean potentiallyHasUndispatchedCoroutine = context.get(kotlinx.coroutines.UndispatchedMarker.INSTANCE) != null;
        if (!potentiallyHasUndispatchedCoroutine) {
            return null;
        }
        kotlinx.coroutines.UndispatchedCoroutine<?> undispatchedCoroutineUndispatchedCompletion = undispatchedCompletion((kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation);
        if (undispatchedCoroutineUndispatchedCompletion != null) {
            undispatchedCoroutineUndispatchedCompletion.saveThreadContext(context, oldValue);
        }
        return undispatchedCoroutineUndispatchedCompletion;
    }

    public static final kotlinx.coroutines.UndispatchedCoroutine<?> undispatchedCompletion(kotlin.coroutines.jvm.internal.CoroutineStackFrame $this$undispatchedCompletion) {
        kotlin.coroutines.jvm.internal.CoroutineStackFrame completion;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$undispatchedCompletion, "<this>");
        while (!($this$undispatchedCompletion instanceof kotlinx.coroutines.DispatchedCoroutine) && (completion = $this$undispatchedCompletion.getCallerFrame()) != null) {
            if (completion instanceof kotlinx.coroutines.UndispatchedCoroutine) {
                return (kotlinx.coroutines.UndispatchedCoroutine) completion;
            }
            $this$undispatchedCompletion = completion;
        }
        return null;
    }

    public static final java.lang.String getCoroutineName(kotlin.coroutines.CoroutineContext $this$coroutineName) {
        kotlinx.coroutines.CoroutineId coroutineId;
        java.lang.String coroutineName;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$coroutineName, "<this>");
        if (!kotlinx.coroutines.DebugKt.getDEBUG() || (coroutineId = (kotlinx.coroutines.CoroutineId) $this$coroutineName.get(kotlinx.coroutines.CoroutineId.INSTANCE)) == null) {
            return null;
        }
        kotlinx.coroutines.CoroutineName coroutineName2 = (kotlinx.coroutines.CoroutineName) $this$coroutineName.get(kotlinx.coroutines.CoroutineName.INSTANCE);
        if (coroutineName2 == null || (coroutineName = coroutineName2.getName()) == null) {
            coroutineName = "coroutine";
        }
        return coroutineName + "#" + coroutineId.getId();
    }
}
