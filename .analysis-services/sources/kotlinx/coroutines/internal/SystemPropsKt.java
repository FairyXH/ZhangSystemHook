package kotlinx.coroutines.internal;

/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"kotlinx/coroutines/internal/SystemPropsKt__SystemPropsKt", "kotlinx/coroutines/internal/SystemPropsKt__SystemProps_commonKt"}, k = 4, mv = {1, 9, 0}, xi = 48)
public final class SystemPropsKt {
    public static final int getAVAILABLE_PROCESSORS() {
        return kotlinx.coroutines.internal.SystemPropsKt__SystemPropsKt.getAVAILABLE_PROCESSORS();
    }

    public static final int systemProp(java.lang.String propertyName, int defaultValue, int minValue, int maxValue) {
        return kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt.systemProp(propertyName, defaultValue, minValue, maxValue);
    }

    public static final long systemProp(java.lang.String propertyName, long defaultValue, long minValue, long maxValue) {
        return kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt.systemProp(propertyName, defaultValue, minValue, maxValue);
    }

    public static final java.lang.String systemProp(java.lang.String propertyName) {
        return kotlinx.coroutines.internal.SystemPropsKt__SystemPropsKt.systemProp(propertyName);
    }

    public static final java.lang.String systemProp(java.lang.String propertyName, java.lang.String defaultValue) {
        return kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt.systemProp(propertyName, defaultValue);
    }

    public static final boolean systemProp(java.lang.String propertyName, boolean defaultValue) {
        return kotlinx.coroutines.internal.SystemPropsKt__SystemProps_commonKt.systemProp(propertyName, defaultValue);
    }
}
