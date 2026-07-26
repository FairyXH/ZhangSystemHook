package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public final class Usb10ACHeader extends com.android.server.usb.descriptors.UsbACHeaderInterface {
    private static final java.lang.String TAG = "Usb10ACHeader";
    private byte mControls;
    private byte[] mInterfaceNums;
    private byte mNumInterfaces;

    public Usb10ACHeader(int length, byte type, byte subtype, int subclass, int spec) {
        super(length, type, subtype, subclass, spec);
        this.mNumInterfaces = (byte) 0;
        this.mInterfaceNums = null;
    }

    public byte getNumInterfaces() {
        return this.mNumInterfaces;
    }

    public byte[] getInterfaceNums() {
        return this.mInterfaceNums;
    }

    public byte getControls() {
        return this.mControls;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor
    public int parseRawDescriptors(com.android.server.usb.descriptors.ByteStream stream) {
        this.mTotalLength = stream.unpackUsbShort();
        if (this.mADCRelease >= 512) {
            this.mControls = stream.getByte();
        } else {
            this.mNumInterfaces = stream.getByte();
            this.mInterfaceNums = new byte[this.mNumInterfaces];
            for (int index = 0; index < this.mNumInterfaces; index++) {
                this.mInterfaceNums[index] = stream.getByte();
            }
        }
        int index2 = this.mLength;
        return index2;
    }

    @Override // com.android.server.usb.descriptors.UsbACHeaderInterface, com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        int numInterfaces = getNumInterfaces();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        sb.append("" + numInterfaces + " Interfaces");
        if (numInterfaces > 0) {
            sb.append(" [");
            byte[] interfaceNums = getInterfaceNums();
            if (interfaceNums != null) {
                for (int index = 0; index < numInterfaces; index++) {
                    sb.append("" + ((int) interfaceNums[index]));
                    if (index < numInterfaces - 1) {
                        sb.append(" ");
                    }
                }
            }
            sb.append("]");
        }
        canvas.writeListItem(sb.toString());
        canvas.writeListItem("Controls: " + com.android.server.usb.descriptors.report.ReportCanvas.getHexString(getControls()));
        canvas.closeList();
    }
}
