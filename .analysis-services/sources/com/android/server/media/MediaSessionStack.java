package com.android.server.media;

/* JADX INFO: loaded from: classes2.dex */
class MediaSessionStack {
    private static final boolean DEBUG = com.android.server.media.MediaSessionService.DEBUG;
    private static final java.lang.String TAG = "MediaSessionStack";
    private final com.android.server.media.AudioPlayerStateMonitor mAudioPlayerStateMonitor;
    private com.android.server.media.MediaSessionRecordImpl mMediaButtonSession;
    private final com.android.server.media.MediaSessionStack.OnMediaButtonSessionChangedListener mOnMediaButtonSessionChangedListener;
    private final java.util.List<com.android.server.media.MediaSessionRecordImpl> mSessions = new java.util.ArrayList();
    private final android.util.SparseArray<java.util.List<com.android.server.media.MediaSessionRecord>> mCachedActiveLists = new android.util.SparseArray<>();

    interface OnMediaButtonSessionChangedListener {
        void onMediaButtonSessionChanged(com.android.server.media.MediaSessionRecordImpl mediaSessionRecordImpl, com.android.server.media.MediaSessionRecordImpl mediaSessionRecordImpl2);
    }

    MediaSessionStack(com.android.server.media.AudioPlayerStateMonitor monitor, com.android.server.media.MediaSessionStack.OnMediaButtonSessionChangedListener listener) {
        this.mAudioPlayerStateMonitor = monitor;
        this.mOnMediaButtonSessionChangedListener = listener;
    }

    public void addSession(com.android.server.media.MediaSessionRecordImpl record) {
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("addSession to bottom of stack | record: %s", new java.lang.Object[]{record}));
        this.mSessions.add(record);
        clearCache(record.getUserId());
        updateMediaButtonSessionIfNeeded();
    }

    public void removeSession(com.android.server.media.MediaSessionRecordImpl record) {
        android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("removeSession | record: %s", new java.lang.Object[]{record}));
        this.mSessions.remove(record);
        if (this.mMediaButtonSession == record) {
            updateMediaButtonSession(null);
        }
        clearCache(record.getUserId());
    }

    public boolean contains(com.android.server.media.MediaSessionRecordImpl record) {
        return this.mSessions.contains(record);
    }

    public com.android.server.media.MediaSessionRecord getMediaSessionRecord(android.media.session.MediaSession.Token sessionToken) {
        for (com.android.server.media.MediaSessionRecordImpl record : this.mSessions) {
            if (record instanceof com.android.server.media.MediaSessionRecord) {
                com.android.server.media.MediaSessionRecord session1 = (com.android.server.media.MediaSessionRecord) record;
                if (java.util.Objects.equals(session1.getSessionToken(), sessionToken)) {
                    return session1;
                }
            }
        }
        return null;
    }

    public void onPlaybackStateChanged(com.android.server.media.MediaSessionRecordImpl record, boolean shouldUpdatePriority) {
        com.android.server.media.MediaSessionRecordImpl newMediaButtonSession;
        if (shouldUpdatePriority) {
            android.util.Slog.i(TAG, android.text.TextUtils.formatSimple("onPlaybackStateChanged - Pushing session to top | record: %s", new java.lang.Object[]{record}));
            this.mSessions.remove(record);
            this.mSessions.add(0, record);
            clearCache(record.getUserId());
        }
        if (this.mMediaButtonSession != null && this.mMediaButtonSession.getUid() == record.getUid() && (newMediaButtonSession = findMediaButtonSession(this.mMediaButtonSession.getUid())) != this.mMediaButtonSession && (newMediaButtonSession.getSessionPolicies() & 2) == 0) {
            updateMediaButtonSession(newMediaButtonSession);
        }
    }

    public void onSessionActiveStateChanged(com.android.server.media.MediaSessionRecordImpl record) {
        clearCache(record.getUserId());
    }

    public void updateMediaButtonSessionIfNeeded() {
        if (DEBUG) {
            android.util.Log.d(TAG, "updateMediaButtonSessionIfNeeded, callers=" + getCallers(2));
        }
        java.util.List<java.lang.Integer> audioPlaybackUids = this.mAudioPlayerStateMonitor.getSortedAudioPlaybackClientUids();
        for (int i = 0; i < audioPlaybackUids.size(); i++) {
            int audioPlaybackUid = audioPlaybackUids.get(i).intValue();
            com.android.server.media.MediaSessionRecordImpl mediaButtonSession = findMediaButtonSession(audioPlaybackUid);
            if (mediaButtonSession == null) {
                if (DEBUG) {
                    android.util.Log.d(TAG, "updateMediaButtonSessionIfNeeded, skipping uid=" + audioPlaybackUid);
                }
            } else {
                boolean ignoreButtonSession = (mediaButtonSession.getSessionPolicies() & 2) != 0;
                if (DEBUG) {
                    android.util.Log.d(TAG, "updateMediaButtonSessionIfNeeded, checking uid=" + audioPlaybackUid + ", mediaButtonSession=" + mediaButtonSession + ", ignoreButtonSession=" + ignoreButtonSession);
                }
                if (!ignoreButtonSession) {
                    this.mAudioPlayerStateMonitor.cleanUpAudioPlaybackUids(mediaButtonSession.getUid());
                    if (mediaButtonSession != this.mMediaButtonSession) {
                        updateMediaButtonSession(mediaButtonSession);
                        return;
                    }
                    return;
                }
            }
        }
    }

    public void updateMediaButtonSessionBySessionPolicyChange(com.android.server.media.MediaSessionRecord record) {
        if ((record.getSessionPolicies() & 2) != 0) {
            if (record == this.mMediaButtonSession) {
                updateMediaButtonSession(null);
                return;
            }
            return;
        }
        updateMediaButtonSessionIfNeeded();
    }

    private com.android.server.media.MediaSessionRecordImpl findMediaButtonSession(int uid) {
        com.android.server.media.MediaSessionRecordImpl mediaButtonSession = null;
        for (com.android.server.media.MediaSessionRecordImpl session : this.mSessions) {
            if (!(session instanceof com.android.server.media.MediaSession2Record) && uid == session.getUid()) {
                if (session.checkPlaybackActiveState(this.mAudioPlayerStateMonitor.isPlaybackActive(session.getUid()))) {
                    return session;
                }
                if (mediaButtonSession == null) {
                    mediaButtonSession = session;
                }
            }
        }
        return mediaButtonSession;
    }

    public java.util.List<com.android.server.media.MediaSessionRecord> getActiveSessions(int userId) {
        java.util.List<com.android.server.media.MediaSessionRecord> cachedActiveList = this.mCachedActiveLists.get(userId);
        if (cachedActiveList == null) {
            java.util.List<com.android.server.media.MediaSessionRecord> cachedActiveList2 = getPriorityList(true, userId);
            this.mCachedActiveLists.put(userId, cachedActiveList2);
            return cachedActiveList2;
        }
        return cachedActiveList;
    }

    public java.util.List<android.media.Session2Token> getSession2Tokens(int userId) {
        java.util.ArrayList<android.media.Session2Token> session2Records = new java.util.ArrayList<>();
        for (com.android.server.media.MediaSessionRecordImpl record : this.mSessions) {
            if (userId == -1 || record.getUserId() == userId) {
                if (record.isActive() && (record instanceof com.android.server.media.MediaSession2Record)) {
                    com.android.server.media.MediaSession2Record session2 = (com.android.server.media.MediaSession2Record) record;
                    session2Records.add(session2.getSession2Token());
                }
            }
        }
        return session2Records;
    }

    public com.android.server.media.MediaSessionRecordImpl getMediaButtonSession() {
        if (this.mMediaButtonSession != null) {
            android.util.Log.d(TAG, "getMediaButtonSession() mMediaButtonSession=" + this.mMediaButtonSession);
            return this.mMediaButtonSession;
        }
        updateMediaButtonSessionIfNeeded();
        int size = this.mSessions.size();
        if (this.mMediaButtonSession == null && size > 0) {
            android.util.Log.d(TAG, "getMediaButtonSession, mSessions.get(0)");
            return this.mSessions.get(0);
        }
        return this.mMediaButtonSession;
    }

    public void updateMediaButtonSession(com.android.server.media.MediaSessionRecordImpl newMediaButtonSession) {
        com.android.server.media.MediaSessionRecordImpl oldMediaButtonSession = this.mMediaButtonSession;
        this.mMediaButtonSession = newMediaButtonSession;
        this.mOnMediaButtonSessionChangedListener.onMediaButtonSessionChanged(oldMediaButtonSession, newMediaButtonSession);
    }

    public com.android.server.media.MediaSessionRecordImpl getDefaultVolumeSession() {
        java.util.List<com.android.server.media.MediaSessionRecord> records = getPriorityList(true, -1);
        int size = records.size();
        for (int i = 0; i < size; i++) {
            com.android.server.media.MediaSessionRecord record = records.get(i);
            if (record.checkPlaybackActiveState(true) && record.canHandleVolumeKey()) {
                return record;
            }
        }
        return null;
    }

    public com.android.server.media.MediaSessionRecordImpl getDefaultRemoteSession(int userId) {
        java.util.List<com.android.server.media.MediaSessionRecord> records = getPriorityList(true, userId);
        int size = records.size();
        for (int i = 0; i < size; i++) {
            com.android.server.media.MediaSessionRecord record = records.get(i);
            if (!record.isPlaybackTypeLocal()) {
                return record;
            }
        }
        return null;
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.println(prefix + "Media button session is " + this.mMediaButtonSession);
        pw.println(prefix + "Sessions Stack - have " + this.mSessions.size() + " sessions:");
        java.lang.String indent = prefix + "  ";
        for (com.android.server.media.MediaSessionRecordImpl record : this.mSessions) {
            record.dump(pw, indent);
        }
    }

    public java.util.List<com.android.server.media.MediaSessionRecord> getPriorityList(boolean activeOnly, int userId) {
        java.util.List<com.android.server.media.MediaSessionRecord> result = new java.util.ArrayList<>();
        int lastPlaybackActiveIndex = 0;
        int lastActiveIndex = 0;
        for (com.android.server.media.MediaSessionRecordImpl record : this.mSessions) {
            if (record instanceof com.android.server.media.MediaSessionRecord) {
                com.android.server.media.MediaSessionRecord session = (com.android.server.media.MediaSessionRecord) record;
                if (userId == -1 || userId == session.getUserId()) {
                    if (!session.isActive()) {
                        if (!activeOnly) {
                            result.add(session);
                        }
                    } else if (session.checkPlaybackActiveState(true)) {
                        result.add(lastPlaybackActiveIndex, session);
                        lastActiveIndex++;
                        lastPlaybackActiveIndex++;
                    } else {
                        int lastPlaybackActiveIndex2 = lastActiveIndex + 1;
                        result.add(lastActiveIndex, session);
                        lastActiveIndex = lastPlaybackActiveIndex2;
                    }
                }
            }
        }
        return result;
    }

    private void clearCache(int userId) {
        this.mCachedActiveLists.remove(userId);
        this.mCachedActiveLists.remove(-1);
    }

    private static java.lang.String getCallers(int depth) {
        java.lang.StackTraceElement[] callStack = java.lang.Thread.currentThread().getStackTrace();
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append(getCaller(callStack, i)).append(" ");
        }
        return sb.toString();
    }

    private static java.lang.String getCaller(java.lang.StackTraceElement[] callStack, int depth) {
        if (depth + 4 >= callStack.length) {
            return "<bottom of call stack>";
        }
        java.lang.StackTraceElement caller = callStack[depth + 4];
        return caller.getClassName() + "." + caller.getMethodName() + ":" + caller.getLineNumber();
    }
}
