package com.android.server.broadcastradio.aidl;

/* JADX INFO: loaded from: classes.dex */
public final class AnnouncementAggregator extends android.hardware.radio.ICloseHandle.Stub {
    private boolean mIsClosed;
    private final android.hardware.radio.IAnnouncementListener mListener;
    private final java.lang.Object mLock;
    private static final java.lang.String TAG = "BcRadioAidlSrv.AnnAggr";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);
    private final android.os.IBinder.DeathRecipient mDeathRecipient = new com.android.server.broadcastradio.aidl.AnnouncementAggregator.DeathRecipient();
    private final java.util.List<com.android.server.broadcastradio.aidl.AnnouncementAggregator.ModuleWatcher> mModuleWatchers = new java.util.ArrayList();

    public AnnouncementAggregator(android.hardware.radio.IAnnouncementListener listener, java.lang.Object lock) {
        this.mListener = (android.hardware.radio.IAnnouncementListener) java.util.Objects.requireNonNull(listener, "listener cannot be null");
        this.mLock = java.util.Objects.requireNonNull(lock, "lock cannot be null");
        try {
            listener.asBinder().linkToDeath(this.mDeathRecipient, 0);
        } catch (android.os.RemoteException ex) {
            ex.rethrowFromSystemServer();
        }
    }

    private final class ModuleWatcher extends android.hardware.radio.IAnnouncementListener.Stub {
        private android.hardware.radio.ICloseHandle mCloseHandle;
        public java.util.List<android.hardware.radio.Announcement> mCurrentList;

        private ModuleWatcher() {
            this.mCurrentList = new java.util.ArrayList();
        }

        public void onListUpdated(java.util.List<android.hardware.radio.Announcement> active) {
            if (com.android.server.broadcastradio.aidl.AnnouncementAggregator.DEBUG) {
                com.android.server.utils.Slogf.d(com.android.server.broadcastradio.aidl.AnnouncementAggregator.TAG, "onListUpdate for %s", active);
            }
            this.mCurrentList = (java.util.List) java.util.Objects.requireNonNull(active, "active cannot be null");
            com.android.server.broadcastradio.aidl.AnnouncementAggregator.this.onListUpdated();
        }

        public void setCloseHandle(android.hardware.radio.ICloseHandle closeHandle) {
            if (com.android.server.broadcastradio.aidl.AnnouncementAggregator.DEBUG) {
                com.android.server.utils.Slogf.d(com.android.server.broadcastradio.aidl.AnnouncementAggregator.TAG, "Set close handle %s", closeHandle);
            }
            this.mCloseHandle = (android.hardware.radio.ICloseHandle) java.util.Objects.requireNonNull(closeHandle, "closeHandle cannot be null");
        }

        public void close() throws android.os.RemoteException {
            if (com.android.server.broadcastradio.aidl.AnnouncementAggregator.DEBUG) {
                com.android.server.utils.Slogf.d(com.android.server.broadcastradio.aidl.AnnouncementAggregator.TAG, "Close module watcher.");
            }
            if (this.mCloseHandle != null) {
                this.mCloseHandle.close();
            }
        }

        public void dumpInfo(android.util.IndentingPrintWriter pw) {
            pw.printf("ModuleWatcher:\n", new java.lang.Object[0]);
            pw.increaseIndent();
            pw.printf("Close handle: %s\n", new java.lang.Object[]{this.mCloseHandle});
            pw.printf("Current announcement list: %s\n", new java.lang.Object[]{this.mCurrentList});
            pw.decreaseIndent();
        }
    }

    private class DeathRecipient implements android.os.IBinder.DeathRecipient {
        private DeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            try {
                com.android.server.broadcastradio.aidl.AnnouncementAggregator.this.close();
            } catch (android.os.RemoteException ex) {
                com.android.server.utils.Slogf.e(com.android.server.broadcastradio.aidl.AnnouncementAggregator.TAG, ex, "Cannot close Announcement aggregator for DeathRecipient", new java.lang.Object[0]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onListUpdated() {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "onListUpdated()");
        }
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                com.android.server.utils.Slogf.e(TAG, "Announcement aggregator is closed, it shouldn't receive callbacks");
                return;
            }
            java.util.List<android.hardware.radio.Announcement> combined = new java.util.ArrayList<>(this.mModuleWatchers.size());
            for (int i = 0; i < this.mModuleWatchers.size(); i++) {
                combined.addAll(this.mModuleWatchers.get(i).mCurrentList);
            }
            try {
                this.mListener.onListUpdated(combined);
            } catch (android.os.RemoteException ex) {
                com.android.server.utils.Slogf.e(TAG, ex, "mListener.onListUpdated() failed", new java.lang.Object[0]);
            }
        }
    }

    public void watchModule(com.android.server.broadcastradio.aidl.RadioModule radioModule, int[] enabledTypes) {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "Watch module for %s with enabled types %s", radioModule, java.util.Arrays.toString(enabledTypes));
        }
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                throw new java.lang.IllegalStateException("Failed to watch modulesince announcement aggregator has already been closed");
            }
            com.android.server.broadcastradio.aidl.AnnouncementAggregator.ModuleWatcher watcher = new com.android.server.broadcastradio.aidl.AnnouncementAggregator.ModuleWatcher();
            try {
                android.hardware.radio.ICloseHandle closeHandle = radioModule.addAnnouncementListener(watcher, enabledTypes);
                watcher.setCloseHandle(closeHandle);
                this.mModuleWatchers.add(watcher);
            } catch (android.os.RemoteException ex) {
                com.android.server.utils.Slogf.e(TAG, ex, "Failed to add announcement listener", new java.lang.Object[0]);
            }
        }
    }

    public void close() throws android.os.RemoteException {
        if (DEBUG) {
            com.android.server.utils.Slogf.d(TAG, "Close watchModule");
        }
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                com.android.server.utils.Slogf.w(TAG, "Announcement aggregator has already been closed.");
                return;
            }
            this.mListener.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
            for (int i = 0; i < this.mModuleWatchers.size(); i++) {
                com.android.server.broadcastradio.aidl.AnnouncementAggregator.ModuleWatcher moduleWatcher = this.mModuleWatchers.get(i);
                try {
                    moduleWatcher.close();
                } catch (java.lang.Exception e) {
                    com.android.server.utils.Slogf.e(TAG, "Failed to close module watcher %s: %s", moduleWatcher, e);
                }
            }
            this.mModuleWatchers.clear();
            this.mIsClosed = true;
        }
    }

    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter printWriter, java.lang.String[] args) {
        android.util.IndentingPrintWriter announcementPrintWriter = new android.util.IndentingPrintWriter(printWriter);
        announcementPrintWriter.printf("AnnouncementAggregator\n", new java.lang.Object[0]);
        announcementPrintWriter.increaseIndent();
        synchronized (this.mLock) {
            announcementPrintWriter.printf("Is session closed? %s\n", new java.lang.Object[]{this.mIsClosed ? "Yes" : "No"});
            announcementPrintWriter.printf("Module Watchers [%d]:\n", new java.lang.Object[]{java.lang.Integer.valueOf(this.mModuleWatchers.size())});
            announcementPrintWriter.increaseIndent();
            for (int i = 0; i < this.mModuleWatchers.size(); i++) {
                this.mModuleWatchers.get(i).dumpInfo(announcementPrintWriter);
            }
            announcementPrintWriter.decreaseIndent();
        }
        announcementPrintWriter.decreaseIndent();
    }
}
