package com.android.server.slice;

/* JADX INFO: loaded from: classes3.dex */
public class PinnedSliceState {
    private static final long SLICE_TIMEOUT = 5000;
    private static final java.lang.String TAG = "PinnedSliceState";
    private final java.lang.Object mLock;
    private final java.lang.String mPkg;
    private final com.android.server.slice.SliceManagerService mService;
    private boolean mSlicePinned;
    private final android.net.Uri mUri;
    private final android.util.ArraySet<java.lang.String> mPinnedPkgs = new android.util.ArraySet<>();
    private final android.util.ArrayMap<android.os.IBinder, com.android.server.slice.PinnedSliceState.ListenerInfo> mListeners = new android.util.ArrayMap<>();
    private android.app.slice.SliceSpec[] mSupportedSpecs = null;
    private final android.os.IBinder.DeathRecipient mDeathRecipient = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.slice.PinnedSliceState$$ExternalSyntheticLambda0
        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            this.f$0.handleRecheckListeners();
        }
    };

    public PinnedSliceState(com.android.server.slice.SliceManagerService service, android.net.Uri uri, java.lang.String pkg) {
        this.mService = service;
        this.mUri = uri;
        this.mPkg = pkg;
        this.mLock = this.mService.getLock();
    }

    public java.lang.String getPkg() {
        return this.mPkg;
    }

    public android.app.slice.SliceSpec[] getSpecs() {
        return this.mSupportedSpecs;
    }

    public void mergeSpecs(final android.app.slice.SliceSpec[] supportedSpecs) {
        synchronized (this.mLock) {
            if (this.mSupportedSpecs == null) {
                this.mSupportedSpecs = supportedSpecs;
            } else {
                java.util.List<android.app.slice.SliceSpec> specs = java.util.Arrays.asList(this.mSupportedSpecs);
                this.mSupportedSpecs = (android.app.slice.SliceSpec[]) specs.stream().map(new java.util.function.Function() { // from class: com.android.server.slice.PinnedSliceState$$ExternalSyntheticLambda3
                    @Override // java.util.function.Function
                    public final java.lang.Object apply(java.lang.Object obj) {
                        return this.f$0.lambda$mergeSpecs$0(supportedSpecs, (android.app.slice.SliceSpec) obj);
                    }
                }).filter(new java.util.function.Predicate() { // from class: com.android.server.slice.PinnedSliceState$$ExternalSyntheticLambda4
                    @Override // java.util.function.Predicate
                    public final boolean test(java.lang.Object obj) {
                        return com.android.server.slice.PinnedSliceState.lambda$mergeSpecs$1((android.app.slice.SliceSpec) obj);
                    }
                }).toArray(new java.util.function.IntFunction() { // from class: com.android.server.slice.PinnedSliceState$$ExternalSyntheticLambda5
                    @Override // java.util.function.IntFunction
                    public final java.lang.Object apply(int i) {
                        return com.android.server.slice.PinnedSliceState.lambda$mergeSpecs$2(i);
                    }
                });
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ android.app.slice.SliceSpec lambda$mergeSpecs$0(android.app.slice.SliceSpec[] supportedSpecs, android.app.slice.SliceSpec s) {
        android.app.slice.SliceSpec other = findSpec(supportedSpecs, s.getType());
        if (other == null) {
            return null;
        }
        if (other.getRevision() < s.getRevision()) {
            return other;
        }
        return s;
    }

    static /* synthetic */ boolean lambda$mergeSpecs$1(android.app.slice.SliceSpec s) {
        return s != null;
    }

    static /* synthetic */ android.app.slice.SliceSpec[] lambda$mergeSpecs$2(int x$0) {
        return new android.app.slice.SliceSpec[x$0];
    }

    private android.app.slice.SliceSpec findSpec(android.app.slice.SliceSpec[] specs, java.lang.String type) {
        for (android.app.slice.SliceSpec spec : specs) {
            if (java.util.Objects.equals(spec.getType(), type)) {
                return spec;
            }
        }
        return null;
    }

    public android.net.Uri getUri() {
        return this.mUri;
    }

    public void destroy() {
        setSlicePinned(false);
    }

    private void setSlicePinned(boolean pinned) {
        synchronized (this.mLock) {
            if (this.mSlicePinned == pinned) {
                return;
            }
            this.mSlicePinned = pinned;
            if (pinned) {
                this.mService.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.slice.PinnedSliceState$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.handleSendPinned();
                    }
                });
            } else {
                this.mService.getHandler().post(new java.lang.Runnable() { // from class: com.android.server.slice.PinnedSliceState$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.handleSendUnpinned();
                    }
                });
            }
        }
    }

    public void pin(java.lang.String pkg, android.app.slice.SliceSpec[] specs, android.os.IBinder token) {
        synchronized (this.mLock) {
            this.mListeners.put(token, new com.android.server.slice.PinnedSliceState.ListenerInfo(token, pkg, true, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid()));
            try {
                token.linkToDeath(this.mDeathRecipient, 0);
            } catch (android.os.RemoteException e) {
            }
            mergeSpecs(specs);
            setSlicePinned(true);
        }
    }

    public boolean unpin(java.lang.String pkg, android.os.IBinder token) {
        synchronized (this.mLock) {
            token.unlinkToDeath(this.mDeathRecipient, 0);
            this.mListeners.remove(token);
        }
        return !hasPinOrListener();
    }

    public boolean isListening() {
        boolean z;
        synchronized (this.mLock) {
            z = !this.mListeners.isEmpty();
        }
        return z;
    }

    public boolean hasPinOrListener() {
        boolean z;
        synchronized (this.mLock) {
            z = (this.mPinnedPkgs.isEmpty() && this.mListeners.isEmpty()) ? false : true;
        }
        return z;
    }

    android.content.ContentProviderClient getClient() {
        android.content.ContentProviderClient client;
        try {
            client = this.mService.getContext().getContentResolver().acquireUnstableContentProviderClient(this.mUri);
        } catch (java.lang.SecurityException e) {
            android.util.Log.w(TAG, "Exception when acquire provider " + this.mUri, e);
            client = null;
        }
        if (client == null) {
            return null;
        }
        client.setDetectNotResponding(SLICE_TIMEOUT);
        return client;
    }

    private void checkSelfRemove() {
        if (!hasPinOrListener()) {
            this.mService.removePinnedSlice(this.mUri);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleRecheckListeners() {
        if (hasPinOrListener()) {
            synchronized (this.mLock) {
                for (int i = this.mListeners.size() - 1; i >= 0; i--) {
                    com.android.server.slice.PinnedSliceState.ListenerInfo l = this.mListeners.valueAt(i);
                    if (!l.token.isBinderAlive()) {
                        this.mListeners.removeAt(i);
                    }
                }
                checkSelfRemove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSendPinned() {
        android.content.ContentProviderClient client = getClient();
        if (client != null) {
            try {
                android.os.Bundle b = new android.os.Bundle();
                b.putParcelable("slice_uri", this.mUri);
                try {
                    client.call("pin", null, b);
                } catch (java.lang.Exception e) {
                    android.util.Log.w(TAG, "Unable to contact " + this.mUri, e);
                }
                if (client != null) {
                    client.close();
                    return;
                }
                return;
            } catch (java.lang.Throwable th) {
                if (client != null) {
                    try {
                        client.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (client != null) {
            client.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleSendUnpinned() {
        android.content.ContentProviderClient client = getClient();
        if (client != null) {
            try {
                android.os.Bundle b = new android.os.Bundle();
                b.putParcelable("slice_uri", this.mUri);
                try {
                    client.call("unpin", null, b);
                } catch (java.lang.Exception e) {
                    android.util.Log.w(TAG, "Unable to contact " + this.mUri, e);
                }
                if (client != null) {
                    client.close();
                    return;
                }
                return;
            } catch (java.lang.Throwable th) {
                if (client != null) {
                    try {
                        client.close();
                    } catch (java.lang.Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        if (client != null) {
            client.close();
        }
    }

    private class ListenerInfo {
        private int callingPid;
        private int callingUid;
        private boolean hasPermission;
        private java.lang.String pkg;
        private android.os.IBinder token;

        public ListenerInfo(android.os.IBinder token, java.lang.String pkg, boolean hasPermission, int callingUid, int callingPid) {
            this.token = token;
            this.pkg = pkg;
            this.hasPermission = hasPermission;
            this.callingUid = callingUid;
            this.callingPid = callingPid;
        }
    }
}
