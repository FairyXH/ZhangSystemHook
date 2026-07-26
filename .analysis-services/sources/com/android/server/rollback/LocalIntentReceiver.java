package com.android.server.rollback;

/* JADX INFO: loaded from: classes3.dex */
class LocalIntentReceiver {
    final java.util.function.Consumer<android.content.Intent> mConsumer;
    private android.content.IIntentSender.Stub mLocalSender = new android.content.IIntentSender.Stub() { // from class: com.android.server.rollback.LocalIntentReceiver.1
        public void send(int code, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder whitelistToken, android.content.IIntentReceiver finishedReceiver, java.lang.String requiredPermission, android.os.Bundle options) {
            com.android.server.rollback.LocalIntentReceiver.this.mConsumer.accept(intent);
        }
    };

    LocalIntentReceiver(java.util.function.Consumer<android.content.Intent> consumer) {
        this.mConsumer = consumer;
    }

    android.content.IntentSender getIntentSender() {
        return new android.content.IntentSender(this.mLocalSender);
    }
}
