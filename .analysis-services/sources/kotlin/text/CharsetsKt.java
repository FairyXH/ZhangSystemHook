package kotlin.text;

/* JADX INFO: compiled from: Charsets.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\u0011\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\b¨\u0006\u0004"}, d2 = {"charset", "Ljava/nio/charset/Charset;", "charsetName", "", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class CharsetsKt {
    private static final java.nio.charset.Charset charset(java.lang.String charsetName) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(charsetName, "charsetName");
        java.nio.charset.Charset charsetForName = java.nio.charset.Charset.forName(charsetName);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(charsetForName, "forName(...)");
        return charsetForName;
    }
}
