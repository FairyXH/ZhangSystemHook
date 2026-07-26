package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class FocusRequester {
    private static final java.lang.String AUDIOFOCUS_BLACK_PACKAGENAME = "audioramp-black-list";
    private static final boolean DEBUG;
    private static final java.lang.String TAG = "FocusRequester";
    private final android.media.AudioAttributes mAttributes;
    private final int mCallingUid;
    private final java.lang.String mClientId;
    private com.android.server.audio.MediaFocusControl.AudioFocusDeathHandler mDeathHandler;
    private final com.android.server.audio.MediaFocusControl mFocusController;
    private android.media.IAudioFocusDispatcher mFocusDispatcher;
    private final int mFocusGainRequest;
    private final int mGrantFlags;
    private final java.lang.String mPackageName;
    private final int mSdkTarget;
    private final android.os.IBinder mSourceRef;
    private int mFocusLossReceived = 0;
    private boolean mFocusLossWasNotified = true;
    boolean mFocusLossFadeLimbo = false;

    static {
        DEBUG = "eng".equals(android.os.Build.TYPE) || "userdebug".equals(android.os.Build.TYPE);
    }

    FocusRequester(android.media.AudioAttributes aa, int focusRequest, int grantFlags, android.media.IAudioFocusDispatcher afl, android.os.IBinder source, java.lang.String id, com.android.server.audio.MediaFocusControl.AudioFocusDeathHandler hdlr, java.lang.String pn, int uid, com.android.server.audio.MediaFocusControl ctlr, int sdk) {
        this.mAttributes = aa;
        this.mFocusDispatcher = afl;
        this.mSourceRef = source;
        this.mClientId = id;
        this.mDeathHandler = hdlr;
        this.mPackageName = pn;
        this.mCallingUid = uid;
        this.mFocusGainRequest = focusRequest;
        this.mGrantFlags = grantFlags;
        this.mFocusController = ctlr;
        this.mSdkTarget = sdk;
    }

    FocusRequester(android.media.AudioFocusInfo afi, android.media.IAudioFocusDispatcher afl, android.os.IBinder source, com.android.server.audio.MediaFocusControl.AudioFocusDeathHandler hdlr, com.android.server.audio.MediaFocusControl ctlr) {
        this.mAttributes = afi.getAttributes();
        this.mClientId = afi.getClientId();
        this.mPackageName = afi.getPackageName();
        this.mCallingUid = afi.getClientUid();
        this.mFocusGainRequest = afi.getGainRequest();
        this.mGrantFlags = afi.getFlags();
        this.mSdkTarget = afi.getSdkTarget();
        this.mFocusDispatcher = afl;
        this.mSourceRef = source;
        this.mDeathHandler = hdlr;
        this.mFocusController = ctlr;
    }

    boolean hasSameClient(java.lang.String otherClient) {
        return this.mClientId.compareTo(otherClient) == 0;
    }

    boolean isLockedFocusOwner() {
        return (this.mGrantFlags & 4) != 0;
    }

    boolean isInFocusLossLimbo() {
        return this.mFocusLossFadeLimbo;
    }

    boolean hasSameBinder(android.os.IBinder ib) {
        return this.mSourceRef != null && this.mSourceRef.equals(ib);
    }

    boolean hasSameDispatcher(android.media.IAudioFocusDispatcher fd) {
        return this.mFocusDispatcher != null && this.mFocusDispatcher.equals(fd);
    }

    java.lang.String getPackageName() {
        return this.mPackageName;
    }

    boolean hasSamePackage(java.lang.String pack) {
        return this.mPackageName.compareTo(pack) == 0;
    }

    boolean hasSameUid(int uid) {
        return this.mCallingUid == uid;
    }

    boolean isAlwaysVisibleUser() {
        com.android.server.pm.UserManagerInternal umi = (com.android.server.pm.UserManagerInternal) com.android.server.LocalServices.getService(com.android.server.pm.UserManagerInternal.class);
        android.content.pm.UserProperties properties = umi.getUserProperties(android.os.UserHandle.getUserId(this.mCallingUid));
        return properties != null && properties.getAlwaysVisible();
    }

    int getClientUid() {
        return this.mCallingUid;
    }

    java.lang.String getClientId() {
        return this.mClientId;
    }

    int getGainRequest() {
        return this.mFocusGainRequest;
    }

    int getGrantFlags() {
        return this.mGrantFlags;
    }

    android.media.AudioAttributes getAudioAttributes() {
        return this.mAttributes;
    }

    int getSdkTarget() {
        return this.mSdkTarget;
    }

    private static java.lang.String focusChangeToString(int focus) {
        switch (focus) {
            case -3:
                return "LOSS_TRANSIENT_CAN_DUCK";
            case -2:
                return "LOSS_TRANSIENT";
            case -1:
                return "LOSS";
            case 0:
                return "none";
            case 1:
                return "GAIN";
            case 2:
                return "GAIN_TRANSIENT";
            case 3:
                return "GAIN_TRANSIENT_MAY_DUCK";
            case 4:
                return "GAIN_TRANSIENT_EXCLUSIVE";
            default:
                return "[invalid focus change" + focus + "]";
        }
    }

    private java.lang.String focusGainToString() {
        return focusChangeToString(this.mFocusGainRequest);
    }

    private java.lang.String focusLossToString() {
        return focusChangeToString(this.mFocusLossReceived);
    }

    private static java.lang.String flagsToString(int flags) {
        java.lang.String msg = new java.lang.String();
        if ((flags & 1) != 0) {
            msg = msg + "DELAY_OK";
        }
        if ((flags & 4) != 0) {
            if (!msg.isEmpty()) {
                msg = msg + "|";
            }
            msg = msg + "LOCK";
        }
        if ((flags & 2) != 0) {
            if (!msg.isEmpty()) {
                msg = msg + "|";
            }
            return msg + "PAUSES_ON_DUCKABLE_LOSS";
        }
        return msg;
    }

    void dump(java.io.PrintWriter pw) {
        pw.println("  source:" + this.mSourceRef + " -- pack: " + this.mPackageName + " -- client: " + this.mClientId + " -- gain: " + focusGainToString() + " -- flags: " + flagsToString(this.mGrantFlags) + " -- loss: " + focusLossToString() + " -- notified: " + this.mFocusLossWasNotified + " -- limbo" + this.mFocusLossFadeLimbo + " -- uid: " + this.mCallingUid + " -- attr: " + this.mAttributes + " -- sdk:" + this.mSdkTarget);
    }

    java.lang.String getAudioFocusInfo() {
        java.lang.String fi = "source:" + this.mSourceRef + " -- pack: " + this.mPackageName + " -- client: " + this.mClientId + " -- gain: " + focusGainToString() + " -- flags: " + flagsToString(this.mGrantFlags) + " -- loss: " + focusLossToString() + " -- notified: " + this.mFocusLossWasNotified + " -- uid: " + this.mCallingUid + " -- attr: " + this.mAttributes + " -- sdk:" + this.mSdkTarget;
        return fi;
    }

    void maybeRelease() {
        if (!this.mFocusLossFadeLimbo) {
            release();
        }
    }

    void release() {
        android.os.IBinder srcRef = this.mSourceRef;
        com.android.server.audio.MediaFocusControl.AudioFocusDeathHandler deathHdlr = this.mDeathHandler;
        if (srcRef != null && deathHdlr != null) {
            try {
                srcRef.unlinkToDeath(deathHdlr, 0);
            } catch (java.util.NoSuchElementException e) {
            }
        }
        this.mDeathHandler = null;
        this.mFocusDispatcher = null;
    }

    protected void finalize() throws java.lang.Throwable {
        release();
        super.finalize();
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.calcSwitchOut(SwitchRegionMaker.java:217)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:68)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.addCases(SwitchRegionMaker.java:123)
        	at jadx.core.dex.visitors.regions.maker.SwitchRegionMaker.process(SwitchRegionMaker.java:71)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.traverse(RegionMaker.java:112)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeRegion(RegionMaker.java:66)
        	at jadx.core.dex.visitors.regions.maker.RegionMaker.makeMthRegion(RegionMaker.java:48)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:25)
        */
    private int focusLossForGainRequest(int r4) {
        /*
            r3 = this;
            r0 = -2
            r1 = -1
            switch(r4) {
                case 1: goto L6;
                case 2: goto Ld;
                case 3: goto L15;
                case 4: goto Ld;
                default: goto L5;
            }
        L5:
            goto L1f
        L6:
            int r2 = r3.mFocusLossReceived
            switch(r2) {
                case -3: goto Lc;
                case -2: goto Lc;
                case -1: goto Lc;
                case 0: goto Lc;
                default: goto Lb;
            }
        Lb:
            goto Ld
        Lc:
            return r1
        Ld:
            int r2 = r3.mFocusLossReceived
            switch(r2) {
                case -3: goto L14;
                case -2: goto L14;
                case -1: goto L13;
                case 0: goto L14;
                default: goto L12;
            }
        L12:
            goto L15
        L13:
            return r1
        L14:
            return r0
        L15:
            int r2 = r3.mFocusLossReceived
            switch(r2) {
                case -3: goto L1d;
                case -2: goto L1c;
                case -1: goto L1b;
                case 0: goto L1d;
                default: goto L1a;
            }
        L1a:
            goto L1f
        L1b:
            return r1
        L1c:
            return r0
        L1d:
            r0 = -3
            return r0
        L1f:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "focusLossForGainRequest() for invalid focus request "
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.StringBuilder r0 = r0.append(r4)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "FocusRequester"
            android.util.Log.e(r1, r0)
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.FocusRequester.focusLossForGainRequest(int):int");
    }

    boolean handleFocusLossFromGain(int focusGain, com.android.server.audio.FocusRequester frWinner, boolean forceDuck) {
        if (DEBUG) {
            android.util.Log.i(TAG, "handleFocusLossFromGain for " + this.mClientId + " gain:" + focusGain);
        }
        int focusLoss = focusLossForGainRequest(focusGain);
        handleFocusLoss(focusLoss, frWinner, forceDuck);
        return focusLoss == -1;
    }

    void handleFocusGain(int focusGain) {
        try {
            this.mFocusLossReceived = 0;
            this.mFocusLossFadeLimbo = false;
            this.mFocusController.notifyExtPolicyFocusGrant_syncAf(toAudioFocusInfo(), 1);
            android.media.IAudioFocusDispatcher fd = this.mFocusDispatcher;
            if (fd != null) {
                if (DEBUG) {
                    android.util.Log.v(TAG, "dispatching " + focusChangeToString(focusGain) + " to " + this.mClientId);
                }
                if (this.mFocusLossWasNotified) {
                    fd.dispatchAudioFocusChange(focusGain, this.mClientId);
                }
            }
            this.mFocusController.restoreVShapedPlayers(this);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Failure to signal gain of audio focus due to: ", e);
        }
    }

    void handleFocusGainFromRequest(int focusRequestResult) {
        if (focusRequestResult == 1) {
            this.mFocusController.restoreVShapedPlayers(this);
        }
    }

    void handleFocusLoss(int focusLoss, com.android.server.audio.FocusRequester frWinner, boolean forceDuck) {
        if (DEBUG) {
            android.util.Log.i(TAG, "handleFocusLoss for " + this.mClientId + " loss:" + focusLoss);
        }
        try {
            if (focusLoss != this.mFocusLossReceived) {
                this.mFocusLossReceived = focusLoss;
                this.mFocusLossWasNotified = false;
                if (!this.mFocusController.mustNotifyFocusOwnerOnDuck() && this.mFocusLossReceived == -3 && (this.mGrantFlags & 2) == 0) {
                    if (DEBUG) {
                        android.util.Log.v(TAG, "NOT dispatching " + focusChangeToString(this.mFocusLossReceived) + " to " + this.mClientId + ", to be handled externally");
                    }
                    this.mFocusController.notifyExtPolicyFocusLoss_syncAf(toAudioFocusInfo(), false);
                    return;
                }
                boolean handled = false;
                if (frWinner != null) {
                    handled = frameworkHandleFocusLoss(focusLoss, frWinner, forceDuck);
                }
                if (handled) {
                    if (DEBUG) {
                        android.util.Log.v(TAG, "NOT dispatching " + focusChangeToString(this.mFocusLossReceived) + " to " + this.mClientId + ", response handled by framework");
                    }
                    this.mFocusController.notifyExtPolicyFocusLoss_syncAf(toAudioFocusInfo(), false);
                    return;
                }
                android.media.IAudioFocusDispatcher fd = this.mFocusDispatcher;
                if (fd != null) {
                    if (DEBUG) {
                        android.util.Log.v(TAG, "dispatching " + focusChangeToString(this.mFocusLossReceived) + " to " + this.mClientId);
                    }
                    this.mFocusController.notifyExtPolicyFocusLoss_syncAf(toAudioFocusInfo(), true);
                    this.mFocusLossWasNotified = true;
                    fd.dispatchAudioFocusChange(this.mFocusLossReceived, this.mClientId);
                    return;
                }
                if (DEBUG) {
                    android.util.Log.i(TAG, "NOT dispatching " + focusChangeToString(this.mFocusLossReceived) + " to " + this.mClientId + " no IAudioFocusDispatcher");
                }
            }
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Failure to signal loss of audio focus due to:", e);
        }
    }

    private boolean frameworkHandleFocusLoss(int focusLoss, com.android.server.audio.FocusRequester frWinner, boolean forceDuck) {
        if (frWinner.mCallingUid == this.mCallingUid) {
            return false;
        }
        if (focusLoss == -3) {
            if (!forceDuck && (this.mGrantFlags & 2) != 0) {
                android.util.Log.v(TAG, "not ducking uid " + this.mCallingUid + " - flags");
                return false;
            }
            if (!forceDuck && getSdkTarget() <= 25) {
                android.util.Log.v(TAG, "not ducking uid " + this.mCallingUid + " - old SDK");
                return false;
            }
            return this.mFocusController.duckPlayers(frWinner, this, forceDuck);
        }
        if (focusLoss != -1) {
            return false;
        }
        android.util.Log.d(TAG, "frameworkHandleFocusLoss frWinner.getClientUid() = " + frWinner.getClientUid() + " frWinner.getPackageName() = " + frWinner.getPackageName());
        java.lang.String winnerPackageName = frWinner.getPackageName();
        if (winnerPackageName != null && com.oplus.atlas.OplusAtlasManager.getInstance().checkIsInDaemonlistByName(AUDIOFOCUS_BLACK_PACKAGENAME, winnerPackageName)) {
            android.util.Log.d(TAG, "frameworkHandleFocusLoss restoreVShapedPlayers this.getClientUid = " + getClientUid());
            this.mFocusController.restoreVShapedPlayers(this);
        }
        return false;
    }

    int dispatchFocusChange(int focusChange) {
        android.media.IAudioFocusDispatcher fd = this.mFocusDispatcher;
        if (fd == null) {
            if (com.android.server.audio.MediaFocusControl.DEBUG) {
                android.util.Log.e(TAG, "dispatchFocusChange: no focus dispatcher");
            }
            return 0;
        }
        if (focusChange == 0) {
            if (com.android.server.audio.MediaFocusControl.DEBUG) {
                android.util.Log.v(TAG, "dispatchFocusChange: AUDIOFOCUS_NONE");
            }
            return 0;
        }
        if ((focusChange == 3 || focusChange == 4 || focusChange == 2 || focusChange == 1) && this.mFocusGainRequest != focusChange) {
            android.util.Log.w(TAG, "focus gain was requested with " + this.mFocusGainRequest + ", dispatching " + focusChange);
        } else if (focusChange == -3 || focusChange == -2 || focusChange == -1) {
            this.mFocusLossReceived = focusChange;
        }
        try {
            fd.dispatchAudioFocusChange(focusChange, this.mClientId);
            return 1;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "dispatchFocusChange: error talking to focus listener " + this.mClientId, e);
            return 0;
        }
    }

    int dispatchFocusChangeWithFadeLocked(int focusChange, java.util.List<com.android.server.audio.FocusRequester> otherActiveFrs) {
        if (focusChange == 3 || focusChange == 4 || focusChange == 2 || focusChange == 1) {
            this.mFocusLossFadeLimbo = false;
            this.mFocusController.restoreVShapedPlayers(this);
        } else if (focusChange == -1 && this.mFocusController.shouldEnforceFade()) {
            for (int index = 0; index < otherActiveFrs.size(); index++) {
                if (this.mFocusController.fadeOutPlayers(otherActiveFrs.get(index), this)) {
                    this.mFocusLossFadeLimbo = true;
                    this.mFocusController.postDelayedLossAfterFade(this, this.mFocusController.getFadeOutDurationOnFocusLossMillis(getAudioAttributes()));
                    return 2;
                }
            }
        }
        return dispatchFocusChange(focusChange);
    }

    void dispatchFocusResultFromExtPolicy(int requestResult) {
        android.media.IAudioFocusDispatcher fd = this.mFocusDispatcher;
        if (fd == null) {
            if (com.android.server.audio.MediaFocusControl.DEBUG) {
                android.util.Log.e(TAG, "dispatchFocusResultFromExtPolicy: no focus dispatcher");
            }
        } else {
            if (DEBUG) {
                android.util.Log.v(TAG, "dispatching result" + requestResult + " to " + this.mClientId);
            }
            try {
                fd.dispatchFocusResultFromExtPolicy(requestResult, this.mClientId);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "dispatchFocusResultFromExtPolicy: error talking to focus listener" + this.mClientId, e);
            }
        }
    }

    android.media.AudioFocusInfo toAudioFocusInfo() {
        return new android.media.AudioFocusInfo(this.mAttributes, this.mCallingUid, this.mClientId, this.mPackageName, this.mFocusGainRequest, this.mFocusLossReceived, this.mGrantFlags, this.mSdkTarget);
    }
}
