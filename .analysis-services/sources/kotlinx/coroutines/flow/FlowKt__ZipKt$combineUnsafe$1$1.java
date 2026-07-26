package kotlinx.coroutines.flow;

/* JADX INFO: Add missing generic type declarations: [R, T] */
/* JADX INFO: compiled from: Zip.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\u0010\u0000\u001a\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u00042\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0006H\u008a@"}, d2 = {"<anonymous>", "", "T", "R", "Lkotlinx/coroutines/flow/FlowCollector;", "it", ""}, k = 3, mv = {1, 9, 0}, xi = 48)
@kotlin.coroutines.jvm.internal.DebugMetadata(c = "kotlinx.coroutines.flow.FlowKt__ZipKt$combineUnsafe$1$1", f = "Zip.kt", i = {}, l = {262, 262}, m = "invokeSuspend", n = {}, s = {})
public final class FlowKt__ZipKt$combineUnsafe$1$1<R, T> extends kotlin.coroutines.jvm.internal.SuspendLambda implements kotlin.jvm.functions.Function3<kotlinx.coroutines.flow.FlowCollector<? super R>, T[], kotlin.coroutines.Continuation<? super kotlin.Unit>, java.lang.Object> {
    final /* synthetic */ kotlin.jvm.functions.Function2<T[], kotlin.coroutines.Continuation<? super R>, java.lang.Object> $transform;
    private /* synthetic */ java.lang.Object L$0;
    /* synthetic */ java.lang.Object L$1;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public FlowKt__ZipKt$combineUnsafe$1$1(kotlin.jvm.functions.Function2<? super T[], ? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> function2, kotlin.coroutines.Continuation<? super kotlinx.coroutines.flow.FlowKt__ZipKt$combineUnsafe$1$1> continuation) {
        super(3, continuation);
        this.$transform = function2;
    }

    @Override // kotlin.jvm.functions.Function3
    public final java.lang.Object invoke(kotlinx.coroutines.flow.FlowCollector<? super R> flowCollector, T[] tArr, kotlin.coroutines.Continuation<? super kotlin.Unit> continuation) {
        kotlin.jvm.internal.Intrinsics.needClassReification();
        kotlinx.coroutines.flow.FlowKt__ZipKt$combineUnsafe$1$1 flowKt__ZipKt$combineUnsafe$1$1 = new kotlinx.coroutines.flow.FlowKt__ZipKt$combineUnsafe$1$1(this.$transform, continuation);
        flowKt__ZipKt$combineUnsafe$1$1.L$0 = flowCollector;
        flowKt__ZipKt$combineUnsafe$1$1.L$1 = tArr;
        return flowKt__ZipKt$combineUnsafe$1$1.invokeSuspend(kotlin.Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0050 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0051  */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$ArrayArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object invokeSuspend(java.lang.Object r8) throws java.lang.Throwable {
        /*
            r7 = this;
            java.lang.Object r0 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r1 = r7.label
            switch(r1) {
                case 0: goto L22;
                case 1: goto L16;
                case 2: goto L11;
                default: goto L9;
            }
        L9:
            java.lang.IllegalStateException r8 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r8.<init>(r0)
            throw r8
        L11:
            r0 = r7
            kotlin.ResultKt.throwOnFailure(r8)
            goto L53
        L16:
            r1 = r7
            java.lang.Object r2 = r1.L$0
            kotlinx.coroutines.flow.FlowCollector r2 = (kotlinx.coroutines.flow.FlowCollector) r2
            kotlin.ResultKt.throwOnFailure(r8)
            r3 = r2
            r2 = r1
            r1 = r8
            goto L41
        L22:
            kotlin.ResultKt.throwOnFailure(r8)
            r1 = r7
            java.lang.Object r2 = r1.L$0
            kotlinx.coroutines.flow.FlowCollector r2 = (kotlinx.coroutines.flow.FlowCollector) r2
            java.lang.Object r3 = r1.L$1
            java.lang.Object[] r3 = (java.lang.Object[]) r3
            kotlin.jvm.functions.Function2<T[], kotlin.coroutines.Continuation<? super R>, java.lang.Object> r4 = r1.$transform
            r1.L$0 = r2
            r5 = 1
            r1.label = r5
            java.lang.Object r3 = r4.invoke(r3, r1)
            if (r3 != r0) goto L3c
            return r0
        L3c:
            r6 = r1
            r1 = r8
            r8 = r3
            r3 = r2
            r2 = r6
        L41:
            r4 = r2
            kotlin.coroutines.Continuation r4 = (kotlin.coroutines.Continuation) r4
            r5 = 0
            r2.L$0 = r5
            r5 = 2
            r2.label = r5
            java.lang.Object r8 = r3.emit(r8, r4)
            if (r8 != r0) goto L51
            return r0
        L51:
            r8 = r1
            r0 = r2
        L53:
            kotlin.Unit r1 = kotlin.Unit.INSTANCE
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.flow.FlowKt__ZipKt$combineUnsafe$1$1.invokeSuspend(java.lang.Object):java.lang.Object");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$ArrayArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public final java.lang.Object invokeSuspend$$forInline(java.lang.Object obj) {
        kotlinx.coroutines.flow.FlowCollector flowCollector = (kotlinx.coroutines.flow.FlowCollector) this.L$0;
        java.lang.Object[] objArr = (java.lang.Object[]) this.L$1;
        kotlin.jvm.internal.InlineMarker.mark(0);
        flowCollector.emit(this.$transform.invoke(objArr, this), this);
        kotlin.jvm.internal.InlineMarker.mark(1);
        return kotlin.Unit.INSTANCE;
    }
}
