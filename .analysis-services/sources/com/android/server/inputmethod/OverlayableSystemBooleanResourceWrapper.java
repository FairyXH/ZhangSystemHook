package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class OverlayableSystemBooleanResourceWrapper implements java.lang.AutoCloseable {
    private static final java.lang.String SYSTEM_PACKAGE_NAME = "android";
    private static final java.lang.String TAG = "OverlayableSystemBooleanResourceWrapper";
    private final java.util.concurrent.atomic.AtomicReference<java.lang.Runnable> mCleanerRef;
    private final int mUserId;
    private final java.util.concurrent.atomic.AtomicBoolean mValueRef;

    static com.android.server.inputmethod.OverlayableSystemBooleanResourceWrapper create(final android.content.Context userContext, final int boolResId, android.os.Handler handler, final java.util.function.Consumer<com.android.server.inputmethod.OverlayableSystemBooleanResourceWrapper> callback) {
        final java.util.concurrent.atomic.AtomicBoolean valueRef = new java.util.concurrent.atomic.AtomicBoolean(evaluate(userContext, boolResId));
        java.util.concurrent.atomic.AtomicReference<java.lang.Runnable> cleanerRef = new java.util.concurrent.atomic.AtomicReference<>();
        final com.android.server.inputmethod.OverlayableSystemBooleanResourceWrapper object = new com.android.server.inputmethod.OverlayableSystemBooleanResourceWrapper(userContext.getUserId(), valueRef, cleanerRef);
        android.content.IntentFilter intentFilter = new android.content.IntentFilter("android.intent.action.OVERLAY_CHANGED");
        intentFilter.addDataScheme("package");
        intentFilter.addDataSchemeSpecificPart("android", 0);
        final android.content.BroadcastReceiver broadcastReceiver = new android.content.BroadcastReceiver() { // from class: com.android.server.inputmethod.OverlayableSystemBooleanResourceWrapper.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(android.content.Context context, android.content.Intent intent) {
                boolean newValue = com.android.server.inputmethod.OverlayableSystemBooleanResourceWrapper.evaluate(userContext, boolResId);
                if (newValue != valueRef.getAndSet(newValue)) {
                    callback.accept(object);
                }
            }
        };
        userContext.registerReceiver(broadcastReceiver, intentFilter, null, handler, 4);
        cleanerRef.set(new java.lang.Runnable() { // from class: com.android.server.inputmethod.OverlayableSystemBooleanResourceWrapper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                userContext.unregisterReceiver(broadcastReceiver);
            }
        });
        valueRef.set(evaluate(userContext, boolResId));
        return object;
    }

    private OverlayableSystemBooleanResourceWrapper(int userId, java.util.concurrent.atomic.AtomicBoolean valueRef, java.util.concurrent.atomic.AtomicReference<java.lang.Runnable> cleanerRef) {
        this.mUserId = userId;
        this.mValueRef = valueRef;
        this.mCleanerRef = cleanerRef;
    }

    boolean get() {
        return this.mValueRef.get();
    }

    int getUserId() {
        return this.mUserId;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean evaluate(android.content.Context context, int boolResId) {
        try {
            return context.getPackageManager().getResourcesForApplication("android").getBoolean(boolResId);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            android.util.Slog.e(TAG, "getResourcesForApplication(\"android\") failed", e);
            return false;
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        java.lang.Runnable cleaner = this.mCleanerRef.getAndSet(null);
        if (cleaner != null) {
            cleaner.run();
        }
    }
}
