package com.android.server.permission.jarjar.kotlin.collections.unsigned;

/* JADX INFO: compiled from: _UArrays.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\f\n\u0000\n\u0002\u0010(\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001H\n¢\u0006\u0002\b\u0003"}, d2 = {"<anonymous>", "", "Lkotlin/UByte;", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
final class UArraysKt___UArraysKt$withIndex$3 extends com.android.server.permission.jarjar.kotlin.jvm.internal.Lambda implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<java.util.Iterator<? extends com.android.server.permission.jarjar.kotlin.UByte>> {
    final /* synthetic */ byte[] $this_withIndex;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    UArraysKt___UArraysKt$withIndex$3(byte[] bArr) {
        super(0);
        this.$this_withIndex = bArr;
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function0
    public final java.util.Iterator<? extends com.android.server.permission.jarjar.kotlin.UByte> invoke() {
        return com.android.server.permission.jarjar.kotlin.UByteArray.m6169iteratorimpl(this.$this_withIndex);
    }
}
