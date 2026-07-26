package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
class UsbPermissionManager {
    private static final boolean DEBUG = false;
    private static final java.lang.String LOG_TAG = com.android.server.usb.UsbPermissionManager.class.getSimpleName();
    private final android.content.Context mContext;
    private final android.util.SparseArray<com.android.server.usb.UsbUserPermissionManager> mPermissionsByUser = new android.util.SparseArray<>();
    final com.android.server.usb.UsbService mUsbService;

    UsbPermissionManager(android.content.Context context, com.android.server.usb.UsbService usbService) {
        this.mContext = context;
        this.mUsbService = usbService;
    }

    com.android.server.usb.UsbUserPermissionManager getPermissionsForUser(int userId) {
        com.android.server.usb.UsbUserPermissionManager permissions;
        synchronized (this.mPermissionsByUser) {
            permissions = this.mPermissionsByUser.get(userId);
            if (permissions == null) {
                permissions = new com.android.server.usb.UsbUserPermissionManager(this.mContext.createContextAsUser(android.os.UserHandle.of(userId), 0), this.mUsbService.getSettingsForUser(userId));
                this.mPermissionsByUser.put(userId, permissions);
            }
        }
        return permissions;
    }

    com.android.server.usb.UsbUserPermissionManager getPermissionsForUser(android.os.UserHandle user) {
        return getPermissionsForUser(user.getIdentifier());
    }

    void remove(android.os.UserHandle userToRemove) {
        synchronized (this.mPermissionsByUser) {
            this.mPermissionsByUser.remove(userToRemove.getIdentifier());
        }
    }

    void usbDeviceRemoved(android.hardware.usb.UsbDevice device) {
        synchronized (this.mPermissionsByUser) {
            for (int i = 0; i < this.mPermissionsByUser.size(); i++) {
                this.mPermissionsByUser.valueAt(i).removeDevicePermissions(device);
            }
        }
        android.content.Intent intent = new android.content.Intent("android.hardware.usb.action.USB_DEVICE_DETACHED");
        intent.addFlags(16777216);
        intent.putExtra("device", device);
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL);
    }

    void usbAccessoryRemoved(android.hardware.usb.UsbAccessory accessory) {
        synchronized (this.mPermissionsByUser) {
            for (int i = 0; i < this.mPermissionsByUser.size(); i++) {
                this.mPermissionsByUser.valueAt(i).removeAccessoryPermissions(accessory);
            }
        }
        android.content.Intent intent = new android.content.Intent("android.hardware.usb.action.USB_ACCESSORY_DETACHED");
        intent.addFlags(16777216);
        intent.putExtra("accessory", accessory);
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL);
    }

    void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
        long token = dump.start(idName, id);
        android.os.UserManager userManager = (android.os.UserManager) this.mContext.getSystemService(android.os.UserManager.class);
        synchronized (this.mPermissionsByUser) {
            java.util.List<android.content.pm.UserInfo> users = userManager.getUsers();
            int numUsers = users.size();
            for (int i = 0; i < numUsers; i++) {
                getPermissionsForUser(users.get(i).id).dump(dump, "user_permissions", 2246267895809L);
            }
        }
        dump.end(token);
    }
}
