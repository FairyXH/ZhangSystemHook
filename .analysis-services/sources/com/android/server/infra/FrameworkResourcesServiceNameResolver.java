package com.android.server.infra;

/* JADX INFO: loaded from: classes2.dex */
public final class FrameworkResourcesServiceNameResolver extends com.android.server.infra.ServiceNameBaseResolver {
    private final int mArrayResourceId;
    private final int mStringResourceId;

    public FrameworkResourcesServiceNameResolver(android.content.Context context, int resourceId) {
        super(context, false);
        this.mStringResourceId = resourceId;
        this.mArrayResourceId = -1;
    }

    public FrameworkResourcesServiceNameResolver(android.content.Context context, int resourceId, boolean isMultiple) {
        super(context, isMultiple);
        if (!isMultiple) {
            throw new java.lang.UnsupportedOperationException("Please use FrameworkResourcesServiceNameResolver(context, @StringRes int) constructor if single service mode is requested.");
        }
        this.mStringResourceId = -1;
        this.mArrayResourceId = resourceId;
    }

    @Override // com.android.server.infra.ServiceNameBaseResolver
    public java.lang.String[] readServiceNameList(int userId) {
        return this.mContext.getResources().getStringArray(this.mArrayResourceId);
    }

    @Override // com.android.server.infra.ServiceNameBaseResolver
    public java.lang.String readServiceName(int userId) {
        return this.mContext.getResources().getString(this.mStringResourceId);
    }

    @Override // com.android.server.infra.ServiceNameResolver
    public void dumpShort(java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            pw.print("FrameworkResourcesServiceNamer: resId=");
            pw.print(this.mStringResourceId);
            pw.print(", numberTemps=");
            pw.print(this.mTemporaryServiceNamesList.size());
            pw.print(", enabledDefaults=");
            pw.print(this.mDefaultServicesDisabled.size());
        }
    }
}
