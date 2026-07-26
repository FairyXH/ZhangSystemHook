package com.android.server.am;

/* JADX INFO: loaded from: classes.dex */
final class CoreSettingsObserver extends android.database.ContentObserver {
    private static volatile boolean sDeviceConfigContextEntriesLoaded;
    private final com.android.server.am.ActivityManagerService mActivityManagerService;
    private final android.os.Bundle mCoreSettings;
    private static final java.lang.String LOG_TAG = com.android.server.am.CoreSettingsObserver.class.getSimpleName();
    static final java.util.Map<java.lang.String, java.lang.Class<?>> sSecureSettingToTypeMap = new java.util.HashMap();
    static final java.util.Map<java.lang.String, java.lang.Class<?>> sSystemSettingToTypeMap = new java.util.HashMap();
    static final java.util.Map<java.lang.String, java.lang.Class<?>> sGlobalSettingToTypeMap = new java.util.HashMap();
    static final java.util.List<com.android.server.am.CoreSettingsObserver.DeviceConfigEntry> sDeviceConfigEntries = new java.util.ArrayList();

    static {
        sSecureSettingToTypeMap.put("long_press_timeout", java.lang.Integer.TYPE);
        sSecureSettingToTypeMap.put("multi_press_timeout", java.lang.Integer.TYPE);
        sSecureSettingToTypeMap.put("key_repeat_timeout", java.lang.Integer.TYPE);
        sSecureSettingToTypeMap.put("key_repeat_delay", java.lang.Integer.TYPE);
        sSecureSettingToTypeMap.put("stylus_pointer_icon_enabled", java.lang.Integer.TYPE);
        sSystemSettingToTypeMap.put("time_12_24", java.lang.String.class);
        sGlobalSettingToTypeMap.put("debug_view_attributes", java.lang.Integer.TYPE);
        sGlobalSettingToTypeMap.put("debug_view_attributes_application_package", java.lang.String.class);
        sGlobalSettingToTypeMap.put("angle_debug_package", java.lang.String.class);
        sGlobalSettingToTypeMap.put("angle_gl_driver_all_angle", java.lang.Integer.TYPE);
        sGlobalSettingToTypeMap.put("angle_gl_driver_selection_pkgs", java.lang.String.class);
        sGlobalSettingToTypeMap.put("angle_gl_driver_selection_values", java.lang.String.class);
        sGlobalSettingToTypeMap.put("angle_egl_features", java.lang.String.class);
        sGlobalSettingToTypeMap.put("show_angle_in_use_dialog_box", java.lang.String.class);
        sGlobalSettingToTypeMap.put("enable_gpu_debug_layers", java.lang.Integer.TYPE);
        sGlobalSettingToTypeMap.put("gpu_debug_app", java.lang.String.class);
        sGlobalSettingToTypeMap.put("gpu_debug_layers", java.lang.String.class);
        sGlobalSettingToTypeMap.put("gpu_debug_layers_gles", java.lang.String.class);
        sGlobalSettingToTypeMap.put("gpu_debug_layer_app", java.lang.String.class);
        sGlobalSettingToTypeMap.put("updatable_driver_all_apps", java.lang.Integer.TYPE);
        sGlobalSettingToTypeMap.put("updatable_driver_production_opt_in_apps", java.lang.String.class);
        sGlobalSettingToTypeMap.put("updatable_driver_prerelease_opt_in_apps", java.lang.String.class);
        sGlobalSettingToTypeMap.put("updatable_driver_production_opt_out_apps", java.lang.String.class);
        sGlobalSettingToTypeMap.put("updatable_driver_production_denylist", java.lang.String.class);
        sGlobalSettingToTypeMap.put("updatable_driver_production_allowlist", java.lang.String.class);
        sGlobalSettingToTypeMap.put("updatable_driver_production_denylists", java.lang.String.class);
        sGlobalSettingToTypeMap.put("updatable_driver_sphal_libraries", java.lang.String.class);
        sDeviceConfigEntries.add(new com.android.server.am.CoreSettingsObserver.DeviceConfigEntry(com.android.server.am.IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__enable_cursor_drag_from_anywhere", "widget__enable_cursor_drag_from_anywhere", java.lang.Boolean.TYPE, true));
        sDeviceConfigEntries.add(new com.android.server.am.CoreSettingsObserver.DeviceConfigEntry(com.android.server.am.IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__min_angle_from_vertical_to_start_cursor_drag", "widget__min_angle_from_vertical_to_start_cursor_drag", java.lang.Integer.TYPE, 45));
        sDeviceConfigEntries.add(new com.android.server.am.CoreSettingsObserver.DeviceConfigEntry(com.android.server.am.IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__finger_to_cursor_distance", "widget__finger_to_cursor_distance", java.lang.Integer.TYPE, -1));
        sDeviceConfigEntries.add(new com.android.server.am.CoreSettingsObserver.DeviceConfigEntry(com.android.server.am.IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__enable_insertion_handle_gestures", "widget__enable_insertion_handle_gestures", java.lang.Boolean.TYPE, false));
        sDeviceConfigEntries.add(new com.android.server.am.CoreSettingsObserver.DeviceConfigEntry(com.android.server.am.IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__insertion_handle_delta_height", "widget__insertion_handle_delta_height", java.lang.Integer.TYPE, 25));
        sDeviceConfigEntries.add(new com.android.server.am.CoreSettingsObserver.DeviceConfigEntry(com.android.server.am.IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__insertion_handle_opacity", "widget__insertion_handle_opacity", java.lang.Integer.TYPE, 50));
        sDeviceConfigEntries.add(new com.android.server.am.CoreSettingsObserver.DeviceConfigEntry(com.android.server.am.IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__line_slop_ratio", "widget__line_slop_ratio", java.lang.Float.TYPE, java.lang.Float.valueOf(0.5f)));
        sDeviceConfigEntries.add(new com.android.server.am.CoreSettingsObserver.DeviceConfigEntry(com.android.server.am.IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__enable_new_magnifier", "widget__enable_new_magnifier", java.lang.Boolean.TYPE, false));
        sDeviceConfigEntries.add(new com.android.server.am.CoreSettingsObserver.DeviceConfigEntry(com.android.server.am.IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__magnifier_zoom_factor", "widget__magnifier_zoom_factor", java.lang.Float.TYPE, java.lang.Float.valueOf(1.5f)));
        sDeviceConfigEntries.add(new com.android.server.am.CoreSettingsObserver.DeviceConfigEntry(com.android.server.am.IOplusSceneManager.APP_SCENE_WIDGET, "CursorControlFeature__magnifier_aspect_ratio", "widget__magnifier_aspect_ratio", java.lang.Float.TYPE, java.lang.Float.valueOf(5.5f)));
        sDeviceConfigEntries.add(new com.android.server.am.CoreSettingsObserver.DeviceConfigEntry("text", "TextEditing__enable_new_context_menu", "text__enable_new_context_menu", java.lang.Boolean.TYPE, true));
        for (int i = 0; i < android.text.TextFlags.TEXT_ACONFIGS_FLAGS.length; i++) {
            java.lang.String flag = android.text.TextFlags.TEXT_ACONFIGS_FLAGS[i];
            boolean defaultValue = android.text.TextFlags.TEXT_ACONFIG_DEFAULT_VALUE[i];
            sDeviceConfigEntries.add(new com.android.server.am.CoreSettingsObserver.DeviceConfigEntry("text", flag, android.text.TextFlags.getKeyForFlag(flag), java.lang.Boolean.TYPE, java.lang.Boolean.valueOf(defaultValue)));
        }
        sDeviceConfigContextEntriesLoaded = false;
    }

    private static class DeviceConfigEntry<T> {
        java.lang.String coreSettingKey;
        T defaultValue;
        java.lang.String flag;
        java.lang.String namespace;
        java.lang.Class<T> type;

        DeviceConfigEntry(java.lang.String str, java.lang.String str2, java.lang.String str3, java.lang.Class<T> cls, T t) {
            this.namespace = str;
            this.flag = str2;
            this.coreSettingKey = str3;
            this.type = cls;
            this.defaultValue = (T) java.util.Objects.requireNonNull(t);
        }
    }

    public CoreSettingsObserver(com.android.server.am.ActivityManagerService activityManagerService) {
        super(activityManagerService.mHandler);
        this.mCoreSettings = new android.os.Bundle();
        if (!sDeviceConfigContextEntriesLoaded) {
            synchronized (sDeviceConfigEntries) {
                if (!sDeviceConfigContextEntriesLoaded) {
                    loadDeviceConfigContextEntries(activityManagerService.mContext);
                    sDeviceConfigContextEntriesLoaded = true;
                }
            }
        }
        this.mActivityManagerService = activityManagerService;
        beginObserveCoreSettings();
        sendCoreSettings();
    }

    private static void loadDeviceConfigContextEntries(android.content.Context context) {
        sDeviceConfigEntries.add(new com.android.server.am.CoreSettingsObserver.DeviceConfigEntry(com.android.server.am.IOplusSceneManager.APP_SCENE_WIDGET, "AnalogClockFeature__analog_clock_seconds_hand_fps", "widget__analog_clock_seconds_hand_fps", java.lang.Integer.TYPE, java.lang.Integer.valueOf(context.getResources().getInteger(android.R.integer.config_datause_threshold_bytes))));
    }

    public android.os.Bundle getCoreSettingsLocked() {
        return (android.os.Bundle) this.mCoreSettings.clone();
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean selfChange) {
        com.android.server.am.ActivityManagerService activityManagerService = this.mActivityManagerService;
        com.android.server.am.ActivityManagerService.boostPriorityForLockedSection();
        synchronized (activityManagerService) {
            try {
                sendCoreSettings();
            } catch (java.lang.Throwable th) {
                com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
                throw th;
            }
        }
        com.android.server.am.ActivityManagerService.resetPriorityAfterLockedSection();
    }

    private void sendCoreSettings() {
        populateSettings(this.mCoreSettings, sSecureSettingToTypeMap);
        populateSettings(this.mCoreSettings, sSystemSettingToTypeMap);
        populateSettings(this.mCoreSettings, sGlobalSettingToTypeMap);
        populateSettingsFromDeviceConfig();
        this.mActivityManagerService.onCoreSettingsChange(this.mCoreSettings);
    }

    private void beginObserveCoreSettings() {
        for (java.lang.String setting : sSecureSettingToTypeMap.keySet()) {
            android.net.Uri uri = android.provider.Settings.Secure.getUriFor(setting);
            this.mActivityManagerService.mContext.getContentResolver().registerContentObserver(uri, false, this);
        }
        for (java.lang.String setting2 : sSystemSettingToTypeMap.keySet()) {
            android.net.Uri uri2 = android.provider.Settings.System.getUriFor(setting2);
            this.mActivityManagerService.mContext.getContentResolver().registerContentObserver(uri2, false, this);
        }
        for (java.lang.String setting3 : sGlobalSettingToTypeMap.keySet()) {
            android.net.Uri uri3 = android.provider.Settings.Global.getUriFor(setting3);
            this.mActivityManagerService.mContext.getContentResolver().registerContentObserver(uri3, false, this);
        }
        java.util.HashSet<java.lang.String> deviceConfigNamespaces = new java.util.HashSet<>();
        for (com.android.server.am.CoreSettingsObserver.DeviceConfigEntry entry : sDeviceConfigEntries) {
            if (!deviceConfigNamespaces.contains(entry.namespace)) {
                android.provider.DeviceConfig.addOnPropertiesChangedListener(entry.namespace, android.app.ActivityThread.currentApplication().getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.am.CoreSettingsObserver$$ExternalSyntheticLambda0
                    public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                        this.f$0.lambda$beginObserveCoreSettings$0(properties);
                    }
                });
                deviceConfigNamespaces.add(entry.namespace);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$beginObserveCoreSettings$0(android.provider.DeviceConfig.Properties prop) {
        onChange(false);
    }

    void populateSettings(android.os.Bundle snapshot, java.util.Map<java.lang.String, java.lang.Class<?>> map) {
        java.lang.String value;
        android.content.Context context = this.mActivityManagerService.mContext;
        android.content.ContentResolver cr = context.getContentResolver();
        for (java.util.Map.Entry<java.lang.String, java.lang.Class<?>> entry : map.entrySet()) {
            java.lang.String setting = entry.getKey();
            if (map == sSecureSettingToTypeMap) {
                value = android.provider.Settings.Secure.getStringForUser(cr, setting, cr.getUserId());
            } else if (map == sSystemSettingToTypeMap) {
                value = android.provider.Settings.System.getStringForUser(cr, setting, cr.getUserId());
            } else {
                value = android.provider.Settings.Global.getString(cr, setting);
            }
            if (value == null) {
                snapshot.remove(setting);
            } else {
                java.lang.Class<?> type = entry.getValue();
                if (type == java.lang.String.class) {
                    snapshot.putString(setting, value);
                } else if (type == java.lang.Integer.TYPE) {
                    snapshot.putInt(setting, java.lang.Integer.parseInt(value));
                } else if (type == java.lang.Float.TYPE) {
                    snapshot.putFloat(setting, java.lang.Float.parseFloat(value));
                } else if (type == java.lang.Long.TYPE) {
                    snapshot.putLong(setting, java.lang.Long.parseLong(value));
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void populateSettingsFromDeviceConfig() {
        for (com.android.server.am.CoreSettingsObserver.DeviceConfigEntry deviceConfigEntry : sDeviceConfigEntries) {
            if (deviceConfigEntry.type == java.lang.String.class) {
                this.mCoreSettings.putString(deviceConfigEntry.coreSettingKey, android.provider.DeviceConfig.getString(deviceConfigEntry.namespace, deviceConfigEntry.flag, (java.lang.String) deviceConfigEntry.defaultValue));
            } else if (deviceConfigEntry.type == java.lang.Integer.TYPE) {
                this.mCoreSettings.putInt(deviceConfigEntry.coreSettingKey, android.provider.DeviceConfig.getInt(deviceConfigEntry.namespace, deviceConfigEntry.flag, ((java.lang.Integer) deviceConfigEntry.defaultValue).intValue()));
            } else if (deviceConfigEntry.type == java.lang.Float.TYPE) {
                this.mCoreSettings.putFloat(deviceConfigEntry.coreSettingKey, android.provider.DeviceConfig.getFloat(deviceConfigEntry.namespace, deviceConfigEntry.flag, ((java.lang.Float) deviceConfigEntry.defaultValue).floatValue()));
            } else if (deviceConfigEntry.type == java.lang.Long.TYPE) {
                this.mCoreSettings.putLong(deviceConfigEntry.coreSettingKey, android.provider.DeviceConfig.getLong(deviceConfigEntry.namespace, deviceConfigEntry.flag, ((java.lang.Long) deviceConfigEntry.defaultValue).longValue()));
            } else if (deviceConfigEntry.type == java.lang.Boolean.TYPE) {
                this.mCoreSettings.putInt(deviceConfigEntry.coreSettingKey, android.provider.DeviceConfig.getBoolean(deviceConfigEntry.namespace, deviceConfigEntry.flag, ((java.lang.Boolean) deviceConfigEntry.defaultValue).booleanValue()) ? 1 : 0);
            }
        }
    }
}
