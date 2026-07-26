package com.android.server.companion.securechannel;

/* JADX INFO: loaded from: classes.dex */
public class SecureChannel {
    private static final boolean DEBUG = android.os.Build.IS_DEBUGGABLE;
    private static final int HEADER_LENGTH = 6;
    private static final java.lang.String TAG = "CDM_SecureChannel";
    private static final int VERSION = 1;
    private java.lang.String mAlias;
    private final com.android.server.companion.securechannel.SecureChannel.Callback mCallback;
    private byte[] mClientInit;
    private com.google.security.cryptauth.lib.securegcm.ukey2.D2DConnectionContextV1 mConnectionContext;
    private com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext mHandshakeContext;
    private volatile boolean mInProgress;
    private final java.io.InputStream mInput;
    private final java.io.OutputStream mOutput;
    private final byte[] mPreSharedKey;
    private boolean mPskVerified;
    private com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.Role mRole;
    private volatile boolean mStopped;
    private int mVerificationResult;
    private final com.android.server.companion.securechannel.AttestationVerifier mVerifier;

    public interface Callback {
        void onError(java.lang.Throwable th);

        void onSecureConnection();

        void onSecureMessageReceived(byte[] bArr);
    }

    public SecureChannel(java.io.InputStream in, java.io.OutputStream out, com.android.server.companion.securechannel.SecureChannel.Callback callback, byte[] preSharedKey) {
        this(in, out, callback, preSharedKey, null);
    }

    public SecureChannel(java.io.InputStream in, java.io.OutputStream out, com.android.server.companion.securechannel.SecureChannel.Callback callback, android.content.Context context) {
        this(in, out, callback, null, new com.android.server.companion.securechannel.AttestationVerifier(context));
    }

    public SecureChannel(java.io.InputStream in, java.io.OutputStream out, com.android.server.companion.securechannel.SecureChannel.Callback callback, byte[] preSharedKey, com.android.server.companion.securechannel.AttestationVerifier verifier) {
        this.mInput = in;
        this.mOutput = out;
        this.mCallback = callback;
        this.mPreSharedKey = preSharedKey;
        this.mVerifier = verifier;
    }

    public void start() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Starting secure channel.");
        }
        this.mStopped = false;
        new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.companion.securechannel.SecureChannel$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$start$0();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$0() {
        try {
            exchangeHandshake();
            exchangeAuthentication();
            this.mInProgress = false;
            this.mCallback.onSecureConnection();
            while (!this.mStopped) {
                receiveSecureMessage();
            }
        } catch (java.lang.Exception e) {
            if (this.mStopped) {
                return;
            }
            android.util.Slog.e(TAG, "Secure channel encountered an error.", e);
            close();
            this.mCallback.onError(e);
        }
    }

    public void stop() {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Stopping secure channel.");
        }
        this.mStopped = true;
        this.mInProgress = false;
    }

    public void close() {
        stop();
        if (DEBUG) {
            android.util.Slog.d(TAG, "Closing secure channel.");
        }
        libcore.io.IoUtils.closeQuietly(this.mInput);
        libcore.io.IoUtils.closeQuietly(this.mOutput);
        com.android.server.companion.securechannel.KeyStoreUtils.cleanUp(this.mAlias);
    }

    public boolean isStopped() {
        return this.mStopped;
    }

    public void establishSecureConnection() throws com.android.server.companion.securechannel.SecureChannelException, java.io.IOException, com.google.security.cryptauth.lib.securegcm.ukey2.HandshakeException {
        if (isSecured()) {
            android.util.Slog.d(TAG, "Channel is already secure.");
            return;
        }
        if (this.mInProgress) {
            android.util.Slog.w(TAG, "Channel has already started establishing secure connection.");
            return;
        }
        try {
            this.mInProgress = true;
            initiateHandshake();
        } catch (com.google.security.cryptauth.lib.securegcm.ukey2.BadHandleException e) {
            throw new com.android.server.companion.securechannel.SecureChannelException("Failed to initiate handshake protocol.", e);
        }
    }

    public void sendSecureMessage(byte[] data) throws java.io.IOException {
        if (!isSecured()) {
            android.util.Slog.d(TAG, "Cannot send a message without a secure connection.");
            throw new java.lang.IllegalStateException("Channel is not secured yet.");
        }
        try {
            sendMessage(com.android.server.companion.securechannel.SecureChannel.MessageType.SECURE_MESSAGE, data);
        } catch (com.google.security.cryptauth.lib.securegcm.ukey2.BadHandleException e) {
            throw new com.android.server.companion.securechannel.SecureChannelException("Failed to encrypt data.", e);
        }
    }

    private void receiveSecureMessage() throws java.io.IOException, com.google.security.cryptauth.lib.securegcm.ukey2.CryptoException {
        if (!isSecured()) {
            android.util.Slog.d(TAG, "Received a message without a secure connection. Message will be ignored.");
            this.mCallback.onError(new java.lang.IllegalStateException("Connection is not secure."));
            return;
        }
        try {
            byte[] receivedMessage = readMessage(com.android.server.companion.securechannel.SecureChannel.MessageType.SECURE_MESSAGE);
            this.mCallback.onSecureMessageReceived(receivedMessage);
        } catch (com.android.server.companion.securechannel.SecureChannelException e) {
            android.util.Slog.w(TAG, "Ignoring received message.", e);
        }
    }

    private byte[] readMessage(com.android.server.companion.securechannel.SecureChannel.MessageType expected) throws com.android.server.companion.securechannel.SecureChannelException, java.io.IOException, com.google.security.cryptauth.lib.securegcm.ukey2.CryptoException {
        if (DEBUG) {
            if (isSecured()) {
                android.util.Slog.d(TAG, "Waiting to receive next secure message.");
            } else {
                android.util.Slog.d(TAG, "Waiting to receive next " + expected + " message.");
            }
        }
        synchronized (this.mInput) {
            byte[] headerBytes = new byte[6];
            libcore.io.Streams.readFully(this.mInput, headerBytes);
            java.nio.ByteBuffer header = java.nio.ByteBuffer.wrap(headerBytes);
            int version = header.getInt();
            short type = header.getShort();
            if (version != 1) {
                libcore.io.Streams.skipByReading(this.mInput, Long.MAX_VALUE);
                throw new com.android.server.companion.securechannel.SecureChannelException("Secure channel version mismatch. Currently on version 1. Skipping rest of data.");
            }
            if (type != expected.mValue) {
                libcore.io.Streams.skipByReading(this.mInput, Long.MAX_VALUE);
                throw new com.android.server.companion.securechannel.SecureChannelException("Unexpected message type. Expected " + expected.name() + "; Found " + com.android.server.companion.securechannel.SecureChannel.MessageType.from(type).name() + ". Skipping rest of data.");
            }
            byte[] lengthBytes = new byte[4];
            libcore.io.Streams.readFully(this.mInput, lengthBytes);
            int length = java.nio.ByteBuffer.wrap(lengthBytes).getInt();
            try {
                byte[] data = new byte[length];
                libcore.io.Streams.readFully(this.mInput, data);
                if (!com.android.server.companion.securechannel.SecureChannel.MessageType.shouldEncrypt(expected)) {
                    return data;
                }
                return this.mConnectionContext.decodeMessageFromPeer(data, headerBytes);
            } catch (java.lang.OutOfMemoryError error) {
                libcore.io.Streams.skipByReading(this.mInput, Long.MAX_VALUE);
                throw new com.android.server.companion.securechannel.SecureChannelException("Payload is too large.", error);
            }
        }
    }

    private void sendMessage(com.android.server.companion.securechannel.SecureChannel.MessageType messageType, byte[] payload) throws java.io.IOException, com.google.security.cryptauth.lib.securegcm.ukey2.BadHandleException {
        byte[] data;
        synchronized (this.mOutput) {
            byte[] header = java.nio.ByteBuffer.allocate(6).putInt(1).putShort(messageType.mValue).array();
            if (com.android.server.companion.securechannel.SecureChannel.MessageType.shouldEncrypt(messageType)) {
                data = this.mConnectionContext.encodeMessageToPeer(payload, header);
            } else {
                data = payload;
            }
            this.mOutput.write(header);
            this.mOutput.write(java.nio.ByteBuffer.allocate(4).putInt(data.length).array());
            this.mOutput.write(data);
            this.mOutput.flush();
        }
    }

    private void initiateHandshake() throws java.io.IOException, com.google.security.cryptauth.lib.securegcm.ukey2.BadHandleException, com.google.security.cryptauth.lib.securegcm.ukey2.HandshakeException {
        if (this.mConnectionContext != null) {
            android.util.Slog.d(TAG, "Ukey2 handshake is already completed.");
            return;
        }
        this.mRole = com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.Role.INITIATOR;
        this.mHandshakeContext = com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.forInitiator();
        this.mClientInit = this.mHandshakeContext.getNextHandshakeMessage();
        if (DEBUG) {
            android.util.Slog.d(TAG, "Sending Ukey2 Client Init message");
        }
        sendMessage(com.android.server.companion.securechannel.SecureChannel.MessageType.HANDSHAKE_INIT, constructHandshakeInitMessage(this.mClientInit));
    }

    private byte[] handleHandshakeCollision(byte[] handshakeInitMessage) throws java.io.IOException, com.google.security.cryptauth.lib.securegcm.ukey2.HandshakeException, com.google.security.cryptauth.lib.securegcm.ukey2.BadHandleException, com.google.security.cryptauth.lib.securegcm.ukey2.CryptoException {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap(handshakeInitMessage);
        boolean isClientInit = buffer.get() == 0;
        byte[] handshakeMessage = new byte[buffer.remaining()];
        buffer.get(handshakeMessage);
        if (this.mHandshakeContext == null || !isClientInit) {
            return handshakeMessage;
        }
        android.util.Slog.w(TAG, "Detected a Ukey2 handshake role collision. Negotiating a role.");
        if (compareByteArray(this.mClientInit, handshakeMessage) < 0) {
            android.util.Slog.d(TAG, "Assigned: Responder");
            this.mHandshakeContext = null;
            return handshakeMessage;
        }
        android.util.Slog.d(TAG, "Assigned: Initiator; Discarding received Client Init");
        java.nio.ByteBuffer nextInitMessage = java.nio.ByteBuffer.wrap(readMessage(com.android.server.companion.securechannel.SecureChannel.MessageType.HANDSHAKE_INIT));
        if (nextInitMessage.get() == 0) {
            throw new com.google.security.cryptauth.lib.securegcm.ukey2.HandshakeException("Failed to resolve Ukey2 handshake role collision.");
        }
        byte[] nextHandshakeMessage = new byte[nextInitMessage.remaining()];
        nextInitMessage.get(nextHandshakeMessage);
        return nextHandshakeMessage;
    }

    private void exchangeHandshake() throws com.google.security.cryptauth.lib.securegcm.ukey2.AlertException, java.io.IOException, com.google.security.cryptauth.lib.securegcm.ukey2.CryptoException, com.google.security.cryptauth.lib.securegcm.ukey2.HandshakeException, com.google.security.cryptauth.lib.securegcm.ukey2.BadHandleException {
        if (this.mConnectionContext != null) {
            android.util.Slog.d(TAG, "Ukey2 handshake is already completed.");
            return;
        }
        byte[] handshakeInitMessage = readMessage(com.android.server.companion.securechannel.SecureChannel.MessageType.HANDSHAKE_INIT);
        this.mInProgress = true;
        byte[] handshakeMessage = handleHandshakeCollision(handshakeInitMessage);
        if (this.mHandshakeContext == null) {
            this.mRole = com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.Role.RESPONDER;
            this.mHandshakeContext = com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.forResponder();
            if (DEBUG) {
                android.util.Slog.d(TAG, "Receiving Ukey2 Client Init message");
            }
            this.mHandshakeContext.parseHandshakeMessage(handshakeMessage);
            if (DEBUG) {
                android.util.Slog.d(TAG, "Sending Ukey2 Server Init message");
            }
            sendMessage(com.android.server.companion.securechannel.SecureChannel.MessageType.HANDSHAKE_INIT, constructHandshakeInitMessage(this.mHandshakeContext.getNextHandshakeMessage()));
            if (DEBUG) {
                android.util.Slog.d(TAG, "Receiving Ukey2 Client Finish message");
            }
            this.mHandshakeContext.parseHandshakeMessage(readMessage(com.android.server.companion.securechannel.SecureChannel.MessageType.HANDSHAKE_FINISH));
        } else {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Receiving Ukey2 Server Init message");
            }
            this.mHandshakeContext.parseHandshakeMessage(handshakeMessage);
            if (DEBUG) {
                android.util.Slog.d(TAG, "Sending Ukey2 Client Finish message");
            }
            sendMessage(com.android.server.companion.securechannel.SecureChannel.MessageType.HANDSHAKE_FINISH, this.mHandshakeContext.getNextHandshakeMessage());
        }
        if (this.mHandshakeContext.isHandshakeComplete()) {
            if (DEBUG) {
                android.util.Slog.d(TAG, "Ukey2 Handshake completed successfully");
            }
            this.mConnectionContext = this.mHandshakeContext.toConnectionContext();
            return;
        }
        android.util.Slog.e(TAG, "Failed to complete Ukey2 Handshake");
        throw new java.lang.IllegalStateException("Ukey2 Handshake did not complete as expected.");
    }

    private void exchangeAuthentication() throws java.security.GeneralSecurityException, java.io.IOException, com.google.security.cryptauth.lib.securegcm.ukey2.BadHandleException, com.google.security.cryptauth.lib.securegcm.ukey2.CryptoException {
        if (this.mPreSharedKey != null) {
            exchangePreSharedKey();
        }
        if (this.mVerifier != null) {
            exchangeAttestation();
        }
    }

    private void exchangePreSharedKey() throws java.security.GeneralSecurityException, java.io.IOException, com.google.security.cryptauth.lib.securegcm.ukey2.BadHandleException, com.google.security.cryptauth.lib.securegcm.ukey2.CryptoException {
        com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.Role role;
        if (DEBUG) {
            android.util.Slog.d(TAG, "Exchanging pre-shared keys.");
        }
        sendMessage(com.android.server.companion.securechannel.SecureChannel.MessageType.PRE_SHARED_KEY, constructToken(this.mRole, this.mPreSharedKey));
        byte[] receivedAuthToken = readMessage(com.android.server.companion.securechannel.SecureChannel.MessageType.PRE_SHARED_KEY);
        if (this.mRole == com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.Role.INITIATOR) {
            role = com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.Role.RESPONDER;
        } else {
            role = com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.Role.INITIATOR;
        }
        byte[] expectedAuthToken = constructToken(role, this.mPreSharedKey);
        this.mPskVerified = java.util.Arrays.equals(receivedAuthToken, expectedAuthToken);
        if (!this.mPskVerified) {
            throw new com.android.server.companion.securechannel.SecureChannelException("Failed to verify the hash of pre-shared key.");
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "The pre-shared key was successfully authenticated.");
        }
    }

    private void exchangeAttestation() throws java.security.GeneralSecurityException, java.io.IOException, com.google.security.cryptauth.lib.securegcm.ukey2.BadHandleException, com.google.security.cryptauth.lib.securegcm.ukey2.CryptoException {
        com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.Role role;
        if (this.mVerificationResult == 1) {
            android.util.Slog.d(TAG, "Remote attestation was already verified.");
            return;
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Exchanging device attestation.");
        }
        if (this.mAlias == null) {
            this.mAlias = generateAlias();
        }
        byte[] localChallenge = constructToken(this.mRole, this.mConnectionContext.getSessionUnique());
        com.android.server.companion.securechannel.KeyStoreUtils.generateAttestationKeyPair(this.mAlias, localChallenge);
        byte[] localAttestation = com.android.server.companion.securechannel.KeyStoreUtils.getEncodedCertificateChain(this.mAlias);
        sendMessage(com.android.server.companion.securechannel.SecureChannel.MessageType.ATTESTATION, localAttestation);
        byte[] remoteAttestation = readMessage(com.android.server.companion.securechannel.SecureChannel.MessageType.ATTESTATION);
        if (this.mRole == com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.Role.INITIATOR) {
            role = com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.Role.RESPONDER;
        } else {
            role = com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.Role.INITIATOR;
        }
        byte[] expectedChallenge = constructToken(role, this.mConnectionContext.getSessionUnique());
        this.mVerificationResult = this.mVerifier.verifyAttestation(remoteAttestation, expectedChallenge);
        byte[] verificationResult = java.nio.ByteBuffer.allocate(4).putInt(this.mVerificationResult).array();
        sendMessage(com.android.server.companion.securechannel.SecureChannel.MessageType.AVF_RESULT, verificationResult);
        byte[] remoteVerificationResult = readMessage(com.android.server.companion.securechannel.SecureChannel.MessageType.AVF_RESULT);
        if (java.nio.ByteBuffer.wrap(remoteVerificationResult).getInt() != 1) {
            throw new com.android.server.companion.securechannel.SecureChannelException("Remote device failed to verify local attestation.");
        }
        if (this.mVerificationResult != 1) {
            throw new com.android.server.companion.securechannel.SecureChannelException("Failed to verify remote attestation.");
        }
        if (DEBUG) {
            android.util.Slog.d(TAG, "Remote attestation was successfully verified.");
        }
    }

    private boolean isSecured() {
        if (this.mConnectionContext == null) {
            return false;
        }
        return this.mPskVerified || this.mVerificationResult == 1;
    }

    private byte[] constructHandshakeInitMessage(byte[] bArr) {
        return java.nio.ByteBuffer.allocate(bArr.length + 1).put((byte) (!com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.Role.INITIATOR.equals(this.mRole) ? 1 : 0)).put(bArr).array();
    }

    private byte[] constructToken(com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.Role role, byte[] authValue) throws java.security.GeneralSecurityException {
        java.security.MessageDigest hash = java.security.MessageDigest.getInstance("SHA-256");
        java.lang.String roleName = role == com.google.security.cryptauth.lib.securegcm.ukey2.D2DHandshakeContext.Role.INITIATOR ? "Initiator" : "Responder";
        byte[] roleUtf8 = roleName.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        int tokenLength = roleUtf8.length + authValue.length;
        return hash.digest(java.nio.ByteBuffer.allocate(tokenLength).put(roleUtf8).put(authValue).array());
    }

    private int compareByteArray(byte[] a, byte[] b) {
        if (a == b) {
            return 0;
        }
        if (a.length != b.length) {
            return a.length - b.length;
        }
        for (int i = 0; i < a.length; i++) {
            if (a[i] != b[i]) {
                return a[i] - b[i];
            }
        }
        return 0;
    }

    private java.lang.String generateAlias() {
        java.lang.String alias;
        do {
            alias = "secure-channel-" + java.util.UUID.randomUUID();
        } while (com.android.server.companion.securechannel.KeyStoreUtils.aliasExists(alias));
        return alias;
    }

    private enum MessageType {
        HANDSHAKE_INIT(18505),
        HANDSHAKE_FINISH(18502),
        PRE_SHARED_KEY(20555),
        ATTESTATION(16724),
        AVF_RESULT(22098),
        SECURE_MESSAGE(21325),
        UNKNOWN(0);

        private final short mValue;

        MessageType(int value) {
            this.mValue = (short) value;
        }

        static com.android.server.companion.securechannel.SecureChannel.MessageType from(short value) {
            for (com.android.server.companion.securechannel.SecureChannel.MessageType messageType : values()) {
                if (value == messageType.mValue) {
                    return messageType;
                }
            }
            return UNKNOWN;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static boolean shouldEncrypt(com.android.server.companion.securechannel.SecureChannel.MessageType type) {
            return (type == HANDSHAKE_INIT || type == HANDSHAKE_FINISH) ? false : true;
        }
    }
}
