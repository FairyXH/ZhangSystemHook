package com.android.server.devicepolicy;

/* JADX INFO: loaded from: classes.dex */
final class BundlePolicySerializer extends com.android.server.devicepolicy.PolicySerializer<android.os.Bundle> {
    private static final java.lang.String ATTR_KEY = "key";
    private static final java.lang.String ATTR_MULTIPLE = "m";
    private static final java.lang.String ATTR_TYPE_BOOLEAN = "b";
    private static final java.lang.String ATTR_TYPE_BUNDLE = "B";
    private static final java.lang.String ATTR_TYPE_BUNDLE_ARRAY = "BA";
    private static final java.lang.String ATTR_TYPE_INTEGER = "i";
    private static final java.lang.String ATTR_TYPE_STRING = "s";
    private static final java.lang.String ATTR_TYPE_STRING_ARRAY = "sa";
    private static final java.lang.String ATTR_VALUE_TYPE = "type";
    private static final java.lang.String TAG = "BundlePolicySerializer";
    private static final java.lang.String TAG_ENTRY = "entry";
    private static final java.lang.String TAG_VALUE = "value";

    BundlePolicySerializer() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public void saveToXml(com.android.modules.utils.TypedXmlSerializer serializer, android.os.Bundle value) throws java.io.IOException {
        java.util.Objects.requireNonNull(value);
        writeBundle(value, serializer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.server.devicepolicy.PolicySerializer
    public android.app.admin.BundlePolicyValue readFromXml(com.android.modules.utils.TypedXmlPullParser parser) {
        android.os.Bundle bundle = new android.os.Bundle();
        java.util.ArrayList<java.lang.String> values = new java.util.ArrayList<>();
        try {
            int outerDepth = parser.getDepth();
            while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
                readBundle(bundle, values, parser);
            }
            return new android.app.admin.BundlePolicyValue(bundle);
        } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.e(TAG, "Error parsing Bundle policy.", e);
            return null;
        }
    }

    private static void readBundle(android.os.Bundle restrictions, java.util.ArrayList<java.lang.String> values, com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (parser.getEventType() == 2 && parser.getName().equals(TAG_ENTRY)) {
            java.lang.String key = parser.getAttributeValue((java.lang.String) null, ATTR_KEY);
            java.lang.String valType = parser.getAttributeValue((java.lang.String) null, "type");
            int count = parser.getAttributeInt((java.lang.String) null, ATTR_MULTIPLE, -1);
            if (count != -1) {
                values.clear();
                while (count > 0) {
                    int type = parser.next();
                    if (type == 1) {
                        break;
                    }
                    if (type == 2 && parser.getName().equals(TAG_VALUE)) {
                        values.add(parser.nextText());
                        count--;
                    }
                }
                java.lang.String[] valueStrings = new java.lang.String[values.size()];
                values.toArray(valueStrings);
                restrictions.putStringArray(key, valueStrings);
                return;
            }
            if (ATTR_TYPE_BUNDLE.equals(valType)) {
                restrictions.putBundle(key, readBundleEntry(parser, values));
                return;
            }
            if (ATTR_TYPE_BUNDLE_ARRAY.equals(valType)) {
                int outerDepth = parser.getDepth();
                java.util.ArrayList<android.os.Bundle> bundleList = new java.util.ArrayList<>();
                while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
                    android.os.Bundle childBundle = readBundleEntry(parser, values);
                    bundleList.add(childBundle);
                }
                restrictions.putParcelableArray(key, (android.os.Parcelable[]) bundleList.toArray(new android.os.Bundle[bundleList.size()]));
                return;
            }
            java.lang.String value = parser.nextText();
            if (ATTR_TYPE_BOOLEAN.equals(valType)) {
                restrictions.putBoolean(key, java.lang.Boolean.parseBoolean(value));
            } else if (ATTR_TYPE_INTEGER.equals(valType)) {
                restrictions.putInt(key, java.lang.Integer.parseInt(value));
            } else {
                restrictions.putString(key, value);
            }
        }
    }

    private static android.os.Bundle readBundleEntry(com.android.modules.utils.TypedXmlPullParser parser, java.util.ArrayList<java.lang.String> values) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        android.os.Bundle childBundle = new android.os.Bundle();
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            readBundle(childBundle, values, parser);
        }
        return childBundle;
    }

    private static void writeBundle(android.os.Bundle restrictions, com.android.modules.utils.TypedXmlSerializer serializer) throws java.io.IOException {
        for (java.lang.String key : restrictions.keySet()) {
            java.lang.Object value = restrictions.get(key);
            serializer.startTag((java.lang.String) null, TAG_ENTRY);
            serializer.attribute((java.lang.String) null, ATTR_KEY, key);
            if (value instanceof java.lang.Boolean) {
                serializer.attribute((java.lang.String) null, "type", ATTR_TYPE_BOOLEAN);
                serializer.text(value.toString());
            } else if (value instanceof java.lang.Integer) {
                serializer.attribute((java.lang.String) null, "type", ATTR_TYPE_INTEGER);
                serializer.text(value.toString());
            } else if (value == null || (value instanceof java.lang.String)) {
                serializer.attribute((java.lang.String) null, "type", ATTR_TYPE_STRING);
                serializer.text(value != null ? (java.lang.String) value : "");
            } else if (value instanceof android.os.Bundle) {
                serializer.attribute((java.lang.String) null, "type", ATTR_TYPE_BUNDLE);
                writeBundle((android.os.Bundle) value, serializer);
            } else {
                int i = 0;
                if (value instanceof android.os.Parcelable[]) {
                    serializer.attribute((java.lang.String) null, "type", ATTR_TYPE_BUNDLE_ARRAY);
                    android.os.Parcelable[] array = (android.os.Parcelable[]) value;
                    int length = array.length;
                    while (i < length) {
                        android.os.Parcelable parcelable = array[i];
                        if (!(parcelable instanceof android.os.Bundle)) {
                            throw new java.lang.IllegalArgumentException("bundle-array can only hold Bundles");
                        }
                        serializer.startTag((java.lang.String) null, TAG_ENTRY);
                        serializer.attribute((java.lang.String) null, "type", ATTR_TYPE_BUNDLE);
                        writeBundle((android.os.Bundle) parcelable, serializer);
                        serializer.endTag((java.lang.String) null, TAG_ENTRY);
                        i++;
                    }
                } else {
                    serializer.attribute((java.lang.String) null, "type", ATTR_TYPE_STRING_ARRAY);
                    java.lang.String[] values = (java.lang.String[]) value;
                    serializer.attributeInt((java.lang.String) null, ATTR_MULTIPLE, values.length);
                    int length2 = values.length;
                    while (i < length2) {
                        java.lang.String choice = values[i];
                        serializer.startTag((java.lang.String) null, TAG_VALUE);
                        serializer.text(choice != null ? choice : "");
                        serializer.endTag((java.lang.String) null, TAG_VALUE);
                        i++;
                    }
                }
            }
            serializer.endTag((java.lang.String) null, TAG_ENTRY);
        }
    }
}
