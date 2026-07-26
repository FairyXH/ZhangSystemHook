package com.android.server.permission.jarjar.kotlin;

/* JADX INFO: compiled from: Lazy.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0018\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001f\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002¢\u0006\u0002\u0010\u0004\u001a4\u0010\u0005\u001a\u0002H\u0002\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\n\u0010\b\u001a\u0006\u0012\u0002\b\u00030\tH\u0087\n¢\u0006\u0002\u0010\n¨\u0006\u000b"}, d2 = {"lazyOf", "Lkotlin/Lazy;", "T", "value", "(Ljava/lang/Object;)Lkotlin/Lazy;", "getValue", "thisRef", "", "property", "Lkotlin/reflect/KProperty;", "(Lkotlin/Lazy;Ljava/lang/Object;Lkotlin/reflect/KProperty;)Ljava/lang/Object;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/LazyKt")
class LazyKt__LazyKt extends com.android.server.permission.jarjar.kotlin.LazyKt__LazyJVMKt {
    public static final <T> com.android.server.permission.jarjar.kotlin.Lazy<T> lazyOf(T t) {
        return new com.android.server.permission.jarjar.kotlin.InitializedLazyImpl(t);
    }

    private static final <T> T getValue(com.android.server.permission.jarjar.kotlin.Lazy<? extends T> lazy, java.lang.Object thisRef, com.android.server.permission.jarjar.kotlin.reflect.KProperty<?> kProperty) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(lazy, "<this>");
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(kProperty, "property");
        return lazy.getValue();
    }
}
