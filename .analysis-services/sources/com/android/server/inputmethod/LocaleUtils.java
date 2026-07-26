package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class LocaleUtils {

    public interface LocaleExtractor<T> {
        java.util.Locale get(T t);
    }

    LocaleUtils() {
    }

    private static byte calculateMatchingSubScore(android.icu.util.ULocale supported, android.icu.util.ULocale desired) {
        if (supported.equals(desired)) {
            return (byte) 4;
        }
        android.icu.util.ULocale maxDesired = android.icu.util.ULocale.addLikelySubtags(desired);
        java.lang.String supportedScript = supported.getScript();
        if (supportedScript.isEmpty() || !supportedScript.equals(maxDesired.getScript())) {
            return (byte) 1;
        }
        java.lang.String supportedCountry = supported.getCountry();
        if (supportedCountry.isEmpty() || !supportedCountry.equals(maxDesired.getCountry())) {
            return (byte) 2;
        }
        java.lang.String desiredScript = desired.getScript();
        java.lang.String desiredCountry = desired.getCountry();
        if (desiredScript.isEmpty() || desiredScript.equals(maxDesired.getScript())) {
            return (desiredCountry.isEmpty() || desiredCountry.equals(maxDesired.getCountry())) ? (byte) 4 : (byte) 3;
        }
        return (byte) 3;
    }

    private static final class ScoreEntry implements java.lang.Comparable<com.android.server.inputmethod.LocaleUtils.ScoreEntry> {
        public int mIndex = -1;
        public final byte[] mScore;

        ScoreEntry(byte[] score, int index) {
            this.mScore = new byte[score.length];
            set(score, index);
        }

        private void set(byte[] score, int index) {
            for (int i = 0; i < this.mScore.length; i++) {
                this.mScore[i] = score[i];
            }
            this.mIndex = index;
        }

        public void updateIfBetter(byte[] score, int index) {
            if (compare(this.mScore, score) == -1) {
                set(score, index);
            }
        }

        private static int compare(byte[] left, byte[] right) {
            for (int i = 0; i < left.length; i++) {
                if (left[i] > right[i]) {
                    return 1;
                }
                if (left[i] < right[i]) {
                    return -1;
                }
            }
            return 0;
        }

        @Override // java.lang.Comparable
        public int compareTo(com.android.server.inputmethod.LocaleUtils.ScoreEntry other) {
            return compare(this.mScore, other.mScore) * (-1);
        }
    }

    public static <T> void filterByLanguage(java.util.List<T> sources, com.android.server.inputmethod.LocaleUtils.LocaleExtractor<T> extractor, android.os.LocaleList preferredLocales, java.util.ArrayList<T> dest) {
        int i;
        if (preferredLocales.isEmpty()) {
            return;
        }
        int numPreferredLocales = preferredLocales.size();
        android.util.ArrayMap<java.lang.String, com.android.server.inputmethod.LocaleUtils.ScoreEntry> scoreboard = new android.util.ArrayMap<>();
        byte[] score = new byte[numPreferredLocales];
        android.icu.util.ULocale[] preferredULocaleCache = new android.icu.util.ULocale[numPreferredLocales];
        int sourceSize = sources.size();
        int i2 = 0;
        while (true) {
            if (i2 >= sourceSize) {
                break;
            }
            java.util.Locale locale = extractor.get(sources.get(i2));
            if (locale != null) {
                boolean canSkip = true;
                for (int j = 0; j < numPreferredLocales; j++) {
                    java.util.Locale preferredLocale = preferredLocales.get(j);
                    if (!android.text.TextUtils.equals(locale.getLanguage(), preferredLocale.getLanguage())) {
                        score[j] = 0;
                    } else {
                        if (preferredULocaleCache[j] == null) {
                            preferredULocaleCache[j] = android.icu.util.ULocale.addLikelySubtags(android.icu.util.ULocale.forLocale(preferredLocale));
                        }
                        score[j] = calculateMatchingSubScore(preferredULocaleCache[j], android.icu.util.ULocale.forLocale(locale));
                        if (canSkip && score[j] != 0) {
                            canSkip = false;
                        }
                    }
                }
                if (!canSkip) {
                    java.lang.String lang = locale.getLanguage();
                    com.android.server.inputmethod.LocaleUtils.ScoreEntry bestScore = scoreboard.get(lang);
                    if (bestScore == null) {
                        scoreboard.put(lang, new com.android.server.inputmethod.LocaleUtils.ScoreEntry(score, i2));
                    } else {
                        bestScore.updateIfBetter(score, i2);
                    }
                }
            }
            i2++;
        }
        int numEntries = scoreboard.size();
        com.android.server.inputmethod.LocaleUtils.ScoreEntry[] result = new com.android.server.inputmethod.LocaleUtils.ScoreEntry[numEntries];
        for (int i3 = 0; i3 < numEntries; i3++) {
            result[i3] = scoreboard.valueAt(i3);
        }
        java.util.Arrays.sort(result);
        for (com.android.server.inputmethod.LocaleUtils.ScoreEntry entry : result) {
            dest.add(sources.get(entry.mIndex));
        }
    }

    static java.lang.String getLanguageFromLocaleString(java.lang.String locale) {
        int idx = locale.indexOf(95);
        if (idx < 0) {
            return locale;
        }
        return locale.substring(0, idx);
    }

    static java.util.Locale getSystemLocaleFromContext(android.content.Context context) {
        try {
            return context.getResources().getConfiguration().locale;
        } catch (android.content.res.Resources.NotFoundException e) {
            return null;
        }
    }
}
