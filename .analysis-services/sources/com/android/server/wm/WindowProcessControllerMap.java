package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class WindowProcessControllerMap {
    private final android.util.SparseArray<com.android.server.wm.WindowProcessController> mPidMap = new android.util.SparseArray<>();
    private final java.util.Map<java.lang.Integer, android.util.ArraySet<com.android.server.wm.WindowProcessController>> mUidMap = new java.util.HashMap();

    WindowProcessControllerMap() {
    }

    com.android.server.wm.WindowProcessController getProcess(int pid) {
        return this.mPidMap.get(pid);
    }

    android.util.ArraySet<com.android.server.wm.WindowProcessController> getProcesses(int uid) {
        return this.mUidMap.get(java.lang.Integer.valueOf(uid));
    }

    android.util.SparseArray<com.android.server.wm.WindowProcessController> getPidMap() {
        return this.mPidMap;
    }

    void put(int pid, com.android.server.wm.WindowProcessController proc) {
        com.android.server.wm.WindowProcessController prevProc = this.mPidMap.get(pid);
        if (prevProc != null) {
            removeProcessFromUidMap(prevProc);
        }
        this.mPidMap.put(pid, proc);
        int uid = proc.mUid;
        android.util.ArraySet<com.android.server.wm.WindowProcessController> procSet = this.mUidMap.getOrDefault(java.lang.Integer.valueOf(uid), new android.util.ArraySet<>());
        procSet.add(proc);
        this.mUidMap.put(java.lang.Integer.valueOf(uid), procSet);
        proc.getWrapper().getExtImpl().shouldUpdateProcessConfig(proc, proc.getWrapper().getAtm());
    }

    void remove(int pid) {
        com.android.server.wm.WindowProcessController proc = this.mPidMap.get(pid);
        if (proc != null) {
            this.mPidMap.remove(pid);
            removeProcessFromUidMap(proc);
            proc.destroy();
        }
    }

    private void removeProcessFromUidMap(com.android.server.wm.WindowProcessController proc) {
        if (proc == null) {
            return;
        }
        int uid = proc.mUid;
        android.util.ArraySet<com.android.server.wm.WindowProcessController> procSet = this.mUidMap.get(java.lang.Integer.valueOf(uid));
        if (procSet != null) {
            procSet.remove(proc);
            if (procSet.isEmpty()) {
                this.mUidMap.remove(java.lang.Integer.valueOf(uid));
            }
        }
    }
}
