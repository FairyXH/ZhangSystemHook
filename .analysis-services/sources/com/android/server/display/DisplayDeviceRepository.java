package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
class DisplayDeviceRepository implements com.android.server.display.DisplayAdapter.Listener {
    public static final int DISPLAY_DEVICE_EVENT_ADDED = 1;
    public static final int DISPLAY_DEVICE_EVENT_REMOVED = 3;
    private final com.android.server.display.PersistentDataStore mPersistentDataStore;
    private final com.android.server.display.DisplayManagerService.SyncRoot mSyncRoot;
    private static final java.lang.String TAG = "DisplayDeviceRepository";
    private static final boolean DEBUG = com.android.server.display.utils.DebugUtils.isDebuggable(TAG);
    private final java.util.List<com.android.server.display.DisplayDevice> mDisplayDevices = new java.util.ArrayList();
    private final java.util.List<com.android.server.display.DisplayDeviceRepository.Listener> mListeners = new java.util.ArrayList();
    private com.android.server.display.IDisplayDeviceRepositoryExt mDisplayDeviceRepositoryExtImpl = (com.android.server.display.IDisplayDeviceRepositoryExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IDisplayDeviceRepositoryExt.class).base(this).create();

    public interface Listener {
        void onDisplayDeviceChangedLocked(com.android.server.display.DisplayDevice displayDevice, int i);

        void onDisplayDeviceEventLocked(com.android.server.display.DisplayDevice displayDevice, int i);

        void onTraversalRequested();
    }

    DisplayDeviceRepository(com.android.server.display.DisplayManagerService.SyncRoot syncRoot, com.android.server.display.PersistentDataStore persistentDataStore) {
        this.mSyncRoot = syncRoot;
        this.mPersistentDataStore = persistentDataStore;
    }

    public void addListener(com.android.server.display.DisplayDeviceRepository.Listener listener) {
        this.mListeners.add(listener);
    }

    @Override // com.android.server.display.DisplayAdapter.Listener
    public void onDisplayDeviceEvent(com.android.server.display.DisplayDevice device, int event) {
        java.lang.String tag = null;
        if (DEBUG) {
            tag = "DisplayDeviceRepository#onDisplayDeviceEvent (event=" + event + ")";
            android.os.Trace.beginAsyncSection(tag, 0);
        }
        switch (event) {
            case 1:
                handleDisplayDeviceAdded(device);
                break;
            case 2:
                handleDisplayDeviceChanged(device);
                break;
            case 3:
                handleDisplayDeviceRemoved(device);
                break;
        }
        if (DEBUG) {
            android.os.Trace.endAsyncSection(tag, 0);
        }
    }

    @Override // com.android.server.display.DisplayAdapter.Listener
    public void onDisplayDeviceEvent(com.android.server.display.DisplayDevice device, int event, long now, long timestamp) {
        this.mDisplayDeviceRepositoryExtImpl.onDisplayDeviceEvent(device, event, now, timestamp);
    }

    @Override // com.android.server.display.DisplayAdapter.Listener
    public void onTraversalRequested() {
        int size = this.mListeners.size();
        for (int i = 0; i < size; i++) {
            this.mListeners.get(i).onTraversalRequested();
        }
    }

    public boolean containsLocked(com.android.server.display.DisplayDevice d) {
        return this.mDisplayDevices.contains(d);
    }

    public int sizeLocked() {
        return this.mDisplayDevices.size();
    }

    public void forEachLocked(java.util.function.Consumer<com.android.server.display.DisplayDevice> consumer) {
        int count = this.mDisplayDevices.size();
        for (int i = 0; i < count; i++) {
            consumer.accept(this.mDisplayDevices.get(i));
        }
    }

    public com.android.server.display.DisplayDevice getByAddressLocked(android.view.DisplayAddress address) {
        for (int i = this.mDisplayDevices.size() - 1; i >= 0; i--) {
            com.android.server.display.DisplayDevice device = this.mDisplayDevices.get(i);
            com.android.server.display.DisplayDeviceInfo info = device.getDisplayDeviceInfoLocked();
            if (address.equals(info.address) || android.view.DisplayAddress.Physical.isPortMatch(address, info.address)) {
                return device;
            }
        }
        return null;
    }

    public com.android.server.display.DisplayDevice getByUniqueIdLocked(java.lang.String uniqueId) {
        for (int i = this.mDisplayDevices.size() - 1; i >= 0; i--) {
            com.android.server.display.DisplayDevice displayDevice = this.mDisplayDevices.get(i);
            if (displayDevice.getUniqueId().equals(uniqueId)) {
                return displayDevice;
            }
        }
        return null;
    }

    private void handleDisplayDeviceAdded(com.android.server.display.DisplayDevice device) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.DisplayDeviceInfo info = device.getDisplayDeviceInfoLocked();
            if (this.mDisplayDevices.contains(device)) {
                android.util.Slog.w(TAG, "Attempted to add already added display device: " + info);
                return;
            }
            android.util.Slog.i(TAG, "Display device added: " + info);
            if (info != null) {
                this.mDisplayDeviceRepositoryExtImpl.handleDisplayDeviceAdded(info.ownerPackageName, info.ownerUid);
            }
            if (info == null || info.ownerPackageName == null || !this.mDisplayDeviceRepositoryExtImpl.interceptDisplayDeviceAdded(this.mDisplayDevices, info)) {
                device.mDebugLastLoggedDeviceInfo = info;
                this.mDisplayDeviceRepositoryExtImpl.handleDisplayDeviceAdded(device);
                this.mDisplayDevices.add(device);
                sendEventLocked(device, 1);
            }
        }
    }

    private void handleDisplayDeviceChanged(com.android.server.display.DisplayDevice device) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.DisplayDeviceInfo info = device.getDisplayDeviceInfoLocked();
            if (!this.mDisplayDevices.contains(device)) {
                android.util.Slog.w(TAG, "Attempted to change non-existent display device: " + info);
                return;
            }
            if (DEBUG) {
                android.os.Trace.traceBegin(131072L, "handleDisplayDeviceChanged");
            }
            int diff = device.mDebugLastLoggedDeviceInfo.diff(info);
            if (diff == 2) {
                android.util.Slog.i(TAG, "Display device changed state: \"" + info.name + "\", " + android.view.Display.stateToString(info.state));
            } else if (diff == 32) {
                android.util.Slog.i(TAG, "Display device rotated: \"" + info.name + "\", " + android.view.Surface.rotationToString(info.rotation));
            } else if (diff == 192) {
                android.util.Slog.i(TAG, "Display device changed render timings: \"" + info.name + "\", renderFrameRate=" + info.renderFrameRate + ", presentationDeadlineNanos=" + info.presentationDeadlineNanos + ", appVsyncOffsetNanos=" + info.appVsyncOffsetNanos);
            } else if (diff == 4) {
                if (DEBUG) {
                    android.util.Slog.i(TAG, "Display device changed committed state: \"" + info.name + "\", " + android.view.Display.stateToString(info.committedState));
                }
            } else if (diff != 16) {
                android.util.Slog.i(TAG, "Display device changed: " + info);
            }
            if ((diff & 8) != 0) {
                try {
                    this.mPersistentDataStore.setColorMode(device, info.colorMode);
                    this.mPersistentDataStore.saveIfNeeded();
                } catch (java.lang.Throwable th) {
                    this.mPersistentDataStore.saveIfNeeded();
                    throw th;
                }
            }
            this.mDisplayDeviceRepositoryExtImpl.handleDisplayDeviceChanged(device, diff, device.mDebugLastLoggedDeviceInfo);
            device.mDebugLastLoggedDeviceInfo = info;
            device.applyPendingDisplayDeviceInfoChangesLocked();
            sendChangedEventLocked(device, diff);
            if (info != null && info.state == 1) {
                this.mDisplayDeviceRepositoryExtImpl.handleDisplayDeviceRemoved(info.ownerPackageName, info.ownerUid);
            }
            if (DEBUG) {
                android.os.Trace.traceEnd(131072L);
            }
        }
    }

    private void handleDisplayDeviceRemoved(com.android.server.display.DisplayDevice device) {
        synchronized (this.mSyncRoot) {
            com.android.server.display.DisplayDeviceInfo info = device.getDisplayDeviceInfoLocked();
            if (!this.mDisplayDevices.remove(device)) {
                android.util.Slog.w(TAG, "Attempted to remove non-existent display device: " + info);
                return;
            }
            android.util.Slog.i(TAG, "Display device removed: " + info);
            this.mDisplayDeviceRepositoryExtImpl.onDisplayRemoved(device);
            if (info != null) {
                this.mDisplayDeviceRepositoryExtImpl.handleDisplayDeviceRemoved(info.ownerPackageName, info.ownerUid);
            }
            this.mDisplayDeviceRepositoryExtImpl.handleDisplayDeviceRemoved(device);
            device.mDebugLastLoggedDeviceInfo = info;
            sendEventLocked(device, 3);
        }
    }

    private void sendEventLocked(com.android.server.display.DisplayDevice device, int event) {
        int size = this.mListeners.size();
        for (int i = 0; i < size; i++) {
            this.mListeners.get(i).onDisplayDeviceEventLocked(device, event);
        }
    }

    private void sendChangedEventLocked(com.android.server.display.DisplayDevice device, int diff) {
        int size = this.mListeners.size();
        for (int i = 0; i < size; i++) {
            this.mListeners.get(i).onDisplayDeviceChangedLocked(device, diff);
        }
    }
}
