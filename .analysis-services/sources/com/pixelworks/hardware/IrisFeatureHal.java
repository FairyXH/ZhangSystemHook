package com.pixelworks.hardware;

/* JADX INFO: loaded from: classes3.dex */
public class IrisFeatureHal {
    private static final java.lang.String TAG = "IrisFeatureHal";
    private com.pixelworks.hardware.IrisFeatureHalAidlImpl mAidlImpl;
    private com.pixelworks.hardware.IrisFeatureHalHidlImpl mHidlImpl;
    private boolean mUseAidl = false;
    private boolean mHasIris = false;
    private final java.lang.String PROP_IRIS_SUPPORT = "sys.pxlw.iris.support";

    public IrisFeatureHal() {
        createHalImpl();
    }

    public int getFeature() {
        if (!this.mHasIris) {
            return 0;
        }
        if (this.mUseAidl) {
            return this.mAidlImpl.getFeature();
        }
        return this.mHidlImpl.getFeature();
    }

    public int[] getFeatures() {
        if (!this.mHasIris) {
            return new int[0];
        }
        if (this.mUseAidl) {
            return this.mAidlImpl.getFeatures();
        }
        return this.mHidlImpl.getFeatures();
    }

    public int getChipType() {
        if (!this.mHasIris) {
            return 0;
        }
        if (this.mUseAidl) {
            return this.mAidlImpl.getChipType();
        }
        return this.mHidlImpl.getChipType();
    }

    public int getChipCapability() {
        if (!this.mHasIris) {
            return 0;
        }
        if (this.mUseAidl) {
            return this.mAidlImpl.getChipCapability();
        }
        return this.mHidlImpl.getChipCapability();
    }

    public int getSoftIrisCapability() {
        if (!this.mHasIris) {
            return 0;
        }
        if (this.mUseAidl) {
            return this.mAidlImpl.getSoftIrisCapability();
        }
        return this.mHidlImpl.getSoftIrisCapability();
    }

    private void createHalImpl() {
        try {
            if (com.pixelworks.hardware.IrisFeatureHalAidlImpl.isDeclared()) {
                this.mAidlImpl = new com.pixelworks.hardware.IrisFeatureHalAidlImpl();
                if (this.mAidlImpl.ready()) {
                    this.mUseAidl = true;
                    this.mHasIris = true;
                    return;
                }
            }
        } catch (java.lang.NullPointerException e) {
        } catch (java.lang.SecurityException e2) {
            android.util.Log.w(TAG, "SELinux denied IIrisFeature AIDL");
        }
        try {
            if (com.pixelworks.hardware.IrisFeatureHalHidlImpl.isDeclared()) {
                this.mHidlImpl = new com.pixelworks.hardware.IrisFeatureHalHidlImpl();
                if (this.mHidlImpl.ready()) {
                    this.mHasIris = true;
                    return;
                }
            }
        } catch (java.lang.NullPointerException e3) {
        } catch (java.lang.SecurityException e4) {
            android.util.Log.w(TAG, "SELinux denied IIrisFeature HIDL");
        }
        android.util.Log.i(TAG, "No found IIrisFeature service");
    }
}
