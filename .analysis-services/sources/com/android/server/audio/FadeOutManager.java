package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public final class FadeOutManager {
    private static final boolean DEBUG = false;
    static final long FADE_OUT_DURATION_MS = 1000;
    public static final java.lang.String TAG = "AS.FadeOutManager";
    private final java.lang.Object mLock = new java.lang.Object();
    private final android.util.SparseArray<com.android.server.audio.FadeOutManager.FadedOutApp> mUidToFadedAppsMap = new android.util.SparseArray<>();
    private final com.android.server.audio.FadeConfigurations mFadeConfigurations = new com.android.server.audio.FadeConfigurations();

    int setFadeManagerConfiguration(android.media.FadeManagerConfiguration fadeManagerConfig) {
        int fadeManagerConfiguration;
        synchronized (this.mLock) {
            fadeManagerConfiguration = this.mFadeConfigurations.setFadeManagerConfiguration(fadeManagerConfig);
        }
        return fadeManagerConfiguration;
    }

    int clearFadeManagerConfiguration() {
        int iClearFadeManagerConfiguration;
        synchronized (this.mLock) {
            iClearFadeManagerConfiguration = this.mFadeConfigurations.clearFadeManagerConfiguration();
        }
        return iClearFadeManagerConfiguration;
    }

    android.media.FadeManagerConfiguration getFadeManagerConfiguration() {
        return this.mFadeConfigurations.getFadeManagerConfiguration();
    }

    int setTransientFadeManagerConfiguration(android.media.FadeManagerConfiguration fadeManagerConfig) {
        int transientFadeManagerConfiguration;
        synchronized (this.mLock) {
            transientFadeManagerConfiguration = this.mFadeConfigurations.setTransientFadeManagerConfiguration(fadeManagerConfig);
        }
        return transientFadeManagerConfiguration;
    }

    int clearTransientFadeManagerConfiguration() {
        int iClearTransientFadeManagerConfiguration;
        synchronized (this.mLock) {
            iClearTransientFadeManagerConfiguration = this.mFadeConfigurations.clearTransientFadeManagerConfiguration();
        }
        return iClearTransientFadeManagerConfiguration;
    }

    boolean isFadeEnabled() {
        return this.mFadeConfigurations.isFadeEnabled();
    }

    boolean canCauseFadeOut(com.android.server.audio.FocusRequester requester, com.android.server.audio.FocusRequester loser) {
        return requester.getAudioAttributes().getContentType() != 1 && (loser.getGrantFlags() & 2) == 0;
    }

    boolean canBeFadedOut(android.media.AudioPlaybackConfiguration apc) {
        boolean zIsFadeable;
        synchronized (this.mLock) {
            zIsFadeable = this.mFadeConfigurations.isFadeable(apc.getAudioAttributes(), apc.getClientUid(), apc.getPlayerType());
        }
        return zIsFadeable;
    }

    long getFadeOutDurationOnFocusLossMillis(android.media.AudioAttributes aa) {
        long fadeOutDuration;
        synchronized (this.mLock) {
            fadeOutDuration = this.mFadeConfigurations.getFadeOutDuration(aa);
        }
        return fadeOutDuration;
    }

    long getFadeInDelayForOffendersMillis(android.media.AudioAttributes aa) {
        long delayFadeInOffenders;
        synchronized (this.mLock) {
            delayFadeInOffenders = this.mFadeConfigurations.getDelayFadeInOffenders(aa);
        }
        return delayFadeInOffenders;
    }

    void fadeOutUid(int uid, java.util.List<android.media.AudioPlaybackConfiguration> players) {
        android.util.Slog.i(TAG, "fadeOutUid() uid:" + uid);
        synchronized (this.mLock) {
            if (!this.mUidToFadedAppsMap.contains(uid)) {
                this.mUidToFadedAppsMap.put(uid, new com.android.server.audio.FadeOutManager.FadedOutApp(uid));
            }
            com.android.server.audio.FadeOutManager.FadedOutApp fa = this.mUidToFadedAppsMap.get(uid);
            for (android.media.AudioPlaybackConfiguration apc : players) {
                android.media.VolumeShaper.Configuration volShaper = this.mFadeConfigurations.getFadeOutVolumeShaperConfig(apc.getAudioAttributes());
                fa.addFade(apc, false, volShaper);
            }
        }
    }

    void unfadeOutUid(int uid, java.util.Map<java.lang.Integer, android.media.AudioPlaybackConfiguration> players) {
        android.util.Slog.i(TAG, "unfadeOutUid() uid:" + uid);
        synchronized (this.mLock) {
            com.android.server.audio.FadeOutManager.FadedOutApp fa = this.mUidToFadedAppsMap.get(uid);
            if (fa == null) {
                return;
            }
            this.mUidToFadedAppsMap.remove(uid);
            if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
                fa.removeUnfadeAll(players);
                return;
            }
            java.util.ArrayList<android.media.AudioPlaybackConfiguration> apcs = new java.util.ArrayList<>(players.values());
            for (int index = 0; index < apcs.size(); index++) {
                android.media.AudioPlaybackConfiguration apc = apcs.get(index);
                android.media.VolumeShaper.Configuration config = this.mFadeConfigurations.getFadeInVolumeShaperConfig(apc.getAudioAttributes());
                fa.fadeInPlayer(apc, config);
            }
            fa.clear();
        }
    }

    void checkFade(android.media.AudioPlaybackConfiguration apc) {
        synchronized (this.mLock) {
            android.media.VolumeShaper.Configuration volShaper = this.mFadeConfigurations.getFadeOutVolumeShaperConfig(apc.getAudioAttributes());
            com.android.server.audio.FadeOutManager.FadedOutApp fa = this.mUidToFadedAppsMap.get(apc.getClientUid());
            if (fa == null) {
                return;
            }
            fa.addFade(apc, true, volShaper);
        }
    }

    void removeReleased(android.media.AudioPlaybackConfiguration apc) {
        int uid = apc.getClientUid();
        synchronized (this.mLock) {
            com.android.server.audio.FadeOutManager.FadedOutApp fa = this.mUidToFadedAppsMap.get(uid);
            if (fa == null) {
                return;
            }
            fa.removeReleased(apc);
        }
    }

    boolean isUidFadedOut(int uid) {
        boolean zContains;
        synchronized (this.mLock) {
            zContains = this.mUidToFadedAppsMap.contains(uid);
        }
        return zContains;
    }

    void dump(java.io.PrintWriter pw) {
        synchronized (this.mLock) {
            for (int index = 0; index < this.mUidToFadedAppsMap.size(); index++) {
                this.mUidToFadedAppsMap.valueAt(index).dump(pw);
            }
        }
    }

    private static final class FadedOutApp {
        private static final android.media.VolumeShaper.Operation PLAY_CREATE_IF_NEEDED = new android.media.VolumeShaper.Operation.Builder(android.media.VolumeShaper.Operation.PLAY).createIfNeeded().build();
        private static final android.media.VolumeShaper.Operation PLAY_SKIP_RAMP = new android.media.VolumeShaper.Operation.Builder(PLAY_CREATE_IF_NEEDED).setXOffset(1.0f).build();
        private final android.util.SparseArray<android.media.VolumeShaper.Configuration> mFadedPlayers = new android.util.SparseArray<>();
        private final int mUid;

        FadedOutApp(int uid) {
            this.mUid = uid;
        }

        void dump(java.io.PrintWriter pw) {
            pw.print("\t uid:" + this.mUid + " piids:");
            for (int index = 0; index < this.mFadedPlayers.size(); index++) {
                pw.print("piid: " + this.mFadedPlayers.keyAt(index) + " Volume shaper: " + this.mFadedPlayers.valueAt(index));
            }
            pw.println("");
        }

        void addFade(android.media.AudioPlaybackConfiguration apc, boolean skipRamp, android.media.VolumeShaper.Configuration volShaper) {
            int piid = java.lang.Integer.valueOf(apc.getPlayerInterfaceId()).intValue();
            if (this.mFadedPlayers.indexOfKey(piid) < 0 && apc.getPlayerProxy() != null) {
                applyVolumeShaperInternal(apc, piid, volShaper, skipRamp ? PLAY_SKIP_RAMP : PLAY_CREATE_IF_NEEDED, skipRamp, "fading out");
                this.mFadedPlayers.put(piid, volShaper);
            }
        }

        void removeUnfadeAll(java.util.Map<java.lang.Integer, android.media.AudioPlaybackConfiguration> players) {
            for (int index = 0; index < this.mFadedPlayers.size(); index++) {
                int piid = this.mFadedPlayers.keyAt(index);
                android.media.AudioPlaybackConfiguration apc = players.get(java.lang.Integer.valueOf(piid));
                if (apc != null && apc.getPlayerProxy() != null) {
                    applyVolumeShaperInternal(apc, piid, null, android.media.VolumeShaper.Operation.REVERSE, false, "fading in");
                }
            }
            this.mFadedPlayers.clear();
        }

        void fadeInPlayer(android.media.AudioPlaybackConfiguration apc, android.media.VolumeShaper.Configuration config) {
            android.media.VolumeShaper.Operation operation;
            int piid = java.lang.Integer.valueOf(apc.getPlayerInterfaceId()).intValue();
            if (!this.mFadedPlayers.contains(piid)) {
                return;
            }
            android.media.VolumeShaper.Operation operation2 = android.media.VolumeShaper.Operation.REVERSE;
            if (config == null) {
                operation = operation2;
            } else {
                android.media.VolumeShaper.Operation operation3 = new android.media.VolumeShaper.Operation.Builder().replace(this.mFadedPlayers.get(piid).getId(), true).build();
                operation = operation3;
            }
            this.mFadedPlayers.remove(piid);
            if (apc.getPlayerProxy() != null) {
                applyVolumeShaperInternal(apc, piid, config, operation, false, "fading in");
            }
        }

        void clear() {
            this.mFadedPlayers.size();
            this.mFadedPlayers.clear();
        }

        void removeReleased(android.media.AudioPlaybackConfiguration apc) {
            this.mFadedPlayers.delete(java.lang.Integer.valueOf(apc.getPlayerInterfaceId()).intValue());
        }

        private void applyVolumeShaperInternal(android.media.AudioPlaybackConfiguration apc, int piid, android.media.VolumeShaper.Configuration volShaperConfig, android.media.VolumeShaper.Operation operation, boolean skipRamp, java.lang.String eventType) {
            android.media.VolumeShaper.Configuration config = volShaperConfig;
            if (operation.equals(android.media.VolumeShaper.Operation.REVERSE)) {
                android.media.VolumeShaper.Configuration config2 = this.mFadedPlayers.get(piid);
                config = config2;
            }
            try {
                logFadeEvent(apc, piid, volShaperConfig, operation, skipRamp, eventType);
                apc.getPlayerProxy().applyVolumeShaper(config, operation);
            } catch (java.lang.Exception e) {
                android.util.Slog.e(com.android.server.audio.FadeOutManager.TAG, "Error " + eventType + " piid:" + piid + " uid:" + this.mUid, e);
            }
        }

        private void logFadeEvent(android.media.AudioPlaybackConfiguration apc, int piid, android.media.VolumeShaper.Configuration config, android.media.VolumeShaper.Operation operation, boolean skipRamp, java.lang.String eventType) {
            if (eventType.equals("fading out")) {
                com.android.server.audio.PlaybackActivityMonitor.sEventLogger.enqueue(new com.android.server.audio.PlaybackActivityMonitor.FadeOutEvent(apc, skipRamp, config, operation).printLog(com.android.server.audio.FadeOutManager.TAG));
            } else if (eventType.equals("fading in")) {
                com.android.server.audio.PlaybackActivityMonitor.sEventLogger.enqueue(new com.android.server.audio.PlaybackActivityMonitor.FadeInEvent(apc, skipRamp, config, operation).printLog(com.android.server.audio.FadeOutManager.TAG));
            } else {
                com.android.server.audio.PlaybackActivityMonitor.sEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent(eventType + " piid:" + piid).printLog(com.android.server.audio.FadeOutManager.TAG));
            }
        }
    }
}
