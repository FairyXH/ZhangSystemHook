package com.android.server.usb.descriptors.tree;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbDescriptorsDeviceNode extends com.android.server.usb.descriptors.tree.UsbDescriptorsTreeNode {
    private static final java.lang.String TAG = "UsbDescriptorsDeviceNode";
    private final java.util.ArrayList<com.android.server.usb.descriptors.tree.UsbDescriptorsConfigNode> mConfigNodes = new java.util.ArrayList<>();
    private final com.android.server.usb.descriptors.UsbDeviceDescriptor mDeviceDescriptor;

    public UsbDescriptorsDeviceNode(com.android.server.usb.descriptors.UsbDeviceDescriptor deviceDescriptor) {
        this.mDeviceDescriptor = deviceDescriptor;
    }

    public void addConfigDescriptorNode(com.android.server.usb.descriptors.tree.UsbDescriptorsConfigNode configNode) {
        this.mConfigNodes.add(configNode);
    }

    @Override // com.android.server.usb.descriptors.tree.UsbDescriptorsTreeNode, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        this.mDeviceDescriptor.report(canvas);
        for (com.android.server.usb.descriptors.tree.UsbDescriptorsConfigNode node : this.mConfigNodes) {
            node.report(canvas);
        }
    }
}
