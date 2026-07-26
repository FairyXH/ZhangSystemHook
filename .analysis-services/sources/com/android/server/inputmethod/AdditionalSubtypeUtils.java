package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class AdditionalSubtypeUtils {
    private static final java.lang.String ADDITIONAL_SUBTYPES_FILE_NAME = "subtypes.xml";
    private static final java.lang.String ATTR_ICON = "icon";
    private static final java.lang.String ATTR_ID = "id";
    private static final java.lang.String ATTR_IME_SUBTYPE_EXTRA_VALUE = "imeSubtypeExtraValue";
    private static final java.lang.String ATTR_IME_SUBTYPE_ID = "subtypeId";
    private static final java.lang.String ATTR_IME_SUBTYPE_LANGUAGE_TAG = "languageTag";
    private static final java.lang.String ATTR_IME_SUBTYPE_LOCALE = "imeSubtypeLocale";
    private static final java.lang.String ATTR_IME_SUBTYPE_MODE = "imeSubtypeMode";
    private static final java.lang.String ATTR_IS_ASCII_CAPABLE = "isAsciiCapable";
    private static final java.lang.String ATTR_IS_AUXILIARY = "isAuxiliary";
    private static final java.lang.String ATTR_LABEL = "label";
    private static final java.lang.String ATTR_NAME_OVERRIDE = "nameOverride";
    private static final java.lang.String ATTR_NAME_PK_LANGUAGE_TAG = "pkLanguageTag";
    private static final java.lang.String ATTR_NAME_PK_LAYOUT_TYPE = "pkLayoutType";
    private static final java.lang.String INPUT_METHOD_PATH = "inputmethod";
    private static final java.lang.String NODE_IMI = "imi";
    private static final java.lang.String NODE_SUBTYPE = "subtype";
    private static final java.lang.String NODE_SUBTYPES = "subtypes";
    private static final java.lang.String SYSTEM_PATH = "system";
    private static final java.lang.String TAG = "AdditionalSubtypeUtils";

    private AdditionalSubtypeUtils() {
    }

    private static java.io.File getInputMethodDir(int userId) {
        java.io.File systemDir;
        if (userId == 0) {
            systemDir = new java.io.File(android.os.Environment.getDataDirectory(), "system");
        } else {
            systemDir = android.os.Environment.getUserSystemDirectory(userId);
        }
        return new java.io.File(systemDir, INPUT_METHOD_PATH);
    }

    private static android.util.AtomicFile getAdditionalSubtypeFile(java.io.File inputMethodDir) {
        java.io.File subtypeFile = new java.io.File(inputMethodDir, ADDITIONAL_SUBTYPES_FILE_NAME);
        return new android.util.AtomicFile(subtypeFile, "input-subtypes");
    }

    static void save(com.android.server.inputmethod.AdditionalSubtypeMap allSubtypes, com.android.server.inputmethod.InputMethodMap methodMap, int userId) throws java.lang.Throwable {
        java.io.File inputMethodDir = getInputMethodDir(userId);
        if (allSubtypes.isEmpty()) {
            if (!inputMethodDir.exists()) {
                return;
            }
            android.util.AtomicFile subtypesFile = getAdditionalSubtypeFile(inputMethodDir);
            if (subtypesFile.exists()) {
                subtypesFile.delete();
            }
            if (android.os.FileUtils.listFilesOrEmpty(inputMethodDir).length == 0 && !inputMethodDir.delete()) {
                android.util.Slog.e(TAG, "Failed to delete the empty parent directory " + inputMethodDir);
                return;
            }
            return;
        }
        if (!inputMethodDir.exists() && !inputMethodDir.mkdirs()) {
            android.util.Slog.e(TAG, "Failed to create a parent directory " + inputMethodDir);
        } else {
            saveToFile(allSubtypes, methodMap, getAdditionalSubtypeFile(inputMethodDir));
        }
    }

    static void saveToFile(com.android.server.inputmethod.AdditionalSubtypeMap allSubtypes, com.android.server.inputmethod.InputMethodMap methodMap, android.util.AtomicFile subtypesFile) throws java.lang.Throwable {
        java.util.Iterator<android.view.inputmethod.InputMethodSubtype> it;
        com.android.server.inputmethod.InputMethodMap inputMethodMap = methodMap;
        boolean isSetMethodMap = inputMethodMap != null && methodMap.size() > 0;
        java.io.FileOutputStream fos = null;
        try {
            fos = subtypesFile.startWrite();
            com.android.modules.utils.TypedXmlSerializer out = android.util.Xml.resolveSerializer(fos);
            java.lang.String str = null;
            out.startDocument((java.lang.String) null, true);
            out.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            out.startTag((java.lang.String) null, NODE_SUBTYPES);
            for (java.lang.String imiId : allSubtypes.keySet()) {
                if (isSetMethodMap && !inputMethodMap.containsKey(imiId)) {
                    android.util.Slog.w(TAG, "IME uninstalled or not valid.: " + imiId);
                } else {
                    try {
                        try {
                            java.util.List<android.view.inputmethod.InputMethodSubtype> subtypesList = allSubtypes.get(imiId);
                            if (subtypesList == null) {
                                android.util.Slog.e(TAG, "Null subtype list for IME " + imiId);
                            } else {
                                out.startTag(str, NODE_IMI);
                                out.attribute(str, ATTR_ID, imiId);
                                java.util.Iterator<android.view.inputmethod.InputMethodSubtype> it2 = subtypesList.iterator();
                                while (it2.hasNext()) {
                                    android.view.inputmethod.InputMethodSubtype subtype = it2.next();
                                    out.startTag(str, NODE_SUBTYPE);
                                    if (!subtype.hasSubtypeId()) {
                                        it = it2;
                                    } else {
                                        it = it2;
                                        out.attributeInt((java.lang.String) null, ATTR_IME_SUBTYPE_ID, subtype.getSubtypeId());
                                    }
                                    out.attributeInt((java.lang.String) null, ATTR_ICON, subtype.getIconResId());
                                    out.attributeInt((java.lang.String) null, ATTR_LABEL, subtype.getNameResId());
                                    out.attribute((java.lang.String) null, ATTR_NAME_OVERRIDE, subtype.getNameOverride().toString());
                                    android.icu.util.ULocale pkLanguageTag = subtype.getPhysicalKeyboardHintLanguageTag();
                                    if (pkLanguageTag != null) {
                                        out.attribute((java.lang.String) null, ATTR_NAME_PK_LANGUAGE_TAG, pkLanguageTag.toLanguageTag());
                                    }
                                    out.attribute((java.lang.String) null, ATTR_NAME_PK_LAYOUT_TYPE, subtype.getPhysicalKeyboardHintLayoutType());
                                    out.attribute((java.lang.String) null, ATTR_IME_SUBTYPE_LOCALE, subtype.getLocale());
                                    out.attribute((java.lang.String) null, ATTR_IME_SUBTYPE_LANGUAGE_TAG, subtype.getLanguageTag());
                                    out.attribute((java.lang.String) null, ATTR_IME_SUBTYPE_MODE, subtype.getMode());
                                    out.attribute((java.lang.String) null, ATTR_IME_SUBTYPE_EXTRA_VALUE, subtype.getExtraValue());
                                    out.attributeInt((java.lang.String) null, ATTR_IS_AUXILIARY, subtype.isAuxiliary() ? 1 : 0);
                                    out.attributeInt((java.lang.String) null, ATTR_IS_ASCII_CAPABLE, subtype.isAsciiCapable() ? 1 : 0);
                                    out.endTag((java.lang.String) null, NODE_SUBTYPE);
                                    it2 = it;
                                    str = null;
                                }
                                out.endTag((java.lang.String) null, NODE_IMI);
                                inputMethodMap = methodMap;
                                str = null;
                            }
                        } catch (java.io.IOException e) {
                            e = e;
                            android.util.Slog.w(TAG, "Error writing subtypes", e);
                            if (fos != null) {
                                subtypesFile.failWrite(fos);
                            }
                            libcore.io.IoUtils.closeQuietly(fos);
                        }
                    } catch (java.lang.Throwable th) {
                        th = th;
                        libcore.io.IoUtils.closeQuietly(fos);
                        throw th;
                    }
                }
            }
            out.endTag((java.lang.String) null, NODE_SUBTYPES);
            out.endDocument();
            subtypesFile.finishWrite(fos);
        } catch (java.io.IOException e2) {
            e = e2;
        } catch (java.lang.Throwable th2) {
            th = th2;
            libcore.io.IoUtils.closeQuietly(fos);
            throw th;
        }
        libcore.io.IoUtils.closeQuietly(fos);
    }

    static com.android.server.inputmethod.AdditionalSubtypeMap load(int userId) {
        android.util.AtomicFile subtypesFile = getAdditionalSubtypeFile(getInputMethodDir(userId));
        if (subtypesFile.exists()) {
            return loadFromFile(subtypesFile);
        }
        return com.android.server.inputmethod.AdditionalSubtypeMap.EMPTY_MAP;
    }

    static com.android.server.inputmethod.AdditionalSubtypeMap loadFromFile(android.util.AtomicFile subtypesFile) {
        android.util.ArrayMap<java.lang.String, java.util.List<android.view.inputmethod.InputMethodSubtype>> allSubtypes;
        java.io.FileInputStream fis;
        java.io.FileInputStream fis2;
        java.lang.Throwable th;
        int i;
        int i2;
        java.io.FileInputStream fis3;
        java.lang.String str;
        android.util.ArrayMap<java.lang.String, java.util.List<android.view.inputmethod.InputMethodSubtype>> allSubtypes2;
        int type;
        java.lang.String firstNodeName;
        int depth;
        java.lang.String str2;
        android.util.ArrayMap<java.lang.String, java.util.List<android.view.inputmethod.InputMethodSubtype>> allSubtypes3;
        int type2;
        java.lang.String firstNodeName2;
        int depth2;
        java.lang.String str3;
        android.icu.util.ULocale uLocale;
        java.lang.String str4 = "1";
        java.lang.String str5 = TAG;
        android.util.ArrayMap<java.lang.String, java.util.List<android.view.inputmethod.InputMethodSubtype>> allSubtypes4 = new android.util.ArrayMap<>();
        try {
            try {
                fis = subtypesFile.openRead();
            } catch (java.io.IOException | java.lang.NumberFormatException | org.xmlpull.v1.XmlPullParserException e) {
                e = e;
                allSubtypes = allSubtypes4;
                android.util.Slog.w(TAG, "Error reading subtypes", e);
                return com.android.server.inputmethod.AdditionalSubtypeMap.of(allSubtypes);
            }
        } catch (java.io.IOException | java.lang.NumberFormatException | org.xmlpull.v1.XmlPullParserException e2) {
            e = e2;
            android.util.Slog.w(TAG, "Error reading subtypes", e);
            return com.android.server.inputmethod.AdditionalSubtypeMap.of(allSubtypes);
        }
        try {
            com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(fis);
            int type3 = parser.next();
            while (true) {
                i = 1;
                i2 = 2;
                if (type3 == 2 || type3 == 1) {
                    break;
                }
                try {
                    type3 = parser.next();
                } catch (java.lang.Throwable th2) {
                    fis2 = fis;
                    th = th2;
                }
            }
            java.lang.String firstNodeName3 = parser.getName();
            try {
                if (!NODE_SUBTYPES.equals(firstNodeName3)) {
                    throw new org.xmlpull.v1.XmlPullParserException("Xml doesn't start with subtypes");
                }
                int depth3 = parser.getDepth();
                java.lang.String currentImiId = null;
                java.util.ArrayList<android.view.inputmethod.InputMethodSubtype> tempSubtypesArray = null;
                while (true) {
                    int type4 = parser.next();
                    if (type4 == 3) {
                        if (parser.getDepth() <= depth3) {
                            allSubtypes = allSubtypes4;
                            fis3 = fis;
                            break;
                        }
                    }
                    if (type4 == i) {
                        allSubtypes = allSubtypes4;
                        fis3 = fis;
                        break;
                    }
                    if (type4 != i2) {
                        str = str4;
                        allSubtypes2 = allSubtypes4;
                        fis2 = fis;
                        type = type4;
                        firstNodeName = firstNodeName3;
                        depth = depth3;
                    } else {
                        java.lang.String nodeName = parser.getName();
                        if (NODE_IMI.equals(nodeName)) {
                            currentImiId = parser.getAttributeValue((java.lang.String) null, ATTR_ID);
                            if (android.text.TextUtils.isEmpty(currentImiId)) {
                                android.util.Slog.w(str5, "Invalid imi id found in subtypes.xml");
                            } else {
                                tempSubtypesArray = new java.util.ArrayList<>();
                                allSubtypes4.put(currentImiId, tempSubtypesArray);
                                str2 = str4;
                                allSubtypes3 = allSubtypes4;
                                fis2 = fis;
                                type2 = type4;
                                firstNodeName2 = firstNodeName3;
                                depth2 = depth3;
                            }
                        } else if (NODE_SUBTYPE.equals(nodeName)) {
                            if (android.text.TextUtils.isEmpty(currentImiId)) {
                                str = str4;
                                str3 = str5;
                                allSubtypes2 = allSubtypes4;
                                fis2 = fis;
                                type = type4;
                                firstNodeName = firstNodeName3;
                                depth = depth3;
                            } else if (tempSubtypesArray == null) {
                                str = str4;
                                str3 = str5;
                                allSubtypes2 = allSubtypes4;
                                fis2 = fis;
                                type = type4;
                                firstNodeName = firstNodeName3;
                                depth = depth3;
                            } else {
                                try {
                                    int icon = parser.getAttributeInt((java.lang.String) null, ATTR_ICON);
                                    int label = parser.getAttributeInt((java.lang.String) null, ATTR_LABEL);
                                    java.lang.String untranslatableName = parser.getAttributeValue((java.lang.String) null, ATTR_NAME_OVERRIDE);
                                    java.lang.String pkLanguageTag = parser.getAttributeValue((java.lang.String) null, ATTR_NAME_PK_LANGUAGE_TAG);
                                    type2 = type4;
                                    java.lang.String pkLayoutType = parser.getAttributeValue((java.lang.String) null, ATTR_NAME_PK_LAYOUT_TYPE);
                                    firstNodeName2 = firstNodeName3;
                                    java.lang.String imeSubtypeLocale = parser.getAttributeValue((java.lang.String) null, ATTR_IME_SUBTYPE_LOCALE);
                                    depth2 = depth3;
                                    java.lang.String languageTag = parser.getAttributeValue((java.lang.String) null, ATTR_IME_SUBTYPE_LANGUAGE_TAG);
                                    java.lang.String imeSubtypeMode = parser.getAttributeValue((java.lang.String) null, ATTR_IME_SUBTYPE_MODE);
                                    allSubtypes3 = allSubtypes4;
                                    try {
                                        java.lang.String imeSubtypeExtraValue = parser.getAttributeValue((java.lang.String) null, ATTR_IME_SUBTYPE_EXTRA_VALUE);
                                        fis2 = fis;
                                        try {
                                            boolean isAuxiliary = str4.equals(java.lang.String.valueOf(parser.getAttributeValue((java.lang.String) null, ATTR_IS_AUXILIARY)));
                                            java.lang.String str6 = str5;
                                            try {
                                                boolean isAsciiCapable = str4.equals(java.lang.String.valueOf(parser.getAttributeValue((java.lang.String) null, ATTR_IS_ASCII_CAPABLE)));
                                                android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder subtypeNameResId = new android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder().setSubtypeNameResId(label);
                                                if (pkLanguageTag == null) {
                                                    str2 = str4;
                                                    uLocale = null;
                                                } else {
                                                    str2 = str4;
                                                    uLocale = new android.icu.util.ULocale(pkLanguageTag);
                                                }
                                                android.view.inputmethod.InputMethodSubtype.InputMethodSubtypeBuilder builder = subtypeNameResId.setPhysicalKeyboardHint(uLocale, pkLayoutType == null ? "" : pkLayoutType).setSubtypeIconResId(icon).setSubtypeLocale(imeSubtypeLocale).setLanguageTag(languageTag).setSubtypeMode(imeSubtypeMode).setSubtypeExtraValue(imeSubtypeExtraValue).setIsAuxiliary(isAuxiliary).setIsAsciiCapable(isAsciiCapable);
                                                int subtypeId = parser.getAttributeInt((java.lang.String) null, ATTR_IME_SUBTYPE_ID, 0);
                                                if (subtypeId != 0) {
                                                    builder.setSubtypeId(subtypeId);
                                                }
                                                if (untranslatableName != null) {
                                                    builder.setSubtypeNameOverride(untranslatableName);
                                                }
                                                tempSubtypesArray.add(builder.build());
                                                str5 = str6;
                                            } catch (java.lang.Throwable th3) {
                                                th = th3;
                                            }
                                        } catch (java.lang.Throwable th4) {
                                            th = th4;
                                        }
                                    } catch (java.lang.Throwable th5) {
                                        fis2 = fis;
                                        th = th5;
                                    }
                                } catch (java.lang.Throwable th6) {
                                    th = th6;
                                    fis2 = fis;
                                    th = th;
                                }
                            }
                            try {
                                str5 = str3;
                                android.util.Slog.w(str5, "IME uninstalled or not valid.: " + currentImiId);
                            } catch (java.lang.Throwable th7) {
                                th = th7;
                            }
                        } else {
                            str2 = str4;
                            allSubtypes3 = allSubtypes4;
                            fis2 = fis;
                            type2 = type4;
                            firstNodeName2 = firstNodeName3;
                            depth2 = depth3;
                        }
                        firstNodeName3 = firstNodeName2;
                        depth3 = depth2;
                        allSubtypes4 = allSubtypes3;
                        fis = fis2;
                        str4 = str2;
                        i = 1;
                        i2 = 2;
                    }
                    firstNodeName3 = firstNodeName;
                    depth3 = depth;
                    allSubtypes4 = allSubtypes2;
                    fis = fis2;
                    str4 = str;
                    i = 1;
                    i2 = 2;
                }
                if (fis3 != null) {
                    fis3.close();
                }
                return com.android.server.inputmethod.AdditionalSubtypeMap.of(allSubtypes);
            } catch (java.lang.Throwable th8) {
                th = th8;
            }
        } catch (java.lang.Throwable th9) {
            th = th9;
        }
        fis2 = fis;
        th = th;
        if (fis2 == null) {
            throw th;
        }
        try {
            fis2.close();
            throw th;
        } catch (java.lang.Throwable th10) {
            th.addSuppressed(th10);
            throw th;
        }
    }
}
