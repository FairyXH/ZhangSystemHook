package com.android.server.usb.descriptors.tree;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbDescriptorsEndpointNode extends com.android.server.usb.descriptors.tree.UsbDescriptorsTreeNode {
    private static final java.lang.String TAG = "UsbDescriptorsEndpointNode";
    private final com.android.server.usb.descriptors.UsbEndpointDescriptor mEndpointDescriptor;

    public UsbDescriptorsEndpointNode(com.android.server.usb.descriptors.UsbEndpointDescriptor endpointDescriptor) {
        this.mEndpointDescriptor = endpointDescriptor;
    }

    @Override // com.android.server.usb.descriptors.tree.UsbDescriptorsTreeNode, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        this.mEndpointDescriptor.report(canvas);
    }
}
