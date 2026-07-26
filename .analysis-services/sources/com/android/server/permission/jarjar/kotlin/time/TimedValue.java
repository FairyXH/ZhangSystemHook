package com.android.server.permission.jarjar.kotlin.time;

/* JADX INFO: compiled from: measureTime.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002B\u0015\u0012\u0006\u0010\u0003\u001a\u00028\u0000\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\r\u001a\u00028\u0000HÆ\u0003¢\u0006\u0002\u0010\u000bJ\u0013\u0010\u000e\u001a\u00020\u0005HÆ\u0003ø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\bJ*\u0010\u0010\u001a\b\u0012\u0004\u0012\u00028\u00000\u00002\b\b\u0002\u0010\u0003\u001a\u00028\u00002\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001¢\u0006\u0004\b\u0011\u0010\u0012J\u0013\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0002HÖ\u0003J\t\u0010\u0016\u001a\u00020\u0017HÖ\u0001J\t\u0010\u0018\u001a\u00020\u0019HÖ\u0001R\u0016\u0010\u0004\u001a\u00020\u0005ø\u0001\u0000¢\u0006\n\n\u0002\u0010\t\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0003\u001a\u00028\u0000¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000b\u0082\u0002\u0004\n\u0002\b!¨\u0006\u001a"}, d2 = {"Lkotlin/time/TimedValue;", "T", "", "value", "duration", "Lkotlin/time/Duration;", "(Ljava/lang/Object;JLkotlin/jvm/internal/DefaultConstructorMarker;)V", "getDuration-UwyO8pc", "()J", "J", "getValue", "()Ljava/lang/Object;", "Ljava/lang/Object;", "component1", "component2", "component2-UwyO8pc", "copy", "copy-RFiDyg4", "(Ljava/lang/Object;J)Lkotlin/time/TimedValue;", "equals", "", "other", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class TimedValue<T> {
    private final long duration;
    private final T value;

    public /* synthetic */ TimedValue(java.lang.Object obj, long j, com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(obj, j);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX INFO: renamed from: copy-RFiDyg4$default, reason: not valid java name */
    public static /* synthetic */ com.android.server.permission.jarjar.kotlin.time.TimedValue m7572copyRFiDyg4$default(com.android.server.permission.jarjar.kotlin.time.TimedValue timedValue, java.lang.Object obj, long j, int i, java.lang.Object obj2) {
        if ((i & 1) != 0) {
            obj = timedValue.value;
        }
        if ((i & 2) != 0) {
            j = timedValue.duration;
        }
        return timedValue.m7574copyRFiDyg4(obj, j);
    }

    public final T component1() {
        return this.value;
    }

    /* JADX INFO: renamed from: component2-UwyO8pc, reason: not valid java name */
    public final long m7573component2UwyO8pc() {
        return this.duration;
    }

    /* JADX INFO: renamed from: copy-RFiDyg4, reason: not valid java name */
    public final com.android.server.permission.jarjar.kotlin.time.TimedValue<T> m7574copyRFiDyg4(T t, long j) {
        return new com.android.server.permission.jarjar.kotlin.time.TimedValue<>(t, j, null);
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof com.android.server.permission.jarjar.kotlin.time.TimedValue)) {
            return false;
        }
        com.android.server.permission.jarjar.kotlin.time.TimedValue timedValue = (com.android.server.permission.jarjar.kotlin.time.TimedValue) obj;
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.value, timedValue.value) && com.android.server.permission.jarjar.kotlin.time.Duration.m7419equalsimpl0(this.duration, timedValue.duration);
    }

    public int hashCode() {
        return ((this.value == null ? 0 : this.value.hashCode()) * 31) + com.android.server.permission.jarjar.kotlin.time.Duration.m7442hashCodeimpl(this.duration);
    }

    public java.lang.String toString() {
        return "TimedValue(value=" + this.value + ", duration=" + ((java.lang.Object) com.android.server.permission.jarjar.kotlin.time.Duration.m7463toStringimpl(this.duration)) + ')';
    }

    private TimedValue(T t, long duration) {
        this.value = t;
        this.duration = duration;
    }

    /* JADX INFO: renamed from: getDuration-UwyO8pc, reason: not valid java name */
    public final long m7575getDurationUwyO8pc() {
        return this.duration;
    }

    public final T getValue() {
        return this.value;
    }
}
