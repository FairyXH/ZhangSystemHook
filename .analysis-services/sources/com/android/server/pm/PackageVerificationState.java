package com.android.server.pm;

/* JADX INFO: loaded from: classes2.dex */
class PackageVerificationState {
    private boolean mIntegrityVerificationComplete;
    private boolean mSufficientVerificationComplete;
    private boolean mSufficientVerificationPassed;
    private final com.android.server.pm.VerifyingSession mVerifyingSession;
    private final android.util.SparseBooleanArray mSufficientVerifierUids = new android.util.SparseBooleanArray();
    private final android.util.SparseBooleanArray mRequiredVerifierUids = new android.util.SparseBooleanArray();
    private final android.util.SparseBooleanArray mUnrespondedRequiredVerifierUids = new android.util.SparseBooleanArray();
    private final android.util.SparseBooleanArray mExtendedTimeoutUids = new android.util.SparseBooleanArray();
    private boolean mRequiredVerificationComplete = false;
    private boolean mRequiredVerificationPassed = true;

    PackageVerificationState(com.android.server.pm.VerifyingSession verifyingSession) {
        this.mVerifyingSession = verifyingSession;
    }

    com.android.server.pm.VerifyingSession getVerifyingSession() {
        return this.mVerifyingSession;
    }

    void addRequiredVerifierUid(int uid) {
        this.mRequiredVerifierUids.put(uid, true);
        this.mUnrespondedRequiredVerifierUids.put(uid, true);
    }

    boolean checkRequiredVerifierUid(int uid) {
        return this.mRequiredVerifierUids.get(uid, false);
    }

    void addSufficientVerifier(int uid) {
        this.mSufficientVerifierUids.put(uid, true);
    }

    boolean checkSufficientVerifierUid(int uid) {
        return this.mSufficientVerifierUids.get(uid, false);
    }

    void setVerifierResponseOnTimeout(int uid, int code) {
        if (!checkRequiredVerifierUid(uid)) {
            return;
        }
        this.mSufficientVerifierUids.clear();
        if (this.mUnrespondedRequiredVerifierUids.get(uid, false)) {
            setVerifierResponse(uid, code);
        }
    }

    void setVerifierResponse(int uid, int code) {
        if (this.mRequiredVerifierUids.get(uid)) {
            switch (code) {
                case 1:
                    break;
                case 2:
                    this.mSufficientVerifierUids.clear();
                    break;
                default:
                    this.mRequiredVerificationPassed = false;
                    this.mUnrespondedRequiredVerifierUids.clear();
                    this.mSufficientVerifierUids.clear();
                    this.mExtendedTimeoutUids.clear();
                    break;
            }
            this.mExtendedTimeoutUids.delete(uid);
            this.mUnrespondedRequiredVerifierUids.delete(uid);
            if (this.mUnrespondedRequiredVerifierUids.size() == 0) {
                this.mRequiredVerificationComplete = true;
                return;
            }
            return;
        }
        if (this.mSufficientVerifierUids.get(uid)) {
            if (code == 1) {
                this.mSufficientVerificationPassed = true;
                this.mSufficientVerificationComplete = true;
            }
            this.mSufficientVerifierUids.delete(uid);
            if (this.mSufficientVerifierUids.size() == 0) {
                this.mSufficientVerificationComplete = true;
            }
        }
    }

    void passRequiredVerification() {
        if (this.mUnrespondedRequiredVerifierUids.size() > 0) {
            throw new java.lang.RuntimeException("Required verifiers still present.");
        }
        this.mRequiredVerificationPassed = true;
        this.mRequiredVerificationComplete = true;
    }

    boolean isVerificationComplete() {
        if (!this.mRequiredVerificationComplete) {
            return false;
        }
        if (this.mSufficientVerifierUids.size() == 0) {
            return true;
        }
        return this.mSufficientVerificationComplete;
    }

    boolean isInstallAllowed() {
        if (!this.mRequiredVerificationComplete || !this.mRequiredVerificationPassed) {
            return false;
        }
        if (this.mSufficientVerificationComplete) {
            return this.mSufficientVerificationPassed;
        }
        return true;
    }

    boolean extendTimeout(int uid) {
        if (!checkRequiredVerifierUid(uid) || timeoutExtended(uid)) {
            return false;
        }
        this.mExtendedTimeoutUids.append(uid, true);
        return true;
    }

    boolean timeoutExtended(int uid) {
        return this.mExtendedTimeoutUids.get(uid, false);
    }

    void setIntegrityVerificationResult(int code) {
        this.mIntegrityVerificationComplete = true;
    }

    boolean isIntegrityVerificationComplete() {
        return this.mIntegrityVerificationComplete;
    }

    boolean areAllVerificationsComplete() {
        return this.mIntegrityVerificationComplete && isVerificationComplete();
    }
}
