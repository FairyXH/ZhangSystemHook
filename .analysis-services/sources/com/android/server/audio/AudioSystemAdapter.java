package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class AudioSystemAdapter implements android.media.AudioSystem.RoutingUpdateCallback, android.media.AudioSystem.VolumeRangeInitRequestCallback {
    private static final boolean DEBUG_CACHE = false;
    private static final boolean ENABLE_GETDEVICES_STATS = false;
    private static final int METHOD_GETDEVICESFORATTRIBUTES = 0;
    private static final int NB_MEASUREMENTS = 1;
    private static final java.lang.String TAG = "AudioSystemAdapter";
    private static final boolean USE_CACHE_FOR_GETDEVICES = true;
    private static com.android.server.audio.AudioSystemAdapter.OnRoutingUpdatedListener sRoutingListener;
    private static com.android.server.audio.AudioSystemAdapter sSingletonDefaultAdapter;
    private static com.android.server.audio.AudioSystemAdapter.OnVolRangeInitRequestListener sVolRangeInitReqListener;
    private java.util.concurrent.ConcurrentHashMap<android.util.Pair<android.media.AudioAttributes, java.lang.Boolean>, java.util.ArrayList<android.media.AudioDeviceAttributes>> mDevicesForAttrCache;
    private int[] mMethodCacheHit;
    private int[] mMethodCallCounter;
    private long[] mMethodTimeNs;
    private static final java.lang.Object sDeviceCacheLock = new java.lang.Object();
    private static final java.lang.Object sRoutingListenerLock = new java.lang.Object();
    private static final java.lang.Object sVolRangeInitReqListenerLock = new java.lang.Object();
    private static final boolean mSupportFm = android.os.SystemProperties.getBoolean("ro.oplus.audio.support.fm", false);
    private java.lang.String[] mMethodNames = {"getDevicesForAttributes"};
    private java.util.concurrent.ConcurrentHashMap<android.util.Pair<android.media.AudioAttributes, java.lang.Boolean>, java.util.ArrayList<android.media.AudioDeviceAttributes>> mLastDevicesForAttr = new java.util.concurrent.ConcurrentHashMap<>();
    private long mDevicesForAttributesCacheClearTimeMs = java.lang.System.currentTimeMillis();
    private final android.util.ArrayMap<android.os.IBinder, java.util.List<android.util.Pair<android.media.AudioAttributes, java.lang.Boolean>>> mRegisteredAttributesMap = new android.util.ArrayMap<>();
    private final android.os.RemoteCallbackList<android.media.IDevicesForAttributesCallback> mDevicesForAttributesCallbacks = new android.os.RemoteCallbackList<>();

    interface OnRoutingUpdatedListener {
        void onRoutingUpdatedFromNative();
    }

    interface OnVolRangeInitRequestListener {
        void onVolumeRangeInitRequestFromNative();
    }

    public void onRoutingUpdated() {
        com.android.server.audio.AudioSystemAdapter.OnRoutingUpdatedListener listener;
        invalidateRoutingCache();
        synchronized (sRoutingListenerLock) {
            listener = sRoutingListener;
        }
        if (listener != null) {
            listener.onRoutingUpdatedFromNative();
        }
        synchronized (this.mRegisteredAttributesMap) {
            int nbCallbacks = this.mDevicesForAttributesCallbacks.beginBroadcast();
            for (int i = 0; i < nbCallbacks; i++) {
                android.media.IDevicesForAttributesCallback cb = this.mDevicesForAttributesCallbacks.getBroadcastItem(i);
                java.util.List<android.util.Pair<android.media.AudioAttributes, java.lang.Boolean>> attrList = this.mRegisteredAttributesMap.get(cb.asBinder());
                if (attrList == null) {
                    throw new java.lang.IllegalStateException("Attribute list must not be null");
                }
                for (android.util.Pair<android.media.AudioAttributes, java.lang.Boolean> attr : attrList) {
                    java.util.ArrayList<android.media.AudioDeviceAttributes> devices = getDevicesForAttributes((android.media.AudioAttributes) attr.first, ((java.lang.Boolean) attr.second).booleanValue());
                    if (!this.mLastDevicesForAttr.containsKey(attr) || !sameDeviceList(devices, this.mLastDevicesForAttr.get(attr))) {
                        try {
                            cb.onDevicesForAttributesChanged((android.media.AudioAttributes) attr.first, ((java.lang.Boolean) attr.second).booleanValue(), devices);
                        } catch (android.os.RemoteException e) {
                        }
                    }
                }
            }
            this.mDevicesForAttributesCallbacks.finishBroadcast();
        }
    }

    static void setRoutingListener(com.android.server.audio.AudioSystemAdapter.OnRoutingUpdatedListener listener) {
        synchronized (sRoutingListenerLock) {
            sRoutingListener = listener;
        }
    }

    public void clearRoutingCache() {
        invalidateRoutingCache();
    }

    public void addOnDevicesForAttributesChangedListener(android.media.AudioAttributes attributes, boolean forVolume, android.media.IDevicesForAttributesCallback listener) {
        android.util.Pair<android.media.AudioAttributes, java.lang.Boolean> attr = new android.util.Pair<>(attributes, java.lang.Boolean.valueOf(forVolume));
        synchronized (this.mRegisteredAttributesMap) {
            java.util.List<android.util.Pair<android.media.AudioAttributes, java.lang.Boolean>> res = this.mRegisteredAttributesMap.get(listener.asBinder());
            if (res == null) {
                res = new java.util.ArrayList();
                this.mRegisteredAttributesMap.put(listener.asBinder(), res);
                this.mDevicesForAttributesCallbacks.register(listener);
            }
            if (!res.contains(attr)) {
                res.add(attr);
            }
        }
        getDevicesForAttributes(attributes, forVolume);
    }

    public void removeOnDevicesForAttributesChangedListener(android.media.IDevicesForAttributesCallback listener) {
        synchronized (this.mRegisteredAttributesMap) {
            if (!this.mRegisteredAttributesMap.containsKey(listener.asBinder())) {
                android.util.Log.w(TAG, "listener to be removed is not found.");
            } else {
                this.mRegisteredAttributesMap.remove(listener.asBinder());
                this.mDevicesForAttributesCallbacks.unregister(listener);
            }
        }
    }

    private boolean sameDeviceList(java.util.List<android.media.AudioDeviceAttributes> a, java.util.List<android.media.AudioDeviceAttributes> b) {
        for (android.media.AudioDeviceAttributes device : a) {
            if (!b.contains(device)) {
                return false;
            }
        }
        for (android.media.AudioDeviceAttributes device2 : b) {
            if (!a.contains(device2)) {
                return false;
            }
        }
        return true;
    }

    public void onVolumeRangeInitializationRequested() {
        com.android.server.audio.AudioSystemAdapter.OnVolRangeInitRequestListener listener;
        synchronized (sVolRangeInitReqListenerLock) {
            listener = sVolRangeInitReqListener;
        }
        if (listener != null) {
            listener.onVolumeRangeInitRequestFromNative();
        }
    }

    static void setVolRangeInitReqListener(com.android.server.audio.AudioSystemAdapter.OnVolRangeInitRequestListener listener) {
        synchronized (sVolRangeInitReqListenerLock) {
            sVolRangeInitReqListener = listener;
        }
    }

    static final synchronized com.android.server.audio.AudioSystemAdapter getDefaultAdapter() {
        if (sSingletonDefaultAdapter == null) {
            sSingletonDefaultAdapter = new com.android.server.audio.AudioSystemAdapter();
            android.media.AudioSystem.setRoutingCallback(sSingletonDefaultAdapter);
            android.media.AudioSystem.setVolumeRangeInitRequestCallback(sSingletonDefaultAdapter);
            synchronized (sDeviceCacheLock) {
                sSingletonDefaultAdapter.mDevicesForAttrCache = new java.util.concurrent.ConcurrentHashMap<>(android.media.AudioSystem.getNumStreamTypes());
                sSingletonDefaultAdapter.mMethodCacheHit = new int[1];
            }
        }
        return sSingletonDefaultAdapter;
    }

    private void invalidateRoutingCache() {
        synchronized (sDeviceCacheLock) {
            if (this.mDevicesForAttrCache != null) {
                this.mDevicesForAttributesCacheClearTimeMs = java.lang.System.currentTimeMillis();
                this.mLastDevicesForAttr.putAll(this.mDevicesForAttrCache);
                this.mDevicesForAttrCache.clear();
            }
        }
    }

    public java.util.ArrayList<android.media.AudioDeviceAttributes> getDevicesForAttributes(android.media.AudioAttributes attributes, boolean forVolume) {
        return getDevicesForAttributesImpl(attributes, forVolume);
    }

    private java.util.ArrayList<android.media.AudioDeviceAttributes> getDevicesForAttributesImpl(android.media.AudioAttributes attributes, boolean forVolume) {
        if (!mSupportFm) {
            android.util.Pair<android.media.AudioAttributes, java.lang.Boolean> key = new android.util.Pair<>(attributes, java.lang.Boolean.valueOf(forVolume));
            synchronized (sDeviceCacheLock) {
                java.util.ArrayList<android.media.AudioDeviceAttributes> res = this.mDevicesForAttrCache.get(key);
                if (res == null) {
                    java.util.ArrayList<android.media.AudioDeviceAttributes> res2 = android.media.AudioSystem.getDevicesForAttributes(attributes, forVolume);
                    this.mDevicesForAttrCache.put(key, res2);
                    return res2;
                }
                int[] iArr = this.mMethodCacheHit;
                iArr[0] = iArr[0] + 1;
                return res;
            }
        }
        return android.media.AudioSystem.getDevicesForAttributes(attributes, forVolume);
    }

    private static java.lang.String attrDeviceToDebugString(android.media.AudioAttributes attr, java.util.List<android.media.AudioDeviceAttributes> devices) {
        return " attrUsage=" + attr.getSystemUsage() + " " + android.media.AudioSystem.deviceSetToString(android.media.AudioSystem.generateAudioDeviceTypesSet(devices));
    }

    public int setDeviceConnectionState(android.media.AudioDeviceAttributes attributes, int state, int codecFormat) {
        invalidateRoutingCache();
        return android.media.AudioSystem.setDeviceConnectionState(attributes, state, codecFormat);
    }

    public int getDeviceConnectionState(int device, java.lang.String deviceAddress) {
        return android.media.AudioSystem.getDeviceConnectionState(device, deviceAddress);
    }

    public int handleDeviceConfigChange(int device, java.lang.String deviceAddress, java.lang.String deviceName, int codecFormat) {
        invalidateRoutingCache();
        return android.media.AudioSystem.handleDeviceConfigChange(device, deviceAddress, deviceName, codecFormat);
    }

    public int setDevicesRoleForStrategy(int strategy, int role, java.util.List<android.media.AudioDeviceAttributes> devices) {
        invalidateRoutingCache();
        return android.media.AudioSystem.setDevicesRoleForStrategy(strategy, role, devices);
    }

    public int removeDevicesRoleForStrategy(int strategy, int role, java.util.List<android.media.AudioDeviceAttributes> devices) {
        invalidateRoutingCache();
        return android.media.AudioSystem.removeDevicesRoleForStrategy(strategy, role, devices);
    }

    public int clearDevicesRoleForStrategy(int strategy, int role) {
        invalidateRoutingCache();
        return android.media.AudioSystem.clearDevicesRoleForStrategy(strategy, role);
    }

    public int setDevicesRoleForCapturePreset(int capturePreset, int role, java.util.List<android.media.AudioDeviceAttributes> devices) {
        invalidateRoutingCache();
        return android.media.AudioSystem.setDevicesRoleForCapturePreset(capturePreset, role, devices);
    }

    public int removeDevicesRoleForCapturePreset(int capturePreset, int role, java.util.List<android.media.AudioDeviceAttributes> devicesToRemove) {
        invalidateRoutingCache();
        return android.media.AudioSystem.removeDevicesRoleForCapturePreset(capturePreset, role, devicesToRemove);
    }

    public int addDevicesRoleForCapturePreset(int capturePreset, int role, java.util.List<android.media.AudioDeviceAttributes> devices) {
        invalidateRoutingCache();
        return android.media.AudioSystem.addDevicesRoleForCapturePreset(capturePreset, role, devices);
    }

    public int clearDevicesRoleForCapturePreset(int capturePreset, int role) {
        invalidateRoutingCache();
        return android.media.AudioSystem.clearDevicesRoleForCapturePreset(capturePreset, role);
    }

    public int setParameters(java.lang.String keyValuePairs) {
        invalidateRoutingCache();
        return android.media.AudioSystem.setParameters(keyValuePairs);
    }

    public boolean isMicrophoneMuted() {
        return android.media.AudioSystem.isMicrophoneMuted();
    }

    public int muteMicrophone(boolean on) {
        return android.media.AudioSystem.muteMicrophone(on);
    }

    public int setCurrentImeUid(int uid) {
        return android.media.AudioSystem.setCurrentImeUid(uid);
    }

    public boolean isStreamActive(int stream, int inPastMs) {
        return android.media.AudioSystem.isStreamActive(stream, inPastMs);
    }

    public boolean isStreamActiveRemotely(int stream, int inPastMs) {
        return android.media.AudioSystem.isStreamActiveRemotely(stream, inPastMs);
    }

    public int setStreamVolumeIndexAS(int stream, int index, int device) {
        return android.media.AudioSystem.setStreamVolumeIndexAS(stream, index, device);
    }

    public int setVolumeIndexForAttributes(android.media.AudioAttributes attributes, int index, int device) {
        return android.media.AudioSystem.setVolumeIndexForAttributes(attributes, index, device);
    }

    public int setPhoneState(int state, int uid) {
        invalidateRoutingCache();
        return android.media.AudioSystem.setPhoneState(state, uid);
    }

    public int setAllowedCapturePolicy(int uid, int flags) {
        return android.media.AudioSystem.setAllowedCapturePolicy(uid, flags);
    }

    public int setForceUse(int usage, int config) {
        invalidateRoutingCache();
        return android.media.AudioSystem.setForceUse(usage, config);
    }

    public int getForceUse(int usage) {
        return android.media.AudioSystem.getForceUse(usage);
    }

    public int setDeviceAbsoluteVolumeEnabled(int nativeDeviceType, java.lang.String address, boolean enabled, int streamToDriveAbs) {
        return android.media.AudioSystem.setDeviceAbsoluteVolumeEnabled(nativeDeviceType, address, enabled, streamToDriveAbs);
    }

    public int registerPolicyMixes(java.util.ArrayList<android.media.audiopolicy.AudioMix> mixes, boolean register) {
        invalidateRoutingCache();
        return android.media.AudioSystem.registerPolicyMixes(mixes, register);
    }

    public java.util.List<android.media.audiopolicy.AudioMix> getRegisteredPolicyMixes() {
        if (!android.media.audiopolicy.Flags.audioMixTestApi()) {
            return java.util.Collections.emptyList();
        }
        java.util.List<android.media.audiopolicy.AudioMix> audioMixes = new java.util.ArrayList<>();
        int result = android.media.AudioSystem.getRegisteredPolicyMixes(audioMixes);
        if (result != 0) {
            throw new java.lang.IllegalStateException("Cannot fetch registered policy mixes. Result: " + result);
        }
        return java.util.Collections.unmodifiableList(audioMixes);
    }

    public int updateMixingRules(android.media.audiopolicy.AudioMix[] mixes, android.media.audiopolicy.AudioMixingRule[] updatedMixingRules) {
        invalidateRoutingCache();
        return android.media.AudioSystem.updatePolicyMixes(mixes, updatedMixingRules);
    }

    public int setUidDeviceAffinities(int uid, int[] types, java.lang.String[] addresses) {
        invalidateRoutingCache();
        return android.media.AudioSystem.setUidDeviceAffinities(uid, types, addresses);
    }

    public int removeUidDeviceAffinities(int uid) {
        invalidateRoutingCache();
        return android.media.AudioSystem.removeUidDeviceAffinities(uid);
    }

    public int setUserIdDeviceAffinities(int userId, int[] types, java.lang.String[] addresses) {
        invalidateRoutingCache();
        return android.media.AudioSystem.setUserIdDeviceAffinities(userId, types, addresses);
    }

    public int removeUserIdDeviceAffinities(int userId) {
        invalidateRoutingCache();
        return android.media.AudioSystem.removeUserIdDeviceAffinities(userId);
    }

    public android.media.ISoundDose getSoundDoseInterface(android.media.ISoundDoseCallback callback) {
        return android.media.AudioSystem.getSoundDoseInterface(callback);
    }

    public int setPreferredMixerAttributes(android.media.AudioAttributes attributes, int portId, int uid, android.media.AudioMixerAttributes mixerAttributes) {
        return android.media.AudioSystem.setPreferredMixerAttributes(attributes, portId, uid, mixerAttributes);
    }

    public int clearPreferredMixerAttributes(android.media.AudioAttributes attributes, int portId, int uid) {
        return android.media.AudioSystem.clearPreferredMixerAttributes(attributes, portId, uid);
    }

    public int setMasterMute(boolean mute) {
        return android.media.AudioSystem.setMasterMute(mute);
    }

    public void dump(java.io.PrintWriter pw) {
        pw.println("\nAudioSystemAdapter:");
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("MM-dd HH:mm:ss:SSS").withLocale(java.util.Locale.US).withZone(java.time.ZoneId.systemDefault());
        synchronized (sDeviceCacheLock) {
            pw.println(" last cache clear time: " + formatter.format(java.time.Instant.ofEpochMilli(this.mDevicesForAttributesCacheClearTimeMs)));
            pw.println(" mDevicesForAttrCache:");
            if (this.mDevicesForAttrCache != null) {
                for (java.util.Map.Entry<android.util.Pair<android.media.AudioAttributes, java.lang.Boolean>, java.util.ArrayList<android.media.AudioDeviceAttributes>> entry : this.mDevicesForAttrCache.entrySet()) {
                    android.media.AudioAttributes attributes = (android.media.AudioAttributes) entry.getKey().first;
                    try {
                        int stream = attributes.getVolumeControlStream();
                        pw.println("\t" + attributes + " forVolume: " + entry.getKey().second + " stream: " + android.media.AudioSystem.STREAM_NAMES[stream] + "(" + stream + ")");
                        for (android.media.AudioDeviceAttributes devAttr : entry.getValue()) {
                            pw.println("\t\t" + devAttr);
                        }
                    } catch (java.lang.IllegalArgumentException e) {
                        pw.println("\t dump failed for attributes: " + attributes);
                        android.util.Log.e(TAG, "dump failed", e);
                    }
                }
            }
        }
    }
}
