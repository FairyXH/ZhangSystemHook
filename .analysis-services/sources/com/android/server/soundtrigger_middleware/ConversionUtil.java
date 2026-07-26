package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
class ConversionUtil {
    ConversionUtil() {
    }

    static android.media.soundtrigger.Properties hidl2aidlProperties(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Properties hidlProperties) {
        android.media.soundtrigger.Properties aidlProperties = new android.media.soundtrigger.Properties();
        aidlProperties.implementor = hidlProperties.implementor;
        aidlProperties.description = hidlProperties.description;
        aidlProperties.version = hidlProperties.version;
        aidlProperties.uuid = hidl2aidlUuid(hidlProperties.uuid);
        aidlProperties.maxSoundModels = hidlProperties.maxSoundModels;
        aidlProperties.maxKeyPhrases = hidlProperties.maxKeyPhrases;
        aidlProperties.maxUsers = hidlProperties.maxUsers;
        aidlProperties.recognitionModes = hidl2aidlRecognitionModes(hidlProperties.recognitionModes);
        aidlProperties.captureTransition = hidlProperties.captureTransition;
        aidlProperties.maxBufferMs = hidlProperties.maxBufferMs;
        aidlProperties.concurrentCapture = hidlProperties.concurrentCapture;
        aidlProperties.triggerInEvent = hidlProperties.triggerInEvent;
        aidlProperties.powerConsumptionMw = hidlProperties.powerConsumptionMw;
        return aidlProperties;
    }

    static android.media.soundtrigger.Properties hidl2aidlProperties(android.hardware.soundtrigger.V2_3.Properties hidlProperties) {
        android.media.soundtrigger.Properties aidlProperties = hidl2aidlProperties(hidlProperties.base);
        aidlProperties.supportedModelArch = hidlProperties.supportedModelArch;
        aidlProperties.audioCapabilities = hidl2aidlAudioCapabilities(hidlProperties.audioCapabilities);
        return aidlProperties;
    }

    static java.lang.String hidl2aidlUuid(android.hardware.audio.common.V2_0.Uuid hidlUuid) {
        if (hidlUuid.node == null || hidlUuid.node.length != 6) {
            throw new java.lang.IllegalArgumentException("UUID.node must be of length 6.");
        }
        return java.lang.String.format("%08x-%04x-%04x-%04x-%02x%02x%02x%02x%02x%02x", java.lang.Integer.valueOf(hidlUuid.timeLow), java.lang.Short.valueOf(hidlUuid.timeMid), java.lang.Short.valueOf(hidlUuid.versionAndTimeHigh), java.lang.Short.valueOf(hidlUuid.variantAndClockSeqHigh), java.lang.Byte.valueOf(hidlUuid.node[0]), java.lang.Byte.valueOf(hidlUuid.node[1]), java.lang.Byte.valueOf(hidlUuid.node[2]), java.lang.Byte.valueOf(hidlUuid.node[3]), java.lang.Byte.valueOf(hidlUuid.node[4]), java.lang.Byte.valueOf(hidlUuid.node[5]));
    }

    static android.hardware.audio.common.V2_0.Uuid aidl2hidlUuid(java.lang.String aidlUuid) {
        java.util.regex.Matcher matcher = com.android.server.soundtrigger_middleware.UuidUtil.PATTERN.matcher(aidlUuid);
        if (!matcher.matches()) {
            throw new java.lang.IllegalArgumentException("Illegal format for UUID: " + aidlUuid);
        }
        android.hardware.audio.common.V2_0.Uuid hidlUuid = new android.hardware.audio.common.V2_0.Uuid();
        hidlUuid.timeLow = java.lang.Integer.parseUnsignedInt(matcher.group(1), 16);
        hidlUuid.timeMid = (short) java.lang.Integer.parseUnsignedInt(matcher.group(2), 16);
        hidlUuid.versionAndTimeHigh = (short) java.lang.Integer.parseUnsignedInt(matcher.group(3), 16);
        hidlUuid.variantAndClockSeqHigh = (short) java.lang.Integer.parseUnsignedInt(matcher.group(4), 16);
        hidlUuid.node = new byte[]{(byte) java.lang.Integer.parseUnsignedInt(matcher.group(5), 16), (byte) java.lang.Integer.parseUnsignedInt(matcher.group(6), 16), (byte) java.lang.Integer.parseUnsignedInt(matcher.group(7), 16), (byte) java.lang.Integer.parseUnsignedInt(matcher.group(8), 16), (byte) java.lang.Integer.parseUnsignedInt(matcher.group(9), 16), (byte) java.lang.Integer.parseUnsignedInt(matcher.group(10), 16)};
        return hidlUuid;
    }

    static int aidl2hidlSoundModelType(int aidlType) {
        switch (aidlType) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                throw new java.lang.IllegalArgumentException("Unknown sound model type: " + aidlType);
        }
    }

    static int hidl2aidlSoundModelType(int hidlType) {
        switch (hidlType) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                throw new java.lang.IllegalArgumentException("Unknown sound model type: " + hidlType);
        }
    }

    static android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Phrase aidl2hidlPhrase(android.media.soundtrigger.Phrase aidlPhrase) {
        android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Phrase hidlPhrase = new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Phrase();
        hidlPhrase.id = aidlPhrase.id;
        hidlPhrase.recognitionModes = aidl2hidlRecognitionModes(aidlPhrase.recognitionModes);
        for (int aidlUser : aidlPhrase.users) {
            hidlPhrase.users.add(java.lang.Integer.valueOf(aidlUser));
        }
        hidlPhrase.locale = aidlPhrase.locale;
        hidlPhrase.text = aidlPhrase.text;
        return hidlPhrase;
    }

    static int aidl2hidlRecognitionModes(int aidlModes) {
        int hidlModes = 0;
        if ((aidlModes & 1) != 0) {
            hidlModes = 0 | 1;
        }
        if ((aidlModes & 2) != 0) {
            hidlModes |= 2;
        }
        if ((aidlModes & 4) != 0) {
            hidlModes |= 4;
        }
        if ((aidlModes & 8) != 0) {
            return hidlModes | 8;
        }
        return hidlModes;
    }

    static int hidl2aidlRecognitionModes(int hidlModes) {
        int aidlModes = 0;
        if ((hidlModes & 1) != 0) {
            aidlModes = 0 | 1;
        }
        if ((hidlModes & 2) != 0) {
            aidlModes |= 2;
        }
        if ((hidlModes & 4) != 0) {
            aidlModes |= 4;
        }
        if ((hidlModes & 8) != 0) {
            return aidlModes | 8;
        }
        return aidlModes;
    }

    static android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel aidl2hidlSoundModel(android.media.soundtrigger.SoundModel aidlModel) {
        android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel hidlModel = new android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel();
        hidlModel.header.type = aidl2hidlSoundModelType(aidlModel.type);
        hidlModel.header.uuid = aidl2hidlUuid(aidlModel.uuid);
        hidlModel.header.vendorUuid = aidl2hidlUuid(aidlModel.vendorUuid);
        hidlModel.data = parcelFileDescriptorToHidlMemory(aidlModel.data, aidlModel.dataSize);
        return hidlModel;
    }

    static android.hardware.soundtrigger.V2_1.ISoundTriggerHw.PhraseSoundModel aidl2hidlPhraseSoundModel(android.media.soundtrigger.PhraseSoundModel aidlModel) {
        android.hardware.soundtrigger.V2_1.ISoundTriggerHw.PhraseSoundModel hidlModel = new android.hardware.soundtrigger.V2_1.ISoundTriggerHw.PhraseSoundModel();
        hidlModel.common = aidl2hidlSoundModel(aidlModel.common);
        for (android.media.soundtrigger.Phrase aidlPhrase : aidlModel.phrases) {
            hidlModel.phrases.add(aidl2hidlPhrase(aidlPhrase));
        }
        return hidlModel;
    }

    static android.hardware.soundtrigger.V2_3.RecognitionConfig aidl2hidlRecognitionConfig(android.media.soundtrigger.RecognitionConfig aidlConfig, int deviceHandle, int ioHandle) {
        android.hardware.soundtrigger.V2_3.RecognitionConfig hidlConfig = new android.hardware.soundtrigger.V2_3.RecognitionConfig();
        hidlConfig.base.header.captureDevice = deviceHandle;
        hidlConfig.base.header.captureHandle = ioHandle;
        hidlConfig.base.header.captureRequested = aidlConfig.captureRequested;
        for (android.media.soundtrigger.PhraseRecognitionExtra aidlPhraseExtra : aidlConfig.phraseRecognitionExtras) {
            hidlConfig.base.header.phrases.add(aidl2hidlPhraseRecognitionExtra(aidlPhraseExtra));
        }
        hidlConfig.base.data = android.os.HidlMemoryUtil.byteArrayToHidlMemory(aidlConfig.data, "SoundTrigger RecognitionConfig");
        hidlConfig.audioCapabilities = aidlConfig.audioCapabilities;
        return hidlConfig;
    }

    static android.hardware.soundtrigger.V2_0.PhraseRecognitionExtra aidl2hidlPhraseRecognitionExtra(android.media.soundtrigger.PhraseRecognitionExtra aidlExtra) {
        android.hardware.soundtrigger.V2_0.PhraseRecognitionExtra hidlExtra = new android.hardware.soundtrigger.V2_0.PhraseRecognitionExtra();
        hidlExtra.id = aidlExtra.id;
        hidlExtra.recognitionModes = aidl2hidlRecognitionModes(aidlExtra.recognitionModes);
        hidlExtra.confidenceLevel = aidlExtra.confidenceLevel;
        hidlExtra.levels.ensureCapacity(aidlExtra.levels.length);
        for (android.media.soundtrigger.ConfidenceLevel aidlLevel : aidlExtra.levels) {
            hidlExtra.levels.add(aidl2hidlConfidenceLevel(aidlLevel));
        }
        return hidlExtra;
    }

    static android.media.soundtrigger.PhraseRecognitionExtra hidl2aidlPhraseRecognitionExtra(android.hardware.soundtrigger.V2_0.PhraseRecognitionExtra hidlExtra) {
        android.media.soundtrigger.PhraseRecognitionExtra aidlExtra = new android.media.soundtrigger.PhraseRecognitionExtra();
        aidlExtra.id = hidlExtra.id;
        aidlExtra.recognitionModes = hidl2aidlRecognitionModes(hidlExtra.recognitionModes);
        aidlExtra.confidenceLevel = hidlExtra.confidenceLevel;
        aidlExtra.levels = new android.media.soundtrigger.ConfidenceLevel[hidlExtra.levels.size()];
        for (int i = 0; i < hidlExtra.levels.size(); i++) {
            aidlExtra.levels[i] = hidl2aidlConfidenceLevel(hidlExtra.levels.get(i));
        }
        return aidlExtra;
    }

    static android.hardware.soundtrigger.V2_0.ConfidenceLevel aidl2hidlConfidenceLevel(android.media.soundtrigger.ConfidenceLevel aidlLevel) {
        android.hardware.soundtrigger.V2_0.ConfidenceLevel hidlLevel = new android.hardware.soundtrigger.V2_0.ConfidenceLevel();
        hidlLevel.userId = aidlLevel.userId;
        hidlLevel.levelPercent = aidlLevel.levelPercent;
        return hidlLevel;
    }

    static android.media.soundtrigger.ConfidenceLevel hidl2aidlConfidenceLevel(android.hardware.soundtrigger.V2_0.ConfidenceLevel hidlLevel) {
        android.media.soundtrigger.ConfidenceLevel aidlLevel = new android.media.soundtrigger.ConfidenceLevel();
        aidlLevel.userId = hidlLevel.userId;
        aidlLevel.levelPercent = hidlLevel.levelPercent;
        return aidlLevel;
    }

    static int hidl2aidlRecognitionStatus(int hidlStatus) {
        switch (hidlStatus) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                throw new java.lang.IllegalArgumentException("Unknown recognition status: " + hidlStatus);
        }
    }

    static android.media.soundtrigger.RecognitionEvent hidl2aidlRecognitionEvent(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent hidlEvent) {
        android.media.soundtrigger.RecognitionEvent aidlEvent = new android.media.soundtrigger.RecognitionEvent();
        aidlEvent.status = hidl2aidlRecognitionStatus(hidlEvent.status);
        aidlEvent.type = hidl2aidlSoundModelType(hidlEvent.type);
        aidlEvent.captureAvailable = hidlEvent.captureAvailable;
        aidlEvent.captureDelayMs = hidlEvent.captureDelayMs;
        aidlEvent.capturePreambleMs = hidlEvent.capturePreambleMs;
        aidlEvent.triggerInData = hidlEvent.triggerInData;
        aidlEvent.audioConfig = hidl2aidlAudioConfig(hidlEvent.audioConfig, true);
        aidlEvent.data = new byte[hidlEvent.data.size()];
        for (int i = 0; i < aidlEvent.data.length; i++) {
            aidlEvent.data[i] = hidlEvent.data.get(i).byteValue();
        }
        int i2 = aidlEvent.status;
        aidlEvent.recognitionStillActive = i2 == 3;
        return aidlEvent;
    }

    static android.media.soundtrigger.RecognitionEvent hidl2aidlRecognitionEvent(android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.RecognitionEvent hidlEvent) {
        android.media.soundtrigger.RecognitionEvent aidlEvent = hidl2aidlRecognitionEvent(hidlEvent.header);
        aidlEvent.data = android.os.HidlMemoryUtil.hidlMemoryToByteArray(hidlEvent.data);
        return aidlEvent;
    }

    static android.media.soundtrigger.PhraseRecognitionEvent hidl2aidlPhraseRecognitionEvent(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.PhraseRecognitionEvent hidlEvent) {
        android.media.soundtrigger.PhraseRecognitionEvent aidlEvent = new android.media.soundtrigger.PhraseRecognitionEvent();
        aidlEvent.common = hidl2aidlRecognitionEvent(hidlEvent.common);
        aidlEvent.phraseExtras = new android.media.soundtrigger.PhraseRecognitionExtra[hidlEvent.phraseExtras.size()];
        for (int i = 0; i < hidlEvent.phraseExtras.size(); i++) {
            aidlEvent.phraseExtras[i] = hidl2aidlPhraseRecognitionExtra(hidlEvent.phraseExtras.get(i));
        }
        return aidlEvent;
    }

    static android.media.soundtrigger.PhraseRecognitionEvent hidl2aidlPhraseRecognitionEvent(android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.PhraseRecognitionEvent hidlEvent) {
        android.media.soundtrigger.PhraseRecognitionEvent aidlEvent = new android.media.soundtrigger.PhraseRecognitionEvent();
        aidlEvent.common = hidl2aidlRecognitionEvent(hidlEvent.common);
        aidlEvent.phraseExtras = new android.media.soundtrigger.PhraseRecognitionExtra[hidlEvent.phraseExtras.size()];
        for (int i = 0; i < hidlEvent.phraseExtras.size(); i++) {
            aidlEvent.phraseExtras[i] = hidl2aidlPhraseRecognitionExtra(hidlEvent.phraseExtras.get(i));
        }
        return aidlEvent;
    }

    static android.media.audio.common.AudioConfig hidl2aidlAudioConfig(android.hardware.audio.common.V2_0.AudioConfig hidlConfig, boolean isInput) {
        android.media.audio.common.AudioConfig aidlConfig = new android.media.audio.common.AudioConfig();
        aidlConfig.base = hidl2aidlAudioConfigBase(hidlConfig.sampleRateHz, hidlConfig.channelMask, hidlConfig.format, isInput);
        aidlConfig.offloadInfo = hidl2aidlOffloadInfo(hidlConfig.offloadInfo);
        aidlConfig.frameCount = hidlConfig.frameCount;
        return aidlConfig;
    }

    static android.media.audio.common.AudioOffloadInfo hidl2aidlOffloadInfo(android.hardware.audio.common.V2_0.AudioOffloadInfo hidlInfo) {
        android.media.audio.common.AudioOffloadInfo aidlInfo = new android.media.audio.common.AudioOffloadInfo();
        aidlInfo.base = hidl2aidlAudioConfigBase(hidlInfo.sampleRateHz, hidlInfo.channelMask, hidlInfo.format, false);
        aidlInfo.streamType = android.media.audio.common.AidlConversion.legacy2aidl_audio_stream_type_t_AudioStreamType(hidlInfo.streamType);
        aidlInfo.bitRatePerSecond = hidlInfo.bitRatePerSecond;
        aidlInfo.durationUs = hidlInfo.durationMicroseconds;
        aidlInfo.hasVideo = hidlInfo.hasVideo;
        aidlInfo.isStreaming = hidlInfo.isStreaming;
        aidlInfo.bitWidth = hidlInfo.bitWidth;
        aidlInfo.offloadBufferSize = hidlInfo.bufferSize;
        aidlInfo.usage = android.media.audio.common.AidlConversion.legacy2aidl_audio_usage_t_AudioUsage(hidlInfo.usage);
        return aidlInfo;
    }

    static android.media.audio.common.AudioConfigBase hidl2aidlAudioConfigBase(int sampleRateHz, int channelMask, int format, boolean isInput) {
        android.media.audio.common.AudioConfigBase aidlBase = new android.media.audio.common.AudioConfigBase();
        aidlBase.sampleRate = sampleRateHz;
        aidlBase.channelMask = android.media.audio.common.AidlConversion.legacy2aidl_audio_channel_mask_t_AudioChannelLayout(channelMask, isInput);
        aidlBase.format = android.media.audio.common.AidlConversion.legacy2aidl_audio_format_t_AudioFormatDescription(format);
        return aidlBase;
    }

    static android.media.soundtrigger.ModelParameterRange hidl2aidlModelParameterRange(android.hardware.soundtrigger.V2_3.ModelParameterRange hidlRange) {
        if (hidlRange == null) {
            return null;
        }
        android.media.soundtrigger.ModelParameterRange aidlRange = new android.media.soundtrigger.ModelParameterRange();
        aidlRange.minInclusive = hidlRange.start;
        aidlRange.maxInclusive = hidlRange.end;
        return aidlRange;
    }

    static int aidl2hidlModelParameter(int aidlParam) {
        switch (aidlParam) {
            case 0:
                return 0;
            default:
                return -1;
        }
    }

    static int hidl2aidlAudioCapabilities(int hidlCapabilities) {
        int aidlCapabilities = 0;
        if ((hidlCapabilities & 1) != 0) {
            aidlCapabilities = 0 | 1;
        }
        if ((hidlCapabilities & 2) != 0) {
            return aidlCapabilities | 2;
        }
        return aidlCapabilities;
    }

    private static android.os.HidlMemory parcelFileDescriptorToHidlMemory(android.os.ParcelFileDescriptor data, int dataSize) {
        if (dataSize > 0) {
            return android.os.HidlMemoryUtil.fileDescriptorToHidlMemory(data.getFileDescriptor(), dataSize);
        }
        return android.os.HidlMemoryUtil.fileDescriptorToHidlMemory((java.io.FileDescriptor) null, 0);
    }
}
