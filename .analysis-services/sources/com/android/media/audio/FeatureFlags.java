package com.android.media.audio;

/* JADX INFO: loaded from: classes.dex */
public interface FeatureFlags {
    boolean absVolumeIndexFix();

    boolean alarmMinVolumeZero();

    boolean asDeviceConnectionFailure();

    boolean audioserverPermissions();

    boolean bluetoothMacAddressAnonymization();

    boolean disablePrescaleAbsoluteVolume();

    boolean dsaOverBtLeAudio();

    boolean musicFxEdgeToEdge();

    boolean portToPiidSimplification();

    boolean replaceStreamBtSco();

    boolean ringerModeAffectsAlarm();

    boolean setStreamVolumeOrder();

    boolean spatializerOffload();

    boolean spatializerUpmix();

    boolean stereoSpatialization();

    boolean vgsVssSyncMuteOrder();

    boolean volumeRefactoring();
}
