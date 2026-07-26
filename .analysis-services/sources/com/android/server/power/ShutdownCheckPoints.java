package com.android.server.power;

/* JADX INFO: loaded from: classes3.dex */
public final class ShutdownCheckPoints {
    private static final int MAX_CHECK_POINTS = 100;
    private static final int MAX_DUMP_FILES = 20;
    private static final java.lang.String TAG = "ShutdownCheckPoints";
    private final java.util.ArrayList<com.android.server.power.ShutdownCheckPoints.CheckPoint> mCheckPoints;
    private final com.android.server.power.ShutdownCheckPoints.Injector mInjector;
    private static final com.android.server.power.ShutdownCheckPoints INSTANCE = new com.android.server.power.ShutdownCheckPoints();
    private static final java.text.SimpleDateFormat DATE_FORMAT = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
    private static final java.io.File[] EMPTY_FILE_ARRAY = new java.io.File[0];

    interface Injector {
        android.app.IActivityManager activityManager();

        long currentTimeMillis();

        int maxCheckPoints();

        int maxDumpFiles();
    }

    private ShutdownCheckPoints() {
        this(new com.android.server.power.ShutdownCheckPoints.Injector() { // from class: com.android.server.power.ShutdownCheckPoints.1
            @Override // com.android.server.power.ShutdownCheckPoints.Injector
            public long currentTimeMillis() {
                return java.lang.System.currentTimeMillis();
            }

            @Override // com.android.server.power.ShutdownCheckPoints.Injector
            public int maxCheckPoints() {
                return 100;
            }

            @Override // com.android.server.power.ShutdownCheckPoints.Injector
            public int maxDumpFiles() {
                return 20;
            }

            @Override // com.android.server.power.ShutdownCheckPoints.Injector
            public android.app.IActivityManager activityManager() {
                return android.app.ActivityManager.getService();
            }
        });
    }

    ShutdownCheckPoints(com.android.server.power.ShutdownCheckPoints.Injector injector) {
        this.mCheckPoints = new java.util.ArrayList<>();
        this.mInjector = injector;
    }

    public static void recordCheckPoint(java.lang.String reason) {
        INSTANCE.recordCheckPointInternal(reason);
    }

    public static void recordCheckPoint(int callerProcessId, java.lang.String reason) {
        INSTANCE.recordCheckPointInternal(callerProcessId, reason);
    }

    public static void recordCheckPoint(java.lang.String intentName, java.lang.String packageName, java.lang.String reason) {
        INSTANCE.recordCheckPointInternal(intentName, packageName, reason);
    }

    public static void dump(java.io.PrintWriter printWriter) {
        INSTANCE.dumpInternal(printWriter);
    }

    public static java.lang.Thread newDumpThread(java.io.File baseFile) {
        return INSTANCE.newDumpThreadInternal(baseFile);
    }

    void recordCheckPointInternal(java.lang.String reason) {
        recordCheckPointInternal(new com.android.server.power.ShutdownCheckPoints.SystemServerCheckPoint(this.mInjector.currentTimeMillis(), reason));
        android.util.Slog.v(TAG, "System server shutdown checkpoint recorded");
    }

    void recordCheckPointInternal(int callerProcessId, java.lang.String reason) {
        com.android.server.power.ShutdownCheckPoints.CheckPoint binderCheckPoint;
        long timestamp = this.mInjector.currentTimeMillis();
        if (callerProcessId == android.os.Process.myPid()) {
            binderCheckPoint = new com.android.server.power.ShutdownCheckPoints.SystemServerCheckPoint(timestamp, reason);
        } else {
            binderCheckPoint = new com.android.server.power.ShutdownCheckPoints.BinderCheckPoint(timestamp, callerProcessId, reason);
        }
        recordCheckPointInternal(binderCheckPoint);
        android.util.Slog.v(TAG, "Binder shutdown checkpoint recorded with pid=" + callerProcessId);
    }

    void recordCheckPointInternal(java.lang.String intentName, java.lang.String packageName, java.lang.String reason) {
        com.android.server.power.ShutdownCheckPoints.CheckPoint intentCheckPoint;
        long timestamp = this.mInjector.currentTimeMillis();
        if (com.android.server.pm.PackageManagerService.PLATFORM_PACKAGE_NAME.equals(packageName)) {
            intentCheckPoint = new com.android.server.power.ShutdownCheckPoints.SystemServerCheckPoint(timestamp, reason);
        } else {
            intentCheckPoint = new com.android.server.power.ShutdownCheckPoints.IntentCheckPoint(timestamp, intentName, packageName, reason);
        }
        recordCheckPointInternal(intentCheckPoint);
        android.util.Slog.v(TAG, java.lang.String.format("Shutdown intent checkpoint recorded intent=%s from package=%s", intentName, packageName));
    }

    private void recordCheckPointInternal(com.android.server.power.ShutdownCheckPoints.CheckPoint checkPoint) {
        synchronized (this.mCheckPoints) {
            this.mCheckPoints.add(checkPoint);
            if (this.mCheckPoints.size() > this.mInjector.maxCheckPoints()) {
                this.mCheckPoints.remove(0);
            }
        }
    }

    void dumpInternal(java.io.PrintWriter printWriter) {
        java.util.List<com.android.server.power.ShutdownCheckPoints.CheckPoint> records;
        synchronized (this.mCheckPoints) {
            records = new java.util.ArrayList<>(this.mCheckPoints);
        }
        for (com.android.server.power.ShutdownCheckPoints.CheckPoint record : records) {
            record.dump(this.mInjector, printWriter);
            printWriter.println();
        }
    }

    java.lang.Thread newDumpThreadInternal(java.io.File baseFile) {
        return new com.android.server.power.ShutdownCheckPoints.FileDumperThread(this, baseFile, this.mInjector.maxDumpFiles());
    }

    private static abstract class CheckPoint {
        private final java.lang.String mReason;
        private final long mTimestamp;

        abstract void dumpDetails(com.android.server.power.ShutdownCheckPoints.Injector injector, java.io.PrintWriter printWriter);

        abstract java.lang.String getOrigin();

        CheckPoint(long timestamp, java.lang.String reason) {
            this.mTimestamp = timestamp;
            this.mReason = reason;
        }

        final void dump(com.android.server.power.ShutdownCheckPoints.Injector injector, java.io.PrintWriter printWriter) {
            printWriter.print("Shutdown request from ");
            printWriter.print(getOrigin());
            if (this.mReason != null) {
                printWriter.print(" for reason ");
                printWriter.print(this.mReason);
            }
            printWriter.print(" at ");
            printWriter.print(com.android.server.power.ShutdownCheckPoints.DATE_FORMAT.format(new java.util.Date(this.mTimestamp)));
            printWriter.println(" (epoch=" + this.mTimestamp + ")");
            dumpDetails(injector, printWriter);
        }
    }

    private static class SystemServerCheckPoint extends com.android.server.power.ShutdownCheckPoints.CheckPoint {
        private final java.lang.StackTraceElement[] mStackTraceElements;

        SystemServerCheckPoint(long timestamp, java.lang.String reason) {
            super(timestamp, reason);
            this.mStackTraceElements = java.lang.Thread.currentThread().getStackTrace();
        }

        @Override // com.android.server.power.ShutdownCheckPoints.CheckPoint
        java.lang.String getOrigin() {
            return "SYSTEM";
        }

        @Override // com.android.server.power.ShutdownCheckPoints.CheckPoint
        void dumpDetails(com.android.server.power.ShutdownCheckPoints.Injector injector, java.io.PrintWriter printWriter) {
            java.lang.String methodName = findMethodName();
            printWriter.println(methodName == null ? "Failed to get method name" : methodName);
            printStackTrace(printWriter);
        }

        java.lang.String findMethodName() {
            int idx = findCallSiteIndex();
            if (idx < this.mStackTraceElements.length) {
                java.lang.StackTraceElement element = this.mStackTraceElements[idx];
                return java.lang.String.format("%s.%s", element.getClassName(), element.getMethodName());
            }
            return null;
        }

        void printStackTrace(java.io.PrintWriter printWriter) {
            int i = findCallSiteIndex();
            while (true) {
                i++;
                if (i < this.mStackTraceElements.length) {
                    printWriter.print(" at ");
                    printWriter.println(this.mStackTraceElements[i]);
                } else {
                    return;
                }
            }
        }

        private int findCallSiteIndex() {
            java.lang.String className = com.android.server.power.ShutdownCheckPoints.class.getCanonicalName();
            int idx = 0;
            while (idx < this.mStackTraceElements.length && !this.mStackTraceElements[idx].getClassName().equals(className)) {
                idx++;
            }
            while (idx < this.mStackTraceElements.length && this.mStackTraceElements[idx].getClassName().equals(className)) {
                idx++;
            }
            return idx;
        }
    }

    private static class BinderCheckPoint extends com.android.server.power.ShutdownCheckPoints.SystemServerCheckPoint {
        private final int mCallerProcessId;

        BinderCheckPoint(long timestamp, int callerProcessId, java.lang.String reason) {
            super(timestamp, reason);
            this.mCallerProcessId = callerProcessId;
        }

        @Override // com.android.server.power.ShutdownCheckPoints.SystemServerCheckPoint, com.android.server.power.ShutdownCheckPoints.CheckPoint
        java.lang.String getOrigin() {
            return "BINDER";
        }

        @Override // com.android.server.power.ShutdownCheckPoints.SystemServerCheckPoint, com.android.server.power.ShutdownCheckPoints.CheckPoint
        void dumpDetails(com.android.server.power.ShutdownCheckPoints.Injector injector, java.io.PrintWriter printWriter) {
            java.lang.String methodName = findMethodName();
            printWriter.println(methodName == null ? "Failed to get method name" : methodName);
            java.lang.String processName = findProcessName(injector.activityManager());
            printWriter.print("From process ");
            printWriter.print(processName == null ? "?" : processName);
            printWriter.println(" (pid=" + this.mCallerProcessId + ")");
        }

        private java.lang.String findProcessName(android.app.IActivityManager activityManager) {
            java.util.List<android.app.ActivityManager.RunningAppProcessInfo> runningProcesses = null;
            try {
                if (activityManager == null) {
                    android.util.Slog.v(com.android.server.power.ShutdownCheckPoints.TAG, "No ActivityManager to find name of process with pid=" + this.mCallerProcessId);
                } else {
                    runningProcesses = activityManager.getRunningAppProcesses();
                }
                if (runningProcesses != null) {
                    for (android.app.ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                        if (processInfo.pid == this.mCallerProcessId) {
                            return processInfo.processName;
                        }
                    }
                    return null;
                }
                return null;
            } catch (android.os.RemoteException e) {
                android.util.Slog.e(com.android.server.power.ShutdownCheckPoints.TAG, "Failed to get running app processes from ActivityManager", e);
                return null;
            }
        }
    }

    private static class IntentCheckPoint extends com.android.server.power.ShutdownCheckPoints.CheckPoint {
        private final java.lang.String mIntentName;
        private final java.lang.String mPackageName;

        IntentCheckPoint(long timestamp, java.lang.String intentName, java.lang.String packageName, java.lang.String reason) {
            super(timestamp, reason);
            this.mIntentName = intentName;
            this.mPackageName = packageName;
        }

        @Override // com.android.server.power.ShutdownCheckPoints.CheckPoint
        java.lang.String getOrigin() {
            return "INTENT";
        }

        @Override // com.android.server.power.ShutdownCheckPoints.CheckPoint
        void dumpDetails(com.android.server.power.ShutdownCheckPoints.Injector injector, java.io.PrintWriter printWriter) {
            printWriter.print("Intent: ");
            printWriter.println(this.mIntentName);
            printWriter.print("Package: ");
            printWriter.println(this.mPackageName);
        }
    }

    private static final class FileDumperThread extends java.lang.Thread {
        private final java.io.File mBaseFile;
        private final int mFileCountLimit;
        private final com.android.server.power.ShutdownCheckPoints mInstance;

        FileDumperThread(com.android.server.power.ShutdownCheckPoints instance, java.io.File baseFile, int fileCountLimit) {
            this.mInstance = instance;
            this.mBaseFile = baseFile;
            this.mFileCountLimit = fileCountLimit;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            this.mBaseFile.getParentFile().mkdirs();
            java.io.File[] checkPointFiles = listCheckPointsFiles();
            int filesToDelete = (checkPointFiles.length - this.mFileCountLimit) + 1;
            for (int i = 0; i < filesToDelete; i++) {
                checkPointFiles[i].delete();
            }
            java.io.File nextCheckPointsFile = new java.io.File(java.lang.String.format("%s-%d", this.mBaseFile.getAbsolutePath(), java.lang.Long.valueOf(java.lang.System.currentTimeMillis())));
            writeCheckpoints(nextCheckPointsFile);
        }

        private java.io.File[] listCheckPointsFiles() {
            final java.lang.String filePrefix = this.mBaseFile.getName() + "-";
            java.io.File[] files = this.mBaseFile.getParentFile().listFiles(new java.io.FilenameFilter() { // from class: com.android.server.power.ShutdownCheckPoints.FileDumperThread.1
                @Override // java.io.FilenameFilter
                public boolean accept(java.io.File dir, java.lang.String name) {
                    if (!name.startsWith(filePrefix)) {
                        return false;
                    }
                    try {
                        java.lang.Long.valueOf(name.substring(filePrefix.length()));
                        return true;
                    } catch (java.lang.NumberFormatException e) {
                        return false;
                    }
                }
            });
            if (files == null) {
                return com.android.server.power.ShutdownCheckPoints.EMPTY_FILE_ARRAY;
            }
            java.util.Arrays.sort(files);
            return files;
        }

        private void writeCheckpoints(java.io.File file) {
            android.util.AtomicFile tmpFile = new android.util.AtomicFile(this.mBaseFile);
            java.io.FileOutputStream fos = null;
            try {
                fos = tmpFile.startWrite();
                java.io.PrintWriter pw = new java.io.PrintWriter(fos);
                this.mInstance.dumpInternal(pw);
                pw.flush();
                tmpFile.finishWrite(fos);
            } catch (java.io.IOException e) {
                android.util.Log.e(com.android.server.power.ShutdownCheckPoints.TAG, "Failed to write shutdown checkpoints", e);
                if (fos != null) {
                    tmpFile.failWrite(fos);
                }
            }
            this.mBaseFile.renameTo(file);
        }
    }
}
