package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: SharingStarted.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u001a(\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\b\b\u0002\u0010\u0003\u001a\u00020\u00042\b\b\u0002\u0010\u0005\u001a\u00020\u0004ø\u0001\u0000¢\u0006\u0004\b\u0006\u0010\u0007\u0082\u0002\u0007\n\u0005\b¡\u001e0\u0001¨\u0006\b"}, d2 = {"WhileSubscribed", "Lkotlinx/coroutines/flow/SharingStarted;", "Lkotlinx/coroutines/flow/SharingStarted$Companion;", "stopTimeout", "Lkotlin/time/Duration;", "replayExpiration", "WhileSubscribed-5qebJ5I", "(Lkotlinx/coroutines/flow/SharingStarted$Companion;JJ)Lkotlinx/coroutines/flow/SharingStarted;", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class SharingStartedKt {
    /* JADX INFO: renamed from: WhileSubscribed-5qebJ5I$default, reason: not valid java name */
    public static /* synthetic */ kotlinx.coroutines.flow.SharingStarted m12852WhileSubscribed5qebJ5I$default(kotlinx.coroutines.flow.SharingStarted.Companion companion, long j, long j2, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            j = kotlin.time.Duration.INSTANCE.m12736getZEROUwyO8pc();
        }
        if ((i & 2) != 0) {
            j2 = kotlin.time.Duration.INSTANCE.m12734getINFINITEUwyO8pc();
        }
        return m12851WhileSubscribed5qebJ5I(companion, j, j2);
    }

    /* JADX INFO: renamed from: WhileSubscribed-5qebJ5I, reason: not valid java name */
    public static final kotlinx.coroutines.flow.SharingStarted m12851WhileSubscribed5qebJ5I(kotlinx.coroutines.flow.SharingStarted.Companion WhileSubscribed, long stopTimeout, long replayExpiration) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(WhileSubscribed, "$this$WhileSubscribed");
        return new kotlinx.coroutines.flow.StartedWhileSubscribed(kotlin.time.Duration.m12651getInWholeMillisecondsimpl(stopTimeout), kotlin.time.Duration.m12651getInWholeMillisecondsimpl(replayExpiration));
    }
}
