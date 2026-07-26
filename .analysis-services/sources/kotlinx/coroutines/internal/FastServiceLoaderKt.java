package kotlinx.coroutines.internal;

/* JADX INFO: compiled from: FastServiceLoader.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003¨\u0006\u0004"}, d2 = {"ANDROID_DETECTED", "", "getANDROID_DETECTED", "()Z", "external__kotlinx.coroutines__linux_glibc_common__kotlinx_coroutines-host"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class FastServiceLoaderKt {
    private static final boolean ANDROID_DETECTED;

    static {
        java.lang.Object objM11307constructorimpl;
        try {
            kotlin.Result.Companion companion = kotlin.Result.INSTANCE;
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl(java.lang.Class.forName("android.os.Build"));
        } catch (java.lang.Throwable th) {
            kotlin.Result.Companion companion2 = kotlin.Result.INSTANCE;
            objM11307constructorimpl = kotlin.Result.m11307constructorimpl(kotlin.ResultKt.createFailure(th));
        }
        ANDROID_DETECTED = kotlin.Result.m11314isSuccessimpl(objM11307constructorimpl);
    }

    public static final boolean getANDROID_DETECTED() {
        return ANDROID_DETECTED;
    }
}
