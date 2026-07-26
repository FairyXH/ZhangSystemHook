package kotlin.io;

/* JADX INFO: compiled from: Console.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\b\u0010\u0014\u001a\u00020\u0004H\u0002J\u0010\u0010\u0015\u001a\u00020\u00042\u0006\u0010\u0016\u001a\u00020\u0010H\u0002J\u0018\u0010\u0017\u001a\u00020\u00042\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u0004H\u0002J\u0018\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fJ\b\u0010 \u001a\u00020!H\u0002J\b\u0010\"\u001a\u00020!H\u0002J\u0010\u0010#\u001a\u00020!2\u0006\u0010\u001e\u001a\u00020\u001fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u0011\u001a\u00060\u0012j\u0002`\u0013X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006$"}, d2 = {"Lkotlin/io/LineReader;", "", "()V", "BUFFER_SIZE", "", "byteBuf", "Ljava/nio/ByteBuffer;", "bytes", "", "charBuf", "Ljava/nio/CharBuffer;", "chars", "", "decoder", "Ljava/nio/charset/CharsetDecoder;", "directEOL", "", "sb", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "compactBytes", "decode", "endOfInput", "decodeEndOfInput", "nBytes", "nChars", "readLine", "", "inputStream", "Ljava/io/InputStream;", "charset", "Ljava/nio/charset/Charset;", "resetAll", "", "trimStringBuilder", "updateCharset", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class LineReader {
    private static final int BUFFER_SIZE = 32;
    private static final java.nio.ByteBuffer byteBuf;
    private static final java.nio.CharBuffer charBuf;
    private static java.nio.charset.CharsetDecoder decoder;
    private static boolean directEOL;
    private static final java.lang.StringBuilder sb;
    public static final kotlin.io.LineReader INSTANCE = new kotlin.io.LineReader();
    private static final byte[] bytes = new byte[32];
    private static final char[] chars = new char[32];

    private LineReader() {
    }

    static {
        java.nio.ByteBuffer byteBufferWrap = java.nio.ByteBuffer.wrap(bytes);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(byteBufferWrap, "wrap(...)");
        byteBuf = byteBufferWrap;
        java.nio.CharBuffer charBufferWrap = java.nio.CharBuffer.wrap(chars);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(charBufferWrap, "wrap(...)");
        charBuf = charBufferWrap;
        sb = new java.lang.StringBuilder();
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x0083, code lost:
    
        if (r1 <= 0) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x008b, code lost:
    
        if (kotlin.io.LineReader.chars[r1 - 1] != '\n') goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x008d, code lost:
    
        r1 = r1 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x008f, code lost:
    
        if (r1 <= 0) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0099, code lost:
    
        if (kotlin.io.LineReader.chars[r1 - 1] != '\r') goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x009b, code lost:
    
        r1 = r1 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00a5, code lost:
    
        if (kotlin.io.LineReader.sb.length() != 0) goto L47;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x00a8, code lost:
    
        r7 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x00a9, code lost:
    
        if (r7 == false) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00b3, code lost:
    
        return new java.lang.String(kotlin.io.LineReader.chars, 0, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x00b4, code lost:
    
        kotlin.io.LineReader.sb.append(kotlin.io.LineReader.chars, 0, r1);
        r2 = kotlin.io.LineReader.sb.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r2, "toString(...)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00cd, code lost:
    
        if (kotlin.io.LineReader.sb.length() <= 32) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00cf, code lost:
    
        trimStringBuilder();
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00d2, code lost:
    
        kotlin.io.LineReader.sb.setLength(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00d8, code lost:
    
        return r2;
     */
    /* JADX WARN: Removed duplicated region for block: B:10:0x0024 A[Catch: all -> 0x00df, TryCatch #0 {, blocks: (B:3:0x0001, B:5:0x0010, B:7:0x0014, B:8:0x001a, B:12:0x0029, B:14:0x0037, B:23:0x004c, B:37:0x0085, B:39:0x008d, B:41:0x0091, B:43:0x009b, B:44:0x009d, B:49:0x00ab, B:52:0x00b4, B:54:0x00cf, B:55:0x00d2, B:24:0x0051, B:27:0x005c, B:31:0x0063, B:33:0x0074, B:35:0x007c, B:58:0x00d9, B:10:0x0024), top: B:63:0x0001 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final synchronized java.lang.String readLine(java.io.InputStream r12, java.nio.charset.Charset r13) {
        /*
            Method dump skipped, instruction units count: 226
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.LineReader.readLine(java.io.InputStream, java.nio.charset.Charset):java.lang.String");
    }

    private final int decode(boolean endOfInput) throws java.nio.charset.CharacterCodingException {
        while (true) {
            java.nio.charset.CharsetDecoder charsetDecoder = decoder;
            if (charsetDecoder == null) {
                kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("decoder");
                charsetDecoder = null;
            }
            java.nio.charset.CoderResult coderResult = charsetDecoder.decode(byteBuf, charBuf, endOfInput);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(coderResult, "decode(...)");
            if (coderResult.isError()) {
                resetAll();
                coderResult.throwException();
            }
            int nChars = charBuf.position();
            if (!coderResult.isOverflow()) {
                return nChars;
            }
            sb.append(chars, 0, nChars - 1);
            charBuf.position(0);
            charBuf.limit(32);
            charBuf.put(chars[nChars - 1]);
        }
    }

    private final int compactBytes() {
        java.nio.ByteBuffer $this$compactBytes_u24lambda_u241 = byteBuf;
        $this$compactBytes_u24lambda_u241.compact();
        int iPosition = $this$compactBytes_u24lambda_u241.position();
        $this$compactBytes_u24lambda_u241.position(0);
        return iPosition;
    }

    private final int decodeEndOfInput(int nBytes, int nChars) throws java.nio.charset.CharacterCodingException {
        byteBuf.limit(nBytes);
        charBuf.position(nChars);
        int iDecode = decode(true);
        java.nio.charset.CharsetDecoder charsetDecoder = decoder;
        if (charsetDecoder == null) {
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("decoder");
            charsetDecoder = null;
        }
        charsetDecoder.reset();
        byteBuf.position(0);
        return iDecode;
    }

    private final void updateCharset(java.nio.charset.Charset charset) {
        java.nio.charset.CharsetDecoder charsetDecoderNewDecoder = charset.newDecoder();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(charsetDecoderNewDecoder, "newDecoder(...)");
        decoder = charsetDecoderNewDecoder;
        byteBuf.clear();
        charBuf.clear();
        byteBuf.put((byte) 10);
        byteBuf.flip();
        java.nio.charset.CharsetDecoder charsetDecoder = decoder;
        if (charsetDecoder == null) {
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("decoder");
            charsetDecoder = null;
        }
        boolean z = false;
        charsetDecoder.decode(byteBuf, charBuf, false);
        if (charBuf.position() == 1 && charBuf.get(0) == '\n') {
            z = true;
        }
        directEOL = z;
        resetAll();
    }

    private final void resetAll() {
        java.nio.charset.CharsetDecoder charsetDecoder = decoder;
        if (charsetDecoder == null) {
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException("decoder");
            charsetDecoder = null;
        }
        charsetDecoder.reset();
        byteBuf.position(0);
        sb.setLength(0);
    }

    private final void trimStringBuilder() {
        sb.setLength(32);
        sb.trimToSize();
    }
}
