package com.android.server.usb.descriptors;

/* JADX INFO: loaded from: classes3.dex */
public abstract class UsbVCHeaderInterface extends com.android.server.usb.descriptors.UsbVCInterface {
    private static final java.lang.String TAG = "UsbVCHeaderInterface";
    protected int mTotalLength;
    protected int mVDCRelease;

    public UsbVCHeaderInterface(int length, byte type, byte subtype, int vdcRelease) {
        super(length, type, subtype);
        this.mVDCRelease = vdcRelease;
    }

    public int getVDCRelease() {
        return this.mVDCRelease;
    }

    public int getTotalLength() {
        return this.mTotalLength;
    }

    @Override // com.android.server.usb.descriptors.UsbDescriptor, com.android.server.usb.descriptors.report.Reporting
    public void report(com.android.server.usb.descriptors.report.ReportCanvas canvas) {
        super.report(canvas);
        canvas.openList();
        canvas.writeListItem("Release: " + com.android.server.usb.descriptors.report.ReportCanvas.getBCDString(getVDCRelease()));
        canvas.writeListItem("Total Length: " + getTotalLength());
        canvas.closeList();
    }
}
