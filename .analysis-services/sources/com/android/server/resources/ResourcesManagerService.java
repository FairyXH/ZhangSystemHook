package com.android.server.resources;

/* JADX INFO: loaded from: classes3.dex */
public class ResourcesManagerService extends com.android.server.SystemService {
    private com.android.server.am.ActivityManagerService mActivityManagerService;
    private final android.os.IBinder mService;

    public ResourcesManagerService(android.content.Context context) {
        super(context);
        this.mService = new android.content.res.IResourcesManager.Stub() { // from class: com.android.server.resources.ResourcesManagerService.1
            public boolean dumpResources(java.lang.String process, android.os.ParcelFileDescriptor fd, android.os.RemoteCallback callback) throws android.os.RemoteException {
                int callingUid = android.os.Binder.getCallingUid();
                if (callingUid != 0 && callingUid != 2000) {
                    callback.sendResult((android.os.Bundle) null);
                    throw new java.lang.SecurityException("dump should only be called by shell");
                }
                return com.android.server.resources.ResourcesManagerService.this.mActivityManagerService.dumpResources(process, fd, callback);
            }

            protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter pw, java.lang.String[] args) {
                try {
                    android.os.ParcelFileDescriptor pfd = android.os.ParcelFileDescriptor.dup(fd);
                    try {
                        com.android.server.resources.ResourcesManagerService.this.mActivityManagerService.dumpAllResources(pfd, pw);
                        if (pfd != null) {
                            pfd.close();
                        }
                    } finally {
                    }
                } catch (java.lang.Exception e) {
                    pw.println("Exception while trying to dump all resources: " + e.getMessage());
                    e.printStackTrace(pw);
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            public int handleShellCommand(android.os.ParcelFileDescriptor in, android.os.ParcelFileDescriptor out, android.os.ParcelFileDescriptor err, java.lang.String[] args) {
                return new com.android.server.resources.ResourcesManagerShellCommand(this).exec(this, in.getFileDescriptor(), out.getFileDescriptor(), err.getFileDescriptor(), args);
            }
        };
        publishBinderService("resources", this.mService);
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        android.content.res.ResourceTimer.start();
    }

    public void setActivityManagerService(com.android.server.am.ActivityManagerService activityManagerService) {
        this.mActivityManagerService = activityManagerService;
    }
}
