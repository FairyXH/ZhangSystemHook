package com.android.server.voiceinteraction;

/* JADX INFO: loaded from: classes3.dex */
final class HotwordAudioStreamCopier {
    static final int DEFAULT_COPY_BUFFER_LENGTH_BYTES = 32768;
    static final int MAX_COPY_BUFFER_LENGTH_BYTES = 65536;
    private static final java.lang.String OP_MESSAGE = "Streaming hotword audio to VoiceInteractionService";
    private static final java.lang.String TAG = "HotwordAudioStreamCopier";
    private static final java.lang.String TASK_ID_PREFIX = "HotwordDetectedResult@";
    private static final java.lang.String THREAD_NAME_PREFIX = "Copy-";
    private final android.app.AppOpsManager mAppOpsManager;
    private final int mDetectorType;
    private final java.util.concurrent.ExecutorService mExecutorService = java.util.concurrent.Executors.newCachedThreadPool();
    private final java.lang.String mVoiceInteractorAttributionTag;
    private final java.lang.String mVoiceInteractorPackageName;
    private final int mVoiceInteractorUid;

    HotwordAudioStreamCopier(android.app.AppOpsManager appOpsManager, int detectorType, int voiceInteractorUid, java.lang.String voiceInteractorPackageName, java.lang.String voiceInteractorAttributionTag) {
        this.mAppOpsManager = appOpsManager;
        this.mDetectorType = detectorType;
        this.mVoiceInteractorUid = voiceInteractorUid;
        this.mVoiceInteractorPackageName = voiceInteractorPackageName;
        this.mVoiceInteractorAttributionTag = voiceInteractorAttributionTag;
    }

    public android.service.voice.HotwordDetectedResult startCopyingAudioStreams(android.service.voice.HotwordDetectedResult result) throws java.io.IOException {
        return startCopyingAudioStreams(result, true);
    }

    public android.service.voice.HotwordDetectedResult startCopyingAudioStreams(android.service.voice.HotwordDetectedResult result, boolean shouldNotifyAppOpsManager) throws java.io.IOException {
        android.os.ParcelFileDescriptor clientAudioSink;
        java.util.List<android.service.voice.HotwordAudioStream> audioStreams = result.getAudioStreams();
        if (audioStreams.isEmpty()) {
            com.android.server.voiceinteraction.HotwordMetricsLogger.writeAudioEgressEvent(this.mDetectorType, 7, this.mVoiceInteractorUid, 0, 0, 0);
            return result;
        }
        int audioStreamCount = audioStreams.size();
        java.util.List<android.service.voice.HotwordAudioStream> newAudioStreams = new java.util.ArrayList<>(audioStreams.size());
        java.util.List<com.android.server.voiceinteraction.HotwordAudioStreamCopier.CopyTaskInfo> copyTaskInfos = new java.util.ArrayList<>(audioStreams.size());
        int totalMetadataBundleSizeBytes = 0;
        int totalInitialAudioSizeBytes = 0;
        for (android.service.voice.HotwordAudioStream audioStream : audioStreams) {
            android.os.ParcelFileDescriptor[] clientPipe = android.os.ParcelFileDescriptor.createReliablePipe();
            android.os.ParcelFileDescriptor clientAudioSource = clientPipe[0];
            android.os.ParcelFileDescriptor clientAudioSink2 = clientPipe[1];
            android.service.voice.HotwordAudioStream newAudioStream = audioStream.buildUpon().setAudioStreamParcelFileDescriptor(clientAudioSource).build();
            newAudioStreams.add(newAudioStream);
            int copyBufferLength = 32768;
            android.os.PersistableBundle metadata = audioStream.getMetadata();
            totalMetadataBundleSizeBytes += android.service.voice.HotwordDetectedResult.getParcelableSize(metadata);
            if (metadata.containsKey("android.service.voice.key.AUDIO_STREAM_COPY_BUFFER_LENGTH_BYTES")) {
                copyBufferLength = metadata.getInt("android.service.voice.key.AUDIO_STREAM_COPY_BUFFER_LENGTH_BYTES", -1);
                if (copyBufferLength < 1 || copyBufferLength > 65536) {
                    int i = this.mDetectorType;
                    int copyBufferLength2 = this.mVoiceInteractorUid;
                    clientAudioSink = clientAudioSink2;
                    com.android.server.voiceinteraction.HotwordMetricsLogger.writeAudioEgressEvent(i, 9, copyBufferLength2, 0, 0, audioStreamCount);
                    android.util.Slog.w(TAG, "Attempted to set an invalid copy buffer length (" + copyBufferLength + ") for: " + audioStream);
                    copyBufferLength = 32768;
                } else {
                    clientAudioSink = clientAudioSink2;
                }
            } else {
                clientAudioSink = clientAudioSink2;
            }
            totalInitialAudioSizeBytes += audioStream.getInitialAudio().length;
            android.os.ParcelFileDescriptor serviceAudioSource = audioStream.getAudioStreamParcelFileDescriptor();
            copyTaskInfos.add(new com.android.server.voiceinteraction.HotwordAudioStreamCopier.CopyTaskInfo(serviceAudioSource, clientAudioSink, copyBufferLength));
        }
        java.lang.String resultTaskId = TASK_ID_PREFIX + java.lang.System.identityHashCode(result);
        this.mExecutorService.execute(new com.android.server.voiceinteraction.HotwordAudioStreamCopier.HotwordDetectedResultCopyTask(resultTaskId, copyTaskInfos, totalMetadataBundleSizeBytes, totalInitialAudioSizeBytes, shouldNotifyAppOpsManager));
        return result.buildUpon().setAudioStreams(newAudioStreams).build();
    }

    private static class CopyTaskInfo {
        private final int mCopyBufferLength;
        private final android.os.ParcelFileDescriptor mSink;
        private final android.os.ParcelFileDescriptor mSource;

        CopyTaskInfo(android.os.ParcelFileDescriptor source, android.os.ParcelFileDescriptor sink, int copyBufferLength) {
            this.mSource = source;
            this.mSink = sink;
            this.mCopyBufferLength = copyBufferLength;
        }
    }

    private class HotwordDetectedResultCopyTask implements java.lang.Runnable {
        private final java.util.List<com.android.server.voiceinteraction.HotwordAudioStreamCopier.CopyTaskInfo> mCopyTaskInfos;
        private final java.util.concurrent.ExecutorService mExecutorService = java.util.concurrent.Executors.newCachedThreadPool();
        private final java.lang.String mResultTaskId;
        private final boolean mShouldNotifyAppOpsManager;
        private final int mTotalInitialAudioSizeBytes;
        private final int mTotalMetadataSizeBytes;

        HotwordDetectedResultCopyTask(java.lang.String resultTaskId, java.util.List<com.android.server.voiceinteraction.HotwordAudioStreamCopier.CopyTaskInfo> copyTaskInfos, int totalMetadataSizeBytes, int totalInitialAudioSizeBytes, boolean shouldNotifyAppOpsManager) {
            this.mResultTaskId = resultTaskId;
            this.mCopyTaskInfos = copyTaskInfos;
            this.mTotalMetadataSizeBytes = totalMetadataSizeBytes;
            this.mTotalInitialAudioSizeBytes = totalInitialAudioSizeBytes;
            this.mShouldNotifyAppOpsManager = shouldNotifyAppOpsManager;
        }

        @Override // java.lang.Runnable
        public void run() {
            java.lang.Thread.currentThread().setName(com.android.server.voiceinteraction.HotwordAudioStreamCopier.THREAD_NAME_PREFIX + this.mResultTaskId);
            int size = this.mCopyTaskInfos.size();
            java.util.ArrayList<com.android.server.voiceinteraction.HotwordAudioStreamCopier.SingleAudioStreamCopyTask> arrayList = new java.util.ArrayList(size);
            for (int i = 0; i < size; i++) {
                com.android.server.voiceinteraction.HotwordAudioStreamCopier.CopyTaskInfo copyTaskInfo = this.mCopyTaskInfos.get(i);
                java.lang.String streamTaskId = this.mResultTaskId + "@" + i;
                arrayList.add(new com.android.server.voiceinteraction.HotwordAudioStreamCopier.SingleAudioStreamCopyTask(streamTaskId, copyTaskInfo.mSource, copyTaskInfo.mSink, copyTaskInfo.mCopyBufferLength, com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mDetectorType, com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorUid));
            }
            if (this.mShouldNotifyAppOpsManager && com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mAppOpsManager.startOpNoThrow("android:record_audio_hotword", com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorUid, com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorPackageName, com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorAttributionTag, com.android.server.voiceinteraction.HotwordAudioStreamCopier.OP_MESSAGE) != 0) {
                com.android.server.voiceinteraction.HotwordMetricsLogger.writeAudioEgressEvent(com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mDetectorType, 4, com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorUid, 0, 0, size);
                bestEffortPropagateError("Failed to obtain RECORD_AUDIO_HOTWORD permission for voice interactor with uid=" + com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorUid + " packageName=" + com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorPackageName + " attributionTag=" + com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorAttributionTag);
                return;
            }
            try {
                try {
                    com.android.server.voiceinteraction.HotwordMetricsLogger.writeAudioEgressEvent(com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mDetectorType, 1, com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorUid, this.mTotalInitialAudioSizeBytes, this.mTotalMetadataSizeBytes, size);
                    this.mExecutorService.invokeAll(arrayList);
                    int totalStreamSizeBytes = this.mTotalInitialAudioSizeBytes;
                    for (com.android.server.voiceinteraction.HotwordAudioStreamCopier.SingleAudioStreamCopyTask task : arrayList) {
                        totalStreamSizeBytes += task.mTotalCopiedBytes;
                    }
                    android.util.Slog.i(com.android.server.voiceinteraction.HotwordAudioStreamCopier.TAG, this.mResultTaskId + ": Task was completed. Total bytes egressed: " + totalStreamSizeBytes + " (including " + this.mTotalInitialAudioSizeBytes + " bytes NOT streamed), total metadata bundle size bytes: " + this.mTotalMetadataSizeBytes);
                    com.android.server.voiceinteraction.HotwordMetricsLogger.writeAudioEgressEvent(com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mDetectorType, 2, com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorUid, totalStreamSizeBytes, this.mTotalMetadataSizeBytes, size);
                    if (!this.mShouldNotifyAppOpsManager) {
                        return;
                    }
                } catch (java.lang.InterruptedException e) {
                    int totalStreamSizeBytes2 = this.mTotalInitialAudioSizeBytes;
                    int totalStreamSizeBytes3 = totalStreamSizeBytes2;
                    for (com.android.server.voiceinteraction.HotwordAudioStreamCopier.SingleAudioStreamCopyTask task2 : arrayList) {
                        totalStreamSizeBytes3 += task2.mTotalCopiedBytes;
                    }
                    com.android.server.voiceinteraction.HotwordMetricsLogger.writeAudioEgressEvent(com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mDetectorType, 3, com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorUid, totalStreamSizeBytes3, this.mTotalMetadataSizeBytes, size);
                    android.util.Slog.i(com.android.server.voiceinteraction.HotwordAudioStreamCopier.TAG, this.mResultTaskId + ": Task was interrupted. Total bytes egressed: " + totalStreamSizeBytes3 + " (including " + this.mTotalInitialAudioSizeBytes + " bytes NOT streamed), total metadata bundle size bytes: " + this.mTotalMetadataSizeBytes);
                    bestEffortPropagateError(e.getMessage());
                    if (!this.mShouldNotifyAppOpsManager) {
                        return;
                    }
                }
                com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mAppOpsManager.finishOp("android:record_audio_hotword", com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorUid, com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorPackageName, com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorAttributionTag);
            } catch (java.lang.Throwable th) {
                if (this.mShouldNotifyAppOpsManager) {
                    com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mAppOpsManager.finishOp("android:record_audio_hotword", com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorUid, com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorPackageName, com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorAttributionTag);
                }
                throw th;
            }
        }

        private void bestEffortPropagateError(java.lang.String errorMessage) {
            try {
                for (com.android.server.voiceinteraction.HotwordAudioStreamCopier.CopyTaskInfo copyTaskInfo : this.mCopyTaskInfos) {
                    copyTaskInfo.mSource.closeWithError(errorMessage);
                    copyTaskInfo.mSink.closeWithError(errorMessage);
                }
                com.android.server.voiceinteraction.HotwordMetricsLogger.writeAudioEgressEvent(com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mDetectorType, 10, com.android.server.voiceinteraction.HotwordAudioStreamCopier.this.mVoiceInteractorUid, 0, 0, this.mCopyTaskInfos.size());
            } catch (java.io.IOException e) {
                android.util.Slog.e(com.android.server.voiceinteraction.HotwordAudioStreamCopier.TAG, this.mResultTaskId + ": Failed to propagate error", e);
            }
        }
    }

    private static class SingleAudioStreamCopyTask implements java.util.concurrent.Callable<java.lang.Void> {
        private final android.os.ParcelFileDescriptor mAudioSink;
        private final android.os.ParcelFileDescriptor mAudioSource;
        private final int mCopyBufferLength;
        private final int mDetectorType;
        private final java.lang.String mStreamTaskId;
        private volatile int mTotalCopiedBytes = 0;
        private final int mUid;

        SingleAudioStreamCopyTask(java.lang.String streamTaskId, android.os.ParcelFileDescriptor audioSource, android.os.ParcelFileDescriptor audioSink, int copyBufferLength, int detectorType, int uid) {
            this.mStreamTaskId = streamTaskId;
            this.mAudioSource = audioSource;
            this.mAudioSink = audioSink;
            this.mCopyBufferLength = copyBufferLength;
            this.mDetectorType = detectorType;
            this.mUid = uid;
        }

        @Override // java.util.concurrent.Callable
        public java.lang.Void call() throws java.lang.Exception {
            java.lang.Thread.currentThread().setName(com.android.server.voiceinteraction.HotwordAudioStreamCopier.THREAD_NAME_PREFIX + this.mStreamTaskId);
            java.io.InputStream fis = null;
            java.io.OutputStream fos = null;
            try {
                try {
                    java.io.InputStream fis2 = new android.os.ParcelFileDescriptor.AutoCloseInputStream(this.mAudioSource);
                    fos = new android.os.ParcelFileDescriptor.AutoCloseOutputStream(this.mAudioSink);
                    byte[] buffer = new byte[this.mCopyBufferLength];
                    while (true) {
                        if (java.lang.Thread.interrupted()) {
                            android.util.Slog.e(com.android.server.voiceinteraction.HotwordAudioStreamCopier.TAG, this.mStreamTaskId + ": SingleAudioStreamCopyTask task was interrupted");
                            break;
                        }
                        int bytesRead = fis2.read(buffer);
                        if (bytesRead < 0) {
                            android.util.Slog.i(com.android.server.voiceinteraction.HotwordAudioStreamCopier.TAG, this.mStreamTaskId + ": Reached end of audio stream");
                            break;
                        }
                        if (bytesRead > 0) {
                            fos.write(buffer, 0, bytesRead);
                            this.mTotalCopiedBytes += bytesRead;
                        }
                    }
                    fis2.close();
                } catch (java.io.IOException e) {
                    this.mAudioSource.closeWithError(e.getMessage());
                    this.mAudioSink.closeWithError(e.getMessage());
                    android.util.Slog.i(com.android.server.voiceinteraction.HotwordAudioStreamCopier.TAG, this.mStreamTaskId + ": Failed to copy audio stream", e);
                    com.android.server.voiceinteraction.HotwordMetricsLogger.writeAudioEgressEvent(this.mDetectorType, 10, this.mUid, 0, 0, 0);
                    if (0 != 0) {
                        fis.close();
                    }
                    if (0 == 0) {
                        return null;
                    }
                }
                fos.close();
                return null;
            } catch (java.lang.Throwable th) {
                if (0 != 0) {
                    fis.close();
                }
                if (0 != 0) {
                    fos.close();
                }
                throw th;
            }
        }
    }
}
