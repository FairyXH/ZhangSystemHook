package com.android.server.permission.jarjar.kotlin.time;

/* JADX INFO: compiled from: TimeSources.kt */
/* JADX INFO: loaded from: classes2.dex */
@com.android.server.permission.jarjar.kotlin.Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0004\b\u0002\u0010\u0003"}, d2 = {"<anonymous>", "", "invoke", "()Ljava/lang/Long;"}, k = 3, mv = {1, 9, 0}, xi = 48)
final class AbstractLongTimeSource$zero$2 extends com.android.server.permission.jarjar.kotlin.jvm.internal.Lambda implements com.android.server.permission.jarjar.kotlin.jvm.functions.Function0<java.lang.Long> {
    final /* synthetic */ com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    AbstractLongTimeSource$zero$2(com.android.server.permission.jarjar.kotlin.time.AbstractLongTimeSource abstractLongTimeSource) {
        super(0);
        this.this$0 = abstractLongTimeSource;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.server.permission.jarjar.kotlin.jvm.functions.Function0
    public final java.lang.Long invoke() {
        return java.lang.Long.valueOf(this.this$0.read());
    }
}
