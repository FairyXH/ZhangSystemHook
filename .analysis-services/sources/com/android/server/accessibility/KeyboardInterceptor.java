package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class KeyboardInterceptor extends com.android.server.accessibility.BaseEventStreamTransformation implements android.os.Handler.Callback {
    private static final java.lang.String LOG_TAG = "KeyboardInterceptor";
    private static final int MESSAGE_PROCESS_QUEUED_EVENTS = 1;
    private final com.android.server.accessibility.AccessibilityManagerService mAms;
    private com.android.server.accessibility.KeyboardInterceptor.KeyEventHolder mEventQueueEnd;
    private com.android.server.accessibility.KeyboardInterceptor.KeyEventHolder mEventQueueStart;
    private final android.os.Handler mHandler;
    private final com.android.server.policy.WindowManagerPolicy mPolicy;

    public KeyboardInterceptor(com.android.server.accessibility.AccessibilityManagerService service, com.android.server.policy.WindowManagerPolicy policy) {
        this.mAms = service;
        this.mPolicy = policy;
        this.mHandler = new android.os.Handler(this);
    }

    public KeyboardInterceptor(com.android.server.accessibility.AccessibilityManagerService service, com.android.server.policy.WindowManagerPolicy policy, android.os.Handler handler) {
        this.mAms = service;
        this.mPolicy = policy;
        this.mHandler = handler;
    }

    @Override // com.android.server.accessibility.EventStreamTransformation
    public void onKeyEvent(android.view.KeyEvent event, int policyFlags) {
        if (this.mAms.getTraceManager().isA11yTracingEnabledForTypes(4096L)) {
            this.mAms.getTraceManager().logTrace("KeyboardInterceptor.onKeyEvent", 4096L, "event=" + event + ";policyFlags=" + policyFlags);
        }
        long eventDelay = getEventDelay(event, policyFlags);
        if (eventDelay < 0) {
            return;
        }
        if (eventDelay > 0 || this.mEventQueueStart != null) {
            addEventToQueue(event, policyFlags, eventDelay);
        } else {
            this.mAms.notifyKeyEvent(event, policyFlags);
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(android.os.Message msg) {
        if (msg.what != 1) {
            android.util.Slog.e(LOG_TAG, "Unexpected message type");
            return false;
        }
        processQueuedEvents();
        if (this.mEventQueueStart != null) {
            scheduleProcessQueuedEvents();
        }
        return true;
    }

    private void addEventToQueue(android.view.KeyEvent event, int policyFlags, long delay) {
        long dispatchTime = android.os.SystemClock.uptimeMillis() + delay;
        if (this.mEventQueueStart == null) {
            com.android.server.accessibility.KeyboardInterceptor.KeyEventHolder keyEventHolderObtain = com.android.server.accessibility.KeyboardInterceptor.KeyEventHolder.obtain(event, policyFlags, dispatchTime);
            this.mEventQueueStart = keyEventHolderObtain;
            this.mEventQueueEnd = keyEventHolderObtain;
            scheduleProcessQueuedEvents();
            return;
        }
        com.android.server.accessibility.KeyboardInterceptor.KeyEventHolder holder = com.android.server.accessibility.KeyboardInterceptor.KeyEventHolder.obtain(event, policyFlags, dispatchTime);
        holder.next = this.mEventQueueStart;
        this.mEventQueueStart.previous = holder;
        this.mEventQueueStart = holder;
    }

    private void scheduleProcessQueuedEvents() {
        if (!this.mHandler.sendEmptyMessageAtTime(1, this.mEventQueueEnd.dispatchTime)) {
            android.util.Slog.e(LOG_TAG, "Failed to schedule key event");
        }
    }

    private void processQueuedEvents() {
        long currentTime = android.os.SystemClock.uptimeMillis();
        while (this.mEventQueueEnd != null && this.mEventQueueEnd.dispatchTime <= currentTime) {
            long eventDelay = getEventDelay(this.mEventQueueEnd.event, this.mEventQueueEnd.policyFlags);
            if (eventDelay > 0) {
                this.mEventQueueEnd.dispatchTime = currentTime + eventDelay;
                return;
            }
            if (eventDelay == 0) {
                this.mAms.notifyKeyEvent(this.mEventQueueEnd.event, this.mEventQueueEnd.policyFlags);
            }
            com.android.server.accessibility.KeyboardInterceptor.KeyEventHolder eventToBeRecycled = this.mEventQueueEnd;
            this.mEventQueueEnd = this.mEventQueueEnd.previous;
            if (this.mEventQueueEnd != null) {
                this.mEventQueueEnd.next = null;
            }
            eventToBeRecycled.recycle();
            if (this.mEventQueueEnd == null) {
                this.mEventQueueStart = null;
            }
        }
    }

    private long getEventDelay(android.view.KeyEvent event, int policyFlags) {
        int keyCode = event.getKeyCode();
        if (keyCode == 25 || keyCode == 24) {
            return this.mPolicy.interceptKeyBeforeDispatching(null, event, policyFlags);
        }
        return 0L;
    }

    private static class KeyEventHolder {
        private static final int MAX_POOL_SIZE = 32;
        private static final android.util.Pools.SimplePool<com.android.server.accessibility.KeyboardInterceptor.KeyEventHolder> sPool = new android.util.Pools.SimplePool<>(32);
        public long dispatchTime;
        public android.view.KeyEvent event;
        public com.android.server.accessibility.KeyboardInterceptor.KeyEventHolder next;
        public int policyFlags;
        public com.android.server.accessibility.KeyboardInterceptor.KeyEventHolder previous;

        private KeyEventHolder() {
        }

        public static com.android.server.accessibility.KeyboardInterceptor.KeyEventHolder obtain(android.view.KeyEvent event, int policyFlags, long dispatchTime) {
            com.android.server.accessibility.KeyboardInterceptor.KeyEventHolder holder = (com.android.server.accessibility.KeyboardInterceptor.KeyEventHolder) sPool.acquire();
            if (holder == null) {
                holder = new com.android.server.accessibility.KeyboardInterceptor.KeyEventHolder();
            }
            holder.event = android.view.KeyEvent.obtain(event);
            holder.policyFlags = policyFlags;
            holder.dispatchTime = dispatchTime;
            return holder;
        }

        public void recycle() {
            this.event.recycle();
            this.event = null;
            this.policyFlags = 0;
            this.dispatchTime = 0L;
            this.next = null;
            this.previous = null;
            sPool.release(this);
        }
    }
}
