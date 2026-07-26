package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class MediaFocusControl implements com.android.server.audio.PlayerFocusEnforcer {
    static final boolean DEBUG;
    static final int DUCKING_IN_APP_SDK_LEVEL = 25;
    static final boolean ENFORCE_DUCKING = true;
    static final boolean ENFORCE_DUCKING_FOR_NEW = true;
    static final boolean ENFORCE_FADEOUT_FOR_FOCUS_LOSS = false;
    static final boolean ENFORCE_MUTING_FOR_RING_OR_CALL = true;
    private static final int MAX_STACK_SIZE = 100;
    private static final int MSG_L_FOCUS_LOSS_AFTER_FADE = 1;
    private static final int MSL_L_FORGET_UID = 2;
    private static final int RING_CALL_MUTING_ENFORCEMENT_DELAY_MS = 100;
    private static final java.lang.String TAG = "MediaFocusControl";
    private static final int[] USAGES_TO_MUTE_IN_RING_OR_CALL;
    private static final java.lang.Object mAudioFocusLock;
    private static final com.android.server.utils.EventLogger mEventLogger;
    private static final java.lang.String mMetricsId = "audio.focus";
    private final android.app.AppOpsManager mAppOps;
    private final android.content.Context mContext;
    private long mExtFocusChangeCounter;
    private final com.android.server.audio.PlayerFocusEnforcer mFocusEnforcer;
    private android.os.Handler mFocusHandler;
    private android.os.HandlerThread mFocusThread;
    private boolean mMultiAudioFocusEnabled;
    private boolean mRingOrCallActive = false;
    private final java.lang.Object mExtFocusChangeLock = new java.lang.Object();
    private final java.util.Stack<com.android.server.audio.FocusRequester> mFocusStack = new java.util.Stack<>();
    java.util.ArrayList<com.android.server.audio.FocusRequester> mMultiAudioFocusList = new java.util.ArrayList<>();
    private boolean mNotifyFocusOwnerOnDuck = true;
    private java.util.ArrayList<android.media.audiopolicy.IAudioPolicyCallback> mFocusFollowers = new java.util.ArrayList<>();
    private android.media.audiopolicy.IAudioPolicyCallback mFocusPolicy = null;
    private android.media.audiopolicy.IAudioPolicyCallback mPreviousFocusPolicy = null;
    private java.util.HashMap<java.lang.String, com.android.server.audio.FocusRequester> mFocusOwnersForFocusPolicy = new java.util.HashMap<>();
    private android.os.IBinder mFocusFreezerForTest = null;
    private android.os.IBinder.DeathRecipient mFocusFreezerDeathHandler = null;
    private int[] mFocusFreezeExemptUids = null;

    static {
        DEBUG = "eng".equals(android.os.Build.TYPE) || "userdebug".equals(android.os.Build.TYPE);
        mAudioFocusLock = new java.lang.Object();
        mEventLogger = new com.android.server.utils.EventLogger(50, "focus commands as seen by MediaFocusControl");
        USAGES_TO_MUTE_IN_RING_OR_CALL = new int[]{1, 14};
    }

    protected MediaFocusControl(android.content.Context cntxt, com.android.server.audio.PlayerFocusEnforcer pfe) {
        this.mMultiAudioFocusEnabled = false;
        this.mContext = cntxt;
        this.mAppOps = (android.app.AppOpsManager) this.mContext.getSystemService("appops");
        this.mFocusEnforcer = pfe;
        android.content.ContentResolver cr = this.mContext.getContentResolver();
        this.mMultiAudioFocusEnabled = android.provider.Settings.System.getIntForUser(cr, "multi_audio_focus_enabled", 0, cr.getUserId()) != 0;
        initFocusThreading();
    }

    protected void dump(java.io.PrintWriter pw) {
        pw.println("\nMediaFocusControl dump time: " + java.text.DateFormat.getTimeInstance().format(new java.util.Date()));
        dumpFocusStack(pw);
        pw.println("\n");
        mEventLogger.dump(pw);
        dumpMultiAudioFocus(pw);
    }

    public long getFocusFadeOutDurationForTest() {
        return getFadeOutDurationMillis(new android.media.AudioAttributes.Builder().setUsage(1).build());
    }

    public long getFocusUnmuteDelayAfterFadeOutForTest() {
        return getFadeInDelayForOffendersMillis(new android.media.AudioAttributes.Builder().setUsage(1).build());
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public boolean duckPlayers(com.android.server.audio.FocusRequester winner, com.android.server.audio.FocusRequester loser, boolean forceDuck) {
        return this.mFocusEnforcer.duckPlayers(winner, loser, forceDuck);
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public void restoreVShapedPlayers(com.android.server.audio.FocusRequester winner) {
        this.mFocusEnforcer.restoreVShapedPlayers(winner);
        this.mFocusHandler.removeEqualMessages(2, new com.android.server.audio.MediaFocusControl.ForgetFadeUidInfo(winner.getClientUid()));
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public void mutePlayersForCall(int[] usagesToMute) {
        this.mFocusEnforcer.mutePlayersForCall(usagesToMute);
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public void unmutePlayersForCall() {
        this.mFocusEnforcer.unmutePlayersForCall();
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public boolean fadeOutPlayers(com.android.server.audio.FocusRequester winner, com.android.server.audio.FocusRequester loser) {
        return this.mFocusEnforcer.fadeOutPlayers(winner, loser);
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public void forgetUid(int uid) {
        this.mFocusEnforcer.forgetUid(uid);
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public long getFadeOutDurationMillis(android.media.AudioAttributes aa) {
        if (aa == null) {
            return 0L;
        }
        return this.mFocusEnforcer.getFadeOutDurationMillis(aa);
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public long getFadeInDelayForOffendersMillis(android.media.AudioAttributes aa) {
        if (aa == null) {
            return 0L;
        }
        return this.mFocusEnforcer.getFadeInDelayForOffendersMillis(aa);
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public boolean shouldEnforceFade() {
        if (!android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
            return false;
        }
        return this.mFocusEnforcer.shouldEnforceFade();
    }

    void noFocusForSuspendedApp(java.lang.String packageName, int uid) {
        synchronized (mAudioFocusLock) {
            java.util.List<java.lang.String> clientsToRemove = new java.util.ArrayList<>();
            for (com.android.server.audio.FocusRequester focusOwner : this.mFocusStack) {
                if (focusOwner.hasSameUid(uid) && focusOwner.hasSamePackage(packageName)) {
                    clientsToRemove.add(focusOwner.getClientId());
                    mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("focus owner:" + focusOwner.getClientId() + " in uid:" + uid + " pack: " + packageName + " getting AUDIOFOCUS_LOSS due to app suspension").printLog(TAG));
                    focusOwner.dispatchFocusChange(-1);
                }
            }
            for (java.lang.String clientToRemove : clientsToRemove) {
                removeFocusStackEntry(clientToRemove, false, true);
            }
        }
    }

    boolean hasAudioFocusUsers() {
        boolean z;
        synchronized (mAudioFocusLock) {
            z = !this.mFocusStack.empty();
        }
        return z;
    }

    protected boolean maybeDiscardAudioFocusOwner() {
        synchronized (mAudioFocusLock) {
            if (this.mFocusStack.empty()) {
                return true;
            }
            com.android.server.audio.FocusRequester exFocusOwner = this.mFocusStack.peek();
            if (exFocusOwner.isAlwaysVisibleUser()) {
                return false;
            }
            this.mFocusStack.pop();
            exFocusOwner.handleFocusLoss(-1, null, false);
            exFocusOwner.release();
            return true;
        }
    }

    java.util.List<android.media.AudioFocusInfo> getFocusStack() {
        java.util.ArrayList<android.media.AudioFocusInfo> stack;
        synchronized (mAudioFocusLock) {
            stack = new java.util.ArrayList<>(this.mFocusStack.size());
            for (com.android.server.audio.FocusRequester fr : this.mFocusStack) {
                stack.add(fr.toAudioFocusInfo());
            }
        }
        return stack;
    }

    protected int getExclusiveFocusOwnerUid() {
        synchronized (mAudioFocusLock) {
            if (this.mFocusStack.empty()) {
                return -1;
            }
            com.android.server.audio.FocusRequester owner = this.mFocusStack.peek();
            if (owner.getGainRequest() != 4) {
                return -1;
            }
            return owner.getClientUid();
        }
    }

    boolean sendFocusLoss(android.media.AudioFocusInfo focusLoser) {
        synchronized (mAudioFocusLock) {
            com.android.server.audio.FocusRequester loserToRemove = null;
            java.util.Iterator<com.android.server.audio.FocusRequester> it = this.mFocusStack.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                com.android.server.audio.FocusRequester fr = it.next();
                if (fr.getClientId().equals(focusLoser.getClientId())) {
                    fr.handleFocusLoss(-1, null, false);
                    loserToRemove = fr;
                    break;
                }
            }
            if (loserToRemove == null) {
                return false;
            }
            this.mFocusStack.remove(loserToRemove);
            loserToRemove.release();
            return true;
        }
    }

    private void notifyTopOfAudioFocusStack() {
        if (!this.mFocusStack.empty() && canReassignAudioFocus()) {
            this.mFocusStack.peek().handleFocusGain(1);
        }
        if (this.mMultiAudioFocusEnabled && !this.mMultiAudioFocusList.isEmpty()) {
            for (com.android.server.audio.FocusRequester multifr : this.mMultiAudioFocusList) {
                if (isLockedFocusOwner(multifr)) {
                    multifr.handleFocusGain(1);
                }
            }
        }
    }

    private void propagateFocusLossFromGain_syncAf(int focusGain, com.android.server.audio.FocusRequester fr, boolean forceDuck) {
        if (DEBUG) {
            android.util.Log.i(TAG, "propagateFocusLossFromGain_syncAf gain:" + focusGain);
        }
        java.util.List<java.lang.String> clientsToRemove = new java.util.LinkedList<>();
        if (!this.mFocusStack.empty()) {
            for (com.android.server.audio.FocusRequester focusLoser : this.mFocusStack) {
                if (DEBUG) {
                    android.util.Log.i(TAG, "propagateFocusLossFromGain_syncAf checking client:" + focusLoser.getClientId());
                }
                boolean isDefinitiveLoss = focusLoser.handleFocusLossFromGain(focusGain, fr, forceDuck);
                if (isDefinitiveLoss) {
                    clientsToRemove.add(focusLoser.getClientId());
                }
            }
        } else if (DEBUG) {
            android.util.Log.i(TAG, "propagateFocusLossFromGain_syncAf empty stack");
        }
        if (this.mMultiAudioFocusEnabled && !this.mMultiAudioFocusList.isEmpty()) {
            for (com.android.server.audio.FocusRequester multifocusLoser : this.mMultiAudioFocusList) {
                boolean isDefinitiveLoss2 = multifocusLoser.handleFocusLossFromGain(focusGain, fr, forceDuck);
                if (isDefinitiveLoss2) {
                    clientsToRemove.add(multifocusLoser.getClientId());
                }
            }
        }
        for (java.lang.String clientToRemove : clientsToRemove) {
            removeFocusStackEntry(clientToRemove, false, true);
        }
    }

    private void dumpFocusStack(java.io.PrintWriter pw) {
        pw.println("\nAudio Focus stack entries (last is top of stack):");
        synchronized (mAudioFocusLock) {
            java.util.Iterator<com.android.server.audio.FocusRequester> stackIterator = this.mFocusStack.iterator();
            while (stackIterator.hasNext()) {
                stackIterator.next().dump(pw);
            }
            pw.println("\n");
            if (this.mFocusPolicy == null) {
                pw.println("No external focus policy\n");
            } else {
                pw.println("External focus policy: " + this.mFocusPolicy + ", focus owners:\n");
                dumpExtFocusPolicyFocusOwners(pw);
            }
        }
        pw.println("\n");
        pw.println(" Notify on duck:  " + this.mNotifyFocusOwnerOnDuck + "\n");
        pw.println(" In ring or call: " + this.mRingOrCallActive + "\n");
    }

    private void removeFocusStackEntry(java.lang.String clientToRemove, boolean signal, boolean notifyFocusFollowers) {
        if (DEBUG) {
            android.util.Log.i(TAG, "removeFocusStackEntry client:" + clientToRemove);
        }
        android.media.AudioFocusInfo abandonSource = null;
        if (!this.mFocusStack.empty() && this.mFocusStack.peek().hasSameClient(clientToRemove)) {
            com.android.server.audio.FocusRequester fr = this.mFocusStack.pop();
            fr.maybeRelease();
            if (notifyFocusFollowers) {
                abandonSource = fr.toAudioFocusInfo();
            }
            if (signal) {
                notifyTopOfAudioFocusStack();
            }
        } else {
            java.util.Iterator<com.android.server.audio.FocusRequester> stackIterator = this.mFocusStack.iterator();
            while (stackIterator.hasNext()) {
                com.android.server.audio.FocusRequester fr2 = stackIterator.next();
                if (fr2.hasSameClient(clientToRemove)) {
                    android.util.Log.i(TAG, "AudioFocus  removeFocusStackEntry(): removing entry for " + clientToRemove);
                    stackIterator.remove();
                    forgetUid(fr2.getClientUid());
                    if (notifyFocusFollowers) {
                        abandonSource = fr2.toAudioFocusInfo();
                    }
                    fr2.maybeRelease();
                }
            }
        }
        if (abandonSource != null) {
            abandonSource.clearLossReceived();
            notifyExtPolicyFocusLoss_syncAf(abandonSource, false);
        }
        if (this.mMultiAudioFocusEnabled && !this.mMultiAudioFocusList.isEmpty()) {
            java.util.Iterator<com.android.server.audio.FocusRequester> listIterator = this.mMultiAudioFocusList.iterator();
            while (listIterator.hasNext()) {
                com.android.server.audio.FocusRequester fr3 = listIterator.next();
                if (fr3.hasSameClient(clientToRemove)) {
                    listIterator.remove();
                    fr3.release();
                }
            }
            if (signal) {
                notifyTopOfAudioFocusStack();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFocusStackEntryOnDeath(android.os.IBinder cb) {
        boolean isTopOfStackForClientToRemove = !this.mFocusStack.isEmpty() && this.mFocusStack.peek().hasSameBinder(cb);
        java.util.Iterator<com.android.server.audio.FocusRequester> stackIterator = this.mFocusStack.iterator();
        while (stackIterator.hasNext()) {
            com.android.server.audio.FocusRequester fr = stackIterator.next();
            if (fr.hasSameBinder(cb)) {
                android.util.Log.i(TAG, "AudioFocus  removeFocusStackEntryOnDeath(): removing entry for " + cb);
                mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("focus requester:" + fr.getClientId() + " in uid:" + fr.getClientUid() + " pack:" + fr.getPackageName() + " died"));
                notifyExtPolicyFocusLoss_syncAf(fr.toAudioFocusInfo(), false);
                stackIterator.remove();
                fr.release();
            }
        }
        if (isTopOfStackForClientToRemove) {
            notifyTopOfAudioFocusStack();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeFocusEntryForExtPolicyOnDeath(android.os.IBinder cb) {
        if (this.mFocusOwnersForFocusPolicy.isEmpty()) {
            return;
        }
        java.util.Set<java.util.Map.Entry<java.lang.String, com.android.server.audio.FocusRequester>> owners = this.mFocusOwnersForFocusPolicy.entrySet();
        java.util.Iterator<java.util.Map.Entry<java.lang.String, com.android.server.audio.FocusRequester>> ownerIterator = owners.iterator();
        while (ownerIterator.hasNext()) {
            java.util.Map.Entry<java.lang.String, com.android.server.audio.FocusRequester> owner = ownerIterator.next();
            com.android.server.audio.FocusRequester fr = owner.getValue();
            if (fr.hasSameBinder(cb)) {
                ownerIterator.remove();
                mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("focus requester:" + fr.getClientId() + " in uid:" + fr.getClientUid() + " pack:" + fr.getPackageName() + " died"));
                fr.release();
                notifyExtFocusPolicyFocusAbandon_syncAf(fr.toAudioFocusInfo());
                return;
            }
        }
    }

    private boolean canReassignAudioFocus() {
        if (!this.mFocusStack.isEmpty() && isLockedFocusOwner(this.mFocusStack.peek())) {
            return false;
        }
        return true;
    }

    private boolean isLockedFocusOwner(com.android.server.audio.FocusRequester fr) {
        return fr.hasSameClient("AudioFocus_For_Phone_Ring_And_Calls") || fr.isLockedFocusOwner();
    }

    private int pushBelowLockedFocusOwnersAndPropagate(com.android.server.audio.FocusRequester nfr) {
        if (DEBUG) {
            android.util.Log.v(TAG, "pushBelowLockedFocusOwnersAndPropagate client=" + nfr.getClientId());
        }
        int lastLockedFocusOwnerIndex = this.mFocusStack.size();
        for (int index = this.mFocusStack.size() - 1; index >= 0; index--) {
            if (isLockedFocusOwner(this.mFocusStack.elementAt(index))) {
                lastLockedFocusOwnerIndex = index;
            }
        }
        if (lastLockedFocusOwnerIndex == this.mFocusStack.size()) {
            android.util.Log.e(TAG, "No exclusive focus owner found in propagateFocusLossFromGain_syncAf()", new java.lang.Exception());
            propagateFocusLossFromGain_syncAf(nfr.getGainRequest(), nfr, false);
            this.mFocusStack.push(nfr);
            return 1;
        }
        if (DEBUG) {
            android.util.Log.v(TAG, "> lastLockedFocusOwnerIndex=" + lastLockedFocusOwnerIndex);
        }
        this.mFocusStack.insertElementAt(nfr, lastLockedFocusOwnerIndex);
        java.util.List<java.lang.String> clientsToRemove = new java.util.LinkedList<>();
        for (int index2 = lastLockedFocusOwnerIndex - 1; index2 >= 0; index2--) {
            boolean isDefinitiveLoss = this.mFocusStack.elementAt(index2).handleFocusLossFromGain(nfr.getGainRequest(), nfr, false);
            if (isDefinitiveLoss) {
                clientsToRemove.add(this.mFocusStack.elementAt(index2).getClientId());
            }
        }
        for (java.lang.String clientToRemove : clientsToRemove) {
            if (DEBUG) {
                android.util.Log.v(TAG, "> removing focus client " + clientToRemove);
            }
            removeFocusStackEntry(clientToRemove, false, true);
        }
        return 2;
    }

    protected class AudioFocusDeathHandler implements android.os.IBinder.DeathRecipient {
        private android.os.IBinder mCb;

        AudioFocusDeathHandler(android.os.IBinder cb) {
            this.mCb = cb;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            synchronized (com.android.server.audio.MediaFocusControl.mAudioFocusLock) {
                if (com.android.server.audio.MediaFocusControl.this.mFocusPolicy != null) {
                    com.android.server.audio.MediaFocusControl.this.removeFocusEntryForExtPolicyOnDeath(this.mCb);
                } else {
                    com.android.server.audio.MediaFocusControl.this.removeFocusStackEntryOnDeath(this.mCb);
                    if (com.android.server.audio.MediaFocusControl.this.mMultiAudioFocusEnabled && !com.android.server.audio.MediaFocusControl.this.mMultiAudioFocusList.isEmpty()) {
                        java.util.Iterator<com.android.server.audio.FocusRequester> listIterator = com.android.server.audio.MediaFocusControl.this.mMultiAudioFocusList.iterator();
                        while (listIterator.hasNext()) {
                            com.android.server.audio.FocusRequester fr = listIterator.next();
                            if (fr.hasSameBinder(this.mCb)) {
                                listIterator.remove();
                                fr.release();
                            }
                        }
                    }
                }
            }
        }
    }

    protected void setDuckingInExtPolicyAvailable(boolean available) {
        this.mNotifyFocusOwnerOnDuck = !available;
    }

    boolean mustNotifyFocusOwnerOnDuck() {
        return this.mNotifyFocusOwnerOnDuck;
    }

    void addFocusFollower(android.media.audiopolicy.IAudioPolicyCallback ff) {
        if (ff == null) {
            return;
        }
        synchronized (mAudioFocusLock) {
            boolean found = false;
            java.util.Iterator<android.media.audiopolicy.IAudioPolicyCallback> it = this.mFocusFollowers.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                android.media.audiopolicy.IAudioPolicyCallback pcb = it.next();
                if (pcb.asBinder().equals(ff.asBinder())) {
                    found = true;
                    break;
                }
            }
            if (found) {
                return;
            }
            this.mFocusFollowers.add(ff);
            notifyExtPolicyCurrentFocusAsync(ff);
        }
    }

    void removeFocusFollower(android.media.audiopolicy.IAudioPolicyCallback ff) {
        if (ff == null) {
            return;
        }
        synchronized (mAudioFocusLock) {
            java.util.Iterator<android.media.audiopolicy.IAudioPolicyCallback> it = this.mFocusFollowers.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                android.media.audiopolicy.IAudioPolicyCallback pcb = it.next();
                if (pcb.asBinder().equals(ff.asBinder())) {
                    this.mFocusFollowers.remove(pcb);
                    break;
                }
            }
        }
    }

    void setFocusPolicy(android.media.audiopolicy.IAudioPolicyCallback policy, boolean isTestFocusPolicy) {
        if (policy == null) {
            return;
        }
        synchronized (mAudioFocusLock) {
            if (isTestFocusPolicy) {
                this.mPreviousFocusPolicy = this.mFocusPolicy;
                this.mFocusPolicy = policy;
            } else {
                this.mFocusPolicy = policy;
            }
        }
    }

    void unsetFocusPolicy(android.media.audiopolicy.IAudioPolicyCallback policy, boolean isTestFocusPolicy) {
        if (policy == null) {
            return;
        }
        synchronized (mAudioFocusLock) {
            if (this.mFocusPolicy == policy) {
                if (isTestFocusPolicy) {
                    this.mFocusPolicy = this.mPreviousFocusPolicy;
                } else {
                    this.mFocusPolicy = null;
                }
            }
        }
    }

    void notifyExtPolicyCurrentFocusAsync(final android.media.audiopolicy.IAudioPolicyCallback pcb) {
        java.lang.Thread thread = new java.lang.Thread() { // from class: com.android.server.audio.MediaFocusControl.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                synchronized (com.android.server.audio.MediaFocusControl.mAudioFocusLock) {
                    if (com.android.server.audio.MediaFocusControl.this.mFocusStack.isEmpty()) {
                        return;
                    }
                    try {
                        pcb.notifyAudioFocusGrant(((com.android.server.audio.FocusRequester) com.android.server.audio.MediaFocusControl.this.mFocusStack.peek()).toAudioFocusInfo(), 1);
                    } catch (android.os.RemoteException e) {
                        android.util.Log.e(com.android.server.audio.MediaFocusControl.TAG, "Can't call notifyAudioFocusGrant() on IAudioPolicyCallback " + pcb.asBinder(), e);
                    }
                }
            }
        };
        thread.start();
    }

    void notifyExtPolicyFocusGrant_syncAf(android.media.AudioFocusInfo afi, int requestResult) {
        for (android.media.audiopolicy.IAudioPolicyCallback pcb : this.mFocusFollowers) {
            try {
                pcb.notifyAudioFocusGrant(afi, requestResult);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Can't call notifyAudioFocusGrant() on IAudioPolicyCallback " + pcb.asBinder(), e);
            }
        }
    }

    void notifyExtPolicyFocusLoss_syncAf(android.media.AudioFocusInfo afi, boolean wasDispatched) {
        for (android.media.audiopolicy.IAudioPolicyCallback pcb : this.mFocusFollowers) {
            try {
                pcb.notifyAudioFocusLoss(afi, wasDispatched);
            } catch (android.os.RemoteException e) {
                android.util.Log.e(TAG, "Can't call notifyAudioFocusLoss() on IAudioPolicyCallback " + pcb.asBinder(), e);
            }
        }
    }

    boolean notifyExtFocusPolicyFocusRequest_syncAf(android.media.AudioFocusInfo afi, android.media.IAudioFocusDispatcher fd, android.os.IBinder cb) {
        boolean keepTrack;
        if (DEBUG) {
            android.util.Log.v(TAG, "notifyExtFocusPolicyFocusRequest client=" + afi.getClientId() + " dispatcher=" + fd);
        }
        synchronized (this.mExtFocusChangeLock) {
            long j = this.mExtFocusChangeCounter;
            this.mExtFocusChangeCounter = 1 + j;
            afi.setGen(j);
        }
        com.android.server.audio.FocusRequester existingFr = this.mFocusOwnersForFocusPolicy.get(afi.getClientId());
        if (existingFr != null) {
            if (existingFr.hasSameDispatcher(fd)) {
                keepTrack = false;
            } else {
                existingFr.release();
                keepTrack = true;
            }
        } else {
            keepTrack = true;
        }
        if (keepTrack) {
            com.android.server.audio.MediaFocusControl.AudioFocusDeathHandler hdlr = new com.android.server.audio.MediaFocusControl.AudioFocusDeathHandler(cb);
            try {
                cb.linkToDeath(hdlr, 0);
                this.mFocusOwnersForFocusPolicy.put(afi.getClientId(), new com.android.server.audio.FocusRequester(afi, fd, cb, hdlr, this));
            } catch (android.os.RemoteException e) {
                return false;
            }
        }
        try {
            this.mFocusPolicy.notifyAudioFocusRequest(afi, 1);
            return true;
        } catch (android.os.RemoteException e2) {
            android.util.Log.e(TAG, "Can't call notifyAudioFocusRequest() on IAudioPolicyCallback " + this.mFocusPolicy.asBinder(), e2);
            return false;
        }
    }

    void setFocusRequestResultFromExtPolicy(android.media.AudioFocusInfo afi, int requestResult) {
        synchronized (this.mExtFocusChangeLock) {
            if (afi.getGen() > this.mExtFocusChangeCounter) {
                return;
            }
            synchronized (mAudioFocusLock) {
                com.android.server.audio.FocusRequester fr = getFocusRequesterLocked(afi.getClientId(), requestResult == 0);
                if (fr != null) {
                    fr.dispatchFocusResultFromExtPolicy(requestResult);
                    if (android.media.audiopolicy.Flags.enableFadeManagerConfiguration()) {
                        fr.handleFocusGainFromRequest(requestResult);
                    }
                }
            }
        }
    }

    boolean notifyExtFocusPolicyFocusAbandon_syncAf(android.media.AudioFocusInfo afi) {
        if (this.mFocusPolicy == null) {
            return false;
        }
        com.android.server.audio.FocusRequester fr = this.mFocusOwnersForFocusPolicy.remove(afi.getClientId());
        if (fr != null) {
            fr.release();
        }
        try {
            this.mFocusPolicy.notifyAudioFocusAbandon(afi);
            return true;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(TAG, "Can't call notifyAudioFocusAbandon() on IAudioPolicyCallback " + this.mFocusPolicy.asBinder(), e);
            return true;
        }
    }

    int dispatchFocusChange(android.media.AudioFocusInfo afi, int focusChange) {
        if (DEBUG) {
            android.util.Log.v(TAG, "dispatchFocusChange " + focusChange + " to afi client=" + afi.getClientId());
        }
        synchronized (mAudioFocusLock) {
            com.android.server.audio.FocusRequester fr = getFocusRequesterLocked(afi.getClientId(), focusChange == -1);
            if (fr == null) {
                if (DEBUG) {
                    android.util.Log.v(TAG, "> failed: no such focus requester known");
                }
                return 0;
            }
            return fr.dispatchFocusChange(focusChange);
        }
    }

    int dispatchFocusChangeWithFade(android.media.AudioFocusInfo afi, int focusChange, java.util.List<android.media.AudioFocusInfo> otherActiveAfis) {
        if (DEBUG) {
            android.util.Log.v(TAG, "dispatchFocusChangeWithFade " + android.media.AudioManager.audioFocusToString(focusChange) + " to afi client=" + afi.getClientId() + " other active afis=" + otherActiveAfis);
        }
        synchronized (mAudioFocusLock) {
            java.lang.String clientId = afi.getClientId();
            com.android.server.audio.FocusRequester fr = getFocusRequesterLocked(clientId, false);
            if (fr == null) {
                if (DEBUG) {
                    android.util.Log.v(TAG, "> failed: no such focus requester known");
                }
                return 0;
            }
            java.util.ArrayList<com.android.server.audio.FocusRequester> otherActiveFrs = new java.util.ArrayList<>();
            for (int index = 0; index < otherActiveAfis.size(); index++) {
                com.android.server.audio.FocusRequester otherFr = getFocusRequesterLocked(otherActiveAfis.get(index).getClientId(), false);
                if (otherFr != null) {
                    otherActiveFrs.add(otherFr);
                }
            }
            int status = fr.dispatchFocusChangeWithFadeLocked(focusChange, otherActiveFrs);
            if (status != 2 && focusChange == -1) {
                this.mFocusOwnersForFocusPolicy.remove(clientId);
            }
            return status;
        }
    }

    private com.android.server.audio.FocusRequester getFocusRequesterLocked(java.lang.String clientId, boolean shouldRemove) {
        com.android.server.audio.FocusRequester fr;
        if (this.mFocusPolicy == null) {
            if (DEBUG) {
                android.util.Log.v(TAG, "> failed: no focus policy");
                return null;
            }
            return null;
        }
        if (shouldRemove) {
            fr = this.mFocusOwnersForFocusPolicy.remove(clientId);
        } else {
            fr = this.mFocusOwnersForFocusPolicy.get(clientId);
        }
        if (fr == null && DEBUG) {
            android.util.Log.v(TAG, "> failed: no such focus requester known");
        }
        return fr;
    }

    private void dumpExtFocusPolicyFocusOwners(java.io.PrintWriter pw) {
        java.util.Set<java.util.Map.Entry<java.lang.String, com.android.server.audio.FocusRequester>> owners = this.mFocusOwnersForFocusPolicy.entrySet();
        for (java.util.Map.Entry<java.lang.String, com.android.server.audio.FocusRequester> owner : owners) {
            com.android.server.audio.FocusRequester fr = owner.getValue();
            fr.dump(pw);
        }
    }

    protected int getCurrentAudioFocus() {
        synchronized (mAudioFocusLock) {
            if (this.mFocusStack.empty()) {
                return 0;
            }
            return this.mFocusStack.peek().getGainRequest();
        }
    }

    protected static int getFocusRampTimeMs(int focusGain, android.media.AudioAttributes attr) {
        switch (attr.getUsage()) {
            case 1:
            case 14:
                return 1000;
            case 2:
            case 3:
            case 5:
            case 7:
            case 8:
            case 9:
            case 10:
            case 13:
            case 1002:
                return 500;
            case 4:
            case 6:
            case 11:
            case 12:
            case 16:
            case 1003:
                return com.android.server.am.ProcessList.PREVIOUS_APP_ADJ;
            default:
                return 0;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0150 A[Catch: all -> 0x013f, TRY_ENTER, TryCatch #7 {all -> 0x013f, blocks: (B:18:0x00fe, B:20:0x0104, B:21:0x011d, B:23:0x011f, B:17:0x00fa, B:30:0x0150, B:31:0x0157, B:43:0x0170), top: B:156:0x00fa }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0159 A[Catch: all -> 0x030c, TRY_ENTER, TryCatch #6 {all -> 0x030c, blocks: (B:12:0x00ed, B:27:0x0146, B:33:0x0159, B:37:0x0161, B:44:0x0172), top: B:147:0x00ed }] */
    /* JADX WARN: Type inference failed for: r11v1 */
    /* JADX WARN: Type inference failed for: r11v2, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r11v5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected int requestAudioFocus(android.media.AudioAttributes r23, int r24, android.os.IBinder r25, android.media.IAudioFocusDispatcher r26, java.lang.String r27, java.lang.String r28, int r29, int r30, boolean r31, int r32, boolean r33) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 789
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.audio.MediaFocusControl.requestAudioFocus(android.media.AudioAttributes, int, android.os.IBinder, android.media.IAudioFocusDispatcher, java.lang.String, java.lang.String, int, int, boolean, int, boolean):int");
    }

    protected int abandonAudioFocus(android.media.IAudioFocusDispatcher fl, java.lang.String clientId, android.media.AudioAttributes aa, java.lang.String callingPackageName) {
        new android.media.MediaMetrics.Item(mMetricsId).setUid(android.os.Binder.getCallingUid()).set(android.media.MediaMetrics.Property.CALLING_PACKAGE, callingPackageName).set(android.media.MediaMetrics.Property.CLIENT_NAME, clientId).set(android.media.MediaMetrics.Property.EVENT, "abandonAudioFocus").record();
        mEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("abandonAudioFocus() from uid/pid " + android.os.Binder.getCallingUid() + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + android.os.Binder.getCallingPid() + " clientId=" + clientId).printLog(TAG));
        try {
        } catch (java.util.ConcurrentModificationException cme) {
            android.util.Log.e(TAG, "FATAL EXCEPTION AudioFocus  abandonAudioFocus() caused " + cme);
            cme.printStackTrace();
        }
        synchronized (mAudioFocusLock) {
            if (this.mFocusPolicy != null) {
                android.media.AudioFocusInfo afi = new android.media.AudioFocusInfo(aa, android.os.Binder.getCallingUid(), clientId, callingPackageName, 0, 0, 0, 0);
                if (notifyExtFocusPolicyFocusAbandon_syncAf(afi)) {
                    return 1;
                }
            }
            boolean exitingRingOrCall = this.mRingOrCallActive & ("AudioFocus_For_Phone_Ring_And_Calls".compareTo(clientId) == 0);
            if (exitingRingOrCall) {
                this.mRingOrCallActive = false;
            }
            removeFocusStackEntry(clientId, true, true);
            if (exitingRingOrCall & true) {
                runAudioCheckerForRingOrCallAsync(false);
            }
            return 1;
        }
    }

    private boolean isFocusFrozenForTest() {
        return this.mFocusFreezerForTest != null;
    }

    private boolean isFocusFrozenForTestForUid(int uidToCheck) {
        if (isFocusFrozenForTest()) {
            return false;
        }
        for (int uid : this.mFocusFreezeExemptUids) {
            if (uid == uidToCheck) {
                return false;
            }
        }
        return true;
    }

    protected boolean enterAudioFocusFreezeForTest(android.os.IBinder cb, int[] exemptedUids) {
        android.util.Log.i(TAG, "enterAudioFocusFreezeForTest UIDs exempt:" + java.util.Arrays.toString(exemptedUids));
        synchronized (mAudioFocusLock) {
            if (this.mFocusFreezerForTest != null) {
                android.util.Log.e(TAG, "Error enterAudioFocusFreezeForTest: focus already frozen");
                return false;
            }
            try {
                this.mFocusFreezerDeathHandler = new android.os.IBinder.DeathRecipient() { // from class: com.android.server.audio.MediaFocusControl.2
                    @Override // android.os.IBinder.DeathRecipient
                    public void binderDied() {
                        android.util.Log.i(com.android.server.audio.MediaFocusControl.TAG, "Audio focus freezer died, exiting focus freeze for test");
                        com.android.server.audio.MediaFocusControl.this.releaseFocusFreeze();
                    }
                };
                cb.linkToDeath(this.mFocusFreezerDeathHandler, 0);
                this.mFocusFreezerForTest = cb;
                this.mFocusFreezeExemptUids = (int[]) exemptedUids.clone();
                return true;
            } catch (android.os.RemoteException e) {
                this.mFocusFreezerForTest = null;
                this.mFocusFreezeExemptUids = null;
                return false;
            }
        }
    }

    protected boolean exitAudioFocusFreezeForTest(android.os.IBinder cb) {
        java.lang.String str;
        synchronized (mAudioFocusLock) {
            if (this.mFocusFreezerForTest != cb) {
                java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("Error exitAudioFocusFreezeForTest: ");
                if (this.mFocusFreezerForTest == null) {
                    str = "call to exit while not frozen";
                } else {
                    str = "call to exit not coming from freeze owner";
                }
                android.util.Log.e(TAG, sbAppend.append(str).toString());
                return false;
            }
            this.mFocusFreezerForTest.unlinkToDeath(this.mFocusFreezerDeathHandler, 0);
            releaseFocusFreeze();
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void releaseFocusFreeze() {
        synchronized (mAudioFocusLock) {
            this.mFocusFreezerDeathHandler = null;
            this.mFocusFreezeExemptUids = null;
            this.mFocusFreezerForTest = null;
        }
    }

    protected void unregisterAudioFocusClient(java.lang.String clientId) {
        synchronized (mAudioFocusLock) {
            removeFocusStackEntry(clientId, false, true);
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.server.audio.MediaFocusControl$3] */
    private void runAudioCheckerForRingOrCallAsync(final boolean enteringRingOrCall) {
        new java.lang.Thread() { // from class: com.android.server.audio.MediaFocusControl.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                if (enteringRingOrCall) {
                    try {
                        java.lang.Thread.sleep(100L);
                    } catch (java.lang.InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                synchronized (com.android.server.audio.MediaFocusControl.mAudioFocusLock) {
                    if (com.android.server.audio.MediaFocusControl.this.mRingOrCallActive) {
                        com.android.server.audio.MediaFocusControl.this.mFocusEnforcer.mutePlayersForCall(com.android.server.audio.MediaFocusControl.USAGES_TO_MUTE_IN_RING_OR_CALL);
                    } else {
                        com.android.server.audio.MediaFocusControl.this.mFocusEnforcer.unmutePlayersForCall();
                    }
                }
            }
        }.start();
    }

    public java.lang.String getAudioFocusStack() {
        synchronized (mAudioFocusLock) {
            if (this.mFocusStack.isEmpty()) {
                return null;
            }
            java.util.List<java.lang.String> list = new java.util.ArrayList<>();
            java.util.Iterator<com.android.server.audio.FocusRequester> stackIterator = this.mFocusStack.iterator();
            while (stackIterator.hasNext()) {
                java.lang.String fi = stackIterator.next().getAudioFocusInfo();
                list.add(fi);
            }
            return java.lang.String.join("|", list);
        }
    }

    public boolean isPackageInFocusStack(java.lang.String packageName) {
        synchronized (mAudioFocusLock) {
            if (this.mFocusStack.size() <= 100 && packageName != null) {
                for (com.android.server.audio.FocusRequester fr : this.mFocusStack) {
                    if (packageName.equals(fr.getPackageName())) {
                        return true;
                    }
                }
                if (this.mMultiAudioFocusEnabled) {
                    for (com.android.server.audio.FocusRequester multifr : this.mMultiAudioFocusList) {
                        if (packageName.equals(multifr.getPackageName())) {
                            return true;
                        }
                    }
                }
                android.util.Log.d(TAG, packageName + " do not in focus stack");
                return false;
            }
            return true;
        }
    }

    public void updateMultiAudioFocus(boolean z) {
        android.util.Log.d(TAG, "updateMultiAudioFocus( " + z + " )");
        this.mMultiAudioFocusEnabled = z;
        android.content.ContentResolver contentResolver = this.mContext.getContentResolver();
        android.provider.Settings.System.putIntForUser(contentResolver, "multi_audio_focus_enabled", z ? 1 : 0, contentResolver.getUserId());
        if (!this.mFocusStack.isEmpty()) {
            this.mFocusStack.peek().handleFocusLoss(-1, null, false);
        }
        if (!z && !this.mMultiAudioFocusList.isEmpty()) {
            java.util.Iterator<com.android.server.audio.FocusRequester> it = this.mMultiAudioFocusList.iterator();
            while (it.hasNext()) {
                it.next().handleFocusLoss(-1, null, false);
            }
            this.mMultiAudioFocusList.clear();
        }
    }

    public boolean getMultiAudioFocusEnabled() {
        return this.mMultiAudioFocusEnabled;
    }

    long getFadeOutDurationOnFocusLossMillis(android.media.AudioAttributes aa) {
        return 0L;
    }

    private void dumpMultiAudioFocus(java.io.PrintWriter pw) {
        pw.println("Multi Audio Focus enabled :" + this.mMultiAudioFocusEnabled);
        if (!this.mMultiAudioFocusList.isEmpty()) {
            pw.println("Multi Audio Focus List:");
            pw.println("------------------------------");
            for (com.android.server.audio.FocusRequester multifr : this.mMultiAudioFocusList) {
                multifr.dump(pw);
            }
            pw.println("------------------------------");
        }
    }

    void postDelayedLossAfterFade(com.android.server.audio.FocusRequester focusLoser, long delayMs) {
        if (DEBUG) {
            android.util.Log.v(TAG, "postDelayedLossAfterFade loser=" + focusLoser.getPackageName() + ", isInFocusLossLimbo=" + focusLoser.isInFocusLossLimbo() + "delayMs=" + delayMs);
        }
        this.mFocusHandler.sendMessageDelayed(this.mFocusHandler.obtainMessage(1, focusLoser), delayMs);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void postForgetUidLater(com.android.server.audio.FocusRequester focusRequester) {
        this.mFocusHandler.sendMessageDelayed(this.mFocusHandler.obtainMessage(2, new com.android.server.audio.MediaFocusControl.ForgetFadeUidInfo(focusRequester.getClientUid())), getFadeInDelayForOffendersMillis(focusRequester.getAudioAttributes()));
    }

    private void initFocusThreading() {
        this.mFocusThread = new android.os.HandlerThread(TAG);
        this.mFocusThread.start();
        this.mFocusHandler = new android.os.Handler(this.mFocusThread.getLooper()) { // from class: com.android.server.audio.MediaFocusControl.4
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                        if (com.android.server.audio.MediaFocusControl.DEBUG) {
                            android.util.Log.d(com.android.server.audio.MediaFocusControl.TAG, "MSG_L_FOCUS_LOSS_AFTER_FADE loser=" + ((com.android.server.audio.FocusRequester) msg.obj).getPackageName());
                        }
                        synchronized (com.android.server.audio.MediaFocusControl.mAudioFocusLock) {
                            com.android.server.audio.FocusRequester loser = (com.android.server.audio.FocusRequester) msg.obj;
                            if (loser.isInFocusLossLimbo()) {
                                loser.dispatchFocusChange(-1);
                                com.android.server.audio.MediaFocusControl.this.mFocusEnforcer.restoreVShapedPlayers((com.android.server.audio.FocusRequester) msg.obj);
                                loser.release();
                                com.android.server.audio.MediaFocusControl.this.postForgetUidLater(loser);
                            }
                            break;
                        }
                        return;
                    case 2:
                        int uid = ((com.android.server.audio.MediaFocusControl.ForgetFadeUidInfo) msg.obj).mUid;
                        if (com.android.server.audio.MediaFocusControl.DEBUG) {
                            android.util.Log.d(com.android.server.audio.MediaFocusControl.TAG, "MSL_L_FORGET_UID uid=" + uid);
                        }
                        com.android.server.audio.MediaFocusControl.this.mFocusEnforcer.forgetUid(uid);
                        return;
                    default:
                        return;
                }
            }
        };
    }

    private static final class ForgetFadeUidInfo {
        private final int mUid;

        ForgetFadeUidInfo(int uid) {
            this.mUid = uid;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            com.android.server.audio.MediaFocusControl.ForgetFadeUidInfo f = (com.android.server.audio.MediaFocusControl.ForgetFadeUidInfo) o;
            if (f.mUid == this.mUid) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return this.mUid;
        }
    }
}
