package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class HapticFeedbackCustomization {
    private static final java.lang.String ATTRIBUTE_ID = "id";
    private static final java.lang.String TAG = "HapticFeedbackCustomization";
    private static final java.lang.String TAG_CONSTANT = "constant";
    private static final java.lang.String TAG_CONSTANTS = "haptic-feedback-constants";

    HapticFeedbackCustomization() {
    }

    static android.util.SparseArray<android.os.VibrationEffect> loadVibrations(android.content.res.Resources res, android.os.VibratorInfo vibratorInfo) throws java.io.IOException, com.android.server.vibrator.HapticFeedbackCustomization.CustomizationParserException {
        try {
            return loadVibrationsInternal(res, vibratorInfo);
        } catch (android.os.vibrator.persistence.VibrationXmlParser.VibrationXmlParserException | com.android.internal.vibrator.persistence.XmlParserException | org.xmlpull.v1.XmlPullParserException e) {
            throw new com.android.server.vibrator.HapticFeedbackCustomization.CustomizationParserException("Error parsing haptic feedback customization file.", e);
        }
    }

    private static android.util.SparseArray<android.os.VibrationEffect> loadVibrationsInternal(android.content.res.Resources res, android.os.VibratorInfo vibratorInfo) throws org.xmlpull.v1.XmlPullParserException, android.os.vibrator.persistence.VibrationXmlParser.VibrationXmlParserException, java.io.IOException, com.android.internal.vibrator.persistence.XmlParserException, com.android.server.vibrator.HapticFeedbackCustomization.CustomizationParserException {
        if (!android.os.vibrator.Flags.hapticFeedbackVibrationOemCustomizationEnabled()) {
            android.util.Slog.d(TAG, "Haptic feedback customization feature is not enabled.");
            return null;
        }
        java.lang.String customizationFile = res.getString(android.R.string.config_inCallNotificationSound);
        if (android.text.TextUtils.isEmpty(customizationFile)) {
            android.util.Slog.d(TAG, "Customization file not configured.");
            return null;
        }
        try {
            java.io.FileReader fileReader = new java.io.FileReader(customizationFile);
            com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.newFastPullParser();
            parser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
            parser.setInput(fileReader);
            com.android.internal.vibrator.persistence.XmlReader.readDocumentStartTag(parser, TAG_CONSTANTS);
            com.android.internal.vibrator.persistence.XmlValidator.checkTagHasNoUnexpectedAttributes(parser, new java.lang.String[0]);
            int rootDepth = parser.getDepth();
            android.util.SparseArray<android.os.VibrationEffect> mapping = new android.util.SparseArray<>();
            while (com.android.internal.vibrator.persistence.XmlReader.readNextTagWithin(parser, rootDepth)) {
                com.android.internal.vibrator.persistence.XmlValidator.checkStartTag(parser, TAG_CONSTANT);
                int customizationDepth = parser.getDepth();
                com.android.internal.vibrator.persistence.XmlValidator.checkTagHasNoUnexpectedAttributes(parser, new java.lang.String[]{ATTRIBUTE_ID});
                int effectId = com.android.internal.vibrator.persistence.XmlReader.readAttributeIntNonNegative(parser, ATTRIBUTE_ID);
                if (mapping.contains(effectId)) {
                    throw new com.android.server.vibrator.HapticFeedbackCustomization.CustomizationParserException("Multiple customizations found for effect " + effectId);
                }
                com.android.internal.vibrator.persistence.XmlValidator.checkParserCondition(com.android.internal.vibrator.persistence.XmlReader.readNextTagWithin(parser, customizationDepth), "Unsupported empty customization tag for effect " + effectId, new java.lang.Object[0]);
                android.os.vibrator.persistence.ParsedVibration parsedVibration = android.os.vibrator.persistence.VibrationXmlParser.parseElement(parser, 1);
                if (parsedVibration == null) {
                    throw new com.android.server.vibrator.HapticFeedbackCustomization.CustomizationParserException("Unable to parse vibration element for effect " + effectId);
                }
                android.os.VibrationEffect effect = parsedVibration.resolve(vibratorInfo);
                if (effect != null) {
                    if (effect.getDuration() == Long.MAX_VALUE) {
                        throw new com.android.server.vibrator.HapticFeedbackCustomization.CustomizationParserException(java.lang.String.format("Vibration for effect ID %d is repeating, which is not allowed as a haptic feedback: %s", java.lang.Integer.valueOf(effectId), effect));
                    }
                    mapping.put(effectId, effect);
                }
                com.android.internal.vibrator.persistence.XmlReader.readEndTag(parser, TAG_CONSTANT, customizationDepth);
            }
            com.android.internal.vibrator.persistence.XmlReader.readEndTag(parser, TAG_CONSTANTS, rootDepth);
            com.android.internal.vibrator.persistence.XmlReader.readDocumentEndTag(parser);
            return mapping;
        } catch (java.io.FileNotFoundException e) {
            android.util.Slog.d(TAG, "Specified customization file not found.");
            return null;
        }
    }

    static final class CustomizationParserException extends java.lang.Exception {
        private CustomizationParserException(java.lang.String message) {
            super(message);
        }

        private CustomizationParserException(java.lang.String message, java.lang.Throwable cause) {
            super(message, cause);
        }
    }
}
