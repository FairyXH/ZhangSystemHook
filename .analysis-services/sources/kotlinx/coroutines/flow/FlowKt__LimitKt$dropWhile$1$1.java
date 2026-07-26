package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: Limit.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002H\u008a@¢\u0006\u0004\b\u0004\u0010\u0005"}, d2 = {"<anonymous>", "", "T", "value", "emit", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;"}, k = 3, mv = {1, 9, 0}, xi = 48)
final class FlowKt__LimitKt$dropWhile$1$1<T> implements kotlinx.coroutines.flow.FlowCollector {
    final /* synthetic */ kotlin.jvm.internal.Ref.BooleanRef $matched;
    final /* synthetic */ kotlin.jvm.functions.Function2<T, kotlin.coroutines.Continuation<? super java.lang.Boolean>, java.lang.Object> $predicate;
    final /* synthetic */ kotlinx.coroutines.flow.FlowCollector<T> $this_unsafeFlow;

    /* JADX WARN: Multi-variable type inference failed */
    FlowKt__LimitKt$dropWhile$1$1(kotlin.jvm.internal.Ref.BooleanRef booleanRef, kotlinx.coroutines.flow.FlowCollector<? super T> flowCollector, kotlin.jvm.functions.Function2<? super T, ? super kotlin.coroutines.Continuation<? super java.lang.Boolean>, ? extends java.lang.Object> function2) {
        this.$matched = booleanRef;
        this.$this_unsafeFlow = flowCollector;
        this.$predicate = function2;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // kotlinx.coroutines.flow.FlowCollector
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object emit(T r7, kotlin.coroutines.Continuation<? super kotlin.Unit> r8) throws java.lang.Throwable {
        /*
            r6 = this;
            boolean r0 = r8 instanceof kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$1$1$emit$1
            if (r0 == 0) goto L14
            r0 = r8
            kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$1$1$emit$1 r0 = (kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$1$1$emit$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r8 = r0.label
            int r8 = r8 - r2
            r0.label = r8
            goto L19
        L14:
            kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$1$1$emit$1 r0 = new kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$1$1$emit$1
            r0.<init>(r6, r8)
        L19:
            r8 = r0
            java.lang.Object r0 = r8.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r8.label
            r3 = 1
            switch(r2) {
                case 0: goto L3d;
                case 1: goto L39;
                case 2: goto L2e;
                case 3: goto L39;
                default: goto L26;
            }
        L26:
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
            java.lang.String r8 = "call to 'resume' before 'invoke' with coroutine"
            r7.<init>(r8)
            throw r7
        L2e:
            java.lang.Object r7 = r8.L$1
            java.lang.Object r2 = r8.L$0
            kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$1$1 r2 = (kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$1$1) r2
            kotlin.ResultKt.throwOnFailure(r0)
            r4 = r0
            goto L65
        L39:
            kotlin.ResultKt.throwOnFailure(r0)
            goto L52
        L3d:
            kotlin.ResultKt.throwOnFailure(r0)
            r2 = r6
            kotlin.jvm.internal.Ref$BooleanRef r4 = r2.$matched
            boolean r4 = r4.element
            if (r4 == 0) goto L55
            kotlinx.coroutines.flow.FlowCollector<T> r4 = r2.$this_unsafeFlow
            r8.label = r3
            java.lang.Object r7 = r4.emit(r7, r8)
            if (r7 != r1) goto L52
            return r1
        L52:
            kotlin.Unit r7 = kotlin.Unit.INSTANCE
            return r7
        L55:
            kotlin.jvm.functions.Function2<T, kotlin.coroutines.Continuation<? super java.lang.Boolean>, java.lang.Object> r4 = r2.$predicate
            r8.L$0 = r2
            r8.L$1 = r7
            r5 = 2
            r8.label = r5
            java.lang.Object r4 = r4.invoke(r7, r8)
            if (r4 != r1) goto L65
            return r1
        L65:
            java.lang.Boolean r4 = (java.lang.Boolean) r4
            boolean r4 = r4.booleanValue()
            if (r4 != 0) goto L82
            kotlin.jvm.internal.Ref$BooleanRef r4 = r2.$matched
            r4.element = r3
            kotlinx.coroutines.flow.FlowCollector<T> r3 = r2.$this_unsafeFlow
            r4 = 0
            r8.L$0 = r4
            r8.L$1 = r4
            r4 = 3
            r8.label = r4
            java.lang.Object r7 = r3.emit(r7, r8)
            if (r7 != r1) goto L52
            return r1
        L82:
            goto L52
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__LimitKt$dropWhile$1$1.emit(java.lang.Object, kotlin.coroutines.Continuation):java.lang.Object");
    }
}
