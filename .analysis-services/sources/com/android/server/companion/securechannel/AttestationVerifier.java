package com.android.server.companion.securechannel;

/* JADX INFO: loaded from: classes.dex */
public class AttestationVerifier {
    private static final long ATTESTATION_VERIFICATION_TIMEOUT_SECONDS = 10;
    private static final java.lang.String PARAM_OWNED_BY_SYSTEM = "android.key_owned_by_system";
    private final android.content.Context mContext;

    AttestationVerifier(android.content.Context context) {
        this.mContext = context;
    }

    public int verifyAttestation(byte[] remoteAttestation, byte[] attestationChallenge) throws com.android.server.companion.securechannel.SecureChannelException {
        android.os.Bundle requirements = new android.os.Bundle();
        requirements.putByteArray("localbinding.challenge", attestationChallenge);
        requirements.putBoolean(PARAM_OWNED_BY_SYSTEM, true);
        final java.util.concurrent.atomic.AtomicInteger verificationResult = new java.util.concurrent.atomic.AtomicInteger(0);
        final java.util.concurrent.CountDownLatch verificationFinished = new java.util.concurrent.CountDownLatch(1);
        java.util.function.BiConsumer<java.lang.Integer, android.security.attestationverification.VerificationToken> onVerificationResult = new java.util.function.BiConsumer() { // from class: com.android.server.companion.securechannel.AttestationVerifier$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                com.android.server.companion.securechannel.AttestationVerifier.lambda$verifyAttestation$0(verificationResult, verificationFinished, (java.lang.Integer) obj, (android.security.attestationverification.VerificationToken) obj2);
            }
        };
        ((android.security.attestationverification.AttestationVerificationManager) this.mContext.getSystemService(android.security.attestationverification.AttestationVerificationManager.class)).verifyAttestation(new android.security.attestationverification.AttestationProfile(3), 3, requirements, remoteAttestation, new com.android.server.SystemServerInitThreadPool$$ExternalSyntheticLambda0(), onVerificationResult);
        try {
            boolean finished = verificationFinished.await(ATTESTATION_VERIFICATION_TIMEOUT_SECONDS, java.util.concurrent.TimeUnit.SECONDS);
            if (!finished) {
                throw new com.android.server.companion.securechannel.SecureChannelException("Attestation verification timed out.");
            }
            return verificationResult.get();
        } catch (java.lang.InterruptedException e) {
            throw new com.android.server.companion.securechannel.SecureChannelException("Attestation verification was interrupted", e);
        }
    }

    static /* synthetic */ void lambda$verifyAttestation$0(java.util.concurrent.atomic.AtomicInteger verificationResult, java.util.concurrent.CountDownLatch verificationFinished, java.lang.Integer result, android.security.attestationverification.VerificationToken token) {
        verificationResult.set(result.intValue());
        verificationFinished.countDown();
    }
}
