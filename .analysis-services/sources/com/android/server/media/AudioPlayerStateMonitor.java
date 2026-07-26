package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
class AudioPlayerStateMonitor {
    private static final boolean DEBUG = com.android.server.media.MediaSessionService.DEBUG;
    private static java.lang.String TAG = "AudioPlayerStateMonitor";
    private static com.android.server.media.AudioPlayerStateMonitor sInstance;
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.Map<com.android.server.media.AudioPlayerStateMonitor.OnAudioPlayerActiveStateChangedListener, com.android.server.media.AudioPlayerStateMonitor.MessageHandler> mListenerMap = new android.util.ArrayMap();
    final java.util.Set<java.lang.Integer> mActiveAudioUids = new android.util.ArraySet();
    android.util.ArrayMap<java.lang.Integer, android.media.AudioPlaybackConfiguration> mPrevActiveAudioPlaybackConfigs = new android.util.ArrayMap<>();
    final java.util.List<java.lang.Integer> mSortedAudioPlaybackClientUids = new java.util.ArrayList();

    interface OnAudioPlayerActiveStateChangedListener {
        void onAudioPlayerActiveStateChanged(android.media.AudioPlaybackConfiguration audioPlaybackConfiguration, boolean z);
    }

    private static final class MessageHandler extends android.os.Handler {
        private static final int MSG_AUDIO_PLAYER_ACTIVE_STATE_CHANGED = 1;
        private final com.android.server.media.AudioPlayerStateMonitor.OnAudioPlayerActiveStateChangedListener mListener;

        MessageHandler(android.os.Looper looper, com.android.server.media.AudioPlayerStateMonitor.OnAudioPlayerActiveStateChangedListener listener) {
            super(looper);
            this.mListener = listener;
        }

        @Override // android.os.Handler
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    this.mListener.onAudioPlayerActiveStateChanged((android.media.AudioPlaybackConfiguration) msg.obj, msg.arg1 != 0);
                    break;
            }
        }

        void sendAudioPlayerActiveStateChangedMessage(android.media.AudioPlaybackConfiguration audioPlaybackConfiguration, boolean z) {
            obtainMessage(1, z ? 1 : 0, 0, audioPlaybackConfiguration).sendToTarget();
        }
    }

    static com.android.server.media.AudioPlayerStateMonitor getInstance(android.content.Context context) {
        com.android.server.media.AudioPlayerStateMonitor audioPlayerStateMonitor;
        synchronized (com.android.server.media.AudioPlayerStateMonitor.class) {
            if (sInstance == null) {
                sInstance = new com.android.server.media.AudioPlayerStateMonitor(context);
            }
            audioPlayerStateMonitor = sInstance;
        }
        return audioPlayerStateMonitor;
    }

    private AudioPlayerStateMonitor(android.content.Context context) {
        android.media.AudioManager am = (android.media.AudioManager) context.getSystemService("audio");
        am.registerAudioPlaybackCallback(new com.android.server.media.AudioPlayerStateMonitor.AudioManagerPlaybackListener(), null);
    }

    public void registerListener(com.android.server.media.AudioPlayerStateMonitor.OnAudioPlayerActiveStateChangedListener listener, android.os.Handler handler) {
        synchronized (this.mLock) {
            this.mListenerMap.put(listener, new com.android.server.media.AudioPlayerStateMonitor.MessageHandler(handler == null ? android.os.Looper.myLooper() : handler.getLooper(), listener));
        }
    }

    public void unregisterListener(com.android.server.media.AudioPlayerStateMonitor.OnAudioPlayerActiveStateChangedListener listener) {
        synchronized (this.mLock) {
            this.mListenerMap.remove(listener);
        }
    }

    public java.util.List<java.lang.Integer> getSortedAudioPlaybackClientUids() {
        java.util.List<java.lang.Integer> sortedAudioPlaybackClientUids = new java.util.ArrayList<>();
        synchronized (this.mLock) {
            sortedAudioPlaybackClientUids.addAll(this.mSortedAudioPlaybackClientUids);
        }
        return sortedAudioPlaybackClientUids;
    }

    public boolean hasUidPlayedAudioLast(int uid) {
        boolean z;
        synchronized (this.mLock) {
            z = false;
            if (!this.mSortedAudioPlaybackClientUids.isEmpty() && uid == this.mSortedAudioPlaybackClientUids.get(0).intValue()) {
                z = true;
            }
        }
        return z;
    }

    public boolean isPlaybackActive(int uid) {
        boolean zContains;
        synchronized (this.mLock) {
            zContains = this.mActiveAudioUids.contains(java.lang.Integer.valueOf(uid));
        }
        return zContains;
    }

    public void cleanUpAudioPlaybackUids(int mediaButtonSessionUid) {
        synchronized (this.mLock) {
            int userId = android.os.UserHandle.getUserHandleForUid(mediaButtonSessionUid).getIdentifier();
            for (int i = this.mSortedAudioPlaybackClientUids.size() - 1; i >= 0 && this.mSortedAudioPlaybackClientUids.get(i).intValue() != mediaButtonSessionUid; i--) {
                int uid = this.mSortedAudioPlaybackClientUids.get(i).intValue();
                if (userId == android.os.UserHandle.getUserHandleForUid(uid).getIdentifier() && !isPlaybackActive(uid)) {
                    this.mSortedAudioPlaybackClientUids.remove(i);
                }
            }
        }
    }

    public void dump(android.content.Context context, java.io.PrintWriter pw, java.lang.String prefix) {
        synchronized (this.mLock) {
            pw.println(prefix + "Audio playback (lastly played comes first)");
            java.lang.String indent = prefix + "  ";
            for (int i = 0; i < this.mSortedAudioPlaybackClientUids.size(); i++) {
                int uid = this.mSortedAudioPlaybackClientUids.get(i).intValue();
                pw.print(indent + "uid=" + uid + " packages=");
                java.lang.String[] packages = context.getPackageManager().getPackagesForUid(uid);
                if (packages != null && packages.length > 0) {
                    for (java.lang.String str : packages) {
                        pw.print(str + " ");
                    }
                }
                pw.println();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendAudioPlayerActiveStateChangedMessageLocked(android.media.AudioPlaybackConfiguration config, boolean isRemoved) {
        for (com.android.server.media.AudioPlayerStateMonitor.MessageHandler messageHandler : this.mListenerMap.values()) {
            messageHandler.sendAudioPlayerActiveStateChangedMessage(config, isRemoved);
        }
    }

    private class AudioManagerPlaybackListener extends android.media.AudioManager.AudioPlaybackCallback {
        private AudioManagerPlaybackListener() {
        }

        @Override // android.media.AudioManager.AudioPlaybackCallback
        public void onPlaybackConfigChanged(java.util.List<android.media.AudioPlaybackConfiguration> configs) {
            synchronized (com.android.server.media.AudioPlayerStateMonitor.this.mLock) {
                com.android.server.media.AudioPlayerStateMonitor.this.mActiveAudioUids.clear();
                android.util.ArrayMap<java.lang.Integer, android.media.AudioPlaybackConfiguration> activeAudioPlaybackConfigs = new android.util.ArrayMap<>();
                for (android.media.AudioPlaybackConfiguration config : configs) {
                    if (config.isActive()) {
                        com.android.server.media.AudioPlayerStateMonitor.this.mActiveAudioUids.add(java.lang.Integer.valueOf(config.getClientUid()));
                        activeAudioPlaybackConfigs.put(java.lang.Integer.valueOf(config.getPlayerInterfaceId()), config);
                    }
                }
                for (int i = 0; i < activeAudioPlaybackConfigs.size(); i++) {
                    android.media.AudioPlaybackConfiguration config2 = activeAudioPlaybackConfigs.valueAt(i);
                    int uid = config2.getClientUid();
                    if (!com.android.server.media.AudioPlayerStateMonitor.this.mPrevActiveAudioPlaybackConfigs.containsKey(java.lang.Integer.valueOf(config2.getPlayerInterfaceId()))) {
                        if (com.android.server.media.AudioPlayerStateMonitor.DEBUG) {
                            android.util.Log.d(com.android.server.media.AudioPlayerStateMonitor.TAG, "Found a new active media playback. " + config2);
                        }
                        int index = com.android.server.media.AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids.indexOf(java.lang.Integer.valueOf(uid));
                        if (index != 0) {
                            if (index > 0) {
                                com.android.server.media.AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids.remove(index);
                            }
                            com.android.server.media.AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids.add(0, java.lang.Integer.valueOf(uid));
                        }
                    }
                }
                if (com.android.server.media.AudioPlayerStateMonitor.this.mActiveAudioUids.size() > 0 && !com.android.server.media.AudioPlayerStateMonitor.this.mActiveAudioUids.contains(com.android.server.media.AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids.get(0))) {
                    int firstActiveUid = -1;
                    int firstActiveUidIndex = -1;
                    int i2 = 1;
                    while (true) {
                        if (i2 >= com.android.server.media.AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids.size()) {
                            break;
                        }
                        int uid2 = com.android.server.media.AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids.get(i2).intValue();
                        if (!com.android.server.media.AudioPlayerStateMonitor.this.mActiveAudioUids.contains(java.lang.Integer.valueOf(uid2))) {
                            i2++;
                        } else {
                            firstActiveUidIndex = i2;
                            firstActiveUid = uid2;
                            break;
                        }
                    }
                    for (int i3 = firstActiveUidIndex; i3 > 0; i3--) {
                        com.android.server.media.AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids.set(i3, com.android.server.media.AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids.get(i3 - 1));
                    }
                    com.android.server.media.AudioPlayerStateMonitor.this.mSortedAudioPlaybackClientUids.set(0, java.lang.Integer.valueOf(firstActiveUid));
                }
                java.util.Iterator<android.media.AudioPlaybackConfiguration> it = configs.iterator();
                while (true) {
                    boolean wasActive = true;
                    if (!it.hasNext()) {
                        break;
                    }
                    android.media.AudioPlaybackConfiguration config3 = it.next();
                    int pii = config3.getPlayerInterfaceId();
                    if (com.android.server.media.AudioPlayerStateMonitor.this.mPrevActiveAudioPlaybackConfigs.remove(java.lang.Integer.valueOf(pii)) == null) {
                        wasActive = false;
                    }
                    if (wasActive != config3.isActive()) {
                        com.android.server.media.AudioPlayerStateMonitor.this.sendAudioPlayerActiveStateChangedMessageLocked(config3, false);
                    }
                }
                java.util.Iterator<android.media.AudioPlaybackConfiguration> it2 = com.android.server.media.AudioPlayerStateMonitor.this.mPrevActiveAudioPlaybackConfigs.values().iterator();
                while (it2.hasNext()) {
                    com.android.server.media.AudioPlayerStateMonitor.this.sendAudioPlayerActiveStateChangedMessageLocked(it2.next(), true);
                }
                com.android.server.media.AudioPlayerStateMonitor.this.mPrevActiveAudioPlaybackConfigs = activeAudioPlaybackConfigs;
            }
        }
    }
}
