package com.android.server.location.injector;

/* JADX INFO: loaded from: classes2.dex */
public class SystemAppOpsHelper extends com.android.server.location.injector.AppOpsHelper {
    private android.app.AppOpsManager mAppOps;
    private final android.content.Context mContext;

    public SystemAppOpsHelper(android.content.Context context) {
        this.mContext = context;
    }

    public void onSystemReady() {
        if (this.mAppOps != null) {
            return;
        }
        this.mAppOps = (android.app.AppOpsManager) java.util.Objects.requireNonNull((android.app.AppOpsManager) this.mContext.getSystemService(android.app.AppOpsManager.class));
        this.mAppOps.startWatchingMode(0, (java.lang.String) null, 1, new android.app.AppOpsManager.OnOpChangedListener() { // from class: com.android.server.location.injector.SystemAppOpsHelper$$ExternalSyntheticLambda1
            @Override // android.app.AppOpsManager.OnOpChangedListener
            public final void onOpChanged(java.lang.String str, java.lang.String str2) {
                this.f$0.lambda$onSystemReady$1(str, str2);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$1(java.lang.String op, final java.lang.String packageName) {
        if (packageName == null) {
            com.android.server.FgThread.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.location.injector.SystemAppOpsHelper$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$onSystemReady$0(packageName);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onSystemReady$0(java.lang.String packageName) {
        notifyAppOpChanged(packageName);
    }

    @Override // com.android.server.location.injector.AppOpsHelper
    public boolean startOpNoThrow(int appOp, android.location.util.identity.CallerIdentity callerIdentity) {
        com.android.internal.util.Preconditions.checkState(this.mAppOps != null);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return this.mAppOps.startOpNoThrow(appOp, callerIdentity.getUid(), callerIdentity.getPackageName(), false, callerIdentity.getAttributionTag(), callerIdentity.getListenerId()) == 0;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @Override // com.android.server.location.injector.AppOpsHelper
    public void finishOp(int appOp, android.location.util.identity.CallerIdentity callerIdentity) {
        com.android.internal.util.Preconditions.checkState(this.mAppOps != null);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            this.mAppOps.finishOp(appOp, callerIdentity.getUid(), callerIdentity.getPackageName(), callerIdentity.getAttributionTag());
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @Override // com.android.server.location.injector.AppOpsHelper
    public boolean checkOpNoThrow(int appOp, android.location.util.identity.CallerIdentity callerIdentity) {
        com.android.internal.util.Preconditions.checkState(this.mAppOps != null);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return ((com.android.server.location.interfaces.IOplusLBSMainClass) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.IOplusLBSMainClass.DEFAULT, this.mContext)).checkOpNoThrow(this.mAppOps, appOp, callerIdentity, identity);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @Override // com.android.server.location.injector.AppOpsHelper
    public boolean noteOp(int appOp, android.location.util.identity.CallerIdentity callerIdentity) {
        com.android.internal.util.Preconditions.checkState(this.mAppOps != null);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            return this.mAppOps.noteOp(appOp, callerIdentity.getUid(), callerIdentity.getPackageName(), callerIdentity.getAttributionTag(), callerIdentity.getListenerId()) == 0;
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }

    @Override // com.android.server.location.injector.AppOpsHelper
    public boolean noteOpNoThrow(int appOp, android.location.util.identity.CallerIdentity callerIdentity) {
        boolean z = true;
        com.android.internal.util.Preconditions.checkState(this.mAppOps != null);
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            if (((com.android.server.location.interfaces.ISystemAppOpsHelperExt) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.ISystemAppOpsHelperExt.DEFAULT, new java.lang.Object[0])).needNoteOp(appOp, callerIdentity)) {
                if (this.mAppOps.noteOpNoThrow(appOp, callerIdentity.getUid(), callerIdentity.getPackageName(), callerIdentity.getAttributionTag(), callerIdentity.getListenerId()) != 0) {
                    z = false;
                }
                boolean status = z;
                ((com.android.server.location.interfaces.ISystemAppOpsHelperExt) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.ISystemAppOpsHelperExt.DEFAULT, new java.lang.Object[0])).updateNoteStatus(appOp, callerIdentity, status);
                return status;
            }
            return ((com.android.server.location.interfaces.ISystemAppOpsHelperExt) com.android.server.location.common.OplusLbsFactory.getInstance().getFeature(com.android.server.location.interfaces.ISystemAppOpsHelperExt.DEFAULT, new java.lang.Object[0])).getNoteOpStatus(appOp, callerIdentity);
        } finally {
            android.os.Binder.restoreCallingIdentity(identity);
        }
    }
}
