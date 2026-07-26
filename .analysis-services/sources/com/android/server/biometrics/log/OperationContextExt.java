package com.android.server.biometrics.log;

/* JADX INFO: loaded from: classes.dex */
public class OperationContextExt {
    private final android.hardware.biometrics.common.OperationContext mAidlContext;
    private int mDockState;
    private int mFoldState;
    private final boolean mIsBP;
    private boolean mIsDisplayOn;
    private int mOrientation;
    private com.android.server.biometrics.log.BiometricContextSessionInfo mSessionInfo;

    public OperationContextExt(boolean isBP) {
        this(new android.hardware.biometrics.common.OperationContext(), isBP, 0);
    }

    public OperationContextExt(boolean isBP, int modality) {
        this(new android.hardware.biometrics.common.OperationContext(), isBP, modality);
    }

    public OperationContextExt(android.hardware.biometrics.common.OperationContext context, boolean isBP, int modality) {
        this.mIsDisplayOn = false;
        this.mDockState = 0;
        this.mOrientation = 0;
        this.mFoldState = 0;
        this.mAidlContext = context;
        this.mIsBP = isBP;
        if (modality == 2) {
            this.mAidlContext.operationState = android.hardware.biometrics.common.OperationState.fingerprintOperationState(new android.hardware.biometrics.common.OperationState.FingerprintOperationState());
        } else if (modality == 8) {
            this.mAidlContext.operationState = android.hardware.biometrics.common.OperationState.faceOperationState(new android.hardware.biometrics.common.OperationState.FaceOperationState());
        }
    }

    public android.hardware.biometrics.common.OperationContext toAidlContext() {
        return this.mAidlContext;
    }

    public android.hardware.biometrics.common.OperationContext toAidlContext(android.hardware.biometrics.AuthenticateOptions options) {
        if (options instanceof android.hardware.face.FaceAuthenticateOptions) {
            return toAidlContext((android.hardware.face.FaceAuthenticateOptions) options);
        }
        if (options instanceof android.hardware.fingerprint.FingerprintAuthenticateOptions) {
            return toAidlContext((android.hardware.fingerprint.FingerprintAuthenticateOptions) options);
        }
        throw new java.lang.IllegalStateException("Authenticate options are invalid.");
    }

    public android.hardware.biometrics.common.OperationContext toAidlContext(android.hardware.face.FaceAuthenticateOptions options) {
        this.mAidlContext.authenticateReason = android.hardware.biometrics.common.AuthenticateReason.faceAuthenticateReason(getAuthReason(options));
        this.mAidlContext.wakeReason = getWakeReason(options);
        return this.mAidlContext;
    }

    public android.hardware.biometrics.common.OperationContext toAidlContext(android.hardware.fingerprint.FingerprintAuthenticateOptions options) {
        if (options.getVendorReason() != null) {
            this.mAidlContext.authenticateReason = android.hardware.biometrics.common.AuthenticateReason.vendorAuthenticateReason(options.getVendorReason());
        } else {
            this.mAidlContext.authenticateReason = android.hardware.biometrics.common.AuthenticateReason.fingerprintAuthenticateReason(getAuthReason(options));
        }
        this.mAidlContext.wakeReason = getWakeReason(options);
        return this.mAidlContext;
    }

    private int getAuthReason(android.hardware.face.FaceAuthenticateOptions options) {
        switch (options.getAuthenticateReason()) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            default:
                return 0;
        }
    }

    private int getWakeReason(android.hardware.face.FaceAuthenticateOptions options) {
        switch (options.getWakeReason()) {
            case 1:
                return 1;
            case 4:
                return 2;
            case 6:
                return 3;
            case 7:
                return 4;
            case 10:
                return 6;
            case 15:
                return 7;
            case 16:
                return 8;
            case 17:
                return 9;
            default:
                return 0;
        }
    }

    private int getAuthReason(android.hardware.fingerprint.FingerprintAuthenticateOptions options) {
        return 0;
    }

    private int getWakeReason(android.hardware.fingerprint.FingerprintAuthenticateOptions options) {
        return 0;
    }

    public int getId() {
        return this.mAidlContext.id;
    }

    public int getOrderAndIncrement() {
        com.android.server.biometrics.log.BiometricContextSessionInfo info = this.mSessionInfo;
        if (info != null) {
            return info.getOrderAndIncrement();
        }
        return -1;
    }

    public byte getReason() {
        return this.mAidlContext.reason;
    }

    public int getWakeReason() {
        return this.mAidlContext.wakeReason;
    }

    public boolean isDisplayOn() {
        return this.mIsDisplayOn;
    }

    public boolean isAod() {
        return this.mAidlContext.isAod;
    }

    public int getDisplayState() {
        return this.mAidlContext.displayState;
    }

    public boolean isCrypto() {
        return this.mAidlContext.isCrypto;
    }

    public int getDockState() {
        return this.mDockState;
    }

    public int getFoldState() {
        return this.mFoldState;
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public android.hardware.biometrics.common.OperationState getOperationState() {
        return this.mAidlContext.operationState;
    }

    com.android.server.biometrics.log.OperationContextExt update(com.android.server.biometrics.log.BiometricContext biometricContext, boolean isCrypto) {
        this.mAidlContext.isAod = biometricContext.isAod();
        this.mAidlContext.displayState = toAidlDisplayState(biometricContext.getDisplayState());
        this.mAidlContext.foldState = toAidlFoldState(biometricContext.getFoldState());
        this.mAidlContext.isCrypto = isCrypto;
        if (this.mAidlContext.operationState != null && this.mAidlContext.operationState.getTag() == 0) {
            this.mAidlContext.operationState.getFingerprintOperationState().isHardwareIgnoringTouches = biometricContext.isHardwareIgnoringTouches();
        }
        setFirstSessionId(biometricContext);
        this.mIsDisplayOn = biometricContext.isDisplayOn();
        this.mDockState = biometricContext.getDockedState();
        this.mFoldState = biometricContext.getFoldState();
        this.mOrientation = biometricContext.getCurrentRotation();
        return this;
    }

    private static int toAidlDisplayState(int state) {
        switch (state) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            default:
                return 0;
        }
    }

    private static int toAidlFoldState(int state) {
        switch (state) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return 0;
        }
    }

    private void setFirstSessionId(com.android.server.biometrics.log.BiometricContext biometricContext) {
        if (this.mIsBP) {
            this.mSessionInfo = biometricContext.getBiometricPromptSessionInfo();
            if (this.mSessionInfo != null) {
                this.mAidlContext.id = this.mSessionInfo.getId();
                this.mAidlContext.reason = (byte) 1;
                return;
            }
        } else {
            this.mSessionInfo = biometricContext.getKeyguardEntrySessionInfo();
            if (this.mSessionInfo != null) {
                this.mAidlContext.id = this.mSessionInfo.getId();
                this.mAidlContext.reason = (byte) 2;
                return;
            }
        }
        this.mAidlContext.id = 0;
        this.mAidlContext.reason = (byte) 0;
    }
}
