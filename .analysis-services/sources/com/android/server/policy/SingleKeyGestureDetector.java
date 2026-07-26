package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public final class SingleKeyGestureDetector {
    private static final int MSG_KEY_DELAYED_PRESS = 2;
    private static final int MSG_KEY_LONG_PRESS = 0;
    private static final int MSG_KEY_UP = 3;
    private static final int MSG_KEY_VERY_LONG_PRESS = 1;
    private static final java.lang.String TAG = "SingleKeyGesture";
    static long sDefaultLongPressTimeout;
    static long sDefaultVeryLongPressTimeout;
    private final android.os.Handler mHandler;
    private int mKeyPressCounter;
    private static final boolean DEBUG = com.android.server.policy.PhoneWindowManager.DEBUG_INPUT;
    static final long MULTI_PRESS_TIMEOUT = android.view.ViewConfiguration.getMultiPressTimeout();
    private boolean mBeganFromNonInteractive = false;
    private boolean mBeganFromDefaultDisplayOn = false;
    private final java.util.ArrayList<com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule> mRules = new java.util.ArrayList<>();
    private com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule mActiveRule = null;
    private int mDownKeyCode = 0;
    private boolean mHandledByLongPress = false;
    private long mLastDownTime = 0;
    com.android.server.policy.ISingleKeyGestureDetectorExt mSingleKeyGestureDetectorExt = (com.android.server.policy.ISingleKeyGestureDetectorExt) system.ext.loader.core.ExtLoader.type(com.android.server.policy.ISingleKeyGestureDetectorExt.class).create();

    static abstract class SingleKeyRule {
        private final int mKeyCode;

        abstract void onPress(long j, int i);

        SingleKeyRule(int keyCode) {
            this.mKeyCode = keyCode;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean shouldInterceptKey(int keyCode) {
            return keyCode == this.mKeyCode;
        }

        boolean supportLongPress() {
            return false;
        }

        boolean supportVeryLongPress() {
            return false;
        }

        int getMaxMultiPressCount() {
            return 1;
        }

        void onMultiPress(long downTime, int count, int displayId) {
        }

        long getLongPressTimeoutMs() {
            return com.android.server.policy.SingleKeyGestureDetector.sDefaultLongPressTimeout;
        }

        void onLongPress(long eventTime) {
        }

        long getVeryLongPressTimeoutMs() {
            return com.android.server.policy.SingleKeyGestureDetector.sDefaultVeryLongPressTimeout;
        }

        void onVeryLongPress(long eventTime) {
        }

        void onKeyUp(long eventTime, int pressCount, int displayId) {
        }

        public java.lang.String toString() {
            return "KeyCode=" + android.view.KeyEvent.keyCodeToString(this.mKeyCode) + ", LongPress=" + supportLongPress() + ", VeryLongPress=" + supportVeryLongPress() + ", MaxMultiPressCount=" + getMaxMultiPressCount();
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule)) {
                return false;
            }
            com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule that = (com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule) o;
            return this.mKeyCode == that.mKeyCode;
        }

        public int hashCode() {
            return this.mKeyCode;
        }
    }

    private static final class MessageObject extends java.lang.Record {
        private final com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule activeRule;
        private final int displayId;
        private final int keyCode;
        private final int pressCount;

        private MessageObject(com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule activeRule, int keyCode, int pressCount, int displayId) {
            this.activeRule = activeRule;
            this.keyCode = keyCode;
            this.pressCount = pressCount;
            this.displayId = displayId;
        }

        public com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule activeRule() {
            return this.activeRule;
        }

        public int displayId() {
            return this.displayId;
        }

        @Override // java.lang.Record
        public final boolean equals(java.lang.Object o) {
            return (boolean) java.lang.runtime.ObjectMethods.bootstrap(java.lang.invoke.MethodHandles.lookup(), "equals", java.lang.invoke.MethodType.methodType(java.lang.Boolean.TYPE, com.android.server.policy.SingleKeyGestureDetector.MessageObject.class, java.lang.Object.class), com.android.server.policy.SingleKeyGestureDetector.MessageObject.class, "activeRule;keyCode;pressCount;displayId", "FIELD:Lcom/android/server/policy/SingleKeyGestureDetector$MessageObject;->activeRule:Lcom/android/server/policy/SingleKeyGestureDetector$SingleKeyRule;", "FIELD:Lcom/android/server/policy/SingleKeyGestureDetector$MessageObject;->keyCode:I", "FIELD:Lcom/android/server/policy/SingleKeyGestureDetector$MessageObject;->pressCount:I", "FIELD:Lcom/android/server/policy/SingleKeyGestureDetector$MessageObject;->displayId:I").dynamicInvoker().invoke(this, o) /* invoke-custom */;
        }

        @Override // java.lang.Record
        public final int hashCode() {
            return (int) java.lang.runtime.ObjectMethods.bootstrap(java.lang.invoke.MethodHandles.lookup(), "hashCode", java.lang.invoke.MethodType.methodType(java.lang.Integer.TYPE, com.android.server.policy.SingleKeyGestureDetector.MessageObject.class), com.android.server.policy.SingleKeyGestureDetector.MessageObject.class, "activeRule;keyCode;pressCount;displayId", "FIELD:Lcom/android/server/policy/SingleKeyGestureDetector$MessageObject;->activeRule:Lcom/android/server/policy/SingleKeyGestureDetector$SingleKeyRule;", "FIELD:Lcom/android/server/policy/SingleKeyGestureDetector$MessageObject;->keyCode:I", "FIELD:Lcom/android/server/policy/SingleKeyGestureDetector$MessageObject;->pressCount:I", "FIELD:Lcom/android/server/policy/SingleKeyGestureDetector$MessageObject;->displayId:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }

        public int keyCode() {
            return this.keyCode;
        }

        public int pressCount() {
            return this.pressCount;
        }

        @Override // java.lang.Record
        public final java.lang.String toString() {
            return (java.lang.String) java.lang.runtime.ObjectMethods.bootstrap(java.lang.invoke.MethodHandles.lookup(), "toString", java.lang.invoke.MethodType.methodType(java.lang.String.class, com.android.server.policy.SingleKeyGestureDetector.MessageObject.class), com.android.server.policy.SingleKeyGestureDetector.MessageObject.class, "activeRule;keyCode;pressCount;displayId", "FIELD:Lcom/android/server/policy/SingleKeyGestureDetector$MessageObject;->activeRule:Lcom/android/server/policy/SingleKeyGestureDetector$SingleKeyRule;", "FIELD:Lcom/android/server/policy/SingleKeyGestureDetector$MessageObject;->keyCode:I", "FIELD:Lcom/android/server/policy/SingleKeyGestureDetector$MessageObject;->pressCount:I", "FIELD:Lcom/android/server/policy/SingleKeyGestureDetector$MessageObject;->displayId:I").dynamicInvoker().invoke(this) /* invoke-custom */;
        }
    }

    static com.android.server.policy.SingleKeyGestureDetector get(android.content.Context context, android.os.Looper looper) {
        com.android.server.policy.SingleKeyGestureDetector detector = new com.android.server.policy.SingleKeyGestureDetector(looper);
        sDefaultLongPressTimeout = context.getResources().getInteger(android.R.integer.config_emergency_call_wait_for_connection_timeout_millis);
        sDefaultVeryLongPressTimeout = context.getResources().getInteger(android.R.integer.config_shutdownBatteryTemperature);
        return detector;
    }

    private SingleKeyGestureDetector(android.os.Looper looper) {
        this.mHandler = new com.android.server.policy.SingleKeyGestureDetector.KeyHandler(looper);
    }

    void addRule(com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule rule) {
        if (this.mRules.contains(rule)) {
            throw new java.lang.IllegalArgumentException("Rule : " + rule + " already exists.");
        }
        this.mRules.add(rule);
    }

    void removeRule(com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule rule) {
        this.mRules.remove(rule);
    }

    void interceptKey(android.view.KeyEvent event, boolean interactive, boolean defaultDisplayOn) {
        if (event.getAction() == 0) {
            if (this.mDownKeyCode == 0 || this.mDownKeyCode != event.getKeyCode()) {
                this.mBeganFromNonInteractive = !interactive;
                this.mBeganFromDefaultDisplayOn = defaultDisplayOn;
            }
            interceptKeyDown(event);
            return;
        }
        interceptKeyUp(event);
    }

    private void interceptKeyDown(android.view.KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (this.mDownKeyCode == keyCode) {
            if (this.mActiveRule != null && (event.getFlags() & 128) != 0 && this.mActiveRule.supportLongPress() && !this.mHandledByLongPress) {
                if (DEBUG) {
                    android.util.Log.i(TAG, "Long press key " + android.view.KeyEvent.keyCodeToString(keyCode));
                }
                this.mHandledByLongPress = true;
                this.mHandler.removeMessages(0);
                this.mHandler.removeMessages(1);
                com.android.server.policy.SingleKeyGestureDetector.MessageObject object = new com.android.server.policy.SingleKeyGestureDetector.MessageObject(this.mActiveRule, keyCode, 1, event.getDisplayId());
                android.os.Message msg = this.mHandler.obtainMessage(0, object);
                msg.setAsynchronous(true);
                this.mHandler.sendMessage(msg);
                return;
            }
            return;
        }
        if (this.mDownKeyCode != 0 || (this.mActiveRule != null && !this.mActiveRule.shouldInterceptKey(keyCode))) {
            if (DEBUG) {
                android.util.Log.i(TAG, "Press another key " + android.view.KeyEvent.keyCodeToString(keyCode));
            }
            reset();
        }
        this.mDownKeyCode = keyCode;
        if (this.mActiveRule == null) {
            int count = this.mRules.size();
            int index = 0;
            while (true) {
                if (index >= count) {
                    break;
                }
                com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule rule = this.mRules.get(index);
                if (!rule.shouldInterceptKey(keyCode)) {
                    index++;
                } else {
                    if (DEBUG) {
                        android.util.Log.i(TAG, "Intercept key by rule " + rule);
                    }
                    this.mActiveRule = rule;
                }
            }
            this.mLastDownTime = 0L;
        }
        if (this.mActiveRule == null) {
            return;
        }
        long keyDownInterval = event.getDownTime() - this.mLastDownTime;
        this.mLastDownTime = event.getDownTime();
        if (keyDownInterval >= MULTI_PRESS_TIMEOUT) {
            this.mKeyPressCounter = 1;
        } else {
            this.mKeyPressCounter++;
        }
        if (this.mKeyPressCounter == 1) {
            if (this.mActiveRule.supportLongPress()) {
                com.android.server.policy.SingleKeyGestureDetector.MessageObject object2 = new com.android.server.policy.SingleKeyGestureDetector.MessageObject(this.mActiveRule, keyCode, this.mKeyPressCounter, event.getDisplayId());
                android.os.Message msg2 = this.mHandler.obtainMessage(0, object2);
                msg2.setAsynchronous(true);
                this.mHandler.sendMessageDelayed(msg2, this.mActiveRule.getLongPressTimeoutMs());
            }
            if (this.mActiveRule.supportVeryLongPress()) {
                com.android.server.policy.SingleKeyGestureDetector.MessageObject object3 = new com.android.server.policy.SingleKeyGestureDetector.MessageObject(this.mActiveRule, keyCode, this.mKeyPressCounter, event.getDisplayId());
                android.os.Message msg3 = this.mHandler.obtainMessage(1, object3);
                msg3.setAsynchronous(true);
                this.mHandler.sendMessageDelayed(msg3, this.mSingleKeyGestureDetectorExt.modifyPressTimeout(1, this.mActiveRule.getVeryLongPressTimeoutMs(), event));
                return;
            }
            return;
        }
        this.mHandler.removeMessages(0);
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(2);
        if (this.mActiveRule.getMaxMultiPressCount() > 1 && this.mKeyPressCounter == this.mActiveRule.getMaxMultiPressCount()) {
            if (DEBUG) {
                android.util.Log.i(TAG, "Trigger multi press " + this.mActiveRule.toString() + " for it reached the max count " + this.mKeyPressCounter);
            }
            com.android.server.policy.SingleKeyGestureDetector.MessageObject object4 = new com.android.server.policy.SingleKeyGestureDetector.MessageObject(this.mActiveRule, keyCode, this.mKeyPressCounter, event.getDisplayId());
            android.os.Message msg4 = this.mHandler.obtainMessage(2, object4);
            msg4.setAsynchronous(true);
            this.mHandler.sendMessage(msg4);
        }
    }

    private boolean interceptKeyUp(android.view.KeyEvent event) {
        this.mDownKeyCode = 0;
        if (this.mActiveRule == null) {
            return false;
        }
        if (!this.mHandledByLongPress) {
            long eventTime = event.getEventTime();
            if (eventTime < this.mLastDownTime + this.mActiveRule.getLongPressTimeoutMs()) {
                this.mHandler.removeMessages(0);
            } else {
                this.mHandledByLongPress = this.mActiveRule.supportLongPress();
            }
            if (eventTime < this.mLastDownTime + this.mActiveRule.getVeryLongPressTimeoutMs()) {
                this.mHandler.removeMessages(1);
            } else {
                this.mHandledByLongPress |= this.mActiveRule.supportVeryLongPress();
            }
        }
        if (this.mHandledByLongPress) {
            this.mHandledByLongPress = false;
            this.mKeyPressCounter = 0;
            this.mActiveRule = null;
            return true;
        }
        if (event.getKeyCode() == this.mActiveRule.mKeyCode) {
            com.android.server.policy.SingleKeyGestureDetector.MessageObject object = new com.android.server.policy.SingleKeyGestureDetector.MessageObject(this.mActiveRule, this.mActiveRule.mKeyCode, this.mKeyPressCounter, event.getDisplayId());
            android.os.Message msgKeyUp = this.mHandler.obtainMessage(3, object);
            msgKeyUp.setAsynchronous(true);
            this.mHandler.sendMessage(msgKeyUp);
            if (this.mActiveRule.getMaxMultiPressCount() == 1) {
                if (DEBUG) {
                    android.util.Log.i(TAG, "press key " + android.view.KeyEvent.keyCodeToString(event.getKeyCode()));
                }
                com.android.server.policy.SingleKeyGestureDetector.MessageObject object2 = new com.android.server.policy.SingleKeyGestureDetector.MessageObject(this.mActiveRule, this.mActiveRule.mKeyCode, 1, event.getDisplayId());
                android.os.Message msg = this.mHandler.obtainMessage(2, object2);
                msg.setAsynchronous(true);
                this.mHandler.sendMessage(msg);
                this.mActiveRule = null;
                return true;
            }
            if (this.mKeyPressCounter < this.mActiveRule.getMaxMultiPressCount()) {
                com.android.server.policy.SingleKeyGestureDetector.MessageObject object3 = new com.android.server.policy.SingleKeyGestureDetector.MessageObject(this.mActiveRule, this.mActiveRule.mKeyCode, this.mKeyPressCounter, event.getDisplayId());
                android.os.Message msg2 = this.mHandler.obtainMessage(2, object3);
                msg2.setAsynchronous(true);
                this.mHandler.sendMessageDelayed(msg2, MULTI_PRESS_TIMEOUT);
            }
            this.mSingleKeyGestureDetectorExt.endHookInterceptKeyUp();
            return true;
        }
        reset();
        return false;
    }

    int getKeyPressCounter(int keyCode) {
        if (this.mActiveRule != null && this.mActiveRule.mKeyCode == keyCode) {
            return this.mKeyPressCounter;
        }
        return 0;
    }

    void reset() {
        if (this.mActiveRule != null) {
            if (this.mDownKeyCode != 0) {
                this.mHandler.removeMessages(0);
                this.mHandler.removeMessages(1);
            }
            if (this.mKeyPressCounter > 0) {
                this.mHandler.removeMessages(2);
                this.mKeyPressCounter = 0;
            }
            this.mActiveRule = null;
        }
        this.mHandledByLongPress = false;
        this.mDownKeyCode = 0;
    }

    boolean isKeyIntercepted(int keyCode) {
        return this.mActiveRule != null && this.mActiveRule.shouldInterceptKey(keyCode);
    }

    boolean beganFromNonInteractive() {
        return this.mBeganFromNonInteractive;
    }

    boolean beganFromDefaultDisplayOn() {
        return this.mBeganFromDefaultDisplayOn;
    }

    void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.println(prefix + "SingleKey rules:");
        for (com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule rule : this.mRules) {
            pw.println(prefix + "  " + rule);
        }
    }

    private class KeyHandler extends android.os.Handler {
        KeyHandler(android.os.Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            com.android.server.policy.SingleKeyGestureDetector.MessageObject object = (com.android.server.policy.SingleKeyGestureDetector.MessageObject) msg.obj;
            com.android.server.policy.SingleKeyGestureDetector.SingleKeyRule rule = object.activeRule;
            if (rule == null) {
                android.util.Log.wtf(com.android.server.policy.SingleKeyGestureDetector.TAG, "No active rule.");
            }
            int keyCode = object.keyCode;
            int pressCount = object.pressCount;
            int displayId = object.displayId;
            switch (msg.what) {
                case 0:
                    if (com.android.server.policy.SingleKeyGestureDetector.DEBUG) {
                        android.util.Log.i(com.android.server.policy.SingleKeyGestureDetector.TAG, "Detect long press " + android.view.KeyEvent.keyCodeToString(keyCode));
                    }
                    rule.onLongPress(com.android.server.policy.SingleKeyGestureDetector.this.mLastDownTime);
                    break;
                case 1:
                    if (com.android.server.policy.SingleKeyGestureDetector.DEBUG) {
                        android.util.Log.i(com.android.server.policy.SingleKeyGestureDetector.TAG, "Detect very long press " + android.view.KeyEvent.keyCodeToString(keyCode));
                    }
                    rule.onVeryLongPress(com.android.server.policy.SingleKeyGestureDetector.this.mLastDownTime);
                    break;
                case 2:
                    if (com.android.server.policy.SingleKeyGestureDetector.DEBUG) {
                        android.util.Log.i(com.android.server.policy.SingleKeyGestureDetector.TAG, "Detect press " + android.view.KeyEvent.keyCodeToString(keyCode) + " on display " + displayId + ", count " + pressCount);
                    }
                    if (pressCount == 1) {
                        rule.onPress(com.android.server.policy.SingleKeyGestureDetector.this.mLastDownTime, displayId);
                    } else {
                        rule.onMultiPress(com.android.server.policy.SingleKeyGestureDetector.this.mLastDownTime, pressCount, displayId);
                    }
                    break;
                case 3:
                    if (com.android.server.policy.SingleKeyGestureDetector.DEBUG) {
                        android.util.Log.i(com.android.server.policy.SingleKeyGestureDetector.TAG, "Detect key up " + android.view.KeyEvent.keyCodeToString(keyCode) + " on display " + displayId);
                    }
                    rule.onKeyUp(com.android.server.policy.SingleKeyGestureDetector.this.mLastDownTime, pressCount, displayId);
                    break;
            }
        }
    }
}
