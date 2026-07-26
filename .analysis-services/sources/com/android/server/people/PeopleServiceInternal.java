package com.android.server.people;

/* JADX INFO: loaded from: classes2.dex */
public abstract class PeopleServiceInternal extends android.service.appprediction.IPredictionService.Stub {
    public abstract byte[] getBackupPayload(int i);

    public abstract void pruneDataForUser(int i, android.os.CancellationSignal cancellationSignal);

    public abstract void restore(int i, byte[] bArr);
}
