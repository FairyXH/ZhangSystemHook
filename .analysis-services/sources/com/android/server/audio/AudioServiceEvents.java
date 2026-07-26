package com.android.server.audio;

/* JADX INFO: loaded from: classes.dex */
public class AudioServiceEvents {

    static final class PhoneStateEvent extends com.android.server.utils.EventLogger.Event {
        static final int MODE_IN_COMMUNICATION_TIMEOUT = 1;
        static final int MODE_SET = 0;
        private static final java.lang.String mMetricsId = "audio.mode";
        final int mActualMode;
        final int mOp;
        final int mOwnerPid;
        final java.lang.String mPackage;
        final int mRequestedMode;
        final int mRequesterPid;

        PhoneStateEvent(java.lang.String callingPackage, int requesterPid, int requestedMode, int ownerPid, int actualMode) {
            this.mOp = 0;
            this.mPackage = callingPackage;
            this.mRequesterPid = requesterPid;
            this.mRequestedMode = requestedMode;
            this.mOwnerPid = ownerPid;
            this.mActualMode = actualMode;
            logMetricEvent();
        }

        PhoneStateEvent(java.lang.String callingPackage, int ownerPid) {
            this.mOp = 1;
            this.mPackage = callingPackage;
            this.mOwnerPid = ownerPid;
            this.mRequesterPid = 0;
            this.mRequestedMode = 0;
            this.mActualMode = 0;
            logMetricEvent();
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            switch (this.mOp) {
                case 0:
                    return "setMode(" + android.media.AudioSystem.modeToString(this.mRequestedMode) + ") from package=" + this.mPackage + " pid=" + this.mRequesterPid + " selected mode=" + android.media.AudioSystem.modeToString(this.mActualMode) + " by pid=" + this.mOwnerPid;
                case 1:
                    return "mode IN COMMUNICATION timeout for package=" + this.mPackage + " pid=" + this.mOwnerPid;
                default:
                    return "FIXME invalid op:" + this.mOp;
            }
        }

        private void logMetricEvent() {
            switch (this.mOp) {
                case 0:
                    new android.media.MediaMetrics.Item(mMetricsId).set(android.media.MediaMetrics.Property.EVENT, "set").set(android.media.MediaMetrics.Property.REQUESTED_MODE, android.media.AudioSystem.modeToString(this.mRequestedMode)).set(android.media.MediaMetrics.Property.MODE, android.media.AudioSystem.modeToString(this.mActualMode)).set(android.media.MediaMetrics.Property.CALLING_PACKAGE, this.mPackage).record();
                    break;
                case 1:
                    new android.media.MediaMetrics.Item(mMetricsId).set(android.media.MediaMetrics.Property.EVENT, "inCommunicationTimeout").set(android.media.MediaMetrics.Property.CALLING_PACKAGE, this.mPackage).record();
                    break;
            }
        }
    }

    static final class WiredDevConnectEvent extends com.android.server.utils.EventLogger.Event {
        final com.android.server.audio.AudioDeviceInventory.WiredDeviceConnectionState mState;

        WiredDevConnectEvent(com.android.server.audio.AudioDeviceInventory.WiredDeviceConnectionState state) {
            this.mState = state;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            return "setWiredDeviceConnectionState( type:" + java.lang.Integer.toHexString(this.mState.mAttributes.getInternalType()) + " (" + (android.media.AudioSystem.isInputDevice(this.mState.mAttributes.getInternalType()) ? "source" : "sink") + ")  state:" + android.media.AudioSystem.deviceStateToString(this.mState.mState) + " addr:" + this.mState.mAttributes.getAddress() + " name:" + this.mState.mAttributes.getName() + ") from " + this.mState.mCaller;
        }
    }

    static final class ForceUseEvent extends com.android.server.utils.EventLogger.Event {
        final int mConfig;
        final java.lang.String mReason;
        final int mUsage;

        ForceUseEvent(int usage, int config, java.lang.String reason) {
            this.mUsage = usage;
            this.mConfig = config;
            this.mReason = reason;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            return "setForceUse(" + android.media.AudioSystem.forceUseUsageToString(this.mUsage) + ", " + android.media.AudioSystem.forceUseConfigToString(this.mConfig) + ") due to " + this.mReason;
        }
    }

    static final class VolChangedBroadcastEvent extends com.android.server.utils.EventLogger.Event {
        final java.lang.String mAliasStreamIndexes;
        final int mIndex;
        final int mOldIndex;
        final int mStreamType;

        VolChangedBroadcastEvent(int stream, java.lang.String aliasIndexes, int index, int oldIndex) {
            this.mStreamType = stream;
            this.mAliasStreamIndexes = aliasIndexes;
            this.mIndex = index;
            this.mOldIndex = oldIndex;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            return "sending VOLUME_CHANGED stream:" + android.media.AudioSystem.streamToString(this.mStreamType) + " index:" + this.mIndex + " (was:" + this.mOldIndex + ")" + this.mAliasStreamIndexes;
        }
    }

    static final class DeviceVolumeEvent extends com.android.server.utils.EventLogger.Event {
        final java.lang.String mCaller;
        final java.lang.String mDeviceAddress;
        final int mDeviceForStream;
        final java.lang.String mDeviceNativeType;
        final boolean mSkipped;
        final int mStream;
        final int mVolIndex;

        DeviceVolumeEvent(int streamType, int index, android.media.AudioDeviceAttributes device, int deviceForStream, java.lang.String callingPackage, boolean skipped) {
            this.mStream = streamType;
            this.mVolIndex = index;
            this.mDeviceNativeType = "0x" + java.lang.Integer.toHexString(device.getInternalType());
            this.mDeviceAddress = device.getAddress();
            this.mDeviceForStream = deviceForStream;
            this.mCaller = callingPackage;
            this.mSkipped = skipped;
            new android.media.MediaMetrics.Item("audio.volume.event").set(android.media.MediaMetrics.Property.EVENT, "setDeviceVolume").set(android.media.MediaMetrics.Property.STREAM_TYPE, android.media.AudioSystem.streamToString(this.mStream)).set(android.media.MediaMetrics.Property.INDEX, java.lang.Integer.valueOf(this.mVolIndex)).set(android.media.MediaMetrics.Property.DEVICE, this.mDeviceNativeType).set(android.media.MediaMetrics.Property.ADDRESS, this.mDeviceAddress).set(android.media.MediaMetrics.Property.CALLING_PACKAGE, this.mCaller).record();
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder("setDeviceVolume(stream:").append(android.media.AudioSystem.streamToString(this.mStream)).append(" index:").append(this.mVolIndex).append(" device:").append(this.mDeviceNativeType).append(" addr:").append(this.mDeviceAddress).append(") from ").append(this.mCaller);
            if (this.mSkipped) {
                sb.append(" skipped [device in use]");
            } else {
                sb.append(" currDevForStream:Ox").append(java.lang.Integer.toHexString(this.mDeviceForStream));
            }
            return sb.toString();
        }
    }

    static final class VolumeEvent extends com.android.server.utils.EventLogger.Event {
        static final int VOL_ADJUST_GROUP_VOL = 11;
        static final int VOL_ADJUST_STREAM_VOL = 1;
        static final int VOL_ADJUST_SUGG_VOL = 0;
        static final int VOL_ADJUST_VOL_UID = 5;
        static final int VOL_MASTER_MUTE = 12;
        static final int VOL_MODE_CHANGE_HEARING_AID = 7;
        static final int VOL_MUTE_STREAM_INT = 9;
        static final int VOL_SET_AVRCP_VOL = 4;
        static final int VOL_SET_GROUP_VOL = 8;
        static final int VOL_SET_HEARING_AID_VOL = 3;
        static final int VOL_SET_LE_AUDIO_VOL = 10;
        static final int VOL_SET_STREAM_VOL = 2;
        static final int VOL_VOICE_ACTIVITY_HEARING_AID = 6;
        private static final java.lang.String mMetricsId = "audio.volume.event";
        final java.lang.String mCaller;
        final java.lang.String mGroupName;
        final int mOp;
        final int mStream;
        final int mVal1;
        final int mVal2;
        final int mVal3;

        VolumeEvent(int op, int stream, int val1, int val2, int val3, java.lang.String caller) {
            this.mOp = op;
            this.mStream = stream;
            this.mVal1 = val1;
            this.mVal2 = val2;
            this.mVal3 = val3;
            this.mCaller = caller;
            this.mGroupName = null;
            logMetricEvent();
        }

        VolumeEvent(int op, int stream, int val1, int val2, java.lang.String caller) {
            this.mOp = op;
            this.mStream = stream;
            this.mVal1 = val1;
            this.mVal2 = val2;
            this.mCaller = caller;
            this.mVal3 = -1;
            this.mGroupName = null;
            logMetricEvent();
        }

        VolumeEvent(int op, int index, int gainDb) {
            this.mOp = op;
            this.mVal1 = index;
            this.mVal2 = gainDb;
            this.mVal3 = -1;
            this.mStream = -1;
            this.mCaller = null;
            this.mGroupName = null;
            logMetricEvent();
        }

        VolumeEvent(int op, int index) {
            this.mOp = op;
            this.mVal1 = index;
            this.mVal2 = 0;
            this.mVal3 = -1;
            this.mStream = -1;
            this.mCaller = null;
            this.mGroupName = null;
            logMetricEvent();
        }

        VolumeEvent(int i, boolean z, int i2, int i3) {
            this.mOp = i;
            this.mStream = i2;
            this.mVal1 = i3;
            this.mVal2 = z ? 1 : 0;
            this.mVal3 = -1;
            this.mCaller = null;
            this.mGroupName = null;
            logMetricEvent();
        }

        VolumeEvent(int op, int mode, int stream, int index) {
            this.mOp = op;
            this.mStream = stream;
            this.mVal1 = index;
            this.mVal2 = mode;
            this.mVal3 = -1;
            this.mCaller = null;
            this.mGroupName = null;
            logMetricEvent();
        }

        VolumeEvent(int op, java.lang.String group, int index, int flags, java.lang.String caller) {
            this.mOp = op;
            this.mStream = -1;
            this.mVal1 = index;
            this.mVal2 = flags;
            this.mCaller = caller;
            this.mGroupName = group;
            this.mVal3 = -1;
            logMetricEvent();
        }

        VolumeEvent(int i, int i2, boolean z) {
            this.mOp = i;
            this.mStream = i2;
            this.mVal1 = z ? 1 : 0;
            this.mVal2 = 0;
            this.mCaller = null;
            this.mGroupName = null;
            this.mVal3 = -1;
            logMetricEvent();
        }

        VolumeEvent(int i, boolean z) {
            this.mOp = i;
            this.mStream = -1;
            this.mVal1 = z ? 1 : 0;
            this.mVal2 = 0;
            this.mVal3 = -1;
            this.mCaller = null;
            this.mGroupName = null;
            logMetricEvent();
        }

        private void logMetricEvent() {
            java.lang.String eventName;
            int i = this.mOp;
            java.lang.String str = android.net.INetd.IF_STATE_UP;
            switch (i) {
                case 0:
                case 1:
                case 5:
                    switch (this.mOp) {
                        case 0:
                            eventName = "adjustSuggestedStreamVolume";
                            break;
                        case 1:
                            eventName = "adjustStreamVolume";
                            break;
                        case 5:
                            eventName = "adjustStreamVolumeForUid";
                            break;
                    }
                    android.media.MediaMetrics.Item item = new android.media.MediaMetrics.Item(mMetricsId).set(android.media.MediaMetrics.Property.CALLING_PACKAGE, this.mCaller);
                    android.media.MediaMetrics.Key key = android.media.MediaMetrics.Property.DIRECTION;
                    if (this.mVal1 <= 0) {
                        str = android.net.INetd.IF_STATE_DOWN;
                    }
                    item.set(key, str).set(android.media.MediaMetrics.Property.EVENT, eventName).set(android.media.MediaMetrics.Property.FLAGS, java.lang.Integer.valueOf(this.mVal2)).set(android.media.MediaMetrics.Property.STREAM_TYPE, android.media.AudioSystem.streamToString(this.mStream)).record();
                    break;
                case 2:
                    new android.media.MediaMetrics.Item(mMetricsId).set(android.media.MediaMetrics.Property.CALLING_PACKAGE, this.mCaller).set(android.media.MediaMetrics.Property.EVENT, "setStreamVolume").set(android.media.MediaMetrics.Property.FLAGS, java.lang.Integer.valueOf(this.mVal2)).set(android.media.MediaMetrics.Property.INDEX, java.lang.Integer.valueOf(this.mVal1)).set(android.media.MediaMetrics.Property.OLD_INDEX, java.lang.Integer.valueOf(this.mVal3)).set(android.media.MediaMetrics.Property.STREAM_TYPE, android.media.AudioSystem.streamToString(this.mStream)).record();
                    break;
                case 3:
                    new android.media.MediaMetrics.Item(mMetricsId).set(android.media.MediaMetrics.Property.EVENT, "setHearingAidVolume").set(android.media.MediaMetrics.Property.GAIN_DB, java.lang.Double.valueOf(this.mVal2)).set(android.media.MediaMetrics.Property.INDEX, java.lang.Integer.valueOf(this.mVal1)).record();
                    break;
                case 4:
                    new android.media.MediaMetrics.Item(mMetricsId).set(android.media.MediaMetrics.Property.EVENT, "setAvrcpVolume").set(android.media.MediaMetrics.Property.INDEX, java.lang.Integer.valueOf(this.mVal1)).record();
                    break;
                case 6:
                    new android.media.MediaMetrics.Item(mMetricsId).set(android.media.MediaMetrics.Property.EVENT, "voiceActivityHearingAid").set(android.media.MediaMetrics.Property.INDEX, java.lang.Integer.valueOf(this.mVal1)).set(android.media.MediaMetrics.Property.STATE, this.mVal2 == 1 ? com.android.server.pm.verify.domain.DomainVerificationPersistence.TAG_ACTIVE : "inactive").set(android.media.MediaMetrics.Property.STREAM_TYPE, android.media.AudioSystem.streamToString(this.mStream)).record();
                    break;
                case 7:
                    new android.media.MediaMetrics.Item(mMetricsId).set(android.media.MediaMetrics.Property.EVENT, "modeChangeHearingAid").set(android.media.MediaMetrics.Property.INDEX, java.lang.Integer.valueOf(this.mVal1)).set(android.media.MediaMetrics.Property.MODE, android.media.AudioSystem.modeToString(this.mVal2)).set(android.media.MediaMetrics.Property.STREAM_TYPE, android.media.AudioSystem.streamToString(this.mStream)).record();
                    break;
                case 8:
                    new android.media.MediaMetrics.Item(mMetricsId).set(android.media.MediaMetrics.Property.CALLING_PACKAGE, this.mCaller).set(android.media.MediaMetrics.Property.EVENT, "setVolumeIndexForAttributes").set(android.media.MediaMetrics.Property.FLAGS, java.lang.Integer.valueOf(this.mVal2)).set(android.media.MediaMetrics.Property.GROUP, this.mGroupName).set(android.media.MediaMetrics.Property.INDEX, java.lang.Integer.valueOf(this.mVal1)).record();
                    break;
                case 10:
                    new android.media.MediaMetrics.Item(mMetricsId).set(android.media.MediaMetrics.Property.EVENT, "setLeAudioVolume").set(android.media.MediaMetrics.Property.INDEX, java.lang.Integer.valueOf(this.mVal1)).set(android.media.MediaMetrics.Property.MAX_INDEX, java.lang.Integer.valueOf(this.mVal2)).set(android.media.MediaMetrics.Property.STREAM_TYPE, android.media.AudioSystem.streamToString(this.mStream)).record();
                    break;
                case 11:
                    android.media.MediaMetrics.Item item2 = new android.media.MediaMetrics.Item(mMetricsId).set(android.media.MediaMetrics.Property.CALLING_PACKAGE, this.mCaller);
                    android.media.MediaMetrics.Key key2 = android.media.MediaMetrics.Property.DIRECTION;
                    if (this.mVal1 <= 0) {
                        str = android.net.INetd.IF_STATE_DOWN;
                    }
                    item2.set(key2, str).set(android.media.MediaMetrics.Property.EVENT, "adjustVolumeGroupVolume").set(android.media.MediaMetrics.Property.FLAGS, java.lang.Integer.valueOf(this.mVal2)).set(android.media.MediaMetrics.Property.GROUP, this.mGroupName).record();
                    break;
            }
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            switch (this.mOp) {
                case 0:
                    return "adjustSuggestedStreamVolume(sugg:" + android.media.AudioSystem.streamToString(this.mStream) + " dir:" + android.media.AudioManager.adjustToString(this.mVal1) + " flags:0x" + java.lang.Integer.toHexString(this.mVal2) + ") from " + this.mCaller;
                case 1:
                    return "adjustStreamVolume(stream:" + android.media.AudioSystem.streamToString(this.mStream) + " dir:" + android.media.AudioManager.adjustToString(this.mVal1) + " flags:0x" + java.lang.Integer.toHexString(this.mVal2) + ") from " + this.mCaller;
                case 2:
                    return "setStreamVolume(stream:" + android.media.AudioSystem.streamToString(this.mStream) + " index:" + this.mVal1 + " flags:0x" + java.lang.Integer.toHexString(this.mVal2) + " oldIndex:" + this.mVal3 + ") from " + this.mCaller;
                case 3:
                    return "setHearingAidVolume: index:" + this.mVal1 + " gain dB:" + this.mVal2;
                case 4:
                    return "setAvrcpVolume: index:" + this.mVal1;
                case 5:
                    return "adjustStreamVolumeForUid(stream:" + android.media.AudioSystem.streamToString(this.mStream) + " dir:" + android.media.AudioManager.adjustToString(this.mVal1) + " flags:0x" + java.lang.Integer.toHexString(this.mVal2) + ") from " + this.mCaller;
                case 6:
                    return "Voice activity change (" + (this.mVal2 == 1 ? com.android.server.pm.verify.domain.DomainVerificationPersistence.TAG_ACTIVE : "inactive") + ") causes setting HEARING_AID volume to idx:" + this.mVal1 + " stream:" + android.media.AudioSystem.streamToString(this.mStream);
                case 7:
                    return "setMode(" + android.media.AudioSystem.modeToString(this.mVal2) + ") causes setting HEARING_AID volume to idx:" + this.mVal1 + " stream:" + android.media.AudioSystem.streamToString(this.mStream);
                case 8:
                    return "setVolumeIndexForAttributes(group: group: " + this.mGroupName + " index:" + this.mVal1 + " flags:0x" + java.lang.Integer.toHexString(this.mVal2) + ") from " + this.mCaller;
                case 9:
                    return "VolumeStreamState.muteInternally(stream:" + android.media.AudioSystem.streamToString(this.mStream) + (this.mVal1 == 1 ? ", muted)" : ", unmuted)");
                case 10:
                    return "setLeAudioVolume(stream:" + android.media.AudioSystem.streamToString(this.mStream) + " index:" + this.mVal1 + " maxIndex:" + this.mVal2;
                case 11:
                    return "adjustVolumeGroupVolume(group:" + this.mGroupName + " dir:" + android.media.AudioManager.adjustToString(this.mVal1) + " flags:0x" + java.lang.Integer.toHexString(this.mVal2) + ") from " + this.mCaller;
                case 12:
                    return "Master mute:" + (this.mVal1 == 1 ? " muted)" : " unmuted)");
                default:
                    return "FIXME invalid op:" + this.mOp;
            }
        }
    }

    static final class SoundDoseEvent extends com.android.server.utils.EventLogger.Event {
        static final int DOSE_ACCUMULATION_START = 3;
        static final int DOSE_REPEAT_5X = 2;
        static final int DOSE_UPDATE = 1;
        static final int LOWER_VOLUME_TO_RS1 = 4;
        static final int MOMENTARY_EXPOSURE = 0;
        final int mEventType;
        final float mFloatValue;
        final long mLongValue;

        private SoundDoseEvent(int event, float f, long l) {
            this.mEventType = event;
            this.mFloatValue = f;
            this.mLongValue = l;
        }

        static com.android.server.audio.AudioServiceEvents.SoundDoseEvent getMomentaryExposureEvent(float mel) {
            return new com.android.server.audio.AudioServiceEvents.SoundDoseEvent(0, mel, 0L);
        }

        static com.android.server.audio.AudioServiceEvents.SoundDoseEvent getDoseUpdateEvent(float csd, long totalDuration) {
            return new com.android.server.audio.AudioServiceEvents.SoundDoseEvent(1, csd, totalDuration);
        }

        static com.android.server.audio.AudioServiceEvents.SoundDoseEvent getDoseRepeat5xEvent() {
            return new com.android.server.audio.AudioServiceEvents.SoundDoseEvent(2, 0.0f, 0L);
        }

        static com.android.server.audio.AudioServiceEvents.SoundDoseEvent getDoseAccumulationStartEvent() {
            return new com.android.server.audio.AudioServiceEvents.SoundDoseEvent(3, 0.0f, 0L);
        }

        static com.android.server.audio.AudioServiceEvents.SoundDoseEvent getLowerVolumeToRs1Event() {
            return new com.android.server.audio.AudioServiceEvents.SoundDoseEvent(4, 0.0f, 0L);
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            switch (this.mEventType) {
                case 0:
                    return java.lang.String.format("momentary exposure MEL=%.2f", java.lang.Float.valueOf(this.mFloatValue));
                case 1:
                    return java.lang.String.format(java.util.Locale.US, "dose update CSD=%.1f%% total duration=%d", java.lang.Float.valueOf(this.mFloatValue * 100.0f), java.lang.Long.valueOf(this.mLongValue));
                case 2:
                    return "CSD reached 500%";
                case 3:
                    return "CSD accumulating: RS2 entered";
                case 4:
                    return "CSD lowering volume to RS1";
                default:
                    return "FIXME invalid event type:" + this.mEventType;
            }
        }
    }

    static final class LoudnessEvent extends com.android.server.utils.EventLogger.Event {
        static final int CLIENT_DIED = 2;
        static final int START_PIID = 0;
        static final int STOP_PIID = 1;
        final int mEventType;
        final int mIntValue1;
        final int mIntValue2;

        private LoudnessEvent(int event, int i1, int i2) {
            this.mEventType = event;
            this.mIntValue1 = i1;
            this.mIntValue2 = i2;
        }

        static com.android.server.audio.AudioServiceEvents.LoudnessEvent getStartPiid(int piid, int pid) {
            return new com.android.server.audio.AudioServiceEvents.LoudnessEvent(0, piid, pid);
        }

        static com.android.server.audio.AudioServiceEvents.LoudnessEvent getStopPiid(int piid, int pid) {
            return new com.android.server.audio.AudioServiceEvents.LoudnessEvent(1, piid, pid);
        }

        static com.android.server.audio.AudioServiceEvents.LoudnessEvent getClientDied(int pid) {
            return new com.android.server.audio.AudioServiceEvents.LoudnessEvent(2, 0, pid);
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            switch (this.mEventType) {
                case 0:
                    return java.lang.String.format("Start loudness updates for piid %d for client pid %d", java.lang.Integer.valueOf(this.mIntValue1), java.lang.Integer.valueOf(this.mIntValue2));
                case 1:
                    return java.lang.String.format("Stop loudness updates for piid %d for client pid %d", java.lang.Integer.valueOf(this.mIntValue1), java.lang.Integer.valueOf(this.mIntValue2));
                case 2:
                    return java.lang.String.format("Loudness client with pid %d died", java.lang.Integer.valueOf(this.mIntValue2));
                default:
                    return "FIXME invalid event type:" + this.mEventType;
            }
        }
    }

    static final class StreamMuteEvent extends com.android.server.utils.EventLogger.Event {
        final boolean mMuted;
        final java.lang.String mSource;
        final int mStreamType;

        StreamMuteEvent(int streamType, boolean muted, java.lang.String source) {
            this.mStreamType = streamType;
            this.mMuted = muted;
            this.mSource = source;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            java.lang.String streamName;
            if (this.mStreamType <= android.media.AudioSystem.getNumStreamTypes() && this.mStreamType >= 0) {
                streamName = android.media.AudioSystem.STREAM_NAMES[this.mStreamType];
            } else {
                streamName = "stream " + this.mStreamType;
            }
            return streamName + (this.mMuted ? " muting by " : " unmuting by ") + this.mSource;
        }
    }

    static final class StreamUnmuteErrorEvent extends com.android.server.utils.EventLogger.Event {
        final int mRingerZenMutedStreams;
        final int mStreamType;

        StreamUnmuteErrorEvent(int streamType, int ringerZenMutedStreams) {
            this.mStreamType = streamType;
            this.mRingerZenMutedStreams = ringerZenMutedStreams;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            java.lang.String streamName;
            if (this.mStreamType <= android.media.AudioSystem.getNumStreamTypes() && this.mStreamType >= 0) {
                streamName = android.media.AudioSystem.STREAM_NAMES[this.mStreamType];
            } else {
                streamName = "stream " + this.mStreamType;
            }
            return "Invalid call to unmute " + streamName + " despite muted streams 0x" + java.lang.Integer.toHexString(this.mRingerZenMutedStreams);
        }
    }

    static final class RingerZenMutedStreamsEvent extends com.android.server.utils.EventLogger.Event {
        final int mRingerZenMutedStreams;
        final java.lang.String mSource;

        RingerZenMutedStreamsEvent(int ringerZenMutedStreams, java.lang.String source) {
            this.mRingerZenMutedStreams = ringerZenMutedStreams;
            this.mSource = source;
        }

        @Override // com.android.server.utils.EventLogger.Event
        public java.lang.String eventToString() {
            return "RingerZenMutedStreams 0x" + java.lang.Integer.toHexString(this.mRingerZenMutedStreams) + " from " + this.mSource;
        }
    }
}
