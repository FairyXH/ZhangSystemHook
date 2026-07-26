package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Collection.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000(\n\u0002\b\u0003\n\u0002\u0010\u001f\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0010!\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010#\n\u0002\b\u0002\u001a8\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0002\"\u0010\b\u0001\u0010\u0001*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u0006\u0010\u0005\u001a\u0002H\u0001H\u0086@¢\u0006\u0002\u0010\u0006\u001a4\u0010\u0007\u001a\b\u0012\u0004\u0012\u0002H\u00020\b\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0086@¢\u0006\u0002\u0010\n\u001a4\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\u00020\f\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u000e\b\u0002\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\rH\u0086@¢\u0006\u0002\u0010\u000e¨\u0006\u000f"}, d2 = {"toCollection", "C", "T", "", "Lkotlinx/coroutines/flow/Flow;", "destination", "(Lkotlinx/coroutines/flow/Flow;Ljava/util/Collection;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toList", "", "", "(Lkotlinx/coroutines/flow/Flow;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toSet", "", "", "(Lkotlinx/coroutines/flow/Flow;Ljava/util/Set;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 5, mv = {1, 9, 0}, xi = 48, xs = "kotlinx/coroutines/flow/FlowKt")
final /* synthetic */ class FlowKt__CollectionKt {

    /* JADX INFO: renamed from: kotlinx.coroutines.flow.FlowKt__CollectionKt$toCollection$1, reason: invalid class name */
    /* JADX INFO: compiled from: Collection.kt */
    @kotlin.Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__CollectionKt", f = "Collection.kt", i = {0}, l = {26}, m = "toCollection", n = {"destination"}, s = {"L$0"})
    static final class AnonymousClass1<T, C extends java.util.Collection<? super T>> extends kotlin.coroutines.jvm.internal.ContinuationImpl {
        java.lang.Object L$0;
        int label;
        /* synthetic */ java.lang.Object result;

        AnonymousClass1(kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__CollectionKt.AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final java.lang.Object invokeSuspend(java.lang.Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return kotlinx.coroutines.flow.FlowKt.toCollection(null, null, this);
        }
    }

    public static final <T> java.lang.Object toList(kotlinx.coroutines.flow.Flow<? extends T> flow, java.util.List<T> list, kotlin.coroutines.Continuation<? super java.util.List<? extends T>> continuation) {
        return kotlinx.coroutines.flow.FlowKt.toCollection(flow, list, continuation);
    }

    public static /* synthetic */ java.lang.Object toList$default(kotlinx.coroutines.flow.Flow flow, java.util.List list, kotlin.coroutines.Continuation continuation, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            list = new java.util.ArrayList();
        }
        return kotlinx.coroutines.flow.FlowKt.toList(flow, list, continuation);
    }

    public static final <T> java.lang.Object toSet(kotlinx.coroutines.flow.Flow<? extends T> flow, java.util.Set<T> set, kotlin.coroutines.Continuation<? super java.util.Set<? extends T>> continuation) {
        return kotlinx.coroutines.flow.FlowKt.toCollection(flow, set, continuation);
    }

    public static /* synthetic */ java.lang.Object toSet$default(kotlinx.coroutines.flow.Flow flow, java.util.Set set, kotlin.coroutines.Continuation continuation, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            set = new java.util.LinkedHashSet();
        }
        return kotlinx.coroutines.flow.FlowKt.toSet(flow, set, continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static final <T, C extends java.util.Collection<? super T>> java.lang.Object toCollection(kotlinx.coroutines.flow.Flow<? extends T> r4, final C r5, kotlin.coroutines.Continuation<? super C> r6) throws java.lang.Throwable {
        /*
            boolean r0 = r6 instanceof kotlinx.coroutines.flow.FlowKt__CollectionKt.AnonymousClass1
            if (r0 == 0) goto L14
            r0 = r6
            kotlinx.coroutines.flow.FlowKt__CollectionKt$toCollection$1 r0 = (kotlinx.coroutines.flow.FlowKt__CollectionKt.AnonymousClass1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r6 = r0.label
            int r6 = r6 - r2
            r0.label = r6
            goto L19
        L14:
            kotlinx.coroutines.flow.FlowKt__CollectionKt$toCollection$1 r0 = new kotlinx.coroutines.flow.FlowKt__CollectionKt$toCollection$1
            r0.<init>(r6)
        L19:
            r6 = r0
            java.lang.Object r0 = r6.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r6.label
            switch(r2) {
                case 0: goto L35;
                case 1: goto L2d;
                default: goto L25;
            }
        L25:
            java.lang.IllegalStateException r4 = new java.lang.IllegalStateException
            java.lang.String r5 = "call to 'resume' before 'invoke' with coroutine"
            r4.<init>(r5)
            throw r4
        L2d:
            java.lang.Object r4 = r6.L$0
            java.util.Collection r4 = (java.util.Collection) r4
            kotlin.ResultKt.throwOnFailure(r0)
            goto L4c
        L35:
            kotlin.ResultKt.throwOnFailure(r0)
            kotlinx.coroutines.flow.FlowKt__CollectionKt$toCollection$2 r2 = new kotlinx.coroutines.flow.FlowKt__CollectionKt$toCollection$2
            r2.<init>()
            kotlinx.coroutines.flow.FlowCollector r2 = (kotlinx.coroutines.flow.FlowCollector) r2
            r6.L$0 = r5
            r3 = 1
            r6.label = r3
            java.lang.Object r4 = r4.collect(r2, r6)
            if (r4 != r1) goto L4b
            return r1
        L4b:
            r4 = r5
        L4c:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__CollectionKt.toCollection(kotlinx.coroutines.flow.Flow, java.util.Collection, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
