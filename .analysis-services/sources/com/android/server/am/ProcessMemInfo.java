package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
public class ProcessMemInfo {
    final java.lang.String adjReason;
    final java.lang.String adjType;
    long memtrack;
    final java.lang.String name;
    final int oomAdj;
    final int pid;
    final int procState;
    long pss;
    long swapPss;

    public ProcessMemInfo(java.lang.String _name, int _pid, int _oomAdj, int _procState, java.lang.String _adjType, java.lang.String _adjReason) {
        this.name = _name;
        this.pid = _pid;
        this.oomAdj = _oomAdj;
        this.procState = _procState;
        this.adjType = _adjType;
        this.adjReason = _adjReason;
    }
}
