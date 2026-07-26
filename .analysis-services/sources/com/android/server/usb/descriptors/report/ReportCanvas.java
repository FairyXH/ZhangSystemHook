package com.android.server.usb.descriptors.report;

/* JADX INFO: loaded from: classes3.dex */
public abstract class ReportCanvas {
    private static final java.lang.String TAG = "ReportCanvas";
    private final com.android.server.usb.descriptors.UsbDescriptorParser mParser;

    public abstract void closeHeader(int i);

    public abstract void closeList();

    public abstract void closeListItem();

    public abstract void closeParagraph();

    public abstract void openHeader(int i);

    public abstract void openList();

    public abstract void openListItem();

    public abstract void openParagraph(boolean z);

    public abstract void write(java.lang.String str);

    public abstract void writeParagraph(java.lang.String str, boolean z);

    public ReportCanvas(com.android.server.usb.descriptors.UsbDescriptorParser parser) {
        this.mParser = parser;
    }

    public com.android.server.usb.descriptors.UsbDescriptorParser getParser() {
        return this.mParser;
    }

    public void writeHeader(int level, java.lang.String text) {
        openHeader(level);
        write(text);
        closeHeader(level);
    }

    public void writeListItem(java.lang.String text) {
        openListItem();
        write(text);
        closeListItem();
    }

    public static java.lang.String getHexString(byte value) {
        return "0x" + java.lang.Integer.toHexString(value & 255).toUpperCase();
    }

    public static java.lang.String getBCDString(int valueBCD) {
        int major = (valueBCD >> 8) & 15;
        int minor = (valueBCD >> 4) & 15;
        int subminor = valueBCD & 15;
        return "" + major + "." + minor + subminor;
    }

    public static java.lang.String getHexString(int value) {
        int intValue = 65535 & value;
        return "0x" + java.lang.Integer.toHexString(intValue).toUpperCase();
    }

    public void dumpHexArray(byte[] rawData, java.lang.StringBuilder builder) {
        if (rawData != null) {
            openParagraph(false);
            for (byte b : rawData) {
                builder.append(getHexString(b) + " ");
            }
            closeParagraph();
        }
    }
}
