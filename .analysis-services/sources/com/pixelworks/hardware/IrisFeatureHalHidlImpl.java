package com.pixelworks.hardware;

/* JADX INFO: loaded from: classes3.dex */
public class IrisFeatureHalHidlImpl {
    private static final java.lang.String INSTANCE_NAME = "default";
    private static final java.lang.String TAG = "IrisFeatureHal";
    private vendor.pixelworks.hardware.feature.V1_0.IIrisFeature mIrisFeature;
    private boolean mIsFeatureParsed = false;
    private int mFeature = 0;
    private int mChipType = 0;
    private int mChipCapability = 0;
    private int mSoftIrisCapability = 0;

    public IrisFeatureHalHidlImpl() {
        if (isDeclared()) {
            try {
                this.mIrisFeature = vendor.pixelworks.hardware.feature.V1_0.IIrisFeature.getService(true);
                if (this.mIrisFeature != null) {
                    android.util.Log.d(TAG, "Use IIrisFeature HIDL");
                    return;
                }
                return;
            } catch (android.os.RemoteException | java.util.NoSuchElementException e) {
                android.util.Log.e(TAG, "Access IIrisFeature failed", e);
                return;
            }
        }
        android.util.Log.d(TAG, "IIrisFeature HIDL is not declared");
    }

    public boolean ready() {
        return this.mIrisFeature != null;
    }

    public static boolean isDeclared() {
        try {
            android.hidl.manager.V1_0.IServiceManager serviceManager = android.hidl.manager.V1_0.IServiceManager.getService();
            return serviceManager.getTransport(vendor.pixelworks.hardware.feature.V1_0.IIrisFeature.kInterfaceName, "default") != 0;
        } catch (android.os.RemoteException | java.util.NoSuchElementException e) {
            android.util.Log.e(TAG, "Access get IrisFeature service failed", e);
            return false;
        }
    }

    /* JADX INFO: renamed from: com.pixelworks.hardware.IrisFeatureHalHidlImpl$1ret, reason: invalid class name */
    class C1ret {
        int v = -1;
        int rc = -1;

        C1ret() {
        }
    }

    public int getFeature() {
        if (this.mIrisFeature != null) {
            final com.pixelworks.hardware.IrisFeatureHalHidlImpl.C1ret ret = new com.pixelworks.hardware.IrisFeatureHalHidlImpl.C1ret();
            try {
                this.mIrisFeature.getFeature(new vendor.pixelworks.hardware.feature.V1_0.IIrisFeature.getFeatureCallback() { // from class: com.pixelworks.hardware.IrisFeatureHalHidlImpl.1
                    @Override // vendor.pixelworks.hardware.feature.V1_0.IIrisFeature.getFeatureCallback
                    public void onValues(int outResult, int outFeature) {
                        ret.rc = outResult;
                        ret.v = outFeature;
                    }
                });
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Access IIrisFeature failed", e);
            }
            android.util.Log.i(TAG, java.lang.String.format("Get Iris feature 0x%x from hidl result %d", java.lang.Integer.valueOf(ret.v), java.lang.Integer.valueOf(ret.rc)));
            if (ret.rc == 0) {
                this.mFeature = ret.v;
            } else {
                this.mFeature = 0;
            }
        } else {
            android.util.Log.e(TAG, "Access IIrisFeature failed");
            this.mFeature = 0;
        }
        int feature = this.mFeature;
        this.mChipType = 0;
        this.mChipCapability = 0;
        this.mSoftIrisCapability = 0;
        if ((feature & 128) > 0) {
            this.mChipType = (feature >> 8) & 255;
            if ((65536 & feature) > 0) {
                this.mChipCapability |= 128;
            }
            if ((262144 & feature) > 0) {
                this.mChipCapability |= 2;
                this.mChipCapability |= 8;
            }
            if ((524288 & feature) > 0) {
                this.mChipCapability |= 4;
                this.mChipCapability |= 16;
                this.mChipCapability |= 1;
            }
            if ((1048576 & feature) > 0) {
                this.mChipCapability |= 32;
                if (this.mChipType == 7) {
                    this.mChipCapability |= 1;
                }
            }
            if ((2097152 & feature) > 0) {
                this.mChipCapability |= 64;
            }
            if ((8388608 & feature) > 0) {
                this.mChipCapability |= 256;
            }
            if ((16777216 & feature) > 0) {
                this.mChipCapability |= 512;
            }
            if ((33554432 & feature) > 0) {
                this.mChipCapability |= 1024;
            }
            if ((4194304 & feature) > 0) {
                this.mSoftIrisCapability = 1;
            }
            if (this.mChipType > 0) {
                int version = (feature >> 30) & 3;
                if (version == 0) {
                    this.mChipType |= 256;
                } else if (version > 1) {
                    this.mChipType |= 512;
                }
            }
        } else {
            if ((feature & 32) > 0) {
                this.mSoftIrisCapability = 1;
            }
            if ((feature & 4) > 0) {
                this.mChipType = 3;
            }
            if ((feature & 8) > 0) {
                this.mChipType = 5;
            }
            if ((feature & 64) > 0) {
                this.mChipType = 5;
                this.mChipCapability |= 1;
            }
            if ((feature & 16) > 0) {
                this.mChipType = 6;
            }
            if (this.mChipType > 0 && (1073741824 & feature) > 0) {
                this.mChipType |= 256;
            }
        }
        this.mIsFeatureParsed = true;
        return this.mFeature;
    }

    public int[] getFeatures() {
        return new int[0];
    }

    public int getChipType() {
        if (!this.mIsFeatureParsed) {
            getFeature();
        }
        return this.mChipType;
    }

    public int getChipCapability() {
        if (!this.mIsFeatureParsed) {
            getFeature();
        }
        return this.mChipCapability;
    }

    public int getSoftIrisCapability() {
        if (!this.mIsFeatureParsed) {
            getFeature();
        }
        return this.mSoftIrisCapability;
    }
}
