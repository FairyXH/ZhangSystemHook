package kotlin;

/* JADX INFO: compiled from: UByteArray.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u001a\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\u001a-\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\u00060\u0005H\u0087\bø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a\u001c\u0010\b\u001a\u00020\u00012\n\u0010\t\u001a\u00020\u0001\"\u00020\u0006H\u0087\b¢\u0006\u0004\b\n\u0010\u000b\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\f"}, d2 = {"UByteArray", "Lkotlin/UByteArray;", "size", "", "init", "Lkotlin/Function1;", "Lkotlin/UByte;", "(ILkotlin/jvm/functions/Function1;)[B", "ubyteArrayOf", "elements", "ubyteArrayOf-GBYM_sE", "([B)[B", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class UByteArrayKt {
    private static final byte[] UByteArray(int size, kotlin.jvm.functions.Function1<? super java.lang.Integer, kotlin.UByte> init) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(init, "init");
        byte[] bArr = new byte[size];
        for (int i = 0; i < size; i++) {
            bArr[i] = init.invoke(java.lang.Integer.valueOf(i)).getData();
        }
        return kotlin.UByteArray.m11378constructorimpl(bArr);
    }

    /* JADX INFO: renamed from: ubyteArrayOf-GBYM_sE, reason: not valid java name */
    private static final byte[] m11394ubyteArrayOfGBYM_sE(byte... elements) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(elements, "elements");
        return elements;
    }
}
