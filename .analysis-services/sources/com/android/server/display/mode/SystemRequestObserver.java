package com.android.server.display.mode;

/* JADX INFO: loaded from: classes2.dex */
class SystemRequestObserver {
    private final com.android.server.display.mode.VotesStorage mVotesStorage;
    private final android.os.IBinder.DeathRecipient mDeathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.display.mode.SystemRequestObserver.1
        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied(android.os.IBinder who) {
            com.android.server.display.mode.SystemRequestObserver.this.removeSystemRequestedVotes(who);
            who.unlinkToDeath(com.android.server.display.mode.SystemRequestObserver.this.mDeathRecipient, 0);
        }
    };
    private final java.lang.Object mLock = new java.lang.Object();
    private final java.util.Map<android.os.IBinder, android.util.SparseArray<java.util.List<java.lang.Integer>>> mDisplaysRestrictions = new java.util.HashMap();

    SystemRequestObserver(com.android.server.display.mode.VotesStorage storage) {
        this.mVotesStorage = storage;
    }

    void requestDisplayModes(android.os.IBinder token, int displayId, int[] modeIds) {
        if (modeIds == null) {
            removeSystemRequestedVote(token, displayId);
        } else {
            addSystemRequestedVote(token, displayId, modeIds);
        }
    }

    private void addSystemRequestedVote(android.os.IBinder token, int displayId, int[] modeIds) {
        boolean needLinkToDeath = false;
        try {
            java.util.List<java.lang.Integer> modeIdsList = new java.util.ArrayList<>();
            for (int mode : modeIds) {
                modeIdsList.add(java.lang.Integer.valueOf(mode));
            }
            synchronized (this.mLock) {
                android.util.SparseArray<java.util.List<java.lang.Integer>> modesByDisplay = this.mDisplaysRestrictions.get(token);
                if (modesByDisplay == null) {
                    needLinkToDeath = true;
                    modesByDisplay = new android.util.SparseArray<>();
                    this.mDisplaysRestrictions.put(token, modesByDisplay);
                }
                modesByDisplay.put(displayId, modeIdsList);
                updateStorageLocked(displayId);
            }
            if (needLinkToDeath) {
                token.linkToDeath(this.mDeathRecipient, 0);
            }
        } catch (android.os.RemoteException e) {
            removeSystemRequestedVotes(token);
        }
    }

    private void removeSystemRequestedVote(android.os.IBinder token, int displayId) {
        boolean needToUnlink = false;
        synchronized (this.mLock) {
            android.util.SparseArray<java.util.List<java.lang.Integer>> modesByDisplay = this.mDisplaysRestrictions.get(token);
            if (modesByDisplay != null) {
                modesByDisplay.remove(displayId);
                needToUnlink = modesByDisplay.size() == 0;
                updateStorageLocked(displayId);
            }
        }
        if (needToUnlink) {
            token.unlinkToDeath(this.mDeathRecipient, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeSystemRequestedVotes(android.os.IBinder token) {
        synchronized (this.mLock) {
            android.util.SparseArray<java.util.List<java.lang.Integer>> removed = this.mDisplaysRestrictions.remove(token);
            if (removed != null) {
                for (int i = 0; i < removed.size(); i++) {
                    updateStorageLocked(removed.keyAt(i));
                }
            }
        }
    }

    private void updateStorageLocked(final int displayId) {
        final java.util.List<java.lang.Integer> modeIds = new java.util.ArrayList<>();
        final boolean[] modesFound = new boolean[1];
        this.mDisplaysRestrictions.forEach(new java.util.function.BiConsumer() { // from class: com.android.server.display.mode.SystemRequestObserver$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(java.lang.Object obj, java.lang.Object obj2) {
                com.android.server.display.mode.SystemRequestObserver.lambda$updateStorageLocked$0(displayId, modesFound, modeIds, (android.os.IBinder) obj, (android.util.SparseArray) obj2);
            }
        });
        this.mVotesStorage.updateVote(displayId, 14, modesFound[0] ? com.android.server.display.mode.Vote.forSupportedModes(modeIds) : null);
    }

    static /* synthetic */ void lambda$updateStorageLocked$0(int displayId, boolean[] modesFound, java.util.List modeIds, android.os.IBinder key, android.util.SparseArray value) {
        java.util.List<java.lang.Integer> modesForDisplay = (java.util.List) value.get(displayId);
        if (modesForDisplay != null) {
            if (!modesFound[0]) {
                modeIds.addAll(modesForDisplay);
                modesFound[0] = true;
            } else {
                modeIds.retainAll(modesForDisplay);
            }
        }
    }
}
