package kotlinx.coroutines.selects;

/* JADX INFO: compiled from: OnTimeout.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000,\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001aC\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u001c\u0010\u0006\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0007H\u0007¢\u0006\u0002\u0010\n\u001aH\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0006\u0010\u000b\u001a\u00020\f2\u001c\u0010\u0006\u001a\u0018\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\b\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0007H\u0007ø\u0001\u0000¢\u0006\u0004\b\r\u0010\n\u0082\u0002\u0007\n\u0005\b¡\u001e0\u0001¨\u0006\u000e"}, d2 = {"onTimeout", "", "R", "Lkotlinx/coroutines/selects/SelectBuilder;", "timeMillis", "", "block", "Lkotlin/Function1;", "Lkotlin/coroutines/Continuation;", "", "(Lkotlinx/coroutines/selects/SelectBuilder;JLkotlin/jvm/functions/Function1;)V", "timeout", "Lkotlin/time/Duration;", "onTimeout-8Mi8wO0", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class OnTimeoutKt {
    /* JADX WARN: Multi-variable type inference failed */
    public static final <R> void onTimeout(kotlinx.coroutines.selects.SelectBuilder<? super R> selectBuilder, long timeMillis, kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(selectBuilder, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        selectBuilder.invoke(new kotlinx.coroutines.selects.OnTimeout(timeMillis).getSelectClause(), (kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super java.lang.Object>, ? extends java.lang.Object>) block);
    }

    /* JADX INFO: renamed from: onTimeout-8Mi8wO0, reason: not valid java name */
    public static final <R> void m12876onTimeout8Mi8wO0(kotlinx.coroutines.selects.SelectBuilder<? super R> onTimeout, long timeout, kotlin.jvm.functions.Function1<? super kotlin.coroutines.Continuation<? super R>, ? extends java.lang.Object> block) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(onTimeout, "$this$onTimeout");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(block, "block");
        onTimeout(onTimeout, kotlinx.coroutines.DelayKt.m12799toDelayMillisLRDsOJo(timeout), block);
    }
}
