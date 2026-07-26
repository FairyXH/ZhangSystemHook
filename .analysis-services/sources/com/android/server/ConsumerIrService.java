package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class ConsumerIrService extends android.hardware.IConsumerIrService.Stub {
    private static final int MAX_XMIT_TIME = 2000000;
    private static final java.lang.String TAG = "ConsumerIrService";
    private final com.android.server.IConsumerIrServiceExt mConsumerIrServiceExt;
    private final android.content.Context mContext;
    private final boolean mHasNativeHal;
    private final android.os.PowerManager.WakeLock mWakeLock;
    private final java.lang.Object mHalLock = new java.lang.Object();
    private android.hardware.ir.IConsumerIr mAidlService = null;

    private static native boolean getHidlHalService();

    private static native int[] halGetCarrierFrequencies();

    private static native int halTransmit(int i, int[] iArr);

    ConsumerIrService(android.content.Context context) {
        this.mContext = context;
        android.os.PowerManager pm = (android.os.PowerManager) context.getSystemService("power");
        this.mWakeLock = pm.newWakeLock(1, TAG);
        this.mWakeLock.setReferenceCounted(true);
        this.mHasNativeHal = getHalService();
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.consumerir")) {
            if (!this.mHasNativeHal) {
                throw new java.lang.RuntimeException("FEATURE_CONSUMER_IR present, but no IR HAL loaded!");
            }
        } else if (this.mHasNativeHal) {
            throw new java.lang.RuntimeException("IR HAL present, but FEATURE_CONSUMER_IR is not set!");
        }
        this.mConsumerIrServiceExt = (com.android.server.IConsumerIrServiceExt) system.ext.loader.core.ExtLoader.type(com.android.server.IConsumerIrServiceExt.class).create();
        this.mConsumerIrServiceExt.init(this.mContext);
    }

    public boolean hasIrEmitter() {
        return this.mHasNativeHal;
    }

    private boolean getHalService() {
        java.lang.String fqName = android.hardware.ir.IConsumerIr.DESCRIPTOR + "/default";
        this.mAidlService = android.hardware.ir.IConsumerIr.Stub.asInterface(android.os.ServiceManager.waitForDeclaredService(fqName));
        if (this.mAidlService != null) {
            return true;
        }
        return getHidlHalService();
    }

    private void throwIfNoIrEmitter() {
        if (!this.mHasNativeHal) {
            throw new java.lang.UnsupportedOperationException("IR emitter not available");
        }
    }

    public void transmit(java.lang.String packageName, int carrierFrequency, int[] pattern) {
        super.transmit_enforcePermission();
        long totalXmitTime = 0;
        for (int slice : pattern) {
            if (slice <= 0) {
                throw new java.lang.IllegalArgumentException("Non-positive IR slice");
            }
            totalXmitTime += (long) slice;
        }
        if (totalXmitTime > 2000000) {
            throw new java.lang.IllegalArgumentException("IR pattern too long");
        }
        throwIfNoIrEmitter();
        synchronized (this.mHalLock) {
            if (this.mAidlService != null) {
                this.mConsumerIrServiceExt.avoidInterference();
                try {
                    this.mAidlService.transmit(carrierFrequency, pattern);
                } catch (android.os.RemoteException e) {
                    android.util.Slog.e(TAG, "Error transmitting frequency: " + carrierFrequency);
                }
            } else {
                int err = halTransmit(carrierFrequency, pattern);
                if (err < 0) {
                    android.util.Slog.e(TAG, "Error transmitting: " + err);
                }
            }
        }
    }

    public int[] getCarrierFrequencies() {
        super.getCarrierFrequencies_enforcePermission();
        throwIfNoIrEmitter();
        synchronized (this.mHalLock) {
            if (this.mAidlService != null) {
                try {
                    android.hardware.ir.ConsumerIrFreqRange[] output = this.mAidlService.getCarrierFreqs();
                    if (output.length <= 0) {
                        android.util.Slog.e(TAG, "Error getting carrier frequencies.");
                    }
                    int[] result = new int[output.length * 2];
                    for (int i = 0; i < output.length; i++) {
                        result[i * 2] = output[i].minHz;
                        result[(i * 2) + 1] = output[i].maxHz;
                    }
                    return result;
                } catch (android.os.RemoteException e) {
                    return null;
                }
            }
            return halGetCarrierFrequencies();
        }
    }
}
