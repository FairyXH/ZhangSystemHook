package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
public class DisplayControl {
    private static native android.os.IBinder nativeCreateVirtualDisplay(java.lang.String str, boolean z, java.lang.String str2, float f);

    private static native void nativeDestroyVirtualDisplay(android.os.IBinder iBinder);

    private static native boolean nativeGetHdrOutputConversionSupport();

    private static native int[] nativeGetHdrOutputTypesWithLatency();

    private static native long[] nativeGetPhysicalDisplayIds();

    private static native android.os.IBinder nativeGetPhysicalDisplayToken(long j);

    private static native int[] nativeGetSupportedHdrOutputTypes();

    private static native void nativeOverrideHdrTypes(android.os.IBinder iBinder, int[] iArr);

    private static native int nativeSetHdrConversionMode(int i, int i2, int[] iArr, int i3);

    public static android.os.IBinder createVirtualDisplay(java.lang.String name, boolean secure) {
        java.util.Objects.requireNonNull(name, "name must not be null");
        return nativeCreateVirtualDisplay(name, secure, "", 0.0f);
    }

    public static android.os.IBinder createVirtualDisplay(java.lang.String name, boolean secure, java.lang.String uniqueId, float requestedRefreshRate) {
        java.util.Objects.requireNonNull(name, "name must not be null");
        java.util.Objects.requireNonNull(uniqueId, "uniqueId must not be null");
        return nativeCreateVirtualDisplay(name, secure, uniqueId, requestedRefreshRate);
    }

    public static void destroyVirtualDisplay(android.os.IBinder displayToken) {
        if (displayToken == null) {
            throw new java.lang.IllegalArgumentException("displayToken must not be null");
        }
        nativeDestroyVirtualDisplay(displayToken);
    }

    public static void overrideHdrTypes(android.os.IBinder displayToken, int[] modes) {
        nativeOverrideHdrTypes(displayToken, modes);
    }

    public static long[] getPhysicalDisplayIds() {
        return nativeGetPhysicalDisplayIds();
    }

    public static android.os.IBinder getPhysicalDisplayToken(long physicalDisplayId) {
        return nativeGetPhysicalDisplayToken(physicalDisplayId);
    }

    public static int setHdrConversionMode(int conversionMode, int preferredHdrOutputType, int[] autoHdrTypes) {
        int length = autoHdrTypes != null ? autoHdrTypes.length : 0;
        return nativeSetHdrConversionMode(conversionMode, preferredHdrOutputType, autoHdrTypes, length);
    }

    public static int[] getSupportedHdrOutputTypes() {
        return nativeGetSupportedHdrOutputTypes();
    }

    public static int[] getHdrOutputTypesWithLatency() {
        return nativeGetHdrOutputTypesWithLatency();
    }

    public static boolean getHdrOutputConversionSupport() {
        return nativeGetHdrOutputConversionSupport();
    }
}
