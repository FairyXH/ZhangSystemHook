package com.android.server.usb.descriptors.tree;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbDescriptorsConfigNode extends com.android.server.usb.descriptors.tree.UsbDescriptorsTreeNode {
    private static final java.lang.String TAG = "UsbDescriptorsConfigNode";
    private final com.android.server.usb.descriptors.UsbConfigDescriptor mConfigDescriptor;
    private final java.util.ArrayList<com.android.server.usb.descriptors.tree.UsbDescriptorsInterfaceNode> mInterfaceNodes = new java.util.ArrayList<>();

    public UsbDescriptorsConfigNode(com.android.server.usb.descriptors.UsbConfigDescriptor configDescriptor) {
        this.mConfigDescriptor = configDescriptor;
    }

    public void addInterfaceNode(com.android.server.usb.descriptors.tree.UsbDescriptorsInterfaceNode interfaceNode) {
        this.mInterfaceNodes.add(interfaceNode);
    }

    @Override // com.android.server.usb.descriptors.tree.UsbDescriptorsTreeNode, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        this.mConfigDescriptor.report(canvas);
        canvas.openList();
        for (com.android.server.usb.descriptors.tree.UsbDescriptorsInterfaceNode node : this.mInterfaceNodes) {
            node.report(canvas);
        }
        canvas.closeList();
    }
}
