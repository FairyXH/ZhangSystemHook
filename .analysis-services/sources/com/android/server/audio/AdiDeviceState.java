package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public final class AdiDeviceState {
    private static final java.lang.String SETTING_FIELD_SEPARATOR = ",";
    private static final java.lang.String TAG = "AS.AdiDeviceState";
    private final java.lang.String mDeviceAddress;
    private final android.util.Pair<java.lang.Integer, java.lang.String> mDeviceId;
    private final int mDeviceType;
    private boolean mHeadTrackerEnabled;
    private final int mInternalDeviceType;
    private boolean mSAEnabled;
    private int mAudioDeviceCategory = 0;
    private boolean mAutoBtCategorySet = false;
    private boolean mHasHeadTracker = false;
    private boolean mUserEnabled = false;

    AdiDeviceState(int deviceType, int internalDeviceType, java.lang.String address) {
        this.mDeviceType = deviceType;
        if (internalDeviceType != 0) {
            this.mInternalDeviceType = internalDeviceType;
        } else {
            this.mInternalDeviceType = android.media.AudioDeviceInfo.convertDeviceTypeToInternalDevice(deviceType);
        }
        this.mDeviceAddress = android.media.AudioSystem.isBluetoothDevice(this.mInternalDeviceType) ? (java.lang.String) java.util.Objects.requireNonNull(address) : "";
        this.mDeviceId = new android.util.Pair<>(java.lang.Integer.valueOf(this.mInternalDeviceType), this.mDeviceAddress);
    }

    public synchronized android.util.Pair<java.lang.Integer, java.lang.String> getDeviceId() {
        return this.mDeviceId;
    }

    public synchronized int getDeviceType() {
        return this.mDeviceType;
    }

    public synchronized int getInternalDeviceType() {
        return this.mInternalDeviceType;
    }

    public synchronized java.lang.String getDeviceAddress() {
        return this.mDeviceAddress;
    }

    public synchronized void setSAEnabled(boolean sAEnabled) {
        this.mSAEnabled = sAEnabled;
    }

    public synchronized boolean isSAEnabled() {
        return this.mSAEnabled;
    }

    public synchronized void setHeadTrackerEnabled(boolean headTrackerEnabled) {
        this.mHeadTrackerEnabled = headTrackerEnabled;
    }

    public synchronized boolean isHeadTrackerEnabled() {
        return this.mHeadTrackerEnabled;
    }

    public synchronized void setHasHeadTracker(boolean hasHeadTracker) {
        this.mHasHeadTracker = hasHeadTracker;
    }

    public synchronized boolean hasHeadTracker() {
        return this.mHasHeadTracker;
    }

    public synchronized void setUserEnabled(boolean userEnable) {
        this.mUserEnabled = userEnable;
    }

    public synchronized boolean isUserEnabled() {
        return this.mUserEnabled;
    }

    public synchronized int getAudioDeviceCategory() {
        return this.mAudioDeviceCategory;
    }

    public synchronized void setAudioDeviceCategory(int audioDeviceCategory) {
        this.mAudioDeviceCategory = audioDeviceCategory;
    }

    public synchronized boolean isBtDeviceCategoryFixed() {
        if (!android.media.audio.Flags.automaticBtDeviceType()) {
            return false;
        }
        updateAudioDeviceCategory();
        return this.mAutoBtCategorySet;
    }

    public synchronized boolean updateAudioDeviceCategory() {
        if (!android.media.audio.Flags.automaticBtDeviceType()) {
            return false;
        }
        if (!android.media.AudioSystem.isBluetoothDevice(this.mInternalDeviceType)) {
            return false;
        }
        if (this.mAutoBtCategorySet) {
            return false;
        }
        int newAudioDeviceCategory = com.android.server.audio.BtHelper.getBtDeviceCategory(this.mDeviceAddress);
        if (newAudioDeviceCategory == 0) {
            return false;
        }
        this.mAudioDeviceCategory = newAudioDeviceCategory;
        this.mAutoBtCategorySet = true;
        return true;
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        com.android.server.audio.AdiDeviceState sads = (com.android.server.audio.AdiDeviceState) obj;
        if (this.mDeviceType == sads.mDeviceType && this.mInternalDeviceType == sads.mInternalDeviceType && this.mDeviceAddress.equals(sads.mDeviceAddress) && this.mSAEnabled == sads.mSAEnabled && this.mHasHeadTracker == sads.mHasHeadTracker && this.mHeadTrackerEnabled == sads.mHeadTrackerEnabled && this.mAudioDeviceCategory == sads.mAudioDeviceCategory && this.mUserEnabled == sads.mUserEnabled) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return java.util.Objects.hash(java.lang.Integer.valueOf(this.mDeviceType), java.lang.Integer.valueOf(this.mInternalDeviceType), this.mDeviceAddress, java.lang.Boolean.valueOf(this.mSAEnabled), java.lang.Boolean.valueOf(this.mHasHeadTracker), java.lang.Boolean.valueOf(this.mHeadTrackerEnabled), java.lang.Integer.valueOf(this.mAudioDeviceCategory), java.lang.Boolean.valueOf(this.mUserEnabled));
    }

    public java.lang.String toString() {
        return "type: " + this.mDeviceType + " internal type: 0x" + java.lang.Integer.toHexString(this.mInternalDeviceType) + " addr: " + android.media.Utils.anonymizeBluetoothAddress(this.mInternalDeviceType, this.mDeviceAddress) + " bt audio type: " + android.media.AudioManager.audioDeviceCategoryToString(this.mAudioDeviceCategory) + " enabled: " + this.mSAEnabled + " HT: " + this.mHasHeadTracker + " HTenabled: " + this.mHeadTrackerEnabled + " userEnabled: " + this.mUserEnabled;
    }

    public synchronized java.lang.String toPersistableString() {
        return this.mDeviceType + SETTING_FIELD_SEPARATOR + this.mDeviceAddress + SETTING_FIELD_SEPARATOR + (this.mSAEnabled ? "1" : "0") + SETTING_FIELD_SEPARATOR + (this.mHasHeadTracker ? "1" : "0") + SETTING_FIELD_SEPARATOR + (this.mHeadTrackerEnabled ? "1" : "0") + SETTING_FIELD_SEPARATOR + this.mInternalDeviceType + SETTING_FIELD_SEPARATOR + this.mAudioDeviceCategory + SETTING_FIELD_SEPARATOR + (this.mUserEnabled ? "1" : "0");
    }

    public static int getPeristedMaxSize() {
        return 39;
    }

    public static com.android.server.audio.AdiDeviceState fromPersistedString(java.lang.String persistedString) {
        if (persistedString == null || persistedString.isEmpty()) {
            return null;
        }
        java.lang.String[] fields = android.text.TextUtils.split(persistedString, SETTING_FIELD_SEPARATOR);
        if (fields.length < 5 || fields.length > 8) {
            return null;
        }
        try {
            int deviceType = java.lang.Integer.parseInt(fields[0]);
            int internalDeviceType = -1;
            if (fields.length >= 6) {
                internalDeviceType = java.lang.Integer.parseInt(fields[5]);
            }
            int audioDeviceCategory = 0;
            if (fields.length == 7) {
                audioDeviceCategory = java.lang.Integer.parseInt(fields[6]);
            }
            boolean userEnabled = false;
            if (fields.length > 7) {
                userEnabled = java.lang.Integer.parseInt(fields[7]) == 1;
            }
            com.android.server.audio.AdiDeviceState deviceState = new com.android.server.audio.AdiDeviceState(deviceType, internalDeviceType, fields[1]);
            deviceState.setSAEnabled(java.lang.Integer.parseInt(fields[2]) == 1);
            deviceState.setHasHeadTracker(java.lang.Integer.parseInt(fields[3]) == 1);
            deviceState.setHeadTrackerEnabled(java.lang.Integer.parseInt(fields[4]) == 1);
            deviceState.setAudioDeviceCategory(audioDeviceCategory);
            deviceState.updateAudioDeviceCategory();
            deviceState.setUserEnabled(userEnabled);
            return deviceState;
        } catch (java.lang.NumberFormatException e) {
            android.util.Log.e(TAG, "unable to parse setting for AdiDeviceState: " + persistedString, e);
            return null;
        }
    }

    public synchronized android.media.AudioDeviceAttributes getAudioDeviceAttributes() {
        return new android.media.AudioDeviceAttributes(2, this.mDeviceType, this.mDeviceAddress);
    }
}
