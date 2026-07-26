package com.android.server.usb.descriptors.tree;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbDescriptorsInterfaceNode extends com.android.server.usb.descriptors.tree.UsbDescriptorsTreeNode {
    private static final java.lang.String TAG = "UsbDescriptorsInterfaceNode";
    private final com.android.server.usb.descriptors.UsbInterfaceDescriptor mInterfaceDescriptor;
    private final java.util.ArrayList<com.android.server.usb.descriptors.tree.UsbDescriptorsEndpointNode> mEndpointNodes = new java.util.ArrayList<>();
    private final java.util.ArrayList<com.android.server.usb.descriptors.tree.UsbDescriptorsACInterfaceNode> mACInterfaceNodes = new java.util.ArrayList<>();

    public UsbDescriptorsInterfaceNode(com.android.server.usb.descriptors.UsbInterfaceDescriptor interfaceDescriptor) {
        this.mInterfaceDescriptor = interfaceDescriptor;
    }

    public void addEndpointNode(com.android.server.usb.descriptors.tree.UsbDescriptorsEndpointNode endpointNode) {
        this.mEndpointNodes.add(endpointNode);
    }

    public void addACInterfaceNode(com.android.server.usb.descriptors.tree.UsbDescriptorsACInterfaceNode acInterfaceNode) {
        this.mACInterfaceNodes.add(acInterfaceNode);
    }

    @Override // com.android.server.usb.descriptors.tree.UsbDescriptorsTreeNode, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        this.mInterfaceDescriptor.report(canvas);
        if (this.mACInterfaceNodes.size() > 0) {
            canvas.writeParagraph("Audio Class Interfaces", false);
            canvas.openList();
            for (com.android.server.usb.descriptors.tree.UsbDescriptorsACInterfaceNode node : this.mACInterfaceNodes) {
                node.report(canvas);
            }
            canvas.closeList();
        }
        if (this.mEndpointNodes.size() > 0) {
            canvas.writeParagraph("Endpoints", false);
            canvas.openList();
            for (com.android.server.usb.descriptors.tree.UsbDescriptorsEndpointNode node2 : this.mEndpointNodes) {
                node2.report(canvas);
            }
            canvas.closeList();
        }
    }
}
