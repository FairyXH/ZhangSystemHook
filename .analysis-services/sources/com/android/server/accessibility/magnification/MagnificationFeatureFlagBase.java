package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
abstract class MagnificationFeatureFlagBase {
    abstract boolean getDefaultValue();

    abstract java.lang.String getFeatureName();

    abstract java.lang.String getNamespace();

    MagnificationFeatureFlagBase() {
    }

    private void clearCallingIdentifyAndTryCatch(final java.lang.Runnable tryBlock, java.lang.Runnable catchBlock) {
        try {
            android.os.Binder.withCleanCallingIdentity(new com.android.internal.util.FunctionalUtils.ThrowingRunnable() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda0
                public final void runOrThrow() {
                    tryBlock.run();
                }
            });
        } catch (java.lang.Throwable th) {
            catchBlock.run();
        }
    }

    public boolean isFeatureFlagEnabled() {
        final java.util.concurrent.atomic.AtomicBoolean isEnabled = new java.util.concurrent.atomic.AtomicBoolean(getDefaultValue());
        clearCallingIdentifyAndTryCatch(new java.lang.Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$isFeatureFlagEnabled$1(isEnabled);
            }
        }, new java.lang.Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$isFeatureFlagEnabled$2(isEnabled);
            }
        });
        return isEnabled.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$isFeatureFlagEnabled$1(java.util.concurrent.atomic.AtomicBoolean isEnabled) {
        isEnabled.set(android.provider.DeviceConfig.getBoolean(getNamespace(), getFeatureName(), getDefaultValue()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$isFeatureFlagEnabled$2(java.util.concurrent.atomic.AtomicBoolean isEnabled) {
        isEnabled.set(getDefaultValue());
    }

    public boolean setFeatureFlagEnabled(final boolean isEnabled) {
        final java.util.concurrent.atomic.AtomicBoolean success = new java.util.concurrent.atomic.AtomicBoolean(getDefaultValue());
        clearCallingIdentifyAndTryCatch(new java.lang.Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setFeatureFlagEnabled$3(success, isEnabled);
            }
        }, new java.lang.Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda7
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$setFeatureFlagEnabled$4(success);
            }
        });
        return success.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setFeatureFlagEnabled$3(java.util.concurrent.atomic.AtomicBoolean success, boolean isEnabled) {
        success.set(android.provider.DeviceConfig.setProperty(getNamespace(), getFeatureName(), java.lang.Boolean.toString(isEnabled), false));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setFeatureFlagEnabled$4(java.util.concurrent.atomic.AtomicBoolean success) {
        success.set(getDefaultValue());
    }

    public android.provider.DeviceConfig.OnPropertiesChangedListener addOnChangedListener(final java.util.concurrent.Executor executor, final java.lang.Runnable listener) {
        final android.provider.DeviceConfig.OnPropertiesChangedListener onChangedListener = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda1
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.lambda$addOnChangedListener$5(listener, properties);
            }
        };
        clearCallingIdentifyAndTryCatch(new java.lang.Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.lambda$addOnChangedListener$6(executor, onChangedListener);
            }
        }, new java.lang.Runnable() { // from class: com.android.server.accessibility.magnification.MagnificationFeatureFlagBase$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                com.android.server.accessibility.magnification.MagnificationFeatureFlagBase.lambda$addOnChangedListener$7();
            }
        });
        return onChangedListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addOnChangedListener$5(java.lang.Runnable listener, android.provider.DeviceConfig.Properties properties) {
        if (properties.getKeyset().contains(getFeatureName())) {
            listener.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addOnChangedListener$6(java.util.concurrent.Executor executor, android.provider.DeviceConfig.OnPropertiesChangedListener onChangedListener) {
        android.provider.DeviceConfig.addOnPropertiesChangedListener(getNamespace(), executor, onChangedListener);
    }

    static /* synthetic */ void lambda$addOnChangedListener$7() {
    }

    public void removeOnChangedListener(android.provider.DeviceConfig.OnPropertiesChangedListener onChangedListener) {
        android.provider.DeviceConfig.removeOnPropertiesChangedListener(onChangedListener);
    }
}
