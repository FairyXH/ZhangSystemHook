package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class StartSequentialEffectStep extends com.android.server.vibrator.Step {
    public final int currentIndex;
    private long mVibratorsOnMaxDuration;
    public final android.os.CombinedVibration.Sequential sequentialEffect;

    StartSequentialEffectStep(com.android.server.vibrator.VibrationStepConductor conductor, android.os.CombinedVibration.Sequential effect) {
        this(conductor, android.os.SystemClock.uptimeMillis() + ((long) ((java.lang.Integer) effect.getDelays().get(0)).intValue()), effect, 0);
    }

    private StartSequentialEffectStep(com.android.server.vibrator.VibrationStepConductor conductor, long startTime, android.os.CombinedVibration.Sequential effect, int index) {
        super(conductor, startTime);
        this.sequentialEffect = effect;
        this.currentIndex = index;
    }

    @Override // com.android.server.vibrator.Step
    public long getVibratorOnDuration() {
        return this.mVibratorsOnMaxDuration;
    }

    @Override // com.android.server.vibrator.Step
    public java.util.List<com.android.server.vibrator.Step> play() {
        android.os.Trace.traceBegin(8388608L, "StartSequentialEffectStep");
        java.util.List<com.android.server.vibrator.Step> nextSteps = new java.util.ArrayList<>();
        this.mVibratorsOnMaxDuration = -1L;
        try {
            if (com.android.server.vibrator.VibrationThread.DEBUG) {
                android.util.Slog.d("VibrationThread", "StartSequentialEffectStep for effect #" + this.currentIndex);
            }
            android.os.CombinedVibration effect = (android.os.CombinedVibration) this.sequentialEffect.getEffects().get(this.currentIndex);
            com.android.server.vibrator.StartSequentialEffectStep.DeviceEffectMap effectMapping = createEffectToVibratorMapping(effect);
            if (effectMapping == null) {
                return nextSteps;
            }
            this.mVibratorsOnMaxDuration = startVibrating(effectMapping, nextSteps);
            this.conductor.vibratorManagerHooks.noteVibratorOn(this.conductor.getVibration().callerInfo.uid, this.mVibratorsOnMaxDuration);
            if (this.mVibratorsOnMaxDuration >= 0) {
                com.android.server.vibrator.Step nextStep = this.mVibratorsOnMaxDuration > 0 ? new com.android.server.vibrator.FinishSequentialEffectStep(this) : nextStep();
                if (nextStep != null) {
                    nextSteps.add(nextStep);
                }
            }
            android.os.Trace.traceEnd(8388608L);
            return nextSteps;
        } finally {
            if (this.mVibratorsOnMaxDuration >= 0) {
                com.android.server.vibrator.Step nextStep2 = this.mVibratorsOnMaxDuration > 0 ? new com.android.server.vibrator.FinishSequentialEffectStep(this) : nextStep();
                if (nextStep2 != null) {
                    nextSteps.add(nextStep2);
                }
            }
            android.os.Trace.traceEnd(8388608L);
        }
    }

    @Override // com.android.server.vibrator.Step
    public java.util.List<com.android.server.vibrator.Step> cancel() {
        return com.android.server.vibrator.VibrationStepConductor.EMPTY_STEP_LIST;
    }

    @Override // com.android.server.vibrator.Step
    public void cancelImmediately() {
    }

    com.android.server.vibrator.Step nextStep() {
        int nextIndex = this.currentIndex + 1;
        if (nextIndex >= this.sequentialEffect.getEffects().size()) {
            return null;
        }
        long nextEffectDelay = ((java.lang.Integer) this.sequentialEffect.getDelays().get(nextIndex)).intValue();
        long nextStartTime = android.os.SystemClock.uptimeMillis() + nextEffectDelay;
        return new com.android.server.vibrator.StartSequentialEffectStep(this.conductor, nextStartTime, this.sequentialEffect, nextIndex);
    }

    private com.android.server.vibrator.StartSequentialEffectStep.DeviceEffectMap createEffectToVibratorMapping(android.os.CombinedVibration effect) {
        if (effect instanceof android.os.CombinedVibration.Mono) {
            return new com.android.server.vibrator.StartSequentialEffectStep.DeviceEffectMap((android.os.CombinedVibration.Mono) effect);
        }
        if (effect instanceof android.os.CombinedVibration.Stereo) {
            return new com.android.server.vibrator.StartSequentialEffectStep.DeviceEffectMap((android.os.CombinedVibration.Stereo) effect);
        }
        return null;
    }

    private long startVibrating(com.android.server.vibrator.StartSequentialEffectStep.DeviceEffectMap effectMapping, java.util.List<com.android.server.vibrator.Step> nextSteps) {
        int vibratorCount = effectMapping.size();
        if (vibratorCount == 0) {
            return 0L;
        }
        com.android.server.vibrator.AbstractVibratorStep[] steps = new com.android.server.vibrator.AbstractVibratorStep[vibratorCount];
        long vibrationStartTime = android.os.SystemClock.uptimeMillis();
        int i = 0;
        while (i < vibratorCount) {
            int i2 = i;
            steps[i2] = this.conductor.nextVibrateStep(vibrationStartTime, this.conductor.getVibrators().get(effectMapping.vibratorIdAt(i)), effectMapping.effectAt(i), 0, 0L);
            i = i2 + 1;
        }
        if (steps.length == 1) {
            return startVibrating(steps[0], nextSteps);
        }
        boolean hasTriggered = false;
        boolean hasFailed = false;
        long maxDuration = 0;
        boolean hasPrepared = this.conductor.vibratorManagerHooks.prepareSyncedVibration(effectMapping.getRequiredSyncCapabilities(), effectMapping.getVibratorIds());
        int length = steps.length;
        int i3 = 0;
        while (true) {
            if (i3 >= length) {
                break;
            }
            com.android.server.vibrator.AbstractVibratorStep step = steps[i3];
            int i4 = length;
            com.android.server.vibrator.AbstractVibratorStep[] steps2 = steps;
            long duration = startVibrating(step, nextSteps);
            if (duration < 0) {
                hasFailed = true;
                break;
            }
            maxDuration = java.lang.Math.max(maxDuration, duration);
            i3++;
            steps = steps2;
            length = i4;
        }
        if (hasPrepared && !hasFailed && maxDuration > 0) {
            hasTriggered = this.conductor.vibratorManagerHooks.triggerSyncedVibration(getVibration().id);
            hasFailed &= hasTriggered;
        }
        if (hasFailed) {
            for (int i5 = nextSteps.size() - 1; i5 >= 0; i5--) {
                nextSteps.remove(i5).cancelImmediately();
            }
        }
        if (hasPrepared && !hasTriggered) {
            this.conductor.vibratorManagerHooks.cancelSyncedVibration();
        }
        if (hasFailed) {
            return -1L;
        }
        return maxDuration;
    }

    private long startVibrating(com.android.server.vibrator.AbstractVibratorStep step, java.util.List<com.android.server.vibrator.Step> nextSteps) {
        nextSteps.addAll(step.play());
        long stepDuration = step.getVibratorOnDuration();
        if (stepDuration < 0) {
            return stepDuration;
        }
        return java.lang.Math.max(stepDuration, step.effect.getDuration());
    }

    final class DeviceEffectMap {
        private final long mRequiredSyncCapabilities;
        private final android.util.SparseArray<android.os.VibrationEffect.Composed> mVibratorEffects;
        private final int[] mVibratorIds;

        DeviceEffectMap(android.os.CombinedVibration.Mono mono) {
            android.util.SparseArray<com.android.server.vibrator.VibratorController> vibrators = com.android.server.vibrator.StartSequentialEffectStep.this.conductor.getVibrators();
            android.os.VibrationEffect.Composed effect = mono.getEffect();
            if (effect instanceof android.os.VibrationEffect.Composed) {
                this.mVibratorEffects = new android.util.SparseArray<>(vibrators.size());
                this.mVibratorIds = new int[vibrators.size()];
                android.os.VibrationEffect.Composed composedEffect = effect;
                for (int i = 0; i < vibrators.size(); i++) {
                    int vibratorId = vibrators.keyAt(i);
                    this.mVibratorEffects.put(vibratorId, composedEffect);
                    this.mVibratorIds[i] = vibratorId;
                }
            } else {
                android.util.Slog.wtf("VibrationThread", "Unable to map device vibrators to unexpected effect: " + effect);
                this.mVibratorEffects = new android.util.SparseArray<>();
                this.mVibratorIds = new int[0];
            }
            this.mRequiredSyncCapabilities = calculateRequiredSyncCapabilities(this.mVibratorEffects);
        }

        DeviceEffectMap(android.os.CombinedVibration.Stereo stereo) {
            android.util.SparseArray<com.android.server.vibrator.VibratorController> vibrators = com.android.server.vibrator.StartSequentialEffectStep.this.conductor.getVibrators();
            android.util.SparseArray<android.os.VibrationEffect> stereoEffects = stereo.getEffects();
            this.mVibratorEffects = new android.util.SparseArray<>();
            for (int i = 0; i < stereoEffects.size(); i++) {
                int vibratorId = stereoEffects.keyAt(i);
                if (vibrators.contains(vibratorId)) {
                    android.os.VibrationEffect vibrationEffectValueAt = stereoEffects.valueAt(i);
                    if (vibrationEffectValueAt instanceof android.os.VibrationEffect.Composed) {
                        this.mVibratorEffects.put(vibratorId, (android.os.VibrationEffect.Composed) vibrationEffectValueAt);
                    } else {
                        android.util.Slog.wtf("VibrationThread", "Unable to map device vibrators to unexpected effect: " + vibrationEffectValueAt);
                    }
                }
            }
            this.mVibratorIds = new int[this.mVibratorEffects.size()];
            for (int i2 = 0; i2 < this.mVibratorEffects.size(); i2++) {
                this.mVibratorIds[i2] = this.mVibratorEffects.keyAt(i2);
            }
            this.mRequiredSyncCapabilities = calculateRequiredSyncCapabilities(this.mVibratorEffects);
        }

        public int size() {
            return this.mVibratorIds.length;
        }

        public long getRequiredSyncCapabilities() {
            return this.mRequiredSyncCapabilities;
        }

        public int[] getVibratorIds() {
            return this.mVibratorIds;
        }

        public int vibratorIdAt(int index) {
            return this.mVibratorEffects.keyAt(index);
        }

        public android.os.VibrationEffect.Composed effectAt(int index) {
            return this.mVibratorEffects.valueAt(index);
        }

        private long calculateRequiredSyncCapabilities(android.util.SparseArray<android.os.VibrationEffect.Composed> effects) {
            long prepareCap = 0;
            for (int i = 0; i < effects.size(); i++) {
                android.os.vibrator.VibrationEffectSegment firstSegment = (android.os.vibrator.VibrationEffectSegment) effects.valueAt(i).getSegments().get(0);
                if (firstSegment instanceof android.os.vibrator.StepSegment) {
                    prepareCap |= 2;
                } else if (firstSegment instanceof android.os.vibrator.PrebakedSegment) {
                    prepareCap |= 4;
                } else if (firstSegment instanceof android.os.vibrator.PrimitiveSegment) {
                    prepareCap |= 8;
                }
            }
            int triggerCap = 0;
            if (requireMixedTriggerCapability(prepareCap, 2L)) {
                triggerCap = 0 | 16;
            }
            if (requireMixedTriggerCapability(prepareCap, 4L)) {
                triggerCap |= 32;
            }
            if (requireMixedTriggerCapability(prepareCap, 8L)) {
                triggerCap |= 64;
            }
            return 1 | prepareCap | ((long) triggerCap);
        }

        private boolean requireMixedTriggerCapability(long prepareCapabilities, long capability) {
            return ((prepareCapabilities & capability) == 0 || ((~capability) & prepareCapabilities) == 0) ? false : true;
        }
    }
}
