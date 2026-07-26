package com.android.server.firewall;

/* JADX INFO: loaded from: classes2.dex */
class AndFilter extends com.android.server.firewall.FilterList {
    public static final com.android.server.firewall.FilterFactory FACTORY = new com.android.server.firewall.FilterFactory("and") { // from class: com.android.server.firewall.AndFilter.1
        @Override // com.android.server.firewall.FilterFactory
        public com.android.server.firewall.Filter newFilter(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            return new com.android.server.firewall.AndFilter().readFromXml(parser);
        }
    };

    AndFilter() {
    }

    @Override // com.android.server.firewall.Filter
    public boolean matches(com.android.server.firewall.IntentFirewall ifw, android.content.ComponentName resolvedComponent, android.content.Intent intent, int callerUid, int callerPid, java.lang.String resolvedType, int receivingUid) {
        for (int i = 0; i < this.children.size(); i++) {
            if (!this.children.get(i).matches(ifw, resolvedComponent, intent, callerUid, callerPid, resolvedType, receivingUid)) {
                return false;
            }
        }
        return true;
    }
}
