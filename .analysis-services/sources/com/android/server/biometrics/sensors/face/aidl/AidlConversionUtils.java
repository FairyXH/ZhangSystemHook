package com.android.server.biometrics.sensors.face.aidl;

/* JADX INFO: loaded from: classes.dex */
public final class AidlConversionUtils {
    private static final byte FACE_ACQUIRED_AUTH_SUCCESS = 114;
    private static final byte FACE_ACQUIRED_CAMERA_PREVIEW = 119;
    private static final byte FACE_ACQUIRED_ENROLL_KEEP = 113;
    private static final byte FACE_ACQUIRED_ENROLL_RESET = 115;
    private static final byte FACE_ACQUIRED_ENROLL_SUCCESS = 112;
    private static final byte FACE_ACQUIRED_HKER = 104;
    private static final byte FACE_ACQUIRED_NO_FACE = 101;
    private static final byte FACE_ACQUIRED_UPDATE_MODE = 116;
    private static final byte FACE_ACQUIRED_UPDATE_MODE_ALL_FAIL = 118;
    private static final byte FACE_ACQUIRED_UPDATE_MODE_PARTITAL_FAIL = 117;
    private static final byte PALMPRINT_ACQUIRED_AUTH_SUCCESS = 54;
    private static final byte PALMPRINT_ACQUIRED_CAMERA_PREVIEW = 59;
    private static final byte PALMPRINT_ACQUIRED_ENROLL_KEEP = 53;
    private static final byte PALMPRINT_ACQUIRED_ENROLL_RESET = 55;
    private static final byte PALMPRINT_ACQUIRED_ENROLL_SUCCESS = 52;
    private static final byte PALMPRINT_ACQUIRED_GOOD = 61;
    private static final byte PALMPRINT_ACQUIRED_HACKER = 44;
    private static final byte PALMPRINT_ACQUIRED_INSUFFICIENT = 62;
    private static final byte PALMPRINT_ACQUIRED_NOT_DETECTED = 72;
    private static final byte PALMPRINT_ACQUIRED_NO_FACE = 41;
    private static final byte PALMPRINT_ACQUIRED_POOR_GAZE = 71;
    private static final byte PALMPRINT_ACQUIRED_SENSOR_DIRTY = 82;
    private static final byte PALMPRINT_ACQUIRED_TOO_BRIGHT = 63;
    private static final byte PALMPRINT_ACQUIRED_TOO_CLOSE = 65;
    private static final byte PALMPRINT_ACQUIRED_TOO_DARK = 64;
    private static final byte PALMPRINT_ACQUIRED_TOO_DIFFERENT = 75;
    private static final byte PALMPRINT_ACQUIRED_TOO_FAR = 66;
    private static final byte PALMPRINT_ACQUIRED_TOO_HIGH = 67;
    private static final byte PALMPRINT_ACQUIRED_TOO_LEFT = 70;
    private static final byte PALMPRINT_ACQUIRED_TOO_LOW = 68;
    private static final byte PALMPRINT_ACQUIRED_TOO_MUCH_MOTION = 73;
    private static final byte PALMPRINT_ACQUIRED_TOO_RIGHT = 69;
    private static final byte PALMPRINT_ACQUIRED_UPDATE_MODE = 56;
    private static final byte PALMPRINT_ACQUIRED_UPDATE_MODE_ALL_FAIL = 58;
    private static final byte PALMPRINT_ACQUIRED_UPDATE_MODE_PARTITAL_FAIL = 57;
    private static final java.lang.String TAG = "AidlConversionUtils";

    private AidlConversionUtils() {
    }

    public static int toFrameworkError(byte aidlError) {
        switch (aidlError) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 8;
            case 8:
                return 16;
            default:
                return 17;
        }
    }

    public static int toFrameworkAcquiredInfo(byte aidlAcquiredInfo) {
        switch (aidlAcquiredInfo) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
            case 8:
                return 7;
            case 9:
                return 8;
            case 10:
                return 9;
            case 11:
                return 10;
            case 12:
                return 11;
            case 13:
                return 12;
            case 14:
                return 13;
            case 15:
                return 14;
            case 16:
                return 15;
            case 17:
                return 16;
            case 18:
                return 17;
            case 19:
                return 18;
            case 20:
                return 19;
            case 21:
                return 20;
            case 22:
                return 21;
            case 23:
                return 22;
            case 24:
                return 24;
            case 25:
                return 25;
            case 26:
                return 26;
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 42:
            case 43:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 60:
            case 74:
            case 76:
            case 77:
            case 78:
            case 79:
            case 80:
            case 81:
            case 83:
            case 84:
            case 85:
            case 86:
            case 87:
            case 88:
            case 89:
            case 90:
            case 91:
            case 92:
            case 93:
            case 94:
            case 95:
            case 96:
            case 97:
            case 98:
            case 99:
            case 100:
            case 102:
            case 103:
            case 105:
            case 106:
            case 107:
            case 108:
            case 109:
            case 110:
            case 111:
            default:
                return 23;
            case 41:
                return 41;
            case 44:
                return 44;
            case 52:
                return 52;
            case 53:
                return 53;
            case 54:
                return 54;
            case 55:
                return 55;
            case 56:
                return 56;
            case 57:
                return 57;
            case 58:
                return 58;
            case 59:
                return 59;
            case 61:
                return 61;
            case 62:
                return 62;
            case 63:
                return 63;
            case 64:
                return 64;
            case 65:
                return 65;
            case 66:
                return 66;
            case 67:
                return 67;
            case 68:
                return 68;
            case 69:
                return 69;
            case 70:
                return 70;
            case 71:
                return 71;
            case 72:
                return 72;
            case 73:
                return 73;
            case 75:
                return 75;
            case 82:
                return 82;
            case 101:
                return 101;
            case 104:
                return 104;
            case 112:
                return 112;
            case 113:
                return 113;
            case 114:
                return 114;
            case 115:
                return 115;
            case 116:
                return 116;
            case 117:
                return 117;
            case 118:
                return 118;
            case 119:
                return 119;
        }
    }

    public static int toFrameworkEnrollmentStage(int aidlEnrollmentStage) {
        switch (aidlEnrollmentStage) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            default:
                return 0;
        }
    }

    public static android.hardware.face.FaceAuthenticationFrame toFrameworkAuthenticationFrame(android.hardware.biometrics.face.AuthenticationFrame frame) {
        return new android.hardware.face.FaceAuthenticationFrame(toFrameworkBaseFrame(frame.data));
    }

    public static android.hardware.face.FaceEnrollFrame toFrameworkEnrollmentFrame(android.hardware.biometrics.face.EnrollmentFrame frame) {
        return new android.hardware.face.FaceEnrollFrame(toFrameworkCell(frame.cell), toFrameworkEnrollmentStage(frame.stage), toFrameworkBaseFrame(frame.data));
    }

    public static android.hardware.face.FaceDataFrame toFrameworkBaseFrame(android.hardware.biometrics.face.BaseFrame frame) {
        return new android.hardware.face.FaceDataFrame(toFrameworkAcquiredInfo(frame.acquiredInfo), frame.vendorCode, frame.pan, frame.tilt, frame.distance, frame.isCancellable);
    }

    public static android.hardware.face.FaceEnrollCell toFrameworkCell(android.hardware.biometrics.face.Cell cell) {
        if (cell == null) {
            return null;
        }
        return new android.hardware.face.FaceEnrollCell(cell.x, cell.y, cell.z);
    }

    public static byte convertFrameworkToAidlFeature(int feature) throws java.lang.IllegalArgumentException {
        switch (feature) {
            case 1:
                return (byte) 0;
            case 2:
                return (byte) 1;
            default:
                android.util.Slog.e(TAG, "Unsupported feature : " + feature);
                throw new java.lang.IllegalArgumentException();
        }
    }

    public static int convertAidlToFrameworkFeature(byte feature) throws java.lang.IllegalArgumentException {
        switch (feature) {
            case 0:
                return 1;
            case 1:
                return 2;
            default:
                android.util.Slog.e(TAG, "Unsupported feature : " + ((int) feature));
                throw new java.lang.IllegalArgumentException();
        }
    }
}
