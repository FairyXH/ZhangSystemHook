package com.android.server.location.contexthub;

/* JADX INFO: loaded from: classes2.dex */
class NanoAppStateManager {
    private static final boolean ENABLE_LOG_DEBUG = true;
    private static final java.lang.String TAG = "NanoAppStateManager";
    private final java.util.HashMap<java.lang.Integer, android.hardware.location.NanoAppInstanceInfo> mNanoAppHash = new java.util.HashMap<>();
    private int mNextHandle = 0;

    NanoAppStateManager() {
    }

    synchronized android.hardware.location.NanoAppInstanceInfo getNanoAppInstanceInfo(int nanoAppHandle) {
        return this.mNanoAppHash.get(java.lang.Integer.valueOf(nanoAppHandle));
    }

    synchronized void foreachNanoAppInstanceInfo(java.util.function.Consumer<android.hardware.location.NanoAppInstanceInfo> consumer) {
        for (android.hardware.location.NanoAppInstanceInfo info : this.mNanoAppHash.values()) {
            consumer.accept(info);
        }
    }

    synchronized int getNanoAppHandle(int contextHubId, long nanoAppId) {
        for (android.hardware.location.NanoAppInstanceInfo info : this.mNanoAppHash.values()) {
            if (info.getContexthubId() == contextHubId && info.getAppId() == nanoAppId) {
                return info.getHandle();
            }
        }
        return -1;
    }

    synchronized void addNanoAppInstance(int contextHubId, long nanoAppId, int nanoAppVersion) {
        removeNanoAppInstance(contextHubId, nanoAppId);
        if (this.mNanoAppHash.size() == Integer.MAX_VALUE) {
            android.util.Log.e(TAG, "Error adding nanoapp instance: max limit exceeded");
            return;
        }
        int nanoAppHandle = this.mNextHandle;
        int i = 0;
        while (true) {
            if (i > Integer.MAX_VALUE) {
                break;
            }
            int i2 = 0;
            if (!this.mNanoAppHash.containsKey(java.lang.Integer.valueOf(nanoAppHandle))) {
                this.mNanoAppHash.put(java.lang.Integer.valueOf(nanoAppHandle), new android.hardware.location.NanoAppInstanceInfo(nanoAppHandle, nanoAppId, nanoAppVersion, contextHubId));
                if (nanoAppHandle != Integer.MAX_VALUE) {
                    i2 = nanoAppHandle + 1;
                }
                this.mNextHandle = i2;
            } else {
                if (nanoAppHandle != Integer.MAX_VALUE) {
                    i2 = nanoAppHandle + 1;
                }
                nanoAppHandle = i2;
                i++;
            }
        }
        android.util.Log.v(TAG, "Added app instance with handle " + nanoAppHandle + " to hub " + contextHubId + ": ID=0x" + java.lang.Long.toHexString(nanoAppId) + ", version=0x" + java.lang.Integer.toHexString(nanoAppVersion));
    }

    synchronized void removeNanoAppInstance(int contextHubId, long nanoAppId) {
        int nanoAppHandle = getNanoAppHandle(contextHubId, nanoAppId);
        this.mNanoAppHash.remove(java.lang.Integer.valueOf(nanoAppHandle));
    }

    synchronized void updateCache(int contextHubId, java.util.List<android.hardware.location.NanoAppState> nanoappStateList) {
        java.util.HashSet<java.lang.Long> nanoAppIdSet = new java.util.HashSet<>();
        for (android.hardware.location.NanoAppState nanoappState : nanoappStateList) {
            handleQueryAppEntry(contextHubId, nanoappState.getNanoAppId(), (int) nanoappState.getNanoAppVersion());
            nanoAppIdSet.add(java.lang.Long.valueOf(nanoappState.getNanoAppId()));
        }
        java.util.Iterator<android.hardware.location.NanoAppInstanceInfo> iterator = this.mNanoAppHash.values().iterator();
        while (iterator.hasNext()) {
            android.hardware.location.NanoAppInstanceInfo info = iterator.next();
            if (info.getContexthubId() == contextHubId && !nanoAppIdSet.contains(java.lang.Long.valueOf(info.getAppId()))) {
                iterator.remove();
            }
        }
    }

    private void handleQueryAppEntry(int contextHubId, long nanoAppId, int nanoAppVersion) {
        int nanoAppHandle = getNanoAppHandle(contextHubId, nanoAppId);
        if (nanoAppHandle == -1) {
            addNanoAppInstance(contextHubId, nanoAppId, nanoAppVersion);
            return;
        }
        android.hardware.location.NanoAppInstanceInfo info = this.mNanoAppHash.get(java.lang.Integer.valueOf(nanoAppHandle));
        if (info.getAppVersion() != nanoAppVersion) {
            this.mNanoAppHash.put(java.lang.Integer.valueOf(nanoAppHandle), new android.hardware.location.NanoAppInstanceInfo(nanoAppHandle, nanoAppId, nanoAppVersion, contextHubId));
            android.util.Log.v(TAG, "Updated app instance with handle " + nanoAppHandle + " at hub " + contextHubId + ": ID=0x" + java.lang.Long.toHexString(nanoAppId) + ", version=0x" + java.lang.Integer.toHexString(nanoAppVersion));
        }
    }
}
