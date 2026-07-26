package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class NativeCrashListener extends java.lang.Thread {
    static final boolean DEBUG = false;
    static final java.lang.String DEBUGGERD_SOCKET_PATH = "/data/system/ndebugsocket";
    static final boolean MORE_DEBUG = false;
    static final long SOCKET_TIMEOUT_MILLIS = 10000;
    static final java.lang.String TAG = "NativeCrashListener";
    final com.android.server.am.ActivityManagerService mAm;

    class NativeCrashReporter extends java.lang.Thread {
        com.android.server.am.ProcessRecord mApp;
        java.lang.String mCrashReport;
        boolean mGwpAsanRecoverableCrash;
        int mSignal;

        NativeCrashReporter(com.android.server.am.ProcessRecord app, int signal, boolean gwpAsanRecoverableCrash, java.lang.String report) {
            super("NativeCrashReport");
            this.mApp = app;
            this.mSignal = signal;
            this.mGwpAsanRecoverableCrash = gwpAsanRecoverableCrash;
            this.mCrashReport = report;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                android.app.ApplicationErrorReport.CrashInfo ci = new android.app.ApplicationErrorReport.CrashInfo();
                ci.exceptionClassName = "Native crash";
                ci.exceptionMessage = android.system.Os.strsignal(this.mSignal);
                ci.throwFileName = "unknown";
                ci.throwClassName = "unknown";
                ci.throwMethodName = "unknown";
                ci.stackTrace = this.mCrashReport;
                com.android.server.am.NativeCrashListener.this.mAm.handleApplicationCrashInner(this.mGwpAsanRecoverableCrash ? "native_recoverable_crash" : "native_crash", this.mApp, this.mApp.processName, ci);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.am.NativeCrashListener.TAG, "Unable to report native crash", e);
            }
        }
    }

    NativeCrashListener(com.android.server.am.ActivityManagerService am) {
        this.mAm = am;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        byte[] ackSignal = new byte[1];
        java.io.File socketFile = new java.io.File(DEBUGGERD_SOCKET_PATH);
        if (socketFile.exists()) {
            socketFile.delete();
        }
        try {
            java.io.FileDescriptor serverFd = android.system.Os.socket(android.system.OsConstants.AF_UNIX, android.system.OsConstants.SOCK_STREAM, 0);
            android.system.UnixSocketAddress sockAddr = android.system.UnixSocketAddress.createFileSystem(DEBUGGERD_SOCKET_PATH);
            android.system.Os.bind(serverFd, sockAddr);
            android.system.Os.listen(serverFd, 1);
            android.system.Os.chmod(DEBUGGERD_SOCKET_PATH, vendor.pixelworks.hardware.display.VendorConfig.TYPE_MAX);
            while (true) {
                java.io.FileDescriptor peerFd = null;
                try {
                    try {
                        peerFd = android.system.Os.accept(serverFd, null);
                        if (peerFd != null) {
                            consumeNativeCrashData(peerFd);
                        }
                        if (peerFd != null) {
                            try {
                                android.system.Os.write(peerFd, ackSignal, 0, 1);
                            } catch (java.lang.Exception e) {
                            }
                            try {
                                android.system.Os.close(peerFd);
                            } catch (android.system.ErrnoException e2) {
                            }
                        }
                    } finally {
                    }
                } catch (java.lang.Exception e3) {
                    android.util.Slog.w(TAG, "Error handling connection", e3);
                    if (peerFd != null) {
                        try {
                            android.system.Os.write(peerFd, ackSignal, 0, 1);
                        } catch (java.lang.Exception e4) {
                        }
                        android.system.Os.close(peerFd);
                    }
                }
            }
        } catch (java.lang.Exception e5) {
            android.util.Slog.e(TAG, "Unable to init native debug socket!", e5);
        }
    }

    static int unpackInt(byte[] buf, int offset) {
        int b0 = buf[offset] & 255;
        int b1 = buf[offset + 1] & 255;
        int b2 = buf[offset + 2] & 255;
        int b3 = buf[offset + 3] & 255;
        return (b0 << 24) | (b1 << 16) | (b2 << 8) | b3;
    }

    static int readExactly(java.io.FileDescriptor fd, byte[] buffer, int offset, int numBytes) throws android.system.ErrnoException, java.io.InterruptedIOException {
        int totalRead = 0;
        while (numBytes > 0) {
            int n = android.system.Os.read(fd, buffer, offset + totalRead, numBytes);
            if (n <= 0) {
                return -1;
            }
            numBytes -= n;
            totalRead += n;
        }
        return totalRead;
    }

    /* JADX WARN: Removed duplicated region for block: B:53:0x00e8 A[LOOP:0: B:25:0x0085->B:53:0x00e8, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x009e A[EDGE_INSN: B:67:0x009e->B:32:0x009e BREAK  A[LOOP:0: B:25:0x0085->B:53:0x00e8], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void consumeNativeCrashData(java.io.FileDescriptor r18) {
        /*
            Method dump skipped, instruction units count: 247
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.NativeCrashListener.consumeNativeCrashData(java.io.FileDescriptor):void");
    }
}
