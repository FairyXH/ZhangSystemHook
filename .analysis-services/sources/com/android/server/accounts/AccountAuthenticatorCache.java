package com.android.server.accounts;

/* JADX INFO: loaded from: classes.dex */
class AccountAuthenticatorCache extends android.content.pm.RegisteredServicesCache<android.accounts.AuthenticatorDescription> implements com.android.server.accounts.IAccountAuthenticatorCache {
    private static final java.lang.String TAG = "Account";
    private static final com.android.server.accounts.AccountAuthenticatorCache.MySerializer sSerializer = new com.android.server.accounts.AccountAuthenticatorCache.MySerializer();

    @Override // com.android.server.accounts.IAccountAuthenticatorCache
    public /* bridge */ /* synthetic */ android.content.pm.RegisteredServicesCache.ServiceInfo getServiceInfo(android.accounts.AuthenticatorDescription authenticatorDescription, int i) {
        return super.getServiceInfo(authenticatorDescription, i);
    }

    public AccountAuthenticatorCache(android.content.Context context) {
        super(context, "android.accounts.AccountAuthenticator", "android.accounts.AccountAuthenticator", "account-authenticator", sSerializer);
    }

    /* JADX INFO: renamed from: parseServiceAttributes, reason: merged with bridge method [inline-methods] */
    public android.accounts.AuthenticatorDescription m877parseServiceAttributes(android.content.res.Resources res, java.lang.String packageName, android.util.AttributeSet attrs) {
        android.content.res.TypedArray sa = res.obtainAttributes(attrs, com.android.internal.R.styleable.AccountAuthenticator);
        try {
            java.lang.String accountType = sa.getString(2);
            int labelId = sa.getResourceId(0, 0);
            int iconId = sa.getResourceId(1, 0);
            int smallIconId = sa.getResourceId(3, 0);
            int prefId = sa.getResourceId(4, 0);
            boolean customTokens = sa.getBoolean(5, false);
            if (!android.text.TextUtils.isEmpty(accountType)) {
                return new android.accounts.AuthenticatorDescription(accountType, packageName, labelId, iconId, smallIconId, prefId, customTokens);
            }
            sa.recycle();
            return null;
        } finally {
            sa.recycle();
        }
    }

    private static class MySerializer implements android.content.pm.XmlSerializerAndParser<android.accounts.AuthenticatorDescription> {
        private MySerializer() {
        }

        public void writeAsXml(android.accounts.AuthenticatorDescription item, com.android.modules.utils.TypedXmlSerializer out) throws java.io.IOException {
            out.attribute((java.lang.String) null, "type", item.type);
        }

        /* JADX INFO: renamed from: createFromXml, reason: merged with bridge method [inline-methods] */
        public android.accounts.AuthenticatorDescription m878createFromXml(com.android.modules.utils.TypedXmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
            return android.accounts.AuthenticatorDescription.newKey(parser.getAttributeValue((java.lang.String) null, "type"));
        }
    }
}
