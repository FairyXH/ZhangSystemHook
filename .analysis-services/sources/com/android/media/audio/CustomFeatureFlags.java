package com.android.media.audio;

/* JADX INFO: loaded from: classes.dex */
public class CustomFeatureFlags implements com.android.media.audio.FeatureFlags {
    private java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.media.audio.FeatureFlags>> mGetValueImpl;
    private java.util.Set<java.lang.String> mReadOnlyFlagsSet = new java.util.HashSet(java.util.Arrays.asList(com.android.media.audio.Flags.FLAG_ABS_VOLUME_INDEX_FIX, com.android.media.audio.Flags.FLAG_ALARM_MIN_VOLUME_ZERO, com.android.media.audio.Flags.FLAG_AS_DEVICE_CONNECTION_FAILURE, com.android.media.audio.Flags.FLAG_AUDIOSERVER_PERMISSIONS, com.android.media.audio.Flags.FLAG_BLUETOOTH_MAC_ADDRESS_ANONYMIZATION, com.android.media.audio.Flags.FLAG_DISABLE_PRESCALE_ABSOLUTE_VOLUME, com.android.media.audio.Flags.FLAG_DSA_OVER_BT_LE_AUDIO, com.android.media.audio.Flags.FLAG_MUSIC_FX_EDGE_TO_EDGE, com.android.media.audio.Flags.FLAG_PORT_TO_PIID_SIMPLIFICATION, com.android.media.audio.Flags.FLAG_REPLACE_STREAM_BT_SCO, com.android.media.audio.Flags.FLAG_RINGER_MODE_AFFECTS_ALARM, com.android.media.audio.Flags.FLAG_SET_STREAM_VOLUME_ORDER, com.android.media.audio.Flags.FLAG_SPATIALIZER_OFFLOAD, com.android.media.audio.Flags.FLAG_SPATIALIZER_UPMIX, com.android.media.audio.Flags.FLAG_STEREO_SPATIALIZATION, com.android.media.audio.Flags.FLAG_VGS_VSS_SYNC_MUTE_ORDER, com.android.media.audio.Flags.FLAG_VOLUME_REFACTORING, ""));

    public CustomFeatureFlags(java.util.function.BiPredicate<java.lang.String, java.util.function.Predicate<com.android.media.audio.FeatureFlags>> getValueImpl) {
        this.mGetValueImpl = getValueImpl;
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean absVolumeIndexFix() {
        return getValue(com.android.media.audio.Flags.FLAG_ABS_VOLUME_INDEX_FIX, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda9
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).absVolumeIndexFix();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean alarmMinVolumeZero() {
        return getValue(com.android.media.audio.Flags.FLAG_ALARM_MIN_VOLUME_ZERO, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda14
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).alarmMinVolumeZero();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean asDeviceConnectionFailure() {
        return getValue(com.android.media.audio.Flags.FLAG_AS_DEVICE_CONNECTION_FAILURE, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda10
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).asDeviceConnectionFailure();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean audioserverPermissions() {
        return getValue(com.android.media.audio.Flags.FLAG_AUDIOSERVER_PERMISSIONS, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).audioserverPermissions();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean bluetoothMacAddressAnonymization() {
        return getValue(com.android.media.audio.Flags.FLAG_BLUETOOTH_MAC_ADDRESS_ANONYMIZATION, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda11
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).bluetoothMacAddressAnonymization();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean disablePrescaleAbsoluteVolume() {
        return getValue(com.android.media.audio.Flags.FLAG_DISABLE_PRESCALE_ABSOLUTE_VOLUME, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda2
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).disablePrescaleAbsoluteVolume();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean dsaOverBtLeAudio() {
        return getValue(com.android.media.audio.Flags.FLAG_DSA_OVER_BT_LE_AUDIO, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda3
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).dsaOverBtLeAudio();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean musicFxEdgeToEdge() {
        return getValue(com.android.media.audio.Flags.FLAG_MUSIC_FX_EDGE_TO_EDGE, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda12
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).musicFxEdgeToEdge();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean portToPiidSimplification() {
        return getValue(com.android.media.audio.Flags.FLAG_PORT_TO_PIID_SIMPLIFICATION, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda8
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).portToPiidSimplification();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean replaceStreamBtSco() {
        return getValue(com.android.media.audio.Flags.FLAG_REPLACE_STREAM_BT_SCO, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda4
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).replaceStreamBtSco();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean ringerModeAffectsAlarm() {
        return getValue(com.android.media.audio.Flags.FLAG_RINGER_MODE_AFFECTS_ALARM, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).ringerModeAffectsAlarm();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean setStreamVolumeOrder() {
        return getValue(com.android.media.audio.Flags.FLAG_SET_STREAM_VOLUME_ORDER, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda13
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).setStreamVolumeOrder();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean spatializerOffload() {
        return getValue(com.android.media.audio.Flags.FLAG_SPATIALIZER_OFFLOAD, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda15
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).spatializerOffload();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean spatializerUpmix() {
        return getValue(com.android.media.audio.Flags.FLAG_SPATIALIZER_UPMIX, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda7
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).spatializerUpmix();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean stereoSpatialization() {
        return getValue(com.android.media.audio.Flags.FLAG_STEREO_SPATIALIZATION, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda16
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).stereoSpatialization();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean vgsVssSyncMuteOrder() {
        return getValue(com.android.media.audio.Flags.FLAG_VGS_VSS_SYNC_MUTE_ORDER, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).vgsVssSyncMuteOrder();
            }
        });
    }

    @Override // com.android.media.audio.FeatureFlags
    public boolean volumeRefactoring() {
        return getValue(com.android.media.audio.Flags.FLAG_VOLUME_REFACTORING, new java.util.function.Predicate() { // from class: com.android.media.audio.CustomFeatureFlags$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(java.lang.Object obj) {
                return ((com.android.media.audio.FeatureFlags) obj).volumeRefactoring();
            }
        });
    }

    public boolean isFlagReadOnlyOptimized(java.lang.String flagName) {
        if (this.mReadOnlyFlagsSet.contains(flagName) && isOptimizationEnabled()) {
            return true;
        }
        return false;
    }

    private boolean isOptimizationEnabled() {
        return false;
    }

    protected boolean getValue(java.lang.String flagName, java.util.function.Predicate<com.android.media.audio.FeatureFlags> getter) {
        return this.mGetValueImpl.test(flagName, getter);
    }

    public java.util.List<java.lang.String> getFlagNames() {
        return java.util.Arrays.asList(com.android.media.audio.Flags.FLAG_ABS_VOLUME_INDEX_FIX, com.android.media.audio.Flags.FLAG_ALARM_MIN_VOLUME_ZERO, com.android.media.audio.Flags.FLAG_AS_DEVICE_CONNECTION_FAILURE, com.android.media.audio.Flags.FLAG_AUDIOSERVER_PERMISSIONS, com.android.media.audio.Flags.FLAG_BLUETOOTH_MAC_ADDRESS_ANONYMIZATION, com.android.media.audio.Flags.FLAG_DISABLE_PRESCALE_ABSOLUTE_VOLUME, com.android.media.audio.Flags.FLAG_DSA_OVER_BT_LE_AUDIO, com.android.media.audio.Flags.FLAG_MUSIC_FX_EDGE_TO_EDGE, com.android.media.audio.Flags.FLAG_PORT_TO_PIID_SIMPLIFICATION, com.android.media.audio.Flags.FLAG_REPLACE_STREAM_BT_SCO, com.android.media.audio.Flags.FLAG_RINGER_MODE_AFFECTS_ALARM, com.android.media.audio.Flags.FLAG_SET_STREAM_VOLUME_ORDER, com.android.media.audio.Flags.FLAG_SPATIALIZER_OFFLOAD, com.android.media.audio.Flags.FLAG_SPATIALIZER_UPMIX, com.android.media.audio.Flags.FLAG_STEREO_SPATIALIZATION, com.android.media.audio.Flags.FLAG_VGS_VSS_SYNC_MUTE_ORDER, com.android.media.audio.Flags.FLAG_VOLUME_REFACTORING);
    }
}
