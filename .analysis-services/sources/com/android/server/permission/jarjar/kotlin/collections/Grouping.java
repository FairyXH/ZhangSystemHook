package com.android.server.permission.jarjar.kotlin.collections;

/* JADX INFO: compiled from: Grouping.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0010(\n\u0000\bg\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0006\b\u0001\u0010\u0002 \u00012\u00020\u0003J\u0015\u0010\u0004\u001a\u00028\u00012\u0006\u0010\u0005\u001a\u00028\u0000H&¢\u0006\u0002\u0010\u0006J\u000e\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bH&¨\u0006\t"}, d2 = {"Lkotlin/collections/Grouping;", "T", "K", "", "keyOf", "element", "(Ljava/lang/Object;)Ljava/lang/Object;", "sourceIterator", "", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public interface Grouping<T, K> {
    K keyOf(T t);

    java.util.Iterator<T> sourceIterator();
}
