package kotlin.text;

/* JADX INFO: compiled from: Regex.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0005HÆ\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0011\u001a\u00020\u0012HÖ\u0001J\t\u0010\u0013\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0014"}, d2 = {"Lkotlin/text/MatchGroup;", "", "value", "", "range", "Lkotlin/ranges/IntRange;", "(Ljava/lang/String;Lkotlin/ranges/IntRange;)V", "getRange", "()Lkotlin/ranges/IntRange;", "getValue", "()Ljava/lang/String;", "component1", "component2", "copy", "equals", "", "other", com.android.server.oplus.osense.OsenseConstants.KEY_INTEGER_HASH, "", "toString", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final /* data */ class MatchGroup {
    private final kotlin.ranges.IntRange range;
    private final java.lang.String value;

    public static /* synthetic */ kotlin.text.MatchGroup copy$default(kotlin.text.MatchGroup matchGroup, java.lang.String str, kotlin.ranges.IntRange intRange, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            str = matchGroup.value;
        }
        if ((i & 2) != 0) {
            intRange = matchGroup.range;
        }
        return matchGroup.copy(str, intRange);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final java.lang.String getValue() {
        return this.value;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final kotlin.ranges.IntRange getRange() {
        return this.range;
    }

    public final kotlin.text.MatchGroup copy(java.lang.String value, kotlin.ranges.IntRange range) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        return new kotlin.text.MatchGroup(value, range);
    }

    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof kotlin.text.MatchGroup)) {
            return false;
        }
        kotlin.text.MatchGroup matchGroup = (kotlin.text.MatchGroup) other;
        return kotlin.jvm.internal.Intrinsics.areEqual(this.value, matchGroup.value) && kotlin.jvm.internal.Intrinsics.areEqual(this.range, matchGroup.range);
    }

    public int hashCode() {
        return (this.value.hashCode() * 31) + this.range.hashCode();
    }

    public java.lang.String toString() {
        return "MatchGroup(value=" + this.value + ", range=" + this.range + ')';
    }

    public MatchGroup(java.lang.String value, kotlin.ranges.IntRange range) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(value, "value");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(range, "range");
        this.value = value;
        this.range = range;
    }

    public final kotlin.ranges.IntRange getRange() {
        return this.range;
    }

    public final java.lang.String getValue() {
        return this.value;
    }
}
