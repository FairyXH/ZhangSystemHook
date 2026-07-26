package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class SubtypeUtils {
    public static final boolean DEBUG = false;
    static final int NOT_A_SUBTYPE_ID = -1;
    static final java.lang.String SUBTYPE_MODE_KEYBOARD = "keyboard";
    private static final java.lang.String TAG = "SubtypeUtils";
    private static final java.lang.String TAG_ENABLED_WHEN_DEFAULT_IS_NOT_ASCII_CAPABLE = "EnabledWhenDefaultIsNotAsciiCapable";
    private static android.view.inputmethod.InputMethodInfo sCachedInputMethodInfo;
    private static java.util.ArrayList<android.view.inputmethod.InputMethodSubtype> sCachedResult;
    private static android.os.LocaleList sCachedSystemLocales;
    static final java.lang.String SUBTYPE_MODE_ANY = null;
    private static final java.lang.Object sCacheLock = new java.lang.Object();
    private static final com.android.server.inputmethod.LocaleUtils.LocaleExtractor<android.view.inputmethod.InputMethodSubtype> sSubtypeToLocale = new com.android.server.inputmethod.LocaleUtils.LocaleExtractor() { // from class: com.android.server.inputmethod.SubtypeUtils$$ExternalSyntheticLambda0
        @Override // com.android.server.inputmethod.LocaleUtils.LocaleExtractor
        public final java.util.Locale get(java.lang.Object obj) {
            return com.android.server.inputmethod.SubtypeUtils.lambda$static$0((android.view.inputmethod.InputMethodSubtype) obj);
        }
    };

    SubtypeUtils() {
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0051  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static boolean containsSubtypeOf(android.view.inputmethod.InputMethodInfo r7, java.util.Locale r8, boolean r9, java.lang.String r10) {
        /*
            r0 = 0
            if (r8 != 0) goto L4
            return r0
        L4:
            int r1 = r7.getSubtypeCount()
            r2 = 0
        L9:
            if (r2 >= r1) goto L67
            android.view.inputmethod.InputMethodSubtype r3 = r7.getSubtypeAt(r2)
            if (r9 == 0) goto L35
            java.util.Locale r4 = r3.getLocaleObject()
            if (r4 == 0) goto L62
            java.lang.String r5 = r4.getLanguage()
            java.lang.String r6 = r8.getLanguage()
            boolean r5 = android.text.TextUtils.equals(r5, r6)
            if (r5 == 0) goto L62
            java.lang.String r5 = r4.getCountry()
            java.lang.String r6 = r8.getCountry()
            boolean r5 = android.text.TextUtils.equals(r5, r6)
            if (r5 != 0) goto L34
            goto L62
        L34:
            goto L51
        L35:
            java.util.Locale r4 = new java.util.Locale
            java.lang.String r5 = r3.getLocale()
            java.lang.String r5 = com.android.server.inputmethod.LocaleUtils.getLanguageFromLocaleString(r5)
            r4.<init>(r5)
            java.lang.String r5 = r4.getLanguage()
            java.lang.String r6 = r8.getLanguage()
            boolean r5 = android.text.TextUtils.equals(r5, r6)
            if (r5 != 0) goto L51
            goto L62
        L51:
            boolean r4 = android.text.TextUtils.isEmpty(r10)
            if (r4 != 0) goto L65
            java.lang.String r4 = r3.getMode()
            boolean r4 = r10.equalsIgnoreCase(r4)
            if (r4 == 0) goto L62
            goto L65
        L62:
            int r2 = r2 + 1
            goto L9
        L65:
            r0 = 1
            return r0
        L67:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.inputmethod.SubtypeUtils.containsSubtypeOf(android.view.inputmethod.InputMethodInfo, java.util.Locale, boolean, java.lang.String):boolean");
    }

    static java.util.ArrayList<android.view.inputmethod.InputMethodSubtype> getSubtypes(android.view.inputmethod.InputMethodInfo imi) {
        java.util.ArrayList<android.view.inputmethod.InputMethodSubtype> subtypes = new java.util.ArrayList<>();
        int subtypeCount = imi.getSubtypeCount();
        for (int i = 0; i < subtypeCount; i++) {
            subtypes.add(imi.getSubtypeAt(i));
        }
        return subtypes;
    }

    static boolean isValidSubtypeHashCode(android.view.inputmethod.InputMethodInfo imi, int subtypeHashCode) {
        return getSubtypeIdFromHashCode(imi, subtypeHashCode) != -1;
    }

    static int getSubtypeIdFromHashCode(android.view.inputmethod.InputMethodInfo imi, int subtypeHashCode) {
        if (imi != null) {
            int subtypeCount = imi.getSubtypeCount();
            for (int i = 0; i < subtypeCount; i++) {
                android.view.inputmethod.InputMethodSubtype ims = imi.getSubtypeAt(i);
                if (subtypeHashCode == ims.hashCode()) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    static /* synthetic */ java.util.Locale lambda$static$0(android.view.inputmethod.InputMethodSubtype source) {
        if (source != null) {
            return source.getLocaleObject();
        }
        return null;
    }

    static java.util.ArrayList<android.view.inputmethod.InputMethodSubtype> getImplicitlyApplicableSubtypes(android.os.LocaleList systemLocales, android.view.inputmethod.InputMethodInfo imi) {
        synchronized (sCacheLock) {
            if (systemLocales.equals(sCachedSystemLocales) && sCachedInputMethodInfo == imi) {
                return new java.util.ArrayList<>(sCachedResult);
            }
            java.util.ArrayList<android.view.inputmethod.InputMethodSubtype> result = getImplicitlyApplicableSubtypesImpl(systemLocales, imi);
            synchronized (sCacheLock) {
                sCachedSystemLocales = systemLocales;
                sCachedInputMethodInfo = imi;
                sCachedResult = new java.util.ArrayList<>(result);
            }
            return result;
        }
    }

    private static java.util.ArrayList<android.view.inputmethod.InputMethodSubtype> getImplicitlyApplicableSubtypesImpl(android.os.LocaleList systemLocales, android.view.inputmethod.InputMethodInfo imi) {
        android.view.inputmethod.InputMethodSubtype lastResortKeyboardSubtype;
        java.util.List<android.view.inputmethod.InputMethodSubtype> subtypes = getSubtypes(imi);
        java.lang.String systemLocale = systemLocales.get(0).toString();
        if (android.text.TextUtils.isEmpty(systemLocale)) {
            return new java.util.ArrayList<>();
        }
        int numSubtypes = subtypes.size();
        android.util.ArrayMap<java.lang.String, android.view.inputmethod.InputMethodSubtype> applicableModeAndSubtypesMap = new android.util.ArrayMap<>();
        for (int i = 0; i < numSubtypes; i++) {
            android.view.inputmethod.InputMethodSubtype subtype = subtypes.get(i);
            if (subtype.overridesImplicitlyEnabledSubtype()) {
                java.lang.String mode = subtype.getMode();
                if (!applicableModeAndSubtypesMap.containsKey(mode)) {
                    applicableModeAndSubtypesMap.put(mode, subtype);
                }
            }
        }
        int i2 = applicableModeAndSubtypesMap.size();
        if (i2 > 0) {
            return new java.util.ArrayList<>(applicableModeAndSubtypesMap.values());
        }
        android.util.ArrayMap<java.lang.String, java.util.ArrayList<android.view.inputmethod.InputMethodSubtype>> nonKeyboardSubtypesMap = new android.util.ArrayMap<>();
        java.util.ArrayList<android.view.inputmethod.InputMethodSubtype> keyboardSubtypes = new java.util.ArrayList<>();
        for (int i3 = 0; i3 < numSubtypes; i3++) {
            android.view.inputmethod.InputMethodSubtype subtype2 = subtypes.get(i3);
            java.lang.String mode2 = subtype2.getMode();
            if (SUBTYPE_MODE_KEYBOARD.equals(mode2)) {
                keyboardSubtypes.add(subtype2);
            } else {
                if (!nonKeyboardSubtypesMap.containsKey(mode2)) {
                    nonKeyboardSubtypesMap.put(mode2, new java.util.ArrayList<>());
                }
                nonKeyboardSubtypesMap.get(mode2).add(subtype2);
            }
        }
        java.util.ArrayList<android.view.inputmethod.InputMethodSubtype> applicableSubtypes = new java.util.ArrayList<>();
        com.android.server.inputmethod.LocaleUtils.filterByLanguage(keyboardSubtypes, sSubtypeToLocale, systemLocales, applicableSubtypes);
        if (!applicableSubtypes.isEmpty()) {
            boolean hasAsciiCapableKeyboard = false;
            int numApplicationSubtypes = applicableSubtypes.size();
            int i4 = 0;
            while (true) {
                if (i4 >= numApplicationSubtypes) {
                    break;
                }
                if (!applicableSubtypes.get(i4).isAsciiCapable()) {
                    i4++;
                } else {
                    hasAsciiCapableKeyboard = true;
                    break;
                }
            }
            if (!hasAsciiCapableKeyboard) {
                int numKeyboardSubtypes = keyboardSubtypes.size();
                for (int i5 = 0; i5 < numKeyboardSubtypes; i5++) {
                    android.view.inputmethod.InputMethodSubtype subtype3 = keyboardSubtypes.get(i5);
                    if (SUBTYPE_MODE_KEYBOARD.equals(subtype3.getMode()) && subtype3.containsExtraValueKey(TAG_ENABLED_WHEN_DEFAULT_IS_NOT_ASCII_CAPABLE)) {
                        applicableSubtypes.add(subtype3);
                    }
                }
            }
        }
        boolean hasAsciiCapableKeyboard2 = applicableSubtypes.isEmpty();
        if (hasAsciiCapableKeyboard2 && (lastResortKeyboardSubtype = findLastResortApplicableSubtype(subtypes, SUBTYPE_MODE_KEYBOARD, systemLocale, true)) != null) {
            applicableSubtypes.add(lastResortKeyboardSubtype);
        }
        for (java.util.ArrayList<android.view.inputmethod.InputMethodSubtype> subtypeList : nonKeyboardSubtypesMap.values()) {
            com.android.server.inputmethod.LocaleUtils.filterByLanguage(subtypeList, sSubtypeToLocale, systemLocales, applicableSubtypes);
        }
        return applicableSubtypes;
    }

    static android.view.inputmethod.InputMethodSubtype findLastResortApplicableSubtype(java.util.List<android.view.inputmethod.InputMethodSubtype> subtypes, java.lang.String mode, java.lang.String locale, boolean canIgnoreLocaleAsLastResort) {
        if (subtypes == null || subtypes.isEmpty()) {
            return null;
        }
        java.lang.String language = com.android.server.inputmethod.LocaleUtils.getLanguageFromLocaleString(locale);
        boolean partialMatchFound = false;
        android.view.inputmethod.InputMethodSubtype applicableSubtype = null;
        android.view.inputmethod.InputMethodSubtype firstMatchedModeSubtype = null;
        int numSubtypes = subtypes.size();
        int i = 0;
        while (true) {
            if (i >= numSubtypes) {
                break;
            }
            android.view.inputmethod.InputMethodSubtype subtype = subtypes.get(i);
            java.lang.String subtypeLocale = subtype.getLocale();
            java.lang.String subtypeLanguage = com.android.server.inputmethod.LocaleUtils.getLanguageFromLocaleString(subtypeLocale);
            if (mode == null || subtypes.get(i).getMode().equalsIgnoreCase(mode)) {
                if (firstMatchedModeSubtype == null) {
                    firstMatchedModeSubtype = subtype;
                }
                if (locale.equals(subtypeLocale)) {
                    applicableSubtype = subtype;
                    break;
                }
                if (!partialMatchFound && language.equals(subtypeLanguage)) {
                    applicableSubtype = subtype;
                    partialMatchFound = true;
                }
            }
            i++;
        }
        if (applicableSubtype == null && canIgnoreLocaleAsLastResort) {
            return firstMatchedModeSubtype;
        }
        return applicableSubtype;
    }

    static android.view.inputmethod.InputMethodSubtype getCurrentInputMethodSubtype(android.view.inputmethod.InputMethodInfo imi, com.android.server.inputmethod.InputMethodSettings settings, android.view.inputmethod.InputMethodSubtype currentSubtype) {
        int userId = settings.getUserId();
        int selectedSubtypeHashCode = com.android.server.inputmethod.SecureSettingsWrapper.getInt("selected_input_method_subtype", -1, userId);
        if (selectedSubtypeHashCode != -1 && currentSubtype != null && isValidSubtypeHashCode(imi, currentSubtype.hashCode())) {
            return currentSubtype;
        }
        int subtypeId = settings.getSelectedInputMethodSubtypeId(imi.getId());
        if (subtypeId != -1) {
            return imi.getSubtypeAt(subtypeId);
        }
        java.util.List<android.view.inputmethod.InputMethodSubtype> subtypes = settings.getEnabledInputMethodSubtypeList(imi, true);
        if (!subtypes.isEmpty()) {
            if (subtypes.size() == 1) {
                return subtypes.get(0);
            }
            java.lang.String locale = com.android.server.inputmethod.SystemLocaleWrapper.get(userId).get(0).toString();
            android.view.inputmethod.InputMethodSubtype subtype = findLastResortApplicableSubtype(subtypes, SUBTYPE_MODE_KEYBOARD, locale, true);
            if (subtype == null) {
                return findLastResortApplicableSubtype(subtypes, null, locale, true);
            }
            return subtype;
        }
        return currentSubtype;
    }
}
