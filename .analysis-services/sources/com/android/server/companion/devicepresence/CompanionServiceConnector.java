package com.android.server.companion.devicepresence;

/* JADX INFO: loaded from: classes.dex */
public class CompanionServiceConnector extends com.android.internal.infra.ServiceConnector.Impl<android.companion.ICompanionDeviceService> {
    private static final java.lang.String TAG = "CDM_CompanionServiceConnector";
    private static final long UNBIND_POST_DELAY_MS = 5000;
    private static volatile com.android.server.ServiceThread sServiceThread;
    private final android.content.ComponentName mComponentName;
    private final boolean mIsPrimary;
    private com.android.server.companion.devicepresence.CompanionServiceConnector.Listener mListener;
    private final int mUserId;

    public interface Listener {
        void onBindingDied(int i, java.lang.String str, com.android.server.companion.devicepresence.CompanionServiceConnector companionServiceConnector);
    }

    static com.android.server.companion.devicepresence.CompanionServiceConnector newInstance(android.content.Context context, int userId, android.content.ComponentName componentName, boolean isSelfManaged, boolean isPrimary) {
        int bindingFlags = isSelfManaged ? 268435456 : 65536;
        return new com.android.server.companion.devicepresence.CompanionServiceConnector(context, userId, componentName, bindingFlags, isPrimary);
    }

    private CompanionServiceConnector(android.content.Context context, int userId, android.content.ComponentName componentName, int bindingFlags, boolean isPrimary) {
        super(context, buildIntent(componentName), bindingFlags, userId, (java.util.function.Function) null);
        this.mUserId = userId;
        this.mComponentName = componentName;
        this.mIsPrimary = isPrimary;
    }

    void setListener(com.android.server.companion.devicepresence.CompanionServiceConnector.Listener listener) {
        this.mListener = listener;
    }

    void postOnDeviceAppeared(final android.companion.AssociationInfo associationInfo) {
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.companion.devicepresence.CompanionServiceConnector$$ExternalSyntheticLambda2
            public final void runNoResult(java.lang.Object obj) {
                ((android.companion.ICompanionDeviceService) obj).onDeviceAppeared(associationInfo);
            }
        });
    }

    void postOnDeviceDisappeared(final android.companion.AssociationInfo associationInfo) {
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.companion.devicepresence.CompanionServiceConnector$$ExternalSyntheticLambda3
            public final void runNoResult(java.lang.Object obj) {
                ((android.companion.ICompanionDeviceService) obj).onDeviceDisappeared(associationInfo);
            }
        });
    }

    void postOnDevicePresenceEvent(final android.companion.DevicePresenceEvent event) {
        post(new com.android.internal.infra.ServiceConnector.VoidJob() { // from class: com.android.server.companion.devicepresence.CompanionServiceConnector$$ExternalSyntheticLambda1
            public final void runNoResult(java.lang.Object obj) {
                ((android.companion.ICompanionDeviceService) obj).onDevicePresenceEvent(event);
            }
        });
    }

    void postUnbind() {
        getJobHandler().postDelayed(new java.lang.Runnable() { // from class: com.android.server.companion.devicepresence.CompanionServiceConnector$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.unbind();
            }
        }, UNBIND_POST_DELAY_MS);
    }

    boolean isPrimary() {
        return this.mIsPrimary;
    }

    android.content.ComponentName getComponentName() {
        return this.mComponentName;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onServiceConnectionStatusChanged(android.companion.ICompanionDeviceService service, boolean isConnected) {
        android.util.Slog.d(TAG, "onServiceConnectionStatusChanged() " + this.mComponentName.toShortString() + " connected=" + isConnected);
    }

    public void binderDied() {
        super.binderDied();
        android.util.Slog.d(TAG, "binderDied() " + this.mComponentName.toShortString());
        if (this.mListener != null) {
            this.mListener.onBindingDied(this.mUserId, this.mComponentName.getPackageName(), this);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX INFO: renamed from: binderAsInterface, reason: merged with bridge method [inline-methods] */
    public android.companion.ICompanionDeviceService m2792binderAsInterface(android.os.IBinder service) {
        return android.companion.ICompanionDeviceService.Stub.asInterface(service);
    }

    protected android.os.Handler getJobHandler() {
        return getServiceThread().getThreadHandler();
    }

    protected long getAutoDisconnectTimeoutMs() {
        return -1L;
    }

    private static android.content.Intent buildIntent(android.content.ComponentName componentName) {
        return new android.content.Intent("android.companion.CompanionDeviceService").setComponent(componentName);
    }

    private static com.android.server.ServiceThread getServiceThread() {
        if (sServiceThread == null) {
            synchronized (com.android.server.companion.CompanionDeviceManagerService.class) {
                if (sServiceThread == null) {
                    sServiceThread = new com.android.server.ServiceThread("companion-device-service-connector", 0, false);
                    sServiceThread.start();
                }
            }
        }
        return sServiceThread;
    }
}
