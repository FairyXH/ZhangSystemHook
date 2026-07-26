package kotlinx.coroutines.debug.internal;

/* JADX INFO: compiled from: AgentInstallationType.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001a\u0010\u0003\u001a\u00020\u0004X\u0080\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\b¨\u0006\t"}, d2 = {"Lkotlinx/coroutines/debug/internal/AgentInstallationType;", "", "()V", "isInstalledStatically", "", "isInstalledStatically$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "()Z", "setInstalledStatically$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host", "(Z)V", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AgentInstallationType {
    public static final kotlinx.coroutines.debug.internal.AgentInstallationType INSTANCE = new kotlinx.coroutines.debug.internal.AgentInstallationType();
    private static boolean isInstalledStatically;

    private AgentInstallationType() {
    }

    public final boolean isInstalledStatically$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host() {
        return isInstalledStatically;
    }

    public final void setInstalledStatically$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(boolean z) {
        isInstalledStatically = z;
    }
}
