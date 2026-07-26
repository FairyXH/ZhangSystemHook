package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
public class MediaShellCommand extends android.os.ShellCommand {
    private static android.media.session.MediaSessionManager sMediaSessionManager;
    private static android.app.ActivityThread sThread;
    private java.io.PrintWriter mErrorWriter;
    private java.io.InputStream mInput;
    private final java.lang.String mPackageName;
    private android.media.session.ISessionManager mSessionService;
    private java.io.PrintWriter mWriter;

    public MediaShellCommand(java.lang.String packageName) {
        this.mPackageName = packageName;
    }

    public int onCommand(java.lang.String cmd) {
        this.mWriter = getOutPrintWriter();
        this.mErrorWriter = getErrPrintWriter();
        this.mInput = getRawInputStream();
        if (android.text.TextUtils.isEmpty(cmd)) {
            return handleDefaultCommands(cmd);
        }
        if (sThread == null) {
            android.os.Looper.prepare();
            sThread = android.app.ActivityThread.currentActivityThread();
            sMediaSessionManager = (android.media.session.MediaSessionManager) sThread.getSystemContext().getSystemService("media_session");
        }
        this.mSessionService = android.media.session.ISessionManager.Stub.asInterface(android.os.ServiceManager.checkService("media_session"));
        if (this.mSessionService == null) {
            throw new java.lang.IllegalStateException("Can't connect to media session service; is the system running?");
        }
        try {
            if (cmd.equals("dispatch")) {
                runDispatch();
                return 0;
            }
            if (cmd.equals("list-sessions")) {
                runListSessions();
                return 0;
            }
            if (cmd.equals("monitor")) {
                runMonitor();
                return 0;
            }
            if (cmd.equals("volume")) {
                runVolume();
                return 0;
            }
            if (cmd.equals("expire-temp-engaged-sessions")) {
                expireTempEngagedSessions();
                return 0;
            }
            showError("Error: unknown command '" + cmd + "'");
            return -1;
        } catch (java.lang.Exception e) {
            showError(e.toString());
            return -1;
        }
    }

    public void onHelp() {
        this.mWriter.println("usage: media_session [subcommand] [options]");
        this.mWriter.println("       media_session dispatch KEY");
        this.mWriter.println("       media_session list-sessions");
        this.mWriter.println("       media_session monitor <tag>");
        this.mWriter.println("       media_session volume [options]");
        this.mWriter.println("       media_session expire-temp-engaged-sessions");
        this.mWriter.println();
        this.mWriter.println("media_session dispatch: dispatch a media key to the system.");
        this.mWriter.println("                KEY may be: play, pause, play-pause, mute, headsethook,");
        this.mWriter.println("                stop, next, previous, rewind, record, fast-forward.");
        this.mWriter.println("media_session list-sessions: print a list of the current sessions.");
        this.mWriter.println("media_session monitor: monitor updates to the specified session.");
        this.mWriter.println("                       Use the tag from list-sessions.");
        this.mWriter.println("media_session volume:  " + com.android.server.media.VolumeCtrl.USAGE);
        this.mWriter.println("media_session expire-temp-engaged-sessions: Expires any ongoing");
        this.mWriter.println("                timers for media sessions in a temporary user-engaged");
        this.mWriter.println("                state.");
        this.mWriter.println();
    }

    private void sendMediaKey(android.view.KeyEvent event) {
        try {
            this.mSessionService.dispatchMediaKeyEvent(this.mPackageName, false, event, false);
        } catch (android.os.RemoteException e) {
        }
    }

    private void runMonitor() throws java.lang.Exception {
        java.lang.String id = getNextArgRequired();
        if (id == null) {
            showError("Error: must include a session id");
            return;
        }
        boolean success = false;
        try {
            java.util.List<android.media.session.MediaController> controllers = sMediaSessionManager.getActiveSessions(null);
            java.util.Iterator<android.media.session.MediaController> it = controllers.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                android.media.session.MediaController controller = it.next();
                if (controller != null) {
                    try {
                        if (id.equals(controller.getTag())) {
                            com.android.server.media.MediaShellCommand.ControllerMonitor monitor = new com.android.server.media.MediaShellCommand.ControllerMonitor(controller);
                            monitor.run();
                            success = true;
                            break;
                        }
                        continue;
                    } catch (android.os.RemoteException e) {
                    }
                }
            }
        } catch (java.lang.Exception e2) {
            this.mErrorWriter.println("***Error monitoring session*** " + e2.getMessage());
        }
        if (!success) {
            this.mErrorWriter.println("No session found with id " + id);
        }
    }

    private void runDispatch() throws java.lang.Exception {
        int keycode;
        java.lang.String cmd = getNextArgRequired();
        if ("play".equals(cmd)) {
            keycode = 126;
        } else if ("pause".equals(cmd)) {
            keycode = 127;
        } else if ("play-pause".equals(cmd)) {
            keycode = 85;
        } else if ("mute".equals(cmd)) {
            keycode = 91;
        } else if ("headsethook".equals(cmd)) {
            keycode = 79;
        } else if ("stop".equals(cmd)) {
            keycode = 86;
        } else if ("next".equals(cmd)) {
            keycode = 87;
        } else if ("previous".equals(cmd)) {
            keycode = 88;
        } else if ("rewind".equals(cmd)) {
            keycode = 89;
        } else if ("record".equals(cmd)) {
            keycode = 130;
        } else {
            if (!"fast-forward".equals(cmd)) {
                showError("Error: unknown dispatch code '" + cmd + "'");
                return;
            }
            keycode = 90;
        }
        long now = android.os.SystemClock.uptimeMillis();
        int i = keycode;
        sendMediaKey(new android.view.KeyEvent(now, now, 0, i, 0, 0, -1, 0, 0, 257));
        sendMediaKey(new android.view.KeyEvent(now, now, 1, i, 0, 0, -1, 0, 0, 257));
    }

    void log(java.lang.String code, java.lang.String msg) {
        this.mWriter.println(code + " " + msg);
    }

    void showError(java.lang.String errMsg) {
        onHelp();
        this.mErrorWriter.println(errMsg);
    }

    class ControllerCallback extends android.media.session.MediaController.Callback {
        ControllerCallback() {
        }

        @Override // android.media.session.MediaController.Callback
        public void onSessionDestroyed() {
            com.android.server.media.MediaShellCommand.this.mWriter.println("onSessionDestroyed. Enter q to quit.");
        }

        @Override // android.media.session.MediaController.Callback
        public void onSessionEvent(java.lang.String event, android.os.Bundle extras) {
            com.android.server.media.MediaShellCommand.this.mWriter.println("onSessionEvent event=" + event + ", extras=" + extras);
        }

        @Override // android.media.session.MediaController.Callback
        public void onPlaybackStateChanged(android.media.session.PlaybackState state) {
            com.android.server.media.MediaShellCommand.this.mWriter.println("onPlaybackStateChanged " + state);
        }

        @Override // android.media.session.MediaController.Callback
        public void onMetadataChanged(android.media.MediaMetadata metadata) {
            java.lang.String mmString = metadata == null ? null : "title=" + metadata.getDescription();
            com.android.server.media.MediaShellCommand.this.mWriter.println("onMetadataChanged " + mmString);
        }

        @Override // android.media.session.MediaController.Callback
        public void onQueueChanged(java.util.List<android.media.session.MediaSession.QueueItem> queue) {
            com.android.server.media.MediaShellCommand.this.mWriter.println("onQueueChanged, " + (queue == null ? "null queue" : " size=" + queue.size()));
        }

        @Override // android.media.session.MediaController.Callback
        public void onQueueTitleChanged(java.lang.CharSequence title) {
            com.android.server.media.MediaShellCommand.this.mWriter.println("onQueueTitleChange " + ((java.lang.Object) title));
        }

        @Override // android.media.session.MediaController.Callback
        public void onExtrasChanged(android.os.Bundle extras) {
            com.android.server.media.MediaShellCommand.this.mWriter.println("onExtrasChanged " + extras);
        }

        @Override // android.media.session.MediaController.Callback
        public void onAudioInfoChanged(android.media.session.MediaController.PlaybackInfo info) {
            com.android.server.media.MediaShellCommand.this.mWriter.println("onAudioInfoChanged " + info);
        }
    }

    private class ControllerMonitor {
        private final android.media.session.MediaController mController;
        private final com.android.server.media.MediaShellCommand.ControllerCallback mControllerCallback;

        ControllerMonitor(android.media.session.MediaController controller) {
            this.mController = controller;
            this.mControllerCallback = com.android.server.media.MediaShellCommand.this.new ControllerCallback();
        }

        void printUsageMessage() {
            try {
                com.android.server.media.MediaShellCommand.this.mWriter.println("V2Monitoring session " + this.mController.getTag() + "...  available commands: play, pause, next, previous");
            } catch (java.lang.RuntimeException e) {
                com.android.server.media.MediaShellCommand.this.mWriter.println("Error trying to monitor session!");
            }
            com.android.server.media.MediaShellCommand.this.mWriter.println("(q)uit: finish monitoring");
        }

        void run() throws android.os.RemoteException {
            printUsageMessage();
            android.os.HandlerThread cbThread = new android.os.HandlerThread("MediaCb") { // from class: com.android.server.media.MediaShellCommand.ControllerMonitor.1
                @Override // android.os.HandlerThread
                protected void onLooperPrepared() {
                    try {
                        com.android.server.media.MediaShellCommand.ControllerMonitor.this.mController.registerCallback(com.android.server.media.MediaShellCommand.ControllerMonitor.this.mControllerCallback);
                    } catch (java.lang.RuntimeException e) {
                        com.android.server.media.MediaShellCommand.this.mErrorWriter.println("Error registering monitor callback");
                    }
                }
            };
            cbThread.start();
            try {
                try {
                    try {
                        java.io.InputStreamReader converter = new java.io.InputStreamReader(com.android.server.media.MediaShellCommand.this.mInput);
                        java.io.BufferedReader in = new java.io.BufferedReader(converter);
                        while (true) {
                            com.android.server.media.MediaShellCommand.this.mWriter.flush();
                            com.android.server.media.MediaShellCommand.this.mErrorWriter.flush();
                            java.lang.String line = in.readLine();
                            if (line == null) {
                                break;
                            }
                            boolean addNewline = true;
                            if (line.length() > 0) {
                                if ("q".equals(line) || "quit".equals(line)) {
                                    break;
                                }
                                if ("play".equals(line)) {
                                    dispatchKeyCode(126);
                                } else if ("pause".equals(line)) {
                                    dispatchKeyCode(127);
                                } else if ("next".equals(line)) {
                                    dispatchKeyCode(87);
                                } else if ("previous".equals(line)) {
                                    dispatchKeyCode(88);
                                } else {
                                    com.android.server.media.MediaShellCommand.this.mErrorWriter.println("Invalid command: " + line);
                                }
                            } else {
                                addNewline = false;
                            }
                            synchronized (this) {
                                if (addNewline) {
                                    com.android.server.media.MediaShellCommand.this.mWriter.println("");
                                    printUsageMessage();
                                } else {
                                    printUsageMessage();
                                }
                            }
                        }
                        cbThread.getLooper().quit();
                        this.mController.unregisterCallback(this.mControllerCallback);
                    } catch (java.lang.Throwable th) {
                        cbThread.getLooper().quit();
                        try {
                            this.mController.unregisterCallback(this.mControllerCallback);
                        } catch (java.lang.Exception e) {
                        }
                        throw th;
                    }
                } catch (java.io.IOException e2) {
                    e2.printStackTrace();
                    cbThread.getLooper().quit();
                    this.mController.unregisterCallback(this.mControllerCallback);
                }
            } catch (java.lang.Exception e3) {
            }
        }

        private void dispatchKeyCode(int keyCode) {
            long now = android.os.SystemClock.uptimeMillis();
            android.view.KeyEvent down = new android.view.KeyEvent(now, now, 0, keyCode, 0, 0, -1, 0, 0, 257);
            android.view.KeyEvent up = new android.view.KeyEvent(now, now, 1, keyCode, 0, 0, -1, 0, 0, 257);
            try {
                this.mController.dispatchMediaButtonEvent(down);
                this.mController.dispatchMediaButtonEvent(up);
            } catch (java.lang.RuntimeException e) {
                com.android.server.media.MediaShellCommand.this.mErrorWriter.println("Failed to dispatch " + keyCode);
            }
        }
    }

    private void runListSessions() {
        this.mWriter.println("Sessions:");
        try {
            java.util.List<android.media.session.MediaController> controllers = sMediaSessionManager.getActiveSessions(null);
            for (android.media.session.MediaController controller : controllers) {
                if (controller != null) {
                    try {
                        this.mWriter.println("  tag=" + controller.getTag() + ", package=" + controller.getPackageName());
                    } catch (java.lang.RuntimeException e) {
                    }
                }
            }
        } catch (java.lang.Exception e2) {
            this.mErrorWriter.println("***Error listing sessions***");
        }
    }

    private void runVolume() throws java.lang.Exception {
        com.android.server.media.VolumeCtrl.run(this);
    }

    private void expireTempEngagedSessions() throws java.lang.Exception {
        this.mSessionService.expireTempEngagedSessions();
    }
}
