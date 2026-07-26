package com.android.server.biometrics.sensors;

/* JADX INFO: loaded from: classes.dex */
public interface BiometricUtils<T extends android.hardware.biometrics.BiometricAuthenticator.Identifier> {
    void addBiometricForUser(android.content.Context context, int i, T t);

    java.util.List<T> getBiometricsForUser(android.content.Context context, int i);

    java.lang.CharSequence getUniqueName(android.content.Context context, int i);

    boolean isInvalidationInProgress(android.content.Context context, int i);

    void removeBiometricForUser(android.content.Context context, int i, int i2);

    void renameBiometricForUser(android.content.Context context, int i, int i2, java.lang.CharSequence charSequence);

    void setInvalidationInProgress(android.content.Context context, int i, boolean z);
}
