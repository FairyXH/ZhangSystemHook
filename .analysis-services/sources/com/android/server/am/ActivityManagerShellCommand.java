package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class ActivityManagerShellCommand extends android.os.ShellCommand {
    public static final java.lang.String NO_CLASS_ERROR_CODE = "Error type 3";
    private static final java.lang.String PROFILER_OUTPUT_VERSION_FLAG = "--profiler-output-version";
    private static final java.lang.String SHELL_PACKAGE_NAME = "com.android.shell";
    static final java.lang.String TAG = "ActivityManager";
    private static final int USER_OPERATION_TIMEOUT_MS = 120000;
    private int mActivityType;
    private java.lang.String mAgent;
    private boolean mAsync;
    private boolean mAttachAgentDuringBind;
    private boolean mAutoStop;
    private android.app.BroadcastOptions mBroadcastOptions;
    private int mClockType;
    private boolean mDismissKeyguardIfInsecure;
    private int mDisplayId;
    final boolean mDumping;
    final android.app.IActivityManager mInterface;
    final com.android.server.am.ActivityManagerService mInternal;
    private boolean mIsLockTask;
    private boolean mIsTaskOverlay;
    private java.lang.String mProfileFile;
    private int mProfilerOutputVersion;
    private java.lang.String mReceiverPermission;
    private int mSamplingInterval;
    private boolean mShowSplashScreen;
    private boolean mStreaming;
    private int mTaskDisplayAreaFeatureId;
    private int mTaskId;
    final android.app.IActivityTaskManager mTaskInterface;
    private int mUserId;
    private int mWindowingMode;
    private static final java.time.format.DateTimeFormatter LOG_NAME_TIME_FORMATTER = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss", java.util.Locale.ROOT);
    private static final java.lang.String[] CAPABILITIES = {"start.suspend"};
    public com.android.server.am.IActivityManagerShellCommandExt mActivityManagerShellCommandExt = (com.android.server.am.IActivityManagerShellCommandExt) system.ext.loader.core.ExtLoader.type(com.android.server.am.IActivityManagerShellCommandExt.class).create();
    private int mStartFlags = 0;
    private boolean mWaitOption = false;
    private boolean mStopOption = false;
    private int mRepeat = 0;
    final android.content.pm.IPackageManager mPm = android.app.AppGlobals.getPackageManager();

    ActivityManagerShellCommand(com.android.server.am.ActivityManagerService service, boolean dumping) {
        this.mInterface = service;
        this.mTaskInterface = service.mActivityTaskManager;
        this.mInternal = service;
        this.mDumping = dumping;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0016  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r10) {
        /*
            Method dump skipped, instruction units count: 2318
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerShellCommand.onCommand(java.lang.String):int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCommand$1(final int[] startResult, final java.io.PrintWriter pw, final com.android.server.am.ActivityManagerShellCommand.ProgressWaiter waiter) {
        android.view.Choreographer.getInstance().postFrameCallback(new android.view.Choreographer.FrameCallback() { // from class: com.android.server.am.ActivityManagerShellCommand$$ExternalSyntheticLambda3
            @Override // android.view.Choreographer.FrameCallback
            public final void doFrame(long j) {
                this.f$0.lambda$onCommand$0(startResult, pw, waiter, j);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onCommand$0(int[] startResult, java.io.PrintWriter pw, com.android.server.am.ActivityManagerShellCommand.ProgressWaiter waiter, long frameTimeNanos) {
        try {
            startResult[0] = runStartActivity(pw);
            waiter.onFinished(0, null);
        } catch (java.lang.Exception ex) {
            getErrPrintWriter().println("Error: unable to start activity, " + ex);
        }
    }

    int runCapabilities(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.io.PrintWriter err = getErrPrintWriter();
        boolean outputAsProtobuf = false;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--protobuf")) {
                    outputAsProtobuf = true;
                } else {
                    err.println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                java.lang.String vmName = java.lang.System.getProperty("java.vm.name", "?");
                java.lang.String vmVersion = java.lang.System.getProperty("java.vm.version", "?");
                if (outputAsProtobuf) {
                    com.android.server.am.nano.Capabilities capabilities = new com.android.server.am.nano.Capabilities();
                    capabilities.values = new com.android.server.am.nano.Capability[CAPABILITIES.length];
                    for (int i = 0; i < CAPABILITIES.length; i++) {
                        com.android.server.am.nano.Capability cap = new com.android.server.am.nano.Capability();
                        cap.name = CAPABILITIES[i];
                        capabilities.values[i] = cap;
                    }
                    java.lang.String[] vmCapabilities = android.os.Debug.getVmFeatureList();
                    capabilities.vmCapabilities = new com.android.server.am.nano.VMCapability[vmCapabilities.length];
                    for (int i2 = 0; i2 < vmCapabilities.length; i2++) {
                        com.android.server.am.nano.VMCapability cap2 = new com.android.server.am.nano.VMCapability();
                        cap2.name = vmCapabilities[i2];
                        capabilities.vmCapabilities[i2] = cap2;
                    }
                    java.lang.String[] fmCapabilities = android.os.Debug.getFeatureList();
                    capabilities.frameworkCapabilities = new com.android.server.am.nano.FrameworkCapability[fmCapabilities.length];
                    for (int i3 = 0; i3 < fmCapabilities.length; i3++) {
                        com.android.server.am.nano.FrameworkCapability cap3 = new com.android.server.am.nano.FrameworkCapability();
                        cap3.name = fmCapabilities[i3];
                        capabilities.frameworkCapabilities[i3] = cap3;
                    }
                    com.android.server.am.nano.VMInfo vmInfo = new com.android.server.am.nano.VMInfo();
                    vmInfo.name = vmName;
                    vmInfo.version = vmVersion;
                    capabilities.vmInfo = vmInfo;
                    try {
                        getRawOutputStream().write(com.android.server.am.nano.Capabilities.toByteArray(capabilities));
                    } catch (java.io.IOException e) {
                        pw.println("Error while serializing capabilities protobuffer");
                    }
                } else {
                    pw.println("Format: 2");
                    for (java.lang.String capability : CAPABILITIES) {
                        pw.println(capability);
                    }
                    for (java.lang.String capability2 : android.os.Debug.getVmFeatureList()) {
                        pw.println("vm:" + capability2);
                    }
                    for (java.lang.String capability3 : android.os.Debug.getFeatureList()) {
                        pw.println("framework:" + capability3);
                    }
                    pw.println("vm_name:" + vmName);
                    pw.println("vm_version:" + vmVersion);
                }
                return 0;
            }
        }
    }

    private android.content.Intent makeIntent(int defUser) throws java.net.URISyntaxException {
        this.mStartFlags = 0;
        this.mWaitOption = false;
        this.mStopOption = false;
        this.mRepeat = 0;
        this.mProfileFile = null;
        this.mSamplingInterval = 0;
        this.mAutoStop = false;
        this.mStreaming = false;
        this.mUserId = defUser;
        this.mDisplayId = -1;
        this.mTaskDisplayAreaFeatureId = -1;
        this.mWindowingMode = 0;
        this.mActivityType = 0;
        this.mTaskId = -1;
        this.mIsTaskOverlay = false;
        this.mIsLockTask = false;
        this.mAsync = false;
        this.mBroadcastOptions = null;
        return android.content.Intent.parseCommandArgs(this, new android.content.Intent.CommandOptionHandler() { // from class: com.android.server.am.ActivityManagerShellCommand.1
            public boolean handleOption(java.lang.String opt, android.os.ShellCommand cmd) {
                if (opt.equals("-D")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mStartFlags |= 2;
                } else if (opt.equals("--suspend")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mStartFlags |= 16;
                } else if (opt.equals("-N")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mStartFlags |= 8;
                } else if (opt.equals("-W")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mWaitOption = true;
                } else if (opt.equals("-P")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mProfileFile = com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired();
                    com.android.server.am.ActivityManagerShellCommand.this.mAutoStop = true;
                } else if (opt.equals("--start-profiler")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mProfileFile = com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired();
                    com.android.server.am.ActivityManagerShellCommand.this.mAutoStop = false;
                } else if (opt.equals("--sampling")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mSamplingInterval = java.lang.Integer.parseInt(com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired());
                } else if (opt.equals("--clock-type")) {
                    java.lang.String clock_type = com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired();
                    com.android.server.am.ActivityManagerShellCommand.this.mClockType = android.app.ProfilerInfo.getClockTypeFromString(clock_type);
                } else if (opt.equals(com.android.server.am.ActivityManagerShellCommand.PROFILER_OUTPUT_VERSION_FLAG)) {
                    com.android.server.am.ActivityManagerShellCommand.this.mProfilerOutputVersion = java.lang.Integer.parseInt(com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired());
                } else if (opt.equals("--streaming")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mStreaming = true;
                } else if (opt.equals("--attach-agent")) {
                    if (com.android.server.am.ActivityManagerShellCommand.this.mAgent != null) {
                        cmd.getErrPrintWriter().println("Multiple --attach-agent(-bind) not supported");
                        return false;
                    }
                    com.android.server.am.ActivityManagerShellCommand.this.mAgent = com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired();
                    com.android.server.am.ActivityManagerShellCommand.this.mAttachAgentDuringBind = false;
                } else if (opt.equals("--attach-agent-bind")) {
                    if (com.android.server.am.ActivityManagerShellCommand.this.mAgent != null) {
                        cmd.getErrPrintWriter().println("Multiple --attach-agent(-bind) not supported");
                        return false;
                    }
                    com.android.server.am.ActivityManagerShellCommand.this.mAgent = com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired();
                    com.android.server.am.ActivityManagerShellCommand.this.mAttachAgentDuringBind = true;
                } else if (opt.equals("-R")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mRepeat = java.lang.Integer.parseInt(com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired());
                } else if (opt.equals("-S")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mStopOption = true;
                } else if (opt.equals("--track-allocation")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mStartFlags |= 4;
                } else if (opt.equals("--user")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mUserId = android.os.UserHandle.parseUserArg(com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired());
                } else if (opt.equals("--receiver-permission")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mReceiverPermission = com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired();
                } else if (opt.equals("--display")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mDisplayId = java.lang.Integer.parseInt(com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired());
                } else if (opt.equals("--task-display-area-feature-id")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mTaskDisplayAreaFeatureId = java.lang.Integer.parseInt(com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired());
                } else if (opt.equals("--windowingMode")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mWindowingMode = java.lang.Integer.parseInt(com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired());
                } else if (opt.equals("--activityType")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mActivityType = java.lang.Integer.parseInt(com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired());
                } else if (opt.equals("--task")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mTaskId = java.lang.Integer.parseInt(com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired());
                } else if (opt.equals("--task-overlay")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mIsTaskOverlay = true;
                } else if (opt.equals("--lock-task")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mIsLockTask = true;
                } else if (opt.equals("--allow-background-activity-starts")) {
                    if (com.android.server.am.ActivityManagerShellCommand.this.mBroadcastOptions == null) {
                        com.android.server.am.ActivityManagerShellCommand.this.mBroadcastOptions = android.app.BroadcastOptions.makeBasic();
                    }
                    com.android.server.am.ActivityManagerShellCommand.this.mBroadcastOptions.setBackgroundActivityStartsAllowed(true);
                } else if (opt.equals("--async")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mAsync = true;
                } else if (opt.equals("--splashscreen-show-icon")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mShowSplashScreen = true;
                } else if (opt.equals("--dismiss-keyguard-if-insecure") || opt.equals("--dismiss-keyguard")) {
                    com.android.server.am.ActivityManagerShellCommand.this.mDismissKeyguardIfInsecure = true;
                } else {
                    if (!opt.equals("--allow-fgs-start-reason")) {
                        return false;
                    }
                    int reasonCode = java.lang.Integer.parseInt(com.android.server.am.ActivityManagerShellCommand.this.getNextArgRequired());
                    com.android.server.am.ActivityManagerShellCommand.this.mBroadcastOptions = android.app.BroadcastOptions.makeBasic();
                    com.android.server.am.ActivityManagerShellCommand.this.mBroadcastOptions.setTemporaryAppAllowlist(10000L, 0, reasonCode, "");
                }
                return true;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    class ProgressWaiter extends android.os.IProgressListener.Stub {
        private final java.util.concurrent.CountDownLatch mFinishedLatch;
        private final int mUserId;

        private ProgressWaiter(int userId) {
            this.mFinishedLatch = new java.util.concurrent.CountDownLatch(1);
            this.mUserId = userId;
        }

        public void onStarted(int id, android.os.Bundle extras) {
        }

        public void onProgress(int id, int progress, android.os.Bundle extras) {
            com.android.server.utils.Slogf.d("ActivityManager", "ProgressWaiter[user=%d]: onProgress(%d, %d)", java.lang.Integer.valueOf(this.mUserId), java.lang.Integer.valueOf(id), java.lang.Integer.valueOf(progress));
        }

        public void onFinished(int id, android.os.Bundle extras) {
            com.android.server.utils.Slogf.d("ActivityManager", "ProgressWaiter[user=%d]: onFinished(%d)", java.lang.Integer.valueOf(this.mUserId), java.lang.Integer.valueOf(id));
            this.mFinishedLatch.countDown();
        }

        public java.lang.String toString() {
            return "ProgressWaiter[userId=" + this.mUserId + ", finished=" + (this.mFinishedLatch.getCount() == 0) + "]";
        }

        public boolean waitForFinish(long timeoutMillis) {
            try {
                return this.mFinishedLatch.await(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
            } catch (java.lang.InterruptedException e) {
                java.lang.System.err.println("Thread interrupted unexpectedly.");
                return false;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x00bb, code lost:
    
        getErrPrintWriter().println("Error: Intent does not match any activities: " + r15);
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00d5, code lost:
    
        return r13;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v0 */
    /* JADX WARN: Type inference failed for: r13v1, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r13v3 */
    /* JADX WARN: Type inference failed for: r20v1 */
    /* JADX WARN: Type inference failed for: r20v2, types: [android.app.ActivityOptions] */
    /* JADX WARN: Type inference failed for: r20v3 */
    /* JADX WARN: Type inference failed for: r24v1 */
    /* JADX WARN: Type inference failed for: r24v2, types: [int] */
    /* JADX WARN: Type inference failed for: r24v3 */
    /* JADX WARN: Type inference failed for: r3v16 */
    /* JADX WARN: Type inference failed for: r3v17 */
    /* JADX WARN: Type inference failed for: r3v18 */
    /* JADX WARN: Type inference failed for: r3v27, types: [android.app.ActivityOptions] */
    /* JADX WARN: Type inference failed for: r3v29, types: [android.app.ActivityOptions] */
    /* JADX WARN: Type inference failed for: r3v31, types: [android.app.ActivityOptions] */
    /* JADX WARN: Type inference failed for: r3v33, types: [android.app.ActivityOptions] */
    /* JADX WARN: Type inference failed for: r3v62 */
    /* JADX WARN: Type inference failed for: r3v63 */
    /* JADX WARN: Type inference failed for: r3v64 */
    /* JADX WARN: Type inference failed for: r3v65 */
    /* JADX WARN: Type inference failed for: r3v66 */
    /* JADX WARN: Type inference failed for: r3v67 */
    /* JADX WARN: Type inference failed for: r3v68 */
    /* JADX WARN: Type inference failed for: r3v69 */
    /* JADX WARN: Type inference failed for: r3v70 */
    /* JADX WARN: Type inference failed for: r3v71 */
    /* JADX WARN: Type inference failed for: r3v72 */
    /* JADX WARN: Type inference failed for: r3v73 */
    /* JADX WARN: Type inference failed for: r3v74 */
    /* JADX WARN: Type inference failed for: r3v75 */
    /* JADX WARN: Type inference failed for: r3v76 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int runStartActivity(java.io.PrintWriter r28) throws java.net.URISyntaxException, android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 990
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerShellCommand.runStartActivity(java.io.PrintWriter):int");
    }

    int runStartService(java.io.PrintWriter pw, boolean asForeground) throws android.os.RemoteException {
        java.io.PrintWriter err = getErrPrintWriter();
        try {
            android.content.Intent intent = makeIntent(-2);
            if (this.mUserId == -1) {
                err.println("Error: Can't start activity with user 'all'");
                return -1;
            }
            pw.println("Starting service: " + intent);
            pw.flush();
            android.content.ComponentName cn = this.mInterface.startService((android.app.IApplicationThread) null, intent, intent.getType(), asForeground, "com.android.shell", (java.lang.String) null, this.mUserId);
            if (cn == null) {
                err.println("Error: Not found; no service started.");
                return -1;
            }
            if (cn.getPackageName().equals("!")) {
                err.println("Error: Requires permission " + cn.getClassName());
                return -1;
            }
            if (cn.getPackageName().equals("!!")) {
                err.println("Error: " + cn.getClassName());
                return -1;
            }
            if (cn.getPackageName().equals("?")) {
                err.println("Error: " + cn.getClassName());
                return -1;
            }
            return 0;
        } catch (java.net.URISyntaxException e) {
            throw new java.lang.RuntimeException(e.getMessage(), e);
        }
    }

    int runStopService(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.io.PrintWriter err = getErrPrintWriter();
        try {
            android.content.Intent intent = makeIntent(-2);
            if (this.mUserId == -1) {
                err.println("Error: Can't stop activity with user 'all'");
                return -1;
            }
            pw.println("Stopping service: " + intent);
            pw.flush();
            int result = this.mInterface.stopService((android.app.IApplicationThread) null, intent, intent.getType(), this.mUserId);
            if (result == 0) {
                err.println("Service not stopped: was not running.");
                return -1;
            }
            if (result == 1) {
                err.println("Service stopped");
                return -1;
            }
            if (result == -1) {
                err.println("Error stopping service");
                return -1;
            }
            return 0;
        } catch (java.net.URISyntaxException e) {
            throw new java.lang.RuntimeException(e.getMessage(), e);
        }
    }

    static final class IntentReceiver extends android.content.IIntentReceiver.Stub {
        private static final int WAIT_TIMEOUT = 60000;
        private boolean mFinished = false;
        private final java.io.PrintWriter mPw;

        IntentReceiver(java.io.PrintWriter pw) {
            this.mPw = pw;
        }

        public void performReceive(android.content.Intent intent, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
            java.lang.String line = "Broadcast completed: result=" + resultCode;
            if (data != null) {
                line = line + ", data=\"" + data + "\"";
            }
            if (extras != null) {
                line = line + ", extras: " + extras;
            }
            this.mPw.println(line);
            this.mPw.flush();
            synchronized (this) {
                this.mFinished = true;
                notifyAll();
            }
        }

        public synchronized void waitForFinish() {
            try {
                if (!this.mFinished) {
                    wait(60000L);
                }
                if (!this.mFinished) {
                    this.mPw.println("Broadcast wait for finish timeout");
                    this.mPw.flush();
                }
            } catch (java.lang.InterruptedException e) {
                throw new java.lang.IllegalStateException(e);
            }
        }
    }

    int runSendBroadcast(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.io.PrintWriter pw2 = new java.io.PrintWriter((java.io.Writer) new android.util.TeeWriter(new java.io.Writer[]{com.android.server.am.ActivityManagerDebugConfig.LOG_WRITER_INFO, pw}));
        try {
            android.content.Intent intent = makeIntent(-2);
            intent.addFlags(4194304);
            com.android.server.am.ActivityManagerShellCommand.IntentReceiver receiver = new com.android.server.am.ActivityManagerShellCommand.IntentReceiver(pw2);
            java.lang.String[] requiredPermissions = this.mReceiverPermission == null ? null : new java.lang.String[]{this.mReceiverPermission};
            pw2.println("Broadcasting: " + intent);
            pw2.flush();
            android.os.Bundle bundle = this.mBroadcastOptions == null ? null : this.mBroadcastOptions.toBundle();
            int result = this.mInterface.broadcastIntentWithFeature((android.app.IApplicationThread) null, (java.lang.String) null, intent, (java.lang.String) null, receiver, 0, (java.lang.String) null, (android.os.Bundle) null, requiredPermissions, (java.lang.String[]) null, (java.lang.String[]) null, -1, bundle, true, false, this.mUserId);
            com.android.server.utils.Slogf.i("ActivityManager", "Enqueued broadcast %s: " + result, intent);
            if (result == 0 && !this.mAsync) {
                receiver.waitForFinish();
            }
            return 0;
        } catch (java.net.URISyntaxException e) {
            throw new java.lang.RuntimeException(e.getMessage(), e);
        }
    }

    int runTraceIpc(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String op = getNextArgRequired();
        if (op.equals("start")) {
            return runTraceIpcStart(pw);
        }
        if (op.equals("stop")) {
            return runTraceIpcStop(pw);
        }
        getErrPrintWriter().println("Error: unknown trace ipc command '" + op + "'");
        return -1;
    }

    int runTraceIpcStart(java.io.PrintWriter pw) throws android.os.RemoteException {
        pw.println("Starting IPC tracing.");
        pw.flush();
        this.mInterface.startBinderTracking();
        return 0;
    }

    int runTraceIpcStop(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.io.PrintWriter err = getErrPrintWriter();
        java.lang.String filename = null;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--dump-file")) {
                    filename = getNextArgRequired();
                } else {
                    err.println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                if (filename == null) {
                    err.println("Error: Specify filename to dump logs to.");
                    return -1;
                }
                android.os.ParcelFileDescriptor fd = openFileForSystem(filename, "w");
                if (fd == null) {
                    return -1;
                }
                if (!this.mInterface.stopBinderTrackingAndDump(fd)) {
                    err.println("STOP TRACE FAILED.");
                    return -1;
                }
                pw.println("Stopped IPC tracing. Dumping logs to: " + filename);
                return 0;
            }
        }
    }

    private int runProfile(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String process;
        android.app.ProfilerInfo profilerInfo;
        java.io.PrintWriter err = getErrPrintWriter();
        boolean start = false;
        int userId = -2;
        this.mSamplingInterval = 0;
        this.mStreaming = false;
        this.mClockType = 0;
        this.mProfilerOutputVersion = 1;
        java.lang.String cmd = getNextArgRequired();
        if ("start".equals(cmd)) {
            start = true;
            while (true) {
                java.lang.String opt = getNextOption();
                if (opt != null) {
                    if (opt.equals("--user")) {
                        userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                    } else if (opt.equals("--clock-type")) {
                        java.lang.String clock_type = getNextArgRequired();
                        this.mClockType = android.app.ProfilerInfo.getClockTypeFromString(clock_type);
                    } else if (opt.equals(PROFILER_OUTPUT_VERSION_FLAG)) {
                        this.mProfilerOutputVersion = java.lang.Integer.parseInt(getNextArgRequired());
                    } else if (opt.equals("--streaming")) {
                        this.mStreaming = true;
                    } else if (opt.equals("--sampling")) {
                        this.mSamplingInterval = java.lang.Integer.parseInt(getNextArgRequired());
                    } else {
                        err.println("Error: Unknown option: " + opt);
                        return -1;
                    }
                } else {
                    java.lang.String process2 = getNextArgRequired();
                    process = process2;
                    break;
                }
            }
        } else if ("stop".equals(cmd)) {
            while (true) {
                java.lang.String opt2 = getNextOption();
                if (opt2 != null) {
                    if (opt2.equals("--user")) {
                        userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                    } else {
                        err.println("Error: Unknown option: " + opt2);
                        return -1;
                    }
                } else {
                    java.lang.String process3 = getNextArgRequired();
                    process = process3;
                    break;
                }
            }
        } else {
            java.lang.String cmd2 = getNextArgRequired();
            if ("start".equals(cmd2)) {
                start = true;
                process = cmd;
            } else if ("stop".equals(cmd2)) {
                process = cmd;
            } else {
                throw new java.lang.IllegalArgumentException("Profile command " + cmd + " not valid");
            }
        }
        if (userId == -1) {
            err.println("Error: Can't profile with user 'all'");
            return -1;
        }
        if (!start) {
            profilerInfo = null;
        } else {
            java.lang.String profileFile = getNextArgRequired();
            android.os.ParcelFileDescriptor fd = openFileForSystem(profileFile, "w");
            if (fd == null) {
                return -1;
            }
            android.app.ProfilerInfo profilerInfo2 = new android.app.ProfilerInfo(profileFile, fd, this.mSamplingInterval, false, this.mStreaming, (java.lang.String) null, false, this.mClockType, this.mProfilerOutputVersion);
            profilerInfo = profilerInfo2;
        }
        if (!this.mInterface.profileControl(process, userId, start, profilerInfo, 0)) {
            err.println("PROFILE FAILED on process " + process);
            return -1;
        }
        return 0;
    }

    @dalvik.annotation.optimization.NeverCompile
    int runCompact(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String op = getNextArgRequired();
        boolean isFullCompact = op.equals("full");
        boolean isSomeCompact = op.equals("some");
        if (isFullCompact || isSomeCompact) {
            com.android.server.am.ProcessRecord app = getProcessFromShell();
            if (app == null) {
                getErrPrintWriter().println("Error: could not find process");
                return -1;
            }
            pw.println("Process record found pid: " + app.mPid);
            if (isFullCompact) {
                pw.println("Executing full compaction for " + app.mPid);
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mInternal.mProcLock;
                com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        this.mInternal.mOomAdjuster.mCachedAppOptimizer.compactApp(app, com.android.server.am.CachedAppOptimizer.CompactProfile.FULL, com.android.server.am.CachedAppOptimizer.CompactSource.SHELL, true);
                    } finally {
                    }
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                pw.println("Finished full compaction for " + app.mPid);
                return 0;
            }
            if (isSomeCompact) {
                pw.println("Executing some compaction for " + app.mPid);
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock2 = this.mInternal.mProcLock;
                com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock2) {
                    try {
                        this.mInternal.mOomAdjuster.mCachedAppOptimizer.compactApp(app, com.android.server.am.CachedAppOptimizer.CompactProfile.SOME, com.android.server.am.CachedAppOptimizer.CompactSource.SHELL, true);
                    } finally {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    }
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                pw.println("Finished some compaction for " + app.mPid);
                return 0;
            }
            return 0;
        }
        if (op.equals("system")) {
            pw.println("Executing system compaction");
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock3 = this.mInternal.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock3) {
                try {
                    this.mInternal.mOomAdjuster.mCachedAppOptimizer.compactAllSystem();
                } finally {
                }
            }
            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
            pw.println("Finished system compaction");
            return 0;
        }
        if (op.equals("native")) {
            java.lang.String op2 = getNextArgRequired();
            boolean isFullCompact2 = op2.equals("full");
            boolean isSomeCompact2 = op2.equals("some");
            java.lang.String pidStr = getNextArgRequired();
            try {
                int pid = java.lang.Integer.parseInt(pidStr);
                if (isFullCompact2) {
                    this.mInternal.mOomAdjuster.mCachedAppOptimizer.compactNative(com.android.server.am.CachedAppOptimizer.CompactProfile.FULL, pid);
                    return 0;
                }
                if (isSomeCompact2) {
                    this.mInternal.mOomAdjuster.mCachedAppOptimizer.compactNative(com.android.server.am.CachedAppOptimizer.CompactProfile.SOME, pid);
                    return 0;
                }
                getErrPrintWriter().println("Error: unknown compaction type '" + op2 + "'");
                return -1;
            } catch (java.lang.Exception e) {
                getErrPrintWriter().println("Error: failed to parse '" + pidStr + "' as a PID");
                return -1;
            }
        }
        getErrPrintWriter().println("Error: unknown compact command '" + op + "'");
        return -1;
    }

    @dalvik.annotation.optimization.NeverCompile
    int runFreeze(java.io.PrintWriter pw, boolean freeze) throws android.os.RemoteException {
        java.lang.String freezerOpt = getNextOption();
        boolean isSticky = false;
        if (freezerOpt != null) {
            isSticky = freezerOpt.equals("--sticky");
        }
        com.android.server.am.ProcessRecord proc = getProcessFromShell();
        if (proc == null) {
            return -1;
        }
        pw.print(freeze ? "Freezing" : "Unfreezing");
        pw.print(" process " + proc.processName);
        pw.println(" (" + proc.mPid + ") sticky=" + isSticky);
        com.android.server.am.ActivityManagerService activityManagerService = this.mInternal;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mInternal.mProcLock;
                com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
                synchronized (activityManagerGlobalLock) {
                    try {
                        proc.mOptRecord.setFreezeSticky(isSticky);
                        if (!freeze) {
                            this.mInternal.mOomAdjuster.mCachedAppOptimizer.unfreezeAppInternalLSP(proc, 0, true);
                        } else {
                            this.mInternal.mOomAdjuster.mCachedAppOptimizer.forceFreezeAppAsyncLSP(proc);
                        }
                    } catch (java.lang.Throwable th) {
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                        throw th;
                    }
                }
                com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
            } catch (java.lang.Throwable th2) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th2;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        return 0;
    }

    @dalvik.annotation.optimization.NeverCompile
    com.android.server.am.ProcessRecord getProcessFromShell() throws android.os.RemoteException {
        com.android.server.am.ProcessRecord proc = null;
        java.lang.String process = getNextArgRequired();
        try {
            int pid = java.lang.Integer.parseInt(process);
            synchronized (this.mInternal.mPidsSelfLocked) {
                proc = this.mInternal.mPidsSelfLocked.get(pid);
            }
        } catch (java.lang.NumberFormatException e) {
        }
        if (proc == null) {
            com.android.server.am.ActivityManagerGlobalLock activityManagerGlobalLock = this.mInternal.mProcLock;
            com.android.server.am.ActivityManagerService.boostPriorityForProcLockedSection();
            synchronized (activityManagerGlobalLock) {
                try {
                    android.util.ArrayMap<java.lang.String, android.util.SparseArray<com.android.server.am.ProcessRecord>> all = this.mInternal.mProcessList.getProcessNamesLOSP().getMap();
                    android.util.SparseArray<com.android.server.am.ProcessRecord> procs = all.get(process);
                    if (procs != null && procs.size() != 0) {
                        if (procs.size() > 1) {
                            getErrPrintWriter().println("Error: more than one processes found");
                            com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                            return null;
                        }
                        proc = procs.valueAt(0);
                        com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    }
                    getErrPrintWriter().println("Error: could not find process");
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    return null;
                } catch (java.lang.Throwable th) {
                    com.android.server.am.ActivityManagerService.resetPriorityAfterProcLockedSection();
                    throw th;
                }
            }
        }
        return proc;
    }

    int runDumpHeap(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String heapFile;
        java.io.PrintWriter err = getErrPrintWriter();
        boolean useForceFork = false;
        boolean useFork = false;
        boolean runGc = false;
        int userId = -2;
        java.lang.String dumpBitmaps = null;
        boolean mallocInfo = false;
        boolean managed = true;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                    if (userId == -1) {
                        err.println("Error: Can't dump heap with user 'all'");
                        return -1;
                    }
                } else if (opt.equals("-n")) {
                    managed = false;
                } else if (opt.equals("-g")) {
                    runGc = true;
                } else if (opt.equals("-m")) {
                    managed = false;
                    mallocInfo = true;
                } else if (opt.equals("--forkdump")) {
                    useFork = true;
                    err.println("enter new dump heap ");
                } else if (opt.equals("--forcedump")) {
                    useForceFork = true;
                    err.println("ignore GC when forkDump ");
                } else if (opt.equals("-b")) {
                    dumpBitmaps = getNextArg();
                    if (dumpBitmaps == null) {
                        dumpBitmaps = "png";
                    }
                } else {
                    err.println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                java.lang.String process = getNextArgRequired();
                java.lang.String heapFile2 = getNextArg();
                if (heapFile2 == null) {
                    java.time.LocalDateTime localDateTime = java.time.LocalDateTime.now(java.time.Clock.systemDefaultZone());
                    java.lang.String logNameTimeString = LOG_NAME_TIME_FORMATTER.format(localDateTime);
                    heapFile2 = "/data/local/tmp/heapdump-" + logNameTimeString + ".prof";
                }
                android.os.ParcelFileDescriptor fd = openFileForSystem(heapFile2, "w");
                if (fd == null) {
                    return -1;
                }
                if (!useFork) {
                    heapFile = heapFile2;
                } else {
                    if (useForceFork) {
                        heapFile2 = "&" + heapFile2;
                    }
                    heapFile = "&" + heapFile2;
                }
                pw.println("File: " + heapFile);
                pw.flush();
                final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
                android.os.RemoteCallback finishCallback = new android.os.RemoteCallback(new android.os.RemoteCallback.OnResultListener() { // from class: com.android.server.am.ActivityManagerShellCommand.2
                    public void onResult(android.os.Bundle result) {
                        latch.countDown();
                    }
                }, (android.os.Handler) null);
                if (!this.mInterface.dumpHeap(process, userId, managed, mallocInfo, runGc, dumpBitmaps, heapFile, fd, finishCallback)) {
                    err.println("HEAP DUMP FAILED on process " + process);
                    return -1;
                }
                pw.println("Waiting for dump to finish...");
                pw.flush();
                try {
                    latch.await();
                    return 0;
                } catch (java.lang.InterruptedException e) {
                    err.println("Caught InterruptedException");
                    return 0;
                }
            }
        }
    }

    int runSetDebugApp(java.io.PrintWriter pw) throws android.os.RemoteException {
        boolean wait = false;
        boolean persistent = false;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("-w")) {
                    wait = true;
                } else if (opt.equals("--persistent")) {
                    persistent = true;
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                java.lang.String pkg = getNextArgRequired();
                this.mInterface.setDebugApp(pkg, wait, persistent);
                return 0;
            }
        }
    }

    int runSetAgentApp(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String pkg = getNextArgRequired();
        java.lang.String agent = getNextArg();
        this.mInterface.setAgentApp(pkg, agent);
        return 0;
    }

    int runClearDebugApp(java.io.PrintWriter pw) throws android.os.RemoteException {
        this.mInterface.setDebugApp((java.lang.String) null, false, true);
        return 0;
    }

    int runSetWatchHeap(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String proc = getNextArgRequired();
        java.lang.String limit = getNextArgRequired();
        this.mInterface.setDumpHeapDebugLimit(proc, 0, java.lang.Long.parseLong(limit), (java.lang.String) null);
        return 0;
    }

    int runClearWatchHeap(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String proc = getNextArgRequired();
        this.mInterface.setDumpHeapDebugLimit(proc, 0, -1L, (java.lang.String) null);
        return 0;
    }

    int runClearStartInfo(java.io.PrintWriter pw) throws android.os.RemoteException {
        this.mInternal.enforceCallingPermission("android.permission.WRITE_SECURE_SETTINGS", "runClearStartInfo()");
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                if (userId == -2) {
                    android.content.pm.UserInfo user = this.mInterface.getCurrentUser();
                    if (user == null) {
                        return -1;
                    }
                    userId = user.id;
                }
                this.mInternal.mProcessList.getAppStartInfoTracker().clearHistoryProcessStartInfo(getNextArg(), userId);
                return 0;
            }
        }
    }

    int runStartInfoDetailedMonitoring(java.io.PrintWriter pw) throws android.os.RemoteException {
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                if (userId == -2) {
                    android.content.pm.UserInfo user = this.mInterface.getCurrentUser();
                    if (user == null) {
                        return -1;
                    }
                    userId = user.id;
                }
                this.mInternal.mProcessList.getAppStartInfoTracker().configureDetailedMonitoring(pw, getNextArg(), userId);
                return 0;
            }
        }
    }

    int runClearExitInfo(java.io.PrintWriter pw) throws android.os.RemoteException {
        this.mInternal.enforceCallingPermission("android.permission.WRITE_SECURE_SETTINGS", "runClearExitInfo()");
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                if (userId == -2) {
                    android.content.pm.UserInfo user = this.mInterface.getCurrentUser();
                    if (user == null) {
                        return -1;
                    }
                    userId = user.id;
                }
                this.mInternal.mProcessList.mAppExitInfoTracker.clearHistoryProcessExitInfo(getNextArg(), userId);
                return 0;
            }
        }
    }

    int runBugReport(java.io.PrintWriter pw) throws android.os.RemoteException {
        boolean fullBugreport = true;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--progress")) {
                    fullBugreport = false;
                    this.mInterface.requestInteractiveBugReport();
                } else if (opt.equals("--telephony")) {
                    fullBugreport = false;
                    this.mInterface.requestTelephonyBugReport("", "");
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                if (fullBugreport) {
                    this.mInterface.requestFullBugReport();
                }
                pw.println("Your lovely bug report is being created; please be patient.");
                return 0;
            }
        }
    }

    int runForceStop(java.io.PrintWriter pw) throws android.os.RemoteException {
        int userId = -1;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                java.lang.String pkgName = getNextArgRequired();
                if (this.mActivityManagerShellCommandExt.isAllowedForcestop(pkgName)) {
                    this.mInterface.forceStopPackage(pkgName, userId);
                    return 0;
                }
                return 0;
            }
        }
    }

    int runStopApp(java.io.PrintWriter pw) throws android.os.RemoteException {
        int userId = 0;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                this.mInterface.stopAppForUser(getNextArgRequired(), userId);
                return 0;
            }
        }
    }

    int runClearRecentApps(java.io.PrintWriter pw) throws android.os.RemoteException {
        this.mTaskInterface.removeAllVisibleRecentTasks();
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0021  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    int runFgsNotificationRateLimit(java.io.PrintWriter r5) throws android.os.RemoteException {
        /*
            r4 = this;
            java.lang.String r0 = r4.getNextArgRequired()
            int r1 = r0.hashCode()
            r2 = 0
            switch(r1) {
                case -1298848381: goto L17;
                case 1671308008: goto Ld;
                default: goto Lc;
            }
        Lc:
            goto L21
        Ld:
            java.lang.String r1 = "disable"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Lc
            r1 = 1
            goto L22
        L17:
            java.lang.String r1 = "enable"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Lc
            r1 = r2
            goto L22
        L21:
            r1 = -1
        L22:
            switch(r1) {
                case 0: goto L2f;
                case 1: goto L2d;
                default: goto L25;
            }
        L25:
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            java.lang.String r2 = "Argument must be either 'enable' or 'disable'"
            r1.<init>(r2)
            throw r1
        L2d:
            r1 = 0
            goto L31
        L2f:
            r1 = 1
        L31:
            android.app.IActivityManager r3 = r4.mInterface
            r3.enableFgsNotificationRateLimit(r1)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerShellCommand.runFgsNotificationRateLimit(java.io.PrintWriter):int");
    }

    int runCrash(java.io.PrintWriter pw) throws android.os.RemoteException {
        int[] userIds;
        int i;
        int i2;
        int userId = -1;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                int pid = -1;
                java.lang.String packageName = null;
                java.lang.String arg = getNextArgRequired();
                try {
                    pid = java.lang.Integer.parseInt(arg);
                } catch (java.lang.NumberFormatException e) {
                    packageName = arg;
                }
                if (userId == -1) {
                    userIds = this.mInternal.mUserController.getUserIds();
                } else {
                    userIds = new int[]{userId};
                }
                int length = userIds.length;
                int i3 = 0;
                while (i3 < length) {
                    int id = userIds[i3];
                    if (this.mInternal.mUserController.hasUserRestriction("no_debugging_features", id)) {
                        getOutPrintWriter().println("Shell does not have permission to crash packages for user " + id);
                        i = i3;
                        i2 = length;
                    } else {
                        i = i3;
                        i2 = length;
                        this.mInterface.crashApplicationWithType(-1, pid, packageName, id, "shell-induced crash", false, 5);
                    }
                    i3 = i + 1;
                    length = i2;
                }
                return 0;
            }
        }
    }

    int runKill(java.io.PrintWriter pw) throws android.os.RemoteException {
        int userId = -1;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                this.mInterface.killBackgroundProcesses(getNextArgRequired(), userId);
                return 0;
            }
        }
    }

    int runKillAll(java.io.PrintWriter pw) throws android.os.RemoteException {
        this.mInterface.killAllBackgroundProcesses();
        return 0;
    }

    int runMakeIdle(java.io.PrintWriter pw) throws android.os.RemoteException {
        int userId = -1;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                this.mInterface.makePackageIdle(getNextArgRequired(), userId);
                return 0;
            }
        }
    }

    int runSetDeterministicUidIdle(java.io.PrintWriter pw) throws android.os.RemoteException {
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                boolean deterministic = java.lang.Boolean.parseBoolean(getNextArgRequired());
                this.mInterface.setDeterministicUidIdle(deterministic);
                return 0;
            }
        }
    }

    static final class MyActivityController extends android.app.IActivityController.Stub {
        static final int RESULT_ANR_DIALOG = 0;
        static final int RESULT_ANR_KILL = 1;
        static final int RESULT_ANR_WAIT = 2;
        static final int RESULT_CRASH_DIALOG = 0;
        static final int RESULT_CRASH_KILL = 1;
        static final int RESULT_DEFAULT = 0;
        static final int RESULT_EARLY_ANR_CONTINUE = 0;
        static final int RESULT_EARLY_ANR_KILL = 1;
        static final int STATE_ANR = 3;
        static final int STATE_CRASHED = 1;
        static final int STATE_EARLY_ANR = 2;
        static final int STATE_NORMAL = 0;
        final boolean mAlwaysContinue;
        final boolean mAlwaysKill;
        final java.lang.String mGdbPort;
        java.lang.Process mGdbProcess;
        java.lang.Thread mGdbThread;
        boolean mGotGdbPrint;
        final java.io.InputStream mInput;
        final android.app.IActivityManager mInterface;
        final boolean mMonkey;
        final java.io.PrintWriter mPw;
        int mResult;
        final boolean mSimpleMode;
        int mState;
        final java.lang.String mTarget;

        MyActivityController(android.app.IActivityManager iam, java.io.PrintWriter pw, java.io.InputStream input, java.lang.String gdbPort, boolean monkey, boolean simpleMode, java.lang.String target, boolean alwaysContinue, boolean alwaysKill) {
            this.mInterface = iam;
            this.mPw = pw;
            this.mInput = input;
            this.mGdbPort = gdbPort;
            this.mMonkey = monkey;
            this.mSimpleMode = simpleMode;
            this.mTarget = target;
            this.mAlwaysContinue = alwaysContinue;
            this.mAlwaysKill = alwaysKill;
        }

        private boolean shouldHandlePackageOrProcess(java.lang.String packageOrProcess) {
            if (this.mTarget == null) {
                return true;
            }
            return this.mTarget.equals(packageOrProcess);
        }

        public boolean activityResuming(java.lang.String pkg) {
            if (!shouldHandlePackageOrProcess(pkg)) {
                return true;
            }
            synchronized (this) {
                this.mPw.println("** Activity resuming: " + pkg);
                this.mPw.flush();
            }
            return true;
        }

        public boolean activityStarting(android.content.Intent intent, java.lang.String pkg) {
            if (!shouldHandlePackageOrProcess(pkg)) {
                return true;
            }
            synchronized (this) {
                this.mPw.println("** Activity starting: " + pkg);
                this.mPw.flush();
            }
            return true;
        }

        public boolean appCrashed(java.lang.String processName, int pid, java.lang.String shortMsg, java.lang.String longMsg, long timeMillis, java.lang.String stackTrace) {
            if (!shouldHandlePackageOrProcess(processName)) {
                return true;
            }
            synchronized (this) {
                if (this.mSimpleMode) {
                    this.mPw.println("** PROCESS CRASHED: " + processName);
                } else {
                    this.mPw.println("** ERROR: PROCESS CRASHED");
                    this.mPw.println("processName: " + processName);
                    this.mPw.println("processPid: " + pid);
                    this.mPw.println("shortMsg: " + shortMsg);
                    this.mPw.println("longMsg: " + longMsg);
                    this.mPw.println("timeMillis: " + timeMillis);
                    this.mPw.println("uptime: " + android.os.SystemClock.uptimeMillis());
                    this.mPw.println("stack:");
                    this.mPw.print(stackTrace);
                    this.mPw.println("#");
                }
                this.mPw.flush();
                if (this.mAlwaysContinue) {
                    return true;
                }
                if (this.mAlwaysKill) {
                    return false;
                }
                int result = waitControllerLocked(pid, 1);
                return result != 1;
            }
        }

        public int appEarlyNotResponding(java.lang.String processName, int pid, java.lang.String annotation) {
            if (!shouldHandlePackageOrProcess(processName)) {
                return 0;
            }
            synchronized (this) {
                if (this.mSimpleMode) {
                    this.mPw.println("** EARLY PROCESS NOT RESPONDING: " + processName);
                } else {
                    this.mPw.println("** ERROR: EARLY PROCESS NOT RESPONDING");
                    this.mPw.println("processName: " + processName);
                    this.mPw.println("processPid: " + pid);
                    this.mPw.println("annotation: " + annotation);
                    this.mPw.println("uptime: " + android.os.SystemClock.uptimeMillis());
                }
                this.mPw.flush();
                if (this.mAlwaysContinue) {
                    return 0;
                }
                if (this.mAlwaysKill) {
                    return -1;
                }
                int result = waitControllerLocked(pid, 2);
                return result == 1 ? -1 : 0;
            }
        }

        public int appNotResponding(java.lang.String processName, int pid, java.lang.String processStats) {
            if (!shouldHandlePackageOrProcess(processName)) {
                return 0;
            }
            synchronized (this) {
                if (this.mSimpleMode) {
                    this.mPw.println("** PROCESS NOT RESPONDING: " + processName);
                } else {
                    this.mPw.println("** ERROR: PROCESS NOT RESPONDING");
                    this.mPw.println("processName: " + processName);
                    this.mPw.println("processPid: " + pid);
                    this.mPw.println("uptime: " + android.os.SystemClock.uptimeMillis());
                    this.mPw.println("processStats:");
                    this.mPw.print(processStats);
                    this.mPw.println("#");
                }
                this.mPw.flush();
                if (this.mAlwaysContinue) {
                    return 0;
                }
                if (this.mAlwaysKill) {
                    return -1;
                }
                int result = waitControllerLocked(pid, 3);
                if (result == 1) {
                    return -1;
                }
                return result == 2 ? 1 : 0;
            }
        }

        public int systemNotResponding(java.lang.String message) {
            if (this.mTarget != null) {
                return -1;
            }
            synchronized (this) {
                this.mPw.println("** ERROR: PROCESS NOT RESPONDING");
                if (!this.mSimpleMode) {
                    this.mPw.println("message: " + message);
                    this.mPw.println("#");
                    this.mPw.println("Allowing system to die.");
                }
                this.mPw.flush();
            }
            return -1;
        }

        void killGdbLocked() {
            this.mGotGdbPrint = false;
            if (this.mGdbProcess != null) {
                this.mPw.println("Stopping gdbserver");
                this.mPw.flush();
                this.mGdbProcess.destroy();
                this.mGdbProcess = null;
            }
            if (this.mGdbThread != null) {
                this.mGdbThread.interrupt();
                this.mGdbThread = null;
            }
        }

        int waitControllerLocked(int pid, int state) {
            if (this.mGdbPort != null) {
                killGdbLocked();
                try {
                    this.mPw.println("Starting gdbserver on port " + this.mGdbPort);
                    this.mPw.println("Do the following:");
                    this.mPw.println("  adb forward tcp:" + this.mGdbPort + " tcp:" + this.mGdbPort);
                    this.mPw.println("  gdbclient app_process :" + this.mGdbPort);
                    this.mPw.flush();
                    this.mGdbProcess = java.lang.Runtime.getRuntime().exec(new java.lang.String[]{"gdbserver", ":" + this.mGdbPort, "--attach", java.lang.Integer.toString(pid)});
                    final java.io.InputStreamReader converter = new java.io.InputStreamReader(this.mGdbProcess.getInputStream());
                    this.mGdbThread = new java.lang.Thread() { // from class: com.android.server.am.ActivityManagerShellCommand.MyActivityController.1
                        @Override // java.lang.Thread, java.lang.Runnable
                        public void run() {
                            java.io.BufferedReader in = new java.io.BufferedReader(converter);
                            int count = 0;
                            while (true) {
                                synchronized (com.android.server.am.ActivityManagerShellCommand.MyActivityController.this) {
                                    if (com.android.server.am.ActivityManagerShellCommand.MyActivityController.this.mGdbThread == null) {
                                        return;
                                    }
                                    if (count == 2) {
                                        com.android.server.am.ActivityManagerShellCommand.MyActivityController.this.mGotGdbPrint = true;
                                        com.android.server.am.ActivityManagerShellCommand.MyActivityController.this.notifyAll();
                                    }
                                    try {
                                        java.lang.String line = in.readLine();
                                        if (line == null) {
                                            return;
                                        }
                                        com.android.server.am.ActivityManagerShellCommand.MyActivityController.this.mPw.println("GDB: " + line);
                                        com.android.server.am.ActivityManagerShellCommand.MyActivityController.this.mPw.flush();
                                        count++;
                                    } catch (java.io.IOException e) {
                                        return;
                                    }
                                }
                            }
                        }
                    };
                    this.mGdbThread.start();
                    try {
                        wait(500L);
                    } catch (java.lang.InterruptedException e) {
                    }
                } catch (java.io.IOException e2) {
                    this.mPw.println("Failure starting gdbserver: " + e2);
                    this.mPw.flush();
                    killGdbLocked();
                }
            }
            this.mState = state;
            this.mPw.println("");
            printMessageForState();
            this.mPw.flush();
            while (this.mState != 0) {
                try {
                    wait();
                } catch (java.lang.InterruptedException e3) {
                }
            }
            killGdbLocked();
            return this.mResult;
        }

        void resumeController(int result) {
            synchronized (this) {
                this.mState = 0;
                this.mResult = result;
                notifyAll();
            }
        }

        void printMessageForState() {
            if ((this.mAlwaysContinue || this.mAlwaysKill) && this.mSimpleMode) {
                return;
            }
            switch (this.mState) {
                case 0:
                    this.mPw.println("Monitoring activity manager...  available commands:");
                    break;
                case 1:
                    this.mPw.println("Waiting after crash...  available commands:");
                    this.mPw.println("(c)ontinue: show crash dialog");
                    this.mPw.println("(k)ill: immediately kill app");
                    break;
                case 2:
                    this.mPw.println("Waiting after early ANR...  available commands:");
                    this.mPw.println("(c)ontinue: standard ANR processing");
                    this.mPw.println("(k)ill: immediately kill app");
                    break;
                case 3:
                    this.mPw.println("Waiting after ANR...  available commands:");
                    this.mPw.println("(c)ontinue: show ANR dialog");
                    this.mPw.println("(k)ill: immediately kill app");
                    this.mPw.println("(w)ait: wait some more");
                    break;
            }
            this.mPw.println("(q)uit: finish monitoring");
        }

        void run() throws android.os.RemoteException {
            try {
                try {
                    printMessageForState();
                    this.mPw.flush();
                    this.mInterface.setActivityController(this, this.mMonkey);
                    this.mState = 0;
                    java.io.InputStreamReader converter = new java.io.InputStreamReader(this.mInput);
                    java.io.BufferedReader in = new java.io.BufferedReader(converter);
                    while (true) {
                        java.lang.String line = in.readLine();
                        if (line == null) {
                            break;
                        }
                        boolean addNewline = true;
                        if (line.length() <= 0) {
                            addNewline = false;
                        } else {
                            if ("q".equals(line) || "quit".equals(line)) {
                                break;
                            }
                            if (this.mState == 1) {
                                if ("c".equals(line) || "continue".equals(line)) {
                                    resumeController(0);
                                } else if ("k".equals(line) || "kill".equals(line)) {
                                    resumeController(1);
                                } else {
                                    this.mPw.println("Invalid command: " + line);
                                }
                            } else if (this.mState == 3) {
                                if ("c".equals(line) || "continue".equals(line)) {
                                    resumeController(0);
                                } else if ("k".equals(line) || "kill".equals(line)) {
                                    resumeController(1);
                                } else if ("w".equals(line) || "wait".equals(line)) {
                                    resumeController(2);
                                } else {
                                    this.mPw.println("Invalid command: " + line);
                                }
                            } else if (this.mState == 2) {
                                if ("c".equals(line) || "continue".equals(line)) {
                                    resumeController(0);
                                } else if ("k".equals(line) || "kill".equals(line)) {
                                    resumeController(1);
                                } else {
                                    this.mPw.println("Invalid command: " + line);
                                }
                            } else {
                                this.mPw.println("Invalid command: " + line);
                            }
                        }
                        synchronized (this) {
                            if (addNewline) {
                                this.mPw.println("");
                                printMessageForState();
                                this.mPw.flush();
                            } else {
                                printMessageForState();
                                this.mPw.flush();
                            }
                        }
                    }
                    resumeController(0);
                } catch (java.io.IOException e) {
                    e.printStackTrace(this.mPw);
                    this.mPw.flush();
                }
            } finally {
                this.mInterface.setActivityController((android.app.IActivityController) null, this.mMonkey);
            }
        }
    }

    int runMonitor(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String gdbPort = null;
        boolean monkey = false;
        boolean simpleMode = false;
        boolean alwaysContinue = false;
        boolean alwaysKill = false;
        java.lang.String target = null;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--gdb")) {
                    gdbPort = getNextArgRequired();
                } else if (opt.equals("-p")) {
                    target = getNextArgRequired();
                } else if (opt.equals("-m")) {
                    monkey = true;
                } else if (opt.equals("-s")) {
                    simpleMode = true;
                } else if (opt.equals("-c")) {
                    alwaysContinue = true;
                } else if (opt.equals("-k")) {
                    alwaysKill = true;
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                if (alwaysContinue && alwaysKill) {
                    getErrPrintWriter().println("Error: -k and -c options can't be used together.");
                    return -1;
                }
                com.android.server.am.ActivityManagerShellCommand.MyActivityController controller = new com.android.server.am.ActivityManagerShellCommand.MyActivityController(this.mInterface, pw, getRawInputStream(), gdbPort, monkey, simpleMode, target, alwaysContinue, alwaysKill);
                controller.run();
                return 0;
            }
        }
    }

    static final class MyUidObserver extends android.app.UidObserver implements com.android.server.am.ActivityManagerService.OomAdjObserver {
        static final int STATE_NORMAL = 0;
        final java.io.InputStream mInput;
        final android.app.IActivityManager mInterface;
        final com.android.server.am.ActivityManagerService mInternal;
        final int mMask;
        final java.io.PrintWriter mPw;
        int mState;
        final int mUid;

        MyUidObserver(com.android.server.am.ActivityManagerService service, java.io.PrintWriter pw, java.io.InputStream input, int uid, int mask) {
            this.mInterface = service;
            this.mInternal = service;
            this.mPw = pw;
            this.mInput = input;
            this.mUid = uid;
            this.mMask = mask;
        }

        public void onUidStateChanged(int uid, int procState, long procStateSeq, int capability) {
            synchronized (this) {
                android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskWrites();
                try {
                    this.mPw.print(uid);
                    this.mPw.print(" procstate ");
                    this.mPw.print(com.android.server.am.ProcessList.makeProcStateString(procState));
                    this.mPw.print(" seq ");
                    this.mPw.print(procStateSeq);
                    this.mPw.print(" capability ");
                    this.mPw.println(this.mMask & capability);
                    this.mPw.flush();
                } finally {
                    android.os.StrictMode.setThreadPolicy(oldPolicy);
                }
            }
        }

        public void onUidGone(int uid, boolean disabled) {
            synchronized (this) {
                android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskWrites();
                try {
                    this.mPw.print(uid);
                    this.mPw.print(" gone");
                    if (disabled) {
                        this.mPw.print(" disabled");
                    }
                    this.mPw.println();
                    this.mPw.flush();
                } finally {
                    android.os.StrictMode.setThreadPolicy(oldPolicy);
                }
            }
        }

        public void onUidActive(int uid) {
            synchronized (this) {
                android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskWrites();
                try {
                    this.mPw.print(uid);
                    this.mPw.println(" active");
                    this.mPw.flush();
                } finally {
                    android.os.StrictMode.setThreadPolicy(oldPolicy);
                }
            }
        }

        public void onUidIdle(int uid, boolean disabled) {
            synchronized (this) {
                android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskWrites();
                try {
                    this.mPw.print(uid);
                    this.mPw.print(" idle");
                    if (disabled) {
                        this.mPw.print(" disabled");
                    }
                    this.mPw.println();
                    this.mPw.flush();
                } finally {
                    android.os.StrictMode.setThreadPolicy(oldPolicy);
                }
            }
        }

        public void onUidCachedChanged(int uid, boolean cached) {
            synchronized (this) {
                android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskWrites();
                try {
                    this.mPw.print(uid);
                    this.mPw.println(cached ? " cached" : " uncached");
                    this.mPw.flush();
                } finally {
                    android.os.StrictMode.setThreadPolicy(oldPolicy);
                }
            }
        }

        @Override // com.android.server.am.ActivityManagerService.OomAdjObserver
        public void onOomAdjMessage(java.lang.String msg) {
            synchronized (this) {
                android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskWrites();
                try {
                    this.mPw.print("# ");
                    this.mPw.println(msg);
                    this.mPw.flush();
                } finally {
                    android.os.StrictMode.setThreadPolicy(oldPolicy);
                }
            }
        }

        void printMessageForState() {
            switch (this.mState) {
                case 0:
                    this.mPw.println("Watching uid states...  available commands:");
                    break;
            }
            this.mPw.println("(q)uit: finish watching");
        }

        /* JADX WARN: Removed duplicated region for block: B:35:0x0093  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        void run() throws android.os.RemoteException {
            /*
                r7 = this;
                r7.printMessageForState()     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                java.io.PrintWriter r0 = r7.mPw     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                r0.flush()     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                android.app.IActivityManager r0 = r7.mInterface     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                r1 = -1
                r2 = 0
                r3 = 31
                r0.registerUidObserver(r7, r3, r1, r2)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                int r0 = r7.mUid     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                if (r0 < 0) goto L1c
                com.android.server.am.ActivityManagerService r0 = r7.mInternal     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                int r1 = r7.mUid     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                r0.setOomAdjObserver(r1, r7)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
            L1c:
                r0 = 0
                r7.mState = r0     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                java.io.InputStreamReader r0 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                java.io.InputStream r1 = r7.mInput     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                r0.<init>(r1)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                r1.<init>(r0)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
            L2b:
                java.lang.String r2 = r1.readLine()     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                r3 = r2
                if (r2 == 0) goto L7d
                r2 = 1
                int r4 = r3.length()     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                if (r4 > 0) goto L3b
                r2 = 0
                goto L66
            L3b:
                java.lang.String r4 = "q"
                boolean r4 = r4.equals(r3)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                if (r4 != 0) goto L7d
                java.lang.String r4 = "quit"
                boolean r4 = r4.equals(r3)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                if (r4 == 0) goto L4e
                goto L7d
            L4e:
                java.io.PrintWriter r4 = r7.mPw     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                r5.<init>()     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                java.lang.String r6 = "Invalid command: "
                java.lang.StringBuilder r5 = r5.append(r6)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                java.lang.StringBuilder r5 = r5.append(r3)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                r4.println(r5)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
            L66:
                monitor-enter(r7)     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
                if (r2 == 0) goto L70
                java.io.PrintWriter r4 = r7.mPw     // Catch: java.lang.Throwable -> L7a
                java.lang.String r5 = ""
                r4.println(r5)     // Catch: java.lang.Throwable -> L7a
            L70:
                r7.printMessageForState()     // Catch: java.lang.Throwable -> L7a
                java.io.PrintWriter r4 = r7.mPw     // Catch: java.lang.Throwable -> L7a
                r4.flush()     // Catch: java.lang.Throwable -> L7a
                monitor-exit(r7)     // Catch: java.lang.Throwable -> L7a
                goto L2b
            L7a:
                r4 = move-exception
                monitor-exit(r7)     // Catch: java.lang.Throwable -> L7a
                throw r4     // Catch: java.lang.Throwable -> L82 java.io.IOException -> L84
            L7d:
                int r0 = r7.mUid
                if (r0 < 0) goto L98
                goto L93
            L82:
                r0 = move-exception
                goto L9f
            L84:
                r0 = move-exception
                java.io.PrintWriter r1 = r7.mPw     // Catch: java.lang.Throwable -> L82
                r0.printStackTrace(r1)     // Catch: java.lang.Throwable -> L82
                java.io.PrintWriter r1 = r7.mPw     // Catch: java.lang.Throwable -> L82
                r1.flush()     // Catch: java.lang.Throwable -> L82
                int r0 = r7.mUid
                if (r0 < 0) goto L98
            L93:
                com.android.server.am.ActivityManagerService r0 = r7.mInternal
                r0.clearOomAdjObserver()
            L98:
                android.app.IActivityManager r0 = r7.mInterface
                r0.unregisterUidObserver(r7)
                return
            L9f:
                int r1 = r7.mUid
                if (r1 < 0) goto La8
                com.android.server.am.ActivityManagerService r1 = r7.mInternal
                r1.clearOomAdjObserver()
            La8:
                android.app.IActivityManager r1 = r7.mInterface
                r1.unregisterUidObserver(r7)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerShellCommand.MyUidObserver.run():void");
        }
    }

    int runWatchUids(java.io.PrintWriter pw) throws android.os.RemoteException {
        int uid = -1;
        int mask = 15;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--oom")) {
                    uid = java.lang.Integer.parseInt(getNextArgRequired());
                } else if (opt.equals("--mask")) {
                    mask = java.lang.Integer.parseInt(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                com.android.server.am.ActivityManagerShellCommand.MyUidObserver controller = new com.android.server.am.ActivityManagerShellCommand.MyUidObserver(this.mInternal, pw, getRawInputStream(), uid, mask);
                controller.run();
                return 0;
            }
        }
    }

    int runHang(java.io.PrintWriter pw) throws android.os.RemoteException {
        boolean allowRestart = false;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--allow-restart")) {
                    allowRestart = true;
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                pw.println("Hanging the system...");
                pw.flush();
                try {
                    this.mInterface.hang(getShellCallback().getShellCallbackBinder(), allowRestart);
                    return 0;
                } catch (java.lang.NullPointerException e) {
                    pw.println("Hanging failed, since caller " + android.os.Binder.getCallingPid() + " did not provide a ShellCallback!");
                    pw.flush();
                    return 1;
                }
            }
        }
    }

    int runRestart(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String opt = getNextOption();
        if (opt != null) {
            getErrPrintWriter().println("Error: Unknown option: " + opt);
            return -1;
        }
        pw.println("Restart the system...");
        pw.flush();
        this.mInterface.restart();
        return 0;
    }

    int runIdleMaintenance(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String opt = getNextOption();
        if (opt != null) {
            getErrPrintWriter().println("Error: Unknown option: " + opt);
            return -1;
        }
        pw.println("Performing idle maintenance...");
        this.mInterface.sendIdleJobTrigger();
        this.mInternal.performIdleMaintenance();
        return 0;
    }

    int runScreenCompat(java.io.PrintWriter pw) throws android.os.RemoteException {
        boolean enabled;
        int i;
        java.lang.String mode = getNextArgRequired();
        if (kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_ON.equals(mode)) {
            enabled = true;
        } else if (kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF.equals(mode)) {
            enabled = false;
        } else {
            getErrPrintWriter().println("Error: enabled mode must be 'on' or 'off' at " + mode);
            return -1;
        }
        java.lang.String packageName = getNextArgRequired();
        do {
            try {
                android.app.IActivityManager iActivityManager = this.mInterface;
                if (enabled) {
                    i = 1;
                } else {
                    i = 0;
                }
                iActivityManager.setPackageScreenCompatMode(packageName, i);
            } catch (android.os.RemoteException e) {
            }
            packageName = getNextArg();
        } while (packageName != null);
        return 0;
    }

    int runPackageImportance(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String packageName = getNextArgRequired();
        int procState = this.mInterface.getPackageProcessState(packageName, "com.android.shell");
        pw.println(android.app.ActivityManager.RunningAppProcessInfo.procStateToImportance(procState));
        return 0;
    }

    int runToUri(java.io.PrintWriter pw, int flags) throws android.os.RemoteException {
        try {
            android.content.Intent intent = makeIntent(-2);
            pw.println(intent.toUri(flags));
            return 0;
        } catch (java.net.URISyntaxException e) {
            throw new java.lang.RuntimeException(e.getMessage(), e);
        }
    }

    private boolean switchUserAndWaitForComplete(final int userId) throws java.lang.InterruptedException, android.os.RemoteException {
        android.content.pm.UserInfo currentUser = this.mInterface.getCurrentUser();
        if (currentUser != null && userId == currentUser.id) {
            return true;
        }
        final java.util.concurrent.CountDownLatch switchLatch = new java.util.concurrent.CountDownLatch(1);
        android.app.UserSwitchObserver userSwitchObserver = new android.app.UserSwitchObserver() { // from class: com.android.server.am.ActivityManagerShellCommand.3
            public void onUserSwitchComplete(int newUserId) {
                if (userId == newUserId) {
                    switchLatch.countDown();
                }
            }
        };
        try {
            this.mInterface.registerUserSwitchObserver(userSwitchObserver, com.android.server.am.ActivityManagerShellCommand.class.getName());
            boolean switched = this.mInterface.switchUser(userId);
            if (switched) {
                try {
                    switched = switchLatch.await(120000L, java.util.concurrent.TimeUnit.MILLISECONDS);
                } catch (java.lang.InterruptedException e) {
                    getErrPrintWriter().println("Error: Thread interrupted unexpectedly.");
                }
                return switched;
            }
            this.mInterface.unregisterUserSwitchObserver(userSwitchObserver);
            return false;
        } finally {
            this.mInterface.unregisterUserSwitchObserver(userSwitchObserver);
        }
    }

    int runSwitchUser(java.io.PrintWriter pw) throws android.os.RemoteException {
        boolean switched;
        boolean wait = false;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if ("-w".equals(opt)) {
                    wait = true;
                } else {
                    getErrPrintWriter().println("Error: unknown option: " + opt);
                    return -1;
                }
            } else {
                int userId = java.lang.Integer.parseInt(getNextArgRequired());
                android.os.UserManager userManager = (android.os.UserManager) this.mInternal.mContext.getSystemService(android.os.UserManager.class);
                int userSwitchable = userManager.getUserSwitchability(android.os.UserHandle.of(userId));
                if (userSwitchable != 0) {
                    getErrPrintWriter().println("Error: UserSwitchabilityResult=" + userSwitchable);
                    return -1;
                }
                android.os.Trace.traceBegin(64L, "shell_runSwitchUser");
                try {
                    if (wait) {
                        switched = switchUserAndWaitForComplete(userId);
                    } else {
                        switched = this.mInterface.switchUser(userId);
                    }
                    if (!switched) {
                        pw.printf("Error: Failed to switch to user %d\n", java.lang.Integer.valueOf(userId));
                        android.os.Trace.traceEnd(64L);
                        return 1;
                    }
                    android.os.Trace.traceEnd(64L);
                    return 0;
                } catch (java.lang.Throwable th) {
                    android.os.Trace.traceEnd(64L);
                    throw th;
                }
            }
        }
    }

    int runGetCurrentUser(java.io.PrintWriter pw) throws android.os.RemoteException {
        int userId = this.mInterface.getCurrentUserId();
        if (userId == -10000) {
            throw new java.lang.IllegalStateException("Current user not set");
        }
        pw.println(userId);
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    int runStartUser(java.io.PrintWriter printWriter) throws java.lang.Throwable {
        android.os.IProgressListener iProgressListener;
        boolean zStartProfileWithListener;
        int displayIdFromNextArg = -1;
        java.lang.Object[] objArr = false;
        while (true) {
            java.lang.String nextOption = getNextOption();
            char c = 1;
            if (nextOption != null) {
                switch (nextOption.hashCode()) {
                    case -1237221598:
                        if (!nextOption.equals("--display")) {
                            c = -1;
                        }
                        break;
                    case 1514:
                        c = !nextOption.equals("-w") ? -1 : 0;
                        break;
                    default:
                        c = -1;
                        break;
                }
                switch (c) {
                    case 0:
                        objArr = true;
                        break;
                    case 1:
                        displayIdFromNextArg = getDisplayIdFromNextArg();
                        break;
                    default:
                        getErrPrintWriter().println("Error: unknown option: " + nextOption);
                        return -1;
                }
            } else {
                int i = java.lang.Integer.parseInt(getNextArgRequired());
                android.os.IProgressListener progressWaiter = objArr == true ? new com.android.server.am.ActivityManagerShellCommand.ProgressWaiter(i) : null;
                com.android.server.pm.UserManagerInternal userManagerInternal = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
                android.app.ActivityManagerInternal activityManagerInternal = (android.app.ActivityManagerInternal) com.android.server.LocalServices.getService(android.app.ActivityManagerInternal.class);
                int profileParentId = userManagerInternal.getProfileParentId(i);
                int currentUserId = activityManagerInternal.getCurrentUserId();
                boolean z = profileParentId != i;
                boolean z2 = z && profileParentId == currentUserId;
                com.android.server.utils.Slogf.d("ActivityManager", "runStartUser(): userId=%d, parentUserId=%d, currentUserId=%d, isProfile=%b, isVisibleProfile=%b, display=%d, waiter=%s", java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(profileParentId), java.lang.Integer.valueOf(currentUserId), java.lang.Boolean.valueOf(z), java.lang.Boolean.valueOf(z2), java.lang.Integer.valueOf(displayIdFromNextArg), progressWaiter);
                java.lang.String str = "";
                android.os.Trace.traceBegin(64L, "shell_runStartUser" + i);
                try {
                    if (z2) {
                        try {
                            iProgressListener = progressWaiter;
                            com.android.server.utils.Slogf.d("ActivityManager", "calling startProfileWithListener(%d, %s)", java.lang.Integer.valueOf(i), iProgressListener);
                            zStartProfileWithListener = this.mInterface.startProfileWithListener(i, iProgressListener);
                        } catch (java.lang.Throwable th) {
                            th = th;
                            android.os.Trace.traceEnd(64L);
                            throw th;
                        }
                    } else {
                        iProgressListener = progressWaiter;
                        if (displayIdFromNextArg == -1) {
                            com.android.server.utils.Slogf.d("ActivityManager", "calling startUserInBackgroundWithListener(%d)", java.lang.Integer.valueOf(i));
                            zStartProfileWithListener = this.mInterface.startUserInBackgroundWithListener(i, iProgressListener);
                        } else {
                            if (!android.os.UserManager.isVisibleBackgroundUsersEnabled()) {
                                printWriter.println("Not supported");
                                android.os.Trace.traceEnd(64L);
                                return -1;
                            }
                            com.android.server.utils.Slogf.d("ActivityManager", "calling startUserInBackgroundVisibleOnDisplay(%d, %d, %s)", java.lang.Integer.valueOf(i), java.lang.Integer.valueOf(displayIdFromNextArg), iProgressListener);
                            zStartProfileWithListener = this.mInterface.startUserInBackgroundVisibleOnDisplay(i, displayIdFromNextArg, iProgressListener);
                            str = " on display " + displayIdFromNextArg;
                        }
                    }
                    if (objArr != false && zStartProfileWithListener) {
                        com.android.server.utils.Slogf.d("ActivityManager", "waiting %d ms", 120000);
                        zStartProfileWithListener = iProgressListener.waitForFinish(120000L);
                    }
                    android.os.Trace.traceEnd(64L);
                    if (zStartProfileWithListener) {
                        printWriter.println("Success: user started" + str);
                        return 0;
                    }
                    getErrPrintWriter().println("Error: could not start user" + str);
                    return 0;
                } catch (java.lang.Throwable th2) {
                    th = th2;
                }
            }
        }
    }

    int runUnlockUser(java.io.PrintWriter pw) throws android.os.RemoteException {
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        java.lang.String token = getNextArg();
        if (!android.text.TextUtils.isEmpty(token) && !"!".equals(token)) {
            getErrPrintWriter().println("Error: token parameter not supported");
            return -1;
        }
        java.lang.String secret = getNextArg();
        if (!android.text.TextUtils.isEmpty(secret) && !"!".equals(secret)) {
            getErrPrintWriter().println("Error: secret parameter not supported");
            return -1;
        }
        boolean success = this.mInterface.unlockUser2(userId, (android.os.IProgressListener) null);
        if (success) {
            pw.println("Success: user unlocked");
            return 0;
        }
        getErrPrintWriter().println("Error: could not unlock user");
        return 0;
    }

    static final class StopUserCallback extends android.app.IStopUserCallback.Stub {
        private boolean mFinished;
        private final int mUserId;

        private StopUserCallback(int userId) {
            this.mFinished = false;
            this.mUserId = userId;
        }

        public synchronized void waitForFinish() {
            while (!this.mFinished) {
                try {
                    wait();
                } catch (java.lang.InterruptedException e) {
                    throw new java.lang.IllegalStateException(e);
                }
            }
            com.android.server.utils.Slogf.d("ActivityManager", "user %d finished stopping", java.lang.Integer.valueOf(this.mUserId));
        }

        public synchronized void userStopped(int userId) {
            com.android.server.utils.Slogf.d("ActivityManager", "StopUserCallback: userStopped(%d)", java.lang.Integer.valueOf(userId));
            this.mFinished = true;
            notifyAll();
        }

        public synchronized void userStopAborted(int userId) {
            com.android.server.utils.Slogf.d("ActivityManager", "StopUserCallback: userStopAborted(%d)", java.lang.Integer.valueOf(userId));
            this.mFinished = true;
            notifyAll();
        }

        public java.lang.String toString() {
            return "ProgressWaiter[userId=" + this.mUserId + ", finished=" + this.mFinished + "]";
        }
    }

    int runStopUser(java.io.PrintWriter pw) throws android.os.RemoteException {
        boolean wait = false;
        boolean force = false;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if ("-w".equals(opt)) {
                    wait = true;
                } else if ("-f".equals(opt)) {
                    force = true;
                } else {
                    getErrPrintWriter().println("Error: unknown option: " + opt);
                    return -1;
                }
            } else {
                int userId = java.lang.Integer.parseInt(getNextArgRequired());
                android.app.IStopUserCallback stopUserCallback = wait ? new com.android.server.am.ActivityManagerShellCommand.StopUserCallback(userId) : null;
                com.android.server.utils.Slogf.d("ActivityManager", "Calling stopUser(%d, %b, %s)", java.lang.Integer.valueOf(userId), java.lang.Boolean.valueOf(force), stopUserCallback);
                android.os.Trace.traceBegin(64L, "shell_runStopUser-" + userId + "-[stopUser]");
                try {
                    int res = this.mInterface.stopUserExceptCertainProfiles(userId, force, stopUserCallback);
                    if (res != 0) {
                        java.lang.String txt = "";
                        switch (res) {
                            case -4:
                                txt = " (Can't stop user " + userId + " - one of its related users can't be stopped)";
                                break;
                            case -3:
                                txt = " (System user cannot be stopped)";
                                break;
                            case -2:
                                txt = " (Can't stop current user)";
                                break;
                            case -1:
                                txt = " (Unknown user " + userId + ")";
                                break;
                        }
                        getErrPrintWriter().println("Switch failed: " + res + txt);
                        return -1;
                    }
                    if (stopUserCallback != null) {
                        stopUserCallback.waitForFinish();
                    }
                    android.os.Trace.traceEnd(64L);
                    return 0;
                } finally {
                    android.os.Trace.traceEnd(64L);
                }
            }
        }
    }

    int runIsUserStopped(java.io.PrintWriter pw) {
        int userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
        boolean stopped = this.mInternal.isUserStopped(userId);
        pw.println(stopped);
        return 0;
    }

    int runGetStartedUserState(java.io.PrintWriter pw) throws android.os.RemoteException {
        this.mInternal.enforceCallingPermission("android.permission.DUMP", "runGetStartedUserState()");
        int userId = java.lang.Integer.parseInt(getNextArgRequired());
        try {
            pw.println(this.mInternal.getStartedUserState(userId));
            return 0;
        } catch (java.lang.NullPointerException e) {
            pw.println("User is not started: " + userId);
            return 0;
        }
    }

    int runTrackAssociations(java.io.PrintWriter pw) {
        this.mInternal.enforceCallingPermission("android.permission.SET_ACTIVITY_WATCHER", "runTrackAssociations()");
        com.android.server.am.ActivityManagerService activityManagerService = this.mInternal;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                if (!this.mInternal.mTrackingAssociations) {
                    this.mInternal.mTrackingAssociations = true;
                    pw.println("Association tracking started.");
                } else {
                    pw.println("Association tracking already enabled.");
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        return 0;
    }

    int runUntrackAssociations(java.io.PrintWriter pw) {
        this.mInternal.enforceCallingPermission("android.permission.SET_ACTIVITY_WATCHER", "runUntrackAssociations()");
        com.android.server.am.ActivityManagerService activityManagerService = this.mInternal;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                if (this.mInternal.mTrackingAssociations) {
                    this.mInternal.mTrackingAssociations = false;
                    this.mInternal.mAssociations.clear();
                    pw.println("Association tracking stopped.");
                } else {
                    pw.println("Association tracking not running.");
                }
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
        return 0;
    }

    int getUidState(java.io.PrintWriter pw) throws android.os.RemoteException {
        this.mInternal.enforceCallingPermission("android.permission.DUMP", "getUidState()");
        int state = this.mInternal.getUidState(java.lang.Integer.parseInt(getNextArgRequired()));
        pw.print(state);
        pw.print(" (");
        pw.printf(android.util.DebugUtils.valueToString(android.app.ActivityManager.class, "PROCESS_STATE_", state), new java.lang.Object[0]);
        pw.println(")");
        return 0;
    }

    private java.util.List<android.content.res.Configuration> getRecentConfigurations(int days) {
        android.app.usage.IUsageStatsManager usm = android.app.usage.IUsageStatsManager.Stub.asInterface(android.os.ServiceManager.getService("usagestats"));
        long now = java.lang.System.currentTimeMillis();
        long nDaysAgo = now - ((long) ((((days * 24) * 60) * 60) * 1000));
        try {
            android.content.pm.ParceledListSlice<android.app.usage.ConfigurationStats> configStatsSlice = usm.queryConfigurationStats(4, nDaysAgo, now, "com.android.shell");
            if (configStatsSlice == null) {
                return java.util.Collections.emptyList();
            }
            final android.util.ArrayMap<android.content.res.Configuration, java.lang.Integer> recentConfigs = new android.util.ArrayMap<>();
            java.util.List<android.app.usage.ConfigurationStats> configStatsList = configStatsSlice.getList();
            int configStatsListSize = configStatsList.size();
            for (int i = 0; i < configStatsListSize; i++) {
                android.app.usage.ConfigurationStats stats = configStatsList.get(i);
                int indexOfKey = recentConfigs.indexOfKey(stats.getConfiguration());
                if (indexOfKey < 0) {
                    recentConfigs.put(stats.getConfiguration(), java.lang.Integer.valueOf(stats.getActivationCount()));
                } else {
                    recentConfigs.setValueAt(indexOfKey, java.lang.Integer.valueOf(recentConfigs.valueAt(indexOfKey).intValue() + stats.getActivationCount()));
                }
            }
            java.util.Comparator<android.content.res.Configuration> comparator = new java.util.Comparator<android.content.res.Configuration>() { // from class: com.android.server.am.ActivityManagerShellCommand.4
                @Override // java.util.Comparator
                public int compare(android.content.res.Configuration a, android.content.res.Configuration b) {
                    return ((java.lang.Integer) recentConfigs.get(b)).compareTo((java.lang.Integer) recentConfigs.get(a));
                }
            };
            java.util.ArrayList<android.content.res.Configuration> configs = new java.util.ArrayList<>(recentConfigs.size());
            configs.addAll(recentConfigs.keySet());
            java.util.Collections.sort(configs, comparator);
            return configs;
        } catch (android.os.RemoteException e) {
            return java.util.Collections.emptyList();
        }
    }

    private static void addExtensionsForConfig(javax.microedition.khronos.egl.EGL10 egl, javax.microedition.khronos.egl.EGLDisplay display, javax.microedition.khronos.egl.EGLConfig config, int[] surfaceSize, int[] contextAttribs, java.util.Set<java.lang.String> glExtensions) {
        javax.microedition.khronos.egl.EGLContext context = egl.eglCreateContext(display, config, javax.microedition.khronos.egl.EGL10.EGL_NO_CONTEXT, contextAttribs);
        if (context == javax.microedition.khronos.egl.EGL10.EGL_NO_CONTEXT) {
            return;
        }
        javax.microedition.khronos.egl.EGLSurface surface = egl.eglCreatePbufferSurface(display, config, surfaceSize);
        if (surface == javax.microedition.khronos.egl.EGL10.EGL_NO_SURFACE) {
            egl.eglDestroyContext(display, context);
            return;
        }
        egl.eglMakeCurrent(display, surface, surface, context);
        java.lang.String extensionList = android.opengl.GLES10.glGetString(7939);
        if (!android.text.TextUtils.isEmpty(extensionList)) {
            for (java.lang.String extension : extensionList.split(" ")) {
                glExtensions.add(extension);
            }
        }
        egl.eglMakeCurrent(display, javax.microedition.khronos.egl.EGL10.EGL_NO_SURFACE, javax.microedition.khronos.egl.EGL10.EGL_NO_SURFACE, javax.microedition.khronos.egl.EGL10.EGL_NO_CONTEXT);
        egl.eglDestroySurface(display, surface);
        egl.eglDestroyContext(display, context);
    }

    java.util.Set<java.lang.String> getGlExtensionsFromDriver() {
        int i;
        int[] attrib;
        javax.microedition.khronos.egl.EGLConfig[] configs;
        char c;
        int[] numConfigs;
        int i2;
        java.util.Set<java.lang.String> glExtensions = new java.util.HashSet<>();
        javax.microedition.khronos.egl.EGL10 egl = (javax.microedition.khronos.egl.EGL10) javax.microedition.khronos.egl.EGLContext.getEGL();
        if (egl == null) {
            getErrPrintWriter().println("Warning: couldn't get EGL");
            return glExtensions;
        }
        javax.microedition.khronos.egl.EGLDisplay display = egl.eglGetDisplay(javax.microedition.khronos.egl.EGL10.EGL_DEFAULT_DISPLAY);
        int[] version = new int[2];
        egl.eglInitialize(display, version);
        int i3 = 1;
        int[] numConfigs2 = new int[1];
        char c2 = 0;
        if (!egl.eglGetConfigs(display, null, 0, numConfigs2)) {
            getErrPrintWriter().println("Warning: couldn't get EGL config count");
            return glExtensions;
        }
        javax.microedition.khronos.egl.EGLConfig[] configs2 = new javax.microedition.khronos.egl.EGLConfig[numConfigs2[0]];
        if (egl.eglGetConfigs(display, configs2, numConfigs2[0], numConfigs2)) {
            int[] surfaceSize = {12375, 1, 12374, 1, 12344};
            int[] gles2 = {12440, 2, 12344};
            int[] attrib2 = new int[1];
            int i4 = 0;
            while (i4 < numConfigs2[c2]) {
                egl.eglGetConfigAttrib(display, configs2[i4], 12327, attrib2);
                if (attrib2[c2] == 12368) {
                    i = i4;
                    attrib = attrib2;
                    configs = configs2;
                    c = c2;
                    numConfigs = numConfigs2;
                    i2 = i3;
                } else {
                    egl.eglGetConfigAttrib(display, configs2[i4], 12339, attrib2);
                    if ((attrib2[c2] & i3) == 0) {
                        i = i4;
                        attrib = attrib2;
                        configs = configs2;
                        c = c2;
                        numConfigs = numConfigs2;
                        i2 = i3;
                    } else {
                        egl.eglGetConfigAttrib(display, configs2[i4], 12352, attrib2);
                        if ((attrib2[c2] & i3) == 0) {
                            i = i4;
                        } else {
                            i = i4;
                            addExtensionsForConfig(egl, display, configs2[i4], surfaceSize, null, glExtensions);
                        }
                        if ((attrib2[c2] & 4) == 0) {
                            attrib = attrib2;
                            configs = configs2;
                            c = c2;
                            numConfigs = numConfigs2;
                            i2 = i3;
                        } else {
                            attrib = attrib2;
                            configs = configs2;
                            c = c2;
                            numConfigs = numConfigs2;
                            i2 = i3;
                            addExtensionsForConfig(egl, display, configs2[i], surfaceSize, gles2, glExtensions);
                        }
                    }
                }
                numConfigs2 = numConfigs;
                configs2 = configs;
                c2 = c;
                i3 = i2;
                i4 = i + 1;
                attrib2 = attrib;
            }
            egl.eglTerminate(display);
            return glExtensions;
        }
        getErrPrintWriter().println("Warning: couldn't get EGL configs");
        return glExtensions;
    }

    private void writeDeviceConfig(android.util.proto.ProtoOutputStream protoOutputStream, long fieldId, java.io.PrintWriter pw, android.content.res.Configuration config, android.util.DisplayMetrics displayMetrics) {
        com.android.internal.util.MemInfoReader memreader;
        android.app.KeyguardManager kgm;
        android.content.pm.ConfigurationInfo configInfo;
        long token = -1;
        if (protoOutputStream != null) {
            token = protoOutputStream.start(fieldId);
            protoOutputStream.write(1155346202625L, displayMetrics.widthPixels);
            protoOutputStream.write(1155346202626L, displayMetrics.heightPixels);
            protoOutputStream.write(1155346202627L, android.util.DisplayMetrics.DENSITY_DEVICE_STABLE);
        }
        if (pw != null) {
            pw.print("stable-width-px: ");
            pw.println(displayMetrics.widthPixels);
            pw.print("stable-height-px: ");
            pw.println(displayMetrics.heightPixels);
            pw.print("stable-density-dpi: ");
            pw.println(android.util.DisplayMetrics.DENSITY_DEVICE_STABLE);
        }
        com.android.internal.util.MemInfoReader memreader2 = new com.android.internal.util.MemInfoReader();
        memreader2.readMemInfo();
        android.app.KeyguardManager kgm2 = (android.app.KeyguardManager) this.mInternal.mContext.getSystemService(android.app.KeyguardManager.class);
        if (protoOutputStream != null) {
            protoOutputStream.write(1116691496964L, memreader2.getTotalSize());
            protoOutputStream.write(1133871366149L, android.app.ActivityManager.isLowRamDeviceStatic());
            protoOutputStream.write(1155346202630L, java.lang.Runtime.getRuntime().availableProcessors());
            protoOutputStream.write(1133871366151L, kgm2.isDeviceSecure());
        }
        if (pw != null) {
            pw.print("total-ram: ");
            pw.println(memreader2.getTotalSize());
            pw.print("low-ram: ");
            pw.println(android.app.ActivityManager.isLowRamDeviceStatic());
            pw.print("max-cores: ");
            pw.println(java.lang.Runtime.getRuntime().availableProcessors());
            pw.print("has-secure-screen-lock: ");
            pw.println(kgm2.isDeviceSecure());
        }
        try {
            android.content.pm.ConfigurationInfo configInfo2 = this.mTaskInterface.getDeviceConfigurationInfo();
            if (configInfo2.reqGlEsVersion != 0) {
                if (protoOutputStream != null) {
                    protoOutputStream.write(1155346202632L, configInfo2.reqGlEsVersion);
                }
                if (pw != null) {
                    pw.print("opengl-version: 0x");
                    pw.println(java.lang.Integer.toHexString(configInfo2.reqGlEsVersion));
                }
            }
            java.util.Set<java.lang.String> glExtensionsSet = getGlExtensionsFromDriver();
            java.lang.String[] glExtensions = (java.lang.String[]) glExtensionsSet.toArray(new java.lang.String[glExtensionsSet.size()]);
            java.util.Arrays.sort(glExtensions);
            for (int i = 0; i < glExtensions.length; i++) {
                if (protoOutputStream != null) {
                    protoOutputStream.write(2237677961225L, glExtensions[i]);
                }
                if (pw != null) {
                    pw.print("opengl-extensions: ");
                    pw.println(glExtensions[i]);
                }
            }
            android.content.pm.PackageManager pm = this.mInternal.mContext.getPackageManager();
            java.util.List<android.content.pm.SharedLibraryInfo> slibs = pm.getSharedLibraries(0);
            java.util.Collections.sort(slibs, java.util.Comparator.comparing(new java.util.function.Function() { // from class: com.android.server.am.ActivityManagerShellCommand$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return ((android.content.pm.SharedLibraryInfo) obj).getName();
                }
            }));
            int i2 = 0;
            while (i2 < slibs.size()) {
                if (protoOutputStream == null) {
                    configInfo = configInfo2;
                } else {
                    configInfo = configInfo2;
                    protoOutputStream.write(2237677961226L, slibs.get(i2).getName());
                }
                if (pw != null) {
                    pw.print("shared-libraries: ");
                    pw.println(slibs.get(i2).getName());
                }
                i2++;
                configInfo2 = configInfo;
            }
            android.content.pm.FeatureInfo[] features = pm.getSystemAvailableFeatures();
            java.util.Arrays.sort(features, new java.util.Comparator() { // from class: com.android.server.am.ActivityManagerShellCommand$$ExternalSyntheticLambda1
                @Override // java.util.Comparator
                public final int compare(java.lang.Object obj, java.lang.Object obj2) {
                    return com.android.server.am.ActivityManagerShellCommand.lambda$writeDeviceConfig$2((android.content.pm.FeatureInfo) obj, (android.content.pm.FeatureInfo) obj2);
                }
            });
            int i3 = 0;
            while (i3 < features.length) {
                if (features[i3].name == null) {
                    memreader = memreader2;
                    kgm = kgm2;
                } else {
                    if (protoOutputStream == null) {
                        memreader = memreader2;
                        kgm = kgm2;
                    } else {
                        memreader = memreader2;
                        kgm = kgm2;
                        protoOutputStream.write(2237677961227L, features[i3].name);
                    }
                    if (pw != null) {
                        pw.print("features: ");
                        pw.println(features[i3].name);
                    }
                }
                i3++;
                memreader2 = memreader;
                kgm2 = kgm;
            }
            if (protoOutputStream != null) {
                protoOutputStream.end(token);
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    static /* synthetic */ int lambda$writeDeviceConfig$2(android.content.pm.FeatureInfo o1, android.content.pm.FeatureInfo o2) {
        if (o1.name == o2.name) {
            return 0;
        }
        if (o1.name == null) {
            return -1;
        }
        if (o2.name == null) {
            return 1;
        }
        return o1.name.compareTo(o2.name);
    }

    private int getDisplayIdFromNextArg() {
        int displayId = java.lang.Integer.parseInt(getNextArgRequired());
        if (displayId < 0) {
            throw new java.lang.IllegalArgumentException("--display must be a non-negative integer");
        }
        return displayId;
    }

    int runGetConfig(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.util.List<android.content.res.Configuration> recentConfigs;
        int recentConfigSize;
        android.util.proto.ProtoOutputStream proto;
        int days = -1;
        int displayId = 0;
        boolean asProto = false;
        boolean inclDevice = false;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--days")) {
                    days = java.lang.Integer.parseInt(getNextArgRequired());
                    if (days <= 0) {
                        throw new java.lang.IllegalArgumentException("--days must be a positive integer");
                    }
                } else if (opt.equals("--proto")) {
                    asProto = true;
                } else if (opt.equals("--device")) {
                    inclDevice = true;
                } else if (opt.equals("--display")) {
                    displayId = getDisplayIdFromNextArg();
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                android.content.res.Configuration config = this.mInterface.getConfiguration();
                if (config == null) {
                    getErrPrintWriter().println("Activity manager has no configuration");
                    return -1;
                }
                android.hardware.display.DisplayManager dm = (android.hardware.display.DisplayManager) this.mInternal.mContext.getSystemService(android.hardware.display.DisplayManager.class);
                android.view.Display display = dm.getDisplay(displayId);
                if (display == null) {
                    getErrPrintWriter().println("Error: Display does not exist: " + displayId);
                    return -1;
                }
                android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
                display.getMetrics(metrics);
                if (asProto) {
                    android.util.proto.ProtoOutputStream proto2 = new android.util.proto.ProtoOutputStream(getOutFileDescriptor());
                    config.writeResConfigToProto(proto2, 1146756268033L, metrics);
                    if (!inclDevice) {
                        proto = proto2;
                    } else {
                        proto = proto2;
                        writeDeviceConfig(proto2, 1146756268034L, null, config, metrics);
                    }
                    proto.flush();
                    return 0;
                }
                pw.println("config: " + android.content.res.Configuration.resourceQualifierString(config, metrics));
                pw.println("abi: " + android.text.TextUtils.join(",", android.os.Build.SUPPORTED_ABIS));
                if (inclDevice) {
                    writeDeviceConfig(null, -1L, pw, config, metrics);
                }
                if (days >= 0 && (recentConfigSize = (recentConfigs = getRecentConfigurations(days)).size()) > 0) {
                    pw.println("recentConfigs:");
                    for (int i = 0; i < recentConfigSize; i++) {
                        pw.println("  config: " + android.content.res.Configuration.resourceQualifierString(recentConfigs.get(i)));
                    }
                    return 0;
                }
                return 0;
            }
        }
    }

    int runSuppressResizeConfigChanges(java.io.PrintWriter pw) throws android.os.RemoteException {
        boolean suppress = java.lang.Boolean.valueOf(getNextArgRequired()).booleanValue();
        this.mTaskInterface.suppressResizeConfigChanges(suppress);
        return 0;
    }

    int runSetInactive(java.io.PrintWriter pw) throws android.os.RemoteException {
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                java.lang.String packageName = getNextArgRequired();
                java.lang.String value = getNextArgRequired();
                android.app.usage.IUsageStatsManager usm = android.app.usage.IUsageStatsManager.Stub.asInterface(android.os.ServiceManager.getService("usagestats"));
                usm.setAppInactive(packageName, java.lang.Boolean.parseBoolean(value), userId);
                return 0;
            }
        }
    }

    private int bucketNameToBucketValue(java.lang.String name) {
        java.lang.String lower = name.toLowerCase();
        if (lower.startsWith("ac")) {
            return 10;
        }
        if (lower.startsWith("wo")) {
            return 20;
        }
        if (lower.startsWith("fr")) {
            return 30;
        }
        if (lower.startsWith("ra")) {
            return 40;
        }
        if (lower.startsWith("re")) {
            return 45;
        }
        if (lower.startsWith("ne")) {
            return 50;
        }
        try {
            int bucket = java.lang.Integer.parseInt(lower);
            return bucket;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: Unknown bucket: " + name);
            return -1;
        }
    }

    int runSetStandbyBucket(java.io.PrintWriter pw) throws android.os.RemoteException {
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                java.lang.String packageName = getNextArgRequired();
                java.lang.String value = getNextArgRequired();
                int bucket = bucketNameToBucketValue(value);
                if (bucket < 0) {
                    return -1;
                }
                boolean multiple = peekNextArg() != null;
                android.app.usage.IUsageStatsManager usm = android.app.usage.IUsageStatsManager.Stub.asInterface(android.os.ServiceManager.getService("usagestats"));
                if (!multiple) {
                    usm.setAppStandbyBucket(packageName, bucketNameToBucketValue(value), userId);
                } else {
                    java.util.ArrayList<android.app.usage.AppStandbyInfo> bucketInfoList = new java.util.ArrayList<>();
                    bucketInfoList.add(new android.app.usage.AppStandbyInfo(packageName, bucket));
                    while (true) {
                        java.lang.String packageName2 = getNextArg();
                        if (packageName2 == null) {
                            break;
                        }
                        int bucket2 = bucketNameToBucketValue(getNextArgRequired());
                        if (bucket2 >= 0) {
                            bucketInfoList.add(new android.app.usage.AppStandbyInfo(packageName2, bucket2));
                        }
                    }
                    android.content.pm.ParceledListSlice<android.app.usage.AppStandbyInfo> slice = new android.content.pm.ParceledListSlice<>(bucketInfoList);
                    usm.setAppStandbyBuckets(slice, userId);
                }
                return 0;
            }
        }
    }

    int runGetStandbyBucket(java.io.PrintWriter pw) throws android.os.RemoteException {
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                java.lang.String packageName = getNextArg();
                android.app.usage.IUsageStatsManager usm = android.app.usage.IUsageStatsManager.Stub.asInterface(android.os.ServiceManager.getService("usagestats"));
                if (packageName != null) {
                    int bucket = usm.getAppStandbyBucket(packageName, (java.lang.String) null, userId);
                    pw.println(bucket);
                    return 0;
                }
                android.content.pm.ParceledListSlice<android.app.usage.AppStandbyInfo> buckets = usm.getAppStandbyBuckets("com.android.shell", userId);
                for (android.app.usage.AppStandbyInfo bucketInfo : buckets.getList()) {
                    pw.print(bucketInfo.mPackageName);
                    pw.print(": ");
                    pw.println(bucketInfo.mStandbyBucket);
                }
                return 0;
            }
        }
    }

    int runGetInactive(java.io.PrintWriter pw) throws android.os.RemoteException {
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                java.lang.String packageName = getNextArgRequired();
                android.app.usage.IUsageStatsManager usm = android.app.usage.IUsageStatsManager.Stub.asInterface(android.os.ServiceManager.getService("usagestats"));
                boolean isIdle = usm.isAppInactive(packageName, userId, "com.android.shell");
                pw.println("Idle=" + isIdle);
                return 0;
            }
        }
    }

    int runSendTrimMemory(java.io.PrintWriter pw) throws android.os.RemoteException {
        byte b;
        int level;
        int userId = -2;
        do {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                java.lang.String proc = getNextArgRequired();
                java.lang.String levelArg = getNextArgRequired();
                switch (levelArg.hashCode()) {
                    case -1943119297:
                        b = !levelArg.equals("RUNNING_CRITICAL") ? (byte) -1 : (byte) 5;
                        break;
                    case -847101650:
                        b = !levelArg.equals("BACKGROUND") ? (byte) -1 : (byte) 2;
                        break;
                    case -219160669:
                        b = !levelArg.equals("RUNNING_MODERATE") ? (byte) -1 : (byte) 1;
                        break;
                    case 163769603:
                        b = !levelArg.equals("MODERATE") ? (byte) -1 : (byte) 4;
                        break;
                    case 183181625:
                        b = !levelArg.equals("COMPLETE") ? (byte) -1 : (byte) 6;
                        break;
                    case 1072631956:
                        b = !levelArg.equals("RUNNING_LOW") ? (byte) -1 : (byte) 3;
                        break;
                    case 2130809258:
                        b = !levelArg.equals("HIDDEN") ? (byte) -1 : (byte) 0;
                        break;
                    default:
                        b = -1;
                        break;
                }
                switch (b) {
                    case 0:
                        level = 20;
                        break;
                    case 1:
                        level = 5;
                        break;
                    case 2:
                        level = 40;
                        break;
                    case 3:
                        level = 10;
                        break;
                    case 4:
                        level = 60;
                        break;
                    case 5:
                        level = 15;
                        break;
                    case 6:
                        level = 80;
                        break;
                    default:
                        try {
                            level = java.lang.Integer.parseInt(levelArg);
                        } catch (java.lang.NumberFormatException e) {
                            getErrPrintWriter().println("Error: Unknown level option: " + levelArg);
                            return -1;
                        }
                        break;
                }
                if (this.mInterface.setProcessMemoryTrimLevel(proc, userId, level)) {
                    return 0;
                }
                getErrPrintWriter().println("Unknown error: failed to set trim level");
                return -1;
            }
        } while (userId != -1);
        getErrPrintWriter().println("Error: Can't use user 'all'");
        return -1;
    }

    int runDisplay(java.io.PrintWriter pw) throws android.os.RemoteException {
        byte b;
        java.lang.String op = getNextArgRequired();
        switch (op.hashCode()) {
            case 1625698700:
                if (op.equals("move-stack")) {
                    b = 0;
                    break;
                }
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return runDisplayMoveStack(pw);
            default:
                getErrPrintWriter().println("Error: unknown command '" + op + "'");
                return -1;
        }
    }

    int runStack(java.io.PrintWriter pw) throws android.os.RemoteException {
        byte b;
        java.lang.String op = getNextArgRequired();
        switch (op.hashCode()) {
            case -934610812:
                b = !op.equals("remove") ? (byte) -1 : (byte) 3;
                break;
            case 3237038:
                b = !op.equals("info") ? (byte) -1 : (byte) 2;
                break;
            case 3322014:
                b = !op.equals("list") ? (byte) -1 : (byte) 1;
                break;
            case 1022285313:
                b = !op.equals("move-task") ? (byte) -1 : (byte) 0;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return runStackMoveTask(pw);
            case 1:
                return runStackList(pw);
            case 2:
                return runRootTaskInfo(pw);
            case 3:
                return runRootTaskRemove(pw);
            default:
                getErrPrintWriter().println("Error: unknown command '" + op + "'");
                return -1;
        }
    }

    private android.graphics.Rect getBounds() {
        java.lang.String leftStr = getNextArgRequired();
        int left = java.lang.Integer.parseInt(leftStr);
        java.lang.String topStr = getNextArgRequired();
        int top = java.lang.Integer.parseInt(topStr);
        java.lang.String rightStr = getNextArgRequired();
        int right = java.lang.Integer.parseInt(rightStr);
        java.lang.String bottomStr = getNextArgRequired();
        int bottom = java.lang.Integer.parseInt(bottomStr);
        if (left < 0) {
            getErrPrintWriter().println("Error: bad left arg: " + leftStr);
            return null;
        }
        if (top < 0) {
            getErrPrintWriter().println("Error: bad top arg: " + topStr);
            return null;
        }
        if (right <= 0) {
            getErrPrintWriter().println("Error: bad right arg: " + rightStr);
            return null;
        }
        if (bottom <= 0) {
            getErrPrintWriter().println("Error: bad bottom arg: " + bottomStr);
            return null;
        }
        return new android.graphics.Rect(left, top, right, bottom);
    }

    int runDisplayMoveStack(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String rootTaskIdStr = getNextArgRequired();
        int rootTaskId = java.lang.Integer.parseInt(rootTaskIdStr);
        java.lang.String displayIdStr = getNextArgRequired();
        int displayId = java.lang.Integer.parseInt(displayIdStr);
        this.mTaskInterface.moveRootTaskToDisplay(rootTaskId, displayId);
        return 0;
    }

    int runStackMoveTask(java.io.PrintWriter pw) throws android.os.RemoteException {
        boolean toTop;
        java.lang.String taskIdStr = getNextArgRequired();
        int taskId = java.lang.Integer.parseInt(taskIdStr);
        java.lang.String rootTaskIdStr = getNextArgRequired();
        int rootTaskId = java.lang.Integer.parseInt(rootTaskIdStr);
        java.lang.String toTopStr = getNextArgRequired();
        if ("true".equals(toTopStr)) {
            toTop = true;
        } else if ("false".equals(toTopStr)) {
            toTop = false;
        } else {
            getErrPrintWriter().println("Error: bad toTop arg: " + toTopStr);
            return -1;
        }
        this.mTaskInterface.moveTaskToRootTask(taskId, rootTaskId, toTop);
        return 0;
    }

    int runStackList(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.util.List<android.app.ActivityTaskManager.RootTaskInfo> tasks = this.mTaskInterface.getAllRootTaskInfos();
        for (android.app.ActivityTaskManager.RootTaskInfo info : tasks) {
            pw.println(info);
        }
        return 0;
    }

    int runRootTaskInfo(java.io.PrintWriter pw) throws android.os.RemoteException {
        int windowingMode = java.lang.Integer.parseInt(getNextArgRequired());
        int activityType = java.lang.Integer.parseInt(getNextArgRequired());
        android.app.ActivityTaskManager.RootTaskInfo info = this.mTaskInterface.getRootTaskInfo(windowingMode, activityType);
        pw.println(info);
        return 0;
    }

    int runRootTaskRemove(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String taskIdStr = getNextArgRequired();
        int taskId = java.lang.Integer.parseInt(taskIdStr);
        this.mTaskInterface.removeTask(taskId);
        return 0;
    }

    int runTask(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String op = getNextArgRequired();
        if (op.equals("lock")) {
            return runTaskLock(pw);
        }
        if (op.equals("resizeable")) {
            return runTaskResizeable(pw);
        }
        if (op.equals("resize")) {
            return runTaskResize(pw);
        }
        if (op.equals("focus")) {
            return runTaskFocus(pw);
        }
        getErrPrintWriter().println("Error: unknown command '" + op + "'");
        return -1;
    }

    int runTaskLock(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String taskIdStr = getNextArgRequired();
        if (taskIdStr.equals("stop")) {
            this.mTaskInterface.stopSystemLockTaskMode();
        } else {
            int taskId = java.lang.Integer.parseInt(taskIdStr);
            this.mTaskInterface.startSystemLockTaskMode(taskId);
        }
        pw.println("Activity manager is " + (this.mTaskInterface.isInLockTaskMode() ? "" : "not ") + "in lockTaskMode");
        return 0;
    }

    int runTaskResizeable(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String taskIdStr = getNextArgRequired();
        int taskId = java.lang.Integer.parseInt(taskIdStr);
        java.lang.String resizeableStr = getNextArgRequired();
        int resizeableMode = java.lang.Integer.parseInt(resizeableStr);
        this.mTaskInterface.setTaskResizeable(taskId, resizeableMode);
        return 0;
    }

    int runTaskResize(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String taskIdStr = getNextArgRequired();
        int taskId = java.lang.Integer.parseInt(taskIdStr);
        android.graphics.Rect bounds = getBounds();
        if (bounds == null) {
            getErrPrintWriter().println("Error: invalid input bounds");
            return -1;
        }
        taskResize(taskId, bounds, 0, false);
        return 0;
    }

    void taskResize(int i, android.graphics.Rect rect, int i2, boolean z) throws android.os.RemoteException {
        this.mTaskInterface.resizeTask(i, rect, z ? 1 : 0);
        try {
            java.lang.Thread.sleep(i2);
        } catch (java.lang.InterruptedException e) {
        }
    }

    int moveTask(int taskId, android.graphics.Rect taskRect, android.graphics.Rect stackRect, int stepSize, int maxToTravel, boolean movingForward, boolean horizontal, int delay_ms) throws android.os.RemoteException {
        if (movingForward) {
            while (maxToTravel > 0 && ((horizontal && taskRect.right < stackRect.right) || (!horizontal && taskRect.bottom < stackRect.bottom))) {
                if (horizontal) {
                    int maxMove = java.lang.Math.min(stepSize, stackRect.right - taskRect.right);
                    maxToTravel -= maxMove;
                    taskRect.right += maxMove;
                    taskRect.left += maxMove;
                } else {
                    int maxMove2 = java.lang.Math.min(stepSize, stackRect.bottom - taskRect.bottom);
                    maxToTravel -= maxMove2;
                    taskRect.top += maxMove2;
                    taskRect.bottom += maxMove2;
                }
                taskResize(taskId, taskRect, delay_ms, false);
            }
        } else {
            while (maxToTravel < 0 && ((horizontal && taskRect.left > stackRect.left) || (!horizontal && taskRect.top > stackRect.top))) {
                if (horizontal) {
                    int maxMove3 = java.lang.Math.min(stepSize, taskRect.left - stackRect.left);
                    maxToTravel -= maxMove3;
                    taskRect.right -= maxMove3;
                    taskRect.left -= maxMove3;
                } else {
                    int maxMove4 = java.lang.Math.min(stepSize, taskRect.top - stackRect.top);
                    maxToTravel -= maxMove4;
                    taskRect.top -= maxMove4;
                    taskRect.bottom -= maxMove4;
                }
                taskResize(taskId, taskRect, delay_ms, false);
            }
        }
        return maxToTravel;
    }

    int getStepSize(int current, int target, int inStepSize, boolean greaterThanTarget) {
        int stepSize = 0;
        if (greaterThanTarget && target < current) {
            current -= inStepSize;
            stepSize = inStepSize;
            if (target > current) {
                stepSize -= target - current;
            }
        }
        if (!greaterThanTarget && target > current) {
            int current2 = current + inStepSize;
            return target < current2 ? inStepSize + (current2 - target) : inStepSize;
        }
        return stepSize;
    }

    int runTaskFocus(java.io.PrintWriter pw) throws android.os.RemoteException {
        int taskId = java.lang.Integer.parseInt(getNextArgRequired());
        pw.println("Setting focus to task " + taskId);
        this.mTaskInterface.setFocusedTask(taskId);
        return 0;
    }

    int runWrite(java.io.PrintWriter pw) {
        this.mInternal.enforceCallingPermission("android.permission.SET_ACTIVITY_WATCHER", "registerUidObserver()");
        this.mInternal.mAtmInternal.flushRecentTasks();
        pw.println("All tasks persisted.");
        return 0;
    }

    int runAttachAgent(java.io.PrintWriter pw) {
        this.mInternal.enforceCallingPermission("android.permission.SET_ACTIVITY_WATCHER", "attach-agent");
        java.lang.String process = getNextArgRequired();
        java.lang.String agent = getNextArgRequired();
        java.lang.String opt = getNextArg();
        if (opt != null) {
            pw.println("Error: Unknown option: " + opt);
            return -1;
        }
        this.mInternal.attachAgent(process, agent);
        return 0;
    }

    int runSupportsMultiwindow(java.io.PrintWriter pw) throws android.os.RemoteException {
        android.content.res.Resources res = getResources(pw);
        if (res == null) {
            return -1;
        }
        pw.println(android.app.ActivityTaskManager.supportsMultiWindow(this.mInternal.mContext));
        return 0;
    }

    int runSupportsSplitScreenMultiwindow(java.io.PrintWriter pw) throws android.os.RemoteException {
        android.content.res.Resources res = getResources(pw);
        if (res == null) {
            return -1;
        }
        pw.println(android.app.ActivityTaskManager.supportsSplitScreenMultiWindow(this.mInternal.mContext));
        return 0;
    }

    int runUpdateApplicationInfo(java.io.PrintWriter pw) throws android.os.RemoteException {
        int userid = android.os.UserHandle.parseUserArg(getNextArgRequired());
        java.util.ArrayList<java.lang.String> packages = new java.util.ArrayList<>();
        packages.add(getNextArgRequired());
        while (true) {
            java.lang.String packageName = getNextArg();
            if (packageName != null) {
                packages.add(packageName);
            } else {
                this.mInternal.scheduleApplicationInfoChanged(packages, userid);
                pw.println("Packages updated with most recent ApplicationInfos.");
                return 0;
            }
        }
    }

    int runNoHomeScreen(java.io.PrintWriter pw) throws android.os.RemoteException {
        android.content.res.Resources res = getResources(pw);
        if (res == null) {
            return -1;
        }
        pw.println(res.getBoolean(android.R.bool.config_lowPowerStandbyEnabledByDefault));
        return 0;
    }

    int runWaitForBroadcastIdle(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.io.PrintWriter pw2 = new java.io.PrintWriter((java.io.Writer) new android.util.TeeWriter(new java.io.Writer[]{com.android.server.am.ActivityManagerDebugConfig.LOG_WRITER_INFO, pw}));
        boolean flushBroadcastLoopers = false;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--flush-broadcast-loopers")) {
                    flushBroadcastLoopers = true;
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                this.mInternal.waitForBroadcastIdle(pw2, flushBroadcastLoopers);
                return 0;
            }
        }
    }

    int runWaitForBroadcastBarrier(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.io.PrintWriter pw2 = new java.io.PrintWriter((java.io.Writer) new android.util.TeeWriter(new java.io.Writer[]{com.android.server.am.ActivityManagerDebugConfig.LOG_WRITER_INFO, pw}));
        boolean flushBroadcastLoopers = false;
        boolean flushApplicationThreads = false;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--flush-broadcast-loopers")) {
                    flushBroadcastLoopers = true;
                } else if (opt.equals("--flush-application-threads")) {
                    flushApplicationThreads = true;
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                this.mInternal.waitForBroadcastBarrier(pw2, flushBroadcastLoopers, flushApplicationThreads);
                return 0;
            }
        }
    }

    int runWaitForApplicationBarrier(java.io.PrintWriter pw) throws android.os.RemoteException {
        this.mInternal.waitForApplicationBarrier(new java.io.PrintWriter((java.io.Writer) new android.util.TeeWriter(new java.io.Writer[]{com.android.server.am.ActivityManagerDebugConfig.LOG_WRITER_INFO, pw})));
        return 0;
    }

    int runWaitForBroadcastDispatch(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.io.PrintWriter pw2 = new java.io.PrintWriter((java.io.Writer) new android.util.TeeWriter(new java.io.Writer[]{com.android.server.am.ActivityManagerDebugConfig.LOG_WRITER_INFO, pw}));
        try {
            android.content.Intent intent = makeIntent(-2);
            this.mInternal.waitForBroadcastDispatch(pw2, intent);
            return 0;
        } catch (java.net.URISyntaxException e) {
            throw new java.lang.RuntimeException(e.getMessage(), e);
        }
    }

    int runSetIgnoreDeliveryGroupPolicy(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String broadcastAction = getNextArgRequired();
        this.mInternal.setIgnoreDeliveryGroupPolicy(broadcastAction);
        return 0;
    }

    int runClearIgnoreDeliveryGroupPolicy(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String broadcastAction = getNextArgRequired();
        this.mInternal.clearIgnoreDeliveryGroupPolicy(broadcastAction);
        return 0;
    }

    int runRefreshSettingsCache() throws android.os.RemoteException {
        this.mInternal.refreshSettingsCache();
        return 0;
    }

    private int runCompat(java.io.PrintWriter pw) throws android.os.RemoteException {
        com.android.server.compat.PlatformCompat platformCompat = (com.android.server.compat.PlatformCompat) android.os.ServiceManager.getService("platform_compat");
        java.lang.String toggleValue = getNextArgRequired();
        byte b = 1;
        boolean killPackage = !"--no-kill".equals(getNextOption());
        boolean toggleAll = false;
        int targetSdkVersion = -1;
        long changeId = -1;
        if (toggleValue.endsWith("-all")) {
            toggleValue = toggleValue.substring(0, toggleValue.lastIndexOf("-all"));
            toggleAll = true;
            if (!toggleValue.equals("reset")) {
                try {
                    targetSdkVersion = java.lang.Integer.parseInt(getNextArgRequired());
                } catch (java.lang.NumberFormatException e) {
                    pw.println("Invalid targetSdkVersion!");
                    return -1;
                }
            }
        } else {
            java.lang.String changeIdString = getNextArgRequired();
            try {
                changeId = java.lang.Long.parseLong(changeIdString);
            } catch (java.lang.NumberFormatException e2) {
                changeId = platformCompat.lookupChangeId(changeIdString);
            }
            if (changeId == -1) {
                pw.println("Unknown or invalid change: '" + changeIdString + "'.");
                return -1;
            }
        }
        java.lang.String packageName = getNextArgRequired();
        if (!toggleAll && !platformCompat.isKnownChangeId(changeId)) {
            pw.println("Warning! Change " + changeId + " is not known yet. Enabling/disabling it could have no effect.");
        }
        android.util.ArraySet<java.lang.Long> enabled = new android.util.ArraySet<>();
        android.util.ArraySet<java.lang.Long> disabled = new android.util.ArraySet<>();
        try {
            try {
                switch (toggleValue.hashCode()) {
                    case -1298848381:
                        b = !toggleValue.equals(com.android.server.bluetooth.IOplusBluetoothManagerServiceExt.FLAG_ENABLE) ? (byte) -1 : (byte) 0;
                        break;
                    case 108404047:
                        b = !toggleValue.equals("reset") ? (byte) -1 : (byte) 2;
                        break;
                    case 1671308008:
                        if (!toggleValue.equals("disable")) {
                            b = -1;
                        }
                        break;
                    default:
                        b = -1;
                        break;
                }
                try {
                    try {
                        switch (b) {
                            case 0:
                                if (!toggleAll) {
                                    enabled.add(java.lang.Long.valueOf(changeId));
                                    com.android.internal.compat.CompatibilityChangeConfig overrides = new com.android.internal.compat.CompatibilityChangeConfig(new android.compat.Compatibility.ChangeConfig(enabled, disabled));
                                    if (killPackage) {
                                        platformCompat.setOverrides(overrides, packageName);
                                    } else {
                                        platformCompat.setOverridesForTest(overrides, packageName);
                                    }
                                    pw.println("Enabled change " + changeId + " for " + packageName + ".");
                                    return 0;
                                }
                                try {
                                    int numChanges = platformCompat.enableTargetSdkChanges(packageName, targetSdkVersion);
                                    if (numChanges == 0) {
                                        pw.println("No changes were enabled.");
                                        return -1;
                                    }
                                    pw.println("Enabled " + numChanges + " changes gated by targetSdkVersion " + targetSdkVersion + " for " + packageName + ".");
                                    return 0;
                                } catch (java.lang.SecurityException e3) {
                                    e = e3;
                                }
                                break;
                                break;
                            case 1:
                                if (toggleAll) {
                                    int numChanges2 = platformCompat.disableTargetSdkChanges(packageName, targetSdkVersion);
                                    if (numChanges2 == 0) {
                                        pw.println("No changes were disabled.");
                                        return -1;
                                    }
                                    pw.println("Disabled " + numChanges2 + " changes gated by targetSdkVersion " + targetSdkVersion + " for " + packageName + ".");
                                    return 0;
                                }
                                try {
                                    disabled.add(java.lang.Long.valueOf(changeId));
                                    com.android.internal.compat.CompatibilityChangeConfig overrides2 = new com.android.internal.compat.CompatibilityChangeConfig(new android.compat.Compatibility.ChangeConfig(enabled, disabled));
                                    if (killPackage) {
                                        platformCompat.setOverrides(overrides2, packageName);
                                    } else {
                                        platformCompat.setOverridesForTest(overrides2, packageName);
                                    }
                                    pw.println("Disabled change " + changeId + " for " + packageName + ".");
                                    return 0;
                                } catch (java.lang.SecurityException e4) {
                                    e = e4;
                                }
                                break;
                            case 2:
                                if (toggleAll) {
                                    if (killPackage) {
                                        platformCompat.clearOverrides(packageName);
                                    } else {
                                        platformCompat.clearOverridesForTest(packageName);
                                    }
                                    pw.println("Reset all changes for " + packageName + " to default value.");
                                    return 0;
                                }
                                boolean existed = killPackage ? platformCompat.clearOverride(changeId, packageName) : platformCompat.clearOverrideForTest(changeId, packageName);
                                if (existed) {
                                    pw.println("Reset change " + changeId + " for " + packageName + " to default value.");
                                } else {
                                    pw.println("No override exists for changeId " + changeId + ".");
                                }
                                return 0;
                            default:
                                pw.println("Invalid toggle value: '" + toggleValue + "'.");
                                return -1;
                        }
                    } catch (java.lang.SecurityException e5) {
                        e = e5;
                    }
                } catch (java.lang.SecurityException e6) {
                    e = e6;
                }
            } catch (java.lang.SecurityException e7) {
                e = e7;
            }
        } catch (java.lang.SecurityException e8) {
            e = e8;
        }
        pw.println(e.getMessage());
        return -1;
    }

    private int runGetCurrentForegroundProcess(java.io.PrintWriter pw, android.app.IActivityManager iam) throws android.os.RemoteException {
        com.android.server.am.ActivityManagerShellCommand.ProcessObserver observer = new com.android.server.am.ActivityManagerShellCommand.ProcessObserver(pw, iam);
        iam.registerProcessObserver(observer);
        java.io.InputStream mInput = getRawInputStream();
        java.io.InputStreamReader converter = new java.io.InputStreamReader(mInput);
        java.io.BufferedReader in = new java.io.BufferedReader(converter);
        while (true) {
            try {
                java.lang.String line = in.readLine();
                if (line == null) {
                    break;
                }
                boolean addNewline = true;
                if (line.length() > 0) {
                    if ("q".equals(line) || "quit".equals(line)) {
                        break;
                    }
                    pw.println("Invalid command: " + line);
                } else {
                    addNewline = false;
                }
                if (addNewline) {
                    pw.println("");
                }
                pw.flush();
            } catch (java.io.IOException e) {
                e.printStackTrace();
                pw.flush();
                return 0;
            } finally {
                iam.unregisterProcessObserver(observer);
            }
        }
        return 0;
    }

    static final class ProcessObserver extends android.app.IProcessObserver.Stub {
        private android.app.IActivityManager mIam;
        private java.io.PrintWriter mPw;

        ProcessObserver(java.io.PrintWriter mPw, android.app.IActivityManager mIam) {
            this.mPw = mPw;
            this.mIam = mIam;
        }

        public void onForegroundActivitiesChanged(int pid, int uid, boolean foregroundActivities) {
            if (foregroundActivities) {
                try {
                    int prcState = this.mIam.getUidProcessState(uid, com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME);
                    if (prcState == 2) {
                        this.mPw.println("New foreground process: " + pid);
                    } else {
                        this.mPw.println("No top app found");
                    }
                    this.mPw.flush();
                } catch (android.os.RemoteException e) {
                    this.mPw.println("Error occurred in binder call");
                    this.mPw.flush();
                }
            }
        }

        public void onProcessStarted(int pid, int processUid, int packageUid, java.lang.String packageName, java.lang.String processName) {
        }

        public void onForegroundServicesChanged(int pid, int uid, int serviceTypes) {
        }

        public void onProcessDied(int pid, int uid) {
        }
    }

    private int runSetMemoryFactor(java.io.PrintWriter pw) throws android.os.RemoteException {
        byte b;
        java.lang.String levelArg = getNextArgRequired();
        int level = -1;
        switch (levelArg.hashCode()) {
            case -1986416409:
                b = !levelArg.equals(com.android.server.utils.PriorityDump.PRIORITY_ARG_NORMAL) ? (byte) -1 : (byte) 0;
                break;
            case -1560189025:
                b = !levelArg.equals(com.android.server.utils.PriorityDump.PRIORITY_ARG_CRITICAL) ? (byte) -1 : (byte) 3;
                break;
            case 75572:
                b = !levelArg.equals("LOW") ? (byte) -1 : (byte) 2;
                break;
            case 163769603:
                b = !levelArg.equals("MODERATE") ? (byte) -1 : (byte) 1;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                level = 0;
                break;
            case 1:
                level = 1;
                break;
            case 2:
                level = 2;
                break;
            case 3:
                level = 3;
                break;
            default:
                try {
                    level = java.lang.Integer.parseInt(levelArg);
                } catch (java.lang.NumberFormatException e) {
                }
                if (level < 0 || level > 3) {
                    getErrPrintWriter().println("Error: Unknown level option: " + levelArg);
                    return -1;
                }
                break;
        }
        this.mInternal.setMemFactorOverride(level);
        return 0;
    }

    private int runShowMemoryFactor(java.io.PrintWriter pw) throws android.os.RemoteException {
        int level = this.mInternal.getMemoryTrimLevel();
        switch (level) {
            case -1:
                pw.println("<UNKNOWN>");
                break;
            case 0:
                pw.println(com.android.server.utils.PriorityDump.PRIORITY_ARG_NORMAL);
                break;
            case 1:
                pw.println("MODERATE");
                break;
            case 2:
                pw.println("LOW");
                break;
            case 3:
                pw.println(com.android.server.utils.PriorityDump.PRIORITY_ARG_CRITICAL);
                break;
        }
        pw.flush();
        return 0;
    }

    private int runResetMemoryFactor(java.io.PrintWriter pw) throws android.os.RemoteException {
        this.mInternal.setMemFactorOverride(-1);
        return 0;
    }

    private int runMemoryFactor(java.io.PrintWriter pw) throws android.os.RemoteException {
        byte b;
        this.mInternal.enforceCallingPermission("android.permission.WRITE_SECURE_SETTINGS", "runMemoryFactor()");
        java.lang.String op = getNextArgRequired();
        switch (op.hashCode()) {
            case 113762:
                b = !op.equals("set") ? (byte) -1 : (byte) 0;
                break;
            case 3529469:
                b = !op.equals("show") ? (byte) -1 : (byte) 1;
                break;
            case 108404047:
                b = !op.equals("reset") ? (byte) -1 : (byte) 2;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                return runSetMemoryFactor(pw);
            case 1:
                return runShowMemoryFactor(pw);
            case 2:
                return runResetMemoryFactor(pw);
            default:
                getErrPrintWriter().println("Error: unknown command '" + op + "'");
                return -1;
        }
    }

    private int runServiceRestartBackoff(java.io.PrintWriter pw) throws android.os.RemoteException {
        byte b;
        this.mInternal.enforceCallingPermission("android.permission.SET_PROCESS_LIMIT", "runServiceRestartBackoff()");
        java.lang.String opt = getNextArgRequired();
        switch (opt.hashCode()) {
            case -1298848381:
                b = !opt.equals(com.android.server.bluetooth.IOplusBluetoothManagerServiceExt.FLAG_ENABLE) ? (byte) -1 : (byte) 0;
                break;
            case 3529469:
                b = !opt.equals("show") ? (byte) -1 : (byte) 2;
                break;
            case 1671308008:
                b = !opt.equals("disable") ? (byte) -1 : (byte) 1;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                this.mInternal.setServiceRestartBackoffEnabled(getNextArgRequired(), true, "shell");
                return 0;
            case 1:
                this.mInternal.setServiceRestartBackoffEnabled(getNextArgRequired(), false, "shell");
                return 0;
            case 2:
                pw.println(this.mInternal.isServiceRestartBackoffEnabled(getNextArgRequired()) ? com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED : com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
                return 0;
            default:
                getErrPrintWriter().println("Error: unknown command '" + opt + "'");
                return -1;
        }
    }

    private int runGetIsolatedProcesses(java.io.PrintWriter pw) throws android.os.RemoteException {
        this.mInternal.enforceCallingPermission("android.permission.DUMP", "getIsolatedProcesses()");
        java.util.List<java.lang.Integer> result = this.mInternal.mInternal.getIsolatedProcesses(java.lang.Integer.parseInt(getNextArgRequired()));
        pw.print("[");
        if (result != null) {
            int size = result.size();
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    pw.print(", ");
                }
                pw.print(result.get(i));
            }
        }
        pw.println("]");
        return 0;
    }

    private int runSetStopUserOnSwitch(java.io.PrintWriter pw) throws android.os.RemoteException {
        int value;
        this.mInternal.enforceCallingPermission("android.permission.INTERACT_ACROSS_USERS_FULL", "setStopUserOnSwitch()");
        java.lang.String arg = getNextArg();
        if (arg == null) {
            com.android.server.utils.Slogf.i("ActivityManager", "setStopUserOnSwitch(): resetting to default value");
            this.mInternal.setStopUserOnSwitch(-1);
            pw.println("Reset to default value");
            return 0;
        }
        boolean stop = java.lang.Boolean.parseBoolean(arg);
        if (stop) {
            value = 1;
        } else {
            value = 0;
        }
        com.android.server.utils.Slogf.i("ActivityManager", "runSetStopUserOnSwitch(): setting to %d (%b)", java.lang.Integer.valueOf(value), java.lang.Boolean.valueOf(stop));
        this.mInternal.setStopUserOnSwitch(value);
        pw.println("Set to " + stop);
        return 0;
    }

    private int runSetBgAbusiveUids(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String arg = getNextArg();
        com.android.server.am.AppBatteryTracker batteryTracker = (com.android.server.am.AppBatteryTracker) this.mInternal.mAppRestrictionController.getAppStateTracker(com.android.server.am.AppBatteryTracker.class);
        if (batteryTracker == null) {
            getErrPrintWriter().println("Unable to get bg battery tracker");
            return -1;
        }
        if (arg == null) {
            batteryTracker.clearDebugUidPercentage();
            return 0;
        }
        java.lang.String[] pairs = arg.split(",");
        int[] uids = new int[pairs.length];
        double[][] values = new double[pairs.length][];
        for (int i = 0; i < pairs.length; i++) {
            try {
                java.lang.String[] pair = pairs[i].split("=");
                if (pair.length != 2) {
                    getErrPrintWriter().println("Malformed input");
                    return -1;
                }
                uids[i] = java.lang.Integer.parseInt(pair[0]);
                java.lang.String[] vals = pair[1].split(":");
                if (vals.length != 5) {
                    getErrPrintWriter().println("Malformed input");
                    return -1;
                }
                values[i] = new double[vals.length];
                for (int j = 0; j < vals.length; j++) {
                    values[i][j] = java.lang.Double.parseDouble(vals[j]);
                }
            } catch (java.lang.NumberFormatException e) {
                getErrPrintWriter().println("Malformed input");
                return -1;
            }
        }
        batteryTracker.setDebugUidPercentage(uids, values);
        return 0;
    }

    private int runListBgExemptionsConfig(java.io.PrintWriter pw) throws android.os.RemoteException {
        android.util.ArraySet<java.lang.String> sysConfigs = this.mInternal.mAppRestrictionController.mBgRestrictionExemptioFromSysConfig;
        if (sysConfigs != null) {
            int size = sysConfigs.size();
            for (int i = 0; i < size; i++) {
                pw.print(sysConfigs.valueAt(i));
                pw.print(' ');
            }
            pw.println();
            return 0;
        }
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0061  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int restrictionNameToLevel(java.lang.String r4) {
        /*
            r3 = this;
            java.lang.String r0 = r4.toLowerCase()
            int r1 = r0.hashCode()
            r2 = 0
            switch(r1) {
                case -1790443964: goto L56;
                case -1502662066: goto L4b;
                case -1349088399: goto L41;
                case -1078417287: goto L36;
                case -775446516: goto L2c;
                case 824339380: goto L21;
                case 1351638995: goto L17;
                case 2052103358: goto Ld;
                default: goto Lc;
            }
        Lc:
            goto L61
        Ld:
            java.lang.String r1 = "exempted"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Lc
            r1 = 1
            goto L62
        L17:
            java.lang.String r1 = "adaptive_bucket"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Lc
            r1 = 2
            goto L62
        L21:
            java.lang.String r1 = "unrestricted"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Lc
            r1 = r2
            goto L62
        L2c:
            java.lang.String r1 = "background_restricted"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Lc
            r1 = 4
            goto L62
        L36:
            java.lang.String r1 = "force_stopped"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Lc
            r1 = 5
            goto L62
        L41:
            java.lang.String r1 = "custom"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Lc
            r1 = 7
            goto L62
        L4b:
            java.lang.String r1 = "restricted_bucket"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Lc
            r1 = 3
            goto L62
        L56:
            java.lang.String r1 = "user_launch_only"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Lc
            r1 = 6
            goto L62
        L61:
            r1 = -1
        L62:
            switch(r1) {
                case 0: goto L7b;
                case 1: goto L78;
                case 2: goto L75;
                case 3: goto L72;
                case 4: goto L6f;
                case 5: goto L6c;
                case 6: goto L69;
                case 7: goto L66;
                default: goto L65;
            }
        L65:
            return r2
        L66:
            r1 = 90
            return r1
        L69:
            r1 = 70
            return r1
        L6c:
            r1 = 60
            return r1
        L6f:
            r1 = 50
            return r1
        L72:
            r1 = 40
            return r1
        L75:
            r1 = 30
            return r1
        L78:
            r1 = 20
            return r1
        L7b:
            r1 = 10
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.am.ActivityManagerShellCommand.restrictionNameToLevel(java.lang.String):int");
    }

    int runSetBgRestrictionLevel(java.io.PrintWriter pw) throws android.os.RemoteException {
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                java.lang.String packageName = getNextArgRequired();
                java.lang.String value = getNextArgRequired();
                int level = restrictionNameToLevel(value);
                if (level == 0) {
                    pw.println("Error: invalid restriction level");
                    return -1;
                }
                try {
                    android.content.pm.PackageManager pm = this.mInternal.mContext.getPackageManager();
                    int uid = pm.getPackageUidAsUser(packageName, android.content.pm.PackageManager.PackageInfoFlags.of(4194304L), userId);
                    this.mInternal.setBackgroundRestrictionLevel(packageName, uid, userId, level, 1024, 0);
                    return 0;
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    pw.println("Error: userId:" + userId + " package:" + packageName + " is not found");
                    return -1;
                }
            }
        }
    }

    int runGetBgRestrictionLevel(java.io.PrintWriter pw) throws android.os.RemoteException {
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                java.lang.String packageName = getNextArgRequired();
                int level = this.mInternal.getBackgroundRestrictionLevel(packageName, userId);
                pw.println(android.app.ActivityManager.restrictionLevelToName(level));
                return 0;
            }
        }
    }

    int runSetForegroundServiceDelegate(java.io.PrintWriter pw) throws java.lang.Throwable {
        boolean isStart;
        int userId = -2;
        while (true) {
            java.lang.String opt = getNextOption();
            if (opt != null) {
                if (opt.equals("--user")) {
                    userId = android.os.UserHandle.parseUserArg(getNextArgRequired());
                } else {
                    getErrPrintWriter().println("Error: Unknown option: " + opt);
                    return -1;
                }
            } else {
                java.lang.String packageName = getNextArgRequired();
                java.lang.String action = getNextArgRequired();
                if ("start".equals(action)) {
                    isStart = true;
                } else if ("stop".equals(action)) {
                    isStart = false;
                } else {
                    pw.println("Error: action is either start or stop");
                    return -1;
                }
                try {
                    android.content.pm.PackageManager pm = this.mInternal.mContext.getPackageManager();
                    int uid = pm.getPackageUidAsUser(packageName, android.content.pm.PackageManager.PackageInfoFlags.of(4194304L), userId);
                    this.mInternal.setForegroundServiceDelegate(packageName, uid, isStart, 12, "FgsDelegate");
                    return 0;
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    pw.println("Error: userId:" + userId + " package:" + packageName + " is not found");
                    return -1;
                }
            }
        }
    }

    int runResetDropboxRateLimiter() throws android.os.RemoteException {
        this.mInternal.resetDropboxRateLimiter();
        return 0;
    }

    int runListDisplaysForStartingUsers(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String string;
        int[] displayIds = this.mInterface.getDisplayIdsForStartingVisibleBackgroundUsers();
        if (displayIds == null || displayIds.length == 0) {
            string = "none";
        } else {
            string = java.util.Arrays.toString(displayIds);
        }
        pw.println(string);
        return 0;
    }

    private android.content.res.Resources getResources(java.io.PrintWriter pw) throws android.os.RemoteException {
        android.content.res.Configuration config = this.mInterface.getConfiguration();
        if (config == null) {
            pw.println("Error: Activity manager has no configuration");
            return null;
        }
        android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
        metrics.setToDefaults();
        return new android.content.res.Resources(android.content.res.AssetManager.getSystem(), metrics, config);
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        dumpHelp(pw, this.mDumping);
    }

    @dalvik.annotation.optimization.NeverCompile
    static void dumpHelp(java.io.PrintWriter pw, boolean dumping) {
        if (dumping) {
            pw.println("Activity manager dump options:");
            pw.println("  [-a] [-c] [-p PACKAGE] [-h] [WHAT] ...");
            pw.println("  WHAT may be one of:");
            pw.println("    a[ctivities]: activity stack state");
            pw.println("    r[recents]: recent activities state");
            pw.println("    b[roadcasts] [PACKAGE_NAME] [history [-s]]: broadcast state");
            pw.println("    broadcast-stats [PACKAGE_NAME]: aggregated broadcast statistics");
            pw.println("    i[ntents] [PACKAGE_NAME]: pending intent state");
            pw.println("    p[rocesses] [PACKAGE_NAME]: process state");
            pw.println("    o[om]: out of memory management");
            pw.println("    perm[issions]: URI permission grant state");
            pw.println("    prov[iders] [COMP_SPEC ...]: content provider state");
            pw.println("    provider [COMP_SPEC]: provider client-side state");
            pw.println("    s[ervices] [COMP_SPEC ...]: service state");
            pw.println("    allowed-associations: current package association restrictions");
            pw.println("    as[sociations]: tracked app associations");
            pw.println("    start-info [PACKAGE_NAME]: historical process start information");
            pw.println("    exit-info [PACKAGE_NAME]: historical process exit information");
            pw.println("    lmk: stats on low memory killer");
            pw.println("    lru: raw LRU process list");
            pw.println("    binder-proxies: stats on binder objects and IPCs");
            pw.println("    settings: currently applied config settings");
            pw.println("    timers: the current ANR timer state");
            pw.println("    service [COMP_SPEC]: service client-side state");
            pw.println("    package [PACKAGE_NAME]: all state related to given package");
            pw.println("    all: dump all activities");
            pw.println("    top: dump the top activity");
            pw.println("    users: user state");
            pw.println("  WHAT may also be a COMP_SPEC to dump activities.");
            pw.println("  COMP_SPEC may be a component name (com.foo/.myApp),");
            pw.println("    a partial substring in a component name, a");
            pw.println("    hex object identifier.");
            pw.println("  -a: include all available server state.");
            pw.println("  -c: include client state.");
            pw.println("  -p: limit output to given package.");
            pw.println("  -d: limit output to given display.");
            pw.println("  --checkin: output checkin format, resetting data.");
            pw.println("  --C: output checkin format, not resetting data.");
            pw.println("  --proto: output dump in protocol buffer format.");
            pw.printf("  %s: dump just the DUMPABLE-related state of an activity. Use the %s option to list the supported DUMPABLEs\n", "--dump-dumpable", "--list-dumpables");
            pw.printf("  %s: show the available dumpables in an activity\n", "--list-dumpables");
            return;
        }
        pw.println("Activity manager (activity) commands:");
        pw.println("  help");
        pw.println("      Print this help text.");
        pw.println("  start-activity [-D] [-N] [-W] [-P <FILE>] [--start-profiler <FILE>]");
        pw.println("          [--sampling INTERVAL] [--clock-type <TYPE>] [--streaming]");
        pw.println("          [--profiler-output-version NUMBER]");
        pw.println("          [-R COUNT] [-S] [--track-allocation]");
        pw.println("          [--user <USER_ID> | current] [--suspend] <INTENT>");
        pw.println("      Start an Activity.  Options are:");
        pw.println("      -D: enable debugging");
        pw.println("      --suspend: debugged app suspend threads at startup (only with -D)");
        pw.println("      -N: enable native debugging");
        pw.println("      -W: wait for launch to complete (initial display)");
        pw.println("      --start-profiler <FILE>: start profiler and send results to <FILE>");
        pw.println("      --sampling INTERVAL: use sample profiling with INTERVAL microseconds");
        pw.println("          between samples (use with --start-profiler)");
        pw.println("      --clock-type <TYPE>: type can be wall / thread-cpu / dual. Specify");
        pw.println("          the clock that is used to report the timestamps when profiling");
        pw.println("          The default value is dual. (use with --start-profiler)");
        pw.println("      --streaming: stream the profiling output to the specified file");
        pw.println("          (use with --start-profiler)");
        pw.println("      --profiler-output-version Specify the version of the");
        pw.println("          profiling output (use with --start-profiler)");
        pw.println("      -P <FILE>: like above, but profiling stops when app goes idle");
        pw.println("      --attach-agent <agent>: attach the given agent before binding");
        pw.println("      --attach-agent-bind <agent>: attach the given agent during binding");
        pw.println("      -R: repeat the activity launch <COUNT> times.  Prior to each repeat,");
        pw.println("          the top activity will be finished.");
        pw.println("      -S: force stop the target app before starting the activity");
        pw.println("      --track-allocation: enable tracking of object allocations");
        pw.println("      --user <USER_ID> | current: Specify which user to run as; if not");
        pw.println("          specified then run as the current user.");
        pw.println("      --windowingMode <WINDOWING_MODE>: The windowing mode to launch the activity into.");
        pw.println("      --activityType <ACTIVITY_TYPE>: The activity type to launch the activity as.");
        pw.println("      --display <DISPLAY_ID>: The display to launch the activity into.");
        pw.println("      --splashscreen-icon: Show the splash screen icon on launch.");
        pw.println("  start-in-vsync");
        pw.println("      Start an Activity with vsync aligned. See `start-activity` for the");
        pw.println("      possible options.");
        pw.println("  start-service [--user <USER_ID> | current] <INTENT>");
        pw.println("      Start a Service.  Options are:");
        pw.println("      --user <USER_ID> | current: Specify which user to run as; if not");
        pw.println("          specified then run as the current user.");
        pw.println("  start-foreground-service [--user <USER_ID> | current] <INTENT>");
        pw.println("      Start a foreground Service.  Options are:");
        pw.println("      --user <USER_ID> | current: Specify which user to run as; if not");
        pw.println("          specified then run as the current user.");
        pw.println("  stop-service [--user <USER_ID> | current] <INTENT>");
        pw.println("      Stop a Service.  Options are:");
        pw.println("      --user <USER_ID> | current: Specify which user to run as; if not");
        pw.println("          specified then run as the current user.");
        pw.println("  broadcast [--user <USER_ID> | all | current]");
        pw.println("          [--receiver-permission <PERMISSION>]");
        pw.println("          [--allow-background-activity-starts]");
        pw.println("          [--async] <INTENT>");
        pw.println("      Send a broadcast Intent.  Options are:");
        pw.println("      --user <USER_ID> | all | current: Specify which user to send to; if not");
        pw.println("          specified then send to all users.");
        pw.println("      --receiver-permission <PERMISSION>: Require receiver to hold permission.");
        pw.println("      --allow-background-activity-starts: The receiver may start activities");
        pw.println("          even if in the background.");
        pw.println("      --async: Send without waiting for the completion of the receiver.");
        pw.println("  compact {some|full} <PROCESS>");
        pw.println("      Perform a single process compaction. The given <PROCESS> argument");
        pw.println("          may be either a process name or pid.");
        pw.println("      some: execute file compaction.");
        pw.println("      full: execute anon + file compaction.");
        pw.println("  compact system");
        pw.println("      Perform a full system compaction.");
        pw.println("  compact native {some|full} <pid>");
        pw.println("      Perform a native compaction for process with <pid>.");
        pw.println("      some: execute file compaction.");
        pw.println("      full: execute anon + file compaction.");
        pw.println("  freeze [--sticky] <PROCESS>");
        pw.println("      Freeze a process. The given <PROCESS> argument");
        pw.println("          may be either a process name or pid.  Options are:");
        pw.println("      --sticky: persists the frozen state for the process lifetime or");
        pw.println("                  until an unfreeze is triggered via shell");
        pw.println("  unfreeze [--sticky] <PROCESS>");
        pw.println("      Unfreeze a process. The given <PROCESS> argument");
        pw.println("          may be either a process name or pid.  Options are:");
        pw.println("      --sticky: persists the unfrozen state for the process lifetime or");
        pw.println("                  until a freeze is triggered via shell");
        pw.println("  instrument [-r] [-e <NAME> <VALUE>] [-p <FILE>] [-w]");
        pw.println("          [--user <USER_ID> | current]");
        pw.println("          [--no-hidden-api-checks [--no-test-api-access]]");
        pw.println("          [--no-isolated-storage]");
        pw.println("          [--no-window-animation] [--abi <ABI>] <COMPONENT>");
        pw.println("      Start an Instrumentation.  Typically this target <COMPONENT> is in the");
        pw.println("      form <TEST_PACKAGE>/<RUNNER_CLASS> or only <TEST_PACKAGE> if there");
        pw.println("      is only one instrumentation.  Options are:");
        pw.println("      -r: print raw results (otherwise decode REPORT_KEY_STREAMRESULT).  Use with");
        pw.println("          [-e perf true] to generate raw output for performance measurements.");
        pw.println("      -e <NAME> <VALUE>: set argument <NAME> to <VALUE>.  For test runners a");
        pw.println("          common form is [-e <testrunner_flag> <value>[,<value>...]].");
        pw.println("      -p <FILE>: write profiling data to <FILE>");
        pw.println("      -m: Write output as protobuf to stdout (machine readable)");
        pw.println("      -f <Optional PATH/TO/FILE>: Write output as protobuf to a file (machine");
        pw.println("          readable). If path is not specified, default directory and file name will");
        pw.println("          be used: /sdcard/instrument-logs/log-yyyyMMdd-hhmmss-SSS.instrumentation_data_proto");
        pw.println("      -w: wait for instrumentation to finish before returning.  Required for");
        pw.println("          test runners.");
        pw.println("      --user <USER_ID> | current: Specify user instrumentation runs in;");
        pw.println("          current user if not specified.");
        pw.println("      --no-hidden-api-checks: disable restrictions on use of hidden API.");
        pw.println("      --no-test-api-access: do not allow access to test APIs, if hidden");
        pw.println("          API checks are enabled.");
        pw.println("      --no-isolated-storage: don't use isolated storage sandbox and ");
        pw.println("          mount full external storage");
        pw.println("      --no-window-animation: turn off window animations while running.");
        pw.println("      --abi <ABI>: Launch the instrumented process with the selected ABI.");
        pw.println("          This assumes that the process supports the selected ABI.");
        pw.println("  trace-ipc [start|stop] [--dump-file <FILE>]");
        pw.println("      Trace IPC transactions.");
        pw.println("      start: start tracing IPC transactions.");
        pw.println("      stop: stop tracing IPC transactions and dump the results to file.");
        pw.println("      --dump-file <FILE>: Specify the file the trace should be dumped to.");
        pw.println("  profile start [--user <USER_ID> current]");
        pw.println("          [--clock-type <TYPE>]");
        pw.println("          [--profiler-output-version VERSION]");
        pw.println("          [--sampling INTERVAL | --streaming] <PROCESS> <FILE>");
        pw.println("      Start profiler on a process.  The given <PROCESS> argument");
        pw.println("        may be either a process name or pid.  Options are:");
        pw.println("      --user <USER_ID> | current: When supplying a process name,");
        pw.println("          specify user of process to profile; uses current user if not");
        pw.println("          specified.");
        pw.println("      --clock-type <TYPE>: use the specified clock to report timestamps.");
        pw.println("          The type can be one of wall | thread-cpu | dual. The default");
        pw.println("          value is dual.");
        pw.println("      --profiler-output-versionVERSION: specifies the output");
        pw.println("          format version");
        pw.println("      --sampling INTERVAL: use sample profiling with INTERVAL microseconds");
        pw.println("          between samples.");
        pw.println("      --streaming: stream the profiling output to the specified file.");
        pw.println("  profile stop [--user <USER_ID> current] <PROCESS>");
        pw.println("      Stop profiler on a process.  The given <PROCESS> argument");
        pw.println("        may be either a process name or pid.  Options are:");
        pw.println("      --user <USER_ID> | current: When supplying a process name,");
        pw.println("          specify user of process to profile; uses current user if not");
        pw.println("          specified.");
        pw.println("  dumpheap [--user <USER_ID> current] [-n] [-g] [-b <format>] ");
        pw.println("           <PROCESS> <FILE>");
        pw.println("      Dump the heap of a process.  The given <PROCESS> argument may");
        pw.println("        be either a process name or pid.  Options are:");
        pw.println("      -n: dump native heap instead of managed heap");
        pw.println("      -g: force GC before dumping the heap");
        pw.println("      -b <format>: dump contents of bitmaps in the format specified,");
        pw.println("         which can be \"png\", \"jpg\" or \"webp\".");
        pw.println("      --user <USER_ID> | current: When supplying a process name,");
        pw.println("          specify user of process to dump; uses current user if not specified.");
        pw.println("  set-debug-app [-w] [--persistent] <PACKAGE>");
        pw.println("      Set application <PACKAGE> to debug.  Options are:");
        pw.println("      -w: wait for debugger when application starts");
        pw.println("      --persistent: retain this value");
        pw.println("  clear-debug-app");
        pw.println("      Clear the previously set-debug-app.");
        pw.println("  set-watch-heap <PROCESS> <MEM-LIMIT>");
        pw.println("      Start monitoring pss size of <PROCESS>, if it is at or");
        pw.println("      above <HEAP-LIMIT> then a heap dump is collected for the user to report.");
        pw.println("  clear-watch-heap");
        pw.println("      Clear the previously set-watch-heap.");
        pw.println("  clear-start-info [--user <USER_ID> | all | current] <PACKAGE>");
        pw.println("      Clear process start-info for the given package.");
        pw.println("      Clear start-info for all packages if no package is provided.");
        pw.println("  start-info-detailed-monitoring [--user <USER_ID> | all | current] <PACKAGE>");
        pw.println("      Enable application start info detailed monitoring for the given package.");
        pw.println("      Disable if no package is supplied.");
        pw.println("  clear-exit-info [--user <USER_ID> | all | current] <PACKAGE>");
        pw.println("      Clear process exit-info for the given package.");
        pw.println("      Clear exit-info for all packages if no package is provided.");
        pw.println("  bug-report [--progress | --telephony]");
        pw.println("      Request bug report generation; will launch a notification");
        pw.println("        when done to select where it should be delivered. Options are:");
        pw.println("     --progress: will launch a notification right away to show its progress.");
        pw.println("     --telephony: will dump only telephony sections.");
        pw.println("  fgs-notification-rate-limit {enable | disable}");
        pw.println("     Enable/disable rate limit on FGS notification deferral policy.");
        pw.println("  force-stop [--user <USER_ID> | all | current] <PACKAGE>");
        pw.println("      Completely stop the given application package.");
        pw.println("  stop-app [--user <USER_ID> | all | current] <PACKAGE>");
        pw.println("      Stop an app and all of its services.  Unlike `force-stop` this does");
        pw.println("      not cancel the app's scheduled alarms and jobs.");
        pw.println("  crash [--user <USER_ID>] <PACKAGE|PID>");
        pw.println("      Induce a VM crash in the specified package or process");
        pw.println("  kill [--user <USER_ID> | all | current] <PACKAGE>");
        pw.println("      Kill all background processes associated with the given application.");
        pw.println("  kill-all");
        pw.println("      Kill all processes that are safe to kill (cached, etc).");
        pw.println("  make-uid-idle [--user <USER_ID> | all | current] <PACKAGE>");
        pw.println("      If the given application's uid is in the background and waiting to");
        pw.println("      become idle (not allowing background services), do that now.");
        pw.println("  set-deterministic-uid-idle [--user <USER_ID> | all | current] <true|false>");
        pw.println("      If true, sets the timing of making UIDs idle consistent and");
        pw.println("      deterministic. If false, the timing will be variable depending on");
        pw.println("      other activity on the device. The default is false.");
        pw.println("  monitor [--gdb <port>] [-p <TARGET>] [-s] [-c] [-k]");
        pw.println("      Start monitoring for crashes or ANRs.");
        pw.println("      --gdb: start gdbserv on the given port at crash/ANR");
        pw.println("      -p: only show events related to a specific process / package");
        pw.println("      -s: simple mode, only show a summary line for each event");
        pw.println("      -c: assume the input is always [c]ontinue");
        pw.println("      -k: assume the input is always [k]ill");
        pw.println("         -c and -k are mutually exclusive.");
        pw.println("  watch-uids [--oom <uid>] [--mask <capabilities integer>]");
        pw.println("      Start watching for and reporting uid state changes.");
        pw.println("      --oom: specify a uid for which to report detailed change messages.");
        pw.println("      --mask: Specify PROCESS_CAPABILITY_XXX mask to report. ");
        pw.println("              By default, it only reports FOREGROUND_LOCATION (1)");
        pw.println("              FOREGROUND_CAMERA (2), FOREGROUND_MICROPHONE (4)");
        pw.println("              and NETWORK (8). New capabilities added on or after");
        pw.println("              Android UDC will not be reported by default.");
        pw.println("  hang [--allow-restart]");
        pw.println("      Hang the system.");
        pw.println("      --allow-restart: allow watchdog to perform normal system restart");
        pw.println("  restart");
        pw.println("      Restart the user-space system.");
        pw.println("  idle-maintenance");
        pw.println("      Perform idle maintenance now.");
        pw.println("  screen-compat [on|off] <PACKAGE>");
        pw.println("      Control screen compatibility mode of <PACKAGE>.");
        pw.println("  package-importance <PACKAGE>");
        pw.println("      Print current importance of <PACKAGE>.");
        pw.println("  to-uri [INTENT]");
        pw.println("      Print the given Intent specification as a URI.");
        pw.println("  to-intent-uri [INTENT]");
        pw.println("      Print the given Intent specification as an intent: URI.");
        pw.println("  to-app-uri [INTENT]");
        pw.println("      Print the given Intent specification as an android-app: URI.");
        pw.println("  switch-user <USER_ID>");
        pw.println("      Switch to put USER_ID in the foreground, starting");
        pw.println("      execution of that user if it is currently stopped.");
        pw.println("  get-current-user");
        pw.println("      Returns id of the current foreground user.");
        pw.println("  start-user [-w] [--display DISPLAY_ID] <USER_ID>");
        pw.println("      Start USER_ID in background if it is currently stopped;");
        pw.println("      use switch-user if you want to start the user in foreground.");
        pw.println("      -w: wait for start-user to complete and the user to be unlocked.");
        pw.println("      --display <DISPLAY_ID>: starts the user visible in that display, which allows the user to launch activities on it.");
        pw.println("        (not supported on all devices; typically only on automotive builds where the vehicle has passenger displays)");
        pw.println("  unlock-user <USER_ID>");
        pw.println("      Unlock the given user.  This will only work if the user doesn't");
        pw.println("      have an LSKF (PIN/pattern/password).");
        pw.println("  stop-user [-w] [-f] <USER_ID>");
        pw.println("      Stop execution of USER_ID, not allowing it to run any");
        pw.println("      code until a later explicit start or switch to it.");
        pw.println("      -w: wait for stop-user to complete.");
        pw.println("      -f: force stop, even if user has an unstoppable parent.");
        pw.println("  is-user-stopped <USER_ID>");
        pw.println("      Returns whether <USER_ID> has been stopped or not.");
        pw.println("  get-started-user-state <USER_ID>");
        pw.println("      Gets the current state of the given started user.");
        pw.println("  track-associations");
        pw.println("      Enable association tracking.");
        pw.println("  untrack-associations");
        pw.println("      Disable and clear association tracking.");
        pw.println("  get-uid-state <UID>");
        pw.println("      Gets the process state of an app given its <UID>.");
        pw.println("  attach-agent <PROCESS> <FILE>");
        pw.println("    Attach an agent to the specified <PROCESS>, which may be either a process name or a PID.");
        pw.println("  get-config [--days N] [--device] [--proto] [--display <DISPLAY_ID>]");
        pw.println("      Retrieve the configuration and any recent configurations of the device.");
        pw.println("      --days: also return last N days of configurations that have been seen.");
        pw.println("      --device: also output global device configuration info.");
        pw.println("      --proto: return result as a proto; does not include --days info.");
        pw.println("      --display: Specify for which display to run the command; if not ");
        pw.println("          specified then run for the default display.");
        pw.println("  supports-multiwindow");
        pw.println("      Returns true if the device supports multiwindow.");
        pw.println("  supports-split-screen-multi-window");
        pw.println("      Returns true if the device supports split screen multiwindow.");
        pw.println("  suppress-resize-config-changes <true|false>");
        pw.println("      Suppresses configuration changes due to user resizing an activity/task.");
        pw.println("  set-inactive [--user <USER_ID>] <PACKAGE> true|false");
        pw.println("      Sets the inactive state of an app.");
        pw.println("  get-inactive [--user <USER_ID>] <PACKAGE>");
        pw.println("      Returns the inactive state of an app.");
        pw.println("  set-standby-bucket [--user <USER_ID>] <PACKAGE> active|working_set|frequent|rare|restricted");
        pw.println("      Puts an app in the standby bucket.");
        pw.println("  get-standby-bucket [--user <USER_ID>] <PACKAGE>");
        pw.println("      Returns the standby bucket of an app.");
        pw.println("  send-trim-memory [--user <USER_ID>] <PROCESS>");
        pw.println("          [HIDDEN|RUNNING_MODERATE|BACKGROUND|RUNNING_LOW|MODERATE|RUNNING_CRITICAL|COMPLETE]");
        pw.println("      Send a memory trim event to a <PROCESS>.  May also supply a raw trim int level.");
        pw.println("  display [COMMAND] [...]: sub-commands for operating on displays.");
        pw.println("       move-stack <STACK_ID> <DISPLAY_ID>");
        pw.println("           Move <STACK_ID> from its current display to <DISPLAY_ID>.");
        pw.println("  stack [COMMAND] [...]: sub-commands for operating on activity stacks.");
        pw.println("       move-task <TASK_ID> <STACK_ID> [true|false]");
        pw.println("           Move <TASK_ID> from its current stack to the top (true) or");
        pw.println("           bottom (false) of <STACK_ID>.");
        pw.println("       list");
        pw.println("           List all of the activity stacks and their sizes.");
        pw.println("       info <WINDOWING_MODE> <ACTIVITY_TYPE>");
        pw.println("           Display the information about activity stack in <WINDOWING_MODE> and <ACTIVITY_TYPE>.");
        pw.println("       remove <STACK_ID>");
        pw.println("           Remove stack <STACK_ID>.");
        pw.println("  task [COMMAND] [...]: sub-commands for operating on activity tasks.");
        pw.println("       lock <TASK_ID>");
        pw.println("           Bring <TASK_ID> to the front and don't allow other tasks to run.");
        pw.println("       lock stop");
        pw.println("           End the current task lock.");
        pw.println("       resizeable <TASK_ID> [0|1|2|3]");
        pw.println("           Change resizeable mode of <TASK_ID> to one of the following:");
        pw.println("           0: unresizeable");
        pw.println("           1: crop_windows");
        pw.println("           2: resizeable");
        pw.println("           3: resizeable_and_pipable");
        pw.println("       resize <TASK_ID> <LEFT> <TOP> <RIGHT> <BOTTOM>");
        pw.println("           The task is resized only if it is in multi-window windowing");
        pw.println("           mode or freeform windowing mode.");
        pw.println("  update-appinfo <USER_ID> <PACKAGE_NAME> [<PACKAGE_NAME>...]");
        pw.println("      Update the ApplicationInfo objects of the listed packages for <USER_ID>");
        pw.println("      without restarting any processes.");
        pw.println("  write");
        pw.println("      Write all pending state to storage.");
        pw.println("  compat [COMMAND] [...]: sub-commands for toggling app-compat changes.");
        pw.println("         enable|disable [--no-kill] <CHANGE_ID|CHANGE_NAME> <PACKAGE_NAME>");
        pw.println("            Toggles a change either by id or by name for <PACKAGE_NAME>.");
        pw.println("            It kills <PACKAGE_NAME> (to allow the toggle to take effect) unless --no-kill is provided.");
        pw.println("         reset <CHANGE_ID|CHANGE_NAME> <PACKAGE_NAME>");
        pw.println("            Toggles a change either by id or by name for <PACKAGE_NAME>.");
        pw.println("            It kills <PACKAGE_NAME> (to allow the toggle to take effect).");
        pw.println("         enable-all|disable-all <targetSdkVersion> <PACKAGE_NAME>");
        pw.println("            Toggles all changes that are gated by <targetSdkVersion>.");
        pw.println("         reset-all [--no-kill] <PACKAGE_NAME>");
        pw.println("            Removes all existing overrides for all changes for ");
        pw.println("            <PACKAGE_NAME> (back to default behaviour).");
        pw.println("            It kills <PACKAGE_NAME> (to allow the toggle to take effect) unless --no-kill is provided.");
        pw.println("  memory-factor [command] [...]: sub-commands for overriding memory pressure factor");
        pw.println("         set <NORMAL|MODERATE|LOW|CRITICAL>");
        pw.println("            Overrides memory pressure factor. May also supply a raw int level");
        pw.println("         show");
        pw.println("            Shows the existing memory pressure factor");
        pw.println("         reset");
        pw.println("            Removes existing override for memory pressure factor");
        pw.println("  service-restart-backoff <COMMAND> [...]: sub-commands to toggle service restart backoff policy.");
        pw.println("         enable|disable <PACKAGE_NAME>");
        pw.println("            Toggles the restart backoff policy on/off for <PACKAGE_NAME>.");
        pw.println("         show <PACKAGE_NAME>");
        pw.println("            Shows the restart backoff policy state for <PACKAGE_NAME>.");
        pw.println("  get-isolated-pids <UID>");
        pw.println("         Get the PIDs of isolated processes with packages in this <UID>");
        pw.println("  set-stop-user-on-switch [true|false]");
        pw.println("         Sets whether the current user (and its profiles) should be stopped when switching to a different user.");
        pw.println("         Without arguments, it resets to the value defined by platform.");
        pw.println("  set-bg-abusive-uids [uid=percentage][,uid=percentage...]");
        pw.println("         Force setting the battery usage of the given UID.");
        pw.println("  set-bg-restriction-level [--user <USER_ID>] <PACKAGE> unrestricted|exempted|adaptive_bucket|restricted_bucket|background_restricted|hibernation");
        pw.println("         Set an app's background restriction level which in turn map to a app standby bucket.");
        pw.println("  get-bg-restriction-level [--user <USER_ID>] <PACKAGE>");
        pw.println("         Get an app's background restriction level.");
        pw.println("  list-displays-for-starting-users");
        pw.println("         Lists the id of displays that can be used to start users on background.");
        pw.println("  set-foreground-service-delegate [--user <USER_ID>] <PACKAGE> start|stop");
        pw.println("         Start/stop an app's foreground service delegate.");
        pw.println("  set-ignore-delivery-group-policy <ACTION>");
        pw.println("         Start ignoring delivery group policy set for a broadcast action");
        pw.println("  clear-ignore-delivery-group-policy <ACTION>");
        pw.println("         Stop ignoring delivery group policy set for a broadcast action");
        pw.println("  capabilities [--protobuf]");
        pw.println("         Output am supported features (text format). Options are:");
        pw.println("         --protobuf: format output using protobuffer");
        android.content.Intent.printIntentArgsHelp(pw, "");
    }
}
