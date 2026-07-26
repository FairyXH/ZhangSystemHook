package com.android.server.usb.descriptors.report;

/* JADX INFO: loaded from: classes3.dex */
public final class HTMLReportCanvas extends com.android.server.usb.descriptors.report.ReportCanvas {
    private static final java.lang.String TAG = "HTMLReportCanvas";
    private final java.lang.StringBuilder mStringBuilder;

    public HTMLReportCanvas(com.android.server.usb.descriptors.UsbDescriptorParser parser, java.lang.StringBuilder stringBuilder) {
        super(parser);
        this.mStringBuilder = stringBuilder;
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void write(java.lang.String text) {
        this.mStringBuilder.append(text);
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void openHeader(int level) {
        this.mStringBuilder.append("<h").append(level).append('>');
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void closeHeader(int level) {
        this.mStringBuilder.append("</h").append(level).append('>');
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void openParagraph(boolean emphasis) {
        if (emphasis) {
            this.mStringBuilder.append("<p style=\"color:red\">");
        } else {
            this.mStringBuilder.append("<p>");
        }
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void closeParagraph() {
        this.mStringBuilder.append("</p>");
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void writeParagraph(java.lang.String text, boolean inRed) {
        openParagraph(inRed);
        this.mStringBuilder.append(text);
        closeParagraph();
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void openList() {
        this.mStringBuilder.append("<ul>");
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void closeList() {
        this.mStringBuilder.append("</ul>");
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void openListItem() {
        this.mStringBuilder.append("<li>");
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void closeListItem() {
        this.mStringBuilder.append("</li>");
    }
}
