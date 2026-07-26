package kotlin.internal;

/* JADX INFO: compiled from: PlatformImplementations.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0003\u001a \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u0005H\u0001\u001a\"\u0010\b\u001a\u0002H\t\"\n\b\u0000\u0010\t\u0018\u0001*\u00020\n2\u0006\u0010\u000b\u001a\u00020\nH\u0083\b¢\u0006\u0002\u0010\f\"\u0010\u0010\u0000\u001a\u00020\u00018\u0000X\u0081\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"IMPLEMENTATIONS", "Lkotlin/internal/PlatformImplementations;", "apiVersionIsAtLeast", "", "major", "", "minor", "patch", "castToBaseType", "T", "", "instance", "(Ljava/lang/Object;)Ljava/lang/Object;", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class PlatformImplementationsKt {
    public static final kotlin.internal.PlatformImplementations IMPLEMENTATIONS;

    static {
        kotlin.internal.jdk8.JDK8PlatformImplementations jDK8PlatformImplementations = new kotlin.internal.jdk8.JDK8PlatformImplementations();
        try {
            IMPLEMENTATIONS = jDK8PlatformImplementations;
        } catch (java.lang.ClassCastException e) {
            java.lang.ClassLoader classLoader = jDK8PlatformImplementations.getClass().getClassLoader();
            java.lang.ClassLoader classLoader2 = kotlin.internal.PlatformImplementations.class.getClassLoader();
            if (!kotlin.jvm.internal.Intrinsics.areEqual(classLoader, classLoader2)) {
                throw new java.lang.ClassNotFoundException("Instance class was loaded from a different classloader: " + classLoader + ", base type classloader: " + classLoader2, e);
            }
            throw e;
        }
    }

    private static final /* synthetic */ <T> T castToBaseType(java.lang.Object obj) throws java.lang.ClassNotFoundException {
        try {
            kotlin.jvm.internal.Intrinsics.reifiedOperationMarker(1, "T");
            return (T) obj;
        } catch (java.lang.ClassCastException e) {
            java.lang.ClassLoader classLoader = obj.getClass().getClassLoader();
            kotlin.jvm.internal.Intrinsics.reifiedOperationMarker(4, "T");
            java.lang.ClassLoader classLoader2 = java.lang.Object.class.getClassLoader();
            if (!kotlin.jvm.internal.Intrinsics.areEqual(classLoader, classLoader2)) {
                throw new java.lang.ClassNotFoundException("Instance class was loaded from a different classloader: " + classLoader + ", base type classloader: " + classLoader2, e);
            }
            throw e;
        }
    }

    public static final boolean apiVersionIsAtLeast(int major, int minor, int patch) {
        return kotlin.KotlinVersion.CURRENT.isAtLeast(major, minor, patch);
    }
}
