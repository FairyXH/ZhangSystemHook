package com.android.server.voiceinteraction;

/* JADX INFO: loaded from: classes3.dex */
class RecognitionServiceInfo {
    private static final java.lang.String TAG = "RecognitionServiceInfo";
    private final java.lang.String mParseError;
    private final boolean mSelectableAsDefault;
    private final android.content.pm.ServiceInfo mServiceInfo;

    static java.util.List<com.android.server.voiceinteraction.RecognitionServiceInfo> getAvailableServices(android.content.Context context, int user) {
        java.util.List<com.android.server.voiceinteraction.RecognitionServiceInfo> services = new java.util.ArrayList<>();
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = context.getPackageManager().queryIntentServicesAsUser(new android.content.Intent("android.speech.RecognitionService"), com.android.server.permission.access.permission.PermissionFlags.MASK_RESTRICTED, user);
        for (android.content.pm.ResolveInfo resolveInfo : resolveInfos) {
            com.android.server.voiceinteraction.RecognitionServiceInfo service = parseInfo(context.getPackageManager(), resolveInfo.serviceInfo);
            if (!android.text.TextUtils.isEmpty(service.mParseError)) {
                android.util.Log.w(TAG, "Parse error in getAvailableServices: " + service.mParseError);
            }
            services.add(service);
        }
        return services;
    }

    static com.android.server.voiceinteraction.RecognitionServiceInfo parseInfo(android.content.pm.PackageManager pm, android.content.pm.ServiceInfo si) {
        android.content.res.XmlResourceParser parser;
        java.lang.String parseError = "";
        boolean selectableAsDefault = true;
        try {
            parser = si.loadXmlMetaData(pm, "android.speech");
        } catch (android.content.pm.PackageManager.NameNotFoundException | java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
            parseError = "Error parsing recognition service meta-data: " + e;
        }
        try {
            if (parser == null) {
                com.android.server.voiceinteraction.RecognitionServiceInfo recognitionServiceInfo = new com.android.server.voiceinteraction.RecognitionServiceInfo(si, true, "No android.speech meta-data for " + si.packageName);
                if (parser != null) {
                    parser.close();
                }
                return recognitionServiceInfo;
            }
            android.content.res.Resources res = pm.getResourcesForApplication(si.applicationInfo);
            android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            for (int type = 0; type != 1 && type != 2; type = parser.next()) {
            }
            java.lang.String nodeName = parser.getName();
            if (!"recognition-service".equals(nodeName)) {
                throw new org.xmlpull.v1.XmlPullParserException("Meta-data does not start with recognition-service tag");
            }
            android.content.res.TypedArray values = res.obtainAttributes(attrs, com.android.internal.R.styleable.RecognitionService);
            selectableAsDefault = values.getBoolean(1, true);
            values.recycle();
            if (parser != null) {
                parser.close();
            }
            return new com.android.server.voiceinteraction.RecognitionServiceInfo(si, selectableAsDefault, parseError);
        } catch (java.lang.Throwable th) {
            if (parser != null) {
                try {
                    parser.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private RecognitionServiceInfo(android.content.pm.ServiceInfo si, boolean selectableAsDefault, java.lang.String parseError) {
        this.mServiceInfo = si;
        this.mSelectableAsDefault = selectableAsDefault;
        this.mParseError = parseError;
    }

    public java.lang.String getParseError() {
        return this.mParseError;
    }

    public android.content.pm.ServiceInfo getServiceInfo() {
        return this.mServiceInfo;
    }

    public boolean isSelectableAsDefault() {
        return this.mSelectableAsDefault;
    }
}
