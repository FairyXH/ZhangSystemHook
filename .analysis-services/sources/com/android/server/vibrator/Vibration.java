package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
abstract class Vibration {
    public final com.android.server.vibrator.Vibration.CallerInfo callerInfo;
    public final android.os.IBinder callerToken;
    public final long id;
    private static final java.time.format.DateTimeFormatter DEBUG_TIME_FORMATTER = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss.SSS").withZone(java.time.ZoneId.systemDefault());
    private static final java.time.format.DateTimeFormatter DEBUG_DATE_TIME_FORMATTER = java.time.format.DateTimeFormatter.ofPattern("MM-dd HH:mm:ss.SSS").withZone(java.time.ZoneId.systemDefault());
    private static final java.util.concurrent.atomic.AtomicInteger sNextVibrationId = new java.util.concurrent.atomic.AtomicInteger(1);
    public final com.android.server.vibrator.VibrationStats stats = new com.android.server.vibrator.VibrationStats();
    private com.android.server.vibrator.IVibrationWrapper mVibrationWrapper = new com.android.server.vibrator.Vibration.VibrationWrapper();
    private int mPid = -1;

    abstract boolean isRepeating();

    enum Status {
        UNKNOWN(0),
        RUNNING(1),
        FINISHED(2),
        FINISHED_UNEXPECTED(3),
        FORWARDED_TO_INPUT_DEVICES(4),
        CANCELLED_BINDER_DIED(5),
        CANCELLED_BY_SCREEN_OFF(6),
        CANCELLED_BY_SETTINGS_UPDATE(7),
        CANCELLED_BY_USER(8),
        CANCELLED_BY_UNKNOWN_REASON(9),
        CANCELLED_SUPERSEDED(10),
        IGNORED_ERROR_APP_OPS(11),
        IGNORED_ERROR_CANCELLING(12),
        IGNORED_ERROR_SCHEDULING(13),
        IGNORED_ERROR_TOKEN(14),
        IGNORED_APP_OPS(15),
        IGNORED_BACKGROUND(16),
        IGNORED_MISSING_PERMISSION(28),
        IGNORED_UNSUPPORTED(18),
        IGNORED_FOR_EXTERNAL(19),
        IGNORED_FOR_HIGHER_IMPORTANCE(20),
        IGNORED_FOR_ONGOING(21),
        IGNORED_FOR_POWER(22),
        IGNORED_FOR_RINGER_MODE(23),
        IGNORED_FOR_SETTINGS(24),
        IGNORED_SUPERSEDED(25),
        IGNORED_FROM_VIRTUAL_DEVICE(26),
        IGNORED_ON_WIRELESS_CHARGER(27);

        private final int mProtoEnumValue;

        Status(int value) {
            this.mProtoEnumValue = value;
        }

        public int getProtoEnumValue() {
            return this.mProtoEnumValue;
        }
    }

    Vibration(android.os.IBinder token, com.android.server.vibrator.Vibration.CallerInfo callerInfo) {
        java.util.Objects.requireNonNull(token);
        java.util.Objects.requireNonNull(callerInfo);
        this.id = sNextVibrationId.getAndIncrement();
        this.callerToken = token;
        this.callerInfo = callerInfo;
    }

    static final class CallerInfo {
        public final android.os.VibrationAttributes attrs;
        public final int deviceId;
        public final java.lang.String opPkg;
        public final java.lang.String reason;
        public final int uid;

        CallerInfo(android.os.VibrationAttributes attrs, int uid, int deviceId, java.lang.String opPkg, java.lang.String reason) {
            java.util.Objects.requireNonNull(attrs);
            this.attrs = attrs;
            this.uid = uid;
            this.deviceId = deviceId;
            this.opPkg = opPkg;
            this.reason = reason;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.vibrator.Vibration.CallerInfo)) {
                return false;
            }
            com.android.server.vibrator.Vibration.CallerInfo that = (com.android.server.vibrator.Vibration.CallerInfo) o;
            return java.util.Objects.equals(this.attrs, that.attrs) && this.uid == that.uid && this.deviceId == that.deviceId && java.util.Objects.equals(this.opPkg, that.opPkg) && java.util.Objects.equals(this.reason, that.reason);
        }

        public int hashCode() {
            return java.util.Objects.hash(this.attrs, java.lang.Integer.valueOf(this.uid), java.lang.Integer.valueOf(this.deviceId), this.opPkg, this.reason);
        }

        public java.lang.String toString() {
            return "CallerInfo{ uid=" + this.uid + ", opPkg=" + this.opPkg + ", deviceId=" + this.deviceId + ", attrs=" + this.attrs + ", reason=" + this.reason + '}';
        }
    }

    static final class EndInfo {
        public final com.android.server.vibrator.Vibration.CallerInfo endedBy;
        public final com.android.server.vibrator.Vibration.Status status;

        EndInfo(com.android.server.vibrator.Vibration.Status status) {
            this(status, null);
        }

        EndInfo(com.android.server.vibrator.Vibration.Status status, com.android.server.vibrator.Vibration.CallerInfo endedBy) {
            this.status = status;
            this.endedBy = endedBy;
        }

        public boolean equals(java.lang.Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof com.android.server.vibrator.Vibration.EndInfo)) {
                return false;
            }
            com.android.server.vibrator.Vibration.EndInfo that = (com.android.server.vibrator.Vibration.EndInfo) o;
            return java.util.Objects.equals(this.endedBy, that.endedBy) && this.status == that.status;
        }

        public int hashCode() {
            return java.util.Objects.hash(this.status, this.endedBy);
        }

        public java.lang.String toString() {
            return "EndInfo{status=" + this.status + ", endedBy=" + this.endedBy + '}';
        }
    }

    static final class DebugInfo {
        private final float mAdaptiveScale;
        final com.android.server.vibrator.Vibration.CallerInfo mCallerInfo;
        final long mCreateTime;
        private final long mDurationMs;
        private final long mEndTime;
        private final android.os.CombinedVibration mOriginalEffect;
        final android.os.CombinedVibration mPlayedEffect;
        private final int mScaleLevel;
        private final long mStartTime;
        final com.android.server.vibrator.Vibration.Status mStatus;

        DebugInfo(com.android.server.vibrator.Vibration.Status status, com.android.server.vibrator.VibrationStats stats, android.os.CombinedVibration playedEffect, android.os.CombinedVibration originalEffect, int scaleLevel, float adaptiveScale, com.android.server.vibrator.Vibration.CallerInfo callerInfo) {
            java.util.Objects.requireNonNull(callerInfo);
            this.mCreateTime = stats.getCreateTimeDebug();
            this.mStartTime = stats.getStartTimeDebug();
            this.mEndTime = stats.getEndTimeDebug();
            this.mDurationMs = stats.getDurationDebug();
            this.mPlayedEffect = playedEffect;
            this.mOriginalEffect = originalEffect;
            this.mScaleLevel = scaleLevel;
            this.mAdaptiveScale = adaptiveScale;
            this.mCallerInfo = callerInfo;
            this.mStatus = status;
        }

        public java.lang.String toString() {
            return "createTime: " + com.android.server.vibrator.Vibration.DEBUG_DATE_TIME_FORMATTER.format(java.time.Instant.ofEpochMilli(this.mCreateTime)) + ", startTime: " + com.android.server.vibrator.Vibration.DEBUG_DATE_TIME_FORMATTER.format(java.time.Instant.ofEpochMilli(this.mStartTime)) + ", endTime: " + (this.mEndTime == 0 ? null : com.android.server.vibrator.Vibration.DEBUG_DATE_TIME_FORMATTER.format(java.time.Instant.ofEpochMilli(this.mEndTime))) + ", durationMs: " + this.mDurationMs + ", status: " + this.mStatus.name().toLowerCase(java.util.Locale.ROOT) + ", playedEffect: " + this.mPlayedEffect + ", originalEffect: " + this.mOriginalEffect + ", scaleLevel: " + com.android.server.vibrator.VibrationScaler.scaleLevelToString(this.mScaleLevel) + ", adaptiveScale: " + java.lang.String.format(java.util.Locale.ROOT, "%.2f", java.lang.Float.valueOf(this.mAdaptiveScale)) + ", callerInfo: " + this.mCallerInfo;
        }

        void logMetrics(com.android.server.vibrator.VibratorFrameworkStatsLogger statsLogger) {
            statsLogger.logVibrationAdaptiveHapticScale(this.mCallerInfo.uid, this.mAdaptiveScale);
        }

        void dumpCompact(android.util.IndentingPrintWriter pw) {
            java.lang.String categoryStr;
            boolean isExternalVibration = this.mPlayedEffect == null;
            java.lang.String timingsStr = java.lang.String.format(java.util.Locale.ROOT, "%s | %8s | %20s | duration: %5dms | start: %12s | end: %12s", com.android.server.vibrator.Vibration.DEBUG_DATE_TIME_FORMATTER.format(java.time.Instant.ofEpochMilli(this.mCreateTime)), isExternalVibration ? "external" : "effect", this.mStatus.name().toLowerCase(java.util.Locale.ROOT), java.lang.Long.valueOf(this.mDurationMs), this.mStartTime == 0 ? "" : com.android.server.vibrator.Vibration.DEBUG_TIME_FORMATTER.format(java.time.Instant.ofEpochMilli(this.mStartTime)), this.mEndTime == 0 ? "" : com.android.server.vibrator.Vibration.DEBUG_TIME_FORMATTER.format(java.time.Instant.ofEpochMilli(this.mEndTime)));
            java.lang.String paramStr = java.lang.String.format(java.util.Locale.ROOT, " | scale: %8s (adaptive=%.2f) | flags: %4s | usage: %s", com.android.server.vibrator.VibrationScaler.scaleLevelToString(this.mScaleLevel), java.lang.Float.valueOf(this.mAdaptiveScale), java.lang.Long.toBinaryString(this.mCallerInfo.attrs.getFlags()), this.mCallerInfo.attrs.usageToString());
            if (this.mCallerInfo.attrs.getCategory() != 0) {
                categoryStr = " | category=" + android.os.VibrationAttributes.categoryToString(this.mCallerInfo.attrs.getCategory());
            } else {
                categoryStr = "";
            }
            java.lang.String audioUsageStr = this.mCallerInfo.attrs.getOriginalAudioUsage() != 0 ? " | audioUsage=" + android.media.AudioAttributes.usageToString(this.mCallerInfo.attrs.getOriginalAudioUsage()) : "";
            java.lang.String callerStr = java.lang.String.format(java.util.Locale.ROOT, " | %s (uid=%d, deviceId=%d) | reason: %s", this.mCallerInfo.opPkg, java.lang.Integer.valueOf(this.mCallerInfo.uid), java.lang.Integer.valueOf(this.mCallerInfo.deviceId), this.mCallerInfo.reason);
            java.lang.String effectStr = java.lang.String.format(java.util.Locale.ROOT, " | played: %s | original: %s", this.mPlayedEffect == null ? null : this.mPlayedEffect.toDebugString(), this.mOriginalEffect != null ? this.mOriginalEffect.toDebugString() : null);
            pw.println(timingsStr + paramStr + categoryStr + audioUsageStr + callerStr + effectStr);
        }

        void dump(android.util.IndentingPrintWriter pw) {
            pw.println("Vibration:");
            pw.increaseIndent();
            pw.println("status = " + this.mStatus.name().toLowerCase(java.util.Locale.ROOT));
            pw.println("durationMs = " + this.mDurationMs);
            pw.println("createTime = " + com.android.server.vibrator.Vibration.DEBUG_DATE_TIME_FORMATTER.format(java.time.Instant.ofEpochMilli(this.mCreateTime)));
            pw.println("startTime = " + com.android.server.vibrator.Vibration.DEBUG_DATE_TIME_FORMATTER.format(java.time.Instant.ofEpochMilli(this.mStartTime)));
            pw.println("endTime = " + (this.mEndTime == 0 ? null : com.android.server.vibrator.Vibration.DEBUG_DATE_TIME_FORMATTER.format(java.time.Instant.ofEpochMilli(this.mEndTime))));
            pw.println("playedEffect = " + this.mPlayedEffect);
            pw.println("originalEffect = " + this.mOriginalEffect);
            pw.println("scale = " + com.android.server.vibrator.VibrationScaler.scaleLevelToString(this.mScaleLevel));
            pw.println("adaptiveScale = " + java.lang.String.format(java.util.Locale.ROOT, "%.2f", java.lang.Float.valueOf(this.mAdaptiveScale)));
            pw.println("callerInfo = " + this.mCallerInfo);
            pw.decreaseIndent();
        }

        void dump(android.util.proto.ProtoOutputStream proto, long fieldId) {
            long token = proto.start(fieldId);
            proto.write(1112396529665L, this.mStartTime);
            proto.write(1112396529666L, this.mEndTime);
            proto.write(1112396529671L, this.mDurationMs);
            proto.write(1159641169928L, this.mStatus.ordinal());
            long attrsToken = proto.start(1146756268037L);
            android.os.VibrationAttributes attrs = this.mCallerInfo.attrs;
            proto.write(1120986464257L, attrs.getUsage());
            proto.write(1120986464258L, attrs.getAudioUsage());
            proto.write(1120986464260L, attrs.getCategory());
            proto.write(1120986464259L, attrs.getFlags());
            proto.end(attrsToken);
            if (this.mPlayedEffect != null) {
                dumpEffect(proto, 1146756268035L, this.mPlayedEffect);
            }
            if (this.mOriginalEffect != null) {
                dumpEffect(proto, 1146756268036L, this.mOriginalEffect);
            }
            proto.end(token);
        }

        private void dumpEffect(android.util.proto.ProtoOutputStream proto, long fieldId, android.os.CombinedVibration effect) {
            dumpEffect(proto, fieldId, (android.os.CombinedVibration.Sequential) android.os.CombinedVibration.startSequential().addNext(effect).combine());
        }

        private void dumpEffect(android.util.proto.ProtoOutputStream proto, long fieldId, android.os.CombinedVibration.Sequential effect) {
            long token = proto.start(fieldId);
            for (int i = 0; i < effect.getEffects().size(); i++) {
                android.os.CombinedVibration nestedEffect = (android.os.CombinedVibration) effect.getEffects().get(i);
                if (nestedEffect instanceof android.os.CombinedVibration.Mono) {
                    dumpEffect(proto, 2246267895809L, (android.os.CombinedVibration.Mono) nestedEffect);
                } else if (nestedEffect instanceof android.os.CombinedVibration.Stereo) {
                    dumpEffect(proto, 2246267895809L, (android.os.CombinedVibration.Stereo) nestedEffect);
                }
                proto.write(2220498092034L, ((java.lang.Integer) effect.getDelays().get(i)).intValue());
            }
            proto.end(token);
        }

        private void dumpEffect(android.util.proto.ProtoOutputStream proto, long fieldId, android.os.CombinedVibration.Mono effect) {
            long token = proto.start(fieldId);
            dumpEffect(proto, 2246267895809L, effect.getEffect());
            proto.end(token);
        }

        private void dumpEffect(android.util.proto.ProtoOutputStream proto, long fieldId, android.os.CombinedVibration.Stereo effect) {
            long token = proto.start(fieldId);
            for (int i = 0; i < effect.getEffects().size(); i++) {
                proto.write(2220498092034L, effect.getEffects().keyAt(i));
                dumpEffect(proto, 2246267895809L, (android.os.VibrationEffect) effect.getEffects().valueAt(i));
            }
            proto.end(token);
        }

        private void dumpEffect(android.util.proto.ProtoOutputStream proto, long fieldId, android.os.VibrationEffect effect) {
            long token = proto.start(fieldId);
            android.os.VibrationEffect.Composed composed = (android.os.VibrationEffect.Composed) effect;
            for (android.os.vibrator.VibrationEffectSegment segment : composed.getSegments()) {
                dumpEffect(proto, 1146756268033L, segment);
            }
            proto.write(1120986464258L, composed.getRepeatIndex());
            proto.end(token);
        }

        private void dumpEffect(android.util.proto.ProtoOutputStream proto, long fieldId, android.os.vibrator.VibrationEffectSegment segment) {
            long token = proto.start(fieldId);
            if (segment instanceof android.os.vibrator.StepSegment) {
                dumpEffect(proto, 1146756268035L, (android.os.vibrator.StepSegment) segment);
            } else if (segment instanceof android.os.vibrator.RampSegment) {
                dumpEffect(proto, 1146756268036L, (android.os.vibrator.RampSegment) segment);
            } else if (segment instanceof android.os.vibrator.PrebakedSegment) {
                dumpEffect(proto, 1146756268033L, (android.os.vibrator.PrebakedSegment) segment);
            } else if (segment instanceof android.os.vibrator.PrimitiveSegment) {
                dumpEffect(proto, 1146756268034L, (android.os.vibrator.PrimitiveSegment) segment);
            }
            proto.end(token);
        }

        private void dumpEffect(android.util.proto.ProtoOutputStream proto, long fieldId, android.os.vibrator.StepSegment segment) {
            long token = proto.start(fieldId);
            proto.write(1120986464257L, segment.getDuration());
            proto.write(1108101562370L, segment.getAmplitude());
            proto.write(1108101562371L, segment.getFrequencyHz());
            proto.end(token);
        }

        private void dumpEffect(android.util.proto.ProtoOutputStream proto, long fieldId, android.os.vibrator.RampSegment segment) {
            long token = proto.start(fieldId);
            proto.write(1120986464257L, segment.getDuration());
            proto.write(1108101562370L, segment.getStartAmplitude());
            proto.write(1108101562371L, segment.getEndAmplitude());
            proto.write(1108101562372L, segment.getStartFrequencyHz());
            proto.write(1108101562373L, segment.getEndFrequencyHz());
            proto.end(token);
        }

        private void dumpEffect(android.util.proto.ProtoOutputStream proto, long fieldId, android.os.vibrator.PrebakedSegment segment) {
            long token = proto.start(fieldId);
            proto.write(1120986464257L, segment.getEffectId());
            proto.write(1120986464258L, segment.getEffectStrength());
            proto.write(1120986464259L, segment.shouldFallback());
            proto.end(token);
        }

        private void dumpEffect(android.util.proto.ProtoOutputStream proto, long fieldId, android.os.vibrator.PrimitiveSegment segment) {
            long token = proto.start(fieldId);
            proto.write(1120986464257L, segment.getPrimitiveId());
            proto.write(1108101562370L, segment.getScale());
            proto.write(1120986464259L, segment.getDelay());
            proto.end(token);
        }
    }

    public com.android.server.vibrator.IVibrationWrapper getWrapper() {
        return this.mVibrationWrapper;
    }

    private class VibrationWrapper implements com.android.server.vibrator.IVibrationWrapper {
        private VibrationWrapper() {
        }

        @Override // com.android.server.vibrator.IVibrationWrapper
        public void setVibrationPid(int pid) {
            com.android.server.vibrator.Vibration.this.mPid = pid;
        }

        @Override // com.android.server.vibrator.IVibrationWrapper
        public int getVibrationPid() {
            return com.android.server.vibrator.Vibration.this.mPid;
        }
    }
}
