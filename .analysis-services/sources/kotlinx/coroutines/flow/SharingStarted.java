package kotlinx.coroutines.flow;

/* JADX INFO: compiled from: SharingStarted.kt */
/* JADX INFO: loaded from: classes2.dex */
@kotlin.Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0002\bæ\u0080\u0001\u0018\u0000 \b2\u00020\u0001:\u0001\bJ\u001c\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006H&¨\u0006\t"}, d2 = {"Lkotlinx/coroutines/flow/SharingStarted;", "", "command", "Lkotlinx/coroutines/flow/Flow;", "Lkotlinx/coroutines/flow/SharingCommand;", "subscriptionCount", "Lkotlinx/coroutines/flow/StateFlow;", "", "Companion", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface SharingStarted {

    /* JADX INFO: renamed from: Companion, reason: from kotlin metadata */
    public static final kotlinx.coroutines.flow.SharingStarted.Companion INSTANCE = kotlinx.coroutines.flow.SharingStarted.Companion.$$INSTANCE;

    kotlinx.coroutines.flow.Flow<kotlinx.coroutines.flow.SharingCommand> command(kotlinx.coroutines.flow.StateFlow<java.lang.Integer> subscriptionCount);

    /* JADX INFO: compiled from: SharingStarted.kt */
    @kotlin.Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\t\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001a\u0010\t\u001a\u00020\u00042\b\b\u0002\u0010\n\u001a\u00020\u000b2\b\b\u0002\u0010\f\u001a\u00020\u000bR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/flow/SharingStarted$Companion;", "", "()V", "Eagerly", "Lkotlinx/coroutines/flow/SharingStarted;", "getEagerly", "()Lkotlinx/coroutines/flow/SharingStarted;", "Lazily", "getLazily", "WhileSubscribed", "stopTimeoutMillis", "", "replayExpirationMillis", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion {
        static final /* synthetic */ kotlinx.coroutines.flow.SharingStarted.Companion $$INSTANCE = new kotlinx.coroutines.flow.SharingStarted.Companion();
        private static final kotlinx.coroutines.flow.SharingStarted Eagerly = new kotlinx.coroutines.flow.StartedEagerly();
        private static final kotlinx.coroutines.flow.SharingStarted Lazily = new kotlinx.coroutines.flow.StartedLazily();

        private Companion() {
        }

        public final kotlinx.coroutines.flow.SharingStarted getEagerly() {
            return Eagerly;
        }

        public final kotlinx.coroutines.flow.SharingStarted getLazily() {
            return Lazily;
        }

        public static /* synthetic */ kotlinx.coroutines.flow.SharingStarted WhileSubscribed$default(kotlinx.coroutines.flow.SharingStarted.Companion companion, long j, long j2, int i, java.lang.Object obj) {
            if ((i & 1) != 0) {
                j = 0;
            }
            if ((i & 2) != 0) {
                j2 = Long.MAX_VALUE;
            }
            return companion.WhileSubscribed(j, j2);
        }

        public final kotlinx.coroutines.flow.SharingStarted WhileSubscribed(long stopTimeoutMillis, long replayExpirationMillis) {
            return new kotlinx.coroutines.flow.StartedWhileSubscribed(stopTimeoutMillis, replayExpirationMillis);
        }
    }
}
