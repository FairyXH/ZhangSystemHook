package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class PerfettoTransitionTracer implements com.android.server.wm.TransitionTracer {
    private final java.util.concurrent.atomic.AtomicInteger mActiveTraces = new java.util.concurrent.atomic.AtomicInteger(0);
    private final android.tracing.transition.TransitionDataSource mDataSource;

    static /* synthetic */ void lambda$new$0() {
    }

    PerfettoTransitionTracer() {
        final java.util.concurrent.atomic.AtomicInteger atomicInteger = this.mActiveTraces;
        java.util.Objects.requireNonNull(atomicInteger);
        java.lang.Runnable runnable = new java.lang.Runnable() { // from class: com.android.server.wm.PerfettoTransitionTracer$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                atomicInteger.incrementAndGet();
            }
        };
        java.lang.Runnable runnable2 = new java.lang.Runnable() { // from class: com.android.server.wm.PerfettoTransitionTracer$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.wm.PerfettoTransitionTracer.lambda$new$0();
            }
        };
        final java.util.concurrent.atomic.AtomicInteger atomicInteger2 = this.mActiveTraces;
        java.util.Objects.requireNonNull(atomicInteger2);
        this.mDataSource = new android.tracing.transition.TransitionDataSource(runnable, runnable2, new java.lang.Runnable() { // from class: com.android.server.wm.PerfettoTransitionTracer$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                atomicInteger2.decrementAndGet();
            }
        });
        android.tracing.perfetto.Producer.init(android.tracing.perfetto.InitArguments.DEFAULTS);
        android.tracing.perfetto.DataSourceParams params = new android.tracing.perfetto.DataSourceParams.Builder().setBufferExhaustedPolicy(0).build();
        this.mDataSource.register(params);
    }

    @Override // com.android.server.wm.TransitionTracer
    public void logSentTransition(com.android.server.wm.Transition transition, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> targets) {
        if (!isTracing()) {
            return;
        }
        android.os.Trace.traceBegin(32L, "logSentTransition");
        try {
            doLogSentTransition(transition, targets);
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    private void doLogSentTransition(final com.android.server.wm.Transition transition, final java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> targets) {
        this.mDataSource.trace(new android.tracing.perfetto.TraceFunction() { // from class: com.android.server.wm.PerfettoTransitionTracer$$ExternalSyntheticLambda3
            public final void trace(android.tracing.perfetto.TracingContext tracingContext) {
                this.f$0.lambda$doLogSentTransition$1(transition, targets, tracingContext);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$doLogSentTransition$1(com.android.server.wm.Transition transition, java.util.ArrayList targets, android.tracing.perfetto.TracingContext ctx) {
        android.util.proto.ProtoOutputStream os = ctx.newTracePacket();
        long token = os.start(1146756268128L);
        os.write(1120986464257L, transition.getSyncId());
        os.write(1112396529666L, transition.mLogger.mCreateTimeNs);
        os.write(1112396529667L, transition.mLogger.mSendTimeNs);
        os.write(1116691496970L, transition.getStartTransaction().getId());
        os.write(1116691496971L, transition.getFinishTransaction().getId());
        os.write(1120986464269L, transition.mType);
        os.write(1120986464272L, transition.getFlags());
        addTransitionTargetsToProto(os, targets);
        os.end(token);
    }

    @Override // com.android.server.wm.TransitionTracer
    public void logFinishedTransition(com.android.server.wm.Transition transition) {
        if (!isTracing()) {
            return;
        }
        android.os.Trace.traceBegin(32L, "logFinishedTransition");
        try {
            doLogFinishTransition(transition);
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    private void doLogFinishTransition(final com.android.server.wm.Transition transition) {
        this.mDataSource.trace(new android.tracing.perfetto.TraceFunction() { // from class: com.android.server.wm.PerfettoTransitionTracer$$ExternalSyntheticLambda4
            public final void trace(android.tracing.perfetto.TracingContext tracingContext) {
                com.android.server.wm.PerfettoTransitionTracer.lambda$doLogFinishTransition$2(transition, tracingContext);
            }
        });
    }

    static /* synthetic */ void lambda$doLogFinishTransition$2(com.android.server.wm.Transition transition, android.tracing.perfetto.TracingContext ctx) {
        android.util.proto.ProtoOutputStream os = ctx.newTracePacket();
        long token = os.start(1146756268128L);
        os.write(1120986464257L, transition.getSyncId());
        os.write(1112396529673L, transition.mLogger.mFinishTimeNs);
        os.end(token);
    }

    @Override // com.android.server.wm.TransitionTracer
    public void logAbortedTransition(com.android.server.wm.Transition transition) {
        if (!isTracing()) {
            return;
        }
        android.os.Trace.traceBegin(32L, "logAbortedTransition");
        try {
            doLogAbortedTransition(transition);
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    private void doLogAbortedTransition(final com.android.server.wm.Transition transition) {
        this.mDataSource.trace(new android.tracing.perfetto.TraceFunction() { // from class: com.android.server.wm.PerfettoTransitionTracer$$ExternalSyntheticLambda6
            public final void trace(android.tracing.perfetto.TracingContext tracingContext) {
                com.android.server.wm.PerfettoTransitionTracer.lambda$doLogAbortedTransition$3(transition, tracingContext);
            }
        });
    }

    static /* synthetic */ void lambda$doLogAbortedTransition$3(com.android.server.wm.Transition transition, android.tracing.perfetto.TracingContext ctx) {
        android.util.proto.ProtoOutputStream os = ctx.newTracePacket();
        long token = os.start(1146756268128L);
        os.write(1120986464257L, transition.getSyncId());
        os.write(1112396529672L, transition.mLogger.mAbortTimeNs);
        os.end(token);
    }

    @Override // com.android.server.wm.TransitionTracer
    public void logRemovingStartingWindow(com.android.server.wm.StartingData startingData) {
        if (!isTracing()) {
            return;
        }
        android.os.Trace.traceBegin(32L, "logRemovingStartingWindow");
        try {
            doLogRemovingStartingWindow(startingData);
        } finally {
            android.os.Trace.traceEnd(32L);
        }
    }

    public void doLogRemovingStartingWindow(final com.android.server.wm.StartingData startingData) {
        this.mDataSource.trace(new android.tracing.perfetto.TraceFunction() { // from class: com.android.server.wm.PerfettoTransitionTracer$$ExternalSyntheticLambda5
            public final void trace(android.tracing.perfetto.TracingContext tracingContext) {
                com.android.server.wm.PerfettoTransitionTracer.lambda$doLogRemovingStartingWindow$4(startingData, tracingContext);
            }
        });
    }

    static /* synthetic */ void lambda$doLogRemovingStartingWindow$4(com.android.server.wm.StartingData startingData, android.tracing.perfetto.TracingContext ctx) {
        android.util.proto.ProtoOutputStream os = ctx.newTracePacket();
        long token = os.start(1146756268128L);
        os.write(1120986464257L, startingData.mTransitionId);
        os.write(1112396529681L, android.os.SystemClock.elapsedRealtimeNanos());
        os.end(token);
    }

    @Override // com.android.server.wm.TransitionTracer
    public void startTrace(java.io.PrintWriter pw) {
    }

    @Override // com.android.server.wm.TransitionTracer
    public void stopTrace(java.io.PrintWriter pw) {
    }

    @Override // com.android.server.wm.TransitionTracer
    public void saveForBugreport(java.io.PrintWriter pw) {
    }

    @Override // com.android.server.wm.TransitionTracer
    public boolean isTracing() {
        return this.mActiveTraces.get() > 0;
    }

    private void addTransitionTargetsToProto(android.util.proto.ProtoOutputStream os, java.util.ArrayList<com.android.server.wm.Transition.ChangeInfo> targets) {
        int layerId;
        for (int i = 0; i < targets.size(); i++) {
            com.android.server.wm.Transition.ChangeInfo target = targets.get(i);
            if (target.mContainer.mSurfaceControl.isValid()) {
                layerId = target.mContainer.mSurfaceControl.getLayerId();
            } else {
                layerId = -1;
            }
            int windowId = java.lang.System.identityHashCode(target.mContainer);
            long token = os.start(2246267895822L);
            os.write(1120986464257L, target.mReadyMode);
            os.write(1120986464260L, target.mReadyFlags);
            os.write(1120986464258L, layerId);
            os.write(1120986464259L, windowId);
            os.end(token);
        }
    }
}
