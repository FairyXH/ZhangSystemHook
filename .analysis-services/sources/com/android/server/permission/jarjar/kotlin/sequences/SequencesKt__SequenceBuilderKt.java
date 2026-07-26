package com.android.server.permission.jarjar.kotlin.sequences;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SequenceBuilder.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000:\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001aJ\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\n0\t\"\u0004\b\u0000\u0010\n2/\b\u0001\u0010\u000b\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\n0\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00100\f¢\u0006\u0002\b\u0011H\u0007¢\u0006\u0002\u0010\u0012\u001aJ\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\n0\u0014\"\u0004\b\u0000\u0010\n2/\b\u0001\u0010\u000b\u001a)\b\u0001\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\n0\r\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e\u0012\u0006\u0012\u0004\u0018\u00010\u00100\f¢\u0006\u0002\b\u0011H\u0007¢\u0006\u0002\u0010\u0015\"\u0012\u0010\u0000\u001a\u00060\u0001j\u0002`\u0002X\u0082T¢\u0006\u0002\n\u0000\"\u0012\u0010\u0003\u001a\u00060\u0001j\u0002`\u0002X\u0082T¢\u0006\u0002\n\u0000\"\u0012\u0010\u0004\u001a\u00060\u0001j\u0002`\u0002X\u0082T¢\u0006\u0002\n\u0000\"\u0012\u0010\u0005\u001a\u00060\u0001j\u0002`\u0002X\u0082T¢\u0006\u0002\n\u0000\"\u0012\u0010\u0006\u001a\u00060\u0001j\u0002`\u0002X\u0082T¢\u0006\u0002\n\u0000\"\u0012\u0010\u0007\u001a\u00060\u0001j\u0002`\u0002X\u0082T¢\u0006\u0002\n\u0000*\f\b\u0002\u0010\u0016\"\u00020\u00012\u00020\u0001¨\u0006\u0017"}, d2 = {"State_Done", "", "Lkotlin/sequences/State;", "State_Failed", "State_ManyNotReady", "State_ManyReady", "State_NotReady", "State_Ready", "iterator", "", "T", "block", "Lkotlin/Function2;", "Lkotlin/sequences/SequenceScope;", "Lkotlin/coroutines/Continuation;", "", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/jvm/functions/Function2;)Ljava/util/Iterator;", "sequence", "Lkotlin/sequences/Sequence;", "(Lkotlin/jvm/functions/Function2;)Lkotlin/sequences/Sequence;", "State", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "com/android/server/permission/jarjar/kotlin/sequences/SequencesKt")
public class SequencesKt__SequenceBuilderKt {
    private static final int State_Done = 4;
    private static final int State_Failed = 5;
    private static final int State_ManyNotReady = 1;
    private static final int State_ManyReady = 2;
    private static final int State_NotReady = 0;
    private static final int State_Ready = 3;

    public static final <T> com.android.server.permission.jarjar.kotlin.sequences.Sequence<T> sequence(final com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super com.android.server.permission.jarjar.kotlin.sequences.SequenceScope<? super T>, ? super com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit>, ? extends java.lang.Object> function2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "block");
        return new com.android.server.permission.jarjar.kotlin.sequences.Sequence<T>() { // from class: com.android.server.permission.jarjar.kotlin.sequences.SequencesKt__SequenceBuilderKt$sequence$$inlined$Sequence$1
            @Override // com.android.server.permission.jarjar.kotlin.sequences.Sequence
            public java.util.Iterator<T> iterator() {
                return com.android.server.permission.jarjar.kotlin.sequences.SequencesKt.iterator(function2);
            }
        };
    }

    public static final <T> java.util.Iterator<T> iterator(com.android.server.permission.jarjar.kotlin.jvm.functions.Function2<? super com.android.server.permission.jarjar.kotlin.sequences.SequenceScope<? super T>, ? super com.android.server.permission.jarjar.kotlin.coroutines.Continuation<? super com.android.server.permission.jarjar.kotlin.Unit>, ? extends java.lang.Object> function2) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(function2, "block");
        com.android.server.permission.jarjar.kotlin.sequences.SequenceBuilderIterator iterator = new com.android.server.permission.jarjar.kotlin.sequences.SequenceBuilderIterator();
        iterator.setNextStep(com.android.server.permission.jarjar.kotlin.coroutines.intrinsics.IntrinsicsKt.createCoroutineUnintercepted(function2, iterator, iterator));
        return iterator;
    }
}
