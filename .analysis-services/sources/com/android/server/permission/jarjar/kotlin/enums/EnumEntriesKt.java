package com.android.server.permission.jarjar.kotlin.enums;

/* JADX INFO: compiled from: EnumEntries.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u001c\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0010\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\u001a!\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0010\b\u0000\u0010\u0002\u0018\u0001*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u0087\b\u001a2\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0001\"\u000e\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\u0012\u0010\u0005\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00040\u00070\u0006H\u0001\u001a1\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0001\"\u000e\b\u0000\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u00032\f\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\u00040\u0007H\u0001¢\u0006\u0002\u0010\t¨\u0006\n"}, d2 = {"enumEntries", "Lkotlin/enums/EnumEntries;", "T", "", "E", "entriesProvider", "Lkotlin/Function0;", "", "entries", "([Ljava/lang/Enum;)Lkotlin/enums/EnumEntries;", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class EnumEntriesKt {
    public static final /* synthetic */ <T extends java.lang.Enum<T>> com.android.server.permission.jarjar.kotlin.enums.EnumEntries<T> enumEntries() {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.reifiedOperationMarker(5, "T");
        return enumEntries(new java.lang.Enum[0]);
    }

    public static final <E extends java.lang.Enum<E>> com.android.server.permission.jarjar.kotlin.enums.EnumEntries<E> enumEntries(com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<E[]> function0) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function0, "entriesProvider");
        return new com.android.server.permission.jarjar.kotlin.enums.EnumEntriesList(function0.invoke());
    }

    public static final <E extends java.lang.Enum<E>> com.android.server.permission.jarjar.kotlin.enums.EnumEntries<E> enumEntries(E[] eArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(eArr, "entries");
        return new com.android.server.permission.jarjar.kotlin.enums.EnumEntriesList(eArr);
    }
}
