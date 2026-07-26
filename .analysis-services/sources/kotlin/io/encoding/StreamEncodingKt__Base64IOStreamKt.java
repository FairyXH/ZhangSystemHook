package kotlin.io.encoding;

/* JADX INFO: compiled from: Base64IOStream.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u0014\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0007\u001a\u0014\u0010\u0004\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u0003H\u0007¨\u0006\u0006"}, d2 = {"decodingWith", "Ljava/io/InputStream;", "base64", "Lkotlin/io/encoding/Base64;", "encodingWith", "Ljava/io/OutputStream;", "kotlin-stdlib"}, k = 5, mv = {1, 9, 0}, xi = 49, xs = "kotlin/io/encoding/StreamEncodingKt")
class StreamEncodingKt__Base64IOStreamKt {
    public static final java.io.InputStream decodingWith(java.io.InputStream $this$decodingWith, kotlin.io.encoding.Base64 base64) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$decodingWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(base64, "base64");
        return new kotlin.io.encoding.DecodeInputStream($this$decodingWith, base64);
    }

    public static final java.io.OutputStream encodingWith(java.io.OutputStream $this$encodingWith, kotlin.io.encoding.Base64 base64) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$encodingWith, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(base64, "base64");
        return new kotlin.io.encoding.EncodeOutputStream($this$encodingWith, base64);
    }
}
