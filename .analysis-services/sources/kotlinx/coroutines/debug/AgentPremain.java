package kotlinx.coroutines.debug;

/* JADX INFO: compiled from: AgentPremain.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bÁ\u0002\u0018\u00002\u00020\u0001:\u0001\fB\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0002J\u001a\u0010\u0007\u001a\u00020\u00062\b\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lkotlinx/coroutines/debug/AgentPremain;", "", "()V", "enableCreationStackTraces", "", "installSignalHandler", "", "premain", "args", "", "instrumentation", "Ljava/lang/instrument/Instrumentation;", "DebugProbesTransformer", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class AgentPremain {
    public static final kotlinx.coroutines.debug.AgentPremain INSTANCE = new kotlinx.coroutines.debug.AgentPremain();
    private static final boolean enableCreationStackTraces;

    private AgentPremain() {
    }

    static {
        java.lang.Object objM11307constructorimpl;
        kotlinx.coroutines.debug.AgentPremain agentPremain = INSTANCE;
        try {
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            java.lang.String property = java.lang.System.getProperty("kotlinx.coroutines.debug.enable.creation.stack.trace");
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl(property != null ? java.lang.Boolean.valueOf(java.lang.Boolean.parseBoolean(property)) : null);
        } catch (java.lang.Throwable th) {
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(th));
        }
        java.lang.Boolean bool = (java.lang.Boolean) (kotlin.Result.m11313isFailureimpl(objM11307constructorimpl) ? null : objM11307constructorimpl);
        enableCreationStackTraces = bool != null ? bool.booleanValue() : kotlinx.coroutines.debug.internal.DebugProbesImpl.INSTANCE.getEnableCreationStackTraces$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
    }

    @kotlin.jvm.JvmStatic
    public static final void premain(java.lang.String args, java.lang.instrument.Instrumentation instrumentation) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(instrumentation, "instrumentation");
        kotlinx.coroutines.debug.internal.AgentInstallationType.INSTANCE.setInstalledStatically$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(true);
        instrumentation.addTransformer(kotlinx.coroutines.debug.AgentPremain.DebugProbesTransformer.INSTANCE);
        kotlinx.coroutines.debug.internal.DebugProbesImpl.INSTANCE.setEnableCreationStackTraces$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(enableCreationStackTraces);
        kotlinx.coroutines.debug.internal.DebugProbesImpl.INSTANCE.install$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host();
        INSTANCE.installSignalHandler();
    }

    /* JADX INFO: compiled from: AgentPremain.kt */
    @kotlin.Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J<\u0010\u0003\u001a\u0004\u0018\u00010\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u00062\u0006\u0010\u0007\u001a\u00020\b2\f\u0010\t\u001a\b\u0012\u0002\b\u0003\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u0004H\u0016¨\u0006\u000e"}, d2 = {"Lkotlinx/coroutines/debug/AgentPremain$DebugProbesTransformer;", "Ljava/lang/instrument/ClassFileTransformer;", "()V", "transform", "", "loader", "Ljava/lang/ClassLoader;", "className", "", "classBeingRedefined", "Ljava/lang/Class;", "protectionDomain", "Ljava/security/ProtectionDomain;", "classfileBuffer", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class DebugProbesTransformer implements java.lang.instrument.ClassFileTransformer {
        public static final kotlinx.coroutines.debug.AgentPremain.DebugProbesTransformer INSTANCE = new kotlinx.coroutines.debug.AgentPremain.DebugProbesTransformer();

        private DebugProbesTransformer() {
        }

        public byte[] transform(java.lang.ClassLoader loader, java.lang.String className, java.lang.Class<?> classBeingRedefined, java.security.ProtectionDomain protectionDomain, byte[] classfileBuffer) {
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(className, "className");
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(protectionDomain, "protectionDomain");
            if (loader == null || !kotlin.jvm.internal.Intrinsics.areEqual(className, "kotlin/coroutines/jvm/internal/DebugProbesKt")) {
                return null;
            }
            kotlinx.coroutines.debug.internal.AgentInstallationType.INSTANCE.setInstalledStatically$external__kotlinx_coroutines__linux_glibc_common__kotlinx_coroutines_host(true);
            java.io.InputStream resourceAsStream = loader.getResourceAsStream("DebugProbesKt.bin");
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(resourceAsStream, "getResourceAsStream(...)");
            return kotlin.io.ByteStreamsKt.readBytes(resourceAsStream);
        }
    }

    private final void installSignalHandler() {
        try {
            sun.misc.Signal.handle(new sun.misc.Signal("TRAP"), new sun.misc.SignalHandler() { // from class: kotlinx.coroutines.debug.AgentPremain.installSignalHandler.1
                public final void handle(sun.misc.Signal it) {
                    if (kotlinx.coroutines.debug.internal.DebugProbesImpl.INSTANCE.isInstalled()) {
                        kotlinx.coroutines.debug.internal.DebugProbesImpl debugProbesImpl = kotlinx.coroutines.debug.internal.DebugProbesImpl.INSTANCE;
                        java.io.PrintStream out = java.lang.System.out;
                        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(out, "out");
                        debugProbesImpl.dumpCoroutines(out);
                        return;
                    }
                    java.lang.System.out.println((java.lang.Object) "Cannot perform coroutines dump, debug probes are disabled");
                }
            });
        } catch (java.lang.Throwable th) {
        }
    }
}
