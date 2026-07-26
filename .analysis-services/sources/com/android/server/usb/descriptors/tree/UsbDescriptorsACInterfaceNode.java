package com.android.server.usb.descriptors.tree;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbDescriptorsACInterfaceNode extends com.android.server.usb.descriptors.tree.UsbDescriptorsTreeNode {
    private static final java.lang.String TAG = "UsbDescriptorsACInterfaceNode";
    private final com.android.server.usb.descriptors.UsbACInterface mACInterface;

    public UsbDescriptorsACInterfaceNode(com.android.server.usb.descriptors.UsbACInterface acInterface) {
        this.mACInterface = acInterface;
    }

    @Override // com.android.server.usb.descriptors.tree.UsbDescriptorsTreeNode, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        canvas.writeListItem("AC Interface type: 0x" + java.lang.Integer.toHexString(this.mACInterface.getSubtype()));
        canvas.openList();
        this.mACInterface.report(canvas);
        canvas.closeList();
    }
}
