package com.android.server.vibrator;

/* JADX INFO: loaded from: classes3.dex */
public interface IVibratorManagerServiceExt {
    default void init(android.content.Context context) {
    }

    default void onSystemReady() {
    }

    default void dynamicallyConfigLogTag(java.io.PrintWriter pw, java.lang.String[] args) {
    }

    default boolean cancelScreenOffReceiver(android.content.Context context, android.content.BroadcastReceiver intentReceive) {
        return false;
    }

    default boolean ignoreVibrateForOneShotEffect(android.os.CombinedVibration newEffect) {
        return false;
    }

    default void updateVibratorStopStatus(boolean isReadyToStop) {
    }

    default void cancelLinearMotorVibrator() {
    }

    @java.lang.Deprecated
    default boolean startCustomizeVibratorLocked(android.os.CombinedVibration effect, int vibUid, int vibUsageHint, com.android.server.vibrator.VibrationThread thread) {
        return false;
    }

    @java.lang.Deprecated
    default boolean startCustomizeVibratorLocked(android.os.CombinedVibration effect, int vibUid, int vibUsageHint, com.android.server.vibrator.VibrationStepConductor conductor) {
        return false;
    }

    @java.lang.Deprecated
    default boolean startRichTapVibratorLocked(android.os.CombinedVibration effect, int vibUid, int vibUsageHint, java.util.ArrayList<android.os.Vibrator> inputDeviceVibrators) {
        return false;
    }

    default boolean startRichTapVibratorLocked(com.android.server.vibrator.HalVibration vib) {
        return false;
    }

    default boolean isNativeVibrationEffect(android.os.VibrationEffect effect) {
        return false;
    }

    default boolean isNativeWaveformEffect(android.os.VibrationEffect effect) {
        return false;
    }

    default int getStrength() {
        return 0;
    }

    default int getVibratorStatus() {
        return 0;
    }

    default void setVibratorStrength(int strength) {
    }

    default int getVibratorTouchStyle() {
        return 0;
    }

    default void setVibratorTouchStyle(int style) {
    }

    default void updateVibrator() {
    }

    default android.util.SparseArray<com.android.server.vibrator.VibratorController> getVibrators() {
        return null;
    }

    default boolean disposeRichtapEffectParams(android.os.CombinedVibration effect) {
        return false;
    }

    default void logVibratorIgnoreStatus(com.android.server.vibrator.Vibration.Status ignoreStatus, java.lang.String functionName) {
    }

    default void vibrate(int uid, int pid, java.lang.String opPkg, android.os.CombinedVibration effect, android.os.VibrationAttributes attrs, android.os.IBinder token) {
    }

    default void cancelVibrate(int uid, int pid, int usageFilter, android.os.IBinder token, com.android.server.vibrator.VibrationStepConductor conductor) {
    }

    default void updateVibrationAmplitude(int uid, java.lang.String opPkg, float amplitudeRatio) {
    }

    default boolean ignoreVibrateForRichTapVibrationEffect(android.os.CombinedVibration newEffect) {
        return false;
    }

    default boolean blockVibrationForApplicationLocked(java.lang.String opPkg, boolean block, android.os.IBinder token) {
        return false;
    }

    default boolean isBlockedByApplicationLocked() {
        return false;
    }

    default android.os.CombinedVibration fixVibrationEffect(android.os.CombinedVibration effect) {
        return effect;
    }

    default boolean ignoreVibrationForCamera(int uid, java.lang.String opPkg, android.os.CombinedVibration effect) {
        return false;
    }

    default android.os.CombinedVibration convertVibrationEffect(android.os.CombinedVibration effect, android.os.VibrationAttributes attrs, int uid, java.lang.String opPkg, java.lang.String reason) {
        return effect;
    }

    default android.os.CombinedVibration transferEffectToWaveform(android.os.CombinedVibration effect) {
        return effect;
    }

    default void fixVibrationEffectStrength(android.os.CombinedVibration effect, android.os.VibrationAttributes attrs) {
    }

    default boolean checkIfRichtapPatternHeEffect(android.os.CombinedVibration effect) {
        return false;
    }

    default boolean checkIfRichTapEffect(android.os.CombinedVibration effect) {
        return false;
    }

    default boolean checkIfRichTapParameter(android.os.CombinedVibration effect) {
        return false;
    }

    default void stopRichtapVibration() {
    }

    default int getWaveformIndex(int effectId) {
        return -1;
    }

    default int getEffectDuration(int effectId) {
        return -1;
    }

    default int getEffectType(int effectId) {
        return -1;
    }

    default int getRingtoneEffectId(java.lang.String ringtonePath) {
        return -1;
    }

    default int fixupVibrationAttributes(android.os.VibrationAttributes attrs, android.os.CombinedVibration effect) {
        return attrs.getUsage();
    }

    default void onNoteVibratorOnLocked(int uid, long timing) {
    }

    default com.android.server.vibrator.Vibration.EndInfo shouldIgnoreVibrationForOngoing(com.android.server.vibrator.Vibration newVibration, com.android.server.vibrator.Vibration ongoingVibration) {
        return null;
    }

    default void vibrate(int uid, java.lang.String opPkg, android.os.CombinedVibration effect, android.os.VibrationAttributes attrs, java.lang.String reason, android.os.IBinder token) {
    }

    default void noteVibration(com.android.server.vibrator.Vibration vibration) {
    }

    default android.os.CombinedVibration fixImeVibrationStrength(android.os.CombinedVibration effect, java.lang.String opPkg) {
        return effect;
    }

    default android.os.CombinedVibration fixVibrationEffectDuration(android.os.CombinedVibration effect) {
        return effect;
    }

    default java.lang.String getConvertVibrationReason(android.os.CombinedVibration effect) {
        return null;
    }

    default boolean shouldPerformHapticFeedback() {
        return true;
    }
}
