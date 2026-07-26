package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public class UsbSettingsManager {
    private static final boolean DEBUG = false;
    private static final java.lang.String LOG_TAG = com.android.server.usb.UsbSettingsManager.class.getSimpleName();
    private final android.content.Context mContext;
    private com.android.server.usb.UsbHandlerManager mUsbHandlerManager;
    final com.android.server.usb.UsbService mUsbService;
    private android.os.UserManager mUserManager;
    private final android.util.SparseArray<com.android.server.usb.UsbUserSettingsManager> mSettingsByUser = new android.util.SparseArray<>();
    private final android.util.SparseArray<com.android.server.usb.UsbProfileGroupSettingsManager> mSettingsByProfileGroup = new android.util.SparseArray<>();

    UsbSettingsManager(android.content.Context context, com.android.server.usb.UsbService usbService) {
        this.mContext = context;
        this.mUsbService = usbService;
        this.mUserManager = (android.os.UserManager) context.getSystemService("user");
        this.mUsbHandlerManager = new com.android.server.usb.UsbHandlerManager(context);
    }

    public com.android.server.usb.UsbUserSettingsManager getSettingsForUser(int userId) {
        com.android.server.usb.UsbUserSettingsManager settings;
        synchronized (this.mSettingsByUser) {
            settings = this.mSettingsByUser.get(userId);
            if (settings == null) {
                settings = new com.android.server.usb.UsbUserSettingsManager(this.mContext, android.os.UserHandle.of(userId));
                this.mSettingsByUser.put(userId, settings);
            }
        }
        return settings;
    }

    com.android.server.usb.UsbProfileGroupSettingsManager getSettingsForProfileGroup(android.os.UserHandle user) {
        android.os.UserHandle parentUser;
        com.android.server.usb.UsbProfileGroupSettingsManager settings;
        android.content.pm.UserInfo parentUserInfo = this.mUserManager.getProfileParent(user.getIdentifier());
        if (parentUserInfo != null) {
            parentUser = parentUserInfo.getUserHandle();
        } else {
            parentUser = user;
        }
        synchronized (this.mSettingsByProfileGroup) {
            settings = this.mSettingsByProfileGroup.get(parentUser.getIdentifier());
            if (settings == null) {
                settings = new com.android.server.usb.UsbProfileGroupSettingsManager(this.mContext, parentUser, this, this.mUsbHandlerManager);
                this.mSettingsByProfileGroup.put(parentUser.getIdentifier(), settings);
            }
        }
        return settings;
    }

    void remove(android.os.UserHandle userToRemove) {
        synchronized (this.mSettingsByUser) {
            this.mSettingsByUser.remove(userToRemove.getIdentifier());
        }
        synchronized (this.mSettingsByProfileGroup) {
            if (this.mSettingsByProfileGroup.indexOfKey(userToRemove.getIdentifier()) >= 0) {
                this.mSettingsByProfileGroup.get(userToRemove.getIdentifier()).unregisterReceivers();
                this.mSettingsByProfileGroup.remove(userToRemove.getIdentifier());
            } else {
                int numProfileGroups = this.mSettingsByProfileGroup.size();
                for (int i = 0; i < numProfileGroups; i++) {
                    this.mSettingsByProfileGroup.valueAt(i).removeUser(userToRemove);
                }
            }
        }
    }

    void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
        long token = dump.start(idName, id);
        synchronized (this.mSettingsByUser) {
            java.util.List<android.content.pm.UserInfo> users = this.mUserManager.getUsers();
            int numUsers = users.size();
            for (int i = 0; i < numUsers; i++) {
                getSettingsForUser(users.get(i).id).dump(dump, "user_settings", 2246267895809L);
            }
        }
        synchronized (this.mSettingsByProfileGroup) {
            int numProfileGroups = this.mSettingsByProfileGroup.size();
            for (int i2 = 0; i2 < numProfileGroups; i2++) {
                this.mSettingsByProfileGroup.valueAt(i2).dump(dump, "profile_group_settings", 2246267895810L);
            }
        }
        dump.end(token);
    }
}
