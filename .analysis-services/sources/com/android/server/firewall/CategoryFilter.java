package com.android.server.firewall;

/* JADX INFO: loaded from: classes2.dex */
class CategoryFilter implements com.android.server.firewall.Filter {
    private static final java.lang.String ATTR_NAME = "name";
    public static final com.android.server.firewall.FilterFactory FACTORY = new com.android.server.firewall.FilterFactory("category") { // from class: com.android.server.firewall.CategoryFilter.1
        @Override // com.android.server.firewall.FilterFactory
        public com.android.server.firewall.Filter newFilter(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            java.lang.String categoryName = parser.getAttributeValue(null, "name");
            if (categoryName == null) {
                throw new org.xmlpull.v1.XmlPullParserException("Category name must be specified.", parser, null);
            }
            return new com.android.server.firewall.CategoryFilter(categoryName);
        }
    };
    private final java.lang.String mCategoryName;

    private CategoryFilter(java.lang.String categoryName) {
        this.mCategoryName = categoryName;
    }

    @Override // com.android.server.firewall.Filter
    public boolean matches(com.android.server.firewall.IntentFirewall ifw, android.content.ComponentName resolvedComponent, android.content.Intent intent, int callerUid, int callerPid, java.lang.String resolvedType, int receivingUid) {
        java.util.Set<java.lang.String> categories = intent.getCategories();
        if (categories == null) {
            return false;
        }
        return categories.contains(this.mCategoryName);
    }
}
