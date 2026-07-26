package com.android.server.usb.descriptors.report;

/* JADX INFO: loaded from: classes3.dex */
public final class TextReportCanvas extends com.android.server.usb.descriptors.report.ReportCanvas {
    private static final int LIST_INDENT_AMNT = 2;
    private static final java.lang.String TAG = "TextReportCanvas";
    private int mListIndent;
    private final java.lang.StringBuilder mStringBuilder;

    public TextReportCanvas(com.android.server.usb.descriptors.UsbDescriptorParser parser, java.lang.StringBuilder stringBuilder) {
        super(parser);
        this.mStringBuilder = stringBuilder;
    }

    private void writeListIndent() {
        for (int space = 0; space < this.mListIndent; space++) {
            this.mStringBuilder.append(" ");
        }
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void write(java.lang.String text) {
        this.mStringBuilder.append(text);
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void openHeader(int level) {
        writeListIndent();
        this.mStringBuilder.append("[");
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void closeHeader(int level) {
        this.mStringBuilder.append("]\n");
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void openParagraph(boolean emphasis) {
        writeListIndent();
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void closeParagraph() {
        this.mStringBuilder.append("\n");
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void writeParagraph(java.lang.String text, boolean inRed) {
        openParagraph(inRed);
        if (inRed) {
            this.mStringBuilder.append(com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER + text + com.android.server.am.SettingsToPropertiesMapper.NAMESPACE_REBOOT_STAGING_DELIMITER);
        } else {
            this.mStringBuilder.append(text);
        }
        closeParagraph();
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void openList() {
        this.mListIndent += 2;
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void closeList() {
        this.mListIndent -= 2;
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void openListItem() {
        writeListIndent();
        this.mStringBuilder.append("- ");
    }

    @Override // com.android.server.usb.descriptors.report.ReportCanvas
    public void closeListItem() {
        this.mStringBuilder.append("\n");
    }
}
