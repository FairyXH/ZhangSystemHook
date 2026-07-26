package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class ValidationUtil {
    static void validateUuid(java.lang.String uuid) {
        java.util.Objects.requireNonNull(uuid);
        java.util.regex.Matcher matcher = com.android.server.soundtrigger_middleware.UuidUtil.PATTERN.matcher(uuid);
        if (!matcher.matches()) {
            throw new java.lang.IllegalArgumentException("Illegal format for UUID: " + uuid);
        }
    }

    static void validateGenericModel(android.media.soundtrigger.SoundModel model) {
        validateModel(model, 1);
    }

    static void validateModel(android.media.soundtrigger.SoundModel model, int expectedType) {
        java.util.Objects.requireNonNull(model);
        if (model.type != expectedType) {
            throw new java.lang.IllegalArgumentException("Invalid type");
        }
        validateUuid(model.uuid);
        validateUuid(model.vendorUuid);
        if (model.dataSize > 0) {
            java.util.Objects.requireNonNull(model.data);
        }
    }

    static void validatePhraseModel(android.media.soundtrigger.PhraseSoundModel model) {
        java.util.Objects.requireNonNull(model);
        validateModel(model.common, 0);
        java.util.Objects.requireNonNull(model.phrases);
        for (android.media.soundtrigger.Phrase phrase : model.phrases) {
            java.util.Objects.requireNonNull(phrase);
            if ((phrase.recognitionModes & (-16)) != 0) {
                throw new java.lang.IllegalArgumentException("Invalid recognitionModes");
            }
            java.util.Objects.requireNonNull(phrase.users);
            java.util.Objects.requireNonNull(phrase.locale);
            java.util.Objects.requireNonNull(phrase.text);
        }
    }

    static void validateRecognitionConfig(android.media.soundtrigger.RecognitionConfig config) {
        java.util.Objects.requireNonNull(config);
        java.util.Objects.requireNonNull(config.phraseRecognitionExtras);
        for (android.media.soundtrigger.PhraseRecognitionExtra extra : config.phraseRecognitionExtras) {
            java.util.Objects.requireNonNull(extra);
            if ((extra.recognitionModes & (-16)) != 0) {
                throw new java.lang.IllegalArgumentException("Invalid recognitionModes");
            }
            if (extra.confidenceLevel < 0 || extra.confidenceLevel > 100) {
                throw new java.lang.IllegalArgumentException("Invalid confidenceLevel");
            }
            java.util.Objects.requireNonNull(extra.levels);
            for (android.media.soundtrigger.ConfidenceLevel level : extra.levels) {
                java.util.Objects.requireNonNull(level);
                if (level.levelPercent < 0 || level.levelPercent > 100) {
                    throw new java.lang.IllegalArgumentException("Invalid confidenceLevel");
                }
            }
        }
        java.util.Objects.requireNonNull(config.data);
    }

    static void validateModelParameter(int modelParam) {
        switch (modelParam) {
            case 0:
                return;
            default:
                throw new java.lang.IllegalArgumentException("Invalid model parameter");
        }
    }
}
