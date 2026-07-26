package com.android.server;

/* JADX INFO: loaded from: classes.dex */
final class WiredAccessoryManager implements com.android.server.input.InputManagerService.WiredAccessoryCallbacks {
    private static final int BIT_HDMI_AUDIO = 16;
    private static final int BIT_HEADSET = 2;
    private static final int BIT_HEADSET_NO_MIC = 1;
    private static final int BIT_LINEOUT = 32;
    private static final int BIT_USB_HEADSET_ANLG = 4;
    private static final int BIT_USB_HEADSET_DGTL = 8;
    private static final java.lang.String[] DP_AUDIO_CONNS;
    private static final java.lang.String INTF_DP = "DP";
    private static final java.lang.String INTF_HDMI = "HDMI";
    private static final boolean LOG;
    private static final int MSG_NEW_DEVICE_STATE = 1;
    private static final int MSG_SYSTEM_READY = 2;
    private static final java.lang.String NAME_DP_AUDIO = "soc:qcom,msm-ext-disp";
    private static final java.lang.String NAME_H2W = "h2w";
    private static final java.lang.String NAME_HDMI = "hdmi";
    private static final java.lang.String NAME_HDMI_AUDIO = "hdmi_audio";
    private static final java.lang.String NAME_USB_AUDIO = "usb_audio";
    private static final int SUPPORTED_HEADSETS = 63;
    private static final java.lang.String TAG = com.android.server.WiredAccessoryManager.class.getSimpleName();
    private final android.media.AudioManager mAudioManager;
    private int mDpCount;
    private final com.android.server.WiredAccessoryManager.WiredAccessoryExtconObserver mExtconObserver;
    private int mHeadsetState;
    private final com.android.server.input.InputManagerService mInputManager;
    private final com.android.server.WiredAccessoryManager.WiredAccessoryObserver mObserver;
    private int mSwitchValues;
    private final boolean mUseDevInputEventForAudioJack;
    private final android.os.PowerManager.WakeLock mWakeLock;
    private final java.lang.Object mLock = new java.lang.Object();
    private java.lang.String mDetectedIntf = INTF_DP;
    private final android.os.Handler mHandler = new android.os.Handler(android.os.Looper.myLooper(), null, true) { // from class: com.android.server.WiredAccessoryManager.1
        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    com.android.server.WiredAccessoryManager.this.setDevicesState(msg.arg1, msg.arg2, (java.lang.String) msg.obj);
                    com.android.server.WiredAccessoryManager.this.mWakeLock.release();
                    break;
                case 2:
                    com.android.server.WiredAccessoryManager.this.onSystemReady();
                    com.android.server.WiredAccessoryManager.this.mWakeLock.release();
                    break;
            }
        }
    };

    static {
        LOG = "eng".equals(android.os.Build.TYPE) || "userdebug".equals(android.os.Build.TYPE);
        DP_AUDIO_CONNS = new java.lang.String[]{"soc:qcom,msm-ext-disp/3/1", "soc:qcom,msm-ext-disp/2/1", "soc:qcom,msm-ext-disp/1/1", "soc:qcom,msm-ext-disp/0/1", "soc:qcom,msm-ext-disp/3/0", "soc:qcom,msm-ext-disp/2/0", "soc:qcom,msm-ext-disp/1/0", "soc:qcom,msm-ext-disp/0/0"};
    }

    public WiredAccessoryManager(android.content.Context context, com.android.server.input.InputManagerService inputManager) {
        android.os.PowerManager pm = (android.os.PowerManager) context.getSystemService("power");
        this.mWakeLock = pm.newWakeLock(1, "WiredAccessoryManager");
        this.mWakeLock.setReferenceCounted(false);
        this.mAudioManager = (android.media.AudioManager) context.getSystemService("audio");
        this.mInputManager = inputManager;
        this.mUseDevInputEventForAudioJack = context.getResources().getBoolean(android.R.bool.config_supportsMicToggle);
        this.mExtconObserver = new com.android.server.WiredAccessoryManager.WiredAccessoryExtconObserver();
        this.mObserver = new com.android.server.WiredAccessoryManager.WiredAccessoryObserver();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onSystemReady() {
        if (this.mUseDevInputEventForAudioJack) {
            int switchValues = 0;
            if (this.mInputManager.getSwitchState(-1, -256, 2) == 1) {
                switchValues = 0 | 4;
            }
            if (this.mInputManager.getSwitchState(-1, -256, 4) == 1) {
                switchValues |= 16;
            }
            if (this.mInputManager.getSwitchState(-1, -256, 6) == 1) {
                switchValues |= 64;
            }
            notifyWiredAccessoryChanged(0L, switchValues, 84);
        }
        if (com.android.server.ExtconUEventObserver.extconExists()) {
            this.mExtconObserver.uEventCount();
        }
        this.mObserver.init();
    }

    @Override // com.android.server.input.InputManagerService.WiredAccessoryCallbacks
    public void notifyWiredAccessoryChanged(long whenNanos, int switchValues, int switchMask) {
        int headset;
        if (LOG) {
            android.util.Slog.v(TAG, "notifyWiredAccessoryChanged: when=" + whenNanos + " bits=" + switchCodeToString(switchValues, switchMask) + " mask=" + java.lang.Integer.toHexString(switchMask));
        }
        synchronized (this.mLock) {
            this.mSwitchValues = (this.mSwitchValues & (~switchMask)) | switchValues;
            switch (this.mSwitchValues & 84) {
                case 0:
                    headset = 0;
                    break;
                case 4:
                    headset = 1;
                    break;
                case 16:
                    headset = 2;
                    break;
                case 20:
                    headset = 2;
                    break;
                case 64:
                    headset = 32;
                    break;
                default:
                    headset = 0;
                    break;
            }
            updateLocked(NAME_H2W, "", (this.mHeadsetState & (-36)) | headset);
        }
    }

    @Override // com.android.server.input.InputManagerService.WiredAccessoryCallbacks
    public void systemReady() {
        synchronized (this.mLock) {
            this.mWakeLock.acquire();
            android.os.Message msg = this.mHandler.obtainMessage(2, 0, 0, null);
            this.mHandler.sendMessage(msg);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLocked(java.lang.String newName, java.lang.String address, int newState) {
        android.os.Message msg;
        int headsetState = newState & 63;
        int newDpState = newState & 16;
        int usb_headset_anlg = headsetState & 4;
        int usb_headset_dgtl = headsetState & 8;
        int h2w_headset = headsetState & 35;
        boolean h2wStateChange = true;
        boolean usbStateChange = true;
        boolean dpBitState = (this.mHeadsetState & 16) > 0;
        boolean dpCountState = this.mDpCount != 0;
        if (LOG) {
            android.util.Slog.v(TAG, "newName=" + newName + " newState=" + newState + " headsetState=" + headsetState + " prev headsetState=" + this.mHeadsetState + " num of active dp conns= " + this.mDpCount);
        }
        if (this.mHeadsetState == headsetState && !newName.startsWith(NAME_DP_AUDIO)) {
            android.util.Log.e(TAG, "No state change.");
            return;
        }
        if (h2w_headset == 35) {
            android.util.Log.e(TAG, "Invalid combination, unsetting h2w flag");
            h2wStateChange = false;
        }
        if (usb_headset_anlg == 4 && usb_headset_dgtl == 8) {
            android.util.Log.e(TAG, "Invalid combination, unsetting usb flag");
            usbStateChange = false;
        }
        if (!h2wStateChange && !usbStateChange) {
            android.util.Log.e(TAG, "invalid transition, returning ...");
            return;
        }
        if (newName.startsWith(NAME_DP_AUDIO)) {
            if (newDpState > 0 && this.mDpCount < DP_AUDIO_CONNS.length && dpBitState == dpCountState) {
                this.mDpCount++;
            } else if (newDpState == 0 && this.mDpCount > 0) {
                this.mDpCount--;
            } else {
                android.util.Log.e(TAG, "No state change for DP.");
                return;
            }
        }
        this.mWakeLock.acquire();
        android.util.Log.i(TAG, "MSG_NEW_DEVICE_STATE");
        if (!newName.startsWith(NAME_DP_AUDIO)) {
            msg = this.mHandler.obtainMessage(1, headsetState, this.mHeadsetState, newName + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + address);
        } else {
            int pseudoHeadsetState = this.mHeadsetState;
            if (dpBitState && newDpState != 0) {
                pseudoHeadsetState = this.mHeadsetState & (-17);
            }
            msg = this.mHandler.obtainMessage(1, headsetState, pseudoHeadsetState, "soc:qcom,msm-ext-disp/" + address);
            if (headsetState == 0 && this.mDpCount != 0) {
                headsetState |= 16;
            }
        }
        this.mHandler.sendMessage(msg);
        this.mHeadsetState = headsetState;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setDevicesState(int headsetState, int prevHeadsetState, java.lang.String headsetNameAddr) {
        synchronized (this.mLock) {
            int allHeadsets = 63;
            int curHeadset = 1;
            while (allHeadsets != 0) {
                if ((curHeadset & allHeadsets) != 0) {
                    setDeviceStateLocked(curHeadset, headsetState, prevHeadsetState, headsetNameAddr);
                    allHeadsets &= ~curHeadset;
                }
                curHeadset <<= 1;
            }
        }
    }

    private void setDeviceStateLocked(int headset, int headsetState, int prevHeadsetState, java.lang.String headsetNameAddr) {
        int state;
        int outDevice;
        if ((headsetState & headset) != (prevHeadsetState & headset)) {
            int inDevice = 0;
            if ((headsetState & headset) != 0) {
                state = 1;
            } else {
                state = 0;
            }
            if (headset == 2) {
                outDevice = 4;
                inDevice = android.hardware.audio.common.V2_0.AudioDevice.IN_WIRED_HEADSET;
            } else if (headset == 1) {
                outDevice = 8;
            } else if (headset == 32) {
                outDevice = 131072;
            } else if (headset == 4) {
                outDevice = 2048;
            } else if (headset == 8) {
                outDevice = 4096;
            } else if (headset == 16) {
                outDevice = 1024;
            } else {
                android.util.Slog.e(TAG, "setDeviceState() invalid headset type: " + headset);
                return;
            }
            if (LOG) {
                android.util.Slog.v(TAG, "headset: " + headsetNameAddr + (state == 1 ? " connected" : " disconnected"));
            }
            java.lang.String[] hs = headsetNameAddr.split(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER);
            if (outDevice != 0) {
                if (LOG) {
                    android.util.Slog.v(TAG, "Output device address " + (hs.length > 1 ? hs[1] : "") + " name " + hs[0]);
                }
                if (prevHeadsetState == 1 && headsetState == 2 && outDevice == 8) {
                    state = 2;
                }
                this.mAudioManager.setWiredDeviceConnectionState(outDevice, state, hs.length > 1 ? hs[1] : "", hs[0]);
            }
            if (inDevice != 0) {
                this.mAudioManager.setWiredDeviceConnectionState(inDevice, state, hs.length > 1 ? hs[1] : "", hs[0]);
            }
        }
    }

    private java.lang.String switchCodeToString(int switchValues, int switchMask) {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if ((switchMask & 4) != 0 && (switchValues & 4) != 0) {
            sb.append("SW_HEADPHONE_INSERT ");
        }
        if ((switchMask & 16) != 0 && (switchValues & 16) != 0) {
            sb.append("SW_MICROPHONE_INSERT");
        }
        return sb.toString();
    }

    class WiredAccessoryObserver extends android.os.UEventObserver {
        private java.util.List<java.lang.String> mDevPath = new java.util.ArrayList();
        private final java.util.List<com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo> mUEventInfo = makeObservedUEventList();

        public WiredAccessoryObserver() {
        }

        void init() {
            synchronized (com.android.server.WiredAccessoryManager.this.mLock) {
                if (com.android.server.WiredAccessoryManager.LOG) {
                    android.util.Slog.v(com.android.server.WiredAccessoryManager.TAG, "init()");
                }
                char[] buffer = new char[1024];
                for (int i = 0; i < this.mUEventInfo.size(); i++) {
                    com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo uei = this.mUEventInfo.get(i);
                    try {
                        java.lang.String switchStatePath = uei.getSwitchStatePath();
                        java.io.FileReader file = new java.io.FileReader(switchStatePath);
                        int len = file.read(buffer, 0, 1024);
                        file.close();
                        int curState = java.lang.Integer.parseInt(new java.lang.String(buffer, 0, len).trim());
                        if (curState > 0) {
                            int index = switchStatePath.lastIndexOf(".");
                            if (switchStatePath.substring(index + 1, index + 2).equals("1")) {
                                com.android.server.WiredAccessoryManager.this.mDetectedIntf = "HDMI";
                            }
                            updateStateLocked(uei.getDevPath(), uei.getDevName(), curState);
                        }
                    } catch (java.io.FileNotFoundException e) {
                        android.util.Slog.w(com.android.server.WiredAccessoryManager.TAG, uei.getSwitchStatePath() + " not found while attempting to determine initial switch state");
                    } catch (java.lang.Exception e2) {
                        android.util.Slog.e(com.android.server.WiredAccessoryManager.TAG, "Error while attempting to determine initial switch state for " + uei.getDevName(), e2);
                    }
                }
            }
            for (int i2 = 0; i2 < this.mUEventInfo.size(); i2++) {
                com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo uei2 = this.mUEventInfo.get(i2);
                java.lang.String devPath = uei2.getDevPath();
                if (!this.mDevPath.contains(devPath)) {
                    startObserving("DEVPATH=" + uei2.getDevPath());
                    this.mDevPath.add(devPath);
                }
            }
        }

        private java.util.List<com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo> makeObservedUEventList() {
            java.util.List<com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo> retVal = new java.util.ArrayList<>();
            if (!com.android.server.WiredAccessoryManager.this.mUseDevInputEventForAudioJack) {
                com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo uei = new com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo(com.android.server.WiredAccessoryManager.NAME_H2W, 2, 1, 32);
                if (uei.checkSwitchExists()) {
                    retVal.add(uei);
                } else {
                    android.util.Slog.w(com.android.server.WiredAccessoryManager.TAG, "This kernel does not have wired headset support");
                }
            }
            com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo uei2 = new com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo(com.android.server.WiredAccessoryManager.NAME_USB_AUDIO, 4, 8, 0);
            if (uei2.checkSwitchExists()) {
                retVal.add(uei2);
            } else {
                android.util.Slog.w(com.android.server.WiredAccessoryManager.TAG, "This kernel does not have usb audio support");
            }
            com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo uei3 = new com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo(com.android.server.WiredAccessoryManager.NAME_HDMI_AUDIO, 16, 0, 0);
            if (uei3.checkSwitchExists()) {
                retVal.add(uei3);
            } else {
                com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo uei4 = new com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo(com.android.server.WiredAccessoryManager.NAME_HDMI, 16, 0, 0);
                if (uei4.checkSwitchExists()) {
                    retVal.add(uei4);
                } else {
                    android.util.Slog.w(com.android.server.WiredAccessoryManager.TAG, "This kernel does not have HDMI audio support");
                }
            }
            for (java.lang.String conn : com.android.server.WiredAccessoryManager.DP_AUDIO_CONNS) {
                if (com.android.server.WiredAccessoryManager.LOG) {
                    android.util.Slog.v(com.android.server.WiredAccessoryManager.TAG, "Monitor DP conn " + conn);
                }
                com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo uei5 = new com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo(conn, 16, 0, 0);
                if (uei5.checkSwitchExists()) {
                    retVal.add(uei5);
                } else {
                    android.util.Slog.w(com.android.server.WiredAccessoryManager.TAG, "Conn " + conn + " does not have DP audio support");
                }
            }
            return retVal;
        }

        public void onUEvent(android.os.UEventObserver.UEvent event) {
            java.lang.String devPath = event.get("DEVPATH");
            java.lang.String name = event.get("NAME");
            int state = 0;
            if (name == null) {
                name = event.get("SWITCH_NAME");
            }
            try {
                if (name.startsWith(com.android.server.WiredAccessoryManager.NAME_DP_AUDIO)) {
                    java.lang.String state_str = event.get("STATE");
                    int offset = 0;
                    int length = state_str.length();
                    while (true) {
                        if (offset >= length) {
                            break;
                        }
                        int equals = state_str.indexOf(61, offset);
                        if (equals > offset) {
                            java.lang.String intf_name = state_str.substring(offset, equals);
                            if ((intf_name.equals(com.android.server.WiredAccessoryManager.INTF_DP) || intf_name.equals("HDMI")) && (state = java.lang.Integer.parseInt(state_str.substring(equals + 1, equals + 2))) == 1) {
                                com.android.server.WiredAccessoryManager.this.mDetectedIntf = intf_name;
                                break;
                            }
                        }
                        offset = equals + 3;
                    }
                } else {
                    state = java.lang.Integer.parseInt(event.get("SWITCH_STATE"));
                }
            } catch (java.lang.NumberFormatException e) {
                android.util.Slog.i(com.android.server.WiredAccessoryManager.TAG, "couldn't get state from event, checking node");
                int i = 0;
                while (true) {
                    if (i >= this.mUEventInfo.size()) {
                        break;
                    }
                    com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo uei = this.mUEventInfo.get(i);
                    if (!name.equals(uei.getDevName())) {
                        i++;
                    } else {
                        char[] buffer = new char[1024];
                        int len = 0;
                        try {
                            java.io.FileReader file = new java.io.FileReader(uei.getSwitchStatePath());
                            len = file.read(buffer, 0, 1024);
                            file.close();
                        } catch (java.io.FileNotFoundException e2) {
                            android.util.Slog.e(com.android.server.WiredAccessoryManager.TAG, "file not found");
                        } catch (java.lang.Exception e11) {
                            android.util.Slog.e(com.android.server.WiredAccessoryManager.TAG, "", e11);
                        }
                        try {
                            state = java.lang.Integer.parseInt(new java.lang.String(buffer, 0, len).trim());
                            break;
                        } catch (java.lang.NumberFormatException e3) {
                            android.util.Slog.e(com.android.server.WiredAccessoryManager.TAG, "could not convert to number");
                        }
                    }
                }
            }
            synchronized (com.android.server.WiredAccessoryManager.this.mLock) {
                updateStateLocked(devPath, name, state);
            }
        }

        private void updateStateLocked(java.lang.String str, java.lang.String str2, int i) {
            for (int i2 = 0; i2 < this.mUEventInfo.size(); i2++) {
                com.android.server.WiredAccessoryManager.WiredAccessoryObserver.UEventInfo uEventInfo = this.mUEventInfo.get(i2);
                if (com.android.server.WiredAccessoryManager.LOG) {
                    android.util.Slog.v(com.android.server.WiredAccessoryManager.TAG, "uei.getDevPath=" + uEventInfo.getDevPath());
                    android.util.Slog.v(com.android.server.WiredAccessoryManager.TAG, "uevent.getDevPath=" + str);
                }
                if (str.equals(uEventInfo.getDevPath())) {
                    if (i == 1 && com.android.server.WiredAccessoryManager.this.mDpCount > 0) {
                        uEventInfo.setStreamIndex(com.android.server.WiredAccessoryManager.this.mDpCount);
                    }
                    if (i == 1) {
                        uEventInfo.setCableIndex(1 ^ (com.android.server.WiredAccessoryManager.this.mDetectedIntf.equals(com.android.server.WiredAccessoryManager.INTF_DP) ? 1 : 0));
                    }
                    com.android.server.WiredAccessoryManager.this.updateLocked(str2, uEventInfo.getDevAddress(), uEventInfo.computeNewHeadsetState(com.android.server.WiredAccessoryManager.this.mHeadsetState, i));
                    return;
                }
            }
        }

        private final class UEventInfo {
            static final /* synthetic */ boolean $assertionsDisabled = false;
            private final java.lang.String mDevName;
            private final int mState1Bits;
            private final int mState2Bits;
            private final int mStateNbits;
            private java.lang.String mDevAddress = "controller=0;stream=0";
            private int mDevIndex = -1;
            private int mCableIndex = -1;

            public UEventInfo(java.lang.String devName, int state1Bits, int state2Bits, int stateNbits) {
                int idx;
                this.mDevName = devName;
                this.mState1Bits = state1Bits;
                this.mState2Bits = state2Bits;
                this.mStateNbits = stateNbits;
                if (this.mDevName.startsWith(com.android.server.WiredAccessoryManager.NAME_DP_AUDIO) && (idx = this.mDevName.indexOf(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER)) != -1) {
                    int idx2 = this.mDevName.indexOf(com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER, idx + 1);
                    int dev = java.lang.Integer.parseInt(this.mDevName.substring(idx + 1, idx2));
                    int cable = java.lang.Integer.parseInt(this.mDevName.substring(idx2 + 1));
                    if (com.android.server.WiredAccessoryManager.LOG) {
                        android.util.Slog.v(com.android.server.WiredAccessoryManager.TAG, "UEvent dev address " + this.mDevAddress);
                    }
                    checkDevIndex(dev);
                    checkCableIndex(cable);
                }
            }

            private void checkDevIndex(int dev_index) {
                int index = 0;
                char[] buffer = new char[1024];
                while (true) {
                    java.lang.String devPath = java.lang.String.format(java.util.Locale.US, "/sys/devices/platform/soc/%s/extcon/extcon%d/name", com.android.server.WiredAccessoryManager.NAME_DP_AUDIO, java.lang.Integer.valueOf(dev_index));
                    if (com.android.server.WiredAccessoryManager.LOG) {
                        android.util.Slog.v(com.android.server.WiredAccessoryManager.TAG, "checkDevIndex " + devPath);
                    }
                    java.io.File f = new java.io.File(devPath);
                    if (!f.exists()) {
                        android.util.Slog.e(com.android.server.WiredAccessoryManager.TAG, "file " + devPath + " not found");
                        return;
                    }
                    try {
                        java.io.FileReader file = new java.io.FileReader(f);
                        int len = file.read(buffer, 0, 1024);
                        file.close();
                        java.lang.String devName = new java.lang.String(buffer, 0, len).trim();
                        if (devName.startsWith(com.android.server.WiredAccessoryManager.NAME_DP_AUDIO) && index == dev_index) {
                            android.util.Slog.e(com.android.server.WiredAccessoryManager.TAG, "set dev_index " + dev_index);
                            this.mDevIndex = dev_index;
                            return;
                        }
                        index++;
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(com.android.server.WiredAccessoryManager.TAG, "checkDevIndex exception ", e);
                        return;
                    }
                }
            }

            private void checkCableIndex(int cable_index) {
                if (this.mDevIndex == -1) {
                    return;
                }
                int index = 0;
                char[] buffer = new char[1024];
                while (true) {
                    java.lang.String cablePath = java.lang.String.format(java.util.Locale.US, "/sys/devices/platform/soc/%s/extcon/extcon%d/cable.%d/name", com.android.server.WiredAccessoryManager.NAME_DP_AUDIO, java.lang.Integer.valueOf(this.mDevIndex), java.lang.Integer.valueOf(index));
                    if (com.android.server.WiredAccessoryManager.LOG) {
                        android.util.Slog.v(com.android.server.WiredAccessoryManager.TAG, "checkCableIndex " + cablePath);
                    }
                    java.io.File f = new java.io.File(cablePath);
                    if (!f.exists()) {
                        android.util.Slog.e(com.android.server.WiredAccessoryManager.TAG, "file " + cablePath + " not found");
                        return;
                    }
                    try {
                        java.io.FileReader file = new java.io.FileReader(f);
                        int len = file.read(buffer, 0, 1024);
                        file.close();
                        java.lang.String cableName = new java.lang.String(buffer, 0, len).trim();
                        if (cableName.equals("HDMI") && index == cable_index) {
                            this.mCableIndex = index;
                            android.util.Slog.w(com.android.server.WiredAccessoryManager.TAG, "checkCableIndex set cable " + cable_index);
                            return;
                        } else if (cableName.equals(com.android.server.WiredAccessoryManager.INTF_DP) && index == cable_index) {
                            this.mCableIndex = index;
                            android.util.Slog.w(com.android.server.WiredAccessoryManager.TAG, "checkCableIndex set cable " + cable_index);
                            return;
                        } else {
                            android.util.Slog.w(com.android.server.WiredAccessoryManager.TAG, "checkCableIndex no name match, skip ");
                            index++;
                        }
                    } catch (java.lang.Exception e) {
                        android.util.Slog.e(com.android.server.WiredAccessoryManager.TAG, "checkCableIndex exception", e);
                        return;
                    }
                }
            }

            public void setStreamIndex(int streamIndex) {
                int index1 = this.mDevAddress.indexOf("=");
                int index2 = this.mDevAddress.indexOf("=", index1 + 1);
                java.lang.String allExceptStreamIdx = this.mDevAddress.substring(0, index2 + 1);
                this.mDevAddress = allExceptStreamIdx + java.lang.String.valueOf(streamIndex);
            }

            public void setCableIndex(int cableIndex) {
                int index = this.mDevAddress.indexOf("=");
                java.lang.String changeControllerIdx = this.mDevAddress.substring(0, index + 1) + cableIndex + this.mDevAddress.substring(index + 2);
                this.mDevAddress = changeControllerIdx;
            }

            public java.lang.String getDevName() {
                return this.mDevName;
            }

            public java.lang.String getDevAddress() {
                return this.mDevAddress;
            }

            public java.lang.String getDevPath() {
                if (this.mDevName.startsWith(com.android.server.WiredAccessoryManager.NAME_DP_AUDIO)) {
                    return java.lang.String.format(java.util.Locale.US, "/devices/platform/soc/%s/extcon/extcon%d", com.android.server.WiredAccessoryManager.NAME_DP_AUDIO, java.lang.Integer.valueOf(this.mDevIndex));
                }
                return java.lang.String.format(java.util.Locale.US, "/devices/virtual/switch/%s", this.mDevName);
            }

            public java.lang.String getSwitchStatePath() {
                if (this.mDevName.startsWith(com.android.server.WiredAccessoryManager.NAME_DP_AUDIO)) {
                    return java.lang.String.format(java.util.Locale.US, "/sys/devices/platform/soc/%s/extcon/extcon%d/cable.%d/state", com.android.server.WiredAccessoryManager.NAME_DP_AUDIO, java.lang.Integer.valueOf(this.mDevIndex), java.lang.Integer.valueOf(this.mCableIndex));
                }
                return java.lang.String.format(java.util.Locale.US, "/sys/class/switch/%s/state", this.mDevName);
            }

            public boolean checkSwitchExists() {
                java.io.File f = new java.io.File(getSwitchStatePath());
                return f.exists();
            }

            public int computeNewHeadsetState(int headsetState, int switchState) {
                int setBits;
                int preserveMask = ~(this.mState1Bits | this.mState2Bits | this.mStateNbits);
                if (switchState == 1) {
                    setBits = this.mState1Bits;
                } else if (switchState == 2) {
                    setBits = this.mState2Bits;
                } else {
                    setBits = switchState == this.mStateNbits ? this.mStateNbits : 0;
                }
                return (headsetState & preserveMask) | setBits;
            }
        }
    }

    private class WiredAccessoryExtconObserver extends com.android.server.ExtconStateObserver<android.util.Pair<java.lang.Integer, java.lang.Integer>> {
        private final java.util.List<com.android.server.ExtconUEventObserver.ExtconInfo> mExtconInfos = com.android.server.ExtconUEventObserver.ExtconInfo.getExtconInfoForTypes(new java.lang.String[]{com.android.server.ExtconUEventObserver.ExtconInfo.EXTCON_HEADPHONE, com.android.server.ExtconUEventObserver.ExtconInfo.EXTCON_MICROPHONE, "HDMI", com.android.server.ExtconUEventObserver.ExtconInfo.EXTCON_LINE_OUT});

        WiredAccessoryExtconObserver() {
        }

        private void init() {
            for (com.android.server.ExtconUEventObserver.ExtconInfo extconInfo : this.mExtconInfos) {
                android.util.Pair<java.lang.Integer, java.lang.Integer> state = null;
                try {
                    state = parseStateFromFile(extconInfo);
                } catch (java.io.FileNotFoundException e) {
                    android.util.Slog.w(com.android.server.WiredAccessoryManager.TAG, extconInfo.getStatePath() + " not found while attempting to determine initial state", e);
                } catch (java.io.IOException e2) {
                    android.util.Slog.e(com.android.server.WiredAccessoryManager.TAG, "Error reading " + extconInfo.getStatePath() + " while attempting to determine initial state", e2);
                }
                if (state != null) {
                    updateStateInt(extconInfo, extconInfo.getName(), state);
                }
                if (com.android.server.WiredAccessoryManager.LOG) {
                    android.util.Slog.d(com.android.server.WiredAccessoryManager.TAG, "observing " + extconInfo.getName());
                }
                startObserving(extconInfo);
            }
        }

        public int uEventCount() {
            return this.mExtconInfos.size();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.android.server.ExtconStateObserver
        public android.util.Pair<java.lang.Integer, java.lang.Integer> parseState(com.android.server.ExtconUEventObserver.ExtconInfo extconInfo, java.lang.String status) {
            if (com.android.server.WiredAccessoryManager.LOG) {
                android.util.Slog.v(com.android.server.WiredAccessoryManager.TAG, "status  " + status);
            }
            int[] maskAndState = {0, 0};
            if (extconInfo.hasCableType(com.android.server.ExtconUEventObserver.ExtconInfo.EXTCON_HEADPHONE)) {
                com.android.server.WiredAccessoryManager.updateBit(maskAndState, 1, status, com.android.server.ExtconUEventObserver.ExtconInfo.EXTCON_HEADPHONE);
            }
            if (extconInfo.hasCableType(com.android.server.ExtconUEventObserver.ExtconInfo.EXTCON_MICROPHONE)) {
                com.android.server.WiredAccessoryManager.updateBit(maskAndState, 2, status, com.android.server.ExtconUEventObserver.ExtconInfo.EXTCON_MICROPHONE);
            }
            if (extconInfo.hasCableType("HDMI")) {
                com.android.server.WiredAccessoryManager.updateBit(maskAndState, 16, status, "HDMI");
            }
            if (extconInfo.hasCableType(com.android.server.ExtconUEventObserver.ExtconInfo.EXTCON_LINE_OUT)) {
                com.android.server.WiredAccessoryManager.updateBit(maskAndState, 32, status, com.android.server.ExtconUEventObserver.ExtconInfo.EXTCON_LINE_OUT);
            }
            if (com.android.server.WiredAccessoryManager.LOG) {
                android.util.Slog.v(com.android.server.WiredAccessoryManager.TAG, "mask " + maskAndState[0] + " state " + maskAndState[1]);
            }
            return android.util.Pair.create(java.lang.Integer.valueOf(maskAndState[0]), java.lang.Integer.valueOf(maskAndState[1]));
        }

        @Override // com.android.server.ExtconStateObserver
        public void updateState(com.android.server.ExtconUEventObserver.ExtconInfo extconInfo, java.lang.String name, android.util.Pair<java.lang.Integer, java.lang.Integer> maskAndState) {
            synchronized (com.android.server.WiredAccessoryManager.this.mLock) {
                int mask = ((java.lang.Integer) maskAndState.first).intValue();
                int state = ((java.lang.Integer) maskAndState.second).intValue();
                com.android.server.WiredAccessoryManager.this.updateLocked(name, "", (com.android.server.WiredAccessoryManager.this.mHeadsetState & (~((~state) & mask))) | (mask & state));
            }
        }

        private void updateStateInt(com.android.server.ExtconUEventObserver.ExtconInfo extconInfo, java.lang.String name, android.util.Pair<java.lang.Integer, java.lang.Integer> maskAndState) {
            synchronized (com.android.server.WiredAccessoryManager.this.mLock) {
                int mask = ((java.lang.Integer) maskAndState.first).intValue();
                int state = ((java.lang.Integer) maskAndState.second).intValue();
                if (com.android.server.WiredAccessoryManager.this.mHeadsetState == 0) {
                    com.android.server.WiredAccessoryManager.this.updateLocked(name, "", (com.android.server.WiredAccessoryManager.this.mHeadsetState & (~((~state) & mask))) | (mask & state));
                } else {
                    com.android.server.WiredAccessoryManager.this.updateLocked(name, "", com.android.server.WiredAccessoryManager.this.mHeadsetState | (mask & state & (~((~state) & mask))));
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateBit(int[] maskAndState, int position, java.lang.String state, java.lang.String name) {
        maskAndState[0] = maskAndState[0] | position;
        if (state.contains(name + "=1")) {
            maskAndState[0] = maskAndState[0] | position;
            maskAndState[1] = maskAndState[1] | position;
        } else if (state.contains(name + "=0")) {
            maskAndState[0] = maskAndState[0] | position;
            maskAndState[1] = maskAndState[1] & (~position);
        }
    }
}
