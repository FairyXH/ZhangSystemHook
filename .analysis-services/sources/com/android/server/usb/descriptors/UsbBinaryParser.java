package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class UsbBinaryParser {
    private static final boolean LOGGING = false;
    private static final java.lang.String TAG = "UsbBinaryParser";

    private void dumpDescriptor(com.android.server.usb.descriptors.ByteStream stream, int length, byte type, java.lang.StringBuilder builder) {
        builder.append("<p>");
        builder.append("<b> l: " + length + " t:0x" + java.lang.Integer.toHexString(type) + " " + com.android.server.usb.descriptors.report.UsbStrings.getDescriptorName(type) + "</b><br>");
        for (int index = 2; index < length; index++) {
            builder.append("0x" + java.lang.Integer.toHexString(stream.getByte() & 255) + " ");
        }
        builder.append("</p>");
    }

    public void parseDescriptors(android.hardware.usb.UsbDeviceConnection connection, byte[] descriptors, java.lang.StringBuilder builder) {
        builder.append("<tt>");
        com.android.server.usb.descriptors.ByteStream stream = new com.android.server.usb.descriptors.ByteStream(descriptors);
        while (stream.available() > 0) {
            int length = stream.getByte() & 255;
            byte type = stream.getByte();
            dumpDescriptor(stream, length, type, builder);
        }
        builder.append("</tt>");
    }
}
