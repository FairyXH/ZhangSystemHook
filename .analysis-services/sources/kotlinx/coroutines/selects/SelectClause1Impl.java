package kotlinx.coroutines.selects;

/* JADX INFO: compiled from: Select.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\b\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B¦\u0002\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012U\u0010\u0005\u001aQ\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0003\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\t¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u00010\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00020\f0\u0006j\u0002`\r\u0012U\u0010\u000e\u001aQ\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0003\u0012\u0015\u0012\u0013\u0018\u00010\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000b\u0012\u0015\u0012\u0013\u0018\u00010\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000f\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0006j\u0002`\u0010\u0012i\b\u0002\u0010\u0011\u001ac\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\t¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u00010\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000b\u0012\u0015\u0012\u0013\u0018\u00010\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0012\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\f0\u0013\u0018\u00010\u0006j\u0004\u0018\u0001`\u0015¢\u0006\u0002\u0010\u0016R\u0014\u0010\u0003\u001a\u00020\u0004X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018Ru\u0010\u0011\u001ac\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\t¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u00010\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000b\u0012\u0015\u0012\u0013\u0018\u00010\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0012\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0014\u0012\u0004\u0012\u00020\f0\u0013\u0018\u00010\u0006j\u0004\u0018\u0001`\u0015X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aRc\u0010\u000e\u001aQ\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0003\u0012\u0015\u0012\u0013\u0018\u00010\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000b\u0012\u0015\u0012\u0013\u0018\u00010\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000f\u0012\u0006\u0012\u0004\u0018\u00010\u00040\u0006j\u0002`\u0010X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001aRc\u0010\u0005\u001aQ\u0012\u0013\u0012\u00110\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u0003\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\t¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u00010\u0004¢\u0006\f\b\u0007\u0012\b\b\b\u0012\u0004\b\b(\u000b\u0012\u0004\u0012\u00020\f0\u0006j\u0002`\rX\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001a¨\u0006\u001d"}, d2 = {"Lkotlinx/coroutines/selects/SelectClause1Impl;", "Q", "Lkotlinx/coroutines/selects/SelectClause1;", "clauseObject", "", "regFunc", "Lkotlin/Function3;", "Lkotlin/ParameterName;", "name", "Lkotlinx/coroutines/selects/SelectInstance;", "select", "param", "", "Lkotlinx/coroutines/selects/RegistrationFunction;", "processResFunc", "clauseResult", "Lkotlinx/coroutines/selects/ProcessResultFunction;", "onCancellationConstructor", "internalResult", "Lkotlin/Function1;", "", "Lkotlinx/coroutines/selects/OnCancellationConstructor;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function3;)V", "getClauseObject", "()Ljava/lang/Object;", "getOnCancellationConstructor", "()Lkotlin/jvm/functions/Function3;", "getProcessResFunc", "getRegFunc", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SelectClause1Impl<Q> implements kotlinx.coroutines.selects.SelectClause1<Q> {
    private final java.lang.Object clauseObject;
    private final kotlin.jvm.functions.Function3<kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, java.lang.Object, kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit>> onCancellationConstructor;
    private final kotlin.jvm.functions.Function3<java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object> processResFunc;
    private final kotlin.jvm.functions.Function3<java.lang.Object, kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, kotlin.Unit> regFunc;

    /* JADX WARN: Multi-variable type inference failed */
    public SelectClause1Impl(java.lang.Object clauseObject, kotlin.jvm.functions.Function3<java.lang.Object, ? super kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, kotlin.Unit> regFunc, kotlin.jvm.functions.Function3<java.lang.Object, java.lang.Object, java.lang.Object, ? extends java.lang.Object> processResFunc, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, java.lang.Object, ? extends kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit>> function3) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clauseObject, "clauseObject");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(regFunc, "regFunc");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(processResFunc, "processResFunc");
        this.clauseObject = clauseObject;
        this.regFunc = regFunc;
        this.processResFunc = processResFunc;
        this.onCancellationConstructor = function3;
    }

    public /* synthetic */ SelectClause1Impl(java.lang.Object obj, kotlin.jvm.functions.Function3 function3, kotlin.jvm.functions.Function3 function32, kotlin.jvm.functions.Function3 function33, int i, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(obj, function3, function32, (i & 8) != 0 ? null : function33);
    }

    @Override // kotlinx.coroutines.selects.SelectClause
    public java.lang.Object getClauseObject() {
        return this.clauseObject;
    }

    @Override // kotlinx.coroutines.selects.SelectClause
    public kotlin.jvm.functions.Function3<java.lang.Object, kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, kotlin.Unit> getRegFunc() {
        return this.regFunc;
    }

    @Override // kotlinx.coroutines.selects.SelectClause
    public kotlin.jvm.functions.Function3<java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object> getProcessResFunc() {
        return this.processResFunc;
    }

    @Override // kotlinx.coroutines.selects.SelectClause
    public kotlin.jvm.functions.Function3<kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, java.lang.Object, kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit>> getOnCancellationConstructor() {
        return this.onCancellationConstructor;
    }
}
