package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public final class PlaybackActivityMonitor implements android.media.AudioPlaybackConfiguration.PlayerDeathMonitor, com.android.server.audio.PlayerFocusEnforcer {
    static final boolean DEBUG = false;
    static final java.lang.String EVENT_TYPE_FADE_IN = "fading in";
    static final java.lang.String EVENT_TYPE_FADE_OUT = "fading out";
    private static final int FLAGS_FOR_SILENCE_OVERRIDE = 192;
    private static final int MSG_IIL_UPDATE_PLAYER_FORMAT = 3;
    private static final int MSG_IIL_UPDATE_PLAYER_MUTED_EVENT = 2;
    private static final int MSG_L_TIMEOUT_MUTE_AWAIT_CONNECTION = 1;
    private static final java.lang.String PARAM_META_AUDIO = "ismetaAudiobyUid=";
    public static final java.lang.String TAG = "AS.PlaybackActivityMon";
    static final int VOLUME_SHAPER_SYSTEM_DUCK_ID = 1;
    static final int VOLUME_SHAPER_SYSTEM_FADEOUT_ID = 2;
    static final int VOLUME_SHAPER_SYSTEM_MUTE_AWAIT_CONNECTION_ID = 3;
    static final int VOLUME_SHAPER_SYSTEM_STRONG_DUCK_ID = 4;
    private final android.content.Context mContext;
    private android.os.Handler mEventHandler;
    private android.os.HandlerThread mEventThread;
    private final int mMaxAlarmVolume;
    private final java.util.function.Consumer<android.media.AudioDeviceAttributes> mMuteAwaitConnectionTimeoutCb;
    private static final android.media.VolumeShaper.Configuration DUCK_VSHAPE = new android.media.VolumeShaper.Configuration.Builder().setId(1).setCurve(new float[]{0.0f, 1.0f}, new float[]{1.0f, 0.2f}).setOptionFlags(2).setDuration(com.android.server.audio.MediaFocusControl.getFocusRampTimeMs(3, new android.media.AudioAttributes.Builder().setUsage(5).build())).build();
    private static final android.media.VolumeShaper.Configuration DUCK_ID = new android.media.VolumeShaper.Configuration(1);
    private static final android.media.VolumeShaper.Configuration STRONG_DUCK_VSHAPE = new android.media.VolumeShaper.Configuration.Builder().setId(4).setCurve(new float[]{0.0f, 1.0f}, new float[]{1.0f, 0.017783f}).setOptionFlags(2).setDuration(com.android.server.audio.MediaFocusControl.getFocusRampTimeMs(3, new android.media.AudioAttributes.Builder().setUsage(5).build())).build();
    private static final android.media.VolumeShaper.Configuration STRONG_DUCK_ID = new android.media.VolumeShaper.Configuration(4);
    private static final android.media.VolumeShaper.Operation PLAY_CREATE_IF_NEEDED = new android.media.VolumeShaper.Operation.Builder(android.media.VolumeShaper.Operation.PLAY).createIfNeeded().build();
    private static final long UNMUTE_DURATION_MS = 100;
    private static final android.media.VolumeShaper.Configuration MUTE_AWAIT_CONNECTION_VSHAPE = new android.media.VolumeShaper.Configuration.Builder().setId(3).setCurve(new float[]{0.0f, 1.0f}, new float[]{1.0f, 0.0f}).setOptionFlags(2).setDuration(UNMUTE_DURATION_MS).build();
    private static final int[] UNDUCKABLE_PLAYER_TYPES = {13, 3};
    private static final android.media.VolumeShaper.Operation PLAY_SKIP_RAMP = new android.media.VolumeShaper.Operation.Builder(PLAY_CREATE_IF_NEEDED).setXOffset(1.0f).build();
    private static boolean mIsExAudioFocusState = false;
    static final com.android.server.utils.EventLogger sEventLogger = new com.android.server.utils.EventLogger(100, "playback activity as reported through PlayerBase");
    private final java.util.concurrent.ConcurrentLinkedQueue<com.android.server.audio.PlaybackActivityMonitor.PlayMonitorClient> mClients = new java.util.concurrent.ConcurrentLinkedQueue<>();
    private final java.lang.Object mPlayerLock = new java.lang.Object();
    private final java.util.HashMap<java.lang.Integer, android.media.AudioPlaybackConfiguration> mPlayers = new java.util.HashMap<>();
    private final android.util.SparseIntArray mPiidToPortId = new android.util.SparseIntArray();
    private final android.util.SparseIntArray mPortIdToPiid = new android.util.SparseIntArray();
    private int mSavedAlarmVolume = -1;
    private int mPrivilegedAlarmActiveCount = 0;
    private final int MSG_SET_TRACK_VOLUME = 100;
    private final int MSG_REUNMUTE_PALYER = 101;
    private final int MSG_REUNMUTE_PALYER_DELAY = 30;
    private final int DEFAULT_UNMUTE = 0;
    private final int APPVOLUME_UNMUTE = 1;
    private com.android.server.audio.PlaybackActivityMonitor.PlaybackActivityMonitorWrapper mPamWrapper = new com.android.server.audio.PlaybackActivityMonitor.PlaybackActivityMonitorWrapper();
    private com.android.server.audio.IPlaybackActivityMonitorExt mPamExt = (com.android.server.audio.IPlaybackActivityMonitorExt) system.ext.loader.core.ExtLoader.type(com.android.server.audio.IPlaybackActivityMonitorExt.class).base(this).create();
    private final com.android.server.audio.FadeOutManager mFadeOutManager = new com.android.server.audio.FadeOutManager();
    private final java.util.ArrayList<java.lang.Integer> mBannedUids = new java.util.ArrayList<>();
    private java.util.ArrayList<java.lang.Integer> mDoNotLogPiidList = new java.util.ArrayList<>();
    private final java.util.HashMap<java.lang.Integer, java.lang.Integer> mAllowedCapturePolicies = new java.util.HashMap<>();
    private final java.util.ArrayList<java.lang.Integer> mMutedPlayers = new java.util.ArrayList<>();
    private final com.android.server.audio.PlaybackActivityMonitor.DuckingManager mDuckingManager = new com.android.server.audio.PlaybackActivityMonitor.DuckingManager();
    private final java.util.ArrayList<java.lang.Integer> mMutedPlayersAwaitingConnection = new java.util.ArrayList<>();
    private int[] mMutedUsagesAwaitingConnection = null;

    /* JADX WARN: Multi-variable type inference failed */
    PlaybackActivityMonitor(android.content.Context context, int i, java.util.function.Consumer<android.media.AudioDeviceAttributes> consumer) {
        this.mContext = context;
        this.mMaxAlarmVolume = i;
        com.android.server.audio.PlaybackActivityMonitor.PlayMonitorClient.sListenerDeathMonitor = this;
        android.media.AudioPlaybackConfiguration.sPlayerDeathMonitor = this;
        this.mMuteAwaitConnectionTimeoutCb = consumer;
        initEventHandler();
    }

    public void disableAudioForUid(boolean disable, int uid) {
        synchronized (this.mPlayerLock) {
            int index = this.mBannedUids.indexOf(new java.lang.Integer(uid));
            if (index >= 0) {
                if (!disable) {
                    this.mBannedUids.remove(index);
                }
            } else if (disable) {
                for (android.media.AudioPlaybackConfiguration apc : this.mPlayers.values()) {
                    checkBanPlayer(apc, uid);
                }
                this.mBannedUids.add(new java.lang.Integer(uid));
            }
        }
    }

    private boolean checkBanPlayer(android.media.AudioPlaybackConfiguration apc, int uid) {
        boolean toBan = apc.getClientUid() == uid;
        if (toBan) {
            int piid = apc.getPlayerInterfaceId();
            try {
                android.util.Log.v(TAG, "banning player " + piid + " uid:" + uid);
                apc.getPlayerProxy().pause();
            } catch (java.lang.Exception e) {
                android.util.Log.e(TAG, "error banning player " + piid + " uid:" + uid, e);
            }
        }
        return toBan;
    }

    void ignorePlayerIId(int doNotLogPiid) {
        synchronized (this.mPlayerLock) {
            this.mDoNotLogPiidList.add(java.lang.Integer.valueOf(doNotLogPiid));
        }
    }

    public int trackPlayer(android.media.PlayerBase.PlayerIdCard pic) {
        int newPiid = android.media.AudioSystem.newAudioPlayerId();
        if (newPiid == -1) {
            android.util.Log.w(TAG, "invalid piid assigned from AudioSystem");
            return newPiid;
        }
        android.media.AudioPlaybackConfiguration apc = new android.media.AudioPlaybackConfiguration(pic, newPiid, android.os.Binder.getCallingUid(), android.os.Binder.getCallingPid());
        apc.init();
        synchronized (this.mAllowedCapturePolicies) {
            int uid = apc.getClientUid();
            if (this.mAllowedCapturePolicies.containsKey(java.lang.Integer.valueOf(uid))) {
                updateAllowedCapturePolicy(apc, this.mAllowedCapturePolicies.get(java.lang.Integer.valueOf(uid)).intValue());
            }
        }
        sEventLogger.enqueue(new com.android.server.audio.PlaybackActivityMonitor.NewPlayerEvent(apc));
        synchronized (this.mPlayerLock) {
            this.mPlayers.put(java.lang.Integer.valueOf(newPiid), apc);
            this.mPamExt.updatePlayerVolumeByApc(apc, this.mContext);
            maybeMutePlayerAwaitingConnection(apc);
        }
        return newPiid;
    }

    public void playerAttributes(int piid, android.media.AudioAttributes attr, int binderUid) {
        boolean change;
        synchronized (this.mAllowedCapturePolicies) {
            if (this.mAllowedCapturePolicies.containsKey(java.lang.Integer.valueOf(binderUid)) && attr.getAllowedCapturePolicy() < this.mAllowedCapturePolicies.get(java.lang.Integer.valueOf(binderUid)).intValue()) {
                attr = new android.media.AudioAttributes.Builder(attr).setAllowedCapturePolicy(this.mAllowedCapturePolicies.get(java.lang.Integer.valueOf(binderUid)).intValue()).build();
            }
        }
        synchronized (this.mPlayerLock) {
            android.media.AudioPlaybackConfiguration apc = this.mPlayers.get(new java.lang.Integer(piid));
            if (checkConfigurationCaller(piid, apc, binderUid)) {
                sEventLogger.enqueue(new com.android.server.audio.PlaybackActivityMonitor.AudioAttrEvent(piid, attr));
                change = apc.handleAudioAttributesEvent(attr);
            } else {
                android.util.Log.e(TAG, "Error updating audio attributes");
                change = false;
            }
        }
        if (change) {
            dispatchPlaybackChange(false);
        }
    }

    public void playerSessionId(int piid, int sessionId, int binderUid) {
        boolean change;
        synchronized (this.mPlayerLock) {
            android.media.AudioPlaybackConfiguration apc = this.mPlayers.get(new java.lang.Integer(piid));
            if (checkConfigurationCaller(piid, apc, binderUid)) {
                change = apc.handleSessionIdEvent(sessionId);
            } else {
                android.util.Log.e(TAG, "Error updating audio session");
                change = false;
            }
        }
        if (change) {
            dispatchPlaybackChange(false);
        }
    }

    private void checkVolumeForPrivilegedAlarm(android.media.AudioPlaybackConfiguration apc, int event) {
        if (event == 5) {
            return;
        }
        if ((event == 2 || apc.getPlayerState() == 2) && (apc.getAudioAttributes().getAllFlags() & 192) == 192 && apc.getAudioAttributes().getUsage() == 4 && this.mContext.checkPermission("android.permission.MODIFY_PHONE_STATE", apc.getClientPid(), apc.getClientUid()) == 0) {
            if (event == 2 && apc.getPlayerState() != 2) {
                int i = this.mPrivilegedAlarmActiveCount;
                this.mPrivilegedAlarmActiveCount = i + 1;
                if (i == 0) {
                    this.mSavedAlarmVolume = android.media.AudioSystem.getStreamVolumeIndex(4, 2);
                    android.media.AudioSystem.setStreamVolumeIndexAS(4, this.mMaxAlarmVolume, 2);
                    return;
                }
                return;
            }
            if (event != 2 && apc.getPlayerState() == 2) {
                int i2 = this.mPrivilegedAlarmActiveCount - 1;
                this.mPrivilegedAlarmActiveCount = i2;
                if (i2 == 0 && android.media.AudioSystem.getStreamVolumeIndex(4, 2) == this.mMaxAlarmVolume) {
                    android.media.AudioSystem.setStreamVolumeIndexAS(4, this.mSavedAlarmVolume, 2);
                }
            }
        }
    }

    public void playerEvent(int piid, int event, int eventValue, int binderUid) {
        boolean change;
        synchronized (this.mPlayerLock) {
            android.media.AudioPlaybackConfiguration apc = this.mPlayers.get(new java.lang.Integer(piid));
            if (apc == null) {
                return;
            }
            boolean doNotLog = this.mDoNotLogPiidList.contains(java.lang.Integer.valueOf(piid));
            if (!doNotLog || event == 0) {
                sEventLogger.enqueue(new com.android.server.audio.PlaybackActivityMonitor.PlayerEvent(piid, event, eventValue));
                if (event == 6) {
                    if (com.android.media.audio.Flags.portToPiidSimplification()) {
                        this.mPiidToPortId.put(piid, eventValue);
                    } else {
                        this.mPortIdToPiid.put(eventValue, piid);
                    }
                    return;
                }
                if (event == 2) {
                    this.mEventHandler.sendMessageDelayed(this.mEventHandler.obtainMessage(100, java.lang.Integer.valueOf(piid)), 30L);
                    for (java.lang.Integer uidInteger : this.mBannedUids) {
                        if (checkBanPlayer(apc, uidInteger.intValue())) {
                            sEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("not starting piid:" + piid + ", is banned"));
                            return;
                        }
                    }
                }
                if (apc.getPlayerType() != 3 || event == 0) {
                    if (checkConfigurationCaller(piid, apc, binderUid)) {
                        checkVolumeForPrivilegedAlarm(apc, event);
                        change = apc.handleStateEvent(event, eventValue);
                    } else {
                        android.util.Log.e(TAG, "Error handling event " + event);
                        change = false;
                    }
                    if (change) {
                        if (event == 2) {
                            this.mDuckingManager.checkDuck(apc);
                            this.mFadeOutManager.checkFade(apc);
                        }
                        if (doNotLog) {
                            change = false;
                        }
                    }
                    if (change) {
                        dispatchPlaybackChange(event == 0);
                    }
                }
            }
        }
    }

    public void portEvent(int portId, int event, android.os.PersistableBundle extras, int binderUid) {
        int idxOfPiid;
        if (!android.os.UserHandle.isCore(binderUid)) {
            android.util.Log.e(TAG, "Forbidden operation from uid " + binderUid);
            return;
        }
        synchronized (this.mPlayerLock) {
            if (com.android.media.audio.Flags.portToPiidSimplification()) {
                int idxOfPiid2 = this.mPiidToPortId.indexOfValue(portId);
                if (idxOfPiid2 < 0) {
                    android.util.Log.w(TAG, "No piid assigned for invalid/internal port id " + portId);
                    return;
                }
                idxOfPiid = this.mPiidToPortId.keyAt(idxOfPiid2);
            } else {
                idxOfPiid = this.mPortIdToPiid.get(portId, -1);
                if (idxOfPiid == -1) {
                    android.util.Log.w(TAG, "No piid assigned for invalid/internal port id " + portId);
                    return;
                }
            }
            android.media.AudioPlaybackConfiguration apc = this.mPlayers.get(java.lang.Integer.valueOf(idxOfPiid));
            if (apc == null) {
                android.util.Log.w(TAG, "No AudioPlaybackConfiguration assigned for piid " + idxOfPiid);
            } else {
                if (apc.getPlayerType() == 3) {
                    return;
                }
                if (event == 7) {
                    this.mEventHandler.sendMessage(this.mEventHandler.obtainMessage(2, idxOfPiid, portId, extras));
                } else if (event == 8) {
                    this.mEventHandler.sendMessage(this.mEventHandler.obtainMessage(3, idxOfPiid, portId, extras));
                }
            }
        }
    }

    public void playerHasOpPlayAudio(int piid, boolean hasOpPlayAudio, int binderUid) {
        sEventLogger.enqueue(new com.android.server.audio.PlaybackActivityMonitor.PlayerOpPlayAudioEvent(piid, hasOpPlayAudio, binderUid));
    }

    public void releasePlayer(int piid, int binderUid) {
        boolean change = false;
        if (piid == -1) {
            android.util.Log.w(TAG, "Received releasePlayer with invalid piid: " + piid);
            sEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("releasePlayer with invalid piid:" + piid + ", uid:" + binderUid));
            return;
        }
        synchronized (this.mPlayerLock) {
            android.media.AudioPlaybackConfiguration apc = this.mPlayers.get(new java.lang.Integer(piid));
            if (checkConfigurationCaller(piid, apc, binderUid)) {
                sEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("releasing player piid:" + piid + ", uid:" + binderUid));
                this.mPlayers.remove(new java.lang.Integer(piid));
                this.mDuckingManager.removeReleased(apc);
                this.mFadeOutManager.removeReleased(apc);
                this.mMutedPlayersAwaitingConnection.remove(java.lang.Integer.valueOf(piid));
                checkVolumeForPrivilegedAlarm(apc, 0);
                change = apc.handleStateEvent(0, 0);
                if (!com.android.media.audio.Flags.portToPiidSimplification()) {
                    while (true) {
                        int portIdx = this.mPortIdToPiid.indexOfValue(piid);
                        if (portIdx < 0) {
                            break;
                        } else {
                            this.mPortIdToPiid.removeAt(portIdx);
                        }
                    }
                } else {
                    this.mPiidToPortId.delete(piid);
                }
                if (change && this.mDoNotLogPiidList.contains(java.lang.Integer.valueOf(piid))) {
                    change = false;
                }
            }
        }
        if (change) {
            dispatchPlaybackChange(true);
        }
    }

    void onAudioServerDied() {
        sEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("clear port id to piid map"));
        synchronized (this.mPlayerLock) {
            if (com.android.media.audio.Flags.portToPiidSimplification()) {
                this.mPiidToPortId.clear();
            } else {
                this.mPortIdToPiid.clear();
            }
        }
    }

    public void setAllowedCapturePolicy(int uid, int capturePolicy) {
        synchronized (this.mAllowedCapturePolicies) {
            if (capturePolicy == 1) {
                this.mAllowedCapturePolicies.remove(java.lang.Integer.valueOf(uid));
                return;
            }
            this.mAllowedCapturePolicies.put(java.lang.Integer.valueOf(uid), java.lang.Integer.valueOf(capturePolicy));
            synchronized (this.mPlayerLock) {
                for (android.media.AudioPlaybackConfiguration apc : this.mPlayers.values()) {
                    if (apc.getClientUid() == uid) {
                        updateAllowedCapturePolicy(apc, capturePolicy);
                    }
                }
            }
        }
    }

    public int getAllowedCapturePolicy(int uid) {
        return this.mAllowedCapturePolicies.getOrDefault(java.lang.Integer.valueOf(uid), 1).intValue();
    }

    public java.util.HashMap<java.lang.Integer, java.lang.Integer> getAllAllowedCapturePolicies() {
        java.util.HashMap<java.lang.Integer, java.lang.Integer> map;
        synchronized (this.mAllowedCapturePolicies) {
            map = (java.util.HashMap) this.mAllowedCapturePolicies.clone();
        }
        return map;
    }

    private void updateAllowedCapturePolicy(android.media.AudioPlaybackConfiguration apc, int capturePolicy) {
        android.media.AudioAttributes attr = apc.getAudioAttributes();
        if (attr.getAllowedCapturePolicy() >= capturePolicy) {
            return;
        }
        apc.handleAudioAttributesEvent(new android.media.AudioAttributes.Builder(apc.getAudioAttributes()).setAllowedCapturePolicy(capturePolicy).build());
    }

    public void playerDeath(int piid) {
        releasePlayer(piid, 0);
    }

    public boolean isPlaybackActiveForUid(int uid) {
        synchronized (this.mPlayerLock) {
            for (android.media.AudioPlaybackConfiguration apc : this.mPlayers.values()) {
                if (apc.isActive() && apc.getClientUid() == uid) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean hasActiveMediaPlaybackOnSubmixWithAddress(java.lang.String address) {
        synchronized (this.mPlayerLock) {
            for (android.media.AudioPlaybackConfiguration apc : this.mPlayers.values()) {
                android.media.AudioDeviceInfo device = apc.getAudioDeviceInfo();
                if (apc.getAudioAttributes().getUsage() == 1 && apc.isActive() && device != null && device.getInternalType() == 32768 && address.equals(device.getAddress())) {
                    return true;
                }
            }
            return false;
        }
    }

    protected void dump(java.io.PrintWriter pw) {
        pw.println("\nPlaybackActivityMonitor dump time: " + java.text.DateFormat.getTimeInstance().format(new java.util.Date()));
        synchronized (this.mPlayerLock) {
            pw.println("\n  playback listeners:");
            for (com.android.server.audio.PlaybackActivityMonitor.PlayMonitorClient pmc : this.mClients) {
                pw.print(" " + (pmc.isPrivileged() ? "(S)" : "(P)") + pmc.toString());
            }
            pw.println("\n");
            pw.println("\n  players:");
            java.util.List<java.lang.Integer> piidIntList = new java.util.ArrayList<>(this.mPlayers.keySet());
            java.util.Collections.sort(piidIntList);
            for (java.lang.Integer piidInt : piidIntList) {
                android.media.AudioPlaybackConfiguration apc = this.mPlayers.get(piidInt);
                if (apc != null) {
                    if (this.mDoNotLogPiidList.contains(java.lang.Integer.valueOf(apc.getPlayerInterfaceId()))) {
                        pw.print("(not logged)");
                    }
                    apc.dump(pw);
                }
            }
            pw.println("\n  ducked players piids:");
            this.mDuckingManager.dump(pw);
            pw.println("\n  faded out players piids:");
            this.mFadeOutManager.dump(pw);
            pw.print("\n  muted player piids due to call/ring:");
            java.util.Iterator<java.lang.Integer> it = this.mMutedPlayers.iterator();
            while (it.hasNext()) {
                int piid = it.next().intValue();
                pw.print(" " + piid);
            }
            pw.println();
            pw.print("\n  banned uids:");
            java.util.Iterator<java.lang.Integer> it2 = this.mBannedUids.iterator();
            while (it2.hasNext()) {
                int uid = it2.next().intValue();
                pw.print(" " + uid);
            }
            pw.println("\n");
            pw.print("\n  muted players (piids) awaiting device connection:");
            java.util.Iterator<java.lang.Integer> it3 = this.mMutedPlayersAwaitingConnection.iterator();
            while (it3.hasNext()) {
                int piid2 = it3.next().intValue();
                pw.print(" " + piid2);
            }
            pw.println("\n");
            if (com.android.media.audio.Flags.portToPiidSimplification()) {
                pw.println("\n  current piid to portId map:");
                for (int i = 0; i < this.mPiidToPortId.size(); i++) {
                    pw.println("  piid: " + this.mPiidToPortId.keyAt(i) + " portId: " + this.mPiidToPortId.valueAt(i));
                }
            } else {
                pw.println("\n  current portId to piid map:");
                for (int i2 = 0; i2 < this.mPortIdToPiid.size(); i2++) {
                    pw.println("  portId: " + this.mPortIdToPiid.keyAt(i2) + " piid: " + this.mPortIdToPiid.valueAt(i2));
                }
            }
            pw.println("\n");
            sEventLogger.dump(pw);
        }
        synchronized (this.mAllowedCapturePolicies) {
            pw.println("\n  allowed capture policies:");
            for (java.util.Map.Entry<java.lang.Integer, java.lang.Integer> entry : this.mAllowedCapturePolicies.entrySet()) {
                pw.println("  uid: " + entry.getKey() + " policy: " + entry.getValue());
            }
        }
    }

    private static boolean checkConfigurationCaller(int piid, android.media.AudioPlaybackConfiguration apc, int binderUid) {
        if (apc == null) {
            return false;
        }
        if (binderUid != 0 && apc.getClientUid() != binderUid) {
            android.util.Log.e(TAG, "Forbidden operation from uid " + binderUid + " for player " + piid);
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchPlaybackChange(boolean iplayerReleased) {
        java.util.List<android.media.AudioPlaybackConfiguration> configsPublic = null;
        synchronized (this.mPlayerLock) {
            if (this.mPlayers.isEmpty()) {
                return;
            }
            java.util.List<android.media.AudioPlaybackConfiguration> configsSystem = new java.util.ArrayList<>(this.mPlayers.values());
            for (com.android.server.audio.PlaybackActivityMonitor.PlayMonitorClient pmc : this.mClients) {
                if (!pmc.reachedMaxErrorCount()) {
                    if (pmc.isPrivileged()) {
                        pmc.dispatchPlaybackConfigChange(configsSystem, iplayerReleased);
                    } else {
                        if (configsPublic == null) {
                            configsPublic = anonymizeForPublicConsumption(configsSystem);
                        }
                        pmc.dispatchPlaybackConfigChange(configsPublic, false);
                    }
                }
            }
        }
    }

    private java.util.ArrayList<android.media.AudioPlaybackConfiguration> anonymizeForPublicConsumption(java.util.List<android.media.AudioPlaybackConfiguration> sysConfigs) {
        java.util.ArrayList<android.media.AudioPlaybackConfiguration> publicConfigs = new java.util.ArrayList<>();
        for (android.media.AudioPlaybackConfiguration config : sysConfigs) {
            if (config.isActive()) {
                publicConfigs.add(android.media.AudioPlaybackConfiguration.anonymizedCopy(config));
            }
        }
        return publicConfigs;
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public boolean duckPlayers(com.android.server.audio.FocusRequester winner, com.android.server.audio.FocusRequester loser, boolean forceDuck) {
        synchronized (this.mPlayerLock) {
            if (this.mPlayers.isEmpty()) {
                return true;
            }
            java.util.ArrayList<android.media.AudioPlaybackConfiguration> apcsToDuck = new java.util.ArrayList<>();
            for (android.media.AudioPlaybackConfiguration apc : this.mPlayers.values()) {
                if (!winner.hasSameUid(apc.getClientUid()) && loser.hasSameUid(apc.getClientUid()) && apc.getPlayerState() == 2) {
                    if (!forceDuck && apc.getAudioAttributes().getContentType() == 1) {
                        android.util.Log.v(TAG, "not ducking player " + apc.getPlayerInterfaceId() + " uid:" + apc.getClientUid() + " pid:" + apc.getClientPid() + " - SPEECH");
                        return false;
                    }
                    if (com.android.internal.util.ArrayUtils.contains(UNDUCKABLE_PLAYER_TYPES, apc.getPlayerType())) {
                        android.util.Log.v(TAG, "not ducking player " + apc.getPlayerInterfaceId() + " uid:" + apc.getClientUid() + " pid:" + apc.getClientPid() + " due to type:" + android.media.AudioPlaybackConfiguration.toLogFriendlyPlayerType(apc.getPlayerType()));
                        return false;
                    }
                    apcsToDuck.add(apc);
                }
            }
            if (this.mPamExt.getExAudioFocusState()) {
                mIsExAudioFocusState = true;
            } else {
                mIsExAudioFocusState = false;
            }
            this.mDuckingManager.duckUid(loser.getClientUid(), apcsToDuck, reqCausesStrongDuck(winner));
            return true;
        }
    }

    private boolean reqCausesStrongDuck(com.android.server.audio.FocusRequester requester) {
        if (requester.getGainRequest() != 3) {
            return false;
        }
        int reqUsage = requester.getAudioAttributes().getUsage();
        return reqUsage == 16;
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public void restoreVShapedPlayers(com.android.server.audio.FocusRequester winner) {
        synchronized (this.mPlayerLock) {
            this.mDuckingManager.unduckUid(winner.getClientUid(), this.mPlayers);
            this.mFadeOutManager.unfadeOutUid(winner.getClientUid(), this.mPlayers);
        }
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public void mutePlayersForCall(int[] usagesToMute) {
        synchronized (this.mPlayerLock) {
            java.util.Set<java.lang.Integer> piidSet = this.mPlayers.keySet();
            for (java.lang.Integer piid : piidSet) {
                android.media.AudioPlaybackConfiguration apc = this.mPlayers.get(piid);
                if (apc != null) {
                    int playerUsage = apc.getAudioAttributes().getUsage();
                    boolean mute = false;
                    int length = usagesToMute.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            break;
                        }
                        int usageToMute = usagesToMute[i];
                        if (playerUsage != usageToMute) {
                            i++;
                        } else {
                            mute = true;
                            break;
                        }
                    }
                    if (mute) {
                        try {
                            sEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("call: muting piid:" + piid + " uid:" + apc.getClientUid()).printLog(TAG));
                            apc.getPlayerProxy().setVolume(0.0f);
                            this.mMutedPlayers.add(new java.lang.Integer(piid.intValue()));
                        } catch (java.lang.Exception e) {
                            android.util.Log.e(TAG, "call: error muting player " + piid, e);
                        }
                    }
                }
            }
        }
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public void unmutePlayersForCall() {
        synchronized (this.mPlayerLock) {
            if (this.mMutedPlayers.isEmpty()) {
                return;
            }
            java.util.Iterator<java.lang.Integer> it = this.mMutedPlayers.iterator();
            while (it.hasNext()) {
                int piid = it.next().intValue();
                android.media.AudioPlaybackConfiguration apc = this.mPlayers.get(java.lang.Integer.valueOf(piid));
                if (apc != null) {
                    try {
                        sEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("call: unmuting piid:" + piid).printLog(TAG));
                        if (this.mPamExt != null && this.mPamExt.isInMusicVolumeMap(apc, this.mContext)) {
                            if (this.mPamExt.isNeedReUnmute(apc.getClientPid())) {
                                this.mEventHandler.sendMessageDelayed(this.mEventHandler.obtainMessage(101, piid, 1), 30L);
                            } else {
                                this.mPamExt.updatePlayerVolumeByApc(apc, this.mContext);
                            }
                        } else if (this.mPamExt != null && this.mPamExt.isNeedReUnmute(apc.getClientPid())) {
                            this.mEventHandler.sendMessageDelayed(this.mEventHandler.obtainMessage(101, piid, 0), 30L);
                        } else {
                            apc.getPlayerProxy().setVolume(1.0f);
                        }
                    } catch (java.lang.Exception e) {
                        android.util.Log.e(TAG, "call: error unmuting player " + piid + " uid:" + apc.getClientUid(), e);
                    }
                }
            }
            this.mMutedPlayers.clear();
        }
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public boolean fadeOutPlayers(com.android.server.audio.FocusRequester winner, com.android.server.audio.FocusRequester loser) {
        boolean loserHasActivePlayers = false;
        synchronized (this.mPlayerLock) {
            if (this.mPlayers.isEmpty()) {
                return false;
            }
            if (!this.mFadeOutManager.canCauseFadeOut(winner, loser)) {
                return false;
            }
            java.util.ArrayList<android.media.AudioPlaybackConfiguration> apcsToFadeOut = new java.util.ArrayList<>();
            for (android.media.AudioPlaybackConfiguration apc : this.mPlayers.values()) {
                if (!winner.hasSameUid(apc.getClientUid()) && loser.hasSameUid(apc.getClientUid()) && apc.getPlayerState() == 2) {
                    if (!this.mFadeOutManager.canBeFadedOut(apc)) {
                        android.util.Log.v(TAG, "not fading out player " + apc.getPlayerInterfaceId() + " uid:" + apc.getClientUid() + " pid:" + apc.getClientPid() + " type:" + android.media.AudioPlaybackConfiguration.toLogFriendlyPlayerType(apc.getPlayerType()) + " attr:" + apc.getAudioAttributes());
                        return false;
                    }
                    loserHasActivePlayers = true;
                    apcsToFadeOut.add(apc);
                }
            }
            if (loserHasActivePlayers) {
                this.mFadeOutManager.fadeOutUid(loser.getClientUid(), apcsToFadeOut);
            }
            return loserHasActivePlayers;
        }
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public void forgetUid(int uid) {
        java.util.HashMap<java.lang.Integer, android.media.AudioPlaybackConfiguration> players;
        synchronized (this.mPlayerLock) {
            players = (java.util.HashMap) this.mPlayers.clone();
        }
        this.mFadeOutManager.unfadeOutUid(uid, players);
        this.mDuckingManager.unduckUid(uid, players);
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public long getFadeOutDurationMillis(android.media.AudioAttributes aa) {
        return this.mFadeOutManager.getFadeOutDurationOnFocusLossMillis(aa);
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public long getFadeInDelayForOffendersMillis(android.media.AudioAttributes aa) {
        return this.mFadeOutManager.getFadeInDelayForOffendersMillis(aa);
    }

    @Override // com.android.server.audio.PlayerFocusEnforcer
    public boolean shouldEnforceFade() {
        return this.mFadeOutManager.isFadeEnabled();
    }

    void registerPlaybackCallback(android.media.IPlaybackConfigDispatcher pcdb, boolean isPrivileged) {
        if (pcdb == null) {
            return;
        }
        com.android.server.audio.PlaybackActivityMonitor.PlayMonitorClient pmc = new com.android.server.audio.PlaybackActivityMonitor.PlayMonitorClient(pcdb, isPrivileged);
        if (pmc.init()) {
            this.mClients.add(pmc);
        }
    }

    void unregisterPlaybackCallback(android.media.IPlaybackConfigDispatcher pcdb) {
        if (pcdb == null) {
            return;
        }
        java.util.Iterator<com.android.server.audio.PlaybackActivityMonitor.PlayMonitorClient> clientIterator = this.mClients.iterator();
        while (clientIterator.hasNext()) {
            com.android.server.audio.PlaybackActivityMonitor.PlayMonitorClient pmc = clientIterator.next();
            if (pmc.equalsDispatcher(pcdb)) {
                pmc.release();
                clientIterator.remove();
            }
        }
    }

    java.util.List<android.media.AudioPlaybackConfiguration> getActivePlaybackConfigurations(boolean isPrivileged) {
        synchronized (this.mPlayerLock) {
            if (isPrivileged) {
                return new java.util.ArrayList(this.mPlayers.values());
            }
            return anonymizeForPublicConsumption(new java.util.ArrayList(this.mPlayers.values()));
        }
    }

    int setFadeManagerConfiguration(int focusType, android.media.FadeManagerConfiguration fadeMgrConfig) {
        return this.mFadeOutManager.setFadeManagerConfiguration(fadeMgrConfig);
    }

    int clearFadeManagerConfiguration(int focusType) {
        return this.mFadeOutManager.clearFadeManagerConfiguration();
    }

    android.media.FadeManagerConfiguration getFadeManagerConfiguration(int focusType) {
        return this.mFadeOutManager.getFadeManagerConfiguration();
    }

    int setTransientFadeManagerConfiguration(int focusType, android.media.FadeManagerConfiguration fadeMgrConfig) {
        return this.mFadeOutManager.setTransientFadeManagerConfiguration(fadeMgrConfig);
    }

    int clearTransientFadeManagerConfiguration(int focusType) {
        return this.mFadeOutManager.clearTransientFadeManagerConfiguration();
    }

    private static final class PlayMonitorClient implements android.os.IBinder.DeathRecipient {
        private static final int MAX_ERRORS = 5;
        static com.android.server.audio.PlaybackActivityMonitor sListenerDeathMonitor;
        private final android.media.IPlaybackConfigDispatcher mDispatcherCb;
        private final boolean mIsPrivileged;
        private boolean mIsReleased = false;
        private int mErrorCount = 0;

        PlayMonitorClient(android.media.IPlaybackConfigDispatcher pcdb, boolean isPrivileged) {
            this.mDispatcherCb = pcdb;
            this.mIsPrivileged = isPrivileged;
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            android.util.Log.w(com.android.server.audio.PlaybackActivityMonitor.TAG, "client died");
            sListenerDeathMonitor.unregisterPlaybackCallback(this.mDispatcherCb);
        }

        synchronized boolean init() {
            if (this.mIsReleased) {
                return false;
            }
            try {
                this.mDispatcherCb.asBinder().linkToDeath(this, 0);
                return true;
            } catch (android.os.RemoteException e) {
                android.util.Log.w(com.android.server.audio.PlaybackActivityMonitor.TAG, "Could not link to client death", e);
                return false;
            }
        }

        synchronized void release() {
            this.mDispatcherCb.asBinder().unlinkToDeath(this, 0);
            this.mIsReleased = true;
        }

        void dispatchPlaybackConfigChange(java.util.List<android.media.AudioPlaybackConfiguration> configs, boolean flush) {
            synchronized (this) {
                if (this.mIsReleased) {
                    return;
                }
                try {
                    this.mDispatcherCb.dispatchPlaybackConfigChange(configs, flush);
                } catch (android.os.RemoteException e) {
                    synchronized (this) {
                        this.mErrorCount++;
                        android.util.Log.e(com.android.server.audio.PlaybackActivityMonitor.TAG, "Error (" + this.mErrorCount + ") trying to dispatch playback config change to " + this, e);
                    }
                }
            }
        }

        synchronized boolean isPrivileged() {
            return this.mIsPrivileged;
        }

        synchronized boolean reachedMaxErrorCount() {
            return this.mErrorCount >= 5;
        }

        synchronized boolean equalsDispatcher(android.media.IPlaybackConfigDispatcher pcdb) {
            if (pcdb == null) {
                return false;
            }
            return pcdb.asBinder().equals(this.mDispatcherCb.asBinder());
        }
    }

    private static final class DuckingManager {
        private final java.util.HashMap<java.lang.Integer, com.android.server.audio.PlaybackActivityMonitor.DuckingManager.DuckedApp> mDuckers;

        private DuckingManager() {
            this.mDuckers = new java.util.HashMap<>();
        }

        synchronized void duckUid(int uid, java.util.ArrayList<android.media.AudioPlaybackConfiguration> apcsToDuck, boolean requestCausesStrongDuck) {
            if (!this.mDuckers.containsKey(java.lang.Integer.valueOf(uid))) {
                this.mDuckers.put(java.lang.Integer.valueOf(uid), new com.android.server.audio.PlaybackActivityMonitor.DuckingManager.DuckedApp(uid, requestCausesStrongDuck));
            }
            com.android.server.audio.PlaybackActivityMonitor.DuckingManager.DuckedApp da = this.mDuckers.get(java.lang.Integer.valueOf(uid));
            for (android.media.AudioPlaybackConfiguration apc : apcsToDuck) {
                da.addDuck(apc, false);
            }
        }

        synchronized void unduckUid(int uid, java.util.HashMap<java.lang.Integer, android.media.AudioPlaybackConfiguration> players) {
            com.android.server.audio.PlaybackActivityMonitor.DuckingManager.DuckedApp da = this.mDuckers.remove(java.lang.Integer.valueOf(uid));
            if (da == null) {
                return;
            }
            da.removeUnduckAll(players);
        }

        synchronized void checkDuck(android.media.AudioPlaybackConfiguration apc) {
            com.android.server.audio.PlaybackActivityMonitor.DuckingManager.DuckedApp da = this.mDuckers.get(java.lang.Integer.valueOf(apc.getClientUid()));
            if (da == null) {
                return;
            }
            da.addDuck(apc, true);
        }

        synchronized void dump(java.io.PrintWriter pw) {
            for (com.android.server.audio.PlaybackActivityMonitor.DuckingManager.DuckedApp da : this.mDuckers.values()) {
                da.dump(pw);
            }
        }

        synchronized void removeReleased(android.media.AudioPlaybackConfiguration apc) {
            int uid = apc.getClientUid();
            com.android.server.audio.PlaybackActivityMonitor.DuckingManager.DuckedApp da = this.mDuckers.get(java.lang.Integer.valueOf(uid));
            if (da == null) {
                return;
            }
            da.removeReleased(apc);
        }

        private static final class DuckedApp {
            private final java.util.ArrayList<java.lang.Integer> mDuckedPlayers = new java.util.ArrayList<>();
            private final int mUid;
            private final boolean mUseStrongDuck;

            DuckedApp(int uid, boolean useStrongDuck) {
                this.mUid = uid;
                this.mUseStrongDuck = useStrongDuck;
            }

            void dump(java.io.PrintWriter pw) {
                pw.print("\t uid:" + this.mUid + " piids:");
                java.util.Iterator<java.lang.Integer> it = this.mDuckedPlayers.iterator();
                while (it.hasNext()) {
                    int piid = it.next().intValue();
                    pw.print(" " + piid);
                }
                pw.println("");
            }

            void addDuck(android.media.AudioPlaybackConfiguration apc, boolean skipRamp) {
                int piid = new java.lang.Integer(apc.getPlayerInterfaceId()).intValue();
                if (this.mDuckedPlayers.contains(java.lang.Integer.valueOf(piid))) {
                    return;
                }
                try {
                    android.media.VolumeShaper.Configuration config = this.mUseStrongDuck ? com.android.server.audio.PlaybackActivityMonitor.STRONG_DUCK_VSHAPE : com.android.server.audio.PlaybackActivityMonitor.DUCK_VSHAPE;
                    android.media.VolumeShaper.Operation operation = skipRamp ? com.android.server.audio.PlaybackActivityMonitor.PLAY_SKIP_RAMP : com.android.server.audio.PlaybackActivityMonitor.PLAY_CREATE_IF_NEEDED;
                    com.android.server.audio.PlaybackActivityMonitor.sEventLogger.enqueue(new com.android.server.audio.PlaybackActivityMonitor.DuckEvent(apc, skipRamp, this.mUseStrongDuck, config, operation).printLog(com.android.server.audio.PlaybackActivityMonitor.TAG));
                    if (android.media.AudioSystem.getParameters(com.android.server.audio.PlaybackActivityMonitor.PARAM_META_AUDIO + this.mUid).contains("true")) {
                        android.util.Log.w(com.android.server.audio.PlaybackActivityMonitor.TAG, "donot change shaper volume for HoloAudio.");
                        this.mDuckedPlayers.add(java.lang.Integer.valueOf(piid));
                    } else if (com.android.server.audio.PlaybackActivityMonitor.mIsExAudioFocusState) {
                        android.util.Log.w(com.android.server.audio.PlaybackActivityMonitor.TAG, "donot change shaper volume in exaudiofocus state");
                        this.mDuckedPlayers.add(java.lang.Integer.valueOf(piid));
                    } else {
                        apc.getPlayerProxy().applyVolumeShaper(config, operation);
                        this.mDuckedPlayers.add(java.lang.Integer.valueOf(piid));
                    }
                } catch (java.lang.Exception e) {
                    android.util.Log.e(com.android.server.audio.PlaybackActivityMonitor.TAG, "Error ducking player piid:" + piid + " uid:" + this.mUid, e);
                }
            }

            void removeUnduckAll(java.util.HashMap<java.lang.Integer, android.media.AudioPlaybackConfiguration> players) {
                java.util.Iterator<java.lang.Integer> it = this.mDuckedPlayers.iterator();
                while (it.hasNext()) {
                    int piid = it.next().intValue();
                    android.media.AudioPlaybackConfiguration apc = players.get(java.lang.Integer.valueOf(piid));
                    if (apc != null) {
                        try {
                            com.android.server.audio.PlaybackActivityMonitor.sEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("unducking piid:" + piid).printLog(com.android.server.audio.PlaybackActivityMonitor.TAG));
                            apc.getPlayerProxy().applyVolumeShaper(this.mUseStrongDuck ? com.android.server.audio.PlaybackActivityMonitor.STRONG_DUCK_ID : com.android.server.audio.PlaybackActivityMonitor.DUCK_ID, android.media.VolumeShaper.Operation.REVERSE);
                        } catch (java.lang.Exception e) {
                            android.util.Log.e(com.android.server.audio.PlaybackActivityMonitor.TAG, "Error unducking player piid:" + piid + " uid:" + this.mUid, e);
                        }
                    }
                }
                this.mDuckedPlayers.clear();
            }

            void removeReleased(android.media.AudioPlaybackConfiguration apc) {
                this.mDuckedPlayers.remove(new java.lang.Integer(apc.getPlayerInterfaceId()));
            }
        }
    }

    protected java.util.List<java.lang.Integer> getFocusDuckedUids() {
        java.util.ArrayList<java.lang.Integer> duckedUids;
        synchronized (this.mPlayerLock) {
            duckedUids = new java.util.ArrayList<>(this.mDuckingManager.mDuckers.keySet());
        }
        return duckedUids;
    }

    private static final class PlayerEvent extends com.android.server.utils.EventLogger.Event {
        final int mEvent;
        final int mEventValue;
        final int mPlayerIId;

        PlayerEvent(int piid, int event, int eventValue) {
            this.mPlayerIId = piid;
            this.mEvent = event;
            this.mEventValue = eventValue;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder("player piid:").append(this.mPlayerIId).append(" event:").append(android.media.AudioPlaybackConfiguration.toLogFriendlyPlayerState(this.mEvent));
            switch (this.mEvent) {
                case 5:
                    if (this.mEventValue != 0) {
                        builder.append(" deviceId:").append(this.mEventValue);
                    }
                    break;
                case 7:
                    builder.append(" source:");
                    if (this.mEventValue <= 0) {
                        builder.append("none ");
                    } else {
                        if ((this.mEventValue & 1) != 0) {
                            builder.append("masterMute ");
                        }
                        if ((this.mEventValue & 2) != 0) {
                            builder.append("streamVolume ");
                        }
                        if ((this.mEventValue & 4) != 0) {
                            builder.append("streamMute ");
                        }
                        if ((this.mEventValue & 8) != 0) {
                            builder.append("appOps ");
                        }
                        if ((this.mEventValue & 16) != 0) {
                            builder.append("clientVolume ");
                        }
                        if ((this.mEventValue & 32) != 0) {
                            builder.append("volumeShaper ");
                        }
                    }
                    break;
            }
            return builder.toString();
        }
    }

    private static final class PlayerOpPlayAudioEvent extends com.android.server.utils.EventLogger.Event {
        final boolean mHasOp;
        final int mPlayerIId;
        final int mUid;

        PlayerOpPlayAudioEvent(int piid, boolean hasOp, int uid) {
            this.mPlayerIId = piid;
            this.mHasOp = hasOp;
            this.mUid = uid;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            return "player piid:" + this.mPlayerIId + " has OP_PLAY_AUDIO:" + this.mHasOp + " in uid:" + this.mUid;
        }
    }

    private static final class NewPlayerEvent extends com.android.server.utils.EventLogger.Event {
        private final int mClientPid;
        private final int mClientUid;
        private final android.media.AudioAttributes mPlayerAttr;
        private final int mPlayerIId;
        private final int mPlayerType;
        private final int mSessionId;

        NewPlayerEvent(android.media.AudioPlaybackConfiguration apc) {
            this.mPlayerIId = apc.getPlayerInterfaceId();
            this.mPlayerType = apc.getPlayerType();
            this.mClientUid = apc.getClientUid();
            this.mClientPid = apc.getClientPid();
            this.mPlayerAttr = apc.getAudioAttributes();
            this.mSessionId = apc.getSessionId();
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            return new java.lang.String("new player piid:" + this.mPlayerIId + " uid/pid:" + this.mClientUid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.mClientPid + " type:" + android.media.AudioPlaybackConfiguration.toLogFriendlyPlayerType(this.mPlayerType) + " attr:" + this.mPlayerAttr + " session:" + this.mSessionId);
        }
    }

    private static abstract class VolumeShaperEvent extends com.android.server.utils.EventLogger.Event {
        private final int mClientPid;
        private final int mClientUid;
        private final android.media.VolumeShaper.Configuration mConfig;
        private final android.media.VolumeShaper.Operation mOperation;
        private final android.media.AudioAttributes mPlayerAttr;
        private final int mPlayerIId;
        private final int mPlayerType;
        private final boolean mSkipRamp;

        abstract java.lang.String getVSAction();

        VolumeShaperEvent(android.media.AudioPlaybackConfiguration apc, boolean skipRamp, android.media.VolumeShaper.Configuration config, android.media.VolumeShaper.Operation operation) {
            this.mPlayerIId = apc.getPlayerInterfaceId();
            this.mSkipRamp = skipRamp;
            this.mClientUid = apc.getClientUid();
            this.mClientPid = apc.getClientPid();
            this.mPlayerAttr = apc.getAudioAttributes();
            this.mPlayerType = apc.getPlayerType();
            this.mConfig = config;
            this.mOperation = operation;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            return getVSAction() + " player piid:" + this.mPlayerIId + " uid/pid:" + this.mClientUid + com.android.server.slice.SliceClientPermissions.SliceAuthority.DELIMITER + this.mClientPid + " skip ramp:" + this.mSkipRamp + " player type:" + android.media.AudioPlaybackConfiguration.toLogFriendlyPlayerType(this.mPlayerType) + " attr:" + this.mPlayerAttr + " config:" + this.mConfig + " operation:" + this.mOperation;
        }
    }

    static final class DuckEvent extends com.android.server.audio.PlaybackActivityMonitor.VolumeShaperEvent {
        final boolean mUseStrongDuck;

        @Override // com.android.server.audio.PlaybackActivityMonitor.VolumeShaperEvent
        java.lang.String getVSAction() {
            return this.mUseStrongDuck ? "ducking (strong)" : "ducking";
        }

        DuckEvent(android.media.AudioPlaybackConfiguration apc, boolean skipRamp, boolean useStrongDuck, android.media.VolumeShaper.Configuration config, android.media.VolumeShaper.Operation operation) {
            super(apc, skipRamp, config, operation);
            this.mUseStrongDuck = useStrongDuck;
        }
    }

    static final class FadeOutEvent extends com.android.server.audio.PlaybackActivityMonitor.VolumeShaperEvent {
        @Override // com.android.server.audio.PlaybackActivityMonitor.VolumeShaperEvent
        java.lang.String getVSAction() {
            return com.android.server.audio.PlaybackActivityMonitor.EVENT_TYPE_FADE_OUT;
        }

        FadeOutEvent(android.media.AudioPlaybackConfiguration apc, boolean skipRamp, android.media.VolumeShaper.Configuration config, android.media.VolumeShaper.Operation operation) {
            super(apc, skipRamp, config, operation);
        }
    }

    static final class FadeInEvent extends com.android.server.audio.PlaybackActivityMonitor.VolumeShaperEvent {
        @Override // com.android.server.audio.PlaybackActivityMonitor.VolumeShaperEvent
        java.lang.String getVSAction() {
            return com.android.server.audio.PlaybackActivityMonitor.EVENT_TYPE_FADE_IN;
        }

        FadeInEvent(android.media.AudioPlaybackConfiguration apc, boolean skipRamp, android.media.VolumeShaper.Configuration config, android.media.VolumeShaper.Operation operation) {
            super(apc, skipRamp, config, operation);
        }
    }

    private static final class AudioAttrEvent extends com.android.server.utils.EventLogger.Event {
        private final android.media.AudioAttributes mPlayerAttr;
        private final int mPlayerIId;

        AudioAttrEvent(int piid, android.media.AudioAttributes attr) {
            this.mPlayerIId = piid;
            this.mPlayerAttr = attr;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            return new java.lang.String("player piid:" + this.mPlayerIId + " new AudioAttributes:" + this.mPlayerAttr);
        }
    }

    private static final class MuteAwaitConnectionEvent extends com.android.server.utils.EventLogger.Event {
        private final int[] mUsagesToMute;

        MuteAwaitConnectionEvent(int[] usagesToMute) {
            this.mUsagesToMute = usagesToMute;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            return "muteAwaitConnection muting usages " + java.util.Arrays.toString(this.mUsagesToMute);
        }
    }

    private static final class PlayerFormatEvent extends com.android.server.utils.EventLogger.Event {
        private final android.media.AudioPlaybackConfiguration.FormatInfo mFormat;
        private final int mPlayerIId;

        PlayerFormatEvent(int piid, android.media.AudioPlaybackConfiguration.FormatInfo format) {
            this.mPlayerIId = piid;
            this.mFormat = format;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            return new java.lang.String("player piid:" + this.mPlayerIId + " format update:" + this.mFormat);
        }
    }

    void muteAwaitConnection(int[] usagesToMute, android.media.AudioDeviceAttributes dev, long timeOutMs) {
        sEventLogger.enqueueAndLog("muteAwaitConnection() dev:" + dev + " timeOutMs:" + timeOutMs, 0, TAG);
        synchronized (this.mPlayerLock) {
            mutePlayersExpectingDevice(usagesToMute);
            this.mEventHandler.removeMessages(1);
            this.mEventHandler.sendMessageDelayed(this.mEventHandler.obtainMessage(1, dev), timeOutMs);
        }
    }

    void cancelMuteAwaitConnection(java.lang.String source) {
        sEventLogger.enqueueAndLog("cancelMuteAwaitConnection() from:" + source, 0, TAG);
        synchronized (this.mPlayerLock) {
            this.mEventHandler.removeMessages(1);
            unmutePlayersExpectingDevice();
        }
    }

    private void mutePlayersExpectingDevice(int[] usagesToMute) {
        sEventLogger.enqueue(new com.android.server.audio.PlaybackActivityMonitor.MuteAwaitConnectionEvent(usagesToMute));
        this.mMutedUsagesAwaitingConnection = usagesToMute;
        java.util.Set<java.lang.Integer> piidSet = this.mPlayers.keySet();
        for (java.lang.Integer piid : piidSet) {
            android.media.AudioPlaybackConfiguration apc = this.mPlayers.get(piid);
            if (apc != null) {
                maybeMutePlayerAwaitingConnection(apc);
            }
        }
    }

    private void maybeMutePlayerAwaitingConnection(android.media.AudioPlaybackConfiguration apc) {
        if (this.mMutedUsagesAwaitingConnection == null) {
            return;
        }
        for (int usage : this.mMutedUsagesAwaitingConnection) {
            if (usage == apc.getAudioAttributes().getUsage()) {
                try {
                    sEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("awaiting connection: muting piid:" + apc.getPlayerInterfaceId() + " uid:" + apc.getClientUid()).printLog(TAG));
                    apc.getPlayerProxy().applyVolumeShaper(MUTE_AWAIT_CONNECTION_VSHAPE, PLAY_SKIP_RAMP);
                    this.mMutedPlayersAwaitingConnection.add(java.lang.Integer.valueOf(apc.getPlayerInterfaceId()));
                } catch (java.lang.Exception e) {
                    android.util.Log.e(TAG, "awaiting connection: error muting player " + apc.getPlayerInterfaceId(), e);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void unmutePlayersExpectingDevice() {
        this.mMutedUsagesAwaitingConnection = null;
        java.util.Iterator<java.lang.Integer> it = this.mMutedPlayersAwaitingConnection.iterator();
        while (it.hasNext()) {
            int piid = it.next().intValue();
            android.media.AudioPlaybackConfiguration apc = this.mPlayers.get(java.lang.Integer.valueOf(piid));
            if (apc != null) {
                try {
                    sEventLogger.enqueue(new com.android.server.utils.EventLogger.StringEvent("unmuting piid:" + piid).printLog(TAG));
                    apc.getPlayerProxy().applyVolumeShaper(MUTE_AWAIT_CONNECTION_VSHAPE, android.media.VolumeShaper.Operation.REVERSE);
                } catch (java.lang.Exception e) {
                    android.util.Log.e(TAG, "Error unmuting player " + piid + " uid:" + apc.getClientUid(), e);
                }
            }
        }
        this.mMutedPlayersAwaitingConnection.clear();
    }

    private void initEventHandler() {
        this.mEventThread = new android.os.HandlerThread(TAG);
        this.mEventThread.start();
        this.mEventHandler = new android.os.Handler(this.mEventThread.getLooper()) { // from class: com.android.server.audio.PlaybackActivityMonitor.1
            @Override // android.os.Handler
            public void handleMessage(android.os.Message msg) {
                android.media.AudioPlaybackConfiguration apc;
                switch (msg.what) {
                    case 1:
                        com.android.server.audio.PlaybackActivityMonitor.sEventLogger.enqueueAndLog("Timeout for muting waiting for " + ((android.media.AudioDeviceAttributes) msg.obj) + ", unmuting", 0, com.android.server.audio.PlaybackActivityMonitor.TAG);
                        synchronized (com.android.server.audio.PlaybackActivityMonitor.this.mPlayerLock) {
                            com.android.server.audio.PlaybackActivityMonitor.this.unmutePlayersExpectingDevice();
                            break;
                        }
                        com.android.server.audio.PlaybackActivityMonitor.this.mMuteAwaitConnectionTimeoutCb.accept((android.media.AudioDeviceAttributes) msg.obj);
                        return;
                    case 2:
                        android.os.PersistableBundle extras = (android.os.PersistableBundle) msg.obj;
                        if (extras == null) {
                            android.util.Log.w(com.android.server.audio.PlaybackActivityMonitor.TAG, "Received mute event with no extras");
                            return;
                        }
                        int eventValue = extras.getInt("android.media.extra.PLAYER_EVENT_MUTE");
                        synchronized (com.android.server.audio.PlaybackActivityMonitor.this.mPlayerLock) {
                            int piid = msg.arg1;
                            com.android.server.audio.PlaybackActivityMonitor.sEventLogger.enqueue(new com.android.server.audio.PlaybackActivityMonitor.PlayerEvent(piid, 7, eventValue));
                            android.media.AudioPlaybackConfiguration apc2 = (android.media.AudioPlaybackConfiguration) com.android.server.audio.PlaybackActivityMonitor.this.mPlayers.get(java.lang.Integer.valueOf(piid));
                            if (apc2 != null && apc2.handleMutedEvent(eventValue)) {
                                com.android.server.audio.PlaybackActivityMonitor.this.dispatchPlaybackChange(false);
                            }
                        }
                        return;
                    case 3:
                        android.os.PersistableBundle formatExtras = (android.os.PersistableBundle) msg.obj;
                        if (formatExtras == null) {
                            android.util.Log.w(com.android.server.audio.PlaybackActivityMonitor.TAG, "Received format event with no extras");
                            return;
                        }
                        boolean spatialized = formatExtras.getBoolean("android.media.extra.PLAYER_EVENT_SPATIALIZED", false);
                        int sampleRate = formatExtras.getInt("android.media.extra.PLAYER_EVENT_SAMPLE_RATE", 0);
                        int nativeChannelMask = formatExtras.getInt("android.media.extra.PLAYER_EVENT_CHANNEL_MASK", 0);
                        android.media.AudioPlaybackConfiguration.FormatInfo format = new android.media.AudioPlaybackConfiguration.FormatInfo(spatialized, nativeChannelMask, sampleRate);
                        com.android.server.audio.PlaybackActivityMonitor.sEventLogger.enqueue(new com.android.server.audio.PlaybackActivityMonitor.PlayerFormatEvent(msg.arg1, format));
                        synchronized (com.android.server.audio.PlaybackActivityMonitor.this.mPlayerLock) {
                            apc = (android.media.AudioPlaybackConfiguration) com.android.server.audio.PlaybackActivityMonitor.this.mPlayers.get(java.lang.Integer.valueOf(msg.arg1));
                            break;
                        }
                        if (apc != null && apc.handleFormatEvent(format)) {
                            com.android.server.audio.PlaybackActivityMonitor.this.dispatchPlaybackChange(false);
                            return;
                        }
                        return;
                    case 100:
                        com.android.server.audio.PlaybackActivityMonitor.this.oplusUpdateTrackVolume(msg.arg1);
                        return;
                    case 101:
                        com.android.server.audio.PlaybackActivityMonitor.this.oplusReUnmutePlayersForCall(msg.arg1, msg.arg2);
                        return;
                    default:
                        return;
                }
            }
        };
    }

    public com.android.server.audio.IPlaybackActivityMonitorWrapper getWrapper() {
        return this.mPamWrapper;
    }

    private class PlaybackActivityMonitorWrapper implements com.android.server.audio.IPlaybackActivityMonitorWrapper {
        private PlaybackActivityMonitorWrapper() {
        }

        @Override // com.android.server.audio.IPlaybackActivityMonitorWrapper
        public com.android.server.audio.IPlaybackActivityMonitorExt getExtImpl() {
            return com.android.server.audio.PlaybackActivityMonitor.this.mPamExt;
        }

        @Override // com.android.server.audio.IPlaybackActivityMonitorWrapper
        public java.lang.Object getPlayerLock() {
            return com.android.server.audio.PlaybackActivityMonitor.this.mPlayerLock;
        }

        @Override // com.android.server.audio.IPlaybackActivityMonitorWrapper
        public java.util.HashMap<java.lang.Integer, android.media.AudioPlaybackConfiguration> getPlayers() {
            return com.android.server.audio.PlaybackActivityMonitor.this.mPlayers;
        }

        @Override // com.android.server.audio.IPlaybackActivityMonitorWrapper
        public android.content.Context getContext() {
            return com.android.server.audio.PlaybackActivityMonitor.this.mContext;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void oplusUpdateTrackVolume(int piid) {
        synchronized (this.mPlayerLock) {
            android.media.AudioPlaybackConfiguration apc = this.mPlayers.get(new java.lang.Integer(piid));
            if (this.mPamExt != null && this.mContext != null && this.mPamExt.isInMusicVolumeMap(apc, this.mContext)) {
                this.mPamExt.updatePlayerVolumeByApc(apc, this.mContext);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void oplusReUnmutePlayersForCall(int piid, int flag) {
        synchronized (this.mPlayerLock) {
            android.media.AudioPlaybackConfiguration apc = this.mPlayers.get(new java.lang.Integer(piid));
            if (apc == null || this.mPamExt == null) {
                return;
            }
            if (flag == 0) {
                try {
                    android.media.PlayerProxy playerProxy = apc.getPlayerProxy();
                    if (playerProxy != null) {
                        playerProxy.setVolume(1.0f);
                    }
                } catch (java.lang.IllegalStateException e) {
                    android.util.Log.e(TAG, "error setVolume when oplusReUnmutePlayersForCall", e);
                }
            } else {
                this.mPamExt.updatePlayerVolumeByApc(apc, this.mContext);
            }
        }
    }
}
