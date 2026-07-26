package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public abstract class UsbACHeaderInterface extends com.android.server.usb.descriptors.UsbACInterface {
    private static final java.lang.String TAG = "UsbACHeaderInterface";
    protected int mADCRelease;
    protected int mTotalLength;

    public UsbACHeaderInterface(int length, byte type, byte subtype, int subclass, int adcRelease) {
        super(length, type, subtype, subclass);
        this.mADCRelease = adcRelease;
    }

    public int getADCRelease() {
        return this.mADCRelease;
    }

    public int getTotalLength() {
        return this.mTotalLength;
    }

    @Override // com.android.server.usb.descriptors.UsbACInterface, com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        canvas.writeListItem("Release: " + com.android.server.usb.descriptors.report.ReportCanvas.getBCDString(getADCRelease()));
        canvas.writeListItem("Total Length: " + getTotalLength());
        canvas.closeList();
    }
}
