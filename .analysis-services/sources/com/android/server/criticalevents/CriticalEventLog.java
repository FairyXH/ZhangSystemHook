package com.android.server.criticalevents;

/* JADX INFO: loaded from: classes.dex */
public class CriticalEventLog {
    private static final int AID_SYSTEM = 1000;
    static final java.lang.String FILENAME = "critical_event_log.pb";
    private static final java.lang.String TAG = com.android.server.criticalevents.CriticalEventLog.class.getSimpleName();
    private static com.android.server.criticalevents.CriticalEventLog sInstance;
    private final com.android.server.criticalevents.CriticalEventLog.ThreadSafeRingBuffer<com.android.server.criticalevents.nano.CriticalEventProto> mEvents;
    private final android.os.Handler mHandler;
    private long mLastSaveAttemptMs;
    private final boolean mLoadAndSaveImmediately;
    private final java.io.File mLogFile;
    private final long mMinTimeBetweenSavesMs;
    private final java.lang.Runnable mSaveRunnable;
    private final int mWindowMs;

    protected interface ILogLoader {
        void load(java.io.File file, com.android.server.criticalevents.CriticalEventLog.ThreadSafeRingBuffer<com.android.server.criticalevents.nano.CriticalEventProto> threadSafeRingBuffer);
    }

    CriticalEventLog(java.lang.String logDir, int capacity, int windowMs, long minTimeBetweenSavesMs, boolean loadAndSaveImmediately, final com.android.server.criticalevents.CriticalEventLog.ILogLoader logLoader) {
        this.mLastSaveAttemptMs = 0L;
        this.mSaveRunnable = new java.lang.Runnable() { // from class: com.android.server.criticalevents.CriticalEventLog$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.saveLogToFileNow();
            }
        };
        this.mLogFile = java.nio.file.Paths.get(logDir, FILENAME).toFile();
        this.mWindowMs = windowMs;
        this.mMinTimeBetweenSavesMs = minTimeBetweenSavesMs;
        this.mLoadAndSaveImmediately = loadAndSaveImmediately;
        this.mEvents = new com.android.server.criticalevents.CriticalEventLog.ThreadSafeRingBuffer<>(com.android.server.criticalevents.nano.CriticalEventProto.class, capacity);
        android.os.HandlerThread thread = new android.os.HandlerThread("CriticalEventLogIO");
        thread.start();
        this.mHandler = new android.os.Handler(thread.getLooper());
        java.lang.Runnable loadEvents = new java.lang.Runnable() { // from class: com.android.server.criticalevents.CriticalEventLog$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$new$0(logLoader);
            }
        };
        if (!this.mLoadAndSaveImmediately) {
            this.mHandler.post(loadEvents);
        } else {
            loadEvents.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(com.android.server.criticalevents.CriticalEventLog.ILogLoader logLoader) {
        logLoader.load(this.mLogFile, this.mEvents);
    }

    private CriticalEventLog() {
        this("/data/misc/critical-events", 20, (int) java.time.Duration.ofMinutes(5L).toMillis(), java.time.Duration.ofSeconds(2L).toMillis(), false, new com.android.server.criticalevents.CriticalEventLog.LogLoader());
    }

    public static com.android.server.criticalevents.CriticalEventLog getInstance() {
        if (sInstance == null) {
            sInstance = new com.android.server.criticalevents.CriticalEventLog();
        }
        return sInstance;
    }

    public static void init() {
        getInstance();
    }

    protected long getWallTimeMillis() {
        return java.lang.System.currentTimeMillis();
    }

    public void logExcessiveBinderCalls(int uid) {
        com.android.server.criticalevents.nano.CriticalEventProto event = new com.android.server.criticalevents.nano.CriticalEventProto();
        com.android.server.criticalevents.nano.CriticalEventProto.ExcessiveBinderCalls calls = new com.android.server.criticalevents.nano.CriticalEventProto.ExcessiveBinderCalls();
        calls.uid = uid;
        event.setExcessiveBinderCalls(calls);
        log(event);
    }

    public void logInstallPackagesStarted() {
        com.android.server.criticalevents.nano.CriticalEventProto event = new com.android.server.criticalevents.nano.CriticalEventProto();
        event.setInstallPackages(new com.android.server.criticalevents.nano.CriticalEventProto.InstallPackages());
        log(event);
    }

    public void logSystemServerStarted() {
        com.android.server.criticalevents.nano.CriticalEventProto event = new com.android.server.criticalevents.nano.CriticalEventProto();
        event.setSystemServerStarted(new com.android.server.criticalevents.nano.CriticalEventProto.SystemServerStarted());
        log(event);
    }

    public void logWatchdog(java.lang.String subject, java.util.UUID uuid) {
        com.android.server.criticalevents.nano.CriticalEventProto.Watchdog watchdog = new com.android.server.criticalevents.nano.CriticalEventProto.Watchdog();
        watchdog.subject = subject;
        watchdog.uuid = uuid.toString();
        com.android.server.criticalevents.nano.CriticalEventProto event = new com.android.server.criticalevents.nano.CriticalEventProto();
        event.setWatchdog(watchdog);
        log(event);
    }

    public void logHalfWatchdog(java.lang.String subject) {
        com.android.server.criticalevents.nano.CriticalEventProto.HalfWatchdog halfWatchdog = new com.android.server.criticalevents.nano.CriticalEventProto.HalfWatchdog();
        halfWatchdog.subject = subject;
        com.android.server.criticalevents.nano.CriticalEventProto event = new com.android.server.criticalevents.nano.CriticalEventProto();
        event.setHalfWatchdog(halfWatchdog);
        log(event);
    }

    public void logAnr(java.lang.String subject, int processClassEnum, java.lang.String processName, int uid, int pid) {
        com.android.server.criticalevents.nano.CriticalEventProto.AppNotResponding anr = new com.android.server.criticalevents.nano.CriticalEventProto.AppNotResponding();
        anr.subject = subject;
        anr.processClass = processClassEnum;
        anr.process = processName;
        anr.uid = uid;
        anr.pid = pid;
        com.android.server.criticalevents.nano.CriticalEventProto event = new com.android.server.criticalevents.nano.CriticalEventProto();
        event.setAnr(anr);
        log(event);
    }

    public void logJavaCrash(java.lang.String exceptionClass, int processClassEnum, java.lang.String processName, int uid, int pid) {
        com.android.server.criticalevents.nano.CriticalEventProto.JavaCrash crash = new com.android.server.criticalevents.nano.CriticalEventProto.JavaCrash();
        crash.exceptionClass = exceptionClass;
        crash.processClass = processClassEnum;
        crash.process = processName;
        crash.uid = uid;
        crash.pid = pid;
        com.android.server.criticalevents.nano.CriticalEventProto event = new com.android.server.criticalevents.nano.CriticalEventProto();
        event.setJavaCrash(crash);
        log(event);
    }

    public void logNativeCrash(int processClassEnum, java.lang.String processName, int uid, int pid) {
        com.android.server.criticalevents.nano.CriticalEventProto.NativeCrash crash = new com.android.server.criticalevents.nano.CriticalEventProto.NativeCrash();
        crash.processClass = processClassEnum;
        crash.process = processName;
        crash.uid = uid;
        crash.pid = pid;
        com.android.server.criticalevents.nano.CriticalEventProto event = new com.android.server.criticalevents.nano.CriticalEventProto();
        event.setNativeCrash(crash);
        log(event);
    }

    private void log(com.android.server.criticalevents.nano.CriticalEventProto event) {
        event.timestampMs = getWallTimeMillis();
        appendAndSave(event);
    }

    void appendAndSave(com.android.server.criticalevents.nano.CriticalEventProto event) {
        this.mEvents.append(event);
        saveLogToFile();
    }

    public java.lang.String logLinesForSystemServerTraceFile() {
        return logLinesForTraceFile(3, "AID_SYSTEM", 1000);
    }

    public java.lang.String logLinesForTraceFile(int traceProcessClassEnum, java.lang.String traceProcessName, int traceUid) {
        com.android.server.criticalevents.nano.CriticalEventLogProto outputLogProto = getOutputLogProto(traceProcessClassEnum, traceProcessName, traceUid);
        return "--- CriticalEventLog ---\n" + com.android.framework.protobuf.nano.MessageNanoPrinter.print(outputLogProto) + '\n';
    }

    protected com.android.server.criticalevents.nano.CriticalEventLogProto getOutputLogProto(int traceProcessClassEnum, java.lang.String traceProcessName, int traceUid) {
        com.android.server.criticalevents.nano.CriticalEventLogProto log = new com.android.server.criticalevents.nano.CriticalEventLogProto();
        log.timestampMs = getWallTimeMillis();
        log.windowMs = this.mWindowMs;
        log.capacity = this.mEvents.capacity();
        com.android.server.criticalevents.nano.CriticalEventProto[] events = recentEventsWithMinTimestamp(log.timestampMs - ((long) this.mWindowMs));
        com.android.server.criticalevents.CriticalEventLog.LogSanitizer sanitizer = new com.android.server.criticalevents.CriticalEventLog.LogSanitizer(traceProcessClassEnum, traceProcessName, traceUid);
        for (int i = 0; i < events.length; i++) {
            events[i] = sanitizer.process(events[i]);
        }
        log.events = events;
        return log;
    }

    private com.android.server.criticalevents.nano.CriticalEventProto[] recentEventsWithMinTimestamp(long minTimestampMs) {
        com.android.server.criticalevents.nano.CriticalEventProto[] allEvents = this.mEvents.toArray();
        for (int i = 0; i < allEvents.length; i++) {
            if (allEvents[i].timestampMs >= minTimestampMs) {
                return (com.android.server.criticalevents.nano.CriticalEventProto[]) java.util.Arrays.copyOfRange(allEvents, i, allEvents.length);
            }
        }
        return new com.android.server.criticalevents.nano.CriticalEventProto[0];
    }

    private void saveLogToFile() {
        if (this.mLoadAndSaveImmediately) {
            saveLogToFileNow();
        } else if (!this.mHandler.hasCallbacks(this.mSaveRunnable) && !this.mHandler.postDelayed(this.mSaveRunnable, saveDelayMs())) {
            android.util.Slog.w(TAG, "Error scheduling save");
        }
    }

    protected long saveDelayMs() {
        long nowMs = getWallTimeMillis();
        return java.lang.Math.max(0L, (this.mLastSaveAttemptMs + this.mMinTimeBetweenSavesMs) - nowMs);
    }

    protected void saveLogToFileNow() {
        this.mLastSaveAttemptMs = getWallTimeMillis();
        java.io.File logDir = this.mLogFile.getParentFile();
        if (!logDir.exists() && !logDir.mkdir()) {
            android.util.Slog.e(TAG, "Error creating log directory: " + logDir.getPath());
            return;
        }
        if (!this.mLogFile.exists()) {
            try {
                this.mLogFile.createNewFile();
            } catch (java.io.IOException e) {
                android.util.Slog.e(TAG, "Error creating log file", e);
                return;
            }
        }
        com.android.server.criticalevents.nano.CriticalEventLogStorageProto logProto = new com.android.server.criticalevents.nano.CriticalEventLogStorageProto();
        logProto.events = this.mEvents.toArray();
        byte[] bytes = com.android.server.criticalevents.nano.CriticalEventLogStorageProto.toByteArray(logProto);
        try {
            java.io.FileOutputStream stream = new java.io.FileOutputStream(this.mLogFile, false);
            try {
                stream.write(bytes);
                stream.close();
            } finally {
            }
        } catch (java.io.IOException e2) {
            android.util.Slog.e(TAG, "Error saving log to disk.", e2);
        }
    }

    protected static class ThreadSafeRingBuffer<T> {
        private final com.android.internal.util.RingBuffer<T> mBuffer;
        private final int mCapacity;

        ThreadSafeRingBuffer(java.lang.Class<T> clazz, int capacity) {
            this.mCapacity = capacity;
            this.mBuffer = new com.android.internal.util.RingBuffer<>(clazz, capacity);
        }

        synchronized void append(T t) {
            this.mBuffer.append(t);
        }

        synchronized T[] toArray() {
            return (T[]) this.mBuffer.toArray();
        }

        int capacity() {
            return this.mCapacity;
        }
    }

    static class LogLoader implements com.android.server.criticalevents.CriticalEventLog.ILogLoader {
        LogLoader() {
        }

        @Override // com.android.server.criticalevents.CriticalEventLog.ILogLoader
        public void load(java.io.File logFile, com.android.server.criticalevents.CriticalEventLog.ThreadSafeRingBuffer<com.android.server.criticalevents.nano.CriticalEventProto> buffer) {
            for (com.android.server.criticalevents.nano.CriticalEventProto event : loadLogFromFile(logFile).events) {
                buffer.append(event);
            }
        }

        private static com.android.server.criticalevents.nano.CriticalEventLogStorageProto loadLogFromFile(java.io.File logFile) {
            if (!logFile.exists()) {
                android.util.Slog.i(com.android.server.criticalevents.CriticalEventLog.TAG, "No log found, returning empty log proto.");
                return new com.android.server.criticalevents.nano.CriticalEventLogStorageProto();
            }
            try {
                return com.android.server.criticalevents.nano.CriticalEventLogStorageProto.parseFrom(java.nio.file.Files.readAllBytes(logFile.toPath()));
            } catch (java.io.IOException e) {
                android.util.Slog.e(com.android.server.criticalevents.CriticalEventLog.TAG, "Error reading log from disk.", e);
                return new com.android.server.criticalevents.nano.CriticalEventLogStorageProto();
            }
        }
    }

    private static class LogSanitizer {
        int mTraceProcessClassEnum;
        java.lang.String mTraceProcessName;
        int mTraceUid;

        LogSanitizer(int traceProcessClassEnum, java.lang.String traceProcessName, int traceUid) {
            this.mTraceProcessClassEnum = traceProcessClassEnum;
            this.mTraceProcessName = traceProcessName;
            this.mTraceUid = traceUid;
        }

        com.android.server.criticalevents.nano.CriticalEventProto process(com.android.server.criticalevents.nano.CriticalEventProto event) {
            if (event.hasAnr()) {
                com.android.server.criticalevents.nano.CriticalEventProto.AppNotResponding anr = event.getAnr();
                if (shouldSanitize(anr.processClass, anr.process, anr.uid)) {
                    return sanitizeAnr(event);
                }
            } else if (event.hasJavaCrash()) {
                com.android.server.criticalevents.nano.CriticalEventProto.JavaCrash crash = event.getJavaCrash();
                if (shouldSanitize(crash.processClass, crash.process, crash.uid)) {
                    return sanitizeJavaCrash(event);
                }
            } else if (event.hasNativeCrash()) {
                com.android.server.criticalevents.nano.CriticalEventProto.NativeCrash crash2 = event.getNativeCrash();
                if (shouldSanitize(crash2.processClass, crash2.process, crash2.uid)) {
                    return sanitizeNativeCrash(event);
                }
            }
            return event;
        }

        private boolean shouldSanitize(int processClassEnum, java.lang.String processName, int uid) {
            boolean sameApp = processName != null && processName.equals(this.mTraceProcessName) && this.mTraceUid == uid;
            return processClassEnum == 1 && this.mTraceProcessClassEnum == 1 && !sameApp;
        }

        private static com.android.server.criticalevents.nano.CriticalEventProto sanitizeAnr(com.android.server.criticalevents.nano.CriticalEventProto base) {
            com.android.server.criticalevents.nano.CriticalEventProto.AppNotResponding anr = new com.android.server.criticalevents.nano.CriticalEventProto.AppNotResponding();
            anr.processClass = base.getAnr().processClass;
            anr.uid = base.getAnr().uid;
            anr.pid = base.getAnr().pid;
            com.android.server.criticalevents.nano.CriticalEventProto sanitized = sanitizeCriticalEventProto(base);
            sanitized.setAnr(anr);
            return sanitized;
        }

        private static com.android.server.criticalevents.nano.CriticalEventProto sanitizeJavaCrash(com.android.server.criticalevents.nano.CriticalEventProto base) {
            com.android.server.criticalevents.nano.CriticalEventProto.JavaCrash crash = new com.android.server.criticalevents.nano.CriticalEventProto.JavaCrash();
            crash.processClass = base.getJavaCrash().processClass;
            crash.uid = base.getJavaCrash().uid;
            crash.pid = base.getJavaCrash().pid;
            com.android.server.criticalevents.nano.CriticalEventProto sanitized = sanitizeCriticalEventProto(base);
            sanitized.setJavaCrash(crash);
            return sanitized;
        }

        private static com.android.server.criticalevents.nano.CriticalEventProto sanitizeNativeCrash(com.android.server.criticalevents.nano.CriticalEventProto base) {
            com.android.server.criticalevents.nano.CriticalEventProto.NativeCrash crash = new com.android.server.criticalevents.nano.CriticalEventProto.NativeCrash();
            crash.processClass = base.getNativeCrash().processClass;
            crash.uid = base.getNativeCrash().uid;
            crash.pid = base.getNativeCrash().pid;
            com.android.server.criticalevents.nano.CriticalEventProto sanitized = sanitizeCriticalEventProto(base);
            sanitized.setNativeCrash(crash);
            return sanitized;
        }

        private static com.android.server.criticalevents.nano.CriticalEventProto sanitizeCriticalEventProto(com.android.server.criticalevents.nano.CriticalEventProto base) {
            com.android.server.criticalevents.nano.CriticalEventProto sanitized = new com.android.server.criticalevents.nano.CriticalEventProto();
            sanitized.timestampMs = base.timestampMs;
            return sanitized;
        }
    }
}
