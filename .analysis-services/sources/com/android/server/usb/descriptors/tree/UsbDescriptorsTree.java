package com.android.server.usb.descriptors.tree;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbDescriptorsTree {
    private static final java.lang.String TAG = "UsbDescriptorsTree";
    private com.android.server.usb.descriptors.tree.UsbDescriptorsConfigNode mConfigNode;
    private com.android.server.usb.descriptors.tree.UsbDescriptorsDeviceNode mDeviceNode;
    private com.android.server.usb.descriptors.tree.UsbDescriptorsInterfaceNode mInterfaceNode;

    private void addDeviceDescriptor(com.android.server.usb.descriptors.UsbDeviceDescriptor deviceDescriptor) {
        this.mDeviceNode = new com.android.server.usb.descriptors.tree.UsbDescriptorsDeviceNode(deviceDescriptor);
    }

    private void addConfigDescriptor(com.android.server.usb.descriptors.UsbConfigDescriptor configDescriptor) {
        this.mConfigNode = new com.android.server.usb.descriptors.tree.UsbDescriptorsConfigNode(configDescriptor);
        this.mDeviceNode.addConfigDescriptorNode(this.mConfigNode);
    }

    private void addInterfaceDescriptor(com.android.server.usb.descriptors.UsbInterfaceDescriptor interfaceDescriptor) {
        this.mInterfaceNode = new com.android.server.usb.descriptors.tree.UsbDescriptorsInterfaceNode(interfaceDescriptor);
        this.mConfigNode.addInterfaceNode(this.mInterfaceNode);
    }

    private void addEndpointDescriptor(com.android.server.usb.descriptors.UsbEndpointDescriptor endpointDescriptor) {
        this.mInterfaceNode.addEndpointNode(new com.android.server.usb.descriptors.tree.UsbDescriptorsEndpointNode(endpointDescriptor));
    }

    private void addACInterface(com.android.server.usb.descriptors.UsbACInterface acInterface) {
        this.mInterfaceNode.addACInterfaceNode(new com.android.server.usb.descriptors.tree.UsbDescriptorsACInterfaceNode(acInterface));
    }

    public void parse(com.android.server.usb.descriptors.UsbDescriptorParser parser) {
        java.util.ArrayList<com.android.server.usb.descriptors.UsbDescriptor> descriptors = parser.getDescriptors();
        for (int descrIndex = 0; descrIndex < descriptors.size(); descrIndex++) {
            com.android.server.usb.descriptors.UsbDescriptor descriptor = descriptors.get(descrIndex);
            switch (descriptor.getType()) {
                case 1:
                    addDeviceDescriptor((com.android.server.usb.descriptors.UsbDeviceDescriptor) descriptor);
                    break;
                case 2:
                    addConfigDescriptor((com.android.server.usb.descriptors.UsbConfigDescriptor) descriptor);
                    break;
                case 4:
                    addInterfaceDescriptor((com.android.server.usb.descriptors.UsbInterfaceDescriptor) descriptor);
                    break;
                case 5:
                    addEndpointDescriptor((com.android.server.usb.descriptors.UsbEndpointDescriptor) descriptor);
                    break;
            }
        }
    }

    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        this.mDeviceNode.report(canvas);
    }
}
