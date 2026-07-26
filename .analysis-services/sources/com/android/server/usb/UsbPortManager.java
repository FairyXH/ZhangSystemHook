package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public class UsbPortManager implements android.os.IBinder.DeathRecipient {
    private static final int MSG_SYSTEM_READY = 2;
    private static final int MSG_UPDATE_PORTS = 1;
    private static final java.lang.String PORT_INFO = "port_info";
    private static final java.lang.String TAG = "UsbPortManager";
    private final android.content.Context mContext;
    private int mIsPortContaminatedNotificationId;
    private android.app.NotificationManager mNotificationManager;
    private boolean mSystemReady;
    private long mTransactionId;
    private static final int COMBO_SOURCE_HOST = android.hardware.usb.UsbPort.combineRolesAsBit(1, 1);
    private static final int COMBO_SOURCE_DEVICE = android.hardware.usb.UsbPort.combineRolesAsBit(1, 2);
    private static final int COMBO_SINK_HOST = android.hardware.usb.UsbPort.combineRolesAsBit(2, 1);
    private static final int COMBO_SINK_DEVICE = android.hardware.usb.UsbPort.combineRolesAsBit(2, 2);
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.ArrayMap<java.lang.String, com.android.server.usb.UsbPortManager.PortInfo> mPorts = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, com.android.server.usb.hal.port.RawPortInfo> mSimulatedPorts = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, java.lang.Boolean> mConnected = new android.util.ArrayMap<>();
    private final android.util.ArrayMap<java.lang.String, java.lang.Integer> mContaminantStatus = new android.util.ArrayMap<>();
    private final java.lang.Object mDisplayPortListenerLock = new java.lang.Object();
    private final android.util.ArrayMap<android.os.IBinder, android.hardware.usb.IDisplayPortAltModeInfoListener> mDisplayPortListeners = new android.util.ArrayMap<>();
    private final android.os.Handler mHandler = new android.os.Handler(com.android.server.FgThread.get().getLooper()) { // from class: com.android.server.usb.UsbPortManager.1
        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    android.os.Bundle b = msg.getData();
                    java.util.ArrayList<com.android.server.usb.hal.port.RawPortInfo> PortInfo2 = b.getParcelableArrayList(com.android.server.usb.UsbPortManager.PORT_INFO, com.android.server.usb.hal.port.RawPortInfo.class);
                    synchronized (com.android.server.usb.UsbPortManager.this.mLock) {
                        com.android.server.usb.UsbPortManager.this.updatePortsLocked(null, PortInfo2);
                        break;
                    }
                    return;
                case 2:
                    com.android.server.usb.UsbPortManager.this.mNotificationManager = (android.app.NotificationManager) com.android.server.usb.UsbPortManager.this.mContext.getSystemService("notification");
                    return;
                default:
                    return;
            }
        }
    };
    private com.android.server.usb.hal.port.UsbPortHal mUsbPortHal = com.android.server.usb.hal.port.UsbPortHalInstance.getInstance(this, null);

    public UsbPortManager(android.content.Context context) {
        this.mContext = context;
        logAndPrint(3, null, "getInstance done");
    }

    public void systemReady() {
        this.mSystemReady = true;
        if (this.mUsbPortHal != null) {
            this.mUsbPortHal.systemReady();
            try {
                com.android.server.usb.hal.port.UsbPortHal usbPortHal = this.mUsbPortHal;
                long j = this.mTransactionId + 1;
                this.mTransactionId = j;
                usbPortHal.queryPortStatus(j);
            } catch (java.lang.Exception e) {
                logAndPrintException(null, "ServiceStart: Failed to query port status", e);
            }
        }
        this.mHandler.sendEmptyMessage(2);
        ((com.android.server.usb.IOplusUsbDeviceFeature) android.common.OplusFeatureCache.getOrCreate(com.android.server.usb.IOplusUsbDeviceFeature.DEFAULT, new java.lang.Object[0])).sendPortChangeMessage(null, true);
    }

    private void updateContaminantNotification() {
        com.android.server.usb.UsbPortManager.PortInfo currentPortInfo = null;
        android.content.res.Resources r = this.mContext.getResources();
        int contaminantStatus = 2;
        for (com.android.server.usb.UsbPortManager.PortInfo portInfo : this.mPorts.values()) {
            contaminantStatus = portInfo.mUsbPortStatus.getContaminantDetectionStatus();
            if (contaminantStatus == 3 || contaminantStatus == 1) {
                currentPortInfo = portInfo;
                break;
            }
        }
        if (contaminantStatus == 3 && this.mIsPortContaminatedNotificationId != 52) {
            if (this.mIsPortContaminatedNotificationId == 53) {
                this.mNotificationManager.cancelAsUser(null, this.mIsPortContaminatedNotificationId, android.os.UserHandle.ALL);
            }
            this.mIsPortContaminatedNotificationId = 52;
            java.lang.CharSequence title = r.getText(android.R.string.suggested_apps_group_a11y_title);
            java.lang.String channel = com.android.internal.notification.SystemNotificationChannels.ALERTS;
            java.lang.CharSequence message = r.getText(android.R.string.submit);
            android.content.Intent intent = new android.content.Intent();
            intent.addFlags(268435456);
            intent.setComponent(android.content.ComponentName.unflattenFromString(r.getString(android.R.string.config_vendorColorModesRestoreHint)));
            intent.putExtra("port", (android.os.Parcelable) android.hardware.usb.ParcelableUsbPort.of(currentPortInfo.mUsbPort));
            intent.putExtra("portStatus", (android.os.Parcelable) currentPortInfo.mUsbPortStatus);
            android.app.PendingIntent pi = android.app.PendingIntent.getActivityAsUser(this.mContext, 0, intent, 67108864, null, android.os.UserHandle.CURRENT);
            android.app.Notification.Builder builder = new android.app.Notification.Builder(this.mContext, channel).setOngoing(true).setTicker(title).setColor(this.mContext.getColor(android.R.color.system_notification_accent_color)).setContentIntent(pi).setContentTitle(title).setContentText(message).setVisibility(1).setSmallIcon(android.R.drawable.stat_sys_warning).setStyle(new android.app.Notification.BigTextStyle().bigText(message));
            android.app.Notification notification = builder.build();
            this.mNotificationManager.notifyAsUser(null, this.mIsPortContaminatedNotificationId, notification, android.os.UserHandle.ALL);
            return;
        }
        if (contaminantStatus != 3 && this.mIsPortContaminatedNotificationId == 52) {
            this.mNotificationManager.cancelAsUser(null, this.mIsPortContaminatedNotificationId, android.os.UserHandle.ALL);
            this.mIsPortContaminatedNotificationId = 0;
            if (contaminantStatus == 2) {
                this.mIsPortContaminatedNotificationId = 53;
                java.lang.CharSequence title2 = r.getText(android.R.string.suspended_widget_accessibility);
                java.lang.String channel2 = com.android.internal.notification.SystemNotificationChannels.ALERTS;
                java.lang.CharSequence message2 = r.getText(android.R.string.supervised_user_creation_label);
                android.app.Notification.Builder builder2 = new android.app.Notification.Builder(this.mContext, channel2).setSmallIcon(android.R.drawable.ic_qs_auto_rotate).setTicker(title2).setColor(this.mContext.getColor(android.R.color.system_notification_accent_color)).setContentTitle(title2).setContentText(message2).setVisibility(1).setStyle(new android.app.Notification.BigTextStyle().bigText(message2));
                android.app.Notification notification2 = builder2.build();
                this.mNotificationManager.notifyAsUser(null, this.mIsPortContaminatedNotificationId, notification2, android.os.UserHandle.ALL);
            }
        }
    }

    public android.hardware.usb.UsbPort[] getPorts() {
        android.hardware.usb.UsbPort[] result;
        synchronized (this.mLock) {
            int count = this.mPorts.size();
            result = new android.hardware.usb.UsbPort[count];
            for (int i = 0; i < count; i++) {
                result[i] = this.mPorts.valueAt(i).mUsbPort;
            }
        }
        return result;
    }

    public android.hardware.usb.UsbPortStatus getPortStatus(java.lang.String portId) {
        android.hardware.usb.UsbPortStatus usbPortStatus;
        synchronized (this.mLock) {
            com.android.server.usb.UsbPortManager.PortInfo portInfo = this.mPorts.get(portId);
            usbPortStatus = portInfo != null ? portInfo.mUsbPortStatus : null;
        }
        return usbPortStatus;
    }

    public boolean isModeChangeSupported(java.lang.String portId) {
        boolean z;
        synchronized (this.mLock) {
            com.android.server.usb.UsbPortManager.PortInfo portInfo = this.mPorts.get(portId);
            z = portInfo != null ? portInfo.mCanChangeMode : false;
        }
        return z;
    }

    public void enableContaminantDetection(java.lang.String portId, boolean enable, com.android.internal.util.IndentingPrintWriter pw) {
        com.android.server.usb.UsbPortManager.PortInfo portInfo = this.mPorts.get(portId);
        if (portInfo == null) {
            if (pw != null) {
                pw.println("No such USB port: " + portId);
                return;
            }
            return;
        }
        if (!portInfo.mUsbPort.supportsEnableContaminantPresenceDetection()) {
            return;
        }
        if (!enable || portInfo.mUsbPortStatus.getContaminantDetectionStatus() == 1) {
            if ((!enable && portInfo.mUsbPortStatus.getContaminantDetectionStatus() == 1) || portInfo.mUsbPortStatus.getContaminantDetectionStatus() == 0) {
                return;
            }
            try {
                com.android.server.usb.hal.port.UsbPortHal usbPortHal = this.mUsbPortHal;
                long j = this.mTransactionId + 1;
                this.mTransactionId = j;
                usbPortHal.enableContaminantPresenceDetection(portId, enable, j);
            } catch (java.lang.Exception e) {
                logAndPrintException(pw, "Failed to set contaminant detection", e);
            }
        }
    }

    public void enableLimitPowerTransfer(java.lang.String portId, boolean limit, long transactionId, android.hardware.usb.IUsbOperationInternal callback, com.android.internal.util.IndentingPrintWriter pw) {
        java.util.Objects.requireNonNull(portId);
        com.android.server.usb.UsbPortManager.PortInfo portInfo = this.mPorts.get(portId);
        if (portInfo == null) {
            logAndPrint(6, pw, "enableLimitPowerTransfer: No such port: " + portId + " opId:" + transactionId);
            if (callback != null) {
                try {
                    callback.onOperationComplete(3);
                    return;
                } catch (android.os.RemoteException e) {
                    logAndPrintException(pw, "enableLimitPowerTransfer: Failed to call OperationComplete. opId:" + transactionId, e);
                    return;
                }
            }
            return;
        }
        try {
            try {
                this.mUsbPortHal.enableLimitPowerTransfer(portId, limit, transactionId, callback);
            } catch (java.lang.Exception e2) {
                logAndPrintException(pw, "enableLimitPowerTransfer: Failed to limit power transfer. opId:" + transactionId, e2);
                if (callback != null) {
                    callback.onOperationComplete(1);
                }
            }
        } catch (android.os.RemoteException e3) {
            logAndPrintException(pw, "enableLimitPowerTransfer:Failed to call onOperationComplete. opId:" + transactionId, e3);
        }
    }

    public void enableUsbDataWhileDocked(java.lang.String portId, long transactionId, android.hardware.usb.IUsbOperationInternal callback, com.android.internal.util.IndentingPrintWriter pw) {
        java.util.Objects.requireNonNull(portId);
        com.android.server.usb.UsbPortManager.PortInfo portInfo = this.mPorts.get(portId);
        if (portInfo == null) {
            logAndPrint(6, pw, "enableUsbDataWhileDocked: No such port: " + portId + " opId:" + transactionId);
            if (callback != null) {
                try {
                    callback.onOperationComplete(3);
                    return;
                } catch (android.os.RemoteException e) {
                    logAndPrintException(pw, "enableUsbDataWhileDocked: Failed to call OperationComplete. opId:" + transactionId, e);
                    return;
                }
            }
            return;
        }
        try {
            try {
                this.mUsbPortHal.enableUsbDataWhileDocked(portId, transactionId, callback);
            } catch (java.lang.Exception e2) {
                logAndPrintException(pw, "enableUsbDataWhileDocked: Failed to limit power transfer. opId:" + transactionId, e2);
                if (callback != null) {
                    callback.onOperationComplete(1);
                }
            }
        } catch (android.os.RemoteException e3) {
            logAndPrintException(pw, "enableUsbDataWhileDocked:Failed to call onOperationComplete. opId:" + transactionId, e3);
        }
    }

    public boolean enableUsbData(java.lang.String portId, boolean enable, int transactionId, android.hardware.usb.IUsbOperationInternal callback, com.android.internal.util.IndentingPrintWriter pw) {
        java.util.Objects.requireNonNull(callback);
        java.util.Objects.requireNonNull(portId);
        com.android.server.usb.UsbPortManager.PortInfo portInfo = this.mPorts.get(portId);
        if (portInfo == null) {
            logAndPrint(6, pw, "enableUsbData: No such port: " + portId + " opId:" + transactionId);
            try {
                callback.onOperationComplete(3);
            } catch (android.os.RemoteException e) {
                logAndPrintException(pw, "enableUsbData: Failed to call OperationComplete. opId:" + transactionId, e);
            }
            return false;
        }
        try {
            try {
                return this.mUsbPortHal.enableUsbData(portId, enable, transactionId, callback);
            } catch (java.lang.Exception e2) {
                logAndPrintException(pw, "enableUsbData: Failed to invoke enableUsbData. opId:" + transactionId, e2);
                callback.onOperationComplete(1);
                return false;
            }
        } catch (android.os.RemoteException e3) {
            logAndPrintException(pw, "enableUsbData: Failed to call onOperationComplete. opId:" + transactionId, e3);
            return false;
        }
    }

    public int getUsbHalVersion() {
        if (this.mUsbPortHal == null) {
            return -2;
        }
        try {
            return this.mUsbPortHal.getUsbHalVersion();
        } catch (android.os.RemoteException e) {
            return -2;
        }
    }

    private int toHalUsbDataRole(int usbDataRole) {
        if (usbDataRole == 2) {
            return 2;
        }
        return 1;
    }

    private int toHalUsbPowerRole(int usbPowerRole) {
        if (usbPowerRole == 2) {
            return 2;
        }
        return 1;
    }

    private int toHalUsbMode(int usbMode) {
        if (usbMode == 1) {
            return 1;
        }
        return 2;
    }

    public void resetUsbPort(java.lang.String portId, int transactionId, android.hardware.usb.IUsbOperationInternal callback, com.android.internal.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            java.util.Objects.requireNonNull(callback);
            java.util.Objects.requireNonNull(portId);
            com.android.server.usb.UsbPortManager.PortInfo portInfo = this.mPorts.get(portId);
            if (portInfo == null) {
                logAndPrint(6, pw, "resetUsbPort: No such port: " + portId + " opId:" + transactionId);
                try {
                    callback.onOperationComplete(3);
                } catch (android.os.RemoteException e) {
                    logAndPrintException(pw, "resetUsbPort: Failed to call OperationComplete. opId:" + transactionId, e);
                }
                try {
                    try {
                        this.mUsbPortHal.resetUsbPort(portId, transactionId, callback);
                    } catch (java.lang.Exception e2) {
                        logAndPrintException(pw, "reseetUsbPort: Failed to resetUsbPort. opId:" + transactionId, e2);
                        callback.onOperationComplete(1);
                    }
                } catch (android.os.RemoteException e3) {
                    logAndPrintException(pw, "resetUsbPort: Failed to call onOperationComplete. opId:" + transactionId, e3);
                }
            } else {
                this.mUsbPortHal.resetUsbPort(portId, transactionId, callback);
            }
        }
    }

    public void setPortRoles(java.lang.String portId, int newPowerRole, int newDataRole, com.android.internal.util.IndentingPrintWriter pw) {
        int newMode;
        com.android.server.usb.hal.port.UsbPortHal usbPortHal;
        int halUsbMode;
        synchronized (this.mLock) {
            com.android.server.usb.UsbPortManager.PortInfo portInfo = this.mPorts.get(portId);
            if (portInfo == null) {
                if (pw != null) {
                    pw.println("No such USB port: " + portId);
                }
                return;
            }
            if (!portInfo.mUsbPortStatus.isRoleCombinationSupported(newPowerRole, newDataRole)) {
                logAndPrint(6, pw, "Attempted to set USB port into unsupported role combination: portId=" + portId + ", newPowerRole=" + android.hardware.usb.UsbPort.powerRoleToString(newPowerRole) + ", newDataRole=" + android.hardware.usb.UsbPort.dataRoleToString(newDataRole));
                return;
            }
            int currentDataRole = portInfo.mUsbPortStatus.getCurrentDataRole();
            int currentPowerRole = portInfo.mUsbPortStatus.getCurrentPowerRole();
            if (currentDataRole == newDataRole && currentPowerRole == newPowerRole) {
                if (pw != null) {
                    pw.println("No change.");
                }
                return;
            }
            boolean canChangeMode = portInfo.mCanChangeMode;
            boolean canChangePowerRole = portInfo.mCanChangePowerRole;
            boolean canChangeDataRole = portInfo.mCanChangeDataRole;
            int currentMode = portInfo.mUsbPortStatus.getCurrentMode();
            if ((!canChangePowerRole && currentPowerRole != newPowerRole) || (!canChangeDataRole && currentDataRole != newDataRole)) {
                if (canChangeMode && newPowerRole == 1 && newDataRole == 1) {
                    newMode = 2;
                } else if (canChangeMode && newPowerRole == 2 && newDataRole == 2) {
                    newMode = 1;
                } else {
                    logAndPrint(6, pw, "Found mismatch in supported USB role combinations while attempting to change role: " + portInfo + ", newPowerRole=" + android.hardware.usb.UsbPort.powerRoleToString(newPowerRole) + ", newDataRole=" + android.hardware.usb.UsbPort.dataRoleToString(newDataRole));
                    return;
                }
            } else {
                newMode = currentMode;
            }
            logAndPrint(4, pw, "Setting USB port mode and role: portId=" + portId + ", currentMode=" + android.hardware.usb.UsbPort.modeToString(currentMode) + ", currentPowerRole=" + android.hardware.usb.UsbPort.powerRoleToString(currentPowerRole) + ", currentDataRole=" + android.hardware.usb.UsbPort.dataRoleToString(currentDataRole) + ", newMode=" + android.hardware.usb.UsbPort.modeToString(newMode) + ", newPowerRole=" + android.hardware.usb.UsbPort.powerRoleToString(newPowerRole) + ", newDataRole=" + android.hardware.usb.UsbPort.dataRoleToString(newDataRole));
            com.android.server.usb.hal.port.RawPortInfo sim = this.mSimulatedPorts.get(portId);
            if (sim != null) {
                sim.currentMode = newMode;
                sim.currentPowerRole = newPowerRole;
                sim.currentDataRole = newDataRole;
                updatePortsLocked(pw, null);
            } else if (this.mUsbPortHal != null) {
                if (currentMode != newMode) {
                    logAndPrint(6, pw, "Trying to set the USB port mode: portId=" + portId + ", newMode=" + android.hardware.usb.UsbPort.modeToString(newMode));
                    try {
                        usbPortHal = this.mUsbPortHal;
                        halUsbMode = toHalUsbMode(newMode);
                    } catch (java.lang.Exception e) {
                        e = e;
                    }
                    try {
                        long j = this.mTransactionId + 1;
                        this.mTransactionId = j;
                        usbPortHal.switchMode(portId, halUsbMode, j);
                    } catch (java.lang.Exception e2) {
                        e = e2;
                        logAndPrintException(pw, "Failed to set the USB port mode: portId=" + portId + ", newMode=" + android.hardware.usb.UsbPort.modeToString(newMode), e);
                    }
                } else {
                    if (currentPowerRole != newPowerRole) {
                        try {
                            com.android.server.usb.hal.port.UsbPortHal usbPortHal2 = this.mUsbPortHal;
                            int halUsbPowerRole = toHalUsbPowerRole(newPowerRole);
                            long j2 = this.mTransactionId + 1;
                            this.mTransactionId = j2;
                            usbPortHal2.switchPowerRole(portId, halUsbPowerRole, j2);
                        } catch (java.lang.Exception e3) {
                            logAndPrintException(pw, "Failed to set the USB port power role: portId=" + portId + ", newPowerRole=" + android.hardware.usb.UsbPort.powerRoleToString(newPowerRole), e3);
                            return;
                        }
                    }
                    if (currentDataRole != newDataRole) {
                        try {
                            com.android.server.usb.hal.port.UsbPortHal usbPortHal3 = this.mUsbPortHal;
                            int halUsbDataRole = toHalUsbDataRole(newDataRole);
                            long j3 = this.mTransactionId + 1;
                            this.mTransactionId = j3;
                            usbPortHal3.switchDataRole(portId, halUsbDataRole, j3);
                        } catch (java.lang.Exception e4) {
                            logAndPrintException(pw, "Failed to set the USB port data role: portId=" + portId + ", newDataRole=" + android.hardware.usb.UsbPort.dataRoleToString(newDataRole), e4);
                        }
                    }
                }
            }
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        android.util.Slog.wtf(TAG, "binderDied() called unexpectedly");
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied(android.os.IBinder deadBinder) {
        synchronized (this.mDisplayPortListenerLock) {
            this.mDisplayPortListeners.remove(deadBinder);
            android.util.Slog.d(TAG, "DisplayPortEventDispatcherListener died at " + deadBinder);
        }
    }

    public boolean registerForDisplayPortEvents(android.hardware.usb.IDisplayPortAltModeInfoListener listener) {
        synchronized (this.mDisplayPortListenerLock) {
            if (this.mDisplayPortListeners.containsKey(listener.asBinder())) {
                return false;
            }
            try {
                listener.asBinder().linkToDeath(this, 0);
                this.mDisplayPortListeners.put(listener.asBinder(), listener);
                return true;
            } catch (android.os.RemoteException e) {
                logAndPrintException(null, "Caught RemoteException in registerForDisplayPortEvents: ", e);
                return false;
            }
        }
    }

    public void unregisterForDisplayPortEvents(android.hardware.usb.IDisplayPortAltModeInfoListener listener) {
        synchronized (this.mDisplayPortListenerLock) {
            if (this.mDisplayPortListeners.remove(listener.asBinder()) != null) {
                listener.asBinder().unlinkToDeath(this, 0);
            }
        }
    }

    public void updatePorts(java.util.ArrayList<com.android.server.usb.hal.port.RawPortInfo> newPortInfo) {
        android.os.Message message = this.mHandler.obtainMessage();
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putParcelableArrayList(PORT_INFO, newPortInfo);
        message.what = 1;
        message.setData(bundle);
        this.mHandler.sendMessage(message);
    }

    public void addSimulatedPort(java.lang.String portId, int supportedModes, boolean supportsComplianceWarnings, boolean supportsDisplayPortAltMode, com.android.internal.util.IndentingPrintWriter pw) throws java.lang.Throwable {
        android.hardware.usb.DisplayPortAltModeInfo displayPortAltModeInfo;
        java.lang.Object obj;
        int supportedAltModes = supportsDisplayPortAltMode ? 1 : 0;
        if (!supportsDisplayPortAltMode) {
            displayPortAltModeInfo = null;
        } else {
            android.hardware.usb.DisplayPortAltModeInfo displayPortAltModeInfo2 = new android.hardware.usb.DisplayPortAltModeInfo();
            displayPortAltModeInfo = displayPortAltModeInfo2;
        }
        java.lang.Object obj2 = this.mLock;
        synchronized (obj2) {
            try {
                try {
                    if (this.mSimulatedPorts.containsKey(portId)) {
                        pw.println("Port with same name already exists.  Please remove it first.");
                    } else {
                        pw.println("Adding simulated port: portId=" + portId + ", supportedModes=" + android.hardware.usb.UsbPort.modeToString(supportedModes));
                        obj = obj2;
                        try {
                            try {
                                this.mSimulatedPorts.put(portId, new com.android.server.usb.hal.port.RawPortInfo(portId, supportedModes, 0, 0, false, 0, false, 0, false, false, 0, false, 0, 0, false, 0, supportsComplianceWarnings, new int[0], 0, supportedAltModes, displayPortAltModeInfo));
                                updatePortsLocked(pw, null);
                            } catch (java.lang.Throwable th) {
                                th = th;
                                throw th;
                            }
                        } catch (java.lang.Throwable th2) {
                            th = th2;
                        }
                    }
                } catch (java.lang.Throwable th3) {
                    th = th3;
                }
            } catch (java.lang.Throwable th4) {
                th = th4;
                obj = obj2;
            }
        }
    }

    public void connectSimulatedPort(java.lang.String portId, int mode, boolean canChangeMode, int powerRole, boolean canChangePowerRole, int dataRole, boolean canChangeDataRole, com.android.internal.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            com.android.server.usb.hal.port.RawPortInfo portInfo = this.mSimulatedPorts.get(portId);
            if (portInfo == null) {
                pw.println("Cannot connect simulated port which does not exist.");
                return;
            }
            if (mode != 0 && powerRole != 0 && dataRole != 0) {
                if ((portInfo.supportedModes & mode) == 0) {
                    pw.println("Simulated port does not support mode: " + android.hardware.usb.UsbPort.modeToString(mode));
                    return;
                }
                pw.println("Connecting simulated port: portId=" + portId + ", mode=" + android.hardware.usb.UsbPort.modeToString(mode) + ", canChangeMode=" + canChangeMode + ", powerRole=" + android.hardware.usb.UsbPort.powerRoleToString(powerRole) + ", canChangePowerRole=" + canChangePowerRole + ", dataRole=" + android.hardware.usb.UsbPort.dataRoleToString(dataRole) + ", canChangeDataRole=" + canChangeDataRole);
                portInfo.currentMode = mode;
                portInfo.canChangeMode = canChangeMode;
                portInfo.currentPowerRole = powerRole;
                portInfo.canChangePowerRole = canChangePowerRole;
                portInfo.currentDataRole = dataRole;
                portInfo.canChangeDataRole = canChangeDataRole;
                updatePortsLocked(pw, null);
                return;
            }
            pw.println("Cannot connect simulated port in null mode, power role, or data role.");
        }
    }

    public void simulateContaminantStatus(java.lang.String portId, boolean detected, com.android.internal.util.IndentingPrintWriter pw) {
        int i;
        synchronized (this.mLock) {
            com.android.server.usb.hal.port.RawPortInfo portInfo = this.mSimulatedPorts.get(portId);
            if (portInfo == null) {
                pw.println("Simulated port not found.");
                return;
            }
            pw.println("Simulating wet port: portId=" + portId + ", wet=" + detected);
            if (detected) {
                i = 3;
            } else {
                i = 2;
            }
            portInfo.contaminantDetectionStatus = i;
            updatePortsLocked(pw, null);
        }
    }

    public void simulateComplianceWarnings(java.lang.String portId, java.lang.String complianceWarningsString, com.android.internal.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            com.android.server.usb.hal.port.RawPortInfo portInfo = this.mSimulatedPorts.get(portId);
            if (portInfo == null) {
                pw.println("Simulated port not found");
                return;
            }
            android.util.IntArray complianceWarnings = new android.util.IntArray();
            for (java.lang.String s : complianceWarningsString.split("[, ]")) {
                if (s.length() > 0) {
                    complianceWarnings.add(java.lang.Integer.parseInt(s));
                }
            }
            pw.println("Simulating Compliance Warnings: portId=" + portId + " Warnings=" + complianceWarningsString);
            portInfo.complianceWarnings = complianceWarnings.toArray();
            updatePortsLocked(pw, null);
        }
    }

    public void simulateDisplayPortAltModeInfo(java.lang.String portId, int partnerSinkStatus, int cableStatus, int numLanes, boolean hpd, int linkTrainingStatus, com.android.internal.util.IndentingPrintWriter pw) throws java.lang.Throwable {
        synchronized (this.mLock) {
            try {
                try {
                    com.android.server.usb.hal.port.RawPortInfo portInfo = this.mSimulatedPorts.get(portId);
                    if (portInfo == null) {
                        pw.println("Simulated port not found");
                        return;
                    }
                    android.hardware.usb.DisplayPortAltModeInfo displayPortAltModeInfo = new android.hardware.usb.DisplayPortAltModeInfo(partnerSinkStatus, cableStatus, numLanes, hpd, linkTrainingStatus);
                    portInfo.displayPortAltModeInfo = displayPortAltModeInfo;
                    pw.println("Simulating DisplayPort Info: " + displayPortAltModeInfo);
                    updatePortsLocked(pw, null);
                } catch (java.lang.Throwable th) {
                    th = th;
                    throw th;
                }
            } catch (java.lang.Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    public void disconnectSimulatedPort(java.lang.String portId, com.android.internal.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            com.android.server.usb.hal.port.RawPortInfo portInfo = this.mSimulatedPorts.get(portId);
            if (portInfo == null) {
                pw.println("Cannot disconnect simulated port which does not exist.");
                return;
            }
            pw.println("Disconnecting simulated port: portId=" + portId);
            portInfo.currentMode = 0;
            portInfo.canChangeMode = false;
            portInfo.currentPowerRole = 0;
            portInfo.canChangePowerRole = false;
            portInfo.currentDataRole = 0;
            portInfo.canChangeDataRole = false;
            updatePortsLocked(pw, null);
        }
    }

    public void removeSimulatedPort(java.lang.String portId, com.android.internal.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            int index = this.mSimulatedPorts.indexOfKey(portId);
            if (index < 0) {
                pw.println("Cannot remove simulated port which does not exist.");
                return;
            }
            pw.println("Disconnecting simulated port: portId=" + portId);
            this.mSimulatedPorts.removeAt(index);
            updatePortsLocked(pw, null);
        }
    }

    public void resetSimulation(com.android.internal.util.IndentingPrintWriter pw) {
        synchronized (this.mLock) {
            pw.println("Removing all simulated ports and ending simulation.");
            if (!this.mSimulatedPorts.isEmpty()) {
                this.mSimulatedPorts.clear();
                updatePortsLocked(pw, null);
            }
        }
    }

    public void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
        long token = dump.start(idName, id);
        synchronized (this.mLock) {
            dump.write("is_simulation_active", 1133871366145L, !this.mSimulatedPorts.isEmpty());
            for (com.android.server.usb.UsbPortManager.PortInfo portInfo : this.mPorts.values()) {
                portInfo.dump(dump, "usb_ports", 2246267895810L);
            }
            dump.write("usb_hal_version", 1159641169924L, getUsbHalVersion());
        }
        dump.end(token);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updatePortsLocked(com.android.internal.util.IndentingPrintWriter pw, java.util.ArrayList<com.android.server.usb.hal.port.RawPortInfo> newPortInfo) {
        com.android.internal.util.IndentingPrintWriter indentingPrintWriter;
        com.android.server.usb.UsbPortManager usbPortManager = this;
        int i = usbPortManager.mPorts.size();
        while (true) {
            int i2 = i - 1;
            if (i <= 0) {
                break;
            }
            usbPortManager.mPorts.valueAt(i2).mDisposition = 3;
            i = i2;
        }
        if (!usbPortManager.mSimulatedPorts.isEmpty()) {
            int i3 = 0;
            for (int count = usbPortManager.mSimulatedPorts.size(); i3 < count; count = count) {
                com.android.server.usb.hal.port.RawPortInfo portInfo = usbPortManager.mSimulatedPorts.valueAt(i3);
                usbPortManager = this;
                usbPortManager.addOrUpdatePortLocked(portInfo.portId, portInfo.supportedModes, portInfo.supportedContaminantProtectionModes, portInfo.currentMode, portInfo.canChangeMode, portInfo.currentPowerRole, portInfo.canChangePowerRole, portInfo.currentDataRole, portInfo.canChangeDataRole, portInfo.supportsEnableContaminantPresenceProtection, portInfo.contaminantProtectionStatus, portInfo.supportsEnableContaminantPresenceDetection, portInfo.contaminantDetectionStatus, portInfo.usbDataStatus, portInfo.powerTransferLimited, portInfo.powerBrickConnectionStatus, portInfo.supportsComplianceWarnings, portInfo.complianceWarnings, portInfo.plugState, portInfo.supportedAltModes, portInfo.displayPortAltModeInfo, pw);
                i3++;
            }
        } else {
            for (com.android.server.usb.hal.port.RawPortInfo currentPortInfo : newPortInfo) {
                addOrUpdatePortLocked(currentPortInfo.portId, currentPortInfo.supportedModes, currentPortInfo.supportedContaminantProtectionModes, currentPortInfo.currentMode, currentPortInfo.canChangeMode, currentPortInfo.currentPowerRole, currentPortInfo.canChangePowerRole, currentPortInfo.currentDataRole, currentPortInfo.canChangeDataRole, currentPortInfo.supportsEnableContaminantPresenceProtection, currentPortInfo.contaminantProtectionStatus, currentPortInfo.supportsEnableContaminantPresenceDetection, currentPortInfo.contaminantDetectionStatus, currentPortInfo.usbDataStatus, currentPortInfo.powerTransferLimited, currentPortInfo.powerBrickConnectionStatus, currentPortInfo.supportsComplianceWarnings, currentPortInfo.complianceWarnings, currentPortInfo.plugState, currentPortInfo.supportedAltModes, currentPortInfo.displayPortAltModeInfo, pw);
            }
        }
        int i4 = this.mPorts.size();
        while (true) {
            int i5 = i4 - 1;
            if (i4 > 0) {
                com.android.server.usb.UsbPortManager.PortInfo portInfo2 = this.mPorts.valueAt(i5);
                switch (portInfo2.mDisposition) {
                    case 0:
                        indentingPrintWriter = pw;
                        handlePortAddedLocked(portInfo2, indentingPrintWriter);
                        portInfo2.mDisposition = 2;
                        break;
                    case 1:
                        indentingPrintWriter = pw;
                        handlePortChangedLocked(portInfo2, indentingPrintWriter);
                        portInfo2.mDisposition = 2;
                        break;
                    case 2:
                    default:
                        indentingPrintWriter = pw;
                        break;
                    case 3:
                        this.mPorts.removeAt(i5);
                        portInfo2.mUsbPortStatus = null;
                        indentingPrintWriter = pw;
                        handlePortRemovedLocked(portInfo2, indentingPrintWriter);
                        break;
                }
                if (portInfo2.mComplianceWarningChange == 1) {
                    handlePortComplianceWarningLocked(portInfo2, indentingPrintWriter);
                }
                if (portInfo2.mDisplayPortAltModeChange == 1) {
                    handleDpAltModeLocked(portInfo2, indentingPrintWriter);
                }
                i4 = i5;
            } else {
                return;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x009c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void addOrUpdatePortLocked(java.lang.String r32, int r33, int r34, int r35, boolean r36, int r37, boolean r38, int r39, boolean r40, boolean r41, int r42, boolean r43, int r44, int r45, boolean r46, int r47, boolean r48, int[] r49, int r50, int r51, android.hardware.usb.DisplayPortAltModeInfo r52, com.android.internal.util.IndentingPrintWriter r53) {
        /*
            Method dump skipped, instruction units count: 444
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.usb.UsbPortManager.addOrUpdatePortLocked(java.lang.String, int, int, int, boolean, int, boolean, int, boolean, boolean, int, boolean, int, int, boolean, int, boolean, int[], int, int, android.hardware.usb.DisplayPortAltModeInfo, com.android.internal.util.IndentingPrintWriter):void");
    }

    private void handlePortLocked(com.android.server.usb.UsbPortManager.PortInfo portInfo, com.android.internal.util.IndentingPrintWriter pw) {
        sendPortChangedBroadcastLocked(portInfo);
        logToStatsd(portInfo, pw);
        updateContaminantNotification();
        ((com.android.server.usb.IOplusUsbDeviceFeature) android.common.OplusFeatureCache.getOrCreate(com.android.server.usb.IOplusUsbDeviceFeature.DEFAULT, new java.lang.Object[0])).sendPortChangeMessage(portInfo.mUsbPortStatus, false);
    }

    private void handlePortAddedLocked(com.android.server.usb.UsbPortManager.PortInfo portInfo, com.android.internal.util.IndentingPrintWriter pw) {
        logAndPrint(4, pw, "USB port added: " + portInfo);
        handlePortLocked(portInfo, pw);
    }

    private void handlePortChangedLocked(com.android.server.usb.UsbPortManager.PortInfo portInfo, com.android.internal.util.IndentingPrintWriter pw) {
        logAndPrint(4, pw, "USB port changed: " + portInfo);
        enableContaminantDetectionIfNeeded(portInfo, pw);
        disableLimitPowerTransferIfNeeded(portInfo, pw);
        handlePortLocked(portInfo, pw);
    }

    private void handlePortComplianceWarningLocked(com.android.server.usb.UsbPortManager.PortInfo portInfo, com.android.internal.util.IndentingPrintWriter pw) {
        logAndPrint(4, pw, "USB port compliance warning changed: " + portInfo);
        logToStatsdComplianceWarnings(portInfo);
        sendComplianceWarningBroadcastLocked(portInfo);
    }

    private void handleDpAltModeLocked(com.android.server.usb.UsbPortManager.PortInfo portInfo, com.android.internal.util.IndentingPrintWriter pw) {
        logAndPrint(4, pw, "USB port DisplayPort Alt Mode Status Changed: " + portInfo);
        sendDpAltModeCallbackLocked(portInfo, pw);
    }

    private void handlePortRemovedLocked(com.android.server.usb.UsbPortManager.PortInfo portInfo, com.android.internal.util.IndentingPrintWriter pw) {
        logAndPrint(4, pw, "USB port removed: " + portInfo);
        handlePortLocked(portInfo, pw);
    }

    private static int convertContaminantDetectionStatusToProto(int contaminantDetectionStatus) {
        switch (contaminantDetectionStatus) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            default:
                return 0;
        }
    }

    private static int[] toStatsLogConstant(int[] complianceWarnings) {
        android.util.IntArray complianceWarningsProto = new android.util.IntArray();
        for (int warning : complianceWarnings) {
            switch (warning) {
                case 1:
                    complianceWarningsProto.add(4);
                    break;
                case 2:
                    complianceWarningsProto.add(1);
                    break;
                case 3:
                    complianceWarningsProto.add(2);
                    break;
                case 4:
                    complianceWarningsProto.add(3);
                    break;
                case 5:
                    complianceWarningsProto.add(5);
                    break;
                case 6:
                    complianceWarningsProto.add(6);
                    break;
                case 7:
                    complianceWarningsProto.add(7);
                    break;
                case 8:
                    complianceWarningsProto.add(8);
                    break;
                case 9:
                    complianceWarningsProto.add(9);
                    break;
            }
        }
        return complianceWarningsProto.toArray();
    }

    private void sendPortChangedBroadcastLocked(com.android.server.usb.UsbPortManager.PortInfo portInfo) {
        final android.content.Intent intent = new android.content.Intent("android.hardware.usb.action.USB_PORT_CHANGED");
        intent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.EVRCB);
        intent.putExtra("port", (android.os.Parcelable) android.hardware.usb.ParcelableUsbPort.of(portInfo.mUsbPort));
        intent.putExtra("portStatus", (android.os.Parcelable) portInfo.mUsbPortStatus);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.usb.UsbPortManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendPortChangedBroadcastLocked$0(intent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendPortChangedBroadcastLocked$0(android.content.Intent intent) {
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL, "android.permission.MANAGE_USB");
    }

    private void sendComplianceWarningBroadcastLocked(com.android.server.usb.UsbPortManager.PortInfo portInfo) {
        if (portInfo.mComplianceWarningChange == 0) {
            return;
        }
        final android.content.Intent intent = new android.content.Intent("android.hardware.usb.action.USB_PORT_COMPLIANCE_CHANGED");
        intent.addFlags(android.hardware.audio.common.V2_0.AudioFormat.EVRCB);
        intent.putExtra("port", (android.os.Parcelable) android.hardware.usb.ParcelableUsbPort.of(portInfo.mUsbPort));
        intent.putExtra("portStatus", (android.os.Parcelable) portInfo.mUsbPortStatus);
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.usb.UsbPortManager$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendComplianceWarningBroadcastLocked$1(intent);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendComplianceWarningBroadcastLocked$1(android.content.Intent intent) {
        this.mContext.sendBroadcastAsUser(intent, android.os.UserHandle.ALL, "android.permission.MANAGE_USB");
    }

    private void sendDpAltModeCallbackLocked(com.android.server.usb.UsbPortManager.PortInfo portInfo, com.android.internal.util.IndentingPrintWriter pw) {
        java.lang.String portId = portInfo.mUsbPort.getId();
        synchronized (this.mDisplayPortListenerLock) {
            for (android.hardware.usb.IDisplayPortAltModeInfoListener mListener : this.mDisplayPortListeners.values()) {
                try {
                    mListener.onDisplayPortAltModeInfoChanged(portId, portInfo.mUsbPortStatus.getDisplayPortAltModeInfo());
                } catch (android.os.RemoteException e) {
                    logAndPrintException(pw, "Caught RemoteException at sendDpAltModeCallbackLocked", e);
                }
            }
        }
    }

    private void enableContaminantDetectionIfNeeded(com.android.server.usb.UsbPortManager.PortInfo portInfo, com.android.internal.util.IndentingPrintWriter pw) {
        if (this.mConnected.containsKey(portInfo.mUsbPort.getId()) && this.mConnected.get(portInfo.mUsbPort.getId()).booleanValue() && !portInfo.mUsbPortStatus.isConnected() && portInfo.mUsbPortStatus.getContaminantDetectionStatus() == 1) {
            enableContaminantDetection(portInfo.mUsbPort.getId(), true, pw);
        }
    }

    private void disableLimitPowerTransferIfNeeded(com.android.server.usb.UsbPortManager.PortInfo portInfo, com.android.internal.util.IndentingPrintWriter pw) {
        if (this.mConnected.containsKey(portInfo.mUsbPort.getId()) && this.mConnected.get(portInfo.mUsbPort.getId()).booleanValue() && !portInfo.mUsbPortStatus.isConnected() && portInfo.mUsbPortStatus.isPowerTransferLimited()) {
            java.lang.String id = portInfo.mUsbPort.getId();
            long j = this.mTransactionId + 1;
            this.mTransactionId = j;
            enableLimitPowerTransfer(id, false, j, null, pw);
        }
    }

    private void logToStatsd(com.android.server.usb.UsbPortManager.PortInfo portInfo, com.android.internal.util.IndentingPrintWriter pw) {
        if (portInfo.mUsbPortStatus == null) {
            if (this.mConnected.containsKey(portInfo.mUsbPort.getId())) {
                if (this.mConnected.get(portInfo.mUsbPort.getId()).booleanValue()) {
                    com.android.internal.util.FrameworkStatsLog.write(70, 0, portInfo.mUsbPort.getId(), portInfo.mLastConnectDurationMillis);
                }
                this.mConnected.remove(portInfo.mUsbPort.getId());
            }
            if (this.mContaminantStatus.containsKey(portInfo.mUsbPort.getId())) {
                if (this.mContaminantStatus.get(portInfo.mUsbPort.getId()).intValue() == 3) {
                    com.android.internal.util.FrameworkStatsLog.write(146, portInfo.mUsbPort.getId(), convertContaminantDetectionStatusToProto(2));
                }
                this.mContaminantStatus.remove(portInfo.mUsbPort.getId());
                return;
            }
            return;
        }
        if (!this.mConnected.containsKey(portInfo.mUsbPort.getId()) || this.mConnected.get(portInfo.mUsbPort.getId()).booleanValue() != portInfo.mUsbPortStatus.isConnected()) {
            this.mConnected.put(portInfo.mUsbPort.getId(), java.lang.Boolean.valueOf(portInfo.mUsbPortStatus.isConnected()));
            com.android.internal.util.FrameworkStatsLog.write(70, portInfo.mUsbPortStatus.isConnected() ? 1 : 0, portInfo.mUsbPort.getId(), portInfo.mLastConnectDurationMillis);
        }
        if (!this.mContaminantStatus.containsKey(portInfo.mUsbPort.getId()) || this.mContaminantStatus.get(portInfo.mUsbPort.getId()).intValue() != portInfo.mUsbPortStatus.getContaminantDetectionStatus()) {
            this.mContaminantStatus.put(portInfo.mUsbPort.getId(), java.lang.Integer.valueOf(portInfo.mUsbPortStatus.getContaminantDetectionStatus()));
            com.android.internal.util.FrameworkStatsLog.write(146, portInfo.mUsbPort.getId(), convertContaminantDetectionStatusToProto(portInfo.mUsbPortStatus.getContaminantDetectionStatus()));
        }
    }

    private void logToStatsdComplianceWarnings(com.android.server.usb.UsbPortManager.PortInfo portInfo) {
        if (portInfo.mUsbPortStatus == null || portInfo.mUsbPortStatus.getComplianceWarnings().length == 0) {
            return;
        }
        com.android.internal.util.FrameworkStatsLog.write(com.android.internal.util.FrameworkStatsLog.USB_COMPLIANCE_WARNINGS_REPORTED, portInfo.mUsbPort.getId(), toStatsLogConstant(portInfo.mUsbPortStatus.getComplianceWarnings()));
    }

    public static void logAndPrint(int priority, com.android.internal.util.IndentingPrintWriter pw, java.lang.String msg) {
        android.util.Slog.println(priority, TAG, msg);
        if (pw != null) {
            pw.println(msg);
        }
    }

    public static void logAndPrintException(com.android.internal.util.IndentingPrintWriter pw, java.lang.String msg, java.lang.Exception e) {
        android.util.Slog.e(TAG, msg, e);
        if (pw != null) {
            pw.println(msg + e);
        }
    }

    public static final class PortInfo {
        public static final int ALTMODE_INFO_CHANGED = 1;
        public static final int ALTMODE_INFO_UNCHANGED = 0;
        public static final int COMPLIANCE_WARNING_CHANGED = 1;
        public static final int COMPLIANCE_WARNING_UNCHANGED = 0;
        public static final int DISPOSITION_ADDED = 0;
        public static final int DISPOSITION_CHANGED = 1;
        public static final int DISPOSITION_READY = 2;
        public static final int DISPOSITION_REMOVED = 3;
        public boolean mCanChangeDataRole;
        public boolean mCanChangeMode;
        public boolean mCanChangePowerRole;
        public long mConnectedAtMillis;
        public int mDisposition;
        public long mLastConnectDurationMillis;
        public final android.hardware.usb.UsbPort mUsbPort;
        public android.hardware.usb.UsbPortStatus mUsbPortStatus;
        public int mComplianceWarningChange = 0;
        public int mDisplayPortAltModeChange = 0;

        PortInfo(android.hardware.usb.UsbManager usbManager, java.lang.String portId, int supportedModes, int supportedContaminantProtectionModes, boolean supportsEnableContaminantPresenceDetection, boolean supportsEnableContaminantPresenceProtection, boolean supportsComplianceWarnings, int supportedAltModes) {
            this.mUsbPort = new android.hardware.usb.UsbPort(usbManager, portId, supportedModes, supportedContaminantProtectionModes, supportsEnableContaminantPresenceDetection, supportsEnableContaminantPresenceProtection, supportsComplianceWarnings, supportedAltModes);
        }

        public boolean complianceWarningsChanged(int[] complianceWarnings) {
            if (java.util.Arrays.equals(complianceWarnings, this.mUsbPortStatus.getComplianceWarnings())) {
                this.mComplianceWarningChange = 0;
                return false;
            }
            this.mComplianceWarningChange = 1;
            return true;
        }

        public boolean displayPortAltModeChanged(android.hardware.usb.DisplayPortAltModeInfo displayPortAltModeInfo) {
            android.hardware.usb.DisplayPortAltModeInfo currentDisplayPortAltModeInfo = this.mUsbPortStatus.getDisplayPortAltModeInfo();
            this.mDisplayPortAltModeChange = 0;
            if (displayPortAltModeInfo == null && currentDisplayPortAltModeInfo != null) {
                this.mDisplayPortAltModeChange = 1;
                return true;
            }
            if (currentDisplayPortAltModeInfo == null) {
                if (displayPortAltModeInfo == null) {
                    return false;
                }
                this.mDisplayPortAltModeChange = 1;
                return true;
            }
            if (currentDisplayPortAltModeInfo.equals(displayPortAltModeInfo)) {
                return false;
            }
            this.mDisplayPortAltModeChange = 1;
            return true;
        }

        public boolean setStatus(int currentMode, boolean canChangeMode, int currentPowerRole, boolean canChangePowerRole, int currentDataRole, boolean canChangeDataRole, int supportedRoleCombinations) {
            boolean dispositionChanged = false;
            this.mCanChangeMode = canChangeMode;
            this.mCanChangePowerRole = canChangePowerRole;
            this.mCanChangeDataRole = canChangeDataRole;
            if (this.mUsbPortStatus == null || this.mUsbPortStatus.getCurrentMode() != currentMode || this.mUsbPortStatus.getCurrentPowerRole() != currentPowerRole || this.mUsbPortStatus.getCurrentDataRole() != currentDataRole || this.mUsbPortStatus.getSupportedRoleCombinations() != supportedRoleCombinations) {
                this.mUsbPortStatus = new android.hardware.usb.UsbPortStatus(currentMode, currentPowerRole, currentDataRole, supportedRoleCombinations, 0, 0, 0, false, 0, new int[0], 0, (android.hardware.usb.DisplayPortAltModeInfo) null);
                dispositionChanged = true;
            }
            if (this.mUsbPortStatus.isConnected() && this.mConnectedAtMillis == 0) {
                this.mConnectedAtMillis = android.os.SystemClock.elapsedRealtime();
                this.mLastConnectDurationMillis = 0L;
            } else if (!this.mUsbPortStatus.isConnected() && this.mConnectedAtMillis != 0) {
                this.mLastConnectDurationMillis = android.os.SystemClock.elapsedRealtime() - this.mConnectedAtMillis;
                this.mConnectedAtMillis = 0L;
            }
            return dispositionChanged;
        }

        public boolean setStatus(int currentMode, boolean canChangeMode, int currentPowerRole, boolean canChangePowerRole, int currentDataRole, boolean canChangeDataRole, int supportedRoleCombinations, int contaminantProtectionStatus, int contaminantDetectionStatus, int usbDataStatus, boolean powerTransferLimited, int powerBrickConnectionStatus) {
            boolean dispositionChanged = false;
            this.mCanChangeMode = canChangeMode;
            this.mCanChangePowerRole = canChangePowerRole;
            this.mCanChangeDataRole = canChangeDataRole;
            if (this.mUsbPortStatus == null || this.mUsbPortStatus.getCurrentMode() != currentMode || this.mUsbPortStatus.getCurrentPowerRole() != currentPowerRole || this.mUsbPortStatus.getCurrentDataRole() != currentDataRole || this.mUsbPortStatus.getSupportedRoleCombinations() != supportedRoleCombinations || this.mUsbPortStatus.getContaminantProtectionStatus() != contaminantProtectionStatus || this.mUsbPortStatus.getContaminantDetectionStatus() != contaminantDetectionStatus || this.mUsbPortStatus.getUsbDataStatus() != usbDataStatus || this.mUsbPortStatus.isPowerTransferLimited() != powerTransferLimited || this.mUsbPortStatus.getPowerBrickConnectionStatus() != powerBrickConnectionStatus) {
                this.mUsbPortStatus = new android.hardware.usb.UsbPortStatus(currentMode, currentPowerRole, currentDataRole, supportedRoleCombinations, contaminantProtectionStatus, contaminantDetectionStatus, usbDataStatus, powerTransferLimited, powerBrickConnectionStatus, new int[0], 0, (android.hardware.usb.DisplayPortAltModeInfo) null);
                dispositionChanged = true;
            }
            if (this.mUsbPortStatus.isConnected() && this.mConnectedAtMillis == 0) {
                this.mConnectedAtMillis = android.os.SystemClock.elapsedRealtime();
                this.mLastConnectDurationMillis = 0L;
            } else if (!this.mUsbPortStatus.isConnected() && this.mConnectedAtMillis != 0) {
                this.mLastConnectDurationMillis = android.os.SystemClock.elapsedRealtime() - this.mConnectedAtMillis;
                this.mConnectedAtMillis = 0L;
            }
            return dispositionChanged;
        }

        public boolean setStatus(int currentMode, boolean canChangeMode, int currentPowerRole, boolean canChangePowerRole, int currentDataRole, boolean canChangeDataRole, int supportedRoleCombinations, int contaminantProtectionStatus, int contaminantDetectionStatus, int usbDataStatus, boolean powerTransferLimited, int powerBrickConnectionStatus, int[] complianceWarnings, int plugState, android.hardware.usb.DisplayPortAltModeInfo displayPortAltModeInfo) {
            boolean complianceChanged;
            boolean displayPortChanged;
            boolean dispositionChanged;
            if (this.mUsbPortStatus == null) {
                complianceChanged = false;
                displayPortChanged = false;
            } else {
                boolean complianceChanged2 = complianceWarningsChanged(complianceWarnings);
                boolean displayPortChanged2 = displayPortAltModeChanged(displayPortAltModeInfo);
                complianceChanged = complianceChanged2;
                displayPortChanged = displayPortChanged2;
            }
            this.mCanChangeMode = canChangeMode;
            this.mCanChangePowerRole = canChangePowerRole;
            this.mCanChangeDataRole = canChangeDataRole;
            if (this.mUsbPortStatus == null || this.mUsbPortStatus.getCurrentMode() != currentMode || this.mUsbPortStatus.getCurrentPowerRole() != currentPowerRole || this.mUsbPortStatus.getCurrentDataRole() != currentDataRole || this.mUsbPortStatus.getSupportedRoleCombinations() != supportedRoleCombinations || this.mUsbPortStatus.getContaminantProtectionStatus() != contaminantProtectionStatus || this.mUsbPortStatus.getContaminantDetectionStatus() != contaminantDetectionStatus || this.mUsbPortStatus.getUsbDataStatus() != usbDataStatus || this.mUsbPortStatus.isPowerTransferLimited() != powerTransferLimited || this.mUsbPortStatus.getPowerBrickConnectionStatus() != powerBrickConnectionStatus || this.mUsbPortStatus.getPlugState() != plugState) {
                if (this.mUsbPortStatus == null && complianceWarnings.length > 0) {
                    this.mComplianceWarningChange = 1;
                }
                this.mUsbPortStatus = new android.hardware.usb.UsbPortStatus(currentMode, currentPowerRole, currentDataRole, supportedRoleCombinations, contaminantProtectionStatus, contaminantDetectionStatus, usbDataStatus, powerTransferLimited, powerBrickConnectionStatus, complianceWarnings, plugState, displayPortAltModeInfo);
                dispositionChanged = true;
            } else {
                if (complianceChanged || displayPortChanged) {
                    this.mUsbPortStatus = new android.hardware.usb.UsbPortStatus(currentMode, currentPowerRole, currentDataRole, supportedRoleCombinations, contaminantProtectionStatus, contaminantDetectionStatus, usbDataStatus, powerTransferLimited, powerBrickConnectionStatus, complianceWarnings, plugState, displayPortAltModeInfo);
                }
                dispositionChanged = false;
            }
            if (this.mUsbPortStatus.isConnected() && this.mConnectedAtMillis == 0) {
                this.mConnectedAtMillis = android.os.SystemClock.elapsedRealtime();
                this.mLastConnectDurationMillis = 0L;
            } else if (!this.mUsbPortStatus.isConnected() && this.mConnectedAtMillis != 0) {
                this.mLastConnectDurationMillis = android.os.SystemClock.elapsedRealtime() - this.mConnectedAtMillis;
                this.mConnectedAtMillis = 0L;
            }
            return dispositionChanged;
        }

        void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
            long token = dump.start(idName, id);
            com.android.internal.usb.DumpUtils.writePort(dump, "port", 1146756268033L, this.mUsbPort);
            com.android.internal.usb.DumpUtils.writePortStatus(dump, "status", 1146756268034L, this.mUsbPortStatus);
            dump.write("can_change_mode", 1133871366147L, this.mCanChangeMode);
            dump.write("can_change_power_role", 1133871366148L, this.mCanChangePowerRole);
            dump.write("can_change_data_role", 1133871366149L, this.mCanChangeDataRole);
            dump.write("connected_at_millis", 1112396529670L, this.mConnectedAtMillis);
            dump.write("last_connect_duration_millis", 1112396529671L, this.mLastConnectDurationMillis);
            dump.end(token);
        }

        public java.lang.String toString() {
            return "port=" + this.mUsbPort + ", status=" + this.mUsbPortStatus + ", canChangeMode=" + this.mCanChangeMode + ", canChangePowerRole=" + this.mCanChangePowerRole + ", canChangeDataRole=" + this.mCanChangeDataRole + ", connectedAtMillis=" + this.mConnectedAtMillis + ", lastConnectDurationMillis=" + this.mLastConnectDurationMillis;
        }
    }
}
