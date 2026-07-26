package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public class SystemPackageResetHelper extends com.android.server.location.injector.PackageResetHelper {
    private final android.content.Context mContext;
    private android.content.BroadcastReceiver mReceiver;

    public SystemPackageResetHelper(android.content.Context context) {
        this.mContext = context;
    }

    @Override // com.android.server.location.injector.PackageResetHelper
    protected void onRegister() {
        com.android.internal.util.Preconditions.checkState(this.mReceiver == null);
        this.mReceiver = new com.android.server.location.injector.SystemPackageResetHelper.Receiver();
        android.content.IntentFilter filter = new android.content.IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_CHANGED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addAction("android.intent.action.PACKAGE_RESTARTED");
        filter.addAction("android.intent.action.QUERY_PACKAGE_RESTART");
        filter.addDataScheme("package");
        filter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@PACKAGE=ENTIRE_PKG_CHANGED");
        this.mContext.registerReceiver(this.mReceiver, filter);
    }

    @Override // com.android.server.location.injector.PackageResetHelper
    protected void onUnregister() {
        com.android.internal.util.Preconditions.checkState(this.mReceiver != null);
        this.mContext.unregisterReceiver(this.mReceiver);
        this.mReceiver = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    class Receiver extends android.content.BroadcastReceiver {
        private Receiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            android.net.Uri data;
            final java.lang.String packageName;
            byte b;
            java.lang.String action = intent.getAction();
            if (action == null || (data = intent.getData()) == null || (packageName = data.getSchemeSpecificPart()) == null) {
                return;
            }
            int i = 0;
            switch (action.hashCode()) {
                case -1072806502:
                    b = !action.equals("android.intent.action.QUERY_PACKAGE_RESTART") ? (byte) -1 : (byte) 0;
                    break;
                case -757780528:
                    b = !action.equals("android.intent.action.PACKAGE_RESTARTED") ? (byte) -1 : (byte) 3;
                    break;
                case 172491798:
                    b = !action.equals("android.intent.action.PACKAGE_CHANGED") ? (byte) -1 : (byte) 1;
                    break;
                case 525384130:
                    b = !action.equals("android.intent.action.PACKAGE_REMOVED") ? (byte) -1 : (byte) 2;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    java.lang.String[] packages = intent.getStringArrayExtra("android.intent.extra.PACKAGES");
                    if (packages != null) {
                        int length = packages.length;
                        while (i < length) {
                            java.lang.String pkg = packages[i];
                            if (!com.android.server.location.injector.SystemPackageResetHelper.this.queryResetableForPackage(pkg)) {
                                i++;
                            } else {
                                setResultCode(-1);
                            }
                        }
                    }
                    break;
                case 1:
                    boolean isPackageChange = false;
                    java.lang.String[] components = intent.getStringArrayExtra("android.intent.extra.changed_component_name_list");
                    if (components != null) {
                        int length2 = components.length;
                        while (true) {
                            if (i < length2) {
                                java.lang.String component = components[i];
                                if (!packageName.equals(component)) {
                                    i++;
                                } else {
                                    isPackageChange = true;
                                }
                            }
                        }
                    }
                    if (isPackageChange) {
                        try {
                            android.content.pm.ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, android.content.pm.PackageManager.ApplicationInfoFlags.of(0L));
                            if (!appInfo.enabled) {
                                com.android.server.FgThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.location.injector.SystemPackageResetHelper$Receiver$$ExternalSyntheticLambda0
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        this.f$0.lambda$onReceive$0(packageName);
                                    }
                                });
                            }
                        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                            return;
                        }
                    }
                    break;
                case 2:
                case 3:
                    com.android.server.FgThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.location.injector.SystemPackageResetHelper$Receiver$$ExternalSyntheticLambda1
                        @Override // java.lang.Runnable
                        public final void run() {
                            this.f$0.lambda$onReceive$1(packageName);
                        }
                    });
                    break;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0(java.lang.String packageName) {
            com.android.server.location.injector.SystemPackageResetHelper.this.notifyPackageReset(packageName);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$1(java.lang.String packageName) {
            com.android.server.location.injector.SystemPackageResetHelper.this.notifyPackageReset(packageName);
        }
    }
}
