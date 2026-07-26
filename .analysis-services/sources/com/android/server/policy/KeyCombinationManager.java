package com.android.server.policy;

/* JADX INFO: loaded from: classes3.dex */
public class KeyCombinationManager {
    private static final long COMBINE_KEY_DELAY_MILLIS = 150;
    private static final java.lang.String TAG = "KeyCombinationManager";
    private final android.os.Handler mHandler;
    private com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule mTriggeredRule;
    private final android.util.SparseLongArray mDownTimes = new android.util.SparseLongArray(2);
    private final java.util.ArrayList<com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule> mRules = new java.util.ArrayList<>();
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.ArrayList<com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule> mActiveRules = new java.util.ArrayList<>();
    private com.android.server.policy.KeyCombinationManager.KeyCombinationManagerWrapper mWrapper = new com.android.server.policy.KeyCombinationManager.KeyCombinationManagerWrapper();
    private com.android.server.policy.IKeyCombinationManagerExt mExt = (com.android.server.policy.IKeyCombinationManagerExt) system.ext.loader.core.ExtLoader.type(com.android.server.policy.IKeyCombinationManagerExt.class).create();

    static abstract class TwoKeysCombinationRule {
        private int mKeyCode1;
        private int mKeyCode2;

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract void cancel();

        abstract void execute();

        TwoKeysCombinationRule(int keyCode1, int keyCode2) {
            this.mKeyCode1 = keyCode1;
            this.mKeyCode2 = keyCode2;
        }

        boolean preCondition() {
            return true;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean shouldInterceptKey(int keyCode) {
            if (keyCode == this.mKeyCode1 || keyCode == this.mKeyCode2) {
                android.util.Log.d(com.android.server.policy.KeyCombinationManager.TAG, "preCondition = " + preCondition() + " keycode = " + keyCode + " current rule is: " + toString());
            }
            return preCondition() && (keyCode == this.mKeyCode1 || keyCode == this.mKeyCode2);
        }

        boolean shouldInterceptKeys(android.util.SparseLongArray downTimes) {
            long now = android.os.SystemClock.uptimeMillis();
            android.util.Log.d(com.android.server.policy.KeyCombinationManager.TAG, "mKeyCode1 = " + downTimes.get(this.mKeyCode1) + " mKeyCode2 = " + downTimes.get(this.mKeyCode2) + " now = " + now + " this:" + toString());
            if (downTimes.get(this.mKeyCode1) > 0 && downTimes.get(this.mKeyCode2) > 0 && now <= downTimes.get(this.mKeyCode1) + 150 && now <= downTimes.get(this.mKeyCode2) + 150) {
                return true;
            }
            return false;
        }

        long getKeyInterceptDelayMs() {
            return 150L;
        }

        public java.lang.String toString() {
            return android.view.KeyEvent.keyCodeToString(this.mKeyCode1) + " + " + android.view.KeyEvent.keyCodeToString(this.mKeyCode2);
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule)) {
                return false;
            }
            com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule that = (com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule) o;
            if (this.mKeyCode1 == that.mKeyCode1 && this.mKeyCode2 == that.mKeyCode2) {
                return true;
            }
            return this.mKeyCode1 == that.mKeyCode2 && this.mKeyCode2 == that.mKeyCode1;
        }

        public int hashCode() {
            int result = this.mKeyCode1;
            return (result * 31) + this.mKeyCode2;
        }
    }

    KeyCombinationManager(android.os.Handler handler) {
        this.mHandler = handler;
    }

    void addRule(com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule rule) {
        if (this.mRules.contains(rule)) {
            throw new java.lang.IllegalArgumentException("Rule : " + rule + " already exists.");
        }
        this.mRules.add(rule);
    }

    void removeRule(com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule rule) {
        this.mRules.remove(rule);
    }

    boolean interceptKey(android.view.KeyEvent event, boolean interactive) {
        boolean zInterceptKeyLocked;
        synchronized (this.mLock) {
            zInterceptKeyLocked = interceptKeyLocked(event, interactive);
        }
        return zInterceptKeyLocked;
    }

    private boolean interceptKeyLocked(android.view.KeyEvent event, boolean interactive) {
        boolean down = event.getAction() == 0;
        final int keyCode = event.getKeyCode();
        int count = this.mActiveRules.size();
        long eventTime = event.getEventTime();
        android.util.Log.d(TAG, "down " + down + " keyCode " + keyCode + " count " + count + " interactive " + interactive + " eventTime " + eventTime + " mDownTimes: " + this.mDownTimes.toString());
        if (down && (this.mWrapper.getExtImpl().canAODScreenshot(event) || interactive)) {
            if (this.mDownTimes.size() > 0) {
                if (count > 0 && eventTime > this.mDownTimes.valueAt(0) + 150) {
                    forAllRules(this.mActiveRules, new java.util.function.Consumer() { // from class: com.android.server.policy.KeyCombinationManager$$ExternalSyntheticLambda2
                        @Override // java.util.function.Consumer
                        public final void accept(java.lang.Object obj) {
                            ((com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule) obj).cancel();
                        }
                    });
                    this.mActiveRules.clear();
                    android.util.Log.d(TAG, "exceed time from first key down, clear active rules");
                    return false;
                }
                if (count == 0) {
                    return false;
                }
            }
            if (this.mDownTimes.get(keyCode) != 0) {
                return false;
            }
            this.mDownTimes.put(keyCode, eventTime);
            if (this.mDownTimes.size() == 1) {
                this.mTriggeredRule = null;
                forAllRules(this.mRules, new java.util.function.Consumer() { // from class: com.android.server.policy.KeyCombinationManager$$ExternalSyntheticLambda3
                    @Override // java.util.function.Consumer
                    public final void accept(java.lang.Object obj) {
                        this.f$0.lambda$interceptKeyLocked$1(keyCode, (com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule) obj);
                    }
                });
            } else {
                if (this.mTriggeredRule != null) {
                    android.util.Log.d(TAG, "mTriggeredRule != null " + this.mTriggeredRule);
                    return true;
                }
                forAllActiveRules(new com.android.internal.util.ToBooleanFunction() { // from class: com.android.server.policy.KeyCombinationManager$$ExternalSyntheticLambda4
                    public final boolean apply(java.lang.Object obj) {
                        return this.f$0.lambda$interceptKeyLocked$2((com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule) obj);
                    }
                });
                this.mActiveRules.clear();
                if (this.mTriggeredRule != null) {
                    this.mActiveRules.add(this.mTriggeredRule);
                    return true;
                }
            }
        } else {
            this.mDownTimes.delete(keyCode);
            for (int index = count - 1; index >= 0; index--) {
                final com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule rule = this.mActiveRules.get(index);
                if (rule.shouldInterceptKey(keyCode)) {
                    android.os.Handler handler = this.mHandler;
                    java.util.Objects.requireNonNull(rule);
                    handler.post(new java.lang.Runnable() { // from class: com.android.server.policy.KeyCombinationManager$$ExternalSyntheticLambda5
                        @Override // java.lang.Runnable
                        public final void run() {
                            rule.cancel();
                        }
                    });
                    this.mActiveRules.remove(index);
                }
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$interceptKeyLocked$1(int keyCode, com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule rule) {
        if (rule.shouldInterceptKey(keyCode)) {
            this.mActiveRules.add(rule);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$interceptKeyLocked$2(final com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule rule) {
        if (!rule.shouldInterceptKeys(this.mDownTimes)) {
            return false;
        }
        android.util.Log.v(TAG, "Performing combination rule : " + rule);
        android.os.Handler handler = this.mHandler;
        java.util.Objects.requireNonNull(rule);
        handler.post(new java.lang.Runnable() { // from class: com.android.server.policy.KeyCombinationManager$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                rule.execute();
            }
        });
        this.mTriggeredRule = rule;
        return true;
    }

    long getKeyInterceptTimeout(int keyCode) {
        synchronized (this.mLock) {
            if (this.mDownTimes.get(keyCode) == 0) {
                return 0L;
            }
            long delayMs = 0;
            for (com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule rule : this.mActiveRules) {
                if (rule.shouldInterceptKey(keyCode)) {
                    delayMs = java.lang.Math.max(delayMs, rule.getKeyInterceptDelayMs());
                }
            }
            return this.mDownTimes.get(keyCode) + java.lang.Math.min(delayMs, 150L);
        }
    }

    boolean isKeyConsumed(android.view.KeyEvent event) {
        synchronized (this.mLock) {
            boolean z = false;
            if ((event.getFlags() & 1024) != 0) {
                return false;
            }
            if (this.mTriggeredRule != null && this.mTriggeredRule.shouldInterceptKey(event.getKeyCode())) {
                z = true;
            }
            return z;
        }
    }

    boolean isPowerKeyIntercepted() {
        synchronized (this.mLock) {
            if (forAllActiveRules(new com.android.internal.util.ToBooleanFunction() { // from class: com.android.server.policy.KeyCombinationManager$$ExternalSyntheticLambda0
                public final boolean apply(java.lang.Object obj) {
                    return ((com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule) obj).shouldInterceptKey(26);
                }
            })) {
                return this.mDownTimes.size() > 1 || this.mDownTimes.get(26) == 0;
            }
            return false;
        }
    }

    private void forAllRules(java.util.ArrayList<com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule> rules, java.util.function.Consumer<com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule> callback) {
        int count = rules.size();
        for (int index = 0; index < count; index++) {
            com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule rule = rules.get(index);
            callback.accept(rule);
        }
    }

    private boolean forAllActiveRules(com.android.internal.util.ToBooleanFunction<com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule> callback) {
        int count = this.mActiveRules.size();
        for (int index = 0; index < count; index++) {
            com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule rule = this.mActiveRules.get(index);
            if (callback.apply(rule)) {
                return true;
            }
        }
        return false;
    }

    void dump(final java.lang.String prefix, final java.io.PrintWriter pw) {
        pw.println(prefix + "KeyCombination rules:");
        forAllRules(this.mRules, new java.util.function.Consumer() { // from class: com.android.server.policy.KeyCombinationManager$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(java.lang.Object obj) {
                pw.println(prefix + "  " + ((com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule) obj));
            }
        });
    }

    public com.android.server.policy.IKeyCombinationManagerWrapper getWrapper() {
        return this.mWrapper;
    }

    private class KeyCombinationManagerWrapper implements com.android.server.policy.IKeyCombinationManagerWrapper {
        private KeyCombinationManagerWrapper() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public com.android.server.policy.IKeyCombinationManagerExt getExtImpl() {
            return com.android.server.policy.KeyCombinationManager.this.mExt;
        }

        @Override // com.android.server.policy.IKeyCombinationManagerWrapper
        public android.util.SparseLongArray getDownTimes() {
            return com.android.server.policy.KeyCombinationManager.this.mDownTimes;
        }

        @Override // com.android.server.policy.IKeyCombinationManagerWrapper
        public java.util.ArrayList<com.android.server.policy.KeyCombinationManager.TwoKeysCombinationRule> getRules() {
            return com.android.server.policy.KeyCombinationManager.this.mRules;
        }
    }
}
