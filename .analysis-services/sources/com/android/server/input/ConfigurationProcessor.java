package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
class ConfigurationProcessor {
    private static final java.lang.String TAG = "ConfigurationProcessor";

    ConfigurationProcessor() {
    }

    static java.util.List<java.lang.String> processExcludedDeviceNames(java.io.InputStream xml) throws java.lang.Exception {
        java.util.List<java.lang.String> names = new java.util.ArrayList<>();
        com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(xml);
        com.android.internal.util.XmlUtils.beginDocument(parser, "devices");
        while (true) {
            com.android.internal.util.XmlUtils.nextElement(parser);
            if ("device".equals(parser.getName())) {
                java.lang.String name = parser.getAttributeValue((java.lang.String) null, "name");
                if (name != null) {
                    names.add(name);
                }
            } else {
                return names;
            }
        }
    }

    static java.util.Map<java.lang.String, java.lang.Integer> processInputPortAssociations(java.io.InputStream xml) throws java.lang.Exception {
        java.util.Map<java.lang.String, java.lang.Integer> associations = new java.util.HashMap<>();
        com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(xml);
        com.android.internal.util.XmlUtils.beginDocument(parser, "ports");
        while (true) {
            com.android.internal.util.XmlUtils.nextElement(parser);
            java.lang.String entryName = parser.getName();
            if ("port".equals(entryName)) {
                java.lang.String inputPort = parser.getAttributeValue((java.lang.String) null, com.android.server.am.IOplusSceneManager.APP_SCENE_DEFAULT_INPUT);
                java.lang.String displayPortStr = parser.getAttributeValue((java.lang.String) null, "display");
                if (android.text.TextUtils.isEmpty(inputPort) || android.text.TextUtils.isEmpty(displayPortStr)) {
                    android.util.Slog.wtf(TAG, "Ignoring incomplete entry");
                } else {
                    try {
                        int displayPort = java.lang.Integer.parseUnsignedInt(displayPortStr);
                        associations.put(inputPort, java.lang.Integer.valueOf(displayPort));
                    } catch (java.lang.NumberFormatException e) {
                        android.util.Slog.wtf(TAG, "Display port should be an integer");
                    }
                }
            } else {
                return associations;
            }
        }
    }
}
