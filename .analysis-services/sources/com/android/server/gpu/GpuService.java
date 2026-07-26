package com.android.server.gpu;

/* JADX INFO: loaded from: classes2.dex */
public class GpuService extends com.android.server.SystemService {
    private static final int BASE64_FLAGS = 3;
    public static final boolean DEBUG = false;
    private static final java.lang.String DEV_DRIVER_PROPERTY = "ro.gfx.driver.1";
    private static final java.lang.String PROD_DRIVER_PROPERTY = "ro.gfx.driver.0";
    public static final java.lang.String TAG = "GpuService";
    private static final java.lang.String UPDATABLE_DRIVER_PRODUCTION_ALLOWLIST_FILENAME = "allowlist.txt";
    private android.content.ContentResolver mContentResolver;
    private final android.content.Context mContext;
    private android.updatabledriver.UpdatableDriverProto.Denylists mDenylists;
    private final java.lang.String mDevDriverPackageName;
    private com.android.server.gpu.GpuService.DeviceConfigListener mDeviceConfigListener;
    private final java.lang.Object mDeviceConfigLock;
    private com.android.server.gpu.IGpuServiceExt mGPSExt;
    private final boolean mHasDevDriver;
    private final boolean mHasProdDriver;
    private final java.lang.Object mLock;
    private final android.content.pm.PackageManager mPackageManager;
    private final java.lang.String mProdDriverPackageName;
    private long mProdDriverVersionCode;
    private com.android.server.gpu.GpuService.SettingsObserver mSettingsObserver;

    private static native void nSetUpdatableDriverPath(java.lang.String str);

    public GpuService(android.content.Context context) {
        super(context);
        this.mLock = new java.lang.Object();
        this.mDeviceConfigLock = new java.lang.Object();
        this.mGPSExt = (com.android.server.gpu.IGpuServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.gpu.IGpuServiceExt.class).base(this).create();
        this.mContext = context;
        this.mProdDriverPackageName = android.os.SystemProperties.get(PROD_DRIVER_PROPERTY);
        this.mProdDriverVersionCode = -1L;
        this.mDevDriverPackageName = android.os.SystemProperties.get(DEV_DRIVER_PROPERTY);
        this.mPackageManager = context.getPackageManager();
        this.mHasProdDriver = !android.text.TextUtils.isEmpty(this.mProdDriverPackageName);
        this.mHasDevDriver = !android.text.TextUtils.isEmpty(this.mDevDriverPackageName);
        if (this.mHasDevDriver || this.mHasProdDriver) {
            android.content.IntentFilter packageFilter = new android.content.IntentFilter();
            packageFilter.addAction("android.intent.action.PACKAGE_ADDED");
            packageFilter.addAction("android.intent.action.PACKAGE_CHANGED");
            packageFilter.addCategory("oplusBrEx@android.intent.action.PACKAGE_CHANGED@PACKAGE=IGNORE_WM_COMP");
            packageFilter.addAction("android.intent.action.PACKAGE_REMOVED");
            packageFilter.addDataScheme("package");
            getContext().registerReceiverAsUser(new com.android.server.gpu.GpuService.PackageReceiver(), android.os.UserHandle.ALL, packageFilter, null, null);
        }
        this.mGPSExt.init(context);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
    }

    @Override // com.android.server.SystemService
    public void onBootPhase(int phase) {
        if (phase == 1000) {
            this.mContentResolver = this.mContext.getContentResolver();
            if (!this.mHasProdDriver && !this.mHasDevDriver) {
                return;
            }
            this.mSettingsObserver = new com.android.server.gpu.GpuService.SettingsObserver();
            this.mDeviceConfigListener = new com.android.server.gpu.GpuService.DeviceConfigListener();
            fetchProductionDriverPackageProperties();
            processDenylists();
            setDenylist();
            fetchPrereleaseDriverPackageProperties();
        }
    }

    private final class SettingsObserver extends android.database.ContentObserver {
        private final android.net.Uri mProdDriverDenylistsUri;

        SettingsObserver() {
            super(new android.os.Handler());
            this.mProdDriverDenylistsUri = android.provider.Settings.Global.getUriFor("updatable_driver_production_denylists");
            com.android.server.gpu.GpuService.this.mContentResolver.registerContentObserver(this.mProdDriverDenylistsUri, false, this, -1);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange, android.net.Uri uri) {
            if (uri != null && this.mProdDriverDenylistsUri.equals(uri)) {
                com.android.server.gpu.GpuService.this.processDenylists();
                com.android.server.gpu.GpuService.this.setDenylist();
            }
        }
    }

    private final class DeviceConfigListener implements android.provider.DeviceConfig.OnPropertiesChangedListener {
        DeviceConfigListener() {
            android.provider.DeviceConfig.addOnPropertiesChangedListener("game_driver", com.android.server.gpu.GpuService.this.mContext.getMainExecutor(), this);
        }

        public void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            synchronized (com.android.server.gpu.GpuService.this.mDeviceConfigLock) {
                if (properties.getKeyset().contains("updatable_driver_production_denylists")) {
                    com.android.server.gpu.GpuService.this.parseDenylists(properties.getString("updatable_driver_production_denylists", ""));
                    com.android.server.gpu.GpuService.this.setDenylist();
                }
            }
        }
    }

    private final class PackageReceiver extends android.content.BroadcastReceiver {
        private PackageReceiver() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:18:0x004c  */
        @Override // android.content.BroadcastReceiver
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public void onReceive(android.content.Context r7, android.content.Intent r8) {
            /*
                r6 = this;
                android.net.Uri r0 = r8.getData()
                java.lang.String r1 = r0.getSchemeSpecificPart()
                com.android.server.gpu.GpuService r2 = com.android.server.gpu.GpuService.this
                java.lang.String r2 = com.android.server.gpu.GpuService.m4003$$Nest$fgetmProdDriverPackageName(r2)
                boolean r2 = r1.equals(r2)
                com.android.server.gpu.GpuService r3 = com.android.server.gpu.GpuService.this
                java.lang.String r3 = com.android.server.gpu.GpuService.m4001$$Nest$fgetmDevDriverPackageName(r3)
                boolean r3 = r1.equals(r3)
                if (r2 != 0) goto L22
                if (r3 != 0) goto L22
                return
            L22:
                java.lang.String r4 = r8.getAction()
                int r5 = r4.hashCode()
                switch(r5) {
                    case 172491798: goto L42;
                    case 525384130: goto L38;
                    case 1544582882: goto L2e;
                    default: goto L2d;
                }
            L2d:
                goto L4c
            L2e:
                java.lang.String r5 = "android.intent.action.PACKAGE_ADDED"
                boolean r4 = r4.equals(r5)
                if (r4 == 0) goto L2d
                r4 = 0
                goto L4d
            L38:
                java.lang.String r5 = "android.intent.action.PACKAGE_REMOVED"
                boolean r4 = r4.equals(r5)
                if (r4 == 0) goto L2d
                r4 = 2
                goto L4d
            L42:
                java.lang.String r5 = "android.intent.action.PACKAGE_CHANGED"
                boolean r4 = r4.equals(r5)
                if (r4 == 0) goto L2d
                r4 = 1
                goto L4d
            L4c:
                r4 = -1
            L4d:
                switch(r4) {
                    case 0: goto L51;
                    case 1: goto L51;
                    case 2: goto L51;
                    default: goto L50;
                }
            L50:
                goto L65
            L51:
                if (r2 == 0) goto L5e
                com.android.server.gpu.GpuService r4 = com.android.server.gpu.GpuService.this
                com.android.server.gpu.GpuService.m4005$$Nest$mfetchProductionDriverPackageProperties(r4)
                com.android.server.gpu.GpuService r4 = com.android.server.gpu.GpuService.this
                com.android.server.gpu.GpuService.m4008$$Nest$msetDenylist(r4)
                goto L65
            L5e:
                if (r3 == 0) goto L65
                com.android.server.gpu.GpuService r4 = com.android.server.gpu.GpuService.this
                com.android.server.gpu.GpuService.m4004$$Nest$mfetchPrereleaseDriverPackageProperties(r4)
            L65:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.gpu.GpuService.PackageReceiver.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    private static void assetToSettingsGlobal(android.content.Context context, android.content.Context driverContext, java.lang.String fileName, java.lang.String settingsGlobal, java.lang.CharSequence delimiter) {
        try {
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(driverContext.getAssets().open(fileName)));
            java.util.ArrayList<java.lang.String> assetStrings = new java.util.ArrayList<>();
            while (true) {
                java.lang.String assetString = reader.readLine();
                if (assetString != null) {
                    assetStrings.add(assetString);
                } else {
                    android.provider.Settings.Global.putString(context.getContentResolver(), settingsGlobal, java.lang.String.join(delimiter, assetStrings));
                    return;
                }
            }
        } catch (java.io.IOException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchProductionDriverPackageProperties() {
        try {
            android.content.pm.ApplicationInfo driverInfo = this.mPackageManager.getApplicationInfo(this.mProdDriverPackageName, 1048576);
            if (driverInfo.targetSdkVersion < 26) {
                return;
            }
            android.provider.Settings.Global.putString(this.mContentResolver, "updatable_driver_production_allowlist", "");
            this.mProdDriverVersionCode = driverInfo.longVersionCode;
            try {
                android.content.Context driverContext = this.mContext.createPackageContext(this.mProdDriverPackageName, 4);
                assetToSettingsGlobal(this.mContext, driverContext, UPDATABLE_DRIVER_PRODUCTION_ALLOWLIST_FILENAME, "updatable_driver_production_allowlist", ",");
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDenylists() {
        java.lang.String base64String = android.provider.DeviceConfig.getProperty("game_driver", "updatable_driver_production_denylists");
        if (base64String == null) {
            base64String = android.provider.Settings.Global.getString(this.mContentResolver, "updatable_driver_production_denylists");
        }
        parseDenylists(base64String != null ? base64String : "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parseDenylists(java.lang.String base64String) {
        synchronized (this.mLock) {
            this.mDenylists = null;
            try {
                this.mDenylists = android.updatabledriver.UpdatableDriverProto.Denylists.parseFrom(android.util.Base64.decode(base64String, 3));
            } catch (java.lang.IllegalArgumentException e) {
            } catch (com.android.framework.protobuf.InvalidProtocolBufferException e2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDenylist() {
        android.provider.Settings.Global.putString(this.mContentResolver, "updatable_driver_production_denylist", "");
        synchronized (this.mLock) {
            if (this.mDenylists == null) {
                return;
            }
            java.util.List<android.updatabledriver.UpdatableDriverProto.Denylist> denylists = this.mDenylists.getDenylistsList();
            for (android.updatabledriver.UpdatableDriverProto.Denylist denylist : denylists) {
                if (denylist.getVersionCode() == this.mProdDriverVersionCode) {
                    android.provider.Settings.Global.putString(this.mContentResolver, "updatable_driver_production_denylist", java.lang.String.join(",", denylist.getPackageNamesList()));
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fetchPrereleaseDriverPackageProperties() {
        try {
            android.content.pm.ApplicationInfo driverInfo = this.mPackageManager.getApplicationInfo(this.mDevDriverPackageName, 1048576);
            if (driverInfo.targetSdkVersion < 26) {
                return;
            }
            setUpdatableDriverPath(driverInfo);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
        }
    }

    private void setUpdatableDriverPath(android.content.pm.ApplicationInfo ai) {
        if (ai.primaryCpuAbi == null) {
            nSetUpdatableDriverPath("");
            return;
        }
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append(ai.sourceDir).append("!/lib/");
        nSetUpdatableDriverPath(sb.toString());
    }
}
