package com.android.server.hdmi;

/* JADX INFO: loaded from: classes2.dex */
public class HdmiCecMessageValidator {
    static final int ADDR_ALL = 65535;
    static final int ADDR_AUDIO_SYSTEM = 32;
    static final int ADDR_BACKUP_1 = 4096;
    static final int ADDR_BACKUP_2 = 8192;
    static final int ADDR_BROADCAST = 32768;
    static final int ADDR_DIRECT = 32767;
    static final int ADDR_NOT_UNREGISTERED = 32767;
    static final int ADDR_PLAYBACK_1 = 16;
    static final int ADDR_PLAYBACK_2 = 256;
    static final int ADDR_PLAYBACK_3 = 2048;
    static final int ADDR_RECORDER_1 = 2;
    static final int ADDR_RECORDER_2 = 4;
    static final int ADDR_RECORDER_3 = 512;
    static final int ADDR_SPECIFIC_USE = 16384;
    static final int ADDR_TUNER_1 = 8;
    static final int ADDR_TUNER_2 = 64;
    static final int ADDR_TUNER_3 = 128;
    static final int ADDR_TUNER_4 = 1024;
    static final int ADDR_TV = 1;
    static final int ADDR_UNREGISTERED = 32768;
    static final int ERROR_DESTINATION = 2;
    static final int ERROR_PARAMETER = 3;
    static final int ERROR_PARAMETER_LONG = 5;
    static final int ERROR_PARAMETER_SHORT = 4;
    static final int ERROR_SOURCE = 1;
    static final int OK = 0;
    private static final java.lang.String TAG = "HdmiCecMessageValidator";
    private static final android.util.SparseArray<com.android.server.hdmi.HdmiCecMessageValidator.ValidationInfo> sValidationInfo = new android.util.SparseArray<>();

    interface ParameterValidator {
        int isValid(byte[] bArr);
    }

    public @interface ValidationResult {
    }

    private static class ValidationInfo {
        public final com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator parameterValidator;
        public final int validDestinations;
        public final int validSources;

        ValidationInfo(com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator parameterValidator, int validSources, int validDestinations) {
            this.parameterValidator = parameterValidator;
            this.validSources = validSources;
            this.validDestinations = validDestinations;
        }
    }

    private HdmiCecMessageValidator() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    static {
        com.android.server.hdmi.HdmiCecMessageValidator.PhysicalAddressValidator physicalAddressValidator = new com.android.server.hdmi.HdmiCecMessageValidator.PhysicalAddressValidator();
        addValidationInfo(130, physicalAddressValidator, 65503, 32768);
        addValidationInfo(157, physicalAddressValidator, 32767, 32767);
        addValidationInfo(132, new com.android.server.hdmi.HdmiCecMessageValidator.ReportPhysicalAddressValidator(), 65535, 32768);
        addValidationInfo(128, new com.android.server.hdmi.HdmiCecMessageValidator.RoutingChangeValidator(), 65535, 32768);
        addValidationInfo(129, physicalAddressValidator, 65535, 32768);
        addValidationInfo(134, physicalAddressValidator, 32767, 32768);
        addValidationInfo(112, new com.android.server.hdmi.HdmiCecMessageValidator.SystemAudioModeRequestValidator(), 32767, 32767);
        com.android.server.hdmi.HdmiCecMessageValidator.FixedLengthValidator fixedLengthValidator = new com.android.server.hdmi.HdmiCecMessageValidator.FixedLengthValidator(0);
        addValidationInfo(255, fixedLengthValidator, 32767, 32767);
        addValidationInfo(159, fixedLengthValidator, 32767, 32767);
        addValidationInfo(145, fixedLengthValidator, 65535, 32767);
        addValidationInfo(113, fixedLengthValidator, 32767, 32767);
        addValidationInfo(143, fixedLengthValidator, 32767, 32767);
        addValidationInfo(140, fixedLengthValidator, 65535, 32767);
        addValidationInfo(70, fixedLengthValidator, 32767, 32767);
        addValidationInfo(131, fixedLengthValidator, 65535, 32767);
        addValidationInfo(125, fixedLengthValidator, 32767, 32767);
        addValidationInfo(4, fixedLengthValidator, 32767, 32767);
        addValidationInfo(192, fixedLengthValidator, 32767, 32767);
        addValidationInfo(11, fixedLengthValidator, 32767, 32767);
        addValidationInfo(15, fixedLengthValidator, 32767, 32767);
        addValidationInfo(193, fixedLengthValidator, 32767, 32767);
        addValidationInfo(194, fixedLengthValidator, 32767, 32767);
        addValidationInfo(195, fixedLengthValidator, 32767, 32767);
        addValidationInfo(196, fixedLengthValidator, 32767, 32767);
        addValidationInfo(133, fixedLengthValidator, 65535, 32768);
        addValidationInfo(54, fixedLengthValidator, 65535, 65535);
        addValidationInfo(197, fixedLengthValidator, 32767, 32767);
        addValidationInfo(13, fixedLengthValidator, 32767, 32767);
        addValidationInfo(6, fixedLengthValidator, 32767, 32767);
        addValidationInfo(5, fixedLengthValidator, 32767, 32767);
        addValidationInfo(69, fixedLengthValidator, 32767, 32767);
        addValidationInfo(139, fixedLengthValidator, 32767, 65535);
        addValidationInfo(9, new com.android.server.hdmi.HdmiCecMessageValidator.VariableLengthValidator(1, 8), 32767, 32767);
        addValidationInfo(10, new com.android.server.hdmi.HdmiCecMessageValidator.RecordStatusInfoValidator(), 32767, 32767);
        addValidationInfo(51, new com.android.server.hdmi.HdmiCecMessageValidator.AnalogueTimerValidator(), 32767, 32767);
        addValidationInfo(153, new com.android.server.hdmi.HdmiCecMessageValidator.DigitalTimerValidator(), 32767, 32767);
        addValidationInfo(161, new com.android.server.hdmi.HdmiCecMessageValidator.ExternalTimerValidator(), 32767, 32767);
        addValidationInfo(52, new com.android.server.hdmi.HdmiCecMessageValidator.AnalogueTimerValidator(), 32767, 32767);
        addValidationInfo(151, new com.android.server.hdmi.HdmiCecMessageValidator.DigitalTimerValidator(), 32767, 32767);
        addValidationInfo(162, new com.android.server.hdmi.HdmiCecMessageValidator.ExternalTimerValidator(), 32767, 32767);
        addValidationInfo(103, new com.android.server.hdmi.HdmiCecMessageValidator.AsciiValidator(1, 14), 32767, 32767);
        addValidationInfo(67, new com.android.server.hdmi.HdmiCecMessageValidator.TimerClearedStatusValidator(), 32767, 32767);
        addValidationInfo(53, new com.android.server.hdmi.HdmiCecMessageValidator.TimerStatusValidator(), 32767, 32767);
        com.android.server.hdmi.HdmiCecMessageValidator.FixedLengthValidator fixedLengthValidator2 = new com.android.server.hdmi.HdmiCecMessageValidator.FixedLengthValidator(1);
        addValidationInfo(158, fixedLengthValidator2, 32767, 32767);
        addValidationInfo(50, new com.android.server.hdmi.HdmiCecMessageValidator.AsciiValidator(3), 1, 32768);
        com.android.server.hdmi.HdmiCecMessageValidator.MinimumOneByteRangeValidator minimumOneByteRangeValidator = new com.android.server.hdmi.HdmiCecMessageValidator.MinimumOneByteRangeValidator(1, 3);
        addValidationInfo(66, new com.android.server.hdmi.HdmiCecMessageValidator.MinimumOneByteRangeValidator(1, 4), 32767, 32767);
        addValidationInfo(27, new com.android.server.hdmi.HdmiCecMessageValidator.MinimumOneByteRangeValidator(17, 31), 32767, 32767);
        addValidationInfo(26, minimumOneByteRangeValidator, 32767, 32767);
        addValidationInfo(65, new com.android.server.hdmi.HdmiCecMessageValidator.PlayModeValidator(), 32767, 32767);
        addValidationInfo(8, minimumOneByteRangeValidator, 32767, 32767);
        addValidationInfo(146, new com.android.server.hdmi.HdmiCecMessageValidator.SelectAnalogueServiceValidator(), 32767, 32767);
        addValidationInfo(147, new com.android.server.hdmi.HdmiCecMessageValidator.SelectDigitalServiceValidator(), 32767, 32767);
        addValidationInfo(7, new com.android.server.hdmi.HdmiCecMessageValidator.TunerDeviceStatusValidator(), 32767, 32767);
        com.android.server.hdmi.HdmiCecMessageValidator.VariableLengthValidator variableLengthValidator = new com.android.server.hdmi.HdmiCecMessageValidator.VariableLengthValidator(0, 14);
        addValidationInfo(135, new com.android.server.hdmi.HdmiCecMessageValidator.FixedLengthValidator(3), 32767, 32768);
        addValidationInfo(137, new com.android.server.hdmi.HdmiCecMessageValidator.VariableLengthValidator(1, 14), 65535, 32767);
        addValidationInfo(160, new com.android.server.hdmi.HdmiCecMessageValidator.VariableLengthValidator(4, 14), 65535, 65535);
        addValidationInfo(138, variableLengthValidator, 65535, 65535);
        addValidationInfo(100, new com.android.server.hdmi.HdmiCecMessageValidator.OsdStringValidator(), 32767, 32767);
        addValidationInfo(71, new com.android.server.hdmi.HdmiCecMessageValidator.AsciiValidator(1, 14), 32767, 32767);
        addValidationInfo(141, new com.android.server.hdmi.HdmiCecMessageValidator.MinimumOneByteRangeValidator(0, 2), 32767, 32767);
        addValidationInfo(142, new com.android.server.hdmi.HdmiCecMessageValidator.MinimumOneByteRangeValidator(0, 1), 32767, 32767);
        addValidationInfo(68, new com.android.server.hdmi.HdmiCecMessageValidator.UserControlPressedValidator(), 32767, 32767);
        addValidationInfo(144, new com.android.server.hdmi.HdmiCecMessageValidator.MinimumOneByteRangeValidator(0, 3), 32767, 65535);
        addValidationInfo(0, new com.android.server.hdmi.HdmiCecMessageValidator.FixedLengthValidator(2), 32767, 32767);
        addValidationInfo(122, fixedLengthValidator2, 32767, 32767);
        addValidationInfo(163, new com.android.server.hdmi.HdmiCecMessageValidator.FixedLengthValidator(3), 32767, 32767);
        addValidationInfo(164, fixedLengthValidator2, 32767, 32767);
        addValidationInfo(114, new com.android.server.hdmi.HdmiCecMessageValidator.SingleByteRangeValidator(0, 1), 32, 65535);
        addValidationInfo(126, new com.android.server.hdmi.HdmiCecMessageValidator.SingleByteRangeValidator(0, 1), 32767, 32767);
        addValidationInfo(154, new com.android.server.hdmi.HdmiCecMessageValidator.MinimumOneByteRangeValidator(0, 6), 32767, 32767);
        addValidationInfo(165, fixedLengthValidator, 65535, 32767);
        addValidationInfo(167, physicalAddressValidator, 32767, 32768);
        addValidationInfo(168, new com.android.server.hdmi.HdmiCecMessageValidator.VariableLengthValidator(4, 14), 32767, 32768);
        addValidationInfo(com.android.internal.util.FrameworkStatsLog.INTEGRITY_RULES_PUSHED, variableLengthValidator, 65535, 32768);
    }

    private static void addValidationInfo(int opcode, com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator validator, int validSources, int validDestinations) {
        sValidationInfo.append(opcode, new com.android.server.hdmi.HdmiCecMessageValidator.ValidationInfo(validator, validSources, validDestinations));
    }

    static int validate(int source, int destination, int opcode, byte[] params) {
        com.android.server.hdmi.HdmiCecMessageValidator.ValidationInfo info = sValidationInfo.get(opcode);
        if (info == null) {
            com.android.server.hdmi.HdmiLogger.warning("No validation information for the opcode: " + opcode, new java.lang.Object[0]);
            return 0;
        }
        int addressValidationResult = validateAddress(source, destination, info.validSources, info.validDestinations);
        if (addressValidationResult != 0) {
            return addressValidationResult;
        }
        int errorCode = info.parameterValidator.isValid(params);
        if (errorCode == 0) {
            return 0;
        }
        return errorCode;
    }

    static int validateAddress(int source, int destination, int validSources, int validDestinations) {
        if (((1 << source) & validSources) == 0) {
            return 1;
        }
        if (((1 << destination) & validDestinations) == 0) {
            return 2;
        }
        return 0;
    }

    private static class FixedLengthValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private final int mLength;

        public FixedLengthValidator(int length) {
            this.mLength = length;
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            return params.length < this.mLength ? 4 : 0;
        }
    }

    private static class VariableLengthValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private final int mMaxLength;
        private final int mMinLength;

        public VariableLengthValidator(int minLength, int maxLength) {
            this.mMinLength = minLength;
            this.mMaxLength = maxLength;
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            return params.length < this.mMinLength ? 4 : 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidPhysicalAddress(byte[] params, int offset) {
        int physicalAddress = com.android.server.hdmi.HdmiUtils.twoBytesToInt(params, offset);
        while (physicalAddress != 0) {
            int maskedAddress = 61440 & physicalAddress;
            physicalAddress = (physicalAddress << 4) & 65535;
            if (maskedAddress == 0 && physicalAddress != 0) {
                return false;
            }
        }
        return true;
    }

    static boolean isValidType(int type) {
        return type >= 0 && type <= 7 && type != 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int toErrorCode(boolean success) {
        return success ? 0 : 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isWithinRange(int value, int min, int max) {
        int value2 = value & 255;
        return value2 >= min && value2 <= max;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidDisplayControl(int value) {
        int value2 = value & 255;
        return value2 == 0 || value2 == 64 || value2 == 128 || value2 == 192;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidAsciiString(byte[] params, int offset, int maxLength) {
        for (int i = offset; i < params.length && i < maxLength; i++) {
            if (!isWithinRange(params[i], 32, 126)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidDayOfMonth(int value) {
        return isWithinRange(value, 1, 31);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidMonthOfYear(int value) {
        return isWithinRange(value, 1, 12);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidHour(int value) {
        return isWithinRange(value, 0, 23);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidMinute(int value) {
        return isWithinRange(value, 0, 59);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidDurationHours(int value) {
        return isWithinRange(value, 0, 99);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidRecordingSequence(int value) {
        int value2 = value & 255;
        return (value2 & 128) == 0 && java.lang.Integer.bitCount(value2) <= 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidAnalogueBroadcastType(int value) {
        return isWithinRange(value, 0, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidAnalogueFrequency(int value) {
        int value2 = value & 65535;
        return (value2 == 0 || value2 == 65535) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidBroadcastSystem(int value) {
        return isWithinRange(value, 0, 31);
    }

    private static boolean isAribDbs(int value) {
        return value == 0 || isWithinRange(value, 8, 10);
    }

    private static boolean isAtscDbs(int value) {
        return value == 1 || isWithinRange(value, 16, 18);
    }

    private static boolean isDvbDbs(int value) {
        return value == 2 || isWithinRange(value, 24, 27);
    }

    private static boolean isValidDigitalBroadcastSystem(int value) {
        return isAribDbs(value) || isAtscDbs(value) || isDvbDbs(value);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidChannelIdentifier(byte[] params, int offset) {
        int channelNumberFormat = params[offset] & android.hardware.audio.common.V2_0.AudioChannelMask.IN_6;
        return channelNumberFormat == 4 ? params.length - offset >= 3 : channelNumberFormat == 8 && params.length - offset >= 4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidDigitalServiceIdentification(byte[] params, int offset) {
        int serviceIdentificationMethod = params[offset] & 128;
        int digitalBroadcastSystem = params[offset] & 127;
        int offset2 = offset + 1;
        if (serviceIdentificationMethod == 0) {
            if (isAribDbs(digitalBroadcastSystem)) {
                if (params.length - offset2 >= 6) {
                    return true;
                }
                return false;
            }
            if (isAtscDbs(digitalBroadcastSystem)) {
                if (params.length - offset2 >= 4) {
                    return true;
                }
                return false;
            }
            if (isDvbDbs(digitalBroadcastSystem) && params.length - offset2 >= 6) {
                return true;
            }
            return false;
        }
        if (serviceIdentificationMethod == 128 && isValidDigitalBroadcastSystem(digitalBroadcastSystem)) {
            return isValidChannelIdentifier(params, offset2);
        }
        return false;
    }

    private static boolean isValidExternalPlug(int value) {
        return isWithinRange(value, 1, 255);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidExternalSource(byte[] params, int offset) {
        int externalSourceSpecifier = params[offset];
        int offset2 = offset + 1;
        if (externalSourceSpecifier == 4) {
            return isValidExternalPlug(params[offset2]);
        }
        if (externalSourceSpecifier == 5 && params.length - offset2 >= 2) {
            return isValidPhysicalAddress(params, offset2);
        }
        return false;
    }

    private static boolean isValidProgrammedInfo(int programedInfo) {
        return isWithinRange(programedInfo, 0, 11);
    }

    private static boolean isValidNotProgrammedErrorInfo(int nonProgramedErrorInfo) {
        return isWithinRange(nonProgramedErrorInfo, 0, 14);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidTimerStatusData(byte[] params, int offset) {
        int programedIndicator = params[offset] & 16;
        boolean durationAvailable = false;
        if (programedIndicator == 16) {
            int programedInfo = params[offset] & 15;
            if (isValidProgrammedInfo(programedInfo)) {
                offset++;
                if ((programedInfo != 9 && programedInfo != 11) || params.length - offset < 2) {
                    return true;
                }
                durationAvailable = true;
            }
        } else {
            int nonProgramedErrorInfo = params[offset] & 15;
            if (isValidNotProgrammedErrorInfo(nonProgramedErrorInfo)) {
                offset++;
                if (nonProgramedErrorInfo != 14 || params.length - offset < 2) {
                    return true;
                }
                durationAvailable = true;
            }
        }
        if (durationAvailable) {
            return isValidDurationHours(params[offset]) && isValidMinute(params[offset + 1]);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidPlayMode(int value) {
        return isWithinRange(value, 5, 7) || isWithinRange(value, 9, 11) || isWithinRange(value, 21, 23) || isWithinRange(value, 25, 27) || isWithinRange(value, 36, 37) || value == 32;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidUiBroadcastType(int value) {
        return value == 0 || value == 1 || value == 16 || value == 32 || value == 48 || value == 64 || value == 80 || value == 96 || value == 112 || value == 128 || value == 144 || value == 145 || value == 160;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidUiSoundPresenationControl(int value) {
        int value2 = value & 255;
        return value2 == 32 || value2 == 48 || value2 == 128 || value2 == 144 || value2 == 160 || isWithinRange(value2, 177, 179) || isWithinRange(value2, 193, 195);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isValidTunerDeviceInfo(byte[] params) {
        int tunerDisplayInfo = params[0] & 127;
        if (tunerDisplayInfo == 0) {
            if (params.length >= 5) {
                return isValidDigitalServiceIdentification(params, 1);
            }
        } else {
            if (tunerDisplayInfo == 1) {
                return true;
            }
            return tunerDisplayInfo == 2 && params.length >= 5 && isValidAnalogueBroadcastType(params[1]) && isValidAnalogueFrequency(com.android.server.hdmi.HdmiUtils.twoBytesToInt(params, 2)) && isValidBroadcastSystem(params[4]);
        }
        return false;
    }

    private static class PhysicalAddressValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private PhysicalAddressValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < 2) {
                return 4;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(com.android.server.hdmi.HdmiCecMessageValidator.isValidPhysicalAddress(params, 0));
        }
    }

    private static class SystemAudioModeRequestValidator extends com.android.server.hdmi.HdmiCecMessageValidator.PhysicalAddressValidator {
        private SystemAudioModeRequestValidator() {
            super();
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.PhysicalAddressValidator, com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length == 0) {
                return 0;
            }
            return super.isValid(params);
        }
    }

    private static class ReportPhysicalAddressValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private ReportPhysicalAddressValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < 3) {
                return 4;
            }
            boolean z = false;
            if (com.android.server.hdmi.HdmiCecMessageValidator.isValidPhysicalAddress(params, 0) && com.android.server.hdmi.HdmiCecMessageValidator.isValidType(params[2])) {
                z = true;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    private static class RoutingChangeValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private RoutingChangeValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < 4) {
                return 4;
            }
            boolean z = false;
            if (com.android.server.hdmi.HdmiCecMessageValidator.isValidPhysicalAddress(params, 0) && com.android.server.hdmi.HdmiCecMessageValidator.isValidPhysicalAddress(params, 2)) {
                z = true;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    private static class RecordStatusInfoValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private RecordStatusInfoValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            boolean z = true;
            if (params.length < 1) {
                return 4;
            }
            if (!com.android.server.hdmi.HdmiCecMessageValidator.isWithinRange(params[0], 1, 7) && !com.android.server.hdmi.HdmiCecMessageValidator.isWithinRange(params[0], 9, 14) && !com.android.server.hdmi.HdmiCecMessageValidator.isWithinRange(params[0], 16, 23) && !com.android.server.hdmi.HdmiCecMessageValidator.isWithinRange(params[0], 26, 27) && params[0] != 31) {
                z = false;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    private static class AsciiValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private final int mMaxLength;
        private final int mMinLength;

        AsciiValidator(int length) {
            this.mMinLength = length;
            this.mMaxLength = length;
        }

        AsciiValidator(int minLength, int maxLength) {
            this.mMinLength = minLength;
            this.mMaxLength = maxLength;
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < this.mMinLength) {
                return 4;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(com.android.server.hdmi.HdmiCecMessageValidator.isValidAsciiString(params, 0, this.mMaxLength));
        }
    }

    private static class OsdStringValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private OsdStringValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < 2) {
                return 4;
            }
            boolean z = false;
            if (com.android.server.hdmi.HdmiCecMessageValidator.isValidDisplayControl(params[0]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidAsciiString(params, 1, 14)) {
                z = true;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    private static class MinimumOneByteRangeValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private final int mMaxValue;
        private final int mMinValue;

        MinimumOneByteRangeValidator(int minValue, int maxValue) {
            this.mMinValue = minValue;
            this.mMaxValue = maxValue;
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < 1) {
                return 4;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(com.android.server.hdmi.HdmiCecMessageValidator.isWithinRange(params[0], this.mMinValue, this.mMaxValue));
        }
    }

    private static class SingleByteRangeValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private final int mMaxValue;
        private final int mMinValue;

        SingleByteRangeValidator(int minValue, int maxValue) {
            this.mMinValue = minValue;
            this.mMaxValue = maxValue;
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < 1) {
                return 4;
            }
            if (params.length > 1) {
                return 5;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(com.android.server.hdmi.HdmiCecMessageValidator.isWithinRange(params[0], this.mMinValue, this.mMaxValue));
        }
    }

    private static class AnalogueTimerValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private AnalogueTimerValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < 11) {
                return 4;
            }
            boolean z = false;
            if (com.android.server.hdmi.HdmiCecMessageValidator.isValidDayOfMonth(params[0]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidMonthOfYear(params[1]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidHour(params[2]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidMinute(params[3]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidDurationHours(params[4]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidMinute(params[5]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidRecordingSequence(params[6]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidAnalogueBroadcastType(params[7]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidAnalogueFrequency(com.android.server.hdmi.HdmiUtils.twoBytesToInt(params, 8)) && com.android.server.hdmi.HdmiCecMessageValidator.isValidBroadcastSystem(params[10])) {
                z = true;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    private static class DigitalTimerValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private DigitalTimerValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < 11) {
                return 4;
            }
            boolean z = false;
            if (com.android.server.hdmi.HdmiCecMessageValidator.isValidDayOfMonth(params[0]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidMonthOfYear(params[1]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidHour(params[2]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidMinute(params[3]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidDurationHours(params[4]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidMinute(params[5]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidRecordingSequence(params[6]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidDigitalServiceIdentification(params, 7)) {
                z = true;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    private static class ExternalTimerValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private ExternalTimerValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < 9) {
                return 4;
            }
            boolean z = false;
            if (com.android.server.hdmi.HdmiCecMessageValidator.isValidDayOfMonth(params[0]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidMonthOfYear(params[1]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidHour(params[2]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidMinute(params[3]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidDurationHours(params[4]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidMinute(params[5]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidRecordingSequence(params[6]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidExternalSource(params, 7)) {
                z = true;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    private static class TimerClearedStatusValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private TimerClearedStatusValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            boolean z = true;
            if (params.length < 1) {
                return 4;
            }
            if (!com.android.server.hdmi.HdmiCecMessageValidator.isWithinRange(params[0], 0, 2) && (params[0] & 255) != 128) {
                z = false;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    private static class TimerStatusValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private TimerStatusValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < 1) {
                return 4;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(com.android.server.hdmi.HdmiCecMessageValidator.isValidTimerStatusData(params, 0));
        }
    }

    private static class PlayModeValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private PlayModeValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < 1) {
                return 4;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(com.android.server.hdmi.HdmiCecMessageValidator.isValidPlayMode(params[0]));
        }
    }

    private static class SelectAnalogueServiceValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private SelectAnalogueServiceValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < 4) {
                return 4;
            }
            boolean z = false;
            if (com.android.server.hdmi.HdmiCecMessageValidator.isValidAnalogueBroadcastType(params[0]) && com.android.server.hdmi.HdmiCecMessageValidator.isValidAnalogueFrequency(com.android.server.hdmi.HdmiUtils.twoBytesToInt(params, 1)) && com.android.server.hdmi.HdmiCecMessageValidator.isValidBroadcastSystem(params[3])) {
                z = true;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(z);
        }
    }

    private static class SelectDigitalServiceValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private SelectDigitalServiceValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < 4) {
                return 4;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(com.android.server.hdmi.HdmiCecMessageValidator.isValidDigitalServiceIdentification(params, 0));
        }
    }

    private static class TunerDeviceStatusValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private TunerDeviceStatusValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < 1) {
                return 4;
            }
            return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(com.android.server.hdmi.HdmiCecMessageValidator.isValidTunerDeviceInfo(params));
        }
    }

    private static class UserControlPressedValidator implements com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator {
        private UserControlPressedValidator() {
        }

        @Override // com.android.server.hdmi.HdmiCecMessageValidator.ParameterValidator
        public int isValid(byte[] params) {
            if (params.length < 1) {
                return 4;
            }
            if (params.length == 1) {
                return 0;
            }
            int uiCommand = params[0];
            switch (uiCommand) {
                case 86:
                    return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(com.android.server.hdmi.HdmiCecMessageValidator.isValidUiBroadcastType(params[1]));
                case 87:
                    return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(com.android.server.hdmi.HdmiCecMessageValidator.isValidUiSoundPresenationControl(params[1]));
                case 96:
                    return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(com.android.server.hdmi.HdmiCecMessageValidator.isValidPlayMode(params[1]));
                case 103:
                    if (params.length >= 4) {
                        return com.android.server.hdmi.HdmiCecMessageValidator.toErrorCode(com.android.server.hdmi.HdmiCecMessageValidator.isValidChannelIdentifier(params, 1));
                    }
                    return 4;
                default:
                    return 0;
            }
        }
    }
}
