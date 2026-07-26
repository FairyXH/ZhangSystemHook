package com.android.server.soundtrigger;

/* JADX INFO: loaded from: classes3.dex */
public class PhoneCallStateHandler {
    private final com.android.server.soundtrigger.PhoneCallStateHandler.Callback mCallback;
    private final android.telephony.SubscriptionManager mSubscriptionManager;
    private final android.telephony.TelephonyManager mTelephonyManager;
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.List<com.android.server.soundtrigger.PhoneCallStateHandler.MyCallStateListener> mListenerList = new java.util.ArrayList();
    private final java.util.concurrent.atomic.AtomicBoolean mIsPhoneCallOngoing = new java.util.concurrent.atomic.AtomicBoolean(false);
    private final java.util.concurrent.ExecutorService mExecutor = java.util.concurrent.Executors.newSingleThreadExecutor();

    public interface Callback {
        void onPhoneCallStateChanged(boolean z);
    }

    public PhoneCallStateHandler(android.telephony.SubscriptionManager subscriptionManager, android.telephony.TelephonyManager telephonyManager, com.android.server.soundtrigger.PhoneCallStateHandler.Callback callback) {
        this.mSubscriptionManager = ((android.telephony.SubscriptionManager) java.util.Objects.requireNonNull(subscriptionManager)).createForAllUserProfiles();
        this.mTelephonyManager = (android.telephony.TelephonyManager) java.util.Objects.requireNonNull(telephonyManager);
        this.mCallback = (com.android.server.soundtrigger.PhoneCallStateHandler.Callback) java.util.Objects.requireNonNull(callback);
        this.mSubscriptionManager.addOnSubscriptionsChangedListener(this.mExecutor, new android.telephony.SubscriptionManager.OnSubscriptionsChangedListener() { // from class: com.android.server.soundtrigger.PhoneCallStateHandler.1
            @Override // android.telephony.SubscriptionManager.OnSubscriptionsChangedListener
            public void onSubscriptionsChanged() {
                com.android.server.soundtrigger.PhoneCallStateHandler.this.updateTelephonyListeners();
            }

            public void onAddListenerFailed() {
                android.util.Slog.wtf("SoundTriggerPhoneCallStateHandler", "Failed to add a telephony listener");
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    final class MyCallStateListener extends android.telephony.TelephonyCallback implements android.telephony.TelephonyCallback.CallStateListener {
        final android.telephony.TelephonyManager mTelephonyManagerForSubId;

        MyCallStateListener(android.telephony.TelephonyManager telephonyManager) {
            this.mTelephonyManagerForSubId = telephonyManager;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$cleanup$0() {
            this.mTelephonyManagerForSubId.unregisterTelephonyCallback(this);
        }

        void cleanup() {
            com.android.server.soundtrigger.PhoneCallStateHandler.this.mExecutor.execute(new java.lang.Runnable() { // from class: com.android.server.soundtrigger.PhoneCallStateHandler$MyCallStateListener$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.lambda$cleanup$0();
                }
            });
        }

        @Override // android.telephony.TelephonyCallback.CallStateListener
        public void onCallStateChanged(int unused) {
            com.android.server.soundtrigger.PhoneCallStateHandler.this.updateCallStatus();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCallStatus() {
        boolean callStatus = checkCallStatus();
        if (this.mIsPhoneCallOngoing.compareAndSet(!callStatus, callStatus)) {
            this.mCallback.onPhoneCallStateChanged(callStatus);
        }
    }

    private boolean checkCallStatus() {
        java.util.List<android.telephony.SubscriptionInfo> infoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
        if (infoList == null) {
            return false;
        }
        if (!com.android.internal.telephony.flags.Flags.enforceTelephonyFeatureMapping()) {
            return infoList.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.soundtrigger.PhoneCallStateHandler$$ExternalSyntheticLambda3
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.soundtrigger.PhoneCallStateHandler.lambda$checkCallStatus$0((android.telephony.SubscriptionInfo) obj);
                }
            }).anyMatch(new java.util.function.Predicate() { // from class: com.android.server.soundtrigger.PhoneCallStateHandler$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return this.f$0.lambda$checkCallStatus$1((android.telephony.SubscriptionInfo) obj);
                }
            });
        }
        return infoList.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.soundtrigger.PhoneCallStateHandler$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return com.android.server.soundtrigger.PhoneCallStateHandler.lambda$checkCallStatus$2((android.telephony.SubscriptionInfo) obj);
            }
        }).anyMatch(new java.util.function.Predicate() { // from class: com.android.server.soundtrigger.PhoneCallStateHandler$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return this.f$0.lambda$checkCallStatus$3((android.telephony.SubscriptionInfo) obj);
            }
        });
    }

    static /* synthetic */ boolean lambda$checkCallStatus$0(android.telephony.SubscriptionInfo s) {
        return s.getSubscriptionId() != -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$checkCallStatus$1(android.telephony.SubscriptionInfo s) {
        return isCallOngoingFromState(this.mTelephonyManager.createForSubscriptionId(s.getSubscriptionId()).getCallStateForSubscription());
    }

    static /* synthetic */ boolean lambda$checkCallStatus$2(android.telephony.SubscriptionInfo s) {
        return s.getSubscriptionId() != -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$checkCallStatus$3(android.telephony.SubscriptionInfo s) {
        try {
            return isCallOngoingFromState(this.mTelephonyManager.createForSubscriptionId(s.getSubscriptionId()).getCallStateForSubscription());
        } catch (java.lang.UnsupportedOperationException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTelephonyListeners() {
        synchronized (this.mLock) {
            for (com.android.server.soundtrigger.PhoneCallStateHandler.MyCallStateListener listener : this.mListenerList) {
                listener.cleanup();
            }
            this.mListenerList.clear();
            java.util.List<android.telephony.SubscriptionInfo> infoList = this.mSubscriptionManager.getActiveSubscriptionInfoList();
            if (infoList == null) {
                return;
            }
            infoList.stream().filter(new java.util.function.Predicate() { // from class: com.android.server.soundtrigger.PhoneCallStateHandler$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(java.lang.Object obj) {
                    return com.android.server.soundtrigger.PhoneCallStateHandler.lambda$updateTelephonyListeners$4((android.telephony.SubscriptionInfo) obj);
                }
            }).map(new java.util.function.Function() { // from class: com.android.server.soundtrigger.PhoneCallStateHandler$$ExternalSyntheticLambda1
                @Override // java.util.function.Function
                public final java.lang.Object apply(java.lang.Object obj) {
                    return this.f$0.lambda$updateTelephonyListeners$5((android.telephony.SubscriptionInfo) obj);
                }
            }).forEach(new java.util.function.Consumer() { // from class: com.android.server.soundtrigger.PhoneCallStateHandler$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(java.lang.Object obj) {
                    this.f$0.lambda$updateTelephonyListeners$6((android.telephony.TelephonyManager) obj);
                }
            });
        }
    }

    static /* synthetic */ boolean lambda$updateTelephonyListeners$4(android.telephony.SubscriptionInfo s) {
        return s.getSubscriptionId() != -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.telephony.TelephonyManager lambda$updateTelephonyListeners$5(android.telephony.SubscriptionInfo s) {
        return this.mTelephonyManager.createForSubscriptionId(s.getSubscriptionId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$updateTelephonyListeners$6(android.telephony.TelephonyManager manager) {
        synchronized (this.mLock) {
            com.android.server.soundtrigger.PhoneCallStateHandler.MyCallStateListener listener = new com.android.server.soundtrigger.PhoneCallStateHandler.MyCallStateListener(manager);
            this.mListenerList.add(listener);
            manager.registerTelephonyCallback(this.mExecutor, listener);
        }
    }

    private static boolean isCallOngoingFromState(int callState) {
        switch (callState) {
            case 0:
            case 1:
                return false;
            case 2:
                return true;
            default:
                throw new java.lang.IllegalStateException("Received unexpected call state from Telephony Manager: " + callState);
        }
    }
}
