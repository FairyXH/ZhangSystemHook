package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class LmkdConnection {
    private static final int LMKD_REPLY_MAX_SIZE = 222;
    private static final java.lang.String TAG = "ActivityManager";
    private final com.android.server.am.LmkdConnection.LmkdConnectionListener mListener;
    private final android.os.MessageQueue mMsgQueue;
    private final java.lang.Object mLmkdSocketLock = new java.lang.Object();
    private android.net.LocalSocket mLmkdSocket = null;
    private java.io.OutputStream mLmkdOutputStream = null;
    private java.io.InputStream mLmkdInputStream = null;
    private final java.nio.ByteBuffer mInputBuf = java.nio.ByteBuffer.allocate(LMKD_REPLY_MAX_SIZE);
    private final java.io.DataInputStream mInputData = new java.io.DataInputStream(new java.io.ByteArrayInputStream(this.mInputBuf.array()));
    private final java.lang.Object mReplyBufLock = new java.lang.Object();
    private java.nio.ByteBuffer mReplyBuf = null;

    interface LmkdConnectionListener {
        boolean handleUnsolicitedMessage(java.io.DataInputStream dataInputStream, int i);

        boolean isReplyExpected(java.nio.ByteBuffer byteBuffer, java.nio.ByteBuffer byteBuffer2, int i);

        boolean onConnect(java.io.OutputStream outputStream);

        void onDisconnect();
    }

    LmkdConnection(android.os.MessageQueue msgQueue, com.android.server.am.LmkdConnection.LmkdConnectionListener listener) {
        this.mMsgQueue = msgQueue;
        this.mListener = listener;
    }

    public boolean connect() {
        synchronized (this.mLmkdSocketLock) {
            if (this.mLmkdSocket != null) {
                return true;
            }
            android.net.LocalSocket socket = openSocket();
            if (socket == null) {
                android.util.Slog.w("ActivityManager", "Failed to connect to lowmemorykiller, retry later");
                return false;
            }
            try {
                java.io.OutputStream ostream = socket.getOutputStream();
                java.io.InputStream istream = socket.getInputStream();
                if (this.mListener != null && !this.mListener.onConnect(ostream)) {
                    android.util.Slog.w("ActivityManager", "Failed to communicate with lowmemorykiller, retry later");
                    libcore.io.IoUtils.closeQuietly(socket);
                    return false;
                }
                this.mLmkdSocket = socket;
                this.mLmkdOutputStream = ostream;
                this.mLmkdInputStream = istream;
                this.mMsgQueue.addOnFileDescriptorEventListener(this.mLmkdSocket.getFileDescriptor(), 5, new android.os.MessageQueue.OnFileDescriptorEventListener() { // from class: com.android.server.am.LmkdConnection.1
                    @Override // android.os.MessageQueue.OnFileDescriptorEventListener
                    public int onFileDescriptorEvents(java.io.FileDescriptor fd, int events) {
                        return com.android.server.am.LmkdConnection.this.fileDescriptorEventHandler(fd, events);
                    }
                });
                this.mLmkdSocketLock.notifyAll();
                return true;
            } catch (java.io.IOException e) {
                libcore.io.IoUtils.closeQuietly(socket);
                return false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int fileDescriptorEventHandler(java.io.FileDescriptor fd, int events) {
        if (this.mListener == null) {
            return 0;
        }
        if ((events & 1) != 0) {
            processIncomingData();
        }
        if ((events & 4) != 0) {
            synchronized (this.mLmkdSocketLock) {
                this.mMsgQueue.removeOnFileDescriptorEventListener(this.mLmkdSocket.getFileDescriptor());
                libcore.io.IoUtils.closeQuietly(this.mLmkdSocket);
                this.mLmkdSocket = null;
            }
            synchronized (this.mReplyBufLock) {
                if (this.mReplyBuf != null) {
                    this.mReplyBuf = null;
                    this.mReplyBufLock.notifyAll();
                }
            }
            this.mListener.onDisconnect();
            return 0;
        }
        return 5;
    }

    private void processIncomingData() {
        int len = read(this.mInputBuf);
        if (len > 0) {
            try {
                this.mInputData.reset();
                synchronized (this.mReplyBufLock) {
                    if (this.mReplyBuf != null) {
                        if (this.mListener.isReplyExpected(this.mReplyBuf, this.mInputBuf, len)) {
                            this.mReplyBuf.put(this.mInputBuf.array(), 0, len);
                            this.mReplyBuf.rewind();
                            this.mReplyBufLock.notifyAll();
                        } else if (!this.mListener.handleUnsolicitedMessage(this.mInputData, len)) {
                            this.mReplyBuf = null;
                            this.mReplyBufLock.notifyAll();
                            android.util.Slog.e("ActivityManager", "Received an unexpected packet from lmkd");
                        }
                    } else if (!this.mListener.handleUnsolicitedMessage(this.mInputData, len)) {
                        android.util.Slog.w("ActivityManager", "Received an unexpected packet from lmkd");
                    }
                }
            } catch (java.io.IOException e) {
                android.util.Slog.e("ActivityManager", "Failed to parse lmkd data buffer. Size = " + len);
            }
        }
    }

    public boolean isConnected() {
        boolean z;
        synchronized (this.mLmkdSocketLock) {
            z = this.mLmkdSocket != null;
        }
        return z;
    }

    public boolean waitForConnection(long timeoutMs) {
        synchronized (this.mLmkdSocketLock) {
            if (this.mLmkdSocket != null) {
                return true;
            }
            try {
                this.mLmkdSocketLock.wait(timeoutMs);
                return this.mLmkdSocket != null;
            } catch (java.lang.InterruptedException e) {
                return false;
            }
        }
    }

    private android.net.LocalSocket openSocket() {
        try {
            android.net.LocalSocket socket = new android.net.LocalSocket(3);
            socket.connect(new android.net.LocalSocketAddress("lmkd", android.net.LocalSocketAddress.Namespace.RESERVED));
            return socket;
        } catch (java.io.IOException ex) {
            android.util.Slog.e("ActivityManager", "Connection failed: " + ex.toString());
            return null;
        }
    }

    private boolean write(java.nio.ByteBuffer buf) {
        synchronized (this.mLmkdSocketLock) {
            try {
                try {
                    this.mLmkdOutputStream.write(buf.array(), 0, buf.position());
                } catch (java.io.IOException e) {
                    return false;
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
        return true;
    }

    private int read(java.nio.ByteBuffer buf) {
        int i;
        synchronized (this.mLmkdSocketLock) {
            try {
                try {
                    i = this.mLmkdInputStream.read(buf.array(), 0, buf.array().length);
                } catch (java.io.IOException e) {
                    return -1;
                }
            } catch (java.lang.Throwable th) {
                throw th;
            }
        }
        return i;
    }

    public boolean exchange(java.nio.ByteBuffer req, java.nio.ByteBuffer repl) {
        if (repl == null) {
            return write(req);
        }
        boolean result = false;
        synchronized (this.mReplyBufLock) {
            this.mReplyBuf = repl;
            if (write(req)) {
                try {
                    this.mReplyBufLock.wait();
                    result = this.mReplyBuf != null;
                } catch (java.lang.InterruptedException e) {
                    result = false;
                }
            }
            this.mReplyBuf = null;
        }
        return result;
    }
}
