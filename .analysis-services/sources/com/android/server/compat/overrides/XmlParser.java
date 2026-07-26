package com.android.server.compat.overrides;

/* JADX INFO: loaded from: classes.dex */
public class XmlParser {
    public static com.android.server.compat.overrides.Overrides read(java.io.InputStream in) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        org.xmlpull.v1.XmlPullParser _parser = org.xmlpull.v1.XmlPullParserFactory.newInstance().newPullParser();
        _parser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
        _parser.setInput(in, null);
        _parser.nextTag();
        java.lang.String _tagName = _parser.getName();
        if (!_tagName.equals("overrides")) {
            return null;
        }
        com.android.server.compat.overrides.Overrides _value = com.android.server.compat.overrides.Overrides.read(_parser);
        return _value;
    }

    public static java.lang.String readText(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (_parser.next() != 4) {
            return "";
        }
        java.lang.String result = _parser.getText();
        _parser.nextTag();
        return result;
    }

    public static void skip(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        if (_parser.getEventType() != 2) {
            throw new java.lang.IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (_parser.next()) {
                case 2:
                    depth++;
                    break;
                case 3:
                    depth--;
                    break;
            }
        }
    }
}
