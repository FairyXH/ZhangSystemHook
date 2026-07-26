package com.android.server.permission.jarjar.kotlin.streams.jdk8;

/* JADX INFO: compiled from: Streams.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000.\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0006\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\u001a\u0012\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\u0007\u001a\u0012\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00040\u0001*\u00020\u0005H\u0007\u001a\u0012\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00060\u0001*\u00020\u0007H\u0007\u001a\u001e\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\b0\u0001\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\tH\u0007\u001a\u001e\u0010\n\u001a\b\u0012\u0004\u0012\u0002H\b0\t\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\u0001H\u0007\u001a\u0012\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00020\f*\u00020\u0003H\u0007\u001a\u0012\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\f*\u00020\u0005H\u0007\u001a\u0012\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00060\f*\u00020\u0007H\u0007\u001a\u001e\u0010\u000b\u001a\b\u0012\u0004\u0012\u0002H\b0\f\"\u0004\b\u0000\u0010\b*\b\u0012\u0004\u0012\u0002H\b0\tH\u0007¨\u0006\r"}, d2 = {"asSequence", "Lkotlin/sequences/Sequence;", "", "Ljava/util/stream/DoubleStream;", "", "Ljava/util/stream/IntStream;", "", "Ljava/util/stream/LongStream;", "T", "Ljava/util/stream/Stream;", "asStream", "toList", "", "kotlin-stdlib-jdk8"}, k = 2, mv = {1, 9, 0}, pn = "com.android.server.permission.jarjar.kotlin.streams", xi = 48)
public final class StreamsKt {
    public static final <T> com.android.server.permission.jarjar.kotlin.sequences.Sequence<T> asSequence(final java.util.stream.Stream<T> stream) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(stream, "<this>");
        return new com.android.server.permission.jarjar.kotlin.sequences.Sequence<T>() { // from class: com.android.server.permission.jarjar.kotlin.streams.jdk8.StreamsKt$asSequence$$inlined$Sequence$1
            @Override // com.android.server.permission.jarjar.kotlin.sequences.Sequence
            public java.util.Iterator<T> iterator() {
                java.util.Iterator<T> it = stream.iterator();
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
                return it;
            }
        };
    }

    public static final com.android.server.permission.jarjar.kotlin.sequences.Sequence<java.lang.Integer> asSequence(final java.util.stream.IntStream $this$asSequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asSequence, "<this>");
        return new com.android.server.permission.jarjar.kotlin.sequences.Sequence<java.lang.Integer>() { // from class: com.android.server.permission.jarjar.kotlin.streams.jdk8.StreamsKt$asSequence$$inlined$Sequence$2
            @Override // com.android.server.permission.jarjar.kotlin.sequences.Sequence
            public java.util.Iterator<java.lang.Integer> iterator() {
                java.util.Iterator<java.lang.Integer> it = $this$asSequence.iterator();
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
                return it;
            }
        };
    }

    public static final com.android.server.permission.jarjar.kotlin.sequences.Sequence<java.lang.Long> asSequence(final java.util.stream.LongStream $this$asSequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asSequence, "<this>");
        return new com.android.server.permission.jarjar.kotlin.sequences.Sequence<java.lang.Long>() { // from class: com.android.server.permission.jarjar.kotlin.streams.jdk8.StreamsKt$asSequence$$inlined$Sequence$3
            @Override // com.android.server.permission.jarjar.kotlin.sequences.Sequence
            public java.util.Iterator<java.lang.Long> iterator() {
                java.util.Iterator<java.lang.Long> it = $this$asSequence.iterator();
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
                return it;
            }
        };
    }

    public static final com.android.server.permission.jarjar.kotlin.sequences.Sequence<java.lang.Double> asSequence(final java.util.stream.DoubleStream $this$asSequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$asSequence, "<this>");
        return new com.android.server.permission.jarjar.kotlin.sequences.Sequence<java.lang.Double>() { // from class: com.android.server.permission.jarjar.kotlin.streams.jdk8.StreamsKt$asSequence$$inlined$Sequence$4
            @Override // com.android.server.permission.jarjar.kotlin.sequences.Sequence
            public java.util.Iterator<java.lang.Double> iterator() {
                java.util.Iterator<java.lang.Double> it = $this$asSequence.iterator();
                com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(it, "iterator(...)");
                return it;
            }
        };
    }

    public static final <T> java.util.stream.Stream<T> asStream(final com.android.server.permission.jarjar.kotlin.sequences.Sequence<? extends T> sequence) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(sequence, "<this>");
        java.util.stream.Stream<T> stream = java.util.stream.StreamSupport.stream(new java.util.function.Supplier() { // from class: com.android.server.permission.jarjar.kotlin.streams.jdk8.StreamsKt$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final java.lang.Object get() {
                return com.android.server.permission.jarjar.kotlin.streams.jdk8.StreamsKt.asStream$lambda$4(sequence);
            }
        }, 16, false);
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(stream, "stream(...)");
        return stream;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final java.util.Spliterator asStream$lambda$4(com.android.server.permission.jarjar.kotlin.sequences.Sequence $this_asStream) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this_asStream, "$this_asStream");
        return java.util.Spliterators.spliteratorUnknownSize($this_asStream.iterator(), 16);
    }

    public static final <T> java.util.List<T> toList(java.util.stream.Stream<T> stream) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(stream, "<this>");
        java.lang.Object objCollect = stream.collect(java.util.stream.Collectors.toList());
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(objCollect, "collect(...)");
        return (java.util.List) objCollect;
    }

    public static final java.util.List<java.lang.Integer> toList(java.util.stream.IntStream $this$toList) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toList, "<this>");
        int[] array = $this$toList.toArray();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(array, "toArray(...)");
        return com.android.server.permission.jarjar.kotlin.collections.ArraysKt.asList(array);
    }

    public static final java.util.List<java.lang.Long> toList(java.util.stream.LongStream $this$toList) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toList, "<this>");
        long[] array = $this$toList.toArray();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(array, "toArray(...)");
        return com.android.server.permission.jarjar.kotlin.collections.ArraysKt.asList(array);
    }

    public static final java.util.List<java.lang.Double> toList(java.util.stream.DoubleStream $this$toList) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toList, "<this>");
        double[] array = $this$toList.toArray();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(array, "toArray(...)");
        return com.android.server.permission.jarjar.kotlin.collections.ArraysKt.asList(array);
    }
}
