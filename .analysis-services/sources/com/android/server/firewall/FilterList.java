package com.android.server.firewall;

/* JADX INFO: loaded from: classes2.dex */
abstract class FilterList implements com.android.server.firewall.Filter {
    protected final java.util.ArrayList<com.android.server.firewall.Filter> children = new java.util.ArrayList<>();

    FilterList() {
    }

    public com.android.server.firewall.FilterList readFromXml(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            readChild(parser);
        }
        return this;
    }

    protected void readChild(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        com.android.server.firewall.Filter filter = com.android.server.firewall.IntentFirewall.parseFilter(parser);
        this.children.add(filter);
    }
}
