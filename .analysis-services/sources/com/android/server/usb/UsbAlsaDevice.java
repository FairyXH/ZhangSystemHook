package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbAlsaDevice {
    protected static final boolean DEBUG = false;
    private static final java.lang.String[] DIRECTION_STR = {"INPUT", "OUTPUT"};
    private static final int INPUT = 0;
    private static final int NUM_DIRECTIONS = 2;
    private static final int OUTPUT = 1;
    private static final java.lang.String OplusSpecialUsbDeviceName = "USB-Audio - MegaSig  U965";
    private static final java.lang.String TAG = "UsbAlsaDevice";
    private final java.lang.String mAlsaCardDeviceString;
    private android.media.IAudioService mAudioService;
    private final int mCardNum;
    private final java.lang.String mDeviceAddress;
    private final int mDeviceNum;
    private final boolean mIsDock;
    private com.android.server.usb.UsbAlsaJackDetector mJackDetector;
    private final boolean[] mHasDevice = new boolean[2];
    private final boolean[] mIsHeadset = new boolean[2];
    private final int[] mDeviceType = new int[2];
    private boolean[] mIsSelected = new boolean[2];
    private int[] mState = new int[2];
    private java.lang.String mDeviceName = "";
    private java.lang.String mDeviceDescription = "";
    private boolean mHasJackDetect = true;

    public UsbAlsaDevice(android.media.IAudioService audioService, int card, int device, java.lang.String deviceAddress, boolean hasOutput, boolean hasInput, boolean isInputHeadset, boolean isOutputHeadset, boolean isDock) {
        this.mAudioService = audioService;
        this.mCardNum = card;
        this.mDeviceNum = device;
        this.mDeviceAddress = deviceAddress;
        this.mHasDevice[1] = hasOutput;
        this.mHasDevice[0] = hasInput;
        this.mIsHeadset[0] = isInputHeadset;
        this.mIsHeadset[1] = isOutputHeadset;
        this.mIsDock = isDock;
        initDeviceType();
        this.mAlsaCardDeviceString = getAlsaCardDeviceString();
    }

    public int getCardNum() {
        return this.mCardNum;
    }

    public int getDeviceNum() {
        return this.mDeviceNum;
    }

    public java.lang.String getDeviceAddress() {
        return this.mDeviceAddress;
    }

    public java.lang.String getAlsaCardDeviceString() {
        if (this.mCardNum < 0 || this.mDeviceNum < 0) {
            android.util.Slog.e(TAG, "Invalid alsa card or device alsaCard: " + this.mCardNum + " alsaDevice: " + this.mDeviceNum);
            return null;
        }
        return com.android.server.audio.AudioService.makeAlsaAddressString(this.mCardNum, this.mDeviceNum);
    }

    public boolean hasOutput() {
        return this.mHasDevice[1];
    }

    public boolean hasInput() {
        return this.mHasDevice[0];
    }

    public boolean isOutputHeadset() {
        return this.mIsHeadset[1];
    }

    public boolean isInputHeadset() {
        return this.mIsHeadset[0];
    }

    public boolean isDock() {
        return this.mIsDock;
    }

    private synchronized boolean isInputJackConnected() {
        if (this.mJackDetector == null) {
            return true;
        }
        return this.mJackDetector.isInputJackConnected();
    }

    private synchronized boolean isOutputJackConnected() {
        if (this.mJackDetector == null) {
            return true;
        }
        return this.mJackDetector.isOutputJackConnected();
    }

    private synchronized void startJackDetect() {
        if (this.mJackDetector != null) {
            return;
        }
        if (this.mHasJackDetect) {
            this.mJackDetector = com.android.server.usb.UsbAlsaJackDetector.startJackDetect(this);
            if (this.mJackDetector == null) {
                this.mHasJackDetect = false;
            }
        }
    }

    private synchronized void stopJackDetect() {
        if (this.mJackDetector != null) {
            this.mJackDetector.pleaseStop();
        }
        this.mJackDetector = null;
    }

    public synchronized void start() {
        startOutput();
        startInput();
    }

    public synchronized void startInput() {
        startDevice(0);
    }

    public synchronized void startOutput() {
        startDevice(1);
    }

    private void startDevice(int direction) {
        if (this.mIsSelected[direction]) {
            return;
        }
        this.mIsSelected[direction] = true;
        this.mState[direction] = 0;
        startJackDetect();
        updateWiredDeviceConnectionState(direction, true);
    }

    public synchronized void stop() {
        stopOutput();
        stopInput();
    }

    public synchronized void stopInput() {
        if (this.mIsSelected[0]) {
            if (!this.mIsSelected[1]) {
                stopJackDetect();
            }
            updateInputWiredDeviceConnectionState(false);
            this.mIsSelected[0] = false;
        }
    }

    public synchronized void stopOutput() {
        if (this.mIsSelected[1]) {
            if (!this.mIsSelected[0]) {
                stopJackDetect();
            }
            updateOutputWiredDeviceConnectionState(false);
            this.mIsSelected[1] = false;
        }
    }

    private void initDeviceType() {
        int i;
        int[] iArr = this.mDeviceType;
        int i2 = 0;
        if (this.mHasDevice[0]) {
            i = this.mIsHeadset[0] ? android.hardware.audio.common.V2_0.AudioDevice.IN_USB_HEADSET : android.hardware.audio.common.V2_0.AudioDevice.IN_USB_DEVICE;
        } else {
            i = 0;
        }
        iArr[0] = i;
        int[] iArr2 = this.mDeviceType;
        if (this.mHasDevice[1]) {
            if (this.mIsDock) {
                i2 = 4096;
            } else {
                i2 = this.mIsHeadset[1] ? 67108864 : 16384;
            }
        }
        iArr2[1] = i2;
    }

    public int getOutputDeviceType() {
        return this.mDeviceType[1];
    }

    public int getInputDeviceType() {
        return this.mDeviceType[0];
    }

    private boolean updateWiredDeviceConnectionState(int direction, boolean enable) {
        if (!this.mIsSelected[direction]) {
            android.util.Slog.e(TAG, "Updating wired device connection state on unselected device");
            return false;
        }
        if (this.mDeviceType[direction] == 0) {
            android.util.Slog.d(TAG, "Unable to set device connection state as " + DIRECTION_STR[direction] + " device type is none");
            return false;
        }
        if (this.mAlsaCardDeviceString == null) {
            android.util.Slog.w(TAG, "Failed to update " + DIRECTION_STR[direction] + " device connection state failed as alsa card device string is null");
            return false;
        }
        if (this.mDeviceName.equals(OplusSpecialUsbDeviceName)) {
            if (this.mDeviceType[direction] == -2147479552) {
                this.mDeviceType[0] = -2113929216;
                android.util.Slog.d(TAG, "Force Usb In Headset");
            } else if (this.mDeviceType[direction] == 16384) {
                this.mDeviceType[1] = 67108864;
                android.util.Slog.d(TAG, "Force Usb Out Headset");
            }
        }
        boolean connected = direction == 0 ? isInputJackConnected() : isOutputJackConnected();
        android.util.Slog.i(TAG, DIRECTION_STR[direction] + " JACK connected: " + connected);
        int state = (enable && connected) ? 1 : 0;
        if (state != this.mState[direction]) {
            this.mState[direction] = state;
            android.media.AudioDeviceAttributes attributes = new android.media.AudioDeviceAttributes(this.mDeviceType[direction], this.mAlsaCardDeviceString, this.mDeviceName);
            try {
                this.mAudioService.setWiredDeviceConnectionState(attributes, state, TAG);
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(TAG, "RemoteException in setWiredDeviceConnectionState for " + DIRECTION_STR[direction]);
                return false;
            }
        }
        return true;
    }

    public synchronized boolean updateInputWiredDeviceConnectionState(boolean enable) {
        return updateWiredDeviceConnectionState(0, enable);
    }

    public synchronized boolean updateOutputWiredDeviceConnectionState(boolean enable) {
        return updateWiredDeviceConnectionState(1, enable);
    }

    public synchronized java.lang.String toString() {
        return "UsbAlsaDevice: [card: " + this.mCardNum + ", device: " + this.mDeviceNum + ", name: " + this.mDeviceName + ", hasOutput: " + this.mHasDevice[1] + ", hasInput: " + this.mHasDevice[0] + "]";
    }

    public synchronized void dump(com.android.internal.util.dump.DualDumpOutputStream dump, java.lang.String idName, long id) {
        long token = dump.start(idName, id);
        dump.write("card", 1120986464257L, this.mCardNum);
        dump.write("device", 1120986464258L, this.mDeviceNum);
        dump.write("name", 1138166333443L, this.mDeviceName);
        dump.write("has_output", 1133871366148L, this.mHasDevice[1]);
        dump.write("has_input", 1133871366149L, this.mHasDevice[0]);
        dump.write("address", 1138166333446L, this.mDeviceAddress);
        dump.end(token);
    }

    synchronized java.lang.String toShortString() {
        return "[card:" + this.mCardNum + " device:" + this.mDeviceNum + " " + this.mDeviceName + "]";
    }

    synchronized java.lang.String getDeviceName() {
        return this.mDeviceName;
    }

    synchronized void setDeviceNameAndDescription(java.lang.String deviceName, java.lang.String deviceDescription) {
        this.mDeviceName = deviceName;
        this.mDeviceDescription = deviceDescription;
    }

    public boolean equals(java.lang.Object obj) {
        if (!(obj instanceof com.android.server.usb.UsbAlsaDevice)) {
            return false;
        }
        com.android.server.usb.UsbAlsaDevice other = (com.android.server.usb.UsbAlsaDevice) obj;
        return this.mCardNum == other.mCardNum && this.mDeviceNum == other.mDeviceNum && java.util.Arrays.equals(this.mHasDevice, other.mHasDevice) && java.util.Arrays.equals(this.mIsHeadset, other.mIsHeadset) && this.mIsDock == other.mIsDock;
    }

    public int hashCode() {
        return (((((((((((((1 * 31) + this.mCardNum) * 31) + this.mDeviceNum) * 31) + (!this.mHasDevice[1] ? 1 : 0)) * 31) + (!this.mHasDevice[0] ? 1 : 0)) * 31) + (!this.mIsHeadset[0] ? 1 : 0)) * 31) + (!this.mIsHeadset[1] ? 1 : 0)) * 31) + (!this.mIsDock ? 1 : 0);
    }
}
