package com.android.server.permission.jarjar.kotlin.text;

/* JADX INFO: compiled from: CharDirectionality.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020\u00030\u0001H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "", "Lkotlin/text/CharDirectionality;", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
final class CharDirectionality$Companion$directionalityMap$2 extends com.android.server.permission.jarjar.kotlin.jvm.internal.Lambda implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<java.util.Map<java.lang.Integer, ? extends com.android.server.permission.jarjar.kotlin.text.CharDirectionality>> {
    public static final com.android.server.permission.jarjar.kotlin.text.CharDirectionality$Companion$directionalityMap$2 INSTANCE = new com.android.server.permission.jarjar.kotlin.text.CharDirectionality$Companion$directionalityMap$2();

    CharDirectionality$Companion$directionalityMap$2() {
        super(0);
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function0
    public final java.util.Map<java.lang.Integer, ? extends com.android.server.permission.jarjar.kotlin.text.CharDirectionality> invoke() {
        java.lang.Iterable $this$associateBy$iv = com.android.server.permission.jarjar.kotlin.text.CharDirectionality.getEntries();
        int capacity$iv = com.android.server.permission.jarjar.kotlin.ranges.RangesKt.coerceAtLeast(com.android.server.permission.jarjar.kotlin.collections.MapsKt.mapCapacity(com.android.server.permission.jarjar.kotlin.collections.CollectionsKt.collectionSizeOrDefault($this$associateBy$iv, 10)), 16);
        java.util.Map destination$iv$iv = new java.util.LinkedHashMap(capacity$iv);
        for (com.android.server.permission.jarjar.kotlin.text.CharDirectionality charDirectionality : $this$associateBy$iv) {
            com.android.server.permission.jarjar.kotlin.text.CharDirectionality it = charDirectionality;
            destination$iv$iv.put(java.lang.Integer.valueOf(it.getValue()), charDirectionality);
        }
        return destination$iv$iv;
    }
}
