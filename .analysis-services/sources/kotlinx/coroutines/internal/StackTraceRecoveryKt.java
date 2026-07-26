package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: StackTraceRecovery.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\u0001\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\b\u001a9\u0010\b\u001a\u0002H\t\"\b\b\u0000\u0010\t*\u00020\n2\u0006\u0010\u000b\u001a\u0002H\t2\u0006\u0010\f\u001a\u0002H\t2\u0010\u0010\r\u001a\f\u0012\b\u0012\u00060\u0001j\u0002`\u000f0\u000eH\u0002¢\u0006\u0002\u0010\u0010\u001a\u001e\u0010\u0011\u001a\f\u0012\b\u0012\u00060\u0001j\u0002`\u000f0\u000e2\n\u0010\u0012\u001a\u00060\u0013j\u0002`\u0014H\u0002\u001a1\u0010\u0015\u001a\u00020\u00162\u0010\u0010\u0017\u001a\f\u0012\b\u0012\u00060\u0001j\u0002`\u000f0\u00182\u0010\u0010\f\u001a\f\u0012\b\u0012\u00060\u0001j\u0002`\u000f0\u000eH\u0002¢\u0006\u0002\u0010\u0019\u001a\u0016\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\nH\u0080H¢\u0006\u0002\u0010\u001d\u001a+\u0010\u001e\u001a\u0002H\t\"\b\b\u0000\u0010\t*\u00020\n2\u0006\u0010\u001c\u001a\u0002H\t2\n\u0010\u0012\u001a\u00060\u0013j\u0002`\u0014H\u0002¢\u0006\u0002\u0010\u001f\u001a\u001f\u0010 \u001a\u0002H\t\"\b\b\u0000\u0010\t*\u00020\n2\u0006\u0010\u001c\u001a\u0002H\tH\u0000¢\u0006\u0002\u0010!\u001a,\u0010 \u001a\u0002H\t\"\b\b\u0000\u0010\t*\u00020\n2\u0006\u0010\u001c\u001a\u0002H\t2\n\u0010\u0012\u001a\u0006\u0012\u0002\b\u00030\"H\u0080\b¢\u0006\u0002\u0010#\u001a \u0010$\u001a\u0002H\t\"\b\b\u0000\u0010\t*\u00020\n2\u0006\u0010\u001c\u001a\u0002H\tH\u0080\b¢\u0006\u0002\u0010!\u001a\u001f\u0010%\u001a\u0002H\t\"\b\b\u0000\u0010\t*\u00020\n2\u0006\u0010\u001c\u001a\u0002H\tH\u0000¢\u0006\u0002\u0010!\u001a1\u0010&\u001a\u0018\u0012\u0004\u0012\u0002H\t\u0012\u000e\u0012\f\u0012\b\u0012\u00060\u0001j\u0002`\u000f0\u00180'\"\b\b\u0000\u0010\t*\u00020\n*\u0002H\tH\u0002¢\u0006\u0002\u0010(\u001a\u001c\u0010)\u001a\u00020**\u00060\u0001j\u0002`\u000f2\n\u0010+\u001a\u00060\u0001j\u0002`\u000fH\u0002\u001a#\u0010,\u001a\u00020-*\f\u0012\b\u0012\u00060\u0001j\u0002`\u000f0\u00182\u0006\u0010.\u001a\u00020\u0003H\u0002¢\u0006\u0002\u0010/\u001a\u0014\u00100\u001a\u00020\u0016*\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0000\u001a\u0010\u00101\u001a\u00020**\u00060\u0001j\u0002`\u000fH\u0000\u001a\u001b\u00102\u001a\u0002H\t\"\b\b\u0000\u0010\t*\u00020\n*\u0002H\tH\u0002¢\u0006\u0002\u0010!\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0082T¢\u0006\u0002\n\u0000\"\u0016\u0010\u0004\u001a\n \u0005*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0006\u001a\u00020\u0003X\u0082T¢\u0006\u0002\n\u0000\"\u0016\u0010\u0007\u001a\n \u0005*\u0004\u0018\u00010\u00030\u0003X\u0082\u0004¢\u0006\u0002\n\u0000*\f\b\u0000\u00103\"\u00020\u00132\u00020\u0013*\f\b\u0000\u00104\"\u00020\u00012\u00020\u0001¨\u00065"}, d2 = {"ARTIFICIAL_FRAME", "Ljava/lang/StackTraceElement;", "baseContinuationImplClass", "", "baseContinuationImplClassName", "kotlin.jvm.PlatformType", "stackTraceRecoveryClass", "stackTraceRecoveryClassName", "createFinalException", "E", "", "cause", "result", "resultStackTrace", "Ljava/util/ArrayDeque;", "Lkotlinx/coroutines/internal/StackTraceElement;", "(Ljava/lang/Throwable;Ljava/lang/Throwable;Ljava/util/ArrayDeque;)Ljava/lang/Throwable;", "createStackTrace", "continuation", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "Lkotlinx/coroutines/internal/CoroutineStackFrame;", "mergeRecoveredTraces", "", "recoveredStacktrace", "", "([Ljava/lang/StackTraceElement;Ljava/util/ArrayDeque;)V", "recoverAndThrow", "", "exception", "(Ljava/lang/Throwable;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "recoverFromStackFrame", "(Ljava/lang/Throwable;Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;)Ljava/lang/Throwable;", "recoverStackTrace", "(Ljava/lang/Throwable;)Ljava/lang/Throwable;", "Lkotlin/coroutines/Continuation;", "(Ljava/lang/Throwable;Lkotlin/coroutines/Continuation;)Ljava/lang/Throwable;", "unwrap", "unwrapImpl", "causeAndStacktrace", "Lkotlin/Pair;", "(Ljava/lang/Throwable;)Lkotlin/Pair;", "elementWiseEquals", "", "e", "firstFrameIndex", "", "methodName", "([Ljava/lang/StackTraceElement;Ljava/lang/String;)I", "initCause", "isArtificial", "sanitizeStackTrace", "CoroutineStackFrame", "StackTraceElement", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class StackTraceRecoveryKt {
    private static final java.lang.StackTraceElement ARTIFICIAL_FRAME;
    private static final java.lang.String baseContinuationImplClass = "kotlin.coroutines.jvm.internal.BaseContinuationImpl";
    private static final java.lang.String baseContinuationImplClassName;
    private static final java.lang.String stackTraceRecoveryClass = "kotlinx.coroutines.internal.StackTraceRecoveryKt";
    private static final java.lang.String stackTraceRecoveryClassName;

    public static /* synthetic */ void CoroutineStackFrame$annotations() {
    }

    public static /* synthetic */ void StackTraceElement$annotations() {
    }

    static {
        java.lang.Object objM11307constructorimpl;
        java.lang.Object objM11307constructorimpl2;
        java.lang.Object obj = stackTraceRecoveryClass;
        java.lang.Object obj2 = baseContinuationImplClass;
        ARTIFICIAL_FRAME = new _COROUTINE.ArtificialStackFrames().coroutineBoundary();
        try {
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl(java.lang.Class.forName(baseContinuationImplClass).getCanonicalName());
        } catch (java.lang.Throwable th) {
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(th));
        }
        if (kotlin.Result.m11310exceptionOrNullimpl(objM11307constructorimpl) == null) {
            obj2 = objM11307constructorimpl;
        }
        baseContinuationImplClassName = (java.lang.String) obj2;
        try {
            kotlin.Result.Companion companion3 = kotlin.Result.INSTANCE;
            objM11307constructorimpl2 = kotlin.Result.m11307constructorimpl(java.lang.Class.forName(stackTraceRecoveryClass).getCanonicalName());
        } catch (java.lang.Throwable th2) {
            kotlin.Result.Companion companion4 = kotlin.Result.INSTANCE;
            objM11307constructorimpl2 = kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(th2));
        }
        if (kotlin.Result.m11310exceptionOrNullimpl(objM11307constructorimpl2) == null) {
            obj = objM11307constructorimpl2;
        }
        stackTraceRecoveryClassName = (java.lang.String) obj;
    }

    public static final <E extends java.lang.Throwable> E recoverStackTrace(E exception) {
        java.lang.Throwable thTryCopyException;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        return (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (thTryCopyException = kotlinx.coroutines.internal.ExceptionsConstructorKt.tryCopyException(exception)) != null) ? (E) sanitizeStackTrace(thTryCopyException) : exception;
    }

    private static final <E extends java.lang.Throwable> E sanitizeStackTrace(E e) {
        int index$iv;
        java.lang.StackTraceElement stackTraceElement;
        java.lang.StackTraceElement[] stackTrace = e.getStackTrace();
        int size = stackTrace.length;
        kotlin.jvm.internal.Intrinsics.checkNotNull(stackTrace);
        int length = stackTrace.length - 1;
        if (length >= 0) {
            do {
                index$iv = length;
                length--;
                java.lang.StackTraceElement it = stackTrace[index$iv];
                if (kotlin.jvm.internal.Intrinsics.areEqual(stackTraceRecoveryClassName, it.getClassName())) {
                    break;
                }
            } while (length >= 0);
            index$iv = -1;
        } else {
            index$iv = -1;
        }
        int lastIntrinsic = index$iv;
        int startIndex = lastIntrinsic + 1;
        java.lang.String baseContinuationImplClassName2 = baseContinuationImplClassName;
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(baseContinuationImplClassName2, "baseContinuationImplClassName");
        int endIndex = firstFrameIndex(stackTrace, baseContinuationImplClassName2);
        int adjustment = endIndex == -1 ? 0 : size - endIndex;
        int i = (size - lastIntrinsic) - adjustment;
        java.lang.StackTraceElement[] trace = new java.lang.StackTraceElement[i];
        for (int i2 = 0; i2 < i; i2++) {
            if (i2 == 0) {
                stackTraceElement = ARTIFICIAL_FRAME;
            } else {
                stackTraceElement = stackTrace[(startIndex + i2) - 1];
            }
            trace[i2] = stackTraceElement;
        }
        e.setStackTrace(trace);
        return e;
    }

    public static final <E extends java.lang.Throwable> E recoverStackTrace(E exception, kotlin.coroutines.Continuation<?> continuation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(continuation, "continuation");
        if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (continuation instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
            return (E) recoverFromStackFrame(exception, (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation);
        }
        return exception;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final <E extends java.lang.Throwable> E recoverFromStackFrame(E e, kotlin.coroutines.jvm.internal.CoroutineStackFrame coroutineStackFrame) {
        kotlin.Pair pairCauseAndStacktrace = causeAndStacktrace(e);
        java.lang.Throwable th = (java.lang.Throwable) pairCauseAndStacktrace.component1();
        java.lang.StackTraceElement[] stackTraceElementArr = (java.lang.StackTraceElement[]) pairCauseAndStacktrace.component2();
        java.lang.Throwable thTryCopyException = kotlinx.coroutines.internal.ExceptionsConstructorKt.tryCopyException(th);
        if (thTryCopyException == null) {
            return e;
        }
        java.util.ArrayDeque<java.lang.StackTraceElement> arrayDequeCreateStackTrace = createStackTrace(coroutineStackFrame);
        if (arrayDequeCreateStackTrace.isEmpty()) {
            return e;
        }
        if (th != e) {
            mergeRecoveredTraces(stackTraceElementArr, arrayDequeCreateStackTrace);
        }
        return (E) createFinalException(th, thTryCopyException, arrayDequeCreateStackTrace);
    }

    private static final <E extends java.lang.Throwable> E createFinalException(E e, E e2, java.util.ArrayDeque<java.lang.StackTraceElement> arrayDeque) {
        arrayDeque.addFirst(ARTIFICIAL_FRAME);
        java.lang.StackTraceElement[] causeTrace = e.getStackTrace();
        kotlin.jvm.internal.Intrinsics.checkNotNull(causeTrace);
        java.lang.String baseContinuationImplClassName2 = baseContinuationImplClassName;
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(baseContinuationImplClassName2, "baseContinuationImplClassName");
        int size = firstFrameIndex(causeTrace, baseContinuationImplClassName2);
        int i = 0;
        if (size == -1) {
            java.util.ArrayDeque<java.lang.StackTraceElement> $this$toTypedArray$iv = arrayDeque;
            e2.setStackTrace((java.lang.StackTraceElement[]) $this$toTypedArray$iv.toArray(new java.lang.StackTraceElement[0]));
            return e2;
        }
        java.lang.StackTraceElement[] mergedStackTrace = new java.lang.StackTraceElement[arrayDeque.size() + size];
        for (int i2 = 0; i2 < size; i2++) {
            mergedStackTrace[i2] = causeTrace[i2];
        }
        for (java.lang.StackTraceElement element : arrayDeque) {
            int index = i;
            i++;
            mergedStackTrace[size + index] = element;
        }
        e2.setStackTrace(mergedStackTrace);
        return e2;
    }

    private static final <E extends java.lang.Throwable> kotlin.Pair<E, java.lang.StackTraceElement[]> causeAndStacktrace(E e) {
        boolean z;
        java.lang.Throwable cause = e.getCause();
        if (cause != null && kotlin.jvm.internal.Intrinsics.areEqual(cause.getClass(), e.getClass())) {
            java.lang.StackTraceElement[] currentTrace = e.getStackTrace();
            kotlin.jvm.internal.Intrinsics.checkNotNull(currentTrace);
            int length = currentTrace.length;
            int i = 0;
            while (true) {
                if (i < length) {
                    java.lang.StackTraceElement stackTraceElement = currentTrace[i];
                    kotlin.jvm.internal.Intrinsics.checkNotNull(stackTraceElement);
                    if (isArtificial(stackTraceElement)) {
                        z = true;
                        break;
                    }
                    i++;
                } else {
                    z = false;
                    break;
                }
            }
            if (z) {
                return kotlin.TuplesKt.to(cause, currentTrace);
            }
            return kotlin.TuplesKt.to(e, new java.lang.StackTraceElement[0]);
        }
        return kotlin.TuplesKt.to(e, new java.lang.StackTraceElement[0]);
    }

    private static final void mergeRecoveredTraces(java.lang.StackTraceElement[] recoveredStacktrace, java.util.ArrayDeque<java.lang.StackTraceElement> arrayDeque) {
        int index$iv = 0;
        int length = recoveredStacktrace.length;
        while (true) {
            if (index$iv < length) {
                java.lang.StackTraceElement it = recoveredStacktrace[index$iv];
                if (isArtificial(it)) {
                    break;
                } else {
                    index$iv++;
                }
            } else {
                index$iv = -1;
                break;
            }
        }
        int startIndex = index$iv + 1;
        int lastFrameIndex = recoveredStacktrace.length - 1;
        int i = lastFrameIndex;
        if (startIndex > i) {
            return;
        }
        while (true) {
            java.lang.StackTraceElement element = recoveredStacktrace[i];
            java.lang.StackTraceElement last = arrayDeque.getLast();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(last, "getLast(...)");
            if (elementWiseEquals(element, last)) {
                arrayDeque.removeLast();
            }
            arrayDeque.addFirst(recoveredStacktrace[i]);
            if (i == startIndex) {
                return;
            } else {
                i--;
            }
        }
    }

    public static final java.lang.Object recoverAndThrow(java.lang.Throwable exception, kotlin.coroutines.Continuation<?> continuation) throws java.lang.Throwable {
        if (kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() && (continuation instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame)) {
            throw recoverFromStackFrame(exception, (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation);
        }
        throw exception;
    }

    private static final java.lang.Object recoverAndThrow$$forInline(java.lang.Throwable exception, kotlin.coroutines.Continuation<?> continuation) throws java.lang.Throwable {
        if (!kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES()) {
            throw exception;
        }
        kotlin.jvm.internal.InlineMarker.mark(0);
        kotlin.coroutines.Continuation<?> continuation2 = continuation;
        if (continuation2 instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame) {
            throw recoverFromStackFrame(exception, (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation2);
        }
        throw exception;
    }

    public static final <E extends java.lang.Throwable> E unwrap(E exception) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        return !kotlinx.coroutines.DebugKt.getRECOVER_STACK_TRACES() ? exception : (E) unwrapImpl(exception);
    }

    public static final <E extends java.lang.Throwable> E unwrapImpl(E exception) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(exception, "exception");
        E e = (E) exception.getCause();
        if (e == null || !kotlin.jvm.internal.Intrinsics.areEqual(e.getClass(), exception.getClass())) {
            return exception;
        }
        java.lang.StackTraceElement[] stackTrace = exception.getStackTrace();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(stackTrace, "getStackTrace(...)");
        java.lang.StackTraceElement[] stackTraceElementArr = stackTrace;
        int length = stackTraceElementArr.length;
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            java.lang.StackTraceElement stackTraceElement = stackTraceElementArr[i];
            kotlin.jvm.internal.Intrinsics.checkNotNull(stackTraceElement);
            if (isArtificial(stackTraceElement)) {
                z = true;
                break;
            }
            i++;
        }
        if (z) {
            return e;
        }
        return exception;
    }

    private static final java.util.ArrayDeque<java.lang.StackTraceElement> createStackTrace(kotlin.coroutines.jvm.internal.CoroutineStackFrame continuation) {
        kotlin.coroutines.jvm.internal.CoroutineStackFrame callerFrame;
        java.util.ArrayDeque<java.lang.StackTraceElement> arrayDeque = new java.util.ArrayDeque<>();
        java.lang.StackTraceElement it = continuation.getStackTraceElement();
        if (it != null) {
            arrayDeque.add(it);
        }
        kotlin.coroutines.jvm.internal.CoroutineStackFrame last = continuation;
        while (true) {
            kotlin.coroutines.jvm.internal.CoroutineStackFrame coroutineStackFrame = last instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame ? last : null;
            if (coroutineStackFrame == null || (callerFrame = coroutineStackFrame.getCallerFrame()) == null) {
                break;
            }
            last = callerFrame;
            java.lang.StackTraceElement it2 = last.getStackTraceElement();
            if (it2 != null) {
                arrayDeque.add(it2);
            }
        }
        return arrayDeque;
    }

    public static final boolean isArtificial(java.lang.StackTraceElement $this$isArtificial) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$isArtificial, "<this>");
        java.lang.String className = $this$isArtificial.getClassName();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(className, "getClassName(...)");
        return kotlin.text.StringsKt.startsWith$default(className, _COROUTINE.CoroutineDebuggingKt.getARTIFICIAL_FRAME_PACKAGE_NAME(), false, 2, (java.lang.Object) null);
    }

    private static final int firstFrameIndex(java.lang.StackTraceElement[] $this$firstFrameIndex, java.lang.String methodName) {
        int length = $this$firstFrameIndex.length;
        for (int index$iv = 0; index$iv < length; index$iv++) {
            java.lang.StackTraceElement it = $this$firstFrameIndex[index$iv];
            if (kotlin.jvm.internal.Intrinsics.areEqual(methodName, it.getClassName())) {
                return index$iv;
            }
        }
        return -1;
    }

    private static final boolean elementWiseEquals(java.lang.StackTraceElement $this$elementWiseEquals, java.lang.StackTraceElement e) {
        return $this$elementWiseEquals.getLineNumber() == e.getLineNumber() && kotlin.jvm.internal.Intrinsics.areEqual($this$elementWiseEquals.getMethodName(), e.getMethodName()) && kotlin.jvm.internal.Intrinsics.areEqual($this$elementWiseEquals.getFileName(), e.getFileName()) && kotlin.jvm.internal.Intrinsics.areEqual($this$elementWiseEquals.getClassName(), e.getClassName());
    }

    public static final void initCause(java.lang.Throwable $this$initCause, java.lang.Throwable cause) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$initCause, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(cause, "cause");
        $this$initCause.initCause(cause);
    }
}
