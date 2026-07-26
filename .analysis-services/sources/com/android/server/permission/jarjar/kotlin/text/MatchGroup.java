package com.android.server.permission.jarjar.kotlin.text;

/* JADX INFO: compiled from: Regex.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0014"}, d2 = {"Lkotlin/text/MatchGroup;", "", "value", "", "range", "Lkotlin/ranges/IntRange;", "(Ljava/lang/String;Lkotlin/ranges/IntRange;)V", "getRange", "()Lkotlin/ranges/IntRange;", "getValue", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MatchGroup {
    private final com.android.server.permission.jarjar.kotlin.ranges.IntRange range;
    private final java.lang.String value;

    public static /* synthetic */ com.android.server.permission.jarjar.kotlin.text.MatchGroup copy$default(com.android.server.permission.jarjar.kotlin.text.MatchGroup matchGroup, java.lang.String str, com.android.server.permission.jarjar.kotlin.ranges.IntRange intRange, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            str = matchGroup.value;
        }
        if ((i & 2) != 0) {
            intRange = matchGroup.range;
        }
        return matchGroup.copy(str, intRange);
    }

    public final java.lang.String component1() {
        return this.value;
    }

    public final com.android.server.permission.jarjar.kotlin.ranges.IntRange component2() {
        return this.range;
    }

    public final com.android.server.permission.jarjar.kotlin.text.MatchGroup copy(java.lang.String str, com.android.server.permission.jarjar.kotlin.ranges.IntRange intRange) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(str, "value");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(intRange, "range");
        return new com.android.server.permission.jarjar.kotlin.text.MatchGroup(str, intRange);
    }

    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof com.android.server.permission.jarjar.kotlin.text.MatchGroup)) {
            return false;
        }
        com.android.server.permission.jarjar.kotlin.text.MatchGroup matchGroup = (com.android.server.permission.jarjar.kotlin.text.MatchGroup) obj;
        return com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.value, matchGroup.value) && com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.areEqual(this.range, matchGroup.range);
    }

    public int hashCode() {
        return (this.value.hashCode() * 31) + this.range.hashCode();
    }

    public java.lang.String toString() {
        return "MatchGroup(value=" + this.value + ", range=" + this.range + ')';
    }

    public MatchGroup(java.lang.String value, com.android.server.permission.jarjar.kotlin.ranges.IntRange range) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        this.value = value;
        this.range = range;
    }

    public final com.android.server.permission.jarjar.kotlin.ranges.IntRange getRange() {
        return this.range;
    }

    public final java.lang.String getValue() {
        return this.value;
    }
}
