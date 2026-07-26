package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public class UsbProfileGroupSettingsManager {
    private static final boolean DEBUG = false;
    private static final int DUMPSYS_LOG_BUFFER = 200;
    public static final java.lang.String PROPERTY_RESTRICT_USB_OVERLAY_ACTIVITIES = "android.app.PROPERTY_RESTRICT_USB_OVERLAY_ACTIVITIES";
    private static com.android.server.utils.EventLogger sEventLogger;
    private final android.app.ActivityManager mActivityManager;
    private final android.content.Context mContext;
    private final boolean mDisablePermissionDialogs;
    private boolean mIsWriteSettingsScheduled;
    private final com.android.server.usb.MtpNotificationManager mMtpNotificationManager;
    private final android.content.pm.PackageManager mPackageManager;
    private final android.os.UserHandle mParentUser;
    private final android.util.AtomicFile mSettingsFile;
    private final com.android.server.usb.UsbSettingsManager mSettingsManager;
    private final com.android.server.usb.UsbHandlerManager mUsbHandlerManager;
    private final android.os.UserManager mUserManager;
    private static final java.lang.String TAG = com.android.server.usb.UsbProfileGroupSettingsManager.class.getSimpleName();
    private static final java.io.File sSingleUserSettingsFile = new java.io.File("/data/system/usb_device_manager.xml");
    private final java.util.HashMap<android.hardware.usb.DeviceFilter, com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage> mDevicePreferenceMap = new java.util.HashMap<>();
    private final android.util.ArrayMap<android.hardware.usb.DeviceFilter, android.util.ArraySet<com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage>> mDevicePreferenceDeniedMap = new android.util.ArrayMap<>();
    private final java.util.HashMap<android.hardware.usb.AccessoryFilter, com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage> mAccessoryPreferenceMap = new java.util.HashMap<>();
    private final android.util.ArrayMap<android.hardware.usb.AccessoryFilter, android.util.ArraySet<com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage>> mAccessoryPreferenceDeniedMap = new android.util.ArrayMap<>();
    private final java.lang.Object mLock = new java.lang.Object();
    com.android.server.usb.IUsbProfileGroupSettingsManagerExt mUsbProfileGroupSettingsManagerExt = (com.android.server.usb.IUsbProfileGroupSettingsManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.usb.IUsbProfileGroupSettingsManagerExt.class).create();
    com.android.server.usb.UsbProfileGroupSettingsManager.MyPackageMonitor mPackageMonitor = new com.android.server.usb.UsbProfileGroupSettingsManager.MyPackageMonitor();

    private static class UserPackage {
        final java.lang.String packageName;
        final android.os.UserHandle user;

        private UserPackage(java.lang.String packageName, android.os.UserHandle user) {
            this.packageName = packageName;
            this.user = user;
        }

        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage)) {
                return false;
            }
            com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage other = (com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage) obj;
            return this.user.equals(other.user) && this.packageName.equals(other.packageName);
        }

        public int hashCode() {
            int result = this.user.hashCode();
            return (result * 31) + this.packageName.hashCode();
        }

        public java.lang.String toString() {
            return this.user.getIdentifier() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.packageName;
        }

        public void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
            long token = dump.start(idName, id);
            dump.write("user_id", 1120986464257L, this.user.getIdentifier());
            dump.write("package_name", 1138166333442L, this.packageName);
            dump.end(token);
        }
    }

    private class MyPackageMonitor extends com.android.internal.content.PackageMonitor {
        private MyPackageMonitor() {
        }

        public void onPackageAdded(java.lang.String packageName, int uid) {
            if (!com.android.server.usb.UsbProfileGroupSettingsManager.this.mUserManager.isSameProfileGroup(com.android.server.usb.UsbProfileGroupSettingsManager.this.mParentUser.getIdentifier(), android.os.UserHandle.getUserId(uid))) {
                return;
            }
            com.android.server.usb.UsbProfileGroupSettingsManager.this.handlePackageAdded(new com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage(packageName, android.os.UserHandle.getUserHandleForUid(uid)));
        }

        public void onPackageRemoved(java.lang.String packageName, int uid) {
            if (!com.android.server.usb.UsbProfileGroupSettingsManager.this.mUserManager.isSameProfileGroup(com.android.server.usb.UsbProfileGroupSettingsManager.this.mParentUser.getIdentifier(), android.os.UserHandle.getUserId(uid))) {
                return;
            }
            com.android.server.usb.UsbProfileGroupSettingsManager.this.clearDefaults(packageName, android.os.UserHandle.getUserHandleForUid(uid));
        }
    }

    public UsbProfileGroupSettingsManager(android.content.Context context, android.os.UserHandle user, com.android.server.usb.UsbSettingsManager settingsManager, com.android.server.usb.UsbHandlerManager usbResolveActivityManager) {
        try {
            android.content.Context parentUserContext = context.createPackageContextAsUser(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, 0, user);
            this.mContext = context;
            this.mPackageManager = context.getPackageManager();
            this.mActivityManager = (android.app.ActivityManager) context.getSystemService(android.app.ActivityManager.class);
            this.mSettingsManager = settingsManager;
            this.mUserManager = (android.os.UserManager) context.getSystemService("user");
            this.mParentUser = user;
            this.mSettingsFile = new android.util.AtomicFile(new java.io.File(android.os.Environment.getUserSystemDirectory(user.getIdentifier()), "usb_device_manager.xml"), "usb-state");
            this.mDisablePermissionDialogs = context.getResources().getBoolean(android.R.bool.config_disableTransitionAnimation);
            synchronized (this.mLock) {
                if (android.os.UserHandle.SYSTEM.equals(user)) {
                    upgradeSingleUserLocked();
                }
                readSettingsLocked();
            }
            this.mPackageMonitor.register(context, null, android.os.UserHandle.ALL, true);
            this.mMtpNotificationManager = new com.android.server.usb.MtpNotificationManager(parentUserContext, new com.android.server.usb.MtpNotificationManager.OnOpenInAppListener() { // from class: com.android.server.usb.UsbProfileGroupSettingsManager$$ExternalSyntheticLambda3
                @Override // com.android.server.usb.MtpNotificationManager.OnOpenInAppListener
                public final void onOpenInApp(android.hardware.usb.UsbDevice usbDevice) {
                    this.f$0.lambda$new$0(usbDevice);
                }
            });
            this.mUsbHandlerManager = usbResolveActivityManager;
            sEventLogger = new com.android.server.utils.EventLogger(200, "UsbProfileGroupSettingsManager activity");
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.RuntimeException("Missing android package");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.hardware.usb.UsbDevice device) {
        resolveActivity(createDeviceAttachedIntent(device), device, false);
    }

    public void unregisterReceivers() {
        this.mPackageMonitor.unregister();
        this.mMtpNotificationManager.unregister();
    }

    void removeUser(android.os.UserHandle userToRemove) {
        synchronized (this.mLock) {
            boolean needToPersist = false;
            java.util.Iterator<java.util.Map.Entry<android.hardware.usb.DeviceFilter, com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage>> devicePreferenceIt = this.mDevicePreferenceMap.entrySet().iterator();
            while (devicePreferenceIt.hasNext()) {
                java.util.Map.Entry<android.hardware.usb.DeviceFilter, com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage> entry = devicePreferenceIt.next();
                if (entry.getValue().user.equals(userToRemove)) {
                    devicePreferenceIt.remove();
                    needToPersist = true;
                }
            }
            java.util.Iterator<java.util.Map.Entry<android.hardware.usb.AccessoryFilter, com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage>> accessoryPreferenceIt = this.mAccessoryPreferenceMap.entrySet().iterator();
            while (accessoryPreferenceIt.hasNext()) {
                java.util.Map.Entry<android.hardware.usb.AccessoryFilter, com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage> entry2 = accessoryPreferenceIt.next();
                if (entry2.getValue().user.equals(userToRemove)) {
                    accessoryPreferenceIt.remove();
                    needToPersist = true;
                }
            }
            int numEntries = this.mDevicePreferenceDeniedMap.size();
            for (int i = 0; i < numEntries; i++) {
                android.util.ArraySet<com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage> userPackages = this.mDevicePreferenceDeniedMap.valueAt(i);
                for (int j = userPackages.size() - 1; j >= 0; j--) {
                    if (userPackages.valueAt(j).user.equals(userToRemove)) {
                        userPackages.removeAt(j);
                        needToPersist = true;
                    }
                }
            }
            int numEntries2 = this.mAccessoryPreferenceDeniedMap.size();
            for (int i2 = 0; i2 < numEntries2; i2++) {
                android.util.ArraySet<com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage> userPackages2 = this.mAccessoryPreferenceDeniedMap.valueAt(i2);
                for (int j2 = userPackages2.size() - 1; j2 >= 0; j2--) {
                    if (userPackages2.valueAt(j2).user.equals(userToRemove)) {
                        userPackages2.removeAt(j2);
                        needToPersist = true;
                    }
                }
            }
            if (needToPersist) {
                scheduleWriteSettingsLocked();
            }
        }
    }

    private void readPreference(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        java.lang.String packageName = null;
        android.os.UserHandle user = this.mParentUser;
        int count = parser.getAttributeCount();
        for (int i = 0; i < count; i++) {
            if ("package".equals(parser.getAttributeName(i))) {
                packageName = parser.getAttributeValue(i);
            }
            if ("user".equals(parser.getAttributeName(i))) {
                user = this.mUserManager.getUserForSerialNumber(java.lang.Integer.parseInt(parser.getAttributeValue(i)));
            }
        }
        com.android.internal.util.XmlUtils.nextElement(parser);
        if ("usb-device".equals(parser.getName())) {
            android.hardware.usb.DeviceFilter filter = android.hardware.usb.DeviceFilter.read(parser);
            if (user != null) {
                this.mDevicePreferenceMap.put(filter, new com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage(packageName, user));
            }
        } else if ("usb-accessory".equals(parser.getName())) {
            android.hardware.usb.AccessoryFilter filter2 = android.hardware.usb.AccessoryFilter.read(parser);
            if (user != null) {
                this.mAccessoryPreferenceMap.put(filter2, new com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage(packageName, user));
            }
        }
        com.android.internal.util.XmlUtils.nextElement(parser);
    }

    private void readPreferenceDeniedList(org.xmlpull.v1.XmlPullParser parser) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        int outerDepth = parser.getDepth();
        if (!com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
            return;
        }
        if ("usb-device".equals(parser.getName())) {
            android.hardware.usb.DeviceFilter filter = android.hardware.usb.DeviceFilter.read(parser);
            while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
                if ("user-package".equals(parser.getName())) {
                    try {
                        int userId = com.android.internal.util.XmlUtils.readIntAttribute(parser, "user");
                        java.lang.String packageName = com.android.internal.util.XmlUtils.readStringAttribute(parser, "package");
                        if (packageName == null) {
                            android.util.Slog.e(TAG, "Unable to parse package name");
                        }
                        android.util.ArraySet<com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage> set = this.mDevicePreferenceDeniedMap.get(filter);
                        if (set == null) {
                            set = new android.util.ArraySet<>();
                            this.mDevicePreferenceDeniedMap.put(filter, set);
                        }
                        set.add(new com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage(packageName, android.os.UserHandle.of(userId)));
                    } catch (java.net.ProtocolException e) {
                        android.util.Slog.e(TAG, "Unable to parse user id", e);
                    }
                }
            }
        } else if ("usb-accessory".equals(parser.getName())) {
            android.hardware.usb.AccessoryFilter filter2 = android.hardware.usb.AccessoryFilter.read(parser);
            while (com.android.internal.util.XmlUtils.nextElementWithin(parser, outerDepth)) {
                if ("user-package".equals(parser.getName())) {
                    try {
                        int userId2 = com.android.internal.util.XmlUtils.readIntAttribute(parser, "user");
                        java.lang.String packageName2 = com.android.internal.util.XmlUtils.readStringAttribute(parser, "package");
                        if (packageName2 == null) {
                            android.util.Slog.e(TAG, "Unable to parse package name");
                        }
                        android.util.ArraySet<com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage> set2 = this.mAccessoryPreferenceDeniedMap.get(filter2);
                        if (set2 == null) {
                            set2 = new android.util.ArraySet<>();
                            this.mAccessoryPreferenceDeniedMap.put(filter2, set2);
                        }
                        set2.add(new com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage(packageName2, android.os.UserHandle.of(userId2)));
                    } catch (java.net.ProtocolException e2) {
                        android.util.Slog.e(TAG, "Unable to parse user id", e2);
                    }
                }
            }
        }
        while (parser.getDepth() > outerDepth) {
            parser.nextTag();
        }
    }

    private void upgradeSingleUserLocked() {
        if (sSingleUserSettingsFile.exists()) {
            this.mDevicePreferenceMap.clear();
            this.mAccessoryPreferenceMap.clear();
            java.io.FileInputStream fis = null;
            try {
                try {
                    fis = new java.io.FileInputStream(sSingleUserSettingsFile);
                    com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(fis);
                    com.android.internal.util.XmlUtils.nextElement(parser);
                    while (parser.getEventType() != 1) {
                        java.lang.String tagName = parser.getName();
                        if ("preference".equals(tagName)) {
                            readPreference(parser);
                        } else {
                            com.android.internal.util.XmlUtils.nextElement(parser);
                        }
                    }
                } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                    android.util.Log.wtf(TAG, "Failed to read single-user settings", e);
                }
                libcore.io.IoUtils.closeQuietly(fis);
                scheduleWriteSettingsLocked();
                sSingleUserSettingsFile.delete();
            } catch (java.lang.Throwable th) {
                libcore.io.IoUtils.closeQuietly(fis);
                throw th;
            }
        }
    }

    private void readSettingsLocked() {
        this.mDevicePreferenceMap.clear();
        this.mAccessoryPreferenceMap.clear();
        java.io.FileInputStream stream = null;
        try {
            try {
                stream = this.mSettingsFile.openRead();
                com.android.modules.utils.TypedXmlPullParser parser = android.util.Xml.resolvePullParser(stream);
                com.android.internal.util.XmlUtils.nextElement(parser);
                while (parser.getEventType() != 1) {
                    java.lang.String tagName = parser.getName();
                    if ("preference".equals(tagName)) {
                        readPreference(parser);
                    } else if ("preference-denied-list".equals(tagName)) {
                        readPreferenceDeniedList(parser);
                    } else {
                        com.android.internal.util.XmlUtils.nextElement(parser);
                    }
                }
            } catch (java.io.FileNotFoundException e) {
            } catch (java.lang.Exception e2) {
                android.util.Slog.e(TAG, "error reading settings file, deleting to start fresh", e2);
                this.mSettingsFile.delete();
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(stream);
        }
    }

    private void scheduleWriteSettingsLocked() {
        if (this.mIsWriteSettingsScheduled) {
            return;
        }
        this.mIsWriteSettingsScheduled = true;
        android.os.AsyncTask.execute(new java.lang.Runnable() { // from class: com.android.server.usb.UsbProfileGroupSettingsManager$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$scheduleWriteSettingsLocked$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scheduleWriteSettingsLocked$1() {
        synchronized (this.mLock) {
            java.io.FileOutputStream fos = null;
            try {
                fos = this.mSettingsFile.startWrite();
                com.android.modules.utils.TypedXmlSerializer serializer = android.util.Xml.resolveSerializer(fos);
                serializer.startDocument((java.lang.String) null, true);
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                serializer.startTag((java.lang.String) null, "settings");
                for (android.hardware.usb.DeviceFilter filter : this.mDevicePreferenceMap.keySet()) {
                    serializer.startTag((java.lang.String) null, "preference");
                    serializer.attribute((java.lang.String) null, "package", this.mDevicePreferenceMap.get(filter).packageName);
                    serializer.attribute((java.lang.String) null, "user", java.lang.String.valueOf(getSerial(this.mDevicePreferenceMap.get(filter).user)));
                    filter.write(serializer);
                    serializer.endTag((java.lang.String) null, "preference");
                }
                for (android.hardware.usb.AccessoryFilter filter2 : this.mAccessoryPreferenceMap.keySet()) {
                    serializer.startTag((java.lang.String) null, "preference");
                    serializer.attribute((java.lang.String) null, "package", this.mAccessoryPreferenceMap.get(filter2).packageName);
                    serializer.attribute((java.lang.String) null, "user", java.lang.String.valueOf(getSerial(this.mAccessoryPreferenceMap.get(filter2).user)));
                    filter2.write(serializer);
                    serializer.endTag((java.lang.String) null, "preference");
                }
                int numEntries = this.mDevicePreferenceDeniedMap.size();
                for (int i = 0; i < numEntries; i++) {
                    android.hardware.usb.DeviceFilter filter3 = this.mDevicePreferenceDeniedMap.keyAt(i);
                    android.util.ArraySet<com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage> userPackageSet = this.mDevicePreferenceDeniedMap.valueAt(i);
                    serializer.startTag((java.lang.String) null, "preference-denied-list");
                    filter3.write(serializer);
                    int numUserPackages = userPackageSet.size();
                    for (int j = 0; j < numUserPackages; j++) {
                        com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage = userPackageSet.valueAt(j);
                        serializer.startTag((java.lang.String) null, "user-package");
                        serializer.attribute((java.lang.String) null, "user", java.lang.String.valueOf(getSerial(userPackage.user)));
                        serializer.attribute((java.lang.String) null, "package", userPackage.packageName);
                        serializer.endTag((java.lang.String) null, "user-package");
                    }
                    serializer.endTag((java.lang.String) null, "preference-denied-list");
                }
                int numEntries2 = this.mAccessoryPreferenceDeniedMap.size();
                for (int i2 = 0; i2 < numEntries2; i2++) {
                    android.hardware.usb.AccessoryFilter filter4 = this.mAccessoryPreferenceDeniedMap.keyAt(i2);
                    android.util.ArraySet<com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage> userPackageSet2 = this.mAccessoryPreferenceDeniedMap.valueAt(i2);
                    serializer.startTag((java.lang.String) null, "preference-denied-list");
                    filter4.write(serializer);
                    int numUserPackages2 = userPackageSet2.size();
                    for (int j2 = 0; j2 < numUserPackages2; j2++) {
                        com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage2 = userPackageSet2.valueAt(j2);
                        serializer.startTag((java.lang.String) null, "user-package");
                        serializer.attribute((java.lang.String) null, "user", java.lang.String.valueOf(getSerial(userPackage2.user)));
                        serializer.attribute((java.lang.String) null, "package", userPackage2.packageName);
                        serializer.endTag((java.lang.String) null, "user-package");
                    }
                    serializer.endTag((java.lang.String) null, "preference-denied-list");
                }
                serializer.endTag((java.lang.String) null, "settings");
                serializer.endDocument();
                this.mSettingsFile.finishWrite(fos);
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Failed to write settings", e);
                if (fos != null) {
                    this.mSettingsFile.failWrite(fos);
                }
            }
            this.mIsWriteSettingsScheduled = false;
        }
    }

    static java.util.ArrayList<android.hardware.usb.DeviceFilter> getDeviceFilters(android.content.pm.PackageManager pm, android.content.pm.ResolveInfo info) {
        java.util.ArrayList<android.hardware.usb.DeviceFilter> filters = null;
        android.content.pm.ActivityInfo ai = info.activityInfo;
        android.content.res.XmlResourceParser parser = null;
        try {
            try {
                parser = ai.loadXmlMetaData(pm, "android.hardware.usb.action.USB_DEVICE_ATTACHED");
            } catch (java.lang.Exception e) {
                android.util.Slog.w(TAG, "Unable to load component info " + info.toString(), e);
                if (0 != 0) {
                }
            }
            if (parser == null) {
                android.util.Slog.w(TAG, "no meta-data for " + info);
            }
            com.android.internal.util.XmlUtils.nextElement(parser);
            while (parser.getEventType() != 1) {
                java.lang.String tagName = parser.getName();
                if ("usb-device".equals(tagName)) {
                    if (filters == null) {
                        filters = new java.util.ArrayList<>(1);
                    }
                    filters.add(android.hardware.usb.DeviceFilter.read(parser));
                }
                com.android.internal.util.XmlUtils.nextElement(parser);
            }
            if (parser != null) {
                parser.close();
            }
            return filters;
        } finally {
            if (0 != 0) {
                parser.close();
            }
        }
    }

    static java.util.ArrayList<android.hardware.usb.AccessoryFilter> getAccessoryFilters(android.content.pm.PackageManager pm, android.content.pm.ResolveInfo info) {
        java.util.ArrayList<android.hardware.usb.AccessoryFilter> filters = null;
        android.content.pm.ActivityInfo ai = info.activityInfo;
        android.content.res.XmlResourceParser parser = null;
        try {
            try {
                parser = ai.loadXmlMetaData(pm, "android.hardware.usb.action.USB_ACCESSORY_ATTACHED");
            } catch (java.lang.Exception e) {
                android.util.Slog.w(TAG, "Unable to load component info " + info.toString(), e);
                if (0 != 0) {
                }
            }
            if (parser == null) {
                android.util.Slog.w(TAG, "no meta-data for " + info);
            }
            com.android.internal.util.XmlUtils.nextElement(parser);
            while (parser.getEventType() != 1) {
                java.lang.String tagName = parser.getName();
                if ("usb-accessory".equals(tagName)) {
                    if (filters == null) {
                        filters = new java.util.ArrayList<>(1);
                    }
                    filters.add(android.hardware.usb.AccessoryFilter.read(parser));
                }
                com.android.internal.util.XmlUtils.nextElement(parser);
            }
            if (parser != null) {
                parser.close();
            }
            return filters;
        } finally {
            if (0 != 0) {
                parser.close();
            }
        }
    }

    private boolean packageMatchesLocked(android.content.pm.ResolveInfo info, android.hardware.usb.UsbDevice device, android.hardware.usb.UsbAccessory accessory) {
        java.util.ArrayList<android.hardware.usb.AccessoryFilter> accessoryFilters;
        java.util.ArrayList<android.hardware.usb.DeviceFilter> deviceFilters;
        if (isForwardMatch(info)) {
            return true;
        }
        if (device != null && (deviceFilters = getDeviceFilters(this.mPackageManager, info)) != null) {
            int numDeviceFilters = deviceFilters.size();
            for (int i = 0; i < numDeviceFilters; i++) {
                if (deviceFilters.get(i).matches(device)) {
                    return true;
                }
            }
        }
        if (accessory != null && (accessoryFilters = getAccessoryFilters(this.mPackageManager, info)) != null) {
            int numAccessoryFilters = accessoryFilters.size();
            for (int i2 = 0; i2 < numAccessoryFilters; i2++) {
                if (accessoryFilters.get(i2).matches(accessory)) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private java.util.ArrayList<android.content.pm.ResolveInfo> queryIntentActivitiesForAllProfiles(android.content.Intent intent) {
        java.util.List<android.content.pm.UserInfo> profiles = this.mUserManager.getEnabledProfiles(this.mParentUser.getIdentifier());
        java.util.ArrayList<android.content.pm.ResolveInfo> resolveInfos = new java.util.ArrayList<>();
        int numProfiles = profiles.size();
        for (int i = 0; i < numProfiles; i++) {
            resolveInfos.addAll(this.mSettingsManager.getSettingsForUser(profiles.get(i).id).queryIntentActivities(intent));
        }
        return resolveInfos;
    }

    private boolean isForwardMatch(android.content.pm.ResolveInfo match) {
        return match.getComponentInfo().name.equals(com.android.internal.app.IntentForwarderActivity.FORWARD_INTENT_TO_MANAGED_PROFILE);
    }

    private java.util.ArrayList<android.content.pm.ResolveInfo> preferHighPriority(java.util.ArrayList<android.content.pm.ResolveInfo> matches) {
        android.util.SparseArray<java.util.ArrayList<android.content.pm.ResolveInfo>> highestPriorityMatchesByUserId = new android.util.SparseArray<>();
        android.util.SparseIntArray highestPriorityByUserId = new android.util.SparseIntArray();
        java.util.ArrayList<android.content.pm.ResolveInfo> forwardMatches = new java.util.ArrayList<>();
        int numMatches = matches.size();
        for (int matchNum = 0; matchNum < numMatches; matchNum++) {
            android.content.pm.ResolveInfo match = matches.get(matchNum);
            if (isForwardMatch(match)) {
                forwardMatches.add(match);
            } else {
                if (highestPriorityByUserId.indexOfKey(match.targetUserId) < 0) {
                    highestPriorityByUserId.put(match.targetUserId, Integer.MIN_VALUE);
                    highestPriorityMatchesByUserId.put(match.targetUserId, new java.util.ArrayList<>());
                }
                int highestPriority = highestPriorityByUserId.get(match.targetUserId);
                java.util.ArrayList<android.content.pm.ResolveInfo> highestPriorityMatches = highestPriorityMatchesByUserId.get(match.targetUserId);
                if (match.priority == highestPriority) {
                    highestPriorityMatches.add(match);
                } else if (match.priority > highestPriority) {
                    highestPriorityByUserId.put(match.targetUserId, match.priority);
                    highestPriorityMatches.clear();
                    highestPriorityMatches.add(match);
                }
            }
        }
        java.util.ArrayList<android.content.pm.ResolveInfo> combinedMatches = new java.util.ArrayList<>(forwardMatches);
        int numMatchArrays = highestPriorityMatchesByUserId.size();
        for (int matchArrayNum = 0; matchArrayNum < numMatchArrays; matchArrayNum++) {
            combinedMatches.addAll(highestPriorityMatchesByUserId.valueAt(matchArrayNum));
        }
        return combinedMatches;
    }

    private java.util.ArrayList<android.content.pm.ResolveInfo> removeForwardIntentIfNotNeeded(java.util.ArrayList<android.content.pm.ResolveInfo> rawMatches) {
        int numRawMatches = rawMatches.size();
        int numParentActivityMatches = 0;
        int numNonParentActivityMatches = 0;
        for (int i = 0; i < numRawMatches; i++) {
            android.content.pm.ResolveInfo rawMatch = rawMatches.get(i);
            if (!isForwardMatch(rawMatch)) {
                if (android.os.UserHandle.getUserHandleForUid(rawMatch.activityInfo.applicationInfo.uid).equals(this.mParentUser)) {
                    numParentActivityMatches++;
                } else {
                    numNonParentActivityMatches++;
                }
            }
        }
        if (numParentActivityMatches == 0 || numNonParentActivityMatches == 0) {
            java.util.ArrayList<android.content.pm.ResolveInfo> matches = new java.util.ArrayList<>(numParentActivityMatches + numNonParentActivityMatches);
            for (int i2 = 0; i2 < numRawMatches; i2++) {
                android.content.pm.ResolveInfo rawMatch2 = rawMatches.get(i2);
                if (!isForwardMatch(rawMatch2)) {
                    matches.add(rawMatch2);
                }
            }
            return matches;
        }
        return rawMatches;
    }

    private java.util.ArrayList<android.content.pm.ResolveInfo> getDeviceMatchesLocked(android.hardware.usb.UsbDevice device, android.content.Intent intent) {
        java.util.ArrayList<android.content.pm.ResolveInfo> matches = new java.util.ArrayList<>();
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = queryIntentActivitiesForAllProfiles(intent);
        int count = resolveInfos.size();
        for (int i = 0; i < count; i++) {
            android.content.pm.ResolveInfo resolveInfo = resolveInfos.get(i);
            if (packageMatchesLocked(resolveInfo, device, null)) {
                matches.add(resolveInfo);
            }
        }
        return removeForwardIntentIfNotNeeded(preferHighPriority(matches));
    }

    private java.util.ArrayList<android.content.pm.ResolveInfo> getAccessoryMatchesLocked(android.hardware.usb.UsbAccessory accessory, android.content.Intent intent) {
        java.util.ArrayList<android.content.pm.ResolveInfo> matches = new java.util.ArrayList<>();
        java.util.List<android.content.pm.ResolveInfo> resolveInfos = queryIntentActivitiesForAllProfiles(intent);
        int count = resolveInfos.size();
        for (int i = 0; i < count; i++) {
            android.content.pm.ResolveInfo resolveInfo = resolveInfos.get(i);
            if (packageMatchesLocked(resolveInfo, null, accessory)) {
                matches.add(resolveInfo);
            }
        }
        return removeForwardIntentIfNotNeeded(preferHighPriority(matches));
    }

    public void deviceAttached(android.hardware.usb.UsbDevice device) {
        android.content.Intent intent = createDeviceAttachedIntent(device);
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL);
        if (!shouldRestrictOverlayActivities()) {
            resolveActivity(intent, device, true);
        }
    }

    private void resolveActivity(android.content.Intent intent, android.hardware.usb.UsbDevice device, boolean showMtpNotification) {
        java.util.ArrayList<android.content.pm.ResolveInfo> matches;
        android.content.pm.ActivityInfo defaultActivity;
        synchronized (this.mLock) {
            matches = getDeviceMatchesLocked(device, intent);
            defaultActivity = getDefaultActivityLocked(matches, this.mDevicePreferenceMap.get(new android.hardware.usb.DeviceFilter(device)));
        }
        if (showMtpNotification && com.android.server.usb.MtpNotificationManager.shouldShowNotification(this.mPackageManager, device) && defaultActivity == null) {
            this.mMtpNotificationManager.showNotification(device);
        } else {
            if (this.mUsbProfileGroupSettingsManagerExt.resolveActivityForOtgTest()) {
                return;
            }
            resolveActivity(intent, matches, defaultActivity, device, null);
        }
    }

    private boolean shouldRestrictOverlayActivities() {
        if (!com.android.server.usb.flags.Flags.allowRestrictionOfOverlayActivities()) {
            return false;
        }
        if (android.provider.Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "user_setup_complete", 1, android.os.UserHandle.CURRENT.getIdentifier()) == 0) {
            android.util.Slog.d(TAG, "restricting usb overlay activities as setup is not complete");
            return true;
        }
        java.util.List<android.app.ActivityManager.RunningAppProcessInfo> appProcessInfos = this.mActivityManager.getRunningAppProcesses();
        java.util.List<java.lang.String> filteredAppProcessInfos = new java.util.ArrayList<>();
        for (android.app.ActivityManager.RunningAppProcessInfo processInfo : appProcessInfos) {
            if (processInfo.importance <= 100) {
                filteredAppProcessInfos.addAll(java.util.List.of((java.lang.Object[]) processInfo.pkgList));
            }
        }
        java.util.List<java.lang.String> packagesHoldingManageUsbPermission = (java.util.List) this.mPackageManager.getPackagesHoldingPermissions(new java.lang.String[]{"android.permission.MANAGE_USB"}, 1048576).stream().map(new java.util.function.Function() { // from class: com.android.server.usb.UsbProfileGroupSettingsManager$$ExternalSyntheticLambda0
            @Override // java.util.function.Function
            public final java.lang.Object apply(java.lang.Object obj) {
                return ((android.content.pm.PackageInfo) obj).packageName;
            }
        }).collect(java.util.stream.Collectors.toList());
        filteredAppProcessInfos.retainAll(packagesHoldingManageUsbPermission);
        boolean shouldRestrictOverlayActivities = filteredAppProcessInfos.stream().anyMatch(new java.util.function.Predicate() { // from class: com.android.server.usb.UsbProfileGroupSettingsManager$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$shouldRestrictOverlayActivities$3((java.lang.String) obj);
            }
        });
        if (!shouldRestrictOverlayActivities) {
            android.util.Slog.d(TAG, "starting of usb overlay activities");
        }
        return shouldRestrictOverlayActivities;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$shouldRestrictOverlayActivities$3(java.lang.String pkg) {
        try {
            boolean restrictUsbOverlayActivitiesForPackage = this.mPackageManager.getProperty(PROPERTY_RESTRICT_USB_OVERLAY_ACTIVITIES, pkg).getBoolean();
            if (restrictUsbOverlayActivitiesForPackage) {
                android.util.Slog.d(TAG, "restricting usb overlay activities as package " + pkg + " is in foreground");
            }
            return restrictUsbOverlayActivitiesForPackage;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void deviceAttachedForFixedHandler(android.hardware.usb.UsbDevice device, android.content.ComponentName component) {
        android.content.Intent intent = createDeviceAttachedIntent(device);
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.of(android.app.ActivityManager.getCurrentUser()));
        try {
            android.content.pm.ApplicationInfo appInfo = this.mPackageManager.getApplicationInfoAsUser(component.getPackageName(), 0, this.mParentUser.getIdentifier());
            this.mSettingsManager.mUsbService.getPermissionsForUser(android.os.UserHandle.getUserId(appInfo.uid)).grantDevicePermission(device, appInfo.uid);
            android.content.Intent activityIntent = new android.content.Intent(intent);
            activityIntent.setComponent(component);
            try {
                this.mContext.startActivityAsUser(activityIntent, this.mParentUser);
            } catch (android.content.ActivityNotFoundException e) {
                android.util.Slog.e(TAG, "unable to start activity " + activityIntent);
            }
        } catch (android.content.pm.PackageManager.NameNotFoundException e2) {
            android.util.Slog.e(TAG, "Default USB handling package (" + component.getPackageName() + ") not found  for user " + this.mParentUser);
        }
    }

    void usbDeviceRemoved(android.hardware.usb.UsbDevice device) {
        this.mMtpNotificationManager.hideNotification(device.getDeviceId());
    }

    public void accessoryAttached(android.hardware.usb.UsbAccessory accessory) {
        java.util.ArrayList<android.content.pm.ResolveInfo> matches;
        android.content.pm.ActivityInfo defaultActivity;
        android.content.Intent intent = new android.content.Intent("android.hardware.usb.action.USB_ACCESSORY_ATTACHED");
        intent.putExtra("accessory", accessory);
        intent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.EVRCB);
        synchronized (this.mLock) {
            matches = getAccessoryMatchesLocked(accessory, intent);
            defaultActivity = getDefaultActivityLocked(matches, this.mAccessoryPreferenceMap.get(new android.hardware.usb.AccessoryFilter(accessory)));
        }
        sEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("accessoryAttached: " + intent));
        resolveActivity(intent, matches, defaultActivity, null, accessory);
    }

    private void resolveActivity(android.content.Intent intent, java.util.ArrayList<android.content.pm.ResolveInfo> matches, android.content.pm.ActivityInfo defaultActivity, android.hardware.usb.UsbDevice device, android.hardware.usb.UsbAccessory accessory) {
        android.util.ArraySet<com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage> arraySet = null;
        if (device != null) {
            arraySet = this.mDevicePreferenceDeniedMap.get(new android.hardware.usb.DeviceFilter(device));
        } else if (accessory != null) {
            arraySet = this.mAccessoryPreferenceDeniedMap.get(new android.hardware.usb.AccessoryFilter(accessory));
        }
        if (arraySet != null) {
            for (int i = matches.size() - 1; i >= 0; i--) {
                android.content.pm.ResolveInfo match = matches.get(i);
                java.lang.String packageName = match.activityInfo.packageName;
                android.os.UserHandle user = android.os.UserHandle.getUserHandleForUid(match.activityInfo.applicationInfo.uid);
                if (arraySet.contains(new com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage(packageName, user))) {
                    matches.remove(i);
                }
            }
        }
        int i2 = matches.size();
        if (i2 == 0) {
            if (accessory != null) {
                this.mUsbHandlerManager.showUsbAccessoryUriActivity(accessory, this.mParentUser);
                return;
            }
            return;
        }
        if (defaultActivity == null) {
            if (matches.size() == 1) {
                this.mUsbHandlerManager.confirmUsbHandler(matches.get(0), device, accessory);
                return;
            } else {
                this.mUsbHandlerManager.selectUsbHandler(matches, this.mParentUser, intent);
                return;
            }
        }
        com.android.server.usb.UsbUserPermissionManager defaultRIUserPermissions = this.mSettingsManager.mUsbService.getPermissionsForUser(android.os.UserHandle.getUserId(defaultActivity.applicationInfo.uid));
        if (device != null) {
            defaultRIUserPermissions.grantDevicePermission(device, defaultActivity.applicationInfo.uid);
        } else if (accessory != null) {
            defaultRIUserPermissions.grantAccessoryPermission(accessory, defaultActivity.applicationInfo.uid);
        }
        try {
            intent.setComponent(new android.content.ComponentName(defaultActivity.packageName, defaultActivity.name));
            android.os.UserHandle user2 = android.os.UserHandle.getUserHandleForUid(defaultActivity.applicationInfo.uid);
            this.mContext.startActivityAsUser(intent, user2);
        } catch (android.content.ActivityNotFoundException e) {
            android.util.Slog.e(TAG, "startActivity failed", e);
        }
    }

    private android.content.pm.ActivityInfo getDefaultActivityLocked(java.util.ArrayList<android.content.pm.ResolveInfo> matches, com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage) {
        android.content.pm.ActivityInfo activityInfo;
        if (userPackage != null) {
            for (android.content.pm.ResolveInfo info : matches) {
                if (info.activityInfo != null && userPackage.equals(new com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage(info.activityInfo.packageName, android.os.UserHandle.getUserHandleForUid(info.activityInfo.applicationInfo.uid)))) {
                    return info.activityInfo;
                }
            }
        }
        if (matches.size() == 1 && (activityInfo = matches.get(0).activityInfo) != null) {
            if (this.mDisablePermissionDialogs) {
                return activityInfo;
            }
            if (activityInfo.applicationInfo != null && (1 & activityInfo.applicationInfo.flags) != 0) {
                return activityInfo;
            }
        }
        return null;
    }

    private boolean clearCompatibleMatchesLocked(com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage, android.hardware.usb.DeviceFilter filter) {
        java.util.ArrayList<android.hardware.usb.DeviceFilter> keysToRemove = new java.util.ArrayList<>();
        for (android.hardware.usb.DeviceFilter device : this.mDevicePreferenceMap.keySet()) {
            if (filter.contains(device)) {
                com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage currentMatch = this.mDevicePreferenceMap.get(device);
                if (!currentMatch.equals(userPackage)) {
                    keysToRemove.add(device);
                }
            }
        }
        if (!keysToRemove.isEmpty()) {
            for (android.hardware.usb.DeviceFilter keyToRemove : keysToRemove) {
                this.mDevicePreferenceMap.remove(keyToRemove);
            }
        }
        return !keysToRemove.isEmpty();
    }

    private boolean clearCompatibleMatchesLocked(com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage, android.hardware.usb.AccessoryFilter filter) {
        java.util.ArrayList<android.hardware.usb.AccessoryFilter> keysToRemove = new java.util.ArrayList<>();
        for (android.hardware.usb.AccessoryFilter accessory : this.mAccessoryPreferenceMap.keySet()) {
            if (filter.contains(accessory)) {
                com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage currentMatch = this.mAccessoryPreferenceMap.get(accessory);
                if (!currentMatch.equals(userPackage)) {
                    keysToRemove.add(accessory);
                }
            }
        }
        if (!keysToRemove.isEmpty()) {
            for (android.hardware.usb.AccessoryFilter keyToRemove : keysToRemove) {
                this.mAccessoryPreferenceMap.remove(keyToRemove);
            }
        }
        return !keysToRemove.isEmpty();
    }

    private boolean handlePackageAddedLocked(com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage, android.content.pm.ActivityInfo aInfo, java.lang.String metaDataName) {
        android.content.res.XmlResourceParser parser = null;
        boolean changed = false;
        try {
            try {
                parser = aInfo.loadXmlMetaData(this.mPackageManager, metaDataName);
            } catch (java.lang.Exception e) {
                android.util.Slog.w(TAG, "Unable to load component info " + aInfo.toString(), e);
                if (parser != null) {
                }
            }
            if (parser == null) {
            }
            com.android.internal.util.XmlUtils.nextElement(parser);
            while (parser.getEventType() != 1) {
                java.lang.String tagName = parser.getName();
                if ("usb-device".equals(tagName)) {
                    android.hardware.usb.DeviceFilter filter = android.hardware.usb.DeviceFilter.read(parser);
                    if (clearCompatibleMatchesLocked(userPackage, filter)) {
                        changed = true;
                    }
                } else if ("usb-accessory".equals(tagName)) {
                    android.hardware.usb.AccessoryFilter filter2 = android.hardware.usb.AccessoryFilter.read(parser);
                    if (clearCompatibleMatchesLocked(userPackage, filter2)) {
                        changed = true;
                    }
                }
                com.android.internal.util.XmlUtils.nextElement(parser);
            }
            if (parser != null) {
                parser.close();
            }
            return changed;
        } finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePackageAdded(com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage) {
        synchronized (this.mLock) {
            boolean changed = false;
            try {
                try {
                    android.content.pm.PackageInfo info = this.mPackageManager.getPackageInfoAsUser(userPackage.packageName, 129, userPackage.user.getIdentifier());
                    android.content.pm.ActivityInfo[] activities = info.activities;
                    if (activities == null) {
                        return;
                    }
                    for (int i = 0; i < activities.length; i++) {
                        if (handlePackageAddedLocked(userPackage, activities[i], "android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
                            changed = true;
                        }
                        if (handlePackageAddedLocked(userPackage, activities[i], "android.hardware.usb.action.USB_ACCESSORY_ATTACHED")) {
                            changed = true;
                        }
                    }
                    if (changed) {
                        scheduleWriteSettingsLocked();
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    android.util.Slog.e(TAG, "handlePackageUpdate could not find package " + userPackage, e);
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
    }

    private int getSerial(android.os.UserHandle user) {
        return this.mUserManager.getUserSerialNumber(user.getIdentifier());
    }

    void setDevicePackage(android.hardware.usb.UsbDevice device, java.lang.String packageName, android.os.UserHandle user) {
        android.hardware.usb.DeviceFilter filter = new android.hardware.usb.DeviceFilter(device);
        synchronized (this.mLock) {
            boolean changed = true;
            if (packageName == null) {
                if (this.mDevicePreferenceMap.remove(filter) == null) {
                    changed = false;
                }
            } else {
                com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage = new com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage(packageName, user);
                changed = true ^ userPackage.equals(this.mDevicePreferenceMap.get(filter));
                if (changed) {
                    this.mDevicePreferenceMap.put(filter, userPackage);
                }
            }
            if (changed) {
                scheduleWriteSettingsLocked();
            }
        }
    }

    void addDevicePackagesToDenied(android.hardware.usb.UsbDevice device, java.lang.String[] packageNames, android.os.UserHandle user) {
        android.util.ArraySet<com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage> userPackages;
        if (packageNames.length == 0) {
            return;
        }
        android.hardware.usb.DeviceFilter filter = new android.hardware.usb.DeviceFilter(device);
        synchronized (this.mLock) {
            if (this.mDevicePreferenceDeniedMap.containsKey(filter)) {
                userPackages = this.mDevicePreferenceDeniedMap.get(filter);
            } else {
                userPackages = new android.util.ArraySet<>();
                this.mDevicePreferenceDeniedMap.put(filter, userPackages);
            }
            boolean shouldWrite = false;
            for (java.lang.String packageName : packageNames) {
                com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage = new com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage(packageName, user);
                if (!userPackages.contains(userPackage)) {
                    userPackages.add(userPackage);
                    shouldWrite = true;
                }
            }
            if (shouldWrite) {
                scheduleWriteSettingsLocked();
            }
        }
    }

    void addAccessoryPackagesToDenied(android.hardware.usb.UsbAccessory accessory, java.lang.String[] packageNames, android.os.UserHandle user) {
        android.util.ArraySet<com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage> userPackages;
        if (packageNames.length == 0) {
            return;
        }
        android.hardware.usb.AccessoryFilter filter = new android.hardware.usb.AccessoryFilter(accessory);
        synchronized (this.mLock) {
            if (this.mAccessoryPreferenceDeniedMap.containsKey(filter)) {
                userPackages = this.mAccessoryPreferenceDeniedMap.get(filter);
            } else {
                userPackages = new android.util.ArraySet<>();
                this.mAccessoryPreferenceDeniedMap.put(filter, userPackages);
            }
            boolean shouldWrite = false;
            for (java.lang.String packageName : packageNames) {
                com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage = new com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage(packageName, user);
                if (!userPackages.contains(userPackage)) {
                    userPackages.add(userPackage);
                    shouldWrite = true;
                }
            }
            if (shouldWrite) {
                scheduleWriteSettingsLocked();
            }
        }
    }

    void removeDevicePackagesFromDenied(android.hardware.usb.UsbDevice device, java.lang.String[] packageNames, android.os.UserHandle user) {
        android.hardware.usb.DeviceFilter filter = new android.hardware.usb.DeviceFilter(device);
        synchronized (this.mLock) {
            android.util.ArraySet<com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage> userPackages = this.mDevicePreferenceDeniedMap.get(filter);
            if (userPackages != null) {
                boolean shouldWrite = false;
                int length = packageNames.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    java.lang.String packageName = packageNames[i];
                    com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage = new com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage(packageName, user);
                    if (userPackages.contains(userPackage)) {
                        userPackages.remove(userPackage);
                        shouldWrite = true;
                        if (userPackages.size() == 0) {
                            this.mDevicePreferenceDeniedMap.remove(filter);
                            break;
                        }
                    }
                    i++;
                }
                if (shouldWrite) {
                    scheduleWriteSettingsLocked();
                }
            }
        }
    }

    void removeAccessoryPackagesFromDenied(android.hardware.usb.UsbAccessory accessory, java.lang.String[] packageNames, android.os.UserHandle user) {
        android.hardware.usb.AccessoryFilter filter = new android.hardware.usb.AccessoryFilter(accessory);
        synchronized (this.mLock) {
            android.util.ArraySet<com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage> userPackages = this.mAccessoryPreferenceDeniedMap.get(filter);
            if (userPackages != null) {
                boolean shouldWrite = false;
                int length = packageNames.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    }
                    java.lang.String packageName = packageNames[i];
                    com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage = new com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage(packageName, user);
                    if (userPackages.contains(userPackage)) {
                        userPackages.remove(userPackage);
                        shouldWrite = true;
                        if (userPackages.size() == 0) {
                            this.mAccessoryPreferenceDeniedMap.remove(filter);
                            break;
                        }
                    }
                    i++;
                }
                if (shouldWrite) {
                    scheduleWriteSettingsLocked();
                }
            }
        }
    }

    void setAccessoryPackage(android.hardware.usb.UsbAccessory accessory, java.lang.String packageName, android.os.UserHandle user) {
        android.hardware.usb.AccessoryFilter filter = new android.hardware.usb.AccessoryFilter(accessory);
        synchronized (this.mLock) {
            boolean changed = true;
            if (packageName == null) {
                if (this.mAccessoryPreferenceMap.remove(filter) == null) {
                    changed = false;
                }
            } else {
                com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage = new com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage(packageName, user);
                changed = true ^ userPackage.equals(this.mAccessoryPreferenceMap.get(filter));
                if (changed) {
                    this.mAccessoryPreferenceMap.put(filter, userPackage);
                }
            }
            if (changed) {
                scheduleWriteSettingsLocked();
            }
        }
    }

    boolean hasDefaults(java.lang.String packageName, android.os.UserHandle user) {
        com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage = new com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage(packageName, user);
        synchronized (this.mLock) {
            if (this.mDevicePreferenceMap.values().contains(userPackage)) {
                return true;
            }
            return this.mAccessoryPreferenceMap.values().contains(userPackage);
        }
    }

    void clearDefaults(java.lang.String packageName, android.os.UserHandle user) {
        com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage = new com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage(packageName, user);
        synchronized (this.mLock) {
            if (clearPackageDefaultsLocked(userPackage)) {
                scheduleWriteSettingsLocked();
            }
        }
    }

    private boolean clearPackageDefaultsLocked(com.android.server.usb.UsbProfileGroupSettingsManager.UserPackage userPackage) {
        boolean cleared = false;
        synchronized (this.mLock) {
            if (this.mDevicePreferenceMap.containsValue(userPackage)) {
                android.hardware.usb.DeviceFilter[] keys = (android.hardware.usb.DeviceFilter[]) this.mDevicePreferenceMap.keySet().toArray(new android.hardware.usb.DeviceFilter[0]);
                for (android.hardware.usb.DeviceFilter key : keys) {
                    if (userPackage.equals(this.mDevicePreferenceMap.get(key))) {
                        this.mDevicePreferenceMap.remove(key);
                        cleared = true;
                    }
                }
            }
            if (this.mAccessoryPreferenceMap.containsValue(userPackage)) {
                android.hardware.usb.AccessoryFilter[] keys2 = (android.hardware.usb.AccessoryFilter[]) this.mAccessoryPreferenceMap.keySet().toArray(new android.hardware.usb.AccessoryFilter[0]);
                for (android.hardware.usb.AccessoryFilter key2 : keys2) {
                    if (userPackage.equals(this.mAccessoryPreferenceMap.get(key2))) {
                        this.mAccessoryPreferenceMap.remove(key2);
                        cleared = true;
                    }
                }
            }
        }
        return cleared;
    }

    public void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
        long token = dump.start(idName, id);
        synchronized (this.mLock) {
            dump.write("parent_user_id", 1120986464257L, this.mParentUser.getIdentifier());
            for (android.hardware.usb.DeviceFilter filter : this.mDevicePreferenceMap.keySet()) {
                long devicePrefToken = dump.start("device_preferences", 2246267895810L);
                filter.dump(dump, com.android.server.pm.verify.domain.DomainVerificationPersistence.ATTR_FILTER, 1146756268033L);
                this.mDevicePreferenceMap.get(filter).dump(dump, "user_package", 1146756268034L);
                dump.end(devicePrefToken);
            }
            for (android.hardware.usb.AccessoryFilter filter2 : this.mAccessoryPreferenceMap.keySet()) {
                long accessoryPrefToken = dump.start("accessory_preferences", 2246267895811L);
                filter2.dump(dump, com.android.server.pm.verify.domain.DomainVerificationPersistence.ATTR_FILTER, 1146756268033L);
                this.mAccessoryPreferenceMap.get(filter2).dump(dump, "user_package", 1146756268034L);
                dump.end(accessoryPrefToken);
            }
        }
        sEventLogger.dump(new com.android.server.usb.DualOutputStreamDumpSink(dump, 1138166333444L));
        dump.end(token);
    }

    private static android.content.Intent createDeviceAttachedIntent(android.hardware.usb.UsbDevice device) {
        android.content.Intent intent = new android.content.Intent("android.hardware.usb.action.USB_DEVICE_ATTACHED");
        intent.putExtra("device", device);
        intent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.EVRCB);
        return intent;
    }
}
