package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
class AudioManagerShellCommand extends android.os.ShellCommand {
    private static final java.lang.String TAG = "AudioManagerShellCommand";
    private final com.android.server.audio.AudioService mService;

    AudioManagerShellCommand(com.android.server.audio.AudioService service) {
        this.mService = service;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00c3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r4) {
        /*
            Method dump skipped, instruction units count: 372
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.AudioManagerShellCommand.onCommand(java.lang.String):int");
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Audio manager commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println();
        pw.println("  set-surround-format-enabled SURROUND_FORMAT IS_ENABLED");
        pw.println("    Enables/disabled the SURROUND_FORMAT based on IS_ENABLED");
        pw.println("  get-is-surround-format-enabled SURROUND_FORMAT");
        pw.println("    Returns if the SURROUND_FORMAT is enabled");
        pw.println("  set-encoded-surround-mode SURROUND_SOUND_MODE");
        pw.println("    Sets the encoded surround sound mode to SURROUND_SOUND_MODE");
        pw.println("  get-encoded-surround-mode");
        pw.println("    Returns the encoded surround sound mode");
        pw.println("  set-sound-dose-value");
        pw.println("    Sets the current sound dose value");
        pw.println("  get-sound-dose-value");
        pw.println("    Returns the current sound dose value");
        pw.println("  reset-sound-dose-timeout");
        pw.println("    Resets the sound dose timeout used for momentary exposure");
        pw.println("  set-ringer-mode NORMAL|SILENT|VIBRATE");
        pw.println("    Sets the Ringer mode to one of NORMAL|SILENT|VIBRATE");
        pw.println("  set-volume STREAM_TYPE VOLUME_INDEX");
        pw.println("    Sets the volume for STREAM_TYPE to VOLUME_INDEX");
        pw.println("  set-device-volume STREAM_TYPE VOLUME_INDEX NATIVE_DEVICE_TYPE");
        pw.println("    Sets for NATIVE_DEVICE_TYPE the STREAM_TYPE volume to VOLUME_INDEX");
        pw.println("  adj-mute STREAM_TYPE");
        pw.println("    mutes the STREAM_TYPE");
        pw.println("  adj-unmute STREAM_TYPE");
        pw.println("    unmutes the STREAM_TYPE");
        pw.println("  adj-volume STREAM_TYPE <RAISE|LOWER|MUTE|UNMUTE>");
        pw.println("    Adjusts the STREAM_TYPE volume given the specified direction");
        pw.println("  set-group-volume GROUP_ID VOLUME_INDEX");
        pw.println("    Sets the volume for GROUP_ID to VOLUME_INDEX");
        pw.println("  adj-group-volume GROUP_ID <RAISE|LOWER|MUTE|UNMUTE>");
        pw.println("    Adjusts the group volume for GROUP_ID given the specified direction");
    }

    private int setSurroundFormatEnabled() {
        java.lang.String surroundFormatText = getNextArg();
        java.lang.String isSurroundFormatEnabledText = getNextArg();
        if (surroundFormatText == null) {
            getErrPrintWriter().println("Error: no surroundFormat specified");
            return 1;
        }
        if (isSurroundFormatEnabledText == null) {
            getErrPrintWriter().println("Error: no enabled value for surroundFormat specified");
            return 1;
        }
        try {
            int surroundFormat = java.lang.Integer.parseInt(surroundFormatText);
            boolean isSurroundFormatEnabled = java.lang.Boolean.parseBoolean(isSurroundFormatEnabledText);
            if (surroundFormat < 0) {
                getErrPrintWriter().println("Error: invalid value of surroundFormat");
                return 1;
            }
            android.content.Context context = this.mService.mContext;
            android.media.AudioManager am = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
            am.setSurroundFormatEnabled(surroundFormat, isSurroundFormatEnabled);
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: wrong format specified for surroundFormat");
            return 1;
        }
    }

    private int setRingerMode() {
        java.lang.String ringerModeText = getNextArg();
        if (ringerModeText == null) {
            getErrPrintWriter().println("Error: no ringer mode specified");
            return 1;
        }
        int ringerMode = getRingerMode(ringerModeText);
        if (!android.media.AudioManager.isValidRingerMode(ringerMode)) {
            getErrPrintWriter().println("Error: invalid value of ringerMode, should be one of NORMAL|SILENT|VIBRATE");
            return 1;
        }
        android.media.AudioManager am = (android.media.AudioManager) this.mService.mContext.getSystemService(android.media.AudioManager.class);
        am.setRingerModeInternal(ringerMode);
        return 0;
    }

    private int getRingerMode(java.lang.String ringerModeText) {
        byte b;
        switch (ringerModeText.hashCode()) {
            case -1986416409:
                b = !ringerModeText.equals(com.android.server.utils.PriorityDump.PRIORITY_ARG_NORMAL) ? (byte) -1 : (byte) 0;
                break;
            case -1848997803:
                b = !ringerModeText.equals("SILENT") ? (byte) -1 : (byte) 2;
                break;
            case 1169293647:
                b = !ringerModeText.equals("VIBRATE") ? (byte) -1 : (byte) 1;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return 2;
            case 1:
                return 1;
            case 2:
                return 0;
            default:
                return -1;
        }
    }

    private int getIsSurroundFormatEnabled() {
        java.lang.String surroundFormatText = getNextArg();
        if (surroundFormatText == null) {
            getErrPrintWriter().println("Error: no surroundFormat specified");
            return 1;
        }
        try {
            int surroundFormat = java.lang.Integer.parseInt(surroundFormatText);
            if (surroundFormat < 0) {
                getErrPrintWriter().println("Error: invalid value of surroundFormat");
                return 1;
            }
            android.content.Context context = this.mService.mContext;
            android.media.AudioManager am = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
            getOutPrintWriter().println("Value of enabled for " + surroundFormat + " is: " + am.isSurroundFormatEnabled(surroundFormat));
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: wrong format specified for surroundFormat");
            return 1;
        }
    }

    private int setEncodedSurroundMode() {
        java.lang.String encodedSurroundModeText = getNextArg();
        if (encodedSurroundModeText == null) {
            getErrPrintWriter().println("Error: no encodedSurroundMode specified");
            return 1;
        }
        try {
            int encodedSurroundMode = java.lang.Integer.parseInt(encodedSurroundModeText);
            if (encodedSurroundMode < 0) {
                getErrPrintWriter().println("Error: invalid value of encodedSurroundMode");
                return 1;
            }
            android.content.Context context = this.mService.mContext;
            android.media.AudioManager am = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
            am.setEncodedSurroundMode(encodedSurroundMode);
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: wrong format specified for encoded surround mode");
            return 1;
        }
    }

    private int getEncodedSurroundMode() {
        android.content.Context context = this.mService.mContext;
        android.media.AudioManager am = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
        getOutPrintWriter().println("Encoded surround mode: " + am.getEncodedSurroundMode());
        return 0;
    }

    private int setSoundDoseValue() {
        java.lang.String soundDoseValueText = getNextArg();
        if (soundDoseValueText == null) {
            getErrPrintWriter().println("Error: no sound dose value specified");
            return 1;
        }
        try {
            float soundDoseValue = java.lang.Float.parseFloat(soundDoseValueText);
            if (soundDoseValue < 0.0f) {
                getErrPrintWriter().println("Error: invalid value of sound dose");
                return 1;
            }
            android.content.Context context = this.mService.mContext;
            android.media.AudioManager am = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
            am.setCsd(soundDoseValue);
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: wrong format specified for sound dose");
            return 1;
        }
    }

    private int getSoundDoseValue() {
        android.content.Context context = this.mService.mContext;
        android.media.AudioManager am = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
        getOutPrintWriter().println("Sound dose value: " + am.getCsd());
        return 0;
    }

    private int resetSoundDoseTimeout() {
        android.content.Context context = this.mService.mContext;
        android.media.AudioManager am = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
        am.setCsd(-1.0f);
        getOutPrintWriter().println("Reset sound dose momentary exposure timeout");
        return 0;
    }

    private int setVolume() {
        android.content.Context context = this.mService.mContext;
        android.media.AudioManager am = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
        int stream = readIntArg();
        int index = readIntArg();
        getOutPrintWriter().println("calling AudioManager.setStreamVolume(" + stream + ", " + index + ", 0)");
        am.setStreamVolume(stream, index, 0);
        return 0;
    }

    private int setDeviceVolume() {
        android.content.Context context = this.mService.mContext;
        android.media.AudioDeviceVolumeManager advm = (android.media.AudioDeviceVolumeManager) context.getSystemService("audio_device_volume");
        int stream = readIntArg();
        int index = readIntArg();
        int device = readIntArg();
        android.media.VolumeInfo volume = new android.media.VolumeInfo.Builder(stream).setVolumeIndex(index).build();
        android.media.AudioDeviceAttributes ada = new android.media.AudioDeviceAttributes(device, "foo");
        getOutPrintWriter().println("calling AudioDeviceVolumeManager.setDeviceVolume(" + volume + ", " + ada + ")");
        advm.setDeviceVolume(volume, ada);
        return 0;
    }

    private int adjMute() {
        android.content.Context context = this.mService.mContext;
        android.media.AudioManager am = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
        int stream = readIntArg();
        getOutPrintWriter().println("calling AudioManager.adjustStreamVolume(" + stream + ", AudioManager.ADJUST_MUTE, 0)");
        am.adjustStreamVolume(stream, -100, 0);
        return 0;
    }

    private int adjUnmute() {
        android.content.Context context = this.mService.mContext;
        android.media.AudioManager am = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
        int stream = readIntArg();
        getOutPrintWriter().println("calling AudioManager.adjustStreamVolume(" + stream + ", AudioManager.ADJUST_UNMUTE, 0)");
        am.adjustStreamVolume(stream, 100, 0);
        return 0;
    }

    private int adjVolume() {
        android.content.Context context = this.mService.mContext;
        android.media.AudioManager am = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
        int stream = readIntArg();
        int direction = readDirectionArg();
        getOutPrintWriter().println("calling AudioManager.adjustStreamVolume(" + stream + ", " + direction + ", 0)");
        am.adjustStreamVolume(stream, direction, 0);
        return 0;
    }

    private int setGroupVolume() {
        android.content.Context context = this.mService.mContext;
        android.media.AudioManager am = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
        int groupId = readIntArg();
        int index = readIntArg();
        getOutPrintWriter().println("calling AudioManager.setVolumeGroupVolumeIndex(" + groupId + ", " + index + ", 0)");
        am.setVolumeGroupVolumeIndex(groupId, index, 0);
        return 0;
    }

    private int adjGroupVolume() {
        android.content.Context context = this.mService.mContext;
        android.media.AudioManager am = (android.media.AudioManager) context.getSystemService(android.media.AudioManager.class);
        int groupId = readIntArg();
        int direction = readDirectionArg();
        getOutPrintWriter().println("calling AudioManager.adjustVolumeGroupVolume(" + groupId + ", " + direction + ", 0)");
        am.adjustVolumeGroupVolume(groupId, direction, 0);
        return 0;
    }

    private int readIntArg() throws java.lang.IllegalArgumentException {
        java.lang.String argText = getNextArg();
        if (argText == null) {
            getErrPrintWriter().println("Error: no argument provided");
            throw new java.lang.IllegalArgumentException("No argument provided");
        }
        try {
            int argIntVal = java.lang.Integer.parseInt(argText);
            return argIntVal;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: wrong format for argument " + argText);
            throw new java.lang.IllegalArgumentException("Wrong format for argument " + argText);
        }
    }

    private int readDirectionArg() throws java.lang.IllegalArgumentException {
        byte b;
        java.lang.String argText = getNextArg();
        if (argText == null) {
            getErrPrintWriter().println("Error: no argument provided");
            throw new java.lang.IllegalArgumentException("No argument provided");
        }
        switch (argText.hashCode()) {
            case -1787076558:
                b = !argText.equals("UNMUTE") ? (byte) -1 : (byte) 3;
                break;
            case 2378265:
                b = !argText.equals("MUTE") ? (byte) -1 : (byte) 2;
                break;
            case 72626913:
                b = !argText.equals("LOWER") ? (byte) -1 : (byte) 1;
                break;
            case 77737932:
                b = !argText.equals("RAISE") ? (byte) -1 : (byte) 0;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return 1;
            case 1:
                return -1;
            case 2:
                return -100;
            case 3:
                return 100;
            default:
                throw new java.lang.IllegalArgumentException("Wrong direction argument: " + argText);
        }
    }
}
