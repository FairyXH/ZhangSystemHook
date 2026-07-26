package com.android.server.print;

/* JADX INFO: loaded from: classes3.dex */
final class RemotePrintSpooler {
    private static final long BIND_SPOOLER_SERVICE_TIMEOUT;
    private static final boolean DEBUG = false;
    private static final java.lang.String LOG_TAG = "RemotePrintSpooler";
    private final com.android.server.print.RemotePrintSpooler.PrintSpoolerCallbacks mCallbacks;
    private boolean mCanUnbind;
    private final android.content.Context mContext;
    private boolean mDestroyed;
    private boolean mIsBinding;
    private boolean mIsLowPriority;
    private android.print.IPrintSpooler mRemoteInstance;
    private final android.os.UserHandle mUserHandle;
    private final java.lang.Object mLock = new java.lang.Object();
    private final com.android.server.print.RemotePrintSpooler.GetPrintJobInfosCaller mGetPrintJobInfosCaller = new com.android.server.print.RemotePrintSpooler.GetPrintJobInfosCaller();
    private final com.android.server.print.RemotePrintSpooler.GetPrintJobInfoCaller mGetPrintJobInfoCaller = new com.android.server.print.RemotePrintSpooler.GetPrintJobInfoCaller();
    private final com.android.server.print.RemotePrintSpooler.SetPrintJobStateCaller mSetPrintJobStatusCaller = new com.android.server.print.RemotePrintSpooler.SetPrintJobStateCaller();
    private final com.android.server.print.RemotePrintSpooler.SetPrintJobTagCaller mSetPrintJobTagCaller = new com.android.server.print.RemotePrintSpooler.SetPrintJobTagCaller();
    private final com.android.server.print.RemotePrintSpooler.OnCustomPrinterIconLoadedCaller mCustomPrinterIconLoadedCaller = new com.android.server.print.RemotePrintSpooler.OnCustomPrinterIconLoadedCaller();
    private final com.android.server.print.RemotePrintSpooler.ClearCustomPrinterIconCacheCaller mClearCustomPrinterIconCache = new com.android.server.print.RemotePrintSpooler.ClearCustomPrinterIconCacheCaller();
    private final com.android.server.print.RemotePrintSpooler.GetCustomPrinterIconCaller mGetCustomPrinterIconCaller = new com.android.server.print.RemotePrintSpooler.GetCustomPrinterIconCaller();
    private final android.content.ServiceConnection mServiceConnection = new com.android.server.print.RemotePrintSpooler.MyServiceConnection();
    private final com.android.server.print.RemotePrintSpooler.PrintSpoolerClient mClient = new com.android.server.print.RemotePrintSpooler.PrintSpoolerClient(this);
    private final android.content.Intent mIntent = new android.content.Intent();

    public interface PrintSpoolerCallbacks {
        void onAllPrintJobsForServiceHandled(android.content.ComponentName componentName);

        void onPrintJobQueued(android.print.PrintJobInfo printJobInfo);

        void onPrintJobStateChanged(android.print.PrintJobInfo printJobInfo);
    }

    static {
        BIND_SPOOLER_SERVICE_TIMEOUT = android.os.Build.IS_ENG ? 30000L : 3000L;
    }

    public RemotePrintSpooler(android.content.Context context, int userId, boolean lowPriority, com.android.server.print.RemotePrintSpooler.PrintSpoolerCallbacks callbacks) {
        this.mContext = context;
        this.mUserHandle = new android.os.UserHandle(userId);
        this.mCallbacks = callbacks;
        this.mIsLowPriority = lowPriority;
        this.mIntent.setComponent(new android.content.ComponentName("com.android.printspooler", "com.android.printspooler.model.PrintSpoolerService"));
    }

    public void increasePriority() {
        if (this.mIsLowPriority) {
            this.mIsLowPriority = false;
            synchronized (this.mLock) {
                throwIfDestroyedLocked();
                while (!this.mCanUnbind) {
                    try {
                        this.mLock.wait();
                    } catch (java.lang.InterruptedException e) {
                        android.util.Slog.e(LOG_TAG, "Interrupted while waiting for operation to complete");
                    }
                }
                unbindLocked();
            }
        }
    }

    public final java.util.List<android.print.PrintJobInfo> getPrintJobInfos(android.content.ComponentName componentName, int state, int appId) {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            this.mCanUnbind = false;
        }
        try {
            try {
                java.util.List<android.print.PrintJobInfo> printJobInfos = this.mGetPrintJobInfosCaller.getPrintJobInfos(getRemoteInstanceLazy(), componentName, state, appId);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                }
                return printJobInfos;
            } catch (android.os.RemoteException | java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.e(LOG_TAG, "Error getting print jobs.", e);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                    return null;
                }
            }
        } catch (java.lang.Throwable th) {
            synchronized (this.mLock) {
                this.mCanUnbind = true;
                this.mLock.notifyAll();
                throw th;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void createPrintJob(android.print.PrintJobInfo printJobInfo) {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            this.mCanUnbind = false;
        }
        boolean z = 1;
        z = 1;
        try {
            try {
                getRemoteInstanceLazy().createPrintJob(printJobInfo);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    java.lang.Object obj = this.mLock;
                    obj.notifyAll();
                    z = obj;
                }
            } catch (android.os.RemoteException | java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.e(LOG_TAG, "Error creating print job.", e);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    java.lang.Object obj2 = this.mLock;
                    obj2.notifyAll();
                    z = obj2;
                }
            }
        } catch (java.lang.Throwable th) {
            synchronized (this.mLock) {
                this.mCanUnbind = z;
                this.mLock.notifyAll();
                throw th;
            }
        }
    }

    public final void writePrintJobData(android.os.ParcelFileDescriptor fd, android.print.PrintJobId printJobId) {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            this.mCanUnbind = false;
        }
        try {
            try {
                getRemoteInstanceLazy().writePrintJobData(fd, printJobId);
                libcore.io.IoUtils.closeQuietly(fd);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                }
            } catch (android.os.RemoteException | java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.e(LOG_TAG, "Error writing print job data.", e);
                libcore.io.IoUtils.closeQuietly(fd);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                }
            }
        } catch (java.lang.Throwable th) {
            libcore.io.IoUtils.closeQuietly(fd);
            synchronized (this.mLock) {
                this.mCanUnbind = true;
                this.mLock.notifyAll();
                throw th;
            }
        }
    }

    public final android.print.PrintJobInfo getPrintJobInfo(android.print.PrintJobId printJobId, int appId) {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            this.mCanUnbind = false;
        }
        try {
            try {
                android.print.PrintJobInfo printJobInfo = this.mGetPrintJobInfoCaller.getPrintJobInfo(getRemoteInstanceLazy(), printJobId, appId);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                }
                return printJobInfo;
            } catch (android.os.RemoteException | java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.e(LOG_TAG, "Error getting print job info.", e);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                    return null;
                }
            }
        } catch (java.lang.Throwable th) {
            synchronized (this.mLock) {
                this.mCanUnbind = true;
                this.mLock.notifyAll();
                throw th;
            }
        }
    }

    public final boolean setPrintJobState(android.print.PrintJobId printJobId, int state, java.lang.String error) {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            this.mCanUnbind = false;
        }
        try {
            try {
                boolean printJobState = this.mSetPrintJobStatusCaller.setPrintJobState(getRemoteInstanceLazy(), printJobId, state, error);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                }
                return printJobState;
            } catch (android.os.RemoteException | java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.e(LOG_TAG, "Error setting print job state.", e);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                    return false;
                }
            }
        } catch (java.lang.Throwable th) {
            synchronized (this.mLock) {
                this.mCanUnbind = true;
                this.mLock.notifyAll();
                throw th;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void setProgress(android.print.PrintJobId printJobId, float f) {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            this.mCanUnbind = false;
        }
        boolean z = 1;
        z = 1;
        try {
            try {
                getRemoteInstanceLazy().setProgress(printJobId, f);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    java.lang.Object obj = this.mLock;
                    obj.notifyAll();
                    z = obj;
                }
            } catch (android.os.RemoteException | java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.e(LOG_TAG, "Error setting progress.", e);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    java.lang.Object obj2 = this.mLock;
                    obj2.notifyAll();
                    z = obj2;
                }
            }
        } catch (java.lang.Throwable th) {
            synchronized (this.mLock) {
                this.mCanUnbind = z;
                this.mLock.notifyAll();
                throw th;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void setStatus(android.print.PrintJobId printJobId, java.lang.CharSequence charSequence) {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            this.mCanUnbind = false;
        }
        boolean z = 1;
        z = 1;
        try {
            try {
                getRemoteInstanceLazy().setStatus(printJobId, charSequence);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    java.lang.Object obj = this.mLock;
                    obj.notifyAll();
                    z = obj;
                }
            } catch (android.os.RemoteException | java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.e(LOG_TAG, "Error setting status.", e);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    java.lang.Object obj2 = this.mLock;
                    obj2.notifyAll();
                    z = obj2;
                }
            }
        } catch (java.lang.Throwable th) {
            synchronized (this.mLock) {
                this.mCanUnbind = z;
                this.mLock.notifyAll();
                throw th;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void setStatus(android.print.PrintJobId printJobId, int i, java.lang.CharSequence charSequence) {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            this.mCanUnbind = false;
        }
        boolean z = 1;
        z = 1;
        try {
            try {
                getRemoteInstanceLazy().setStatusRes(printJobId, i, charSequence);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    java.lang.Object obj = this.mLock;
                    obj.notifyAll();
                    z = obj;
                }
            } catch (android.os.RemoteException | java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.e(LOG_TAG, "Error setting status.", e);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    java.lang.Object obj2 = this.mLock;
                    obj2.notifyAll();
                    z = obj2;
                }
            }
        } catch (java.lang.Throwable th) {
            synchronized (this.mLock) {
                this.mCanUnbind = z;
                this.mLock.notifyAll();
                throw th;
            }
        }
    }

    public final void onCustomPrinterIconLoaded(android.print.PrinterId printerId, android.graphics.drawable.Icon icon) {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            this.mCanUnbind = false;
        }
        try {
            try {
                this.mCustomPrinterIconLoadedCaller.onCustomPrinterIconLoaded(getRemoteInstanceLazy(), printerId, icon);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                }
            } catch (android.os.RemoteException | java.lang.InterruptedException | java.util.concurrent.TimeoutException re) {
                android.util.Slog.e(LOG_TAG, "Error loading new custom printer icon.", re);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                }
            }
        } catch (java.lang.Throwable th) {
            synchronized (this.mLock) {
                this.mCanUnbind = true;
                this.mLock.notifyAll();
                throw th;
            }
        }
    }

    public final android.graphics.drawable.Icon getCustomPrinterIcon(android.print.PrinterId printerId) {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            this.mCanUnbind = false;
        }
        try {
            try {
                android.graphics.drawable.Icon customPrinterIcon = this.mGetCustomPrinterIconCaller.getCustomPrinterIcon(getRemoteInstanceLazy(), printerId);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                }
                return customPrinterIcon;
            } catch (android.os.RemoteException | java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.e(LOG_TAG, "Error getting custom printer icon.", e);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                    return null;
                }
            }
        } catch (java.lang.Throwable th) {
            synchronized (this.mLock) {
                this.mCanUnbind = true;
                this.mLock.notifyAll();
                throw th;
            }
        }
    }

    public void clearCustomPrinterIconCache() {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            this.mCanUnbind = false;
        }
        try {
            try {
                this.mClearCustomPrinterIconCache.clearCustomPrinterIconCache(getRemoteInstanceLazy());
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                }
            } catch (android.os.RemoteException | java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.e(LOG_TAG, "Error clearing custom printer icon cache.", e);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                }
            }
        } catch (java.lang.Throwable th) {
            synchronized (this.mLock) {
                this.mCanUnbind = true;
                this.mLock.notifyAll();
                throw th;
            }
        }
    }

    public final boolean setPrintJobTag(android.print.PrintJobId printJobId, java.lang.String tag) {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            this.mCanUnbind = false;
        }
        try {
            try {
                boolean printJobTag = this.mSetPrintJobTagCaller.setPrintJobTag(getRemoteInstanceLazy(), printJobId, tag);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                }
                return printJobTag;
            } catch (android.os.RemoteException | java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.e(LOG_TAG, "Error setting print job tag.", e);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    this.mLock.notifyAll();
                    return false;
                }
            }
        } catch (java.lang.Throwable th) {
            synchronized (this.mLock) {
                this.mCanUnbind = true;
                this.mLock.notifyAll();
                throw th;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void setPrintJobCancelling(android.print.PrintJobId printJobId, boolean z) {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            this.mCanUnbind = false;
        }
        boolean z2 = 1;
        z2 = 1;
        try {
            try {
                getRemoteInstanceLazy().setPrintJobCancelling(printJobId, z);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    java.lang.Object obj = this.mLock;
                    obj.notifyAll();
                    z2 = obj;
                }
            } catch (android.os.RemoteException | java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.e(LOG_TAG, "Error setting print job cancelling.", e);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    java.lang.Object obj2 = this.mLock;
                    obj2.notifyAll();
                    z2 = obj2;
                }
            }
        } catch (java.lang.Throwable th) {
            synchronized (this.mLock) {
                this.mCanUnbind = z2;
                this.mLock.notifyAll();
                throw th;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void pruneApprovedPrintServices(java.util.List<android.content.ComponentName> list) {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            this.mCanUnbind = false;
        }
        boolean z = 1;
        z = 1;
        try {
            try {
                getRemoteInstanceLazy().pruneApprovedPrintServices(list);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    java.lang.Object obj = this.mLock;
                    obj.notifyAll();
                    z = obj;
                }
            } catch (android.os.RemoteException | java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.e(LOG_TAG, "Error pruning approved print services.", e);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    java.lang.Object obj2 = this.mLock;
                    obj2.notifyAll();
                    z = obj2;
                }
            }
        } catch (java.lang.Throwable th) {
            synchronized (this.mLock) {
                this.mCanUnbind = z;
                this.mLock.notifyAll();
                throw th;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final void removeObsoletePrintJobs() {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            this.mCanUnbind = false;
        }
        boolean z = 1;
        z = 1;
        try {
            try {
                getRemoteInstanceLazy().removeObsoletePrintJobs();
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    java.lang.Object obj = this.mLock;
                    obj.notifyAll();
                    z = obj;
                }
            } catch (android.os.RemoteException | java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
                android.util.Slog.e(LOG_TAG, "Error removing obsolete print jobs .", e);
                synchronized (this.mLock) {
                    this.mCanUnbind = true;
                    java.lang.Object obj2 = this.mLock;
                    obj2.notifyAll();
                    z = obj2;
                }
            }
        } catch (java.lang.Throwable th) {
            synchronized (this.mLock) {
                this.mCanUnbind = z;
                this.mLock.notifyAll();
                throw th;
            }
        }
    }

    public final void destroy() {
        throwIfCalledOnMainThread();
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            unbindLocked();
            this.mDestroyed = true;
            this.mCanUnbind = false;
        }
    }

    public void dump(com.android.internal.util.dump.DualDumpOutputStream dumpStream) {
        synchronized (this.mLock) {
            dumpStream.write("is_destroyed", 1133871366145L, this.mDestroyed);
            dumpStream.write("is_bound", 1133871366146L, this.mRemoteInstance != null);
        }
        try {
            if (dumpStream.isProto()) {
                dumpStream.write((java.lang.String) null, 1146756268035L, com.android.internal.os.TransferPipe.dumpAsync(getRemoteInstanceLazy().asBinder(), new java.lang.String[]{"--proto"}));
            } else {
                dumpStream.writeNested("internal_state", com.android.internal.os.TransferPipe.dumpAsync(getRemoteInstanceLazy().asBinder(), new java.lang.String[0]));
            }
        } catch (android.os.RemoteException | java.io.IOException | java.lang.InterruptedException | java.util.concurrent.TimeoutException e) {
            android.util.Slog.e(LOG_TAG, "Failed to dump remote instance", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAllPrintJobsHandled() {
        synchronized (this.mLock) {
            throwIfDestroyedLocked();
            unbindLocked();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onPrintJobStateChanged(android.print.PrintJobInfo printJob) {
        this.mCallbacks.onPrintJobStateChanged(printJob);
    }

    private android.print.IPrintSpooler getRemoteInstanceLazy() throws java.lang.InterruptedException, java.util.concurrent.TimeoutException {
        synchronized (this.mLock) {
            if (this.mRemoteInstance != null) {
                return this.mRemoteInstance;
            }
            bindLocked();
            return this.mRemoteInstance;
        }
    }

    private void bindLocked() throws java.lang.InterruptedException, java.util.concurrent.TimeoutException {
        int flags;
        while (this.mIsBinding) {
            this.mLock.wait();
        }
        if (this.mRemoteInstance != null) {
            return;
        }
        this.mIsBinding = true;
        try {
            if (this.mIsLowPriority) {
                flags = 1;
            } else {
                flags = android.hardware.audio.common.V2_0.AudioFormat.AAC_MAIN;
            }
            this.mContext.bindServiceAsUser(this.mIntent, this.mServiceConnection, flags, this.mUserHandle);
            long startMillis = android.os.SystemClock.uptimeMillis();
            while (this.mRemoteInstance == null) {
                long elapsedMillis = android.os.SystemClock.uptimeMillis() - startMillis;
                long remainingMillis = BIND_SPOOLER_SERVICE_TIMEOUT - elapsedMillis;
                if (remainingMillis <= 0) {
                    throw new java.util.concurrent.TimeoutException("Cannot get spooler!");
                }
                this.mLock.wait(remainingMillis);
            }
            this.mCanUnbind = true;
        } finally {
            this.mIsBinding = false;
            this.mLock.notifyAll();
        }
    }

    private void unbindLocked() {
        if (this.mRemoteInstance == null) {
            return;
        }
        while (!this.mCanUnbind) {
            try {
                this.mLock.wait();
            } catch (java.lang.InterruptedException e) {
            }
        }
        clearClientLocked();
        this.mRemoteInstance = null;
        this.mContext.unbindService(this.mServiceConnection);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setClientLocked() {
        try {
            if (this.mRemoteInstance != null) {
                this.mRemoteInstance.setClient(this.mClient);
            }
        } catch (android.os.RemoteException re) {
            android.util.Slog.d(LOG_TAG, "Error setting print spooler client", re);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void clearClientLocked() {
        try {
            this.mRemoteInstance.setClient((android.print.IPrintSpoolerClient) null);
        } catch (android.os.RemoteException re) {
            android.util.Slog.d(LOG_TAG, "Error clearing print spooler client", re);
        }
    }

    private void throwIfDestroyedLocked() {
        if (this.mDestroyed) {
            throw new java.lang.IllegalStateException("Cannot interact with a destroyed instance.");
        }
    }

    private void throwIfCalledOnMainThread() {
        if (java.lang.Thread.currentThread() == this.mContext.getMainLooper().getThread()) {
            throw new java.lang.RuntimeException("Cannot invoke on the main thread");
        }
    }

    private final class MyServiceConnection implements android.content.ServiceConnection {
        private MyServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            synchronized (com.android.server.print.RemotePrintSpooler.this.mLock) {
                com.android.server.print.RemotePrintSpooler.this.mRemoteInstance = android.print.IPrintSpooler.Stub.asInterface(service);
                com.android.server.print.RemotePrintSpooler.this.setClientLocked();
                com.android.server.print.RemotePrintSpooler.this.mLock.notifyAll();
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            synchronized (com.android.server.print.RemotePrintSpooler.this.mLock) {
                if (com.android.server.print.RemotePrintSpooler.this.mRemoteInstance != null) {
                    com.android.server.print.RemotePrintSpooler.this.clearClientLocked();
                    com.android.server.print.RemotePrintSpooler.this.mRemoteInstance = null;
                }
            }
        }
    }

    private static final class GetPrintJobInfosCaller extends android.util.TimedRemoteCaller<java.util.List<android.print.PrintJobInfo>> {
        private final android.print.IPrintSpoolerCallbacks mCallback;

        public GetPrintJobInfosCaller() {
            super(5000L);
            this.mCallback = new com.android.server.print.RemotePrintSpooler.BasePrintSpoolerServiceCallbacks() { // from class: com.android.server.print.RemotePrintSpooler.GetPrintJobInfosCaller.1
                @Override // com.android.server.print.RemotePrintSpooler.BasePrintSpoolerServiceCallbacks
                public void onGetPrintJobInfosResult(java.util.List<android.print.PrintJobInfo> printJobs, int sequence) {
                    com.android.server.print.RemotePrintSpooler.GetPrintJobInfosCaller.this.onRemoteMethodResult(printJobs, sequence);
                }
            };
        }

        public java.util.List<android.print.PrintJobInfo> getPrintJobInfos(android.print.IPrintSpooler target, android.content.ComponentName componentName, int state, int appId) throws java.util.concurrent.TimeoutException, android.os.RemoteException {
            int sequence = onBeforeRemoteCall();
            target.getPrintJobInfos(this.mCallback, componentName, state, appId, sequence);
            return (java.util.List) getResultTimed(sequence);
        }
    }

    private static final class GetPrintJobInfoCaller extends android.util.TimedRemoteCaller<android.print.PrintJobInfo> {
        private final android.print.IPrintSpoolerCallbacks mCallback;

        public GetPrintJobInfoCaller() {
            super(5000L);
            this.mCallback = new com.android.server.print.RemotePrintSpooler.BasePrintSpoolerServiceCallbacks() { // from class: com.android.server.print.RemotePrintSpooler.GetPrintJobInfoCaller.1
                @Override // com.android.server.print.RemotePrintSpooler.BasePrintSpoolerServiceCallbacks
                public void onGetPrintJobInfoResult(android.print.PrintJobInfo printJob, int sequence) {
                    com.android.server.print.RemotePrintSpooler.GetPrintJobInfoCaller.this.onRemoteMethodResult(printJob, sequence);
                }
            };
        }

        public android.print.PrintJobInfo getPrintJobInfo(android.print.IPrintSpooler target, android.print.PrintJobId printJobId, int appId) throws java.util.concurrent.TimeoutException, android.os.RemoteException {
            int sequence = onBeforeRemoteCall();
            target.getPrintJobInfo(printJobId, this.mCallback, appId, sequence);
            return (android.print.PrintJobInfo) getResultTimed(sequence);
        }
    }

    private static final class SetPrintJobStateCaller extends android.util.TimedRemoteCaller<java.lang.Boolean> {
        private final android.print.IPrintSpoolerCallbacks mCallback;

        public SetPrintJobStateCaller() {
            super(5000L);
            this.mCallback = new com.android.server.print.RemotePrintSpooler.BasePrintSpoolerServiceCallbacks() { // from class: com.android.server.print.RemotePrintSpooler.SetPrintJobStateCaller.1
                @Override // com.android.server.print.RemotePrintSpooler.BasePrintSpoolerServiceCallbacks
                public void onSetPrintJobStateResult(boolean success, int sequence) {
                    com.android.server.print.RemotePrintSpooler.SetPrintJobStateCaller.this.onRemoteMethodResult(java.lang.Boolean.valueOf(success), sequence);
                }
            };
        }

        public boolean setPrintJobState(android.print.IPrintSpooler target, android.print.PrintJobId printJobId, int status, java.lang.String error) throws java.util.concurrent.TimeoutException, android.os.RemoteException {
            int sequence = onBeforeRemoteCall();
            target.setPrintJobState(printJobId, status, error, this.mCallback, sequence);
            return ((java.lang.Boolean) getResultTimed(sequence)).booleanValue();
        }
    }

    private static final class SetPrintJobTagCaller extends android.util.TimedRemoteCaller<java.lang.Boolean> {
        private final android.print.IPrintSpoolerCallbacks mCallback;

        public SetPrintJobTagCaller() {
            super(5000L);
            this.mCallback = new com.android.server.print.RemotePrintSpooler.BasePrintSpoolerServiceCallbacks() { // from class: com.android.server.print.RemotePrintSpooler.SetPrintJobTagCaller.1
                @Override // com.android.server.print.RemotePrintSpooler.BasePrintSpoolerServiceCallbacks
                public void onSetPrintJobTagResult(boolean success, int sequence) {
                    com.android.server.print.RemotePrintSpooler.SetPrintJobTagCaller.this.onRemoteMethodResult(java.lang.Boolean.valueOf(success), sequence);
                }
            };
        }

        public boolean setPrintJobTag(android.print.IPrintSpooler target, android.print.PrintJobId printJobId, java.lang.String tag) throws java.util.concurrent.TimeoutException, android.os.RemoteException {
            int sequence = onBeforeRemoteCall();
            target.setPrintJobTag(printJobId, tag, this.mCallback, sequence);
            return ((java.lang.Boolean) getResultTimed(sequence)).booleanValue();
        }
    }

    private static final class OnCustomPrinterIconLoadedCaller extends android.util.TimedRemoteCaller<java.lang.Void> {
        private final android.print.IPrintSpoolerCallbacks mCallback;

        public OnCustomPrinterIconLoadedCaller() {
            super(5000L);
            this.mCallback = new com.android.server.print.RemotePrintSpooler.BasePrintSpoolerServiceCallbacks() { // from class: com.android.server.print.RemotePrintSpooler.OnCustomPrinterIconLoadedCaller.1
                @Override // com.android.server.print.RemotePrintSpooler.BasePrintSpoolerServiceCallbacks
                public void onCustomPrinterIconCached(int sequence) {
                    com.android.server.print.RemotePrintSpooler.OnCustomPrinterIconLoadedCaller.this.onRemoteMethodResult(null, sequence);
                }
            };
        }

        public java.lang.Void onCustomPrinterIconLoaded(android.print.IPrintSpooler target, android.print.PrinterId printerId, android.graphics.drawable.Icon icon) throws java.util.concurrent.TimeoutException, android.os.RemoteException {
            int sequence = onBeforeRemoteCall();
            target.onCustomPrinterIconLoaded(printerId, icon, this.mCallback, sequence);
            return (java.lang.Void) getResultTimed(sequence);
        }
    }

    private static final class ClearCustomPrinterIconCacheCaller extends android.util.TimedRemoteCaller<java.lang.Void> {
        private final android.print.IPrintSpoolerCallbacks mCallback;

        public ClearCustomPrinterIconCacheCaller() {
            super(5000L);
            this.mCallback = new com.android.server.print.RemotePrintSpooler.BasePrintSpoolerServiceCallbacks() { // from class: com.android.server.print.RemotePrintSpooler.ClearCustomPrinterIconCacheCaller.1
                @Override // com.android.server.print.RemotePrintSpooler.BasePrintSpoolerServiceCallbacks
                public void customPrinterIconCacheCleared(int sequence) {
                    com.android.server.print.RemotePrintSpooler.ClearCustomPrinterIconCacheCaller.this.onRemoteMethodResult(null, sequence);
                }
            };
        }

        public java.lang.Void clearCustomPrinterIconCache(android.print.IPrintSpooler target) throws java.util.concurrent.TimeoutException, android.os.RemoteException {
            int sequence = onBeforeRemoteCall();
            target.clearCustomPrinterIconCache(this.mCallback, sequence);
            return (java.lang.Void) getResultTimed(sequence);
        }
    }

    private static final class GetCustomPrinterIconCaller extends android.util.TimedRemoteCaller<android.graphics.drawable.Icon> {
        private final android.print.IPrintSpoolerCallbacks mCallback;

        public GetCustomPrinterIconCaller() {
            super(5000L);
            this.mCallback = new com.android.server.print.RemotePrintSpooler.BasePrintSpoolerServiceCallbacks() { // from class: com.android.server.print.RemotePrintSpooler.GetCustomPrinterIconCaller.1
                @Override // com.android.server.print.RemotePrintSpooler.BasePrintSpoolerServiceCallbacks
                public void onGetCustomPrinterIconResult(android.graphics.drawable.Icon icon, int sequence) {
                    com.android.server.print.RemotePrintSpooler.GetCustomPrinterIconCaller.this.onRemoteMethodResult(icon, sequence);
                }
            };
        }

        public android.graphics.drawable.Icon getCustomPrinterIcon(android.print.IPrintSpooler target, android.print.PrinterId printerId) throws java.util.concurrent.TimeoutException, android.os.RemoteException {
            int sequence = onBeforeRemoteCall();
            target.getCustomPrinterIcon(printerId, this.mCallback, sequence);
            return (android.graphics.drawable.Icon) getResultTimed(sequence);
        }
    }

    private static abstract class BasePrintSpoolerServiceCallbacks extends android.print.IPrintSpoolerCallbacks.Stub {
        private BasePrintSpoolerServiceCallbacks() {
        }

        public void onGetPrintJobInfosResult(java.util.List<android.print.PrintJobInfo> printJobIds, int sequence) {
        }

        public void onGetPrintJobInfoResult(android.print.PrintJobInfo printJob, int sequence) {
        }

        public void onCancelPrintJobResult(boolean canceled, int sequence) {
        }

        public void onSetPrintJobStateResult(boolean success, int sequece) {
        }

        public void onSetPrintJobTagResult(boolean success, int sequence) {
        }

        public void onCustomPrinterIconCached(int sequence) {
        }

        public void onGetCustomPrinterIconResult(android.graphics.drawable.Icon icon, int sequence) {
        }

        public void customPrinterIconCacheCleared(int sequence) {
        }
    }

    private static final class PrintSpoolerClient extends android.print.IPrintSpoolerClient.Stub {
        private final java.lang.ref.WeakReference<com.android.server.print.RemotePrintSpooler> mWeakSpooler;

        public PrintSpoolerClient(com.android.server.print.RemotePrintSpooler spooler) {
            this.mWeakSpooler = new java.lang.ref.WeakReference<>(spooler);
        }

        public void onPrintJobQueued(android.print.PrintJobInfo printJob) {
            com.android.server.print.RemotePrintSpooler spooler = this.mWeakSpooler.get();
            if (spooler != null) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    spooler.mCallbacks.onPrintJobQueued(printJob);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void onAllPrintJobsForServiceHandled(android.content.ComponentName printService) {
            com.android.server.print.RemotePrintSpooler spooler = this.mWeakSpooler.get();
            if (spooler != null) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    spooler.mCallbacks.onAllPrintJobsForServiceHandled(printService);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void onAllPrintJobsHandled() {
            com.android.server.print.RemotePrintSpooler spooler = this.mWeakSpooler.get();
            if (spooler != null) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    spooler.onAllPrintJobsHandled();
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }

        public void onPrintJobStateChanged(android.print.PrintJobInfo printJob) {
            com.android.server.print.RemotePrintSpooler spooler = this.mWeakSpooler.get();
            if (spooler != null) {
                long identity = android.os.Binder.clearCallingIdentity();
                try {
                    spooler.onPrintJobStateChanged(printJob);
                } finally {
                    android.os.Binder.restoreCallingIdentity(identity);
                }
            }
        }
    }
}
