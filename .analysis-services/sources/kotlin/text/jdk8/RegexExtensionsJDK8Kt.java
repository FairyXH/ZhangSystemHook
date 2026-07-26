package kotlin.text.jdk8;

/* JADX INFO: compiled from: RegexExtensions.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\u0017\u0010\u0000\u001a\u0004\u0018\u00010\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\u0002¨\u0006\u0005"}, d2 = {"get", "Lkotlin/text/MatchGroup;", "Lkotlin/text/MatchGroupCollection;", "name", "", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, pn = "kotlin.text", xi = 48)
public final class RegexExtensionsJDK8Kt {
    public static final kotlin.text.MatchGroup get(kotlin.text.MatchGroupCollection $this$get, java.lang.String name) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$get, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(name, "name");
        kotlin.text.MatchNamedGroupCollection namedGroups = $this$get instanceof kotlin.text.MatchNamedGroupCollection ? (kotlin.text.MatchNamedGroupCollection) $this$get : null;
        if (namedGroups == null) {
            throw new java.lang.UnsupportedOperationException("Retrieving groups by name is not supported on this platform.");
        }
        return namedGroups.get(name);
    }
}
