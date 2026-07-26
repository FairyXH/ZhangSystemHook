package com.android.internal.util.jobs;

/* JADX INFO: loaded from: classes.dex */
public class XmlUtils {
    private static final java.lang.String STRING_ARRAY_SEPARATOR = ":";

    public interface ReadMapCallback {
        java.lang.Object readThisUnknownObjectXml(com.android.modules.utils.TypedXmlPullParser typedXmlPullParser, java.lang.String str) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException;
    }

    public interface WriteMapCallback {
        void writeUnknownObject(java.lang.Object obj, java.lang.String str, com.android.modules.utils.TypedXmlSerializer typedXmlSerializer) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException;
    }

    private static class ForcedTypedXmlSerializer extends com.android.internal.util.XmlSerializerWrapper implements com.android.modules.utils.TypedXmlSerializer {
        public ForcedTypedXmlSerializer(org.xmlpull.v1.XmlSerializer wrapped) {
            super(wrapped);
        }

        public org.xmlpull.v1.XmlSerializer attributeInterned(java.lang.String namespace, java.lang.String name, java.lang.String value) throws java.io.IOException {
            return attribute(namespace, name, value);
        }

        public org.xmlpull.v1.XmlSerializer attributeBytesHex(java.lang.String namespace, java.lang.String name, byte[] value) throws java.io.IOException {
            return attribute(namespace, name, com.android.internal.util.HexDump.toHexString(value));
        }

        public org.xmlpull.v1.XmlSerializer attributeBytesBase64(java.lang.String namespace, java.lang.String name, byte[] value) throws java.io.IOException {
            return attribute(namespace, name, android.util.Base64.encodeToString(value, 2));
        }

        public org.xmlpull.v1.XmlSerializer attributeInt(java.lang.String namespace, java.lang.String name, int value) throws java.io.IOException {
            return attribute(namespace, name, java.lang.Integer.toString(value));
        }

        public org.xmlpull.v1.XmlSerializer attributeIntHex(java.lang.String namespace, java.lang.String name, int value) throws java.io.IOException {
            return attribute(namespace, name, java.lang.Integer.toString(value, 16));
        }

        public org.xmlpull.v1.XmlSerializer attributeLong(java.lang.String namespace, java.lang.String name, long value) throws java.io.IOException {
            return attribute(namespace, name, java.lang.Long.toString(value));
        }

        public org.xmlpull.v1.XmlSerializer attributeLongHex(java.lang.String namespace, java.lang.String name, long value) throws java.io.IOException {
            return attribute(namespace, name, java.lang.Long.toString(value, 16));
        }

        public org.xmlpull.v1.XmlSerializer attributeFloat(java.lang.String namespace, java.lang.String name, float value) throws java.io.IOException {
            return attribute(namespace, name, java.lang.Float.toString(value));
        }

        public org.xmlpull.v1.XmlSerializer attributeDouble(java.lang.String namespace, java.lang.String name, double value) throws java.io.IOException {
            return attribute(namespace, name, java.lang.Double.toString(value));
        }

        public org.xmlpull.v1.XmlSerializer attributeBoolean(java.lang.String namespace, java.lang.String name, boolean value) throws java.io.IOException {
            return attribute(namespace, name, java.lang.Boolean.toString(value));
        }
    }

    public static com.android.modules.utils.TypedXmlSerializer makeTyped(org.xmlpull.v1.XmlSerializer xml) {
        if (xml instanceof com.android.modules.utils.TypedXmlSerializer) {
            return (com.android.modules.utils.TypedXmlSerializer) xml;
        }
        return new com.android.internal.util.jobs.XmlUtils.ForcedTypedXmlSerializer(xml);
    }

    private static class ForcedTypedXmlPullParser extends com.android.internal.util.XmlPullParserWrapper implements com.android.modules.utils.TypedXmlPullParser {
        public ForcedTypedXmlPullParser(org.xmlpull.v1.XmlPullParser wrapped) {
            super(wrapped);
        }

        public byte[] getAttributeBytesHex(int index) throws org.xmlpull.v1.XmlPullParserException {
            try {
                return com.android.internal.util.HexDump.hexStringToByteArray(getAttributeValue(index));
            } catch (java.lang.Exception e) {
                throw new org.xmlpull.v1.XmlPullParserException("Invalid attribute " + getAttributeName(index) + ": " + e);
            }
        }

        public byte[] getAttributeBytesBase64(int index) throws org.xmlpull.v1.XmlPullParserException {
            try {
                return android.util.Base64.decode(getAttributeValue(index), 2);
            } catch (java.lang.Exception e) {
                throw new org.xmlpull.v1.XmlPullParserException("Invalid attribute " + getAttributeName(index) + ": " + e);
            }
        }

        public int getAttributeInt(int index) throws org.xmlpull.v1.XmlPullParserException {
            try {
                return java.lang.Integer.parseInt(getAttributeValue(index));
            } catch (java.lang.Exception e) {
                throw new org.xmlpull.v1.XmlPullParserException("Invalid attribute " + getAttributeName(index) + ": " + e);
            }
        }

        public int getAttributeIntHex(int index) throws org.xmlpull.v1.XmlPullParserException {
            try {
                return java.lang.Integer.parseInt(getAttributeValue(index), 16);
            } catch (java.lang.Exception e) {
                throw new org.xmlpull.v1.XmlPullParserException("Invalid attribute " + getAttributeName(index) + ": " + e);
            }
        }

        public long getAttributeLong(int index) throws org.xmlpull.v1.XmlPullParserException {
            try {
                return java.lang.Long.parseLong(getAttributeValue(index));
            } catch (java.lang.Exception e) {
                throw new org.xmlpull.v1.XmlPullParserException("Invalid attribute " + getAttributeName(index) + ": " + e);
            }
        }

        public long getAttributeLongHex(int index) throws org.xmlpull.v1.XmlPullParserException {
            try {
                return java.lang.Long.parseLong(getAttributeValue(index), 16);
            } catch (java.lang.Exception e) {
                throw new org.xmlpull.v1.XmlPullParserException("Invalid attribute " + getAttributeName(index) + ": " + e);
            }
        }

        public float getAttributeFloat(int index) throws org.xmlpull.v1.XmlPullParserException {
            try {
                return java.lang.Float.parseFloat(getAttributeValue(index));
            } catch (java.lang.Exception e) {
                throw new org.xmlpull.v1.XmlPullParserException("Invalid attribute " + getAttributeName(index) + ": " + e);
            }
        }

        public double getAttributeDouble(int index) throws org.xmlpull.v1.XmlPullParserException {
            try {
                return java.lang.Double.parseDouble(getAttributeValue(index));
            } catch (java.lang.Exception e) {
                throw new org.xmlpull.v1.XmlPullParserException("Invalid attribute " + getAttributeName(index) + ": " + e);
            }
        }

        public boolean getAttributeBoolean(int index) throws org.xmlpull.v1.XmlPullParserException {
            java.lang.String value = getAttributeValue(index);
            if ("true".equalsIgnoreCase(value)) {
                return true;
            }
            if ("false".equalsIgnoreCase(value)) {
                return false;
            }
            throw new org.xmlpull.v1.XmlPullParserException("Invalid attribute " + getAttributeName(index) + ": " + value);
        }
    }

    public static com.android.modules.utils.TypedXmlPullParser makeTyped(org.xmlpull.v1.XmlPullParser xml) {
        if (xml instanceof com.android.modules.utils.TypedXmlPullParser) {
            return (com.android.modules.utils.TypedXmlPullParser) xml;
        }
        return new com.android.internal.util.jobs.XmlUtils.ForcedTypedXmlPullParser(xml);
    }

    public static void skipCurrentTag(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type == 3 && parser.getDepth() <= outerDepth) {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public static final int convertValueToList(java.lang.CharSequence value, java.lang.String[] options, int defaultValue) {
        if (!android.text.TextUtils.isEmpty(value)) {
            for (int i = 0; i < options.length; i++) {
                if (value.equals(options[i])) {
                    return i;
                }
            }
        }
        return defaultValue;
    }

    public static final boolean convertValueToBoolean(java.lang.CharSequence value, boolean defaultValue) {
        if (android.text.TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        if (!value.equals("1") && !value.equals("true") && !value.equals("TRUE")) {
            return false;
        }
        return true;
    }

    public static final int convertValueToInt(java.lang.CharSequence charSeq, int defaultValue) {
        if (android.text.TextUtils.isEmpty(charSeq)) {
            return defaultValue;
        }
        java.lang.String nm = charSeq.toString();
        int sign = 1;
        int index = 0;
        int len = nm.length();
        int base = 10;
        if ('-' == nm.charAt(0)) {
            sign = -1;
            index = 0 + 1;
        }
        if ('0' == nm.charAt(index)) {
            if (index == len - 1) {
                return 0;
            }
            char c = nm.charAt(index + 1);
            if ('x' == c || 'X' == c) {
                index += 2;
                base = 16;
            } else {
                index++;
                base = 8;
            }
        } else if ('#' == nm.charAt(index)) {
            index++;
            base = 16;
        }
        return java.lang.Integer.parseInt(nm.substring(index), base) * sign;
    }

    public static int convertValueToUnsignedInt(java.lang.String value, int defaultValue) {
        if (android.text.TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        return parseUnsignedIntAttribute(value);
    }

    public static int parseUnsignedIntAttribute(java.lang.CharSequence charSeq) {
        java.lang.String value = charSeq.toString();
        int index = 0;
        int len = value.length();
        int base = 10;
        if ('0' == value.charAt(0)) {
            if (0 == len - 1) {
                return 0;
            }
            char c = value.charAt(0 + 1);
            if ('x' == c || 'X' == c) {
                index = 0 + 2;
                base = 16;
            } else {
                index = 0 + 1;
                base = 8;
            }
        } else if ('#' == value.charAt(0)) {
            index = 0 + 1;
            base = 16;
        }
        return (int) java.lang.Long.parseLong(value.substring(index), base);
    }

    public static final void writeMapXml(java.util.Map val, java.io.OutputStream out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.newFastSerializer();
        serializer.setOutput(out, java.nio.charset.StandardCharsets.UTF_8.name());
        serializer.startDocument((java.lang.String) null, true);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        writeMapXml(val, (java.lang.String) null, serializer);
        serializer.endDocument();
    }

    public static final void writeListXml(java.util.List val, java.io.OutputStream out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.newFastSerializer();
        serializer.setOutput(out, java.nio.charset.StandardCharsets.UTF_8.name());
        serializer.startDocument((java.lang.String) null, true);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        writeListXml(val, null, serializer);
        serializer.endDocument();
    }

    public static final void writeMapXml(java.util.Map val, java.lang.String name, com.android.modules.utils.TypedXmlSerializer out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        writeMapXml(val, name, out, null);
    }

    public static final void writeMapXml(java.util.Map val, java.lang.String name, com.android.modules.utils.TypedXmlSerializer out, com.android.internal.util.jobs.XmlUtils.WriteMapCallback callback) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (val == null) {
            out.startTag((java.lang.String) null, "null");
            out.endTag((java.lang.String) null, "null");
            return;
        }
        out.startTag((java.lang.String) null, "map");
        if (name != null) {
            out.attribute((java.lang.String) null, "name", name);
        }
        writeMapXml(val, out, callback);
        out.endTag((java.lang.String) null, "map");
    }

    public static final void writeMapXml(java.util.Map val, com.android.modules.utils.TypedXmlSerializer out, com.android.internal.util.jobs.XmlUtils.WriteMapCallback callback) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (val == null) {
            return;
        }
        java.util.Set<java.util.Map.Entry> s = val.entrySet();
        for (java.util.Map.Entry e : s) {
            writeValueXml(e.getValue(), (java.lang.String) e.getKey(), out, callback);
        }
    }

    public static final void writeListXml(java.util.List val, java.lang.String name, com.android.modules.utils.TypedXmlSerializer out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (val == null) {
            out.startTag((java.lang.String) null, "null");
            out.endTag((java.lang.String) null, "null");
            return;
        }
        out.startTag((java.lang.String) null, "list");
        if (name != null) {
            out.attribute((java.lang.String) null, "name", name);
        }
        int N = val.size();
        for (int i = 0; i < N; i++) {
            writeValueXml(val.get(i), (java.lang.String) null, out);
        }
        out.endTag((java.lang.String) null, "list");
    }

    public static final void writeSetXml(java.util.Set val, java.lang.String name, com.android.modules.utils.TypedXmlSerializer out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (val == null) {
            out.startTag((java.lang.String) null, "null");
            out.endTag((java.lang.String) null, "null");
            return;
        }
        out.startTag((java.lang.String) null, "set");
        if (name != null) {
            out.attribute((java.lang.String) null, "name", name);
        }
        for (java.lang.Object v : val) {
            writeValueXml(v, (java.lang.String) null, out);
        }
        out.endTag((java.lang.String) null, "set");
    }

    public static final void writeByteArrayXml(byte[] val, java.lang.String name, com.android.modules.utils.TypedXmlSerializer out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (val == null) {
            out.startTag((java.lang.String) null, "null");
            out.endTag((java.lang.String) null, "null");
            return;
        }
        out.startTag((java.lang.String) null, "byte-array");
        if (name != null) {
            out.attribute((java.lang.String) null, "name", name);
        }
        int N = val.length;
        out.attributeInt((java.lang.String) null, "num", N);
        out.text(libcore.util.HexEncoding.encodeToString(val).toLowerCase());
        out.endTag((java.lang.String) null, "byte-array");
    }

    public static final void writeIntArrayXml(int[] val, java.lang.String name, com.android.modules.utils.TypedXmlSerializer out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (val == null) {
            out.startTag((java.lang.String) null, "null");
            out.endTag((java.lang.String) null, "null");
            return;
        }
        out.startTag((java.lang.String) null, "int-array");
        if (name != null) {
            out.attribute((java.lang.String) null, "name", name);
        }
        int N = val.length;
        out.attributeInt((java.lang.String) null, "num", N);
        for (int i : val) {
            out.startTag((java.lang.String) null, com.android.server.pm.Settings.TAG_ITEM);
            out.attributeInt((java.lang.String) null, "value", i);
            out.endTag((java.lang.String) null, com.android.server.pm.Settings.TAG_ITEM);
        }
        out.endTag((java.lang.String) null, "int-array");
    }

    public static final void writeLongArrayXml(long[] val, java.lang.String name, com.android.modules.utils.TypedXmlSerializer out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (val == null) {
            out.startTag((java.lang.String) null, "null");
            out.endTag((java.lang.String) null, "null");
            return;
        }
        out.startTag((java.lang.String) null, "long-array");
        if (name != null) {
            out.attribute((java.lang.String) null, "name", name);
        }
        int N = val.length;
        out.attributeInt((java.lang.String) null, "num", N);
        for (long j : val) {
            out.startTag((java.lang.String) null, com.android.server.pm.Settings.TAG_ITEM);
            out.attributeLong((java.lang.String) null, "value", j);
            out.endTag((java.lang.String) null, com.android.server.pm.Settings.TAG_ITEM);
        }
        out.endTag((java.lang.String) null, "long-array");
    }

    public static final void writeDoubleArrayXml(double[] val, java.lang.String name, com.android.modules.utils.TypedXmlSerializer out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (val == null) {
            out.startTag((java.lang.String) null, "null");
            out.endTag((java.lang.String) null, "null");
            return;
        }
        out.startTag((java.lang.String) null, "double-array");
        if (name != null) {
            out.attribute((java.lang.String) null, "name", name);
        }
        int N = val.length;
        out.attributeInt((java.lang.String) null, "num", N);
        for (double d : val) {
            out.startTag((java.lang.String) null, com.android.server.pm.Settings.TAG_ITEM);
            out.attributeDouble((java.lang.String) null, "value", d);
            out.endTag((java.lang.String) null, com.android.server.pm.Settings.TAG_ITEM);
        }
        out.endTag((java.lang.String) null, "double-array");
    }

    public static final void writeStringArrayXml(java.lang.String[] val, java.lang.String name, com.android.modules.utils.TypedXmlSerializer out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (val == null) {
            out.startTag((java.lang.String) null, "null");
            out.endTag((java.lang.String) null, "null");
            return;
        }
        out.startTag((java.lang.String) null, "string-array");
        if (name != null) {
            out.attribute((java.lang.String) null, "name", name);
        }
        int N = val.length;
        out.attributeInt((java.lang.String) null, "num", N);
        for (java.lang.String str : val) {
            out.startTag((java.lang.String) null, com.android.server.pm.Settings.TAG_ITEM);
            out.attribute((java.lang.String) null, "value", str);
            out.endTag((java.lang.String) null, com.android.server.pm.Settings.TAG_ITEM);
        }
        out.endTag((java.lang.String) null, "string-array");
    }

    public static final void writeBooleanArrayXml(boolean[] val, java.lang.String name, com.android.modules.utils.TypedXmlSerializer out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (val == null) {
            out.startTag((java.lang.String) null, "null");
            out.endTag((java.lang.String) null, "null");
            return;
        }
        out.startTag((java.lang.String) null, "boolean-array");
        if (name != null) {
            out.attribute((java.lang.String) null, "name", name);
        }
        int N = val.length;
        out.attributeInt((java.lang.String) null, "num", N);
        for (boolean z : val) {
            out.startTag((java.lang.String) null, com.android.server.pm.Settings.TAG_ITEM);
            out.attributeBoolean((java.lang.String) null, "value", z);
            out.endTag((java.lang.String) null, com.android.server.pm.Settings.TAG_ITEM);
        }
        out.endTag((java.lang.String) null, "boolean-array");
    }

    @java.lang.Deprecated
    public static final void writeValueXml(java.lang.Object v, java.lang.String name, org.xmlpull.v1.XmlSerializer out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        writeValueXml(v, name, makeTyped(out));
    }

    public static final void writeValueXml(java.lang.Object v, java.lang.String name, com.android.modules.utils.TypedXmlSerializer out) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        writeValueXml(v, name, out, null);
    }

    private static final void writeValueXml(java.lang.Object v, java.lang.String name, com.android.modules.utils.TypedXmlSerializer out, com.android.internal.util.jobs.XmlUtils.WriteMapCallback callback) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (v == null) {
            out.startTag((java.lang.String) null, "null");
            if (name != null) {
                out.attribute((java.lang.String) null, "name", name);
            }
            out.endTag((java.lang.String) null, "null");
            return;
        }
        if (v instanceof java.lang.String) {
            out.startTag((java.lang.String) null, "string");
            if (name != null) {
                out.attribute((java.lang.String) null, "name", name);
            }
            out.text(v.toString());
            out.endTag((java.lang.String) null, "string");
            return;
        }
        if (v instanceof java.lang.Integer) {
            out.startTag((java.lang.String) null, "int");
            if (name != null) {
                out.attribute((java.lang.String) null, "name", name);
            }
            out.attributeInt((java.lang.String) null, "value", ((java.lang.Integer) v).intValue());
            out.endTag((java.lang.String) null, "int");
            return;
        }
        if (v instanceof java.lang.Long) {
            out.startTag((java.lang.String) null, "long");
            if (name != null) {
                out.attribute((java.lang.String) null, "name", name);
            }
            out.attributeLong((java.lang.String) null, "value", ((java.lang.Long) v).longValue());
            out.endTag((java.lang.String) null, "long");
            return;
        }
        if (v instanceof java.lang.Float) {
            out.startTag((java.lang.String) null, "float");
            if (name != null) {
                out.attribute((java.lang.String) null, "name", name);
            }
            out.attributeFloat((java.lang.String) null, "value", ((java.lang.Float) v).floatValue());
            out.endTag((java.lang.String) null, "float");
            return;
        }
        if (v instanceof java.lang.Double) {
            out.startTag((java.lang.String) null, "double");
            if (name != null) {
                out.attribute((java.lang.String) null, "name", name);
            }
            out.attributeDouble((java.lang.String) null, "value", ((java.lang.Double) v).doubleValue());
            out.endTag((java.lang.String) null, "double");
            return;
        }
        if (v instanceof java.lang.Boolean) {
            out.startTag((java.lang.String) null, "boolean");
            if (name != null) {
                out.attribute((java.lang.String) null, "name", name);
            }
            out.attributeBoolean((java.lang.String) null, "value", ((java.lang.Boolean) v).booleanValue());
            out.endTag((java.lang.String) null, "boolean");
            return;
        }
        if (v instanceof byte[]) {
            writeByteArrayXml((byte[]) v, name, out);
            return;
        }
        if (v instanceof int[]) {
            writeIntArrayXml((int[]) v, name, out);
            return;
        }
        if (v instanceof long[]) {
            writeLongArrayXml((long[]) v, name, out);
            return;
        }
        if (v instanceof double[]) {
            writeDoubleArrayXml((double[]) v, name, out);
            return;
        }
        if (v instanceof java.lang.String[]) {
            writeStringArrayXml((java.lang.String[]) v, name, out);
            return;
        }
        if (v instanceof boolean[]) {
            writeBooleanArrayXml((boolean[]) v, name, out);
            return;
        }
        if (v instanceof java.util.Map) {
            writeMapXml((java.util.Map) v, name, out);
            return;
        }
        if (v instanceof java.util.List) {
            writeListXml((java.util.List) v, name, out);
            return;
        }
        if (v instanceof java.util.Set) {
            writeSetXml((java.util.Set) v, name, out);
            return;
        }
        if (v instanceof java.lang.CharSequence) {
            out.startTag((java.lang.String) null, "string");
            if (name != null) {
                out.attribute((java.lang.String) null, "name", name);
            }
            out.text(v.toString());
            out.endTag((java.lang.String) null, "string");
            return;
        }
        if (callback != null) {
            callback.writeUnknownObject(v, name, out);
            return;
        }
        throw new java.lang.RuntimeException("writeValueXml: unable to write value " + v);
    }

    public static final java.util.HashMap<java.lang.String, ?> readMapXml(java.io.InputStream in) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.newFastPullParser();
        parser.setInput(in, java.nio.charset.StandardCharsets.UTF_8.name());
        return (java.util.HashMap) readValueXml(parser, new java.lang.String[1]);
    }

    public static final java.util.ArrayList readListXml(java.io.InputStream in) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.newFastPullParser();
        parser.setInput(in, java.nio.charset.StandardCharsets.UTF_8.name());
        return (java.util.ArrayList) readValueXml(parser, new java.lang.String[1]);
    }

    public static final java.util.HashSet readSetXml(java.io.InputStream in) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.newFastPullParser();
        parser.setInput(in, java.nio.charset.StandardCharsets.UTF_8.name());
        return (java.util.HashSet) readValueXml(parser, new java.lang.String[1]);
    }

    public static final java.util.HashMap<java.lang.String, ?> readThisMapXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String endTag, java.lang.String[] name) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        return readThisMapXml(parser, endTag, name, null);
    }

    public static final java.util.HashMap<java.lang.String, ?> readThisMapXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String endTag, java.lang.String[] name, com.android.internal.util.jobs.XmlUtils.ReadMapCallback callback) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.util.HashMap<java.lang.String, ?> map = new java.util.HashMap<>();
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                java.lang.Object val = readThisValueXml(parser, name, callback, false);
                map.put(name[0], val);
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return map;
                }
                throw new org.xmlpull.v1.XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new org.xmlpull.v1.XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final android.util.ArrayMap<java.lang.String, ?> readThisArrayMapXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String endTag, java.lang.String[] name, com.android.internal.util.jobs.XmlUtils.ReadMapCallback callback) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        android.util.ArrayMap<java.lang.String, ?> arrayMap = new android.util.ArrayMap<>();
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                java.lang.Object val = readThisValueXml(parser, name, callback, true);
                arrayMap.put(name[0], val);
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return arrayMap;
                }
                throw new org.xmlpull.v1.XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new org.xmlpull.v1.XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final java.util.ArrayList readThisListXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String endTag, java.lang.String[] name) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        return readThisListXml(parser, endTag, name, null, false);
    }

    private static final java.util.ArrayList readThisListXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String endTag, java.lang.String[] name, com.android.internal.util.jobs.XmlUtils.ReadMapCallback callback, boolean arrayMap) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.util.ArrayList list = new java.util.ArrayList();
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                java.lang.Object val = readThisValueXml(parser, name, callback, arrayMap);
                list.add(val);
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return list;
                }
                throw new org.xmlpull.v1.XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new org.xmlpull.v1.XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final java.util.HashSet readThisSetXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String endTag, java.lang.String[] name) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        return readThisSetXml(parser, endTag, name, null, false);
    }

    private static final java.util.HashSet readThisSetXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String endTag, java.lang.String[] name, com.android.internal.util.jobs.XmlUtils.ReadMapCallback callback, boolean arrayMap) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.util.HashSet set = new java.util.HashSet();
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                java.lang.Object val = readThisValueXml(parser, name, callback, arrayMap);
                set.add(val);
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return set;
                }
                throw new org.xmlpull.v1.XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new org.xmlpull.v1.XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final byte[] readThisByteArrayXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String endTag, java.lang.String[] name) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int num = parser.getAttributeInt((java.lang.String) null, "num");
        byte[] array = new byte[0];
        int eventType = parser.getEventType();
        do {
            if (eventType == 4) {
                if (num > 0) {
                    java.lang.String values = parser.getText();
                    if (values == null || values.length() != num * 2) {
                        throw new org.xmlpull.v1.XmlPullParserException("Invalid value found in byte-array: " + values);
                    }
                    array = libcore.util.HexEncoding.decode(values);
                }
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return array;
                }
                throw new org.xmlpull.v1.XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new org.xmlpull.v1.XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final int[] readThisIntArrayXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String endTag, java.lang.String[] name) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int num = parser.getAttributeInt((java.lang.String) null, "num");
        parser.next();
        int[] array = new int[num];
        int i = 0;
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                if (parser.getName().equals(com.android.server.pm.Settings.TAG_ITEM)) {
                    array[i] = parser.getAttributeInt((java.lang.String) null, "value");
                } else {
                    throw new org.xmlpull.v1.XmlPullParserException("Expected item tag at: " + parser.getName());
                }
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return array;
                }
                if (parser.getName().equals(com.android.server.pm.Settings.TAG_ITEM)) {
                    i++;
                } else {
                    throw new org.xmlpull.v1.XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
                }
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new org.xmlpull.v1.XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final long[] readThisLongArrayXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String endTag, java.lang.String[] name) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int num = parser.getAttributeInt((java.lang.String) null, "num");
        parser.next();
        long[] array = new long[num];
        int i = 0;
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                if (parser.getName().equals(com.android.server.pm.Settings.TAG_ITEM)) {
                    array[i] = parser.getAttributeLong((java.lang.String) null, "value");
                } else {
                    throw new org.xmlpull.v1.XmlPullParserException("Expected item tag at: " + parser.getName());
                }
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return array;
                }
                if (parser.getName().equals(com.android.server.pm.Settings.TAG_ITEM)) {
                    i++;
                } else {
                    throw new org.xmlpull.v1.XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
                }
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new org.xmlpull.v1.XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final double[] readThisDoubleArrayXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String endTag, java.lang.String[] name) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int num = parser.getAttributeInt((java.lang.String) null, "num");
        parser.next();
        double[] array = new double[num];
        int i = 0;
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                if (parser.getName().equals(com.android.server.pm.Settings.TAG_ITEM)) {
                    array[i] = parser.getAttributeDouble((java.lang.String) null, "value");
                } else {
                    throw new org.xmlpull.v1.XmlPullParserException("Expected item tag at: " + parser.getName());
                }
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return array;
                }
                if (parser.getName().equals(com.android.server.pm.Settings.TAG_ITEM)) {
                    i++;
                } else {
                    throw new org.xmlpull.v1.XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
                }
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new org.xmlpull.v1.XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final java.lang.String[] readThisStringArrayXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String endTag, java.lang.String[] name) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int num = parser.getAttributeInt((java.lang.String) null, "num");
        parser.next();
        java.lang.String[] array = new java.lang.String[num];
        int i = 0;
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                if (parser.getName().equals(com.android.server.pm.Settings.TAG_ITEM)) {
                    array[i] = parser.getAttributeValue((java.lang.String) null, "value");
                } else {
                    throw new org.xmlpull.v1.XmlPullParserException("Expected item tag at: " + parser.getName());
                }
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return array;
                }
                if (parser.getName().equals(com.android.server.pm.Settings.TAG_ITEM)) {
                    i++;
                } else {
                    throw new org.xmlpull.v1.XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
                }
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new org.xmlpull.v1.XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final boolean[] readThisBooleanArrayXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String endTag, java.lang.String[] name) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int num = parser.getAttributeInt((java.lang.String) null, "num");
        parser.next();
        boolean[] array = new boolean[num];
        int i = 0;
        int eventType = parser.getEventType();
        do {
            if (eventType == 2) {
                if (parser.getName().equals(com.android.server.pm.Settings.TAG_ITEM)) {
                    array[i] = parser.getAttributeBoolean((java.lang.String) null, "value");
                } else {
                    throw new org.xmlpull.v1.XmlPullParserException("Expected item tag at: " + parser.getName());
                }
            } else if (eventType == 3) {
                if (parser.getName().equals(endTag)) {
                    return array;
                }
                if (parser.getName().equals(com.android.server.pm.Settings.TAG_ITEM)) {
                    i++;
                } else {
                    throw new org.xmlpull.v1.XmlPullParserException("Expected " + endTag + " end tag at: " + parser.getName());
                }
            }
            eventType = parser.next();
        } while (eventType != 1);
        throw new org.xmlpull.v1.XmlPullParserException("Document ended before " + endTag + " end tag");
    }

    public static final java.lang.Object readValueXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String[] name) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int eventType = parser.getEventType();
        while (eventType != 2) {
            if (eventType == 3) {
                throw new org.xmlpull.v1.XmlPullParserException("Unexpected end tag at: " + parser.getName());
            }
            if (eventType == 4) {
                throw new org.xmlpull.v1.XmlPullParserException("Unexpected text: " + parser.getText());
            }
            eventType = parser.next();
            if (eventType == 1) {
                throw new org.xmlpull.v1.XmlPullParserException("Unexpected end of document");
            }
        }
        return readThisValueXml(parser, name, null, false);
    }

    private static final java.lang.Object readThisValueXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String[] name, com.android.internal.util.jobs.XmlUtils.ReadMapCallback callback, boolean arrayMap) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.Object res;
        java.lang.Object res2;
        int eventType;
        java.lang.String valueName = parser.getAttributeValue((java.lang.String) null, "name");
        java.lang.String tagName = parser.getName();
        if (tagName.equals("null")) {
            res2 = null;
        } else if (tagName.equals("string")) {
            java.lang.StringBuilder value = new java.lang.StringBuilder();
            while (true) {
                int eventType2 = parser.next();
                if (eventType2 != 1) {
                    if (eventType2 == 3) {
                        if (parser.getName().equals("string")) {
                            name[0] = valueName;
                            return value.toString();
                        }
                        throw new org.xmlpull.v1.XmlPullParserException("Unexpected end tag in <string>: " + parser.getName());
                    }
                    if (eventType2 == 4) {
                        value.append(parser.getText());
                    } else if (eventType2 == 2) {
                        throw new org.xmlpull.v1.XmlPullParserException("Unexpected start tag in <string>: " + parser.getName());
                    }
                } else {
                    throw new org.xmlpull.v1.XmlPullParserException("Unexpected end of document in <string>");
                }
            }
        } else {
            java.lang.Object res3 = readThisPrimitiveValueXml(parser, tagName);
            if (res3 == null) {
                if (tagName.equals("byte-array")) {
                    java.lang.Object res4 = readThisByteArrayXml(parser, "byte-array", name);
                    name[0] = valueName;
                    return res4;
                }
                if (tagName.equals("int-array")) {
                    java.lang.Object res5 = readThisIntArrayXml(parser, "int-array", name);
                    name[0] = valueName;
                    return res5;
                }
                if (tagName.equals("long-array")) {
                    java.lang.Object res6 = readThisLongArrayXml(parser, "long-array", name);
                    name[0] = valueName;
                    return res6;
                }
                if (tagName.equals("double-array")) {
                    java.lang.Object res7 = readThisDoubleArrayXml(parser, "double-array", name);
                    name[0] = valueName;
                    return res7;
                }
                if (tagName.equals("string-array")) {
                    java.lang.Object res8 = readThisStringArrayXml(parser, "string-array", name);
                    name[0] = valueName;
                    return res8;
                }
                if (tagName.equals("boolean-array")) {
                    java.lang.Object res9 = readThisBooleanArrayXml(parser, "boolean-array", name);
                    name[0] = valueName;
                    return res9;
                }
                if (tagName.equals("map")) {
                    parser.next();
                    if (arrayMap) {
                        res = readThisArrayMapXml(parser, "map", name, callback);
                    } else {
                        res = readThisMapXml(parser, "map", name, callback);
                    }
                    name[0] = valueName;
                    return res;
                }
                if (tagName.equals("list")) {
                    parser.next();
                    java.lang.Object res10 = readThisListXml(parser, "list", name, callback, arrayMap);
                    name[0] = valueName;
                    return res10;
                }
                if (tagName.equals("set")) {
                    parser.next();
                    java.lang.Object res11 = readThisSetXml(parser, "set", name, callback, arrayMap);
                    name[0] = valueName;
                    return res11;
                }
                if (callback != null) {
                    java.lang.Object res12 = callback.readThisUnknownObjectXml(parser, tagName);
                    name[0] = valueName;
                    return res12;
                }
                throw new org.xmlpull.v1.XmlPullParserException("Unknown tag: " + tagName);
            }
            res2 = res3;
        }
        do {
            eventType = parser.next();
            if (eventType == 1) {
                throw new org.xmlpull.v1.XmlPullParserException("Unexpected end of document in <" + tagName + ">");
            }
            if (eventType == 3) {
                if (parser.getName().equals(tagName)) {
                    name[0] = valueName;
                    return res2;
                }
                throw new org.xmlpull.v1.XmlPullParserException("Unexpected end tag in <" + tagName + ">: " + parser.getName());
            }
            if (eventType == 4) {
                throw new org.xmlpull.v1.XmlPullParserException("Unexpected text in <" + tagName + ">: " + parser.getName());
            }
        } while (eventType != 2);
        throw new org.xmlpull.v1.XmlPullParserException("Unexpected start tag in <" + tagName + ">: " + parser.getName());
    }

    private static final java.lang.Object readThisPrimitiveValueXml(com.android.modules.utils.TypedXmlPullParser parser, java.lang.String tagName) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (tagName.equals("int")) {
            return java.lang.Integer.valueOf(parser.getAttributeInt((java.lang.String) null, "value"));
        }
        if (tagName.equals("long")) {
            return java.lang.Long.valueOf(parser.getAttributeLong((java.lang.String) null, "value"));
        }
        if (tagName.equals("float")) {
            return java.lang.Float.valueOf(parser.getAttributeFloat((java.lang.String) null, "value"));
        }
        if (tagName.equals("double")) {
            return java.lang.Double.valueOf(parser.getAttributeDouble((java.lang.String) null, "value"));
        }
        if (tagName.equals("boolean")) {
            return java.lang.Boolean.valueOf(parser.getAttributeBoolean((java.lang.String) null, "value"));
        }
        return null;
    }

    public static final void beginDocument(org.xmlpull.v1.XmlPullParser parser, java.lang.String firstElementName) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int type;
        do {
            type = parser.next();
            if (type == 2) {
                break;
            }
        } while (type != 1);
        if (type != 2) {
            throw new org.xmlpull.v1.XmlPullParserException("No start tag found");
        }
        if (!parser.getName().equals(firstElementName)) {
            throw new org.xmlpull.v1.XmlPullParserException("Unexpected start tag: found " + parser.getName() + ", expected " + firstElementName);
        }
    }

    public static final void nextElement(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int type;
        do {
            type = parser.next();
            if (type == 2) {
                return;
            }
        } while (type != 1);
    }

    public static boolean nextElementWithin(org.xmlpull.v1.XmlPullParser parser, int outerDepth) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        while (true) {
            int type = parser.next();
            if (type != 1) {
                if (type == 3 && parser.getDepth() == outerDepth) {
                    return false;
                }
                if (type == 2 && parser.getDepth() == outerDepth + 1) {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    public static int readIntAttribute(org.xmlpull.v1.XmlPullParser in, java.lang.String name, int defaultValue) {
        if (in instanceof com.android.modules.utils.TypedXmlPullParser) {
            return ((com.android.modules.utils.TypedXmlPullParser) in).getAttributeInt((java.lang.String) null, name, defaultValue);
        }
        java.lang.String value = in.getAttributeValue(null, name);
        if (android.text.TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return java.lang.Integer.parseInt(value);
        } catch (java.lang.NumberFormatException e) {
            return defaultValue;
        }
    }

    public static int readIntAttribute(org.xmlpull.v1.XmlPullParser in, java.lang.String name) throws java.io.IOException {
        if (in instanceof com.android.modules.utils.TypedXmlPullParser) {
            try {
                return ((com.android.modules.utils.TypedXmlPullParser) in).getAttributeInt((java.lang.String) null, name);
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                throw new java.net.ProtocolException(e.getMessage());
            }
        }
        java.lang.String value = in.getAttributeValue(null, name);
        try {
            return java.lang.Integer.parseInt(value);
        } catch (java.lang.NumberFormatException e2) {
            throw new java.net.ProtocolException("problem parsing " + name + "=" + value + " as int");
        }
    }

    public static void writeIntAttribute(org.xmlpull.v1.XmlSerializer out, java.lang.String name, int value) throws java.io.IOException {
        if (out instanceof com.android.modules.utils.TypedXmlSerializer) {
            ((com.android.modules.utils.TypedXmlSerializer) out).attributeInt((java.lang.String) null, name, value);
        } else {
            out.attribute(null, name, java.lang.Integer.toString(value));
        }
    }

    public static long readLongAttribute(org.xmlpull.v1.XmlPullParser in, java.lang.String name, long defaultValue) {
        if (in instanceof com.android.modules.utils.TypedXmlPullParser) {
            return ((com.android.modules.utils.TypedXmlPullParser) in).getAttributeLong((java.lang.String) null, name, defaultValue);
        }
        java.lang.String value = in.getAttributeValue(null, name);
        if (android.text.TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        try {
            return java.lang.Long.parseLong(value);
        } catch (java.lang.NumberFormatException e) {
            return defaultValue;
        }
    }

    public static long readLongAttribute(org.xmlpull.v1.XmlPullParser in, java.lang.String name) throws java.io.IOException {
        if (in instanceof com.android.modules.utils.TypedXmlPullParser) {
            try {
                return ((com.android.modules.utils.TypedXmlPullParser) in).getAttributeLong((java.lang.String) null, name);
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                throw new java.net.ProtocolException(e.getMessage());
            }
        }
        java.lang.String value = in.getAttributeValue(null, name);
        try {
            return java.lang.Long.parseLong(value);
        } catch (java.lang.NumberFormatException e2) {
            throw new java.net.ProtocolException("problem parsing " + name + "=" + value + " as long");
        }
    }

    public static void writeLongAttribute(org.xmlpull.v1.XmlSerializer out, java.lang.String name, long value) throws java.io.IOException {
        if (out instanceof com.android.modules.utils.TypedXmlSerializer) {
            ((com.android.modules.utils.TypedXmlSerializer) out).attributeLong((java.lang.String) null, name, value);
        } else {
            out.attribute(null, name, java.lang.Long.toString(value));
        }
    }

    public static float readFloatAttribute(org.xmlpull.v1.XmlPullParser in, java.lang.String name) throws java.io.IOException {
        if (in instanceof com.android.modules.utils.TypedXmlPullParser) {
            try {
                return ((com.android.modules.utils.TypedXmlPullParser) in).getAttributeFloat((java.lang.String) null, name);
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                throw new java.net.ProtocolException(e.getMessage());
            }
        }
        java.lang.String value = in.getAttributeValue(null, name);
        try {
            return java.lang.Float.parseFloat(value);
        } catch (java.lang.NumberFormatException e2) {
            throw new java.net.ProtocolException("problem parsing " + name + "=" + value + " as long");
        }
    }

    public static void writeFloatAttribute(org.xmlpull.v1.XmlSerializer out, java.lang.String name, float value) throws java.io.IOException {
        if (out instanceof com.android.modules.utils.TypedXmlSerializer) {
            ((com.android.modules.utils.TypedXmlSerializer) out).attributeFloat((java.lang.String) null, name, value);
        } else {
            out.attribute(null, name, java.lang.Float.toString(value));
        }
    }

    public static boolean readBooleanAttribute(org.xmlpull.v1.XmlPullParser in, java.lang.String name) {
        return readBooleanAttribute(in, name, false);
    }

    public static boolean readBooleanAttribute(org.xmlpull.v1.XmlPullParser in, java.lang.String name, boolean defaultValue) {
        if (in instanceof com.android.modules.utils.TypedXmlPullParser) {
            return ((com.android.modules.utils.TypedXmlPullParser) in).getAttributeBoolean((java.lang.String) null, name, defaultValue);
        }
        java.lang.String value = in.getAttributeValue(null, name);
        if (android.text.TextUtils.isEmpty(value)) {
            return defaultValue;
        }
        return java.lang.Boolean.parseBoolean(value);
    }

    public static void writeBooleanAttribute(org.xmlpull.v1.XmlSerializer out, java.lang.String name, boolean value) throws java.io.IOException {
        if (out instanceof com.android.modules.utils.TypedXmlSerializer) {
            ((com.android.modules.utils.TypedXmlSerializer) out).attributeBoolean((java.lang.String) null, name, value);
        } else {
            out.attribute(null, name, java.lang.Boolean.toString(value));
        }
    }

    public static android.net.Uri readUriAttribute(org.xmlpull.v1.XmlPullParser in, java.lang.String name) {
        java.lang.String value = in.getAttributeValue(null, name);
        if (value != null) {
            return android.net.Uri.parse(value);
        }
        return null;
    }

    public static void writeUriAttribute(org.xmlpull.v1.XmlSerializer out, java.lang.String name, android.net.Uri value) throws java.io.IOException {
        if (value != null) {
            out.attribute(null, name, value.toString());
        }
    }

    public static java.lang.String readStringAttribute(org.xmlpull.v1.XmlPullParser in, java.lang.String name) {
        return in.getAttributeValue(null, name);
    }

    public static void writeStringAttribute(org.xmlpull.v1.XmlSerializer out, java.lang.String name, java.lang.CharSequence value) throws java.io.IOException {
        if (value != null) {
            out.attribute(null, name, value.toString());
        }
    }

    public static byte[] readByteArrayAttribute(org.xmlpull.v1.XmlPullParser in, java.lang.String name) {
        if (in instanceof com.android.modules.utils.TypedXmlPullParser) {
            try {
                return ((com.android.modules.utils.TypedXmlPullParser) in).getAttributeBytesBase64((java.lang.String) null, name);
            } catch (org.xmlpull.v1.XmlPullParserException e) {
                return null;
            }
        }
        java.lang.String value = in.getAttributeValue(null, name);
        if (android.text.TextUtils.isEmpty(value)) {
            return null;
        }
        return android.util.Base64.decode(value, 0);
    }

    public static void writeByteArrayAttribute(org.xmlpull.v1.XmlSerializer out, java.lang.String name, byte[] value) throws java.io.IOException {
        if (value != null) {
            if (out instanceof com.android.modules.utils.TypedXmlSerializer) {
                ((com.android.modules.utils.TypedXmlSerializer) out).attributeBytesBase64((java.lang.String) null, name, value);
            } else {
                out.attribute(null, name, android.util.Base64.encodeToString(value, 0));
            }
        }
    }

    public static android.graphics.Bitmap readBitmapAttribute(org.xmlpull.v1.XmlPullParser in, java.lang.String name) {
        byte[] value = readByteArrayAttribute(in, name);
        if (value != null) {
            return android.graphics.BitmapFactory.decodeByteArray(value, 0, value.length);
        }
        return null;
    }

    @java.lang.Deprecated
    public static void writeBitmapAttribute(org.xmlpull.v1.XmlSerializer out, java.lang.String name, android.graphics.Bitmap value) throws java.io.IOException {
        if (value != null) {
            java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
            value.compress(android.graphics.Bitmap.CompressFormat.PNG, 90, os);
            writeByteArrayAttribute(out, name, os.toByteArray());
        }
    }
}
