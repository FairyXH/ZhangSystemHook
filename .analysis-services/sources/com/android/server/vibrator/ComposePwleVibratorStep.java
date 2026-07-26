package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class ComposePwleVibratorStep extends com.android.server.vibrator.AbstractVibratorStep {
    private static final int DEFAULT_PWLE_SIZE_LIMIT = 100;

    ComposePwleVibratorStep(com.android.server.vibrator.VibrationStepConductor conductor, long startTime, com.android.server.vibrator.VibratorController controller, android.os.VibrationEffect.Composed effect, int index, long pendingVibratorOffDeadline) {
        super(conductor, java.lang.Math.max(startTime, pendingVibratorOffDeadline), controller, effect, index, pendingVibratorOffDeadline);
    }

    @Override // com.android.server.vibrator.Step
    public java.util.List<com.android.server.vibrator.Step> play() {
        android.os.Trace.traceBegin(8388608L, "ComposePwleStep");
        try {
            int limit = this.controller.getVibratorInfo().getPwleSizeMax();
            java.util.List<android.os.vibrator.RampSegment> pwles = unrollRampSegments(this.effect, this.segmentIndex, limit > 0 ? limit : 100);
            if (pwles.isEmpty()) {
                android.util.Slog.w("VibrationThread", "Ignoring wrong segment for a ComposePwleStep: " + this.effect.getSegments().get(this.segmentIndex));
                return nextSteps(1);
            }
            if (com.android.server.vibrator.VibrationThread.DEBUG) {
                android.util.Slog.d("VibrationThread", "Compose " + pwles + " PWLEs on vibrator " + this.controller.getVibratorInfo().getId());
            }
            android.os.vibrator.RampSegment[] pwlesArray = (android.os.vibrator.RampSegment[]) pwles.toArray(new android.os.vibrator.RampSegment[pwles.size()]);
            long vibratorOnResult = this.controller.on(pwlesArray, getVibration().id);
            handleVibratorOnResult(vibratorOnResult);
            getVibration().stats.reportComposePwle(vibratorOnResult, pwlesArray);
            return nextSteps(pwles.size());
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x002e  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0045 A[EDGE_INSN: B:21:0x0045->B:14:0x0045 BREAK  A[LOOP:0: B:3:0x0015->B:13:0x0042], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.List<android.os.vibrator.RampSegment> unrollRampSegments(android.os.VibrationEffect.Composed r10, int r11, int r12) {
        /*
            r9 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>(r12)
            r1 = 1065353216(0x3f800000, float:1.0)
            r2 = r12
            java.util.List r3 = r10.getSegments()
            int r3 = r3.size()
            int r4 = r10.getRepeatIndex()
            r5 = r11
        L15:
            int r6 = r0.size()
            if (r6 > r12) goto L45
            if (r5 != r3) goto L20
            if (r4 < 0) goto L45
            r5 = r4
        L20:
            java.util.List r6 = r10.getSegments()
            java.lang.Object r6 = r6.get(r5)
            android.os.vibrator.VibrationEffectSegment r6 = (android.os.vibrator.VibrationEffectSegment) r6
            boolean r7 = r6 instanceof android.os.vibrator.RampSegment
            if (r7 == 0) goto L45
            r7 = r6
            android.os.vibrator.RampSegment r7 = (android.os.vibrator.RampSegment) r7
            r0.add(r7)
            boolean r8 = r9.isBetterBreakPosition(r0, r1, r12)
            if (r8 == 0) goto L42
            float r1 = r7.getEndAmplitude()
            int r2 = r0.size()
        L42:
            int r5 = r5 + 1
            goto L15
        L45:
            int r5 = r0.size()
            if (r5 <= r12) goto L51
            r5 = 0
            java.util.List r5 = r0.subList(r5, r2)
            goto L52
        L51:
            r5 = r0
        L52:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.vibrator.ComposePwleVibratorStep.unrollRampSegments(android.os.VibrationEffect$Composed, int, int):java.util.List");
    }

    private boolean isBetterBreakPosition(java.util.List<android.os.vibrator.RampSegment> segments, float currentBestBreakAmplitude, int limit) {
        android.os.vibrator.RampSegment lastSegment = segments.get(segments.size() - 1);
        float breakAmplitudeCandidate = lastSegment.getEndAmplitude();
        int breakPositionCandidate = segments.size();
        if (breakPositionCandidate > limit) {
            return false;
        }
        if (breakAmplitudeCandidate == 0.0f) {
            return true;
        }
        return breakPositionCandidate >= limit / 2 && breakAmplitudeCandidate <= currentBestBreakAmplitude;
    }
}
