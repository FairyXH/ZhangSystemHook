package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class StartInputHistory {
    private static final int ENTRY_SIZE_FOR_HIGH_RAM_DEVICE = 32;
    private static final int ENTRY_SIZE_FOR_LOW_RAM_DEVICE = 5;
    private final com.android.server.inputmethod.StartInputHistory.Entry[] mEntries = new com.android.server.inputmethod.StartInputHistory.Entry[getEntrySize()];
    private int mNextIndex = 0;

    StartInputHistory() {
    }

    private static int getEntrySize() {
        if (android.app.ActivityManager.isLowRamDeviceStatic()) {
            return 5;
        }
        return 32;
    }

    private static final class Entry {
        int mClientBindSequenceNumber;
        android.view.inputmethod.EditorInfo mEditorInfo;
        int mImeDisplayId;
        java.lang.String mImeId;
        java.lang.String mImeTokenString;
        int mImeUserId;
        boolean mRestarting;
        int mSequenceNumber;
        int mStartInputReason;
        int mTargetDisplayId;
        int mTargetUserId;
        int mTargetWindowSoftInputMode;
        java.lang.String mTargetWindowString;
        long mTimestamp;
        long mWallTime;

        Entry(com.android.server.inputmethod.StartInputInfo original) {
            set(original);
        }

        void set(com.android.server.inputmethod.StartInputInfo original) {
            this.mSequenceNumber = original.mSequenceNumber;
            this.mTimestamp = original.mTimestamp;
            this.mWallTime = original.mWallTime;
            this.mImeUserId = original.mImeUserId;
            this.mImeTokenString = java.lang.String.valueOf(original.mImeToken);
            this.mImeDisplayId = original.mImeDisplayId;
            this.mImeId = original.mImeId;
            this.mStartInputReason = original.mStartInputReason;
            this.mRestarting = original.mRestarting;
            this.mTargetUserId = original.mTargetUserId;
            this.mTargetDisplayId = original.mTargetDisplayId;
            this.mTargetWindowString = java.lang.String.valueOf(original.mTargetWindow);
            this.mEditorInfo = original.mEditorInfo;
            this.mTargetWindowSoftInputMode = original.mTargetWindowSoftInputMode;
            this.mClientBindSequenceNumber = original.mClientBindSequenceNumber;
        }
    }

    void addEntry(com.android.server.inputmethod.StartInputInfo info) {
        int index = this.mNextIndex;
        if (this.mEntries[index] == null) {
            this.mEntries[index] = new com.android.server.inputmethod.StartInputHistory.Entry(info);
        } else {
            this.mEntries[index].set(info);
        }
        this.mNextIndex = (this.mNextIndex + 1) % this.mEntries.length;
    }

    void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", java.util.Locale.US).withZone(java.time.ZoneId.systemDefault());
        for (int i = 0; i < this.mEntries.length; i++) {
            com.android.server.inputmethod.StartInputHistory.Entry entry = this.mEntries[(this.mNextIndex + i) % this.mEntries.length];
            if (entry != null) {
                pw.print(prefix);
                pw.println("StartInput #" + entry.mSequenceNumber + ":");
                pw.print(prefix);
                pw.println("  time=" + formatter.format(java.time.Instant.ofEpochMilli(entry.mWallTime)) + " (timestamp=" + entry.mTimestamp + ") reason=" + com.android.internal.inputmethod.InputMethodDebug.startInputReasonToString(entry.mStartInputReason) + " restarting=" + entry.mRestarting);
                pw.print(prefix);
                pw.print("  imeToken=" + entry.mImeTokenString + " [" + entry.mImeId + "]");
                pw.print(" imeUserId=" + entry.mImeUserId);
                pw.println(" imeDisplayId=" + entry.mImeDisplayId);
                pw.print(prefix);
                pw.println("  targetWin=" + entry.mTargetWindowString + " [" + entry.mEditorInfo.packageName + "] targetUserId=" + entry.mTargetUserId + " targetDisplayId=" + entry.mTargetDisplayId + " clientBindSeq=" + entry.mClientBindSequenceNumber);
                pw.print(prefix);
                pw.println("  softInputMode=" + com.android.internal.inputmethod.InputMethodDebug.softInputModeToString(entry.mTargetWindowSoftInputMode));
                pw.print(prefix);
                pw.println("  inputType=0x" + java.lang.Integer.toHexString(entry.mEditorInfo.inputType) + " imeOptions=0x" + java.lang.Integer.toHexString(entry.mEditorInfo.imeOptions) + " fieldId=0x" + java.lang.Integer.toHexString(entry.mEditorInfo.fieldId) + " fieldName=" + entry.mEditorInfo.fieldName + " actionId=" + entry.mEditorInfo.actionId + " actionLabel=" + ((java.lang.Object) entry.mEditorInfo.actionLabel));
            }
        }
    }
}
