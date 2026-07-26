package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class SoftInputShowHideHistory {
    private static final java.util.concurrent.atomic.AtomicInteger sSequenceNumber = new java.util.concurrent.atomic.AtomicInteger(0);
    private final com.android.server.inputmethod.SoftInputShowHideHistory.Entry[] mEntries = new com.android.server.inputmethod.SoftInputShowHideHistory.Entry[16];
    private int mNextIndex = 0;

    SoftInputShowHideHistory() {
    }

    static final class Entry {
        final com.android.server.inputmethod.ClientState mClientState;
        final android.view.inputmethod.EditorInfo mEditorInfo;
        final java.lang.String mFocusedWindowName;
        final int mFocusedWindowSoftInputMode;
        final java.lang.String mImeControlTargetName;
        final java.lang.String mImeSurfaceParentName;
        final java.lang.String mImeTargetNameFromWm;
        final boolean mInFullscreenMode;
        final int mReason;
        final java.lang.String mRequestWindowName;
        final int mSequenceNumber = com.android.server.inputmethod.SoftInputShowHideHistory.sSequenceNumber.getAndIncrement();
        final long mTimestamp = android.os.SystemClock.uptimeMillis();
        final long mWallTime = java.lang.System.currentTimeMillis();

        Entry(com.android.server.inputmethod.ClientState client, android.view.inputmethod.EditorInfo editorInfo, java.lang.String focusedWindowName, int softInputMode, int reason, boolean inFullscreenMode, java.lang.String requestWindowName, java.lang.String imeControlTargetName, java.lang.String imeTargetName, java.lang.String imeSurfaceParentName) {
            this.mClientState = client;
            this.mEditorInfo = editorInfo;
            this.mFocusedWindowName = focusedWindowName;
            this.mFocusedWindowSoftInputMode = softInputMode;
            this.mReason = reason;
            this.mInFullscreenMode = inFullscreenMode;
            this.mRequestWindowName = requestWindowName;
            this.mImeControlTargetName = imeControlTargetName;
            this.mImeTargetNameFromWm = imeTargetName;
            this.mImeSurfaceParentName = imeSurfaceParentName;
        }
    }

    void addEntry(com.android.server.inputmethod.SoftInputShowHideHistory.Entry entry) {
        int index = this.mNextIndex;
        this.mEntries[index] = entry;
        this.mNextIndex = (this.mNextIndex + 1) % this.mEntries.length;
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", java.util.Locale.US).withZone(java.time.ZoneId.systemDefault());
        for (int i = 0; i < this.mEntries.length; i++) {
            com.android.server.inputmethod.SoftInputShowHideHistory.Entry entry = this.mEntries[(this.mNextIndex + i) % this.mEntries.length];
            if (entry != null) {
                pw.print(prefix);
                pw.println("SoftInputShowHide #" + entry.mSequenceNumber + ":");
                pw.print(prefix);
                pw.println("  time=" + formatter.format(java.time.Instant.ofEpochMilli(entry.mWallTime)) + " (timestamp=" + entry.mTimestamp + ")");
                pw.print(prefix);
                pw.print("  reason=" + com.android.internal.inputmethod.InputMethodDebug.softInputDisplayReasonToString(entry.mReason));
                pw.println(" inFullscreenMode=" + entry.mInFullscreenMode);
                pw.print(prefix);
                pw.println("  requestClient=" + entry.mClientState);
                pw.print(prefix);
                pw.println("  focusedWindowName=" + entry.mFocusedWindowName);
                pw.print(prefix);
                pw.println("  requestWindowName=" + entry.mRequestWindowName);
                pw.print(prefix);
                pw.println("  imeControlTargetName=" + entry.mImeControlTargetName);
                pw.print(prefix);
                pw.println("  imeTargetNameFromWm=" + entry.mImeTargetNameFromWm);
                pw.print(prefix);
                pw.println("  imeSurfaceParentName=" + entry.mImeSurfaceParentName);
                pw.print(prefix);
                pw.print("  editorInfo:");
                if (entry.mEditorInfo != null) {
                    pw.print(" inputType=" + entry.mEditorInfo.inputType);
                    pw.print(" privateImeOptions=" + entry.mEditorInfo.privateImeOptions);
                    pw.println(" fieldId (viewId)=" + entry.mEditorInfo.fieldId);
                } else {
                    pw.println(" null");
                }
                pw.print(prefix);
                pw.println("  focusedWindowSoftInputMode=" + com.android.internal.inputmethod.InputMethodDebug.softInputModeToString(entry.mFocusedWindowSoftInputMode));
            }
        }
    }
}
