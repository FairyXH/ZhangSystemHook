package kotlinx.coroutines.debug.internal;

/* JADX INFO: compiled from: DebugProbesImpl.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000Æ\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0017\n\u0002\u0010\u0003\n\u0002\b\n\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\bÁ\u0002\u0018\u00002\u00020\u0001:\u0001|B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J,\u00100\u001a\b\u0012\u0004\u0012\u0002H201\"\u0004\b\u0000\u001022\f\u00103\u001a\b\u0012\u0004\u0012\u0002H2012\b\u00104\u001a\u0004\u0018\u000105H\u0002J\u0010\u00106\u001a\u00020\u00142\u0006\u00107\u001a\u000208H\u0001J\f\u00109\u001a\b\u0012\u0004\u0012\u00020;0:J\u0011\u0010<\u001a\b\u0012\u0004\u0012\u00020\u00010=¢\u0006\u0002\u0010>J9\u0010?\u001a\b\u0012\u0004\u0012\u0002H@0:\"\b\b\u0000\u0010@*\u00020\u00012\u001e\b\u0004\u0010A\u001a\u0018\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u000b\u0012\u0004\u0012\u00020C\u0012\u0004\u0012\u0002H@0BH\u0082\bJ\u0010\u0010D\u001a\u00020\u00142\u0006\u00107\u001a\u000208H\u0002J\f\u0010E\u001a\b\u0012\u0004\u0012\u00020F0:J\"\u0010G\u001a\b\u0012\u0004\u0012\u00020\u00040:2\u0006\u0010H\u001a\u00020;2\f\u0010I\u001a\b\u0012\u0004\u0012\u00020\u00040:J\u000e\u0010J\u001a\u00020(2\u0006\u0010H\u001a\u00020;J.\u0010K\u001a\b\u0012\u0004\u0012\u00020\u00040:2\u0006\u0010L\u001a\u00020(2\b\u0010M\u001a\u0004\u0018\u00010&2\f\u0010I\u001a\b\u0012\u0004\u0012\u00020\u00040:H\u0002J=\u0010N\u001a\u000e\u0012\u0004\u0012\u00020P\u0012\u0004\u0012\u00020P0O2\u0006\u0010Q\u001a\u00020P2\f\u0010R\u001a\b\u0012\u0004\u0012\u00020\u00040=2\f\u0010I\u001a\b\u0012\u0004\u0012\u00020\u00040:H\u0002¢\u0006\u0002\u0010SJ1\u0010T\u001a\u00020P2\u0006\u0010U\u001a\u00020P2\f\u0010R\u001a\b\u0012\u0004\u0012\u00020\u00040=2\f\u0010I\u001a\b\u0012\u0004\u0012\u00020\u00040:H\u0002¢\u0006\u0002\u0010VJ\u0016\u0010W\u001a\u0010\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u0013H\u0002J\u0015\u0010X\u001a\u00020(2\u0006\u0010Y\u001a\u00020)H\u0000¢\u0006\u0002\bZJ\r\u0010[\u001a\u00020\u0014H\u0000¢\u0006\u0002\b\\J\u001e\u0010]\u001a\u00020\u00142\u0006\u00107\u001a\u0002082\f\u0010^\u001a\b\u0012\u0004\u0012\u00020\u00040:H\u0002J\u0014\u0010_\u001a\u00020\u00142\n\u0010`\u001a\u0006\u0012\u0002\b\u00030\u000bH\u0002J'\u0010a\u001a\b\u0012\u0004\u0012\u0002H201\"\u0004\b\u0000\u001022\f\u00103\u001a\b\u0012\u0004\u0012\u0002H201H\u0000¢\u0006\u0002\bbJ\u0019\u0010c\u001a\u00020\u00142\n\u00104\u001a\u0006\u0012\u0002\b\u000301H\u0000¢\u0006\u0002\bdJ\u0019\u0010e\u001a\u00020\u00142\n\u00104\u001a\u0006\u0012\u0002\b\u000301H\u0000¢\u0006\u0002\bfJ%\u0010g\u001a\b\u0012\u0004\u0012\u00020\u00040:\"\b\b\u0000\u00102*\u00020h2\u0006\u0010i\u001a\u0002H2H\u0002¢\u0006\u0002\u0010jJ\b\u0010k\u001a\u00020\u0014H\u0002J\b\u0010l\u001a\u00020\u0014H\u0002J\r\u0010m\u001a\u00020\u0014H\u0000¢\u0006\u0002\bnJ\u0018\u0010o\u001a\u00020\u00142\u0006\u00104\u001a\u00020\u00072\u0006\u0010L\u001a\u00020(H\u0002J\u001c\u0010p\u001a\u00020\u00142\n\u00104\u001a\u0006\u0012\u0002\b\u0003012\u0006\u0010L\u001a\u00020(H\u0002J(\u0010p\u001a\u00020\u00142\n\u0010`\u001a\u0006\u0012\u0002\b\u00030\u000b2\n\u00104\u001a\u0006\u0012\u0002\b\u0003012\u0006\u0010L\u001a\u00020(H\u0002J4\u0010q\u001a\u00020\u0014*\u00020)2\u0012\u0010r\u001a\u000e\u0012\u0004\u0012\u00020)\u0012\u0004\u0012\u00020\b0s2\n\u0010t\u001a\u00060uj\u0002`v2\u0006\u0010w\u001a\u00020(H\u0002J\u0010\u0010x\u001a\u00020\u000f*\u0006\u0012\u0002\b\u00030\u000bH\u0002J\u0016\u0010`\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u000b*\u0006\u0012\u0002\b\u000301H\u0002J\u0013\u0010`\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u000b*\u00020\u0007H\u0082\u0010J\u000f\u0010y\u001a\u0004\u0018\u00010\u0007*\u00020\u0007H\u0082\u0010J\u0012\u0010z\u001a\u000205*\b\u0012\u0004\u0012\u00020\u00040:H\u0002J\f\u0010{\u001a\u00020(*\u00020\u0001H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\t\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u000b0\n8BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u001e\u0010\u000e\u001a\u0012\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u000b\u0012\u0004\u0012\u00020\u000f0\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u0012\u001a\u0010\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u0014\u0018\u00010\u0013X\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0015\u001a\u00020\u000fX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\u001a\u001a\u00020\u000fX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u0017\"\u0004\b\u001c\u0010\u0019R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u001f\u001a\u00020\u000f8F¢\u0006\u0006\u001a\u0004\b\u001f\u0010\u0017R\u001a\u0010 \u001a\u00020\u000fX\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b!\u0010\u0017\"\u0004\b\"\u0010\u0019R\u000e\u0010#\u001a\u00020$X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010%\u001a\u0004\u0018\u00010&X\u0082\u000e¢\u0006\u0002\n\u0000R\u001e\u0010'\u001a\u00020(*\u00020)8BX\u0082\u0004¢\u0006\f\u0012\u0004\b*\u0010+\u001a\u0004\b,\u0010-R\u0018\u0010.\u001a\u00020\u000f*\u00020\u00048BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b.\u0010/¨\u0006}"}, d2 = {"Lkotlinx/coroutines/debug/internal/DebugProbesImpl;", "", "()V", "ARTIFICIAL_FRAME", "Ljava/lang/StackTraceElement;", "callerInfoCache", "Lkotlinx/coroutines/debug/internal/ConcurrentWeakMap;", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "Lkotlinx/coroutines/debug/internal/DebugCoroutineInfoImpl;", "capturedCoroutines", "", "Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;", "getCapturedCoroutines", "()Ljava/util/Set;", "capturedCoroutinesMap", "", "dateFormat", "Ljava/text/SimpleDateFormat;", "dynamicAttach", "Lkotlin/Function1;", "", "enableCreationStackTraces", "getEnableCreationStackTraces$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "()Z", "setEnableCreationStackTraces$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "(Z)V", "ignoreCoroutinesWithEmptyContext", "getIgnoreCoroutinesWithEmptyContext", "setIgnoreCoroutinesWithEmptyContext", "installations", "Lkotlinx/atomicfu/AtomicInt;", "isInstalled", "sanitizeStackTraces", "getSanitizeStackTraces$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "setSanitizeStackTraces$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "sequenceNumber", "Lkotlinx/atomicfu/AtomicLong;", "weakRefCleanerThread", "Ljava/lang/Thread;", "debugString", "", "Lkotlinx/coroutines/Job;", "getDebugString$annotations", "(Lkotlinx/coroutines/Job;)V", "getDebugString", "(Lkotlinx/coroutines/Job;)Ljava/lang/String;", "isInternalMethod", "(Ljava/lang/StackTraceElement;)Z", "createOwner", "Lkotlin/coroutines/Continuation;", "T", "completion", "frame", "Lkotlinx/coroutines/debug/internal/StackTraceFrame;", "dumpCoroutines", "out", "Ljava/io/PrintStream;", "dumpCoroutinesInfo", "", "Lkotlinx/coroutines/debug/internal/DebugCoroutineInfo;", "dumpCoroutinesInfoAsJsonAndReferences", "", "()[Ljava/lang/Object;", "dumpCoroutinesInfoImpl", "R", "create", "Lkotlin/Function2;", "Lkotlin/coroutines/CoroutineContext;", "dumpCoroutinesSynchronized", "dumpDebuggerInfo", "Lkotlinx/coroutines/debug/internal/DebuggerInfo;", "enhanceStackTraceWithThreadDump", "info", "coroutineTrace", "enhanceStackTraceWithThreadDumpAsJson", "enhanceStackTraceWithThreadDumpImpl", "state", "thread", "findContinuationStartIndex", "Lkotlin/Pair;", "", "indexOfResumeWith", "actualTrace", "(I[Ljava/lang/StackTraceElement;Ljava/util/List;)Lkotlin/Pair;", "findIndexOfFrame", "frameIndex", "(I[Ljava/lang/StackTraceElement;Ljava/util/List;)I", "getDynamicAttach", "hierarchyToString", com.android.server.am.HostingRecord.TRIGGER_TYPE_JOB, "hierarchyToString$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "install", "install$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "printStackTrace", "frames", "probeCoroutineCompleted", "owner", "probeCoroutineCreated", "probeCoroutineCreated$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "probeCoroutineResumed", "probeCoroutineResumed$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "probeCoroutineSuspended", "probeCoroutineSuspended$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "sanitizeStackTrace", "", "throwable", "(Ljava/lang/Throwable;)Ljava/util/List;", "startWeakRefCleanerThread", "stopWeakRefCleanerThread", "uninstall", "uninstall$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "updateRunningState", "updateState", "build", "map", "", "builder", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "indent", "isFinished", "realCaller", "toStackTraceFrame", "toStringRepr", "CoroutineOwner", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class DebugProbesImpl {
    private static java.lang.Thread weakRefCleanerThread;
    public static final kotlinx.coroutines.debug.internal.DebugProbesImpl INSTANCE = new kotlinx.coroutines.debug.internal.DebugProbesImpl();
    private static final java.lang.StackTraceElement ARTIFICIAL_FRAME = new _COROUTINE.ArtificialStackFrames().coroutineCreation();
    private static final java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final kotlinx.coroutines.debug.internal.ConcurrentWeakMap<kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?>, java.lang.Boolean> capturedCoroutinesMap = new kotlinx.coroutines.debug.internal.ConcurrentWeakMap<>(false, 1, null);
    private static final kotlinx.atomicfu.AtomicInt installations = kotlinx.atomicfu.AtomicFU.atomic(0);
    private static final kotlinx.atomicfu.AtomicLong sequenceNumber = kotlinx.atomicfu.AtomicFU.atomic(0L);
    private static boolean sanitizeStackTraces = true;
    private static boolean enableCreationStackTraces = true;
    private static boolean ignoreCoroutinesWithEmptyContext = true;
    private static final kotlin.jvm.functions.Function1<java.lang.Boolean, kotlin.Unit> dynamicAttach = INSTANCE.getDynamicAttach();
    private static final kotlinx.coroutines.debug.internal.ConcurrentWeakMap<kotlin.coroutines.jvm.internal.CoroutineStackFrame, kotlinx.coroutines.debug.internal.DebugCoroutineInfo> callerInfoCache = new kotlinx.coroutines.debug.internal.ConcurrentWeakMap<>(true);

    private static /* synthetic */ void getDebugString$annotations(kotlinx.coroutines.Job job) {
    }

    private DebugProbesImpl() {
    }

    private final java.util.Set<kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?>> getCapturedCoroutines() {
        return capturedCoroutinesMap.keySet();
    }

    public final boolean isInstalled() {
        return installations.getValue() > 0;
    }

    public final boolean getSanitizeStackTraces$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return sanitizeStackTraces;
    }

    public final void setSanitizeStackTraces$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(boolean z) {
        sanitizeStackTraces = z;
    }

    public final boolean getEnableCreationStackTraces$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return enableCreationStackTraces;
    }

    public final void setEnableCreationStackTraces$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(boolean z) {
        enableCreationStackTraces = z;
    }

    public final boolean getIgnoreCoroutinesWithEmptyContext() {
        return ignoreCoroutinesWithEmptyContext;
    }

    public final void setIgnoreCoroutinesWithEmptyContext(boolean z) {
        ignoreCoroutinesWithEmptyContext = z;
    }

    private final kotlin.jvm.functions.Function1<java.lang.Boolean, kotlin.Unit> getDynamicAttach() {
        java.lang.Object objM11307constructorimpl;
        try {
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            kotlinx.coroutines.debug.internal.DebugProbesImpl debugProbesImpl = this;
            java.lang.Object objNewInstance = java.lang.Class.forName("kotlinx.coroutines.debug.internal.ByteBuddyDynamicAttach").getConstructors()[0].newInstance(new java.lang.Object[0]);
            kotlin.jvm.internal.Intrinsics.checkNotNull(objNewInstance, "null cannot be cast to non-null type kotlin.Function1<kotlin.Boolean, kotlin.Unit>");
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl((kotlin.jvm.functions.Function1) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(objNewInstance, 1));
        } catch (java.lang.Throwable th) {
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(th));
        }
        if (kotlin.Result.m11313isFailureimpl(objM11307constructorimpl)) {
            objM11307constructorimpl = null;
        }
        return (kotlin.jvm.functions.Function1) objM11307constructorimpl;
    }

    public final void install$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        kotlin.jvm.functions.Function1<java.lang.Boolean, kotlin.Unit> function1;
        if (installations.incrementAndGet() > 1) {
            return;
        }
        startWeakRefCleanerThread();
        if (!kotlinx.coroutines.debug.internal.AgentInstallationType.INSTANCE.isInstalledStatically$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() && (function1 = dynamicAttach) != null) {
            function1.invoke(true);
        }
    }

    public final void uninstall$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() throws java.lang.InterruptedException {
        kotlin.jvm.functions.Function1<java.lang.Boolean, kotlin.Unit> function1;
        if (!isInstalled()) {
            throw new java.lang.IllegalStateException("Agent was not installed".toString());
        }
        if (installations.decrementAndGet() != 0) {
            return;
        }
        stopWeakRefCleanerThread();
        capturedCoroutinesMap.clear();
        callerInfoCache.clear();
        if (!kotlinx.coroutines.debug.internal.AgentInstallationType.INSTANCE.isInstalledStatically$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() && (function1 = dynamicAttach) != null) {
            function1.invoke(false);
        }
    }

    private final void startWeakRefCleanerThread() {
        weakRefCleanerThread = kotlin.concurrent.ThreadsKt.thread((21 & 1) != 0, (21 & 2) != 0 ? false : true, (21 & 4) != 0 ? null : null, (21 & 8) != 0 ? null : "Coroutines Debugger Cleaner", (21 & 16) != 0 ? -1 : 0, new kotlin.jvm.functions.Function0<kotlin.Unit>() { // from class: kotlinx.coroutines.debug.internal.DebugProbesImpl.startWeakRefCleanerThread.1
            @Override // kotlin.jvm.functions.Function0
            public /* bridge */ /* synthetic */ kotlin.Unit invoke() {
                invoke2();
                return kotlin.Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2() {
                kotlinx.coroutines.debug.internal.DebugProbesImpl.callerInfoCache.runWeakRefQueueCleaningLoopUntilInterrupted();
            }
        });
    }

    private final void stopWeakRefCleanerThread() throws java.lang.InterruptedException {
        java.lang.Thread thread = weakRefCleanerThread;
        if (thread == null) {
            return;
        }
        weakRefCleanerThread = null;
        thread.interrupt();
        thread.join();
    }

    public final java.lang.String hierarchyToString$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(kotlinx.coroutines.Job job) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(job, "job");
        if (!isInstalled()) {
            throw new java.lang.IllegalStateException("Debug probes are not installed".toString());
        }
        java.lang.Iterable $this$filter$iv = getCapturedCoroutines();
        java.util.Collection destination$iv$iv = new java.util.ArrayList();
        for (java.lang.Object element$iv$iv : $this$filter$iv) {
            kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner it = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) element$iv$iv;
            if (it.delegate.getContext().get(kotlinx.coroutines.Job.INSTANCE) != null) {
                destination$iv$iv.add(element$iv$iv);
            }
        }
        java.lang.Iterable $this$associateBy$iv = (java.util.List) destination$iv$iv;
        int capacity$iv = kotlin.ranges.RangesKt.coerceAtLeast(kotlin.collections.MapsKt.mapCapacity(kotlin.collections.CollectionsKt.collectionSizeOrDefault($this$associateBy$iv, 10)), 16);
        java.util.Map destination$iv$iv2 = new java.util.LinkedHashMap(capacity$iv);
        for (java.lang.Object element$iv$iv2 : $this$associateBy$iv) {
            kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner it2 = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) element$iv$iv2;
            kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner it3 = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) element$iv$iv2;
            destination$iv$iv2.put(kotlinx.coroutines.JobKt.getJob(it2.delegate.getContext()), it3.info);
        }
        java.lang.StringBuilder $this$hierarchyToString_u24lambda_u246 = new java.lang.StringBuilder();
        INSTANCE.build(job, destination$iv$iv2, $this$hierarchyToString_u24lambda_u246, "");
        java.lang.String string = $this$hierarchyToString_u24lambda_u246.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    private final void build(kotlinx.coroutines.Job $this$build, java.util.Map<kotlinx.coroutines.Job, kotlinx.coroutines.debug.internal.DebugCoroutineInfo> map, java.lang.StringBuilder builder, java.lang.String indent) {
        java.lang.String newIndent;
        kotlinx.coroutines.debug.internal.DebugCoroutineInfo info = map.get($this$build);
        if (info == null) {
            if (!($this$build instanceof kotlinx.coroutines.internal.ScopeCoroutine)) {
                builder.append(indent + getDebugString($this$build) + "\n");
                newIndent = indent + "\t";
            } else {
                newIndent = indent;
            }
        } else {
            java.lang.StackTraceElement element = (java.lang.StackTraceElement) kotlin.collections.CollectionsKt.firstOrNull((java.util.List) info.lastObservedStackTrace$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host());
            java.lang.String state = info.get_state();
            builder.append(indent + getDebugString($this$build) + ", continuation is " + state + " at line " + element + "\n");
            newIndent = indent + "\t";
        }
        for (kotlinx.coroutines.Job child : $this$build.getChildren()) {
            build(child, map, builder, newIndent);
        }
    }

    private final java.lang.String getDebugString(kotlinx.coroutines.Job $this$debugString) {
        return $this$debugString instanceof kotlinx.coroutines.JobSupport ? ((kotlinx.coroutines.JobSupport) $this$debugString).toDebugString() : $this$debugString.toString();
    }

    private final <R> java.util.List<R> dumpCoroutinesInfoImpl(final kotlin.jvm.functions.Function2<? super kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?>, ? super kotlin.coroutines.CoroutineContext, ? extends R> create) {
        if (!isInstalled()) {
            throw new java.lang.IllegalStateException("Debug probes are not installed".toString());
        }
        kotlin.sequences.Sequence $this$sortedBy$iv = kotlin.collections.CollectionsKt.asSequence(getCapturedCoroutines());
        return kotlin.sequences.SequencesKt.toList(kotlin.sequences.SequencesKt.mapNotNull(kotlin.sequences.SequencesKt.sortedWith($this$sortedBy$iv, new kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$$inlined$sortedBy$1()), new kotlin.jvm.functions.Function1<kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?>, R>() { // from class: kotlinx.coroutines.debug.internal.DebugProbesImpl.dumpCoroutinesInfoImpl.3
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public final R invoke(kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?> owner) {
                kotlin.coroutines.CoroutineContext context;
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(owner, "owner");
                if (kotlinx.coroutines.debug.internal.DebugProbesImpl.INSTANCE.isFinished(owner) || (context = owner.info.getContext()) == null) {
                    return null;
                }
                return create.invoke(owner, context);
            }
        }));
    }

    public final java.lang.Object[] dumpCoroutinesInfoAsJsonAndReferences() {
        java.lang.String name;
        kotlinx.coroutines.debug.internal.DebugProbesImpl debugProbesImpl = this;
        java.util.List<kotlinx.coroutines.debug.internal.DebugCoroutineInfo> listDumpCoroutinesInfo = dumpCoroutinesInfo();
        int size = listDumpCoroutinesInfo.size();
        java.util.ArrayList lastObservedThreads = new java.util.ArrayList(size);
        java.util.ArrayList lastObservedFrames = new java.util.ArrayList(size);
        java.util.ArrayList coroutinesInfoAsJson = new java.util.ArrayList(size);
        for (kotlinx.coroutines.debug.internal.DebugCoroutineInfo info : listDumpCoroutinesInfo) {
            kotlin.coroutines.CoroutineContext context = info.getContext();
            kotlinx.coroutines.CoroutineName coroutineName = (kotlinx.coroutines.CoroutineName) context.get(kotlinx.coroutines.CoroutineName.INSTANCE);
            java.lang.Long lValueOf = null;
            java.lang.String name2 = (coroutineName == null || (name = coroutineName.getName()) == null) ? null : debugProbesImpl.toStringRepr(name);
            kotlinx.coroutines.CoroutineDispatcher coroutineDispatcher = (kotlinx.coroutines.CoroutineDispatcher) context.get(kotlinx.coroutines.CoroutineDispatcher.INSTANCE);
            java.lang.String dispatcher = coroutineDispatcher != null ? debugProbesImpl.toStringRepr(coroutineDispatcher) : null;
            kotlinx.coroutines.CoroutineId coroutineId = (kotlinx.coroutines.CoroutineId) context.get(kotlinx.coroutines.CoroutineId.INSTANCE);
            if (coroutineId != null) {
                lValueOf = java.lang.Long.valueOf(coroutineId.getId());
            }
            coroutinesInfoAsJson.add(kotlin.text.StringsKt.trimIndent("\n                {\n                    \"name\": " + name2 + ",\n                    \"id\": " + lValueOf + ",\n                    \"dispatcher\": " + dispatcher + ",\n                    \"sequenceNumber\": " + info.getSequenceNumber() + ",\n                    \"state\": \"" + info.getState() + "\"\n                } \n                "));
            lastObservedFrames.add(info.getLastObservedFrame());
            lastObservedThreads.add(info.getLastObservedThread());
            debugProbesImpl = this;
        }
        java.util.ArrayList $this$toTypedArray$iv = lastObservedThreads;
        java.util.ArrayList $this$toTypedArray$iv2 = lastObservedFrames;
        java.util.List<kotlinx.coroutines.debug.internal.DebugCoroutineInfo> $this$toTypedArray$iv3 = listDumpCoroutinesInfo;
        return new java.lang.Object[]{"[" + kotlin.collections.CollectionsKt.joinToString$default(coroutinesInfoAsJson, null, null, null, 0, null, null, 63, null) + "]", $this$toTypedArray$iv.toArray(new java.lang.Thread[0]), $this$toTypedArray$iv2.toArray(new kotlin.coroutines.jvm.internal.CoroutineStackFrame[0]), $this$toTypedArray$iv3.toArray(new kotlinx.coroutines.debug.internal.DebugCoroutineInfo[0])};
    }

    public final java.lang.String enhanceStackTraceWithThreadDumpAsJson(kotlinx.coroutines.debug.internal.DebugCoroutineInfo info) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(info, "info");
        java.util.List<java.lang.StackTraceElement> listEnhanceStackTraceWithThreadDump = enhanceStackTraceWithThreadDump(info, info.lastObservedStackTrace());
        java.util.List stackTraceElementsInfoAsJson = new java.util.ArrayList();
        for (java.lang.StackTraceElement element : listEnhanceStackTraceWithThreadDump) {
            java.lang.String className = element.getClassName();
            java.lang.String methodName = element.getMethodName();
            java.lang.String fileName = element.getFileName();
            stackTraceElementsInfoAsJson.add(kotlin.text.StringsKt.trimIndent("\n                {\n                    \"declaringClass\": \"" + className + "\",\n                    \"methodName\": \"" + methodName + "\",\n                    \"fileName\": " + (fileName != null ? toStringRepr(fileName) : null) + ",\n                    \"lineNumber\": " + element.getLineNumber() + "\n                }\n                "));
        }
        return "[" + kotlin.collections.CollectionsKt.joinToString$default(stackTraceElementsInfoAsJson, null, null, null, 0, null, null, 63, null) + "]";
    }

    private final java.lang.String toStringRepr(java.lang.Object $this$toStringRepr) {
        return kotlinx.coroutines.debug.internal.DebugProbesImplKt.repr($this$toStringRepr.toString());
    }

    public final java.util.List<kotlinx.coroutines.debug.internal.DebugCoroutineInfo> dumpCoroutinesInfo() {
        if (!isInstalled()) {
            throw new java.lang.IllegalStateException("Debug probes are not installed".toString());
        }
        kotlin.sequences.Sequence $this$sortedBy$iv$iv = kotlin.collections.CollectionsKt.asSequence(getCapturedCoroutines());
        return kotlin.sequences.SequencesKt.toList(kotlin.sequences.SequencesKt.mapNotNull(kotlin.sequences.SequencesKt.sortedWith($this$sortedBy$iv$iv, new kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$$inlined$sortedBy$1()), new kotlin.jvm.functions.Function1<kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?>, kotlinx.coroutines.debug.internal.DebugCoroutineInfo>() { // from class: kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfo$$inlined$dumpCoroutinesInfoImpl$1
            @Override // kotlin.jvm.functions.Function1
            public final kotlinx.coroutines.debug.internal.DebugCoroutineInfo invoke(kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?> owner) {
                kotlin.coroutines.CoroutineContext context;
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(owner, "owner");
                if (kotlinx.coroutines.debug.internal.DebugProbesImpl.INSTANCE.isFinished(owner) || (context = owner.info.getContext()) == null) {
                    return null;
                }
                return new kotlinx.coroutines.debug.internal.DebugCoroutineInfo(owner.info, context);
            }
        }));
    }

    public final java.util.List<kotlinx.coroutines.debug.internal.DebuggerInfo> dumpDebuggerInfo() {
        if (!isInstalled()) {
            throw new java.lang.IllegalStateException("Debug probes are not installed".toString());
        }
        kotlin.sequences.Sequence $this$sortedBy$iv$iv = kotlin.collections.CollectionsKt.asSequence(getCapturedCoroutines());
        return kotlin.sequences.SequencesKt.toList(kotlin.sequences.SequencesKt.mapNotNull(kotlin.sequences.SequencesKt.sortedWith($this$sortedBy$iv$iv, new kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesInfoImpl$$inlined$sortedBy$1()), new kotlin.jvm.functions.Function1<kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?>, kotlinx.coroutines.debug.internal.DebuggerInfo>() { // from class: kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpDebuggerInfo$$inlined$dumpCoroutinesInfoImpl$1
            @Override // kotlin.jvm.functions.Function1
            public final kotlinx.coroutines.debug.internal.DebuggerInfo invoke(kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?> owner) {
                kotlin.coroutines.CoroutineContext context;
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(owner, "owner");
                if (kotlinx.coroutines.debug.internal.DebugProbesImpl.INSTANCE.isFinished(owner) || (context = owner.info.getContext()) == null) {
                    return null;
                }
                return new kotlinx.coroutines.debug.internal.DebuggerInfo(owner.info, context);
            }
        }));
    }

    public final void dumpCoroutines(java.io.PrintStream out) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(out, "out");
        synchronized (out) {
            INSTANCE.dumpCoroutinesSynchronized(out);
            kotlin.Unit unit = kotlin.Unit.INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isFinished(kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?> coroutineOwner) {
        kotlinx.coroutines.Job job;
        kotlin.coroutines.CoroutineContext context = coroutineOwner.info.getContext();
        if (context == null || (job = (kotlinx.coroutines.Job) context.get(kotlinx.coroutines.Job.INSTANCE)) == null || !job.isCompleted()) {
            return false;
        }
        capturedCoroutinesMap.remove(coroutineOwner);
        return true;
    }

    private final void dumpCoroutinesSynchronized(java.io.PrintStream out) {
        if (!isInstalled()) {
            throw new java.lang.IllegalStateException("Debug probes are not installed".toString());
        }
        out.print("Coroutines dump " + dateFormat.format(java.lang.Long.valueOf(java.lang.System.currentTimeMillis())));
        kotlin.sequences.Sequence $this$sortedBy$iv = kotlin.sequences.SequencesKt.filter(kotlin.collections.CollectionsKt.asSequence(getCapturedCoroutines()), new kotlin.jvm.functions.Function1<kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?>, java.lang.Boolean>() { // from class: kotlinx.coroutines.debug.internal.DebugProbesImpl.dumpCoroutinesSynchronized.2
            @Override // kotlin.jvm.functions.Function1
            public final java.lang.Boolean invoke(kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?> it) {
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
                return java.lang.Boolean.valueOf(!kotlinx.coroutines.debug.internal.DebugProbesImpl.INSTANCE.isFinished(it));
            }
        });
        for (java.lang.Object element$iv : kotlin.sequences.SequencesKt.sortedWith($this$sortedBy$iv, new java.util.Comparator() { // from class: kotlinx.coroutines.debug.internal.DebugProbesImpl$dumpCoroutinesSynchronized$$inlined$sortedBy$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner it = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) t;
                kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner it2 = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) t2;
                return kotlin.comparisons.ComparisonsKt.compareValues(java.lang.Long.valueOf(it.info.sequenceNumber), java.lang.Long.valueOf(it2.info.sequenceNumber));
            }
        })) {
            kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner owner = (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) element$iv;
            kotlinx.coroutines.debug.internal.DebugCoroutineInfo info = owner.info;
            java.util.List<java.lang.StackTraceElement> listLastObservedStackTrace$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = info.lastObservedStackTrace$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
            java.util.List<java.lang.StackTraceElement> listEnhanceStackTraceWithThreadDumpImpl = INSTANCE.enhanceStackTraceWithThreadDumpImpl(info.get_state(), info.lastObservedThread, listLastObservedStackTrace$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host);
            java.lang.String state = (kotlin.jvm.internal.Intrinsics.areEqual(info.get_state(), kotlinx.coroutines.debug.internal.DebugCoroutineInfoImplKt.RUNNING) && listEnhanceStackTraceWithThreadDumpImpl == listLastObservedStackTrace$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host) ? info.get_state() + " (Last suspension stacktrace, not an actual stacktrace)" : info.get_state();
            out.print("\n\nCoroutine " + owner.delegate + ", state: " + state);
            if (listLastObservedStackTrace$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host.isEmpty()) {
                out.print("\n\tat " + ARTIFICIAL_FRAME);
                INSTANCE.printStackTrace(out, info.getCreationStackTrace());
            } else {
                INSTANCE.printStackTrace(out, listEnhanceStackTraceWithThreadDumpImpl);
            }
        }
    }

    private final void printStackTrace(java.io.PrintStream out, java.util.List<java.lang.StackTraceElement> frames) {
        java.util.List<java.lang.StackTraceElement> $this$forEach$iv = frames;
        for (java.lang.Object element$iv : $this$forEach$iv) {
            java.lang.StackTraceElement frame = (java.lang.StackTraceElement) element$iv;
            out.print("\n\tat " + frame);
        }
    }

    public final java.util.List<java.lang.StackTraceElement> enhanceStackTraceWithThreadDump(kotlinx.coroutines.debug.internal.DebugCoroutineInfo info, java.util.List<java.lang.StackTraceElement> coroutineTrace) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(info, "info");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(coroutineTrace, "coroutineTrace");
        return enhanceStackTraceWithThreadDumpImpl(info.getState(), info.getLastObservedThread(), coroutineTrace);
    }

    private final java.util.List<java.lang.StackTraceElement> enhanceStackTraceWithThreadDumpImpl(java.lang.String state, java.lang.Thread thread, java.util.List<java.lang.StackTraceElement> coroutineTrace) {
        java.lang.Object objM11307constructorimpl;
        if (!kotlin.jvm.internal.Intrinsics.areEqual(state, kotlinx.coroutines.debug.internal.DebugCoroutineInfoImplKt.RUNNING) || thread == null) {
            return coroutineTrace;
        }
        try {
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            kotlinx.coroutines.debug.internal.DebugProbesImpl debugProbesImpl = this;
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl(thread.getStackTrace());
        } catch (java.lang.Throwable th) {
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(th));
        }
        if (kotlin.Result.m11313isFailureimpl(objM11307constructorimpl)) {
            objM11307constructorimpl = null;
        }
        java.lang.StackTraceElement[] actualTrace = (java.lang.StackTraceElement[]) objM11307constructorimpl;
        if (actualTrace == null) {
            return coroutineTrace;
        }
        int index$iv = 0;
        int length = actualTrace.length;
        while (true) {
            if (index$iv < length) {
                java.lang.StackTraceElement it = actualTrace[index$iv];
                if (kotlin.jvm.internal.Intrinsics.areEqual(it.getClassName(), "kotlin.coroutines.jvm.internal.BaseContinuationImpl") && kotlin.jvm.internal.Intrinsics.areEqual(it.getMethodName(), "resumeWith") && kotlin.jvm.internal.Intrinsics.areEqual(it.getFileName(), "ContinuationImpl.kt")) {
                    break;
                }
                index$iv++;
            } else {
                index$iv = -1;
                break;
            }
        }
        int indexOfResumeWith = index$iv;
        kotlin.Pair<java.lang.Integer, java.lang.Integer> pairFindContinuationStartIndex = findContinuationStartIndex(indexOfResumeWith, actualTrace, coroutineTrace);
        int continuationStartFrame = pairFindContinuationStartIndex.component1().intValue();
        int delta = pairFindContinuationStartIndex.component2().intValue();
        if (continuationStartFrame == -1) {
            return coroutineTrace;
        }
        int expectedSize = (((coroutineTrace.size() + indexOfResumeWith) - continuationStartFrame) - 1) - delta;
        java.util.ArrayList result = new java.util.ArrayList(expectedSize);
        int i = indexOfResumeWith - delta;
        for (int index = 0; index < i; index++) {
            result.add(actualTrace[index]);
        }
        int size = coroutineTrace.size();
        for (int index2 = continuationStartFrame + 1; index2 < size; index2++) {
            result.add(coroutineTrace.get(index2));
        }
        return result;
    }

    private final kotlin.Pair<java.lang.Integer, java.lang.Integer> findContinuationStartIndex(int indexOfResumeWith, java.lang.StackTraceElement[] actualTrace, java.util.List<java.lang.StackTraceElement> coroutineTrace) {
        for (int i = 0; i < 3; i++) {
            int it = i;
            int result = INSTANCE.findIndexOfFrame((indexOfResumeWith - 1) - it, actualTrace, coroutineTrace);
            if (result != -1) {
                return kotlin.TuplesKt.to(java.lang.Integer.valueOf(result), java.lang.Integer.valueOf(it));
            }
        }
        return kotlin.TuplesKt.to(-1, 0);
    }

    private final int findIndexOfFrame(int frameIndex, java.lang.StackTraceElement[] actualTrace, java.util.List<java.lang.StackTraceElement> coroutineTrace) {
        java.lang.StackTraceElement continuationFrame = (java.lang.StackTraceElement) kotlin.collections.ArraysKt.getOrNull(actualTrace, frameIndex);
        if (continuationFrame == null) {
            return -1;
        }
        int index$iv = 0;
        for (java.lang.Object item$iv : coroutineTrace) {
            java.lang.StackTraceElement it = (java.lang.StackTraceElement) item$iv;
            if (kotlin.jvm.internal.Intrinsics.areEqual(it.getFileName(), continuationFrame.getFileName()) && kotlin.jvm.internal.Intrinsics.areEqual(it.getClassName(), continuationFrame.getClassName()) && kotlin.jvm.internal.Intrinsics.areEqual(it.getMethodName(), continuationFrame.getMethodName())) {
                return index$iv;
            }
            index$iv++;
        }
        return -1;
    }

    public final void probeCoroutineResumed$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(kotlin.coroutines.Continuation<?> frame) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(frame, "frame");
        updateState(frame, kotlinx.coroutines.debug.internal.DebugCoroutineInfoImplKt.RUNNING);
    }

    public final void probeCoroutineSuspended$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(kotlin.coroutines.Continuation<?> frame) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(frame, "frame");
        updateState(frame, kotlinx.coroutines.debug.internal.DebugCoroutineInfoImplKt.SUSPENDED);
    }

    private final void updateState(kotlin.coroutines.Continuation<?> frame, java.lang.String state) {
        if (isInstalled()) {
            if (ignoreCoroutinesWithEmptyContext && frame.getContext() == kotlin.coroutines.EmptyCoroutineContext.INSTANCE) {
                return;
            }
            if (kotlin.jvm.internal.Intrinsics.areEqual(state, kotlinx.coroutines.debug.internal.DebugCoroutineInfoImplKt.RUNNING)) {
                kotlin.coroutines.jvm.internal.CoroutineStackFrame stackFrame = frame instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame ? (kotlin.coroutines.jvm.internal.CoroutineStackFrame) frame : null;
                if (stackFrame == null) {
                    return;
                }
                updateRunningState(stackFrame, state);
                return;
            }
            kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?> coroutineOwnerOwner = owner(frame);
            if (coroutineOwnerOwner == null) {
                return;
            }
            updateState(coroutineOwnerOwner, frame, state);
        }
    }

    private final void updateRunningState(kotlin.coroutines.jvm.internal.CoroutineStackFrame frame, java.lang.String state) {
        kotlinx.coroutines.debug.internal.DebugCoroutineInfo debugCoroutineInfo;
        kotlinx.coroutines.debug.internal.DebugCoroutineInfo info;
        boolean shouldBeMatchedWithProbeSuspended;
        if (isInstalled()) {
            kotlinx.coroutines.debug.internal.DebugCoroutineInfo cached = callerInfoCache.remove(frame);
            if (cached != null) {
                info = cached;
                shouldBeMatchedWithProbeSuspended = false;
            } else {
                kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?> coroutineOwnerOwner = owner(frame);
                if (coroutineOwnerOwner == null || (debugCoroutineInfo = coroutineOwnerOwner.info) == null) {
                    return;
                }
                info = debugCoroutineInfo;
                shouldBeMatchedWithProbeSuspended = true;
                kotlin.coroutines.jvm.internal.CoroutineStackFrame lastObservedFrame$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = info.getLastObservedFrame$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
                kotlin.coroutines.jvm.internal.CoroutineStackFrame realCaller = lastObservedFrame$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host != null ? realCaller(lastObservedFrame$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host) : null;
                if (realCaller != null) {
                    callerInfoCache.remove(realCaller);
                }
            }
            kotlin.jvm.internal.Intrinsics.checkNotNull(frame, "null cannot be cast to non-null type kotlin.coroutines.Continuation<*>");
            info.updateState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(state, (kotlin.coroutines.Continuation) frame, shouldBeMatchedWithProbeSuspended);
            kotlin.coroutines.jvm.internal.CoroutineStackFrame caller = realCaller(frame);
            if (caller == null) {
                return;
            }
            callerInfoCache.put(caller, info);
        }
    }

    private final kotlin.coroutines.jvm.internal.CoroutineStackFrame realCaller(kotlin.coroutines.jvm.internal.CoroutineStackFrame $this$realCaller) {
        while (true) {
            kotlin.coroutines.jvm.internal.CoroutineStackFrame caller = $this$realCaller.getCallerFrame();
            if (caller == null) {
                return null;
            }
            if (caller.getStackTraceElement() != null) {
                return caller;
            }
            $this$realCaller = caller;
        }
    }

    private final void updateState(kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?> owner, kotlin.coroutines.Continuation<?> frame, java.lang.String state) {
        if (isInstalled()) {
            owner.info.updateState$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(state, frame, true);
        }
    }

    private final kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?> owner(kotlin.coroutines.Continuation<?> continuation) {
        kotlin.coroutines.jvm.internal.CoroutineStackFrame coroutineStackFrame = continuation instanceof kotlin.coroutines.jvm.internal.CoroutineStackFrame ? (kotlin.coroutines.jvm.internal.CoroutineStackFrame) continuation : null;
        if (coroutineStackFrame != null) {
            return owner(coroutineStackFrame);
        }
        return null;
    }

    private final kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?> owner(kotlin.coroutines.jvm.internal.CoroutineStackFrame $this$owner) {
        while (!($this$owner instanceof kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner)) {
            kotlin.coroutines.jvm.internal.CoroutineStackFrame callerFrame = $this$owner.getCallerFrame();
            if (callerFrame == null) {
                return null;
            }
            $this$owner = callerFrame;
        }
        return (kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner) $this$owner;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <T> kotlin.coroutines.Continuation<T> probeCoroutineCreated$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(kotlin.coroutines.Continuation<? super T> completion) {
        kotlinx.coroutines.debug.internal.StackTraceFrame frame;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(completion, "completion");
        if (!isInstalled()) {
            return completion;
        }
        if ((ignoreCoroutinesWithEmptyContext && completion.getContext() == kotlin.coroutines.EmptyCoroutineContext.INSTANCE) || owner(completion) != null) {
            return completion;
        }
        if (enableCreationStackTraces) {
            frame = toStackTraceFrame(sanitizeStackTrace(new java.lang.Exception()));
        } else {
            frame = null;
        }
        return createOwner(completion, frame);
    }

    private final kotlinx.coroutines.debug.internal.StackTraceFrame toStackTraceFrame(java.util.List<java.lang.StackTraceElement> list) {
        java.lang.Object accumulator$iv = null;
        if (!list.isEmpty()) {
            java.util.ListIterator<java.lang.StackTraceElement> listIterator = list.listIterator(list.size());
            while (listIterator.hasPrevious()) {
                java.lang.StackTraceElement frame = listIterator.previous();
                java.lang.Object acc = accumulator$iv;
                accumulator$iv = new kotlinx.coroutines.debug.internal.StackTraceFrame((kotlin.coroutines.jvm.internal.CoroutineStackFrame) acc, frame);
            }
        }
        java.lang.Object initial$iv = accumulator$iv;
        return new kotlinx.coroutines.debug.internal.StackTraceFrame((kotlin.coroutines.jvm.internal.CoroutineStackFrame) initial$iv, ARTIFICIAL_FRAME);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private final <T> kotlin.coroutines.Continuation<T> createOwner(kotlin.coroutines.Continuation<? super T> completion, kotlinx.coroutines.debug.internal.StackTraceFrame frame) {
        if (!isInstalled()) {
            return completion;
        }
        kotlinx.coroutines.debug.internal.DebugCoroutineInfo info = new kotlinx.coroutines.debug.internal.DebugCoroutineInfo(completion.getContext(), frame, sequenceNumber.incrementAndGet());
        kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?> coroutineOwner = new kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<>(completion, info);
        capturedCoroutinesMap.put(coroutineOwner, true);
        if (!isInstalled()) {
            capturedCoroutinesMap.clear();
        }
        return coroutineOwner;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void probeCoroutineCompleted(kotlinx.coroutines.debug.internal.DebugProbesImpl.CoroutineOwner<?> owner) {
        kotlin.coroutines.jvm.internal.CoroutineStackFrame caller;
        capturedCoroutinesMap.remove(owner);
        kotlin.coroutines.jvm.internal.CoroutineStackFrame lastObservedFrame$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host = owner.info.getLastObservedFrame$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        if (lastObservedFrame$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host == null || (caller = realCaller(lastObservedFrame$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host)) == null) {
            return;
        }
        callerInfoCache.remove(caller);
    }

    /* JADX INFO: compiled from: DebugProbesImpl.kt */
    @kotlin.Metadata(d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00020\u0003B\u001d\b\u0000\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\n\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016J\u001b\u0010\u0015\u001a\u00020\u00162\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u00028\u00000\u0018H\u0016¢\u0006\u0002\u0010\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0016R\u0016\u0010\b\u001a\u0004\u0018\u00010\u00038VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0012\u0010\u000b\u001a\u00020\fX\u0096\u0005¢\u0006\u0006\u001a\u0004\b\r\u0010\u000eR\u0016\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u00028\u0000X\u0081\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\u0004\u0018\u00010\u00108BX\u0082\u0004¢\u0006\u0006\u001a\u0004\b\u0011\u0010\u0012R\u0010\u0010\u0005\u001a\u00020\u00068\u0006X\u0087\u0004¢\u0006\u0002\n\u0000¨\u0006\u001c"}, d2 = {"Lkotlinx/coroutines/debug/internal/DebugProbesImpl$CoroutineOwner;", "T", "Lkotlin/coroutines/Continuation;", "Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "delegate", "info", "Lkotlinx/coroutines/debug/internal/DebugCoroutineInfoImpl;", "(Lkotlin/coroutines/Continuation;Lkotlinx/coroutines/debug/internal/DebugCoroutineInfoImpl;)V", "callerFrame", "getCallerFrame", "()Lkotlin/coroutines/jvm/internal/CoroutineStackFrame;", "context", "Lkotlin/coroutines/CoroutineContext;", "getContext", "()Lkotlin/coroutines/CoroutineContext;", "frame", "Lkotlinx/coroutines/debug/internal/StackTraceFrame;", "getFrame", "()Lkotlinx/coroutines/debug/internal/StackTraceFrame;", "getStackTraceElement", "Ljava/lang/StackTraceElement;", "resumeWith", "", "result", "Lkotlin/Result;", "(Ljava/lang/Object;)V", "toString", "", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class CoroutineOwner<T> implements kotlin.coroutines.Continuation<T>, kotlin.coroutines.jvm.internal.CoroutineStackFrame {
        public final kotlin.coroutines.Continuation<T> delegate;
        public final kotlinx.coroutines.debug.internal.DebugCoroutineInfo info;

        @Override // kotlin.coroutines.Continuation
        public kotlin.coroutines.CoroutineContext getContext() {
            return this.delegate.getContext();
        }

        /* JADX WARN: Multi-variable type inference failed */
        public CoroutineOwner(kotlin.coroutines.Continuation<? super T> delegate, kotlinx.coroutines.debug.internal.DebugCoroutineInfo info) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(delegate, "delegate");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(info, "info");
            this.delegate = delegate;
            this.info = info;
        }

        private final kotlinx.coroutines.debug.internal.StackTraceFrame getFrame() {
            return this.info.getCreationStackBottom();
        }

        @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
        public kotlin.coroutines.jvm.internal.CoroutineStackFrame getCallerFrame() {
            kotlinx.coroutines.debug.internal.StackTraceFrame frame = getFrame();
            if (frame != null) {
                return frame.getCallerFrame();
            }
            return null;
        }

        @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
        public java.lang.StackTraceElement getStackTraceElement() {
            kotlinx.coroutines.debug.internal.StackTraceFrame frame = getFrame();
            if (frame != null) {
                return frame.getStackTraceElement();
            }
            return null;
        }

        @Override // kotlin.coroutines.Continuation
        public void resumeWith(java.lang.Object result) {
            kotlinx.coroutines.debug.internal.DebugProbesImpl.INSTANCE.probeCoroutineCompleted(this);
            this.delegate.resumeWith(result);
        }

        public java.lang.String toString() {
            return this.delegate.toString();
        }
    }

    private final <T extends java.lang.Throwable> java.util.List<java.lang.StackTraceElement> sanitizeStackTrace(T throwable) {
        java.lang.StackTraceElement[] stackTrace = throwable.getStackTrace();
        int size = stackTrace.length;
        kotlin.jvm.internal.Intrinsics.checkNotNull(stackTrace);
        int i = -1;
        int length = stackTrace.length - 1;
        if (length >= 0) {
            while (true) {
                int index$iv = length;
                length--;
                java.lang.StackTraceElement it = stackTrace[index$iv];
                if (!kotlin.jvm.internal.Intrinsics.areEqual(it.getClassName(), "kotlin.coroutines.jvm.internal.DebugProbesKt")) {
                    if (length < 0) {
                        break;
                    }
                } else {
                    i = index$iv;
                    break;
                }
            }
        }
        int traceStart = i + 1;
        if (!sanitizeStackTraces) {
            int i2 = size - traceStart;
            java.util.ArrayList arrayList = new java.util.ArrayList(i2);
            for (int i3 = 0; i3 < i2; i3++) {
                int it2 = i3;
                arrayList.add(stackTrace[it2 + traceStart]);
            }
            return arrayList;
        }
        java.util.ArrayList result = new java.util.ArrayList((size - traceStart) + 1);
        int i4 = traceStart;
        while (i4 < size) {
            java.lang.StackTraceElement stackTraceElement = stackTrace[i4];
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(stackTraceElement, "get(...)");
            if (isInternalMethod(stackTraceElement)) {
                result.add(stackTrace[i4]);
                int j = i4 + 1;
                while (j < size) {
                    java.lang.StackTraceElement stackTraceElement2 = stackTrace[j];
                    kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(stackTraceElement2, "get(...)");
                    if (!isInternalMethod(stackTraceElement2)) {
                        break;
                    }
                    j++;
                }
                int k = j - 1;
                while (k > i4 && stackTrace[k].getFileName() == null) {
                    k--;
                }
                if (k > i4 && k < j - 1) {
                    result.add(stackTrace[k]);
                }
                result.add(stackTrace[j - 1]);
                i4 = j;
            } else {
                result.add(stackTrace[i4]);
                i4++;
            }
        }
        return result;
    }

    private final boolean isInternalMethod(java.lang.StackTraceElement $this$isInternalMethod) {
        java.lang.String className = $this$isInternalMethod.getClassName();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(className, "getClassName(...)");
        return kotlin.text.StringsKt.startsWith$default(className, "kotlinx.coroutines", false, 2, (java.lang.Object) null);
    }
}
