package com.android.server.firewall;

/* JADX INFO: loaded from: classes2.dex */
class PortFilter implements com.android.server.firewall.Filter {
    private static final java.lang.String ATTR_EQUALS = "equals";
    private static final java.lang.String ATTR_MAX = "max";
    private static final java.lang.String ATTR_MIN = "min";
    public static final com.android.server.firewall.FilterFactory FACTORY = new com.android.server.firewall.FilterFactory("port") { // from class: com.android.server.firewall.PortFilter.1
        @Override // com.android.server.firewall.FilterFactory
        public com.android.server.firewall.Filter newFilter(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            int lowerBound = -1;
            int upperBound = -1;
            java.lang.String equalsValue = parser.getAttributeValue(null, com.android.server.firewall.PortFilter.ATTR_EQUALS);
            if (equalsValue != null) {
                try {
                    int value = java.lang.Integer.parseInt(equalsValue);
                    lowerBound = value;
                    upperBound = value;
                } catch (java.lang.NumberFormatException e) {
                    throw new org.xmlpull.v1.XmlPullParserException("Invalid port value: " + equalsValue, parser, null);
                }
            }
            java.lang.String lowerBoundString = parser.getAttributeValue(null, com.android.server.firewall.PortFilter.ATTR_MIN);
            java.lang.String upperBoundString = parser.getAttributeValue(null, com.android.server.firewall.PortFilter.ATTR_MAX);
            if (lowerBoundString != null || upperBoundString != null) {
                if (equalsValue != null) {
                    throw new org.xmlpull.v1.XmlPullParserException("Port filter cannot use both equals and range filtering", parser, null);
                }
                if (lowerBoundString != null) {
                    try {
                        lowerBound = java.lang.Integer.parseInt(lowerBoundString);
                    } catch (java.lang.NumberFormatException e2) {
                        throw new org.xmlpull.v1.XmlPullParserException("Invalid minimum port value: " + lowerBoundString, parser, null);
                    }
                }
                if (upperBoundString != null) {
                    try {
                        upperBound = java.lang.Integer.parseInt(upperBoundString);
                    } catch (java.lang.NumberFormatException e3) {
                        throw new org.xmlpull.v1.XmlPullParserException("Invalid maximum port value: " + upperBoundString, parser, null);
                    }
                }
            }
            return new com.android.server.firewall.PortFilter(lowerBound, upperBound);
        }
    };
    private static final int NO_BOUND = -1;
    private final int mLowerBound;
    private final int mUpperBound;

    private PortFilter(int lowerBound, int upperBound) {
        this.mLowerBound = lowerBound;
        this.mUpperBound = upperBound;
    }

    @Override // com.android.server.firewall.Filter
    public boolean matches(com.android.server.firewall.IntentFirewall ifw, android.content.ComponentName resolvedComponent, android.content.Intent intent, int callerUid, int callerPid, java.lang.String resolvedType, int receivingUid) {
        int port = -1;
        android.net.Uri uri = intent.getData();
        if (uri != null) {
            port = uri.getPort();
        }
        return port != -1 && (this.mLowerBound == -1 || this.mLowerBound <= port) && (this.mUpperBound == -1 || this.mUpperBound >= port);
    }
}
