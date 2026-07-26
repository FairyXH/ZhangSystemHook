package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public class UsbUserSettingsManager {
    private static final boolean DEBUG = false;
    private static final java.lang.String TAG = com.android.server.usb.UsbUserSettingsManager.class.getSimpleName();
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.content.pm.PackageManager mPackageManager;
    private final android.os.UserHandle mUser;
    private final android.content.Context mUserContext;

    UsbUserSettingsManager(android.content.Context context, android.os.UserHandle user) {
        try {
            this.mUserContext = context.createPackageContextAsUser(com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME, 0, user);
            this.mPackageManager = this.mUserContext.getPackageManager();
            this.mUser = user;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.RuntimeException("Missing android package");
        }
    }

    public java.util.List<android.content.pm.ResolveInfo> queryIntentActivities(android.content.Intent intent) {
        return this.mPackageManager.queryIntentActivitiesAsUser(intent, 128, this.mUser.getIdentifier());
    }

    boolean canBeDefault(android.hardware.usb.UsbDevice device, java.lang.String packageName) {
        android.content.pm.ActivityInfo[] activities = getPackageActivities(packageName);
        if (activities == null) {
            return false;
        }
        for (android.content.pm.ActivityInfo activityInfo : activities) {
            try {
                android.content.res.XmlResourceParser parser = activityInfo.loadXmlMetaData(this.mPackageManager, "android.hardware.usb.action.USB_DEVICE_ATTACHED");
                if (parser != null) {
                    try {
                        com.android.internal.util.XmlUtils.nextElement(parser);
                        while (parser.getEventType() != 1) {
                            if ("usb-device".equals(parser.getName())) {
                                android.hardware.usb.DeviceFilter filter = android.hardware.usb.DeviceFilter.read(parser);
                                if (filter.matches(device)) {
                                    if (parser != null) {
                                        parser.close();
                                    }
                                    return true;
                                }
                            }
                            com.android.internal.util.XmlUtils.nextElement(parser);
                        }
                        if (parser != null) {
                            parser.close();
                        }
                    } finally {
                    }
                } else if (parser != null) {
                    parser.close();
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.w(TAG, "Unable to load component info " + activityInfo.toString(), e);
            }
        }
        return false;
    }

    boolean canBeDefault(android.hardware.usb.UsbAccessory accessory, java.lang.String packageName) {
        android.content.pm.ActivityInfo[] activities = getPackageActivities(packageName);
        if (activities == null) {
            return false;
        }
        for (android.content.pm.ActivityInfo activityInfo : activities) {
            try {
                android.content.res.XmlResourceParser parser = activityInfo.loadXmlMetaData(this.mPackageManager, "android.hardware.usb.action.USB_ACCESSORY_ATTACHED");
                if (parser != null) {
                    try {
                        com.android.internal.util.XmlUtils.nextElement(parser);
                        while (parser.getEventType() != 1) {
                            if ("usb-accessory".equals(parser.getName())) {
                                android.hardware.usb.AccessoryFilter filter = android.hardware.usb.AccessoryFilter.read(parser);
                                if (filter.matches(accessory)) {
                                    if (parser != null) {
                                        parser.close();
                                    }
                                    return true;
                                }
                            }
                            com.android.internal.util.XmlUtils.nextElement(parser);
                        }
                        if (parser != null) {
                            parser.close();
                        }
                    } finally {
                    }
                } else if (parser != null) {
                    parser.close();
                }
            } catch (java.lang.Exception e) {
                android.util.Slog.w(TAG, "Unable to load component info " + activityInfo.toString(), e);
            }
        }
        return false;
    }

    private android.content.pm.ActivityInfo[] getPackageActivities(java.lang.String packageName) {
        try {
            android.content.pm.PackageInfo packageInfo = this.mPackageManager.getPackageInfo(packageName, 129);
            return packageInfo.activities;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
        int numDeviceAttachedActivities;
        long token = dump.start(idName, id);
        synchronized (this.mLock) {
            dump.write("user_id", 1120986464257L, this.mUser.getIdentifier());
            java.util.List<android.content.pm.ResolveInfo> deviceAttachedActivities = queryIntentActivities(new android.content.Intent("android.hardware.usb.action.USB_DEVICE_ATTACHED"));
            int numDeviceAttachedActivities2 = deviceAttachedActivities.size();
            for (int activityNum = 0; activityNum < numDeviceAttachedActivities2; activityNum++) {
                android.content.pm.ResolveInfo deviceAttachedActivity = deviceAttachedActivities.get(activityNum);
                long deviceAttachedActivityToken = dump.start("device_attached_activities", 2246267895812L);
                com.android.internal.util.dump.DumpUtils.writeComponentName(dump, com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY, 1146756268033L, new android.content.ComponentName(deviceAttachedActivity.activityInfo.packageName, deviceAttachedActivity.activityInfo.name));
                java.util.ArrayList<android.hardware.usb.DeviceFilter> deviceFilters = com.android.server.usb.UsbProfileGroupSettingsManager.getDeviceFilters(this.mPackageManager, deviceAttachedActivity);
                if (deviceFilters != null) {
                    int filterNum = 0;
                    for (int numDeviceFilters = deviceFilters.size(); filterNum < numDeviceFilters; numDeviceFilters = numDeviceFilters) {
                        deviceFilters.get(filterNum).dump(dump, "filters", 2246267895810L);
                        filterNum++;
                        deviceFilters = deviceFilters;
                    }
                }
                dump.end(deviceAttachedActivityToken);
            }
            java.util.List<android.content.pm.ResolveInfo> accessoryAttachedActivities = queryIntentActivities(new android.content.Intent("android.hardware.usb.action.USB_ACCESSORY_ATTACHED"));
            int numAccessoryAttachedActivities = accessoryAttachedActivities.size();
            int activityNum2 = 0;
            while (activityNum2 < numAccessoryAttachedActivities) {
                android.content.pm.ResolveInfo accessoryAttachedActivity = accessoryAttachedActivities.get(activityNum2);
                long accessoryAttachedActivityToken = dump.start("accessory_attached_activities", 2246267895813L);
                java.util.List<android.content.pm.ResolveInfo> deviceAttachedActivities2 = deviceAttachedActivities;
                int numDeviceAttachedActivities3 = numDeviceAttachedActivities2;
                java.util.List<android.content.pm.ResolveInfo> accessoryAttachedActivities2 = accessoryAttachedActivities;
                com.android.internal.util.dump.DumpUtils.writeComponentName(dump, com.android.server.am.HostingRecord.HOSTING_TYPE_ACTIVITY, 1146756268033L, new android.content.ComponentName(accessoryAttachedActivity.activityInfo.packageName, accessoryAttachedActivity.activityInfo.name));
                java.util.ArrayList<android.hardware.usb.AccessoryFilter> accessoryFilters = com.android.server.usb.UsbProfileGroupSettingsManager.getAccessoryFilters(this.mPackageManager, accessoryAttachedActivity);
                if (accessoryFilters == null) {
                    numDeviceAttachedActivities = numDeviceAttachedActivities3;
                } else {
                    int filterNum2 = 0;
                    for (int numAccessoryFilters = accessoryFilters.size(); filterNum2 < numAccessoryFilters; numAccessoryFilters = numAccessoryFilters) {
                        accessoryFilters.get(filterNum2).dump(dump, "filters", 2246267895810L);
                        filterNum2++;
                        numDeviceAttachedActivities3 = numDeviceAttachedActivities3;
                        accessoryFilters = accessoryFilters;
                    }
                    numDeviceAttachedActivities = numDeviceAttachedActivities3;
                }
                dump.end(accessoryAttachedActivityToken);
                activityNum2++;
                accessoryAttachedActivities = accessoryAttachedActivities2;
                numDeviceAttachedActivities2 = numDeviceAttachedActivities;
                deviceAttachedActivities = deviceAttachedActivities2;
            }
        }
        dump.end(token);
    }
}
