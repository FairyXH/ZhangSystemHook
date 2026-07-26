package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
public class MediaResourceMonitorService extends com.android.server.SystemService {
    private static final java.lang.String SERVICE_NAME = "media_resource_monitor";
    private final com.android.server.media.MediaResourceMonitorService.MediaResourceMonitorImpl mMediaResourceMonitorImpl;
    private static final java.lang.String TAG = "MediaResourceMonitor";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    public MediaResourceMonitorService(android.content.Context context) {
        super(context);
        this.mMediaResourceMonitorImpl = new com.android.server.media.MediaResourceMonitorService.MediaResourceMonitorImpl();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService(SERVICE_NAME, this.mMediaResourceMonitorImpl);
    }

    class MediaResourceMonitorImpl extends android.media.IMediaResourceMonitor.Stub {
        MediaResourceMonitorImpl() {
        }

        public void notifyResourceGranted(int pid, int type) throws android.os.RemoteException {
            if (com.android.server.media.MediaResourceMonitorService.DEBUG) {
                android.util.Log.d(com.android.server.media.MediaResourceMonitorService.TAG, "notifyResourceGranted(pid=" + pid + ", type=" + type + ")");
            }
            long identity = android.os.Binder.clearCallingIdentity();
            try {
                java.lang.String[] pkgNames = getPackageNamesFromPid(pid);
                if (pkgNames == null) {
                    return;
                }
                android.os.UserManager manager = (android.os.UserManager) com.android.server.media.MediaResourceMonitorService.this.getContext().createContextAsUser(android.os.UserHandle.of(android.app.ActivityManager.getCurrentUser()), 0).getSystemService(android.os.UserManager.class);
                java.util.List<android.os.UserHandle> enabledProfiles = manager.getEnabledProfiles();
                if (enabledProfiles.isEmpty()) {
                    return;
                }
                android.content.Intent intent = new android.content.Intent("android.intent.action.MEDIA_RESOURCE_GRANTED");
                intent.putExtra("android.intent.extra.PACKAGES", pkgNames);
                intent.putExtra("android.intent.extra.MEDIA_RESOURCE_TYPE", type);
                for (android.os.UserHandle userHandle : enabledProfiles) {
                    com.android.server.media.MediaResourceMonitorService.this.getContext().sendBroadcastAsUser(intent, userHandle, "android.permission.RECEIVE_MEDIA_RESOURCE_USAGE");
                }
            } finally {
                android.os.Binder.restoreCallingIdentity(identity);
            }
        }

        private java.lang.String[] getPackageNamesFromPid(int pid) {
            android.app.ActivityManager manager = (android.app.ActivityManager) com.android.server.media.MediaResourceMonitorService.this.getContext().getSystemService(android.app.ActivityManager.class);
            for (android.app.ActivityManager.RunningAppProcessInfo proc : manager.getRunningAppProcesses()) {
                if (proc.pid == pid) {
                    return proc.pkgList;
                }
            }
            return null;
        }
    }
}
