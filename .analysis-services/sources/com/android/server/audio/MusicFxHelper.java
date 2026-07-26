package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class MusicFxHelper {
    static final int MSG_EFFECT_CLIENT_GONE = 1101;
    private static final java.lang.String TAG = "AS.MusicFxHelper";
    private final com.android.server.audio.AudioService.AudioHandler mAudioHandler;
    private final android.content.Context mContext;
    private boolean mIsBinded;
    private final java.lang.Object mClientUidMapLock = new java.lang.Object();
    private final java.lang.String mPackageName = getClass().getPackage().getName();
    private final java.lang.String mMusicFxPackageName = "com.android.musicfx";
    private android.os.IBinder mUidObserverToken = null;
    private com.android.server.audio.MusicFxHelper.MySparseArray mClientUidSessionMap = new com.android.server.audio.MusicFxHelper.MySparseArray();
    private final android.app.IUidObserver mEffectUidObserver = new android.app.UidObserver() { // from class: com.android.server.audio.MusicFxHelper.1
        public void onUidGone(int uid, boolean disabled) {
            android.util.Log.w(com.android.server.audio.MusicFxHelper.TAG, " send MSG_EFFECT_CLIENT_GONE");
            com.android.server.audio.MusicFxHelper.this.mAudioHandler.sendMessageAtTime(com.android.server.audio.MusicFxHelper.this.mAudioHandler.obtainMessage(com.android.server.audio.MusicFxHelper.MSG_EFFECT_CLIENT_GONE, uid, 0, null), 0L);
        }
    };
    private android.content.ServiceConnection mMusicFxBindConnection = new android.content.ServiceConnection() { // from class: com.android.server.audio.MusicFxHelper.2
        @Override // android.content.ServiceConnection
        public void onServiceConnected(android.content.ComponentName name, android.os.IBinder service) {
            android.util.Log.d(com.android.server.audio.MusicFxHelper.TAG, " service connected to " + name);
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(android.content.ComponentName name) {
            android.util.Log.d(com.android.server.audio.MusicFxHelper.TAG, " service disconnected from " + name);
        }
    };

    private static class PackageSessions {
        java.lang.String mPackageName;
        java.util.List<java.lang.Integer> mSessions;

        private PackageSessions() {
        }
    }

    private class MySparseArray extends android.util.SparseArray<com.android.server.audio.MusicFxHelper.PackageSessions> {
        private MySparseArray() {
        }

        @Override // android.util.SparseArray
        public void put(int uid, com.android.server.audio.MusicFxHelper.PackageSessions pkgSessions) {
            if (size() == 0) {
                int procState = 20;
                try {
                    procState = android.app.ActivityManager.getService().getPackageProcessState("com.android.musicfx", com.android.server.audio.MusicFxHelper.this.mPackageName);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.audio.MusicFxHelper.TAG, "RemoteException with getPackageProcessState: " + e);
                }
                if (procState > 6) {
                    android.content.Intent bindIntent = new android.content.Intent().setClassName("com.android.musicfx", "com.android.musicfx.KeepAliveService");
                    com.android.server.audio.MusicFxHelper.this.mIsBinded = com.android.server.audio.MusicFxHelper.this.mContext.bindServiceAsUser(bindIntent, com.android.server.audio.MusicFxHelper.this.mMusicFxBindConnection, 1, android.os.UserHandle.of(com.android.server.audio.MusicFxHelper.this.getCurrentUserId()));
                    android.util.Log.i(com.android.server.audio.MusicFxHelper.TAG, "bindService to com.android.musicfx");
                }
                android.util.Log.i(com.android.server.audio.MusicFxHelper.TAG, "com.android.musicfx procState " + procState);
            }
            try {
                if (com.android.server.audio.MusicFxHelper.this.mUidObserverToken == null) {
                    com.android.server.audio.MusicFxHelper.this.mUidObserverToken = android.app.ActivityManager.getService().registerUidObserverForUids(com.android.server.audio.MusicFxHelper.this.mEffectUidObserver, 2, -1, com.android.server.audio.MusicFxHelper.this.mPackageName, new int[]{uid});
                    android.util.Log.i(com.android.server.audio.MusicFxHelper.TAG, "registered to observer with UID " + uid);
                } else if (get(uid) == null) {
                    android.app.ActivityManager.getService().addUidToObserver(com.android.server.audio.MusicFxHelper.this.mUidObserverToken, com.android.server.audio.MusicFxHelper.this.mPackageName, uid);
                    android.util.Log.i(com.android.server.audio.MusicFxHelper.TAG, " UID " + uid + " add to observer");
                }
            } catch (android.os.RemoteException e2) {
                android.util.Log.e(com.android.server.audio.MusicFxHelper.TAG, "RemoteException with UID observer add/register: " + e2);
            }
            super.put(uid, pkgSessions);
        }

        @Override // android.util.SparseArray
        public void remove(int uid) {
            if (get(uid) != null) {
                try {
                    android.app.ActivityManager.getService().removeUidFromObserver(com.android.server.audio.MusicFxHelper.this.mUidObserverToken, com.android.server.audio.MusicFxHelper.this.mPackageName, uid);
                } catch (android.os.RemoteException e) {
                    android.util.Log.e(com.android.server.audio.MusicFxHelper.TAG, "RemoteException with removeUidFromObserver: " + e);
                }
            }
            super.remove(uid);
            if (size() == 0) {
                try {
                    android.app.ActivityManager.getService().unregisterUidObserver(com.android.server.audio.MusicFxHelper.this.mEffectUidObserver);
                } catch (android.os.RemoteException e2) {
                    android.util.Log.e(com.android.server.audio.MusicFxHelper.TAG, "RemoteException with unregisterUidObserver: " + e2);
                }
                com.android.server.audio.MusicFxHelper.this.mUidObserverToken = null;
                if (com.android.server.audio.MusicFxHelper.this.mIsBinded) {
                    com.android.server.audio.MusicFxHelper.this.mContext.unbindService(com.android.server.audio.MusicFxHelper.this.mMusicFxBindConnection);
                    android.util.Log.i(com.android.server.audio.MusicFxHelper.TAG, "last session closed, unregister UID observer, and unbind com.android.musicfx");
                }
            }
        }
    }

    MusicFxHelper(android.content.Context context, com.android.server.audio.AudioService.AudioHandler audioHandler) {
        this.mContext = context;
        this.mAudioHandler = audioHandler;
    }

    public void handleAudioEffectBroadcast(android.content.Context context, android.content.Intent intent) {
        java.lang.String target = intent.getPackage();
        if (target != null) {
            android.util.Log.w(TAG, "effect broadcast already targeted to " + target);
            return;
        }
        android.content.pm.PackageManager pm = context.getPackageManager();
        java.util.List<android.content.pm.ResolveInfo> ril = pm.queryBroadcastReceivers(intent, 0);
        if (ril != null && ril.size() != 0) {
            android.content.pm.ResolveInfo ri = ril.get(0);
            java.lang.String senderPackageName = intent.getStringExtra("android.media.extra.PACKAGE_NAME");
            if (senderPackageName == null) {
                android.util.Log.w(TAG, "Intent package name must not be null");
                return;
            }
            if (ri != null) {
                try {
                    if (ri.activityInfo != null && ri.activityInfo.packageName != null) {
                        int senderUid = pm.getPackageUidAsUser(senderPackageName, android.content.pm.PackageManager.PackageInfoFlags.of(4194304L), getCurrentUserId());
                        intent.addFlags(32);
                        intent.setPackage(ri.activityInfo.packageName);
                        if (setMusicFxServiceWithObserver(intent, senderUid, senderPackageName)) {
                            context.sendBroadcastAsUser(intent, android.os.UserHandle.ALL);
                            return;
                        }
                        return;
                    }
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    android.util.Log.e(TAG, "Not able to find UID from package: " + senderPackageName + " error: " + e);
                }
            }
        }
        android.util.Log.w(TAG, "couldn't find receiver package for effect intent");
    }

    private boolean handleAudioEffectSessionOpen(int senderUid, java.lang.String senderPackageName, int sessionId) {
        android.util.Log.d(TAG, senderPackageName + " UID " + senderUid + " open MusicFx session " + sessionId);
        com.android.server.audio.MusicFxHelper.PackageSessions pkgSessions = this.mClientUidSessionMap.get(java.lang.Integer.valueOf(senderUid).intValue());
        if (pkgSessions != null && pkgSessions.mSessions != null) {
            if (pkgSessions.mSessions.contains(java.lang.Integer.valueOf(sessionId))) {
                android.util.Log.e(TAG, "Audio session " + sessionId + " already open for UID: " + senderUid + ", package: " + senderPackageName + ", abort");
                return false;
            }
            if (!pkgSessions.mPackageName.equals(senderPackageName)) {
                android.util.Log.w(TAG, "Inconsistency package names for UID open: " + senderUid + " prev: " + pkgSessions.mPackageName + ", now: " + senderPackageName);
                return false;
            }
        } else {
            pkgSessions = new com.android.server.audio.MusicFxHelper.PackageSessions();
            pkgSessions.mSessions = new java.util.ArrayList();
            pkgSessions.mPackageName = senderPackageName;
        }
        pkgSessions.mSessions.add(java.lang.Integer.valueOf(sessionId));
        this.mClientUidSessionMap.put(java.lang.Integer.valueOf(senderUid).intValue(), pkgSessions);
        return true;
    }

    private boolean handleAudioEffectSessionClose(int senderUid, java.lang.String senderPackageName, int sessionId) {
        android.util.Log.d(TAG, senderPackageName + " UID " + senderUid + " close MusicFx session " + sessionId);
        com.android.server.audio.MusicFxHelper.PackageSessions pkgSessions = this.mClientUidSessionMap.get(java.lang.Integer.valueOf(senderUid).intValue());
        if (pkgSessions == null) {
            android.util.Log.e(TAG, senderPackageName + " UID " + senderUid + " does not exist in map, abort");
            return false;
        }
        if (!pkgSessions.mPackageName.equals(senderPackageName)) {
            android.util.Log.w(TAG, "Inconsistency package names for UID " + senderUid + " close, prev: " + pkgSessions.mPackageName + ", now: " + senderPackageName);
            return false;
        }
        if (pkgSessions.mSessions != null && pkgSessions.mSessions.size() != 0) {
            if (!pkgSessions.mSessions.contains(java.lang.Integer.valueOf(sessionId))) {
                android.util.Log.e(TAG, senderPackageName + " UID " + senderUid + " session " + sessionId + " does not exist in map, abort");
                return false;
            }
            pkgSessions.mSessions.remove(java.lang.Integer.valueOf(sessionId));
        }
        if (pkgSessions.mSessions == null || pkgSessions.mSessions.size() == 0) {
            this.mClientUidSessionMap.remove(java.lang.Integer.valueOf(senderUid).intValue());
            return true;
        }
        this.mClientUidSessionMap.put(java.lang.Integer.valueOf(senderUid).intValue(), pkgSessions);
        return true;
    }

    private boolean setMusicFxServiceWithObserver(android.content.Intent intent, int senderUid, java.lang.String packageName) {
        int session = intent.getIntExtra("android.media.extra.AUDIO_SESSION", 0);
        if (session == 0) {
            android.util.Log.e(TAG, packageName + " intent have no invalid audio session");
            return false;
        }
        synchronized (this.mClientUidMapLock) {
            if (intent.getAction().equals("android.media.action.OPEN_AUDIO_EFFECT_CONTROL_SESSION")) {
                return handleAudioEffectSessionOpen(senderUid, packageName, session);
            }
            return handleAudioEffectSessionClose(senderUid, packageName, session);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getCurrentUserId() {
        long ident = android.os.Binder.clearCallingIdentity();
        try {
            android.content.pm.UserInfo currentUser = android.app.ActivityManager.getService().getCurrentUser();
            int i = currentUser.id;
            android.os.Binder.restoreCallingIdentity(ident);
            return i;
        } catch (android.os.RemoteException e) {
            android.os.Binder.restoreCallingIdentity(ident);
            return 0;
        } catch (java.lang.Throwable th) {
            android.os.Binder.restoreCallingIdentity(ident);
            throw th;
        }
    }

    private void handleEffectClientUidGone(int uid) {
        synchronized (this.mClientUidMapLock) {
            android.util.Log.d(TAG, "handle MSG_EFFECT_CLIENT_GONE uid: " + uid + " mapSize: " + this.mClientUidSessionMap.size());
            com.android.server.audio.MusicFxHelper.PackageSessions pkgSessions = this.mClientUidSessionMap.get(java.lang.Integer.valueOf(uid).intValue());
            if (pkgSessions != null) {
                android.util.Log.i(TAG, "UID " + uid + " gone, closing all sessions");
                for (java.lang.Integer sessionId : pkgSessions.mSessions) {
                    android.content.Intent closeIntent = new android.content.Intent("android.media.action.CLOSE_AUDIO_EFFECT_CONTROL_SESSION");
                    closeIntent.putExtra("android.media.extra.PACKAGE_NAME", pkgSessions.mPackageName);
                    closeIntent.putExtra("android.media.extra.AUDIO_SESSION", sessionId);
                    closeIntent.addFlags(32);
                    closeIntent.setPackage("com.android.musicfx");
                    this.mContext.sendBroadcastAsUser(closeIntent, android.os.UserHandle.ALL);
                }
                this.mClientUidSessionMap.remove(java.lang.Integer.valueOf(uid).intValue());
            }
        }
    }

    void handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case MSG_EFFECT_CLIENT_GONE /* 1101 */:
                android.util.Log.w(TAG, " handle MSG_EFFECT_CLIENT_GONE");
                handleEffectClientUidGone(msg.arg1);
                break;
            default:
                android.util.Log.e(TAG, "Unexpected msg to handle in MusicFxHelper: " + msg.what);
                break;
        }
    }
}
