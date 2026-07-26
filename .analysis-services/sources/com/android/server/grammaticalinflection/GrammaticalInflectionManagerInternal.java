package com.android.server.grammaticalinflection;

/* JADX INFO: loaded from: classes2.dex */
public abstract class GrammaticalInflectionManagerInternal {
    public abstract void applyRestoredSystemPayload(byte[] bArr, int i);

    public abstract boolean canGetSystemGrammaticalGender(int i);

    public abstract byte[] getBackupPayload(int i);

    public abstract int getGrammaticalGenderFromDeveloperSettings();

    public abstract byte[] getSystemBackupPayload(int i);

    public abstract int getSystemGrammaticalGender(int i);

    public abstract int mergedFinalSystemGrammaticalGender();

    public abstract void stageAndApplyRestoredPayload(byte[] bArr, int i);
}
