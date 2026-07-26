package kotlinx.coroutines;

/* JADX INFO: compiled from: Interruptible.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b\u0002\u0018\u00002#\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\u0003\u0012\b\b\u0004\u0012\u0004\b\b(\u0005\u0012\u0004\u0012\u00020\u00060\u0001j\u0002`\u0007B\r\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0006\u0010\u0012\u001a\u00020\u0006J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0013\u0010\u0017\u001a\u00020\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u0002H\u0096\u0002J\u0006\u0010\u0018\u001a\u00020\u0006R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n \u0011*\u0004\u0018\u00010\u00100\u0010X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0019"}, d2 = {"Lkotlinx/coroutines/ThreadState;", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "cause", "", "Lkotlinx/coroutines/CompletionHandler;", com.android.server.am.HostingRecord.TRIGGER_TYPE_JOB, "Lkotlinx/coroutines/Job;", "(Lkotlinx/coroutines/Job;)V", "_state", "Lkotlinx/atomicfu/AtomicInt;", "cancelHandle", "Lkotlinx/coroutines/DisposableHandle;", "targetThread", "Ljava/lang/Thread;", "kotlin.jvm.PlatformType", "clearInterrupt", "invalidState", "", "state", "", "invoke", "setup", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class ThreadState implements kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit> {
    private final kotlinx.atomicfu.AtomicInt _state;
    private kotlinx.coroutines.DisposableHandle cancelHandle;
    private final kotlinx.coroutines.Job job;
    private final java.lang.Thread targetThread;

    public ThreadState(kotlinx.coroutines.Job job) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(job, "job");
        this.job = job;
        this._state = kotlinx.atomicfu.AtomicFU.atomic(0);
        this.targetThread = java.lang.Thread.currentThread();
    }

    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ kotlin.Unit invoke(java.lang.Throwable th) {
        invoke2(th);
        return kotlin.Unit.INSTANCE;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:217)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:68)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:104)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    public final void setup() {
        /*
            r6 = this;
            kotlinx.coroutines.Job r0 = r6.job
            r1 = 1
            r2 = r6
            kotlin.jvm.functions.Function1 r2 = (kotlin.jvm.functions.Function1) r2
            kotlinx.coroutines.DisposableHandle r0 = r0.invokeOnCompletion(r1, r1, r2)
            r6.cancelHandle = r0
            kotlinx.atomicfu.AtomicInt r0 = r6._state
            r1 = 0
        Lf:
            int r2 = r0.getValue()
            r3 = 0
            switch(r2) {
                case 0: goto L22;
                case 1: goto L18;
                case 2: goto L21;
                case 3: goto L21;
                default: goto L18;
            }
        L18:
            r6.invalidState(r2)
            kotlin.KotlinNothingValueException r4 = new kotlin.KotlinNothingValueException
            r4.<init>()
            throw r4
        L21:
            return
        L22:
            kotlinx.atomicfu.AtomicInt r4 = r6._state
            r5 = 0
            boolean r4 = r4.compareAndSet(r2, r5)
            if (r4 == 0) goto L2c
            return
        L2c:
            goto Lf
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.ThreadState.setup():void");
    }

    public final void clearInterrupt() {
        kotlinx.atomicfu.AtomicInt $this$loop$iv = this._state;
        while (true) {
            int state = $this$loop$iv.getValue();
            switch (state) {
                case 0:
                    if (this._state.compareAndSet(state, 1)) {
                        kotlinx.coroutines.DisposableHandle disposableHandle = this.cancelHandle;
                        if (disposableHandle != null) {
                            disposableHandle.dispose();
                            return;
                        }
                        return;
                    }
                    break;
                case 1:
                default:
                    invalidState(state);
                    throw new kotlin.KotlinNothingValueException();
                case 2:
                    break;
                case 3:
                    java.lang.Thread.interrupted();
                    return;
            }
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:217)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:68)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.LoopRegionMaker.process(LoopRegionMaker.java:104)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:89)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
    public void invoke2(java.lang.Throwable r7) {
        /*
            r6 = this;
            kotlinx.atomicfu.AtomicInt r0 = r6._state
            r1 = 0
        L3:
            int r2 = r0.getValue()
            r3 = 0
            switch(r2) {
                case 0: goto L16;
                case 1: goto L15;
                case 2: goto L15;
                case 3: goto L15;
                default: goto Lc;
            }
        Lc:
            r6.invalidState(r2)
            kotlin.KotlinNothingValueException r4 = new kotlin.KotlinNothingValueException
            r4.<init>()
            throw r4
        L15:
            return
        L16:
            kotlinx.atomicfu.AtomicInt r4 = r6._state
            r5 = 2
            boolean r4 = r4.compareAndSet(r2, r5)
            if (r4 == 0) goto L2b
            java.lang.Thread r4 = r6.targetThread
            r4.interrupt()
            kotlinx.atomicfu.AtomicInt r4 = r6._state
            r5 = 3
            r4.setValue(r5)
            return
        L2b:
            goto L3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.ThreadState.invoke2(java.lang.Throwable):void");
    }

    private final java.lang.Void invalidState(int state) {
        throw new java.lang.IllegalStateException(("Illegal state " + state).toString());
    }
}
