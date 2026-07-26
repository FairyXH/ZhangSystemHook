package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
public class AppOpsCheckingServiceTracingDecorator implements com.android.server.appop.AppOpsCheckingServiceInterface {
    private static final long TRACE_TAG = 64;
    private final com.android.server.appop.AppOpsCheckingServiceInterface mService;

    AppOpsCheckingServiceTracingDecorator(com.android.server.appop.AppOpsCheckingServiceInterface appOpsCheckingServiceInterface) {
        this.mService = appOpsCheckingServiceInterface;
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void writeState() {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#writeState");
        try {
            this.mService.writeState();
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void readState() {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#readState");
        try {
            this.mService.readState();
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void shutdown() {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#shutdown");
        try {
            this.mService.shutdown();
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void systemReady() {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#systemReady");
        try {
            this.mService.systemReady();
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseIntArray getNonDefaultUidModes(int uid, java.lang.String persistentDeviceId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#getNonDefaultUidModes");
        try {
            return this.mService.getNonDefaultUidModes(uid, persistentDeviceId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseIntArray getNonDefaultPackageModes(java.lang.String packageName, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#getNonDefaultPackageModes");
        try {
            return this.mService.getNonDefaultPackageModes(packageName, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public int getUidMode(int uid, java.lang.String persistentDeviceId, int op) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#getUidMode");
        try {
            return this.mService.getUidMode(uid, persistentDeviceId, op);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean setUidMode(int uid, java.lang.String persistentDeviceId, int op, int mode) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#setUidMode");
        try {
            return this.mService.setUidMode(uid, persistentDeviceId, op, mode);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public int getPackageMode(java.lang.String packageName, int op, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#getPackageMode");
        try {
            return this.mService.getPackageMode(packageName, op, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void setPackageMode(java.lang.String packageName, int op, int mode, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#setPackageMode");
        try {
            this.mService.setPackageMode(packageName, op, mode, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean removePackage(java.lang.String packageName, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#removePackage");
        try {
            return this.mService.removePackage(packageName, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void removeUid(int uid) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#removeUid");
        try {
            this.mService.removeUid(uid);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public void clearAllModes() {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#clearAllModes");
        try {
            this.mService.clearAllModes();
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseBooleanArray getForegroundOps(int uid, java.lang.String persistentDeviceId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#getForegroundOps");
        try {
            return this.mService.getForegroundOps(uid, persistentDeviceId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public android.util.SparseBooleanArray getForegroundOps(java.lang.String packageName, int userId) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#getForegroundOps");
        try {
            return this.mService.getForegroundOps(packageName, userId);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean addAppOpsModeChangedListener(com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener listener) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#addAppOpsModeChangedListener");
        try {
            return this.mService.addAppOpsModeChangedListener(listener);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }

    @Override // com.android.server.appop.AppOpsCheckingServiceInterface
    public boolean removeAppOpsModeChangedListener(com.android.server.appop.AppOpsCheckingServiceInterface.AppOpsModeChangedListener listener) {
        android.os.Trace.traceBegin(TRACE_TAG, "TaggedTracingAppOpsCheckingServiceInterfaceImpl#removeAppOpsModeChangedListener");
        try {
            return this.mService.removeAppOpsModeChangedListener(listener);
        } finally {
            android.os.Trace.traceEnd(TRACE_TAG);
        }
    }
}
