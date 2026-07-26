package com.android.server.permission.jarjar.kotlin.random;

/* JADX INFO: compiled from: PlatformRandom.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\t\u0010\u0000\u001a\u00020\u0001H\u0081\b\u001a\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0000\u001a\f\u0010\u0007\u001a\u00020\b*\u00020\u0001H\u0007\u001a\f\u0010\t\u001a\u00020\u0001*\u00020\bH\u0007¨\u0006\n"}, d2 = {"defaultPlatformRandom", "Lkotlin/random/Random;", "doubleFromParts", "", "hi26", "", "low27", "asJavaRandom", "Ljava/util/Random;", "asKotlinRandom", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class PlatformRandomKt {
    public static final java.util.Random asJavaRandom(com.android.server.permission.jarjar.kotlin.random.Random $this$asJavaRandom) {
        java.util.Random impl;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asJavaRandom, "<this>");
        com.android.server.permission.jarjar.kotlin.random.AbstractPlatformRandom abstractPlatformRandom = $this$asJavaRandom instanceof com.android.server.permission.jarjar.kotlin.random.AbstractPlatformRandom ? (com.android.server.permission.jarjar.kotlin.random.AbstractPlatformRandom) $this$asJavaRandom : null;
        return (abstractPlatformRandom == null || (impl = abstractPlatformRandom.getImpl()) == null) ? new com.android.server.permission.jarjar.kotlin.random.KotlinRandom($this$asJavaRandom) : impl;
    }

    public static final com.android.server.permission.jarjar.kotlin.random.Random asKotlinRandom(java.util.Random $this$asKotlinRandom) {
        com.android.server.permission.jarjar.kotlin.random.Random impl;
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asKotlinRandom, "<this>");
        com.android.server.permission.jarjar.kotlin.random.KotlinRandom kotlinRandom = $this$asKotlinRandom instanceof com.android.server.permission.jarjar.kotlin.random.KotlinRandom ? (com.android.server.permission.jarjar.kotlin.random.KotlinRandom) $this$asKotlinRandom : null;
        return (kotlinRandom == null || (impl = kotlinRandom.getImpl()) == null) ? new com.android.server.permission.jarjar.kotlin.random.PlatformRandom($this$asKotlinRandom) : impl;
    }

    private static final com.android.server.permission.jarjar.kotlin.random.Random defaultPlatformRandom() {
        return com.android.server.permission.jarjar.kotlin.internal.PlatformImplementationsKt.IMPLEMENTATIONS.defaultPlatformRandom();
    }

    public static final double doubleFromParts(int hi26, int low27) {
        return ((((long) hi26) << 27) + ((long) low27)) / 9.007199254740992E15d;
    }
}
