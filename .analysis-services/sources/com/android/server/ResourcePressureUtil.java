package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public final class ResourcePressureUtil {
    private static final java.util.List<java.lang.String> PSI_FILES = java.util.Arrays.asList("/proc/pressure/memory", "/proc/pressure/cpu", "/proc/pressure/io");
    private static final java.lang.String PSI_ROOT = "/proc/pressure";
    private static final java.lang.String TAG = "ResourcePressureUtil";

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String readResourcePsiState(java.lang.String filePath) {
        java.io.StringWriter contents = new java.io.StringWriter();
        try {
            if (new java.io.File(filePath).exists()) {
                contents.append((java.lang.CharSequence) ("----- Output from " + filePath + " -----\n"));
                contents.append((java.lang.CharSequence) libcore.io.IoUtils.readFileAsString(filePath));
                contents.append((java.lang.CharSequence) ("----- End output from " + filePath + " -----\n"));
            }
        } catch (java.io.IOException e) {
            android.util.Slog.e(TAG, " could not read " + filePath, e);
        }
        return contents.toString();
    }

    public static java.lang.String currentPsiState() {
        android.os.StrictMode.ThreadPolicy savedPolicy = android.os.StrictMode.allowThreadDiskReads();
        final java.io.StringWriter aggregatedState = new java.io.StringWriter();
        try {
            java.util.stream.Stream<R> map = PSI_FILES.stream().map(new java.util.function.Function() { // from class: com.android.server.ResourcePressureUtil$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return com.android.server.ResourcePressureUtil.readResourcePsiState((java.lang.String) obj);
                }
            });
            java.util.Objects.requireNonNull(aggregatedState);
            map.forEach(new java.util.function.Consumer() { // from class: com.android.server.ResourcePressureUtil$$ExternalSyntheticLambda1
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    aggregatedState.append((java.lang.CharSequence) obj);
                }
            });
            android.os.StrictMode.setThreadPolicy(savedPolicy);
            java.lang.String psiState = aggregatedState.toString();
            return psiState.length() > 0 ? psiState + "\n" : psiState;
        } catch (java.lang.Throwable th) {
            android.os.StrictMode.setThreadPolicy(savedPolicy);
            throw th;
        }
    }

    private ResourcePressureUtil() {
    }
}
