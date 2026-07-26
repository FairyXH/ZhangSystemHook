package com.android.server.content;

/* JADX INFO: loaded from: classes.dex */
public class SyncLogger {
    public static final int CALLING_UID_SELF = -1;
    private static final java.lang.String TAG = "SyncLogger";
    private static com.android.server.content.SyncLogger sInstance;

    SyncLogger() {
    }

    public static synchronized com.android.server.content.SyncLogger getInstance() {
        if (sInstance == null) {
            java.lang.String flag = android.os.SystemProperties.get("debug.synclog");
            boolean enable = (android.os.Build.IS_DEBUGGABLE || "1".equals(flag) || android.util.Log.isLoggable(TAG, 2)) && !"0".equals(flag);
            if (enable) {
                sInstance = new com.android.server.content.SyncLogger.RotatingFileLogger();
            } else {
                sInstance = new com.android.server.content.SyncLogger();
            }
        }
        return sInstance;
    }

    public void log(java.lang.Object... message) {
    }

    public void purgeOldLogs() {
    }

    public java.lang.String jobParametersToString(android.app.job.JobParameters params) {
        return "";
    }

    public void dumpAll(java.io.PrintWriter pw) {
    }

    public boolean enabled() {
        return false;
    }

    private static class RotatingFileLogger extends com.android.server.content.SyncLogger {
        private long mCurrentLogFileDayTimestamp;
        private boolean mErrorShown;
        private java.io.Writer mLogWriter;
        private static final java.text.SimpleDateFormat sTimestampFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        private static final java.text.SimpleDateFormat sFilenameDateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
        private static final boolean DO_LOGCAT = android.util.Log.isLoggable(com.android.server.content.SyncLogger.TAG, 3);
        private final java.lang.Object mLock = new java.lang.Object();
        private final long mKeepAgeMs = java.util.concurrent.TimeUnit.DAYS.toMillis(7);
        private final java.util.Date mCachedDate = new java.util.Date();
        private final java.lang.StringBuilder mStringBuilder = new java.lang.StringBuilder();
        private final java.io.File mLogPath = new java.io.File(android.os.Environment.getDataSystemDirectory(), "syncmanager-log");
        private final com.android.server.content.SyncLogger.RotatingFileLogger.MyHandler mHandler = new com.android.server.content.SyncLogger.RotatingFileLogger.MyHandler(com.android.server.IoThread.get().getLooper());

        RotatingFileLogger() {
        }

        @Override // com.android.server.content.SyncLogger
        public boolean enabled() {
            return true;
        }

        private void handleException(java.lang.String message, java.lang.Exception e) {
            if (!this.mErrorShown) {
                android.util.Slog.e(com.android.server.content.SyncLogger.TAG, message, e);
                this.mErrorShown = true;
            }
        }

        @Override // com.android.server.content.SyncLogger
        public void log(java.lang.Object... message) {
            if (message == null) {
                return;
            }
            long now = java.lang.System.currentTimeMillis();
            this.mHandler.log(now, message);
        }

        void logInner(long now, java.lang.Object[] message) {
            synchronized (this.mLock) {
                openLogLocked(now);
                if (this.mLogWriter == null) {
                    return;
                }
                this.mStringBuilder.setLength(0);
                this.mCachedDate.setTime(now);
                this.mStringBuilder.append(sTimestampFormat.format(this.mCachedDate));
                this.mStringBuilder.append(' ');
                this.mStringBuilder.append(android.os.Process.myTid());
                this.mStringBuilder.append(' ');
                int messageStart = this.mStringBuilder.length();
                for (java.lang.Object o : message) {
                    this.mStringBuilder.append(o);
                }
                this.mStringBuilder.append('\n');
                try {
                    this.mLogWriter.append((java.lang.CharSequence) this.mStringBuilder);
                    this.mLogWriter.flush();
                    if (DO_LOGCAT) {
                        android.util.Log.d(com.android.server.content.SyncLogger.TAG, this.mStringBuilder.substring(messageStart));
                    }
                } catch (java.io.IOException e) {
                    handleException("Failed to write log", e);
                }
            }
        }

        private void openLogLocked(long now) {
            long day = now % 86400000;
            if (this.mLogWriter != null && day == this.mCurrentLogFileDayTimestamp) {
                return;
            }
            closeCurrentLogLocked();
            this.mCurrentLogFileDayTimestamp = day;
            this.mCachedDate.setTime(now);
            java.lang.String filename = "synclog-" + sFilenameDateFormat.format(this.mCachedDate) + ".log";
            java.io.File file = new java.io.File(this.mLogPath, filename);
            file.getParentFile().mkdirs();
            try {
                this.mLogWriter = new java.io.FileWriter(file, true);
            } catch (java.io.IOException e) {
                handleException("Failed to open log file: " + file, e);
            }
        }

        private void closeCurrentLogLocked() {
            libcore.io.IoUtils.closeQuietly(this.mLogWriter);
            this.mLogWriter = null;
        }

        @Override // com.android.server.content.SyncLogger
        public void purgeOldLogs() {
            synchronized (this.mLock) {
                android.os.FileUtils.deleteOlderFiles(this.mLogPath, 1, this.mKeepAgeMs);
            }
        }

        @Override // com.android.server.content.SyncLogger
        public java.lang.String jobParametersToString(android.app.job.JobParameters params) {
            return com.android.server.content.SyncJobService.jobParametersToString(params);
        }

        @Override // com.android.server.content.SyncLogger
        public void dumpAll(java.io.PrintWriter pw) {
            synchronized (this.mLock) {
                java.lang.String[] files = this.mLogPath.list();
                if (files != null && files.length != 0) {
                    java.util.Arrays.sort(files);
                    for (java.lang.String file : files) {
                        dumpFile(pw, new java.io.File(this.mLogPath, file));
                    }
                }
            }
        }

        private void dumpFile(java.io.PrintWriter pw, java.io.File file) {
            android.util.Slog.w(com.android.server.content.SyncLogger.TAG, "Dumping " + file);
            char[] buffer = new char[32768];
            try {
                java.io.Reader in = new java.io.BufferedReader(new java.io.FileReader(file));
                while (true) {
                    try {
                        int read = in.read(buffer);
                        if (read >= 0) {
                            if (read > 0) {
                                pw.write(buffer, 0, read);
                            }
                        } else {
                            in.close();
                            return;
                        }
                    } finally {
                    }
                }
            } catch (java.io.IOException e) {
            }
        }

        private class MyHandler extends android.os.Handler {
            public static final int MSG_LOG_ID = 1;

            MyHandler(android.os.Looper looper) {
                super(looper);
            }

            public void log(long now, java.lang.Object[] message) {
                obtainMessage(1, com.android.internal.util.IntPair.first(now), com.android.internal.util.IntPair.second(now), message).sendToTarget();
            }

            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                        com.android.server.content.SyncLogger.RotatingFileLogger.this.logInner(com.android.internal.util.IntPair.of(msg.arg1, msg.arg2), (java.lang.Object[]) msg.obj);
                        break;
                }
            }
        }
    }

    static java.lang.String logSafe(android.accounts.Account account) {
        return account == null ? "[null]" : account.toSafeString();
    }

    static java.lang.String logSafe(com.android.server.content.SyncStorageEngine.EndPoint endPoint) {
        return endPoint == null ? "[null]" : endPoint.toSafeString();
    }

    static java.lang.String logSafe(com.android.server.content.SyncOperation operation) {
        return operation == null ? "[null]" : operation.toSafeString();
    }

    static java.lang.String logSafe(com.android.server.content.SyncManager.ActiveSyncContext asc) {
        return asc == null ? "[null]" : asc.toSafeString();
    }
}
