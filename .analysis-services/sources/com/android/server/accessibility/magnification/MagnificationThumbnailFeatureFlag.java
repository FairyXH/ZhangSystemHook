package com.android.server.accessibility.magnification;

/* JADX INFO: loaded from: classes.dex */
public class MagnificationThumbnailFeatureFlag extends com.android.server.accessibility.magnification.MagnificationFeatureFlagBase {
    private static final java.lang.String FEATURE_NAME_ENABLE_MAGNIFIER_THUMBNAIL = "enable_magnifier_thumbnail";
    private static final java.lang.String NAMESPACE = "accessibility";

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    public /* bridge */ /* synthetic */ android.provider.DeviceConfig.OnPropertiesChangedListener addOnChangedListener(java.util.concurrent.Executor executor, java.lang.Runnable runnable) {
        return super.addOnChangedListener(executor, runnable);
    }

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    public /* bridge */ /* synthetic */ boolean isFeatureFlagEnabled() {
        return super.isFeatureFlagEnabled();
    }

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    public /* bridge */ /* synthetic */ void removeOnChangedListener(android.provider.DeviceConfig.OnPropertiesChangedListener onPropertiesChangedListener) {
        super.removeOnChangedListener(onPropertiesChangedListener);
    }

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    public /* bridge */ /* synthetic */ boolean setFeatureFlagEnabled(boolean z) {
        return super.setFeatureFlagEnabled(z);
    }

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    java.lang.String getNamespace() {
        return NAMESPACE;
    }

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    java.lang.String getFeatureName() {
        return FEATURE_NAME_ENABLE_MAGNIFIER_THUMBNAIL;
    }

    @Override // com.android.server.accessibility.magnification.MagnificationFeatureFlagBase
    boolean getDefaultValue() {
        return false;
    }
}
