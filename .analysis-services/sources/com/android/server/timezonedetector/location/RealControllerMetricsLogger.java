package com.android.server.timezonedetector.location;

/* JADX INFO: loaded from: classes3.dex */
final class RealControllerMetricsLogger implements com.android.server.timezonedetector.location.LocationTimeZoneProviderController.MetricsLogger {
    RealControllerMetricsLogger() {
    }

    @Override // com.android.server.timezonedetector.location.LocationTimeZoneProviderController.MetricsLogger
    public void onStateChange(java.lang.String state) {
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.LOCATION_TIME_ZONE_PROVIDER_CONTROLLER_STATE_CHANGED, metricsState(state));
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0060  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static int metricsState(java.lang.String r9) {
        /*
            int r0 = r9.hashCode()
            r1 = 0
            r2 = 7
            r3 = 6
            r4 = 5
            r5 = 4
            r6 = 3
            r7 = 2
            r8 = 1
            switch(r0) {
                case -1166336595: goto L56;
                case -468307734: goto L4c;
                case 433141802: goto L42;
                case 478389753: goto L38;
                case 872357833: goto L2e;
                case 1386911874: goto L24;
                case 1917201485: goto L1a;
                case 2066319421: goto L10;
                default: goto Lf;
            }
        Lf:
            goto L60
        L10:
            java.lang.String r0 = "FAILED"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r3
            goto L61
        L1a:
            java.lang.String r0 = "INITIALIZING"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r7
            goto L61
        L24:
            java.lang.String r0 = "CERTAIN"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r6
            goto L61
        L2e:
            java.lang.String r0 = "UNCERTAIN"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r5
            goto L61
        L38:
            java.lang.String r0 = "DESTROYED"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r4
            goto L61
        L42:
            java.lang.String r0 = "UNKNOWN"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r2
            goto L61
        L4c:
            java.lang.String r0 = "PROVIDERS_INITIALIZING"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r1
            goto L61
        L56:
            java.lang.String r0 = "STOPPED"
            boolean r0 = r9.equals(r0)
            if (r0 == 0) goto Lf
            r0 = r8
            goto L61
        L60:
            r0 = -1
        L61:
            switch(r0) {
                case 0: goto L6b;
                case 1: goto L6a;
                case 2: goto L69;
                case 3: goto L68;
                case 4: goto L67;
                case 5: goto L66;
                case 6: goto L65;
                default: goto L64;
            }
        L64:
            return r1
        L65:
            return r2
        L66:
            return r3
        L67:
            return r4
        L68:
            return r5
        L69:
            return r6
        L6a:
            return r7
        L6b:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.timezonedetector.location.RealControllerMetricsLogger.metricsState(java.lang.String):int");
    }
}
