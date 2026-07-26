package com.android.server.display.brightness;

/* JADX INFO: loaded from: classes2.dex */
public final class BrightnessReason {
    public static final int ADJUSTMENT_AUTO = 2;
    public static final int ADJUSTMENT_AUTO_TEMP = 1;
    public static final int MODIFIER_DIMMED = 1;
    public static final int MODIFIER_HDR = 4;
    public static final int MODIFIER_LOW_POWER = 2;
    public static final int MODIFIER_MASK = 63;
    public static final int MODIFIER_MIN_LUX = 16;
    public static final int MODIFIER_MIN_USER_SET_LOWER_BOUND = 32;
    public static final int MODIFIER_THROTTLED = 8;
    public static final int REASON_AUTOMATIC = 4;
    public static final int REASON_BOOST = 8;
    public static final int REASON_DOZE = 2;
    public static final int REASON_DOZE_DEFAULT = 3;
    public static final int REASON_DOZE_MANUAL = 12;
    public static final int REASON_FOLLOWER = 10;
    public static final int REASON_MANUAL = 1;
    public static final int REASON_MAX = 12;
    public static final int REASON_OFFLOAD = 11;
    public static final int REASON_OVERRIDE = 6;
    public static final int REASON_SCREEN_OFF = 5;
    public static final int REASON_SCREEN_OFF_BRIGHTNESS_SENSOR = 9;
    public static final int REASON_TEMPORARY = 7;
    public static final int REASON_UNKNOWN = 0;
    private static final java.lang.String TAG = "BrightnessReason";
    private int mModifier;
    private int mReason;

    public void set(com.android.server.display.brightness.BrightnessReason other) {
        setReason(other == null ? 0 : other.mReason);
        setModifier(other != null ? other.mModifier : 0);
    }

    public void addModifier(int modifier) {
        setModifier(this.mModifier | modifier);
    }

    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof com.android.server.display.brightness.BrightnessReason)) {
            return false;
        }
        com.android.server.display.brightness.BrightnessReason other = (com.android.server.display.brightness.BrightnessReason) obj;
        return other.mReason == this.mReason && other.mModifier == this.mModifier;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(this.mReason), java.lang.Integer.valueOf(this.mModifier));
    }

    public java.lang.String toString() {
        return toString(0);
    }

    public java.lang.String toString(int adjustments) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(reasonToString(this.mReason));
        sb.append(" [");
        if ((adjustments & 1) != 0) {
            sb.append(" temp_adj");
        }
        if ((adjustments & 2) != 0) {
            sb.append(" auto_adj");
        }
        if ((this.mModifier & 2) != 0) {
            sb.append(" low_pwr");
        }
        if ((this.mModifier & 1) != 0) {
            sb.append(" dim");
        }
        if ((this.mModifier & 4) != 0) {
            sb.append(" hdr");
        }
        if ((this.mModifier & 8) != 0) {
            sb.append(" throttled");
        }
        if ((this.mModifier & 16) != 0) {
            sb.append(" lux_lower_bound");
        }
        if ((this.mModifier & 32) != 0) {
            sb.append(" user_min_pref");
        }
        int strlen = sb.length();
        if (sb.charAt(strlen - 1) == '[') {
            sb.setLength(strlen - 2);
        } else {
            sb.append(" ]");
        }
        return sb.toString();
    }

    public void setReason(int reason) {
        if (reason < 0 || reason > 12) {
            android.util.Slog.w(TAG, "brightness reason out of bounds: " + reason);
        } else {
            this.mReason = reason;
        }
    }

    public int getReason() {
        return this.mReason;
    }

    public int getModifier() {
        return this.mModifier;
    }

    public void setModifier(int modifier) {
        if ((modifier & (-64)) != 0) {
            android.util.Slog.w(TAG, "brightness modifier out of bounds: 0x" + java.lang.Integer.toHexString(modifier));
        } else {
            this.mModifier = modifier;
        }
    }

    private java.lang.String reasonToString(int reason) {
        switch (reason) {
            case 1:
                return "manual";
            case 2:
                return "doze";
            case 3:
                return "doze_default";
            case 4:
                return "automatic";
            case 5:
                return "screen_off";
            case 6:
                return "override";
            case 7:
                return "temporary";
            case 8:
                return "boost";
            case 9:
                return "screen_off_brightness_sensor";
            case 10:
                return "follower";
            case 11:
                return "offload";
            case 12:
                return "doze_manual";
            default:
                return java.lang.Integer.toString(reason);
        }
    }
}
