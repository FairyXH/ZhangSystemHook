package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
class UsbUserPermissionManager {
    private static final boolean DEBUG = false;
    private static final int SNET_EVENT_LOG_ID = 1397638484;
    private static final java.lang.String TAG = com.android.server.usb.UsbUserPermissionManager.class.getSimpleName();
    private final android.content.Context mContext;
    private final boolean mDisablePermissionDialogs;
    private boolean mIsCopyPermissionsScheduled;
    private final android.util.AtomicFile mPermissionsFile;
    private final com.android.server.usb.UsbUserSettingsManager mUsbUserSettingsManager;
    private final android.os.UserHandle mUser;
    private final android.util.ArrayMap<java.lang.String, android.util.SparseBooleanArray> mDevicePermissionMap = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<android.hardware.usb.UsbAccessory, android.util.SparseBooleanArray> mAccessoryPermissionMap = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<android.hardware.usb.DeviceFilter, android.util.SparseBooleanArray> mDevicePersistentPermissionMap = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<android.hardware.usb.AccessoryFilter, android.util.SparseBooleanArray> mAccessoryPersistentPermissionMap = new android.util.ArrayMap<>();
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.hardware.SensorPrivacyManagerInternal mSensorPrivacyMgrInternal = (android.hardware.SensorPrivacyManagerInternal) com.android.server.LocalServices.getService(android.hardware.SensorPrivacyManagerInternal.class);

    UsbUserPermissionManager(android.content.Context context, com.android.server.usb.UsbUserSettingsManager usbUserSettingsManager) {
        this.mContext = context;
        this.mUser = context.getUser();
        this.mUsbUserSettingsManager = usbUserSettingsManager;
        this.mDisablePermissionDialogs = context.getResources().getBoolean(android.R.bool.config_disableTransitionAnimation);
        this.mPermissionsFile = new android.util.AtomicFile(new java.io.File(android.os.Environment.getUserSystemDirectory(this.mUser.getIdentifier()), "usb_permissions.xml"), "usb-permissions");
        synchronized (this.mLock) {
            readPermissionsLocked();
        }
    }

    void removeAccessoryPermissions(android.hardware.usb.UsbAccessory accessory) {
        synchronized (this.mLock) {
            this.mAccessoryPermissionMap.remove(accessory);
        }
    }

    void removeDevicePermissions(android.hardware.usb.UsbDevice device) {
        synchronized (this.mLock) {
            this.mDevicePermissionMap.remove(device.getDeviceName());
        }
    }

    void grantDevicePermission(android.hardware.usb.UsbDevice device, int uid) {
        synchronized (this.mLock) {
            java.lang.String deviceName = device.getDeviceName();
            android.util.SparseBooleanArray uidList = this.mDevicePermissionMap.get(deviceName);
            if (uidList == null) {
                uidList = new android.util.SparseBooleanArray(1);
                this.mDevicePermissionMap.put(deviceName, uidList);
            }
            uidList.put(uid, true);
        }
    }

    void grantAccessoryPermission(android.hardware.usb.UsbAccessory accessory, int uid) {
        synchronized (this.mLock) {
            android.util.SparseBooleanArray uidList = this.mAccessoryPermissionMap.get(accessory);
            if (uidList == null) {
                uidList = new android.util.SparseBooleanArray(1);
                this.mAccessoryPermissionMap.put(accessory, uidList);
            }
            uidList.put(uid, true);
        }
    }

    boolean hasPermission(android.hardware.usb.UsbDevice device, java.lang.String packageName, int pid, int uid) {
        int idx;
        if (device.getHasVideoCapture()) {
            boolean isCameraPrivacyEnabled = this.mSensorPrivacyMgrInternal.isSensorPrivacyEnabled(android.os.UserHandle.getUserId(uid), 2);
            if (isCameraPrivacyEnabled || !isCameraPermissionGranted(packageName, pid, uid)) {
                return false;
            }
        }
        boolean isCameraPrivacyEnabled2 = device.getHasAudioCapture();
        if (isCameraPrivacyEnabled2 && this.mSensorPrivacyMgrInternal.isSensorPrivacyEnabled(android.os.UserHandle.getUserId(uid), 1)) {
            return false;
        }
        synchronized (this.mLock) {
            if (uid != 1000) {
                if (!this.mDisablePermissionDialogs) {
                    android.hardware.usb.DeviceFilter filter = new android.hardware.usb.DeviceFilter(device);
                    android.util.SparseBooleanArray permissionsForDevice = this.mDevicePersistentPermissionMap.get(filter);
                    if (permissionsForDevice != null && (idx = permissionsForDevice.indexOfKey(uid)) >= 0) {
                        return permissionsForDevice.valueAt(idx);
                    }
                    android.util.SparseBooleanArray uidList = this.mDevicePermissionMap.get(device.getDeviceName());
                    if (uidList == null) {
                        return false;
                    }
                    return uidList.get(uid);
                }
            }
            return true;
        }
    }

    boolean hasPermission(android.hardware.usb.UsbAccessory accessory, int pid, int uid) {
        int idx;
        synchronized (this.mLock) {
            if (uid != 1000) {
                if (!this.mDisablePermissionDialogs && this.mContext.checkPermission("android.permission.MANAGE_USB", pid, uid) != 0) {
                    android.hardware.usb.AccessoryFilter filter = new android.hardware.usb.AccessoryFilter(accessory);
                    android.util.SparseBooleanArray permissionsForAccessory = this.mAccessoryPersistentPermissionMap.get(filter);
                    if (permissionsForAccessory != null && (idx = permissionsForAccessory.indexOfKey(uid)) >= 0) {
                        return permissionsForAccessory.valueAt(idx);
                    }
                    android.util.SparseBooleanArray uidList = this.mAccessoryPermissionMap.get(accessory);
                    if (uidList == null) {
                        return false;
                    }
                    return uidList.get(uid);
                }
            }
            return true;
        }
    }

    void setDevicePersistentPermission(android.hardware.usb.UsbDevice device, int uid, boolean isGranted) {
        boolean isChanged;
        android.hardware.usb.DeviceFilter filter = new android.hardware.usb.DeviceFilter(device);
        synchronized (this.mLock) {
            android.util.SparseBooleanArray permissionsForDevice = this.mDevicePersistentPermissionMap.get(filter);
            if (permissionsForDevice == null) {
                permissionsForDevice = new android.util.SparseBooleanArray();
                this.mDevicePersistentPermissionMap.put(filter, permissionsForDevice);
            }
            int idx = permissionsForDevice.indexOfKey(uid);
            if (idx >= 0) {
                isChanged = permissionsForDevice.valueAt(idx) != isGranted;
                permissionsForDevice.setValueAt(idx, isGranted);
            } else {
                isChanged = true;
                permissionsForDevice.put(uid, isGranted);
            }
            if (isChanged) {
                scheduleWritePermissionsLocked();
            }
        }
    }

    void setAccessoryPersistentPermission(android.hardware.usb.UsbAccessory accessory, int uid, boolean isGranted) {
        boolean isChanged;
        android.hardware.usb.AccessoryFilter filter = new android.hardware.usb.AccessoryFilter(accessory);
        synchronized (this.mLock) {
            android.util.SparseBooleanArray permissionsForAccessory = this.mAccessoryPersistentPermissionMap.get(filter);
            if (permissionsForAccessory == null) {
                permissionsForAccessory = new android.util.SparseBooleanArray();
                this.mAccessoryPersistentPermissionMap.put(filter, permissionsForAccessory);
            }
            int idx = permissionsForAccessory.indexOfKey(uid);
            if (idx >= 0) {
                isChanged = permissionsForAccessory.valueAt(idx) != isGranted;
                permissionsForAccessory.setValueAt(idx, isGranted);
            } else {
                isChanged = true;
                permissionsForAccessory.put(uid, isGranted);
            }
            if (isChanged) {
                scheduleWritePermissionsLocked();
            }
        }
    }

    private void readPermission(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        try {
            int uid = com.android.internal.util.XmlUtils.readIntAttribute(parser, "uid");
            java.lang.String isGrantedString = parser.getAttributeValue(null, "granted");
            if (isGrantedString == null || (!isGrantedString.equals(java.lang.Boolean.TRUE.toString()) && !isGrantedString.equals(java.lang.Boolean.FALSE.toString()))) {
                android.util.Slog.e(TAG, "error reading usb permission granted state");
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
                return;
            }
            boolean isGranted = isGrantedString.equals(java.lang.Boolean.TRUE.toString());
            com.android.internal.util.XmlUtils.nextElement(parser);
            if ("usb-device".equals(parser.getName())) {
                android.hardware.usb.DeviceFilter filter = android.hardware.usb.DeviceFilter.read(parser);
                int idx = this.mDevicePersistentPermissionMap.indexOfKey(filter);
                if (idx >= 0) {
                    this.mDevicePersistentPermissionMap.valueAt(idx).put(uid, isGranted);
                    return;
                }
                android.util.SparseBooleanArray permissionsForDevice = new android.util.SparseBooleanArray();
                this.mDevicePersistentPermissionMap.put(filter, permissionsForDevice);
                permissionsForDevice.put(uid, isGranted);
                return;
            }
            if ("usb-accessory".equals(parser.getName())) {
                android.hardware.usb.AccessoryFilter filter2 = android.hardware.usb.AccessoryFilter.read(parser);
                int idx2 = this.mAccessoryPersistentPermissionMap.indexOfKey(filter2);
                if (idx2 >= 0) {
                    this.mAccessoryPersistentPermissionMap.valueAt(idx2).put(uid, isGranted);
                    return;
                }
                android.util.SparseBooleanArray permissionsForAccessory = new android.util.SparseBooleanArray();
                this.mAccessoryPersistentPermissionMap.put(filter2, permissionsForAccessory);
                permissionsForAccessory.put(uid, isGranted);
            }
        } catch (java.lang.NumberFormatException e) {
            android.util.Slog.e(TAG, "error reading usb permission uid", e);
            com.android.internal.util.XmlUtils.skipCurrentTag(parser);
        }
    }

    private void readPermissionsLocked() {
        this.mDevicePersistentPermissionMap.clear();
        this.mAccessoryPersistentPermissionMap.clear();
        try {
            java.io.FileInputStream in = this.mPermissionsFile.openRead();
            try {
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(in);
                com.android.internal.util.XmlUtils.nextElement(parser);
                while (parser.getEventType() != 1) {
                    java.lang.String tagName = parser.getName();
                    if (com.android.server.permission.access.PermissionUri.SCHEME.equals(tagName)) {
                        readPermission(parser);
                    } else {
                        com.android.internal.util.XmlUtils.nextElement(parser);
                    }
                }
                if (in != null) {
                    in.close();
                }
            } catch (java.lang.Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        } catch (java.io.FileNotFoundException e) {
        } catch (java.lang.Exception e2) {
            android.util.Slog.e(TAG, "error reading usb permissions file, deleting to start fresh", e2);
            this.mPermissionsFile.delete();
        }
    }

    private void scheduleWritePermissionsLocked() {
        if (this.mIsCopyPermissionsScheduled) {
            return;
        }
        this.mIsCopyPermissionsScheduled = true;
        android.os.AsyncTask.execute(new java.lang.Runnable() { // from class: com.android.server.usb.UsbUserPermissionManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() throws java.lang.Throwable {
                this.f$0.lambda$scheduleWritePermissionsLocked$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0184 A[Catch: all -> 0x018b, TryCatch #4 {all -> 0x018b, blocks: (B:29:0x00e5, B:49:0x017b, B:51:0x0184, B:52:0x0189, B:55:0x018c, B:34:0x0113, B:37:0x0122, B:39:0x0128, B:40:0x015b, B:41:0x015e), top: B:60:0x00ab }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public /* synthetic */ void lambda$scheduleWritePermissionsLocked$0() throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 401
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.usb.UsbUserPermissionManager.lambda$scheduleWritePermissionsLocked$0():void");
    }

    void requestPermissionDialog(android.hardware.usb.UsbDevice device, android.hardware.usb.UsbAccessory accessory, boolean canBeDefault, java.lang.String packageName, int uid, android.content.Context userContext, android.app.PendingIntent pi) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            try {
                android.content.Intent intent = new android.content.Intent();
                if (device != null) {
                    intent.putExtra("device", device);
                } else {
                    intent.putExtra("accessory", accessory);
                }
                intent.putExtra("android.intent.extra.INTENT", pi);
                intent.putExtra("android.intent.extra.UID", uid);
                intent.putExtra("android.hardware.usb.extra.CAN_BE_DEFAULT", canBeDefault);
                intent.putExtra("android.hardware.usb.extra.PACKAGE", packageName);
                intent.setComponent(android.content.ComponentName.unflattenFromString(userContext.getResources().getString(android.R.string.config_wallpaperCropperPackage)));
                intent.addFlags(268435456);
                userContext.startActivityAsUser(intent, this.mUser);
            } catch (android.content.ActivityNotFoundException e) {
                android.util.Slog.e(TAG, "unable to start UsbPermissionActivity");
            }
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:46:0x01f7
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    void dump(com.android.internal.util.dump.DualDumpOutputStream r23, java.lang.String r24, long r25) {
        /*
            Method dump skipped, instruction units count: 506
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.usb.UsbUserPermissionManager.dump(com.android.internal.util.dump.DualDumpOutputStream, java.lang.String, long):void");
    }

    private boolean isCameraPermissionGranted(java.lang.String packageName, int pid, int uid) {
        try {
            android.content.pm.ApplicationInfo aInfo = this.mContext.getPackageManager().getApplicationInfo(packageName, 0);
            if (aInfo.uid != uid) {
                android.util.Slog.i(TAG, "Package " + packageName + " does not match caller's uid " + uid);
                return false;
            }
            int targetSdkVersion = aInfo.targetSdkVersion;
            if (targetSdkVersion >= 28) {
                int allowed = this.mContext.checkPermission("android.permission.CAMERA", pid, uid);
                if (-1 == allowed) {
                    android.util.Slog.i(TAG, "Camera permission required for USB video class devices");
                    return false;
                }
                return true;
            }
            return true;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.i(TAG, "Package not found, likely due to invalid package name!");
            return false;
        }
    }

    public void checkPermission(android.hardware.usb.UsbDevice device, java.lang.String packageName, int pid, int uid) {
        if (!hasPermission(device, packageName, pid, uid)) {
            throw new java.lang.SecurityException("User has not given " + uid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + packageName + " permission to access device " + device.getDeviceName());
        }
    }

    public void checkPermission(android.hardware.usb.UsbAccessory accessory, int pid, int uid) {
        if (!hasPermission(accessory, pid, uid)) {
            throw new java.lang.SecurityException("User has not given " + uid + " permission to accessory " + accessory);
        }
    }

    private void requestPermissionDialog(android.hardware.usb.UsbDevice device, android.hardware.usb.UsbAccessory accessory, boolean canBeDefault, java.lang.String packageName, android.app.PendingIntent pi, int uid) {
        boolean throwException = false;
        try {
            android.content.pm.ApplicationInfo aInfo = this.mContext.getPackageManager().getApplicationInfo(packageName, 0);
            if (aInfo.uid != uid) {
                android.util.Slog.w(TAG, "package " + packageName + " does not match caller's uid " + uid);
                android.util.EventLog.writeEvent(SNET_EVENT_LOG_ID, "180104273", -1, "");
                throwException = true;
            }
            if (throwException) {
                throw new java.lang.IllegalArgumentException("package " + packageName + " not found");
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throwException = true;
            if (1 != 0) {
                throw new java.lang.IllegalArgumentException("package " + packageName + " not found");
            }
        } catch (java.lang.Throwable th) {
            if (0 != 0) {
                throw new java.lang.IllegalArgumentException("package " + packageName + " not found");
            }
            throw th;
        }
        requestPermissionDialog(device, accessory, canBeDefault, packageName, uid, this.mContext, pi);
    }

    public void requestPermission(android.hardware.usb.UsbDevice device, java.lang.String packageName, android.app.PendingIntent pi, int pid, int uid) {
        android.content.Intent intent = new android.content.Intent();
        if (hasPermission(device, packageName, pid, uid)) {
            intent.putExtra("device", device);
            intent.putExtra(com.android.server.permission.access.PermissionUri.SCHEME, true);
            try {
                pi.send(this.mContext, 0, intent);
                return;
            } catch (android.app.PendingIntent.CanceledException e) {
                return;
            }
        }
        if (device.getHasVideoCapture() && !isCameraPermissionGranted(packageName, pid, uid)) {
            intent.putExtra("device", device);
            intent.putExtra(com.android.server.permission.access.PermissionUri.SCHEME, false);
            try {
                pi.send(this.mContext, 0, intent);
                return;
            } catch (android.app.PendingIntent.CanceledException e2) {
                return;
            }
        }
        requestPermissionDialog(device, null, this.mUsbUserSettingsManager.canBeDefault(device, packageName), packageName, pi, uid);
    }

    public void requestPermission(android.hardware.usb.UsbAccessory accessory, java.lang.String packageName, android.app.PendingIntent pi, int pid, int uid) {
        if (hasPermission(accessory, pid, uid)) {
            android.content.Intent intent = new android.content.Intent();
            intent.putExtra("accessory", accessory);
            intent.putExtra(com.android.server.permission.access.PermissionUri.SCHEME, true);
            try {
                pi.send(this.mContext, 0, intent);
                return;
            } catch (android.app.PendingIntent.CanceledException e) {
                return;
            }
        }
        requestPermissionDialog(null, accessory, this.mUsbUserSettingsManager.canBeDefault(accessory, packageName), packageName, pi, uid);
    }
}
