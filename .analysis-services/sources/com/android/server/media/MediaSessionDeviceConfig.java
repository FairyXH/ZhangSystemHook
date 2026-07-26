package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
class MediaSessionDeviceConfig {
    private static final long DEFAULT_MEDIA_BUTTON_RECEIVER_FGS_ALLOWLIST_DURATION_MS = 10000;
    private static final long DEFAULT_MEDIA_SESSION_CALLBACK_FGS_ALLOWLIST_DURATION_MS = 10000;
    private static final long DEFAULT_MEDIA_SESSION_CALLBACK_FGS_WHILE_IN_USE_TEMP_ALLOW_DURATION_MS = 10000;
    private static final java.lang.String KEY_MEDIA_BUTTON_RECEIVER_FGS_ALLOWLIST_DURATION_MS = "media_button_receiver_fgs_allowlist_duration_ms";
    private static final java.lang.String KEY_MEDIA_SESSION_CALLBACK_FGS_ALLOWLIST_DURATION_MS = "media_session_calback_fgs_allowlist_duration_ms";
    private static final java.lang.String KEY_MEDIA_SESSION_CALLBACK_FGS_WHILE_IN_USE_TEMP_ALLOW_DURATION_MS = "media_session_callback_fgs_while_in_use_temp_allow_duration_ms";
    private static volatile long sMediaButtonReceiverFgsAllowlistDurationMs = 10000;
    private static volatile long sMediaSessionCallbackFgsAllowlistDurationMs = 10000;
    private static volatile long sMediaSessionCallbackFgsWhileInUseTempAllowDurationMs = 10000;

    MediaSessionDeviceConfig() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void refresh(final android.provider.DeviceConfig.Properties properties) {
        properties.getKeyset();
        properties.getKeyset().forEach(new java.util.function.Consumer() { // from class: com.android.server.media.MediaSessionDeviceConfig$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                com.android.server.media.MediaSessionDeviceConfig.lambda$refresh$0(properties, (java.lang.String) obj);
            }
        });
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static /* synthetic */ void lambda$refresh$0(android.provider.DeviceConfig.Properties r3, java.lang.String r4) {
        /*
            int r0 = r4.hashCode()
            switch(r0) {
                case -1976080914: goto L1e;
                case -1060130895: goto L13;
                case 1803361950: goto L8;
                default: goto L7;
            }
        L7:
            goto L29
        L8:
            java.lang.String r0 = "media_session_calback_fgs_allowlist_duration_ms"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L7
            r0 = 1
            goto L2a
        L13:
            java.lang.String r0 = "media_session_callback_fgs_while_in_use_temp_allow_duration_ms"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L7
            r0 = 2
            goto L2a
        L1e:
            java.lang.String r0 = "media_button_receiver_fgs_allowlist_duration_ms"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L7
            r0 = 0
            goto L2a
        L29:
            r0 = -1
        L2a:
            r1 = 10000(0x2710, double:4.9407E-320)
            switch(r0) {
                case 0: goto L3e;
                case 1: goto L37;
                case 2: goto L30;
                default: goto L2f;
            }
        L2f:
            goto L45
        L30:
            long r0 = r3.getLong(r4, r1)
            com.android.server.media.MediaSessionDeviceConfig.sMediaSessionCallbackFgsWhileInUseTempAllowDurationMs = r0
            goto L45
        L37:
            long r0 = r3.getLong(r4, r1)
            com.android.server.media.MediaSessionDeviceConfig.sMediaSessionCallbackFgsAllowlistDurationMs = r0
            goto L45
        L3e:
            long r0 = r3.getLong(r4, r1)
            com.android.server.media.MediaSessionDeviceConfig.sMediaButtonReceiverFgsAllowlistDurationMs = r0
        L45:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.media.MediaSessionDeviceConfig.lambda$refresh$0(android.provider.DeviceConfig$Properties, java.lang.String):void");
    }

    public static void initialize(android.content.Context context) {
        android.provider.DeviceConfig.addOnPropertiesChangedListener("media", context.getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.media.MediaSessionDeviceConfig$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                com.android.server.media.MediaSessionDeviceConfig.refresh(properties);
            }
        });
        refresh(android.provider.DeviceConfig.getProperties("media", new java.lang.String[0]));
    }

    public static long getMediaButtonReceiverFgsAllowlistDurationMs() {
        return sMediaButtonReceiverFgsAllowlistDurationMs;
    }

    public static long getMediaSessionCallbackFgsAllowlistDurationMs() {
        return sMediaSessionCallbackFgsAllowlistDurationMs;
    }

    public static long getMediaSessionCallbackFgsWhileInUseTempAllowDurationMs() {
        return sMediaSessionCallbackFgsWhileInUseTempAllowDurationMs;
    }

    public static void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println("Media session config:");
        java.lang.String dumpFormat = prefix + "  %s: [cur: %s, def: %s]";
        pw.println(android.text.TextUtils.formatSimple(dumpFormat, new java.lang.Object[]{KEY_MEDIA_BUTTON_RECEIVER_FGS_ALLOWLIST_DURATION_MS, java.lang.Long.valueOf(sMediaButtonReceiverFgsAllowlistDurationMs), 10000L}));
        pw.println(android.text.TextUtils.formatSimple(dumpFormat, new java.lang.Object[]{KEY_MEDIA_SESSION_CALLBACK_FGS_ALLOWLIST_DURATION_MS, java.lang.Long.valueOf(sMediaSessionCallbackFgsAllowlistDurationMs), 10000L}));
        pw.println(android.text.TextUtils.formatSimple(dumpFormat, new java.lang.Object[]{KEY_MEDIA_SESSION_CALLBACK_FGS_WHILE_IN_USE_TEMP_ALLOW_DURATION_MS, java.lang.Long.valueOf(sMediaSessionCallbackFgsWhileInUseTempAllowDurationMs), 10000L}));
    }
}
