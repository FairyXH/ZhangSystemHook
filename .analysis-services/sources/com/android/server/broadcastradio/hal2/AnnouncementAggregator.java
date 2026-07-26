package com.android.server.broadcastradio.hal2;

/* JADX INFO: loaded from: classes.dex */
public final class AnnouncementAggregator extends android.hardware.radio.ICloseHandle.Stub {
    private static final java.lang.String TAG = "BcRadio2Srv.AnnAggr";
    private final android.hardware.radio.IAnnouncementListener mListener;
    private final java.lang.Object mLock;
    private final android.os.IBinder.DeathRecipient mDeathRecipient = new com.android.server.broadcastradio.hal2.AnnouncementAggregator.DeathRecipient();
    private final java.util.Collection<com.android.server.broadcastradio.hal2.AnnouncementAggregator.ModuleWatcher> mModuleWatchers = new java.util.ArrayList();
    private boolean mIsClosed = false;

    public AnnouncementAggregator(android.hardware.radio.IAnnouncementListener listener, java.lang.Object lock) {
        this.mListener = (android.hardware.radio.IAnnouncementListener) java.util.Objects.requireNonNull(listener);
        this.mLock = java.util.Objects.requireNonNull(lock);
        try {
            listener.asBinder().linkToDeath(this.mDeathRecipient, 0);
        } catch (android.os.RemoteException ex) {
            ex.rethrowFromSystemServer();
        }
    }

    private class ModuleWatcher extends android.hardware.radio.IAnnouncementListener.Stub {
        public java.util.List<android.hardware.radio.Announcement> currentList;
        private android.hardware.radio.ICloseHandle mCloseHandle;

        private ModuleWatcher() {
            this.currentList = new java.util.ArrayList();
        }

        public void onListUpdated(java.util.List<android.hardware.radio.Announcement> active) {
            this.currentList = (java.util.List) java.util.Objects.requireNonNull(active);
            com.android.server.broadcastradio.hal2.AnnouncementAggregator.this.onListUpdated();
        }

        public void setCloseHandle(android.hardware.radio.ICloseHandle closeHandle) {
            this.mCloseHandle = (android.hardware.radio.ICloseHandle) java.util.Objects.requireNonNull(closeHandle);
        }

        public void close() throws android.os.RemoteException {
            if (this.mCloseHandle != null) {
                this.mCloseHandle.close();
            }
        }
    }

    private class DeathRecipient implements android.os.IBinder.DeathRecipient {
        private DeathRecipient() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            try {
                com.android.server.broadcastradio.hal2.AnnouncementAggregator.this.close();
            } catch (android.os.RemoteException ex) {
                com.android.server.utils.Slogf.e(com.android.server.broadcastradio.hal2.AnnouncementAggregator.TAG, ex, "Cannot close Announcement aggregator for DeathRecipient", new java.lang.Object[0]);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onListUpdated() {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                com.android.server.utils.Slogf.e(TAG, "Announcement aggregator is closed, it shouldn't receive callbacks");
                return;
            }
            java.util.List<android.hardware.radio.Announcement> combined = new java.util.ArrayList<>();
            for (com.android.server.broadcastradio.hal2.AnnouncementAggregator.ModuleWatcher watcher : this.mModuleWatchers) {
                combined.addAll(watcher.currentList);
            }
            try {
                this.mListener.onListUpdated(combined);
            } catch (android.os.RemoteException ex) {
                com.android.server.utils.Slogf.e(TAG, "mListener.onListUpdated() failed: ", ex);
            }
        }
    }

    public void watchModule(com.android.server.broadcastradio.hal2.RadioModule module, int[] enabledTypes) {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                throw new java.lang.IllegalStateException("Failed to watch modulesince announcement aggregator has already been closed");
            }
            com.android.server.broadcastradio.hal2.AnnouncementAggregator.ModuleWatcher watcher = new com.android.server.broadcastradio.hal2.AnnouncementAggregator.ModuleWatcher();
            try {
                android.hardware.radio.ICloseHandle closeHandle = module.addAnnouncementListener(enabledTypes, watcher);
                watcher.setCloseHandle(closeHandle);
                this.mModuleWatchers.add(watcher);
            } catch (android.os.RemoteException ex) {
                com.android.server.utils.Slogf.e(TAG, "Failed to add announcement listener", ex);
            }
        }
    }

    public void close() throws android.os.RemoteException {
        synchronized (this.mLock) {
            if (this.mIsClosed) {
                return;
            }
            this.mIsClosed = true;
            this.mListener.asBinder().unlinkToDeath(this.mDeathRecipient, 0);
            for (com.android.server.broadcastradio.hal2.AnnouncementAggregator.ModuleWatcher watcher : this.mModuleWatchers) {
                watcher.close();
            }
            this.mModuleWatchers.clear();
        }
    }
}
