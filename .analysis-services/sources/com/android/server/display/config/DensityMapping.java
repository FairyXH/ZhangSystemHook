package com.android.server.display.config;

/* JADX INFO: loaded from: classes2.dex */
public class DensityMapping {
    private java.util.List<com.android.server.display.config.Density> density;

    public java.util.List<com.android.server.display.config.Density> getDensity() {
        if (this.density == null) {
            this.density = new java.util.ArrayList();
        }
        return this.density;
    }

    static com.android.server.display.config.DensityMapping read(org.xmlpull.v1.XmlPullParser _parser) throws org.xmlpull.v1.XmlPullParserException, javax.xml.datatype.DatatypeConfigurationException, java.io.IOException {
        int type;
        com.android.server.display.config.DensityMapping _instance = new com.android.server.display.config.DensityMapping();
        _parser.getDepth();
        while (true) {
            type = _parser.next();
            if (type == 1 || type == 3) {
                break;
            }
            if (_parser.getEventType() == 2) {
                java.lang.String _tagName = _parser.getName();
                if (_tagName.equals("density")) {
                    com.android.server.display.config.Density _value = com.android.server.display.config.Density.read(_parser);
                    _instance.getDensity().add(_value);
                } else {
                    com.android.server.display.config.XmlParser.skip(_parser);
                }
            }
        }
        if (type != 3) {
            throw new javax.xml.datatype.DatatypeConfigurationException("DensityMapping is not closed");
        }
        return _instance;
    }
}
