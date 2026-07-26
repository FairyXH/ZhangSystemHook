package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class InputMethodSettings {
    public static final boolean DEBUG = false;
    private static final char INPUT_METHOD_SEPARATOR = ':';
    private static final char INPUT_METHOD_SUBTYPE_SEPARATOR = ';';
    static final int INVALID_SUBTYPE_HASHCODE = -1;
    private static final java.lang.String INVALID_SUBTYPE_HASHCODE_STR = java.lang.String.valueOf(-1);
    private static final java.lang.String TAG = "InputMethodSettings";
    private final java.util.List<android.view.inputmethod.InputMethodInfo> mMethodList;
    private final com.android.server.inputmethod.InputMethodMap mMethodMap;
    private final int mUserId;

    private static void buildEnabledInputMethodsSettingString(java.lang.StringBuilder builder, android.util.Pair<java.lang.String, java.util.ArrayList<java.lang.String>> ime) {
        builder.append((java.lang.String) ime.first);
        for (int i = 0; i < ((java.util.ArrayList) ime.second).size(); i++) {
            java.lang.String subtypeId = (java.lang.String) ((java.util.ArrayList) ime.second).get(i);
            builder.append(INPUT_METHOD_SUBTYPE_SEPARATOR).append(subtypeId);
        }
    }

    static com.android.server.inputmethod.InputMethodSettings createEmptyMap(int userId) {
        return new com.android.server.inputmethod.InputMethodSettings(com.android.server.inputmethod.InputMethodMap.emptyMap(), userId);
    }

    static com.android.server.inputmethod.InputMethodSettings create(com.android.server.inputmethod.InputMethodMap methodMap, int userId) {
        return new com.android.server.inputmethod.InputMethodSettings(methodMap, userId);
    }

    private InputMethodSettings(com.android.server.inputmethod.InputMethodMap methodMap, int userId) {
        this.mMethodMap = methodMap;
        this.mMethodList = methodMap.values();
        this.mUserId = userId;
    }

    com.android.server.inputmethod.InputMethodMap getMethodMap() {
        return this.mMethodMap;
    }

    java.util.List<android.view.inputmethod.InputMethodInfo> getMethodList() {
        return this.mMethodList;
    }

    private void putString(java.lang.String key, java.lang.String str) {
        com.android.server.inputmethod.SecureSettingsWrapper.putString(key, str, this.mUserId);
    }

    private java.lang.String getString(java.lang.String key, java.lang.String defaultValue) {
        return com.android.server.inputmethod.SecureSettingsWrapper.getString(key, defaultValue, this.mUserId);
    }

    private void putInt(java.lang.String key, int value) {
        com.android.server.inputmethod.SecureSettingsWrapper.putInt(key, value, this.mUserId);
    }

    private int getInt(java.lang.String key, int defaultValue) {
        return com.android.server.inputmethod.SecureSettingsWrapper.getInt(key, defaultValue, this.mUserId);
    }

    java.util.ArrayList<android.view.inputmethod.InputMethodInfo> getEnabledInputMethodList() {
        return getEnabledInputMethodListWithFilter(null);
    }

    java.util.ArrayList<android.view.inputmethod.InputMethodInfo> getEnabledInputMethodListWithFilter(java.util.function.Predicate<android.view.inputmethod.InputMethodInfo> matchingCondition) {
        return createEnabledInputMethodList(getEnabledInputMethodsAndSubtypeList(), matchingCondition);
    }

    java.util.List<android.view.inputmethod.InputMethodSubtype> getEnabledInputMethodSubtypeList(android.view.inputmethod.InputMethodInfo imi, boolean allowsImplicitlyEnabledSubtypes) {
        java.util.List<android.view.inputmethod.InputMethodSubtype> enabledSubtypes = getEnabledInputMethodSubtypeList(imi);
        if (allowsImplicitlyEnabledSubtypes && enabledSubtypes.isEmpty()) {
            enabledSubtypes = com.android.server.inputmethod.SubtypeUtils.getImplicitlyApplicableSubtypes(com.android.server.inputmethod.SystemLocaleWrapper.get(this.mUserId), imi);
        }
        return android.view.inputmethod.InputMethodSubtype.sort(imi, enabledSubtypes);
    }

    java.util.List<android.view.inputmethod.InputMethodSubtype> getEnabledInputMethodSubtypeList(android.view.inputmethod.InputMethodInfo imi) {
        java.util.List<android.util.Pair<java.lang.String, java.util.ArrayList<java.lang.String>>> imsList = getEnabledInputMethodsAndSubtypeList();
        java.util.ArrayList<android.view.inputmethod.InputMethodSubtype> enabledSubtypes = new java.util.ArrayList<>();
        if (imi != null) {
            int i = 0;
            while (true) {
                if (i >= imsList.size()) {
                    break;
                }
                android.util.Pair<java.lang.String, java.util.ArrayList<java.lang.String>> imsPair = imsList.get(i);
                android.view.inputmethod.InputMethodInfo info = this.mMethodMap.get((java.lang.String) imsPair.first);
                if (info == null || !info.getId().equals(imi.getId())) {
                    i++;
                } else {
                    int subtypeCount = info.getSubtypeCount();
                    for (int j = 0; j < subtypeCount; j++) {
                        android.view.inputmethod.InputMethodSubtype ims = info.getSubtypeAt(j);
                        for (int k = 0; k < ((java.util.ArrayList) imsPair.second).size(); k++) {
                            java.lang.String s = (java.lang.String) ((java.util.ArrayList) imsPair.second).get(k);
                            if (java.lang.String.valueOf(ims.hashCode()).equals(s)) {
                                enabledSubtypes.add(ims);
                            }
                        }
                    }
                }
            }
        }
        return enabledSubtypes;
    }

    java.util.List<android.util.Pair<java.lang.String, java.util.ArrayList<java.lang.String>>> getEnabledInputMethodsAndSubtypeList() {
        java.lang.String enabledInputMethodsStr = getEnabledInputMethodsStr();
        android.text.TextUtils.SimpleStringSplitter inputMethodSplitter = new android.text.TextUtils.SimpleStringSplitter(INPUT_METHOD_SEPARATOR);
        android.text.TextUtils.SimpleStringSplitter subtypeSplitter = new android.text.TextUtils.SimpleStringSplitter(INPUT_METHOD_SUBTYPE_SEPARATOR);
        java.util.ArrayList<android.util.Pair<java.lang.String, java.util.ArrayList<java.lang.String>>> imsList = new java.util.ArrayList<>();
        if (android.text.TextUtils.isEmpty(enabledInputMethodsStr)) {
            return imsList;
        }
        inputMethodSplitter.setString(enabledInputMethodsStr);
        while (inputMethodSplitter.hasNext()) {
            java.lang.String nextImsStr = inputMethodSplitter.next();
            subtypeSplitter.setString(nextImsStr);
            if (subtypeSplitter.hasNext()) {
                java.util.ArrayList<java.lang.String> subtypeHashes = new java.util.ArrayList<>();
                java.lang.String imeId = subtypeSplitter.next();
                while (subtypeSplitter.hasNext()) {
                    subtypeHashes.add(subtypeSplitter.next());
                }
                imsList.add(new android.util.Pair<>(imeId, subtypeHashes));
            }
        }
        return imsList;
    }

    boolean buildAndPutEnabledInputMethodsStrRemovingId(java.lang.StringBuilder builder, java.util.List<android.util.Pair<java.lang.String, java.util.ArrayList<java.lang.String>>> imsList, java.lang.String id) {
        boolean isRemoved = false;
        boolean needsAppendSeparator = false;
        for (int i = 0; i < imsList.size(); i++) {
            android.util.Pair<java.lang.String, java.util.ArrayList<java.lang.String>> ims = imsList.get(i);
            java.lang.String curId = (java.lang.String) ims.first;
            if (curId.equals(id)) {
                isRemoved = true;
            } else {
                if (needsAppendSeparator) {
                    builder.append(INPUT_METHOD_SEPARATOR);
                } else {
                    needsAppendSeparator = true;
                }
                buildEnabledInputMethodsSettingString(builder, ims);
            }
        }
        if (isRemoved) {
            putEnabledInputMethodsStr(builder.toString());
        }
        return isRemoved;
    }

    private java.util.ArrayList<android.view.inputmethod.InputMethodInfo> createEnabledInputMethodList(java.util.List<android.util.Pair<java.lang.String, java.util.ArrayList<java.lang.String>>> imsList, java.util.function.Predicate<android.view.inputmethod.InputMethodInfo> matchingCondition) {
        java.util.ArrayList<android.view.inputmethod.InputMethodInfo> res = new java.util.ArrayList<>();
        for (int i = 0; i < imsList.size(); i++) {
            android.util.Pair<java.lang.String, java.util.ArrayList<java.lang.String>> ims = imsList.get(i);
            android.view.inputmethod.InputMethodInfo info = this.mMethodMap.get((java.lang.String) ims.first);
            if (info != null && !info.isVrOnly() && (matchingCondition == null || matchingCondition.test(info))) {
                res.add(info);
            }
        }
        return res;
    }

    void putEnabledInputMethodsStr(java.lang.String str) {
        if (android.text.TextUtils.isEmpty(str)) {
            putString("enabled_input_methods", null);
        } else {
            putString("enabled_input_methods", str);
        }
    }

    java.lang.String getEnabledInputMethodsStr() {
        return getString("enabled_input_methods", "");
    }

    private void saveSubtypeHistory(java.util.List<android.util.Pair<java.lang.String, java.lang.String>> savedImes, java.lang.String newImeId, java.lang.String newSubtypeHashCodeStr) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        boolean isImeAdded = false;
        if (!android.text.TextUtils.isEmpty(newImeId) && !android.text.TextUtils.isEmpty(newSubtypeHashCodeStr)) {
            builder.append(newImeId).append(INPUT_METHOD_SUBTYPE_SEPARATOR).append(newSubtypeHashCodeStr);
            isImeAdded = true;
        }
        for (int i = 0; i < savedImes.size(); i++) {
            android.util.Pair<java.lang.String, java.lang.String> ime = savedImes.get(i);
            java.lang.String imeId = (java.lang.String) ime.first;
            java.lang.String subtypeHashCodeStr = (java.lang.String) ime.second;
            if (android.text.TextUtils.isEmpty(subtypeHashCodeStr)) {
                subtypeHashCodeStr = INVALID_SUBTYPE_HASHCODE_STR;
            }
            if (isImeAdded) {
                builder.append(INPUT_METHOD_SEPARATOR);
            } else {
                isImeAdded = true;
            }
            builder.append(imeId).append(INPUT_METHOD_SUBTYPE_SEPARATOR).append(subtypeHashCodeStr);
        }
        putSubtypeHistoryStr(builder.toString());
    }

    private void addSubtypeToHistory(java.lang.String imeId, java.lang.String subtypeHashCodeStr) {
        java.util.List<android.util.Pair<java.lang.String, java.lang.String>> subtypeHistory = loadInputMethodAndSubtypeHistory();
        int i = 0;
        while (true) {
            if (i >= subtypeHistory.size()) {
                break;
            }
            android.util.Pair<java.lang.String, java.lang.String> ime = subtypeHistory.get(i);
            if (!((java.lang.String) ime.first).equals(imeId)) {
                i++;
            } else {
                subtypeHistory.remove(ime);
                break;
            }
        }
        saveSubtypeHistory(subtypeHistory, imeId, subtypeHashCodeStr);
    }

    private void putSubtypeHistoryStr(java.lang.String str) {
        if (android.text.TextUtils.isEmpty(str)) {
            putString("input_methods_subtype_history", null);
        } else {
            putString("input_methods_subtype_history", str);
        }
    }

    android.util.Pair<java.lang.String, java.lang.String> getLastInputMethodAndSubtype() {
        return getLastSubtypeForInputMethodInternal(null);
    }

    android.view.inputmethod.InputMethodSubtype getLastInputMethodSubtype() {
        android.view.inputmethod.InputMethodInfo lastImi;
        android.util.Pair<java.lang.String, java.lang.String> lastIme = getLastInputMethodAndSubtype();
        if (lastIme == null || android.text.TextUtils.isEmpty((java.lang.CharSequence) lastIme.first) || android.text.TextUtils.isEmpty((java.lang.CharSequence) lastIme.second) || (lastImi = this.mMethodMap.get((java.lang.String) lastIme.first)) == null) {
            return null;
        }
        try {
            int lastSubtypeHash = java.lang.Integer.parseInt((java.lang.String) lastIme.second);
            int lastSubtypeId = com.android.server.inputmethod.SubtypeUtils.getSubtypeIdFromHashCode(lastImi, lastSubtypeHash);
            if (lastSubtypeId >= 0 && lastSubtypeId < lastImi.getSubtypeCount()) {
                return lastImi.getSubtypeAt(lastSubtypeId);
            }
            return null;
        } catch (java.lang.NumberFormatException e) {
            return null;
        }
    }

    java.lang.String getLastSubtypeForInputMethod(java.lang.String imeId) {
        android.util.Pair<java.lang.String, java.lang.String> ime = getLastSubtypeForInputMethodInternal(imeId);
        if (ime != null) {
            return (java.lang.String) ime.second;
        }
        return null;
    }

    private android.util.Pair<java.lang.String, java.lang.String> getLastSubtypeForInputMethodInternal(java.lang.String imeId) {
        java.util.List<android.util.Pair<java.lang.String, java.util.ArrayList<java.lang.String>>> enabledImes = getEnabledInputMethodsAndSubtypeList();
        java.util.List<android.util.Pair<java.lang.String, java.lang.String>> subtypeHistory = loadInputMethodAndSubtypeHistory();
        for (int i = 0; i < subtypeHistory.size(); i++) {
            android.util.Pair<java.lang.String, java.lang.String> imeAndSubtype = subtypeHistory.get(i);
            java.lang.String imeInTheHistory = (java.lang.String) imeAndSubtype.first;
            if (android.text.TextUtils.isEmpty(imeId) || imeInTheHistory.equals(imeId)) {
                java.lang.String subtypeInTheHistory = (java.lang.String) imeAndSubtype.second;
                java.lang.String subtypeHashCode = getEnabledSubtypeHashCodeForInputMethodAndSubtype(enabledImes, imeInTheHistory, subtypeInTheHistory);
                if (!android.text.TextUtils.isEmpty(subtypeHashCode)) {
                    return new android.util.Pair<>(imeInTheHistory, subtypeHashCode);
                }
            }
        }
        return null;
    }

    private java.lang.String getEnabledSubtypeHashCodeForInputMethodAndSubtype(java.util.List<android.util.Pair<java.lang.String, java.util.ArrayList<java.lang.String>>> enabledImes, java.lang.String imeId, java.lang.String subtypeHashCode) {
        android.os.LocaleList localeList = com.android.server.inputmethod.SystemLocaleWrapper.get(this.mUserId);
        for (int i = 0; i < enabledImes.size(); i++) {
            android.util.Pair<java.lang.String, java.util.ArrayList<java.lang.String>> enabledIme = enabledImes.get(i);
            if (((java.lang.String) enabledIme.first).equals(imeId)) {
                java.util.ArrayList<java.lang.String> explicitlyEnabledSubtypes = (java.util.ArrayList) enabledIme.second;
                android.view.inputmethod.InputMethodInfo imi = this.mMethodMap.get(imeId);
                if (explicitlyEnabledSubtypes.isEmpty()) {
                    if (imi != null && imi.getSubtypeCount() > 0) {
                        java.util.List<android.view.inputmethod.InputMethodSubtype> implicitlyEnabledSubtypes = com.android.server.inputmethod.SubtypeUtils.getImplicitlyApplicableSubtypes(localeList, imi);
                        int numSubtypes = implicitlyEnabledSubtypes.size();
                        for (int j = 0; j < numSubtypes; j++) {
                            android.view.inputmethod.InputMethodSubtype st = implicitlyEnabledSubtypes.get(j);
                            if (java.lang.String.valueOf(st.hashCode()).equals(subtypeHashCode)) {
                                return subtypeHashCode;
                            }
                        }
                    }
                } else {
                    for (int j2 = 0; j2 < explicitlyEnabledSubtypes.size(); j2++) {
                        java.lang.String s = explicitlyEnabledSubtypes.get(j2);
                        if (s.equals(subtypeHashCode)) {
                            try {
                                int hashCode = java.lang.Integer.parseInt(subtypeHashCode);
                                if (com.android.server.inputmethod.SubtypeUtils.isValidSubtypeHashCode(imi, hashCode)) {
                                    return s;
                                }
                                return INVALID_SUBTYPE_HASHCODE_STR;
                            } catch (java.lang.NumberFormatException e) {
                                return INVALID_SUBTYPE_HASHCODE_STR;
                            }
                        }
                    }
                }
                return INVALID_SUBTYPE_HASHCODE_STR;
            }
        }
        return null;
    }

    private java.util.List<android.util.Pair<java.lang.String, java.lang.String>> loadInputMethodAndSubtypeHistory() {
        java.util.ArrayList<android.util.Pair<java.lang.String, java.lang.String>> imsList = new java.util.ArrayList<>();
        java.lang.String subtypeHistoryStr = getSubtypeHistoryStr();
        if (android.text.TextUtils.isEmpty(subtypeHistoryStr)) {
            return imsList;
        }
        android.text.TextUtils.SimpleStringSplitter inputMethodSplitter = new android.text.TextUtils.SimpleStringSplitter(INPUT_METHOD_SEPARATOR);
        android.text.TextUtils.SimpleStringSplitter subtypeSplitter = new android.text.TextUtils.SimpleStringSplitter(INPUT_METHOD_SUBTYPE_SEPARATOR);
        inputMethodSplitter.setString(subtypeHistoryStr);
        while (inputMethodSplitter.hasNext()) {
            java.lang.String nextImsStr = inputMethodSplitter.next();
            subtypeSplitter.setString(nextImsStr);
            if (subtypeSplitter.hasNext()) {
                java.lang.String subtypeHashCodeStr = INVALID_SUBTYPE_HASHCODE_STR;
                java.lang.String imeId = subtypeSplitter.next();
                if (subtypeSplitter.hasNext()) {
                    subtypeHashCodeStr = subtypeSplitter.next();
                }
                imsList.add(new android.util.Pair<>(imeId, subtypeHashCodeStr));
            }
        }
        return imsList;
    }

    private java.lang.String getSubtypeHistoryStr() {
        java.lang.String history = getString("input_methods_subtype_history", "");
        return history;
    }

    void putSelectedInputMethod(java.lang.String imeId) {
        putString("default_input_method", imeId);
    }

    void putSelectedSubtype(int subtypeId) {
        putInt("selected_input_method_subtype", subtypeId);
    }

    java.lang.String getSelectedInputMethod() {
        java.lang.String imi = getString("default_input_method", null);
        return imi;
    }

    java.lang.String getSelectedDefaultDeviceInputMethod() {
        java.lang.String imi = getString("default_device_input_method", null);
        return imi;
    }

    void putSelectedDefaultDeviceInputMethod(java.lang.String imeId) {
        putString("default_device_input_method", imeId);
    }

    void putDefaultVoiceInputMethod(java.lang.String imeId) {
        putString("default_voice_input_method", imeId);
    }

    java.lang.String getDefaultVoiceInputMethod() {
        java.lang.String imi = getString("default_voice_input_method", null);
        return imi;
    }

    private int getSelectedInputMethodSubtypeHashCode() {
        return getInt("selected_input_method_subtype", -1);
    }

    public int getUserId() {
        return this.mUserId;
    }

    int getSelectedInputMethodSubtypeId(java.lang.String selectedImiId) {
        android.view.inputmethod.InputMethodInfo imi = this.mMethodMap.get(selectedImiId);
        if (imi == null) {
            return -1;
        }
        int subtypeHashCode = getSelectedInputMethodSubtypeHashCode();
        return com.android.server.inputmethod.SubtypeUtils.getSubtypeIdFromHashCode(imi, subtypeHashCode);
    }

    void saveCurrentInputMethodAndSubtypeToHistory(java.lang.String curMethodId, android.view.inputmethod.InputMethodSubtype currentSubtype) {
        java.lang.String subtypeHashCodeStr = INVALID_SUBTYPE_HASHCODE_STR;
        if (currentSubtype != null) {
            subtypeHashCodeStr = java.lang.String.valueOf(currentSubtype.hashCode());
        }
        if (com.android.server.inputmethod.InputMethodUtils.canAddToLastInputMethod(currentSubtype)) {
            addSubtypeToHistory(curMethodId, subtypeHashCodeStr);
        }
    }

    android.view.inputmethod.InputMethodSubtype getCurrentInputMethodSubtypeForNonCurrentUsers() {
        android.view.inputmethod.InputMethodInfo imi;
        int subtypeIndex;
        java.lang.String selectedMethodId = getSelectedInputMethod();
        if (selectedMethodId == null || (imi = this.mMethodMap.get(selectedMethodId)) == null || imi.getSubtypeCount() == 0) {
            return null;
        }
        int subtypeHashCode = getSelectedInputMethodSubtypeHashCode();
        if (subtypeHashCode != -1 && (subtypeIndex = com.android.server.inputmethod.SubtypeUtils.getSubtypeIdFromHashCode(imi, subtypeHashCode)) >= 0) {
            return imi.getSubtypeAt(subtypeIndex);
        }
        java.util.List<android.view.inputmethod.InputMethodSubtype> explicitlyOrImplicitlyEnabledSubtypes = getEnabledInputMethodSubtypeList(imi, true);
        if (explicitlyOrImplicitlyEnabledSubtypes.isEmpty()) {
            return null;
        }
        if (explicitlyOrImplicitlyEnabledSubtypes.size() == 1) {
            return explicitlyOrImplicitlyEnabledSubtypes.get(0);
        }
        java.lang.String locale = com.android.server.inputmethod.SystemLocaleWrapper.get(this.mUserId).get(0).toString();
        android.view.inputmethod.InputMethodSubtype subtype = com.android.server.inputmethod.SubtypeUtils.findLastResortApplicableSubtype(explicitlyOrImplicitlyEnabledSubtypes, "keyboard", locale, true);
        if (subtype != null) {
            return subtype;
        }
        return com.android.server.inputmethod.SubtypeUtils.findLastResortApplicableSubtype(explicitlyOrImplicitlyEnabledSubtypes, null, locale, true);
    }

    com.android.server.inputmethod.AdditionalSubtypeMap getNewAdditionalSubtypeMap(java.lang.String imeId, java.util.ArrayList<android.view.inputmethod.InputMethodSubtype> subtypes, com.android.server.inputmethod.AdditionalSubtypeMap additionalSubtypeMap, android.content.pm.PackageManagerInternal packageManagerInternal, int callingUid) {
        android.view.inputmethod.InputMethodInfo imi = this.mMethodMap.get(imeId);
        if (imi == null || !com.android.server.inputmethod.InputMethodUtils.checkIfPackageBelongsToUid(packageManagerInternal, callingUid, imi.getPackageName())) {
            return additionalSubtypeMap;
        }
        if (subtypes.isEmpty()) {
            com.android.server.inputmethod.AdditionalSubtypeMap newMap = additionalSubtypeMap.cloneWithRemoveOrSelf(imi.getId());
            return newMap;
        }
        com.android.server.inputmethod.AdditionalSubtypeMap newMap2 = additionalSubtypeMap.cloneWithPut(imi.getId(), subtypes);
        return newMap2;
    }

    boolean setEnabledInputMethodSubtypes(java.lang.String imeId, int[] subtypeHashCodes) {
        android.view.inputmethod.InputMethodInfo imi = this.mMethodMap.get(imeId);
        if (imi == null) {
            return false;
        }
        android.util.IntArray validSubtypeHashCodes = new android.util.IntArray(subtypeHashCodes.length);
        for (int subtypeHashCode : subtypeHashCodes) {
            if (subtypeHashCode != -1 && com.android.server.inputmethod.SubtypeUtils.isValidSubtypeHashCode(imi, subtypeHashCode) && validSubtypeHashCodes.indexOf(subtypeHashCode) < 0) {
                validSubtypeHashCodes.add(subtypeHashCode);
            }
        }
        java.lang.String originalEnabledImesString = getEnabledInputMethodsStr();
        java.lang.String updatedEnabledImesString = updateEnabledImeString(originalEnabledImesString, imi.getId(), validSubtypeHashCodes);
        if (android.text.TextUtils.equals(originalEnabledImesString, updatedEnabledImesString)) {
            return false;
        }
        putEnabledInputMethodsStr(updatedEnabledImesString);
        return true;
    }

    static java.lang.String updateEnabledImeString(java.lang.String enabledImesString, java.lang.String imeId, android.util.IntArray enabledSubtypeHashCodes) {
        android.text.TextUtils.SimpleStringSplitter imeSplitter = new android.text.TextUtils.SimpleStringSplitter(INPUT_METHOD_SEPARATOR);
        android.text.TextUtils.SimpleStringSplitter imeSubtypeSplitter = new android.text.TextUtils.SimpleStringSplitter(INPUT_METHOD_SUBTYPE_SEPARATOR);
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        imeSplitter.setString(enabledImesString);
        boolean needsImeSeparator = false;
        while (imeSplitter.hasNext()) {
            java.lang.String nextImsStr = imeSplitter.next();
            imeSubtypeSplitter.setString(nextImsStr);
            if (imeSubtypeSplitter.hasNext()) {
                if (needsImeSeparator) {
                    sb.append(INPUT_METHOD_SEPARATOR);
                }
                if (android.text.TextUtils.equals(imeId, imeSubtypeSplitter.next())) {
                    sb.append(imeId);
                    for (int i = 0; i < enabledSubtypeHashCodes.size(); i++) {
                        sb.append(INPUT_METHOD_SUBTYPE_SEPARATOR);
                        sb.append(enabledSubtypeHashCodes.get(i));
                    }
                } else {
                    sb.append(nextImsStr);
                }
                needsImeSeparator = true;
            }
        }
        return sb.toString();
    }

    void dump(android.util.Printer pw, java.lang.String prefix) {
        pw.println(prefix + "mUserId=" + this.mUserId);
    }
}
