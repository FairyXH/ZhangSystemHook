package com.android.server.permission.jarjar.kotlin.random;

/* JADX INFO: compiled from: PlatformRandom.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\b \u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bH\u0016J\b\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\rH\u0016J\b\u0010\u000f\u001a\u00020\u0010H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\b\u0010\u0013\u001a\u00020\bH\u0016J\u0010\u0010\u0013\u001a\u00020\b2\u0006\u0010\u0014\u001a\u00020\bH\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016R\u0012\u0010\u0003\u001a\u00020\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0017"}, d2 = {"Lkotlin/random/AbstractPlatformRandom;", "Lkotlin/random/Random;", "()V", "impl", "Ljava/util/Random;", "getImpl", "()Ljava/util/Random;", "nextBits", "", "bitCount", "nextBoolean", "", "nextBytes", "", "array", "nextDouble", "", "nextFloat", "", "nextInt", "until", "nextLong", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public abstract class AbstractPlatformRandom extends com.android.server.permission.jarjar.kotlin.random.Random {
    public abstract java.util.Random getImpl();

    @Override // com.android.server.permission.jarjar.kotlin.random.Random
    public int nextBits(int bitCount) {
        return com.android.server.permission.jarjar.kotlin.random.RandomKt.takeUpperBits(getImpl().nextInt(), bitCount);
    }

    @Override // com.android.server.permission.jarjar.kotlin.random.Random
    public int nextInt() {
        return getImpl().nextInt();
    }

    @Override // com.android.server.permission.jarjar.kotlin.random.Random
    public int nextInt(int until) {
        return getImpl().nextInt(until);
    }

    @Override // com.android.server.permission.jarjar.kotlin.random.Random
    public long nextLong() {
        return getImpl().nextLong();
    }

    @Override // com.android.server.permission.jarjar.kotlin.random.Random
    public boolean nextBoolean() {
        return getImpl().nextBoolean();
    }

    @Override // com.android.server.permission.jarjar.kotlin.random.Random
    public double nextDouble() {
        return getImpl().nextDouble();
    }

    @Override // com.android.server.permission.jarjar.kotlin.random.Random
    public float nextFloat() {
        return getImpl().nextFloat();
    }

    @Override // com.android.server.permission.jarjar.kotlin.random.Random
    public byte[] nextBytes(byte[] array) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(array, "array");
        getImpl().nextBytes(array);
        return array;
    }
}
