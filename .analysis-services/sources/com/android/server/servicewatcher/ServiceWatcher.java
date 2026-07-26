package com.android.server.servicewatcher;

/* JADX INFO: loaded from: classes3.dex */
public interface ServiceWatcher {

    public interface ServiceChangedListener {
        void onServiceChanged();
    }

    public interface ServiceListener<TBoundServiceInfo extends com.android.server.servicewatcher.ServiceWatcher.BoundServiceInfo> {
        void onBind(android.os.IBinder iBinder, TBoundServiceInfo tboundserviceinfo) throws android.os.RemoteException;

        void onUnbind();
    }

    public interface ServiceSupplier<TBoundServiceInfo extends com.android.server.servicewatcher.ServiceWatcher.BoundServiceInfo> {
        TBoundServiceInfo getServiceInfo();

        boolean hasMatchingService();

        void register(com.android.server.servicewatcher.ServiceWatcher.ServiceChangedListener serviceChangedListener);

        void unregister();
    }

    boolean checkServiceResolves();

    void dump(java.io.PrintWriter printWriter);

    void register();

    void runOnBinder(com.android.server.servicewatcher.ServiceWatcher.BinderOperation binderOperation);

    void unregister();

    public interface BinderOperation {
        void run(android.os.IBinder iBinder) throws android.os.RemoteException;

        default void onError(java.lang.Throwable t) {
        }
    }

    public static class BoundServiceInfo {
        protected final java.lang.String mAction;
        protected final android.content.ComponentName mComponentName;
        protected final int mUid;

        protected BoundServiceInfo(java.lang.String action, android.content.pm.ResolveInfo resolveInfo) {
            this(action, resolveInfo.serviceInfo.applicationInfo.uid, resolveInfo.serviceInfo.getComponentName());
        }

        protected BoundServiceInfo(java.lang.String action, int uid, android.content.ComponentName componentName) {
            this.mAction = action;
            this.mUid = uid;
            this.mComponentName = (android.content.ComponentName) java.util.Objects.requireNonNull(componentName);
        }

        public java.lang.String getAction() {
            return this.mAction;
        }

        public android.content.ComponentName getComponentName() {
            return this.mComponentName;
        }

        public int getUserId() {
            return android.os.UserHandle.getUserId(this.mUid);
        }

        public final boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.servicewatcher.ServiceWatcher.BoundServiceInfo)) {
                return false;
            }
            com.android.server.servicewatcher.ServiceWatcher.BoundServiceInfo that = (com.android.server.servicewatcher.ServiceWatcher.BoundServiceInfo) o;
            return this.mUid == that.mUid && java.util.Objects.equals(this.mAction, that.mAction) && this.mComponentName.equals(that.mComponentName);
        }

        public final int hashCode() {
            return java.util.Objects.hash(this.mAction, java.lang.Integer.valueOf(this.mUid), this.mComponentName);
        }

        public java.lang.String toString() {
            if (this.mComponentName == null) {
                return "none";
            }
            return this.mUid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.mComponentName.flattenToShortString();
        }
    }

    static <TBoundServiceInfo extends com.android.server.servicewatcher.ServiceWatcher.BoundServiceInfo> com.android.server.servicewatcher.ServiceWatcher create(android.content.Context context, java.lang.String tag, com.android.server.servicewatcher.ServiceWatcher.ServiceSupplier<TBoundServiceInfo> serviceSupplier, com.android.server.servicewatcher.ServiceWatcher.ServiceListener<? super TBoundServiceInfo> serviceListener) {
        return create(context, com.android.server.FgThread.getHandler(), tag, serviceSupplier, serviceListener);
    }

    static <TBoundServiceInfo extends com.android.server.servicewatcher.ServiceWatcher.BoundServiceInfo> com.android.server.servicewatcher.ServiceWatcher create(android.content.Context context, android.os.Handler handler, java.lang.String tag, com.android.server.servicewatcher.ServiceWatcher.ServiceSupplier<TBoundServiceInfo> serviceSupplier, com.android.server.servicewatcher.ServiceWatcher.ServiceListener<? super TBoundServiceInfo> serviceListener) {
        return new com.android.server.servicewatcher.ServiceWatcherImpl(context, handler, tag, serviceSupplier, serviceListener);
    }
}
