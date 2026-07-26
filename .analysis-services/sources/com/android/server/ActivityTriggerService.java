package com.android.server;

/* JADX INFO: loaded from: classes.dex */
public class ActivityTriggerService extends com.android.server.SystemService {
    public static final int PROC_ADDED_NOTIFICATION = 1;
    public static final int PROC_REMOVED_NOTIFICATION = 0;
    private static java.lang.String TAG = "ActivityTriggerService";
    private com.android.server.ActivityTriggerService.EventHandlerThread eventHandler;

    static native void notifyAction_native(java.lang.String str, long j, java.lang.String str2, int i, int i2);

    public ActivityTriggerService(android.content.Context context) {
        super(context);
        this.eventHandler = new com.android.server.ActivityTriggerService.EventHandlerThread("EventHandlerThread");
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        android.util.Slog.i(TAG, "Starting ActivityTriggerService");
        this.eventHandler.start();
        publishLocalService(com.android.server.ActivityTriggerService.class, this);
    }

    public void updateRecord(com.android.server.am.HostingRecord hr, android.content.pm.ApplicationInfo info, int pid, int event) {
        if (hr != null) {
            this.eventHandler.getHandler().post(new com.android.server.ActivityTriggerService.LocalRunnable(info.packageName, info.longVersionCode, info.processName, pid, event));
        }
    }

    public class EventHandlerThread extends android.os.HandlerThread {
        private android.os.Handler handler;

        public EventHandlerThread(java.lang.String name) {
            super(name);
        }

        @Override // android.os.HandlerThread
        protected void onLooperPrepared() {
            this.handler = new android.os.Handler();
        }

        public android.os.Handler getHandler() {
            return this.handler;
        }
    }

    static class LocalRunnable implements java.lang.Runnable {
        private int event;
        private long lvCode;
        private java.lang.String packageName;
        private int pid;
        private java.lang.String procName;

        LocalRunnable(java.lang.String packageName, long lvCode, java.lang.String procName, int pid, int event) {
            this.packageName = packageName;
            this.lvCode = lvCode;
            this.procName = procName;
            this.pid = pid;
            this.event = event;
        }

        @Override // java.lang.Runnable
        public void run() {
            com.android.server.ActivityTriggerService.notifyAction_native(this.packageName, this.lvCode, this.procName, this.pid, this.event);
        }
    }
}
