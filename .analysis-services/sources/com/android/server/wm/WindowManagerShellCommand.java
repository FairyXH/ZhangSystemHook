package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class WindowManagerShellCommand extends android.os.ShellCommand {
    private final android.view.IWindowManager mInterface;
    private final com.android.server.wm.WindowManagerService mInternal;
    private final com.android.server.wm.LetterboxConfiguration mLetterboxConfiguration;

    public WindowManagerShellCommand(com.android.server.wm.WindowManagerService service) {
        this.mInterface = service;
        this.mInternal = service;
        this.mLetterboxConfiguration = service.mLetterboxConfiguration;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0013  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public int onCommand(java.lang.String r6) {
        /*
            Method dump skipped, instruction units count: 614
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowManagerShellCommand.onCommand(java.lang.String):int");
    }

    private int getDisplayId(java.lang.String opt) {
        java.lang.String option = "-d".equals(opt) ? opt : getNextOption();
        if (option == null || !"-d".equals(option)) {
            return 0;
        }
        try {
            int displayId = java.lang.Integer.parseInt(getNextArgRequired());
            return displayId;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: bad number " + e);
            return 0;
        } catch (java.lang.IllegalArgumentException e2) {
            getErrPrintWriter().println("Error: " + e2);
            return 0;
        }
    }

    private void printInitialDisplaySize(java.io.PrintWriter pw, int displayId) {
        android.graphics.Point initialSize = new android.graphics.Point();
        android.graphics.Point baseSize = new android.graphics.Point();
        try {
            this.mInterface.getInitialDisplaySize(displayId, initialSize);
            this.mInterface.getBaseDisplaySize(displayId, baseSize);
            pw.println("Physical size: " + initialSize.x + "x" + initialSize.y);
            if (!initialSize.equals(baseSize)) {
                pw.println("Override size: " + baseSize.x + "x" + baseSize.y);
            }
        } catch (android.os.RemoteException e) {
            pw.println("Remote exception: " + e);
        }
    }

    private int runDisplaySize(java.io.PrintWriter pw) throws android.os.RemoteException {
        int div;
        java.lang.String size = getNextArg();
        int displayId = getDisplayId(size);
        if (size == null) {
            printInitialDisplaySize(pw, displayId);
            return 0;
        }
        if ("-d".equals(size)) {
            printInitialDisplaySize(pw, displayId);
            return 0;
        }
        int w = -1;
        if ("reset".equals(size)) {
            div = -1;
        } else {
            int div2 = size.indexOf(120);
            if (div2 <= 0 || div2 >= size.length() - 1) {
                getErrPrintWriter().println("Error: bad size " + size);
                return -1;
            }
            java.lang.String wstr = size.substring(0, div2);
            java.lang.String hstr = size.substring(div2 + 1);
            try {
                int w2 = parseDimension(wstr, displayId);
                int h = parseDimension(hstr, displayId);
                div = h;
                w = w2;
            } catch (java.lang.NumberFormatException e) {
                getErrPrintWriter().println("Error: bad number " + e);
                return -1;
            }
        }
        if (w >= 0 && div >= 0) {
            this.mInterface.setForcedDisplaySize(displayId, w, div);
        } else {
            this.mInterface.clearForcedDisplaySize(displayId);
        }
        return 0;
    }

    private int runSetBlurDisabled(java.io.PrintWriter pw) throws android.os.RemoteException {
        byte b;
        int i;
        java.lang.String arg = getNextArg();
        if (arg == null) {
            pw.println("Blur supported on device: " + android.view.CrossWindowBlurListeners.CROSS_WINDOW_BLUR_SUPPORTED);
            pw.println("Blur enabled: " + this.mInternal.mBlurController.getBlurEnabled());
            return 0;
        }
        switch (arg.hashCode()) {
            case 48:
                b = !arg.equals("0") ? (byte) -1 : (byte) 3;
                break;
            case 49:
                b = !arg.equals("1") ? (byte) -1 : (byte) 1;
                break;
            case 3569038:
                b = !arg.equals("true") ? (byte) -1 : (byte) 0;
                break;
            case 97196323:
                b = !arg.equals("false") ? (byte) -1 : (byte) 2;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
            case 1:
                i = 1;
                break;
            case 2:
            case 3:
                i = 0;
                break;
            default:
                getErrPrintWriter().println("Error: expected true, 1, false, 0, but got " + arg);
                return -1;
        }
        android.provider.Settings.Global.putInt(this.mInternal.mContext.getContentResolver(), "disable_window_blurs", i);
        return 0;
    }

    private void printInitialDisplayDensity(java.io.PrintWriter pw, int displayId) {
        try {
            int initialDensity = this.mInterface.getInitialDisplayDensity(displayId);
            int baseDensity = this.mInterface.getBaseDisplayDensity(displayId);
            pw.println("Physical density: " + initialDensity);
            if (initialDensity != baseDensity) {
                pw.println("Override density: " + baseDensity);
            }
        } catch (android.os.RemoteException e) {
            pw.println("Remote exception: " + e);
        }
    }

    private int runDisplayDensity(java.io.PrintWriter pw) throws android.os.RemoteException {
        int density;
        java.lang.String densityStr = getNextArg();
        java.lang.String option = getNextOption();
        java.lang.String arg = getNextArg();
        int displayId = 0;
        if ("-d".equals(option) && arg != null) {
            try {
                displayId = java.lang.Integer.parseInt(arg);
            } catch (java.lang.NumberFormatException e) {
                getErrPrintWriter().println("Error: bad number " + e);
            }
        } else if ("-u".equals(option) && arg != null && (displayId = this.mInterface.getDisplayIdByUniqueId(arg)) == -1) {
            getErrPrintWriter().println("Error: the uniqueId is invalid ");
            return -1;
        }
        if (densityStr == null) {
            printInitialDisplayDensity(pw, displayId);
            return 0;
        }
        if ("-d".equals(densityStr)) {
            printInitialDisplayDensity(pw, displayId);
            return 0;
        }
        if ("reset".equals(densityStr)) {
            density = -1;
        } else {
            try {
                density = java.lang.Integer.parseInt(densityStr);
                if (density < 72) {
                    getErrPrintWriter().println("Error: density must be >= 72");
                    return -1;
                }
            } catch (java.lang.NumberFormatException e2) {
                getErrPrintWriter().println("Error: bad number " + e2);
                return -1;
            }
        }
        if (density > 0) {
            this.mInterface.setForcedDisplayDensityForUser(displayId, density, -2);
        } else {
            this.mInterface.clearForcedDisplayDensityForUser(displayId, -2);
        }
        return 0;
    }

    private void printFoldedArea(java.io.PrintWriter pw) {
        android.graphics.Rect foldedArea = this.mInternal.getFoldedArea();
        if (foldedArea.isEmpty()) {
            pw.println("Folded area: none");
        } else {
            pw.println("Folded area: " + foldedArea.left + "," + foldedArea.top + "," + foldedArea.right + "," + foldedArea.bottom);
        }
    }

    private int runDisplayFoldedArea(java.io.PrintWriter pw) {
        java.lang.String areaStr = getNextArg();
        android.graphics.Rect rect = new android.graphics.Rect();
        if (areaStr == null) {
            printFoldedArea(pw);
            return 0;
        }
        if ("reset".equals(areaStr)) {
            rect.setEmpty();
        } else {
            java.util.regex.Pattern flattenedPattern = java.util.regex.Pattern.compile("(-?\\d+),(-?\\d+),(-?\\d+),(-?\\d+)");
            java.util.regex.Matcher matcher = flattenedPattern.matcher(areaStr);
            if (!matcher.matches()) {
                getErrPrintWriter().println("Error: area should be LEFT,TOP,RIGHT,BOTTOM");
                return -1;
            }
            rect.set(java.lang.Integer.parseInt(matcher.group(1)), java.lang.Integer.parseInt(matcher.group(2)), java.lang.Integer.parseInt(matcher.group(3)), java.lang.Integer.parseInt(matcher.group(4)));
        }
        this.mInternal.setOverrideFoldedArea(rect);
        return 0;
    }

    private int runDisplayScaling(java.io.PrintWriter pw) throws android.os.RemoteException {
        java.lang.String scalingStr = getNextArgRequired();
        if ("auto".equals(scalingStr)) {
            this.mInterface.setForcedDisplayScalingMode(getDisplayId(scalingStr), 0);
        } else if (kotlinx.coroutines.DebugKt.DEBUG_PROPERTY_VALUE_OFF.equals(scalingStr)) {
            this.mInterface.setForcedDisplayScalingMode(getDisplayId(scalingStr), 1);
        } else {
            getErrPrintWriter().println("Error: scaling must be 'auto' or 'off'");
            return -1;
        }
        return 0;
    }

    private int runSandboxDisplayApis(java.io.PrintWriter pw) throws android.os.RemoteException {
        byte b;
        boolean sandboxDisplayApis;
        int displayId = 0;
        java.lang.String arg = getNextArgRequired();
        if ("-d".equals(arg)) {
            displayId = java.lang.Integer.parseInt(getNextArgRequired());
            arg = getNextArgRequired();
        }
        switch (arg.hashCode()) {
            case 48:
                b = !arg.equals("0") ? (byte) -1 : (byte) 3;
                break;
            case 49:
                b = !arg.equals("1") ? (byte) -1 : (byte) 1;
                break;
            case 3569038:
                b = !arg.equals("true") ? (byte) -1 : (byte) 0;
                break;
            case 97196323:
                b = !arg.equals("false") ? (byte) -1 : (byte) 2;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
            case 1:
                sandboxDisplayApis = true;
                break;
            case 2:
            case 3:
                sandboxDisplayApis = false;
                break;
            default:
                getErrPrintWriter().println("Error: expecting true, 1, false, 0, but we get " + arg);
                return -1;
        }
        this.mInternal.setSandboxDisplayApis(displayId, sandboxDisplayApis);
        return 0;
    }

    private int runDismissKeyguard(java.io.PrintWriter pw) throws android.os.RemoteException {
        this.mInterface.dismissKeyguard((com.android.internal.policy.IKeyguardDismissCallback) null, (java.lang.CharSequence) null);
        return 0;
    }

    private int parseDimension(java.lang.String s, int displayId) throws java.lang.NumberFormatException {
        int density;
        if (s.endsWith("px")) {
            return java.lang.Integer.parseInt(s.substring(0, s.length() - 2));
        }
        if (s.endsWith("dp")) {
            try {
                density = this.mInterface.getBaseDisplayDensity(displayId);
            } catch (android.os.RemoteException e) {
                density = 160;
            }
            return (java.lang.Integer.parseInt(s.substring(0, s.length() - 2)) * density) / 160;
        }
        int density2 = java.lang.Integer.parseInt(s);
        return density2;
    }

    private int runDisplayUserRotation(java.io.PrintWriter pw) {
        int rotation;
        int displayId = 0;
        java.lang.String arg = getNextArg();
        if (arg == null) {
            return printDisplayUserRotation(pw, 0);
        }
        if ("-d".equals(arg)) {
            displayId = java.lang.Integer.parseInt(getNextArgRequired());
            arg = getNextArg();
        }
        java.lang.String lockMode = arg;
        if (lockMode == null) {
            return printDisplayUserRotation(pw, displayId);
        }
        if ("free".equals(lockMode)) {
            this.mInternal.thawDisplayRotation(displayId, "WindowManagerShellCommand#free");
            return 0;
        }
        if (!"lock".equals(lockMode)) {
            getErrPrintWriter().println("Error: argument needs to be either -d, free or lock.");
            return -1;
        }
        java.lang.String arg2 = getNextArg();
        if (arg2 == null) {
            rotation = -1;
        } else {
            try {
                rotation = java.lang.Integer.parseInt(arg2);
            } catch (java.lang.IllegalArgumentException e) {
                getErrPrintWriter().println("Error: " + e.getMessage());
                return -1;
            }
        }
        this.mInternal.freezeDisplayRotation(displayId, rotation, "WindowManagerShellCommand#lock");
        return 0;
    }

    private int printDisplayUserRotation(java.io.PrintWriter pw, int displayId) {
        int displayUserRotation = this.mInternal.getDisplayUserRotation(displayId);
        if (displayUserRotation < 0) {
            getErrPrintWriter().println("Error: check logcat for more details.");
            return -1;
        }
        if (!this.mInternal.isDisplayRotationFrozen(displayId)) {
            pw.println("free");
            return 0;
        }
        pw.print("lock ");
        pw.println(displayUserRotation);
        return 0;
    }

    private int runFixedToUserRotation(java.io.PrintWriter pw) throws android.os.RemoteException {
        byte b;
        int fixedToUserRotation;
        int displayId = 0;
        java.lang.String arg = getNextArg();
        if (arg == null) {
            printFixedToUserRotation(pw, 0);
            return 0;
        }
        if ("-d".equals(arg)) {
            displayId = java.lang.Integer.parseInt(getNextArgRequired());
            arg = getNextArg();
        }
        if (arg == null) {
            return printFixedToUserRotation(pw, displayId);
        }
        switch (arg.hashCode()) {
            case -1609594047:
                b = !arg.equals(com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED) ? (byte) -1 : (byte) 0;
                break;
            case -768077068:
                b = !arg.equals("enabled_if_no_auto_rotation") ? (byte) -1 : (byte) 3;
                break;
            case 270940796:
                b = !arg.equals(com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED) ? (byte) -1 : (byte) 1;
                break;
            case 1544803905:
                b = !arg.equals("default") ? (byte) -1 : (byte) 2;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                fixedToUserRotation = 2;
                break;
            case 1:
                fixedToUserRotation = 1;
                break;
            case 2:
                fixedToUserRotation = 0;
                break;
            case 3:
                fixedToUserRotation = 3;
                break;
            default:
                getErrPrintWriter().println("Error: expecting enabled, disabled or default, but we get " + arg);
                return -1;
        }
        this.mInterface.setFixedToUserRotation(displayId, fixedToUserRotation);
        return 0;
    }

    private int printFixedToUserRotation(java.io.PrintWriter pw, int displayId) {
        int fixedToUserRotationMode = this.mInternal.getFixedToUserRotation(displayId);
        switch (fixedToUserRotationMode) {
            case 0:
                pw.println("default");
                return 0;
            case 1:
                pw.println(com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_DISABLED);
                return 0;
            case 2:
                pw.println(com.android.server.timezonedetector.ServiceConfigAccessor.PROVIDER_MODE_ENABLED);
                return 0;
            case 3:
                pw.println("enabled_if_no_auto_rotation");
                return 0;
            default:
                getErrPrintWriter().println("Error: check logcat for more details.");
                return -1;
        }
    }

    private int runSetIgnoreOrientationRequest(java.io.PrintWriter pw) throws android.os.RemoteException {
        byte b;
        boolean ignoreOrientationRequest;
        int displayId = 0;
        java.lang.String arg = getNextArgRequired();
        if ("-d".equals(arg)) {
            displayId = java.lang.Integer.parseInt(getNextArgRequired());
            arg = getNextArgRequired();
        }
        switch (arg.hashCode()) {
            case 48:
                b = !arg.equals("0") ? (byte) -1 : (byte) 3;
                break;
            case 49:
                b = !arg.equals("1") ? (byte) -1 : (byte) 1;
                break;
            case 3569038:
                b = !arg.equals("true") ? (byte) -1 : (byte) 0;
                break;
            case 97196323:
                b = !arg.equals("false") ? (byte) -1 : (byte) 2;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
            case 1:
                ignoreOrientationRequest = true;
                break;
            case 2:
            case 3:
                ignoreOrientationRequest = false;
                break;
            default:
                getErrPrintWriter().println("Error: expecting true, 1, false, 0, but we get " + arg);
                return -1;
        }
        this.mInterface.setIgnoreOrientationRequest(displayId, ignoreOrientationRequest);
        return 0;
    }

    private int runGetIgnoreOrientationRequest(java.io.PrintWriter pw) throws android.os.RemoteException {
        int displayId = 0;
        java.lang.String arg = getNextArg();
        if ("-d".equals(arg)) {
            displayId = java.lang.Integer.parseInt(getNextArgRequired());
        }
        boolean ignoreOrientationRequest = this.mInternal.getIgnoreOrientationRequest(displayId);
        pw.println("ignoreOrientationRequest " + ignoreOrientationRequest + " for displayId=" + displayId);
        return 0;
    }

    private void dumpLocalWindowAsync(final android.view.IWindow client, final android.os.ParcelFileDescriptor pfd) {
        com.android.server.IoThread.getExecutor().execute(new java.lang.Runnable() { // from class: com.android.server.wm.WindowManagerShellCommand$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$dumpLocalWindowAsync$0(client, pfd);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$dumpLocalWindowAsync$0(android.view.IWindow client, android.os.ParcelFileDescriptor pfd) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                try {
                    client.executeCommand("DUMP_ENCODED", (java.lang.String) null, pfd);
                } catch (java.lang.Exception e) {
                    e.printStackTrace();
                }
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    private int runDumpVisibleWindowViews(java.io.PrintWriter pw) {
        final int recentsComponentUid;
        if (!this.mInternal.checkCallingPermission("android.permission.DUMP", "runDumpVisibleWindowViews()")) {
            throw new java.lang.SecurityException("Requires DUMP permission");
        }
        try {
            java.util.zip.ZipOutputStream out = new java.util.zip.ZipOutputStream(getRawOutputStream());
            try {
                final java.util.ArrayList<android.util.Pair<java.lang.String, com.android.internal.os.ByteTransferPipe>> requestList = new java.util.ArrayList<>();
                com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mGlobalLock;
                com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
                synchronized (windowManagerGlobalLock) {
                    try {
                        com.android.server.wm.RecentTasks recentTasks = this.mInternal.mAtmService.getRecentTasks();
                        if (recentTasks != null) {
                            recentsComponentUid = recentTasks.getRecentsComponentUid();
                        } else {
                            recentsComponentUid = -1;
                        }
                        this.mInternal.mRoot.forAllWindows(new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerShellCommand$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(java.lang.Object obj) {
                                this.f$0.lambda$runDumpVisibleWindowViews$1(recentsComponentUid, requestList, (com.android.server.wm.WindowState) obj);
                            }
                        }, false);
                    } catch (java.lang.Throwable th) {
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        throw th;
                    }
                }
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                for (android.util.Pair<java.lang.String, com.android.internal.os.ByteTransferPipe> entry : requestList) {
                    try {
                        byte[] data = ((com.android.internal.os.ByteTransferPipe) entry.second).get();
                        out.putNextEntry(new java.util.zip.ZipEntry((java.lang.String) entry.first));
                        out.write(data);
                    } catch (java.io.IOException e) {
                    }
                }
                out.close();
            } finally {
            }
        } catch (java.io.IOException e2) {
            pw.println("Error fetching dump " + e2.getMessage());
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runDumpVisibleWindowViews$1(int recentsComponentUid, java.util.ArrayList requestList, com.android.server.wm.WindowState w) {
        boolean isRecents = w.getUid() == recentsComponentUid;
        if (w.isVisible() || isRecents) {
            com.android.internal.os.ByteTransferPipe pipe = null;
            try {
                pipe = new com.android.internal.os.ByteTransferPipe();
                android.os.ParcelFileDescriptor pfd = pipe.getWriteFd();
                if (w.isClientLocal()) {
                    dumpLocalWindowAsync(w.mClient, pfd);
                } else {
                    w.mClient.executeCommand("DUMP_ENCODED", (java.lang.String) null, pfd);
                }
                requestList.add(android.util.Pair.create(w.getName(), pipe));
            } catch (android.os.RemoteException | java.io.IOException e) {
                if (pipe != null) {
                    pipe.kill();
                }
            }
        }
    }

    private int runSetFixedOrientationLetterboxAspectRatio(java.io.PrintWriter pw) throws android.os.RemoteException {
        try {
            java.lang.String arg = getNextArgRequired();
            float aspectRatio = java.lang.Float.parseFloat(arg);
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mLetterboxConfiguration.setFixedOrientationLetterboxAspectRatio(aspectRatio);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: bad aspect ratio format " + e);
            return -1;
        } catch (java.lang.IllegalArgumentException e2) {
            getErrPrintWriter().println("Error: aspect ratio should be provided as an argument " + e2);
            return -1;
        }
    }

    private int runSetDefaultMinAspectRatioForUnresizableApps(java.io.PrintWriter pw) throws android.os.RemoteException {
        try {
            java.lang.String arg = getNextArgRequired();
            float aspectRatio = java.lang.Float.parseFloat(arg);
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mLetterboxConfiguration.setDefaultMinAspectRatioForUnresizableApps(aspectRatio);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: bad aspect ratio format " + e);
            return -1;
        } catch (java.lang.IllegalArgumentException e2) {
            getErrPrintWriter().println("Error: aspect ratio should be provided as an argument " + e2);
            return -1;
        }
    }

    private int runSetLetterboxActivityCornersRadius(java.io.PrintWriter pw) throws android.os.RemoteException {
        try {
            java.lang.String arg = getNextArgRequired();
            int cornersRadius = java.lang.Integer.parseInt(arg);
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mLetterboxConfiguration.setLetterboxActivityCornersRadius(cornersRadius);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: bad corners radius format " + e);
            return -1;
        } catch (java.lang.IllegalArgumentException e2) {
            getErrPrintWriter().println("Error: corners radius should be provided as an argument " + e2);
            return -1;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0055 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:5:0x000d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runSetLetterboxBackgroundType(java.io.PrintWriter r6) throws android.os.RemoteException {
        /*
            r5 = this;
            r0 = -1
            java.lang.String r1 = r5.getNextArgRequired()     // Catch: java.lang.IllegalArgumentException -> L6b
            int r2 = r1.hashCode()     // Catch: java.lang.IllegalArgumentException -> L6b
            r3 = 0
            switch(r2) {
                case -1700528003: goto L2e;
                case -231186968: goto L24;
                case 1216433359: goto L19;
                case 1474694658: goto Le;
                default: goto Ld;
            }     // Catch: java.lang.IllegalArgumentException -> L6b
        Ld:
            goto L38
        Le:
            java.lang.String r2 = "wallpaper"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L6b
            if (r2 == 0) goto Ld
            r2 = 3
            goto L39
        L19:
            java.lang.String r2 = "solid_color"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L6b
            if (r2 == 0) goto Ld
            r2 = r3
            goto L39
        L24:
            java.lang.String r2 = "app_color_background"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L6b
            if (r2 == 0) goto Ld
            r2 = 1
            goto L39
        L2e:
            java.lang.String r2 = "app_color_background_floating"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L6b
            if (r2 == 0) goto Ld
            r2 = 2
            goto L39
        L38:
            r2 = r0
        L39:
            switch(r2) {
                case 0: goto L4a;
                case 1: goto L47;
                case 2: goto L44;
                case 3: goto L41;
                default: goto L3c;
            }     // Catch: java.lang.IllegalArgumentException -> L6b
        L3c:
            java.io.PrintWriter r2 = r5.getErrPrintWriter()     // Catch: java.lang.IllegalArgumentException -> L6b
            goto L65
        L41:
            r0 = 3
            r2 = r0
            goto L4c
        L44:
            r0 = 2
            r2 = r0
            goto L4c
        L47:
            r0 = 1
            r2 = r0
            goto L4c
        L4a:
            r0 = 0
            r2 = r0
        L4c:
            com.android.server.wm.WindowManagerService r0 = r5.mInternal
            com.android.server.wm.WindowManagerGlobalLock r4 = r0.mGlobalLock
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection()
            monitor-enter(r4)
            com.android.server.wm.LetterboxConfiguration r0 = r5.mLetterboxConfiguration     // Catch: java.lang.Throwable -> L5f
            r0.setLetterboxBackgroundTypeOverride(r2)     // Catch: java.lang.Throwable -> L5f
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L5f
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            return r3
        L5f:
            r0 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L5f
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            throw r0
        L65:
            java.lang.String r3 = "Error: 'solid_color', 'app_color_background' or 'wallpaper' should be provided as an argument"
            r2.println(r3)     // Catch: java.lang.IllegalArgumentException -> L6b
            return r0
        L6b:
            r1 = move-exception
            java.io.PrintWriter r2 = r5.getErrPrintWriter()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Error: 'solid_color', 'app_color_background' or 'wallpaper' should be provided as an argument"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r1)
            java.lang.String r3 = r3.toString()
            r2.println(r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowManagerShellCommand.runSetLetterboxBackgroundType(java.io.PrintWriter):int");
    }

    private int runSetLetterboxBackgroundColorResource(java.io.PrintWriter pw) throws android.os.RemoteException {
        try {
            java.lang.String arg = getNextArgRequired();
            int colorId = this.mInternal.mContext.getResources().getIdentifier(arg, "color", "com.android.internal");
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mLetterboxConfiguration.setLetterboxBackgroundColorResourceId(colorId);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return 0;
        } catch (android.content.res.Resources.NotFoundException e) {
            getErrPrintWriter().println("Error: color in '@android:color/resource_name' format should be provided as an argument " + e);
            return -1;
        }
    }

    private int runSetLetterboxBackgroundColor(java.io.PrintWriter pw) throws android.os.RemoteException {
        try {
            java.lang.String arg = getNextArgRequired();
            android.graphics.Color color = android.graphics.Color.valueOf(android.graphics.Color.parseColor(arg));
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mLetterboxConfiguration.setLetterboxBackgroundColor(color);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return 0;
        } catch (java.lang.IllegalArgumentException e) {
            getErrPrintWriter().println("Error: color in #RRGGBB format should be provided as an argument " + e);
            return -1;
        }
    }

    private int runSetLetterboxBackgroundWallpaperBlurRadius(java.io.PrintWriter pw) throws android.os.RemoteException {
        try {
            java.lang.String arg = getNextArgRequired();
            int radiusDp = java.lang.Integer.parseInt(arg);
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    int radiusPx = (int) android.util.TypedValue.applyDimension(1, radiusDp, this.mInternal.mContext.getResources().getDisplayMetrics());
                    this.mLetterboxConfiguration.setLetterboxBackgroundWallpaperBlurRadiusPx(radiusPx);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: blur radius format " + e);
            return -1;
        } catch (java.lang.IllegalArgumentException e2) {
            getErrPrintWriter().println("Error: blur radius should be provided as an argument " + e2);
            return -1;
        }
    }

    private int runSetLetterboxBackgroundWallpaperDarkScrimAlpha(java.io.PrintWriter pw) throws android.os.RemoteException {
        try {
            java.lang.String arg = getNextArgRequired();
            float alpha = java.lang.Float.parseFloat(arg);
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    this.mLetterboxConfiguration.setLetterboxBackgroundWallpaperDarkScrimAlpha(alpha);
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return 0;
        } catch (java.lang.NumberFormatException e) {
            getErrPrintWriter().println("Error: bad alpha format " + e);
            return -1;
        } catch (java.lang.IllegalArgumentException e2) {
            getErrPrintWriter().println("Error: alpha should be provided as an argument " + e2);
            return -1;
        }
    }

    private int runSetLetterboxHorizontalPositionMultiplier(java.io.PrintWriter pw) throws android.os.RemoteException {
        try {
            java.lang.String arg = getNextArgRequired();
            float multiplier = java.lang.Float.parseFloat(arg);
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    try {
                        this.mLetterboxConfiguration.setLetterboxHorizontalPositionMultiplier(multiplier);
                    } catch (java.lang.IllegalArgumentException e) {
                        getErrPrintWriter().println("Error: invalid multiplier value " + e);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return -1;
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return 0;
        } catch (java.lang.NumberFormatException e2) {
            getErrPrintWriter().println("Error: bad multiplier format " + e2);
            return -1;
        } catch (java.lang.IllegalArgumentException e3) {
            getErrPrintWriter().println("Error: multiplier should be provided as an argument " + e3);
            return -1;
        }
    }

    private int runSetLetterboxVerticalPositionMultiplier(java.io.PrintWriter pw) throws android.os.RemoteException {
        try {
            java.lang.String arg = getNextArgRequired();
            float multiplier = java.lang.Float.parseFloat(arg);
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    try {
                        this.mLetterboxConfiguration.setLetterboxVerticalPositionMultiplier(multiplier);
                    } catch (java.lang.IllegalArgumentException e) {
                        getErrPrintWriter().println("Error: invalid multiplier value " + e);
                        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                        return -1;
                    }
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return 0;
        } catch (java.lang.NumberFormatException e2) {
            getErrPrintWriter().println("Error: bad multiplier format " + e2);
            return -1;
        } catch (java.lang.IllegalArgumentException e3) {
            getErrPrintWriter().println("Error: multiplier should be provided as an argument " + e3);
            return -1;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0048 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:5:0x000d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runSetLetterboxDefaultPositionForHorizontalReachability(java.io.PrintWriter r6) throws android.os.RemoteException {
        /*
            r5 = this;
            r0 = -1
            java.lang.String r1 = r5.getNextArgRequired()     // Catch: java.lang.IllegalArgumentException -> L5e
            int r2 = r1.hashCode()     // Catch: java.lang.IllegalArgumentException -> L5e
            r3 = 0
            switch(r2) {
                case -1364013995: goto L24;
                case 3317767: goto L19;
                case 108511772: goto Le;
                default: goto Ld;
            }     // Catch: java.lang.IllegalArgumentException -> L5e
        Ld:
            goto L2e
        Le:
            java.lang.String r2 = "right"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L5e
            if (r2 == 0) goto Ld
            r2 = 2
            goto L2f
        L19:
            java.lang.String r2 = "left"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L5e
            if (r2 == 0) goto Ld
            r2 = r3
            goto L2f
        L24:
            java.lang.String r2 = "center"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L5e
            if (r2 == 0) goto Ld
            r2 = 1
            goto L2f
        L2e:
            r2 = r0
        L2f:
            switch(r2) {
                case 0: goto L3d;
                case 1: goto L3a;
                case 2: goto L37;
                default: goto L32;
            }     // Catch: java.lang.IllegalArgumentException -> L5e
        L32:
            java.io.PrintWriter r2 = r5.getErrPrintWriter()     // Catch: java.lang.IllegalArgumentException -> L5e
            goto L58
        L37:
            r0 = 2
            r2 = r0
            goto L3f
        L3a:
            r0 = 1
            r2 = r0
            goto L3f
        L3d:
            r0 = 0
            r2 = r0
        L3f:
            com.android.server.wm.WindowManagerService r0 = r5.mInternal
            com.android.server.wm.WindowManagerGlobalLock r4 = r0.mGlobalLock
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection()
            monitor-enter(r4)
            com.android.server.wm.LetterboxConfiguration r0 = r5.mLetterboxConfiguration     // Catch: java.lang.Throwable -> L52
            r0.setDefaultPositionForHorizontalReachability(r2)     // Catch: java.lang.Throwable -> L52
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L52
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            return r3
        L52:
            r0 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L52
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            throw r0
        L58:
            java.lang.String r3 = "Error: 'left', 'center' or 'right' are expected as an argument"
            r2.println(r3)     // Catch: java.lang.IllegalArgumentException -> L5e
            return r0
        L5e:
            r1 = move-exception
            java.io.PrintWriter r2 = r5.getErrPrintWriter()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Error: 'left', 'center' or 'right' are expected as an argument"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r1)
            java.lang.String r3 = r3.toString()
            r2.println(r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowManagerShellCommand.runSetLetterboxDefaultPositionForHorizontalReachability(java.io.PrintWriter):int");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0047 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:5:0x000d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runSetLetterboxDefaultPositionForVerticalReachability(java.io.PrintWriter r6) throws android.os.RemoteException {
        /*
            r5 = this;
            r0 = -1
            java.lang.String r1 = r5.getNextArgRequired()     // Catch: java.lang.IllegalArgumentException -> L5d
            int r2 = r1.hashCode()     // Catch: java.lang.IllegalArgumentException -> L5d
            r3 = 0
            switch(r2) {
                case -1383228885: goto L23;
                case -1364013995: goto L19;
                case 115029: goto Le;
                default: goto Ld;
            }     // Catch: java.lang.IllegalArgumentException -> L5d
        Ld:
            goto L2d
        Le:
            java.lang.String r2 = "top"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L5d
            if (r2 == 0) goto Ld
            r2 = r3
            goto L2e
        L19:
            java.lang.String r2 = "center"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L5d
            if (r2 == 0) goto Ld
            r2 = 1
            goto L2e
        L23:
            java.lang.String r2 = "bottom"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L5d
            if (r2 == 0) goto Ld
            r2 = 2
            goto L2e
        L2d:
            r2 = r0
        L2e:
            switch(r2) {
                case 0: goto L3c;
                case 1: goto L39;
                case 2: goto L36;
                default: goto L31;
            }     // Catch: java.lang.IllegalArgumentException -> L5d
        L31:
            java.io.PrintWriter r2 = r5.getErrPrintWriter()     // Catch: java.lang.IllegalArgumentException -> L5d
            goto L57
        L36:
            r0 = 2
            r2 = r0
            goto L3e
        L39:
            r0 = 1
            r2 = r0
            goto L3e
        L3c:
            r0 = 0
            r2 = r0
        L3e:
            com.android.server.wm.WindowManagerService r0 = r5.mInternal
            com.android.server.wm.WindowManagerGlobalLock r4 = r0.mGlobalLock
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection()
            monitor-enter(r4)
            com.android.server.wm.LetterboxConfiguration r0 = r5.mLetterboxConfiguration     // Catch: java.lang.Throwable -> L51
            r0.setDefaultPositionForVerticalReachability(r2)     // Catch: java.lang.Throwable -> L51
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L51
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            return r3
        L51:
            r0 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L51
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            throw r0
        L57:
            java.lang.String r3 = "Error: 'top', 'center' or 'bottom' are expected as an argument"
            r2.println(r3)     // Catch: java.lang.IllegalArgumentException -> L5d
            return r0
        L5d:
            r1 = move-exception
            java.io.PrintWriter r2 = r5.getErrPrintWriter()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Error: 'top', 'center' or 'bottom' are expected as an argument"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r1)
            java.lang.String r3 = r3.toString()
            r2.println(r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowManagerShellCommand.runSetLetterboxDefaultPositionForVerticalReachability(java.io.PrintWriter):int");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0048 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:5:0x000d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runSetPersistentLetterboxPositionForHorizontalReachability(java.io.PrintWriter r6) throws android.os.RemoteException {
        /*
            r5 = this;
            r0 = -1
            java.lang.String r1 = r5.getNextArgRequired()     // Catch: java.lang.IllegalArgumentException -> L5e
            int r2 = r1.hashCode()     // Catch: java.lang.IllegalArgumentException -> L5e
            r3 = 0
            switch(r2) {
                case -1364013995: goto L24;
                case 3317767: goto L19;
                case 108511772: goto Le;
                default: goto Ld;
            }     // Catch: java.lang.IllegalArgumentException -> L5e
        Ld:
            goto L2e
        Le:
            java.lang.String r2 = "right"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L5e
            if (r2 == 0) goto Ld
            r2 = 2
            goto L2f
        L19:
            java.lang.String r2 = "left"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L5e
            if (r2 == 0) goto Ld
            r2 = r3
            goto L2f
        L24:
            java.lang.String r2 = "center"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L5e
            if (r2 == 0) goto Ld
            r2 = 1
            goto L2f
        L2e:
            r2 = r0
        L2f:
            switch(r2) {
                case 0: goto L3d;
                case 1: goto L3a;
                case 2: goto L37;
                default: goto L32;
            }     // Catch: java.lang.IllegalArgumentException -> L5e
        L32:
            java.io.PrintWriter r2 = r5.getErrPrintWriter()     // Catch: java.lang.IllegalArgumentException -> L5e
            goto L58
        L37:
            r0 = 2
            r2 = r0
            goto L3f
        L3a:
            r0 = 1
            r2 = r0
            goto L3f
        L3d:
            r0 = 0
            r2 = r0
        L3f:
            com.android.server.wm.WindowManagerService r0 = r5.mInternal
            com.android.server.wm.WindowManagerGlobalLock r4 = r0.mGlobalLock
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection()
            monitor-enter(r4)
            com.android.server.wm.LetterboxConfiguration r0 = r5.mLetterboxConfiguration     // Catch: java.lang.Throwable -> L52
            r0.setPersistentLetterboxPositionForHorizontalReachability(r3, r2)     // Catch: java.lang.Throwable -> L52
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L52
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            return r3
        L52:
            r0 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L52
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            throw r0
        L58:
            java.lang.String r3 = "Error: 'left', 'center' or 'right' are expected as an argument"
            r2.println(r3)     // Catch: java.lang.IllegalArgumentException -> L5e
            return r0
        L5e:
            r1 = move-exception
            java.io.PrintWriter r2 = r5.getErrPrintWriter()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Error: 'left', 'center' or 'right' are expected as an argument"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r1)
            java.lang.String r3 = r3.toString()
            r2.println(r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowManagerShellCommand.runSetPersistentLetterboxPositionForHorizontalReachability(java.io.PrintWriter):int");
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0047 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:5:0x000d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runSetPersistentLetterboxPositionForVerticalReachability(java.io.PrintWriter r6) throws android.os.RemoteException {
        /*
            r5 = this;
            r0 = -1
            java.lang.String r1 = r5.getNextArgRequired()     // Catch: java.lang.IllegalArgumentException -> L5d
            int r2 = r1.hashCode()     // Catch: java.lang.IllegalArgumentException -> L5d
            r3 = 0
            switch(r2) {
                case -1383228885: goto L23;
                case -1364013995: goto L19;
                case 115029: goto Le;
                default: goto Ld;
            }     // Catch: java.lang.IllegalArgumentException -> L5d
        Ld:
            goto L2d
        Le:
            java.lang.String r2 = "top"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L5d
            if (r2 == 0) goto Ld
            r2 = r3
            goto L2e
        L19:
            java.lang.String r2 = "center"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L5d
            if (r2 == 0) goto Ld
            r2 = 1
            goto L2e
        L23:
            java.lang.String r2 = "bottom"
            boolean r2 = r1.equals(r2)     // Catch: java.lang.IllegalArgumentException -> L5d
            if (r2 == 0) goto Ld
            r2 = 2
            goto L2e
        L2d:
            r2 = r0
        L2e:
            switch(r2) {
                case 0: goto L3c;
                case 1: goto L39;
                case 2: goto L36;
                default: goto L31;
            }     // Catch: java.lang.IllegalArgumentException -> L5d
        L31:
            java.io.PrintWriter r2 = r5.getErrPrintWriter()     // Catch: java.lang.IllegalArgumentException -> L5d
            goto L57
        L36:
            r0 = 2
            r2 = r0
            goto L3e
        L39:
            r0 = 1
            r2 = r0
            goto L3e
        L3c:
            r0 = 0
            r2 = r0
        L3e:
            com.android.server.wm.WindowManagerService r0 = r5.mInternal
            com.android.server.wm.WindowManagerGlobalLock r4 = r0.mGlobalLock
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection()
            monitor-enter(r4)
            com.android.server.wm.LetterboxConfiguration r0 = r5.mLetterboxConfiguration     // Catch: java.lang.Throwable -> L51
            r0.setPersistentLetterboxPositionForVerticalReachability(r3, r2)     // Catch: java.lang.Throwable -> L51
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L51
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            return r3
        L51:
            r0 = move-exception
            monitor-exit(r4)     // Catch: java.lang.Throwable -> L51
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection()
            throw r0
        L57:
            java.lang.String r3 = "Error: 'top', 'center' or 'bottom' are expected as an argument"
            r2.println(r3)     // Catch: java.lang.IllegalArgumentException -> L5d
            return r0
        L5d:
            r1 = move-exception
            java.io.PrintWriter r2 = r5.getErrPrintWriter()
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Error: 'top', 'center' or 'bottom' are expected as an argument"
            java.lang.StringBuilder r3 = r3.append(r4)
            java.lang.StringBuilder r3 = r3.append(r1)
            java.lang.String r3 = r3.toString()
            r2.println(r3)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowManagerShellCommand.runSetPersistentLetterboxPositionForVerticalReachability(java.io.PrintWriter):int");
    }

    private int runSetBooleanFlag(java.io.PrintWriter pw, java.util.function.Consumer<java.lang.Boolean> setter) throws android.os.RemoteException {
        byte b;
        boolean enabled;
        java.lang.String arg = getNextArg();
        if (arg != null) {
            switch (arg.hashCode()) {
                case 48:
                    b = !arg.equals("0") ? (byte) -1 : (byte) 3;
                    break;
                case 49:
                    b = !arg.equals("1") ? (byte) -1 : (byte) 1;
                    break;
                case 3569038:
                    b = !arg.equals("true") ? (byte) -1 : (byte) 0;
                    break;
                case 97196323:
                    b = !arg.equals("false") ? (byte) -1 : (byte) 2;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                case 1:
                    enabled = true;
                    break;
                case 2:
                case 3:
                    enabled = false;
                    break;
                default:
                    getErrPrintWriter().println("Error: expected true, 1, false, 0, but got " + arg);
                    return -1;
            }
            com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mGlobalLock;
            com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
            synchronized (windowManagerGlobalLock) {
                try {
                    setter.accept(java.lang.Boolean.valueOf(enabled));
                } catch (java.lang.Throwable th) {
                    com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                    throw th;
                }
            }
            com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
            return 0;
        }
        getErrPrintWriter().println("Error: expected true, 1, false, 0, but got empty input.");
        return -1;
    }

    private int runSetLetterboxStyle(java.io.PrintWriter pw) throws android.os.RemoteException {
        if (peekNextArg() == null) {
            getErrPrintWriter().println("Error: No arguments provided.");
        }
        while (true) {
            byte b = 0;
            if (peekNextArg() == null) {
                return 0;
            }
            java.lang.String arg = getNextArg();
            switch (arg.hashCode()) {
                case -2007271181:
                    if (!arg.equals("--aspectRatio")) {
                        b = -1;
                    }
                    break;
                case -1688278685:
                    b = !arg.equals("--isEducationEnabled") ? (byte) -1 : (byte) 17;
                    break;
                case -1440939136:
                    b = !arg.equals("--verticalPositionMultiplier") ? (byte) -1 : (byte) 9;
                    break;
                case -1310848756:
                    b = !arg.equals("--defaultPositionForVerticalReachability") ? (byte) -1 : android.hardware.tv.hdmi.cec.CecLogicalAddress.FREE_USE;
                    break;
                case -1294369338:
                    b = !arg.equals("--isDisplayAspectRatioEnabledForFixedOrientationLetterbox") ? (byte) -1 : (byte) 19;
                    break;
                case -1264068297:
                    b = !arg.equals("--isCameraCompatRefreshEnabled") ? (byte) -1 : (byte) 23;
                    break;
                case -1052930822:
                    b = !arg.equals("--defaultPositionForHorizontalReachability") ? (byte) -1 : (byte) 13;
                    break;
                case -1031747914:
                    b = !arg.equals("--persistentPositionForVerticalReachability") ? (byte) -1 : com.android.server.usb.descriptors.UsbDescriptor.DESCRIPTORTYPE_CAPABILITY;
                    break;
                case -1009939225:
                    b = !arg.equals("--cornerRadius") ? (byte) -1 : (byte) 2;
                    break;
                case -951337176:
                    b = !arg.equals("--backgroundType") ? (byte) -1 : (byte) 3;
                    break;
                case -911250737:
                    b = !arg.equals("--isSplitScreenAspectRatioForUnresizableAppsEnabled") ? (byte) -1 : (byte) 18;
                    break;
                case -335739429:
                    b = !arg.equals("--wallpaperBlurRadius") ? (byte) -1 : (byte) 6;
                    break;
                case -302214401:
                    b = !arg.equals("--isUserAppAspectRatioFullscreenEnabled") ? (byte) -1 : (byte) 22;
                    break;
                case -301215364:
                    b = !arg.equals("--isHorizontalReachabilityEnabled") ? (byte) -1 : (byte) 10;
                    break;
                case -69722518:
                    b = !arg.equals("--isVerticalReachabilityEnabled") ? (byte) -1 : (byte) 11;
                    break;
                case 229853520:
                    b = !arg.equals("--wallpaperDarkScrimAlpha") ? (byte) -1 : (byte) 7;
                    break;
                case 304986101:
                    b = !arg.equals("--isTranslucentLetterboxingEnabled") ? (byte) -1 : (byte) 20;
                    break;
                case 557317429:
                    b = !arg.equals("--backgroundColor") ? (byte) -1 : (byte) 4;
                    break;
                case 875005988:
                    b = !arg.equals("--persistentPositionForHorizontalReachability") ? (byte) -1 : (byte) 15;
                    break;
                case 935353942:
                    b = !arg.equals("--isCameraCompatRefreshCycleThroughStopEnabled") ? (byte) -1 : (byte) 24;
                    break;
                case 1033642083:
                    b = !arg.equals("--backgroundColorResource") ? (byte) -1 : (byte) 5;
                    break;
                case 1066804362:
                    b = !arg.equals("--minAspectRatioForUnresizable") ? (byte) -1 : (byte) 1;
                    break;
                case 1070248110:
                    b = !arg.equals("--horizontalPositionMultiplier") ? (byte) -1 : (byte) 8;
                    break;
                case 1416509399:
                    b = !arg.equals("--isUserAppAspectRatioSettingsEnabled") ? (byte) -1 : (byte) 21;
                    break;
                case 1739415288:
                    b = !arg.equals("--isAutomaticReachabilityInBookModeEnabled") ? (byte) -1 : (byte) 12;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    runSetFixedOrientationLetterboxAspectRatio(pw);
                    break;
                case 1:
                    runSetDefaultMinAspectRatioForUnresizableApps(pw);
                    break;
                case 2:
                    runSetLetterboxActivityCornersRadius(pw);
                    break;
                case 3:
                    runSetLetterboxBackgroundType(pw);
                    break;
                case 4:
                    runSetLetterboxBackgroundColor(pw);
                    break;
                case 5:
                    runSetLetterboxBackgroundColorResource(pw);
                    break;
                case 6:
                    runSetLetterboxBackgroundWallpaperBlurRadius(pw);
                    break;
                case 7:
                    runSetLetterboxBackgroundWallpaperDarkScrimAlpha(pw);
                    break;
                case 8:
                    runSetLetterboxHorizontalPositionMultiplier(pw);
                    break;
                case 9:
                    runSetLetterboxVerticalPositionMultiplier(pw);
                    break;
                case 10:
                    final com.android.server.wm.LetterboxConfiguration letterboxConfiguration = this.mLetterboxConfiguration;
                    java.util.Objects.requireNonNull(letterboxConfiguration);
                    runSetBooleanFlag(pw, new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerShellCommand$$ExternalSyntheticLambda2
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            letterboxConfiguration.setIsHorizontalReachabilityEnabled(((java.lang.Boolean) obj).booleanValue());
                        }
                    });
                    break;
                case 11:
                    final com.android.server.wm.LetterboxConfiguration letterboxConfiguration2 = this.mLetterboxConfiguration;
                    java.util.Objects.requireNonNull(letterboxConfiguration2);
                    runSetBooleanFlag(pw, new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerShellCommand$$ExternalSyntheticLambda4
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            letterboxConfiguration2.setIsVerticalReachabilityEnabled(((java.lang.Boolean) obj).booleanValue());
                        }
                    });
                    break;
                case 12:
                    final com.android.server.wm.LetterboxConfiguration letterboxConfiguration3 = this.mLetterboxConfiguration;
                    java.util.Objects.requireNonNull(letterboxConfiguration3);
                    runSetBooleanFlag(pw, new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerShellCommand$$ExternalSyntheticLambda5
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            letterboxConfiguration3.setIsAutomaticReachabilityInBookModeEnabled(((java.lang.Boolean) obj).booleanValue());
                        }
                    });
                    break;
                case 13:
                    runSetLetterboxDefaultPositionForHorizontalReachability(pw);
                    break;
                case 14:
                    runSetLetterboxDefaultPositionForVerticalReachability(pw);
                    break;
                case 15:
                    runSetPersistentLetterboxPositionForHorizontalReachability(pw);
                    break;
                case 16:
                    runSetPersistentLetterboxPositionForVerticalReachability(pw);
                    break;
                case 17:
                    final com.android.server.wm.LetterboxConfiguration letterboxConfiguration4 = this.mLetterboxConfiguration;
                    java.util.Objects.requireNonNull(letterboxConfiguration4);
                    runSetBooleanFlag(pw, new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerShellCommand$$ExternalSyntheticLambda6
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            letterboxConfiguration4.setIsEducationEnabled(((java.lang.Boolean) obj).booleanValue());
                        }
                    });
                    break;
                case 18:
                    final com.android.server.wm.LetterboxConfiguration letterboxConfiguration5 = this.mLetterboxConfiguration;
                    java.util.Objects.requireNonNull(letterboxConfiguration5);
                    runSetBooleanFlag(pw, new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerShellCommand$$ExternalSyntheticLambda7
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            letterboxConfiguration5.setIsSplitScreenAspectRatioForUnresizableAppsEnabled(((java.lang.Boolean) obj).booleanValue());
                        }
                    });
                    break;
                case 19:
                    final com.android.server.wm.LetterboxConfiguration letterboxConfiguration6 = this.mLetterboxConfiguration;
                    java.util.Objects.requireNonNull(letterboxConfiguration6);
                    runSetBooleanFlag(pw, new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerShellCommand$$ExternalSyntheticLambda8
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            letterboxConfiguration6.setIsDisplayAspectRatioEnabledForFixedOrientationLetterbox(((java.lang.Boolean) obj).booleanValue());
                        }
                    });
                    break;
                case 20:
                    final com.android.server.wm.LetterboxConfiguration letterboxConfiguration7 = this.mLetterboxConfiguration;
                    java.util.Objects.requireNonNull(letterboxConfiguration7);
                    runSetBooleanFlag(pw, new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerShellCommand$$ExternalSyntheticLambda9
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            letterboxConfiguration7.setTranslucentLetterboxingOverrideEnabled(((java.lang.Boolean) obj).booleanValue());
                        }
                    });
                    break;
                case 21:
                    final com.android.server.wm.LetterboxConfiguration letterboxConfiguration8 = this.mLetterboxConfiguration;
                    java.util.Objects.requireNonNull(letterboxConfiguration8);
                    runSetBooleanFlag(pw, new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerShellCommand$$ExternalSyntheticLambda10
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            letterboxConfiguration8.setUserAppAspectRatioSettingsOverrideEnabled(((java.lang.Boolean) obj).booleanValue());
                        }
                    });
                    break;
                case 22:
                    final com.android.server.wm.LetterboxConfiguration letterboxConfiguration9 = this.mLetterboxConfiguration;
                    java.util.Objects.requireNonNull(letterboxConfiguration9);
                    runSetBooleanFlag(pw, new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerShellCommand$$ExternalSyntheticLambda11
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            letterboxConfiguration9.setUserAppAspectRatioFullscreenOverrideEnabled(((java.lang.Boolean) obj).booleanValue());
                        }
                    });
                    break;
                case 23:
                    final com.android.server.wm.LetterboxConfiguration letterboxConfiguration10 = this.mLetterboxConfiguration;
                    java.util.Objects.requireNonNull(letterboxConfiguration10);
                    runSetBooleanFlag(pw, new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerShellCommand$$ExternalSyntheticLambda12
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            letterboxConfiguration10.setCameraCompatRefreshEnabled(((java.lang.Boolean) obj).booleanValue());
                        }
                    });
                    break;
                case 24:
                    final com.android.server.wm.LetterboxConfiguration letterboxConfiguration11 = this.mLetterboxConfiguration;
                    java.util.Objects.requireNonNull(letterboxConfiguration11);
                    runSetBooleanFlag(pw, new java.util.function.Consumer() { // from class: com.android.server.wm.WindowManagerShellCommand$$ExternalSyntheticLambda3
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            letterboxConfiguration11.setCameraCompatRefreshCycleThroughStopEnabled(((java.lang.Boolean) obj).booleanValue());
                        }
                    });
                    break;
                default:
                    getErrPrintWriter().println("Error: Unrecognized letterbox style option: " + arg);
                    return -1;
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0024  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runResetLetterboxStyle(java.io.PrintWriter r7) throws android.os.RemoteException {
        /*
            Method dump skipped, instruction units count: 642
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowManagerShellCommand.runResetLetterboxStyle(java.io.PrintWriter):int");
    }

    private int runSetMultiWindowConfig() {
        if (peekNextArg() == null) {
            getErrPrintWriter().println("Error: No arguments provided.");
        }
        int result = 0;
        while (true) {
            byte b = 0;
            if (peekNextArg() == null) {
                return result == 0 ? 0 : -1;
            }
            java.lang.String arg = getNextArg();
            switch (arg.hashCode()) {
                case 1485032610:
                    if (!arg.equals("--supportsNonResizable")) {
                        b = -1;
                    }
                    break;
                case 1714039607:
                    b = !arg.equals("--respectsActivityMinWidthHeight") ? (byte) -1 : (byte) 1;
                    break;
                default:
                    b = -1;
                    break;
            }
            switch (b) {
                case 0:
                    result += runSetSupportsNonResizableMultiWindow();
                    break;
                case 1:
                    result += runSetRespectsActivityMinWidthHeightMultiWindow();
                    break;
                default:
                    getErrPrintWriter().println("Error: Unrecognized multi window option: " + arg);
                    return -1;
            }
        }
    }

    private int runSetSupportsNonResizableMultiWindow() {
        java.lang.String arg = getNextArg();
        if (!arg.equals("-1") && !arg.equals("0") && !arg.equals("1")) {
            getErrPrintWriter().println("Error: a config value of [-1, 0, 1] must be provided as an argument for supportsNonResizableMultiWindow");
            return -1;
        }
        int configValue = java.lang.Integer.parseInt(arg);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mAtmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mInternal.mAtmService.mSupportsNonResizableMultiWindow = configValue;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return 0;
    }

    private int runSetRespectsActivityMinWidthHeightMultiWindow() {
        java.lang.String arg = getNextArg();
        if (!arg.equals("-1") && !arg.equals("0") && !arg.equals("1")) {
            getErrPrintWriter().println("Error: a config value of [-1, 0, 1] must be provided as an argument for respectsActivityMinWidthHeightMultiWindow");
            return -1;
        }
        int configValue = java.lang.Integer.parseInt(arg);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mAtmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mInternal.mAtmService.mRespectsActivityMinWidthHeightMultiWindow = configValue;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return 0;
    }

    private int runGetMultiWindowConfig(java.io.PrintWriter pw) {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mAtmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                pw.println("Supports non-resizable in multi window: " + this.mInternal.mAtmService.mSupportsNonResizableMultiWindow);
                pw.println("Respects activity min width/height in multi window: " + this.mInternal.mAtmService.mRespectsActivityMinWidthHeightMultiWindow);
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return 0;
    }

    private int runResetMultiWindowConfig() {
        int supportsNonResizable = this.mInternal.mContext.getResources().getInteger(android.R.integer.config_screen_rotation_fade_in_delay);
        int respectsActivityMinWidthHeight = this.mInternal.mContext.getResources().getInteger(android.R.integer.config_pdp_reject_retry_delay_ms);
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mAtmService.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mInternal.mAtmService.mSupportsNonResizableMultiWindow = supportsNonResizable;
                this.mInternal.mAtmService.mRespectsActivityMinWidthHeightMultiWindow = respectsActivityMinWidthHeight;
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return 0;
    }

    private void resetLetterboxStyle() {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                this.mLetterboxConfiguration.resetFixedOrientationLetterboxAspectRatio();
                this.mLetterboxConfiguration.resetDefaultMinAspectRatioForUnresizableApps();
                this.mLetterboxConfiguration.resetLetterboxActivityCornersRadius();
                this.mLetterboxConfiguration.resetLetterboxBackgroundType();
                this.mLetterboxConfiguration.resetLetterboxBackgroundColor();
                this.mLetterboxConfiguration.resetLetterboxBackgroundWallpaperBlurRadiusPx();
                this.mLetterboxConfiguration.resetLetterboxBackgroundWallpaperDarkScrimAlpha();
                this.mLetterboxConfiguration.resetLetterboxHorizontalPositionMultiplier();
                this.mLetterboxConfiguration.resetLetterboxVerticalPositionMultiplier();
                this.mLetterboxConfiguration.resetIsHorizontalReachabilityEnabled();
                this.mLetterboxConfiguration.resetIsVerticalReachabilityEnabled();
                this.mLetterboxConfiguration.resetEnabledAutomaticReachabilityInBookMode();
                this.mLetterboxConfiguration.resetDefaultPositionForHorizontalReachability();
                this.mLetterboxConfiguration.resetDefaultPositionForVerticalReachability();
                this.mLetterboxConfiguration.resetPersistentLetterboxPositionForHorizontalReachability();
                this.mLetterboxConfiguration.resetPersistentLetterboxPositionForVerticalReachability();
                this.mLetterboxConfiguration.resetIsEducationEnabled();
                this.mLetterboxConfiguration.resetIsSplitScreenAspectRatioForUnresizableAppsEnabled();
                this.mLetterboxConfiguration.resetIsDisplayAspectRatioEnabledForFixedOrientationLetterbox();
                this.mLetterboxConfiguration.resetTranslucentLetterboxingEnabled();
                this.mLetterboxConfiguration.resetUserAppAspectRatioSettingsEnabled();
                this.mLetterboxConfiguration.resetUserAppAspectRatioFullscreenEnabled();
                this.mLetterboxConfiguration.resetCameraCompatRefreshEnabled();
                this.mLetterboxConfiguration.resetCameraCompatRefreshCycleThroughStopEnabled();
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
    }

    private int runGetLetterboxStyle(java.io.PrintWriter pw) throws android.os.RemoteException {
        com.android.server.wm.WindowManagerGlobalLock windowManagerGlobalLock = this.mInternal.mGlobalLock;
        com.android.server.wm.WindowManagerService.boostPriorityForLockedSection();
        synchronized (windowManagerGlobalLock) {
            try {
                pw.println("Corner radius: " + this.mLetterboxConfiguration.getLetterboxActivityCornersRadius());
                pw.println("Horizontal position multiplier: " + this.mLetterboxConfiguration.getLetterboxHorizontalPositionMultiplier(false));
                pw.println("Vertical position multiplier: " + this.mLetterboxConfiguration.getLetterboxVerticalPositionMultiplier(false));
                pw.println("Horizontal position multiplier (book mode): " + this.mLetterboxConfiguration.getLetterboxHorizontalPositionMultiplier(true));
                pw.println("Vertical position multiplier (tabletop mode): " + this.mLetterboxConfiguration.getLetterboxVerticalPositionMultiplier(true));
                pw.println("Horizontal position multiplier for reachability: " + this.mLetterboxConfiguration.getHorizontalMultiplierForReachability(false));
                pw.println("Vertical position multiplier for reachability: " + this.mLetterboxConfiguration.getVerticalMultiplierForReachability(false));
                pw.println("Aspect ratio: " + this.mLetterboxConfiguration.getFixedOrientationLetterboxAspectRatio());
                pw.println("Default min aspect ratio for unresizable apps: " + this.mLetterboxConfiguration.getDefaultMinAspectRatioForUnresizableApps());
                pw.println("Is horizontal reachability enabled: " + this.mLetterboxConfiguration.getIsHorizontalReachabilityEnabled());
                pw.println("Is vertical reachability enabled: " + this.mLetterboxConfiguration.getIsVerticalReachabilityEnabled());
                pw.println("Is automatic reachability in book mode enabled: " + this.mLetterboxConfiguration.getIsAutomaticReachabilityInBookModeEnabled());
                pw.println("Default position for horizontal reachability: " + com.android.server.wm.LetterboxConfiguration.letterboxHorizontalReachabilityPositionToString(this.mLetterboxConfiguration.getDefaultPositionForHorizontalReachability()));
                pw.println("Default position for vertical reachability: " + com.android.server.wm.LetterboxConfiguration.letterboxVerticalReachabilityPositionToString(this.mLetterboxConfiguration.getDefaultPositionForVerticalReachability()));
                pw.println("Current position for horizontal reachability:" + com.android.server.wm.LetterboxConfiguration.letterboxHorizontalReachabilityPositionToString(this.mLetterboxConfiguration.getLetterboxPositionForHorizontalReachability(false)));
                pw.println("Current position for vertical reachability:" + com.android.server.wm.LetterboxConfiguration.letterboxVerticalReachabilityPositionToString(this.mLetterboxConfiguration.getLetterboxPositionForVerticalReachability(false)));
                pw.println("Is education enabled: " + this.mLetterboxConfiguration.getIsEducationEnabled());
                pw.println("Is using split screen aspect ratio as aspect ratio for unresizable apps: " + this.mLetterboxConfiguration.getIsSplitScreenAspectRatioForUnresizableAppsEnabled());
                pw.println("Is using display aspect ratio as aspect ratio for all letterboxed apps: " + this.mLetterboxConfiguration.getIsDisplayAspectRatioEnabledForFixedOrientationLetterbox());
                pw.println("    Is activity \"refresh\" in camera compatibility treatment enabled: " + this.mLetterboxConfiguration.isCameraCompatRefreshEnabled());
                pw.println("    Refresh using \"stopped -> resumed\" cycle: " + this.mLetterboxConfiguration.isCameraCompatRefreshCycleThroughStopEnabled());
                pw.println("Background type: " + com.android.server.wm.LetterboxConfiguration.letterboxBackgroundTypeToString(this.mLetterboxConfiguration.getLetterboxBackgroundType()));
                pw.println("    Background color: " + java.lang.Integer.toHexString(this.mLetterboxConfiguration.getLetterboxBackgroundColor().toArgb()));
                pw.println("    Wallpaper blur radius: " + this.mLetterboxConfiguration.getLetterboxBackgroundWallpaperBlurRadiusPx());
                pw.println("    Wallpaper dark scrim alpha: " + this.mLetterboxConfiguration.getLetterboxBackgroundWallpaperDarkScrimAlpha());
                pw.println("Is letterboxing for translucent activities enabled: " + this.mLetterboxConfiguration.isTranslucentLetterboxingEnabled());
                pw.println("Is the user aspect ratio settings enabled: " + this.mLetterboxConfiguration.isUserAppAspectRatioSettingsEnabled());
                pw.println("Is the fullscreen option in user aspect ratio settings enabled: " + this.mLetterboxConfiguration.isUserAppAspectRatioFullscreenEnabled());
            } catch (java.lang.Throwable th) {
                com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.wm.WindowManagerService.resetPriorityAfterLockedSection();
        return 0;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:11:0x0021  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int runWmShellCommand(java.io.PrintWriter r3) {
        /*
            r2 = this;
            java.lang.String r0 = r2.getNextArg()
            int r1 = r0.hashCode()
            switch(r1) {
                case -1067396926: goto L16;
                case 3198785: goto Lc;
                default: goto Lb;
            }
        Lb:
            goto L21
        Lc:
            java.lang.String r1 = "help"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Lb
            r1 = 1
            goto L22
        L16:
            java.lang.String r1 = "tracing"
            boolean r1 = r0.equals(r1)
            if (r1 == 0) goto Lb
            r1 = 0
            goto L22
        L21:
            r1 = -1
        L22:
            switch(r1) {
                case 0: goto L2a;
                default: goto L25;
            }
        L25:
            int r1 = r2.runHelp(r3)
            return r1
        L2a:
            int r1 = r2.runWmShellTracing(r3)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.WindowManagerShellCommand.runWmShellCommand(java.io.PrintWriter):int");
    }

    private int runHelp(java.io.PrintWriter pw) {
        pw.println("Window Manager Shell commands:");
        pw.println("  help");
        pw.println("    Print this help text.");
        pw.println("  tracing <start/stop>");
        pw.println("    Start/stop shell transition tracing.");
        return 0;
    }

    private int runWmShellTracing(java.io.PrintWriter pw) {
        byte b;
        java.lang.String arg = getNextArg();
        switch (arg.hashCode()) {
            case -390772652:
                b = !arg.equals("save-for-bugreport") ? (byte) -1 : (byte) 2;
                break;
            case 3540994:
                b = !arg.equals("stop") ? (byte) -1 : (byte) 1;
                break;
            case 109757538:
                b = !arg.equals("start") ? (byte) -1 : (byte) 0;
                break;
            default:
                b = -1;
                break;
        }
        switch (b) {
            case 0:
                this.mInternal.mTransitionTracer.startTrace(pw);
                return 0;
            case 1:
                this.mInternal.mTransitionTracer.stopTrace(pw);
                return 0;
            case 2:
                this.mInternal.mTransitionTracer.saveForBugreport(pw);
                return 0;
            default:
                getErrPrintWriter().println("Error: expected 'start' or 'stop', but got '" + arg + "'");
                return -1;
        }
    }

    private int runReset(java.io.PrintWriter pw) throws android.os.RemoteException {
        int displayId = getDisplayId(getNextArg());
        this.mInterface.clearForcedDisplaySize(displayId);
        this.mInterface.clearForcedDisplayDensityForUser(displayId, -2);
        this.mInternal.setOverrideFoldedArea(new android.graphics.Rect());
        this.mInterface.setForcedDisplayScalingMode(displayId, 0);
        this.mInternal.thawDisplayRotation(displayId, "WindowManagerShellCommand#runReset");
        this.mInterface.setFixedToUserRotation(displayId, 0);
        this.mInterface.setIgnoreOrientationRequest(displayId, false);
        resetLetterboxStyle();
        this.mInternal.setSandboxDisplayApis(displayId, true);
        runResetMultiWindowConfig();
        pw.println("Reset all settings for displayId=" + displayId);
        return 0;
    }

    public void onHelp() {
        java.io.PrintWriter pw = getOutPrintWriter();
        pw.println("Window manager (window) commands:");
        pw.println("  help");
        pw.println("      Print this help text.");
        pw.println("  size [reset|WxH|WdpxHdp] [-d DISPLAY_ID]");
        pw.println("    Return or override display size.");
        pw.println("    width and height in pixels unless suffixed with 'dp'.");
        pw.println("  density [reset|DENSITY] [-d DISPLAY_ID] [-u UNIQUE_ID]");
        pw.println("    Return or override display density.");
        pw.println("  folded-area [reset|LEFT,TOP,RIGHT,BOTTOM]");
        pw.println("    Return or override folded area.");
        pw.println("  scaling [off|auto] [-d DISPLAY_ID]");
        pw.println("    Set display scaling mode.");
        pw.println("  dismiss-keyguard");
        pw.println("    Dismiss the keyguard, prompting user for auth if necessary.");
        pw.println("  disable-blur [true|1|false|0]");
        pw.println("  user-rotation [-d DISPLAY_ID] [free|lock] [rotation]");
        pw.println("    Print or set user rotation mode and user rotation.");
        pw.println("  dump-visible-window-views");
        pw.println("    Dumps the encoded view hierarchies of visible windows");
        pw.println("  fixed-to-user-rotation [-d DISPLAY_ID] [enabled|disabled|default");
        pw.println("      |enabled_if_no_auto_rotation]");
        pw.println("    Print or set rotating display for app requested orientation.");
        pw.println("  set-ignore-orientation-request [-d DISPLAY_ID] [true|1|false|0]");
        pw.println("  get-ignore-orientation-request [-d DISPLAY_ID] ");
        pw.println("    If app requested orientation should be ignored.");
        pw.println("  set-sandbox-display-apis [true|1|false|0]");
        pw.println("    Sets override of Display APIs getRealSize / getRealMetrics to reflect ");
        pw.println("    DisplayArea of the activity, or the window bounds if in letterbox or");
        pw.println("    Size Compat Mode.");
        printLetterboxHelp(pw);
        printMultiWindowConfigHelp(pw);
        pw.println("  reset [-d DISPLAY_ID]");
        pw.println("    Reset all override settings.");
        if (!android.os.Build.IS_USER) {
            pw.println("  tracing (start | stop)");
            pw.println("    Start or stop window tracing.");
            pw.println("  logging (start | stop | enable | disable | enable-text | disable-text)");
            pw.println("    Logging settings.");
        }
    }

    private void printLetterboxHelp(java.io.PrintWriter pw) {
        pw.println("  set-letterbox-style");
        pw.println("    Sets letterbox style using the following options:");
        pw.println("      --aspectRatio aspectRatio");
        pw.println("        Aspect ratio of letterbox for fixed orientation. If aspectRatio <= 1.0");
        pw.println("        both it and R.dimen.config_fixedOrientationLetterboxAspectRatio will");
        pw.println("        be ignored and framework implementation will determine aspect ratio.");
        pw.println("      --minAspectRatioForUnresizable aspectRatio");
        pw.println("        Default min aspect ratio for unresizable apps which is used when an");
        pw.println("        app is eligible for the size compat mode.  If aspectRatio <= 1.0");
        pw.println("        both it and R.dimen.config_fixedOrientationLetterboxAspectRatio will");
        pw.println("        be ignored and framework implementation will determine aspect ratio.");
        pw.println("      --cornerRadius radius");
        pw.println("        Corners radius (in pixels) for activities in the letterbox mode.");
        pw.println("        If radius < 0, both R.integer.config_letterboxActivityCornersRadius");
        pw.println("        and it will be ignored and corners of the activity won't be rounded.");
        pw.println("      --backgroundType [reset|solid_color|app_color_background");
        pw.println("          |app_color_background_floating|wallpaper]");
        pw.println("        Type of background used in the letterbox mode.");
        pw.println("      --backgroundColor color");
        pw.println("        Color of letterbox which is be used when letterbox background type");
        pw.println("        is 'solid-color'. Use (set)get-letterbox-style to check and control");
        pw.println("        letterbox background type. See Color#parseColor for allowed color");
        pw.println("        formats (#RRGGBB and some colors by name, e.g. magenta or olive).");
        pw.println("      --backgroundColorResource resource_name");
        pw.println("        Color resource name of letterbox background which is used when");
        pw.println("        background type is 'solid-color'. Use (set)get-letterbox-style to");
        pw.println("        check and control background type. Parameter is a color resource");
        pw.println("        name, for example, @android:color/system_accent2_50.");
        pw.println("      --wallpaperBlurRadius radius");
        pw.println("        Blur radius for 'wallpaper' letterbox background. If radius <= 0");
        pw.println("        both it and R.dimen.config_letterboxBackgroundWallpaperBlurRadius");
        pw.println("        are ignored and 0 is used.");
        pw.println("      --wallpaperDarkScrimAlpha alpha");
        pw.println("        Alpha of a black translucent scrim shown over 'wallpaper'");
        pw.println("        letterbox background. If alpha < 0 or >= 1 both it and");
        pw.println("        R.dimen.config_letterboxBackgroundWallaperDarkScrimAlpha are ignored");
        pw.println("        and 0.0 (transparent) is used instead.");
        pw.println("      --horizontalPositionMultiplier multiplier");
        pw.println("        Horizontal position of app window center. If multiplier < 0 or > 1,");
        pw.println("        both it and R.dimen.config_letterboxHorizontalPositionMultiplier");
        pw.println("        are ignored and central position (0.5) is used.");
        pw.println("      --verticalPositionMultiplier multiplier");
        pw.println("        Vertical position of app window center. If multiplier < 0 or > 1,");
        pw.println("        both it and R.dimen.config_letterboxVerticalPositionMultiplier");
        pw.println("        are ignored and central position (0.5) is used.");
        pw.println("      --isHorizontalReachabilityEnabled [true|1|false|0]");
        pw.println("        Whether horizontal reachability repositioning is allowed for ");
        pw.println("        letterboxed fullscreen apps in landscape device orientation.");
        pw.println("      --isVerticalReachabilityEnabled [true|1|false|0]");
        pw.println("        Whether vertical reachability repositioning is allowed for ");
        pw.println("        letterboxed fullscreen apps in portrait device orientation.");
        pw.println("      --defaultPositionForHorizontalReachability [left|center|right]");
        pw.println("        Default position of app window when horizontal reachability is.");
        pw.println("        enabled.");
        pw.println("      --defaultPositionForVerticalReachability [top|center|bottom]");
        pw.println("        Default position of app window when vertical reachability is.");
        pw.println("        enabled.");
        pw.println("      --persistentPositionForHorizontalReachability [left|center|right]");
        pw.println("        Persistent position of app window when horizontal reachability is.");
        pw.println("        enabled.");
        pw.println("      --persistentPositionForVerticalReachability [top|center|bottom]");
        pw.println("        Persistent position of app window when vertical reachability is.");
        pw.println("        enabled.");
        pw.println("      --isEducationEnabled [true|1|false|0]");
        pw.println("        Whether education is allowed for letterboxed fullscreen apps.");
        pw.println("      --isSplitScreenAspectRatioForUnresizableAppsEnabled [true|1|false|0]");
        pw.println("        Whether using split screen aspect ratio as a default aspect ratio for");
        pw.println("        unresizable apps.");
        pw.println("      --isTranslucentLetterboxingEnabled [true|1|false|0]");
        pw.println("        Whether letterboxing for translucent activities is enabled.");
        pw.println("      --isUserAppAspectRatioSettingsEnabled [true|1|false|0]");
        pw.println("        Whether user aspect ratio settings are enabled.");
        pw.println("      --isUserAppAspectRatioFullscreenEnabled [true|1|false|0]");
        pw.println("        Whether user aspect ratio fullscreen option is enabled.");
        pw.println("      --isCameraCompatRefreshEnabled [true|1|false|0]");
        pw.println("        Whether camera compatibility refresh is enabled.");
        pw.println("      --isCameraCompatRefreshCycleThroughStopEnabled [true|1|false|0]");
        pw.println("        Whether activity \"refresh\" in camera compatibility treatment should");
        pw.println("        happen using the \"stopped -> resumed\" cycle rather than");
        pw.println("        \"paused -> resumed\" cycle.");
        pw.println("  reset-letterbox-style [aspectRatio|cornerRadius|backgroundType");
        pw.println("      |backgroundColor|wallpaperBlurRadius|wallpaperDarkScrimAlpha");
        pw.println("      |horizontalPositionMultiplier|verticalPositionMultiplier");
        pw.println("      |isHorizontalReachabilityEnabled|isVerticalReachabilityEnabled");
        pw.println("      |isEducationEnabled|defaultPositionMultiplierForHorizontalReachability");
        pw.println("      |isTranslucentLetterboxingEnabled|isUserAppAspectRatioSettingsEnabled");
        pw.println("      |persistentPositionMultiplierForHorizontalReachability");
        pw.println("      |persistentPositionMultiplierForVerticalReachability");
        pw.println("      |defaultPositionMultiplierForVerticalReachability]");
        pw.println("    Resets overrides to default values for specified properties separated");
        pw.println("    by space, e.g. 'reset-letterbox-style aspectRatio cornerRadius'.");
        pw.println("    If no arguments provided, all values will be reset.");
        pw.println("  get-letterbox-style");
        pw.println("    Prints letterbox style configuration.");
    }

    private void printMultiWindowConfigHelp(java.io.PrintWriter pw) {
        pw.println("  set-multi-window-config");
        pw.println("    Sets options to determine if activity should be shown in multi window:");
        pw.println("      --supportsNonResizable [configValue]");
        pw.println("        Whether the device supports non-resizable activity in multi window.");
        pw.println("        -1: The device doesn't support non-resizable in multi window.");
        pw.println("         0: The device supports non-resizable in multi window only if");
        pw.println("            this is a large screen device.");
        pw.println("         1: The device always supports non-resizable in multi window.");
        pw.println("      --respectsActivityMinWidthHeight [configValue]");
        pw.println("        Whether the device checks the activity min width/height to determine ");
        pw.println("        if it can be shown in multi window.");
        pw.println("        -1: The device ignores the activity min width/height when determining");
        pw.println("            if it can be shown in multi window.");
        pw.println("         0: If this is a small screen, the device compares the activity min");
        pw.println("            width/height with the min multi window modes dimensions");
        pw.println("            the device supports to determine if the activity can be shown in");
        pw.println("            multi window.");
        pw.println("         1: The device always compare the activity min width/height with the");
        pw.println("            min multi window dimensions the device supports to determine if");
        pw.println("            the activity can be shown in multi window.");
        pw.println("  get-multi-window-config");
        pw.println("    Prints values of the multi window config options.");
        pw.println("  reset-multi-window-config");
        pw.println("    Resets overrides to default values of the multi window config options.");
    }
}
