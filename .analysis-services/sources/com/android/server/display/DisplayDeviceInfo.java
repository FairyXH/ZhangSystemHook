package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
final class DisplayDeviceInfo {
    public static final int DIFF_COLOR_MODE = 8;
    public static final int DIFF_COMMITTED_STATE = 4;
    public static final int DIFF_EVERYTHING = -1;
    public static final int DIFF_HDR_SDR_RATIO = 16;
    public static final int DIFF_MODE_ID = 128;
    public static final int DIFF_OTHER = 1;
    public static final int DIFF_RENDER_TIMINGS = 64;
    public static final int DIFF_ROTATION = 32;
    public static final int DIFF_STATE = 2;
    public static final int FLAG_ALLOWED_TO_BE_DEFAULT_DISPLAY = 1;
    public static final int FLAG_ALWAYS_UNLOCKED = 32768;
    public static final int FLAG_CAN_SHOW_WITH_INSECURE_KEYGUARD = 512;
    public static final int FLAG_DESTROY_CONTENT_ON_REMOVAL = 1024;
    public static final int FLAG_DEVICE_DISPLAY_GROUP = 262144;
    public static final int FLAG_MASK_DISPLAY_CUTOUT = 2048;
    public static final int FLAG_NEVER_BLANK = 32;
    public static final int FLAG_OWN_CONTENT_ONLY = 128;
    public static final int FLAG_OWN_DISPLAY_GROUP = 16384;
    public static final int FLAG_OWN_FOCUS = 131072;
    public static final int FLAG_PRESENTATION = 64;
    public static final int FLAG_PRIVATE = 16;
    public static final int FLAG_ROTATES_WITH_CONTENT = 2;
    public static final int FLAG_ROUND = 256;
    public static final int FLAG_SECURE = 4;
    public static final int FLAG_SHOULD_SHOW_SYSTEM_DECORATIONS = 4096;
    public static final int FLAG_STEAL_TOP_FOCUS_DISABLED = 524288;
    public static final int FLAG_SUPPORTS_PROTECTED_BUFFERS = 8;
    public static final int FLAG_TOUCH_FEEDBACK_DISABLED = 65536;
    public static final int FLAG_TRUSTED = 8192;
    public static final int TOUCH_EXTERNAL = 2;
    public static final int TOUCH_INTERNAL = 1;
    public static final int TOUCH_NONE = 0;
    public static final int TOUCH_VIRTUAL = 3;
    public android.view.DisplayAddress address;
    public boolean allmSupported;
    public long appVsyncOffsetNanos;
    public float brightnessDefault;
    public float brightnessMaximum;
    public float brightnessMinimum;
    public int colorMode;
    public int defaultModeId;
    public int densityDpi;
    public android.hardware.display.DeviceProductInfo deviceProductInfo;
    public android.view.DisplayCutout displayCutout;
    public android.view.DisplayShape displayShape;
    public int flags;
    public boolean gameContentTypeSupported;
    public android.view.Display.HdrCapabilities hdrCapabilities;
    public int height;
    public int modeId;
    public java.lang.String name;
    public java.lang.String ownerPackageName;
    public int ownerUid;
    public long presentationDeadlineNanos;
    public float renderFrameRate;
    public android.view.RoundedCorners roundedCorners;
    public int touch;
    public int type;
    public java.lang.String uniqueId;
    public int width;
    public float xDpi;
    public float yDpi;
    public int userPreferredModeId = -1;
    public android.view.Display.Mode[] supportedModes = android.view.Display.Mode.EMPTY_ARRAY;
    public int[] supportedColorModes = {0};
    public int rotation = 0;
    public int state = 2;
    public int committedState = 0;
    public android.view.DisplayEventReceiver.FrameRateOverride[] frameRateOverrides = new android.view.DisplayEventReceiver.FrameRateOverride[0];
    public float hdrSdrRatio = Float.NaN;
    public int backlightType = 0;
    public int installOrientation = 0;

    DisplayDeviceInfo() {
    }

    public void setAssumedDensityForExternalDisplay(int width, int height) {
        this.densityDpi = (java.lang.Math.min(width, height) * 320) / 1080;
        this.xDpi = this.densityDpi;
        this.yDpi = this.densityDpi;
    }

    public boolean equals(java.lang.Object o) {
        return (o instanceof com.android.server.display.DisplayDeviceInfo) && equals((com.android.server.display.DisplayDeviceInfo) o);
    }

    public boolean equals(com.android.server.display.DisplayDeviceInfo other) {
        return other != null && diff(other) == 0;
    }

    public int diff(com.android.server.display.DisplayDeviceInfo other) {
        int diff = 0;
        if (this.state != other.state) {
            diff = 0 | 2;
        }
        if (this.committedState != other.committedState) {
            diff |= 4;
        }
        if (this.colorMode != other.colorMode) {
            diff |= 8;
        }
        if (!com.android.internal.display.BrightnessSynchronizer.floatEquals(this.hdrSdrRatio, other.hdrSdrRatio)) {
            diff |= 16;
        }
        if (this.rotation != other.rotation) {
            diff |= 32;
        }
        if (this.renderFrameRate != other.renderFrameRate || this.presentationDeadlineNanos != other.presentationDeadlineNanos || this.appVsyncOffsetNanos != other.appVsyncOffsetNanos) {
            diff |= 64;
        }
        if (this.modeId != other.modeId) {
            diff |= 128;
        }
        if (!java.util.Objects.equals(this.name, other.name) || !java.util.Objects.equals(this.uniqueId, other.uniqueId) || this.width != other.width || this.height != other.height || this.defaultModeId != other.defaultModeId || this.userPreferredModeId != other.userPreferredModeId || !java.util.Arrays.equals(this.supportedModes, other.supportedModes) || !java.util.Arrays.equals(this.supportedColorModes, other.supportedColorModes) || !java.util.Objects.equals(this.hdrCapabilities, other.hdrCapabilities) || this.allmSupported != other.allmSupported || this.gameContentTypeSupported != other.gameContentTypeSupported || this.densityDpi != other.densityDpi || this.xDpi != other.xDpi || this.yDpi != other.yDpi || this.flags != other.flags || !java.util.Objects.equals(this.displayCutout, other.displayCutout) || this.touch != other.touch || this.type != other.type || !java.util.Objects.equals(this.address, other.address) || !java.util.Objects.equals(this.deviceProductInfo, other.deviceProductInfo) || this.ownerUid != other.ownerUid || !java.util.Objects.equals(this.ownerPackageName, other.ownerPackageName) || !java.util.Arrays.equals(this.frameRateOverrides, other.frameRateOverrides) || !com.android.internal.display.BrightnessSynchronizer.floatEquals(this.brightnessMinimum, other.brightnessMinimum) || !com.android.internal.display.BrightnessSynchronizer.floatEquals(this.brightnessMaximum, other.brightnessMaximum) || !com.android.internal.display.BrightnessSynchronizer.floatEquals(this.brightnessDefault, other.brightnessDefault) || !java.util.Objects.equals(this.roundedCorners, other.roundedCorners) || this.installOrientation != other.installOrientation || !java.util.Objects.equals(this.displayShape, other.displayShape)) {
            return diff | 1;
        }
        return diff;
    }

    public int hashCode() {
        return 0;
    }

    public void copyFrom(com.android.server.display.DisplayDeviceInfo other) {
        this.name = other.name;
        this.uniqueId = other.uniqueId;
        this.width = other.width;
        this.height = other.height;
        this.modeId = other.modeId;
        this.renderFrameRate = other.renderFrameRate;
        this.defaultModeId = other.defaultModeId;
        this.userPreferredModeId = other.userPreferredModeId;
        this.supportedModes = other.supportedModes;
        this.colorMode = other.colorMode;
        this.supportedColorModes = other.supportedColorModes;
        this.hdrCapabilities = other.hdrCapabilities;
        this.allmSupported = other.allmSupported;
        this.gameContentTypeSupported = other.gameContentTypeSupported;
        this.densityDpi = other.densityDpi;
        this.xDpi = other.xDpi;
        this.yDpi = other.yDpi;
        this.appVsyncOffsetNanos = other.appVsyncOffsetNanos;
        this.presentationDeadlineNanos = other.presentationDeadlineNanos;
        this.flags = other.flags;
        this.displayCutout = other.displayCutout;
        this.touch = other.touch;
        this.rotation = other.rotation;
        this.type = other.type;
        this.address = other.address;
        this.deviceProductInfo = other.deviceProductInfo;
        this.state = other.state;
        this.committedState = other.committedState;
        this.ownerUid = other.ownerUid;
        this.ownerPackageName = other.ownerPackageName;
        this.frameRateOverrides = other.frameRateOverrides;
        this.brightnessMinimum = other.brightnessMinimum;
        this.brightnessMaximum = other.brightnessMaximum;
        this.brightnessDefault = other.brightnessDefault;
        this.hdrSdrRatio = other.hdrSdrRatio;
        this.backlightType = other.backlightType;
        this.roundedCorners = other.roundedCorners;
        this.installOrientation = other.installOrientation;
        this.displayShape = other.displayShape;
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("DisplayDeviceInfo{\"");
        sb.append(this.name).append("\": uniqueId=\"").append(this.uniqueId).append("\", ");
        sb.append(this.width).append(" x ").append(this.height);
        sb.append(", modeId ").append(this.modeId);
        sb.append(", renderFrameRate ").append(this.renderFrameRate);
        sb.append(", defaultModeId ").append(this.defaultModeId);
        sb.append(", userPreferredModeId ").append(this.userPreferredModeId);
        sb.append(", supportedModes ").append(java.util.Arrays.toString(this.supportedModes));
        sb.append(", colorMode ").append(this.colorMode);
        sb.append(", supportedColorModes ").append(java.util.Arrays.toString(this.supportedColorModes));
        sb.append(", hdrCapabilities ").append(this.hdrCapabilities);
        sb.append(", allmSupported ").append(this.allmSupported);
        sb.append(", gameContentTypeSupported ").append(this.gameContentTypeSupported);
        sb.append(", density ").append(this.densityDpi);
        sb.append(", ").append(this.xDpi).append(" x ").append(this.yDpi).append(" dpi");
        sb.append(", appVsyncOff ").append(this.appVsyncOffsetNanos);
        sb.append(", presDeadline ").append(this.presentationDeadlineNanos);
        if (this.displayCutout != null) {
            sb.append(", cutout ").append(this.displayCutout);
        }
        sb.append(", touch ").append(touchToString(this.touch));
        sb.append(", rotation ").append(this.rotation);
        sb.append(", type ").append(android.view.Display.typeToString(this.type));
        if (this.address != null) {
            sb.append(", address ").append(this.address);
        }
        sb.append(", deviceProductInfo ").append(this.deviceProductInfo);
        sb.append(", state ").append(android.view.Display.stateToString(this.state));
        sb.append(", committedState ").append(android.view.Display.stateToString(this.committedState));
        if (this.ownerUid != 0 || this.ownerPackageName != null) {
            sb.append(", owner ").append(this.ownerPackageName);
            sb.append(" (uid ").append(this.ownerUid).append(")");
        }
        sb.append(", frameRateOverride ");
        for (android.view.DisplayEventReceiver.FrameRateOverride frameRateOverride : this.frameRateOverrides) {
            sb.append(frameRateOverride).append(" ");
        }
        sb.append(", brightnessMinimum ").append(this.brightnessMinimum);
        sb.append(", brightnessMaximum ").append(this.brightnessMaximum);
        sb.append(", brightnessDefault ").append(this.brightnessDefault);
        sb.append(", hdrSdrRatio ").append(this.hdrSdrRatio);
        sb.append(", backlightType ").append(this.backlightType);
        if (this.roundedCorners != null) {
            sb.append(", roundedCorners ").append(this.roundedCorners);
        }
        sb.append(flagsToString(this.flags));
        sb.append(", installOrientation ").append(this.installOrientation);
        if (this.displayShape != null) {
            sb.append(", displayShape ").append(this.displayShape);
        }
        sb.append("}");
        return sb.toString();
    }

    private static java.lang.String touchToString(int touch) {
        switch (touch) {
            case 0:
                return "NONE";
            case 1:
                return "INTERNAL";
            case 2:
                return "EXTERNAL";
            case 3:
                return "VIRTUAL";
            default:
                return java.lang.Integer.toString(touch);
        }
    }

    private static java.lang.String flagsToString(int flags) {
        java.lang.StringBuilder msg = new java.lang.StringBuilder();
        if ((flags & 1) != 0) {
            msg.append(", FLAG_ALLOWED_TO_BE_DEFAULT_DISPLAY");
        }
        if ((flags & 2) != 0) {
            msg.append(", FLAG_ROTATES_WITH_CONTENT");
        }
        if ((flags & 4) != 0) {
            msg.append(", FLAG_SECURE");
        }
        if ((flags & 8) != 0) {
            msg.append(", FLAG_SUPPORTS_PROTECTED_BUFFERS");
        }
        if ((flags & 16) != 0) {
            msg.append(", FLAG_PRIVATE");
        }
        if ((flags & 32) != 0) {
            msg.append(", FLAG_NEVER_BLANK");
        }
        if ((flags & 64) != 0) {
            msg.append(", FLAG_PRESENTATION");
        }
        if ((flags & 128) != 0) {
            msg.append(", FLAG_OWN_CONTENT_ONLY");
        }
        if ((flags & 256) != 0) {
            msg.append(", FLAG_ROUND");
        }
        if ((flags & 512) != 0) {
            msg.append(", FLAG_CAN_SHOW_WITH_INSECURE_KEYGUARD");
        }
        if ((flags & 1024) != 0) {
            msg.append(", FLAG_DESTROY_CONTENT_ON_REMOVAL");
        }
        if ((flags & 2048) != 0) {
            msg.append(", FLAG_MASK_DISPLAY_CUTOUT");
        }
        if ((flags & 4096) != 0) {
            msg.append(", FLAG_SHOULD_SHOW_SYSTEM_DECORATIONS");
        }
        if ((flags & 8192) != 0) {
            msg.append(", FLAG_TRUSTED");
        }
        if ((flags & 16384) != 0) {
            msg.append(", FLAG_OWN_DISPLAY_GROUP");
        }
        if ((32768 & flags) != 0) {
            msg.append(", FLAG_ALWAYS_UNLOCKED");
        }
        if ((65536 & flags) != 0) {
            msg.append(", FLAG_TOUCH_FEEDBACK_DISABLED");
        }
        if ((131072 & flags) != 0) {
            msg.append(", FLAG_OWN_FOCUS");
        }
        if ((524288 & flags) != 0) {
            msg.append(", FLAG_STEAL_TOP_FOCUS_DISABLED");
        }
        return msg.toString();
    }
}
