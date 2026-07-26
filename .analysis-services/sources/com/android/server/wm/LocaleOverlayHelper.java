package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class LocaleOverlayHelper {
    LocaleOverlayHelper() {
    }

    static android.os.LocaleList combineLocalesIfOverlayExists(android.os.LocaleList overlayLocales, android.os.LocaleList baseLocales) {
        if (overlayLocales == null || overlayLocales.isEmpty()) {
            return overlayLocales;
        }
        return combineLocales(overlayLocales, baseLocales);
    }

    private static android.os.LocaleList combineLocales(android.os.LocaleList overlayLocales, android.os.LocaleList baseLocales) {
        java.util.Locale[] combinedLocales = new java.util.Locale[overlayLocales.size() + baseLocales.size()];
        for (int i = 0; i < overlayLocales.size(); i++) {
            combinedLocales[i] = overlayLocales.get(i);
        }
        for (int i2 = 0; i2 < baseLocales.size(); i2++) {
            combinedLocales[overlayLocales.size() + i2] = baseLocales.get(i2);
        }
        return new android.os.LocaleList(combinedLocales);
    }
}
