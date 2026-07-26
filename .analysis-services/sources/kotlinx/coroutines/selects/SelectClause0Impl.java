package kotlinx.coroutines.selects;

/* JADX INFO: compiled from: Select.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0000\u0018\u00002\u00020\u0001BÏ\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012U\u0010\u0004\u001aQ\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u0002\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\b¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\t\u0012\u0015\u0012\u0013\u0018\u00010\u0003¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b0\u0005j\u0002`\f\u0012i\b\u0002\u0010\r\u001ac\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\b¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\t\u0012\u0015\u0012\u0013\u0018\u00010\u0003¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u00010\u0003¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u000e\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u000b0\u000f\u0018\u00010\u0005j\u0004\u0018\u0001`\u0011¢\u0006\u0002\u0010\u0012R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014Ru\u0010\r\u001ac\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\b¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\t\u0012\u0015\u0012\u0013\u0018\u00010\u0003¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u00010\u0003¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u000e\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u000b0\u000f\u0018\u00010\u0005j\u0004\u0018\u0001`\u0011X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0016Rc\u0010\u0017\u001aQ\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u0002\u0012\u0015\u0012\u0013\u0018\u00010\u0003¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\n\u0012\u0015\u0012\u0013\u0018\u00010\u0003¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u0018\u0012\u0006\u0012\u0004\u0018\u00010\u00030\u0005j\u0002`\u0019X\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0016Rc\u0010\u0004\u001aQ\u0012\u0013\u0012\u00110\u0003¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\u0002\u0012\u0017\u0012\u0015\u0012\u0002\b\u00030\b¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\t\u0012\u0015\u0012\u0013\u0018\u00010\u0003¢\u0006\f\b\u0006\u0012\b\b\u0007\u0012\u0004\b\b(\n\u0012\u0004\u0012\u00020\u000b0\u0005j\u0002`\fX\u0096\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0016¨\u0006\u001c"}, d2 = {"Lkotlinx/coroutines/selects/SelectClause0Impl;", "Lkotlinx/coroutines/selects/SelectClause0;", "clauseObject", "", "regFunc", "Lkotlin/Function3;", "Lkotlin/ParameterName;", "name", "Lkotlinx/coroutines/selects/SelectInstance;", "select", "param", "", "Lkotlinx/coroutines/selects/RegistrationFunction;", "onCancellationConstructor", "internalResult", "Lkotlin/Function1;", "", "Lkotlinx/coroutines/selects/OnCancellationConstructor;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function3;Lkotlin/jvm/functions/Function3;)V", "getClauseObject", "()Ljava/lang/Object;", "getOnCancellationConstructor", "()Lkotlin/jvm/functions/Function3;", "processResFunc", "clauseResult", "Lkotlinx/coroutines/selects/ProcessResultFunction;", "getProcessResFunc", "getRegFunc", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class SelectClause0Impl implements kotlinx.coroutines.selects.SelectClause0 {
    private final java.lang.Object clauseObject;
    private final kotlin.jvm.functions.Function3<kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, java.lang.Object, kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit>> onCancellationConstructor;
    private final kotlin.jvm.functions.Function3<java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object> processResFunc;
    private final kotlin.jvm.functions.Function3<java.lang.Object, kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, kotlin.Unit> regFunc;

    /* JADX WARN: Multi-variable type inference failed */
    public SelectClause0Impl(java.lang.Object clauseObject, kotlin.jvm.functions.Function3<java.lang.Object, ? super kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, kotlin.Unit> regFunc, kotlin.jvm.functions.Function3<? super kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, java.lang.Object, ? extends kotlin.jvm.functions.Function1<? super java.lang.Throwable, kotlin.Unit>> function3) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(clauseObject, "clauseObject");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(regFunc, "regFunc");
        this.clauseObject = clauseObject;
        this.regFunc = regFunc;
        this.onCancellationConstructor = function3;
        this.processResFunc = kotlinx.coroutines.selects.SelectKt.DUMMY_PROCESS_RESULT_FUNCTION;
    }

    public /* synthetic */ SelectClause0Impl(java.lang.Object obj, kotlin.jvm.functions.Function3 function3, kotlin.jvm.functions.Function3 function32, int i, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(obj, function3, (i & 4) != 0 ? null : function32);
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
    public kotlin.jvm.functions.Function3<kotlinx.coroutines.selects.SelectInstance<?>, java.lang.Object, java.lang.Object, kotlin.jvm.functions.Function1<java.lang.Throwable, kotlin.Unit>> getOnCancellationConstructor() {
        return this.onCancellationConstructor;
    }

    @Override // kotlinx.coroutines.selects.SelectClause
    public kotlin.jvm.functions.Function3<java.lang.Object, java.lang.Object, java.lang.Object, java.lang.Object> getProcessResFunc() {
        return this.processResFunc;
    }
}
