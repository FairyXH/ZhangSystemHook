package com.android.server.infra;

/* JADX INFO: loaded from: classes2.dex */
public final class SecureSettingsServiceNameResolver extends com.android.server.infra.ServiceNameBaseResolver {
    private static final char COMPONENT_NAME_SEPARATOR = ':';
    private final java.lang.String mProperty;
    private final android.text.TextUtils.SimpleStringSplitter mStringColonSplitter;

    public SecureSettingsServiceNameResolver(android.content.Context context, java.lang.String property) {
        this(context, property, false);
    }

    public SecureSettingsServiceNameResolver(android.content.Context context, java.lang.String property, boolean isMultiple) {
        super(context, isMultiple);
        this.mStringColonSplitter = new android.text.TextUtils.SimpleStringSplitter(COMPONENT_NAME_SEPARATOR);
        this.mProperty = property;
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public void dumpShort(java.io.PrintWriter pw) {
        pw.print("SecureSettingsServiceNamer: prop=");
        pw.print(this.mProperty);
    }

    @Override // com.android.server.infra.ServiceNameBaseResolver, com.android.server.infra.ServiceNameResolver
    public void dumpShort(java.io.PrintWriter pw, int userId) {
        pw.print("defaultService=");
        pw.print(getDefaultServiceName(userId));
    }

    @Override // com.android.server.infra.ServiceNameBaseResolver
    public java.lang.String toString() {
        return "SecureSettingsServiceNameResolver[" + this.mProperty + "]";
    }

    @Override // com.android.server.infra.ServiceNameBaseResolver
    public java.lang.String[] readServiceNameList(int userId) {
        return parseColonDelimitedServiceNames(android.provider.Settings.Secure.getStringForUser(this.mContext.getContentResolver(), this.mProperty, userId));
    }

    @Override // com.android.server.infra.ServiceNameBaseResolver
    public java.lang.String readServiceName(int userId) {
        return android.provider.Settings.Secure.getStringForUser(this.mContext.getContentResolver(), this.mProperty, userId);
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public void setServiceNameList(java.util.List<java.lang.String> componentNames, int userId) {
        if (componentNames == null || componentNames.isEmpty()) {
            android.provider.Settings.Secure.putStringForUser(this.mContext.getContentResolver(), this.mProperty, null, userId);
            return;
        }
        java.lang.StringBuilder builder = new java.lang.StringBuilder(componentNames.get(0));
        for (int i = 1; i < componentNames.size(); i++) {
            builder.append(COMPONENT_NAME_SEPARATOR);
            builder.append(componentNames.get(i));
        }
        android.provider.Settings.Secure.putStringForUser(this.mContext.getContentResolver(), this.mProperty, builder.toString(), userId);
    }

    private java.lang.String[] parseColonDelimitedServiceNames(java.lang.String serviceNames) {
        java.util.Set<java.lang.String> delimitedServices = new android.util.ArraySet<>();
        if (!android.text.TextUtils.isEmpty(serviceNames)) {
            android.text.TextUtils.SimpleStringSplitter splitter = this.mStringColonSplitter;
            splitter.setString(serviceNames);
            while (splitter.hasNext()) {
                java.lang.String str = splitter.next();
                if (!android.text.TextUtils.isEmpty(str)) {
                    delimitedServices.add(str);
                }
            }
        }
        java.lang.String[] delimitedServicesArray = new java.lang.String[delimitedServices.size()];
        return (java.lang.String[]) delimitedServices.toArray(delimitedServicesArray);
    }
}
