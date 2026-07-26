package kotlin.io.encoding;

/* JADX INFO: compiled from: Base64JVM.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000 \n\u0000\n\u0002\u0010\u0012\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\u001a%\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0081\b\u001a5\u0010\b\u001a\u00020\u0006*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u00012\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0081\b\u001a%\u0010\u000b\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0081\b\u001a%\u0010\f\u001a\u00020\r*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u0006H\u0081\b¨\u0006\u000e"}, d2 = {"platformCharsToBytes", "", "Lkotlin/io/encoding/Base64;", "source", "", "startIndex", "", "endIndex", "platformEncodeIntoByteArray", "destination", "destinationOffset", "platformEncodeToByteArray", "platformEncodeToString", "", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class Base64JVMKt {
    private static final byte[] platformCharsToBytes(kotlin.io.encoding.Base64 $this$platformCharsToBytes, java.lang.CharSequence source, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$platformCharsToBytes, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        if (source instanceof java.lang.String) {
            $this$platformCharsToBytes.checkSourceBounds$kotlin_stdlib(source.length(), startIndex, endIndex);
            java.lang.String strSubstring = ((java.lang.String) source).substring(startIndex, endIndex);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            java.nio.charset.Charset charset = kotlin.text.Charsets.ISO_8859_1;
            kotlin.jvm.internal.Intrinsics.checkNotNull(strSubstring, "null cannot be cast to non-null type java.lang.String");
            byte[] bytes = strSubstring.getBytes(charset);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bytes, "getBytes(...)");
            return bytes;
        }
        return $this$platformCharsToBytes.charsToBytesImpl$kotlin_stdlib(source, startIndex, endIndex);
    }

    private static final java.lang.String platformEncodeToString(kotlin.io.encoding.Base64 $this$platformEncodeToString, byte[] source, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$platformEncodeToString, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        byte[] byteResult = $this$platformEncodeToString.encodeToByteArrayImpl$kotlin_stdlib(source, startIndex, endIndex);
        return new java.lang.String(byteResult, kotlin.text.Charsets.ISO_8859_1);
    }

    private static final int platformEncodeIntoByteArray(kotlin.io.encoding.Base64 $this$platformEncodeIntoByteArray, byte[] source, byte[] destination, int destinationOffset, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$platformEncodeIntoByteArray, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        return $this$platformEncodeIntoByteArray.encodeIntoByteArrayImpl$kotlin_stdlib(source, destination, destinationOffset, startIndex, endIndex);
    }

    private static final byte[] platformEncodeToByteArray(kotlin.io.encoding.Base64 $this$platformEncodeToByteArray, byte[] source, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$platformEncodeToByteArray, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        return $this$platformEncodeToByteArray.encodeToByteArrayImpl$kotlin_stdlib(source, startIndex, endIndex);
    }
}
