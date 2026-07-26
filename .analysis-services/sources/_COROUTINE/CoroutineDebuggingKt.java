package _COROUTINE;

/* JADX INFO: compiled from: CoroutineDebugging.kt */
/* JADX INFO: loaded from: classes.dex */
@kotlin.Metadata(d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\b\u0002\u001a\u0014\u0010\u0004\u001a\u00020\u0005*\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0001H\u0002\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080D¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003¨\u0006\b"}, d2 = {"ARTIFICIAL_FRAME_PACKAGE_NAME", "", "getARTIFICIAL_FRAME_PACKAGE_NAME", "()Ljava/lang/String;", "artificialFrame", "Ljava/lang/StackTraceElement;", "", "name", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class CoroutineDebuggingKt {
    private static final java.lang.String ARTIFICIAL_FRAME_PACKAGE_NAME = "_COROUTINE";

    public static final java.lang.String getARTIFICIAL_FRAME_PACKAGE_NAME() {
        return ARTIFICIAL_FRAME_PACKAGE_NAME;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final java.lang.StackTraceElement artificialFrame(java.lang.Throwable $this$artificialFrame, java.lang.String name) {
        java.lang.StackTraceElement $this$artificialFrame_u24lambda_u240 = $this$artificialFrame.getStackTrace()[0];
        return new java.lang.StackTraceElement(ARTIFICIAL_FRAME_PACKAGE_NAME + "." + name, "_", $this$artificialFrame_u24lambda_u240.getFileName(), $this$artificialFrame_u24lambda_u240.getLineNumber());
    }
}
