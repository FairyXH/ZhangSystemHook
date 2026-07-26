package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class InstrumentationReporter {
    static final boolean DEBUG = false;
    static final int REPORT_TYPE_FINISHED = 1;
    static final int REPORT_TYPE_STATUS = 0;
    static final java.lang.String TAG = "ActivityManager";
    final java.lang.Object mLock = new java.lang.Object();
    java.util.ArrayList<com.android.server.am.InstrumentationReporter.Report> mPendingReports;
    java.lang.Thread mThread;

    final class MyThread extends java.lang.Thread {
        public MyThread() {
            super("InstrumentationReporter");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            java.util.ArrayList<com.android.server.am.InstrumentationReporter.Report> reports;
            android.os.Process.setThreadPriority(0);
            boolean waited = false;
            while (true) {
                synchronized (com.android.server.am.InstrumentationReporter.this.mLock) {
                    reports = com.android.server.am.InstrumentationReporter.this.mPendingReports;
                    com.android.server.am.InstrumentationReporter.this.mPendingReports = null;
                    if (reports == null || reports.isEmpty()) {
                        if (!waited) {
                            try {
                                com.android.server.am.InstrumentationReporter.this.mLock.wait(10000L);
                            } catch (java.lang.InterruptedException e) {
                            }
                            waited = true;
                        } else {
                            com.android.server.am.InstrumentationReporter.this.mThread = null;
                            return;
                        }
                    }
                }
                waited = false;
                for (int i = 0; i < reports.size(); i++) {
                    com.android.server.am.InstrumentationReporter.Report rep = reports.get(i);
                    try {
                        if (rep.mType == 0) {
                            rep.mWatcher.instrumentationStatus(rep.mName, rep.mResultCode, rep.mResults);
                        } else {
                            rep.mWatcher.instrumentationFinished(rep.mName, rep.mResultCode, rep.mResults);
                        }
                    } catch (android.os.RemoteException e2) {
                        android.util.Slog.i("ActivityManager", "Failure reporting to instrumentation watcher: comp=" + rep.mName + " results=" + rep.mResults);
                    }
                }
            }
        }
    }

    final class Report {
        final android.content.ComponentName mName;
        final int mResultCode;
        final android.os.Bundle mResults;
        final int mType;
        final android.app.IInstrumentationWatcher mWatcher;

        Report(int type, android.app.IInstrumentationWatcher watcher, android.content.ComponentName name, int resultCode, android.os.Bundle results) {
            this.mType = type;
            this.mWatcher = watcher;
            this.mName = name;
            this.mResultCode = resultCode;
            this.mResults = results;
            android.os.Binder.allowBlocking(this.mWatcher.asBinder());
        }
    }

    public void reportStatus(android.app.IInstrumentationWatcher watcher, android.content.ComponentName name, int resultCode, android.os.Bundle results) {
        report(new com.android.server.am.InstrumentationReporter.Report(0, watcher, name, resultCode, results));
    }

    public void reportFinished(android.app.IInstrumentationWatcher watcher, android.content.ComponentName name, int resultCode, android.os.Bundle results) {
        report(new com.android.server.am.InstrumentationReporter.Report(1, watcher, name, resultCode, results));
    }

    private void report(com.android.server.am.InstrumentationReporter.Report report) {
        synchronized (this.mLock) {
            if (this.mThread == null) {
                this.mThread = new com.android.server.am.InstrumentationReporter.MyThread();
                this.mThread.start();
            }
            if (this.mPendingReports == null) {
                this.mPendingReports = new java.util.ArrayList<>();
            }
            this.mPendingReports.add(report);
            this.mLock.notifyAll();
        }
    }
}
