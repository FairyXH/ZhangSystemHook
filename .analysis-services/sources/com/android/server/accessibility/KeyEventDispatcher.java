package com.android.server.accessibility;

/* JADX INFO: loaded from: classes.dex */
public class KeyEventDispatcher implements android.os.Handler.Callback {
    private static final boolean DEBUG = false;
    private static final java.lang.String LOG_TAG = "KeyEventDispatcher";
    private static final int MAX_POOL_SIZE = 10;
    public static final int MSG_ON_KEY_EVENT_TIMEOUT = 1;
    private static final long ON_KEY_EVENT_TIMEOUT_MILLIS = 500;
    private final android.os.Handler mHandlerToSendKeyEventsToInputFilter;
    private android.os.Handler mKeyEventTimeoutHandler;
    private final java.lang.Object mLock;
    private final int mMessageTypeForSendKeyEvent;
    private final android.util.Pools.Pool<com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent> mPendingEventPool;
    private final java.util.Map<com.android.server.accessibility.KeyEventDispatcher.KeyEventFilter, java.util.ArrayList<com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent>> mPendingEventsMap;
    private final android.os.PowerManager mPowerManager;
    private final android.view.InputEventConsistencyVerifier mSentEventsVerifier;

    public interface KeyEventFilter {
        boolean onKeyEvent(android.view.KeyEvent keyEvent, int i);
    }

    public KeyEventDispatcher(android.os.Handler handlerToSendKeyEventsToInputFilter, int messageTypeForSendKeyEvent, java.lang.Object lock, android.os.PowerManager powerManager) {
        this.mPendingEventPool = new android.util.Pools.SimplePool(10);
        this.mPendingEventsMap = new android.util.ArrayMap();
        if (android.view.InputEventConsistencyVerifier.isInstrumentationEnabled()) {
            this.mSentEventsVerifier = new android.view.InputEventConsistencyVerifier(this, 0, com.android.server.accessibility.KeyEventDispatcher.class.getSimpleName());
        } else {
            this.mSentEventsVerifier = null;
        }
        this.mHandlerToSendKeyEventsToInputFilter = handlerToSendKeyEventsToInputFilter;
        this.mMessageTypeForSendKeyEvent = messageTypeForSendKeyEvent;
        this.mKeyEventTimeoutHandler = new android.os.Handler(handlerToSendKeyEventsToInputFilter.getLooper(), this);
        this.mLock = lock;
        this.mPowerManager = powerManager;
    }

    public KeyEventDispatcher(android.os.Handler handlerToSendKeyEventsToInputFilter, int messageTypeForSendKeyEvent, java.lang.Object lock, android.os.PowerManager powerManager, android.os.Handler timeoutHandler) {
        this(handlerToSendKeyEventsToInputFilter, messageTypeForSendKeyEvent, lock, powerManager);
        this.mKeyEventTimeoutHandler = timeoutHandler;
    }

    public boolean notifyKeyEventLocked(android.view.KeyEvent event, int policyFlags, java.util.List<? extends com.android.server.accessibility.KeyEventDispatcher.KeyEventFilter> keyEventFilters) {
        com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent pendingKeyEvent = null;
        android.view.KeyEvent localClone = android.view.KeyEvent.obtain(event);
        for (int i = 0; i < keyEventFilters.size(); i++) {
            com.android.server.accessibility.KeyEventDispatcher.KeyEventFilter keyEventFilter = keyEventFilters.get(i);
            if (keyEventFilter.onKeyEvent(localClone, localClone.getSequenceNumber())) {
                if (pendingKeyEvent == null) {
                    pendingKeyEvent = obtainPendingEventLocked(localClone, policyFlags);
                }
                java.util.ArrayList<com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent> pendingEventList = this.mPendingEventsMap.get(keyEventFilter);
                if (pendingEventList == null) {
                    pendingEventList = new java.util.ArrayList<>();
                    this.mPendingEventsMap.put(keyEventFilter, pendingEventList);
                }
                pendingEventList.add(pendingKeyEvent);
                pendingKeyEvent.referenceCount++;
            }
        }
        if (pendingKeyEvent == null) {
            localClone.recycle();
            return false;
        }
        android.os.Message message = this.mKeyEventTimeoutHandler.obtainMessage(1, pendingKeyEvent);
        this.mKeyEventTimeoutHandler.sendMessageDelayed(message, 500L);
        return true;
    }

    public void setOnKeyEventResult(com.android.server.accessibility.KeyEventDispatcher.KeyEventFilter keyEventFilter, boolean handled, int sequence) {
        synchronized (this.mLock) {
            com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent pendingEvent = removeEventFromListLocked(this.mPendingEventsMap.get(keyEventFilter), sequence);
            if (pendingEvent != null) {
                if (handled && !pendingEvent.handled) {
                    pendingEvent.handled = handled;
                    long identity = android.os.Binder.clearCallingIdentity();
                    try {
                        this.mPowerManager.userActivity(pendingEvent.event.getEventTime(), 3, 0);
                        android.os.Binder.restoreCallingIdentity(identity);
                    } catch (java.lang.Throwable th) {
                        android.os.Binder.restoreCallingIdentity(identity);
                        throw th;
                    }
                }
                removeReferenceToPendingEventLocked(pendingEvent);
            }
        }
    }

    public void flush(com.android.server.accessibility.KeyEventDispatcher.KeyEventFilter keyEventFilter) {
        synchronized (this.mLock) {
            java.util.List<com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent> pendingEvents = this.mPendingEventsMap.get(keyEventFilter);
            if (pendingEvents != null) {
                for (int i = 0; i < pendingEvents.size(); i++) {
                    com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent pendingEvent = pendingEvents.get(i);
                    removeReferenceToPendingEventLocked(pendingEvent);
                }
                this.mPendingEventsMap.remove(keyEventFilter);
            }
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(android.os.Message message) {
        if (message.what != 1) {
            android.util.Slog.w(LOG_TAG, "Unknown message: " + message.what);
            return false;
        }
        com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent pendingKeyEvent = (com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent) message.obj;
        synchronized (this.mLock) {
            for (java.util.ArrayList<com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent> listForService : this.mPendingEventsMap.values()) {
                if (listForService.remove(pendingKeyEvent) && removeReferenceToPendingEventLocked(pendingKeyEvent)) {
                    break;
                }
            }
        }
        return true;
    }

    private com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent obtainPendingEventLocked(android.view.KeyEvent event, int policyFlags) {
        com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent pendingEvent = (com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent) this.mPendingEventPool.acquire();
        if (pendingEvent == null) {
            pendingEvent = new com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent();
        }
        pendingEvent.event = event;
        pendingEvent.policyFlags = policyFlags;
        pendingEvent.referenceCount = 0;
        pendingEvent.handled = false;
        return pendingEvent;
    }

    private static com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent removeEventFromListLocked(java.util.List<com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent> listOfEvents, int sequence) {
        for (int i = 0; i < listOfEvents.size(); i++) {
            com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent pendingKeyEvent = listOfEvents.get(i);
            if (pendingKeyEvent.event.getSequenceNumber() == sequence) {
                listOfEvents.remove(pendingKeyEvent);
                return pendingKeyEvent;
            }
        }
        return null;
    }

    private boolean removeReferenceToPendingEventLocked(com.android.server.accessibility.KeyEventDispatcher.PendingKeyEvent pendingEvent) {
        int i = pendingEvent.referenceCount - 1;
        pendingEvent.referenceCount = i;
        if (i > 0) {
            return false;
        }
        this.mKeyEventTimeoutHandler.removeMessages(1, pendingEvent);
        if (!pendingEvent.handled) {
            if (this.mSentEventsVerifier != null) {
                this.mSentEventsVerifier.onKeyEvent(pendingEvent.event, 0);
            }
            int policyFlags = pendingEvent.policyFlags | 1073741824;
            this.mHandlerToSendKeyEventsToInputFilter.obtainMessage(this.mMessageTypeForSendKeyEvent, policyFlags, 0, pendingEvent.event).sendToTarget();
        } else {
            pendingEvent.event.recycle();
        }
        this.mPendingEventPool.release(pendingEvent);
        return true;
    }

    private static final class PendingKeyEvent {
        android.view.KeyEvent event;
        boolean handled;
        int policyFlags;
        int referenceCount;

        private PendingKeyEvent() {
        }
    }
}
