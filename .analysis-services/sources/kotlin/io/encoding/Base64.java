package kotlin.io.encoding;

/* JADX INFO: compiled from: Base64.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\n\b\u0017\u0018\u0000 22\u00020\u0001:\u00012B\u0017\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003¢\u0006\u0002\u0010\u0005J\u0015\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0000¢\u0006\u0002\b\rJ%\u0010\u000e\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0000¢\u0006\u0002\b\u0013J \u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\u0011H\u0002J%\u0010\u0019\u001a\u00020\u00152\u0006\u0010\u001a\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0000¢\u0006\u0002\b\u001bJ\"\u0010\u001c\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J\"\u0010\u001c\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\u000f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J0\u0010\u001d\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001e\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0002J4\u0010\u001f\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001e\u001a\u00020\f2\b\b\u0002\u0010\u0017\u001a\u00020\u00112\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J4\u0010\u001f\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\f2\b\b\u0002\u0010\u0017\u001a\u00020\u00112\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J \u0010 \u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0002J\"\u0010!\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J4\u0010\"\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001e\u001a\u00020\f2\b\b\u0002\u0010\u0017\u001a\u00020\u00112\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J5\u0010#\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001e\u001a\u00020\f2\u0006\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0000¢\u0006\u0002\b$J\u0010\u0010%\u001a\u00020\u00112\u0006\u0010\u001a\u001a\u00020\u0011H\u0002J=\u0010&\u001a\u0002H'\"\f\b\u0000\u0010'*\u00060(j\u0002`)2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u001e\u001a\u0002H'2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011¢\u0006\u0002\u0010*J\"\u0010+\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\u0010\u001a\u00020\u00112\b\b\u0002\u0010\u0012\u001a\u00020\u0011J%\u0010,\u001a\u00020\f2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0000¢\u0006\u0002\b-J(\u0010.\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010/\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00112\u0006\u00100\u001a\u00020\u0011H\u0002J \u00101\u001a\u00020\u00112\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0011H\u0002R\u0014\u0010\u0004\u001a\u00020\u0003X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0014\u0010\u0002\u001a\u00020\u0003X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0007¨\u00063"}, d2 = {"Lkotlin/io/encoding/Base64;", "", "isUrlSafe", "", "isMimeScheme", "(ZZ)V", "isMimeScheme$kotlin_stdlib", "()Z", "isUrlSafe$kotlin_stdlib", "bytesToStringImpl", "", "source", "", "bytesToStringImpl$kotlin_stdlib", "charsToBytesImpl", "", "startIndex", "", "endIndex", "charsToBytesImpl$kotlin_stdlib", "checkDestinationBounds", "", "destinationSize", "destinationOffset", "capacityNeeded", "checkSourceBounds", "sourceSize", "checkSourceBounds$kotlin_stdlib", "decode", "decodeImpl", "destination", "decodeIntoByteArray", "decodeSize", "encode", "encodeIntoByteArray", "encodeIntoByteArrayImpl", "encodeIntoByteArrayImpl$kotlin_stdlib", "encodeSize", "encodeToAppendable", "A", "Ljava/lang/Appendable;", "Lkotlin/text/Appendable;", "([BLjava/lang/Appendable;II)Ljava/lang/Appendable;", "encodeToByteArray", "encodeToByteArrayImpl", "encodeToByteArrayImpl$kotlin_stdlib", "handlePaddingSymbol", "padIndex", "byteStart", "skipIllegalSymbolsIfMime", "Default", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
public class Base64 {
    private static final int bitsPerByte = 8;
    private static final int bitsPerSymbol = 6;
    public static final int bytesPerGroup = 3;
    private static final int mimeGroupsPerLine = 19;
    public static final int mimeLineLength = 76;
    public static final byte padSymbol = 61;
    public static final int symbolsPerGroup = 4;
    private final boolean isMimeScheme;
    private final boolean isUrlSafe;

    /* JADX INFO: renamed from: Default, reason: from kotlin metadata */
    public static final kotlin.io.encoding.Base64.Companion INSTANCE = new kotlin.io.encoding.Base64.Companion(null);
    private static final byte[] mimeLineSeparatorSymbols = {13, 10};
    private static final kotlin.io.encoding.Base64 UrlSafe = new kotlin.io.encoding.Base64(true, false);
    private static final kotlin.io.encoding.Base64 Mime = new kotlin.io.encoding.Base64(false, true);

    public /* synthetic */ Base64(boolean z, boolean z2, kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
        this(z, z2);
    }

    private Base64(boolean isUrlSafe, boolean isMimeScheme) {
        this.isUrlSafe = isUrlSafe;
        this.isMimeScheme = isMimeScheme;
        if ((this.isUrlSafe && this.isMimeScheme) ? false : true) {
        } else {
            throw new java.lang.IllegalArgumentException("Failed requirement.".toString());
        }
    }

    /* JADX INFO: renamed from: isUrlSafe$kotlin_stdlib, reason: from getter */
    public final boolean getIsUrlSafe() {
        return this.isUrlSafe;
    }

    /* JADX INFO: renamed from: isMimeScheme$kotlin_stdlib, reason: from getter */
    public final boolean getIsMimeScheme() {
        return this.isMimeScheme;
    }

    public static /* synthetic */ byte[] encodeToByteArray$default(kotlin.io.encoding.Base64 base64, byte[] bArr, int i, int i2, int i3, java.lang.Object obj) {
        if (obj != null) {
            throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: encodeToByteArray");
        }
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = bArr.length;
        }
        return base64.encodeToByteArray(bArr, i, i2);
    }

    public final byte[] encodeToByteArray(byte[] source, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        return encodeToByteArrayImpl$kotlin_stdlib(source, startIndex, endIndex);
    }

    public static /* synthetic */ int encodeIntoByteArray$default(kotlin.io.encoding.Base64 base64, byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4, java.lang.Object obj) {
        if (obj == null) {
            return base64.encodeIntoByteArray(bArr, bArr2, (i4 & 4) != 0 ? 0 : i, (i4 & 8) != 0 ? 0 : i2, (i4 & 16) != 0 ? bArr.length : i3);
        }
        throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: encodeIntoByteArray");
    }

    public final int encodeIntoByteArray(byte[] source, byte[] destination, int destinationOffset, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        return encodeIntoByteArrayImpl$kotlin_stdlib(source, destination, destinationOffset, startIndex, endIndex);
    }

    public static /* synthetic */ java.lang.String encode$default(kotlin.io.encoding.Base64 base64, byte[] bArr, int i, int i2, int i3, java.lang.Object obj) {
        if (obj != null) {
            throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: encode");
        }
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = bArr.length;
        }
        return base64.encode(bArr, i, i2);
    }

    public final java.lang.String encode(byte[] source, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        return new java.lang.String(encodeToByteArrayImpl$kotlin_stdlib(source, startIndex, endIndex), kotlin.text.Charsets.ISO_8859_1);
    }

    public static /* synthetic */ java.lang.Appendable encodeToAppendable$default(kotlin.io.encoding.Base64 base64, byte[] bArr, java.lang.Appendable appendable, int i, int i2, int i3, java.lang.Object obj) {
        if (obj != null) {
            throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: encodeToAppendable");
        }
        if ((i3 & 4) != 0) {
            i = 0;
        }
        if ((i3 & 8) != 0) {
            i2 = bArr.length;
        }
        return base64.encodeToAppendable(bArr, appendable, i, i2);
    }

    public final <A extends java.lang.Appendable> A encodeToAppendable(byte[] source, A destination, int startIndex, int endIndex) throws java.io.IOException {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        java.lang.String stringResult = new java.lang.String(encodeToByteArrayImpl$kotlin_stdlib(source, startIndex, endIndex), kotlin.text.Charsets.ISO_8859_1);
        destination.append(stringResult);
        return destination;
    }

    public static /* synthetic */ byte[] decode$default(kotlin.io.encoding.Base64 base64, byte[] bArr, int i, int i2, int i3, java.lang.Object obj) {
        if (obj != null) {
            throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: decode");
        }
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = bArr.length;
        }
        return base64.decode(bArr, i, i2);
    }

    public final byte[] decode(byte[] source, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        checkSourceBounds$kotlin_stdlib(source.length, startIndex, endIndex);
        int decodeSize = decodeSize(source, startIndex, endIndex);
        byte[] destination = new byte[decodeSize];
        int bytesWritten = decodeImpl(source, destination, 0, startIndex, endIndex);
        if (!(bytesWritten == destination.length)) {
            throw new java.lang.IllegalStateException("Check failed.".toString());
        }
        return destination;
    }

    public static /* synthetic */ int decodeIntoByteArray$default(kotlin.io.encoding.Base64 base64, byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4, java.lang.Object obj) {
        if (obj == null) {
            return base64.decodeIntoByteArray(bArr, bArr2, (i4 & 4) != 0 ? 0 : i, (i4 & 8) != 0 ? 0 : i2, (i4 & 16) != 0 ? bArr.length : i3);
        }
        throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: decodeIntoByteArray");
    }

    public final int decodeIntoByteArray(byte[] source, byte[] destination, int destinationOffset, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        checkSourceBounds$kotlin_stdlib(source.length, startIndex, endIndex);
        checkDestinationBounds(destination.length, destinationOffset, decodeSize(source, startIndex, endIndex));
        return decodeImpl(source, destination, destinationOffset, startIndex, endIndex);
    }

    public static /* synthetic */ byte[] decode$default(kotlin.io.encoding.Base64 base64, java.lang.CharSequence charSequence, int i, int i2, int i3, java.lang.Object obj) {
        if (obj != null) {
            throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: decode");
        }
        if ((i3 & 2) != 0) {
            i = 0;
        }
        if ((i3 & 4) != 0) {
            i2 = charSequence.length();
        }
        return base64.decode(charSequence, i, i2);
    }

    public final byte[] decode(java.lang.CharSequence source, int startIndex, int endIndex) {
        byte[] bArrCharsToBytesImpl$kotlin_stdlib;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        if (source instanceof java.lang.String) {
            checkSourceBounds$kotlin_stdlib(source.length(), startIndex, endIndex);
            java.lang.String strSubstring = ((java.lang.String) source).substring(startIndex, endIndex);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            java.nio.charset.Charset charset = kotlin.text.Charsets.ISO_8859_1;
            kotlin.jvm.internal.Intrinsics.checkNotNull(strSubstring, "null cannot be cast to non-null type java.lang.String");
            bArrCharsToBytesImpl$kotlin_stdlib = strSubstring.getBytes(charset);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bArrCharsToBytesImpl$kotlin_stdlib, "getBytes(...)");
        } else {
            bArrCharsToBytesImpl$kotlin_stdlib = charsToBytesImpl$kotlin_stdlib(source, startIndex, endIndex);
        }
        byte[] byteSource = bArrCharsToBytesImpl$kotlin_stdlib;
        return decode$default(this, byteSource, 0, 0, 6, (java.lang.Object) null);
    }

    public static /* synthetic */ int decodeIntoByteArray$default(kotlin.io.encoding.Base64 base64, java.lang.CharSequence charSequence, byte[] bArr, int i, int i2, int i3, int i4, java.lang.Object obj) {
        if (obj == null) {
            return base64.decodeIntoByteArray(charSequence, bArr, (i4 & 4) != 0 ? 0 : i, (i4 & 8) != 0 ? 0 : i2, (i4 & 16) != 0 ? charSequence.length() : i3);
        }
        throw new java.lang.UnsupportedOperationException("Super calls with default arguments not supported in this target, function: decodeIntoByteArray");
    }

    public final int decodeIntoByteArray(java.lang.CharSequence source, byte[] destination, int destinationOffset, int startIndex, int endIndex) {
        byte[] bArrCharsToBytesImpl$kotlin_stdlib;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        if (source instanceof java.lang.String) {
            checkSourceBounds$kotlin_stdlib(source.length(), startIndex, endIndex);
            java.lang.String strSubstring = ((java.lang.String) source).substring(startIndex, endIndex);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            java.nio.charset.Charset charset = kotlin.text.Charsets.ISO_8859_1;
            kotlin.jvm.internal.Intrinsics.checkNotNull(strSubstring, "null cannot be cast to non-null type java.lang.String");
            bArrCharsToBytesImpl$kotlin_stdlib = strSubstring.getBytes(charset);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bArrCharsToBytesImpl$kotlin_stdlib, "getBytes(...)");
        } else {
            bArrCharsToBytesImpl$kotlin_stdlib = charsToBytesImpl$kotlin_stdlib(source, startIndex, endIndex);
        }
        byte[] byteSource = bArrCharsToBytesImpl$kotlin_stdlib;
        return decodeIntoByteArray$default(this, byteSource, destination, destinationOffset, 0, 0, 24, (java.lang.Object) null);
    }

    public final byte[] encodeToByteArrayImpl$kotlin_stdlib(byte[] source, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        checkSourceBounds$kotlin_stdlib(source.length, startIndex, endIndex);
        int encodeSize = encodeSize(endIndex - startIndex);
        byte[] destination = new byte[encodeSize];
        encodeIntoByteArrayImpl$kotlin_stdlib(source, destination, 0, startIndex, endIndex);
        return destination;
    }

    public final int encodeIntoByteArrayImpl$kotlin_stdlib(byte[] source, byte[] destination, int destinationOffset, int startIndex, int endIndex) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(destination, "destination");
        checkSourceBounds$kotlin_stdlib(source.length, startIndex, endIndex);
        checkDestinationBounds(destination.length, destinationOffset, encodeSize(endIndex - startIndex));
        byte[] encodeMap = this.isUrlSafe ? kotlin.io.encoding.Base64Kt.base64UrlEncodeMap : kotlin.io.encoding.Base64Kt.base64EncodeMap;
        int sourceIndex = startIndex;
        int destinationIndex = destinationOffset;
        int groupsPerLine = this.isMimeScheme ? 19 : Integer.MAX_VALUE;
        while (true) {
            if (sourceIndex + 2 >= endIndex) {
                break;
            }
            int groups = java.lang.Math.min((endIndex - sourceIndex) / 3, groupsPerLine);
            int i = 0;
            while (i < groups) {
                int sourceIndex2 = sourceIndex + 1;
                int byte1 = source[sourceIndex] & 255;
                int sourceIndex3 = sourceIndex2 + 1;
                int byte2 = source[sourceIndex2] & 255;
                int sourceIndex4 = sourceIndex3 + 1;
                int byte3 = source[sourceIndex3] & 255;
                int bits = (byte1 << 16) | (byte2 << 8) | byte3;
                int destinationIndex2 = destinationIndex + 1;
                destination[destinationIndex] = encodeMap[bits >>> 18];
                int destinationIndex3 = destinationIndex2 + 1;
                destination[destinationIndex2] = encodeMap[(bits >>> 12) & 63];
                int destinationIndex4 = destinationIndex3 + 1;
                destination[destinationIndex3] = encodeMap[(bits >>> 6) & 63];
                destinationIndex = destinationIndex4 + 1;
                destination[destinationIndex4] = encodeMap[bits & 63];
                i++;
                sourceIndex = sourceIndex4;
            }
            if (groups == groupsPerLine && sourceIndex != endIndex) {
                int destinationIndex5 = destinationIndex + 1;
                destination[destinationIndex] = mimeLineSeparatorSymbols[0];
                destinationIndex = destinationIndex5 + 1;
                destination[destinationIndex5] = mimeLineSeparatorSymbols[1];
            }
        }
        switch (endIndex - sourceIndex) {
            case 1:
                int byte22 = sourceIndex + 1;
                int byte12 = source[sourceIndex] & 255;
                int bits2 = byte12 << 4;
                int destinationIndex6 = destinationIndex + 1;
                destination[destinationIndex] = encodeMap[bits2 >>> 6];
                int destinationIndex7 = destinationIndex6 + 1;
                destination[destinationIndex6] = encodeMap[bits2 & 63];
                int destinationIndex8 = destinationIndex7 + 1;
                destination[destinationIndex7] = 61;
                destinationIndex = destinationIndex8 + 1;
                destination[destinationIndex8] = 61;
                sourceIndex = byte22;
                break;
            case 2:
                int sourceIndex5 = sourceIndex + 1;
                int byte13 = source[sourceIndex] & 255;
                int sourceIndex6 = sourceIndex5 + 1;
                int byte23 = source[sourceIndex5] & 255;
                int bits3 = (byte13 << 10) | (byte23 << 2);
                int destinationIndex9 = destinationIndex + 1;
                destination[destinationIndex] = encodeMap[bits3 >>> 12];
                int destinationIndex10 = destinationIndex9 + 1;
                destination[destinationIndex9] = encodeMap[(bits3 >>> 6) & 63];
                int destinationIndex11 = destinationIndex10 + 1;
                destination[destinationIndex10] = encodeMap[bits3 & 63];
                destinationIndex = destinationIndex11 + 1;
                destination[destinationIndex11] = 61;
                sourceIndex = sourceIndex6;
                break;
        }
        if (!(sourceIndex == endIndex)) {
            throw new java.lang.IllegalStateException("Check failed.".toString());
        }
        return destinationIndex - destinationOffset;
    }

    private final int encodeSize(int sourceSize) {
        int groups = ((sourceSize + 3) - 1) / 3;
        int lineSeparators = this.isMimeScheme ? (groups - 1) / 19 : 0;
        int size = (groups * 4) + (lineSeparators * 2);
        if (size < 0) {
            throw new java.lang.IllegalArgumentException("Input is too big");
        }
        return size;
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x00de, code lost:
    
        if (r5 == (-2)) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00e0, code lost:
    
        r6 = skipIllegalSymbolsIfMime(r20, r6, r24);
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x00e4, code lost:
    
        if (r6 < r24) goto L34;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00e8, code lost:
    
        return r7 - r22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00e9, code lost:
    
        r11 = r20[r6] & 255;
        r10 = new java.lang.StringBuilder().append("Symbol '").append((char) r11).append("'(");
        r13 = java.lang.Integer.toString(r11, kotlin.text.CharsKt.checkRadix(8));
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r13, "toString(...)");
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x012b, code lost:
    
        throw new java.lang.IllegalArgumentException(r10.append(r13).append(") at index ").append(r6 - 1).append(" is prohibited after the pad character").toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0133, code lost:
    
        throw new java.lang.IllegalArgumentException("The last unit of input does not have enough bits");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private final int decodeImpl(byte[] r20, byte[] r21, int r22, int r23, int r24) {
        /*
            Method dump skipped, instruction units count: 308
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.io.encoding.Base64.decodeImpl(byte[], byte[], int, int, int):int");
    }

    private final int decodeSize(byte[] source, int startIndex, int endIndex) {
        int symbols = endIndex - startIndex;
        if (symbols == 0) {
            return 0;
        }
        if (symbols == 1) {
            throw new java.lang.IllegalArgumentException("Input should have at list 2 symbols for Base64 decoding, startIndex: " + startIndex + ", endIndex: " + endIndex);
        }
        if (this.isMimeScheme) {
            int index = startIndex;
            while (true) {
                if (index >= endIndex) {
                    break;
                }
                int symbol = source[index] & 255;
                int symbolBits = kotlin.io.encoding.Base64Kt.base64DecodeMap[symbol];
                if (symbolBits < 0) {
                    if (symbolBits == -2) {
                        symbols -= endIndex - index;
                        break;
                    }
                    symbols--;
                }
                index++;
            }
        } else {
            int index2 = endIndex - 1;
            if (source[index2] == 61) {
                symbols--;
                if (source[endIndex - 2] == 61) {
                    symbols--;
                }
            }
        }
        return (int) ((((long) symbols) * ((long) 6)) / ((long) 8));
    }

    public final byte[] charsToBytesImpl$kotlin_stdlib(java.lang.CharSequence source, int startIndex, int endIndex) {
        int length;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        checkSourceBounds$kotlin_stdlib(source.length(), startIndex, endIndex);
        byte[] byteArray = new byte[endIndex - startIndex];
        int length2 = 0;
        for (int index = startIndex; index < endIndex; index++) {
            int symbol = source.charAt(index);
            if (symbol <= 255) {
                length = length2 + 1;
                byteArray[length2] = (byte) symbol;
            } else {
                length = length2 + 1;
                byteArray[length2] = 63;
            }
            length2 = length;
        }
        return byteArray;
    }

    public final java.lang.String bytesToStringImpl$kotlin_stdlib(byte[] source) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(source, "source");
        java.lang.StringBuilder stringBuilder = new java.lang.StringBuilder(source.length);
        for (byte b : source) {
            stringBuilder.append((char) b);
        }
        java.lang.String string = stringBuilder.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    private final int handlePaddingSymbol(byte[] source, int padIndex, int endIndex, int byteStart) {
        switch (byteStart) {
            case -8:
                throw new java.lang.IllegalArgumentException("Redundant pad character at index " + padIndex);
            case -7:
            case -5:
            case -3:
            default:
                throw new java.lang.IllegalStateException("Unreachable".toString());
            case -6:
                return padIndex + 1;
            case -4:
                int secondPadIndex = skipIllegalSymbolsIfMime(source, padIndex + 1, endIndex);
                if (secondPadIndex == endIndex || source[secondPadIndex] != 61) {
                    throw new java.lang.IllegalArgumentException("Missing one pad character at index " + secondPadIndex);
                }
                return secondPadIndex + 1;
            case -2:
                return padIndex + 1;
        }
    }

    private final int skipIllegalSymbolsIfMime(byte[] source, int startIndex, int endIndex) {
        if (!this.isMimeScheme) {
            return startIndex;
        }
        int sourceIndex = startIndex;
        while (sourceIndex < endIndex) {
            int symbol = source[sourceIndex] & 255;
            if (kotlin.io.encoding.Base64Kt.base64DecodeMap[symbol] != -1) {
                return sourceIndex;
            }
            sourceIndex++;
        }
        return sourceIndex;
    }

    public final void checkSourceBounds$kotlin_stdlib(int sourceSize, int startIndex, int endIndex) {
        kotlin.collections.AbstractList.INSTANCE.checkBoundsIndexes$kotlin_stdlib(startIndex, endIndex, sourceSize);
    }

    private final void checkDestinationBounds(int destinationSize, int destinationOffset, int capacityNeeded) {
        if (destinationOffset < 0 || destinationOffset > destinationSize) {
            throw new java.lang.IndexOutOfBoundsException("destination offset: " + destinationOffset + ", destination size: " + destinationSize);
        }
        int destinationEndIndex = destinationOffset + capacityNeeded;
        if (destinationEndIndex < 0 || destinationEndIndex > destinationSize) {
            throw new java.lang.IndexOutOfBoundsException("The destination array does not have enough capacity, destination offset: " + destinationOffset + ", destination size: " + destinationSize + ", capacity needed: " + capacityNeeded);
        }
    }

    /* JADX INFO: renamed from: kotlin.io.encoding.Base64$Default, reason: from kotlin metadata */
    /* JADX INFO: compiled from: Base64.kt */
    @kotlin.Metadata(d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0012\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0005R\u0011\u0010\u0006\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\u0005R\u000e\u0010\b\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\tX\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\tX\u0082T¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\tX\u0080T¢\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\u00020\u000fX\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0012\u001a\u00020\u0013X\u0080T¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\tX\u0080T¢\u0006\u0002\n\u0000¨\u0006\u0015"}, d2 = {"Lkotlin/io/encoding/Base64$Default;", "Lkotlin/io/encoding/Base64;", "()V", "Mime", "getMime", "()Lkotlin/io/encoding/Base64;", "UrlSafe", "getUrlSafe", "bitsPerByte", "", "bitsPerSymbol", "bytesPerGroup", "mimeGroupsPerLine", "mimeLineLength", "mimeLineSeparatorSymbols", "", "getMimeLineSeparatorSymbols$kotlin_stdlib", "()[B", "padSymbol", "", "symbolsPerGroup", "kotlin-stdlib"}, k = 1, mv = {1, 9, 0}, xi = 48)
    public static final class Companion extends kotlin.io.encoding.Base64 {
        public /* synthetic */ Companion(kotlin.jvm.internal.DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        /* JADX WARN: Illegal instructions before constructor call */
        private Companion() {
            boolean z = false;
            super(z, z, null);
        }

        public final byte[] getMimeLineSeparatorSymbols$kotlin_stdlib() {
            return kotlin.io.encoding.Base64.mimeLineSeparatorSymbols;
        }

        public final kotlin.io.encoding.Base64 getUrlSafe() {
            return kotlin.io.encoding.Base64.UrlSafe;
        }

        public final kotlin.io.encoding.Base64 getMime() {
            return kotlin.io.encoding.Base64.Mime;
        }
    }
}
