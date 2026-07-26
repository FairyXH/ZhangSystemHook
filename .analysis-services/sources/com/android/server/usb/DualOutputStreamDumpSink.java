package com.android.server.usb;

/* JADX INFO: loaded from: classes3.dex */
final class DualOutputStreamDumpSink implements com.android.server.utils.EventLogger.DumpSink {
    private final com.android.internal.util.dump.DualDumpOutputStream mDumpOutputStream;
    private final long mId;

    DualOutputStreamDumpSink(com.android.internal.util.dump.DualDumpOutputStream dualDumpOutputStream, long id) {
        this.mDumpOutputStream = dualDumpOutputStream;
        this.mId = id;
    }

    @Override // com.android.server.utils.EventLogger.DumpSink
    public void sink(java.lang.String tag, java.util.List<com.android.server.utils.EventLogger.Event> events) {
        this.mDumpOutputStream.write("USB Event Log", this.mId, tag);
        for (com.android.server.utils.EventLogger.Event evt : events) {
            this.mDumpOutputStream.write("USB Event", this.mId, evt.toString());
        }
    }
}
