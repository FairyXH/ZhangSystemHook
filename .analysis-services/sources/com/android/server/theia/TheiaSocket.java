package com.android.server.theia;

/* JADX INFO: loaded from: classes3.dex */
public class TheiaSocket {
    private static final java.lang.String TAG = "TheiaSocketClient";
    private static volatile com.android.server.theia.TheiaSocket theiaSocket;
    private android.net.LocalSocketAddress address;
    private android.net.LocalSocket client;
    private java.io.BufferedReader in;
    android.os.HandlerThread mSendThread;
    com.android.server.theia.TheiaSocket.TheiaSender mSender;
    private java.io.PrintWriter out;
    private final java.lang.String SOCKET_NAME = "theia_socket";
    private boolean isConnected = false;
    private int connectTime = 1;
    private com.android.server.theia.TheiaSocket.ConnectSocketThread connectSocketThread = new com.android.server.theia.TheiaSocket.ConnectSocketThread();

    public com.android.server.theia.TheiaSocket.TheiaSender getSender() {
        return this.mSender;
    }

    public static com.android.server.theia.TheiaSocket getInstance() {
        if (theiaSocket == null) {
            synchronized (com.android.server.theia.TheiaSocket.class) {
                if (theiaSocket == null) {
                    theiaSocket = new com.android.server.theia.TheiaSocket();
                }
            }
        }
        return theiaSocket;
    }

    public TheiaSocket() {
        android.util.Log.d(TAG, "TheiaSocket ready to Start");
        TheiaSocketStart();
    }

    private void TheiaSocketStart() {
        this.client = new android.net.LocalSocket();
        this.address = new android.net.LocalSocketAddress("theia_socket", android.net.LocalSocketAddress.Namespace.RESERVED);
        this.mSendThread = new android.os.HandlerThread("TheiaSender");
        this.mSendThread.start();
        android.os.Looper looper = this.mSendThread.getLooper();
        this.mSender = new com.android.server.theia.TheiaSocket.TheiaSender(looper);
        this.connectSocketThread.start();
    }

    public void sendMessage(java.lang.String content) {
        android.os.Message message = android.os.Message.obtain();
        message.what = 1;
        message.obj = content;
        this.mSender.sendMessage(message);
    }

    private class ConnectSocketThread extends java.lang.Thread {
        private ConnectSocketThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!com.android.server.theia.TheiaSocket.this.isConnected && com.android.server.theia.TheiaSocket.this.connectTime <= 10) {
                try {
                    sleep(1000L);
                    android.util.Log.d(com.android.server.theia.TheiaSocket.TAG, "Try to connect socket;ConnectTime:" + com.android.server.theia.TheiaSocket.this.connectTime);
                    com.android.server.theia.TheiaSocket.this.client.connect(com.android.server.theia.TheiaSocket.this.address);
                    com.android.server.theia.TheiaSocket.this.out = new java.io.PrintWriter(com.android.server.theia.TheiaSocket.this.client.getOutputStream());
                    com.android.server.theia.TheiaSocket.this.in = new java.io.BufferedReader(new java.io.InputStreamReader(com.android.server.theia.TheiaSocket.this.client.getInputStream()));
                    com.android.server.theia.TheiaSocket.this.isConnected = true;
                    com.android.server.theia.TheiaSocket.this.client.setSoTimeout(1200);
                    android.util.Log.d(com.android.server.theia.TheiaSocket.TAG, "TheiaSocket Connect Success");
                } catch (java.lang.Exception e) {
                    com.android.server.theia.TheiaSocket.this.connectTime++;
                    android.util.Log.d(com.android.server.theia.TheiaSocket.TAG, "Connect fail; Reason: " + e.toString());
                }
            }
        }
    }

    public void destroy() {
        try {
            if (this.in != null) {
                this.in.close();
            }
            if (this.out != null) {
                this.out.close();
            }
            if (this.client != null) {
                this.client.close();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    class TheiaSender extends android.os.Handler implements java.lang.Runnable {
        public TheiaSender(android.os.Looper looper) {
            super(looper);
            android.util.Log.d(com.android.server.theia.TheiaSocket.TAG, "entering TheiaSender Constructor");
        }

        @Override // java.lang.Runnable
        public void run() {
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    java.lang.String content = msg.obj.toString();
                    android.util.Log.d(com.android.server.theia.TheiaSocket.TAG, "TheiaSend receive content : " + content);
                    if (!com.android.server.theia.TheiaSocket.this.connectSocketThread.isAlive() && !com.android.server.theia.TheiaSocket.this.isConnected) {
                        android.util.Log.d(com.android.server.theia.TheiaSocket.TAG, "Socket unConnected, restart connectSocketThread");
                        com.android.server.theia.TheiaSocket.this.connectSocketThread = new com.android.server.theia.TheiaSocket.ConnectSocketThread();
                        com.android.server.theia.TheiaSocket.this.connectSocketThread.start();
                    }
                    if (com.android.server.theia.TheiaSocket.this.out != null) {
                        com.android.server.theia.TheiaSocket.this.out.println(content);
                        com.android.server.theia.TheiaSocket.this.out.flush();
                        android.util.Log.d(com.android.server.theia.TheiaSocket.TAG, "send message success");
                    }
                    break;
            }
        }
    }
}
