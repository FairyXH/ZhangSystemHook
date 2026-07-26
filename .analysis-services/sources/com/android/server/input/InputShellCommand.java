package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
public class InputShellCommand extends android.os.ShellCommand {
    private static final int DEFAULT_BUTTON_STATE = 0;
    private static final int DEFAULT_DEVICE_ID = 0;
    private static final int DEFAULT_EDGE_FLAGS = 0;
    private static final int DEFAULT_FLAGS = 0;
    private static final int DEFAULT_META_STATE = 0;
    private static final float DEFAULT_PRECISION_X = 1.0f;
    private static final float DEFAULT_PRECISION_Y = 1.0f;
    private static final float DEFAULT_PRESSURE = 1.0f;
    private static final float DEFAULT_SIZE = 1.0f;
    private static final boolean INJECT_ASYNC = true;
    private static final boolean INJECT_SYNC = false;
    private static final java.lang.String INVALID_ARGUMENTS = "Error: Invalid arguments for command: ";
    private static final java.lang.String INVALID_DISPLAY_ARGUMENTS = "Error: Invalid arguments for display ID.";
    private static final java.util.Map<java.lang.Integer, java.lang.Integer> MODIFIER;
    private static final float NO_PRESSURE = 0.0f;
    private static final java.util.Map<java.lang.String, java.lang.Integer> SOURCES;
    private final java.util.function.BiConsumer<android.view.InputEvent, java.lang.Integer> mInputEventInjector;

    static {
        java.util.Map<java.lang.Integer, java.lang.Integer> map = new android.util.ArrayMap<>();
        map.put(113, 12288);
        map.put(114, 20480);
        map.put(57, 18);
        map.put(58, 34);
        map.put(59, 65);
        map.put(60, 129);
        map.put(117, 196608);
        map.put(118, 327680);
        MODIFIER = java.util.Collections.unmodifiableMap(map);
        java.util.Map<java.lang.String, java.lang.Integer> map2 = new android.util.ArrayMap<>();
        map2.put("keyboard", 257);
        map2.put("dpad", 513);
        map2.put("gamepad", 1025);
        map2.put("touchscreen", 4098);
        map2.put("mouse", 8194);
        map2.put("stylus", 16386);
        map2.put("trackball", 65540);
        map2.put("touchpad", 1048584);
        map2.put("touchnavigation", 2097152);
        map2.put("joystick", 16777232);
        map2.put("rotaryencoder", 4194304);
        SOURCES = java.util.Collections.unmodifiableMap(map2);
    }

    public InputShellCommand() {
        this(new java.util.function.BiConsumer() { // from class: com.android.server.input.InputShellCommand$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                com.android.server.input.InputShellCommand.injectInputEvent((android.view.InputEvent) obj, (java.lang.Integer) obj2);
            }
        });
    }

    InputShellCommand(java.util.function.BiConsumer<android.view.InputEvent, java.lang.Integer> inputEventInjector) {
        this.mInputEventInjector = inputEventInjector;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void injectInputEvent(android.view.InputEvent event, java.lang.Integer injectMode) {
        android.hardware.input.InputManagerGlobal.getInstance().injectInputEvent(event, injectMode.intValue());
    }

    private void injectKeyEvent(android.view.KeyEvent event, boolean async) {
        int injectMode;
        if (async) {
            injectMode = 0;
        } else {
            injectMode = 2;
        }
        this.mInputEventInjector.accept(event, java.lang.Integer.valueOf(injectMode));
    }

    private int getInputDeviceId(int inputSource) {
        int[] devIds = android.view.InputDevice.getDeviceIds();
        for (int devId : devIds) {
            android.view.InputDevice inputDev = android.view.InputDevice.getDevice(devId);
            if (inputDev.supportsSource(inputSource)) {
                return devId;
            }
        }
        return 0;
    }

    private int getDisplayId() {
        java.lang.String displayArg = getNextArgRequired();
        if ("INVALID_DISPLAY".equalsIgnoreCase(displayArg)) {
            return -1;
        }
        if ("DEFAULT_DISPLAY".equalsIgnoreCase(displayArg)) {
            return 0;
        }
        try {
            int displayId = java.lang.Integer.parseInt(displayArg);
            if (displayId == -1) {
                return -1;
            }
            return java.lang.Math.max(displayId, 0);
        } catch (java.lang.NumberFormatException e) {
            throw new java.lang.IllegalArgumentException(INVALID_DISPLAY_ARGUMENTS);
        }
    }

    private void injectMotionEvent(int inputSource, int action, long downTime, long when, float x, float y, float pressure, int displayId) {
        java.util.Map<java.lang.Integer, java.lang.Float> axisValues = java.util.Map.of(0, java.lang.Float.valueOf(x), 1, java.lang.Float.valueOf(y), 2, java.lang.Float.valueOf(pressure));
        injectMotionEvent(inputSource, action, downTime, when, axisValues, displayId);
    }

    private void injectMotionEvent(int inputSource, int action, long downTime, long when, java.util.Map<java.lang.Integer, java.lang.Float> axisValues, int displayId) {
        int displayId2;
        android.view.MotionEvent.PointerProperties[] pointerProperties = new android.view.MotionEvent.PointerProperties[1];
        for (int i = 0; i < 1; i++) {
            pointerProperties[i] = new android.view.MotionEvent.PointerProperties();
            pointerProperties[i].id = i;
            pointerProperties[i].toolType = getToolType(inputSource);
        }
        android.view.MotionEvent.PointerCoords[] pointerCoords = new android.view.MotionEvent.PointerCoords[1];
        for (int i2 = 0; i2 < 1; i2++) {
            pointerCoords[i2] = new android.view.MotionEvent.PointerCoords();
            pointerCoords[i2].size = 1.0f;
            for (java.util.Map.Entry<java.lang.Integer, java.lang.Float> entry : axisValues.entrySet()) {
                pointerCoords[i2].setAxisValue(entry.getKey().intValue(), entry.getValue().floatValue());
            }
        }
        if (displayId == -1 && (inputSource & 2) != 0) {
            displayId2 = 0;
        } else {
            displayId2 = displayId;
        }
        android.view.MotionEvent event = android.view.MotionEvent.obtain(downTime, when, action, 1, pointerProperties, pointerCoords, 0, 0, 1.0f, 1.0f, getInputDeviceId(inputSource), 0, inputSource, displayId2, 0);
        this.mInputEventInjector.accept(event, 2);
    }

    private float lerp(float a, float b, float alpha) {
        return ((b - a) * alpha) + a;
    }

    private int getSource(int inputSource, int defaultSource) {
        return inputSource == 0 ? defaultSource : inputSource;
    }

    private int getToolType(int inputSource) {
        switch (inputSource) {
            case 4098:
            case 1048584:
            case 2097152:
                return 1;
            case 8194:
            case 65540:
            case 131076:
                return 3;
            case 16386:
            case 49154:
                return 2;
            default:
                return 0;
        }
    }

    public final int onCommand(java.lang.String cmd) {
        java.lang.String arg = cmd;
        int inputSource = 0;
        if (SOURCES.containsKey(arg)) {
            inputSource = SOURCES.get(arg).intValue();
            arg = getNextArgRequired();
        }
        int displayId = -1;
        if ("-d".equals(arg)) {
            displayId = getDisplayId();
            arg = getNextArgRequired();
        }
        try {
            if ("text".equals(arg)) {
                runText(inputSource, displayId);
                return 0;
            }
            if ("keyevent".equals(arg)) {
                runKeyEvent(inputSource, displayId);
                return 0;
            }
            if ("tap".equals(arg)) {
                runTap(inputSource, displayId);
                return 0;
            }
            if ("swipe".equals(arg)) {
                runSwipe(inputSource, displayId);
                return 0;
            }
            if ("draganddrop".equals(arg)) {
                runDragAndDrop(inputSource, displayId);
                return 0;
            }
            if ("press".equals(arg)) {
                runPress(inputSource, displayId);
                return 0;
            }
            if ("roll".equals(arg)) {
                runRoll(inputSource, displayId);
                return 0;
            }
            if ("scroll".equals(arg)) {
                runScroll(inputSource, displayId);
                return 0;
            }
            if ("motionevent".equals(arg)) {
                runMotionEvent(inputSource, displayId);
                return 0;
            }
            if ("keycombination".equals(arg)) {
                runKeyCombination(inputSource, displayId);
                return 0;
            }
            handleDefaultCommands(arg);
            return 0;
        } catch (java.lang.NumberFormatException e) {
            throw new java.lang.IllegalArgumentException(INVALID_ARGUMENTS + arg);
        }
    }

    public final void onHelp() {
        java.io.PrintWriter out = getOutPrintWriter();
        try {
            out.println("Usage: input [<source>] [-d DISPLAY_ID] <command> [<arg>...]");
            out.println();
            out.println("The sources are: ");
            for (java.lang.String src : SOURCES.keySet()) {
                out.println("      " + src);
            }
            out.println("[axis_value] represents an option specifying the value of a given axis ");
            out.println("      The syntax is as follows: --axis <axis_name>,<axis_value>");
            out.println("            where <axis_name> is the name of the axis as defined in ");
            out.println("            MotionEvent without the AXIS_ prefix (e.g. SCROLL, X)");
            out.println("      Sample [axis_values] entry: `--axis Y,3`, `--axis SCROLL,-2`");
            out.println();
            out.printf("-d: specify the display ID.\n      (Default: %d for key event, %d for motion event if not specified.)", -1, 0);
            out.println();
            out.println("The commands and default sources are:");
            out.println("      text <string> (Default: keyboard)");
            out.println("      keyevent [--longpress|--duration <duration to hold key down in ms>] [--doubletap] [--async] [--delay <duration between keycodes in ms>] <key code number or name> ... (Default: keyboard)");
            out.println("      tap <x> <y> (Default: touchscreen)");
            out.println("      swipe <x1> <y1> <x2> <y2> [duration(ms)] (Default: touchscreen)");
            out.println("      draganddrop <x1> <y1> <x2> <y2> [duration(ms)] (Default: touchscreen)");
            out.println("      press (Default: trackball)");
            out.println("      roll <dx> <dy> (Default: trackball)");
            out.println("      motionevent <DOWN|UP|MOVE|CANCEL> <x> <y> (Default: touchscreen)");
            out.println("      scroll (Default: rotaryencoder). Has the following syntax:");
            out.println("            scroll <x> <y> [axis_value] (for pointer-based sources)");
            out.println("            scroll [axis_value] (for non-pointer-based sources)");
            out.println("            Axis options: SCROLL, HSCROLL, VSCROLL");
            out.println("            None or one or multiple axis value options can be specified.");
            out.println("            To specify multiple axes, use one axis option for per axis.");
            out.println("            Example: `scroll --axis VSCROLL,2 --axis SCROLL,-2.4`");
            out.println("      keycombination [-t duration(ms)] <key code 1> <key code 2> ... (Default: keyboard, the key order is important here.)");
            if (out != null) {
                out.close();
            }
        } catch (java.lang.Throwable th) {
            if (out != null) {
                try {
                    out.close();
                } catch (java.lang.Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    private void runText(int inputSource, int displayId) {
        sendText(getSource(inputSource, 257), getNextArgRequired(), displayId);
    }

    private void sendText(int source, java.lang.String text, int displayId) {
        java.lang.StringBuilder buff = new java.lang.StringBuilder(text);
        boolean escapeFlag = false;
        int i = 0;
        while (i < buff.length()) {
            if (escapeFlag) {
                escapeFlag = false;
                if (buff.charAt(i) == 's') {
                    buff.setCharAt(i, ' ');
                    i--;
                    buff.deleteCharAt(i);
                }
            }
            if (buff.charAt(i) == '%') {
                escapeFlag = true;
            }
            i++;
        }
        char[] chars = buff.toString().toCharArray();
        android.view.KeyCharacterMap kcm = android.view.KeyCharacterMap.load(-1);
        android.view.KeyEvent[] events = kcm.getEvents(chars);
        for (android.view.KeyEvent e : events) {
            if (source != e.getSource()) {
                e.setSource(source);
            }
            e.setDisplayId(displayId);
            injectKeyEvent(e, false);
        }
    }

    private void runKeyEvent(int inputSource, int displayId) {
        boolean longPress;
        boolean async;
        boolean doubleTap;
        long delayMs;
        long durationMs;
        boolean longPress2 = false;
        boolean async2 = false;
        boolean doubleTap2 = false;
        long delayMs2 = 0;
        long durationMs2 = 0;
        java.lang.String arg = getNextArgRequired();
        while (true) {
            if (!arg.startsWith("--")) {
                longPress = longPress2;
                async = async2;
                doubleTap = doubleTap2;
                delayMs = delayMs2;
                break;
            }
            longPress2 = longPress2 || arg.equals("--longpress");
            async2 = async2 || arg.equals("--async");
            doubleTap2 = doubleTap2 || arg.equals("--doubletap");
            if (arg.equals("--delay")) {
                delayMs2 = java.lang.Long.parseLong(getNextArgRequired());
            } else if (arg.equals("--duration")) {
                durationMs2 = java.lang.Long.parseLong(getNextArgRequired());
            }
            java.lang.String nextArg = getNextArg();
            arg = nextArg;
            if (nextArg == null) {
                longPress = longPress2;
                async = async2;
                doubleTap = doubleTap2;
                delayMs = delayMs2;
                break;
            }
        }
        if (durationMs2 > 0 && longPress) {
            getErrPrintWriter().println("--duration and --longpress cannot be used at the same time.");
            throw new java.lang.IllegalArgumentException("keyevent args should only contain either durationMs or longPress");
        }
        if (!longPress) {
            durationMs = durationMs2;
        } else {
            long durationMs3 = android.view.ViewConfiguration.getLongPressTimeout();
            durationMs = durationMs3;
        }
        boolean firstInput = true;
        while (true) {
            if (!firstInput && delayMs > 0) {
                sleep(delayMs);
            }
            int keyCode = android.view.KeyEvent.keyCodeFromString(arg);
            sendKeyEvent(inputSource, keyCode, durationMs, displayId, async);
            if (doubleTap) {
                sleep(android.view.ViewConfiguration.getDoubleTapMinTime());
                sendKeyEvent(inputSource, keyCode, durationMs, displayId, async);
            }
            java.lang.String nextArg2 = getNextArg();
            arg = nextArg2;
            if (nextArg2 != null) {
                firstInput = false;
            } else {
                return;
            }
        }
    }

    private void sendKeyEvent(int inputSource, int keyCode, long durationMs, int displayId, boolean async) {
        long now = android.os.SystemClock.uptimeMillis();
        android.view.KeyEvent event = new android.view.KeyEvent(now, now, 0, keyCode, 0, 0, -1, 0, 0, inputSource);
        event.setDisplayId(displayId);
        injectKeyEvent(event, async);
        long firstSleepDurationMs = java.lang.Math.min(durationMs, android.view.ViewConfiguration.getLongPressTimeout());
        if (firstSleepDurationMs > 0) {
            sleep(firstSleepDurationMs);
            if (durationMs >= android.view.ViewConfiguration.getLongPressTimeout()) {
                long nextEventTime = now + ((long) android.view.ViewConfiguration.getLongPressTimeout());
                android.view.KeyEvent longPressEvent = android.view.KeyEvent.changeTimeRepeat(event, nextEventTime, 1, 128);
                injectKeyEvent(longPressEvent, async);
                long secondSleepDurationMs = durationMs - firstSleepDurationMs;
                if (secondSleepDurationMs > 0) {
                    sleep(secondSleepDurationMs);
                }
            }
        }
        injectKeyEvent(android.view.KeyEvent.changeAction(event, 1), async);
    }

    private void runTap(int inputSource, int displayId) {
        sendTap(getSource(inputSource, 4098), java.lang.Float.parseFloat(getNextArgRequired()), java.lang.Float.parseFloat(getNextArgRequired()), displayId);
    }

    private void sendTap(int inputSource, float x, float y, int displayId) {
        long now = android.os.SystemClock.uptimeMillis();
        injectMotionEvent(inputSource, 0, now, now, x, y, 1.0f, displayId);
        injectMotionEvent(inputSource, 1, now, now, x, y, 0.0f, displayId);
    }

    private void runPress(int inputSource, int displayId) {
        sendTap(getSource(inputSource, 65540), 0.0f, 0.0f, displayId);
    }

    private void runSwipe(int inputSource, int displayId) {
        sendSwipe(getSource(inputSource, 4098), displayId, false);
    }

    private void sendSwipe(int inputSource, int displayId, boolean isDragDrop) {
        int duration;
        float x1 = java.lang.Float.parseFloat(getNextArgRequired());
        float y1 = java.lang.Float.parseFloat(getNextArgRequired());
        float x2 = java.lang.Float.parseFloat(getNextArgRequired());
        float y2 = java.lang.Float.parseFloat(getNextArgRequired());
        java.lang.String durationArg = getNextArg();
        int duration2 = durationArg != null ? java.lang.Integer.parseInt(durationArg) : -1;
        if (duration2 >= 0) {
            duration = duration2;
        } else {
            duration = 300;
        }
        long down = android.os.SystemClock.uptimeMillis();
        float y12 = y1;
        int duration3 = duration;
        injectMotionEvent(inputSource, 0, down, down, x1, y1, 1.0f, displayId);
        if (isDragDrop) {
            sleep(android.view.ViewConfiguration.getLongPressTimeout());
        }
        long now = android.os.SystemClock.uptimeMillis();
        long endTime = down + ((long) duration3);
        long now2 = now;
        while (now2 < endTime) {
            long elapsedTime = now2 - down;
            float alpha = elapsedTime / duration3;
            float y13 = y12;
            injectMotionEvent(inputSource, 2, down, now2, lerp(x1, x2, alpha), lerp(y13, y2, alpha), 1.0f, displayId);
            now2 = android.os.SystemClock.uptimeMillis();
            y12 = y13;
        }
        injectMotionEvent(inputSource, 1, down, now2, x2, y2, 0.0f, displayId);
    }

    private void runDragAndDrop(int inputSource, int displayId) {
        sendSwipe(getSource(inputSource, 4098), displayId, true);
    }

    private void runRoll(int inputSource, int displayId) {
        sendMove(getSource(inputSource, 65540), java.lang.Float.parseFloat(getNextArgRequired()), java.lang.Float.parseFloat(getNextArgRequired()), displayId);
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x008f A[LOOP:0: B:10:0x0059->B:22:0x008f, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0076 A[SYNTHETIC] */
    /*  JADX ERROR: UnsupportedOperationException in pass: RegionMakerVisitor
        java.lang.UnsupportedOperationException
        	at java.base/java.util.Collections$UnmodifiableCollection.add(Unknown Source)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker$1.leaveRegion(SwitchRegionMaker.java:390)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:70)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:23)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.insertBreaksForCase(SwitchRegionMaker.java:370)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.insertBreaks(SwitchRegionMaker.java:85)
        	at jadx.core.dex.visitors.regions.PostProcessRegions.leaveRegion(PostProcessRegions.java:33)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:70)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at java.base/java.util.Collections$UnmodifiableCollection.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.lambda$traverseInternal$0(DepthRegionTraversal.java:68)
        	at java.base/java.util.ArrayList.forEach(Unknown Source)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverseInternal(DepthRegionTraversal.java:68)
        	at jadx.core.dex.visitors.regions.DepthRegionTraversal.traverse(DepthRegionTraversal.java:19)
        	at jadx.core.dex.visitors.regions.PostProcessRegions.process(PostProcessRegions.java:23)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:31)
        */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void runScroll(int r18, int r19) {
        /*
            r17 = this;
            r9 = r17
            r0 = 4194304(0x400000, float:5.877472E-39)
            r1 = r18
            int r10 = r9.getSource(r1, r0)
            r0 = r10 & 2
            r1 = 1
            r2 = 0
            r3 = 2
            if (r0 != r3) goto L13
            r0 = r1
            goto L14
        L13:
            r0 = r2
        L14:
            r11 = r0
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            r12 = r0
            if (r11 == 0) goto L43
            java.lang.Integer r0 = java.lang.Integer.valueOf(r2)
            java.lang.String r3 = r17.getNextArgRequired()
            float r3 = java.lang.Float.parseFloat(r3)
            java.lang.Float r3 = java.lang.Float.valueOf(r3)
            r12.put(r0, r3)
            java.lang.Integer r0 = java.lang.Integer.valueOf(r1)
            java.lang.String r1 = r17.getNextArgRequired()
            float r1 = java.lang.Float.parseFloat(r1)
            java.lang.Float r1 = java.lang.Float.valueOf(r1)
            r12.put(r0, r1)
        L43:
            r0 = 10
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
            r1 = 9
            java.lang.Integer r1 = java.lang.Integer.valueOf(r1)
            r3 = 26
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            java.util.Set r13 = java.util.Set.of(r0, r1, r3)
        L59:
            java.lang.String r0 = r17.getNextOption()
            r14 = r0
            if (r0 == 0) goto La0
            int r0 = r14.hashCode()
            switch(r0) {
                case 1332878657: goto L68;
                default: goto L67;
            }
        L67:
            goto L72
        L68:
            java.lang.String r0 = "--axis"
            boolean r0 = r14.equals(r0)
            if (r0 == 0) goto L67
            r0 = r2
            goto L73
        L72:
            r0 = -1
        L73:
            switch(r0) {
                case 0: goto L8f;
                default: goto L76;
            }
        L76:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unsupported option: "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r14)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L8f:
            android.util.Pair r0 = r9.readAxisOptionValues(r13)
            java.lang.Object r1 = r0.first
            java.lang.Integer r1 = (java.lang.Integer) r1
            java.lang.Object r3 = r0.second
            java.lang.Float r3 = (java.lang.Float) r3
            r12.put(r1, r3)
            goto L59
        La0:
            long r15 = android.os.SystemClock.uptimeMillis()
            r2 = 8
            r0 = r17
            r1 = r10
            r3 = r15
            r5 = r15
            r7 = r12
            r8 = r19
            r0.injectMotionEvent(r1, r2, r3, r5, r7, r8)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.input.InputShellCommand.runScroll(int, int):void");
    }

    private android.util.Pair<java.lang.Integer, java.lang.Float> readAxisOptionValues(java.util.Set<java.lang.Integer> supportedAxes) {
        java.lang.String optionValue = getNextArgRequired();
        java.lang.String[] axisAndValue = optionValue.split(",");
        if (axisAndValue.length != 2) {
            throw new java.lang.IllegalArgumentException("Invalid --axis option value: " + optionValue);
        }
        java.lang.String axisName = "AXIS_" + axisAndValue[0];
        int axis = android.view.MotionEvent.axisFromString(axisName);
        if (axis == -1) {
            throw new java.lang.IllegalArgumentException("Invalid axis name: " + axisName);
        }
        if (!supportedAxes.contains(java.lang.Integer.valueOf(axis))) {
            throw new java.lang.IllegalArgumentException("Unsupported axis: " + axisName);
        }
        return android.util.Pair.create(java.lang.Integer.valueOf(axis), java.lang.Float.valueOf(java.lang.Float.parseFloat(axisAndValue[1])));
    }

    private void sendMove(int inputSource, float dx, float dy, int displayId) {
        long now = android.os.SystemClock.uptimeMillis();
        injectMotionEvent(inputSource, 2, now, now, dx, dy, 0.0f, displayId);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:17:0x003c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int getAction() {
        /*
            r7 = this;
            java.lang.String r0 = r7.getNextArgRequired()
            java.lang.String r1 = r0.toUpperCase()
            int r2 = r1.hashCode()
            r3 = 3
            r4 = 2
            r5 = 1
            r6 = 0
            switch(r2) {
                case 2715: goto L32;
                case 2104482: goto L28;
                case 2372561: goto L1e;
                case 1980572282: goto L14;
                default: goto L13;
            }
        L13:
            goto L3c
        L14:
            java.lang.String r2 = "CANCEL"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L13
            r1 = r3
            goto L3d
        L1e:
            java.lang.String r2 = "MOVE"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L13
            r1 = r4
            goto L3d
        L28:
            java.lang.String r2 = "DOWN"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L13
            r1 = r6
            goto L3d
        L32:
            java.lang.String r2 = "UP"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L13
            r1 = r5
            goto L3d
        L3c:
            r1 = -1
        L3d:
            switch(r1) {
                case 0: goto L5c;
                case 1: goto L5b;
                case 2: goto L5a;
                case 3: goto L59;
                default: goto L40;
            }
        L40:
            java.lang.IllegalArgumentException r1 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "Unknown action: "
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.StringBuilder r2 = r2.append(r0)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r1
        L59:
            return r3
        L5a:
            return r4
        L5b:
            return r5
        L5c:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.input.InputShellCommand.getAction():int");
    }

    private void runMotionEvent(int inputSource, int displayId) {
        float x;
        float y;
        int inputSource2 = getSource(inputSource, 4098);
        int action = getAction();
        if (action == 0 || action == 2 || action == 1) {
            float x2 = java.lang.Float.parseFloat(getNextArgRequired());
            float y2 = java.lang.Float.parseFloat(getNextArgRequired());
            x = x2;
            y = y2;
        } else {
            java.lang.String xString = getNextArg();
            java.lang.String yString = getNextArg();
            if (xString != null && yString != null) {
                float x3 = java.lang.Float.parseFloat(xString);
                float y3 = java.lang.Float.parseFloat(yString);
                x = x3;
                y = y3;
            } else {
                x = 0.0f;
                y = 0.0f;
            }
        }
        sendMotionEvent(inputSource2, action, x, y, displayId);
    }

    private void sendMotionEvent(int inputSource, int action, float x, float y, int displayId) {
        float pressure = (action == 0 || action == 2) ? 1.0f : 0.0f;
        long now = android.os.SystemClock.uptimeMillis();
        injectMotionEvent(inputSource, action, now, now, x, y, pressure, displayId);
    }

    private void runKeyCombination(int inputSource, int displayId) {
        long duration;
        java.lang.String arg = getNextArgRequired();
        if (!"-t".equals(arg)) {
            duration = 0;
        } else {
            long duration2 = java.lang.Integer.parseInt(getNextArgRequired());
            arg = getNextArgRequired();
            duration = duration2;
        }
        android.util.IntArray keyCodes = new android.util.IntArray();
        while (arg != null) {
            int keyCode = android.view.KeyEvent.keyCodeFromString(arg);
            if (keyCode == 0) {
                throw new java.lang.IllegalArgumentException("Unknown keycode: " + arg);
            }
            keyCodes.add(keyCode);
            arg = getNextArg();
        }
        if (keyCodes.size() < 2) {
            throw new java.lang.IllegalArgumentException("keycombination requires at least 2 keycodes");
        }
        sendKeyCombination(inputSource, keyCodes, displayId, duration);
    }

    private void sendKeyCombination(int inputSource, android.util.IntArray keyCodes, int displayId, long duration) {
        long now = android.os.SystemClock.uptimeMillis();
        int count = keyCodes.size();
        android.view.KeyEvent[] events = new android.view.KeyEvent[count];
        int metaState = 0;
        int i = 0;
        while (i < count) {
            int keyCode = keyCodes.get(i);
            int i2 = i;
            android.view.KeyEvent event = new android.view.KeyEvent(now, now, 0, keyCode, 0, metaState, -1, 0, 0, inputSource);
            event.setDisplayId(displayId);
            events = events;
            events[i2] = event;
            metaState |= MODIFIER.getOrDefault(java.lang.Integer.valueOf(keyCode), 0).intValue();
            i = i2 + 1;
            count = count;
        }
        for (android.view.KeyEvent event2 : events) {
            injectKeyEvent(event2, true);
        }
        sleep(duration);
        int i3 = 0;
        for (int length = events.length; i3 < length; length = length) {
            android.view.KeyEvent event3 = events[i3];
            int keyCode2 = event3.getKeyCode();
            android.view.KeyEvent upEvent = new android.view.KeyEvent(now, now, 1, keyCode2, 0, metaState, -1, 0, 0, inputSource);
            injectKeyEvent(upEvent, true);
            metaState &= ~MODIFIER.getOrDefault(java.lang.Integer.valueOf(keyCode2), 0).intValue();
            i3++;
            events = events;
        }
    }

    private void sleep(long milliseconds) {
        try {
            java.lang.Thread.sleep(milliseconds);
        } catch (java.lang.InterruptedException e) {
            throw new java.lang.RuntimeException(e);
        }
    }
}
