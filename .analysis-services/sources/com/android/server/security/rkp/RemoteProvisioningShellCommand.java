package com.android.server.security.rkp;

/* JADX INFO: loaded from: classes3.dex */
class RemoteProvisioningShellCommand extends android.os.ShellCommand {
    private static final java.time.Duration BIND_TIMEOUT = java.time.Duration.ofSeconds(10);
    static final java.lang.String EEK_ED25519_BASE64 = "goRDoQEnoFgqpAEBAycgBiFYIJm57t1e5FL2hcZMYtw+YatXSH11NymtdoAy0rPLY1jZWEAeIghLpLekyNdOAw7+uK8UTKc7b6XN3Np5xitk/pk5r3bngPpmAIUNB5gqrJFcpyUUSQY0dcqKJ3rZ41pJ6wIDhEOhASegWE6lAQECWCDQrsEVyirPc65rzMvRlh1l6LHd10oaN7lDOpfVmd+YCAM4GCAEIVggvoXnRsSjQlpA2TY6phXQLFh+PdwzAjLS/F4ehyVfcmBYQJvPkOIuS6vRGLEOjl0gJ0uEWP78MpB+cgWDvNeCvvpkeC1UEEvAMb9r6B414vAtzmwvT/L1T6XUg62WovGHWAQ=";
    static final java.lang.String EEK_P256_BASE64 = "goRDoQEmoFhNpQECAyYgASFYIPcUituX9MxT79JkEcTjdR9mH6RxDGzP+glGgHSHVPKtIlggXn9b9uzk9hnM/xM3/Q+hyJPbGAZ2xF3m12p3hsMtr49YQC+XjkL7vgctlUeFR5NAsB/Um0ekxESp8qEHhxDHn8sR9L+f6Dvg5zRMFfx7w34zBfTRNDztAgRgehXgedOK/ySEQ6EBJqBYcaYBAgJYIDVztz+gioCJsSZn6ct8daGvAmH8bmUDkTvTS30UlD5GAzgYIAEhWCDgQc8vDzQPHDMsQbDP1wwwVTXSHmpHE0su0UiWfiScaCJYIB/ORcX7YbqBIfnlBZubOQ52hoZHuB4vRfHOr9o/gGjbWECMs7p+ID4ysGjfYNEdffCsOI5RvP9s4Wc7Snm8Vnizmdh8igfY2rW1f3H02GvfMyc0e2XRKuuGmZirOrSAqr1Q";
    private static final int ERROR = -1;
    private static final int KEY_ID = 452436;
    private static final int SUCCESS = 0;
    private static final java.lang.String USAGE = "usage: cmd remote_provisioning SUBCOMMAND [ARGS]\nhelp\n  Show this message.\ndump\n  Dump service diagnostics.\nlist\n  List the names of the IRemotelyProvisionedComponent instances.\ncsr [--challenge CHALLENGE] NAME\n  Generate and print a base64-encoded CSR from the named\n  IRemotelyProvisionedComponent. A base64-encoded challenge can be provided,\n  or else it defaults to an empty challenge.\ncertify NAME\n  Output the PEM-encoded certificate chain provisioned for the named\n  IRemotelyProvisionedComponent.\n";
    private final int mCallerUid;
    private final android.content.Context mContext;
    private final com.android.server.security.rkp.RemoteProvisioningShellCommand.Injector mInjector;

    RemoteProvisioningShellCommand(android.content.Context context, int callerUid) {
        this(context, callerUid, new com.android.server.security.rkp.RemoteProvisioningShellCommand.Injector());
    }

    RemoteProvisioningShellCommand(android.content.Context context, int callerUid, com.android.server.security.rkp.RemoteProvisioningShellCommand.Injector injector) {
        this.mContext = context;
        this.mCallerUid = callerUid;
        this.mInjector = injector;
    }

    public void onHelp() {
        getOutPrintWriter().print(USAGE);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x000f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r4) {
        /*
            r3 = this;
            if (r4 != 0) goto L7
            int r0 = r3.handleDefaultCommands(r4)
            return r0
        L7:
            r0 = -1
            int r1 = r4.hashCode()     // Catch: java.lang.Exception -> L48
            switch(r1) {
                case 98818: goto L25;
                case 3322014: goto L1a;
                case 668936792: goto L10;
                default: goto Lf;
            }     // Catch: java.lang.Exception -> L48
        Lf:
            goto L2f
        L10:
            java.lang.String r1 = "certify"
            boolean r1 = r4.equals(r1)     // Catch: java.lang.Exception -> L48
            if (r1 == 0) goto Lf
            r1 = 2
            goto L30
        L1a:
            java.lang.String r1 = "list"
            boolean r1 = r4.equals(r1)     // Catch: java.lang.Exception -> L48
            if (r1 == 0) goto Lf
            r1 = 0
            goto L30
        L25:
            java.lang.String r1 = "csr"
            boolean r1 = r4.equals(r1)     // Catch: java.lang.Exception -> L48
            if (r1 == 0) goto Lf
            r1 = 1
            goto L30
        L2f:
            r1 = r0
        L30:
            switch(r1) {
                case 0: goto L42;
                case 1: goto L3d;
                case 2: goto L38;
                default: goto L33;
            }     // Catch: java.lang.Exception -> L48
        L33:
            int r0 = r3.handleDefaultCommands(r4)     // Catch: java.lang.Exception -> L48
            goto L47
        L38:
            int r0 = r3.certify()     // Catch: java.lang.Exception -> L48
            return r0
        L3d:
            int r0 = r3.csr()     // Catch: java.lang.Exception -> L48
            return r0
        L42:
            int r0 = r3.list()     // Catch: java.lang.Exception -> L48
            return r0
        L47:
            return r0
        L48:
            r1 = move-exception
            java.io.PrintWriter r2 = r3.getErrPrintWriter()
            r1.printStackTrace(r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.security.rkp.RemoteProvisioningShellCommand.onCommand(java.lang.String):int");
    }

    void dump(java.io.PrintWriter pw) {
        try {
            android.util.IndentingPrintWriter ipw = new android.util.IndentingPrintWriter(pw);
            for (java.lang.String name : this.mInjector.getIrpcNames()) {
                ipw.println(name + ":");
                ipw.increaseIndent();
                dumpRpcInstance(ipw, name);
                ipw.decreaseIndent();
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace(pw);
        }
    }

    private void dumpRpcInstance(java.io.PrintWriter pw, java.lang.String name) throws android.os.RemoteException {
        android.hardware.security.keymint.RpcHardwareInfo info = this.mInjector.getIrpcBinder(name).getHardwareInfo();
        pw.println("hwVersion=" + info.versionNumber);
        pw.println("rpcAuthorName=" + info.rpcAuthorName);
        if (info.versionNumber < 3) {
            pw.println("supportedEekCurve=" + info.supportedEekCurve);
        }
        pw.println("uniqueId=" + info.uniqueId);
        if (info.versionNumber >= 3) {
            pw.println("supportedNumKeysInCsr=" + info.supportedNumKeysInCsr);
        }
    }

    private int list() throws android.os.RemoteException {
        for (java.lang.String name : this.mInjector.getIrpcNames()) {
            getOutPrintWriter().println(name);
        }
        return 0;
    }

    private int csr() throws co.nstant.in.cbor.CborException, android.os.RemoteException {
        byte[] keysToSignMac;
        byte b;
        byte[] challenge = new byte[0];
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                switch (opt.hashCode()) {
                    case 1891027651:
                        if (opt.equals("--challenge")) {
                            b = 0;
                            break;
                        }
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        challenge = java.util.Base64.getDecoder().decode(getNextArgRequired());
                        break;
                    default:
                        getErrPrintWriter().println("error: unknown option " + opt);
                        return -1;
                }
            } else {
                java.lang.String name = getNextArgRequired();
                android.hardware.security.keymint.IRemotelyProvisionedComponent binder = this.mInjector.getIrpcBinder(name);
                android.hardware.security.keymint.RpcHardwareInfo info = binder.getHardwareInfo();
                android.hardware.security.keymint.MacedPublicKey[] emptyKeys = new android.hardware.security.keymint.MacedPublicKey[0];
                switch (info.versionNumber) {
                    case 1:
                    case 2:
                        android.hardware.security.keymint.DeviceInfo deviceInfo = new android.hardware.security.keymint.DeviceInfo();
                        android.hardware.security.keymint.ProtectedData protectedData = new android.hardware.security.keymint.ProtectedData();
                        byte[] eek = getEekChain(info.supportedEekCurve);
                        byte[] keysToSignMac2 = binder.generateCertificateRequest(false, emptyKeys, eek, challenge, deviceInfo, protectedData);
                        byte[] csrBytes = composeCertificateRequestV1(deviceInfo, challenge, protectedData, keysToSignMac2);
                        keysToSignMac = csrBytes;
                        break;
                    case 3:
                        keysToSignMac = binder.generateCertificateRequestV2(emptyKeys, challenge);
                        break;
                    default:
                        getErrPrintWriter().println("error: unsupported hwVersion: " + info.versionNumber);
                        return -1;
                }
                getOutPrintWriter().println(java.util.Base64.getEncoder().encodeToString(keysToSignMac));
                return 0;
            }
        }
    }

    private byte[] getEekChain(int supportedEekCurve) {
        switch (supportedEekCurve) {
            case 1:
                return java.util.Base64.getDecoder().decode(EEK_P256_BASE64);
            case 2:
                return java.util.Base64.getDecoder().decode(EEK_ED25519_BASE64);
            default:
                throw new java.lang.IllegalArgumentException("unsupported EEK curve: " + supportedEekCurve);
        }
    }

    private byte[] composeCertificateRequestV1(android.hardware.security.keymint.DeviceInfo deviceInfo, byte[] challenge, android.hardware.security.keymint.ProtectedData protectedData, byte[] keysToSignMac) throws co.nstant.in.cbor.CborException {
        co.nstant.in.cbor.model.Array info = new co.nstant.in.cbor.model.Array().add(decode(deviceInfo.deviceInfo)).add(new co.nstant.in.cbor.model.Map());
        co.nstant.in.cbor.model.Array mac = new co.nstant.in.cbor.model.Array().add(new co.nstant.in.cbor.model.ByteString(encode(new co.nstant.in.cbor.model.Map().put(new co.nstant.in.cbor.model.UnsignedInteger(1L), new co.nstant.in.cbor.model.UnsignedInteger(5L))))).add(new co.nstant.in.cbor.model.Map()).add(co.nstant.in.cbor.model.SimpleValue.NULL).add(new co.nstant.in.cbor.model.ByteString(keysToSignMac));
        co.nstant.in.cbor.model.Array csr = new co.nstant.in.cbor.model.Array().add(info).add(new co.nstant.in.cbor.model.ByteString(challenge)).add(decode(protectedData.protectedData)).add(mac);
        return encode(csr);
    }

    private byte[] encode(co.nstant.in.cbor.model.DataItem item) throws co.nstant.in.cbor.CborException {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        new co.nstant.in.cbor.CborEncoder(baos).encode(item);
        return baos.toByteArray();
    }

    private co.nstant.in.cbor.model.DataItem decode(byte[] data) throws co.nstant.in.cbor.CborException {
        java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(data);
        return new co.nstant.in.cbor.CborDecoder(bais).decodeNext();
    }

    private int certify() throws java.lang.Exception {
        java.lang.String name = getNextArgRequired();
        java.util.concurrent.Executor executor = this.mContext.getMainExecutor();
        android.os.CancellationSignal cancellationSignal = new android.os.CancellationSignal();
        com.android.server.security.rkp.RemoteProvisioningShellCommand.OutcomeFuture<android.security.rkp.service.RemotelyProvisionedKey> key = new com.android.server.security.rkp.RemoteProvisioningShellCommand.OutcomeFuture<>();
        this.mInjector.getRegistrationProxy(this.mContext, this.mCallerUid, name, executor).getKeyAsync(KEY_ID, cancellationSignal, executor, key);
        byte[] encodedCertChain = key.join().getEncodedCertChain();
        java.io.ByteArrayInputStream is = new java.io.ByteArrayInputStream(encodedCertChain);
        java.io.PrintWriter pw = getOutPrintWriter();
        for (java.security.cert.Certificate cert : java.security.cert.CertificateFactory.getInstance("X.509").generateCertificates(is)) {
            java.lang.String encoded = java.util.Base64.getEncoder().encodeToString(cert.getEncoded());
            pw.println("-----BEGIN CERTIFICATE-----");
            pw.println(encoded.replaceAll("(.{64})", "$1\n").stripTrailing());
            pw.println("-----END CERTIFICATE-----");
        }
        return 0;
    }

    private static class OutcomeFuture<T> implements android.os.OutcomeReceiver<T, java.lang.Exception> {
        private java.util.concurrent.CompletableFuture<T> mFuture;

        private OutcomeFuture() {
            this.mFuture = new java.util.concurrent.CompletableFuture<>();
        }

        @Override // android.os.OutcomeReceiver
        public void onResult(T result) {
            this.mFuture.complete(result);
        }

        @Override // android.os.OutcomeReceiver
        public void onError(java.lang.Exception e) {
            this.mFuture.completeExceptionally(e);
        }

        public T join() {
            return this.mFuture.join();
        }
    }

    static class Injector {
        Injector() {
        }

        java.lang.String[] getIrpcNames() {
            return android.os.ServiceManager.getDeclaredInstances(android.hardware.security.keymint.IRemotelyProvisionedComponent.DESCRIPTOR);
        }

        android.hardware.security.keymint.IRemotelyProvisionedComponent getIrpcBinder(java.lang.String name) {
            java.lang.String irpc = android.hardware.security.keymint.IRemotelyProvisionedComponent.DESCRIPTOR + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + name;
            android.hardware.security.keymint.IRemotelyProvisionedComponent binder = android.hardware.security.keymint.IRemotelyProvisionedComponent.Stub.asInterface(android.os.ServiceManager.waitForDeclaredService(irpc));
            if (binder == null) {
                throw new java.lang.IllegalArgumentException("failed to find " + irpc);
            }
            return binder;
        }

        android.security.rkp.service.RegistrationProxy getRegistrationProxy(android.content.Context context, int callerUid, java.lang.String name, java.util.concurrent.Executor executor) {
            java.lang.String irpc = android.hardware.security.keymint.IRemotelyProvisionedComponent.DESCRIPTOR + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + name;
            com.android.server.security.rkp.RemoteProvisioningShellCommand.OutcomeFuture<android.security.rkp.service.RegistrationProxy> registration = new com.android.server.security.rkp.RemoteProvisioningShellCommand.OutcomeFuture<>();
            android.security.rkp.service.RegistrationProxy.createAsync(context, callerUid, irpc, com.android.server.security.rkp.RemoteProvisioningShellCommand.BIND_TIMEOUT, executor, registration);
            return registration.join();
        }
    }
}
