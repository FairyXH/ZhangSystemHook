package com.android.server.security;

/* JADX INFO: loaded from: classes3.dex */
public class AttestationVerificationManagerService extends com.android.server.SystemService {
    private static final int DUMP_EVENT_LOG_SIZE = 10;
    private static final java.lang.String TAG = "AVF";
    private final com.android.server.security.AttestationVerificationManagerService.DumpLogger mDumpLogger;
    private final com.android.server.security.AttestationVerificationPeerDeviceVerifier mPeerDeviceVerifier;
    private final android.os.IBinder mService;

    public AttestationVerificationManagerService(android.content.Context context) throws java.lang.Exception {
        super(context);
        this.mDumpLogger = new com.android.server.security.AttestationVerificationManagerService.DumpLogger();
        this.mService = new android.security.attestationverification.IAttestationVerificationManagerService.Stub() { // from class: com.android.server.security.AttestationVerificationManagerService.1
            public void verifyAttestation(android.security.attestationverification.AttestationProfile profile, int localBindingType, android.os.Bundle requirements, byte[] attestation, com.android.internal.infra.AndroidFuture resultCallback) throws android.os.RemoteException {
                enforceUsePermission();
                try {
                    android.util.Slog.d(com.android.server.security.AttestationVerificationManagerService.TAG, "verifyAttestation");
                    com.android.server.security.AttestationVerificationManagerService.this.verifyAttestationForAllVerifiers(profile, localBindingType, requirements, attestation, resultCallback);
                } catch (java.lang.Throwable t) {
                    android.util.Slog.e(com.android.server.security.AttestationVerificationManagerService.TAG, "failed to verify attestation", t);
                    throw android.util.ExceptionUtils.propagate(t, android.os.RemoteException.class);
                }
            }

            public void verifyToken(android.security.attestationverification.VerificationToken token, android.os.ParcelDuration parcelDuration, com.android.internal.infra.AndroidFuture resultCallback) throws android.os.RemoteException {
                enforceUsePermission();
                resultCallback.complete(0);
            }

            private void enforceUsePermission() {
                com.android.server.security.AttestationVerificationManagerService.this.getContext().enforceCallingOrSelfPermission("android.permission.USE_ATTESTATION_VERIFICATION_SERVICE", null);
            }

            protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
                if (!android.security.Flags.dumpAttestationVerifications()) {
                    super.dump(fd, writer, args);
                    return;
                }
                if (com.android.internal.util.DumpUtils.checkDumpAndUsageStatsPermission(com.android.server.security.AttestationVerificationManagerService.this.getContext(), com.android.server.security.AttestationVerificationManagerService.TAG, writer)) {
                    android.util.IndentingPrintWriter fout = new android.util.IndentingPrintWriter(writer, "    ");
                    fout.print("AttestationVerificationManagerService");
                    fout.println();
                    fout.increaseIndent();
                    fout.println("Event Log:");
                    fout.increaseIndent();
                    com.android.server.security.AttestationVerificationManagerService.this.mDumpLogger.dumpTo(fout);
                    fout.decreaseIndent();
                }
            }
        };
        this.mPeerDeviceVerifier = new com.android.server.security.AttestationVerificationPeerDeviceVerifier(context, this.mDumpLogger);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void verifyAttestationForAllVerifiers(android.security.attestationverification.AttestationProfile profile, int localBindingType, android.os.Bundle requirements, byte[] attestation, com.android.internal.infra.AndroidFuture<android.security.attestationverification.IVerificationResult> resultCallback) {
        android.security.attestationverification.IVerificationResult result = new android.security.attestationverification.IVerificationResult();
        result.token = null;
        switch (profile.getAttestationProfileId()) {
            case 2:
                android.util.Slog.d(TAG, "Verifying Self Trusted profile.");
                try {
                    result.resultCode = com.android.server.security.AttestationVerificationSelfTrustedVerifierForTesting.getInstance().verifyAttestation(localBindingType, requirements, attestation);
                } catch (java.lang.Throwable th) {
                    result.resultCode = 2;
                }
                break;
            case 3:
                android.util.Slog.d(TAG, "Verifying Peer Device profile.");
                result.resultCode = this.mPeerDeviceVerifier.verifyAttestation(localBindingType, requirements, attestation);
                break;
            default:
                android.util.Slog.d(TAG, "No profile found, defaulting.");
                result.resultCode = 0;
                break;
        }
        resultCallback.complete(result);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        android.util.Slog.d(TAG, "Started");
        publishBinderService("attestation_verification", this.mService);
    }

    static class DumpLogger {
        private final java.util.ArrayDeque<com.android.server.security.AttestationVerificationManagerService.DumpData> mData = new java.util.ArrayDeque<>(10);
        private int mEventsLogged = 0;

        DumpLogger() {
        }

        void logAttempt(com.android.server.security.AttestationVerificationManagerService.DumpData data) {
            synchronized (this.mData) {
                if (this.mData.size() == 10) {
                    this.mData.removeFirst();
                }
                this.mEventsLogged++;
                data.mEventNumber = this.mEventsLogged;
                data.mEventTimeMs = java.lang.System.currentTimeMillis();
                this.mData.add(data);
            }
        }

        void dumpTo(android.util.IndentingPrintWriter writer) {
            synchronized (this.mData) {
                for (com.android.server.security.AttestationVerificationManagerService.DumpData data : this.mData.reversed()) {
                    writer.println(android.text.TextUtils.formatSimple("Verification #%d [%s]", new java.lang.Object[]{java.lang.Integer.valueOf(data.mEventNumber), android.util.TimeUtils.formatForLogging(data.mEventTimeMs)}));
                    writer.increaseIndent();
                    data.dumpTo(writer);
                    writer.decreaseIndent();
                }
            }
        }
    }

    static abstract class DumpData {
        protected int mEventNumber = -1;
        protected long mEventTimeMs = -1;

        abstract void dumpTo(android.util.IndentingPrintWriter indentingPrintWriter);

        DumpData() {
        }
    }
}
