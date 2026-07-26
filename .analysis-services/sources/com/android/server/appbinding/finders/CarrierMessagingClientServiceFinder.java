package com.android.server.appbinding.finders;

/* JADX INFO: loaded from: classes.dex */
public class CarrierMessagingClientServiceFinder extends com.android.server.appbinding.finders.AppServiceFinder<android.service.carrier.CarrierMessagingClientService, android.service.carrier.ICarrierMessagingClientService> {
    private final android.app.role.OnRoleHoldersChangedListener mRoleHolderChangedListener;
    private final android.app.role.RoleManager mRoleManager;

    public CarrierMessagingClientServiceFinder(android.content.Context context, java.util.function.BiConsumer<com.android.server.appbinding.finders.AppServiceFinder, java.lang.Integer> listener, android.os.Handler callbackHandler) {
        super(context, listener, callbackHandler);
        this.mRoleHolderChangedListener = new android.app.role.OnRoleHoldersChangedListener() { // from class: com.android.server.appbinding.finders.CarrierMessagingClientServiceFinder$$ExternalSyntheticLambda0
            public final void onRoleHoldersChanged(java.lang.String str, android.os.UserHandle userHandle) {
                this.f$0.lambda$new$0(str, userHandle);
            }
        };
        this.mRoleManager = (android.app.role.RoleManager) context.getSystemService(android.app.role.RoleManager.class);
    }

    @Override // com.android.server.appbinding.finders.AppServiceFinder
    protected boolean isEnabled(com.android.server.appbinding.AppBindingConstants constants) {
        return constants.SMS_SERVICE_ENABLED && this.mContext.getResources().getBoolean(android.R.bool.config_supportsSplitScreenMultiWindow);
    }

    @Override // com.android.server.appbinding.finders.AppServiceFinder
    public java.lang.String getAppDescription() {
        return "[Default SMS app]";
    }

    @Override // com.android.server.appbinding.finders.AppServiceFinder
    protected java.lang.Class<android.service.carrier.CarrierMessagingClientService> getServiceClass() {
        return android.service.carrier.CarrierMessagingClientService.class;
    }

    @Override // com.android.server.appbinding.finders.AppServiceFinder
    public android.service.carrier.ICarrierMessagingClientService asInterface(android.os.IBinder obj) {
        return android.service.carrier.ICarrierMessagingClientService.Stub.asInterface(obj);
    }

    @Override // com.android.server.appbinding.finders.AppServiceFinder
    protected java.lang.String getServiceAction() {
        return "android.telephony.action.CARRIER_MESSAGING_CLIENT_SERVICE";
    }

    @Override // com.android.server.appbinding.finders.AppServiceFinder
    protected java.lang.String getServicePermission() {
        return "android.permission.BIND_CARRIER_MESSAGING_CLIENT_SERVICE";
    }

    @Override // com.android.server.appbinding.finders.AppServiceFinder
    public java.lang.String getTargetPackage(int userId) {
        java.lang.String ret = (java.lang.String) com.android.internal.util.CollectionUtils.firstOrNull(this.mRoleManager.getRoleHoldersAsUser("android.app.role.SMS", android.os.UserHandle.of(userId)));
        return ret;
    }

    @Override // com.android.server.appbinding.finders.AppServiceFinder
    public void startMonitoring() {
        this.mRoleManager.addOnRoleHoldersChangedListenerAsUser(com.android.internal.os.BackgroundThread.getExecutor(), this.mRoleHolderChangedListener, android.os.UserHandle.ALL);
    }

    @Override // com.android.server.appbinding.finders.AppServiceFinder
    protected java.lang.String validateService(android.content.pm.ServiceInfo service) {
        java.lang.String packageName = service.packageName;
        java.lang.String process = service.processName;
        if (process == null || android.text.TextUtils.equals(packageName, process)) {
            return "Service must not run on the main process";
        }
        return null;
    }

    @Override // com.android.server.appbinding.finders.AppServiceFinder
    public int getBindFlags(com.android.server.appbinding.AppBindingConstants constants) {
        return constants.SMS_APP_BIND_FLAGS;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(java.lang.String role, android.os.UserHandle user) {
        if ("android.app.role.SMS".equals(role)) {
            this.mListener.accept(this, java.lang.Integer.valueOf(user.getIdentifier()));
        }
    }
}
