package com.android.server.appop;

/* JADX INFO: loaded from: classes.dex */
public class AudioRestrictionManager {
    static final android.util.SparseArray<android.util.SparseBooleanArray> CAMERA_AUDIO_RESTRICTIONS;
    static final java.lang.String TAG = "AudioRestriction";
    final android.util.SparseArray<android.util.SparseArray<com.android.server.appop.AudioRestrictionManager.Restriction>> mZenModeAudioRestrictions = new android.util.SparseArray<>();
    int mCameraAudioRestriction = 0;

    static {
        android.util.SparseBooleanArray audioMutedUsages = new android.util.SparseBooleanArray();
        android.util.SparseBooleanArray vibrationMutedUsages = new android.util.SparseBooleanArray();
        for (int usage : android.media.AudioAttributes.SDK_USAGES.toArray()) {
            int suppressionBehavior = android.media.AudioAttributes.SUPPRESSIBLE_USAGES.get(usage);
            if (suppressionBehavior == 1 || suppressionBehavior == 2 || suppressionBehavior == 4) {
                audioMutedUsages.append(usage, true);
                vibrationMutedUsages.append(usage, true);
            } else if (suppressionBehavior != 5 && suppressionBehavior != 6 && suppressionBehavior != 3) {
                android.util.Slog.e(TAG, "Unknown audio suppression behavior" + suppressionBehavior);
            }
        }
        CAMERA_AUDIO_RESTRICTIONS = new android.util.SparseArray<>();
        CAMERA_AUDIO_RESTRICTIONS.append(28, audioMutedUsages);
        CAMERA_AUDIO_RESTRICTIONS.append(3, vibrationMutedUsages);
    }

    private static final class Restriction {
        private static final android.util.ArraySet<java.lang.String> NO_EXCEPTIONS = new android.util.ArraySet<>();
        android.util.ArraySet<java.lang.String> exceptionPackages;
        int mode;

        private Restriction() {
            this.exceptionPackages = NO_EXCEPTIONS;
        }
    }

    public int checkAudioOperation(int code, int usage, int uid, java.lang.String packageName) {
        android.util.SparseBooleanArray mutedUsages;
        synchronized (this) {
            if (this.mCameraAudioRestriction != 0) {
                android.util.Slog.d(TAG, "cameraAudioRestriction = " + this.mCameraAudioRestriction);
                if ((code == 3 || (code == 28 && this.mCameraAudioRestriction == 3)) && (mutedUsages = CAMERA_AUDIO_RESTRICTIONS.get(code)) != null && mutedUsages.get(usage)) {
                    android.util.Slog.d(TAG, "mute usage=" + usage + "; pkg=" + packageName + "; code=" + code);
                    return 1;
                }
            }
            int mode = checkZenModeRestrictionLocked(code, usage, uid, packageName);
            if (mode != 0) {
                android.util.Slog.d(TAG, "zen mode restricted pkg=" + packageName + "; code=" + code);
                return mode;
            }
            return 0;
        }
    }

    private int checkZenModeRestrictionLocked(int code, int usage, int uid, java.lang.String packageName) {
        com.android.server.appop.AudioRestrictionManager.Restriction r;
        android.util.SparseArray<com.android.server.appop.AudioRestrictionManager.Restriction> usageRestrictions = this.mZenModeAudioRestrictions.get(code);
        if (usageRestrictions != null && (r = usageRestrictions.get(usage)) != null && !r.exceptionPackages.contains(packageName)) {
            return r.mode;
        }
        return 0;
    }

    public void setZenModeAudioRestriction(int code, int usage, int uid, int mode, java.lang.String[] exceptionPackages) {
        synchronized (this) {
            android.util.SparseArray<com.android.server.appop.AudioRestrictionManager.Restriction> usageRestrictions = this.mZenModeAudioRestrictions.get(code);
            if (usageRestrictions == null) {
                usageRestrictions = new android.util.SparseArray<>();
                this.mZenModeAudioRestrictions.put(code, usageRestrictions);
            }
            usageRestrictions.remove(usage);
            if (mode != 0) {
                com.android.server.appop.AudioRestrictionManager.Restriction r = new com.android.server.appop.AudioRestrictionManager.Restriction();
                r.mode = mode;
                if (exceptionPackages != null) {
                    int N = exceptionPackages.length;
                    r.exceptionPackages = new android.util.ArraySet<>(N);
                    for (java.lang.String pkg : exceptionPackages) {
                        if (pkg != null) {
                            r.exceptionPackages.add(pkg.trim());
                        }
                    }
                }
                usageRestrictions.put(usage, r);
            }
        }
    }

    public void setCameraAudioRestriction(int mode) {
        synchronized (this) {
            this.mCameraAudioRestriction = mode;
        }
    }

    public boolean hasActiveRestrictions() {
        boolean hasActiveRestrictions;
        synchronized (this) {
            hasActiveRestrictions = this.mZenModeAudioRestrictions.size() > 0 || this.mCameraAudioRestriction != 0;
        }
        return hasActiveRestrictions;
    }

    public boolean dump(java.io.PrintWriter pw) {
        boolean printedHeader = false;
        boolean needSep = hasActiveRestrictions();
        synchronized (this) {
            for (int o = 0; o < this.mZenModeAudioRestrictions.size(); o++) {
                java.lang.String op = android.app.AppOpsManager.opToName(this.mZenModeAudioRestrictions.keyAt(o));
                android.util.SparseArray<com.android.server.appop.AudioRestrictionManager.Restriction> restrictions = this.mZenModeAudioRestrictions.valueAt(o);
                for (int i = 0; i < restrictions.size(); i++) {
                    if (!printedHeader) {
                        pw.println("  Zen Mode Audio Restrictions:");
                        printedHeader = true;
                    }
                    int usage = restrictions.keyAt(i);
                    pw.print("    ");
                    pw.print(op);
                    pw.print(" usage=");
                    pw.print(android.media.AudioAttributes.usageToString(usage));
                    com.android.server.appop.AudioRestrictionManager.Restriction r = restrictions.valueAt(i);
                    pw.print(": mode=");
                    pw.println(android.app.AppOpsManager.modeToName(r.mode));
                    if (!r.exceptionPackages.isEmpty()) {
                        pw.println("      Exceptions:");
                        for (int j = 0; j < r.exceptionPackages.size(); j++) {
                            pw.print("        ");
                            pw.println(r.exceptionPackages.valueAt(j));
                        }
                    }
                }
            }
            int o2 = this.mCameraAudioRestriction;
            if (o2 != 0) {
                pw.println("  Camera Audio Restriction Mode: " + cameraRestrictionModeToName(this.mCameraAudioRestriction));
            }
        }
        return needSep;
    }

    private static java.lang.String cameraRestrictionModeToName(int mode) {
        switch (mode) {
            case 0:
                return com.android.server.input.KeyboardMetricsCollector.DEFAULT_LANGUAGE_TAG;
            case 1:
                return "MuteVibration";
            case 2:
            default:
                return "Unknown";
            case 3:
                return "MuteVibrationAndSound";
        }
    }
}
