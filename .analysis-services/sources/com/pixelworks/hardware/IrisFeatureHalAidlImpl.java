package com.pixelworks.hardware;

/* JADX INFO: loaded from: classes3.dex */
public class IrisFeatureHalAidlImpl {
    private static final java.lang.String INSTANCE_NAME = vendor.pixelworks.hardware.feature.IIrisFeature.DESCRIPTOR + "/default";
    private static final java.lang.String TAG = "IrisFeatureHal";
    private vendor.pixelworks.hardware.feature.IIrisFeature mIrisFeature;
    private boolean mIsFeatureParsed = false;
    private int mFeature = 0;
    private int mChipType = 0;
    private int mChipCapability = 0;
    private int mSoftIrisCapability = 0;

    public IrisFeatureHalAidlImpl() {
        if (isDeclared()) {
            try {
                this.mIrisFeature = vendor.pixelworks.hardware.feature.IIrisFeature.Stub.asInterface(android.os.ServiceManager.waitForDeclaredService(INSTANCE_NAME));
                if (this.mIrisFeature != null) {
                    android.util.Log.d(TAG, "Use IIrisFeature AIDL");
                    return;
                }
                return;
            } catch (java.util.NoSuchElementException e) {
                android.util.Log.e(TAG, "Access IIrisFeature failed", e);
                return;
            }
        }
        android.util.Log.d(TAG, "IIrisFeature AIDL is not declared");
    }

    public boolean ready() {
        return this.mIrisFeature != null;
    }

    public static boolean isDeclared() {
        return android.os.ServiceManager.isDeclared(INSTANCE_NAME);
    }

    public int getFeature() {
        getFeatures();
        this.mIsFeatureParsed = true;
        return this.mFeature;
    }

    public int[] getFeatures() {
        int[] features;
        if (this.mIrisFeature != null) {
            try {
                this.mFeature = this.mIrisFeature.getFeature();
            } catch (android.os.RemoteException | java.lang.RuntimeException e) {
                android.util.Log.e(TAG, "Get Iris feature failed", e);
                this.mFeature = 0;
            } catch (android.os.ServiceSpecificException e2) {
                android.util.Log.e(TAG, "Get Iris feature failed", e2);
                this.mFeature = 0;
            }
            try {
                features = this.mIrisFeature.getFeatures();
                if (features.length > 2) {
                    android.util.Log.i(TAG, java.lang.String.format("Get Iris features %d %d %d from aidl", java.lang.Integer.valueOf(features[0]), java.lang.Integer.valueOf(features[1]), java.lang.Integer.valueOf(features[2])));
                }
            } catch (android.os.RemoteException | java.lang.RuntimeException e3) {
                android.util.Log.e(TAG, "Get Iris feature failed", e3);
                features = new int[0];
            } catch (android.os.ServiceSpecificException e4) {
                android.util.Log.e(TAG, "Get Iris feature failed", e4);
                features = new int[0];
            }
        } else {
            android.util.Log.e(TAG, "Access IIrisFeature failed");
            this.mFeature = 0;
            features = new int[0];
        }
        if (features.length > 2) {
            this.mChipType = features[0];
            this.mChipCapability = features[1];
            this.mSoftIrisCapability = features[2];
        } else {
            this.mChipType = 0;
            this.mChipCapability = 0;
            this.mSoftIrisCapability = 0;
        }
        this.mIsFeatureParsed = true;
        return features;
    }

    public int getChipType() {
        if (!this.mIsFeatureParsed) {
            getFeatures();
        }
        return this.mChipType;
    }

    public int getChipCapability() {
        if (!this.mIsFeatureParsed) {
            getFeatures();
        }
        return this.mChipCapability;
    }

    public int getSoftIrisCapability() {
        if (!this.mIsFeatureParsed) {
            getFeatures();
        }
        return this.mSoftIrisCapability;
    }
}
