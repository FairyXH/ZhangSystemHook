package com.android.server.display.notifications;

/* JADX INFO: loaded from: classes2.dex */
public class ConnectedDisplayUsbErrorsDetector implements android.hardware.usb.UsbManager.DisplayPortAltModeInfoListener {
    private static final java.lang.String TAG = "ConnectedDisplayUsbErrorsDetector";
    private final android.content.Context mContext;
    private final com.android.server.display.notifications.ConnectedDisplayUsbErrorsDetector.Injector mInjector;
    private final boolean mIsConnectedDisplayErrorHandlingEnabled;
    private com.android.server.display.notifications.ConnectedDisplayUsbErrorsDetector.Listener mListener;

    public interface Injector {
        android.hardware.usb.UsbManager getUsbManager();
    }

    public interface Listener {
        void onCableNotCapableDisplayPort();

        void onDisplayPortLinkTrainingFailure();
    }

    ConnectedDisplayUsbErrorsDetector(com.android.server.display.feature.DisplayManagerFlags flags, final android.content.Context context) {
        this(flags, context, new com.android.server.display.notifications.ConnectedDisplayUsbErrorsDetector.Injector() { // from class: com.android.server.display.notifications.ConnectedDisplayUsbErrorsDetector$$ExternalSyntheticLambda0
            @Override // com.android.server.display.notifications.ConnectedDisplayUsbErrorsDetector.Injector
            public final android.hardware.usb.UsbManager getUsbManager() {
                return com.android.server.display.notifications.ConnectedDisplayUsbErrorsDetector.lambda$new$0(context);
            }
        });
    }

    static /* synthetic */ android.hardware.usb.UsbManager lambda$new$0(android.content.Context context) {
        return (android.hardware.usb.UsbManager) context.getSystemService(android.hardware.usb.UsbManager.class);
    }

    ConnectedDisplayUsbErrorsDetector(com.android.server.display.feature.DisplayManagerFlags flags, android.content.Context context, com.android.server.display.notifications.ConnectedDisplayUsbErrorsDetector.Injector injector) {
        this.mContext = context;
        this.mInjector = injector;
        this.mIsConnectedDisplayErrorHandlingEnabled = flags.isConnectedDisplayErrorHandlingEnabled();
    }

    void registerListener(com.android.server.display.notifications.ConnectedDisplayUsbErrorsDetector.Listener listener) {
        if (!this.mIsConnectedDisplayErrorHandlingEnabled) {
            return;
        }
        android.hardware.usb.UsbManager usbManager = this.mInjector.getUsbManager();
        if (usbManager == null) {
            android.util.Slog.e(TAG, "UsbManager is null");
            return;
        }
        this.mListener = listener;
        try {
            usbManager.registerDisplayPortAltModeInfoListener(this.mContext.getMainExecutor(), this);
        } catch (java.lang.IllegalStateException e) {
            android.util.Slog.e(TAG, "Failed to register listener", e);
        }
    }

    public void onDisplayPortAltModeInfoChanged(java.lang.String portId, android.hardware.usb.DisplayPortAltModeInfo info) {
        if (this.mListener == null) {
            return;
        }
        if (2 == info.getPartnerSinkStatus() && 1 == info.getCableStatus()) {
            this.mListener.onCableNotCapableDisplayPort();
        } else if (2 == info.getLinkTrainingStatus()) {
            this.mListener.onDisplayPortLinkTrainingFailure();
        }
    }
}
