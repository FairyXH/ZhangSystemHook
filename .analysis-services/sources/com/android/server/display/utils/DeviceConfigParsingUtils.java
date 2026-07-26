package com.android.server.display.utils;

/* JADX INFO: loaded from: classes2.dex */
public class DeviceConfigParsingUtils {
    private static final java.lang.String TAG = "DeviceConfigParsingUtils";

    /* JADX WARN: Code restructure failed: missing block: B:27:0x00cd, code lost:
    
        r19 = r3;
        r7 = r23.apply(r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00d9, code lost:
    
        if (r7 != null) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00db, code lost:
    
        android.util.Slog.e(com.android.server.display.utils.DeviceConfigParsingUtils.TAG, "Invalid dataSetMapped dataPoints=" + r14 + ",dataSet=" + r5, new java.lang.Throwable());
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0102, code lost:
    
        return java.util.Map.of();
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0104, code lost:
    
        if (r11 >= r6.length) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0106, code lost:
    
        r0 = r6[r11];
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0109, code lost:
    
        r0 = "default";
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x010b, code lost:
    
        r10 = r0.computeIfAbsent(r8, new com.android.server.display.utils.DeviceConfigParsingUtils$$ExternalSyntheticLambda0());
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x011a, code lost:
    
        if (r10.put(r0, r7) == null) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x011c, code lost:
    
        android.util.Slog.e(com.android.server.display.utils.DeviceConfigParsingUtils.TAG, "Duplicate dataSetId=" + r0 + ",data=" + r21, new java.lang.Throwable());
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0145, code lost:
    
        return java.util.Map.of();
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0146, code lost:
    
        r4 = r4 + 1;
        r0 = r16;
        r3 = r19;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static <T, V> java.util.Map<java.lang.String, java.util.Map<java.lang.String, V>> parseDeviceConfigMap(java.lang.String r21, java.util.function.BiFunction<java.lang.String, java.lang.String, T> r22, java.util.function.Function<java.util.List<T>, V> r23) {
        /*
            Method dump skipped, instruction units count: 413
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.utils.DeviceConfigParsingUtils.parseDeviceConfigMap(java.lang.String, java.util.function.BiFunction, java.util.function.Function):java.util.Map");
    }

    static /* synthetic */ java.util.Map lambda$parseDeviceConfigMap$0(java.lang.String k) {
        return new java.util.HashMap();
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:26:0x005a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int parseThermalStatus(java.lang.String r8) throws java.lang.IllegalArgumentException {
        /*
            int r0 = r8.hashCode()
            r1 = 6
            r2 = 5
            r3 = 4
            r4 = 3
            r5 = 2
            r6 = 1
            r7 = 0
            switch(r0) {
                case -905723276: goto L4f;
                case -618857213: goto L44;
                case -169343402: goto L39;
                case 3387192: goto L2e;
                case 102970646: goto L23;
                case 1629013393: goto L19;
                case 1952151455: goto Lf;
                default: goto Le;
            }
        Le:
            goto L5a
        Lf:
            java.lang.String r0 = "critical"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto Le
            r0 = r3
            goto L5b
        L19:
            java.lang.String r0 = "emergency"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto Le
            r0 = r2
            goto L5b
        L23:
            java.lang.String r0 = "light"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto Le
            r0 = r6
            goto L5b
        L2e:
            java.lang.String r0 = "none"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto Le
            r0 = r7
            goto L5b
        L39:
            java.lang.String r0 = "shutdown"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto Le
            r0 = r1
            goto L5b
        L44:
            java.lang.String r0 = "moderate"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto Le
            r0 = r5
            goto L5b
        L4f:
            java.lang.String r0 = "severe"
            boolean r0 = r8.equals(r0)
            if (r0 == 0) goto Le
            r0 = r4
            goto L5b
        L5a:
            r0 = -1
        L5b:
            switch(r0) {
                case 0: goto L7d;
                case 1: goto L7c;
                case 2: goto L7b;
                case 3: goto L7a;
                case 4: goto L79;
                case 5: goto L78;
                case 6: goto L77;
                default: goto L5e;
            }
        L5e:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Invalid Thermal Status: "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r8)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L77:
            return r1
        L78:
            return r2
        L79:
            return r3
        L7a:
            return r4
        L7b:
            return r5
        L7c:
            return r6
        L7d:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.display.utils.DeviceConfigParsingUtils.parseThermalStatus(java.lang.String):int");
    }

    public static float parseBrightness(java.lang.String stringVal) throws java.lang.IllegalArgumentException {
        float value = java.lang.Float.parseFloat(stringVal);
        if (value < 0.0f || value > 1.0f) {
            throw new java.lang.IllegalArgumentException("Brightness value out of bounds: " + stringVal);
        }
        return value;
    }

    public static float[] displayBrightnessThresholdsIntToFloat(int[] thresholdsInt) {
        if (thresholdsInt == null) {
            return null;
        }
        float[] thresholds = new float[thresholdsInt.length];
        for (int i = 0; i < thresholds.length; i++) {
            if (thresholdsInt[i] < 0) {
                thresholds[i] = thresholdsInt[i];
            } else {
                thresholds[i] = com.android.internal.display.BrightnessSynchronizer.brightnessIntToFloat(thresholdsInt[i]);
            }
        }
        return thresholds;
    }

    public static float[] ambientBrightnessThresholdsIntToFloat(int[] thresholdsInt) {
        if (thresholdsInt == null) {
            return null;
        }
        float[] thresholds = new float[thresholdsInt.length];
        for (int i = 0; i < thresholds.length; i++) {
            thresholds[i] = thresholdsInt[i];
        }
        return thresholds;
    }
}
