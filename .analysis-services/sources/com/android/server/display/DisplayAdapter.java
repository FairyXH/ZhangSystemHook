package com.android.server.display;

/* JADX INFO: loaded from: classes2.dex */
abstract class DisplayAdapter {
    public static final int DISPLAY_DEVICE_EVENT_ADDED = 1;
    public static final int DISPLAY_DEVICE_EVENT_CHANGED = 2;
    public static final int DISPLAY_DEVICE_EVENT_REMOVED = 3;
    private static final java.util.concurrent.atomic.AtomicInteger NEXT_DISPLAY_MODE_ID = new java.util.concurrent.atomic.AtomicInteger(1);
    private final android.content.Context mContext;
    public com.android.server.display.IDisplayAdapterExt mDisplayAdapterExt = (com.android.server.display.IDisplayAdapterExt) system.ext.loader.core.ExtLoader.type(com.android.server.display.IDisplayAdapterExt.class).base(this).create();
    private final com.android.server.display.feature.DisplayManagerFlags mFeatureFlags;
    private final android.os.Handler mHandler;
    private final com.android.server.display.DisplayAdapter.Listener mListener;
    private final java.lang.String mName;
    private final com.android.server.display.DisplayManagerService.SyncRoot mSyncRoot;

    public interface Listener {
        void onDisplayDeviceEvent(com.android.server.display.DisplayDevice displayDevice, int i);

        void onDisplayDeviceEvent(com.android.server.display.DisplayDevice displayDevice, int i, long j, long j2);

        void onTraversalRequested();
    }

    DisplayAdapter(com.android.server.display.DisplayManagerService.SyncRoot syncRoot, android.content.Context context, android.os.Handler handler, com.android.server.display.DisplayAdapter.Listener listener, java.lang.String name, com.android.server.display.feature.DisplayManagerFlags featureFlags) {
        this.mSyncRoot = syncRoot;
        this.mContext = context;
        this.mHandler = handler;
        this.mListener = listener;
        this.mName = name;
        this.mFeatureFlags = featureFlags;
        this.mDisplayAdapterExt.setDisplayHandler(handler);
        this.mDisplayAdapterExt.setListener(listener);
    }

    public final com.android.server.display.DisplayManagerService.SyncRoot getSyncRoot() {
        return this.mSyncRoot;
    }

    public final android.content.Context getContext() {
        return this.mContext;
    }

    public final android.os.Handler getHandler() {
        return this.mHandler;
    }

    public final java.lang.String getName() {
        return this.mName;
    }

    public final com.android.server.display.feature.DisplayManagerFlags getFeatureFlags() {
        return this.mFeatureFlags;
    }

    public void registerLocked() {
    }

    public void dumpLocked(java.io.PrintWriter pw) {
    }

    protected final void sendDisplayDeviceEventLocked(com.android.server.display.DisplayDevice device, int event) {
        sendDisplayDeviceEventLocked(device, event, -1L);
    }

    protected final void sendDisplayDeviceEventLocked(com.android.server.display.DisplayDevice device, int event, long timestamp) {
        this.mDisplayAdapterExt.sendDisplayDeviceEventLocked(device, event, timestamp);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$sendTraversalRequestLocked$0() {
        this.mListener.onTraversalRequested();
    }

    protected final void sendTraversalRequestLocked() {
        this.mHandler.post(new java.lang.Runnable() { // from class: com.android.server.display.DisplayAdapter$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$sendTraversalRequestLocked$0();
            }
        });
    }

    public static android.view.Display.Mode createMode(int width, int height, float refreshRate) {
        return createMode(width, height, refreshRate, refreshRate, new float[0], new int[0]);
    }

    public static android.view.Display.Mode createMode(int width, int height, float refreshRate, float vsyncRate, float[] alternativeRefreshRates, int[] supportedHdrTypes) {
        return new android.view.Display.Mode(NEXT_DISPLAY_MODE_ID.getAndIncrement(), width, height, refreshRate, vsyncRate, false, alternativeRefreshRates, supportedHdrTypes);
    }
}
