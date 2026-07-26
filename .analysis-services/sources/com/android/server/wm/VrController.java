package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
final class VrController {
    private static final int FLAG_NON_VR_MODE = 0;
    private static final int FLAG_PERSISTENT_VR_MODE = 2;
    private static final int FLAG_VR_MODE = 1;
    private static int[] ORIG_ENUMS = {0, 1, 2};
    private static int[] PROTO_ENUMS = {0, 1, 2};
    private static final java.lang.String TAG = "VrController";
    private final java.lang.Object mGlobalAmLock;
    com.android.server.vr.VrManagerInternal mVrService;
    private volatile int mVrState = 0;
    private int mVrRenderThreadTid = 0;
    private final android.service.vr.IPersistentVrStateCallbacks mPersistentVrModeListener = new android.service.vr.IPersistentVrStateCallbacks.Stub() { // from class: com.android.server.wm.VrController.1
        public void onPersistentVrStateChanged(boolean enabled) {
            synchronized (com.android.server.wm.VrController.this.mGlobalAmLock) {
                if (enabled) {
                    com.android.server.wm.VrController.this.setVrRenderThreadLocked(0, 3, true);
                    com.android.server.wm.VrController.this.mVrState |= 2;
                } else {
                    com.android.server.wm.VrController.this.setPersistentVrRenderThreadLocked(0, true);
                    com.android.server.wm.VrController.this.mVrState &= -3;
                }
            }
        }
    };

    public VrController(java.lang.Object globalAmLock) {
        this.mGlobalAmLock = globalAmLock;
    }

    public void onSystemReady() {
        com.android.server.vr.VrManagerInternal vrManagerInternal = (com.android.server.vr.VrManagerInternal) com.android.server.LocalServices.getService(com.android.server.vr.VrManagerInternal.class);
        if (vrManagerInternal != null) {
            this.mVrService = vrManagerInternal;
            vrManagerInternal.addPersistentVrModeStateListener(this.mPersistentVrModeListener);
        }
    }

    boolean isInterestingToSchedGroup() {
        return (this.mVrState & 3) != 0;
    }

    public void onTopProcChangedLocked(com.android.server.wm.WindowProcessController proc) {
        int curSchedGroup = proc.getCurrentSchedulingGroup();
        if (curSchedGroup == 3) {
            setVrRenderThreadLocked(proc.mVrThreadTid, curSchedGroup, true);
        } else if (proc.mVrThreadTid == this.mVrRenderThreadTid) {
            clearVrRenderThreadLocked(true);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find top splitter block for handler:B:24:0x003e
        	at jadx.core.utils.BlockUtils.getTopSplitterForHandler(BlockUtils.java:1182)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.collectHandlerRegions(ExcHandlersRegionMaker.java:53)
        	at jadx.core.dex.visitors.regions.maker.ExcHandlersRegionMaker.process(ExcHandlersRegionMaker.java:38)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:27)
        */
    public boolean onVrModeChanged(com.android.server.wm.ActivityRecord r12) {
        /*
            r11 = this;
            com.android.server.vr.VrManagerInternal r6 = r11.mVrService
            r0 = 0
            if (r6 != 0) goto L6
            return r0
        L6:
            r1 = -1
            r4 = 0
            java.lang.Object r7 = r11.mGlobalAmLock
            monitor-enter(r7)
            android.content.ComponentName r2 = r12.requestedVrComponent     // Catch: java.lang.Throwable -> L3e
            if (r2 == 0) goto L10
            r0 = 1
        L10:
            r8 = r0
            android.content.ComponentName r2 = r12.requestedVrComponent     // Catch: java.lang.Throwable -> L3e
            int r3 = r12.mUserId     // Catch: java.lang.Throwable -> L3e
            android.content.pm.ActivityInfo r0 = r12.info     // Catch: java.lang.Throwable -> L3e
            android.content.ComponentName r5 = r0.getComponentName()     // Catch: java.lang.Throwable -> L3e
            com.android.server.wm.WindowProcessController r0 = r12.app     // Catch: java.lang.Throwable -> L3e
            boolean r0 = r11.changeVrModeLocked(r8, r0)     // Catch: java.lang.Throwable -> L3e
            r9 = r0
            com.android.server.wm.WindowProcessController r0 = r12.app     // Catch: java.lang.Throwable -> L3b
            if (r0 == 0) goto L2e
            com.android.server.wm.WindowProcessController r0 = r12.app     // Catch: java.lang.Throwable -> L3b
            int r0 = r0.getPid()     // Catch: java.lang.Throwable -> L3b
            r10 = r0
            goto L2f
        L2e:
            r10 = r1
        L2f:
            monitor-exit(r7)     // Catch: java.lang.Throwable -> L37
            r0 = r6
            r1 = r8
            r4 = r10
            r0.setVrMode(r1, r2, r3, r4, r5)
            return r9
        L37:
            r0 = move-exception
            r4 = r9
            r1 = r10
            goto L3f
        L3b:
            r0 = move-exception
            r4 = r9
            goto L3f
        L3e:
            r0 = move-exception
        L3f:
            monitor-exit(r7)     // Catch: java.lang.Throwable -> L3e
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.wm.VrController.onVrModeChanged(com.android.server.wm.ActivityRecord):boolean");
    }

    public void setVrThreadLocked(int tid, int pid, com.android.server.wm.WindowProcessController proc) {
        if (hasPersistentVrFlagSet()) {
            android.util.Slog.w(TAG, "VR thread cannot be set in persistent VR mode!");
            return;
        }
        if (proc == null) {
            android.util.Slog.w(TAG, "Persistent VR thread not set, calling process doesn't exist!");
            return;
        }
        if (tid != 0) {
            enforceThreadInProcess(tid, pid);
        }
        if (!inVrMode()) {
            android.util.Slog.w(TAG, "VR thread cannot be set when not in VR mode!");
        } else {
            setVrRenderThreadLocked(tid, proc.getCurrentSchedulingGroup(), false);
        }
        proc.mVrThreadTid = tid > 0 ? tid : 0;
    }

    public void setPersistentVrThreadLocked(int tid, int pid, com.android.server.wm.WindowProcessController proc) {
        if (!hasPersistentVrFlagSet()) {
            android.util.Slog.w(TAG, "Persistent VR thread may only be set in persistent VR mode!");
        } else {
            if (proc == null) {
                android.util.Slog.w(TAG, "Persistent VR thread not set, calling process doesn't exist!");
                return;
            }
            if (tid != 0) {
                enforceThreadInProcess(tid, pid);
            }
            setPersistentVrRenderThreadLocked(tid, false);
        }
    }

    public boolean shouldDisableNonVrUiLocked() {
        return this.mVrState != 0;
    }

    private boolean changeVrModeLocked(boolean vrMode, com.android.server.wm.WindowProcessController proc) {
        int oldVrState = this.mVrState;
        if (vrMode) {
            this.mVrState |= 1;
        } else {
            this.mVrState &= -2;
        }
        boolean changed = oldVrState != this.mVrState;
        if (changed) {
            if (proc != null) {
                if (proc.mVrThreadTid > 0) {
                    setVrRenderThreadLocked(proc.mVrThreadTid, proc.getCurrentSchedulingGroup(), false);
                }
            } else {
                clearVrRenderThreadLocked(false);
            }
        }
        return changed;
    }

    private int updateVrRenderThreadLocked(int newTid, boolean suppressLogs) {
        if (this.mVrRenderThreadTid == newTid) {
            return this.mVrRenderThreadTid;
        }
        if (this.mVrRenderThreadTid > 0) {
            com.android.server.am.ActivityManagerService.scheduleAsRegularPriority(this.mVrRenderThreadTid, suppressLogs);
            this.mVrRenderThreadTid = 0;
        }
        if (newTid > 0) {
            this.mVrRenderThreadTid = newTid;
            com.android.server.am.ActivityManagerService.scheduleAsFifoPriority(this.mVrRenderThreadTid, suppressLogs);
        }
        return this.mVrRenderThreadTid;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int setPersistentVrRenderThreadLocked(int newTid, boolean suppressLogs) {
        if (!hasPersistentVrFlagSet()) {
            if (!suppressLogs) {
                android.util.Slog.w(TAG, "Failed to set persistent VR thread, system not in persistent VR mode.");
            }
            return this.mVrRenderThreadTid;
        }
        return updateVrRenderThreadLocked(newTid, suppressLogs);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int setVrRenderThreadLocked(int newTid, int schedGroup, boolean suppressLogs) {
        boolean inVr = inVrMode();
        boolean inPersistentVr = hasPersistentVrFlagSet();
        if (!inVr || inPersistentVr || schedGroup != 3) {
            if (!suppressLogs) {
                java.lang.String reason = "caller is not the current top application.";
                if (!inVr) {
                    reason = "system not in VR mode.";
                } else if (inPersistentVr) {
                    reason = "system in persistent VR mode.";
                }
                android.util.Slog.w(TAG, "Failed to set VR thread, " + reason);
            }
            return this.mVrRenderThreadTid;
        }
        return updateVrRenderThreadLocked(newTid, suppressLogs);
    }

    private void clearVrRenderThreadLocked(boolean suppressLogs) {
        updateVrRenderThreadLocked(0, suppressLogs);
    }

    private void enforceThreadInProcess(int tid, int pid) {
        if (!android.os.Process.isThreadInProcess(pid, tid)) {
            throw new java.lang.IllegalArgumentException("VR thread does not belong to process");
        }
    }

    private boolean inVrMode() {
        return (this.mVrState & 1) != 0;
    }

    private boolean hasPersistentVrFlagSet() {
        return (this.mVrState & 2) != 0;
    }

    public java.lang.String toString() {
        return java.lang.String.format("[VrState=0x%x,VrRenderThreadTid=%d]", java.lang.Integer.valueOf(this.mVrState), java.lang.Integer.valueOf(this.mVrRenderThreadTid));
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        android.util.proto.ProtoUtils.writeBitWiseFlagsToProtoEnum(proto, 2259152797697L, this.mVrState, ORIG_ENUMS, PROTO_ENUMS);
        proto.write(1120986464258L, this.mVrRenderThreadTid);
        proto.end(token);
    }
}
