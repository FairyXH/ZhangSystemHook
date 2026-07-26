package com.android.server.media.metrics;

/* JADX INFO: loaded from: classes2.dex */
public final class MediaMetricsManagerService extends com.android.server.SystemService {
    private static final java.lang.String AUDIO_MIME_TYPE_PREFIX = "audio/";
    private static final int DURATION_BUCKETS_BELOW_ONE_MINUTE = 8;
    private static final int DURATION_BUCKETS_COUNT = 13;
    private static final java.lang.String FAILED_TO_GET = "failed_to_get";
    private static final int LOGGING_LEVEL_BLOCKED = 99999;
    private static final int LOGGING_LEVEL_EVERYTHING = 0;
    private static final int LOGGING_LEVEL_NO_UID = 1000;
    private static final java.lang.String MEDIA_METRICS_MODE = "media_metrics_mode";
    private static final int MEDIA_METRICS_MODE_ALLOWLIST = 3;
    private static final int MEDIA_METRICS_MODE_BLOCKLIST = 2;
    private static final int MEDIA_METRICS_MODE_OFF = 0;
    private static final int MEDIA_METRICS_MODE_ON = 1;
    private static final java.lang.String PLAYER_METRICS_APP_ALLOWLIST = "player_metrics_app_allowlist";
    private static final java.lang.String PLAYER_METRICS_APP_BLOCKLIST = "player_metrics_app_blocklist";
    private static final java.lang.String PLAYER_METRICS_PER_APP_ATTRIBUTION_ALLOWLIST = "player_metrics_per_app_attribution_allowlist";
    private static final java.lang.String PLAYER_METRICS_PER_APP_ATTRIBUTION_BLOCKLIST = "player_metrics_per_app_attribution_blocklist";
    private static final java.lang.String TAG = "MediaMetricsManagerService";
    private static final java.lang.String VIDEO_MIME_TYPE_PREFIX = "video/";
    private static final java.lang.String mMetricsId = "metrics.manager";
    private java.util.List<java.lang.String> mAllowlist;
    private java.util.List<java.lang.String> mBlockList;
    private final android.content.Context mContext;
    private final java.lang.Object mLock;
    private java.lang.Integer mMode;
    private java.util.List<java.lang.String> mNoUidAllowlist;
    private java.util.List<java.lang.String> mNoUidBlocklist;
    private final java.security.SecureRandom mSecureRandom;
    private static final android.media.metrics.MediaItemInfo EMPTY_MEDIA_ITEM_INFO = new android.media.metrics.MediaItemInfo.Builder().build();
    private static final java.util.regex.Pattern PATTERN_KNOWN_EDITING_LIBRARY_NAMES = java.util.regex.Pattern.compile("androidx\\.media3:media3-(transformer|muxer):[\\d.]+(-(alpha|beta|rc)\\d\\d)?");

    public MediaMetricsManagerService(android.content.Context context) {
        super(context);
        this.mMode = null;
        this.mAllowlist = null;
        this.mNoUidAllowlist = null;
        this.mBlockList = null;
        this.mNoUidBlocklist = null;
        this.mLock = new java.lang.Object();
        this.mContext = context;
        this.mSecureRandom = new java.security.SecureRandom();
    }

    @Override // com.android.server.SystemService
    public void onStart() {
        publishBinderService("media_metrics", new com.android.server.media.metrics.MediaMetricsManagerService.BinderService());
        android.provider.DeviceConfig.addOnPropertiesChangedListener("media", this.mContext.getMainExecutor(), new android.provider.DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.server.media.metrics.MediaMetricsManagerService$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(android.provider.DeviceConfig.Properties properties) {
                this.f$0.updateConfigs(properties);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateConfigs(android.provider.DeviceConfig.Properties properties) {
        synchronized (this.mLock) {
            this.mMode = java.lang.Integer.valueOf(properties.getInt(MEDIA_METRICS_MODE, 2));
            java.util.List<java.lang.String> newList = getListLocked(PLAYER_METRICS_APP_ALLOWLIST);
            if (newList != null || this.mMode.intValue() != 3) {
                this.mAllowlist = newList;
            }
            java.util.List<java.lang.String> newList2 = getListLocked(PLAYER_METRICS_PER_APP_ATTRIBUTION_ALLOWLIST);
            if (newList2 != null || this.mMode.intValue() != 3) {
                this.mNoUidAllowlist = newList2;
            }
            java.util.List<java.lang.String> newList3 = getListLocked(PLAYER_METRICS_APP_BLOCKLIST);
            if (newList3 != null || this.mMode.intValue() != 2) {
                this.mBlockList = newList3;
            }
            java.util.List<java.lang.String> newList4 = getListLocked(PLAYER_METRICS_PER_APP_ATTRIBUTION_BLOCKLIST);
            if (newList4 != null || this.mMode.intValue() != 2) {
                this.mNoUidBlocklist = newList4;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public java.util.List<java.lang.String> getListLocked(java.lang.String listName) {
        long identity = android.os.Binder.clearCallingIdentity();
        try {
            java.lang.String listString = android.provider.DeviceConfig.getString("media", listName, FAILED_TO_GET);
            android.os.Binder.restoreCallingIdentity(identity);
            if (listString.equals(FAILED_TO_GET)) {
                android.util.Slog.d(TAG, "failed to get " + listName + " from DeviceConfig");
                return null;
            }
            java.lang.String[] pkgArr = listString.split(",");
            return java.util.Arrays.asList(pkgArr);
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(identity);
            throw th;
        }
    }

    private final class BinderService extends android.media.metrics.IMediaMetricsManager.Stub {
        private BinderService() {
        }

        public void reportPlaybackMetrics(java.lang.String sessionId, android.media.metrics.PlaybackMetrics metrics, int userId) {
            int level = loggingLevel();
            if (level == com.android.server.media.metrics.MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED) {
                return;
            }
            android.util.StatsEvent statsEvent = android.util.StatsEvent.newBuilder().setAtomId(320).writeInt(level == 0 ? android.os.Binder.getCallingUid() : 0).writeString(sessionId).writeLong(metrics.getMediaDurationMillis()).writeInt(metrics.getStreamSource()).writeInt(metrics.getStreamType()).writeInt(metrics.getPlaybackType()).writeInt(metrics.getDrmType()).writeInt(metrics.getContentType()).writeString(metrics.getPlayerName()).writeString(metrics.getPlayerVersion()).writeByteArray(new byte[0]).writeInt(metrics.getVideoFramesPlayed()).writeInt(metrics.getVideoFramesDropped()).writeInt(metrics.getAudioUnderrunCount()).writeLong(metrics.getNetworkBytesRead()).writeLong(metrics.getLocalBytesRead()).writeLong(metrics.getNetworkTransferDurationMillis()).writeString(android.util.Base64.encodeToString(metrics.getDrmSessionId(), 0)).usePooledBuffer().build();
            android.util.StatsLog.write(statsEvent);
        }

        public void reportBundleMetrics(java.lang.String sessionId, android.os.PersistableBundle metrics, int userId) {
            int level = loggingLevel();
            if (level == com.android.server.media.metrics.MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED) {
            }
            int atomid = metrics.getInt("bundlesession-statsd-atom");
            switch (atomid) {
                case 322:
                    java.lang.String _sessionId = metrics.getString("playbackstateevent-sessionid");
                    int _state = metrics.getInt("playbackstateevent-state", -1);
                    long _lifetime = metrics.getLong("playbackstateevent-lifetime", -1L);
                    if (_sessionId == null || _state < 0 || _lifetime < 0) {
                        android.util.Slog.d(com.android.server.media.metrics.MediaMetricsManagerService.TAG, "dropping incomplete data for atom 322: _sessionId: " + _sessionId + " _state: " + _state + " _lifetime: " + _lifetime);
                    } else {
                        android.util.StatsEvent statsEvent = android.util.StatsEvent.newBuilder().setAtomId(322).writeString(_sessionId).writeInt(_state).writeLong(_lifetime).usePooledBuffer().build();
                        android.util.StatsLog.write(statsEvent);
                    }
                    break;
            }
        }

        public void reportPlaybackStateEvent(java.lang.String sessionId, android.media.metrics.PlaybackStateEvent event, int userId) {
            int level = loggingLevel();
            if (level == com.android.server.media.metrics.MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED) {
                return;
            }
            android.util.StatsEvent statsEvent = android.util.StatsEvent.newBuilder().setAtomId(322).writeString(sessionId).writeInt(event.getState()).writeLong(event.getTimeSinceCreatedMillis()).usePooledBuffer().build();
            android.util.StatsLog.write(statsEvent);
        }

        private java.lang.String getSessionIdInternal(int userId) {
            byte[] byteId = new byte[12];
            com.android.server.media.metrics.MediaMetricsManagerService.this.mSecureRandom.nextBytes(byteId);
            java.lang.String id = android.util.Base64.encodeToString(byteId, 11);
            new android.media.MediaMetrics.Item(com.android.server.media.metrics.MediaMetricsManagerService.mMetricsId).set(android.media.MediaMetrics.Property.EVENT, "create").set(android.media.MediaMetrics.Property.LOG_SESSION_ID, id).record();
            return id;
        }

        public void releaseSessionId(java.lang.String sessionId, int userId) {
            android.util.Slog.v(com.android.server.media.metrics.MediaMetricsManagerService.TAG, "Releasing sessionId " + sessionId + " for userId " + userId + " [NOP]");
        }

        public java.lang.String getPlaybackSessionId(int userId) {
            return getSessionIdInternal(userId);
        }

        public java.lang.String getRecordingSessionId(int userId) {
            return getSessionIdInternal(userId);
        }

        public java.lang.String getTranscodingSessionId(int userId) {
            return getSessionIdInternal(userId);
        }

        public java.lang.String getEditingSessionId(int userId) {
            return getSessionIdInternal(userId);
        }

        public java.lang.String getBundleSessionId(int userId) {
            return getSessionIdInternal(userId);
        }

        public void reportPlaybackErrorEvent(java.lang.String sessionId, android.media.metrics.PlaybackErrorEvent event, int userId) {
            int level = loggingLevel();
            if (level == com.android.server.media.metrics.MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED) {
                return;
            }
            android.util.StatsEvent statsEvent = android.util.StatsEvent.newBuilder().setAtomId(323).writeString(sessionId).writeString(event.getExceptionStack()).writeInt(event.getErrorCode()).writeInt(event.getSubErrorCode()).writeLong(event.getTimeSinceCreatedMillis()).usePooledBuffer().build();
            android.util.StatsLog.write(statsEvent);
        }

        public void reportNetworkEvent(java.lang.String sessionId, android.media.metrics.NetworkEvent event, int userId) {
            int level = loggingLevel();
            if (level == com.android.server.media.metrics.MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED) {
                return;
            }
            android.util.StatsEvent statsEvent = android.util.StatsEvent.newBuilder().setAtomId(321).writeString(sessionId).writeInt(event.getNetworkType()).writeLong(event.getTimeSinceCreatedMillis()).usePooledBuffer().build();
            android.util.StatsLog.write(statsEvent);
        }

        public void reportTrackChangeEvent(java.lang.String sessionId, android.media.metrics.TrackChangeEvent event, int userId) {
            int level = loggingLevel();
            if (level == com.android.server.media.metrics.MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED) {
                return;
            }
            android.util.StatsEvent statsEvent = android.util.StatsEvent.newBuilder().setAtomId(com.android.internal.util.FrameworkStatsLog.APP_BACKGROUND_RESTRICTIONS_INFO__EXEMPTION_REASON__REASON_ACTIVE_DEVICE_ADMIN).writeString(sessionId).writeInt(event.getTrackState()).writeInt(event.getTrackChangeReason()).writeString(event.getContainerMimeType()).writeString(event.getSampleMimeType()).writeString(event.getCodecName()).writeInt(event.getBitrate()).writeLong(event.getTimeSinceCreatedMillis()).writeInt(event.getTrackType()).writeString(event.getLanguage()).writeString(event.getLanguageRegion()).writeInt(event.getChannelCount()).writeInt(event.getAudioSampleRate()).writeInt(event.getWidth()).writeInt(event.getHeight()).writeFloat(event.getVideoFrameRate()).usePooledBuffer().build();
            android.util.StatsLog.write(statsEvent);
        }

        public void reportEditingEndedEvent(java.lang.String sessionId, android.media.metrics.EditingEndedEvent event, int userId) {
            android.media.metrics.MediaItemInfo inputMediaItemInfo;
            android.media.metrics.MediaItemInfo outputMediaItemInfo;
            int outputVideoResolution;
            int level = loggingLevel();
            if (level == com.android.server.media.metrics.MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED) {
                return;
            }
            if (event.getInputMediaItemInfos().isEmpty()) {
                inputMediaItemInfo = com.android.server.media.metrics.MediaMetricsManagerService.EMPTY_MEDIA_ITEM_INFO;
            } else {
                inputMediaItemInfo = (android.media.metrics.MediaItemInfo) event.getInputMediaItemInfos().get(0);
            }
            long inputDataTypes = inputMediaItemInfo.getDataTypes();
            java.lang.String inputAudioSampleMimeType = com.android.server.media.metrics.MediaMetricsManagerService.getFilteredFirstMimeType(inputMediaItemInfo.getSampleMimeTypes(), com.android.server.media.metrics.MediaMetricsManagerService.AUDIO_MIME_TYPE_PREFIX);
            java.lang.String inputVideoSampleMimeType = com.android.server.media.metrics.MediaMetricsManagerService.getFilteredFirstMimeType(inputMediaItemInfo.getSampleMimeTypes(), com.android.server.media.metrics.MediaMetricsManagerService.VIDEO_MIME_TYPE_PREFIX);
            android.util.Size inputVideoSize = inputMediaItemInfo.getVideoSize();
            int inputVideoResolution = com.android.server.media.metrics.MediaMetricsManagerService.getVideoResolutionEnum(inputVideoSize);
            if (inputVideoResolution == 0) {
                inputVideoResolution = com.android.server.media.metrics.MediaMetricsManagerService.getVideoResolutionEnum(new android.util.Size(inputVideoSize.getHeight(), inputVideoSize.getWidth()));
            }
            java.util.List<java.lang.String> inputCodecNames = inputMediaItemInfo.getCodecNames();
            java.lang.String inputFirstCodecName = !inputCodecNames.isEmpty() ? inputCodecNames.get(0) : "";
            java.lang.String inputSecondCodecName = inputCodecNames.size() > 1 ? inputCodecNames.get(1) : "";
            if (event.getOutputMediaItemInfo() == null) {
                outputMediaItemInfo = com.android.server.media.metrics.MediaMetricsManagerService.EMPTY_MEDIA_ITEM_INFO;
            } else {
                outputMediaItemInfo = event.getOutputMediaItemInfo();
            }
            long outputDataTypes = outputMediaItemInfo.getDataTypes();
            java.lang.String outputAudioSampleMimeType = com.android.server.media.metrics.MediaMetricsManagerService.getFilteredFirstMimeType(outputMediaItemInfo.getSampleMimeTypes(), com.android.server.media.metrics.MediaMetricsManagerService.AUDIO_MIME_TYPE_PREFIX);
            java.lang.String outputVideoSampleMimeType = com.android.server.media.metrics.MediaMetricsManagerService.getFilteredFirstMimeType(outputMediaItemInfo.getSampleMimeTypes(), com.android.server.media.metrics.MediaMetricsManagerService.VIDEO_MIME_TYPE_PREFIX);
            android.util.Size outputVideoSize = outputMediaItemInfo.getVideoSize();
            int outputVideoResolution2 = com.android.server.media.metrics.MediaMetricsManagerService.getVideoResolutionEnum(outputVideoSize);
            if (outputVideoResolution2 == 0) {
                int level2 = outputVideoSize.getHeight();
                outputVideoResolution = com.android.server.media.metrics.MediaMetricsManagerService.getVideoResolutionEnum(new android.util.Size(level2, outputVideoSize.getWidth()));
            } else {
                outputVideoResolution = outputVideoResolution2;
            }
            java.util.List<java.lang.String> outputCodecNames = outputMediaItemInfo.getCodecNames();
            java.lang.String outputFirstCodecName = !outputCodecNames.isEmpty() ? outputCodecNames.get(0) : "";
            java.lang.String outputSecondCodecName = outputCodecNames.size() > 1 ? outputCodecNames.get(1) : "";
            long operationTypes = event.getOperationTypes();
            android.util.StatsEvent statsEvent = android.util.StatsEvent.newBuilder().setAtomId(798).writeString(sessionId).writeInt(event.getFinalState()).writeFloat(event.getFinalProgressPercent()).writeInt(event.getErrorCode()).writeLong(event.getTimeSinceCreatedMillis()).writeBoolean((operationTypes & 1) != 0).writeBoolean((operationTypes & 2) != 0).writeBoolean((operationTypes & 4) != 0).writeBoolean((operationTypes & 8) != 0).writeBoolean((operationTypes & 16) != 0).writeBoolean((operationTypes & 32) != 0).writeBoolean((operationTypes & 64) != 0).writeBoolean((operationTypes & 128) != 0).writeString(com.android.server.media.metrics.MediaMetricsManagerService.getFilteredLibraryName(event.getExporterName())).writeString(com.android.server.media.metrics.MediaMetricsManagerService.getFilteredLibraryName(event.getMuxerName())).writeInt(com.android.server.media.metrics.MediaMetricsManagerService.getThroughputFps(event)).writeInt(event.getInputMediaItemInfos().size()).writeInt(inputMediaItemInfo.getSourceType()).writeBoolean((inputDataTypes & 1) != 0).writeBoolean((inputDataTypes & 2) != 0).writeBoolean((inputDataTypes & 4) != 0).writeBoolean((inputDataTypes & 8) != 0).writeBoolean((inputDataTypes & 16) != 0).writeBoolean((inputDataTypes & 32) != 0).writeBoolean((inputDataTypes & 64) != 0).writeBoolean((inputDataTypes & 128) != 0).writeBoolean((inputDataTypes & 256) != 0).writeBoolean((inputDataTypes & 512) != 0).writeBoolean((inputDataTypes & 1024) != 0).writeLong(com.android.server.media.metrics.MediaMetricsManagerService.getBucketedDurationMillis(inputMediaItemInfo.getDurationMillis())).writeLong(com.android.server.media.metrics.MediaMetricsManagerService.getBucketedDurationMillis(inputMediaItemInfo.getClipDurationMillis())).writeString(com.android.server.media.metrics.MediaMetricsManagerService.getFilteredMimeType(inputMediaItemInfo.getContainerMimeType())).writeString(inputAudioSampleMimeType).writeString(inputVideoSampleMimeType).writeInt(com.android.server.media.metrics.MediaMetricsManagerService.getCodecEnum(inputVideoSampleMimeType)).writeInt(com.android.server.media.metrics.MediaMetricsManagerService.getFilteredAudioSampleRateHz(inputMediaItemInfo.getAudioSampleRateHz())).writeInt(inputMediaItemInfo.getAudioChannelCount()).writeLong(inputMediaItemInfo.getAudioSampleCount()).writeInt(inputVideoSize.getWidth()).writeInt(inputVideoSize.getHeight()).writeInt(inputVideoResolution).writeInt(com.android.server.media.metrics.MediaMetricsManagerService.getVideoResolutionAspectRatioEnum(inputVideoSize)).writeInt(inputMediaItemInfo.getVideoDataSpace()).writeInt(com.android.server.media.metrics.MediaMetricsManagerService.getVideoHdrFormatEnum(inputMediaItemInfo.getVideoDataSpace(), inputVideoSampleMimeType)).writeInt(java.lang.Math.round(inputMediaItemInfo.getVideoFrameRate())).writeInt(com.android.server.media.metrics.MediaMetricsManagerService.getVideoFrameRateEnum(inputMediaItemInfo.getVideoFrameRate())).writeString(inputFirstCodecName).writeString(inputSecondCodecName).writeBoolean((outputDataTypes & 1) != 0).writeBoolean((outputDataTypes & 2) != 0).writeBoolean((outputDataTypes & 4) != 0).writeBoolean((outputDataTypes & 8) != 0).writeBoolean((outputDataTypes & 16) != 0).writeBoolean((outputDataTypes & 32) != 0).writeBoolean((outputDataTypes & 64) != 0).writeBoolean((outputDataTypes & 128) != 0).writeBoolean((outputDataTypes & 256) != 0).writeBoolean((outputDataTypes & 512) != 0).writeBoolean((outputDataTypes & 1024) != 0).writeLong(com.android.server.media.metrics.MediaMetricsManagerService.getBucketedDurationMillis(outputMediaItemInfo.getDurationMillis())).writeLong(com.android.server.media.metrics.MediaMetricsManagerService.getBucketedDurationMillis(outputMediaItemInfo.getClipDurationMillis())).writeString(com.android.server.media.metrics.MediaMetricsManagerService.getFilteredMimeType(outputMediaItemInfo.getContainerMimeType())).writeString(outputAudioSampleMimeType).writeString(outputVideoSampleMimeType).writeInt(com.android.server.media.metrics.MediaMetricsManagerService.getCodecEnum(outputVideoSampleMimeType)).writeInt(com.android.server.media.metrics.MediaMetricsManagerService.getFilteredAudioSampleRateHz(outputMediaItemInfo.getAudioSampleRateHz())).writeInt(outputMediaItemInfo.getAudioChannelCount()).writeLong(outputMediaItemInfo.getAudioSampleCount()).writeInt(outputVideoSize.getWidth()).writeInt(outputVideoSize.getHeight()).writeInt(outputVideoResolution).writeInt(com.android.server.media.metrics.MediaMetricsManagerService.getVideoResolutionAspectRatioEnum(outputVideoSize)).writeInt(outputMediaItemInfo.getVideoDataSpace()).writeInt(com.android.server.media.metrics.MediaMetricsManagerService.getVideoHdrFormatEnum(outputMediaItemInfo.getVideoDataSpace(), outputVideoSampleMimeType)).writeInt(java.lang.Math.round(outputMediaItemInfo.getVideoFrameRate())).writeInt(com.android.server.media.metrics.MediaMetricsManagerService.getVideoFrameRateEnum(outputMediaItemInfo.getVideoFrameRate())).writeString(outputFirstCodecName).writeString(outputSecondCodecName).usePooledBuffer().build();
            android.util.StatsLog.write(statsEvent);
        }

        private int loggingLevel() {
            synchronized (com.android.server.media.metrics.MediaMetricsManagerService.this.mLock) {
                int uid = android.os.Binder.getCallingUid();
                if (com.android.server.media.metrics.MediaMetricsManagerService.this.mMode == null) {
                    long identity = android.os.Binder.clearCallingIdentity();
                    try {
                        com.android.server.media.metrics.MediaMetricsManagerService.this.mMode = java.lang.Integer.valueOf(android.provider.DeviceConfig.getInt("media", com.android.server.media.metrics.MediaMetricsManagerService.MEDIA_METRICS_MODE, 2));
                        android.os.Binder.restoreCallingIdentity(identity);
                    } catch (java.lang.Throwable th) {
                        android.os.Binder.restoreCallingIdentity(identity);
                        throw th;
                    }
                }
                if (com.android.server.media.metrics.MediaMetricsManagerService.this.mMode.intValue() == 1) {
                    return 0;
                }
                int iIntValue = com.android.server.media.metrics.MediaMetricsManagerService.this.mMode.intValue();
                int i = com.android.server.media.metrics.MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                if (iIntValue == 0) {
                    android.util.Slog.v(com.android.server.media.metrics.MediaMetricsManagerService.TAG, "Logging level blocked: MEDIA_METRICS_MODE_OFF");
                    return com.android.server.media.metrics.MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                }
                android.content.pm.PackageManager pm = com.android.server.media.metrics.MediaMetricsManagerService.this.getContext().getPackageManager();
                java.lang.String[] packages = pm.getPackagesForUid(uid);
                if (packages != null && packages.length != 0) {
                    if (com.android.server.media.metrics.MediaMetricsManagerService.this.mMode.intValue() == 2) {
                        if (com.android.server.media.metrics.MediaMetricsManagerService.this.mBlockList == null) {
                            com.android.server.media.metrics.MediaMetricsManagerService.this.mBlockList = com.android.server.media.metrics.MediaMetricsManagerService.this.getListLocked(com.android.server.media.metrics.MediaMetricsManagerService.PLAYER_METRICS_APP_BLOCKLIST);
                            if (com.android.server.media.metrics.MediaMetricsManagerService.this.mBlockList == null) {
                                android.util.Slog.v(com.android.server.media.metrics.MediaMetricsManagerService.TAG, "Logging level blocked: Failed to get PLAYER_METRICS_APP_BLOCKLIST.");
                                return com.android.server.media.metrics.MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                            }
                        }
                        java.lang.Integer level = loggingLevelInternal(packages, com.android.server.media.metrics.MediaMetricsManagerService.this.mBlockList, com.android.server.media.metrics.MediaMetricsManagerService.PLAYER_METRICS_APP_BLOCKLIST);
                        if (level != null) {
                            return level.intValue();
                        }
                        if (com.android.server.media.metrics.MediaMetricsManagerService.this.mNoUidBlocklist == null) {
                            com.android.server.media.metrics.MediaMetricsManagerService.this.mNoUidBlocklist = com.android.server.media.metrics.MediaMetricsManagerService.this.getListLocked(com.android.server.media.metrics.MediaMetricsManagerService.PLAYER_METRICS_PER_APP_ATTRIBUTION_BLOCKLIST);
                            if (com.android.server.media.metrics.MediaMetricsManagerService.this.mNoUidBlocklist == null) {
                                android.util.Slog.v(com.android.server.media.metrics.MediaMetricsManagerService.TAG, "Logging level blocked: Failed to get PLAYER_METRICS_PER_APP_ATTRIBUTION_BLOCKLIST.");
                                return com.android.server.media.metrics.MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                            }
                        }
                        java.lang.Integer level2 = loggingLevelInternal(packages, com.android.server.media.metrics.MediaMetricsManagerService.this.mNoUidBlocklist, com.android.server.media.metrics.MediaMetricsManagerService.PLAYER_METRICS_PER_APP_ATTRIBUTION_BLOCKLIST);
                        if (level2 == null) {
                            return 0;
                        }
                        return level2.intValue();
                    }
                    if (com.android.server.media.metrics.MediaMetricsManagerService.this.mMode.intValue() == 3) {
                        if (com.android.server.media.metrics.MediaMetricsManagerService.this.mNoUidAllowlist == null) {
                            com.android.server.media.metrics.MediaMetricsManagerService.this.mNoUidAllowlist = com.android.server.media.metrics.MediaMetricsManagerService.this.getListLocked(com.android.server.media.metrics.MediaMetricsManagerService.PLAYER_METRICS_PER_APP_ATTRIBUTION_ALLOWLIST);
                            if (com.android.server.media.metrics.MediaMetricsManagerService.this.mNoUidAllowlist == null) {
                                android.util.Slog.v(com.android.server.media.metrics.MediaMetricsManagerService.TAG, "Logging level blocked: Failed to get PLAYER_METRICS_PER_APP_ATTRIBUTION_ALLOWLIST.");
                                return com.android.server.media.metrics.MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                            }
                        }
                        java.lang.Integer level3 = loggingLevelInternal(packages, com.android.server.media.metrics.MediaMetricsManagerService.this.mNoUidAllowlist, com.android.server.media.metrics.MediaMetricsManagerService.PLAYER_METRICS_PER_APP_ATTRIBUTION_ALLOWLIST);
                        if (level3 != null) {
                            return level3.intValue();
                        }
                        if (com.android.server.media.metrics.MediaMetricsManagerService.this.mAllowlist == null) {
                            com.android.server.media.metrics.MediaMetricsManagerService.this.mAllowlist = com.android.server.media.metrics.MediaMetricsManagerService.this.getListLocked(com.android.server.media.metrics.MediaMetricsManagerService.PLAYER_METRICS_APP_ALLOWLIST);
                            if (com.android.server.media.metrics.MediaMetricsManagerService.this.mAllowlist == null) {
                                android.util.Slog.v(com.android.server.media.metrics.MediaMetricsManagerService.TAG, "Logging level blocked: Failed to get PLAYER_METRICS_APP_ALLOWLIST.");
                                return com.android.server.media.metrics.MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                            }
                        }
                        java.lang.Integer level4 = loggingLevelInternal(packages, com.android.server.media.metrics.MediaMetricsManagerService.this.mAllowlist, com.android.server.media.metrics.MediaMetricsManagerService.PLAYER_METRICS_APP_ALLOWLIST);
                        if (level4 != null) {
                            return level4.intValue();
                        }
                        android.util.Slog.v(com.android.server.media.metrics.MediaMetricsManagerService.TAG, "Logging level blocked: Not detected in any allowlist.");
                        return com.android.server.media.metrics.MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                    }
                    android.util.Slog.v(com.android.server.media.metrics.MediaMetricsManagerService.TAG, "Logging level blocked: Blocked by default.");
                    return com.android.server.media.metrics.MediaMetricsManagerService.LOGGING_LEVEL_BLOCKED;
                }
                android.util.Slog.d(com.android.server.media.metrics.MediaMetricsManagerService.TAG, "empty package from uid " + uid);
                if (com.android.server.media.metrics.MediaMetricsManagerService.this.mMode.intValue() == 2) {
                    i = 1000;
                }
                return i;
            }
        }

        private java.lang.Integer loggingLevelInternal(java.lang.String[] packages, java.util.List<java.lang.String> cached, java.lang.String listName) {
            if (inList(packages, cached)) {
                return java.lang.Integer.valueOf(listNameToLoggingLevel(listName));
            }
            return null;
        }

        private boolean inList(java.lang.String[] packages, java.util.List<java.lang.String> arr) {
            for (java.lang.String p : packages) {
                for (java.lang.String element : arr) {
                    if (p.equals(element)) {
                        return true;
                    }
                }
            }
            return false;
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARN: Removed duplicated region for block: B:17:0x0035  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        private int listNameToLoggingLevel(java.lang.String r4) {
            /*
                r3 = this;
                int r0 = r4.hashCode()
                r1 = 0
                switch(r0) {
                    case -1894232751: goto L2a;
                    case -1289480849: goto L1f;
                    case -789056333: goto L14;
                    case 1900310029: goto L9;
                    default: goto L8;
                }
            L8:
                goto L35
            L9:
                java.lang.String r0 = "player_metrics_per_app_attribution_allowlist"
                boolean r0 = r4.equals(r0)
                if (r0 == 0) goto L8
                r0 = 2
                goto L36
            L14:
                java.lang.String r0 = "player_metrics_app_blocklist"
                boolean r0 = r4.equals(r0)
                if (r0 == 0) goto L8
                r0 = r1
                goto L36
            L1f:
                java.lang.String r0 = "player_metrics_app_allowlist"
                boolean r0 = r4.equals(r0)
                if (r0 == 0) goto L8
                r0 = 1
                goto L36
            L2a:
                java.lang.String r0 = "player_metrics_per_app_attribution_blocklist"
                boolean r0 = r4.equals(r0)
                if (r0 == 0) goto L8
                r0 = 3
                goto L36
            L35:
                r0 = -1
            L36:
                r2 = 99999(0x1869f, float:1.40128E-40)
                switch(r0) {
                    case 0: goto L41;
                    case 1: goto L40;
                    case 2: goto L3d;
                    case 3: goto L3d;
                    default: goto L3c;
                }
            L3c:
                return r2
            L3d:
                r0 = 1000(0x3e8, float:1.401E-42)
                return r0
            L40:
                return r1
            L41:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.server.media.metrics.MediaMetricsManagerService.BinderService.listNameToLoggingLevel(java.lang.String):int");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getFilteredLibraryName(java.lang.String libraryName) {
        return (!android.text.TextUtils.isEmpty(libraryName) && PATTERN_KNOWN_EDITING_LIBRARY_NAMES.matcher(libraryName).matches()) ? libraryName : "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getThroughputFps(android.media.metrics.EditingEndedEvent event) {
        android.media.metrics.MediaItemInfo outputMediaItemInfo = event.getOutputMediaItemInfo();
        if (outputMediaItemInfo == null) {
            return -1;
        }
        long videoSampleCount = outputMediaItemInfo.getVideoSampleCount();
        if (videoSampleCount == -1) {
            return -1;
        }
        long elapsedTimeMs = event.getTimeSinceCreatedMillis();
        if (elapsedTimeMs == -1) {
            return -1;
        }
        return (int) java.lang.Math.min(2147483647L, java.lang.Math.round((videoSampleCount * 1000.0d) / elapsedTimeMs));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long getBucketedDurationMillis(long durationMillis) {
        if (durationMillis == -1 || durationMillis <= 0) {
            return -1L;
        }
        int bucketIndex = (int) java.lang.Math.floor((java.lang.Math.log((1 + durationMillis) / 60000.0d) / java.lang.Math.log(2.0d)) + 8.0d);
        return (long) java.lang.Math.ceil(java.lang.Math.pow(2.0d, java.lang.Math.min(13, java.lang.Math.max(0, bucketIndex)) - 8) * 60000.0d);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.String getFilteredFirstMimeType(java.util.List<java.lang.String> mimeTypes, java.lang.String prefix) {
        int size = mimeTypes.size();
        for (int i = 0; i < size; i++) {
            java.lang.String mimeType = mimeTypes.get(i);
            if (mimeType.startsWith(prefix)) {
                return getFilteredMimeType(mimeType);
            }
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:134:0x0207  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String getFilteredMimeType(java.lang.String r2) {
        /*
            Method dump skipped, instruction units count: 784
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.media.metrics.MediaMetricsManagerService.getFilteredMimeType(java.lang.String):java.lang.String");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:23:0x004b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int getCodecEnum(java.lang.String r6) {
        /*
            boolean r0 = android.text.TextUtils.isEmpty(r6)
            r1 = 0
            if (r0 == 0) goto L8
            return r1
        L8:
            int r0 = r6.hashCode()
            r2 = 4
            r3 = 3
            r4 = 2
            r5 = 1
            switch(r0) {
                case -1662735862: goto L40;
                case -1662541442: goto L35;
                case 1331836730: goto L2a;
                case 1599127256: goto L1f;
                case 1599127257: goto L14;
                default: goto L13;
            }
        L13:
            goto L4b
        L14:
            java.lang.String r0 = "video/x-vnd.on2.vp9"
            boolean r0 = r6.equals(r0)
            if (r0 == 0) goto L13
            r0 = r3
            goto L4c
        L1f:
            java.lang.String r0 = "video/x-vnd.on2.vp8"
            boolean r0 = r6.equals(r0)
            if (r0 == 0) goto L13
            r0 = r4
            goto L4c
        L2a:
            java.lang.String r0 = "video/avc"
            boolean r0 = r6.equals(r0)
            if (r0 == 0) goto L13
            r0 = r1
            goto L4c
        L35:
            java.lang.String r0 = "video/hevc"
            boolean r0 = r6.equals(r0)
            if (r0 == 0) goto L13
            r0 = r5
            goto L4c
        L40:
            java.lang.String r0 = "video/av01"
            boolean r0 = r6.equals(r0)
            if (r0 == 0) goto L13
            r0 = r2
            goto L4c
        L4b:
            r0 = -1
        L4c:
            switch(r0) {
                case 0: goto L58;
                case 1: goto L56;
                case 2: goto L54;
                case 3: goto L52;
                case 4: goto L50;
                default: goto L4f;
            }
        L4f:
            goto L59
        L50:
            r1 = 5
            goto L59
        L52:
            r1 = r2
            goto L59
        L54:
            r1 = r3
            goto L59
        L56:
            r1 = r4
            goto L59
        L58:
            r1 = r5
        L59:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.media.metrics.MediaMetricsManagerService.getCodecEnum(java.lang.String):int");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getFilteredAudioSampleRateHz(int sampleRateHz) {
        switch (sampleRateHz) {
            case com.android.server.EventLogTags.JOB_DEFERRED_EXECUTION /* 8000 */:
            case 11025:
            case 16000:
            case 22050:
            case 44100:
            case 48000:
            case 96000:
            case 192000:
                return sampleRateHz;
            default:
                return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getVideoResolutionEnum(android.util.Size size) {
        int width = size.getWidth();
        int height = size.getHeight();
        if (width == 352 && height == 640) {
            return 228;
        }
        if (width == 360 && height == 640) {
            return com.android.internal.util.FrameworkStatsLog.MEDIA_CODEC_RENDERED__RESOLUTION__RESOLUTION_360X640;
        }
        if (width == 480 && height == 640) {
            return 311;
        }
        if (width == 480 && height == 854) {
            return 414;
        }
        if (width == 540 && height == 960) {
            return 524;
        }
        if (width == 576 && height == 1024) {
            return com.android.internal.util.FrameworkStatsLog.MEDIA_CODEC_RENDERED__RESOLUTION__RESOLUTION_576X1024;
        }
        if (width == 1280 && height == 720) {
            return com.android.internal.util.FrameworkStatsLog.MEDIA_CODEC_RENDERED__RESOLUTION__RESOLUTION_720P_HD;
        }
        if (width == 1920 && height == 1080) {
            return com.android.internal.util.FrameworkStatsLog.MEDIA_CODEC_RENDERED__RESOLUTION__RESOLUTION_1080P_FHD;
        }
        if (width == 1440 && height == 2560) {
            return com.android.internal.util.FrameworkStatsLog.MEDIA_CODEC_RENDERED__RESOLUTION__RESOLUTION_1440X2560;
        }
        if (width == 3840 && height == 2160) {
            return com.android.internal.util.FrameworkStatsLog.MEDIA_CODEC_RENDERED__RESOLUTION__RESOLUTION_4K_UHD;
        }
        if (width == 7680 && height == 4320) {
            return com.android.internal.util.FrameworkStatsLog.MEDIA_CODEC_RENDERED__RESOLUTION__RESOLUTION_8K_UHD;
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getVideoResolutionAspectRatioEnum(android.util.Size size) {
        int width = size.getWidth();
        int height = size.getHeight();
        if (width <= 0 || height <= 0) {
            return 0;
        }
        if (width < height) {
            return 3;
        }
        if (height < width) {
            return 2;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getVideoHdrFormatEnum(int dataSpace, java.lang.String mimeType) {
        if (dataSpace == 0) {
            return 0;
        }
        if (mimeType.equals("video/dolby-vision")) {
            return 5;
        }
        int standard = android.hardware.DataSpace.getStandard(dataSpace);
        int transfer = android.hardware.DataSpace.getTransfer(dataSpace);
        if (standard == 393216 && transfer == 33554432) {
            return 2;
        }
        if (standard == 393216 && transfer == 29360128) {
            return 3;
        }
        return 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getVideoFrameRateEnum(float frameRate) {
        int frameRateInt = java.lang.Math.round(frameRate);
        switch (frameRateInt) {
            case 24:
                return 2400;
            case 25:
                return 2500;
            case 30:
                return 3000;
            case 50:
                return 5000;
            case 60:
                return 6000;
            case 120:
                return 12000;
            case com.android.internal.util.FrameworkStatsLog.BOOT_TIME_EVENT_ELAPSED_TIME_REPORTED /* 240 */:
                return 24000;
            case com.android.server.SystemService.PHASE_LOCK_SETTINGS_READY /* 480 */:
                return 48000;
            case 960:
                return 96000;
            default:
                return 0;
        }
    }
}
