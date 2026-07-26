package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class InputMethodInfoUtils {
    private static final java.lang.String TAG = "InputMethodInfoUtils";
    private static final java.util.Locale[] SEARCH_ORDER_OF_FALLBACK_LOCALES = {java.util.Locale.ENGLISH, java.util.Locale.US, java.util.Locale.UK};
    private static final java.util.Locale ENGLISH_LOCALE = new java.util.Locale("en");

    InputMethodInfoUtils() {
    }

    private static final class InputMethodListBuilder {
        private final java.util.LinkedHashSet<android.view.inputmethod.InputMethodInfo> mInputMethodSet;

        private InputMethodListBuilder() {
            this.mInputMethodSet = new java.util.LinkedHashSet<>();
        }

        com.android.server.inputmethod.InputMethodInfoUtils.InputMethodListBuilder fillImes(java.util.List<android.view.inputmethod.InputMethodInfo> imis, android.content.Context context, boolean checkDefaultAttribute, java.util.Locale locale, boolean checkCountry, java.lang.String requiredSubtypeMode) {
            for (int i = 0; i < imis.size(); i++) {
                android.view.inputmethod.InputMethodInfo imi = imis.get(i);
                if (com.android.server.inputmethod.InputMethodInfoUtils.isSystemImeThatHasSubtypeOf(imi, context, checkDefaultAttribute, locale, checkCountry, requiredSubtypeMode)) {
                    this.mInputMethodSet.add(imi);
                }
            }
            return this;
        }

        com.android.server.inputmethod.InputMethodInfoUtils.InputMethodListBuilder fillAuxiliaryImes(java.util.List<android.view.inputmethod.InputMethodInfo> imis, android.content.Context context) {
            java.util.Iterator<android.view.inputmethod.InputMethodInfo> it = this.mInputMethodSet.iterator();
            while (it.hasNext()) {
                if (it.next().isAuxiliaryIme()) {
                    return this;
                }
            }
            boolean added = false;
            for (int i = 0; i < imis.size(); i++) {
                android.view.inputmethod.InputMethodInfo imi = imis.get(i);
                if (com.android.server.inputmethod.InputMethodInfoUtils.isSystemAuxilialyImeThatHasAutomaticSubtype(imi, context, true)) {
                    this.mInputMethodSet.add(imi);
                    added = true;
                }
            }
            if (added) {
                return this;
            }
            for (int i2 = 0; i2 < imis.size(); i2++) {
                android.view.inputmethod.InputMethodInfo imi2 = imis.get(i2);
                if (com.android.server.inputmethod.InputMethodInfoUtils.isSystemAuxilialyImeThatHasAutomaticSubtype(imi2, context, false)) {
                    this.mInputMethodSet.add(imi2);
                }
            }
            return this;
        }

        public boolean isEmpty() {
            return this.mInputMethodSet.isEmpty();
        }

        public java.util.ArrayList<android.view.inputmethod.InputMethodInfo> build() {
            return new java.util.ArrayList<>(this.mInputMethodSet);
        }
    }

    private static com.android.server.inputmethod.InputMethodInfoUtils.InputMethodListBuilder getMinimumKeyboardSetWithSystemLocale(java.util.List<android.view.inputmethod.InputMethodInfo> imis, android.content.Context context, java.util.Locale systemLocale, java.util.Locale fallbackLocale) {
        com.android.server.inputmethod.InputMethodInfoUtils.InputMethodListBuilder builder = new com.android.server.inputmethod.InputMethodInfoUtils.InputMethodListBuilder();
        builder.fillImes(imis, context, true, systemLocale, true, "keyboard");
        if (!builder.isEmpty()) {
            return builder;
        }
        builder.fillImes(imis, context, true, systemLocale, false, "keyboard");
        if (!builder.isEmpty()) {
            return builder;
        }
        builder.fillImes(imis, context, true, fallbackLocale, true, "keyboard");
        if (!builder.isEmpty()) {
            return builder;
        }
        builder.fillImes(imis, context, true, fallbackLocale, false, "keyboard");
        if (!builder.isEmpty()) {
            return builder;
        }
        builder.fillImes(imis, context, false, fallbackLocale, true, "keyboard");
        if (!builder.isEmpty()) {
            return builder;
        }
        builder.fillImes(imis, context, false, fallbackLocale, false, "keyboard");
        if (!builder.isEmpty()) {
            return builder;
        }
        android.util.Slog.w(TAG, "No software keyboard is found. imis=" + java.util.Arrays.toString(imis.toArray()) + " systemLocale=" + systemLocale + " fallbackLocale=" + fallbackLocale);
        return builder;
    }

    static java.util.ArrayList<android.view.inputmethod.InputMethodInfo> getDefaultEnabledImes(android.content.Context context, java.util.List<android.view.inputmethod.InputMethodInfo> imis, boolean onlyMinimum) {
        java.util.Locale fallbackLocale = getFallbackLocaleForDefaultIme(imis, context);
        java.util.Locale systemLocale = com.android.server.inputmethod.LocaleUtils.getSystemLocaleFromContext(context);
        com.android.server.inputmethod.InputMethodInfoUtils.InputMethodListBuilder builder = getMinimumKeyboardSetWithSystemLocale(imis, context, systemLocale, fallbackLocale);
        if (!onlyMinimum) {
            builder.fillImes(imis, context, true, systemLocale, true, com.android.server.inputmethod.SubtypeUtils.SUBTYPE_MODE_ANY).fillAuxiliaryImes(imis, context);
        }
        return builder.build();
    }

    static java.util.ArrayList<android.view.inputmethod.InputMethodInfo> getDefaultEnabledImes(android.content.Context context, java.util.List<android.view.inputmethod.InputMethodInfo> imis) {
        return getDefaultEnabledImes(context, imis, false);
    }

    static android.view.inputmethod.InputMethodInfo chooseSystemVoiceIme(com.android.server.inputmethod.InputMethodMap methodMap, java.lang.String systemSpeechRecognizerPackageName, java.lang.String currentDefaultVoiceImeId) {
        if (android.text.TextUtils.isEmpty(systemSpeechRecognizerPackageName)) {
            return null;
        }
        android.view.inputmethod.InputMethodInfo defaultVoiceIme = methodMap.get(currentDefaultVoiceImeId);
        if (defaultVoiceIme != null && defaultVoiceIme.isSystem() && defaultVoiceIme.getPackageName().equals(systemSpeechRecognizerPackageName)) {
            return defaultVoiceIme;
        }
        android.view.inputmethod.InputMethodInfo firstMatchingIme = null;
        int methodCount = methodMap.size();
        for (int i = 0; i < methodCount; i++) {
            android.view.inputmethod.InputMethodInfo imi = methodMap.valueAt(i);
            if (imi.isSystem() && android.text.TextUtils.equals(imi.getPackageName(), systemSpeechRecognizerPackageName)) {
                if (firstMatchingIme != null) {
                    android.util.Slog.e(TAG, "At most one InputMethodService can be published in systemSpeechRecognizer: " + systemSpeechRecognizerPackageName + ". Ignoring all of them.");
                    return null;
                }
                firstMatchingIme = imi;
            }
        }
        return firstMatchingIme;
    }

    static android.view.inputmethod.InputMethodInfo getMostApplicableDefaultIME(java.util.List<android.view.inputmethod.InputMethodInfo> enabledImes) {
        if (enabledImes == null || enabledImes.isEmpty()) {
            return null;
        }
        int i = enabledImes.size();
        int firstFoundSystemIme = -1;
        while (i > 0) {
            i--;
            android.view.inputmethod.InputMethodInfo imi = enabledImes.get(i);
            if (!imi.isAuxiliaryIme()) {
                if (imi.isSystem() && com.android.server.inputmethod.SubtypeUtils.containsSubtypeOf(imi, ENGLISH_LOCALE, false, "keyboard")) {
                    return imi;
                }
                if (firstFoundSystemIme < 0 && imi.isSystem()) {
                    firstFoundSystemIme = i;
                }
            }
        }
        return enabledImes.get(java.lang.Math.max(firstFoundSystemIme, 0));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSystemAuxilialyImeThatHasAutomaticSubtype(android.view.inputmethod.InputMethodInfo imi, android.content.Context context, boolean checkDefaultAttribute) {
        if (!imi.isSystem()) {
            return false;
        }
        if ((checkDefaultAttribute && !imi.isDefault(context)) || !imi.isAuxiliaryIme()) {
            return false;
        }
        int subtypeCount = imi.getSubtypeCount();
        for (int i = 0; i < subtypeCount; i++) {
            android.view.inputmethod.InputMethodSubtype s = imi.getSubtypeAt(i);
            if (s.overridesImplicitlyEnabledSubtype()) {
                return true;
            }
        }
        return false;
    }

    private static java.util.Locale getFallbackLocaleForDefaultIme(java.util.List<android.view.inputmethod.InputMethodInfo> imis, android.content.Context context) {
        for (java.util.Locale fallbackLocale : SEARCH_ORDER_OF_FALLBACK_LOCALES) {
            for (int i = 0; i < imis.size(); i++) {
                if (isSystemImeThatHasSubtypeOf(imis.get(i), context, true, fallbackLocale, true, "keyboard")) {
                    return fallbackLocale;
                }
            }
        }
        for (java.util.Locale fallbackLocale2 : SEARCH_ORDER_OF_FALLBACK_LOCALES) {
            for (int i2 = 0; i2 < imis.size(); i2++) {
                if (isSystemImeThatHasSubtypeOf(imis.get(i2), context, false, fallbackLocale2, true, "keyboard")) {
                    return fallbackLocale2;
                }
            }
        }
        android.util.Slog.w(TAG, "Found no fallback locale. imis=" + java.util.Arrays.toString(imis.toArray()));
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSystemImeThatHasSubtypeOf(android.view.inputmethod.InputMethodInfo imi, android.content.Context context, boolean checkDefaultAttribute, java.util.Locale requiredLocale, boolean checkCountry, java.lang.String requiredSubtypeMode) {
        if (!imi.isSystem()) {
            return false;
        }
        if (!checkDefaultAttribute || imi.isDefault(context)) {
            return com.android.server.inputmethod.SubtypeUtils.containsSubtypeOf(imi, requiredLocale, checkCountry, requiredSubtypeMode);
        }
        return false;
    }

    static byte[] marshal(android.view.inputmethod.InputMethodInfo imi) {
        android.os.Parcel parcel = null;
        try {
            parcel = android.os.Parcel.obtain();
            parcel.writeTypedObject(imi, 0);
            return parcel.marshall();
        } finally {
            if (parcel != null) {
                parcel.recycle();
            }
        }
    }
}
