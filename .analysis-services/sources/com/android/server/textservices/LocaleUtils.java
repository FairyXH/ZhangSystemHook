package com.android.server.textservices;

/* JADX INFO: loaded from: classes3.dex */
final class LocaleUtils {
    LocaleUtils() {
    }

    public static java.util.ArrayList<java.util.Locale> getSuitableLocalesForSpellChecker(java.util.Locale systemLocale) {
        java.util.Locale systemLocaleLanguageCountryVariant;
        java.util.Locale systemLocaleLanguageCountry;
        java.util.Locale systemLocaleLanguage;
        if (systemLocale != null) {
            java.lang.String language = systemLocale.getLanguage();
            boolean hasLanguage = !android.text.TextUtils.isEmpty(language);
            java.lang.String country = systemLocale.getCountry();
            boolean hasCountry = !android.text.TextUtils.isEmpty(country);
            java.lang.String variant = systemLocale.getVariant();
            boolean hasVariant = !android.text.TextUtils.isEmpty(variant);
            if (hasLanguage && hasCountry && hasVariant) {
                systemLocaleLanguageCountryVariant = new java.util.Locale(language, country, variant);
            } else {
                systemLocaleLanguageCountryVariant = null;
            }
            if (hasLanguage && hasCountry) {
                systemLocaleLanguageCountry = new java.util.Locale(language, country);
            } else {
                systemLocaleLanguageCountry = null;
            }
            if (hasLanguage) {
                systemLocaleLanguage = new java.util.Locale(language);
            } else {
                systemLocaleLanguage = null;
            }
        } else {
            systemLocaleLanguageCountryVariant = null;
            systemLocaleLanguageCountry = null;
            systemLocaleLanguage = null;
        }
        java.util.ArrayList<java.util.Locale> locales = new java.util.ArrayList<>();
        if (systemLocaleLanguageCountryVariant != null) {
            locales.add(systemLocaleLanguageCountryVariant);
        }
        if (java.util.Locale.ENGLISH.equals(systemLocaleLanguage)) {
            if (systemLocaleLanguageCountry != null) {
                if (systemLocaleLanguageCountry != null) {
                    locales.add(systemLocaleLanguageCountry);
                }
                if (!java.util.Locale.US.equals(systemLocaleLanguageCountry)) {
                    locales.add(java.util.Locale.US);
                }
                if (!java.util.Locale.UK.equals(systemLocaleLanguageCountry)) {
                    locales.add(java.util.Locale.UK);
                }
                locales.add(java.util.Locale.ENGLISH);
            } else {
                locales.add(java.util.Locale.ENGLISH);
                locales.add(java.util.Locale.US);
                locales.add(java.util.Locale.UK);
            }
        } else {
            if (systemLocaleLanguageCountry != null) {
                locales.add(systemLocaleLanguageCountry);
            }
            if (systemLocaleLanguage != null) {
                locales.add(systemLocaleLanguage);
            }
            locales.add(java.util.Locale.US);
            locales.add(java.util.Locale.UK);
            locales.add(java.util.Locale.ENGLISH);
        }
        return locales;
    }
}
