package com.android.server.biometrics.sensors.fingerprint.aidl;

/* JADX INFO: loaded from: classes.dex */
final class AidlConversionUtils {
    private static final int FINGERPRINT_ACQUIRED_ALREADY_ENROLLED = 1002;
    private static final byte FINGERPRINT_ACQUIRED_ALREADY_ENROLLED_AIDL = 102;
    private static final int FINGERPRINT_ACQUIRED_ENROLL_SLIDE_DIRTY = 1114;
    private static final byte FINGERPRINT_ACQUIRED_ENROLL_SLIDE_DIRTY_AIDL = 114;
    private static final int FINGERPRINT_ACQUIRED_ENROLL_SLIDE_FASTUP_LIFT = 1110;
    private static final byte FINGERPRINT_ACQUIRED_ENROLL_SLIDE_FASTUP_LIFT_AIDL = 110;
    private static final int FINGERPRINT_ACQUIRED_ENROLL_SLIDE_MOVE_FAST = 1111;
    private static final byte FINGERPRINT_ACQUIRED_ENROLL_SLIDE_MOVE_FAST_AIDL = 111;
    private static final int FINGERPRINT_ACQUIRED_ENROLL_SLIDE_MOVE_SLOW = 1112;
    private static final byte FINGERPRINT_ACQUIRED_ENROLL_SLIDE_MOVE_SLOW_AIDL = 112;
    private static final int FINGERPRINT_ACQUIRED_ENROLL_SLIDE_PARTIAL = 1115;
    private static final byte FINGERPRINT_ACQUIRED_ENROLL_SLIDE_PARTIAL_AIDL = 115;
    private static final int FINGERPRINT_ACQUIRED_ENROLL_SLIDE_REDUNDANT = 1116;
    private static final byte FINGERPRINT_ACQUIRED_ENROLL_SLIDE_REDUNDANT_AIDL = 116;
    private static final int FINGERPRINT_ACQUIRED_ENROLL_SLIDE_TOUCH_MOVE = 1113;
    private static final byte FINGERPRINT_ACQUIRED_ENROLL_SLIDE_TOUCH_MOVE_AIDL = 113;
    private static final byte FINGERPRINT_ACQUIRED_INCOMPAT_FILM_DETECTED_AIDL = 103;
    private static final int FINGERPRINT_ACQUIRED_TOO_SIMILAR = 1001;
    private static final byte FINGERPRINT_ACQUIRED_TOO_SIMILAR_AIDL = 101;

    private AidlConversionUtils() {
    }

    public static int toFrameworkError(byte aidlError) {
        if (aidlError == 0) {
            return 17;
        }
        if (aidlError == 1) {
            return 1;
        }
        if (aidlError == 2) {
            return 2;
        }
        if (aidlError == 3) {
            return 3;
        }
        if (aidlError == 4) {
            return 4;
        }
        if (aidlError == 5) {
            return 5;
        }
        if (aidlError == 6) {
            return 6;
        }
        if (aidlError == 7) {
            return 8;
        }
        if (aidlError == 8) {
            return 18;
        }
        if (aidlError != 9) {
            return 17;
        }
        return 19;
    }

    public static int toFrameworkAcquiredInfo(byte aidlAcquiredInfo) {
        if (aidlAcquiredInfo == 0) {
            return 8;
        }
        if (aidlAcquiredInfo == 1) {
            return 0;
        }
        if (aidlAcquiredInfo == 2) {
            return 1;
        }
        if (aidlAcquiredInfo == 3) {
            return 2;
        }
        if (aidlAcquiredInfo == 4) {
            return 3;
        }
        if (aidlAcquiredInfo == 5) {
            return 4;
        }
        if (aidlAcquiredInfo == 6) {
            return 5;
        }
        if (aidlAcquiredInfo == 7) {
            return 6;
        }
        if (aidlAcquiredInfo == 8) {
            return 7;
        }
        if (aidlAcquiredInfo == 9) {
            return 8;
        }
        if (aidlAcquiredInfo == 10) {
            return 10;
        }
        if (aidlAcquiredInfo == 11) {
            return 9;
        }
        if (aidlAcquiredInfo == 12) {
            return 8;
        }
        if (aidlAcquiredInfo == 14) {
            return 11;
        }
        if (aidlAcquiredInfo == 101) {
            return 1001;
        }
        if (aidlAcquiredInfo == 102) {
            return 1002;
        }
        if (aidlAcquiredInfo == 103) {
            return 103;
        }
        if (aidlAcquiredInfo == 110) {
            return FINGERPRINT_ACQUIRED_ENROLL_SLIDE_FASTUP_LIFT;
        }
        if (aidlAcquiredInfo == 111) {
            return FINGERPRINT_ACQUIRED_ENROLL_SLIDE_MOVE_FAST;
        }
        if (aidlAcquiredInfo == 112) {
            return FINGERPRINT_ACQUIRED_ENROLL_SLIDE_MOVE_SLOW;
        }
        if (aidlAcquiredInfo == 113) {
            return FINGERPRINT_ACQUIRED_ENROLL_SLIDE_TOUCH_MOVE;
        }
        if (aidlAcquiredInfo == 114) {
            return FINGERPRINT_ACQUIRED_ENROLL_SLIDE_DIRTY;
        }
        if (aidlAcquiredInfo == 115) {
            return FINGERPRINT_ACQUIRED_ENROLL_SLIDE_PARTIAL;
        }
        if (aidlAcquiredInfo != 116) {
            return 8;
        }
        return FINGERPRINT_ACQUIRED_ENROLL_SLIDE_REDUNDANT;
    }
}
