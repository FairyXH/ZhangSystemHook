package com.android.server.wm;

/* JADX INFO: loaded from: classes3.dex */
public class DisplayRotationSocExtImpl implements com.android.server.wm.IDisplayRotationSocExt {
    private static final java.lang.String ACTION_WIFI_DISPLAY_VIDEO = "org.codeaurora.intent.action.WIFI_DISPLAY_VIDEO";
    private static final java.lang.String TAG = "DisplayRotationSocExtImpl";
    private static final java.lang.String WIFI_DISPLAY_PERMISSION = "com.qualcomm.permission.wfd.QC_WFD";
    com.android.server.wm.DisplayRotation mRotation;
    private boolean mWifiDisplayConnected = false;
    private int mWifiDisplayRotation = -1;

    public DisplayRotationSocExtImpl(java.lang.Object rotation) {
        this.mRotation = (com.android.server.wm.DisplayRotation) rotation;
    }

    @Override // com.android.server.wm.IDisplayRotationSocExt
    public void hookRegisterWifiDisplay(final android.content.Context context, final com.android.server.wm.WindowManagerService service) {
        java.lang.Thread t = new java.lang.Thread() { // from class: com.android.server.wm.DisplayRotationSocExtImpl.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                context.registerReceiver(new android.content.BroadcastReceiver() { // from class: com.android.server.wm.DisplayRotationSocExtImpl.1.1
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(android.content.Context context2, android.content.Intent intent) {
                        try {
                            java.lang.String action = intent.getAction();
                            if (action.equals(com.android.server.wm.DisplayRotationSocExtImpl.ACTION_WIFI_DISPLAY_VIDEO)) {
                                int state = intent.getIntExtra("state", 0);
                                if (state == 1) {
                                    com.android.server.wm.DisplayRotationSocExtImpl.this.mWifiDisplayConnected = true;
                                } else {
                                    com.android.server.wm.DisplayRotationSocExtImpl.this.mWifiDisplayConnected = false;
                                }
                                int rotation = intent.getIntExtra("wfd_UIBC_rot", -1);
                                switch (rotation) {
                                    case 0:
                                        com.android.server.wm.DisplayRotationSocExtImpl.this.mWifiDisplayRotation = 0;
                                        break;
                                    case 1:
                                        com.android.server.wm.DisplayRotationSocExtImpl.this.mWifiDisplayRotation = 1;
                                        break;
                                    case 2:
                                        com.android.server.wm.DisplayRotationSocExtImpl.this.mWifiDisplayRotation = 2;
                                        break;
                                    case 3:
                                        com.android.server.wm.DisplayRotationSocExtImpl.this.mWifiDisplayRotation = 3;
                                        break;
                                    default:
                                        com.android.server.wm.DisplayRotationSocExtImpl.this.mWifiDisplayRotation = -1;
                                        break;
                                }
                                service.updateRotation(true, false);
                            }
                        } catch (java.lang.Exception e) {
                            android.util.Slog.e(com.android.server.wm.DisplayRotationSocExtImpl.TAG, "DisplayRotation: error to receive wifi action.", e);
                        }
                    }
                }, new android.content.IntentFilter(com.android.server.wm.DisplayRotationSocExtImpl.ACTION_WIFI_DISPLAY_VIDEO), com.android.server.wm.DisplayRotationSocExtImpl.WIFI_DISPLAY_PERMISSION, com.android.server.UiThread.getHandler());
            }
        };
        t.start();
    }

    @Override // com.android.server.wm.IDisplayRotationSocExt
    public boolean hookIsWifiDisplayConnected() {
        return this.mWifiDisplayConnected;
    }

    @Override // com.android.server.wm.IDisplayRotationSocExt
    public int hookGetWifiDisplayRotation() {
        return this.mWifiDisplayRotation;
    }
}
