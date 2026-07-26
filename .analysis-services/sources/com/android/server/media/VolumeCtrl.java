package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
public class VolumeCtrl {
    private static final java.lang.String ADJUST_LOWER = "lower";
    private static final java.lang.String ADJUST_RAISE = "raise";
    private static final java.lang.String ADJUST_SAME = "same";
    private static final java.lang.String LOG_E = "[E]";
    private static final java.lang.String LOG_V = "[V]";
    private static final java.lang.String TAG = "VolumeCtrl";
    public static final java.lang.String USAGE = new java.lang.String("the options are as follows: \n\t\t--stream STREAM selects the stream to control, see AudioManager.STREAM_*\n\t\t                controls AudioManager.STREAM_MUSIC if no stream is specified\n\t\t--set INDEX     sets the volume index value\n\t\t--adj DIRECTION adjusts the volume, use raise|same|lower for the direction\n\t\t--get           outputs the current volume\n\t\t--show          shows the UI during the volume change\n\texamples:\n\t\tadb shell media volume --show --stream 3 --set 11\n\t\tadb shell media volume --stream 0 --adj lower\n\t\tadb shell media volume --stream 3 --get\n");
    private static final int VOLUME_CONTROL_MODE_ADJUST = 2;
    private static final int VOLUME_CONTROL_MODE_SET = 1;

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:23:0x004e  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0116  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void run(com.android.server.media.MediaShellCommand r17) throws java.lang.Exception {
        /*
            Method dump skipped, instruction units count: 574
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.media.VolumeCtrl.run(com.android.server.media.MediaShellCommand):void");
    }

    static java.lang.String streamName(int stream) {
        try {
            return android.media.AudioSystem.STREAM_NAMES[stream];
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            return "invalid stream";
        }
    }
}
