package com.android.server.clipboard;

/* JADX INFO: loaded from: classes.dex */
class EmulatorClipboardMonitor implements java.util.function.Consumer<android.content.ClipData> {
    private static final int HOST_PORT = 5000;
    private static final boolean LOG_CLIBOARD_ACCESS = android.os.SystemProperties.getBoolean("ro.boot.qemu.log_clipboard_access", false);
    private static final int MAX_CLIPBOARD_BYTES = 134217728;
    private static final java.lang.String PIPE_NAME = "pipe:clipboard";
    private static final java.lang.String TAG = "EmulatorClipboardMonitor";
    private final java.lang.Thread mHostMonitorThread;
    private java.io.FileDescriptor mPipe = null;

    private static byte[] createOpenHandshake() {
        byte[] bits = java.util.Arrays.copyOf(PIPE_NAME.getBytes(), PIPE_NAME.length() + 1);
        bits[PIPE_NAME.length()] = 0;
        return bits;
    }

    private synchronized java.io.FileDescriptor getPipeFD() {
        return this.mPipe;
    }

    private synchronized void setPipeFD(java.io.FileDescriptor fd) {
        this.mPipe = fd;
    }

    private static java.io.FileDescriptor openPipeImpl() {
        try {
            java.io.FileDescriptor fd = android.system.Os.socket(android.system.OsConstants.AF_VSOCK, android.system.OsConstants.SOCK_STREAM, 0);
            try {
                android.system.Os.connect(fd, new android.system.VmSocketAddress(5000, android.system.OsConstants.VMADDR_CID_HOST));
                byte[] handshake = createOpenHandshake();
                writeFully(fd, handshake, 0, handshake.length);
                return fd;
            } catch (android.system.ErrnoException | java.io.InterruptedIOException | java.net.SocketException e) {
                android.system.Os.close(fd);
                return null;
            }
        } catch (android.system.ErrnoException e2) {
            return null;
        }
    }

    private static java.io.FileDescriptor openPipe() throws java.lang.InterruptedException {
        java.io.FileDescriptor fd = openPipeImpl();
        while (fd == null) {
            java.lang.Thread.sleep(100L);
            fd = openPipeImpl();
        }
        return fd;
    }

    private byte[] receiveMessage(java.io.FileDescriptor fd) throws java.net.ProtocolException, android.system.ErrnoException, java.io.InterruptedIOException, java.io.EOFException {
        byte[] lengthBits = new byte[4];
        readFully(fd, lengthBits, 0, lengthBits.length);
        java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(lengthBits);
        bb.order(java.nio.ByteOrder.LITTLE_ENDIAN);
        int msgLen = bb.getInt();
        if (msgLen < 0 || msgLen > 134217728) {
            throw new java.net.ProtocolException("Clipboard message length: " + msgLen + " out of bounds.");
        }
        byte[] msg = new byte[msgLen];
        readFully(fd, msg, 0, msg.length);
        return msg;
    }

    private static void sendMessage(java.io.FileDescriptor fd, byte[] msg) throws android.system.ErrnoException, java.io.InterruptedIOException {
        byte[] lengthBits = new byte[4];
        java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(lengthBits);
        bb.order(java.nio.ByteOrder.LITTLE_ENDIAN);
        bb.putInt(msg.length);
        writeFully(fd, lengthBits, 0, lengthBits.length);
        writeFully(fd, msg, 0, msg.length);
    }

    EmulatorClipboardMonitor(final java.util.function.Consumer<android.content.ClipData> setAndroidClipboard) {
        this.mHostMonitorThread = new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.clipboard.EmulatorClipboardMonitor$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0(setAndroidClipboard);
            }
        });
        this.mHostMonitorThread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(java.util.function.Consumer setAndroidClipboard) {
        java.io.FileDescriptor fd = null;
        while (!java.lang.Thread.interrupted()) {
            if (fd == null) {
                try {
                    fd = openPipe();
                    setPipeFD(fd);
                } catch (android.system.ErrnoException | java.io.EOFException | java.io.InterruptedIOException | java.lang.InterruptedException | java.lang.OutOfMemoryError | java.net.ProtocolException e) {
                    android.util.Slog.w(TAG, "Failure to read from host clipboard", e);
                    setPipeFD(null);
                    try {
                        android.system.Os.close(fd);
                    } catch (android.system.ErrnoException e2) {
                    }
                    fd = null;
                }
            }
            byte[] receivedData = receiveMessage(fd);
            java.lang.String str = new java.lang.String(receivedData);
            android.content.ClipData clip = new android.content.ClipData("host clipboard", new java.lang.String[]{"text/plain"}, new android.content.ClipData.Item(str));
            android.os.PersistableBundle bundle = new android.os.PersistableBundle();
            bundle.putBoolean("com.android.systemui.SUPPRESS_CLIPBOARD_OVERLAY", true);
            clip.getDescription().setExtras(bundle);
            if (LOG_CLIBOARD_ACCESS) {
                android.util.Slog.i(TAG, "Setting the guest clipboard to '" + str + "'");
            }
            setAndroidClipboard.accept(clip);
        }
    }

    @Override // java.util.function.Consumer
    public void accept(android.content.ClipData clip) {
        java.io.FileDescriptor fd = getPipeFD();
        if (fd != null) {
            setHostClipboard(fd, getClipString(clip));
        }
    }

    private java.lang.String getClipString(android.content.ClipData clip) {
        java.lang.CharSequence text;
        if (clip == null || clip.getItemCount() == 0 || (text = clip.getItemAt(0).getText()) == null) {
            return "";
        }
        return text.toString();
    }

    private static void setHostClipboard(final java.io.FileDescriptor fd, final java.lang.String value) {
        java.lang.Thread t = new java.lang.Thread(new java.lang.Runnable() { // from class: com.android.server.clipboard.EmulatorClipboardMonitor$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.clipboard.EmulatorClipboardMonitor.lambda$setHostClipboard$1(value, fd);
            }
        });
        t.start();
    }

    static /* synthetic */ void lambda$setHostClipboard$1(java.lang.String value, java.io.FileDescriptor fd) {
        if (LOG_CLIBOARD_ACCESS) {
            android.util.Slog.i(TAG, "Setting the host clipboard to '" + value + "'");
        }
        try {
            sendMessage(fd, value.getBytes());
        } catch (android.system.ErrnoException | java.io.InterruptedIOException e) {
            android.util.Slog.e(TAG, "Failed to set host clipboard " + e.getMessage());
        } catch (java.lang.IllegalArgumentException e2) {
        }
    }

    private static void readFully(java.io.FileDescriptor fd, byte[] buf, int offset, int size) throws android.system.ErrnoException, java.io.InterruptedIOException, java.io.EOFException {
        while (size > 0) {
            int r = android.system.Os.read(fd, buf, offset, size);
            if (r > 0) {
                offset += r;
                size -= r;
            } else {
                throw new java.io.EOFException();
            }
        }
    }

    private static void writeFully(java.io.FileDescriptor fd, byte[] buf, int offset, int size) throws android.system.ErrnoException, java.io.InterruptedIOException {
        while (size > 0) {
            int r = android.system.Os.write(fd, buf, offset, size);
            if (r > 0) {
                offset += r;
                size -= r;
            } else {
                throw new android.system.ErrnoException("write", android.system.OsConstants.EIO);
            }
        }
    }
}
