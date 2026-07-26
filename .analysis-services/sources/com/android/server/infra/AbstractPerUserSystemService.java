package com.android.server.infra;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AbstractPerUserSystemService<S extends com.android.server.infra.AbstractPerUserSystemService<S, M>, M extends com.android.server.infra.AbstractMasterSystemService<M, S>> {
    private boolean mDisabled;
    public final java.lang.Object mLock;
    protected final M mMaster;
    private android.content.pm.ServiceInfo mServiceInfo;
    private boolean mSetupComplete;
    protected final java.lang.String mTag = getClass().getSimpleName();
    protected final int mUserId;

    protected AbstractPerUserSystemService(M master, java.lang.Object lock, int userId) {
        this.mMaster = master;
        this.mLock = lock;
        this.mUserId = userId;
        updateIsSetupComplete(userId);
    }

    private void updateIsSetupComplete(int userId) {
        java.lang.String setupComplete = android.provider.Settings.Secure.getStringForUser(getContext().getContentResolver(), "user_setup_complete", userId);
        this.mSetupComplete = "1".equals(setupComplete);
    }

    protected android.content.pm.ServiceInfo newServiceInfoLocked(android.content.ComponentName serviceComponent) throws android.content.pm.PackageManager.NameNotFoundException {
        throw new java.lang.UnsupportedOperationException("not overridden");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void handlePackageUpdateLocked(java.lang.String packageName) {
    }

    protected boolean isEnabledLocked() {
        return (!this.mSetupComplete || this.mServiceInfo == null || this.mDisabled) ? false : true;
    }

    protected final boolean isDisabledByUserRestrictionsLocked() {
        return this.mDisabled;
    }

    protected boolean updateLocked(boolean disabled) {
        boolean wasEnabled = isEnabledLocked();
        if (this.mMaster.verbose) {
            android.util.Slog.v(this.mTag, "updateLocked(u=" + this.mUserId + "): wasEnabled=" + wasEnabled + ", mSetupComplete=" + this.mSetupComplete + ", disabled=" + disabled + ", mDisabled=" + this.mDisabled);
        }
        updateIsSetupComplete(this.mUserId);
        this.mDisabled = disabled;
        if (this.mMaster.mServiceNameResolver != null && this.mMaster.mServiceNameResolver.isConfiguredInMultipleMode()) {
            if (this.mMaster.debug) {
                android.util.Slog.d(this.mTag, "Should not end up in updateLocked when isConfiguredInMultipleMode is true");
            }
        } else {
            updateServiceInfoLocked();
        }
        return wasEnabled != isEnabledLocked();
    }

    protected final android.content.ComponentName updateServiceInfoLocked() {
        android.content.ComponentName[] componentNames = updateServiceInfoListLocked();
        if (componentNames == null || componentNames.length == 0) {
            return null;
        }
        return componentNames[0];
    }

    protected final android.content.ComponentName[] updateServiceInfoListLocked() {
        if (this.mMaster.mServiceNameResolver == null) {
            return null;
        }
        if (!this.mMaster.mServiceNameResolver.isConfiguredInMultipleMode()) {
            java.lang.String componentName = getComponentNameLocked();
            return new android.content.ComponentName[]{getServiceComponent(componentName)};
        }
        java.lang.String[] componentNames = this.mMaster.mServiceNameResolver.getServiceNameList(this.mUserId);
        if (componentNames == null) {
            return null;
        }
        android.content.ComponentName[] serviceComponents = new android.content.ComponentName[componentNames.length];
        for (int i = 0; i < componentNames.length; i++) {
            serviceComponents[i] = getServiceComponent(componentNames[i]);
        }
        return serviceComponents;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x006d A[Catch: Exception -> 0x00d7, all -> 0x00fe, TRY_ENTER, TryCatch #0 {Exception -> 0x00d7, blocks: (B:17:0x006d, B:19:0x0079, B:20:0x00aa, B:22:0x00b2), top: B:32:0x006b, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:20:0x00aa A[Catch: Exception -> 0x00d7, all -> 0x00fe, TryCatch #0 {Exception -> 0x00d7, blocks: (B:17:0x006d, B:19:0x0079, B:20:0x00aa, B:22:0x00b2), top: B:32:0x006b, outer: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private android.content.ComponentName getServiceComponent(java.lang.String r9) {
        /*
            Method dump skipped, instruction units count: 257
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.infra.AbstractPerUserSystemService.getServiceComponent(java.lang.String):android.content.ComponentName");
    }

    private void updateService(android.content.ComponentName serviceComponent) {
        if (getClass().getSimpleName().contains("AutofillManager") && this.mUserId == 0) {
            com.android.server.pm.UserManagerInternal userManager = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
            if (userManager.isUserUnlockingOrUnlocked(this.mUserId)) {
                printInfo(serviceComponent);
                this.mMaster.updateService(this.mUserId);
            } else {
                android.util.Slog.e(this.mTag, "isUserUnlockingOrUnlocked false ");
            }
        }
    }

    private void printInfo(android.content.ComponentName serviceComponent) {
        android.content.pm.ServiceInfo info;
        if (serviceComponent == null) {
            android.util.Slog.e(this.mTag, "serviceComponent == null ");
            return;
        }
        try {
            android.content.pm.ServiceInfo info2 = android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, 786432L, this.mUserId);
            if (info2 != null) {
                android.util.Slog.d(this.mTag, "get serviceComponent with flags: " + info2);
                postStatisticEvent(getContext(), 1, info2.toString());
            }
            if (info2 == null && (info = android.app.AppGlobals.getPackageManager().getServiceInfo(serviceComponent, 786944L, this.mUserId)) != null) {
                android.util.Slog.d(this.mTag, "get serviceComponent with flags2: " + info);
                postStatisticEvent(getContext(), 2, info.toString());
            }
        } catch (java.lang.Exception ex) {
            android.util.Slog.e(this.mTag, "retry error: " + ex);
        }
    }

    private void postStatisticEvent(android.content.Context cxt, int type, java.lang.String message) {
        if (cxt == null) {
            return;
        }
        java.util.HashMap<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
        map.put("type", java.lang.String.valueOf(type));
        map.put("msg", message);
        ((oplus.util.IOplusStatisticsExt) system.ext.loader.core.ExtLoader.type(oplus.util.IOplusStatisticsExt.class).create()).onCommon(cxt, "AutoFill", "err_info", map, false);
    }

    public final int getUserId() {
        return this.mUserId;
    }

    public final M getMaster() {
        return this.mMaster;
    }

    protected final int getServiceUidLocked() {
        if (this.mServiceInfo == null) {
            if (this.mMaster.verbose) {
                android.util.Slog.v(this.mTag, "getServiceUidLocked(): no mServiceInfo");
                return -1;
            }
            return -1;
        }
        return this.mServiceInfo.applicationInfo.uid;
    }

    protected final java.lang.String getComponentNameLocked() {
        return this.mMaster.mServiceNameResolver.getServiceName(this.mUserId);
    }

    protected final java.lang.String getComponentNameForMultipleLocked(java.lang.String serviceName) {
        java.lang.String[] services = this.mMaster.mServiceNameResolver.getServiceNameList(this.mUserId);
        for (int i = 0; i < services.length; i++) {
            if (serviceName.equals(services[i])) {
                return services[i];
            }
        }
        return null;
    }

    public final boolean isTemporaryServiceSetLocked() {
        return this.mMaster.mServiceNameResolver.isTemporary(this.mUserId);
    }

    protected final void resetTemporaryServiceLocked() {
        this.mMaster.mServiceNameResolver.resetTemporaryService(this.mUserId);
    }

    public final android.content.pm.ServiceInfo getServiceInfo() {
        return this.mServiceInfo;
    }

    public final android.content.ComponentName getServiceComponentName() {
        android.content.ComponentName componentName;
        synchronized (this.mLock) {
            componentName = this.mServiceInfo == null ? null : this.mServiceInfo.getComponentName();
        }
        return componentName;
    }

    public final java.lang.String getServicePackageName() {
        android.content.ComponentName serviceComponent = getServiceComponentName();
        if (serviceComponent == null) {
            return null;
        }
        return serviceComponent.getPackageName();
    }

    public final java.lang.CharSequence getServiceLabelLocked() {
        if (this.mServiceInfo == null) {
            return null;
        }
        return this.mServiceInfo.loadSafeLabel(getContext().getPackageManager(), 0.0f, 5);
    }

    public final android.graphics.drawable.Drawable getServiceIconLocked() {
        if (this.mServiceInfo == null) {
            return null;
        }
        return this.mServiceInfo.loadIcon(getContext().getPackageManager());
    }

    protected final void removeSelfFromCache() {
        synchronized (this.mMaster.mLock) {
            this.mMaster.removeCachedServiceListLocked(this.mUserId);
        }
    }

    public final boolean isDebug() {
        return this.mMaster.debug;
    }

    public final boolean isVerbose() {
        return this.mMaster.verbose;
    }

    public final int getTargedSdkLocked() {
        if (this.mServiceInfo == null) {
            return 0;
        }
        return this.mServiceInfo.applicationInfo.targetSdkVersion;
    }

    protected final boolean isSetupCompletedLocked() {
        return this.mSetupComplete;
    }

    protected final android.content.Context getContext() {
        return this.mMaster.getContext();
    }

    protected void dumpLocked(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.print("User: ");
        pw.println(this.mUserId);
        if (this.mServiceInfo != null) {
            pw.print(prefix);
            pw.print("Service Label: ");
            pw.println(getServiceLabelLocked());
            pw.print(prefix);
            pw.print("Target SDK: ");
            pw.println(getTargedSdkLocked());
        }
        if (this.mMaster.mServiceNameResolver != null) {
            pw.print(prefix);
            pw.print("Name resolver: ");
            this.mMaster.mServiceNameResolver.dumpShort(pw, this.mUserId);
            pw.println();
        }
        pw.print(prefix);
        pw.print("Disabled by UserManager: ");
        pw.println(this.mDisabled);
        pw.print(prefix);
        pw.print("Setup complete: ");
        pw.println(this.mSetupComplete);
        if (this.mServiceInfo != null) {
            pw.print(prefix);
            pw.print("Service UID: ");
            pw.println(this.mServiceInfo.applicationInfo.uid);
        }
        pw.println();
    }
}
