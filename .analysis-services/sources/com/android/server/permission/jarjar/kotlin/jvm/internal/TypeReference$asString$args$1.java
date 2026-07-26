package com.android.server.permission.jarjar.kotlin.jvm.internal;

/* JADX INFO: compiled from: TypeReference.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "it", "Lkotlin/reflect/KTypeProjection;", "invoke"}, k = 3, mv = {1, 9, 0}, xi = 48)
final class TypeReference$asString$args$1 extends com.android.server.permission.jarjar.kotlin.jvm.internal.Lambda implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function1<com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection, java.lang.CharSequence> {
    final /* synthetic */ com.android.server.permission.jarjar.kotlin.jvm.internal.TypeReference this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    TypeReference$asString$args$1(com.android.server.permission.jarjar.kotlin.jvm.internal.TypeReference typeReference) {
        super(1);
        this.this$0 = typeReference;
    }

    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function1
    public final java.lang.CharSequence invoke(com.android.server.permission.jarjar.kotlin.reflect.KTypeProjection it) {
        com.android.server.permission.jarjar.kotlin.jvm.internal.Intrinsics.checkNotNullParameter(it, "it");
        return this.this$0.asString(it);
    }
}
