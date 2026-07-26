package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public final class FadeConfigurations {
    private static final boolean DEBUG = false;
    private static final long DEFAULT_DELAY_FADE_IN_OFFENDERS_MS = 2000;
    private static final long DEFAULT_FADE_OUT_DURATION_MS = 2000;
    private static final int INVALID_UID = -1;
    public static final java.lang.String TAG = "AS.FadeConfigurations";
    private android.media.FadeManagerConfiguration mActiveFadeManagerConfig;
    private android.media.FadeManagerConfiguration mDefaultFadeManagerConfig;
    private final java.lang.Object mLock = new java.lang.Object();
    private android.media.FadeManagerConfiguration mTransientFadeManagerConfig;
    private android.media.FadeManagerConfiguration mUpdatedFadeManagerConfig;
    private static final java.util.List<java.lang.Integer> DEFAULT_UNFADEABLE_PLAYER_TYPES = java.util.List.of(13, 3);
    private static final java.util.List<java.lang.Integer> DEFAULT_UNFADEABLE_CONTENT_TYPES = java.util.List.of(1);
    private static final java.util.List<java.lang.Integer> DEFAULT_FADEABLE_USAGES = java.util.List.of(14, 1);
    private static final android.media.VolumeShaper.Configuration DEFAULT_FADEOUT_VSHAPE = new android.media.VolumeShaper.Configuration.Builder().setId(2).setCurve(new float[]{0.0f, 0.25f, 1.0f}, new float[]{1.0f, 0.65f, 0.0f}).setOptionFlags(2).setDuration(2000).build();

    public int setFadeManagerConfiguration(android.media.FadeManagerConfiguration fadeManagerConfig) {
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return -1;
        }
        synchronized (this.mLock) {
            this.mUpdatedFadeManagerConfig = (android.media.FadeManagerConfiguration) java.util.Objects.requireNonNull(fadeManagerConfig, "Fade manager configuration cannot be null");
            this.mActiveFadeManagerConfig = getActiveFadeMgrConfigLocked();
        }
        return 0;
    }

    public int clearFadeManagerConfiguration() {
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return -1;
        }
        synchronized (this.mLock) {
            this.mUpdatedFadeManagerConfig = null;
            this.mActiveFadeManagerConfig = getActiveFadeMgrConfigLocked();
        }
        return 0;
    }

    public android.media.FadeManagerConfiguration getFadeManagerConfiguration() {
        android.media.FadeManagerConfiguration fadeManagerConfiguration;
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return null;
        }
        synchronized (this.mLock) {
            fadeManagerConfiguration = this.mActiveFadeManagerConfig;
        }
        return fadeManagerConfiguration;
    }

    public int setTransientFadeManagerConfiguration(android.media.FadeManagerConfiguration fadeManagerConfig) {
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return -1;
        }
        synchronized (this.mLock) {
            this.mTransientFadeManagerConfig = (android.media.FadeManagerConfiguration) java.util.Objects.requireNonNull(fadeManagerConfig, "Transient FadeManagerConfiguration cannot be null");
            this.mActiveFadeManagerConfig = getActiveFadeMgrConfigLocked();
        }
        return 0;
    }

    public int clearTransientFadeManagerConfiguration() {
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return -1;
        }
        synchronized (this.mLock) {
            this.mTransientFadeManagerConfig = null;
            this.mActiveFadeManagerConfig = getActiveFadeMgrConfigLocked();
        }
        return 0;
    }

    public boolean isFadeEnabled() {
        boolean zIsFadeEnabled;
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return true;
        }
        synchronized (this.mLock) {
            zIsFadeEnabled = getUpdatedFadeManagerConfigLocked().isFadeEnabled();
        }
        return zIsFadeEnabled;
    }

    public java.util.List<java.lang.Integer> getFadeableUsages() {
        java.util.List<java.lang.Integer> fadeableUsages;
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return DEFAULT_FADEABLE_USAGES;
        }
        synchronized (this.mLock) {
            android.media.FadeManagerConfiguration fadeManagerConfig = getUpdatedFadeManagerConfigLocked();
            fadeableUsages = fadeManagerConfig.isFadeEnabled() ? fadeManagerConfig.getFadeableUsages() : java.util.Collections.EMPTY_LIST;
        }
        return fadeableUsages;
    }

    public java.util.List<java.lang.Integer> getUnfadeableContentTypes() {
        java.util.List<java.lang.Integer> unfadeableContentTypes;
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return DEFAULT_UNFADEABLE_CONTENT_TYPES;
        }
        synchronized (this.mLock) {
            android.media.FadeManagerConfiguration fadeManagerConfig = getUpdatedFadeManagerConfigLocked();
            unfadeableContentTypes = fadeManagerConfig.isFadeEnabled() ? fadeManagerConfig.getUnfadeableContentTypes() : java.util.Collections.EMPTY_LIST;
        }
        return unfadeableContentTypes;
    }

    public java.util.List<java.lang.Integer> getUnfadeablePlayerTypes() {
        java.util.List<java.lang.Integer> unfadeablePlayerTypes;
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return DEFAULT_UNFADEABLE_PLAYER_TYPES;
        }
        synchronized (this.mLock) {
            android.media.FadeManagerConfiguration fadeManagerConfig = getUpdatedFadeManagerConfigLocked();
            unfadeablePlayerTypes = fadeManagerConfig.isFadeEnabled() ? fadeManagerConfig.getUnfadeablePlayerTypes() : java.util.Collections.EMPTY_LIST;
        }
        return unfadeablePlayerTypes;
    }

    public android.media.VolumeShaper.Configuration getFadeOutVolumeShaperConfig(android.media.AudioAttributes aa) {
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return DEFAULT_FADEOUT_VSHAPE;
        }
        return getOptimalFadeOutVolShaperConfig(aa);
    }

    public android.media.VolumeShaper.Configuration getFadeInVolumeShaperConfig(android.media.AudioAttributes aa) {
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return null;
        }
        return getOptimalFadeInVolShaperConfig(aa);
    }

    public long getFadeOutDuration(android.media.AudioAttributes aa) {
        if (!isFadeable(aa, -1, -1)) {
            return 0L;
        }
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return 2000L;
        }
        return getOptimalFadeOutDuration(aa);
    }

    public long getDelayFadeInOffenders(android.media.AudioAttributes aa) {
        long fadeInDelayForOffenders;
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return 2000L;
        }
        synchronized (this.mLock) {
            fadeInDelayForOffenders = getUpdatedFadeManagerConfigLocked().getFadeInDelayForOffenders();
        }
        return fadeInDelayForOffenders;
    }

    public java.util.List<android.media.AudioAttributes> getUnfadeableAudioAttributes() {
        java.util.List<android.media.AudioAttributes> unfadeableAudioAttributes;
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return java.util.Collections.EMPTY_LIST;
        }
        synchronized (this.mLock) {
            android.media.FadeManagerConfiguration fadeManagerConfig = getUpdatedFadeManagerConfigLocked();
            unfadeableAudioAttributes = fadeManagerConfig.isFadeEnabled() ? fadeManagerConfig.getUnfadeableAudioAttributes() : java.util.Collections.EMPTY_LIST;
        }
        return unfadeableAudioAttributes;
    }

    public java.util.List<java.lang.Integer> getUnfadeableUids() {
        java.util.List<java.lang.Integer> unfadeableUids;
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return java.util.Collections.EMPTY_LIST;
        }
        synchronized (this.mLock) {
            android.media.FadeManagerConfiguration fadeManagerConfig = getUpdatedFadeManagerConfigLocked();
            unfadeableUids = fadeManagerConfig.isFadeEnabled() ? fadeManagerConfig.getUnfadeableUids() : java.util.Collections.EMPTY_LIST;
        }
        return unfadeableUids;
    }

    public boolean isFadeable(android.media.AudioAttributes aa, int uid, int playerType) {
        synchronized (this.mLock) {
            if (isPlayerTypeUnfadeableLocked(playerType)) {
                return false;
            }
            if (isContentTypeUnfadeableLocked(aa.getContentType())) {
                return false;
            }
            if (isUsageFadeableLocked(aa.getSystemUsage())) {
                return !isUnfadeableForFadeMgrConfigLocked(aa, uid);
            }
            return false;
        }
    }

    private android.media.VolumeShaper.Configuration getOptimalFadeOutVolShaperConfig(android.media.AudioAttributes aa) {
        synchronized (this.mLock) {
            android.media.FadeManagerConfiguration fadeManagerConfig = getUpdatedFadeManagerConfigLocked();
            android.media.VolumeShaper.Configuration volShaperConfig = fadeManagerConfig.getFadeOutVolumeShaperConfigForAudioAttributes(aa);
            if (volShaperConfig != null) {
                return volShaperConfig;
            }
            return fadeManagerConfig.getFadeOutVolumeShaperConfigForUsage(aa.getSystemUsage());
        }
    }

    private android.media.VolumeShaper.Configuration getOptimalFadeInVolShaperConfig(android.media.AudioAttributes aa) {
        synchronized (this.mLock) {
            android.media.FadeManagerConfiguration fadeManagerConfig = getUpdatedFadeManagerConfigLocked();
            android.media.VolumeShaper.Configuration volShaperConfig = fadeManagerConfig.getFadeInVolumeShaperConfigForAudioAttributes(aa);
            if (volShaperConfig != null) {
                return volShaperConfig;
            }
            return fadeManagerConfig.getFadeInVolumeShaperConfigForUsage(aa.getSystemUsage());
        }
    }

    private long getOptimalFadeOutDuration(android.media.AudioAttributes aa) {
        synchronized (this.mLock) {
            android.media.FadeManagerConfiguration fadeManagerConfig = getUpdatedFadeManagerConfigLocked();
            long duration = fadeManagerConfig.getFadeOutDurationForAudioAttributes(aa);
            if (duration != 0) {
                return duration;
            }
            return fadeManagerConfig.getFadeOutDurationForUsage(aa.getSystemUsage());
        }
    }

    private boolean isUnfadeableForFadeMgrConfigLocked(android.media.AudioAttributes aa, int uid) {
        return isAudioAttributesUnfadeableLocked(aa) || isUidUnfadeableLocked(uid);
    }

    private boolean isUsageFadeableLocked(int usage) {
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return DEFAULT_FADEABLE_USAGES.contains(java.lang.Integer.valueOf(usage));
        }
        return getUpdatedFadeManagerConfigLocked().isUsageFadeable(usage);
    }

    private boolean isContentTypeUnfadeableLocked(int contentType) {
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return DEFAULT_UNFADEABLE_CONTENT_TYPES.contains(java.lang.Integer.valueOf(contentType));
        }
        return getUpdatedFadeManagerConfigLocked().isContentTypeUnfadeable(contentType);
    }

    private boolean isPlayerTypeUnfadeableLocked(int playerType) {
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return DEFAULT_UNFADEABLE_PLAYER_TYPES.contains(java.lang.Integer.valueOf(playerType));
        }
        return getUpdatedFadeManagerConfigLocked().isPlayerTypeUnfadeable(playerType);
    }

    private boolean isAudioAttributesUnfadeableLocked(android.media.AudioAttributes aa) {
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return false;
        }
        return getUpdatedFadeManagerConfigLocked().isAudioAttributesUnfadeable(aa);
    }

    private boolean isUidUnfadeableLocked(int uid) {
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return false;
        }
        return getUpdatedFadeManagerConfigLocked().isUidUnfadeable(uid);
    }

    private android.media.FadeManagerConfiguration getUpdatedFadeManagerConfigLocked() {
        if (this.mActiveFadeManagerConfig == null) {
            this.mActiveFadeManagerConfig = getActiveFadeMgrConfigLocked();
        }
        return this.mActiveFadeManagerConfig;
    }

    private android.media.FadeManagerConfiguration getActiveFadeMgrConfigLocked() {
        if (this.mTransientFadeManagerConfig != null) {
            return this.mTransientFadeManagerConfig;
        }
        if (this.mUpdatedFadeManagerConfig != null) {
            return this.mUpdatedFadeManagerConfig;
        }
        return getDefaultFadeManagerConfigLocked();
    }

    private android.media.FadeManagerConfiguration getDefaultFadeManagerConfigLocked() {
        if (this.mDefaultFadeManagerConfig == null) {
            this.mDefaultFadeManagerConfig = new android.media.FadeManagerConfiguration.Builder().build();
        }
        return this.mDefaultFadeManagerConfig;
    }
}
