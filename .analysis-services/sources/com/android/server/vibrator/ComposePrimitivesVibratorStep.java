package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
final class ComposePrimitivesVibratorStep extends com.android.server.vibrator.AbstractVibratorStep {
    private static final int DEFAULT_COMPOSITION_SIZE_LIMIT = 100;

    ComposePrimitivesVibratorStep(com.android.server.vibrator.VibrationStepConductor conductor, long startTime, com.android.server.vibrator.VibratorController controller, android.os.VibrationEffect.Composed effect, int index, long pendingVibratorOffDeadline) {
        super(conductor, java.lang.Math.max(startTime, pendingVibratorOffDeadline), controller, effect, index, pendingVibratorOffDeadline);
    }

    @Override // com.android.server.vibrator.Step
    public java.util.List<com.android.server.vibrator.Step> play() {
        android.os.Trace.traceBegin(8388608L, "ComposePrimitivesStep");
        try {
            int limit = this.controller.getVibratorInfo().getCompositionSizeMax();
            java.util.List<android.os.vibrator.PrimitiveSegment> primitives = unrollPrimitiveSegments(this.effect, this.segmentIndex, limit > 0 ? limit : 100);
            if (primitives.isEmpty()) {
                android.util.Slog.w("VibrationThread", "Ignoring wrong segment for a ComposePrimitivesStep: " + this.effect.getSegments().get(this.segmentIndex));
                return nextSteps(1);
            }
            if (com.android.server.vibrator.VibrationThread.DEBUG) {
                android.util.Slog.d("VibrationThread", "Compose " + primitives + " primitives on vibrator " + getVibratorId());
            }
            android.os.vibrator.PrimitiveSegment[] primitivesArray = (android.os.vibrator.PrimitiveSegment[]) primitives.toArray(new android.os.vibrator.PrimitiveSegment[primitives.size()]);
            long vibratorOnResult = this.controller.on(primitivesArray, getVibration().id);
            handleVibratorOnResult(vibratorOnResult);
            getVibration().stats.reportComposePrimitives(vibratorOnResult, primitivesArray);
            return nextSteps(primitives.size());
        } finally {
            android.os.Trace.traceEnd(8388608L);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x002b A[LOOP:0: B:3:0x0012->B:10:0x002b, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0034 A[EDGE_INSN: B:14:0x0034->B:11:0x0034 BREAK  A[LOOP:0: B:3:0x0012->B:10:0x002b], SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.util.List<android.os.vibrator.PrimitiveSegment> unrollPrimitiveSegments(android.os.VibrationEffect.Composed r7, int r8, int r9) {
        /*
            r6 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>(r9)
            java.util.List r1 = r7.getSegments()
            int r1 = r1.size()
            int r2 = r7.getRepeatIndex()
            r3 = r8
        L12:
            int r4 = r0.size()
            if (r4 >= r9) goto L34
            if (r3 != r1) goto L1d
            if (r2 < 0) goto L34
            r3 = r2
        L1d:
            java.util.List r4 = r7.getSegments()
            java.lang.Object r4 = r4.get(r3)
            android.os.vibrator.VibrationEffectSegment r4 = (android.os.vibrator.VibrationEffectSegment) r4
            boolean r5 = r4 instanceof android.os.vibrator.PrimitiveSegment
            if (r5 == 0) goto L34
            r5 = r4
            android.os.vibrator.PrimitiveSegment r5 = (android.os.vibrator.PrimitiveSegment) r5
            r0.add(r5)
            int r3 = r3 + 1
            goto L12
        L34:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.vibrator.ComposePrimitivesVibratorStep.unrollPrimitiveSegments(android.os.VibrationEffect$Composed, int, int):java.util.List");
    }
}
