package kotlin.text;

/* JADX INFO: compiled from: HexExtensions.kt */
/* JADX INFO: loaded from: classes3.dex */
@kotlin.Metadata(d1 = {"\u0000L\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0012\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0005\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0010\n\n\u0002\b\u0004\u001a \u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0002\u001a@\u0010\u000b\u001a\u00020\t2\u0006\u0010\f\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\tH\u0000\u001a@\u0010\u0013\u001a\u00020\t2\u0006\u0010\u0014\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\t2\u0006\u0010\u000f\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\tH\u0000\u001a \u0010\u0015\u001a\u00020\u00062\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\u00062\u0006\u0010\n\u001a\u00020\tH\u0002\u001a,\u0010\u0016\u001a\u00020\t*\u00020\u00032\u0006\u0010\u0017\u001a\u00020\u00032\u0006\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u0003H\u0002\u001a,\u0010\u001b\u001a\u00020\u001c*\u00020\u00032\u0006\u0010\u001d\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\t2\u0006\u0010\u001e\u001a\u00020\t2\u0006\u0010\u001f\u001a\u00020 H\u0002\u001a\u001c\u0010!\u001a\u00020\t*\u00020\u00032\u0006\u0010\u0018\u001a\u00020\t2\u0006\u0010\u0019\u001a\u00020\tH\u0002\u001a\u0014\u0010\"\u001a\u00020\t*\u00020\u00032\u0006\u0010\u0018\u001a\u00020\tH\u0002\u001a*\u0010#\u001a\u00020$*\u00020\u00032\b\b\u0002\u0010\u001d\u001a\u00020\t2\b\b\u0002\u0010\u0019\u001a\u00020\t2\b\b\u0002\u0010%\u001a\u00020&H\u0003\u001a\u0016\u0010#\u001a\u00020$*\u00020\u00032\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a*\u0010'\u001a\u00020(*\u00020\u00032\b\b\u0002\u0010\u001d\u001a\u00020\t2\b\b\u0002\u0010\u0019\u001a\u00020\t2\b\b\u0002\u0010%\u001a\u00020&H\u0003\u001a\u0016\u0010'\u001a\u00020(*\u00020\u00032\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a*\u0010)\u001a\u00020\t*\u00020\u00032\b\b\u0002\u0010\u001d\u001a\u00020\t2\b\b\u0002\u0010\u0019\u001a\u00020\t2\b\b\u0002\u0010%\u001a\u00020&H\u0003\u001a\u0016\u0010)\u001a\u00020\t*\u00020\u00032\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a*\u0010*\u001a\u00020\u0006*\u00020\u00032\b\b\u0002\u0010\u001d\u001a\u00020\t2\b\b\u0002\u0010\u0019\u001a\u00020\t2\b\b\u0002\u0010%\u001a\u00020&H\u0003\u001a\u0016\u0010*\u001a\u00020\u0006*\u00020\u00032\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a0\u0010+\u001a\u00020\u0006*\u00020\u00032\b\b\u0002\u0010\u001d\u001a\u00020\t2\b\b\u0002\u0010\u0019\u001a\u00020\t2\u0006\u0010%\u001a\u00020&2\u0006\u0010\u001e\u001a\u00020\tH\u0003\u001a*\u0010,\u001a\u00020-*\u00020\u00032\b\b\u0002\u0010\u001d\u001a\u00020\t2\b\b\u0002\u0010\u0019\u001a\u00020\t2\b\b\u0002\u0010%\u001a\u00020&H\u0003\u001a\u0016\u0010,\u001a\u00020-*\u00020\u00032\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a\u0016\u0010.\u001a\u00020\u0003*\u00020$2\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a*\u0010.\u001a\u00020\u0003*\u00020(2\b\b\u0002\u0010\u001d\u001a\u00020\t2\b\b\u0002\u0010\u0019\u001a\u00020\t2\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a\u0016\u0010.\u001a\u00020\u0003*\u00020(2\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a\u0016\u0010.\u001a\u00020\u0003*\u00020\t2\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a\u0016\u0010.\u001a\u00020\u0003*\u00020\u00062\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a\u0016\u0010.\u001a\u00020\u0003*\u00020-2\b\b\u0002\u0010%\u001a\u00020&H\u0007\u001a\u001c\u0010/\u001a\u00020\u0003*\u00020\u00062\u0006\u0010%\u001a\u00020&2\u0006\u00100\u001a\u00020\tH\u0003\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0082\u0004¢\u0006\u0002\n\u0000\"\u000e\u0010\u0002\u001a\u00020\u0003X\u0082T¢\u0006\u0002\n\u0000\"\u000e\u0010\u0004\u001a\u00020\u0003X\u0082T¢\u0006\u0002\n\u0000¨\u00061"}, d2 = {"HEX_DIGITS_TO_DECIMAL", "", "LOWER_CASE_HEX_DIGITS", "", "UPPER_CASE_HEX_DIGITS", "charsPerSet", "", "charsPerElement", "elementsPerSet", "", "elementSeparatorLength", "formattedStringLength", "totalBytes", "bytesPerLine", "bytesPerGroup", "groupSeparatorLength", "byteSeparatorLength", "bytePrefixLength", "byteSuffixLength", "parsedByteArrayMaxSize", "stringLength", "wholeElementsPerSet", "checkContainsAt", "part", com.android.server.am.AssistDataRequester.KEY_RECEIVER_EXTRA_INDEX, "endIndex", "partName", "checkHexLength", "", "startIndex", "maxDigits", "requireMaxLength", "", "checkNewLineAt", "decimalFromHexDigitAt", "hexToByte", "", "format", "Lkotlin/text/HexFormat;", "hexToByteArray", "", "hexToInt", "hexToLong", "hexToLongImpl", "hexToShort", "", "toHexString", "toHexStringImpl", "bits", "kotlin-stdlib"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class HexExtensionsKt {
    private static final int[] HEX_DIGITS_TO_DECIMAL;
    private static final java.lang.String LOWER_CASE_HEX_DIGITS = "0123456789abcdef";
    private static final java.lang.String UPPER_CASE_HEX_DIGITS = "0123456789ABCDEF";

    static {
        int[] $this$HEX_DIGITS_TO_DECIMAL_u24lambda_u242 = new int[128];
        int i = 0;
        for (int i2 = 0; i2 < 128; i2++) {
            $this$HEX_DIGITS_TO_DECIMAL_u24lambda_u242[i2] = -1;
        }
        int index$iv = 0;
        int i3 = 0;
        while (i3 < $this$forEachIndexed$iv.length()) {
            char item$iv = $this$forEachIndexed$iv.charAt(i3);
            $this$HEX_DIGITS_TO_DECIMAL_u24lambda_u242[item$iv] = index$iv;
            i3++;
            index$iv++;
        }
        int index$iv2 = 0;
        while (i < $this$forEachIndexed$iv.length()) {
            char item$iv2 = $this$forEachIndexed$iv.charAt(i);
            $this$HEX_DIGITS_TO_DECIMAL_u24lambda_u242[item$iv2] = index$iv2;
            i++;
            index$iv2++;
        }
        HEX_DIGITS_TO_DECIMAL = $this$HEX_DIGITS_TO_DECIMAL_u24lambda_u242;
    }

    public static final java.lang.String toHexString(byte[] $this$toHexString, kotlin.text.HexFormat format) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$toHexString, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return toHexString($this$toHexString, 0, $this$toHexString.length, format);
    }

    public static /* synthetic */ java.lang.String toHexString$default(byte[] bArr, kotlin.text.HexFormat hexFormat, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return toHexString(bArr, hexFormat);
    }

    public static /* synthetic */ java.lang.String toHexString$default(byte[] bArr, int i, int i2, kotlin.text.HexFormat hexFormat, int i3, java.lang.Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = bArr.length;
        }
        if ((i3 & 4) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return toHexString(bArr, i, i2, hexFormat);
    }

    public static final java.lang.String toHexString(byte[] $this$toHexString, int startIndex, int endIndex, kotlin.text.HexFormat format) {
        byte[] bArr = $this$toHexString;
        int i = endIndex;
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(bArr, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        kotlin.collections.AbstractList.INSTANCE.checkBoundsIndexes$kotlin_stdlib(startIndex, i, bArr.length);
        if (startIndex == i) {
            return "";
        }
        java.lang.String digits = format.getUpperCase() ? UPPER_CASE_HEX_DIGITS : LOWER_CASE_HEX_DIGITS;
        kotlin.text.HexFormat.BytesHexFormat bytesFormat = format.getBytes();
        int bytesPerLine = bytesFormat.getBytesPerLine();
        int bytesPerGroup = bytesFormat.getBytesPerGroup();
        java.lang.String bytePrefix = bytesFormat.getBytePrefix();
        java.lang.String byteSuffix = bytesFormat.getByteSuffix();
        java.lang.String byteSeparator = bytesFormat.getByteSeparator();
        java.lang.String groupSeparator = bytesFormat.getGroupSeparator();
        int formatLength = formattedStringLength(i - startIndex, bytesPerLine, bytesPerGroup, groupSeparator.length(), byteSeparator.length(), bytePrefix.length(), byteSuffix.length());
        int indexInLine = 0;
        int indexInGroup = 0;
        java.lang.StringBuilder $this$toHexString_u24lambda_u243 = new java.lang.StringBuilder(formatLength);
        int i2 = startIndex;
        while (true) {
            if (i2 >= i) {
                break;
            }
            int i3 = bArr[i2] & 255;
            if (indexInLine == bytesPerLine) {
                $this$toHexString_u24lambda_u243.append('\n');
                indexInLine = 0;
                indexInGroup = 0;
            } else if (indexInGroup == bytesPerGroup) {
                $this$toHexString_u24lambda_u243.append(groupSeparator);
                indexInGroup = 0;
            }
            if (indexInGroup != 0) {
                $this$toHexString_u24lambda_u243.append(byteSeparator);
            }
            $this$toHexString_u24lambda_u243.append(bytePrefix);
            $this$toHexString_u24lambda_u243.append(digits.charAt(i3 >> 4));
            $this$toHexString_u24lambda_u243.append(digits.charAt(i3 & 15));
            $this$toHexString_u24lambda_u243.append(byteSuffix);
            indexInGroup++;
            indexInLine++;
            i2++;
            bArr = $this$toHexString;
            i = endIndex;
        }
        if (formatLength == $this$toHexString_u24lambda_u243.length()) {
            java.lang.String string = $this$toHexString_u24lambda_u243.toString();
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
            return string;
        }
        throw new java.lang.IllegalStateException("Check failed.".toString());
    }

    public static final int formattedStringLength(int totalBytes, int bytesPerLine, int bytesPerGroup, int groupSeparatorLength, int byteSeparatorLength, int bytePrefixLength, int byteSuffixLength) {
        if (!(totalBytes > 0)) {
            throw new java.lang.IllegalArgumentException("Failed requirement.".toString());
        }
        int lineSeparators = (totalBytes - 1) / bytesPerLine;
        int groupSeparatorsPerLine = (bytesPerLine - 1) / bytesPerGroup;
        int it = totalBytes % bytesPerLine;
        if (it == 0) {
            it = bytesPerLine;
        }
        int groupSeparatorsInLastLine = (it - 1) / bytesPerGroup;
        int groupSeparators = (lineSeparators * groupSeparatorsPerLine) + groupSeparatorsInLastLine;
        int byteSeparators = ((totalBytes - 1) - lineSeparators) - groupSeparators;
        long totalLength = ((long) lineSeparators) + (((long) groupSeparators) * ((long) groupSeparatorLength)) + (((long) byteSeparators) * ((long) byteSeparatorLength)) + (((long) totalBytes) * (((long) bytePrefixLength) + 2 + ((long) byteSuffixLength)));
        if (kotlin.ranges.RangesKt.intRangeContains((kotlin.ranges.ClosedRange<java.lang.Integer>) new kotlin.ranges.IntRange(0, Integer.MAX_VALUE), totalLength)) {
            return (int) totalLength;
        }
        throw new java.lang.IllegalArgumentException("The resulting string length is too big: " + ((java.lang.Object) kotlin.ULong.m11527toStringimpl(kotlin.ULong.m11481constructorimpl(totalLength))));
    }

    public static final byte[] hexToByteArray(java.lang.String $this$hexToByteArray, kotlin.text.HexFormat format) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexToByteArray, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return hexToByteArray($this$hexToByteArray, 0, $this$hexToByteArray.length(), format);
    }

    public static /* synthetic */ byte[] hexToByteArray$default(java.lang.String str, kotlin.text.HexFormat hexFormat, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return hexToByteArray(str, hexFormat);
    }

    static /* synthetic */ byte[] hexToByteArray$default(java.lang.String str, int i, int i2, kotlin.text.HexFormat hexFormat, int i3, java.lang.Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        if ((i3 & 4) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return hexToByteArray(str, i, i2, hexFormat);
    }

    private static final byte[] hexToByteArray(java.lang.String $this$hexToByteArray, int startIndex, int endIndex, kotlin.text.HexFormat format) {
        kotlin.collections.AbstractList.INSTANCE.checkBoundsIndexes$kotlin_stdlib(startIndex, endIndex, $this$hexToByteArray.length());
        if (startIndex == endIndex) {
            return new byte[0];
        }
        kotlin.text.HexFormat.BytesHexFormat bytesFormat = format.getBytes();
        int bytesPerLine = bytesFormat.getBytesPerLine();
        int bytesPerGroup = bytesFormat.getBytesPerGroup();
        java.lang.String bytePrefix = bytesFormat.getBytePrefix();
        java.lang.String byteSuffix = bytesFormat.getByteSuffix();
        java.lang.String byteSeparator = bytesFormat.getByteSeparator();
        java.lang.String groupSeparator = bytesFormat.getGroupSeparator();
        java.lang.String groupSeparator2 = groupSeparator;
        int resultCapacity = parsedByteArrayMaxSize(endIndex - startIndex, bytesPerLine, bytesPerGroup, groupSeparator.length(), byteSeparator.length(), bytePrefix.length(), byteSuffix.length());
        byte[] result = new byte[resultCapacity];
        int i = startIndex;
        int byteIndex = 0;
        int indexInLine = 0;
        int indexInGroup = 0;
        while (i < endIndex) {
            if (indexInLine == bytesPerLine) {
                i = checkNewLineAt($this$hexToByteArray, i, endIndex);
                indexInLine = 0;
                indexInGroup = 0;
            } else if (indexInGroup == bytesPerGroup) {
                i = checkContainsAt($this$hexToByteArray, groupSeparator2, i, endIndex, "group separator");
                indexInGroup = 0;
            } else if (indexInGroup != 0) {
                i = checkContainsAt($this$hexToByteArray, byteSeparator, i, endIndex, "byte separator");
            }
            indexInLine++;
            indexInGroup++;
            int i2 = checkContainsAt($this$hexToByteArray, bytePrefix, i, endIndex, "byte prefix");
            kotlin.text.HexFormat.BytesHexFormat bytesFormat2 = bytesFormat;
            checkHexLength($this$hexToByteArray, i2, kotlin.ranges.RangesKt.coerceAtMost(i2 + 2, endIndex), 2, true);
            int i3 = i2 + 1;
            result[byteIndex] = (byte) (decimalFromHexDigitAt($this$hexToByteArray, i3) | (decimalFromHexDigitAt($this$hexToByteArray, i2) << 4));
            i = checkContainsAt($this$hexToByteArray, byteSuffix, i3 + 1, endIndex, "byte suffix");
            byteIndex++;
            groupSeparator2 = groupSeparator2;
            bytesFormat = bytesFormat2;
        }
        if (byteIndex == result.length) {
            return result;
        }
        byte[] bArrCopyOf = java.util.Arrays.copyOf(result, byteIndex);
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(bArrCopyOf, "copyOf(...)");
        return bArrCopyOf;
    }

    public static final int parsedByteArrayMaxSize(int stringLength, int bytesPerLine, int bytesPerGroup, int groupSeparatorLength, int byteSeparatorLength, int bytePrefixLength, int byteSuffixLength) {
        long jCharsPerSet;
        if (!(stringLength > 0)) {
            throw new java.lang.IllegalArgumentException("Failed requirement.".toString());
        }
        long charsPerByte = ((long) bytePrefixLength) + 2 + ((long) byteSuffixLength);
        long charsPerGroup = charsPerSet(charsPerByte, bytesPerGroup, byteSeparatorLength);
        if (bytesPerLine <= bytesPerGroup) {
            jCharsPerSet = charsPerSet(charsPerByte, bytesPerLine, byteSeparatorLength);
        } else {
            int groupsPerLine = bytesPerLine / bytesPerGroup;
            long result = charsPerSet(charsPerGroup, groupsPerLine, groupSeparatorLength);
            int bytesPerLastGroupInLine = bytesPerLine % bytesPerGroup;
            if (bytesPerLastGroupInLine != 0) {
                result = result + ((long) groupSeparatorLength) + charsPerSet(charsPerByte, bytesPerLastGroupInLine, byteSeparatorLength);
            }
            jCharsPerSet = result;
        }
        long charsPerLine = jCharsPerSet;
        long numberOfChars = stringLength;
        long wholeLines = wholeElementsPerSet(numberOfChars, charsPerLine, 1);
        long numberOfChars2 = numberOfChars - ((charsPerLine + 1) * wholeLines);
        long wholeGroupsInLastLine = wholeElementsPerSet(numberOfChars2, charsPerGroup, groupSeparatorLength);
        long numberOfChars3 = numberOfChars2 - ((((long) groupSeparatorLength) + charsPerGroup) * wholeGroupsInLastLine);
        long wholeBytesInLastGroup = wholeElementsPerSet(numberOfChars3, charsPerByte, byteSeparatorLength);
        int spare = numberOfChars3 - ((((long) byteSeparatorLength) + charsPerByte) * wholeBytesInLastGroup) > 0 ? 1 : 0;
        return (int) ((((long) bytesPerLine) * wholeLines) + (((long) bytesPerGroup) * wholeGroupsInLastLine) + wholeBytesInLastGroup + ((long) spare));
    }

    private static final long charsPerSet(long charsPerElement, int elementsPerSet, int elementSeparatorLength) {
        if (!(elementsPerSet > 0)) {
            throw new java.lang.IllegalArgumentException("Failed requirement.".toString());
        }
        return (((long) elementsPerSet) * charsPerElement) + (((long) elementSeparatorLength) * (((long) elementsPerSet) - 1));
    }

    private static final long wholeElementsPerSet(long charsPerSet, long charsPerElement, int elementSeparatorLength) {
        if (charsPerSet <= 0 || charsPerElement <= 0) {
            return 0L;
        }
        return (((long) elementSeparatorLength) + charsPerSet) / (((long) elementSeparatorLength) + charsPerElement);
    }

    private static final int checkNewLineAt(java.lang.String $this$checkNewLineAt, int index, int endIndex) {
        if ($this$checkNewLineAt.charAt(index) == '\r') {
            return (index + 1 >= endIndex || $this$checkNewLineAt.charAt(index + 1) != '\n') ? index + 1 : index + 2;
        }
        if ($this$checkNewLineAt.charAt(index) == '\n') {
            return index + 1;
        }
        throw new java.lang.NumberFormatException("Expected a new line at index " + index + ", but was " + $this$checkNewLineAt.charAt(index));
    }

    public static final java.lang.String toHexString(byte $this$toHexString, kotlin.text.HexFormat format) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return toHexStringImpl($this$toHexString, format, 8);
    }

    public static /* synthetic */ java.lang.String toHexString$default(byte b, kotlin.text.HexFormat hexFormat, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return toHexString(b, hexFormat);
    }

    public static final byte hexToByte(java.lang.String $this$hexToByte, kotlin.text.HexFormat format) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexToByte, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return hexToByte($this$hexToByte, 0, $this$hexToByte.length(), format);
    }

    public static /* synthetic */ byte hexToByte$default(java.lang.String str, kotlin.text.HexFormat hexFormat, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return hexToByte(str, hexFormat);
    }

    static /* synthetic */ byte hexToByte$default(java.lang.String str, int i, int i2, kotlin.text.HexFormat hexFormat, int i3, java.lang.Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        if ((i3 & 4) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return hexToByte(str, i, i2, hexFormat);
    }

    private static final byte hexToByte(java.lang.String $this$hexToByte, int startIndex, int endIndex, kotlin.text.HexFormat format) {
        return (byte) hexToLongImpl($this$hexToByte, startIndex, endIndex, format, 2);
    }

    public static final java.lang.String toHexString(short $this$toHexString, kotlin.text.HexFormat format) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return toHexStringImpl($this$toHexString, format, 16);
    }

    public static /* synthetic */ java.lang.String toHexString$default(short s, kotlin.text.HexFormat hexFormat, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return toHexString(s, hexFormat);
    }

    public static final short hexToShort(java.lang.String $this$hexToShort, kotlin.text.HexFormat format) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexToShort, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return hexToShort($this$hexToShort, 0, $this$hexToShort.length(), format);
    }

    public static /* synthetic */ short hexToShort$default(java.lang.String str, kotlin.text.HexFormat hexFormat, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return hexToShort(str, hexFormat);
    }

    static /* synthetic */ short hexToShort$default(java.lang.String str, int i, int i2, kotlin.text.HexFormat hexFormat, int i3, java.lang.Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        if ((i3 & 4) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return hexToShort(str, i, i2, hexFormat);
    }

    private static final short hexToShort(java.lang.String $this$hexToShort, int startIndex, int endIndex, kotlin.text.HexFormat format) {
        return (short) hexToLongImpl($this$hexToShort, startIndex, endIndex, format, 4);
    }

    public static final java.lang.String toHexString(int $this$toHexString, kotlin.text.HexFormat format) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return toHexStringImpl($this$toHexString, format, 32);
    }

    public static /* synthetic */ java.lang.String toHexString$default(int i, kotlin.text.HexFormat hexFormat, int i2, java.lang.Object obj) {
        if ((i2 & 1) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return toHexString(i, hexFormat);
    }

    public static final int hexToInt(java.lang.String $this$hexToInt, kotlin.text.HexFormat format) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexToInt, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return hexToInt($this$hexToInt, 0, $this$hexToInt.length(), format);
    }

    public static /* synthetic */ int hexToInt$default(java.lang.String str, kotlin.text.HexFormat hexFormat, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return hexToInt(str, hexFormat);
    }

    static /* synthetic */ int hexToInt$default(java.lang.String str, int i, int i2, kotlin.text.HexFormat hexFormat, int i3, java.lang.Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        if ((i3 & 4) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return hexToInt(str, i, i2, hexFormat);
    }

    private static final int hexToInt(java.lang.String $this$hexToInt, int startIndex, int endIndex, kotlin.text.HexFormat format) {
        return (int) hexToLongImpl($this$hexToInt, startIndex, endIndex, format, 8);
    }

    public static final java.lang.String toHexString(long $this$toHexString, kotlin.text.HexFormat format) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return toHexStringImpl($this$toHexString, format, 64);
    }

    public static /* synthetic */ java.lang.String toHexString$default(long j, kotlin.text.HexFormat hexFormat, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return toHexString(j, hexFormat);
    }

    public static final long hexToLong(java.lang.String $this$hexToLong, kotlin.text.HexFormat format) {
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter($this$hexToLong, "<this>");
        kotlin.jvm.internal.Intrinsics.checkNotNullParameter(format, "format");
        return hexToLong($this$hexToLong, 0, $this$hexToLong.length(), format);
    }

    public static /* synthetic */ long hexToLong$default(java.lang.String str, kotlin.text.HexFormat hexFormat, int i, java.lang.Object obj) {
        if ((i & 1) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return hexToLong(str, hexFormat);
    }

    static /* synthetic */ long hexToLong$default(java.lang.String str, int i, int i2, kotlin.text.HexFormat hexFormat, int i3, java.lang.Object obj) {
        if ((i3 & 1) != 0) {
            i = 0;
        }
        if ((i3 & 2) != 0) {
            i2 = str.length();
        }
        if ((i3 & 4) != 0) {
            hexFormat = kotlin.text.HexFormat.INSTANCE.getDefault();
        }
        return hexToLong(str, i, i2, hexFormat);
    }

    private static final long hexToLong(java.lang.String $this$hexToLong, int startIndex, int endIndex, kotlin.text.HexFormat format) {
        return hexToLongImpl($this$hexToLong, startIndex, endIndex, format, 16);
    }

    private static final java.lang.String toHexStringImpl(long $this$toHexStringImpl, kotlin.text.HexFormat format, int bits) {
        if (!((bits & 3) == 0)) {
            throw new java.lang.IllegalArgumentException("Failed requirement.".toString());
        }
        java.lang.String digits = format.getUpperCase() ? UPPER_CASE_HEX_DIGITS : LOWER_CASE_HEX_DIGITS;
        java.lang.String prefix = format.getNumber().getPrefix();
        java.lang.String suffix = format.getNumber().getSuffix();
        int formatLength = prefix.length() + (bits >> 2) + suffix.length();
        boolean removeZeros = format.getNumber().getRemoveLeadingZeros();
        java.lang.StringBuilder $this$toHexStringImpl_u24lambda_u246 = new java.lang.StringBuilder(formatLength);
        $this$toHexStringImpl_u24lambda_u246.append(prefix);
        int shift = bits;
        while (shift > 0) {
            shift -= 4;
            int decimal = (int) (($this$toHexStringImpl >> shift) & 15);
            removeZeros = removeZeros && decimal == 0 && shift > 0;
            if (!removeZeros) {
                $this$toHexStringImpl_u24lambda_u246.append(digits.charAt(decimal));
            }
        }
        $this$toHexStringImpl_u24lambda_u246.append(suffix);
        java.lang.String string = $this$toHexStringImpl_u24lambda_u246.toString();
        kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(string, "toString(...)");
        return string;
    }

    static /* synthetic */ long hexToLongImpl$default(java.lang.String str, int i, int i2, kotlin.text.HexFormat hexFormat, int i3, int i4, java.lang.Object obj) {
        if ((i4 & 1) != 0) {
            i = 0;
        }
        if ((i4 & 2) != 0) {
            i2 = str.length();
        }
        return hexToLongImpl(str, i, i2, hexFormat, i3);
    }

    private static final long hexToLongImpl(java.lang.String $this$hexToLongImpl, int startIndex, int endIndex, kotlin.text.HexFormat format, int maxDigits) {
        kotlin.collections.AbstractList.INSTANCE.checkBoundsIndexes$kotlin_stdlib(startIndex, endIndex, $this$hexToLongImpl.length());
        java.lang.String prefix = format.getNumber().getPrefix();
        java.lang.String suffix = format.getNumber().getSuffix();
        if (prefix.length() + suffix.length() >= endIndex - startIndex) {
            java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("Expected a hexadecimal number with prefix \"").append(prefix).append("\" and suffix \"").append(suffix).append("\", but was ");
            kotlin.jvm.internal.Intrinsics.checkNotNull($this$hexToLongImpl, "null cannot be cast to non-null type java.lang.String");
            java.lang.String strSubstring = $this$hexToLongImpl.substring(startIndex, endIndex);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            throw new java.lang.NumberFormatException(sbAppend.append(strSubstring).toString());
        }
        int digitsStartIndex = checkContainsAt($this$hexToLongImpl, prefix, startIndex, endIndex, "prefix");
        int digitsEndIndex = endIndex - suffix.length();
        checkContainsAt($this$hexToLongImpl, suffix, digitsEndIndex, endIndex, "suffix");
        checkHexLength($this$hexToLongImpl, digitsStartIndex, digitsEndIndex, maxDigits, false);
        long result = 0;
        for (int i = digitsStartIndex; i < digitsEndIndex; i++) {
            result = (result << 4) | ((long) decimalFromHexDigitAt($this$hexToLongImpl, i));
        }
        return result;
    }

    private static final int checkContainsAt(java.lang.String $this$checkContainsAt, java.lang.String part, int index, int endIndex, java.lang.String partName) {
        int end = part.length() + index;
        if (end > endIndex || !kotlin.text.StringsKt.regionMatches($this$checkContainsAt, index, part, 0, part.length(), true)) {
            java.lang.StringBuilder sbAppend = new java.lang.StringBuilder().append("Expected ").append(partName).append(" \"").append(part).append("\" at index ").append(index).append(", but was ");
            int iCoerceAtMost = kotlin.ranges.RangesKt.coerceAtMost(end, endIndex);
            kotlin.jvm.internal.Intrinsics.checkNotNull($this$checkContainsAt, "null cannot be cast to non-null type java.lang.String");
            java.lang.String strSubstring = $this$checkContainsAt.substring(index, iCoerceAtMost);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(strSubstring, "substring(...)");
            throw new java.lang.NumberFormatException(sbAppend.append(strSubstring).toString());
        }
        return end;
    }

    private static final void checkHexLength(java.lang.String $this$checkHexLength, int startIndex, int endIndex, int maxDigits, boolean requireMaxLength) {
        int digitsLength = endIndex - startIndex;
        boolean isCorrectLength = true;
        if (!requireMaxLength ? digitsLength > maxDigits : digitsLength != maxDigits) {
            isCorrectLength = false;
        }
        if (!isCorrectLength) {
            java.lang.String specifier = requireMaxLength ? "exactly" : "at most";
            kotlin.jvm.internal.Intrinsics.checkNotNull($this$checkHexLength, "null cannot be cast to non-null type java.lang.String");
            java.lang.String substring = $this$checkHexLength.substring(startIndex, endIndex);
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(substring, "substring(...)");
            throw new java.lang.NumberFormatException("Expected " + specifier + ' ' + maxDigits + " hexadecimal digits at index " + startIndex + ", but was " + substring + " of length " + digitsLength);
        }
    }

    private static final int decimalFromHexDigitAt(java.lang.String $this$decimalFromHexDigitAt, int index) {
        int code = $this$decimalFromHexDigitAt.charAt(index);
        if (code > 127 || HEX_DIGITS_TO_DECIMAL[code] < 0) {
            throw new java.lang.NumberFormatException("Expected a hexadecimal digit at index " + index + ", but was " + $this$decimalFromHexDigitAt.charAt(index));
        }
        return HEX_DIGITS_TO_DECIMAL[code];
    }
}
