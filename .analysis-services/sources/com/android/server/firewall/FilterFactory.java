package com.android.server.firewall;

/* JADX INFO: loaded from: classes2.dex */
public abstract class FilterFactory {
    private final java.lang.String mTag;

    public abstract com.android.server.firewall.Filter newFilter(org.xmlpull.v1.XmlPullParser xmlPullParser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException;

    protected FilterFactory(java.lang.String tag) {
        if (tag == null) {
            throw new java.lang.NullPointerException();
        }
        this.mTag = tag;
    }

    public java.lang.String getTagName() {
        return this.mTag;
    }
}
