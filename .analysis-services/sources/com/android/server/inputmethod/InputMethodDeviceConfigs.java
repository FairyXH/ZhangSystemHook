package com.android.server.inputmethod;

/* JADX INFO: loaded from: classes2.dex */
final class InputMethodDeviceConfigs {
    private final android.provider.DeviceConfig.OnPropertiesChangedListener mDeviceConfigChangedListener = new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.inputmethod.InputMethodDeviceConfigs$$ExternalSyntheticLambda0
        public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
            this.f$0.lambda$new$0(properties);
        }
    };
    private boolean mHideImeWhenNoEditorFocus = android.provider.DeviceConfig.getBoolean("input_method_manager", "hide_ime_when_no_editor_focus", true);

    InputMethodDeviceConfigs() {
        android.provider.DeviceConfig.addOnPropertiesChangedListener("input_method_manager", android.app.ActivityThread.currentApplication().getMainExecutor(), this.mDeviceConfigChangedListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(android.provider.DeviceConfig.Properties properties) {
        if (!"input_method_manager".equals(properties.getNamespace())) {
            return;
        }
        for (java.lang.String name : properties.getKeyset()) {
            if ("hide_ime_when_no_editor_focus".equals(name)) {
                this.mHideImeWhenNoEditorFocus = properties.getBoolean(name, true);
            }
        }
    }

    public boolean shouldHideImeWhenNoEditorFocus() {
        return this.mHideImeWhenNoEditorFocus;
    }

    void destroy() {
        android.provider.DeviceConfig.removeOnPropertiesChangedListener(this.mDeviceConfigChangedListener);
    }
}
