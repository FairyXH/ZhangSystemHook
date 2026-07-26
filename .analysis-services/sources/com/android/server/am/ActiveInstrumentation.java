package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
class ActiveInstrumentation {
    android.os.Bundle mArguments;
    android.content.ComponentName mClass;
    android.os.Bundle mCurResults;
    boolean mFinished;
    boolean mHasBackgroundActivityStartsPermission;
    boolean mHasBackgroundForegroundServiceStartsPermission;
    boolean mIsSdkInSandbox;
    boolean mNoRestart;
    java.lang.String mProfileFile;
    android.content.ComponentName mResultClass;
    final java.util.ArrayList<com.android.server.am.ProcessRecord> mRunningProcesses = new java.util.ArrayList<>();
    final com.android.server.am.ActivityManagerService mService;
    int mSourceUid;
    android.content.pm.ApplicationInfo mTargetInfo;
    java.lang.String[] mTargetProcesses;
    android.app.IUiAutomationConnection mUiAutomationConnection;
    android.app.IInstrumentationWatcher mWatcher;

    ActiveInstrumentation(com.android.server.am.ActivityManagerService service) {
        this.mService = service;
    }

    void removeProcess(com.android.server.am.ProcessRecord proc) {
        this.mFinished = true;
        this.mRunningProcesses.remove(proc);
        if (this.mRunningProcesses.size() == 0) {
            this.mService.mActiveInstrumentation.remove(this);
        }
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("ActiveInstrumentation{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(' ');
        sb.append(this.mClass.toShortString());
        if (this.mFinished) {
            sb.append(" FINISHED");
        }
        sb.append(" ");
        sb.append(this.mRunningProcesses.size());
        sb.append(" procs");
        sb.append('}');
        return sb.toString();
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("mClass=");
        pw.print(this.mClass);
        pw.print(" mFinished=");
        pw.println(this.mFinished);
        pw.print(prefix);
        pw.println("mRunningProcesses:");
        for (int i = 0; i < this.mRunningProcesses.size(); i++) {
            pw.print(prefix);
            pw.print("  #");
            pw.print(i);
            pw.print(": ");
            pw.println(this.mRunningProcesses.get(i));
        }
        pw.print(prefix);
        pw.print("mTargetProcesses=");
        pw.println(java.util.Arrays.toString(this.mTargetProcesses));
        pw.print(prefix);
        pw.print("mTargetInfo=");
        pw.println(this.mTargetInfo);
        if (this.mTargetInfo != null) {
            this.mTargetInfo.dump(new android.util.PrintWriterPrinter(pw), prefix + "  ", 0);
        }
        if (this.mProfileFile != null) {
            pw.print(prefix);
            pw.print("mProfileFile=");
            pw.println(this.mProfileFile);
        }
        if (this.mWatcher != null) {
            pw.print(prefix);
            pw.print("mWatcher=");
            pw.println(this.mWatcher);
        }
        if (this.mUiAutomationConnection != null) {
            pw.print(prefix);
            pw.print("mUiAutomationConnection=");
            pw.println(this.mUiAutomationConnection);
        }
        pw.print("mHasBackgroundActivityStartsPermission=");
        pw.println(this.mHasBackgroundActivityStartsPermission);
        pw.print("mHasBackgroundForegroundServiceStartsPermission=");
        pw.println(this.mHasBackgroundForegroundServiceStartsPermission);
        pw.print(prefix);
        pw.print("mArguments=");
        pw.println(this.mArguments);
    }

    void dumpDebug(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        this.mClass.dumpDebug(proto, 1146756268033L);
        proto.write(1133871366146L, this.mFinished);
        for (int i = 0; i < this.mRunningProcesses.size(); i++) {
            this.mRunningProcesses.get(i).dumpDebug(proto, 2246267895811L);
        }
        for (java.lang.String p : this.mTargetProcesses) {
            proto.write(2237677961220L, p);
        }
        if (this.mTargetInfo != null) {
            this.mTargetInfo.dumpDebug(proto, 1146756268037L, 0);
        }
        proto.write(1138166333446L, this.mProfileFile);
        proto.write(1138166333447L, this.mWatcher.toString());
        proto.write(1138166333448L, this.mUiAutomationConnection.toString());
        if (this.mArguments != null) {
            this.mArguments.dumpDebug(proto, 1146756268042L);
        }
        proto.end(token);
    }
}
