package com.android.server.people.data;

/* JADX INFO: loaded from: classes2.dex */
class Utils {
    static java.lang.String getCurrentCountryIso(android.content.Context context) {
        android.location.Country country;
        java.lang.String countryIso = null;
        android.location.CountryDetector detector = (android.location.CountryDetector) context.getSystemService("country_detector");
        if (detector != null && (country = detector.detectCountry()) != null) {
            countryIso = country.getCountryIso();
        }
        if (countryIso == null) {
            return java.util.Locale.getDefault().getCountry();
        }
        return countryIso;
    }

    private Utils() {
    }
}
