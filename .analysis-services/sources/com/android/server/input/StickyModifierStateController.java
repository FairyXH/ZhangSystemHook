package com.android.server.input;

/* JADX INFO: loaded from: classes2.dex */
final class StickyModifierStateController {
    private final android.util.SparseArray<com.android.server.input.StickyModifierStateController.StickyModifierStateListenerRecord> mStickyModifierStateListenerRecords = new android.util.SparseArray<>();
    private static final java.lang.String TAG = "ModifierStateController";
    private static final boolean DEBUG = android.util.Log.isLoggable(TAG, 3);

    StickyModifierStateController() {
    }

    public void notifyStickyModifierStateChanged(int modifierState, int lockedModifierState) {
        if (DEBUG) {
            android.util.Slog.d(TAG, "Sticky modifier state changed, modifierState = " + modifierState + ", lockedModifierState = " + lockedModifierState);
        }
        synchronized (this.mStickyModifierStateListenerRecords) {
            for (int i = 0; i < this.mStickyModifierStateListenerRecords.size(); i++) {
                this.mStickyModifierStateListenerRecords.valueAt(i).notifyStickyModifierStateChanged(modifierState, lockedModifierState);
            }
        }
    }

    public void registerStickyModifierStateListener(android.hardware.input.IStickyModifierStateListener listener, int pid) {
        synchronized (this.mStickyModifierStateListenerRecords) {
            if (this.mStickyModifierStateListenerRecords.get(pid) != null) {
                throw new java.lang.IllegalStateException("The calling process has already registered a StickyModifierStateListener.");
            }
            com.android.server.input.StickyModifierStateController.StickyModifierStateListenerRecord record = new com.android.server.input.StickyModifierStateController.StickyModifierStateListenerRecord(pid, listener);
            try {
                listener.asBinder().linkToDeath(record, 0);
                this.mStickyModifierStateListenerRecords.put(pid, record);
            } catch (android.os.RemoteException ex) {
                throw new java.lang.RuntimeException(ex);
            }
        }
    }

    public void unregisterStickyModifierStateListener(android.hardware.input.IStickyModifierStateListener listener, int pid) {
        synchronized (this.mStickyModifierStateListenerRecords) {
            com.android.server.input.StickyModifierStateController.StickyModifierStateListenerRecord record = this.mStickyModifierStateListenerRecords.get(pid);
            if (record == null) {
                throw new java.lang.IllegalStateException("The calling process has no registered StickyModifierStateListener.");
            }
            if (record.mListener.asBinder() != listener.asBinder()) {
                throw new java.lang.IllegalStateException("The calling process has a different registered StickyModifierStateListener.");
            }
            record.mListener.asBinder().unlinkToDeath(record, 0);
            this.mStickyModifierStateListenerRecords.remove(pid);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onStickyModifierStateListenerDied(int pid) {
        synchronized (this.mStickyModifierStateListenerRecords) {
            this.mStickyModifierStateListenerRecords.remove(pid);
        }
    }

    private class StickyModifierStateListenerRecord implements android.os.IBinder.DeathRecipient {
        public final android.hardware.input.IStickyModifierStateListener mListener;
        public final int mPid;

        StickyModifierStateListenerRecord(int pid, android.hardware.input.IStickyModifierStateListener listener) {
            this.mPid = pid;
            this.mListener = listener;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            if (com.android.server.input.StickyModifierStateController.DEBUG) {
                android.util.Slog.d(com.android.server.input.StickyModifierStateController.TAG, "Sticky modifier state listener for pid " + this.mPid + " died.");
            }
            com.android.server.input.StickyModifierStateController.this.onStickyModifierStateListenerDied(this.mPid);
        }

        public void notifyStickyModifierStateChanged(int modifierState, int lockedModifierState) {
            try {
                this.mListener.onStickyModifierStateChanged(modifierState, lockedModifierState);
            } catch (android.os.RemoteException ex) {
                android.util.Slog.w(com.android.server.input.StickyModifierStateController.TAG, "Failed to notify process " + this.mPid + " that sticky modifier state changed, assuming it died.", ex);
                binderDied();
            }
        }
    }
}
