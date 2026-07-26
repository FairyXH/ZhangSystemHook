package com.android.server.firewall;

/* JADX INFO: loaded from: classes2.dex */
class NotFilter implements com.android.server.firewall.Filter {
    public static final com.android.server.firewall.FilterFactory FACTORY = new com.android.server.firewall.FilterFactory("not") { // from class: com.android.server.firewall.NotFilter.1
        @Override // com.android.server.firewall.FilterFactory
        public com.android.server.firewall.Filter newFilter(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            com.android.server.firewall.Filter child = null;
            int outerDepth = parser.getDepth();
            while (true) {
                if (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
                    com.android.server.firewall.Filter filter = com.android.server.firewall.IntentFirewall.parseFilter(parser);
                    if (child == null) {
                        child = filter;
                    } else {
                        throw new org.xmlpull.v1.XmlPullParserException("<not> tag can only contain a single child filter.", parser, null);
                    }
                } else {
                    return new com.android.server.firewall.NotFilter(child);
                }
            }
        }
    };
    private final com.android.server.firewall.Filter mChild;

    private NotFilter(com.android.server.firewall.Filter child) {
        this.mChild = child;
    }

    @Override // com.android.server.firewall.Filter
    public boolean matches(com.android.server.firewall.IntentFirewall ifw, android.content.ComponentName resolvedComponent, android.content.Intent intent, int callerUid, int callerPid, java.lang.String resolvedType, int receivingUid) {
        return !this.mChild.matches(ifw, resolvedComponent, intent, callerUid, callerPid, resolvedType, receivingUid);
    }
}
