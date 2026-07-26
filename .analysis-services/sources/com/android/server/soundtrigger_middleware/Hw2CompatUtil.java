package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
class Hw2CompatUtil {
    Hw2CompatUtil() {
    }

    static android.hardware.soundtrigger.V2_0.ISoundTriggerHw.SoundModel convertSoundModel_2_1_to_2_0(android.hardware.soundtrigger.V2_1.ISoundTriggerHw.SoundModel soundModel) {
        android.hardware.soundtrigger.V2_0.ISoundTriggerHw.SoundModel model_2_0 = soundModel.header;
        model_2_0.data = android.os.HidlMemoryUtil.hidlMemoryToByteList(soundModel.data);
        return model_2_0;
    }

    static android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.RecognitionEvent convertRecognitionEvent_2_0_to_2_1(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.RecognitionEvent event) {
        android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.RecognitionEvent event_2_1 = new android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.RecognitionEvent();
        event_2_1.header = event;
        event_2_1.data = android.os.HidlMemoryUtil.byteListToHidlMemory(event_2_1.header.data, "SoundTrigger RecognitionEvent");
        event_2_1.header.data = new java.util.ArrayList<>();
        return event_2_1;
    }

    static android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.PhraseRecognitionEvent convertPhraseRecognitionEvent_2_0_to_2_1(android.hardware.soundtrigger.V2_0.ISoundTriggerHwCallback.PhraseRecognitionEvent event) {
        android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.PhraseRecognitionEvent event_2_1 = new android.hardware.soundtrigger.V2_1.ISoundTriggerHwCallback.PhraseRecognitionEvent();
        event_2_1.common = convertRecognitionEvent_2_0_to_2_1(event.common);
        event_2_1.phraseExtras = event.phraseExtras;
        return event_2_1;
    }

    static android.hardware.soundtrigger.V2_0.ISoundTriggerHw.PhraseSoundModel convertPhraseSoundModel_2_1_to_2_0(android.hardware.soundtrigger.V2_1.ISoundTriggerHw.PhraseSoundModel soundModel) {
        android.hardware.soundtrigger.V2_0.ISoundTriggerHw.PhraseSoundModel model_2_0 = new android.hardware.soundtrigger.V2_0.ISoundTriggerHw.PhraseSoundModel();
        model_2_0.common = convertSoundModel_2_1_to_2_0(soundModel.common);
        model_2_0.phrases = soundModel.phrases;
        return model_2_0;
    }

    static android.hardware.soundtrigger.V2_1.ISoundTriggerHw.RecognitionConfig convertRecognitionConfig_2_3_to_2_1(android.hardware.soundtrigger.V2_3.RecognitionConfig config) {
        return config.base;
    }

    static android.hardware.soundtrigger.V2_0.ISoundTriggerHw.RecognitionConfig convertRecognitionConfig_2_3_to_2_0(android.hardware.soundtrigger.V2_3.RecognitionConfig config) {
        android.hardware.soundtrigger.V2_0.ISoundTriggerHw.RecognitionConfig config_2_0 = config.base.header;
        config_2_0.data = android.os.HidlMemoryUtil.hidlMemoryToByteList(config.base.data);
        return config_2_0;
    }

    static android.hardware.soundtrigger.V2_3.Properties convertProperties_2_0_to_2_3(android.hardware.soundtrigger.V2_0.ISoundTriggerHw.Properties properties) {
        android.hardware.soundtrigger.V2_3.Properties properties_2_3 = new android.hardware.soundtrigger.V2_3.Properties();
        properties_2_3.base = properties;
        return properties_2_3;
    }
}
