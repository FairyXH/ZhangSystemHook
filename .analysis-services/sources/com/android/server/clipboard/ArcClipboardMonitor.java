package com.android.server.clipboard;

/* JADX INFO: loaded from: classes.dex */
public class ArcClipboardMonitor implements java.util.function.Consumer<android.content.ClipData> {
    private static final java.lang.String TAG = "ArcClipboardMonitor";
    private java.util.function.BiConsumer<android.content.ClipData, java.lang.Integer> mAndroidClipboardSetter;
    private com.android.server.clipboard.ArcClipboardMonitor.ArcClipboardBridge mBridge;

    public interface ArcClipboardBridge {
        void onPrimaryClipChanged(android.content.ClipData clipData);

        void setHandler(java.util.function.BiConsumer<android.content.ClipData, java.lang.Integer> biConsumer);
    }

    ArcClipboardMonitor(java.util.function.BiConsumer<android.content.ClipData, java.lang.Integer> setAndroidClipboard) {
        this.mAndroidClipboardSetter = setAndroidClipboard;
        com.android.server.LocalServices.addService(com.android.server.clipboard.ArcClipboardMonitor.class, this);
    }

    @Override // java.util.function.Consumer
    public void accept(android.content.ClipData clip) {
        if (this.mBridge != null) {
            this.mBridge.onPrimaryClipChanged(clip);
        }
    }

    public void setClipboardBridge(com.android.server.clipboard.ArcClipboardMonitor.ArcClipboardBridge bridge) {
        this.mBridge = bridge;
        this.mBridge.setHandler(this.mAndroidClipboardSetter);
    }
}
