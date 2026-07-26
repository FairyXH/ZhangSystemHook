package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
class ViewServer implements java.lang.Runnable {
    private static final java.lang.String COMMAND_PROTOCOL_VERSION = "PROTOCOL";
    private static final java.lang.String COMMAND_SERVER_VERSION = "SERVER";
    private static final java.lang.String COMMAND_WINDOW_MANAGER_AUTOLIST = "AUTOLIST";
    private static final java.lang.String COMMAND_WINDOW_MANAGER_GET_FOCUS = "GET_FOCUS";
    private static final java.lang.String COMMAND_WINDOW_MANAGER_LIST = "LIST";
    private static final java.lang.String LOG_TAG = "WindowManager";
    private static final java.lang.String VALUE_PROTOCOL_VERSION = "4";
    private static final java.lang.String VALUE_SERVER_VERSION = "4";
    public static final int VIEW_SERVER_DEFAULT_PORT = 4939;
    private static final int VIEW_SERVER_MAX_CONNECTIONS = 10;
    private final int mPort;
    private java.net.ServerSocket mServer;
    private java.lang.Thread mThread;
    private java.util.concurrent.ExecutorService mThreadPool;
    private final com.android.server.wm.WindowManagerService mWindowManager;

    ViewServer(com.android.server.wm.WindowManagerService windowManager, int port) {
        this.mWindowManager = windowManager;
        this.mPort = port;
    }

    boolean start() throws java.io.IOException {
        if (this.mThread != null) {
            return false;
        }
        this.mServer = new java.net.ServerSocket(this.mPort, 10, java.net.InetAddress.getLocalHost());
        this.mThread = new java.lang.Thread(this, "Remote View Server [port=" + this.mPort + "]");
        this.mThreadPool = java.util.concurrent.Executors.newFixedThreadPool(10);
        this.mThread.start();
        return true;
    }

    boolean stop() {
        if (this.mThread != null) {
            this.mThread.interrupt();
            if (this.mThreadPool != null) {
                try {
                    this.mThreadPool.shutdownNow();
                } catch (java.lang.SecurityException e) {
                    android.util.Slog.w(LOG_TAG, "Could not stop all view server threads");
                }
            }
            this.mThreadPool = null;
            this.mThread = null;
            try {
                this.mServer.close();
                this.mServer = null;
                return true;
            } catch (java.io.IOException e2) {
                android.util.Slog.w(LOG_TAG, "Could not close the view server");
                return false;
            }
        }
        return false;
    }

    boolean isRunning() {
        return this.mThread != null && this.mThread.isAlive();
    }

    @Override // java.lang.Runnable
    public void run() {
        while (java.lang.Thread.currentThread() == this.mThread) {
            try {
                java.net.Socket client = this.mServer.accept();
                if (this.mThreadPool != null) {
                    this.mThreadPool.submit(new com.android.server.wm.ViewServer.ViewServerWorker(client));
                } else {
                    try {
                        client.close();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (java.lang.Exception e2) {
                android.util.Slog.w(LOG_TAG, "Connection error: ", e2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:19:0x0036 -> B:24:0x0038). Please report as a decompilation issue!!! */
    public static boolean writeValue(java.net.Socket client, java.lang.String value) {
        boolean result;
        java.io.BufferedWriter out = null;
        try {
            try {
                java.io.OutputStream clientStream = client.getOutputStream();
                out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(clientStream), 8192);
                out.write(value);
                out.write("\n");
                out.flush();
                result = true;
                out.close();
            } catch (java.lang.Exception e) {
                result = false;
                if (out != null) {
                    out.close();
                }
                return result;
            } catch (java.lang.Throwable th) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (java.io.IOException e2) {
                    }
                }
                throw th;
            }
        } catch (java.io.IOException e3) {
            result = false;
        }
        return result;
    }

    class ViewServerWorker implements java.lang.Runnable, com.android.server.wm.WindowManagerService.WindowChangeListener {
        private java.net.Socket mClient;
        private boolean mNeedWindowListUpdate = false;
        private boolean mNeedFocusedWindowUpdate = false;

        public ViewServerWorker(java.net.Socket client) {
            this.mClient = client;
        }

        @Override // java.lang.Runnable
        public void run() {
            java.lang.String command;
            java.lang.String parameters;
            java.io.BufferedReader in = null;
            try {
                try {
                    try {
                        in = new java.io.BufferedReader(new java.io.InputStreamReader(this.mClient.getInputStream()), 1024);
                        java.lang.String request = in.readLine();
                        int index = request.indexOf(32);
                        if (index == -1) {
                            command = request;
                            parameters = "";
                        } else {
                            command = request.substring(0, index);
                            parameters = request.substring(index + 1);
                        }
                        boolean result = (com.android.server.wm.ViewServer.COMMAND_PROTOCOL_VERSION.equalsIgnoreCase(command) || com.android.server.wm.ViewServer.COMMAND_SERVER_VERSION.equalsIgnoreCase(command)) ? com.android.server.wm.ViewServer.writeValue(this.mClient, "4") : com.android.server.wm.ViewServer.COMMAND_WINDOW_MANAGER_LIST.equalsIgnoreCase(command) ? com.android.server.wm.ViewServer.this.mWindowManager.viewServerListWindows(this.mClient) : com.android.server.wm.ViewServer.COMMAND_WINDOW_MANAGER_GET_FOCUS.equalsIgnoreCase(command) ? com.android.server.wm.ViewServer.this.mWindowManager.viewServerGetFocusedWindow(this.mClient) : com.android.server.wm.ViewServer.COMMAND_WINDOW_MANAGER_AUTOLIST.equalsIgnoreCase(command) ? windowManagerAutolistLoop() : com.android.server.wm.ViewServer.this.mWindowManager.viewServerWindowCommand(this.mClient, command, parameters);
                        if (!result) {
                            android.util.Slog.w(com.android.server.wm.ViewServer.LOG_TAG, "An error occurred with the command: " + command);
                        }
                        try {
                            in.close();
                        } catch (java.io.IOException e) {
                            e.printStackTrace();
                        }
                    } catch (java.io.IOException e2) {
                        android.util.Slog.w(com.android.server.wm.ViewServer.LOG_TAG, "Connection error: ", e2);
                        if (in != null) {
                            try {
                                in.close();
                            } catch (java.io.IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                        if (this.mClient == null) {
                            return;
                        } else {
                            this.mClient.close();
                        }
                    }
                    if (this.mClient != null) {
                        this.mClient.close();
                    }
                } finally {
                }
            } catch (java.io.IOException e4) {
                e4.printStackTrace();
            }
        }

        @Override // com.android.server.wm.WindowManagerService.WindowChangeListener
        public void windowsChanged() {
            synchronized (this) {
                this.mNeedWindowListUpdate = true;
                notifyAll();
            }
        }

        @Override // com.android.server.wm.WindowManagerService.WindowChangeListener
        public void focusChanged() {
            synchronized (this) {
                this.mNeedFocusedWindowUpdate = true;
                notifyAll();
            }
        }

        private boolean windowManagerAutolistLoop() {
            com.android.server.wm.ViewServer.this.mWindowManager.addWindowChangeListener(this);
            java.io.BufferedWriter out = null;
            try {
                out = new java.io.BufferedWriter(new java.io.OutputStreamWriter(this.mClient.getOutputStream()));
                while (!java.lang.Thread.interrupted()) {
                    boolean needWindowListUpdate = false;
                    boolean needFocusedWindowUpdate = false;
                    synchronized (this) {
                        while (!this.mNeedWindowListUpdate && !this.mNeedFocusedWindowUpdate) {
                            wait();
                        }
                        if (this.mNeedWindowListUpdate) {
                            this.mNeedWindowListUpdate = false;
                            needWindowListUpdate = true;
                        }
                        if (this.mNeedFocusedWindowUpdate) {
                            this.mNeedFocusedWindowUpdate = false;
                            needFocusedWindowUpdate = true;
                        }
                    }
                    if (needWindowListUpdate) {
                        out.write("LIST UPDATE\n");
                        out.flush();
                    }
                    if (needFocusedWindowUpdate) {
                        out.write("ACTION_FOCUS UPDATE\n");
                        out.flush();
                    }
                }
                try {
                    out.close();
                } catch (java.io.IOException e) {
                }
            } catch (java.lang.Exception e2) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (java.io.IOException e3) {
                    }
                }
            } catch (java.lang.Throwable th) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (java.io.IOException e4) {
                    }
                }
                com.android.server.wm.ViewServer.this.mWindowManager.removeWindowChangeListener(this);
                throw th;
            }
            com.android.server.wm.ViewServer.this.mWindowManager.removeWindowChangeListener(this);
            return true;
        }
    }
}
