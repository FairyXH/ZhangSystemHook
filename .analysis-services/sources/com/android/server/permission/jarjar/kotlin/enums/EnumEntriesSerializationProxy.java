package com.android.server.permission.jarjar.kotlin.enums;

/* JADX INFO: compiled from: EnumEntriesSerializationProxy.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0000\u0018\u0000 \f*\u000e\b\u0000\u0010\u0001*\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004:\u0001\fB\u0013\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006¢\u0006\u0002\u0010\u0007J\b\u0010\n\u001a\u00020\u000bH\u0002R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00028\u00000\tX\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lkotlin/enums/EnumEntriesSerializationProxy;", "E", "", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "entries", "", "([Ljava/lang/Enum;)V", "c", "Ljava/lang/Class;", "readResolve", "", "Companion", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class EnumEntriesSerializationProxy<E extends java.lang.Enum<E>> implements java.io.Serializable {
    private static final com.android.server.permission.jarjar.kotlin.enums.EnumEntriesSerializationProxy.Companion Companion = new com.android.server.permission.jarjar.kotlin.enums.EnumEntriesSerializationProxy.Companion(null);
    private static final long serialVersionUID = 0;
    private final java.lang.Class<E> c;

    public EnumEntriesSerializationProxy(E[] eArr) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(eArr, "entries");
        java.lang.Class<E> cls = (java.lang.Class<E>) eArr.getClass().getComponentType();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNull(cls);
        this.c = cls;
    }

    /* JADX INFO: compiled from: EnumEntriesSerializationProxy.kt */
    @com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0082\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000¨\u0006\u0005"}, d2 = {"Lkotlin/enums/EnumEntriesSerializationProxy$Companion;", "", "()V", "serialVersionUID", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    private static final class Companion {
        public /* synthetic */ Companion(com.android.server.permission.jarjar.kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private final java.lang.Object readResolve() {
        E[] enumConstants = this.c.getEnumConstants();
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(enumConstants, "getEnumConstants(...)");
        return com.android.server.permission.jarjar.kotlin.enums.EnumEntriesKt.enumEntries(enumConstants);
    }
}
