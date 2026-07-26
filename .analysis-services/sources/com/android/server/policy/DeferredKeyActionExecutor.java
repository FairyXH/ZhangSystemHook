package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
class DeferredKeyActionExecutor {
    private static final boolean DEBUG = com.android.server.policy.PhoneWindowManager.DEBUG_INPUT;
    private static final java.lang.String TAG = "DeferredKeyAction";
    private final android.util.SparseArray<com.android.server.policy.DeferredKeyActionExecutor.TimedActionsBuffer> mBuffers = new android.util.SparseArray<>();

    DeferredKeyActionExecutor() {
    }

    public void queueKeyAction(int keyCode, long downTime, java.lang.Runnable action) {
        getActionsBufferWithLazyCleanUp(keyCode, downTime).addAction(action);
    }

    public void setActionsExecutable(int keyCode, long downTime) {
        getActionsBufferWithLazyCleanUp(keyCode, downTime).setExecutable();
    }

    public void cancelQueuedAction(int keyCode) {
        com.android.server.policy.DeferredKeyActionExecutor.TimedActionsBuffer actionsBuffer = this.mBuffers.get(keyCode);
        if (actionsBuffer != null) {
            actionsBuffer.clear();
        }
    }

    private com.android.server.policy.DeferredKeyActionExecutor.TimedActionsBuffer getActionsBufferWithLazyCleanUp(int keyCode, long downTime) {
        com.android.server.policy.DeferredKeyActionExecutor.TimedActionsBuffer buffer = this.mBuffers.get(keyCode);
        if (buffer == null || buffer.getDownTime() != downTime) {
            if (DEBUG && buffer != null) {
                android.util.Log.d(TAG, "getActionsBufferWithLazyCleanUp: cleaning up gesture actions for key " + android.view.KeyEvent.keyCodeToString(keyCode));
            }
            com.android.server.policy.DeferredKeyActionExecutor.TimedActionsBuffer buffer2 = new com.android.server.policy.DeferredKeyActionExecutor.TimedActionsBuffer(keyCode, downTime);
            this.mBuffers.put(keyCode, buffer2);
            return buffer2;
        }
        return buffer;
    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.println(prefix + "Deferred key action executor:");
        if (this.mBuffers.size() == 0) {
            pw.println(prefix + "  empty");
            return;
        }
        for (int i = 0; i < this.mBuffers.size(); i++) {
            this.mBuffers.valueAt(i).dump(prefix, pw);
        }
    }

    private static class TimedActionsBuffer {
        private final java.util.List<java.lang.Runnable> mActions = new java.util.ArrayList();
        private final long mDownTime;
        private boolean mExecutable;
        private final int mKeyCode;

        TimedActionsBuffer(int keyCode, long downTime) {
            this.mKeyCode = keyCode;
            this.mDownTime = downTime;
        }

        long getDownTime() {
            return this.mDownTime;
        }

        void addAction(java.lang.Runnable action) {
            if (this.mExecutable) {
                if (com.android.server.policy.DeferredKeyActionExecutor.DEBUG) {
                    android.util.Log.i(com.android.server.policy.DeferredKeyActionExecutor.TAG, "addAction: execute action for key " + android.view.KeyEvent.keyCodeToString(this.mKeyCode));
                }
                action.run();
                return;
            }
            this.mActions.add(action);
        }

        void setExecutable() {
            this.mExecutable = true;
            if (com.android.server.policy.DeferredKeyActionExecutor.DEBUG && !this.mActions.isEmpty()) {
                android.util.Log.i(com.android.server.policy.DeferredKeyActionExecutor.TAG, "setExecutable: execute actions for key " + android.view.KeyEvent.keyCodeToString(this.mKeyCode));
            }
            for (java.lang.Runnable action : this.mActions) {
                action.run();
            }
            this.mActions.clear();
        }

        void clear() {
            this.mActions.clear();
        }

        void dump(java.lang.String prefix, java.io.PrintWriter pw) {
            if (this.mExecutable) {
                pw.println(prefix + "  " + android.view.KeyEvent.keyCodeToString(this.mKeyCode) + ": executable");
            } else {
                pw.println(prefix + "  " + android.view.KeyEvent.keyCodeToString(this.mKeyCode) + ": " + this.mActions.size() + " actions queued");
            }
        }
    }
}
