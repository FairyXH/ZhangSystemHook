package kotlinx.coroutines.selects;

/* JADX INFO: compiled from: OnTimeout.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001e\u0010\u000b\u001a\u00020\f2\n\u0010\r\u001a\u0006\u0012\u0002\b\u00030\u000e2\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001H\u0002R\u0017\u0010\u0005\u001a\u00020\u00068F¢\u0006\f\u0012\u0004\b\u0007\u0010\b\u001a\u0004\b\t\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lkotlinx/coroutines/selects/OnTimeout;", "", "timeMillis", "", "(J)V", "selectClause", "Lkotlinx/coroutines/selects/SelectClause0;", "getSelectClause$annotations", "()V", "getSelectClause", "()Lkotlinx/coroutines/selects/SelectClause0;", "register", "", "select", "Lkotlinx/coroutines/selects/SelectInstance;", "ignoredParam", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
final class OnTimeout {
    private final long timeMillis;

    public static /* synthetic */ void getSelectClause$annotations() {
    }

    public OnTimeout(long timeMillis) {
        this.timeMillis = timeMillis;
    }

    public final kotlinx.coroutines.selects.SelectClause0 getSelectClause() {
        kotlinx.coroutines.selects.OnTimeout$selectClause$1 onTimeout$selectClause$1 = kotlinx.coroutines.selects.OnTimeout$selectClause$1.INSTANCE;
        kotlin.jvm.internal.Intrinsics.checkNotNull(onTimeout$selectClause$1, "null cannot be cast to non-null type kotlin.Function3<@[ParameterName(name = 'clauseObject')] kotlin.Any, @[ParameterName(name = 'select')] kotlinx.coroutines.selects.SelectInstance<*>, @[ParameterName(name = 'param')] kotlin.Any?, kotlin.Unit>{ kotlinx.coroutines.selects.SelectKt.RegistrationFunction }");
        return new kotlinx.coroutines.selects.SelectClause0Impl(this, (kotlin.jvm.functions.Function3) kotlin.jvm.internal.TypeIntrinsics.beforeCheckcastToFunctionOfArity(onTimeout$selectClause$1, 3), null, 4, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void register(final kotlinx.coroutines.selects.SelectInstance<?> select, java.lang.Object ignoredParam) {
        if (this.timeMillis <= 0) {
            select.selectInRegistrationPhase(kotlin.Unit.INSTANCE);
            return;
        }
        java.lang.Runnable action = new java.lang.Runnable() { // from class: kotlinx.coroutines.selects.OnTimeout$register$$inlined$Runnable$1
            @Override // java.lang.Runnable
            public final void run() {
                select.trySelect(this, kotlin.Unit.INSTANCE);
            }
        };
        kotlin.jvm.internal.Intrinsics.checkNotNull(select, "null cannot be cast to non-null type kotlinx.coroutines.selects.SelectImplementation<*>");
        kotlin.coroutines.CoroutineContext context = select.getContext();
        kotlinx.coroutines.DisposableHandle disposableHandle = kotlinx.coroutines.DelayKt.getDelay(context).invokeOnTimeout(this.timeMillis, action, context);
        select.disposeOnCompletion(disposableHandle);
    }
}
