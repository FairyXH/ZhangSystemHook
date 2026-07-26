package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbAlsaJackDetector implements java.lang.Runnable {
    private static final java.lang.String TAG = "UsbAlsaJackDetector";
    private com.android.server.usb.UsbAlsaDevice mAlsaDevice;
    private boolean mStopJackDetect = false;

    private static native boolean nativeHasJackDetect(int i);

    private native boolean nativeInputJackConnected(int i);

    private native boolean nativeJackDetect(int i);

    private native boolean nativeOutputJackConnected(int i);

    private UsbAlsaJackDetector(com.android.server.usb.UsbAlsaDevice device) {
        this.mAlsaDevice = device;
    }

    public static com.android.server.usb.UsbAlsaJackDetector startJackDetect(com.android.server.usb.UsbAlsaDevice device) {
        if (!nativeHasJackDetect(device.getCardNum())) {
            return null;
        }
        com.android.server.usb.UsbAlsaJackDetector jackDetector = new com.android.server.usb.UsbAlsaJackDetector(device);
        new java.lang.Thread(jackDetector, "USB jack detect thread").start();
        return jackDetector;
    }

    public boolean isInputJackConnected() {
        return nativeInputJackConnected(this.mAlsaDevice.getCardNum());
    }

    public boolean isOutputJackConnected() {
        return nativeOutputJackConnected(this.mAlsaDevice.getCardNum());
    }

    public void pleaseStop() {
        synchronized (this) {
            this.mStopJackDetect = true;
        }
    }

    public boolean jackDetectCallback() {
        synchronized (this) {
            if (this.mStopJackDetect) {
                return false;
            }
            this.mAlsaDevice.updateOutputWiredDeviceConnectionState(true);
            this.mAlsaDevice.updateInputWiredDeviceConnectionState(true);
            return true;
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        nativeJackDetect(this.mAlsaDevice.getCardNum());
    }
}
