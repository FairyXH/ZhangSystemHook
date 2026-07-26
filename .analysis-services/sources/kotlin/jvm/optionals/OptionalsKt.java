package kotlin.jvm.optionals;

/* JADX INFO: compiled from: Optionals.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u00000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u001f\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\n\u0002\u0010\"\n\u0000\u001a$\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0003*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0004H\u0007\u001a,\u0010\u0005\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\f\u0012\b\b\u0001\u0012\u0004\b\u0002H\u00020\u00042\u0006\u0010\u0006\u001a\u0002H\u0002H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a6\u0010\b\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\f\u0012\b\b\u0001\u0012\u0004\b\u0002H\u00020\u00042\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\tH\u0087\bø\u0001\u0001ø\u0001\u0000¢\u0006\u0002\u0010\n\u001a#\u0010\u000b\u001a\u0004\u0018\u0001H\u0002\"\b\b\u0000\u0010\u0002*\u00020\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u0007¢\u0006\u0002\u0010\f\u001a;\u0010\r\u001a\u0002H\u000e\"\b\b\u0000\u0010\u0002*\u00020\u0003\"\u0010\b\u0001\u0010\u000e*\n\u0012\u0006\b\u0000\u0012\u0002H\u00020\u000f*\b\u0012\u0004\u0012\u0002H\u00020\u00042\u0006\u0010\u0010\u001a\u0002H\u000eH\u0007¢\u0006\u0002\u0010\u0011\u001a$\u0010\u0012\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0013\"\b\b\u0000\u0010\u0002*\u00020\u0003*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0004H\u0007\u001a$\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0015\"\b\b\u0000\u0010\u0002*\u00020\u0003*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0004H\u0007\u0082\u0002\u000b\n\u0002\b9\n\u0005\b\u009920\u0001¨\u0006\u0016"}, d2 = {"asSequence", "Lkotlin/sequences/Sequence;", "T", "", "Ljava/util/Optional;", "getOrDefault", "defaultValue", "(Ljava/util/Optional;Ljava/lang/Object;)Ljava/lang/Object;", "getOrElse", "Lkotlin/Function0;", "(Ljava/util/Optional;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "getOrNull", "(Ljava/util/Optional;)Ljava/lang/Object;", "toCollection", "C", "", "destination", "(Ljava/util/Optional;Ljava/util/Collection;)Ljava/util/Collection;", "toList", "", "toSet", "", "kotlin-stdlib-jdk8"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class OptionalsKt {
    public static final <T> T getOrNull(java.util.Optional<T> optional) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(optional, "<this>");
        return optional.orElse(null);
    }

    public static final <T> T getOrDefault(java.util.Optional<? extends T> optional, T t) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(optional, "<this>");
        return optional.isPresent() ? optional.get() : t;
    }

    public static final <T> T getOrElse(java.util.Optional<? extends T> optional, kotlin.jvm.functions.Function0<? extends T> defaultValue) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(optional, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(defaultValue, "defaultValue");
        return optional.isPresent() ? optional.get() : defaultValue.invoke();
    }

    public static final <T, C extends java.util.Collection<? super T>> C toCollection(java.util.Optional<T> optional, C destination) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(optional, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        if (optional.isPresent()) {
            T t = optional.get();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(t, "get(...)");
            destination.add(t);
        }
        return destination;
    }

    public static final <T> java.util.List<T> toList(java.util.Optional<? extends T> optional) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(optional, "<this>");
        return optional.isPresent() ? kotlin.collections.CollectionsKt.listOf(optional.get()) : kotlin.collections.CollectionsKt.emptyList();
    }

    public static final <T> java.util.Set<T> toSet(java.util.Optional<? extends T> optional) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(optional, "<this>");
        return optional.isPresent() ? kotlin.collections.SetsKt.setOf(optional.get()) : kotlin.collections.SetsKt.emptySet();
    }

    public static final <T> kotlin.sequences.Sequence<T> asSequence(java.util.Optional<? extends T> optional) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(optional, "<this>");
        return optional.isPresent() ? kotlin.sequences.SequencesKt.sequenceOf(optional.get()) : kotlin.sequences.SequencesKt.emptySequence();
    }
}
